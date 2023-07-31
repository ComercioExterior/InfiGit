package com.bdv.infi.data;

import java.io.Serializable;

public class CampoDinamico implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**Id del campo din&aacute;mico*/
	private int idCampo;
	
	/**Valor del campo*/
	private String valor;
	
	/**Tipo de dato
	 * 1 Numerico
	 * 2 Tipo String
	 * 3 Fecha dd/mm/yyyy*/
	private int tipoDato;
	
	private String Descripcion;
	/**
	 * Nombre del campo
	 */
	private String campoNombre;
	
	/**
	 * @return the campoNombre
	 */
	public String getCampoNombre() {
		return campoNombre;
	}

	/**
	 * @param campoNombre the campoNombre to set
	 */
	public void setCampoNombre(String campoNombre) {
		this.campoNombre = campoNombre;
	}

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

	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	
	
}
