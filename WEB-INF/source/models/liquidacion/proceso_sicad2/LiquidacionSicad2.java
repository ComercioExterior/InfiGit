package models.liquidacion.proceso_sicad2;


import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.LiquidacionUnidadInversionSicad2;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de liquidar ordenes de tipo SICADII
 * @author NM25287
 */
public class LiquidacionSicad2 extends MSCModelExtend {

	//NM29643 - infi_TTS_466 13/08/2014
	String tipoProducto;
	
	@Override
	public void execute() throws Exception {
		
		try {
			UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);
			int usuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
			String ip=_req.getRemoteAddr();
			String sucursal=_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL) == null? "" : _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL).toString();
			String[] unidadesDeInversion = _req.getParameterValues("unidades");
			//NM29643 - infi_TTS_466 13/08/2014
//			String tipoProducto=_record.getValue("tipo_producto");
			
			LiquidacionUnidadInversionSicad2 liquidacionUnidadInversion = new LiquidacionUnidadInversionSicad2(_dso,usuario,ip,sucursal,unidadesDeInversion,getUserName(),tipoProducto);
			Thread t = new Thread(liquidacionUnidadInversion);
			t.start();
	
		} catch (Throwable e) {			
			Logger.error(this,e.getMessage(),e);
		}
	}//FIN EXECUTE
	
	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception {
		
		boolean valido = super.isValid();
		
		if(valido){
			
			//NM29643 - infi_TTS_466 13/08/2014 Se valida que no exista un ciclo o proceso de cobro de adjudicacion en ejecucion
			tipoProducto = _record.getValue("tipo_producto");
			String transaccionNegocio="";
			//NM29643 - 07/01/2015 Se descomenta validacion de ciclo de cobro de adjudicacion en ejecucion
			String cicloEnvio="";
			
			if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
				transaccionNegocio = TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_ENVIO;
				cicloEnvio =  "'"+TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL+"'";
			}
			else if (tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
				transaccionNegocio = TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_ENVIO;
				cicloEnvio = "'"+TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL+"'";
			}
			
			ControlArchivoDAO controlArchivoDAO 	= new ControlArchivoDAO(_dso);
			controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(cicloEnvio);
			DataSet dataset = controlArchivoDAO.getDataSet();

			if(dataset.count() > 0 ) {
				_record.addError("Liquidación",
				"No se puede procesar la solicitud porque existe un ciclo de cobro de adjudicación en ejecución para este tipo de producto");
				valido = false;
			}else{
				ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
				
				procesosDAO.listarPorTransaccionActiva(transaccionNegocio);

				if (procesosDAO.getDataSet().count() > 0) {
					_record.addError("Liquidación",
					"No se puede procesar la solicitud porque existe un proceso de cobro de adjudicación en ejecución para este tipo de producto");
					valido = false;
				}else{
					procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.LIQUIDACION);
					
					if (procesosDAO.getDataSet().count() > 0) {
						_record.addError("Liquidación",
										"No se puede procesar la solicitud porque otra "
										+"persona realizó esta acción y esta actualmente activa");
						valido = false;
					}
				}
			}
		}
		
		return valido;
	}// fin isValid
}//FIN CLASE
