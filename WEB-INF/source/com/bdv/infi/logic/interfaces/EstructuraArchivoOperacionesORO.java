package com.bdv.infi.logic.interfaces;

/*
 * NM11383 ALEXANDER RINCÓN- 10/09/2018 CERTIFICADO EN ORO
 */
/**
 * Clase para el manejo de la estructuras de Archivos enviadas por BCV
 * para el producto de Certificados en ORO en la aplicación INFI 
 * */
public enum EstructuraArchivoOperacionesORO {
	
	LONGITUD_CERO(0),
	TIPO_REGISTRO(2),
	NUM_OPER_LONG(12),
	NRO_JORNADA(8),
	TIPO_CLIENTE_LONG(1),
	CED_RIF_CLIENTE_LONG(9),
	NUM_CTA_VEF_LONG(20),
	MONTO_OPER_VEF_LONG(16), 
	MONTO_OPER_VEF_LONG_ENTEROS(14), 
	MONTO_OPER_VEF_LONG_DECIMALES(2),
	ID_CICLO_LONG(7),
	//VARIABLE DE CONTROL DE PROCESO EN EJECUCION (0)PROCESO DE VALIDACION DE ORO (NO ESTAN LAS OPERACIONES EN FIRME) Y (1) PROCESO DE ADJUDICACION DE ORO
	PROCESO_VERIFICACION_ORO(0),
	DIVISA_EXT_LONG(3);
	
	//Campos tipo constante   
	    private  int campo; //Nombre formateado del campo	    
	    
	    	    
	    EstructuraArchivoOperacionesORO (int campo) { 
	        this.campo =campo;	
	        
	    } //Cierre del constructor
	    
	    public int getValor() {	    	
	    	return campo; 
	    }
	    	    
}
