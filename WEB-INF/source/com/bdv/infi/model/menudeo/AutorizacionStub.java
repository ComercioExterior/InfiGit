package com.bdv.infi.model.menudeo;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.bdv.infi.util.Utilitario;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import org.apache.axis.transport.http.HTTPConstants;
import java.lang.reflect.Method;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ve.org.bcv.service.AutorizacionPortBindingStub;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import java.util.Hashtable;
import criptografia.TripleDes;
import electric.xml.Node;

/**
 * Establece la conexion con los servicios web de bcv
 * 
 * @author Angel Herrera(NM36635)
 * 
 */
public class AutorizacionStub {

	AutorizacionPortBindingStub Stub;
	Propiedades propiedades;
	boolean Estatus = false;
	HashMap<String, String> Documento;
	int Cantidad = 0;
	org.w3c.dom.NodeList Lista;
	org.w3c.dom.Node nodo;
	CredencialesDAO credencialesDAO = null;
	DataSet _credenciales = new DataSet();

	public AutorizacionStub(){
		verificarServicios();
		buscarUsuarioYClaveWs();
	}

	// Establecer conexion con el servicios
	// https://casacambioserv-cert.extra.bcv.org.ve:443/service/autorizacion
	public void verificarServicios() {
		try {
			this.propiedades = Propiedades.cargar();
			this.Stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_MENUDEO)), null);
			this.Estatus = true;
		} catch (Exception e) {
			Logger.error(true, "AutorizacionStub : veriricarServicios() " + e.toString());
		}
	}

	// Stub.TASASCAMBIO()
	public void Cargar(String WSParametro, String NODOPadre) throws ParserConfigurationException, SAXException, IOException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document document = documentBuilder.parse(new InputSource(new StringReader(WSParametro)));
			((org.w3c.dom.Document) document).getDocumentElement().normalize();
			this.Lista = document.getElementsByTagName(NODOPadre);
			this.Cantidad = Lista.getLength();
		} catch (Exception e) {
			Logger.error(this, "AutorizacionStub : Cargar() " + e.toString());
		}
	}

	/**
	 * Recorrer todo los elementos del servicios XML mediante la lista
	 * 
	 * @param object
	 * @param metodo
	 * @return
	 * @throws Exception
	 */
	public boolean procesar(Object object, Method metodo) {
		boolean valor = false;
		this.Documento = new HashMap<String, String>();
		for (int i = 0; i < Cantidad; i++) {
			this.nodo = Lista.item(i);
			if (nodo.getNodeType() == Node.ELEMENT_NODE) {
				try {
					metodo.invoke(object);
				} catch (Exception e) {
					Logger.error(this, "AutorizacionStub : procesar() " + e.toString());
				}

			}
			valor = true;
		}

		return valor;

	}

	public void buscarUsuarioYClaveWs() {
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			this.credencialesDAO = new CredencialesDAO(dso);
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.MENUDEO);
			_credenciales = credencialesDAO.getDataSet();
			String userName = "";
			String clave = "";

			if (_credenciales.next()) {
				if (propiedades.getProperty("use_https_proxy").equals("1")) {
					Utilitario.configurarProxy();
				}
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
				TripleDes desc = new TripleDes();
				userName = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("CLAVE"));
				System.out.println("Clave : " + clave);
				Hashtable headers = (Hashtable) Stub._getProperty(HTTPConstants.REQUEST_HEADERS);
				if (headers == null) {
					headers = new Hashtable();
					Stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
				}
				headers.put("Username", userName);
				headers.put("Password", clave);
			}
		} catch (Exception e) {
			Logger.error(this, "AutorizacionStub : buscarUsuarioYClaveWs() " + e.toString());
		}
	}

}
