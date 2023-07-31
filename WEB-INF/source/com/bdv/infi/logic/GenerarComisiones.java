package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import megasoft.DataSet;
import megasoft.db;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.CalculoMesDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.FacturaDAO;
import com.bdv.infi.dao.FechasCierresDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.data.DetalleFactura;
import com.bdv.infi.data.Factura;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UsoCuentas;

/**Genera las comisiones que se deben cobrar a los clientes*/
public class GenerarComisiones extends Transaccion implements Runnable {

	private Date fechaInicio;
	private Date fechaFin;
	private int usuarioId;
	private String nmUsuario = "";
	private String sucursal = "";
	private String ultimoUsuarioProcesado = ""; //Se usa para la búsqueda de instrucciones de cobro de comisión
	private ClienteCuentasDAO clienteCuentasDAO= null;
	private DataSet instruccionesCobro = null;
	private OrdenDAO ordenDao;
	private Factura factura;
	private FacturaDAO facturaDao;
	private DetalleFactura detalleFactura;
	private com.bdv.infi.dao.Transaccion transaccion;
	private Orden orden;
	GregorianCalendar gc = new GregorianCalendar(); 
	
	public GenerarComisiones(DataSource datasource, Date fechaInicio, Date fechaFin,int usuarioId, String nmUsuario, String codigoSucursal) {
		this._dso = datasource;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.usuarioId=usuarioId;
		this.nmUsuario = nmUsuario;
		this.sucursal = codigoSucursal;
		
		//Determina la fecha valor para el cobro de las comisiones. 15 días después de la fecha cierre de comisiones
		gc.setTime(fechaFin);
		gc.add(Calendar.DATE, 15);		
	}
	
	Logger logger = Logger.getLogger(GenerarComisiones.class);
	
	public void run(){
		logger.info("Iniciado el proceso de cierre de mes por el usuario que posee id " + usuarioId);
		logger.info("Efectuando cierre de mes de fecha "+fechaFin);
		
		/* Primero creamos el proceso */
		Proceso proceso = new Proceso();
		proceso.setTransaId(TransaccionNegocio.CUSTODIA_COMISIONES);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		ProcesosDAO procesoDao = new ProcesosDAO(this._dso);
		ordenDao = new OrdenDAO(this._dso);
		FechasCierresDAO fechaCierreDao = new FechasCierresDAO(this._dso); 
		
		//Inicia una transacción
		transaccion = new com.bdv.infi.dao.Transaccion(this._dso);					
		clienteCuentasDAO = new ClienteCuentasDAO(this._dso);
		TransaccionFijaDAO transaccionFijaDAO= new TransaccionFijaDAO(this._dso);
								
		// orden
		Date FechaActual = new Date();
		try {
			String idCliente = "";
			DataSet consultaComision = null;
			long idConsulta;
			
			//Inicia la transacción
			transaccion.begin();
			
			CalculoMesDAO calculoMesDao = new CalculoMesDAO(transaccion);
			BigDecimal montoTotalComisiones = new BigDecimal(0); 
			BigDecimal montoComision = new BigDecimal(0);
			
			facturaDao = new FacturaDAO(transaccion);
			
			//Obtenemos el código de operación que llevarán la operación de las ordenes
			com.bdv.infi.data.TransaccionFija tf = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.COBRO_COMISION_CUSTODIA);
			if (tf == null){
				logger.info("Error en la búsqueda de la transacción fija con código " + TransaccionFija.COBRO_COMISION_CUSTODIA);
				throw new Exception("Error en la búsqueda de la transacción fija con código " + TransaccionFija.COBRO_COMISION_CUSTODIA);
			}
			
			/* Lo guardamos en BD */
			db.exec(this._dso, procesoDao.insertar(proceso));
			
			logger.info("Invocando el cálculo de comisiones...");
			
			CalculoComisiones calculoComisiones = new CalculoComisiones(this._dso);
			idConsulta = calculoComisiones.calcularComisiones(fechaInicio, fechaFin, usuarioId, null);
		
			//Busca la última consulta ejecutada
			calculoMesDao.listarDetalles(idConsulta);
			consultaComision = calculoMesDao.getDataSet();
			factura = new Factura();
			orden = new Orden();
			
			OrdenDataExt ordenDataExt = new OrdenDataExt();
			ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
			ordenDataExt.setValor(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
			orden.agregarOrdenDataExt(ordenDataExt);
			
			if (consultaComision.count()>0){
				consultaComision.first();
				while (consultaComision.next()){
					//Si el usuario no es igual se crea la orden
					if (!idCliente.equals(consultaComision.getValue("client_id"))){
						orden.setStatus(StatusOrden.REGISTRADA);
						orden.setIdEjecucion(proceso.getEjecucionId());
						if (!idCliente.equals("")){
							//Guardamos la orden y generamos la factura final
							guardarOrden(orden,montoTotalComisiones);
							factura = new Factura();
							orden = new Orden();
							orden.agregarOrdenDataExt(ordenDataExt);
							orden.setStatus(StatusOrden.REGISTRADA);							
							montoTotalComisiones = new BigDecimal(0);
						}						
						idCliente = consultaComision.getValue("client_id");
						orden.setNombreUsuario(this.nmUsuario);
						orden.setSucursal(this.sucursal);
						orden.setIdCliente(Long.parseLong(idCliente));
						orden.setFechaOrden(FechaActual);
						orden.setFechaValor(gc.getTime());
						orden.setIdTransaccion(TransaccionNegocio.CUSTODIA_COMISIONES);
						
						factura.setFechaMes(fechaFin);
						factura.setIdCliente(orden.getIdCliente());						
					}
					
					//Generamos el detalle de la factura
					detalleFactura = new DetalleFactura();
					
					//Generamos la operación y se la agregamos a la orden
					OrdenOperacion ordenOperacion = new OrdenOperacion();
					ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
					ordenOperacion.setIdMoneda(consultaComision.getValue("moneda_id"));
					ordenOperacion.setFechaAplicar(gc.getTime());
					ordenOperacion.setInComision(1);
					montoComision = new BigDecimal(consultaComision.getValue("monto_operacion"));
					montoTotalComisiones = montoTotalComisiones.add(montoComision);
					ordenOperacion.setMontoOperacion(montoComision);
					ordenOperacion.setNombreOperacion(consultaComision.getValue("operacion_nombre"));
					ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
					ordenOperacion.setIdTransaccionFinanciera(String.valueOf(tf.getIdTransaccion()));
					ordenOperacion.setCodigoOperacion(tf.getCodigoOperacionFija());					
					
					orden.agregarOperacion(ordenOperacion);					
					
					//Generamos el detalle que guardaremos en factura
					detalleFactura.setCantidad(Double.parseDouble(consultaComision.getValue("cantidad")));
					detalleFactura.setIdMoneda(ordenOperacion.getIdMoneda());
					detalleFactura.setMontoOperacion(montoComision.doubleValue());
					detalleFactura.setNombreServicio(ordenOperacion.getNombreOperacion());
					detalleFactura.setTasaMonto(Double.parseDouble(consultaComision.getValue("tasa_monto")));
					detalleFactura.setCantidadOperaciones(Long.parseLong(consultaComision.getValue("cantidad_operaciones")));
					//Asociamos el detalle a la factura
					factura.agregarDetalleFactura(detalleFactura);
					
					establecerInstruccioneDeCobro(ordenOperacion,idCliente);					
				}
				//Efectúa el último guardado de la orden del cliente
				//Guardamos la orden y generamos la factura final
				guardarOrden(orden,montoTotalComisiones);				
				
			}
			//Actualiza la fecha de cierre
			String[] cierreMes = fechaCierreDao.cerrarMes();
			transaccion.ejecutarConsultas(cierreMes);			
			transaccion.end();
			logger.info("Proceso de cierre de mes finalizado con éxito");
		} catch (Exception e) {
			try{
				transaccion.rollback();
				logger.error("Error en el proceso de cierre de mes. " + e.getMessage());				
			} catch (Exception ex){
				e.printStackTrace();	
			}
			proceso.setDescripcionError(e.getMessage());
		} finally {
			proceso.setFechaFin(new Date());
			if (proceso.getDescripcionError() == null) {
				proceso.setDescripcionError("");
			}

			try {
				db.exec(this._dso, procesoDao.modificar(proceso));
				procesoDao.cerrarConexion();
			} catch (Exception e) {
				logger.error("Error efectuando modificación al proceso. " + e.getMessage());				
				e.printStackTrace();
			}
		}
	}

	public void execute() throws Exception {

	}
	
	/**Busca la cuenta a la que se le debe efectuar el débito de la comisión calculada y la setea al objeto
	 * operación que se recibe
	 * @param operacionCobro objeto operación
	 * @param idCliente id del cliente que se está procesando*/
	private void establecerInstruccioneDeCobro(OrdenOperacion operacionCobro, String idCliente) throws Exception{
		//Busca las instrucciones de cobro del cliente
		if (!idCliente.equals(ultimoUsuarioProcesado)){
			clienteCuentasDAO.listarCuentaUsoCliente(idCliente,UsoCuentas.COBRO_DE_COMISIONES);
			instruccionesCobro = clienteCuentasDAO.getDataSet();
			if (instruccionesCobro.count()>0){
				instruccionesCobro.next();	
			}else{
				logger.warn("El cliente con código " + idCliente + " no posee instrucciones de cobro");
			}
			ultimoUsuarioProcesado = idCliente;
		} 		
		
		if (instruccionesCobro.count()>0){
			operacionCobro.setNumeroCuenta(instruccionesCobro.getValue("ctecta_numero"));
		}
	}
	
	/**Guarda la orden correspondiente a la comisión que debe cobrarse al cliente*/
	private void guardarOrden(Orden orden,BigDecimal montoTotalComisiones) throws Exception{
		String[] sql = ordenDao.insertar(orden);
		factura.setIdOrden(orden.getIdOrden());
		factura.setMontoTotal(montoTotalComisiones.doubleValue());
		transaccion.ejecutarConsultas(sql);
				
		facturaDao.insertar(factura);
	}
}