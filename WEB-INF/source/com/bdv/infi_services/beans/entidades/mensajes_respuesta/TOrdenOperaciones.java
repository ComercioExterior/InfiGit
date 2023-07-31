package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Lista de Operaciones asociados a una Orden generada en el proceso 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class TOrdenOperaciones implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Lista de operaciones financieras aplicadas a una orden
	 */
	private ArrayList<TOrdenOperacion> tOrdenOperacion;
	
	/**
	 * Devuelve los beans con la informacion recuperada a la Lista de bean de Operaciones
	 * @return tOrdenOperacion
	 */
	public ArrayList getTOrdenOperacion() {
		return tOrdenOperacion;
	}
	/**
	 * Asigna los beans con la informacion recuperada a la Lista de bean de Operaciones
	 * @param tOrdenOperacion
	 */
	public void setTOrdenOperacion(ArrayList<TOrdenOperacion> tOrdenOperacion) {
		this.tOrdenOperacion = tOrdenOperacion;
	}

}
