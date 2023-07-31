package models.configuracion.generales.tipo_bloqueo;

import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoBloqueoDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		storeDataSet("status",confiD.status());	
	}
}