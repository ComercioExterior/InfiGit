package com.programador.quartz.jobs;

import java.net.InetAddress;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.cierre_sistema.CierreSistema;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de ejecutar el proceso de LIQUIDACION automaticamente, dependiendo de como este configurado en el SCHEDULER
 * @author elaucho
 */
public class QuartzCierreSistema implements Job{
	private Logger logger = Logger.getLogger(QuartzCierreSistema.class);
	private String TIPO_PETICION_AUDITORIA;
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			logger.debug("Entrando a ejecucion de tareas programadas cierre de sistema");
			InetAddress direccion = InetAddress.getLocalHost();
            String direccionIpstr = direccion.getHostAddress();
            TIPO_PETICION_AUDITORIA="Inicio Automático del Proceso de Cierre del Sistema. Tarea Programada";
 		
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			Thread t = new Thread(new CierreSistema(dso, direccionIpstr, null, TIPO_PETICION_AUDITORIA,ConstantesGenerales.TIPO_CIERRE_AUTOMATICO));//EL usuario es null por ser una tarea automática del sistema
			t.start();
			t.join();
			
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}
}