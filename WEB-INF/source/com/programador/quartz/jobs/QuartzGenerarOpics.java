package com.programador.quartz.jobs;

import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.transform.GeneracionArchivoOpics;
import com.bdv.infi.util.Utilitario;
/**
 * Clase Quartz para la generacion automatica de Datos Opics.
 * Invoca el componente que busca en la tabla 705 los deal, genera el archivo .csv y lo envia vía FTP.
 * De ser exitoso el envio, los deal quedan en status enviado = 1.
 * @author elaucho
 */
public class QuartzGenerarOpics implements Job {

	private Logger logger = Logger.getLogger(QuartzGenerarOpics.class);
	private static final long serialVersionUID = 1L;
	
	/**
	 * Metodo para ejecutar la tarea programada
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("Se inicia el proceso de Generación automatico de datos Opics...");
        try {

        	//Se busca el DataSource
        	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			//Buscamos los id de los vehiculos asociados a los deal que no hayan sido enviados a OPICS
			MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(dso);
			String vehiculos[] = mensajeOpicsDAO.listarMensajesPorVehiculo();
			
			//Objeto que contiene la logica para el envio del archivo
			GeneracionArchivoOpics generacionArchivoOpics = new GeneracionArchivoOpics(dso,vehiculos);
			generacionArchivoOpics.generarArchivoOpics();

		} catch (Exception e) {

			try {
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			} catch (Exception e1) {
				e.printStackTrace();
			}
			
		}//FIN 
    }//FIN EXECUTE
}//FIN CLASE

