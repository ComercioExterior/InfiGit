package models.bcv.taquilla_aereopuerto;

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
public class ExportarCVS extends ExportableOutputStream {

	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		long idUnidad;
		String statusP = null;
		String fecha   = null;
		idUnidad           = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP            = _record.getValue("statusp");		
		fecha              = (String) _record.getValue("fecha");
		String  tipoNegocio      = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR_TAQUILLA;
		String  cruceProcesado   = "0";
		Integer tasaMinima       = Integer.parseInt(_req.getParameter("tasa_minima")         == null ? "0" : _req.getParameter("tasa_minima")); 
		Integer tasaMaxima       = Integer.parseInt(_req.getParameter("tasa_maxima")         == null ? "0" : _req.getParameter("tasa_maxima"));
		Integer montoMinimo      = Integer.parseInt(_req.getParameter("monto_minimo")        == null ? "0" : _req.getParameter("monto_minimo"));
		Integer montoMaximo      = Integer.parseInt(_req.getParameter("monto_maximo")        == null ? "0" : _req.getParameter("monto_maximo"));
		Integer clienteID        = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));
		Integer incluirCliente   = Integer.parseInt(_req.getParameter("incluir_cliente")   == null ? "0" : _req.getParameter("incluir_cliente"));
		String  estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
		//NM25287 TTS-504 Modificación para usar en anulacion de operaciones de taquilla
		boolean anulacionEfectivo=true;
		String tipoEnvio    = _req.getParameter("tipoEnvio")         == null ? "0" : _req.getParameter("tipoEnvio");
		if(!tipoEnvio.equalsIgnoreCase("anulacion")){
			anulacionEfectivo=false;
		}
		
		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_TAQUILLA_AEREOPUERTO_EXP_CSV.getNombreAccion())){ //DEMANDA
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			
			//ordenesCrucesDAO.listarOrdenesPorEnviarBCVMenudeo(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false, idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
			ordenDAO.listarOrdenesPorEnviarBCVPorVentaTaquilla(tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false, idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI,anulacionEfectivo);
			
			_ordenes = ordenDAO.getDataSet();			
		
			try {//Se le da nombre e inicio al documento	
				registrarInicio("ordenes.csv");
				/*if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){ //FUE APROBADA
				//crearCabecera("Unidad de Inversión;Orden INFI;Cliente;Cédula o Rif;Monto Solicitado; Monto Cta Dolares; Monto Efectivo; Tasa; Número de Cuenta; Email; Estatus BCV; Estatus INFI; Observación; Número Orden BCV;");				
				}else {
				//crearCabecera("Unidad de Inversión;Orden INFI;Cliente;Cédula o Rif;Monto Solicitado; Monto Cta Dolares; Monto Efectivo; Tasa; Número de Cuenta; Email; Estatus BCV; Estatus INFI; Observación; ");				
				}*/
				
				crearCabecera("Unidad de Inversión;Orden INFI;Cliente;Cédula o Rif;Monto Operacion; Tipo Operacion;Tasa; Número de Cuenta; Email; Estatus BCV; Número Orden BCV;Observación;");
				 
				escribir("\r\n");
				
				while (_ordenes.next()) {					
					registroProcesado++;
					String observacion;
					if(_ordenes.getValue("OBSERVACION")!=null){
						observacion = _ordenes.getValue("OBSERVACION").replaceAll("\n", "")+";";
					}else {
						observacion = " ;";
					}
					
					//se agregan uno por uno los registros
					escribir(_ordenes.getValue("UNDINV_NOMBRE")==null?" ;":_ordenes.getValue("UNDINV_NOMBRE")+";");
					escribir(_ordenes.getValue("ORDENE_ID")==null?" ;":_ordenes.getValue("ORDENE_ID")+";");
					escribir(_ordenes.getValue("CLIENT_NOMBRE")==null?" ;":_ordenes.getValue("CLIENT_NOMBRE")+";");
					escribir(_ordenes.getValue("CLIENT_CEDRIF")==null?" ;":_ordenes.getValue("TIPPER_ID")+""+_ordenes.getValue("CLIENT_CEDRIF")+";");										
					escribir(_ordenes.getValue("monto_adj")==null?" ;":_ordenes.getValue("monto_adj")+";");
					escribir(_ordenes.getValue("tipo_operacion")==null?" ;":_ordenes.getValue("tipo_operacion")+";");
					escribir(_ordenes.getValue("tasa_cambio")==null?" ;":_ordenes.getValue("tasa_cambio")+";");
					escribir(_ordenes.getValue("CTA_NUMERO")==null?" ;":_ordenes.getValue("CTA_NUMERO").concat("'")+";");
					escribir(_ordenes.getValue("CLIENT_CORREO_ELECTRONICO")==null?" ;":_ordenes.getValue("CLIENT_CORREO_ELECTRONICO")+";");
					escribir(_ordenes.getValue("ESTATUS")==null?" ;":_ordenes.getValue("ESTATUS").replaceAll("\n", "")+";");
					escribir(_ordenes.getValue("ORDENE_ID_BCV")==null?" ;":_ordenes.getValue("ORDENE_ID_BCV")+";");					
					
					//escribir(_ordenes.getValue("ORDSTA_ID")==null?" ;":_ordenes.getValue("ORDSTA_ID")+";");
					escribir(observacion);
					/*if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){ //FUE APROBADA
						escribir(_ordenes.getValue("ORDENE_ID_BCV")==null?" ;":_ordenes.getValue("ORDENE_ID_BCV")+";");	
					}*/
					escribir("\r\n");
				} // fin while
				registrarFin();
				obtenerSalida();
			} catch (Exception e) {
				_record.addError("Nombre","Error en la exportación del Excel" + "Error:"  + e.getMessage());
				Logger.error(this,"Error en la exportación del Excel",e);
			} 
		}
	}// fin execute

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
	}
	

}// Fin Clase