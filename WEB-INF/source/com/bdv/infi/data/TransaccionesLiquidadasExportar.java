/**
 * 
 */
package com.bdv.infi.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author bag
 *
 */
public class TransaccionesLiquidadasExportar implements Serializable{
/**
 *  Nombre del Cliente 
 */
private String clienteNombre;

/**
 * Fecha liquidada
 */

private Date fechaLiquidada;

/**
 * Numero de Cuenta
 */

private String cuentaCliente;

/**
 * nombre del titulo
 */

private String tituloDescripcion;

/**
 * Contraparte
 */

private String contraparte;

/*
 * monto del titulo
 */
private BigDecimal montoTitulo;

/**
 * Monto de la operacion
 */

private BigDecimal montoOperacion;

/**
 * Tipo de Transaccion
 */

private String tipoTransaccion;

/**
 * Comision
 */

private String comision;


/**
 * totales Externas
 */

public BigDecimal totalesExternas;

/**
 * totales Internas
 */

public BigDecimal totalesInternas;


public BigDecimal getTotalesExternas() {
	return totalesExternas;
}

public void setTotalesExternas(BigDecimal totalesExternas) {
	this.totalesExternas = totalesExternas;
}

public BigDecimal getTotalesInternas() {
	return totalesInternas;
}

public void setTotalesInternas(BigDecimal totalesInternas) {
	this.totalesInternas = totalesInternas;
}

public String getComision() {
	return comision;
}

public void setComision(String comision) {
	this.comision = comision;
}


public String getTipoTransaccion() {
	return tipoTransaccion;
}

public void setTipoTransaccion(String tipoTransaccion) {
	this.tipoTransaccion = tipoTransaccion;
}

public String getClienteNombre() {
	return clienteNombre;
}

public void setClienteNombre(String clienteNombre) {
	this.clienteNombre = clienteNombre;
}

public String getContraparte() {
	return contraparte;
}

public void setContraparte(String contraparte) {
	this.contraparte = contraparte;
}

public String getCuentaCliente() {
	return cuentaCliente;
}

public void setCuentaCliente(String cuentaCliente) {
	this.cuentaCliente = cuentaCliente;
}

public Date getFechaLiquidada() {
	return fechaLiquidada;
}

public void setFechaLiquidada(Date fechaLiquidada) {
	this.fechaLiquidada = fechaLiquidada;
}

public BigDecimal getMontoOperacion() {
	return montoOperacion;
}

public void setMontoOperacion(BigDecimal montoOperacion) {
	this.montoOperacion = montoOperacion;
}

public BigDecimal getMontoTitulo() {
	return montoTitulo;
}

public void setMontoTitulo(BigDecimal montoTitulo) {
	this.montoTitulo = montoTitulo;
}

public String getTituloDescripcion() {
	return tituloDescripcion;
}

public void setTituloDescripcion(String tituloDescripcion) {
	this.tituloDescripcion = tituloDescripcion;
}


}
