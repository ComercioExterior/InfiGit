package com.bdv.infi.webservices.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 * Representa una tarjeta de crédito que tiene el cliente en el banco. Contiene
 * los datos de una tarjeta tal como se muestran en la pantalla integral de call
 * center. El propósito de estos objetos es el de pasar la lista de tarjetas de
 * crédito que tiene un cliente hacia la capa de presentación.
 * <p>
 * Los objetos de esta clase se crean con jibx a partir de un xml, y están
 * contenidos en un objeto de clase ListaDeTarjetasDeCredito, metidos en un
 * ArrayList.
 * 
 * @author Camilo Torres
 * 
 */
public class TarjetaDeCredito {
public String activo;
	/**
	 * Fecha en que se crea la tarjeta
	 */
	private Date fechaDeInicio;

	/**
	 * Número de la tarjeta
	 */
	private String numero = "";

	/**
	 * Fecha del saldo: EN LINEA o una fecha dd/MM/yyyy
	 */
	private Date fechaDeConsulta ;

	/**
	 * Indica si el saldo es en linea (traido del host) o diferido (de D-1)
	 */
	private boolean esSaldoEnLinea = false;

	/**
	 * Tipo o nombre de la tarjeta: visa, master, etc.
	 */
	private String tipoDeTarjeta = "";

	/**
	 * Saldo disponible en la tarjeta
	 */
	private BigDecimal saldoDisponible ;

	/**
	 * Cantidad de pago mínimo
	 */
	private BigDecimal pagoMinimo;
	
	private BigDecimal limiteDeCredito;
	
	private BigDecimal creditoDisponible;
	
	private Date fechaDeCorte;
	
	private String plazoDeCredito;
	
	private BigDecimal pagoDeContado;
	
	private Date fechaDeProximoCorte;
	
	private Date fechaEstadoDeCuenta;
	
	private Date fechaLimiteDePago;
	
	private BigDecimal totalesCargos;
	
	private BigDecimal totalesCreditos;
	
	private BigDecimal interesesYComisiones;
	
	private BigDecimal saldoActual;
	
	private String centroDeAlta;
	
	private String numeroDeContrato;
	
	/**
	 * Movimientos de esta tarjeta de crédito
	 */
	private ArrayList<MovimientoTarjetaDeCredito> movimientos = null;

	/**
	 * Usuario de la aplicación
	 */
	private CredencialesDeUsuario credenciales;

	public String getCentroDeAlta() {
		return centroDeAlta;
	}

	public void setCentroDeAlta(String centroDeAlta) {
		this.centroDeAlta = centroDeAlta;
	}

	public CredencialesDeUsuario getCredenciales() {
		return credenciales;
	}

	public void setCredenciales(CredencialesDeUsuario credenciales) {
		this.credenciales = credenciales;
	}

	public BigDecimal getCreditoDisponible() {
		return creditoDisponible;
	}

	public void setCreditoDisponible(BigDecimal creditoDisponible) {
		this.creditoDisponible = creditoDisponible;
	}

	public boolean isEsSaldoEnLinea() {
		return esSaldoEnLinea;
	}

	public void setEsSaldoEnLinea(boolean esSaldoEnLinea) {
		this.esSaldoEnLinea = esSaldoEnLinea;
	}

	public Date getFechaDeConsulta() {
		return fechaDeConsulta;
	}

	public void setFechaDeConsulta(Date fechaDeConsulta) {
		this.fechaDeConsulta = fechaDeConsulta;
	}

	public Date getFechaDeCorte() {
		return fechaDeCorte;
	}

	public void setFechaDeCorte(Date fechaDeCorte) {
		this.fechaDeCorte = fechaDeCorte;
	}

	public Date getFechaDeInicio() {
		return fechaDeInicio;
	}

	public void setFechaDeInicio(Date fechaDeInicio) {
		this.fechaDeInicio = fechaDeInicio;
	}

	public Date getFechaDeProximoCorte() {
		return fechaDeProximoCorte;
	}

	public void setFechaDeProximoCorte(Date fechaDeProximoCorte) {
		this.fechaDeProximoCorte = fechaDeProximoCorte;
	}

	public Date getFechaEstadoDeCuenta() {
		return fechaEstadoDeCuenta;
	}

	public void setFechaEstadoDeCuenta(Date fechaEstadoDeCuenta) {
		this.fechaEstadoDeCuenta = fechaEstadoDeCuenta;
	}

	public Date getFechaLimiteDePago() {
		return fechaLimiteDePago;
	}

	public void setFechaLimiteDePago(Date fechaLimiteDePago) {
		this.fechaLimiteDePago = fechaLimiteDePago;
	}

	public BigDecimal getInteresesYComisiones() {
		return interesesYComisiones;
	}

	public void setInteresesYComisiones(BigDecimal interesesYComisiones) {
		this.interesesYComisiones = interesesYComisiones;
	}

	public BigDecimal getLimiteDeCredito() {
		return limiteDeCredito;
	}

	public void setLimiteDeCredito(BigDecimal limiteDeCredito) {
		this.limiteDeCredito = limiteDeCredito;
	}

	public ArrayList<MovimientoTarjetaDeCredito> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(ArrayList<MovimientoTarjetaDeCredito> movimientos) {
		this.movimientos = movimientos;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNumeroDeContrato() {
		return numeroDeContrato;
	}

	public void setNumeroDeContrato(String numeroDeContrato) {
		this.numeroDeContrato = numeroDeContrato;
	}

	public BigDecimal getPagoDeContado() {
		return pagoDeContado;
	}

	public void setPagoDeContado(BigDecimal pagoDeContado) {
		this.pagoDeContado = pagoDeContado;
	}

	public BigDecimal getPagoMinimo() {
		return pagoMinimo;
	}

	public void setPagoMinimo(BigDecimal pagoMinimo) {
		this.pagoMinimo = pagoMinimo;
	}

	public String getPlazoDeCredito() {
		return plazoDeCredito;
	}

	public void setPlazoDeCredito(String plazoDeCredito) {
		this.plazoDeCredito = plazoDeCredito;
	}

	public BigDecimal getSaldoActual() {
		return saldoActual;
	}

	public void setSaldoActual(BigDecimal saldoActual) {
		this.saldoActual = saldoActual;
	}

	public BigDecimal getSaldoDisponible() {
		return saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	public String getTipoDeTarjeta() {
		return tipoDeTarjeta;
	}

	public void setTipoDeTarjeta(String tipoDeTarjeta) {
		this.tipoDeTarjeta = tipoDeTarjeta;
	}

	public BigDecimal getTotalesCargos() {
		return totalesCargos;
	}

	public void setTotalesCargos(BigDecimal totalesCargos) {
		this.totalesCargos = totalesCargos;
	}

	public BigDecimal getTotalesCreditos() {
		return totalesCreditos;
	}

	public void setTotalesCreditos(BigDecimal totalesCreditos) {
		this.totalesCreditos = totalesCreditos;
	}
}
