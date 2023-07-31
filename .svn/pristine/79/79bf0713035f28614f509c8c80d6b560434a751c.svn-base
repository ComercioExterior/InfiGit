package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;


/**
 * Lista de OrdenTitulo de una Orden recuperados de la Base de Datos 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class OrdenRespuestaTitulos implements Serializable{
	
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
	 * Identificador de la OrdenRespuesta
	 */
	private String idOrden = "0";
	
	/**
	 * Lista de bean de Titulos de una Orden
	 */
	private ArrayList<TOrdenTitulo> tOrdenTitulo;
	
	
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
	 * Devuelve el valor del Identificador de la OrdenRespuesta
	 * @return idOrden
	 */
	public String getIdOrden() {
		return idOrden;
	}
	/**
	 * Asigna valor a el Identificador de la OrdenRespuesta
	 * @param idOrden
	 */
	public void setIdOrden(String idOrden) {
		this.idOrden = idOrden;
	}

	/**
	 * Devuelve los beans con la informacion recuperada a la Lista de bean de UnidadInversionTitulo
	 * @return tOrdenTitulo
	 */
	public ArrayList<TOrdenTitulo> getTOrdenTitulo() {
		return tOrdenTitulo;
	}
	/**
	 * Asigna los beans con la informacion recuperada a la Lista de bean de UnidadInversionTitulo
	 * @param tOrdenTitulo
	 */
	public void setTOrdenTitulo(ArrayList<TOrdenTitulo> tOrdenTitulo) {
		this.tOrdenTitulo = tOrdenTitulo;
	}
	
}
