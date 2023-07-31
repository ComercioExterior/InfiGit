package models.configuracion.generales.campos_dinamicos;

import models.msc_utilitys.*;
import com.bdv.infi.dao.CamposDinamicos;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		CamposDinamicos confiD = new CamposDinamicos(_dso);
			
		//Realizar consulta
		confiD.listar();
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
	}
}