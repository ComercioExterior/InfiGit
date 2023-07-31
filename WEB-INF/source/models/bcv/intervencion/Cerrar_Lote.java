package models.bcv.intervencion;

import com.bdv.infi.dao.IntervencionDAO;
import com.enterprisedt.util.debug.Logger;
import megasoft.AbstractModel;
import megasoft.db;

public class Cerrar_Lote extends AbstractModel {
	IntervencionDAO intervencionDao;
	private Logger logger = Logger.getLogger(Cerrar_Lote.class);

	public void execute() throws Exception {
		try {
			System.out.println("llegoooooooooooooooooooooooooo cerrar lote");
			intervencionDao = new IntervencionDAO(_dso);
			String sql = intervencionDao.modificarEstatusCerrarLote();
			db.exec(_dso, sql);

		} catch (Exception e) {
			logger.error("Cerrar_Lote : execute() " + e);
			System.out.println("Cerrar_Lote : execute() " + e);

		}
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		intervencionDao = new IntervencionDAO(_dso);
		intervencionDao.verificarLoteCerrado();

		if (intervencionDao.verificarLoteCerrado()) {
			_record.addError("Cerrar Lote", "No se puede procesar la solicitud porque ya cerro el lote del dia de hoy");
			valido = false;
		}
		return valido;
	}
}
