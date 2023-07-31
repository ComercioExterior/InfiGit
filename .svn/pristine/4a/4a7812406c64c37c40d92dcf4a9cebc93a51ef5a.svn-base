package models.custodia.transacciones.salida_externa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MonedaDAO;
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
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de procesar Salida Externa.
 * 
 * @author elaucho, nvisbal
 */
public class SalidaExternaInsert extends Transaccion {
	private DataSet datos_titulos = new DataSet();
	private Vector<String> query = new Vector<String>();
	private DataSet _operacion = new DataSet();
	private DataSet _cliente = new DataSet();
	private Orden ordenClteT = new Orden();
	private String tipperIdtransfiere = "";
	private String cedRifCliente ="";
	private DataSet clienteTransfiere;
	private long idClienteTransfiere = 0;
	private DataSet _datosCliente;
	private DataSet _cuentaCustodia = new DataSet(); 

	/**
	 * 
	 * @param ejecucion
	 * @throws Exception
	 */
	private void ejecutar_querys(Vector<String> ejecucion) throws Exception {
		String[] querys = new String[ejecucion.size()];
		ejecucion.toArray(querys);
		db.execBatch(_dso, querys);
	}

	public void execute() throws Exception {
		Date fechaValor = (Date) _req.getSession().getAttribute("salida.externa.fecha.valor");
		CustodiaDAO custodia = new CustodiaDAO(_dso);
		Custodia custodiaData = new Custodia();		
		datos_titulos = (DataSet) _req.getSession().getAttribute("titulos_seleccionados");// se toman los titulos seleccionados de sesion
		String cantidad = _req.getSession().getAttribute("cantidad").toString();
		String depositario = _req.getSession().getAttribute("depositario").toString();		
		long numeroOrdenClienteTransfiere=0;
		int transaccion_req = 0;
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		Date fecha = new Date();
		String depositarioCentral = _req.getSession().getAttribute("depositarioCentral").toString();
		String contraparte = _req.getSession().getAttribute("contraparte").toString();
		long idOrden = Long.parseLong(com.bdv.infi.dao.GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_ORDENES));
		
		DataSet _operacionesTrnfin = new DataSet();
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		Moneda monedaTitulo = new Moneda();
		BigDecimal tasaCambio = new BigDecimal(1);

		// ----Obtener NUMERO DE OFICINA guardado en sesion
		String nroOficina = "";
		if (_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL) != null) {
			nroOficina = (String) _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);
		} else {
			Logger.info(this, "NO SE ENCUENTRA EL NUMERO DE OFICINA EN LA SESION");
			throw new Exception("No se encuentra el n&uacute;mero de oficina.");
		}

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

			if (datos_titulos.count() > 0 && transaccion_req != 1) {
				datos_titulos.first();
				if (datos_titulos.next()) {
					datos_titulos.setValue("cantidad_transferir", cantidad);
					datos_titulos.setValue("depositario", depositario);

					if (monedaDAO.listarPorId(datos_titulos.getValue("titulo_moneda_den"))) {
						// Objeto con todos los atributos de MONEDA SELECCIONADA PARA LA VENTA
						monedaTitulo = (Moneda) monedaDAO.moveNext();

						TasaCambioCierre tasaCambioCierre = new TasaCambioCierre();
						// /OBTENER TASA DE CAMBIO DE LA MONEDA DEL TITULO
						TasaCambioCierreDAO tasaCambioCierreDAO = new TasaCambioCierreDAO(_dso);
						tasaCambioCierre = tasaCambioCierreDAO.listarTasas(monedaTitulo.getSiglas());

						tasaCambio = new BigDecimal(tasaCambioCierre.getTasaCambioCompra());

					}
				}
			}
		
			// Se coloca en sesion el cliente q trasfiere			
			_cliente.append("client_id", java.sql.Types.VARCHAR);
			_cliente.append("tipo_persona", java.sql.Types.VARCHAR);
			_cliente.addNew();
			_cliente.setValue("client_id", String.valueOf(idClienteTransfiere));
			_cliente.setValue("tipo_persona", tipperIdtransfiere);
			_req.getSession().setAttribute("cliente", _cliente);

			_operacion.append("titulo", java.sql.Types.VARCHAR);
			_operacion.append("titulo_id", java.sql.Types.VARCHAR);
			_operacion.append("tipo_producto", java.sql.Types.VARCHAR);
			_operacion.append("cliente_transfiere", java.sql.Types.VARCHAR);
			_operacion.append("disponible", java.sql.Types.VARCHAR);
			_operacion.append("cantidad_transferir", java.sql.Types.VARCHAR);
			_operacion.append("moneda_id", java.sql.Types.VARCHAR);
			_operacion.append("depositario", java.sql.Types.VARCHAR);
			_operacion.append("comisiones", java.sql.Types.INTEGER);
			_operacion.append("comisiones_dep", java.sql.Types.INTEGER);
			_operacion.append("valor_nominal", java.sql.Types.INTEGER);
			_operacion.append("total_final", java.sql.Types.DOUBLE);
			_operacion.append("monto_sin_comision", java.sql.Types.DOUBLE);
			_operacion.append("titulo_moneda_denominacion", java.sql.Types.VARCHAR);
			_operacion.append("cuenta_depositario_central", java.sql.Types.VARCHAR);
			_operacion.append("fecha_liquidacion", java.sql.Types.VARCHAR);
			_operacion.append("fecha_valor", java.sql.Types.VARCHAR);
			_operacion.append("contraparte", java.sql.Types.VARCHAR);
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
					_operacion.setValue("cliente_transfiere", _datosCliente.getValue("client_nombre"));
					_operacion.setValue("disponible", datos_titulos.getValue("cantidad_disponible"));
					_operacion.setValue("cantidad_transferir", cantidad);
					_operacion.setValue("moneda_id", datos_titulos.getValue("moneda_id"));
					_operacion.setValue("depositario", _req.getParameter("depositario"));
					_operacion.setValue("cuenta_depositario_central", depositarioCentral);
					_operacion.setValue("fecha_liquidacion", String.valueOf(formato.format((fecha))));
					_operacion.setValue("fecha_valor", String.valueOf(formato.format((Date) _req.getSession().getAttribute("salida.externa.fecha.valor"))));
					_operacion.setValue("contraparte", contraparte);

					// Genera el título involucrado en esta orden cliente Transfiere
					OrdenTitulo oTituloT = new OrdenTitulo();
					oTituloT.setTituloId(datos_titulos.getValue("titulo_id"));
					oTituloT.setUnidades(Double.parseDouble(datos_titulos.getValue("cantidad_transferir")));
					oTituloT.setPorcentaje("0");
					ordenClteT.agregarOrdenTitulo(oTituloT);

					// Se cre el objeto orden con todos sus atributos para el cliente que Transfiere
					ordenClteT.setIdCliente(idClienteTransfiere);
					ordenClteT.setTipoProducto(datos_titulos.getValue("tipo_producto"));
					ordenClteT.setIdTransaccion(TransaccionNegocio.SALIDA_EXTERNA);
					ordenClteT.setIdEmpresa(depositario);
					ordenClteT.setIdTipoPersona(tipperIdtransfiere);
					ordenClteT.setIdOrden(idOrden);
					ordenClteT.setStatus(StatusOrden.REGISTRADA);
					ordenClteT.setContraparte(contraparte);
					ordenClteT.setFechaValor(fechaValor);
					ordenClteT.setNombreUsuario(this.getUserName());
					ordenClteT.setTerminal(_req.getRemoteAddr());
					ordenClteT.setSucursal(nroOficina);

					// setear tasa de cambio de la moneda
					ordenClteT.setTasaCambio(tasaCambio.doubleValue());

					String transfiere[] = guardarOrden(ordenClteT);
					for (int i = 0; i < transfiere.length; i++) {
						query.add(transfiere[i]);
					}
					numeroOrdenClienteTransfiere = ordenClteT.getIdOrden();
					
					// Proceso para actualizar la cantidad del Cliente que Transfiere la introducida se le resta a la disponible

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
					String updateTb_701 = custodia.actualizarCantidadEnCustodia(custodiaData);
					query.add(updateTb_701);
					
					//----Llamada a generación de Mensaje para Sistema CARMEN-------------
					generarMensajeCarmen();					
					//---------------------------------------------------------------------
					
					// ejecutar query execBatch
					ejecutar_querys(query);

				}// fin while
			}// fin if
			_req.getSession().setAttribute("operacion", _operacion);			
			storeDataSet("operacion", _operacion);
			limpiarSesion();
			DataSet _numero_orden = new DataSet();
			_numero_orden.append("numero_orden", java.sql.Types.VARCHAR);
			_numero_orden.addNew();
			_numero_orden.setValue("numero_orden", String.valueOf(numeroOrdenClienteTransfiere));
			storeDataSet("numero_orden", _numero_orden);			
		} catch (Exception e) {
			Logger.error(this, e.getMessage() + " " + Utilitario.stackTraceException(e));
			throw new Exception("Error procesando la salida externa del t&iacute;tulo.");

		} finally {
			monedaDAO.closeResources();			
			monedaDAO.cerrarConexion();
		}

	}// fin execute
	
	/**
	 * Genera el mensaje para Carmen
	 * @return mensajeCarmen: objeto con el mensaje para carmen
	 * @throws Exception 
	 */
	private void generarMensajeCarmen() throws Exception{
		
		//Verificar si el tipo de producto es SITME
		
		if(ordenClteT.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
	
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);
			MensajeCarmen mensajeCarmen = new MensajeCarmen();
			
			//Datos de la cuenta custodia
			_cuentaCustodia.first();
			_cuentaCustodia.next();
						
			//Setear Valores al Mensaje para Interfaz Carmen
			mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, datos_titulos.getValue("titulo_id"));
			mensajeCarmen.set(mensajeCarmen.CANTIDAD, datos_titulos.getValue("cantidad_transferir"));
			mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, new Date());
			mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, ordenClteT.getFechaValor());
			mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, (String) _req.getSession().getAttribute("contraparte_carmen_codigo"));
			mensajeCarmen.setUsuarioNM(getUserName());			
			mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(ordenClteT.getIdOrden())));
		
			//Establecer valores por defecto al mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeCarmen);
	
			//Generar senetencias sql para ingreso de mensaje en BD
			String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);
			for (int i = 0; i < sentenciasMje.length; i++) {
				query.add(sentenciasMje[i]);//guardar en vector global de queries a ejecutar
			}

		}
		
	}


	public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		
		if (flag) {			
			String cantidad[] = _req.getParameterValues("transferir");
			String depositario = _req.getParameter("depositario");
			String contraparte = _req.getParameter("contraparte");
			String cuenta_despositario = _req.getParameter("depositario_central");
			datos_titulos = (DataSet) _req.getSession().getAttribute("titulos_seleccionados");// se toman los titulos seleccionados de sesion
			
			//---Información del request:--------------------------------------------
			clienteTransfiere = (DataSet) _req.getSession().getAttribute("salida_externa-browse.framework.page.record");
			if (clienteTransfiere.count() > 0) {
				clienteTransfiere.next();
				idClienteTransfiere = Long.parseLong(clienteTransfiere.getValue("client_id"));
			}	
			//-------------------------------------------------------------------------
			
			//----Obtener datos del Cliente:-----------------------------------------------------
			ClienteDAO clienteDAO = new ClienteDAO(_dso);
			clienteDAO.detallesCliente(String.valueOf(idClienteTransfiere));
			_datosCliente = clienteDAO.getDataSet();
			if (_datosCliente.count() > 0) {
				_datosCliente.next();
				tipperIdtransfiere = _datosCliente.getValue("tipper_id");
				cedRifCliente = _datosCliente.getValue("client_cedrif");
			}
			//------------------------------------------------------------------------------
			
			//-----Validaciones Campos---------------------------------
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

			if (_req.getParameterValues("transferir") != null) {
				int j = 0;
				if (datos_titulos.next()) {
					if (!cantidad[j].equals("")) {
						if (Integer.parseInt(cantidad[j]) > Integer.parseInt(datos_titulos.getValue("cantidad_disponible"))) {
							_record.addError("Cantidad a Transferir", "No puede ser mayor a la cantidad disponible para el t&iacute;tulo: " + datos_titulos.getValue("titulo_id"));
							flag = false;
						}
						if (Integer.parseInt(cantidad[j]) == 0) {
							_record.addError("Cantidad a Transferir", "Debe ser mayor a 0 para el t&iacute;tulo: " + datos_titulos.getValue("titulo_id"));
							flag = false;
						}
					}
					j++;
				}
			}
			//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
			//---------Validación Cuenta Custodia---------------------------------------------------------
			flag = existeCuentaCustodia();
			
			//----------------------------------------------------------------------------------------------
			
		}// fin if
		return flag;
	}
	
	/**
	 * Validación Cuenta custodia del cliente para Salida Externa
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private boolean existeCuentaCustodia() throws NumberFormatException, Exception {

		if(datos_titulos.count()>0){ 
			datos_titulos.first(); datos_titulos.next();
			//Verificar si es producto SITME
			//---ITS-755: se solicito eliminar la validación para casos SUBASTA---if(datos_titulos.getValue("tipo_producto").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) || datos_titulos.getValue("tipo_producto").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){				
			if(datos_titulos.getValue("tipo_producto").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				_cuentaCustodia = getCuentaCustodia(Long.parseLong(cedRifCliente), tipperIdtransfiere);
				if (_cuentaCustodia.count() == 0) {
					_record.addError("Cliente", "El cliente seleccionado debe poseer una cuenta custodia para la salida externa de t&iacute;tulos "+datos_titulos.getValue("tipo_producto"));
					return false;
				}			
			}
		}
		
		return true;
	}

	private void limpiarSesion() {
		// remover datos de sesion
		_req.getSession().removeAttribute("cliente_recibe");
		_req.getSession().removeAttribute("salida_externa-browse.framework.page.record");
		_req.getSession().removeAttribute("cliente_trasfiere");
		_req.getSession().removeAttribute("seleccion");
		_req.getSession().removeAttribute("datasetParam");
		_req.getSession().removeAttribute("titulos_seleccionados");
		_req.getSession().removeAttribute("ordenes_blotter-find.framework.page.record");
		_req.getSession().removeAttribute("salida_externa-browse.framework.page.query-string");
		_req.getSession().removeAttribute("unidad_inversion_seleccionada");
		_req.getSession().removeAttribute("orders_clte-browse.framework.page.record");
		_req.getSession().removeAttribute("salida.externa.fecha.valor");
		_req.getSession().removeAttribute("cantidad");
		_req.getSession().removeAttribute("depositario");
		_req.getSession().removeAttribute("depositarioCentral");
		_req.getSession().removeAttribute("contraparte");
		_req.getSession().removeAttribute("contraparte_carmen_codigo");		
	}
	
	/**
	 * Busca la información de la cuenta custodia del cliente si existe
	 * @param cedRifCliente: cedula o rif numérico del cliente
	 * @param tipoPersona: tipo de persona del cliente (V,E,J,G)
	 * @return dataSet con los datos del cliente
	 * @throws Exception
	 */
	public DataSet getCuentaCustodia(long cedRifCliente, String tipoPersona) throws Exception{
			
		ClienteCuentasDAO cuentaCustodiaDAO = new ClienteCuentasDAO(_dso);
				
		cuentaCustodiaDAO.listarCuentaCustodia(String.valueOf(cedRifCliente), tipoPersona);
	
		return cuentaCustodiaDAO.getDataSet();
	}
}