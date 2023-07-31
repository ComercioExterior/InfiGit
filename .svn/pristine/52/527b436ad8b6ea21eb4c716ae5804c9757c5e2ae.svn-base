package models.liquidacion.proceso_blotter;


import java.sql.CallableStatement;
import java.sql.Connection;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.LiquidacionTipoInventario;
import com.bdv.infi.logic.LiquidacionUnidadInversion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de liquidar todas las ordenes, creando una sola operacion de Credito para ser enviada a ALTAIR.
 * Ingresa los titulos a custodia y liquida la unidad de inversión en caso de ser exitosa la Operacion contra ALTAIR
 * Se crea un registro en la tabla de procesos y cada orden se actualiza con el ejecucionId del proceso
 * @author elaucho, nelson visbal
 */
public class LiquidacionBloterProcesar extends MSCModelExtend{

	private Logger logger = Logger.getLogger(LiquidacionBloterProcesar.class);	
	
	@Override
	public void execute() throws Exception {
		
		DataSet _error = new DataSet();
		_error.append("error",java.sql.Types.VARCHAR);
		_error.addNew();
		Connection conn      = _dso.getConnection();
		
		
		try {
			//Se verifica si la unidad de inversion es de tipo inventario
			String tipoInstrumento[] = new String[2];
			tipoInstrumento[0] = ConstantesGenerales.INST_TIPO_INVENTARIO;
			tipoInstrumento[1] = ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO;
			Long unidadInversion = Long.parseLong(_req.getSession().getAttribute("unidad").toString());
			UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
			UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);
			int usuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
			
			if(unidadInversionDAO.esTipoInstrumento(unidadInversion, tipoInstrumento)){
				
				LiquidacionTipoInventario liquidacionTipoInventario = new LiquidacionTipoInventario(_dso,usuario,_app,_req);
				liquidacionTipoInventario.liquidarTipoInventario(unidadInversion);
				_error.setValue("error","");
				storeDataSet("_error",_error);
				
			}else{
				//int numeroDeGrupos = 7;
				int numeroDeGrupos = 1;
				conn = _dso.getConnection();
				logger.info("Estableciendo números de grupo...");
				long tiempoNG = System.currentTimeMillis();
				CallableStatement prcProcedimientoAlmacenado = conn.prepareCall("{call INFI_ESTABLECER_GRUPO(?,?)}");
				
				prcProcedimientoAlmacenado.setLong(1, unidadInversion);
				prcProcedimientoAlmacenado.setLong(2, numeroDeGrupos);
				prcProcedimientoAlmacenado.execute();
				
				if (logger.isDebugEnabled()){
					logger.debug("Tiempo en el establecimiento de números de grupo " + (System.currentTimeMillis() - tiempoNG) + " miliseg");
				}
				
				for (int i=1; i <= numeroDeGrupos; i++){
					LiquidacionUnidadInversion liquidacionUnidadInversion = new LiquidacionUnidadInversion(_dso,usuario,_app,_req,getUserName(),unidadInversion,i);
					Thread t = new Thread(liquidacionUnidadInversion);
					t.start();					
				}
				
				_error.setValue("error","");
				storeDataSet("_error",_error);
				
			}//Fin else
	
		} catch (Throwable e) {
			
			logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
			_error.setValue("error",e.getMessage());
			storeDataSet("_error",_error);
			_config.template = "form-error.htm";
			
		}finally{
			
			_req.getSession().removeAttribute("opics_data");
			_req.getSession().removeAttribute("ordenes");
			_req.getSession().removeAttribute("radio");
			_req.getSession().removeAttribute("unidad");
			_req.getSession().removeAttribute("blotter");
			_req.getSession().removeAttribute("status");
			_req.getSession().removeAttribute("nombre_unidad");
			if (conn != null){
				conn.close();
			}
		}//fin finally
	}//FIN EXECUTE
	
	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception {
		
		boolean valido = super.isValid();
		
		if(valido){
			//NM29643 14/08/2014 Se valida que no exista proceso de adjudicacion en ejecucion para el tipo de producto
			String transaccionNegocio = "";
//			String cicloEnvio = "";
			transaccionNegocio = TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_ENVIO;
//			cicloEnvio = TransaccionNegocio.CICLO_BATCH_SUBASTA;
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
