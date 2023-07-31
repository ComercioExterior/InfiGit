package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.Credenciales;

/**
 * Lista de Titulos en Custodia recuperadas de la Base de Datos 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class OperacionesCheque implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Mensaje de excepcion
	 */
	private String mensajeExcepcion = "";
	/**
	 * Credenciales del usuario
	 */
	private Credenciales credenciales;
	/**
	 * Bean con la informacion de paginacion
	 */
	private ParametrosPaginacion beanPaginacion;
	/**
	 * Lista de bean de OperacionCheque
	 */
	private ArrayList<OperacionCheque> listaOperaciones;
	
	
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
	 * @return the listaOperaciones
	 */
	public ArrayList<OperacionCheque> getListaOperaciones() {
		return listaOperaciones;
	}
	/**
	 * @param listaOperaciones the listaOperaciones to set
	 */
	public void setListaOperaciones(ArrayList<OperacionCheque> listaOperaciones) {
		this.listaOperaciones = listaOperaciones;
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
