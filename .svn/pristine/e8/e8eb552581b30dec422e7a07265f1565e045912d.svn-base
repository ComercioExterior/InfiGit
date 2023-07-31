package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.venta_titulo;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		ordenDAO.ListarTitulosAbonoCuentaDolaresVentas();
		storeDataSet("titulos_ventas",ordenDAO.getDataSet());
	}
}
