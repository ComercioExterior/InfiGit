/**
 * 
 */
package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author bag
 *
 */
public class ValoresLiberadosExportar {
	
	/**
	 * Cuenta del Cliente 
	 */
	
private String cuentaCliente;

/**
 * Nombre del Cliente
 */

private String clienteNombre;

/**
 * Nombre del titulo
 */

private String tituloDescripcion;

/**
 * Cantidad liberada
 */

private BigDecimal cantidadLiberada;

/**
 * El valor nominal
 */

private BigDecimal tituloValorNominal;

/**
 * la fecha de operacion
 */

private Date fechaOperacion;

/**Tasa de Cambio*/
private BigDecimal tasaCambio;

/**Multiplicación de la tasa de cambio por la cantidad liberada*/
private BigDecimal total;

public String getClienteNombre() {
	return clienteNombre;
}

public void setClienteNombre(String clienteNombre) {
	this.clienteNombre = clienteNombre;
}

public String getCuentaCliente() {
	return cuentaCliente;
}

public void setCuentaCliente(String cuentaCliente) {
	this.cuentaCliente = cuentaCliente;
}

public Date getFechaOperacion() {
	return fechaOperacion;
}

public void setFechaOperacion(Date fechaOperacion) {
	this.fechaOperacion = fechaOperacion;
}

public BigDecimal getCantidadLiberada() {
	return cantidadLiberada;
}

public void setCantidadLiberada(BigDecimal cantidadLiberada) {
	this.cantidadLiberada = cantidadLiberada;
}

public String getTituloDescripcion() {
	return tituloDescripcion;
}

public void setTituloDescripcion(String tituloDescripcion) {
	this.tituloDescripcion = tituloDescripcion;
}

public BigDecimal getTituloValorNominal() {
	return tituloValorNominal;
}

public void setTituloValorNominal(BigDecimal tituloValorNominal) {
	this.tituloValorNominal = tituloValorNominal;
}

public BigDecimal getTasaCambio() {
	return tasaCambio;
}

public void setTasaCambio(BigDecimal tasaCambio) {
	this.tasaCambio = tasaCambio;
}

public BigDecimal getTotal() {
	return total;
}

public void setTotal(BigDecimal total) {
	this.total = total;
}



}
