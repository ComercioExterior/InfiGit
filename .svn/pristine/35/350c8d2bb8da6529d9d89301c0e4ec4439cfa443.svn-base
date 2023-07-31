package models.bcv.mesa_cambio_consulta;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import javax.sql.DataSource;
import megasoft.AbstractModel;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import org.apache.axis.AxisFault;
import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.service.AutorizacionPortBindingStub;
import org.bcv.serviceMESACAMBIO.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

public class EnvioBCVWSMesaCambio extends AbstractModel implements Runnable {
	protected HashMap<String, String> parametrosMesaDeCambio;
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
	String idOrdenes;
	String statusP;
	String statusE;
	String Tipo;
	String urlInvocacion;
	DataSource _dso;
	String tipoTransaccion = TransaccionNegocio.WS_BCV_MESADECAMBIO;
	int idUsuario;
	String fecha;
	Integer clienteID;
	OrdenesCrucesDAO ordenesCrucesDAO;
	String jornadaMesaDeCambio="";
	
	
	public EnvioBCVWSMesaCambio (Integer clienteID,int numeroDePagina, int pageSize, boolean todos,String idOrdenes, String statusP, String urlInvocacion, DataSource _dso, int idUsuario, String fecha,String statusE,String Tipo){
	
		this.numeroDePagina = numeroDePagina;
		this.pageSize = pageSize;
		this.todos = todos;
		this.idOrdenes = idOrdenes;
		this.statusP = statusP;
		this.statusE = statusE;
		this.Tipo = Tipo;
		this.urlInvocacion = urlInvocacion;
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.credencialesDAO = new CredencialesDAO(_dso);
		this.clienteID   = clienteID;
	}

	//NM26659_07/09/2015 Sobrecarga de constructor para inclusion de tipo transaccion  
	public EnvioBCVWSMesaCambio (Integer clienteID,int numeroDePagina, int pageSize, boolean todos,String idOrdenes, String statusP, String urlInvocacion, DataSource _dso, int idUsuario, String fecha,String tipoTransaccion,String statusE,String Tipo){
		this.numeroDePagina = numeroDePagina;
		this.pageSize = pageSize;
		this.todos = todos;
		this.idOrdenes = idOrdenes;
		this.statusP = statusP;
		this.statusE = statusE;
		this.Tipo = Tipo;
		this.urlInvocacion = urlInvocacion;
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.credencialesDAO = new CredencialesDAO(_dso);
		this.clienteID   = clienteID;
		this.tipoTransaccion=tipoTransaccion;
	}
	
	public void run() {
		//INCIAR PROCESO
		try {
			System.out.println("paso procesar arbol");
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
			
//			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			obtenerParametros();
			
			Logger.info(this, "SE INICIA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
			_credenciales = credencialesDAO.getDataSet();
			Propiedades propiedades =  Propiedades.cargar();
			String userName = "";
			String clave    = "";
			
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
				
				userName = "C1040001020101";//desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave    = "bcv2021";//desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));//"bcv2019";
			}else {
				Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: "+ tipoTransaccion);
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
			String codigoBanco;
			String nombreCliente;
			BigDecimal montoBase=new BigDecimal(0);
			BigDecimal tasaCambio;
			BigDecimal montoTransaccion=new BigDecimal(0);
			String cuentaCliente;
			String cuentaConvenio;
			String   tipoOperacion;
			String codMonedaIso;
			String tipoMovimiento = "";
			String instrumento ="";

			String ordenBCV;
			String rifClient;
			String Codigo= ConstantesGenerales.TIPO_MOVIMIENTOS_MENUDEO_V;

			if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_MESA_CAMBIO_PROCESAR.getNombreAccion())){ //DEMANDA - VENTA

				ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);

				ordenDAO         = new OrdenDAO(_dso);

				ordenesCrucesDAO.listarOrdenesPorEnviarMesaDeCambio(false,true, numeroDePagina,pageSize, todos, fecha,statusE,Tipo,idOrdenes);
				_ordenes = ordenesCrucesDAO.getDataSet();
				while (_ordenes.next()) {
					jornadaMesaDeCambio = parametrosMesaDeCambio.get(ParametrosSistema.JORNADA_MESA_CAMBIO);
					rifClient = _ordenes.getValue("RIF_CLIENTE");
					ordeneID = _ordenes.getValue("ID_OPER");
					tipoOperacion = _ordenes.getValue("movimiento");
					nombreCliente = _ordenes.getValue("NOM_CLIEN");
					codMonedaIso = _ordenes.getValue("COD_DIVISAS");
					montoTransaccion = new BigDecimal(_ordenes.getValue("MTO_DIVISAS"));
					tasaCambio    = new BigDecimal(_ordenes.getValue("TASA_CAMBIO"));
					codigoBanco =  _ordenes.getValue("COD_INS_BANCO");
					cuentaCliente = _ordenes.getValue("CTA_CLIEN");
					cuentaConvenio = _ordenes.getValue("CTA_CONVENIO");
					instrumento =_ordenes.getValue("INSTRUMENTO");
					
					ordenBCV = "";
					

					System.out.println("tipoMovimiento-->"+tipoMovimiento);
					try {
						System.out.println("paso ");
						System.out.println("rifClient-->"+rifClient);
						System.out.println("ordeneID-->"+ordeneID);
						System.out.println("tipoOperacion-->"+tipoOperacion);
						System.out.println("nombreCliente-->"+nombreCliente);
						System.out.println("codMonedaIso-->"+codMonedaIso);
						System.out.println("montoTransaccion-->"+montoTransaccion);
						System.out.println("tasaCambio-->"+tasaCambio);
						System.out.println("codigoBanco-->"+codigoBanco);
						System.out.println("cuentaCliente-->"+cuentaCliente);
						System.out.println("cuentaConvenio-->"+cuentaConvenio);
						System.out.println("jornadaMesaDeCambio-->"+jornadaMesaDeCambio);
						System.out.println("instrumento-->"+instrumento);

						if(tipoOperacion.toString().equals(Codigo)){
							
							
							
							ordenBCV = stub.oferta(rifClient, 
									nombreCliente, 
									codMonedaIso, 
									montoTransaccion, 
									tasaCambio, 
									codigoBanco, jornadaMesaDeCambio, 
									cuentaConvenio, 
									cuentaCliente, 
									instrumento);
						}else{
							System.out.println("paso COMPRA");
							ordenBCV = stub.demanda(rifClient, 
									nombreCliente, 
									codMonedaIso, 
									montoTransaccion, 
									tasaCambio, 
									codigoBanco, 
									jornadaMesaDeCambio, 
									cuentaConvenio, 
									cuentaCliente);
							
						}
						
						System.out.println("ordenBCV--->"+ordenBCV);
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: "+ordeneID+" - "
								+e.toString()+" "+Utilitario.stackTraceException(e));
						e.printStackTrace();
						proceso.agregarDescripcionErrorTrunc("Ha ocurrido un error al momento de enviar al bcv revisar en observacion de menudeo",true);

						Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV");
						Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
						Logger.error(this, "nombreCliente: "+nombreCliente);
						Logger.error(this, "montoBase: "+montoBase);
						Logger.error(this, "tasaCambio: "+tasaCambio);
						Logger.error(this, "codMonedaIso: "+codMonedaIso);
						Logger.error(this, "montoTransaccion: "+montoTransaccion);
						Logger.error(this, "tipoOperacion: "+tipoOperacion);
						
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
							proceso.agregarDescripcionErrorTrunc("Revisar el detalle algunas operaciones no fueron enviadas",true);
							ordenDAO.actualizarOrdenBCVMesaDeCambio(ordeneID,fecha,e.toString(),"",ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA);
							ordenesCrucesDAO.actualizarMesaCambioBCV(ordeneID,fecha,ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA);
							//
						}else {//SE GENERA UN ERROR NO CONTROLADO
							//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
							ordenDAO.actualizarOrdenBCVMesaDeCambio(ordeneID,fecha,e.toString(),"",ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA);
							ordenesCrucesDAO.actualizarMesaCambioBCV(ordeneID,fecha,ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA);
							
						}
						
						continue; //NO QUIERO QUE ACTUALICE DE NUEVO
					}
					
					try {
						//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA APROBADA
				
						ordenDAO.actualizarOrdenBCVMesaDeCambio(ordeneID,fecha,"Envio a BCV ejecutado con exito-->"+ordenBCV,ordenBCV,ConstantesGenerales.ENVIO_MENUDEO);
						ordenesCrucesDAO.actualizarMesaCambioBCV(ordeneID,fecha,ConstantesGenerales.ENVIO_MENUDEO);
						proceso.agregarDescripcionErrorTrunc("Todas las operaciones fueron enviadas",true);
						Logger.info("Notificacion","Envio a BCV ejecutado con exito");
					} catch (Exception e) {
						Logger.error(this, "Ha ocurrido un error al momento de actualizar el estaus del BCV "+ordeneID+" para la orden INFI "+ordeneID+"."+e.toString());
						e.printStackTrace();
						//continue; //NO QUIERO QUE ENTRE AL TRY SIGUIENTE, QUIERO QUE INICIE LA SIGUIENTE ITERACION
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
	protected void obtenerParametros() throws Exception {		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
		parametrosMesaDeCambio=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MESA_CAMBIO);
	}	
}
