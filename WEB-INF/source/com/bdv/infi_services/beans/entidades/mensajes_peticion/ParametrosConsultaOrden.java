package com.bdv.infi_services.beans.entidades.mensajes_peticion;

import java.io.Serializable;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;


/**
 * Mensaje de peticion para la consulta de Ordenes
 * @author MegaSoft Computacion
 */
public class ParametrosConsultaOrden implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Credenciales credenciales;
	/**
	 * Bean con la informacion de paginacion
	 */
	private ParametrosPaginacion parametrosPaginacion;
	/**
	 * Identificador de la OrdenRespuesta
	 */
	private String idOrden = "0";
	/**
	 * Cedula de Identidad del Cliente
	 */
	private String cedulaIndentidad = "";
	/**
	 * Fecha desde del rango de consulta
	 * Fecha en que se tomo la orden 
	 */
	private String fechaDesde = "";
	/**
	 * Fecha Hasta del rango de consulta
	 * Fecha en que se tomo la orden
	 */
	private String fechaHasta  = "";
	
	/**
	 * Constructor del bean
	 */
	public ParametrosConsultaOrden () throws Exception {
	}
	
	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public ParametrosConsultaOrden (Object [] objValor) throws Exception {
		this.idOrden = (String)objValor[0];
		this.cedulaIndentidad = (String)objValor[1];
		this.fechaDesde  = (String)objValor[2];
		this.fechaHasta = (String)objValor[3];
	}
	
	/**
	 * Devuelve el Bean con la informacion de paginacion
	 * @return indicaPagineo
	 */
	public ParametrosPaginacion getParametrosPaginacion() {
		return parametrosPaginacion;
	}
	/**
	 * Almacena el Bean con la informacion de paginacion
	 * @param indicaPagineo
	 */
	public void setParametrosPaginacion(ParametrosPaginacion beanPaginacion) {
		this.parametrosPaginacion = beanPaginacion;
	}
	
	/**
	 * Devuelve el valor del Identificador de la Orden 
	 * @return idOrden
	 */
	public String getIdOrden() {
		return idOrden;
	}
	/**
	 * Asigna valor a el Identificador de la Orden 
	 * @param idOrden
	 */
	public void setIdOrden(String idOrden) {
		this.idOrden = idOrden;
	}
	
	/**
	 * Devuelve el valor de la Cedula de Identidad
	 * @return cedulaIndentidad
	 */
	public String getCedulaIndentidad() {
		return cedulaIndentidad;
	}
	/**
	 * Asigna valor a la Cedula de Identidad
	 * @param cedulaIndentidad
	 */
	public void setCedulaIndentidad(String cedulaIndentidad) {
		this.cedulaIndentidad = cedulaIndentidad;
	}
	
	/**
	 * Devuelve el valor de la Fecha desde del rango de consulta
	 * @return fechaDesde
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}
	/**
	 * Asigna valor a la Fecha desde del rango de consulta
	 * @param fechaDesde
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	
	/**
	 * Devuelve el valor de la Fecha hasta del rango de consulta
	 * @return fechaHasta
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}
	/**
	 * Asigna valor a la Fecha hasta del rango de consulta
	 * @param fechaHasta
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	/**
	 * @return the credenciales
	 */
	public Credenciales getCredenciales() {
		return credenciales;
	}

	/**
	 * @param credenciales the credenciales to set
	 */
	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}
}
