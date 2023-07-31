package models.oficina_comercial;

import com.bdv.infi.dao.OficinaDAO;

import models.msc_utilitys.MSCModelExtend;

public class OficinaEdit extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		oficinaDAO.ListarEstado();
		storeDataSet("estados", oficinaDAO.getDataSet());
		storeDataSet("request",getDataSetFromRequest());
		
	}
}
