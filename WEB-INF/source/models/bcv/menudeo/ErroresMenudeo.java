package models.bcv.menudeo;

/*
 * NM29643 - INFI_TTS_443_SICADII
 */

public enum ErroresMenudeo {
	
	CLIENTE_INVALIDO ("20300"),
	INSTRUMENTO_INVALIDO ("20301"),
	//OPERADOR_INVALIDO ("20302"),
	FECHA_INVALIDO ("20304"),
	SALDO_NO_EXISTE ("20001"),
	USUARIO_NO_PERTENECE_GRUPO ("MF012"),
	//CLAVE_INVALIDA ("MV014"),
	//CLAVE_INVALIDA2 ("MV017"),
	ERROR_AUTENTICACION ("MV011"),
	SALDO_INSUFICIENTE ("20002"),
	MONTO_NO_PERMITIDO ("20003"),
	MOVIMIENTO_NO_EXISTE ("20004"),
	VENTA_SUPERA_MONTO_ASIGNADO ("20005"),
	MONTO_DEBE_SER_MAYOR_A_CERO ("20006"),
	EXCEDE_MONTO_PERMITIDO_VENTA_PERIDO ("20007"),
	EXCEDE_MONTO_PERMITIDO_VENTA_DIA ("20008"),
	//OPERADOR_NO_AUTORIZADO ("20010"),
	EXCEDE_MONTO_MAXIMO_PERMITO_MES ("20011"),
	EXCEDE_MONTO_MAXIMO_PERMITIDO_VENTA_TURISTA_PERIODO ("20012"),
	EXCEDE_MONTO_MAXIMO_PERMITIDO_COMPRA_TURISTA_PERIODO ("20013"),
	TIPO_MOVIMIENTO_NO_EXISTE ("20014"),
	MONEDA_NO_EXISTE ("20015"),
	MONTO_SUPERA_MAXIMO_PERMITIDO_VENTA ("20016"),
	TIPO_CLIENTE_NO_AUTORIZADO ("20022"),
	TIPO_MOVIMIENTO_NO_AUTORIZADO ("20023"),
	TIPO_MOVIMIENTO_NO_PERMITIDO_PARA_TIPO_INDENTIFICACION ("20024"),
	CLIENTE_EN_INVESTIGACION_POR_ILISITO_CAMBIARIO ("20025"),
	MONEDA_NO_EXISTE_2 ("20041"),
	MONTO_DEB_SER_MAYOR_O_IGUAL_AUTORIZADO ("20042"),
	CUENTA_CONVENIO_NO_CUMPLE_FORMATO ("20043"),
	ANULACION_SOLO_DEL_MISMO_DIA ("20044"),
	EXCEDE_MONTO_MAXIMO_PERMITIDO_TRANSACCION_DIARIA ("20027"),
	TIPO_MOVIMIENTO_NO_PER_PER_NATURAL ("20026"),
	TIPO_MOVIMIENTO_NO_PERMITIDO_PARA_INSTITUCION ("20028"),
	TASA_CAMBIO_INCORRECTA ("20029"),
	MOVIMIENTO_NO_EXISTE_2 ("20030"),
	ANULACION_YA_PROCESADA ("20031"),
	NO_ES_POSIBILE_ANULAR_UNA_ANULACION ("20032"),
	OPERADOR_DE_OTRA_INSTITUCION ("20033"),
	EMAIL_INVALIDO ("20034"),
	TELEFONO_NULO("20035"),
	MONTO_MONEDA_BASE_DIFERENTE_A_MONTO_USD("20036"),
	USUARIO_NULO("20037"),
	MOVIMIENTO_ANULACION_NO_PROCESADO("20038"),
	//TELEFONO_INCORRECTO("20040"),
	DEBE_TENER_UNA_COMPRA_PREVIA ("20009");
	
	
    //Campos tipo constante   
    private  String nombreAccion; //Nombre formateado del campo	    
   
    ErroresMenudeo (String nombreCampo) { 
        this.nombreAccion =nombreCampo;	
        
    } //Cierre del constructor
    
    public String getCodigoError() {	    	
    	return nombreAccion; 
    }  	    
}