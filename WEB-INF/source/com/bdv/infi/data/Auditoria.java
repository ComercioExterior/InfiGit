package com.bdv.infi.data;

import java.util.ArrayList;
import java.util.Date;

/** 
 * Clase que representa la informaci&oacute;n principal en el proceso de una generaci&oacute;n de archivos
 */
public class Auditoria {
	
	private String direccionIp;
	private String fechaAuditoria;
	private String usuario;
	private String peticion;
	private String detalle;
	
	public String getDetalle() {
		return detalle;
	}
	
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	
	public String getDireccionIp() {
		return direccionIp;
	}
	
	public void setDireccionIp(String direccionIp) {
		this.direccionIp = direccionIp;
	}
	
	public String getFechaAuditoria() {
		return fechaAuditoria;
	}
	
	public void setFechaAuditoria(String fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}
	
	public String getPeticion() {
		return peticion;
	}
	
	public void setPeticion(String peticion) {
		this.peticion = peticion;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
}
