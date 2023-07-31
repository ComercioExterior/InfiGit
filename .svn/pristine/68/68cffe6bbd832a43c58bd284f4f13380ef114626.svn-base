package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;



/**
 * OredenResultado de la Simulacion de una Toma de Orden 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class TomaDeOrden implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Identificador de la Orden 
	 */
	private String idOrden = "0";
	/**
	 * Fecha de la Orden 
	 */
	private String fechaOrden;	
	/**
	 * Monto de la inversión en base al cálculo de las unidades 
	 */
	private String montoInvertido = "0";	
	/**
	 * Cantidad de UI ordenada
	 */
	private String cantidadOrdenada = "0";		
	/**
	 * Monto que sobra en base al cálculo de las unidades
	 */
	private String montoRemanente = "0";
	/**
	 * Monto total de las comisiones que se van a aplicar
	 */
	private String montoTotalComisiones = "0";
	/**
	 * Monto total de los intereses calculados
	 */
	private String montoIntereses = "0";
	/**
	 * Monto total a pagar capital + intereses + comisiones eb bsf
	 */
	private String montoTotal = "0";
	/**
	 * Tasa de cambio usada
	 */
	private String tasaCambio = "0";
	
	/**
	 * Documentos asociados a la Orden
	 */
	
	private DocumentosAsociados documentosAsociados;
	/**
	 * Constructor del bean 
	 */
	public TomaDeOrden () {		
	}
	
	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public TomaDeOrden (Object [] objValor) throws Exception {
		this.idOrden = (String)objValor[0];
		this.fechaOrden = (String)objValor[1];		
		this.montoInvertido = (String)objValor[2];
		this.cantidadOrdenada = (String)objValor[3];
		this.montoRemanente= (String)objValor[4];		
		this.montoTotalComisiones  = (String)objValor[5];
		this.montoIntereses = (String)objValor[6];
		this.montoTotal = (String)objValor[7];
		this.tasaCambio = (String)objValor[8];
	}
	
	/**
	 * Devuelve el valor del Identificador de la Orden 
	 * @return idOrden
	 */
	public String getIdOrden() {
		return idOrden;
	}
	/**
	 * Asigna valor al Identificador de la Orden 
	 * @param idOrden
	 */
	public void setIdOrden(String idOrden) {
		this.idOrden = idOrden;
	}
	
	/**
	 * Devuelve el valor de la Fecha de la Orden
	 * @return fechaOrden
	 */
	public String getFechaOrden() {
		return fechaOrden;
	}
	/**
	 * Asigna valor a la Fecha de la Orden
	 * @param fechaOrden
	 */
	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}
	
	/**
	 * Retorna el valor del Monto de la inversión en base al cálculo de las unidades
	 * @return montoInvertido
	 */
	public String getMontoInvertido() {
		return montoInvertido;
	}
	/**
	 * Asigna valor al Monto de la inversión en base al cálculo de las unidades
	 * @param montoInvertido
	 */	
	public void setMontoInvertido(String montoInversion) {
		this.montoInvertido = montoInversion;
	}
	
	/**
	 * Retorna el valor del Cantidad de UI ordenada
	 * @return cantidadOrdenada
	 */
	public String getCantidadOrdenada() {
		return cantidadOrdenada;
	}
	/**
	 * Asigna valor al Cantidad de UI ordenada
	 * @param montoInvertido
	 */	
	public void setCantidadOrdenada(String cantidadOrdenada) {
		this.cantidadOrdenada = cantidadOrdenada;
	}
	
	/**
	 * Retorna el valor del Monto que sobra en base al cálculo de las unidades
	 * @return montoRemanente
	 */
	public String getMontoRemanente() {
		return montoRemanente;
	}
	/**
	 * Asigna valor al Monto que sobra en base al cálculo de las unidades
	 * @param montoRemanente
	 */	
	public void setMontoRemanente(String montoRemanente) {
		this.montoRemanente = montoRemanente;
	}
	
	/**
	 * Retorna el valor del Monto total de las comisiones
	 * @return montoTotalComisiones
	 */
	public String getMontoTotalComisiones() {
		return montoTotalComisiones;
	}
	/**
	 * Asigna valor al Monto total de las comisiones
	 * @param montoTotalComisiones
	 */	
	public void setMontoTotalComisiones(String montoInversion) {
		this.montoTotalComisiones = montoInversion;
	}
	
	/**
	 * Retorna el valor del Monto total de los intereses calculados
	 * @return montoIntereses
	 */
	public String getMontoIntereses() {
		return montoIntereses;
	}
	/**
	 * Asigna valor al Monto total de los intereses calculados
	 * @param montoIntereses
	 */	
	public void setMontoIntereses(String montoInversion) {
		this.montoIntereses = montoInversion;
	}
	
	/**
	 * Retorna el valor del Monto total a pagar capital + intereses + comisiones
	 * @return montoTotal
	 */
	public String getMontoTotal() {
		return montoTotal;
	}
	/**
	 * Asigna valor al Monto total a pagar capital + intereses + comisiones
	 * @param montoTotal
	 */	
	public void setMontoTotal(String montoInversion) {
		this.montoTotal = montoInversion;
	}
	
	/**
	 * Devuelve el valor de la Tasa de Cambio utilizada 
	 * @return tasaCambio
	 */
	public String getTasaCambio() {
		return tasaCambio;
	}

	/**
	 * Asigna valor a  la Tasa de Cambio utilizada 
	 * @param tasaCambio
	 */
	public void setTasaCambio(String tasaCambio) {
		this.tasaCambio = tasaCambio;
	}

	/**
	 * @return the documentosAsociados
	 */
	public DocumentosAsociados getDocumentosAsociados() {
		return documentosAsociados;
	}

	/**
	 * @param documentosAsociados the documentosAsociados to set
	 */
	public void setDocumentosAsociados(DocumentosAsociados documentosAsociados) {
		this.documentosAsociados = documentosAsociados;
	}
}
