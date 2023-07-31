package com.bdv.infi_toma_orden.logic;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
import org.jibx.runtime.JiBXException;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.CalendarioDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ContratadosDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ReglaValidacionDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.VehiculoRolDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.PrecioRecompra;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.data.TransaccionFija;
import com.bdv.infi.data.UIBlotter;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;
import com.bdv.infi.webservices.manager.ManejadorUltraTemprano;
import com.bdv.infi_toma_orden.dao.ClienteDAO;
import com.bdv.infi_toma_orden.dao.UIBlotterRangosDAO;
import com.bdv.infi_toma_orden.dao.UnidadInversionDAO;
import com.bdv.infi_toma_orden.data.CampoDinamico;
import com.bdv.infi_toma_orden.data.Cliente;
import com.bdv.infi_toma_orden.data.OrdenTitulo;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.bdv.infi_toma_orden.data.UIBlotterRangos;
import com.bdv.infi_toma_orden.util.Utilitario;

/**
 * Clase que encapsula la funcionalidad a ejeuctar para la Toma de Ordenes de INFI
 * 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class TomaDeOrdenesDivisasPersonas {

	private TransaccionFijaDAO trnfFijaDAO = null;
	private MonedaDAO monedaDAO = null;
	private UnidadInversionDAO boUISimular = null;
	private Transaccion bolTransaccion = null;
	private Object[] objResultadoSimular = null;
	private long unidadInversionActiva = 0;
	private HashMap<String, UIBlotterRangos> mapUiBlotterRangos = new HashMap<String, UIBlotterRangos>();
	private HashMap<String, TransaccionFija> mapTransaccionFija = new HashMap<String, TransaccionFija>();
	private HashMap<String, String> mapTipoProducto = new HashMap<String, String>();

	DecimalFormat dFormato = new DecimalFormat("###,###,##0.00");

	/**
	 * Constantes de apoyo
	 */
	private static final Long CERO_LOG = new Long(0);
	private static final BigDecimal CERO_BD = new BigDecimal(0);
	private static final BigDecimal CIEN_BD = new BigDecimal(100);
	private static final String STATUS_ORDEN_REGISTRADA = StatusOrden.REGISTRADA;
	private Logger logger = Logger.getLogger(TomaDeOrdenesDivisas.class);
	/**
	 * DataSource a utilizar si el cliente no es un WebService
	 */
	private DataSource dso;
	/**
	 * Nombre del DataSource
	 */
	private String nombreDataSource;
	/**
	 * Parametros de la Toma de Orden
	 */
	private HashMap parametrosEntrada = new HashMap();
	/**
	 * Bean que encapsula informacion de la Toma de Orden
	 */
	private TomaOrdenSimulada beanTOSimulada;
	/**
	 * Bean que encapsula informacion del Cliente
	 */
	private Cliente beanCliente;
	/**
	 * Atributos de la Unidad de Inversion recuperados de la base de datos
	 */
	private String idEmpresa = "";
	private String idMoneda = "";// id Moneda Local
	private String idMonedaUI = "";// id Moneda de la Unidad de Inversión
	private BigDecimal tasaCambio = new BigDecimal(0);
	private boolean indicaInventario = false;
	private int indicaPrecioSucioUI = 0;
	private long idCliente = -1;
	private String tipoProductoId = "";

	private int manejoProductoInstFin = 0;
	private int inPedidoMontoUI = 0;
	/**
	 * Lista de Titulos recuperados
	 */
	private ArrayList listaTitulos;
	/**
	 * Lista de Operaciones generadas
	 */
	private ArrayList<OrdenOperacion> listaOperacion;
	/**
	 * Lista de Mensaje a presentar al cliente
	 */
	private ArrayList<String> listaMensajes = new ArrayList<String>();
	/**
	 * Campos auxiliares
	 */
	private long cantidadPedida = 0;
	private BigDecimal montoPedido = new BigDecimal(0);
	private BigDecimal tasaPropuesta = new BigDecimal(0);
	private BigDecimal montoInversion = new BigDecimal(0);
	private BigDecimal montoTotal = new BigDecimal(0);
	private BigDecimal montoTotalFinanciado = new BigDecimal(0);
	private BigDecimal montoComisiones = new BigDecimal(0);
	private BigDecimal montoInteresCaidos = new BigDecimal(0);
	private int recompraConNeteoUI;

	Date dateValor = new Date();
	SimpleDateFormat formatear = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	SimpleDateFormat formatear2 = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_HORA_BD);

	public void setParametrosEntrada(HashMap parametrosEntrada) {
		this.parametrosEntrada = parametrosEntrada;
	}

	public void setCliente(Cliente beanCliente) {
		this.beanCliente = beanCliente;
	}

	private ArrayList<Cuenta> listaCuentasCliente = new ArrayList<Cuenta>();

	// Lista de Cuentas del Cliente
	private void setListaCuentasCliente(ArrayList<Cuenta> listaCuentas) {
		this.listaCuentasCliente = listaCuentas;
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param dso
	 *            :DatSource a utilizar para acceder a la base de datos
	 */
	public TomaDeOrdenesDivisasPersonas(DataSource dso) {
		this.dso = dso;
		trnfFijaDAO = new TransaccionFijaDAO(dso);
		monedaDAO = new MonedaDAO(dso);
		boUISimular = new UnidadInversionDAO(nombreDataSource, dso);
		bolTransaccion = new Transaccion(nombreDataSource, dso);
	}

	/**
	 * Metodo de validacion de una TomaOrdenSimulada en base a los parametros dados
	 * 
	 * @param parametrosEntrada
	 * @return TomaOrdenSimulada con la informacion producida en el proceso
	 * @throws Throwable
	 */
	public ArrayList validar() throws Exception {

		BigDecimal montoAux = new BigDecimal(0);
		long cantAux = 0;
		String editado = "";

		if (parametrosEntrada == null || parametrosEntrada.isEmpty()) {

			listaMensajes.add("Los datos para la toma de orden est&aacute;n incompletos. Verifique que los rangos de montos o cantidades m&iacute;nimas y m&aacute;ximas a comprar sean mayores a cero(0). Es posible que no se hayan configurado para el blotter y tipo de persona seleccionados.");

			return listaMensajes;

		} else {
			// 1.- Validar la informacion requerida
			if ((Long) parametrosEntrada.get("idUnidadInversion") == 0) {
				listaMensajes.add("La Unidad de Inversion es requerida");
			}
			if (parametrosEntrada.get("idBlotter") == null || parametrosEntrada.get("idBlotter").equals("")) {
				listaMensajes.add("El Blotter es requerido");
			}
			if (parametrosEntrada.get("tipoPersona") == null || parametrosEntrada.get("tipoPersona").equals("")) {
				listaMensajes.add("El Tipo de Persona del Cliente es requerido");
			}

			if (parametrosEntrada.get("cedulaCliente") == null || parametrosEntrada.get("cedulaCliente").equals("")) {
				listaMensajes.add("La Cedula del Cliente es requerida");
			}

			// validaciones de vehiculo
				if(parametrosEntrada.get("idVehiculoRecompra")==null || parametrosEntrada.get("idVehiculoRecompra").equals("")
						|| parametrosEntrada.get("idVehiculoColocador")==null || parametrosEntrada.get("idVehiculoColocador").equals("")
						|| parametrosEntrada.get("idVehiculoTomador")==null || parametrosEntrada.get("idVehiculoTomador").equals("")){

				VehiculoRolDAO vehiculoRolDAO = new VehiculoRolDAO(dso);
				vehiculoRolDAO.listarVehiculoRolPorDefecto();
				DataSet vehiculo_defecto = vehiculoRolDAO.getDataSet();

				if (!vehiculo_defecto.next()) {
					listaMensajes.add("No existe veh&iacute;culo por defecto. Este par&aacute;metro es requerido");
				}
			}
				
			montoPedido = (BigDecimal) parametrosEntrada.get("montoInversion");
			cantidadPedida = (Long) parametrosEntrada.get("cantidadComprar");
			if (montoPedido.doubleValue() == 0) {
				listaMensajes.add("Debe colocar el monto a comprar a procesar");
			}else{
				if (montoPedido.doubleValue() < 0) {
					listaMensajes.add("El monto a comprar no puede ser negativo.");
				}else{
					/*if (montoPedido.doubleValue() > ((BigDecimal)parametrosEntrada.get("montoMaximoComprar")).doubleValue() ) {
						listaMensajes.add("El monto a comprar no debe exceder el monto m&aacute;ximo a comprar.");
					}*/
				}
			}
			tasaPropuesta = (BigDecimal) parametrosEntrada.get("tasaPropuesta");
			if (tasaPropuesta.doubleValue() == 0) {
				listaMensajes.add("Debe colocar la tasa propuesta a procesar");
			}else{
				if (tasaPropuesta.doubleValue() < 0) {
					listaMensajes.add("La tasa propuesta no puede ser negativa.");
				}
			}
			/*if (cantidadPedida == 0) {
				listaMensajes.add("Debe colocar la cantidad a procesar");
			}*/
			montoAux = (BigDecimal) parametrosEntrada.get("precioOfrecido");
			if (montoAux.doubleValue() == 0) {
				listaMensajes.add("El Precio es requerido");
			}

			// Si NO es cartera propia
			if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

				// validar numero de cuenta requerido
				if (parametrosEntrada.get("numeroCuentaCliente") == null || parametrosEntrada.get("numeroCuentaCliente").equals("")) {
					listaMensajes.add("La Cuenta del Cliente es requerida");
				}

				// validar Financiamiento
				montoAux = (BigDecimal) parametrosEntrada.get("porcentajeFinanciado");
				if (montoAux.doubleValue() > 100) {
					listaMensajes.add("El Porcentaje de Financimiento debe ser menor a 100%");
				}

				// -----Validar Datos del Conyuge del Cliente si son obligatorios---------------------------------
				// ---Si el cliente es venezolano o extranjero
					if (parametrosEntrada.get("tipoPersona") != null && !parametrosEntrada.get("tipoPersona").toString().equals("")
							&& (parametrosEntrada.get("tipoPersona").toString().equalsIgnoreCase("V") || parametrosEntrada.get("tipoPersona").toString().equalsIgnoreCase("E")))

					// verificar que indico el estado civil
					if (parametrosEntrada.get("estadoCasado") == null) {
						listaMensajes.add("Debe indicar el el estado civil del cliente");
					} else {
						// ---verificar que se ingresaron los datos del conyuge si el cliente es casado
						if (parametrosEntrada.get("estadoCasado").toString().equalsIgnoreCase("SI")) {
							if (parametrosEntrada.get("cedulaConyuge") == null || parametrosEntrada.get("tipoPersonaConyuge") == null) {
								listaMensajes.add("Debe indicar el tipo de persona (V &oacute; E) y la c&eacute;dula de identidad del conyuge del cliente");
							} else {
								// Validar si el conyuge existe en altair, solo al momento de tomar la orden
								if (parametrosEntrada.get("calculador") == null) {// si no viene del calculador

									try {
										buscarPersonaAltair((String) parametrosEntrada.get("cedulaConyuge"), (String) parametrosEntrada.get("tipoPersonaConyuge"));

									} catch (Exception eC) {
										// No existe el cliente en altair
										if (eC.getMessage().contains("@ERPEE0221")) {
											listaMensajes.add("El conyuge del cliente no se encuentra registrado en Altair.");
											logger.error(eC.getMessage() + com.bdv.infi.util.Utilitario.stackTraceException(eC));
											}else{
											// Error realizando la consulta en altair
											listaMensajes.add("Error consultando el conyuge del cliente en arquitectura extendida.");
											logger.error(eC.getMessage() + com.bdv.infi.util.Utilitario.stackTraceException(eC));
										}

									}

								}

							}
						}

					}
				// ------------------------------------------------------------------------------------------------------------------

			}

			// ----Validar campos dinámicos como requeridos---------------------------------------------------
			ArrayList ArrayCamposDinamicos = (ArrayList) parametrosEntrada.get("camposDinamicos");
			if (ArrayCamposDinamicos != null && !ArrayCamposDinamicos.isEmpty()) {

				for (int i = 0; i < ArrayCamposDinamicos.size(); i++) {
					CampoDinamico campoDinamico = (CampoDinamico) ArrayCamposDinamicos.get(i);
					if (campoDinamico.getValor() == null || campoDinamico.getValor().equals("")) {
						listaMensajes.add("El campo '" + campoDinamico.getNombreCampo() + "' es requerido");
					}
				}
			}
			// ----------------------------------------------------------------------------------------------

			// ---- Salida si se encontro errores de campos requeridos ----
			if (listaMensajes.size() > 0) {
				return listaMensajes;
			}
			// ----------------------------------------------------------------------------

			// ------------------Validación de día feriado-----------------------------------
			CalendarioDAO calendarioDAO = new CalendarioDAO(dso);
			MSCModelExtend me = new MSCModelExtend();
			String hoy_calendario = me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA);
			boolean diaFeriado = calendarioDAO.esDiaFeriado(hoy_calendario);

			if (diaFeriado) {
				listaMensajes.add("El d&iacute;a de hoy no se pueden tomar ordenes por ser d&iacute;a feriado");
				// ---salida por validación de dia feriado
				return listaMensajes;
			}
			// ------------------------------------------------------------------------------

			// 2.- Validar Cliente existe
			ClienteDAO boCliente = new ClienteDAO(nombreDataSource, dso);
			try {
					boCliente.listarPorCedula(
							(String)parametrosEntrada.get("tipoPersona"), (String)parametrosEntrada.get("cedulaCliente"));
				beanCliente = boCliente.getCliente();

				// ----------obtener id del cliente----------------------------------------
				idCliente = beanCliente.getIdCliente();

			} catch (Exception e) {
				if (e.getMessage().indexOf("MSGTO8001") > -1) {// cliente no encontrado en INFI
					buscarClienteAltair();// Buscar cliente en altair

				} else {
					logger.error(e.getMessage() + com.bdv.infi.util.Utilitario.stackTraceException(e));
					throw new Exception(e);
				}
			}

			// 3.- Buscar informacion de la Unidad de Inversion.
			// Requiere de informacion de limites de compra
			UnidadInversionDAO boUI = new UnidadInversionDAO(nombreDataSource, dso);
			ContratadosDAO contratadosDAO = new ContratadosDAO(dso);
			Object[] objResultado = null;
			try {
				objResultado = boUI.listarID((Long) parametrosEntrada.get("idUnidadInversion"), true);
				// obtener moneda de la UI
				idMonedaUI = (String) objResultado[10];
				// --obtener tasa de cambio para calculos
				//tasaCambio = (BigDecimal) objResultado[2];
				
				tasaCambio = (BigDecimal) parametrosEntrada.get("tasaPropuesta");
				//System.out.println("TASAAAAA PROPUESTA: "+tasaCambio);
				recompraConNeteoUI = (Integer) objResultado[17];// Indicador de recompra con neteo

				// -------------------indicador de Pedido de Monto------------------------------------------------------------
				inPedidoMontoUI = (Integer) objResultado[5];
				// --------------------------------------------------------------------------------------------------------
				// ------------------Tipo de Producto del Instrumento Financiero de la UI--------------------------------------------
				tipoProductoId = (String) objResultado[19];
				manejoProductoInstFin = (Integer) objResultadoSimular[20];
				// --------------------------------------------------------------------------------------------------------------
				// ---------------Validar unidad de Inversión Publicada---------------------------------------------------
				String statusUnidadInv = (String) objResultado[16];
				// ----Si la unidad no esta publicada
				if (!statusUnidadInv.equals(UnidadInversionConstantes.UISTATUS_PUBLICADA)) {
					listaMensajes.add("La Unidad de Inversi&oacute;n seleccionada no se encuentra publicada");
					// --salida por unidad de iunversión no publicada
					return listaMensajes;
				}
				// ---------------------------------------------------------------------------------------------------------

				// -------Validar multiplos de inversión----------------------------------------------------------------------------------
				BigDecimal multiploUInversion = (BigDecimal) objResultado[11];
				// Validar solo cuando la unidad de inversion es por monto
				if (inPedidoMontoUI == 1) {

					double resto = montoPedido.doubleValue() % multiploUInversion.doubleValue();
					if (resto > 0) {
						listaMensajes.add("El Monto Total a Invertir (" + montoPedido + ") debe ser m&uacute;ltiplo de " + multiploUInversion);
					}
					// ------------------------------------------------------------------------------------------------------------------------
				}

				if (idCliente != -1) {// si el cliente existe, realizar validaciones

					// Si NO es cartera propia
					if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

						// ---------------Validar Venta a Empleados del Banco--------------------------------
						//boolean contratado = contratadosDAO.esContratado((String) parametrosEntrada.get("cedulaCliente"));
						boolean contratado = false;
						int inVentaEmpleados = (Integer) objResultado[12];
						// si el cliente es contratado y la unidad de inversión no permite venta a empleados del banco, no se puede tomar la orden
						if (contratado && inVentaEmpleados != 1) {
							listaMensajes.add("El cliente con el n&uacute;mero de cedula/rif " + (String) parametrosEntrada.get("cedulaCliente") + " es empleado del banco y la unidad de inversi&oacute;n " + (String) objResultado[13] + " no permite la venta a empleados. Seleccione otro cliente u otra unidad de inversi&oacute;n.");
						}
						// -----------------------------------------------------------------------------------

					}

					// ---------------------Validar multiples ordenes por cliente según instrumento financiero-------------------
					int inMultiplesOrdenes = (Integer) objResultado[14];
					if (parametrosEntrada.get("calculador") == null) {// si no viene del calculador
						if (inMultiplesOrdenes != 1) {// si el instrumento financiero no permite multiples ordenes por cliente
							OrdenDAO ordenDAO = new OrdenDAO(dso);

							// ---si existen ordenes para el cliente y la unidad de inversion seleccionada------
							if (ordenDAO.existenOrdenesNoCanceladasClienteUI((Long) parametrosEntrada.get("idUnidadInversion"), idCliente, (String) parametrosEntrada.get("tipoTransaccionNegocio"))) {
								listaMensajes.add("El Cliente ya posee una Orden asociada a la Unidad de Inversi&oacute;n seleccionada. El Instrumento Financiero " + (String) objResultado[15] + " no permite tomar m&aacute;s de una orden por cliente.");
							}
						}
					}
				}
				// ----------------------------------------------------------------------------------------------------------
			} catch (Exception e) {
				if (e.getMessage().indexOf("MSGTO9001") > -1) {
					listaMensajes.add("La Unidad de Inversion no esta registrada");
				} else {
					throw new Exception(e);
				}
			}

			// ---------------------Tipo de Instrumento Financiero-----------------------------------------------------
			String tipoInstrumento = (String) objResultado[3];

			if (tipoInstrumento.equals(ConstantesGenerales.INST_TIPO_INVENTARIO) || tipoInstrumento.equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO))
				indicaInventario = true;
			// -------------------------------------------------------------------------------------------------------------

			// 3.- Validar si la transaccion esta autorizada por la parametrizacion del Blotter

			UIBlotterRangosDAO uiBlotterRangosDAO = new UIBlotterRangosDAO(nombreDataSource, dso);
			UIBlotterRangos uiBlotterRangos = new UIBlotterRangos();
			BigDecimal monto_maximo_blot = new BigDecimal(0);
			BigDecimal monto_minimo_blot = new BigDecimal(0);
			int cantidad_maxima_blot = 0;
			int cantidad_minima_blot = 0;

			// ----VALIDACIONES DE BLOTTER------------------------------------------
			UIBlotterDAO uiBlotterDAO = new UIBlotterDAO(dso);
			UIBlotter uiBlotter;

			uiBlotter = uiBlotterDAO.obtenerBlotterUI((Long) parametrosEntrada.get("idUnidadInversion"), (String) parametrosEntrada.get("idBlotter"));

			// --------------Validar blotter no asociado a la unidad de inversión-----------------------------------------------------------
			if (uiBlotter == null) {
				listaMensajes.add("El Blotter no se encuentra asociado a la unidad de inversi&oacute;n");
				// ---salida por blotter no asociado a la UI---
				return listaMensajes;
			}
			// -------------------------------------------------------------------------------------------------------------------------------

			// boUI.buscarBlottersPorID((Long)parametrosEntrada.get("idUnidadInversion"), (String)parametrosEntrada.get("idBlotter"), (String)parametrosEntrada.get("tipoPersona"));

			// ------------------Validar disponibilidad del blotter---------------------------------------------------------------
			if (uiBlotter.getIndicaDisponible() != 1) {
				listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " no est&aacute; disponible para tomar &oacute;rdenes con la Unidad de Inversi&oacute;n seleccionada");
			}
			// -------------------------------------------------------------------------------------------------------------------

			// ---------------------Validar Blotter Restringido------------------------------------------------------------------------------------
			if (uiBlotter.getIndicaRestringido() == 1) {
				listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " se encuentra en restringido para la toma de &oacute;rdenes");
			}
			// --------------------------------------------------------------------------------------------------------------------------------------

			// ----------------------Validar Horario de Blotter--------------------------------------------------------------------------------

			String aux_hora_act = me.getFechaHoyFormateada(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_HORA_JAVA);
			Date hora_actual = me.StringToDate(aux_hora_act, com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_HORA_JAVA);

			// horas blotter
			Date hora_blot_desde = me.StringToDate(uiBlotter.getHorarioDesde(), com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_HORA_JAVA);
			Date hora_blot_hasta = me.StringToDate(uiBlotter.getHorarioHasta(), com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_HORA_JAVA);
			Date hora_blot_ult_dia_desde = me.StringToDate(uiBlotter.getHorarioDesdeUltDia(), com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_HORA_JAVA);
			Date hora_blot_ult_dia_hasta = me.StringToDate(uiBlotter.getHorarioHastaUltDia(), com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_HORA_JAVA);

			// formato para mostrar en mensajes
			SimpleDateFormat sdfh = new SimpleDateFormat("hh:mm:ss a");

			// obtener fecha actual
			Date hoy = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

			Date dateHoy = me.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA);

			// -----------------Validar horario de ultimo dia del Blotter----------------------------------------------------------------------

			if (dateHoy.compareTo(uiBlotter.getPeriodoFinal()) == 0) {// Si hoy es el ultimo dia
				if (hora_actual.before(hora_blot_ult_dia_desde)) {
					listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " no puede tomar &oacute;rdenes antes de la hora " + sdfh.format(hora_blot_ult_dia_desde) + " para el ultimo d&iacute;a");
				}

				if (hora_actual.after(hora_blot_ult_dia_hasta)) {
					listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " no puede tomar &oacute;rdenes despu&eacute;s de la hora " + sdfh.format(hora_blot_ult_dia_hasta) + " para el ultimo d&iacute;a");
				}
				// --------------------------------------------------------------------------------
			} else {// ----Si no es el ultimo dia validar contra horario normal

				if (hora_actual.before(hora_blot_desde)) {
					listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " no puede tomar &oacute;rdenes antes de la hora " + sdfh.format(hora_blot_desde));
				}
				if (hora_actual.after(hora_blot_hasta)) {
					listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " no puede tomar &oacute;rdenes despu&eacute;s de la hora " + sdfh.format(hora_blot_hasta));
				}

			}
			// --------------------------------------------------------------------------------------------------------------------------------

			// -----------------Validar Rango de Fechas del Blotter-------------------------------------------------------------------------

			if (dateHoy.before(uiBlotter.getPeriodoInicial())) {
				listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " no puede tomar &oacute;rdenes antes de la fecha " + sdf.format(uiBlotter.getPeriodoInicial()));
			}

			if (dateHoy.after(uiBlotter.getPeriodoFinal())) {
				listaMensajes.add("El Blotter " + uiBlotter.getDescripcionBlotter() + " no puede tomar &oacute;rdenes despu&eacute;s de la fecha " + sdf.format(uiBlotter.getPeriodoFinal()));
			}
			// --------------------------------------------------------------------------------------------------------------------------
			
			//ESTA VALIDACION APLICA PARA SOLO EL BLOTTER ELECTRONICO
			String tipoOperacionElectronico = ConstantesGenerales.BLOTTER_TIPO_OPERACION_ELEC;
			// Buscar información de los rangos de Validación del Blotter
			uiBlotterRangos = uiBlotterRangosDAO.listarBlotterRangosObj((Long) parametrosEntrada.get("idUnidadInversion"), (String) parametrosEntrada.get("idBlotter"), (String) parametrosEntrada.get("tipoPersona"), tipoOperacionElectronico);

			if (uiBlotterRangos == null) {// Si el objeto con la informacion requerida de acuerdo al blotter, la UI y el tipo de persona es nulo, no es posible tomar la orden ni realizar las validadciones respectivas

				listaMensajes.add("El Blotter no puede tomar ordenes para los tipos de persona " + (String) parametrosEntrada.get("tipoPersona") + " y la unidad de Inversi&oacute;n seleccionada");

			} else {

				monto_maximo_blot = uiBlotterRangos.getMontoMaximoInversion();
				monto_minimo_blot = uiBlotterRangos.getMontoMinimoInversion();
				cantidad_maxima_blot = uiBlotterRangos.getCantMaximaInversion();
				cantidad_minima_blot = uiBlotterRangos.getCantMinimaInversion();
			}

			// Validar financiamiento si no es cartera propia
			if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

				// --------------validar rango de financiamento----------------------------------
				BigDecimal maxFinanciamiento = uiBlotterRangos.getMaxFinanciamiento();
				BigDecimal porcFinanciado = (BigDecimal) parametrosEntrada.get("porcentajeFinanciado");
				if (porcFinanciado.doubleValue() > maxFinanciamiento.doubleValue()) {
					listaMensajes.add("El porcentaje a financiar sobrepasa el porcentaje m&aacute;ximo de financiamiento permitido para la Unidad de Inversi&oacute;n");
				}
				// -----------------------------------------------------------------------------------
			}

			// ----------------Validar rango de precio de compra----------------------------------
			// -----Validar solo para instrumentos subasta
			if (!indicaInventario) {
				BigDecimal precioCompra = (BigDecimal) parametrosEntrada.get("precioOfrecido");
				BigDecimal minPrecioCompra = uiBlotterRangos.getPrecioMinimo();
				BigDecimal maxPrecioCompra = uiBlotterRangos.getPrecioMaximo();

				if (precioCompra.doubleValue() < minPrecioCompra.doubleValue()) {
					listaMensajes.add("El Precio de compra indicado no puede ser menor al precio m&iacute;nimo establecido para la unidad de inversi&oacute;n (" + minPrecioCompra + "%)");
				}
				// -----------Sólo para subasta competitiva----------------------------
				if (tipoInstrumento.equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA)) {
					if (precioCompra.doubleValue() > maxPrecioCompra.doubleValue()) {
						listaMensajes.add("El Precio de compra indicado no puede ser mayor al precio m&aacute;ximo establecido para la unidad de inversi&oacute;n (" + maxPrecioCompra + "%)");
					}
				}
			}
			// ------------------------------------------------------------------------------------

			if (inPedidoMontoUI == 1) {

				if (montoPedido.doubleValue() == 0) {
					listaMensajes.add("El Monto a Invertir es requerido para la Toma de Orden");
				} else {

					montoAux = montoPedido.subtract(monto_minimo_blot);
					if (montoAux.doubleValue() < 0) {
						montoAux = monto_minimo_blot;
						editado = dFormato.format(montoAux.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
						listaMensajes.add("El Monto debe ser mayor o igual a: " + editado);
					}
					montoAux = montoPedido.subtract(monto_maximo_blot);
					if (montoAux.doubleValue() > 0) {
						montoAux = monto_maximo_blot;
						editado = dFormato.format(montoAux.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
						listaMensajes.add("El Monto debe ser menor o igual a: " + editado);
					}

				}

			} else {

				if (cantidadPedida == 0) {
					listaMensajes.add("La Cantidad a Invertir es requerida para la Toma de Orden");
				} else {

					// calcular el monto en base a la cantidad

					if (cantidadPedida != 0 && montoPedido.doubleValue() == 0) {
						montoPedido = (BigDecimal) objResultado[1];
						montoPedido = montoPedido.multiply(new BigDecimal(cantidadPedida));
					}
					if (cantidadPedida == 0) {
						montoAux = (BigDecimal) objResultado[1];

						cantidadPedida = montoPedido.longValue() / montoAux.longValue();
						int resto = montoPedido.intValue() % montoAux.intValue();
						if (resto != 0) {
							montoPedido = (BigDecimal) objResultado[1];
							montoPedido = montoPedido.multiply(new BigDecimal(cantidadPedida));
						}
					}

					// Validar rangos de Cantidad
					cantAux = cantidadPedida - cantidad_minima_blot;
					if (cantAux < 0) {
						listaMensajes.add("La Cantidad debe ser mayor a: " + cantidad_minima_blot);
					}

					cantAux = cantidadPedida - cantidad_maxima_blot;
					if (cantAux > 0) {
						listaMensajes.add("La Cantidad debe ser menor a: " + cantidad_maxima_blot);
					}

				}
			}

			// 6.- Si la UI es del Tipo Inventario:
			if (indicaInventario) {
				// a.- Determinar si hay disponible para la Toma de Orden si es inventario :
				// objResultado[4] = undinv_umi_inv_disponible
				montoAux = montoPedido.subtract((BigDecimal) objResultado[4]);
				if (montoAux.doubleValue() > 0) {
					montoAux = (BigDecimal) objResultado[4];
					editado = dFormato.format(montoAux.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					listaMensajes.add("El Inventario no cubre el requerimiento. El disponible es: " + editado);
				}

			}

			// --Validar transacciones financieras si no es cartera propia
			if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

				// ----------------------------------------------------------------------------------------------------

				// ------------VALIDACIONES DE CUENTA DEL CLIENTE
				// -----Buscar cuentas del cliente en altair
				ClienteCuentasDAO clienteCuentasDao = new ClienteCuentasDAO(dso);
				ArrayList<Cuenta> listaCuentas = clienteCuentasDao.buscarCuentasAltair((String) parametrosEntrada.get("cedulaCliente"), (String) parametrosEntrada.get("tipoPersona"), (ServletContext) parametrosEntrada.get("servletContext"), (String) parametrosEntrada.get("ipTerminal"), (String) parametrosEntrada.get("userName"));
				this.setListaCuentasCliente(listaCuentas);
				// Verificar si la cuenta esta asociada al cliente
				// if(!existeCuentaCliente(listaCuentas, (String)parametrosEntrada.get("numeroCuentaCliente"))){
				// listaMensajes.add("La cuenta Numero " +(String)parametrosEntrada.get("numeroCuentaCliente")+ " no se encuentra asociada al cliente "+ beanCliente.getNombre());
				// }

				// -------------VALIDACIONES DE PRECIOS DE RECOMPRA DE TITULOS-----------------------------------------------
				// ---recuperar lista de Titulos con recompra
				ArrayList recompraTitulos = new ArrayList();
				// ---Si se ha pactado una recompra de titulos
				if (parametrosEntrada.get("recompraTitulos") != null) {
					recompraTitulos = (ArrayList) parametrosEntrada.get("recompraTitulos");

					if (!recompraTitulos.isEmpty()) {

						for (int k = 0; k < recompraTitulos.size(); k++) {
							PrecioRecompra preciosTitulos = (PrecioRecompra) recompraTitulos.get(k);

							if (preciosTitulos.getTitulo_precio_recompra() != null) {
								if (preciosTitulos.getTitulo_precio_recompra().doubleValue() == 0) {
									listaMensajes.add("El precio de recompra para el t&iacute;tulo " + preciosTitulos.getTituloId() + " debe ser mayor a cero(0) si desea pactar la recompra.");
								}

							} else {
								listaMensajes.add("Debe especificar un precio para el t&iacute;tulo " + preciosTitulos.getTituloId() + " si desea pactar la recompra.");
							}

							if (preciosTitulos.getTasaPool() != null) {
								if (preciosTitulos.getTasaPool().doubleValue() == 0) {
									listaMensajes.add("La tasa pool para el t&iacute;tulo " + preciosTitulos.getTituloId() + " debe ser mayor a cero(0).");
								}

								if (preciosTitulos.getTitulo_precio_recompra().doubleValue() > preciosTitulos.getTasaPool().doubleValue()) {
									listaMensajes.add("El precio de recompra debe ser menor que la tasa pool para el t&iacute;tulo " + preciosTitulos.getTituloId() + ".");
								}
							}

						}

						// si las instrucciones de pago deben ser validadas
						if ((String) parametrosEntrada.get("validarInstruccionesPago") != null) {

							// --SI SE PACTA UNA RECOMPRA DEBE EXISTIR UNA INSTRUCCIÓN DE PAGO PARA LA MISMA
							// ----------VALIDACIONES DE INSTRUCCIONES DE PAGO----------------------------------------------
							if (parametrosEntrada.get("instruccionPagoRecompra") != null) {

								ArrayList ArrayCtasInstruccionPago = (ArrayList) parametrosEntrada.get("instruccionPagoRecompra");

								if (!ArrayCtasInstruccionPago.isEmpty()) {

									// verificar las instrucciones de pago existentes
									for (int k = 0; k < ArrayCtasInstruccionPago.size(); k++) {

										CuentaCliente cuentaCliente = (CuentaCliente) ArrayCtasInstruccionPago.get(k);

										if (cuentaCliente.getTipo_instruccion_id() != null) {

											// Si es una transferencia a cuenta internacional
											if (cuentaCliente.getTipo_instruccion_id().equals(String.valueOf(TipoInstruccion.CUENTA_SWIFT))) {
												// --validar datos requeridos
												if (cuentaCliente.getCtecta_numero() == null || cuentaCliente.getCtecta_numero().equals("")) {
													listaMensajes.add("Debe indicar el n&uacute;mero de cuenta para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
												}
												if (cuentaCliente.getCtecta_bcocta_bco() == null || cuentaCliente.getCtecta_bcocta_bco().equals("")) {
													listaMensajes.add("Debe indicar el nombre del banco para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
												}
													/*if(cuentaCliente.getCtecta_bcocta_direccion()==null || cuentaCliente.getCtecta_bcocta_direccion().equals("")){
														listaMensajes.add("Debe indicar la direcci&oacute;n del banco para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
													}*/								
													
													/*if(cuentaCliente.getCtecta_bcocta_swift()==null || cuentaCliente.getCtecta_bcocta_swift().equals("")){
														listaMensajes.add("Debe indicar el c&oacute;digo SWIFT para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
													}	*/

												// validar codigo BIC y ABA si no se indica banco intermediario
												if (cuentaCliente.getCtecta_bcoint_bco() == null || cuentaCliente.getCtecta_bcoint_bco().equals("")) {

													// Si el codigo ABA es nulo, debe indicarse el codigo BIC (SWIFT)
														if((cuentaCliente.getCtecta_bcocta_bic()==null || cuentaCliente.getCtecta_bcocta_bic().equals("")) &&
															(cuentaCliente.getCtecta_bcocta_aba()==null || cuentaCliente.getCtecta_bcocta_aba().equals(""))){

														listaMensajes.add("Debe indicar el c&oacute;digo ABA para la instrucci&oacute;n de pago por transferencia a cuenta internacional. O en su defecto el c&oacute;digo BIC.");

													}

													// Validar que no se ingresen ambos c&oacute;digos BIC y ABA
														if((cuentaCliente.getCtecta_bcocta_bic()!=null && !cuentaCliente.getCtecta_bcocta_bic().equals("")) &&
															(cuentaCliente.getCtecta_bcocta_aba()!=null && !cuentaCliente.getCtecta_bcocta_aba().equals(""))){

														listaMensajes.add("Debe indicar c&oacute;digo ABA o BIC para la instrucci&oacute;n de pago por transferencia a cuenta internacional, pero no ambos c&oacute;digos.");

													}

												}

												/*
													/*if(cuentaCliente.getCtecta_bcocta_bic()==null || cuentaCliente.getCtecta_bcocta_bic().equals("")){
														listaMensajes.add("Debe indicar el c&oacute;digo BIC para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
													}			
													if(cuentaCliente.getCtecta_bcocta_aba()==null || cuentaCliente.getCtecta_bcocta_aba().equals("")){
														listaMensajes.add("Debe indicar el c&oacute;digo ABA para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
													}*/
													
													/*if(cuentaCliente.getCtecta_bcocta_telefono()==null || cuentaCliente.getCtecta_bcocta_telefono().equals("")){
														listaMensajes.add("Debe indicar el n&uacute;mero tel&eacute;fono para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
													}	*/
													
												// Validaciones Banco intermediario
												// si se indicó un banco intermediario
												if (cuentaCliente.getCtecta_bcoint_bco() != null && !cuentaCliente.getCtecta_bcoint_bco().equals("")) {

														/*if(cuentaCliente.getCtecta_bcoint_direccion()==null || cuentaCliente.getCtecta_bcoint_direccion().equals("")){
															listaMensajes.add("Debe indicar la direcci&oacute;n del banco intermediario para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
														}	*/
														
														/*if(cuentaCliente.getCtecta_bcoint_bic()==null || cuentaCliente.getCtecta_bcoint_bic().equals("")){
															listaMensajes.add("Debe indicar el c&oacute;digo BIC del banco intermediario para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
														}				
														if(cuentaCliente.getCtecta_bcoint_aba()==null || cuentaCliente.getCtecta_bcoint_aba().equals("")){
															listaMensajes.add("Debe indicar el c&oacute;digo ABA del banco intermediario para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
														}	*/
														
														//Validar numero de cuenta del banco en el banco intermediario
														if(cuentaCliente.getCtecta_bcoint_swift()==null || cuentaCliente.getCtecta_bcoint_swift().equals("")){
															listaMensajes.add("Debe indicar el n&uacute;mero de cuenta del banco destino en el banco intermediario para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
														}									
									
														//Si el codigo ABA es nulo, debe indicarse el codigo BIC (SWIFT) y viceversa
														if((cuentaCliente.getCtecta_bcoint_bic()==null || cuentaCliente.getCtecta_bcoint_bic().equals("")) &&
															(cuentaCliente.getCtecta_bcoint_aba()==null || cuentaCliente.getCtecta_bcoint_aba().equals(""))){
															
															listaMensajes.add("Debe indicar el c&oacute;digo ABA del banco intermediario para la instrucci&oacute;n de pago por transferencia a cuenta internacional. O en su defecto el c&oacute;digo BIC.");
															
														}
														
														//Validar que no se ingresen ambos c&oacute;digos BIC y ABA
														if((cuentaCliente.getCtecta_bcoint_bic()!=null && !cuentaCliente.getCtecta_bcoint_bic().equals("")) &&
															(cuentaCliente.getCtecta_bcoint_aba()!=null && !cuentaCliente.getCtecta_bcoint_aba().equals(""))){
																
																listaMensajes.add("Debe indicar c&oacute;digo ABA o BIC para la instrucci&oacute;n de pago por transferencia a cuenta internacional, pero no ambos c&oacute;digos.");
																
														}

														/*if(cuentaCliente.getCtecta_bcoint_telefono()==null || cuentaCliente.getCtecta_bcoint_telefono().equals("")){
															listaMensajes.add("Debe indicar el n&uacute;mero tel&eacute;fono del banco intermediario para la instrucci&oacute;n de pago por transferencia a cuenta internacional.");
														}	*/

												}

											} else {

												// Si es cheque (TipoInstruccion.CHEQUE)...no hay validaciones pertinentes
												// ------------------------------------------------------------------------------------------------------
												// Si NO es recompra con Neteo (Las recompras con neteo no necesitan instrucciones de pago
												if (recompraConNeteoUI != 1) {
													// si se pacta recompra para una un titulo en moneda nacional
													if (cuentaCliente.getTipo_instruccion_id().equals(String.valueOf(TipoInstruccion.CUENTA_NACIONAL))) {

														if (cuentaCliente.getCtecta_numero() == null || cuentaCliente.getCtecta_numero().equals("")) {
															listaMensajes.add("Debe indicar el n&uacute;mero de cuenta para la instrucci&oacute;n de pago en moneda nacional.");
														}

													}
												}
											}

										}
									}

								} else {
									if (recompraConNeteoUI != 1) {// si no es recompra con neteo
										listaMensajes.add("Debe indicar las instrucciones de pago para la recompra pactada.");
									}
								}

							} else {
								listaMensajes.add("Debe indicar las instrucciones de pago para la recompra pactada.");
							}
							// ---------------------------------------------------------------------------------------------

						}
					}
				}

				// Verifica si el instrumento es sitme para validaciones de Montos
				if (tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)) {
					this.validarReglasSitme();
				}
				// ----------------------------------------------------------------------------------------------------------

			}// FIN VALIDACION SI NO ES CARTERA PROPIA

			// ---- Salida si se encontro errores de integridad de informacion y parametrizacion de aplicacion ----
			if (listaMensajes.size() > 0) {
				return listaMensajes;
			}

			parametrosEntrada.put("montoPedido", montoPedido);
			//parametrosEntrada.put("cantidadPedida", cantidadPedida);
			return listaMensajes;

		}
	}

	/**
	 * Busca una persona en altair de acuerdo a su numero de cedula y tipo de persona
	 * 
	 * @param cedula
	 * @param tipoPersona
	 * @throws Exception
	 */
	private void buscarPersonaAltair(String cedula, String tipoPersona) throws Exception {

		String nombreUsuario = (String) parametrosEntrada.get("userName");
		ManejadorDeClientes mdc = new ManejadorDeClientes((ServletContext) parametrosEntrada.get("servletContext"), null);

		// Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
		com.bdv.infi.webservices.beans.Cliente clienteWS = mdc.getCliente(cedula, tipoPersona, nombreUsuario, (String) parametrosEntrada.get("ipTerminal"), false, true, false ,false);

	}

	/**
	 * Valida que el cliente no haya hecho una solicitud en los últimos días (Cantidad de días depende de parametro)
	 * 
	 * @param beanTOSimulada
	 * @param cantDiasUltraT
	 * @return true si el cliente ha realizado una solicitud, false en caso contrario
	 * @throws Exception
	 */
	public boolean validarSolicitudUltraTemprano(int cantDiasUltraT) throws Exception {

		boolean esSolicitudUltraTemprano = false;

		if (cantDiasUltraT > 0) {// Si se han configurado días de validación para Ultra temprano
			ManejadorUltraTemprano mUT = new ManejadorUltraTemprano((ServletContext) parametrosEntrada.get("servletContext"), null);

			String fechaHasta = Utilitario.DateToString(new Date(), ConstantesGenerales.FORMATO_FECHA2);
			String fechaDesde = Utilitario.DateToString(Utilitario.obtenerFechaPasada(new Date(), cantDiasUltraT), ConstantesGenerales.FORMATO_FECHA2);
			try {
				esSolicitudUltraTemprano = mUT.esSolicitudUltraTemprano(
						(String) parametrosEntrada.get("cedulaCliente"),
						(String) parametrosEntrada.get("tipoPersona"), fechaDesde,
						fechaHasta, (String) parametrosEntrada.get("ipTerminal"));
			} catch (Exception e) {
				throw e;
			}
		}

		return esSolicitudUltraTemprano;
	}

	/**
	 * Valida el saldo del cliente de la cuenta seleccionada contra el monto total a pagar en la orden
	 * 
	 * @param beanTOSimulada
	 * @return Lista con mensajes de validaci&oacute;n de saldos
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	public ArrayList validarSaldoCliente(TomaOrdenSimulada beanTOSimulada) throws IOException, JiBXException, Exception {
		ArrayList<String> ListaValidacionesSaldo = new ArrayList<String>();

		if (!operacionesMiscelaneo(beanTOSimulada)) {
			ArrayList<Cuenta> listaCuentas = this.listaCuentasCliente;
			// ---Verificar el saldo de la cuenta----------------------------------------
			if (!saldoSuficiente(listaCuentas, (String) parametrosEntrada.get("numeroCuentaCliente"), beanTOSimulada.getMontoTotalPagar())) {
				ListaValidacionesSaldo.add("No es posible tomar la orden ya que el saldo del cliente es insuficiente para cancelar el monto total de la inversi&oacute;n.");
			}
		}
		// ------------------------------------------
		return ListaValidacionesSaldo;
	}

	/**
	 * Verifica si el tipo de operaciones de una orden es miscelaneo
	 * 
	 * @param beanTOSimulada
	 * @return true si las operaciones son tipo miscelaneo, false en caso contrario (bloqueo o debito)
	 */
	private boolean operacionesMiscelaneo(TomaOrdenSimulada beanTOSimulada) {
		boolean esMiscelaneo = false;
		ArrayList<OrdenOperacion> listaOperaciones = beanTOSimulada.getListaOperaciones();

		for (int k = 0; k < listaOperaciones.size(); k++) {
			OrdenOperacion oper = listaOperaciones.get(k);
			// Si operacion encontrada es Miscelaneo (todas miscelaneo) retornar verdadero
			if (oper.getTipoTransaccionFinanc().equals(TransaccionFinanciera.MISCELANEO)) {
				esMiscelaneo = true;
			}
			// Se actualiza la operación para evitar problemas en adjudicación y liquidación
			if (oper.getTipoTransaccionFinanc().equals(TransaccionFinanciera.MISCELANEO_VERIFICABLE)) {
				oper.setTipoTransaccionFinanc(TransaccionFinanciera.MISCELANEO);
			}
		}
		return esMiscelaneo;
	}

	/**
	 * Verifica el saldo de la cuenta del cliente
	 * 
	 * @param listaCuentas
	 * @param cuentaEnviada
	 * @param montoInversion2
	 * @return true si el saldo es suficiente, false en caso contrario
	 */
	private boolean saldoSuficiente(ArrayList<Cuenta> listaCuentas, String cuentaEnviada, BigDecimal montoPagar) {
		logger.info("Validando saldo...");
		boolean saldoSuficiente = true;
		if (listaCuentas != null && !listaCuentas.isEmpty()) {
			for (int k = 0; k < listaCuentas.size(); k++) {
				Cuenta cuenta = (Cuenta) listaCuentas.get(k);
				if (cuentaEnviada.equalsIgnoreCase(cuenta.getNumero())) {
					double saldoDisponible = 0;
					if (cuenta.getSaldoDisponible() != null)
						saldoDisponible = cuenta.getSaldoDisponible().doubleValue();

					if (saldoDisponible < montoPagar.doubleValue()) {
						saldoSuficiente = false;
					}
				}
			}
		}
		return saldoSuficiente;
	}

	/**
	 * Verifica si la cuenta enviada esta asociada al cliente
	 * 
	 * @param listaCuentas
	 * @param cuentaEnviada
	 * @return true si esta asociada la cuenta al cliente, false en caso contrario
	 */
	private boolean existeCuentaCliente(ArrayList<Cuenta> listaCuentas, String cuentaEnviada) {

		boolean existeCuenta = false;
		if (listaCuentas != null && !listaCuentas.isEmpty()) {
			for (int k = 0; k < listaCuentas.size(); k++) {
				Cuenta cuenta = (Cuenta) listaCuentas.get(k);
				if (cuentaEnviada.equalsIgnoreCase(cuenta.getNumero())) {
					existeCuenta = true;
					break;
				}
			}
		}
		return existeCuenta;

	}

	/**
	 * Obtiene el saldo de la cuenta del cliente en Altair
	 * 
	 * @param listaCuentas
	 * @param cuentaEnviada
	 * @return saldo de la cuenta del cliente
	 */
	private BigDecimal obtenerSaldoCtaCliente(ArrayList<Cuenta> listaCuentas, String cuentaEnviada) {

		BigDecimal saldoCuenta = new BigDecimal(0);

		if (listaCuentas != null && !listaCuentas.isEmpty()) {
			for (int k = 0; k < listaCuentas.size(); k++) {
				Cuenta cuenta = (Cuenta) listaCuentas.get(k);
				if (cuentaEnviada.equalsIgnoreCase(cuenta.getNumero())) {
					saldoCuenta = cuenta.getSaldoDisponible();
					break;
				}
			}
		}
		return saldoCuenta;
	}

	/**
	 * Verifica si el cliente recibido existe en altair
	 * 
	 */
	private void buscarClienteAltair() throws Exception {

		try {

			String nombreUsuario = (String) parametrosEntrada.get("userName");
			ManejadorDeClientes mdc = new ManejadorDeClientes((ServletContext) parametrosEntrada.get("servletContext"), null);

			// Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
			com.bdv.infi.webservices.beans.Cliente clienteWS = mdc.getCliente((String) parametrosEntrada.get("cedulaCliente"), (String) parametrosEntrada.get("tipoPersona"), nombreUsuario, (String) parametrosEntrada.get("ipTerminal"), false, true, false ,false);

			// Cliente no existente en INFI
			idCliente = 0;
			beanCliente.setIdCliente(idCliente);

		} catch (Exception e) {
			// No existe el cliente en altair
			if (e.getMessage().contains("@ERPEE0221")) {
				listaMensajes.add("El Cliente no se encuentra registrado en Altair");
				logger.error(e.getMessage() + com.bdv.infi.util.Utilitario.stackTraceException(e));
			} else {
				// Error realizando la consulta en altair
				listaMensajes.add("Error consultando el cliente en arquitectura extendida.");
				logger.error(e.getMessage() + com.bdv.infi.util.Utilitario.stackTraceException(e));
			}

		}
	}

	/**
	 * Verifica si el cliente existe en INFI, sino es buscado en altair para insertarlo en el sistema
	 * 
	 * @throws Exception
	 */
	public void verificarExistenciaCliente(ArrayList<SolicitudClavenet> ordenes) throws Exception {
		ClienteDAO boCliente = new ClienteDAO(nombreDataSource, dso);
		try {
			boCliente.listarPorCedula(
					(String)parametrosEntrada.get("tipoPersona"), (String)parametrosEntrada.get("cedulaCliente"));
			beanCliente = boCliente.getCliente();
			
			//NM29643
			if(beanCliente==null && ordenes.size()>0){
				
				String tipoPer = ordenes.get(0).getCedRifCliente().substring(0, 1);
				String cedRif = ordenes.get(0).getCedRifCliente().substring(1, ordenes.get(0).getCedRifCliente().length() - 1);
				
				if (Long.valueOf(cedRif) > 0){
					try{
																																												
						//beanCliente = new com.bdv.infi.data.Cliente();
						beanCliente = new Cliente();
						beanCliente.setRifCedula(Long.parseLong(cedRif));
						String nombreCliente = ordenes.get(0).getNombreCliente();
						
						if(nombreCliente!=null){
							nombreCliente=nombreCliente.trim();
							nombreCliente=nombreCliente.toUpperCase();
						}
						beanCliente.setNombre(nombreCliente);
						beanCliente.setTipoPersona(tipoPer);
						String direccionCliente = ordenes.get(0).getCtaDireccionC();
						
						if(direccionCliente!=null){
							direccionCliente=direccionCliente.trim();
							direccionCliente=direccionCliente.toUpperCase();
						}
						beanCliente.setDireccion(direccionCliente);
						
						//TODO: VERIFICAR CUAL CAMPO DE NUMERO SE INGRESARA
						String clienteNumeroTlf = ordenes.get(0).getCtaTlf();
						if(clienteNumeroTlf!=null){
							clienteNumeroTlf=clienteNumeroTlf.trim();
							clienteNumeroTlf=clienteNumeroTlf.toUpperCase();
						}
						beanCliente.setTelefono(clienteNumeroTlf);
						
						//beanCliente.setTipo(ConstantesGenerales.CLIENTE_BANCA_COMERCIAL);
						
						String correoCliente=ordenes.get(0).getEmailCliente();
						if(correoCliente!=null){
							correoCliente=correoCliente.trim();
							correoCliente=correoCliente.toUpperCase();
						}
						//beanCliente.setCorreoElectronico(correoCliente);
						//beanCliente.setEmpleado(false);												
						//beanCliente.setCodigoSegmento(ConstantesGenerales.CODIGO_SEGMENTO); //TODO: Verificar valor de la constante de segmento
						
						/*clienteDAO.insertar(beanCliente);
						idCliente = String.valueOf(beanCliente.getIdCliente());
						clienteDAO.cerrarConexion();*/
														
					}catch (Exception e) {	
						System.out.println("Ha ocurrido un error en el proceso de creacion de cliente en INFI " + e.getMessage());
						logger.error("Ha ocurrido un error en el proceso de creacion de cliente en INFI " + e.getMessage());
						throw e;
					}
				}
				
			}else{
				// ----------obtener id del cliente----------------------------------------
				idCliente = beanCliente.getIdCliente();
			}

		} catch (Exception e) {
			if (e.getMessage().indexOf("MSGTO8001") > -1) {// cliente no encontrado en INFI
				logger.info("NO EXISTE CLIENTE EN INFI");
				insertarClienteNuevoDeAltair();// Buscar cliente en altair y registrarlo

			} else {
				logger.error(e.getMessage() + com.bdv.infi.util.Utilitario.stackTraceException(e));
				throw new Exception(e);
			}
		}

	}

	/**
	 * Inserta un nuevo Cliente en INFI con los datos encontrados en Altair
	 * 
	 * @throws Exception
	 */
	public void insertarClienteNuevoDeAltair() throws Exception {

		try {

			String nombreUsuario = (String) parametrosEntrada.get("userName");
			ManejadorDeClientes mdc = new ManejadorDeClientes((ServletContext) parametrosEntrada.get("servletContext"), null);

			// Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
			com.bdv.infi.webservices.beans.Cliente clienteWS = mdc.getCliente((String) parametrosEntrada.get("cedulaCliente"), (String) parametrosEntrada.get("tipoPersona"), nombreUsuario, (String) parametrosEntrada.get("ipTerminal"), false, true, false ,false);

			// Si lo encuentra lo almacena en la tabla de cliente
			com.bdv.infi.data.Cliente clienteNuevo = new com.bdv.infi.data.Cliente();
			// clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi().replaceAll("^0+", "")));
			clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi()));
			clienteNuevo.setNombre(clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " "));
			clienteNuevo.setTipoPersona(clienteWS.getTipoDocumento().trim());
			clienteNuevo.setDireccion(clienteWS.getDireccion());
			clienteNuevo.setTelefono(clienteWS.getTelefono());
			clienteNuevo.setTipo(clienteWS.getTipo().trim());
			clienteNuevo.setCorreoElectronico(clienteWS.getCorreoElectronico());
			clienteNuevo.setEmpleado(clienteNuevo.EMPLEADO.equals(clienteWS.getEsEmpleado()) ? true : false);
			clienteNuevo.setCodigoSegmento(clienteWS.getPEM1403().getSegmento().trim());

			// insertar nuevo cliente en base de datos INFI
			com.bdv.infi.dao.ClienteDAO clienteDao = new com.bdv.infi.dao.ClienteDAO(dso);
			try {
				clienteDao.insertar(clienteNuevo);

				// setear id de cliente generado
				idCliente = clienteNuevo.getIdCliente();
				beanCliente.setIdCliente(idCliente);

				logger.info("CLIENTE NUEVO CON ID " + beanCliente.getIdCliente());

			} catch (Exception e) {
				throw new Exception("Error insertando cliente en INFI.");
			} finally {
				clienteDao.cerrarConexion();
			}

		} catch (Exception e) {
			logger.error(e.getMessage() + com.bdv.infi.util.Utilitario.stackTraceException(e));
			throw new Exception("Error consultando el cliente en arquitectura extendida.");
		}

	}

	/**
	 * Verifica que una operacion contenga los seriales correspondientes
	 * 
	 * @param ordenOperacion
	 * @return true si posee los seriales, false en caso contrario
	 */
	private boolean validaSeriales(OrdenOperacion ordenOperacion) {
		boolean operacionConSerial = true;

		if (ordenOperacion.getCodigoOperacion() == null || ordenOperacion.getCodigoOperacion().trim().equals("")) {

			operacionConSerial = false;
		}

		return operacionConSerial;
	}

	/**
	 * Metodo de calculo de una TomaOrden en base a los parametros dados
	 * 
	 * @param parametrosEntrada
	 * @return TomaOrdenSimulada con la informacion resultado de la simulacion
	 * @throws Throwable
	 */
	public TomaOrdenSimulada simuladorTO() throws Throwable {
		TransaccionFija transaccionFija = null;
		Integer internoBDV = 0;
		BigDecimal montoCapitalConIDB = new BigDecimal(0);
		BigDecimal montoCapitalSinIDB = new BigDecimal(0);
		BigDecimal montoTotalNeteo = new BigDecimal(0);
		boolean indicadorFinanciada = (Boolean) parametrosEntrada.get("IndicadorFinanciada");
		boolean ordenConFinanciamiento = false;
		boolean esMonedaLocalUI = false;
		String fechaValor = "";
		String fv = "";
		reiniciarValores();
		
		montoPedido = (BigDecimal) parametrosEntrada.get("montoPedido");

		// indicador de toma de Orden por Cartera Propia
		if (((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {
			internoBDV = 1;
		}

		// Buscar vehiculo en caso de venir nulo
		// validaciones de vehiculo
		if(parametrosEntrada.get("idVehiculoRecompra")==null || parametrosEntrada.get("idVehiculoRecompra").equals("")
				|| parametrosEntrada.get("idVehiculoColocador")==null || parametrosEntrada.get("idVehiculoColocador").equals("")
				|| parametrosEntrada.get("idVehiculoTomador")==null || parametrosEntrada.get("idVehiculoTomador").equals("")){

			VehiculoRolDAO vehiculoRolDAO = new VehiculoRolDAO(dso);
			vehiculoRolDAO.listarVehiculoRolPorDefecto();
			DataSet vehiculo_defecto = vehiculoRolDAO.getDataSet();

			vehiculo_defecto.next();

			parametrosEntrada.put("idVehiculoRecompra", vehiculo_defecto.getValue("id_veh_recompra"));
			parametrosEntrada.put("idVehiculoColocador", vehiculo_defecto.getValue("id_colocador"));
			parametrosEntrada.put("idVehiculoTomador", vehiculo_defecto.getValue("id_tomador"));

		}

		// ----Buscar la Moneda Local-------------------------
		try {
			if (idMoneda.equals("")) {
				idMoneda = monedaDAO.listarIdMonedaLocal();
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		// ----------------------------------------------------

		// Porcentaje financiado
		BigDecimal pctFinanciado = (BigDecimal) parametrosEntrada.get("porcentajeFinanciado");

		// ///---Indicador de orden Financiada////////////////////////////
		if (indicadorFinanciada && pctFinanciado.doubleValue() > 0) {
			ordenConFinanciamiento = true;
		}
		// ////////////////////////////////////////////////////////////
		//ESTA VALIDACION APLICA PARA SOLO EL BLOTTER ELECTRONICO
		String tipoOperacionElectronico = ConstantesGenerales.BLOTTER_TIPO_OPERACION_ELEC;
		
		// Buscar información de los rangos de Validación del Blotter
		UIBlotterRangos uiBlotterRangos = obtenerBlotterRangos(String.valueOf(parametrosEntrada.get("idUnidadInversion")), (String) parametrosEntrada.get("idBlotter"), (String) parametrosEntrada.get("tipoPersona"), tipoOperacionElectronico);
		BigDecimal comisionEmisor = uiBlotterRangos.getComisionEmisor();

		// 1.- Buscar informacion de la Unidad de Inversion.
		// No requiere de informacion de limites de compra
		try {
			if ((Long) parametrosEntrada.get("idUnidadInversion") != unidadInversionActiva) {
				objResultadoSimular = boUISimular.listarID((Long) parametrosEntrada.get("idUnidadInversion"), false);
				unidadInversionActiva = (Long) parametrosEntrada.get("idUnidadInversion");
				boUISimular.listarTitulosID((Long) parametrosEntrada.get("idUnidadInversion"),true); //Modificado por NM25287. Requerimiento SICAD
				listaTitulos = boUISimular.getListaTitulos();

				idEmpresa = (String) objResultadoSimular[0];
				//tasaCambio = (BigDecimal) objResultadoSimular[2];
				tasaCambio = (BigDecimal) parametrosEntrada.get("tasaPropuesta");
				indicaPrecioSucioUI = (Integer) objResultadoSimular[9];
				idMonedaUI = (String) objResultadoSimular[10];

				recompraConNeteoUI = (Integer) objResultadoSimular[17];// Indicador de recompra con neteo
				tipoProductoId = (String) objResultadoSimular[19];
			}
		} catch (Exception e) {
			throw new Exception(e);
		}

		String tipoInstrumento = (String) objResultadoSimular[3];
		if (tipoInstrumento.equals(ConstantesGenerales.INST_TIPO_INVENTARIO) || tipoInstrumento.equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO))
			indicaInventario = true;

		// 2.- Determinar si hay disponible para la Toma de Orden si es inventario
		if (indicaInventario) {
			BigDecimal montoDisponible = (BigDecimal) objResultadoSimular[4];
			if (montoPedido.doubleValue() > montoDisponible.doubleValue()) {
				throw new Exception("No existe monto Disponible en Inventario . . . .");
			}
		}

		if (idMoneda.equals(idMonedaUI)) {
			esMonedaLocalUI = true;
		}

		// 4.- Armar el bean de Respuesta para Inventario
		montoInversion = montoPedido;

		// Multiplicar por la tasa de cambio cuando la moneda de la UI no es local
		if (!esMonedaLocalUI) {
			montoInversion = montoInversion.multiply(tasaCambio);
		}

		montoInversion = montoInversion.multiply((BigDecimal) parametrosEntrada.get("precioOfrecido"));
		montoInversion = montoInversion.divide(CIEN_BD, 4, BigDecimal.ROUND_HALF_UP);
		montoInversion = montoInversion.setScale(2, BigDecimal.ROUND_HALF_UP);

		Calendar hoy = Calendar.getInstance();
		// La fecha valor en TO, viene del html
		// La fecha valor en Adjudicacion viene de la fecha almacenada en la 204.

		fv = parametrosEntrada.get("fechaValor").toString();
		dateValor = formatear.parse(fv);

		Object [] objAux = {CERO_LOG, (Long)parametrosEntrada.get("idUnidadInversion"), 
				new Date(), dateValor,
				montoPedido, (Long)parametrosEntrada.get("cantidadComprar"), montoInversion, CERO_BD, CERO_BD, CERO_BD, CERO_BD,
				tasaCambio,(BigDecimal)parametrosEntrada.get("precioOfrecido"),	
				(String)parametrosEntrada.get("idBlotter"), new Long(beanCliente.getIdCliente()), idEmpresa,
				(String)parametrosEntrada.get("tipoTransaccionNegocio"), STATUS_ORDEN_REGISTRADA, 
				(String)parametrosEntrada.get("numeroCuentaCliente"), (BigDecimal)parametrosEntrada.get("porcentajeFinanciado"),	
				ordenConFinanciamiento, new Boolean(indicaInventario), (String)parametrosEntrada.get("ipTerminal"), (String)parametrosEntrada.get("idVehiculoTomador"), (String)parametrosEntrada.get("idVehiculoColocador"), (String)parametrosEntrada.get("idVehiculoRecompra"), (String)parametrosEntrada.get("userName"), internoBDV,
				(String)parametrosEntrada.get("actividadEconomica"),(String)parametrosEntrada.get("sectorProductivo"),(String)parametrosEntrada.get("concepto"),(String)parametrosEntrada.get("idInstruccion"),parametrosEntrada.get("fechaRecompra"),objResultadoSimular[18],
				""
		};

		beanTOSimulada = new TomaOrdenSimulada(objAux);

		// 5.- Calcular la cantidad de titulos a ser asignados
		// Se Crea el Objeto de la orden Tipo TITULO . . . . .
		OrdenTitulo beanTitulo;
		BigDecimal valorInvertidoTitulo = CERO_BD;

		// /Obtener Lista de Operaciones
		listaOperacion = beanTOSimulada.getListaOperaciones();

		// recuperar lista de Titulos con recompra
		ArrayList recompraTitulos = new ArrayList();

		if (parametrosEntrada.get("recompraTitulos") != null)
			recompraTitulos = (ArrayList) parametrosEntrada.get("recompraTitulos");

		for (int i = 0; i < listaTitulos.size(); i++) {

			BigDecimal montoNeteo = new BigDecimal(0);
			BigDecimal valorEquivMonedaUI = new BigDecimal(0);

			beanTitulo = (OrdenTitulo) listaTitulos.get(i);
			valorInvertidoTitulo = montoPedido.multiply(beanTitulo.getPorcentaje());
			valorInvertidoTitulo = valorInvertidoTitulo.divide(CIEN_BD, 4, BigDecimal.ROUND_HALF_UP);
			valorInvertidoTitulo = valorInvertidoTitulo.divide(beanTitulo.getValorNominal(), 4, BigDecimal.ROUND_HALF_UP);
			valorEquivMonedaUI = valorInvertidoTitulo;

			// ---Convertir con tasa de cambio de la UI-------------------------
			// Convertir solo si la moneda del titulo es distinta a la moneda de la UI
			if (!beanTitulo.getSiglasMoneda().equalsIgnoreCase(idMonedaUI)) {
				if (esMonedaLocalUI) {// /dividir en caso de cambio de moneda local aextranjera
					valorInvertidoTitulo = valorInvertidoTitulo.divide(tasaCambio, 4, BigDecimal.ROUND_HALF_UP);
				} else
					// multiplicar en caso de moneda extranjera a moneda local
					valorInvertidoTitulo = valorInvertidoTitulo.multiply(tasaCambio);
			}
			// ---------------------------------------------------------------
			// Valor invertido del titulo
			beanTitulo.setValorInvertido(valorInvertidoTitulo.setScale(2, BigDecimal.ROUND_HALF_UP));
			// En Unidades
			beanTitulo.setUnidadesInvertidas(valorInvertidoTitulo.setScale(0, BigDecimal.ROUND_HALF_UP));

			if (parametrosEntrada.get("adjudicacion") == null) {
				// Obtener precios del titulo
				com.bdv.infi.dao.PrecioRecompraDAO precioRecompraDAO = new com.bdv.infi.dao.PrecioRecompraDAO(dso);
				PrecioRecompra precioRecompra = new PrecioRecompra();
				precioRecompra = precioRecompraDAO.obtenerPrecioRecompraTitulo(beanTitulo.getIdTitulo(), valorInvertidoTitulo, beanTitulo.getSiglasMoneda(), tipoProductoId);

				// Si la fecha de la actualizaci{on del precio del título no es del día no se puede recomprar
				if ((precioRecompra.getFecha_act()!=null && precioRecompra.getFecha_act().compareTo(new Date())!=0)
						|| !precioRecompraDAO.esAprobadoPrecioRecompraTitulo(precioRecompra) ){
					precioRecompra.setTitulo_precio_recompra(new BigDecimal(0));
					precioRecompra.setTasaPool(new BigDecimal(0));
				}

				beanTitulo.setPorcentajeRecompra(precioRecompra.getTitulo_precio_recompra());
				beanTitulo.setPrecioMercado(precioRecompra.getTasaPool());
			}

			// Verifica si no se pacto recompra para el título, no se almacena precio de recompra
			if (parametrosEntrada.get("recompraTitulos") != null) {
				guardarPrecioRecompraTitulo(recompraTitulos, beanTitulo.getIdTitulo(), beanTitulo);
				// SOLO TITULOS EN MONEDA LOCAL
				// Verificar si la unidad de inversion recompra con Neteo y la moneda del titulo es local (Bolivares)
				if (recompraConNeteoUI == 1 && beanTitulo.getMonedaNegociacion().equalsIgnoreCase(idMoneda)) {
					// Monto por concepto de neteo
					montoNeteo = valorEquivMonedaUI.multiply(beanTitulo.getPorcentajeRecompra());
					montoNeteo = montoNeteo.divide(CIEN_BD, 4, BigDecimal.ROUND_HALF_UP);
					// si la moneda de la UI no es la Local, multiplicar por tasa de cambio
					if (!esMonedaLocalUI)
						montoNeteo = montoNeteo.multiply(tasaCambio);
				}
			}

			// Asignar monto por neteo al titulo
			beanTitulo.setMontoNeteo(montoNeteo);

			// Sumar monto total por neteo
			montoTotalNeteo = montoTotalNeteo.add(montoNeteo);
			// Se Calcula el Precio Sucio
			if (indicaPrecioSucioUI == 0) {// si no indica precio sucio la UI calcular los intereses caidos
				logger.info("Calculando intereses caidos...");
				// Calcula los interes caídos por cada uno de los títulos
				BigDecimal auxIntereses = new BigDecimal(0);
				if (parametrosEntrada.get("fechaRecompra")!=null){
					fechaValor=parametrosEntrada.get("fechaRecompra").toString();
				}
				else{
					fechaValor = parametrosEntrada.get("fechaValor").toString();
				}

				auxIntereses = Utilitario.calcularInteresesCaidos(valorEquivMonedaUI, beanTitulo.getIdTitulo(), fechaValor, nombreDataSource, dso, beanTitulo);
				if (!esMonedaLocalUI)// si la moneda UI NO es local, multiplicar por tasa
					auxIntereses = auxIntereses.multiply(tasaCambio);
				// Monto Intereses Caidos Bs.f
				beanTitulo.setMontoIntCaidos(auxIntereses);
				montoInteresCaidos = montoInteresCaidos.add(beanTitulo.getMontoIntCaidos());

				// QUITAR:
				// beanTOSimulada.setPorsentajeRecompra(beanTitulo.getPorcentajeRecompra());

			}

			// beanTOSimulada.setMontoInversion(beanTOSimulada.getMontoInversion().add(montoInteresCaidos));
			beanTOSimulada.setMontoInversion(montoInversion.add(montoInteresCaidos));

			// Si NO es cartera propia
			if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

				BigDecimal montoTitulo = montoInversion.multiply(beanTitulo.getPorcentaje());
				montoTitulo = montoTitulo.divide(CIEN_BD, 4, BigDecimal.ROUND_HALF_UP);

				// restar Monto de Neteo en caso de que sea mayor a 0 para las operaciones de capital
				if (parametrosEntrada.get("calculador") == null) {// si no viene del calculador
					montoTitulo = montoTitulo.subtract(montoNeteo);
				}

				if (beanTitulo.getIndicadorConIDB() == 1) {// Titulo Con IDB
					montoCapitalConIDB = montoCapitalConIDB.add(montoTitulo);
					montoCapitalConIDB = montoCapitalConIDB.add(beanTitulo.getMontoIntCaidos());
				} else {// Titulo sin IDB
					montoCapitalSinIDB = montoCapitalSinIDB.add(montoTitulo);
					montoCapitalSinIDB = montoCapitalSinIDB.add(beanTitulo.getMontoIntCaidos());
				}
			}
		}// FIN RECORRIDO TITULOS for (int i=0; i<listaTitulos.size(); i++)

		// Seteando Total de Intereses caidos:
		beanTOSimulada.setMontoInteresCaidosRecompra(montoInteresCaidos.setScale(2, BigDecimal.ROUND_HALF_UP));

		// Transacciones:
		String transaccionFinanciera = uiBlotterRangos.getTipoTransaccionFinanciera();
		// Si NO es cartera propia
		if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

			if (montoCapitalConIDB.doubleValue() > 0) {
				transaccionFija = obtenerTransaccionFija(com.bdv.infi.logic.interfaces.TransaccionFija.CAPITAL_CON_IDB, beanTOSimulada.getVehiculoTomador(), String.valueOf(objResultadoSimular[18]));
				crearOperacionCapitalParaTitulo(transaccionFija, montoCapitalConIDB, transaccionFinanciera);
			}

			if (montoCapitalSinIDB.doubleValue() > 0) {
				transaccionFija = obtenerTransaccionFija(com.bdv.infi.logic.interfaces.TransaccionFija.CAPITAL_SIN_IDB, beanTOSimulada.getVehiculoTomador(), String.valueOf(objResultadoSimular[18]));
				crearOperacionCapitalParaTitulo(transaccionFija, montoCapitalSinIDB, transaccionFinanciera);
			}
		}// fin de Si NO es cartera propia

		beanTOSimulada.setListaOrdenTitulo(listaTitulos);

		if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

			// 7.- Buscar la siguiente informacion utilizando metodos
			// 7.1.- Comisiones a aplicar
			// 7.2.- Intereses caidos
			try {	
			//Modificado Requerimiento TTS_443 SICAD 2 NM26659_28/05/2014
			//Modificacion para adaptar al tipo de manejo de instrumento (Mixto o unico) en las diferentes tomas de ordenes
				bolTransaccion.aplicarTransacciones(beanTOSimulada, beanCliente.getTipoPersona(), idMoneda, transaccionFinanciera,manejoProductoInstFin, false, new Double(0));
			} catch (Exception e) {
				throw new Exception(e);
			}
		}

		// 8.- Calcular Comisiones
		// OrdenOperacion beanOperacion;
		listaOperacion = beanTOSimulada.getListaOperaciones();
		OrdenOperacion beanOperacion;
		for (int i = 0; i < listaOperacion.size(); i++) {
			beanOperacion = (OrdenOperacion) listaOperacion.get(i);
			beanOperacion.setFechaAplicar(hoy.getTime());
			beanOperacion.setFechaProcesada(hoy.getTime());
			beanOperacion.setNumeroCuenta((String) parametrosEntrada.get("numeroCuentaCliente"));
			// si la operacion es miscelanea no se ejecutan contra altair y mandamos el esatus en aplicada para ser usado en el proceso de adjudicacion
			if (transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO) || transaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO_VERIFICABLE)) {
				beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
			} else {
				beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			}
			beanOperacion.setIdMoneda(idMoneda);
			beanOperacion.setSiglasMoneda(idMoneda);

			if (logger.isDebugEnabled()) {
				logger.debug("Monto: " + beanOperacion.getMontoOperacion());
			}

			if (beanOperacion.getInComision() == 1) {
				// buscar si existe una comisión especificada por el usuario
				if (parametrosEntrada.get("comisionesOrden") != null) {
					ArrayList comisionesOrden = (ArrayList) parametrosEntrada.get("comisionesOrden");
					buscarNuevaComision(comisionesOrden, beanOperacion);
				}
				// calculos
				montoComisiones = montoComisiones.add(beanOperacion.getMontoOperacion());
				// crear data extendida para guardar los ID_COMISION de la unidad de inversion
				// por cada comision que se cobrara
				OrdenDataExt ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.ID_COMISION_UI);
				ordenDataExt.setValor(beanOperacion.getIdOperacion() + ";" + beanOperacion.getTasa());
				if (logger.isDebugEnabled()) {
					logger.debug("VALOR ID COMISION: " + ordenDataExt.getValor());
				}
				beanTOSimulada.agregarOrdenDataExt(ordenDataExt);

			} else {
				beanOperacion.setMontoOperacion(beanOperacion.getMontoOperacion());
				// Solo aplica el financiamiento a las operaciones que son de capital
				if (indicadorFinanciada && pctFinanciado.doubleValue() > 0) {
					BigDecimal montoFinanciado = beanOperacion.getMontoOperacion().multiply(pctFinanciado).divide(CIEN_BD, 4, BigDecimal.ROUND_HALF_UP);
					beanOperacion.setMontoOperacion(beanOperacion.getMontoOperacion().subtract(montoFinanciado));
					montoTotalFinanciado = montoTotalFinanciado.add(montoFinanciado);
				}
				// sumar monto de operacion al Total.
				// Al monto de la operacíon ya le fue sumado el monto de intereses caidos
				montoTotal = montoTotal.add(beanOperacion.getMontoOperacion());
			}

		}

		// Si NO es cartera propia obtener el monto total de la Suma de las operaciones que incluyen al operación de capital
		if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {
			montoTotal = montoTotal.add(montoComisiones);
		} else {// Si es cartera propia no se toman en cuenta las operaciones
			montoTotal = montoInversion;
		}

		if (parametrosEntrada.get("calculador") != null) {// si viene del calculador
			montoTotal = montoTotal.subtract(montoTotalNeteo);// restar monto de neteo por recompra
		}

		// setear Monto total a pagar sin financiamiento
		beanTOSimulada.setMontoTotalPagar(montoTotal);

		// Verificar si es para guardar
		// Sumar Financiamiento para el TOTAL de toda la orden
		if (parametrosEntrada.get("calculador") == null) {// si no viene del calculador
			montoTotal = montoTotal.add(montoTotalFinanciado); // Monto total de la orden
		}

		beanTOSimulada.setMontoInversion(montoInversion);
		beanTOSimulada.setMontoTotal(montoTotal);
		beanTOSimulada.setMontoInteresCaidos(montoInteresCaidos);
		beanTOSimulada.setMontoComisiones(montoComisiones);
		beanTOSimulada.setMontoFinanciado(montoTotalFinanciado);
		beanTOSimulada.setTasaCambio(tasaPropuesta);
		beanTOSimulada.setTasaPool(tasaPropuesta);
	
		// tipo de mercado asociado a la
		String tipoMercado = (String) objResultadoSimular[8];

		// guardar tasa Pool y comision de Emisor para tipo inventario
		if (indicaInventario) {
			beanTOSimulada.setComisionEmisor(comisionEmisor);
			beanTOSimulada.setTasaPool((BigDecimal) objResultadoSimular[6]);

			// calculos de acuerdo al tipo de mercado
			calculosMercado(tipoMercado, uiBlotterRangos);

		}
		// Setear indicador de inventario: true o false
		beanTOSimulada.setInventario(indicaInventario);

		// Setea los campos dinámicos
		beanTOSimulada.setListaCamposDinamicos((ArrayList<CampoDinamico>) parametrosEntrada.get("camposDinamicos"));

		// ---------------CREAR OBJETOS PARA VALORES DE DATA EXTENDIDA EN LA ORDEN--------------------------------
		OrdenDataExt ordenDataExt;
		// --Data Extendida para porcentaje de financiamiento en la orden----------
		// --en caso de permitir financiamiento y dicho porcentaje sea mayor que cero
		if (indicadorFinanciada && pctFinanciado.doubleValue() > 0) {

			ordenDataExt = new OrdenDataExt();
			ordenDataExt.setIdData(DataExtendida.PCT_FINANCIAMIENTO);
			ordenDataExt.setValor(String.valueOf(pctFinanciado));
			beanTOSimulada.agregarOrdenDataExt(ordenDataExt);

			ordenDataExt = new OrdenDataExt();
			ordenDataExt.setIdData(DataExtendida.MTO_FINANCIAMIENTO);
			ordenDataExt.setValor(String.valueOf(montoTotalFinanciado));
			beanTOSimulada.agregarOrdenDataExt(ordenDataExt);

		}

		// Si NO es cartera propia
		if (!((String) parametrosEntrada.get("tipoTransaccionNegocio")).equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) {

			// --Data extendida para la datos del conyuge del cliente asociado a la orden
			// --en caso de ser casado
			if (parametrosEntrada.get("estadoCasado") != null && parametrosEntrada.get("estadoCasado").toString().equalsIgnoreCase("SI")) {

				ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.CED_CONYUGE);
				ordenDataExt.setValor((String) parametrosEntrada.get("tipoPersonaConyuge") + (String) parametrosEntrada.get("cedulaConyuge"));
				beanTOSimulada.agregarOrdenDataExt(ordenDataExt);
			}
			// ---------------------------------------------------------------------------------------------------------

		}

		// Asignar Numero de Oficina o Sucursal
		if (parametrosEntrada.get("numeroOficina") != null) {
			beanTOSimulada.setNumeroOficina((String) parametrosEntrada.get("numeroOficina"));
		}

		// Asignar tipo de Producto del Instrumento en la Orden-------------------------
		if (tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)) {
			beanTOSimulada.setTipoProductoId(obtenerTipoProducto(beanTOSimulada.getInstrumentoId()));
		} else {
			beanTOSimulada.setTipoProductoId(obtenerTipoProducto(beanTOSimulada.getInstrumentoId()));
		}
		// -----------------------------------------------------------------------------
		
		beanTOSimulada.setMonedaId(idMonedaUI);
		
		return beanTOSimulada;
	}// FIN DE METODO simuladorTO() . . . . . .

	/**
	 * Crea una operacion de capital por el monto correspondiente al titulo
	 * 
	 * @param transaccionFija
	 * @param beanTitulo
	 * @param montoTitulo
	 * @throws Exception
	 */
	private void crearOperacionCapitalParaTitulo(TransaccionFija transaccionFija, BigDecimal montoTitulo, String transaccionFinanciera) throws Exception {
		if (transaccionFija == null) {
			throw new Exception("No se ha definido una transacci&oacute;n de capital para el veh&iacute;culo tomador.");
		}

		OrdenOperacion beanOperacion = new OrdenOperacion();
		beanOperacion.setMontoOperacion(montoTitulo);
		beanOperacion.setTasa(new BigDecimal(100));
		// Operacion de Capital
		beanOperacion.setInComision(0);
		beanOperacion.setNombreOperacion(transaccionFija.getNombreTransaccion());
		beanOperacion.setTipoTransaccionFinanc(transaccionFinanciera);
		beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);

		if (transaccionFinanciera.equals(TransaccionFinanciera.DEBITO)) {

			if (transaccionFija.getCodigoOperacionCteDeb() == null || transaccionFija.getCodigoOperacionCteDeb().equals("")) {
				throw new Exception("No se ha definido el c&oacute;digo de operaci&oacute;n de d&eacute;bito asociado al veh&iacute;culo tomador de la orden.");
			}

			beanOperacion.setCodigoOperacion(transaccionFija.getCodigoOperacionCteDeb());

		} else if (transaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)) {

			if (transaccionFija.getCodigoOperacionCteBlo() == null || transaccionFija.getCodigoOperacionCteBlo().equals("")) {
				throw new Exception("No se ha definido el c&oacute;digo de operaci&oacute;n de bloqueo asociado al veh&iacute;culo tomador de la orden.");
			}

			beanOperacion.setCodigoOperacion(transaccionFija.getCodigoOperacionCteBlo());
		}// **SI ES MISCELANEO NO LLEVA CODIGO DE OPERACION Ya QUE NO SE ENVIAN A ALTAIR**

		beanOperacion.setIdTransaccionFinanciera(String.valueOf(transaccionFija.getIdTransaccion()));
		listaOperacion.add(beanOperacion);
	}

	/**
	 * Busca la comisi&oacute;n especificada por el usuario especial, la cual se envi&oacute; como parámetro
	 * 
	 * @param comisionesOrden
	 * @param beanOperacion
	 */
	private void buscarNuevaComision(ArrayList comisionesOrden, OrdenOperacion beanOperacion) {
		// si existen comisiones especificadas por el usuario

		if (!comisionesOrden.isEmpty()) {
			for (int i = 0; i < comisionesOrden.size(); i++) {
				OrdenOperacion ordenOperComision = (OrdenOperacion) comisionesOrden.get(i);

				if (logger.isDebugEnabled()) {
					logger.debug("comisionesOrden: " + ordenOperComision.getInComision());
					logger.debug("beanOperacion.getIdOperacion(): " + beanOperacion.getIdOperacion());
					logger.debug("ordenOperComision.getIdOperacion(): " + ordenOperComision.getIdOperacion());
					logger.debug("ordenOperComision.getTasa(): " + ordenOperComision.getTasa());
					logger.debug("beanOperacion.getTasa(): " + beanOperacion.getTasa());
				}

				if (beanOperacion.getIdOperacion() == (ordenOperComision.getIdOperacion())) {
					// verificar si la comisión ha cambiado con respecto a la original
					if (ordenOperComision.getTasa().compareTo(beanOperacion.getTasa()) != 0) {
						beanOperacion.setIndicadorComisionInvariable(0);
						logger.info("COMISION " + beanOperacion.getIdOperacion() + " VARIO DE " + beanOperacion.getTasa() + " A " + ordenOperComision.getTasa());
					}
					// si encuentra una comisión especificada por el usuario
					// en el array de comisiones editadas, entonces se asigna esta nueva comisión
					// a la operación, sino queda la comisión original de la transaccion financiera
					if (beanOperacion.getTasa().doubleValue() > 0) {
						// recalcular solo si la tasa original es mayor que cero, sino es una comision de monto fijo y no se debe multiplicar por la tasa
						if (logger.isDebugEnabled()) {
							logger.debug("RECALCULANDO COMISION..");
						}
						recalcularComision(beanOperacion, ordenOperComision.getTasa());
					}
					break;
				}

			}
		}
	}

	/**
	 * Recalcula el monto de la operación dada una nueva tasa
	 * 
	 * @param beanOperacion
	 * @param tasa
	 */
	private void recalcularComision(OrdenOperacion beanOperacion, BigDecimal tasa) {
		// Modificacion V.R. 23-09-2010
		BigDecimal montoComision = (beanTOSimulada.getMontoInversion().multiply(tasa)).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
		beanOperacion.setMontoOperacion(montoComision.setScale(2, BigDecimal.ROUND_HALF_UP));
		beanOperacion.setTasa(tasa);
		if (logger.isDebugEnabled()) {
			logger.debug("montoInversion(O): " + montoInversion);
			logger.debug("tasa: " + tasa);
			logger.debug("montoComision: " + montoComision);
		}
	}

	/**
	 * Realiza los c&aacute;lculos de acuerdo al tipo de mercado
	 * 
	 * @param tipoMercado
	 * @param uiBlotterRangos
	 * @throws Exception
	 */
	private void calculosMercado(String tipoMercado, UIBlotterRangos uiBlotterRangos) throws Exception {
		long t1 = System.currentTimeMillis();
		BigDecimal comisionOficina = new BigDecimal(0);
		BigDecimal comisionOperacion = new BigDecimal(0);
		BigDecimal gananciaRed = new BigDecimal(0);
		BigDecimal precioCompra = new BigDecimal(0);

		// si es mercado primario
		if (tipoMercado.equals(ConstantesGenerales.MERCADO_PRIMARIO)) {

			UnidadInversionDAO boUI = new UnidadInversionDAO(nombreDataSource, dso);
			Object[] resultado = boUI.listarValoresUiBlotter(uiBlotterRangos.getIdUnidadInversion(), uiBlotterRangos.getIdBlotter());
			if (resultado != null) {
				gananciaRed = new BigDecimal((Double) resultado[0]);
			}
			comisionOficina = uiBlotterRangos.getComisionEmisor().multiply(gananciaRed);
			comisionOficina = comisionOficina.divide(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_HALF_UP);
		} else {// si es mercado secundario
			precioCompra = (BigDecimal) parametrosEntrada.get("precioOfrecido");
			gananciaRed = beanTOSimulada.getTasaPool().subtract(precioCompra);

		}

		beanTOSimulada.setGananciaRed(gananciaRed);
		beanTOSimulada.setComisionOficina(comisionOficina);
		beanTOSimulada.setComisionOperacion(comisionOperacion);

		// calcular la comisión de Operación
		comisionOperacion = calcularComisionOperacion(tipoMercado);

		beanTOSimulada.setComisionOperacion(comisionOperacion);
		logger.info(" Tiempo de procesamiento de calculosMercado " + (System.currentTimeMillis() - t1) + " miliseg");
	}

	/**
	 * Calcula la Comision Total de Operación para la Orden. El c&aacute;lculo se realiza por t&iacute;tulo y se va acumulando 
	 * para el caso en que sea m&aacute;s de un t&iacute;tulo para el instrumento financiero 
	 * @param comisionOficina comision de oficina calculada
	 * @return la comisión de Operación
	 * @throws Exception
	 */
	private BigDecimal calcularComisionOperacion(String tipoMercado) throws Exception {
		long tcc = System.currentTimeMillis();
		ArrayList listaTitulos = beanTOSimulada.getListaOrdenTitulo();
		BigDecimal comisionOperTitulo = new BigDecimal(0);
		BigDecimal comisionTotalOperacion = new BigDecimal(0);
		BigDecimal plazoTitulo;

		for (int i = 0; i < listaTitulos.size(); i++) {
			plazoTitulo = new BigDecimal(0);
			OrdenTitulo beanTitulo = new OrdenTitulo();
			beanTitulo = (OrdenTitulo) listaTitulos.get(i);

			if (tipoMercado.equals(ConstantesGenerales.MERCADO_PRIMARIO)) {
				// ------validaciones de datos necesarios para calculos de operacion por UI de inventario con precio sucio
				if (beanTitulo.getFechaEmision() == null) {
					throw new Exception("No se puede completar el proceso ya que el t&iacute;tulo " + beanTitulo.getIdTitulo() + " no posee fecha de emisi&oacute;n.");
				}
				if (beanTitulo.getFechaVencimiento() == null) {
					throw new Exception("No se puede completar el proceso ya que el t&iacute;tulo " + beanTitulo.getIdTitulo() + " no posee fecha de vencimiento.");
				}
				if (beanTitulo.getBasis() == null) {
					throw new Exception("No se puede completar el proceso ya que el t&iacute;tulo " + beanTitulo.getIdTitulo() + " no posee un base.");
				}
				// -------------------

				String fechaEmision = beanTitulo.getFechaEmision().getYear() + 1900 + "-" + (beanTitulo.getFechaEmision().getMonth() + 1) + "-" + beanTitulo.getFechaEmision().getDate();
				String fechaVcto = beanTitulo.getFechaVencimiento().getYear() + 1900 + "-" + (beanTitulo.getFechaVencimiento().getMonth() + 1) + "-" + beanTitulo.getFechaVencimiento().getDate();

				plazoTitulo = Utilitario.cuponesDiferenciaBaseDias(fechaEmision, fechaVcto, beanTitulo.getBasis(), 1);

				comisionOperTitulo = beanTitulo.getValorInvertido().multiply(beanTOSimulada.getComisionOficina()).multiply(plazoTitulo);
				Double comisionOperacion = comisionOperTitulo.doubleValue() / com.bdv.infi.util.Utilitario.diasBaseCalculo(beanTitulo.getBasis());
				comisionOperTitulo = new BigDecimal(comisionOperacion).setScale(2, BigDecimal.ROUND_HALF_UP);

			} else {
				comisionOperTitulo = (beanTitulo.getValorInvertido().multiply(beanTOSimulada.getGananciaRed())).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
			}

			comisionTotalOperacion = comisionTotalOperacion.add(comisionOperTitulo);

		}
		logger.info(" Tiempo de procesamiento de calcularComision " + (System.currentTimeMillis() - tcc) + " miliseg");
		return comisionTotalOperacion;
	}

	/**
	 * Verifica que el T&iacute;tulo exista en la Lista de T&iacute;tulos con recompra y almacena el precio de recompra indicado y la tasa pool correspondiente, de lo contrario almacena cero(0)
	 * 
	 * @param array
	 * @param idTitulo
	 * @param beanTitulo
	 */
	public void guardarPrecioRecompraTitulo(ArrayList array, String idTitulo, OrdenTitulo beanTitulo) {

		BigDecimal precioRecompraTitulo = new BigDecimal(0);
		BigDecimal tasaPoolTitulo = new BigDecimal(0);

		if (!array.isEmpty()) {
			for (Iterator iter = array.iterator(); iter.hasNext();) {
				PrecioRecompra precioRecompra = (PrecioRecompra) iter.next();
				if (precioRecompra.getTituloId().equals(idTitulo)) {

					if (precioRecompra.getTitulo_precio_recompra() != null) {
						precioRecompraTitulo = precioRecompra.getTitulo_precio_recompra();
					}

					if (precioRecompra.getTasaPool() != null) {
						tasaPoolTitulo = precioRecompra.getTasaPool();
					}

					break;
				}
			}

			// Setenado Fecha de Recompra en la Orden:
			beanTOSimulada.setFechaValorRecompra((String) parametrosEntrada.get("fechaRecompra"));
		}

		beanTitulo.setPorcentajeRecompra(precioRecompraTitulo);
		beanTitulo.setPrecioMercado(tasaPoolTitulo);
	}

	/**
	 * Valida que el cliente pueda comprar sitme
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	private void validarReglasSitme() throws Exception {
		long montoDiario = 0;
		long montoMensual = 0;
		long montoAnual = 0;

		ReglaValidacionDAO rvDao = new ReglaValidacionDAO(this.dso);
		OrdenDAO ordenDao = new OrdenDAO(this.dso);
		DataSet dsMontos = null;
		DataSet dsReglas = null;

		if (parametrosEntrada.get("tipoPersona").equals("J") || parametrosEntrada.get("tipoPersona").equals("G")) {
			ordenDao.obtenerMontoSolicitudOrdenesSitme(beanCliente.getIdCliente(), idMonedaUI, null);
			rvDao.obtenerReglas(beanCliente.getTipoPersona(), rvDao.TIPO_ADQUISICION_DIVISAS);
			dsMontos = ordenDao.getDataSet();
			dsReglas = rvDao.getDataSet();
		} else {
			ordenDao.obtenerMontoSolicitudOrdenesSitme(beanCliente.getIdCliente(), idMonedaUI, (String) parametrosEntrada.get("concepto"));
			rvDao.obtenerReglasPorConcepto(beanCliente.getTipoPersona(), rvDao.TIPO_ADQUISICION_DIVISAS, (String) parametrosEntrada.get("concepto"));
			dsMontos = ordenDao.getDataSet();
			dsReglas = rvDao.getDataSet();
		}

		while (dsMontos.next()) {
			if (dsMontos.getValue("periocidad").equals(rvDao.PERIOCIDAD_DIARIA)) {
				montoDiario = Long.parseLong(dsMontos.getValue("monto"));
			} else if (dsMontos.getValue("periocidad").equals(rvDao.PERIOCIDAD_MENSUAL)) {
				montoMensual = Long.parseLong(dsMontos.getValue("monto"));
			} else {
				montoAnual = Long.parseLong(dsMontos.getValue("monto"));
			}
		}
		while (dsReglas.next()) {

			if (dsReglas.getValue("regla_periocidad").equals(rvDao.PERIOCIDAD_DIARIA)) {
				if (montoDiario + montoPedido.longValue() > Long.parseLong(dsReglas.getValue("regla_valor"))) {
					listaMensajes.add(dsReglas.getValue("regla_mensaje"));
				}
			} else if (dsReglas.getValue("regla_periocidad").equals(rvDao.PERIOCIDAD_MENSUAL)) {
				if (montoMensual + montoPedido.longValue() > Long.parseLong(dsReglas.getValue("regla_valor"))) {
					listaMensajes.add(dsReglas.getValue("regla_mensaje"));
				}
			} else if (dsReglas.getValue("regla_periocidad").equals(rvDao.PERIOCIDAD_ANUAL)) {
				if (montoAnual + montoPedido.longValue() > Long.parseLong(dsReglas.getValue("regla_valor"))) {
					listaMensajes.add(dsReglas.getValue("regla_mensaje"));
				}
			}
		}
	}

	/**
	 * Reinicia los valores de la orden
	 */
	public void reiniciarValores() {
		//cantidadPedida = 0;
		montoInversion = new BigDecimal(0);
		montoTotal = new BigDecimal(0);
		montoTotalFinanciado = new BigDecimal(0);
		montoComisiones = new BigDecimal(0);
		montoInteresCaidos = new BigDecimal(0);
	}

	/**
	 * Cache que rango de blotter
	 * 
	 * @param idUnidadInversion
	 *            id de la unidad de inversión
	 * @param idBlotter
	 *            id del blotter
	 * @param tipoPersona
	 *            tipo de persona
	 * @return Objeto UIBlotterRangos encontrado
	 * @throws Exception
	 *             en caso de no haber encontrado los rangos
	 */
	private UIBlotterRangos obtenerBlotterRangos(String idUnidadInversion, String idBlotter, String tipoPersona, String tipoOperacion) throws Exception {
		String llave = idUnidadInversion + idBlotter + tipoPersona;
		UIBlotterRangos claseuiBlotterRangos = null;
		if (mapUiBlotterRangos.containsKey(llave)) {
			logger.info("llave obtenerBlotterRangos encontrada " + llave);
			claseuiBlotterRangos = mapUiBlotterRangos.get(llave);
		} else {
			logger.info("llave obtenerBlotterRangos NO encontrada " + llave);
			UIBlotterRangosDAO uiBlotterRangosDAO = new UIBlotterRangosDAO(nombreDataSource, dso);
			claseuiBlotterRangos = uiBlotterRangosDAO.listarBlotterRangosObj(Long.parseLong(idUnidadInversion), idBlotter, tipoPersona,tipoOperacion);
			mapUiBlotterRangos.put(llave, claseuiBlotterRangos);
		}
		return claseuiBlotterRangos;
	}

	/**
	 * Busca en cache la transacción fija, si no la tiene la busca y la almacena
	 * 
	 * @param tipo
	 *            puede ser com.bdv.infi.logic.interfaces.TransaccionFija.CAPITAL_CON_IDB o com.bdv.infi.logic.interfaces.TransaccionFija.CAPITAL_SIN_IDB
	 * @throws Exception
	 *             en caso de error en la búsqueda de la transaccion fija
	 */
	private TransaccionFija obtenerTransaccionFija(int tipo, String vehiculoTomador, String idInstrumentoFinanciero) throws Exception {
		TransaccionFija claseTransaccionFija = null;
		String llave = String.valueOf(tipo) + vehiculoTomador + idInstrumentoFinanciero;
		if (mapTransaccionFija.containsKey(llave)) {
			logger.info("llave obtenerTransaccionFija encontrada");
			claseTransaccionFija = mapTransaccionFija.get(llave);
		} else {
			logger.info("llave obtenerTransaccionFija NO encontrada");
			claseTransaccionFija = trnfFijaDAO.obtenerTransaccion(tipo, vehiculoTomador, idInstrumentoFinanciero);
			mapTransaccionFija.put(llave, claseTransaccionFija);
		}
		return claseTransaccionFija;
	}

	/**
	 * Obtiene el tipo de producto. Hace uso del cache
	 * 
	 * @param idInstrumentoFinanciero
	 *            id del instrumento financiero
	 * @return tipo de producto
	 * @throws Exception
	 *             en caso de error
	 */
	private String obtenerTipoProducto(String idInstrumentoFinanciero) throws Exception {
		String tipoProducto = null;
		if (mapTipoProducto.containsKey(idInstrumentoFinanciero)) {
			logger.info("llave obtenerTipoProducto encontrada");
			tipoProducto = mapTipoProducto.get(idInstrumentoFinanciero);
		} else {
			logger.info("llave obtenerTipoProducto NO encontrada");
			InstrumentoFinancieroDAO instrumentoFinancieroDAO = new InstrumentoFinancieroDAO(dso);
			tipoProducto = instrumentoFinancieroDAO.obtenerIdTipoProducto(idInstrumentoFinanciero);
			mapTipoProducto.put(idInstrumentoFinanciero, tipoProducto);
		}
		return tipoProducto;
	}
}
