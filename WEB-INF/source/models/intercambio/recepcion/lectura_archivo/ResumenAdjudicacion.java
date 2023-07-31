package models.intercambio.recepcion.lectura_archivo;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class ResumenAdjudicacion extends MSCModelExtend{

	public void execute() throws Exception {

		if (_req.getSession().getAttribute("unidadInversion") != null){
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			ordenDAO.resumenOrdenes(Long.parseLong((String)_req.getSession().getAttribute("unidadInversion")));			
			storeDataSet("resumen", ordenDAO.getDataSet());
		}
	}
}