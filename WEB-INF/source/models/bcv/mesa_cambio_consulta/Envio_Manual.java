package models.bcv.mesa_cambio_consulta;

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

public class Envio_Manual extends MSCModelExtend {
	private DataSet datosFilter;

	Integer clienteID = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		//int _ordenes1;
		DataSet _totales = new DataSet();
		String idOrdenes  = _req.getParameter("idOrdenes");
		String statusP    = _req.getParameter("statusp");
		String statusE    = _req.getParameter("statusE");
		String Tipo    = _req.getParameter("Tipo");
		String fecha         = _req.getParameter("fecha");

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
			ordenesCrucesDAO.listarOrdenesParaEnviarmanualMenudeoBCV(false, true, getNumeroDePagina(),getPageSize(),true, statusP, "SYSDATE",ConstantesGenerales.ENVIO_MENUDEO_FALTANTES,Tipo,"");
			_ordenes = ordenesCrucesDAO.getDataSet();
//			System.out.println("_ordenes.count()-->"+_ordenes.count());
			//_ordenes1=ordenesCrucesDAO.getDataSet().count();
			int cantidadOperaciones=_ordenes.count();
//			System.out.println("cantidadOperaciones-->"+cantidadOperaciones);
			storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(true));

			storeDataSet("rows", _ordenes);	
			
			_record.setValue("statusp",statusP);
			_record.setValue("statusE",statusE);
			_record.setValue("Tipo",Tipo);
			_record.setValue("fecha",fecha);
//			System.out.println("pasando");
			storeDataSet("record", _record);
			storeDataSet("datosFilter", datosFilter);
//			System.out.println("pasando");
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
//			System.out.println("pasando");
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