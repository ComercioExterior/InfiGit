package com.bdv.infi.webservices.manager;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import models.security.login.QG30;
import models.security.login.QG30Respuesta;
import com.bdv.infi.webservices.client.ClienteWs;

/**
 * Sirve para manejar todo lo que tiene que ver con los datos de los clientes o
 * que se obtienen a partir de una cedula de identidad (o rif) de un cliente.
 * 
 * Contiene un metodo para traer un cliente desde los web services.
 * 
 * Utiliza jibx para realizar el xml binding desde el web service.
 * 
 * @author camilo torres
 * 
 */
public class ManejadorLoginCP {
	private ServletContext contexto = null;

	private Logger logger = Logger.getLogger(ManejadorLoginCP.class);

	public ManejadorLoginCP(ServletContext cont) {
		this.contexto = cont;
	}

	/**
	 * Retorna la tasa de cambio vigente al momento
	 * 
	 * @param monedaSiglas
	 *            las siglas de la moneda para buscar su tasa de cambio
	 * @param username
	 *            nombre del usuario con permisología para accesar el servicio
	 *            web
	 * @param ip
	 *            ip de donde se está invocando el servicio
	 * @return TasaCambio Tasa de cambio encontrada
	 * @throws Exception
	 */
	public QG30Respuesta getCambioPassword(QG30 qg30, String ip)
			throws Exception {

		ClienteWs clienteWs = new ClienteWs();	
		QG30Respuesta qg30Respuesta = new QG30Respuesta();
		
		try {
			clienteWs = ClienteWs.crear("getQG30", contexto);

			qg30Respuesta = (QG30Respuesta) clienteWs.enviarYRecibir(qg30,
					QG30.class, QG30Respuesta.class, qg30.getNombreDeUsuario(), ip);
			

		} catch (Exception e) {
			
			logger.info("Error cambiando el password del usuario " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Error cambiando el password del usuario " + e.getMessage());
		}	
		
		return qg30Respuesta;
	}
}
