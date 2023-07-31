package models.oficina_comercial;

import com.bdv.infi.dao.OficinaDAO;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class OficinaUpdateEstatus extends MSCModelExtend {

	@Override
	public void execute() {
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		System.out.println("Oficinas : " + _req.getParameter("oficinas"));
		String sql = oficinaDAO.modificarEstatusPorLote(_req.getParameter("oficinas"),_req.getParameter("estatus"));

		try {
			db.exec(_dso, sql);

		} catch (Exception e) {
			System.out.println("OficinaUpdate : execute() " + e);
			Logger.error(this, "OficinaUpdate : execute() " + e);

		}
	}

}
