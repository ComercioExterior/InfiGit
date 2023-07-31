package com.bancovenezuela.infi_services.interfaces;

import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosTomaDeOrden;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TomaOrdenResultado;
import com.bdv.infi_services.business.ordenes.ManejadorDeTomaDeOrden;
import com.bdv.infi_services.utilities.MessageTransformer;

public class SimularTomaDeOrden {
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;
	
	/**
	 * Metodo que permite simular una toma de orden<br>
	 * @param entrada : bean ParametrosTomaOrden
	 * @return bean TomaDeOrden con la informacion con los resultados de la simulacion
	 * @throws Throwable
	 */
	public String simularTomaDeOrden (String entrada) throws Throwable {
		
		try {
			// Parsing de Entrada
			ParametrosTomaDeOrden parametros = (ParametrosTomaDeOrden) MessageTransformer.unmarshall(ParametrosTomaDeOrden.class, entrada);

			// Invocación a la clase de Negocio
			ManejadorDeTomaDeOrden  manejador = new ManejadorDeTomaDeOrden();
			manejador.armarParametros(parametros);
			TomaOrdenResultado tomaDeOrden = manejador.simuladorTO();
			
			// Parsing de salida
			response = MessageTransformer.marshall(TomaOrdenResultado.class, tomaDeOrden);

		} catch (Throwable e) {
			throw e;
		}

		return response;
	}
}