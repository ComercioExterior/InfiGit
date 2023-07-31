package com.bdv.infi_services.beans.entidades.mensajes_peticion;

import java.io.Serializable;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;


/**
 * Mensaje de peticion para la consulta de Titulos en Custodia
 * @author MegaSoft Computacion
 */
public class ParametrosConsultaTitulosCustodia implements Serializable{
	
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
	 * Cedula de Identidad del Cliente
	 */
	private String cedulaIdentidad = "";

	/**
	 * Constructor del bean
	 */
	public ParametrosConsultaTitulosCustodia () throws Exception {
		
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
	 * Devuelve el valor de la Cedula de Identidad
	 * @return cedulaIdentidad
	 */
	public String getCedulaIdentidad() {
		return cedulaIdentidad;
	}
	/**
	 * Asigna valor a la Cedula de Identidad
	 * @param cedulaIdentidad
	 */
	public void setCedulaIdentidad(String cedulaIndentidad) {
		this.cedulaIdentidad = cedulaIndentidad;
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
