package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;

public class CampoDinamico implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**Id del campo din&aacute;mico*/
	private String idCampo;
	/**
	 * Descripcion del campo
	 */
	private String descrCampo;
	

	public String getIdCampo() {
		return idCampo;
	}

	public void setIdCampo(String idCampo) {
		this.idCampo = idCampo;
	}

	/**
	 * @return the descrCampo
	 */
	public String getDescrCampo() {
		return descrCampo;
	}

	/**
	 * @param descrCampo the descrCampo to set
	 */
	public void setDescrCampo(String descrCampo) {
		this.descrCampo = descrCampo;
	}

}
