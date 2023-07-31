package models.intercambio.transferencia.generar_archivo_subasta_divisas_personal;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Detalle extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ControlArchivoDAO confiD = new ControlArchivoDAO(_dso);
		//DataSet _unidad_vehiculo = (DataSet)_req.get;//.getAttribute("unidad_vehiculo");
		// Realizar consulta
		//******************************* comentado por Victor Goncalves (cambio 1)********************************************************************
		//confiD.listarDetalles(_unidad_vehiculo.getValue("uniinv_id"),true,getNumeroDePagina(),getPageSize(), ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		
		if (_req.getParameter("undinv_id")!=null&&!_req.getParameter("undinv_id").equals(null)){
		confiD.listarDetalles(_req.getParameter("undinv_id"),true,getNumeroDePagina(),getPageSize(), ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL);
		}
		// registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros(false));
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
}
