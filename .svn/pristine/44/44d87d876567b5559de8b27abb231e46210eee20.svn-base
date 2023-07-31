package models.oficina;

import com.bdv.infi.dao.OficinaDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que inserta un registro de oficina en base de datos
 * @author elaucho
 */
public class OficinaInsert extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);

		//Se ejecuta el query
		db.exec(_dso, oficinaDAO.insertar(_req.getParameter("oficina")));
		
	}//fin execute
}//Fin clase
