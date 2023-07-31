package com.bdv.infi.data;

public class CustodiaComisionTitulo {

	/**
	 * Identificador de la Comision
	 */
	private int idComision;

	/**
	 *Identificador del Titulo
	 */
	private String idTitulo;
	
	/**
	 * Porcentaje de la Comision
	 */
	private double pctComision;
	
	/**
	 * Monto de la Comision
	 */
	private double montoComision;
	
	/**
	 * Moneda Definida para el Monto de la Comision
	 */
	private String monedaComision;

	public int getIdComision() {
		return idComision;
	}

	public void setIdComision(int idComision) {
		this.idComision = idComision;
	}

	public String getIdTitulo() {
		return idTitulo;
	}

	public void setIdTitulo(String idTitulo) {
		this.idTitulo = idTitulo;
	}

	public String getMonedaComision() {
		return monedaComision;
	}

	public void setMonedaComision(String monedaComision) {
		this.monedaComision = monedaComision;
	}

	public double getMontoComision() {
		return montoComision;
	}

	public void setMontoComision(double montoComision) {
		this.montoComision = montoComision;
	}

	public double getPctComision() {
		return pctComision;
	}

	public void setPctComision(double pctComision) {
		this.pctComision = pctComision;
	}
	

	
}
