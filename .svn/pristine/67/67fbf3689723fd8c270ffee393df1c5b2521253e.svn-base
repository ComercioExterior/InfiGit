package models.log4j;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bdv.infi.util.FileUtil;

/**
 * Servlet que inicia el log4j
 * @author nvisbal
 */
public class log4jInicializacion extends HttpServlet {

	public void destroy() {
		
	}

	public ServletConfig getServletConfig() {
		return null;
	}

	public String getServletInfo() {
		return null;
	}
	@SuppressWarnings("unused")
	public void init(ServletConfig arg0) throws ServletException {

		Properties config = new Properties();
		
		System.out.println("Inicializando Logger");
		
		String archivo = FileUtil.getRootApplicationPath();
		if (archivo.endsWith("WEB-INF")){
			archivo = archivo + File.separator + "log4j.ini";
		} else {
			archivo = archivo + "WEB-INF" + File.separator + "log4j.ini";
		}
    	System.out.println("Encontrado archivo..." + archivo);
		PropertyConfigurator.configure(archivo);
		
		Logger logger = Logger.getLogger("log4jInicializacion");
		logger.info("Escribiendo...");
	}

	public void service(ServletRequest arg0, ServletResponse arg1) throws IOException, ServletException {
	
	}
}

