package models.intercambio.recepcion.cruce_sicad_II.consulta_cruce;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {
	
	private DataSet datosFilter;
	private String idProducto;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		long idUnidad;
		long idCliente;
		String idOrden;
		String idEjecucion;
		String status = null;
		String statusP = null;
		String statusOrden = null;
		String tipoProd = null;
		String indTitulo = null;
		
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		idCliente = Long.valueOf(_record.getValue("client_id")==null?"0":_record.getValue("client_id"));
		idEjecucion = _record.getValue("ejecucion_id");
		status = _record.getValue("status");
		idOrden = _record.getValue("orden_id");
		statusP = _record.getValue("statusP");
		tipoProd = _record.getValue("tipo_prod");
		indTitulo = _record.getValue("ind_titulo");
		
		OrdenesCrucesDAO consCruces = new OrdenesCrucesDAO(_dso);
		
		consCruces.consultarCruces(idUnidad, status, idCliente, idOrden, statusP, idEjecucion, indTitulo, true,getNumeroDePagina(),getPageSize());
		
		DataSet _cruces = new DataSet();
		
		_cruces = consCruces.getDataSet();
		
		UnidadInversionDAO statusUI = new UnidadInversionDAO(_dso);
				
		statusUI.consultarStatusUI(idUnidad);
		storeDataSet("statusUI", statusUI.getDataSet());
		
		DataSet reConsulta = new DataSet();
		reConsulta.addNew();
		
		reConsulta.append("idUnidadF", java.sql.Types.VARCHAR);
		reConsulta.setValue("idUnidadF", _record.getValue("ui_id"));
		reConsulta.append("idClienteF", java.sql.Types.VARCHAR);
		reConsulta.setValue("idClienteF", _record.getValue("client_id"));
		reConsulta.append("idOrdenF", java.sql.Types.VARCHAR);
		reConsulta.setValue("idOrdenF", idOrden);
		reConsulta.append("idEjecucionF", java.sql.Types.VARCHAR);
		reConsulta.setValue("idEjecucionF", _record.getValue("ejecucion_id"));
		reConsulta.append("statusF", java.sql.Types.VARCHAR);
		reConsulta.setValue("statusF", status);
		reConsulta.append("statusP", java.sql.Types.VARCHAR);
		reConsulta.setValue("statusP", statusP);
		reConsulta.append("indTitulo", java.sql.Types.VARCHAR);
		reConsulta.setValue("indTitulo", indTitulo);

		//storeDataSet("consulta", reConsulta);
		
		
		//Se publica el dataset de la consulta, del Path a mostrar y el tipoProducto
		//getSeccionPaginacion(consCruces.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
		
		//storeDataSet("datosFilter", datosFilter);
		
		datosFilter = new DataSet();
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("tipo_prod", tipoProd);
		
		
		getSeccionPaginacion(consCruces.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
		storeDataSet("datos", consCruces.getTotalRegistros(false));
		storeDataSet("rows", _cruces);
		//storeDataSet("total", consCruces.getTotalRegistros(false));
		storeDataSet("datosFilter", datosFilter);
		
		
		//storeDataSet("filtro_ConsultaCruces", reConsulta);
		
		//Se carga la consulta de cruces en sesion para hacer la exportacion
		//_req.getSession().setAttribute("exportar_excel",_cruces);
		setSessionDataSet("filtro_ConsultaCruces", reConsulta);
	
	}
}