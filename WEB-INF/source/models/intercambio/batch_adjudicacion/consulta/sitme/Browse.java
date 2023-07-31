package models.intercambio.batch_adjudicacion.consulta.sitme;

import megasoft.DataSet;
import models.intercambio.consultas.ciclos.ConsultaCicloBrowse;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Browse extends ConsultaCicloBrowse{
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		super.execute();
	}
	
	protected DataSet getCiclos() throws Exception{
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		controlArchivoDAO.listarCiclos("",_record.getValue("fe_desde"), _record.getValue("fe_hasta"), TransaccionNegocio.CICLO_BATCH_SITME, true,true,getNumeroDePagina(),getPageSize());
		this.totalDeRegistros = controlArchivoDAO.getTotalDeRegistros();
		return controlArchivoDAO.getDataSet();
	}
	
	protected String getTitulo(){
		return "Intercambio / Cobro Adjudicación Batch / Consulta Ciclo Sitme";
	}
}
