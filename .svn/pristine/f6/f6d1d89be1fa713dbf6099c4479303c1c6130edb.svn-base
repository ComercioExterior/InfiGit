package models.configuracion.portafolio_equivalencia;

import com.bdv.infi.dao.EquivalenciaPortafolioDAO;
import com.bdv.infi.data.EquivalenciaPortafolio;
import megasoft.db;
import models.msc_utilitys.*;



public class Delete extends MSCModelExtend {

	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EquivalenciaPortafolioDAO equivalenciaPortafolioDAO= new EquivalenciaPortafolioDAO(_dso);
		EquivalenciaPortafolio equivalenciaPortafolio =new EquivalenciaPortafolio();
		String sql ="";
		equivalenciaPortafolio.setIdSegmento(_req.getParameter("segmento_id"));
		sql=equivalenciaPortafolioDAO.eliminar(equivalenciaPortafolio);
		db.exec(_dso, sql);
		
	}
	
}