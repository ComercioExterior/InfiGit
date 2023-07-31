package com.programador.quartz.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_conciliacion_MENUDEO_BCV.ConciliacionMenudeo;

/**
 * Job para el manejo de la conciliacion menudeo
 * 
 * @author 24661
 * 
 */
public class QuartzConciliacionMenudeoBCV implements Job {

	private Logger logger = Logger.getLogger(QuartzConciliacionMenudeoBCV.class);
	protected HashMap<String, String> parametrosConciliacion;
	String fecha = "";

	public void execute(JobExecutionContext arg0) {

		Date fechaSistema = new Date();
		System.out.println("fechaSistema : " + fechaSistema);
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FECHA_MANUAL_CONCILIACION);
		this.fecha = formato.format(fechaSistema);
		System.out.println("fechaSistema formato: " + this.fecha);
		try {

			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			obtenerParametros(dso);
			String fechaManualActivar = parametrosConciliacion.get(ParametrosSistema.ACTIVAR_FECHA_MANUAL);

			if (fechaManualActivar.equalsIgnoreCase(ConstantesGenerales.FECHA_MANUAL_CONCILIACION)) {
				this.fecha = parametrosConciliacion.get(ParametrosSistema.FECHA_VALOR_MENUDEO);

			}

			logger.info("**Programador Conciliacion**  : Fechas a enviar :" + fecha);
			System.out.println("**Programador Conciliacion**  : Fechas a enviar :" + fecha);

			ConciliacionMenudeo conciliacion = new ConciliacionMenudeo(dso, 1, fecha,1);
			Thread t = new Thread(conciliacion);
			t.start();
			t.join();

		} catch (Exception e) {
			System.out.println("QuartzConciliacionMENUDEO : execute()" + e);
			logger.error("QuartzConciliacionMENUDEO : execute()" + e);

		}
	}

	/**
	 * Metodo de busqueda de parametros asociados a interfaz Menudeo
	 * 
	 * @param _dso
	 * @throws Exception
	 */
	protected void obtenerParametros(DataSource _dso) {

		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		try {
			parametrosConciliacion = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);

		} catch (Exception e) {
			System.out.println("QuartzConciliacionMENUDEO : obtenerParametros()" + e);
			logger.error("QuartzConciliacionMENUDEO : obtenerParametros()" + e);
			
		}

	}
}