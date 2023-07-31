package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;
import java.util.ArrayList;

public class OperacionesFinancierasOrden implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ArrayList que contiene las operaciones financieras de una orden
	 */
	private ArrayList<OperacionFinancieraOrden> operacionesFinancierasOrden = new ArrayList<OperacionFinancieraOrden>();

	/**
	 * @return the operacionesFinancierasOrden
	 */
	public ArrayList<OperacionFinancieraOrden> getOperacionesFinancierasOrden() {
		return operacionesFinancierasOrden;
	}

	/**
	 * @param operacionesFinancierasOrden the operacionesFinancierasOrden to set
	 */
	public void setOperacionesFinancierasOrden(
			ArrayList<OperacionFinancieraOrden> operacionesFinancierasOrden) {
		this.operacionesFinancierasOrden = operacionesFinancierasOrden;
	}
}
