package models.configuracion.portafolio_equivalencia;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EquivalenciaPortafolioDAO;


public class Table extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		EquivalenciaPortafolioDAO equivalenciaPortafolioDAO= new EquivalenciaPortafolioDAO(_dso);
		equivalenciaPortafolioDAO.listar(_record.getValue("segmento_descripcion"));
		//registrar los datasets exportados por este modelo
		storeDataSet("table", equivalenciaPortafolioDAO.getDataSet());
		storeDataSet("total_registros", equivalenciaPortafolioDAO.getTotalRegistros());
	}
}