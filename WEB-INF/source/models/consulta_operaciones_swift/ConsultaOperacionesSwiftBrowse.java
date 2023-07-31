/**
 * 
 */
package models.consulta_operaciones_swift;

import java.text.SimpleDateFormat;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
/**
 * Clase encargada de mostrar las operaciones enviadas o no V&iacute;a interfaz SWIFT
 */
public class ConsultaOperacionesSwiftBrowse extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
	
		//DAO a utilizar
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		OperacionDAO operacionDAO = new OperacionDAO(_dso);			
		//Listar operaciones DAO
		/*if(_record.getValue("tipo_producto").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){
			operacionDAO.listarMensajesSwift(formato.parse(_record.getValue("fecha_desde")),formato.parse(_record.getValue("fecha_hasta")),0,_record.getValue("enviado").equals("0")?ConstantesGenerales.STATUS_EN_ESPERA:ConstantesGenerales.STATUS_APLICADA,_record.getValue("tipo_producto"),_record.getValue("transaccion_id"));
			
		}else{*/
		operacionDAO.listarMensajesSwiftInstruccion202(formato.parse(_record.getValue("fecha_desde")),formato.parse(_record.getValue("fecha_hasta")),0,_record.getValue("enviado").equals("0")?ConstantesGenerales.STATUS_EN_ESPERA:ConstantesGenerales.STATUS_APLICADA,_record.getValue("tipo_producto"),_record.getValue("transaccion_id"));
		
		//subiendo a session para tener disponible la lista en caso de que se use la opción 'todos'
		setSessionDataSet("lista_ConsultaOperacionesSwiftBrowse", operacionDAO.getDataSet());		
			
		//}
		//Publicamos el dataset
		storeDataSet("registros",operacionDAO.getDataSet());
		storeDataSet("total",operacionDAO.getTotalRegistros());
		storeDataSet("request",getDataSetFromRequest());
		storeDataSet("record",_record);
		
	}//fin execute

}//fin ConsultaOperacionesSwiftBrowse
