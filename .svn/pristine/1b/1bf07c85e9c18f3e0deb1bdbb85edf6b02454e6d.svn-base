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
import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaLocal;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_ops.RecepcionArchivoOps;

/**
 * Clase encargada de ejecutar el proceso de ADJUDICACION automaticamente, dependiendo de como este configurado en el SCHEDULER
 * @author elaucho
 */
public class QuartzOpsMonedaLocal implements Job{
	private Logger logger = Logger.getLogger(QuartzOpsMonedaLocal.class);
	private ControlProcesosOps controlProceso;	
	ControlArchivoDAO controlArchivoDAO;
	private DataSet _dataSet;	
	private String cicloEjecucion;
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {				
			
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));

				controlArchivoDAO= new ControlArchivoDAO(dso);
				controlArchivoDAO.listarCicloAbiertoPorTransaccion(ControlProcesosOpsMonedaLocal.getListaCiclosMonedaLocal());
				_dataSet=controlArchivoDAO.getDataSet();
				System.out.println(_dataSet);
				if(_dataSet!=null && _dataSet.count()!=0){//Si existe un ciclo abierto
					_dataSet.first();
					_dataSet.next();
					cicloEjecucion=_dataSet.getValue("status");
					if(cicloEjecucion.equals(ControlProcesosOpsMonedaLocal.CICLO_BATCH_SUBASTA)) {
						this.controlProceso=ControlProcesosOpsMonedaLocal.PROCESO_COBRO_SUBASTA_TITULO.getInstance();		
					} else if(cicloEjecucion.equals(ControlProcesosOpsMonedaLocal.CICLO_BATCH_COBRO_DICOM_DEMANDA)) {
						this.controlProceso=ControlProcesosOpsMonedaLocal.PROCESO_DEMANDA_COBRO_DICOM.getInstance();
					} else if(cicloEjecucion.equals(ControlProcesosOpsMonedaLocal.CICLO_BATCH_COBRO_COMISION_DICOM_OFERTA)) {
						this.controlProceso=ControlProcesosOpsMonedaLocal.PROCESO_DEMANDA_COBRO_COMISION_DICOM.getInstance();
					} else if(cicloEjecucion.equals(ControlProcesosOpsMonedaLocal.CICLO_BATCH_ABONO_DICOM_OFERTA)) {
						this.controlProceso=ControlProcesosOpsMonedaLocal.PROCESO_OFERTA_ABONO_DICOM.getInstance();
					}
					
					RecepcionArchivoOps recepcionArchivoOps = new RecepcionArchivoOps(dso,ParametrosSistema.INTERFACE_OPS,ParametrosSistema.INTERFACE_OPICS,controlProceso);
					Thread t = new Thread(recepcionArchivoOps);
					t.start();
					t.join();
					
				} else {
					logger.debug("Planificador de Ejecucion de Moneda Local: No se puede ejecutar, NO EXISTE UN CICLO DE MONEDA LOCAL ABIERTO --> ");
				}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}
}
