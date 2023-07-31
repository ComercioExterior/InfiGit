package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;


/**
 * Lista de Unidades de Inversion recuperadas de la Base de Datos 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class UnidadesInversion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Mensaje de excepcion
	 */
	private String mensajeExcepcion = "";
	/**
	 * Bean con la informacion de paginacion
	 */
	private ParametrosPaginacion parametrosPaginacion;
	/**
	 * Lista de bean de OrdenesRespuesta
	 */
	private ArrayList<UnidadInversion> unidadInversion;
	
	/**
	 * Devuelve el valor del Mensaje de Excepcion
	 * @return mensajeExcepcion
	 */
	public String getMensajeExcepcion() {
		return mensajeExcepcion;
	}
	/**
	 * Asigna valor al Mensaje de Excepcion
	 * @param mensajeExcepcion
	 */
	public void setMensajeExcepcion(String mensajeExcepcion) {
		this.mensajeExcepcion = mensajeExcepcion;
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
	 * Devuelve los beans con la informacion recuperada a la Lista de bean de OrdenesRespuesta
	 * @return unidadInversion
	 */
	public ArrayList<UnidadInversion> getUnidadInversion() {
		return unidadInversion;
	}
	/**
	 * Asigna los beans con la informacion recuperada a la Lista de bean de OrdenesRespuesta
	 * @param unidadInversion
	 */
	public void setUnidadInversion(ArrayList<UnidadInversion> listaOrdenes) {
		this.unidadInversion = listaOrdenes;
	}

}

