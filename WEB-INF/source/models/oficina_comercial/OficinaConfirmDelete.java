package models.oficina_comercial;

import com.bdv.infi.dao.OficinaDAO;
import models.msc_utilitys.MSCModelExtend;

public class OficinaConfirmDelete extends MSCModelExtend {

	@Override
	public void execute() throws Exception {

		storeDataSet("request", getDataSetFromRequest());

	}

	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		if (oficinaDAO.verificarOficinaComercial(_req.getParameter("oficina"))) {
			_record.addError("Eliminar Oficina", "Tiene referencia a un inventario.");
			valido = false;

		}
		return valido;
	}

}
