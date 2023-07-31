package models.sector_productivo;

import com.bdv.infi.dao.SectorProductivoDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que inserta un registro de oficina en base de datos
 * @author elaucho
 */
public class sectorProductivoInsert extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		SectorProductivoDAO sectorProductivoDAO = new SectorProductivoDAO(_dso);

		//Se ejecuta el query
		try{
			//Se ejecuta el query
			db.exec(_dso, sectorProductivoDAO.insertar(_req.getParameter("sector_id"),_req.getParameter("descripcion")));
				
		}catch (Exception e) {
			throw new Exception("Sector Productivo ya existe en Base de Datos.");
		}
		
	}//fin execute
}//Fin clase
