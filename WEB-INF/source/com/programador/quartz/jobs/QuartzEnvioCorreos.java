package com.programador.quartz.jobs;

import java.net.InetAddress;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreosCall;

/**
 * Clase encargada de ejecutar el proceso de Envio de Correos, dependiendo de como este configurado en el SCHEDULER
 * @author Dayana Torres
 */
public class QuartzEnvioCorreos implements Job{
private Logger logger = Logger.getLogger(QuartzLiquidacionSubasta.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			logger.debug("Entrando a ejecucion de tareas programadas envio de correos");
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
	    	
	    	/** Colocar esto DESDE AQUI para realizar la llamada a EnvioCorreos()**/
	    	InetAddress direccion = InetAddress.getLocalHost();
	    	String direccionIpstr = direccion.getHostAddress();
	    	//System.out.println("Direccion IP: "+direccionIpstr);
	    	//System.out.println("_app en Job EnvioCorreos: "+this._app);
	    	EnvioCorreosCall ecCall = new EnvioCorreosCall();
	    	EnvioCorreos ec = new EnvioCorreos(dso, null, direccionIpstr); //Como es un Job no se tiene el ServletContext (se pasa null)
	    	//EnvioCorreos ec = new EnvioCorreos(dso, this._app, direccionIpstr);
	    	ec.initParamEnvio();
//	    	ecCall.execute(ec.parametros.get(ParametrosSistema.DEST_CLIENTE), "", ec, 0);
            /** Colocar esto HASTA AQUI para realizar la llamada a EnvioCorreos()**/
			
		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}
	
}