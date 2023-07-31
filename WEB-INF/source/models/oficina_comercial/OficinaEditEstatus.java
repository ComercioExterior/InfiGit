package models.oficina_comercial;

import com.bdv.infi.dao.OficinaDAO;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class OficinaEditEstatus extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		DataSet _datos = new DataSet();
		String idOrdenes  = _req.getParameter("idOrdenes");
		System.out.println("idOrdenes oficina : " + idOrdenes);
		
		_datos.append("oficinas", java.sql.Types.VARCHAR);
		_datos.addNew();
		_datos.setValue("oficinas", idOrdenes);
		
//		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
//		oficinaDAO.ListarEstado();
//		storeDataSet("estados", oficinaDAO.getDataSet());
		storeDataSet("request",getDataSetFromRequest());
		storeDataSet("datos", _datos);
		
	}
}
