package models.ordenes.venta_titulos;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.sql.DataSource; //import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.TCCCFMT;
import com.bdv.infi.webservices.manager.ManejadorTasaCambio;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.logic.interfaz_varias.MensajeEstadistica;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.GenericoDAO;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.Custodia;
import com.bdv.infi.data.FechaValor;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.Moneda;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.ProcesoGestion;
import com.bdv.infi.data.TransaccionFija;
import com.bdv.infi_toma_orden.util.Utilitario;

import megasoft.*;
import models.security.userInfo.SecurityDBUserInfo;

/**
 * Clase encargada de realizar los calculos para la venta de un t&iacute;tulo en custodia por un cliente en particular.
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class CalculosVentaTitulo extends com.bdv.infi.logic.Transaccion {
	
	private String tipoInstruccionPago=null;
	private String cuentaNacionalAbono=null;
	String[] titulos_venta;
	long idCliente = 0;
	String idTitulo = "";
	String tipoProductoId = "";
	String tipoPersona = "";
	String idBlotter = "";
	String productoCliente ="";
	Cliente cliente = new Cliente();
	private DataSet mensajes = null;
	private DecimalFormat dFormato1 = new DecimalFormat("###,###,##0.000000");
	private DecimalFormat dFormatoSinDecim = new DecimalFormat("###,###,##0");
	private int numDecimales = 2;
	private boolean operacionCambio = false;
	// private String[] sqlsOrden = null;
	boolean usuarioEspecial = false;
	boolean usuarioIngresoInstruccionesPago = false;
	DataSet user_esp = null;
	private String mensajeError = "";
	private DataSet datos = new DataSet();
	private DataSet camposDin = new DataSet();

	private String nombreDataSource;
	DataSet datos_custodia = null;
	private Orden orden;
	private Moneda moneda_venta = new Moneda();
	private InstruccionesPago instruccionesPago = new InstruccionesPago();
	private OrdenTitulo ordenTitulo = new OrdenTitulo();
	private OrdenOperacion operacion = new OrdenOperacion();
	private BigDecimal cantidad_venta = new BigDecimal(0);
	private DataSet _datosCliente = new DataSet(); 
	private DataSet _cuentaCustodia = new DataSet(); 
	
	private String tipoInstSeleccionada;
	
	private int customerNumberBDV; //Número de cliente de la contraparte de BDV
	private HashMap<String, String> hashTituloBCV = new HashMap<String, String>();
	
	private DataSet _sectorProductivo = new DataSet();
	private DataSet _conceptos = new DataSet();
		
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {				
		
		// DataSet Para mensajes de Error
		mensajes = new DataSet();
		mensajes.append("error_busqueda_tasa_cambio", java.sql.Types.VARCHAR);
		mensajes.addNew();
		mensajes.setValue("error_busqueda_tasa_cambio", "<div style='display:none'>&nbsp;</div>");

		// DataSet para campos dinamicos
		camposDin.append("nombre_campo", java.sql.Types.VARCHAR);
		camposDin.append("valor_campo", java.sql.Types.VARCHAR);

		// DataSet para datos
		datos.append("display_pago", java.sql.Types.VARCHAR);
		datos.append("display_campos_din", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("display_pago", "display:none");
		datos.setValue("display_campos_din", "display:none");

		// DataSet de Conyugue
		DataSet _conyugue = new DataSet();
		_conyugue.append("tipo_persona_conyugue", java.sql.Types.VARCHAR);
		_conyugue.append("cedula_conyugue", java.sql.Types.VARCHAR);
		_conyugue.append("nombre_conyugue", java.sql.Types.VARCHAR);
		_conyugue.append("display", java.sql.Types.VARCHAR);
		_conyugue.addNew();
		_conyugue.setValue("tipo_persona_conyugue", _req.getSession().getAttribute("tipo_persona_conyugue") != null ? _req.getSession().getAttribute("tipo_persona_conyugue").toString() : "");
		_conyugue.setValue("nombre_conyugue", _req.getSession().getAttribute("nombre_conyugue") != null ? _req.getSession().getAttribute("nombre_conyugue").toString() : "");
		_conyugue.setValue("cedula_conyugue", _req.getSession().getAttribute("cedula_conyugue") != null ? _req.getSession().getAttribute("cedula_conyugue").toString() : "");
		_conyugue.setValue("display", _req.getSession().getAttribute("display") != null ? _req.getSession().getAttribute("display").toString() : "none");

		storeDataSet("conyugue", _conyugue);

		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		Vector<String> vec_sql_updates = new Vector<String>();
		DataSet datos_titulo = null;
		DataSet moneda_titulo = null;
		TitulosDAO titulosDAO = new TitulosDAO(_dso);		
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		VehiculoDAO vehiculoDAO=new VehiculoDAO(_dso);

		CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);
		FechaValorDAO fechaValorDAO = new FechaValorDAO(_dso);
		FechaValor objFechaValor = new FechaValor();
		ManejadorTasaCambio manejadorTasaCambio = new ManejadorTasaCambio(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		// --instruccion de pago
		instruccionesPago = new InstruccionesPago();
		instruccionesPago.setTasaCambio(new BigDecimal(100));
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		String columnaNumeroCuentaNac = "";
		String colspanFormaPago = "3";
										
		// Dataset para los datos y los calculos de los titulos vendidos
		DataSet ds_titulos_venta = new DataSet();
		ds_titulos_venta.append("titulo_descripcion", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("titulo_fe_ingreso_custodia", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("titcus_cantidad", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("bloqueada", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("moneda_neg_titulo", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("base_titulo", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("cantidad_venta", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("fecha_valor", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("moneda_descripcion", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("tasa_cambio_moneda_pago", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("titulos_precio_recompra", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("forma_pago_id", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("ctecta_numero", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("precio_compra", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("monto_intereses_caidos", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("monto_total_neto", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("monto_pago_capital", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("monto_comisiones", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("colspan_forma_pago", java.sql.Types.VARCHAR);
		ds_titulos_venta.append("columna_cuenta_nac", java.sql.Types.VARCHAR);

		// Buscar Bloter del usuario conectado
		SecurityDBUserInfo userInfo = new SecurityDBUserInfo(_dso);
		idBlotter = userInfo.getUserBlotterId(getUserName());

		// Crear objeto Orden para la venta de titulos
		// Crear objeto orden para registrar la transaccion posteriormente
		
		try {
			Logger.info(this, "---------------EMPEZANDO EL PROCESO DE CALCULOS PARA LA VENTA DE TITULOS------------------");
						
			orden = new Orden();

			orden.setIdTransaccion(TransaccionNegocio.VENTA_TITULOS);//FLAG 
			orden.setIdCliente(idCliente);
			orden.setStatus(StatusOrden.REGISTRADA);
			orden.setIdBloter(idBlotter);// blotter se obtiene por el usuario conectado
			orden.setNombreUsuario(getUserName());
			orden.setTerminal(_req.getRemoteAddr());


			// ------Buscar fecha valor------------------------------------------------------------------
			objFechaValor = fechaValorDAO.listar(tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)?com.bdv.infi.logic.interfaces.FechaValor.VENTA_TITULOS_SITME:com.bdv.infi.logic.interfaces.FechaValor.VENTA_TITULOS);
			Date fechaValor = objFechaValor.getFechaValor();

			// -------------buscar datos usuario-------------------------
			DataSource _dsoSeguridad = db.getDataSource(_app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));

			UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
			usuarioSegDAO.listar(getUserName(), null, null);
			usuarioSegDAO.getDataSet().next();
			int usuarioId = Integer.parseInt(usuarioSegDAO.getDataSet().getValue("msc_user_id"));

			// -----------------------------------------------------------

			// ----Obtener NUMERO DE OFICINA guardado en sesion
			String nroOficina = null;
			if (_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL) != null) {
				nroOficina = (String) _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);
			} else {
				mensajeError = "No se encuentra el n&uacute;mero de oficina";
				Logger.info(this, "NO SE ENCUENTRA EL NUMERO DE OFICINA EN LA SESION");
				throw new Exception("No se encuentra el n&uacute;mero de oficina");
			}
			// --------------------------------------------------------------------
			String vehiculoBDV=null;
			orden.setFechaOrden(new Date());
			orden.setFechaValor(fechaValor);
			orden.setMonto(0);
			orden.setInternoBDV(0);
			orden.setSucursal(nroOficina);
			orden.setTipoProducto(tipoProductoId);
			//System.out.println("PRODUCTO CLIENTE ------------> "  +  productoCliente);
			orden.setCuentaCliente(productoCliente);//TODO DEFINIR QUE VALOR SE COLOCARA EN LA CTA EN BOLIVARES
			
			vehiculoDAO.obtenerVehiculoBDV();
			if(vehiculoDAO.getDataSet().count()>0){
				vehiculoDAO.getDataSet().first();
				vehiculoDAO.getDataSet().next();
				vehiculoBDV=vehiculoDAO.getDataSet().getValue("VEHICU_ID");
				
			}
			
			if(vehiculoBDV!=null){
				orden.setVehiculoTomador(vehiculoBDV);
			}
			
			if((tipoInstruccionPago).equals(ConstantesGenerales.INSTRUCCION_ABONO_CTA_NACIONAL_DOLARES)){
				orden.setTipoCuentaAbono(Integer.parseInt(ConstantesGenerales.ABONO_CUENTA_NACIONAL));
			}else if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_SWIFT)) {
				orden.setTipoCuentaAbono(Integer.parseInt(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO));
			}

			// crear objeto proceso gestion para las instrucciones de pago
			ProcesoGestion procesoGestion = null;
			
			if (titulos_venta != null) {
			
				for (int i = 0; i < titulos_venta.length; i++) {
			
					TransaccionFija transaccionFija = new TransaccionFija();
					TransaccionFijaDAO trnfFijaDAO = new TransaccionFijaDAO(_dso);
					// ID del TITULO en Base de Datos
					idTitulo = _req.getParameter("titulo_id_" + titulos_venta[i]);

					// String moneda_titulo="";
					String forma_pago = "";
					String cuenta_numero = "";
					String descrip_moneda_venta = "";
					boolean monedaLocal = true;

					BigDecimal cambio_moneda_ext = new BigDecimal(1);
					cantidad_venta = new BigDecimal(0);
					BigDecimal cantidad_nueva_custodia = new BigDecimal(0);
					BigDecimal valor_nominal = new BigDecimal(1);
					BigDecimal porcentaje_recompra = new BigDecimal(0);
					BigDecimal precio_compra = new BigDecimal(0); // precio de compra del titulo
					BigDecimal monto_pago_capital = new BigDecimal(0); // basado en la cantidad de la venta y el precio de compra
					BigDecimal monto_intereses_caidos = new BigDecimal(0); // intereses caidos desde ek ultimo pago de cupon
					BigDecimal monto_total_neto = new BigDecimal(0); // monto total a pagar por el titulo, menos las comisiones, mas los intereses

					titulosDAO.detallesTitulo(idTitulo);
					datos_titulo = titulosDAO.getDataSet();

					monedaDAO = new MonedaDAO(_dso);
					moneda_venta = new Moneda();
					moneda_venta.setMonedaLocal(monedaLocal);
					
					if (datos_titulo.next()) {
					
						// Buscar los datos de la MONEDA de NEGOCIACION ASOCIADA AL T&iacute;TULO
						monedaDAO.listar(datos_titulo.getValue("titulo_moneda_neg"));
						moneda_titulo = monedaDAO.getDataSet();
						
						// control cambiario
						if (moneda_titulo.next()) {
							//Asignacion del tipo de moneda a la orden Venta Titulo
							orden.setIdMoneda(moneda_titulo.getValue("moneda_id"));
							
							// si la moneda de negociaci&oacute;n del titulo es extranjera
							// obtener el cambio correspondiente en caso de que el pago para la venta del titulo se haga en bol&iacute;vares
							if (moneda_titulo.getValue("moneda_in_local") != null && moneda_titulo.getValue("moneda_in_local").equals("0")) {
								monedaLocal = false;
								// Datos para moneda de negociacion seleccionada
						
								if (monedaDAO.listarPorId(_req.getParameter("moneda_id_" + titulos_venta[i]))) {
									// Objeto con todos los atributos de MONEDA SELECCIONADA PARA LA VENTA
									moneda_venta = (Moneda) monedaDAO.moveNext();
									descrip_moneda_venta = moneda_venta.getDescripcion();
									if (moneda_venta.isMonedaLocal()) {
										
										operacionCambio = true;// Operacion de cambio ya que la moneda de negociacion en titulo es extranjera y se vendera en bolivares

										if (monedaDAO.listarPorId(moneda_titulo.getValue("moneda_id"))) {
											Moneda monedaNegTitulo = (Moneda) monedaDAO.moveNext();

											try {
												// OPERACION DE CAMBIO
												// /OBTENER TASA DE CAMBIO DE LA MONEDA DE NEGOCIACION DEL TITULO		
												TCCCFMT TCMCCC1 = manejadorTasaCambio.getTasaCambio(monedaNegTitulo.getSiglas(), getUserName(), _req.getRemoteAddr());
												cambio_moneda_ext = TCMCCC1.getVenta();												

											} catch (Exception ex) {
												mensajeError = "Error buscando la tasa de cambio de la moneda " + monedaNegTitulo.getSiglas();
												Logger.error(this, ex.getMessage(),ex);
												throw new Exception("Error buscando la tasa de cambio de la moneda " + monedaNegTitulo.getSiglas());

											}

										}
									}
								}
							} else {// si la moneda de negociaci&oacute;n del titulo es local (bolívares)
								
								descrip_moneda_venta = moneda_titulo.getValue("moneda_descripcion");
							}
						}
						//						

						// precio de recompra
						if (_req.getParameter("titulos_precio_recompra_" + titulos_venta[i]) != null) {
							porcentaje_recompra = new BigDecimal(_req.getParameter("titulos_precio_recompra_" + titulos_venta[i]));
						}

						// cantidad a vender
						if (_req.getParameter("cantidad_venta_" + titulos_venta[i]) != null && !_req.getParameter("cantidad_venta_" + titulos_venta[i]).trim().equals(""))
							cantidad_venta = new BigDecimal(_req.getParameter("cantidad_venta_" + titulos_venta[i]));

						// valor nominal del titulo
						if (datos_titulo.getValue("titulo_valor_nominal") != null)
							valor_nominal = new BigDecimal(datos_titulo.getValue("titulo_valor_nominal"));

						// actualizar cantidad en custodia
						datos_custodia.next();
						cantidad_nueva_custodia = new BigDecimal(datos_custodia.getValue("titcus_cantidad")).subtract(cantidad_venta);

						// calcular precio de compra del titulo
						precio_compra = cantidad_venta.multiply(porcentaje_recompra.divide(new BigDecimal(100)));

						// calcular pago capital
						monto_pago_capital = valor_nominal.multiply(precio_compra);

						// Formato de fecha
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

						BigDecimal auxIntereses = new BigDecimal(0);
						DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
						String reportDateValor = "";
						reportDateValor = df.format(fechaValor);
						auxIntereses = Utilitario.calcularInteresesCaidos(new BigDecimal(String.valueOf(cantidad_venta)), idTitulo, reportDateValor, nombreDataSource, _dso,null); //ITS-1534: Validaciones cuenta custodia y calculos venta título

						// El total del calculo generado es ahora intereses caidos
						monto_intereses_caidos = monto_intereses_caidos.add(auxIntereses);

						// calcular monto total neto a pagar, multiplicado por la tasa de cambio
						monto_total_neto = (monto_pago_capital).add(monto_intereses_caidos);

						monto_total_neto = monto_total_neto.multiply(cambio_moneda_ext);

						// Monto en la moneda de denominación del título. Se almacena aqui para enviar el monto neto a OPICS
						instruccionesPago.setMontoInstruccionNoConversion(monto_pago_capital.add(monto_intereses_caidos));

						// --Multiplicar montos a mostrar en pantalla por tasa de cambio
						monto_intereses_caidos = monto_intereses_caidos.multiply(cambio_moneda_ext);
						monto_pago_capital = monto_pago_capital.multiply(cambio_moneda_ext);
						precio_compra = precio_compra.multiply(cambio_moneda_ext);
						// --------------------------------------------------------------------------

						// actualizar monto del titulo en la orden
						
						//CONFIGURACION DE CAMPO ORDENE_PED_TOTAL
						orden.setMontoTotal((monto_total_neto.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)).doubleValue());
						
						//orden.setMonto((monto_total_neto.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)).doubleValue());						
						orden.setMonto(cantidad_venta.doubleValue());
						
						orden.setMontoAdjudicado(cantidad_venta.doubleValue());
						
						// ---buscar transaccion fija para venta de titulos
						transaccionFija = trnfFijaDAO.obtenerTransaccion(com.bdv.infi.logic.interfaces.TransaccionFija.VENTA_TITULOS);

						operacion = new OrdenOperacion();
						// /setear numero de cuenta a la operaci&oacute;n

						operacion.setFechaAplicar(fechaValor);// calcular fecha a aplicar la transaccion (fecha actual mas parametro dias laborales)
						operacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
						operacion.setIdMoneda(_req.getParameter("moneda_id_" + titulos_venta[i]));
						operacion.setIdTitulo(idTitulo);
						operacion.setTasa(new BigDecimal(100));
						// Operacion de Capital
						operacion.setInComision(0);
						operacion.setNombreOperacion(transaccionFija.getNombreTransaccion());
						operacion.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
						operacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
						operacion.setMontoOperacion(monto_total_neto.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN));
						operacion.setIdTransaccionFinanciera(String.valueOf(transaccionFija.getIdTransaccion()));
						if(transaccionFija.getCodigoOperacionFija()==null || transaccionFija.getCodigoOperacionFija().equals("") || transaccionFija.getCodigoOperacionFija().equals("0")){
							throw new Exception("Error en proceso de Calcula para Venta Título: No se ha encontrado el codigo de operacion para la operacion VENTA_TITULOS");
						}
						operacion.setCodigoOperacion(transaccionFija.getCodigoOperacionFija());

						// ---GUARDAR INSTRUCCIONES DE PAGO
						
						if (usuarioIngresoInstruccionesPago) {// si el usuario especial debe ingresar instrucciones de pago
							// ----DATOS DEL BENEFICIARIO---------------------------------------------------
						
							if (_req.getParameter("cedula_beneficiario") != null && !_req.getParameter("cedula_beneficiario").equals("")) {

								OrdenDataExt ordenDataExt = new OrdenDataExt();
								ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
								ordenDataExt.setValor(_req.getParameter("cedula_beneficiario"));
								//
								orden.agregarOrdenDataExt(ordenDataExt);
								instruccionesPago.setCedulaBeneficiario(_req.getParameter("cedula_beneficiario"));
						
							} else {// si no se indica beneficiario, es el mismo cliente
						
								OrdenDataExt ordenDataExt = new OrdenDataExt();
								ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
								ordenDataExt.setValor(String.valueOf(cliente.getRifCedula()));
								//
								orden.agregarOrdenDataExt(ordenDataExt);
								instruccionesPago.setCedulaBeneficiario(String.valueOf(cliente.getRifCedula()));

							}
							// NOMBRE BENEFICIARIO
							if (_req.getParameter("nombre_beneficiario") != null && !_req.getParameter("nombre_beneficiario").equals("")) {
								instruccionesPago.setNombrebeneficiario(_req.getParameter("nombre_beneficiario"));
								OrdenDataExt ordenDataExt = new OrdenDataExt();
								ordenDataExt.setIdData(DataExtendida.NOMBRE_BENEFICIARIO);
								ordenDataExt.setValor(_req.getParameter("nombre_beneficiario"));
								//
								orden.agregarOrdenDataExt(ordenDataExt);

							} else {// si no se indica beneficiario, es el mismo cliente

								instruccionesPago.setNombrebeneficiario(cliente.getNombre());
								OrdenDataExt ordenDataExt = new OrdenDataExt();
								ordenDataExt.setIdData(DataExtendida.NOMBRE_BENEFICIARIO);
								ordenDataExt.setValor(cliente.getNombre());
								//
								orden.agregarOrdenDataExt(ordenDataExt);

							}
						}
						// -----------------------------------------------------------------------------------------
						
						// ---DATOS DE LA OPERACION-----------------------------------------------------------
						if (!moneda_venta.isMonedaLocal()) {// si es una moneda extranjera
						
							String numeroCtaInternacional=null;
							if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_SWIFT)){
								numeroCtaInternacional = _req.getParameter("ctecta_numero");	
							}else if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_ABONO_CTA_NACIONAL_DOLARES)){
								numeroCtaInternacional = _req.getParameter("text_cta_nacional_dol");
							}
							//TODO Verificar si se debe eliminar o dejar la validacion del codigo IBAN
							// Si es una cuenta europea concatenar el numero de cuenta con indicador de IBAN
//							if (_req.getParameter("iban_cta_europea") != null && _req.getParameter("iban_cta_europea").equals("1")) {
//								numeroCtaInternacional = ConstantesGenerales.INDICADOR_IBAN + numeroCtaInternacional;
//							}

							//RESOLUCION INCIDENCIA ITS-1567 NM26659
							//orden.setCuentaCliente(numeroCtaInternacional);
							//System.out.println("NUMERO DE CUENTA EN DOLARES -------------> " + numeroCtaInternacional);
							operacion.setNumeroCuenta(numeroCtaInternacional);
							operacion.setCodigoABA(_req.getParameter("ctecta_bcocta_aba"));
							operacion.setCodigoBicBanco(_req.getParameter("ctecta_bcocta_bic"));

							operacion.setDireccionBanco(_req.getParameter("ctecta_bcocta_direccion"));
							if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_SWIFT)){
								operacion.setNombreBanco(_req.getParameter("ctecta_bcocta_bco"));	
							} else if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_ABONO_CTA_NACIONAL_DOLARES)) {
//								Busqueda de 
								vehiculoDAO=new VehiculoDAO(_dso);
								vehiculoDAO.obtenerVehiculoBDV();
								
								String nombreBDV=null;
								if(vehiculoDAO.getDataSet().count()>0){
									vehiculoDAO.getDataSet().first();
									vehiculoDAO.getDataSet().next();
									nombreBDV=vehiculoDAO.getDataSet().getValue("VEHICU_NOMBRE");
									
								}																																														
									operacion.setNombreBanco(nombreBDV);																	
							}
							
							operacion.setTelefonoBanco(_req.getParameter("ctecta_bcocta_telefono"));
							
							tipoInstSeleccionada=_req.getParameter("tipo_inst_int");
							//System.out.println("TIPO DE INSTRUCCION SELECCIONADA ------------------------->  " + tipoInstSeleccionada);
							//System.out.println("_req.getParameter(intermediarioHidden): --> " + _req.getParameter("intermediarioHidden"));
							if (_req.getParameter("intermediarioHidden") != null && _req.getParameter("intermediarioHidden").equals("true")) {
								operacion.setCodigoABAIntermediario(_req.getParameter("ctecta_bcoint_aba"));
								operacion.setCodigoBicBancoIntermediario(_req.getParameter("ctecta_bcoint_bic"));
								operacion.setCodigoSwiftBancoIntermediario(_req.getParameter("ctecta_bcoint_swift"));
								operacion.setDireccionBancoIntermediario(_req.getParameter("ctecta_bcoint_direccion"));
								operacion.setNombreBancoIntermediario(_req.getParameter("ctecta_bcoint_bco"));
								operacion.setTelefonoBancoIntermediario(_req.getParameter("ctecta_bcoint_telefono"));

								
								
								if (usuarioIngresoInstruccionesPago && tipoInstSeleccionada!=null && tipoInstSeleccionada!="3") {
									// --SETEAR VALORES PARA INSTRUCCION DE PAGO
									instruccionesPago.setCtectaBcointAba(_req.getParameter("ctecta_bcoint_aba"));
									instruccionesPago.setCtectaBcointBco(_req.getParameter("ctecta_bcoint_bco"));
									instruccionesPago.setCtectaBcointBic(_req.getParameter("ctecta_bcoint_bic"));
									instruccionesPago.setCtectaBcointDireccion(_req.getParameter("ctecta_bcoint_bco"));
									instruccionesPago.setCtectaBcointSwift(_req.getParameter("ctecta_bcoint_swift"));
									instruccionesPago.setCtectaBcointTelefono(_req.getParameter("ctecta_bcoint_telefono"));
									instruccionesPago.setObservacionCtectaBcoint(_req.getParameter("ctecta_bcoint_observacion"));
									
									instruccionesPago.setClienteCuentaNumero(numeroCtaInternacional);
									instruccionesPago.setCtectaBcoctaAba(_req.getParameter("ctecta_bcocta_aba"));
									instruccionesPago.setCtectaBcoctaBco(_req.getParameter("ctecta_bcocta_bco"));
									instruccionesPago.setCtectaBcoctaBic(_req.getParameter("ctecta_bcocta_bic"));
									instruccionesPago.setCtectaBcoctaDireccion(_req.getParameter("ctecta_bcocta_direccion"));
									instruccionesPago.setCtectaBcoctaSwift(_req.getParameter("cta_bco_bcoint"));
									instruccionesPago.setCtectaBcoctaTelefono(_req.getParameter("ctecta_bcocta_telefono"));
									instruccionesPago.setObservacionCtectaBcocta(_req.getParameter("ctecta_observacion"));
								}

							}

							
							if (usuarioIngresoInstruccionesPago) {
								// /SETEAR CAMPOS PARA LA INSTRUCCION DE PAGO													
								//if (_req.getParameter("tipo_inst_int") != null && _req.getParameter("tipo_inst_int").equals("1")) {// si es una transferencia a cuenta internacional
								if(tipoInstSeleccionada!=null  && tipoInstSeleccionada.equals("1")){// si es una transferencia a cuenta internacional
									
									instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_SWIFT);
									// Si la moneda es extranjera se agrega data extendida con tipo de instruccion
									OrdenDataExt ordenDataExt = new OrdenDataExt();
									ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
									ordenDataExt.setValor(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
									orden.agregarOrdenDataExt(ordenDataExt);
									// Colocar forma de Pago
									forma_pago = ConstantesGenerales.FORMA_PAGO_CTA_INTERN;
								//} else if(_req.getParameter("tipo_inst_int") != null && _req.getParameter("tipo_inst_int").equals("2")){// si es un cheque
								} else if(tipoInstSeleccionada!=null  && tipoInstSeleccionada.equals("2")){// si es un cheque
									instruccionesPago.setTipoInstruccionId(TipoInstruccion.CHEQUE);
									
									// Se agrega data extendida indicando el tipo de instruccion
									OrdenDataExt ordenDataExt = new OrdenDataExt();
									ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
									ordenDataExt.setValor(String.valueOf(TipoInstruccion.CHEQUE));
									orden.agregarOrdenDataExt(ordenDataExt);

									// Colocar Forma de pago
									forma_pago = ConstantesGenerales.FORMA_PAGO_CHEQUE;
								//} else if(_req.getParameter("tipo_inst_int") != null && _req.getParameter("tipo_inst_int").equals("3")){//Si es transferencia a una cuenta nacional en dolares
								}  else if(tipoInstSeleccionada!=null  && tipoInstSeleccionada.equals("3")){//Si es transferencia a una cuenta nacional en dolares
									
									// Colocar Forma de pago
									forma_pago = ConstantesGenerales.FORMA_PAGO_CTA_NACIONAL_DOLARES;
								}

							}

						} else {// si es en moneda nacional
							operacion.setNumeroCuenta(_req.getParameter("cta_nac_numero"));
							
							if (usuarioIngresoInstruccionesPago) {// si el usuario especial debe ingresar instrucciones de pago
								// /SETEAR CAMPOS PARA LA INSTRUCCION DE PAGO
								instruccionesPago.setClienteCuentaNumero(_req.getParameter("cta_nac_numero"));
								instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_NACIONAL);

								// Colocar Forma de Pago
								forma_pago = ConstantesGenerales.FORMA_PAGO_CTA_NAC;
								columnaNumeroCuentaNac = "<td>N&uacute;mero de Cuenta:</td><td>" + _req.getParameter("cta_nac_numero") + "</td>";
								colspanFormaPago = "1";

								if (operacionCambio) {
									// Si es operacion de cambio se grega a data extendida el tipo de instruccion
									OrdenDataExt ordenDataExt = new OrdenDataExt();
									ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
									ordenDataExt.setValor(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO));
									orden.agregarOrdenDataExt(ordenDataExt);

								} else {
									// Si es en moneda Local se grega a data extendida el tipo de instruccion
									OrdenDataExt ordenDataExt = new OrdenDataExt();
									ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
									ordenDataExt.setValor(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
									orden.agregarOrdenDataExt(ordenDataExt);
								}

							} else {// Si no se crean instrucciones de pago verificar si se debe crear data extendida para operacion de cambio
								// Generar data extendida cuando es una operacion de cambio
								if (operacionCambio) {
									// Si es operacion de cambio se grega a data extendida el tipo de instruccion
									OrdenDataExt ordenDataExt = new OrdenDataExt();
									ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
									ordenDataExt.setValor(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO));
									orden.agregarOrdenDataExt(ordenDataExt);
								}
							}
						}

						if (usuarioIngresoInstruccionesPago &&  (tipoInstSeleccionada!=null && (!tipoInstSeleccionada.equals("3")))) {
							// SETEAR VALORES A INSTRUCCION DE PAGO
							// id del proceso
							long idProceso = Long.parseLong(GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.INFI_TB_810_PROCESO_INST));
							instruccionesPago.setFechaAplicacion(new Date());
							instruccionesPago.setFechaOperacion(new Date());
							instruccionesPago.setFechaValor(fechaValor);
							instruccionesPago.setMonedaId(_req.getParameter("moneda_id_" + titulos_venta[i]));
							instruccionesPago.setProcesoId(idProceso);
							instruccionesPago.setInstruccionId(1);
							instruccionesPago.setMontoInstruccion(monto_total_neto.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN));
							instruccionesPago.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);

						}
						// ----------------------------------------------------------------

						// Agregar campos dinamicos a la orden de venta
						agregarCamposDinamicosOrden(orden);

						// setear tasa de cambio en la orden
						orden.setTasaCambio(cambio_moneda_ext.doubleValue());

						orden.agregarOperacion(operacion);
						
						orden.setPrecioCompra(porcentaje_recompra.doubleValue());
						// /////CREAR OBJETO DE ORDEN TITULO//////////////////////

						// Incorporar el t&iacute;tulo vendido a la orden
						ordenTitulo = new OrdenTitulo();
						ordenTitulo.setUnidades(cantidad_venta.intValue());
						ordenTitulo.setTituloId(idTitulo);
						
						//Modificacion de codigo para que en el campo TITULO_MONTO se guarde la misma informacion que en TITULO_UNIDADES
						//ordenTitulo.setMonto(monto_total_neto.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN).doubleValue());
						ordenTitulo.setMonto(cantidad_venta.intValue());
						
						ordenTitulo.setMontoIntCaidos(monto_intereses_caidos.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN));
						ordenTitulo.setPorcentajeRecompra(porcentaje_recompra.doubleValue());
						ordenTitulo.setPrecioMercado(porcentaje_recompra.doubleValue());
						ordenTitulo.setMontoNeteo(new BigDecimal(0));
						//Configuracion de porcentaje en 100 dado que el proceso de venta titulo solo toma un titulo a la vez 
						ordenTitulo.setPorcentaje(new String("100"));

						orden.agregarOrdenTitulo(ordenTitulo);

						// Se agrega Data Extendida de la c3dula del conyugue
						if (_req.getSession().getAttribute("tipo_persona_conyugue") != null && !_req.getSession().getAttribute("tipo_persona_conyugue").equals("") && _req.getSession().getAttribute("cedula_conyugue") != null && !_req.getSession().getAttribute("cedula_conyugue").equals("")) {
							OrdenDataExt ordenDataExt = new OrdenDataExt();
							ordenDataExt.setIdData(DataExtendida.CED_CONYUGE);
							ordenDataExt.setValor(_req.getSession().getAttribute("tipo_persona_conyugue").toString().concat(_req.getSession().getAttribute("cedula_conyugue").toString()));
							orden.agregarOrdenDataExt(ordenDataExt);

							_req.getSession().removeAttribute("tipo_persona_conyugue");
							_req.getSession().removeAttribute("cedula_conyugue");
							_req.getSession().removeAttribute("nombre_conyugue");
							_req.getSession().removeAttribute("display");

						}

						// Crear objeto custodia con la cantidad en custodia del titulo que ser&aacute; actualizada una vez realizada la venta
						Custodia custodia = new Custodia();

						custodia.setIdCliente(idCliente);
						custodia.setIdTitulo(idTitulo);
						custodia.setTipoProductoId(tipoProductoId);
						custodia.setCantidad(cantidad_nueva_custodia.intValue());

						// agregar actualizaciones: cantidad en custodia del t&iacute;tulo y proceso de instruccion de pago
						vec_sql_updates.add(custodiaDAO.actualizarCantidadEnCustodia(custodia));

						// -----------Proceso de instruccion de pago---------------------------------------------

						if (usuarioIngresoInstruccionesPago) {// si el usuario especial debe ingresar instrucciones de pago
							procesoGestion = new ProcesoGestion();
							procesoGestion.setProcesoID(instruccionesPago.getProcesoId());
							procesoGestion.setClientId(idCliente);
							procesoGestion.setUsuarioId(usuarioId);
							procesoGestion.setFechaRegistro(new Date());
							// procesoGestion.setRecompra(true);
							procesoGestion.agregarInstruccion(instruccionesPago);
						}

						// llenar dataset con los datos del titulo
						ds_titulos_venta.addNew();
						ds_titulos_venta.setValue("titulo_descripcion", datos_titulo.getValue("titulo_id"));
						ds_titulos_venta.setValue("titulo_fe_ingreso_custodia", datos_custodia.getValue("titulo_fe_ingreso_custodia"));
						ds_titulos_venta.setValue("titcus_cantidad", dFormatoSinDecim.format(Double.parseDouble(datos_custodia.getValue("titcus_cantidad"))));
						ds_titulos_venta.setValue("bloqueada", dFormatoSinDecim.format(Double.parseDouble(datos_custodia.getValue("cantidad_bloqueada"))));
						ds_titulos_venta.setValue("moneda_neg_titulo", datos_titulo.getValue("titulo_moneda_neg"));
						ds_titulos_venta.setValue("base_titulo", datos_titulo.getValue("basis"));
						// datos de la venta del titulo
						ds_titulos_venta.setValue("cantidad_venta", dFormatoSinDecim.format(cantidad_venta));
						ds_titulos_venta.setValue("fecha_valor", sdf.format(fechaValor));
						ds_titulos_venta.setValue("moneda_descripcion", descrip_moneda_venta);
						ds_titulos_venta.setValue("tasa_cambio_moneda_pago", dFormato1.format(cambio_moneda_ext.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)));
						ds_titulos_venta.setValue("titulos_precio_recompra", dFormato1.format(porcentaje_recompra.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)));
						ds_titulos_venta.setValue("forma_pago_id", forma_pago);
						ds_titulos_venta.setValue("ctecta_numero", cuenta_numero);
						ds_titulos_venta.setValue("precio_compra", dFormato1.format(precio_compra.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)));
						ds_titulos_venta.setValue("monto_intereses_caidos", dFormato1.format(monto_intereses_caidos.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)));
						ds_titulos_venta.setValue("monto_total_neto", dFormato1.format(monto_total_neto.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)));
						ds_titulos_venta.setValue("monto_pago_capital", dFormato1.format(monto_pago_capital.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN)));
						ds_titulos_venta.setValue("colspan_forma_pago", colspanFormaPago);
						ds_titulos_venta.setValue("columna_cuenta_nac", columnaNumeroCuentaNac);

					}
										
					//----LLamada a generar mensajes para interfaces-----------------------------
					//LLamada para generar Mje a Interfaz con estadistico
					generarMensajeEstadistica();
					//LLamada a generar Mje a Interfaz con Carmen
					generarMensajeCarmen();
					//Llamada a generar Mje para Interfaz con Bvc
					generarMensajeBCV();
					//---------------------------------------------------------------------------
				}

				// si el usuario especial debe ingresar instrucciones de pago
				// por lo tanto se deben mostrar los datos del pago
				if (usuarioIngresoInstruccionesPago) {
					datos.setValue("display_pago", "display:block");
				}

			}

			// registrar los datasets exportados por este modelo
			storeDataSet("cliente", _datosCliente);// datos del cliente
			storeDataSet("ds_titulos_venta", ds_titulos_venta);// datos de la custodia del cliente sobre el titulo
			storeDataSet("mensajes", mensajes);// mensajes
			storeDataSet("datos", datos);// datos
			storeDataSet("campos_dinamicos", camposDin);

			// guardar objeto orden en sesion
			_req.getSession().setAttribute("orden_venta_titulos", orden);
			// guardar objeto procesoGestion en sesion para posteriormente setear los id de las operaciones asociadas a la orden
			_req.getSession().setAttribute("proceso_gestion", procesoGestion);
			// guardar vector de sentencias sql de actualizacion en sesion
			_req.getSession().setAttribute("vec_sql_updates", vec_sql_updates);
			// guardar en sesion parametro operacion de cambio
			_req.getSession().setAttribute("operacionCambio", operacionCambio);
			// Guarda la instruccion de pago
			_req.getSession().setAttribute("instruccionesPago", instruccionesPago);
			//Guarda informacion de tipo de cuenta a la que enviara el abono (Swift o Cuenta Nacional en Dolares) 
			_req.getSession().setAttribute("tipo_abono",tipoInstruccionPago);
			
			if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_SWIFT)){
				//Guarda informacion del ID de la instruccion de pago en caso de que el tipo de abono seleccionado sea Swift 
				_req.getSession().setAttribute("idInstruccionPago",_req.getParameter("text_instruc_swift"));
			}

			

			Logger.info(this, "---CALCULOS PARA LA VENTA DE LOS TITULOS REALIZADOS EXITOSAMENTE----");

		} catch (Exception e) {
			Logger.error(this, e.getMessage() + " " + Utilitario.stackTraceException(e));
			Logger.info(this, "ERROR REALIZANDO CALCULOS PARA LA VENTA DE TITULOS " + e.getMessage());
			throw new Exception("Error realizando los c&aacute;lculos para la venta de t&iacute;tulos. " + mensajeError);

		} finally {
			// cerrar conexiones de los objetos DAO en los cuales se utilizo un moveNext
			gestionPagoDAO.cerrarConexion();
			gestionPagoDAO.closeResources();
			custodiaDAO.closeResources();
			custodiaDAO.cerrarConexion();
			fechaValorDAO.cerrarConexion();
			fechaValorDAO.closeResources();
			monedaDAO.cerrarConexion();
			monedaDAO.closeResources();			

		}
	}

	/**
	 * Agrega los campos dinamicos a la orden de venta
	 * 
	 * @param orden
	 * @throws Exception
	 */
	private void agregarCamposDinamicosOrden(Orden orden) throws Exception {
		String[] camposDinamicos = _req.getParameterValues("campo_id");

		if (camposDinamicos != null) {
			for (int k = 0; k < camposDinamicos.length; k++) {
				if (_req.getParameter("valor_campo_" + camposDinamicos[k]) != null && !_req.getParameter("valor_campo_" + camposDinamicos[k]).trim().equals("")) {

					CampoDinamico campoDinamico = new CampoDinamico();
					campoDinamico.setIdCampo(Integer.parseInt(camposDinamicos[k]));
					campoDinamico.setCampoNombre(_req.getParameter("nombre_campo_" + camposDinamicos[k]));
					campoDinamico.setValor(_req.getParameter("valor_campo_" + camposDinamicos[k]));
					// agregar campo a la orden
					orden.agregarCampoDinamico(campoDinamico);

					// llenar dataset para mostrar campos dinamicos en pantalla de calculos
					camposDin.addNew();
					camposDin.setValue("nombre_campo", _req.getParameter("nombre_campo_" + camposDinamicos[k]));
					camposDin.setValue("valor_campo", _req.getParameter("valor_campo_" + camposDinamicos[k]));
				}
			}
		}

		if (orden.getCamposDinamicos().size() > 0) {
			// desplegar tabla de campos dinamicos
			datos.setValue("display_campos_din", "display:block");
		}

	}

	/**
	 * Verifica si el usuario conectado es un usuario especial colocando la variable de usuario especial en falso o verdadero según sea el caso
	 * 
	 * @throws Exception
	 */
	private void verificarUsuarioEspecial() throws Exception {

		UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO(_dso);

		// -------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource(_app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));

		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		String usuario = usuarioSegDAO.idUserSession(getUserName());
		// -----------------------------------------------------------

		userEspecialDAO.listar(usuario);
		user_esp = userEspecialDAO.getDataSet();

		if (user_esp.count() > 0) {
			usuarioEspecial = true;
			user_esp.next();

			if (user_esp.getValue("usresp_ingreso_instrucciones_pago") != null && user_esp.getValue("usresp_ingreso_instrucciones_pago").equals("1")) {
				//System.out.println("-----------------------> ES UN USUSARIO ESPECIAL <-----------------------");
				usuarioIngresoInstruccionesPago = true;
			}

			user_esp.first();

		} else {
			usuarioEspecial = false;
		}

	}
	
	/**
	 * Genera el mensaje para Estadistica
	 * @return mensajeEstadistica: objeto con el mensaje para estadistica
	 * @throws Exception 
	 */
	private void generarMensajeEstadistica() throws Exception{
				
		MensajeEstadistica mensajeEstadistica = null;
		//Generar Mensaje estadístico si es título SITME, moneda de Negociación Local y forma de Negociación Banco de eVzla
		if (orden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) && moneda_venta.isMonedaLocal() 
			&& _record.getValue("forma_negociacion")!= null && _record.getValue("forma_negociacion").equals("NBDV")){
			
			mensajeEstadistica = new MensajeEstadistica();
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);		
			MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(_dso);
			 			 
			 //Setear Valores de la Venta al Mensaje:
			  mensajeEstadistica.set(mensajeEstadistica.CEDULA_RIF_PASAPORTE , cliente.getTipoPersona()+String.valueOf(cliente.getRifCedula()));
			  mensajeEstadistica.set(mensajeEstadistica.FECHA_PROCESO , orden.getFechaOrden());
			  mensajeEstadistica.set(mensajeEstadistica.CONCEPTO_ESTADISTICO ,MensajeEstadistica.V_CONCEPTO_COMPRA);
			  mensajeEstadistica.set(mensajeEstadistica.CODIGO_DIVISA , MensajeEstadistica.V_CODIGO_DIVISA_DOLAR);
			  mensajeEstadistica.set(mensajeEstadistica.NOMBRE_RAZON_SOCIAL , cliente.getNombre());
			  mensajeEstadistica.set(mensajeEstadistica.MONTO_MONEDA_EXTRANJERA , instruccionesPago.getMontoInstruccionNoConversion().setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN).doubleValue());
			  mensajeEstadistica.set(mensajeEstadistica.HORA , com.bdv.infi.util.Utilitario.DateToString(orden.getFechaOrden(), "HHmm"));
			  mensajeEstadistica.set(mensajeEstadistica.FECHA_VALOR , orden.getFechaValor());
			  mensajeEstadistica.set(mensajeEstadistica.USUARIO , getUserName());
			  mensajeEstadistica.set(mensajeEstadistica.REFERENCIA_BANKTRADE , "BXXXX00001");// TODO PENDIENTE
			  mensajeEstadistica.set(mensajeEstadistica.NOMBRE_DEL_BENEFICIARIO, instruccionesPago.getNombrebeneficiario());
			  mensajeEstadistica.set(mensajeEstadistica.NUM_CUENTA_CLIENTE , mensajeOpicsDAO.convertirCuenta20A12(operacion.getNumeroCuenta()));
			  mensajeEstadistica.set(mensajeEstadistica.FIRMAS_AUTORIZADAS , getUserName());
			  mensajeEstadistica.set(mensajeEstadistica.CEDULA_RIF_BENEFICIARIO , cliente.getTipoPersona()+String.valueOf(cliente.getRifCedula()));
			  
			  ArrayList<OrdenTitulo> titulos = orden.getOrdenTitulo();
			  for (OrdenTitulo ordenTitulo : titulos) {
					 mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_BOLIVAR , ordenTitulo.getPorcentajeRecompra());
					 mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_DOLAR , "1");				
			  }
			  
			  if(cliente.getTipoPersona().equalsIgnoreCase("G")){
				  mensajeEstadistica.set(mensajeEstadistica.SECTOR_PUBLICO_PRIVADO, MensajeEstadistica.V_SECTOR_PUBLICO); 
			  }else{
				  mensajeEstadistica.set(mensajeEstadistica.SECTOR_PUBLICO_PRIVADO, MensajeEstadistica.V_SECTOR_PRIVADO);  
			  }
			  
			  mensajeEstadistica.setUsuarioNM(getUserName());
			  
			 //Establecer valores por Defecto al Mensaje:
			 mensajeDAO.estableceValoresPorDefecto(mensajeEstadistica);//
			 
			 if (mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA) != null && (mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA).equals("") ||  mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA).equals("0"))){
				  mensajeEstadistica.set(mensajeEstadistica.CODIGO_OFICINA, orden.getSucursal());
			 }
		}
		
		//Guardar objeto mensaje con toda la informacion en Sesion
		_req.getSession().setAttribute("mensaje_estadistica", mensajeEstadistica);
		
	}
	
	/**
	 * Genera el mensaje para Carmen
	 * @return mensajeCarmen: objeto con el mensaje para carmen
	 * @throws Exception 
	 */
	private void generarMensajeCarmen() throws Exception{
		
		MensajeCarmen mensajeCarmen = null;	
		
		//Verificar si el tipo de producto es SITME
		if (orden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);
			mensajeCarmen = new MensajeCarmen();
			
			//Datos de la cuenta custodia
			_cuentaCustodia.first();
			_cuentaCustodia.next();
			
			//Setear Valores al Mensaje para Interfaz Carmen			
			mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, ordenTitulo.getTituloId());
			mensajeCarmen.set(mensajeCarmen.CANTIDAD, cantidad_venta.intValue());
			mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, orden.getFechaOrden());
			mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, orden.getFechaValor());
			mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, obtenerCodigoClienteContraparteBDV());
			mensajeCarmen.setUsuarioNM(getUserName());
		
			//Establecer valores por defecto al mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeCarmen);
			
		}

		//Guardar objeto mensaje con toda la informacion en Sesion
		_req.getSession().setAttribute("mensaje_carmen", mensajeCarmen);
		
	}

	/**
	 * Genera el mensaje para BCV
	 * @return mensajeBcv: objeto con el mensaje para bcv
	 * @throws Exception 
	 */
	private void generarMensajeBCV() throws Exception{
		
		MensajeBcv mensajeBcv = null;	
	
		//Verificar si el tipo de producto es SITME
		if (orden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
			
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);		
			mensajeBcv = new MensajeBcv();
			
			//Setear Valores al Mensaje para Interfaz BCV
			mensajeBcv.set(mensajeBcv.SECUENCIA, "1");//TODO ??PENDIENTE SECUENCIA EN MENSAJE BCV
			mensajeBcv.set(mensajeBcv.NOMBRE_RAZON_SOCIAL, cliente.getNombre());
			mensajeBcv.set(mensajeBcv.RIF_LETRA, cliente.getTipoPersona());
			mensajeBcv.set(mensajeBcv.RIF_NUMERO, String.valueOf(cliente.getRifCedula()));
			mensajeBcv.set(mensajeBcv.TITULO, obtenerDatosTitulo(mensajeBcv,ordenTitulo.getTituloId()));
			mensajeBcv.set(mensajeBcv.BROKER, _req.getParameter("broker"));//TODO PENDIENTE buscar Broker en Venta de Titulos
			mensajeBcv.set(mensajeBcv.CONCEPTO, _req.getParameter("concepto"));
			mensajeBcv.set(mensajeBcv.SECTOR, _req.getParameter("sector_productivo"));
			mensajeBcv.set(mensajeBcv.OPERADOR_VENTA, "001");//TODO PENDIENTE POR BUSCAR
			mensajeBcv.set(mensajeBcv.FECHA_OPERACION, orden.getFechaOrden());
			mensajeBcv.set(mensajeBcv.FECHA_VALOR, orden.getFechaValor());
			mensajeBcv.set(mensajeBcv.PRECIO_TITULO, ordenTitulo.getPorcentajeRecompra());
			mensajeBcv.set(mensajeBcv.MONTO_DOLARES, instruccionesPago.getMontoInstruccionNoConversion().setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN).doubleValue());
			mensajeBcv.setUsuarioNM(getUserName());
			
			//Establecer valores por defecto al mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeBcv);

		}
		
		//Guardar objeto mensaje con toda la informacion en Sesion
		_req.getSession().setAttribute("mensaje_bcv", mensajeBcv);
		
	}


	/**
	 * Validaciones Basicas del action
	 * 
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario
	 * @throws Exception
	 */
	public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		boolean error = false;
		if (flag) {
			// verificar si el usuario es especial
			verificarUsuarioEspecial();

			tipoProductoId = _req.getParameter("tipo_producto_id");
			productoCliente=_req.getParameter("text_prod_cte");
			
			if (_record.getValue("client_id") != null){
				idCliente = Long.parseLong(_record.getValue("client_id"));
			}
			
			titulos_venta = _req.getParameterValues("titulos_custodia");
			
			//---- buscar datos del cliente--------
			buscarDatosCliente();
			//-------------------------------------
			
			if (titulos_venta != null) {
				String idTitulo = "";
				for (int i = 0; i < titulos_venta.length; i++) {

					// ID del TITULO en Base de Datos
					idTitulo = _req.getParameter("titulo_id_" + titulos_venta[i]);

					// Validar que la forma de pago, la moneda y la cantidad a vender no sean nulas
					if (_req.getParameter("moneda_id_" + titulos_venta[i]) != null && !_req.getParameter("moneda_id_" + titulos_venta[i]).equals("") && _req.getParameter("cantidad_venta_" + titulos_venta[i]) != null && !_req.getParameter("cantidad_venta_" + titulos_venta[i]).trim().equals("")) {

						CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);
						MonedaDAO monedaDAO = new MonedaDAO(_dso);

						// listar datos de la moneda seleccionada para el t&iacute;tulo
						monedaDAO.listar(_req.getParameter("moneda_id_" + titulos_venta[i]));

						custodiaDAO.listarTitulos(idCliente, idTitulo, tipoProductoId);
						datos_custodia = custodiaDAO.getDataSet();
						if (custodiaDAO.getDataSet().next()) {

							int cantidad_disponible = Integer.parseInt(custodiaDAO.getDataSet().getValue("cantidad_disponible"));

							// si la cantidad disponible es 0
							if (cantidad_disponible == 0) {
								_record.addError("Ordenes / Venta de T&iacute;tulos", "El cliente no posee cantidad disponible para la venta del t&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]));
								error = true;
							} else {

								if (Integer.parseInt(_req.getParameter("cantidad_venta_" + titulos_venta[i])) == 0) {
									_record.addError("Ordenes / Venta de T&iacute;tulos", "La cantidad vender para el T&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]) + " debe ser mayor a cero (0)");
									error = true;
								} else {
									if (Integer.parseInt(_req.getParameter("cantidad_venta_" + titulos_venta[i])) > cantidad_disponible) {
										_record.addError("Ordenes / Venta de T&iacute;tulos", "La cantidad vender sobrepasa la cantidad disponible para el T&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]));
										error = true;									
									}								
								}							
							}


							monedaDAO.getDataSet().next();

							// validar que se haya introducido precio de recompra para el título
							if (_req.getParameter("titulos_precio_recompra_" + titulos_venta[i]) == null || _req.getParameter("titulos_precio_recompra_" + titulos_venta[i]).trim().equals("")) {
								_record.addError("Para su informaci&oacute;n", "Debe introducir un precio de recompra para el t&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]));
								error = true;							
							} else {							
								// validar precio de recompra mayor que 0
								if (Double.parseDouble(_req.getParameter("titulos_precio_recompra_" + titulos_venta[i])) == 0) {																
									_record.addError("Para su informaci&oacute;n", "El precio de recompra para el t&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]) + " debe ser mayor a cero (0)");
									error = true;
								}
							
								

							}
							
							//Validacion que se seleccione la cuenta a la cual se va a realizar la transferencia
							if((_req.getParameter("tipo_inst_int")!=null)&& (!_req.getParameter("tipo_inst_int").equals(""))){
								tipoInstruccionPago=_req.getParameter("tipo_inst_int");
								if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_SWIFT)){								
								
									if((_req.getParameter("text_instruc_swift")==null) || (_req.getParameter("text_instruc_swift").equals(""))){																		
										_record.addError("Para su informaci&oacute;n", " Debe seleccionar una Instruccion de Pago ");									
										error = true;								
									}							
									
									//valida la seleccion del producto-cuenta
									if (productoCliente == null ||productoCliente.equals("")) {
										_record.addError("Para su informaci&oacute;n", "Debe seleccionar un producto asociado al cliente");
										error = true;							
									}

								}else if(tipoInstruccionPago.equals(ConstantesGenerales.INSTRUCCION_ABONO_CTA_NACIONAL_DOLARES)){	
									
									if((_req.getParameter("text_cta_nacional_dol")==null) || (_req.getParameter("text_cta_nacional_dol").equals(""))) {									
										_record.addError("Para su informaci&oacute;n", " Debe seleccionar una Cuenta Nacional en Dólares ");																			
										error = true;								
									}							
								}																					
							}else {
								_record.addError("Para su informaci&oacute;n", " Debe seleccionar un Tipo de instrucci&oacute;n de pago");																			
								error = true;	
							}
							// ---Validación de datos para instrucciones de pago----
							// si el usuario especial debe ingresar instrucciones de pago
							
							/*if (usuarioIngresoInstruccionesPago) {
								// VALIDACIONEES DE INSTRUCCIONES DE PAGO
								ValidacionInstruccionesPago valInst = new ValidacionInstruccionesPago(_dso);
								HashMap<String, String> parametrosEntrada = new HashMap<String, String>();

								// /---Si la moneda seleccionada para la venta es local validar datos para cuenta nacional
								if ((monedaDAO.getDataSet().getValue("moneda_in_local") != null && monedaDAO.getDataSet().getValue("moneda_in_local").equals("1"))) {

									parametrosEntrada.put(ValidacionInstruccionesPago.TIPO_INSTRUCCION, String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
									parametrosEntrada.put(ValidacionInstruccionesPago.NUMERO_CUENTA_NACIONAL, _req.getParameter("cta_nac_numero"));

								}

								// --Si la moneda seleccionada para venta es extranjera, validar datos para transferencia o cheque
								if ((monedaDAO.getDataSet().getValue("moneda_in_local") != null && monedaDAO.getDataSet().getValue("moneda_in_local").equals("0"))) {

									if (_req.getParameter("tipo_inst_int") != null && !_req.getParameter("tipo_inst_int").equals("")) {

										// si es una transferencia
										if (_req.getParameter("tipo_inst_int").equals("1")) {

											parametrosEntrada.put(ValidacionInstruccionesPago.TIPO_INSTRUCCION, String.valueOf(TipoInstruccion.CUENTA_SWIFT));

											// Seteando parametros de instrucciones
											parametrosEntrada.put(ValidacionInstruccionesPago.NUMERO_CUENTA_EXTRANJERA, _req.getParameter("ctecta_numero"));
											parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BANCO_EXTRANJERO, _req.getParameter("ctecta_bcocta_bco"));
											parametrosEntrada.put(ValidacionInstruccionesPago.ABA_BANCO, _req.getParameter("ctecta_bcocta_aba"));
											parametrosEntrada.put(ValidacionInstruccionesPago.BIC_BANCO, _req.getParameter("ctecta_bcocta_bic"));
											parametrosEntrada.put(ValidacionInstruccionesPago.DIRECCION_BANCO_CLIENTE, _req.getParameter("ctecta_bcocta_direccion"));
											parametrosEntrada.put(ValidacionInstruccionesPago.TELEFONO_BANCO_CLIENTE, _req.getParameter("ctecta_bcocta_telefono"));

											

										} else if(_req.getParameter("tipo_inst_int").equals("2")) {
											parametrosEntrada.put(ValidacionInstruccionesPago.TIPO_INSTRUCCION, String.valueOf(TipoInstruccion.CHEQUE));
										} else if(_req.getParameter("tipo_inst_int").equals("3")) {
											parametrosEntrada.put(ValidacionInstruccionesPago.TIPO_INSTRUCCION, String.valueOf(TipoInstruccion.CUENTA_NACIONAL_DOLARES));
										}

									}// finaliza

								}// fin moneda extranjera

								parametrosEntrada.put(ValidacionInstruccionesPago.CEDULA_BENEFICIARIO, _req.getParameter("cedula_beneficiario"));
								// parametrosEntrada.put(ValidacionInstruccionesPago.CUENTA_BENEFICIARIO, arg1);
								parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BENEFICIARIO, _req.getParameter("nombre_beneficiario"));
								parametrosEntrada.put(ValidacionInstruccionesPago.VALIDAR_DATOS_BENEFICIARIO, "1");

								valInst.setParametrosEntrada(parametrosEntrada);

								ArrayList<String> listaMensajes = valInst.validador();
								// Verificar lista de errores
								if (listaMensajes.size() != 0) {
									for (int k = 0; k < listaMensajes.size(); k++) {
										_record.addError("Para su informaci&oacute;n", (String) listaMensajes.get(k));
									}
									error = true;
								}

							}*/

						} else {

							_record.addError("Ordenes / Venta de T&iacute;tulos", "El T&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]) + " no fue encontrado en custodia");
							error = true;
						}
						
						
					} else {

						if (_req.getParameter("moneda_id_" + titulos_venta[i]) == null || _req.getParameter("moneda_id_" + titulos_venta[i]).equals("")) {

							_record.addError("Ordenes / Venta de T&iacute;tulos", "Debe seleccionar una moneda de pago para el t&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]));
							error = true;
						}

						if (_req.getParameter("cantidad_venta_" + titulos_venta[i]) == null || _req.getParameter("cantidad_venta_" + titulos_venta[i]).trim().equals("")) {

							_record.addError("Ordenes / Venta de T&iacute;tulos", "Debe indicar la cantidad a vender para el t&iacute;tulo " + _req.getParameter("titulo_descripcion_" + titulos_venta[i]));
							error = true;
						}

					}

				}
				
				//Se elimina la validación de cuenta custodia 18/10/2012				
				/*if(validarCuentaCustodia()){
					error=true;	
				}	*/			 
				

			} else {

				_record.addError("Ordenes / Venta de T&iacute;tulos", "Debe seleccionar el t&iacute;tulo para la venta");
				error = true;

			}
					
			//Guarda la forma de negociación
			if (_record.getValue("forma_negociacion") != null){
				_req.getSession().setAttribute("forma_negociacion", _record.getValue("forma_negociacion"));
			}
			
			if(tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				if (_req.getParameter("sector_productivo").equals("") || _req.getParameter("concepto").equals("") || _req.getParameter("broker").equals("")){
					_record.addError("Error", "No es posible realizar la venta, la actividad económica, el sector productivo, el concepto y el broker son necesarios");
					error = true;
				}
			}
			
			if (error){
				flag = false;
			}

		}

		return flag;
	}

	/**
	 * Busca la cuenta custodia del cliente y valida que exista
	 * @return true si hay error al No encontrar la cuenta custodia, false en caso contrario
	 * @throws Exception
	 */
	private boolean validarCuentaCustodia() throws Exception {
		
		boolean error = false;
		//Verificar tipo producto SITME
		if(tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) || tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){			
			_cuentaCustodia = getCuentaCustodia(cliente.getRifCedula(), cliente.getTipoPersona());
			if (_cuentaCustodia.count() == 0) {
				_record.addError("Cliente", "El cliente seleccionado debe poseer una cuenta custodia para la venta de t&iacute;tulos "+tipoProductoId);
				error = true;
			}
		}
				
		return error;
	}

	/**
	 * Busca los datos del cliente para los calculos en la venta del t&iacute;tulo
	 * @throws Exception
	 */
	private void buscarDatosCliente() throws Exception {
		
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		
		try {
			clienteDAO.detallesCliente(String.valueOf(idCliente));
			_datosCliente = clienteDAO.getDataSet();
			if (clienteDAO.listarPorId(idCliente)) {
				cliente = (Cliente) clienteDAO.moveNext();
				tipoPersona = cliente.getTipoPersona();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error buscando los datos del cliente..");
		}finally{
			clienteDAO.closeResources();
			clienteDAO.cerrarConexion();
		}		
	}
	
	/**
	 * Busca la información de la cuenta custodia del cliente si existe
	 * @param cedRifCliente: cedula o rif numérico del cliente
	 * @param tipoPersona: tipo de persona del cliente (V,E,J,G)
	 * @return dataSet con los datos del cliente
	 * @throws Exception
	 */
	public DataSet getCuentaCustodia(long cedRifCliente, String tipoPersona) throws Exception{
			
		ClienteCuentasDAO cuentaCustodiaDAO = new ClienteCuentasDAO(_dso);
				
		cuentaCustodiaDAO.listarCuentaCustodia(String.valueOf(cedRifCliente), tipoPersona);
	
		return cuentaCustodiaDAO.getDataSet();
	}

	/**
	 * Obtiene el código del cliente registrado en CARMEN perteneciente a la contraparte de BDV
	 * @return código registrado en INFI
	 * @throws Exception en caso de error
	 */
	private int obtenerCodigoClienteContraparteBDV() throws Exception{
		if (customerNumberBDV == 0){
			String codigo = ParametrosDAO.listarParametros(ParametrosSistema.CUSTOMER_NUMBER_BDV, this._dso);
			if (codigo != null && !codigo.equals("")){
				customerNumberBDV = Integer.parseInt(codigo);
			}
		}
		return customerNumberBDV;
	}
	
	/**
	 * Obtiene el valor final para el campo de TITULO de BCV. Usa cache.
	 * @param mensajeBcv objeto correspondiente al mensajeBcv
	 * @param idTitulo id del título a buscar
	 * @return valor final que debe llevar el campo
	 * @throws Exception en caso de error
	 */
	private String obtenerDatosTitulo(MensajeBcv mensajeBcv, String idTitulo) throws Exception{
		String valorFinal = "";
		if (hashTituloBCV.containsKey(idTitulo)){
			valorFinal = hashTituloBCV.get(idTitulo); 
		}else{
			valorFinal = mensajeBcv.obtenerValorFinalTitulo(_dso, idTitulo);
			hashTituloBCV.put(idTitulo, valorFinal);
		}
		return valorFinal;
	}

}
