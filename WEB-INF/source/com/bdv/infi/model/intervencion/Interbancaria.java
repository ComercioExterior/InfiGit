package com.bdv.infi.model.intervencion;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import org.bcv.serviceINTERVENCION.codigoIsoDivisa;
import org.bcv.serviceINTERVENCION.codigoVentaBCV;
import org.bcv.serviceINTERVENCION.operaciones_interbancaria;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.google.gson.Gson;

public class Interbancaria extends Configuracion {

	public Interbancaria() throws Exception {
		super();

	}

	public String postBancaria(String pathWS, String token, String codigoTipoOperacion, String codigoInstitucionCredito, String codigoIsoDivisa, int coVentaBCV, String fechaOperacion, String montoDivisa) throws IOException {
		reloadUri();

		Gson gson = new Gson();
		operaciones_interbancaria reportar_oper = new operaciones_interbancaria();
		reportar_oper.setCodigoInstitucionCredito(codigoInstitucionCredito);
		reportar_oper.setCodigoVentaBCV(new codigoVentaBCV(coVentaBCV));
		reportar_oper.setFechaOperacion(fechaOperacion);
		reportar_oper.setMontoDivisa(new BigDecimal(montoDivisa));
		reportar_oper.setCodigoTipoOperacion(codigoTipoOperacion);
		reportar_oper.setCodigoIsoDivisa(new codigoIsoDivisa(codigoIsoDivisa));
		String operacion = gson.toJson(reportar_oper);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("operacion", operacion);
		HttpURLConnection conn = null;
		String line = "";
		String line2 = "";
		try {
			operacion = "operacion=" + operacion;
			operacion = operacion.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D");
			String postData = URLEncoder.encode("operacion=" + operacion, "UTF-8");
			System.out.println("URL : " + propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_BANCARIA));
			URL url = new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION_BANCARIA)+"?" + operacion);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization", token);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Length", Integer.toString(postData.length()));
			conn.setUseCaches(false);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes("");

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				line2 = line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

			while ((line = br.readLine()) != null) {
				line2 = line;
			}
		}
		return line2;
	}

}
