package models.actividad_economica;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ActividadEconomicaDAO;
/**
 * Clase que elimina un registro de oficina
 * @author elaucho
 */
public class actividadEconomicaDelete extends MSCModelExtend{
	public void execute() throws Exception {

		//DAO a utilizar
		ActividadEconomicaDAO actividadEconomicaDAO = new ActividadEconomicaDAO(_dso);
		
		//Se ejcuta la transacción
		db.exec(_dso, actividadEconomicaDAO.eliminar(_req.getParameter("codigo_id")));
	}//fin execute	
}//fin clase
