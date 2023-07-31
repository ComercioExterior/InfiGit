package com.bdv.infi.data;

import java.util.Date;

/**
 * Clase que encapsula los datos del envio de correos 
 * asociado a la tabla INFI_TB_228_ENVIO_MAIL
 * @author nm29643
 *
 */
public class EnvioMail {
	
	private long idCorreo;
	private long idPlantilla;
	private long idCliente;
	private long idArea;
	private String direccionCorreo;
	private String status;
	private long idCicloEjecucion;
	private Date fechaRegistro;
	private Date fechaEnvio;
	private long idOrden;
	//NM29643 infi_TTS_466
	private String contenido;
	
	public long getIdCorreo() {
		return idCorreo;
	}
	public void setIdCorreo(long idCorreo) {
		this.idCorreo = idCorreo;
	}
	public long getIdPlantilla() {
		return idPlantilla;
	}
	public void setIdPlantilla(long idPlantilla) {
		this.idPlantilla = idPlantilla;
	}
	public long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}
	public long getIdArea() {
		return idArea;
	}
	public void setIdArea(long idArea) {
		this.idArea = idArea;
	}
	public String getDireccionCorreo() {
		return direccionCorreo;
	}
	public void setDireccionCorreo(String direccionCorreo) {
		this.direccionCorreo = direccionCorreo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getIdCicloEjecucion() {
		return idCicloEjecucion;
	}
	public void setIdCicloEjecucion(long idCicloEjecucion) {
		this.idCicloEjecucion = idCicloEjecucion;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public long getIdOrden() {
		return idOrden;
	}
	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}
	//NM29643 infi_TTS_466
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
}
