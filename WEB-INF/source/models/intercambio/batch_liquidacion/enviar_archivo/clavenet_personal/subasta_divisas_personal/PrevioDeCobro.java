package models.intercambio.batch_liquidacion.enviar_archivo.clavenet_personal.subasta_divisas_personal;

import megasoft.DataSet;
import models.intercambio.consultas.detalle.DetalleDeOperaciones;

import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import org.apache.log4j.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class PrevioDeCobro extends DetalleDeOperaciones{
	private String [] Transaccion ={TransaccionNegocio.TOMA_DE_ORDEN,TransaccionNegocio.LIQUIDACION};
Logger logger = Logger.getLogger(PrevioDeCobro.class);
	
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
		
		operaciones = statement.executeQuery(inversionDAO.previoDeOperacionBatchPorIdOrden(ordenesIdEspecificos,_req.getParameter("undinv_id"),Transaccion,StatusOrden.LIQUIDADA,StatusOrden.REGISTRADA,StatusOrden.NO_ADJUDICADA_INFI,StatusOrden.NO_CRUZADA));
	}

}
