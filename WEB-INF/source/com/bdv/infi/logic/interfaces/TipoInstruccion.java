package com.bdv.infi.logic.interfaces;

/**Contiene las constantes de definición de tipos de instrucciones de pago*/
public interface TipoInstruccion {

	public static int CUENTA_SWIFT=1;
	public static int CUENTA_NACIONAL=2;
	public static int OPERACION_DE_CAMBIO=3;
	public static int CHEQUE=4;	
	public static int CUENTA_NACIONAL_DOLARES=5;
	
}
