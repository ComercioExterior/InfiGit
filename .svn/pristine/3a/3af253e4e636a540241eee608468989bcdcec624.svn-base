package models.configuracion.generales.tipo_garantias;

import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoGarantiaDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
	
		String status = null;
		if (_record.getValue("tipgar_status")!=null){
			status= _record.getValue("tipgar_status");
		}
		//Realizar consulta
		confiD.listarPorStatus(status);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros());
	}
}