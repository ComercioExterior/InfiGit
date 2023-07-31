package com.bdv.infi.data;

public class CustodiaComisionDepositario {

	/**
	 * Identificador de la Comision
	 */
	private int idComision;

	/**
	 *Identificador de la Empresa
	 */
	private String idEmpresa;
	
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

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
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
	
}
