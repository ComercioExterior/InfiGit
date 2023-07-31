package models.intercambio.certificados_ORO.operaciones_demandas.operaciones_abonos.consulta_ciclo;

import megasoft.DataSet;
import models.intercambio.consultas.ciclos.ConsultaCicloPorParametroBrowse;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaExtranjera;
import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaLocal;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Browse extends ConsultaCicloPorParametroBrowse{
	

	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversi�n
	 */
	public void execute() throws Exception {
		this.setNombreArchivo("ciclos.html");
		this.setRutaHtml("../../../../consultas/ciclos/");
		super.execute();
	}
	
	protected DataSet getCiclos() throws Exception{
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);//TODO Colocar el tipo de ciclo correcto en el llamado al medoto controlArchivoDAO.listarCiclos 
		controlArchivoDAO.listarCiclos(_record.getValue("undinv_id"),null,null, true,true,getNumeroDePagina(),getPageSize(), ControlProcesosOpsMonedaExtranjera.PROCESO_DEMANDA_ABONO_ORO.getCiclo());
		this.totalDeRegistros = controlArchivoDAO.getTotalDeRegistros();		
		return controlArchivoDAO.getDataSet();
	}
	
	protected DataSet getUnidadInversion() throws Exception{
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarPorId(Long.parseLong(_record.getValue("undinv_id")));
		return unidadInversionDAO.getDataSet();
	}
	
	protected String getTitulo(){
		return "Intercambio / Operaciones ORO / Operaciones Demanda / Abono Operaciones / Consulta de Ciclo ORO";
	}	
}
