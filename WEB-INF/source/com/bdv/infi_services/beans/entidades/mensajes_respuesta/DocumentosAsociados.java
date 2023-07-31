package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

public class DocumentosAsociados implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ArrayList que contiene los documentos asociados a una orden
	 */
	ArrayList<Documento>documento = new ArrayList<Documento>();

	/**
	 * @return the documento
	 */
	public ArrayList<Documento> getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(ArrayList<Documento> documento) {
		this.documento = documento;
	}

}
