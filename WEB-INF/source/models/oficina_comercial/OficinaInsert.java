package models.oficina_comercial;

import com.bdv.infi.dao.OficinaDAO;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class OficinaInsert extends MSCModelExtend {

	@Override
	public void execute() {

		OficinaDAO oficinaDAO = new OficinaDAO(_dso);

		try {
			db.exec(_dso, oficinaDAO.insertar(_req.getParameter("oficina"), _req.getParameter("descripcion"), _req.getParameter("direccion"), _req.getParameter("estado"), _req.getParameter("municipio")));

		} catch (Exception e) {
			System.out.println("OficinaInsert : execute() " + e);
			Logger.error(this, "OficinaInsert : execute() " + e);
		}

	}

}
