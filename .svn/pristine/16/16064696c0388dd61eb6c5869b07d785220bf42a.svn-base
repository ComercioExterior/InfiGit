package models.configuracion.generales.campos_dinamicos;

import models.msc_utilitys.*;
import com.bdv.infi.dao.CamposDinamicos;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		CamposDinamicos confiD = new CamposDinamicos(_dso);
		storeDataSet("status",confiD.status());	
	}
}