package models.custodia.transacciones.salida_interna;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.SecsDao;
import com.bdv.infi.dao.TasaCambioCierreDAO;
import com.bdv.infi.data.Custodia;
import com.bdv.infi.data.Moneda;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.TasaCambioCierre;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class SalidaInternaInsert extends Transaccion {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	DataSet datos_titulos = null;
	private BigDecimal tasaCambio = new BigDecimal(1);

	public void ejecutar_querys(Vector<String> ejecucion) throws Exception {
		String[] querys = new String[ejecucion.size()];
		ejecucion.toArray(querys);
		db.execBatch(_dso, querys);
	}

	public void execute() throws Exception {
		Date fechaValor = (Date) _req.getSession().getAttribute("salida.interna.fecha.valor");
		Orden ordenClteT = new Orden();// Objeto que tendra todos los atributos cliente que transfiere
		Orden ordenClteR = new Orden();// Objeto que tendra todos los atributos cliente que recibe
		SecsDao secsDao = new SecsDao(_dso); 
		CustodiaDAO custodia = new CustodiaDAO(_dso);
		Custodia custodiaData = new Custodia();
		Custodia custodiaDataR = new Custodia();
		ClienteDAO cliente = new ClienteDAO(_dso);
		ClienteDAO cliente1 = new ClienteDAO(_dso);
		String tipperIdtransfiere = "";
		String tipperIdRecibe = "";
		long idClienteTransfiere = 0;
		long idClienteRecibe = 0;
		long numeroOrdenClienteTransfiere = 0;
		Vector<String> query = new Vector<String>(10, 5);
		datos_titulos = (DataSet) _req.getSession().getAttribute("titulos_seleccionados");// se toman los titulos seleccionados de sesion
		DataSet clienteRecibe = (DataSet) _req.getSession().getAttribute("cliente_recibe");
		DataSet clienteTransfiere = (DataSet) _req.getSession().getAttribute("transacciones_salida_interna-browse.framework.page.record");
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		Moneda monedaTitulo = new Moneda();
		// ManejadorTasaCambio manejadorTasaCambio = new ManejadorTasaCambio(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));

		// ----Obtener NUMERO DE OFICINA guardado en sesion
		String nroOficina = "";
		if (_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL) != null) {
			nroOficina = (String) _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);
		} else {
			Logger.info(this, "NO SE ENCUENTRA EL NUMERO DE OFICINA EN LA SESION");
			throw new Exception("No se encuentra el n&uacute;mero de oficina");
		}

		if (datos_titulos.count() > 0) {
			datos_titulos.first();
			if (datos_titulos.next()) {
				try {
					// Buscar tasa de cambio de la moneda de denominacion del titulo
					if (monedaDAO.listarPorId(datos_titulos.getValue("titulo_moneda_den"))) {
						// Objeto con todos los atributos de MONEDA SELECCIONADA PARA LA VENTA
						monedaTitulo = (Moneda) monedaDAO.moveNext();

						TasaCambioCierre tasaCambioCierre = new TasaCambioCierre();
						// /OBTENER TASA DE CAMBIO DE LA MONEDA DEL TITULO
						TasaCambioCierreDAO tasaCambioCierreDAO = new TasaCambioCierreDAO(_dso);
						tasaCambioCierre = tasaCambioCierreDAO.listarTasas(monedaTitulo.getSiglas());

						tasaCambio = new BigDecimal(tasaCambioCierre.getTasaCambioCompra());

					}
				} catch (Exception ex) {
					throw ex;
				} finally {
					monedaDAO.closeResources();
					monedaDAO.cerrarConexion();
				}
			}
		}
		if (clienteRecibe.count() > 0) {
			clienteRecibe.next();
			tipperIdRecibe = clienteRecibe.getValue("tipper_id");
		}
		if (clienteTransfiere.count() > 0) {
			clienteTransfiere.next();
			idClienteTransfiere = Long.parseLong(clienteTransfiere.getValue("client_id"));
			idClienteRecibe = Long.parseLong(clienteTransfiere.getValue("client_id_1"));
		}
		cliente.listarNombreCliente(Long.parseLong(clienteTransfiere.getValue("client_id")));

		// Se busca el tipo de persona en clientes
		cliente1.detallesCliente(String.valueOf(idClienteTransfiere));
		if (cliente1.getDataSet().count() > 0) {
			cliente1.getDataSet().next();
			tipperIdtransfiere = cliente.getDataSet().getValue("tipper_id");
		}
		// fin busqueda tipo de persona

		DataSet _operacion = new DataSet();
		_operacion.append("titulo", java.sql.Types.VARCHAR);
		_operacion.append("titulo_id", java.sql.Types.VARCHAR);
		_operacion.append("cliente_recibe", java.sql.Types.VARCHAR);
		_operacion.append("cliente_transfiere", java.sql.Types.VARCHAR);
		_operacion.append("cuenta_custodia", java.sql.Types.VARCHAR);
		_operacion.append("disponible", java.sql.Types.VARCHAR);
		_operacion.append("cantidad_transferir", java.sql.Types.VARCHAR);
		_operacion.append("moneda_id", java.sql.Types.VARCHAR);
		_operacion.append("depositario", java.sql.Types.VARCHAR);
		_operacion.append("comisiones", java.sql.Types.VARCHAR);
		_operacion.append("comisiones_dep", java.sql.Types.VARCHAR);
		_operacion.append("valor_nominal", java.sql.Types.VARCHAR);
		_operacion.append("total_final", java.sql.Types.DOUBLE);
		_operacion.append("monto_sin_comision", java.sql.Types.DOUBLE);
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
				_operacion.addNew();
				_operacion.setValue("titulo_id", datos_titulos.getValue("titulo_id"));
				_operacion.setValue("cliente_recibe", clienteRecibe.getValue("client_nombre"));
				_operacion.setValue("cliente_transfiere", cliente.getDataSet().getValue("client_nombre"));
				_operacion.setValue("cuenta_custodia", clienteRecibe.getValue("client_cta_custod_id"));
				_operacion.setValue("disponible", datos_titulos.getValue("cantidad_disponible"));
				_operacion.setValue("cantidad_transferir", datos_titulos.getValue("cantidad_transferir"));
				_operacion.setValue("moneda_id", datos_titulos.getValue("moneda_id"));
				_operacion.setValue("depositario", datos_titulos.getValue("depositario"));

				// Genera el t&iacute;tulo involucrado en esta orden cliente Transfiere
				OrdenTitulo oTituloT = new OrdenTitulo();
				oTituloT.setTituloId(datos_titulos.getValue("titulo_id"));
				oTituloT.setUnidades(Double.parseDouble(datos_titulos.getValue("cantidad_transferir")));
				oTituloT.setPorcentaje("0");
				ordenClteT.agregarOrdenTitulo(oTituloT);

				// Genera el t&iacute;tulo involucrado en esta orden cliente Recibe
				OrdenTitulo oTituloR = new OrdenTitulo();
				oTituloR.setTituloId(datos_titulos.getValue("titulo_id"));
				oTituloR.setPorcentaje("0");
				oTituloR.setUnidades(Double.parseDouble(datos_titulos.getValue("cantidad_transferir")));
				ordenClteR.agregarOrdenTitulo(oTituloR);

				// Se cre el objeto orden con todos sus atributos para el cliente que Transfiere
				ordenClteT.setIdCliente(idClienteTransfiere);
				ordenClteT.setIdTransaccion(TransaccionNegocio.SALIDA_INTERNA);
				ordenClteT.setIdDepositario(datos_titulos.getValue("depositario"));
				ordenClteT.setIdTipoPersona(tipperIdtransfiere);
				ordenClteT.setFechaValor(fechaValor);
				ordenClteT.setTipoProducto(datos_titulos.getValue("tipo_producto"));
				ordenClteT.setStatus(StatusOrden.REGISTRADA);
				ordenClteT.setNombreUsuario(this.getUserName());
				ordenClteT.setTerminal(_req.getRemoteAddr());
				ordenClteT.setSucursal(nroOficina);

				// setear tasa de cambio de la moneda
				ordenClteT.setTasaCambio(tasaCambio.doubleValue());

				// query.add(guardarOrden(ordenClteT));
				String transfiere[] = guardarOrden(ordenClteT);
				for (int i = 0; i < transfiere.length; i++) {
					query.add(transfiere[i]);
				}

				numeroOrdenClienteTransfiere = ordenClteT.getIdOrden();
				// Se crea el objeto orden con todos sus atributos para el cliente que recibe
				ordenClteR.setIdCliente(idClienteRecibe);
				ordenClteR.setStatus(StatusOrden.REGISTRADA);
				ordenClteR.setIdTransaccion(TransaccionNegocio.ENTRADA_DE_TITULOS);
				ordenClteR.setIdEmpresa(datos_titulos.getValue("depositario"));
				ordenClteR.setIdTipoPersona(tipperIdRecibe);
				ordenClteR.setFechaValor(fechaValor);
				ordenClteR.setIdOrdenRelacionada(ordenClteT.getIdOrden());
				ordenClteR.setTipoProducto(datos_titulos.getValue("tipo_producto"));
				ordenClteR.setTasaCambio(tasaCambio.doubleValue());
				ordenClteR.setNombreUsuario(this.getUserName());
				ordenClteR.setTerminal(_req.getRemoteAddr());
				ordenClteR.setSucursal(nroOficina);

				// Aplicar transaccion para el objeto que recibe
				String recibeR[] = guardarOrden(ordenClteR);
				for (int i = 0; i < recibeR.length; i++) {
					query.add(recibeR[i]);
				}

				custodiaData.setIdCliente(idClienteTransfiere);
				custodiaData.setIdTitulo(datos_titulos.getValue("titulo_id"));
				custodiaData.setTipoProductoId(datos_titulos.getValue("tipo_producto"));

				// Se resta a la cantidad de titulos que posee, la cantidad a transferir
				custodia.listarTitulos(idClienteTransfiere, datos_titulos.getValue("titulo_id"), datos_titulos.getValue("tipo_producto"));
				if (custodia.getDataSet().count() > 0) {
					custodia.getDataSet().next();
				}
				long cantidadActualizada = Long.parseLong(custodia.getDataSet().getValue("titcus_cantidad")) - Long.parseLong(datos_titulos.getValue("cantidad_transferir"));
				custodiaData.setCantidad(cantidadActualizada);
				query.add(custodia.actualizarCantidadEnCustodia(custodiaData));

				// Metodo para abonar la cantidad transferida al Cliente que recibe
				custodiaDataR.setIdCliente(idClienteRecibe);
				custodiaDataR.setIdTitulo(datos_titulos.getValue("titulo_id"));
				custodiaDataR.setTipoProductoId(datos_titulos.getValue("tipo_producto"));
				custodiaDataR.setCantidad(Long.parseLong(datos_titulos.getValue("cantidad_transferir")));
				custodiaDataR.setFechaUltimoCupon(secsDao.obtenerFechaAmortizacionCupon(custodiaDataR.getIdTitulo()));
				custodiaDataR.setFechaUltimaAmortizacion(custodiaDataR.getFechaUltimoCupon());				
				query.add(custodia.insertarUpdateCteRecibeTitulos(custodiaDataR));

				ejecutar_querys(query);
			}
		}
		storeDataSet("operacion", _operacion);
		limpiarSesion();
		DataSet _numero_orden = new DataSet();
		_numero_orden.append("numero_orden", java.sql.Types.VARCHAR);
		_numero_orden.addNew();
		_numero_orden.setValue("numero_orden", String.valueOf(numeroOrdenClienteTransfiere));
		storeDataSet("numero_orden", _numero_orden);

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

	private void limpiarSesion() {
		// remover datos de sesion
		_req.getSession().removeAttribute("cliente_recibe");
		_req.getSession().removeAttribute("transacciones_salida_interna-browse.framework.page.record");
		_req.getSession().removeAttribute("cliente_trasfiere");
		_req.getSession().removeAttribute("seleccion");
		_req.getSession().removeAttribute("datasetParam");
		_req.getSession().removeAttribute("titulos_seleccionados");
		_req.getSession().removeAttribute("ordenes_blotter-find.framework.page.record");
		_req.getSession().removeAttribute("transacciones_salida_interna-browse.framework.page.query-string");
		_req.getSession().removeAttribute("unidad_inversion_seleccionada");
		_req.getSession().removeAttribute("orders_clte-browse.framework.page.record");
		_req.getSession().removeAttribute("salida.interna.fecha.valor");
		_req.getSession().removeAttribute("cantidad");
		_req.getSession().removeAttribute("depositario");
		_req.getSession().removeAttribute("transacciones_salida_interna-browse.framework.page.using-page");
		_req.getSession().removeAttribute("transacciones_salida_interna-browse.framework.page.current-page");
	}
}// fin clase
