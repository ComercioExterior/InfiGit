package com.bdv.infi_services.beans.entidades;

import java.util.ArrayList;

/**
 * Lista de String que son mensajes de respuesta de un Servicio a nivel de negocio
 * @author MegaSoft Computacion
 */
public class MensajesServicio {
	
	/**
	 * Lista de Mensajes
	 */
	private ArrayList<ValorAtributo> valorAtributo = new ArrayList<ValorAtributo>();

	/**
	 * Devuelve la Lista de Mensajes
	 * @return getListaMensajes
	 */
	public ArrayList<ValorAtributo> getValorAtributo() {
		return valorAtributo;
	}
	/**
	 * Almacena cada mensaje en la lista
	 * @param getListaMensajes
	 */
	public void setValorAtributo(ArrayList<ValorAtributo> listaMensajes) {
		this.valorAtributo = listaMensajes;
	}

}
