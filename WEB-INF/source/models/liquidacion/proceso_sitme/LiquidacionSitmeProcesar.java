package models.liquidacion.proceso_sitme;


import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.LiquidacionUnidadInversionSitme;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de liquidar todas las ordenes de tipo sitme
 * @author nvisbal.
 */
public class LiquidacionSitmeProcesar extends MSCModelExtend {

	@Override
	public void execute() throws Exception {
		
		try {
			UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);
			int usuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
			
			//Se verifica si la unidad de inversion es de tipo inventario
			String[] unidadesDeInversion = _req.getParameterValues("unidades");
			LiquidacionUnidadInversionSitme liquidacionUnidadInversion = new LiquidacionUnidadInversionSitme(_dso,usuario,_app,_req,unidadesDeInversion,getUserName());
			Thread t = new Thread(liquidacionUnidadInversion);
			t.start();
	
		} catch (Throwable e) {			
			Logger.error(this,e.getMessage(),e);
		}finally{
			_req.getSession().removeAttribute("opics_data");
			_req.getSession().removeAttribute("ordenes");
			_req.getSession().removeAttribute("unidad");
			_req.getSession().removeAttribute("blotter");
			_req.getSession().removeAttribute("status");
			_req.getSession().removeAttribute("nombre_unidad");			
		}//fin finally
	}//FIN EXECUTE
	
	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception {
		
		boolean valido = super.isValid();
		
		if(valido){
			
			//NM29643 14/08/2014 Se valida que no exista ciclo ni proceso en ejecucion para el tipo de producto
			String transaccionNegocio = "";
//			String cicloEnvio = "";
			transaccionNegocio = TransaccionNegocio.PROC_BATCH_ADJ_SITME_ENVIO;
//			cicloEnvio = TransaccionNegocio.CICLO_BATCH_SITME;
			ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
			
//			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
//			controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(cicloEnvio);
//			DataSet dataset = controlArchivoDAO.getDataSet();
//
//			if(dataset.count() > 0 ) {
//				_record.addError("Liquidación",
//				"No se puede procesar la solicitud porque existe un ciclo de cobro de adjudicación en ejecución para este tipo de producto");
//				valido = false;
//			}else{
				
				procesosDAO.listarPorTransaccionActiva(transaccionNegocio);

				if (procesosDAO.getDataSet().count() > 0) {
					_record.addError("Liquidación",
					"No se puede procesar la solicitud porque existe un proceso de cobro de adjudicación en ejecución para este tipo de producto");
					valido = false;
				}else{
					procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.LIQUIDACION);
					
					if (procesosDAO.getDataSet().count() > 0) {
						_record.addError("Liquidación",
										" No se puede procesar la solicitud porque otra "
										+"persona realizó esta acción y esta actualmente activa");
						valido = false;
					}
				}
//			}
		}
		
		return valido;
	}// fin isValid
}//FIN CLASE
