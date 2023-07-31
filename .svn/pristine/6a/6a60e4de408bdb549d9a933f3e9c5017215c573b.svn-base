package models.bcv.intervencion_oficina;

import megasoft.*;
import com.enterprisedt.util.debug.Logger;

public class EditDias extends AbstractModel {
	
	private Logger logger = Logger.getLogger(EditDias.class);
	private DataSet _estado = new DataSet();
	
	
	public void execute() throws Exception {
		
		try {		
			String estado = _req.getParameter("estado");
			String fecha  = _req.getParameter("fecha");
			String moneda  = _req.getParameter("moneda");

			_estado.append("estado", java.sql.Types.VARCHAR);
			_estado.append("fecha", java.sql.Types.VARCHAR);
			_estado.append("moneda", java.sql.Types.VARCHAR);
			_estado.addNew();
			_estado.setValue("estado", estado);
			_estado.setValue("fecha", fecha);
			_estado.setValue("moneda", moneda);
			
			storeDataSet("table", _estado);
		} catch (Exception e) {
			logger.error("Edit : execute()" + e);
			System.out.println("Edit : execute()" + e);
		}

	}

}
