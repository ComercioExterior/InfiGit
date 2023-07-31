package com.bdv.infi.logic.interfaces;

public enum EstatusOperacionesORO {
	
	ESTATUS_ERROR_VALIDACION_TOTALES(-2),
	ESTATUS_CARGADA(-1),
	ESTATUS_PREAPROBADA(0),
	ESTATUS_ERROR_DATOS(1),
	ESTATUS_VERIFICADA(2),
	ESTATUS_COBRADA(3),
	ESTATUS_LIQUIDADA(4),
	MONTO_OPER_VEF_LONG_ENTEROS(14); // NM11383 EXPANDIDO DE 14 a 16 26-032018	

    //Campos tipo constante   
    private  int campo; //Nombre formateado del campo	    
    
    	    
    EstatusOperacionesORO (int campo) { 
        this.campo =campo;	
        
    } //Cierre del constructor
    
    public int getValor() {	    	
    	return campo; 
    }
	    	    
}
