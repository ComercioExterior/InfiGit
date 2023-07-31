package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.util.Date;
import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.CalculoMesDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosCierreDAO;
import com.bdv.infi.data.CalculoMes;
import com.bdv.infi.data.CalculoMesDetalle;
import com.bdv.infi.data.CustodiaComisionDepositario;
import com.bdv.infi.data.CustodiaEstructuraTarifaria;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.ConversionMontos;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de realizar el cálculo de comisiones que debe cobrar el banco
 * por concepto de custodia y operaciones de salida interna y externa
 */
public class CalculoComisiones{

	/** DataSource de conexión a base de datos */
	private DataSource dataSource;

	/**
	 * Constructor
	 * 
	 * @param DataSource
	 *            ds
	 */
	public CalculoComisiones(DataSource ds) {
		this.dataSource = ds;
	}


	// Variables globales
	Date fechaInicio = null;
	Date fechaFin = null;
	String idClienteCalculo = "";
	CustodiaEstructuraTarifaria custodiaTarifas = null;
	CustodiaEstructuraTarifaria custodiaTarifasCliente = null;
	CustodiaEstructuraTarifariaDAO custodiaTarifasDao = null;
	CalculoMesDetalle calculoMesDetalle = new CalculoMesDetalle();
	String monedaLocal = "";
	CalculoMesDAO CalculoMesDao = null;	
	ConversionMontos conversionMontos = null;
	
	//Se usa para división de valores en la obtención de comisiones
	int POSICIONES_DECIMALES = 2;

	/**
	 * Proceso que calcula las comisiones que se deben cobrar por operaciones de salida interna, salida externa
	 * y posiciones en custodia que poseea el cliente.
	 * @param fechaInicio fecha de inicio para el cálculo de las comisiones
	 * @param fechaFin fecha fin para el cálculo de las comisiones
	 * @param idUsuario id del usuario que está efectuando la consulta
	 * @param idCliente id del cliente si se desea consultar las comisiones para un cliente. Si se 
	 * envia null las comisiones se calculan para todos los clientes que tengan posición en custodia
	 * @return devuelve el id generado que identifica la consulta efectuada 
	 */
	public long calcularComisiones(Date fechaInicio, Date fechaFin, int idUsuario, String idCliente)
			throws Exception {

		// Obtiene la fecha fin del mes
		/*Calendar calendario = Calendar.getInstance();
		calendario.set(Calendar.YEAR, anio);
		calendario.set(Calendar.DATE, 1);
		calendario.set(Calendar.MONTH, mes);
		fechaInicio = calendario.getTime();
		calendario.set(Calendar.DATE, Utilitario.diasDelMes(mes - 1, anio));
		fechaFin = calendario.getTime();*/
		
		// Se trabaja en una transaccion a base de datos
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(
				this.dataSource);
		conversionMontos = new ConversionMontos(this.dataSource);		

		try {
			this.fechaInicio = fechaInicio;
			this.fechaFin = fechaFin;
			idClienteCalculo = idCliente;
			// Mondeda Local
			MonedaDAO monedaDAO = new MonedaDAO(this.dataSource);
			monedaLocal = monedaDAO.listarIdMonedaLocal();
			// Crea una transacción y la inicia
			
			Logger.info(this,"Iniciando proceso de cálculo de comisión...");
			
			transaccion.begin();

			// Carga la estructura tarifaria general
			custodiaTarifasDao = new CustodiaEstructuraTarifariaDAO(this.dataSource);
			
			Logger.info(this,"Cargando estructura tarifaria general...");
			custodiaTarifas = custodiaTarifasDao.listarEstructura();
			
			if (custodiaTarifas!=null){
				Logger.info(this,"Id de estructura general obtendida: " + custodiaTarifas.getDatosComision().getIdComision());
				Logger.info(this,"Nombre de estructura general obtendida: " + custodiaTarifas.getDatosComision().getComisionNombre());
			} else {
				Logger.info(this,"No existe estructura tarifaria general");
			}
			
					
			if (custodiaTarifas != null){
				Logger.debug(this,"Se carga la  estructura tarifaria General para el proceso de Calculo de Comisiones");
				Logger.debug(this,"getMonedaAnualExtranjera()"+ custodiaTarifas.getTarifas().getMonedaAnualExtranjera());
				Logger.debug(this,"getMonedaAnualNacional()"+ custodiaTarifas.getTarifas().getMonedaAnualNacional());
				Logger.debug(this,"getMonedaTransaccionExterna()"	+ custodiaTarifas.getTarifas().getMonedaTransaccionExterna());
				Logger.debug(this,"getMonedaTransaccionInterna()"+ custodiaTarifas.getTarifas().getMonedaTransaccionInterna());
				Logger.debug(this,"getMontoAnualExtranjera()"+ custodiaTarifas.getTarifas().getMontoAnualExtranjera());
				Logger.debug(this,"getMontoAnualNacional()"+ custodiaTarifas.getTarifas().getMontoAnualNacional());
				Logger.debug(this,"getMontoTransaccionInterna()"+ custodiaTarifas.getTarifas().getMontoTransaccionInterna());
				Logger.debug(this,"getMontoTransaccionExterna()"+ custodiaTarifas.getTarifas().getMontoTransaccionExterna());
				Logger.debug(this,"getPctAnualExtranjera()"+ custodiaTarifas.getTarifas().getPctAnualExtranjera());
				Logger.debug(this,"getPctAnualNacional()"+ custodiaTarifas.getTarifas().getPctAnualNacional());
				Logger.debug(this,"getPctTransaccionExterna()"+ custodiaTarifas.getTarifas().getPctTransaccionExterna());
				Logger.debug(this,"getPctTransaccionInterna()"+ custodiaTarifas.getTarifas().getPctTransaccionInterna());
			}
			

			CalculoMesDao = new CalculoMesDAO(transaccion);

			// Elimina la última consulta efectuada por el usuario

			CalculoMesDao.eliminar(idUsuario,TransaccionNegocio.CUSTODIA_COMISIONES);

			Logger.info(this,"Eliminando consultas del usuario:"+ idUsuario);

			// Crea la cabecera de la consulta
			CalculoMes calculoMes = new CalculoMes();
			calculoMes.setFechaDesde(fechaInicio);
			calculoMes.setFechaHasta(fechaFin);
			calculoMes.setIdUsuario(idUsuario);
			calculoMes.setIdTransaccion(TransaccionNegocio.CUSTODIA_COMISIONES);

			CalculoMesDao.insertar(calculoMes);

			// Objeto Calculo MES
			calculoMesDetalle = new CalculoMesDetalle();
			calculoMesDetalle.setIdCalculoMes(calculoMes.getIdCalculoMes());
			
			//Calcula las comisiones de salida interna y externa por cliente
			calcularSalidaInternaExterna();
			
			//Calcula la posición global del cliente		
			calcularPosicionGlobal();
			
			Logger.info(this,"Cerrando cálculo de comisiones...");
			transaccion.end();
			return calculoMes.getIdCalculoMes();

		} catch (Exception e) {
			Logger.error(this,"Efectuando rollback de la operación de cálculo de comisión "+e.getMessage()+ Utilitario.stackTraceException(e));
			transaccion.rollback();
			throw e;
		} finally {
			custodiaTarifasDao.closeResources();
			custodiaTarifasDao.cerrarConexion();
			CalculoMesDao.closeResources();
			CalculoMesDao.cerrarConexion();
		}
	}// FIN calcularComisiones


	/**Calcula las comisiones para los clientes por concepto de operaciones en salida interna y externa*/
	private void calcularSalidaInternaExterna() throws Exception{
		OrdenDAO ordenDAO = new OrdenDAO(this.dataSource);
		// Se inicializan las variables que contaran la cantidad de
		// transacciones
		long cantidadSalidaInterna = 0;
		long cantidadSalidaExterna = 0;
		BigDecimal montoSalidaInterna = new BigDecimal(0);
		BigDecimal montoSalidaExterna = new BigDecimal(0);
		String idCliente = "";
		String transaccionOperacion = "";
		
		
		// Buscamos todos los clientes que se encuentran en custodia y que hayan efectuado operaciones
		// de salida interna y externa
		Logger.info(this,"Obteniendo las operaciones de salida interna y externa...");
		ordenDAO.listarMontosTitulos(idClienteCalculo,fechaInicio, fechaFin);
		DataSet transacciones = ordenDAO.getDataSet();
		if (transacciones.count()>0){
			transacciones.first();
			
			while (transacciones.next()){
				transaccionOperacion = transacciones.getValue("transa_id");
				
				//Si es la primera vez que entra lo setea
				if (idCliente.equals("")){
					idCliente = transacciones.getValue("client_id");
				}
				if (idCliente.equals(transacciones.getValue("client_id"))){					
					if (transaccionOperacion.equals(TransaccionNegocio.SALIDA_EXTERNA)){						
						cantidadSalidaExterna += Long.parseLong(transacciones.getValue("cantidad"));
						montoSalidaExterna = montoSalidaExterna.add(this.conversionMonto(transacciones.getValue("titulo_moneda_den"),new BigDecimal(transacciones.getValue("monto"))));
					} else if (transaccionOperacion.equals(TransaccionNegocio.SALIDA_INTERNA)){
						cantidadSalidaInterna += Long.parseLong(transacciones.getValue("cantidad"));
						Logger.info(this,"Monto de la operación " + transacciones.getValue("monto"));
						montoSalidaInterna = montoSalidaInterna.add(this.conversionMonto(transacciones.getValue("titulo_moneda_den"),new BigDecimal(transacciones.getValue("monto"))));						
					}					
				}else{
					generarCalculoComision(idCliente,cantidadSalidaExterna,montoSalidaExterna,cantidadSalidaInterna,montoSalidaInterna);
					idCliente = transacciones.getValue("client_id");					
					cantidadSalidaInterna = 0;
					cantidadSalidaExterna = 0;
					montoSalidaInterna = new BigDecimal(0);
					montoSalidaExterna = new BigDecimal(0);					
					if (transaccionOperacion.equals(TransaccionNegocio.SALIDA_EXTERNA)){					
						cantidadSalidaExterna += Long.parseLong(transacciones.getValue("cantidad"));
						montoSalidaExterna = montoSalidaExterna.add(this.conversionMonto(transacciones.getValue("titulo_moneda_den"),new BigDecimal(transacciones.getValue("monto"))));
					} else if (transaccionOperacion.equals(TransaccionNegocio.SALIDA_INTERNA)){
						cantidadSalidaInterna += Long.parseLong(transacciones.getValue("cantidad"));
						montoSalidaInterna = montoSalidaInterna.add(this.conversionMonto(transacciones.getValue("titulo_moneda_den"),new BigDecimal(transacciones.getValue("monto"))));						
					}					
				}
			}
			generarCalculoComision(idCliente,cantidadSalidaExterna,montoSalidaExterna,cantidadSalidaInterna,montoSalidaInterna);			
		}		
	}
	
	/**Genera la comisión calculada*/
	private void generarCalculoComision(String idCliente,long cantidadSalidaExterna, BigDecimal montoSalidaExterna, long cantidadSalidaInterna, BigDecimal montoSalidaInterna) throws Exception{
		custodiaTarifasCliente = custodiaTarifasDao.listarEstructura(Long.parseLong(idCliente));
		if (custodiaTarifasCliente == null){
			Logger.info(this,"No hay estructura tarifaria asociada al cliente " + idCliente);
			custodiaTarifasCliente = custodiaTarifas;			
		}
				
		Logger.info(this,"Calculando comisiones a cobrar por salida interna y externa para el cliente " + idCliente);
		
		//Calcula comisión salida interna
		if (custodiaTarifasCliente != null){		
			if (custodiaTarifasCliente.getTarifas().getPctTransaccionInterna()>0){
				calculoMesDetalle.setCantidad(montoSalidaInterna);
				if (montoSalidaInterna.doubleValue()>0){
					montoSalidaInterna = montoSalidaInterna.multiply(new BigDecimal(custodiaTarifas.getTarifas().getPctTransaccionInterna()));
					montoSalidaInterna = montoSalidaInterna.divide(new BigDecimal(100),POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
					calculoMesDetalle.setMontoOperacion(montoSalidaInterna);
					calculoMesDetalle.setNombreOperacion("Traspasos Internos");
					calculoMesDetalle.setTasaMonto(custodiaTarifasCliente.getTarifas().getPctTransaccionInterna());
					calculoMesDetalle.setIdMoneda(monedaLocal);			
					calculoMesDetalle.setIdCliente(Long.parseLong(idCliente));
					calculoMesDetalle.setSignoPorcentaje(true);
					calculoMesDetalle.setCantidadOperaciones(cantidadSalidaInterna);
					CalculoMesDao.insertarDetalle(calculoMesDetalle);
				}
			} else {
				//Si la moneda del monto a cobrar es diferente a la local se efectúal a conversión			
				BigDecimal montoAplicar = new BigDecimal(custodiaTarifasCliente.getTarifas().getMontoTransaccionInterna());			
				if (!custodiaTarifasCliente.getTarifas().getMonedaTransaccionInterna().equals(monedaLocal)){
					montoAplicar = conversionMonto(custodiaTarifasCliente.getTarifas().getMonedaTransaccionInterna(), montoAplicar); 
				}
				
				if (montoAplicar.doubleValue() > 0 && montoSalidaInterna.doubleValue()>0){			  
					//Por cantidad
					calculoMesDetalle.setCantidad(new BigDecimal(cantidadSalidaInterna));			
					montoSalidaInterna = montoAplicar.multiply(new BigDecimal(cantidadSalidaInterna));
					calculoMesDetalle.setMontoOperacion(montoSalidaInterna);
					calculoMesDetalle.setNombreOperacion("Traspasos Internos");
					calculoMesDetalle.setTasaMonto(montoAplicar.doubleValue());
					calculoMesDetalle.setIdMoneda(monedaLocal);			
					calculoMesDetalle.setIdCliente(Long.parseLong(idCliente));
					calculoMesDetalle.setSignoPorcentaje(false);
					calculoMesDetalle.setCantidadOperaciones(cantidadSalidaExterna);					
					CalculoMesDao.insertarDetalle(calculoMesDetalle);
				}
			}
		}
		
		//Calcula comisión de salida externa
		if (custodiaTarifasCliente != null){				
			if (custodiaTarifasCliente.getTarifas().getPctTransaccionExterna()>0){
				calculoMesDetalle.setCantidad(montoSalidaExterna);
				if (montoSalidaExterna.doubleValue()>0){
					montoSalidaExterna = montoSalidaExterna.multiply(new BigDecimal(custodiaTarifas.getTarifas().getPctTransaccionExterna()));
					montoSalidaExterna = montoSalidaExterna.divide(new BigDecimal(100),POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
					calculoMesDetalle.setMontoOperacion(montoSalidaExterna);
					calculoMesDetalle.setNombreOperacion("Traspasos Externos");
					calculoMesDetalle.setTasaMonto(custodiaTarifasCliente.getTarifas().getPctTransaccionExterna());
					calculoMesDetalle.setIdMoneda(monedaLocal);			
					calculoMesDetalle.setIdCliente(Long.parseLong(idCliente));
					calculoMesDetalle.setSignoPorcentaje(true);	
					calculoMesDetalle.setCantidadOperaciones(cantidadSalidaExterna);					
					CalculoMesDao.insertarDetalle(calculoMesDetalle);
				}
			} else {
				//Si la moneda del monto a cobrar es diferente a la local se efectúal a conversión
				BigDecimal montoAplicar = new BigDecimal(custodiaTarifasCliente.getTarifas().getMontoTransaccionExterna());			
				if (!custodiaTarifasCliente.getTarifas().getMonedaTransaccionExterna().equals(monedaLocal)){
					montoAplicar = conversionMonto(custodiaTarifasCliente.getTarifas().getMonedaTransaccionExterna(), montoAplicar); 
				}
				if (montoAplicar.doubleValue() > 0 && montoSalidaExterna.doubleValue()>0){			
					//Por cantidad
					calculoMesDetalle.setCantidad(new BigDecimal(cantidadSalidaExterna));			
					montoSalidaExterna = montoAplicar.multiply(new BigDecimal(cantidadSalidaExterna));
					calculoMesDetalle.setMontoOperacion(montoSalidaExterna);
					calculoMesDetalle.setNombreOperacion("Traspasos Externos");
					calculoMesDetalle.setTasaMonto(montoAplicar.doubleValue());
					calculoMesDetalle.setIdMoneda(monedaLocal);			
					calculoMesDetalle.setIdCliente(Long.parseLong(idCliente));
					calculoMesDetalle.setSignoPorcentaje(false);
					calculoMesDetalle.setCantidadOperaciones(cantidadSalidaExterna);					
					CalculoMesDao.insertarDetalle(calculoMesDetalle);
				}
			}
		}
		
		//Calcula las comisiones por depositarios
		calcularComisionesDepositarios(idCliente,custodiaTarifasCliente);
	}
	
	/**Calcula las comisiones por cliente y por deposotario
	 * @param idCliente id del cliente a consultar.
	 * @param custodiaTarifasCliente estructura tarifaria encontrada para el cliente. 
	 * @throws Exception lanza una excepción si hay un error 
	 * */ 
	private void calcularComisionesDepositarios(String idCliente, CustodiaEstructuraTarifaria custodiaTarifasCliente) throws Exception{
		OrdenDAO ordenDAODep = new OrdenDAO(this.dataSource);
		ordenDAODep.listarTotalOperacionesDepositarios(idCliente, fechaInicio, fechaFin);
		DataSet depositarios = ordenDAODep.getDataSet();
		BigDecimal montoComision = new BigDecimal(0);
		BigDecimal cantidadOperaciones = new BigDecimal(0);
		CustodiaComisionDepositario custodiaComision = null;
		
		Logger.info(this,"Calculando comisiones de depositarios del cliente " + idCliente);
		if (custodiaTarifasCliente != null){
			if (depositarios.count()>0){
				depositarios.first();
				while(depositarios.next()){				
					custodiaComision = custodiaTarifasCliente.getTarifaDepositario(depositarios.getValue("empres_id"));				
					if (custodiaComision != null){
						if (custodiaComision.getMontoComision()>0){
							montoComision = new BigDecimal(custodiaComision.getMontoComision());					
							if (!custodiaComision.getMonedaComision().equals(monedaLocal)){
								montoComision = this.conversionMonto(custodiaComision.getMonedaComision(), montoComision);
							}
							cantidadOperaciones = new BigDecimal(depositarios.getValue("total_operaciones"));
							calculoMesDetalle.setCantidad(cantidadOperaciones);
							calculoMesDetalle.setCantidadOperaciones(cantidadOperaciones.intValue());
							calculoMesDetalle.setMontoOperacion(montoComision.multiply(cantidadOperaciones));
							calculoMesDetalle.setNombreOperacion("Comisión " + depositarios.getValue("empres_siglas"));
							calculoMesDetalle.setTasaMonto(montoComision.doubleValue());
							calculoMesDetalle.setIdMoneda(monedaLocal);			
							calculoMesDetalle.setIdCliente(Long.parseLong(idCliente));
							calculoMesDetalle.setSignoPorcentaje(false);
							CalculoMesDao.insertarDetalle(calculoMesDetalle);
						}
					}
				}
			}		
		}
	}
	
	/**Calcula la posición global del cliente para el cálculo de la comisión
	 * 
	 * @throws Exception
	 */
	private void calcularPosicionGlobal() throws Exception{

		CustodiaDAO cDao = new CustodiaDAO(this.dataSource);
		DataSet ds = null;
		cDao.listarClientesConTitulosCustodia(idClienteCalculo);
		ds = cDao.getDataSet();
		String idCliente = "";
		
		ds.first();
		while (ds.next()){
			//Buscamos la estructura tarifaria del cliente
			calculoMesDetalle.setCantidadOperaciones(0);
			idCliente = ds.getValue("client_id");
			custodiaTarifasCliente = custodiaTarifasDao.listarEstructura(Long.parseLong(idCliente));
			if (custodiaTarifasCliente == null){
				Logger.info(this,"No hay estructura tarifaria asociada al cliente " + idCliente);
				custodiaTarifasCliente = custodiaTarifas;			
			}			
		
			Logger.info(this,"Calculando posición global del cliente " + idCliente);
			if (custodiaTarifasCliente != null){				
				//Se verifica si al cliente se le va a cobrar algo por la posición en custodia
				if (custodiaTarifasCliente.getTarifas().getMontoAnualExtranjera() > 0 || 
						custodiaTarifasCliente.getTarifas().getMontoAnualNacional() > 0 ||
						custodiaTarifasCliente.getTarifas().getPctAnualExtranjera() > 0 ||
						custodiaTarifasCliente.getTarifas().getPctAnualNacional() > 0){
		
					//Se inicilizan las variables
					TitulosCierreDAO titulosCierresDao = new TitulosCierreDAO(this.dataSource);
					titulosCierresDao.listarTitulosAFecha(idCliente, fechaInicio, fechaFin);
					BigDecimal montoExtranjero = new BigDecimal(0);
					BigDecimal montoNacional = new BigDecimal(0);
					DataSet posicion = titulosCierresDao.getDataSet();
					String monedaTitulo = "";
					BigDecimal meses = new BigDecimal(12);
					
					BigDecimal montoComision = new BigDecimal(0);	
					BigDecimal tasaMonto = new BigDecimal(0);
							
					if (posicion.count()>0){
						posicion.first();
							while(posicion.next()){
								monedaTitulo = posicion.getValue("titulo_moneda_den");
								//Se verifica si la moneda del título es diferente a la local ya que los cálculos
								//siempre deben ser en bolívares
								if (!monedaTitulo.equals(monedaLocal)){
									montoExtranjero = montoExtranjero.add(this.conversionMonto(monedaTitulo, new BigDecimal(posicion.getValue("cantidad"))));
								} else {
									montoNacional = montoNacional.add(new BigDecimal(posicion.getValue("cantidad")));
								}
							}
						}
					
					Logger.info(this,"Monto en posición extranjera " + montoExtranjero);
					Logger.info(this,"Monto en posición nacional " + montoNacional);
					
					//Obtiene la comisión a cobrar para posición extranjera si mantiene posición en esta moneda
					if (montoExtranjero.doubleValue()>0){
						if (custodiaTarifasCliente.getTarifas().getPctAnualExtranjera() > 0){
							Logger.info(this,"Buscando porcentaje a cobrar por posición en moneda extranjera");
							calculoMesDetalle.setSignoPorcentaje(true);
							montoComision = montoExtranjero.multiply(new BigDecimal(custodiaTarifasCliente.getTarifas().getPctAnualExtranjera()/100)).divide(meses,POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
							tasaMonto = new BigDecimal(custodiaTarifasCliente.getTarifas().getPctAnualExtranjera());
							Logger.info(this,"Porcentaje en posición extranjera " + tasaMonto);
							Logger.info(this,"Monto resultante por porcentaje en posición extranjera " + montoComision.doubleValue());				
						} else if (custodiaTarifasCliente.getTarifas().getMontoAnualExtranjera()>0){
							Logger.info(this,"Buscando monto a cobrar por posición en moneda extranjera");						
							calculoMesDetalle.setSignoPorcentaje(false);
							//Verificamos si la comisión no está expresada en moneda local
							if (!custodiaTarifasCliente.getTarifas().getMonedaAnualExtranjera().equals(monedaLocal)){
								Logger.info(this,"Encontrado monto en moneda extranjera");
								Logger.info(this,"Monto por comisión: " + custodiaTarifasCliente.getTarifas().getMontoAnualExtranjera());
								Logger.info(this,"Moneda: " + custodiaTarifasCliente.getTarifas().getMonedaAnualExtranjera());							
								tasaMonto = this.conversionMonto(custodiaTarifasCliente.getTarifas().getMonedaAnualExtranjera(), new BigDecimal(custodiaTarifasCliente.getTarifas().getMontoAnualExtranjera()));
								montoComision = tasaMonto.divide(meses,POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
							} else {
								Logger.info(this,"Encontrado monto en moneda local");							
								tasaMonto = new BigDecimal(custodiaTarifasCliente.getTarifas().getMontoAnualExtranjera()); 
								montoComision = tasaMonto.divide(meses,POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
							}
							Logger.info(this,"Monto comisión a cobrar en posición extranjera " + tasaMonto);
							Logger.info(this,"Monto resultante por cobro de comisión en posición extranjera " + montoComision.doubleValue());				
						}
						
						//Verificamos si ha comisión a cobrar
						if (montoComision.doubleValue() > 0){
							calculoMesDetalle.setCantidad(montoExtranjero);
							calculoMesDetalle.setMontoOperacion(montoComision);
							calculoMesDetalle.setNombreOperacion("Saldo al Cierre Moneda Extranjera");
							calculoMesDetalle.setTasaMonto(tasaMonto.doubleValue());
							calculoMesDetalle.setIdMoneda(monedaLocal);
							calculoMesDetalle.setIdCliente(Long.parseLong(idCliente));
							CalculoMesDao.insertarDetalle(calculoMesDetalle);				
						}
					}
					
					//Reiniciamos los valores a 0 de monto y tasa para el cálculo de comisión en posición nacional
					montoComision = new BigDecimal(0);
					tasaMonto = new BigDecimal(0);
		
					//Obtiene la comisión a cobrar para posición en moneda local
					if (montoNacional.doubleValue()>0){					
						if (custodiaTarifasCliente.getTarifas().getPctAnualNacional() > 0){
							calculoMesDetalle.setSignoPorcentaje(true);
							montoComision = montoNacional.multiply(new BigDecimal(custodiaTarifasCliente.getTarifas().getPctAnualNacional()/100)).divide(meses,POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
							tasaMonto = new BigDecimal(custodiaTarifasCliente.getTarifas().getPctAnualNacional());
							Logger.info(this,"Porcentaje en posición nacional " + tasaMonto);
							Logger.info(this,"Monto resultante por porcentaje en posición nacional " + montoComision.doubleValue());				
							
						} else if (custodiaTarifasCliente.getTarifas().getMontoAnualNacional()>0){
							calculoMesDetalle.setSignoPorcentaje(false);
							//Verificamos si la comisión no está expresada en moneda local
							if (!custodiaTarifasCliente.getTarifas().getMonedaAnualNacional().equals(monedaLocal)){
								tasaMonto = this.conversionMonto(custodiaTarifasCliente.getTarifas().getMonedaAnualNacional(), new BigDecimal(custodiaTarifasCliente.getTarifas().getMontoAnualNacional()));
								montoComision = tasaMonto.divide(meses,POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
							} else {
								tasaMonto = new BigDecimal(custodiaTarifasCliente.getTarifas().getMontoAnualNacional()); 
								montoComision = tasaMonto.divide(meses,POSICIONES_DECIMALES,BigDecimal.ROUND_HALF_UP);
							}
							Logger.info(this,"Monto comisión a cobrar en posición nacional " + tasaMonto);
							Logger.info(this,"Monto resultante por cobro de comisión en posición nacional " + montoComision.doubleValue());				
						}
			
						//Verificamos si ha comisión a cobrar
						if (montoComision.doubleValue() > 0){
							calculoMesDetalle.setCantidad(montoNacional);
							calculoMesDetalle.setMontoOperacion(montoComision);
							calculoMesDetalle.setNombreOperacion("Saldo al Cierre Moneda Local");
							calculoMesDetalle.setTasaMonto(tasaMonto.doubleValue());
							calculoMesDetalle.setIdMoneda(monedaLocal);
							calculoMesDetalle.setIdCliente(Long.parseLong(idCliente));
							CalculoMesDao.insertarDetalle(calculoMesDetalle);				
						}
					}
				}
			}
		}
	}
	
	/**
	 * Efectúa la conversión del monto expresado en moneda extranjera a moneda
	 * local
	 */
	private BigDecimal conversionMonto(String codigoMoneda, BigDecimal monto) throws Exception{
		return conversionMontos.convertir(codigoMoneda, monto, monedaLocal);
	}	
}