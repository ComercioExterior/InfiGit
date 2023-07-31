package models.configuracion.generales.segmentacion;

import com.bdv.infi.dao.SegmentacionDAO;
import com.bdv.infi.data.SegmentacionDefinicion;

import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		SegmentacionDAO segmentacionDAO = new SegmentacionDAO(_dso);
		SegmentacionDefinicion segmentacionDefinicion = new SegmentacionDefinicion();
			
		String sql ="";
		
		segmentacionDefinicion.setCteseg_id(_req.getParameter("cteseg_id"));
		segmentacionDefinicion.setCteseg_descripcion(_req.getParameter("cteseg_descripcion"));
		segmentacionDefinicion.setCteseg_altair_banco(_req.getParameter("cteseg_altair_banco"));
		segmentacionDefinicion.setCteseg_altair_segmento(_req.getParameter("cteseg_altair_segmento"));
		segmentacionDefinicion.setCteseg_altair_subsegmento(_req.getParameter("cteseg_altair_subsegmento"));
		
		sql=segmentacionDAO.insertar(segmentacionDefinicion);
		db.exec(_dso, sql);				
	}
}