package models.intercambio.batch_adjudicacion.consulta.clavenet_personal.subasta_divisas_personal;

import megasoft.DataSet;
import models.intercambio.consultas.ciclos.ConsultaCicloPorParametroBrowse;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Browse extends ConsultaCicloPorParametroBrowse{
	

	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		this.setNombreArchivo("ciclos.html");
		this.setRutaHtml("../../../../consultas/ciclos/");
		super.execute();
	}
	
	protected DataSet getCiclos() throws Exception{
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		controlArchivoDAO.listarCiclos(_record.getValue("undinv_id"),null,null, true,true,getNumeroDePagina(),getPageSize(), TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL);
		this.totalDeRegistros = controlArchivoDAO.getTotalDeRegistros();		
		return controlArchivoDAO.getDataSet();
	}
	
	protected DataSet getUnidadInversion() throws Exception{
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarPorId(Long.parseLong(_record.getValue("undinv_id")));
		return unidadInversionDAO.getDataSet();
	}
	
	protected String getTitulo(){
		return "Intercambio / Cobro Adjudicación Batch / Consulta Ciclo SICAD II";
	}	
}
