package models.custodia.estructura_tarifaria;

import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String comision = null;
		String cliente = null;
		
		_req.getSession().removeAttribute("comision_id");
		
		if (_record.getValue("comision_nombre")!=null&&_record.getValue("comision_nombre")!=""&&!_record.getValue("comision_nombre").equals("null")){
			comision= _record.getValue("comision_nombre");
		}
		if (_record.getValue("client_id")!=null&&_record.getValue("client_id")!=""&&!_record.getValue("client_id").equals("null")){
			cliente= _record.getValue("client_id");
		}
		
		CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
		
		// Realizar consulta
		estructuraTarifaria.listarComisionesExistentes(comision,cliente);
		DataSet _data=estructuraTarifaria.getDataSet();
		storeDataSet("table", _data);
		storeDataSet("total", estructuraTarifaria.getTotalRegistros());
		DataSet _filter=new DataSet();
		_filter.append("comision_nombre", java.sql.Types.VARCHAR);
		_filter.append("client_id", java.sql.Types.VARCHAR);
		_filter.addNew();
		_filter.setValue("comision_nombre", comision);
		_filter.setValue("client_id", cliente);
		storeDataSet("filter", _filter);
	}
}
