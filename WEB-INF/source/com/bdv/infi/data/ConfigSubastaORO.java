package com.bdv.infi.data;

import java.io.Serializable;

public class ConfigSubastaORO implements Serializable{
	/**
	 * Clase entidad que contiene los datos de una solicitud DICOM
	 */
	
	private long idUnidadInv;
	
	private String empresId;
	private String vehiculoBDVId;
	private String blotterId;
	//******** TTS-546-DICOM_INTERCANCARIO NM26659_28/01/2018
	//Seccion de codigo agregada para manejo de adjudicacion de 2 tipos de solicitudes (Oferta y Demanda) de forma simultanea 
	//Se agrega el manejo de blotter por unidad de inversion
	private String blotterIdOferta;
	private String blotterIdDemanda;
	
	
	public String getBlotterIdOferta() {
		return blotterIdOferta;
	}
	public void setBlotterIdOferta(String blotterIdOferta) {
		this.blotterIdOferta = blotterIdOferta;
	}
	public String getBlotterIdDemanda() {
		return blotterIdDemanda;
	}
	public void setBlotterIdDemanda(String blotterIdDemanda) {
		this.blotterIdDemanda = blotterIdDemanda;
	}
			
	//Se guardan los valores asociados a los codigos de operacion por Oferta
	private String codOperacionBloCapOferta;
	private String codOperacionBloComOferta;
	private String codOperacionDebCapOferta;
	private String codOperacionCreCapOferta;
	private String codOperacionDebComOferta;
	private String codOperacionCreConv20Oferta;
	//Se guardan los valores asociados a los codigos de operacion por Demanda	
	private String codOperacionBloCapDemanda;
	private String codOperacionBloComDemanda;
	private String codOperacionDebCapDemanda;
	private String codOperacionCreCapDemanda;
	private String codOperacionDebComDemanda;
	private String codOperacionCreConv20Demanda;
	
	
	public String getCodOperacionBloCapOferta() {
		return codOperacionBloCapOferta;
	}
	public void setCodOperacionBloCapOferta(String codOperacionBloCapOferta) {
		this.codOperacionBloCapOferta = codOperacionBloCapOferta;
	}
	public String getCodOperacionBloComOferta() {
		return codOperacionBloComOferta;
	}
	public void setCodOperacionBloComOferta(String codOperacionBloComOferta) {
		this.codOperacionBloComOferta = codOperacionBloComOferta;
	}
	public String getCodOperacionDebCapOferta() {
		return codOperacionDebCapOferta;
	}
	public void setCodOperacionDebCapOferta(String codOperacionDebCapOferta) {
		this.codOperacionDebCapOferta = codOperacionDebCapOferta;
	}
	public String getCodOperacionCreCapOferta() {
		return codOperacionCreCapOferta;
	}
	public void setCodOperacionCreCapOferta(String codOperacionCreCapOferta) {
		this.codOperacionCreCapOferta = codOperacionCreCapOferta;
	}
	public String getCodOperacionDebComOferta() {
		return codOperacionDebComOferta;
	}
	public void setCodOperacionDebComOferta(String codOperacionDebComOferta) {
		this.codOperacionDebComOferta = codOperacionDebComOferta;
	}
	public String getCodOperacionCreConv20Oferta() {
		return codOperacionCreConv20Oferta;
	}
	public void setCodOperacionCreConv20Oferta(String codOperacionCreConv20Oferta) {
		this.codOperacionCreConv20Oferta = codOperacionCreConv20Oferta;
	}
	public String getCodOperacionBloCapDemanda() {
		return codOperacionBloCapDemanda;
	}
	public void setCodOperacionBloCapDemanda(String codOperacionBloCapDemanda) {
		this.codOperacionBloCapDemanda = codOperacionBloCapDemanda;
	}
	public String getCodOperacionBloComDemanda() {
		return codOperacionBloComDemanda;
	}
	public void setCodOperacionBloComDemanda(String codOperacionBloComDemanda) {
		this.codOperacionBloComDemanda = codOperacionBloComDemanda;
	}
	public String getCodOperacionDebCapDemanda() {
		return codOperacionDebCapDemanda;
	}
	public void setCodOperacionDebCapDemanda(String codOperacionDebCapDemanda) {
		this.codOperacionDebCapDemanda = codOperacionDebCapDemanda;
	}
	public String getCodOperacionCreCapDemanda() {
		return codOperacionCreCapDemanda;
	}
	public void setCodOperacionCreCapDemanda(String codOperacionCreCapDemanda) {
		this.codOperacionCreCapDemanda = codOperacionCreCapDemanda;
	}
	public String getCodOperacionDebComDemanda() {
		return codOperacionDebComDemanda;
	}
	public void setCodOperacionDebComDemanda(String codOperacionDebComDemanda) {
		this.codOperacionDebComDemanda = codOperacionDebComDemanda;
	}
	public String getCodOperacionCreConv20Demanda() {
		return codOperacionCreConv20Demanda;
	}
	public void setCodOperacionCreConv20Demanda(String codOperacionCreConv20Demanda) {
		this.codOperacionCreConv20Demanda = codOperacionCreConv20Demanda;
	}
	//************
	private String codOperacionBloCap;
	private String codOperacionBloCom;
	private String codOperacionDebCap;
	private String codOperacionCreCap;
	private String codOperacionDebCom;
	private String codOperacionCreConv20;
	
	private String username;
	private String ip;
	
	private String nroJornada;
	
	public String getNroJornada() {
		return nroJornada;
	}
	public void setNroJornada(String nroJornada) {
		this.nroJornada = nroJornada;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	private String ejecucionId;
	
	public long getIdUnidadInv() {
		return idUnidadInv;
	}
	public void setIdUnidadInv(long idUnidadInv) {
		this.idUnidadInv = idUnidadInv;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEjecucionId() {
		return ejecucionId;
	}
	
	public void setEjecucionId(String ejecucionId) {
		this.ejecucionId = ejecucionId;
	}
	public String getEmpresId() {
		return empresId;
	}
	public void setEmpresId(String empresId) {
		this.empresId = empresId;
	}
	public String getVehiculoBDVId() {
		return vehiculoBDVId;
	}
	public void setVehiculoBDVId(String vehiculoBDVId) {
		this.vehiculoBDVId = vehiculoBDVId;
	}
	public String getBlotterId() {
		return blotterId;
	}
	public void setBlotterId(String blotterId) {
		this.blotterId = blotterId;
	}
	public String getCodOperacionBloCap() {
		return codOperacionBloCap;
	}
	public void setCodOperacionBloCap(String codOperacionBloCap) {
		this.codOperacionBloCap = codOperacionBloCap;
	}
	public String getCodOperacionBloCom() {
		return codOperacionBloCom;
	}
	public void setCodOperacionBloCom(String codOperacionBloCom) {
		this.codOperacionBloCom = codOperacionBloCom;
	}
	public String getCodOperacionDebCap() {
		return codOperacionDebCap;
	}
	public void setCodOperacionDebCap(String codOperacionDebCap) {
		this.codOperacionDebCap = codOperacionDebCap;
	}
	public String getCodOperacionCreCap() {
		return codOperacionCreCap;
	}
	public void setCodOperacionCreCap(String codOperacionCreCap) {
		this.codOperacionCreCap = codOperacionCreCap;
	}
	public String getCodOperacionDebCom() {
		return codOperacionDebCom;
	}
	public void setCodOperacionDebCom(String codOperacionDebCom) {
		this.codOperacionDebCom = codOperacionDebCom;
	}
	public String getCodOperacionCreConv20() {
		return codOperacionCreConv20;
	}
	public void setCodOperacionCreConv20(String codOperacionCreConv20) {
		this.codOperacionCreConv20 = codOperacionCreConv20;
	}
	
	
}
