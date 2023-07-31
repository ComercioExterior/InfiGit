package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

public class TipoPersonasValidas implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Persona> tipoPersonasValidas;

	/**
	 * @return the tipoPersonasValidas
	 */
	public ArrayList<Persona> getTipoPersonasValidas() {
		return tipoPersonasValidas;
	}

	/**
	 * @param tipoPersonasValidas the tipoPersonasValidas to set
	 */
	public void setTipoPersonasValidas(ArrayList<Persona> tipoPersonasValidas) {
		this.tipoPersonasValidas = tipoPersonasValidas;
	}
}
