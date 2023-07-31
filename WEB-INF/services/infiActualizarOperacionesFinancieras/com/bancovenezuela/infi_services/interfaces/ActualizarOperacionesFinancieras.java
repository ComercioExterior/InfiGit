package com.bancovenezuela.infi_services.interfaces;



import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosActualizarOperacionesFinancieras;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaOperacionPagar;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionesFinancieras;
import com.bdv.infi_services.business.ActualizarOperacionesFinancieras.ManejadorActualizarOperacionesFinancieras;
import com.bdv.infi_services.utilities.MessageTransformer;
/**
 * Clase que recibe los parametros de entrada para llamar al manejador correspondiente.
 * Transforma el XML en beans y luego de ser procesada la petición realiza el proceso inverso,
 * para retornar la respuesta vía web services
 * @author elaucho
 *
 */
public class ActualizarOperacionesFinancieras {
	
	/**
	 * Mensaje de respuesta
	 */
	private String response;

		public String actualizarOperacionFinanciera (String entrada) throws Throwable {
			//Transformar el XML de la Peticion al bean ConsultaOrden
			try {
				// Parsing de Entrada
				ParametrosActualizarOperacionesFinancieras  consulta =(ParametrosActualizarOperacionesFinancieras)
					MessageTransformer.unmarshall(ParametrosConsultaOperacionPagar.class, entrada);

				// Invocación a la clase de Negocio 
				ManejadorActualizarOperacionesFinancieras manejador = new ManejadorActualizarOperacionesFinancieras();
				OperacionesFinancieras operaciones = manejador.getOperacionesActualizar(consulta);
				
				// Parsing de salida
				response = MessageTransformer.marshall(OperacionesFinancieras.class, operaciones);
			} catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}

			return response;
			}//fin metodo

}//fin clase
