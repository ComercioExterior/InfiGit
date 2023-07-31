package com.bdv.infi.data;

/** 
 * Campos asociados a una forma de pago
 * Si el pago es con tarjeta de cr&eacute;dito por ejemplo se tendr&iacute;a un campo n&uacute;mero tajeta, otro campo vencimiento tarjeta. Cada uno con su tipo de dato (Entero, Texto) para efectuar validaciones autom&aacute;ticas
 */
public class FormaPagoRef {

	//Tipos de datos
	public static final String NUMERICO = "NU";
	public static final String TEXTO = "ST";	
	
	/**
 * Id del campo que se debe solicitar
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

/**
 * Indica el tipo de dato del campo
 */
private String tipoDato;
 
/**
 * Getter of the property <tt>tipoDato</tt>
 *
 * @return Returns the tipoDato.
 * 
 */
public String getTipoDato()
{
	return tipoDato;
}

/**
 * Setter of the property <tt>tipoDato</tt>
 *
 * @param tipoDato The tipoDato to set.
 *
 */
public void setTipoDato(String tipoDato ){
	this.tipoDato = tipoDato;
}

	/**
 * Indica si el campo es requerido
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
 * Valor escrito por el usuario seg&uacute;n la informaci&oacute;n requerida
 */
private String valor;
 
/**
 * Getter of the property <tt>valor</tt>
 *
 * @return Returns the valor.
 * 
 */
public String getValor()
{
	return valor;
}

/**
 * Setter of the property <tt>valor</tt>
 *
 * @param valor The valor to set.
 *
 */
public void setValor(String valor ){
	this.valor = valor;
}

	/**
 * Descripci&oacute;n de la referencia
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

}