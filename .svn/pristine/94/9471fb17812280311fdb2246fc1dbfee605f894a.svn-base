package models.sector_productivo;

import com.bdv.infi.dao.SectorProductivoDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de actualizar un registro de oficina
 * @author elaucho
 */
public class sectorProductivoUpdate extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {

		//DAO a utilizar
		SectorProductivoDAO sectorProductivoDAO = new SectorProductivoDAO(_dso);
		
		//Se ejcuta la transacción
		db.exec(_dso, sectorProductivoDAO.modificar(_req.getParameter("sector_id"),_req.getParameter("descripcion")));
	}//fin execute	
}//fin clase
