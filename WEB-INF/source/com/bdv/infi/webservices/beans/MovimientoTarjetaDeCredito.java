package com.bdv.infi.webservices.beans;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Movimientos de la tarjeta de crédito
 * 
 * @author cet
 * 
 */
public class MovimientoTarjetaDeCredito {
	/**
	 * fecha del movimiento
	 */
	private Date fecha;
	/**
	 * código de referencia del movimiento
	 */
	private String referencia;
	/**
	 * texto descriptivo del movimiento
	 */
	private String descripcion;
	/**
	 * cantidad en dólares del movimiento, si aplica
	 */
	private BigDecimal montoEnDolares;
	/**
	 * cantidad en bolívares del movimiento
	 */
	private BigDecimal montoEnBolivares;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getMontoEnBolivares() {
		return montoEnBolivares;
	}
	public void setMontoEnBolivares(BigDecimal montoEnBolivares) {
		this.montoEnBolivares = montoEnBolivares;
	}
	public BigDecimal getMontoEnDolares() {
		return montoEnDolares;
	}
	public void setMontoEnDolares(BigDecimal montoEnDolares) {
		this.montoEnDolares = montoEnDolares;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}
