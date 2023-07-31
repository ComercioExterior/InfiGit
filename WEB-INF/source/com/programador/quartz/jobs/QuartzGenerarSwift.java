package com.programador.quartz.jobs;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_swift.FactorySwift;
import com.bdv.infi.logic.interfaz_swift.FactorySwiftSitme;
import com.bdv.infi.util.Utilitario;
/**
 * Clase encargada de armar las ordenes con sus respectivas operaciones para ser enviadas hacia <b>SWIFT</b><br>
 * Se inicia de forma automatica a traves del Scheduler de <b>QUARTZ</b><br>
 * Debe estar previamente configurado en el programador de tareas de <b>INFI<b><br>
 * @author elaucho
 */
public class QuartzGenerarSwift implements Job{
	/**
	 * DAO a Utilizar
	 */	
		OperacionDAO operacionDAO = null;
		
		private Logger logger = Logger.getLogger(QuartzGenerarSwift.class);
	
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		try {
			DataSource _dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			ServletContext _app = null;
			FactorySwift swift = new FactorySwift(_dso,_app);
			FactorySwiftSitme swiftSitme = new FactorySwiftSitme(_dso,_app);
			operacionDAO 		= new OperacionDAO(_dso);
			logger.info("Se inicia proceso automatico de envio de archivos SWIFT");
			
			//Se buscan las ordenes SUBASTA que se deban ser enviadas hasta el día actual	
			operacionDAO 		= new OperacionDAO(_dso);
			/*try {		
				operacionDAO.listarMensajesSwift(null, new Date(), 0, ConstantesGenerales.STATUS_EN_ESPERA, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA,null);
				swift.procesarOperacionesSwift(operacionDAO.getDataSet(), _dso);
			} catch (Exception e) {
				logger.info("Ha ocurrido una falla en el procesamiento de mensajes SWIFT SUBASTA: "+e.getMessage());
			}			*/
			//Se buscan las ordenes SITME que se deban ser enviadas hasta el día actual	
			try {	
				operacionDAO.listarMensajesSwiftInstruccion202(null, new Date(), 0, ConstantesGenerales.STATUS_EN_ESPERA, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA,null);				
				swiftSitme.procesarOperacionesSwift(operacionDAO.getDataSet(), _dso);
				
				operacionDAO.listarMensajesSwiftInstruccion202(null, new Date(), 0, ConstantesGenerales.STATUS_EN_ESPERA, ConstantesGenerales.ID_TIPO_PRODUCTO_SITME,null);				
				swiftSitme.procesarOperacionesSwift(operacionDAO.getDataSet(), _dso);
			} catch (Exception e) {
				logger.info("Ha ocurrido una falla en el procesamiento de mensajes SWIFT SITME: "+e.getMessage());
			}			
			
		}//fin try
		catch (Exception e) {
			try {
				logger.error(e.getMessage(),e);
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}//fin catch
	}//fin execute
	
}//fin clase