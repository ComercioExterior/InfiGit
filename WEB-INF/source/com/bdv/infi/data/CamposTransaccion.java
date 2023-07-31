package com.bdv.infi.data;

/** 
 * Clase usada para determinar los campos asociados a una transacci&oacute;n con el fin que el usuario conozca los posibles campos que puede incorporar en un documento plantilla.
 */
public class CamposTransaccion {

	/**
 * Nombre del campo que debe aparecer en el documento plantilla encerrado en dos @
 */
private String nombre;
 
/**
 * Getter of the property <tt>nombre</tt>
 *
 * @return Returns the nombre.
 * 
 */
public String getNombre()
{
	return nombre;
}

/**
 * Setter of the property <tt>nombre</tt>
 *
 * @param nombre The nombre to set.
 *
 */
public void setNombre(String nombre ){
	this.nombre = nombre;
}

	/**
 * Una descripci&oacute;n del uso del campo
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
 * Id del campo
 */
private String idCampo;
 
/**
 * Getter of the property <tt>idCampo</tt>
 *
 * @return Returns the idCampo.
 * 
 */
public String getIdCampo()
{
	return idCampo;
}

/**
 * Setter of the property <tt>idCampo</tt>
 *
 * @param idCampo The idCampo to set.
 *
 */
public void setIdCampo(String idCampo ){
	this.idCampo = idCampo;
}

}
