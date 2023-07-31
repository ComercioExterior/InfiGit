package models.intercambio.batch_adjudicacion.enviar_archivo.subasta;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		UnidadInversionDAO unIn = new UnidadInversionDAO(_dso);
		unIn.listarUnidadesParaCobroAdjBatch();			
		storeDataSet("unidadesInversion",unIn.getDataSet());
	}
}
