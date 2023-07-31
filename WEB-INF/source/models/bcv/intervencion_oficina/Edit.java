package models.bcv.intervencion_oficina;

import megasoft.*;
import com.bdv.infi.model.inventariodivisas.Oficina;
import com.enterprisedt.util.debug.Logger;

public class Edit extends AbstractModel {
	private Logger logger = Logger.getLogger(Edit.class);

	public void execute() throws Exception {
		try {
			Oficina ofi = new Oficina(_dso);
			ofi.Id = _req.getParameter("nro");
			ofi.Moneda = _req.getParameter("moneda");
			ofi.Fecha = _req.getParameter("fecha");
			ofi.ListarPorOficina();

			storeDataSet("table", ofi.getDataSet());
		} catch (Exception e) {
			logger.error("Edit : execute()" + e);
			System.out.println("Edit : execute()" + e);
		}

	}

}
