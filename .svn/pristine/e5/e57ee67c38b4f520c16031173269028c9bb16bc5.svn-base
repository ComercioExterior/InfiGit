package models.bcv.menudeo;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import javax.sql.DataSource;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import org.apache.axis.AxisFault;
import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.service.AutorizacionPortBindingStub;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

public class EnvioBCVWSMenudeo extends AbstractModel implements Runnable {
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	OrdenDAO ordenDAO = null;
	SolicitudesSitmeDAO sitmeDAO = null;
	CredencialesDAO credencialesDAO = null;
	DataSet _credenciales = new DataSet();
	DataSet _ordenes = new DataSet();
	DataSet _cliente = new DataSet();
	ClienteDAO clienteDAO = null;
	long idUnidad;
	int numeroDePagina;
	int pageSize;
	boolean todos;
	boolean incluir; 
	String idOrdenes;
	String statusP;
	String urlInvocacion;
	DataSource _dso;
	String tipoTransaccion = TransaccionNegocio.WS_BCV_MENUDEO;
	int idUsuario;
	String fecha;
	Integer tasaMinima;
	Integer tasaMaxima;
	Integer montoMinimo;
	Integer montoMaximo;
	Integer clienteID;
	Integer incluirCliente;
	OrdenesCrucesDAO ordenesCrucesDAO;
	
	
	public EnvioBCVWSMenudeo (Integer incluirCliente, Integer clienteID,Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo, long idUnidad, int numeroDePagina, int pageSize, boolean todos, boolean incluir, String idOrdenes, String statusP, String urlInvocacion, DataSource _dso, int idUsuario, String fecha){
		this.idUnidad = idUnidad;
		this.numeroDePagina = numeroDePagina;
		this.pageSize = pageSize;
		this.todos = todos;
		this.incluir = incluir;
		this.idOrdenes = idOrdenes;
		this.statusP = statusP;
		this.urlInvocacion = urlInvocacion;
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.credencialesDAO = new CredencialesDAO(_dso);
		this.tasaMinima = tasaMinima;
		this.tasaMaxima = tasaMaxima;
		this.montoMinimo = montoMinimo;
		this.montoMaximo = montoMaximo;
		this.clienteID   = clienteID;
		this.incluirCliente = incluirCliente;
	}

	//NM26659_07/09/2015 Sobrecarga de constructor para inclusion de tipo transaccion  
	public EnvioBCVWSMenudeo (Integer incluirCliente, Integer clienteID,Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo, long idUnidad, int numeroDePagina, int pageSize, boolean todos, boolean incluir, String idOrdenes, String statusP, String urlInvocacion, DataSource _dso, int idUsuario, String fecha,String tipoTransaccion){
		this.idUnidad = idUnidad;
		this.numeroDePagina = numeroDePagina;
		this.pageSize = pageSize;
		this.todos = todos;
		this.incluir = incluir;
		this.idOrdenes = idOrdenes;
		this.statusP = statusP;
		this.urlInvocacion = urlInvocacion;
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.credencialesDAO = new CredencialesDAO(_dso);
		this.tasaMinima = tasaMinima;
		this.tasaMaxima = tasaMaxima;
		this.montoMinimo = montoMinimo;
		this.montoMaximo = montoMaximo;
		this.clienteID   = clienteID;
		this.incluirCliente = incluirCliente;
		this.tipoTransaccion=tipoTransaccion;
	}
	
	public void run() {
		//INCIAR PROCESO
		try {
			iniciarProceso();
			enviarOperacionesBCV();
			finalizarProceso();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void iniciarProceso() throws Exception {
		Logger.info(this,"INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(tipoTransaccion); 
		proceso.setUsuarioId(this.idUsuario); 

		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}
	
	private void finalizarProceso() throws Exception {
		String queryProcesoCerrar = procesosDAO.modificar(proceso);
		db.exec(_dso, queryProcesoCerrar);
		Logger.info(this,"FIN DE PROCESO: " + new Date());
	}
	
	private void enviarOperacionesBCV (){
		try {
			Logger.info(this, "SE INICIA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_MENUDEO);
			_credenciales = credencialesDAO.getDataSet();
			Propiedades propiedades =  Propiedades.cargar();
			String userName = "";
			String clave    = "";
			String tipoNegocio ="";
			
			//NM26659 - 28/08/2015 Inclusion de validacion para el caso de Envio a BCV por venta en Taquilla Aereopuerto
			//NM25287 - 18/09/2015 Inclusion de URL para anulación taquilla
			if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_TAQUILLA_AEREOPUERTO_PROCESAR.getNombreAccion())||urlInvocacion.equals(ActionINFI.WEB_SERVICE_TAQUILLA_AEREOPUERTO_ANULAR_PROCESAR.getNombreAccion())) {
				tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR_TAQUILLA;	
			}else {
				tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR;
			}
			
			if(_credenciales.next()){
				//se carga el certificado autofirmado del BDV y se configura el proxy
				//Utilitario.cargarCertificado(propiedades.getProperty(ConstantesGenerales.RUTA_CER_MENUDEO_BCV));
				//System.setProperty("sun.security.ssl.allowUnsafeRenegotiation","true");
				//SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
				if(propiedades.getProperty("use_https_proxy").equals("1")){
					Utilitario.configurarProxy();
				}
				
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

				TripleDes desc = new TripleDes();
				
				userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
			}else {
				Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: "+ tipoTransaccion);
				throw new org.bcv.service.Exception();
			}
			
			
			AutorizacionPortBindingStub stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_MENUDEO)), null);
			Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null) {
				headers = new Hashtable();
				stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
			}
			headers.put("Username", userName);
			headers.put("Password", clave);
			
			//SE DECLARAN VARIABLES PARA EL BUCLE
			String ordeneID;
			String cedRif;
			String tipperID;
			String codigoCliente;
			String nombreCliente;
			String telefono;
			String email;
			BigDecimal montoBase=new BigDecimal(0);
			BigDecimal tasaCambio;  
			BigDecimal montoTransaccion=new BigDecimal(0);
			String ctaConvenio20;
			Long   tipoOperacion;
			String codMonedaIso;
			String tipoMovimiento = "";
			String ordenBCV;
			String estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
			String cruceProcesado = "0";
			
			if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_DEMAN_PROCESAR.getNombreAccion())){ //DEMANDA - VENTA
				ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
				ordenDAO         = new OrdenDAO(_dso);
				ordenesCrucesDAO.listarOrdenesPorEnviarBCVMenudeo(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,idUnidad, false, numeroDePagina,pageSize, todos, incluir, idOrdenes,statusP, fecha, estatusOrdenINFI);
				_ordenes = ordenesCrucesDAO.getDataSet();

				while (_ordenes.next()) {
					cedRif   =  _ordenes.getValue("CLIENT_CEDRIF");
					cedRif   =  Utilitario.completarCaracterIzquierda(cedRif, 8, "0"); 
					ordeneID =  _ordenes.getValue("ORDENE_ID");
					tipperID =  _ordenes.getValue("TIPPER_ID");
					codigoCliente = _ordenes.getValue("TIPPER_ID") + cedRif; 
					nombreCliente = _ordenes.getValue("client_nombre");
					telefono          = _ordenes.getValue("client_telefono");
					email             = _ordenes.getValue("client_correo_electronico");
					montoBase     = new BigDecimal(_ordenes.getValue("ORDENE_ADJ_MONTO"));
					tasaCambio    = new BigDecimal(_ordenes.getValue("ordene_tasa_pool"));  
					montoTransaccion = new BigDecimal(( _ordenes.getValue("ORDENE_ADJ_MONTO")));
					ctaConvenio20     = _ordenes.getValue("cta_numero");
					codMonedaIso     =  ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
					tipoOperacion     = Long.parseLong(ConstantesGenerales.TIPO_DE_OPERACION);
					ordenBCV = "";
					
					if(tipperID.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PN)){
						tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_PN_VEN;
					}else if (tipperID.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PE) ){
						tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EX_VEN;
					}
					

					if(email != null && email.contains("@") ){
						//NM26659 17/03/2015 RESOLUCION INCIDENCIA CALIDAD						
						email=email.toLowerCase().trim();
					}else { //EMAIL ES NULL
						email = "sinemail@mail.com";
					}
					
					String telefonoDepurado;
					if(telefono == null){
						telefonoDepurado = "05006425283";
					}else {
						telefonoDepurado = Utilitario.depurarString(telefono.trim());
					}

					try {
						ordenBCV = stub.VENTADIV(tipoMovimiento, 
													   codigoCliente,
								                       nombreCliente , 
								                       montoBase, 
								                       tasaCambio, 
								                       codMonedaIso, 
								                       montoTransaccion, 
								                       tipoOperacion, 
								                       ctaConvenio20,
								                       telefonoDepurado,
								                       email);
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: "+ordeneID+" - "
								+e.toString()+" "+Utilitario.stackTraceException(e));
						e.printStackTrace();
						
						Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
						Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
						Logger.error(this, "codigoCliente: "+codigoCliente);
						Logger.error(this, "nombreCliente: "+nombreCliente);
						Logger.error(this, "montoBase: "+montoBase);
						Logger.error(this, "tasaCambio: "+tasaCambio);
						Logger.error(this, "codMonedaIso: "+codMonedaIso);
						Logger.error(this, "montoTransaccion: "+montoTransaccion);
						Logger.error(this, "tipoOperacion: "+tipoOperacion);
						Logger.error(this, "ctaConvenio20: "+ctaConvenio20);
						Logger.error(this, "telefono: "+telefonoDepurado);
						Logger.error(this, "email: "+email);
						
						boolean errorControlado = false;
						//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
						for (ErroresMenudeo tmp: ErroresMenudeo.values() ) {
							if(e.toString().contains(tmp.getCodigoError())){
								errorControlado = true;
								break;
							}
				        }
						if(errorControlado){
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA RECHAZADA
							ordenDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString());
							ordenesCrucesDAO.actualizarOrdenBCVMenudeo(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString(), ConstantesGenerales.STATUS_INVALIDA);
						}else {//SE GENERA UN ERROR NO CONTROLADO
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
							ordenDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString());
							ordenesCrucesDAO.actualizarOrdenBCVMenudeo(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString(), null);
						}
						
						continue; //NO QUIERO QUE ACTUALICE DE NUEVO
					}
					
					try {
						//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
						ordenDAO.actualizarOrdenBCV(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito");
						ordenesCrucesDAO.actualizarOrdenBCVMenudeo(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito", null);
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
						e.printStackTrace();
						continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
					}
				}
			}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MENUDEO_OFER_PROCESAR.getNombreAccion())) { //OFERTA - COMPRA
				ClienteDAO clienteDAO = new ClienteDAO(_dso);
				String cedRifCliente;
				String tipperId;
				Long   cedulaCliente;
				String tipper_id;
				String codigoCliente2 = "0";

				if(clienteID != 0){
					clienteDAO.listarPorId(clienteID.toString());
					_cliente = clienteDAO.getDataSet();
					
					if(_cliente.count() > 0){
						tipperId =  _cliente.getValue("TIPPER_ID");
						cedRifCliente = _cliente.getValue("CLIENT_CEDRIF");
						cedRifCliente = Utilitario.completarCaracterIzquierda(cedRifCliente, 13, "0");
						codigoCliente2 = tipperId + cedRifCliente;
						//se agrega digito verificador enviado por clavenet
						codigoCliente2 = Utilitario.completarCaracterDerecha(codigoCliente2, 15, "0");
					}else {
						Logger.error(this, "No se han conseguido los datos del cliente para el filtro en la tabla INFI_TB_201_CTES con el ID "+clienteID+".");
					}
				}
				
				sitmeDAO = new  SolicitudesSitmeDAO(_dso);
				sitmeDAO.listarOrdenesPorEnviarBCV(incluirCliente,codigoCliente2,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,idUnidad, false, numeroDePagina,pageSize, todos, incluir, idOrdenes,statusP, fecha);
				_ordenes = sitmeDAO.getDataSet();
				clienteDAO = new ClienteDAO(_dso);
				
				while (_ordenes.next()) {
					//TIPO DE CLIENTE Y FECHA QUE VIENE DE LA TABLA SOLICITUDES_SITME
					cedRifCliente  = _ordenes.getValue("CED_RIF_CLIENTE");		
					tipper_id      = cedRifCliente.substring(0,1);
					//SE ELIMINA EL ULTIMO CARACTER DE LA CADENA (DIGITO VERIFICADOR EN 0 ENVIADO POR CLAVE NET)
					cedRifCliente = cedRifCliente.substring(0, cedRifCliente.length()-1); 
					cedulaCliente  = Long.parseLong(cedRifCliente.substring(1,cedRifCliente.length()));
					clienteDAO.listar(0, tipper_id, cedulaCliente, null, 0, null);
					_cliente = clienteDAO.getDataSet();
					
					cedRifCliente = Utilitario.completarCaracterIzquierda(cedulaCliente.toString(), 8, "0"); 
					codigoCliente = tipper_id + cedRifCliente;
					
					if(_cliente.next()){
						telefono = _cliente.getValue("client_telefono");
						email    = _cliente.getValue("client_correo_electronico");
						
						if(email != null && email.contains("@")){
							//NM26659 17/03/2015 RESOLUCION INCIDENCIA CALIDAD
							email=email.toLowerCase().trim();							
						}else { //EMAIL ES NULL
							email = "sinemail@mail.com";
						}
					}else {
						Logger.error(this, "No se han conseguido los datos del cliente "+cedRifCliente+". No sera enviado al BCV");
						continue; 
					}

					ordeneID = _ordenes.getValue("ID_ORDEN");
					nombreCliente = _ordenes.getValue("NOMBRE_CLIENTE");
					montoBase     = new BigDecimal(_ordenes.getValue("MONTO_SOLICITADO"));
					tasaCambio    = new BigDecimal(_ordenes.getValue("TASA_CAMBIO"));  
					montoTransaccion  = new BigDecimal(_ordenes.getValue("MONTO_SOLICITADO"));
					ctaConvenio20     = _ordenes.getValue("CTA_NUMERO");
					tipoOperacion     = Long.parseLong(ConstantesGenerales.TIPO_DE_OPERACION);
					codMonedaIso     =  ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
					ordenBCV = "";
					
					if(tipper_id.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PN)){
						tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_PN_COM;
					}else if (tipper_id.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PE) ){
						tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EX_COM;
					}
					
					String telefonoDepurado;
					if(telefono == null){
						telefonoDepurado = "05006425283";
					}else {
						telefonoDepurado = Utilitario.depurarString(telefono.trim());
					}
					
					try {
						ordenBCV = stub.COMPRADIV(tipoMovimiento, 
												       codigoCliente,
								                       nombreCliente , 
								                       montoBase, 
								                       tasaCambio, 
								                       codMonedaIso, 
								                       montoTransaccion, 
								                       tipoOperacion, 
								                       ctaConvenio20,
								                       telefonoDepurado,
								                       email,
								                       "1051");	
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV id_orden (TABLA SITME) "+ordeneID+" - "
								+e.toString()+" "+Utilitario.stackTraceException(e));
						
						Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
						Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
						Logger.error(this, "codigoCliente: "+codigoCliente);
						Logger.error(this, "nombreCliente: "+nombreCliente);
						Logger.error(this, "montoBase: "+montoBase);
						Logger.error(this, "tasaCambio: "+tasaCambio);
						Logger.error(this, "codMonedaIso: "+codMonedaIso);
						Logger.error(this, "montoTransaccion: "+montoTransaccion);
						Logger.error(this, "tipoOperacion: "+tipoOperacion);
						Logger.error(this, "ctaConvenio20: "+ctaConvenio20);
						Logger.error(this, "telefono: "+telefonoDepurado);
						Logger.error(this, "email: "+email);
						
						boolean errorControlado = false;
						//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
						for (ErroresMenudeo tmp: ErroresMenudeo.values() ) {
							if(e.toString().contains(tmp.getCodigoError())){
								errorControlado = true;
								break;
							}
				        }
						if(errorControlado){
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA RECHAZADA
							sitmeDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString());
						}else {//SE GENERA UN ERROR NO CONTROLADO
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
							sitmeDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString());
						}

						continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
					}
					
					try {
						//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
						sitmeDAO.actualizarOrdenBCV(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito");
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
						e.printStackTrace();
						continue; //NO QUIERO QUE ACTUALICE LA ORDEN SI OCURRE ALGUN ERROR
					}
				}

			}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_TAQUILLA_AEREOPUERTO_PROCESAR.getNombreAccion())){//NM26659 27/08/2015 Inclusion de envio de notificaciones a BCV por concepto de Venta de Efectivo y Cta en dolares en taquilla de aereopuerto
				String operacionTipo="";
				
				
				ordenDAO         = new OrdenDAO(_dso);
				ordenDAO.listarOrdenesPorEnviarBCVPorVentaTaquilla(tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,idUnidad, false, numeroDePagina,pageSize, todos, incluir, idOrdenes,statusP, fecha, estatusOrdenINFI,false); //NM25287 TTS-504 Modificación incluir parámetro que indique si es anulación o notificación de operaciones de taquilla
				_ordenes = ordenDAO.getDataSet();
				if(_ordenes.count()>0) {
					_ordenes.first();
				while (_ordenes.next()) {	
					
					boolean operacionElectronico=false;
					boolean operacionEfectivo=false;	
					//Double montoEfectivo=new Double(_ordenes.getValue("monto_efectivo"));
					//Double montoCtaDolares=new Double(_ordenes.getValue("monto_cta_dolares"));
					
				//	if(montoEfectivo!=0 && montoCtaDolares!=0){
				//		envioDoble=true;
				//	}
					
					operacionTipo = _ordenes.getValue("tipo_operacion");
					cedRif   =  _ordenes.getValue("CLIENT_CEDRIF");
					cedRif   =  Utilitario.completarCaracterIzquierda(cedRif, 8, "0"); 
					ordeneID =  _ordenes.getValue("ORDENE_ID");
					tipperID =  _ordenes.getValue("TIPPER_ID");
					codigoCliente = _ordenes.getValue("TIPPER_ID") + cedRif; 
					nombreCliente = _ordenes.getValue("client_nombre");
					telefono          = _ordenes.getValue("client_telefono");
					email             = _ordenes.getValue("client_correo_electronico");
					montoBase     = new BigDecimal(_ordenes.getValue("monto_adj"));
					//tasaCambio    = new BigDecimal(_ordenes.getValue("ordene_tasa_pool"));  
					tasaCambio    = new BigDecimal(_ordenes.getValue("tasa_cambio"));
					montoTransaccion = new BigDecimal(( _ordenes.getValue("monto_adj")));
					ctaConvenio20     = _ordenes.getValue("cta_numero");					
					codMonedaIso     =  ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
					tipoOperacion     = Long.parseLong(ConstantesGenerales.TIPO_DE_OPERACION);
					ordenBCV = "";					
					
					if(email != null && email.contains("@") ){
						//NM26659 17/03/2015 RESOLUCION INCIDENCIA CALIDAD						
						email=email.toLowerCase().trim();
					}else { //EMAIL ES NULL
						email = "sinemail@mail.com";
					}
					
					String telefonoDepurado;
					if(telefono == null){
						telefonoDepurado = "05006425283";
					}else {
						telefonoDepurado = Utilitario.depurarString(telefono.trim());
					}

					try {
												
						if(operacionTipo.equals(ConstantesGenerales.TIPO_OPERACION_ELECTRONICO)){//Si la operacion tiene monto para Cta en Dolares
							operacionElectronico=true;
							if(tipperID.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PN)){//Verificacion del tipo de persona Venezolano para denominacion de tipo de operacion BTETRV por ELECTRONICO (Cta en Dolares)										
								tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_PN_VEN_TAQ_ELE;									
							}else if (tipperID.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PE) ){//Verificacion del tipo de persona Extranjera para denominacion de tipo de operacion BTVTRV por ELECTRONICO (Cta en Dolares)										
								tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EX_VEN_TAQ_ELE;															
							}							
						} else if(operacionTipo.equals(ConstantesGenerales.TIPO_OPERACION_EFECTIVO)){//Si la operacion tiene monto para Efectivo
							operacionEfectivo=true;
							if(tipperID.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PN)){//Verificacion del tipo de persona Venezolano para denominacion de tipo de operacion BTVEFV por EFECTIVO									
								tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_PN_VEN_TAQ_EFE;								
							}else if (tipperID.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PE) ){//Verificacion del tipo de persona Extranjera para denominacion de tipo de operacion BTEEFV por EFECTIVO									
								tipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EX_VEN_TAQ_EFE;							
							
							}						
						}
							
							ordenBCV = stub.VENTADIV(tipoMovimiento, 
														   codigoCliente,
									                       nombreCliente , 
									                       montoBase, 
									                       tasaCambio, 
									                       codMonedaIso, 
									                       montoTransaccion, 
									                       tipoOperacion, 
									                       ctaConvenio20,
									                       telefonoDepurado,
									                       email);
							
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: "+ordeneID+" - "
								+e.toString()+" "+Utilitario.stackTraceException(e));
						e.printStackTrace();
						
						Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
						Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
						Logger.error(this, "codigoCliente: "+codigoCliente);
						Logger.error(this, "nombreCliente: "+nombreCliente);
						Logger.error(this, "montoBase: "+montoBase);
						Logger.error(this, "tasaCambio: "+tasaCambio);
						Logger.error(this, "codMonedaIso: "+codMonedaIso);
						Logger.error(this, "montoTransaccion: "+montoTransaccion);
						Logger.error(this, "tipoOperacion: "+tipoOperacion);
						Logger.error(this, "ctaConvenio20: "+ctaConvenio20);
						Logger.error(this, "telefono: "+telefonoDepurado);
						Logger.error(this, "email: "+email);
						
						boolean errorControlado = false;
						//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
						for (ErroresMenudeo tmp: ErroresMenudeo.values() ) {
							if(e.toString().contains(tmp.getCodigoError())){
								errorControlado = true;
								break;
							}
				        }
						if(errorControlado){
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA RECHAZADA
							if(operacionElectronico){
								ordenDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString());	
							} else if (operacionEfectivo) {
								ordenDAO.actualizarEstatusTaquilla(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString());
							}
							
							//ordenesCrucesDAO.actualizarOrdenBCVMenudeo(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString(), ConstantesGenerales.STATUS_INVALIDA);
						} else {//SE GENERA UN ERROR NO CONTROLADO
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
							if(operacionElectronico){
								ordenDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString());
							} else if (operacionEfectivo) {
								ordenDAO.actualizarEstatusTaquilla(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString());
							}
							//ordenesCrucesDAO.actualizarOrdenBCVMenudeo(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString(), null);
						}
						
						continue; //NO QUIERO QUE ACTUALICE DE NUEVO
					}
					
					try {
						//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
						//ordenDAO.actualizarOrdenBCV(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito");
						if(operacionElectronico){
							ordenDAO.actualizarOrdenBCV(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito");
						} else if (operacionEfectivo) {
							ordenDAO.actualizarEstatusTaquilla(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito");
						}
						//ordenesCrucesDAO.actualizarOrdenBCVMenudeo(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito", null);
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
						e.printStackTrace();
						continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
					}
				}
			
			}
			}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_TAQUILLA_AEREOPUERTO_ANULAR_PROCESAR.getNombreAccion())){
				System.out.println("HOLAAA---WEB_SERVICE_TAQUILLA_AEREOPUERTO_ANULAR_PROCESAR");
				String idAnulacionBCV="";
				String motivoAnulacion = "Anulacion a solicitud del cliente";				
				ordenDAO         = new OrdenDAO(_dso);
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_ANU_TAQ_EFE;
				
				ordenDAO.listarOrdenesPorEnviarBCVPorVentaTaquilla(tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,idUnidad, false, numeroDePagina,pageSize, todos, incluir, idOrdenes,statusP, fecha, estatusOrdenINFI,true);
				
				_ordenes = ordenDAO.getDataSet();
				if(_ordenes.count()>0) {
					_ordenes.first();
					while (_ordenes.next()) {
						ordeneID=_ordenes.getValue("ORDENE_ID");
						ordenBCV=_ordenes.getValue("ORDENE_ID_BCV");
						try {							
							idAnulacionBCV=stub.ANULAR(ordenBCV,tipoMovimiento,motivoAnulacion);							
							
						} catch (Exception e) {

							Logger.error(this, "Ha ocurrido un error al momento de ANULAR la orden al BCV ORDENE_ID_BCV: "+ordenBCV+" - "
									+e.toString()+" "+Utilitario.stackTraceException(e));
							e.printStackTrace();
							
							Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV DE ANULACION");
							Logger.error(this, "ordenBCV: "+ordenBCV);
							Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
							Logger.error(this, "motivoAnulacion: "+motivoAnulacion);
							
							boolean errorControlado = false;
							//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
							for (ErroresMenudeo tmp: ErroresMenudeo.values() ) {
								if(e.toString().contains(tmp.getCodigoError())){
									errorControlado = true;
									break;
								}
					        }
							
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
							ordenDAO.actualizarEstatusTaquilla(ordeneID, null, null,ConstantesGenerales.STATUS_TAQ_RESERVADA_X_TF,errorControlado?"Error Controlado: ":"Error: "+e.toString());
							
							continue; //NO QUIERO QUE ACTUALICE DE NUEVO
						
						}
						
						try {
							//SE ACTUALIZA LA ORDEN ANULADA						
							ordenDAO.actualizarEstatusTaquilla(ordeneID, null, ConstantesGenerales.ANULADA_BCV, "Anulación BCV ejecutado con exito bajo el número: "+idAnulacionBCV);
							
						} catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
							e.printStackTrace();
							continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
						}
						
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
		} finally {
			Logger.info(this, "FINALIZA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
		}
	}
	
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
