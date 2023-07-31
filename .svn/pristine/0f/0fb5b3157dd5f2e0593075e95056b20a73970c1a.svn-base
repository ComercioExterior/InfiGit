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
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.Custodia;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.data.OperacionIntento;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.TituloBloqueo;
import com.bdv.infi.ftp.FTPUtil;
import com.bdv.infi.logic.interfaces.Beneficiarios;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoBloqueos;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public abstract class BatchOps {
	static final Logger logger = Logger.getLogger(BatchOps.class);
	static final String TIPO_ADJUDICACION = "0";
	static final String TIPO_LIQUIDACION = "1";
	static final String TIPO_ABONO_CUENTA_DOLARES = "3";
	static final String TIPO_ABONO_CUENTA_DOLARES_VENTAS = "4";
	static final String TIPO_ABONO_CUENTA_DOLARES_CUPON = "5";

	protected DataSource _dso;
	int usuarioId;
	String nmUsuario = "";
	HashMap<String, String> parametrosOPS;
	HashMap<String, String> parametrosFTP;
	protected Proceso proceso;

	// recepcion
	Long numeroUltimaOrden = null;
	int batchEjecutados = 0;
	int ordenesTotales = 0;
	int ordenesExitosas = 0;
	int operacionesTotales = 0;
	int operacionesExitosas = 0;
	int operacionesRetencionNOK = 0;
	int tipo = -1;
	int claveLiga = 0;
	DataSet datasetOrden = null;
	private Long idOrdenTomaOrden=null;

	/** Id de la unidad de inversión */	
	int idUnidadInversion;
	
	/** Id del Título a procesar */
	String idTitulo;
	
	public String idTipoMoneda ="";

	final List<String> batch = new ArrayList<String>();
	protected OrdenDAO ordenDAO;
	protected OperacionDAO operacionDAO;
	protected IntentoOperacionDAO intentoOperacionDAO;
	protected ControlArchivoDAO controlArchivoDAO;
	protected UnidadInversionDAO inversionDAO;
	protected ProcesosDAO procesoDAO;
	protected TitulosBloqueoDAO titulosBloqueoDAO;
	protected CustodiaDAO custodiaDAO;
	protected MensajeOpicsDAO mensajeOpicsDao;
	protected String archivoTemporal = "";

	final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	final SimpleDateFormat sdfhora = new SimpleDateFormat("HHmmss");
	public final SimpleDateFormat sdfArchivoRespaldo = new SimpleDateFormat("ddMMyyyyHHmmss");
	final SimpleDateFormat sdfAplicacion = new SimpleDateFormat("yyyyMMddHHmmss");
	final SimpleDateFormat formatoDeFechaBD = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

	// Campos
	final HashMap<String, String> camposCabecera = new HashMap<String, String>();
	final HashMap<String, String> camposDetalle = new HashMap<String, String>();
	final HashMap<String, String> camposTotales = new HashMap<String, String>();

	// Cabecera
	public final String TIPO_REGISTRO = "TIPO_REGISTRO";
	public final String FECHA_PROCESO = "FECHA_PROCESO";
	public final String HORA_PROCESO = "HORA_PROCESO";
	public final String TOTAL_REGISTROS = "TOTAL_REGISTROS";
	public final String NUMERO_PROCESO = "NUMERO_PROCESO";
	public final String CODIGO_VALIDADOR_LOTE = "CODIGO_VALIDADOR_LOTE";

	// Detalle
	public final String CONSECUTIVO = "CONSECUTIVO";
	public final String TIPO_OPERACION = "TIPO_OPERACION";
	public final String CODIGO_OPERACION = "CODIGO_OPERACION";
	public final Integer LONGITUD_CODIGO_OPERACION = 4;

	public final String CTA_CLIENTE = "CTA_CLIENTE";
	public final Integer LONGITUD_CTA_CLIENTE = 20;

	public final String REFERENCIA = "REFERENCIA";
	public final String CLAVE_LIG = "CLAVE_LIG";
	public final Integer INICIA_CLAVE_LIG = 49;	
	public final Integer LONGITUD_CLAVE_LIG = 20;
	public final Integer FIN_CLAVE_LIG = INICIA_CLAVE_LIG+LONGITUD_CLAVE_LIG;

	public final String MONTO_OPERACION = "MONTO_OPERACION";
	public final Integer LONGITUD_MONTO_OPERACION = 15;

	public final String INDICA_RETENCION = "INDICA_RETENCION";
	public final String NRO_RETENCION = "NRO_RETENCION";
	public final Integer LONGITUD_NRO_RETENCION = 5;
	public final String MTO_RETENCION = "MTO_RETENCION";
	public final Integer LONGITUD_MTO_RETENCION = 15;

	public final String COD_RETORNO = "COD_RETORNO";
	public final String COD_RESPUESTA = "COD_RESPUESTA";
	public final String NUMERO_OPERACION_ID = "NUMERO_OPERACION_ID";
	public final String RESERVADO = "RESERVADO";
	public final String TIPO_MONEDA = "TIPO_MONEDA";

	public final String DESC_RESPUESTA = "DESC_RESPUESTA";
	public final String COD_RETORNO_RETENCION = "COD_RETORNO_RETENCION";
	public final String DESC_RESPUESTA_RETENCION = "DESC_RESPUESTA_RETENCION";
	public final String HORA_APLIC_OPERACION = "HORA_APLIC_OPERACION";
	public final String FECHA_APLIC_OPERACION = "FECHA_APLIC_OPERACION";

	// Totales
	public final String TOTAL_DEBITOS = "TOTAL_DEBITOS";
	public final String TOTAL_CREDITOS = "TOTAL_CREDITOS";

	public final String COD_RETORNO_OK = "00";
	public final String COD_RETORNO_ERROR = "99";

	public final String COD_RETENCION_OK = "00";
	public final String COD_T = "T";
	public final String COD_S = "S";

	public final Integer COMIENZO_TIPO_REGISTRO = 0;
	public final Integer LONGITUD_TIPO_REGISTRO = 2;
	public final Integer FIN_TIPO_REGISTRO = COMIENZO_TIPO_REGISTRO + LONGITUD_TIPO_REGISTRO;
	public final Integer COMIENZO_FECHA_PROCESO = 2;
	public final Integer LONGITUD_FECHA_PROCESO = 8;
	public final Integer FIN_FECHA_PROCESO = COMIENZO_FECHA_PROCESO + LONGITUD_FECHA_PROCESO;
	public final Integer COMIENZO_HORA_PROCESO = 10;
	public final Integer LONGITUD_HORA_PROCESO = 6;
	public final Integer FIN_HORA_PROCESO = COMIENZO_HORA_PROCESO + LONGITUD_HORA_PROCESO;
	public final Integer COMIENZO_TOTAL_REGISTROS = 16;
	public final Integer LONGITUD_TOTAL_REGISTROS = 7;
	public final Integer FIN_TOTAL_REGISTROS = COMIENZO_TOTAL_REGISTROS + LONGITUD_TOTAL_REGISTROS;
	public final Integer COMIENZO_NUMERO_PROCESO = 23;
	public final Integer LONGITUD_NUMERO_PROCESO = 6;
	public final Integer FIN_NUMERO_PROCESO = COMIENZO_NUMERO_PROCESO + LONGITUD_NUMERO_PROCESO;
	public final Integer COMIENZO_VALIDATOR_LOTE = 29;
	public final Integer LONGITUD_VALIDATOR_LOTE = 2;
	public final Integer FIN_VALIDATOR_LOTE = COMIENZO_VALIDATOR_LOTE + LONGITUD_VALIDATOR_LOTE;

	public final Integer COMIENZO_CONSECUTIVO = 2;
	public final Integer LONGITUD_CONSECUTIVO = 7;
	public final Integer FIN_CONSECUTIVO = COMIENZO_CONSECUTIVO + LONGITUD_CONSECUTIVO;
	public final Integer COMIENZO_REFERENCIA = 34;
	public final Integer LONGITUD_REFERENCIA = 15;
	public final Integer FIN_REFERENCIA = COMIENZO_REFERENCIA + LONGITUD_REFERENCIA;
	//cambio incidencia Produccion nm26659
	public final Integer COMIENZO_CODIGO_RETORNO = 105;	
	public final Integer LONGITUD_CODIGO_RETORNO = 2;
	public final Integer FIN_CODIGO_RETORNO = COMIENZO_CODIGO_RETORNO + LONGITUD_CODIGO_RETORNO;
	//cambio incidencia Produccion nm26659
	//public final Integer COMIENZO_CODIGO_RESPUESTA = 105;//TODO Verificar si hay cambio de numero de campo
	public final Integer COMIENZO_CODIGO_RESPUESTA = 264;
	public final Integer LONGITUD_CODIGO_RESPUESTA = 2;
	public final Integer FIN_CODIGO_RESPUESTA = COMIENZO_CODIGO_RESPUESTA + LONGITUD_CODIGO_RESPUESTA;
	public final Integer COMIENZO_NUMERO_OPERACION_ID = 114;
	public final Integer LONGITUD_NUMERO_OPERACION_ID = 8;
	public final Integer FIN_NUMERO_OPERACION_ID = COMIENZO_NUMERO_OPERACION_ID + LONGITUD_NUMERO_OPERACION_ID;

	// No se usa por los momentos
	public final Integer COMIENZO_RESERVADO = 122;
	public final Integer LONGITUD_RESERVADO = 42;

	public final Integer LONGITUD_TIPO_MONEDA = 3;
	public final Integer LONGITUD_RESERVADO_ABONO_CTA_USD = 39;

	public final Integer FIN_RESERVADO = COMIENZO_RESERVADO + LONGITUD_RESERVADO;

	public final Integer COMIENZO_DESCRIPCION_RESPUESTA = 164;
	public final Integer LONGITUD_DESCRIPCION_RESPUESTA = 100;
	public final Integer FIN_DESCRIPCION_RESPUESTA = COMIENZO_DESCRIPCION_RESPUESTA + LONGITUD_DESCRIPCION_RESPUESTA;
	public final Integer COMIENZO_CODIGO_RETENCION = 264;
	public final Integer LONGITUD_CODIGO_RETENCION = 2;
	public final Integer FIN_CODIGO_RETENCION = COMIENZO_CODIGO_RETENCION + LONGITUD_CODIGO_RETENCION;
	// public final Integer COMIENZO_DESCRIPCION_RESPUESTA_RETENCION = 266;
	public final Integer COMIENZO_DESCRIPCION_RESPUESTA_RETENCION = 267;// TODO Verificar si hay cambio de numero de campo
	public final Integer LONGITUD_DESCRIPCION_RESPUESTA_RETENCION = 150;
	public final Integer FIN_DESCRIPCION_RESPUESTA_RETENCION = COMIENZO_DESCRIPCION_RESPUESTA_RETENCION + LONGITUD_DESCRIPCION_RESPUESTA_RETENCION;
	public final Integer COMIENZO_FECHA_APLICACION = 416;
	public final Integer LONGITUD_FECHA_APLICACION = 8;
	public final Integer FIN_FECHA_APLICACION = COMIENZO_FECHA_APLICACION + LONGITUD_FECHA_APLICACION;
	public final Integer COMIENZO_HORA_APLICACION = 424;
	public final Integer LONGITUD_HORA_APLICACION = 8;
	public final Integer FIN_HORA_APLICACION = COMIENZO_HORA_APLICACION + LONGITUD_HORA_APLICACION;
	public final Integer COMIENZO_INDICA_RETENCION = 84;
	public final Integer LONGITUD_INDICA_RETENCION = 1;
	public final Integer FIN_INDICA_RETENCION = COMIENZO_INDICA_RETENCION + LONGITUD_INDICA_RETENCION;
	//NM29643 - infi_tts_466 31/07/2014 Actualizacion proceso envio correos
	public String idEjecRecepcionAbonoUSD;

	public BatchOps() {
		reiniciarValoresCamposCabecera();
		reiniciarValoresCamposDetalle();
		reiniciarValoresCamposTotales();
	}

	protected abstract boolean verificarCiclo(String ciclo, String status) throws Exception;

	protected boolean comenzarProceso(String tipoProceso) throws Exception {
		// if (tipo != TIPO_ADJUDICACION && tipo != TIPO_LIQUIDACION) {
		// logger.error("Tipo de proceso inválido: " + tipo);
		// throw new Exception("Tipo de proceso inválido: " + tipo);
		// }

		procesoDAO = new ProcesosDAO(_dso);

		procesoDAO.listarPorTransaccionActiva(tipoProceso);

		if (procesoDAO.getDataSet().count() > 0) {
			logger.info("Proceso: " + tipoProceso + " ya esta en ejecución.");
			return false;
		}
		logger.info("TIPO PROCESO " + tipoProceso + " -------------------> ");
		System.out.println("TIPO PROCESO " + tipoProceso + " -------------------> ");
		proceso = new Proceso();
//		//NM29643 infi_TTS_466
		proceso.setEjecucionId(Integer.parseInt(procesoDAO.dbGetSequence(_dso,ConstantesGenerales.SECUENCIA_PROCESOS)));
		proceso.setTransaId(tipoProceso);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		logger.info("TIPO PROCESO INSERTADO: " + proceso.getTransaId() + " -------------------> ");
		logger.info(" procesoDAO.insertar(proceso) ---------->" +  procesoDAO.insertar(proceso));
		/* Primero creamos el proceso */
		db.exec(_dso, procesoDAO.insertar(proceso));

		logger.info("Comenzó proceso: " + tipoProceso);

		controlArchivoDAO = new ControlArchivoDAO(_dso);
		inversionDAO = new UnidadInversionDAO(_dso);
		ordenDAO = new OrdenDAO(_dso);
		mensajeOpicsDao = new MensajeOpicsDAO(_dso);
		operacionDAO = new OperacionDAO(_dso);
		intentoOperacionDAO = new IntentoOperacionDAO(_dso);
		titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
		custodiaDAO = new CustodiaDAO(_dso);
		return true;
	}

	//Metodo Desarrollado en requerimiento SICAD nm26659
	protected boolean comenzarProceso(String... tipoProceso) throws Exception {
		// if (tipo != TIPO_ADJUDICACION && tipo != TIPO_LIQUIDACION) {
		// logger.error("Tipo de proceso inválido: " + tipo);
		// throw new Exception("Tipo de proceso inválido: " + tipo);
		// }
				
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
			return false;
		}
		logger.info("TIPO PROCESO EN EJECUCION -----------------> " + procesoEjecucion + " -------------------> ");
		proceso = new Proceso();
		proceso.setTransaId(procesoEjecucion);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		logger.info("TIPO PROCESO INSERTADO: " + proceso.getTransaId() + " -------------------> ");
		logger.info(" procesoDAO.insertar(proceso) ---------->" +  procesoDAO.insertar(proceso));
		/* Primero creamos el proceso */
		db.exec(_dso, procesoDAO.insertar(proceso));

		logger.info("Comenzó proceso: " + tipoProceso);

		controlArchivoDAO = new ControlArchivoDAO(_dso);
		inversionDAO = new UnidadInversionDAO(_dso);
		ordenDAO = new OrdenDAO(_dso);
		mensajeOpicsDao = new MensajeOpicsDAO(_dso);
		operacionDAO = new OperacionDAO(_dso);
		intentoOperacionDAO = new IntentoOperacionDAO(_dso);
		titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
		custodiaDAO = new CustodiaDAO(_dso);
		return true;
	}
	protected void terminarProceso() {
		try {
			if (proceso != null) {
				proceso.setFechaFin(new Date());
				if (proceso.getDescripcionError() == null) {
					proceso.setDescripcionError("");
				}
				if (this.camposCabecera.get(this.NUMERO_PROCESO) != null) {
					proceso.setDescripcionError("Ciclo: " + this.camposCabecera.get(this.NUMERO_PROCESO) + ". " + proceso.getDescripcionError() + " ");
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

	/** Reinicia los valores de los campos de cabecera */
	protected void reiniciarValoresCamposCabecera() {
		camposCabecera.put(TIPO_REGISTRO, "01");
		camposCabecera.put(FECHA_PROCESO, "");
		camposCabecera.put(HORA_PROCESO, "");
		camposCabecera.put(TOTAL_REGISTROS, "");
		camposCabecera.put(NUMERO_PROCESO, "");
		camposCabecera.put(CODIGO_VALIDADOR_LOTE, "");
	}

	/** Reinicia los valores de los campos de detalle */
	protected void reiniciarValoresCamposDetalle() {
		camposDetalle.put(TIPO_REGISTRO, "02");
		camposDetalle.put(CONSECUTIVO, "");
		camposDetalle.put(TIPO_OPERACION, "");
		camposDetalle.put(CODIGO_OPERACION, "");
		camposDetalle.put(CTA_CLIENTE, "");
		camposDetalle.put(REFERENCIA, "");
		camposDetalle.put(CLAVE_LIG, "");
		camposDetalle.put(MONTO_OPERACION, "");
		camposDetalle.put(INDICA_RETENCION, "N");
		camposDetalle.put(NRO_RETENCION, "");
		camposDetalle.put(MTO_RETENCION, "");
		camposDetalle.put(COD_RETORNO, "00");
		camposDetalle.put(COD_RESPUESTA, "00");
		camposDetalle.put(NUMERO_OPERACION_ID, "00000000");
		camposDetalle.put(RESERVADO, "");
		camposDetalle.put(DESC_RESPUESTA, "");
		camposDetalle.put(COD_RETORNO_RETENCION, "00");
		camposDetalle.put(DESC_RESPUESTA_RETENCION, "");
		camposDetalle.put(HORA_APLIC_OPERACION, "00000000");
		camposDetalle.put(FECHA_APLIC_OPERACION, "00000000");
	}

	/** Reinicia los valores de los campos de totales */
	protected void reiniciarValoresCamposTotales() {
		camposTotales.put(TIPO_REGISTRO, "03");
		camposTotales.put(TOTAL_DEBITOS, "");
		camposTotales.put(TOTAL_CREDITOS, "");
	}

	/** Genera el texto para la cabecera del archivo */
	protected String generarRegistroDeCabecera() {
		StringBuilder sb = new StringBuilder();
		sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(TIPO_REGISTRO), '0', 2, false));
		sb.append(camposCabecera.get(FECHA_PROCESO));
		sb.append(camposCabecera.get(HORA_PROCESO));
		sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(TOTAL_REGISTROS), '0', 7, false));
		sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(NUMERO_PROCESO), '0', 6, false));
		sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(CODIGO_VALIDADOR_LOTE), '0', 2, false));
		return sb.toString();
	}

	/** Genera el detalle */
	protected String generarRegistroDeDetalle() {
		StringBuilder sb = new StringBuilder();
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(TIPO_REGISTRO), '0', LONGITUD_TIPO_REGISTRO, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CONSECUTIVO), '0', LONGITUD_CONSECUTIVO, false));
		sb.append(camposDetalle.get(TIPO_OPERACION));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CODIGO_OPERACION), '0', LONGITUD_CODIGO_OPERACION, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CTA_CLIENTE), '0', LONGITUD_CTA_CLIENTE, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(REFERENCIA), ' ', LONGITUD_REFERENCIA, true));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CLAVE_LIG), ' ', LONGITUD_CLAVE_LIG, true));

		// v13 13 enteros, 2 decimales
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MONTO_OPERACION), '0', LONGITUD_MONTO_OPERACION, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MONTO_OPERACION).substring(0,camposDetalle.get(MONTO_OPERACION).length()-2), '0', 11, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MONTO_OPERACION).substring(camposDetalle.get(MONTO_OPERACION).length()-2), '0', 2, false));

		sb.append(camposDetalle.get(INDICA_RETENCION));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(NRO_RETENCION), '0', LONGITUD_NRO_RETENCION, false));

		// v13 11 enteros, 2 decimales
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MTO_RETENCION), '0', LONGITUD_MTO_RETENCION, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MTO_RETENCION).substring(0,camposDetalle.get(MTO_RETENCION).length()-2), '0', 11, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MTO_RETENCION).substring(camposDetalle.get(MTO_RETENCION).length()-2), '0', 2, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RETORNO), ' ', LONGITUD_CODIGO_RETORNO, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RESPUESTA), ' ', 7, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(NUMERO_OPERACION_ID), '0', LONGITUD_NUMERO_OPERACION_ID, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(RESERVADO), ' ', LONGITUD_RESERVADO, false));

		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(DESC_RESPUESTA), ' ', LONGITUD_DESCRIPCION_RESPUESTA, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RETORNO_RETENCION), ' ', LONGITUD_CODIGO_RETENCION, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(DESC_RESPUESTA_RETENCION), ' ', LONGITUD_DESCRIPCION_RESPUESTA_RETENCION, false));
		sb.append(camposDetalle.get(FECHA_APLIC_OPERACION));
		sb.append(camposDetalle.get(HORA_APLIC_OPERACION));

		return sb.toString();
	}

	/** Genera el detalle de Abono cuenta nacional en Moneda Extranjera */
	protected String generarRegistroDeDetalleAbonoMonedaExtranjera() {
		StringBuilder sb = new StringBuilder();
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(TIPO_REGISTRO), '0', LONGITUD_TIPO_REGISTRO, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CONSECUTIVO), '0', LONGITUD_CONSECUTIVO, false));
		sb.append(camposDetalle.get(TIPO_OPERACION));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CODIGO_OPERACION), '0', LONGITUD_CODIGO_OPERACION, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CTA_CLIENTE), '0', LONGITUD_CTA_CLIENTE, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(REFERENCIA), ' ', LONGITUD_REFERENCIA, true));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CLAVE_LIG), ' ', LONGITUD_CLAVE_LIG, true));

		// v13 13 enteros, 2 decimales
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MONTO_OPERACION), '0', LONGITUD_MONTO_OPERACION, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MONTO_OPERACION).substring(0,camposDetalle.get(MONTO_OPERACION).length()-2), '0', 11, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MONTO_OPERACION).substring(camposDetalle.get(MONTO_OPERACION).length()-2), '0', 2, false));

		sb.append(camposDetalle.get(INDICA_RETENCION));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(NRO_RETENCION), '0', LONGITUD_NRO_RETENCION, false));

		// v13 11 enteros, 2 decimales
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MTO_RETENCION), '0', LONGITUD_MTO_RETENCION, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MTO_RETENCION).substring(0,camposDetalle.get(MTO_RETENCION).length()-2), '0', 11, false));
		// sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MTO_RETENCION).substring(camposDetalle.get(MTO_RETENCION).length()-2), '0', 2, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RETORNO), ' ', LONGITUD_CODIGO_RETORNO, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RESPUESTA), ' ', 7, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(NUMERO_OPERACION_ID), '0', LONGITUD_NUMERO_OPERACION_ID, false));		
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(RESERVADO), ' ', LONGITUD_RESERVADO_ABONO_CTA_USD, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(TIPO_MONEDA), '0', LONGITUD_TIPO_MONEDA, false));

		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(DESC_RESPUESTA), ' ', LONGITUD_DESCRIPCION_RESPUESTA, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RETORNO_RETENCION), ' ', LONGITUD_CODIGO_RETENCION, false));
		sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(DESC_RESPUESTA_RETENCION), ' ', LONGITUD_DESCRIPCION_RESPUESTA_RETENCION, false));
		sb.append(camposDetalle.get(FECHA_APLIC_OPERACION));
		sb.append(camposDetalle.get(HORA_APLIC_OPERACION));

		return sb.toString();
	}

	/** Generar el totales */
	protected String generarRegistroDeTotales() {
		StringBuilder sb = new StringBuilder();
		sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TIPO_REGISTRO), '0', 2, false));
		sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_DEBITOS), '0', 15, false));
		sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_CREDITOS), '0', 15, false));
		// sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_DEBITOS).substring(0,camposTotales.get(TOTAL_DEBITOS).length()-2), '0', 11, false));
		// sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_DEBITOS).substring(camposTotales.get(TOTAL_DEBITOS).length()-2), '0', 2, false));
		// sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_CREDITOS).substring(0,camposTotales.get(TOTAL_CREDITOS).length()-2), '0', 11, false));
		// sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_CREDITOS).substring(camposTotales.get(TOTAL_CREDITOS).length()-2), '0', 2, false));
		return sb.toString();
	}

	/**
	 * Busca si se tienen los permisos adecuados sobre las carpetas donde se deben dejar los archivos
	 * 
	 * @return
	 */
	protected void obtenerParametros() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosOPS = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_OPS);
		// Se buscan los parametros en base de datos
		parametrosFTP = parametrosDAO.buscarParametros(ConstantesGenerales.INTERFACE_OPICS);
	}

	// Verifica si hay permisos sobre el archivo
	protected void tienePermisosEscritura(File archivo) throws Exception {
		// logger.info("Ruta " + archivo);
		// logger.info("Verificando permisos de escritura para la ruta " + archivo.getParentFile());
		// if (!archivo.canWrite()){
		// logger.error("No se tienen permisos para escribir en la ruta " + archivo.getParentFile());
		// throw new Exception("No se tienen permisos para escribir en la ruta " + archivo.getParentFile());
		// }
	}

	// Verifica si hay permisos sobre el archivo
	protected void tienePermisosLectura(File archivo) throws Exception {
		// logger.info("Verificando permisos de lectura para la ruta " + archivo.getParentFile());
		// if (!archivo.getParentFile().canRead()){
		// logger.error("No se tienen permisos para leer en la ruta " + archivo.getParentFile());
		// throw new Exception("No se tienen permisos para leer en la ruta " + archivo.getParentFile());
		// }
	}

	protected void obtenerArchivoTemporal() {
		//System.out.println("********obtenerArchivoTemporal *********** ");
		// Directorio temp para guardar los archivos
		archivoTemporal = parametrosFTP.get(ParametrosSistema.TEMP_DIRECTORY);		
		if (archivoTemporal.endsWith(File.separator)) {
			archivoTemporal += parametrosOPS.get(getNombreArchivo());
		} else {
			archivoTemporal += File.separator + parametrosOPS.get(getNombreArchivo());
		}
	}

	protected File getArchivo(String ruta, String archivo) {
		String carpeta = parametrosOPS.get(ruta);
		// if(!carpeta.endsWith(File.separator)){
		// carpeta = carpeta.concat(File.separator);
		// }
		return new File(carpeta);
		// return new File(carpeta + parametrosOPS.get(archivo));
	}

	protected String getCarpetaRespaldo() {
		return null;
	}

	protected void respaldarArchivo(File archivo) throws Exception {
		respaldarArchivo(archivo, true);
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

		if (this instanceof AdjudicacionRecepcionSubasta || this instanceof AdjudicacionRecepcionSitme || this instanceof LiquidacionRecepcionSubasta || this instanceof LiquidacionRecepcionSitme || this instanceof AbonoRecepcionSitmeCuentaNacionalMonedaExtranjera || this instanceof AbonoRecepcionCuentaNacionalMonedaExtranjeraVentaTitulo
				|| this instanceof AbonoRecepcionCuentaNacionalMonedaExtranjeraPagoCupones ||this instanceof ConciliacionRetencionRecepcion) {
			stringBuilder.append("Recepcion");
		} else {
			stringBuilder.append("Envio");
		}
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

	//NM29643 - infi_tts_466 31/07/2014 Actualizacion proceso envio correos
	protected String recepcionArchivo(File archivo) throws FileNotFoundException, IOException, Exception {
		idEjecRecepcionAbonoUSD = "";
		
		if (!archivo.exists()) {
			logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
			
			ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);

			
			procesarArchivo(archivoRecepcion);

			respaldarArchivo(archivo);

			cerrarCiclo();
			
		}
		return idEjecRecepcionAbonoUSD;
	}

	protected void procesarArchivo(ArchivoRecepcion archivoRecepcion) throws Exception {

		camposCabecera.clear();
		camposDetalle.clear();
		camposTotales.clear();
		ordenesExitosas = 0;
		ordenesTotales = 0;
		operacionesExitosas = 0;
		operacionesTotales = 0;
		operacionesRetencionNOK = 0;
		
		String linea = archivoRecepcion.leerLinea();
		
		logger.info("Ejecucion de metodo procesarArchivo - Lectura de Linea:  "+linea);
		int num = 0;
		while (linea != null) {

			if (logger.isDebugEnabled()) {
				logger.debug("linea " + ++num + ": " + linea);
			}

			if (linea.length() >= 2) {

				// Extraer tipo registro
				final String tipo = linea.substring(COMIENZO_TIPO_REGISTRO, FIN_TIPO_REGISTRO);

				if ("01".equals(tipo)) {
					// Registro Cabecera
					camposCabecera.put(FECHA_PROCESO, linea.substring(COMIENZO_FECHA_PROCESO, FIN_FECHA_PROCESO));
					camposCabecera.put(HORA_PROCESO, linea.substring(COMIENZO_HORA_PROCESO, FIN_HORA_PROCESO));
					camposCabecera.put(TOTAL_REGISTROS, linea.substring(COMIENZO_TOTAL_REGISTROS, FIN_TOTAL_REGISTROS));
					camposCabecera.put(NUMERO_PROCESO, linea.substring(COMIENZO_NUMERO_PROCESO, FIN_NUMERO_PROCESO));
					camposCabecera.put(CODIGO_VALIDADOR_LOTE, linea.substring(COMIENZO_VALIDATOR_LOTE, FIN_VALIDATOR_LOTE));

					procesarCabecera();
					
					//NM29643 - infi_tts_466 31/07/2014 Actualizacion proceso envio correos
					if(camposCabecera!=null) idEjecRecepcionAbonoUSD = String.valueOf((camposCabecera.get(NUMERO_PROCESO))==null?"":camposCabecera.get(NUMERO_PROCESO));
					logger.debug("---------------------------idEjecucion recepcionArchivo: "+idEjecRecepcionAbonoUSD);
					
				} else if ("02".equals(tipo)) {
					// Registro Detalle

					camposDetalle.put(CONSECUTIVO, linea.substring(COMIENZO_CONSECUTIVO, FIN_CONSECUTIVO));
					logger.info("CONSECUTIVO --------> " + linea.substring(COMIENZO_CONSECUTIVO, FIN_CONSECUTIVO));
					camposDetalle.put(REFERENCIA, linea.substring(COMIENZO_REFERENCIA, FIN_REFERENCIA));
					logger.info("REFERENCIA --------> " + linea.substring(COMIENZO_REFERENCIA, FIN_REFERENCIA));
					camposDetalle.put(COD_RETORNO, linea.substring(COMIENZO_CODIGO_RETORNO, FIN_CODIGO_RETORNO));
					logger.info("COD_RETORNO --------> " + linea.substring(COMIENZO_CODIGO_RETORNO, FIN_CODIGO_RETORNO));
					camposDetalle.put(DESC_RESPUESTA, linea.substring(COMIENZO_DESCRIPCION_RESPUESTA, FIN_DESCRIPCION_RESPUESTA).trim());
					logger.info("DESC_RESPUESTA --------> " + linea.substring(COMIENZO_DESCRIPCION_RESPUESTA, FIN_DESCRIPCION_RESPUESTA).trim());
					camposDetalle.put(DESC_RESPUESTA_RETENCION, linea.substring(COMIENZO_DESCRIPCION_RESPUESTA_RETENCION, FIN_DESCRIPCION_RESPUESTA_RETENCION).trim());
					logger.info("DESC_RESPUESTA_RETENCION --------> " + linea.substring(COMIENZO_DESCRIPCION_RESPUESTA_RETENCION, FIN_DESCRIPCION_RESPUESTA_RETENCION).trim());
					camposDetalle.put(COD_RETORNO_RETENCION, linea.substring(COMIENZO_CODIGO_RETENCION, FIN_CODIGO_RETENCION));
					logger.info("COD_RETORNO_RETENCION *** --------> " + linea.substring(COMIENZO_CODIGO_RETENCION, FIN_CODIGO_RETENCION));
					camposDetalle.put(FECHA_APLIC_OPERACION, linea.substring(COMIENZO_FECHA_APLICACION, FIN_FECHA_APLICACION));
					logger.info("FECHA_APLIC_OPERACION --------> " + linea.substring(COMIENZO_FECHA_APLICACION, FIN_FECHA_APLICACION));
					camposDetalle.put(HORA_APLIC_OPERACION, linea.substring(COMIENZO_HORA_APLICACION, FIN_HORA_APLICACION));
					logger.info("HORA_APLIC_OPERACION --------> " + linea.substring(COMIENZO_HORA_APLICACION, FIN_HORA_APLICACION));
					camposDetalle.put(INDICA_RETENCION, linea.substring(COMIENZO_INDICA_RETENCION, FIN_INDICA_RETENCION));
					logger.info("INDICA_RETENCION --------> " + linea.substring(COMIENZO_INDICA_RETENCION, FIN_INDICA_RETENCION));
					camposDetalle.put(CLAVE_LIG, linea.substring(INICIA_CLAVE_LIG, FIN_CLAVE_LIG));
					logger.info("CLAVE_LIG --------> " + linea.substring(INICIA_CLAVE_LIG, FIN_CLAVE_LIG));
					camposDetalle.put(NUMERO_OPERACION_ID, linea.substring(COMIENZO_NUMERO_OPERACION_ID, FIN_NUMERO_OPERACION_ID));
					logger.info("NUMERO_OPERACION_ID --------> " + linea.substring(COMIENZO_NUMERO_OPERACION_ID, FIN_NUMERO_OPERACION_ID));
					procesarDetalle();

				} else if ("03".equals(tipo)) {
					// Registro Total

					procesarTotal();
				} else {
					logger.info("Registro invalido: " + linea);
				}
			}

			linea = archivoRecepcion.leerLinea();
			
		}

		if (numeroUltimaOrden != null) {
			// Actualizar estatus orden en BD
			
			if(idTipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
				idOrdenTomaOrden = ordenDAO.buscarDatosOrdenRelacion(numeroUltimaOrden,com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_PACTO_RECOMPRA);
				actualizarOrden(numeroUltimaOrden,idOrdenTomaOrden);
			} else {
				actualizarOrden(numeroUltimaOrden);
			}
		}

		logger.info("Ordenes: " + ordenesTotales);

		// actualizarUnidadInversion();

		if (logger.isDebugEnabled()) {
			logger.debug("batchEjecutados: " + batchEjecutados);
		}
	}

	protected void cerrarCiclo() throws ParseException, Exception {
		final int ciclo = Integer.valueOf(camposCabecera.get(NUMERO_PROCESO));
		logger.info("Cerrando ciclo: " + ciclo);

		controlArchivoDAO.cerrarCiclo(ciclo);
	}

	protected void procesarCabecera() throws Exception {
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Fecha: " + camposCabecera.get(FECHA_PROCESO));
			sb.append(", Hora: " + camposCabecera.get(HORA_PROCESO));
			sb.append(", Registros: " + camposCabecera.get(TOTAL_REGISTROS));
			sb.append(", Numero proceso: " + camposCabecera.get(NUMERO_PROCESO));

			logger.debug(sb.toString());
		}

		final int ciclo = Integer.valueOf(camposCabecera.get(NUMERO_PROCESO));
		// Verifica si el ciclo está abierto, de no ser así no se procesa el archivo
		controlArchivoDAO.listarCiclosAbiertos(ciclo);
		if (controlArchivoDAO.getDataSet().count() == 0) {
			throw new Exception("El ciclo correspondiente al número " + ciclo + " no se encuentra abierto, por lo tanto no es posible continuar con el proceso");
		}

		logger.info("Procesando ciclo: " + ciclo);

	}

	protected void procesarDetalle() throws Exception {
				
		logger.info(" Ingreso al metodo procesarDetalle ");
		final String numeroDeOperacion = camposDetalle.get(NUMERO_OPERACION_ID);
		final Long numeroOrden = obtenerNumeroDeOrden();
		final Long numeroOrdenPactoRecom = obtenerNumeroDeOrdenCtaNacionalDolares();
		final Long numeroOperacion = Long.valueOf(numeroDeOperacion);

		final String codigoRetorno = camposDetalle.get(COD_RETORNO);
		final String codigoRetencion = camposDetalle.get(COD_RETORNO_RETENCION);
		final String descripcionRespuesta = camposDetalle.get(DESC_RESPUESTA);
		final String fechaAplicacion = camposDetalle.get(FECHA_APLIC_OPERACION);
		final String horaAplicacion = camposDetalle.get(HORA_APLIC_OPERACION);

		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(", numeroOrden: " + numeroOrden);
			sb.append(", numeroOperacion: " + numeroOperacion);
			sb.append(", codigoRetorno: " + codigoRetorno);
			sb.append(", codigoRetencion: " + codigoRetencion);
			sb.append(", fechaAplicacion: " + fechaAplicacion);
			sb.append(", horaAplicacion: " + horaAplicacion);

			logger.debug(sb.toString());
		}
		
			
		if(idTipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
			logger.info("********** Recepcion de Archivo para Abono Cuenta en Dolares **********");
			if ((numeroUltimaOrden == null || (!numeroOrdenPactoRecom.equals(numeroUltimaOrden)))) {			
																								
				idOrdenTomaOrden = ordenDAO.buscarDatosOrdenRelacion(numeroUltimaOrden,com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_PACTO_RECOMPRA);								
				actualizarOrden(numeroUltimaOrden,idOrdenTomaOrden);												
				logger.info("Numero de Pacto Recompra en Recepcion de Archivo----------------> " + numeroOrdenPactoRecom);	
				numeroUltimaOrden = numeroOrdenPactoRecom;
				operacionesExitosas = 0;
				operacionesTotales = 0;
				operacionesRetencionNOK = 0; logger.info("cambio de orden----- numeroUltimaOrden: "+numeroUltimaOrden+"------operacionesRetencionNOK:"+operacionesRetencionNOK);
				ordenesTotales++;			
			}					
			
			actualizarOperacion(numeroOrdenPactoRecom, numeroOperacion, codigoRetorno, descripcionRespuesta, codigoRetencion, fechaAplicacion, horaAplicacion);		
		} else {	
			logger.info("********** Recepcion de archivo cuentas nacionales **********");
			if ((numeroUltimaOrden == null || !numeroOrden.equals(numeroUltimaOrden))) {
				// Actualizar orden en BD			
				actualizarOrden(numeroUltimaOrden);			
				numeroUltimaOrden = numeroOrden;
				operacionesExitosas = 0;
				operacionesTotales = 0;
				operacionesRetencionNOK = 0;logger.info("cambio de orden----- numeroUltimaOrden: "+numeroUltimaOrden+"------operacionesRetencionNOK:"+operacionesRetencionNOK);
				ordenesTotales++;
			}
			//Actualizar operacion en BD
			actualizarOperacion(numeroOrden, numeroOperacion, codigoRetorno, descripcionRespuesta, codigoRetencion, fechaAplicacion, horaAplicacion);
		}	
																		
		camposDetalle.clear();
	}

	protected void procesarTotal() {

		// StringBuilder sb = new StringBuilder();
		// sb.append("Referencia: " + camposDetalle.get(REFERENCIA));
		// sb.append(", Retorno: " + camposDetalle.get(COD_RETORNO));
		//		
		// logger.info(sb.toString());
	}

	protected void actualizarOrden(Long numeroOrden,Long... numeroOrdenTomaOrden) throws Exception {
		logger.info("Actualizar estatus de la orden  BatchOps :" + numeroOrden);
		long tomaOrden=0;
		
		if (numeroOrden != null) {
		
			//Busqueda de numero de orden original (trans_id=TOMA_ORDEN) en caso de ser abono cuenta nacional en dolares
			if(numeroOrdenTomaOrden.length>0){					
				tomaOrden=numeroOrdenTomaOrden[0];					
				logger.info("Abono cuenta Nacional en Dolares: Numero de orden Original (Trans_id Toma de Orden) " + tomaOrden);		
			}		
		
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
					logger.debug("----------NO EXISTEN OPERACIONES PENDIENTES POR COBRAR----------");
					if (this instanceof Liquidacion) {
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
					}
					
				/*
				 * 	//ordenDAO.actualizarEstatusOrdenClave (tomaOrden, ConstantesGenerales.ESTATUS_ORDEN_ENVIADA, Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
				 *  Seccion de codigo comentada en requerimiento SICAD_2 (No se actualiza la tabla de SOLICITUDES_SITME  NM26659_13/03/2014 
				 * */
						
					if(this.idTipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
						logger.info("Actualizacion de ordenes OPS_MONEDA_EXTRANJERA --------------------> " + idTipoMoneda);																			
						//ordenDAO.actualizarEstatusOrdenClave (tomaOrden, ConstantesGenerales.ESTATUS_ORDEN_ENVIADA, Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
						ordenDAO.actualizarOrdenesRecepcionSitmeCuentaDolares(numeroOrden);
						//NM29643 infi_TTS_466 25/07/2014: En caso de abono en cuenta extranjera, se marca el idEjecucion del proceso actual en las ordenes originales (Transaccion TOMA_ORDEN), campo ORDENE_EJEC_ID
						logger.debug("ID CICLO A SETEAR COMO ORDENE_EJEC_ID---------: "+String.valueOf(camposCabecera.get(NUMERO_PROCESO)));
						logger.debug("ID ORDEN (TOMA_ORDEN) A SETEARLE EL CAMPO ORDENE_EJEC_ID---------: "+tomaOrden);
						if(tomaOrden>0) ordenDAO.actualizarOrdenesTomaOrdenRecepcionSitmeCuentaDolares(tomaOrden, String.valueOf(camposCabecera.get(NUMERO_PROCESO))); //this.proceso.getEjecucionId()
					}else{	
						logger.info("Actualizacion de ordenes OPS_MONEDA_LOCAL --------------------> " + idTipoMoneda);
						//ordenDAO.actualizarEstatusOrdenClave (numeroOrden, ConstantesGenerales.ESTATUS_ORDEN_COBRADA, Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
					}
					

				} else {
					logger.debug("---------------EXISTEN OPERACIONES PENDIENTES POR COBRAR---------------");
					if (this.idTipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
						logger.info("Actualizacion de ordenes OPS_MONEDA_EXTRANJERA --------------------> " + idTipoMoneda);
						//ordenDAO.actualizarEstatusOrdenClave (tomaOrden, ConstantesGenerales.ESTATUS_ORDEN_PENDIENTE_COBRO, Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
					}else {
						logger.info("Actualizacion de ordenes OPS_MONEDA_LOCAL --------------------> " + idTipoMoneda);
						//ordenDAO.actualizarEstatusOrdenClave (numeroOrden, ConstantesGenerales.ESTATUS_ORDEN_PENDIENTE_COBRO, Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
					}
				}
			} else {
				//logger.debug("----------CONDICION FALSE----------");
				if (this.idTipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
					logger.info("Actualizacion de ordenes OPS_MONEDA_EXTRANJERA --------------------> " + idTipoMoneda);
					//ordenDAO.actualizarEstatusOrdenClave (tomaOrden, ConstantesGenerales.ESTATUS_ORDEN_PENDIENTE_COBRO, Utilitario.DateToString(new Date(), "dd/MM/yyyy"));				
				} else {
					logger.info("Actualizacion de ordenes OPS_MONEDA_LOCAL --------------------> " + idTipoMoneda);
					//ordenDAO.actualizarEstatusOrdenClave (numeroOrden, ConstantesGenerales.ESTATUS_ORDEN_PENDIENTE_COBRO, Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
				}
			}
			logger.debug("OperacionesExitosas: " + operacionesExitosas + " operacionesTotales:" + operacionesTotales);
		}
	}

	protected void actualizarOperacion(Long numeroOrden, Long numeroOperacion, String codigoRetorno, String descripcionRespuesta, String codigoRetencion, String fechaAplicacion, String horaAplicacion) throws Exception {
		logger.info(" Actualizacion de Operacion orden ----> " + numeroOrden);
		Detalle detalle = new Detalle();
		operacionesTotales++;
		logger.info("Numero de  codigoRetorno: "+codigoRetorno);
		logger.info("COD_RETORNO_ERROR: "+COD_RETORNO_ERROR);
				
		if (logger.isDebugEnabled()) {
			logger.debug("numeroOperacion: " + numeroOperacion + ", codigoRetorno: " + codigoRetorno + ", codigoRetencion: " + codigoRetencion + ",descripcion: " + descripcionRespuesta);
		}
		
		if (COD_RETORNO_OK.equals(codigoRetorno) && COD_RETORNO_OK.equals(codigoRetencion)) {
			operacionesExitosas++;
			// Si es Q el tipo de operación
			if (camposDetalle.get(INDICA_RETENCION).equals(COD_T)) {
				logger.info("Orden posee Retencion ");
				batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_APLICADA, numeroOperacion, Utilitario.DateToString(new Date(), "yyyyMMdd")));
			} else {
				logger.info("Orden NO posee Retencion ");
				batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_APLICADA, numeroOperacion, camposDetalle.get(this.FECHA_APLIC_OPERACION)));
			}
			detalle.setStatus(ConstantesGenerales.STATUS_APLICADA);
			detalle.setIdOrden(numeroOrden);
			detalle.setIdOperacion(numeroOperacion);
			batch.add(controlArchivoDAO.actualizarOperacionDetalle(Integer.parseInt(camposCabecera.get(NUMERO_PROCESO)), detalle));
			//Resolucion Incidencia TTS-385 en Produccion NM26659
			//} else if (COD_RETORNO_ERROR.equals(codigoRetorno)) {
		} else {
			// si codigo retencion == 00 && codigo retorno = 99
			// borrar numero retencion de la operacion
			if (camposDetalle.get(INDICA_RETENCION).equals(COD_T) || camposDetalle.get(INDICA_RETENCION).equals(COD_S)) {
				if (COD_RETENCION_OK.equals(codigoRetencion)) {
					batch.add(operacionDAO.resetNumeroRetencion(numeroOperacion));
				} else {
					descripcionRespuesta += " - COD: " + camposDetalle.get(COD_RETORNO_RETENCION) + " DESC_RET: " + camposDetalle.get(DESC_RESPUESTA_RETENCION);
					//NM25287. Incidencia COD20. CONTABILIZAMOS LOS DESBLOQUEOS FALLIDOS DE UNA ORDEN
					operacionesRetencionNOK++;
					logger.info("operacionesRetencionNOK: "+operacionesRetencionNOK);
				}
			}

			// insertar intento
			OperacionIntento intento = new OperacionIntento();
			intento.setIdOrden(numeroOrden);
			intento.setIdOperacion(numeroOperacion);

			if (camposDetalle.get(INDICA_RETENCION).equals(COD_T)) {
				intento.setFechaAplicacion(new Date());
			} else {
				intento.setFechaAplicacion(sdfAplicacion.parse(fechaAplicacion + horaAplicacion));
			}

			intento.setIndicadorAplicado(0);
			intento.setTextoError(descripcionRespuesta);

			batch.add(intentoOperacionDAO.insertar(intento));

			batch.add(operacionDAO.modificarOperacionStatus(ConstantesGenerales.STATUS_RECHAZADA, numeroOperacion, null));
			detalle.setStatus(ConstantesGenerales.STATUS_RECHAZADA);
			detalle.setError(descripcionRespuesta.replace("'", ""));
			detalle.setIdOrden(numeroOrden);
			detalle.setIdOperacion(numeroOperacion);
			batch.add(controlArchivoDAO.actualizarOperacionDetalle(Integer.parseInt(camposCabecera.get(NUMERO_PROCESO)), detalle));
		}
	}

	protected void envioArchivo(String tipoMoneda) throws Exception {
		logger.info("*** Ejecucion envioArchivo de la OPS BatchOps ****");
		System.out.println("*** Ejecucion envioArchivo de la OPS BatchOps ****");
		int consecutivo = 0;
		BigDecimal totalDebitos = new BigDecimal(0);
		BigDecimal totalCreditos = new BigDecimal(0);

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
			obtenerArchivoTemporal();

			transaccion.begin();
			statement = transaccion.getConnection().createStatement();

			operaciones = statement.executeQuery(getRegistrosAProcesar());

			// if (totalDeRegistros>0){
			while (operaciones.next()) {

				totalDeRegistros++;
				if (!cabecaraArmada) {
					idEjecucion = controlArchivoDAO.obtenerNumeroDeSecuencia();
					archivo.setIdEjecucion(Integer.parseInt(idEjecucion));
					archivo.setNombreArchivo(parametrosOPS.get(getNombreArchivo()));
					archivo.setUnidadInv(operaciones.getInt("undinv_id"));
					archivo.setUsuario(nmUsuario);
					archivo.setFechaGeneracion(new Date());
					archivo.setStatus(getCiclo());
					archivo.setVehiculoId(operaciones.getString("ordene_veh_tom"));

					// this.camposCabecera.put(this.FECHA_PROCESO, sdf.format(new Date()));
					// this.camposCabecera.put(this.HORA_PROCESO, sdfhora.format(new Date()));
					// this.camposCabecera.put(this.TOTAL_REGISTROS, String.valueOf(totalDeRegistros));
					// this.camposCabecera.put(this.NUMERO_PROCESO, idEjecucion);
					// registrosDeArchivoDefinitivo.add(this.generarRegistroDeCabecera());
					cabecaraArmada = true;
				}

				Detalle detalle = new Detalle();
				reiniciarValoresCamposDetalle();
				this.camposDetalle.put(this.CONSECUTIVO, String.valueOf(++consecutivo));

				if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.CREDITO)) {

					this.camposDetalle.put(this.TIPO_OPERACION, "C");
					totalCreditos = totalCreditos.add(new BigDecimal(operaciones.getString("monto_operacion")));
				} else if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.DEBITO)) {
					this.camposDetalle.put(this.TIPO_OPERACION, "D");
					totalDebitos = totalDebitos.add(new BigDecimal(operaciones.getString("monto_operacion")));
					if (operaciones.getString("numero_retencion") != null && !operaciones.getString("numero_retencion").equals("")) {
						this.camposDetalle.put(this.MTO_RETENCION, formatearNumero(operaciones.getString("monto_operacion")));
						this.camposDetalle.put(this.INDICA_RETENCION, "S");
						this.camposDetalle.put(this.NRO_RETENCION, operaciones.getString("numero_retencion"));
					}
				} else if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.DESBLOQUEO)) {
					this.camposDetalle.put(this.TIPO_OPERACION, "Q");
					this.camposDetalle.put(this.MTO_RETENCION, formatearNumero(operaciones.getString("monto_operacion")));
					this.camposDetalle.put(this.INDICA_RETENCION, "T");
					this.camposDetalle.put(this.NRO_RETENCION, operaciones.getString("numero_retencion"));
				}
				this.camposDetalle.put(this.CODIGO_OPERACION, operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"));
				this.camposDetalle.put(this.CTA_CLIENTE, operaciones.getString("ctecta_numero"));

				if(tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_NACIONAL)){		
					logger.info(" Envio de archivos OPS para MONEDA LOCAL ");
					System.out.println(" Envio de archivos OPS para MONEDA LOCAL ");
					this.camposDetalle.put(this.REFERENCIA, "9" + Utilitario.rellenarCaracteres(operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"), '0', 4, false) + Utilitario.rellenarCaracteres(operaciones.getString("ordene_id"), '0', 8, false) + "00");
				}else if (tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
					
					logger.info(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					System.out.println(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					this.camposDetalle.put(this.REFERENCIA, "9" + Utilitario.rellenarCaracteres(operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"), '0', 4, false) + Utilitario.rellenarCaracteres(operaciones.getString("ordene_id_relacion"), '0', 8, false) + "00");
				}
						

				this.camposDetalle.put(this.NUMERO_OPERACION_ID, Utilitario.rellenarCaracteres(operaciones.getString("ordene_operacion_id"), '0', LONGITUD_NUMERO_OPERACION_ID, false));

				this.camposDetalle.put(this.CLAVE_LIG, operaciones.getString("ordene_id")); // La clave liga es el número de orden

				this.camposDetalle.put(this.MONTO_OPERACION, formatearNumero(operaciones.getString("monto_operacion")));

				if (this instanceof AdjudicacionEnvioSubasta || this instanceof AdjudicacionEnvioSitme || this instanceof AdjudicacionEnvioSubastaDivisas|| this instanceof LiquidacionEnvioSubastaDivisas) {
					this.camposDetalle.put(this.FECHA_APLIC_OPERACION, operaciones.getString("fecha_operacion_padre"));
				}

				if(tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_NACIONAL)){		
					logger.info(" Envio de archivos OPS para MONEDA LOCAL ");
					System.out.println(" Envio de archivos OPS para MONEDA LOCAL ");
					registrosDeArchivo.add(this.generarRegistroDeDetalle());
				}else if (tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
					
					logger.info(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					System.out.println(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					this.camposDetalle.put(this.TIPO_MONEDA, operaciones.getString("MONEDA_ID"));
					registrosDeArchivo.add(this.generarRegistroDeDetalleAbonoMonedaExtranjera());
				}
				// Detalle del archivo
				detalle.setIdOperacion(Long.parseLong(operaciones.getString("ordene_operacion_id")));
				detalle.setIdOrden(Long.parseLong(operaciones.getString("ordene_id")));
				archivo.agregarDetalle(detalle);
			}
			// Genera los totales
			this.camposTotales.put(this.TOTAL_CREDITOS, formatearNumero(String.valueOf(totalCreditos.setScale(2, RoundingMode.HALF_EVEN))));
			this.camposTotales.put(this.TOTAL_DEBITOS, formatearNumero(String.valueOf(totalDebitos.setScale(2, RoundingMode.HALF_EVEN))));

			logger.info("Total debito " + totalDebitos);
			logger.info("Total Credito " + totalCreditos);

			logger.info("Total debito " + formatearNumero(String.valueOf(totalDebitos.setScale(2, RoundingMode.HALF_EVEN))));
			logger.info("Total Credito " + formatearNumero(String.valueOf(totalCreditos.setScale(2, RoundingMode.HALF_EVEN))));

			registrosDeArchivo.add(this.generarRegistroDeTotales());

			// Arma la cabecera y el definitivo
			this.camposCabecera.put(this.FECHA_PROCESO, sdf.format(new Date()));
			this.camposCabecera.put(this.HORA_PROCESO, sdfhora.format(new Date()));
			this.camposCabecera.put(this.TOTAL_REGISTROS, String.valueOf(totalDeRegistros));
			this.camposCabecera.put(this.NUMERO_PROCESO, idEjecucion);
			registrosDeArchivoDefinitivo.add(this.generarRegistroDeCabecera());
			registrosDeArchivoDefinitivo.addAll(registrosDeArchivo);

			logger.info("Total de registros " + totalDeRegistros);
			logger.info("Escribiendo en archivo temporal " + archivoTemporal);
			FileUtil.put(archivoTemporal, registrosDeArchivoDefinitivo, true);

			logger.info("Actualizando base de datos... ");

			// Si es liquidación marca recepción en 1
			if (this instanceof Liquidacion) {
				archivo.setInRecepcion(1);
			} else if (this instanceof CuentaNacionalMonedaExtranjera) {
				archivo.setInRecepcion(3);//flag 			
			}

			db.execBatch(this._dso, controlArchivoDAO.insertarArchivoRecepcion(archivo));
			
			//NM32454 RESOLUCION DE INCIDENCIA ITS - 2817
			//ITS-2817 Error en proceso de control de ciclos en caso de error en tabla INFI_TB_804_CONTROL_ARCH_DET
			//SE COLOCA LA LINEA DE TRASMICION DEL ARCHIVO AL FINAL DEL METODO
			// copiarArchivo(new File(archivoTemporal),getDestinoFinal());
			transferirArchivo(archivoTemporal, getDestinoFinal());
		} catch (Exception ex) {
			try {
				logger.error("Error en el proceso ", ex);
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

	protected void verificarArchivoFinal(File destinoFinal) throws Exception {
		if (destinoFinal.exists()) {
			logger.info("El archivo existe, no es posible sobreescribir el archivo");
			throw new Exception("El archivo en el destino final existe " + destinoFinal.toString());
		}
	}

	protected String getRegistrosAProcesar() throws Exception {
		return null;
	}

	protected String formatearNumero(String numero) {
		String s = Utilitario.formatearNumero(numero, "0000000000000.00");
		s = s.replace(",", "");
		s = s.replace(".", "");
		return s;
	}

	/**
	 * Copia el archivo hacia la ruta
	 * 
	 * @param archivo
	 * @param rutaFinal
	 */
	protected void copiarArchivo(File archivo, File rutaFinal) {
		logger.info("Copiando archivo desde " + archivo + " hasta " + rutaFinal);
		archivo.renameTo(rutaFinal);
	}

	protected String getDestinoFinal() {
		return null;
	}

	protected String getCiclo() {
		return null;
	}

	protected String getNombreArchivo() {
		return null;
	}

	protected String getArchivoTemporal(String nombreArchivo) {
		String carpeta = parametrosFTP.get(ParametrosSistema.TEMP_DIRECTORY);
		if (!carpeta.endsWith(File.separator)) {
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta.concat(nombreArchivo);
	}

	/**
	 * Verifica si para la orden existen operaciones pendientes
	 * 
	 * @param ordenId
	 *            id de la orden
	 * @return verdadero en caso de existir operaciones en estatus EN ESPERA o RECHAZADA
	 * @throws Exception
	 *             en caso de error
	 */
	protected boolean existenOperacionesPendientes(long ordenId) throws Exception {
		boolean operacionesPendientes = false;
		operacionDAO.listarOperacionesFinancieraOrden(ordenId);
		DataSet data = operacionDAO.getDataSet();
		while (data.next()) {
			if (data.getValue("status_operacion").equals(ConstantesGenerales.STATUS_EN_ESPERA) || data.getValue("status_operacion").equals(ConstantesGenerales.STATUS_RECHAZADA)) {
				operacionesPendientes = true;
				logger.debug("********** FLAG DANIEL ** EXISTEN OPERACIONES PENDIENTES POR COBRAR ________");
				break;
			}
		}
		return operacionesPendientes;
	}

	/**
	 * Transfiere el archivo temporal generado hasta la ruta final
	 * 
	 * @param archivoOriginal
	 *            archivo original a transferir
	 * @param archivoFinal
	 *            archivo final
	 * @param rutaFinal
	 *            ruta del archivo final
	 * @throws Exception
	 *             en caso de error
	 */
	protected void transferirArchivo(String archivoOriginal, String archivoFinal) throws Exception {
		// if (!rutaFinal.endsWith(File.separator)){
		// rutaFinal = rutaFinal.concat(File.separator);
		// }

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

	/**
	 * Actualiza la fecha valor de la orden de pacto de recompra y su respectivo swift
	 * 
	 * @param numeroOrden
	 *            número de la orden a verificar
	 * @throws Exception
	 *             en caso de error
	 */
	protected void actualizarOrdenRecompra(long numeroOrden) throws Exception {
		logger.debug("Actualizando fechas de recompra");
		String[] consultas = ordenDAO.actualizarOrdenDeRecompra(numeroOrden);
		for (String string : consultas) {
			batch.add(string);
		}
	}

	/**
	 * Obtiene el número de orden a partir de la referencia
	 * 
	 * @return el id de la orden relacionada a la operación financiera
	 * @throws Exception
	 *             en caso que el número de orden
	 */
	protected long obtenerNumeroDeOrden() throws Exception {
		long numeroOrden = Long.parseLong(camposDetalle.get(REFERENCIA).substring(5, 13));
		logger.info("obtenerNumeroDeOrden.numeroOrden: " + numeroOrden);
		// long numeroOrden = 0;
		// ordenDAO.listarOrdenOperacion(camposDetalle.get(REFERENCIA).substring(5, 13));
		// datasetOrden = ordenDAO.getDataSet();
		// if (datasetOrden.count()>0){
		// datasetOrden.next();
		// numeroOrden = Long.parseLong(datasetOrden.getValue("ordene_id"));
		// }else{
		// throw new Exception("Número de orden no encontrado a partir del número de operación número: " + camposDetalle.get(REFERENCIA).substring(5, 14));
		// }
		return numeroOrden;
	}		

	protected long obtenerNumeroDeOrdenCtaNacionalDolares() throws Exception {
		long numeroOrden = Long.parseLong(camposDetalle.get(CLAVE_LIG).trim());
		logger.info("obtenerNumeroDeOrden.numeroOrden: " + numeroOrden);
		// long numeroOrden = 0;
		// ordenDAO.listarOrdenOperacion(camposDetalle.get(REFERENCIA).substring(5, 13));
		// datasetOrden = ordenDAO.getDataSet();
		// if (datasetOrden.count()>0){
		// datasetOrden.next();
		// numeroOrden = Long.parseLong(datasetOrden.getValue("ordene_id"));
		// }else{
		// throw new Exception("Número de orden no encontrado a partir del número de operación número: " + camposDetalle.get(REFERENCIA).substring(5, 14));
		// }
		return numeroOrden;
	}	
}