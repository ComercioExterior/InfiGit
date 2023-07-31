package com.bdv.infi_toma_orden.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Documentos configurados para un tipo de transacción específica
 */
public class TransaccionFinanciera extends DataRegistro implements Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = 5639493534756305993L;
	/**
	 * Id de la Transacción Financiera
	 */
	private String idTransaccionFinanciera;
	/**
	 * Nombre de la Transacción Financiera
	 */
	private String nombre;
	/**
	 * Descripción de la Transacción Financiera
	 */
	private String descripcion;
	/**
	 * Tipo de aplicación de la Transacción Financiera: Monto Fijo o Porcentaje
	 */
	private String tipoAplicacion;
	/**
	 * Tipo de Transacción Financiera
	 */
	private String tipoTranscaccionFinanc;
	/**
	 * Función asociada a la Transacción Financiera
	 */
	private String funcion;
	/**
	 * Indicador de Transacción Financiera de Comisión
	 */
	private int indicadorComision;
	/**
	 * Estatus de la Transacción Financiera
	 */
	private int statusTransaccionFinanc;
	/**
	 * Fecha de Aplicación de la Transacción Financiera
	 */
	private Date fechaAplicacion;
	/**
	 * Valor aplicado a la Transacción Financiera
	 */
	private BigDecimal valor;
	/**
	 * Lista de reglas asociadas a una transacción financiera.
	 */
	private ArrayList reglas = new ArrayList();
	
	
	/**
	 * Retorna el valor del Id de la Transacción Financiera 
	 * @return idTransaccionFinanciera
	 */
	public String getIdTransaccionFinanciera() {
		return idTransaccionFinanciera;
	}
	/**
	 * Asigna valor al Id de la Transacción Financiera
	 * @param idTransaccionFinanciera
	 */	
	public void setIdTransaccionFinanciera(String idTransaccionFinanciera) {
		this.idTransaccionFinanciera = idTransaccionFinanciera;
	}
	
	/**
	 * Retorna el valor del Nombre de la Transacción Financiera 
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Asigna valor al Nombre de la Transacción Financiera
	 * @param nombre
	 */	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Retorna el valor de la Descripcion de la Transacción Financiera 
	 * @return descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * Asigna valor a la Descripcion de la Transacción Financiera
	 * @param descripcion
	 */	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * Retorna el valor del Tipo de Transacción Financiera 
	 * @return tipoTranscaccionFinanc
	 */
	public String getTipoTranscaccionFinanc() {
		return tipoTranscaccionFinanc;
	}
	/**
	 * Asigna valor al Tipo de Transacción Financiera
	 * @param tipoTranscaccionFinanc
	 */
	public void setTipoTranscaccionFinanc(String tipoTranscaccionFinanc) {
		this.tipoTranscaccionFinanc = tipoTranscaccionFinanc;
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
	public int getIndicadorComision() {
		return indicadorComision;
	}
	/**
	 * Asigna valor al Indicador de Transacción Financiera de Comisión
	 * @param indicadorComision
	 */
	public void setIndicadorComision(int indicadorComision) {
		this.indicadorComision = indicadorComision;
	}	

	/**
	 * Retorna el valor del Estatus de la Transacción Financiera
	 * @return statusTransaccionFinanc
	 */
	public int getStatusTransaccionFinanc() {
		return statusTransaccionFinanc;
	}
	/**
	 * Asigna valor al Estatus de la Transacción Financiera
	 * @param statusTransaccionFinanc
	 */
	public void setStatusTransaccionFinanc(int statusTransaccionFinanc) {
		this.statusTransaccionFinanc = statusTransaccionFinanc;
	}
	
	/**
	 * Retorna el valor de la Fecha de Aplicación de la Transacción Financiera
	 * @return fechaAplicacion
	 */
	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}
	/**
	 * Asigna valor a la Fecha de Aplicación de la Transacción Financiera
	 * @param fechaAplicacion
	 */
	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
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
	 * Verifica si la lista de reglas está vacía 
	 **/
	public boolean isReglasVacio() {
		return reglas.isEmpty();
	}

	/** 
	 * Agrega una regla de transacción Financiera 
	 **/
	public boolean agregarRegla(ReglaTransaccionFinanciera reglaTransaccionFinanciera) {
		return this.reglas.add(reglaTransaccionFinanciera);
	}

	/**
	 * Retorna los valores de las reglas de transacción Financiera
	 * @return fechaAplicacion
	 */
	public ArrayList getReglas() {
		return reglas;
	}
	/**
	 * Asigna valores a las reglas de transacción Financiera
	 * @param fechaAplicacion
	 */
	public void setReglas(ArrayList reglas) {
		this.reglas = reglas;
	}
}
