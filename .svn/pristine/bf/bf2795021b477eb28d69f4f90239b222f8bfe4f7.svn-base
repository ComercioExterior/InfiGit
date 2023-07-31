/**
 * 
 */
package models.consulta_operaciones_swift;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;

/**
 * Clase que muestra el detalle para una operación SWIFT
 */
public class ConsultaOperacionesSwiftDetalle extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		OperacionDAO operacionDAO = new OperacionDAO(_dso);
		ClienteCuentasDAO clienteCuentasDAO= new ClienteCuentasDAO(_dso);
	
		//Listar operaciones DAO
		/*if(_req.getParameter("tipoProducto").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){	
			operacionDAO.listarMensajesSwift(null,null,Long.parseLong(_req.getParameter("ordene_id")),_req.getParameter("enviado").equals("0")?ConstantesGenerales.STATUS_EN_ESPERA:ConstantesGenerales.STATUS_APLICADA,_req.getParameter("tipoProducto"),null);
		}else{*/
			operacionDAO.listarSwiftPorIdInstruccion202(_req.getParameter("ordene_id"),_req.getParameter("tipoProducto"));			
			_config.template = "detalleSwift.htm";
		//}
		
		DataSet resultado = operacionDAO.getDataSet(); 
		if (resultado != null){
			resultado.next();
			if (resultado.getValue("TIPO_INSTRUCCION_ID").equals(String.valueOf(TipoInstruccion.CHEQUE))){
				_config.template = "cheque.htm"	;
			}
		}	
		
		//Publicamos el dataset
		storeDataSet("cuenta",clienteCuentasDAO.getDataSet());
		storeDataSet("registros",operacionDAO.getDataSet());
		storeDataSet("total",operacionDAO.getTotalRegistros());
		
	}//fin execute
}//Fin clase
