package models.intercambio.consultas.envio;

import megasoft.DataSet;
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
		String ejecucion = null;
		
		if(_record.getValue("ejecucion_id")!=null)
			_req.getSession().setAttribute("ejecucion",_record.getValue("ejecucion_id"));
		 
		ejecucion = _req.getSession().getAttribute("ejecucion").toString();
		archivo.setIdEjecucion(Long.parseLong(ejecucion));
		// Realizar consulta
		confiD.listarDetallesEnvio(archivo,true,getNumeroDePagina(),getPageSize());
		// registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros(false));
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());		
	}
}
