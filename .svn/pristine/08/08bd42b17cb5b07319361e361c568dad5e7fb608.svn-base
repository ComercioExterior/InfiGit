package models.intercambio.recepcion.lectura_archivo;

import com.bdv.infi.dao.OrdenDAO;
import models.msc_utilitys.MSCModelExtend;

public class Informe extends MSCModelExtend{

	public void execute() throws Exception {

		String unidadInversion = getSessionObject("unidadInversion").toString();
				
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		ordenDAO.listarOrdenesPorUnidadInversion(Long.parseLong(unidadInversion),true,getNumeroDePagina(),getPageSize());
		storeDataSet("table", ordenDAO.getDataSet());
		storeDataSet("total", ordenDAO.getTotalRegistros(false));
		//Arma la sección de paginación
		getSeccionPaginacion(ordenDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
}
