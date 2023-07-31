package org.bcv.serviceINTERVENCION;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Type;
//import java.security.SecureRandom;
import javax.mail.Multipart;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.lang.Object;
//import javax.mail.Multipart;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.security.cert.CertificateException;
import javax.ws.rs.client.*;
//import javax.ws.rs.client.Configuration;
//import javax.ws.rs.client.Configuration;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.*;
//import javax.ws.rs.client.ClientFactory;
import org.json.*;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.HttpsURLConnection.*;
import org.glassfish.jersey.client.ClientConfig;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.glassfish.jersey.client.*;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.ContentDisposition.ContentDispositionBuilder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;
import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.media.multipart;

import com.bdv.infi.data.FechaValor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JerseyClient3 {
	

	@SuppressWarnings("deprecation")
	public String postRequestVentas134 (String pathWS, String token,Object arreglo) throws IOException, ServiceException{
		String codigoVenta = "";
		try{
		//ArcCabecera
		//Conecta al servicio
		/**
		*
		* //Valor obtenido de consulta de datos de BASE DE DATOS, llenar objeto con la informacion y luego mapear a JSON
		*
		ArcCabecera jsonObject = new ArcCabecera();
		//Se mapea el objeto a JSON
		ObjectMapper mapper = new ObjectMapper();
		String valueMapper = mapper.writeValueAsString(jsonObject);
		System.out.println("ArcCabecera = " + valueMapper);
		*
		*
		*/
			Gson gson = new Gson();
//			 cliente1 arreglo= new cliente1();
//		   	 	List<operaciones> listOperaciones= new ArrayList<operaciones>();
//		   	 
//		   	 	operaciones reportar = new operaciones();
//	
//		   		reportar.setCodigoCliente(codigoCliente);
//		   		reportar.setNombreCliente(nombreCliente);
//		   		reportar.setFechaValor(fechaValor1);
//		   		reportar.setCodigoTipoOperacion(codigoTipoOperacion);
//		   		reportar.setMontoDivisa(montoDivisa);
//		   		reportar.setTipoCambio(tipoCambio);
//		   		reportar.setCodigoCuentaDivisa(codigoCuentaDivisa);
//		   		reportar.setCodigoCuentaBs(codigoCuentaBs);
//		   		reportar.setCodigoIsoDivisa(new codigoIsoDivisa(codigoIsoDivisa));
//		   		reportar.setCodigoVentaBCV(new codigoVentaBCV(1000));
//		   		reportar.getCodigoVentaBCV().setCoVentaBCV(coVentaBCV);
//		   		listOperaciones.add(reportar);
//		   		arreglo.setOperaciones(listOperaciones);
		   		
		   		String arcVentas1 = gson.toJson(arreglo);
		System.out.println("arcVentas1--->"+arcVentas1);
		//Obtienes el JSON para transmitir
		ObjectMapper mapper = new ObjectMapper();
		String valueMapper = mapper.writeValueAsString(arreglo);
		System.out.println("valueMapper--->"+valueMapper);

		//Se crea el archivo con los datos del JSON
		File data = File.createTempFile("test", ".json");
		FileWriter writer = new FileWriter(data);
		writer.write(arcVentas1);
		writer.close();
		//Solo para verificar la data en el archivo
		BufferedReader readerBuff = new BufferedReader(new FileReader(data));
		System.out.println("writer = " + readerBuff.readLine());
		readerBuff.close();


		//Se configura el MULTIPART
		final ClientConfig clientConfigMultipart = new ClientConfig();
		clientConfigMultipart.register(MultiPartFeature.class);
		clientConfigMultipart.register(LoggingFilter.class);
		Client clientMultipart = ClientBuilder.newClient(clientConfigMultipart);
		WebTarget targetMultipart = clientMultipart.target("https://intervencionbancaria.cert.extra.bcv.org.ve");
		//SE CARGA EL MULTIPART
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("arcVentas", data, MediaType.TEXT_PLAIN_TYPE);
		MultiPart entity = new FormDataMultiPart().bodyPart(fileDataBodyPart);
		//SE INVOCA
		Response responseWs = targetMultipart.path("/intervencion/api/notificaOperaciones/Venta").request().header(HttpHeaders.AUTHORIZATION, token).post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE), Response.class);

		codigoVenta = responseWs.readEntity(String.class);

		System.out.println("codigoVenta = " + codigoVenta);
		System.out.println("responseWs-->"+responseWs);
//		PrintWriter out = response.getWriter();
//		out.println("codigoVenta = " + codigoVenta);

		} catch (Exception e) {
		e.printStackTrace();
		System.out.println("error multipart-->"+e.getMessage()+" "+e.getStackTrace());
		}

		return codigoVenta;
	}
}