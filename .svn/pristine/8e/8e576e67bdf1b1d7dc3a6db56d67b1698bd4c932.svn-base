package models.configuracion.generales.indicadores;

import com.bdv.infi.dao.IndicadoresDAO;
import com.bdv.infi.data.IndicadoresDefinicion;

import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		IndicadoresDAO indicadoresDAO = new IndicadoresDAO(_dso);
		IndicadoresDefinicion indicadoresDefinicion = new IndicadoresDefinicion();
		
		String sql ="";
		
		indicadoresDefinicion.setIndica_id(_req.getParameter("indica_id"));
		indicadoresDefinicion.setIndica_descripcion(_req.getParameter("indica_descripcion"));
		indicadoresDefinicion.setIndica_in_requerido(_req.getParameter("indica_in_requerido"));
		indicadoresDefinicion.setIndica_in_requisito(_req.getParameter("indica_in_requisito"));
		sql=indicadoresDAO.insertar(indicadoresDefinicion);
		db.exec(_dso, sql);				
	}
}