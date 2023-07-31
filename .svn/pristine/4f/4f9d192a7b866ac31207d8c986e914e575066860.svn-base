package com.bdv.infi.model.intervencion;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import megasoft.Logger;
import org.bcv.serviceINTERVENCION.IntervencionActiva;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.google.gson.Gson;

public class IntervencionActivas extends Configuracion {

	public IntervencionActivas() throws Exception {
		super();

	}

	public IntervencionActiva[] Jornadas(String pathWS, String token) {

		IntervencionActiva[] lcs = null;
		String urlToConnect = propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_ACTIVAS);
		HttpURLConnection conn = null;
		String boundary = Long.toHexString(System.currentTimeMillis());

		try {
			URL url = new URL(urlToConnect);
			Gson gson = new Gson();

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", token);
			conn.setRequestProperty("Content-Type", "application/json; boundary=" + boundary);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				System.err.println("Failed : HTTP error code : " + conn.getResponseCode());
				Logger.error(this, "Failed : HTTP error code : " + conn.getResponseCode());

			}
			InputStream is = conn.getInputStream();
			BufferedInputStream inputStream = new BufferedInputStream(is);
			StringBuilder response = new StringBuilder();
			int readedBytes;
			byte[] buffer = new byte[4096];

			while ((readedBytes = inputStream.read(buffer)) != -1) {
				response.append(new String(buffer, 0, readedBytes, "UTF-8"));

			}

			lcs = gson.fromJson(response.toString(), IntervencionActiva[].class);
		} catch (Exception e) {
			System.out.println("IntervencionActivas : Jornadas() " + e);
			Logger.error(this, "IntervencionActivas : Jornadas() " + e);
		}

		return lcs;

	}

}
