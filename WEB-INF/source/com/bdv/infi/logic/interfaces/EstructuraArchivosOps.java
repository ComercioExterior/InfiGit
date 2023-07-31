package com.bdv.infi.logic.interfaces;

/*
 * NM26659 - TTS_537
 */
/**
 * Clase de manejo de estatus y codigos de anulacion manejados por aplicaciones INFI y Moneda Extranjera para el proceso de anulacion de operaciones vencidas
 * asociadas al producto convenio 36 
 * */
public enum EstructuraArchivosOps {
	
	CODIGO_LOTE_OK("00"),
	CODIGO_LOTE_ARCH_VACIO("10"),
	CODIGO_LOTE_DESCUADRE("15"),	
	TIPO_REGISTRO_CABECERA(1,"01"),
	TIPO_REGISTRO_DETALLE(2,"02"),
	TIPO_REGISTRO_TOTALES(3,"03"),
	LONGITUD_CERO(0),
	TIPO_REGISTRO_LONG(2),
	FECHA_OPER_CABECERA_LONG(8),		
	HORA_OPER_CABECERA_LONG(6),
	TOTAL_REGISTROS_CABECERA_LONG(7),
	NRO_PROCESO_CABECERA_LONG(6),
	CODIGO_LOTE_CABECERA_LONG(2),
	CONSECUTIVO_LONG(7),
	TIPO_REGISTRO_DET_LONG(2),
	TIPO_OPERACION_LONG(1),
	COD_OPERACION_LONG(4), 
	NRO_CTA_LONG(20),			
	NRO_REF_LONG(15),		
		DIGITO_INDENT_REF(1),//Digito 9
		CODIGO_OPERACION(4),
		ORDENE_ID(8),
		DIGITO_INDENT_FINALES_REF(2),//Digito 00
	CLAVE_LIG_LONG(20),		
	MTO_OPER_LONG(15),		
	IND_RET_LONG(1),		
	NRO_RET_LONG(5),		
	MTO_RET_LONG(15),			
	COD_RETORNO_LONG(2),		
	COD_RESP_LONG(7),			
	CAMPOS_ADD_LONG(47),
		ID_OPERACION_LONG(8),//SUB-CAMPO DE CAMPOS ADICIONALES NUMERO DE OPERACION
		ID_ORDEN_PROCESO_LONG(8),//SUB-CAMPO DE CAMPOS ADICIONALES NUMERO DE ORDEN QUE SE ENCUENTRA EN PROCESO (SEA TOMA_ORDEN O ORDEN_PAGO)
		RESERVADO_LONG(31),  //SUB-CAMPO DE CAMPOS ADICIONALES
	DIVISA_LONG(3),			
	DES_RESP_LONG(100),		
	COD_RESP_RET_LONG(2),	
	DES_RESP_RET_LONG(150),
	FECHA_OPER_LONG(8),		
	HORA_OPER_LONG(8),
	TOTAL_DEBITOS_PIE_LONG(15),
	TOTAL_CREDITOS_PIE_LONG(15);     

	// Cabecera
	/*public final String TIPO_REGISTRO = "TIPO_REGISTRO";
	public final String FECHA_PROCESO = "FECHA_PROCESO";
	public final String HORA_PROCESO = "HORA_PROCESO";
	public final String TOTAL_REGISTROS = "TOTAL_REGISTROS";
	public final String NUMERO_PROCESO = "NUMERO_PROCESO";
	public final String CODIGO_VALIDADOR_LOTE = "CODIGO_VALIDADOR_LOTE";

	// Detalle
	public final String CONSECUTIVO = "CONSECUTIVO";
	public final String TIPO_OPERACION = "TIPO_OPERACION";
	public final String CODIGO_OPERACION = "CODIGO_OPERACION";	
	public final String CTA_CLIENTE = "CTA_CLIENTE";	
	public final String REFERENCIA = "REFERENCIA";
	public final String CLAVE_LIG = "CLAVE_LIG";
		
	public final String MONTO_OPERACION = "MONTO_OPERACION";	
	public final String INDICA_RETENCION = "INDICA_RETENCION";
	public final String NRO_RETENCION = "NRO_RETENCION";	
	public final String MTO_RETENCION = "MTO_RETENCION";
		
	public final String COD_RETORNO = "COD_RETORNO";
	public final String COD_RESPUESTA = "COD_RESPUESTA";
	public final String NUMERO_OPERACION_ID = "NUMERO_OPERACION_ID";
	public final String RESERVADO = "RESERVADO";
	public final String TIPO_MONEDA = "TIPO_MONEDA";

	public final String DESC_RESPUESTA = "DESC_RESPUESTA";
	public final String COD_RETORNO_RETENCION = "COD_RETORNO_RETENCION";
	public final String DESC_RESPUESTA_RETENCION = "DESC_RESPUESTA_RETENCION";
	public final String HORA_APLIC_OPERACION = "HORA_APLIC_OPERACION";
	public final String FECHA_APLIC_OPERACION = "FECHA_APLIC_OPERACION";*/

	public int campo;
	public String codLoteCabecera;
	public String tipoRegistro;
	
	EstructuraArchivosOps(int valor){
		campo=valor;
	}
	
	EstructuraArchivosOps(String valor){
		codLoteCabecera=valor;
	}
	
	EstructuraArchivosOps(int numTipoRegistro,String tipoRegistro){
		this.tipoRegistro=tipoRegistro;
	}
	
	public int getValor(){
		return campo;
	}		
	
	public String getCodigoLote(){
		return codLoteCabecera;
	}
	
	public String getTipoRegistro(){
		return tipoRegistro;
	}
}
