package models.bcv.alto_valor;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OfertaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Browse extends MSCModelExtend {
	private DataSet datosFilter;
	Integer tasaMinima = null;
	Integer tasaMaxima = null;
	Integer montoMinimo = null;
	Integer montoMaximo = null;
	Integer clienteID = null;
	Integer incluirCliente = null;
	String  origen = null;
	String estatusCruce = null;
	Integer tipoOperacion = null;
	Integer blotterID = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	@SuppressWarnings("null")
	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		DataSet _totales = new DataSet();
		long idUnidad;
		String statusP = null;
		String fecha   = null;
		
		
			
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP  =          _record.getValue("statusP") == null?"": _record.getValue("statusP");		
		fecha    = (String) _record.getValue("fecha");
		
		
		String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
		String tipoProductoId = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
		
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
		datosFilter.append("origen", java.sql.Types.VARCHAR);
		datosFilter.append("estatus_cruce", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("bloter_id", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("ui_id", String.valueOf(idUnidad));
		datosFilter.setValue("tasa_minima", String.valueOf(tasaMinima));
		datosFilter.setValue("tasa_maxima", String.valueOf(tasaMaxima));
		datosFilter.setValue("monto_minimo", String.valueOf(montoMinimo));
		datosFilter.setValue("monto_maximo", String.valueOf(montoMaximo));
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
		datosFilter.setValue("incluir_cliente",String.valueOf(incluirCliente));
		datosFilter.setValue("origen", String.valueOf(origen));
		datosFilter.setValue("estatus_cruce", String.valueOf(estatusCruce));
		datosFilter.setValue("tipo_operacion", String.valueOf(tipoOperacion));
		datosFilter.setValue("bloter_id", String.valueOf(blotterID));
		
		String urlInvocacion = _req.getPathInfo();
		Integer ofertaAnulacion  = Integer.parseInt(ConstantesGenerales.OFERTA_ANULACION);
		Integer demandaAnulacion = Integer.parseInt(ConstantesGenerales.DEMANDA_ANULACION);
		
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ANULAR_OFER_DEMAN_BROWSE.getNombreAccion())){ //ANULAR DEMANDA U OFERTA
			if(statusP.equals(ConstantesGenerales.VERIFICADA_APROBADA) && tipoOperacion != 0){ //SE VA A ANULAR UNA OPERACION
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			}
		} else {
			if(statusP.equals(ConstantesGenerales.SIN_VERIFICAR)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			}else if(statusP.equals(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equals(ConstantesGenerales.VERIFICADA_RECHAZADA)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
			}else if(statusP.equals(ConstantesGenerales.TODOS)){
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesadosTodos()' >Procesar</button>&nbsp;");
			}
			else {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesadosManual()' >Procesar</button>&nbsp;");
			}
		}
		
		UnidadInversionDAO statusUI = new UnidadInversionDAO(_dso);
		statusUI.consultarStatusUI(idUnidad);
		storeDataSet("statusUI", statusUI.getDataSet());
		
		
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_DEMAN_FILTER.getNombreAccion()) || tipoOperacion == demandaAnulacion){ //DEMANDA ALTO VALOR
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			
			//SE LLAMA AL METODO PARA CONSULTAR REGISTROS
			ordenDAO.listarOrdenesPorEnviarBCV(tipoProductoId,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,tipoNegocio,idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, blotterID);
			_ordenes = ordenDAO.getDataSet();			
			
			//SE LLAMA AL METODO PARA CONSULTAR TOTALES
			ordenDAO.listarOrdenesPorEnviarBCV(tipoProductoId,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,true,tipoNegocio,idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, blotterID);
			_totales = ordenDAO.getDataSet();	
			
			Double montoOperacion = new Double(0);
			Integer cantidadOperaciones = 0;
			
			//SE SUMAN LOS RESULTADOS PORQUE SE HACE UN UNION 
			while (_totales.next()) {
				montoOperacion      +=  _totales.getValue("monto_operacion")     ==null ? new Double(0) : Double.parseDouble(_totales.getValue("monto_operacion"));
				cantidadOperaciones +=  _totales.getValue("cantidad_operaciones")  ==null ? 0 : Integer.parseInt(_totales.getValue("cantidad_operaciones"));
			}
			
			datosFilter.setValue("monto_operacion", String.valueOf(montoOperacion));
			datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
			
			storeDataSet("datos", ordenDAO.getTotalRegistros(false));
			storeDataSet("rows", _ordenes);		
			storeDataSet("datosFilter", datosFilter);
			storeDataSet("totales", _totales);
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
		}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_OFER_FILTER.getNombreAccion()) || tipoOperacion == ofertaAnulacion) { //OFERTA ALTO VALOR
			Integer cedRifCliente;
			ClienteDAO clienteDAO = new ClienteDAO(_dso);
			OfertaDAO  ofertaDAO  = new OfertaDAO(_dso);
			
			if(clienteID != 0){
				cedRifCliente = Integer.parseInt(clienteDAO.buscarDatosPorIdCliente(String.valueOf(clienteID)));
			}else {
				cedRifCliente = 0;
			}
			
			//SE LLAMA AL METODO PARA CONSULTAR REGISTROS
			ofertaDAO.listarOrdenesPorEnviarBCV(incluirCliente,cedRifCliente, tasaMinima,tasaMaxima,montoMinimo,montoMaximo, false,true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, origen, estatusCruce);
			_ordenes = ofertaDAO.getDataSet();			
			
			//SE LLAMA AL METODO PARA CONSULTAR TOTALES
			ofertaDAO.listarOrdenesPorEnviarBCV(incluirCliente,cedRifCliente,tasaMinima,tasaMaxima,montoMinimo,montoMaximo, true,false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, origen,estatusCruce);
			_totales = ofertaDAO.getDataSet();			
			
			_totales.next();
			
			String montoOperacion="";
			Integer cantidadOperaciones = 0;
			montoOperacion      +=  _totales.getValue("monto_operacion")     ==null ? new Double(0) : _totales.getValue("monto_operacion");
			cantidadOperaciones +=  _totales.getValue("cantidad_operaciones")  ==null ? 0 : Integer.parseInt(_totales.getValue("cantidad_operaciones"));
		
			
			datosFilter.setValue("monto_operacion", String.valueOf(montoOperacion));
			datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
			
			storeDataSet("datos", ofertaDAO.getTotalRegistros(false));
			storeDataSet("rows", _ordenes);		
			storeDataSet("datosFilter", datosFilter);
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
		}
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
			origen         = _record.getValue("origen")== null ? "" : _record.getValue("origen");
			estatusCruce   = _record.getValue("estatus_cruce")== null ? "" : _record.getValue("estatus_cruce");
			tipoOperacion  = Integer.parseInt(_record.getValue("tipo_operacion")== null ? "0" : _record.getValue("tipo_operacion"));
			blotterID      = Integer.parseInt(_record.getValue("bloter_id")== null ? "0" : _record.getValue("bloter_id"));
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