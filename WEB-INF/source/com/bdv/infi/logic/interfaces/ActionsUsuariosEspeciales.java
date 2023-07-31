package com.bdv.infi.logic.interfaces;

/**Contiene las constantes para los actions de usuarios especiales*/
public interface ActionsUsuariosEspeciales {
	
	/**Cambio de Financiamiento */
	public static String CAMBIO_FINANCIAMIENTO="/cambio_financiamiento";
	
	/**Cambio de Comisión */
	public static String CAMBIO_COMISION="/cambio_comision";
	
	/**Cambio de Blotter */
	public static String CAMBIO_BLOTTER="/cambio_blotter";
	
	/**
	 * Cambio de Vehiculo */
	public static String CAMBIO_VEHICULO="/cambio_vehiculo";
	
	/**
	 * Cambio de precio de recompra en venta de titulos
	 */
	public static String CAMBIO_PRECIO_VENTA_TITULOS = "/cambio_precio_venta_titulos";
	
	/**
	 * Cambio de precio de recompraq en toma de orden
	 */
	public static String CAMBIO_PRECIO_TOMA_ORDEN = "/cambio_precio_toma_orden";
	
	/**Permite ingresar instrucciones de pago para recompra**/
	public static String INGRESO_INSTRUCCIONES_PAGO = "/ingreso_instrucciones_pago";
}
