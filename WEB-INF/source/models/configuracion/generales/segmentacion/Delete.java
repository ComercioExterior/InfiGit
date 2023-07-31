package models.configuracion.generales.segmentacion;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.SegmentacionDAO;
import com.bdv.infi.data.SegmentacionDefinicion;;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		SegmentacionDAO segmentacionDAO = new SegmentacionDAO(_dso);
		SegmentacionDefinicion segmentacionDefinicion = new SegmentacionDefinicion();
		
		String sql ="";
		
		segmentacionDefinicion.setCteseg_id(_req.getParameter("cteseg_id"));
		
		sql=segmentacionDAO.eliminar(segmentacionDefinicion);
		db.exec(_dso, sql);
	}
}