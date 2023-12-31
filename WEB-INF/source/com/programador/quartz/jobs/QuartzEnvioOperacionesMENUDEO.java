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
import org.quartz.JobExecutionException;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_operaciones_MENUDEO.EnvioOperacionesPreaprobadasMENUDEO;

/**
 * Clase encargada de ejecutar el proceso de ENVIO DE OPERACIONES MENUDEO, configurado en el PROGRAMADOR DE TAREAS
 * 
 * @author NM36635
 */
public class QuartzEnvioOperacionesMENUDEO implements Job {
	private Logger logger = Logger.getLogger(QuartzEnvioOperacionesMENUDEO.class);
	protected HashMap<String, String> parametrosRecepcionMENUDEO;
	String fecha1="";
	String fechaManual="1";
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			obtenerParametros(dso);

			
			Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate

			
			String strDateFormat = "dd-MM-yyyy"; // El formato de fecha est� especificado
			SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto
			
			this.fecha1=objSDF.format(objDate);
			
			String fechaManualActivar= parametrosRecepcionMENUDEO.get(ParametrosSistema.ACTIVAR_FECHA_MANUAL);
			
			if (fechaManualActivar.equalsIgnoreCase(fechaManual.trim())) {
				
				this.fecha1 =parametrosRecepcionMENUDEO.get(ParametrosSistema.FECHA_VALOR_MENUDEO);
				
			}
			
			
			String statusP = "T";//son los filtros que emulo de el proceso anterior.Ejemplo:Todos(T),Original(O),Reversado(R)
			String statusE = "6";//son los filtros que emulo de el proceso anterior.Ejemplo:0(NO_ENVIADO),1(ENVIADO)
			String Tipo = "1111";//son los filtros que emulo de el proceso anterior.Ejemplo:1111(TODAS),1203(COMPRA),5204(VENTA)
			String combustible = "0";//son los filtros que emulo de el proceso anterior.Ejemplo:0(NO COMBUSTIBLE),1(COMBUSTIBLE)

			String idOrdenes  = "todos";//son los filtros que emulo de el proceso anterior.Ejemplo es el numero de la operacion.
			logger.info("fecha a enviar : "+fecha1);
			logger.debug("Entrando a ejecucion de tareas programadas Envio Operaciones MENUDEO");
			EnvioOperacionesPreaprobadasMENUDEO envioBCVWSMenudeo = new EnvioOperacionesPreaprobadasMENUDEO(idOrdenes,true,statusP,dso, 1, fecha1,statusE,Tipo,combustible);
			Thread t = new Thread(envioBCVWSMenudeo);
			t.start();
			t.join();

		} catch (Exception e) {
			logger.error(e);
			throw new JobExecutionException(e);
		}
	}

	
	 protected void obtenerParametros(DataSource _dso) throws Exception {
	 ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
	 parametrosRecepcionMENUDEO=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);
	 }
}