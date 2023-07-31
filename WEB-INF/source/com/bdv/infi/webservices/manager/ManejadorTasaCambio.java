package com.bdv.infi.webservices.manager;

import java.util.Calendar;

import javax.servlet.ServletContext;

import megasoft.Logger;

import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.TCCC;
import com.bdv.infi.webservices.beans.TCCCFMT;
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
public class ManejadorTasaCambio {
	private ServletContext contexto = null;

	/**
	 * Datos de usuario/clave del usuario
	 */
	private CredencialesDeUsuario credenciales = null;

	public ManejadorTasaCambio(ServletContext cont,
			CredencialesDeUsuario credenciales) {
		this.contexto = cont;
		this.credenciales = credenciales;
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
	public TCCCFMT getTasaCambio(String monedaSiglas, String username, String ip)
			throws Exception {
		
		Calendar calendar = Calendar.getInstance();
		String dia = String.valueOf(calendar.get(calendar.DATE));
		String mes = String.valueOf(calendar.get(calendar.MONTH)+1);
		String anio = String.valueOf(calendar.get(calendar.YEAR));
		
		dia = (Utilitario.rellenarCaracteres(dia, '0', 2, false));
		mes = (Utilitario.rellenarCaracteres(mes, '0', 2, false));
		anio = (Utilitario.rellenarCaracteres(anio, '0', 4, false));
			
		TCCC entrada = new TCCC();
		entrada.setCodDivisa(monedaSiglas);
		entrada.setInddivbi("D");
		entrada.setFechaCambio(anio+"-"+mes+"-"+dia);
		entrada.setCompra("");
		entrada.setVenta("");
		entrada.setFixing("");

		ClienteWs clienteWs = null;
		TCCCFMT tasaCambio = null;

		try {
			clienteWs = ClienteWs.crear("getTCCC", contexto);
			tasaCambio = (TCCCFMT) clienteWs.enviarYRecibir(
					entrada, TCCC.class, TCCCFMT.class, username, ip);
		} catch (Exception e) {
			Logger.error(this, "No se pudo obtener la respuesta correctamente: TCCCFMT",e);
		}
		return tasaCambio;		
	}
}
