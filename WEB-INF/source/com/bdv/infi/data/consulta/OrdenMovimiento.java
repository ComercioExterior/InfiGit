package com.bdv.infi.data.consulta;

/** 
 * Representa una consulta de las transacciones efectuadas por un cliente o por t&iacute;tulos
 */
public class OrdenMovimiento {

	/*
 * (non-javadoc)
 */
private String fecha;
 
/**
 * Getter of the property <tt>fecha</tt>
 *
 * @return Returns the fecha.
 * 
 */
public String getFecha()
{
	return fecha;
}

/**
 * Setter of the property <tt>fecha</tt>
 *
 * @param fecha The fecha to set.
 *
 */
public void setFecha(String fecha ){
	this.fecha = fecha;
}

/**
 * id de la orden
 */
private String idOrden;
 
/**
 * Getter of the property <tt>idOrden</tt>
 *
 * @return Returns the idOrden.
 * 
 */
public String getIdOrden()
{
	return idOrden;
}

/**
 * Setter of the property <tt>idOrden</tt>
 *
 * @param idOrden The idOrden to set.
 *
 */
public void setIdOrden(String idOrden ){
	this.idOrden = idOrden;
}

	/**
 * Descripci&oacute;n del t&iacute;tulo
 */
private String descripcionTitulo;
 
/**
 * Getter of the property <tt>descripcionTitulo</tt>
 *
 * @return Returns the descripcionTitulo.
 * 
 */
public String getDescripcionTitulo()
{
	return descripcionTitulo;
}

/**
 * Setter of the property <tt>descripcionTitulo</tt>
 *
 * @param descripcionTitulo The descripcionTitulo to set.
 *
 */
public void setDescripcionTitulo(String descripcionTitulo ){
	this.descripcionTitulo = descripcionTitulo;
}

	/**
 * Id del t&iacute;tulo
 */
private String idTitulo;
 
/**
 * Getter of the property <tt>idTitulo</tt>
 *
 * @return Returns the idTitulo.
 * 
 */
public String getIdTitulo()
{
	return idTitulo;
}

/**
 * Setter of the property <tt>idTitulo</tt>
 *
 * @param idTitulo The idTitulo to set.
 *
 */
public void setIdTitulo(String idTitulo ){
	this.idTitulo = idTitulo;
}

	/**
 * Nombre del cliente
 */
private String nombreCliente;
 
/**
 * Getter of the property <tt>nombreCliente</tt>
 *
 * @return Returns the nombreCliente.
 * 
 */
public String getNombreCliente()
{
	return nombreCliente;
}

/**
 * Setter of the property <tt>nombreCliente</tt>
 *
 * @param nombreCliente The nombreCliente to set.
 *
 */
public void setNombreCliente(String nombreCliente ){
	this.nombreCliente = nombreCliente;
}

	/**
 * Id del cliente
 */
private String idCliente;
 
/**
 * Getter of the property <tt>idCliente</tt>
 *
 * @return Returns the idCliente.
 * 
 */
public String getIdCliente()
{
	return idCliente;
}

/**
 * Setter of the property <tt>idCliente</tt>
 *
 * @param idCliente The idCliente to set.
 *
 */
public void setIdCliente(String idCliente ){
	this.idCliente = idCliente;
}

	/**
 * Descripci&oacute;n de la transacci&oacute;n
 */
private String descripcionTransaccion;
 
/**
 * Getter of the property <tt>descripcionTransaccion</tt>
 *
 * @return Returns the descripcionTransaccion.
 * 
 */
public String getDescripcionTransaccion()
{
	return descripcionTransaccion;
}

/**
 * Setter of the property <tt>descripcionTransaccion</tt>
 *
 * @param descripcionTransaccion The descripcionTransaccion to set.
 *
 */
public void setDescripcionTransaccion(String descripcionTransaccion ){
	this.descripcionTransaccion = descripcionTransaccion;
}

	/**
 * Id de la transacci&oacute;n
 */
private String idTransaccion;
 
/**
 * Getter of the property <tt>idTransaccion</tt>
 *
 * @return Returns the idTransaccion.
 * 
 */
public String getIdTransaccion()
{
	return idTransaccion;
}

/**
 * Setter of the property <tt>idTransaccion</tt>
 *
 * @param idTransaccion The idTransaccion to set.
 *
 */
public void setIdTransaccion(String idTransaccion ){
	this.idTransaccion = idTransaccion;
}

}