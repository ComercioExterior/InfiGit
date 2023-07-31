package models.configuracion.generales.campos_dinamicos;


//import static com.bdv.infi.logic.interfaces.CamposDinamicosConstantes.*;

public enum tipoCampoDinamico {
	
	    TIPO_GENERAL ("General",1),
		TIPO_VENTA ("Venta",2),
		TIPO_FECHA_RANGO ("Rango Fecha",3),
		TIPO_FECHA ("Fecha",4),
		TIPO_FECHA_MAYOR ("Fecha Mayor que",5),
		TIPO_FECHA_MENOR ("Fecha Menor que",6),
		TIPO_PAIS ("Pais",7),
		TIPO_LISTA_DINAMICA("Lista Dinamica",8);	
	
	    //Campos tipo constante   
	    private  String nombreCampo; //Nombre formateado del campo	    
	    private final int  valorCampo;
	 
	    /**
	     * Constructor. Al asignarle uno de los valores posibles a una variable del tipo enumerado el constructor asigna 
	        automáticamente valores de los campos
	     */ 
	    
	    tipoCampoDinamico (String nombreCampo,int valor) { 
	        this.nombreCampo =nombreCampo;	
	        this.valorCampo=valor;
	    } //Cierre del constructor
	    
	    public String getNombreCampo() {	    	
	    	return nombreCampo; 
	    }
	    
	    public int getValorCampo() {	    	
	    	return valorCampo; 
	    }

}
