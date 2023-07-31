package models.intercambio.transferencia.generar_archivo;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Detalle extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ControlArchivoDAO confiD = new ControlArchivoDAO(_dso);
		DataSet _unidad_vehiculo = (DataSet)_req.getSession().getAttribute("unidad_vehiculo");
		// Realizar consulta
		confiD.listarDetalles(_unidad_vehiculo.getValue("uniinv_id"),true,getNumeroDePagina(),getPageSize());
		// registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros(false));
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
}
