package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;

/**
 * OrdenRespuesta  
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class OrdenRespuesta implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Identificador de la Orden 
	 */
	private String idOrden = "0";
	/**
	 * Empresa que actúa como emisor. Sólo aplica para ordenes de tipo TOMA_ORDEN
	 */
	private String emisor;
	/**
	 * Tasa de cambio en el momento de capturar la orden 
	 */
	private String tasa;
	/**
	 * Indicador del tipo de instrumento (subasta, subasta competitiva, inventario, inventario con precio). Sólo aplica cuando es de tipo TOMA_ORDEN 
	 */
	private String instrumentoFinanciero;
	/**
	 * Número de emisión – serie (sólo aplica cuando es de tipo TOMA_ORDEN)
	 */
	private String serie;
	/**
	 * Fecha de emisión de la unidad de inversión (sólo aplica cuando es de tipo TOMA_ORDEN)
	 */
	private String fechaEmision;
	/**
	 * Fecha de Adjudicacion
	 */
	private String fechaAdjudicacion;
	/**
	 * Fecha valor de la orden
	 */
	private String fechaValorOrden;
	/**
	 * Moneda de la unidad de inversi&oacute;n
	 */
	private String monedaId;
	
	/** 
	 * Descripcion de la transacción 
	 **/
	private String descrTransaccion = "";
	/**
	 * Nombre asignado de la Unidad de Inversion
	 */
	private String nombreUnidadInversion = "";
	/**
	 * Fecha en la que se efectuó la orden 
	 */
	private String fechaOrden = "";
	/**
	 * Nombre del Cliente
	 */
	private String nombreCliente = "";
	/**
	 * Status de la Orden
	 */
	private String descrStatus = "";	

	/**
	 * Monto pedido en la toma de una orden
	 */
	private String montoPedido = "";
	/** 
	 * Monto Adjudicado a la Orden
	 **/
	private String montoAdjudicado = "";
	
	/**
	 * Monto de reintegro de capital. 
	 */
	private String montoReintegroCapital;
	/**
	 * Monto de reintegro de Comisi&oacute;n.
	 */
	private String montoReintegroComision;
	
	/**
	 * Depositario
	 */
	private String depositario = "";
	/**
	 * Contraparte
	 */
	private String contraparte = "";
	/**
	 * Operaciones financieras de una orden
	 */
	private OperacionesFinancierasOrden operacionesFinancieras;
	/**
	 * Constructor del bean
	 */
	public OrdenRespuesta () throws Exception {
	}
	
	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public OrdenRespuesta (Object [] objValor) throws Exception {
		this.idOrden =(String)objValor[0];
		this.descrTransaccion =(String)objValor[1];
		this.nombreUnidadInversion =(String)objValor[2];
		this.fechaOrden = (String)objValor[3];
		this.nombreCliente  = (String)objValor[4];
		this.descrStatus  = (String)objValor[5];
		this.montoPedido = (String)objValor[6];	
		this.montoAdjudicado = (String)objValor[7];	
		this.depositario = (String)objValor[8];
		this.contraparte  = (String)objValor[9];
		this.tasa  = (String)objValor[10];
		this.fechaAdjudicacion  = (String)objValor[11];
		this.fechaValorOrden  = (String)objValor[12];
		this.emisor = ((String)objValor[13]!=null?(String)objValor[13]:"");
		this.instrumentoFinanciero = ((String)objValor[14]!=null?(String)objValor[14]:"");
		this.serie = ((String)objValor[15]!=null?(String)objValor[15]:"");
		this.fechaEmision = ((String)objValor[16]!=null?(String)objValor[16]:"");
		this.monedaId = ((String)objValor[17]!=null?(String)objValor[17]:"");
		this.montoReintegroCapital = (String)objValor[18];
		this.montoReintegroComision = (String)objValor[19];
	}
	/**
	 * Devuelve el valor del Identificador de la TomaOrden 
	 * @return idOrden
	 */
	public String getIdOrden() {
		return idOrden;
	}
	/**
	 * Asigna valor al Identificador de la TomaOrden 
	 * @param idOrden
	 */
	public void setIdOrden(String idOrden) {
		this.idOrden = idOrden;
	}
	
	/**
	 * Devuelve el valor de la Descripcion de la Transaccion 
	 * @return descrTransaccion
	 */
	public String getDescrTransaccion() {
		return descrTransaccion;
	}
	/**
	 * Asigna valor a la Descripcion de la Transaccion
	 * @param descrTransaccion
	 */
	public void setDescrTransaccion(String idTransaccion) {
		this.descrTransaccion = idTransaccion;
	}
	
	/**
	 * Retorna el valor del atributo Nombre de la Unidad de Inversion
	 * @return
	 */
	public String getNombreUnidadInversion() {
		return nombreUnidadInversion;
	}
	/**
	 * Asigna valor al atributo Nombre de la Unidad de Inversion
	 * @param nombreUnidadInversion
	 */	
	public void setNombreUnidadInversion(String nombreUnidadInversion) {
		this.nombreUnidadInversion = nombreUnidadInversion;
	}
	
	/**
	 * Devuelve el valor de al Fecha en la que se efectuó la orden 
	 * @return fechaOrden
	 */
	public String getFechaOrden() {
		return fechaOrden;
	}
	/**
	 * Asigna valor a la Fecha en la que se efectuó la orden 
	 * @param fechaOrden
	 */	
	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}
	
	/**
	 * Retorna el valor del atributo Nombre del Cliente
	 * @return
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}
	/**
	 * Asigna valor al atributo Nombre del Cliente
	 * @param nombreUnidadInversion
	 */	
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	
	/**
	 * Devuelve el valor de la Descripcion del Status de la Orden
	 * @return descrStatus
	 */
	public String getDescrStatus() {
		return descrStatus;
	}
	/**
	 * Asigna valor a la Descripcion del Status de la Orden
	 * @param idSistema
	 */
	public void setDescrStatus(String descrStatus) {
		this.descrStatus = descrStatus;
	}

	/**
	 * Devuelve el valor del Monto pedido en la toma de una orden 
	 * @return montoPedido
	 */
	public String getMontoPedido() {
		return montoPedido;
	}
	/**
	 * Asigna valor al Monto pedido en la toma de una orden
	 * @param montoPedido
	 */
	public void setMontoPedido(String montoPedido) {
		this.montoPedido = montoPedido;
	}
	
	/**
	 * Devuelve el valor del Monto Adjudicado a la Orden
	 * @return montoAdjudicado
	 */
	public String getMontoAdjudicado() {
		return montoAdjudicado;
	}
	/**
	 * Asigna valor al Monto Adjudicado a la Orden
	 * @param montoAdjudicado
	 */
	public void setMontoAdjudicado(String montoAdjudicado) {
		this.montoAdjudicado = montoAdjudicado;
	}
	
	/**
	 * Devuelve el valor del Depositario de la Orden
	 * @return depositario
	 */
	public String getDepositario() {
		return depositario;
	}
	/**
	 * Asigna valor al Depositario de la Orden
	 * @param depositario
	 */
	public void setDepositario(String depositario) {
		this.depositario = depositario;
	}
	
	/**
	 * Devuelve el valor del Contraparte de la Orden
	 * @return contraparte
	 */
	public String getContraparte() {
		return contraparte;
	}
	/**
	 * Asigna valor al Contraparte de la Orden
	 * @param contraparte
	 */
	public void setContraparte(String contraparte) {
		this.contraparte = contraparte;
	}

	/**
	 * @return the emisor
	 */
	public String getEmisor() {
		return emisor;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	/**
	 * @return the fechaAdjudicacion
	 */
	public String getFechaAdjudicacion() {
		return fechaAdjudicacion;
	}

	/**
	 * @param fechaAdjudicacion the fechaAdjudicacion to set
	 */
	public void setFechaAdjudicacion(String fechaAdjudicacion) {
		this.fechaAdjudicacion = fechaAdjudicacion;
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
	 * @return the fechaValorOrden
	 */
	public String getFechaValorOrden() {
		return fechaValorOrden;
	}

	/**
	 * @param fechaValorOrden the fechaValorOrden to set
	 */
	public void setFechaValorOrden(String fechaValorOrden) {
		this.fechaValorOrden = fechaValorOrden;
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
	 * @return the montoReintegroCapital
	 */
	public String getMontoReintegroCapital() {
		return montoReintegroCapital;
	}

	/**
	 * @param montoReintegroCapital the montoReintegroCapital to set
	 */
	public void setMontoReintegroCapital(String montoReintegroCapital) {
		this.montoReintegroCapital = montoReintegroCapital;
	}

	/**
	 * @return the montoReintegroComision
	 */
	public String getMontoReintegroComision() {
		return montoReintegroComision;
	}

	/**
	 * @param montoReintegroComision the montoReintegroComision to set
	 */
	public void setMontoReintegroComision(String montoReintegroComision) {
		this.montoReintegroComision = montoReintegroComision;
	}

	/**
	 * @return the operacionesFinancieras
	 */
	public OperacionesFinancierasOrden getOperacionesFinancieras() {
		return operacionesFinancieras;
	}

	/**
	 * @param operacionesFinancieras the operacionesFinancieras to set
	 */
	public void setOperacionesFinancieras(
			OperacionesFinancierasOrden operacionesFinancieras) {
		this.operacionesFinancieras = operacionesFinancieras;
	}

	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
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
	 * @return the tipoInstrumento
	 */
	public String getInstrumentoFinanciero() {
		return instrumentoFinanciero;
	}

	/**
	 * @param tipoInstrumento the tipoInstrumento to set
	 */
	public void setInstrumentoFinanciero(String instrumentoFinanciero) {
		this.instrumentoFinanciero = instrumentoFinanciero;
	}

}
