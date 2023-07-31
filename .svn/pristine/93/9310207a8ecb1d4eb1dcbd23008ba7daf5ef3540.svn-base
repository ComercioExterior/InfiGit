package models.intercambio.batch_adjudicacion.enviar_archivo.subasta;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ArchivosOpsDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ControlProcesosOps;
import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaLocal;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.EnvioArchivosOps;

public class Generar2 extends MSCModelExtend {
	private ControlProcesosOps controlProceso;	
	private String consultaRegistros;
	private int unidadInvId;
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		UsuarioDAO usu				= new UsuarioDAO(_dso);

		Runnable EnvioArchivoAdjudicacionSubasta= new EnvioArchivosOps(_dso,ParametrosSistema.INTERFACE_OPS,ParametrosSistema.INTERFACE_OPICS,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),consultaRegistros,controlProceso);
		new Thread(EnvioArchivoAdjudicacionSubasta).start();
	}
	
	public boolean isValid() throws Exception {
				
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		ArchivosOpsDAO archivosOpsDAO=new ArchivosOpsDAO(_dso);
		
		this.controlProceso=ControlProcesosOpsMonedaLocal.PROCESO_COBRO_SUBASTA_TITULO.getInstance();
		
		
		if(_record.getValue("undinv_id")!=null){
			unidadInvId=Integer.parseInt(_record.getValue("undinv_id"));			
		} else {
			_record.addError("Adjudicación Batch Subasta"," Debe seleccionar la unidad de inversion a procesar ");
			return false;
		}
		
		
		procesosDAO
		.listarProcesosActivosPorTransaccion(controlProceso.getListaProceso());
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Adjudicación Batch Subasta",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		} else {
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			controlArchivoDAO.listarCicloAbiertoPorTransaccion(controlProceso.getListaCiclo());
			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Adjudicación Batch Subasta",
						"No se puede procesar la solicitud porque el ciclo de envío y recepción "  + (dataSet.getValue("in_recepcion").equals("0")?"de adjudicación ":"de liquidación ") + " no ha finalizado. ");
				valido = false;
			}else{
				//NM29643 14/08/2014 Se valida que no se encuentre en ejecucion la liquidacion			
				procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.LIQUIDACION);
				
				if (procesosDAO.getDataSet().count() > 0) {
					_record.addError("Adjudicación Batch Sitme",
						" No se puede procesar la solicitud porque un proceso de liquidación "
						+"se encuentra en ejecución");
					valido = false;			
				}				
			}			
		}	
		
		consultaRegistros=archivosOpsDAO.getRegistrosSubastaOps(unidadInvId);//TODO consulta de los registros a enviar a la OPS
		
		return valido;		
	}
}
