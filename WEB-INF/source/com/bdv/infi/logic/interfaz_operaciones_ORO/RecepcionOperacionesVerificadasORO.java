package com.bdv.infi.logic.interfaz_operaciones_ORO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import models.intercambio.consultas.ciclos.CierreCiclo;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesDICOMDAO;
import com.bdv.infi.dao.SolicitudesORODAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.ConfigSubastaDICOM;
import com.bdv.infi.data.ConfigSubastaORO;
import com.bdv.infi.data.FormatoOperacionesDICOM;
import com.bdv.infi.data.FormatoOperacionesORO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudORO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesORO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.ArchivoRecepcion;
import com.bdv.infi.logic.interfaz_ops.BatchOps;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.data.SolicitudDICOM;

public class RecepcionOperacionesVerificadasORO extends BatchOps implements Runnable {

	public RecepcionOperacionesVerificadasORO(DataSource datasource) {
		super();
		_dso = datasource;
		// archivoAnulacionConv36DAO = new ArchivoAnulacionConv36DAO(_dso);
		// operacionDAO = new OperacionDAO(_dso);
	}
	
	static final Logger logger = Logger.getLogger(RecepcionOperacionesVerificadasORO.class);
	protected HashMap<String, String> parametrosRecepcionORO;
	// DataSource _dso;
	// private ArchivoAnulacionConv36DAO archivoAnulacionConv36DAO;
	protected String procesoEjecucion;
	protected String tipoCiclo;
	protected String tipoProducto;
	ArrayList<String> querysEjecutar = new ArrayList<String>();
	protected long idCiclo = 0;
	FormatoOperacionesORO formatoArchivoRecepcion = new FormatoOperacionesORO();
	UnidadInversionDAO uiDAO = null;
	protected String transaccionEjecutar;
	boolean existeArchivo = false;
	private long idUnidadInv = 0;
	SolicitudesORODAO solicitudesORODAO = null;
	VehiculoDAO vehiculoDAO = null;
	BlotterDAO blotterDAO = null;
	TransaccionFijaDAO transaccionFijaDAO = null;	
	private String empresaID;
	private String tipoSolicitud = null;// Indicador de Oferta (O) o Demanda (D)
	private int tipoSolicitudNumeric;
	private String vehiculoBDV = null;
	private String blotterId;
	private SolicitudORO solicitudORO;
	private ConfigSubastaORO configSubastaORO;
	com.bdv.infi.data.TransaccionFija transaccionFija = null;
	String monedaDicom_siglas="";
	Proceso procesoEnEjecucion;
	private long idCicloActivo;
	private boolean jornadaManual=false;
	String monedaSubasta="";

	public long getIdCicloActivo() {
		return idCicloActivo;
	}

	public void setIdCicloActivo(long idCicloActivo) {
		this.idCicloActivo = idCicloActivo;
	}

		public void run() {		
		boolean cicloAbierto=false;
			try {
					obtenerParametros();
					monedaSubasta=parametrosRecepcionORO.get(ParametrosSistema.MONEDA_SUBASTA_ORO);
					monedaDicom_siglas=parametrosRecepcionORO.get(monedaSubasta);
					//VALIDACION DE EJECUCION MANUAL
					jornadaManual=parametrosRecepcionORO.get(ParametrosSistema.JORNADA_RECEPCION_MANUAL)=="1"?true:false;
					//jornadaManual=true;
  
					cicloAbierto=verificarCicloAbierto("'"+TransaccionNegocio.CICLO_BATCH_ORO+"'");
	
		if(jornadaManual||cicloAbierto){
	
				File archivo = 	getArchivoORODemandas();
				logger.info("getArchivoORODemandas() --->"+archivo);
				
			if (archivo.exists()){// Recepcion Archivo Demandas
			
				existeArchivo = true;
				System.out.println("paso");
				transaccionEjecutar =TransaccionNegocio.PROC_RECEPCION_DEMANDA_ORO;
				tipoSolicitud = ConstantesGenerales.SOLICITUD_DEMANDA;
			}
					
		  logger.info("**********DEMANDA = " + transaccionEjecutar + "TSol = "+tipoSolicitud + "  *********");
		  logger.info("RececpcionOperacionesORO archivo.getAbsolutePath(): " + archivo.getAbsolutePath());
				
		  if (existeArchivo){
				procesoEnEjecucion = comenzarProceso(1, transaccionEjecutar);
	
			if (getProceso()!=null) {
					logger.info("RececpcionOperacionesORO getProceso(): " + getProceso() + "Antes de recepcion de Archivo");						
					recepcionArchivoOperacionesOROVerificadas(tipoSolicitud, archivo);
					
					//Verificacion para el proceso de Cierre de Ciclo
					cerrarCiclo();
			}
		  } else {
					logger.info(" *********************** El archivo no existe ***********************");
		  }
			
		}else {
				logger.info(" *********************** No existe un CICLO " +TransaccionNegocio.CICLO_BATCH_ORO+ " abierto para su procesamiento ***********************");
			  }
		} catch (Exception ex) {
			// logger.error("Error en el proceso de recepción archivo batch para CONCILIACION DE RETENCIONES. ", ex);
		
			ex.printStackTrace();
			logger.info("Exception ex ---> " + ex.getMessage());
			if (proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {
			terminarProceso();
			 logger.info("Terminado el proceso de recepción de archivo batch para CONCILIACION DE RETENCIONES... ");
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected File getArchivoORODemandas() {
		return getArchivo(ParametrosSistema.RUTA_ORO_DEMANDA_RECEP);// , ParametrosSistema.NOMBRE_ANUL_CONVENIO_36);
	}

	protected String getJornadaORO() {
		return parametrosRecepcionORO.get(ParametrosSistema.JORNADA_ORO);// , ParametrosSistema manejo de Titulo ahorro Oro Soberano
	}


	protected File getArchivo(String ruta, String archivo) {
		String carpeta = parametrosRecepcionORO.get(ruta);

		return new File(carpeta);
		// return new File(carpeta + parametrosOPS.get(archivo));
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected File getArchivo(String ruta) {
		String carpeta = parametrosRecepcionORO.get(ruta);
		
		return new File(carpeta);
		// return new File(carpeta + parametrosOPS.get(archivo));
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Metodo de busqueda de parametros asociados a interfaz con Moneda Extranjera
	 * */
	@Override
	protected void obtenerParametros() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		// TTS-537 Busqueda de parametros de transacciones a moneda extranjera para cancelacion de operaciones vencidas de Convenio 36 NM26659 28-12-2016
		parametrosRecepcionORO = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_ORO);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	protected void recepcionArchivoOperacionesOROVerificadas(String tipoSolicitud, File archivo) throws FileNotFoundException, IOException, Exception {
		long unInvId = 0;
		if (!archivo.exists()) {
	
			// logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
			// Lee Archivo de Moneda Extranjera y guarda en BD
			configBaseORO();
			procesarArchivo(configSubastaORO, archivo);
			respaldarArchivo(tipoSolicitud, archivo, true);
			/*
			 * AnulacionOperacionesVencidasConv36Logic anulacionOperacionesVencidasConv36Logic=new AnulacionOperacionesVencidasConv36Logic(AnulacionesConv36.MONEDA_EXTRANJERA_OP_SIN_PROCESAR.getValor(),_dso); anulacionOperacionesVencidasConv36Logic.run();
			 */
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void configBaseORO() throws Exception {

		solicitudesORODAO = new SolicitudesORODAO(_dso);
		vehiculoDAO = new VehiculoDAO(_dso);
		blotterDAO = new BlotterDAO(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		controlArchivoDAO = new ControlArchivoDAO(_dso);
		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		String usuario = parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES, _dso);
		
		InetAddress direccion = InetAddress.getLocalHost();
		String direccionIpstr = direccion.getHostAddress();
		
		/*UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO( _dso); 
		int usuario = Integer.parseInt((userEspecialDAO.idUserSession(usuario));
		Integer.parseInt((userEspecialDAO.idUserSession(getUserName())));*/
		
		long idUnidadInv = 0;
		configSubastaORO = new ConfigSubastaORO();
		idUnidadInv = solicitudesORODAO.creacionUnidadInvORO(tipoSolicitud, getJornadaORO(), usuario,direccionIpstr,ConstantesGenerales.PROCESO_VERIFICACION_ORO);
	//System.out.println("idUnidadInv------------> "+idUnidadInv);
	logger.info("RececpcionOperacionesORO idUnidadInv------------> "+idUnidadInv);
		configSubastaORO.setIdUnidadInv(idUnidadInv);
		configSubastaORO.setNroJornada(getJornadaORO());
		uiDAO = new UnidadInversionDAO(_dso);

		uiDAO.obtenerDatosUIporId("EMPRES_ID, TIPO_SOLICITUD", String.valueOf(idUnidadInv));
		if (uiDAO.getDataSet() != null && uiDAO.getDataSet().count() > 0) {
			uiDAO.getDataSet().next();
			empresaID = uiDAO.getDataSet().getValue("EMPRES_ID");
			tipoSolicitudNumeric = Integer.parseInt(uiDAO.getDataSet().getValue("TIPO_SOLICITUD"));
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
		configSubastaORO.setEmpresId(empresaID);
		configSubastaORO.setVehiculoBDVId(vehiculoBDV);
		configSubastaORO.setBlotterId(blotterId);

		transaccionFija = transaccionFijaDAO.obtenerTransaccionORO(idUnidadInv, vehiculoBDV, TransaccionFija.CAPITAL_SIN_IDB);
		configSubastaORO.setCodOperacionBloCap(transaccionFija.getCodigoOperacionCteBlo());
		configSubastaORO.setCodOperacionDebCap(transaccionFija.getCodigoOperacionCteDeb());
		configSubastaORO.setCodOperacionCreCap(transaccionFija.getCodigoOperacionCteCre());

		transaccionFija = transaccionFijaDAO.obtenerTransaccionORO(idUnidadInv, vehiculoBDV, TransaccionFija.COMISION_DEB);
		configSubastaORO.setCodOperacionBloCom(transaccionFija.getCodigoOperacionCteBlo());
		configSubastaORO.setCodOperacionDebCom(transaccionFija.getCodigoOperacionCteDeb());

		transaccionFija = transaccionFijaDAO.obtenerTransaccionORO(idUnidadInv, vehiculoBDV, TransaccionFija.TOMA_ORDEN_CTA_DOLARES);
		configSubastaORO.setCodOperacionCreConv20(transaccionFija.getCodigoOperacionCteCre());
		

		// configSubastaDICOM.setUsername(usuario);
		configSubastaORO.setUsername(usuario);
		configSubastaORO.setIp(direccionIpstr);
		configSubastaORO.setEjecucionId(String.valueOf(procesoEnEjecucion.getEjecucionId()));

	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void procesarArchivo(ConfigSubastaORO configSubastaORO, File archivo) throws Exception {
		// PROCESAR CABECERA
		ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);
		String linea = null;
		linea = archivoRecepcion.leerLinea();//TODO VERIFICAR EL USO DE CABECERA
		formatoArchivoRecepcion.cargarCabeceraMainframeOro(linea);//TODO VERIFICAR EL USO DE CABECERA 
	//System.out.println("********** Cabecera -> " + linea + " ********** ");
	logger.info("RececpcionOperacionesORO Cabecera -> " + linea + " **********"); 
	// VALIDAR CICLO
		procesarCabecera(formatoArchivoRecepcion);//TODO VERIFICAR EL USO DE CABECERA
	//System.out.println("despues de procesarCabecera ");
		linea = archivoRecepcion.leerLinea();
		if (linea != null) {
			// if (formatoArchivoRecepcion.esValidoFormatoRecepcion()) {//TODO VERIFICAR FUNCIONALIDAD
		//System.out.println("********** Arch_fecha " + new java.util.Date() + " Jor --> " + configSubastaORO.getNroJornada()+ " **********");
	logger.info("RececpcionOperacionesORO *** Arch_fecha " + new java.util.Date() + " Jor --> " + configSubastaORO.getNroJornada()+ " **********");			
			while (linea != null) {
				if (linea.length() > 0) {
					System.out.println("linea: " + linea);
					formatoArchivoRecepcion.cargarCuerpoOpVerificadas(linea);
					solicitudORO = new SolicitudORO(); // formatoArchivoRecepcion
					solicitudORO.setIdUnidadInversion(configSubastaORO.getIdUnidadInv());
					solicitudORO.setCodigoRespuesta(formatoArchivoRecepcion.getCodigoRespuestaInt());
					solicitudORO.setIdOperacion(formatoArchivoRecepcion.getIdOperacion());
					solicitudORO.setTipoCliente(formatoArchivoRecepcion.getTipoCliente());
					solicitudORO.setCedRifCliente(Long.parseLong(formatoArchivoRecepcion.getCedRifCliente()));
					solicitudORO.setNombreCliente(formatoArchivoRecepcion.getNombreCliente());
					solicitudORO.setTelefono(formatoArchivoRecepcion.getTelefono());
					solicitudORO.setMail(formatoArchivoRecepcion.getMail());
					solicitudORO.setNumCuentaNacional(formatoArchivoRecepcion.getNumCuentaNacional());
					solicitudORO.setNumRetencionCap(formatoArchivoRecepcion.getNumRetencionCap());
					solicitudORO.setHoraBloqueoCap(formatoArchivoRecepcion.getHoraBloqueoCap());
					solicitudORO.setMontoOperacionVEF(formatoArchivoRecepcion.getMontoOperacionVEF());
					solicitudORO.setDivisaMonNacional(formatoArchivoRecepcion.getDivisaMonNacional());
					solicitudORO.setNumRetencionCom(formatoArchivoRecepcion.getNumRetencionCom());
					solicitudORO.setMontoComision(formatoArchivoRecepcion.getMontoComision());
					solicitudORO.setPorcentajeComision(formatoArchivoRecepcion.getPorcentajeComision());
					solicitudORO.setFechaOperacion(Utilitario.StringToDate(formatoArchivoRecepcion.getFechaOperacion(), ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2));
					solicitudORO.setNumCuentaMonedaExtranjera(formatoArchivoRecepcion.getNumCuentaMonedaExtranjera());
					solicitudORO.setMontoOperacionUSD(formatoArchivoRecepcion.getMontoOperacionUSD());
					solicitudORO.setDivisaMonExtranjera(formatoArchivoRecepcion.getDivisaMonExtranjera());
					solicitudORO.setTasaCambio(formatoArchivoRecepcion.getTasaCambio());
					solicitudORO.setFechaValorOperacion(Utilitario.StringToDate(formatoArchivoRecepcion.getFechaValorOperacion(), ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2));
					solicitudORO.setTipoOperacion(formatoArchivoRecepcion.getTipoOperacion());
					//Se agregan los campos de comision IGTF y monto total de Retencion  NM26659_14/06/2017 
					//System.out.println("******************** procesarArchivo ********************");
					solicitudORO.setPtcComisionIGTF(formatoArchivoRecepcion.getPtcComisionIGTF());
					solicitudORO.setMontoComisionIGTF(formatoArchivoRecepcion.getMontoComisionIGTF());
					solicitudORO.setMontoTotalRetencion(formatoArchivoRecepcion.getMontoTotalRetencion());
					solicitudORO.setTipoSolicitud(tipoSolicitudNumeric);
					solicitudORO.setProcesoTipo(EstructuraArchivoOperacionesORO.PROCESO_VERIFICACION_ORO.getValor());
					// PROCESAR DETALLE
					procesarDetalle(configSubastaORO, solicitudORO); // GUARDADO EN BASE DE DATOS

				}
				linea = archivoRecepcion.leerLinea();
			}

		}

	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void procesarCabecera(FormatoOperacionesORO formatoRecepcion) throws Exception {
		if(!formatoRecepcion.getTipoRegistro().equals("01")){
			throw new Exception("El primer elemento del archivo no corresponde con el registro de cabecera, por lo tanto no es posible continuar con el proceso");
		}
		System.out.println(formatoRecepcion.getIdSubasta());
		System.out.println(getJornadaORO());
		if(!formatoRecepcion.getIdSubasta().equals(getJornadaORO())){				
			throw new Exception("El numero de jornada asociado al archivo recibido no corresponde con la Jornada configurada en el sistema");	
		}
		
		idCiclo = Integer.valueOf(formatoRecepcion.getIdCiclo());
		// VERIFICA SI EL CICLO ESTA ABIERTO, DE NO SER ASÍ NO SE PROCESA EL ARCHIVO	
		System.out.println("Numero ciclo en archivo " + idCiclo);
		System.out.println("Numero ciclo en abierto " + getIdCicloActivo());
		if (!jornadaManual &&(getIdCicloActivo()!=idCiclo)) {
			throw new Exception("El ciclo correspondiente al número " + idCiclo + " no se encuentra abierto, por lo tanto no es posible continuar con el proceso");
		}					
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	protected void procesarDetalle(ConfigSubastaORO configSubastaORO, SolicitudORO solicitudORO) throws Exception {
		// CARGAR REGISTRO DE RETENCION
		solicitudesORODAO.procesarSolicitudORO(configSubastaORO, solicitudORO);
		//solicitudesDICOMDAO.procesarSolicitudORO(configSubastaORO, solicitudORO);
		// querysEjecutar.add(archivoAnulacionConv36DAO.insertarOperacionesVencidas(registroAnulacion));
	}

	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Obtiene la carpeta de respaldo
	 */

	protected String getCarpetaRespaldo(String tipoSolicitud) {
		String carpeta = null;
		if (tipoSolicitud.equalsIgnoreCase(ConstantesGenerales.SOLICITUD_DEMANDA)) {
			carpeta = getCarpetaDemandaRespaldo();
			if (!carpeta.endsWith(File.separator)) {
				carpeta = carpeta.concat(File.separator);
			}
		} else if (tipoSolicitud.equalsIgnoreCase(ConstantesGenerales.SOLICITUD_OFERTA)) {
			carpeta = getCarpetaOfertaRespaldo();
			if (!carpeta.endsWith(File.separator)) {
				carpeta = carpeta.concat(File.separator);
			}
		}
		return carpeta;
	}

	protected String getCarpetaOfertaRespaldo() {
		String carpeta = parametrosRecepcionORO.get(ParametrosSistema.RUTA_ORO_OFERTA_RESP);
		if (!carpeta.endsWith(File.separator)) {
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}

	protected String getCarpetaDemandaRespaldo() {
		String carpeta = parametrosRecepcionORO.get(ParametrosSistema.RUTA_DICOM_DEMANDA_RESP);
		System.out.println("carpeta---> "+carpeta);
		if (!carpeta.endsWith(File.separator)) {
			System.out.println("!carpeta.endsWith(File.separator)");
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}

	protected void respaldarArchivo(String tipoSolicitud, File archivo, boolean borrarArchivo) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("carpetaRespaldo: " + getCarpetaRespaldo());
			System.out.println("carpetaRespaldo: " + getCarpetaRespaldo());
		}
		logger.info("PROCESO DE RESPALDO DE ACHIVO ----------->");
		final String carpeta = getCarpetaRespaldo(tipoSolicitud);
		System.out.println("carpeta---> " + carpeta);
		final File carpetaRespaldo = new File(carpeta);
		carpetaRespaldo.mkdirs();
		System.out.println("carpetaRespaldo--->" +carpetaRespaldo);

		// agregar fecha/hora al nombre del archivo respaldo
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(archivo.getName().substring(0, archivo.getName().length() - 4)); // nombre
		
		

		stringBuilder.append(sdfArchivoRespaldo.format(new Date()));
		stringBuilder.append(archivo.getName().substring(archivo.getName().length() - 4)); // extension

		File destino = new File(carpeta.concat(stringBuilder.toString()));
		System.out.println("destino--->" +destino);
		logger.info("Respaldar: " + archivo.getAbsolutePath() + " a: " + destino.getAbsolutePath());

		FileUtil.copiarArchivo(archivo, destino);
		System.out.println("copiarArchivo--->" +archivo+" "+destino);
		// if (archivo.renameTo(destino)) {
		if (borrarArchivo) {
			archivo.delete();
		}
		// }
	}

	/**
	 * Método que maneja la ejeción por lotes de los querys del proceso de liquidación
	 * 
	 * @author nm25287
	 * @throws SQLException
	 */
	/*
	 * protected void procesarQuerysPorLote(ArrayList<String> sentencias) throws SQLException{ //EJECUCIÓN DE QUERYS ++cantidadTotalOrdenes; ++cantidadLoteOrdenesProcesadas; if (ConstantesGenerales.COMMIT_REGISTROS_LIQ == cantidadLoteOrdenesProcesadas) { cantidadTotalOrdenesProcesadas = cantidadTotalOrdenesProcesadas + cantidadLoteOrdenesProcesadas;
	 * ordenDAO.ejecutarStatementsBatchBool(sentencias); sentencias.clear(); logger.info( "Ordenes enviadas por COMMIT en proceso de CONCILIACION DE RETENCIONES : " + cantidadLoteOrdenesProcesadas); cantidadLoteOrdenesProcesadas = 0; } //Logger.info(this, "Realizacion de commit al numero de registro N° " + cantidadTotalOrdenesProcesadas); }
	 */

	protected void procesarQuerysPorLote(ArrayList<String> sentencias) throws SQLException {
		// EJECUCIÓN DE QUERYS
		// archivoAnulacionConv36DAO.ejecutarStatementsBatchBool(sentencias);
		sentencias.clear();
	}

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
		// TODO Buscar especificacion de ruta para enviar archivo via FTP
		// FTPUtil ftpUtil = new FTPUtil(parametrosRecepcionDICOM.get(TransaccionNegocio.PROC_RECEPCION_DICOM), this._dso);
		// ftpUtil.putFTPAscci(archivoOriginal, archivoFinal, "", false);
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public Proceso comenzarProceso(int usuarioId, String... tipoProceso) throws Exception {
		Proceso proceso = new Proceso();
		String procesoEjecucion = new String();
		StringBuffer procesoValidacion = new StringBuffer();
		procesoDAO = new ProcesosDAO(_dso);
		
		if (tipoProceso.length > 0) {
			procesoEjecucion = tipoProceso[0];// El primer parametro que se pasa es del proceso que se esta ejecutando
			int count = 0;
			for (String element : tipoProceso) {
				if (count > 0) {
					procesoValidacion.append(",");
				}
				procesoValidacion.append("'" + element + "'");
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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected boolean verificarCicloAbierto(String cicloAbierto) throws Exception {
		boolean puedeEjecutar = false;
	
		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		//UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
		
		controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(cicloAbierto);
		DataSet dataset = controlArchivoDAO.getDataSet();

		if (dataset.count() > 0) {//Verificacion si existe un Ciclo de envios abierto
			if(dataset.count() == 1){
				puedeEjecutar=true;
				dataset.first();
				dataset.next();
				if(dataset.getValue("EJECUCION_ID")!=null){
					setIdCicloActivo(Long.parseLong(dataset.getValue("EJECUCION_ID")));	
				}	
			}else{
				logger.info("Existe más de un ciclo abierto de la transaccion " + cicloAbierto);
			}
		}else {
			logger.info("No existe un ciclo abierto de tipo " + cicloAbierto);
		}
				
		return puedeEjecutar;
	}
	
	
	protected void cerrarCiclo() throws ParseException, Exception {
		if(idCicloActivo>0&&!jornadaManual){
			solicitudesORODAO.listarSolicitudesVerificadasORO(getJornadaORO(),"NRO_SOLICITUD",0);
			if(solicitudesORODAO.getDataSet().count()>0){
				logger.info("No se han procesado todas las operaciones de la Jornada: "+ getJornadaORO() +" El ciclo aun no puede ser Finalizado de forma automatica");
			}else {
				controlArchivoDAO.cerrarCiclo((int)getIdCicloActivo());
				logger.info("Se cerró el ciclo: " + getIdCicloActivo());
			}
		}else{
			logger.info("No se ejecuta cierre de ciclo: " + getIdCicloActivo()+", jornadaManual:"+jornadaManual);
		}
	}

}
