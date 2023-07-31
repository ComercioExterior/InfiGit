package com.bdv.infi.data;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class OrdenBloqueoExportarExcel implements Serializable {
	private long idOrden;
	private String idOrdenString;
	private String nroCuenta;
	private String tipoCuenta;
	private BigDecimal montoCapital;
	private BigDecimal montoComision;
	private BigDecimal montoDesbloqueo;
	
	public long getIdOrden() {
		return idOrden;
	}
	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}
	public BigDecimal getMontoCapital() {
		return montoCapital;
	}
	public void setMontoCapital(BigDecimal montoCapital) {
		this.montoCapital = montoCapital;
	}
	public BigDecimal getMontoComision() {
		return montoComision;
	}
	public void setMontoComision(BigDecimal montoComision) {
		this.montoComision = montoComision;
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}
	public String getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	public BigDecimal getMontoDesbloqueo() {
		return montoDesbloqueo;
	}
	public void setMontoDesbloqueo(BigDecimal montoDesbloqueo) {
		this.montoDesbloqueo = montoDesbloqueo;
	}
	public String getIdOrdenString() {
		return idOrdenString;
	}
	public void setIdOrdenString(String idOrdenString) {
		this.idOrdenString = idOrdenString;
	}
		
}
