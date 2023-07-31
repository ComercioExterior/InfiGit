package com.bdv.infi.logic.interfaces;

/*
 * NM26659 - TTS_537
 */
/**
 * Clase de manejo de estatus y codigos de anulacion manejados por aplicaciones INFI y Moneda Extranjera para el proceso de anulacion de operaciones vencidas
 * asociadas al producto convenio 36 
 * */
public enum EstructuraArchivoOperacionesDICOM {
	
	LONGITUD_CERO(0),
	TIPO_REGISTRO(2),
	NRO_JORNADA(4),
	ID_OPER_LONG(12),
	TIPO_OPERACION_LONG(1),
	TIPO_CLIENTE_LONG(1),
	CED_RIF_CLIENTE_LONG(9),
	NOMBRE_CLIENTE_LONG(40),
	TLF_LONG(11),
	MAIL_LONG(50),
	NUM_CTA_VEF_LONG(20),
	RETENCION_CAP_LONG(5),
	HORA_BLO_LONG(8),
	MONTO_OPER_VEF_LONG(16),  // NM11383 EXPANDIDO DE 14 a 16 26-032018  
	MONTO_OPER_VEF_LONG_ENTEROS(14), // NM11383 EXPANDIDO DE 14 a 16 26-032018
	MONTO_OPER_COM_VEF_LONG_ENTEROS(13),
	MONTO_OPER_VEF_LONG_DECIMALES(2),
	PORC_COM_LONG_ENTEROS(3),
	TASA_CAMBIO_LONG_ENTEROS(6),
	TASA_CAMBIO_LONG_DECIMALES(4),
	DIVISA_NAC_LONG(3),
	RETENCION_COM_LONG(5),
	MONTO_OPER_COM_LONG(15),
	PORC_COM_LONG(5),
	FECHA_OPER_LONG(8),
	NUM_CTA_USD_LONG(20),
	MONTO_OPER_USD_LONG(16), // NM11383 EXPANDIDO DE 14 a 16 26-032018
	CODIGO_DIVISA_LONG(3),
	CODIGO_BCV_DIVISA_LONG(4),//TTS-546_Agregado en Desarrollo Dicom Interbancario NM26659_27/01/2018
	MONTO_OPER_USD_LONG_ENTEROS(14), // NM11383 EXPANDIDO DE 14 a 16 26-032018
	MONTO_OPER_USD_LONG_DECIMALES(2),
	DIVISA_EXT_LONG(3),
	TASA_CAMBIO_LONG(10),
	FECHA_VALOR_OPER_LONG(8),
	CODIGO_RESPUESTA_LONG(1),
	NRO_OP_DEBITO_LONG(9),
	NRO_OP_CREDITO_LONG(9),
	ID_CICLO_LONG(7),
	ID_MONEDA_LONG(4),//TTS-543_DICOM MULTIMONEDA NM26659 Se agrega el manejo de la moneda asociada a la subasta 12/09/2017
	//Valores asociados a comision IGTF
	PORC_COM_IGTF_LONG(5),
	PORC_COM_IGTF_LONG_ENTEROS(3),
	PORC_COM_IGTF_LONG_DECIMALES(2),
	MONTO_COM_IGTF_LONG(15),
	MONTO_COM_IGTF_LONG_ENTEROS(13),
	MONTO_COM_IGTF_LONG_DECIMALES(2),
	//Valores asociados a Monto total de la retencion
	MONTO_TOTAL_RET_LONG(15),
	MONTO_TOTAL_RET_LONG_ENTEROS(13),
	MONTO_TOTAL_RET_LONG_DECIMALES(2),
		
	//MOTIVO_RECHAZO_LONG(35);
	
	//CABECERA ARCHIVO PREAPROBADAS
	ID_SUBASTA_LONG(4),
	FECHA_JORNADA_LONG(8),
	TOTAL_REGISTROS_DEMANDA_LONG(7),
	TOTAL_MONTO_DOLAR_DEMANDA_LONG(16), // NM11383 EXPANDIDO DE 14 a 16 26-032018  
	TOTAL_MONTO_BOLIVARES_DEMANDA_LONG(16), // NM11383 EXPANDIDO DE 14 a 16 26-032018  
	TOTAL_REGISTROS_OFERTA_LONG(7),
	TOTAL_MONTO_DOLAR_OFERTA_LONG(16), // NM11383 EXPANDIDO DE 14 a 16 26-032018  
	TOTAL_MONTO_BOLIVARES_OFERTA_LONG(16), // NM11383 EXPANDIDO DE 14 a 16 26-032018  

	//VARIABLE DE CONTROL DE PROCESO EN EJECUCION (0)PROCESO DE VALIDACION DE DICOM (NO ESTAN LAS OPERACIONES EN FIRME) Y (1) PROCESO DE ADJUDICACION DE DICOM
	PROCESO_VERIFICACION_DICOM(0),
	PROCESO_ADJUDICACION_DICOM(1),
	TIPO_PROCESO_DEMANDA(0),
	TIPO_PROCESO_OFERTA(1),
	
	PRIMERA_SECCION_LINEA_CABECERA_LONG(90),
	
	
	PRIMERA_SECCION_LINEA_DETALLE_LONG(111),
	SEGUNDA_SECCION_LINEA_DETALLE_LONG(93);
	
	    //Campos tipo constante   
	    private  int campo; //Nombre formateado del campo	    
	    
	    	    
	    EstructuraArchivoOperacionesDICOM (int campo) { 
	        this.campo =campo;	
	        
	    } //Cierre del constructor
	    
	    public int getValor() {	    	
	    	return campo; 
	    }
	    	    
}