package models.intercambio.batch_adjudicacion.enviar_archivo.subasta_divisas;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		UnidadInversionDAO unIn = new UnidadInversionDAO(_dso);
		unIn.listarUnidadesParaCobroAdjBatchSubastaDivisas(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);			
		storeDataSet("unidadesInversion",unIn.getDataSet());
	}
}
