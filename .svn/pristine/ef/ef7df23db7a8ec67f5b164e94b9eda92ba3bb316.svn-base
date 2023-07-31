package models.configuracion.cuentasCliente;

import java.util.ArrayList;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de buscar en ALTAIR las cuentas asociadas al cliente por
 * medio de AJAX
 * 
 * @author bag
 */
public class BuscarCuentaDolaresClienteAjax extends MSCModelExtend {
	private DataSet mensajes = new DataSet();

	/**
	 * ArrayList que contiene las cuentas asociadas a un cliente
	 */
	ArrayList<Cuenta> cuentasArrayList = new ArrayList<Cuenta>();

	/**
	 * DataSet que se publicara con las cuentas asociadas al cliente
	 */
	DataSet _cuentas = new DataSet();

	@Override
	public void execute() throws Exception {

		ClienteDAO clienteDAO = new ClienteDAO(_dso);		
		try {
			/*
			 * Dataset de error
			 */
			mensajes = new DataSet();
			mensajes.append("mensaje_error", java.sql.Types.VARCHAR);
			mensajes.addNew();

			/*
			 * Buscamos en Altair las cuentas asociadas al Cliente!!!
			 */
			String cliente = _req.getParameter("id_cliente");
			/*
			 * Buscar cedula
			 */
			String cedula = "";
			String tipoPersona = "";
			ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(_dso);
			if (clienteDAO.listarPorId(Long.parseLong(cliente))) {
				Cliente clienteObj = (Cliente) clienteDAO.moveNext();
				cedula = String.valueOf(clienteObj.getRifCedula());
				tipoPersona = String.valueOf(clienteObj.getTipoPersona());

				cuentasArrayList = clienteCuentasDAO.buscarCuentasEnDolaresAltair(
						cedula, tipoPersona, _app, _req.getRemoteAddr(), getUserName());
		
				ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(
						 _app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
				/*
				 * Metodo retorna un dataset para ser publicado
				 */
								
				_cuentas = manejadorDeClientes.cargarDataSet(cuentasArrayList);
				
				mensajes.setValue("mensaje_error", "");
				storeDataSet("mensajes", mensajes);
				_req.getSession().setAttribute("mensajes", mensajes);
				
			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			e.printStackTrace();
			mensajes.setValue("mensaje_error",
					"Error consultando las cuentas del cliente en arquitectura extendida. "
							+ e.getMessage());
			storeDataSet("mensajes", mensajes);
			_req.getSession().setAttribute("mensajes", mensajes);
			//crearDataSetCuentasVacio();
		} catch (Throwable t) {
			t.printStackTrace();
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
		
		storeDataSet("_cuentas", _cuentas);

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
