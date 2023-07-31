package models.oficina;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OficinaDAO;
/**
 * Clase que elimina un registro de oficina
 * @author elaucho
 */
public class OficinaDelete extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);

		//Se ejecuta el query
		db.exec(_dso, oficinaDAO.eliminar(_req.getParameter("oficina")));
		
	}//fin execute
}
