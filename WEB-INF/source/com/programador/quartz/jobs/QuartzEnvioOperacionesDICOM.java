package com.programador.quartz.jobs;

import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.EnvioOperacionesPreaprobadasDICOM;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de ejecutar el proceso de ENVIO DE OPERACIONES PREAPROBADAS DICOM, configurado en el PROGRAMADOR DE TAREAS
 * @author NM25287
 */
public class QuartzEnvioOperacionesDICOM implements Job{
	private Logger logger = Logger.getLogger(QuartzEnvioOperacionesDICOM.class);
	protected HashMap<String, String> parametrosRecepcionDICOM;
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String jornadaDicom="";
		String jornadafechaInicio="";
		String jornadafechaFin="";	
		Date fechaActual=new Date();
		Date fechaInicioDate=null;
		Date fechaFinDate=null;
		String modenaSubastaDicom="";
		try {
			//FORMATEAR FECHA ACTUAL SIN MINUTOS Y SEGUNDOS			
			try {
				System.out.println("paso 1");
				fechaActual=Utilitario.fechaDateFormateada(new Date(), ConstantesGenerales.FORMATO_FECHA2);
			} catch (Exception e) {
				logger.debug("Error en formato de fecha actual, se usará new Date()");
			}
			
			//VALIDAR Y ACTIVAR HILO
			
			logger.debug("Entrando a ejecucion de tareas programadas Envio  Operaciones DICOM INTERBANCARIO");
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
	    	ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
	    	String usuarioGenerico=parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES,dso);
	    	System.out.println("paso 2");
	    	obtenerParametros(dso);
	    	System.out.println("paso 3");
			//VALIDAR JORNADA, FECHA INICIO Y FECHA FIN
			jornadaDicom = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_DICOM);
			jornadafechaInicio = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_INICIO);
			jornadafechaFin = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_FIN);
			modenaSubastaDicom=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
			/*if(Utilitario.longitudValida(jornadaDicom, 1, EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor())&&
					Utilitario.longitudValida(jornadafechaInicio, 10, 10)&&
							Utilitario.longitudValida(jornadafechaFin, 10, 10) && 
							(modenaSubastaDicom!=null && !modenaSubastaDicom.equals(""))){*/
			System.out.println("paso 4");	
			//VALIDAR FECHA
			fechaInicioDate=Utilitario.StringToDate(jornadafechaInicio, ConstantesGenerales.FORMATO_FECHA2);
			fechaFinDate=Utilitario.StringToDate(jornadafechaFin, ConstantesGenerales.FORMATO_FECHA2);
				
			//if(Utilitario.betweenDates(fechaInicioDate, fechaFinDate, fechaActual)){
			//ACTIVAR HILO DE PROCESAMIENTO DE OPERACIONES PREAPROBADAS DICOM
			System.out.println("paso 5");
			Thread t = new Thread(new EnvioOperacionesPreaprobadasDICOM(dso,usuarioGenerico));
			t.start();
			t.join();
				/*}else{
					logger.info("EnvioOperacionesDICOM-> La Jornada no se encuentra activa en la fecha configurada");
				}*/			
			
			/*}else
			{
				logger.info("EnvioOperacionesDICOM-> Los parametros JORNADA_DICOM,JORNADA_FECHA_INICIO,JORNADA_FECHA_FIN,MONEDA_SUBASTA_DICOM no estan bien configurados");
			}*/
		}catch(Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}
	
	/**
	 * Metodo de busqueda de parametros asociados a interfaz DICOM
	 * */
	protected void obtenerParametros(DataSource _dso) throws Exception {		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
	}	
}