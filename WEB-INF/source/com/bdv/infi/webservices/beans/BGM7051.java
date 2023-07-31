package com.bdv.infi.webservices.beans;

public class BGM7051 {

	private String claveDeBloqueo;
	private String entidad;
	private String centroAlta;
	private String cuenta;
	private String motivo;
	private String observaciones;
	private String lineaFinal;
	private String lineaCondicional;

	public String getCentroAlta() {
		return centroAlta;
	}

	public void setCentroAlta(String centroAlta) {
		this.centroAlta = centroAlta;
	}

	public String getClaveDeBloqueo() {
		return claveDeBloqueo;
	}

	public void setClaveDeBloqueo(String claveDeBloqueo) {
		this.claveDeBloqueo = claveDeBloqueo;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getLineaCondicional() {
		return lineaCondicional;
	}

	public void setLineaCondicional(String lineaCondicional) {
		this.lineaCondicional = lineaCondicional;
	}

	public String getLineaFinal() {
		return lineaFinal;
	}

	public void setLineaFinal(String lineaFinal) {
		this.lineaFinal = lineaFinal;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public BGM7051() {
	}
	
	public String  toString (){
		return "claveDeBloqueo: "+claveDeBloqueo+",entidad: "+entidad+",centroAlta: "+centroAlta+",cuenta: "+cuenta+",motivo: "+motivo+",observaciones: "+observaciones+",lineaFinal: "+lineaFinal+",lineaCondicional: "+lineaCondicional;
	}

}
