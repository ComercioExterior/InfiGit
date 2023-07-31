package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;

/**
 * OrdenTitulo resultado de una Toma de Orden
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class TituloCustodia implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Identificador del Titulo
	 */
	private String idTitulo;
	/**
	 * Moneda de Denominacion del T&iacute;tulo
	 */
	private String monedaId;
	/**
	 * Fecha de emision del T&iacute;tulo
	 */
	private String fechaEmision;
	
	/**
	 * Fecha de vencimiento del T&iacute;tulo
	 */
	private String fechaVencimiento;
	
	/**
	 * Tasa Actual del T@iacute;tulo
	 */
	private String tasaCuponVigente;
	/**
	 * Tasa de Cambio Vigente
	 */
	private String tasaCambioVigente;
	/**
	 * Descripcion del OrdenTitulo
	 */
	private String descrTitulo = "";
	/**
	 * Fecha en la que ingresó el título a custodia
	 */
	private String fechaIngreso = "";	
	/**
	 * Cantidad que posee el cliente en custodia
	 */
	private String valorNominal;
	/**
	 * Total nominal del titulo
	 */
	private String totalNominalTitulo;
	/**
	 * Cantidad bloqueada para un título en específico
	 */
	private String cantidadBloqueada;	
	
	/**
	 * Constructor del bean
	 */
	public TituloCustodia () throws Exception {
		
	}
	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public TituloCustodia (Object [] objValor) throws Exception {
		this.idTitulo = (String)objValor[0];
		this.descrTitulo = (String)objValor[1];
		this.fechaIngreso = (String)objValor[2];
		this.valorNominal = (String)objValor[3];
		cantidadBloqueada  = (String)objValor[4];
		this.monedaId = (String)objValor[5];
		this.fechaEmision = (String)objValor[6];
		this.fechaVencimiento = (String)objValor[7];
		this.tasaCuponVigente = (String)objValor[8];
		this.tasaCambioVigente = String.valueOf(objValor[9]);
		this.totalNominalTitulo = String.valueOf(objValor[10]);
	}
	
	/**
	 * Retorna el valor del Identificador del Titulo
	 * @return idTitulo
	 */
	public String getIdTitulo() {
		return idTitulo;
	}
	/**
	 * Asigna valor al Identificador del OrdenTitulo
	 * @param idTitulo
	 */	
	public void setIdTitulo(String idTitulo) {
		this.idTitulo = idTitulo;
	}
	
	/**
	 * Retorna el valor de la Descripcion del OrdenTitulo
	 * @return descrTitulo
	 */
	public String getDescrTitulo() {
		return descrTitulo;
	}
	/**
	 * Asigna valor a la Descripcion del OrdenTitulo
	 * @param descrTitulo
	 */	
	public void setDescrTitulo(String descrTitulo) {
		this.descrTitulo = descrTitulo;
	}
	
	/**
	 * Devuelve el valor de al Fecha en la que ingresó el título a custodia
	 * @return fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	/**
	 * Asigna valor a la Fecha en la que ingresó el título a custodia
	 * @param fechaIngreso
	 */	
	public void setFechaIngreso(String fechaOrden) {
		this.fechaIngreso = fechaOrden;
	}
	
	/**
	 * Retorna el valor de la Unidades compradas por el título
	 * @return cantidad
	 */
	public String getValorNominal() {
		return valorNominal;
	}
	/**
	 * Asigna valor a la Cantidad que posee el cliente en custodia
	 * @param numeroCuentaCliente
	 */
	public void setValorNominal(String valorNominal) {
		this.valorNominal = valorNominal;
	}
	
	/**
	 * Retorna el valor de la Cantidad bloqueada para un título en específico
	 * @return cantidadBloqueada
	 */
	public String getCantidadBloqueada() {
		return cantidadBloqueada;
	}
	/**
	 * Asigna valor a la Cantidad bloqueada para un título en específico
	 * @param cantidadBloqueada
	 */
	public void setCantidadBloqueada(String cantidadBloqueada) {
		this.cantidadBloqueada = cantidadBloqueada;
	}
	/**
	 * @return the fechaEmision
	 */
	public String getFechaEmision() {
		return fechaEmision;
	}
	/**
	 * @param fechaEmision the fechaEmision to set
	 */
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	/**
	 * @return the fechaVencimiento
	 */
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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
	 * @return the tasaCambioVigente
	 */
	public String getTasaCambioVigente() {
		return tasaCambioVigente;
	}
	/**
	 * @param tasaCambioVigente the tasaCambioVigente to set
	 */
	public void setTasaCambioVigente(String tasaCambioVigente) {
		this.tasaCambioVigente = tasaCambioVigente;
	}
	/**
	 * @return the tasaCuponVigente
	 */
	public String getTasaCuponVigente() {
		return tasaCuponVigente;
	}
	/**
	 * @param tasaCuponVigente the tasaCuponVigente to set
	 */
	public void setTasaCuponVigente(String tasaCuponVigente) {
		this.tasaCuponVigente = tasaCuponVigente;
	}
	/**
	 * @return the totalNominalTitulo
	 */
	public String getTotalNominalTitulo() {
		return totalNominalTitulo;
	}
	/**
	 * @param totalNominalTitulo the totalNominalTitulo to set
	 */
	public void setTotalNominalTitulo(String totalNominalTitulo) {
		this.totalNominalTitulo = totalNominalTitulo;
	}
	
}
