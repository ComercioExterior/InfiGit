package com.bdv.infi.data;

/** 
 * Tipo de bloqueo de t&iacute;tulos configurados
 */
public class TipoBloqueo extends com.bdv.infi.data.DataRegistro {

/**
 * Status del bloqueo
 */
private int status;

/**
 * Tipo de bloqueo
 */
private String tipo;

/**
 * Descripcion del bloqueo
 */
private String descripcion;

/**Indica si el bloqueo es para ser usado con ordenes financiadas */
private int inBloqueo;
/* Indica La aprobacion por financiamiento*/
private int AprobacionFinanciamiento;



/**Indica si el tipo de bloqueo es para ser usado cuando el financiamiento sea otorgado */
private boolean BloqueoFinanciado;


 
/**
 * Getter of the property <tt>status</tt>
 *
 * @return Returns the status.
 * 
 */
public int getStatus()
{
	return status;
}

/**
 * Setter of the property <tt>status</tt>
 *
 * @param status The status to set.
 *
 */
public void setStatus(int status ){
	this.status = status;
}


 
/**
 * Getter of the property <tt>descripcion</tt>
 *
 * @return Returns the descripcion.
 * 
 */
public String getDescripcion()
{
	return descripcion;
}

/**
 * Setter of the property <tt>descripcion</tt>
 *
 * @param descripcion The descripcion to set.
 *
 */
public void setDescripcion(String descripcion ){
	this.descripcion = descripcion;
}


 
/**
 * Getter of the property <tt>tipo</tt>
 *
 * @return Returns the tipo.
 * 
 */
public String getTipo()
{
	return tipo;
}

/**
 * Setter of the property <tt>tipo</tt>
 *
 * @param tipo The tipo to set.
 *
 */
public void setTipo(String tipo ){
	this.tipo = tipo;
}



public int getInBloqueo() {
	return inBloqueo;
}

public void setInBloqueo(int inBloqueo) {
	this.inBloqueo = inBloqueo;
}

/**Recupera el indicador que determina si el tipo de bloqueo es usado para el financiamiento otorgado
 * @return retorna verdadero en caso que el tipo de bloqueo sea usado es para el financiamiento otorgado, falso en caso contrario*/
public boolean isBloqueoFinanciado() {
	return BloqueoFinanciado;
}

/**Setea el valor para indicar que el tipo de bloqueo es usado cuando el financiamiento de una orden sea otorgado.*/
public void setBloqueoFinanciado(boolean bloqueoFinanciado) {
	BloqueoFinanciado = bloqueoFinanciado;
}

/**
 * @return the aprobacionFinanciamiento
 */
public int getAprobacionFinanciamiento() {
	return AprobacionFinanciamiento;
}

/**
 * @param aprobacionFinanciamiento the aprobacionFinanciamiento to set
 */
public void setAprobacionFinanciamiento(int aprobacionFinanciamiento) {
	AprobacionFinanciamiento = aprobacionFinanciamiento;
}

}