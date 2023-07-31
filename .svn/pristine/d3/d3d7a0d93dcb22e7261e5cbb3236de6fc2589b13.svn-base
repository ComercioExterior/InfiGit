package com.programador.quartz.jobs;

import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ControlProcesosOps;
import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaExtranjera;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_ops.RecepcionArchivoOps;

/**
 * Clase encargada de ejecutar el proceso de ADJUDICACION automaticamente, dependiendo de como este configurado en el SCHEDULER
 * @author elaucho
 */
public class QuartzOpsMonedaExtranjera implements Job{
	private Logger logger = Logger.getLogger(QuartzOpsMonedaExtranjera.class);
	private ControlProcesosOps controlProceso;	
	ControlArchivoDAO controlArchivoDAO;
	private DataSet _dataSet;	
	private String cicloEjecucion;
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {						
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
				controlArchivoDAO= new ControlArchivoDAO(dso);
				controlArchivoDAO.listarCicloAbiertoPorTransaccion(ControlProcesosOpsMonedaExtranjera.getListaCiclosMonedaExtranjera());
				_dataSet=controlArchivoDAO.getDataSet();
				if(_dataSet!=null && _dataSet.count()!=0){//Si existe un ciclo abierto
					_dataSet.first();
					_dataSet.next();
					cicloEjecucion=_dataSet.getValue("STATUS");
					if(cicloEjecucion.equals(ControlProcesosOpsMonedaExtranjera.CICLO_BATCH_ABONO_DICOM_DEMANDA)){
						this.controlProceso=ControlProcesosOpsMonedaExtranjera.PROCESO_DEMANDA_ABONO_DICOM.getInstance();		
					}else if(cicloEjecucion.equals(ControlProcesosOpsMonedaExtranjera.CICLO_BATCH_COBRO_DICOM_OFERTA)){
						this.controlProceso=ControlProcesosOpsMonedaExtranjera.PROCESO_OFERTA_COBRO_DICOM.getInstance();
					}
					RecepcionArchivoOps recepcionArchivoOps = new RecepcionArchivoOps(dso,ParametrosSistema.INTERFACE_OPS,ParametrosSistema.INTERFACE_OPICS,controlProceso);
					Thread t = new Thread(recepcionArchivoOps);
					t.start();
					t.join();
				} else {
					logger.debug("Planificador de Ejecucion de Moneda Extranjera: No se puede ejecutar, NO EXISTE UN CICLO DE MONEDA EXTRANJERA  ABIERTO --> ");
				}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}
}
