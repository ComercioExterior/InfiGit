package models.configuracion.generales.tipo_garantias;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoGarantiaDAO;
import com.bdv.infi.data.TipoGarantia;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
		TipoGarantia tipoGarantia = new TipoGarantia();
		
		String sql ="";
		
		tipoGarantia.setDescripcion(_req.getParameter("tipgar_descripcion"));
		tipoGarantia.setStatus(_req.getParameter("tipgar_status"));
		tipoGarantia.setTipo(_req.getParameter("tipgar_id"));
		sql=confiD.modificar(tipoGarantia);
		db.exec(_dso, sql);
	}
}