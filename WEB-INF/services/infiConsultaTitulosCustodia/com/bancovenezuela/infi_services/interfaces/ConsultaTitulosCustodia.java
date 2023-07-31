package com.bancovenezuela.infi_services.interfaces;


import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaTitulosCustodia;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TitulosCustodia;
import com.bdv.infi_services.business.custodia.ManejadorCustodia;
import com.bdv.infi_services.utilities.MessageTransformer;


public class ConsultaTitulosCustodia {
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;
	
	/**
	 * Metodo que permite conocer los títulos que se encuentran en custodia para un determinado cliente.  <br>
	 * El criterio de busqueda: <br>
	 * 	1.-	Cedula de Identidad del Cliente 
	 * @param entrada : bean ParametrosConsultaTitulosCustodia con los criterios
	 * @return  bean TitulosCustodia, lista de beans TituloCustodia con la informacion recuperada
	 * @throws Throwable
	 */
	public String getTitulosCustodia (String entrada) throws Throwable {

		// Transformar el XML de la Peticion al bean ConsultaOrden
		try {
			// Parsing de Entrada
			ParametrosConsultaTitulosCustodia consulta = (ParametrosConsultaTitulosCustodia) MessageTransformer.unmarshall(
					ParametrosConsultaTitulosCustodia.class, entrada);

			// Invocación a la clase de Negocio
			ManejadorCustodia manejador = new ManejadorCustodia();
			TitulosCustodia listaTitulosCustodia = manejador.getTitulosCustodia(consulta);
			
			// Parsing de salida
			response = MessageTransformer.marshall(TitulosCustodia.class, listaTitulosCustodia);

		} catch (Throwable e) {
			throw e;
		}

		return response;
	}
	
}

