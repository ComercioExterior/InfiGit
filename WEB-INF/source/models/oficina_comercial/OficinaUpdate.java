package models.oficina_comercial;

import com.bdv.infi.dao.OficinaDAO;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class OficinaUpdate extends MSCModelExtend {

	@Override
	public void execute() {
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		String sql = oficinaDAO.modificarComercial(_req.getParameter("oficina_anterior"), _req.getParameter("oficina"), _req.getParameter("descripcion"), _req.getParameter("direccion"), _req.getParameter("estado"), _req.getParameter("municipio"), _req.getParameter("estatus"));

		try {
			db.exec(_dso, sql);

		} catch (Exception e) {
			System.out.println("OficinaUpdate : execute() " + e);
			Logger.error(this, "OficinaUpdate : execute() " + e);

		}
	}

}
