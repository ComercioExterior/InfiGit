package models.bcv.intervencion_oficina;

import com.bdv.infi.model.inventariodivisas.Oficina;
import com.enterprisedt.util.debug.Logger;
import megasoft.AbstractModel;
import megasoft.db;

public class UpdateDias extends AbstractModel {
	private Logger logger = Logger.getLogger(UpdateDias.class);

	public void execute() throws Exception {
		try {

			Oficina ofi = new Oficina(_dso);
			ofi.diaEntrega = _record.getValue("diasentrega");
			ofi.Fecha = _record.getValue("fecha");
			ofi.Estado = _record.getValue("estado");
			ofi.Moneda = _record.getValue("moneda");
			String saldo = _record.getValue("saldo");
			if (Integer.parseInt(saldo) > 0) {
				ofi.Donde = "AND INV.DISPONIBLE > 0";
			} else {
				ofi.Donde = "AND INV.DISPONIBLE = 0";
			}

			db.exec(_dso, ofi.ModificarDiasPorLote());
		} catch (Exception e) {

			logger.error("Update : execute() " + e);
			System.out.println("Update : execute() " + e);
		}

	}
}
