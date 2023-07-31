package com.bdv.infi.webservices.beans;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Datos para la tabla de amortización de créditos
 * 
 * @author cet
 * 
 */
public class DatoDeAmortizacionDeCredito {
	private int numero;
	private Date fechaDeVencimiento;
	private BigDecimal montoDeCapital;
	private BigDecimal montoDeInteres;
	private BigDecimal montoDeSeguro;
	private BigDecimal montoDeCuota;
	private int diasPeriodo;
	private BigDecimal saldoPendiente;
	public int getDiasPeriodo() {
		return diasPeriodo;
	}
	public void setDiasPeriodo(int diasPeriodo) {
		this.diasPeriodo = diasPeriodo;
	}
	public Date getFechaDeVencimiento() {
		return fechaDeVencimiento;
	}
	public void setFechaDeVencimiento(Date fechaDeVencimiento) {
		this.fechaDeVencimiento = fechaDeVencimiento;
	}
	public BigDecimal getMontoDeCapital() {
		return montoDeCapital;
	}
	public void setMontoDeCapital(BigDecimal montoDeCapital) {
		this.montoDeCapital = montoDeCapital;
	}
	public BigDecimal getMontoDeCuota() {
		return montoDeCuota;
	}
	public void setMontoDeCuota(BigDecimal montoDeCuota) {
		this.montoDeCuota = montoDeCuota;
	}
	public BigDecimal getMontoDeInteres() {
		return montoDeInteres;
	}
	public void setMontoDeInteres(BigDecimal montoDeInteres) {
		this.montoDeInteres = montoDeInteres;
	}
	public BigDecimal getMontoDeSeguro() {
		return montoDeSeguro;
	}
	public void setMontoDeSeguro(BigDecimal montoDeSeguro) {
		this.montoDeSeguro = montoDeSeguro;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public BigDecimal getSaldoPendiente() {
		return saldoPendiente;
	}
	public void setSaldoPendiente(BigDecimal saldoPendiente) {
		this.saldoPendiente = saldoPendiente;
	}
}
