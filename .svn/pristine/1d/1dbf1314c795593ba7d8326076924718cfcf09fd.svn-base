package models.bcv.pacto;
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
import models.bcv.alto_valor.ErroresAltoValor;
import models.bcv.menudeo.ErroresMenudeo;

import org.apache.axis.AxisFault;
import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.serviceAltoValor.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OfertaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

public class EnvioBCVWSPacto extends AbstractModel implements Runnable {
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	OrdenesCrucesDAO ordenesCrucesDAO;
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
	String tipoTransaccion = TransaccionNegocio.WS_BCV_PACTO; 
	int idUsuario;
	String fecha;
	Integer tasaMinima;
	Integer tasaMaxima;
	Integer montoMinimo;
	Integer montoMaximo;
	Integer clienteID;
	Integer incluirCliente;
	
	public EnvioBCVWSPacto (Integer incluirCliente, Integer clienteID,Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo, long idUnidad, int numeroDePagina, int pageSize, boolean todos, boolean incluir, String idOrdenes, String statusP, String urlInvocacion, DataSource _dso, int idUsuario, String fecha){
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
			String ordeneIDBCVOferta;
			String ordenIDBCVDemanda;
			BigDecimal montoAdjudicado;
			String ordenBCV;
			String ordeneID;
			String jornadaActivaUI = "";
			String estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
			String cruceProcesado = "0";
			
			if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_PACTO_PROCESAR.getNombreAccion())){ //PACTO  ALTO VALOR
				ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
				ordenesCrucesDAO.listarOrdenesPorEnviarBCV(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false,idUnidad, false, numeroDePagina,pageSize, todos, incluir, idOrdenes,statusP, fecha, estatusOrdenINFI);
				_ordenes = ordenesCrucesDAO.getDataSet();
				
				while (_ordenes.next()) {
					ordeneID           =  _ordenes.getValue("ordene_id");
					ordeneIDBCVOferta  =  _ordenes.getValue("ordene_id_bcv_of");
					ordenIDBCVDemanda  =  _ordenes.getValue("ordene_id_bcv_de");
					montoAdjudicado    =  new BigDecimal(_ordenes.getValue("ordene_adj_monto") == null ? "0" : _ordenes.getValue("ordene_adj_monto"));
					jornadaActivaUI    =  _ordenes.getValue("nro_jornada");
					
					if(jornadaActivaUI == null || jornadaActivaUI == ""){
						// TODO Auto-generated catch block
						Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV. Las ordenes no tiene el codigo de la jornada. ");
						proceso.agregarDescripcionError("Las ordenes no cuenta con el codigo de la jornada. Por favor actualizar");
						try {
							procesosDAO.modificar(proceso);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							Logger.error(this, "Ha ocurrido un error en la modificacion del Proceso:"+proceso.getEjecucionId()+"Error: "+e1.toString());
						}
						throw new Exception();
					}
					
					ordenBCV = "";

					try { 
						ordenBCV = stub.pacto(jornadaActivaUI, ordeneIDBCVOferta, ordenIDBCVDemanda, montoAdjudicado);
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: "+ordeneID+" - "
								+e.toString()+" "+Utilitario.stackTraceException(e));
						e.printStackTrace();
						
						Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
						Logger.error(this, "ordeneIDBCVOferta: "+ordeneIDBCVOferta);
						Logger.error(this, "ordenIDBCVDemanda: "+ordenIDBCVDemanda);
						Logger.error(this, "montoAdjudicado: "+montoAdjudicado);
						
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
							ordenesCrucesDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.VERIFICADA_RECHAZADA, e.toString(), ConstantesGenerales.STATUS_INVALIDA);
						}else {
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
							ordenesCrucesDAO.actualizarOrdenBCV(ordeneID, null, ConstantesGenerales.SIN_VERIFICAR, e.toString(), null);
						}
					
						continue; //NO QUIERO QUE ACTUALICE DE NUEVO
					}
					
					try {
						//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
						ordenesCrucesDAO.actualizarOrdenBCV(ordeneID, ordenBCV, ConstantesGenerales.VERIFICADA_APROBADA, "Envio a BCV ejecutado con exito", null);
					}  catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de actualizar la orden del BCV "+ordenBCV+" para la orden INFI "+ordeneID+"."+e.toString());
						e.printStackTrace();
						continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
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
