package com.programador.quartz.jobs;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_varias.InterfaceCarmen;
/**
 * Clase encargada disparar el proceso de generación del archivo hacia la interfaz de carmen
 * @author nvisbal
 */
public class QuartzInterfaceCarmen implements Job{
	
	private Logger logger = Logger.getLogger(QuartzInterfaceCarmen.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		try {
			logger.debug("Entrando a ejecucion de tareas programadas, interfaz carmen");
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			Thread t = new Thread(new InterfaceCarmen(dso));
			t.start();
			t.join();
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}//fin execute	
}//fin clase