package com.bdv.infi.model.intervencion;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import org.glassfish.jersey.client.ClientConfig;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.data.OficinaDTO;

import com.bdv.infi.data.OficinaRespuestaDTO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import com.google.gson.Gson;

import criptografia.TripleDes;

public class ConfiguracionBeta {
	static Propiedades propiedades;
	private CredencialesDAO credencialesDAO = null;
	private DataSet _credenciales = new DataSet();
	private String urlWs = "";
	protected Client client = null;
	protected WebTarget target = null;
	String userName = "";
	String clave = "";
	ClientConfig clientConfig = null;

	public ConfiguracionBeta() {

		Utilitario.configurarProxy();

		try {
			propiedades = Propiedades.cargar();
			
		} catch (Exception e) {
			System.out.println("Configuracion : Configuracion() " + e);
			
		}
		// this.urlWs = urlws;
		client = ClientBuilder.newClient();
		target = client.target(urlWs);
		ObtenerHolaMundo();
		
		OficinaRespuestaDTO resp = new OficinaRespuestaDTO();
		resp = ObtenerOficinas();
		
		for (OficinaDTO e : resp.getData()) {
			System.out.println( "ciudad "+ e.getCity());
		}
		
		
		System.out.println(" respeuesta del webservices "+  resp.getData().get(1).getCode());
		
	}

	public void reloadUri() {
		target = null;
		target = client.target(urlWs);
		clientConfig = new ClientConfig();
	}
	
	public OficinaRespuestaDTO ObtenerOficinas() {

		
		String response = "";
		System.out.println("Llego prueba");
		Gson gson = new Gson();
		try {
			String urlWs = "http://bdvdigital.banvenez.com:443/bdvx-geolocalizacion/api/offices/all";
			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);
			WebTarget target = client.target(urlWs);
			System.out.println("target : " + target);
			response = target.request().get(String.class);
			System.out.println("response : " + response);
			
			

		} catch (Exception e) {
			System.out.println("Configuracion : ObtenerOficinas() " + e);

		}

		return gson.fromJson(response, OficinaRespuestaDTO.class);

	}

	public String ObtenerHolaMundo() {

		String response = "";
		reloadUri();
		desactivarSsl();

		try {
			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);
			WebTarget target = client.target(getBaseURI());
			response = target.request().get(String.class);
			System.out.println("response : " + response);

		} catch (Exception e) {
			System.out.println("Configuracion : ObtenerHolaMundo() " + e);

		}

		return response;

	}

	private static URI getBaseURI() throws IOException {

		return UriBuilder.fromUri(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION)).build();
		// https://intervencionbancaria.cert.extra.bcv.org.ve/intervencion/
	}

	public void ObtenerCredencialWs() {
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			this.credencialesDAO = new CredencialesDAO(dso);
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.MENUDEO);
			_credenciales = credencialesDAO.getDataSet();

			if (_credenciales.next()) {
				if (propiedades.getProperty("use_https_proxy").equals("1")) {
					Utilitario.configurarProxy();
				}
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
				TripleDes desc = new TripleDes();
				userName = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("CLAVE"));

			}
		} catch (Exception e) {
			System.out.println("AutorizacionStub : ObtenerCredencialWs() " + e);
		}
	}

	public static void desactivarSsl() {

		try {
			final SSLContext sc = SSLContext.getInstance("SSL");
			TrustManager[] trustManagers = new TrustManager[] {};
			sc.init(null, trustManagers, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});

		} catch (Exception e) {
			System.out.println("AutorizacionStub : desactivarSsl() " + e);

		}

	}
}
