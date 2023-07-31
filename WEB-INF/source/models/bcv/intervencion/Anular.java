package models.bcv.intervencion;

import models.msc_utilitys.MSCModelExtend;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.Utilitario;

public class Anular extends MSCModelExtend {
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
		String idOrdenes  = _req.getParameter("idOrdenes");
		datosFilter = new DataSet();
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("moneda", java.sql.Types.VARCHAR);
		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));

			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");

		String urlInvocacion = _req.getPathInfo();
		
	//	if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_FILTER1.getNombreAccion())){ //DEMANDA Y OFERTA
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false, true, getNumeroDePagina(),getPageSize(), true, statusP, fecha,ConstantesGenerales.ENVIO_MENUDEO,Tipo,"",clienteID,true,"0");
			_ordenes = ordenesCrucesDAO.getDataSet();
			Integer cantidadOperaciones = 0;

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
			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un número valido");
			return false;
		}
			
		return valido;
	}
}