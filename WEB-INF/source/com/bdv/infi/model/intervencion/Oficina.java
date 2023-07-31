package com.bdv.infi.model.intervencion;

import megasoft.Logger;
import com.bdv.infi.data.OficinaRespuestaDTO;
import com.google.gson.Gson;  
import java.io.*;   
import java.net.HttpURLConnection;
import java.net.URL;

public class Oficina {

	public OficinaRespuestaDTO BuscarOficinas(){
		Gson gson = new Gson();
		String respuesta = "";
		try {
			URL url = new URL("http://180.183.170.70:30081/bdvx-geolocalizacion/api/offices/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200) {
				System.err.println("Fallo la conexion al servicio obtenerOficinas");
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());				
				
			}
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));

				String output;
				while ((output = br.readLine()) != null) {
					respuesta = output;
					
				}

				conn.disconnect();
		} catch (Exception e) {
			Logger.error(this, "Error al obtener respuesta del webServices RestFll" + e.getMessage());
			System.err.println("Error al obtener respuesta del webServices RestFll" + e.getMessage());
		}
		return gson.fromJson(respuesta, OficinaRespuestaDTO.class);
		
	}
}
