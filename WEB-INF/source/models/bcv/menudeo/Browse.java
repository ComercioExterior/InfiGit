package models.bcv.menudeo;

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
import com.bdv.infi.util.Utilitario;

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
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP  =          _record.getValue("statusP");		
		fecha    = (String) _record.getValue("fecha");
		String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR;
		
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
		
		if(statusP.equals(ConstantesGenerales.SIN_VERIFICAR)){
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
		}else if(statusP.equals(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equals(ConstantesGenerales.VERIFICADA_RECHAZADA)){
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
		}else {
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesadosManual()' >Procesar</button>&nbsp;");
		}
		
		UnidadInversionDAO statusUI = new UnidadInversionDAO(_dso);
		statusUI.consultarStatusUI(idUnidad);
		storeDataSet("statusUI", statusUI.getDataSet());
		String cruceProcesado = "0";
		String estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
		
		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_FILTER.getNombreAccion())){ //DEMANDA
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			
			//SE CONSULTAN LOS REGISTROS
			ordenesCrucesDAO.listarOrdenesPorEnviarBCVMenudeo(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false, idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
			_ordenes = ordenesCrucesDAO.getDataSet();	
			
			//SE CONSULTAN LOS TOTALES
			ordenesCrucesDAO.listarOrdenesPorEnviarBCVMenudeo(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,true, idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
			_totales = ordenesCrucesDAO.getDataSet();
			
			_totales.next();
			
			Double montoOperacion = new Double(0);
			Integer cantidadOperaciones = 0;
			montoOperacion      +=  _totales.getValue("monto_operacion")     ==null ? new Double(0) : Double.parseDouble(_totales.getValue("monto_operacion"));
			cantidadOperaciones +=  _totales.getValue("cantidad_operaciones")  ==null ? 0 : Integer.parseInt(_totales.getValue("cantidad_operaciones"));
			
			datosFilter.setValue("monto_operacion", String.valueOf(montoOperacion));
			datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
			
			storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(false));
			storeDataSet("rows", _ordenes);		
			storeDataSet("datosFilter", datosFilter);
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
		}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_OFER_FILTER.getNombreAccion())) { //OFERTA
			ClienteDAO clienteDAO = new ClienteDAO(_dso);
			String cedRifCliente;
			String tipperId;
			String codigoCliente = "0";

			if(clienteID != 0){		 		
				clienteDAO.listarPorId(clienteID.toString());
				_cliente = clienteDAO.getDataSet();
				//_cliente.next();
				
				if(_cliente.count() > 0){
					tipperId =  _cliente.getValue("TIPPER_ID");
					cedRifCliente = _cliente.getValue("CLIENT_CEDRIF");
					cedRifCliente = Utilitario.completarCaracterIzquierda(cedRifCliente, 13, "0");
					codigoCliente = tipperId + cedRifCliente;
					//se agrega digito verificador enviado por clavenet
					codigoCliente = Utilitario.completarCaracterDerecha(codigoCliente, 15, "0"); 
				}else {
					Logger.error(this, "No se han conseguido los datos del cliente para el filtro en la tabla INFI_TB_201_CTES con el ID "+clienteID+".");
				}
				
			}			
			
			SolicitudesSitmeDAO sitmeDAO = new  SolicitudesSitmeDAO(_dso);
			sitmeDAO.listarOrdenesPorEnviarBCV(incluirCliente,codigoCliente,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha);
			_ordenes = sitmeDAO.getDataSet();			
			
			//SE LLAMA AL METODO PARA CONSULTAR TOTALES
			sitmeDAO.listarOrdenesPorEnviarBCV(incluirCliente,codigoCliente,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,true,idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha);
			_totales = sitmeDAO.getDataSet();	
			
			_totales.next();
			
			Double montoOperacion = new Double(0);
			Integer cantidadOperaciones = 0;
			montoOperacion      +=  _totales.getValue("monto_operacion")     ==null ? new Double(0) : Double.parseDouble(_totales.getValue("monto_operacion"));
			cantidadOperaciones +=  _totales.getValue("cantidad_operaciones")  ==null ? 0 : Integer.parseInt(_totales.getValue("cantidad_operaciones"));
			
			datosFilter.setValue("monto_operacion", String.valueOf(montoOperacion));
			datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
			
			storeDataSet("datos", sitmeDAO.getTotalRegistros(false));
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
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un n�mero valido");
			return false;
		}
		
		
		if(tasaMinima > tasaMaxima && tasaMaxima != 0){
			_record.addError(
					"Error: ",
					"La tasa m�nima no puede ser mayor a la tasa m�xima ");
			valido = false;
		}
		
		if(montoMinimo > montoMaximo && montoMaximo != 0 ){
			_record.addError(
					"Error: ",
					"El monto m�nimo no puede ser mayor al monto m�ximo ");
			valido = false;
		}
			
		return valido;
	}
}