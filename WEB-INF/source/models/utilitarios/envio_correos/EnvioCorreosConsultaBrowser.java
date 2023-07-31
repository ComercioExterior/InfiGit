package models.utilitarios.envio_correos;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlCicloDAO;
import com.bdv.infi.dao.EnvioMailDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.EnvioMail;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.PlantillaMailArea;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
//import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;

public class EnvioCorreosConsultaBrowser extends MSCModelExtend {
	
	PlantillasMailDAO plantillasMailDAO;
	DataSet infoClientArea, datos, plantilla;
	String[] areas_req;
	String[] codOrdenes;
	Hashtable<String,PlantillaMailArea> areasInfo = new Hashtable<String,PlantillaMailArea>();
	//Hashtable<String,Orden> ordenesInfo = new Hashtable<String,Orden>();
	ArrayList<EnvioMail> listaCorreosFiltrados;
	PlantillaMail pm;
	ControlCicloDAO controlCicloDAO;
	EnvioMailDAO emDAO;
	//long ciclo_id;
	long idCorreoIni;
	long idCorreoIncrem;
	//EnvioCorreos ec;
	String mensError, parametros; 
	//consulta, consulta2;
	//boolean enviar = true;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		try{
		
		datos.setValue("t_registros", infoClientArea.count()+"");
		datos.setValue("mens_error", mensError);
		datos.setValue("parametros", parametros);
//Logger.debug(this, "Antes del replace");
		//consulta = consulta.replaceAll("'", "#");
		//consulta = consulta.replaceAll("=", ":");
		//consulta = consulta.replaceAll(" ", "@");
//Logger.debug(this, "CONSULTAAAAA: "+consulta);
//		datos.setValue("consulta", consulta);
//		consulta2 = consulta2.replaceAll("'", "#");
//		datos.setValue("consulta2", consulta2);
		if(infoClientArea.count()==0){
			datos.setValue("enviar_display", "none");
		}else{
			datos.setValue("enviar_display", "inline");
		}
		
//Logger.debug(this, "Despues de enviar_Display");
		//if(!mensError.equals("")){
//		if(!enviar){
//			datos.setValue("table_display", "none");
//			datos.setValue("enviar_display", "none");
//		}
//Logger.debug(this, "Despues de table_display y enviar_Display");
		
		//datos.setValue("ciclo_id", ciclo.getCicloId()+"");
		
Logger.debug(this, "infoClientArea Tam (Nro. Correos): "+infoClientArea.count());
Logger.debug(this, "areasInfo Tam: "+areasInfo.size());
//Logger.debug(this, "RECORD\n "+_record);
//Logger.debug(this, "infoClientArea\n "+infoClientArea);

		
		}catch (Exception e) {
			Logger.error(this, "ENVIO CORREOS: Ha ocurrido un error inesperado durante la consulta de los envios de correos. Mensaje de la Excepcion: "+e.getMessage());
			mensError = "Ha ocurrido un error inesperado durante la consulta de los envios de correos";
		}
		
		storeDataSet("datos", datos); //Registra el dataset exportado por este modelo
		storeDataSet("info_correos", infoClientArea); //Registra el dataset exportado por este modelo
		storeDataSet("record", _record); //Registra el dataset exportado por este modelo
		
	}
	
	
	
	public boolean isValid() throws Exception {
		
		boolean valido = true;
		DataSet correosFiltrados;
		
		mensError = "";
		parametros = "?plantilla_id=";
		
		//Se inicializa el dataSet con datos para la vista
		datos = new DataSet();
		datos.append("ciclo_id", java.sql.Types.VARCHAR);
		datos.append("cliente_display", java.sql.Types.VARCHAR);
		datos.append("funcional_display", java.sql.Types.VARCHAR);
		datos.append("table_display", java.sql.Types.VARCHAR);
		datos.append("colspan", java.sql.Types.VARCHAR);
		datos.append("colspan_todos", java.sql.Types.VARCHAR);
		datos.append("columnas", java.sql.Types.VARCHAR);
		datos.append("t_registros", java.sql.Types.VARCHAR);
		datos.append("mens_error", java.sql.Types.VARCHAR);
		datos.append("enviar_display", java.sql.Types.VARCHAR);
		datos.append("parametros", java.sql.Types.VARCHAR);
//		datos.append("consulta", java.sql.Types.VARCHAR);
//		datos.append("consulta2", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("cliente_display", "none");
		datos.setValue("funcional_display", "none");
		datos.setValue("table_display", "block");
		
//		//Creacion de objeto EnvioCorreos
//		InetAddress direccion = InetAddress.getLocalHost();
//    	String direccionIpstr = direccion.getHostAddress();
//		ec = new EnvioCorreos(_dso, null, direccionIpstr);
//		//Se inicializan los parametros relacionados con el envio de correos
//		ec.initParamEnvio();
		
		emDAO = new EnvioMailDAO(_dso);
		//Consulta el id del proximo correo a precargar (en caso de pasar isValid)
//		idCorreoIni = emDAO.getSecuencia(ConstantesGenerales.SQ_ENVIO_MAIL);
//Logger.debug(this, "idCorreoIni ---------------- "+idCorreoIni);
//		idCorreoIncrem = 0; //Se inicializa en 0 el contador de secuencia
		
//		controlCicloDAO = new ControlCicloDAO(_dso);
//		//Consulta el id del proximo ciclo a aperturar (en caso de pasar isValid)
//		ciclo_id = controlCicloDAO.getSecuencia(ConstantesGenerales.SQ_CONTROL_CICLOS);
		
		//Se inicializa la lista de correos filtrados
		listaCorreosFiltrados = new ArrayList<EnvioMail>();
		
		//Se inicializa dataSet con informacion a mostrar en tabla de la vista
		infoClientArea = new DataSet();
		
		//Se define el campo fijo (independientemente del tipo de destinatario)
		infoClientArea.append("correo_id", java.sql.Types.VARCHAR);
		infoClientArea.append("status", java.sql.Types.VARCHAR);
		
		
Logger.debug(this, "plantilla_id: "+_record.getValue("plantilla_id"));
Logger.debug(this, "plant_mail_name: "+_record.getValue("plant_mail_name"));
Logger.debug(this, "ui_id: "+_record.getValue("ui_id"));
Logger.debug(this, "ui_id: "+_record.getValue("ui_txt"));
Logger.debug(this, "id_ordenes: "+_record.getValue("id_ordenes"));
Logger.debug(this, "id_ordenes reqGetParameter: "+_req.getParameter("id_ordenes"));
//Logger.debug(this, "tipo_destinatario: "+_record.getValue("tipo_destinatario"));
//Logger.debug(this, "status_orden: "+_record.getValue("status_orden"));
//Logger.debug(this, "transaccion: "+_record.getValue("transaccion"));
//Logger.debug(this, "tipo_destinatario name: "+_record.getValue("name_dest").toUpperCase());
//Logger.debug(this, "status_orden name: "+_record.getValue("name_status_orden_plant"));
//Logger.debug(this, "transaccion name: "+_record.getValue("name_transac_plant"));
Logger.debug(this, "Evento_id: "+_record.getValue("evento_id"));
Logger.debug(this, "evento_name: "+_record.getValue("evento_name"));
Logger.debug(this, "fecha desde: "+_record.getValue("fecha_desde"));
Logger.debug(this, "fecha hasta: "+_record.getValue("fecha_hasta"));
Logger.debug(this, "status_correo "+_record.getValue("status_correo"));

//actions = _req.getParameterValues("actions_role");
		
		if(_record.getValue("plantilla_id")!=null){
			
			plantillasMailDAO = new PlantillasMailDAO(_dso);
			pm = new PlantillaMail();
			pm.setPlantillaMailId(Integer.parseInt(_record.getValue("plantilla_id")));
			
			parametros += _record.getValue("plantilla_id");
			
			parametros += "&status_correo="+_record.getValue("status_correo");

			
			if(_record.getValue("fecha_desde")!=null) {
				parametros += "&fecha_desde="+_record.getValue("fecha_desde");
				//_record.setValue("fecha_desde", _record.getValue("fecha_desde"));
			}
			if(_record.getValue("fecha_hasta")!=null) {
				parametros += "&fecha_hasta="+_record.getValue("fecha_hasta");
				//_record.setValue("fecha_hasta", _record.getValue("fecha_hasta"));
			}
			
//			if(_record.getValue("tipo_destinatario")!=null){
				
//				parametros += "&tipo_destinatario="+_record.getValue("tipo_destinatario");//+_record.getValue("fecha_desde")+_record.getValue("fecha_hasta");
				
//				if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){ //DESTINATARIO FUNCIONAL
//					
//					datos.setValue("funcional_display", "block");
//					datos.setValue("colspan", "4");
//					datos.setValue("colspan_todos", "3");
//					datos.setValue("columnas", "<th>Estatus Env&iacute;o</th><th>Area</th><th>Destinatario</th>");
//					
//					//Se almacenenan los ids de las areas seleccionadas
//					areas_req = _req.getParameterValues("area");
//					
//Logger.debug(this, "areas string------: "+_req.getParameter("area"));
//					
//if(areas_req!=null) Logger.debug(this, "areas lenght: "+areas_req.length);
//					
//if(areas_req!=null) for(int i=0; i<areas_req.length; i++) Logger.debug(this, "area "+(i+1)+": "+areas_req[i]);
//					
//					if(areas_req!=null && areas_req.length>0){ //Se selecciono al menos una area asociada a la plantilla
//						
//						String areas_param = "";
//						for(int x=0; x<areas_req.length; x++) {
//							areas_param += areas_req[x];
//							if(x<areas_req.length-1) {
//								areas_param += ",";
//							}
//						}
//						parametros += "&areas=" + areas_param;
//						_record.setValue("areas", areas_param);
//Logger.debug(this, "areas PARAMETROS: "+parametros);
//						
//						plantillasMailDAO.listarPlantillasMail(pm, null, null, null, null);
//						plantilla = plantillasMailDAO.getDataSet();
//						
//						if(plantilla.count()>0){
//Logger.debug(this, "Encontro datos plantilla");
//							plantilla.first();
//							plantilla.next();
//							
////							if(plantilla.getValue("ESTATUS_ACTIVACION").trim().equals(ConstantesGenerales.VERDADERO+"")){ //Plantilla activa
//								
//								//Se llena informacion de la plantilla
//								pm.setPlantillaMailName(plantilla.getValue("PLANTILLA_MAIL_NAME").trim());
//								
//								//VALIDACION DE AREAS
//								plantillasMailDAO.listarAreasPlantillasMail(plantilla.getValue("PLANTILLA_MAIL_ID"), ConstantesGenerales.VERDADERO+"");
//								DataSet areas = plantillasMailDAO.getDataSet();
//								
//								if(areas.count()>0){
//Logger.debug(this, "Encuentra "+areas.count()+" areas");
//									//Se define estructura del dataSet para caso Funcional
//									infoClientArea.append("area_name", java.sql.Types.VARCHAR);
//									infoClientArea.append("destinatario", java.sql.Types.VARCHAR);
//									PlantillaMailArea pma;
//									String mensAreas = "";
//									
//									areas.first();
//									
//									for(int i=0; i<areas_req.length; i++){
//										for(int j=0; j<areas.count(); j++){
//											areas.next();
//											//valido = true;
//											if(areas_req[i].trim().equals(areas.getValue("PLANT_MAIL_AREA_ID").trim())){
//Logger.debug(this, "Area seleccionada "+areas_req[i]+" coincide con "+areas.getValue("PLANT_MAIL_AREA_ID"));
//												emDAO.listarCorreos(null, false, _record.getValue("tipo_destinatario"), null, null, pm.getPlantillaMailId()+"", areas_req[i], null, null, _record.getValue("fecha_desde"), _record.getValue("fecha_hasta"), _record.getValue("status_correo"));
//												correosFiltrados = emDAO.getDataSet();
//												if(correosFiltrados.count()>0){
//Logger.debug(this, "Encontro "+correosFiltrados.count()+" correo(s) del area "+areas_req[i]); 
//													correosFiltrados.first();
//													for(int k=0; k<correosFiltrados.count(); k++){
//														correosFiltrados.next();
//														//Informacion para la vista
//														infoClientArea.addNew();
//														infoClientArea.setValue("correo_id", correosFiltrados.getValue("CORREO_ID"));
//														infoClientArea.setValue("status", correosFiltrados.getValue("status"));
//														infoClientArea.setValue("area_name", correosFiltrados.getValue("PLANT_MAIL_AREA_NAME"));
//														infoClientArea.setValue("destinatario", correosFiltrados.getValue("DIRECCION_CORREO"));
//													}
//													pma = new PlantillaMailArea();
//													pma.setPlantMailAreaId(Integer.parseInt(areas.getValue("PLANT_MAIL_AREA_ID")));
//													pma.setPlantMailAreaName(areas.getValue("PLANT_MAIL_AREA_NAME"));
//													pma.setDestinatario(areas.getValue("DESTINATARIO"));
//													pma.setPlantillaMailId(Integer.parseInt(areas.getValue("PLANTILLA_MAIL_ID")));
//													pma.setEstatusActivacion(Integer.parseInt(areas.getValue("ESTATUS_ACTIVACION")));
//													//Si no se ha insertado el area seleccionada
//													if(!areasInfo.containsKey(areas.getValue("PLANT_MAIL_AREA_ID"))){
//														//Se guarda la informacion del area valida (la clave es el ID de area)
//														areasInfo.put(areas.getValue("PLANT_MAIL_AREA_ID"), pma);
//													}
//												}else{
//													//valido = false;
//													mensAreas += "No se encontr&oacute; ning&uacute;n env&iacute;o de correo para el &Aacute;rea "+areas.getValue("PLANT_MAIL_AREA_NAME").toUpperCase()+" que cumpla con los criterios seleccionados";
//												}
//												//Se inicializa recorrido por areas para comparar la proxima area seleccionada
//												areas.first();
//												break;
//											}else{
//												if(j==areas.count()-1) {
//Logger.debug(this, "Area seleccionada "+areas_req[i].trim()+" NO coincide con la ultima: "+areas.getValue("PLANT_MAIL_AREA_ID").trim()+" ni ningun area asoaciada a la plantilla "+j);
//													valido = false;
//													mensAreas += "El &Aacute;rea "+areas.getValue("PLANT_MAIL_AREA_NAME").toUpperCase()+" no se encuentra asociada a la plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase()+". ";
//												}
//											}
//										}//for areas plantilla
//									}//for areas seleccionadas
//									
//									if(!valido){ //Alguna de las areas seleccionadas no es valida
//										_record.addError("Plantilla &Aacute;reas", mensAreas);
//									}
//									
//									//Para que se muestre mensaje de error de las areas en la vista
//									mensError = mensAreas;
//									
//								}else{
//									//enviar = false;
//									valido = false;
//									_record.addError("Plantilla &Aacute;reas", "No se encontr&oacute; ninguna &aacute;rea asociada a la plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase());
//									//mensError = "No se encontr&oacute; ninguna &aacute;rea asociada a la plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase();
//								}
//								
////							}else{ //Plantilla Inactiva
////								valido = false;
////								_record.addError("Plantilla", "La plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase()+" se encuentra inactiva");
////							}
//							
//						}else{ //No encontro la plantilla
//							valido = false;
//							_record.addError("Plantilla", "No se encontr&oacute; ninguna plantilla para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase());
//							//mensError = "No se encontr&oacute; ninguna plantilla para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase();
//						}
//						
//					}else{
//						//mensError = "<span style=\"color:red;\">Error:</span> Debe seleccionar al menos un area asociada a la plantilla";
//						valido = false;
//						_record.addError("Area(s)", "Debe seleccionar al menos un area asociada a la plantilla");
//					}
//					
//				}else{ //Destinatario No Funcional
					
//					if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
					
					//DESTINATARIO CLIENTE
					
					datos.setValue("cliente_display", "block");
					datos.setValue("colspan", "7");
					datos.setValue("colspan_todos", "6");
					datos.setValue("columnas", "<th>Estatus Env&iacute;o</th><th>Nro. Orden</th><th>CI/RIF</th><th>Nombre</th><th>Correo</th><th>Unidad de Inversi&oacute;n</th>");
						
					plantillasMailDAO.listarPlantillasMail(pm, null, null, null, null);
					plantilla = plantillasMailDAO.getDataSet();
					if(plantilla.count()>0){
Logger.debug(this, "Encontro datos plantilla");
						plantilla.first();
						plantilla.next();
						
						//if(plantilla.getValue("ESTATUS_ACTIVACION").trim().equals(ConstantesGenerales.VERDADERO+"")){ //Plantilla Valida
Logger.debug(this, "Entra a plantilla activa");
							//Se llena informacion de la plantilla
							pm.setPlantillaMailName(plantilla.getValue("PLANTILLA_MAIL_NAME").trim());
							
							//Se define estructura del dataSet para caso Cliente
							infoClientArea.append("ci_rif", java.sql.Types.VARCHAR);
							infoClientArea.append("nombre", java.sql.Types.VARCHAR);
							infoClientArea.append("correo", java.sql.Types.VARCHAR);
							infoClientArea.append("orden_id", java.sql.Types.VARCHAR);
							infoClientArea.append("ui_name", java.sql.Types.VARCHAR);
							
							//VALIDACION DE UNIDAD DE INVERSION
							if(_record.getValue("ui_id")!=null){
Logger.debug(this, "Entra a Valida UI");
								parametros += "&ui_id="+_record.getValue("ui_id");
								
								UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
								uiDAO.listarDatosUIPorId(Long.parseLong(_record.getValue("ui_id")));
								if(uiDAO.getDataSet().count()==0){
									valido = false;
									_record.addError("Unidad de Inversi&oacute;n", "No se pudo hallar informaci&oacute;n de la Unidad de Inversi&oacute;n seleccionada");
								}
							}
							
							if(valido){ //Si, en caso de seleccionarse ui, esta es valida
								//VALIDACION DE ORDENES
								if(_record.getValue("id_ordenes")!=null){
Logger.debug(this, "Campos ordenes no vacio");
									parametros += "&id_ordenes="+_record.getValue("id_ordenes");

									codOrdenes = _record.getValue("id_ordenes").split(",");
Logger.debug(this, "codOrdenes length: "+codOrdenes.length);
									String mensOrdenes = "";
									Pattern patCodOrd = Pattern.compile("[0-9]{1,10}");
									Matcher m;
									OrdenDAO ordDAO = new OrdenDAO(_dso);
									for(int i=0; i<codOrdenes.length; i++){
										if(codOrdenes[i]!=null){
											codOrdenes[i]=codOrdenes[i].trim(); //Se quitan espacios iniciales o finales
Logger.debug(this, "orden "+codOrdenes[i].trim()+" no null (trim)");
											m = patCodOrd.matcher(codOrdenes[i]);
											if (m.find()) { //Si el registro coincide con el formato correspondiente
Logger.debug(this, "orden "+codOrdenes[i]+" coincide con patron");
												//Se busca la informacion de la orden
												ordDAO.listarDetalleDeOrdenPorId(Long.parseLong(codOrdenes[i]));
												
												if(ordDAO.getDataSet().count()>0){
Logger.debug(this, "orden "+codOrdenes[i]+" encontrada en bd");
													ordDAO.getDataSet().first();
													ordDAO.getDataSet().next();
													
//													//Validacion de status de orden contra plantilla
//													if(!ordDAO.getDataSet().getValue("ordsta_id").equalsIgnoreCase(plantilla.getValue("ORDSTA_ID"))){
//Logger.debug(this, "orden "+codOrdenes[i]+" no coincide con estatus plantilla");
//														valido = false;
//														mensOrdenes += "La Orden Nro. "+codOrdenes[i]+" no se encuentra en estatus "+_record.getValue("name_status_orden_plant")+", correspondiente a la plantilla seleccionada. El estatus de la Orden es: "+ordDAO.getDataSet().getValue("ORDSTA_NOMBRE")+". ";
//													}
//													//Validacion de transaccion de orden contra plantilla
//													if(!ordDAO.getDataSet().getValue("TRANSA_ID").equalsIgnoreCase(plantilla.getValue("TRANSA_ID"))){
//Logger.debug(this, "orden "+codOrdenes[i]+" no coincide con transaccion plantilla");
//														valido = false;
//														mensOrdenes += "La Orden Nro. "+codOrdenes[i]+" no se encuentra asociada a la transacci&oacute;n "+_record.getValue("name_transac_plant")+", correspondiente a la plantilla seleccionada. La transacci&oacute;n asoaciada a la Orden es: "+ordDAO.getDataSet().getValue("TRANSA_DESCRIPCION")+". ";
//													}
													//Validacion de Unidad de Inversion de orden contra la seleccionada en caso de haberse seleccionado
													if(_record.getValue("ui_id")!=null){
Logger.debug(this, "Ui no null");
														if(!ordDAO.getDataSet().getValue("UNIINV_ID").equalsIgnoreCase(_record.getValue("ui_id"))){
Logger.debug(this, "orden "+codOrdenes[i]+" no coincide con ui seleccionada");
															valido = false;
															mensOrdenes += "La Orden Nro. "+codOrdenes[i]+" no se encuentra asociada a la Unidad de Inversi&oacute;n seleccionada: "+_record.getValue("ui_txt")+". La Unidad de Inversi&oacute;n asoaciada a la Orden es: "+ordDAO.getDataSet().getValue("undinv_nombre")+". ";
														}
													}
													
													if(valido){ //Orden valida
														emDAO.listarCorreos(null, false, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, null, pm.getPlantillaMailId()+"", codOrdenes[i], null, null, _record.getValue("fecha_desde"), _record.getValue("fecha_hasta"), _record.getValue("status_correo")); //cicloStatusPlantillaAreaFecha)
														correosFiltrados = emDAO.getDataSet();
														if(correosFiltrados.count()>0){
															correosFiltrados.first();
															for(int k=0; k<correosFiltrados.count(); k++){
																correosFiltrados.next();
																//Informacion para la vista
																infoClientArea.addNew();
																infoClientArea.setValue("correo_id", correosFiltrados.getValue("CORREO_ID"));
																infoClientArea.setValue("status", correosFiltrados.getValue("status"));
																infoClientArea.setValue("ci_rif", correosFiltrados.getValue("TIPPER_ID")+"-"+correosFiltrados.getValue("CLIENT_CEDRIF"));
																infoClientArea.setValue("nombre", correosFiltrados.getValue("CLIENT_NOMBRE"));
																infoClientArea.setValue("correo", correosFiltrados.getValue("DIRECCION_CORREO"));
																infoClientArea.setValue("orden_id", correosFiltrados.getValue("ORDENE_ID"));
																infoClientArea.setValue("ui_name", correosFiltrados.getValue("UNDINV_NOMBRE"));
															}
														}else{
															mensError += "No se encontr&oacute; ning&uacute;n env&iacute;o de correo relacionado con la Orden Nro. &Aacute;rea "+codOrdenes[i]+" que cumpla con los criterios seleccionados";
														}
													}//Orden valida
													
												}else{ //orden no encontrada en BD
Logger.debug(this, "orden "+codOrdenes[i]+" no encontrada en bd");
													valido = false;
													mensOrdenes += "La Orden Nro. "+codOrdenes[i]+" no se encuentra registrada en el sistema. ";
												}
											}else{ //Si el registro NO coincide con el formato correspondiente
Logger.debug(this, "orden "+codOrdenes[i]+" formato invalido");
												valido = false;
												mensOrdenes += "Formato de Nro. de Orden inv&aacute;lido: "+codOrdenes[i]+". Debe ser num&eacute;rico. ";
											}
										}//!=null
									}//for
									
									if(!valido){ //Alguna de las ordenes no es valida
										_record.addError("ID de Orden(es)", mensOrdenes);
									}
									
								}else{ //Si no se especifican los ids de ordenes a buscar
									
									//Validacion de Unidad de Inversion de orden contra la seleccionada en caso de haberse seleccionado
									if(_record.getValue("ui_id")!=null){
Logger.debug(this, "Ui no null");
										emDAO.listarCorreos(null, false, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, null, pm.getPlantillaMailId()+"", null, _record.getValue("ui_id"), null, _record.getValue("fecha_desde"), _record.getValue("fecha_hasta"), _record.getValue("status_correo"));
									}else{
										emDAO.listarCorreos(null, false, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, null, pm.getPlantillaMailId()+"", null, null, null, _record.getValue("fecha_desde"), _record.getValue("fecha_hasta"), _record.getValue("status_correo"));
										
//										consulta = emDAO.listarCorreos(null, false, _record.getValue("tipo_destinatario"), null, null, pm.getPlantillaMailId()+"", null, null, null);
//Logger.debug(this, "Consulta-----: "+consulta);
//										consulta = consulta.substring(0, consulta.indexOf("order")).trim();
//Logger.debug(this, "Consulta sub-: "+consulta);
//										consulta += "|"+_record.getValue("fecha_desde")+"|"+_record.getValue("fecha_hasta");
//Logger.debug(this, "Consulta fec-: "+consulta);
//										emDAO.listarCorreos(null, false, _record.getValue("tipo_destinatario"), null, null, pm.getPlantillaMailId()+"", null, null, null, _record.getValue("fecha_desde"), _record.getValue("fecha_hasta"));
										
//										consulta = emDAO.listarCorreos(null, false, _record.getValue("tipo_destinatario"), null, null, pm.getPlantillaMailId()+"", null, null, null, _record.getValue("fecha_desde"), _record.getValue("fecha_hasta"));
//										consulta2 = consulta.substring(consulta.indexOf("where"), consulta.length()).trim();
//										consulta = consulta.substring(0, consulta.indexOf("where")).trim();
//Logger.debug(this, "Consulta-----: "+consulta);
//Logger.debug(this, "Consulta2----: "+consulta2);
									}
									correosFiltrados = emDAO.getDataSet();
									if(correosFiltrados.count()>0){
										correosFiltrados.first();
										for(int k=0; k<correosFiltrados.count(); k++){
											correosFiltrados.next();
											//Informacion para la vista
											infoClientArea.addNew();
											infoClientArea.setValue("correo_id", correosFiltrados.getValue("CORREO_ID"));
											infoClientArea.setValue("status", correosFiltrados.getValue("status"));
											infoClientArea.setValue("ci_rif", correosFiltrados.getValue("TIPPER_ID")+"-"+correosFiltrados.getValue("CLIENT_CEDRIF"));
											infoClientArea.setValue("nombre", correosFiltrados.getValue("CLIENT_NOMBRE"));
											infoClientArea.setValue("correo", correosFiltrados.getValue("DIRECCION_CORREO"));
											infoClientArea.setValue("orden_id", correosFiltrados.getValue("ORDENE_ID"));
											infoClientArea.setValue("ui_name", correosFiltrados.getValue("UNDINV_NOMBRE"));
										}
									}else{
										mensError += "No se encontr&oacute; ning&uacute;n env&iacute;o de correo que cumpla con los criterios seleccionados";
									}
									
								}//Si no se especifican los ids de ordenes a buscar
								
								
							}//Si, en caso de seleccionarse ui, esta es valida
							
//						}else{ //Plantilla Invalida
//							valido = false;
//							_record.addError("Plantilla", "La plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase()+" se encuentra inactiva");
//						}
						
					}else{ //No encontro la plantilla
						valido = false;
						_record.addError("Plantilla", "No se encontr&oacute; ninguna plantilla para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase());
						//mensError = "No se encontr&oacute; ninguna plantilla para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase();
					}
					
//					}//Destinatario Cliente
					
//				}//Destinatario No Funcional
				
//			}else{ //tipo_destinatario==null
//				//mensError = "<span style=\"color:red;\">Error:</span> Debe indicar el tipo de destinatario";
//				valido = false;
//				_record.addError("Tipo de Destinatario", "Debe indicar el tipo de destinatario");
//			}
			
		}else{ //plantilla_id==null
			//mensError = "<span style=\"color:red;\">Error:</span> No se indic&oacute; la plantilla";
			valido = false;
			_record.addError("Plantilla", "No se suministr&oacute; la informaci&oacute;n requerida para obtener la plantilla (Producto y Evento)");
		}
		
		return valido;
	}
}