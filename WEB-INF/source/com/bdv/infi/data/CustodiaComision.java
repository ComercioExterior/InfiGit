package com.bdv.infi.data;

import java.util.Date;

public class CustodiaComision {
	
	/**
	 * Identificador de la Comision
	 */
	private int idComision;
	
	/**
	 * Nombre de la Comision 
	 */
	private String comisionNombre;
	
	/**
	 * Identificador del usuario que Crea o Actualiza 
	 */
	private int idUsuario;
	
	/**
	 * Fecha de Creacion o Ultima Actualizacion 
	 */
	private Date fecha;
	
	/**
	 * Indicador que especifica si la comision es eneral o especifica 
	 */
	private int comisionInGeneral;

	public String getComisionNombre() {
		return comisionNombre;
	}

	public void setComisionNombre(String comisionNombre) {
		this.comisionNombre = comisionNombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getIdComision() {
		return idComision;
	}

	public void setIdComision(int idComision) {
		this.idComision = idComision;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getComisionInGeneral() {
		return comisionInGeneral;
	}

	public void setComisionInGeneral(int comisionInGeneral) {
		this.comisionInGeneral = comisionInGeneral;
	}
	
}
