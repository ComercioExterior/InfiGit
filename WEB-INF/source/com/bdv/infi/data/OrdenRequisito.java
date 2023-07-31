package com.bdv.infi.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa un requisito de una orden
 */
public class OrdenRequisito implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int ordeneId;
	private Date fechaRecepcion;
	private String usuarioRecepcion;
	private int indicaId;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrdeneId() {
		return ordeneId;
	}
	public void setOrdeneId(int ordeneId) {
		this.ordeneId = ordeneId;
	}
	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}
	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	public String getUsuarioRecepcion() {
		return usuarioRecepcion;
	}
	public void setUsuarioRecepcion(String usuarioRecepcion) {
		this.usuarioRecepcion = usuarioRecepcion;
	}
	public int getIndicaId() {
		return indicaId;
	}
	public void setIndicaId(int indicaId) {
		this.indicaId = indicaId;
	}
}
