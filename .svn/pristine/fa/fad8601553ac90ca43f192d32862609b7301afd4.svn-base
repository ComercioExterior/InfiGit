package com.programador.quartz.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import models.bcv.intervencion.EnvioBCVWSIntervencion;
import models.msc_utilitys.MSCModelExtend;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class QuartzEnvioOperacionesINTERVENCION extends MSCModelExtend implements Job {

	private Logger logger = Logger.getLogger(QuartzEnvioOperacionesINTERVENCION.class);
	protected HashMap<String, String> parametrosIntervencion;
	String fechaManual = "1";
	String fecha = "";

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		try {

			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			obtenerParametros(dso);

			String statusE = "0";// son los filtros que emulo de el proceso anterior.Ejemplo:0(NO_ENVIADO),1(ENVIADO)
			String idOrdenes = "todos";// son los filtros que emulo de el proceso anterior.Ejemplo es el numero de la operacion.
			this.fecha = obtenerFecha();

			System.out.println("fecha: " + fecha);
			logger.debug("******************* PROGRAMADOR INTERVENCION *******************");
			System.out.println("******************* PROGRAMADOR INTERVENCION *******************");

			UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
//			int idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
			try {
				EnvioBCVWSIntervencion envioBCVWSIntervencion = new EnvioBCVWSIntervencion(idOrdenes, true, dso, 1, fecha, statusE);
				Thread t = new Thread(envioBCVWSIntervencion);
				t.start();
			} catch (Exception e) {

				logger.error("Procesar : execute()" + e);
				System.out.println("Procesar : execute() " + e);
			}

		} catch (Exception e) {
			logger.error(e);
			System.out.println("QuartzEnvioOperacionesINTERVENCION : execute()" + e);
			throw new JobExecutionException(e);
		}
	}

	protected void obtenerParametros(DataSource _dso) {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);

		try {
			parametrosIntervencion = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_INTERVENCION_BANCARIA);
		} catch (Exception e) {
			System.out.println("QuartzEnvioOperacionesINTERVENCION : obtenerParametros()" + e);
		}
	}

	private String obtenerFecha() {

		Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
		String strDateFormat = "dd-MM-yyyy"; // El formato de fecha está especificado
		SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto
		this.fecha = objSDF.format(objDate);
		String fechaManualActivar = parametrosIntervencion.get(ParametrosSistema.ACTIVAR_FECHA_MANUAL);

		if (fechaManualActivar.equalsIgnoreCase(fechaManual.trim())) {
			this.fecha = parametrosIntervencion.get(ParametrosSistema.FECHA_VALOR_INTERVENCION);
		}

		return fecha;
	}

}