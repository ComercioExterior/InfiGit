package models.intercambio.batch_adjudicacion.enviar_archivo.subasta_divisas;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaz_ops.AdjudicacionEnvioSubastaDivisas;


public class Generar extends MSCModelExtend {
	
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		UsuarioDAO usu				= new UsuarioDAO(_dso);		
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion=(String)_req.getParameter("seleccion");
		String tipoFiltro=_req.getParameter("tipoFiltro");
		String ordenesIdEspecificos = "";
		String tipoProducto="";
		String transaccionNegocio ="";
		String cicloEnvio ="";
		ArrayList<String> statusUnidadInv=new ArrayList<String>();
		
		if(seleccion.equalsIgnoreCase("todos")){			
			ordenesIdEspecificos=null;
		}else{
			if(tipoFiltro.equals("INCLUIR")){
				ordenesIdEspecificos=idOrdenes;
			}
			if(tipoFiltro.equals("EXCLUIR")){
				//descargando de session el dataSet de 'lista_opcion_todos'
				DataSet listaTodos =  new DataSet();
				listaTodos = getSessionDataSet("listarUnidadesParaCobroAdjBatchSubastaDivisas");
				boolean flag = true;
				while(listaTodos.next()){
					String idOperacion = listaTodos.getValue("ordene_id");
					if(idOrdenes.indexOf(idOperacion) == -1){
						if(flag){
							ordenesIdEspecificos+=idOperacion;
							flag = false;
						}else
							ordenesIdEspecificos+=","+idOperacion;
					}
				}
			}
		}
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_CERRADA);
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);	
				
		Runnable adjudicacionEnvio= new AdjudicacionEnvioSubastaDivisas(_dso,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),Integer.parseInt(_req.getParameter("undinv_id")),ordenesIdEspecificos,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_ENVIO,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS,statusUnidadInv);
		new Thread(adjudicacionEnvio).start();
		setSessionDataSet("listarUnidadesParaCobroAdjBatchSubastaDivisas", null);
	}

	public boolean isValid() throws Exception {
		
		String ciclosValidar="'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS+"'".concat(",").concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA+"'".concat(",")).concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL+"'").concat("'"+TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL+"'").concat("'"+TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL+"'");
		String procesoValidar="'"+TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_ENVIO+"'".concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_ENVIO+"'");
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO
				.listarPorTransaccionActivaSubastaDivisasSubastaTitulo(procesoValidar);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Adjudicación Batch Subasta Divisas canal Red Comercial",
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
						"Adjudicación Batch Subasta Divisas canal Red Comercial",
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
