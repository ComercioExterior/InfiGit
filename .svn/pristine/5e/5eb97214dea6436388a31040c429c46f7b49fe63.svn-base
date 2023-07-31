package models.bcv.intervencion;

import megasoft.*;
import com.bdv.infi.dao.ParametrosDAO;
import com.enterprisedt.util.debug.Logger;

public class Edit extends AbstractModel {
	private Logger logger = Logger.getLogger(Edit.class);

	public void execute() throws Exception {
		try {
			ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
			parametrosDAO.listarCliente(_req.getParameter("idOrdenes"));
			storeDataSet("table", parametrosDAO.getDataSet());
		} catch (Exception e) {
			logger.error("Edit : execute()" + e);
			System.out.println("Edit : execute()" + e);
		}

	}

}
