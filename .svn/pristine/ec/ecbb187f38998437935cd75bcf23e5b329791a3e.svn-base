package models.utilitarios.envio_correos; 


import java.sql.ResultSet;
import java.sql.Statement;

import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.EnvioMailDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

/**
 * Clase que exporta a excel los correos enviados fitrados
 * 
 * @author NM29643
 */
public class InformeEnvioCorreos extends ExportableOutputStream {

	private String plantilla_id="", fechaDesde="", fechaHasta="", status_correo="", ui_id="", id_ordenes="";
	//areas="", 
	
	public void execute() throws Exception {
		
		Transaccion transaccion = new Transaccion(_dso);
		Statement statement = null;
		ResultSet _exportar = null;
		
		try{
		
		plantilla_id = _req.getParameter("plantilla_id");
		status_correo = _req.getParameter("status_correo").equals("")?null:_req.getParameter("status_correo");
		fechaDesde = _req.getParameter("fecha_desde").equals("")?null:_req.getParameter("fecha_desde");
		fechaHasta = _req.getParameter("fecha_hasta").equals("")?null:_req.getParameter("fecha_hasta");
		ui_id = _req.getParameter("ui_id").equals("")?null:_req.getParameter("ui_id");
		id_ordenes = _req.getParameter("id_ordenes").equals("")?null:_req.getParameter("id_ordenes");
		boolean cliente = false;
		String consulta = "";
		
Logger.debug(this, "plantilla_id: "+plantilla_id+" status_correo: "+status_correo+" ui_id: "+ui_id+" id_ordenes: "+id_ordenes+" fechaDesde: "+fechaDesde+" fechaHasta: "+fechaHasta+" framework.controller.outputstream.open: "+_req.getParameter("framework.controller.outputstream.open"));
		
		if(plantilla_id!=null){
		
		EnvioMailDAO emDAO = new EnvioMailDAO(_dso);
		
		cliente = true;
		if(ui_id!=null){
			if(id_ordenes!=null){ //ui_id!=null && id_ordenes!=null
				Logger.debug(this, "ui_id!=null && id_ordenes!=null");
				consulta = emDAO.listarCorreos(null, false, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, status_correo, plantilla_id, id_ordenes, ui_id, null, fechaDesde, fechaHasta);
			}else{ //ui_id!=null && id_ordenes==null
Logger.debug(this, "ui_id!=null && id_ordenes==null");
				consulta = emDAO.listarCorreos(null, false, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, status_correo, plantilla_id, null, ui_id, null, fechaDesde, fechaHasta);
			}
		}else{
			if(id_ordenes!=null){ //ui_id==null && id_ordenes!=null
Logger.debug(this, "ui_id==null && id_ordenes!=null");
				consulta = emDAO.listarCorreos(null, false, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, status_correo, plantilla_id, id_ordenes, null, null, fechaDesde, fechaHasta);
			}else{ //ui_id==null && id_ordenes==null
Logger.debug(this, "ui_id==null && id_ordenes==null");
				consulta = emDAO.listarCorreos(null, false, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, status_correo, plantilla_id, null, null, null, fechaDesde, fechaHasta);
			}
		}
		
		transaccion.begin();
		statement = transaccion.getConnection().createStatement();
			
//		DataSet correos = emDAO.getDataSet();
		
//		if(correos.count()>0){ //Hay correos filtrados cargados para el ciclo
//Logger.debug(this, "Hay "+correos.count()+" correos cargados para el informe");

				//Se registra el inicio del proceso de exportacion				
				registrarInicio(obtenerNombreArchivo(ConstantesGenerales.ARCH_INFORME_ENVIO_CORREOS+"_"));
				
				//Creacion de cabecera del informe
				crearCabecera();
				
				_exportar = statement.executeQuery(consulta);
				
//				correos.first();
//				
//				for(int i=0; i<correos.count(); i++){
//Logger.debug(this, "Entra al ciclo");
//				correos.next();
			int i=0;
			while (_exportar.next()) {
//					escribir(correos.getValue("STATUS"));
					escribir(_exportar.getString("STATUS"));
					escribir(";");
					if(cliente){
						escribir(_exportar.getString("ORDENE_ID"));
						escribir(";");
						escribir(_exportar.getString("TIPPER_ID")+"-"+_exportar.getString("CLIENT_CEDRIF"));
						escribir(";");
						escribir(_exportar.getString("CLIENT_NOMBRE"));
						escribir(";");
						escribir(_exportar.getString("CLIENT_CORREO_ELECTRONICO"));
						escribir(";");
						escribir(_exportar.getString("UNDINV_NOMBRE"));									
						escribir(";");
					}else{
						escribir(_exportar.getString("DESTINATARIO"));
						escribir(";");
						escribir(_exportar.getString("PLANT_MAIL_AREA_NAME"));
						escribir(";");
					}
					escribir(_exportar.getString("PLANTILLA_MAIL_NAME"));
					escribir(";");
					escribir(_exportar.getString("CICLO_ID"));
					escribir(";");
					escribir(_exportar.getString("FECHA_ENVIO"));
					escribir(";\r\n");
					i++;
					Logger.debug(this, "Escribe registro nro "+i);
			}//ciclo
				
Logger.debug(this, "Sale el for de escritura");
			
			//Se registra final del proceso de exportacion del informe
			registrarFin();
Logger.debug(this, "Registra el fin");
			
			//Se obtiene el archivo generado
			obtenerSalida();
Logger.debug(this, "Obtiene la salida");
			
//		}else{ //no hay correos cargados
//			Logger.error(this, "ENVIO CORREOS: No se pudo generar el informe de envío de correos ya que no se encontraron correos que cumplan con los criterios seleccionados");
//		}
		
		}//plantilla_id!=null
		
		}catch (Exception e) {
			Logger.error(this, "ENVIO CORREOS: Error en la generación del informe de envío de correos: ",e);
		} finally{
			if (_exportar != null){
				_exportar.close();
			}
			if (statement != null){
				statement.close();
			}
			if (transaccion != null){
				transaccion.closeConnection();
			}
		}
	}
	
	protected void crearCabecera() throws Exception{
//		if(tipoDest.equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
			escribir((ConstantesGenerales.CAMPO_INF_STATUS_ENVIO+";"+ConstantesGenerales.CAMPO_INF_COD_ORDEN+";"+ConstantesGenerales.CAMPO_INF_RIF_CI+";"+ConstantesGenerales.CAMPO_INF_NOMBRE+";"+ConstantesGenerales.CAMPO_INF_CORREO+";"+ConstantesGenerales.CAMPO_INF_UI+";"+ConstantesGenerales.CAMPO_INF_PLANT+";"+ConstantesGenerales.CAMPO_INF_CICLO+";"+ConstantesGenerales.CAMPO_INF_FECHA_ENV+";\r\n").toUpperCase());
//		}else{
//			if(tipoDest.equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){
//				escribir((ConstantesGenerales.CAMPO_INF_STATUS_ENVIO+";"+ConstantesGenerales.CAMPO_INF_DEST+";"+ConstantesGenerales.CAMPO_INF_AREA+";"+ConstantesGenerales.CAMPO_INF_PLANT+";"+ConstantesGenerales.CAMPO_INF_CICLO+";"+ConstantesGenerales.CAMPO_INF_FECHA_ENV+";\r\n").toUpperCase());
//			}
//		}
Logger.debug(this, "Se escribio la cabecera");
	}
	
}