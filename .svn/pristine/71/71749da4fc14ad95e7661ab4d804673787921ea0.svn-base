package com.bdv.infi.logic.interfaz_operaciones_DICOM;
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
import org.bcv.serviceDicomMulti.OperCamFileTransferPortBindingStub;
//import ve.org.bcv.ws.mtom.server.OperCamFileTransferPortBindingStub;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesDICOMDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.ConfigSubastaDICOM;
import com.bdv.infi.data.FormatoOperacionesDICOM;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudDICOM;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstatusOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.ArchivoRecepcion;
import com.bdv.infi.logic.interfaz_ops.BatchOps;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public class EnvioOperacionesPreaprobadasDICOM extends BatchOps implements Runnable{
	
	public EnvioOperacionesPreaprobadasDICOM(DataSource datasource,String usuarioGenerico) throws Exception {
		super();		
		_dso = datasource;
		controlArchivoDAO= new ControlArchivoDAO(_dso);
		this.usuario=usuarioGenerico;
	}	
	
	static final Logger logger = Logger.getLogger(EnvioOperacionesPreaprobadasDICOM.class);
	protected HashMap<String, String> parametrosRecepcionDICOM;
	ArrayList<String> querysEjecutar=new ArrayList<String>();
	FormatoOperacionesDICOM formatoArchivoRecepcion=new FormatoOperacionesDICOM();
	protected String transaccionEjecutar;
	boolean existeArchivo=false;
	private String TRAN="";
	private String tipomoneda="";
	private String rutaRecepcion="";
	private String rutaRespaldo="";	
	private String rutaEnvio="";	
	private String usuario="";
	private String cabecera ="";
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
	String reenvioArchPreaprobadasDicom="";
	String MonedaDicom_Siglas="";
	int monedaInt;
	int constatemoneda;
	
	public void run() {
		String jornadaDicom="";
		
		
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
				System.out.println("Paso 6");
				fechaActual=Utilitario.fechaDateFormateada(new Date(), ConstantesGenerales.FORMATO_FECHA2);
			} catch (Exception e) {
				logger.debug("Error en formato de fecha actual, se usará new Date()");
			}
			
			obtenerParametros();
			reenvioArchPreaprobadasDicom = parametrosRecepcionDICOM.get(ParametrosSistema.REENVIO_OP_PREAPROBADAS_DICOM);
			monedaSubasta=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
			MonedaDicom_Siglas=parametrosRecepcionDICOM.get(monedaSubasta);			
			monedaInt= Integer.parseInt(monedaSubasta.trim());
			constatemoneda= Integer.parseInt(ConstantesGenerales.CODIGO_MONEDA_ISO_USD_DICOM);
			TRAN=getTransaccionNegocio();
			
			
			//VERIFICAR CICLO			
			if (reenvioArchPreaprobadasDicom.equals("1") || verificarCiclo(TRAN) ){
				
				//OBTENER PARAMETROS DICOM
				//VALIDAR JORNADA, FECHA INICIO Y FECHA FIN
				jornadaDicom = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_DICOM);
				jornadafechaInicio = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_INICIO);
				jornadafechaFin = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_FIN);
				jornadaManual = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_ENVIO_MANUAL);
				
				rutaRecepcion = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_RECEP);
				rutaRespaldo = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_RESP);
				rutaEnvio = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_ENVIO);
				
				logger.info("EnvioOperacionesDICOM-> Se validaran parametros de entrada. Jornada: "+jornadaDicom+" jornadafechaInicio: "+jornadafechaInicio+" jornadafechaFin: "+jornadafechaFin);
				
				/*if(Utilitario.longitudValida(jornadaDicom, 1, EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor())&&
						Utilitario.longitudValida(jornadafechaInicio, 10, 10)&&
								Utilitario.longitudValida(jornadafechaFin, 10, 10)){*/
					
				//VALIDAR FECHA
				fechaInicioDate=Utilitario.StringToDate(jornadafechaInicio, ConstantesGenerales.FORMATO_FECHA2);
				fechaFinDate=Utilitario.StringToDate(jornadafechaFin, ConstantesGenerales.FORMATO_FECHA2);
					
				//if(Utilitario.betweenDates(fechaInicioDate, fechaFinDate, fechaActual)){	
						
				//REGISTRAR PROCESO
				transaccionEjecutar=getTransaccionEjecutar();
				procesoEnEjecucion=comenzarProceso(1,transaccionEjecutar);
				
				if (getProceso()!=null) {
					//(getProceso()==null) {
					try { 								
						//BUSCAR ARCHIVO EN WB DICOM BCV					
						try {
							if(jornadaManual.equals("0")){
										
								rutaRecepcion=rutaRecepcion+getNombreArchivoRecepcion(jornadaDicom);
								logger.info("EnvioOperacionesDICOM-> Antes de Consumir el servicio bajarSolicitudesRealizadas. Jornada: "+jornadaDicom);
								logger.info("EnvioOperacionesDICOM-> Antes de Instanciar bajarSolicitudesRealizadas. Jornada: "+jornadaDicom+ "  Moneda=" + monedaSubasta);
										
								OperCamFileTransferPortBindingStub stub = new OperCamFileTransferPortBindingStub(null,_dso);
								byte[] _bytes;
								
								logger.info("EnvioOperacionesDICOM-> Antes de Consumir el servicio bajarSolicitudesRealizadas. Jornada: "+jornadaDicom+ "  Moneda=" + monedaSubasta);										 
								_bytes=stub.bajarSolicitudesRealizadas(jornadaDicom, monedaSubasta);		
								logger.info("EnvioOperacionesDICOM-> Despues de Consumir el servicio bajarSolicitudesRealizadas");
					
								//SE ESCRIBE EL ARCHIVO PROVISIONALMENTE EN EL SERVIDOR 										
								Utilitario.byteToFile(_bytes,rutaRecepcion);	
								//logger.error("EnvioOperacionesDICOM-> "+Utilitario.openFileToString(_bytes));
							}else{
								rutaRecepcion=rutaRecepcion+getNombreArchivoRecepcionManualRespaldo(jornadaDicom);
								logger.info("EnvioOperacionesDICOM-> Prueba de Consumo Manual de archivo de operaciones Preaprobadas");
							}									
									
				//LEER ARCHIVO
			    archivoRecibido=getArchivo(rutaRecepcion);
				//PROCESAR ARCHIVO
				recepcionArchivoOpPreaprobadas(archivoRecibido,jornadaDicom);	
				//RESPALDAR ARCHIVO RECIBIDO
				respaldarArchivo(archivoRecibido);
									
							}catch(Exception e1){
								if(e1.getMessage().equals(DESCUADRE_TOTALES_EXCEPTION)||e1.getMessage().startsWith(ERROR_CABECERA_EXCEPTION)){
									proceso.agregarAlInicioDescripcionErrorTrunc(e1.getMessage(),true);
								}else{
									proceso.agregarDescripcionErrorTrunc("EnvioOperacionesDICOM->Error: "+e1.getMessage(),true);
								}
									
						     logger.error("EnvioOperacionesDICOM-> "+e1.getMessage());
						     e1.printStackTrace();
							}
	
						} catch (Exception e) {
							proceso.agregarDescripcionErrorTrunc("EnvioOperacionesDICOM->Error: "+e.getMessage(),true);
							logger.error("EnvioOperacionesDICOM-> "+e.getMessage());
							e.printStackTrace();
						}
						}else{
							logger.info("EnvioOperacionesDICOM-> Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
						}//////////////
						
					/*}else{
						logger.info("EnvioOperacionesDICOM-> La Jornada no se encuentra activa en la fecha configurada");
					}	*/		
				
				/*}else
				{
					logger.info("EnvioOperacionesDICOM-> Los parametros JORNADA_DICOM,JORNADA_FECHA_INICIO,JORNADA_FECHA_FIN no estan bien configurados");
				}*/
			
			}else{
				logger.info("EnvioOperacionesDICOM-> Ya se encuentra registrado un ciclo en ejecución");
				
			}
		} catch (Exception ex) {
			
			ex.printStackTrace();
			logger.error("Error en el proceso de recepción archivo batch para EnvioOperacionesPreaprobadasDICOM. ", ex);		
			logger.info("Exception ex ---> " + ex.getMessage() );
			if(proceso != null) {
				proceso.agregarDescripcionErrorTrunc(ex.getMessage(),true);
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de recepción de archivo batch para EnvioOperacionesPreaprobadasDICOM... ");
		}
	}
	
	protected File getArchivo(String ruta, String archivo) {
		String carpeta = parametrosRecepcionDICOM.get(ruta);		
		return new File(carpeta);

	}
	
	protected File getArchivo(String ruta) {
		return new File(ruta);
	}
	
	/**
	 * Metodo de busqueda de parametros asociados a interfaz con Moneda Extranjera
	 * */
	@Override
	protected void obtenerParametros() throws Exception {
		super.obtenerParametros();
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		
		//TTS-537 Busqueda de parametros de transacciones a moneda extranjera para cancelacion de operaciones vencidas de Convenio 36 NM26659 28-12-2016  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
		
	}
			
	protected void recepcionArchivoOpPreaprobadas(File archivo,String jornada) throws FileNotFoundException, IOException, Exception {
				
		if (!archivo.exists()) {
			System.out.println("!archivo.exists()");
			//logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
			System.out.println("archivo.exists()");
			//Lee Archivo de Moneda Extranjera y guarda en BD
			
			procesarArchivo(archivo,jornada);
			System.out.println("despues de procesarArchivo");
			respaldarArchivo(archivo);
			System.out.println("despues de respaldarArchivo");

		}
	}
	
	protected void procesarArchivo(File archivo, String jornadaDicom) throws Exception {
		//boolean uiOfertaPorCrear =true;
		//boolean uiDemandaPorCrear=true;
		SolicitudesDICOMDAO solicitudesDICOMDAO;
		ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);
		String linea = archivoRecepcion.leerLinea();
		
		solicitudesDICOMDAO = new SolicitudesDICOMDAO(_dso);
		SolicitudDICOM solicitudDICOM;
		SolicitudDICOM solicitudDICOMTotalesArchivo=null;
		SolicitudDICOM solicitudDICOMTotalesRegistrados=null;
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
		ArrayList<String> registrosCuerpoDeArchivo= new ArrayList<String>();
		boolean ejecutarCommit=false;
		File archivoEnviado=null;
		String idEjecucion = "";
		
		
		rutaEnvio=rutaEnvio+getNombreArchivoEnvio(jornadaDicom);
		System.out.println("rutaEnvio--->"+rutaEnvio);
		if(reenvioArchPreaprobadasDicom.equals("0")){//Se inicia el ciclo en caso de NO  estar procesando un reenvio de archivo al mainframe			
			//ABRIR CICLO
			System.out.println("paso reenvioArchPreaprobadasDicom 0");
			idEjecucion = controlArchivoDAO.obtenerNumeroDeSecuencia(); //CORRECCION DE MANEJO ERRADO DEL ID DE CICLO NM25287 12/06/2017
			
			Archivo controlCiclo = new Archivo();			
			//controlCiclo.setIdEjecucion(getProceso().getEjecucionId()); //CORRECCION DE MANEJO ERRADO DEL ID DE CICLO NM25287 12/06/2017
			controlCiclo.setIdEjecucion(Integer.parseInt(idEjecucion));
			controlCiclo.setNombreArchivo(getDestinoFinal());		
			controlCiclo.setFechaGeneracion(new Date());
			controlCiclo.setInRecepcion(0);		
			controlCiclo.setStatus(TRAN/*TransaccionNegocio.CICLO_BATCH_DICOM+monedaSubasta*/);
			controlCiclo.setUsuario(usuario);
			
			db.execBatch(this._dso,controlArchivoDAO.insertarArchivoRecepcion(controlCiclo));
		}// Cambio para Buscar el Ciclo cuando es un reenvio del archivo 1 Alexander Rincon NM11383 14/03/2018 
		else if(reenvioArchPreaprobadasDicom.equals("1")) {
			///////////////////////////////////////////////////REVISAR
			System.out.println("paso reenvioArchPreaprobadasDicom 1");
			   // idEjecucion = controlArchivoDAO.obtenerCicloProceso();//revisar
				idEjecucion = controlArchivoDAO.obtenerCicloProcesomultimoneda(TRAN);
			    System.out.println("idEjecucion--->"+idEjecucion);
			   // controlCiclo.setIdEjecucion(Integer.parseInt(idEjecucion));
		}
		//PROCESAR CABECERA
		System.out.println("paso yaju");
		if (linea != null) {
			System.out.println("linea = null");
			if (linea.length() > 0) {	
				System.out.println("linea > 0");
				//SE CARGAN LOS TOTALES QUE SERAN VALIDADOS UNA VEZ SE TERMINEN DE REGISTRAR EN BD
				System.out.println("paso cargarCabeceraPreaprobadas");
				formatoArchivoRecepcion.cargarCabeceraPreaprobadas(linea);
				System.out.println("paso cargarCabeceraPreaprobadas213");
				cabecera=linea;
				System.out.println("paso cargarCabeceraPreaprobadas2");
				solicitudDICOMTotalesArchivo=formatoArchivoRecepcion.validarCabeceraOpPreaprobadas(jornadaDicom);
				System.out.println("paso cargarCabeceraPreaprobadas3");
				linea = archivoRecepcion.leerLinea();
				System.out.println("paso cargarCabeceraPreaprobadas4");
			}
		}

		formatoArchivoRecepcion.setParamtrosSistema(parametrosRecepcionDICOM);		
		//PROCESAR DETALLE DEL ARCHIVO
		while (linea != null) {
			System.out.println("paso PROCESAR DETALLE DEL ARCHIVO");
				if (linea.length() > 0) {					
					try {
						System.out.println("paso PROCESAR DETALLE DEL ARCHIVO1");
						//CARGAR CUERPO OPERACIONES PREAPROBADAS
						formatoArchivoRecepcion.cargarCuerpoOpPreaprobadas(linea);
						System.out.println("paso PROCESAR DETALLE DEL ARCHIVO2");
						//VALIDAR CUERPO OPERACIONES PREAPROBADAS
						if(((formatoArchivoRecepcion.getIdOperacion().trim()).length()>0)){
							System.out.println("paso PROCESAR DETALLE DEL ARCHIVO3");
							solicitudDICOM=formatoArchivoRecepcion.validarCuerpoOpPreaprobadas();
							System.out.println("paso PROCESAR DETALLE DEL ARCHIVO4");
							if(solicitudDICOM!=null){
								System.out.println("paso PROCESAR DETALLE DEL ARCHIVO5");
								ejecutarCommit=true;
								System.out.println("paso PROCESAR DETALLE DEL ARCHIVO6");
								solicitudDICOM.setIdJornada(jornadaDicom);
								solicitudDICOM.setCodMoneda(MonedaDicom_Siglas);
								if(reenvioArchPreaprobadasDicom.equals("0")){//Si no hay reenvio de archivo se guarda el registro a base de datos  
									solicitudesDICOMDAO.registrarSolicitudDICOM(solicitudDICOM,ejecutarCommit);
								}
																
								registrosCuerpoDeArchivo.add(formatoArchivoRecepcion.crearCuerpoOpPreaprobadas(linea));
							}else{
								proceso.agregarDescripcionErrorTrunc("EnvioOperacionesPreaprobadasDICOM.procesarArchivo: La linea " + linea + " no se puede registrar",true);
								logger.error("EnvioOperacionesPreaprobadasDICOM.procesarArchivo: La linea " + linea + " no se puede registrar");
							}
						}else{
							proceso.agregarDescripcionErrorTrunc("EnvioOperacionesPreaprobadasDICOM.procesarArchivo: La linea " + linea + " no se puede registrar - Id invalido",true);
							logger.error("EnvioOperacionesPreaprobadasDICOM.procesarArchivo: La linea " + linea + " no se puede registrar - Id invalido");
						}
					} catch (Exception e) {
						e.printStackTrace();
						proceso.agregarDescripcionErrorTrunc("La linea " + linea + " no se puede registrar - Error: "+e.getMessage(),true);
						logger.error("EnvioOperacionesPreaprobadasDICOM.procesarArchivo: La linea " + linea + " no se puede registrar - Error: "+e.getMessage());
					}						
				}
				linea = archivoRecepcion.leerLinea();						
			}
		
		  	logger.debug("EnvioOperacionesPreaprobadasDICOM.procesarArchivo: registrosCuerpoDeArchivo.size()= "+registrosCuerpoDeArchivo.size());							
			
		  	if (registrosCuerpoDeArchivo.size()>0) {
				//CREAR ARCHIVO			
				formatoArchivoRecepcion.setNroJornada(jornadaDicom);
				formatoArchivoRecepcion.setIdCiclo(idEjecucion);
				formatoArchivoRecepcion.setMonedaSubasta(this.monedaSubasta);				
			registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCabecera(cabecera,parametrosRecepcionDICOM));
			// Comentado por Alexander Rincon 31/01/2018
			//registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCabeceraDicomInterbancario(jornadaDicom));
				registrosDeArchivoDefinitivo.addAll(registrosCuerpoDeArchivo);
				logger.info("Escribiendo en archivo temporal " + rutaEnvio);
				FileUtil.put(rutaEnvio, registrosDeArchivoDefinitivo, true);
				//ENVIAR ARCHIVO A MAINFRAME
				logger.info("Antes de transferir a Mainframe ");
				transferirArchivo(rutaEnvio, getDestinoFinal()/*.replaceAll("COM", parametrosRecepcionDICOM.get(monedaSubasta))*/);
				logger.info("Despues de transferir a Mainframe ");
				//RESPALDAR ARCHIVO ENVIADO
				archivoEnviado = getArchivo(rutaEnvio);
				respaldarArchivo(archivoEnviado);
			}else{
				proceso.agregarDescripcionErrorTrunc("No existen operaciones a enviar. registrosCuerpoDeArchivo.size()= "+registrosCuerpoDeArchivo.size(),true);
			}
			
			//VALIDAR TOTALES DE LA CABECERA CONTRA LO REGISTRADO EN BD
		  	solicitudDICOMTotalesRegistrados=solicitudesDICOMDAO.listarTotalesSolicitudesPreaprobadasDICOM(jornadaDicom,MonedaDicom_Siglas);
			if(!compararTotales(solicitudDICOMTotalesArchivo,solicitudDICOMTotalesRegistrados)){//TODO CUADRE DE TOTALES COMENTADO HASTA SABER EXTRUCTURA DE ARCHIVO			
				//ACTUALIZAR OPERACIONES INVALIDAS
				solicitudesDICOMDAO.actualizarEstatusRegistroSolicitudes(jornadaDicom,MonedaDicom_Siglas, EstatusOperacionesDICOM.ESTATUS_CARGADA.getValor(), EstatusOperacionesDICOM.ESTATUS_ERROR_VALIDACION_TOTALES.getValor());
				//PROCESAR REGISTROS INVALIDOS
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
		proceso = new Proceso();
		proceso.setTransaId(procesoEjecucion);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		logger.info("TIPO PROCESO INSERTADO: " + proceso.getTransaId() + " -------------------> ");	
		db.exec(_dso, procesoDAO.insertar(proceso));

		logger.info("Comenzó proceso: " + tipoProceso);
		setProceso(proceso);
		return proceso;
	}
	
	
	protected String getFechaHora() throws ParseException{		
		return Utilitario.DateToString(new Date(), "ddMMyyyyhhmmss");
	}
	
	protected String getNombreArchivoRecepcion(String jornada) throws ParseException{	
		String nombreo;
		//String nombref="preaprobadas_recepcion_"+jornada+monedaSubasta+".txt";
		if(monedaInt==constatemoneda){
			//nombreo.replaceAll(ConstantesGenerales.CODIGO_DOLAR,nombref);
			nombreo="preaprobadas_recepcion_"+jornada+".txt";
		}else{
			nombreo="preaprobadas_recepcion_"+jornada+MonedaDicom_Siglas+".txt";
		}
		System.out.println("nombreo--->"+nombreo);
		return nombreo;
		//return "preaprobadas_recepcion_"+jornada+".txt";
	}
	
	protected String getNombreArchivoRecepcionManualRespaldo(String jornada) throws ParseException{		
		//return "preaprobadas_recepcion_manual_"+jornada+".txt";
		
		String nombreo;
		//String nombref="preaprobadas_recepcion_"+jornada+monedaSubasta+".txt";
		if(monedaInt==constatemoneda){
			//nombreo.replaceAll(ConstantesGenerales.CODIGO_DOLAR,nombref);
			nombreo="preaprobadas_recepcion_manual_"+jornada+".txt";
		}else{
			nombreo="preaprobadas_recepcion_manual_"+jornada+MonedaDicom_Siglas+".txt";
		}
		System.out.println("nombreo--->"+nombreo);
		return nombreo;
		
	}
	
	protected String getTransaccionNegocio(){
		String transsaciones="";
		if(monedaInt==constatemoneda){
			transsaciones = TransaccionNegocio.CICLO_BATCH_DICOM;
		}else{
			transsaciones =TransaccionNegocio.CICLO_BATCH_DICOM+MonedaDicom_Siglas;
		}
		
		return transsaciones;	
	}
	
	
	protected String getTransaccionEjecutar(){
		String cicloejecutar="";
		if(monedaInt==constatemoneda){
			cicloejecutar = TransaccionNegocio.PROC_ENVIO_DICOM;
		}else{
			cicloejecutar =TransaccionNegocio.PROC_ENVIO_DICOM+MonedaDicom_Siglas;
		}
		return cicloejecutar;	
	}
	
	protected String getNombreArchivoEnvio(String jornada) throws ParseException{	
		//return "preaprobadas_envio_"+jornada+".txt";
		
		String nombreo="";
		//String nombref="preaprobadas_recepcion_"+jornada+monedaSubasta+".txt";
		if(monedaInt==constatemoneda){
			//nombreo.replaceAll(ConstantesGenerales.CODIGO_DOLAR,nombref);
			nombreo="preaprobadas_recepcion_manual_"+jornada+".txt";
		}else{
			nombreo="preaprobadas_envio_"+jornada+MonedaDicom_Siglas+".txt";	
		}
		System.out.println("nombreoenvio--->"+nombreo);
		return nombreo;
	}
	
	
	protected String getTranssaccion(){
		
		return cabecera;
		
	}

	protected String getDestinoFinal(){
		//replaceAll(String regex, String replacement)
		//replaceAll(getNombreArchivo(), )
		//String MonedaV="COM";
		String destino=parametrosRecepcionDICOM.get(getNombreArchivo());
		String destino1=null;
		//System.out.println("jesusprueba--->"+tipomoneda+" y "+"ConstantesGenerales.CODIGO_MONEDA_ISO_USD_DICOM---->"+ConstantesGenerales.CODIGO_MONEDA_ISO_USD_DICOM);
		if(monedaInt==constatemoneda){
			destino1=destino;
			System.out.println("paso el else de destino");
		
		 //destino=parametrosRecepcionDICOM.get(getNombreArchivo()+ConstantesGenerales.CODIGO_DOLAR);
		}else{
		destino1=destino.replaceAll("COM",MonedaDicom_Siglas);
		}
		System.out.println("ConstantesGenerales.CODIGO_DOLAR--->"+ConstantesGenerales.CODIGO_DOLAR+" y "+"MonedaDicom_Siglas--->"+MonedaDicom_Siglas);
		
		System.out.println("destino--->"+destino1);
		return destino1;
	}
	
	protected String getNombreArchivo(){//REVISAR CON FRANCISCO
		return ParametrosSistema.NOMBRE_ARCH_DICOM_ENVIO;
	}
	
	
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_RESP);
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
			if (proceso != null) {
				proceso.setFechaFin(new Date());
				if (proceso.getDescripcionError() == null) {
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
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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


