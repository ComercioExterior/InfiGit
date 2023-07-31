package com.programador.quartz.jobs;

import models.liquidacion.proceso_blotter.LiquidacionScheduler;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de ejecutar el proceso de LIQUIDACION automaticamente, dependiendo de como este configurado en el SCHEDULER
 * @author elaucho
 */
public class QuartzGenerarLiquidacion implements Job{

	private Logger logger = Logger.getLogger(QuartzGenerarLiquidacion.class);
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		
		LiquidacionScheduler liquidacionScheduler = new LiquidacionScheduler();
		try {
			liquidacionScheduler.execute();
		} catch (Exception e) {
			
			try {
        		logger.error(e.getMessage(),e);
			} catch (Exception e1) {
				e.printStackTrace();
			}
			throw new JobExecutionException(e);
		}
	}//fin execute
}//fin QuartzGenerarLiquidacion