package models.intercambio.batch_adjudicacion.enviar_archivo.conciliacion_sicad_II;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.ConciliacionRetencionEnvio;


public class Generar extends MSCModelExtend {
	
	private String idUnidadInv;
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		idUnidadInv=_req.getParameter("unidad_inv");
		
		UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);		
		int usuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));	
		ConciliacionRetencionEnvio conciliacionRetencionEnvio = new ConciliacionRetencionEnvio(_dso,usuario, getUserName(),Integer.parseInt(idUnidadInv));
		Thread t = new Thread(conciliacionRetencionEnvio);
		t.start();

	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		
		String ciclosValidar="'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS+"'".concat(",").concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA+"'".concat(",")).concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL+"'").concat(",'"+TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL+"'").concat(",'"+TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL+"'").concat(",'"+TransaccionNegocio.CICLO_BATCH_CONCILIACION_RETENCION+"'");
		String procesoValidar="'"+TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_ENVIO+"'".concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_CONCILIACION_RETENCION_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO+"'".concat(",'"+TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_CLAVENET_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_RED_COMERCIAL_ENVIO+"'"));
		
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO
				.listarPorTransaccionActivaSubastaDivisasSubastaTitulo(procesoValidar);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Conciliacion Operaciones Batch SICAD 2",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}else{
			//Verifica si se espera una recepción de archivo
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			
			controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(ciclosValidar);			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Conciliacion Operaciones Batch SICAD 2",
						"No se puede procesar la solicitud porque el ciclo de envío y recepción no ha finalizado. ");
				valido = false;
			}
		}
		return valido;
	}// fin isValid
}
