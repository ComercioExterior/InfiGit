package models.intercambio.recepcion.lectura_archivo;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listaUnidadesAdjudicar();
		storeDataSet("uniInverPublicadas",confiD.getDataSet());
	}
}
