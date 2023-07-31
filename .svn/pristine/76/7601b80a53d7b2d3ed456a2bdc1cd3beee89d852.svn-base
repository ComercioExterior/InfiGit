package models.bcv.intervencion_oficina;

import java.math.BigDecimal;
import com.bdv.infi.model.inventariodivisas.Oficina;
import com.enterprisedt.util.debug.Logger;
import megasoft.AbstractModel;
import megasoft.db;

/**
 * Clase encargada actualizar en Base de Datos un Valor del parametro.
 * 
 */
public class Update extends AbstractModel {
	private Logger logger = Logger.getLogger(Update.class);

	public void execute() throws Exception {
		try {

			Oficina ofi = new Oficina(_dso);
			ofi.Monto = new BigDecimal(_record.getValue("asignado"));
			ofi.Porcentaje = new BigDecimal(_record.getValue("porcentaje"));
			ofi.Disponible =  ofi.Monto.multiply(ofi.Porcentaje).divide(new BigDecimal(100));
			ofi.Consumido = new BigDecimal(_record.getValue("consumido"));
			ofi.Moneda = _record.getValue("moneda");
			ofi.diaEntrega = _record.getValue("diasentrega");
			ofi.Estatus = _record.getValue("estatus");
			System.out.println("diaEntrega : " + ofi.diaEntrega);
			ofi.Id = _record.getValue("nro");
			ofi.Fecha = _record.getValue("fecha");

			db.exec(_dso, ofi.ModificarInv());
		} catch (Exception e) {
			
			logger.error("Update : execute() " + e);
			System.out.println("Update : execute() " + e);
		}

	}
}
