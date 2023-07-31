package org.bcv.serviceINTERVENCION;

import java.math.BigDecimal;

public class operaciones {

	String codigoCliente;
	String nombreCliente;
	String fechaValor;
	String codigoTipoOperacion;
	double montoDivisa;
	double tipoCambio;
	String codigoCuentaDivisa;
	String codigoCuentaBs;

	private codigoIsoDivisa codigoIsoDivisa;
	private codigoVentaBCV codigoVentaBCV;
	private operacion operacion;
	
	public operacion getOperacion() {
		return operacion;
	}
	public void setOperacion(operacion operacion) {
		this.operacion = operacion;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getFechaValor() {
		return fechaValor;
	}
	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}
	public String getCodigoTipoOperacion() {
		return codigoTipoOperacion;
	}
	public void setCodigoTipoOperacion(String codigoTipoOperacion) {
		this.codigoTipoOperacion = codigoTipoOperacion;
	}
	public double getMontoDivisa() {
		return montoDivisa;
	}
	public void setMontoDivisa(double montoDivisa) {
		this.montoDivisa = montoDivisa;
	}
	public double getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getCodigoCuentaDivisa() {
		return codigoCuentaDivisa;
	}
	public void setCodigoCuentaDivisa(String codigoCuentaDivisa) {
		this.codigoCuentaDivisa = codigoCuentaDivisa;
	}
	public String getCodigoCuentaBs() {
		return codigoCuentaBs;
	}
	public void setCodigoCuentaBs(String codigoCuentaBs) {
		this.codigoCuentaBs = codigoCuentaBs;
	}
	public codigoVentaBCV getCodigoVentaBCV() {
		return codigoVentaBCV;
	}
	public void setCodigoVentaBCV(codigoVentaBCV codigoVentaBCV) {
		this.codigoVentaBCV = codigoVentaBCV;
	}
	public codigoIsoDivisa getCodigoIsoDivisa() {
		return codigoIsoDivisa;
	}
	public void setCodigoIsoDivisa(codigoIsoDivisa codigoIsoDivisa) {
		this.codigoIsoDivisa = codigoIsoDivisa;
	}
	
}
