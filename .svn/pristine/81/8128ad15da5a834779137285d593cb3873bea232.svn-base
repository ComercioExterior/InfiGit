package com.bdv.infi.webservices.manager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;

/**
 * Sirve para manejar todo lo que tiene que ver con los datos de los clientes o
 * que se obtienen a partir de una cedula de identidad (o rif) de un cliente.
 *
 * Contiene un metodo para traer un cliente desde los web services.
 *
 * Utiliza jibx para realizar el xml binding desde el web service.
 *
 * @author camilo torres
 *
 */
public class ManejadorUltraTemprano {
	private ServletContext contexto = null;

	private Logger logger = Logger.getLogger(TomaOrdenDAO.class);
	Propiedades wsProperties = null;

	/**
	 * Datos de usuario/clave del usuario
	 */
	private CredencialesDeUsuario credenciales = null;

	public ManejadorUltraTemprano(ServletContext cont,
			CredencialesDeUsuario credenciales) {
		this.contexto = cont;
		this.credenciales = credenciales;
	}

	
	public boolean esSolicitudUltraTemprano(String ciRif, String tipoPersona, String fechaDesde, String fechaHasta, String ip) throws Exception {
		
		boolean solicitudUltraTemprano = false;		
		String ciRifCompleto = tipoPersona.toUpperCase()+Utilitario.rellenarCaracteres(ciRif, '0', 13, false);
		logger.info("Ejecutando Invocación del Servicio Ultra Temprano...");
		logger.debug("---------Parametros:-----");
		logger.debug("CiRif Completo: "+ciRifCompleto);
		logger.debug("Fecha Desde: " +fechaDesde);
		logger.debug("Fecha Hasta: " +fechaHasta);
		//cargar archivo ws.properties de la aplicacion
		wsProperties = Propiedades.cargar(contexto);
		//obtener el enpoint de la propiedad correspondiente a la operación ultra temprano
		//y obtenemos el url del servicio
		String urlWs = wsProperties.getProperty("endpoint." + "operClienteUltraTemprano");
    	logger.debug("Endpoint: "+ urlWs);
		PostMethod post = null;
    	HttpClient httpclient = new HttpClient();
    	httpclient.getParams().setParameter("http.connection.timeout", 50000);
    	
    	//XML de ENTRADA concatenado con los parámetros de entrada del servicio web:
    	String xmlAux = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:BonoIntf-IBono\"> <soapenv:Header/>   <soapenv:Body>      <urn:WConsulta_SoliLiq soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">         <status>0</status>         <mensaje/>         <rifi>"+ciRifCompleto+"</rifi>         <fldesde>"+fechaDesde+"</fldesde>         <flhasta>"+fechaHasta+"</flhasta>         <cadxml1/>         <cadxml2/>      </urn:WConsulta_SoliLiq>   </soapenv:Body></soapenv:Envelope>";
		try {
			//Invocar Servicio
			post = new PostMethod(urlWs);

			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));

			StringRequestEntity str = new StringRequestEntity(xmlAux, "text/xml", "UTF-8");

			post.setRequestEntity(str);

			int res = httpclient.executeMethod(post);			

			if (res != 200) {
				logger.error("Error invocando el servicio Ultra Temprano... el servidor ha respondido " + res);
				throw new Exception("Error invocando el servicio Ultra Temprano. El servidor ha respondido " + res);
			}
			
			//respuesta del servicio (XML de SALIDA) se guarda en un InputString
			InputStream in = post.getResponseBodyAsStream();
			//Llevar xml a string 
			BufferedInputStream bis = new BufferedInputStream(in);
		    ByteArrayOutputStream buf = new ByteArrayOutputStream();
		    int result = bis.read();
		    while(result != -1) {
		      byte b = (byte)result;
		      buf.write(b);
		      result = bis.read();
		    }
		    logger.debug("XML DE SALIDA: ");
		    logger.debug(buf.toString());

		    //Interpretar el xml resultante como string
		    if (buf.toString().indexOf("<faultstring>")>-1){
		    	logger.info("Error en la consulta al servicio web Ultra Temprano. No est&acute;n retornando los datos correctos.");
		    	throw new Exception("Error en la consulta al Servicio Web Ultra Temprano. No est&aacute;n retornando los datos correctos.");
		    }else{
		    	if(buf.toString().toUpperCase().indexOf("<OPER01>")>-1){
		    		solicitudUltraTemprano = true;
		    	}
		    }
			
		} catch (Exception e){
			logger.error("Servicio Web Ultra Temprano", e);
			throw e;
		}
		logger.info("SolicitudUltraTemprano: "+solicitudUltraTemprano);
		return solicitudUltraTemprano;
	}
			
}


