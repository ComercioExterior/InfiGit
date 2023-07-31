package models.oficina;

import com.bdv.infi.dao.OficinaDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de actualizar un registro de oficina
 * @author elaucho
 */
public class OficinaUpdate extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {

		//DAO a utilizar
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		
		//Se ejcuta la transacción
		db.execBatch(_dso, oficinaDAO.modificar(_req.getParameter("oficina_anterior"),_req.getParameter("oficina")));
		
	}//fin execute	
}//fin clase
