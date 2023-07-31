package com.bdv.infi.data;

public class UrlParametersLogConfig {
	
	/*
	 * Identificador unico del Url
	 */
	private int id_config;
	
	/*Nombre del parametro
	 */
	private String parametro;
	
	/*Nombre del parametro modificado
	 */
	private String name;

	public int getId_config() {
		return id_config;
	}

	public void setId_config(int id_config) {
		this.id_config = id_config;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
