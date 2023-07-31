package com.bdv.infi.model.intervencion;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import megasoft.Logger;
import org.bcv.serviceINTERVENCION.ConsultaArchivoBcv;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.google.gson.Gson;

public class ListarCodigodArchivo extends Configuracion {

	public ListarCodigodArchivo() throws Exception {
		super();

	}
	
	public ConsultaArchivoBcv[] ListaArchivo(String pathWS, String token,String fecha) {
		
		ConsultaArchivoBcv[] lcs = null;
		System.out.println(" url listar archivo : "+ propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_LISTAR_ARCHIVO));
		String urlToConnect = propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_LISTAR_ARCHIVO)+fecha;
		HttpURLConnection conn = null;
		String boundary = Long.toHexString(System.currentTimeMillis());
		
		try {
			URL url = new URL(urlToConnect);
			Gson gson = new Gson();
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", token);
		//	conn.setRequestProperty("feRegistro ", fecha);
			conn.setRequestProperty("Content-Type", "application/json; boundary=" + boundary);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				System.err.println("Failed : HTTP error code : " + conn.getResponseCode());
				Logger.error(this, "Failed : HTTP error code : " + conn.getResponseCode());

			}
			System.out.println("conn.getResponseCode() : " + conn.getResponseCode());
			InputStream is = conn.getInputStream();
			BufferedInputStream inputStream = new BufferedInputStream(is);
			StringBuilder response = new StringBuilder();
			int readedBytes;
			byte[] buffer = new byte[4096];
			
			while ((readedBytes = inputStream.read(buffer)) != -1) {
				response.append(new String(buffer, 0, readedBytes,"UTF-8"));
				
			}
			
			lcs = gson.fromJson(response.toString(), ConsultaArchivoBcv[].class);
		} catch (Exception e) {
			System.out.println("ListarCodigodArchivo : ListaArchivo() " + e);
			Logger.error(this, "ListarCodigodArchivo : ListaArchivo() " + e);
		}

		return lcs;

	}

}
