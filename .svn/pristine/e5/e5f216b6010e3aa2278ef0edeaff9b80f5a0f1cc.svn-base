package models.bcv.pacto;

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
		String fecha   = null;
		String estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
		String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
		String cruceProcesado = "0";
		
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP  =          _record.getValue("statusp");		
		fecha    = (String) _record.getValue("fecha");
		
		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_PACTO_EXP_CSV.getNombreAccion())){ //DEMANDA
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarBCV(cruceProcesado,tipoNegocio,0,0,0,0,0,0,false,idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
			_ordenes = ordenesCrucesDAO.getDataSet();			
		
			try {//Se le da nombre e inicio al documento	
				registrarInicio("ordenes.csv");
				if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){ //FUE APROBADA
					crearCabecera("Unidad de Inversión;Número de Oferta Infi; Número de Orden BCV Oferta;Monto Ofertado; Número de Demanda Infi; Número de Orden BCV Demanda; Monto Demandado; Número de Pacto BCV; Demandante;Cédula o Rif del Demandante;Ofertante; Cédula o Rif del Ofertante; Tasa; Estatus; Observación; ");
				}else {
					crearCabecera("Unidad de Inversión;Número de Oferta Infi; Número de Orden BCV Oferta;Monto Ofertado; Número de Demanda Infi; Número de Orden BCV Demanda; Monto Demandado; Demandante;Cédula o Rif del Demandante;Ofertante; Cédula o Rif del Ofertante;Tasa; Estatus; Observación; ");
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
					escribir(_ordenes.getValue("undinv_nombre")==null?" ;":_ordenes.getValue("undinv_nombre")+";");
					escribir(_ordenes.getValue("id_oferta")==null?" ;":_ordenes.getValue("id_oferta")+";");
					escribir(_ordenes.getValue("ordene_id_bcv_of")==null?" ;":_ordenes.getValue("ordene_id_bcv_of")+";");
					escribir(_ordenes.getValue("monto_ofertado")==null?" ;":_ordenes.getValue("monto_ofertado")+";");
					escribir(_ordenes.getValue("ordene_id")==null?" ;":_ordenes.getValue("ordene_id")+";");
					escribir(_ordenes.getValue("ordene_id_bcv_de")==null?" ;":_ordenes.getValue("ordene_id_bcv_de")+";");
					escribir(_ordenes.getValue("ordene_adj_monto")==null?" ;":_ordenes.getValue("ordene_adj_monto")+";");
					
					if(statusP.equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){ //FUE APROBADA
						escribir(_ordenes.getValue("ORDENE_ID_PACTO_BCV")==null?" ;":_ordenes.getValue("ORDENE_ID_PACTO_BCV")+";");
					}
					
					escribir(_ordenes.getValue("client_nombre_de")==null?" ;":_ordenes.getValue("client_nombre_de")+";");
					escribir(_ordenes.getValue("client_cedrif_de")==null?" ;":_ordenes.getValue("tipper_id_de")+""+_ordenes.getValue("client_cedrif_de")+";");
					escribir(_ordenes.getValue("client_nombre_of")==null?" ;":_ordenes.getValue("client_nombre_of")+";");
					escribir(_ordenes.getValue("client_cedrif_of")==null?" ;":_ordenes.getValue("tipper_id_of")+""+_ordenes.getValue("client_cedrif_of")+";");
					escribir(_ordenes.getValue("ordene_tasa_pool")==null?" ;":_ordenes.getValue("ordene_tasa_pool")+";");
					escribir(_ordenes.getValue("estatus_string")==null?" ;":_ordenes.getValue("estatus_string")+";");
					escribir(_ordenes.getValue("observacion")==null?" ;":_ordenes.getValue("observacion").replaceAll("\n", "") +";");
					
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