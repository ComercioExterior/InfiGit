package models.bcv.mesa_cambio_consulta;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;

public class Browse extends MSCModelExtend {
	private DataSet datosFilter;
//	Integer clienteID = null;
	Integer incluirCliente = null;
	String Status;
	int estatus_n;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		System.out.println("paso mesa de cambio browse");
		DataSet _ordenes = new DataSet();
		DataSet _totales = new DataSet();
		DataSet _cliente = new DataSet();
		

//		String statusP = null;
		String statusE = null;
		String fecha   = null;
		String Tipo=null;
//		statusP  =          _record.getValue("statusP");		
		fecha    = (String) _record.getValue("fecha");
//		statusE  =         _record.getValue("statusE");
//		Tipo = _record.getValue("Tipo") == null?"": _record.getValue("Tipo");
//		System.out.println("statusP-->"+statusP);
		System.out.println("statusE-->"+statusE);
//		System.out.println("Tipo-->"+Tipo);
		System.out.println("paso mesa de cambio browse1");
		datosFilter = new DataSet();
//		datosFilter.append("statusp", java.sql.Types.VARCHAR);
//		datosFilter.append("statusE", java.sql.Types.VARCHAR);
//		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
//		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		System.out.println("paso mesa de cambio browse2");
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
//		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
		datosFilter.addNew();
//		datosFilter.setValue("statusp", statusP);
//		datosFilter.setValue("statusE", statusE);
//		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);
//		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
//		System.out.println("paso mesa de cambio browse3");
//		if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){
//			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
//		}else if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_TODAS) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)){
//			datosFilter.setValue("boton_procesar", " <button id='btnbloquear' name='btnProcesar' onclick='bloquearlotodo()'>Procesar</button>&nbsp;");		
//		}else{
//			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
//		}
		
		System.out.println("paso mesa de cambio browse4");
		String urlInvocacion = _req.getPathInfo();
		//REVISARRRRRRRRRR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MESA_CAMBIO_BROWSE.getNombreAccion())){ //DEMANDA Y OFERTA
			
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
//			System.out.println("PASO");
			//SE CONSULTAN LOS REGISTROS
			ordenesCrucesDAO.listarPactosMesaDeCambio(false,true, getNumeroDePagina(),getPageSize(),fecha);
			_ordenes = ordenesCrucesDAO.getDataSet();
		//	while(_ordenes.next()){
			Double montoOperacion = new Double(0);
			Integer cantidadOperaciones = _ordenes.count();
			/*Status   =  _ordenes.getValue("STATUS_ENVIO");
			System.out.println("Status : = " + Status);
			if(Status==ConstantesGenerales.ENVIO_MENUDEO){
				estatus_n=1;
			}*/
//			System.out.println("estatus_n : = " + estatus_n);
			System.out.println("paso mesa de cambio browse5");
			datosFilter.setValue("monto_operacion",String.valueOf(montoOperacion));
			datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
			storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(false));
			System.out.println("paso mesa de cambio browse6");
			storeDataSet("rows", _ordenes);		
			storeDataSet("datosFilter", datosFilter);
			System.out.println("paso mesa de cambio browse7");
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());

		}
	}
//	}
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
//			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));

			
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un número valido");
			return false;
		}
			
		return valido;
	}
}