package models.unidad_inversion.titulos;

import java.math.BigDecimal;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class UITitulosSubastaInsert extends AbstractModel implements UnidadInversionConstantes {
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
					
		boUI.listarTitulosPorUI(idUnidadInversion,dsUI.getValue("tipo_producto_id"));
		DataSet dsUITit = boUI.getDataSet();
		if(dsUITit.count()>0){
			_record.addError("Para su informacion", "La Unidad de Inversion ya tiene configurados los datos de la subasta");
			return;
		}
		
		
		String [] idSubasta = {_req.getParameter("nombre")};
		BigDecimal [] valorNominalSubasta={new BigDecimal(_req.getParameter("monto"))};	
		BigDecimal [] porcentaje = {new BigDecimal(_req.getParameter("porcentaje"))};
		String [] idsMoneda = {dsUI.getValue("moneda_id")};
		BigDecimal [] impDebBan = {new BigDecimal(_req.getParameter("idb"))};
		int [] indControlDisponible = {Integer.parseInt(_req.getParameter("monto_disponible"))};
		
		// Aplicar la persistencia
		UITitulosDAO boTitulos = new UITitulosDAO(_dso);
		boTitulos.setIdsTitulos(idSubasta);		
		boTitulos.setValorNominalTitulo(valorNominalSubasta);
		boTitulos.setPorcentajeNuevo(porcentaje);
		boTitulos.setIdsMoneda(idsMoneda);
		boTitulos.setImpDebBan(impDebBan);
		boTitulos.setIndControlDisponible(indControlDisponible);
		
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
				
		if (!flag) 	{
			return flag;
		}
		
		idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
		UITitulosDAO boTitulos = new UITitulosDAO(_dso);
		if(boTitulos.consultarUITitulo(_req.getParameter("nombre"))>0){
			_record.addError("Para su informacion", "Existe un título registrado con el mismo nombre");
			flag=false;
		}
		
		
		return flag;
	}
}
