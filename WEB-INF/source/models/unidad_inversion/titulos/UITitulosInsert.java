package models.unidad_inversion.titulos;

import java.math.BigDecimal;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de Ingreso de una Asociacion entre una Unidad de Inversion y Titulos y los registra en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UITitulosInsert extends AbstractModel implements UnidadInversionConstantes {
	/**
	 * Clase que encapsula la validacion de una unidd de inversion
	 */
	private UITitulosValidar classValidar = new UITitulosValidar();
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada
		idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	

		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA) || dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_PUBLICADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}		
		
		int ca = 0;
		// campos obligatorios recuperados de la pagina
		String [] strIdTitulo = _req.getParameterValues("idTitulo");
		for (int i=0; i< strIdTitulo.length-1; i++) {
			if (!strIdTitulo[i].equals("-1")) {
				++ca;
			}
		}

		if (ca == 0) {
			return;
		}
		String [] strPorcentaje = _req.getParameterValues("porcUITitulo");
		String [] strValorNominal = _req.getParameterValues("valorNominal");	
		String [] strIdMoneda = _req.getParameterValues("idMoneda");	
		
		String [] idsTitulos = new String [ca];
		BigDecimal [] porcentajeNuevo = new BigDecimal [ca];
		BigDecimal [] valorNominalTitulo = new BigDecimal [ca];
		String [] idsMoneda = new String [ca];
		
		int j=0;
		for (int i = 0; i < strIdTitulo.length-1; i++) {
			if (strIdTitulo[i].equals("-1")) {
				continue;
			}
			idsTitulos[j] = strIdTitulo[i];
			porcentajeNuevo[j] = new BigDecimal(strPorcentaje[i].replace(',', '.'));
			valorNominalTitulo[j] = new BigDecimal(strValorNominal[i].replace(',', '.'));
			idsMoneda  [j] = strIdMoneda [j]; 
			++j;
		}
		
		// Aplicar la persistencia
		UITitulosDAO boTitulos = new UITitulosDAO(_dso);
		boTitulos.setIdsTitulos(idsTitulos);
		boTitulos.setPorcentajeNuevo(porcentajeNuevo);
		boTitulos.setValorNominalTitulo(valorNominalTitulo);
		boTitulos.setIdsMoneda(idsMoneda);
		boTitulos.insertar(idUnidadInversion, dsUI.getValue("moneda_id"), new BigDecimal(dsUI.getValue("undinv_tasa_cambio")));
	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "entry=insert&idUnidadInversion=" + idUnidadInversion;
	}
	

	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		
		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error
		if (!flag) 	{
			return flag;
		}

		String [] strIdTitulo = _req.getParameterValues("idTitulo");
		String [] strPorcentajeActual = _req.getParameterValues("porcUITitulo");
		
		flag = classValidar.isValid(_record, strIdTitulo, strPorcentajeActual); 
		return flag;
	}
}
