package com.bancovenezuela.infi_services.interfaces;

import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaOperacionPagar;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionesCheque;
import com.bdv.infi_services.business.operaciones_financieras.ManejadorOperacionesFinancieras;
import com.bdv.infi_services.utilities.MessageTransformer;

public class ConsultaOperacionCheque {
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;

		public String getOperacionCheque (String entrada) throws Throwable {
			// Transformar el XML de la Peticion al bean ConsultaOrden
			try {
				// Parsing de Entrada
				ParametrosConsultaOperacionPagar consulta =(ParametrosConsultaOperacionPagar)
					MessageTransformer.unmarshall(ParametrosConsultaOperacionPagar.class, entrada);
				
				// Invocación a la clase de Negocio 
				ManejadorOperacionesFinancieras manejador = new ManejadorOperacionesFinancieras();
				OperacionesCheque operaciones = manejador.getOperacionesPendientes(consulta);

				// Parsing de salida
				response = MessageTransformer.marshall(OperacionesCheque.class, operaciones);
			} catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}

			return response;
			}
	
}