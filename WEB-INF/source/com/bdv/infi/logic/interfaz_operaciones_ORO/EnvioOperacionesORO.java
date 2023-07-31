package com.bdv.infi.logic.interfaz_operaciones_ORO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;
import org.bcv.certificadORO.OroFileTransferPortBindingStub;

//import ve.org.bcv.ws.mtom.server.OperCamFileTransferPortBindingStub;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesDICOMDAO;
import com.bdv.infi.dao.SolicitudesORODAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.ConfigSubastaDICOM;
import com.bdv.infi.data.FormatoOperacionesDICOM;
import com.bdv.infi.data.FormatoOperacionesORO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudDICOM;
import com.bdv.infi.data.SolicitudORO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstatusOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstatusOperacionesORO;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesORO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.ArchivoRecepcion;
import com.bdv.infi.logic.interfaz_ops.BatchOps;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public class EnvioOperacionesORO extends BatchOps implements Runnable{
	
	public EnvioOperacionesORO(DataSource datasource,String usuarioGenerico) throws Exception {
		//super();		
		_dso = datasource;
		controlArchivoDAO= new ControlArchivoDAO(_dso);
		this.usuario=usuarioGenerico;
	}	
// MAJENO DE LOG PARA LA CLASE EN log4j	
	static final Logger logger = Logger.getLogger(EnvioOperacionesORO.class);
// Lista para el manejo de las Variables de la interfaz para ORO	
	protected HashMap<String, String> parametrosRecepcionORO;
	ArrayList<String> querysEjecutar=new ArrayList<String>();	
	FormatoOperacionesORO formatoArchivoRecepcion=new FormatoOperacionesORO();
	protected String transaccionEjecutar;
	boolean existeArchivo=false; 
	
	private String rutaRecepcion="";
	private String rutaRecepcion2="";
	private String rutaRespaldo="";	
	private String rutaEnvio="";	
	private String usuario="";
	private String cabecera = "";
	public static String DESCUADRE_TOTALES_EXCEPTION="DESCUADRE_TOTALES_EXCEPTION";
	public static String ERROR_CABECERA_EXCEPTION="ERROR_CABECERA_EXCEPTION";
	public String monedaSubasta="";
	private String detalleCuadre="";
	SolicitudesDICOMDAO solicitudesDICOMDAO = null;
	VehiculoDAO vehiculoDAO = null;
	BlotterDAO blotterDAO = null;
	TransaccionFijaDAO transaccionFijaDAO = null;
	private ConfigSubastaDICOM configSubastaDICOM;
	Proceso procesoEnEjecucion;
	String reenvioArchPreaprobadasOro="";
	
	public void run() {
		String jornadaOro="";
		String MonedaDicom="";
		String jornadafechaInicio="";
		String jornadafechaFin="";	
		String jornadaManual="";
							
		Date fechaActual=new Date();
		Date fechaInicioDate=null;
		Date fechaFinDate=null;
		File archivoRecibido=null;
		

		try {		
			//FORMATEAR FECHA ACTUAL SIN MINUTOS Y SEGUNDOS			
			try {
				fechaActual=Utilitario.fechaDateFormateada(new Date(), ConstantesGenerales.FORMATO_FECHA2);
				System.out.println("paso");
			} catch (Exception e) {
				logger.debug("Error en formato de fecha actual, se usará new Date()");
			}
			obtenerParametros();
			reenvioArchPreaprobadasOro = parametrosRecepcionORO.get(ParametrosSistema.REENVIO_OP_PREAPROBADAS_ORO);
			//VERIFICAR CICLO			
			//System.out.println("Parametro = "+ParametrosSistema.REENVIO_OP_PREAPROBADAS_ORO +"  Preuntas = "+reenvioArchPreaprobadasOro+"  Ciclo="+TransaccionNegocio.CICLO_BATCH_ORO);			
			if (reenvioArchPreaprobadasOro.equals("1") || verificarCiclo(TransaccionNegocio.CICLO_BATCH_ORO) ){
				//OBTENER PARAMETROS ORO
				//VALIDAR JORNADA, FECHA INICIO Y FECHA FIN
				jornadaOro = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_ORO);
				jornadafechaInicio = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_FECHA_INICIO);
				jornadafechaFin = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_FECHA_FIN);
				jornadaManual = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_ENVIO_MANUAL);
				monedaSubasta = parametrosRecepcionORO.get(ParametrosSistema.MONEDA_SUBASTA_ORO);
				rutaRecepcion = parametrosRecepcionORO.get(ParametrosSistema.RUTA_ORO_RECEP);
				rutaRecepcion2 = parametrosRecepcionORO.get(ParametrosSistema.RUTA_ORO_RECEP);
				rutaRespaldo = parametrosRecepcionORO.get(ParametrosSistema.RUTA_ORO_RESP);
				rutaEnvio = parametrosRecepcionORO.get(ParametrosSistema.RUTA_ORO_ENVIO);
				
					//VALIDAR FECHA
					fechaInicioDate=Utilitario.StringToDate(jornadafechaInicio, ConstantesGenerales.FORMATO_FECHA2);
					fechaFinDate=Utilitario.StringToDate(jornadafechaFin, ConstantesGenerales.FORMATO_FECHA2);
						//REGISTRAR PROCESO
						transaccionEjecutar=TransaccionNegocio.PROC_ORO_ENVIO;
						procesoEnEjecucion=comenzarProceso(1,transaccionEjecutar);
	//System.out.println("EnvioOperacionesORO-> Se validaran parametros de entrada. Proceso: "+transaccionEjecutar+" ProcesoEjecucion: "+getProceso().toString());						
	logger.info("EnvioOperacionesORO-> Se validaran parametros de entrada. Jornada: "+jornadaOro+" jornadafechaInicio: "+jornadafechaInicio+" jornadafechaFin: "+jornadafechaFin);
						if (getProceso()!=null) {
							try { 								
								//BUSCAR ARCHIVO EN WB ORO BCV					
								try {
									if(jornadaManual.equals("0")){
										rutaRecepcion=rutaRecepcion+getNombreArchivoRecepcion(jornadaOro);
								logger.info("EnvioOperacionesORO-> Antes de Consumir el servicio bajarSolicitudesRealizadas. Jornada: "+jornadaOro);
										OroFileTransferPortBindingStub stub = new OroFileTransferPortBindingStub(null,_dso);
										//Prueba de oro NM11383OperCamFileTransferPortBindingStub stub = new OperCamFileTransferPortBindingStub(null,_dso);
										 byte[] _bytes;						
										 _bytes=stub.bajarSolicitudesRealizadas(jornadaOro, monedaSubasta);
										 System.out.println("despues de bajarSolicitudesRealizadas");
								logger.info("EnvioOperacionesORO-> Despues de Consumir el servicio bajarSolicitudesRealizadas");
										//SE ESCRIBE EL ARCHIVO PROVISIONALMENTE EN EL SERVIDOR
										Utilitario.byteToFile(_bytes,rutaRecepcion);
								logger.info("depues del utilitario RUTA=  "+ rutaRecepcion);
									}else{
										rutaRecepcion=rutaRecepcion+getNombreArchivoRecepcion(jornadaOro);
										logger.info("EnvioOperacionesORO-> Consumo Manual de archivos "+rutaRecepcion);
									}									
									
									//LEER ARCHIVO
									archivoRecibido=getArchivo(rutaRecepcion);
								logger.info("EnvioOperacionesORO-> depues del utilitario Nombre de ARCHIVO=  "+ archivoRecibido + "  JORNADA ORO=" + jornadaOro);							
									//PROCESAR ARCHIVO
									recepcionArchivoOpPreaprobadas(archivoRecibido,jornadaOro);
								logger.info("EnvioOperacionesORO-> depues del recepcionArchivoOpPreaprobadas ORO=  "+ archivoRecibido + "  JORNADA ORO=" + jornadaOro);
									//RESPALDAR ARCHIVO RECIBIDO
									respaldarArchivo(archivoRecibido);
								logger.info("EnvioOperacionesORO-> depues del respaldarArchivo ORO=  "+ archivoRecibido + "  JORNADA ORO=" + jornadaOro);
								//System.out.println("depues del respaldarArchivo ORO=  "+ archivoRecibido + "  JORNADA ORO=" + jornadaOro);					
								} catch (Exception e1) {
									if(e1.getMessage().equals(DESCUADRE_TOTALES_EXCEPTION)||e1.getMessage().startsWith(ERROR_CABECERA_EXCEPTION)){
										proceso.agregarAlInicioDescripcionErrorTrunc(e1.getMessage(),true);
									}else{
										//System.out.println("llego al error");
										proceso.agregarDescripcionErrorTrunc("EnvioOperacionesORO->Error e1: "+e1.getMessage(),true);
									}
									
									logger.error("EnvioOperacionesORO-> e1"+e1.getMessage());
									e1.printStackTrace();
								}
	
							} catch (Exception e) {
								proceso.agregarDescripcionErrorTrunc("EnvioOperacionesORO->Error e: "+e.getMessage(),true);
								logger.error("EnvioOperacionesORO-> e"+e.getMessage());
								e.printStackTrace();
								
							}
						}else{
							logger.info("parametrosRecepcionORO-> Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
						}		
			}else{
				logger.info("parametrosRecepcionORO-> Ya se encuentra registrado un ciclo en ejecución");
			}
		} catch (Exception ex) {
			
			ex.printStackTrace();
			logger.error("Error en el proceso de recepción archivo batch para EnvioOperacionesPreaprobadasORO. ", ex);		
			logger.info("Exception ex ---> " + ex.getMessage() );
			if(proceso != null) {
				proceso.agregarDescripcionErrorTrunc(ex.getMessage(),true);
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de recepción de archivo batch para EnvioOperacionesPreaprobadasORO... ");
		}
	}
	
	protected File getArchivo(String ruta, String archivo) {
		String carpeta = parametrosRecepcionORO.get(ruta);		
		return new File(carpeta);

	}
	
	protected File getArchivo(String ruta) {
		return new File(ruta);
	}
	
	/**
	 * Metodo de busqueda de parametros asociados a interfaz con CERTIFICADO ORO
	 * */
	@Override
	protected void obtenerParametros() throws Exception {
		super.obtenerParametros();
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		
//TTS-537 Busqueda de parametros de transacciones a operaciones de Certificado en ORO NM11383 10-09-2018
		parametrosRecepcionORO=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_ORO);
	}
			
	protected void recepcionArchivoOpPreaprobadas(File archivo,String jornada) throws FileNotFoundException, IOException, Exception {
		if (!archivo.exists()) {
			//logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
			//Lee Archivo de Certificado en ORO y guarda en BD
			
			procesarArchivo(archivo,jornada);

			respaldarArchivo(archivo);		

		}
	}
	
	protected void procesarArchivo(File archivo, String jornadaOro) throws Exception {
		//boolean uiOfertaPorCrear =true;
		//boolean uiDemandaPorCrear=true;
		SolicitudesORODAO solicitudesORODAO;
		ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);

		String linea = archivoRecepcion.leerLinea();
		System.out.println("linea--->"+linea);
		
		solicitudesORODAO = new SolicitudesORODAO(_dso);
		SolicitudORO solicitudOro;
		//SolicitudDICOM solicitudDICOMTotalesArchivo=null;
		//SolicitudDICOM solicitudDICOMTotalesRegistrados=null;
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
		ArrayList<String> registrosCuerpoDeArchivo= new ArrayList<String>();
		boolean ejecutarCommit=false;
		File archivoEnviado=null;
		String idEjecucion = "";
		
		rutaEnvio=rutaEnvio+getNombreArchivoEnvio(jornadaOro);
		if(reenvioArchPreaprobadasOro.equals("0")){//Se inicia el ciclo en caso de NO  estar procesando un reenvio de archivo al mainframe			
			//ABRIR CICLO
			idEjecucion = controlArchivoDAO.obtenerNumeroDeSecuencia(); //CORRECCION DE MANEJO ERRADO DEL ID DE CICLO NM25287 12/06/2017
			
			Archivo controlCiclo = new Archivo();			
			//controlCiclo.setIdEjecucion(getProceso().getEjecucionId()); //CORRECCION DE MANEJO ERRADO DEL ID DE CICLO NM25287 12/06/2017
			controlCiclo.setIdEjecucion(Integer.parseInt(idEjecucion));
			controlCiclo.setNombreArchivo(getDestinoFinal());		
			controlCiclo.setFechaGeneracion(new Date());
			controlCiclo.setInRecepcion(0);		
			controlCiclo.setStatus(TransaccionNegocio.CICLO_BATCH_ORO);
			controlCiclo.setUsuario(usuario);
			
			db.execBatch(this._dso,controlArchivoDAO.insertarArchivoRecepcion(controlCiclo));
		}// Cambio para Buscar el Ciclo cuando es un reenvio del archivo 1 Alexander Rincon NM11383 14/03/2018 
		else if(reenvioArchPreaprobadasOro.equals("1")) {
			    idEjecucion = controlArchivoDAO.obtenerCicloProcesoOro();
		}
		//CREAR CABECERA PARA MANEJO CERTIFICADO ORO
		if (linea != null) {
			if (linea.length() > 0) {		
				//cabecera=linea;
				cabecera = Utilitario.rellenarCaracteres("01",' ',EstructuraArchivoOperacionesORO.TIPO_REGISTRO.getValor(),false);
				cabecera = cabecera + Utilitario.rellenarCaracteres(idEjecucion,' ',EstructuraArchivoOperacionesORO.ID_CICLO_LONG.getValor(),false);
				cabecera = cabecera + Utilitario.rellenarCaracteres(jornadaOro,' ',EstructuraArchivoOperacionesORO.NRO_JORNADA.getValor(),false);
				cabecera = cabecera + Utilitario.rellenarCaracteres(this.monedaSubasta,' ',EstructuraArchivoOperacionesORO.DIVISA_EXT_LONG.getValor(),false);
				//solicitudDICOMTotalesArchivo=formatoArchivoRecepcion.validarCabeceraOpPreaprobadas(jornadaOro);
				//linea = archivoRecepcion.leerLinea();
			}
		}
//System.out.println("Cabecera=  "+ cabecera);		
		//PROCESAR DETALLE DEL ARCHIVO
		formatoArchivoRecepcion.setParamtrosSistemaORO(parametrosRecepcionORO);
		while (linea != null) {
				if (linea.length() > 0) {					
					try {
						//CARGAR CUERPO OPERACIONES PREAPROBADAS
						formatoArchivoRecepcion.cargarOperacionesOro(linea);
						//VALIDAR CUERPO OPERACIONES PREAPROBADAS
//System.out.println("IdOper= "+formatoArchivoRecepcion.getIdOperacion()+"   Linea= "+linea);
						if(((formatoArchivoRecepcion.getIdOperacion().trim()).length()>0)){
							solicitudOro=formatoArchivoRecepcion.validarOperacionesOro();							
							if(solicitudOro!=null){
								ejecutarCommit=true;
								solicitudOro.setIdJornada(jornadaOro);
								if(reenvioArchPreaprobadasOro.equals("0")){//Si no hay reenvio de archivo se guarda el registro a base de datos  
									solicitudesORODAO.registrarSolicitudORO(solicitudOro,ejecutarCommit);
									System.out.println("paso registrarSolicitudORO");
								}
								registrosCuerpoDeArchivo.add(formatoArchivoRecepcion.crearCuerpoOpPreaprobadas(linea));
							}else{
								proceso.agregarDescripcionErrorTrunc("EnvioOperacionesPreaprobadasORO.procesarArchivo: La linea " + linea + " no se puede registrar",true);
								logger.error("EnvioOperacionesPreaprobadasORO.procesarArchivo: La linea " + linea + " no se puede registrar");
							}
						}else{
							proceso.agregarDescripcionErrorTrunc("EnvioOperacionesPreaprobadasORO.procesarArchivo: La linea " + linea + " no se puede registrar - Id invalido",true);
							logger.error("EnvioOperacionesPreaprobadasORO.procesarArchivo: La linea " + linea + " no se puede registrar - Id invalido");
						}
					} catch (Exception e) {
						e.printStackTrace();
						proceso.agregarDescripcionErrorTrunc("La linea " + linea + " no se puede registrar - Error: "+e.getMessage(),true);
						logger.error("EnvioOperacionesPreaprobadasORO.procesarArchivo: La linea " + linea + " no se puede registrar - Error: "+e.getMessage());
					}						
				}//Fin if length linea
				linea = archivoRecepcion.leerLinea();						
			}
		
		  	logger.debug("EnvioOperacionesPreaprobadasORO.procesarArchivo: registrosCuerpoDeArchivo.size()= "+registrosCuerpoDeArchivo.size());							
			
		  	if (registrosCuerpoDeArchivo.size()>0) {
				//CREAR ARCHIVO			
				formatoArchivoRecepcion.setNroJornada(jornadaOro);
				formatoArchivoRecepcion.setIdCiclo(idEjecucion);
				formatoArchivoRecepcion.setMonedaSubasta(this.monedaSubasta);				
			//registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCabecera(cabecera,parametrosRecepcionORO));
				registrosDeArchivoDefinitivo.add(cabecera);				
			// Comentado por Alexander Rincon 31/01/2018
			//registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCabeceraDicomInterbancario(jornadaDicom));
				registrosDeArchivoDefinitivo.addAll(registrosCuerpoDeArchivo);
	logger.info("Escribiendo en archivo temporal " + rutaEnvio);
	//System.out.println("Escribiendo en archivo temporal " + rutaEnvio);
				FileUtil.put(rutaEnvio, registrosDeArchivoDefinitivo, true);
			//ENVIAR ARCHIVO A MAINFRAME
	logger.info("Antes de transferir a Mainframe ");
				transferirArchivo(rutaEnvio, getDestinoFinal());
	logger.info("Despues de transferir a Mainframe " );
			//RESPALDAR ARCHIVO ENVIADO
				archivoEnviado = getArchivo(rutaEnvio);
				respaldarArchivo(archivoEnviado);
			}else{
				proceso.agregarDescripcionErrorTrunc("No existen operaciones a enviar. registrosCuerpoDeArchivo.size()= "+registrosCuerpoDeArchivo.size(),true);
			}
			
			//VALIDAR TOTALES DE LA CABECERA CONTRA LO REGISTRADO EN BD
			//solicitudDICOMTotalesRegistrados=solicitudesDICOMDAO.listarTotalesSolicitudesPreaprobadasDICOM(jornadaOro);				
			//if(!compararTotales(solicitudDICOMTotalesArchivo,solicitudDICOMTotalesRegistrados)){//TODO CUADRE DE TOTALES COMENTADO HASTA SABER EXTRUCTURA DE ARCHIVO
	logger.info("EnvioOperacionesORO.procesarArchivo Archivo = "+ archivoEnviado.exists());
	//System.out.println("EnvioOperacionesORO.procesarArchivo Archivo = "+ archivoEnviado.exists());
		  	if (archivoEnviado.exists()){//TODO CUADRE DE TOTALES COMENTADO HASTA SABER EXTRUCTURA DE ARCHIVO
				//ACTUALIZAR OPERACIONES INVALIDAS
				solicitudesORODAO.actualizarEstatusRegistroSolicitudes(jornadaOro, EstatusOperacionesORO.ESTATUS_CARGADA.getValor(), EstatusOperacionesORO.ESTATUS_ERROR_VALIDACION_TOTALES.getValor());
				//PROCESAR REGISTROS INVALIDOS
	logger.info("EnvioOperacionesORO.procesarArchivo Exceptio = DESCUADRE_TOTALES_EXCEPTION");				
				throw new Exception(DESCUADRE_TOTALES_EXCEPTION);				 //Los valores de la Cabecera no concuerdan con los totales registrados. Verifique el archivo 
			}
	}		


	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}	
	/**
	 * Obtiene la carpeta de respaldo
	 */
	/*protected String getCarpetaOfertaRespaldo() {
		String carpeta = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_OFERTA_RESP);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
	
	protected String getCarpetaDemandaRespaldo() {
		String carpeta = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_DEMANDA_RESP);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}*/

	/**
	 * Método que maneja la ejeción por lotes de los querys del proceso de liquidación
	 * @author nm25287
	 * @throws SQLException
	 */
	/*protected boolean procesarQuerysPorLote() throws SQLException{
		//EJECUCIÓN DE QUERYS
		++cantidadTotalOrdenes;
		++cantidadLoteOrdenesProcesadas;
		if (ConstantesGenerales.COMMIT_REGISTROS_LIQ == cantidadLoteOrdenesProcesadas) {
			cantidadTotalOrdenesProcesadas = cantidadTotalOrdenesProcesadas + cantidadLoteOrdenesProcesadas;		
			logger.info( "Se activa ejecucion de COMMIT en proceso de EnvioOperacionesPreaprobadasDICOM : " + cantidadLoteOrdenesProcesadas);
			cantidadLoteOrdenesProcesadas = 0;
			return true;
		}
		return false;		
	}*/
	
	public void setProceso(Proceso proceso){
		this.proceso=proceso;
	}
	
	public Proceso getProceso() {
		return proceso;
	}
	
	public Proceso comenzarProceso(int usuarioId,String... tipoProceso) throws Exception {	
		Proceso proceso= new Proceso();
		String procesoEjecucion=new String();		
		StringBuffer procesoValidacion=new StringBuffer();		
		procesoDAO = new ProcesosDAO(_dso);
		
		if(tipoProceso.length>0){
			procesoEjecucion=tipoProceso[0];//El primer parametro que se pasa es del proceso que se esta ejecutando			
			int count=0;
			for (String element : tipoProceso) {
				if(count>0){
					procesoValidacion.append(",");									
				}							
				procesoValidacion.append("'"+element+"'");
				++count;
			}
		}
		
		procesoDAO.listarPorTransaccionActivaSubastaDivisasSubastaTitulo(procesoValidacion.toString());

		if (procesoDAO.getDataSet().count() > 0) {
			logger.info("Proceso: " + tipoProceso + " ya esta en ejecución.");
			return null;
		}
		logger.info("TIPO PROCESO EN EJECUCION -----------------> " + procesoEjecucion + " -------------------> ");
System.out.println("TIPO PROCESO EN EJECUCION -----------------> " + procesoEjecucion + " -------------------> ");		
		proceso = new Proceso();
		proceso.setTransaId(procesoEjecucion);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		logger.info("TIPO PROCESO INSERTADO: " + proceso.getTransaId() + " -------------------> ");
System.out.println("TIPO PROCESO INSERTADO: " + proceso.getTransaId() + " -------------------> ");
		db.exec(_dso, procesoDAO.insertar(proceso));

		logger.info("Comenzó proceso: " + tipoProceso);
		setProceso(proceso);
		return proceso;
	}
	
	
	protected String getFechaHora() throws ParseException{		
		return Utilitario.DateToString(new Date(), "ddMMyyyyhhmmss");
	}
	
	protected String getNombreArchivoRecepcion(String jornada) throws ParseException{		
		return "preaprobadas_recepcion_"+jornada+".txt";
	}
	
	protected String getNombreArchivoRecepcionManualRespaldo(String jornada) throws ParseException{		
		return "preaprobadas_recepcion_manual_"+jornada+".txt";
	}
	
	protected String getNombreArchivoEnvio(String jornada) throws ParseException{		
		return "preaprobadas_envio_"+jornada+".txt";
	}

	protected String getDestinoFinal(){	
		return parametrosRecepcionORO.get(getNombreArchivo());
	}
	
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_ORO_ENVIO;
	}
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosRecepcionORO.get(ParametrosSistema.RUTA_ORO_RESP);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
	
	protected void respaldarArchivo(File archivo, boolean borrarArchivo) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("carpetaRespaldo: " + getCarpetaRespaldo());
		}
		logger.info("PROCESO DE RESPALDO DE ACHIVO ----------->");
		final String carpeta = getCarpetaRespaldo();

		final File carpetaRespaldo = new File(carpeta);
		carpetaRespaldo.mkdirs();

		// agregar fecha/hora al nombre del archivo respaldo
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(archivo.getName().substring(0, archivo.getName().length() - 4)); // nombre

		stringBuilder.append(sdfArchivoRespaldo.format(new Date()));
		stringBuilder.append(archivo.getName().substring(archivo.getName().length() - 4)); // extension

		File destino = new File(carpeta.concat(stringBuilder.toString()));

		logger.info("Respaldar: " + archivo.getAbsolutePath() + " a: " + destino.getAbsolutePath());

		FileUtil.copiarArchivo(archivo, destino);
		// if (archivo.renameTo(destino)) {
		if (borrarArchivo) {
			archivo.delete();
		}
		// }
	}
	
	protected boolean verificarCiclo(String... ciclos) throws Exception {
		
		StringBuffer ciclosVerificar=new StringBuffer();
		boolean puedeEjecutar = false;
		
		// si estado de la unidad de inversion es adecuado para la liquidacion			
		int count=0;
		for (String element : ciclos) {
			if(count>0){
				ciclosVerificar.append(",");
			}
			ciclosVerificar.append("'"+element+"'");
			++count;
		}
		
		controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(ciclosVerificar.toString());
		DataSet dataset = controlArchivoDAO.getDataSet();		
		
		if(dataset.count() == 0 ) {			
			puedeEjecutar = true;
		}else{
			puedeEjecutar = false;
		}
		return puedeEjecutar;
	}
	
	protected void terminarProceso() {
		try {
			System.out.println("antes proceso--->"+proceso);
			if (proceso != null) {
				
				proceso.setFechaFin(new Date());
				System.out.println("antes proceso.getDescripcionError()--->"+proceso.getDescripcionError());
				if (proceso.getDescripcionError() == null) {
					System.out.println("despues proceso.getDescripcionError()+cabecera+detalleCuadre--->"+proceso.getDescripcionError()+cabecera+detalleCuadre);
					proceso.setDescripcionError("Cabecera:"+cabecera+detalleCuadre);
				}else{
					proceso.setDescripcionError("Cabecera:"+cabecera+detalleCuadre+"-"+proceso.getDescripcionError());
			
				}
				db.exec(this._dso, procesoDAO.modificar(proceso));
			}

			if (procesoDAO != null) {
				procesoDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (proceso != null) {
				final long duracion = proceso.getFechaFin().getTime() - proceso.getFechaInicio().getTime();
				logger.info("Termino proceso: " + proceso.getTransaId() + ", duracion: " + (duracion / 1000) + " secs.");
			}
		}
	}
	
	public boolean compararTotales(SolicitudDICOM totalesArchivo,SolicitudDICOM totalesRegistrados){
		boolean totalesIguales=true;
		logger.info("compararTotales--->");
		detalleCuadre=",TOTALES_ARCHIVO: "+totalesArchivo.toStringCabecera()+",TOTALES_REGISTRADO:"+totalesRegistrados.toStringCabecera();
		logger.info("totalesArchivo: "+totalesArchivo.toStringCabecera());
		logger.info("totalesRegistrados: "+totalesRegistrados.toStringCabecera());
		
		if(totalesArchivo!=null&&totalesRegistrados!=null){
			if(totalesArchivo.getTotalRegistrosDemanda().compareTo(totalesRegistrados.getTotalRegistrosDemanda())!=0){
				totalesIguales=false;
			}
			if(totalesArchivo.getTotalRegistrosOferta().compareTo(totalesRegistrados.getTotalRegistrosOferta())!=0){
				totalesIguales=false;
			}			
			if(totalesArchivo.getTotalMontoBolivaresDemanda().compareTo(totalesRegistrados.getTotalMontoBolivaresDemanda())!=0){
				totalesIguales=false;
			}
			if(totalesArchivo.getTotalMontoBolivaresOferta().compareTo(totalesRegistrados.getTotalMontoBolivaresOferta())!=0){
				totalesIguales=false;
			}
			if(totalesArchivo.getTotalMontoDolarDemanda().compareTo(totalesRegistrados.getTotalMontoDolarDemanda())!=0){
				totalesIguales=false;
			}
			if(totalesArchivo.getTotalMontoDolarOferta().compareTo(totalesRegistrados.getTotalMontoDolarOferta())!=0){
				totalesIguales=false;
			}			
		}
		return totalesIguales;
	}
	
	public void configBaseDicom(String tipoSolicitud,String nroJornada,int tipoProceso) throws Exception {

		solicitudesDICOMDAO = new SolicitudesDICOMDAO(_dso);
		vehiculoDAO = new VehiculoDAO(_dso);
		blotterDAO = new BlotterDAO(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		controlArchivoDAO = new ControlArchivoDAO(_dso);
		com.bdv.infi.data.TransaccionFija transaccionFija = null;
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		String usuario = parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES, _dso);
		
		InetAddress direccion = InetAddress.getLocalHost();
		String direccionIpstr = direccion.getHostAddress();
		
		/*UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO( _dso); 
		int usuario = Integer.parseInt((userEspecialDAO.idUserSession(usuario));
		Integer.parseInt((userEspecialDAO.idUserSession(getUserName())));*/
		
		long idUnidadInv = 0;
		configSubastaDICOM = new ConfigSubastaDICOM();
		idUnidadInv = solicitudesDICOMDAO.creacionUnidadInvDICOM(tipoSolicitud, nroJornada, usuario,direccionIpstr,tipoProceso);
		configSubastaDICOM.setIdUnidadInv(idUnidadInv);
		configSubastaDICOM.setNroJornada(nroJornada);
		UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
		String empresaID=null;
		String vehiculoBDV=null;
		String blotterId=null;
		
		uiDAO.obtenerDatosUIporId("EMPRES_ID, TIPO_SOLICITUD", String.valueOf(idUnidadInv));
		if (uiDAO.getDataSet() != null && uiDAO.getDataSet().count() > 0) {
			uiDAO.getDataSet().next();
			empresaID = uiDAO.getDataSet().getValue("EMPRES_ID");
			//tipoSolicitudNumeric = Integer.parseInt(uiDAO.getDataSet().getValue("TIPO_SOLICITUD"));
		}

		vehiculoDAO.obtenerVehiculoBDV();
		if (vehiculoDAO.getDataSet() != null && vehiculoDAO.getDataSet().count() > 0) {
			vehiculoDAO.getDataSet().next();
			vehiculoBDV = vehiculoDAO.getDataSet().getValue("vehicu_id");
		}

		blotterDAO.listarBlotterPorUI(String.valueOf(idUnidadInv));
		if (blotterDAO.getDataSet() != null && blotterDAO.getDataSet().count() > 0) {
			blotterDAO.getDataSet().next();
			blotterId = blotterDAO.getDataSet().getValue("BLOTER_ID");
		}
		configSubastaDICOM.setEmpresId(empresaID);
		configSubastaDICOM.setVehiculoBDVId(vehiculoBDV);
		configSubastaDICOM.setBlotterId(blotterId);
		
		transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.CAPITAL_SIN_IDB);
		configSubastaDICOM.setCodOperacionBloCap(transaccionFija.getCodigoOperacionCteBlo());
		//configSubastaDICOM.setCodOperacionDebCap(transaccionFija.getCodigoOperacionCteDeb());
		//configSubastaDICOM.setCodOperacionCreCap(transaccionFija.getCodigoOperacionCteCre());

		transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.COMISION_DEB);
		//configSubastaDICOM.setCodOperacionBloCom(transaccionFija.getCodigoOperacionCteBlo());
		//configSubastaDICOM.setCodOperacionDebCom(transaccionFija.getCodigoOperacionCteDeb());

		transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.TOMA_ORDEN_CTA_DOLARES);
		//configSubastaDICOM.setCodOperacionCreConv20(transaccionFija.getCodigoOperacionCteCre());
		

		// configSubastaDICOM.setUsername(usuario);
		configSubastaDICOM.setUsername(usuario);
		configSubastaDICOM.setIp(direccionIpstr);
		configSubastaDICOM.setEjecucionId(String.valueOf(procesoEnEjecucion.getEjecucionId()));

	}
	
	public static void main(String[]args ){
		String a="999902102017000000500000000250000000000125000000000002000000001000000000000500000003";
		String b=a.substring(0,82);
		System.out.println(" Linea " + b);
	}

}


