package models.oficina_comercial;

import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OficinaDAO;

public class OficinaDelete extends MSCModelExtend {

	@Override
	public void execute() throws Exception {

		OficinaDAO oficinaDAO = new OficinaDAO(_dso);

		try {
			db.exec(_dso, oficinaDAO.eliminarInventarioPorOficina(_req.getParameter("oficina")));
			db.exec(_dso, oficinaDAO.eliminarComercial(_req.getParameter("oficina")));
		} catch (Exception e) {
			System.out.println("OficinaDelete : execute() " + e);
			Logger.error(this, "OficinaDelete : execute() " + e);
		}
	}
}
