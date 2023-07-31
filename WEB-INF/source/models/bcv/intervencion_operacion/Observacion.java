package models.bcv.intervencion_operacion;

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

		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);

		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
		String urlInvocacion = _req.getPathInfo();
		//REVISARRRRRRRRRR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	//	if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_FILTER1.getNombreAccion())){ //DEMANDA Y OFERTA
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			//SE CONSULTAN LOS TOTALES
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false, true, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
			_ordenes = ordenesCrucesDAO.getDataSet();


			Integer cantidadOperaciones = _ordenes.count();

//			System.out.println("datosFilter--->"+datosFilter);
//			System.out.println("_ordenes--->"+_ordenes);
//			System.out.println("ordenesCrucesDAO.getTotalRegistros(false)--->"+ordenesCrucesDAO.getTotalRegistros(false));
		storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(true));
//			System.out.println("despues 1");
			storeDataSet("rows", _ordenes);	
			
			_record.setValue("statusp",statusP);
			_record.setValue("statusE",statusE);
			_record.setValue("Tipo",Tipo);
			_record.setValue("fecha",fecha);
			
			storeDataSet("record", _record);
			storeDataSet("datosFilter", datosFilter);
//			System.out.println("despues 2");
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
//			System.out.println("despues 3");
	
	}
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
	
			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un n�mero valido");
			return false;
		}

			
		return valido;
	}
}