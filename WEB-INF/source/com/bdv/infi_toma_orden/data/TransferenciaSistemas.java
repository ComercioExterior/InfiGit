package com.bdv.infi_toma_orden.data;

public class TransferenciaSistemas {
	private String trnfinId="";
	private String sistemaID="";
	private String serial="";
	private String bloterId="";
	private String centroContable="";
	private String codigoOperacionDebito="";
	private String codigoOperacionCredito="";
	private String codigoOperacionBloqueo="";	
	private long codigoUnico=0;
	private long unidadnversion=0;
	
	
	public long getUnidadnversion() {
		return unidadnversion;
	}
	public void setUnidadnversion(long unidadnversion) {
		this.unidadnversion = unidadnversion;
	}
	public String getBloterId() {
		return bloterId;
	}
	public void setBloterId(String bloterId) {
		this.bloterId = bloterId;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getSistemaID() {
		return sistemaID;
	}
	public void setSistemaID(String sistemaID) {
		this.sistemaID = sistemaID;
	}
	public String getTrnfinId() {
		return trnfinId;
	}
	public void setTrnfinId(String trnfinId) {
		this.trnfinId = trnfinId;
	}
	public String getCentroContable() {
		return centroContable;
	}
	public void setCentroContable(String centroContable) {
		this.centroContable = centroContable;
	}
	public String getCodigoOperacionDebito() {
		return codigoOperacionDebito;
	}
	public void setCodigoOperacionDebito(String codigoOperacionDebito) {
		this.codigoOperacionDebito = codigoOperacionDebito;
	}
	public long getCodigoUnico() {
		return codigoUnico;
	}
	public void setCodigoUnico(long codigoUnico) {
		this.codigoUnico = codigoUnico;
	}
	public String getCodigoOperacionCredito() {
		return codigoOperacionCredito;
	}
	public void setCodigoOperacionCredito(String codigoOperacionCredito) {
		this.codigoOperacionCredito = codigoOperacionCredito;
	}
	public String getCodigoOperacionBloqueo() {
		return codigoOperacionBloqueo;
	}
	public void setCodigoOperacionBloqueo(String codigoOperacionBloqueo) {
		this.codigoOperacionBloqueo = codigoOperacionBloqueo;
	}
	
	
}
