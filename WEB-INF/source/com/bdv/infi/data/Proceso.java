package com.bdv.infi.data;

import java.util.Date;

public class Proceso {
	
	/**
	 * Id de ejecuci&oacute;n del proceso
	 */	
	private int ejecucionId;
	
	/**
	 * Id de la transacci&oacute;n
	 */
	private String transaId;
	
	/**
	 * Id del usuario
	 */
	private int usuarioId;
	
	/**
	 * Fecha de inicio del proceso
	 */
	private Date fechaInicio;
	
	/**
	 * Fecha de fin del proceso
	 */
	private Date fechaFin;
	
	/**
	 * Descripci&oacute;n del mensaje de error, si se encontr&oacute; alguno durante el proceso
	 */
	private String descripcionError=new String();
	
	/**
	 * Fecha a la que corresponde el mes en que se est&aacute; ejecutando el proceso
	 */
	private Date fechaValor;
	
	/**
	 * Fecha a la que corresponde el mes en que se est&aacute; ejecutando el proceso
	 */
	private long cicloEjecucionId;
	
	private int longitudMax=999;

	public String getDescripcionError() {
		if (descripcionError.length() > longitudMax) {
			descripcionError = descripcionError.substring(0, longitudMax);
		}
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {		
		this.descripcionError = descripcionError;
	}
	
	public void agregarDescripcionError(String mensajeError){
		descripcionError=descripcionError.concat(" - " + mensajeError);
	}
	
	public void agregarDescripcionErrorTrunc(String mensajeError, boolean truncar){		
		descripcionError=descripcionError.concat(" - " + mensajeError);
		if (truncar) {
			if (descripcionError.length() > longitudMax) {
				descripcionError = descripcionError.substring(0, longitudMax);
			}
		}
	}
	public void agregarAlInicioDescripcionErrorTrunc(String mensajeError, boolean truncar){
		int longitudMax=999;
		descripcionError=mensajeError.concat(" - " + descripcionError);
		if (truncar) {
			if (descripcionError.length() > longitudMax) {
				descripcionError = descripcionError.substring(0, longitudMax);
			}
		}
	}

	public int getEjecucionId() {
		return ejecucionId;
	}

	public void setEjecucionId(int ejecucionId) {
		this.ejecucionId = ejecucionId;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}

	public String getTransaId() {
		return transaId;
	}

	public void setTransaId(String transaId) {
		this.transaId = transaId;
	}

	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	public long getCicloEjecucionId() {
		return cicloEjecucionId;
	}

	public void setCicloEjecucionId(long cicloEjecucionId) {
		this.cicloEjecucionId = cicloEjecucionId;
	}
	
	
}
