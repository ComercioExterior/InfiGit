package com.bdv.infi.logic.interfaces;

/*
 * NM26659 - TTS_537
 */
/**
 * Clase de manejo de estatus y codigos de anulacion manejados por aplicaciones INFI y Moneda Extranjera para el proceso de anulacion de operaciones vencidas
 * asociadas al producto convenio 36 
 * */
public enum EstructuraArchivoCruceOperacionesDICOMInterbancario {
	
	LONGITUD_CERO(0),
	TIPO_REGISTRO(2),
	
	CORRELATIVO_LONG(12),
	
	MONTO_DES_BS_LONG(14),
	MONTO_DES_DIV_LONG(14),
	MONEDA_DIV_DES_LONG(14),
	
	MONTO_CRE_BS_LONG(14),
	MONTO_CRE_DIV_LONG(14),
	MONEDA_DIV_CRE_LONG(14),
		
	MONTO_DEB_BS_LONG(14),
	MONTO_DEB_DIV_LONG(14),
	MONEDA_DIV_DEB_LONG(14),

	CODIGO_BCV_DIVISA_LONG(2),//TTS-546_Agregado en Desarrollo Dicom Interbancario NM26659_27/01/2018


	//VARIABLE DE CONTROL DE PROCESO EN EJECUCION (0)PROCESO DE VALIDACION DE DICOM (NO ESTAN LAS OPERACIONES EN FIRME) Y (1) PROCESO DE ADJUDICACION DE DICOM
	PROCESO_VERIFICACION_DICOM(0),
	PROCESO_ADJUDICACION_DICOM(1);
	
	    //Campos tipo constante   
	    private  int campo; //Nombre formateado del campo	    
	    
	    	    
	    EstructuraArchivoCruceOperacionesDICOMInterbancario (int campo) { 
	        this.campo =campo;	
	        
	    } //Cierre del constructor
	    
	    public int getValor() {	    	
	    	return campo; 
	    }
	    	    
}
