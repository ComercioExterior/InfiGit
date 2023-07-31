package models.bcv.intervencion_oficina;

import com.enterprisedt.util.debug.Logger;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Confirm extends MSCModelExtend {
	private Logger logger = Logger.getLogger(Confirm.class);

	
	public void execute() throws Exception {

		try {
			DataSet _filter = getDataSetFromRequest();
			storeDataSet("filter", _filter);
		} catch (Exception e) {
			logger.error("Confirm : execute()" + e);
			System.out.println("Confirm : execute()" + e);
		}

	}
}