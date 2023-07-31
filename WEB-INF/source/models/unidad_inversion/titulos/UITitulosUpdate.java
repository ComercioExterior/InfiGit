package models.unidad_inversion.titulos;

import java.math.BigDecimal;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Util;

import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de actualizacion de las relaciones entre Unidad de Inversion y los Titulos y aplica la persistencia en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UITitulosUpdate extends AbstractModel implements UnidadInversionConstantes {
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
		idUnidadInversion = Long.parseLong(_record.getValue("idUnidadInversion"));
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	

		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		
		if (!(dsUI.getValue("tipo_producto_id").equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)||dsUI.getValue("tipo_producto_id").equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL))
				&&(dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA) || dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_PUBLICADA))){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		// campos obligatorios recuperados de la pagina
		String [] strIdTitulo = _req.getParameterValues("idTitulo");
		String [] strPorcentajeAnterior = _req.getParameterValues("porcUITituloAnterior");
		String [] strPorcentajeActual = _req.getParameterValues("porcUITitulo");
		String [] strValorNominal = _req.getParameterValues("valorNominal");
		String [] strValorNominalAnterior = _req.getParameterValues("valorNominalAnterior");
		String [] strIdMoneda = _req.getParameterValues("idMoneda");
		String [] strImpDebBanActual = _req.getParameterValues("idb");
		String [] strImpDebBanAnterior = _req.getParameterValues("idbAnterior");
		String [] strControlDisponible = _req.getParameterValues("controlDisponible");
		String [] strControlDisponibleAnterior = _req.getParameterValues("controlDisponibleAnterior");
		
		
		// Verificar si hay modificaciones
		int ca = 0;
		for (int i=0; i<strIdTitulo.length-1; i++) {
			strPorcentajeAnterior[i] = Util.replace(strPorcentajeAnterior[i],",",".");
			strPorcentajeActual[i] = Util.replace(strPorcentajeActual[i],",",".");
			double pa = new Double(strPorcentajeAnterior[i]).doubleValue();
			double pn = new Double(strPorcentajeActual[i]).doubleValue();
			strValorNominal[i] = Util.replace(strValorNominal[i],".","");
			strValorNominal[i] = Util.replace(strValorNominal[i],",",".");
			strValorNominalAnterior[i] = Util.replace(strValorNominalAnterior[i],".","");
			strValorNominalAnterior[i] = Util.replace(strValorNominalAnterior[i],",",".");
			double vna = new Double(strValorNominal[i]).doubleValue();
			double vnn = new Double(strValorNominalAnterior[i]).doubleValue();
			String idbAnterior = strImpDebBanAnterior[i];
			String idbActual = strImpDebBanActual[i];
			String controlDisponibleAnterior =  strControlDisponibleAnterior[i];
			String controlDisponibleActual = strControlDisponible[i];
			
			if ((pa != pn)||(vna != vnn)||(!idbAnterior.equals(idbActual)||(!controlDisponibleAnterior.equals(controlDisponibleActual)))){
				++ca;
			}
		}
		String [] idsTitulos = new String [ca];
		BigDecimal [] porcentajeNuevo = new BigDecimal [ca];	
		BigDecimal [] valorNominalTitulo = new BigDecimal [ca];
		String [] idsMoneda = new String [ca];
		BigDecimal [] ImpDebBan = new BigDecimal [ca];
		int [] controlDisp = new int [ca] ;
		
		int j=0;
		for (int i = 0; i < strIdTitulo.length-1; i++) {
			
			double pa = new Double(strPorcentajeAnterior[i]).doubleValue();
			double pn = new Double(strPorcentajeActual[i]).doubleValue();
			
			double vna = new Double(strValorNominal[i]).doubleValue();
			double vnn = new Double(strValorNominalAnterior[i]).doubleValue();
			
			String idbAnterior = strImpDebBanAnterior[i];
			String idbActual = strImpDebBanActual[i];
			String controlDisponibleAnterior = strControlDisponibleAnterior[i];
			String controlDisponibleActual = strControlDisponible[i];
			
			if ((pa == pn)&& (vna == vnn)&&(idbAnterior.equals(idbActual))&&(controlDisponibleAnterior.equals(controlDisponibleActual))){
				continue;
			}else {
				idsTitulos[j] = strIdTitulo[i];
				porcentajeNuevo[j] = new BigDecimal(strPorcentajeActual[i].replace(',', '.'));
				valorNominalTitulo[j] = new BigDecimal(strValorNominal[i].replace(',', '.'));
				idsMoneda  [j] = strIdMoneda [i];
				ImpDebBan [j] = new BigDecimal(strImpDebBanActual[i]);
				controlDisp [j] = Integer.parseInt((strControlDisponible[i]));
				++j;
			}
		}
		
		// Aplicar la persistencia
		UITitulosDAO boTitulos = new UITitulosDAO(_dso);
		boTitulos.setIdsTitulos(idsTitulos);
		boTitulos.setPorcentajeNuevo(porcentajeNuevo);
		boTitulos.setValorNominalTitulo(valorNominalTitulo);
		boTitulos.setIdsMoneda(idsMoneda);
		boTitulos.setImpDebBan(ImpDebBan);
		boTitulos.setIndControlDisponible(controlDisp);
		boTitulos.modificar(idUnidadInversion, dsUI.getValue("moneda_id"), new BigDecimal(dsUI.getValue("undinv_tasa_cambio")));
		
		// Buscar todos los titulos para recalcular la Unidad de Inversion
		boUI.listarTitulosPorUI(idUnidadInversion);	
		DataSet dsTitulos = boUI.getDataSet();
		
		BigDecimal umiUnidadInversion = new BigDecimal(0);
		while(dsTitulos.next()) {
			umiUnidadInversion = umiUnidadInversion.add(new BigDecimal(dsTitulos.getValue("uititu_valor_equivalente")));
		}
	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "entry=update&idUnidadInversion=" + idUnidadInversion;
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
		_record.setValue("totalPorcentaje", "0");
		flag = classValidar.isValid(_record, strIdTitulo, strPorcentajeActual); 
		
		return flag;
	}
}
