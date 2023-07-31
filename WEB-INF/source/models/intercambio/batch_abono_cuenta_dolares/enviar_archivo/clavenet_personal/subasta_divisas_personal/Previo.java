package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.clavenet_personal.subasta_divisas_personal;

import megasoft.DataSet;
import models.intercambio.consultas.detalle.DetalleDeOperaciones;

public class Previo extends DetalleDeOperaciones {

	
	
	public void execute() throws Exception {		
		super.execute();		
	}
	
	protected void getOperaciones() throws Exception{
		
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion=(String)_req.getParameter("seleccion");
		String tipoFiltro=_req.getParameter("tipoFiltro");
		
		String ordenesIdEspecificos = "";
		if(seleccion.equalsIgnoreCase("todos")){
			ordenesIdEspecificos=null;
			//Obtener todas las operaciones 
			/*DataSet listaTodos =  new DataSet();
			listaTodos = getSessionDataSet("listarUnidadesParaCobroAdjBatchSubastaDivisas");
			boolean flag = true;
			while(listaTodos.next()){
				//TODO  verificar el nombre del elemento en el dataset
				//String idOperacion = listaTodos.getValue("ordene_operacion_id");
				String idOperacion = listaTodos.getValue("ordene_id");
				if(flag){
					ordenesIdEspecificos+=idOperacion;
					flag = false;
				}else
					ordenesIdEspecificos+=","+idOperacion;*/				
			//}
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
		
		operaciones = statement.executeQuery(inversionDAO.previoDeOperacionBatchAbonoCtaDolaresPorIdOrden(ordenesIdEspecificos/*_req.getParameter("idOrdenes")*/,_req.getParameter("undinv_id")));
	}

}
