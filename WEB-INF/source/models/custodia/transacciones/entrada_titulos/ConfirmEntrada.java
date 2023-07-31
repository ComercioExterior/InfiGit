package models.custodia.transacciones.entrada_titulos;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class ConfirmEntrada extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
			
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		storeDataSet("record", _record);
	}
}
