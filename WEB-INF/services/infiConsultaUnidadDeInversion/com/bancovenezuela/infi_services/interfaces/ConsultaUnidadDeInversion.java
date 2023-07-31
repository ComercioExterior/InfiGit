package com.bancovenezuela.infi_services.interfaces;

import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaUI;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.UnidadesInversion;
import com.bdv.infi_services.business.unidad_inversion.ManejadorUnidadInversion;
import com.bdv.infi_services.utilities.MessageTransformer;


public class ConsultaUnidadDeInversion {
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;
	
	/**
	 * Metodo que permite recupera las Unidades de Inversion registradas <br>
	 * Puede recuperarlas todas o utilizar los criterios de busqueda: <br>
	 * 	1.-	Status de la Unidad de Inversion <br>
	 * 	2.-	Monedas asociadas a la Unidad de Inversion
	 * @param entrada : bean ConsultaUnidadDeInversion
	 * @return bean UnidadesInversion que contiene lista de beans UnidadInversion con la informacion recuperada
	 * @throws Throwable
	 */
	public String getUnidadDeInversion (String entrada) throws Throwable {
		
		// Transformar el XML de la Peticion al bean ConsultaOrden
		try {
			// Parsing de Entrada
			ParametrosConsultaUI consulta = (ParametrosConsultaUI) MessageTransformer.unmarshall(
					ParametrosConsultaUI.class, entrada);

			// Invocación a la clase de Negocio
			ManejadorUnidadInversion manejador = new ManejadorUnidadInversion();
			UnidadesInversion listaUnidadesInversion = manejador.getUnidadInversion(consulta);
			
			// Parsing de salida
			response = MessageTransformer.marshall(UnidadesInversion.class, listaUnidadesInversion);

		} catch (Throwable e) {
			throw e;
		}

		return response;
	}

}
