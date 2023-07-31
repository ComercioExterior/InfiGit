package models.detalles_entidades.detalles_requisito;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenRequisitoDAO;

public class DetalleRequisitos extends MSCModelExtend {

	public void execute() throws Exception {
		OrdenRequisitoDAO ordenRequisitoDAO = new OrdenRequisitoDAO(_dso);
		if (_req.getParameter("ord_id") != null) {// viene de consulta
			ordenRequisitoDAO.listarRequisitosPorOrdenEntregados(Integer.parseInt(_req
					.getParameter("ord_id")));
			// Se publica ordene id
			DataSet _ordene_id = new DataSet();
			_ordene_id.append("ordene_id", java.sql.Types.VARCHAR);
			_ordene_id.append("encabezado_req_faltantes", java.sql.Types.VARCHAR);
			_ordene_id.addNew();
			_ordene_id.setValue("ordene_id", _req.getParameter("ord_id"));
			_ordene_id.setValue("encabezado_req_faltantes", "");
			storeDataSet("ordene_id", _ordene_id);
			storeDataSet("requisitos", ordenRequisitoDAO.getDataSet());
			
			ordenRequisitoDAO.listarRequisitosPorOrdenPendientes(Integer.parseInt(_req
					.getParameter("ord_id")));
			
			if(ordenRequisitoDAO.getDataSet().count()>0){
				_ordene_id.setValue("encabezado_req_faltantes", "<p class=\"negrita\">Requisitos Faltantes</p>");
			}
			
			storeDataSet("requisitos_pendientes", ordenRequisitoDAO.getDataSet());
			
			ordenRequisitoDAO.listarObservaciones(Integer.parseInt(_req.getParameter("ord_id")));
			storeDataSet("observaciones", ordenRequisitoDAO.getDataSet());
		}
	}
}
