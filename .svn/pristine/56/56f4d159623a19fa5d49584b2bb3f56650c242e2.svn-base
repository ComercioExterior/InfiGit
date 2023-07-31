package com.bdv.infi.model.intervencion;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import megasoft.Logger;
import org.bcv.serviceINTERVENCION.ConsultaArchivoBcv;
import org.bcv.serviceINTERVENCION.listOperaciones;
import org.bcv.serviceINTERVENCION.operaciones;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.google.gson.Gson;

public class ListarOperaciones extends Configuracion {

	public ListarOperaciones() throws Exception {
		super();

	}
	
	@SuppressWarnings("unchecked")
	public listOperaciones ListaArchivo(String pathWS, String token,String archivo) {
		
		listOperaciones lcs = new listOperaciones();
		System.out.println(" url reportar : "+ propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_LISTAR_OPERACIONES));
		String urlToConnect = propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_LISTAR_OPERACIONES) +archivo;
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
			System.out.println("response.toString() Operaciones aaaa: " + response.toString());
			lcs = gson.fromJson(response.toString(), listOperaciones.class);
		} catch (Exception e) {
			System.out.println("ListarCodigodArchivo aaa: ListaArchivo() " + e);
			Logger.error(this, "ListarCodigodArchivo aaa: ListaArchivo() " + e);
		}

		return lcs;

	}

}
