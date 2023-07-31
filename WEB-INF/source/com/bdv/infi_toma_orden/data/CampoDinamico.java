package com.bdv.infi_toma_orden.data;

import java.io.Serializable;

public class CampoDinamico implements Serializable{

	/**Id del campo dinámico*/
	private int idCampo;
	
	/**Valor del campo*/
	private String valor;
	
	/**
	 * Nombre del Campo
	 */
	
	private String nombreCampo;
	
	/**Tipo de dato
	 * 1 Numerico
	 * 2 Tipo String
	 * 3 Fecha dd/mm/yyyy*/
	private int tipoDato;

	public int getIdCampo() {
		return idCampo;
	}

	public void setIdCampo(int idCampo) {
		this.idCampo = idCampo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(int tipoDato) {
		this.tipoDato = tipoDato;
	}

	public String getNombreCampo() {
		return nombreCampo;
	}

	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}
	
	
}
