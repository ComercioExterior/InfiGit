package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import megasoft.Util;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.EquivalenciaPortafolioDAO;
import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.dao.OperadorDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_altair.consult.ManejoDeClientes;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpics;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsOperacionCambio;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsRentaFija;
import com.bdv.infi.webservices.beans.Cliente;

/**
 * Clase encargada de generar los Deal Tickets elaucho,nvisbal
 */
public class IngresoOpics {

	/*** DataSource */
	protected DataSource _dso;

	/*** Contexto de la Aplicación */
	protected ServletContext _app;

	/*** Id del usuario */
	protected long idUsuario;

	/*** Direcci&oacute;n Ip */
	protected String ip;

	/*** UserName */
	protected String userName = null;

	/*** Logger APACHE */
	private Logger logger = Logger.getLogger(IngresoOpics.class);
	
	/**Un hashmap que almacena los valores por defecto de los mensajes*/
	protected HashMap<String, HashMap> parametrosMensajes = new HashMap<String, HashMap>();

	//DAO
	protected ParametrosDAO parametrosDAO;
	
	/**
	 * Constructor de la Clase
	 * 
	 * @param DataSource
	 *            _dso
	 * @param ServletContext
	 *            _app
	 * @param long idUsuario
	 */
	public IngresoOpics(DataSource _dso, ServletContext _app, long idUsuario, String ip, String userName) {
		this._dso = _dso;
		this._app = _app;
		this.idUsuario = idUsuario;
		this.ip = ip;
		this.userName = userName;
		parametrosDAO = new ParametrosDAO(_dso);
	}

	/**
	 * M&eacute;todo que genera el deal para Renta Fija por Orden. Busca el cliente en ALTAIR,con el segmento al cual pertenece se busca su equivalencia en INFI_TB_047_EQUIV_PORTAFOLIO
	 * 
	 * @param Orden
	 *            orden
	 * @return ArrayList<String> consultas para ser ejecutadas
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked", "static-access" })
	public ArrayList<String> rentaFija(Orden orden,String... tipoOperacion) throws Exception {

		logger.info("ORDEN RENTA FIJA--->" + orden.getIdOrden());
		ArrayList<String> sqlsArrayList = new ArrayList<String>();
		ArrayList<OrdenTitulo> arrayListOrdenTitulo = orden.getOrdenTitulo();
		MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(this._dso);
		HashMap<String, String> parametrosHashMap = new HashMap<String, String>();
		String nombreCliente = "";
		String cedula = "";
		String tipo_persona = "";
		String segmento = "";
		MensajeOpics mensajeOpics = new MensajeOpics();
		String vehiculo = "";
		OrdenDAO ordenDAO = new OrdenDAO(_dso);

		// Si el vehiculo tomador es diferente de NULL, le seteamos al deal el BRANCH,buscandolo en Base de Datos
		if (orden.getVehiculoTomador() != null && !orden.getVehiculoTomador().equals("")) {
			VehiculoDAO vehiculoDAO = new VehiculoDAO(this._dso);
		    vehiculo=vehiculoDAO.listarCampoPorId(orden.getVehiculoTomador(),"vehicu_branch");

		}// fin if

		// Buscamos la cedula del cliente en INFI
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		ClienteDAO clienteOpicsDAO = new ClienteDAO(_dso);
		clienteDAO.buscarDatosPorIdCliente(String.valueOf(orden.getIdCliente()));

		if (clienteDAO.getDataSet().count() > 0) {
			clienteDAO.getDataSet().first();
			clienteDAO.getDataSet().next();
			nombreCliente = clienteDAO.getDataSet().getValue("CLIENT_NOMBRE");
			cedula = clienteDAO.getDataSet().getValue("CLIENT_CEDRIF");
			tipo_persona = clienteDAO.getDataSet().getValue("TIPPER_ID");
			segmento = clienteDAO.getDataSet().getValue("CTESEG_ID")==null?"":clienteDAO.getDataSet().getValue("CTESEG_ID");
			
			logger.info("Sitme: false");
			parametrosHashMap = parametrosDAO.buscarParametros(ParametrosSistema.MENSAJE_OPICS_RF);

			// Buscamos informacion del cliente en ALTAIR (Se cambio para obtenerlo de la data almacenada en INFI del cliente
			//ManejoDeClientes manejoDeClientes = new ManejoDeClientes(_dso);
			//Cliente clienteWS = new Cliente();
			//clienteWS = manejoDeClientes.obtenerClienteAltair(cedula, tipo_persona, ip, _app, false, false, true, false, this.userName);
			//String segmento = clienteWS.getPEM1403().getSegmento().trim();
			String portafolio = parametrosHashMap.get(ParametrosSistema.PORT);

			// Con el segmento buscamos en la tabla INFI_TB_047_EQUIV_PORTAFOLIO para setear el valor a el Deal
			EquivalenciaPortafolioDAO equivalenciaPortafolioDAO = new EquivalenciaPortafolioDAO(_dso);
			equivalenciaPortafolioDAO.listarEquivalenciaPortafolio(segmento);

			if (equivalenciaPortafolioDAO.getDataSet().count() > 0) {
				equivalenciaPortafolioDAO.getDataSet().first();
				equivalenciaPortafolioDAO.getDataSet().next();
				portafolio = equivalenciaPortafolioDAO.getDataSet().getValue("portafolio");
			}// fin equivalenciaPortafolioDAO

			// Le solicito a la lista que me devuelva un iterador con todos los elementos contenidos en ella
			Iterator<OrdenTitulo> iterador = arrayListOrdenTitulo.listIterator();

			// Mensaje de Renta Fija
			MensajeOpicsRentaFija mensajeOpicsRentaFija = null;

			// Se establecen los valores por defecto
			mensajeOpics.setFechaValor(orden.getFechaValor());
			mensajeOpics.setIdUsuario(idUsuario);

			// Mientras que el iterador tenga un proximo elemento
			while (iterador.hasNext()) {
				mensajeOpicsRentaFija = new MensajeOpicsRentaFija();
				mensajeOpicsDAO.estableceValoresPorDefecto(mensajeOpicsRentaFija);

				// Set de clave valor
				OrdenTitulo ordenTitulo = (OrdenTitulo) iterador.next();
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SECID, ordenTitulo.getTituloId());

				// Parametro CNO OPICS				
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.ORDENEID, String.valueOf(orden.getIdOrden()));
				
				//INCIDENCIA CALIDAD - Busqueda del cliente en OPICS
				//Error en orden Opics:  Envio de CNO Generico, solucion de busqueda de CNO de cliente codificada 08/11/12
				
				clienteOpicsDAO.clienteOpics(cedula, tipo_persona);
				if(clienteOpicsDAO.getDataSet().count()>0) {
					clienteOpicsDAO.getDataSet().first();
					clienteOpicsDAO.getDataSet().next();
					
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO, clienteOpicsDAO.getDataSet().getValue("ID_CLIENTE"));
					
				} else {
					
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO, parametrosHashMap.get(ParametrosSistema.CNO_OPICS));	
				}
				
				
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.ORIGQTY, String.valueOf(new BigDecimal(ordenTitulo.getUnidades())));
				
				//Si es de cartera propia la orden se obtiene el precio de compra, de lo contrario el precio de recompra
				if(orden.getIdTransaccion()!= null && orden.getIdTransaccion().equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)){
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PRICE_8, String.valueOf(orden.getPrecioCompra()));
				}else{
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PRICE_8, String.valueOf(ordenTitulo.getPorcentajeRecompra()));	
				}
				
				//mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTDATE, String.valueOf(mensajeOpicsRentaFija.establecerFecha(orden.getFechaValor())));
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PORT, portafolio);
				//mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALDATE, mensajeOpicsRentaFija.establecerFecha(orden.getFechaOrden()));
				

				if (orden.getIdOrdenRelacionada() > 0 ){
					//Busca la ordenRelacionada
					Orden ordenPadre = ordenDAO.listarOrden(orden.getIdOrdenRelacionada(),false,false,false,false,false);
					
					//INCIDENCIA CALIDAD - SOLICITUD DESDE OPICS
					//Error en Opics: Rechazo en numero de solicitud de recompra 09/11/12
					if(tipoOperacion.length>0){						
						if(tipoOperacion[0].equals(TransaccionNegocio.VENTA_TITULOS)){							
							  mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PLEDGEID,TransaccionNegocio.RECOMPRA);
						}
					} else {						
						mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PLEDGEID, String.valueOf(ordenPadre.getIdOrden()));	
					}
					

					//Fecha valor
					
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTDATE, String.valueOf(mensajeOpicsRentaFija.establecerFecha(ordenPadre.getFechaPactoRecompra())));
					// Fecha pacto de la operación
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALDATE, mensajeOpicsRentaFija.establecerFecha(ordenPadre.getFechaOrden()));
				}else{
					
					if(tipoOperacion.length>0){
						if(tipoOperacion[0].equals(TransaccionNegocio.VENTA_TITULOS)){
							  mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PLEDGEID,TransaccionNegocio.RECOMPRA);
						}
					} else {
						mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PLEDGEID, String.valueOf(orden.getIdOrden()));	
					}
					
					//Fecha valor
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTDATE, String.valueOf(mensajeOpicsRentaFija.establecerFecha(orden.getFechaValor())));
					// Fecha pacto de la operación
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALDATE, mensajeOpicsRentaFija.establecerFecha(orden.getFechaOrden()));
				}
				
				
				// Si el vehiculo es diferente de null
				if (orden.getVehiculoTomador() != null && !orden.getVehiculoTomador().equals("")) {
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.BRANCH, vehiculo);
				}
				// Se utiliza el util replace para realizar el reemplazo de {cedula} y {nombre}
				String valor = Util.replace(Util.replace(mensajeOpicsRentaFija.get(mensajeOpicsRentaFija.DEALTEXT), "{cedula}", tipo_persona + cedula), "{nombre}", nombreCliente);
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALTEXT, valor);
				mensajeOpics.agregarMensajeDetalle(mensajeOpicsRentaFija);

			}// fin while

			String sqls[] = mensajeOpicsDAO.ingresar(mensajeOpics);

			/*
			 * Actualizamos el id_opics de la orden
			 */			
			sqlsArrayList.add(ordenDAO.actualizarCampoOpics(mensajeOpics.getIdOpics(), orden.getIdOrden()));

			for (int i = 0; i < sqls.length; i++) {
				sqlsArrayList.add(sqls[i]);
			}// fin for
		}// fin if clienteDAO
		return sqlsArrayList;
	}// fin rentaFija

	/**
	 * Arma un deal ticket de operación de cambio FX OPICS. Este mensaje se arma cuando se quiere pagar al cliente en una moneda distinta a la de negociación del título. Ejemplo el cliente desea que los títulos que mantiene en dólares le sean pagados en bolívares, por lo que solicita una operación de cambio moneda base = USD, moneda de conversión = VEF.
	 * 
	 * @param orden
	 *            Objeto orden que contiene la acción ejecutada
	 * @param nmUsuario
	 *            el nombre del usuario que se conecta a la aplicación
	 * @param cuentaClienteAltair
	 *            el código de cuenta del cliente 20 dígitos
	 * @param idMonedaBase
	 *            la moneda original de la operación de cambio.
	 * @param idMonedaConversion
	 *            la moneda de conversión que se debe usar para la conversión.
	 */
	@SuppressWarnings("static-access")
	public ArrayList<String> operacionCambio(Orden orden, String nmUsuario, String cuentaClienteAltair, String idMonedaBase, String idMonedaConversion, BigDecimal montoOperacion, BigDecimal tasaCambio) throws Exception {

		logger.info("ORDEN OPERACION CAMBIO-->" + orden.getIdOrden());
		ArrayList<String> sqlsArrayList = new ArrayList<String>();
		MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(this._dso);
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		String nombreCliente = "";
		String cedula = "";
		String tipo_persona = "";
		MensajeOpics mensajeOpics = new MensajeOpics();
		String trader = null;
		String vehiculo = "";
		String segmento = "";

		// Si el vehiculo tomador es diferente de NULL, le seteamos al deal el BRANCH,buscandolo en Base de Datos
		if (orden.getVehiculoTomador() != null && !orden.getVehiculoTomador().equals("")) {
			VehiculoDAO vehiculoDAO = new VehiculoDAO(this._dso);
			vehiculoDAO.listarPorId(orden.getVehiculoTomador());

			if (vehiculoDAO.getDataSet().count() > 0) {
				vehiculoDAO.getDataSet().first();
				vehiculoDAO.getDataSet().next();
				vehiculo = vehiculoDAO.getDataSet().getValue("vehicu_branch");
			}// fin if
		}// fin if

		// Buscamos la cedula del cliente en INFI
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		clienteDAO.listarPorId(String.valueOf(orden.getIdCliente()));

		if (clienteDAO.getDataSet().count() > 0) {
			clienteDAO.getDataSet().first();
			clienteDAO.getDataSet().next();
			nombreCliente = clienteDAO.getDataSet().getValue("CLIENT_NOMBRE");
			cedula = clienteDAO.getDataSet().getValue("CLIENT_CEDRIF");
			tipo_persona = clienteDAO.getDataSet().getValue("TIPPER_ID");
			segmento = clienteDAO.getDataSet().getValue("CTESEG_ID")==null?"":clienteDAO.getDataSet().getValue("CTESEG_ID");
		}// fin if clienteDAO

		// Se buscan los parametros
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		HashMap<String, String> parametrosHashMap = parametrosDAO.buscarParametros(ParametrosSistema.MENSAJE_OPICS_OC);

		// Buscamos informacion del cliente en ALTAIR
//		ManejoDeClientes manejoDeClientes = new ManejoDeClientes(_dso);
//		Cliente clienteWS = new Cliente();
//		clienteWS = manejoDeClientes.obtenerClienteAltair(cedula, tipo_persona, ip, _app, false, false, true, false, this.userName);
//		String segmento = clienteWS.getPEM1403().getSegmento().trim();
		String portafolio = parametrosHashMap.get(ParametrosSistema.PORT);

		// Con el segmento buscamos en la tabla INFI_TB_047_EQUIV_PORTAFOLIO para setear el valor a el Deal
		EquivalenciaPortafolioDAO equivalenciaPortafolioDAO = new EquivalenciaPortafolioDAO(_dso);
		equivalenciaPortafolioDAO.listarEquivalenciaPortafolio(segmento);

		if (equivalenciaPortafolioDAO.getDataSet().count() > 0) {
			equivalenciaPortafolioDAO.getDataSet().first();
			equivalenciaPortafolioDAO.getDataSet().next();
			portafolio = equivalenciaPortafolioDAO.getDataSet().getValue("portafolio");
		}// fin equivalenciaPortafolioDAO

		// Arma el mensaje de opics de operación de cambio
		MensajeOpicsOperacionCambio mOpicsOperacionCambio = new MensajeOpicsOperacionCambio();
		mensajeOpicsDAO.estableceValoresPorDefecto(mOpicsOperacionCambio);

		// Busca el trader, si lo consigue lo establece
		trader = obtenerOperador(nmUsuario);
		if (trader != null) {
			mOpicsOperacionCambio.set(mOpicsOperacionCambio.TRAD, trader);
		} else {
			trader = parametrosHashMap.get(ParametrosSistema.TRADER);
		}

		// Establece a que orden estara relacionado eld eal
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.ORDENEID, String.valueOf(orden.getIdOrden()));
		// Establece el centro de costo
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.COST, parametrosHashMap.get(ParametrosSistema.COST));
		// Establece el número de cuenta
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.CTRSACCT, mensajeOpicsDAO.convertirCuenta20A12(cuentaClienteAltair));
		// Establece la tasa de cambio
		tasaCambio = tasaCambio.setScale(8, 2); // 8 decimales
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.CCYRATE_8, tasaCambio.toString());
		// Establece la moneda base
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.CCY, idMonedaBase);
		// Establece la moneda de conversión
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.CTRCCY, idMonedaConversion);

		if (orden.getIdOrdenRelacionada() > 0 ){
			//Busca la ordenRelacionada
			Orden ordenPadre = ordenDAO.listarOrden(orden.getIdOrdenRelacionada(),false,false,false,false,false);			
			//Fecha valor
			mOpicsOperacionCambio.set(mOpicsOperacionCambio.VDATE, String.valueOf(mOpicsOperacionCambio.establecerFecha(ordenPadre.getFechaValor())));
			// Fecha pacto de la operación
			mOpicsOperacionCambio.set(mOpicsOperacionCambio.DEALDATE, mOpicsOperacionCambio.establecerFecha(ordenPadre.getFechaOrden()));
		}else{
			// Número de orden
			// mOpicsOperacionCambio.set(mOpicsOperacionCambio.FEDEALNO, String.valueOf(orden.getIdOrden()));
			// Fecha Valor de la operación opics
			mOpicsOperacionCambio.set(mOpicsOperacionCambio.VDATE, String.valueOf(mOpicsOperacionCambio.establecerFecha(orden.getFechaValor())));
			// Fecha pacto de la operación
			mOpicsOperacionCambio.set(mOpicsOperacionCambio.DEALDATE, mOpicsOperacionCambio.establecerFecha(orden.getFechaOrden()));			
		}
		// Valores adicionales
		String valor = Util.replace(Util.replace(mOpicsOperacionCambio.get(mOpicsOperacionCambio.DEALTEXT), "{cedula}", tipo_persona + cedula), "{nombre}", nombreCliente);
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.DEALTEXT, valor);
		// Obtiene el portafolio
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.PORT, portafolio);
		// Monto de la operación
		montoOperacion = montoOperacion.setScale(4, BigDecimal.ROUND_HALF_EVEN); // 4 decimales
		mOpicsOperacionCambio.set(mOpicsOperacionCambio.CCYAMT, montoOperacion.toString());

		mensajeOpics.setFechaValor(orden.getFechaValor());
		mensajeOpics.setIdUsuario(idUsuario);

		if (orden.getVehiculoTomador() != null && !orden.getVehiculoTomador().equals("")) {
			mOpicsOperacionCambio.set(mOpicsOperacionCambio.BRANCH, vehiculo);
		}

		mensajeOpics.agregarMensajeDetalle(mOpicsOperacionCambio);
		String sqls[] = mensajeOpicsDAO.ingresar(mensajeOpics);
		orden.setIdEjecucion(mensajeOpics.getIdOpics());

		/*
		 * Actualizamos el id_opics de la orden
		 */
		sqlsArrayList.add(ordenDAO.actualizarCampoOpics(mensajeOpics.getIdOpics(), orden.getIdOrden()));

		for (int i = 0; i < sqls.length; i++) {
			sqlsArrayList.add(sqls[i]);
		}// fin for
		return sqlsArrayList;
	}

	/** Busca el operador o trader asociado al usuario. Devuelve null en caso de no estar asociado ninguno */
	private String obtenerOperador(String nmUsuario) throws Exception {
		OperadorDAO operadorDao = new OperadorDAO(this._dso);
		return operadorDao.listarOperador(nmUsuario);
	}

	/**
	 * Método que devuelve los parámetros por defecto del mensaje OPICS según el tipo recibido. Usa cache para evitar múltiples accesos a base de datos
	 * 
	 * @param tipoMensaje
	 *            tipo de mensaje a buscar
	 * @return hashmap de parámetros por defecto
	 * @throws Exception
	 *             en caso de error
	 */
	protected HashMap<String, String> getParametrosPorDefecto(String tipoMensaje) throws Exception {
		if (parametrosMensajes.containsKey(tipoMensaje)) {
			return parametrosMensajes.get(tipoMensaje);
		} else {
			HashMap<String, String> parametrosHashMap = new HashMap<String, String>();
			parametrosHashMap = parametrosDAO.buscarParametros(tipoMensaje);
			parametrosMensajes.put(tipoMensaje, parametrosHashMap);
			return parametrosHashMap;
		}
	}

}// fin clase IngresoOpics
