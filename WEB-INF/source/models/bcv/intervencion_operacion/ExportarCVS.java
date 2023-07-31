package models.bcv.intervencion_operacion;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * @author eel
 * 
 */
public class ExportarCVS extends ExportableOutputStream {

	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		long idUnidad;
		String statusP = null;
		String statusE = null;
		String Tipo = null;
		String fecha   = null;
		statusP            = _record.getValue("statusp");
		statusE            = _record.getValue("statuse");
		Tipo            = _record.getValue("tipo");
		fecha              = (String) _record.getValue("fecha");


		Integer clienteID        = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));

		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_EXP_CSV1.getNombreAccion())){ //DEMANDA
			
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false, true, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
			
			_ordenes = ordenesCrucesDAO.getDataSet();
			
			try {//Se le da nombre e inicio al documento
				
				registrarInicio("ordenes.csv");
				
				
				if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){//FUE APROBADA
					crearCabecera("OPERACION;NOMBRE CLIENTE ;CEDULA O RIF;MONTO DIVISAS; EMAIL;ESTATUS;");
				
				}else {
					crearCabecera("OPERACION;NOMBRE CLIENTE ;CEDULA O RIF;MONTO DIVISAS; EMAIL;ESTATUS;");
				} 				
				 
				escribir("\r\n");
				while (_ordenes.next()) {					
					registroProcesado++;
					escribir(_ordenes.getValue("movimiento")==null?" ;":_ordenes.getValue("movimiento")+";");
					escribir(_ordenes.getValue("NOM_CLIEN")==null?" ;":_ordenes.getValue("NOM_CLIEN")+";");
					escribir(_ordenes.getValue("NRO_CED_RIF")==null?" ;":_ordenes.getValue("NACIONALIDAD")+""+_ordenes.getValue("NRO_CED_RIF")+";");
					escribir(_ordenes.getValue("MTO_DIVISAS")==null?" ;":_ordenes.getValue("MTO_DIVISAS")+";");
//					escribir(_ordenes.getValue("CTA_CLIEN")==null?" ;":_ordenes.getValue("CTA_CLIEN").replaceAll("\n", "")+";");
					escribir(_ordenes.getValue("EMAIL_CLIEN")==null?" ;":_ordenes.getValue("EMAIL_CLIEN")+";");
					escribir(_ordenes.getValue("Estatus")==null?" ;":_ordenes.getValue("Estatus")+";");					


					escribir("\r\n");
				} // fin while
				registrarFin();
				obtenerSalida();
			} catch (Exception e) {
				_record.addError("Nombre","Error en la exportaci�n del Excel" + "Error:"  + e.getMessage());
				Logger.error(this,"Error en la exportaci�n del Excel",e);
			} 
		}
	}// fin execute

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
		
	}
	
	
	

}// Fin Clase