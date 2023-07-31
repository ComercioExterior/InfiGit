package com.bdv.infi.data;

public class UrlLogConfig {
	
	/*
	 * Identificador unico del Url
	 */
	private int id_config;
	
	/*Url
	 */
	private String url;
	
	/*
	 * Indicador de habilitado
	 */
	private int enable;

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public int getId_config() {
		return id_config;
	}

	public void setId_config(int id_config) {
		this.id_config = id_config;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
