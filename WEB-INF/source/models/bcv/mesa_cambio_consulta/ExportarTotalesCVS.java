package models.bcv.mesa_cambio_consulta;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * @author eel
 * 
 */
public class ExportarTotalesCVS extends ExportableOutputStream {

	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
//		System.out.println("paso reportetotales");
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
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_EXP_CSV_TOTALES.getNombreAccion())){ //DEMANDA
			
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(true, false, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
			
			_ordenes = ordenesCrucesDAO.getDataSet();
			
			try {//Se le da nombre e inicio al documento	
				registrarInicio("ordenesTotales.csv");
				if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){ //FUE APROBADA
					crearCabecera("MONEDA;MONTO TOTALES ;CANTIDAD OPERACIONES;TIPO OPERACIONES;");
				}else {
					crearCabecera("MONEDA;MONTO TOTALES ;CANTIDAD OPERACIONES;TIPO OPERACIONES;");
				} 				
				 
				escribir("\r\n");
	
				while (_ordenes.next()) {					
					registroProcesado++;

				
					//se agregan uno por uno los registros
					escribir(_ordenes.getValue("MONEDA")==null?" ;":_ordenes.getValue("MONEDA")+";");
					escribir(_ordenes.getValue("monto_operacion")==null?" ;":_ordenes.getValue("monto_operacion")+";");
					escribir(_ordenes.getValue("cantidad_operaciones")==null?" ;":_ordenes.getValue("cantidad_operaciones")+";");//////////////////////
					escribir(_ordenes.getValue("movimiento")==null?" ;":_ordenes.getValue("movimiento").replaceAll("\n", "")+";");
					
			

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