package com.programador.quartz.jobs;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_ops.ConciliacionRetencionRecepcion;

/**
 * Clase encargada de ejecutar el proceso de CONCILIACION DE RETENCIONES, congigurado en el PROGRAMADOR DE TAREAS
 * @author NM25287
 */
public class QuartzConciliacionRetencion implements Job{
	private Logger logger = Logger.getLogger(QuartzConciliacionRetencion.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			logger.debug("Entrando a ejecucion de tareas programadas Conciliacion Retencion");
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			Thread t = new Thread(new ConciliacionRetencionRecepcion(dso));
			t.start();
			t.join();
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}
}