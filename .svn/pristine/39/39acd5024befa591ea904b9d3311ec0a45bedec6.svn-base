package models.oficina_comercial;

import models.msc_utilitys.MSCModelExtend;

public class OficinaConfirmUpdate extends MSCModelExtend {

	@Override
	public void execute() throws Exception {
		storeDataSet("request", getDataSetFromRequest());

	}

	public boolean isValid() throws Exception {
		boolean valido = true;

		if (_req.getParameter("oficina").trim().equals("")) {
			_record.addError("Modificar Oficina", "Tiene que colocar una oficina");
			valido = false;
		}
		if (_req.getParameter("descripcion").trim().equals("")) {
			_record.addError("Modificar Oficina", "Tiene que colocar una descripcion");
			valido = false;
		}
		if (_req.getParameter("direccion").trim().equals("")) {
			_record.addError("Modificar Oficina", "Tiene que colocar una direccion");
			valido = false;
		}

		if (_req.getParameter("estado").trim().equals("")) {
			_record.addError("Modificar Oficina", "Tiene que colocar un estado");
			valido = false;
		}

		if (_req.getParameter("municipio").trim().equals("")) {
			_record.addError("Modificar Oficina", "Tiene que colocar un municipio");
			valido = false;
		}
		if (_req.getParameter("estatus").trim().equals("")) {
			_record.addError("Modificar Oficina", "Tiene que colocar un estatus");
			valido = false;
		}

		return valido;
	}
}
