package models.eventos.generacion_amortizacion.browse;

import megasoft.DataSet;
import models.msc_utilitys.*;
public class Confirm extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {				
		//crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		_req.getSession().setAttribute("filter",_filter);
	}
}