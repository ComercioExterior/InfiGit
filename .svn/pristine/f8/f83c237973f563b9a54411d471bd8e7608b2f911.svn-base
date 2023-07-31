package com.programador.quartz.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import models.bcv.mesas_cambios.EnvioOperaciones;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de ejecutar el proceso de ENVIO DE OPERACIONES MENUDEO, configurado en el PROGRAMADOR DE TAREAS
 * 
 * @author NM36635
 */
public class QuartzOperacionesMesa implements Job {
	private Logger logger = Logger.getLogger(QuartzOperacionesMesa.class);
	protected HashMap<String, String> parametrosRecepcionMENUDEO;
	String fecha1 = "";
	String fechaManual = "1";

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			Date objDate = new Date();
			String strDateFormat = "dd-MM-yyyy";
			SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
			this.fecha1 = objSDF.format(objDate);

			String statusP = "T";// son los filtros que emulo de el proceso anterior.Ejemplo:Todos(T),Original(O),Reversado(R)
			String statusE = "5";// son los filtros que emulo de el proceso anterior.Ejemplo:0(NO_ENVIADO),1(ENVIADO)
			String Tipo = "1111";// son los filtros que emulo de el proceso anterior.Ejemplo:1111(TODAS),1203(COMPRA),5204(VENTA)
			String idOrdenes = "";// son los filtros que emulo de el proceso anterior.Ejemplo es el numero de la operacion.
			EnvioOperaciones notificacion = new EnvioOperaciones(true, idOrdenes, statusP, dso, 1, fecha1, statusE, Tipo);
			Thread t = new Thread(notificacion);
			t.start();
			t.join();

		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}

}