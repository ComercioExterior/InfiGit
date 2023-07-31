package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.sitme;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		UnidadInversionDAO unIn = new UnidadInversionDAO(_dso);
		unIn.listarUnidadesParaAbonoCuentaDolaresSitme();
		storeDataSet("unidadesInversion",unIn.getDataSet());
	}
}
