package models.bcv.alto_valor;

/*
 * NM29643 - INFI_TTS_443_SICADII
 */

public enum ErroresAltoValor {
	
	OFERTA_POSEE_UN_PACTO ("20041"),
	DEMANDA_DIFERENTE_INSTITUCION ("20042"),
	DEMANDA_POSEE_UN_PACTO ("20043"),
	DEMANDA_NO_PUEDE_ACTUALIZAR ("20044"),
	CIERRE_DEL_BANCO_NO_EXISTE ("20000"),
	USUARIO_NULO ("20001"),
	BANCO_NULO ("20002"),
	FALLA_REGISTRO_BANCO_CIERRE ("20003"),
	RIF_INCORRECTO ("20004"),
	NOMBRE_NULO ("20005"),
	MONEDA_NULA ("20006"),
	MONTO_MENOR_A_CERO ("20007"),
	TASA_MENOR_A_CERO ("20008"),
	REFERENCIA_INSTITUCION_NULA ("20009"),
	//JORNADA_NO_EXISTE ("20010"),
	BANCO_NO_EXISTE ("20011"),
	NO_SE_PUDO_REGISTRAR_OFERTA ("20012"),
	JORNADA_NO_EXISTE2 ("20013"),
	OFERTA_NO_EXISTE ("20014"),
	//JORNADA_NO_PERMITE_CARGAR_PACTO_AUN ("20015"),
	OFERTA_NO_ES_CALIDAD ("20016"),
	FECHA_JORNADA_NULA ("20017"),
	FECHA_INICIO_JORNADA_NULA ("20018"),
	FECHA_FIN_JORNADA_NULA ("20019"),
	TASA_BANDA_MINIMA_MENOR_A_CERO ("20020"),
	TASA_BANDA_MAXIMA_MENOR_A_CERO ("20021"),
	//JORNADA_NO_PUDO_SER_REGISTRADA ("20022"),
	//JORNADA_NO_PUEDE_ACTUALIZARSE ("20023"),
	//JORNADA_CERRADA ("20024"),
	//JORNADA_NO_ESTA_EN_BACKOFFICE ("20025"),
	//JORNADA_NO_ACTIVA ("20026"),
	OFERTA_NO_CORRESPONDE_CON_JORNADA ("20027"),
	OFERTA_NO_PERTENECE_A_LA_INSTITUCION ("20028"),
	INSTITUCION_NO_AUTORIZADA ("20029"),
	NO_EXISTE_INFORMACION_PARA_EL_BANCO ("20030"),
	//NO_SE_PUDO_REGISTRAR_PRE_CIERRE_PARA_OFERTA ("20031"),
	//MAS_DE_UNA_JORNADA_ACTIVA ("20032"),
	//PACTO_NO_PUDO_SER_REGISTRADO ("20033"),
	//TIPO_TRANSACCION_NO_VALIDA_SALDO ("20034"),
	//DEMANDA_NO_PUDO_SER_REGISTRADA ("20035"),
	OFERTA_Y_DEMANDA_DIFERENTE_INSTITUCION ("20036"),
	//OFERTA_Y_DEMANDA_DIFERENTE_JORNADA ("20037"),
	//DEMANDA_NO_EXISTE ("20039"),
	//DEMANDA_YA_FUE_PACTADA ("20040"),
	TASA_OFERTA_DIFERENTE_A_LA_DEMANDA ("20038"),
	OFERTA_DEBE_SER__DE_MISMA_INSTITUCION ("20046"), 
	SALDO_OFERTA_INSUFICIENTE_PARA_DEMANDA ("20047");
	
    //Campos tipo constante   
    private  String nombreAccion; //Nombre formateado del campo	    
   
    ErroresAltoValor (String nombreCampo) { 
        this.nombreAccion =nombreCampo;	
        
    } //Cierre del constructor
    
    public String getCodigoError() {	    	
    	return nombreAccion; 
    }  	    
}