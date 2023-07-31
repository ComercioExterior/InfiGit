package com.programador.quartz.jobs;

import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_tasas_BCV.ConsultaTasasBCV;

/**
 * Clase encargada de ejecutar el proceso de ENVIO DE OPERACIONES MENUDEO, configurado en el PROGRAMADOR DE TAREAS
 * 
 * @author NM36635
 */
public class QuartzGuardarTasasBCV implements Job {
	private Logger logger = Logger.getLogger(QuartzGuardarTasasBCV.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));

			logger.debug("Entrando a ejecucion de tareas programadas Lectura de tasa bcv");
			System.out.println("***************PROGRAMADOR TASA BCV***************");
			
			ConsultaTasasBCV LecturaTasabcv = new ConsultaTasasBCV(dso, "1");
			Thread t = new Thread(LecturaTasabcv);
			t.start();
			t.join();

		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}
}