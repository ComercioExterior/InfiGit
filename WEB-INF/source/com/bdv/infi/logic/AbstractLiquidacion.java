package com.bdv.infi.logic;

import static com.bdv.infi.logic.interfaces.ConstantesGenerales.SIGLAS_BCV;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.Logger;
import megasoft.db;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaces.UsoCuentas;

/**
 * Clase abstracta que contiene los metodos generales a usar para los procesos de liquidacion
 * y los métodos específicos a implementar según el tipo de producto
 * @author nm25287
 *
 */
abstract class AbstractLiquidacion {
	protected DataSource _dso;
	private int idUsuario;
	private String ip;
	private String sucursal;
	protected String nmUsuario; 
	protected Proceso proceso;
	protected int secuenciaProcesos = 0;			
	protected ArrayList<String> querysEjecutar=new ArrayList<String>();	
	protected Date fechaActual=new Date();
	private String codigoOperacionCteCreAbonoCtaDolares="";
	private String codigoOperacionCteCreAbonoBCV="";
	private String monedaLocal;
	private int cantidadTotalOrdenes=0;
	private int cantidadTotalOrdenesProcesadas=0;
	private int cantidadLoteOrdenesProcesadas=0;	
	//DAO
	private ProcesosDAO procesosDAO;
	protected OrdenDAO ordenDAO;
	private ClienteCuentasDAO clienteCuentasDAO;
	private TransaccionFijaDAO transaccionFijaDAO;
	private MonedaDAO monedaDAO;
	private UnidadInversionDAO unidadInversionDAO;	
	private OperacionDAO operacionDAO;
	
	
	public AbstractLiquidacion(DataSource _dso, int usuario, String ipEquipo, String sucursal, String nmUsuario) throws Exception {
		this._dso = _dso;
		this.idUsuario = usuario;
		this.ip = ipEquipo;
		this.sucursal = sucursal;
		this.nmUsuario = nmUsuario;	
		procesosDAO = new ProcesosDAO(_dso);
		ordenDAO = new OrdenDAO(_dso);
		clienteCuentasDAO = new ClienteCuentasDAO(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		monedaDAO = new MonedaDAO(_dso);
		operacionDAO = new OperacionDAO(_dso);
		unidadInversionDAO =  new UnidadInversionDAO(_dso);
	}
	
	
	/**
	 * Obtiene los códigos de operacion necesarios para la liquidación
	 * @author nm25287
	 * @param vehiculoId
	 * @param unidadInversion
	 * @return
	 */
	protected boolean obtenerCodigosOperacion(String vehiculoId, String unidadInversion){
		com.bdv.infi.data.TransaccionFija transaccionFija = null;
		try {
			//OBTENER CODIGO DE OPERACION PARA ABONO EN CUENTA EN DOLARES
			transaccionFija=transaccionFijaDAO.obtenerCodigosOperacionesTRNFVehiculo(vehiculoId,unidadInversion,TransaccionFija.TOMA_ORDEN_CTA_DOLARES_MANEJO_MIXTO);
			codigoOperacionCteCreAbonoCtaDolares=transaccionFija.getCodigoOperacionCteCre();
			
			//OBTENER CODIGO DE OPERACION PARA ABONO A BANCO CENTRAL
			//transaccionFija=transaccionFijaDAO.obtenerCodigosOperacionesTRNFVehiculo(vehiculoId,unidadInversion,TransaccionFija.DEPOSITO_BCV);
			//codigoOperacionCteCreAbonoBCV=transaccionFija.getCodigoOperacionCteCre();
						
			Logger.debug(this,"CODIGO OPERACION CUENTA EN DOLARES: "+transaccionFija.getCodigoOperacionCteCre());
			//Logger.debug(this,"CODIGO OPERACION BCV: "+transaccionFija.getCodigoOperacionVehCre());
			
			if(codigoOperacionCteCreAbonoCtaDolares!=null&&codigoOperacionCteCreAbonoCtaDolares.length()>0&&!codigoOperacionCteCreAbonoCtaDolares.equalsIgnoreCase("0"))
			//&&codigoOperacionCteCreAbonoBCV!=null&&codigoOperacionCteCreAbonoBCV.length()>0&&!codigoOperacionCteCreAbonoBCV.equalsIgnoreCase("0")){
				return true;
			
			
		} catch (Exception e) {
			Logger.info(this,"Ha ocurrido un error en la consulta de códigos de operación: "+e.getMessage());
		}
		return false;
	}
	
	/**
	 * Cambia de estatus a la unidad de inversion a LIQUIDADA
	 * @author nm25287
	 * @param estatusOrdenLiquidar
	 * @param unidadInversion
	 * @param tipoProducto
	 */
	public void cambioEstatusUnidadInversion(String estatusOrdenLiquidar,String unidadInversion, String tipoProducto){
		int cantOrdenes=0;
		try {
			ordenDAO.listarOrdenesPorUnidadInversionDataSet(estatusOrdenLiquidar, true, unidadInversion, tipoProducto,true);
			if(ordenDAO.getDataSet().count()>0){			
				if (ordenDAO.getDataSet().next()) {	
					cantOrdenes=Integer.parseInt(ordenDAO.getDataSet().getValue("cantordenes"));
					Logger.info(this, "Cantidad de ordenes en estatus "+estatusOrdenLiquidar+": "+cantOrdenes);
					if (cantOrdenes==0) {
						String updateUnidadInversion = unidadInversionDAO.modificarStatusQuery(Long.parseLong(unidadInversion), UnidadInversionConstantes.UISTATUS_LIQUIDADA);
						db.exec(_dso, updateUnidadInversion);
					}					
				}				
			}				
		} catch (Exception e) {
			Logger.info(this,"Ha ocurrido un error en la liquidación de la Unidad de Inversión");
		}	
	}
		
	/**
	 * Genera la orden de pago con las coordenadas de la instruccion de pago previamente registradas
	 * @author nm25287
	 * @param idOrden
	 * @param fechaValor
	 * @param clienteId
	 * @param monedaId
	 * @param tipoProducto
	 * @param montoOperacion
	 * @param idUnidadInversion
	 * @param sistemaId
	 * @param empresaId
	 * @param cuentaClienteBDV
	 * @param cuentaAbono
	 * @param vehiculoTomadorId
	 * @return
	 * @throws Exception
	 */
	public boolean crearOrdenPago(String idOrden, Date fechaValor,long clienteId, String monedaId,  String tipoProducto,BigDecimal montoOperacion, 
			String idUnidadInversion, String sistemaId, String empresaId, String cuentaClienteBDV, String cuentaAbono, String vehiculoTomadorId,ArrayList<String> querys) throws Exception {
		ValidacionInstruccionesPago valInst = new ValidacionInstruccionesPago(_dso);
		HashMap<String, String> parametrosEntrada = new HashMap<String, String>();
		boolean ordenCreada =false;
		
		OrdenDataExt ordenDataExt=null;		
		int tipoInstruccionId=0;
		CuentaCliente cuentaCliente= new CuentaCliente();
		String bicIntermediario = null;
			
		Logger.debug(this, "CREACIÓN ORDEN DE PAGO");
	
		if(cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO)){				
			tipoInstruccionId=TipoInstruccion.CUENTA_SWIFT;
		}else{
			tipoInstruccionId=TipoInstruccion.CUENTA_NACIONAL_DOLARES;
		}		
		Orden orden = new Orden();
		orden.setIdCliente(clienteId);
		orden.setStatus(StatusOrden.REGISTRADA);
		orden.setIdTransaccion(TransaccionNegocio.ORDEN_PAGO);
		orden.setFechaOrden(fechaActual);
		orden.setFechaValor(fechaValor);
		orden.setCarteraPropia(false);
		orden.setSucursal(sucursal);
		orden.setTerminal(ip);
		orden.setNombreUsuario(nmUsuario);	
		orden.setTipoProducto(tipoProducto);
		orden.setCuentaCliente(cuentaClienteBDV);	
		orden.setIdMoneda(monedaId);
		orden.setMonto(montoOperacion.doubleValue());
		orden.setIdOrdenRelacionada(Long.parseLong(idOrden));
		orden.setIdUnidadInversion(Integer.parseInt(idUnidadInversion));
		orden.setIdEmpresa(empresaId);
		orden.setIdSistema(Integer.parseInt(sistemaId!=null?sistemaId:"0"));
		orden.setVehiculoTomador(vehiculoTomadorId);
		
		OrdenOperacion ordenOperacion = new OrdenOperacion();
		ordenOperacion.setAplicaReverso(false);
		ordenOperacion.setDescripcionTransaccion("");
		ordenOperacion.setIdMoneda(monedaId);
		ordenOperacion.setMontoOperacion(montoOperacion);
		ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
		ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
		ordenOperacion.setFechaAplicar(fechaValor);
		ordenOperacion.setTasa(BigDecimal.ZERO);		
		ordenOperacion.setNombreOperacion("Orden de Pago de Solicitud Subasta Divisas");
				
		//ASIGNAR FECHA VALOR POSTERIOR PARA NO PERMITIR APLICAR LA OPERACION HASTA QUE SE COBRE
		if (operacionDAO.operacionesNoAplicadasOrden(idOrden)) {
			orden.setFechaValor(new Date(2200, 01, 01));
			ordenOperacion.setFechaAplicar(new Date(2200, 01, 01));
		}
		
		//AGREGAR DATA EXT-TIPO_INSTRUCCION_PAGO
		ordenDataExt = new OrdenDataExt();
		ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
		ordenDataExt.setValor(String.valueOf(tipoInstruccionId));
		orden.agregarOrdenDataExt(ordenDataExt);	
		
		//CONSULTAR DATOS DE LA CUENTA DEL CLIENTE
		clienteCuentasDAO.listarCuentaCliente(Long.parseLong(idOrden),tipoInstruccionId, UsoCuentas.RECOMPRA);	
		
		if (clienteCuentasDAO.getDataSet().next()) {
			//DATOS PROPIOS DEL TIPO DE INSTRUCCIOM
			if (cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO)) {
				Logger.debug(this, "Selección de pago SWIFT ");

				//OBTENER DATOS DE LA CUENTA DEL CLIENTE		
				cuentaCliente.setIdInstruccion(Long.parseLong(clienteCuentasDAO.getDataSet().getValue("CTES_CUENTAS_ID")));
				cuentaCliente.setCtecta_bcocta_aba(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_aba"));
				cuentaCliente.setCtecta_bcocta_bic(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bic"));
				cuentaCliente.setCtecta_bcocta_swift(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bic"));
				cuentaCliente.setCtecta_bcocta_bco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bco"));
				//cuentaCliente.setCtecta_nombre(clienteCuentasDAO.getDataSet().getValue("nombre"));
				cuentaCliente.setCtecta_numero(clienteCuentasDAO.getDataSet().getValue("ctecta_numero"));
				cuentaCliente.setCtecta_bcocta_telefono(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_telefono"));
				cuentaCliente.setCodCiudadOrigen(clienteCuentasDAO.getDataSet().getValue("cod_ciudad_origen"));
				cuentaCliente.setCodEstadoOrigen(clienteCuentasDAO.getDataSet().getValue("cod_estado_origen"));
				cuentaCliente.setNombre_beneficiario(clienteCuentasDAO.getDataSet().getValue("nombre_beneficiario"));

				bicIntermediario = clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_SWIFT");
				if (bicIntermediario != null && !bicIntermediario.trim().equals("")) {
					cuentaCliente.setCtecta_bcoint_aba(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_ABA"));
					cuentaCliente.setCtecta_bcoint_bco(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_BCO"));
					cuentaCliente.setCtecta_bcoint_direccion(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_DIRECCION"));
					cuentaCliente.setCtecta_bcoint_swift(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_SWIFT"));
					cuentaCliente.setCtecta_bcoint_telefono(clienteCuentasDAO.getDataSet().getValue("CTECTA_BCOINT_TELEFONO"));
				}

				parametrosEntrada.put(ValidacionInstruccionesPago.TIPO_INSTRUCCION, String.valueOf(TipoInstruccion.CUENTA_SWIFT));
				parametrosEntrada.put(ValidacionInstruccionesPago.NUMERO_CUENTA_EXTRANJERA, cuentaCliente.getCtecta_numero());
				parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BANCO_EXTRANJERO, cuentaCliente.getCtecta_bcocta_bco());
				parametrosEntrada.put(ValidacionInstruccionesPago.ABA_BANCO, cuentaCliente.getCtecta_bcocta_aba());
				parametrosEntrada.put(ValidacionInstruccionesPago.BIC_BANCO, cuentaCliente.getCtecta_bcocta_bic());
				parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BENEFICIARIO, cuentaCliente.getNombre_beneficiario());
				parametrosEntrada.put(ValidacionInstruccionesPago.ESTADO, cuentaCliente.getCodEstadoOrigen());
				parametrosEntrada.put(ValidacionInstruccionesPago.CIUDAD, cuentaCliente.getCodCiudadOrigen());

				valInst.setParametrosEntrada(parametrosEntrada);

				//TODO PENDIENTES LAS VALIDACIONES DE BANCO INTERMEDIARIO
				ArrayList<String> listaMensajes = valInst.validadorMensajeSWIFT();
				//Verificar lista de errores	
				if (listaMensajes.size() == 0) {
					orden.setTipoCuentaAbono(Integer.valueOf(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO));
					ordenOperacion.setCodigoABA(cuentaCliente.getCtecta_bcocta_aba());
					ordenOperacion.setCodigoSwiftBanco(cuentaCliente.getCtecta_bcocta_swift());
					ordenOperacion.setCodigoBicBanco(cuentaCliente.getCtecta_bcocta_bic());
					ordenOperacion.setNombreBanco(cuentaCliente.getCtecta_bcoint_bco());
					ordenOperacion.setNombreReferenciaCuenta(cuentaCliente.getCtecta_nombre());
					ordenOperacion.setNumeroCuenta(cuentaCliente.getCtecta_numero());
					ordenOperacion.setTelefonoBanco(cuentaCliente.getCtecta_bcocta_telefono());
					ordenOperacion.setCodigoOperacion("");

					if (bicIntermediario != null && !bicIntermediario.trim().equals("")) {
						ordenOperacion.setCodigoABAIntermediario(cuentaCliente.getCtecta_bcoint_aba());
						ordenOperacion.setCodigoSwiftBancoIntermediario(cuentaCliente.getCtecta_bcoint_swift());
						ordenOperacion.setDireccionBancoIntermediario(cuentaCliente.getCtecta_bcoint_direccion());
						ordenOperacion.setNombreBancoIntermediario(cuentaCliente.getCtecta_bcoint_bco());
						ordenOperacion.setTelefonoBancoIntermediario(cuentaCliente.getCtecta_bcoint_telefono());
					}
					ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB));	
					
					//AGREGAR OPERACION A LA ORDEN
					orden.agregarOperacion(ordenOperacion);

					//AGREGAR QUERYS PARA LA INSERCION de ORDEN,OPERACION y DATA EXT
					querys.addAll(Arrays.asList(ordenDAO.insertar(orden)));
					cuentaCliente.setIdOrden(orden.getIdOrden());

					//AGREGAR QUERYS PARA LA INSERCION de CUENTAS CLIENTE
					querys.addAll(Arrays.asList(clienteCuentasDAO.insertarClienteCuentasOrd(cuentaCliente)));
					ordenCreada=true;
				} else {
					Logger.debug(this, "La orden no posee datos de instrucción de pago SWIFT correctos"+listaMensajes.toString());
					throw new Exception("La orden no posee datos de instrucción de pago SWIFT correctos");
				}

			} else {
				Logger.debug(this, "Abono a cuenta en dólares ");
				ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.TOMA_ORDEN_CTA_DOLARES));	
				orden.setTipoCuentaAbono(Integer.valueOf(ConstantesGenerales.ABONO_CUENTA_NACIONAL));
				ordenOperacion.setNumeroCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_numero"));
				ordenOperacion.setCodigoOperacion(codigoOperacionCteCreAbonoCtaDolares);

				//AGREGAR OPERACION A LA ORDEN
				orden.agregarOperacion(ordenOperacion);

				//AGREGAR QUERYS PARA LA INSERCION DE ORDEN,OPERACION y DATA EXT
				querys.addAll(Arrays.asList(ordenDAO.insertar(orden)));
				ordenCreada=true;
			}
		}else
		{
			Logger.debug(this, "La orden no posee datos de instrucción de pago");
		}
		return ordenCreada;
	}

	/**
	 * Genera la orden de credito al BCV por el monto total adjudicado de las operaciones
	 * @author nm25287
	 * @param unidadInversion
	 * @param tipoProducto
	 * @throws Exception
	 */
	public void crearOrdenCreditoBCV(String unidadInversion,String tipoProducto) throws Exception{	
		com.bdv.infi.dao.Transaccion transaccionDao = new com.bdv.infi.dao.Transaccion(_dso);
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		Statement statement = null;
		ResultSet _ordenesALiquidar = null;
		ArrayList<String> Sql = new ArrayList<String>();
		String[] consultas;
		long idCliente = 0;
		String empresID=null;		
		long idRelacion = 0;
		String rifBancoCentral = null;
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		EmpresaDefinicionDAO empresDAO=new EmpresaDefinicionDAO(_dso);
		BigDecimal montoOperacion = new BigDecimal(0);
		String vehiculo = "", blotter="";

		try {				
			rifBancoCentral = ParametrosDAO.listarParametros(ParametrosSistema.RIF_BCV,_dso);
			monedaLocal = monedaDAO.listarIdMonedaLocal();
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
			}

			transaccionDao.begin();
			statement = transaccionDao.getConnection().createStatement();
			
			Logger.info(this,"Liquidando, Generacion orden credito BCV " );			
								
			_ordenesALiquidar = statement.executeQuery(ordenDAO.listarMontosGeneracionOrdenBCV(unidadInversion));
			
			Logger.info(this,"Id de unidad a liquidar:" + unidadInversion);

			while (_ordenesALiquidar.next()) {
				vehiculo=_ordenesALiquidar.getString("vehicu_id");
				blotter= _ordenesALiquidar.getString("bloter_id");
				montoOperacion=new BigDecimal(_ordenesALiquidar.getString("monto")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				Logger.info(this,"Monto de la operacion:" + montoOperacion);
				
				// Efectúa el crédito a BCV
				Orden ordenCredito = new Orden();
				if (idCliente == 0) {
					Logger.error(this,"Ha ocurrido un problema realizando la busqueda de empresa BCV");
					throw new Exception("Ha ocurrido un problema realizando la busqueda de empresa BCV");
				}
				
				ordenCredito.setIdEmpresa(empresID);			
				ordenCredito.setIdCliente(idCliente);
				ordenCredito.setFechaOrden(new Date());
				ordenCredito.setFechaValor(new Date());
				ordenCredito.setStatus(StatusOrden.REGISTRADA);
				ordenCredito.setIdTransaccion(TransaccionNegocio.LIQUIDACION);			
				ordenCredito.setIdUnidadInversion(Integer.parseInt(unidadInversion));		
				ordenCredito.setVehiculoTomador(_ordenesALiquidar.getString("vehicu_id"));
				ordenCredito.setTipoProducto(tipoProducto);
				ordenCredito.setSucursal(sucursal);
				ordenCredito.setNombreUsuario(nmUsuario);
				ordenCredito.setTerminal(ip);
				ordenCredito.setIdEjecucion(proceso.getEjecucionId());
				ordenCredito.setVehiculoColocador(vehiculo);
				ordenCredito.setVehiculoTomador(vehiculo);
				ordenCredito.setVehiculoRecompra(vehiculo);
				ordenCredito.setMontoTotal(montoOperacion.doubleValue());
				ordenCredito.setIdBloter(blotter);
				ordenCredito.setMontoComisionOrden(new BigDecimal(0));
				if (idRelacion > 0) {
					ordenCredito.setIdOrdenRelacionada(idRelacion);
				}
				
				OrdenOperacion operacionCredito = new OrdenOperacion();				
				operacionCredito.setMontoOperacion(montoOperacion);				
				String cuentaBCV=_ordenesALiquidar.getString("vehicu_numero_cuenta_bcv");
				if(cuentaBCV==null){
					Logger.error(this,"Problema en la generacion de Orden de Credito a BCV: No se ha encontrado el numero de cuenta BCV en el vehiculo");
					throw new Exception("Problema en la generacion de Orden de Credito a BCV: No se ha encontrado el numero de cuenta BCV en el vehiculo");
				} else {
					cuentaBCV=cuentaBCV.trim();
				}
				operacionCredito.setNumeroCuenta(cuentaBCV);
				operacionCredito.setCodigoOperacion(codigoOperacionCteCreAbonoBCV);				
				operacionCredito.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_BCV));
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
				Sql.add(ordenDAO.actualizarOrdenesIdRelacion(ordenCredito.getIdOrden(), _ordenesALiquidar.getLong("empres_id"), unidadInversion,tipoProducto));
			}		
			
			db.execBatch(_dso, (String[]) Sql.toArray(new String[Sql.size()]));
			
		} catch (Exception e) {
			Logger.error(this,"Error en la generación de la Orden a BCV", e);
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
		
	/**
	 * Registro de inicio del proceso
	 * @author nm25287
	 * @throws Exception
	 */
	protected void iniciarProceso() throws Exception {
		Logger.info(this,"INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.LIQUIDACION);
		proceso.setUsuarioId(this.idUsuario);

		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}
	
	/**
	 * Registro de fin del proceso
	 * @author nm25287
	 * @throws Exception
	 */
	protected void finalizarProceso() throws Exception {
		String queryProcesoCerrar = procesosDAO.modificar(proceso);
		db.exec(_dso, queryProcesoCerrar);
		Logger.info(this,"FIN DE PROCESO: " + new Date());
	}
	
	/**
	 * Método que maneja la ejeción por lotes de los querys del proceso de liquidación
	 * @author nm25287
	 * @throws SQLException
	 */
	protected void procesarQuerysPorLote(ArrayList<String> sentencias) throws SQLException{
		//EJECUCIÓN DE QUERYS
		Logger.debug(this, "EJECUCIÓN DE QUERYS");
		++cantidadTotalOrdenes;
		++cantidadLoteOrdenesProcesadas;
		if (ConstantesGenerales.COMMIT_REGISTROS_LIQ == cantidadLoteOrdenesProcesadas) {
			cantidadTotalOrdenesProcesadas = cantidadTotalOrdenesProcesadas + cantidadLoteOrdenesProcesadas;
			ordenDAO.ejecutarStatementsBatch(sentencias);
			sentencias.clear();
			cantidadLoteOrdenesProcesadas = 0;
			Logger.info(this, "Ordenes enviadas por COMMIT en proceso de LIQUIDACION : " + cantidadLoteOrdenesProcesadas);
		}
		Logger.info(this, "Realizacion de commit al numero de registro N° " + cantidadTotalOrdenesProcesadas);
	}
	
	/**
	 * Metodo que implementará la lógica propia de la liquidación de una orden según el tipo de producto asociado
	 * @author nm25287
	 * @param estatusOrdenLiquidar
	 * @param unidadInversion
	 * @param tipoProducto
	 * @param custodiaTitulos
	 * @throws Exception
	 */
	abstract void liquidarOrdenes(String estatusOrdenLiquidar,String unidadInversion, String tipoProducto, boolean custodiaTitulos) throws Exception;
	
	/**
	 * Metodo que implementará la lógica propia de la liquidación de una unidad de inversion según el tipo de producto asociado
	 * @author nm25287
	 * @throws Exception
	 */
	abstract void liquidarUnidadInversion() throws Exception;
	
}
