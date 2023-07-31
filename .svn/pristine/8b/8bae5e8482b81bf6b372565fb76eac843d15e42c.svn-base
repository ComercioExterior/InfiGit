package models.bcv.alto_valor;
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
import models.bcv.menudeo.ErroresMenudeo;

import org.apache.axis.AxisFault;
import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.serviceAltoValor.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OfertaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

public class EnvioBCVWSAltoValor extends AbstractModel implements Runnable {
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	OrdenDAO ordenDAO = null;
	OfertaDAO ofertaDAO = null;
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
	String tipoTransaccion;
	int idUsuario;
	String fecha;
	Integer tasaMinima;
	Integer tasaMaxima;
	Integer montoMinimo;
	Integer montoMaximo;
	Integer clienteID;
	Integer incluirCliente;
	Integer tipoOperacion;
	String  origen;
	String estatusCruce;
	Integer bloterID;
	
	public EnvioBCVWSAltoValor (Integer tipoOperacion, Integer incluirCliente, Integer clienteID, Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo, long idUnidad, int numeroDePagina, int pageSize, boolean todos, boolean incluir, String idOrdenes, String statusP, String urlInvocacion, DataSource _dso, int idUsuario, String fecha, String origen, String estatusCruce, Integer bloterID){
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
		this.origen = origen;
		this.estatusCruce = estatusCruce;
		this.tipoOperacion = tipoOperacion;
		this.bloterID = bloterID;
		
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_DEMAN_PROCESAR.getNombreAccion())){//DEMANDA
			tipoTransaccion = TransaccionNegocio.WS_BCV_ALTO_VALOR_DEMAN;
		}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_OFER_PROCESAR.getNombreAccion())) { //OFERTA - COMPRA ALTO VALOR
			tipoTransaccion = TransaccionNegocio.WS_BCV_ALTO_VALOR_OFER;
		}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ANULAR_OFER_DEMAN_PROCESAR.getNombreAccion())) { //OFERTA - DEMANDA ANULAR
			tipoTransaccion = TransaccionNegocio.WS_BCV_ANULAR_OFER_DEMAN; 
		}
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
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
			_credenciales = credencialesDAO.getDataSet();
			Propiedades propiedades =  Propiedades.cargar();
			String userName = "";
			String clave    = "";
			String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
		 	String tipoProductoId = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
			
			if(_credenciales.next()){
				//se carga el certificado autofirmado del BDV y se configura el proxy
				//Utilitario.cargarCertificado(propiedades.getProperty(ConstantesGenerales.RUTA_CER_ALTO_VALOR_BCV));
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
				Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: "+ConstantesGenerales.WS_BCV_MENUDEO);
				throw new org.bcv.service.Exception();
			}
			
			BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
			
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
			BigDecimal montoBase;
			BigDecimal tasaCambio;  
			String montoTransaccion;
			String ctaConvenio20;
			//Long   tipoOperacion;
			String codMonedaIso;
			String ordenBCV;
			//String jornadaActiva = "";
			String jornadaActivaUI = "";
			String ordeneIDBCV = "";
			Integer ofertaAnulacion  = Integer.parseInt(ConstantesGenerales.OFERTA_ANULACION);
			Integer demandaAnulacion = Integer.parseInt(ConstantesGenerales.DEMANDA_ANULACION);

			if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_DEMAN_PROCESAR.getNombreAccion()) || tipoOperacion== demandaAnulacion){ //DEMANDA - VENTA ALTO VALOR
				ordenDAO = new OrdenDAO(_dso);
				ordenDAO.listarOrdenesPorEnviarBCV(tipoProductoId,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,tipoNegocio,idUnidad, false, numeroDePagina,pageSize, todos, incluir, idOrdenes,statusP, fecha, bloterID);
				_ordenes = ordenDAO.getDataSet();
				
				while (_ordenes.next()) {
					tipperID =  _ordenes.getValue("TIPPER_ID");
					cedRif   =  _ordenes.getValue("CLIENT_CEDRIF");
					
					if(tipperID.equals("J") || tipperID.equals("G")){ //SI ES J O G SE COMPLETA CON 9 CARACTERES PARA UN TOTAL DE 10
						//11/04/2016 NM26659 Resolucion incidencia ITS-3069						
						cedRif=Utilitario.completarCaracterIzquierda(cedRif, 8, "0");						
						cedRif=Utilitario.digitoVerificador(tipperID.concat(cedRif),false);						
					}else {
						cedRif    =  Utilitario.completarCaracterIzquierda(cedRif, 8, "0");
						cedRif    = _ordenes.getValue("TIPPER_ID")+ cedRif; 
					}
					
					ordeneID =  _ordenes.getValue("ORDENE_ID");
					// 11/04/2016 NM26659 Resolucion incidencia ITS-3069 
					//codigoCliente = _ordenes.getValue("TIPPER_ID") + cedRif;
					codigoCliente = cedRif;					
					System.out.println(" Envio Operacion SIMADI Alto Valor Demanda Cliente: ---> " + codigoCliente);
					nombreCliente = _ordenes.getValue("client_nombre");
					montoBase     = new BigDecimal(_ordenes.getValue("ordene_ped_monto"));
					tasaCambio    = new BigDecimal(_ordenes.getValue("ordene_tasa_pool"));  
					montoTransaccion  = _ordenes.getValue("ordene_ped_monto");
					ctaConvenio20     = _ordenes.getValue("cta_numero");
					codMonedaIso     =  ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
					//tipoOperacion     = Long.parseLong(ConstantesGenerales.TIPO_DE_OPERACION);
					jornadaActivaUI   = _ordenes.getValue("nro_jornada");
					ordeneIDBCV       = _ordenes.getValue("ordene_id_bcv"); 
					
					if(jornadaActivaUI == null || jornadaActivaUI == ""){
						//jornadaActivaUI = jornadaActiva;
						// TODO Auto-generated catch block
						Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV. La unidad de inversion no tiene el codigo de la jornada. ");
						proceso.agregarDescripcionError("La unidad de inversion no cuenta con el codigo de la jornada. Actualizar en el modulo: 'Configurar Jornada Unidad Inversion'");
						try {
							procesosDAO.modificar(proceso);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
						}
						throw new Exception("Jornada no esta configurada en la UI");
					}
					
					ordenBCV = "";
					
					if(tipoOperacion == demandaAnulacion){ //SE VA A REALIZAR UNA ANULACION
						try { 
							ordenBCV = stub.ANULADEMANDA(jornadaActivaUI, ordeneIDBCV);
						} catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de anular la orden al BCV ORDENE_ID: "+ordeneID+" - ORDENE_ID_BCV: "+ordeneIDBCV
									+e.toString()+" "+Utilitario.stackTraceException(e));
							e.printStackTrace();
							Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
							Logger.error(this, "numero jornada: "+jornadaActivaUI);
							Logger.error(this, "ordene id bcv: "+ordeneIDBCV);
							//ACTUALIZO EL CAMPO DE OBSERVACION DE LA ORDEN
							ordenDAO.actualizarOrdenBCV(ordeneID, null, statusP, e.toString()); //SE LE DEJA EL MISMO ESTATUS PERO SE ACTUALIZA LA OBSERVACION
							continue; //NO QUIERO QUE ACTUALICE DE NUEVO
						}
						
						try {
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS ANULADA BCV
							ordenDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.ANULADA_BCV, "Anulacion ejecutada con exito");
						}  catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
							e.printStackTrace();
							continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
						}
					}else { //COMPORTAMIENTO ORIGINAL SE LLAMA AL METODO QUE REGISTRA LA DEMANDA
						try { 
							ordenBCV = stub.demanda(codigoCliente, nombreCliente, codMonedaIso, montoBase, tasaCambio, ordeneID, jornadaActivaUI, ctaConvenio20);
						} catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: "+ordeneID+" - "
									+e.toString()+" "+Utilitario.stackTraceException(e));
							e.printStackTrace();
							
							Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
							Logger.error(this, "codigoCliente: "+codigoCliente);
							Logger.error(this, "nombreCliente: "+nombreCliente);
							Logger.error(this, "montoBase: "+montoBase);
							Logger.error(this, "tasaCambio: "+tasaCambio);
							Logger.error(this, "codMonedaIso: "+codMonedaIso);
							Logger.error(this, "jornadaActiva: "+jornadaActivaUI);
							Logger.error(this, "ctaConvenio20: "+ctaConvenio20);
							
							boolean errorControlado = false;
							//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
							for (ErroresAltoValor tmp: ErroresAltoValor.values() ) {
								if(e.toString().contains(tmp.getCodigoError())){
									errorControlado = true;
									break;
								}
					        }
							
							if(errorControlado){
								//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
								ordenDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString());
							}else {
								//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
								ordenDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString());
							}
							continue; //NO QUIERO QUE ACTUALICE DE NUEVO
						}
						
						try {
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
							ordenDAO.actualizarOrdenBCV(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito");
						}  catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
							e.printStackTrace();
							continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
						}
					}
				}
			}else if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_ALTO_VALOR_OFER_PROCESAR.getNombreAccion()) || tipoOperacion == ofertaAnulacion) { //OFERTA - COMPRA ALTO VALOR
				String cedRifCliente;
				String tipper_id;
				Integer cedRifClienteIN;
				ClienteDAO clienteDAO = new ClienteDAO(_dso);
				ofertaDAO = new  OfertaDAO(_dso);
				
				if(clienteID != 0){
					cedRifClienteIN = Integer.parseInt(clienteDAO.buscarDatosPorIdCliente(String.valueOf(clienteID)));
				}else {
					cedRifClienteIN = 0;
				}
				
				ofertaDAO.listarOrdenesPorEnviarBCV(incluirCliente,cedRifClienteIN,tasaMinima,tasaMaxima,montoMinimo,montoMaximo, false, false, numeroDePagina,pageSize, todos, incluir, idOrdenes,statusP, fecha, "0",estatusCruce);
				_ordenes = ofertaDAO.getDataSet();
				
				while (_ordenes.next()) {
					//TIPO DE CLIENTE Y FECHA QUE VIENE DE LA TABLA SOLICITUDES_SITME
					cedRifCliente  = _ordenes.getValue("CLIENT_CEDRIF");		
					tipper_id      = _ordenes.getValue("TIPPER_ID");
					
					if(tipper_id.endsWith("J") || tipper_id.endsWith("G")){ //SI ES J O G SE COMPLETA CON 9 CARACTERES PARA UN TOTAL DE 10
						cedRifCliente = Utilitario.completarCaracterIzquierda(cedRifCliente.toString(), 9, "0");
					}else {
						cedRifCliente = Utilitario.completarCaracterIzquierda(cedRifCliente.toString(), 8, "0");
					}
					
					codigoCliente = tipper_id + cedRifCliente;
					System.out.println(" Envio Operacion SIMADI Alto Valor Oferta Cliente: ---> " + codigoCliente);
					nombreCliente = _ordenes.getValue("CLIENT_NOMBRE");
					codMonedaIso  =  ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
					montoBase     = new BigDecimal(_ordenes.getValue("ORDENE_MONTO_OFERTADO"));
					tasaCambio    = new BigDecimal(_ordenes.getValue("ORDENE_TASA_CAMBIO"));
					jornadaActivaUI = _ordenes.getValue("NRO_JORNADA");
					ordeneID          = _ordenes.getValue("ID_OFERTA");
					//tipoOperacion     = Long.parseLong(ConstantesGenerales.TIPO_DE_OPERACION);
					ordenBCV = "";
					ordeneIDBCV       = _ordenes.getValue("ordene_id_bcv");
					
					if(jornadaActivaUI == null || jornadaActivaUI == ""){
						//jornadaActivaUI = jornadaActiva;
						// TODO Auto-generated catch block
						Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV. La unidad de inversion no tiene el codigo de la jornada. ");
						proceso.agregarDescripcionError("La unidad de inversion no cuenta con el codigo de la jornada. Actualizar en el modulo: 'Configurar Jornada Unidad Inversion'");
						try {
							procesosDAO.modificar(proceso);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
						}
						throw new Exception();
					}
					
					if(tipoOperacion == ofertaAnulacion){ //SE VA A REALIZAR UNA ANULACION
						try { 
							ordenBCV = stub.ANULAOFERTA(jornadaActivaUI, ordeneIDBCV);
						} catch (Exception e) {							
							Logger.error(this, "Ha ocurrido un error al momento de anular la orden al BCV ORDENE_ID: "+ordeneID+" - ORDENE_ID_BCV: "+ordeneIDBCV
									+e.toString()+" "+Utilitario.stackTraceException(e));
							e.printStackTrace();
							Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
							Logger.error(this, "numero jornada: "+jornadaActivaUI);
							Logger.error(this, "ordene id bcv: "+ordeneIDBCV);
							//ACTUALIZO EL CAMPO DE OBSERVACION DE LA ORDEN
							//NM26659 - NM25287	_ 13/04/2016 Error en la actualizacion de estatus de Oferta.
							//ordenDAO.actualizarOrdenBCV(ordeneID, null, statusP, e.toString());
							ofertaDAO.actualizarOrdenBCV(ordeneID, null, statusP,e.toString());
							continue; //NO QUIERO QUE ACTUALICE DE NUEVO
						}
						
						try {
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS ANULADA BCV
							ofertaDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.ANULADA_BCV, "Anulacion ejecutada con exito");
						}  catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
							e.printStackTrace();
							continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
						}
					}else { //COMPORTAMIENTO ORIGINAL SE LLAMA AL METODO QUE REGISTRA LA OFERTA
						try {
							ordenBCV = stub.oferta(codigoCliente, nombreCliente, codMonedaIso, montoBase, tasaCambio, ordeneID, jornadaActivaUI);	 
						} catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV id_orden "+ordeneID+" - "
									+e.toString()+" "+Utilitario.stackTraceException(e));
							
							Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
							Logger.error(this, "codigoCliente: "+codigoCliente);
							Logger.error(this, "nombreCliente: "+nombreCliente);
							Logger.error(this, "montoBase: "+montoBase);
							Logger.error(this, "tasaCambio: "+tasaCambio);
							Logger.error(this, "codMonedaIso: "+codMonedaIso);
							//Logger.error(this, "tipoOperacion: "+tipoOperacion);
							Logger.error(this, "jornadaActiva: "+jornadaActivaUI);
							
							
							boolean errorControlado = false;
							//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
							for (ErroresAltoValor tmp: ErroresAltoValor.values() ) {
								if(e.toString().contains(tmp.getCodigoError())){
									errorControlado = true;
									break;
								}
					        }
							
							if(errorControlado){
								//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA RECHAZADA
								ofertaDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString());
							}else {
								//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
								ofertaDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString());
							}
							continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
						}
						
						try {
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
							ofertaDAO.actualizarOrdenBCV(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito");
						} catch (Exception e) {
							Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
							e.printStackTrace();
							continue; //NO QUIERO QUE ACTUALICE LA ORDEN SI OCURRE ALGUN ERROR
						}
					}
				}
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			proceso.agregarDescripcionError(e.toString());
			try {
				procesosDAO.modificar(proceso);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
			proceso.agregarDescripcionError(e.toString());
			try {
				procesosDAO.modificar(proceso);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
			proceso.agregarDescripcionError(e.toString());
			try {
				procesosDAO.modificar(proceso);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
			proceso.agregarDescripcionError(e.toString());
			try {
				procesosDAO.modificar(proceso);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV "+e.toString());
			e.printStackTrace();
			proceso.agregarDescripcionError(e.toString());
			try {
				procesosDAO.modificar(proceso);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
			}
		} finally {
			Logger.info(this, "FINALIZA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
		}
	}
	
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
