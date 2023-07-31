package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.clavenet_personal.subasta_divisas_personal;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.AbonoCuentaNacionalMonedaExtranjera;

public class Generar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		UsuarioDAO usu = new UsuarioDAO(_dso);
		
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion=(String)_req.getParameter("seleccion");
		String tipoFiltro=_req.getParameter("tipoFiltro");		
		
		String ordenesIdEspecificos = "";
		if(seleccion.equalsIgnoreCase("todos")){
		
			ordenesIdEspecificos=null;
			//Obtener todas las operaciones 
			/*DataSet listaTodos =  new DataSet();
			listaTodos = getSessionDataSet("listaOperacionesAbonoCtaDolaresSubasaDivisasPersonal");
			boolean flag = true;
			while(listaTodos.next()){
				//TODO  verificar el nombre del elemento en el dataset
				String idOperacion = listaTodos.getValue("ordene_operacion_id");
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
				listaTodos = getSessionDataSet("listaOperacionesAbonoCtaDolaresSubasaDivisasPersonal");				
				boolean flag = true;
				if(listaTodos.count()>0){										
					listaTodos.first();				
						while(listaTodos.next()){					
						//TODO  verificar el nombre del elemento en el dataset
						String idOperacion = listaTodos.getValue("ordene_id_relacion");					
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
		}
		
		Runnable abonoCuentaNacionalEnDolares= new AbonoCuentaNacionalMonedaExtranjera(_dso,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),Integer.parseInt(_record.getValue("undinv_id")),ordenesIdEspecificos);		
		new Thread(abonoCuentaNacionalEnDolares).start();
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO
				.listarPorTransaccionActiva(TransaccionNegocio.PROC_BATCH_CTA_NACIONAL_MONEDA_EXT_SUBV_P_ENVIO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Envio Batch Abono Cuenta Nacional en Moneda Extranjera",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}else{
			//Verifica si se espera una recepción de archivo
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			controlArchivoDAO.listarEnvioPorRecepcionBatch("'"+TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SUB_DIV_P+"'");			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Envio Batch Abono Cuenta Nacional en Moneda Extranjera",
						"No se puede procesar la solicitud porque el ciclo de envío y recepción " + " Abono Cuenta Nacional en Dólares"+ " no ha finalizado. ");
				valido = false;
			}
		}
		return valido;
	}// fin isValid
}
