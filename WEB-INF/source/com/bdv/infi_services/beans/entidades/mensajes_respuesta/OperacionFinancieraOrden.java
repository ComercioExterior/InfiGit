package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;

public class OperacionFinancieraOrden implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Monto de la operaci&oacute;n financiera
	 */
	private String monto;
	/**
	 * Tasa de la operaci&oacute;n financiera
	 */
	private String tasa;
	/**
	 * Moneda de la operaci&oacute;n financiera
	 */
	private String monedaId;
	/**
	 * Tipo de la operaci&oacute;n financiera
	 */
	private String tipoOperacion;
	/**
	 * Indica si la operaci&oacute;n financiera es o no de comisi&oacute;n
	 */
	private String indicadorComision;
	/**
	 * @return the indicadorComision
	 */
	public String getIndicadorComision() {
		return indicadorComision;
	}
	/**
	 * @param indicadorComision the indicadorComision to set
	 */
	public void setIndicadorComision(String indicadorComision) {
		this.indicadorComision = indicadorComision;
	}
	/**
	 * @return the monedaId
	 */
	public String getMonedaId() {
		return monedaId;
	}
	/**
	 * @param monedaId the monedaId to set
	 */
	public void setMonedaId(String monedaId) {
		this.monedaId = monedaId;
	}
	/**
	 * @return the monto
	 */
	public String getMonto() {
		return monto;
	}
	/**
	 * @param monto the monto to set
	 */
	public void setMonto(String monto) {
		this.monto = monto;
	}
	/**
	 * @return the tasa
	 */
	public String getTasa() {
		return tasa;
	}
	/**
	 * @param tasa the tasa to set
	 */
	public void setTasa(String tasa) {
		this.tasa = tasa;
	}
	/**
	 * @return the tipoOperacion
	 */
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	/**
	 * @param tipoOperacion the tipoOperacion to set
	 */
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
}
