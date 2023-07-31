package models.intercambio.batch_adjudicacion.consulta.subasta_divisas;

import megasoft.DataSet;
import models.intercambio.consultas.ciclos.ConsultaCicloFilter;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Filter extends ConsultaCicloFilter {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		super.execute();
	}
	
	protected DataSet getUnidadesDeInversion() throws Exception{
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		controlArchivoDAO.listarUnidadesDeInversionPorCiclo(TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS, true);
		return controlArchivoDAO.getDataSet();
	}
	
}
