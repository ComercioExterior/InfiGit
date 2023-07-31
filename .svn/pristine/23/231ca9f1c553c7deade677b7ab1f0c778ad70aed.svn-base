package com.programador.quartz.jobs;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.dao.AuditoriaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.IngresoMasivoCustodia;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaz_varias.CallEnvioCorreos;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de ingresar las custodias de los titulos pendientes por Orden según la FechaValor del Titulo<br>
 * Se inicia de forma automatica a traves del Scheduler de <b>QUARTZ</b><br>
 * Debe estar previamente configurado en el programador de tareas de <b>INFI<b><br>
 * @author elaucho
 */
public class QuartzIngresarCustodia implements Job{
	/**
	 * DAO a Utilizar
	 */	
		OperacionDAO 		operacionDAO = null;
		OrdenDAO 	 		ordenDAO = null;
		OrdenesCrucesDAO 	ordenesCrucesDAO = null;
		UnidadInversionDAO  unidadInversionDAO = null;
		IngresoMasivoCustodia ingresoCustodia;
		DataSource _dso;//= new DataSource();
		ArrayList<String> querysEjecutar=new ArrayList<String>();
		private Proceso proceso;
		private ProcesosDAO procesoDAO;
		boolean procesoCreado = false;
		private Logger logger = Logger.getLogger(QuartzIngresarCustodia.class);
		private DataSource dataSource;
		
		String descripcionError = "";
		String errorProceso = "";
		String mensajeError = "";
		private String ip;
		private String nombreUsuario;
		private int cantidadTotalOrdenes=0;
		private int cantidadTotalOrdenesProcesadas=0;
		private int cantidadLoteOrdenesProcesadas=0;
		boolean flag = true;
		String crucesId = ""; 
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		DataSet titulosProcesar=null;
		
		
		try {
			//DataSource _dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			_dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			operacionDAO = new OperacionDAO(_dso);
			ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenDAO = new OrdenDAO(_dso);
			ingresoCustodia = new IngresoMasivoCustodia(_dso);
			unidadInversionDAO = new UnidadInversionDAO(_dso);
			procesoDAO = new ProcesosDAO(_dso);
			Date fechaTitulo=null;
			int inserts = 0;
			flag = true;
			crucesId = ""; 
			
			//SE INICIA EL PROCESO DE CARGA DE CRUCES
			procesoCreado = true;// comenzarProceso();
			if(procesoCreado){
				logger.info("Se inicia proceso automatico de Registro de Custodias");
	
				//Se buscan las ordenes SICAD2 con Titulos pendientes por procesar
				ordenesCrucesDAO.consultarTitulosPorFechaValor(null,1);
				titulosProcesar = ordenesCrucesDAO.getDataSet();
				
				if (titulosProcesar.count()>0) {
					titulosProcesar.first();
					logger.info("Se encontraron "+ titulosProcesar.count() +"Titulos pendientes por procesar");
					while (titulosProcesar.next()) {
							
						 fechaTitulo =  Utilitario.StringToDate(titulosProcesar.getValue("FECHA_VALOR"), ConstantesGenerales.FORMATO_FECHA3);//(fechaActual)titulosProcesar.getValue("FECHA_VALOR");
						 System.out.println("Compara Actual --- " + new Date() +  " vs " + fechaTitulo + " del titulo " + titulosProcesar.getValue("TITULO_ID"));
						//if (((Utilitario.getHoraActual(ConstantesGenerales.FORMATO_FECHA).compareTo(titulosProcesar.getValue("FECHA_VALOR"))) >= 0) || ((Utilitario.getHoraActual(ConstantesGenerales.FORMATO_FECHA2).compareTo(titulosProcesar.getValue("FECHA_VALOR"))>=0))){
						 if (Utilitario.compareDates(new Date() ,fechaTitulo) >= 0){
							//REGISTRAR CUSTODIA DE TITULOS
							logger.info("Se inicia el registro de custodia para el Titulo "+ titulosProcesar.getValue("TITULO_ID") +" Perteneciente a la Orden" + titulosProcesar.getValue("ORDENE_ID"));
							procesarCustodiaTitulo(Long.parseLong(titulosProcesar.getValue("ORDENE_ID")),titulosProcesar.getValue("UNDINV_ID"), titulosProcesar.getValue("TITULO_ID"));
							if(flag){
								crucesId+=titulosProcesar.getValue("ID_CRUCE");
								flag = false;
							}else{crucesId+=","+titulosProcesar.getValue("ID_CRUCE");}
						}
						if(querysEjecutar!= null && !querysEjecutar.toString().equals("[]")){
							procesarQuerysPorLote(querysEjecutar);//SE PROCESA CADA 250 ORDNES

						}else logger.info("Los Titulos encontrados no cumplen con las condiciones para ser procesados");
					}//END WHILE
					//EJECUCIÓN DE QUERYS DEL ÚLTIMO LOTE
					if(querysEjecutar!= null && !querysEjecutar.toString().equals("[]")){
						System.out.println("querysEjecutar " + querysEjecutar.toString());
						ordenDAO.ejecutarStatementsBatch(querysEjecutar);
					}else logger.info("Los Titulos encontrados no cumplen con las condiciones para ser procesados");
					//TODO ALMACENAR EN UN HASH TODOS LOS CRUCES A UTILIZAR PARA MANDAR EL CORREO
					
					//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS **************
					CallEnvioCorreos callEnvio = new CallEnvioCorreos(ConstantesGenerales.RECEPCION_TITULO, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, null, _dso, null,null, crucesId);
					Thread t = new Thread(callEnvio); //Ejecucion del hilo que envia los correos
					t.start();
				}else logger.info("No se encontraron Titulos pendientes por procesar");
		
		}else{ //PROCESO NO UNICIADO
			//logger.error("No se pudo iniciar el proceso de carga de cruces puesto que ya existe un proceso en ejecución");
			proceso.setDescripcionError("No se pudo iniciar el proceso de carga de cruces "+proceso.getTransaId()+" puesto que ya existe un proceso en ejecución");
			//setMensajeError("Disculpe, no se pudo iniciar el proceso de carga de cruces puesto que ya existe un proceso en ejecuci&oacute;n. Intente nuevamente.<br/>");
		}
		}//fin try

		catch (Exception e) {
			try {
				logger.error(e.getMessage(),e);
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}finally{					
			try {			
				if (ordenDAO != null) {
					ordenDAO.cerrarConexion();
				}
				if (ordenesCrucesDAO != null) {
					ordenesCrucesDAO.cerrarConexion();
				}
				//REGISTRAR AUDITORIA
				registrarAuditoria();
		
			} catch (Exception e) {
				logger.error("Ha ocurrido un error al cerrar las conexiones en el proceso de Registro de Custodia "+e.getMessage());
			}											
		}//fin finally
	}//fin execute
	void procesarCustodiaTitulo(Long ordenId,String unidadInversion, String idTitulo) throws Exception {	
		
		BigDecimal montoOperacion;
		ArrayList<String> querysEjecutarOrden=new ArrayList<String>();	
		
		try {
			try {
				//CALCULAR MONTO ADJUDICADO EN DIVISAS (TABLA INFI_TB_229_ORDENES_CRUCES)						
				montoOperacion = ordenesCrucesDAO.montoCrucePorTipoCruce(ordenId.toString(),1);
				System.out.println("montoOperacion " + montoOperacion.toString());
				System.out.println("procesarCustodiaTitulo " + idTitulo +" "+ ordenId +" "+ unidadInversion);
				if (montoOperacion!=null) {
					System.out.println("Dentro de montoOperacion " + montoOperacion.toString());
					//Se consultan los operaciones pendientes por liquidacion
					boolean existe = ordenDAO.listarOperacionesLiquidacion(ordenId);
					//REGISTRAR CUSTODIA DE TITULOS
					System.out.println("ingresarCustodia " + idTitulo + ordenId + existe);
					String[] consultas = ingresoCustodia.ingresarCustodia(idTitulo, ordenId, false, existe);
					System.out.println("consultas " + consultas.toString());

					querysEjecutarOrden.addAll(Arrays.asList(consultas));
					querysEjecutarOrden.add(ordenesCrucesDAO.actualizarTitulosProcesados(ordenId, idTitulo));
					System.out.println("querysEjecutarOrden " + querysEjecutarOrden);
				}							
				if(querysEjecutarOrden!= null && !querysEjecutarOrden.equals("")){
				querysEjecutar.addAll(querysEjecutarOrden);
				}else	logger.info("El monto de la operacion para el titulo: " + idTitulo +" es nulo");
				
				querysEjecutarOrden.clear();
			} catch (Exception e) {
				logger.error("Error en la ejecución de el Titulo:" +idTitulo + ". Error: " + e.getMessage());
				querysEjecutarOrden.clear();
			}
		//EJECUCIÓN DE QUERYS DE ACUERDO A LA CANTIDAD DE ORDENES A PROCESAR POR LOTES
		//TODO procesarQuerysPorLote(querysEjecutar);					
				
		} catch (Exception e) {
			logger.error("Ha ocurrido un error en el Registro de Custodia "+e.getMessage());
			}		
	}
	
	/**
	 * Registra el inicio de un proceso en la tabla 807
	 * @return
	 * @throws Exception
	 */
	protected boolean comenzarProceso() throws Exception {
		procesoDAO.listarPorTransaccionActiva(proceso.getTransaId());
		if (procesoDAO.getDataSet().count() > 0) {
			logger.info("Proceso: " + proceso.getTransaId() + " ya esta en ejecución.");
			proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+" | Proceso: "+proceso.getTransaId()+" ya esta en ejecución.");
			setMensajeError(getMensajeError() + "Existe un proceso de Registro de Custodias en ejecución<br/>");
			return false;
		}
		try{
			//logger.debug(this, "-----NO ENCUENTRA PROCESO EN EJECUCION");
			ArrayList<String> querysEjecutar = new ArrayList<String>();
			//Se crea el query de insercion del proceso
			querysEjecutar.add(procesoDAO.insertar(proceso));
			boolean insertado = procesoDAO.ejecutarStatementsBatchBool(querysEjecutar);
			if(insertado){
				logger.info("Comenzó proceso: " + proceso.getTransaId());
			}else{
				logger.error("Error al crear el proceso de tipo: " + proceso.getTransaId());
				proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+" | Error al crear el proceso de tipo: "+proceso.getTransaId());
				setMensajeError(getMensajeError() + "Ocurri&oacute; un error al intentar iniciar el Job de Registro de Custodias <br/>");
				return false;
			}
		}catch(Exception e){
			logger.error("Error al crear el proceso de tipo: " + proceso.getTransaId());
			proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+" | Error al crear el proceso de tipo: "+proceso.getTransaId());
			setMensajeError(getMensajeError() + "Ocurri&oacute; un error al intentar iniciar el proceso de Job de Registro de Custodias<br/>");
			return false;
		}
		return true;
	}
	
	/**
	 * Registra el fin de un proceso en la tabla 807
	 */
	protected void terminarProceso() {
		try {
			if (proceso != null) {
				proceso.setFechaFin(new Date());
				if (proceso.getDescripcionError() == null) {
					proceso.setDescripcionError("");
				}
				proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+errorProceso);
				db.exec(this.dataSource, procesoDAO.modificar(proceso));
			}
			if (procesoDAO != null) {
				procesoDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error("Excepcion ocurrida al cerrar el proceso de tipo "+proceso.getTransaId()+": "+e.getMessage());
		} finally {
			if (proceso != null) {
				if(proceso.getFechaInicio()!=null && proceso.getFechaFin()!=null){
					final long duracion = proceso.getFechaFin().getTime() - proceso.getFechaInicio().getTime();
					logger.info("Termino proceso: " + proceso.getTransaId() + ", duracion: " + (duracion / 1000) + " secs.");
				}
			}
		}
	}
	
	/**
	 * Registra la auditoria del proceso de carga
	 * @throws Exception 
	 * @throws Exception 
	 */
	private void registrarAuditoria() {
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(dataSource);
		ArrayList<String> querys=new ArrayList<String>();
		AuditoriaDAO auditoriaDAO = new AuditoriaDAO(dataSource);
		
		logger.info("Registrando auditoria del Job de Registro de Custodias...");
			try {
				///********REGISTRAR LA AUDITORIA DE LA PETICIÓN DE LLAMADA AL REPROCESO DEL CIERRE DEL SISTEMA****///
				//Configuracion del objeto para el proceso de auditoria
				Auditoria auditoria= new Auditoria();
				auditoria.setDireccionIp(ip);			
				auditoria.setFechaAuditoria(Utilitario.DateToString(new Date(),ConstantesGenerales.FORMATO_FECHA));
				auditoria.setUsuario(nombreUsuario);			
				auditoria.setPeticion(proceso.getTransaId());
				auditoria.setDetalle(proceso.getDescripcionError()==null?"":proceso.getDescripcionError());	
				
				querys.add(auditoriaDAO.insertRegistroAuditoria(auditoria));
				auditoriaDAO.ejecutarStatementsBatch(querys);				
				
			}catch(Exception ex){
				try {
					transaccion.rollback();
				} catch (Exception e) {
					logger.error( "Ha ocurrido un error registrando la auditoría del Job de Registro de Custodias : " + ex.getMessage());
				}
			}finally{	
				try {
					if(transaccion.getConnection()!=null){
						transaccion.getConnection().close();
					}
					if(procesoCreado){ //Si se creo el proceso
						//TERMINAR PROCESO
						terminarProceso();
					}
				} catch (Exception e) {
					logger.error( "Ha ocurrido un error registrando la auditoría del proceso de Registro de Custodias : " + e.getMessage());
				}				
			}	
	}
	public String getMensajeError() {
		return mensajeError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	/**
	 * Método que maneja la ejeción por lotes de los querys del proceso de liquidación
	 * @author nm25287
	 * @throws SQLException
	 */
	protected void procesarQuerysPorLote(ArrayList<String> sentencias) throws SQLException{
		//EJECUCIÓN DE QUERYS
		logger.debug("EJECUCIÓN DE QUERYS");
		++cantidadTotalOrdenes;
		++cantidadLoteOrdenesProcesadas;
		if (ConstantesGenerales.COMMIT_REGISTROS_LIQ == cantidadLoteOrdenesProcesadas) {
			
			cantidadTotalOrdenesProcesadas = cantidadTotalOrdenesProcesadas + cantidadLoteOrdenesProcesadas;
			ordenDAO.ejecutarStatementsBatch(sentencias);
			sentencias.clear();
			cantidadLoteOrdenesProcesadas = 0;
			logger.info("Ordenes enviadas por COMMIT en proceso de LIQUIDACION : " + cantidadLoteOrdenesProcesadas);
			System.out.println("Se procesaron por lote procesarQuerysPorLote " + sentencias.toString());
			//TODO 
			//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Desde Aqui)**************
			CallEnvioCorreos callEnvio = new CallEnvioCorreos(ConstantesGenerales.RECEPCION_TITULO, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, null, _dso, null,null, crucesId);
			Thread t = new Thread(callEnvio); //Ejecucion del hilo que envia los correos
			t.start();	
			//Se reinician tambien los cruces a ser procesados
			crucesId = "";
			flag = true;
		}
		logger.info("Realizacion de commit al numero de registro N° " + cantidadTotalOrdenesProcesadas);
	}
}//fin clase