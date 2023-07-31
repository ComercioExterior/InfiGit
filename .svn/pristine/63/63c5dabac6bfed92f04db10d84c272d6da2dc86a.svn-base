package com.bdv.infi.util;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.TasaCambioCierreDAO;
import com.bdv.infi.data.TasaCambioCierre;

/**Clase que se encarga de convertir los montos enviados a la moneda indicada*/
public class ConversionMontos {

	/**Variable usada para el manejo de cache de tasas de cambio*/
	HashMap<String, TasaCambioCierre> tasasDeCambio = new HashMap<String, TasaCambioCierre>();
	
	/**Origen de datos*/
	DataSource dataSource;
	String monedaLocal = "";
	TasaCambioCierre tasaCambioCierre = new TasaCambioCierre();
	TasaCambioCierreDAO tasaCambioCierreDAO = null;	
		
	Logger logger = Logger.getLogger(ConversionMontos.class);
	/**
	 * Constructor
	 * @param ds Origen de datos
	 * @throws Exception lanza una excepción si hay un error
	 */
	public ConversionMontos(DataSource ds) throws Exception {
		this.dataSource = ds;
		MonedaDAO monedaDao = new MonedaDAO(this.dataSource);
		monedaLocal = monedaDao.listarIdMonedaLocal();
		tasaCambioCierreDAO = new TasaCambioCierreDAO(this.dataSource);		
	}
	
	/**
	 * Método que convierte un monto de una moneda a otra buscando las tasas de cambio de cierre.   
	 * @param codigoMonedaOrigen codigo de la moneda en que se encuentra el monto que se desea convertir
	 * @param montoAConvertir monto que se desea convertir
	 * @param codigoMonedaDestino codigo de la moneda al que se desea convertir el monto
	 * @return Monto convertido
	 */
	public BigDecimal convertir(String codigoMonedaOrigen, BigDecimal montoAConvertir, String codigoMonedaDestino) throws Exception{
		if (logger.isDebugEnabled()){
			logger.debug("Conviertiendo monto...");
			logger.debug("Monto original: " + montoAConvertir);		
		}
		
		BigDecimal montoResultante = null; 
		//Verifica monedas para obtener la tasa correcta 
		if (codigoMonedaDestino.equals(monedaLocal)){
			//Se busca tasa de moneda origen
			obtenerTasaDeCambio(codigoMonedaOrigen);
			montoResultante = montoAConvertir.multiply(new BigDecimal(tasaCambioCierre.getTasaCambioCompra()));			
		} else if (codigoMonedaOrigen.equals(monedaLocal)){
			obtenerTasaDeCambio(codigoMonedaDestino);
			montoResultante = montoAConvertir.divide(new BigDecimal(tasaCambioCierre.getTasaCambioCompra()),BigDecimal.ROUND_HALF_EVEN);			
		} else {
			/*Cuando ninguna de las monedas es la local se convierte primero a bolívares y después 
			a la moneda destino*/
			obtenerTasaDeCambio(codigoMonedaOrigen);
			BigDecimal montoBolivares = montoAConvertir.multiply(new BigDecimal(tasaCambioCierre.getTasaCambioCompra()));
			obtenerTasaDeCambio(codigoMonedaDestino);
			montoResultante = montoBolivares.divide(new BigDecimal(tasaCambioCierre.getTasaCambioCompra()));
		}
		logger.debug("Monto resultante:" + montoResultante);		
		return montoResultante;
	}
	
	/**
	 * Método que convierte un monto de una moneda a otra buscando las tasas de cambio de cierre a tasa especial. 
	 * Se toma la tasa de cambio y no la tasa de cambio para la compra   
	 * @param codigoMonedaOrigen codigo de la moneda en que se encuentra el monto que se desea convertir
	 * @param montoAConvertir monto que se desea convertir
	 * @param codigoMonedaDestino codigo de la moneda al que se desea convertir el monto
	 * @return Monto convertido
	 */
	public BigDecimal convertirTasaPagoTitulos(String codigoMonedaOrigen, BigDecimal montoAConvertir, String codigoMonedaDestino) throws Exception{
		if (logger.isDebugEnabled()){
			logger.debug("Conviertiendo monto...");
			logger.debug("Monto original: " + montoAConvertir);		
		}
		
		BigDecimal montoResultante = null; 
		//Verifica monedas para obtener la tasa correcta 
		if (codigoMonedaDestino.equals(monedaLocal)){
			//Se busca tasa de moneda origen
			obtenerTasaDeCambio(codigoMonedaOrigen);
			montoResultante = montoAConvertir.multiply(new BigDecimal(tasaCambioCierre.getTasaCambio()));			
		} else if (codigoMonedaOrigen.equals(monedaLocal)){
			obtenerTasaDeCambio(codigoMonedaDestino);
			montoResultante = montoAConvertir.divide(new BigDecimal(tasaCambioCierre.getTasaCambio()),BigDecimal.ROUND_HALF_EVEN);			
		} else {
			/*Cuando ninguna de las monedas es la local se convierte primero a bolívares y después 
			a la moneda destino*/
			obtenerTasaDeCambio(codigoMonedaOrigen);
			BigDecimal montoBolivares = montoAConvertir.multiply(new BigDecimal(tasaCambioCierre.getTasaCambio()));
			obtenerTasaDeCambio(codigoMonedaDestino);
			montoResultante = montoBolivares.divide(new BigDecimal(tasaCambioCierre.getTasaCambio()));
		}
		logger.debug("Monto resultante:" + montoResultante);		
		return montoResultante;
	}	
	
	/** Busca y obtiene la casa de cambio según la divisa */
	private void obtenerTasaDeCambio(String codigoMoneda) throws Exception {
		tasaCambioCierre = null;
		// Busca el objeto en el hashmap, de no encontrarse lo busca en la tabla
		if (!tasasDeCambio.containsKey((codigoMoneda))) {
			tasaCambioCierre = tasaCambioCierreDAO.listarTasas(codigoMoneda);
			if (tasaCambioCierre != null) {
				tasasDeCambio.put(codigoMoneda, tasaCambioCierre);
			} else {
				throw new Exception("Tasa de cambio para la divisa "
						+ codigoMoneda + " no ha sido encontrada ");
			}
		} else {
			tasaCambioCierre = tasasDeCambio.get(codigoMoneda);
		}
		logger.debug("Tasa de cambio:" + tasaCambioCierre.getTasaCambio());		
	}
	
	/**
	 * Obtiene la tasa de cambio de cierre para el id de la moneda recibido
	 * @param codigoMoneda codigo de la moneda que se desea buscar
	 * @return objeto tasaCambioCierre con la información. Retorna null en caso de no existir
	 * @throws Exception lanza una excepción en caso de error*/
	public TasaCambioCierre obtenerTasaDeCambioCierre(String codigoMoneda) throws Exception{
		obtenerTasaDeCambio(codigoMoneda);
		return tasasDeCambio.get(codigoMoneda);
	}
}
