package com.bdv.infi_services.beans.entidades.mensajes_peticion;

import java.io.Serializable;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;


/**
 * Mensaje de peticion para la consulta de Ordenes
 * @author MegaSoft Computacion
 */
public class ParametrosConsultaOperacionPagar implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Credenciales credenciales;
	/**
	 * Bean con la informacion de paginacion
	 */
	public ParametrosPaginacion beanPaginacion;
	/**
	 * Cedula de Identidad del Cliente
	 */
	public String cedulaIdentidad = "";
	
	/**
	 * Constructor del bean
	 */
	public ParametrosConsultaOperacionPagar () throws Exception {
	}
	
	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public ParametrosConsultaOperacionPagar (String valor) throws Exception {
		this.cedulaIdentidad = valor;
	}
	
	/**
	 * Devuelve el Bean con la informacion de paginacion
	 * @return indicaPagineo
	 */
	public ParametrosPaginacion getBeanPaginacion() {
		return beanPaginacion;
	}
	/**
	 * Almacena el Bean con la informacion de paginacion
	 * @param indicaPagineo
	 */
	public void setBeanPaginacion(ParametrosPaginacion beanPaginacion) {
		this.beanPaginacion = beanPaginacion;
	}

	/**
	 * Devuelve el valor de la Cedula de Identidad
	 * @return cedulaIdentidad
	 */
	public String getCedulaIndentidad() {
		return cedulaIdentidad;
	}
	/**
	 * Asigna valor a la Cedula de Identidad
	 * @param cedulaIdentidad
	 */
	public void setCedulaIndentidad(String cedulaIdentidad) {
		this.cedulaIdentidad = cedulaIdentidad;
	}

	/**
	 * @return the cedulaIdentidad
	 */
	public String getCedulaIdentidad() {
		return cedulaIdentidad;
	}

	/**
	 * @param cedulaIdentidad the cedulaIdentidad to set
	 */
	public void setCedulaIdentidad(String cedulaIdentidad) {
		this.cedulaIdentidad = cedulaIdentidad;
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
