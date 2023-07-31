package models.actividad_economica;

import com.bdv.infi.dao.ActividadEconomicaDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de actualizar un registro de oficina
 * @author elaucho
 */
public class actividadEconomicaUpdate extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {

		//DAO a utilizar
		ActividadEconomicaDAO actividadEconomica = new ActividadEconomicaDAO(_dso);
		
		//Se ejcuta la transacción
		db.exec(_dso, actividadEconomica.modificar(_req.getParameter("sector"),_req.getParameter("codigo_id"),_req.getParameter("sector_descripcion")));
	}//fin execute	
}//fin clase
