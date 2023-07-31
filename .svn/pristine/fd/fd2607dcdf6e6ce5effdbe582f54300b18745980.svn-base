package models.configuracion.generales.blotter;

import models.msc_utilitys.*;
import com.bdv.infi.dao.BlotterDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		BlotterDAO bloterDAO = new BlotterDAO(_dso);
		
		storeDataSet("indicador", bloterDAO.indicador());
	}
}