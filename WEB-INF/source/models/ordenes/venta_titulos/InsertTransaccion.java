package models.ordenes.venta_titulos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.ProcesoGestion;
import com.bdv.infi.logic.IngresoOpics;
import com.bdv.infi.logic.ProcesarDocumentos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.logic.interfaz_varias.MensajeEstadistica;

/**
 * Clase encargada de recuperar el objeto orden almacenado en sesion y hacer el llamado a la insersi&oacute;n en base de datos de la orden asociada a la venta de titulos
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class InsertTransaccion extends AbstractModel {
	
	private DataSet datosTitulo = new DataSet();
	private Orden orden = null;
	private InstruccionesPago instruccionesPago = null;
	private long usuarioId = 0;
	private boolean generarOpicsRentaFija = true;
	private boolean operacionCambio = false;
	private boolean generaronInstruccionesPago = false;	
	private OrdenOperacion operacion = null;
	private Vector<String> vec_sql_updates = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		Logger.debug(this, "---------------INICIANDO EL PROCESO DE INSERCION DE LA ORDEN DE VENTA DE TITULOS------------------");
		String idInstruccionPago=null;
		String sqlInstruccionOrdenVenta=null;
		vec_sql_updates = new Vector<String>();
		Connection conn = null;
		java.sql.Statement statement = null;

		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		ProcesoGestion procesoGestion = new ProcesoGestion();
		operacion = new OrdenOperacion();

		// Instrucción de pago
		instruccionesPago = (InstruccionesPago) _req.getSession().getAttribute("instruccionesPago");

		// arreglo de sentencias generadas en la insercion de la orden
		String[] sqlsOrden = ordenDAO.insertar(orden);

		// recuperar el objeto de procesoGestion de la sesion
		
		procesoGestion = (ProcesoGestion) _req.getSession().getAttribute("proceso_gestion");
		
		//Insercion de relacion entre la instruccion de pago y la orden de venta 
		if(_req.getSession().getAttribute("tipo_abono").equals(ConstantesGenerales.INSTRUCCION_SWIFT)){
			idInstruccionPago=_req.getSession().getAttribute("idInstruccionPago").toString();
			if(idInstruccionPago!=null){			
				sqlInstruccionOrdenVenta=ordenDAO.insertCtesCtasOrdenes(String.valueOf(orden.getIdOrden()),_req.getSession().getAttribute("idInstruccionPago").toString());
			}
		}
		// obtener el vector de actualizaciones de cantidades en custodia para los titulos a vender
		vec_sql_updates = (Vector<String>) _req.getSession().getAttribute("vec_sql_updates");

		// obtener parametro operacion cambio
		operacionCambio = (Boolean) _req.getSession().getAttribute("operacionCambio");
		
		//---- Obtener Objetos para Mensajes a Interfaces----
		//si No se genero el mensaje los objetos serán NULOS y NO SE INSERTARAN!
		//Obtener objeto para mensaje estadistico
		MensajeEstadistica mensajeEstadistica = (MensajeEstadistica)_req.getSession().getAttribute("mensaje_estadistica");
		//Obtener objeto para mensaje Carmen
		MensajeCarmen mensajeCarmen = (MensajeCarmen)_req.getSession().getAttribute("mensaje_carmen");
		//Obtener objeto para mensaje Bcv
		MensajeBcv mensajeBcv = (MensajeBcv)_req.getSession().getAttribute("mensaje_bcv");
		//------------------------------------------------------------------------------
		
		buscarOperacionVenta(orden, operacion);

		// Datos Usuario--
		long usuarioId = Long.parseLong(ordenDAO.idUserSession(getUserName()));

		// verificar si se generaron instrucciones de pago
		if (procesoGestion != null) {
			generaronInstruccionesPago = true;
		}

		// -------------INGRESO A OPICS-------------------------------------------------------------
		ingresarAOpics();
		// ------------------------------------------------------------------------------------------

//		if (generaronInstruccionesPago) {// si se crearon instrucciones de pago
//			agregarOperacionesAProcesoGestion(orden, procesoGestion);
//			// buscar moneda de negociacion del titulo vendido
//			String monedaNegTitulo = datosTitulo.getValue("titulo_moneda_neg");
//			// Guardar proceso e instrucción de pago en el vector de sentencias
//			ArrayList<String> procesoInstruccionesPago = gestionPagoDAO.insertarProceso(procesoGestion, _app, getUserName(), _req.getRemoteAddr(), monedaNegTitulo, orden.getIdOrden());
//			guardarSqlVectorActualizaciones(vec_sql_updates, procesoInstruccionesPago);
//			// ---------------------------------------------------------------------------------------
//		}

		try {
			conn = _dso.getConnection();
			conn.setAutoCommit(false);
			statement = conn.createStatement();

			Logger.debug(this, "INSERTANDO ORDEN DE VENTA DE TITULOS NUMERO " + orden.getIdOrden());
			Logger.debug(this, "CLIENTE NUMERO: " + orden.getIdCliente());
			// ---Ejecutar querys de inserción de la orden
			for (int q = 0; q < sqlsOrden.length; q++) {			
				statement.executeUpdate(sqlsOrden[q]);
			}
			// ---------------------------------------------

			
			  if(idInstruccionPago!=null){
				  Logger.debug(this, "INSERTANDO RELACION ENTRE ORDEN E INSTRUCCION DE PAGO - ORDEN DE VENTA ---> " + orden.getIdOrden() + " INSTRUCCION DE PAGO ---> " + idInstruccionPago);
				  //System.out.println("INSERTANDO RELACION ENTRE ORDEN E INSTRUCCION DE PAGO - ORDEN DE VENTA ---> " + orden.getIdOrden() + " INSTRUCCION DE PAGO ---> " + idInstruccionPago);
				  statement.executeUpdate(sqlInstruccionOrdenVenta);  
			  }
			 
			Logger.debug(this, "INSERTANDO DOCUMENTOS ASOCIADO A LA VENTA DE TITULOS...");
			// Busca el objeto orden para buscar los documentos asociados a la transacci&oacute;n de negocio
			ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(_dso);
			procesarDocumentos.procesar(orden, _app, _req.getRemoteAddr());
			Logger.debug(this, "AGREGADOS " + orden.getDocumentos().size() + " DOCUMENTO(S) DE VENTA");

			// ejecutar querys de insercion documentos de venta de titulos a la orden
			String documentosVenta[] = ordenDAO.insertarDocumentosSQL(orden);
			if (documentosVenta != null) {
				for (int u = 0; u < documentosVenta.length; u++) {					
					statement.executeUpdate(documentosVenta[u]);
				}
				// ------------------------------------------------------------------
			}

			Logger.debug(this, "INSERTANDO INSTRUCCIONES DE PAGO (SI SE GENERARON) Y GESTIONES DE PAGO...");
			Logger.debug(this, "ACTUALIZANDO EN CUSTODIA...");
			// ---Insertar instrucciones y gestiones de pago, actualizar cusodia
			for (int u = 0; u < vec_sql_updates.size(); u++) {
				statement.executeUpdate(vec_sql_updates.get(u).toString());
			}
			// ------------------------------------------------------------------
			
			//--Inserción de Mensaje Estadístico--------------------------- 
			if(mensajeEstadistica!=null){
				MensajeDAO mensajeDAO = new MensajeDAO(_dso);
				Logger.debug(this, "INSERTANDO MENSAJE ESTASISTICO...");
				//Setear Id Orden al Mensaje estadistico:
				mensajeEstadistica.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));
				//Generar senctencias sql para Guardar mensaje:
				String[] sentenciasMje = mensajeDAO.ingresar(mensajeEstadistica);
				//Ejecutar sentencias:
				for(int k=0; k<sentenciasMje.length;k++){
					statement.executeUpdate(sentenciasMje[k]);
				}
			}
			//----------------------------------------------------------
			
			//--Inserción de Mensaje Carmen---------------------------------------
			if(mensajeCarmen!=null){
				MensajeDAO mensajeDAO = new MensajeDAO(_dso);
				Logger.debug(this, "INSERTANDO MENSAJE CARMEN...");
				//Setear Id de Orden al Mensaje Carmen:
				mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));
				//Generar senctencias sql para Guardar mensaje:
				String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);
				//Ejecutar Sentencias:
				for(int k=0; k<sentenciasMje.length;k++){
					statement.executeUpdate(sentenciasMje[k]);
				}
			}
			//-----------------------------------------------------------------------
			
			//--Inserción de Mensaje BCV---------------------------------------
			if(mensajeBcv!=null){
				MensajeDAO mensajeDAO = new MensajeDAO(_dso);
				Logger.debug(this, "INSERTANDO MENSAJE BCV...");
				//Setear Id de Orden al Mensaje Carmen:
				mensajeBcv.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));
				//Generar senctencias sql para Guardar mensaje:
				String[] sentenciasMje = mensajeDAO.ingresar(mensajeBcv);
				//Ejecutar Sentencias:
				for(int k=0; k<sentenciasMje.length;k++){
					statement.executeUpdate(sentenciasMje[k]);
				}
			}
			//-----------------------------------------------------------------------


			conn.commit();
			Logger.debug(this, "GUARDADA LA ORDEN DE VENTA EXITOSAMENTE BAJO EL NRO: " + orden.getIdOrden());
			Logger.debug(this, "--------------TERMINADO EL PROCESO DE VENTA DE TITULOS-----------------");

		} catch (Exception e) {
			conn.rollback();// Deshacer actualizaciones en base de datos
			Logger.error(this, "ERROR INSERTANDO LA ORDEN DE VENTA DE TITULOS: " + e.getMessage(),e);
			_req.getSession().setAttribute("mensaje_error", "Error insertando la orden de venta de t&iacute;tulos.");
		} catch (Throwable t) {
			conn.rollback();// Deshacer actualizaciones en base de datos
			Logger.error(this, "ERROR INSERTANDO LA ORDEN DE VENTA DE TITULOS: " + t.getMessage(),t);
			_req.getSession().setAttribute("mensaje_error", "Error insertando la orden de venta de t&iacute;tulos.");

		} finally {
			if (statement != null)
				statement.close();

			if (conn != null)
				conn.close();
		}

		// Se almacena en sessi&oacute;n para que pueda ser recuperado para la impresi&oacute;n
		_req.getSession().setAttribute("OrdenDocumentos", orden);

		_req.getSession().removeAttribute("orden_venta_titulos");
		_req.getSession().removeAttribute("vec_sql_updates");
		_req.getSession().removeAttribute("operacionCambio");
		_req.getSession().removeAttribute("proceso_gestion");
		_req.getSession().removeAttribute("mensaje_estadistica");
		_req.getSession().removeAttribute("mensaje_carmen");
		_req.getSession().removeAttribute("mensaje_bcv");		

	}
	
	

	@Override
	public boolean isValid() throws Exception {
		boolean correcto = true;
		boolean uipublicada = false;
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso); 
		correcto = super.isValid();
		if (correcto){
			// obtener el objeto orden creado y guardado en sesion
			orden = (Orden) _req.getSession().getAttribute("orden_venta_titulos");
			buscarDatosTituloVenta(orden);
			
			if (orden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				inversionDAO.listarUIPorTituloId(datosTitulo.getValue("titulo_id"));
				//Si la negociación no es de CARP (Cartera Propia)
				if (!_req.getSession().getAttribute("forma_negociacion").equals("CARP")){
					while (inversionDAO.getDataSet().next()){
						if (inversionDAO.getDataSet().getValue("undinv_status").equals(UnidadInversionConstantes.UISTATUS_PUBLICADA)){
							uipublicada = true;
							break;
						}
					}
					if (!uipublicada){
						_record.addError("Error", "No es posible realizar la venta, el título actualmente no se encuentra abierto para negociación");
						correcto = false;
					}					
				}
			}
		}
		return correcto;
	}


	/**
	 * Devuelve la opercion de venta de titulos que se llevo a cabo, para obtener sus datos
	 * 
	 * @param orden
	 * @param operacion
	 */
	private void buscarOperacionVenta(Orden orden, OrdenOperacion operacion) {
		ArrayList<OrdenOperacion> listaOper = orden.getOperacion();
		for (int i = 0; i < listaOper.size(); i++) {
			operacion = (OrdenOperacion) listaOper.get(i);
		}
	}

	/**
	 * Busca la moneda de denominaci&oacute;n del titulo vendido
	 * 
	 * @param orden
	 * @return moneda de denominaci&oacute;n
	 * @throws Exception
	 */
	private void buscarDatosTituloVenta(Orden orden) throws Exception {
		ArrayList listaTitulo = orden.getOrdenTitulo();

		for (int k = 0; k < listaTitulo.size(); k++) {
			OrdenTitulo ordenTitulo = (OrdenTitulo) listaTitulo.get(k);
			TitulosDAO titulosDAO = new TitulosDAO(_dso);
			titulosDAO.listarTitulos(ordenTitulo.getTituloId());
			datosTitulo = titulosDAO.getDataSet();
			if (datosTitulo.count() > 0) {
				datosTitulo.next();
			}
		}

	}

	/**
	 * Agrega los id de las operaciones asociadas a la orden de venta de t&iacute;tulos al objeto proceso gestion
	 * 
	 * @param orden
	 * @param procesoGestion
	 */
	private void agregarOperacionesAProcesoGestion(Orden orden, ProcesoGestion procesoGestion) {
		ArrayList<OrdenOperacion> operacionesOrden = new ArrayList<OrdenOperacion>();
		operacionesOrden = orden.getOperacion();
		Logger.debug(this, "AGREGANDO OPERACIONES AL PROCESO DE GESTION..");
		for (int k = 0; k < operacionesOrden.size(); k++) {
			OrdenOperacion ordenOperacion = (OrdenOperacion) operacionesOrden.get(k);
			procesoGestion.agregarOperacion(ordenOperacion.getIdOperacion());
		}

	}

	/**
	 * Guarda las sentencias sql en el vector de actualizaciones para la venta los t&iacute;tulos
	 * 
	 * @param vec_sql_updates
	 * @param arraySql
	 */
	private void guardarSqlVectorActualizaciones(Vector<String> vec_sql_updates, ArrayList<String> arraySql) {

		if (!arraySql.isEmpty()) {

			for (int k = 0; k < arraySql.size(); k++) {				
				String sql = (String) arraySql.get(k);
				vec_sql_updates.add(sql);
			}
		}

	}
	
	/**
	 * Prepara los mensajes necesarios hacia OPICS
	 * @throws Exception en caso de error
	 */
	private void ingresarAOpics() throws Exception{
		IngresoOpics ingresoOpics = new IngresoOpics(_dso, _app, usuarioId, _req.getRemoteAddr(), getUserName());
		ArrayList<String> sqlOpics = new ArrayList<String>();
		ArrayList<String> sqlOpicsOC = new ArrayList<String>();
		// si es una operacion de cambio y se genero instruccion de pago (usuario NO especial)
		if (operacionCambio && generaronInstruccionesPago) {
			sqlOpicsOC = ingresoOpics.operacionCambio(orden, getUserName(), operacion.getNumeroCuenta(), datosTitulo.getValue("titulo_moneda_neg"), instruccionesPago.getMonedaId(), instruccionesPago.getMontoInstruccionNoConversion(), new BigDecimal(orden.getTasaCambio()));
			this.guardarSqlVectorActualizaciones(vec_sql_updates, sqlOpicsOC);
		}
		
		//Verifica el tipo de producto SITME
		if (orden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
			if (!_req.getSession().getAttribute("forma_negociacion").equals("CARP")){
				generarOpicsRentaFija = false;				
			}
		}
		if (generarOpicsRentaFija){
			// generar siempre deal de renta fija
			sqlOpics = ingresoOpics.rentaFija(orden,TransaccionNegocio.VENTA_TITULOS);
			this.guardarSqlVectorActualizaciones(vec_sql_updates, sqlOpics);
		}
	}
}
