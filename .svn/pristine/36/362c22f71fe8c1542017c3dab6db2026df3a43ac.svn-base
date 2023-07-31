package com.bancovenezuela.infi_services.interfaces;


import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaOrden;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OrdenesRespuesta;
import com.bdv.infi_services.business.ordenes.ManejadorOrdenes;
import com.bdv.infi_services.utilities.MessageTransformer;

public class ConsultaOrdenes {
	
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;
	
	/**
	 * Metodo que permite recupera las OrdenesRespuesta registradas  <br>
	 * Puede recuperarlas todas o utilizar los criterios de busqueda: <br>
	 * 	1.-	Numero de la OrdenRespuesta <br>
	 * 	2.-	Cedula de Identidad del Cliente <br>
	 * 	3.-	Fecha Desde - Fecha Hasta 
	 * @param entrada : bean ConsultaOrden con los criterios
	 * @return bean OrdenesRespuesta que contiene lista de beans OrdenRespuesta con la informacion recuperada
	 * @throws Throwable
	 */
	public String getOrdenes (String entrada) throws Throwable {

		// Transformar el XML de la Peticion al bean ConsultaOrden
		try {
			// Parsing de Entrada
			ParametrosConsultaOrden consulta = (ParametrosConsultaOrden) MessageTransformer.unmarshall(ParametrosConsultaOrden.class, entrada);

			// Invocación a la clase de Negocio
			ManejadorOrdenes manejador = new ManejadorOrdenes();
			OrdenesRespuesta listaOrdenes = manejador.getOrdenes(consulta);
			
			response = MessageTransformer.marshall(OrdenesRespuesta.class, listaOrdenes);

		} catch (Throwable e) {
			throw e;
		}

		return response;
	}

}

