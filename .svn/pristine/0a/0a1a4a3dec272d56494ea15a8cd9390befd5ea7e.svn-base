package models.intercambio.recepcion.lectura_archivo;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bancovenezuela.comun.data.PrecioRecompra;
import com.bdv.infi.dao.ActividadEconomicaDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SectorProductivoDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenDetalle;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.IngresoOpicsSitme;
import com.bdv.infi.logic.ProcesarDocumentos;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
import com.bdv.infi.logic.interfaz_varias.MensajeEstadistica;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
import com.bdv.infi_toma_orden.data.Cliente;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.bdv.infi_toma_orden.logic.TomaDeOrdenes;

public class LeerArchivo extends Transaccion {

	/**
	 * Parametros de la Toma de Orden
	 */
	private final int ADJUDICADO_CERO = 0;
	private final int ADJUDICADO_IGUAL_SOLICITADO = 1;
	private final int ADJUDICADO_MENOR_SOLICITADO = 3;
	private BigDecimal interesesCaidos = new BigDecimal(0);
	BigDecimal interesesCaidosBs = new BigDecimal(0);
	private com.bdv.infi.dao.Transaccion transaccion;
	SolicitudClavenet ordenOpics = null;

	ArrayList<String> querysTransacciones = new ArrayList<String>();

	OrdenDAO ordenDAOClavenet;

	CuentaCliente clienteCuentaEntidad = null;
	ClienteCuentasDAO clienteCuentasDAO = null;
	ClienteDAO clienteDAOClavenet;
	UnidadInversionDAO unidadInvDAO;
	ControlArchivoDAO controlArchivoDAO;
	TitulosDAO ordenesTitulos;
	EmpresaDefinicionDAO empresaDAO;
	SolicitudClavenet ordenClavenet;
	OrdenDAO ordenCLaveNet;
	ActividadEconomicaDAO actividadEconomicaDAO = null;
	SectorProductivoDAO sectorProductivoDAO = null;

	BigDecimal totalMontoComisionOperaciones = null;
	BigDecimal totalMontoCapitalOperaciones = null;
	// private final int LISTAR_INFORMACION_VEHICULO=3;
	// DecimalFormat form = (DecimalFormat)NumberFormat.getInstance();
	int cantidadOrdenesAdj = 0;
	int acumuladorOrdenesAdj = 0;

	private UnidadInversionDAO unidadInversionDao;
	private HashMap<String, Object> parametrosEntrada = new HashMap<String, Object>();
	private ArrayList<CampoDinamico> ArrayCamposDinamicos = new ArrayList<CampoDinamico>();
	private ArrayList<PrecioRecompra> ArrayRecompraTitulos = new ArrayList<PrecioRecompra>();
	private ArrayList<OrdenOperacion> ArrayComisiones = new ArrayList<OrdenOperacion>();
	TomaOrdenSimulada beanTOSimulada = null;
	private TomaDeOrdenes boSTO;
	// StringBuffer TrazasProceso = new StringBuffer();
	DataSet transaccionesFijasAdjudicacion = new DataSet();
	String codigo_operacion = null;
	String nombre_operacion = null;
	Boolean esBDV;
	Boolean existeOrdenCreditoVehiculo;
	String idOrdenVehiculo = null;
	TomaOrdenDAO tomaOrdenDAO = null;
	private MensajeOpicsDAO mensajeOpicsDAO = null;
	String unidadInversion = null;
	String numero_oficina = null;
	BigDecimal cero = null;
	boolean operacionEncontrada = false;// indicador de operacion encontrada en la lista de operaciones simuladas
	private boolean multiplesUI = false;
	private boolean cobroEnLinea = false;
	private HashMap hashCobroEnLineaPorUI = new HashMap();
	private HashMap hashIdPorUnidad = new HashMap();
	private DataSet mensajesPorRif = new DataSet();
	private HashMap<String, String> hashTituloBCV = new HashMap<String, String>();

	private DataSet dataExtComisiones = new DataSet();// Dataset para almacenar la data extendida referente a la informacion de las comisiones de las ordenes a adjudicar
	private Logger logger = Logger.getLogger(LeerArchivo.class);

	private DataSource dataSource;
	// Cache de transacciones fijas y vehículo
	private HashMap<String, DataSet> transaccionesFijasCache = new HashMap<String, DataSet>();
	private TransaccionFijaDAO fijaDAO;
	private DataSet empresasCache = null;

	// String rifBDV = "";
	Proceso proceso = null;
	ProcesosDAO procesosDAO = null;
	EmpresaDefinicionDAO empresaDefinicionDAO = null;
	private DataSet _cliente = null;

	// private DataSet _unidadInverscion=null;

	public void execute() throws Exception {
		iniciarProceso();
		long t1 = System.currentTimeMillis();
		logger.info("------------------------------------- INICIO TRAZAS DEL PROCESO DE ADJUDICACION -----------------------------------");
		boSTO = new TomaDeOrdenes(_dso);
		tomaOrdenDAO = new TomaOrdenDAO(null, _dso, _app, _req.getRemoteAddr());
		fijaDAO = new TransaccionFijaDAO(_dso);
		mensajeOpicsDAO = new MensajeOpicsDAO(_dso);
		// rifBDV = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.RIF_BDV, this._dso);

		// obtener archivo para ser leido (clase FileInputStream)

		String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
		String nombreDocumento = getSessionObject("nombreDocumento").toString();
		empresaDefinicionDAO = new EmpresaDefinicionDAO(_dso);

		if (!getSessionObject("unidadInversion").equals("0")) {
			logger.info("No es MUI: " + getSessionObject("unidadInversion"));
			unidadInversion = getSessionObject("unidadInversion").toString();
			multiplesUI = false;
		}
		if (getSessionObject("unidadInversion").equals("0")) {
			logger.info("Es MUI  .: " + getSessionObject("unidadInversion"));
			multiplesUI = true;
		}

		unidadInversionDao = new UnidadInversionDAO(_dso);
		if (unidadInversionDao.getInstrumentoFinancieroPorUI(Long.parseLong(unidadInversion)).equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P)) {

			adjudicarSitmeClaveNet();
			return;
		}

		setSessionObject("ejecucion_id", null);
		// System.out.println(" CONTINUA");
		// Determina si es en batch o no el cobro de adjudicación
		setearCobroEnLineaDeLaUnidad();
		numero_oficina = (String) _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);

		FileInputStream documento = new FileInputStream(contenidoDocumento);// contenido del documento a leer
		String xlsNombre = nombreDocumento;// nombre del archivo que estamos leyendo

		HSSFSheet hoja = null;// hoja
		HSSFCell celdaControl = null;// primera celda o columna
		HSSFCell ui = null;// columna Unidad de Inversion
		HSSFCell registro = null;// columna cedula
		HSSFCell cedula = null;// columna cedula
		HSSFCell montoCompra = null;// columna monto que compro el cliente
		HSSFCell precioPacto = null;// columna precio que compro el cliente
		HSSFCell precioNegociado = null;// columna precio asignado de venta al cliente
		HSSFCell montoAsignado = null;// columna monto adjudicado

		HSSFCell contraparte = null;// columna de contraparte
		String empresa_id = null;
		HSSFCell broker = null;// columna de broker
		HSSFCell consecutivo = null;// columna de consecutivo

		HSSFRow row = null; // fila
		boolean indicaFinanciada = false;
		String statusOrdenLeida = null;// Id del cliente
		String idCliente = null;// Id del cliente
		// String nameCliente = null;//Nombre del cliente
		String ced_cliente = null;// Cedula del cliente
		String status_orden = null; // estatus de la orden
		String status = null;// estatus de la operacion
		String transaccionFinanciera = "";// Tipo de transaccion financiera
		String idBlotter = null;// id del blotter
		String financiado = null;// indicador de financiamiento
		String numeroCedula = null;
		String tipoPersona = null;// id tipo de persona
		String veh_colocador = null;// id vehiculo colocador
		String veh_tomador = null;// id vehiculo tomador
		String numeroCuentaVehTomador = null;
		String veh_recomprador = null;// id vehiculo recomprador
		String cuenta = null;// numero cuenta cliente. Obtenida de la operacion
		String ordenIdArchivo = null;
		// String cuentaCliente = null;//numero cuenta cliente. Obtenida de la orden
		String monedaLocal = null;
		String transa_id = null;
		BigDecimal preciOfrecido = new BigDecimal(0);// precio ofrecido para la compra reuperado de base de dato
		BigDecimal pctFinanciado = new BigDecimal(0);// Porcentaje de financiamiento
		BigDecimal montoPedido = new BigDecimal(0);// monto que pidio el cliente
		BigDecimal gananciaRedBlotter = new BigDecimal(0);
		BigDecimal montoAdjudicado = new BigDecimal(0); // monto adjudicado
		BigDecimal montoPendiente = new BigDecimal(0); // monto pendiente por cobrar (financiado)
		BigDecimal saldoCtaCliente = new BigDecimal(0);// saldo de la cuenta
		BigDecimal divisor = new BigDecimal(100);// dividir entre 100, usado para calculo de porcentaje
		cero = new BigDecimal(0);// dividir entre 100, usado para calculo de porcentaje
		String ejecucion_id = MSCModelExtend.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS); // Tomamos la secuencia fuera del for para que todos los registros tengan el mismo valor
		DataSet _record = new DataSet();
		_record.append("idunidadInversion", java.sql.Types.VARCHAR);

		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		ControlArchivoDAO controlDAO = new ControlArchivoDAO(_dso);
		controlDAO.ListarExiste(Long.parseLong(ejecucion_id));
		boolean insertadoArchivoRecepcion = false;

		FactoryAltair factoryAltair = new FactoryAltair(_dso, _app, true);
		factoryAltair.nombreUsuario = getUserName();
		factoryAltair.ipTerminal = _req.getRemoteAddr();

		// abrir el archivo xls
		HSSFWorkbook libro = new HSSFWorkbook(documento);
		// leyendo por cada hoja del Libro

		for (int i = 0; i < libro.getNumberOfSheets(); i++) { // hojas
			// Obtener la hoja i del Libro
			hoja = libro.getSheet(libro.getSheetName(i));// objeto hoja
			// vehiculo = libro.getSheetName(i); //se tomaba el vehiculo tomador del nombre de la pestaña
			// recorrer filas (registros)
			logger.info("********** INICIO ADJUDICACION *********\n");

			// Llenamos dataset con las transacciones fijas para el Proceso, seran usadas a lo largo de la clase
			// TransaccionFijaDAO fijaDAO = new TransaccionFijaDAO(_dso);
			// fijaDAO.listarTransaccionesFijasAdjudicacion(unidadInversion);
			// transaccionesFijasAdjudicacion = fijaDAO.getDataSet();

			// Listamos la moneda local, sera usada durante toda la clase
			MonedaDAO monedaDAO = new MonedaDAO(_dso);
			monedaLocal = monedaDAO.listarIdMonedaLocal();

			for (int j = 1; j < hoja.getPhysicalNumberOfRows(); j++) {// filas
				long inicioProcesarOrden = System.currentTimeMillis();

				pctFinanciado = new BigDecimal(0);// Porcentaje de financiamiento
				montoPedido = new BigDecimal(0);// monto que pidio el cliente
				gananciaRedBlotter = new BigDecimal(0);
				montoAdjudicado = new BigDecimal(0); // monto adjudicado
				montoPendiente = new BigDecimal(0); // monto pendiente por cobrar (financiado)
				saldoCtaCliente = new BigDecimal(0);// saldo de la cuenta

				String idOrden = null;// Id de la orden, declara aqui para que se blanquee al cambiar de fila
				String transaccionNegocioOrden = "";
				try {
					row = hoja.getRow(j);// Obtener la fila j de la hoja
					// vas leyendo por cada celda (columna) de la fila
					// para cada fila el numero de columnas es fijo
					celdaControl = row.getCell((short) 0);
					int tipo = celdaControl.getCellType();
					if (tipo == HSSFCell.CELL_TYPE_BLANK) {// romperemos la lectura de filas al encontrar la primera celda vacia (fin de registros)
						if (j == 1) {
							j = hoja.getPhysicalNumberOfRows();
							logger.info("* Archivo sin Registros para Procesar o Mal Formado, Verifique e Intente Nuevamente)\n");
							break;
						}
						if (j > 1) {
							j = hoja.getPhysicalNumberOfRows();
							// Falta: validar que si la lista de mensajes del proceso es <=0 entonces la lectura fue exitosa para todos los registros
							// sino solo mostrar mensaje de lectura finalizada con las siguientes observaciones y mostrar los mensajes
							logger.info("* Fin de Lectura de Registros Exitosamente.\n");
							break;
						}
					} else {
						// seguimos recorriendo si hay filas con registros
						if ((multiplesUI) == true) {
							ui = row.getCell((short) 0);
							registro = row.getCell((short) 1);
							cedula = row.getCell((short) 2);// obtenemos valor
							precioPacto = row.getCell((short) 3);
							precioNegociado = row.getCell((short) 4);
							montoCompra = row.getCell((short) 5);
							montoAsignado = row.getCell((short) 6);

							contraparte = row.getCell((short) 7); // Rif de la contraparte
							broker = row.getCell((short) 8);
							consecutivo = row.getCell((short) 9);

							empresa_id = verificarContraparte(contraparte.toString());
							// if (empresa_id == null){
							// mensajesPorRif.addNew();
							// mensajesPorRif.setValue("orden", registro.toString());
							// mensajesPorRif.setValue("mensaje", "Rif " + contraparte.toString() + " no ha sido encontrado como empresa." );
							// logger.info( "Rif " + contraparte.toString() + " no ha sido encontrado como empresa, se continúa con la siguiente orden");
							// continue; //Pasa a otra orden
							// }

							obtenerDatosDeLaUnidadDeInversionPorNombre(String.valueOf(ui.getRichStringCellValue()));
							_record.addNew();
							_req.getSession().setAttribute("unidadInversion", unidadInversion);
							_record.setValue("idunidadInversion", (unidadInversion));
							// Llenamos dataset con las transacciones fijas para el Proceso, seran usadas a lo largo de la clase
							_req.getSession().setAttribute("unidadInversion", unidadInversion);
							getTransaccionesFijas(unidadInversion);
							// fijaDAO.listarTransaccionesFijasAdjudicacion(unidadInversion,multiplesUI);
							// transaccionesFijasAdjudicacion = fijaDAO.getDataSet();
						}
						if ((multiplesUI) == false) {
							registro = row.getCell((short) 0);
							cedula = row.getCell((short) 1);// obtenemos valor
							montoCompra = row.getCell((short) 2);
							montoAsignado = row.getCell((short) 3);
							getTransaccionesFijas(unidadInversion);
							// if (!obtuvoTransaccionesFijas){
							// _req.getSession().setAttribute("unidadInversion", unidadInversion);
							// fijaDAO.listarTransaccionesFijasAdjudicacion(unidadInversion,multiplesUI);
							// transaccionesFijasAdjudicacion = fijaDAO.getDataSet();
							// obtuvoTransaccionesFijas = true;
							// }
						}

						int tipoCed = cedula.getCellType(); // obtenemos el tipo de celda de cedula
						if (tipoCed == HSSFCell.CELL_TYPE_STRING) {
							ced_cliente = String.valueOf(cedula.getRichStringCellValue());
						}
						ordenIdArchivo = registro.toString().replaceAll("\\.0", "");
						numeroCedula = ced_cliente.substring(1);
						tipoPersona = ced_cliente.substring(0, 1);
						// Traer de la base de datos . . . . . . . .
						// montoPedido = new BigDecimal(montoCompra.getNumericCellValue());
						montoAdjudicado = new BigDecimal(montoAsignado.getNumericCellValue());
						if (logger.isDebugEnabled()) {
							logger.info("* Leyendo Orden(" + ordenIdArchivo + ")");
						}
					}

					// HAREMOS UNA SERIE DE VALIDACIONES PARA VERIFICAR QUE LOS REGISTROS LEIDOS ESTAN EN LA TABLA 201 Y 204

					// 2.-buscamos si existe una orden asociada al cliente, con ese monto de compra, y ese precio de corte, en esa unidad e inversion
					if (logger.isDebugEnabled()) {
						logger.debug("Orden: " + ordenIdArchivo + " * Monto asignado establecido en la hoja excel: " + montoAdjudicado);
					}
					long tiempoLo = System.currentTimeMillis();
					Orden ordenOriginal = ordenDAO.listarOrden(Long.parseLong(ordenIdArchivo));
					logger.info("Tiempo de listado de orden " + ordenIdArchivo + " " + (System.currentTimeMillis() - tiempoLo) + " miliseg");
					// ordenDAO.listarOrdenRecibida(ordenIdArchivo, Integer.parseInt(unidadInversion),multiplesUI );
					// 1DataSet _orden = ordenDAO.getDataSet();
					// Recorremos todas las ordenes de la UI.
					if (ordenOriginal.getIdOrden() > 0) {
						// 1if (_orden.count()>0){
						// 1_orden.first();
						// 1_orden.next();
						// 1parametrosEntrada.put("fechaValor",_orden.getValue("ordene_ped_fe_valor"));
						// 1//parametrosEntrada.put("fechaRecompra",_ordenId.getValue("ordene_fe_ult_act"));
						// 1idOrden=_orden.getValue("ordene_id");
						// 1idCliente=_orden.getValue("client_id");
						// 1transaccionNegocioOrden = _orden.getValue("transa_id");
						// 1statusOrdenLeida=_orden.getValue("ordsta_id");
						// 1montoPendiente= new BigDecimal(_orden.getValue("ordene_ped_total_pend"));
						// 1logger.info("* Encontrado en la tabla 204 bajo el numero de orden("+idOrden+") y status: "+statusOrdenLeida);
						// 1logger.info("* Orden para al proceso de Adjudicacion . . . .");
						// 1montoPedido = new BigDecimal(_orden.getValue("ordene_ped_monto"));

						parametrosEntrada.put("fechaValor", Utilitario.DateToString(ordenOriginal.getFechaValor(), "dd-MM-yyyy"));
						// parametrosEntrada.put("fechaRecompra",_ordenId.getValue("ordene_fe_ult_act"));
						idOrden = String.valueOf(ordenOriginal.getIdOrden());
						idCliente = String.valueOf(ordenOriginal.getIdCliente());
						transaccionNegocioOrden = ordenOriginal.getIdTransaccion();
						statusOrdenLeida = ordenOriginal.getStatus();
						montoPendiente = new BigDecimal(ordenOriginal.getMontoPendiente());
						montoPedido = new BigDecimal(ordenOriginal.getMonto());
					}

					// 2.-Validar el client_id asociado a la orden
					long tiempoCli = System.currentTimeMillis();
					String rifCedula = clienteDAO.buscarDatosPorIdCliente(idCliente);
					_cliente = clienteDAO.getDataSet();
					logger.info("Tiempo de buscar cliente " + ordenIdArchivo + " " + (System.currentTimeMillis() - tiempoCli) + " miliseg");
					if (numeroCedula != null && !numeroCedula.equals("") && rifCedula != null && !rifCedula.equals("")) {
						if (Long.parseLong(numeroCedula) != Long.parseLong(rifCedula)) {
							idCliente = null;
							logger.info("* El cliente con Cedula/RIF encontrada en el archivo: " + numeroCedula + " no corresponde con la encontrada en la orden: " + rifCedula);
							continue;
						}
					}

					// Recorremos todas las ordenes
					if ((idCliente != null) && (idOrden != null) || (statusOrdenLeida.equals(StatusOrden.ENVIADA)) || (((statusOrdenLeida.equals(StatusOrden.REGISTRADA)) && multiplesUI == true))) {
						Orden orden = new Orden();
						Orden ordenCopia = new Orden();
						// Archivo archivo = new Archivo();

						Boolean bloqueoAdjMenor = false;

						boolean indicador = false;
						boolean indicador2 = false;
						boolean miscelaneo = false;

						orden.setIdCliente(Long.parseLong(idCliente));
						orden.setIdOrden(Long.parseLong(idOrden));
						orden.setMontoAdjudicado(montoAdjudicado.doubleValue());
						orden.setFechaAdjudicacion(new Date());

						OrdenDataExt ordenDataExt = new OrdenDataExt();

						if ((statusOrdenLeida.equals(StatusOrden.ENVIADA) || statusOrdenLeida.equals(StatusOrden.REGISTRADA)) && ordenOriginal.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)) {
							// Sitme
							ordenCopia.setContraparte(empresa_id);
							if (broker != null) {
								ordenDataExt.setIdData(DataExtendida.BROKER);
								ordenDataExt.setValor(broker.toString());
								ordenCopia.agregarOrdenDataExt(ordenDataExt);
							}
							if (consecutivo != null) {
								ordenDataExt = new OrdenDataExt();
								ordenDataExt.setIdData(DataExtendida.CONSECUTIVO);
								ordenDataExt.setValor(consecutivo.toString());
								ordenCopia.agregarOrdenDataExt(ordenDataExt);
							}
						}

						// BUSCAMOS EL VEHICULO TOMADOR RE ASOCIADO A LA ORDEN
						transa_id = ordenOriginal.getIdTransaccion();
						veh_tomador = ordenOriginal.getVehiculoTomador();

						// MEJORAS
						if (transaccionesFijasAdjudicacion.count() > 0) {
							transaccionesFijasAdjudicacion.first();
							while (transaccionesFijasAdjudicacion.next()) {
								if (transaccionesFijasAdjudicacion.getValue("vehicu_id").equals(veh_tomador)) {
									numeroCuentaVehTomador = transaccionesFijasAdjudicacion.getValue("vehicu_numero_cuenta");
									esBDV = Boolean.valueOf(transaccionesFijasAdjudicacion.getValue("esbdv"));
									break;
								}
							}
						}

						Orden ordenAlVehiculo = null;
						if (!esBDV) {
							ordenAlVehiculo = tomaOrdenDAO.crearBuscarOrdenVehiculoUI(veh_tomador, _req.getRemoteAddr(), Long.parseLong(unidadInversion), getUserName(), cero.doubleValue(), numero_oficina);
						}
						// FIN MEJORAS

						status_orden = ordenOriginal.getStatus();
						// cuentaCliente=_datosOrden.getValue("ctecta_numero");
						// archivo.setVehiculoId(veh_tomador);

						// DETALLE DE LA ORDEN RECIBIDA EN EL ARCHIVO A SER GUARDADA EN CONTROL DE DETALLE DE ARCHIVO
						// Detalle detalle = new Detalle();
						// detalle.setIdOrden(Long.parseLong(idOrden));
						// archivo.agregarDetalle(detalle);
						ArrayList<String> querys = new ArrayList<String>();

						// Operaciones originales de la orden
						OperacionDAO operacionDAO = new OperacionDAO(_dso);
						long tiempoBoo = System.currentTimeMillis();
						operacionDAO.listarOperacionOrden(Long.parseLong(idOrden));
						logger.info("Tiempo de buscar operaciones de orden " + ordenIdArchivo + " " + (System.currentTimeMillis() - tiempoBoo) + " miliseg");
						// _ordenOperacion posee las operaciones de la orden . . . .
						DataSet _ordenOperacion = operacionDAO.getDataSet();

						if ((status_orden.equals(StatusOrden.ENVIADA)) || ((status_orden.equals(StatusOrden.REGISTRADA)) && multiplesUI == true)) {

							// Ciclo si el monto adjudicado es IGUAL al monto soliciado
							if (montoAdjudicado.compareTo(montoPedido) == 0) {// entra al ciclo si el monto adjudicado es IGUAL al monto pedido
								long inicioMontoIgual = System.currentTimeMillis();
								logger.debug("* Se Adjudica todo el monto Pedido");
								ordenCopia.setMontoPendiente(montoPendiente.doubleValue());
								if (transa_id.equals(TransaccionNegocio.TOMA_DE_ORDEN)) {
									// ciclo de recorrido de la operaciones
									if (_ordenOperacion.count() > 0) {
										_ordenOperacion.first();
										// Recorro DATASET de operaciones de la orden encontrada
										while (_ordenOperacion.next()) {
											transaccionFinanciera = _ordenOperacion.getValue("trnf_tipo");
											// ciclo de cambio de tipo de operacion . . . .
											if (transaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO) || transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO)) {
												if (logger.isDebugEnabled()) {
													logger.debug("* La operacion de tipo (" + transaccionFinanciera + ") pasara a ser de tipo (" + TransaccionFinanciera.DEBITO + ")");
												}
												OrdenOperacion operacion = new OrdenOperacion();
												// cambia tipo de operacion . . . .
												operacion = crearContraparteOperacion(new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("monto_operacion"))), new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")),
														_ordenOperacion.getValue("moneda_id"), _ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"),
														_ordenOperacion.getValue("ctecta_bcocta_telefono"), _ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"), _ordenOperacion.getValue("ctecta_bcoint_bic"),
														_ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")), Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")), _ordenOperacion.getValue("numero_retencion"),
														TransaccionFinanciera.DEBITO, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), false, ConstantesGenerales.STATUS_EN_ESPERA, _ordenOperacion.getValue("operacion_nombre"), Utilitario.StringToDate(_ordenOperacion.getValue("fecha_aplicar"), ConstantesGenerales.FORMATO_FECHA));
												if (transaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)) {
													operacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
												} else if (transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO)) {
													operacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
												}

												if (!transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO)) {
													orden.agregarOperacion(operacion);// las operaciones seran actualizadas
												}
												ordenCopia.agregarOperacion(operacion);// las operaciones seran actualizadas
											}// fin if transaccionFinanciera
											if (transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO)) {
												miscelaneo = true;
											}
										}// fin while recorrido operaciones . . . .
									}// fin de ciclo de las operaciones . . . .
								}
								logger.info("Tiempo de procesamiento de MONTO IGUAL ENVIADA" + (System.currentTimeMillis() - inicioMontoIgual) + " miliseg ");

							}// FIN de Ciclo si el monto adjudicado es IGUAL al monto soliciado

							// INICIO si el monto adjudicado es MENOR al monto pedido soliciado
							else if ((montoAdjudicado.compareTo(montoPedido) < 0) || (multiplesUI == true)) {
								long inicioMontoMenor = System.currentTimeMillis();
								// entra al ciclo si el monto adjudicado es MENOR al monto pedido
								if (_ordenOperacion.count() > 0) {
									_ordenOperacion.first();
									_ordenOperacion.next();
									cuenta = _ordenOperacion.getValue("ctecta_numero");
								}
								// Titulos originales de la orden
								TitulosDAO tituloDAO = new TitulosDAO(_dso);
								long tiempoBto = System.currentTimeMillis();
								tituloDAO.listarDatosTituloOrden(idOrden);
								logger.info("Tiempo de buscar titulos de orden " + ordenIdArchivo + " " + (System.currentTimeMillis() - tiempoBto) + " miliseg");
								DataSet ordenTitulos = tituloDAO.getDataSet();

								// - - - - - - - - - - - - - - - - Inicio de monto adjudicado igual a CERO
								if (montoAdjudicado.doubleValue() == cero.doubleValue()) {
									logger.debug("* No se adjudico monto a la orden: ");
									if (ordenTitulos.count() > 0) {
										ordenTitulos.first();
										OrdenTitulo ordenTitulo = new OrdenTitulo();
										while (ordenTitulos.next()) {
											ordenTitulo.setTituloId(ordenTitulos.getValue("titulo_id"));
											ordenTitulo.setMontoIntCaidos(new BigDecimal(ordenTitulos.getValue("titulo_mto_int_caidos")));
											ordenTitulo.setUnidades(cero.doubleValue());
											ordenTitulo.setMonto(cero.doubleValue());
											ordenTitulo.setMontoNeteo(new BigDecimal(ordenTitulos.getValue("titulo_mto_neteo")));
											querys.add(tituloDAO.modificarOrdenTitulos(ordenTitulo, orden));// modificamos los titulos en base al monto adjudicado
											orden.agregarOrdenTitulo(ordenTitulo);
											ordenCopia.agregarOrdenTitulo(ordenTitulo);
											ordenCopia.setComisionCero(true);// ITS-638
											orden.setComisionCero(true);// ITS-638
										}
									}
								}// - - - - - - FIN de monto adjudicado igual a CERO

								// Inicio de monto adjudicado > a CERO o menor precio Negociado.
								else if (montoAdjudicado.doubleValue() > cero.doubleValue()) {
									logger.debug("* Se adjudico a la orden menos del monto Solicitado, o menor precio Negociado");
									if (ordenOriginal.getIdOrden() > 0) {
										idEmpresa = ordenOriginal.getIdEmpresa();
										idBlotter = ordenOriginal.getIdBloter();
										idMoneda = ordenOriginal.getIdMoneda();
										veh_colocador = ordenOriginal.getVehiculoColocador();
										veh_recomprador = ordenOriginal.getVehiculoRecompra();
										if (multiplesUI == true) {
											preciOfrecido = new BigDecimal(precioNegociado.getNumericCellValue());
										}
										if (multiplesUI == false) {
											preciOfrecido = new BigDecimal(ordenOriginal.getPrecioCompra());
										}
										gananciaRedBlotter = ordenOriginal.getGananciaRed();
										financiado = ordenOriginal.getFinanciada() ? "1" : "0";
										if (Integer.parseInt(financiado) == ConstantesGenerales.VERDADERO) {
											indicaFinanciada = true;
										}
									}// fin if count _datosOrden

									// Inicio de corrido de las operacion by VR
									if (_ordenOperacion.count() > 0) {
										_ordenOperacion.first();
										// buscar data extendida de comisiones
										// Buscar la data extendida de la orden asociada a las comisiones de la UI
										long tiempoDte = System.currentTimeMillis();
										ordenDAO.listarRegistrosDataExtendida(ordenOriginal.getIdOrden(), DataExtendida.ID_COMISION_UI);
										logger.info("Tiempo de buscar dataextendida de orden " + ordenIdArchivo + " " + (System.currentTimeMillis() - tiempoDte) + " miliseg");
										dataExtComisiones = ordenDAO.getDataSet();
										ArrayList<OrdenOperacion> ArrComisiones = null;
										ArrComisiones = new ArrayList<OrdenOperacion>();
										while (_ordenOperacion.next()) {
											OrdenOperacion ordenOpera = new OrdenOperacion();
											ordenOpera.setTasa(new BigDecimal(_ordenOperacion.getValue("tasa")));
											ordenOpera.setIdTransaccionFinanciera(_ordenOperacion.getValue("trnfin_id"));
											// --setear id comisionUI de operacion para diferenciar cada una de las comisiones
											if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("1")) {
												buscarIdComisionUI(_ordenOperacion, ordenOpera);
												// setear id de comisionUI al dataset original de operaciones
												_ordenOperacion.setValue("id_comision_ui", String.valueOf(ordenOpera.getIdOperacion()));
											}
											// ----------------------------------------------------------------------------

											ArrComisiones.add(ordenOpera);
										}// fin de While . . . .
										ArrayComisiones = ArrComisiones;
									}// FIN de recorrido de operaciones de la orden . . . .

									// Buscamos los titulos pactados para recompra con neteo
									if (ordenTitulos.count() > 0) {
										ordenTitulos.first();
										ArrayList<PrecioRecompra> ArrRecompraTitulos = null;
										ArrRecompraTitulos = new ArrayList<PrecioRecompra>();
										while (ordenTitulos.next()) {
											if (new BigDecimal(ordenTitulos.getValue("titulo_mto_neteo")).compareTo(cero) > ConstantesGenerales.FALSO) {// si EL CAMPO DE NETEO ES MAYOR A CERO LO ANEXAMOS AL ARRAY SINO NO)
												PrecioRecompra tituPrecioRecompra = new PrecioRecompra();
												tituPrecioRecompra.setTituloId(ordenTitulos.getValue("titulo_id"));
												tituPrecioRecompra.setTitulo_precio_recompra(new BigDecimal(ordenTitulos.getValue("titulo_pct_recompra")));
												tituPrecioRecompra.setTasaPool(new BigDecimal(ordenTitulos.getValue("titulo_precio_mercado")));

												ArrRecompraTitulos.add(tituPrecioRecompra);
											}
										}
										ArrayRecompraTitulos = ArrRecompraTitulos;
									}// fin de recorrido de titulos pactados para recompra con neteo

									parametrosEntrada.put("estadoCasado", "NO");
									parametrosEntrada.put("cedulaConyuge", null);
									parametrosEntrada.put("tipoPersonaConyuge", null);
									long tiempoDte2 = System.currentTimeMillis();
									ordenDAO.listarRegistrosDataExtendida(Long.parseLong(idOrden));
									logger.info("Tiempo de buscar dataextendida de orden 2" + ordenIdArchivo + " " + (System.currentTimeMillis() - tiempoDte2) + " miliseg");

									DataSet _datoFinanciamiento = ordenDAO.getDataSet();
									if (_datoFinanciamiento.count() > 0) {
										_datoFinanciamiento.first();
										while (_datoFinanciamiento.next()) {
											if (_datoFinanciamiento.getValue("dtaext_id").equals(DataExtendida.PCT_FINANCIAMIENTO)) {
												pctFinanciado = new BigDecimal(_datoFinanciamiento.getValue("dtaext_valor"));
											} else if (_datoFinanciamiento.getValue("dtaext_id").equals(DataExtendida.CED_CONYUGE)) {
												parametrosEntrada.put("estadoCasado", "SI");
												parametrosEntrada.put("cedulaConyuge", _datoFinanciamiento.getValue("dtaext_valor").substring(1));
												parametrosEntrada.put("tipoPersonaConyuge", _datoFinanciamiento.getValue("dtaext_valor").substring(0, 1));
											}
										}
									}
									parametrosEntrada.put("comisionesOrden", ArrayComisiones);
									parametrosEntrada.put("idUnidadInversion", Long.parseLong(unidadInversion));
									parametrosEntrada.put("idBlotter", idBlotter);
									parametrosEntrada.put("tipoPersona", tipoPersona);
									parametrosEntrada.put("cedulaCliente", ced_cliente.substring(1));
									parametrosEntrada.put("montoInversion", montoAdjudicado);
									parametrosEntrada.put("montoPedido", montoAdjudicado);
									parametrosEntrada.put("cantidadComprar", new Long(0));
									parametrosEntrada.put("precioOfrecido", preciOfrecido);
									parametrosEntrada.put("numeroCuentaCliente", cuenta);
									parametrosEntrada.put("porcentajeFinanciado", pctFinanciado);
									parametrosEntrada.put("camposDinamicos", ArrayCamposDinamicos);
									parametrosEntrada.put("servletContext", _app);
									parametrosEntrada.put("ipTerminal", _req.getRemoteAddr());
									parametrosEntrada.put("idVehiculoTomador", veh_tomador);
									parametrosEntrada.put("idVehiculoColocador", veh_colocador);
									parametrosEntrada.put("idVehiculoRecompra", veh_recomprador);
									parametrosEntrada.put("userName", getUserName());
									parametrosEntrada.put("IndicadorFinanciada", new Boolean(indicaFinanciada));
									parametrosEntrada.put("recompraTitulos", ArrayRecompraTitulos);
									parametrosEntrada.put("gananciaRedBlotter", gananciaRedBlotter);
									parametrosEntrada.put("saldoCtaCliente", saldoCtaCliente);
									parametrosEntrada.put("adjudicacion", "SI");
									// tipo de Transaccion de Negocio
									parametrosEntrada.put("tipoTransaccionNegocio", transaccionNegocioOrden);
									Cliente cliente = new Cliente();
									cliente.setIdCliente(Long.parseLong(idCliente));
									cliente.setTipoPersona(tipoPersona);
									// ArrayList listaMensajes = new ArrayList();
									try {
										boSTO.setParametrosEntrada(parametrosEntrada);
										boSTO.setCliente(cliente);

										long t = System.currentTimeMillis();
										logger.info("---------------------- Star llamada de simuladorTO() ----------------------");
										beanTOSimulada = boSTO.simuladorTO();
										parametrosEntrada.clear();
										logger.info("---------------------- Retorno de simuladorTO(), tiempo Simulado: " + (System.currentTimeMillis() - t) + "----------------------");
									} catch (Exception e) {
										logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
										throw new Exception(e);
									} catch (Throwable e) {
										logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
										throw new Exception(e);
									}

									OrdenTitulo ordenTitulo = new OrdenTitulo();
									ArrayList<com.bdv.infi_toma_orden.data.OrdenTitulo> titulosOrden = beanTOSimulada.getListaOrdenTitulo();
									for (Iterator<com.bdv.infi_toma_orden.data.OrdenTitulo> iter = titulosOrden.iterator(); iter.hasNext();) {
										com.bdv.infi_toma_orden.data.OrdenTitulo titulo = (com.bdv.infi_toma_orden.data.OrdenTitulo) iter.next();
										BigDecimal pct = titulo.getPorcentaje();
										BigDecimal calculo = pct.divide(divisor);
										BigDecimal monto = montoAdjudicado.multiply(calculo);

										ordenTitulo.setTituloId(titulo.getIdTitulo());
										ordenTitulo.setMontoIntCaidos(titulo.getMontoIntCaidos());
										ordenTitulo.setUnidades(titulo.getUnidadesInvertidas().doubleValue());
										ordenTitulo.setMonto(monto.doubleValue());
										ordenTitulo.setMontoNeteo(titulo.getMontoNeteo());

										querys.add(tituloDAO.modificarOrdenTitulos(ordenTitulo, orden));// modificamos los titulos en base al monto adjudicado
										orden.agregarOrdenTitulo(ordenTitulo);
										ordenCopia.agregarOrdenTitulo(ordenTitulo);
									}
									ordenDataExt = new OrdenDataExt();
									ArrayList<OrdenDataExt> dataExtendida = beanTOSimulada.getOrdenDataExt();
									for (Iterator<OrdenDataExt> iter = dataExtendida.iterator(); iter.hasNext();) {
										OrdenDataExt dataExt = (OrdenDataExt) iter.next();
										String dataDescripcion = dataExt.getIdData();
										if (dataDescripcion.equals(DataExtendida.PCT_FINANCIAMIENTO)) {
											ordenDataExt.setIdData(dataDescripcion);
											ordenDataExt.setIdOrden(Long.parseLong(idOrden));
											ordenDataExt.setValor(dataExt.getValor());
											ordenCopia.agregarOrdenDataExt(ordenDataExt);
											orden.agregarOrdenDataExt(ordenDataExt);
										}
									}
									orden.setComisionOperacion(beanTOSimulada.getComisionOperacion());
									orden.setMontoInteresCaidos(beanTOSimulada.getMontoInteresCaidos().doubleValue());
									orden.setMontoTotal(beanTOSimulada.getMontoTotal().doubleValue());
									orden.setMontoCobrado(beanTOSimulada.getMontoInversion().doubleValue());
									orden.setMontoComisionOrden(beanTOSimulada.getMontoComisiones());
									orden.setMontoFinanciado(beanTOSimulada.getMontoFinanciado().doubleValue());
									orden.setMontoPendiente(beanTOSimulada.getMontoFinanciado().doubleValue());
									ordenCopia.setComisionOperacion(beanTOSimulada.getComisionOperacion());
									ordenCopia.setMontoInteresCaidos(beanTOSimulada.getMontoInteresCaidos().doubleValue());
									ordenCopia.setMontoTotal(beanTOSimulada.getMontoTotal().doubleValue());
									ordenCopia.setMontoCobrado(beanTOSimulada.getMontoInversion().doubleValue());
									ordenCopia.setMontoComisionOrden(beanTOSimulada.getMontoComisiones());
									ordenCopia.setMontoFinanciado(beanTOSimulada.getMontoFinanciado().doubleValue());
									ordenCopia.setMontoPendiente(beanTOSimulada.getMontoFinanciado().doubleValue());
									// Seteo precio de Compra . . .
									ordenCopia.setPrecioCompra(beanTOSimulada.getPrecioCompra().doubleValue());
									logger.debug("* FIN de adjudicacion de la orden por menos del monto Solicitado, o menor precio Negociado");
								}// FIN montoAdjudicado.doubleValue()>cero.doubleValue()

								// Buscamos las operaciones existentes de la orden para aplicar la devolucion de dinero
								logger.info("* -------- INICIO DE CREACION OPERACIONES ----------");

								if (_ordenOperacion.count() > 0) {
									_ordenOperacion.first();
									// Recorro DATASET de operaciones de la orden encontrada
									logger.debug("* Operaciones para devolucion del dinero por concepto de bloqueos hechos en TO.");
									while (_ordenOperacion.next()) {
										transaccionFinanciera = _ordenOperacion.getValue("trnf_tipo");
										status = String.valueOf(_ordenOperacion.getValue("status_operacion"));
										if (transaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)) {
											logger.debug("********** OPERACIONES AL CLIENTE ************");
											logger.debug("* Encontrada operacion de Tipo(" + TransaccionFinanciera.BLOQUEO + ") . . .");
											if (status.equals(ConstantesGenerales.STATUS_APLICADA)) {
												// si la operacion era de bloqueo mandar a altair las operaciones con debito para asegurar que se retenga el dinero
												// Luego madar contra altair y genera una operacion de credito con la diferencia!!!
												OrdenOperacion operacion = new OrdenOperacion();
												String transaccion_financ = null;
												if (montoAdjudicado.doubleValue() > cero.doubleValue()) {
													operacion.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
													transaccion_financ = TransaccionFinanciera.DEBITO;
												} else if (montoAdjudicado.doubleValue() == cero.doubleValue()) {// SE ADJUDICO cero(0)
													// para las comisiones de monto fijo se debe crear el debito: se cobran igual
													// todas las demas operaciones se desbloquean (capital y comisiones de porcentaje variables e invariables)
													if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("1") && esComisionMontoFijo(_ordenOperacion.getValue("monto_operacion"), _ordenOperacion.getValue("tasa"))) {

														ordenCopia.setComisionCero(false);// ITS-638
														orden.setComisionCero(false);// ITS-638
														// Se creara operacion de Debito ya que se cobrar de todos modos por ser comision monto fijo:
														operacion.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
														transaccion_financ = TransaccionFinanciera.DEBITO;
													} else {// si no, se desbloqueara el monto para los demas tipos de operaciones
														operacion.setTipoTransaccionFinanc(TransaccionFinanciera.DESBLOQUEO);
														transaccion_financ = TransaccionFinanciera.DESBLOQUEO;
													}
												}

												operacion = crearContraparteOperacion(new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("monto_operacion"))), new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")),
														_ordenOperacion.getValue("moneda_id"), _ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"),
														_ordenOperacion.getValue("ctecta_bcocta_telefono"), _ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"), _ordenOperacion.getValue("ctecta_bcoint_bic"),
														_ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")), Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")), _ordenOperacion.getValue("numero_retencion"),
														transaccion_financ, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), Boolean.parseBoolean(_ordenOperacion.getValue("aplica_reverso")), ConstantesGenerales.STATUS_EN_ESPERA, _ordenOperacion.getValue("operacion_nombre"));

												if (logger.isDebugEnabled()) {
													logger.debug(" con una transaccion Fija (" + _ordenOperacion.getValue("trnfin_id") + ")");
													logger.debug(" y con status (" + ConstantesGenerales.STATUS_EN_ESPERA + ")");
												}
												orden.agregarOperacion(operacion);// las operaciones seran enviadas a altair
												ordenCopia.agregarOperacion(operacion);// las operaciones seran actualizadas en BD

											}// fin else if estatus APLICADA
												// Operacion de credito por devolucion, se cobro el totalbloqueado se devolvera la diferencia no adjudicada
												// Si existe diferencia mayor que cero es porque se adjudico menos y se simulo nuevamente la orden con el monto menor
											if (montoAdjudicado.doubleValue() > cero.doubleValue()) {
												operacionEncontrada = false;
												ArrayList<OrdenOperacion> operacionesOrden = beanTOSimulada.getListaOperaciones();

												for (Iterator<OrdenOperacion> iter = operacionesOrden.iterator(); iter.hasNext();) {
													OrdenOperacion oper = (OrdenOperacion) iter.next();

													if (_ordenOperacion.getValue("trnfin_id").equals(oper.getIdTransaccionFinanciera())) {

														if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("0")) {
															operacionEncontrada = true;
														} else {
															// Verificar si es una comision, comparar con el idComisionUI ya que el id transaccion financiera sera el mismo para todas las comisiones
															// comparamos idComisionUI de la operacion original con el idComisionUI de la operacion simulada que esta seteado en el campo idOperacion
															if (_ordenOperacion.getValue("id_comision_ui").equals(String.valueOf(oper.getIdOperacion()))) {
																logger.info("OPERACION DE COMISION NRO " + _ordenOperacion.getValue("id_comision_ui") + " ENCONTRADA");
																operacionEncontrada = true;
															}
														}

														if (operacionEncontrada) {// si se encontro operacion en la lista de simuladas

															BigDecimal montoCre = new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("monto_operacion"))).subtract(oper.getMontoOperacion());
															if (montoCre.doubleValue() != cero.doubleValue() && montoCre.doubleValue() > 0) {
																OrdenOperacion operacionCredito = new OrdenOperacion();
																operacionCredito = crearContraparteOperacion(montoCre, new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")), _ordenOperacion.getValue("moneda_id"),
																		_ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"),
																		_ordenOperacion.getValue("ctecta_bcocta_telefono"), _ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"),
																		_ordenOperacion.getValue("ctecta_bcoint_bic"), _ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")),
																		Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")), _ordenOperacion.getValue("numero_retencion"), TransaccionFinanciera.CREDITO, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), false, ConstantesGenerales.STATUS_EN_ESPERA, _ordenOperacion.getValue("operacion_nombre"));
																orden.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
																ordenCopia.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
															}

															if (!esBDV) {
																if (logger.isDebugEnabled()) {
																	logger.debug("********** OPERACIONES AL VEHICULO ************");
																	logger.debug("********** OPERACION numero: " + oper.getIdOperacion());
																	logger.debug("* Generando operacion de (" + TransaccionFinanciera.CREDITO + " a la cuenta del Vehiculo por un monto de (" + oper.getMontoOperacion() + ")");
																}
																OrdenOperacion operacion1 = new OrdenOperacion();

																operacion1 = llenarOperacionAlVehiculo(ordenAlVehiculo, oper.getMontoOperacion(), veh_tomador, numeroCuentaVehTomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), TransaccionFinanciera.CREDITO, ConstantesGenerales.STATUS_EN_ESPERA, monedaLocal, false, new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))),
																		_ordenOperacion.getValue("operacion_nombre"), Integer.parseInt(_ordenOperacion.getValue("in_comision")), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")), Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")));
																ordenAlVehiculo.agregarOperacion(operacion1);
															}
															break;// salir si encontro operacion y pasar a la siguiente
														}// Fin SI OPERACION ENCONTRADA EN LISTA
													}// Fin comparacion de transacciones financieras
												}// Fin FoR: recorriendo operaciones simuladas por cada operacion de la orden

												// si no fue encontrada operacion original en lista de simuladas (solo aplica a comisiones)
												// :Caso de que se hayan encontrado menos operaciones en la simulacion por rango de montos
												if (!operacionEncontrada) {
													// si es una comision y NO es invariable: DEVOLVER DINERO
													// Verifica si la comisión NO ES invariable O SI NO ES MONTO FIJO: SI NO LA COMISION SE COBRA IGUAL
													// 1: NO INVARIABLE, 0: INVARIABLE
													if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("1") && _ordenOperacion.getValue("in_comision_invariable").equals("1") && !esComisionMontoFijo(_ordenOperacion.getValue("monto_operacion"), _ordenOperacion.getValue("tasa"))) {
														// Devolver dinero:Crear operacion de credito por el monto total de la operacion

														BigDecimal montoCre = new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("monto_operacion")));

														if (montoCre.doubleValue() != cero.doubleValue() && montoCre.doubleValue() > 0) {
															OrdenOperacion operacionCredito = new OrdenOperacion();
															operacionCredito = crearContraparteOperacion(montoCre, new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")), _ordenOperacion.getValue("moneda_id"),
																	_ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"),
																	_ordenOperacion.getValue("ctecta_bcocta_telefono"), _ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"),
																	_ordenOperacion.getValue("ctecta_bcoint_bic"), _ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")), Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")),
																	_ordenOperacion.getValue("numero_retencion"), TransaccionFinanciera.CREDITO, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), false, ConstantesGenerales.STATUS_EN_ESPERA, _ordenOperacion.getValue("operacion_nombre"));
															orden.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
															ordenCopia.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
														}
													}
												}

											}// Fin monto adjudicado mayor a cero (menor al pedido)
												// Es verdadera si se aplica un bloqueo por un monto menor al pedido pero mayor a cero
												// se usara para recorrer las operaciones del cliente y crear las del vehiculo para ejecutarlas en BD, se mandan aca relacionadas con el ID de las operaciones oriinales de BLOQUEO
											bloqueoAdjMenor = true;
										}// fin if transaccionFinanciera
									}// fin while recorrido de las operaciones de la orden
								}// fin if count _ordenOperacion
								if (transa_id.equals(TransaccionNegocio.TOMA_DE_ORDEN)) {
									if (_ordenOperacion.count() > 0) {
										_ordenOperacion.first();
										// Recorro DATASET de operaciones de la orden encontrada
										while (_ordenOperacion.next()) {
											if (transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO)) {
												miscelaneo = true;
												if (montoAdjudicado.doubleValue() > cero.doubleValue()) {
													operacionEncontrada = false;
													// buscar el monto de la operacion en la orden simulada, compararemos si es la misma por el nombre de a operacion
													ArrayList<OrdenOperacion> operacionesOrden = beanTOSimulada.getListaOperaciones();
													for (Iterator<OrdenOperacion> iter = operacionesOrden.iterator(); iter.hasNext();) {

														OrdenOperacion oper = (OrdenOperacion) iter.next();
														if (_ordenOperacion.getValue("trnfin_id").equals(oper.getIdTransaccionFinanciera())) {

															if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("0")) {
																operacionEncontrada = true;
															} else {
																// Verificar si es una comision, comparar con el idComisionUI ya que el id transaccion financiera sera el mismo para todas las comisiones
																// comparamos idComisionUI de la operacion original con el idComisionUI de la operacion simulada que esta seteado en el campo idOperacion
																if (_ordenOperacion.getValue("id_comision_ui").equals(String.valueOf(oper.getIdOperacion()))) {
																	operacionEncontrada = true;
																}
															}

															if (operacionEncontrada) {// si se encontro operacio en lista de simuladas
																OrdenOperacion operacionDebito = new OrdenOperacion();
																operacionDebito = crearContraparteOperacion(oper.getMontoOperacion(), oper.getTasa(), oper.getErrorNoAplicacion(), oper.getSerialContable(), oper.getInComision(), oper.getIdMoneda(), oper.getNumeroCuenta(), oper.getNombreReferenciaCuenta(), oper.getNombreBanco(), oper.getDireccionBanco(), oper.getCodigoSwiftBanco(),
																		oper.getCodigoBicBanco(), oper.getTelefonoBanco(), oper.getCodigoABA(), oper.getPaisBancoCuenta(), oper.getNombreBancoIntermediario(), oper.getDireccionBancoIntermediario(), oper.getCodigoSwiftBancoIntermediario(), oper.getCodigoBicBancoIntermediario(), oper.getTelefonoBancoIntermediario(), oper.getCodigoABAIntermediario(),
																		oper.getPaisBancoIntermediario(), oper.getIndicadorComisionInvariable(), oper.getIdOperacionRelacion(), oper.getNumeroRetencion(), TransaccionFinanciera.DEBITO, veh_tomador, Integer.parseInt(oper.getIdTransaccionFinanciera()), Long.parseLong(idOrden), false, ConstantesGenerales.STATUS_EN_ESPERA,
																		_ordenOperacion.getValue("operacion_nombre"));
																// no se setea la operacion al objeto orden ya que ests operaciones de debito no van contra altair en este proceso, iran en liquidacion
																ordenCopia.agregarOperacion(operacionDebito);// las operaciones seran actualizadas en BD

																break;// salir si encontro operacion y pasar a la siguiente
															}// Fin SI OPERACION ENCONTRADA
														}// Fin compraracio transaccion Financiera
													}// Fin FOR

												} else if (montoAdjudicado.doubleValue() == cero.doubleValue()) {
													// Cuando NO SE ADJUDICA NADA PAR MISCELANEOS
													// Si es operacion de Comision y es monto fijo, se cobrara igual. asi que se crea la operacion de débito
													if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("1") && esComisionMontoFijo(_ordenOperacion.getValue("monto_operacion"), _ordenOperacion.getValue("tasa"))) {

														OrdenOperacion operacionDebito = new OrdenOperacion();
														operacionDebito = crearContraparteOperacion(new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("monto_operacion"))), new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")),
																_ordenOperacion.getValue("moneda_id"), _ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"),
																_ordenOperacion.getValue("ctecta_bcocta_telefono"), _ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"),
																_ordenOperacion.getValue("ctecta_bcoint_bic"), _ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")), Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")),
																_ordenOperacion.getValue("numero_retencion"), TransaccionFinanciera.DEBITO, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), Boolean.parseBoolean(_ordenOperacion.getValue("aplica_reverso")), ConstantesGenerales.STATUS_EN_ESPERA, _ordenOperacion.getValue("operacion_nombre"));
														// no se setea la operacion al objeto orden ya que ests operaciones de debito no van contra altair en este proceso, iran en liquidacion
														ordenCopia.agregarOperacion(operacionDebito);// las operaciones seran actualizadas en BD
														ordenCopia.setComisionCero(false);// ITS-638
														orden.setComisionCero(false); // ITS-638
													} else {
														ordenCopia.setMontoComisionOrden(new BigDecimal(0));
													}
												}
											}// fin Transaccion Financiera MISCELANEO
										}// Fin WHILE
									}// fin recorrido de Operaciones

									// generamos una operacion de credito con el monto que devolveremos
									// en caso de que sea bloqueo o debito

									logger.debug(" * Creando operaciones de devolucion al cliente y al vehiculo Por concepto de Debitos Hechos en TO.");
									// Recorremos todas la operaciones de la orden para saber el monto cobrado y hacer los calculos para la devolucion del dinero
									if (_ordenOperacion.count() > 0) {
										_ordenOperacion.first();
										// Recorro DATASET de operaciones de la orden encontrada
										while (_ordenOperacion.next()) {
											operacionEncontrada = false;
											if (transaccionFinanciera.equals(TransaccionFinanciera.DEBITO)) {

												if (montoAdjudicado.doubleValue() > cero.doubleValue()) {
													ArrayList<OrdenOperacion> operacionesOrden = beanTOSimulada.getListaOperaciones();
													// Recorriendo operaciones simuladas
													for (Iterator<OrdenOperacion> iter = operacionesOrden.iterator(); iter.hasNext();) {

														OrdenOperacion oper = (OrdenOperacion) iter.next();

														if (_ordenOperacion.getValue("trnfin_id").equals(oper.getIdTransaccionFinanciera())) {

															if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("0")) {
																operacionEncontrada = true;
															} else {
																// Verificar si es una operacion, comparar con el idComisionUI ya que el id transaccion financiera sera el mismo para todas las comisiones
																// comparamos idComisionUI de la operacion original con el idComisionUI de la operacion simulada que esta seteado en el campo idOperacion
																if (_ordenOperacion.getValue("id_comision_ui").equals(String.valueOf(oper.getIdOperacion()))) {
																	operacionEncontrada = true;
																}
															}
															// HASTA AQUI
															if (operacionEncontrada) {

																BigDecimal montoCre = new BigDecimal(_ordenOperacion.getValue("monto_operacion"));
																montoCre = montoCre.subtract(oper.getMontoOperacion()).setScale(5, BigDecimal.ROUND_HALF_EVEN);
																OrdenOperacion operacionCredito = new OrdenOperacion();
																if (montoCre.doubleValue() != cero.doubleValue() && montoCre.doubleValue() > 0) {
																	operacionCredito = crearContraparteOperacion(montoCre, new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")), _ordenOperacion.getValue("moneda_id"),
																			_ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"),
																			_ordenOperacion.getValue("ctecta_bcocta_telefono"), _ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"),
																			_ordenOperacion.getValue("ctecta_bcoint_bic"), _ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")),
																			Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")), _ordenOperacion.getValue("numero_retencion"), TransaccionFinanciera.CREDITO, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), false, ConstantesGenerales.STATUS_EN_ESPERA,
																			_ordenOperacion.getValue("operacion_nombre"));
																	orden.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
																	ordenCopia.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
																}

																break;// romper ciclo
															}

														}// Fin compraracion de transaccciones finanieras
													}// fin FOR

													if (!operacionEncontrada) {
														// Si la operacion (COMISION) original no fue encontrada en la lista de opers simuladas, se debe devolver el dinero
														// Verifica si la comisión NO ES invariable O SI NO ES MONTO FIJO: SI NO LA COMISION SE COBRA IGUAL
														// 1: NO INVARIABLE, 0: INVARIABLE
														if (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("1") && _ordenOperacion.getValue("in_comision_invariable").equals("1") && !esComisionMontoFijo(_ordenOperacion.getValue("monto_operacion"), _ordenOperacion.getValue("tasa"))
																&& Double.parseDouble(_ordenOperacion.getValue("monto_operacion")) > cero.doubleValue()) {
															// Devolver dinero:Crear operacion de credito por el monto total de la operacion
															BigDecimal montoCre = new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("monto_operacion")));
															OrdenOperacion operacionCredito = new OrdenOperacion();
															operacionCredito = crearContraparteOperacion(montoCre, new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")), _ordenOperacion.getValue("moneda_id"),
																	_ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"),
																	_ordenOperacion.getValue("ctecta_bcocta_telefono"), _ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"),
																	_ordenOperacion.getValue("ctecta_bcoint_bic"), _ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")), Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")),
																	_ordenOperacion.getValue("numero_retencion"), TransaccionFinanciera.CREDITO, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), false, ConstantesGenerales.STATUS_EN_ESPERA, _ordenOperacion.getValue("operacion_nombre"));
															orden.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
															ordenCopia.agregarOperacion(operacionCredito);// las operaciones seran actualizadas

														}

													}

												} else if (montoAdjudicado.doubleValue() == cero.doubleValue()) {
													// Se creara el credito solo si la operacion es de capital o es una comision de porcentaje.
													// Si es monto fijo no se acredita ya que se cobrara siempre.
													if ((_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("1") && !esComisionMontoFijo(_ordenOperacion.getValue("monto_operacion"), _ordenOperacion.getValue("tasa"))) || (_ordenOperacion.getValue("in_comision") != null && _ordenOperacion.getValue("in_comision").equals("0"))) {

														BigDecimal montoCre = new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("monto_operacion")));
														OrdenOperacion operacionCredito = new OrdenOperacion();
														operacionCredito = crearContraparteOperacion(montoCre, new BigDecimal(Double.parseDouble(_ordenOperacion.getValue("tasa"))), _ordenOperacion.getValue("ordene_operacion_error"), _ordenOperacion.getValue("serial"), Integer.parseInt(_ordenOperacion.getValue("in_comision")), _ordenOperacion.getValue("moneda_id"),
																_ordenOperacion.getValue("ctecta_numero"), _ordenOperacion.getValue("ctecta_nombre"), _ordenOperacion.getValue("ctecta_bcocta_bco"), _ordenOperacion.getValue("ctecta_bcocta_direccion"), _ordenOperacion.getValue("ctecta_bcocta_swift"), _ordenOperacion.getValue("ctecta_bcocta_bic"), _ordenOperacion.getValue("ctecta_bcocta_telefono"),
																_ordenOperacion.getValue("ctecta_bcocta_aba"), _ordenOperacion.getValue("ctecta_bcocta_pais"), _ordenOperacion.getValue("ctecta_bcoint_bco"), _ordenOperacion.getValue("ctecta_bcoint_direccion"), _ordenOperacion.getValue("ctecta_bcoint_swift"), _ordenOperacion.getValue("ctecta_bcoint_bic"),
																_ordenOperacion.getValue("ctecta_bcoint_telefono"), _ordenOperacion.getValue("ctecta_bcoint_aba"), _ordenOperacion.getValue("ctecta_bcoint_pais"), Integer.parseInt(_ordenOperacion.getValue("in_comision_invariable")), Long.parseLong(_ordenOperacion.getValue("ordene_operacion_id")), _ordenOperacion.getValue("numero_retencion"),
																TransaccionFinanciera.CREDITO, veh_tomador, Integer.parseInt(_ordenOperacion.getValue("trnfin_id")), Long.parseLong(idOrden), false, ConstantesGenerales.STATUS_EN_ESPERA, _ordenOperacion.getValue("operacion_nombre"));
														orden.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
														ordenCopia.agregarOperacion(operacionCredito);// las operaciones seran actualizadas
														orden.setComisionCero(false); // ITS-638
														ordenCopia.setComisionCero(false); // ITS-638
													} else {
														ordenCopia.setMontoComisionOrden(new BigDecimal(0));
													}
												}// Fin Monto Adjudicado IGUAL a CERO
											}// Fin transacion Financiera DEBITO
										}// Fin WHILE
									}// Fin recorrido de Operaciones
								}// Fin transaccion Negocio TOMA DE ORDEN
								logger.info("Tiempo de procesamiento de MONTO MENOR ENVIADA" + (System.currentTimeMillis() - inicioMontoMenor) + " miliseg ");

							}// FIN si el monto adjudicado es MENOR al monto pedido soliciado
							else {
								// Si el monto adjudicado es mayor rompemos el ciclo y continuamos con el siguiente registro
								continue;
							}
							// Aplicar transacciones financieras
							if (!transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO) || (transa_id.equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA) && orden.getOperacion().size() == 0)) {
								if (!orden.isOperacionVacio()) {
									try {
										if (logger.isDebugEnabled()) {
											logger.debug("la Cantidad de operaciones de la orden(" + orden.getIdOrden() + ") es de: " + orden.getOperacion().size());
										}
										ArrayList<Object> listaOrdenSimulada = new ArrayList<Object>();
										listaOrdenSimulada.add(orden);
										long t2 = System.currentTimeMillis();
										// Verifica si el cobro es en línea
										if (cobroEnLinea) {
											factoryAltair.aplicarOrdenes(orden.getOperacion());
											_req.getSession().setAttribute("error_transf_altair", "0");
										}
									} catch (Exception e) {
										logger.error("* ERROR Aplicando las Transacciones Financieras contra Altair. ");
										logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
										_req.getSession().setAttribute("error_transf_altair", "1");
									} catch (Throwable e) {
										logger.error("* ERROR Aplicando las Transacciones Financieras contra Altair. ");
										logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
										_req.getSession().setAttribute("error_transf_altair", "1");
									}
								}
							}
							if (!esBDV) {
								if (bloqueoAdjMenor) {
									if (!miscelaneo) {
										if (!ordenAlVehiculo.isOperacionVacio()) {
											try {
												logger.debug("la Cantidad de operaciones de la orden(" + ordenAlVehiculo.getIdOrden() + ") es de: " + ordenAlVehiculo.getOperacion().size());
												ArrayList<Object> listaOrdenSimulada = new ArrayList<Object>();
												listaOrdenSimulada.add(ordenAlVehiculo);
												long t2 = System.currentTimeMillis();
												if (cobroEnLinea) {
													factoryAltair.aplicarOrdenes(ordenAlVehiculo.getOperacion());
													logger.info("TIEMPO EN ALTAIR=" + (System.currentTimeMillis() - t2));
													_req.getSession().setAttribute("error_transf_altair", "0");
													logger.info("* Aplicadas las Transacciones Financieras contra Altair.");
												}
											} catch (Exception e) {
												logger.error("* ERROR Aplicando las Transacciones Financieras contra Altair. ");
												logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
												_req.getSession().setAttribute("error_transf_altair", "1");
											} catch (Throwable e) {
												logger.error("* ERROR Aplicando las Transacciones Financieras contra Altair. ");
												logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
												_req.getSession().setAttribute("error_transf_altair", "1");
											}
										}
									}
								}
							}

							// VERIFICAMOS CUALES DE LAS OPERACIONES ENVIADAS ALTAIR QUEDARON RECHAZADAS

							if (!orden.isOperacionVacio()) {
								ArrayList<OrdenOperacion> listaOperaciones = orden.getOperacion();
								// Se recorre la lista y se almacenan en la base de datos
								for (Iterator<OrdenOperacion> iter = listaOperaciones.iterator(); iter.hasNext();) {
									OrdenOperacion ordenOperacion = (OrdenOperacion) iter.next();
									if (ordenOperacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_RECHAZADA)) {
										indicador = true;
										// logger.info("* Por lo menos una operacion fue rechazada");
										break;
									}
								}
							}
							// verificamos que las operaciones fueron exitosas
							// if fueron rechazadas la dejamos en proceso de adjudicacion sino actualizamos la orden a adjudicada
							if ((indicador || !cobroEnLinea)) {
								ordenCopia.setStatus(StatusOrden.PROCESO_ADJUDICACION);
							} else {
								ordenCopia.setStatus(StatusOrden.ADJUDICADA);
							}

						} // if status orden ENVIADA

						if (status_orden.equals(StatusOrden.REGISTRADA) && multiplesUI == true) {
							ordenCopia.setStatus(StatusOrden.PROCESO_ADJUDICACION);
							// indicador2=true;
						}// if status orden EN PROCESO DE ADJUDICADA . . . . .

						// **************** INICIO VALORES FIJO DE UNA ORDEN A SER ADJUDICADA ***************
						ordenCopia.setIdCliente(Long.parseLong(idCliente));
						ordenCopia.setIdOrden(Long.parseLong(idOrden));
						ordenCopia.setMontoAdjudicado(montoAdjudicado.doubleValue());
						if (montoAdjudicado.doubleValue() == 0) {
							ordenCopia.setMontoTotal(-1);
						}
						ordenCopia.setFechaAdjudicacion(new Date());
						// VALORES PARA LLEVAR EL CONTROL DEL ARCHIVO A GENERAR

						// archivo.setIdEjecucion(Long.parseLong(ejecucion_id));
						// archivo.setIdEjecucionRelacion(ConstantesGenerales.FALSO);
						// archivo.setInRecepcion(ConstantesGenerales.VERDADERO);// Verdadero para indicar recepcion y Falso para indicar envio
						// archivo.setNombreArchivo(xlsNombre);
						// archivo.setStatus(ConstantesGenerales.RECIBIDO);
						// archivo.setUnidadInv(Long.parseLong(unidadInversion));
						// archivo.setUsuario(getUserName());

						// **************** FIN VALORES FIJOS DE UNA ORDEN A SER ADJUDICADA *****************
						if ((!indicador2)) {
							String[] consulta = null;
							logger.debug("STATUS ORDEN ORIGINAL " + ordenOriginal.getStatus());
							if (multiplesUI) {
								if (ordenOriginal.getStatus().equals(StatusOrden.ENVIADA) || ordenOriginal.getStatus().equals(StatusOrden.REGISTRADA)) {
									consulta = ordenDAO.modificar(ordenCopia);
								}
							} else {
								consulta = ordenDAO.modificar(ordenCopia);
							}
							if (consulta != null) {
								String[] sqlA = new String[consulta.length];
								// Almacena las consultas finales
								for (int cont = 0; cont < consulta.length; cont++) {
									sqlA[cont] = (String) consulta[cont];
									querys.add(sqlA[cont]);
								}
							}
							// String[] consulta2 = null;
							// if (!insertadoArchivoRecepcion){
							// //Insertamos detalles sin crear registro en 803 ya que todos los registros procesados pertenecen al mismo ejecuion_id generado al inicio de esta clase
							// consulta2 = controlDAO.insertarDetalle(archivo);
							// }else{
							// //por aca pasara una sola vez para el primer registro
							// consulta2 =controlDAO.insertarArchivoRecepcion(archivo);
							// insertadoArchivoRecepcion = true;
							// }
							// String[] sqlB = new String[consulta2.length];
							// for(int cont=0;cont<consulta2.length; cont++){
							// sqlB[cont] = (String) consulta2[cont];
							// querys.add(sqlB[cont]);
							//
							// }

							// Recuperamos la operaciones del cliente y creamos contraparte para el vehiculo en caso de ser Diferente de BDV
							if (!esBDV) {
								if (!bloqueoAdjMenor) {
									ArrayList<OrdenOperacion> ordenesDelCliente = ordenCopia.getOperacion();
									OrdenOperacion operacionAlVehiculo = new OrdenOperacion();
									for (Iterator<OrdenOperacion> iter = ordenesDelCliente.iterator(); iter.hasNext();) {
										OrdenOperacion ordenOperacion = (OrdenOperacion) iter.next();
										if (ordenOperacion.getTipoTransaccionFinanc().equals(TransaccionFinanciera.DEBITO)) {
											operacionAlVehiculo = llenarOperacionAlVehiculo(ordenAlVehiculo, ordenOperacion.getMontoOperacion(), veh_tomador, numeroCuentaVehTomador, Integer.parseInt(ordenOperacion.getIdTransaccionFinanciera()), TransaccionFinanciera.CREDITO, ConstantesGenerales.STATUS_EN_ESPERA, ordenOperacion.getIdMoneda(), false, ordenOperacion.getTasa(),
													ordenOperacion.getNombreOperacion(), ordenOperacion.getInComision(), ordenOperacion.getIndicadorComisionInvariable(), ordenOperacion.getIdOperacion());
										} else if (ordenOperacion.getTipoTransaccionFinanc().equals(TransaccionFinanciera.CREDITO)) {
											operacionAlVehiculo = llenarOperacionAlVehiculo(ordenAlVehiculo, ordenOperacion.getMontoOperacion(), veh_tomador, numeroCuentaVehTomador, Integer.parseInt(ordenOperacion.getIdTransaccionFinanciera()), TransaccionFinanciera.DEBITO, ConstantesGenerales.STATUS_EN_ESPERA, ordenOperacion.getIdMoneda(), false, ordenOperacion.getTasa(),
													ordenOperacion.getNombreOperacion(), ordenOperacion.getInComision(), ordenOperacion.getIndicadorComisionInvariable(), ordenOperacion.getIdOperacion());
										}
										logger.debug("********** OPERACION NUMERO: " + ordenOperacion.getIdOperacion());
										ordenAlVehiculo.agregarOperacion(operacionAlVehiculo);
									}

									// Aplicar transacciones financieras
									if (!miscelaneo) {
										if (!ordenAlVehiculo.isOperacionVacio()) {
											try {
												ArrayList<Object> listaOrdenSimulada = new ArrayList<Object>();
												listaOrdenSimulada.add(orden);
												long t2 = System.currentTimeMillis();
												if (cobroEnLinea) {
													factoryAltair.aplicarOrdenes(ordenAlVehiculo.getOperacion());
													logger.info("TIEMPO EN ALTAIR=" + (System.currentTimeMillis() - t2));
													_req.getSession().setAttribute("error_transf_altair", "0");
													logger.info("* Aplicadas las Transacciones Financieras contra Altair.");
												}
											} catch (Exception e) {
												logger.error("* ERROR Aplicando las Transacciones Financieras contra Altair. ");
												logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
												_req.getSession().setAttribute("error_transf_altair", "1");
											} catch (Throwable e) {
												logger.error("* ERROR Aplicando las Transacciones Financieras contra Altair. ");
												logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
												_req.getSession().setAttribute("error_transf_altair", "1");
											}
										}
									}
								}
								String[] querysVEhiculo = ordenDAO.modificar(ordenAlVehiculo);
								String[] sqlC = new String[querysVEhiculo.length];
								for (int cont = 0; cont < querysVEhiculo.length; cont++) {
									sqlC[cont] = (String) querysVEhiculo[cont];
									querys.add(sqlC[cont]);
								}
							}
							// Fin Operaciones al Vehiculo

							// Se invoca el proceso de generacion de Deal de OPICS y creacion
							if (this.multiplesUI) {
								if (ordenOriginal.getStatus().equals(StatusOrden.ENVIADA) || ordenOriginal.getStatus().equals(StatusOrden.REGISTRADA)) {
									generarOpics(ordenOriginal, querys);
								}
							}

							// ---LLamada a generar mensaje para Sistema Estadística y BVC--------
							generarMensajeEstadistica(querys, ordenOriginal, _cliente, montoAdjudicado, (broker != null ? broker.toString() : ""), (consecutivo != null ? Long.parseLong(consecutivo.toString()) : 0));
							generarMensajeBCV(querys, ordenOriginal, _cliente, montoAdjudicado, (broker != null ? broker.toString() : ""), (consecutivo != null ? Long.parseLong(consecutivo.toString()) : 0));
							// -------------------------------------------------------------

							String[] ejecutar = new String[querys.size()];
							for (int z = 0; z < querys.size(); z++) {
								ejecutar[z] = querys.get(z).toString();
							}

							long t3 = System.currentTimeMillis();
							db.execBatch(_dso, ejecutar);
							logger.info("EJECUTADOS LOS QUERYS - TIEMPO DE EXECBATCH=" + (System.currentTimeMillis() - t3));

							/*
							 * de Orden de recompra para SITME logger.info("Orden a crear del de opics SITME: "+ordenCopia.getIdOrden());
							 * 
							 * try{ UsuarioDAO usuarioDAO = new UsuarioDAO(_dso); int idUserSession = Integer.parseInt((usuarioDAO.idUserSession(getUserName()))); GeneradorDealTicketSITME generadorDealTicketSITME = new GeneradorDealTicketSITME(_dso, idUserSession, _app, _req); generadorDealTicketSITME.generarDealTicketSITME(orden); }catch(Exception e){
							 * logger.error("Error durante el proceso de generación de Ordenes de Recompra y Deal Tickets " + "de las Ordenes ADJUDICADAS pertenecientes a Unidades de Inversión tipo SITME: "+e); }
							 */
						}

						int cantDocumentos = ordenDAO.cantidadDocumentosOrdenPorTransacion(Long.parseLong(idOrden), TransaccionNegocio.ADJUDICACION);
						// DataSet documentosOrden = ordenDAO.getDataSet();
						if (cantDocumentos <= 0 && !transa_id.equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {
							long t4 = System.currentTimeMillis();
							ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(_dso);
							// Busca los documentos asociados a la adjudicación
							idTransaccion = ordenOriginal.getIdTransaccion();
							ordenOriginal.setIdTransaccion(TransaccionNegocio.ADJUDICACION);
							ordenOriginal.setMonto(montoPedido.doubleValue());
							ordenOriginal.setMontoAdjudicado(montoAdjudicado.doubleValue());
							ordenOriginal.eliminarDocumento();// eliminamos los documentos que trae previamente ya que no nos interesa guardarlos por no ser de adjudicacion (son los de toma de orden)
							com.bdv.infi.data.Cliente objcliente = new com.bdv.infi.data.Cliente();
							objcliente.setIdCliente(Long.parseLong(idCliente));
							objcliente.setTipoPersona(tipoPersona);
							long tiempoDocuc = System.currentTimeMillis();
							procesarDocumentos.procesar(ordenOriginal, this._app, _req.getRemoteAddr(), objcliente);
							logger.debug("Tiempo de agregación de documentos " + (System.currentTimeMillis() - tiempoDocuc) + " miliseg");

							ordenOriginal.setIdTransaccion(idTransaccion);
							ordenDAO.insertarDocumentos(ordenOriginal);
						}

					} else {
						if (idCliente == null) {
							logger.info("* El cliente con Cedula/RIF: " + ced_cliente + " no fue encontrado en Base de Datos");
						}
						if (idOrden != null) {
							if (statusOrdenLeida.equals(StatusOrden.ADJUDICADA)) {
								logger.info("* La Orden: " + idOrden + " ha sido ADJUDICADA por " + montoAdjudicado + ", correspondiente al cliente con Cedula/RIF: " + ced_cliente + " con un Monto de (" + montoPedido + ")");
							}
						} else if (idOrden == null) {
							logger.info("* No fue encontrada en Base de Datos una orden para el cliente con Cedula/RIF: " + ced_cliente + " con un Monto de (" + montoPedido + ")");

						}
					}
				} catch (Exception npe) {
					proceso.setDescripcionError(npe.getMessage());
					logger.error(npe.getMessage() + " " + Utilitario.stackTraceException(npe));
				}
				logger.info("Tiempo de procesamiento de orden " + (System.currentTimeMillis() - inicioProcesarOrden) + " miliseg ");
			}// fin for filas
			storeDataSet("record", _record);
			_req.getSession().setAttribute("MUI", _record);

		}// fin de for columnas
			// /Cambiar estatus de la unidad a ADJUDICADA si todas las ordenes existentes estan en estatus ADJUDICADA
		try {
			// Actualiza el status de las órdenes si el proceso es en bacth
			long tiempoActO = System.currentTimeMillis();
			actualizarOrdenes();
			logger.info("Tiempo de actualizando ordenes " + (System.currentTimeMillis() - tiempoActO) + " miliseg ");

			int faltanOrdenes = ordenDAO.existeOrdenesPorAdjudicar(unidadInversion);
			if (faltanOrdenes == ConstantesGenerales.FALSO) {
				UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
				if (multiplesUI != true) {
					unidadDAO.modificarStatus(Long.parseLong(unidadInversion), UnidadInversionConstantes.UISTATUS_ADJUDICADA);
				}
				logger.info("* Todas las ordenes de la Unidad han sido ADJUDICADAS, por lo tanto la Unidad de Inversion cambió a estatus ADJUDICADA");
			} else {
				logger.info("* Faltan ordenes por adjudicar");
			}
		} catch (Exception e) {
			proceso.setDescripcionError(e.getMessage());
			logger.error("* Error al intentar Actualizar el Status de la Unidad de Inversion. " + e.getMessage());
			throw new Exception("Error al intentar Actualizar el Status de la Unidad de Inversion. " + e.getMessage());
		} finally {
			logger.info("********** FIN ADJUDICACION *********");
			logger.info("------------------------------------- FIN TRAZAS DEL PROCESO DE ADJUDICACION -----------------------------------");
		}
		// FIN CAMBIO DE ESTATUS
		logger.info("TIEMPO ADJUDICANDO=" + (System.currentTimeMillis() - t1));
		terminarProceso();

		// Se ejecuta el proceso que genera las ordenes de recompra y los Deal Tickets para las ordenes
		// con estatus ADJUDICADA que pertenezcan a Unidades de Inversión tipo SITME

		storeDataSet("mensajes", mensajesPorRif);

	} // FIN EXECUTE

	/**
	 * Verifica si la comision es de monto fijo
	 * 
	 * @param montoComision
	 * @param tasaComision
	 * @return
	 */
	private boolean esComisionMontoFijo(String montoComision, String tasaComision) {
		// si la tasa de comision es 0 pero el monto es mayor a 0, corresponde a comision de monto fijo
		if (Double.parseDouble(tasaComision) == 0 && Double.parseDouble(montoComision) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Busca el id de comisionUI que se encuentra guardado en DataExtendida de la orden a adjudicar y lo setea al objeto de operacion de comision
	 * 
	 * @param operacion
	 * @param ordenOpera
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	private void buscarIdComisionUI(DataSet operaciones, OrdenOperacion ordenOpera) throws NumberFormatException, Exception {

		if (dataExtComisiones.count() > 0) {
			dataExtComisiones.first();
			while (dataExtComisiones.next()) {
				String[] comisiones = dataExtComisiones.getValue("dtaext_valor").split(";");
				// comisiones[0] = idComisionUI
				// comisiones[1] = tasa
				if (ordenOpera.getTasa().doubleValue() == Double.parseDouble(comisiones[1])) {
					logger.info("* ID comision UI encontrada: " + comisiones[0] + " para tasa: " + ordenOpera.getTasa());
					// setear idComisionUI a la comision de que se enviará
					ordenOpera.setIdOperacion(Long.parseLong(comisiones[0]));
					// remover registro en dataset para evitar que encuentre dos tasas iguales y se asigne el mismo id de comisionUI
					dataExtComisiones.removeRow(dataExtComisiones.cursorPos() + 1);
					break;
				}
			}
		}
	}

	/**
	 * Metodo que retorna el codigo y nombre de la transaccion asociada a la operacion
	 * 
	 * @param vehiculo_id
	 *            vehiculo tomador
	 * @param transacionFija
	 *            id de la transaccion fija
	 * @param vehiculo
	 *            boolean para indicar si la opreacion es a la cuenta del cliente(false) o al vehiculo(true)
	 * @param transaccionFinanciera
	 *            tipo de operacion (DEB, BLO, CRE)
	 * @throws Exception
	 */
	private void BuscarCodigoyNombreOperacion(String vehiculo_id, int transacionFija, boolean vehiculo, String transaccionFinanciera) throws Exception {

		codigo_operacion = "";
		nombre_operacion = "";

		logger.info("* Buscando Codigo y nombre de operacion para el Vehiculo: " + vehiculo_id + ", Transaccion Fija: " + transacionFija + ", transaccion Financiera: " + transaccionFinanciera);

		if (transaccionesFijasAdjudicacion.count() > 0) {

			transaccionesFijasAdjudicacion.first();
			while (transaccionesFijasAdjudicacion.next()) {

				if (transaccionesFijasAdjudicacion.getValue("vehicu_id").equals(vehiculo_id) && Integer.parseInt(transaccionesFijasAdjudicacion.getValue("trnfin_id")) == transacionFija) {

					if (transaccionFinanciera.equals(TransaccionFinanciera.DEBITO)) {

						if (vehiculo) {
							codigo_operacion = transaccionesFijasAdjudicacion.getValue("cod_operacion_veh_deb");
						} else {

							codigo_operacion = transaccionesFijasAdjudicacion.getValue("cod_operacion_cte_deb");
						}
					} else if (transaccionFinanciera.equals(TransaccionFinanciera.CREDITO)) {
						if (vehiculo) {
							codigo_operacion = transaccionesFijasAdjudicacion.getValue("cod_operacion_veh_cre");
						} else {
							codigo_operacion = transaccionesFijasAdjudicacion.getValue("cod_operacion_cte_cre");
						}
					} else if (transaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)) {

						if (!vehiculo) {

							codigo_operacion = transaccionesFijasAdjudicacion.getValue("cod_operacion_cte_blo");

						}
					}
					nombre_operacion = transaccionesFijasAdjudicacion.getValue("trnfin_nombre");
				}
			}

		}
		// logger.info("Nombre Operacion: "+nombre_operacion+" codigo operacion: "+codigo_operacion);
	}

	/**
	 * Metodo que crea una operacion de credito hacia la cuenta del vehiculo tomador, asociada a la orden de tipo ORDEN_VEHICULO en caso de que no exista dicha orden se crea y la orden de tipo ORDEN_VEHICULO con su operacion de CREDITO
	 * 
	 * @param montoOperacion
	 * @param vehiculo
	 * @param transaccionFija
	 * @param transaccionFinanciera
	 * @param status
	 * @param moneda
	 * @return operacion el objeto operacion con toda la informacion de la operacion hacia la cuenta del vehiculo
	 * @throws Exception
	 */
	private OrdenOperacion llenarOperacionAlVehiculo(Orden ordenAlVehiculo, BigDecimal montoOperacion, String vehiculo, String numeroCuentaVehTomador, int transaccionFija, String transaccionFinanciera, String status, String moneda, boolean reverso, BigDecimal tasa, String operacionNombre, int inComision, int indicadorComisionInvariable, Long idOperacionRelacion) throws Exception {

		OrdenOperacion operacion = new OrdenOperacion();

		operacion.setIdOrden(ordenAlVehiculo.getIdOrden());
		operacion.setIdOperacionRelacion(idOperacionRelacion);
		operacion.setIdTransaccionFinanciera(String.valueOf(transaccionFija));
		operacion.setStatusOperacion(status);
		operacion.setAplicaReverso(reverso);
		operacion.setMontoOperacion(montoOperacion);
		operacion.setTasa(tasa);
		operacion.setIdMoneda(moneda);
		operacion.setNumeroCuenta(numeroCuentaVehTomador); // buscar la cuenta a tabla vehiculo
		operacion.setTipoTransaccionFinanc(transaccionFinanciera);
		BuscarCodigoyNombreOperacion(vehiculo, transaccionFija, true, transaccionFinanciera);
		operacion.setCodigoOperacion(codigo_operacion);
		// operacion.setNombreOperacion(nombre_operacion);
		operacion.setNombreOperacion(operacionNombre);
		operacion.setInComision(inComision);
		operacion.setIndicadorComisionInvariable(indicadorComisionInvariable);
		operacion.setIndicadorComisionInvariable(cero.intValue());
		operacion.setNumeroRetencion(cero.toString());

		return operacion;
	}

	private OrdenOperacion crearContraparteOperacion(BigDecimal montoOperacion, BigDecimal tasa, String errorNoAplicacion, String serialContable, int inComision, String idMoneda, String numeroCuenta, String nombreReferenciaCuenta, String nombreBanco, String direccionBanco, String codigoSwiftBanco, String codigoBicBanco, String telefonoBanco, String codigoABA, String paisBancoCuenta,
			String nombreBancoIntermediario, String direccionBancoIntermediario, String codigoSwiftBancoIntermediario, String codigoBicBancoIntermediario, String telefonoBancoIntermediario, String codigoABAIntermediario, String paisBancoIntermediario, int indicadorComisionInvariable, Long idOperacionRelacion, String numeroRetencion, String tipoTransaccionFinanc, String vehiculo,
			int transaccionFija, Long idOrden, boolean aplicaReverso, String status, String descripcionOperacion) throws Exception {
		return crearContraparteOperacionDefinitiva(montoOperacion, tasa, errorNoAplicacion, serialContable, inComision, idMoneda, numeroCuenta, nombreReferenciaCuenta, nombreBanco, direccionBanco, codigoSwiftBanco, codigoBicBanco, telefonoBanco, codigoABA, paisBancoCuenta, nombreBancoIntermediario, direccionBancoIntermediario, codigoSwiftBancoIntermediario, codigoBicBancoIntermediario,
				telefonoBancoIntermediario, codigoABAIntermediario, paisBancoIntermediario, indicadorComisionInvariable, idOperacionRelacion, numeroRetencion, tipoTransaccionFinanc, vehiculo, transaccionFija, idOrden, aplicaReverso, status, descripcionOperacion, null);
	}

	private OrdenOperacion crearContraparteOperacion(BigDecimal montoOperacion, BigDecimal tasa, String errorNoAplicacion, String serialContable, int inComision, String idMoneda, String numeroCuenta, String nombreReferenciaCuenta, String nombreBanco, String direccionBanco, String codigoSwiftBanco, String codigoBicBanco, String telefonoBanco, String codigoABA, String paisBancoCuenta,
			String nombreBancoIntermediario, String direccionBancoIntermediario, String codigoSwiftBancoIntermediario, String codigoBicBancoIntermediario, String telefonoBancoIntermediario, String codigoABAIntermediario, String paisBancoIntermediario, int indicadorComisionInvariable, Long idOperacionRelacion, String numeroRetencion, String tipoTransaccionFinanc, String vehiculo,
			int transaccionFija, Long idOrden, boolean aplicaReverso, String status, String descripcionOperacion, Date fechaValor) throws Exception {
		return crearContraparteOperacionDefinitiva(montoOperacion, tasa, errorNoAplicacion, serialContable, inComision, idMoneda, numeroCuenta, nombreReferenciaCuenta, nombreBanco, direccionBanco, codigoSwiftBanco, codigoBicBanco, telefonoBanco, codigoABA, paisBancoCuenta, nombreBancoIntermediario, direccionBancoIntermediario, codigoSwiftBancoIntermediario, codigoBicBancoIntermediario,
				telefonoBancoIntermediario, codigoABAIntermediario, paisBancoIntermediario, indicadorComisionInvariable, idOperacionRelacion, numeroRetencion, tipoTransaccionFinanc, vehiculo, transaccionFija, idOrden, aplicaReverso, status, descripcionOperacion, fechaValor);
	}

	private OrdenOperacion crearContraparteOperacionDefinitiva(BigDecimal montoOperacion, BigDecimal tasa, String errorNoAplicacion, String serialContable, int inComision, String idMoneda, String numeroCuenta, String nombreReferenciaCuenta, String nombreBanco, String direccionBanco, String codigoSwiftBanco, String codigoBicBanco, String telefonoBanco, String codigoABA, String paisBancoCuenta,
			String nombreBancoIntermediario, String direccionBancoIntermediario, String codigoSwiftBancoIntermediario, String codigoBicBancoIntermediario, String telefonoBancoIntermediario, String codigoABAIntermediario, String paisBancoIntermediario, int indicadorComisionInvariable, Long idOperacionRelacion, String numeroRetencion, String tipoTransaccionFinanc, String vehiculo,
			int transaccionFija, Long idOrden, boolean aplicaReverso, String status, String descripcionOperacion, Date fechaValor) throws Exception {
		OrdenOperacion operacion = new OrdenOperacion();

		operacion.setMontoOperacion(montoOperacion);
		operacion.setTasa(tasa);
		operacion.setErrorNoAplicacion(errorNoAplicacion);
		operacion.setSerialContable(serialContable);
		operacion.setInComision(inComision);
		operacion.setIdMoneda(idMoneda);
		operacion.setNumeroCuenta(numeroCuenta);
		operacion.setNombreReferenciaCuenta(nombreReferenciaCuenta);
		operacion.setNombreBanco(nombreBanco);
		operacion.setDireccionBanco(direccionBanco);
		operacion.setCodigoSwiftBanco(codigoSwiftBanco);
		operacion.setCodigoBicBanco(codigoBicBanco);
		operacion.setTelefonoBanco(telefonoBanco);
		operacion.setCodigoABA(codigoABA);
		operacion.setPaisBancoCuenta(paisBancoCuenta);
		operacion.setNombreBancoIntermediario(nombreBanco);
		operacion.setDireccionBancoIntermediario(direccionBancoIntermediario);
		operacion.setCodigoSwiftBancoIntermediario(codigoSwiftBancoIntermediario);
		operacion.setCodigoBicBancoIntermediario(codigoBicBancoIntermediario);
		operacion.setTelefonoBancoIntermediario(telefonoBancoIntermediario);
		operacion.setCodigoABAIntermediario(codigoABAIntermediario);
		operacion.setPaisBancoIntermediario(paisBancoIntermediario);
		operacion.setIndicadorComisionInvariable(indicadorComisionInvariable);
		operacion.setIdOperacionRelacion(idOperacionRelacion);
		operacion.setNumeroRetencion(numeroRetencion);
		operacion.setTipoTransaccionFinanc(tipoTransaccionFinanc);
		operacion.setIdOrden(idOrden);
		operacion.setStatusOperacion(status);
		operacion.setIdTransaccionFinanciera(String.valueOf(transaccionFija));
		BuscarCodigoyNombreOperacion(vehiculo, transaccionFija, false, tipoTransaccionFinanc);
		operacion.setCodigoOperacion(codigo_operacion);
		if (descripcionOperacion == null) {
			operacion.setNombreOperacion(nombre_operacion);
		} else {
			operacion.setNombreOperacion(descripcionOperacion);
		}

		if (fechaValor != null) {
			operacion.setFechaAplicar(fechaValor);
		}

		return operacion;
	}

	private void setearCobroEnLineaDeLaUnidad() throws Exception {
		if (this.unidadInversion != null) {
			UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
			unidadDAO.listarPorId(Long.parseLong(this.unidadInversion));
			DataSet dataSet = unidadDAO.getDataSet();
			if (dataSet.next()) {
				if (dataSet.getValue("in_cobro_batch_adj") != null && dataSet.getValue("in_cobro_batch_adj").equals("0")) {
					logger.info("Cobro de adjudicación vía en línea");
					cobroEnLinea = true;
				}
			}
		}

	}

	/**
	 * Obtiene los valores básicos de las unidad de inversión
	 * 
	 * @param nombreUI
	 *            nombre de la unidad
	 * @throws Exception
	 *             en caso de error
	 */
	private void obtenerDatosDeLaUnidadDeInversionPorNombre(String nombreUI) throws Exception {
		// Verifica si el nombre está en el hash
		// El objeto hashIdPorUnidad contiene DataSet
		if (hashIdPorUnidad.containsKey(nombreUI)) {
			DataSet ds = (DataSet) hashIdPorUnidad.get(nombreUI);
			unidadInversion = ds.getValue("undinv_id");
			cobroEnLinea = ds.getValue("IN_COBRO_BATCH_ADJ").equals("0") ? true : false;
		} else {
			UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
			unidadDAO.listarDatosBasicosPorNombreUI(nombreUI);
			if (unidadDAO.getDataSet().next()) {
				unidadInversion = unidadDAO.getDataSet().getValue("undinv_id");
				hashIdPorUnidad.put(nombreUI, unidadDAO.getDataSet());
				cobroEnLinea = unidadDAO.getDataSet().getValue("IN_COBRO_BATCH_ADJ").equals("0") ? true : false;
				hashCobroEnLineaPorUI.put(unidadInversion, unidadDAO.getDataSet().getValue("IN_COBRO_BATCH_ADJ"));
			} else {
				cobroEnLinea = false;
				unidadInversion = "0";
				logger.error("Unidad de inversión " + nombreUI + " no ha sido encontrada");
			}
		}
	}

	/**
	 * Obtiene el dataset que contiene la unidad de inversión si lo encuentra en el cache
	 * 
	 * @param unidadId
	 *            id de la unidad de inversión
	 * @return un dataset con información de la unidad de inversión encontrada
	 * @throws Exception
	 *             en caso de error
	 */
	private DataSet obtenerDatosDeLaUnidadDeInversionPorId(int unidadId) throws Exception {
		DataSet ds = null;
		Iterator it = hashIdPorUnidad.keySet().iterator();
		while (it.hasNext()) {
			ds = (DataSet) hashIdPorUnidad.get(it.next());
			if (Integer.parseInt(ds.getValue("UNDINV_ID")) == unidadId) {
				return ds;
			}
		}
		ds = null;
		return ds;
	}

	/**
	 * Actualiza las ordenes sólo si el proceso es batch
	 */
	private void actualizarOrdenes() throws Exception {

		if (multiplesUI) { // Es sitme
			Iterator<DataSet> it = hashIdPorUnidad.keySet().iterator();
			String unidInv = "";
			while (it.hasNext()) {
				unidInv = ((DataSet) hashIdPorUnidad.get(it.next())).getValue("undinv_id"); // Obiene el id de la unidad
				// Verifica si el cobro es en línea

				if (hashCobroEnLineaPorUI.get(unidInv).equals("1")) {
					ejecutar(unidInv);
				}
			}
		} else {
			ejecutar(unidadInversion);
		}
	}

	private void ejecutar(String unidadInversion) throws Exception {
		String[] sql = new String[3];

		// --- miscelaneas
		sql[0] = "UPDATE infi_tb_204_ordenes SET ordsta_id='" + StatusOrden.ADJUDICADA + "' WHERE ORDENE_ID IN( " + " select ordene_id from infi_tb_204_ordenes a where a.uniinv_id=" + unidadInversion + " and a.ordene_id in( " + " select ordene_id from infi_tb_207_ordenes_operacion where trnf_tipo in('" + TransaccionFinanciera.MISCELANEO + "') " + " )  and ordsta_id in('"
				+ StatusOrden.PROCESO_ADJUDICACION + "')  and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "'))";
		// --- Débito
		sql[1] = "UPDATE infi_tb_204_ordenes SET ordsta_id='" + StatusOrden.ADJUDICADA + "' WHERE ORDENE_ID IN( " + " select ordene_id from infi_tb_204_ordenes a where a.uniinv_id=" + unidadInversion + " and a.ordene_id in( " + " select ordene_id from infi_tb_207_ordenes_operacion where trnf_tipo in('" + TransaccionFinanciera.DEBITO + "') " + " and status_operacion='"
				+ ConstantesGenerales.STATUS_APLICADA + "'" + " )  and ordsta_id in('" + StatusOrden.PROCESO_ADJUDICACION + "')  and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "') " + "and ordene_ped_monto = ordene_adj_monto)";
		// Cartera Propia
		sql[2] = "UPDATE infi_tb_204_ordenes SET ordsta_id='" + StatusOrden.ADJUDICADA + "' " + " WHERE uniinv_id=" + unidadInversion + " and ordsta_id in('" + StatusOrden.PROCESO_ADJUDICACION + "')  " + " and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "')";
		db.execBatch(_dso, sql);
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		procesosDAO = new ProcesosDAO(_dso);
		procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.ADJUDICACION);
		if (procesosDAO.getDataSet().count() > 0) {
			_record.addError("Adjudicación", "No se puede procesar la solicitud porque otra " + "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}
		return valido;
	}// fin isValid

	/**
	 * Inicia el proceso de adjudicación
	 */
	private void iniciarProceso() throws Exception {
		UsuarioDAO usu = new UsuarioDAO(_dso);

		proceso = new Proceso();
		proceso.setTransaId(TransaccionNegocio.ADJUDICACION);
		proceso.setUsuarioId(Integer.parseInt(usu.idUserSession(getUserName())));
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		/* Primero creamos el proceso */
		db.exec(_dso, procesosDAO.insertar(proceso));
		logger.info("Comenzó proceso: Adjudicación");

		// Prepara el dataset de mensajes
		mensajesPorRif.append("orden", java.sql.Types.VARCHAR);
		mensajesPorRif.append("mensaje", java.sql.Types.VARCHAR);
	}

	/**
	 * Finaliza el proceso de adjudicación
	 */
	private void terminarProceso() {
		try {
			if (proceso != null) {
				db.exec(this._dso, procesosDAO.modificar(proceso));
			}

			if (procesosDAO != null) {
				procesosDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (proceso != null) {
				logger.info("Finalizó proceso: Adjudicación");
			}
		}
	}

	/**
	 * Obtiene las transacciones fijas según la unidad y el vehículo tomador
	 * 
	 * @param unidadInversion
	 *            id de la unidad de inversión
	 * @param vehiculoTomador
	 *            vehículo tomador
	 * @throws Exception
	 *             en caso de error
	 */
	protected void getTransaccionesFijas(String unidadInversion) throws Exception {

		if (transaccionesFijasCache.containsKey(unidadInversion)) {
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);

		} else {

			fijaDAO.listarTransaccionesFijasAdjudicacion(unidadInversion);
			DataSet ds = fijaDAO.getDataSet();

			transaccionesFijasCache.put(unidadInversion, ds);
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);

		}
	}

	/**
	 * Genera los registros que deben enviarse a opics sólo si es unidad de inversión SITME
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	protected void generarOpics(Orden orden, ArrayList<String> querys) throws Exception {
		if (orden.getFechaPactoRecompra() != null) {
			UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
			int idUserSession = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
			IngresoOpicsSitme ingresoOpics = new IngresoOpicsSitme(_dso, _app, idUserSession, _req.getRemoteAddr(), getUserName());
			querys.addAll(ingresoOpics.rentaFija(orden));
		}
	}

	/**
	 * Verifica si la empresa esta registrada
	 * 
	 * @param rif
	 *            rif de la empresa a buscar
	 * @return El código de la empresa registrada. Si no la encuentra devuelve null
	 * @throws Exception
	 *             en caso de error
	 */
	protected String verificarContraparte(String rif) throws Exception {
		String codigo = null;
		if (rif != null) {
			logger.debug("Buscando rif de la empresa " + rif);

			if (empresasCache == null) {
				empresaDefinicionDAO.listarTodas();
				empresasCache = empresaDefinicionDAO.getDataSet();
			}

			if (empresasCache.count() > 0) {
				empresasCache.first();
				while (empresasCache.next()) {
					logger.debug("Rif de empresa: " + empresasCache.getValue("empres_rif"));
					if (empresasCache.getValue("empres_rif").trim().equals(rif.trim())) {
						logger.debug("Rif encontrado");
						codigo = empresasCache.getValue("empres_id");
						break;
					}
				}
			}
		}
		return codigo;
	}

	/**
	 * Genera el mensaje para Estadistica
	 * 
	 * @param querys
	 * @param orden
	 * @param montoAdjudicado
	 * @param numeroCedula
	 * @param tipoPersona
	 * @return mensajeEstadistica: objeto con el mensaje para estadistica
	 * @throws Exception
	 */
	private void generarMensajeEstadistica(ArrayList<String> querys, Orden ordenOriginal, DataSet _cliente, BigDecimal montoAdjudicado, String broker, long consecutivo) throws Exception {

		// Generar Mensaje estadístico si es título SITME
		if (ordenOriginal.getFechaPactoRecompra() != null && ordenOriginal.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) && (ordenOriginal.getStatus().equals(StatusOrden.ENVIADA) || ordenOriginal.getStatus().equals(StatusOrden.REGISTRADA))) {

			logger.debug("Creando Mensaje Estadistica Orden: " + ordenOriginal.getIdOrden() + "...");

			MensajeEstadistica mensajeEstadistica = new MensajeEstadistica();
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);
			OrdenOperacion ordenOperacion = new OrdenOperacion();
			String cedRifCliente = "";
			String tipoPersona = "";
			String nombreCliente = "";

			// Datos del Cliente
			if (_cliente.count() > 0) {
				_cliente.first();
				_cliente.next();
				cedRifCliente = _cliente.getValue("client_cedrif");
				tipoPersona = _cliente.getValue("tipper_id");
				nombreCliente = _cliente.getValue("client_nombre");
			}

			// Datos de las Operaciones:
			ArrayList<OrdenOperacion> listaOperaciones = ordenOriginal.getOperacion();
			if (!listaOperaciones.isEmpty()) {
				// Para obtener el Numero de Cuenta, tomar la 1era operacion ya que es el mismo para Todas
				ordenOperacion = listaOperaciones.get(0);
			}

			// Setear Valores de la Venta al Mensaje:
			mensajeEstadistica.set(mensajeEstadistica.CEDULA_RIF_PASAPORTE, tipoPersona + cedRifCliente);
			mensajeEstadistica.set(mensajeEstadistica.FECHA_PROCESO, new Date());
			mensajeEstadistica.set(mensajeEstadistica.CONCEPTO_ESTADISTICO, MensajeEstadistica.V_CONCEPTO_VENTA);
			mensajeEstadistica.set(mensajeEstadistica.CODIGO_DIVISA, MensajeEstadistica.V_CODIGO_DIVISA_DOLAR);
			mensajeEstadistica.set(mensajeEstadistica.NOMBRE_RAZON_SOCIAL, nombreCliente);
			mensajeEstadistica.set(mensajeEstadistica.MONTO_MONEDA_EXTRANJERA, montoAdjudicado.doubleValue());
			mensajeEstadistica.set(mensajeEstadistica.HORA, com.bdv.infi.util.Utilitario.DateToString(new Date(), "HHmm"));
			mensajeEstadistica.set(mensajeEstadistica.FECHA_VALOR, ordenOriginal.getFechaValor());
			mensajeEstadistica.set(mensajeEstadistica.USUARIO, getUserName());
			mensajeEstadistica.set(mensajeEstadistica.REFERENCIA_BANKTRADE, "B" + Utilitario.rellenarCaracteres(broker, '0', 4, false) + Utilitario.rellenarCaracteres(String.valueOf(consecutivo), '0', 5, false));// PENDIENTE
			mensajeEstadistica.set(mensajeEstadistica.NOMBRE_DEL_BENEFICIARIO, nombreCliente);
			mensajeEstadistica.set(mensajeEstadistica.NUM_CUENTA_CLIENTE, mensajeOpicsDAO.convertirCuenta20A12(ordenOperacion.getNumeroCuenta()));
			mensajeEstadistica.set(mensajeEstadistica.FIRMAS_AUTORIZADAS, getUserName());
			mensajeEstadistica.set(mensajeEstadistica.CEDULA_RIF_BENEFICIARIO, tipoPersona + cedRifCliente);

			// Establecer valores por Defecto al Mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeEstadistica);//

			if (mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA) != null && (mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA).equals("") || mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA).equals("0"))) {
				mensajeEstadistica.set(mensajeEstadistica.CODIGO_OFICINA, ordenOriginal.getSucursal());
			}
			mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_BOLIVAR, ordenOriginal.getPrecioCompra());

			// TODO validar modificacion
			// mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_DOLAR , "1");
			mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_DOLAR, ordenOriginal.getTasaCambio());

			if (tipoPersona.equalsIgnoreCase("G")) {
				mensajeEstadistica.set(mensajeEstadistica.SECTOR_PUBLICO_PRIVADO, MensajeEstadistica.V_SECTOR_PUBLICO);
			} else {
				mensajeEstadistica.set(mensajeEstadistica.SECTOR_PUBLICO_PRIVADO, MensajeEstadistica.V_SECTOR_PRIVADO);
			}

			mensajeEstadistica.setUsuarioNM(getUserName());
			mensajeEstadistica.setOrdeneId(Integer.parseInt(String.valueOf(ordenOriginal.getIdOrden())));
			mensajeEstadistica.setFechaValor(ordenOriginal.getFechaPactoRecompra());

			// Generar sentecias sql para guardar el mensaje de Estadística
			String[] sentenciasMje = mensajeDAO.ingresar(mensajeEstadistica);

			// Agregar sentencias de Mensaje a vector Global de queries a ejecutar
			for (int k = 0; k < sentenciasMje.length; k++) {
				// System.out.println("SQL ESTADISTICA " + sentenciasMje[k]);
				querys.add(sentenciasMje[k]);
			}
		}
	}

	/**
	 * Genera el mensaje para BCV
	 * 
	 * @return mensajeBcv: objeto con el mensaje para bcv
	 * @throws Exception
	 */
	private void generarMensajeBCV(ArrayList<String> querys, Orden ordenOriginal, DataSet _cliente, BigDecimal montoAdjudicado, String broker, long consecutivo) throws Exception {

		// Verificar si el tipo de producto es SITME y si la orden aun se encuentra en status ENVIADA
		if (ordenOriginal.getFechaPactoRecompra() != null && ordenOriginal.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) && (ordenOriginal.getStatus().equals(StatusOrden.ENVIADA) || ordenOriginal.getStatus().equals(StatusOrden.REGISTRADA))) {

			if (logger.isDebugEnabled()) {
				logger.debug("Creando Mensaje BCV Orden: " + ordenOriginal.getIdOrden() + "...");
			}

			MensajeDAO mensajeDAO = new MensajeDAO(_dso);
			MensajeBcv mensajeBcv = new MensajeBcv();
			OrdenTitulo titulo = new OrdenTitulo();
			String cedRifCliente = "";
			String tipoPersona = "";
			String nombreCliente = "";

			// Datos del Cliente
			if (_cliente.count() > 0) {
				_cliente.first();
				_cliente.next();
				cedRifCliente = _cliente.getValue("client_cedrif");
				tipoPersona = _cliente.getValue("tipper_id");
				nombreCliente = _cliente.getValue("client_nombre");
			}

			// Datos del Titulo: (SITME ES 1 solo TITULO)
			ArrayList<OrdenTitulo> listaTitulos = ordenOriginal.getOrdenTitulo();
			if (!listaTitulos.isEmpty()) {
				// Obtiene el Titulo
				titulo = listaTitulos.get(0);
			}

			// Setear Valores al Mensaje para Interfaz BCV
			mensajeBcv.set(mensajeBcv.SECUENCIA, consecutivo);
			mensajeBcv.set(mensajeBcv.NOMBRE_RAZON_SOCIAL, nombreCliente);
			mensajeBcv.set(mensajeBcv.RIF_LETRA, tipoPersona);
			mensajeBcv.set(mensajeBcv.RIF_NUMERO, cedRifCliente);
			mensajeBcv.set(mensajeBcv.TITULO, obtenerDatosTitulo(mensajeBcv, titulo.getTituloId()));
			mensajeBcv.set(mensajeBcv.OPERADOR_COMPRA, "001"); // TODO Pendiente
			mensajeBcv.set(mensajeBcv.OPERADOR_VENTA, "001"); // TODO Pendiente
			mensajeBcv.set(mensajeBcv.FECHA_OPERACION, new Date());
			mensajeBcv.set(mensajeBcv.FECHA_VALOR, ordenOriginal.getFechaValor());
			mensajeBcv.set(mensajeBcv.PRECIO_TITULO, titulo.getPorcentajeRecompra());
			mensajeBcv.set(mensajeBcv.MONTO_DOLARES, montoAdjudicado.doubleValue());
			mensajeBcv.set(mensajeBcv.CONCEPTO, ordenOriginal.getConceptoId());
			mensajeBcv.set(mensajeBcv.SECTOR, ordenOriginal.getSectorProductivoId());
			mensajeBcv.setUsuarioNM(getUserName());
			mensajeBcv.setOrdeneId(Integer.parseInt(String.valueOf(ordenOriginal.getIdOrden())));
			mensajeBcv.setFechaValor(new Date());
			mensajeBcv.set(mensajeBcv.BROKER, broker);

			// Establecer valores por defecto al mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeBcv);

			// Configuracion de la fecha valor para el mensaje BCV
			mensajeBcv.setFechaValor(ordenOriginal.getFechaValor());

			// Generar sentecias sql para guardar el mensaje de Estadística
			String[] sentenciasMje = mensajeDAO.ingresar(mensajeBcv);

			// Agregar sentencias de Mensaje a vector Global de queries a ejecutar
			for (int k = 0; k < sentenciasMje.length; k++) {
				querys.add(sentenciasMje[k]);
			}
		}

	}

	/**
	 * Obtiene el valor final para el campo de TITULO de BCV. Usa cache.
	 * 
	 * @param mensajeBcv
	 *            objeto correspondiente al mensajeBcv
	 * @param idTitulo
	 *            id del título a buscar
	 * @return valor final que debe llevar el campo
	 * @throws Exception
	 *             en caso de error
	 */
	private String obtenerDatosTitulo(MensajeBcv mensajeBcv, String idTitulo) throws Exception {
		String valorFinal = "";
		if (hashTituloBCV.containsKey(idTitulo)) {
			valorFinal = hashTituloBCV.get(idTitulo);
		} else {
			valorFinal = mensajeBcv.obtenerValorFinalTitulo(_dso, idTitulo);
			hashTituloBCV.put(idTitulo, valorFinal);
		}
		return valorFinal;
	}

	private void adjudicarSitmeClaveNet() throws Exception {

		BigDecimal totalAdj = null;// new BigDecimal(0);;
		String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
		String nombreDocumento = getSessionObject("nombreDocumento").toString();

		// setearCobroEnLineaDeLaUnidad();
		numero_oficina = (String) _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);

		/*
		 * FileInputStream documento = new FileInputStream(contenidoDocumento);//contenido del documento a leer String xlsNombre = nombreDocumento;// nombre del archivo que estamos leyendo
		 */
		FileInputStream documento = new FileInputStream(contenidoDocumento);// contenido del documento a leer
		HSSFSheet hoja = null;// hoja
		HSSFCell celdaControl = null;// primera celda o columna
		HSSFCell ui = null;// columna Unidad de Inversion
		HSSFCell codOrden = null;// columna cedula
		HSSFCell cedula = null;// columna cedula

		HSSFCell precioPacto = null;// columna precio que compro el cliente
		HSSFCell precioNegociado = null;// columna precio asignado de venta al cliente
		HSSFCell montoAsignado = null;// columna monto adjudicado

		HSSFCell contraparte = null;// columna de contraparte

		HSSFCell broker = null;// columna de broker
		HSSFCell consecutivo = null;// columna de consecutivo
		HSSFCell fecha_valor = null;// columna de fecha valor que viene de clavenet
		HSSFCell actividadEconomica = null;// columna de codigo de actividad economica
		HSSFCell sectorProductivo = null;// columna de codigo de sector productivo

		HSSFRow row = null; // fila

		String idCliente = null;// Id del cliente

		// String cuentaCliente = null;//numero cuenta cliente. Obtenida de la orden
		String monedaLocal = null;

		BigDecimal montoPedido = new BigDecimal(0);// monto que pidio el cliente

		BigDecimal montoAdjudicado = null;// new BigDecimal(0); //monto adjudicado

		String ejecucion_id = MSCModelExtend.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS); // Tomamos la secuencia fuera del for para que todos los registros tengan el mismo valor

		setSessionObject("ejecucion_id", ejecucion_id);

		try {

			empresaDAO = new EmpresaDefinicionDAO(_dso);
			ordenDAOClavenet = new OrdenDAO(_dso);
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			clienteDAOClavenet = new ClienteDAO(_dso);
			clienteCuentasDAO = new ClienteCuentasDAO(_dso);
			UnidadInversionDAO unidadInvDAO = new UnidadInversionDAO(_dso);
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			TitulosDAO ordenesTitulos = new TitulosDAO(_dso);
			actividadEconomicaDAO = new ActividadEconomicaDAO(_dso);
			sectorProductivoDAO = new SectorProductivoDAO(_dso);

			/* VARIABLES NUEVAS */
			BigDecimal precioCompra = new BigDecimal(0);
			cero = new BigDecimal(0);// dividir entre 100, usado para calculo de porcentaje

			double unidadInversionTasaPool = 0.0;
			Orden orden = null;
			OrdenDetalle ordenDetalle = null;
			// BigDecimal tasaCambioUI=new BigDecimal(0);
			double tasaCambioUI;
			String empresaID = null;
			String contraparteID = null;
			Integer unidadInvId = 0;
			long ordenCedulaCliente;
			String[] ordenesStatus = { StatusOrden.ENVIADA };
			BigDecimal totalMontoCapital = new BigDecimal(0);
			String clienteCuenta;
			BigDecimal pctComision = new BigDecimal(0);
			OrdenTitulo ordenTitulo = new OrdenTitulo(); // com.bdv.infi_toma_orden.data.
			double precioNegociadoSalida;
			String fechaValorString = null;
			Calendar fechaValor = Calendar.getInstance();
			String ordenId;
			String stringBroker = null;
			ArrayList<String> listaActividadesEconomicas = new ArrayList<String>();
			ArrayList<String> listaSectoresProductivos = new ArrayList<String>();
			long consecutivoNumeric = 0;
			String idUnivGenerica = null;
			BigDecimal preNegociado = new BigDecimal(0);
			ArrayList<OrdenTitulo> ordenesTitulo = null;
			String nombreUnidadInversion = null;
			String numeroRetencion = null;

			String cuentaAbono = null;
			// controlDAO.ListarExiste(Long.parseLong(ejecucion_id)); //TODO VERIFICAR ESCRITURA DE ARCHIVO
			boolean insertadoArchivoRecepcion = false;

			FactoryAltair factoryAltair = new FactoryAltair(_dso, _app, true);
			factoryAltair.nombreUsuario = getUserName();
			factoryAltair.ipTerminal = _req.getRemoteAddr();

			HSSFWorkbook libro = new HSSFWorkbook(documento);
			/***************************************/

			MonedaDAO monedaDAO = new MonedaDAO(_dso);
			monedaLocal = monedaDAO.listarIdMonedaLocal();

			// BUSQUEDA DE VEHICULO_ID de BDV
			ResultSet result;
			Statement statementVehiculo;
			Statement statement;
			transaccion = new com.bdv.infi.dao.Transaccion(_dso);

			String vehiculoID = null;
			String vehiculo_rif = null;
			transaccion.begin();
			statementVehiculo = transaccion.getConnection().createStatement();
			result = statementVehiculo.executeQuery(controlArchivoDAO.listar_VALORES_SITME(null, ConstantesGenerales.LISTAR_INFORMACION_VEHICULO));

			while (result.next()) {

				vehiculoID = result.getString("vehicu_id");
				vehiculo_rif = result.getString("vehicu_rif");

			}

			// Validacion de Actividad Economica
			actividadEconomicaDAO.listar();
			while (actividadEconomicaDAO.getDataSet().next()) {
				listaActividadesEconomicas.add(actividadEconomicaDAO.getDataSet().getValue("CODIGO_ID"));
			}

			statementVehiculo.close();
			sectorProductivoDAO.listar();
			while (sectorProductivoDAO.getDataSet().next()) {
				listaSectoresProductivos.add(sectorProductivoDAO.getDataSet().getValue("sector_id"));
			}
			// abrir el archivo xls
			for (int i = 0; i < libro.getNumberOfSheets(); i++) {

				hoja = libro.getSheet(libro.getSheetName(i));// objeto hoja

				for (int j = 1; j < hoja.getPhysicalNumberOfRows(); j++) {// recorrido de filas

					row = hoja.getRow(j);// Obtener la fila j de la hoja
					// vas leyendo por cada celda (columna) de la fila
					// para cada fila el numero de columnas es fijo
					celdaControl = row.getCell((short) 0);
					int tipo = celdaControl.getCellType();
					if (tipo == HSSFCell.CELL_TYPE_BLANK) {// romperemos la lectura de filas al encontrar la primera celda vacia (fin de registros)
						if (j == 1) {
							// System.out.println("Archivo sin Registros para Procesar o Mal Formado");
							j = hoja.getPhysicalNumberOfRows();
							logger.info("* Archivo sin Registros para Procesar o Mal Formado, Verifique e Intente Nuevamente)\n");
							break;
						}
						if (j > 1) {
							// System.out.println(" Fin de Lectura de Registros Exitosamente");
							j = hoja.getPhysicalNumberOfRows();

							logger.info("* Fin de Lectura de Registros Exitosamente.\n");
							break;
						}
					}// FIN DE FINAL DE LECTURA POR PRIMERA FILA VACIA

					ui = row.getCell((short) 0);
					codOrden = row.getCell((short) 1);
					cedula = row.getCell((short) 2);// obtenemos valor
					precioPacto = row.getCell((short) 3);
					precioNegociado = row.getCell((short) 4);

					montoAsignado = row.getCell((short) 5);

					contraparte = row.getCell((short) 6); // Rif de la contraparte
					broker = row.getCell((short) 7);
					consecutivo = row.getCell((short) 8);
					fecha_valor = row.getCell((short) 9);
					actividadEconomica = row.getCell((short) 10);
					sectorProductivo = row.getCell((short) 11);

					String dia = null;

					String mes = null;

					String anio = null;

					try {

						try {
							dia = fecha_valor.toString().substring(0, 2);

							mes = fecha_valor.toString().substring(3, 5);

							anio = fecha_valor.toString().substring(6, 10);
						} catch (Exception e) {
							// System.out.println("Ocurrio un error con el campo Fecha Valor del archivo, por favor verifique que el formato sea DD/MM/YYYY. Descripcion del error " + e.getMessage());
							logger.error("Ocurrio un error con el campo Fecha Valor del archivo, por favor verifique que el formato sea DD/MM/YYYY. Descripcion del error " + e.getMessage());
							continue;
						}

						fechaValorString = new String();
						fechaValorString = fechaValorString.concat(dia).concat("-").concat(mes).concat("-").concat(anio);

						if (Double.parseDouble(montoAsignado.toString()) < 0) {
							logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - " + "El campo de monto Asignado en el archivo excel posee un valor menor o igual a cero para la orden");
							// System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El campo de monto Asignado en el archivo excel posee un valor menor a cero para la orden ");
							continue;
						}

						if (Double.parseDouble(precioPacto.toString()) <= 0) {
							logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - El valor colocado en el campo Precio Compra es igual o menor a cero ");
							// System.out.println("El valor colocado en el campo  Precio Compra es igual o menor a cero para la orden " + codOrden.toString().replaceAll("\\.0", ""));
							continue;
						}

						if (Double.parseDouble(precioNegociado.toString()) <= 0) {
							logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - El valor colocado en el campo Precio Negociado es igual o menor a cero ");
							// System.out.println("El valor colocado en el campo Precio Negociado es igual o menor a cero para la orden " + codOrden.toString().replaceAll("\\.0", ""));
							continue;
						}

						DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
						String s = df.format(new Date());

						if ((Utilitario.StringToDate(s, "dd-MM-yyyy")).compareTo(Utilitario.StringToDate(fechaValorString, "dd-MM-yyyy")) > 0) {
							logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - La fecha valor ingresada de la orden es menor a la fecha actual por favor verifique");
							// System.out.println("La fecha valor ingresada para la orden " + codOrden.toString().replaceAll("\\.0", "") + " es menor a la fecha actual por favor verifique");
							continue;
						}

						// TODO Seccion de codigo a la espera de confirmacion de implantacion
						empresaDAO.listarPorRif(contraparte.toString());

						if (!(empresaDAO.getDataSet().count() > 0)) {
							logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - El Rif de contraparte no ha concordado con ningun registro de la Base de Datos");
							// System.out.println("El Rif de contraparte no ha concordado con ningun registro de Base de Datos");
							continue;
						} else {

							empresaDAO.getDataSet().first();
							if (empresaDAO.getDataSet().next()) {
								contraparteID = empresaDAO.getDataSet().getValue("empres_id");
							}

							if (contraparteID != null) {
								contraparteID = contraparteID.trim();
							} else {
								logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - No contiene ID de contraparte");
								// System.out.println("La orden " + codOrden.toString().replaceAll("\\.0", "") + " no contiene ID de contraparte");
								continue;
							}

						}

						// Consultas Actividad y Sector Economico
						if (!listaActividadesEconomicas.contains(actividadEconomica.toString())) {
							logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - El valor ingresado en el campo Actividad Económica no corresponde a ningun código de Actividad Económica existente");
							// System.out.println("Orden: " +codOrden.toString().replaceAll("\\.0", "")+ " - El valor ingresado en el campo Actividad Económica no corresponde a ningun codigo de Actividad Económica existente");
							continue;
						}

						if (!listaSectoresProductivos.contains(sectorProductivo.toString())) {
							logger.info("Orden: " + codOrden.toString().replaceAll("\\.0", "") + " - El valor ingresado en el campo Sector Productivo no corresponde a ningun código de Sector Productivo existente");
							// System.out.println("Orden: " +codOrden.toString().replaceAll("\\.0", "")+ " - El valor ingresado en el campo Sector Productivo no corresponde a ningun codigo de Sector Productivo existente");
							continue;
						}

						unidadInvDAO.listarPorNombre(ui.toString(), ConstantesGenerales.ID_TIPO_PRODUCTO_SITME, null);

						totalAdj = new BigDecimal(0);
						if (unidadInvDAO.getDataSet().next()) {// IF UNIDAD DE INVERSION EXISTE

							orden = new Orden();

							nombreUnidadInversion = ui.toString();
							getTransaccionesFijasClaveNet(unidadInvDAO.getDataSet().getValue("UNDINV_ID"), vehiculoID);

							ordenId = codOrden.toString().replaceAll("\\.0", "");
							// VARIABLES RELACIONADAS CON LA UNIDAD DE INVERSION
							unidadInvId = Integer.parseInt(unidadInvDAO.getDataSet().getValue("UNDINV_ID"));
							tasaCambioUI = Double.parseDouble(unidadInvDAO.getDataSet().getValue("UNDINV_TASA_CAMBIO"));
							empresaID = unidadInvDAO.getDataSet().getValue("EMPRES_ID");
							unidadInversionTasaPool = Double.parseDouble(unidadInvDAO.getDataSet().getValue("UNDINV_TASA_POOL"));
							// System.out.println("Orden:  " + ordenId);

							ordenDAOClavenet.listarDatosBasicosOrdenPorStatus(Long.parseLong(ordenId), StatusOrden.ENVIADA);

							String fechaLiquidacionUI = unidadInvDAO.getDataSet().getValue("UNDINV_FE_LIQUIDACION");

							// System.out.println("Fecha liquidacion UI: " + fechaLiquidacionUI);
							// System.out.println("Fecha Valor plantilla: " + fechaValorString);

							if ((Utilitario.StringToDate(fechaValorString, "dd-MM-yyyy")).compareTo(Utilitario.StringToDate(fechaLiquidacionUI, "dd-MM-yyyy")) > 0) {
								logger.info("Orden " + codOrden.toString().replaceAll("\\.0", "") + " - La fecha valor de la plantilla excel es mayor a la fecha liquidacion de la Unidad de Inversion");
								// System.out.println("Orden "+codOrden.toString().replaceAll("\\.0", "")+" - La fecha valor de la plantilla excel es mayor a la fecha liquidacion de la Unidad de Inversion");
								continue;
							}

							if (ordenDAOClavenet.getDataSet().count() == 0) {// Validacion IF orden NO existe en la tabla 204
								logger.info("Orden: " + ordenId + " -  Orden no registrada en la tabla de ordenes o ya ha sido procesada");
								// System.out.println("La orden "+ ordenId+ " ingresada en el archivo XLS no esta registrada en la tabla de ordenes  o ya ha sido procesada");
								continue;

							} else {// IF existente en la tabla 204 TRUE

								ordenDAOClavenet.getDataSet().next();
								precioNegociadoSalida = Double.parseDouble(precioNegociado.toString());

								montoPedido = BigDecimal.valueOf(Double.parseDouble(ordenDAOClavenet.getDataSet().getValue("ORDENE_PED_MONTO")));
								montoPedido = montoPedido.setScale(2, BigDecimal.ROUND_HALF_EVEN);

								pctComision = BigDecimal.valueOf(Double.parseDouble(ordenDAOClavenet.getDataSet().getValue("ORDENE_COMISION_OPERACION")));
								pctComision = pctComision.setScale(2, BigDecimal.ROUND_HALF_EVEN);

								montoAdjudicado = new BigDecimal(Double.parseDouble(montoAsignado.toString()));
								montoAdjudicado = montoAdjudicado.setScale(2, BigDecimal.ROUND_HALF_EVEN);

								precioCompra = new BigDecimal(Double.parseDouble(precioPacto.toString()));
								precioCompra = precioCompra.setScale(2, BigDecimal.ROUND_HALF_EVEN);

								preNegociado = BigDecimal.valueOf(Double.parseDouble(precioNegociado.toString()));

								clienteCuenta = ordenDAOClavenet.getDataSet().getValue("CTECTA_NUMERO");

								/* validaciones basicas */

								/* VALIDACIONES ADICIONALES */
								String ced_rif = cedula.toString().substring(1);
								String tipper_id = cedula.toString().substring(0, 1);

								logger.info("ced_rif: " + ced_rif + ", tipper_id: " + tipper_id);

								// cliente exista
								clienteDAOClavenet.listarCliente(0, tipper_id, Long.parseLong(ced_rif));
								if (!clienteDAOClavenet.getDataSet().next()) {
									// System.out.println("El cliente "+ cedula.toString()+ " No se encuentra registrado en el sistema. Se debe verificar el numero de cedula");
									logger.info("Orden: " + ordenId + "El cliente " + cedula.toString() + " No se encuentra registrado en el sistema. Se debe verificar el numero de cedula");
									continue;
								} else {

									idCliente = clienteDAOClavenet.getDataSet().getValue("CLIENT_ID");

								}

								// ORDEN ES DE TIPO SITME
								if (!ordenDAOClavenet.getDataSet().getValue("TIPO_PRODUCTO_ID").equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)) {
									// System.out.println("La orden "+ ordenId+ " contiene un tipo de producto diferente a SITME, no puede ser procesada");
									logger.info("La orden " + ordenId + " contiene un tipo de producto diferente a SITME, no puede ser procesada");
									continue;
								}
								orden.setTipoProducto(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME);
								// cliente dueño de la orden

								String cliente = ordenDAOClavenet.getDataSet().getValue("CLIENT_ID");

								logger.info("Id cliente (plantilla): " + idCliente);
								logger.info("Id cliente (orden): " + cliente);
								if (!cliente.equals(idCliente)) {
									// System.out.println("Orden: " + ordenId+ " - El cliente " + cedula.toString() + " no es dueño de la orden");
									logger.info("Orden: " + ordenId + " - El cliente " + cedula.toString() + " no es dueño de la orden");
									continue;
								}

								// estatus de la orden "REGISTRADA"
								if (!ordenDAOClavenet.getDataSet().getValue("ORDSTA_ID").equalsIgnoreCase(StatusOrden.ENVIADA)) {
									// System.out.println("La orden "+ ordenDAOClavenet.getDataSet().getValue("ORDENE_ID") + " no se encuentra en estatus REGISTRADA, no puede ser procesada");
									logger.info("La orden " + ordenId + " no se encuentra en estatus REGISTRADA, no puede ser procesada");
									continue;
								}

								// Orden sea de tipo clavenet
								idUnivGenerica = ordenDAOClavenet.getDataSet().getValue("UNIINV_ID");

								if (!(unidadInvDAO.getInstrumentoFinancieroPorUI(Long.parseLong(idUnivGenerica)).equalsIgnoreCase(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P) || unidadInvDAO.getInstrumentoFinancieroPorUI(Long.parseLong(idUnivGenerica)).equalsIgnoreCase(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E))) {
									// System.out.println("La orden " +ordenId + " no es una orden tomada desde ClaveNet por favor verifique ");
									logger.info("La orden " + ordenId + " no es una orden tomada desde ClaveNet por favor verifique ");
									continue;
								}

								/* VALIDACIONES DE ACTIVIDAD Y SECTOR PRODUCTIVO */

								interesesCaidos = new BigDecimal(0);
								interesesCaidosBs = new BigDecimal(0);

								orden.setIdOrden(Long.parseLong(ordenId));
								orden.setIdEjecucion(Long.parseLong(ejecucion_id));

								if (!(montoAdjudicado.doubleValue() == cero.doubleValue())) {

									stringBroker = broker.toString();
									try {
										consecutivoNumeric = Long.parseLong(consecutivo.toString());
									} catch (Exception e) {
										// System.out.println("Orden: "+ orden.getIdOrden()+ " - Error en transformacion del campo CONSECUTIVO: " + e.getMessage());
										logger.error("Orden: " + orden.getIdOrden() + " - Error en transformacion del campo CONSECUTIVO: " + e.getMessage());
										continue;
									}

									orden.setFechaValorString("");
									/* COFIGURACION GENERAL DE LA ORDEN */
									orden.setIdCliente(Long.parseLong(clienteDAOClavenet.getDataSet().getValue("CLIENT_ID").toString()));

									orden.setClienteRifCed(cedula.toString().toUpperCase()); // ITS-824
									orden.setTasaCambio(tasaCambioUI);
									orden.setActividadEconomicaId(actividadEconomica.toString());
									orden.setSectorProductivoId(sectorProductivo.toString());

									orden.setIdEmpresa(empresaID); // EMPRES_ID

									orden.setContraparte(contraparteID);
									orden.setFechaPactoRecompra(Utilitario.StringToDate(fecha_valor.toString(), "dd/MM/yyyy"));
									orden.setFechaOrden(Utilitario.StringToDate(ordenDAOClavenet.getDataSet().getValue("ORDENE_PED_FE_ORDEN"), "yyyy-MM-dd"));

									orden.setTasaPool(BigDecimal.valueOf(unidadInversionTasaPool));// ORDENE_TASA_POOL proviene de la unidad de inversion especifica
									orden.setMontoAdjudicado(montoAdjudicado.doubleValue()); // MONTO ADJUDICADO
									orden.setIdUnidadInversion(unidadInvId);// UNIDAD INVERSION ID

									orden.setPrecioCompra(precioCompra.doubleValue());// ORDENE_PED_PRECIO --> PRECIO DE COMPREA
									orden.setComisionOperacion(pctComision);
									orden.setFechaValorString(fecha_valor.toString());

									orden.setFechaValor(Utilitario.StringToDate(fecha_valor.toString(), "dd/MM/yyyy"));

									String conceptoID = ordenDAOClavenet.getDataSet().getValue("CONCEPTO_ID");

									if (conceptoID != null) {
										conceptoID = conceptoID.trim();
									}

									orden.setConceptoId(conceptoID);

									// Calculo de Intereses Caidos
									try {
										calcularIntCaidos(orden, BigDecimal.valueOf(tasaCambioUI), ui.toString(), fechaValorString, _dso.toString(), _dso, ui.toString());
									} catch (Exception e) {
										logger.error("Error en la orden " + orden.getIdOrden() + " en calculo de intereses Caidos : " + e.getMessage());
										continue;
									}

									// totalMontoCapital=BigDecimal.valueOf(((tasaCambioUI*orden.getMontoAdjudicado())*(orden.getPrecioCompra()/100)));
									totalMontoCapital = BigDecimal.valueOf(tasaCambioUI * orden.getMontoAdjudicado()).multiply(precioCompra.divide(new BigDecimal(100)));
									totalMontoCapitalOperaciones = totalMontoCapital;
									orden.setMonto(totalMontoCapital.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());

									BigDecimal montoComision = new BigDecimal(0);
									montoComision = (totalMontoCapital.add(interesesCaidosBs)).multiply((pctComision.divide(new BigDecimal(100))));
									totalMontoComisionOperaciones = montoComision.setScale(2, BigDecimal.ROUND_HALF_EVEN);

									orden.setMontoComisionOrden(montoComision.setScale(2, BigDecimal.ROUND_HALF_EVEN));

									// Validacion de monto solicitado no sea mayor a Monto Adj * Precio Negociado + Intereses caidos
									// ********** Validacion modificada por incidencia en PRODUCCION: El monto a verificar esta basado en el monto adjudicado por el PRECIO NEGOCIADO de la plantilla de adjudicacion ********
									totalAdj = montoAdjudicado.multiply(preNegociado.divide(new BigDecimal(100))).add(interesesCaidos);

									// Condicion monto adjudicado es mayor al monto solicitado
									if (totalAdj.compareTo(montoPedido) > 0) {

										logger.info("Orden: " + ordenId + " - Monto de Valor Efectivo mayor al monto solicitado _ Precio Negociado " + preNegociado.toString() + " Monto Adjudicado _  " + montoAdjudicado.toString());
										// System.out.println("La orden "+ ordenId+ " contiene un monto adjudicado mayor al monto solicitado");
										continue;
									}

									orden.setMontoInteresCaidos(interesesCaidosBs.doubleValue());

									// totalMontoCapital=tasaCambioUI.multiply(BigDecimal.valueOf(orden.getMontoAdjudicado())).multiply(BigDecimal.valueOf(orden.getPrecioCompra()).divide(new BigDecimal(100))).add(interesesCaidos);

									orden.setStatus(StatusOrden.ENVIADA);
									OrdenTitulo titulo = new OrdenTitulo();
									titulo.setTituloId(ui.toString());
									// Modificacion de setPorcentajeRecompra para colocar en el campo Precio del Titulo del mensaje BCV con el valor del precio de compra
									titulo.setPorcentajeRecompra(Double.parseDouble(precioPacto.toString()));
									ordenesTitulo = new ArrayList<OrdenTitulo>();
									ordenesTitulo.add(titulo);
									orden.setOrdenTitulo(ordenesTitulo);

									try {
										// Generacion de mensaje a BCV.
										generarMensajeBCV(querysTransacciones, orden, clienteDAOClavenet.getDataSet(), new BigDecimal(orden.getMontoAdjudicado()), broker.toString(), consecutivoNumeric);
										orden.setOrdenTitulo(null);
									} catch (Exception e) {
										logger.error("Orden: " + orden.getIdOrden() + " - Ocurrio un error en el proceso de creacion de mensaje a BCV. " + e.getMessage());
										// System.out.println("Orden: "+orden.getIdOrden() +" - Ocurrio un error en el proceso de creacion de mensaje a BCV. "+e.getMessage());

										continue;
										// flag Falta de throws Exception()
									}
									orden.setStatus(StatusOrden.PROCESO_ADJUDICACION);
								}

								ordenCLaveNet = new OrdenDAO(_dso);

								SolicitudClavenet solicitud = new SolicitudClavenet();
								solicitud.setIdOrdenInfi(orden.getIdOrden());
								ordenCLaveNet.mostrarSolicitudesClaveNet(solicitud);

								if (ordenCLaveNet.getDataSet().count() > 0) {
									ordenCLaveNet.getDataSet().next();

									numeroRetencion = ordenCLaveNet.getDataSet().getValue("NUM_BLOQUEO");
									// System.out.println("NUMERO DE RETENCION " + numeroRetencion);
									if (numeroRetencion != null && (!numeroRetencion.equals(""))) {
										numeroRetencion = numeroRetencion.trim();
										if (numeroRetencion.length() > 5) {
											numeroRetencion = numeroRetencion.substring(5);

										}
									}

								}

								orden.setCuentaCliente(clienteCuenta);// configuracion de cuenta cliente
								orden.setIdMoneda(monedaLocal);// configuracion de moneda local
								orden.setFechaAdjudicacion(new Date());

								// MONTO ADJUIDACADO IGUAL A CERO
								// if(totalAdj.doubleValue()==cero.doubleValue()) {

								// RESOLUCION INCIDENCIA ITS-C-4
								if (montoAdjudicado.doubleValue() == cero.doubleValue()) {

									// OPERACION DE MODIFICACION DE LA TABLA DE OPICS
									logger.info("Monto Adjudicado CERO para la orden: " + orden.getIdOrden());
									// System.out.println("Monto Adjudicado CERO para la orden: " + orden.getIdOrden());
									orden.setStatus(StatusOrden.PROCESO_ADJUDICACION);
									orden.setMontoAdjudicado(montoAdjudicado.doubleValue()); // MONTO ADJUDICADO
									orden.setComisionCero(true);

									try {

										querysTransacciones.add(ordenDAOClavenet.modificarOrdenesClaveNet(orden, StatusOrden.ENVIADA));

									} catch (Exception e) {
										// System.out.println("Ha ocurrido un error en la modificacion de la orden " + orden.getIdOrden()+" para la actualizacion de la tabla INFI_TB_204_ORDENES " + e.getMessage());
										logger.error("Ha ocurrido un error en la modificacion de la orden " + orden.getIdOrden() + " para la actualizacion de la tabla INFI_TB_204_ORDENES " + e.getMessage());
										continue;
									}
									generarOperaciones(ADJUDICADO_CERO, orden, ordenDAOClavenet, vehiculoID, BigDecimal.valueOf(tasaCambioUI), ui.toString(), Double.parseDouble(precioNegociado.toString()), BigDecimal.valueOf(Double.parseDouble(montoAsignado.toString())), stringBroker, consecutivoNumeric, numeroRetencion);

									// actualizarOrdenesOPICS(orden.getIdOrden(),StatusOrden.NO_ADJUDICADA,StatusOrden.EN_TRAMITE,montoAdjudicado, controlArchivoDAO);
								} else {

									// Configuracion de la Informacion de la INSTRUCCION DE PAGO
									clienteCuentaEntidad = new CuentaCliente();
									clienteCuentaEntidad.setIdOrden(orden.getIdOrden());
									clienteCuentaEntidad.setClient_id(Long.parseLong(idCliente));
									clienteCuentaEntidad.setCtecta_uso(UsoCuentas.RECOMPRA);

									// consulta del tipo de cuenta ha realizar el abono: 1)Cuenta en el exterior 2)Cuenta nacional en Dolares
									cuentaAbono = ordenDAOClavenet.getDataSet().getValue("CTA_ABONO"); // flag daniel

									// Verifica el tipo de cuenta abono especificado desde clavenet personal
									if (cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO)) {// Si especifica cuenta en el extranjero se configura el tipo de instruccion como cuenta Swift
										clienteCuentaEntidad.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
									} else if (cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_NACIONAL)) {// Si especifica cuenta nacional en dolares se configura el tipo de instruccion como Cuenta Nacional en Dolares
										clienteCuentaEntidad.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_NACIONAL_DOLARES));
									}

									// OPERACIONES DE DETALLE DE ORDENES
									ordenDetalle = new OrdenDetalle();

									if (ordenCLaveNet.getDataSet().count() > 0) {

										String numPasaporte = ordenCLaveNet.getDataSet().getValue("NUM_PASAPORTE");
										if (numPasaporte != null && (!numPasaporte.equals(""))) {
											numPasaporte = numPasaporte.trim();
											ordenDetalle.setNumeroPasaporte(numPasaporte);
										}

										String fechaSalida = ordenCLaveNet.getDataSet().getValue("FECHA_SALIDA_VIAJE");
										if (fechaSalida != null && (!fechaSalida.equals(""))) {
											ordenDetalle.setFechaSalidaViaje(fechaSalida);
										}

										String fechaRegreso = ordenCLaveNet.getDataSet().getValue("FECHA_RETORNO_VIAJE");
										if (fechaRegreso != null && (!fechaRegreso.equals(""))) {
											ordenDetalle.setFechaRetornoViaje(fechaRegreso);
										}

										if (orden.getIdOrden() > 0) {
											ordenDetalle.setIdOrden(orden.getIdOrden());
										}

										String nroTicket = ordenCLaveNet.getDataSet().getValue("ID_ORDEN");

										if (nroTicket != null && (!nroTicket.equals(""))) {
											ordenDetalle.setNumeroTicketClavenet(Long.parseLong(nroTicket));
										}
										querysTransacciones.add(ordenDAO.insertarOrdenDetalle(ordenDetalle));
										// OPERACIONES DE DETALLE DE ORDENES

										String nombreCLienteClavenet = ordenCLaveNet.getDataSet().getValue("NOMBRE_CLIENTE");

										if (nombreCLienteClavenet != null && (!nombreCLienteClavenet.equals(""))) {
											nombreCLienteClavenet = nombreCLienteClavenet.trim();
											clienteCuentaEntidad.setCtecta_nombre(nombreCLienteClavenet.toUpperCase());
										}

										String nombreBeneficiario = ordenCLaveNet.getDataSet().getValue("NOMBRE_BENEFICIARIO");

										if (nombreBeneficiario != null && (!nombreBeneficiario.equals(""))) {
											nombreBeneficiario = nombreBeneficiario.trim();
											// clienteCuentaEntidad.setCtecta_nombre(nombreBeneficiario);
											clienteCuentaEntidad.setNombre_beneficiario(nombreBeneficiario.toUpperCase());
										}

										String ctaBanco = ordenCLaveNet.getDataSet().getValue("CTA_BANCO");

										if (ctaBanco != null && (!ctaBanco.equals(""))) {
											ctaBanco = ctaBanco.trim();
											clienteCuentaEntidad.setCtecta_bcocta_bco(ctaBanco.toUpperCase());
										}

										String cta_numero = ordenCLaveNet.getDataSet().getValue("CTA_NUMERO");
										if (cta_numero != null && (!cta_numero.equals(""))) {
											cta_numero = cta_numero.trim();
											clienteCuentaEntidad.setCtecta_numero(cta_numero);
										}

										String bcoDireccion = ordenCLaveNet.getDataSet().getValue("CTA_DIRECCION_C");

										if (bcoDireccion != null && (!bcoDireccion.equals(""))) {
											bcoDireccion = bcoDireccion.trim();
											clienteCuentaEntidad.setCtecta_bcocta_direccion(bcoDireccion.toUpperCase());
										}

										String bicSwift = ordenCLaveNet.getDataSet().getValue("CTA_BIC_SWIFT");

										if (bicSwift != null && (!bicSwift.equals(""))) {
											bicSwift = bicSwift.trim();
											clienteCuentaEntidad.setCtecta_bcocta_swift(bicSwift);
											clienteCuentaEntidad.setCtecta_bcocta_bic(bicSwift.toUpperCase());
										}

										String bcoTelefono = ordenCLaveNet.getDataSet().getValue("CTA_TELEFONO_3");

										if (bcoTelefono != null && (!bcoTelefono.equals(""))) {
											bcoTelefono = bcoTelefono.trim();
											clienteCuentaEntidad.setCtecta_bcocta_telefono(bcoTelefono);
										}

										String aba = ordenCLaveNet.getDataSet().getValue("CTA_ABA");

										if (aba != null && (!aba.equals(""))) {
											aba = aba.trim();
											clienteCuentaEntidad.setCtecta_bcocta_aba(aba.toUpperCase());
										}

										// String observacion="Orden "+orden.getIdOrden()+" Canal Clavenet";

										clienteCuentaEntidad.setCtecta_observacion(nombreUnidadInversion);

										String cedulaBeneficiario = ordenCLaveNet.getDataSet().getValue("CED_RIF_CLIENTE");
										long cedBeneficiario = Long.parseLong(cedulaBeneficiario.substring(1, cedulaBeneficiario.length() - 1));
										cedulaBeneficiario = String.valueOf(cedBeneficiario);
										if (cedulaBeneficiario != null && (!cedulaBeneficiario.equals(""))) {
											cedulaBeneficiario = cedulaBeneficiario.trim();
											clienteCuentaEntidad.setCedrif_beneficiario(cedulaBeneficiario.toUpperCase());
										}

										String codPaisDestino = ordenCLaveNet.getDataSet().getValue("COD_PAIS_DESTINO");
										if (codPaisDestino != null && (!codPaisDestino.equals(""))) {
											codPaisDestino = codPaisDestino.trim();
											clienteCuentaEntidad.setCodPaisDestino(codPaisDestino.toUpperCase());

										}

										String descPaisDestino = ordenCLaveNet.getDataSet().getValue("DESC_PAIS_DESTINO");
										if (descPaisDestino != null && (!descPaisDestino.equals(""))) {
											descPaisDestino = descPaisDestino.trim();
											clienteCuentaEntidad.setDescPaisDestino(descPaisDestino.toUpperCase());
										}

										String codPaisOrigen = ordenCLaveNet.getDataSet().getValue("COD_PAIS_ORIGEN");
										if (codPaisOrigen != null && (!codPaisOrigen.equals(""))) {
											codPaisOrigen = codPaisOrigen.trim();
											clienteCuentaEntidad.setCodPaisOrigen(codPaisOrigen.toUpperCase());
										}

										String descPaisOrigen = ordenCLaveNet.getDataSet().getValue("DESC_PAIS_ORIGEN");
										if (descPaisOrigen != null && (!descPaisOrigen.equals(""))) {
											descPaisOrigen = descPaisOrigen.trim();
											clienteCuentaEntidad.setDescPaisOrigen(descPaisOrigen.toUpperCase());
										}

										String codCiudadOrigen = ordenCLaveNet.getDataSet().getValue("COD_CIUDAD_ORIGEN");
										if (codCiudadOrigen != null && (!codCiudadOrigen.equals(""))) {
											codCiudadOrigen = codCiudadOrigen.trim();
											clienteCuentaEntidad.setCodCiudadOrigen(codCiudadOrigen.toUpperCase());
										}

										String descCiudadOrigen = ordenCLaveNet.getDataSet().getValue("DESC_CIUDAD_ORIGEN");
										if (descCiudadOrigen != null && (!descCiudadOrigen.equals(""))) {
											descCiudadOrigen = descCiudadOrigen.trim();
											clienteCuentaEntidad.setDescCiudadOrigen(descCiudadOrigen.toUpperCase());
										}

										String codEstadoOrigen = ordenCLaveNet.getDataSet().getValue("COD_ESTADO_ORIGEN");
										if (codEstadoOrigen != null && (!codEstadoOrigen.equals(""))) {
											codEstadoOrigen = codEstadoOrigen.trim();
											clienteCuentaEntidad.setCodEstadoOrigen(codEstadoOrigen.toUpperCase());
										}

										String descEstadoOrigen = ordenCLaveNet.getDataSet().getValue("DESC_ESTADO_ORIGEN");
										if (descEstadoOrigen != null && (!descEstadoOrigen.equals(""))) {
											descEstadoOrigen = descEstadoOrigen.trim();
											clienteCuentaEntidad.setDescEstadoOrigen(descEstadoOrigen.toUpperCase());
										}

										// -------Datos del Banco intermediario:----------------------------------------------------------------

										String bicIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_BIC_SWIFT");

										// Si tiene intermediario
										if (bicIntermediario != null && (!bicIntermediario.trim().equals(""))) {

											bicIntermediario = bicIntermediario.trim();
											clienteCuentaEntidad.setCtecta_bcoint_bic(bicIntermediario.toUpperCase());

											String abaIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_ABA");
											if (abaIntermediario != null && (!abaIntermediario.equals(""))) {
												abaIntermediario = abaIntermediario.trim();
												clienteCuentaEntidad.setCtecta_bcoint_aba(abaIntermediario.toUpperCase());
											}

											String nombreBancoIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_BANCO");
											if (nombreBancoIntermediario != null && (!nombreBancoIntermediario.trim().equals(""))) {
												nombreBancoIntermediario = nombreBancoIntermediario.trim();
												clienteCuentaEntidad.setCtecta_bcoint_bco(nombreBancoIntermediario.toUpperCase());
											}

											String direccionIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_DIRECCION");
											if (direccionIntermediario != null && (!direccionIntermediario.trim().equals(""))) {
												direccionIntermediario = direccionIntermediario.trim();
												clienteCuentaEntidad.setCtecta_bcoint_direccion(direccionIntermediario.toUpperCase());
											}

											String cuentaEnIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_NUMERO");
											if (cuentaEnIntermediario != null && (!cuentaEnIntermediario.equals(""))) {
												cuentaEnIntermediario = cuentaEnIntermediario.trim();
												clienteCuentaEntidad.setCtecta_bcoint_swift(cuentaEnIntermediario.toUpperCase());
											}

											String telefonoIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_TELEFONO");
											if (telefonoIntermediario != null && (!telefonoIntermediario.equals(""))) {
												telefonoIntermediario = telefonoIntermediario.trim();
												clienteCuentaEntidad.setCtecta_bcoint_telefono(telefonoIntermediario.toUpperCase());
											}

										}

										String[] clientesCuentasSql = null;

										clientesCuentasSql = clienteCuentasDAO.insertarClienteCuentas(clienteCuentaEntidad);

										for (String element : clientesCuentasSql) {
											// System.out.println("INSERT DE CLIENTES CUENTAS: " + element);
											querysTransacciones.add(element);

										}

									}

									// actualizarOrdenesOPICS(orden.getIdOrden(),StatusOrden.ADJUDICADA,StatusOrden.EN_TRAMITE,montoAdjudicado, controlArchivoDAO);
									// Configuracion de ordenes titulos

									ordenTitulo.setTituloId(ui.toString());
									ordenTitulo.setPorcentaje("100");
									ordenTitulo.setMonto(montoAdjudicado.doubleValue());
									ordenTitulo.setUnidades(montoAdjudicado.doubleValue());
									ordenTitulo.setPorcentajeRecompra(precioNegociadoSalida);
									ordenTitulo.setPrecioMercado(precioNegociadoSalida);
									ordenTitulo.setMontoIntCaidos(interesesCaidos);
									ordenTitulo.setMontoNeteo(new BigDecimal(0));
									ArrayList<OrdenTitulo> arregloOrdenTitulo = new ArrayList<OrdenTitulo>();
									arregloOrdenTitulo.add(ordenTitulo);
									orden.setOrdenTitulo(arregloOrdenTitulo);

									if (totalAdj.compareTo(montoPedido) == 0) {// validacion monto ADJUDICADO IGUAL MONTO PEDIDO

										logger.info("Monto Adjudicado IGUAL al Solicitado para la orden: " + orden.getIdOrden());
										// System.out.println("Monto Adjudicado IGUAL al Solicitado para la orden: " + orden.getIdOrden());

										try {
											// Modificacion de la orden Sitme Clavenet en INFI

											querysTransacciones.add(ordenDAOClavenet.modificarOrdenesClaveNet(orden, StatusOrden.ENVIADA));
										} catch (Exception e) {
											// System.out.println("Ha ocurrido un error en la modificacion de la orden " + orden.getIdOrden()+" para la actualizacion de la tabla INFI_TB_204_ORDENES " + e.getMessage());
											logger.error("Ha ocurrido un error en la modificacion de la orden " + orden.getIdOrden() + " para la actualizacion de la tabla INFI_TB_204_ORDENES " + e.getMessage());
											continue;
										}

										// Generacion de las Ordenes Titulos
										querysTransacciones.add(ordenesTitulos.insertarTitulosOrden(orden.getIdOrden(), ordenTitulo));

										// Genereacion de Operaciones de INFI
										generarOperaciones(ADJUDICADO_IGUAL_SOLICITADO, orden, ordenDAOClavenet, vehiculoID, BigDecimal.valueOf(tasaCambioUI), ui.toString(), Double.parseDouble(precioNegociado.toString()), BigDecimal.valueOf(Double.parseDouble(montoAsignado.toString())), stringBroker, consecutivoNumeric, numeroRetencion);

										// Modificacion de ordenes de tabla OPICS
									} else if (totalAdj.compareTo(montoPedido) < 0) {

										logger.info("Orden: " + orden.getIdOrden() + " - Valor Efectivo MENOR al monto solicitado");
										// System.out.println("Orden: "+ orden.getIdOrden()+ " - Valor Efectivo MENOR al monto solicitado");
										orden.setPrecioCompra(precioCompra.doubleValue());// ORDENE_PED_PRECIO --> PRECIO DE COMPREA

										try {
											// Modificacion de la orden Sitme Clavenet en INFI

											querysTransacciones.add(ordenDAOClavenet.modificarOrdenesClaveNet(orden, StatusOrden.ENVIADA));

										} catch (Exception e) {
											// System.out.println("Ha ocurrido un error en la modificacion de la orden " + orden.getIdOrden()+" para la actualizacion de la tabla INFI_TB_204_ORDENES " + e.getMessage());
											logger.error("Ha ocurrido un error en la modificacion de la orden " + orden.getIdOrden() + " para la actualizacion de la tabla INFI_TB_204_ORDENES " + e.getMessage());
											continue;
										}

										// Generacion de las Ordenes Titulos
										querysTransacciones.add(ordenesTitulos.insertarTitulosOrden(orden.getIdOrden(), ordenTitulo));

										// Genereacion de Operaciones de INFI
										generarOperaciones(ADJUDICADO_MENOR_SOLICITADO, orden, ordenDAOClavenet, vehiculoID, BigDecimal.valueOf(tasaCambioUI), ui.toString(), Double.parseDouble(precioNegociado.toString()), BigDecimal.valueOf(Double.parseDouble(montoAsignado.toString())), stringBroker, consecutivoNumeric, numeroRetencion);

									}// FIN MONTO ADJUDICADO MENOR A SOLICITADO

								}// FIN MONTO ADJUDICADO MAYOR A CERO

							}// FIN ORDEN EXISTE

						} else {// Fin IF unidad de inversion existe

							logger.info("La unidad de inversion '" + ui.toString() + "' no existe en la base de datos, por favor verifique el nombre de la Unidad de Inversion");
						}

					} catch (Exception e) {

						logger.error("Ocurrio un error en el proceso de Adjudicacion. " + e.getMessage());
						throw new Exception("Ocurrio un error en el proceso de Adjudicacion. " + e.getMessage());
					}
					// }//FIN DE FINAL DE LECTURA POR PRIMERA FILA VACIA

					try {

						Statement s = transaccion.getConnection().createStatement();
						for (String element : querysTransacciones) {
							// System.out.println("Sentencia: " + element);
							s.addBatch(element);
						}
						++cantidadOrdenesAdj;
						s.executeBatch();
						s.close();

						if (ConstantesGenerales.COMMIT_REGISTROS_ADJ == cantidadOrdenesAdj) {
							try {

								acumuladorOrdenesAdj = acumuladorOrdenesAdj + cantidadOrdenesAdj;
								logger.info("Ordenes enviadas por COMMIT en proceso de ADJUDICACION: " + acumuladorOrdenesAdj);
								transaccion.getConnection().commit();

							} catch (Exception e) {
								logger.error("Eror en el proceso de COMMIT de los registros en Adjudicacion " + e.getMessage());
								throw new Exception("He ocurrido en el proceso de realizacion de COMMIT de los registros de Adjudicacion: " + e.getMessage());
							}
							logger.info("Realizacion de commit al numero de registro N° " + cantidadOrdenesAdj);
							cantidadOrdenesAdj = 0;
						}
					} catch (SQLException sql) {
						// System.out.println("Ha ocurrido un error de tipo SQLException " + sql.getMessage());
						querysTransacciones.clear();
						transaccion.getConnection().rollback();
						logger.error("Ha ocurrido un error de tipo SQLException " + sql.getMessage());
						continue;
					} catch (Exception e) {
						querysTransacciones.clear();
						transaccion.getConnection().rollback();
						// System.out.println("Ha ocurrido un error Inesperado " + e.getMessage());
						logger.error("Ha ocurrido un error Inesperado " + e.getMessage());
						continue;
					}

					querysTransacciones.clear();
					transaccion.getConnection().commit();

				}// FIN RECORRIDO DE FILAS DEL XLS

			}// FIN DEL RECORRIDO DE LAS HOJAS DEL XLS

		} finally {

			if (transaccion.getConnection() != null) {
				transaccion.getConnection().commit();
			}
			terminarProceso();

			if (clienteDAOClavenet != null) {
				clienteDAOClavenet.cerrarConexion();
			}

			if (unidadInvDAO != null) {
				unidadInvDAO.cerrarConexion();
			}

			if (controlArchivoDAO != null) {
				controlArchivoDAO.cerrarConexion();
			}
			if (ordenesTitulos != null) {
				ordenesTitulos.cerrarConexion();
			}

			if (ordenDAOClavenet != null) {
				ordenDAOClavenet.cerrarConexion();
			}
			if (transaccion != null) {
				transaccion.closeConnection();
			}

		}
	}// FIN METODO adjudicarSitmeClaveNet

	private void calcularIntCaidos(Orden orden, BigDecimal tasaCambio, String idTitulo, String fechaValor, String nombreDataSource, DataSource _dso, String unidadInversionNombre) throws Exception {

		BigDecimal totalMontoCapital = new BigDecimal(0);

		// calculo anterior en bolivares
		// totalMontoCapital=tasaCambio.multiply(BigDecimal.valueOf(orden.getMontoAdjudicado()));

		// Incidencia (1) calidad
		// Calculo actual basado en el monto Adj en dolares
		totalMontoCapital = BigDecimal.valueOf(orden.getMontoAdjudicado());
		// System.out.println("MONTO CAPITAL " + totalMontoCapital);

		try {
			interesesCaidos = com.bdv.infi_toma_orden.util.Utilitario.calcularInteresesCaidos(totalMontoCapital, idTitulo, fechaValor, nombreDataSource, _dso, null);
			interesesCaidos = interesesCaidos.setScale(2, BigDecimal.ROUND_HALF_EVEN);

			interesesCaidosBs = interesesCaidos.multiply(tasaCambio).setScale(2, BigDecimal.ROUND_HALF_EVEN);

			// System.out.println("ORDEN " + orden.getIdOrden()+ " Intereses Caidos: " + interesesCaidos );
			// System.out.println("ORDEN " + orden.getIdOrden()+ " Intereses Caidos Bs: " + interesesCaidosBs );
		} catch (Exception e) {
			char comilla = 34;
			logger.error("Verifique que el nombre de la unidad de inversion " + comilla + unidadInversionNombre + comilla + " se igual al titulo que tiene asociado " + e.getMessage());
			throw new Exception("Verifique que el nombre de la unidad de inversion " + comilla + unidadInversionNombre + comilla + " se igual al titulo que tiene asociado " + e.getMessage());
		}

	}

	private void generarOperaciones(int condicion, Orden orden, OrdenDAO ordenDAO, String vehiculoId, BigDecimal tasaCambio, String tituloNombre, double precioNegociado, BigDecimal montoAsignado, String broker, long consecutivo, String numeroRetencion) throws Exception {

		ArrayList<OrdenOperacion> operaciones = new ArrayList<OrdenOperacion>();
		ordenOpics = new SolicitudClavenet();

		String totalMontoComisionString = null;
		OperacionDAO operacionDAO = new OperacionDAO(_dso);

		OrdenOperacion operacionDesbloqueo = new OrdenOperacion();

		BigDecimal monto_nominal = new BigDecimal(0);

		BigDecimal monto_comision = new BigDecimal(0);
		BigDecimal montoTotalDesb;
		// OrdenDAO ordenCLaveNet=new OrdenDAO(_dso);

		try {

			long idOperacion = Long.valueOf(operacionDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_ORDENES_OPERACION)).longValue();

			monto_nominal = BigDecimal.valueOf(Double.parseDouble(ordenDAO.getDataSet().getValue("ORDENE_PED_TOTAL")));

			monto_comision = BigDecimal.valueOf(Double.parseDouble(ordenDAO.getDataSet().getValue("ORDENE_PED_COMISIONES")));

			// Operaciones 1) Operacion de Desbloqueo de Capital y Comision juntos --> Monto registrado en la tabla 204 campo ORDENE_PED_TOTAL+ORDENE_PED_COMISIONES
			BuscarCodigoyNombreOperacion(vehiculoId, ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN, false, TransaccionFinanciera.BLOQUEO);

			// Codigo desplazado a la seccion de adjudicacion
			/*
			 * SolicitudClavenet solicitud=new SolicitudClavenet(); solicitud.setIdOrdenInfi(orden.getIdOrden()); ordenCLaveNet.mostrarSolicitudesClaveNet(solicitud);
			 * 
			 * if(ordenCLaveNet.getDataSet().count()>0){ ordenCLaveNet.getDataSet().next();
			 * 
			 * String numeroRetencion=null; numeroRetencion=ordenCLaveNet.getDataSet().getValue("NUM_BLOQUEO");
			 * 
			 * if(numeroRetencion!=null && (!numeroRetencion.equals(""))){ numeroRetencion=numeroRetencion.trim(); if(numeroRetencion.length()>5){ numeroRetencion=numeroRetencion.substring(5);
			 * 
			 * } } operacionDesbloqueo.setNumeroRetencion(numeroRetencion); }
			 */

			operacionDesbloqueo.setNumeroRetencion(numeroRetencion);

			operacionDesbloqueo.setCodigoOperacion(codigo_operacion);
			operacionDesbloqueo.setNombreOperacion(nombre_operacion);
			operacionDesbloqueo.setIdOperacion(idOperacion);
			operacionDesbloqueo.setIdOperacionRelacion(idOperacion);
			operacionDesbloqueo.setIdOrden(orden.getIdOrden());
			operacionDesbloqueo.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			operacionDesbloqueo.setIdTransaccionFinanciera(new String(ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN + ""));

			montoTotalDesb = monto_nominal.add(monto_comision);
			operacionDesbloqueo.setMontoOperacion(montoTotalDesb.setScale(2, BigDecimal.ROUND_HALF_EVEN));
			operacionDesbloqueo.setAplicaReverso(false);
			operacionDesbloqueo.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
			operacionDesbloqueo.setCuentaOrigen(orden.getCuentaCliente());
			operacionDesbloqueo.setNumeroCuenta(orden.getCuentaCliente());
			operacionDesbloqueo.setIdMoneda(orden.getIdMoneda());
			operacionDesbloqueo.setInComision(ConstantesGenerales.VERDADERO);
			operacionDesbloqueo.setTipoTransaccionFinanc(TransaccionFinanciera.DESBLOQUEO);
			operacionDesbloqueo.setTasa(new BigDecimal(100));
			operacionDesbloqueo.setFechaAplicar(new Date());

			// operacionDAO.insertarOperacion(operacionDesbloqueo);

			operaciones.add(operacionDesbloqueo);
			querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDesbloqueo, true));

			if (condicion == ADJUDICADO_CERO) {
				ordenOpics.setEstatus(StatusOrden.NO_ADJUDICADA);
				ordenOpics.setMontoComision(new BigDecimal(0));
			} else {

				ordenOpics.setEstatus(StatusOrden.ADJUDICADA);
				ordenOpics.setTituloAdjudicado(tituloNombre);
				ordenOpics.setPrecioAdjudicacion(precioNegociado);

			}

			ordenOpics.setFechaAdjudicacion(Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
			ordenOpics.setValorNominalUSD(BigDecimal.valueOf(Double.parseDouble(montoAsignado.toString())));

			BigDecimal precioVenta = BigDecimal.valueOf(precioNegociado);
			BigDecimal valorNominalUSD = montoAsignado.multiply(precioVenta.divide(new BigDecimal(100))).add(interesesCaidos);

			ordenOpics.setValorEfectivoUSD(valorNominalUSD.setScale(2, BigDecimal.ROUND_HALF_EVEN));
			ordenOpics.setMontoAdjudicado(BigDecimal.valueOf(Double.parseDouble(montoAsignado.toString())));
			ordenOpics.setIdOrdenInfi(orden.getIdOrden());

			if (condicion == ADJUDICADO_IGUAL_SOLICITADO || condicion == ADJUDICADO_MENOR_SOLICITADO) {
				// Operaciones 2) debito por el monto capital --> Monto registrado en la tabla 204 campo ORDENE_PED_TOTAL
				// BigDecimal totalMontoCapital=new BigDecimal(0);

				OrdenOperacion operacionDebitoCapital = new OrdenOperacion();

				BuscarCodigoyNombreOperacion(vehiculoId, ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN, false, TransaccionFinanciera.DEBITO);
				operacionDebitoCapital.setCodigoOperacion(codigo_operacion);
				operacionDebitoCapital.setNombreOperacion(nombre_operacion);

				operacionDebitoCapital.setIdOrden(orden.getIdOrden());
				operacionDebitoCapital.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
				operacionDebitoCapital.setIdTransaccionFinanciera(new String(ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN + ""));

				// totalMontoCapital=tasaCambio.multiply(BigDecimal.valueOf(orden.getMontoAdjudicado())).multiply(BigDecimal.valueOf(orden.getPrecioCompra()).divide(new BigDecimal(100))).add(interesesCaidos.multiply(tasaCambio));
				// totalMontoCapital=BigDecimal.valueOf(orden.getMonto()).add(interesesCaidosBs);
				totalMontoCapitalOperaciones = totalMontoCapitalOperaciones.add(interesesCaidosBs);
				// totalMontoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN);

				// operacionDebitoCapital.setMontoOperacion(totalMontoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN));//FALTA //configurar monto
				operacionDebitoCapital.setMontoOperacion(totalMontoCapitalOperaciones.setScale(2, BigDecimal.ROUND_HALF_EVEN));// FALTA //configurar monto

				operacionDebitoCapital.setAplicaReverso(false);
				operacionDebitoCapital.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
				operacionDebitoCapital.setCuentaOrigen(orden.getCuentaCliente());
				operacionDebitoCapital.setIdMoneda(orden.getIdMoneda());
				operacionDebitoCapital.setInComision(ConstantesGenerales.FALSO);
				operacionDebitoCapital.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
				operacionDebitoCapital.setTasa(BigDecimal.valueOf(orden.getPrecioCompra()));
				operacionDebitoCapital.setFechaAplicar(new Date());
				operacionDebitoCapital.setIdOperacionRelacion(operacionDesbloqueo.getIdOperacion());

				operaciones.add(operacionDebitoCapital);
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoCapital, false));

				// Operaciones 3) debito por el monto comision --> Monto registrado en la tabla 204 campo ORDENE_PED_COMISIONES
				BigDecimal totalMontoComision = new BigDecimal(0);
				OrdenOperacion operacionDebitoComision = new OrdenOperacion();
				BuscarCodigoyNombreOperacion(vehiculoId, ConstantesGenerales.TRANSACCION_FIJA_COMISION, false, TransaccionFinanciera.DEBITO);
				operacionDebitoComision.setCodigoOperacion(codigo_operacion);
				operacionDebitoComision.setNombreOperacion(nombre_operacion);

				operacionDebitoComision.setIdOrden(orden.getIdOrden());
				operacionDebitoComision.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
				operacionDebitoComision.setIdTransaccionFinanciera(new String(ConstantesGenerales.TRANSACCION_FIJA_COMISION + ""));

				// totalMontoComision=totalMontoCapital.multiply(orden.getComisionOperacion().divide(new BigDecimal(100)));
				// totalMontoComision.setScale(2,BigDecimal.ROUND_HALF_EVEN);

				// totalMontoComisionString=String.valueOf(totalMontoComision.setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalMontoComisionString = String.valueOf(totalMontoComisionOperaciones.setScale(2, BigDecimal.ROUND_HALF_EVEN));

				operacionDebitoComision.setMontoOperacion(totalMontoComisionOperaciones.setScale(2, BigDecimal.ROUND_HALF_EVEN)); // configurar monto
				// operacionDebitoComision.setMontoOperacion(totalMontoComision.setScale(2,BigDecimal.ROUND_HALF_EVEN)); //configurar monto

				operacionDebitoComision.setAplicaReverso(false);
				operacionDebitoComision.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
				operacionDebitoComision.setCuentaOrigen(orden.getCuentaCliente());
				operacionDebitoComision.setIdMoneda(orden.getIdMoneda());
				operacionDebitoComision.setInComision(ConstantesGenerales.FALSO);
				operacionDebitoComision.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
				operacionDebitoComision.setTasa(BigDecimal.valueOf(orden.getPrecioCompra()));
				operacionDebitoComision.setFechaAplicar(new Date());

				operacionDebitoComision.setIdOperacionRelacion(operacionDesbloqueo.getIdOperacion());
				// nuevo
				operaciones.add(operacionDebitoComision);

				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComision, false));

				// ordenOpics.setValorEfectivo(totalMontoCapital.add(totalMontoComision));
				// ordenOpics.setValorNominal(totalMontoCapital);
				totalMontoComision = totalMontoComisionOperaciones;

				ordenOpics.setValorEfectivo(totalMontoCapitalOperaciones.add(totalMontoComisionOperaciones));
				ordenOpics.setValorNominal(totalMontoCapitalOperaciones);

				if (condicion == ADJUDICADO_MENOR_SOLICITADO) {
					ordenOpics.setMontoComision(totalMontoComision);
				}
				String stringConsecutivo = String.valueOf(consecutivo);
				actualizacionDataExtendida(orden.getIdOrden(), ordenDAO.getDataSet().getValue("ORDENE_COMISION_OPERACION"), totalMontoComisionString, ordenDAO, broker, stringConsecutivo);

				// Generacion de Deal de Opics

				try {
					orden.setOperacion(operaciones);
					orden.setStatus(StatusOrden.ENVIADA);

					generarMensajeEstadistica(querysTransacciones, orden, clienteDAOClavenet.getDataSet(), new BigDecimal(orden.getMontoAdjudicado()), broker.toString(), consecutivo);
					orden.setStatus(StatusOrden.PROCESO_ADJUDICACION);
				} catch (Exception e) {
					logger.error("Error en el proceso de generacion de Mensaje de Estadistica");
					// System.out.println("Error en el proceso de generacion de Mensaje de Estadistica");
					throw new Exception("Error en el proceso de generacion de Mensaje de Estadistica");
				}

				try {

					UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
					int idUserSession = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));

					IngresoOpicsSitme ingresoOpics = new IngresoOpicsSitme(_dso, _app, idUserSession, _req.getRemoteAddr(), getUserName());

					StringBuffer sql = new StringBuffer();
					ArrayList<String> querys = new ArrayList<String>();
					querys = ingresoOpics.rentaFija(orden);

					for (int z = 0; z < querys.size(); z++) {
						// System.out.println("query consulta " + querys.get(z).toString());
						querysTransacciones.add(querys.get(z).toString());
					}

				} catch (Exception e) {
					// System.out.println("Se ha generado un error en el proceso de generacion de DEALS OPICS " + e.getMessage());
					logger.error("Se ha generado un error en el proceso de generacion de DEALS OPICS " + e.getMessage());
					throw new Exception("Se ha generado un error en el proceso de generacion de DEALS OPICS " + e.getMessage());
				}
			}

			actualizarOrdenesClavenet(ordenOpics);

		} catch (Exception e) {
			// System.out.println("Ocurrido un error en el proceso de Generar Operaciones");
			throw new Exception(" Ocurrido un error en el proceso de Generar Operaciones. " + e.getMessage());
		} finally {
			if (operacionDAO != null) {
				operacionDAO.cerrarConexion();
			}

			if (ordenCLaveNet != null) {
				ordenCLaveNet.cerrarConexion();
			}
		}
	}

	private void actualizarOrdenesClavenet(SolicitudClavenet ordenOpics) throws Exception {

		try {
			logger.info("Actualizacion de la orden Opics en la tabla SOLICITUDES_SITME ");

			querysTransacciones.add(ordenDAOClavenet.actualizarSolicitudClavenet(ordenOpics, StatusOrden.EN_TRAMITE));

		} catch (Exception e) {
			// System.out.println("Se ha generado un error en el proceso de  actualizarOrdenesOPICS" + e.getMessage());
			logger.error("Se ha generado un error en el proceso de actualizarOrdenesOPICS " + e.getMessage());
			throw new Exception("Se ha generado un error en el proceso de actualizarOrdenesOPICS " + e.getMessage());
		}
	}

	private void actualizacionDataExtendida(long ordenID, String pctComision, String montoComision, OrdenDAO ordenDAO, String broker, String consecutivo) throws Exception {

		try {

			String stringValor = montoComision.concat(";" + pctComision);

			querysTransacciones.add(ordenDAO.modificarValorDataExtendida(ordenID, DataExtendida.COMISION_CLAVENET, stringValor));

			querysTransacciones.add(ordenDAO.insertarValoresDataExtendida(ordenID, DataExtendida.BROKER, broker));
			querysTransacciones.add(ordenDAO.insertarValoresDataExtendida(ordenID, DataExtendida.CONSECUTIVO, consecutivo));
		} catch (Exception e) {
			// System.out.println("Se ha generado un error en el proceso de actualizacionDataExtendida " + e.getMessage());
			logger.error("Se ha generado un error en el proceso de actualizacionDataExtendida " + e.getMessage());
			throw new Exception("Se ha generado un error en el proceso de actualizacionDataExtendida " + e.getMessage());
		}
	}

	/**
	 * Obtiene las transacciones fijas según la unidad y el vehículo tomador
	 * 
	 * @param unidadInversion
	 *            id de la unidad de inversión
	 * @param vehiculoTomador
	 *            vehículo tomador
	 * @throws Exception
	 *             en caso de error
	 */
	protected void getTransaccionesFijasClaveNet(String unidadInversion, String vehiculo) throws Exception {

		if (transaccionesFijasCache.containsKey(unidadInversion)) {
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);

		} else {

			fijaDAO.listarTransaccionesFijasAdjudicacionClaveNet(unidadInversion, vehiculo);
			DataSet ds = fijaDAO.getDataSet();

			transaccionesFijasCache.put(unidadInversion, ds);
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);

		}

	}

}// Fin clase

