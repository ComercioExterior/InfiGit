package models.bcv.taquilla_aereopuerto;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;


public class Browse extends MSCModelExtend {
	private DataSet datosFilter;
	Integer tasaMinima = null;
	Integer tasaMaxima = null;
	Integer montoMinimo = null;
	Integer montoMaximo = null;
	Integer clienteID = null;
	Integer incluirCliente = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		DataSet _totales = new DataSet();
		DataSet _cliente = new DataSet();
		
		long idUnidad;
		String statusP = null;
		String fecha   = null;
		String tipoEnvio = null;
		boolean anulacionEfectivo=true;
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP  =          _record.getValue("statusP");		
		fecha    = (String) _record.getValue("fecha");
		tipoEnvio    = (String) _record.getValue("tipoEnvio");
		String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR_TAQUILLA;
		
		if(!tipoEnvio.equalsIgnoreCase("anulacion")){
			anulacionEfectivo=false;
		}
		
		
		datosFilter = new DataSet();
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("ui_id", java.sql.Types.VARCHAR);
		datosFilter.append("tasa_minima", java.sql.Types.VARCHAR);
		datosFilter.append("tasa_maxima", java.sql.Types.VARCHAR);
		datosFilter.append("monto_minimo", java.sql.Types.VARCHAR);
		datosFilter.append("monto_maximo", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
		datosFilter.append("incluir_cliente", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("ui_id", String.valueOf(idUnidad));
		datosFilter.setValue("tasa_minima", String.valueOf(tasaMinima));
		datosFilter.setValue("tasa_maxima", String.valueOf(tasaMaxima));
		datosFilter.setValue("monto_minimo", String.valueOf(montoMinimo));
		datosFilter.setValue("monto_maximo", String.valueOf(montoMaximo));
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
		datosFilter.setValue("incluir_cliente", String.valueOf(incluirCliente));
		
		//NM25287 TTS-504 Modificación para usar en anulacion de operaciones de taquilla
		if(anulacionEfectivo){
			if(statusP.equals(ConstantesGenerales.VERIFICADA_APROBADA)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			}else if(statusP.equals(ConstantesGenerales.ANULADA_BCV) || statusP.equals(ConstantesGenerales.VERIFICADA_RECHAZADA)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
			}
		}else{
			if(statusP.equals(ConstantesGenerales.SIN_VERIFICAR)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			}else if(statusP.equals(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equals(ConstantesGenerales.VERIFICADA_RECHAZADA)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
			}/*else {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesadosManual()' >Procesar</button>&nbsp;");
			}*/
		}
				
		UnidadInversionDAO statusUI = new UnidadInversionDAO(_dso);
		statusUI.consultarStatusUI(idUnidad);
		storeDataSet("statusUI", statusUI.getDataSet());
		String cruceProcesado = "0";
		String estatusOrdenINFI = StatusOrden.CRUZADA;
		//TODO Borrar Ejecuciond de Prueba
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		
		
		//ordenesCrucesDAO.listarOrdenesPorEnviarBCVMenudeo(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false, idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
		ordenDAO.listarOrdenesPorEnviarBCVPorVentaTaquilla(tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false, idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI,anulacionEfectivo);
		_ordenes = ordenDAO.getDataSet();
		
		int cantidadOperaciones=_ordenes.count();
		Double montoTotalOperacion = new Double(0);
		
		storeDataSet("rows", _ordenes);
		//TODO Borrar Ejecuciond de Prueba
		if(_ordenes.count()>0){		
			_ordenes.first();		
			while(_ordenes.next()){			
				montoTotalOperacion      +=  _ordenes.getValue("monto_adj")==null ? new Double(0) : Double.parseDouble(_ordenes.getValue("monto_adj"));		
			}	
		}
				
		datosFilter.setValue("cantidad_operaciones",String.valueOf(cantidadOperaciones));
		datosFilter.setValue("monto_operacion",String.valueOf(montoTotalOperacion));
		
		getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
		
		storeDataSet("datosFilter", datosFilter);
	}
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
			tasaMinima     = Integer.parseInt(_record.getValue("tasa_minima")== null ? "0" : _record.getValue("tasa_minima"));
			tasaMaxima     = Integer.parseInt(_record.getValue("tasa_maxima")== null ? "0" : _record.getValue("tasa_maxima") );
			montoMinimo    = Integer.parseInt(_record.getValue("monto_minimo")== null ? "0" : _record.getValue("monto_minimo"));
			montoMaximo    = Integer.parseInt(_record.getValue("monto_maximo")== null ? "0" : _record.getValue("monto_maximo"));
			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));
			incluirCliente = Integer.parseInt(_record.getValue("incluir_cliente")== null ? "0" : _record.getValue("incluir_cliente"));
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un número valido");
			return false;
		}
		
		
		if(tasaMinima > tasaMaxima && tasaMaxima != 0){
			_record.addError(
					"Error: ",
					"La tasa mínima no puede ser mayor a la tasa máxima ");
			valido = false;
		}
		
		if(montoMinimo > montoMaximo && montoMaximo != 0 ){
			_record.addError(
					"Error: ",
					"El monto mínimo no puede ser mayor al monto máximo ");
			valido = false;
		}
			
		return valido;
	}
}