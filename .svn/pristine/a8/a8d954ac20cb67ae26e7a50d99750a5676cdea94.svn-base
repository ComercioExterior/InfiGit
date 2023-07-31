package models.bcv.intervencion_oficina;

import java.math.BigDecimal;
import com.bdv.infi.model.inventariodivisas.Oficina;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

/**
 * insert en la tabla inventario.
 * @author nm11383
 *
 */
public class InventarioInsert extends MSCModelExtend {
	Oficina ofi;

	@Override
	public void execute() {
		try {
			storeDataSet("request", getDataSetFromRequest());
			ofi = new Oficina(_dso);
			ofi.Id = _req.getParameter("oficina");
			ofi.Moneda = _req.getParameter("moneda");
			ofi.Monto = new BigDecimal(_req.getParameter("monto"));
			ofi.Porcentaje = new BigDecimal(_req.getParameter("porcentaje"));
			ofi.Estatus = _req.getParameter("estatus");
			ofi.diaEntrega = _req.getParameter("diasentrega");
			ofi.Disponible = new BigDecimal(0);
			ofi.Consumido = new BigDecimal(0);
			ofi.MontoAsignado = ofi.Monto.multiply(ofi.Porcentaje).divide(new BigDecimal(100));
			ofi.IdTabla = "INFI_SQ_030.NEXTVAL";
			
			db.exec(_dso, ofi.CargarInventario());
			
		} catch (Exception e) {
			System.out.println("InventarioInsert : execute() " + e);
			Logger.error(this, "InventarioInsert : execute() " + e);
			
		}

	}

	public boolean isValid() throws Exception {
		boolean valido = false;
		ofi = new Oficina(_dso);
		
		if (ofi.verificarOficinaComercial(_req.getParameter("oficina"))) {
			if (ofi.verificarInventario(_req.getParameter("oficina"), "SYSDATE")) {
				_record.addError("Añadir Inventario", "Ya esta oficina tiene cargado un inventario");
				
			} else {
				valido = true;
			}
			
		} else {
			_record.addError("Añadir Inventario", "No se puede porque no ha registrado la oficina");
		}

		return valido;
	}
}
