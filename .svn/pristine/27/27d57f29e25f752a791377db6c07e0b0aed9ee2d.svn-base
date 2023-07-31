package models.intercambio.batch_liquidacion.consulta.subasta;

import megasoft.DataSet;
import models.intercambio.consultas.ciclos.ConsultaCicloBrowse;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
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
		controlArchivoDAO.listarCiclos(_record.getValue("undinv_id"),_record.getValue("fe_desde"), _record.getValue("fe_hasta"), TransaccionNegocio.CICLO_BATCH_SUBASTA, false,true,getNumeroDePagina(),getPageSize());
		this.totalDeRegistros = controlArchivoDAO.getTotalDeRegistros();
		return controlArchivoDAO.getDataSet();
	}
	
	protected DataSet getUnidadInversion() throws Exception{
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarPorId(Long.parseLong(_record.getValue("undinv_id")));
		return unidadInversionDAO.getDataSet();
	}			
	
	protected String getTitulo(){
		return "Intercambio / Cobro Liquidación Batch / Consulta Ciclo Subasta";
	}	
}
