package com.bancovenezuela.infi_services.interfaces;

import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosTomaDeOrden;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TomaOrdenResultado;
import com.bdv.infi_services.business.ordenes.ManejadorDeTomaDeOrden;
import com.bdv.infi_services.utilities.MessageTransformer;
/**
 * Clase encargada de llamar al componente que realiza la simulacion e inserta la orden.
 * @author elaucho,mgalindo
 */

public class RegistrarTomaDeOrden {
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;
	
	/**
	 * Metodo que permite simular y registrar la compra de titulos <br>
	 * @param entrada : bean ParametrosTomaOrden
	 * @return bean TomaDeOrden con la informacion con los resultados de la simulacion
	 * @throws Throwable
	 */
	public String insertTomaDeOrden (String entrada) throws Throwable {

		try {
			
			
			// Parsing de Entrada
			ParametrosTomaDeOrden parametros = (ParametrosTomaDeOrden) MessageTransformer.unmarshall(ParametrosTomaDeOrden.class, entrada);

			// Invocación a la clase de Negocio
			ManejadorDeTomaDeOrden  manejador = new ManejadorDeTomaDeOrden();
			manejador.armarParametros(parametros);
			TomaOrdenResultado tomaDeOrden = manejador.insertTO();

			// Parsing de salida
			response = MessageTransformer.marshall(TomaOrdenResultado.class, tomaDeOrden);

		} catch (Throwable e) {
			throw e;
		}
		

		return response;
	}
}