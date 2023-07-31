package models.custodia.transacciones.salida_externa;

import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

public class SalidaExternaTransaccion extends Transaccion {
	DataSet datos_titulos = new DataSet();

	public void execute() throws Exception {
		ClienteDAO cliente = new ClienteDAO(_dso);
		ClienteDAO cliente1 = new ClienteDAO(_dso);
		datos_titulos = (DataSet) _req.getSession().getAttribute("titulos_seleccionados");// se toman los titulos seleccionados de sesion
		String tipperIdtransfiere = "";
		String cantidad = _req.getParameter("transferir");
		String depositario = _req.getParameter("depositario");
		String contraparte_carmen_codigo = _req.getParameter("contraparte_carmen_codigo");
		long idClienteTransfiere = 0;
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		Date fecha = new Date();
		String depositarioCentral = _record.getValue("depositario_central");
		String contraparte = _record.getValue("contraparte");
		DataSet clienteTransfiere = (DataSet) _req.getSession().getAttribute("salida_externa-browse.framework.page.record");
		DataSet _operacionesTrnfin = new DataSet();
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		_req.getSession().setAttribute("cantidad", cantidad);
		_req.getSession().setAttribute("depositario", depositario);
		_req.getSession().setAttribute("depositarioCentral", depositarioCentral);
		_req.getSession().setAttribute("contraparte", contraparte);
		_req.getSession().setAttribute("contraparte_carmen_codigo", _record.getValue("contraparte_carmen_codigo"));

		// Dataset Operaciones financieras
		_operacionesTrnfin.append("referencia", java.sql.Types.VARCHAR);
		_operacionesTrnfin.append("titulo_id", java.sql.Types.VARCHAR);
		_operacionesTrnfin.append("titulo", java.sql.Types.VARCHAR);
		_operacionesTrnfin.append("porcentaje", java.sql.Types.VARCHAR);
		_operacionesTrnfin.append("monto", java.sql.Types.VARCHAR);
		_operacionesTrnfin.append("readonly", java.sql.Types.VARCHAR);
		_operacionesTrnfin.append("monto_fijo", java.sql.Types.VARCHAR);

		// Obtener fecha valor
		// Date fechaValor = Utilitario.calcularFechaValor(_dso);

		try {

			if (clienteTransfiere.count() > 0) {
				clienteTransfiere.next();
				idClienteTransfiere = Long.parseLong(clienteTransfiere.getValue("client_id"));
			}
			cliente.listarNombreCliente(Long.parseLong(clienteTransfiere.getValue("client_id")));

			// Se busca el tipo de persona en clientes
			cliente1.detallesCliente(String.valueOf(idClienteTransfiere));
			if (cliente1.getDataSet().count() > 0) {
				cliente1.getDataSet().next();
				tipperIdtransfiere = cliente.getDataSet().getValue("tipper_id");
			}
			// fin busqueda tipo de persona

			// Se coloca en sesion el cliente q trasfiere
			DataSet _cliente = new DataSet();
			_cliente.append("client_id", java.sql.Types.VARCHAR);
			_cliente.append("tipo_persona", java.sql.Types.VARCHAR);
			_cliente.addNew();
			_cliente.setValue("client_id", String.valueOf(idClienteTransfiere));
			_cliente.setValue("tipo_persona", tipperIdtransfiere);
			_req.getSession().setAttribute("cliente", _cliente);

			DataSet _operacion = new DataSet();
			_operacion.append("titulo", java.sql.Types.VARCHAR);
			_operacion.append("titulo_id", java.sql.Types.VARCHAR);
			_operacion.append("tipo_producto", java.sql.Types.VARCHAR);
			_operacion.append("cliente_transfiere", java.sql.Types.VARCHAR);
			_operacion.append("disponible", java.sql.Types.VARCHAR);
			_operacion.append("cantidad_transferir", java.sql.Types.VARCHAR);
			_operacion.append("moneda_id", java.sql.Types.VARCHAR);
			_operacion.append("depositario", java.sql.Types.VARCHAR);
			_operacion.append("titulo_moneda_denominacion", java.sql.Types.VARCHAR);
			_operacion.append("cuenta_depositario_central", java.sql.Types.VARCHAR);
			_operacion.append("fecha_liquidacion", java.sql.Types.VARCHAR);
			_operacion.append("fecha_valor", java.sql.Types.VARCHAR);
			_operacion.append("contraparte", java.sql.Types.VARCHAR);
			_operacion.append("contraparte_carmen_codigo", java.sql.Types.VARCHAR);
			_operacion.append("tasa", java.sql.Types.DOUBLE);
			_operacion.append("fecha_custodia", java.sql.Types.VARCHAR);
			_operacion.append("fe_ult_cobro_comision", java.sql.Types.VARCHAR);
			_operacion.append("fe_ult_pago_cupon", java.sql.Types.VARCHAR);
			_operacion.append("id_trfin", java.sql.Types.VARCHAR);

			// se recorre el dataset de sesion para mostrar los resultados en un nuevo dataset
			if (datos_titulos.count() > 0) {
				datos_titulos.first();
				if (datos_titulos.next()) {
					_operacion.addNew();
					_operacion.setValue("titulo_id", datos_titulos.getValue("titulo_id"));
					_operacion.setValue("tipo_producto", datos_titulos.getValue("tipo_producto"));
					_operacion.setValue("cliente_transfiere", cliente.getDataSet().getValue("client_nombre"));
					_operacion.setValue("disponible", datos_titulos.getValue("cantidad_disponible"));
					_operacion.setValue("cantidad_transferir", cantidad);
					_operacion.setValue("moneda_id", datos_titulos.getValue("moneda_id"));
					_operacion.setValue("depositario", _req.getParameter("depositario"));
					_operacion.setValue("cuenta_depositario_central", depositarioCentral);
					_operacion.setValue("fecha_liquidacion", String.valueOf(formato.format((fecha))));
					_operacion.setValue("fecha_valor", String.valueOf(formato.format((Date) _req.getSession().getAttribute("salida.externa.fecha.valor"))));
					_operacion.setValue("contraparte", contraparte);
					_operacion.setValue("contraparte_carmen_codigo", contraparte_carmen_codigo);
				}// fin while
			}// fin if
			storeDataSet("operacion", _operacion);
			_req.getSession().setAttribute("operacion", _operacion);

		} catch (Exception e) {
			Logger.error(this, e.getMessage() + " " + Utilitario.stackTraceException(e));
			throw new Exception("Error procesando la salida externa del t&iacute;tulo.");

		} finally {
			monedaDAO.cerrarConexion();
			monedaDAO.closeResources();
		}

	}// fin execute

	public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		String cantidad[] = _req.getParameterValues("transferir");
		String depositario = _req.getParameter("depositario");
		String contraparte = _req.getParameter("contraparte");
		String contraparte_carmen_codigo = _req.getParameter("contraparte_carmen_codigo");
		String cuenta_despositario = _req.getParameter("depositario_central");
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

			if (_req.getParameterValues("contraparte") != null) {
				if (contraparte.equals("")) {
					_record.addError("Contraparte", "Este campo es obligatorio");
					flag = false;
				}
			}

			if (_req.getParameterValues("depositario_central") != null) {
				if (cuenta_despositario.equals("")) {
					_record.addError("Cuenta Depositario Central", "Este campo es obligatorio");
					flag = false;
				}
			}
			if (_req.getParameterValues("depositario") != null) {
				if (depositario.equals("")) {
					_record.addError("Depositario", "Este campo es obligatorio");
					flag = false;
				}
			}
			
			if (_req.getParameterValues("contraparte_carmen_codigo") != null) {
				if (contraparte_carmen_codigo.equals("")) {
					_record.addError("Contraparte Carmen", "Este campo es obligatorio");
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
