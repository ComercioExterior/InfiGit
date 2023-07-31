package models.bcv.menudeo;

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
		String  tipoNegocio      = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR;
		String  cruceProcesado   = "0";
		Integer tasaMinima       = Integer.parseInt(_req.getParameter("tasa_minima")         == null ? "0" : _req.getParameter("tasa_minima")); 
		Integer tasaMaxima       = Integer.parseInt(_req.getParameter("tasa_maxima")         == null ? "0" : _req.getParameter("tasa_maxima"));
		Integer montoMinimo      = Integer.parseInt(_req.getParameter("monto_minimo")        == null ? "0" : _req.getParameter("monto_minimo"));
		Integer montoMaximo      = Integer.parseInt(_req.getParameter("monto_maximo")        == null ? "0" : _req.getParameter("monto_maximo"));
		Integer clienteID        = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));
		Integer incluirCliente   = Integer.parseInt(_req.getParameter("incluir_cliente")   == null ? "0" : _req.getParameter("incluir_cliente"));
		String  estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
		
		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_EXP_CSV.getNombreAccion())){ //DEMANDA
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarBCVMenudeo(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false, idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
			
			_ordenes = ordenesCrucesDAO.getDataSet();			
		
			try {//Se le da nombre e inicio al documento	
				registrarInicio("ordenes.csv");
				if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){ //FUE APROBADA
					crearCabecera("Unidad de Inversión;Orden INFI;Cliente;Cédula o Rif;Monto Solicitado; Monto Adjudicado;Tasa; Número de Cuenta; Email; Estatus BCV; Estatus INFI; Observación; Número Orden BCV;");
				}else {
					crearCabecera("Unidad de Inversión;Orden INFI;Cliente;Cédula o Rif;Monto Solicitado; Monto Adjudicado;Tasa; Número de Cuenta; Email; Estatus BCV; Estatus INFI; Observación; ");
				} 				
				 
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
					escribir(_ordenes.getValue("ORDENE_PED_MONTO")==null?" ;":_ordenes.getValue("ORDENE_PED_MONTO")+";");
					escribir(_ordenes.getValue("ORDENE_ADJ_MONTO")==null?" ;":_ordenes.getValue("ORDENE_ADJ_MONTO")+";");	
					escribir(_ordenes.getValue("ORDENE_TASA_POOL")==null?" ;":_ordenes.getValue("ORDENE_TASA_POOL")+";");
					escribir(_ordenes.getValue("CTA_NUMERO")==null?" ;":_ordenes.getValue("CTA_NUMERO")+";");
					escribir(_ordenes.getValue("CLIENT_CORREO_ELECTRONICO")==null?" ;":_ordenes.getValue("CLIENT_CORREO_ELECTRONICO")+";");
					escribir(_ordenes.getValue("ESTATUS_STRING")==null?" ;":_ordenes.getValue("ESTATUS_STRING").replaceAll("\n", "")+";");
					escribir(_ordenes.getValue("ORDSTA_ID")==null?" ;":_ordenes.getValue("ORDSTA_ID")+";");
					escribir(observacion);
					if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){ //FUE APROBADA
						escribir(_ordenes.getValue("ORDENE_ID_BCV")==null?" ;":_ordenes.getValue("ORDENE_ID_BCV")+";");
					}
					escribir("\r\n");
				} // fin while
				registrarFin();
				obtenerSalida();
			} catch (Exception e) {
				_record.addError("Nombre","Error en la exportación del Excel" + "Error:"  + e.getMessage());
				Logger.error(this,"Error en la exportación del Excel",e);
			} 
		}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_OFER_EXP_CSV.getNombreAccion())) { //OFERTA
			SolicitudesSitmeDAO sitmeDAO = new  SolicitudesSitmeDAO(_dso);
			sitmeDAO.listarOrdenesPorEnviarBCV(incluirCliente,"0",tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha);
			_ordenes = sitmeDAO.getDataSet();
			
			try {//Se le da nombre e inicio al documento	
				registrarInicio("ordenes.csv");
				if(statusP.equalsIgnoreCase(ConstantesGenerales.SIN_VERIFICAR)){ //SI SIN VERIFICAR
					crearCabecera("Unidad de Inversión;Orden Oferta;Cliente;Cédula o Rif;Monto Solicitado;Tasa; Número de Cuenta; Email; Estatus; Observación; ");
				}else {
					crearCabecera("Unidad de Inversión;Orden Oferta;Cliente;Cédula o Rif;Monto Solicitado;Tasa; Número de Cuenta; Email; Estatus; Observación; Numero Orden BCV;");
				}
				
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
					escribir(_ordenes.getValue("ID_ORDEN")==null?" ;":_ordenes.getValue("ID_ORDEN")+";");
					escribir(_ordenes.getValue("NOMBRE_CLIENTE")==null?" ;":_ordenes.getValue("NOMBRE_CLIENTE")+";");
					escribir(_ordenes.getValue("CED_RIF_CLIENTE")==null?" ;":_ordenes.getValue("CED_RIF_CLIENTE")+";");
					escribir(_ordenes.getValue("MONTO_SOLICITADO")==null?" ;":_ordenes.getValue("MONTO_SOLICITADO")+";");	
					escribir(_ordenes.getValue("TASA_CAMBIO")==null?" ;":_ordenes.getValue("TASA_CAMBIO")+";");
					escribir(_ordenes.getValue("CTA_NUMERO")==null?" ;":_ordenes.getValue("CTA_NUMERO")+";");
					escribir(_ordenes.getValue("EMAIL_CLIENTE")==null?" ;":_ordenes.getValue("EMAIL_CLIENTE")+";");
					escribir(_ordenes.getValue("ESTATUS_STRING")==null?" ;":_ordenes.getValue("ESTATUS_STRING")+";");
					escribir(observacion);
					if(statusP.equalsIgnoreCase(ConstantesGenerales.SIN_VERIFICAR)){ //SI NO ES SIN VERIFICAR
						escribir(_ordenes.getValue("ID_BCV")==null?" ;":_ordenes.getValue("ID_BCV")+";");
					}
					
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