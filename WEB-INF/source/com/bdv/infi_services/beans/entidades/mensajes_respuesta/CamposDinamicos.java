package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

public class CamposDinamicos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Array de Campos Dinamicos
	 */
	private ArrayList<CampoDinamico> camposDinamicos;

	/**
	 * @return the camposDinamicos
	 */
	public ArrayList<CampoDinamico> getCamposDinamicos() {
		return camposDinamicos;
	}

	/**
	 * @param camposDinamicos the camposDinamicos to set
	 */
	public void setCamposDinamicos(ArrayList<CampoDinamico> camposDinamicos) {
		this.camposDinamicos = camposDinamicos;
	}
}
