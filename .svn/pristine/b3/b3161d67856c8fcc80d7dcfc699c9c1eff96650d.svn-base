package models.actividad_economica;

import com.bdv.infi.dao.ActividadEconomicaDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
public class actividadEconomicaInsert extends MSCModelExtend{
	public void execute() throws Exception {
		
		//DAO a utilizar
		ActividadEconomicaDAO actividaEconomica = new ActividadEconomicaDAO(_dso);

		try{
			//Se ejecuta el query
			db.exec(_dso, actividaEconomica.insertar(_req.getParameter("codigo_id"),_req.getParameter("sector"),_req.getParameter("sector_descripcion")));

		}catch (Exception e) {
			throw new Exception("Codigo Id de la Actividad Economica ya existe en Base de Datos.");
		}
		
	}//fin execute
}//Fin clase
