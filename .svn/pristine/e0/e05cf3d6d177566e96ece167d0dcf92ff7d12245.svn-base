package models.configuracion.generales.tipo_garantias;

import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoGarantiaDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
		storeDataSet("status",confiD.status());	
	}
}