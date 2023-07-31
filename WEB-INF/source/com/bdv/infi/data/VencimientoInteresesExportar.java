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
public class VencimientoInteresesExportar {
	


	/**
	 * Nombre del titulo
	 */
private String tituloDescripcion;

/**
 * Nombre del Cliente
 */

private String clienteNombre;

/**
 * Numero de cuenta
 */
private String cuentaNumero;

/**
 * Vencimiento del titulo
 */
private Date vctoTitulo;

/**
 * Capital
 */

private BigDecimal capital;

private BigDecimal valorNominal;

/**
 * Estatus
 */

private String estatus;

/**
 * fecha de incio de cupon
 */

private Date fechaInicioCupon;

/**
 * fecha de vencimiento de cupon
 */

private Date fechaVctoCupon;

/**
 * Tasa
 */

private BigDecimal tasa;

/**
 * Dias de calculo
 */

private int diasCalculo;

/**
 * Moneda
 */

private String moneda;

/**
 * monto por pago de amortizacion
 */

private BigDecimal intereses;


public String getMoneda() {
	return moneda;
}

public void setMoneda(String moneda) {
	this.moneda = moneda;
}

/**
 * @return the intereses
 */
public BigDecimal getIntereses() {
	return intereses;
}



/**
 * @param intereses the intereses to set
 */
public void setIntereses(BigDecimal intereses) {
	this.intereses = intereses;
}

public BigDecimal getValorNominal() {
	return capital;
}

public void setValorNominal(BigDecimal valorNominal) {
	this.valorNominal = valorNominal;
}

public BigDecimal getCapital() {
	return capital;
}



public void setCapital(BigDecimal capital) {
	this.capital = capital;
}

public String getClienteNombre() {
	return clienteNombre;
}

public void setClienteNombre(String clienteNombre) {
	this.clienteNombre = clienteNombre;
}

public String getCuentaNumero() {
	return cuentaNumero;
}

public void setCuentaNumero(String cuentaNumero) {
	this.cuentaNumero = cuentaNumero;
}

public String getEstatus() {
	return estatus;
}

public void setEstatus(String estatus) {
	this.estatus = estatus;
}

public Date getFechaInicioCupon() {
	return fechaInicioCupon;
}

public void setFechaInicioCupon(Date fechaInicioCupon) {
	this.fechaInicioCupon = fechaInicioCupon;
}

public Date getFechaVctoCupon() {
	return fechaVctoCupon;
}

public void setFechaVctoCupon(Date fechaVctoCupon) {
	this.fechaVctoCupon = fechaVctoCupon;
}

public BigDecimal getTasa() {
	return tasa;
}

public void setTasa(BigDecimal tasa) {
	this.tasa = tasa;
}

public String getTituloDescripcion() {
	return tituloDescripcion;
}

public void setTituloDescripcion(String tituloDescripcion) {
	this.tituloDescripcion = tituloDescripcion;
}

public Date getVctoTitulo() {
	return vctoTitulo;
}

public void setVctoTitulo(Date vctoTitulo) {
	this.vctoTitulo = vctoTitulo;
}

public void setDiasCalculo(int diasCalculo) {
	this.diasCalculo = diasCalculo;
}

public int getDiasCalculo() {
	return diasCalculo;
}
}
