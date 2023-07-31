package org.bcv.serviceINTERVENCION;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.security.cert.CertificateException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import org.json.*;
import org.glassfish.jersey.client.*;
import com.google.gson.Gson;

public class JerseyClient2 {
	private String urlWs;
	private Client client = null;
	private WebTarget target = null;
	ClientConfig clientConfig = null;
	String jsonAenviarVista =null;
	public JerseyClient2(String urlws) {
		
		this.urlWs = urlws;
		client = ClientBuilder.newClient();
		target = client.target(urlWs);
			
	}
	
	public void reloadUri(){
		target = null;
		target = client.target(urlWs);
		clientConfig = new ClientConfig();
	}
	


	
public String getRequestHolaMundo(){
		
ClientConfig config = new ClientConfig();
Client client = ClientBuilder.newClient(config);
WebTarget target = client.target(getBaseURI());
String response = target.request().get(String.class);
System.out.println("response-->"+response);
return response;
		 
}


public String getRequestIniciarsesion (String pathWS, String usuario, String contrasena){
	 reloadUri();
	 target = target.path(pathWS);
	 
	 clientConfig.register(JerseyClient.class);
	 client=ClientBuilder.newClient(clientConfig);
	 Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON).header("username", usuario).header("password", contrasena);
	 Response response = invocationBuilder.get();
	 String respuesta = response.getHeaderString("Authorization");
	 System.out.println("token-->"+respuesta);
	return respuesta;
}


public IntervencionActiva[] getRequestActivas (String pathWS, String token){
		reloadUri();
		Gson gson= new Gson();
	 target = target.path(pathWS);
	 clientConfig.register(JerseyClient.class);
	 client=ClientBuilder.newClient(clientConfig);
	 Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON).header("Authorization", token);
	System.out.println("jsonAenviarVista-->"+jsonAenviarVista);
	String response1 = invocationBuilder.get(String.class);
	IntervencionActiva[] lcs = gson.fromJson(response1, IntervencionActiva[].class);

	
	return lcs;
}


public String postIntervencion (String pathWS, String token,String codigoTipoOperacion,String codigoInstitucionCredito,String codigoIsoDivisa,int coVentaBCV,String fechaOperacion,Double montoDivisa) throws JSONException, ServiceException{
	reloadUri();

	Gson gson = new Gson();
	target = target.path(pathWS);
	clientConfig.register(JerseyClient.class);
	client=ClientBuilder.newClient(clientConfig);
	operaciones_interbancaria reportar_oper = new operaciones_interbancaria();
	
	reportar_oper.setCodigoInstitucionCredito(codigoInstitucionCredito);
	reportar_oper.setCodigoVentaBCV(new codigoVentaBCV(coVentaBCV));
	reportar_oper.setFechaOperacion(fechaOperacion);
	reportar_oper.setMontoDivisa(new BigDecimal(montoDivisa));
	reportar_oper.setCodigoTipoOperacion(codigoTipoOperacion);
	reportar_oper.setCodigoIsoDivisa(new codigoIsoDivisa(codigoIsoDivisa));
	String operacion = gson.toJson(reportar_oper);
	
	String response1=null;
	target = target.queryParam("operacion",operacion.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D"));
	Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON).header("Authorization", token);
	Response response =invocationBuilder.post(Entity.entity(null, MediaType.APPLICATION_JSON));
	
	int estatus = response.getStatus();
	 
	 if (Status.OK.getStatusCode() == estatus){
		 
		 response1  = response.readEntity(String.class);
	 }else{
		 
		 throw new ServiceException(response.readEntity(String.class), estatus);
		
	 }
	 
	return response1;
	
}

public static void disableSslChecks() throws KeyManagementException,
NoSuchAlgorithmException,
KeyStoreException,
UnrecoverableKeyException,
CertificateException,
IOException,
NoSuchProviderException {
final SSLContext sc = SSLContext.getInstance("SSL");
TrustManager[] trustManagers = new TrustManager[]{};
sc.init(null,trustManagers, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier(
new HostnameVerifier() {
	
public boolean verify(String arg0, SSLSession arg1) {
	return true;
}
}
);
}

 
private static URI getBaseURI(){
	return UriBuilder.fromUri("https://intervencionbancaria.cert.extra.bcv.org.ve/intervencion/").build();
}



}