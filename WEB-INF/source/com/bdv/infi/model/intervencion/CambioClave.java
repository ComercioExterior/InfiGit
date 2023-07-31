package com.bdv.infi.model.intervencion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import megasoft.Logger;
import org.bcv.serviceINTERVENCION.IntervencionActiva;

import com.bdv.infi.data.RespuestaDTO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.google.gson.Gson;

public class CambioClave extends Configuracion {

	public CambioClave() throws Exception {
		super();

	}

	public String Cambio(String clave1) throws IOException {
		String respuesta = "";
		HttpURLConnection conn = null;
		String line = "";
		try {
			System.out.println("claveee : " + clave);
			System.out.println(" url sesion : "+ propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_CAMBIO));
			URL url = new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_CAMBIO) + "?claveNueva="+clave1);
			conn = (HttpURLConnection) url.openConnection();
			System.out.println("cambio2");
			conn.setRequestProperty("username", userName);
			conn.setRequestProperty("password", clave);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				Logger.error(this, "Failed : HTTP error code : " + conn.getResponseCode());
				System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
			}
//			respuesta = conn.getHeaderField("successfulMessageValue");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				respuesta = line;
			}
			System.out.println("mensaje : " + conn.getResponseMessage());
			System.out.println("Respuesta : " + respuesta);
		} catch (Exception e) {
			System.out.println("CambioClave : Cambio() " + e);
			Logger.error(this, "CambioClave : Cambio() " + e);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

			while ((line = br.readLine()) != null) {
				respuesta = line;
			}
		} finally {
			conn.disconnect();
		}
		return respuesta;
	}

}
