package models.bcv.mesa_cambio_consulta;



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


//		Integer clienteID        = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));

		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MESA_CAMBIO_EXPORTAR.getNombreAccion())){ //DEMANDA
			
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarMesaDeCambio(false, true, getNumeroDePagina(),getPageSize(), true, fecha,statusE,Tipo,"");
			
			_ordenes = ordenesCrucesDAO.getDataSet();
			
			try {//Se le da nombre e inicio al documento
				
				registrarInicio("ordenesMesaCambio.csv");
				
				
				if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){//FUE APROBADA
					crearCabecera("OPERACION;NOMBRE CLIENTE ;RIF;CODIGO;ESTATUS;TASA DE CAMBIO;MONTO");
				
				}else {
					crearCabecera("OPERACION;NOMBRE CLIENTE ;RIF;CODIGO;ESTATUS;TASA DE CAMBIO;MONTO");
				} 				
				 
				escribir("\r\n");
				while (_ordenes.next()) {					
					registroProcesado++;
					escribir(_ordenes.getValue("movimiento")==null?" ;":_ordenes.getValue("movimiento")+";");
					escribir(_ordenes.getValue("NOM_CLIEN")==null?" ;":_ordenes.getValue("NOM_CLIEN")+";");
					escribir(_ordenes.getValue("RIF_CLIENTE")==null?" ;":_ordenes.getValue("RIF_CLIENTE")+";");
					escribir(_ordenes.getValue("ID_BCV")==null?" ;":_ordenes.getValue("ID_BCV")+";");
					escribir(_ordenes.getValue("Estatus")==null?" ;":_ordenes.getValue("Estatus")+";");
					escribir(_ordenes.getValue("TASA_CAMBIO")==null?" ;":_ordenes.getValue("TASA_CAMBIO")+";");
					escribir(_ordenes.getValue("MTO_DIVISAS")==null?" ;":_ordenes.getValue("MTO_DIVISAS")+";");
					
					


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