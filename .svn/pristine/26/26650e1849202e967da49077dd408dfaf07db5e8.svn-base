package models.bcv.mesa_cambio;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {
	private DataSet datosFilter;
	Integer incluirCliente = null;
	String Status;
	int estatus_n;
	CredencialesDAO credencialesDAO = null;
	DataSet _credenciales = new DataSet();
	String userName = null;
	String clave = null;
	String jornada = null;
	String status =null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		System.out.println("paso mesa de cambio browse");
		DataSet _ordenes = new DataSet();
		DataSet _totales = new DataSet();
		DataSet _cliente = new DataSet();
		

		String statusE = null;
		String fecha   = null;
		String Tipo=null;	
		fecha    = (String) _record.getValue("fecha");
		statusE  =         _record.getValue("statusE");
		Tipo = _record.getValue("Tipo") == null?"": _record.getValue("Tipo");
		System.out.println("statusE-->"+statusE);
		System.out.println("Tipo-->"+Tipo);
		System.out.println("paso mesa de cambio browse1");
		datosFilter = new DataSet();
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		System.out.println("paso mesa de cambio browse2");
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);
		System.out.println("paso mesa de cambio browse3");
		

		
			if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			}else if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_TODAS) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)){
				datosFilter.setValue("boton_procesar", " <button id='btnbloquear' name='btnProcesar' onclick='bloquearlotodo()'>Procesar</button>&nbsp;");
			}else{
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
		    }
		
		System.out.println("paso mesa de cambio browse4");
		String urlInvocacion = _req.getPathInfo();
		//REVISARRRRRRRRRR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MESA_CAMBIO_FILTER.getNombreAccion())){ //DEMANDA Y OFERTA
			
				OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
				//SE CONSULTAN LOS REGISTROS
				ordenesCrucesDAO.listarOrdenesPorEnviarMesaDeCambio(false,true, getNumeroDePagina(),getPageSize(), true, fecha,statusE,Tipo,"");
				_ordenes = ordenesCrucesDAO.getDataSet();

				Double montoOperacion = new Double(0);
				Integer cantidadOperaciones = _ordenes.count();

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
	
@Override
public boolean isValid() throws Exception {
		
	boolean valido = true;
		
	try {

			
	} catch (NumberFormatException e) {
		
		_record.addError("Error: ","Debe Introducir un número valido");
		return false;
	}
			
		return valido;
}
	
	

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getEstatus_n() {
		return estatus_n;
	}

	public void setEstatus_n(int estatus_n) {
		this.estatus_n = estatus_n;
	}
}