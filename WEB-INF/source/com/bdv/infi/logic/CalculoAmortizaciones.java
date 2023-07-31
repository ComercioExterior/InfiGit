package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.CalculoMesDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.data.CalculoMes;
import com.bdv.infi.data.CalculoMesDetalle;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de realizar el cálculo de amortizaciones que debe cancelar el banco a los clientes
 * @author nvisbal
 */
public class CalculoAmortizaciones{
	/**
	 * Variable para el logger
	 */
	private Logger logger;

	/** DataSource de conexión a base de datos */
	private DataSource dataSource;

	/**
	 * Constructor
	 * 
	 * @param DataSource
	 *            ds
	 */
	public CalculoAmortizaciones(DataSource ds) {
		this.dataSource = ds;
	}


	// Variables globales
	private Date fechaInicio = null;
	private Date fechaFin = null;
	private String idClienteCalculo = "";
	private String idTitulo = "";
	private CalculoMesDetalle calculoMesDetalle = new CalculoMesDetalle();
	private CalculoMesDAO calculoMesDao = null;
	private long idClienteProcesando = 0;
	
	//Se usa para división de valores en la obtención de comisiones
	int POSICIONES_DECIMALES = 4;

	/**
	 * Proceso que calcula los montos a cancelar por concepto de amortización de capital
	 * @param fechaInicio fecha de inicio para el cálculo de las amortizaciones
	 * @param fechaFin fecha fin para el cálculo de las amortizaciones
	 * @param idUsuario id del usuario que está efectuando la consulta
	 * @param idCliente id del cliente si se desea consultar las amortizaciones para un cliente. Si se 
	 * envia null las amortizaciones se calculan para todos los clientes
	 * @param idTitulo id del titulo para el cálculo de la amortizción 
	 * @return devuelve el id generado que identifica la consulta efectuada 
	 */
	public long CalcularAmortizaciones(Date fechaInicio, Date fechaFin, int idUsuario, String idCliente, String idTitulo)
			throws Exception {

		/*
		 * Ejemplo para el cálculo de la amortización
		 *  			  PRINAMT_8
		 *               (PCT_AMORTIZACION) PRINPAYAMT_8(Depreciacion)
		 *                                                  500*0.875=437,5      500-437,5=62.50 
		 Monto Inicial 	 % Amotizacion 	 Depreciación 		Calculo 	 Monto a pagar 
		500					0,875		-0,125				437,5		62,50 
		437,5				0,75		-0,125				328,125		109,38 
		328,125				0,625		-0,125				205,078125	123,05 
		205,078125			0,5			-0,125				102,5390625	102,54 
		102,5390625			0,375		-0,125				38,45214844	64,09 
		38,45214844			0,25		-0,125				9,613037109	28,84 
		9,613037109			0,125		-0,125				1,201629639	8,41 
		1,201629639			0			-0,125				0		1,20 
		0					-0,125		-0,125				0		- 
		*/
		
		// Se trabaja en una transaccion a base de datos
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(
				this.dataSource);		

		try {
			this.fechaInicio = fechaInicio;
			this.fechaFin = fechaFin;
			this.idTitulo = idTitulo;
			idClienteCalculo = idCliente;
			CustodiaDAO custodiaDAO = new CustodiaDAO(this.dataSource); 
			DataSet datosTitulos = null;
			BigDecimal montoBase = BigDecimal.ZERO;
			BigDecimal montoCalculo = BigDecimal.ZERO;
			SimpleDateFormat formatear	=new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
			
			// Crea una transacción y la inicia			
			logger.info(this,"Iniciando proceso de cálculo de amortizaciones...");
			logger.info(this,"Fecha inicio: " + fechaInicio + ", fecha fin: " + fechaFin);
			
			transaccion.begin();
			
			TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(this.dataSource);
			 transaccionFijaDAO.listar(TransaccionFija.CUSTODIA_AMORTIZACION);
			transaccionFijaDAO.getDataSet().first();
			transaccionFijaDAO.getDataSet().next();
			String transaccionFija = transaccionFijaDAO.getDataSet().getValue("trnfin_nombre");
			
			calculoMesDao = new CalculoMesDAO(transaccion);

			// Elimina la última consulta efectuada por el usuario
			calculoMesDao.eliminar(idUsuario,TransaccionNegocio.CUSTODIA_AMORTIZACION);

			logger.info(this,"Eliminando consultas del usuario: "+ idUsuario);

			// Crea la cabecera de la consulta
			CalculoMes calculoMes = new CalculoMes();
			calculoMes.setFechaDesde(fechaInicio);
			calculoMes.setFechaHasta(fechaFin);
			calculoMes.setIdUsuario(idUsuario);
			calculoMes.setIdTransaccion(TransaccionNegocio.CUSTODIA_AMORTIZACION);

			calculoMesDao.insertar(calculoMes);
			
			// Objeto Calculo MES
			calculoMesDetalle = new CalculoMesDetalle();
			calculoMesDetalle.setIdCalculoMes(calculoMes.getIdCalculoMes());
			
			//Busca los títulos que deben amortizarce según las fechas indicadas
			custodiaDAO.listarTitulosAmortizar(this.fechaInicio, this.fechaFin, idCliente, idTitulo);
			if (custodiaDAO.getDataSet().count() == 0) {
				new Exception ("No hay títulos para amortizar en las fechas indicadas");
			} 
			datosTitulos = custodiaDAO.getDataSet();
			
			if (datosTitulos.count()>0){
				datosTitulos.first();				
			}
			
			//Recorremos el dataSet y calculamos el pago			
			while (datosTitulos.next()){
				//Verificamos si ya se ha amortizado
				//if (datosTitulos.getValue("titulo_monto_ult_amortizacion")!=null){
					//montoBase = new BigDecimal(datosTitulos.getValue("titulo_monto_ult_amortizacion"));	
				//} else {
					montoBase = new BigDecimal(datosTitulos.getValue("titcus_cantidad"));
				//}
				logger.info(this,"Calculando amortización para el cliente " + datosTitulos.getValue("client_id") + " y título " + datosTitulos.getValue("titulo_id"));				
				logger.info(this,"Monto base de amortización " + montoBase);
				
				//Verifica el porcentaje de amorización. Si es igual a 1 indica que se está pagando la
				//amortización al vencimiento
				
				montoCalculo = montoBase.multiply(new BigDecimal(datosTitulos.getValue("depreciacion")));					
				logger.info(this,"Porcentaje de amortización " + datosTitulos.getValue("depreciacion"));					
				logger.info(this,"Monto de amortización " + montoCalculo);
				
				if (montoCalculo.longValue() > 0){
					idClienteProcesando = Long.parseLong(datosTitulos.getValue("client_id"));
					calculoMesDetalle.setIdCliente(idClienteProcesando);
					calculoMesDetalle.setIdMoneda(datosTitulos.getValue("titulo_moneda_den"));
					calculoMesDetalle.setIdTitulo(datosTitulos.getValue("titulo_id"));
					calculoMesDetalle.setCantidad(montoBase);
					calculoMesDetalle.setNombreOperacion(transaccionFija);				
					calculoMesDetalle.setFechaInicio(formatear.parse(datosTitulos.getValue("fecha_inicio_amortizacion")));
					calculoMesDetalle.setFechaFin(formatear.parse(datosTitulos.getValue("fecha_fin_amortizacion")));
					calculoMesDetalle.setMontoOperacion(montoCalculo.setScale(2, BigDecimal.ROUND_HALF_UP));
					calculoMesDetalle.setTasaMonto(Double.parseDouble(datosTitulos.getValue("pct_amortizacion")));
					
					calculoMesDao.insertarDetalle(calculoMesDetalle);
				}
			}			
			logger.info(this,"Cerrando cálculo de amortizaciones...");
			transaccion.end();
			return calculoMes.getIdCalculoMes();

		} catch (Exception e) {
			logger.info(this,"Efectuando rollback de la operación de cálculo de amortización");
			transaccion.rollback();
			logger.error(this,e.getMessage()+ Utilitario.stackTraceException(e));
			throw e;
		}
	}// FIN calcularAmortizacion	
}