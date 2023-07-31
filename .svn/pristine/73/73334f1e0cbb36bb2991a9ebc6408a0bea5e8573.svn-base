package models.intercambio.batch_adjudicacion.enviar_archivo.sitme;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.AdjudicacionEnvioSitme;

public class Generar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		UsuarioDAO usu				= new UsuarioDAO(_dso);		
		Runnable adjudicacionEnvio= new AdjudicacionEnvioSitme(_dso,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),Integer.parseInt(_record.getValue("undinv_id")));
		new Thread(adjudicacionEnvio).start();
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.PROC_BATCH_ADJ_SITME_ENVIO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Adjudicación Batch Sitme",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}else{
			//Verifica si se espera una recepción de archivo
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			controlArchivoDAO.listarEnvioPorRecepcionBatch("'"+TransaccionNegocio.CICLO_BATCH_SITME+"'");
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				 dataSet.next(); 
				_record
				.addError(
						"Adjudicación Batch Sitme",
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
		return valido;
	}// fin isValid
}
