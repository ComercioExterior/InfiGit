package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.Util;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.EquivalenciaPortafolioDAO;
import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.OrdenesCruce;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpics;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsRentaFija;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de generar los Deal Tickets de SITME nvisbal
 */
public class IngresoOpicsSitme extends IngresoOpics {


	/*** Logger APACHE */
	private Logger logger = Logger.getLogger(IngresoOpicsSitme.class);

	private String vehiculo = "";
	private String nombreCliente = "";
	private String cedula = "";
	private String tipo_persona = "";
	private HashMap<String, String> parametrosHashMap = new HashMap<String, String>();
	private String segmento = "";
	private String portafolio = "";
	
	/**
	 * Constructor de la Clase
	 * 
	 * @param DataSource
	 *            _dso
	 * @param ServletContext
	 *            _app
	 * @param long idUsuario
	 */
	public IngresoOpicsSitme(DataSource _dso, ServletContext _app, long idUsuario, String ip, String userName) {
		super(_dso, _app, idUsuario, ip, userName);
	}

	/**
	 * Genera los mensajes opics relacionados al proceso de adjudicación de SITME
	 * 
	 * @param orden
	 *            orden adjudicada
	 * @return lista de sql que deben ejecutarse
	 * @throws Exception
	 *             en caso de error
	 */
	public ArrayList<String> rentaFija(Orden orden) throws Exception {
		ArrayList<String> sqlsArrayList = new ArrayList<String>();
		MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(this._dso);
		MensajeOpics mensajeOpics = new MensajeOpics();
		
		logger.debug("Opics renta fija de compra--->" + orden.getIdOrden());
		//System.out.println("Opics renta fija de compra--->" + orden.getIdOrden());
		rentaFija(orden, true, mensajeOpics);
		logger.debug("Opics renta fija de venta--->" + orden.getIdOrden());
		//System.out.println("Opics renta fija de venta--->" + orden.getIdOrden());
		rentaFija(orden, false, mensajeOpics);
		String sqls[] = mensajeOpicsDAO.ingresar(mensajeOpics);

		/*
		 * Actualizamos el id_opics de la orden
		 */
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		sqlsArrayList.add(ordenDAO.actualizarCampoOpics(mensajeOpics.getIdOpics(), orden.getIdOrden()));

		for (int i = 0; i < sqls.length; i++) {
			sqlsArrayList.add(sqls[i]);
		}// fin for
		return sqlsArrayList;
	}

	/**
	 * M&eacute;todo que genera el deal para Renta Fija por Orden de tipo SITME Busca el cliente en ALTAIR,con el segmento al cual pertenece se busca su equivalencia en INFI_TB_047_EQUIV_PORTAFOLIO
	 * 
	 * @param Orden
	 *            orden orden que se está adjudicando
	 * @return ArrayList<String> consultas para ser ejecutadas
	 * @throws Exception
	 *             en caso de error
	 */
	@SuppressWarnings( { "unchecked", "static-access" })
	protected void rentaFija(Orden orden, boolean compra, MensajeOpics mensajeOpics) throws Exception {
		logger.info("Generar deal de compra= "+compra+" de la orden"+orden.getIdOrden());
		ArrayList<OrdenTitulo> arrayListOrdenTitulo = orden.getOrdenTitulo();
		//MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(this._dso);
		ClienteCuentasDAO clienteCuenta=new ClienteCuentasDAO(this._dso);
		TitulosDAO tituloDao=new TitulosDAO(this._dso);
		if (vehiculo.equals("")){
			getVehiculo(orden);	
		}
		
		if (cedula.equals("")){
			getCliente(orden);	
		}		

		// Se buscan los parametros		
		if (compra) {
			parametrosHashMap = getParametrosPorDefecto(ParametrosSistema.MENSAJE_OPICS_RF_SITME);
		} else {
			parametrosHashMap = getParametrosPorDefecto(ParametrosSistema.MENSAJE_OPICS_RF_SITME_VENTA);
		}

		if (portafolio.equals("")){
		portafolio = parametrosHashMap.get(ParametrosSistema.PORT);
		}

		
		// Le solicito a la lista que me devuelva un iterador con todos los
		// elementos contenidos en ella
		Iterator<OrdenTitulo> iterador = arrayListOrdenTitulo.listIterator();

		// Mensaje de Renta Fija
		MensajeOpicsRentaFija mensajeOpicsRentaFija = null;

		// Se establecen los valores por defecto
		mensajeOpics.setFechaValor(orden.getFechaValor());
		mensajeOpics.setIdUsuario(idUsuario);

		// Mientras que el iterador tenga un proximo elemento
		while (iterador.hasNext()) {

			mensajeOpicsRentaFija = new MensajeOpicsRentaFija();
			//mensajeOpicsDAO.estableceValoresPorDefecto(mensajeOpicsRentaFija);

			// Set de clave valor
			OrdenTitulo ordenTitulo = (OrdenTitulo) iterador.next();
			
			// Establece los valores por defecto de sitme
			//if (!compra) {
			//Resolucion a incidencia: Precios de compra y venta intercambiados en Deal OPICS
			if (compra) {
				//Si no hay recompra sólo genera el DEAL de compra
				if (ordenTitulo.getPorcentajeRecompra()<=0){
					break;
				}
				mensajeOpicsRentaFija.setHashMap(parametrosHashMap);
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PRICE_8, String.valueOf(ordenTitulo.getPorcentajeRecompra()));
			}else{
				mensajeOpicsRentaFija.setHashMap(parametrosHashMap);
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PRICE_8, String.valueOf(orden.getPrecioCompra()));
			}
			
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SECID, ordenTitulo.getTituloId());

			// Parametro CNO OPICS			
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PLEDGEID, String.valueOf(orden.getIdOrden()));
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.ORDENEID, String.valueOf(orden.getIdOrden()));
			//mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO, parametrosHashMap.get(ParametrosSistema.CNO_OPICS));
			
			String rif=orden.getClienteRifCed().substring(0,1);
			
			String cedula=orden.getClienteRifCed().substring(1,orden.getClienteRifCed().length());
			System.out.println("TIPO: "+rif);
			System.out.println("CEDULA: "+cedula);
			clienteCuenta.listarCuentaCustodia(cedula,rif);
				
			if(clienteCuenta.getDataSet().count()>0){
				
				clienteCuenta.getDataSet().next();
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO,clienteCuenta.getDataSet().getValue("ID_cliente"));
			
			}else {	
				
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO, parametrosHashMap.get(ParametrosSistema.CNO_OPICS));
			
			}
			
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.ORIGQTY, String.valueOf(new BigDecimal(ordenTitulo.getUnidades())));
						
			logger.info("SETTDATE: "+orden.getFechaPactoRecompra());
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTDATE, String.valueOf(mensajeOpicsRentaFija.establecerFecha(orden.getFechaPactoRecompra())));
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PORT, portafolio);
			logger.info("DEALDATE: "+orden.getFechaOrden());
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALDATE, mensajeOpicsRentaFija.establecerFecha(orden.getFechaOrden()));
			// Si el vehiculo es diferente de null
			if (orden.getVehiculoTomador() != null && !orden.getVehiculoTomador().equals("")) {
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.BRANCH, vehiculo);
			}
			// Se utiliza el util replace para realizar el reemplazo de
			// {cedula} y {nombre}
			String valor = Util.replace(Util.replace(mensajeOpicsRentaFija.get(mensajeOpicsRentaFija.DEALTEXT), "{cedula}", tipo_persona + cedula), "{nombre}", nombreCliente);
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALTEXT, valor);
			
			//solo se agrega el campo tasa para las ventas (COMPRAS EN OPICS)
			if(!compra){
				//Obtener tasa de liquidacion registrada en OPICS para el título
				String tasaLiq = tituloDao.obtenerTasaLiquidacionOPICS(ordenTitulo.getTituloId());
				if (tasaLiq != null) {
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTCCY, tasaLiq);
				} 
			}
						
			
					
			mensajeOpics.agregarMensajeDetalle(mensajeOpicsRentaFija);
			
			
		}// fin while

	}// fin rentaFija

	/**
	 * Obtiene datos del vehículo asociado a la orden
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	protected void getVehiculo(Orden orden) throws Exception {
		// Si el vehiculo tomador es diferente de NULL, le seteamos al deal el
		// BRANCH,buscandolo en Base de Datos
		if (orden.getVehiculoTomador() != null && !orden.getVehiculoTomador().equals("") && vehiculo.equals("")) {
			VehiculoDAO vehiculoDAO = new VehiculoDAO(_dso);
			vehiculoDAO.listarPorId(orden.getVehiculoTomador());

			if (vehiculoDAO.getDataSet().count() > 0) {
				vehiculoDAO.getDataSet().first();
				vehiculoDAO.getDataSet().next();
				vehiculo = vehiculoDAO.getDataSet().getValue("vehicu_branch");
			}// fin if
		}// fin if
	}

	/**
	 * Obtiene datos del cliente asociado a la orden
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	protected void getCliente(Orden orden) throws Exception {
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
			
//			ManejoDeClientes manejoDeClientes = new ManejoDeClientes(_dso);
//			Cliente clienteWS = new Cliente();
//			clienteWS = manejoDeClientes.obtenerClienteAltair(cedula, tipo_persona, ip, _app, false, false, true, false, this.userName);
//			segmento = clienteWS.getPEM1403().getSegmento().trim();
			
			// Con el segmento buscamos en la tabla INFI_TB_047_EQUIV_PORTAFOLIO
			// para setear el valor a el Deal
			EquivalenciaPortafolioDAO equivalenciaPortafolioDAO = new EquivalenciaPortafolioDAO(_dso);
			equivalenciaPortafolioDAO.listarEquivalenciaPortafolio(segmento);

			if (equivalenciaPortafolioDAO.getDataSet().count() > 0) {
				equivalenciaPortafolioDAO.getDataSet().first();
				equivalenciaPortafolioDAO.getDataSet().next();
				portafolio = equivalenciaPortafolioDAO.getDataSet().getValue("portafolio");
			}// fin equivalenciaPortafolioDAO
			
		} else {
			logger.error("Error en la búsqueda del cliente " + orden.getIdCliente());
			throw new Exception("Error en la búsqueda del cliente " + orden.getIdCliente());
		}
	}
	
	
	/**
	 * M&eacute;todo que genera el deal para Renta Fija por Orden de tipo SICAD 2 Busca el cliente en ALTAIR,con el segmento al cual pertenece se busca su equivalencia en INFI_TB_047_EQUIV_PORTAFOLIO
	 * 
	 * @param Orden
	 *            orden orden que se está adjudicando
	 * @return ArrayList<String> consultas para ser ejecutadas
	 * @throws Exception
	 *             en caso de error
	 */
	//Metodo creado en Requerimiento TTS_443_NM26659_09/05/2014
	@SuppressWarnings( { "unchecked", "static-access" })
	protected void rentaFijaSICAD2(Orden orden, boolean compra, MensajeOpics mensajeOpics) throws Exception {
		logger.info("Generar deal de compra= "+compra+" de la orden"+orden.getIdOrden());
		ArrayList<OrdenTitulo> arrayListOrdenTitulo = orden.getOrdenTitulo();
		//MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(this._dso);
		ClienteCuentasDAO clienteCuenta=new ClienteCuentasDAO(this._dso);
		TitulosDAO tituloDao=new TitulosDAO(this._dso);
		if (vehiculo.equals("")){
			getVehiculo(orden);	
		}
		
		if (cedula.equals("")){
			getCliente(orden);	
		}		

		// Se buscan los parametros		
		if (compra) {
			parametrosHashMap = getParametrosPorDefecto(ParametrosSistema.MENSAJE_OPICS_RF_SITME);
		} else {
			parametrosHashMap = getParametrosPorDefecto(ParametrosSistema.MENSAJE_OPICS_RF_SITME_VENTA);
		}

		if (portafolio.equals("")){
		portafolio = parametrosHashMap.get(ParametrosSistema.PORT);
		}

		
		// Le solicito a la lista que me devuelva un iterador con todos los
		// elementos contenidos en ella
		Iterator<OrdenTitulo> iterador = arrayListOrdenTitulo.listIterator();

		// Mensaje de Renta Fija
		MensajeOpicsRentaFija mensajeOpicsRentaFija = null;

		// Se establecen los valores por defecto
		mensajeOpics.setFechaValor(orden.getFechaValor());
		mensajeOpics.setIdUsuario(idUsuario);

		// Mientras que el iterador tenga un proximo elemento
		while (iterador.hasNext()) {

			mensajeOpicsRentaFija = new MensajeOpicsRentaFija();
			//mensajeOpicsDAO.estableceValoresPorDefecto(mensajeOpicsRentaFija);

			// Set de clave valor
			OrdenTitulo ordenTitulo = (OrdenTitulo) iterador.next();
			
			// Establece los valores por defecto de sitme
			//if (!compra) {
			//Resolucion a incidencia: Precios de compra y venta intercambiados en Deal OPICS
			if (compra) {
				//Si no hay recompra sólo genera el DEAL de compra
				if (ordenTitulo.getPorcentajeRecompra()<=0){
					break;
				}
				mensajeOpicsRentaFija.setHashMap(parametrosHashMap);
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PRICE_8, String.valueOf(ordenTitulo.getPorcentajeRecompra()));
			}else{
				mensajeOpicsRentaFija.setHashMap(parametrosHashMap);
				//Resolucion de Incidencia ITS-2093 Produccion 13/06/2014 (Deals Opics se generan al 100% del precio del titulo)
				//mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PRICE_8, String.valueOf(orden.getPrecioCompra()));
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PRICE_8, String.valueOf(orden.getOrdenTitulo().get(0).getPorcentaje()));
				//Resolucion de Incidencia ITS-2093 Produccion 16/06/2014 (Tasa del Deal se esta enviando con valor por defecto de configuracion de parametros)
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTPROCEXCHRATE_8, String.valueOf(orden.getTasaCambio()));
			}
			
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SECID, ordenTitulo.getTituloId());
			
			
			// Parametro CNO OPICS			
			String rif=orden.getClienteRifCed().substring(0,1).trim();
			
			if(rif.equalsIgnoreCase("V") || rif.equalsIgnoreCase("E")){								
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PLEDGEID, String.valueOf(orden.getIdOrden()));	
			} else {				
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PLEDGEID, parametrosHashMap.get(ParametrosSistema.PLEDGEID_JURIDICO_SICAD2));
			}
			
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.ORDENEID, String.valueOf(orden.getIdOrden()));
			//mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO, parametrosHashMap.get(ParametrosSistema.CNO_OPICS));
			
			
			
			String cedula=orden.getClienteRifCed().substring(1,orden.getClienteRifCed().length());
			//System.out.println("TIPO: "+rif);
			//System.out.println("CEDULA: "+cedula);
			clienteCuenta.listarCuentaCustodia(cedula,rif);
				
			if(clienteCuenta.getDataSet().count()>0){
				
				clienteCuenta.getDataSet().next();
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO,clienteCuenta.getDataSet().getValue("ID_cliente"));
			
			}else {	
				
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.CNO, parametrosHashMap.get(ParametrosSistema.CNO_OPICS));
			
			}
			
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.ORIGQTY, String.valueOf(new BigDecimal(ordenTitulo.getUnidades())));
						
			logger.info("SETTDATE: "+orden.getFechaPactoRecompra());
			//mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTDATE, String.valueOf(mensajeOpicsRentaFija.establecerFecha(orden.getFechaPactoRecompra())));
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTDATE, String.valueOf(mensajeOpicsRentaFija.establecerFecha(orden.getFechaValor())));

			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.PORT, portafolio);
			logger.info("DEALDATE: "+orden.getFechaOrden());
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALDATE, mensajeOpicsRentaFija.establecerFecha(orden.getFechaOrden()));
			// Si el vehiculo es diferente de null
			if (orden.getVehiculoTomador() != null && !orden.getVehiculoTomador().equals("")) {
				mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.BRANCH, vehiculo);
			}
			// Se utiliza el util replace para realizar el reemplazo de
			// {cedula} y {nombre}
			String valor = Util.replace(Util.replace(mensajeOpicsRentaFija.get(mensajeOpicsRentaFija.DEALTEXT), "{cedula}", tipo_persona + cedula), "{nombre}", nombreCliente);
			mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.DEALTEXT, valor);
			
			//solo se agrega el campo tasa para las ventas (COMPRAS EN OPICS)
			if(!compra){
				//Obtener tasa de liquidacion registrada en OPICS para el título
				String tasaLiq = tituloDao.obtenerTasaLiquidacionOPICS(ordenTitulo.getTituloId());
				if (tasaLiq != null) {
					mensajeOpicsRentaFija.set(mensajeOpicsRentaFija.SETTCCY, tasaLiq);
				} 
			}
						
			
					
			mensajeOpics.agregarMensajeDetalle(mensajeOpicsRentaFija);					
		}// fin while

	}// fin rentaFijasicad2

	/**
	 * Genera los mensajes opics relacionados al proceso de Cruces SICAD 2
	 * 
	 * @param orden
	 *            orden cruzada
	 * @return lista de sql que deben ejecutarse
	 * @throws Exception
	 *             en caso de error
	 */
	//Metodo creado en Requerimiento TTS_443_NM26659_09/05/2014
	public ArrayList<String> rentaFijaSICAD2(Orden orden,OrdenesCruce cruce) throws Exception {
		ArrayList<String> sqlsArrayList = new ArrayList<String>();
		MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(this._dso);
		MensajeOpics mensajeOpics = null;//new MensajeOpics();
				
		for (OrdenTitulo titulo : cruce.getOrdenTitulo()) {
			mensajeOpics = new MensajeOpics();	
			mensajeOpics.setIdOrdenInfi(orden.getIdOrden());
			orden.agregarOrdenTitulo(titulo);
			orden.setTasaCambio(titulo.getTasaCambio().doubleValue());
			orden.setFechaValor(Utilitario.StringToDate(titulo.getFechaValor(), ConstantesGenerales.FORMATO_FECHA2) );
			logger.debug("Opics renta fija de compra--->" + orden.getIdOrden());
			//System.out.println("Opics renta fija de compra--->" + orden.getIdOrden());
			rentaFijaSICAD2(orden, true, mensajeOpics);
			logger.debug("Opics renta fija de venta--->" + orden.getIdOrden());
			//System.out.println("Opics renta fija de venta--->" + orden.getIdOrden());
			rentaFijaSICAD2(orden, false, mensajeOpics);
			String sqls[] = mensajeOpicsDAO.ingresar(mensajeOpics);
	
			/*
			 * Actualizamos el id_opics de la orden
			 */
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			sqlsArrayList.add(ordenDAO.actualizarCampoOpics(mensajeOpics.getIdOpics(), orden.getIdOrden()));
	
			for (int i = 0; i < sqls.length; i++) {
				sqlsArrayList.add(sqls[i]);
			}// fin for		
			
			orden.getOrdenTitulo().clear();
		}		
		return sqlsArrayList;
	}
}// fin clase IngresoOpics
