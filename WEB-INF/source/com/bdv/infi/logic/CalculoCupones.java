package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;
import megasoft.DataSet;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.CalculoMesDAO;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.PagoCuponesDao;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.data.CalculoMes;
import com.bdv.infi.data.CalculoMesDetalle;
import com.bdv.infi.data.CustodiaComisionTitulo;
import com.bdv.infi.data.CustodiaEstructuraTarifaria;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.ConversionMontos;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de realizar el cálculo de los montos que debe cancelar
 * el banco por concepto de pago de cupones a clientes que posean títulos
 * en custodia.
 * @author Nelson Visbal
 */

public class CalculoCupones {

	/**
	 * Variable para el logger
	 */
	private Logger logger = Logger.getLogger(CalculoCupones.class);

	/** DataSource de conexión a base de datos */
	private DataSource dataSource;

	// Variables globales
	Date fechaInicio = null;
	Date fechaFin = null;
	String transaccionFija = "";
	String  transaccionFijaAmortizacion = "";
	ConversionMontos conversionMontos = null;
	HashMap<String,Double> totalAmortizado = new HashMap<String,Double>(); 
	
	/**
	 * Constructor
	 * 
	 * @param DataSource
	 *            ds
	 */
	public CalculoCupones(DataSource ds) {
		this.dataSource = ds;
	}
	
	/**Proceso que calcula los montos que debe cancelar el banco por concepto de 
	 * pago de cupones a clientes que posean títulos en custodia
	 * @param fechaInicio fecha de inicio para la búsqueda de los títulos a vencer en esa fecha
	 * @param fechaFin fecha fin para la búsqueda de los títulos a vencer en esa fecha
	 * @param idUsuario id del usuario que está efectuando la consulta
	 * @param idCliente id del cliente si se desea consultar el pago para un cliente. Si se 
	 * envia null se calcularán los montos para todos los clientes que tengan posición en custodia
	 * @return devuelve el id generado que identifica la consulta efectuada 
	 * */
	public long calcularCupones(Date fechaInicio, Date fechaFin, int idUsuario, String idCliente,  String[] titulos)
	throws Exception {
		
		// Se trabaja en una transaccion a base de datos
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(
				this.dataSource);
		conversionMontos = new ConversionMontos(this.dataSource);		
		CalculoMes calculoMes = new CalculoMes();
		CalculoMesDAO calculoMesDao = null;
		try {			
			CustodiaComisionTitulo custodiaComisionTitulo = null;
			CalculoMesDetalle calculoMesDetalle;			
			BigDecimal baseDiferencia;
			BigDecimal totalCalculo;			
			String baseCalculo = "";
			String fechaInicioCupon = "";
			String fechaFinCupon = "";
			PagoCuponesDao pagoCuponesDao = new PagoCuponesDao(this.dataSource);
			TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(this.dataSource);			
			this.fechaInicio = fechaInicio;
			this.fechaFin = fechaFin;
			SimpleDateFormat formatear	=new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
			BigDecimal montoAmortizacion = new BigDecimal(0);
			double valorResidual = 0;
			
			//Verifica que no haya en evento de cupón ejecutandose
			ProcesosDAO procesosDAO = new ProcesosDAO(this.dataSource);
			
			procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.PAGO_CUPON);			
						
			logger.info("\n\n\nIniciando proceso de cálculo de cupones...");
			
			// Carga la estructura tarifaria general
			CustodiaEstructuraTarifariaDAO custodiaTarifasDao = new CustodiaEstructuraTarifariaDAO(this.dataSource);
			CustodiaEstructuraTarifaria custodiaTarifas = custodiaTarifasDao.listarEstructura();
			
			logger.info("Tarifas de titulos\n");
			if (custodiaTarifas!=null && custodiaTarifas.getTitulos()!=null){
			   logger.info(custodiaTarifas.getTitulos().toString());
			}
			
			transaccion.begin();
						
			//Busca los cupones a vencer según la fecha de inicio y fecha fin
			pagoCuponesDao.listarCupones(fechaInicio, fechaFin, idCliente, titulos);
			
			DataSet dataSetTitulos = pagoCuponesDao.getDataSet();
			
			if (dataSetTitulos.count()>0){

				//Transaccion fija para cupón
				transaccionFijaDAO.listar(TransaccionFija.DEPOSITO_CUPON);
				transaccionFijaDAO.getDataSet().first();
				transaccionFijaDAO.getDataSet().next();
				transaccionFija = transaccionFijaDAO.getDataSet().getValue("trnfin_nombre");

				//Transaccion fija para amortización				
				transaccionFijaDAO.listar(TransaccionFija.CUSTODIA_AMORTIZACION);
				transaccionFijaDAO.getDataSet().first();
				transaccionFijaDAO.getDataSet().next();
				transaccionFijaAmortizacion = transaccionFijaDAO.getDataSet().getValue("trnfin_nombre");				
				
				calculoMesDao = new CalculoMesDAO(transaccion);

				// Elimina la última consulta efectuada por el usuario

				calculoMesDao.eliminar(idUsuario,TransaccionNegocio.PAGO_CUPON);

				logger.info("Eliminando consultas del usuario:"+ idUsuario);

				// Crea la cabecera de la consulta
				calculoMes.setFechaDesde(fechaInicio);
				calculoMes.setFechaHasta(fechaFin);
				calculoMes.setIdUsuario(idUsuario);
				calculoMes.setIdTransaccion(TransaccionNegocio.PAGO_CUPON);

				calculoMesDao.insertar(calculoMes);

				// Objeto Calculo MES
				calculoMesDetalle = new CalculoMesDetalle();
				calculoMesDetalle.setIdCalculoMes(calculoMes.getIdCalculoMes());
				
				logger.info("Id de cálculo " + calculoMes.getIdCalculoMes());
				
				
				//Recorre cada uno de los títulos encontrados para calcular las comisiones a pagar
				dataSetTitulos.first();
				while(dataSetTitulos.next()){
					baseCalculo	= dataSetTitulos.getValue("base_calculo");
					
					logger.debug("Base de calculo--->"+baseCalculo);
					//Obtiene la fecha de inicio del cupón
					logger.info("Procesando título " + dataSetTitulos.getValue("titulo_id") + " del cliente " + dataSetTitulos.getValue("client_id"));					
					
					fechaInicioCupon =  dataSetTitulos.getValue("fecha_inicio_pago_cupon");
					fechaFinCupon   =  dataSetTitulos.getValue("fecha_fin_pago_cupon");
					baseDiferencia	= Utilitario.cuponesDiferenciaBaseDias(formatear.parse(fechaInicioCupon), formatear.parse(fechaFinCupon), baseCalculo,0);
					logger.debug("BaseDiferencia--->"+baseDiferencia);
					
					//Busca las amortizaciones históricas generadas al título
					valorResidual = this.valorNominal(dataSetTitulos.getValue("titulo_id"), formatear.parse(fechaInicioCupon), pagoCuponesDao, dataSetTitulos.getValue("titcus_cantidad"));					
					totalCalculo	= Utilitario.calculoCupones(baseDiferencia,valorResidual,new BigDecimal(dataSetTitulos.getValue("intereses_cupones")));
						
					logger.debug("Total Calculo--->"+totalCalculo);
					if (!dataSetTitulos.getValue("moneda_pago").equals(dataSetTitulos.getValue("titulo_moneda_den"))){
						totalCalculo = conversionMontos.convertirTasaPagoTitulos(dataSetTitulos.getValue("titulo_moneda_den"), totalCalculo, dataSetTitulos.getValue("moneda_pago"));
						
						logger.debug("Total calculo conversion montos--->"+totalCalculo);
					}
					
					//Si el monto generado es mayor a 0 se guarda el registro
					if (totalCalculo.doubleValue()>0){
						
						//Reinicia la comisión ya que se usa el mismo objeto para todos los cálculos
						calculoMesDetalle.setComisionOperacion(new BigDecimal(0));
										
						//Calcula comisión para el título
						if (custodiaTarifas != null){
							if (custodiaTarifas.getTitulos().containsKey(dataSetTitulos.getValue("titulo_id"))){
								logger.info("Encontrado titulo para comisión " + dataSetTitulos.getValue("titulo_id"));
								
								//Se obtiene la clase
								custodiaComisionTitulo = custodiaTarifas.getTitulos().get(dataSetTitulos.getValue("titulo_id"));
								if (custodiaComisionTitulo.getPctComision()>0){									
									BigDecimal calculoComision = totalCalculo.multiply(new BigDecimal(custodiaComisionTitulo.getPctComision()));
									calculoComision = calculoComision.divide(new BigDecimal(100));
									calculoMesDetalle.setComisionOperacion(calculoComision);
									totalCalculo = totalCalculo.subtract(calculoComision);
									logger.info("Restando comisión por monto " + calculoComision + " al " + custodiaComisionTitulo.getPctComision() + "%");									
								} else if (custodiaComisionTitulo.getMontoComision()>0){
									logger.info("Restando comisión al título " + dataSetTitulos.getValue("titulo_id") + " del cliente " + dataSetTitulos.getValue("client_id") + " con monto " + custodiaComisionTitulo.getMontoComision() + " y moneda " + custodiaComisionTitulo.getMonedaComision());
									//Se verifica si la moneda de pago es la misma para el pago del título
									if (dataSetTitulos.getValue("moneda_pago").equals(custodiaComisionTitulo.getMonedaComision())){
										totalCalculo = totalCalculo.subtract(new BigDecimal(custodiaComisionTitulo.getMontoComision()));
										calculoMesDetalle.setComisionOperacion(new BigDecimal(custodiaComisionTitulo.getMontoComision()));
									} else {
										logger.info("Convirtiendo monto " + custodiaComisionTitulo.getMontoComision() + " expresado en " + custodiaComisionTitulo.getMonedaComision() + " a la moneda " + dataSetTitulos.getValue("moneda_pago"));										
										BigDecimal calculoComision = conversionMontos.convertir(custodiaComisionTitulo.getMonedaComision(),new BigDecimal(custodiaComisionTitulo.getMontoComision()),dataSetTitulos.getValue("moneda_pago"));
										logger.info(" Monto de conversión resultante " + calculoComision.toString());
										totalCalculo = totalCalculo.subtract(calculoComision);
										calculoMesDetalle.setComisionOperacion(calculoComision);										
									}
								}
							}
						}
						
						calculoMesDetalle.setDiasCalculo(baseDiferencia.intValue());
						calculoMesDetalle.setCantidad(new BigDecimal(valorResidual));
						calculoMesDetalle.setFechaFin(formatear.parse(fechaFinCupon));
						calculoMesDetalle.setFechaInicio(formatear.parse(fechaInicioCupon));
						calculoMesDetalle.setIdCliente(Long.parseLong(dataSetTitulos.getValue("client_id")));
						calculoMesDetalle.setIdTitulo(dataSetTitulos.getValue("titulo_id"));
						calculoMesDetalle.setIdMoneda(dataSetTitulos.getValue("moneda_pago"));
						
						calculoMesDetalle.setMontoOperacion(totalCalculo.setScale(2, BigDecimal.ROUND_HALF_UP));
						calculoMesDetalle.setNombreOperacion(transaccionFija + " " + dataSetTitulos.getValue("titulo_id"));
						calculoMesDetalle.setTasaMonto(Double.parseDouble(dataSetTitulos.getValue("intereses_cupones")));						
								
						//Verifica si no hay mas cupones por pagar
						if (dataSetTitulos.getValue("cuenta_cupones_proximos").equals("0")){
							calculoMesDetalle.setSignoPorcentaje(true);
						} else {
							calculoMesDetalle.setSignoPorcentaje(false);
						}
						calculoMesDao.insertarDetalle(calculoMesDetalle);						
						
						//Verifica si hay amortización para el período
						if (!dataSetTitulos.getValue("amortizable").equals("0")){
							logger.info("Calculando amortización para el título ");
							montoAmortizacion = new BigDecimal(dataSetTitulos.getValue("titcus_cantidad")).multiply(new BigDecimal(dataSetTitulos.getValue("amortizable"))).multiply(new BigDecimal(-1));
							
							if (!dataSetTitulos.getValue("moneda_pago").equals(dataSetTitulos.getValue("titulo_moneda_den"))){
								montoAmortizacion = conversionMontos.convertirTasaPagoTitulos(dataSetTitulos.getValue("titulo_moneda_den"), montoAmortizacion, dataSetTitulos.getValue("moneda_pago"));									
								logger.debug("Total calculo conversion montos--->"+totalCalculo);
							}
							
							calculoMesDetalle.setDiasCalculo(0);
							calculoMesDetalle.setCantidad(new BigDecimal(dataSetTitulos.getValue("titcus_cantidad")));
							calculoMesDetalle.setFechaInicio(formatear.parse(fechaInicioCupon));								
							calculoMesDetalle.setFechaFin(formatear.parse(fechaFinCupon));
							calculoMesDetalle.setIdCliente(Long.parseLong(dataSetTitulos.getValue("client_id")));
							calculoMesDetalle.setIdTitulo(dataSetTitulos.getValue("titulo_id"));
							calculoMesDetalle.setIdMoneda(dataSetTitulos.getValue("moneda_pago"));
							
							calculoMesDetalle.setMontoOperacion(montoAmortizacion.setScale(2, BigDecimal.ROUND_HALF_UP));
							calculoMesDetalle.setNombreOperacion(transaccionFijaAmortizacion + " " + dataSetTitulos.getValue("titulo_id"));
							calculoMesDetalle.setTasaMonto(Double.parseDouble(dataSetTitulos.getValue("amortizable"))*-1);
							calculoMesDao.insertarDetalle(calculoMesDetalle);											
						}
						
						
					}					
				}
			}else{
				throw new Exception("No hay títulos a vencer en las fechas indicadas");
			}
			logger.info("Cerrando proceso de cálculo de cupones...");
			transaccion.end();			
		} catch (Exception e){			
			logger.error("Error en el procesamiento del cálculo de cupones" + e.getMessage()+" "+ Utilitario.stackTraceException(e));			
			transaccion.rollback();
			throw e;	
		} finally {
			if (calculoMesDao != null){
				calculoMesDao.closeResources();
				calculoMesDao.cerrarConexion();
			}
		}
		return calculoMes.getIdCalculoMes();
	}
	
	/**
	 * Usa una cache para almacenar el porcentaje amortizado pagado a la fecha del cupón
	 * @param idTitulo id del título a buscar 
	 * @param fecha fecha de inicio del cupón del título a pagar
	 * @param pagoCuponesDao el dao instanciado para la busqueda de cupones
	 * @param valorNominalCustodia valor nominal que posee el cliente en custodia del título a calcular
	 */
	private double valorNominal(String idTitulo, Date fecha, PagoCuponesDao pagoCuponesDao, String valorNominalCustodia) throws Exception{
		/*El id está conformado por id del título concatenado con la fecha, ya que el estimado se puede hacer
		por rango de fechas*/
		String id = idTitulo + fecha.getDate()+fecha.getMonth()+fecha.getYear();
		double amortizacionAcumulada = 0; //Amortización acumulada
		
		//Busca si existe en el cache
		if (totalAmortizado.containsKey(id)){
			amortizacionAcumulada = totalAmortizado.get(id);
		} else {			
			amortizacionAcumulada = pagoCuponesDao.obtenerAmortizacionHistorica(idTitulo, fecha);
			totalAmortizado.put(id, amortizacionAcumulada);
		}
		double valorResidual = Integer.parseInt(valorNominalCustodia) - (Integer.parseInt(valorNominalCustodia) * amortizacionAcumulada);
		return valorResidual;
	}
}
