package models.configuracion.generales.tipo_garantias;

import com.bdv.infi.dao.TipoGarantiaDAO;
import com.bdv.infi.data.TipoGarantia;
import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
		TipoGarantia tipoGarantia = new TipoGarantia();
		
		tipoGarantia.setDescripcion(_req.getParameter("tipgar_descripcion"));
		tipoGarantia.setStatus(_req.getParameter("tipgar_status"));
				
		//ensamblar SQL
		sql=confiD.insertar(tipoGarantia);
		//ejecutar query
		db.exec(_dso,sql);
	}
}