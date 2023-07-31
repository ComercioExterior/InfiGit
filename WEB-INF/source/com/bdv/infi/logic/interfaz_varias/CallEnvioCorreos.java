package com.bdv.infi.logic.interfaz_varias;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.sql.DataSource;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlCicloDAO;
import com.bdv.infi.dao.EnvioMailDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.PlantillasMailDAO;
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
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de llamar a la ejecucion del proceso de Envio de Correos
 * @author Dayana Torres
 */
public class CallEnvioCorreos extends MSCModelExtend implements Runnable{
	
	private Logger logger = Logger.getLogger(CallEnvioCorreos.class);
	private String eventoId,tipoDest,user, uiId,ejecucionId,ordenesIds, idsCruce;
	private DataSource ds;
	private ParametrosDAO parametrosDAO;
	
	 public CallEnvioCorreos(String eventoId, String tipoDest, String user, String uiId, DataSource ds, String ejecucionId, String ordenesIds, String idsCruce){
//		 this.statusOrden=statusOrden;
//		 this.transaccion=transaccion;
		 this.eventoId=eventoId;
		 this.tipoDest=tipoDest;
		 this.user=user;
		 this.uiId=uiId;
		 this.ds=ds;
		 this.ejecucionId=ejecucionId;
		 this.ordenesIds=ordenesIds;
		 this.idsCruce=idsCruce;
	 }
	 
	 public void run(){
		
		logger.debug("-------EVENTO ID: "+eventoId);
		
		boolean cicloCreado = false;
		int procesoCreado = -1;
		PlantillaMail pm = new PlantillaMail();
	 
	try{
		
		//NM29643 - 15/12/2014 Se verifica el parametro general de activacion del envio de correos antes de crear el proceso
		
		//Busqueda de parametro ACTIVACION ENVIO AUTOMATICO DE CORREOS
		parametrosDAO=new ParametrosDAO(ds);
		String grupoParametroId=null;
	 
		try{
			parametrosDAO.buscarGrupoParametro(TransaccionNegocio.ENVIO_CORREOS);
			if(parametrosDAO.getDataSet().count()>0){
				parametrosDAO.getDataSet().first();
				while(parametrosDAO.getDataSet().next()){					
					grupoParametroId=parametrosDAO.getDataSet().getValue("PARGRP_ID");
					logger.debug("Se obtiene Id del grupo de parametros de la trasaccion ENVIO_CORREOS ---> "+grupoParametroId);
				}
			}
			parametrosDAO.listarParametros(TransaccionNegocio.CAMPO_INDICADOR_CORREO_ACTIVO,grupoParametroId);
			if(parametrosDAO.getDataSet().count()>0){				
				parametrosDAO.getDataSet().first();
				while(parametrosDAO.getDataSet().next()){
					String indicadorCorreo=parametrosDAO.getDataSet().getValue("PARVAL_VALOR");
					if(indicadorCorreo.equals(String.valueOf(ConstantesGenerales.STATUS_INACTIVO))){
						logger.info("Proceso de envio de correos automatico INACTIVO!!!");
						return;
					}else{
						logger.info("Proceso de envio de correos automatico ACTIVO");
					}
				}
			}else{
				logger.error("Error en el proceso de verificación de Parámetro Envío Automático de Correos: No se ha encontrado el parametro");
			}
		}catch(Exception e2){			
			logger.error("Error en proceso de verificacion Parametro ENVIO CORREOS: Ha ocurrido un error inseperado en el proceso de envio de correos automatico: " + e2.getMessage());
		}
		
		
		//Obtencion de la direccion IP del host
		InetAddress direccion = null;
		try{
			direccion = InetAddress.getLocalHost();
		}catch (UnknownHostException e1) {
			logger.debug("Excepcion ocurrida al obtener LocalHost: "+e1.getMessage());
			//e1.printStackTrace();
		}
		String direccionIpstr = direccion.getHostAddress();
		logger.debug("Ip: "+direccionIpstr+" _app: "+_app);
		
		//Creacion de objeto EnvioCorreos
		EnvioCorreos ec = new EnvioCorreos(ds, _app, direccionIpstr);
		
		ControlCicloDAO controlCicloDAO = new ControlCicloDAO(ds);
		
		//Consulta el id del proximo ciclo a aperturar (en caso de pasar isValid)
		long ciclo_id;
//		synchronized ( this ) {
			ciclo_id = controlCicloDAO.getSecuencia(ConstantesGenerales.SQ_CONTROL_CICLOS);
//		}
		logger.debug("idCicloIni ---------------- "+ciclo_id);
		
		//Consulta el id del proximo proceso a aperturar
		int proceso_id;
		Proceso proceso = new Proceso();
//		synchronized ( proceso ) {
			proceso_id = Integer.parseInt(dbGetSequence(ds, ConstantesGenerales.SECUENCIA_PROCESOS));
//		}
		logger.debug("proceso_id ---------------- "+proceso_id);			
		//Creacion de Proceso
		proceso.setEjecucionId(proceso_id);
		proceso.setTransaId(TransaccionNegocio.ENVIO_CORREOS);
		UsuarioDAO usuarioDAO = new UsuarioDAO(ds);
		if(user!=null && !user.equals("")){
			proceso.setUsuarioId(Integer.parseInt(usuarioDAO.idUserSession(user)));
		}
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setCicloEjecucionId(ciclo_id);
		proceso.setDescripcionError(eventoId+" ");
		
		//Se setea el proceso al objeto EnvioCorreos
		ec.setProceso(proceso);
		logger.debug("Se seteo proceso a objeto EnvioCorreos");
		
		//REGISTRAR PROCESO
		procesoCreado = ec.comenzarProceso();
		logger.debug("procesoCreado valor: "+procesoCreado);
		
		
		if(procesoCreado==1){ //Se inserto el proceso en BD
		
		logger.debug("Inicio de proceso de envio automatico de Correos asociados al Id de ejecucion: " + ejecucionId );
		
		
		try{
		
		//Se inicializan los parametros relacionados con el envio de correos
		ec.initParamEnvio();
		
		//Se setea el tipo de destinatario
		ec.setTipoDestinatario(tipoDest);
		
		//Se asignan los ids de cruce en caso de evento RECEPCION_TITULOS
		if(idsCruce!=null && !idsCruce.equals("")){
			ec.setIdsCruce(idsCruce); //se guardan los ids de cruce
		}
		
		EnvioMailDAO emDAO = new EnvioMailDAO(ds);
		//Consulta el id del proximo correo a precargar (en caso de pasar isValid)
		long idCorreoIni = emDAO.getSecuencia(ConstantesGenerales.SQ_ENVIO_MAIL);
logger.debug("idCorreoIni ---------------- "+idCorreoIni);
		
		//Se inicializa la lista de correos filtrados
		ArrayList<EnvioMail> listaCorreosFiltrados = new ArrayList<EnvioMail>();
		
		//BUSQUEDA PLANTILLA
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(ds);
			
//		if(statusOrden!=null && transaccion!=null && tipoDest!=null){
		if(eventoId!=null && tipoDest!=null){
			
			EnvioMail em;
			
logger.debug("ENVIO CORREOS: Datos para busqueda plantilla completos");
//			pm = new PlantillaMail();
//			pm.setStatusOrdenId(statusOrden);
//			pm.setTransaccionId(transaccion);
			pm.setEventoId(eventoId);
			pm.setTipoDestinatario(tipoDest);
			pm.setEstatusActivacion(String.valueOf(ConstantesGenerales.VERDADERO));
			
			plantillasMailDAO.listarPlantillasMail(pm, null, null, null, null);
			plantillasMailDAO.getDataSet();
			
			if(plantillasMailDAO.getDataSet().count()>0){
logger.debug("Encontro datos plantilla");
				plantillasMailDAO.getDataSet().first();
				plantillasMailDAO.getDataSet().next();
				
				pm.setPlantillaMailId(Integer.parseInt(plantillasMailDAO.getDataSet().getValue("PLANTILLA_MAIL_ID")));
				pm.setPlantillaMailName(plantillasMailDAO.getDataSet().getValue("PLANTILLA_MAIL_NAME"));
				
				//FUNCIONAL
				if(pm.getTipoDestinatario().equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){//Tipo de destinatario Funcional
logger.debug("Tipo de Destinatario Funcional");

					//BUSQUEDA AREAS PLANTILLA
					plantillasMailDAO.listarAreasPlantillasMail(plantillasMailDAO.getDataSet().getValue("PLANTILLA_MAIL_ID"), ConstantesGenerales.VERDADERO+"");
					DataSet areas = plantillasMailDAO.getDataSet();
					if(areas.count()>0){ //Se hallo al menos un area activa asociada a la plantilla
logger.debug("Encuentra "+areas.count()+" areas asociadas a la plantilla funcional");
						
						PlantillaMailArea pma;
						Hashtable<String,PlantillaMailArea> areasInfo = new Hashtable<String,PlantillaMailArea>();
						String[] destinatarios;
						
						areas.first();
						
						for(int j=0; j<areas.count(); j++){
							areas.next();
							if( areas.getValue("DESTINATARIO")==null || (areas.getValue("DESTINATARIO")!=null && areas.getValue("DESTINATARIO").trim().equals("")) ){
								logger.debug("No se realizara el envio de correos al area "+areas.getValue("PLANT_MAIL_AREA_NAME").toUpperCase()+" ya que no posee destinatarios.");
							}else{
								//Destinatarios seteados
								pma = new PlantillaMailArea();
								pma.setPlantMailAreaId(Integer.parseInt(areas.getValue("PLANT_MAIL_AREA_ID")));
								pma.setPlantMailAreaName(areas.getValue("PLANT_MAIL_AREA_NAME"));
								pma.setDestinatario(areas.getValue("DESTINATARIO")==null?"":areas.getValue("DESTINATARIO"));
								pma.setPlantillaMailId(Integer.parseInt(areas.getValue("PLANTILLA_MAIL_ID")));
								pma.setEstatusActivacion(Integer.parseInt(areas.getValue("ESTATUS_ACTIVACION")));
								
								//Si no se ha insertado el area seleccionada
								if(!areasInfo.containsKey(areas.getValue("PLANT_MAIL_AREA_ID"))){
									//Se guarda la informacion del area valida (la clave es el ID de area)
									areasInfo.put(areas.getValue("PLANT_MAIL_AREA_ID"), pma);
									
									//Se crea objeto EnvioMail por cada destinatario del area valida y se setea informacion para la vista
									destinatarios = pma.getDestinatario().split(",");
									for(int k=0; k<destinatarios.length; k++){
										if(destinatarios[k]!=null){
											em = new EnvioMail();
											em.setIdCorreo(idCorreoIni);
											em.setIdPlantilla(pm.getPlantillaMailId());
											em.setIdArea(Long.parseLong(pma.getPlantillaMailId()+""));
											em.setDireccionCorreo(destinatarios[k]);
											em.setStatus(ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO));
											em.setIdCicloEjecucion(ciclo_id);
											//Se agrega el correo a la lista de correos filtrados
											listaCorreosFiltrados.add(em);
																						
											idCorreoIni++; //Se incrementa el id para el proximo correo
										}
									}//for
									
								}//area nueva
								
							}//Destinatarios seteados
							
						}//for areas
						
					}else{
						logger.error("ENVIO CORREOS: No se hallo ningun area activa asociada a la plantilla funcional: "+pm.getPlantillaMailName()+" (ID: "+pm.getPlantillaMailId()+")");
					}
				}else{
					
				//CLIENTE
logger.debug("Tipo de destinatario Cliente");

					DataSet ordenesNotificar = getOrdenesAnotificar(ds, pm);
					
					logger.debug("Realiza consulta de ordenes, cantidad de ordenes filtradas: "+ordenesNotificar.count());
					//Se verifica que existan ordenes que cumplan con los filtros del evento a notificar
					if(ordenesNotificar.count()>0){
						
						Cliente client;
						Orden ord;
logger.debug("Se hallaron ordenes en bd que cumplen con los filtros seleccionados");
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
logger.debug("Orden "+ordenesNotificar.getValue("ORDENE_ID")+" Cliente seteado");
							//Almacena informacion de la orden
							ord = new Orden();
							ord.setCliente(client);
							ord.setIdOrden(Long.parseLong(ordenesNotificar.getValue("ORDENE_ID")));
							ord.setIdUnidadInversion(Integer.parseInt(ordenesNotificar.getValue("UNIINV_ID")));
							ord.setStatus(ordenesNotificar.getValue("ordsta_id"));
							ord.setIdTransaccion(ordenesNotificar.getValue("TRANSA_ID"));
							ord.setTipoProducto(ordenesNotificar.getValue("TIPO_PRODUCTO_ID"));
logger.debug("Orden "+ord.getIdOrden()+" seteada");
								
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
							
							idCorreoIni++; //Se incrementa el id para el proximo correo
							
						}//for ordenes
						
					}else{//No se hallaron ordenes que cumplen con los filtros
						logger.info("ENVIO CORREOS: No se hallaron ordenes de clientes que cumplan con la plantilla: "+pm.getPlantillaMailName()+" para la Unidad de Inversion ID "+uiId);
					}
					
				}//CLIENTE
				
				
				if(listaCorreosFiltrados!=null && listaCorreosFiltrados.size()>0){
					
				logger.debug("Se lleno arreglo con la informacion de los correos");

				//SE SETEA LISTA DE CORREOS POR ENVIAR
				ec.setCorreos(listaCorreosFiltrados); //Se setean los correos a enviar
				
				//Creacion de ciclo e insercion de tabla de correos 228
				Ciclo ciclo = new Ciclo();
				
				//Creacion de Ciclo
				ciclo.setCicloId(ciclo_id);
				ciclo.setNombre(TransaccionNegocio.ENVIO_CORREOS.replaceAll("_", " "));
				ciclo.setExterno(ConstantesGenerales.FALSO);
				ciclo.setTipo(TransaccionNegocio.ENVIO_CORREOS);
				ciclo.setUsuarioRegistro(user);
				ciclo.setStatus(ConstantesGenerales.STATUS_ENVIO_CORREO_PREINICIADO);
				
				//Se setea el ciclo al objeto EnvioCorreos
				ec.setCiclo(ciclo);
logger.debug("Se seteo ciclo a objeto EnvioCorreos");
				
				//Insercion de Ciclo en BD
				//if(ec.insertarCicloEnvio(ciclo)){
				
				//Script de Insercion de Ciclo en BD
				ec.insertarCicloEnvio(ciclo);
					
//					cicloCreado = true;
					
						//Insercion de info de correos a enviar en TB 228
						if(ec.insertarCorreos(listaCorreosFiltrados)){
							
							cicloCreado = true;
logger.debug("Se inserto ciclo");
logger.debug("Se insertaron los correos en la tabla 228");
							
							//Se incrementa la secuencia tantas veces como inserciones se realizaron menos una (por el nexval inicial)
//							synchronized ( this ) {
								for(int i=0; i<listaCorreosFiltrados.size()-1; i++){
									emDAO.getSecuencia(ConstantesGenerales.SQ_ENVIO_MAIL);
								}
//							}
logger.debug("Se incrementa la secuencia 228 "+(listaCorreosFiltrados.size()-1)+" veces");
//logger.debug("SQ ACTUAL --------------- "+emDAO.getSecuenciaActual(ConstantesGenerales.SQ_ENVIO_MAIL));
							
							//LLAMADA AL PROCESO DE ENVIO DE CORREOS
							
							//Se setean los correos en estatus CARGADO
							if(ec.actualizarCorreo(null, ec.getCorreos(), false, ec.parametros.get(ParametrosSistema.STATUS_CARGADO))){ //Se cambio el estatus de los correos a CARGADO
								
logger.debug("Se actualizaron los correos en la tabla 228 al estatus CARGADO");

								//SE OBTIENE INFO DE CORREOS POR ENVIAR
//								synchronized ( this ) {
									emDAO.listarCorreos(null, false, tipoDest, ec.getCiclo().getCicloId()+"", ec.parametros.get(ParametrosSistema.STATUS_CARGADO));
//								}
								
								//Se seta info de correos por enviar
								ec.setInfoCorreos(emDAO.getDataSet());
logger.debug("Cantidad de correos listados por enviar: "+ec.getInfoCorreos().count());
logger.debug("Se setea info de correos por enviar y se llama a EJECUCION el proceso de ENVIO");
								

								//LLAMADA AL PROCESO DE ENVIO DE CORREOS

								ec.envioCorreos();

								
							}else{
								if(ec.getProceso()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Ocurrio un error al intentar actualizar el estatus d elos correos a CARGADO");
								ec.borrarCorreos(ec.getCiclo().getCicloId(), ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO), false);
								logger.error(ciclo.getTipo()+": "+ec.getProceso().getDescripcionError());
								//SE TERMINA EL CICLO (Se deja el estatus en el cual se encontraba)
								if(cicloCreado) ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
							}
							

						}else{
							if(ec.getProceso()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Ocurrio un error al insertar en BD los correos filtrados (precargados)");
							ec.borrarCorreos(ec.getCiclo().getCicloId(), ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO), false);
							logger.error(ciclo.getTipo()+": "+ec.getProceso().getDescripcionError());
							//SE TERMINA EL CICLO (Se deja el estatus en el cual se encontraba)
							if(cicloCreado) ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
						}
					
						
				}else{//listaCorreosFiltrados.size > 0
					if(ec.getProceso()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" No se inicio el proceso debido a que no se filtraron correos por enviar");
				}
					
				/*}else{ //No se inserta el ciclo
					logger.error(ciclo.getTipo()+": Ocurrio un error al crear el ciclo de tipo "+ciclo.getTipo());
				}*/
				
			}else{
				logger.info("ENVIO CORREOS: No se hallo una plantilla configurada para el evento: "+eventoId+" y el tipo de destinatario: "+tipoDest);
			}
		}else{
			logger.info("ENVIO CORREOS: Datos faltantes para busqueda de plantilla de correos (evento y tipo de destinatario)");
		}
		
		
			
		}catch(Exception e) {
			if(ec.getProceso()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Error inesperado durante el proceso de tipo "+ec.getProceso().getTransaId());
			if(ec.getProceso()!=null && ec.getCiclo()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+". Estatus Ciclo: "+ec.getCiclo().getStatus());
			if(ec.getProceso()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+". Mensaje de la Excepcion: "+e.getMessage());
			logger.error("ENVIO CORREOS: Ha ocurrido un error inesperado durante en el proceso de Envio de Correos"+". Mensaje de la Excepcion: "+e.getMessage());
			logger.error("Causa del Error: "+e.getCause());
			e.printStackTrace();
			if(ec.getCiclo()!=null) logger.error("ENVIO CORREOS: Estatus Ciclo: "+ec.getCiclo().getStatus());
			//TERMINAR CICLO
			if(cicloCreado) ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
			
		}finally{
			
			if(procesoCreado==1){ //Si se creo el proceso
				//TERMINAR PROCESO
				ec.terminarProceso();
			}
			
			//REGISTRAR AUDITORIA
			if(ec!=null) ec.registrarAuditoria(ec.getProceso(), ec.getCiclo());
			logger.debug("Auditoria registrada...");
			
			
			
		}//finally
		
		
		}else{ //Proceso no creado
			logger.debug("Proceso NO creado!");
			//Siempre se debe setear primero la descripcion en el ciclo antes de terminar el ciclo (pasa a ser la observacion del ciclo)
			//Por ende se debe terminar primero el ciclo que el proceso
			if(procesoCreado==0){
				if(ec.getProceso()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" No se pudo crear proceso de tipo "+ec.getProceso().getTransaId()+" puesto que ya existe un proceso de dicho tipo en ejecucion.");
				if(ec.getProceso()!=null && ec.getCiclo()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Estatus Ciclo: "+ec.getCiclo().getStatus());
				if(ec.getProceso()!=null) logger.info(ec.getProceso().getTransaId()+": Ya existe un proceso de este tipo en ejecucion.");
			}else{
				if(procesoCreado==-1){
					if(ec.getProceso()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Ocurrio una excepcion al insertar en BD proceso de tipo "+ec.getProceso().getTransaId());
					if(ec.getProceso()!=null && ec.getCiclo()!=null) ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Estatus Ciclo: "+ec.getCiclo().getStatus());
					logger.error(ec.getProceso().getTransaId()+": Ocurrio una excepcion al insertar en BD proceso.");
				}
			}
			//NM29643 infi_TTS_466: A este nivel aun no se ha insertado el ciclo en bd
			//SE TERMINA EL CICLO (Se deja el estatus en el cual se encontraba)
//			if(cicloCreado) ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
		}
		 
		 }catch(Throwable e1){
			e1.printStackTrace();
			logger.error("Error general en proceso de ENVIO CORREOS: Ha ocurrido un error inesperado en el proceso de envio de correos automatico: "+e1.getLocalizedMessage());
			logger.error("Causa del Error: "+e1.getCause());
			e1.printStackTrace();
			if(procesoCreado==1){ //Se inserto el proceso en BD
				
			}
		 }
	}
	 
	 public DataSet getOrdenesAnotificar(DataSource ds, PlantillaMail pm) throws Exception{
		 
		//NM29643 infi_TTS_466 Envio por eventos de envio configurados
			OrdenDAO ordDAO = new OrdenDAO(ds);
			String[] statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct = new String[10];
			String tipoProd = "", estatusOrden = "";
			boolean filtrarCorreos = true;
			
			if(pm.getEventoId().toUpperCase().endsWith("PER")) tipoProd = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;
			else
				if(pm.getEventoId().toUpperCase().endsWith("RED")) tipoProd = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
				else
					tipoProd = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL + "', '" + ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
			
			logger.debug("Tipo Producto: "+tipoProd);
			logger.debug("Evento: "+pm.getEventoId());
			
			if(pm.getEventoId().equals(ConstantesGenerales.TOMA_ORDEN_RED)){
				//EVENTO TOMA_ORDEN_RED
				estatusOrden = StatusOrden.REGISTRADA;
				//NM29643 01/09/2014 Correccion para evitar envios de correos repetidos al mismo cliente
//				if(uiId!=null && !uiId.equals("")){
//					logger.debug("UI id: "+uiId);
//					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = estatusOrden;
//					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
//					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = uiId;
//					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
//					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
//					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = null;
//					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
//				}else{
				
				//NM29643 22/09/2014 Modificacion para modulo de envio de correos a demanda
				logger.debug("Ids ordenes: "+ordenesIds);
				statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = estatusOrden;
				statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
				if(uiId!=null && !uiId.equals("")){
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = uiId;
				}else{
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
				}
				if(ordenesIds!=null && !ordenesIds.equals("")){
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = ordenesIds;
				}else{
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
				}
				statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
				statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = null;
				statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
				
				if((uiId==null||uiId.equals("")) && (ordenesIds==null||ordenesIds.equals(""))){
					filtrarCorreos = false;
					logger.info("ENVIO CORREOS: No se especifico la Unidad de Inversion o los Ids de las ordenes a las cuales se les debe enviar el correo de notificacion ("+eventoId+")");
				}
				
//				}
					
				//Se obtienen las ordenes que cumplen con los filtros del evento a notificar
				if(filtrarCorreos) ordDAO.listarOrdenEnvioCorreos(ejecucionId, statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct);
				
			}else{
				if(pm.getEventoId().equals(ConstantesGenerales.ENVIO_BCV_PER) || pm.getEventoId().equals(ConstantesGenerales.ENVIO_BCV_RED)){
				//EVENTO ENVIO_BCV_PER OEVENTO ENVIO_BCV_RED
					estatusOrden = StatusOrden.ENVIADA;
					//NM29643 - infi_TTS_466_Calidad 29/09/2014
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = estatusOrden;
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
					if(uiId!=null && !uiId.equals("")){
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = uiId;
					}else{
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
					}
					if(ordenesIds!=null && !ordenesIds.equals("")){
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = ordenesIds;
					}else{
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
					}
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = null;
					statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
					
					if((uiId==null||uiId.equals("")) && (ordenesIds==null||ordenesIds.equals(""))){
						filtrarCorreos = false;
						logger.info("ENVIO CORREOS: No se especifico la Unidad de Inversion o los Ids de las ordenes a las cuales se les debe enviar el correo de notificacion ("+eventoId+")");
					}
					
					//Se obtienen las ordenes que cumplen con los filtros del evento a notificar
					if(filtrarCorreos) ordDAO.listarOrdenEnvioCorreos(ejecucionId, statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct);
					
				}else{
					if(pm.getEventoId().equals(ConstantesGenerales.CRUCE)){
					//EVENTO CRUCE
						//NM29643 - infi_TTS_466_Calidad 29/09/2014
						estatusOrden = StatusOrden.CRUZADA;
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = estatusOrden;
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
						if(uiId!=null && !uiId.equals("")){
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = uiId;
						}else{
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
						}
						if(ordenesIds!=null && !ordenesIds.equals("")){
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = ordenesIds;
						}else{
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
						}
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = null;
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7] = String.valueOf(ConstantesGenerales.VERDADERO);
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8] = null;
						statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9] = "1";
						
						if((uiId==null||uiId.equals("")) && (ordenesIds==null||ordenesIds.equals(""))){
							filtrarCorreos = false;
							logger.info("ENVIO CORREOS: No se especifico la Unidad de Inversion o los Ids de las ordenes a las cuales se les debe enviar el correo de notificacion ("+eventoId+")");
						}
						
						//Se obtienen las ordenes que cumplen con los filtros del evento a notificar
						if(filtrarCorreos) ordDAO.listarOrdenEnvioCorreos(ejecucionId, statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct);
						
					}else{
						if(pm.getEventoId().equals(ConstantesGenerales.NO_CRUCE)){
						//EVENTO NO_CRUCE
							//NM29643 - infi_TTS_466_Calidad 29/09/2014
							estatusOrden = StatusOrden.NO_CRUZADA;
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = estatusOrden;
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
							if(uiId!=null && !uiId.equals("")){
								statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = uiId;
							}else{
								statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
							}
							if(ordenesIds!=null && !ordenesIds.equals("")){
								statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = ordenesIds;
							}else{
								statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
							}
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = null;
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7] = String.valueOf(ConstantesGenerales.VERDADERO);
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8] = null;
							statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9] = "1";
							
							if((uiId==null||uiId.equals("")) && (ordenesIds==null||ordenesIds.equals(""))){
								filtrarCorreos = false;
								logger.info("ENVIO CORREOS: No se especifico la Unidad de Inversion o los Ids de las ordenes a las cuales se les debe enviar el correo de notificacion ("+eventoId+")");
							}
							
							//Se obtienen las ordenes que cumplen con los filtros del evento a notificar
							if(filtrarCorreos) ordDAO.listarOrdenEnvioCorreos(ejecucionId, statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct);
							
						}else{
							if(pm.getEventoId().equals(ConstantesGenerales.RECEPCION_TITULO)){
							//EVENTO RECEPCION_TITULO
								logger.debug("----------idsCruce: "+idsCruce);
//								ec.setIdsCruce(idsCruce); //se guardan los ids de cruce
								if(idsCruce!=null && !idsCruce.equals("")){
									//TODO Verificar status CRUZADA y Transaccion ENTRADA_DE_TITULOS
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = null;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = null;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = String.valueOf(ConstantesGenerales.VERDADERO);
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = idsCruce;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7] = String.valueOf(ConstantesGenerales.VERDADERO);
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8] = null;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9] = "1";
								}else{
									//NM29643 - infi_TTS_466_Calidad 29/09/2014
									estatusOrden = StatusOrden.CRUZADA;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = estatusOrden;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
									if(uiId!=null && !uiId.equals("")){
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = uiId;
									}else{
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
									}
									if(ordenesIds!=null && !ordenesIds.equals("")){
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = ordenesIds;
									}else{
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
									}
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = String.valueOf(ConstantesGenerales.VERDADERO);
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7] = String.valueOf(ConstantesGenerales.VERDADERO);
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8] = null;
									statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9] = "1";
									
									if((uiId==null||uiId.equals("")) && (ordenesIds==null||ordenesIds.equals(""))){
										filtrarCorreos = false;
										logger.info("ENVIO CORREOS: No se especifico la Unidad de Inversion o los Ids de las ordenes a las cuales se les debe enviar el correo de notificacion ("+eventoId+")");
									}
									
								}
								//Se obtienen las ordenes que cumplen con los filtros del evento a notificar
								if(filtrarCorreos) ordDAO.listarOrdenEnvioCorreos(ejecucionId, statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct);
								
							}else{
								if(pm.getEventoId().equals(ConstantesGenerales.LIQUIDACION_EFECTIVO)){
								//EVENTO LIQUIDACION_EFECTIVO
									if(ejecucionId!=null && !ejecucionId.equals("")){
										//TODO Verficicar estatus LIQUIDADA
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = StatusOrden.LIQUIDADA;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = String.valueOf(ConstantesGenerales.FALSO);
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7] = String.valueOf(ConstantesGenerales.VERDADERO);
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8] = StatusOrden.PROCESADA;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9] = "1";
									}else{
										
										//NM29643 - infi_TTS_466_Calidad 29/09/2014
										estatusOrden = StatusOrden.LIQUIDADA;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0] = estatusOrden;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1] = TransaccionNegocio.TOMA_DE_ORDEN;
										if(uiId!=null && !uiId.equals("")){
											statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = uiId;
										}else{
											statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2] = null;
										}
										if(ordenesIds!=null && !ordenesIds.equals("")){
											statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = ordenesIds;
										}else{
											statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3] = null;
										}
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4] = tipoProd;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5] = String.valueOf(ConstantesGenerales.FALSO);
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6] = null;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7] = String.valueOf(ConstantesGenerales.VERDADERO);
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8] = StatusOrden.PROCESADA;
										statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9] = "1";
										
										if((uiId==null||uiId.equals("")) && (ordenesIds==null||ordenesIds.equals(""))){
											filtrarCorreos = false;
											logger.info("ENVIO CORREOS: No se especifico la Unidad de Inversion o los Ids de las ordenes a las cuales se les debe enviar el correo de notificacion ("+eventoId+")");
										}
										
									}
									//Se obtienen las ordenes que cumplen con los filtros del evento a notificar
									if(filtrarCorreos) ordDAO.listarOrdenEnvioCorreos(ejecucionId, statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct);
									
								}
							}
						}
					}
				}
			}
		 
			return ordDAO.getDataSet();
	 }

	
}