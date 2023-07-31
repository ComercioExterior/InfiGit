package models.custodia.transacciones.salida_interna;

import megasoft.DataSet;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.logic.Transaccion;

/**
 * Clase encargada de Realizar la transaccion de salida interna, en donde se pasan los titulos de custodia de un cliente a otro, generando la orden, las ordenes titulos y actualizando la cantidad de titulos en custodia para ambos clientes involucrados
 * 
 * @author elaucho
 */
public class SalidaInternaTransaccion extends Transaccion {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	DataSet datos_titulos = new DataSet();

	public void execute() throws Exception {

		ClienteDAO cliente = new ClienteDAO(_dso);
		ClienteDAO cliente1 = new ClienteDAO(_dso);
		String cantidad = _req.getParameter("transferir");
		String depositario = _req.getParameter("depositario");
		long idClienteTransfiere = 0;
		datos_titulos = (DataSet) _req.getSession().getAttribute("titulos_seleccionados");// se toman los titulos seleccionados de sesion
		DataSet clienteRecibe = (DataSet) _req.getSession().getAttribute("cliente_recibe");
		DataSet clienteTransfiere = (DataSet) _req.getSession().getAttribute("transacciones_salida_interna-browse.framework.page.record");

		cliente.listarNombreCliente(Long.parseLong(clienteTransfiere.getValue("client_id")));

		// Se busca el tipo de persona en clientes
		cliente1.detallesCliente(String.valueOf(idClienteTransfiere));
		// fin busqueda tipo de persona

		DataSet _operacion = new DataSet();
		_operacion.append("titulo_id", java.sql.Types.VARCHAR);
		_operacion.append("cliente_recibe", java.sql.Types.VARCHAR);
		_operacion.append("cliente_transfiere", java.sql.Types.VARCHAR);
		_operacion.append("cuenta_custodia", java.sql.Types.VARCHAR);
		_operacion.append("disponible", java.sql.Types.VARCHAR);
		_operacion.append("cantidad_transferir", java.sql.Types.VARCHAR);
		_operacion.append("moneda_id", java.sql.Types.VARCHAR);
		_operacion.append("depositario", java.sql.Types.VARCHAR);
		_operacion.append("titulo_moneda_denominacion", java.sql.Types.VARCHAR);
		_operacion.append("fecha_liquidacion", java.sql.Types.VARCHAR);
		_operacion.append("fecha_valor", java.sql.Types.VARCHAR);
		_operacion.append("fecha_custodia", java.sql.Types.VARCHAR);
		_operacion.append("fe_ult_cobro_comision", java.sql.Types.VARCHAR);
		_operacion.append("fe_ult_pago_cupon", java.sql.Types.VARCHAR);

		// se recorre el dataset de sesion para mostrar los resultados en un nuevo dataset
		if (datos_titulos.count() > 0) {
			datos_titulos.first();
			if (datos_titulos.next()) {
				datos_titulos.setValue("cantidad_transferir", _req.getParameter("transferir"));
				datos_titulos.setValue("depositario", depositario);				
				_operacion.addNew();
				_operacion.setValue("titulo_id", datos_titulos.getValue("titulo_id"));
				_operacion.setValue("cliente_recibe", clienteRecibe.getValue("client_nombre"));
				_operacion.setValue("cliente_transfiere", cliente.getDataSet().getValue("client_nombre"));
				_operacion.setValue("cuenta_custodia", clienteRecibe.getValue("client_cta_custod_id"));
				_operacion.setValue("disponible", datos_titulos.getValue("cantidad_disponible"));
				_operacion.setValue("cantidad_transferir", cantidad);
				_operacion.setValue("moneda_id", datos_titulos.getValue("moneda_id"));
				_operacion.setValue("depositario", datos_titulos.getValue("depositario"));

				// Se buscan datos adicionales para el titulo
//				custodia.listarPorTitulo(datos_titulos.getValue("titulo_id"), Long.parseLong(clienteTransfiere.getValue("client_id")));
//				if (custodia.getDataSet().count() > 0) {
//					custodia.getDataSet().first();
//					custodia.getDataSet().next();
//				}
//				_operacion.setValue("fecha_custodia", custodia.getDataSet().getValue("titulo_fe_ingreso_custodia"));
//				_operacion.setValue("fe_ult_cobro_comision", custodia.getDataSet().getValue("titulo_fe_ult_cobro_comision"));
//				_operacion.setValue("fe_ult_pago_cupon", custodia.getDataSet().getValue("titulo_fe_ult_pago_cupon"));

				// Buscar moneda de denominacion y descripcion
//				custodia.listarTituloMonedaDen(datos_titulos.getValue("titulo_id"));
//				if (custodia.getDataSet().count() > 0) {
//					custodia.getDataSet().next();
//				}
//				_operacion.setValue("titulo_moneda_denominacion", custodia.getDataSet().getValue("titulo_moneda_den"));
				// fin moneda denominacion

			}
		}
		storeDataSet("operacion", _operacion);
	}// fin execute

	public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		String cantidad[] = _req.getParameterValues("transferir");
		String depositario = _req.getParameter("depositario");
		datos_titulos = (DataSet) _req.getSession().getAttribute("titulos_seleccionados");// se toman los titulos seleccionados de sesion
		if (datos_titulos.count() > 0) {
			datos_titulos.first();
		}
		if (flag) {
			if (_req.getParameterValues("transferir") != null) {
				for (int i = 0; i < cantidad.length; i++) {
					if (cantidad[i].equals("")) {
						_record.addError("Cantidad", "Debe indicar la cantidad de titulos a transferir");
						flag = false;
					}
				}
			}
			if (_req.getParameterValues("depositario") != null) {
				if (depositario.equals("")) {
					_record.addError("Depositario", "Debe indicar el depositario");
					flag = false;
				}
			}

			if (_req.getParameterValues("transferir") != null) {
				int j = 0;
				if (datos_titulos.next()) {
					if (!cantidad[j].equals("")) {
						if (Long.parseLong(cantidad[j]) > Long.parseLong(datos_titulos.getValue("cantidad_disponible"))) {
							_record.addError("Cantidad a Transferir", "No puede ser mayor a la cantidad disponible para el t&iacute;tulo: " + datos_titulos.getValue("titulo_id"));
							flag = false;
						}
						if (Long.parseLong(cantidad[j]) == 0) {
							_record.addError("Cantidad a Transferir", "Debe ser mayor a 0 para el t&iacute;tulo: " + datos_titulos.getValue("titulo_id"));
							flag = false;
						}
					}
					j++;
				}
			}
		}// fin if
		return flag;
	}
}