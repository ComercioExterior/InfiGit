package models.configuracion.cuentasCliente;
import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CiudadDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CuentasUsoDAO;
import com.bdv.infi.dao.EstadoDAO;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

public class Edit extends MSCModelExtend {
	
	private DataSet mensajes = null;
	
	/**
	 * ArrayList que contiene las cuentas asociadas a un cliente
	 */
	ArrayList<Cuenta> cuentasArrayList = new ArrayList<Cuenta>();
	/**
	 * DataSet que se publicara con las cuentas asociadas al cliente
	 */
	DataSet _cuentas = new DataSet();
	
	public void execute()throws Exception{
		
		DataSet datosCuenta = new DataSet();
		CuentasUsoDAO cuentasUsoDAO=new CuentasUsoDAO(_dso);
		GestionPagoDAO intruccionDAO= new GestionPagoDAO(_dso);
		EstadoDAO estadoDAO= new EstadoDAO(_dso);
		CiudadDAO ciudadDAO=new CiudadDAO(_dso);
		ClienteCuentasDAO clienteCuentasDAO	=new ClienteCuentasDAO(_dso);
		String tipoInstruccionId=null;
		
		datosCuenta.append("iban_cta_europea", java.sql.Types.VARCHAR);
		datosCuenta.append("intermediario", java.sql.Types.VARCHAR);
		datosCuenta.addNew();
		datosCuenta.setValue("intermediario","0");
		
		/*
		 * Dataset de error
		 */
		mensajes = new DataSet();
		mensajes.append("mensaje_error", java.sql.Types.VARCHAR);
		mensajes.addNew();
				
		/*Creacion de Dataset para Control de Cambio*/
		DataSet _controlCambio=new DataSet();
		_controlCambio.append("control_cambio", java.sql.Types.VARCHAR);
		String control_cambio=ParametrosDAO.listarParametros(ParametrosSistema.CONTROL_DE_CAMBIO,_dso);
		_controlCambio.addNew();
		_controlCambio.setValue("control_cambio",control_cambio);
		storeDataSet("_controlCambio", _controlCambio);
	
		DataSet req=getDataSetFromRequest();
		req.first();
		req.next();		
	
		clienteCuentasDAO.browseClienteCuentas(null,null,null,_req.getParameter("cuentaId"));
		
		if(clienteCuentasDAO.getDataSet().next()){
			
			tipoInstruccionId=clienteCuentasDAO.getDataSet().getValue("tipo_instruccion_id");
			if(tipoInstruccionId!=null && tipoInstruccionId.equals(String.valueOf(TipoInstruccion.CUENTA_SWIFT))){
				//si el numero de cuenta contiene la cadena IBAN que indica cuenta europea
				/*if(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_swift").indexOf("IBAN")>-1){
					
					datosCuenta.setValue("iban_cta_europea", "1");//cuenta iban
					int longIndicIBAN = ConstantesGenerales.INDICADOR_IBAN.length();
					
					String numeroCuentaInternacional = clienteCuentasDAO.getDataSet().getValue("ctecta_numero");
					//numeroCuentaInternacional = numeroCuentaInternacional.substring(ConstantesGenerales.INDICADOR_IBAN.length());//obtener numero de cuenta menos la cadena "/IBAN"
					clienteCuentasDAO.getDataSet().setValue("ctecta_numero", numeroCuentaInternacional);
				}*/
				
				//Si hay banco intermediario CTECTA_BCOINT_BCO
				if(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bco")!=null && clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bco").length()>1){
					datosCuenta.setValue("intermediario","1");
				}
			}

			//si es una cuenta nacional o operacion de cambio
			if(tipoInstruccionId!=null && (tipoInstruccionId.equals(String.valueOf(TipoInstruccion.CUENTA_NACIONAL))
				|| tipoInstruccionId.equals(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO))|| tipoInstruccionId.equals(String.valueOf(TipoInstruccion.CUENTA_NACIONAL_DOLARES)))){
				
				ClienteDAO clienteDAO = new ClienteDAO(_dso);				
				try {	
					String cliente = _req.getParameter("clientId");				
					String cedula = "";
					String tipoPersona = "";
					
					clienteDAO.listarPorId(Long.parseLong(cliente));
					Cliente clienteObj = (Cliente) clienteDAO.moveNext();					
					cedula = String.valueOf(clienteObj.getRifCedula());
					tipoPersona = String.valueOf(clienteObj.getTipoPersona());	
					
					if(tipoInstruccionId.equals(String.valueOf(TipoInstruccion.CUENTA_NACIONAL_DOLARES))){
						cuentasArrayList = clienteCuentasDAO.buscarCuentasEnDolaresAltair(cedula, tipoPersona, _app, _req.getRemoteAddr(), getUserName());
					}else{
						cuentasArrayList = clienteCuentasDAO.buscarCuentasAltair(cedula, tipoPersona, _app, _req.getRemoteAddr(), getUserName());
					}
						
				
					/*
					 * Instancia de object ManejadorDeClientes para acceder al metodo cargarDataSet
					 */
					CredencialesDeUsuario credencialesDeUsuario = new CredencialesDeUsuario();
					credencialesDeUsuario.setClaveSecreta("claveSecreta");
					credencialesDeUsuario.setNombreDeUsuario("nombreDeUsuario");
				
					ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app,credencialesDeUsuario);
				
					_cuentas = manejadorDeClientes.cargarDataSet(cuentasArrayList);
					mensajes.setValue("mensaje_error","");
					storeDataSet("mensajes", mensajes);
					//_req.getSession().setAttribute("mensajes", mensajes);					
					
				} catch(Exception e){
					//crearDataSetCuentasVacio();
					e.printStackTrace();
					mensajes.setValue("mensaje_error", "Error consultando las cuentas del cliente en arquitectura extendida");
					//_req.getSession().setAttribute("mensajes", mensajes);				
					
				}
				catch(Throwable t){
					//crearDataSetCuentasVacio();
					t.printStackTrace();
					mensajes.setValue("mensaje_error", "Error consultando las cuentas del cliente en arquitectura extendida");
					//_req.getSession().setAttribute("mensajes", mensajes);
					
				} finally{
					clienteDAO.closeResources();
					clienteDAO.cerrarConexion();
				}
			}
			
		}
		
		cuentasUsoDAO.listarUsuoCuenta();
		intruccionDAO.listarInstruccion(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
		//intruccionDAO.listarInstruccion();
		estadoDAO.consultarEstados();	
		if(clienteCuentasDAO.getDataSet().getValue("cod_estado_origen")!=null&&clienteCuentasDAO.getDataSet().getValue("cod_estado_origen").length()>0){
			ciudadDAO.consultarCiudades(clienteCuentasDAO.getDataSet().getValue("cod_estado_origen"));
		}
				
		storeDataSet("mensajes", mensajes);
		storeDataSet("_cuentas",_cuentas);		
		storeDataSet("datos", clienteCuentasDAO.getDataSet());
		storeDataSet("tipos",cuentasUsoDAO.getDataSet());
		storeDataSet("cuenta", intruccionDAO.getDataSet());
		storeDataSet("datos_cuenta", datosCuenta);
		storeDataSet("cuenta", intruccionDAO.getDataSet());
		storeDataSet("estados", estadoDAO.getDataSet());
		storeDataSet("ciudades", ciudadDAO.getDataSet());
		storeDataSet("mensajes",mensajes);

	}//Fin execute
		
	/**
	 * Crea un dataSet vacio en caso de que Altair de un error consultando el
	 * numero de cuenta del cliente
	 * 
	 */
	public void crearDataSetCuentasVacio() throws Exception {
		//DataSet _cuentas = new DataSet();
		_cuentas.append("id", java.sql.Types.VARCHAR);
		
		//Dataset limpio de cuentas del cliente
		_cuentas = new DataSet();
		_cuentas.append("numero", java.sql.Types.VARCHAR);
		_cuentas.append("tipo", java.sql.Types.VARCHAR);
		_cuentas.append("saldo_disponible", java.sql.Types.VARCHAR);
		
		_cuentas.addNew();
		_cuentas.setValue("numero", "332248");
		_cuentas.setValue("tipo", "Corriente");
		_cuentas.setValue("saldo_disponible", "8000000.00");

		
		storeDataSet("_cuentas", _cuentas);
		storeDataSet("mensajes", mensajes);

	}
}
		
	//}//Fin Clase Edit