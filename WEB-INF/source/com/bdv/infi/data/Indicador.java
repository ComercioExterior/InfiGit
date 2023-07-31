package com.bdv.infi.data;

/** 
 * Clase que representa un indicador de cotejo dentro de una transacci&oacute;n
 */
public class Indicador {

	/**
 * Indica si el indicador es requerido
 */
private boolean requerido;
 
/**
 * Getter of the property <tt>requerido</tt>
 *
 * @return Returns the requerido.
 * 
 */
public boolean getRequerido()
{
	return requerido;
}

/**
 * Setter of the property <tt>requerido</tt>
 *
 * @param requerido The requerido to set.
 *
 */
public void setRequerido(boolean requerido ){
	this.requerido = requerido;
}

	/**
 * Descripci&oacute;n
 */
private String descripcion;
 
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
 * Id del indicador
 */
private String idIndicador;
 
/**
 * Getter of the property <tt>idIndicador</tt>
 *
 * @return Returns the idIndicador.
 * 
 */
public String getIdIndicador()
{
	return idIndicador;
}

/**
 * Setter of the property <tt>idIndicador</tt>
 *
 * @param idIndicador The idIndicador to set.
 *
 */
public void setIdIndicador(String idIndicador ){
	this.idIndicador = idIndicador;
}

}
