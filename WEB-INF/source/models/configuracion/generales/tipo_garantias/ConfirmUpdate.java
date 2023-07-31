package models.configuracion.generales.tipo_garantias;

import com.bdv.infi.dao.TipoGarantiaDAO;
import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmUpdate extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
}
