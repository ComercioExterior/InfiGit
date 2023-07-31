package models.intercambio.batch_liquidacion.enviar_archivo.subasta;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.LiquidacionEnvioSubasta;

public class Generar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		UsuarioDAO usu				= new UsuarioDAO(_dso);
		Runnable liquidacionEnvio= new LiquidacionEnvioSubasta(_dso,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),Integer.parseInt(_record.getValue("undinv_id")),TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO,TransaccionNegocio.CICLO_BATCH_SUBASTA);
		new Thread(liquidacionEnvio).start();
	}

	public boolean isValid() throws Exception {
		
		String ciclosValidar="'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS+"'".concat(",").concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA+"'".concat(",")).concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL+"'");
		String procesoValidar="'"+TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO+"'".concat(",").concat("'"+TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_ENVIO+"'");
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO
				.listarPorTransaccionActivaSubastaDivisasSubastaTitulo(procesoValidar);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Liquidación Batch Subasta",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}else{
			//Verifica si se espera una recepción de archivo
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			controlArchivoDAO.listarEnvioPorRecepcionBatch(ciclosValidar);			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Liquidación Batch Subasta",
						"No se puede procesar la solicitud porque el ciclo de envío y recepción "  + (dataSet.getValue("in_recepcion").equals("0")?"de adjudicación ":"de liquidación ") + " no ha finalizado. ");
				valido = false;
			}
		}
		return valido;
	}// fin isValid
}
