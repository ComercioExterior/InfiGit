package com.programador.quartz.jobs;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.RecepcionOperacionesVerificadasDICOM;
//import com.bdv.infi.logic.interfaz_moneda_extranjera.AnulacionOperacionesConvenio36;

/**
 * Clase encargada de ejecutar el proceso de ANULACION OPERACIONES VENCIDAS CONVENIO 36, congigurado en el PROGRAMADOR DE TAREAS
 * @author NM26659
 */
public class QuartzRecepcionOperacionesDICOM implements Job{
	private Logger logger = Logger.getLogger(QuartzRecepcionOperacionesDICOM.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			logger.debug("Entrando a ejecucion de tareas programadas DICOM");
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			Thread t = new Thread(new RecepcionOperacionesVerificadasDICOM(dso));
			t.start();
			t.join();
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}
}