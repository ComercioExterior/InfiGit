package com.bdv.infi.webservices.client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.log4j.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.FileUtil;

import criptografia.TripleDes;

/**
 * Servicio que ofrece la conectividad con los Web Services. Es utilizado por
 * los manejadores ( com.bdv.infi.webservices.manager) para comunicarse con los web
 * services.
 * 
 * Existen dos formas basicas de crear el ClienteWs. Una es con la ayuda de las
 * Propiedades, para que el cliente quede parcialmente configurado. La otra es
 * cargando manualmente las Propiedades y configurando manualmente todos los
 * datos del cliente.
 * 
 * @author Camilo Torres
 * 
 */
public class ClienteWs {
	/**
	 * Se utiliza para probar cuando no hay webservices disponibles para
	 * pruebas. debe estar siempre en false y pasar a true solo cuando se haga
	 * la prueba.
	 */
	public boolean probando = false;

	/**
	 * Si se va a probar hay que indicar en el archivo de propiedades un nombre
	 * de la clase para pruebas y tambien indicar la clase de pruebas que debe
	 * estar en com.megasoft.bo.webservice.clasesparaprueba
	 */
	private String nombreDeLaClaseParaPruebas = null;

	/**
	 * URL del web service. Es el end point address.
	 * 
	 * Valor por defecto: http://192.168.1.23/axis2/services/FrontEnd
	 */
	private String urlDelWs = "http://192.168.1.23/axis2/services/FrontEnd";

	/**
	 * Nombre de la operacion que va a ser llamada en el web service. Cada
	 * operacion o cada consulta tiene un nombre diferente. Aqui debe colocarse
	 * ese nombre, el cual es provisto por quien hace el web service.
	 * 
	 * Valor por defecto: AccesoDatos
	 */
	private String operacion = "AccesoDatos";

	/**
	 * Namespace utilizado en el xml del sobre SOAP para el web service. Verlo
	 * en el WSDL del web service o consultarlo con quien hace el webservice.
	 * 
	 * Valor por defecto: http://FrontEnd/xsd
	 */
	private String namespace = "http://FrontEnd/xsd";

	/**
	 * Prefijo utilizado para el xml del sobre SOAP. Puede ser cualquier
	 * prefijo.
	 * 
	 * Valor por defecto: ns1
	 */
	private String prefijoNamespace = "ns1";

	/**
	 * Indica si se debe utilizar autenticacion para el uso del web service. Si
	 * contiene "true" se usara autenticacion; de otro modo no se usara
	 * autenticacion.
	 * 
	 * Valor por defecto: false
	 */
	private String secure = "false";

	/**
	 * Contexto del servidor de aplicaciones, usado para obtener los path de
	 * forma relativa
	 */
	private ServletContext contexto = null;

	private static Propiedades prop = null;

	private static HashMap<Class, IMarshallingContext> mapaMarshallingContext = new HashMap<Class, IMarshallingContext>();

	private static HashMap<Class, IUnmarshallingContext> mapaUnmarshallingContext = new HashMap<Class, IUnmarshallingContext>();

	private static HashMap<String, ServiceClient> mapaServiceClient = new HashMap<String, ServiceClient>();

	private static ConfigurationContext ctx = null;

	private String nombreDeLaOperacion = null;
	
	/*** Logger APACHE*/
	private Logger logger = Logger.getLogger(ClienteWs.class);

	/**
	 * Crea un cliente configurandolo automaticamente a partir de las
	 * propiedades. Se le debe indicar el nombre de la propiedad que corresponde
	 * a la operacion a utilizar.
	 * 
	 * Las propiedades que utiliza son:
	 * <ul>
	 * <li>url_del_ws
	 * <li>namespace
	 * <li>PrefijoNamespace
	 * <li>secure
	 * </ul>
	 * Por supuesto tambien busca la propiedad correspondiente a la operacion
	 * que se va a buscar; por ejemplo, la operacion puede ser
	 * "operacion.DatosDeContactoDelCliente", que retornaria "AccesoDatos" segun
	 * el archivo de propiedades.
	 * 
	 * Debes indicarle el parametro o llamar al metodo de ejecucion con el
	 * parametro.
	 * 
	 * @param operacion
	 *            nombre de la propiedad de la operacion que se quiere utilizar
	 * @return un cliente configurado para indicarle el parametro y ponerlo a
	 *         funcionar
	 * @throws IOException
	 *             Si falla al leer del archivo de propiedades o si falla al
	 *             conectarse con el web service.
	 */
	public static ClienteWs crear(String operacion, ServletContext contexto)
			throws IOException {
		if (ClienteWs.prop == null) {
			ClienteWs.prop = Propiedades.cargar(contexto);
		}

		ClienteWs cliente = new ClienteWs();

		cliente.urlDelWs = ClienteWs.prop.getProperty("endpoint." + operacion);
		cliente.namespace = ClienteWs.prop
				.getProperty("namespace." + operacion);
		cliente.operacion = ClienteWs.prop
				.getProperty("operacion." + operacion);
		cliente.probando = ClienteWs.prop.getProperty("probando." + operacion)
				.equalsIgnoreCase("si");
		cliente.nombreDeLaClaseParaPruebas = ClienteWs.prop
				.getProperty("claseparapruebas." + operacion);

		cliente.prefijoNamespace = ClienteWs.prop
				.getProperty("PrefijoNamespace");
		cliente.secure = ClienteWs.prop.getProperty("secure");

		cliente.contexto = contexto;

		cliente.nombreDeLaOperacion = operacion;

		return cliente;
	}

	/**
	 * Realiza la comunicacion con el web service. Es el metodo que hace la
	 * magia.
	 * 
	 * Para que funcione, todos los parametros del objeto deben estar bien
	 * configurados, ya que los valores por defecto no funcionaran para todos
	 * los casos.
	 * 
	 * Debe estar indicado el parametro.
	 * 
	 * @return el xml que retorna el web service
	 * @throws AxisFault
	 *             Si falla la comunicacion con el web service o si este provoca
	 *             un error
	 * @throws JiBXException
	 */
	public Object enviarYRecibir(Object objetoAEnviar, Class claseAEnviar,
			Class claseARecibir, String username, String ip) throws AxisFault, JiBXException {
		String xmlAEnviar ="";
		try{
			xmlAEnviar = convertirObjetoAXml(objetoAEnviar, claseAEnviar);
			
			/*
			 * En este punto raliza el envio del web service.
			 */
			if (ctx == null) {
				synchronized ("createConfigurationContextFromFileSystem") {
					if (ctx == null) {
						ctx = ConfigurationContextFactory
						.createConfigurationContextFromFileSystem(
						contexto==null? FileUtil.getRootApplicationPath()+"/": contexto.getRealPath("/WEB-INF/"),
						contexto==null? FileUtil.getRootApplicationPath()+"/axis2.xml": contexto.getRealPath("/WEB-INF")+"/axis2.xml"
								);
					}
				}
			}
	
			Options options = new Options();
			options.setManageSession(true);
	
			ServiceClient cliente = null; 		
			
			if ( !mapaServiceClient.containsKey(this.urlDelWs) )
			{
	
			                if (secure.equals("true"))
			                    cliente = new ServiceClient(ctx, null);
			                else
			                    cliente = new ServiceClient(null, null); 
	
			                cliente.setOptions(options);
			                cliente.setTargetEPR(new EndpointReference(this.urlDelWs)); 
	
			                synchronized (mapaServiceClient)
			                {
			                    mapaServiceClient.put(this.urlDelWs, cliente);
			                }
			}    
			else
			{
			    cliente = (ServiceClient) mapaServiceClient.get(this.urlDelWs);
			} 
			
			logger.info("URL Servicio: "+ urlDelWs);
//			logger.info("XML A Enviar: "+ xmlAEnviar);		
			try {
				Propiedades propiedades =  Propiedades.cargar();
				logger.info("URL Servicio: "+ urlDelWs);
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
				TripleDes desc = new TripleDes();
				String userincriptado = desc.cifrar(rutaCustodio1,rutaCustodio2, xmlAEnviar);
				logger.info("XML A Enviar: "+ userincriptado);		
				System.out.println("XML A Enviar: "+ userincriptado);
			} catch (Exception e) {
				System.out.println("error incriptar clave : "+e);
			}
			cliente.setOptions(options);
			cliente.setTargetEPR(new EndpointReference(this.urlDelWs));
			
			OMFactory omFactory = OMAbstractFactory.getOMFactory();
			OMNamespace omNamespace = omFactory.createOMNamespace(this.namespace,
					this.prefijoNamespace);
			OMElement request = omFactory.createOMElement(this.operacion,
					omNamespace);
			
			OMElement elemParametro = omFactory.createOMElement("in1", null);
			/*
			 * aqui coloca el objeto ya convertido a xml de la primera parte de este
			 * metodo. luego lo envia la web service y recibe la respuesta
			 */
			elemParametro.setText(xmlAEnviar);
			request.addChild(elemParametro);
	
			OMElement usernameElem = omFactory.createOMElement("username", null);
			usernameElem.setText(username);
			request.addChild(usernameElem);
			
			OMElement ipElem = omFactory.createOMElement("ip", null);
			ipElem.setText(ip);
			request.addChild(ipElem);
	
			OMElement respuesta = cliente.sendReceive(request);
			
			//logger.info("XML de Respuesta: "+ respuesta);
			return convertirXmlAObjeto(respuesta.getFirstElement().getText(), claseARecibir);
			
		}catch(AxisFault e){
			logger.info("Error en la ejecución del servicio: (AxisFault)"+urlDelWs+", Error:"+e.getMessage());		
			//logger.info("Error en la ejecución del servicio: (XMLEnviado)"+xmlAEnviar);
			throw e;
		}catch(JiBXException j){
			logger.info("Error en la ejecución del servicio: (JiBXException)"+urlDelWs+", Error:"+j.getMessage());
			//logger.info("Error en la ejecución del servicio: (XMLEnviado)"+xmlAEnviar);
			throw j;
		}
		
	}


	/**
	 * convierte un objeto a xml usando jibx
	 * 
	 * @param objetoAEnviar
	 *            es la instancia a convertir a XML
	 * @param claseAEnviar
	 *            es la clase de la instancia
	 * @return un string con el xml
	 * @throws JiBXException
	 *             si falla la conversion
	 */
	protected String convertirObjetoAXml(Object objetoAEnviar,
			Class claseAEnviar) throws JiBXException {
		IMarshallingContext mctx = mapaMarshallingContext.get(claseAEnviar);
		if (mctx == null) {
			IBindingFactory bfact = BindingDirectory.getFactory(claseAEnviar);
			mctx = bfact.createMarshallingContext();
			mapaMarshallingContext.put(claseAEnviar, mctx);
		}
		StringWriter swri = new StringWriter();

		synchronized (mctx) {
			mctx.marshalDocument(objetoAEnviar, "ISO-8859-1", null, swri);
		}
		return swri.getBuffer().toString();
	}

	/**
	 * Convierte un string xml a objeto utilizando jibx
	 * 
	 * @param claseARecibir
	 *            es la clase del objeto que se va a generar
	 * @param xml
	 *            a convertir a objeto
	 * @return el objeto
	 * @throws JiBXException
	 *             si falla la conversion
	 */
	public Object convertirXmlAObjeto(String xml, Class claseARecibir)
			throws JiBXException {
		/*
		 * es la ultima parte, convertir el resultado en un objeto
		 */
		IUnmarshallingContext uctx = mapaUnmarshallingContext
				.get(claseARecibir);
		if (uctx == null) {
			IBindingFactory bfact = BindingDirectory.getFactory(claseARecibir);
			uctx = bfact.createUnmarshallingContext();
			mapaUnmarshallingContext.put(claseARecibir, uctx);
		}

		/*
		 * aqui convierte en objeto el xml de respuesta del web service
		 */
		//TODO quitar el print
		StringReader sre = new StringReader(xml);

		synchronized (uctx) {
			return uctx.unmarshalDocument(sre, null);
		}
	}

	/**
	 * URL del web service. Es el end point address.
	 * 
	 * Valor por defecto: http://192.168.1.23/axis2/services/FrontEnd
	 */
	public String getUrlDelWs() {
		return urlDelWs;
	}

	/**
	 * URL del web service. Es el end point address.
	 * 
	 * Valor por defecto: http://192.168.1.23/axis2/services/FrontEnd
	 */
	public void setUrlDelWs(String urlDelWs) {
		this.urlDelWs = urlDelWs;
	}

	/**
	 * Nombre de la operacion que va a ser llamada en el web service. Cada
	 * operacion o cada consulta tiene un nombre diferente. Aqui debe colocarse
	 * ese nombre, el cual es provisto por quien hace el web service.
	 * 
	 * Valor por defecto: AccesoDatos
	 */
	public String getOperacion() {
		return operacion;
	}

	/**
	 * Nombre de la operacion que va a ser llamada en el web service. Cada
	 * operacion o cada consulta tiene un nombre diferente. Aqui debe colocarse
	 * ese nombre, el cual es provisto por quien hace el web service.
	 * 
	 * Valor por defecto: AccesoDatos
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	/**
	 * Namespace utilizado en el xml del sobre SOAP para el web service. Verlo
	 * en el WSDL del web service o consultarlo con quien hace el webservice.
	 * 
	 * Valor por defecto: http://FrontEnd/xsd
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Namespace utilizado en el xml del sobre SOAP para el web service. Verlo
	 * en el WSDL del web service o consultarlo con quien hace el webservice.
	 * 
	 * Valor por defecto: http://FrontEnd/xsd
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Indica si se debe utilizar autenticacion para el uso del web service. Si
	 * contiene "true" se usara autenticacion; de otro modo no se usara
	 * autenticacion.
	 * 
	 * Valor por defecto: false
	 */
	public String getSecure() {
		return secure;
	}

	/**
	 * Indica si se debe utilizar autenticacion para el uso del web service. Si
	 * contiene "true" se usara autenticacion; de otro modo no se usara
	 * autenticacion.
	 * 
	 * Valor por defecto: false
	 */
	public void setSecure(String secure) {
		this.secure = secure;
	}

	/**
	 * Prefijo utilizado para el xml del sobre SOAP. Puede ser cualquier
	 * prefijo.
	 * 
	 * Valor por defecto: ns1
	 */
	public String getPrefijoNamespace() {
		return prefijoNamespace;
	}

	/**
	 * Prefijo utilizado para el xml del sobre SOAP. Puede ser cualquier
	 * prefijo.
	 * 
	 * Valor por defecto: ns1
	 */
	public void setPrefijoNamespace(String prefijoNamespace) {
		this.prefijoNamespace = prefijoNamespace;
	}

	public String toString() {
		return "URL: " + this.urlDelWs + "\nOperacion: " + this.operacion
				+ "\nNamespace: " + this.namespace + "\nPrefijoNamespace: "
				+ this.prefijoNamespace + "\nSecure: " + this.secure;
	}

	/**
	 * Contexto del servidor de aplicaciones, usado para obtener los path de
	 * forma relativa
	 */
	public ServletContext getContexto() {
		return contexto;
	}

	/**
	 * Contexto del servidor de aplicaciones, usado para obtener los path de
	 * forma relativa
	 */
	public void setContexto(ServletContext contexto) {
		this.contexto = contexto;
	}
}
