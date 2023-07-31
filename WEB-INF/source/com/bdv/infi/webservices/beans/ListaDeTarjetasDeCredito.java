package com.bdv.infi.webservices.beans;

import java.util.ArrayList;

/**
 * <p>
 * Representa una lista de tarjetas de crédito para un cliente. La razón por la
 * que existe esta clase es para poder realizar el xml binding con jibx. Este
 * objeto, así como la lista de cuentas asociadas, son creados a partir de un
 * xml que es retornado desde un web service.
 * <p>
 * Esta clase es utilizada únicamente por los manejadores
 *  y éstos retornan la lista de tarjetas de
 * crédito a la capa de presentación.
 * 
 * @author camilo torres
 * 
 */
public class ListaDeTarjetasDeCredito {
	/**
	 * Lista de tarjetas de crédito para un cliente. Este es el que se retorna
	 * hacia la capa de presentación
	 */
	private ArrayList<TarjetaDeCredito> tarjetas;

	public ArrayList<TarjetaDeCredito> getTarjetas() {
		return tarjetas;
	}

	public void setTarjetas(ArrayList<TarjetaDeCredito> tarjetas) {
		this.tarjetas = tarjetas;
	}
}
