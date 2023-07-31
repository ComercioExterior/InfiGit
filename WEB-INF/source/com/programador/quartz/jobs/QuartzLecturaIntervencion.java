package com.programador.quartz.jobs;

import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_operaciones_INTERVENCION.InventarioIntervencion;
import com.bdv.infi.logic.interfaz_operaciones_INTERVENCION.LecturaIntervencion;

public class QuartzLecturaIntervencion implements Job {

	private Logger logger = Logger.getLogger(QuartzLecturaIntervencion.class);

	public void execute(JobExecutionContext arg0){

		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
			String usuarioGenerico = parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES, dso);
			logger.debug("Entrando a ejecucion de tareas programadas inventario intervencion");
			System.out.println("***************PROGRAMADOR INVENTARIO INTERVENCION***************");

			LecturaIntervencion lectura = new LecturaIntervencion(dso, usuarioGenerico);
			Thread t = new Thread(lectura);
			t.start();
			t.join();

		} catch (Exception e) {
			logger.error("QuartzInventarioIntervencion : execute() " + e);
			System.out.println("QuartzInventarioIntervencion : execute() " + e);
			
		}
	}
}