package com.bdv.infi.logic.interfaces;

/**Constantes que hacen referencia a los id correspondientes a 
 * las fechas valores registradas*/
public interface FechaValor {

	/**Id de fecha valor Venta de títulos*/
	public static final int VENTA_TITULOS = 1;

	/**Id de fecha valor recompra de títulos*/
	public static final int RECOMPRA_TITULOS = 2;
	
	/**Id de fecha valor Gestión de pago con operación de cambio*/
	public static final int PAGO_CHEQUES = 3;
	
	/**Id de fecha valor Gestión de pago con cheques*/
	public static final int OPERACION_CAMBIO = 4;
	
	/**Id de fecha valor Gestión de pago transferencia swift*/
	public static final int TRANSFERENCIA_SWIFT = 5;	
	
	/**Id de fecha valor Gestión de pago transferencia nacional*/
	public static final int TRANSFERENCIA_NACIONAL = 6;	
	
	/**Id de fecha valor para toma de orden modelo SITME*/
	public static final int DEBITO_TOMA_ORDEN_SITME = 7;	
	
	/**Id de fecha valor recompra de títulos SITME*/
	public static final int RECOMPRA_TITULOS_SITME = 8;
	
	/**Id de fecha valor Venta de títulos Sitme*/
	public static final int VENTA_TITULOS_SITME = 9;
	
	/**Id de fecha valor Pago de Cupones*/
	public static final int PAGO_CUPON = 10;
	
}
