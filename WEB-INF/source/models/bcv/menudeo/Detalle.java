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

public class Detalle extends MSCModelExtend {
	private DataSet datosFilter;
	/*Integer tasaMinima = null;
	Integer tasaMaxima = null;
	Integer montoMinimo = null;
	Integer montoMaximo = null;*/
	Integer clienteID = null;
	//Integer incluirCliente = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		DataSet _totales = new DataSet();
		DataSet _cliente = new DataSet();
		
	//	long idUnidad;
		String statusP = null;
		String statusE = null;
		String fecha   = null;
		String Tipo=null;
	//	idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP  =          _record.getValue("statusP");		
		fecha    = (String) _record.getValue("fecha");
		statusE  =         _record.getValue("statusE");
		Tipo  =         _record.getValue("Tipo") == null?"": _record.getValue("Tipo");	
		//String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR;
		
		datosFilter = new DataSet();
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
	//	datosFilter.append("statuse", java.sql.Types.VARCHAR);
	//	datosFilter.append("ui_id", java.sql.Types.VARCHAR);
	//	datosFilter.append("tasa_minima", java.sql.Types.VARCHAR);
	//	datosFilter.append("tasa_maxima", java.sql.Types.VARCHAR);
	//	datosFilter.append("monto_minimo", java.sql.Types.VARCHAR);
	//	datosFilter.append("monto_maximo", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
	//	datosFilter.append("incluir_cliente", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);
	//	datosFilter.setValue("ui_id", String.valueOf(idUnidad));
	//	datosFilter.setValue("tasa_minima", String.valueOf(tasaMinima));
	//	datosFilter.setValue("tasa_maxima", String.valueOf(tasaMaxima));
	//	datosFilter.setValue("monto_minimo", String.valueOf(montoMinimo));
	//	datosFilter.setValue("monto_maximo", String.valueOf(montoMaximo));
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
	//	datosFilter.setValue("incluir_cliente", String.valueOf(incluirCliente));
		
		if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
		}else{
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
			
		}
		
	//	UnidadInversionDAO statusUI = new UnidadInversionDAO(_dso);
	//	statusUI.consultarStatusUI(idUnidad);
	//	storeDataSet("statusUI", statusUI.getDataSet());
	//	String cruceProcesado = "0";
	//	String estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
	//	String estatusENVIO= ConstantesGenerales.STATUS_ENVIADAS;
		System.out.println("statusP---->"+statusP);
		System.out.println("statusE---->"+statusE);
		System.out.println("tipo movimiento---->"+Tipo);
		System.out.println("getNumeroDePagina()---->"+getNumeroDePagina());
		System.out.println("getPageSize()---->"+getPageSize());
		String urlInvocacion = _req.getPathInfo();
		//REVISARRRRRRRRRR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_FILTER1.getNombreAccion())){ //DEMANDA Y OFERTA
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			System.out.println("PASO");
			//SE CONSULTAN LOS REGISTROS
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false,true, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
			_ordenes = ordenesCrucesDAO.getDataSet();
			System.out.println(_ordenes.count());
			//			String prueba = _ordenes.getValue("ID_OPER");
//			System.out.println("prueba--->"+prueba);
			
			
			//SE CONSULTAN LOS TOTALES
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(true, false, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
			_totales = ordenesCrucesDAO.getDataSet();
			
			System.out.println(_totales.count());
			System.out.println("despues listarOrdenesPorEnviarMenudeoBCV1");
			_totales.next();
			System.out.println("despues _totales");
			Double montoOperacion = new Double(0);
			Integer cantidadOperaciones = 0;
			montoOperacion      +=  _totales.getValue("monto_operacion")     ==null ? new Double(0) : Double.parseDouble(_totales.getValue("monto_operacion"));
			cantidadOperaciones +=  _totales.getValue("cantidad_operaciones")  ==null ? 0 : Integer.parseInt(_totales.getValue("cantidad_operaciones"));
			System.out.println("monto_operacion-->"+montoOperacion);
			System.out.println("monto_operacion-->"+cantidadOperaciones);
			datosFilter.setValue("monto_operacion",String.valueOf(montoOperacion));
			datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
			System.out.println("datosFilter--->"+datosFilter);
			System.out.println("_ordenes--->"+_ordenes);
			System.out.println("ordenesCrucesDAO.getTotalRegistros(false)--->"+ordenesCrucesDAO.getTotalRegistros(false));
			storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(false));
			System.out.println("despues 1");
			storeDataSet("rows", _ordenes);		
			storeDataSet("datosFilter", datosFilter);
			System.out.println("despues 2");
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
			System.out.println("despues 3");
		}/*else{ //OFERTA
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
			
			//storeDataSet("datos", sitmeDAO.getTotalRegistros(false));
			storeDataSet("rows", _ordenes);		
			storeDataSet("datosFilter", datosFilter);
			getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
		}*/
	}
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
			/*tasaMinima     = Integer.parseInt(_record.getValue("tasa_minima")== null ? "0" : _record.getValue("tasa_minima"));
			tasaMaxima     = Integer.parseInt(_record.getValue("tasa_maxima")== null ? "0" : _record.getValue("tasa_maxima") );
			montoMinimo    = Integer.parseInt(_record.getValue("monto_minimo")== null ? "0" : _record.getValue("monto_minimo"));
			montoMaximo    = Integer.parseInt(_record.getValue("monto_maximo")== null ? "0" : _record.getValue("monto_maximo"));*/
			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));

			//incluirCliente = Integer.parseInt(_record.getValue("incluir_cliente")== null ? "0" : _record.getValue("incluir_cliente"));
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un n�mero valido");
			return false;
		}
		
		
	/*	if(tasaMinima > tasaMaxima && tasaMaxima != 0){
			_record.addError(
					"Error: ",
					"La tasa m�nima no puede ser mayor a la tasa m�xima ");
			valido = false;
		}*/
		
	/*	if(montoMinimo > montoMaximo && montoMaximo != 0 ){
			_record.addError(
					"Error: ",
					"El monto m�nimo no puede ser mayor al monto m�ximo ");
			valido = false;
		}*/
			
		return valido;
	}
}