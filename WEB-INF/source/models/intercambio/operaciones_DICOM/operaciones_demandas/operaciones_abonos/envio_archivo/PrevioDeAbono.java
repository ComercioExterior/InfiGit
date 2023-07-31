package models.intercambio.operaciones_DICOM.operaciones_demandas.operaciones_abonos.envio_archivo;

import megasoft.DataSet;
import models.intercambio.consultas.detalle.DetalleDeOperaciones;

import org.apache.log4j.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class PrevioDeAbono extends DetalleDeOperaciones{

Logger logger = Logger.getLogger(PrevioDeAbono.class);

private String [] Transaccion ={TransaccionNegocio.ORDEN_PAGO};
	
	public void execute() throws Exception {
		super.execute();		
	}
	
	protected void getOperaciones() throws Exception{
		
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion=(String)_req.getParameter("seleccion");
		String tipoFiltro=_req.getParameter("tipoFiltro");
		
		String ordenesIdEspecificos = "";
		if(seleccion.equalsIgnoreCase("todos")){
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
				ordenesIdEspecificos=null;
			//}
		}else{
			if(tipoFiltro.equals("INCLUIR")){
				ordenesIdEspecificos=idOrdenes;
			}
			if(tipoFiltro.equals("EXCLUIR")){
				//descargando de session el dataSet de 'lista_opcion_todos'
				DataSet listaTodos =  new DataSet();
				listaTodos = getSessionDataSet("listarUnidadesParaAbonoBatchDicom");
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
		
		operaciones = statement.executeQuery(inversionDAO.previoDeOperacionBatchPorIdOrden(ordenesIdEspecificos,_req.getParameter("undinv_id"),Transaccion,StatusOrden.REGISTRADA));
	}

}
