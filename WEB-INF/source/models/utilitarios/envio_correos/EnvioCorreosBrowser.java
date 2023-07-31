package models.utilitarios.envio_correos;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
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
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Ciclo;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.EnvioMail;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.PlantillaMailArea;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.CallEnvioCorreos;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;
//import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;

public class EnvioCorreosBrowser extends MSCModelExtend {
	
	PlantillasMailDAO plantillasMailDAO;
	DataSet infoClientArea, datos, plantilla;
	String[] areas_req;
	String[] codOrdenes;
	Hashtable<String,PlantillaMailArea> areasInfo = new Hashtable<String,PlantillaMailArea>();
	Hashtable<String,Orden> ordenesInfo = new Hashtable<String,Orden>();
	ArrayList<EnvioMail> listaCorreosFiltrados;
	PlantillaMail pm;
	ControlCicloDAO controlCicloDAO;
	EnvioMailDAO emDAO;
	long ciclo_id;
	long idCorreoIni;
	long idCorreoIncrem;
	EnvioCorreos ec;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String mensError = "";
		Ciclo ciclo = new Ciclo();
		int procesoCreado = -1;
		
		try{
		
		datos.setValue("t_registros", infoClientArea.count()+"");
		datos.setValue("mens_error", mensError);
		if(infoClientArea.count()==0){
			datos.setValue("enviar_display", "none");
		}else{
			datos.setValue("enviar_display", "inline");
		}
		
		//Consulta el id del proximo proceso a aperturar
		int proceso_id = Integer.parseInt(dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_PROCESOS));
Logger.debug(this, "Proceso ID obtenido: "+proceso_id);
		
		//Creacion de Proceso
		Proceso proceso = new Proceso();
		proceso.setEjecucionId(proceso_id);
		proceso.setTransaId(TransaccionNegocio.ENVIO_CORREOS);
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		if(getUserName()!=null && !getUserName().equals("")){
			proceso.setUsuarioId(Integer.parseInt(usuarioDAO.idUserSession(getUserName())));			
		}
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setCicloEjecucionId(ciclo_id);
		
		//Se setea el proceso al objeto EnvioCorreos
		ec.setProceso(proceso);
		
		//Creacion de Ciclo
		ciclo.setCicloId(ciclo_id);
		ciclo.setNombre(TransaccionNegocio.ENVIO_CORREOS.replaceAll("_", " "));
		ciclo.setExterno(ConstantesGenerales.FALSO);
		ciclo.setTipo(TransaccionNegocio.ENVIO_CORREOS);
		ciclo.setUsuarioRegistro(getUserName());
		ciclo.setStatus(ConstantesGenerales.STATUS_ENVIO_CORREO_PREINICIADO);
		
		//Se setea el ciclo al objeto EnvioCorreos
		ec.setCiclo(ciclo);
		
		//Insercion de Ciclo en BD
		ec.insertarCicloEnvio(ciclo);
			
			datos.setValue("ciclo_id", ciclo.getCicloId()+"");
			//_record.setValue("id_ciclo", ciclo.getCicloId()+"");
			
			//REGISTRAR PROCESO
			procesoCreado = ec.comenzarProceso();
Logger.debug(this, "procesoCreado: "+procesoCreado);
			if(procesoCreado==1){ //Se inserto el proceso en BD
			
				//Insercion de info de correos a enviar en TB 228
				if(ec.insertarCorreos(listaCorreosFiltrados)){
					
					//Se incrementa la secuencia tantas veces como inserciones se realizaron menos una (por el nexval inicial)
					for(int i=0; i<listaCorreosFiltrados.size()-1; i++){
						emDAO.getSecuencia(ConstantesGenerales.SQ_ENVIO_MAIL);
					}
Logger.debug(this, "listaCorreosFiltrados-1: --------------- "+(listaCorreosFiltrados.size()-1));
Logger.debug(this, "SQ ACTUAL --------------- "+emDAO.getSecuenciaActual(ConstantesGenerales.SQ_ENVIO_MAIL));
					
				}else{
					ec.getProceso().setDescripcionError("Ocurrio un error al insertar en BD los correos filtrados (precargados)");
					ec.borrarCorreos(ec.getCiclo().getCicloId(), ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO), false);
					mensError += "Ocurri&oacute; un error al insertar los correos filtrados. Intente nuevamente.";
					Logger.error(this, ciclo.getTipo()+": "+ec.getProceso().getDescripcionError());
					//Se termina el proceso
					ec.terminarProceso();
					//SE TERMINA EL CICLO (Se deja el estatus en el cual se encontraba)
					ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
				}
				
			}else{
				//Siempre se debe setear primero la descripcion en el ciclo antes de terminar el ciclo (pasa a ser la observacion del ciclo)
				//Por ende se debe terminar primero el ciclo que el proceso
				if(procesoCreado==0){
					ec.getProceso().setDescripcionError("No se pudo crear proceso de tipo "+ec.getProceso().getTransaId()+" puesto que ya existe un proceso de dicho tipo en ejecucion. Estatus Ciclo: "+ec.getCiclo().getStatus());
					mensError += "No se puede continuar puesto que ya existe un proceso de este tipo en ejecuci&oacute;n. Una vez finalizado &eacute;ste se podr&aacute; iniciar un nuevo proceso<br/>";
					Logger.info(this,ec.getProceso().getTransaId()+": Ya existe un proceso de este tipo en ejecucion. Estatus Ciclo: "+ec.getCiclo().getStatus());
				}else{
					if(procesoCreado==-1){
						ec.getProceso().setDescripcionError("Ocurrio una excepcion al insertar en BD proceso de tipo "+ec.getProceso().getTransaId()+" Estatus Ciclo: "+ec.getCiclo().getStatus());
						mensError += "Ocurri&oacute; un error inesperado al intentar crear proceso de este tipo<br/>";
						Logger.error(this,ec.getProceso().getTransaId()+": Ocurrio una excepcion al insertar en BD proceso. Estatus Ciclo: "+ec.getCiclo().getStatus());
					}
				}
				//SE TERMINA EL CICLO (Se deja el estatus en el cual se encontraba)
				ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
			}
			
		/*}else{
			mensError += "Ocurri&oacute; un error al crear el ciclo de env&iacute;o de correos. Intente nuevamente.";
			Logger.error(this,ciclo.getTipo()+": Ocurrio un error al crear el ciclo de tipo "+ciclo.getTipo());
		}*/
		
Logger.debug(this, "infoClientArea Tam (Nro. Correos): "+infoClientArea.count());
Logger.debug(this, "areasInfo Tam: "+areasInfo.size());
Logger.debug(this, "ordenesInfo Tam: "+ordenesInfo.size());
Logger.debug(this, "RECORD\n "+_record);
Logger.debug(this, "infoClientArea\n "+infoClientArea);

		//infoClientArea.first();

		
		}catch (Exception e) {
			if(ec.getProceso()!=null){
				ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Error inesperado durante el proceso de tipo "+ec.getProceso().getTransaId()+". Estatus Ciclo: "+ec.getCiclo().getStatus()+". Mensaje de la Excepcion: "+e.getMessage());
				Logger.error(this,ec.getProceso().getTransaId()+": Ha ocurrido un error inesperado durante en el proceso de tipo "+ec.getProceso().getTransaId()+". Estatus Ciclo: "+ec.getCiclo().getStatus()+". Mensaje de la Excepcion: "+e.getMessage());
				mensError = "Ha ocurrido un error inesperado en el proceso de tipo "+ec.getProceso().getTransaId();
			}else{
				Logger.error(this, "ENVIO CORREOS: Ha ocurrido un error inesperado. Mensaje de la Excepcion: "+e.getMessage());
				mensError = "Ha ocurrido un error inesperado.";
			}
			if(ec.getCiclo()!=null){
				//TERMINAR CICLO
				ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
			}
			
		}finally{
			
			if(procesoCreado==1){ //Si se creo el proceso
				//TERMINAR PROCESO
				ec.terminarProceso();
			}
			
			//REGISTRAR AUDITORIA
//			ec.registrarAuditoria();
		}
		
		datos.setValue("mens_error", mensError);
		
		if(!mensError.equals("")){
			datos.setValue("table_display", "none");
			datos.setValue("enviar_display", "none");
		}

		storeDataSet("datos", datos); //Registra el dataset exportado por este modelo
		storeDataSet("info_correos", infoClientArea); //Registra el dataset exportado por este modelo
		storeDataSet("record", _record); //Registra el dataset exportado por este modelo
		
		Logger.error(this,"Record\n"+_record);
	}
	
	
	
	public boolean isValid() throws Exception {
		
		boolean valido = true;
		EnvioMail em;
		
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
		datos.addNew();
		datos.setValue("cliente_display", "none");
		datos.setValue("funcional_display", "none");
		datos.setValue("table_display", "block");
		
		//Creacion de objeto EnvioCorreos
		InetAddress direccion = InetAddress.getLocalHost();
    	String direccionIpstr = direccion.getHostAddress();
		ec = new EnvioCorreos(_dso, null, direccionIpstr);
		//Se inicializan los parametros relacionados con el envio de correos
		ec.initParamEnvio();
		
		emDAO = new EnvioMailDAO(_dso);
		//Consulta el id del proximo correo a precargar (en caso de pasar isValid)
		idCorreoIni = emDAO.getSecuencia(ConstantesGenerales.SQ_ENVIO_MAIL);
Logger.debug(this, "idCorreoIni ---------------- "+idCorreoIni);
		idCorreoIncrem = 0; //Se inicializa en 0 el contador de secuencia
		
		controlCicloDAO = new ControlCicloDAO(_dso);
		//Consulta el id del proximo ciclo a aperturar (en caso de pasar isValid)
		ciclo_id = controlCicloDAO.getSecuencia(ConstantesGenerales.SQ_CONTROL_CICLOS);
		
		//Se inicializa la lista de correos filtrados
		listaCorreosFiltrados = new ArrayList<EnvioMail>();
		
		//Se inicializa dataSet con informacion a mostrar en tabla de la vista
		infoClientArea = new DataSet();
		
		//Se define el campo fijo (independientemente del tipo de destinatario)
		infoClientArea.append("correo_id", java.sql.Types.VARCHAR);
		
Logger.debug(this, "Evento_id: "+_record.getValue("evento_id"));
Logger.debug(this, "evento_name: "+_record.getValue("evento_name"));
Logger.debug(this, "plantilla_id: "+_record.getValue("plantilla_id"));
Logger.debug(this, "plant_mail_name: "+_record.getValue("plant_mail_name"));
Logger.debug(this, "ui_id: "+_record.getValue("ui_id"));
Logger.debug(this, "ui_txt: "+_record.getValue("ui_txt"));
Logger.debug(this, "id_ordenes: "+_record.getValue("id_ordenes"));
//Logger.debug(this, "id_ordenes reqGetParameter: "+_req.getParameter("id_ordenes"));
//Logger.debug(this, "tipo_destinatario: "+_record.getValue("tipo_destinatario"));
//Logger.debug(this, "status_orden: "+_record.getValue("status_orden"));
//Logger.debug(this, "transaccion: "+_record.getValue("transaccion"));
//Logger.debug(this, "tipo_destinatario name: "+_record.getValue("name_dest").toUpperCase());
//Logger.debug(this, "status_orden name: "+_record.getValue("name_status_orden_plant"));
//Logger.debug(this, "transaccion name: "+_record.getValue("name_transac_plant"));
//actions = _req.getParameterValues("actions_role");
		
		if(_record.getValue("plantilla_id")!=null){
			
			plantillasMailDAO = new PlantillasMailDAO(_dso);
			pm = new PlantillaMail();
			pm.setPlantillaMailId(Integer.parseInt(_record.getValue("plantilla_id")));
			
//			if(_record.getValue("tipo_destinatario")!=null){
//				
//				if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){ //DESTINATARIO FUNCIONAL
//					
//					datos.setValue("funcional_display", "block");
//					datos.setValue("colspan", "3");
//					datos.setValue("colspan_todos", "2");
//					datos.setValue("columnas", "<th>Area</th><th>Destinatario</th>");
//					
//					//Se almacenenan los ids de las areas seleccionadas
//					areas_req = _req.getParameterValues("area");
//if(areas_req!=null) Logger.debug(this, "areas lenght: "+areas_req.length);
//if(areas_req!=null) for(int i=0; i<areas_req.length; i++) Logger.debug(this, "area "+(i+1)+": "+areas_req[i]);
//					
//					if(areas_req!=null && areas_req.length>0){ //Se selecciono al menos una area asociada a la plantilla
//						
//						plantillasMailDAO.listarPlantillasMail(pm, null, null, null, null);
//						plantilla = plantillasMailDAO.getDataSet();
//						
//						if(plantilla.count()>0){
//Logger.debug(this, "Encontro datos plantilla");
//							plantilla.first();
//							plantilla.next();
//							
//							if(plantilla.getValue("ESTATUS_ACTIVACION").trim().equals(ConstantesGenerales.VERDADERO+"")){ //Plantilla activa
//								
//								//Se llena informacion de la plantilla
//								pm.setPlantillaMailName(plantilla.getValue("PLANTILLA_MAIL_NAME").trim());
//								
//								//VALIDACION DE AREAS
//								
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
//									String[] destinatarios;
//									
//									areas.first();
//									
//									for(int i=0; i<areas_req.length; i++){
//										for(int j=0; j<areas.count(); j++){
//											areas.next();
//											if(areas_req[i].trim().equals(areas.getValue("PLANT_MAIL_AREA_ID").trim())){
//Logger.debug(this, "Area seleccionada "+areas_req[i]+" coincide con "+areas.getValue("PLANT_MAIL_AREA_ID"));
//												if( areas.getValue("DESTINATARIO")==null || (areas.getValue("DESTINATARIO")!=null && areas.getValue("DESTINATARIO").trim().equals("")) ){
//													valido = false;
//													mensAreas += "El &Aacute;rea "+areas.getValue("PLANT_MAIL_AREA_NAME").toUpperCase()+" no posee destinatarios. ";
//												}else{
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
//														//Se crea objeto EnvioMail por cada destinatario del area valida y se setea informacion para la vista
//														destinatarios = pma.getDestinatario().split(",");
//														for(int k=0; k<destinatarios.length; k++){
//															if(destinatarios[k]!=null){
//																//idCorreoIncrem++; //Se incrementa contador de secuencia
//																em = new EnvioMail();
//																em.setIdCorreo(idCorreoIni);
//																em.setIdPlantilla(pm.getPlantillaMailId());
//																em.setIdArea(Long.parseLong(pma.getPlantMailAreaId()+""));
//																em.setDireccionCorreo(destinatarios[k]);
//																em.setStatus(ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO));
//																em.setIdCicloEjecucion(ciclo_id);
//																//Se agrega el correo a la lista de correos filtrados
//																listaCorreosFiltrados.add(em);
//																
//																//Informacion para la vista
//																infoClientArea.addNew();
//																infoClientArea.setValue("correo_id", idCorreoIni+"");
//																infoClientArea.setValue("area_name", pma.getPlantMailAreaName());
//																infoClientArea.setValue("destinatario", destinatarios[k]);
//																
//																idCorreoIni++; //Se incrementa el id para el proximo correo
//															}
//														}
//													}
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
//										}
//									}
//									
//									if(!valido){ //Alguna de las areas seleccionadas no es valida
//										_record.addError("Plantilla &Aacute;reas", mensAreas);
//									}
//									
//								}else{
//									valido = false;
//									_record.addError("Plantilla &Aacute;reas", "No se encontr&oacute; ninguna &aacute;rea asociada a la plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase());
//									//mensError = "No se encontr&oacute; ninguna &aacute;rea asociada a la plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase();
//								}
//								
//							}else{ //Plantilla Inactiva
//								valido = false;
//								_record.addError("Plantilla", "La plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase()+" se encuentra inactiva");
//							}
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
//					
//					if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
//					
					//DESTINATARIO CLIENTE
					
					datos.setValue("cliente_display", "block");
					datos.setValue("colspan", "6");
					datos.setValue("colspan_todos", "5");
					datos.setValue("columnas", "<th>Nro. Orden</th><th>CI/RIF</th><th>Nombre</th><th>Correo</th><th>Unidad de Inversi&oacute;n</th>");
						
					plantillasMailDAO.listarPlantillasMail(pm, null, null, null, null);
					plantilla = plantillasMailDAO.getDataSet();
					if(plantilla.count()>0){
Logger.debug(this, "Encontro datos plantilla");
						plantilla.first();
						plantilla.next();
						
						if(plantilla.getValue("ESTATUS_ACTIVACION").trim().equals(ConstantesGenerales.VERDADERO+"")){ //Plantilla Valida
Logger.debug(this, "Entra a plantilla activa");
							//Se llena informacion de la plantilla
							pm.setPlantillaMailName(plantilla.getValue("PLANTILLA_MAIL_NAME").trim());
							
							//Se define estructura del dataSet para caso Cliente
							infoClientArea.append("ci_rif", java.sql.Types.VARCHAR);
							infoClientArea.append("nombre", java.sql.Types.VARCHAR);
							infoClientArea.append("correo", java.sql.Types.VARCHAR);
							infoClientArea.append("orden_id", java.sql.Types.VARCHAR);
							infoClientArea.append("ui_name", java.sql.Types.VARCHAR);
							
							if(_record.getValue("ui_id")!=null || _record.getValue("id_ordenes")!=null){
								
							//VALIDACION DE UNIDAD DE INVERSION
							if(_record.getValue("ui_id")!=null){
Logger.debug(this, "Entra a Valida UI");
								UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
								uiDAO.listarDatosUIPorId(Long.parseLong(_record.getValue("ui_id")));
								if(uiDAO.getDataSet().count()==0){
									valido = false;
									_record.addError("Unidad de Inversi&oacute;n", "No se pudo hallar informaci&oacute;n de la Unidad de Inversi&oacute;n seleccionada");
								}
							}
							
							if(valido){
								//VALIDACION DE ORDENES
								if(_record.getValue("id_ordenes")!=null){
Logger.debug(this, "Campos ordenes no vacio");
									codOrdenes = _record.getValue("id_ordenes").split(",");
Logger.debug(this, "codOrdenes length: "+codOrdenes.length);
									String mensOrdenes = "";
									Pattern patCodOrd = Pattern.compile("[0-9]{1,10}");
									Matcher m;
									Cliente client;
									Orden ord;
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
														//Almacena informacion del cliente
														client = new Cliente();
														client.setIdCliente(Long.parseLong(ordDAO.getDataSet().getValue("CLIENT_ID")));
														client.setCorreoElectronico(ordDAO.getDataSet().getValue("CLIENT_CORREO_ELECTRONICO"));
														client.setNombre(ordDAO.getDataSet().getValue("client_nombre"));
														client.setTipoPersona(ordDAO.getDataSet().getValue("tipper_id"));
														client.setRifCedula(Long.parseLong(ordDAO.getDataSet().getValue("client_cedrif")));
Logger.debug(this, "orden "+codOrdenes[i]+" Cliente seteado");
														//Almacena informacion de la orden
														ord = new Orden();
														ord.setCliente(client);
														ord.setIdOrden(Long.parseLong(ordDAO.getDataSet().getValue("ORDENE_ID")));
														ord.setIdUnidadInversion(Integer.parseInt(ordDAO.getDataSet().getValue("UNIINV_ID")));
														ord.setStatus(ordDAO.getDataSet().getValue("ordsta_id"));
														ord.setIdTransaccion(ordDAO.getDataSet().getValue("TRANSA_ID"));
														ord.setTipoProducto(ordDAO.getDataSet().getValue("TIPO_PRODUCTO_ID"));
Logger.debug(this, "orden "+codOrdenes[i]+" Orden seteada");
														
														//Si no se ha insertado la orden seleccionada
														if(!ordenesInfo.containsKey(ordDAO.getDataSet().getValue("ORDENE_ID"))){
															//idCorreoIncrem++; //Se incrementa contador de secuencia
															
															//Se guarda la informacion de la orden valida (la clave es el ID de orden)
															ordenesInfo.put(ordDAO.getDataSet().getValue("ORDENE_ID"), ord);
															//Se crea el objeto EnvioMail
															em = new EnvioMail();
															em.setIdCorreo(idCorreoIni);
															em.setIdPlantilla(pm.getPlantillaMailId());
															em.setIdCliente(client.getIdCliente());
															em.setDireccionCorreo(client.getCorreoElectronico());
															em.setStatus(ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO));
															em.setIdCicloEjecucion(ciclo_id);
															em.setIdOrden(ord.getIdOrden());
															//Se agrega el correo a la lista de correos filtrados
															listaCorreosFiltrados.add(em);
															
															//Informacion para la vista
															infoClientArea.addNew();
															infoClientArea.setValue("correo_id", idCorreoIni+"");
															infoClientArea.setValue("ci_rif", client.getTipoPersona()+"-"+client.getRifCedula()+"");
															infoClientArea.setValue("nombre", client.getNombre());
															infoClientArea.setValue("correo", client.getCorreoElectronico());
															infoClientArea.setValue("orden_id", ord.getIdOrden()+"");
															infoClientArea.setValue("ui_name", ordDAO.getDataSet().getValue("undinv_nombre"));
															
															idCorreoIni++; //Se incrementa el id para el proximo correo
														}
													}
													
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
									
								}else{ //Si NO se especifican los ids de ordenes a buscar
									
									//ENVIO POR UNIDAD DE INVERSION
									Logger.debug(this, "ENTRA A ENVIO POR UI - Evento: "+_record.getValue("evento_id"));
									
									if(_record.getValue("ui_id")!=null && !_record.getValue("ui_id").trim().equals("")){
										
										if(_record.getValue("evento_id")!=null && !_record.getValue("evento_id").trim().equals("")){
											
											pm.setEventoId(_record.getValue("evento_id"));
											
											CallEnvioCorreos callEc = new CallEnvioCorreos(_record.getValue("evento_id"), PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, getUserName(), _record.getValue("ui_id"), _dso, null, null, null);
											
											DataSet ordenesNotificar = callEc.getOrdenesAnotificar(_dso, pm);
											
//											OrdenDAO ordDAO = new OrdenDAO(_dso);
//											String[] statusTransaUI = {_record.getValue("status_orden"), _record.getValue("transaccion"), _record.getValue("ui_id")};
											
											//Se obtienen las ordenes que cumplen con los filtros seleccionados
//												ordDAO.listarOrdenStatusTransaUIProdTit(null,statusTransaUI);
											
											if(ordenesNotificar.count()>0){
												Cliente client;
												Orden ord;
		Logger.debug(this, "Se hallaron ordenes en bd que cumplen con los filtros seleccionados");
												ordenesNotificar.first();
												
												for(int i=0; i<ordenesNotificar.count(); i++){
												
													ordenesNotificar.next();
													
													//Almacena informacion del cliente
													client = new Cliente();
													client.setIdCliente(Long.parseLong(ordenesNotificar.getValue("CLIENT_ID")));
													client.setCorreoElectronico(ordenesNotificar.getValue("CLIENT_CORREO_ELECTRONICO"));
													client.setNombre(ordenesNotificar.getValue("client_nombre"));
													client.setTipoPersona(ordenesNotificar.getValue("tipper_id"));
													client.setRifCedula(Long.parseLong(ordenesNotificar.getValue("client_cedrif")));
		Logger.debug(this, "orden "+ordenesNotificar.getValue("ORDENE_ID")+" Cliente seteado");
													//Almacena informacion de la orden
													ord = new Orden();
													ord.setCliente(client);
													ord.setIdOrden(Long.parseLong(ordenesNotificar.getValue("ORDENE_ID")));
													ord.setIdUnidadInversion(Integer.parseInt(ordenesNotificar.getValue("UNIINV_ID")));
													ord.setStatus(ordenesNotificar.getValue("ordsta_id"));
													ord.setIdTransaccion(ordenesNotificar.getValue("TRANSA_ID"));
													ord.setTipoProducto(ordenesNotificar.getValue("TIPO_PRODUCTO_ID"));
		Logger.debug(this, "orden "+ord.getIdOrden()+" Orden seteada");
														
													//Se crea el objeto EnvioMail
													em = new EnvioMail();
													em.setIdCorreo(idCorreoIni);
													em.setIdPlantilla(pm.getPlantillaMailId());
													em.setIdCliente(client.getIdCliente());
													em.setDireccionCorreo(client.getCorreoElectronico());
													em.setStatus(ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO));
													em.setIdCicloEjecucion(ciclo_id);
													em.setIdOrden(ord.getIdOrden());
													//Se agrega el correo a la lista de correos filtrados
													listaCorreosFiltrados.add(em);
													
													//Informacion para la vista
													infoClientArea.addNew();
													infoClientArea.setValue("correo_id", idCorreoIni+"");
													infoClientArea.setValue("ci_rif", client.getTipoPersona()+"-"+client.getRifCedula()+"");
													infoClientArea.setValue("nombre", client.getNombre());
													infoClientArea.setValue("correo", client.getCorreoElectronico());
													infoClientArea.setValue("orden_id", ord.getIdOrden()+"");
													infoClientArea.setValue("ui_name", ordenesNotificar.getValue("undinv_nombre"));
													
													idCorreoIni++; //Se incrementa el id para el proximo correo
													
												}//for
												
											}else{//No se hallaron ordenes que cumplan con los filtros
												valido = false;
												Logger.info(this,"ENVIO CORREOS: No se hallaron ordenes de clientes que cumplan con la plantilla: "+pm.getPlantillaMailName()+" para la Unidad de Inversion "+_record.getValue("ui_txt"));
												_record.addError("Información","No se hallaron órdenes de clientes que cumplan con la plantilla: "+pm.getPlantillaMailName()+" para la Unidad de Inversión "+_record.getValue("ui_txt"));
											}
											
											
										}else{//evento NO seteado
											valido = false;
											_record.addError("Evento", "Ocurrio un error al obtener el evento a notificar, intente de nuevo.");
										}
										
									}//UI id recibido
										
									
								}//Si no se especifican los ids de ordenes a buscar
								
								}//valido
								
							}else{ //UI o Ids de ordenes no seteados
								valido = false;
								_record.addError("Unidad de Inversión / ID de Orden(es)", "Debe seleccionar una Unidad de Inversión o indicar algún ID de Orden");
							}
							
						}else{ //Plantilla Invalida
							valido = false;
							_record.addError("Plantilla", "La plantilla configurada para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase()+" se encuentra inactiva");
						}
						
					}else{ //No encontro la plantilla
						valido = false;
						_record.addError("Plantilla", "No se encontr&oacute; ninguna plantilla para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase());
						//mensError = "No se encontr&oacute; ninguna plantilla para el estatus de orden: "+_record.getValue("name_status_orden_plant")+", la transacci&oacute;n: "+_record.getValue("name_transac_plant")+" y el tipo de destinatario: "+_record.getValue("name_dest").toUpperCase();
					}
					
//				}//Destinatario Cliente
					
//				}//Destinatario No Funcional
				
//			}else{ //tipo_destinatario==null
//				//mensError = "<span style=\"color:red;\">Error:</span> Debe indicar el tipo de destinatario";
//				valido = false;
//				_record.addError("Tipo de Destinatario", "Debe indicar el tipo de destinatario");
//			}
			
		}else{ //plantilla_id==null
			//mensError = "<span style=\"color:red;\">Error:</span> No se indic&oacute; la plantilla";
			valido = false;
			_record.addError("Plantilla", "No se suministr&oacute; la informaci&oacute;n requerida para obtener la plantilla (Evento");
		}
		
		return valido;
	}
}