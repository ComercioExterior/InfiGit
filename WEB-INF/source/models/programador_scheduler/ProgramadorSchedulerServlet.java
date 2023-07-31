package models.programador_scheduler;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.bdv.infi.util.Utilitario;

/**
 * Servlet que inicia el Scheduler con las tareas previamente configuradas
 * @author elaucho
 */
public class ProgramadorSchedulerServlet implements Servlet{

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
		
		Logger logger = Logger.getLogger(ProgramadorSchedulerServlet.class);
		
		try {
			
			//Instancia de clase para inicializar Scheduler
			ProgramadorSchedulerUpdate programadorSchedulerUpdate = new ProgramadorSchedulerUpdate();			
			programadorSchedulerUpdate.inicializarScheduler();
			
			//programadorSchedulerUpdate.inicializarScheduler();
						
			logger.info("Se inicializa Scheduler desde ProgramadorSchedulerServlet...");
			
		} catch (Exception e) {
			
			try {
				logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
			} catch (Exception e1) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void service(ServletRequest arg0, ServletResponse arg1) throws IOException, ServletException {
	
	}
}
