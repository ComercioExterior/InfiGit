package com.bdv.infi.model.intervencion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import megasoft.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.google.gson.Gson;

public class ReportarOperaciones extends Configuracion {
	String arcVentas;
	File data;

	public ReportarOperaciones() throws Exception {
		super();

	}

	public void Archivo(Object arreglo) {

		try {
			Gson gson = new Gson();
			System.out.println("arreglo metodo : "+ arreglo.toString());
			arcVentas = gson.toJson(arreglo);
			System.out.println("arcVentas :" + arcVentas);

			// Se crea el archivo con los datos del txt
			data = File.createTempFile("arcVentas", ".txt");
			FileWriter writer = new FileWriter(data);
			writer.write(arcVentas);
			writer.close();
			// Solo para verificar la data en el archivo
			BufferedReader readerBuff = new BufferedReader(new FileReader(data));
			System.out.println("writer : " + readerBuff.readLine());
			readerBuff.close();
		} catch (Exception e) {
			System.out.println("ReportarOperaciones : Archivo() " + e);
			Logger.error(this, "ReportarOperaciones : Archivo() " + e);

		}
	}

	public String PostMultiPart(String token) throws MalformedURLException, IOException {
		System.out.println(" url reportar : "+ propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_VENTA));
		String urlToConnect = propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_VENTA);
		String boundary = Long.toHexString(System.currentTimeMillis());
		URLConnection connection = new URL(urlToConnect).openConnection();
		connection.setDoOutput(true); // This sets request method to POST.
		connection.setRequestProperty("Authorization", token);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//		System.out.println("connection.getOutputStream() : " + connection.getOutputStream());
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
			writer.println("--" + boundary);
			writer.println("Content-Disposition: form-data; name=\"arcVentas\"; filename=\"" + data.getName() + "\"");
			writer.println("Content-Type: text/plain");
			writer.println();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(data)));
				for (String line; (line = reader.readLine()) != null;) {
					writer.println(line);
					System.out.println("line : " + line);
				}
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException logOrIgnore) {
					}
			}
			writer.println("--" + boundary + "--");
		} finally {

			if (writer != null)
				writer.close();
		}
	
		// Connection is lazily executed whenever you request any status.
		InputStream is = connection.getInputStream();
		BufferedInputStream inputStream = new BufferedInputStream(is);
		StringBuilder response = new StringBuilder();
		int readedBytes;
		byte[] buffer = new byte[4096];

		while ((readedBytes = inputStream.read(buffer)) != -1) {
			response.append(new String(buffer, 0, readedBytes));
			System.out.println(response.toString());

		}

		return response.toString();
	}

}
