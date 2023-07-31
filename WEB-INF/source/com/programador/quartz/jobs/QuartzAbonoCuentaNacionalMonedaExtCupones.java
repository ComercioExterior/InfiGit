package com.programador.quartz.jobs;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_ops.AbonoRecepcionCuentaNacionalMonedaExtranjeraPagoCupones;


public class QuartzAbonoCuentaNacionalMonedaExtCupones implements Job {
	
	private Logger logger = Logger.getLogger(QuartzAbonoCuentaNacionalMonedaExtCupones.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			logger.debug("Entrando a ejecucion de tareas programadas abono cuenta nacional moneda extranjera Pago Cupones");
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			Thread t = new Thread(new AbonoRecepcionCuentaNacionalMonedaExtranjeraPagoCupones(dso));
			t.start();
			t.join();
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	
		
	}

}
