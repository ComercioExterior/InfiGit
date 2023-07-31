package com.programador.quartz.jobs;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_ops.AdjudicacionRecepcionSubasta;

/**
 * Clase encargada de ejecutar el proceso de ADJUDICACION automaticamente, dependiendo de como este configurado en el SCHEDULER
 * @author elaucho
 */
public class QuartzAdjudicacionSubasta implements Job{
	private Logger logger = Logger.getLogger(QuartzAdjudicacionSubasta.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		try {
			logger.debug("Entrando a ejecucion de tareas programadas, respuesta de adjudicacion subasta");
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));

			Thread t = new Thread(new AdjudicacionRecepcionSubasta(dso));
			t.start();
			t.join();
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}
}
