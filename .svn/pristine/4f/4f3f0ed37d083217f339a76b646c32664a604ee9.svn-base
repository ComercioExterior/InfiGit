package com.bdv.infi.webservices.manager;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.jibx.runtime.JiBXException;

import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.PEM2010;
import com.bdv.infi.webservices.beans.PEVB;
import com.bdv.infi.webservices.beans.TCGG;
import com.bdv.infi.webservices.beans.TCGGRespuesta;
import com.bdv.infi.webservices.client.ClienteWs;

/**
 * Clase que implementa las llamadas a servicios de Tablas Corporativas
 * Utiliza jibx para realizar el xml binding desde el web service. *
 * @author NM25287
 */
public class ManejadorTablasCorp {
	private ServletContext contexto = null;

	/**
	 * Datos de usuario/clave del usuario
	 */
	private CredencialesDeUsuario credenciales = null;	
	
	
	public ManejadorTablasCorp(ServletContext cont,
			CredencialesDeUsuario credenciales) {
		this.contexto = cont;
		this.credenciales = credenciales;
	}
	
	public TCGGRespuesta getTCGG(TCGG entrada, String username, String ip) throws IOException, JiBXException, Exception{
		TCGGRespuesta respuesta = null;
		ClienteWs cws = ClienteWs.crear("getTCGG", contexto);	
	
		respuesta = (TCGGRespuesta) cws.enviarYRecibir(entrada, TCGG.class,TCGGRespuesta.class , username, ip);
		
		return respuesta;
	}
	
	
}


