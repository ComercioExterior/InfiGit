package com.bdv.infi.data;

import java.math.BigDecimal;

/**Entidad de persona contratada del Banco de Venezuela*/
public class TasaCambioBCV {
	
	String moneda;
	BigDecimal tasaVenta;
	BigDecimal tasaCompra;
	
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getTasaVenta() {
		return tasaVenta;
	}
	public void setTasaVenta(BigDecimal tasaVenta) {
		this.tasaVenta = tasaVenta;
	}
	public BigDecimal getTasaCompra() {
		return tasaCompra;
	}
	public void setTasaCompra(BigDecimal tasaCompra) {
		this.tasaCompra = tasaCompra;
	}
	
	
	
}
