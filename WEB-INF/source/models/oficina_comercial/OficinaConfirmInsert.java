package models.oficina_comercial;

import com.bdv.infi.model.inventariodivisas.Oficina;
import models.msc_utilitys.MSCModelExtend;

public class OficinaConfirmInsert extends MSCModelExtend {

	@Override
	public void execute() throws Exception {
		storeDataSet("request", getDataSetFromRequest());

	}

	public boolean isValid() throws Exception {
		boolean valido = false;
		Oficina ofi = new Oficina(_dso);

		if (ofi.verificarOficinaComercial(_req.getParameter("oficina"))) {
			_record.addError("Añadir Oficina", "Ya esta oficina esta registrada");
		} else {
			valido = true;

			if (_req.getParameter("oficina").trim().equals("")) {
				_record.addError("Añadir Oficina", "Tiene que colocar una oficina");
				valido = false;
			}
			if (_req.getParameter("descripcion").trim().equals("")) {
				_record.addError("Añadir Oficina", "Tiene que colocar una descripcion");
				valido = false;
			}
			if (_req.getParameter("direccion").trim().equals("")) {
				_record.addError("Añadir Oficina", "Tiene que colocar una direccion");
				valido = false;
			}

			if (_req.getParameter("estado").trim().equals("")) {
				_record.addError("Añadir Oficina", "Tiene que colocar un estado");
				valido = false;
			}

			if (_req.getParameter("municipio").trim().equals("")) {
				_record.addError("Añadir Oficina", "Tiene que colocar un municipio");
				valido = false;
			}
		}

		return valido;
	}

}
