package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;
import com.bdv.infi_services.beans.entidades.MensajesServicio;


public class TomaOrdenResultado implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Bean con los mensajes de respuesta del servicio a nivel de negocio
	 */
	private MensajesServicio mensajesServicio = new MensajesServicio();
	/**
	 * Bean con la Orden Resultado 
	 */
	private TomaDeOrden tomaDeOrden = new TomaDeOrden();
	/**
	 * Bean con la Lista de bean de TomaOrdenTitulo asociados a la OrdenResultado
	 */
	private TOrdenTitulos tOrdenTitulos;
	/**
	 * Bean con la Lista de bean de TomaOrdenOperacion asociados a la OrdenResultado
	 */
	private TOrdenOperaciones tOrdenOperaciones;
	
	/**
	 * Devuelve el Bean con los mensajes emitidos por el servicio a nivel de negocio
	 * @return mensajesServicio
	 */
	public MensajesServicio getMensajesServicio() {
		return mensajesServicio;
	}
	/**
	 * Almacena el Bean con los mensajes emitidos por el servicio a nivel de negocio
	 * @param mensajesServicio
	 */
	public void setMensajesServicio(MensajesServicio mensajesServicio) {
		this.mensajesServicio = mensajesServicio;
	}
	
	/**
	 * Devuelve el Bean con la Orden Resultado 
	 * @return mensajesServicio
	 */
	public TomaDeOrden getTomaDeOrden() {
		return tomaDeOrden;
	}
	/**
	 * Almacena el Bean con la Orden Resultado 
	 * @param mensajesServicio
	 */
	public void setTomaDeOrden(TomaDeOrden tomaDeOrden) {
		this.tomaDeOrden = tomaDeOrden;
	}	
	
	/**
	 * Devuelve el Bean con la Lista de bean de TomaOrdenTitulo
	 * @return tOrdenTitulos
	 */
	public TOrdenTitulos getTOrdenTitulos() {
		return tOrdenTitulos;
	}
	/**
	 * Almacena el Bean con la Lista de bean de TomaOrdenTitulo
	 * @param tOrdenTitulos
	 */
	public void setTOrdenTitulos(TOrdenTitulos tOrdenTitulos) {
		this.tOrdenTitulos = tOrdenTitulos;
	}
	
	/**
	 * Devuelve los beans con la informacion recuperada a la Lista de bean de Operaciones
	 * @return tOrdenOperaciones
	 */
	public TOrdenOperaciones getTOrdenOperaciones() {
		return tOrdenOperaciones;
	}
	/**
	 * Asigna los beans con la informacion recuperada a la Lista de bean de Operaciones
	 * @param TOrdenOperaciones
	 */
	public void setTOrdenOperaciones(TOrdenOperaciones tOrdenOperaciones) {
		this.tOrdenOperaciones = tOrdenOperaciones;
	}
}
