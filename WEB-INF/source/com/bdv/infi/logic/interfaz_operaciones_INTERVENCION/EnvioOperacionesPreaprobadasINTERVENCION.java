package com.bdv.infi.logic.interfaz_operaciones_INTERVENCION;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import models.bcv.menudeo_envio.ErroresMenudeo;
import org.apache.axis.AxisFault;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.bcv.service.AutorizacionPortBindingStub;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.BatchOps;
import com.bdv.infi.util.Utilitario;
import criptografia.TripleDes;
import electric.xml.Node;

public class EnvioOperacionesPreaprobadasINTERVENCION extends BatchOps implements Runnable {

	public EnvioOperacionesPreaprobadasINTERVENCION(DataSource datasource, String usuarioGenerico) throws Exception {
		super();
		_dso = datasource;
		controlArchivoDAO = new ControlArchivoDAO(_dso);
		this.usuario = usuarioGenerico;
	}

	// menudeo nm36635
	protected HashMap<String, String> parametrosMenudeBCV;
	String tasaWsBancoCentral;
	String codigoprueba;
	String comprausd;
	String ventausd;
	String compraeur;
	String ventaeur;
	String tasa_cambio_ws = "";
	String tasa_cambio_wss = "";
	double monto_trans;
	BigDecimal monto_tasa_division;
	HashMap<String, String> parametros;
	private ProcesosDAO procesosDAO;
	AutorizacionPortBindingStub stub;
	Propiedades propiedades;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	OrdenDAO ordenDAO = null;
	SolicitudesSitmeDAO sitmeDAO = null;
	CredencialesDAO credencialesDAO = null;
	DataSet _credenciales = new DataSet();
	DataSet _ordenes = new DataSet();
	DataSet _cliente = new DataSet();
	ClienteDAO clienteDAO = null;
	long idUnidad;
	int numeroDePagina;
	int pageSize;
	boolean todos;
	String idOrdenes;
	String statusP;
	String statusE;
	String Tipo;
	String urlInvocacion;
	String rutaCustodio1;
	String rutaCustodio2;
	DataSource _dso;
	String tipoTransaccion = TransaccionNegocio.WS_BCV_MENUDEO;
	int idUsuario;
	String fecha;
	Integer clienteID;
	OrdenesCrucesDAO ordenesCrucesDAO;
	private BigDecimal monto_trans_big_decimal;
	static final Logger logger = Logger.getLogger(EnvioOperacionesPreaprobadasINTERVENCION.class);
	protected HashMap<String, String> parametrosMENUDEO;
	ArrayList<String> querysEjecutar = new ArrayList<String>();
	protected String transaccionEjecutar;
	Proceso procesoEnEjecucion;
	String reenvioArchPreaprobadasDicom = "";
	String MonedaDicom_Siglas = "";
	String usuario = "";

	public EnvioOperacionesPreaprobadasINTERVENCION(String idOrdenes, boolean todos, String statusP, DataSource _dso, int idUsuario, String fecha, String statusE, String Tipo) {
		this.idOrdenes = idOrdenes;
		this.statusP = statusP;
		this.statusE = statusE;
		this.Tipo = Tipo;
		this.todos = todos;
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.credencialesDAO = new CredencialesDAO(_dso);
	}

	public void run() {

		try {

			// VERIFICAR CICLO
			if (verificarCiclo(tipoTransaccion)) {
				logger.info("EnvioOperacionesMENUDEO-> Se validaran parametros de entrada para MENUDEO");
				transaccionEjecutar = getTransaccionEjecutar();
				iniciarProceso();
				if (getProceso() != null) {
					try {
						logger.info("EnvioOperacionesMENUDEO-> Antes de enviar las operaciones a bcv ");
						enviarOperacionesBCV();
						logger.info("EnvioOperacionesMENUDEO-> Despues de enviar las operaciones a bcv");
						finalizarProceso();
						logger.info("EnvioOperacionesMENUDEO-> despues de finalizar proceso");
					} catch (Exception e1) {
						logger.error("EnvioOperacionesMENUDEO-> " + e1.getMessage());
						e1.printStackTrace();
					}
				} else {
					logger.info("EnvioOperacionesMENUDEO-> Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
					System.out.println("EnvioOperacionesMENUDEO-> Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
				}
			} else {
				logger.info("EnvioOperacionesMENUDEO-> Ya se encuentra registrado un ciclo en ejecución");
				System.out.println("EnvioOperacionesMENUDEO-> Ya se encuentra registrado un ciclo en ejecución");
			}
		} catch (Exception ex) {

			ex.printStackTrace();
			logger.error("Error en el proceso de envio menudeo a bcv. ", ex);
			logger.info("Exception ex ---> " + ex.getMessage());
			if (proceso != null) {
				proceso.agregarDescripcionErrorTrunc(ex.getMessage(), true);
			}
		} finally {
			terminarProceso();
			System.out.println("Terminado el proceso de envio operaciones menudeo... ");
			logger.info("Terminado el proceso de envio operaciones menudeo... ");
		}
	}

	/**
	 * Metodo de busqueda de parametros asociados a interfaz con Moneda Extranjera
	 * */
	@Override
	protected void obtenerParametros() throws Exception {
		super.obtenerParametros();
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosMENUDEO = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);

	}

	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Obtiene la carpeta de respaldo
	 */

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

	protected String getFechaHora() throws ParseException {
		return Utilitario.DateToString(new Date(), "ddMMyyyyhhmmss");
	}

	protected String getTransaccionNegocio() {

		return TransaccionNegocio.CICLO_BATCH_MENUDEO;
	}

	protected String getTransaccionEjecutar() {

		return TransaccionNegocio.BATCH_MENUDEO_ENVIO;
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected void iniciarProceso() throws Exception {
		logger.info("INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(tipoTransaccion);
		proceso.setUsuarioId(this.idUsuario);
		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}

	/**
	 * metodo para finalizar los proceso y asignar la fecha de cierre
	 * 
	 * @throws Exception
	 */
	private void finalizarProceso() throws Exception {
		String queryProcesoCerrar = procesosDAO.modificar(proceso);
		db.exec(_dso, queryProcesoCerrar);
		logger.info("FIN DE PROCESO: " + new Date());
	}

	private void enviarOperacionesBCV() {
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			obtenerParametros(dso);
			logger.info("SE INICIA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.MENUDEO);
			_credenciales = credencialesDAO.getDataSet();
			this.propiedades = Propiedades.cargar();
			String userName = "";
			String clave = "";
			if (_credenciales.next()) {

				// SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
				if (propiedades.getProperty("use_https_proxy").equals("1")) {
					Utilitario.configurarProxy();
				}
				rutaCustodia();
				TripleDes desc = new TripleDes();
				userName = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("CLAVE"));
			} else {
				logger.error("Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: " + tipoTransaccion);
				throw new org.bcv.service.Exception();
			}

			serviciosMenudeoBancoCentral(ConstantesGenerales.END_POINT_BCV_MENUDEO);
			leerServicioTasaBCV();
			Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null) {
				headers = new Hashtable();
				stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
			}
			headers.put("Username", userName);
			headers.put("Password", clave);

			// SE DECLARAN VARIABLES PARA EL BUCLE
			String ordeneID;
			String cedRif;
			String tipperID;
			String codigoOperacion;
			String codigoCliente;
			String nombreCliente;
			String telefono;
			String email;
			BigDecimal montoBase = new BigDecimal(0);
			BigDecimal tasaCambio;
			BigDecimal montoTransaccion = new BigDecimal(0);
			String estatusEnvio = "";
			String estatusRechazada = ConstantesGenerales.ESTATUS_RECHAZADAS;
			String conceptoEstadistica;
			String ctaConvenio20;
			String tipoOperacion;
			String codMonedaIso;//
			String tipoMovimiento = "";
			String tipo_persona_jur = "J";
			String tipo_persona_ex = "E";
			String tipo_persona_natural = "V";
			String tipo_persona_pasaporte = "P";
			String tipo_persona_gobierno = "G";
			String codigoEstadisticaParaCombustible = "100";
			int movimiento;
			String ordenBCV;
			String Codigo = ConstantesGenerales.TIPO_MOVIMIENTOS_MENUDEO_V;

			ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenDAO = new OrdenDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false, true, 0, 0, todos, statusP, fecha, statusE, Tipo, idOrdenes, clienteID,true,"0");
			_ordenes = ordenesCrucesDAO.getDataSet();
			if (_ordenes.count() == 0) {
				proceso.agregarDescripcionErrorTrunc("No tienes operaciones para enviar.", true);
			}
			while (_ordenes.next()) {
				movimiento = 1;

				cedRif = _ordenes.getValue("CED_RIF");
				ordeneID = _ordenes.getValue("ID_OPER");
				tipperID = _ordenes.getValue("NACIONALIDAD");
				codigoCliente = _ordenes.getValue("NACIONALIDAD") + cedRif;
				nombreCliente = _ordenes.getValue("NOM_CLIEN");
				telefono = _ordenes.getValue("TEL_CLIEN");
				email = _ordenes.getValue("EMAIL_CLIEN");
				ctaConvenio20 = _ordenes.getValue("CTA_CLIEN");
				codMonedaIso = _ordenes.getValue("COD_DIVISAS");
				tipoOperacion = _ordenes.getValue("movimiento");
				codigoOperacion = _ordenes.getValue("OPERACION");
				montoTransaccion = new BigDecimal(_ordenes.getValue("MTO_DIVISAS"));
				conceptoEstadistica = _ordenes.getValue("estadistica");
				estatusEnvio = _ordenes.getValue("Estatus");

				tasa_cambio_ws = parametros.get(ConstantesGenerales.SIGLAS_MONEDA_DOLAR + codigoOperacion.toString());
				tasa_cambio_wss = parametros.get(ConstantesGenerales.SIGLAS_MONEDA_EURO + codigoOperacion.toString());

				if (codMonedaIso.equals(ConstantesGenerales.SIGLAS_MONEDA_DOLAR)) {

					if (estatusEnvio.equals(estatusRechazada)) {
						codMonedaIso = ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
						monto_trans_big_decimal = montoTransaccion;
						tasaCambio = new BigDecimal(_ordenes.getValue("TASA_CAMBIO"));

					} else {
						codMonedaIso = ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
						monto_trans_big_decimal = montoTransaccion;
						tasaCambio = new BigDecimal(tasa_cambio_ws);
					}

				} else {
					if (estatusEnvio.equals(estatusRechazada)) {
						codMonedaIso = ConstantesGenerales.CODIGO_MONEDA_ISO_EUR;
						montoBase = new BigDecimal((_ordenes.getValue("MTO_DIVISAS_TRANS")));
						monto_trans_big_decimal = new BigDecimal(montoBase.toString());
						tasaCambio = new BigDecimal(_ordenes.getValue("TASA_CAMBIO"));

					} else {
						codMonedaIso = ConstantesGenerales.CODIGO_MONEDA_ISO_EUR;
						tasaCambio = new BigDecimal(tasa_cambio_wss);
						BigDecimal tasa_cambio_ws_bg = new BigDecimal(tasa_cambio_ws);
						BigDecimal tasa_cambio_wss_bg = new BigDecimal(tasa_cambio_wss);
						monto_tasa_division = tasa_cambio_wss_bg.divide(tasa_cambio_ws_bg, 8, RoundingMode.DOWN).multiply(montoTransaccion);
						monto_tasa_division = monto_tasa_division.setScale(4, RoundingMode.DOWN);
						monto_trans_big_decimal = monto_tasa_division;
					}
				}

				if (tipoOperacion.toString().equals(Codigo)) {

					if (tipperID.toString().equals(tipo_persona_jur)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_VEN_EFEC_JUR;
						movimiento = 11;
					} else if (tipperID.toString().equals(tipo_persona_ex)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_V_EXTRAN;
					} else if (tipperID.toString().equals(tipo_persona_natural)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN_VENT;
					} else if (tipperID.toString().equals(tipo_persona_pasaporte)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_VENTA_EFEC_PASAPORTE;
						movimiento = 2;
					} else if (tipperID.toString().equals(tipo_persona_gobierno)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_VENTA_EFEC_GOBIERNO;
						movimiento = 12;
					} else {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN_VENT;
					}
				} else {
					if (tipperID.toString().equals(tipo_persona_jur)) {

						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_JUR;
						movimiento = 11;

					} else if (tipperID.toString().equals(tipo_persona_ex)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_EXTRAN;
					} else if (tipperID.toString().equals(tipo_persona_natural)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN;
					} else if (tipperID.toString().equals(tipo_persona_pasaporte)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_PASAPORTE;
						movimiento = 2;
					} else if (tipperID.toString().equals(tipo_persona_gobierno)) {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_GOBIERNO;
						movimiento = 12;
					} else {
						tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN;
					}
					if (conceptoEstadistica.equals(codigoEstadisticaParaCombustible)) {
						movimiento = 18;
						if (tipperID.toString().equals(tipo_persona_jur)) {
							tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_JUR;
						} else {
							tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_GOBIERNO;
						}
					}
				}

				ordenBCV = "";
				logger.info("tipoMovimiento-->" + tipoMovimiento);
				logger.info("codigoCliente-->" + codigoCliente);
				logger.info("nombreCliente-->" + nombreCliente.trim());
				logger.info("monto_trans_big_decimal-->" + monto_trans_big_decimal);
				logger.info("tasaCambio-->" + tasaCambio);
				logger.info("codMonedaIso-->" + codMonedaIso);
				logger.info("montoTransaccion-->" + montoTransaccion);
				logger.info("movimiento-->" + movimiento);
				logger.info("ctaConvenio20-->" + ctaConvenio20);
				logger.info("telefono-->" + telefono);
				logger.info("email-->" + email);
				try {
					if (tipoOperacion.toString().equals(Codigo)) {
						ordenBCV = stub.VENTADIV(tipoMovimiento, codigoCliente, nombreCliente, monto_trans_big_decimal, tasaCambio, codMonedaIso, montoTransaccion, movimiento, "", telefono, email);
					} else {
						ordenBCV = stub.COMPRADIV(tipoMovimiento, codigoCliente, nombreCliente, monto_trans_big_decimal, tasaCambio, codMonedaIso, montoTransaccion, movimiento, "", telefono, email, "");
					}

				} catch (Exception e) {
					logger.error("Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: " + ordeneID + " - " + e.toString() + " " + Utilitario.stackTraceException(e));
					e.printStackTrace();
					proceso.agregarDescripcionErrorTrunc("Ha ocurrido un error al momento de enviar al bcv revisar en observacion de menudeo", true);
					logger.error("INFORMACION ENVIADA AL WS DE BCV");
					logger.error("tipoMovimiento: " + tipoMovimiento);
					logger.error("codigoCliente: " + codigoCliente);
					logger.error("nombreCliente: " + nombreCliente);
					logger.error("montoBase: " + montoBase);
					logger.error("tasaCambio: " + tasaCambio);
					logger.error("codMonedaIso: " + codMonedaIso);
					logger.error("montoTransaccion: " + montoTransaccion);
					logger.error("tipoOperacion: " + tipoOperacion);
					logger.error("ctaConvenio20: " + ctaConvenio20);
					logger.error("telefono: " + telefono);
					logger.error("email: " + email);

					boolean errorControlado = false;
					// VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
					for (ErroresMenudeo tmp : ErroresMenudeo.values()) {
						if (e.toString().contains(tmp.getCodigoError())) {
							errorControlado = true;
							break;
						}
					}
					if (errorControlado) {
						proceso.agregarDescripcionErrorTrunc("Revisar el detalle algunas operaciones no fueron enviadas", true);
						ordenDAO.actualizarOrdenBCVMenudeoM(ordeneID, fecha, e.toString(), "", ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA, monto_trans_big_decimal, tasaCambio);
						ordenesCrucesDAO.actualizarMenudeoBCV(ordeneID, fecha, ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA);
					} else {// SE GENERA UN ERROR NO CONTROLADO
						
						proceso.agregarDescripcionErrorTrunc("Ha ocurrido un error al momento de enviar al bcv revisar en observacion de menudeo", true);
						ordenDAO.actualizarOrdenBCVMenudeoM(ordeneID, fecha, e.toString(), "", ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA, monto_trans_big_decimal, tasaCambio);
						ordenesCrucesDAO.actualizarMenudeoBCV(ordeneID, fecha, ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA);

					}

					continue; // NO QUIERO QUE ACTUALICE DE NUEVO
				}

				try {
					// SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
					ordenDAO.actualizarOrdenBCVMenudeoM(ordeneID, fecha, "Envio a BCV ejecutado con exito", ordenBCV, ConstantesGenerales.ENVIO_MENUDEO, monto_trans_big_decimal, tasaCambio);
					ordenesCrucesDAO.actualizarMenudeoBCV(ordeneID, fecha, ConstantesGenerales.ENVIO_MENUDEO);
					proceso.agregarDescripcionErrorTrunc("Todas las operaciones fueron enviadas", true);
					logger.info("Envio a BCV ejecutado con exito");
				} catch (Exception e) {
					proceso.agregarDescripcionErrorTrunc("Ha ocurrido un error al momento de enviar al bcv revisar en observacion de menudeo", true);
					logger.error("Ha ocurrido un error al momento de actualizar el estaus del BCV " + ordeneID + " para la orden INFI " + ordeneID + "." + e.toString());
					e.printStackTrace();
				}
			}

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Ha ocurrido un error en el proceso de envio de orden al BCV " + e.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("Ha ocurrido un error en el proceso de envio de orden al BCV " + e.toString());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error("Ha ocurrido un error en el proceso de envio de orden al BCV " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Ha ocurrido un error en el proceso de envio de orden al BCV " + e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Ha ocurrido un error en el proceso de envio de orden al BCV " + e.toString());
			e.printStackTrace();
		} finally {
			// terminarProceso();
			logger.error("FINALIZA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
		}
	}

	/**
	 * Ruta de custodia para descifrar la clave y usuario del ws
	 */
	private void rutaCustodia() {
		this.rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
		this.rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
	}

	/**
	 * carga los parametros de la interfaz de menudeo
	 * 
	 * @param _dso
	 * @throws Exception
	 */
	protected void obtenerParametros(DataSource _dso) throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosMenudeBCV = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);
	}

	/**
	 * Metodo para llamar los servicios de menudeo de banco central.
	 * 
	 * @param enpPoint
	 * @throws AxisFault
	 * @throws MalformedURLException
	 */
	public void serviciosMenudeoBancoCentral(String enpPoint) throws AxisFault, MalformedURLException {

		this.stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(enpPoint)), null);
	}

	public void leerServicioTasaBCV() throws ParserConfigurationException, SAXException, IOException {

		this.parametros = new HashMap<String, String>();
		this.propiedades = Propiedades.cargar();
		try {
			this.tasaWsBancoCentral = stub.TASASCAMBIO();
			// Define una API de fábrica que permite a las aplicaciones obtener un analizador que produce árboles de objetos DOM a partir de documentos XML.
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// Define la API para obtener instancias de documento DOM a partir de un documento XML. Con esta clase, un programador de aplicaciones puede obtener un Documentarchivo XML.
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

			org.w3c.dom.Document document = documentBuilder.parse(new InputSource(new StringReader(this.tasaWsBancoCentral)));
			((org.w3c.dom.Document) document).getDocumentElement().normalize();
			org.w3c.dom.NodeList listaTasa = document.getElementsByTagName("MONEDA");

			for (int temp = 0; temp < listaTasa.getLength(); temp++) {
				org.w3c.dom.Node nodo = listaTasa.item(temp);
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {

					org.w3c.dom.Node element = nodo;
					this.codigoprueba = ((org.w3c.dom.Element) element).getAttribute("CODIGO");
					element.equals(ConstantesGenerales.SIGLAS_MONEDA_DOLAR);
					
					this.comprausd = ((org.w3c.dom.Element) element).getElementsByTagName("COMPRA").item(0).getTextContent();
					
					parametros.put(codigoprueba + ConstantesGenerales.COD_COMPRA, comprausd);
					this.ventausd = ((org.w3c.dom.Element) element).getElementsByTagName("VENTA").item(0).getTextContent();
				
					parametros.put(codigoprueba + ConstantesGenerales.COD_VENTA, ventausd);

					element.equals(ConstantesGenerales.SIGLAS_MONEDA_EURO);
					this.compraeur = ((org.w3c.dom.Element) element).getElementsByTagName("COMPRA").item(0).getTextContent();
				
					parametros.put(codigoprueba + ConstantesGenerales.COD_COMPRA, compraeur);
					this.ventaeur = ((org.w3c.dom.Element) element).getElementsByTagName("VENTA").item(0).getTextContent();
				
					parametros.put(codigoprueba + ConstantesGenerales.COD_VENTA, ventaeur);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Ha ocurrido un error al momento de buscar la tasa,revisar el servicio" + "." + e.toString());
			proceso.agregarDescripcionErrorTrunc("Ha ocurrido un error al momento de conectar con BCV,Notificar al area de sistema", true);
		}

	}

	public boolean verificarCiclo(String ciclos) throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);

		procesosDAO.listarPorTransaccionActiva(ciclos);
		if (procesosDAO.getDataSet().count() > 0) {
			valido = false;
		}

		return valido;
	}

	public static void main(String[] args) {
		String a = "999902102017000000500000000250000000000125000000000002000000001000000000000500000003";
		String b = a.substring(0, 82);
		System.out.println(" Linea " + b);
	}

}
