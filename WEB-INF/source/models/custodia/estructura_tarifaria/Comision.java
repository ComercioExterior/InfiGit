package models.custodia.estructura_tarifaria;

import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Comision extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		_req.getSession().setAttribute("comision_id", _record.getValue("comision_id"));
		String indicador= "";
		
		if (_record.getValue("comision_in_general")!=null){
			_req.getSession().setAttribute("com_in_general", _record.getValue("comision_in_general"));
			indicador= getSessionObject("com_in_general").toString();
		}else{
			indicador= getSessionObject("com_in_general")!=null?getSessionObject("com_in_general").toString():"";
		}
		DataSet _comIndicador=new DataSet();
		_comIndicador.append("com_in_general",java.sql.Types.VARCHAR);
		_comIndicador.addNew();
		_comIndicador.setValue("com_in_general",indicador);
		storeDataSet("indicador", _comIndicador);
		
		if (_record.getValue("comision_id") ==null || _record.getValue("comision_id").equals("X")) {//Mostraremos plantilla de AGREGAR

			DataSet ds = new DataSet();
			ds.append("comision_id", java.sql.Types.VARCHAR);
			ds.append("comision_nombre", java.sql.Types.VARCHAR);
			ds.append("usuario_id", java.sql.Types.VARCHAR);
			ds.append("fecha_ult_act", java.sql.Types.VARCHAR);
			ds.addNew();
			ds.setValue("comision_id", "X");
			ds.setValue("comision_nombre", "");	
			ds.setValue("usuario_id", "");	
			ds.setValue("fecha_ult_act", "");
			storeDataSet("comision", ds);
			
		}else if(_record.getValue("comision_id")!=null){//Mostraremos plantilla de EDITAR
			CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
			
			// Realizar consulta
			estructuraTarifaria.listarComisionPorId(_record.getValue("comision_id"));
			storeDataSet("comision", estructuraTarifaria.getDataSet());
		}
	}
}
