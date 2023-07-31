package com.bdv.infi.logic.interfaz_operaciones_DICOM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;
import org.bcv.serviceDICOM.OperCamFileTransferPortBindingStub;

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
import com.bdv.infi.data.FormatoOperacionesDICOMInterbancario;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudDICOM;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.ArchivoRecepcion;
import com.bdv.infi.logic.interfaz_ops.BatchOps;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public class RecepcionCruceOperacionesDICOMContingencia extends BatchOps implements Runnable{
	
	public RecepcionCruceOperacionesDICOMContingencia(DataSet unidadesInvProcesar,String usuarioGenerico,DataSource datasource) throws Exception {
		super();		
		_dso = datasource;
		controlArchivoDAO= new ControlArchivoDAO(_dso);
		this.usuario=usuarioGenerico;
		this.unidadesInvProcesar=unidadesInvProcesar;
	}
	
	public RecepcionCruceOperacionesDICOMContingencia(DataSource datasource) throws Exception {
		super();		
		_dso = datasource;
		controlArchivoDAO= new ControlArchivoDAO(_dso);
		
	}	
		
	private com.bdv.infi.data.TransaccionFija transaccionFija = null;
	TransaccionFijaDAO transaccionFijaDAO = null;
	private ConfigSubastaDICOM configSubastaDICOM;
	private DataSet unidadesInvProcesar;
	VehiculoDAO vehiculoDAO = null;
	BlotterDAO blotterDAO = null;
	UnidadInversionDAO uiDAO = null;
	private String vehiculoBDV = null;
	private String blotterId;
	private String empresaID;
	ParametrosDAO parametrosDAO;
	static final Logger logger = Logger.getLogger(RecepcionCruceOperacionesDICOM.class);
	protected HashMap<String, String> parametrosRecepcionDICOM;
	private Proceso procesoEnEjecucion;
	
	ArrayList<String> querysEjecutar=new ArrayList<String>();	
	//protected int idCiclo=0;
	FormatoOperacionesDICOMInterbancario formatoArchivoRecepcion=new FormatoOperacionesDICOMInterbancario();
	protected String transaccionEjecutar;
	boolean existeArchivo=false; 
	
	//private int cantidadTotalOrdenes=0;
	//private int cantidadTotalOrdenesProcesadas=0;
	//private int cantidadLoteOrdenesProcesadas=0;	
	
	private String rutaRecepcion="";
	private String rutaRespaldo="";	
	private String rutaEnvio="";	
	private String usuario="";
	private String cabecera ="";
	public static String DESCUADRE_TOTALES_EXCEPTION="DESCUADRE_TOTALES_EXCEPTION";
	public static String ERROR_CABECERA_EXCEPTION="ERROR_CABECERA_EXCEPTION";
	public String monedaSubasta="";
	private String detalleCuadre="";
	
	public void run() {
		String jornadaDicom="";
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
			} catch (Exception e) {
				logger.debug("Error en formato de fecha actual, se usará new Date()");
			}
			
			//VERIFICAR CICLO
			if (verificarCiclo(TransaccionNegocio.CICLO_BATCH_DICOM) ){
				//OBTENER PARAMETROS DICOM
				obtenerParametros();				
				//VALIDAR JORNADA, FECHA INICIO Y FECHA FIN
				jornadaDicom = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_DICOM);
				jornadafechaInicio = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_INICIO);
				jornadafechaFin = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_FIN);
				jornadaManual = parametrosRecepcionDICOM.get(ParametrosSistema.CRUCE_DICOM_MANUAL);
				//monedaSubasta=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
				rutaRecepcion = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_CRUCE_DICOM_RECEP);
				rutaRespaldo = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_CRUCE_DICOM_RESP);
				rutaEnvio = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_ENVIO);
				
				logger.info("EnvioOperacionesDICOM-> Se validaran parametros de entrada. Jornada: "+jornadaDicom+" jornadafechaInicio: "+jornadafechaInicio+" jornadafechaFin: "+jornadafechaFin);
				
				if(Utilitario.longitudValida(jornadaDicom, 1, EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor())&&
						Utilitario.longitudValida(jornadafechaInicio, 10, 10)&&
								Utilitario.longitudValida(jornadafechaFin, 10, 10)){
					
					//VALIDAR FECHA
					fechaInicioDate=Utilitario.StringToDate(jornadafechaInicio, ConstantesGenerales.FORMATO_FECHA2);
					fechaFinDate=Utilitario.StringToDate(jornadafechaFin, ConstantesGenerales.FORMATO_FECHA2);
					
					if(Utilitario.betweenDates(fechaInicioDate, fechaFinDate, fechaActual)){	
												
						//REGISTRAR PROCESO
						transaccionEjecutar=TransaccionNegocio.PROC_CRUCE_DICOM_INTERBANCARIO;
						
						procesoEnEjecucion=comenzarProceso(1,transaccionEjecutar);
						if (getProceso()!=null) {
							
							try { 
								//BUSCAR ARCHIVO EN WB DICOM BCV					
								try {
									if(jornadaManual.equals("0")){
										rutaRecepcion=rutaRecepcion+getNombreArchivoRecepcion(jornadaDicom);
										logger.info("EnvioOperacionesDICOM-> Antes de Consumir el servicio bajarSolicitudComprobacionBloqueo. Jornada: "+jornadaDicom);
										
										OperCamFileTransferPortBindingStub stub = new OperCamFileTransferPortBindingStub(null,_dso);
										 byte[] _bytes;						
										_bytes=stub.bajarOrdenDesbloqueosCreditosDebitos(jornadaDicom);
										
										logger.info("EnvioOperacionesDICOM-> Despues de Consumir el servicio bajarSolicitudComprobacionBloqueo");
										
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
									recepcionArchivoOpCruzadas(archivoRecibido,jornadaDicom);	
									
									//RESPALDAR ARCHIVO RECIBIDO
									respaldarArchivo(archivoRecibido);
									
								} catch (Exception e1) {
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
						}
						
					}else{
						logger.info("EnvioOperacionesDICOM-> La Jornada no se encuentra activa en la fecha configurada");
					}			
				
				}else
				{
					logger.info("EnvioOperacionesDICOM-> Los parametros JORNADA_DICOM,JORNADA_FECHA_INICIO,JORNADA_FECHA_FIN no estan bien configurados");
				}
			
			}else{
				logger.info("EnvioOperacionesDICOM-> Ya se encuentra registrado un ciclo en ejecución");
			}
		} catch (Exception ex) {
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
		parametrosDAO = new ParametrosDAO(_dso);		
		//TTS-537 Busqueda de parametros de transacciones a moneda extranjera para cancelacion de operaciones vencidas de Convenio 36 NM26659 28-12-2016  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
		
	}
			
	protected void recepcionArchivoOpCruzadas(File archivo,String jornada) throws FileNotFoundException, IOException, Exception {
				
		if (!archivo.exists()) {
			//logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
			//Lee Archivo de Moneda Extranjera y guarda en BD
			
		//procesarArchivo(archivo,jornada);

			respaldarArchivo(archivo);		

		}
	}
	
//	protected void procesarArchivo(File archivo, String jornadaDicom) throws Exception {		  	
//		  	
//		//CREAR ARCHIVO							
//		formatoArchivoRecepcion.setNroJornada(jornadaDicom);			
//		formatoArchivoRecepcion.setIdCiclo(idEjecucion);			
//		formatoArchivoRecepcion.setMonedaSubasta(this.monedaSubasta);			
//		registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCabecera(cabecera));			
//		registrosDeArchivoDefinitivo.addAll(registrosCuerpoDeArchivo);			
//		logger.info("Escribiendo en archivo temporal " + rutaEnvio);
//		FileUtil.put(rutaEnvio, registrosDeArchivoDefinitivo, true);
//				//ENVIAR ARCHIVO A MAINFRAME
//				logger.info("Antes de transferir a Mainframe ");
//				transferirArchivo(rutaEnvio, getDestinoFinal());
//				logger.info("Despues de transferir a Mainframe ");
//				//RESPALDAR ARCHIVO ENVIADO
//				archivoEnviado = getArchivo(rutaEnvio);
//				respaldarArchivo(archivoEnviado);
//			}else{
//				proceso.agregarDescripcionErrorTrunc("No existen operaciones a enviar. registrosCuerpoDeArchivo.size()= "+registrosCuerpoDeArchivo.size(),true);
//			}
//			
//			//VALIDAR TOTALES DE LA CABECERA CONTRA LO REGISTRADO EN BD
//			/*solicitudDICOMTotalesRegistrados=solicitudesDICOMDAO.listarTotalesSolicitudesPreaprobadasDICOM(jornadaDicom);
//				
//			if(!compararTotales(solicitudDICOMTotalesArchivo,solicitudDICOMTotalesRegistrados)){			
//				//ACTUALIZAR OPERACIONES INVALIDAS
//				//solicitudesDICOMDAO.actualizarEstatusRegistroSolicitudes(jornadaDicom, EstatusOperacionesDICOM.ESTATUS_CARGADA.getValor(), EstatusOperacionesDICOM.ESTATUS_ERROR_VALIDACION_TOTALES.getValor());
//				//PROCESAR REGISTROS INVALIDOS
//				throw new Exception(DESCUADRE_TOTALES_EXCEPTION);				 //Los valores de la Cabecera no concuerdan con los totales registrados. Verifique el archivo 
//			}*/	
//	}		


	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}	

	
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
		return "cruce_recepcion_"+jornada+".txt";
	}
	
	protected String getNombreArchivoRecepcionManualRespaldo(String jornada) throws ParseException{		
		return "cruzadas_recepcion_manual_"+jornada+".txt";
	}
	
	protected String getNombreArchivoEnvio(String jornada) throws ParseException{		
		return "preaprobadas_envio_"+jornada+".txt";
	}

	protected String getDestinoFinal(){	
		return parametrosRecepcionDICOM.get(getNombreArchivo());
	}
	
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_CRUCE_DICOM;
	}
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_CRUCE_DICOM_RESP);
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
	
	private void procesarAdjudicacion(SolicitudDICOM registroCruce)throws Exception{
				
		SolicitudDICOM solicitudDICOM;
		
		SolicitudesDICOMDAO solicitudesDICOMDAO=new SolicitudesDICOMDAO(_dso);
		solicitudDICOM=solicitudesDICOMDAO.busquedaSolicitudesDicomPorId(registroCruce.getIdOperacion());				//Se busca en BD la informacion de la solicitud
		
		
		BigDecimal montoComisionRecalculado=null;//Monto por el que se genera el debito de comision en casos de adjudicaciones Parciales
		BigDecimal montoComisionIGTFRecalculado=null;//Monto por el que se genera el debito de comision en casos de adjudicaciones Parciales
		boolean adjudicacionParcial=false;
		
		
		if(solicitudDICOM!=null){																			
			if(solicitudDICOM.getTipoOperacion().equals(ConstantesGenerales.SOLICITUD_OFERTA)){//SI la operaciones es OFERTA se compara el campo MONTO_OP_NACIONAL con el registro de Credito Bs del archivo de cruce				
				if(!(registroCruce.getMontoCreditoBs().equals(new BigDecimal(0))) && !(solicitudDICOM.getMontoOperacionVEF().equals(registroCruce.getMontoCreditoBs()))){//Monto Adjudicado menor al solicitado
					adjudicacionParcial=true;
				}
				
				solicitudDICOM.setMontoOperacionUSD(registroCruce.getMontoDebitoDiv());
				solicitudDICOM.setMontoOperacionVEF(registroCruce.getMontoCreditoBs());		
				solicitudDICOM.setTipoSolicitud(EstructuraArchivoOperacionesDICOM.TIPO_PROCESO_DEMANDA.getValor());
			}else if (solicitudDICOM.getTipoOperacion().equals(ConstantesGenerales.SOLICITUD_DEMANDA)){//SI la operaciones es DEMANDA se compara el campo MONTO_OP_EXTRANJERA con el registro de Credito Bs del archivo de cruce								
				if(!(registroCruce.getMontoCreditoDiv().equals(new BigDecimal(0))) && !(solicitudDICOM.getMontoOperacionUSD().equals(registroCruce.getMontoCreditoDiv()))){//Monto Adjudicado menor al solicitado
					adjudicacionParcial=true;					
				} else {//ADJUDICACION TOTAL
					
				}			
				solicitudDICOM.setTipoSolicitud(EstructuraArchivoOperacionesDICOM.TIPO_PROCESO_OFERTA.getValor());
				solicitudDICOM.setMontoOperacionVEF(registroCruce.getMontoDebitoBs());
				solicitudDICOM.setMontoOperacionUSD(registroCruce.getMontoCreditoDiv());
			}	
			
			
			if(adjudicacionParcial){//Si hay Adjudicacion Parcial se deben realcular los montos de comision en base al monto adjudicado				
				//Comision de operacion
				montoComisionRecalculado=new BigDecimal(registroCruce.getMontoCreditoBs().multiply(solicitudDICOM.getPorcentajeComision()).toString()).divide(new BigDecimal("100"));
				montoComisionRecalculado=montoComisionRecalculado.setScale(2,BigDecimal.ROUND_HALF_EVEN);
				solicitudDICOM.setMontoComision(montoComisionRecalculado);
				
				//Comision IGTF
				montoComisionIGTFRecalculado=new BigDecimal(registroCruce.getMontoCreditoBs().multiply(solicitudDICOM.getPtcComisionIGTF()).toString()).divide(new BigDecimal("100"));
				montoComisionIGTFRecalculado=montoComisionIGTFRecalculado.setScale(2,BigDecimal.ROUND_HALF_EVEN);
				solicitudDICOM.setMontoComisionIGTF(montoComisionIGTFRecalculado.toString());
			}
			
			solicitudesDICOMDAO.procesarSolicitudDICOM(configSubastaDICOM, solicitudDICOM);			
		}
	}
	

	public void configurarCodigosOperacion(DataSet unidadesInvProcesar,String nroJornada) throws Exception{
		
		vehiculoDAO = new VehiculoDAO(_dso);
		blotterDAO = new BlotterDAO(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		uiDAO=new UnidadInversionDAO(_dso);
		configSubastaDICOM = new ConfigSubastaDICOM();				
		configSubastaDICOM.setNroJornada(nroJornada);
		long idUnidadInv=0;
		int tipoSolicitud=0;//
		InetAddress direccion = InetAddress.getLocalHost();
		String direccionIpstr = direccion.getHostAddress();
		configSubastaDICOM.setUsername(usuario);
		configSubastaDICOM.setIp(direccionIpstr);
		vehiculoDAO.obtenerVehiculoBDV();
		if (vehiculoDAO.getDataSet() != null && vehiculoDAO.getDataSet().count() > 0) {
			vehiculoDAO.getDataSet().next();
			vehiculoBDV = vehiculoDAO.getDataSet().getValue("vehicu_id");
		}
		configSubastaDICOM.setVehiculoBDVId(vehiculoBDV);
		if(unidadesInvProcesar.count()>0){
			unidadesInvProcesar.first();
			while(unidadesInvProcesar.next()){
				idUnidadInv=Long.parseLong(unidadesInvProcesar.getValue("UNDINV_ID"));
				tipoSolicitud=Integer.parseInt(unidadesInvProcesar.getValue("TIPO_SOLICITUD"));
				blotterDAO.listarBlotterPorUI(String.valueOf(idUnidadInv));
				if (blotterDAO.getDataSet() != null && blotterDAO.getDataSet().count() > 0) {
					blotterDAO.getDataSet().next();
					blotterId = blotterDAO.getDataSet().getValue("BLOTER_ID");
				}
				configSubastaDICOM.setBlotterId(blotterId);
				uiDAO.obtenerDatosUIporId("EMPRES_ID, TIPO_SOLICITUD", String.valueOf(idUnidadInv));
				if (uiDAO.getDataSet() != null && uiDAO.getDataSet().count() > 0) {
					uiDAO.getDataSet().next();
					empresaID = uiDAO.getDataSet().getValue("EMPRES_ID");					
				}
			}			
			
		}
		configSubastaDICOM.setEmpresId(empresaID);		

		if(tipoSolicitud==EstructuraArchivoOperacionesDICOM.TIPO_PROCESO_DEMANDA.getValor()){
			transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.CAPITAL_SIN_IDB);
			configSubastaDICOM.setCodOperacionBloCapDemanda(transaccionFija.getCodigoOperacionCteBlo());
			configSubastaDICOM.setCodOperacionDebCapDemanda(transaccionFija.getCodigoOperacionCteDeb());
			configSubastaDICOM.setCodOperacionCreCapDemanda(transaccionFija.getCodigoOperacionCteCre());

			transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.COMISION_DEB);
			configSubastaDICOM.setCodOperacionBloComDemanda(transaccionFija.getCodigoOperacionCteBlo());
			configSubastaDICOM.setCodOperacionDebComDemanda(transaccionFija.getCodigoOperacionCteDeb());

			transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.TOMA_ORDEN_CTA_DOLARES);
			configSubastaDICOM.setCodOperacionCreConv20Demanda(transaccionFija.getCodigoOperacionCteCre());	
		}else if(tipoSolicitud==EstructuraArchivoOperacionesDICOM.TIPO_PROCESO_OFERTA.getValor()){
			transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.CAPITAL_SIN_IDB);
			configSubastaDICOM.setCodOperacionBloCapOferta(transaccionFija.getCodigoOperacionCteBlo());
			configSubastaDICOM.setCodOperacionDebCapOferta(transaccionFija.getCodigoOperacionCteDeb());
			configSubastaDICOM.setCodOperacionCreCapOferta(transaccionFija.getCodigoOperacionCteCre());

			transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.COMISION_DEB);
			configSubastaDICOM.setCodOperacionBloComOferta(transaccionFija.getCodigoOperacionCteBlo());
			configSubastaDICOM.setCodOperacionDebComOferta(transaccionFija.getCodigoOperacionCteDeb());

			transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.TOMA_ORDEN_CTA_DOLARES);
			configSubastaDICOM.setCodOperacionCreConv20Oferta(transaccionFija.getCodigoOperacionCteCre());
		}
		
		// configSubastaDICOM.setUsername(usuario);
		
		configSubastaDICOM.setEjecucionId(String.valueOf(procesoEnEjecucion.getEjecucionId()));
	}
}


