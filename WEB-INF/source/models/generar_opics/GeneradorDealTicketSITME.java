/**
 * 
 */
package models.generar_opics;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.logic.IngresoOpicsSitme;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UsoCuentas;

/**
 * Genera los Deal Ticket correspondientes al proceso de SITME
 * 
 */
public class GeneradorDealTicketSITME extends MSCModelExtend {

	private Logger logger = Logger.getLogger(GeneradorDealTicketSITME.class);

	public void generarDealTicketSITME() throws Exception {
		com.bdv.infi.dao.Transaccion transaccionDao = new com.bdv.infi.dao.Transaccion(_dso);
		Statement statement = null;
		ResultSet _ordenes = null;

		try {
			transaccionDao.begin();
			statement = transaccionDao.getConnection().createStatement();
			
			UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
			DataSet _unidadesinversion = null;
			OrdenDAO ordenDAO = new OrdenDAO(_dso);

			logger.info("Generando Deal Tickets para las unidades de inversión tipo SITME...");

			// Obtenemos las unidades de inversion tipo SITME

			String insfin_id = unidadInversionDAO.listarinsfinID(ConstantesGenerales.INST_TIPO_SITME);

			int countUI = unidadInversionDAO.listarUnidadesPorInstFinanciero(insfin_id);

			// Si se obtuvieron unidades de inversion SITME
			if (countUI > 0) {
				logger.info("Unidades de Unversión tipo SITME obtenidas con éxito de la BD...");
				_unidadesinversion = unidadInversionDAO.getDataSet();

				// Para cada unidad de universión se buscan las ordenes con estatus de adjudicada
				// y se generan los Deal Tickets asociados
				while (_unidadesinversion.next()) {
					long idUI = Long.parseLong(_unidadesinversion.getValue("idUnidadInversion"));
					String nombreUI = _unidadesinversion.getValue("nombreUnidadInversion");

					// Si la unidad de inversion tiene ordenes con estatus 'ADJUDICADA'
					_ordenes = statement.executeQuery(ordenDAO.listarOrdenesPorUnidadInversion(idUI, StatusOrden.ADJUDICADA, false,0));
					logger.info("Procesando Ordenes con estatus ADJUDICADA de la Unidad de Inversion: " + nombreUI);

					/* Por cada orden se genera la orden de recompra y los Deal Tickets asociados */
					while (_ordenes.next()) {
						try {
							// ArrayList que contendrá los querys para la generación de la
							// orden de recompra y los Deal Tickets asociados
							ArrayList<String> sqlsFinales = new ArrayList<String>();

							// Crea una objeto que representa la Orden para la cual
							// se generará la orden de recompra y los Deal Ticktets
							Orden orden = new Orden();
							orden.setIdCliente(_ordenes.getLong("client_id"));
							orden.setIdOrden(_ordenes.getLong("ordene_id"));
							orden.setVehiculoTomador(_ordenes.getString("ordene_veh_tom"));

							logger.info("Procesando Orden: " + orden.getIdOrden());

							// Genera las sentencias SQL para registrar la orden de recompra y
							// los Deal Tickets en la BD
							ArrayList<String> sqls = this.pactoRecompraSITME(orden);

							for (int i = 0; i < sqls.size(); i++) {
								sqlsFinales.add(sqls.get(i));
							}

							// Pasamos el ArrayList a Array de String
							String[] sqlsFinalesArray = new String[sqlsFinales.size()];
							sqlsFinales.toArray(sqlsFinalesArray);

							// Registra la orden de recompra y los Deal Tickets en la BD
							db.execBatch(_dso, sqlsFinalesArray);

							logger.info("La Orden: " + orden.getIdOrden() + "fue procesada con éxito");
						} catch (Exception exc) {
							logger.error("[La Orden presento inconvenientes para ser procesada]--->" + "[" + _ordenes.getString("ordene_id") + "]--->",exc);
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("Error en la generación de los Deal Ticket");
			throw e;
		}finally{
			if (_ordenes != null){
				_ordenes.close();
			}
			if (statement != null){
				statement.close();
			}
			if (transaccionDao != null){
				transaccionDao.closeConnection();
			}
		}
	}

	/**
	 * Se valida que exista para los titulos correspondiente a la orden, pacto de recompra En caso de haber pactado se genera una orden con las operaciones financieras y las instrucciones de pago
	 * 
	 * @param orden
	 * @throws Exception
	 */
	// @SuppressWarnings({"unchecked","static-access"})
	public ArrayList<String> pactoRecompraSITME(Orden orden) throws Exception {

		// Objeto que permite obtener los titulos que el cliente haya pactado para recompra
		TitulosDAO titulosDAO = new TitulosDAO(_dso);

		// ArrayList que contendra los SQL para las instrucciones de pago
		ArrayList<String> sqls = new ArrayList<String>();

		// Obtiene los titulos CON NETEO que el cliente haya pactado para recompra
		boolean neteo = true;
		titulosDAO.titulosRecompra(orden.getIdOrden(), neteo);
		DataSet _titulosRecompraNeteo = titulosDAO.getDataSet();

		// Se crean las ordenes de recompra para titulos con neteo
		this.crearOrdenRecompraSITME(_titulosRecompraNeteo, orden, sqls, neteo);

		// Obtiene los titulos SIN NETEO que el cliente haya pactado para recompra
		neteo = false;
		titulosDAO.titulosRecompra(orden.getIdOrden(), neteo);
		DataSet _titulosRecompraSinNeteo = titulosDAO.getDataSet();

		// Se crean las ordenes de pacto de recompra para titulos SIN NETEO
		this.crearOrdenRecompraSITME(_titulosRecompraSinNeteo, orden, sqls, neteo);

		if (sqls.isEmpty()) {

			logger.info("[NO EXISTEN TITULOS PACTADOS PARA RECOMPRA PARA LA ORDEN NUMERO]--->" + orden.getIdOrden());

		}
		return sqls;
	} // fin verificarPactoRecompra

	/**
	 * Crea la orden de recompra dependiendo si es con NETEO o sin NETEO
	 * 
	 * @param _titulosRecompra
	 * @param orden
	 * @param sqls
	 * @param neteo
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void crearOrdenRecompraSITME(DataSet _titulosRecompra, Orden orden, ArrayList<String> sqls, boolean neteo) throws Exception {
		FechaValorDAO fechaValorDAO = new FechaValorDAO(_dso);
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(_dso);
		MonedaDAO monedaDAO = new MonedaDAO(_dso);

		Date fecha = new Date();

		int usuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
		String ip = _req.getRemoteAddr();
		String nombreUsuario = _req.getSession().getAttribute("framework.user.principal").toString();
		String sucursal = _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL).toString();

		IngresoOpicsSitme ingresoOpics = new IngresoOpicsSitme(_dso, _app, usuario, ip, nombreUsuario);

		ArrayList<String> sqlsMensajesOpics = new ArrayList<String>();

		// Se verifica si existen titulos pactados para recompra
		if (_titulosRecompra.count() > 0) {
			// logger.info("[EXISTEN TITULOS PARA RECOMPRA PARA LA ORDEN NUMERO]--->" + orden.getIdOrden());
			_titulosRecompra.first();

			// HashMap a utilizar en el proceso para agrupar montos y titulos por moneda
			HashMap<String, BigDecimal> operacionesHashMap = new HashMap<String, BigDecimal>();
			HashMap<String, ArrayList> idTitulos = new HashMap<String, ArrayList>(); // Agrupa los titulos por moneda

			while (_titulosRecompra.next()) {

				// Se crea el objeto Orden Titulo
				OrdenTitulo ordenTitulo = new OrdenTitulo();
				ordenTitulo.setMonto(Double.parseDouble(_titulosRecompra.getValue("titulo_monto")));
				ordenTitulo.setPorcentaje(_titulosRecompra.getValue("titulo_pct"));
				ordenTitulo.setPorcentajeRecompra(Double.parseDouble(_titulosRecompra.getValue("titulo_pct_recompra")));
				ordenTitulo.setTituloId(_titulosRecompra.getValue("titulo_id"));
				ordenTitulo.setUnidades(Double.parseDouble(_titulosRecompra.getValue("titulo_unidades")));
				ordenTitulo.setMontoNeteo(_titulosRecompra.getValue("TITULO_MTO_NETEO") != null && !_titulosRecompra.getValue("TITULO_MTO_NETEO").equals("") ? new BigDecimal(_titulosRecompra.getValue("TITULO_MTO_NETEO")) : new BigDecimal(0));

				// Calculamos el monto para el precio de recompra indicado
				BigDecimal precioRecompra = new BigDecimal(_titulosRecompra.getValue("titulo_pct_recompra"));
				BigDecimal tituloMonto = new BigDecimal(_titulosRecompra.getValue("titulo_monto"));
				BigDecimal montoRecompra = new BigDecimal(0);
				montoRecompra = tituloMonto.setScale(3, BigDecimal.ROUND_HALF_EVEN).multiply(precioRecompra.setScale(3, BigDecimal.ROUND_HALF_EVEN));
				montoRecompra = montoRecompra.divide(new BigDecimal(100).setScale(3, BigDecimal.ROUND_HALF_EVEN));

				// logger.info("[CALCULANDO PRECIO DE RECOMPRA...(MontoRecompra = tituloMonto*PrecioRecompra)/100...]--->" +montoRecompra);

				boolean contiene = operacionesHashMap.containsKey(_titulosRecompra.getValue("titulo_moneda_neg"));

				/*
				 * Se verifica si el HashMap contiene la Clave de la moneda, de ser asi se le suma el monto calculando el monto de recompra, de no contenerlo, se agrega la nueva clave valor. De igual forma se agrega el objeto orden titulo al arrayList perteneciente a la misma moneda
				 */
				if (contiene) {

					BigDecimal monto = operacionesHashMap.get(_titulosRecompra.getValue("titulo_moneda_neg"));
					monto = monto.setScale(3, BigDecimal.ROUND_HALF_EVEN).add(montoRecompra).setScale(3, BigDecimal.ROUND_HALF_EVEN);
					operacionesHashMap.put(_titulosRecompra.getValue("titulo_moneda_neg"), monto);
					idTitulos.get(_titulosRecompra.getValue("titulo_moneda_neg")).add(ordenTitulo);

				} else {

					operacionesHashMap.put(_titulosRecompra.getValue("titulo_moneda_neg"), montoRecompra);
					ArrayList<OrdenTitulo> titulosId = new ArrayList<OrdenTitulo>();
					titulosId.add(ordenTitulo);
					idTitulos.put(_titulosRecompra.getValue("titulo_moneda_neg"), titulosId);

				} // fin else
			} // fin while titulos en recompra

			/*
			 * Se itera el HashMap para crear las Ordenes con sus titulos asociados, la operacion financiera, las instrucciones de pago y el deal para renta fija
			 */
			Iterator<Entry<String, ArrayList>> iterator = idTitulos.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry e = (Map.Entry) iterator.next();
				String moneda = e.getKey().toString();
				ArrayList<OrdenTitulo> ordenTituloArrayList = (ArrayList) e.getValue();

				// Generamos una orden de Recompra
				Orden ordenRecompra = new Orden();
				InstruccionesPago instruccionesPago = new InstruccionesPago();
				com.bdv.infi.data.FechaValor fechaValor = new com.bdv.infi.data.FechaValor();
				fechaValor = fechaValorDAO.listar(com.bdv.infi.logic.interfaces.FechaValor.RECOMPRA_TITULOS);

				// Orden Recompra
				ordenRecompra.setCarteraPropia(false);
				ordenRecompra.setTerminal(ip);
				ordenRecompra.setVehiculoTomador(orden.getVehiculoTomador());
				ordenRecompra.setSucursal(sucursal);
				ordenRecompra.setNombreUsuario(nombreUsuario);
				ordenRecompra.setIdCliente(orden.getIdCliente());
				ordenRecompra.setStatus(StatusOrden.REGISTRADA);
				ordenRecompra.setIdTransaccion(TransaccionNegocio.PACTO_RECOMPRA);
				ordenRecompra.setFechaOrden(fecha);
				ordenRecompra.setFechaValor(fechaValor.getFechaValor()); // pendiente fechas valores
				ordenRecompra.setIdOrdenRelacionada(orden.getIdOrden());

				ordenRecompra.agregarOrdenTitulo(ordenTituloArrayList);

				// Si los titulos fueron pactados para recompra con neteo
				if (neteo) {
					// Creamos la operacion financiera
					OrdenOperacion operacion = new OrdenOperacion();
					operacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
					operacion.setAplicaReverso(false);
					operacion.setMontoOperacion(operacionesHashMap.get(moneda));
					operacion.setTasa(new BigDecimal(0));
					operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
					operacion.setIdMoneda(moneda);
					operacion.setFechaAplicar(fechaValor.getFechaValor());

					// Agregamos la operacion financiera a la orden
					ordenRecompra.agregarOperacion(operacion);

					// Se agrega la data extendida del tipo instruccion
					OrdenDataExt ordenDataExt = new OrdenDataExt();
					ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
					ordenDataExt.setValor(String.valueOf(instruccionesPago.getTipoInstruccionId()));
					ordenRecompra.agregarOrdenDataExt(ordenDataExt);

					String[] consultas = ordenDAO.insertar(ordenRecompra);

					for (int i = 0; i < consultas.length; i++) {
						sqls.add(consultas[i]);
					}

					// logger.info("Invocacion de metodo rentaFija para SITME");
					sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra);

					for (int i = 0; i < sqlsMensajesOpics.size(); i++) {
						sqls.add(sqlsMensajesOpics.get(i).toString());
					}

				} else { // De lo contrario, si los titulos no se pactaron a neteo

					/*
					 * Por los diferentes tipos de moneda se genera la operacion financiera, la instruccion de pago y el deal ticket renta fija
					 */
					/*ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16
					 * monedaDAO.listarMonedaLocal();
					monedaDAO.getDataSet().first();
					monedaDAO.getDataSet().next();

					String monedaLocal = monedaDAO.getDataSet().getValue("moneda_id");*/
					String monedaLocal = monedaDAO.listarIdMonedaLocal();
					
					if (moneda.equalsIgnoreCase(monedaLocal)) {

						// Buscamos la Instruccion de pago definida para transferencia cuenta nacional
						boolean existeInstruccionPago = clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(), TipoInstruccion.CUENTA_NACIONAL, UsoCuentas.RECOMPRA);

						if (existeInstruccionPago) {
							if (clienteCuentasDAO.getDataSet().count() > 0) {
								clienteCuentasDAO.getDataSet().first();
								clienteCuentasDAO.getDataSet().next();
							}

							instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_NACIONAL);

							// Creamos la operacion financiera
							OrdenOperacion operacion = new OrdenOperacion();
							operacion.setStatusOperacion(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
							operacion.setAplicaReverso(false);
							operacion.setMontoOperacion(operacionesHashMap.get(moneda));
							operacion.setTasa(new BigDecimal(0));
							operacion.setNumeroCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_numero"));
							operacion.setNombreReferenciaCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_nombre"));
							operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
							operacion.setIdMoneda(moneda);
							operacion.setFechaAplicar(fechaValor.getFechaValor());

							// Agregamos la operacion financiera a la orden
							ordenRecompra.agregarOperacion(operacion);

							// Se agrega como data extendida el nombre del beneficiario
							OrdenDataExt ordenDataExt = new OrdenDataExt();
							ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
							ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
							ordenRecompra.agregarOrdenDataExt(ordenDataExt);

							// Se agrega como data extendida la cedula del beneficiario
							ordenDataExt = new OrdenDataExt();
							ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
							ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("CEDULA_BENEFICIARIO"));
							ordenRecompra.agregarOrdenDataExt(ordenDataExt);
						} else {
							throw new Exception("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA LOCAL PARA LA ORDEN]--->" + orden.getIdOrden());
						}
					} else { // De lo contrario, si la moneda es extranjera buscamos las instrucciones de pago
						// definidas para dicha transacción
						// Si la moneda no es local, sabemos que es moneda extranjera y buscamos las instrucciones de pago definidas
						// para dicha transaccion

						// Buscamos la Instruccion de pago definida para transferencia cuenta internacional o cheque
						boolean cheque = clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(), TipoInstruccion.CHEQUE, UsoCuentas.RECOMPRA);

						// Se verifica si existe una instruccion de pago definida para pago de cheque
						if (cheque) {

							// logger.info("[INSTRUCCION DE PAGO]--->[CHEQUE]");
							/*
							 * clienteCuentasDAO.getDataSet().first(); clienteCuentasDAO.getDataSet().next();
							 */
							instruccionesPago.setTipoInstruccionId(TipoInstruccion.CHEQUE);

							// Creamos la operacion financiera
							OrdenOperacion operacion = new OrdenOperacion();
							operacion.setStatusOperacion(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
							operacion.setAplicaReverso(false);
							operacion.setMontoOperacion(operacionesHashMap.get(moneda));
							operacion.setTasa(new BigDecimal(0));
							operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
							operacion.setIdMoneda(moneda);
							operacion.setFechaAplicar(fechaValor.getFechaValor());

							ordenRecompra.agregarOperacion(operacion);

							// Se agrega como data extendida el nombre del beneficiario
							OrdenDataExt ordenDataExt = new OrdenDataExt();
							ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
							ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
							ordenRecompra.agregarOrdenDataExt(ordenDataExt);

							// Se agrega como data extendida la cedula del beneficiario
							ordenDataExt = new OrdenDataExt();
							ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
							ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("CEDULA_BENEFICIARIO"));
							ordenRecompra.agregarOrdenDataExt(ordenDataExt);
						} // FIN if(cheque)
						// Si no existe una operacion de cheque, deberia estar definida una para transferencia internacional SWIFT
						else {
							// logger.info("[INSTRUCCION DE PAGO]--->[SWIFT]");

							// boolean swift = clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(),TipoInstruccion.CUENTA_SWIFT,UsoCuentas.RECOMPRA);
							boolean swift = clienteCuentasDAO.existeCuentaClienteSwift(orden.getIdCliente());

							if (!swift) {
								logger.error("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA EXTRANJERA PARA LA ORDEN]--->" + orden.getIdOrden());
								throw new Exception("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA EXTRANJERA PARA LA ORDEN]--->" + orden.getIdOrden());

							} else {

								clienteCuentasDAO.getDataSet().first();
								clienteCuentasDAO.getDataSet().next();

								instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_SWIFT);

								// Creamos la operacion financiera
								OrdenOperacion operacion = new OrdenOperacion();
								operacion.setStatusOperacion(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
								operacion.setAplicaReverso(false);
								operacion.setMontoOperacion(operacionesHashMap.get(moneda));
								operacion.setTasa(new BigDecimal(0));
								operacion.setNumeroCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_numero"));
								operacion.setNombreReferenciaCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_nombre"));
								operacion.setNombreBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bco"));
								operacion.setDireccionBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_direccion"));
								operacion.setCodigoSwiftBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_swift"));
								operacion.setCodigoBicBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bic"));
								operacion.setTelefonoBanco(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_telefono"));
								operacion.setCodigoABA(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_aba"));
								operacion.setPaisBancoCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_pais"));
								operacion.setNombreBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bco"));
								operacion.setDireccionBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_direccion"));
								operacion.setCodigoSwiftBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_swift"));
								operacion.setCodigoBicBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bic"));
								operacion.setTelefonoBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_telefono"));
								operacion.setCodigoABAIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_aba"));
								operacion.setPaisBancoIntermediario(clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_pais"));
								operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
								operacion.setFechaAplicar(fechaValor.getFechaValor());

								// Agregamos la operacion financiera a la orden
								ordenRecompra.agregarOperacion(operacion);

								// Se agrega como data extendida el nombre del beneficiario
								OrdenDataExt ordenDataExt = new OrdenDataExt();
								ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
								ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
								ordenRecompra.agregarOrdenDataExt(ordenDataExt);

								// Se agrega como data extendida la cedula del beneficiario
								ordenDataExt = new OrdenDataExt();
								ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
								ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("CEDULA_BENEFICIARIO"));
								ordenRecompra.agregarOrdenDataExt(ordenDataExt);
							}
						}
					}

					// Se agrega la data extendida del tipo instruccion
					OrdenDataExt ordenDataExt = new OrdenDataExt();
					ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
					ordenDataExt.setValor(String.valueOf(instruccionesPago.getTipoInstruccionId()));
					ordenRecompra.agregarOrdenDataExt(ordenDataExt);

					String[] consultas = ordenDAO.insertar(ordenRecompra);

					for (int i = 0; i < consultas.length; i++) {
						sqls.add(consultas[i]);
					}// fin for

					// Generacion de los deal tickets por titulo
					// logger.info("Invocacion de metodo rentaFija para SITME");
					sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra);
					for (int i = 0; i < sqlsMensajesOpics.size(); i++) {
						sqls.add(sqlsMensajesOpics.get(i).toString());
					}// fin for

				}

			} // fin iteracion del hashmap idTitulos

		} // FIN if(_titulosRecompra.count()>0){

	}// FIN crearOrdenRecompra
}
