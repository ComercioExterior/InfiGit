package com.bdv.infi_toma_orden.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Documentos configurados para un tipo de transacción específica
 */
public class ReglaTransaccionFinanciera extends DataRegistro implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7354670968725035071L;
	/**
	 * Id de la Regla de Transacción Financiera
	 */
	private long idReglaTransaccionFinanciera;
	/**
	 * Id de Unidad de Inversión
	 */
	private long idUnidadInversion;
	/**
	 * Id de la Transacción Financiera
	 */
	private String idTransaccionFinanciera;
	/**
	 * Tipo de aplicación de la Transacción Financiera: Monto Fijo o Porcentaje
	 */
	private String tipoAplicacion;	
	/**
	 * Id de la Transacción de Negocio
	 */
	private String idTransaccionNegocio;
	/**
	 * Id de Moneda
	 */
	private String idMoneda;
	/**
	 * Id del blotter emisor
	 */
	private String idBlotter;
	/** 
	 * Id de la empresa 
	 **/
	private String idEmpresa = "";
	/**
	 * Id de Tipo de Persona
	 */
	private String tipoPersona;
	/**
	 * Monto fijo que se debe aplicar a la transaccion
	 */
	private BigDecimal montoAsignar = new BigDecimal(0);
	/**
	 * Porcentaje que se debe aplicar a la transaccion
	 */
	private BigDecimal porcentajeAsignar = new BigDecimal(0);
	/**
	 * Rango Mínimo aplicado a la Regla de Transacción Financiera
	 */
	private BigDecimal rangoMinimo;
	/**
	 * Rango Máximo aplicado a la Regla de Transacción Financiera
	 * 
	 */
	private BigDecimal rangoMaximo;
	/**
	 * Valor aplicado a la Transacción Financiera
	 */
	private BigDecimal valor;
	/**
	 * Función asociada a la Transacción Financiera
	 */
	private String funcion;
	/**
	 * Indicador de Transacción Financiera de Comisión
	 */
	private boolean indicadorComision;
	
	/**Descripción de la transacción financiera*/
	private String descripcionTransaccion = "";
	
	/**
	 * Tipo de Transacci&oacute;n Financiera
	 */
	private String tipoTranscaccionFinanc = "";
	
	/**
	 * Serial de Transacción Financiera
	 */
	private String serialTransaccionFinanciera ="";
	
	public String getSerialTransaccionFinanciera() {
		return serialTransaccionFinanciera;
	}
	public void setSerialTransaccionFinanciera(String serialTransaccionFinanciera) {
		this.serialTransaccionFinanciera = serialTransaccionFinanciera;
	}	
	/**
	 * Devuelve el valor del Identificador de la Regla de TF
	 * @return idReglaTransaccionFinanciera
	 */
	public long getIdReglaTransaccionFinanciera() {
		return idReglaTransaccionFinanciera;
	}
	/**
	 * Asigna valor al Identificador de la Regla de TF
	 * @param idReglaTransaccionFinanciera
	 */
	public void setIdReglaTransaccionFinanciera(long idReglaTransaccionFinanciera) {
		this.idReglaTransaccionFinanciera = idReglaTransaccionFinanciera;
	}
	
	/**
	 * Devuelve el valor del Identificador de la Unidad Inversion
	 * @return idUnidadInversion
	 */
	public long getIdUnidadInversion() {
		return idUnidadInversion;
	}
	/**
	 * Asigna valor al Identificador de la Unidad de Inversion
	 * @param idUnidadInversion
	 */
	public void setIdUnidadInversion(long idUnidadInversion) {
		this.idUnidadInversion = idUnidadInversion;
	}
	
	/**
	 * Devuelve el valor del Identificador de la Transaccion Financiera
	 * @return idReglaTransaccionFinanciera
	 */
	public String getIdTransaccionFinanciera() {
		return idTransaccionFinanciera;
	}
	/**
	 * Asigna valor al Identificador de la Transaccion Financiera
	 * @param idTransaccionFinanciera
	 */
	public void setIdTransaccionFinanciera(String idTransaccionFinanciera) {
		this.idTransaccionFinanciera = idTransaccionFinanciera;
	}
	
	/**
	 * Retorna el valor del Tipo de aplicación 
	 * @return tipoAplicacion
	 */
	public String getTipoAplicacion() {
		return tipoAplicacion;
	}
	/**
	 * Asigna valor al Tipo de aplicación
	 * @param tipoAplicacion
	 */
	public void setTipoAplicacion(String tipoAplicacion) {
		this.tipoAplicacion = tipoAplicacion;
	}
	
	/**
	 * Devuelve el valor del Identificador de la Transaccion Financiera
	 * @return idTransaccionNegocio
	 */
	public String getIdTransaccionNegocio() {
		return idTransaccionNegocio;
	}
	/**
	 * Asigna valor al Identificador de la Transaccion Financiera
	 * @param idTransaccionNegocio
	 */
	public void setIdTransaccionNegocio(String idTransaccionNegocio) {
		this.idTransaccionNegocio = idTransaccionNegocio;
	}
	
	/**
	 * Devuelve el valor de la Moneda de la UI
	 * @return idMoneda
	 */
	public String getIdMoneda() {
		return idMoneda;
	}
	/**
	 * Asigna valor a la Moneda de la UI
	 * @param idMoneda
	 */
	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}
	
	/**
	 * Devuelve el valor del Identificador del Blotter
	 * @return idBlotter
	 */
	public String getIdBlotter() {
		return idBlotter;
	}
	/**
	 * Asigna valor al Identificador del Blotter
	 * @param idBlotter
	 */
	public void setIdBlotter(String idBloter) {
		this.idBlotter = idBloter;
	}
	
	/**
	 * Devuelve el valor del Identificador de la Empresa
	 * @return idEmpresa
	 */
	public String getIdEmpresa() {
		return idEmpresa;
	}
	/**
	 * Asigna valor al Identificador de la Empresa
	 * @param idEmpresa
	 */
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	/**
	 * Devuelve el valor del Tipo de Persona
	 * @return tipoPersona
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}
	/**
	 * Asigna valor al Tipo de Persona
	 * @param tipoPersona
	 */
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	/**
	 * Devuelve el valor del Monto pedido en la toma de una orden 
	 * @return montoAsignar
	 */
	public BigDecimal getMontoAsignar() {
		return montoAsignar;
	}
	/**
	 * Asigna valor al Monto pedido en la toma de una orden
	 * @param montoAsignar
	 */
	public void setMontoAsignar(BigDecimal montoPedido) {
		this.montoAsignar = montoPedido;
	}	
	
	/**
	 * Retorna el valor del Porcentaje que forma parte del 100% de los títulos 
	 * @return
	 */
	public BigDecimal getPorcentajeAsignar() {
		return porcentajeAsignar;
	}
	/**
	 * Asigna valor al Porcentaje que forma parte del 100% de los títulos 
	 * @param porcentajeAsignar
	 */	
	public void setPorcentajeAsignar(BigDecimal porcentaje) {
		this.porcentajeAsignar = porcentaje;
	}
	
	/**
	 * Retorna el valor del Rango Mínimo aplicado a la Regla 
	 * @return rangoMinimo
	 */
	public BigDecimal getRangoMinimo() {
		return rangoMinimo;
	}
	/**
	 * Asigna valor al Rango Mínimo aplicado a la Regla
	 * @param rangoMinimo
	 */	
	public void setRangoMinimo(BigDecimal rangoMinimo) {
		this.rangoMinimo = rangoMinimo;
	}

	/**
	 * Retorna el valor del Rango Máximo aplicado a la Regla
	 * @return rangoMaximo
	 */
	public BigDecimal getRangoMaximo() {
		return rangoMaximo;
	}
	/**
	 * Asigna valor al Rango Máximo aplicado a la Regla
	 * @param rangoMaximo
	 */	
	public void setRangoMaximo(BigDecimal rangoMaximo) {
		this.rangoMinimo = rangoMaximo;
	}

	/**
	 * Retorna el valor aplicado a la Transacción Financiera
	 * @return fechaAplicacion
	 */
	public BigDecimal getValor() {
		return valor;
	}
	/**
	 * Asigna el valor aplicado a la Transacción Financiera
	 * @param fechaAplicacion
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	/**
	 * Retorna el valor de la Función asociada a la Transacción Financiera
	 * @return funcion
	 */
	public String getFuncion() {
		return funcion;
	}
	/**
	 * Asigna valor a la Función asociada a la Transacción Financiera
	 * @param funcion
	 */
	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
	
	/**
	 * Retorna el valor del Indicador de Transacción Financiera de Comisión
	 * @return indicadorComision
	 */
	public boolean isIndicadorComision() {
		return indicadorComision;
	}
	/**
	 * Asigna valor al Indicador de Transacción Financiera de Comisión
	 * @param indicadorComision
	 */
	public void setIndicadorComision(boolean indicadorComision) {
		this.indicadorComision = indicadorComision;
	}
	public String getDescripcionTransaccion() {
		return descripcionTransaccion;
	}
	public void setDescripcionTransaccion(String descripcionTransaccion) {
		this.descripcionTransaccion = descripcionTransaccion;
	}
	public String getTipoTranscaccionFinanc() {
		return tipoTranscaccionFinanc;
	}
	public void setTipoTranscaccionFinanc(String tipoTranscaccionFinanc) {
		this.tipoTranscaccionFinanc = tipoTranscaccionFinanc;
	}
	
	
}
