package models.oficina_comercial;

import com.bdv.infi.dao.OficinaDAO;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Oficinas con filtros dinamicos 
 * @author Angel
 *
 */
public class OficinaBrowse extends MSCModelExtend {

	
	@Override
	public void execute() throws Exception {
		getDataSetFromRequest();
		
		
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		oficinaDAO.Dinamico = (String) (_req.getParameter("dinamico") == null ? _req.getSession().getAttribute("dina") : _req.getParameter("dinamico"));
		
		if (oficinaDAO.Dinamico == null || oficinaDAO.Dinamico == "") {
			oficinaDAO.listarPorParametros(getNumeroDePagina(), getPageSize());
			
		} else {
			_req.getSession().setAttribute("dina", oficinaDAO.Dinamico);
			oficinaDAO.listarPorParametrosDinamicos(getNumeroDePagina(), getPageSize());

		}
		
		storeDataSet("oficinas", oficinaDAO.getDataSet());
		storeDataSet("datos", oficinaDAO.getTotalRegistros(false));
		
		DataSet datosFilter = new DataSet();
		datosFilter.append("totales", java.sql.Types.VARCHAR);
		datosFilter.append("activa", java.sql.Types.VARCHAR);
		datosFilter.append("inactiva", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		
		oficinaDAO.listarTotalesOficinaComercial();
		oficinaDAO.getDataSet().next();
		datosFilter.setValue("totales", String.valueOf(oficinaDAO.getDataSet().getValue("total")));
		
		oficinaDAO.listarTotalesOficinaComercialActiva();
		oficinaDAO.getDataSet().next();
		datosFilter.setValue("activa", String.valueOf(oficinaDAO.getDataSet().getValue("activa")));
		
		oficinaDAO.listarTotalesOficinaComercialInactiva();
		oficinaDAO.getDataSet().next();
		datosFilter.setValue("inactiva", String.valueOf(oficinaDAO.getDataSet().getValue("inactiva")));
		
		storeDataSet("datosFilter", datosFilter);
		getSeccionPaginacion(oficinaDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}

}
