package models.intercambio.consultas.recepcion;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.data.Archivo;

public class Detalle extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ControlArchivoDAO confiD = new ControlArchivoDAO(_dso);
		Archivo archivo =new Archivo();		
		
		if (_req.getParameter("undinv_id")!=null)
			archivo.setUnidadInv(Long.parseLong(_req.getParameter("undinv_id")));
		if (_req.getParameter("ordene_veh_col")!=null)
			archivo.setVehiculoId(_req.getParameter("ordene_veh_col"));
		// Realizar consulta
		confiD.listarDetallesRecepcion(archivo,true,getNumeroDePagina(),getPageSize());
		// registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros(false));
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());		
	}
}
