package models.gestion_pago_cheque;

import java.util.ArrayList;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de buscar en ALTAIR las cuentas asociadas al cliente por
 * medio de AJAX
 */
public class BuscarCuentaClienteAjax extends MSCModelExtend {
	private DataSet mensajes = new DataSet();

	// ArrayList que contiene las cuentas asociadas a un cliente
	ArrayList<Cuenta> cuentasArrayList = new ArrayList<Cuenta>();

	//DataSet que se publicara con las cuentas asociadas al cliente
	DataSet _cuentas = new DataSet();

	@Override
	public void execute() throws Exception {

		try {
			// codigo de Ajax a ejecutar
			int cod_ajax = Integer.parseInt(_req.getParameter("cod_ajax"));
			switch (cod_ajax) {
			case 1:
				this.ajaxCuentaPorCliente();
				break;
			}
		} catch (RuntimeException e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));

		} catch (Exception e) {
			throw e;
		}
	}// fin execute

	public void ajaxCuentaPorCliente() throws Exception {

		ClienteDAO clienteDAO = new ClienteDAO(_dso);	
		ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(_dso);
		DataSet _cliente = new DataSet();
		_cliente.append("tipo_persona",java.sql.Types.VARCHAR);
		_cliente.append("cedula",java.sql.Types.VARCHAR);
		_cliente.append("monto_operaciones",java.sql.Types.VARCHAR);
		
		try {
			//Dataset de error
			mensajes = new DataSet();
			mensajes.append("mensaje_error", java.sql.Types.VARCHAR);
			mensajes.addNew();

			//Buscamos en Altair las cuentas asociadas al Cliente!!!
			String cliente = _req.getParameter("id_cliente");
			
			//Buscar cedula
			String cedula = "";
			String tipoPersona = "";
			
			
			if (clienteDAO.listarPorId(Long.parseLong(cliente))) {
				Cliente clienteObj = (Cliente) clienteDAO.moveNext();
				cedula = String.valueOf(clienteObj.getRifCedula());
				tipoPersona = String.valueOf(clienteObj.getTipoPersona());
				
				//Set cedula y tipo persona al dataset de cliente
				_cliente.addNew();
				_cliente.setValue("tipo_persona",tipoPersona);
				_cliente.setValue("cedula", cedula);
				if (_req.getSession().getAttribute("infi.monto.operaciones")!=null){
					_cliente.setValue("monto_operaciones", _req.getSession().getAttribute("infi.monto.operaciones").toString());	
				}	
				
				cuentasArrayList = clienteCuentasDAO.buscarCuentasAltair(
						cedula, tipoPersona, _app, _req.getRemoteAddr(), getUserName());
				//Credenciales de usuario
				CredencialesDeUsuario credencialesDeUsuario = new CredencialesDeUsuario();
				credencialesDeUsuario.setClaveSecreta("claveSecreta");
				credencialesDeUsuario.setNombreDeUsuario("nombreDeUsuario");
				/*
				 * Instancia de object ManejadorDeClientes para acceder al
				 * metodo cargarDataSet
				 */
				ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(
						_app, credencialesDeUsuario);
				/*
				 * Metodo retorna un dataset para ser publicado
				 */
				_cuentas = manejadorDeClientes.cargarDataSet(cuentasArrayList);
				mensajes.setValue("mensaje_error", "");
				storeDataSet("mensajes", mensajes);
				_req.getSession().setAttribute("mensajes", mensajes);
				storeDataSet("_cuentas", _cuentas);
				storeDataSet("cliente", _cliente);
				
			} else {

				throw new Exception();
			}

		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			mensajes.setValue("mensaje_error",
					"Error consultando las cuentas del cliente en arquitectura extendida. "
							+ e.getMessage());
			storeDataSet("mensajes", mensajes);
			_req.getSession().setAttribute("mensajes", mensajes);
			//crearDataSetCuentasVacio();
		} catch (Throwable t) {
			Logger.error(this,t.getMessage()+" "+Utilitario.stackTraceException(t));
			mensajes.setValue("mensaje_error",
					"Error consultando las cuentas del cliente en arquitectura extendida. "
							+ t.getMessage());
			storeDataSet("mensajes", mensajes);
			_req.getSession().setAttribute("mensajes", mensajes);
			//crearDataSetCuentasVacio();
		} finally{
			clienteDAO.closeResources();
			clienteDAO.cerrarConexion();
		}

	}// Fin execute

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
}// Fin BuscarCuentaClienteAjax
