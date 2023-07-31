package com.bdv.infi_services.beans.entidades.mensajes_peticion;

import java.io.Serializable;

public class CampoDinamicoTo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Id Del Campo
	 */
	String idCampo;
	/**
	 * Valor del Campo
	 */
	String valorCampo;
	/**
	 * @return the idCampo
	 */
	public String getIdCampo() {
		return idCampo;
	}
	/**
	 * @param idCampo the idCampo to set
	 */
	public void setIdCampo(String idCampo) {
		this.idCampo = idCampo;
	}
	/**
	 * @return the valorCampo
	 */
	public String getValorCampo() {
		return valorCampo;
	}
	/**
	 * @param valorCampo the valorCampo to set
	 */
	public void setValorCampo(String valorCampo) {
		this.valorCampo = valorCampo;
	}
}
