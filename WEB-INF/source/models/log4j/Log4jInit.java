package models.log4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * HttpServlet que lee el archivo de propiedades del log4j lo modifica y guarda, para que el log
 * quede a nivel de infi.
 * Levanta el archivo de propiedades modificado, para que se pueda utilizar el Logger
 * @author elaucho 
 */
public class Log4jInit extends HttpServlet {
	
    /*** serialVersionUID */
    private static final long serialVersionUID = 1L;

    
    /*** SLASH */
    private final String SLASH = "/";

    /**
     * Init() del servlet
     */
    public void init() {
    	
        String prefix  = getServletContext().getRealPath(SLASH);
        String file    = getInitParameter("log4j-init-file");      

        if (file != null) {
            PropertyConfigurator.configure(prefix + file);

            Logger logger = Logger.getLogger(Log4jInit.class);
            logger.info("Cargado archivo de propiedades del Logger log4j...Desde Log4jInit...File: "+file);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }
}
