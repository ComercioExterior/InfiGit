package com.bdv.infi.webservices.beans;

import java.util.ArrayList;

/**
 * <p>
 * Representa una lista de cuentas para un cliente. La razon por la que existe
 * esta clase es para poder realizar el xml binding con jibx. Este objeto, asi
 * como la lista de cuentas asociadas, son creados a partir de un xml que es
 * retornado desde un web service.
 * <p>
 * Esta clase es utilizada unicamente por los manejadores
 * (com.megasoft.bo.manejadores) y estos retornan la lista de cuentas a la capa
 * de presentacion.
 * <p>
 * Para ver el xml que se utiliza para generar estos objetos:
 * WEB-INF/templates/ejemplos_xml_para_ws/lista_de_cuentas.xml
 * 
 * @author camilo torres
 * 
 */
public class ListaDeCuentas {
	/**
	 * Lista de cuentas para un cliente. Este es el que se retorna hacia la capa
	 * de presentacion
	 */
	private ArrayList<Cuenta> cuentas;

	public ArrayList<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(ArrayList<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	
}
