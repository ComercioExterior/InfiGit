package models.bcv.mesa_cambio;

import models.msc_utilitys.MSCModelExtend;
import megasoft.DataSet;
import com.bdv.infi.dao.OrdenesCrucesDAO;

public class Observacion extends MSCModelExtend {
	private DataSet datosFilter;

	Integer clienteID = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
	DataSet _ordenes = new DataSet();
		DataSet _totales = new DataSet();

		String statusP    = _req.getParameter("statusp");
		String statusE    = _req.getParameter("statusE");
		String Tipo    = _req.getParameter("Tipo");
		String fecha         = _req.getParameter("fecha");

		
		datosFilter = new DataSet();
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);

		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("moneda", java.sql.Types.VARCHAR);

		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);
		String urlInvocacion = _req.getPathInfo();

			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarMesaDeCambio(false, true, getNumeroDePagina(),getPageSize(), true, fecha,statusE,Tipo,"");
			_ordenes = ordenesCrucesDAO.getDataSet();
			Integer cantidadOperaciones = _ordenes.count();
			storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(true));
			storeDataSet("rows", _ordenes);	
			
			_record.setValue("statusp",statusP);
			_record.setValue("statusE",statusE);
			_record.setValue("Tipo",Tipo);
			_record.setValue("fecha",fecha);
			
			storeDataSet("record", _record);
			storeDataSet("datosFilter", datosFilter);
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
	}
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {

		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un n�mero valido");
			return false;
		}

			
		return valido;
	}
}