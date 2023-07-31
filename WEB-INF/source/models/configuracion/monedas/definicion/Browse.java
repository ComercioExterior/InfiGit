package models.configuracion.monedas.definicion;

import models.msc_utilitys.*;
import com.bdv.infi.dao.MonedaDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		MonedaDAO moneda=new MonedaDAO(_dso);

		//Realizar consulta
		moneda.listar();
		//registrar los datasets exportados por este modelo
		storeDataSet("table", moneda.getDataSet());
		storeDataSet("total", moneda.getTotalRegistros());
	}
}