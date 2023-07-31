package models.bcv.configuracion;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CredencialesDAO;

public class Delete  extends MSCModelExtend {
	@Override
	public void execute() throws Exception {
		String sistema = _req.getParameter("sistema");
		//DAO a utilizar
		CredencialesDAO credencialesDAO = new CredencialesDAO(_dso);
		
		//Se ejecuta el query
		db.exec(_dso, credencialesDAO.delete(sistema));
	}//fin execute
}
