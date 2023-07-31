package com.bdv.infi.logic.interfaz_operaciones_DICOM;

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
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.ConfigSubastaDICOM;
import com.bdv.infi.data.FormatoOperacionesDICOM;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.ArchivoRecepcion;
import com.bdv.infi.logic.interfaz_ops.BatchOps;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.data.SolicitudDICOM;

public class RecepcionOperacionesVerificadasDICOM extends BatchOps implements Runnable {

	public RecepcionOperacionesVerificadasDICOM(DataSource datasource) {
		super();
		_dso = datasource;
	}
	
	static final Logger logger = Logger.getLogger(RecepcionOperacionesVerificadasDICOM.class);
	protected HashMap<String, String> parametrosRecepcionDICOM;
	protected String procesoEjecucion;
	protected String tipoCiclo;
	protected String tipoProducto;
	ArrayList<String> querysEjecutar = new ArrayList<String>();
	protected long idCiclo = 0;
	FormatoOperacionesDICOM formatoArchivoRecepcion = new FormatoOperacionesDICOM();
	UnidadInversionDAO uiDAO = null;
	protected String transaccionEjecutar;
	boolean existeArchivo = false;
	private String TRAN="";
	private long idUnidadInv = 0;
	SolicitudesDICOMDAO solicitudesDICOMDAO = null;
	VehiculoDAO vehiculoDAO = null;
	BlotterDAO blotterDAO = null;
	TransaccionFijaDAO transaccionFijaDAO = null;	
	private String empresaID;
	private String tipoSolicitud = null;// Indicador de Oferta (O) o Demanda (D)
	private int tipoSolicitudNumeric;
	private String vehiculoBDV = null;
	private String blotterId;
	private SolicitudDICOM solicitudDICOM;
	String monedaDicom_siglas="";
	private ConfigSubastaDICOM configSubastaDICOM;
	com.bdv.infi.data.TransaccionFija transaccionFija = null;
	int monedaInt;
	int constatemoneda;
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
			//VALIDACION DE EJECUCION MANUAL
			monedaSubasta=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
			monedaInt= Integer.parseInt(monedaSubasta.trim());
			constatemoneda= Integer.parseInt(ConstantesGenerales.CODIGO_MONEDA_ISO_USD_DICOM);
			monedaDicom_siglas=parametrosRecepcionDICOM.get(monedaSubasta);
			TRAN = getTransaccionNegocio();			
			jornadaManual=parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_RECEPCION_MANUAL)=="1"?true:false;		
			cicloAbierto=verificarCicloAbierto("'"+TRAN+"'");
			
			if(jornadaManual||cicloAbierto){
				
				File archivo = getArchivoDicomOfertas();// Busqueda Archivo Ofertas
				
					if (archivo.exists()) {// Recepcion Archivo Ofertas
						
						existeArchivo = true;
						transaccionEjecutar= getTranssacion();
						tipoSolicitud = ConstantesGenerales.SOLICITUD_OFERTA;
						
					}else{// Recepcion Archivo Demandas
						
					archivo = getArchivoDicomDemandas(); // Busqueda Archivo Demandas
					
						if (archivo.exists()) {
							
							existeArchivo = true;
							transaccionEjecutar = getTranssacion();
							tipoSolicitud = ConstantesGenerales.SOLICITUD_DEMANDA;
							
						}
					}
	
			logger.info("********* Iniciando el proceso de recepción de archivo batch DICOM - " + transaccionEjecutar + "  *********");
			logger.info("archivo.getAbsolutePath(): " + archivo.getAbsolutePath());
				
				if (existeArchivo) {
					
					procesoEnEjecucion = comenzarProceso(1, transaccionEjecutar);
					
				if (getProceso() != null) {
					
					recepcionArchivoOperacionesDicomVerificadas(tipoSolicitud, archivo);
					//Verificacion para el proceso de Cierre de Ciclo
					cerrarCiclo();
				}
				
				} else {
					logger.info(" *********************** El archivo no existe ***********************");
				}
			
			}else {
				logger.info(" *********************** No existe un CICLO " +TRAN+ " abierto para su procesamiento ***********************");
			}
			
		} catch (Exception ex) {
			
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
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected File getArchivoDicomOfertas() {
		
		String RutaArchivoDicomOfertas=parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_OFERTA_RECEP);
		String RutaArchivoDicomOfertasC=null;
		
			if(monedaInt==constatemoneda){
			RutaArchivoDicomOfertasC=RutaArchivoDicomOfertas;
			}else{
			RutaArchivoDicomOfertasC=RutaArchivoDicomOfertas.replaceAll(".",monedaDicom_siglas+".");
			}
			
		return new File (RutaArchivoDicomOfertasC);
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected File getArchivoDicomDemandas() {
		
		String RutaArchivoDicomDemandas=parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_DEMANDA_RECEP);
		String RutaArchivoDicomDemandasC=null;
		
			if(monedaInt==constatemoneda){
			RutaArchivoDicomDemandasC=RutaArchivoDicomDemandas;
			}else{
			RutaArchivoDicomDemandasC=RutaArchivoDicomDemandas.replaceAll(".",monedaDicom_siglas+".");
			}
		
		return new File(RutaArchivoDicomDemandasC);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected String getTranssacion() {
		File archivo1 = getArchivoDicomOfertas();
		
			if (archivo1.exists()) {
				
				if(monedaInt==constatemoneda){
					transaccionEjecutar= TransaccionNegocio.PROC_RECEPCION_OFERTA_DICOM;
				}else{
					transaccionEjecutar=TransaccionNegocio.PROC_RECEPCION_OFERTA_DICOM+parametrosRecepcionDICOM.get(monedaSubasta);
				}
				
			}else{
				if(monedaInt==constatemoneda){
					transaccionEjecutar= TransaccionNegocio.PROC_RECEPCION_DEMANDA_DICOM;
				}else{
					transaccionEjecutar=TransaccionNegocio.PROC_RECEPCION_DEMANDA_DICOM+monedaDicom_siglas;
				}
			}
		
		return transaccionEjecutar;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected String getTransaccionNegocio(){
		String transsaciones="";
		
			if(monedaInt==constatemoneda){
				transsaciones = TransaccionNegocio.CICLO_BATCH_DICOM;
			}else{
				transsaciones =TransaccionNegocio.CICLO_BATCH_DICOM+monedaDicom_siglas;
			}
		
		return transsaciones;	
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	protected String getJornadaDicom() {
		return parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_DICOM);// , ParametrosSistema.NOMBRE_ANUL_CONVENIO_36);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected File getArchivo(String ruta, String archivo) {
		String carpeta = parametrosRecepcionDICOM.get(ruta);
		
		return new File(carpeta);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected File getArchivo(String ruta) {
		String carpeta = parametrosRecepcionDICOM.get(ruta);
		
		return new File(carpeta);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Metodo de busqueda de parametros asociados a interfaz con Moneda Extranjera
	 * */
	@Override
	protected void obtenerParametros() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		// TTS-537 Busqueda de parametros de transacciones a moneda extranjera para cancelacion de operaciones vencidas de Convenio 36 NM26659 28-12-2016
		parametrosRecepcionDICOM = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void recepcionArchivoOperacionesDicomVerificadas(String tipoSolicitud, File archivo) throws FileNotFoundException, IOException, Exception {
		long unInvId = 0;
		
			if (!archivo.exists()) {
				throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			} else {
			// Lee Archivo de Moneda Extranjera y guarda en BD
				configBaseDicom();
				procesarArchivo(configSubastaDICOM, archivo);
				respaldarArchivo(tipoSolicitud, archivo, true);			
			/*
			 * AnulacionOperacionesVencidasConv36Logic anulacionOperacionesVencidasConv36Logic=new AnulacionOperacionesVencidasConv36Logic(AnulacionesConv36.MONEDA_EXTRANJERA_OP_SIN_PROCESAR.getValor(),_dso); anulacionOperacionesVencidasConv36Logic.run();
			 */
			}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void configBaseDicom() throws Exception {

		solicitudesDICOMDAO = new SolicitudesDICOMDAO(_dso);
		vehiculoDAO = new VehiculoDAO(_dso);
		blotterDAO = new BlotterDAO(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		controlArchivoDAO = new ControlArchivoDAO(_dso);
		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		String usuario = parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES, _dso);
		
		InetAddress direccion = InetAddress.getLocalHost();
		String direccionIpstr = direccion.getHostAddress();
		
		long idUnidadInv = 0;
		configSubastaDICOM = new ConfigSubastaDICOM();
		idUnidadInv = solicitudesDICOMDAO.creacionUnidadInvDICOM(tipoSolicitud, getJornadaDicom(), usuario,direccionIpstr,ConstantesGenerales.PROCESO_VERIFICACION_DICOM);
		configSubastaDICOM.setIdUnidadInv(idUnidadInv);
		configSubastaDICOM.setNroJornada(getJornadaDicom());
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
			
		configSubastaDICOM.setEmpresId(empresaID);
		configSubastaDICOM.setVehiculoBDVId(vehiculoBDV);
		configSubastaDICOM.setBlotterId(blotterId);

		transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.CAPITAL_SIN_IDB);
		configSubastaDICOM.setCodOperacionBloCap(transaccionFija.getCodigoOperacionCteBlo());
		configSubastaDICOM.setCodOperacionDebCap(transaccionFija.getCodigoOperacionCteDeb());
		configSubastaDICOM.setCodOperacionCreCap(transaccionFija.getCodigoOperacionCteCre());

		transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.COMISION_DEB);
		configSubastaDICOM.setCodOperacionBloCom(transaccionFija.getCodigoOperacionCteBlo());
		configSubastaDICOM.setCodOperacionDebCom(transaccionFija.getCodigoOperacionCteDeb());

		transaccionFija = transaccionFijaDAO.obtenerTransaccionDicom(idUnidadInv, vehiculoBDV, TransaccionFija.TOMA_ORDEN_CTA_DOLARES);
		configSubastaDICOM.setCodOperacionCreConv20(transaccionFija.getCodigoOperacionCteCre());

		configSubastaDICOM.setUsername(usuario);
		configSubastaDICOM.setIp(direccionIpstr);
		configSubastaDICOM.setEjecucionId(String.valueOf(procesoEnEjecucion.getEjecucionId()));

	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void procesarArchivo(ConfigSubastaDICOM configSubastaDICOM, File archivo) throws Exception {
		
		// PROCESAR CABECERA
		ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);
		String linea = null;
		linea = archivoRecepcion.leerLinea();//TODO VERIFICAR EL USO DE CABECERA
		formatoArchivoRecepcion.cargarCabeceraMainframe(linea);//TODO VERIFICAR EL USO DE CABECERA 
		System.out.println("********** Cab -> " + linea + " ********** ");

		// VALIDAR CICLO
		procesarCabecera(formatoArchivoRecepcion);//TODO VERIFICAR EL USO DE CABECERA
		
		linea = archivoRecepcion.leerLinea();
			if (linea != null) {
			// if (formatoArchivoRecepcion.esValidoFormatoRecepcion()) {//TODO VERIFICAR FUNCIONALIDAD
				System.out.println("********** Arch_fecha " + new java.util.Date() + " Jor --> " + configSubastaDICOM.getNroJornada()+ " **********");
				while (linea != null) {

					if (linea.length() > 0) {
						System.out.println("linea: " + linea);
						formatoArchivoRecepcion.cargarCuerpoOpVerificadas(linea);
						solicitudDICOM = new SolicitudDICOM(); // formatoArchivoRecepcion
						solicitudDICOM.setIdUnidadInversion(configSubastaDICOM.getIdUnidadInv());
						System.out.println(configSubastaDICOM.getIdUnidadInv());
						solicitudDICOM.setCodigoRespuesta(formatoArchivoRecepcion.getCodigoRespuestaInt());
						System.out.println(formatoArchivoRecepcion.getCodigoRespuestaInt());
						solicitudDICOM.setIdOperacion(formatoArchivoRecepcion.getIdOperacion());
						System.out.println(formatoArchivoRecepcion.getIdOperacion());
						solicitudDICOM.setTipoCliente(formatoArchivoRecepcion.getTipoCliente());
						System.out.println(formatoArchivoRecepcion.getTipoCliente());
						solicitudDICOM.setCedRifCliente(Long.parseLong(formatoArchivoRecepcion.getCedRifCliente()));
						System.out.println(formatoArchivoRecepcion.getCedRifCliente());
						solicitudDICOM.setNombreCliente(formatoArchivoRecepcion.getNombreCliente());
						System.out.println(formatoArchivoRecepcion.getNombreCliente());
						solicitudDICOM.setTelefono(formatoArchivoRecepcion.getTelefono());
						System.out.println(formatoArchivoRecepcion.getTelefono());
						solicitudDICOM.setMail(formatoArchivoRecepcion.getMail());
						System.out.println(formatoArchivoRecepcion.getMail());
						solicitudDICOM.setNumCuentaNacional(formatoArchivoRecepcion.getNumCuentaNacional());
						System.out.println(formatoArchivoRecepcion.getNumCuentaNacional());
						solicitudDICOM.setNumRetencionCap(formatoArchivoRecepcion.getNumRetencionCap());
						System.out.println(formatoArchivoRecepcion.getNumRetencionCap());
						solicitudDICOM.setHoraBloqueoCap(formatoArchivoRecepcion.getHoraBloqueoCap());
						System.out.println(formatoArchivoRecepcion.getHoraBloqueoCap());
						solicitudDICOM.setMontoOperacionVEF(formatoArchivoRecepcion.getMontoOperacionVEF());
						System.out.println(formatoArchivoRecepcion.getMontoOperacionVEF());
						solicitudDICOM.setDivisaMonNacional(formatoArchivoRecepcion.getDivisaMonNacional());
						System.out.println(formatoArchivoRecepcion.getDivisaMonNacional());
						solicitudDICOM.setNumRetencionCom(formatoArchivoRecepcion.getNumRetencionCom());
						System.out.println(formatoArchivoRecepcion.getNumRetencionCom());
						solicitudDICOM.setMontoComision(formatoArchivoRecepcion.getMontoComision());
						System.out.println(formatoArchivoRecepcion.getMontoComision());
						solicitudDICOM.setPorcentajeComision(formatoArchivoRecepcion.getPorcentajeComision());
						System.out.println(formatoArchivoRecepcion.getPorcentajeComision());
						solicitudDICOM.setFechaOperacion(Utilitario.StringToDate(formatoArchivoRecepcion.getFechaOperacion(), ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2));
						System.out.println(Utilitario.StringToDate(formatoArchivoRecepcion.getFechaOperacion(), ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2));
						solicitudDICOM.setNumCuentaMonedaExtranjera(formatoArchivoRecepcion.getNumCuentaMonedaExtranjera());
						System.out.println(formatoArchivoRecepcion.getNumCuentaMonedaExtranjera());
						solicitudDICOM.setMontoOperacionUSD(formatoArchivoRecepcion.getMontoOperacionUSD());
						System.out.println(formatoArchivoRecepcion.getMontoOperacionUSD());
						solicitudDICOM.setDivisaMonExtranjera(formatoArchivoRecepcion.getDivisaMonExtranjera());
						System.out.println(formatoArchivoRecepcion.getDivisaMonExtranjera());
						solicitudDICOM.setTasaCambio(formatoArchivoRecepcion.getTasaCambio());
						System.out.println(formatoArchivoRecepcion.getTasaCambio());
						solicitudDICOM.setFechaValorOperacion(Utilitario.StringToDate(formatoArchivoRecepcion.getFechaValorOperacion(), ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2));
						System.out.println(Utilitario.StringToDate(formatoArchivoRecepcion.getFechaValorOperacion(), ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2));
						solicitudDICOM.setTipoOperacion(formatoArchivoRecepcion.getTipoOperacion());
						System.out.println(formatoArchivoRecepcion.getTipoOperacion());
						solicitudDICOM.setPtcComisionIGTF(formatoArchivoRecepcion.getPtcComisionIGTF());
						System.out.println(formatoArchivoRecepcion.getPtcComisionIGTF());
						solicitudDICOM.setMontoComisionIGTF(formatoArchivoRecepcion.getMontoComisionIGTF());
						System.out.println(formatoArchivoRecepcion.getMontoComisionIGTF());
						solicitudDICOM.setMontoTotalRetencion(formatoArchivoRecepcion.getMontoTotalRetencion());
						System.out.println(formatoArchivoRecepcion.getMontoTotalRetencion());
						solicitudDICOM.setTipoSolicitud(tipoSolicitudNumeric);
						System.out.println(tipoSolicitudNumeric);
						solicitudDICOM.setProcesoTipo(EstructuraArchivoOperacionesDICOM.PROCESO_VERIFICACION_DICOM.getValor());
						System.out.println(EstructuraArchivoOperacionesDICOM.PROCESO_VERIFICACION_DICOM.getValor());
					// PROCESAR DETALLE
					procesarDetalle(configSubastaDICOM, solicitudDICOM); // GUARDADO EN BASE DE DATOS

					}
				linea = archivoRecepcion.leerLinea();
				}

			}

	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void procesarCabecera(FormatoOperacionesDICOM formatoRecepcion) throws Exception {
		
		if(!formatoRecepcion.getTipoRegistro().equals("01")){
			throw new Exception("El primer elemento del archivo no corresponde con el registro de cabecera, por lo tanto no es posible continuar con el proceso");
		}
		
		if(!formatoRecepcion.getIdSubasta().equals(getJornadaDicom())){				
			throw new Exception("El numero de jornada asociado al archivo recibido no corresponde con la Jornada configurada en el sistema");	
		}
		
		idCiclo = Integer.valueOf(formatoRecepcion.getIdCiclo());
		// VERIFICA SI EL CICLO ESTA ABIERTO, DE NO SER ASÍ NO SE PROCESA EL ARCHIVO
		if (!jornadaManual &&(getIdCicloActivo()!=idCiclo)) {
			throw new Exception("El ciclo correspondiente al número " + idCiclo + " no se encuentra abierto, por lo tanto no es posible continuar con el proceso");
		}					
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void procesarDetalle(ConfigSubastaDICOM configSubastaDICOM, SolicitudDICOM solicitudDICOM) throws Exception {
		// CARGAR REGISTRO DE RETENCION
		solicitudesDICOMDAO.procesarSolicitudDICOM(configSubastaDICOM, solicitudDICOM);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected String getCarpetaOfertaRespaldo() {
		String carpeta = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_OFERTA_RESP);
		
			if (!carpeta.endsWith(File.separator)) {
				carpeta = carpeta.concat(File.separator);
			}
		return carpeta;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected String getCarpetaDemandaRespaldo() {
		String carpeta = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_DEMANDA_RESP);
			
		if (!carpeta.endsWith(File.separator)) {
				carpeta = carpeta.concat(File.separator);
			}
		return carpeta;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void respaldarArchivo(String tipoSolicitud, File archivo, boolean borrarArchivo) throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug("carpetaRespaldo: " + getCarpetaRespaldo());
		}
		logger.info("PROCESO DE RESPALDO DE ACHIVO ----------->");
		final String carpeta = getCarpetaRespaldo(tipoSolicitud);

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
		
		if (borrarArchivo) {
			archivo.delete();
		}
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
			solicitudesDICOMDAO.listarSolicitudesVerificadasDICOM(getJornadaDicom(),"NRO_SOLICITUD",monedaSubasta,0);
			if(solicitudesDICOMDAO.getDataSet().count()>0){
				logger.info("No se han procesado todas las operaciones de la Jornada: "+ getJornadaDicom() +" El ciclo aun no puede ser Finalizado de forma automatica");
			}else {
				controlArchivoDAO.cerrarCiclo((int)getIdCicloActivo());
				logger.info("Se cerró el ciclo: " + getIdCicloActivo());
			}
		}else{
			logger.info("No se ejecuta cierre de ciclo: " + getIdCicloActivo()+", jornadaManual:"+jornadaManual);
		}
	}

}
