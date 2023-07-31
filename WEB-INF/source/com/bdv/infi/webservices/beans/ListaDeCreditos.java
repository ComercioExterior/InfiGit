package com.bdv.infi.webservices.beans;

import java.util.ArrayList;

/**
 * <p>
 * Representa una lista de créditos para un cliente. La razón por la que existe
 * esta clase es para poder realizar el xml binding con jibx. Este objeto, así
 * como la lista de créditos asociadas, son creados a partir de un xml que es
 * retornado desde un web service.
 * <p>
 * Esta clase es utilizada únicamente por los manejadores
 * y éstos retornan la lista de créditos a la capa
 * de presentación.
 * 
 * @author camilo torres
 * 
 */
public class ListaDeCreditos {
	/**
	 * Lista de créditos para un cliente. Este es el que se retorna hacia la
	 * capa de presentación
	 */
	private ArrayList<Credito> creditos;

	public ArrayList<Credito> getCreditos() {
		return creditos;
	}

	public void setCreditos(ArrayList<Credito> creditos) {
		this.creditos = creditos;
	}
}
