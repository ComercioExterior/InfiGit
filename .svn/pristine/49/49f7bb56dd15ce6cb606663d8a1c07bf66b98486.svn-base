package com.bancovenezuela.infi_services.interfaces;


import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaOrdenTitulos;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OrdenRespuestaTitulos;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OrdenesRespuesta;
import com.bdv.infi_services.business.ordenes.ManejadorOrdenes;
import com.bdv.infi_services.utilities.MessageTransformer;

public class ConsultaOrdenTitulos {
	
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;
	
	/**
	 * Metodo que permite recupera los Titulos de una Orden  <br>
	 * Puede recuperarlas todas o utilizar los criterios de busqueda: <br>
	 * 	1.-	Numero de la Orden <br>
	 * @param entrada : bean ConsultaOrden con los criterios
	 * @return bean OrdenesRespuesta que contiene lista de beans OrdenRespuesta con la informacion recuperada
	 * @throws Throwable
	 */
	public String getOrdenTitulos (String entrada) throws Throwable {

		// Transformar el XML de la Peticion al bean ConsultaOrden
		try {
			// Parsing de Entrada
			ParametrosConsultaOrdenTitulos consulta = (ParametrosConsultaOrdenTitulos) MessageTransformer.unmarshall(ParametrosConsultaOrdenTitulos.class, entrada);

			// Invocación a la clase de Negocio
			ManejadorOrdenes manejador = new ManejadorOrdenes();
			OrdenRespuestaTitulos listaOrdenes = manejador.getOrdenTitulos(consulta);
			
			// Parsing de salida
			response = MessageTransformer.marshall(OrdenesRespuesta.class, listaOrdenes);

		} catch (Throwable e) {
			throw e;
		}

		return response;
	}

}


