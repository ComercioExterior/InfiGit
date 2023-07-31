package models.utilitarios.envio_correos;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlCicloDAO;
import com.bdv.infi.dao.EnvioMailDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Ciclo;
import com.bdv.infi.data.EnvioMail;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;
//import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;

public class EnvioCorreosEnviar extends MSCModelExtend implements Runnable {
	/*
	private String correo_ids_concat;
	private String tipoFiltro;
	private String todos_val;
	 * */
	public EnvioCorreosEnviar(long ciclo_id,String plantillaId,String correo_ids_concat,String tipoFiltro,String todos_val,String tipoDest,String userName,DataSource dso){
		
		this.ciclo_id=ciclo_id;
		this.plantillaId=plantillaId;
		this.correo_ids_concat=correo_ids_concat;
		this.tipoFiltro=tipoFiltro;
		this.todos_val=todos_val;
		this.tipoDest=tipoDest;
		this.userName=userName;
		this._dso=dso;
	}
	
	
	DataSet infoCorreos;
	String[] areas_req;
	String[] codOrdenes;
	//Hashtable<String,PlantillaMailArea> areasInfo = new Hashtable<String,PlantillaMailArea>();
	//Hashtable<String,Orden> ordenesInfo = new Hashtable<String,Orden>();
	ArrayList<EnvioMail> listaCorreosFiltrados;
	PlantillaMail pm;
	ControlCicloDAO controlCicloDAO;
	EnvioMailDAO emDAO;
	long ciclo_id;
	EnvioCorreos ec;
	String correo_ids_concat;
	String tipoFiltro;
	String todos_val;
	String plantillaId;
	String tipoDest;
	String userName;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void run() {
		try {
			
			if(!inicializar()){
				Logger.debug(this,"Error en inicializacion de parametros del proceso de envio de correos");
				return;
			}			
		} catch (Exception e) {
			Logger.debug(this,"Ha ocurrido un error inesperado en el proceso de envio de correos: " + e.getMessage());
		}
		
		Logger.debug(this,"************* EJECUCION DE HILO *************");
		String mensError = "Proceso de env&iacute;o de correos finalizado";
		Logger.debug(this,"-------------------> FLAG 1");
		//Se inicializa el dataSet con datos para la vista
		DataSet datos = new DataSet();
		datos.append("mens_error", java.sql.Types.VARCHAR);
		datos.append("funcional_display", java.sql.Types.VARCHAR);
		Logger.debug(this,"-------------------> FLAG 2");
		int procesoCreado = 0;
		Ciclo ciclo = new Ciclo();
		
		try{
			Logger.debug(this,"-------------------> FLAG 3");
		datos.addNew();
		Logger.debug(this,"-------------------> FLAG 3.1");
		datos.setValue("mens_error", mensError);
		Logger.debug(this,"-------------------> FLAG 3.2");
		//Consulta el id del proximo proceso a aperturar		
		int proceso_id = Integer.parseInt(dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_PROCESOS));
		Logger.debug(this,"-------------------> FLAG 3.3");
Logger.debug(this, "ID del proximo proceso a crear: "+proceso_id);
Logger.debug(this,"-------------------> FLAG 4");
		//Creacion de Ciclo
		ciclo.setCicloId(ciclo_id);
		Logger.debug(this,"-------------------> FLAG 5");
		ciclo.setNombre(TransaccionNegocio.ENVIO_CORREOS.replaceAll("_", " "));
		ciclo.setExterno(ConstantesGenerales.FALSO);
		ciclo.setTipo(TransaccionNegocio.ENVIO_CORREOS);
		ciclo.setUsuarioRegistro(userName);
		ciclo.setStatus(ConstantesGenerales.STATUS_ENVIO_CORREO_INICIADO);
		Logger.debug(this,"-------------------> FLAG 6");
		//Se setea el ciclo al objeto EnvioCorreos
		ec.setCiclo(ciclo);
		
		//Creacion de Proceso
		Proceso proceso = new Proceso();
		proceso.setEjecucionId(proceso_id);
		Logger.debug(this,"-------------------> FLAG 7");
		proceso.setTransaId(TransaccionNegocio.ENVIO_CORREOS);
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		if(userName!=null && !userName.equals("")){			
			proceso.setUsuarioId(Integer.parseInt(usuarioDAO.idUserSession(userName)));
		}
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setCicloEjecucionId(ec.getCiclo().getCicloId());
		
		//Se setea el proceso al objeto EnvioCorreos
		ec.setProceso(proceso);
		
		//Se setea el DataSet con la informacion de los correos a enviar al objeto EnvioCorreos
		ec.setInfoCorreos(infoCorreos);
		
Logger.debug(this, "ANTES DE ACTUALIZAR EL CICLO");
		//Actualizacion de Ciclo
		if(ec.actualizarCicloEnvio(ciclo)){
			
Logger.debug(this, "SE ACTUALIZO EL CICLO");
			
			//REGISTRAR PROCESO
			procesoCreado = ec.comenzarProceso();
			
			if(procesoCreado==1){
				
Logger.debug(this, "Se creo el proceso");
				
				if(!todos_val.equalsIgnoreCase("todos")){ //Si no se marco "todos"
					//Se ELIMINAN los correos no seleccionados de la tabla de envio
					boolean deleted = false;
					if(tipoFiltro.equalsIgnoreCase("INCLUIR")){
						deleted = ec.borrarCorreos(ciclo_id, ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO), true, correo_ids_concat);
					}else{ //EXCLUIR
						deleted = ec.borrarCorreos(ciclo_id, ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO), false, correo_ids_concat);
					}
					if(!deleted){						
Logger.debug(this, "NO SE ELIMINARON LOS CORREOS NO SELECCIONADOS");
						//ec.terminarCiclo(ciclo.getCicloId(), null);
						ec.getProceso().setDescripcionError("No se pudieron eliminar los correos no seleccionadas para el ciclo "+proceso.getTransaId()+" en ejecucion");
						mensError += "No se pudieron eliminar los correos no seleccionados para el ciclo en ejecucion <br/>";
						Logger.info(this,"No se pudieron eliminar los correos no seleccionados para el ciclo en ejecucion");
					}else{
						Logger.debug(this, "Se eliminaron los correos no seleccionados");
					}
					
				}
					

				if ( infoCorreos.count() > 0 ) { //Si existen correos por enviar
					//for(int i=0; i<correosPorEnviar.size(); i++) { //Recorre los registros con la info de los correos por enviar
					infoCorreos.first();
					
					Logger.info(this, ec.getProceso().getTransaId()+": Hay "+infoCorreos.count()+" correos por enviar");
					
					ArrayList<EnvioMail> correos = new ArrayList<EnvioMail>();
					
					for(int i=0; i<infoCorreos.count(); i++) { //Recorre los registros con la info de los correos por enviar
						infoCorreos.next();
						
						//Se llena informacion del correo
				    	EnvioMail em = new EnvioMail();
				    	em.setIdCorreo(Long.parseLong(infoCorreos.getValue("CORREO_ID")));
				    	em.setIdPlantilla(Long.parseLong(infoCorreos.getValue("PLANTILLA_MAIL_ID")));
				    	em.setDireccionCorreo(infoCorreos.getValue("DIRECCION_CORREO"));
				    	//Se cambia estatus a CARGADO
				    	em.setStatus(ec.parametros.get(ParametrosSistema.STATUS_CARGADO));
				    	em.setIdCicloEjecucion(Long.parseLong(infoCorreos.getValue("CICLO_ID")));
				    	//em.setFechaRegistro(infoCorreos.getValue("FECHA_REGISTRO"));
				    	if(ec.getTipoDestinatario().equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){ //CLIENTE
				    		em.setIdCliente(Long.parseLong(infoCorreos.getValue("CLIENT_AREA_ID")));
				    		em.setIdOrden(Long.parseLong(infoCorreos.getValue("ORDENE_ID")));
				    	}else{
				    		if(ec.getTipoDestinatario().equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){ //FUNCIONAL
					    		em.setIdArea(Long.parseLong(infoCorreos.getValue("CLIENT_AREA_ID")));
					    	}
				    	}
				    	
				    	//Se añade el correo a la lista
				    	correos.add(em);
					}
					
					ec.setCorreos(correos); //Se setean los correos a enviar
					
					//Se setean los correos en estatus CARGADO
					if(ec.actualizarCorreo(null, correos, false, null)){ //Se cambio el estatus de los correos a CARGADO
						
						//SE EJECUTA HILO DE ENVIO DE CORREOS
Logger.info(this, "Envio Correos: Llamada al hilo del envio de correos...");
						
						ec.envioCorreos();
						/*Thread t = new Thread(ec); //Ejecucion del hilo que envia los correos
						t.start();*/
						
						//DE COLOCAR EL JOIN SE QUEDA LA VISTA ESPERANDO A QUE TERMINE EL PROCESO DE ENVIO
						//t.join();
						
					}else{
						Logger.info(this, ec.getCiclo().getTipo()+": No se pudo cambiar el estatus de los correos por enviar a "+ec.parametros.get(ParametrosSistema.STATUS_CARGADO));
		            	proceso.setDescripcionError(proceso.getDescripcionError()+" No se pudo cambiar el estatus de los correos por enviar a "+ec.parametros.get(ParametrosSistema.STATUS_CARGADO));
		            	//ec.terminarCiclo(ciclo.getCicloId(), ConstantesGenerales.STATUS_ENVIO_CORREO_FINALIZADO);
					}
					
				}else{
					Logger.info(this, ec.getCiclo().getTipo()+": No se hallaron correos por enviar.");
	            	proceso.setDescripcionError(proceso.getDescripcionError()+" No se hallaron correos por enviar.");
	            	//ec.terminarCiclo(ciclo.getCicloId(), ConstantesGenerales.STATUS_ENVIO_CORREO_FINALIZADO);
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
				//NO SE TERMINA EL CICLO PARA QUE PUEDAN ACCEDER A CONFIRMACION
				//ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
			}
			
		}else{
			ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Ocurrio un error al actualizar el ciclo ID "+ec.getCiclo().getCicloId()+" a estatus "+ec.getCiclo().getStatus());
			mensError += "Ocurri&oacute; un error al actualizar el ciclo de env&iacute;o de correos. Intente nuevamente. (Opci&oacute;n Confirmaci&oacute;n)";
			Logger.error(this,ec.getCiclo().getTipo()+": Ocurrio un error al actualizar el ciclo ID "+ec.getCiclo().getCicloId()+" a estatus "+ec.getCiclo().getStatus());
		}

Logger.debug(this, "RECORD\n "+_record);


		
		}catch (Exception e) {
			if(ec.getProceso()!=null){
			ec.getProceso().setDescripcionError(ec.getProceso().getDescripcionError()+" Error inesperado durante el proceso de tipo "+ec.getProceso().getTransaId()+". Estatus Ciclo: "+ec.getCiclo().getStatus()+". Mensaje de la Excepcion: "+e.getMessage());
			Logger.error(this,ec.getProceso().getTransaId()+": Ha ocurrido un error inesperado durante en el proceso de tipo "+ec.getProceso().getTransaId()+". Estatus Ciclo: "+ec.getCiclo().getStatus()+". Mensaje de la Excepcion: "+e.getMessage());
			}
			mensError = "Ha ocurrido un error inesperado durante el proceso de envio de correos. Puede reintentarlo mediante la Opci&oacute;n Confirmaci&oacute;n";
			//TERMINAR CICLO (No se termina para que se pueda acceder al ciclo mediante la opcion Confirmacion)
			//ec.terminarCiclo(ec.getCiclo().getCicloId(), null);
			if(ec.getProceso()!=null && procesoCreado==1){ //Por si el hilo ya habia finalizado el proceso se valida que no sea null
				//TERMINAR PROCESO
				ec.terminarProceso();
			}
			
		}finally{
			
			//El proceso se termina en el run del hilo de envio de correos
			/*if(procesoCreado==0){ //Si se creo el proceso
				//TERMINAR PROCESO
				ec.terminarProceso();
			}*/
			
			//REGISTRAR AUDITORIA
//			ec.registrarAuditoria();
		}
		
		try {
			
			datos.setValue("mens_error", mensError);				
			storeDataSet("datos", datos); //Registra el dataset exportado por este modelo		
			//storeDataSet("info_correos", infoClientArea); //Registra el dataset exportado por este modelo		
			storeDataSet("record", _record); //Registra el dataset exportado por este modelo
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.debug(this,"FIN DEL ENVIO DE CORREOS" );
	}
	
	
	
	public boolean inicializar() throws Exception {

		

		String cicloId = String.valueOf(ciclo_id);
		
		Logger.debug(this, "cicloId: "+cicloId);
		Logger.debug(this, "plantillaId: "+plantillaId);
		Logger.debug(this, "correo_ids_concat: "+correo_ids_concat);
		Logger.debug(this, "tipoFiltro: "+tipoFiltro);
		Logger.debug(this, "todos_val: "+todos_val);
		Logger.debug(this, "tipoDest: "+tipoDest);

		//Se preserva el ID del Ciclo y el tipo de destinatario para la generaciond el informe
		/*_record.setValue("ciclo", cicloId);
		_record.setValue("tipo_destinatario", tipoDest);
		Logger.debug(this, "cicloId: "+_record.getValue("ciclo"));
		Logger.debug(this, "tipo_destinatario: "+_record.getValue("tipo_destinatario"));*/
		
		boolean valido = true;
		
		ciclo_id = Long.parseLong(cicloId);
		
		//Creacion de objeto EnvioCorreos
		InetAddress direccion = InetAddress.getLocalHost();
    	String direccionIpstr = direccion.getHostAddress();
		ec = new EnvioCorreos(_dso, null, direccionIpstr);
		//Se inicializan los parametros relacionados con el envio de correos
		ec.initParamEnvio();
		
		ec.setTipoDestinatario(tipoDest);
		
		emDAO = new EnvioMailDAO(_dso);
		
		//SE OBTIENE INFO DE CORREOS
		if(!todos_val.equalsIgnoreCase("todos")){ //Si no se marco todos se traen por los ids de correos seleccionados
Logger.debug(this, "Listar Correos por ids");
			if(tipoFiltro.equalsIgnoreCase("INCLUIR")){
Logger.debug(this, "Listar INCLUYENDO ids");
				emDAO.listarCorreos(correo_ids_concat, true, tipoDest);
			}else{ //EXCLUIR
Logger.debug(this, "Listar EXCLUYENDO ids");
				emDAO.listarCorreos(correo_ids_concat, false, tipoDest);
			}
		}else{ //Si se marco todos se trae todos los correos para el ciclo y en estado precargado
Logger.debug(this, "Listar Correos TODOS por ciclo y precargado");
			emDAO.listarCorreos(null, false, tipoDest, cicloId, ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO));
		}
		
Logger.debug(this, "Despues Listar Correos");
		
		//Contiene la informacion de los correos a enviar
		infoCorreos = emDAO.getDataSet();
		
		if(infoCorreos.count()>0){
			
Logger.debug(this, "Se encontro info de los Correos en bd - count: "+infoCorreos.count());
			
		}else{
			valido = false;
			_record.addError("Correos Seleccionados", "No se pudo encontrar informaci&oacute;n en el sistema de los correos seleccionados. Para volver a intentarlo, confirme de nuevo los correos seleccionados mediante la opci&oacute;n de Confirmaci&oacute;n");
			Logger.error(this, "No se encontro informacion de los Correos precargados para el Ciclo ID "+cicloId);
		}
		
		return valido;
	}
}