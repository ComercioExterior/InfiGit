package models.intercambio.batch_liquidacion.enviar_archivo.clavenet_personal.subasta_divisas_personal;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaz_ops.LiquidacionEnvioSubastaDivisas;

public class Generar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversi�n
	 */
	public void execute() throws Exception {
		
		UsuarioDAO usu				= new UsuarioDAO(_dso);		
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion=(String)_req.getParameter("seleccion");
		String tipoFiltro=_req.getParameter("tipoFiltro");
		String ordenesIdEspecificos = "";
		String tipoProducto=_req.getParameter("undinv_tipo_prod");
		String transaccionNegocio ="";
		String cicloEnvio ="";		
		ArrayList<String> statusUnidadInv=new ArrayList<String>();	
		
		if(seleccion.equalsIgnoreCase("todos")){
			ordenesIdEspecificos=null;
			//Obtener todas las operaciones 
			/*DataSet listaTodos =  new DataSet();
			listaTodos = getSessionDataSet("listarUnidadesParaCobroLiqPorTipoProBatch");
			boolean flag = true;
			while(listaTodos.next()){
				//TODO  verificar el nombre del elemento en el dataset
				//String idOperacion = listaTodos.getValue("ordene_operacion_id");
				String idOperacion = listaTodos.getValue("ordene_id");
				if(flag){
					ordenesIdEspecificos+=idOperacion;
					flag = false;
				}else
					ordenesIdEspecificos+=","+idOperacion;
			}*/
		}else{
			if(tipoFiltro.equals("INCLUIR")){
				ordenesIdEspecificos=idOrdenes;
			}
			if(tipoFiltro.equals("EXCLUIR")){
				//descargando de session el dataSet de 'lista_opcion_todos'
				DataSet listaTodos =  new DataSet();
				listaTodos = getSessionDataSet("listarUnidadesParaCobroLiqPorTipoProBatch");
				boolean flag = true;
				while(listaTodos.next()){
					//TODO  verificar el nombre del elemento en el dataset
					//String idOperacion = listaTodos.getValue("ordene_operacion_id");
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
		
		 if (tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){
				transaccionNegocio = TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_ENVIO;
				cicloEnvio = TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL;
				statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_PUBLICADA);
				statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_CERRADA);	
		} else 
		if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
			transaccionNegocio = TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_CLAVENET_PERSONAL_ENVIO;
			cicloEnvio =  TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL;
			statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_LIQUIDADA);			
		} else 
		if (tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
			transaccionNegocio = TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_RED_COMERCIAL_ENVIO;
			cicloEnvio = TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL;
			statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_LIQUIDADA);
		}
		//TODO: ELIMINAR ESTA CONDICION
		
		
		Runnable liquidacionEnvioSubastaDivisas= new LiquidacionEnvioSubastaDivisas(_dso,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),Integer.parseInt(_record.getValue("undinv_id")),ordenesIdEspecificos,transaccionNegocio,cicloEnvio,statusUnidadInv);
		new Thread(liquidacionEnvioSubastaDivisas).start();
		setSessionDataSet("listarUnidadesParaCobroLiqPorTipoProBatch", null);
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		
		String ciclosValidar="'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS+"'".concat(",").concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA+"'".concat(",")).concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL+"'").concat(",'"+TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL+"'").concat(",'"+TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL+"'").concat(",'"+TransaccionNegocio.CICLO_BATCH_CONCILIACION_RETENCION+"'");
		String procesoValidar="'"+TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO+"'".concat(",").concat("'"+TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_CLAVENET_PERSONAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_RED_COMERCIAL_ENVIO+"'").concat(",'"+TransaccionNegocio.PROC_BATCH_CONCILIACION_RETENCION_ENVIO+"'");
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO
				.listarPorTransaccionActivaSubastaDivisasSubastaTitulo(procesoValidar);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Liquidaci�n Batch Subasta",
							"No se puede procesar la solicitud porque otra "
									+ "persona realiz� esta acci�n y esta actualmente activa");
			valido = false;
		}else{
			//Verifica si se espera una recepci�n de archivo
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(ciclosValidar);			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Liquidaci�n Batch Subasta Divisas canal Clavenet Personal",
						"No se puede procesar la solicitud porque el ciclo de env�o y recepci�n "  + (dataSet.getValue("in_recepcion").equals("0")?"de adjudicaci�n ":"de liquidaci�n ") + " no ha finalizado. ");
				valido = false;
			}
		}
		return valido;
	}// fin isValid
}
