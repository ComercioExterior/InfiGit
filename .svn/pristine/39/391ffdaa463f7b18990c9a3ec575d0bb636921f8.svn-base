package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;


/**
 * OrdenTitulo resultado de una Toma de Orden
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class TOrdenTitulo implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Identificador del Titulo
	 */
	private String idTitulo;
	/**
	 * Descripcion del OrdenTitulo
	 */
	private String descrTitulo = "";
	/**
	 * Porcentaje que forma parte del 100% de los títulos 
	 */
	private String porcentaje;
	/**
	 * Porcentaje de Recompra
	 */
	private String porcentajeRecompra;
	/**
	 * Monto calculado en base al porcentaje definido
	 */
	private String valorNominal;
	/**
	 * Monto calculado en base al porcentaje definido
	 */
	private String valorInvertido = "0";
	
	/**
	 * Fecha de Emisi&oacute;n del T&iacute;tulo
	 */
	private String fechaEmision;
	/**
	 * Fecha de vencimiento del T&iacute;tulo
	 */
	private String fechaVencimiento;
	/**
	 * Tasa cupon vigente para ese momento
	 */
	private String tasaCuponVigente;
	/**
	 * Id del T&iacute;tulo.
	 */
	private String monedaId;
	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public TOrdenTitulo () throws Exception {
		
	}
	
	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public TOrdenTitulo (Object [] objValor) throws Exception {
		this.idTitulo = (String)objValor[1];
		this.descrTitulo = (String)objValor[2];
		this.valorNominal = (String)objValor[3];
		this.porcentaje  = (String)objValor[4];
		this.valorInvertido = (String)objValor[6];
		this.monedaId=(String)objValor[7];
		this.porcentajeRecompra=(String)objValor[8];
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
	 * Retorna el valor del ValorIdMoneda Nominal de Titulo
	 * @return valorNominal
	 */
	public String getValorNominal() {
		return valorNominal;
	}
	/**
	 * Asigna valor al  ValorIdMoneda Nominal de Titulo
	 * @param valorNominal
	 */	
	public void setValorNominal(String valorNominal) {
		this.valorNominal = valorNominal;
	}
	
	/**
	 * Retorna el valor del Porcentaje que forma parte del 100% de los títulos 
	 * @return
	 */
	public String getPorcentaje() {
		return porcentaje;
	}
	/**
	 * Asigna valor al Porcentaje que forma parte del 100% de los títulos 
	 * @param porcentaje
	 */	
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	
	/**
	 * Retorna el valor del Monto calculado en base al porcentaje definido
	 * @return valorInvertido
	 */
	public String getValorInvertido() {
		return valorInvertido;
	}
	/**
	 * Asigna valor al Monto calculado en base al porcentaje definido
	 * @param valorInvertido
	 */	
	public void setValorInvertido(String montoInversion) {
		this.valorInvertido = montoInversion;
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
	 * @return the porcentajeRecompra
	 */
	public String getPorcentajeRecompra() {
		return porcentajeRecompra;
	}

	/**
	 * @param porcentajeRecompra the porcentajeRecompra to set
	 */
	public void setPorcentajeRecompra(String porcentajeRecompra) {
		this.porcentajeRecompra = porcentajeRecompra;
	}
}
