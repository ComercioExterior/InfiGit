package com.programador.quartz.jobs;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_varias.InterfaceBcv;
/**
 * Clase encargada disparar el proceso de generación del archivo hacia la interfaz de bcv
 * @author nvisbal
 */
public class QuartzInterfaceBcv implements Job{
	
	private Logger logger = Logger.getLogger(QuartzInterfaceBcv.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		try {
			logger.debug("Entrando a ejecucion de tareas programadas, interfaz bcv");
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			Thread t = new Thread(new InterfaceBcv(dso));
			t.start();
			t.join();
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}//fin execute	
}//fin clase