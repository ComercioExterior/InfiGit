package models.intercambio.batch_abono_cuenta_dolares.consulta.clavenet_personal.subasta_divisas_personal;

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
		controlArchivoDAO.listarCiclosAbonoCuentaDolares(_record.getValue("undinv_id"),null,null, TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SUB_DIV_P,true,getNumeroDePagina(),getPageSize());
		this.totalDeRegistros = controlArchivoDAO.getTotalDeRegistros();		
		return controlArchivoDAO.getDataSet();
	}
	
	protected DataSet getUnidadInversion() throws Exception{
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarPorId(Long.parseLong(_record.getValue("undinv_id")));
		return unidadInversionDAO.getDataSet();
	}
	
	protected String getTitulo(){
		return "Intercambio / Abono Cuenta Dolares Subasta Divisas Personal  / Consulta Ciclo Abono Cuenta en Dolares Subasta Divisas canal Clavenet Personal";
	}	
}
