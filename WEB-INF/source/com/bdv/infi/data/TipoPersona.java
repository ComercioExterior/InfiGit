package com.bdv.infi.data;

/** 
 * Tipo de personas configuradas en la aplicaci&oacute;n
 */
public class TipoPersona extends com.bdv.infi.data.DataRegistro {

	/**
 * Descripci&oacute;n del tipo de persona
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
 * Id del tipo de persona
 */
private String idTipoPersona;
 
/**
 * Getter of the property <tt>idTipoPersona</tt>
 *
 * @return Returns the idTipoPersona.
 * 
 */
public String getIdTipoPersona()
{
	return idTipoPersona;
}

/**
 * Setter of the property <tt>idTipoPersona</tt>
 *
 * @param idTipoPersona The idTipoPersona to set.
 *
 */
public void setIdTipoPersona(String idTipoPersona ){
	this.idTipoPersona = idTipoPersona;
}

}
