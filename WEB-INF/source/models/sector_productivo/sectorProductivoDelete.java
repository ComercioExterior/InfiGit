package models.sector_productivo;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OficinaDAO;
import com.bdv.infi.dao.SectorProductivoDAO;
/**
 * Clase que elimina un registro de oficina
 * @author elaucho
 */
public class sectorProductivoDelete extends MSCModelExtend{
	public void execute() throws Exception {

		//DAO a utilizar
		SectorProductivoDAO sectorProductivoDAO = new SectorProductivoDAO(_dso);
		
		//Se ejcuta la transacción
		db.exec(_dso, sectorProductivoDAO.eliminar(_req.getParameter("sector_id")));
	}//fin execute	
}//fin clase
