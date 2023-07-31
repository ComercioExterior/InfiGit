package models.ordenes.financiamiento;

import com.bdv.infi.dao.OrdenDAO;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String cliente = null;

		if (_record.getValue("client_id")!=null){
			cliente= _record.getValue("client_id");
		}
		
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		
		// Realizar consulta
		ordenDAO.ordenesFinanciadas(cliente);
		DataSet _data=ordenDAO.getDataSet();
		storeDataSet("table", _data);
		storeDataSet("total", ordenDAO.getTotalRegistros());
	}
}
