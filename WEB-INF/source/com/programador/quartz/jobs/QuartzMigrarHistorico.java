package com.programador.quartz.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bdv.infi.dao.MigracionHistoricoDao;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de ejecutar el proceso de MIGRACION automaticamente, dependiendo de como este configurado en el SCHEDULER
 * @author jose bustamante
 */
public class QuartzMigrarHistorico implements Job{

	private int secuenciaProcesos = 0;
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	String tipoTransaccion = TransaccionNegocio.PROCESO_HIST_MENUDEO;
	DataSource dso;
		private Logger logger = Logger.getLogger(QuartzMigrarHistorico.class);
		
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
			try {
				//se envia la fecha de ejecucion del proceso
				SimpleDateFormat fechaFormat = new SimpleDateFormat("dd-MM-yyyy");
				String fecha = fechaFormat.format(new Date());
				
				// se carga el datasources 
			    dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
				//
			    logger.error("INICIO EL PROCESO DE MIGRACION MENUDEO");
			    iniciarProceso();
				MigracionHistoricoDao migrar = new MigracionHistoricoDao(dso);

				//se invoca dao para ejecutar procedimiento de BD
				migrar.migracionHistoricoDao(fecha);
				proceso.agregarDescripcionErrorTrunc("Se ha migrado de forma exitosa la tabla de menudeo", true);
				finalizarProceso();
				logger.error("TERMINO EL PROCESO DE MIGRACION MENUDEO");
			} catch (Exception e) {
				logger.error(e);
				proceso.agregarDescripcionErrorTrunc("Ha ocurrido un error al momento de migrar la informacion", true);
				throw new JobExecutionException(e);
			}
		}	
		
		
		
		protected void iniciarProceso() throws Exception {
			logger.info("INICIO DE PROCESO");
			procesosDAO = new ProcesosDAO(dso);
			proceso = new Proceso();
			secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
			proceso.setEjecucionId(secuenciaProcesos);
			proceso.setFechaInicio(new Date());
			proceso.setFechaValor(new Date());
			proceso.setTransaId(tipoTransaccion);
			proceso.setUsuarioId(226);
			String queryProceso = procesosDAO.insertar(proceso);
			db.exec(dso, queryProceso);
		}

		/**
		 * metodo para finalizar los proceso y asignar la fecha de cierre
		 * 
		 * @throws Exception
		 */
		private void finalizarProceso() throws Exception {
			String queryProcesoCerrar = procesosDAO.modificar(proceso);
			db.exec(dso, queryProcesoCerrar);
			logger.info("FIN DE PROCESO: " + new Date());
		}
}
