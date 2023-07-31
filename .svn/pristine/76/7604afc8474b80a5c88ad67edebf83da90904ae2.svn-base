package models.security.login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

/**
 * Archivo de propiedades para configurar los Web Services.
 * Es un java.util.Properties. Utilizar el metodo estatico
 * cargar() para crear los objetos de esta clase.
 * 
 * @author Camilo Torres
 * 
 */
public class Propiedades extends Properties {
	/**
	 * Nombre del archivo de propiedades
	 */
	public static String ARCHIVO_DE_PROPIEDADES_WS = "/WEB-INF/ws.properties";

	/**
	 * Carga un archivo de propiedades
	 * 
	 * @return Archivo de propiedades
	 * @throws IOException
	 *             Si no puede leer el archivo de propiedades
	 */
	public static Propiedades cargar(ServletContext contexto) throws IOException {
		Propiedades p = new Propiedades();

		
		 // FileInputStream inStream = new FileInputStream(
		 // ARCHIVO_DE_PROPIEDADES_WS);
		 
		InputStream inStream = new FileInputStream(contexto.getRealPath(ARCHIVO_DE_PROPIEDADES_WS));

		p.load(inStream);
		inStream.close();
		return p;
	}
}
