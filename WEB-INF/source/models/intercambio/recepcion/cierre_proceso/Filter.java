package models.intercambio.recepcion.cierre_proceso;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listarRecepcion();
		DataSet unidad = confiD.getDataSet();
		if(unidad.count()<=0){
			unidad.addNew();
			unidad.setValue("undinv_id", "0");
			unidad.setValue("undinv_nombre", null);
			unidad.setValue("undinv_serie", null);
		}
		storeDataSet("uniInver",unidad);
	}
}
