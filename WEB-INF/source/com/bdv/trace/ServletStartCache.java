package com.bdv.trace;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import megasoft.DBServlet;
import com.megasoft.enginecachecontrol.MegaCacheControl;

/**
 * @author ncv
 * Servlet encargado de iniciar el control de cache.
 */
public class ServletStartCache extends DBServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Instancia de engine de cache
	private MegaCacheControl cache = new MegaCacheControl();
	
	private Logger logger = Logger.getLogger(ServletStartCache.class);
	
	public void init(){	
		try{
			logger.info("iniciando ServletCache");
			ServletContext context = getServletContext();
			// Obtiene los parametros de configuracion necesarios para el engine
			//String path_config 	= context.getRealPath("/WEB-INF/cache_config.xml");
			String path_config 	= null;
			String path_log4j 	= context.getRealPath("/WEB-INF/log4jcache.properties");
			
			try{
				//Inicia el  engine
				cache.startCache(path_config	, path_log4j);	
			}
			catch (Exception e) {
				logger.error("Error iniciando el cache  " + e.getMessage());
			}
			super.init();
		}
		catch (ServletException e) {
			logger.error("Error iniciando el Servlet de cache" + e.getMessage());
		}	
	}
}