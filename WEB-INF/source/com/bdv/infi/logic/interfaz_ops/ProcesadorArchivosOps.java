package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.IntentoOperacionDAO;
import com.bdv.infi.dao.MensajeOpicsDAO;       
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TitulosBloqueoDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.Custodia;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.data.FormatoArchivosOps;
import com.bdv.infi.data.OperacionIntento;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.TituloBloqueo;
import com.bdv.infi.ftp.FTPUtil;
import com.bdv.infi.logic.interfaces.Beneficiarios;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ControlProcesosOps;
import com.bdv.infi.logic.interfaces.EstructuraArchivosOps;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoBloqueos;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public abstract class ProcesadorArchivosOps {
	
	static final Logger logger = Logger.getLogger(ProcesadorArchivosOps.class);	
	HashMap<String, String> parametrosOPS;
	HashMap<String, String> parametrosFTP;
	protected ProcesosDAO procesoDAO;
	protected OperacionDAO operacionDAO;
	protected OrdenDAO ordenDAO;
	protected IntentoOperacionDAO intentoOperacionDAO;
	protected TitulosBloqueoDAO titulosBloqueoDAO;
	protected CustodiaDAO custodiaDAO;
	protected MensajeOpicsDAO mensajeOpicsDao;
	protected Proceso proceso;
	protected DataSource _dso;
	protected int usuarioId;
	protected String userName;
	protected String grupoParamOps;
	protected String grupoParamFtp;	
	//protected String rutaServidor;
	protected String consultaRegistros;
	protected DataSet _registros;
	protected ControlProcesosOps controlProceso;
	
	public final SimpleDateFormat sdfArchivoRespaldo = new SimpleDateFormat("ddMMyyyyHHmmss");
	
	FormatoArchivosOps formatoArchivosOps;
	long batchEjecutados = 0;
	final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	final SimpleDateFormat sdfhora = new SimpleDateFormat("HHmmss");
	final SimpleDateFormat sdfAplicacion = new SimpleDateFormat("yyyyMMddHHmmss");
	protected String archivoTemporal = "";
	protected ControlArchivoDAO controlArchivoDAO;
	int ordenesTotales = 0;
	int ordenesExitosas = 0;
	int operacionesTotales = 0;
	int operacionesExitosas = 0;
	int operacionesRetencionNOK = 0;
	
	public final String COD_RETORNO_OK = "00";
	public final String COD_RETORNO_ERROR = "99";

	public final String COD_RETENCION_OK = "00";
	public final String COD_T = "T";
	public final String COD_S = "S";
	public final String COD_D = "D";
	public final String COD_C = "C";
	public final String COD_Q = "Q";//Indicador de Operacion de Retencion
	final List<String> batch = new ArrayList<String>();
	
	
	public ProcesadorArchivosOps(DataSource _dso,String grupoParamOps,String grupoParamFtp,int usuarioId,String userName,String query,ControlProcesosOps controlProceso){		
		this._dso=_dso;		
		this.grupoParamOps=grupoParamOps;
		this.grupoParamFtp=grupoParamFtp;			
		this.usuarioId=usuarioId;
		this.userName=userName;
		this.consultaRegistros=query;
		this.controlProceso=controlProceso;		
		
	}
	
	public ProcesadorArchivosOps(DataSource _dso,String grupoParamOps,String grupoParamFtp,ControlProcesosOps controlProceso){		
		this._dso=_dso;		
		this.grupoParamOps=grupoParamOps;
		this.grupoParamFtp=grupoParamFtp;				
		this.controlProceso=controlProceso;		
		
	}
	
	protected abstract void procesar();

	public boolean verificarCiclo(String... ciclos){
	boolean flag=false;	
	return flag;
	}
	
	public boolean verificarProceso(String... proceso){
		boolean flag=false;		
		return flag;	
	}
		
	protected HashMap<String, String> obtenerParametros(String grupoParametro) throws Exception {
		HashMap<String, String> parametro;
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametro = parametrosDAO.buscarParametros(grupoParametro);
		return parametro;
	}
	
	protected void obtenerParametrosFTP(String grupoParametro) throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosFTP = parametrosDAO.buscarParametros(grupoParametro);
	}
	
	
	
	protected void envioArchivo() throws Exception{		
		logger.info("*** Ejecucion envioArchivo de la OPS BatchOps ****");
		System.out.println("*** Ejecucion envioArchivo de la OPS BatchOps ****");
		int consecutivo = 0;
		BigDecimal totalDebitos = new BigDecimal(0);
		BigDecimal totalCreditos = new BigDecimal(0);
		
		long acumuladorOrdenId=0; 
		long idOrdenEnProceso=0;
		String transFinanciera="";//Indica si la operaciones es Capital (1) o Comision (11)
		
		boolean generarOpDesbloqueo=true;
		
		ArrayList<String> registrosDeArchivo = new ArrayList<String>();
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
		String idEjecucion = "";
		Transaccion transaccion = new Transaccion(this._dso);
		Statement statement = null;
		ResultSet operaciones = null;
		boolean cabecaraArmada = false;
		int totalDeRegistros = 0;
		Archivo archivo = new Archivo();
		try {
			obtenerArchivoTemporal(controlProceso);			
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			System.out.println("consultaRegistros ---> " + consultaRegistros);
			operaciones = statement.executeQuery(this.consultaRegistros);

			// if (totalDeRegistros>0){
			formatoArchivosOps= new FormatoArchivosOps();
			while (operaciones.next()) {
				
				totalDeRegistros++;
				if (!cabecaraArmada) {
					idEjecucion = controlArchivoDAO.obtenerNumeroDeSecuencia();
					archivo.setIdEjecucion(Integer.parseInt(idEjecucion));
					archivo.setNombreArchivo(parametrosOPS.get(controlProceso.getNombreArchivo()));
					archivo.setUnidadInv(operaciones.getInt("undinv_id"));
					archivo.setUsuario(this.userName);
					archivo.setFechaGeneracion(new Date());
					archivo.setStatus(controlProceso.getCiclo());
					archivo.setVehiculoId(operaciones.getString("ordene_veh_tom"));

					cabecaraArmada = true;
				}
				
				idOrdenEnProceso=Long.parseLong(operaciones.getString("orden_id_proceso"));
				if(acumuladorOrdenId==0){
					acumuladorOrdenId=idOrdenEnProceso;
				} else if(acumuladorOrdenId!=idOrdenEnProceso) {//Se reinicia el indicador de generacion de desbloqueo al iterar la siguiente orden
					acumuladorOrdenId=idOrdenEnProceso;
					generarOpDesbloqueo=true;
				}
				//System.out.println("Monto Operacion --> " + operaciones.getString("monto_operacion"));
				//System.out.println("transacciones financiera --> " + operaciones.getString("TRNFIN_ID"));
				formatoArchivosOps.reiniciarDetaller();
				Detalle detalle = new Detalle();
				//reiniciarValoresCamposDetalle();
				//this.camposDetUalle.put(this.CONSECUTIVO, String.valueOf(++consecutivo));
					formatoArchivosOps.setConsecutivo(String.valueOf(++consecutivo));
				if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.CREDITO)) {					
					//this.camposDetalle.put(this.TIPO_OPERACION, "C");
					formatoArchivosOps.setTipoOperacion("C");
					totalCreditos = totalCreditos.add(new BigDecimal(operaciones.getString("monto_operacion")));
					formatoArchivosOps.setIndicadorRetencion("N");
				} else if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.DEBITO)) {
					//this.camposDetalle.put(this.TIPO_OPERACION, "D");
					formatoArchivosOps.setTipoOperacion("D");
					totalDebitos = totalDebitos.add(new BigDecimal(operaciones.getString("monto_operacion")));
					if (operaciones.getString("numero_retencion") != null && !operaciones.getString("numero_retencion").equals("")) {																
						//this.camposDetalle.put(this.MTO_RETENCION, formatearNumero(operaciones.getString("monto_operacion")));
						formatoArchivosOps.setMontoRetencion(formatoArchivosOps.formatearNumero(operaciones.getString("monto_operacion")));
						transFinanciera=operaciones.getString("TRNFIN_ID");
						//this.camposDetalle.put(this.INDICA_RETENCION, "S");						
						if(transFinanciera.equals(TransaccionFija.COMISION_DEB) && !controlProceso.getIndDesbloMult()){//Se verifica si el negocio genera bloqueos multiples (uno por capital y uno por comision) en caso que no, no le genera el indicador de retencion a la op de deb comision
							formatoArchivosOps.setIndicadorRetencion("N");	
						} else {
							formatoArchivosOps.setIndicadorRetencion("S");	
						}
						
						//this.camposDetalle.put(this.NRO_RETENCION, operaciones.getString("numero_retencion"));
						formatoArchivosOps.setNumeroRetencion(operaciones.getString("numero_retencion"));
						generarOpDesbloqueo=false;//variable que indica que no se genera operacion de desbloqueo por que la operacion de debito ya tiene indicador de retencion 
					}else{
						formatoArchivosOps.setIndicadorRetencion("N");
					}						
				} else if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.DESBLOQUEO) && generarOpDesbloqueo) {					
					//this.camposDetalle.put(this.TIPO_OPERACION, "Q");
					formatoArchivosOps.setTipoOperacion("Q");
					//this.camposDetalle.put(this.MTO_RETENCION, formatearNumero(operaciones.getString("monto_operacion")));
					formatoArchivosOps.setMontoRetencion(formatoArchivosOps.formatearNumero(operaciones.getString("monto_operacion")));
					//this.camposDetalle.put(this.INDICA_RETENCION, "T");
					formatoArchivosOps.setIndicadorRetencion("T");
					//this.camposDetalle.put(this.NRO_RETENCION, operaciones.getString("numero_retencion"));
					formatoArchivosOps.setNumeroRetencion(operaciones.getString("numero_retencion"));
				}
				//this.camposDetalle.put(this.CODIGO_OPERACION, operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"));
				formatoArchivosOps.setCodOperacion(operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"));
				//this.camposDetalle.put(this.CTA_CLIENTE, operaciones.getString("ctecta_numero"));
				formatoArchivosOps.setNumeroCuenta(operaciones.getString("ctecta_numero"));			
											
				formatoArchivosOps.setReferencia("9" + Utilitario.rellenarCaracteres(operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"), '0', 4, false) + Utilitario.rellenarCaracteres(operaciones.getString("orden_id_referencia"), '0', 8, false) + "00");//TODO Verificar campo ordene_id (Se debe guardar ordene_id_relacion para ctas moneda ext) 				
				//this.camposDetalle.put(this.NUMERO_OPERACION_ID, Utilitario.rellenarCaracteres(operaciones.getString("ordene_operacion_id"), '0', LONGITUD_NUMERO_OPERACION_ID, false));
				formatoArchivosOps.setCamposAdicionales(Utilitario.rellenarCaracteres(operaciones.getString("ordene_operacion_id"), '0', EstructuraArchivosOps.CAMPOS_ADD_LONG.getValor(), false));
				//this.camposDetalle.put(this.CLAVE_LIG, operaciones.getString("ordene_id")); // La clave liga es el número de orden				
				formatoArchivosOps.setClaveLiga(operaciones.getString("orden_id_proceso"));				
				//this.camposDetalle.put(this.MONTO_OPERACION, formatearNumero(operaciones.getString("monto_operacion")));
				formatoArchivosOps.setMontoOperacion(formatoArchivosOps.formatearNumero(operaciones.getString("monto_operacion")));
				
				formatoArchivosOps.setFechaOperacion(operaciones.getString("fecha_operacion"));//TODO Cambiar en el query nombre del campo de consulta por fecha aplicar 
				
				formatoArchivosOps.setIdOperacion(operaciones.getString("ordene_operacion_id"));
				formatoArchivosOps.setIdOrdenProceso(operaciones.getString("orden_id_proceso"));				
				formatoArchivosOps.armarCamposAdicionales();
				
				formatoArchivosOps.setDivisa(operaciones.getString("MONEDA_ID"));
				
				
				registrosDeArchivo.add(formatoArchivosOps.generarRegistroDeDetalle());
				// Detalle del archivo
				detalle.setIdOperacion(Long.parseLong(operaciones.getString("ordene_operacion_id")));
				detalle.setIdOrden(Long.parseLong(operaciones.getString("orden_id_proceso")));
				archivo.agregarDetalle(detalle);
			}
			
			// Genera los totales
			//this.camposTotales.put(this.TOTAL_CREDITOS, formatearNumero(String.valueOf(totalCreditos.setScale(2, RoundingMode.HALF_EVEN))));
			formatoArchivosOps.setTotalCredito(formatoArchivosOps.formatearNumero(String.valueOf(totalCreditos.setScale(2, RoundingMode.HALF_EVEN))));
			//this.camposTotales.put(this.TOTAL_DEBITOS, formatearNumero(String.valueOf(totalDebitos.setScale(2, RoundingMode.HALF_EVEN))));
			formatoArchivosOps.setTotalDebito(formatoArchivosOps.formatearNumero(String.valueOf(totalDebitos.setScale(2, RoundingMode.HALF_EVEN))));
			logger.info("Total debito " + totalDebitos);
			System.out.println("Total debito " + totalDebitos);
			logger.info("Total Credito " + totalCreditos);
			System.out.println("Total debito " + totalCreditos);
			logger.info("Total debito " + formatoArchivosOps.formatearNumero(String.valueOf(totalDebitos.setScale(2, RoundingMode.HALF_EVEN))));
			logger.info("Total Credito " + formatoArchivosOps.formatearNumero(String.valueOf(totalCreditos.setScale(2, RoundingMode.HALF_EVEN))));

			registrosDeArchivo.add(formatoArchivosOps.generarRegistroDeTotales());

			// Arma la cabecera y el definitivo
			//this.camposCabecera.put(this.FECHA_PROCESO, sdf.format(new Date()));
			formatoArchivosOps.setFechaProceso(sdf.format(new Date()));
			//this.camposCabecera.put(this.HORA_PROCESO, sdfhora.format(new Date()));
			formatoArchivosOps.setHoraProceso(sdfhora.format(new Date()));
			//this.camposCabecera.put(this.TOTAL_REGISTROS, String.valueOf(totalDeRegistros));
			formatoArchivosOps.setTotalRegistros(String.valueOf(totalDeRegistros));
			//this.camposCabecera.put(this.NUMERO_PROCESO, idEjecucion);
			formatoArchivosOps.setNumeroProceso(idEjecucion);
			registrosDeArchivoDefinitivo.add(formatoArchivosOps.generarRegistroDeCabecera());
			registrosDeArchivoDefinitivo.addAll(registrosDeArchivo);

			logger.info("Total de registros " + totalDeRegistros);
			logger.info("Escribiendo en archivo temporal " + archivoTemporal);			
			FileUtil.put(archivoTemporal, registrosDeArchivoDefinitivo, true);

			logger.info("Actualizando base de datos... ");

			db.execBatch(this._dso, controlArchivoDAO.insertarArchivoRecepcion(archivo));
			
			//NM32454 RESOLUCION DE INCIDENCIA ITS - 2817
			//ITS-2817 Error en proceso de control de ciclos en caso de error en tabla INFI_TB_804_CONTROL_ARCH_DET
			//SE COLOCA LA LINEA DE TRASMICION DEL ARCHIVO AL FINAL DEL METODO
			// copiarArchivo(new File(archivoTemporal),getDestinoFinal());
			transferirArchivo(archivoTemporal, getDestinoFinal());
		} catch (Exception ex) {
			try {
				logger.error("Error en el proceso ", ex);
				ex.printStackTrace();
				FileUtil.delete(archivoTemporal);
			} catch (Exception e) {
				logger.error("No fue posible borrar el archivo " + archivoTemporal);
			}
			throw ex;
		} finally {
			try {									
				if (statement != null) {						
					statement.close();				
				}
				transaccion.closeConnection();
			} catch (Exception e) {
				logger.error("Error efectuando modificación al proceso. " + e.getMessage());
			}
		}
	}
	
	//protected abstract void respaldarArchivo();
	protected void respaldarArchivo(File archivo,ControlProcesosOps controlOps,boolean procesoEnvio, boolean borrarArchivo) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("carpetaRespaldo: " + getCarpetaRespaldo(controlOps.getNombreRutaRespaldo()));
		}
		logger.info("PROCESO DE RESPALDO DE ACHIVO ----------->");
		final String carpeta = getCarpetaRespaldo(controlOps.getNombreRutaRespaldo());

		final File carpetaRespaldo = new File(carpeta);
		carpetaRespaldo.mkdirs();

		// agregar fecha/hora al nombre del archivo respaldo
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(archivo.getName().substring(0, archivo.getName().length() - 4)); // nombre
		
			if(procesoEnvio){
				stringBuilder.append(controlOps.getProcesoEnvio());
			}else {
				stringBuilder.append(controlOps.getProcesoRecepcion());
			}
		
		stringBuilder.append(sdfArchivoRespaldo.format(new Date()));
		stringBuilder.append(archivo.getName().substring(archivo.getName().length() - 4)); // extension

		File destino = new File(carpeta.concat(stringBuilder.toString()));

		logger.info("Respaldar: " + archivo.getAbsolutePath() + " a: " + destino.getAbsolutePath());
		System.out.println("Respaldar: " + archivo.getAbsolutePath() + " a: " + destino.getAbsolutePath());
		FileUtil.copiarArchivo(archivo, destino);
		// if (archivo.renameTo(destino)) {
		if (borrarArchivo) {
			archivo.delete();
		}
		// }
	}
	
	protected void verificarArchivoFinal(File destinoFinal) throws Exception {
		if (destinoFinal.exists()) {
			logger.info("El archivo existe, no es posible sobreescribir el archivo");
			throw new Exception("El archivo en el destino final existe " + destinoFinal.toString());
		}
	}
	
	protected DataSet getRegistrosAProcesar() throws Exception {
		
		return null;
	}
	
	protected boolean comenzarProceso(ControlProcesosOps controlProc,boolean procEnvio) throws Exception{
		
		 
		if(controlProc!=null){
			procesoDAO= new ProcesosDAO(_dso);
			String procesoEjecucion=new String();
			if(procEnvio){
				procesoEjecucion=controlProc.getProcesoEnvio();	
			}else {
				procesoEjecucion=controlProc.getProcesoRecepcion();
			}
			
		
			procesoDAO.listarProcesosActivosPorTransaccion(controlProc.getListaProceso());

		if (procesoDAO.getDataSet().count() > 0) {
			logger.info("Proceso: " + proceso + " ya esta en ejecución.");
			return false;
		}
		operacionDAO= new OperacionDAO(_dso);
		ordenDAO= new OrdenDAO(_dso);
		titulosBloqueoDAO= new TitulosBloqueoDAO(_dso);
		custodiaDAO=new CustodiaDAO(_dso);
		mensajeOpicsDao = new MensajeOpicsDAO(_dso);
		intentoOperacionDAO= new IntentoOperacionDAO(_dso); 
		logger.info("TIPO PROCESO EN EJECUCION -----------------> " + procesoEjecucion + " -------------------> ");
		controlArchivoDAO = new ControlArchivoDAO(_dso);
		proceso = new Proceso();
		proceso.setTransaId(procesoEjecucion);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		logger.info("TIPO PROCESO INSERTADO: " + proceso.getTransaId() + " -------------------> ");
		logger.info(" procesoDAO.insertar(proceso) ---------->" +  procesoDAO.insertar(proceso));
		/* Primero creamos el proceso */
		db.exec(_dso, procesoDAO.insertar(proceso));

		logger.info("Comenzó proceso: " + procesoEjecucion);
		}
		return true;
	}
	
	protected boolean verificarCiclo(ControlProcesosOps controlProc,boolean procEnvio) throws Exception {//ArrayList<String> status,String... ciclos) throws Exception {			
		boolean puedeEjecutar = false;
				
		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		//UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);		
		if(controlProc!=null){ 
			controlArchivoDAO.listarCicloAbiertoPorTransaccion(controlProc.getListaCiclo());		
			DataSet dataset = controlArchivoDAO.getDataSet();
			if(procEnvio){//Indica que se esta ejecutando la ENVIO del archivo de Pasivo
				if(dataset.count() == 0 ) {
					puedeEjecutar = true;				
				}			
			} else {//Proceso de RECEPCION de Archivo a Mainframe									
				String cicloEnProceso="";				
				if(dataset.count() != 0 ) {	
					dataset.first();
					dataset.next();
					cicloEnProceso=dataset.getValue("STATUS");					
					if(controlProc.getCiclo().equals(cicloEnProceso)){//Verifica si el ciclo abierto en base de datos corresponde al invocado desde el Scheduler   
						puedeEjecutar = true;	
					}				
				}			
			}		
		}
		return puedeEjecutar;
	}
	
	
	protected void obtenerArchivoTemporal(ControlProcesosOps controlProcesosOps) {
		System.out.println("********obtenerArchivoTemporal *********** ");
		// Directorio temp para guardar los archivos
//		archivoTemporal = parametrosFTP.get(ParametrosSistema.TEMP_DIRECTORY);		
//		if (archivoTemporal.endsWith(File.separator)) {
//			archivoTemporal += parametrosOPS.get(controlProcesosOps.getNombreArchivo());
//		} else {
//			archivoTemporal += File.separator + parametrosOPS.get(controlProcesosOps.getNombreArchivo());
//		}
		archivoTemporal=parametrosOPS.get(controlProcesosOps.getNombreArchivoEnvio());
	}
	
	protected String getDestinoFinal() {
		return null;
	}
	
	protected String getNombreArchivo() {
		return null;
	}
	
	protected void transferirArchivo(String archivoOriginal, String archivoFinal) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Archivo origen " + archivoOriginal);
			logger.debug("Archivo destino " + archivoFinal);
			// logger.debug("Ruta final " + rutaFinal);
		}
		archivoFinal = "'" + archivoFinal + "'";

		logger.info("Archivo origen " + archivoOriginal);
		logger.info("Archivo destino " + archivoFinal);
		
		FTPUtil ftpUtil = new FTPUtil(parametrosOPS.get(ParametrosSistema.DIRECCION_SERVIDOR_FTP_OPS), this._dso);
		ftpUtil.putFTPAscci(archivoOriginal, archivoFinal, "", false);
	}
	
	protected void recepcionArchivo(File archivo) throws FileNotFoundException, IOException, Exception {
		
		try{
		if (!archivo.exists()) {
			logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
			
			ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);
			
			procesarArchivo(archivoRecepcion);

		}	
		
		}	catch (Exception ex){
			ex.printStackTrace();
			System.out.println("ex.getMessage() --> " + ex.getMessage());
			proceso.setDescripcionError(ex.getMessage());		
		}finally{
			terminarProceso();
		}
		
	}

	protected void procesarArchivo(ArchivoRecepcion archivoRecepcion) throws Exception {
		String linea = null;
		int totalOrdenes=0;
		formatoArchivosOps=new FormatoArchivosOps();
		//LEER Y VALIDAR CABECERA
		linea = archivoRecepcion.leerLinea();		
		formatoArchivosOps.setLinea(linea);
		logger.info("Ejecucion de metodo procesarArchivo - Lectura de Linea:  "+linea);
		if(formatoArchivosOps.leerCabecera()){
			procesarCabecera(formatoArchivosOps);
			
			//PROCESAR CUERPO
			linea = archivoRecepcion.leerLinea();
			formatoArchivosOps.setLinea(linea);
			if (linea != null) {
				//int countDetalle=0;				
				while (linea != null) {							
					System.out.println("formatoArchivosOps.getTipoRegistro() " + formatoArchivosOps.getTipoRegistro());
					/*if(countDetalle!=0){}*/
					if (linea.length() > 0) {
						System.out.println("linea: " + linea);
						if(formatoArchivosOps.leerCuerpo()){
							totalOrdenes++;
							procesarDetalle();							
						}else{ 
							break;
						}
					}
					linea = archivoRecepcion.leerLinea();
					formatoArchivosOps.setLinea(linea);
				}
				//LEER TOTALES
				//formatoArchivosOps.setLinea(linea);
				/*if(formatoArchivosOps.leerPieTotales()){
					System.out.println("linea: " + linea);
				}*/
			}			
		}	
	}		
	
	protected void procesarCabecera(FormatoArchivosOps formatoArchivosOps) throws Exception {
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Fecha: " + formatoArchivosOps.getFechaProceso());
			sb.append(", Hora: " + formatoArchivosOps.getHoraProceso());
			sb.append(", Registros: " + formatoArchivosOps.getTotalRegistros());
			sb.append(", Numero proceso: " + formatoArchivosOps.getNumeroProceso());
			sb.append(", Codigo Lote: " + formatoArchivosOps.getCodigoLote());
			System.out.println(sb.toString());
			logger.debug(sb.toString());
		}

		if(!formatoArchivosOps.getCodigoLote().equalsIgnoreCase(EstructuraArchivosOps.CODIGO_LOTE_OK.getCodigoLote())){
			throw new Exception("El lote presenta errores, no es posible continuar con el proceso. CODIGO_LOTE:"+formatoArchivosOps.getCodigoLote());
		}
		
		final int ciclo = Integer.valueOf(formatoArchivosOps.getNumeroProceso());
		// Verifica si el ciclo está abierto, de no ser así no se procesa el archivo
		controlArchivoDAO.listarCiclosAbiertos(ciclo);
		if (controlArchivoDAO.getDataSet().count() == 0) {
			throw new Exception("El ciclo correspondiente al número " + ciclo + " no se encuentra abierto, por lo tanto no es posible continuar con el proceso");
		}

		logger.info("Procesando ciclo: " + ciclo);

	}	
	
	protected void procesarDetalle() throws Exception {		
		logger.info(" Ingreso al metodo procesarDetalle ");		
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(", numeroOrden: " + formatoArchivosOps.getOrdeneId());
			sb.append(", numeroOperacion: " + formatoArchivosOps.getIdOperacion());
			sb.append(", codigoRetorno: " + formatoArchivosOps.getCodigoRetorno());
			sb.append(", codigoRetencion: " + formatoArchivosOps.getNumeroRetencion());
			sb.append(", fechaAplicacion: " + formatoArchivosOps.getFechaOperacion());
			sb.append(", horaAplicacion: " + formatoArchivosOps.getHoraOperacion());

			logger.debug(sb.toString());
			
			actualizarOperacion();
		}
	}
	
		
	
	//protected void actualizarOperacion(Long numeroOrden, Long numeroOperacion, String codigoRetorno, String descripcionRespuesta, String codigoRetencion, String fechaAplicacion, String horaAplicacion) throws Exception {
	protected void actualizarOperacion() throws Exception{	
		
		logger.info(" Actualizacion de Operacion orden ----> " + formatoArchivosOps.getOrdeneId());
		System.out.println(" Actualizacion de Operacion orden ----> " + formatoArchivosOps.getOrdeneId());
		Detalle detalle = new Detalle();
		operacionesTotales++;
		logger.info("Numero de  codigoRetorno operacion: "+formatoArchivosOps.getCodigoRetorno());
		System.out.println("Numero de  codigoRetorno operacion: "+formatoArchivosOps.getCodigoRetorno());
		logger.info(" COD_RETORNO_ERROR: "+COD_RETORNO_ERROR+" - "+" COD_RETORNO_OK: "+COD_RETORNO_OK);
		System.out.println(" COD_RETORNO_ERROR: "+COD_RETORNO_ERROR+" - "+" COD_RETORNO_OK: "+COD_RETORNO_OK);
				
		if (logger.isDebugEnabled()) {
			logger.debug("numeroOperacion: " + formatoArchivosOps.getIdOperacion() + ", codigoRetorno: " + formatoArchivosOps.getCodigoRetorno() + ", codigoRetencion: " + formatoArchivosOps.getNumeroRetencion()+ ",descripcion: " + formatoArchivosOps.getDescripcionRespuesta());
		}
				
		//***INICIO SECCION DE CODIGO NUEVO (EN PRUEBA)**/
		if((formatoArchivosOps.getTipoOperacion().equals(COD_D)) || (formatoArchivosOps.getTipoOperacion().equals(COD_C))){//Verificacion Operaciones de Debito
			if(COD_RETORNO_OK.equals(formatoArchivosOps.getCodigoRetorno())){//CODIGO RETORNO OK
				operacionesExitosas++;
				if (formatoArchivosOps.getIndicadorRetencion().equals(COD_S)) {//INDICADOR DE RETENCION
					logger.info("Orden posee Retencion ");
					batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_APLICADA, formatoArchivosOps.getIdOperacionLong(), Utilitario.DateToString(new Date(), "yyyyMMdd")));
				} else {//OPERACION SIN RETENCION
					logger.info("Orden NO posee Retencion ");
					batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_APLICADA, formatoArchivosOps.getIdOperacionLong(), formatoArchivosOps.getFechaOperacion()));
				}
			} else {//CODIGO RETORNO NO OK
				operacionesRetencionNOK++;
				OperacionIntento intento = new OperacionIntento();
				intento.setIdOrden(formatoArchivosOps.getOrdeneIdProcesoLong());
				intento.setIdOperacion(formatoArchivosOps.getIdOperacionLong());
				intento.setIndicadorAplicado(0);
				intento.setTextoError(formatoArchivosOps.getDescripcionRespuesta());

				batch.add(intentoOperacionDAO.insertar(intento));

				batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_RECHAZADA, formatoArchivosOps.getIdOperacionLong(), null));
				detalle.setStatus(ConstantesGenerales.STATUS_RECHAZADA);
				detalle.setError(formatoArchivosOps.getDescripcionRespuesta().replace("'", ""));
				detalle.setIdOrden(formatoArchivosOps.getOrdeneIdProcesoLong());
				detalle.setIdOperacion(formatoArchivosOps.getIdOperacionLong());
				batch.add(controlArchivoDAO.actualizarOperacionDetalle(formatoArchivosOps.getNumeroProcesoInt(), detalle));
				
			}
		
		}	
			if((formatoArchivosOps.getTipoOperacion().equals(COD_Q))){//OPERACIONES DE BLOQUEO / DESBLOQUEO
				if(COD_RETORNO_OK.equals(formatoArchivosOps.getCodigoRespuestaRetencion())){					
						//batch.add(operacionDAO.resetNumeroRetencion(formatoArchivosOps.getIdOperacionLong()));
						batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_APLICADA, formatoArchivosOps.getIdOperacionLong(), Utilitario.DateToString(new Date(), "yyyyMMdd")));
					} else {
						batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_RECHAZADA, formatoArchivosOps.getIdOperacionLong(), null));
						formatoArchivosOps.setDescripcionRespuesta(formatoArchivosOps.getDescripcionRespuesta()+ " - COD: " + formatoArchivosOps.getNumeroRetencion() + " DESC_RET: " + formatoArchivosOps.getDescripcionRespuestaRetencion());
						
						operacionesRetencionNOK++;
						OperacionIntento intento = new OperacionIntento();
						intento.setIdOrden(formatoArchivosOps.getOrdeneIdProcesoLong());
						intento.setIdOperacion(formatoArchivosOps.getIdOperacionLong());
						intento.setIndicadorAplicado(0);
						intento.setTextoError(formatoArchivosOps.getDescripcionRespuesta());

						batch.add(intentoOperacionDAO.insertar(intento));

						batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_RECHAZADA, formatoArchivosOps.getIdOperacionLong(), null));
						detalle.setStatus(ConstantesGenerales.STATUS_RECHAZADA);
						detalle.setError(formatoArchivosOps.getDescripcionRespuesta().replace("'", ""));
						detalle.setIdOrden(formatoArchivosOps.getOrdeneIdProcesoLong());
						detalle.setIdOperacion(formatoArchivosOps.getIdOperacionLong());
						batch.add(controlArchivoDAO.actualizarOperacionDetalle(formatoArchivosOps.getNumeroProcesoInt(), detalle));
						logger.info("operacionesRetencionNOK: "+operacionesRetencionNOK);
				}
			}	
			
			actualizarOrden(formatoArchivosOps);
			
	}
		//***FIN SECCION DE CODIGO NUEVO (EN PRUEBA)**/
		
		
	
	protected void actualizarOrden(FormatoArchivosOps formatoArchivosOps/*Long numeroOrden*/) throws Exception {
		long numeroOrden=0;
		numeroOrden=formatoArchivosOps.getOrdeneIdProcesoLong();
		logger.info("Actualizar estatus de la orden  BatchOps :" + numeroOrden);		
		
		if (numeroOrden != 0) {					
		
			if (logger.isDebugEnabled()) {
				logger.debug("orden: " + numeroOrden + ", operacionesTotales: " + operacionesTotales + ", operacionesExitosas: " + operacionesExitosas);
			}
			if (!batch.isEmpty()) {
				if (logger.isDebugEnabled()) {
					logger.debug("batch: " + batch.toString());
				}

				logger.debug("Operaciones a ejecutar: ");
				String[] retorno = new String[batch.size()];
				for (int i = 0; i < batch.size(); i++) {
					logger.debug("SQL: " + i + batch.get(i).toString());
					retorno[i] = batch.get(i).toString();
				}
				db.execBatch(_dso, retorno);

				batchEjecutados++;
				batch.clear();
			}
			logger.debug("----------ANTES DE LA VALIDACION----------operacionesRetencionNOK="+operacionesRetencionNOK+", operacionesRetencionNOK+operacionesExitosas="+(operacionesRetencionNOK+operacionesExitosas) +", operacionesTotales="+operacionesTotales);
			if (operacionesExitosas > 0 && ((operacionesExitosas == operacionesTotales)||((operacionesRetencionNOK+operacionesExitosas)==operacionesTotales))) {
				ordenesExitosas++;
				logger.debug("----------CONDICION TRUE----------");
				if (!existenOperacionesPendientes(numeroOrden)) {	
					Orden orden=new Orden();
					logger.debug("----------NO EXISTEN OPERACIONES PENDIENTES POR COBRAR----------");
					if(controlProceso.getProcesoEnvio().equals(com.bdv.infi.logic.interfaces.TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO)){//Se verifica si el proceso que se esta ejecucando es de COBRO LIQUIDACION SUBASTA
						//if (this instanceof Liquidacion) {//TODO PROBLEMA A RESOLVER 
						//Resolver como EMULAR proceso de cobro liquidacion y desbloqueo de titulo en caso de negocio "Subasta de titulos" 
						desbloquearTitulos(numeroOrden);
						if (!batch.isEmpty()) {
						String[] retorno = new String[batch.size()];
							for (int i = 0; i < batch.size(); i++) {
								logger.debug("SQL: " + i + batch.get(i).toString());
								retorno[i] = batch.get(i).toString();
							}
							db.execBatch(_dso, retorno);
							batch.clear();
						}
					}//FIN Validacion si el proceso ejecutando es cobro Liquidacion Subasta
					//}					
					//ACTUALIZACION DE ORDENES		
					orden.setStatus(controlProceso.getStatusActOrden());
					orden.setIdOrden(formatoArchivosOps.getOrdeneIdProcesoLong());
					ordenDAO.actualizarStatus(orden);
					//orden.getStatus()
					//orden.getIdOrden()
				}
				
			}
			logger.debug("OperacionesExitosas: " + operacionesExitosas + " operacionesTotales:" + operacionesTotales);
		}
	}
	
	protected boolean existenOperacionesPendientes(long ordenId) throws Exception {
		boolean operacionesPendientes = false;
		operacionDAO.listarOperacionesFinancieraOrden(ordenId);
		DataSet data = operacionDAO.getDataSet();
		while (data.next()) {
			if (data.getValue("status_operacion").equals(ConstantesGenerales.STATUS_EN_ESPERA) || data.getValue("status_operacion").equals(ConstantesGenerales.STATUS_RECHAZADA)) {
				operacionesPendientes = true;
				logger.debug("**** EXISTEN OPERACIONES PENDIENTES POR COBRAR **** ");
				break;
			}
		}
		return operacionesPendientes;
	}
	
	/**
	 * Desbloquea los títulos del cliente si el pago fue efectuado. Desbloquea tanto los bloqueados por pago como los bloqueados por recompra. Actualiza la fecha valor de la orden de pacto de recompra y su respectivo swift
	 * 
	 * @param numeroOrden
	 *            número de la orden a verificar
	 * @throws Exception
	 *             en caso de error
	 */
	protected void desbloquearTitulos(long numeroOrden) throws Exception {
		Long idCliente = null;
		String idTitulo = "";
		DataSet dataSet = null;

		ordenDAO.listarTitulosOrden(numeroOrden);

		dataSet = ordenDAO.getDataSet();
		Custodia custodia = new Custodia();

		if (logger.isDebugEnabled()) {
			logger.debug("orden: " + numeroOrden + " tiene " + dataSet.count() + " titulos.");
		}

		while (dataSet.next()) {
			if (idCliente == null) {
				idCliente = Long.parseLong(dataSet.getValue("CLIENT_ID"));
			}

			idTitulo = dataSet.getValue("TITULO_ID");
			// Bloqueo por pago
			TituloBloqueo titulo = titulosBloqueoDAO.listarBloqueo(idTitulo, idCliente, TipoBloqueos.BLOQUEO_POR_PAGO, Beneficiarios.BENEFICIARIO_DEFECTO, dataSet.getValue("tipo_producto_id"));

			if (titulo != null && titulo.getTitulo() != null && !titulo.getTitulo().equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("Desbloqueando por pago el título: " + titulo.getTitulo());
				}
				batch.add(titulosBloqueoDAO.delete(titulo));
			}

			// Bloqueo por recompra. Resta la posición recomprada
			titulo = titulosBloqueoDAO.listarBloqueo(idTitulo, idCliente, TipoBloqueos.BLOQUEO_POR_RECOMPRA, Beneficiarios.BENEFICIARIO_DEFECTO, dataSet.getValue("tipo_producto_id"));
			if (titulo != null && titulo.getTitulo() != null && !titulo.getTitulo().equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug("Desbloqueando por recompra el título: " + titulo.getTitulo());
				}
				batch.add(titulosBloqueoDAO.delete(titulo));

				custodiaDAO.verificarTitulo(String.valueOf(idCliente), titulo.getTitulo(), dataSet.getValue("tipo_producto_id"));
				if (custodiaDAO.getDataSet().count() > 0) {
					custodiaDAO.getDataSet().next();
					custodia.setCantidad(Long.parseLong(custodiaDAO.getDataSet().getValue("TITCUS_CANTIDAD")));
					// Resta la cantidad recomprada por el cliente
					custodia.setIdCliente(idCliente);
					custodia.setIdTitulo(titulo.getTitulo());
					custodia.setTipoProductoId(dataSet.getValue("tipo_producto_id"));
					custodia.restarCantidad(titulo.getTituloCustodiaCantidad());
					batch.add(custodiaDAO.modificar(custodia));
				}
			}
		}
		//NM25287: TTS_443 24/04/2014. Se eliminó la validación de existencia de títulos para que funcione para el caso de divisas
		// Actualiza los mensajes swift
		logger.debug("Actualizando fechas de recompra");
		String[] consultas = ordenDAO.actualizarOrdenDeRecompra(numeroOrden);
		for (String string : consultas) {
			batch.add(string);
		}

		// Actualiza las fechas de mensaje d opics
		logger.debug("Actualizando fechas de mensaje de opics");
		batch.add(mensajeOpicsDao.actualizarFechaValorMensaje(numeroOrden));
		
	}
	
	protected String getCarpetaRespaldo(String rutaRespaldo) {
		String carpeta = parametrosOPS.get(rutaRespaldo);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}	
	
	protected void cerrarCiclo() throws ParseException, Exception {
		final int ciclo = Integer.valueOf(this.formatoArchivosOps.getNumeroProcesoInt());// camposCabecera.get(NUMERO_PROCESO));
		logger.info("Cerrando ciclo: " + ciclo);

		controlArchivoDAO.cerrarCiclo(ciclo);
	}

	protected void terminarProceso() {
		try {
			if (proceso != null) {
				proceso.setFechaFin(new Date());
				if (proceso.getDescripcionError() == null) {
					proceso.setDescripcionError("");
				}				
				if (formatoArchivosOps.getNumeroProceso() != null) {
					System.out.println(proceso.getDescripcionError());
					proceso.setDescripcionError("Ciclo: " + formatoArchivosOps.getNumeroProceso() + ". " + proceso.getDescripcionError() + " ");
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
}
