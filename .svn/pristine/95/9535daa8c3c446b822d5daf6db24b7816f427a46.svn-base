package com.bdv.infi.data;

import java.util.Date;

/** 
 * Clase que representa la entidad custodia
 */
public class Custodia extends com.bdv.infi.data.DataRegistro {


/**
 * Monto total del t&iacute;tulo = cantidad * valor nominal
 */
private double montoTotal;

private String tipoProductoId;

/**
 * Getter of the property <tt>montoTotal</tt>
 *
 * @return Returns the montoTotal.
 * 
 */
public double getMontoTotal()
{
	return montoTotal;
}

/**
 * Setter of the property <tt>montoTotal</tt>
 *
 * @param montoTotal The montoTotal to set.
 *
 */
public void setMontoTotal(double montoTotal ){
	this.montoTotal = montoTotal;
}

	/**
	 * Indica si el t&iacute;tulo est&aacute; bloqueado 
	*/
	public boolean isBloqueado(){
		return (custodiaBloqueo.getCantidad()>0);
	}

	/**
	 * Indica la cantidad bloqueada que tiene el t&iacute;tulo 
	*/
	public double getCantidadBloqueada(){
		return custodiaBloqueo.getCantidad();
	}

/*
 * (non-javadoc)
 */
private CustodiaBloqueo custodiaBloqueo;
 
/**
 * Getter of the property <tt>custodiaBloqueo</tt>
 *
 * @return Returns the custodiaBloqueo.
 * 
 */
public CustodiaBloqueo getCustodiaBloqueo()
{
	return custodiaBloqueo;
}

/**
 * Setter of the property <tt>custodiaBloqueo</tt>
 *
 * @param custodiaBloqueo The custodiaBloqueo to set.
 *
 */
public void setCustodiaBloqueo(CustodiaBloqueo custodiaBloqueo ){
	this.custodiaBloqueo = custodiaBloqueo;
}

/**
 * &uacute;ltima fecha de cobro de comisión
 */
private Date ultimaFechaComision;
 
/**
 * Getter of the property <tt>ultimaFechaCierre</tt>
 *
 * @return Returns the ultimaFechaCierre.
 * 
 */
public Date getUltimaFechaComision()
{
	return ultimaFechaComision;
}

/**
 * Setter of the property <tt>ultimaFechaCierre</tt>
 *
 * @param ultimaFechaCierre The ultimaFechaCierre to set.
 *
 */
public void setUltimaFechaComision(Date ultimaFechaComision ){
	this.ultimaFechaComision = ultimaFechaComision;
}

	/**
 * Fecha del &uacute;ltimo cup&oacute;n
 */
private Date fechaUltimoCupon;
 
/**
 * Getter of the property <tt>fechaUltimoCupon</tt>
 *
 * @return Returns the fechaUltimoCupon.
 * 
 */
public Date getFechaUltimoCupon()
{
	return fechaUltimoCupon;
}

/**
 * Setter of the property <tt>fechaUltimoCupon</tt>
 *
 * @param fechaUltimoCupon The fechaUltimoCupon to set.
 *
 */
public void setFechaUltimoCupon(Date fechaUltimoCupon ){
	this.fechaUltimoCupon = fechaUltimoCupon;
}

	/**
 * Fecha del pago del pr&oacute;ximo cup&oacute;n
 */
private Date fechaCuponProximo;
 
/**
 * Getter of the property <tt>fechaCuponProximo</tt>
 *
 * @return Returns the fechaCuponProximo.
 * 
 */
public Date getFechaCuponProximo()
{
	return fechaCuponProximo;
}

/**
 * Setter of the property <tt>fechaCuponProximo</tt>
 *
 * @param fechaCuponProximo The fechaCuponProximo to set.
 *
 */
public void setFechaCuponProximo(Date fechaCuponProximo ){
	this.fechaCuponProximo = fechaCuponProximo;
}

	/**
 * Cantidad que posee en custodia
 */
private long cantidad;
 
/**
 * Getter of the property <tt>cantidad</tt>
 *
 * @return Returns the cantidad.
 * 
 */
public long getCantidad()
{
	return cantidad;
}

/**
 * Setter of the property <tt>cantidad</tt>
 *
 * @param cantidad The cantidad to set.
 *
 */
public void setCantidad(long cantidad ){
	this.cantidad = cantidad;
}

	/**
 * Id del cliente dueño del t&iacute;tulo
 */
private long idCliente;
 
/**
 * Getter of the property <tt>idCliente</tt>
 *
 * @return Returns the idCliente.
 * 
 */
public long getIdCliente()
{
	return idCliente;
}

/**
 * Setter of the property <tt>idCliente</tt>
 *
 * @param idCliente The idCliente to set.
 *
 */
public void setIdCliente(long idCliente ){
	this.idCliente = idCliente;
}

	/**
 * T&iacute;tulo en custodia
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

/**Indica la fecha de último pago de amortización*/
private Date fechaUltimaAmortizacion;

/**Establece la fecha de último pago de amortización*/
public void setFechaUltimaAmortizacion(Date fechaUltimaAmortizacion ){
	this.fechaUltimaAmortizacion = fechaUltimaAmortizacion;
}

public Date getFechaUltimaAmortizacion() {
	return fechaUltimaAmortizacion;
}


/**Obtiene la fecha de última amortización cancelada al cliente */


/**Suma la cantidad de títulos recibidos a lo que existe*/
public void agregarCantidad(long cantidad){
	this.cantidad += cantidad;
}

/**Resta la cantidad de títulos recibidos a lo que existe*/
public void restarCantidad(long cantidad){
	this.cantidad -= cantidad;
}

public String getTipoProductoId() {
	return tipoProductoId;
}

public void setTipoProductoId(String tipoProductoId) {
	this.tipoProductoId = tipoProductoId;
}



}