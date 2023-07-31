package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.logic.interfaz_altair.consult.ManejoDeClientes;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.util.Utilitario;
import static com.bdv.infi.logic.interfaces.ConstantesGenerales.*;

/**
 * Clase encargada de realizar la liquidacion de la unidad de inversión de tipo SITME.
 * 
 * @author nvisbal
 */
public class LiquidacionUnidadInversionSitme implements Runnable {

	/** Unidad de Inversión Procesada anteriormente */
	private String unidadProcesada;

	/** * Id de moneda Local */
	private String monedaLocal;

	/** * DataSource */
	private DataSource _dso;

	/** * Contexto de la Aplicación */
	private ServletContext _app;

	/** * Nombre del usuario */
	private String nombreUsuario;

	/** * Usuario que inicializa el proceso de Liquidación */
	private int usuario;

	/** * Direccion Ip */
	private String ip;

	/** * Sucursal */
	private String sucursal;

	/** * Unidades de Inversión */
	private String[] unidadesInversion;

	/** NM del usuario */
	private String nmUsuario;

	/** * Variable de gestion de logs */
	private Logger logger = Logger.getLogger(LiquidacionUnidadInversionSitme.class);

	/** * Dataset Global */
	private DataSet _unidad_inversion;

	boolean cobroEnLinea = false;

	private int customerNumberBDV; // Número de cliente de la contraparte de BDV

	// DAO
	protected ProcesosDAO procesosDAO;
	protected OrdenDAO ordenDAO;
	protected VehiculoDAO vehiculoDAO;
	protected TransaccionFijaDAO transaccionFijaDAO;
	protected ClienteDAO clienteDAO;
	protected ManejoDeClientes manejoDeClientes;
	protected FechaValorDAO fechaValorDAO;
	protected ClienteCuentasDAO clienteCuentasDAO;
	protected MonedaDAO monedaDAO;
	protected MensajeDAO mensajeDAO;
	protected ClienteCuentasDAO cuentaCustodiaDAO;
	
	ControlArchivoDAO controlArchivoDAO = null;
	private com.bdv.infi.dao.Transaccion transaccion;
	
	// Variables
	protected Proceso proceso;
	protected int secuenciaProcesos = 0;
	protected FactoryAltair factoryAltair;
	protected IngresoMasivoCustodia ingresoMasivoCustodia;
	protected IngresoOpics ingresoOpics;
	protected String rifBDV;
	protected HashMap<String, String> codigosDeOperacion = new HashMap<String, String>();
	
	protected String codigoOperacionCredito;
	private final String DEBITO_VEHICULO = "1";
	private final String CREDITO_CONTRAPARTE = "2";
	private DataSet _cliente; // DataSet del cliente
	private ArrayList<OrdenTitulo> listaOrdenTitulo = null;
	private MensajeBcv mensajeBcv = null;
	private DataSet _cuentaCustodia = null;

	// DataSet
	protected DataSet _ordenesALiquidar = null;

	private TransaccionFijaDAO fijaDAO;
	/***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
	 * Constructor
	 * 
	 * @param DataSource
	 *            _dso
	 * @param int
	 *            usuario
	 * @param ServletContext
	 *            _app
	 */
	public LiquidacionUnidadInversionSitme(DataSource _dso, int usuario, ServletContext _app, HttpServletRequest _req, String[] unidades, String nmUsuario) {
		this._dso = _dso;
		this.usuario = usuario;
		this._app = _app;
		this.ip = _req.getRemoteAddr();
		this.nombreUsuario = _req.getSession().getAttribute("framework.user.principal").toString();
		// this.sucursal = _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL).toString();
		this.sucursal = "";
		// System.out.println("LiquidacionUnidadInversionSitme CONSTRUCTOR");
		this.unidadesInversion = unidades;
		this.nmUsuario = nmUsuario;
	}

	/**
	 * <li>Recibe la unidad de inversión y verifica si se puede ejecutar, dependiendo de las fechas 1 y 2 de la Unidad</li>
	 * <li>Crea una Orden con el monto total de todas las ordenes involucradas por vehiculo,para que se envie a ALTAIR al BCV</li>
	 * <li>Recorre las ordenes y se verifica que no tenga ninguna operacion pendiente por cobrar y de CREDITO en el caso de otro vehículo diferente a BDV (Se coloca Status Liquidada)</li>
	 * <li>Se ingresan los titulos en Custodia</li>
	 * <li>Genera la Orden de Recompra en caso de haberse pactado (Con NETEO, Sin NETEO)</li>
	 * <li>Genera el deal para Renta Fija (Recompra,Cartera Propia)</li>
	 * 
	 * @param long
	 *            unidadInversion
	 * @throws Exception
	 */

	private void liquidarSitme() throws Exception, Throwable {
		// Boolean para saber si la operacion de credito al banco central fue procesada con exito
		ordenDAO = new OrdenDAO(_dso);
		vehiculoDAO = new VehiculoDAO(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		clienteDAO = new ClienteDAO(_dso);
		manejoDeClientes = new ManejoDeClientes(_dso);
		fechaValorDAO = new FechaValorDAO(_dso);
		clienteCuentasDAO = new ClienteCuentasDAO(_dso);
		monedaDAO = new MonedaDAO(_dso);
		mensajeDAO = new MensajeDAO(_dso);
		cuentaCustodiaDAO = new ClienteCuentasDAO(_dso);
		fijaDAO=new TransaccionFijaDAO(_dso);
	
		factoryAltair = new FactoryAltair(_dso, _app, true);
		factoryAltair.ipTerminal = this.ip;
		factoryAltair.nombreUsuario = this.nombreUsuario;
		ingresoMasivoCustodia = new IngresoMasivoCustodia(_dso);
		ingresoOpics = new IngresoOpics(_dso, _app, usuario, ip, this.nombreUsuario);
		rifBDV = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.RIF_BDV, this._dso);
		monedaLocal = monedaDAO.listarIdMonedaLocal();

		try {
			logger.info("*************** INICIO DE PROCESO DE LIQUIDACION ***************");
			iniciarProceso();
			/*Invocacion de metodo comentado ya que actualmente no se requeriere la generacion de la orden de contraparte
       		  al momento de requerir dicha generacion de orden de contraparte se deben realizar modificaciones (Validar que la contraparte tenga cuenta registrada)*/ 
			//crearOperacionesContraparte();

			//Metodo para generar las operaciones de credito al Banco Central de Venezuela (BCV)
			crearOperacionCreditoBCV();
			liquidarOrdenes();
			finalizarProceso();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			proceso.setDescripcionError(e.getMessage());
			throw e;

		} finally {
			finalizarProceso();
		}

	} // fin metodo para liquidar la unidad de inversion

	// ****************************************************************************************************************
	/**
	 * Se valida que exista para los titulos correspondiente a la orden, pacto de recompra En caso de haber pactado se genera una orden con las operaciones financieras y las instrucciones de pago
	 * 
	 * @param orden
	 * @throws Exception
	 */
	public ArrayList<String> pactoRecompra(Orden orden) throws Exception {

		// Se busca los titulos que el cliente haya pactado para recompra
		TitulosDAO titulosDAO = new TitulosDAO(_dso);
		long ordenlong = orden.getIdOrden();

		// ArrayList que contendra los SQL para las instrucciones de pago
		ArrayList<String> sqls = new ArrayList<String>();

		// Se crean las ordenes de pacto de recompra para titulos CON NETEO
		boolean neteo = true;
		titulosDAO.titulosRecompra(orden.getIdOrden(), neteo);

		// Dataset a utilizar
		DataSet _titulosRecompraNeteo = titulosDAO.getDataSet();

		// Se crean las ordenes de pacto de recompra para titulos con neteo
		try {
			this.crearOrdenRecompra(_titulosRecompraNeteo, orden, sqls, neteo);
		} catch (Exception e) {
			logger.error("[PROBLEMAS AL GENERAR ORDEN RECOMPRA CON NETEO]--->" + ordenlong + " " + e.getMessage() + Utilitario.stackTraceException(e));
		}

		// Se crean las ordenes de pacto de recompra para titulos SIN NETEO
		neteo = false;
		titulosDAO.titulosRecompra(orden.getIdOrden(), neteo);

		// Dataset a utilizar
		DataSet _titulosRecompraSinNeteo = titulosDAO.getDataSet();
		// Se crean las ordenes de pacto de recompra para titulos SIN NETEO
		this.crearOrdenRecompra(_titulosRecompraSinNeteo, orden, sqls, neteo);

		if (sqls.isEmpty()) {

			logger.info("[NO EXISTEN TITULOS PACTADOS PARA RECOMPRA PARA LA ORDEN NUMERO]--->" + ordenlong);

		}
		return sqls;
	} // fin verificarPactoRecompra

	/**
	 * Crea la orden de recompra dependiendo si es con NETEO o sin NETEO
	 * 
	 * @param _titulosRecompra
	 * @param orden
	 * @param sqls
	 * @param neteo
	 * @throws Exception
	 */
	public void crearOrdenRecompra(DataSet _titulosRecompra, Orden orden, ArrayList<String> sqls, boolean neteo) throws Exception {
		// Date fecha = new Date();
		// ArrayList<String> sqlsMensajesOpics = new ArrayList<String>();
		String idOrdenClave = null;
		

		//BUSQUEDA DE VEHICULO_ID de BDV 
		VehiculoDAO vehiculoDAO=new VehiculoDAO(_dso);		
		vehiculoDAO.obtenerVehiculoBDV();
		String vehiculoID=null;		
		
		if(vehiculoDAO.getDataSet().count()>0){
			vehiculoDAO.getDataSet().first();
			vehiculoDAO.getDataSet().next();
			vehiculoID=vehiculoDAO.getDataSet().getValue("VEHICU_ID");
		}
		
		//Resolucion de incidencia en Produccion proyecto OGD766 
		/*ResultSet result;
		Statement statementVehiculo;
		controlArchivoDAO=new ControlArchivoDAO(_dso);
		transaccion = new com.bdv.infi.dao.Transaccion(_dso);
		transaccion.begin();
		statementVehiculo = transaccion.getConnection().createStatement();			
		result=statementVehiculo.executeQuery(controlArchivoDAO.listar_VALORES_SITME(null,ConstantesGenerales.LISTAR_INFORMACION_VEHICULO));		
		while(result.next()){							
			vehiculoID=result.getString("vehicu_id");													
		}*/
		
		//Obtencion de codigo de operacion para el abono de cuenta nacional en dolares
		fijaDAO.listarTransaccionesFijasAdjudicacionClaveNet(String.valueOf(orden.getIdUnidadInversion()),vehiculoID,ConstantesGenerales.TRANSACCION_ABONO_CTA_DOLARES);
		DataSet ds = fijaDAO.getDataSet();
							
		if(ds.count()>0){
			ds.first();
			ds.next();
			codigoOperacionCredito=ds.getValue("COD_OPERACION_CTE_CRE");	
		}
		
		
		if(codigoOperacionCredito==null){
			logger.error("El instrumento financiero no tiene configurado el codigo de operacion: COD_OPERACION_CTE_CRE");
			throw new Exception("Error en el proceso de de creacion de orden de recompra. El instrumento financiero no tiene configurado el codigo de operacion: COD_OPERACION_CTE_CRE");
		}
		
		//statementVehiculo.close();
		// Se verifica si existen titulos pactados para recompra
		if (_titulosRecompra.count() > 0) {
			logger.info("[EXISTEN TITULOS PARA RECOMPRA PARA LA ORDEN NUMERO]--->" + orden.getIdOrden());
			_titulosRecompra.first();

			// HashMap a utilizar en el proceso para agrupar montos y titulos por moneda
			HashMap<String, BigDecimal> operacionesHashMap = new HashMap<String, BigDecimal>();
			HashMap<String, ArrayList> idTitulos = new HashMap<String, ArrayList>(); // Agrupa los titulos por moneda

			while (_titulosRecompra.next()) {

				// Se crea el objeto Orden Titulo
				OrdenTitulo ordenTitulo = new OrdenTitulo();
				ordenTitulo.setMonto(Double.parseDouble(_titulosRecompra.getValue("titulo_monto")));				
				ordenTitulo.setPorcentaje(_titulosRecompra.getValue("titulo_pct"));
				ordenTitulo.setPorcentajeRecompra(Double.parseDouble(_titulosRecompra.getValue("titulo_pct_recompra")));
				ordenTitulo.setTituloId(_titulosRecompra.getValue("titulo_id"));
				ordenTitulo.setUnidades(Double.parseDouble(_titulosRecompra.getValue("titulo_unidades")));
				ordenTitulo.setMontoNeteo(_titulosRecompra.getValue("TITULO_MTO_NETEO") != null && !_titulosRecompra.getValue("TITULO_MTO_NETEO").equals("") ? new BigDecimal(_titulosRecompra.getValue("TITULO_MTO_NETEO")) : new BigDecimal(0));
				ordenTitulo.setMontoIntCaidos(new BigDecimal(_titulosRecompra.getValue("TITULO_MTO_INT_CAIDOS")));
				
				logger.info("crearOrdenRecompra-->titulo_monto= "+_titulosRecompra.getValue("titulo_monto")+",TITULO_MTO_INT_CAIDOS= "+_titulosRecompra.getValue("TITULO_MTO_INT_CAIDOS")+", titulo_pct_recompra= "+_titulosRecompra.getValue("titulo_pct_recompra"));
				
				// Calculamos el monto para el precio de recompra indicado
				BigDecimal precioRecompra = new BigDecimal(_titulosRecompra.getValue("titulo_pct_recompra"));
				BigDecimal tituloMonto = new BigDecimal(_titulosRecompra.getValue("titulo_monto"));
				BigDecimal montoRecompra = new BigDecimal(0);
				montoRecompra = tituloMonto.setScale(3, BigDecimal.ROUND_HALF_EVEN).multiply(precioRecompra.setScale(3, BigDecimal.ROUND_HALF_EVEN));
				montoRecompra = montoRecompra.divide(new BigDecimal(100).setScale(3, BigDecimal.ROUND_HALF_EVEN));	
				montoRecompra = montoRecompra.add(ordenTitulo.getMontoIntCaidos());
				
				logger.info("crearOrdenRecompra-->[(titulo_monto*titulo_pct_recompra)/100]+iTITULO_MTO_INT_CAIDOS"+ montoRecompra);		

				boolean contiene = operacionesHashMap.containsKey(_titulosRecompra.getValue("titulo_moneda_neg"));

				/*
				 * Se verifica si el HashMap contiene la Clave de la moneda, de ser asi se le suma el monto calculando el monto de recompra, de no contenerlo, se agrega la nueva clave valor. De igual forma se agrega el objeto orden titulo al arrayList perteneciente a la misma moneda
				 */
				if (contiene) {
					
					BigDecimal monto = operacionesHashMap.get(_titulosRecompra.getValue("titulo_moneda_neg"));
					monto = monto.setScale(3, BigDecimal.ROUND_HALF_EVEN).add(montoRecompra).setScale(3, BigDecimal.ROUND_HALF_EVEN);
					operacionesHashMap.put(_titulosRecompra.getValue("titulo_moneda_neg"), monto);
					idTitulos.get(_titulosRecompra.getValue("titulo_moneda_neg")).add(ordenTitulo);

				} else {
					
					operacionesHashMap.put(_titulosRecompra.getValue("titulo_moneda_neg"), montoRecompra);
					ArrayList<OrdenTitulo> titulosId = new ArrayList<OrdenTitulo>();
					titulosId.add(ordenTitulo);
					idTitulos.put(_titulosRecompra.getValue("titulo_moneda_neg"), titulosId);

				} // fin else
			} // fin while titulos en recompra
			

			/*
			 * Se itera el HashMap para crear las Ordenes con sus titulos asociados, la operacion financiera, las instrucciones de pago y el deal para renta fija
			 */
			Iterator<Entry<String, ArrayList>> iterator = idTitulos.entrySet().iterator();

			while (iterator.hasNext()) {

				Map.Entry e = (Map.Entry) iterator.next();
				String moneda = e.getKey().toString();
				ArrayList<OrdenTitulo> ordenTituloArrayList = (ArrayList) e.getValue();

				// Generamos una orden de Recompra
				Orden ordenRecompra = new Orden();
				InstruccionesPago instruccionesPago = new InstruccionesPago();
				// com.bdv.infi.data.FechaValor fechaValor = new com.bdv.infi.data.FechaValor();
				// fechaValor = fechaValorDAO.listar(com.bdv.infi.logic.interfaces.FechaValor.RECOMPRA_TITULOS_SITME);

				// Orden Recompra
				ordenRecompra.setCarteraPropia(false);
				ordenRecompra.setTerminal(ip);
				ordenRecompra.setVehiculoTomador(orden.getVehiculoTomador());
				ordenRecompra.setSucursal(sucursal);
				ordenRecompra.setIdUnidadInversion(orden.getIdUnidadInversion());
				ordenRecompra.setNombreUsuario(nombreUsuario);
				ordenRecompra.setIdCliente(orden.getIdCliente());
				ordenRecompra.setStatus(neteo ? StatusOrden.REGISTRADA : StatusOrden.REGISTRADA);
				ordenRecompra.setIdTransaccion(TransaccionNegocio.PACTO_RECOMPRA);
				ordenRecompra.setFechaOrden(orden.getFechaOrden()); // cambio 29/12/2011 CE
				ordenRecompra.setFechaValor(orden.getFechaValor()); 
				ordenRecompra.setTipoProducto(orden.getTipoProducto());
				ordenRecompra.setIdMoneda(orden.getIdMoneda());

				/*
				 * Si al cliente no se le ha cobrado no puede establecerse fecha de pacto. Debe ser el proceso de cobro de liquidación quien lo establezca.
				 */
				if (!cobroEnLinea) {
					if (hayOperacionesEnRechazo(orden.getOperacion())) {
						ordenRecompra.setFechaValor(new Date(2200, 01, 01));
					}
				}

				ordenRecompra.setIdOrdenRelacionada(orden.getIdOrden());
				ordenRecompra.agregarOrdenTitulo(ordenTituloArrayList);
				ordenRecompra.setTipoCuentaAbono(orden.getTipoCuentaAbono());
				ordenRecompra.setMontoAdjudicado(orden.getMontoAdjudicado());
				
				// Si los titulos fueron pactados para recompra con neteo
				if (neteo) {
					// Creamos la operacion financiera
					OrdenOperacion operacion = new OrdenOperacion();
					operacion.setStatusOperacion(neteo ? com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA : com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
					operacion.setAplicaReverso(false);
					operacion.setMontoOperacion(operacionesHashMap.get(moneda));
					operacion.setTasa(new BigDecimal(0));
					operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
					operacion.setIdMoneda(moneda);
					operacion.setFechaAplicar(new Date());

					// Agregamos la operacion financiera a la orden
					ordenRecompra.agregarOperacion(operacion);

					// Se agrega la data extendida del tipo instruccion
					OrdenDataExt ordenDataExt = new OrdenDataExt();
					ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
					ordenDataExt.setValor(String.valueOf(instruccionesPago.getTipoInstruccionId()));
					ordenRecompra.agregarOrdenDataExt(ordenDataExt);

					String[] consultas = ordenDAO.insertar(ordenRecompra);

					for (int i = 0; i < consultas.length; i++) {
						sqls.add(consultas[i]);
					}// fin for

					// Genera el mensaje CARMEN de salida

					generarMensajeCarmen(ordenRecompra, sqls);

					// Generacion de los deal tickets por titulo
					/*
					 * if (_unidad_inversion.getValue("insfin_descripcion").equals("TITULOS_SITME")){ logger.info("Invocacion de metodo rentaFija para SITME"); sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra,true); for (int i = 0; i < sqlsMensajesOpics.size(); i++) { sqls.add(sqlsMensajesOpics.get(i).toString()); }//fin for }
					 */
					/*
					 * sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra);
					 * 
					 * for (int i = 0; i < sqlsMensajesOpics.size(); i++) { sqls.add(sqlsMensajesOpics.get(i).toString()); }// fin for
					 */

					/*
					 * Como los titulos fueron pactados para recompra, no es necesario buscar las intrucciones de pago, se continua
					 */
					logger.info("[RECOMPRA CON NETEO, SE CONTINUA EL PROCESO]");
					continue;
				}// Si los titulos fueron pactados para recompra con neteo

				/*
				 * Por los diferentes tipos de moneda se genera la operacion financiera, la instruccion de pago y el deal ticket renta fija
				 */
				/*monedaDAO.listarMonedaLocal();
				monedaDAO.getDataSet().first();
				monedaDAO.getDataSet().next();*/

				logger.info("[INSTRUCCION DE PAGO]--->[SWIFT]");

				/*
				 * Consulta la instrucción de pago SWIFT(RECOMPRA) asociada a la orden de TOMA DE ORDEN
				 */
				idOrdenClave = ordenDAO.validarOrdenClave(orden.getIdOrden());

				System.out.println("idOrdenClave: " + idOrdenClave);
				if (idOrdenClave != null) {
					logger.debug("crearOrdenRecompra->la orden es de clave: " + idOrdenClave);
					
					
					if(orden.getTipoCuentaAbono()==Integer.parseInt(ABONO_CUENTA_EXTRANJERO)) {
						clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(),TipoInstruccion.CUENTA_SWIFT, UsoCuentas.RECOMPRA);	
					} else if(orden.getTipoCuentaAbono()==Integer.parseInt(ABONO_CUENTA_NACIONAL)) {											
						clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(),TipoInstruccion.CUENTA_NACIONAL_DOLARES, UsoCuentas.RECOMPRA);
					}
					
					
					logger.debug("crearOrdenRecompra->" + clienteCuentasDAO.getDataSet());
					if (clienteCuentasDAO.getDataSet().count() > 0) {
						clienteCuentasDAO.getDataSet().first();
						clienteCuentasDAO.getDataSet().next();
						
						if(orden.getTipoCuentaAbono()==Integer.parseInt(ABONO_CUENTA_EXTRANJERO)) {						
							instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_SWIFT);
						}else if(orden.getTipoCuentaAbono()==Integer.parseInt(ABONO_CUENTA_NACIONAL)) {
							instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_NACIONAL_DOLARES);
						}
												
						// Creamos la operacion financiera
						OrdenOperacion operacion = new OrdenOperacion();
						operacion.setStatusOperacion(neteo ? com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA : com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
						operacion.setAplicaReverso(false);
						
						System.out.println("monto operacion "+operacionesHashMap.get(moneda));
						operacion.setMontoOperacion(operacionesHashMap.get(moneda));
						operacion.setIdMoneda(moneda);
						operacion.setTasa(new BigDecimal(0));
						operacion.setNumeroCuenta(clienteCuentasDAO.getDataSet().getValue("CTECTA_NUMERO"));
						operacion.setNombreReferenciaCuenta(clienteCuentasDAO.getDataSet().getValue("CTECTA_NOMBRE"));
						operacion.setNombreBanco(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOCTA_BCO"));
						operacion.setDireccionBanco(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOCTA_DIRECCION"));
						operacion.setCodigoSwiftBanco(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOCTA_SWIFT"));
						operacion.setCodigoBicBanco(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOCTA_BIC"));
						operacion.setTelefonoBanco(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOCTA_TELEFONO"));
						operacion.setCodigoABA(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOCTA_ABA"));
						
						//---Datos del Intermediario-----------------------------------------------------------------------
						//Si la instrucción tiene intermediario						
						if(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_BIC")!=null && !clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_BIC").trim().equals("")){							
							operacion.setCodigoBicBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_BIC"));//BIC del banco intermediario							
							operacion.setNombreBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_BCO"));//Nombre del banco intermediario
							operacion.setDireccionBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_DIRECCION"));//Dirección del banco intermediario
							operacion.setCodigoSwiftBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_SWIFT")); //Cuenta del banco destino en el banco intermediario
							operacion.setTelefonoBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_TELEFONO"));//Telefono del banco intermediario
							
							if(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_ABA")!=null){
								operacion.setCodigoABAIntermediario(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_ABA").trim());//Aba del banco intermediario.
							}							

						}		
						
						operacion.setPaisBancoCuenta("");	
						operacion.setCodigoOperacion(codigoOperacionCredito);
						operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
						operacion.setFechaAplicar(orden.getFechaPactoRecompra());

						/*
						 * Si al cliente no se le ha cobrado no puede establecerse fecha de pacto. Debe ser el proceso de cobro de liquidación quien lo establezca.
						 */
						if (!cobroEnLinea) {
							if (hayOperacionesEnRechazo(orden.getOperacion())) {
								operacion.setFechaAplicar(new Date(2200, 01, 01));
							}
						}
						// Agregamos la operacion financiera a la orden
						ordenRecompra.agregarOperacion(operacion);
						// Se agrega como data extendida el nombre del beneficiario
						OrdenDataExt ordenDataExt = new OrdenDataExt();
						ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
						ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
						ordenRecompra.agregarOrdenDataExt(ordenDataExt);
						// Se agrega como data extendida la cedula del beneficiario
						/*ordenDataExt = new OrdenDataExt();
						ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO); 
						ordenDataExt.setValor(formatearCedula(clienteCuentasDAO.getDataSet().getValue("CED_RIF_CLIENTE")));
						ordenRecompra.agregarOrdenDataExt(ordenDataExt);*/
						// Se agrega como data extendida el id de la orden tomada en clave
						ordenDataExt = new OrdenDataExt();
						ordenDataExt.setIdData(DataExtendida.NRO_TICKET);
						ordenDataExt.setValor(idOrdenClave);
						ordenRecompra.agregarOrdenDataExt(ordenDataExt);

					}

				} else {

					boolean swift = clienteCuentasDAO.existeCuentaClienteSwift(orden.getIdCliente());
					if (!swift) {
						logger.error("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA EXTRANJERA PARA LA ORDEN]--->" + orden.getIdOrden());
						throw new Exception("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA EXTRANJERA PARA LA ORDEN]--->" + orden.getIdOrden());

					} else {
						clienteCuentasDAO.getDataSet().first();
						clienteCuentasDAO.getDataSet().next();

						instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_SWIFT);

						// Creamos la operacion financiera
						OrdenOperacion operacion = new OrdenOperacion();
						operacion.setStatusOperacion(neteo ? com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA : com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
						operacion.setAplicaReverso(false);
						operacion.setMontoOperacion(operacionesHashMap.get(moneda));
						operacion.setIdMoneda(moneda);
						operacion.setTasa(new BigDecimal(0));
						operacion.setNumeroCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_numero"));
						operacion.setNombreReferenciaCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_nombre"));
						operacion.setNombreBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bco"));
						operacion.setDireccionBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_direccion"));
						operacion.setCodigoSwiftBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_swift"));
						operacion.setCodigoBicBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bic"));
						operacion.setTelefonoBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_telefono"));
						operacion.setCodigoABA(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_aba"));
						operacion.setPaisBancoCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_pais"));
						operacion.setNombreBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bco"));
						operacion.setDireccionBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_direccion"));
						operacion.setCodigoSwiftBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_swift"));
						operacion.setCodigoBicBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bic"));
						operacion.setTelefonoBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_telefono"));
						operacion.setCodigoABAIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_aba"));
						operacion.setPaisBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_pais"));
						operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
						operacion.setFechaAplicar(orden.getFechaPactoRecompra());

						/*
						 * Si al cliente no se le ha cobrado no puede establecerse fecha de pacto. Debe ser el proceso de cobro de liquidación quien lo establezca.
						 */
						if (!cobroEnLinea) {
							if (hayOperacionesEnRechazo(orden.getOperacion())) {
								operacion.setFechaAplicar(new Date(2200, 01, 01));
							}
						}

						// Agregamos la operacion financiera a la orden
						ordenRecompra.agregarOperacion(operacion);

						// Se agrega como data extendida el nombre del beneficiario
						OrdenDataExt ordenDataExt = new OrdenDataExt();
						ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
						ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
						ordenRecompra.agregarOrdenDataExt(ordenDataExt);

						// Se agrega como data extendida la cedula del beneficiario
						ordenDataExt = new OrdenDataExt();
						ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
						ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("CEDULA_BENEFICIARIO"));
						ordenRecompra.agregarOrdenDataExt(ordenDataExt);
					}
				}
				// Se agrega la data extendida del tipo instruccion
				OrdenDataExt ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
				ordenDataExt.setValor(String.valueOf(instruccionesPago.getTipoInstruccionId()));
				ordenRecompra.agregarOrdenDataExt(ordenDataExt);

				String[] consultas = ordenDAO.insertar(ordenRecompra);

				for (int i = 0; i < consultas.length; i++) {
					sqls.add(consultas[i]);
				}// fin for

				// Genera el mensaje CARMEN de salida
				generarMensajeCarmen(ordenRecompra, sqls);

				logger.info("Inicio de Generacion del en crearOrdenRecompra");

				/*
				 * sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra); for (int i = 0; i < sqlsMensajesOpics.size(); i++) { sqls.add(sqlsMensajesOpics.get(i).toString()); }// fin for
				 */
			} // fin iteracion del hashmap idTitulos
		} // FIN if(_titulosRecompra.count()>0){
	}// FIN crearOrdenRecompra

	/**
	 * Enviar el arrayList hacia ALTAIR para que sean procesadas las operaciones
	 * 
	 * @param factoryAltair
	 * @param ordenOperacionArrayList
	 * @throws Exception
	 * @throws Throwable
	 */
	public void enviarAltair(FactoryAltair factoryAltair, ArrayList<OrdenOperacion> ordenOperacionArrayList) throws Exception, Throwable {

		try {
			// Se intenta enviar la orden contra altair
			factoryAltair.aplicarOrdenes(ordenOperacionArrayList);
			logger.debug("OPERACIONES ENVIADAS");
		} catch (Throwable e) {
			logger.error("[INCONVENIENTES EN PROCESAR OPERACIONES HACIA ALTAIR" + e.getMessage() + Utilitario.stackTraceException(e));
		}// fin catch
	}// FIN METODo

	/**
	 * Busca si la unidad de inversión debe cobrarse batch
	 * 
	 * @param unidadInversion
	 *            unidad de inversión
	 * @throws Exception
	 */
	private void setearCobroEnLineaDeLaUnidad(long unidadInversion) throws Exception {
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		unidadDAO.listarPorId(unidadInversion);
		DataSet dataSet = unidadDAO.getDataSet();
		if (dataSet.next()) {
			if (dataSet.getValue("in_cobro_batch_liq") != null && dataSet.getValue("in_cobro_batch_liq").equals("0")) {
				logger.debug("Cobro de liquidación vía batch");
				cobroEnLinea = true;
			}
		}
	}

	/**
	 * Inicia el proceso de liquidación
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	protected void iniciarProceso() throws Exception {
		logger.info("[Aperturando el proceso de liquidación]...");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.LIQUIDACION);
		proceso.setUsuarioId(this.usuario);

		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}

	/**
	 * Liquida las órdenes la unidad de inversión seleccionada.
	 * 
	 * @throws Throwable
	 */
	protected void liquidarOrdenes() throws Throwable {
		com.bdv.infi.dao.Transaccion transaccionDao = new com.bdv.infi.dao.Transaccion(_dso);
		Statement statement = null;
		ResultSet _ordenes = null;
		
		try {
			transaccionDao.begin();
			statement = transaccionDao.getConnection().createStatement();

			unidadProcesada = null;
			// Por unidad de inversión
			for (int unidad = 0; unidad < this.unidadesInversion.length; unidad++) {
				if (!esPosibleProcesar(unidadesInversion[unidad])) {
					continue;
				}
				logger.info("[PROCESANDO UNIDAD SITME ID]---> " + unidadesInversion[unidad]);
				_ordenes = statement.executeQuery(ordenDAO.listarOrdenesPorUnidadInversion(StatusOrden.ADJUDICADA, false, unidadesInversion[unidad],ConstantesGenerales.ID_TIPO_PRODUCTO_SITME,false));
				String sqlAdjudicado = "";
				boolean operacionesEnRechazo = false;
				/*
				 * Mientras el dataset contenga datos se actualiza el status de la orden a liquidada, si no tiene ningun monto pendiente por aplicar,se ingresan los titulos a custodia y se verifican si los titulos poseen pacto de recompra
				 */
				while (_ordenes.next()) {
					logger.info("[PROCESANDO ORDEN]---> " + _ordenes.getString("ordene_id"));
					Orden orden = null;
					operacionesEnRechazo = false;
														
					setearCobroEnLineaDeLaUnidad(_ordenes.getLong("uniinv_id"));

					// Si a la orden le fue ADJUDICADO (CERO) , solo se liquida la orden
					if (_ordenes.getString("ordene_adj_monto") == null || _ordenes.getString("ordene_adj_monto").equals("0")) {
						logger.debug("[LA ORDEN TIENE MONTO ADJUDICADO DE 0]");
						// update infi_tb_204_ordenes set ejecucion_id
						sqlAdjudicado = ordenDAO.liquidarOrden(secuenciaProcesos, StatusOrden.LIQUIDADA, _ordenes.getLong("ordene_id"));
						try {
							// Ejecutamos en una transacción
							db.exec(_dso, sqlAdjudicado);
							logger.info("[ORDEN LIQUIDADA...]");
						} catch (Exception e) {
							logger.error("[LA ORDEN PRESENTO INCONVENIENTES PARA INTENTAR SER LIQUIDADA]--->", e);
							throw e;
						}
						// Se continua con el proceso
						continue;
					}// FIN Si a la orden le fue adjudicado 0, solo se liquida la unidad de inversion

					// ----------- Inicio de generacion de Deal cuando es cartera propia -----------------
					// Si la orden es de cartera propia, solo generamos el DEAL y liquidamos la orden
					if (_ordenes.getString("transa_id").equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {
						logger.debug("[ORDEN DE CARTERA PROPIA]--->" + "[VEHICULO TOMADOR]--->" + _ordenes.getString("ordene_veh_tom"));

						// Generacion de los deal tickets por titulo
						IngresoOpics ingresoOpics = new IngresoOpics(_dso, _app, usuario, ip, this.nombreUsuario);
						orden = ordenDAO.listarOrden(_ordenes.getLong("ordene_id"), false, true, false, false, false);

						/*
						 * Se elimina la generacion de deals de ArrayList<String> sqlsMensajesOpics = ingresoOpics.rentaFija(orden);
						 */
						ArrayList<String> sqls = new ArrayList<String>();
						// Agregamos el query de liquidar la orden
						sqls.add(ordenDAO.liquidarOrden(secuenciaProcesos, StatusOrden.LIQUIDADA, _ordenes.getLong("ordene_id")));

						try {
							db.execBatch(_dso, (String[]) sqls.toArray(new String[sqls.size()]));
							logger.info("[SE LIQUIDA LA ORDEN]--->LOS TITULOS NO ENTRAN A CUSTODIA POR INFI");
						} catch (Exception e) {
							logger.error("[LA ORDEN DE CARTERA PROPIA PRESENTO INCONVENIENTES PARA SER LIQUIDADA]", e);
							throw e;
						}

						// Se continua con la siguiente orden
						continue;
					}// FIN de generacion de Deal cuando es CARTERA PROPIA . . . . . . . .

					// SE INICIA RECORRIDO DE ORDENES Y OPERACIONES FUERA DE CARTERRA PROPIA . . . . . . . .
					ArrayList<OrdenOperacion> operacionesFinancieras = new ArrayList<OrdenOperacion>();
					orden = ordenDAO.listarOrden(_ordenes.getLong("ordene_id"), false, true, false, true, false);

					// Orden ordenRecompra = new Orden();
					if (cobroEnLinea) {
						operacionesFinancieras = orden.getOperacion();
						this.enviarAltair(factoryAltair, operacionesFinancieras);
						operacionesEnRechazo = hayOperacionesEnRechazo(operacionesFinancieras);
					} else {
						// Recorre las operaciones de la orden a ver si hay una en espera por cobrar
						for (OrdenOperacion operacion : orden.getOperacion()) {
							if (operacion.getTipoTransaccionFinanc().equals(TransaccionFinanciera.DEBITO) && operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_EN_ESPERA)) {
								operacionesEnRechazo = true;
								break;
							}
						}
					}

					if (!operacionesEnRechazo || !cobroEnLinea) {
						try {
							// Se invoca Ingreso Masivo a Custodia.
							// Si la orden tiene recompra, Efectuando la salida de los títulos de recompra.
							String[] consultas = ingresoMasivoCustodia.ingresarCustodia(_ordenes.getLong("ordene_id"), cobroEnLinea, operacionesEnRechazo);

							// ArrayList que contendra los querys finales para su ejecucion
							ArrayList<String> sqlsFinales = new ArrayList<String>();

							// Recorremos las consultas para insertarlas en una conexion
							for (int i = 0; i < consultas.length; i++) {
								sqlsFinales.add(consultas[i]);
							}

							// Query para liquidar la orden
							// update infi_tb_204_ordenes set ejecucion_id
							String sqlLiquidar = ordenDAO.liquidarOrden(secuenciaProcesos, StatusOrden.LIQUIDADA, _ordenes.getLong("ordene_id"));
							sqlsFinales.add(sqlLiquidar);
							String tipoCuentaAbono=_ordenes.getString("CTA_ABONO");
							orden.setTipoCuentaAbono(Integer.parseInt(tipoCuentaAbono));
							orden.setIdMoneda(_ordenes.getString("MONEDA_ID"));
							orden.setIdUnidadInversion(Integer.parseInt(unidadesInversion[unidad]));
							ArrayList<String> sqls = this.pactoRecompra(orden);
							for (int i = 0; i < sqls.size(); i++) {
								sqlsFinales.add(sqls.get(i));
							}

							generarMensajeCarmen(orden, sqlsFinales);

							// Pasamos el ArrayList A Array de String
							String[] sqlsFinalesArray = new String[sqlsFinales.size()];
							sqlsFinales.toArray(sqlsFinalesArray);

							db.execBatch(_dso, sqlsFinalesArray);

							logger.info("[VERIFICADOS TITULOS PACTO RECOMPRA]");
							logger.info("[INGRESADOS TITULOS A CUSTODIA]");
							logger.info("[ORDEN LIQUIDADA]");
						} catch (Exception e) {
							logger.error("[LA ORDEN PRESENTO INCONVENIENTES PARA SER PROCESADA]--->" + "[" + _ordenes.getString("ordene_id") + "]--->", e);
							throw e;
						}
					}
				} // fin while ORDEN
			} // fin for
		} catch (Exception e) {
			logger.error("Error en la liquidación de ordenes SITME", e);
			throw e;
		} finally {
			if (_ordenes != null) {
				_ordenes.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (transaccionDao != null) {
				transaccionDao.closeConnection();
			}
		}
	}

	/**
	 * Crea las ordenes de contraparte por cada unidad de inversión a liquidar
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	protected void crearOperacionesContraparte() throws Exception {
		
		com.bdv.infi.dao.Transaccion transaccionDao = new com.bdv.infi.dao.Transaccion(_dso);
		Statement statement = null;
		ResultSet _ordenesALiquidar = null;
		ArrayList<String> Sql = new ArrayList<String>();
		String[] consultas;
		long idCliente = 0;
		long idRelacion = 0;

		try {
			transaccionDao.begin();
			statement = transaccionDao.getConnection().createStatement();
			logger.info("Liquidando contrapartes de " + unidadesInversion.length + " unidades de inversión");
			for (int i = 0; i < this.unidadesInversion.length; i++) {
				if (!esPosibleProcesar(unidadesInversion[i])) {
					continue;
				}
				
				_ordenesALiquidar = statement.executeQuery(ordenDAO.listarMontosSitmeParaLiquidacion(unidadesInversion[i]));
				
				logger.info("Id de unidad a liquidar:" + unidadesInversion[i]);

				while (_ordenesALiquidar.next()) {
					if (!_ordenesALiquidar.getString("vehicu_rif").equals(rifBDV)) {
						
						// Crea la orden de débito de la cuenta del vehículo y hace un crédito a la cuenta de la contraparte
						Orden ordenDebito = new Orden();
						idCliente = getCliente(_ordenesALiquidar.getString("vehicu_rif").substring(2), _ordenesALiquidar.getString("vehicu_rif").substring(0, 1));

						if (idCliente == 0) {
							logger.info("Cliente con rif " + _ordenesALiquidar.getString("vehicu_rif") + " no fue encontrado");
							throw new Exception("Cliente con rif " + _ordenesALiquidar.getString("vehicu_rif") + " no fue encontrado");
						}

						ordenDebito.setIdEmpresa(_ordenesALiquidar.getString("empres_id"));
						ordenDebito.setIdCliente(idCliente);
						ordenDebito.setFechaOrden(new Date());
						ordenDebito.setFechaValor(new Date());
						ordenDebito.setStatus(StatusOrden.REGISTRADA);
						ordenDebito.setIdTransaccion(TransaccionNegocio.LIQUIDACION);
						ordenDebito.setIdUnidadInversion(Integer.parseInt(unidadesInversion[i]));
						ordenDebito.setVehiculoTomador(_ordenesALiquidar.getString("vehicu_id"));

						OrdenOperacion operacion = new OrdenOperacion();
						operacion.setMontoOperacion(new BigDecimal(_ordenesALiquidar.getString("monto")));
						operacion.setNumeroCuenta(_ordenesALiquidar.getString("vehicu_numero_cuenta"));
						operacion.setCodigoOperacion(getCodigoOperacion(String.valueOf(TransaccionFija.DEBITO_CUENTA_VEHICULO), _ordenesALiquidar.getString("vehicu_id"), _ordenesALiquidar.getString("insfin_id"), this.DEBITO_VEHICULO));
						operacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEBITO_CUENTA_VEHICULO));
						operacion.setFechaAplicar(new Date());
						operacion.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
						operacion.setIdMoneda(monedaLocal); //
						operacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
						operacion.setNombreOperacion("Débito a vehículo");
						ordenDebito.agregarOperacion(operacion);
						consultas = ordenDAO.insertar(ordenDebito);
						idRelacion = ordenDebito.getIdOrden();
						for (String string : consultas) {
							Sql.add(string);
						}
					}
					// Efectúa el crédito a la contraparte
					Orden ordenCredito = new Orden();
					idCliente = getCliente(_ordenesALiquidar.getString("empres_rif").substring(2), _ordenesALiquidar.getString("empres_rif").substring(0, 1));

					if (idCliente == 0) {
						logger.info("Cliente con rif " + _ordenesALiquidar.getString("empres_rif") + " no fue encontrado");
						throw new Exception("Cliente con rif " + _ordenesALiquidar.getString("empres_rif") + " no fue encontrado");
					}

					
					ordenCredito.setIdEmpresa(_ordenesALiquidar.getString("empres_id"));
					ordenCredito.setIdCliente(idCliente);
					ordenCredito.setFechaOrden(new Date());
					ordenCredito.setFechaValor(new Date());
					ordenCredito.setStatus(StatusOrden.REGISTRADA);
					ordenCredito.setIdTransaccion(TransaccionNegocio.LIQUIDACION);
					ordenCredito.setIdUnidadInversion(Integer.parseInt(unidadesInversion[i]));
					ordenCredito.setVehiculoTomador(_ordenesALiquidar.getString("vehicu_id"));

					if (idRelacion > 0) {
						ordenCredito.setIdOrdenRelacionada(idRelacion);
					}

					OrdenOperacion operacionCredito = new OrdenOperacion();
					operacionCredito.setMontoOperacion(new BigDecimal(_ordenesALiquidar.getString("monto")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
					operacionCredito.setNumeroCuenta(_ordenesALiquidar.getString("empres_cuenta"));
					operacionCredito.setCodigoOperacion(getCodigoOperacion(String.valueOf(TransaccionFija.CREDITO_CUENTA_EMISOR), _ordenesALiquidar.getString("vehicu_id"), _ordenesALiquidar.getString("insfin_id"), this.CREDITO_CONTRAPARTE));
					operacionCredito.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CREDITO_CUENTA_EMISOR));
					operacionCredito.setFechaAplicar(new Date());
					operacionCredito.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
					operacionCredito.setIdMoneda(monedaLocal); //
					operacionCredito.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
					operacionCredito.setNombreOperacion("Crédito a la contraparte");
					ordenCredito.agregarOperacion(operacionCredito);
					
					consultas = ordenDAO.insertar(ordenCredito);
					for (String string : consultas) {						
						Sql.add(string);
					}
					// Actualiza las ordenes relacionadas
					Sql.add(ordenDAO.actualizarOrdenesIdRelacion(ordenCredito.getIdOrden(), _ordenesALiquidar.getLong("empres_id"), unidadesInversion[i],ConstantesGenerales.ID_TIPO_PRODUCTO_SITME));
				}

				if (logger.isDebugEnabled()) {
					Iterator it = Sql.iterator();
					while (it.hasNext()) {
						logger.debug("SQL-->" + it.next());
					}
				}
				db.execBatch(_dso, (String[]) Sql.toArray(new String[Sql.size()]));
			}
		} catch (Exception e) {
			logger.error("Error en la generación de los montos de la contraparte", e);
			
			//throw e;
		} finally {
			if (_ordenesALiquidar != null) {
				_ordenesALiquidar.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (transaccionDao != null) {
				transaccionDao.closeConnection();
			}
		}
	}

	/**
	 * Obtiene el código de la operación correspondiente a los datos pasados
	 * 
	 * @param transaccionFija
	 *            indica la transacción fija a buscar
	 * @param vehiculo
	 *            id del vehículo involucrado
	 * @param instruncionFinanciera
	 *            tipo de instrucción financiera
	 * @param tipo
	 *            Indica el tipo de operación para saber de que campo se obtienen los datos. Los valores son 1=debito al vehículo y 2=credito a la contraparte
	 * @return código de operación, puede devolver "" en caso que no encuentre valor
	 */
	private String getCodigoOperacion(String transaccionFija, String vehiculo, String instruncionFinanciera, String tipo) throws Exception {
		String codigoOperacion = "";
		if (codigosDeOperacion.containsKey(transaccionFija.concat(vehiculo).concat(instruncionFinanciera))) {
			codigoOperacion = codigosDeOperacion.get(transaccionFija.concat(vehiculo).concat(instruncionFinanciera));
		} else {
			transaccionFijaDAO.listar(Integer.parseInt(transaccionFija), vehiculo, instruncionFinanciera);
			DataSet ds = transaccionFijaDAO.getDataSet();
			if (ds.next()) {
				if (tipo.equals(DEBITO_VEHICULO)) {
					codigosDeOperacion.put(transaccionFija.concat(vehiculo).concat(instruncionFinanciera), ds.getValue("cod_operacion_veh_deb"));
					codigoOperacion = ds.getValue("cod_operacion_veh_deb");
				} else if (tipo.equals(CREDITO_CONTRAPARTE)) {
					codigosDeOperacion.put(transaccionFija.concat(vehiculo).concat(instruncionFinanciera), ds.getValue("cod_operacion_veh_cre"));
					codigoOperacion = ds.getValue("cod_operacion_veh_cre");
				}
			}
		}
		return codigoOperacion;
	}

	/**
	 * Busca el cliente en INFI, si no lo consigue lo busca en ALTAIR
	 * 
	 * @param rif
	 *            rif a buscar
	 * @param tipoPersona
	 *            tipo de persona J,G,V,E
	 * @return id del cliente encontrado
	 * @throws Exception
	 *             en caso de error
	 */
	private long getCliente(String rif, String tipoPersona) throws Exception {
		long idCliente = 0;

		// Busca el cliente
		clienteDAO.listarPorCedRifyTipoPersona(Integer.parseInt(rif), tipoPersona);
		_cliente = clienteDAO.getDataSet();
		if (_cliente.count() == 0) {
			// Objetos de los servicios
			manejoDeClientes.obtenerClienteAltair(rif, tipoPersona, ip, _app, true, false, true, false, this.nombreUsuario);
			clienteDAO.listarPorCedRifyTipoPersona(Integer.parseInt(rif), tipoPersona);
			_cliente = clienteDAO.getDataSet();
		}

		if (_cliente.count() > 0) {
			_cliente.first();
			_cliente.next();
			idCliente = Long.parseLong(_cliente.getValue("client_id"));
		}
		return idCliente;
	}

	/**
	 * Verifica si existen ordenes rechazadas
	 * 
	 * @param ordenesOperaciones
	 *            una lista de operaciones
	 * @return verdadero en caso que una operación este en rechazo
	 */
	private boolean hayOperacionesEnRechazo(ArrayList<OrdenOperacion> ordenesOperaciones) {
		boolean rechazadas = false;
		for (OrdenOperacion ordenOperacion : ordenesOperaciones) {
			if (ordenOperacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_RECHAZADA) || ordenOperacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_EN_ESPERA)) {
				rechazadas = true;
				break;
			}
		}
		return rechazadas;
	}

	/**
	 * Finaliza el proceso de liquidación sitme
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	private void finalizarProceso() throws Exception {
		String queryProcesoCerrar = procesosDAO.modificar(proceso);
		db.exec(_dso, queryProcesoCerrar);
		logger.info("Finalizado proceso de Liquidacion...." + new Date());
	}

	public void run() {
		try {
			liquidarSitme();
		} catch (Exception e) {
			proceso.setDescripcionError(e.getMessage());
			logger.info("Error procesando la liquidación");
		} catch (Throwable e) {
			logger.info("Error procesando la liquidación");
		}
	}

	/**
	 * Verifica si es la misma unidad a procesar
	 * 
	 * @param unidadAntes
	 *            unidad anterior procesada
	 * @param unidadNueva
	 *            unidad nueva a procesar
	 * @return verdadero en caso de haber procesado ya la unidad
	 */
	private boolean esPosibleProcesar(String unidadAProcesar) {
		boolean continuar = true;
		if (unidadProcesada != null) {
			if (unidadProcesada.equals(unidadAProcesar)) {
				continuar = false;
			}
		}
		unidadProcesada = unidadAProcesar;
		return continuar;
	}

	/**
	 * Genera el mensaje para Carmen
	 * 
	 * @param orden
	 *            objeto orden
	 * @param titulo
	 *            objeto orden de título
	 * @param queriesEjec
	 *            una lista de consultas sql que deben ejecutarse. Se usa para adicionar alli las generadas en este método
	 * @throws Exception
	 *             en caso de error
	 */
	private void generarMensajeCarmen(Orden orden, ArrayList<String> queriesExec) throws Exception {
		System.out.println("generarMensajeCarmen---> antes de obtener cuenta custodia");
		_cuentaCustodia = getCuentaCustodia(orden.getIdCliente());
		if (_cuentaCustodia.count() > 0) {
			// Datos de la cuenta custodia
			_cuentaCustodia.first();
			_cuentaCustodia.next();
			System.out.println("generarMensajeCarmen---> despues de obtener cuenta custodia");
			ArrayList<OrdenTitulo> listaTitulos = orden.getOrdenTitulo();
			for (OrdenTitulo ordenTitulo : listaTitulos) {
				MensajeCarmen mensajeCarmen = new MensajeCarmen();

				// Setear Valores al Mensaje para Interfaz Carmen
				mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, ordenTitulo.getTituloId());
				mensajeCarmen.set(mensajeCarmen.CANTIDAD, ordenTitulo.getUnidades());
				mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, orden.getFechaOrden());// fecha orden ORDENE_PED_FE_ORDEN cambio 29/12/2011 CE
				mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, orden.getFechaValor());// fecha valor
				mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, obtenerCodigoClienteContraparteBDV());
				mensajeCarmen.setUsuarioNM(nmUsuario);
				mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));

				// Establecer valores por defecto al mensaje:
				mensajeDAO.estableceValoresPorDefecto(mensajeCarmen);

				String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);

				for (int k = 0; k < sentenciasMje.length; k++) {
					queriesExec.add(sentenciasMje[k]);
				}
			}
		} else {
			logger.info("No se genero el mensaje CARMEN - El cliente " + orden.getIdCliente() + " no posee cuenta custodia");
		}

	}

	/**
	 * Busca los datos de la cuenta custodia del cliente si existe.
	 * 
	 * @param idCliente
	 *            id del cliente
	 * @return dataSet con los datos de la cuenta custodia del cliente
	 * @throws Exception
	 *             en caso de error
	 */
	public DataSet getCuentaCustodia(long idCliente) throws Exception {
		cuentaCustodiaDAO.getCuentaCustodia(idCliente);
		return cuentaCustodiaDAO.getDataSet();
	}

	/**
	 * Obtiene el código del cliente registrado en CARMEN perteneciente a la contraparte de BDV
	 * 
	 * @return código registrado en INFI
	 * @throws Exception
	 *             en caso de error
	 */
	private int obtenerCodigoClienteContraparteBDV() throws Exception {
		if (customerNumberBDV == 0) {
			String codigo = ParametrosDAO.listarParametros(ParametrosSistema.CUSTOMER_NUMBER_BDV, this._dso);
			if (codigo != null && !codigo.equals("")) {
				customerNumberBDV = Integer.parseInt(codigo);
			}
		}
		return customerNumberBDV;
	}

	public String formatearCedula(String ced) {
		return String.valueOf(Long.valueOf(ced.substring(1, ced.length())));
	}

	public void crearOperacionCreditoBCV() throws Exception{
		
		com.bdv.infi.dao.Transaccion transaccionDao = new com.bdv.infi.dao.Transaccion(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		Statement statement = null;
		ResultSet _ordenesALiquidar = null;
		ArrayList<String> Sql = new ArrayList<String>();
		String[] consultas;
		long idCliente = 0;
		String empresID=null;
		String empresRif=null;
		long idRelacion = 0;
		String rifBancoCentral = ParametrosDAO.listarParametros(ParametrosSistema.RIF_BCV,_dso);
		ClienteDAO clienteDAO=new ClienteDAO(_dso);
		EmpresaDefinicionDAO empresDAO=new EmpresaDefinicionDAO(_dso);
		com.bdv.infi.data.TransaccionFija transaccionFija=new com.bdv.infi.data.TransaccionFija();// .getCodigoOperacionCteCre()
		try {
			
			clienteDAO.buscarPorCedRif(Long.parseLong(rifBancoCentral));
			
			if(clienteDAO.getDataSet().count()>0){
				clienteDAO.getDataSet().first();
				clienteDAO.getDataSet().next();
				idCliente=Long.parseLong(clienteDAO.getDataSet().getValue("client_id"));
			}
			
			empresDAO.listarPorSiglas(SIGLAS_BCV);
			
			if(empresDAO.getDataSet().count()>0){
				empresDAO.getDataSet().first();
				empresDAO.getDataSet().next();
				empresID=empresDAO.getDataSet().getValue("empres_id");
				empresRif=empresDAO.getDataSet().getValue("empres_rif");
			}
			
			transaccionDao.begin();
			statement = transaccionDao.getConnection().createStatement();
			logger.info("Liquidando, Generacion orden credito BCV de " + unidadesInversion.length + " unidades de inversión");
			for (int i = 0; i < this.unidadesInversion.length; i++) {
				if (!esPosibleProcesar(unidadesInversion[i])) {
					continue;
				}
				
				_ordenesALiquidar = statement.executeQuery(ordenDAO.listarMontosGeneracionOrdenBCV(unidadesInversion[i]));
				
				logger.info("Id de unidad a liquidar:" + unidadesInversion[i]);

				while (_ordenesALiquidar.next()) {
					
					// Efectúa el crédito a BCV
					Orden ordenCredito = new Orden();
					//idCliente = getCliente(_ordenesALiquidar.getString("empres_rif").substring(2), _ordenesALiquidar.getString("empres_rif").substring(0, 1));

					if (idCliente == 0) {
						logger.error("Ha ocurrido un problema realizando la busqueda de empresa BCV");
						throw new Exception("Ha ocurrido un problema realizando la busqueda de empresa BCV");
					}

					
					ordenCredito.setIdEmpresa(empresID);
					System.out.println("IdCliente " + idCliente);
					ordenCredito.setIdCliente(idCliente);
					ordenCredito.setFechaOrden(new Date());
					ordenCredito.setFechaValor(new Date());
					ordenCredito.setStatus(StatusOrden.REGISTRADA);
					ordenCredito.setIdTransaccion(TransaccionNegocio.LIQUIDACION);
					//System.out.println("UNIDAD INVERSION " + unidadesInversion[i]);
					ordenCredito.setIdUnidadInversion(Integer.parseInt(unidadesInversion[i]));
					//System.out.println("vehiculo tomador " + _ordenesALiquidar.getString("vehicu_id"));
					ordenCredito.setVehiculoTomador(_ordenesALiquidar.getString("vehicu_id"));
					ordenCredito.setTipoProducto(ID_TIPO_PRODUCTO_SITME);
					if (idRelacion > 0) {
						ordenCredito.setIdOrdenRelacionada(idRelacion);
					}
					
					OrdenOperacion operacionCredito = new OrdenOperacion();
					//System.out.println("Monto de Operacion " + new BigDecimal(_ordenesALiquidar.getString("monto")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
					operacionCredito.setMontoOperacion(new BigDecimal(_ordenesALiquidar.getString("monto")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
					//System.out.println("NUMERO CUENTA EMPRESA " + _ordenesALiquidar.getString("vehicu_numero_cuenta_bcv"));
					String cuentaBCV=_ordenesALiquidar.getString("vehicu_numero_cuenta_bcv");
					if(cuentaBCV==null){
						logger.error("Problema en la generacion de Orden de Credito a BCV: No se ha encontrado el numero de cuenta BCV en el vehiculo");
						throw new Exception("Problema en la generacion de Orden de Credito a BCV: No se ha encontrado el numero de cuenta BCV en el vehiculo");
					} else {
						cuentaBCV=cuentaBCV.trim();
					}
					operacionCredito.setNumeroCuenta(cuentaBCV);
					//System.out.println("Codigo Operacion: " + getCodigoOperacion(String.valueOf(TransaccionFija.CREDITO_CUENTA_EMISOR), _ordenesALiquidar.getString("vehicu_id"), _ordenesALiquidar.getString("insfin_id"), this.CREDITO_CONTRAPARTE));
					
					//operacionCredito.setCodigoOperacion(getCodigoOperacion(String.valueOf(TransaccionFija.CREDITO_CUENTA_EMISOR), _ordenesALiquidar.getString("vehicu_id"), _ordenesALiquidar.getString("insfin_id"), this.CREDITO_CONTRAPARTE));
					
					
					transaccionFija = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.DEPOSITO_BCV,_ordenesALiquidar.getString("vehicu_id"),_ordenesALiquidar.getString("insfin_id"));
					operacionCredito.setCodigoOperacion(transaccionFija.getCodigoOperacionCteCre());
					//System.out.println("Transaccion financiera " + transaccionFija.getCodigoOperacionCteCre());
					operacionCredito.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CREDITO_CUENTA_EMISOR));
					operacionCredito.setFechaAplicar(new Date());
					operacionCredito.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
					operacionCredito.setIdMoneda(monedaLocal); //
					operacionCredito.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
					operacionCredito.setNombreOperacion("Crédito a BCV");
					ordenCredito.agregarOperacion(operacionCredito);
					
					
					
					consultas = ordenDAO.insertar(ordenCredito);
					for (String string : consultas) {						
						Sql.add(string);
					}
					// Actualiza las ordenes relacionadas
					Sql.add(ordenDAO.actualizarOrdenesIdRelacion(ordenCredito.getIdOrden(), _ordenesALiquidar.getLong("empres_id"), unidadesInversion[i],ConstantesGenerales.ID_TIPO_PRODUCTO_SITME));
				}

				if (logger.isDebugEnabled()) {
					Iterator it = Sql.iterator();
					while (it.hasNext()) {
						logger.debug("SQL-->" + it.next());
					}
				}
				db.execBatch(_dso, (String[]) Sql.toArray(new String[Sql.size()]));
			}
		} catch (Exception e) {
			logger.error("Error en la generación de la Orden a BCV", e);
			System.out.println("Error en la generación de la Orden a BCV " + e);
			//throw e;
		} finally {
			if (_ordenesALiquidar != null) {
				_ordenesALiquidar.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (transaccionDao != null) {
				transaccionDao.closeConnection();
			}
		}
	
	}
}
