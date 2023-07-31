package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.Date;

public class EventosExportarExcel {

	/*** Proceso */
	private long proceso;
	
	/*** Instrucción de Pago definida */
	private String instruccion;
	
	/*** Nombre del Cliente */
	private String nombreCliente;
	
	/*** operacionId */
	private long operacionId;
	
	/*** Nombre de la operación */
	private String operacionNombre;
	
	/***Título involucrado */
	private String titulo;
	
	/***Título involucrado Descripcion*/
	private String tituloDescripcion;
	
	/***Transacción */
	private String transaccion;
	
	/***Status */
	private String status;
	
	/***Moneda */
	private String moneda;
	
	/***Calculo : Monto involucrado en la operación */
	private BigDecimal calculo;
	
	
	/***Fecha Desde*/
	private Date fechaDesde;
	
	/***Fecha Hasta*/
	private Date fechaHasta;

	/**
	 * @return the calculo
	 */
	public BigDecimal getCalculo() {
		return calculo;
	}

	/**
	 * @param calculo the calculo to set
	 */
	public void setCalculo(BigDecimal calculo) {
		this.calculo = calculo;
	}

	/**
	 * @return the fechaDesde
	 */
	public Date getFechaDesde() {
		return fechaDesde;
	}

	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	/**
	 * @return the fechaHasta
	 */
	public Date getFechaHasta() {
		return fechaHasta;
	}

	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	/**
	 * @return the instruccion
	 */
	public String getInstruccion() {
		return instruccion;
	}

	/**
	 * @param instruccion the instruccion to set
	 */
	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}

	/**
	 * @return the moneda
	 */
	public String getMoneda() {
		return moneda;
	}

	/**
	 * @param moneda the moneda to set
	 */
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	/**
	 * @return the nombreCliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * @param nombreCliente the nombreCliente to set
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
	 * @return the proceso
	 */
	public long getProceso() {
		return proceso;
	}

	/**
	 * @param proceso the proceso to set
	 */
	public void setProceso(long proceso) {
		this.proceso = proceso;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the transaccion
	 */
	public String getTransaccion() {
		return transaccion;
	}

	/**
	 * @param transaccion the transaccion to set
	 */
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	/**
	 * @return the tituloDescripcion
	 */
	public String getTituloDescripcion() {
		return tituloDescripcion;
	}

	/**
	 * @param tituloDescripcion the tituloDescripcion to set
	 */
	public void setTituloDescripcion(String tituloDescripcion) {
		this.tituloDescripcion = tituloDescripcion;
	}

	/**
	 * @return the operacionId
	 */
	public long getOperacionId() {
		return operacionId;
	}

	/**
	 * @param operacionId the operacionId to set
	 */
	public void setOperacionId(long operacionId) {
		this.operacionId = operacionId;
	}

	/**
	 * @return the operacionNombre
	 */
	public String getOperacionNombre() {
		return operacionNombre;
	}

	/**
	 * @param operacionNombre the operacionNombre to set
	 */
	public void setOperacionNombre(String operacionNombre) {
		this.operacionNombre = operacionNombre;
	}
}
