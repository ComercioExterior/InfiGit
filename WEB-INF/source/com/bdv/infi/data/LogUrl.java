package com.bdv.infi.data;

public class LogUrl {
	/*
	 * Nombre del action
	 */
	private String action;
	
	/*Fecha de modificacion
	 */
	private String fecha;
	
	/*Hora Modificacion
	 */
	private String time;
	
	/*Maquina donde fue modificado 
	 */
	private String ip;
	
	/*Usuario que modifico
	 */
	private String usuario;
	
	/*Parametros que fueron modificados
	 */
	private String parametros;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	

}
