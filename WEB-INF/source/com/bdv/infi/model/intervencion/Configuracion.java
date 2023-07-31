package com.bdv.infi.model.intervencion;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
//import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sql.DataSource;
import javax.ws.rs.core.UriBuilder;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import criptografia.TripleDes;


public class Configuracion {
	static Propiedades propiedades;
	private CredencialesDAO credencialesDAO = null;
	private DataSet _credenciales = new DataSet();
	// HttpURLConnection connn = null;
	private String urlWs = "";
	String userName = "";
	String clave = "";

	public Configuracion() {

		Utilitario.configurarProxy();

		try {
			doTrustToCertificates();
			propiedades = Propiedades.cargar();
			ObtenerCredencialWs();
		} catch (Exception e) {
			System.out.println("Configuracion : Configuracion() " + e);

		}
	}

	public void reloadUri() {
	}

	private static URI getBaseURI() throws IOException {

		return UriBuilder.fromUri(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_INTERVENCION)).build();
		// https://intervencionbancaria.cert.extra.bcv.org.ve/intervencion/
	}

	public void ObtenerCredencialWs() {
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			this.credencialesDAO = new CredencialesDAO(dso);
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_INTERVENCION);
			_credenciales = credencialesDAO.getDataSet();

			if (_credenciales.next()) {
				if (propiedades.getProperty("use_https_proxy").equals("1")) {
					Utilitario.configurarProxy();
				}
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
				TripleDes desc = new TripleDes();
				userName = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("USUARIO"));
				System.out.println("Usuario :" + userName);
				clave = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("CLAVE"));//;"ccs2020";
				System.out.println("Clave :" + clave);
			}
		} catch (Exception e) {
			System.out.println("AutorizacionStub : ObtenerCredencialWs() " + e);
		}
	}

	public void doTrustToCertificates() throws Exception {
//		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}
		} };
		SSLContext sc = SSLContext.getInstance("TLSv1");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
					System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
				}
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	
//	public void yta(){
//		
//		TrustManager[] trustAllCerts = new TrustManager[]{
//		        new X509TrustManager() {
//
//		            public java.security.cert.X509Certificate[] getAcceptedIssuers()
//		            {
//		                return null;
//		            }
//		            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
//		            {
//		                //No need to implement.
//		            }
//		            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
//		            {
//		                //No need to implement.
//		            }
//		        }
//		};
//
//		// Install the all-trusting trust manager
//		try 
//		{
//		    SSLContext sc = SSLContext.getInstance("SSL");
//		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
//		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//		} 
//		catch (Exception e) 
//		{
//		    System.out.println(e);
//		}
//	}
}
