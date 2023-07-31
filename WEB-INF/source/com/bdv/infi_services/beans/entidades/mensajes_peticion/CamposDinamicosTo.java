package com.bdv.infi_services.beans.entidades.mensajes_peticion;

import java.io.Serializable;
import java.util.ArrayList;

public class CamposDinamicosTo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ArrayList de Campos Dinamicos
	 */
	ArrayList<CampoDinamicoTo> camposDinamicos;

	/**
	 * @return the camposDinamicos
	 */
	public ArrayList<CampoDinamicoTo> getCamposDinamicos() {
		return camposDinamicos;
	}

	/**
	 * @param camposDinamicos the camposDinamicos to set
	 */
	public void setCamposDinamicos(ArrayList<CampoDinamicoTo> camposDinamicos) {
		this.camposDinamicos = camposDinamicos;
	}
}
