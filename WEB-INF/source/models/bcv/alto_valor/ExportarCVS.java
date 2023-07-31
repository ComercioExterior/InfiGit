package models.bcv.alto_valor;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.OfertaDAO;
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
		String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
		String tipoProductoId = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
		String origen;
		String estatusCruce;
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP      = _record.getValue("statusP")      == null? "": _record.getValue("statusP");
		estatusCruce = _record.getValue("estatus_cruce")== null? "": _record.getValue("estatus_cruce");
		fecha        = (String) _record.getValue("fecha");
		origen       = _record.getValue("origen")       == null? "": _record.getValue("origen");
		
		Integer tasaMinima       = Integer.parseInt(_req.getParameter("tasa_minima")         == null ? "0" : _req.getParameter("tasa_minima")); 
		Integer tasaMaxima       = Integer.parseInt(_req.getParameter("tasa_maxima")         == null ? "0" : _req.getParameter("tasa_maxima"));
		Integer montoMinimo      = Integer.parseInt(_req.getParameter("monto_minimo")        == null ? "0" : _req.getParameter("monto_minimo"));
		Integer montoMaximo      = Integer.parseInt(_req.getParameter("monto_maximo")        == null ? "0" : _req.getParameter("monto_maximo"));
		Integer clienteID        = Integer.parseInt(_req.getParameter("cliente_id")          == null ? "0" : _req.getParameter("cliente_id"));
		Integer incluirCliente   = Integer.parseInt(_req.getParameter("incluir_cliente")     == null ? "0" : _req.getParameter("incluir_cliente"));
		Integer tipoOperacion    = Integer.parseInt(_req.getParameter("tipo_operacion")      == null ? "0" : _req.getParameter("tipo_operacion"));
		Integer blotterID        = Integer.parseInt(_req.getParameter("bloter_id")          == null ? "0" : _req.getParameter("bloter_id"));
		Integer ofertaAnulacion  = Integer.parseInt(ConstantesGenerales.OFERTA_ANULACION);
		Integer demandaAnulacion = Integer.parseInt(ConstantesGenerales.DEMANDA_ANULACION);
		
		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_DEMAN_EXP_CSV.getNombreAccion()) || tipoOperacion == demandaAnulacion){ //DEMANDA
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			ordenDAO.listarOrdenesPorEnviarBCV(tipoProductoId,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,tipoNegocio,idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha,blotterID);
			_ordenes = ordenDAO.getDataSet();			
		
			try {//Se le da nombre e inicio al documento	
				registrarInicio("ordenes.csv");
				if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equalsIgnoreCase("")){ //FUE APROBADA
					crearCabecera("Unidad de Inversión;Orden INFI;Cliente;Cédula o Rif;Monto Solicitado;Tasa; Número de Cuenta; Email; Estatus; Observación; Número Orden BCV;");
				}else {
					crearCabecera("Unidad de Inversión;Orden INFI;Cliente;Cédula o Rif;Monto Solicitado;Tasa; Número de Cuenta; Email; Estatus; Observación; ");
				} 				
				 
				escribir("\r\n");
				
				while (_ordenes.next()) {					
					registroProcesado++;
					String observacion;
					if(_ordenes.getValue("ORDENE_OBSERVACION")!=null){
						observacion = _ordenes.getValue("ORDENE_OBSERVACION").replaceAll("\n", "")+";";
					}else {
						observacion = " ;";
					}
					
					//se agregan uno por uno los registros
					escribir(_ordenes.getValue("UNDINV_NOMBRE")==null?" ;":_ordenes.getValue("UNDINV_NOMBRE")+";");
					escribir(_ordenes.getValue("ORDENE_ID")==null?" ;":_ordenes.getValue("ORDENE_ID")+";");
					escribir(_ordenes.getValue("CLIENT_NOMBRE")==null?" ;":_ordenes.getValue("CLIENT_NOMBRE")+";");
					escribir(_ordenes.getValue("CLIENT_CEDRIF")==null?" ;":_ordenes.getValue("TIPPER_ID")+""+_ordenes.getValue("CLIENT_CEDRIF")+";");
					escribir(_ordenes.getValue("ORDENE_PED_MONTO")==null?" ;":_ordenes.getValue("ORDENE_PED_MONTO")+";");	
					escribir(_ordenes.getValue("ORDENE_TASA_POOL")==null?" ;":_ordenes.getValue("ORDENE_TASA_POOL")+";");
					escribir(_ordenes.getValue("CTA_NUMERO")==null?" ;":"'"+_ordenes.getValue("CTA_NUMERO")+";");
					escribir(_ordenes.getValue("CLIENT_CORREO_ELECTRONICO")==null?" ;":_ordenes.getValue("CLIENT_CORREO_ELECTRONICO")+";");
					escribir(_ordenes.getValue("ESTATUS_STRING")==null?" ;":_ordenes.getValue("ESTATUS_STRING").replaceAll("\n", "")+";");
					escribir(observacion);
					if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equalsIgnoreCase("")){ //FUE APROBADA
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
		}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_OFER_EXP_CSV.getNombreAccion()) || tipoOperacion == ofertaAnulacion) { //OFERTA
			OfertaDAO ofertaDAO = new  OfertaDAO(_dso);
			ofertaDAO.listarOrdenesPorEnviarBCV(incluirCliente,0, tasaMinima,tasaMaxima,montoMinimo,montoMaximo, false,false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, origen, estatusCruce);
			_ordenes = ofertaDAO.getDataSet();
			
			try {//Se le da nombre e inicio al documento	
				registrarInicio("ordenes.csv");
				if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equalsIgnoreCase("")){ //FUE APROBADA
					crearCabecera("Id Oferta;Cliente;Cédula o Rif;Monto Solicitado;Tasa; Estatus; Observación; Número Orden BCV;");
				}else {
					crearCabecera("Id Oferta;Cliente;Cédula o Rif;Monto Solicitado;Tasa; Estatus; Observación;");
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
					escribir(_ordenes.getValue("ID_OFERTA")==null?" ;":_ordenes.getValue("ID_OFERTA")+";");
					escribir(_ordenes.getValue("CLIENT_NOMBRE")==null?" ;":_ordenes.getValue("CLIENT_NOMBRE")+";");
					escribir(_ordenes.getValue("CLIENT_CEDRIF")==null?" ;":   _ordenes.getValue("TIPPER_ID")+""+_ordenes.getValue("CLIENT_CEDRIF")+";");
					escribir(_ordenes.getValue("ORDENE_MONTO_OFERTADO")==null?" ;":_ordenes.getValue("ORDENE_MONTO_OFERTADO")+";");	
					escribir(_ordenes.getValue("ORDENE_TASA_CAMBIO")==null?" ;":_ordenes.getValue("ORDENE_TASA_CAMBIO")+";");
					escribir(_ordenes.getValue("ESTATUS_STRING")==null?" ;":_ordenes.getValue("ESTATUS_STRING")+";");
					escribir(observacion);
					if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equalsIgnoreCase("")){ //FUE APROBADA
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
		}
	}// fin execute

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
	}
	

}// Fin Clase