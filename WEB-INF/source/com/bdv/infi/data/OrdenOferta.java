package com.bdv.infi.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Clase usada para el manejo de las ordenes de Ofertas del sistema SIMADI
 * Bean de informacion asociado a la tabla INFI_TB_225_OFERTAS_SIMADI
 */
//
public class OrdenOferta implements Serializable,Cloneable {
	
	//Campo ID_OFERTA en dato String
	private String idOrdenOfertaString;
	//Campo ID_OFERTA en dato long
	private long idOrdenOferta;

	//ORDENE_ID_BCV 
	private String idBcvOferta;
	//ORDENE_ESTATUS_BCV
	private String estatusBcvOferta;
	
	//Informacion asociada al cliente hace referencia a los campos  CLIENT_CEDRIF,TIPPER_ID,CLIENT_NOMBRE
	private Cliente Cliente;
	//ORDENE_MONTO_OFERTADO
	private BigDecimal totalOfertado;
	//ORDENE_TASA_CAMBIO
	private BigDecimal tasaOfertada;
	//NRO_JORNADA
	private String nroJornada;	
	//COD_INSTITUCION
	private String codInstitucion;
	//ESTATUS_CRUCE
	private String statusCruce;  
	//DEALNO	
	private String nroDeal;		
	//FECHA_PACTO formato String
	private String fechapactoString;
	//FECHA_PACTO formato Date
	private Date fechapacto;
	
	public String getIdOrdenOfertaString() {
		return idOrdenOfertaString;
	}
	public void setIdOrdenOfertaString(String idOrdenOfertaString) {
		this.idOrdenOfertaString = idOrdenOfertaString;
	}
	public long getIdOrdenOferta() {
		return idOrdenOferta;
	}
	public void setIdOrdenOferta(long idOrdenOferta) {
		this.idOrdenOferta = idOrdenOferta;
	}
	public String getIdBcvOferta() {
		return idBcvOferta;
	}
	public void setIdBcvOferta(String idBcvOferta) {
		this.idBcvOferta = idBcvOferta;
	}
	public String getEstatusBcvOferta() {
		return estatusBcvOferta;
	}
	public void setEstatusBcvOferta(String estatusBcvOferta) {
		this.estatusBcvOferta = estatusBcvOferta;
	}
	public Cliente getCliente() {
		return Cliente;
	}
	public void setCliente(Cliente cliente) {
		Cliente = cliente;
	}
	public BigDecimal getTotalOfertado() {
		return totalOfertado;
	}
	public void setTotalOfertado(BigDecimal totalOfertado) {
		this.totalOfertado = totalOfertado;
	}
	public BigDecimal getTasaOfertada() {
		return tasaOfertada;
	}
	public void setTasaOfertada(BigDecimal tasaOfertada) {
		this.tasaOfertada = tasaOfertada;
	}
	public String getNroJornada() {
		return nroJornada;
	}
	public void setNroJornada(String nroJornada) {
		this.nroJornada = nroJornada;
	}
	public String getCodInstitucion() {
		return codInstitucion;
	}
	public void setCodInstitucion(String codInstitucion) {
		this.codInstitucion = codInstitucion;
	}
	public String getStatusCruce() {
		return statusCruce;
	}
	public void setStatusCruce(String statusCruce) {
		this.statusCruce = statusCruce;
	}
	public String getNroDeal() {
		return nroDeal;
	}
	public void setNroDeal(String nroDeal) {
		this.nroDeal = nroDeal;
	}
	public String getFechapactoString() {
		return fechapactoString;
	}
	public void setFechapactoString(String fechapactoString) {
		this.fechapactoString = fechapactoString;
	}
	public Date getFechapacto() {
		return fechapacto;
	}
	public void setFechapacto(Date fechapacto) {
		this.fechapacto = fechapacto;
	}
	
	
}
