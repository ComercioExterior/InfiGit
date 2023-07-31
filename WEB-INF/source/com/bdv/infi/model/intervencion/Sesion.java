package com.bdv.infi.model.intervencion;

import java.net.URL;
import java.net.HttpURLConnection;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;


import megasoft.Logger;

public class Sesion extends Configuracion {

	public Sesion() {
		super();
	}

	public String ObtenerToken(String pathWS) {
		String respuesta = "";
		HttpURLConnection conn = null;
		try {
			System.out.println(" url sesion : "+ propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_SESION));
			URL url = new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_SESION));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("username", userName);
			conn.setRequestProperty("password", clave);
			System.out.println("Clave : " + clave);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				Logger.error(this, "Failed : HTTP error code : " + conn.getResponseCode());
				System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
			}
			respuesta = conn.getHeaderField("Authorization");
			System.out.println("Respuesta : " + respuesta);
		} catch (Exception e) {
			System.out.println("Configuracion : ObtenerToken() " + e);
			Logger.error(this, "Configuracion : ObtenerToken() " + e);
		} finally {
			conn.disconnect();
		}
		return respuesta;
	}
}
