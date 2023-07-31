package com.bdv.infi.logic.cruces_ordenes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ArchivoRetencionDAO;
import com.bdv.infi.dao.AuditoriaDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OfertaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.OrdenesCruce;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.IngresoOpicsSitme;
import com.bdv.infi.logic.ProcesarDocumentos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.logic.interfaz_ops.ConciliacionRetencionEnvio;
import com.bdv.infi.logic.interfaz_varias.CallEnvioCorreos;
import com.bdv.infi.util.Utilitario;



public class CruceOrdenesSICAD2 extends AbstractModel implements Runnable {
	private DataSource _dso;
	private int idUsuario;
	private ServletContext _app;
	private String ip;
	//private String nombreUsuario;
	
	private String sucursal;
	private String unidadInversion;
	private String nmUsuario; 
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	private final String NO_CRUZADA="NO_CRUZADA";
	private final String CRUZADO_PARCIAL="CRUZADO_PARCIAL";
	private final String CRUZADO_TOTAL="CRUZADO_TOTAL";
	private String vehiculoId;
	

	//Listado de movimientos asociados a los cruces
	private TreeMap<String,OrdenesCruce> movimientosCruces=new TreeMap<String,OrdenesCruce>();
	ArrayList<OrdenTitulo> listaOrdenesTitulos=null;
	
	ArrayList<String> querysTransacciones=new ArrayList<String>();
	
	private OrdenesCruce ordenesCruce;
	private OrdenDataExt ordenDataExt;	
	
	//Variable que determina el TIPO DE PRODUCTO asociado al proceso de cruce de ordenes 
	private String tipoProducto;
	private String tipoTransaccion;
	private String tipoNegocio;
	
	
	
	
	//Configuracion DataSets
	private DataSet _crucesConTitulo;
	private DataSet _crucesDivisas;
	
	private String cruceTipo="";
	private String estatusCruce="";
	
	private String codigo_operacion;
	private String nombre_operacion;
	
	CuentaCliente clienteCuentaEntidad=null;
	
	//Objetos Bean
	private Orden orden;
	private OrdenTitulo ordenTitulo;
	
	//DAO
	private ProcesosDAO procesosDAO;
	
	private ClienteCuentasDAO clienteCuentasDAO;	
	private TransaccionFijaDAO transaccionFijaDAO;	
	private OrdenDAO ordenCLaveNet;
	private TitulosDAO titulosDAO;
	private UnidadInversionDAO unidadInvDAO;
	private OrdenDAO ordenDAO;
	private OrdenesCrucesDAO ordenesCrucesDAO;
	private OperacionDAO operacionDAO;
	private ClienteDAO clienteDAO;
	
	private OfertaDAO ofertaDAO;
	
	private com.bdv.infi.dao.Transaccion transaccion;
	
	BigDecimal participacionTitulo=new BigDecimal(0);
	BigDecimal participacionDivisa=new BigDecimal(0);
	
	boolean cruceMixto=false;
	//NM26659 31/03/2015 Inclusion manejo SIMADI Alto Valor
	private boolean simadiAltoValor=false;
	
	DataSet transaccionesFijasAdjudicacion = new DataSet();
	private HashMap<String,DataSet> transaccionesFijasCache = new HashMap<String,DataSet>();
	
	//BUSQUEDA DE COMISION CONFIGURADA EN LA ORDEN								
	private BigDecimal tasaComisionVariable=new BigDecimal(0);											
	private BigDecimal montoComisionVariable=new BigDecimal(0);
	private  BigDecimal montoComisionInvariable=new BigDecimal(0);
	private boolean comisionFijaCobrada=false;
	
	long cantidadOrdenesProcesadas;
	long contadorOrdenesProcesadas;
	long acumuladorOrdenesProcesadas=0;
	
	String tipoTransaccionFinanciera=null;	
	
	//private Logger logger = Logger.getLogger(CruceOrdenesSICAD2.class);
	
	public CruceOrdenesSICAD2(DataSource _dso, int usuario, ServletContext _app, HttpServletRequest _req, String unidadInversion,String tipoProducto,String transaccion, String nmUsuario,String tipoNegocio) throws Exception {
		this._dso = _dso;
		this.idUsuario = usuario;
		this._app = _app;
		this.ip = _req.getRemoteAddr();
		//this.nombreUsuario = _req.getSession().getAttribute("framework.user.principal").toString();
		//NM29643 - infi_TTS_466: A veces da error null pointer excepction por problemas con la sesion
		//Logger.debug(this, "Sesion: "+_req.getSession());
		Logger.debug(this, "Sesion Attribute Sucursal: "+_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL));
		this.sucursal = _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL)==null?"":_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL).toString();
		this.unidadInversion = unidadInversion;
		this.tipoProducto=tipoProducto;
		this.nmUsuario = nmUsuario;	
		this.tipoNegocio=tipoNegocio;
		
		if(tipoNegocio!=null && !tipoNegocio.equals("") && tipoNegocio.equals(ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR)){						
			simadiAltoValor=true;			
		}
		tipoTransaccion=transaccion;
		
		clienteDAO = new ClienteDAO(_dso); 
		procesosDAO = new ProcesosDAO(_dso);
		ordenDAO = new OrdenDAO(_dso);		
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);		
		ordenesCrucesDAO=new OrdenesCrucesDAO(_dso);
		ofertaDAO=new OfertaDAO(_dso);
	}
	
	public void run() {
		try {
			//INCIAR PROCESO
			iniciarProceso();
			
			procesoCruceOrdenes();
			
			conciliacionRetenciones();
			finalizarProceso();
			
		} catch (Exception e) {			
			proceso.agregarDescripcionError("Ocurrio un error durante el proceso de cruce de ordenes: "+e.getMessage());
			e.printStackTrace();
			try {
				finalizarProceso();
			} catch (Exception ex) {
				ex.printStackTrace();
				proceso.agregarDescripcionError("Ocurrio un error durante el proceso de cruce de ordenes: "+ex.getMessage());
			}			
			Logger.info(this,"ERROR EN EL PROCESO DE CIERRE DE CRUCE SICAD 2 ---> " + tipoProducto + " Error: " + e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
			Logger.info(this,"ERROR EN EL PROCESO DE CIERRE DE CRUCE SICAD 2 ---> " + tipoProducto + " Error: " + e.getMessage());
			proceso.agregarDescripcionError("Ocurrio un error durante el proceso de cruce de ordenes: "+e.getMessage());
		}
		
		Logger.info(this, "DESCRIPCION ERROR (Cruce): "+proceso.getDescripcionError());
		
		if(proceso.getDescripcionError()==null || proceso.getDescripcionError().equals("")){
			
			//NM29643 infi_TTS_466 17/07/2014: Actualizacion del proceso de envio de correos
			Logger.debug(this, "UI ID en CruceOrdenesSICAD2 (General) - Llamada a ENVIO CORREOS CRUCE -----------------: "+unidadInversion);
			//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Desde Aqui)**************
			CallEnvioCorreos callEnvio = new CallEnvioCorreos(ConstantesGenerales.CRUCE, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, nmUsuario, unidadInversion, _dso, null, null, null);
			Thread t = new Thread(callEnvio); //Ejecucion del hilo que envia los correos
			t.setPriority(Thread.MAX_PRIORITY); //Se da mayor prioridad al envio de cruces
			t.start();
			//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Hasta Aqui)**************
			try {
				t.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//NM29643 infi_TTS_466 17/07/2014: Actualizacion del proceso de envio de correos
			Logger.debug(this, "UI ID en CruceOrdenesSICAD2 (General) - Llamada a ENVIO CORREOS NO_CRUCE -----------------: "+unidadInversion);
			//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Desde Aqui)**************
			CallEnvioCorreos callEnvio2 = new CallEnvioCorreos(ConstantesGenerales.NO_CRUCE, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, nmUsuario, unidadInversion, _dso, null, null, null);
			Thread t2 = new Thread(callEnvio2); //Ejecucion del hilo que envia los correos
			t2.setPriority(Thread.NORM_PRIORITY); //Se da menor prioridad al envio de no cruces
			t2.start();
			
		}
	}

	private void procesoCruceOrdenes() throws Exception {
		
		//29643 infi_TTS_466
		try{
		
		Logger.info(this,"*************** INICIO DE PROCESO DE CRUCE ORDENES "+ tipoProducto + " ***************");
		//System.out.println("*************** INICIO DE PROCESO DE CRUCE ORDENES "+ tipoProducto + " ***************");
		
		listaOrdenesTitulos=new ArrayList<OrdenTitulo>();
		operacionDAO=new OperacionDAO(_dso);
		ordenCLaveNet=new OrdenDAO(_dso);
		titulosDAO=new TitulosDAO(_dso);
		
		clienteCuentasDAO=new ClienteCuentasDAO(_dso);
		transaccion = new com.bdv.infi.dao.Transaccion(_dso);
		transaccion.begin();
		
		long idUnidadInversion=Long.parseLong(unidadInversion);		
		ordenesCrucesDAO.listarCrucesPorUnidadInversion(idUnidadInversion,true,null,ConstantesGenerales.CRUCE_SIN_PROCESADO,StatusOrden.CRUZADA,StatusOrden.NO_CRUZADA);
		_crucesConTitulo=ordenesCrucesDAO.getDataSet();
		
		ordenesCrucesDAO.listarCrucesPorUnidadInversion(idUnidadInversion,false,null,ConstantesGenerales.CRUCE_SIN_PROCESADO,StatusOrden.CRUZADA,StatusOrden.NO_CRUZADA);
		_crucesDivisas=ordenesCrucesDAO.getDataSet();
				
		//Variable que determina la orden que se esta procesando		
		String ordenEnProceso=null;
		String ordenActual=null;
		String OrdenBcv=null;		
		BigDecimal montoCruzado=new BigDecimal(0);
		BigDecimal montoTotalTitulosBs=new BigDecimal(0);
		BigDecimal montoTotalDivisasBs=new BigDecimal(0);
		
		 ordenEnProceso=null;
		 ordenActual=null;
		 OrdenBcv=null;
		 String fechaValor=null;
		 String statusCruce=null;
		 ordenesCruce=new OrdenesCruce();
		 //Busqueda de Cruces asociados a una orden por DIVISAS		
		 Logger.info(this,"*************** PROCESO DE CRUCE ORDENES 2");		 
		if(_crucesDivisas.count()>0){			
			Logger.info(this,"*************** PROCESO DE CRUCE ORDENES 3 ");
			_crucesDivisas.first();		
			long registroDivisas=0;			
			while(_crucesDivisas.next()){				
				statusCruce=_crucesDivisas.getValue("estatus");
				
				//Incidencia Calidad 12/05/2014_NM26659
				/*if(statusCruce.equals(StatusOrden.CRUZADA)){
					ordenesCruce.agregarIdBcv(_crucesDivisas.getValue("ORDENE_ID_BCV")==null?0L:Long.parseLong(_crucesDivisas.getValue("ORDENE_ID_BCV")));
					ordenesCruce.agregarContraparteList(_crucesDivisas.getValue("CONTRAPARTE"));	
				}*/
				
				
				ordenEnProceso=_crucesDivisas.getValue("ORDENE_ID");//Orden que se va a procesar								
				fechaValor=_crucesDivisas.getValue("fecha_valor");
				//System.out.println("fecha valor ----> " + fechaValor);
				//System.out.println("Orden DIVISA en Proceso ---> " + ordenEnProceso + " estatus ---> " + statusCruce);
				if(ordenActual!=null && !ordenActual.equals(ordenEnProceso)){//Si la orden a Procesar no es la que ya se estaba configurando se finiquita															
					//System.out.println("Finiquitar la Orden ----> " + ordenActual);						
					ordenesCruce.setFechaValor(fechaValor);
				
					
					movimientosCruces.put(ordenActual, ordenesCruce);	
					ordenesCruce=new OrdenesCruce();
					montoCruzado=new BigDecimal(0);
					montoTotalDivisasBs=new BigDecimal(0);
				}
								
				//Configuracion Orden actual
				ordenActual=ordenEnProceso;				
				if(statusCruce.equals(StatusOrden.NO_CRUZADA)){						
										
					ordenesCruce.setEstatus(StatusOrden.NO_CRUZADA);
					ordenesCruce.setIdOrdenINFI(Long.parseLong(ordenEnProceso));	
					ordenesCruce.setMontoTitulos(new BigDecimal(0));
					ordenesCruce.setMontoDivisas(new BigDecimal(0));					
				} else if(statusCruce.equals(StatusOrden.CRUZADA)){						
					
					montoCruzado=montoCruzado.add(new BigDecimal(_crucesDivisas.getValue("monto_operacion")));
					ordenesCruce.setEstatus(StatusOrden.CRUZADA);
					ordenesCruce.setIdOrdenINFI(Long.parseLong(ordenEnProceso));
					ordenesCruce.setMontoDivisas(montoCruzado);					
					ordenesCruce.setTasa(new BigDecimal(_crucesDivisas.getValue("tasa")));		
					
					ordenesCruce.setIdOrdenOfertaString(_crucesDivisas.getValue("ORDENE_ID_OFERTA"));					
					if(ordenesCruce.getIdOrdenOfertaString()!=null && !ordenesCruce.getIdOrdenOfertaString().equals("")){
						ordenesCruce.setIdOrdenOferta(Long.parseLong(ordenesCruce.getIdOrdenOfertaString()));
					}
					//BigDecimal tasaCambio=new BigDecimal(_crucesDivisas.getValue("tasa"));
					//System.out.println(" ORDEN DIVISA " + ordenActual + " tasa  " + tasaCambio);
					//ordenesCruce.agregarIdBcv(_crucesDivisas.getValue("ORDENE_ID_BCV")==null?0L:Long.parseLong(_crucesDivisas.getValue("ORDENE_ID_BCV")));
					ordenesCruce.agregarIdBcv(_crucesDivisas.getValue("ORDENE_ID_BCV"));
					ordenesCruce.agregarContraparteList(_crucesDivisas.getValue("CONTRAPARTE"));
					//montoTotalDivisasBs=montoTotalDivisasBs.add(montoCruzado.multiply(tasaCambio));					
					montoTotalDivisasBs=montoTotalDivisasBs.add(new BigDecimal(_crucesDivisas.getValue("contravalor_bolivares_capital")));

					ordenesCruce.setMontoTotalDivisasBs(montoTotalDivisasBs);				
				}								

				++registroDivisas;					
				if(_crucesDivisas.count()==registroDivisas){
					//System.out.println("Finiquito Ultima Orden -------> " + ordenActual );
					ordenesCruce.setFechaValor(fechaValor);					
					movimientosCruces.put(ordenActual, ordenesCruce);  
				}
												
			}
		}
		Logger.info(this,"*************** PROCESO DE CRUCE ORDENES 4");
		//Busqueda de Cruces asociados a una orden por TITULOS
		montoCruzado=new BigDecimal(0);
		long registroTitulos=0;
		BigDecimal tasaTitulos=new BigDecimal(0);
		ordenActual=null; 
		boolean indicadorTituloDuplicado=false;		
		//ordenesCruce=new OrdenesCruce();
		if(_crucesConTitulo.count()>0){				
			//System.out.println(" CRUCES TITULOS ");
			_crucesConTitulo.first();			
			while(_crucesConTitulo.next()){		
				ordenEnProceso=_crucesConTitulo.getValue("ORDENE_ID");//Orden que se va a procesar
//				ordenesCruce.agregarIdBcv(_crucesConTitulo.getValue("ORDENE_ID_BCV")==null?0L:Long.parseLong(_crucesConTitulo.getValue("ORDENE_ID_BCV")));
//				ordenesCruce.agregarContraparteList(_crucesConTitulo.getValue("CONTRAPARTE"));
				fechaValor=_crucesConTitulo.getValue("fecha_valor");
				//System.out.println("Orden TITULO en Proceso ---> " + ordenEnProceso );				
				if(ordenActual!=null && !ordenActual.equals(ordenEnProceso)){//Si la orden a Procesar no es la que ya se estaba configurando se finiquita
					if(!movimientosCruces.containsKey(ordenActual)){
						//ordenesCruce=new OrdenesCruce();
						ordenesCruce=new OrdenesCruce();
						ordenesCruce.setIdOrdenINFI(Long.parseLong(ordenActual));
						//Resolucion Incidencia Calidad NM26659_16/05/2014
						ordenesCruce.setFechaValor(fechaValor);
						ordenesCruce.setEstatus(StatusOrden.CRUZADA);
						ordenesCruce.setMontoTitulos(montoCruzado);
						ordenesCruce.setMontoTotalTitulosBs(montoTotalTitulosBs);
						ordenesCruce.agregarOrdenTitulo(listaOrdenesTitulos);
						//ordenesCruce.setTasa(new BigDecimal(_crucesConTitulo.getValue("tasa")));
						ordenesCruce.setTasa(tasaTitulos);
						//ordenesCruce.setTasa(tasa);
						//movimientosCruces.put(Long.parseLong(ordenActual), ordenesCruce);
						movimientosCruces.put(ordenActual, ordenesCruce);
						
						//REINICIO DE VALORES PARA PROCESAMIENTO DE PROXIMA ORDEN
						montoCruzado=new BigDecimal(0);
						montoTotalTitulosBs=new BigDecimal(0);
						ordenesCruce=new OrdenesCruce();
						tasaTitulos=new BigDecimal(0);
						indicadorTituloDuplicado=false;
						listaOrdenesTitulos.clear();
						
					} else {	

						ordenesCruce=movimientosCruces.get(ordenActual);
						ordenesCruce.setFechaValor(fechaValor);
						ordenesCruce.setEstatus(StatusOrden.CRUZADA);
						
						ordenesCruce.setMontoTitulos(montoCruzado);
						ordenesCruce.setMontoTotalTitulosBs(montoTotalTitulosBs);
						//ordenesCruce.setTasa(new BigDecimal(_crucesConTitulo.getValue("tasa")));
						ordenesCruce.setTasa(tasaTitulos);
						ordenesCruce.agregarOrdenTitulo(listaOrdenesTitulos);
						//REINICIO DE VALORES PARA PROCESAMIENTO DE PROXIMA ORDEN
						montoCruzado=new BigDecimal(0);
						montoTotalTitulosBs=new BigDecimal(0);
						ordenesCruce=new OrdenesCruce();
						tasaTitulos=new BigDecimal(0);
						indicadorTituloDuplicado=false;
						listaOrdenesTitulos.clear();
					}		
				}
				
				
				ordenActual=ordenEnProceso;
				int posicionTitulo=0;
				if(listaOrdenesTitulos.size()>0){					
					 for (OrdenTitulo titulo : listaOrdenesTitulos) {//Recorrido de los titulos configurados para la ordenActual						 
							if(titulo.getTituloId().equals(_crucesConTitulo.getValue("TITULO_ID"))){
								indicadorTituloDuplicado=true;								
								break;
							}
							++posicionTitulo;
					 }								
				}
				
				
				//OrdenBcv=_crucesConTitulo.getValue("ORDENE_ID_BCV");
				//System.out.println("Procesamiento Cierre de Cruce Titulo de la Orden INFI ---> "+ ordenActual + " - Orden BCV ---> " + OrdenBcv);					
				tasaTitulos=new BigDecimal(_crucesConTitulo.getValue("tasa"));
				if(indicadorTituloDuplicado){//BUSQUEDA DEL TITULO PARA LA ACTUALIZACION DE SUS MONTOS
					
					ordenTitulo=listaOrdenesTitulos.get(posicionTitulo);
					
					BigDecimal unidadesTitulo=new BigDecimal(0);
					unidadesTitulo=new BigDecimal(ordenTitulo.getUnidades()).add(new BigDecimal(_crucesConTitulo.getValue("VALOR_NOMINAL"))) ;
					
					BigDecimal montoTitulo=new BigDecimal(0);
					montoTitulo=new BigDecimal(ordenTitulo.getMonto()).add(new BigDecimal(_crucesConTitulo.getValue("VALOR_NOMINAL"))) ;
					
					BigDecimal interesesCaidosTitulo=new BigDecimal(0);
					interesesCaidosTitulo=ordenTitulo.getMontoIntCaidos().add(new BigDecimal(_crucesConTitulo.getValue("TITULO_MTO_INT_CAIDOS")));
					
					ordenTitulo.setUnidades(unidadesTitulo.doubleValue());
					ordenTitulo.setMonto(montoTitulo.doubleValue());
					ordenTitulo.setMontoIntCaidos(interesesCaidosTitulo);
					indicadorTituloDuplicado=false;
				} else {//CREACION DE NUEVO REGISTRO DE TITULO
					ordenTitulo=new OrdenTitulo();
					ordenTitulo.setIdOrden(Long.parseLong(ordenActual));
					ordenTitulo.setTituloId(_crucesConTitulo.getValue("TITULO_ID"));

					//Inclusion cambios para envio DEALS Opics
					ordenTitulo.setTasaCambio(tasaTitulos);
					ordenTitulo.setFechaValor(_crucesConTitulo.getValue("FECHA_VALOR"));
					ordenTitulo.setPorcentaje(_crucesConTitulo.getValue("PRECIO_TITULO"));
					ordenTitulo.setUnidades(_crucesConTitulo.getValue("VALOR_NOMINAL")==null?0:Double.parseDouble(_crucesConTitulo.getValue("VALOR_NOMINAL")));
					ordenTitulo.setMonto(_crucesConTitulo.getValue("MONTO_OPERACION")==null?0:Double.parseDouble(_crucesConTitulo.getValue("MONTO_OPERACION")));
				ordenTitulo.setMontoIntCaidos(new BigDecimal(_crucesConTitulo.getValue("TITULO_MTO_INT_CAIDOS")));
					ordenTitulo.setMontoNeteo(new BigDecimal(0));
					listaOrdenesTitulos.add(ordenTitulo);										
				}

				//System.out.println("FIN CREACION DE 206");
				
				//validar si la orden ya posee un cruce con el mismo título
				//listaOrdenesTitulos.get(0).getTituloId()==""
						

				montoCruzado=montoCruzado.add(new BigDecimal(_crucesConTitulo.getValue("monto_operacion")));
				
				//BigDecimal montoEfectivo=new BigDecimal(_crucesConTitulo.getValue("monto_operacion"));
				BigDecimal contravalorEfectivo=new BigDecimal(_crucesConTitulo.getValue("contravalor_bolivares_capital"));

				
				//BigDecimal tasaCambio=new BigDecimal(_crucesConTitulo.getValue("tasa"));
				montoTotalTitulosBs=montoTotalTitulosBs.add(contravalorEfectivo);
				
				++registroTitulos;																				
														
				//PROCESAMIENTO ULTIMO REGISTRO
				if(_crucesConTitulo.count()==registroTitulos){//**********Procesamiento de la ultima orden de la consulta*************
					if(!movimientosCruces.containsKey(ordenActual)){						
						//System.out.println(" No contiene el titulos");
						//ordenesCruce=new OrdenesCruce();
						ordenesCruce=new OrdenesCruce();
						ordenesCruce.setIdOrdenINFI(Long.parseLong(ordenActual));						
						ordenesCruce.setEstatus(StatusOrden.CRUZADA);
						ordenesCruce.setMontoTitulos(montoCruzado);
						//Resolucion Incidencia Calidad NM26659_16/05/2014
						ordenesCruce.setFechaValor(fechaValor);
						ordenesCruce.setMontoTotalTitulosBs(montoTotalTitulosBs);
						//ordenesCruce.setTasa(new BigDecimal(_crucesConTitulo.getValue("tasa")));
						ordenesCruce.setTasa(tasaTitulos);												
						ordenesCruce.agregarOrdenTitulo(listaOrdenesTitulos);
						//ordenesCruce.agregarIdBcv(_crucesConTitulo.getValue("ORDENE_ID_BCV")==null?0L:Long.parseLong(_crucesConTitulo.getValue("ORDENE_ID_BCV")));//TODO VALDIDATE VERACIDAD
						ordenesCruce.agregarIdBcv(_crucesConTitulo.getValue("ORDENE_ID_BCV"));//TODO VALDIDATE VERACIDAD
						ordenesCruce.agregarContraparteList(_crucesConTitulo.getValue("CONTRAPARTE"));//TODO VALDIDATE VERACIDAD

						listaOrdenesTitulos.clear();		
												
						//orden.setStatus(StatusOrden.CRUZADA);
						//movimientosCruces.put(Long.parseLong(ordenActual), ordenesCruce);
						movimientosCruces.put(ordenActual, ordenesCruce);
						//REINICIO DE VALORES PARA PROCESAMIENTO DE PROXIMA ORDEN
						montoCruzado=new BigDecimal(0);
						montoTotalTitulosBs=new BigDecimal(0);
						ordenesCruce=new OrdenesCruce();
						tasaTitulos=new BigDecimal(0);
						listaOrdenesTitulos.clear();						
					} else {		
						ordenesCruce=movimientosCruces.get(ordenActual);
						ordenesCruce.setFechaValor(fechaValor);
						ordenesCruce.setEstatus(StatusOrden.CRUZADA);
						ordenesCruce.setMontoTitulos(montoCruzado);
						ordenesCruce.setMontoTotalTitulosBs(montoTotalTitulosBs);
						//ordenesCruce.setTasa(new BigDecimal(_crucesConTitulo.getValue("tasa")));
						ordenesCruce.setTasa(tasaTitulos);
						ordenesCruce.agregarOrdenTitulo(listaOrdenesTitulos);
						//ordenesCruce.agregarIdBcv(_crucesConTitulo.getValue("ORDENE_ID_BCV")==null?0L:Long.parseLong(_crucesConTitulo.getValue("ORDENE_ID_BCV")));//TODO VALDIDATE VERACIDAD
						ordenesCruce.agregarIdBcv(_crucesConTitulo.getValue("ORDENE_ID_BCV"));//TODO VALDIDATE VERACIDAD
						ordenesCruce.agregarContraparteList(_crucesConTitulo.getValue("CONTRAPARTE"));//TODO VALDIDATE VERACIDAD

						//REINICIO DE VALORES PARA PROCESAMIENTO DE PROXIMA ORDEN							
						montoCruzado=new BigDecimal(0);
						montoTotalTitulosBs=new BigDecimal(0);
						ordenesCruce=new OrdenesCruce();
						tasaTitulos=new BigDecimal(0);
						listaOrdenesTitulos.clear();
					}					
				}
			}																					
		}		
		Logger.info(this,"*************** PROCESO DE CRUCE ORDENES 6");
			getTransaccionesFijas(unidadInversion);			
			Logger.info(this,"*************** PROCESO DE CRUCE ORDENES 7");
			generarOperaciones(movimientosCruces);						
			Logger.info(this,"*************** PROCESO DE CRUCE ORDENES 8");
		//System.out.println("MAPA -----------> " + movimientosCruces);		
		Logger.info(this,"*************** PROCESO DE CRUCE ORDENES 9");
		
		}catch(Throwable ex){
			proceso.agregarDescripcionError("Ocurrio un error general en la ejecucion del proceso de cruce de ordenes");
			Logger.error(this, "Ocurrio un error general en la ejecucion del proceso de cruce de ordenes");
		}
	}
				
	private void generarOperaciones(Map cruces)throws Exception {

		OrdenesCruce ordenCruce=null;
		Orden orden=null;
		
		
		BigDecimal totalCruzado=new BigDecimal(0);		
		Collection<OrdenesCruce>listadoCruces=cruces.values();
		
	
		try {
			
			for (OrdenesCruce element : listadoCruces) {			
					try{
						comisionFijaCobrada=false;
						ordenCruce=element;									
	
						totalCruzado=new BigDecimal(0);
						
						totalCruzado=totalCruzado.add(ordenCruce.getMontoTitulos());					
						totalCruzado=totalCruzado.add(ordenCruce.getMontoDivisas());
						
						if(ordenCruce.getMontoTitulos().compareTo(new BigDecimal(0))>0 && ordenCruce.getMontoDivisas().compareTo(new BigDecimal(0))>0){
							cruceMixto=true;
						}
	
						
						orden=ordenDAO.listarOrden(ordenCruce.getIdOrdenINFI(),false,false,false,true,false);												
	
						
						if(ordenCruce.getEstatus().equals(StatusOrden.NO_CRUZADA)) {	
							//System.out.println(" CONSULTA GENERAL " + ordenCruce.getIdOrdenINFI() + " NO_CRUZADA");
							cruceTipo=StatusOrden.NO_CRUZADA;	
							estatusCruce=StatusOrden.NO_CRUZADA;
						} else if(totalCruzado.doubleValue()==orden.getMonto()) {
							//System.out.println(" CONSULTA GENERAL " + ordenCruce.getIdOrdenINFI() + " CRUZADO_TOTAL");
							cruceTipo=CRUZADO_TOTAL;
							estatusCruce=StatusOrden.CRUZADA;
						} else if(totalCruzado.doubleValue()<orden.getMonto()) {
							//System.out.println(" CONSULTA GENERAL " + ordenCruce.getIdOrdenINFI() + " CRUZADO_PARCIAL");
							cruceTipo=CRUZADO_PARCIAL;
							estatusCruce=StatusOrden.CRUZADA;
						}
						
						//Recorrido de operaciones asociadas a la orden INFI
						for (OrdenOperacion op : orden.getOperacion()) {
							tipoTransaccionFinanciera=op.getTipoTransaccionFinanc();		
						}					
					
						
						//*********ACTUALIZACION DE ORDENES*********				
						//y busqueda de montos de comision				
						actualizarOrdenes(orden, ordenCruce, totalCruzado,estatusCruce);
					
						//Busqueda de % de participacion de titulos y divisas				
	
					
						if(cruceMixto){								
							participacionTitulo=ordenCruce.getMontoTitulos().multiply(new BigDecimal(100)).divide(totalCruzado, 2, RoundingMode.HALF_UP).setScale(2,BigDecimal.ROUND_HALF_EVEN);											
							participacionDivisa=ordenCruce.getMontoDivisas().multiply(new BigDecimal(100)).divide(totalCruzado, 2, RoundingMode.HALF_UP).setScale(2,BigDecimal.ROUND_HALF_EVEN);
							cruceMixto=false;
						}
										
						if(tipoTransaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO) || tipoTransaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO_VERIFICABLE) ||  tipoTransaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)){
																
							if(tipoTransaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)){
								//System.out.println("CREACION DESBLOQUEOS");
								creacionOperacionDesbloqueo(orden,tipoProducto);
							}//FIN SI OPERACION ES BLOQUEO
											
							if(!cruceTipo.equals(NO_CRUZADA)){								
								//Generacion Operaciones DEBITO por DIVISAS
								if(ordenCruce.getMontoDivisas().compareTo(new BigDecimal(0))>0){							
									creacionOperacionDebito(orden,ordenCruce,ConstantesGenerales.CLASIFICACION_PRODUCTO_DIVISA);
								}
								//Generacion Operaciones DEBITO por TITULOS
								if(ordenCruce.getMontoTitulos().compareTo(new BigDecimal(0))>0){								
									creacionOperacionDebito(orden,ordenCruce,ConstantesGenerales.CLASIFICACION_PRODUCTO_TITULO);
								}
								
							}else if(orden.isComisionInvariable()){//Generacion Operacion Debito por Concepto COMISION FIJA
								creacionOperacionDebito(orden,ordenCruce,ConstantesGenerales.CLASIFICACION_PRODUCTO_DIVISA);
							}
					}else if(tipoTransaccionFinanciera.equals(TransaccionFinanciera.DEBITO)){
						if(!cruceTipo.equals(CRUZADO_TOTAL)){
							creacionOperacionCredito(orden,ordenCruce,totalCruzado,tasaComisionVariable);
						}				
					}//FIN ESCENARIO DEBITO
											
					try {
						
						
						Statement s =transaccion.getConnection().createStatement();					
						//System.out.println("**************************************************************");
						for(String query:querysTransacciones){
							//System.out.println(query);
							//System.out.println("/");
							s.addBatch(query);
						}
						++cantidadOrdenesProcesadas;
						++contadorOrdenesProcesadas;
						s.executeBatch();						
						s.close();
						
						if(ConstantesGenerales.COMMIT_REGISTROS_ADJ==cantidadOrdenesProcesadas){
							try {							
								acumuladorOrdenesProcesadas=acumuladorOrdenesProcesadas+cantidadOrdenesProcesadas;
								Logger.info(this,"Ordenes enviadas por COMMIT en proceso de CIERRE CRUCE: " + acumuladorOrdenesProcesadas);
								transaccion.getConnection().commit();							
							}catch(Exception e){
								Logger.error(this,"Eror en el proceso de COMMIT en proceso de CIERRE CRUCE: " + e.getMessage());
								//System.out.println("Eror en el proceso de COMMIT de los registros en Adjudicacion de Subasta Divisas: " + e.getMessage());								
								throw new Exception("He ocurrido en el proceso de realizacion de COMMIT de los registros de CIERRE CRUCE: " + e.getMessage());
							}
							Logger.info(this,"Realizacion de commit al numero de registro N° " + cantidadOrdenesProcesadas);
							cantidadOrdenesProcesadas=0;								
						}
						
					}catch(SQLException sql){
					//System.out.println("Ha ocurrido un error de tipo SQLException " + sql.getMessage());
					querysTransacciones.clear();
					transaccion.getConnection().rollback();
					Logger.error(this,"Ha ocurrido un error de tipo SQLException " + sql.getMessage());
					
					proceso.agregarDescripcionError("Error Orden: "+  ordenCruce.getIdOrdenINFI() + " de tipo SQLException " + sql.getMessage());
						continue;			
					}catch(Exception e){
						//System.out.println("Ha ocurrido un error Inesperado " + e.getMessage());
						querysTransacciones.clear();
						transaccion.getConnection().rollback();
						Logger.error(this,"Ha ocurrido un error Inesperado " + e.getMessage());
						proceso.agregarDescripcionError("Error Inesperado Orden: "+  ordenCruce.getIdOrdenINFI() + e.getMessage());						
						continue;
				}
					querysTransacciones.clear();	
					
					}catch(Exception ex){		
						querysTransacciones.clear();
						ex.printStackTrace();								
						Logger.error(this,"Error procesando la orden - "+ ordenCruce.getIdOrdenINFI()+ " Exception: " + ex.getMessage());
												
						proceso.agregarDescripcionError("Error procesando la orden - "+ ordenCruce.getIdOrdenINFI()+ " Exception: " + ex.getMessage());
						continue;
					}				
			}		
			transaccion.getConnection().commit();			
			cerrarUnidadInversion(Long.parseLong(this.unidadInversion));
		} finally{
					
			if(transaccion.getConnection()!=null){			
				transaccion.getConnection().commit();			
				transaccion.getConnection().close();
		
			}
		
			if(operacionDAO!=null){			
				operacionDAO.cerrarConexion();
			}
		
			if(ordenCLaveNet!=null){
				ordenCLaveNet.cerrarConexion();
			}
			
			if(titulosDAO!=null){
				titulosDAO.cerrarConexion();
			}
			
			if(unidadInvDAO!=null){
				unidadInvDAO.cerrarConexion();
			}
			registrarAuditoria();														
		}
		
	}
	
	private void cerrarUnidadInversion(long idUnidadInv)throws Exception {
		StringBuffer idOrdenesSinProcesar=new StringBuffer();
		
		//Elimina Todos los registros invalidos asociados a la unidad de inversion
		ordenesCrucesDAO.eliminarCrucesInvalidosPorUnidadInv(Long.parseLong(this.unidadInversion));
		
		ordenDAO.listarOrdenesPorUnidadInvStatusOrden(idUnidadInv,StatusOrden.ENVIADA,StatusOrden.PROCESO_CRUCE);
		if(ordenDAO.getDataSet().count()>0){
			ordenDAO.getDataSet().first();
			while(ordenDAO.getDataSet().next()){
				idOrdenesSinProcesar.append(ordenDAO.getDataSet().getValue("ORDENE_ID")+ " - ");				
			}
			Logger.warning(this,"No se puede cerrar la unidad de inversion debido a que la(s) siguiente(s) orden(es) no se han procesado:   " + idOrdenesSinProcesar.toString());
			//System.out.println("No se puede cerrar la unidad de inversion debido a que la(s) siguiente(s) orden(es) no se han procesado:   " + idOrdenesSinProcesar.toString());
		} else {
			unidadInvDAO=new UnidadInversionDAO(_dso);
			if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
				unidadInvDAO.activacionUnidaInv(String.valueOf(idUnidadInv),String.valueOf(ConstantesGenerales.STATUS_INACTIVO));
			}
			
			unidadInvDAO.modificarStatus(idUnidadInv,UnidadInversionConstantes.UISTATUS_CERRADA);
		}
		
	}	
	
	private void actualizarOrdenes(Orden orden,OrdenesCruce ordenCruce,BigDecimal totalCruzado,String estatusCruce)throws Exception{
		String queryDocumentos=null;
		//Actualizacion de fecha valor (ORDENE_PED_FE_VALOR) de la tabla INFI_TB_204_ORDENES
		orden.setFechaValorString(ordenCruce.getFechaValor());
		//Asignacion de fecha valor al campo FechaPactoRecompra para la generacion del DEALS OPICS		
		orden.setFechaPactoRecompra(Utilitario.StringToDate(ordenCruce.getFechaValor(),ConstantesGenerales.FORMATO_FECHA2));//TODO Hacer pruebas para probar con el formato de fecha dd/MM/yyyy 
		
		//NM25287 CAMBIO PARA LA INCLUSION DE LA GENERACION DE DOCUMENTOS
		//Consultar datos del cliente 
		clienteDAO.listarPorId(String.valueOf(orden.getIdCliente()));
		String rifCliente=new String();
		if(clienteDAO.getDataSet().count()>0){	
			clienteDAO.getDataSet().first();
			if(clienteDAO.getDataSet().next()){
				rifCliente=rifCliente.concat(clienteDAO.getDataSet().getValue("TIPPER_ID"));
				rifCliente=rifCliente.concat(clienteDAO.getDataSet().getValue("CLIENT_CEDRIF"));				
				orden.setClienteRifCed(rifCliente);
				orden.setCliente(new Cliente());
				orden.getCliente().setIdCliente(orden.getIdCliente());
				orden.getCliente().setTipoPersona(clienteDAO.getDataSet().getValue("TIPPER_ID"));
				orden.getCliente().setRifCedula(Long.parseLong(clienteDAO.getDataSet().getValue("CLIENT_CEDRIF")));
			}
		}
		//System.out.println("actualizarOrdenes");
		if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){				
			for (OrdenOperacion operacion : orden.getOperacion()) {										
				if(operacion.getInComision()==1){//La operacion es de tipo comision							
					orden.setPoseeComision(true);					
					if(esComisionMontoFijo(operacion.getMontoOperacion().toString(),operacion.getTasa().toString())){//Operacion de comision por monto fijo						
						//poseeComisionInveriable=true;
						orden.setComisionInvariable(true);							
						//Configuracion Comision Monto Fijo
						//Resolucion caso operaciones en cero por comision menor a cero 
						montoComisionInvariable=new BigDecimal(String.valueOf(operacion.getMontoOperacion()));
						//montoComisionInvariable=new BigDecimal(operacion.getMontoOperacion().toBigInteger());//Linea sustituida para solventar incidencia.						
					} else {//Monto comision basado en % de monto adjudicado						
						tasaComisionVariable=operacion.getTasa();//Configuracion de la tasa de la comision en caso de NO ser comision invariable
						//Configuracion de comision Variable
						if(!(totalCruzado.compareTo(new BigDecimal(0))==0)){							
							montoComisionVariable=(totalCruzado.multiply(operacion.getTasa().divide(new BigDecimal(100))));						
							montoComisionVariable=montoComisionVariable.multiply(ordenCruce.getTasa());						
							montoComisionVariable=montoComisionVariable.setScale(2,BigDecimal.ROUND_HALF_EVEN);						
						}											
					}
					break;
				}
			}
			
	//ACTUALIZACION REGISTRO INFI_TB_204_ORDENES									
		if(cruceTipo.equals(StatusOrden.NO_CRUZADA)){
			//System.out.println("ORDEN NO ADJUDICADA"); 
			orden.setFechaAdjudicacion(new Date());
			orden.setMontoAdjudicado(0);			

			orden.setStatus(StatusOrden.NO_CRUZADA);
			if(!orden.isComisionInvariable()){
				orden.setMontoComisionOrden(new BigDecimal(0));
				orden.setMontoTotal(0);
			} else {
				orden.setMontoTotal(orden.getMontoComisionOrden().doubleValue());
			}
		} else if(cruceTipo.equals(CRUZADO_PARCIAL) || cruceTipo.equals(CRUZADO_TOTAL)){
			
			orden.setFechaAdjudicacion(new Date());
			orden.setMontoAdjudicado(totalCruzado.doubleValue());			
			orden.setStatus(StatusOrden.CRUZADA);
			//orden.montoTotal();
			BigDecimal comisionOrden=new BigDecimal(0);
			if(!orden.isComisionInvariable()){
				comisionOrden=totalCruzado.multiply(tasaComisionVariable).divide(new BigDecimal(100));
				//orden.setMontoComisionOrden(comisionOrden.setScale(2,BigDecimal.ROUND_HALF_EVEN));
				orden.setMontoComisionOrden(montoComisionVariable.setScale(2,BigDecimal.ROUND_HALF_EVEN));
			}
			orden.setMontoTotal(totalCruzado.multiply(ordenCruce.getTasa()).add(orden.getMontoComisionOrden()).doubleValue());
		
			//System.out.println("ANTES DE GENERAR DOCUMENTO RED COMERCIAL");
			//NM25287 CAMBIO PARA LA INCLUSION DE LA GENERACION DE DOCUMENTOS
			//NM25287 Inclusión de validación de query de documentos NULL. TTS-466 17/09/2014
			queryDocumentos=generarDocumentoAdjudicacion(orden, this._app);			
			if (queryDocumentos!=null&&queryDocumentos.length()>1) 
			{
				querysTransacciones.add(queryDocumentos);
				//System.out.println("DESPUES DE GENERAR DOCUMENTO RED COMERCIAL");
			}			
		}
		
	}else if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){

		SolicitudClavenet solicitud=new SolicitudClavenet();
		solicitud.setIdOrdenInfi(orden.getIdOrden());
		solicitud.setFechaAdjudicacion(Utilitario.DateToString(new Date(),"dd/MM/yyyy"));
		
		//BUSQUEDA DE INFORMACION DE LA TABLA OPICS SOLICITUD_SITME
		ordenCLaveNet.mostrarSolicitudesClaveNet(solicitud);
		
		if(ordenCLaveNet.getDataSet().count()>0){					
			ordenCLaveNet.getDataSet().first();
			ordenCLaveNet.getDataSet().next();					
			BigDecimal pctComisionClavenet=null;//Porcentaje comision de CLAVENET PERSONAL						
			pctComisionClavenet=new BigDecimal(ordenCLaveNet.getDataSet().getValue("porc_comision"));//PORC_COMISION

			montoComisionInvariable=new BigDecimal(ordenCLaveNet.getDataSet().getValue("monto_comision"));
			if(pctComisionClavenet.equals(new BigDecimal(0)) && !montoComisionInvariable.equals(new BigDecimal(0))){//COMISION INVARIABLE. Si monto porcentaje comision es cero, se trata de comision invariable											
				orden.setComisionInvariable(true);
				orden.setPoseeComision(true);
				
			} else {
				
				orden.setPoseeComision(true);
				
				montoComisionInvariable=new BigDecimal(0);	
				if(!cruceTipo.equals(StatusOrden.NO_CRUZADA)){
					montoComisionVariable=(totalCruzado.multiply(pctComisionClavenet.divide(new BigDecimal(100))));				
					montoComisionVariable=montoComisionVariable.multiply(ordenCruce.getTasa());				
					montoComisionVariable=montoComisionVariable.setScale(2,BigDecimal.ROUND_HALF_EVEN);	
				}
				orden.setMontoComisionOrden(montoComisionVariable);
				
				//montoComisionVariable
				tasaComisionVariable=pctComisionClavenet;						
				orden.setComisionInvariable(false);						
			}
		}

		if(cruceTipo.equals(StatusOrden.NO_CRUZADA)){
			solicitud.setEstatus(ConstantesGenerales.ESTATUS_ORDEN_NO_PACTADA);
			orden.setStatus(StatusOrden.NO_CRUZADA);
			orden.setFechaAdjudicacion(new Date());
			orden.setMontoAdjudicado(0);				
			if(!orden.isComisionInvariable()){
				orden.setMontoComisionOrden(new BigDecimal(0));	
			}
			
			solicitud.setEstatus(ConstantesGenerales.ESTATUS_ORDEN_NO_PACTADA);					
			solicitud.setMontoAdjudicado(new BigDecimal(0));			
			orden.setMontoTotal(orden.getMontoComisionOrden().doubleValue());

		} else if(cruceTipo.equals(CRUZADO_PARCIAL) || cruceTipo.equals(CRUZADO_TOTAL)){

			orden.setStatus(StatusOrden.CRUZADA);
			orden.setFechaAdjudicacion(new Date());
			orden.setMontoAdjudicado(totalCruzado.doubleValue());
			orden.getMontoComisionOrden();
			
			solicitud.setEstatus(ConstantesGenerales.ESTATUS_ORDEN_PACTADA);					
			solicitud.setMontoAdjudicado(totalCruzado);	
			solicitud.setValorEfectivoUSD(totalCruzado);							
			
			//BigDecimal comisionOrden=new BigDecimal(0);
			if(!orden.isComisionInvariable()){

				orden.setMontoComisionOrden(montoComisionVariable.setScale(2,BigDecimal.ROUND_HALF_EVEN));	
			} else {
				orden.setMontoComisionOrden(montoComisionInvariable.setScale(2,BigDecimal.ROUND_HALF_EVEN));
			}
			orden.setMontoTotal(totalCruzado.multiply(ordenCruce.getTasa()).add(orden.getMontoComisionOrden()).doubleValue());				
			registroInstruccionPago(orden,ordenCruce);		
			
			
			//System.out.println("ANTES DE GENERAR DOCUMENTO CLAVENET PERSONAL");
			//NM25287 CAMBIO PARA LA INCLUSION DE LA GENERACION DE DOCUMENTOS
			//NM25287 Inclusión de validación de query de documentos NULL. TTS-466 17/09/2014
			queryDocumentos=generarDocumentoAdjudicacion(orden, this._app);			
			if (queryDocumentos!=null&&queryDocumentos.length()>1) 
			{
				querysTransacciones.add(queryDocumentos);
				//System.out.println("DESPUES DE GENERAR DOCUMENTO CLAVENET PERSONAL");
			}	
		}
		
		actualizarSolicitudesClavenet(solicitud);
	}
		//NM26659 TTS-491 WEB SERVICE ALTO VALOR (Actualizacion de registro de Oferta) - 01/04/2015
		if(simadiAltoValor){
			querysTransacciones.add(ofertaDAO.actualizarEstatusCruceOrdenOferta(ordenCruce.getIdOrdenOferta(),estatusCruce));	
		}
		//**** Actualizacion registro de cruce 
		querysTransacciones.add(ordenesCrucesDAO.actualizarCrucesProcesados(orden.getIdOrden()));
		
		//******Generacion Deals Opics //TODO Terminar de definir Proceso de envio Deals de Opics*********  
		if(!ordenCruce.isOrdenTituloVacio()){/*
			//System.out.println("********* GENERACION DEAL OPICS ***********");
			clienteDAO.listarPorId(String.valueOf(orden.getIdCliente()));
			String rifCliente=new String();
			if(clienteDAO.getDataSet().count()>0){	
				clienteDAO.getDataSet().first();
				if(clienteDAO.getDataSet().next()){
					rifCliente=rifCliente.concat(clienteDAO.getDataSet().getValue("TIPPER_ID"));
					rifCliente=rifCliente.concat(clienteDAO.getDataSet().getValue("CLIENT_CEDRIF"));
					
					orden.setClienteRifCed(rifCliente);
					//orden.setOrdenTitulo(ordenCruce.getOrdenTitulo());
					generarDealsOpics(orden,ordenCruce);
					orden.setOrdenTitulo(ordenCruce.getOrdenTitulo());
				}
			}*/
			//NM25287 CAMBIO PARA LA INCLUSION DE LA GENERACION DE DOCUMENTOS
			generarDealsOpics(orden,ordenCruce);
			orden.setOrdenTitulo(ordenCruce.getOrdenTitulo());
		}

		//**Configuracion de ID ORDENES BCV
		if(!ordenCruce.isListaBcvIdVacio()){
			for (String element : ordenCruce.getListaIdBcv()) {
				ordenDataExt=new OrdenDataExt();			
				ordenDataExt.setIdData(DataExtendida.ORDEN_BCV);
				ordenDataExt.setValor(String.valueOf(element));	
				
				orden.agregarOrdenDataExt(ordenDataExt);
			}
		}
		
		//**Configuracion de Contrapartes
		if(!ordenCruce.isContraparteListVacio()){
			for (String element : ordenCruce.getContraparteList()) {
				ordenDataExt=new OrdenDataExt();			
				ordenDataExt.setIdData(DataExtendida.RIF_CONTRAPARTE);
				ordenDataExt.setValor(element);	
				
				orden.agregarOrdenDataExt(ordenDataExt);
			}
		}
		
		//****MODICICACION DE LA ORDEN
		for (String query : ordenDAO.modificar(orden)) {										
			querysTransacciones.add(query);				
		}
		
		//***ACTUALIZACION DE REGISTRO SOLICITUDES_SITME
		//TODO Falta verificar el estatus de la orden en la tabla SOLICITUDES_SITME para su busqueda y actualizacion  
		
		//*** ACTUALIZACION DE POSICION DE TITULOS EN LA TABLA  
		if(!ordenCruce.isOrdenTituloVacio()){
			for (OrdenTitulo element : ordenCruce.getOrdenTitulo()) {
				querysTransacciones.add(titulosDAO.insertarTitulosOrden(element.getIdOrden(), element));
				//System.out.println("QUE TITULOS --> " + titulosDAO.insertarTitulosOrden(element.getIdOrden(), ordenTitulo));
			}						
		}
		
	}
	
	private void registroInstruccionPago(Orden orden,OrdenesCruce ordenCruce) throws Exception{

		//CONFIGURACION INSTRUCCION PAGO 
		clienteCuentaEntidad=new CuentaCliente();									
		clienteCuentaEntidad.setIdOrden(orden.getIdOrden());
		clienteCuentaEntidad.setClient_id(orden.getIdCliente());
		clienteCuentaEntidad.setCtecta_uso(UsoCuentas.RECOMPRA);
		
		String cuentaAbono=ordenCLaveNet.getDataSet().getValue("cta_abono");  	

		if(cuentaAbono!=null && !cuentaAbono.equals("")){
			//Verifica el tipo de cuenta abono especificado desde clavenet personal
			if(cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO)){//Si especifica cuenta en el extranjero se configura el tipo de instruccion como cuenta Swift
				clienteCuentaEntidad.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
			} else if(cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_NACIONAL)){//Si especifica cuenta nacional en dolares se configura el tipo de instruccion como Cuenta Nacional en Dolares
				clienteCuentaEntidad.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_NACIONAL_DOLARES));	
			}										
		}else {
			//logger.info("Orden: " + idOrdenProcesar +" - No posee especificacion de tipo de operacion (Cta Nacional en Dolares o Cuenta en el Extranjero)");
			////System.out.println("Orden: " + idOrdenProcesar +" - No posee especificacion de tipo de operacion (Cta Nacional en Dolares o Cuenta en el Extranjero)");
		    //throw new Exception("Orden: " + idOrdenProcesar +" - No posee especificacion de tipo de operacion (Cta Nacional en Dolares o Cuenta en el Extranjero)");
		}
		
		String nombreCLienteClavenet=ordenCLaveNet.getDataSet().getValue("NOMBRE_CLIENTE");
		//System.out.println("NOMBRE CLIENTE ---> " + nombreCLienteClavenet);
		if(nombreCLienteClavenet!=null && (!nombreCLienteClavenet.equals(""))){
			nombreCLienteClavenet=nombreCLienteClavenet.trim();
			clienteCuentaEntidad.setCtecta_nombre(nombreCLienteClavenet.toUpperCase());
		}
		
		String nombreBeneficiario=ordenCLaveNet.getDataSet().getValue("NOMBRE_BENEFICIARIO");
		
		if(nombreBeneficiario!=null && (!nombreBeneficiario.equals(""))){
			nombreBeneficiario=nombreBeneficiario.trim();
			//clienteCuentaEntidad.setCtecta_nombre(nombreBeneficiario);
			clienteCuentaEntidad.setNombre_beneficiario(nombreBeneficiario.toUpperCase());
		}
		
		String ctaBanco=ordenCLaveNet.getDataSet().getValue("CTA_BANCO");
		
		if(ctaBanco!=null && (!ctaBanco.equals(""))){
			ctaBanco=ctaBanco.trim();
			clienteCuentaEntidad.setCtecta_bcocta_bco(ctaBanco.toUpperCase());
		}
		
		String cta_numero=ordenCLaveNet.getDataSet().getValue("CTA_NUMERO");
		if(cta_numero!=null && (!cta_numero.equals(""))){
			cta_numero=cta_numero.trim();
			clienteCuentaEntidad.setCtecta_numero(cta_numero);
		}
		
		String bcoDireccion=ordenCLaveNet.getDataSet().getValue("CTA_DIRECCION_C");
		
		if(bcoDireccion!=null && (!bcoDireccion.equals(""))){
			bcoDireccion=bcoDireccion.trim();
			clienteCuentaEntidad.setCtecta_bcocta_direccion(bcoDireccion.toUpperCase());
		}
		
		String bicSwift=ordenCLaveNet.getDataSet().getValue("CTA_BIC_SWIFT");
		
		if(bicSwift!=null && (!bicSwift.equals(""))){
			bicSwift=bicSwift.trim();
			clienteCuentaEntidad.setCtecta_bcocta_swift(bicSwift);
			clienteCuentaEntidad.setCtecta_bcocta_bic(bicSwift.toUpperCase());
		}
		
		String bcoTelefono=ordenCLaveNet.getDataSet().getValue("CTA_TELEFONO_3");
		
		if(bcoTelefono!=null && (!bcoTelefono.equals(""))){
			bcoTelefono=bcoTelefono.trim();
			clienteCuentaEntidad.setCtecta_bcocta_telefono(bcoTelefono);
		}
		
	//Seccion codigo comentada tras incidencias pruebas Desarrollo 14/08/2013 nm26659
	//Validacion de instruccion de pago en proceso liquidacion
	String aba=ordenCLaveNet.getDataSet().getValue("CTA_ABA");
		
		if(aba!=null && (!aba.equals(""))){
			aba=aba.trim();
			clienteCuentaEntidad.setCtecta_bcocta_aba(aba.toUpperCase());
		}
		
		//String observacion="Orden "+orden.getIdOrden()+" Canal Clavenet";
		
		String nombreUnidadInversion=null;
		clienteCuentaEntidad.setCtecta_observacion(nombreUnidadInversion);

		String cedulaBeneficiario=ordenCLaveNet.getDataSet().getValue("CED_RIF_CLIENTE").trim();
		long cedBeneficiario=Long.parseLong(cedulaBeneficiario.substring(1,cedulaBeneficiario.length()-1));																				
		 cedulaBeneficiario=String.valueOf(cedBeneficiario);
		if(cedulaBeneficiario!=null && (!cedulaBeneficiario.equals(""))){
			cedulaBeneficiario=cedulaBeneficiario.trim();											
			clienteCuentaEntidad.setCedrif_beneficiario(cedulaBeneficiario.toUpperCase());
		}
		
		

		String codPaisDestino=ordenCLaveNet.getDataSet().getValue("COD_PAIS_DESTINO");
		if (codPaisDestino!=null && (!codPaisDestino.equals(""))){
			codPaisDestino=codPaisDestino.trim();
			clienteCuentaEntidad.setCodPaisDestino(codPaisDestino.toUpperCase());
		}
		

		String descPaisDestino=ordenCLaveNet.getDataSet().getValue("DESC_PAIS_DESTINO");
		if(descPaisDestino!=null && (!descPaisDestino.equals(""))){
			descPaisDestino=descPaisDestino.trim();
			clienteCuentaEntidad.setDescPaisDestino(descPaisDestino.toUpperCase());
		}

		String codPaisOrigen=ordenCLaveNet.getDataSet().getValue("COD_PAIS_ORIGEN");
		if(codPaisOrigen!=null &&(!codPaisOrigen.equals(""))){
			codPaisOrigen=codPaisOrigen.trim();
			clienteCuentaEntidad.setCodPaisOrigen(codPaisOrigen.toUpperCase());
		}

		String descPaisOrigen=ordenCLaveNet.getDataSet().getValue("DESC_PAIS_ORIGEN");
		if(descPaisOrigen!=null && (!descPaisOrigen.equals(""))){
			descPaisOrigen=descPaisOrigen.trim();
			clienteCuentaEntidad.setDescPaisOrigen(descPaisOrigen.toUpperCase());
		}
		
		String codCiudadOrigen=ordenCLaveNet.getDataSet().getValue("COD_CIUDAD_ORIGEN");
		if(codCiudadOrigen!=null && (!codCiudadOrigen.equals(""))){
			codCiudadOrigen=codCiudadOrigen.trim();
			clienteCuentaEntidad.setCodCiudadOrigen(codCiudadOrigen.toUpperCase());
		}
		
		String descCiudadOrigen=ordenCLaveNet.getDataSet().getValue("DESC_CIUDAD_ORIGEN");
		if(descCiudadOrigen!=null && (!descCiudadOrigen.equals(""))){
			descCiudadOrigen=descCiudadOrigen.trim();
			clienteCuentaEntidad.setDescCiudadOrigen(descCiudadOrigen.toUpperCase());
		}

		String codEstadoOrigen=ordenCLaveNet.getDataSet().getValue("COD_ESTADO_ORIGEN");
		if(codEstadoOrigen!=null && (!codEstadoOrigen.equals(""))){
			codEstadoOrigen=codEstadoOrigen.trim();
			clienteCuentaEntidad.setCodEstadoOrigen(codEstadoOrigen.toUpperCase());
		}
		
		String descEstadoOrigen=ordenCLaveNet.getDataSet().getValue("DESC_ESTADO_ORIGEN");
		if(descEstadoOrigen!=null && (!descEstadoOrigen.equals(""))){
			descEstadoOrigen=descEstadoOrigen.trim();
			clienteCuentaEntidad.setDescEstadoOrigen(descEstadoOrigen.toUpperCase());
		}
		
		//-------Datos del Banco intermediario:----------------------------------------------------------------											
		String bicIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_BIC_SWIFT");										
		
		//Si tiene intermediario
		if(bicIntermediario!=null && (!bicIntermediario.trim().equals(""))){//CONFIGURACION INFORMACION BIC INTERMEDIARIO
			
			bicIntermediario = bicIntermediario.trim();
			clienteCuentaEntidad.setCtecta_bcoint_bic(bicIntermediario.toUpperCase());																						
			String abaIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_ABA");
			if(abaIntermediario!=null && (!abaIntermediario.equals(""))){ 
				abaIntermediario = abaIntermediario.trim();
				clienteCuentaEntidad.setCtecta_bcoint_aba(abaIntermediario.toUpperCase());
			}					
			
			String nombreBancoIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_BANCO");
			if(nombreBancoIntermediario!=null && (!nombreBancoIntermediario.trim().equals(""))){
				nombreBancoIntermediario = nombreBancoIntermediario.trim();
				clienteCuentaEntidad.setCtecta_bcoint_bco(nombreBancoIntermediario.toUpperCase());
			}

			String direccionIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_DIRECCION");
			if(direccionIntermediario!=null && (!direccionIntermediario.trim().equals(""))){
				direccionIntermediario = direccionIntermediario.trim();
				clienteCuentaEntidad.setCtecta_bcoint_direccion(direccionIntermediario.toUpperCase());
			}
			
			String cuentaEnIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_NUMERO");
			if(cuentaEnIntermediario!=null && (!cuentaEnIntermediario.equals(""))){
				cuentaEnIntermediario = cuentaEnIntermediario.trim();
				clienteCuentaEntidad.setCtecta_bcoint_swift(cuentaEnIntermediario.toUpperCase());
			}
			
			String telefonoIntermediario = ordenCLaveNet.getDataSet().getValue("CTA_INT_TELEFONO");
			if(telefonoIntermediario!=null && (!telefonoIntermediario.equals(""))){
				telefonoIntermediario = telefonoIntermediario.trim();
				clienteCuentaEntidad.setCtecta_bcoint_telefono(telefonoIntermediario.toUpperCase());
			}																									
	}//FIN CONFIGURACION DE INTERMEDIARIO
		
		
		String [] clientesCuentasSql=null;											
		clientesCuentasSql=clienteCuentasDAO.insertarClienteCuentas(clienteCuentaEntidad);											
		for(String element : clientesCuentasSql){
			//System.out.println("INSERT DE CLIENTES CUENTAS: " + element);
			querysTransacciones.add(element);												
		}
	}
	
	/**
	 * Metodo de creacion de operaciones de DESBLOQUEO
	 * */
	private void creacionOperacionDesbloqueo(Orden orden,String tipoCanal) throws Exception{	
		//System.out.println("------------ creacionOperacionDesbloqueo ------------");
		if(tipoCanal.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){//CANAL CLAVENET			
			   //*************************GENERACION DE OPERACION DESBLOQUE CAPITAL Y COMISION*************************			
			String numeroRetencion=null;
			BigDecimal montoOperacion=new BigDecimal(0);
			//System.out.println("PRODUCTO CLAVENET ");
			for (OrdenOperacion ordenOperacion: orden.getOperacion()) {
				numeroRetencion=ordenOperacion.getNumeroRetencion();
				montoOperacion=ordenOperacion.getMontoOperacion();
			}			
			//System.out.println("flag 1");
			OrdenOperacion operacionDesbloqueo = new OrdenOperacion();			
			this.vehiculoId=orden.getVehiculoTomador();
			BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.GENERAL_CAPITAL_SIN_IDB_MANEJO_MIXTO,false,TransaccionFinanciera.BLOQUEO);			
			operacionDesbloqueo.setCodigoOperacion(codigo_operacion);			
			operacionDesbloqueo.setNombreOperacion(nombre_operacion);	
			codigo_operacion="";
			nombre_operacion="";
			//System.out.println("flag 2");
			operacionDesbloqueo.setInComision(ConstantesGenerales.FALSO);
			operacionDesbloqueo.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.GENERAL_CAPITAL_SIN_IDB_MANEJO_MIXTO));															
			montoOperacion=montoOperacion.setScale(2,BigDecimal.ROUND_HALF_EVEN);
			operacionDesbloqueo.setMontoOperacion(montoOperacion);
			//System.out.println("flag 3");
			operacionDesbloqueo.setTasa(new BigDecimal(100));
			operacionDesbloqueo.setIdOrden(orden.getIdOrden());
			operacionDesbloqueo.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			//System.out.println("flag 4");
			operacionDesbloqueo.setAplicaReverso(false);			
			if(orden.isComisionInvariable()){//Validacion de comision fija Invariable					
				operacionDesbloqueo.setIndicadorComisionInvariable(ConstantesGenerales.VERDADERO);						
			}else {
				operacionDesbloqueo.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
			}
			//System.out.println("flag 5");
			operacionDesbloqueo.setCuentaOrigen(orden.getCuentaCliente());
			operacionDesbloqueo.setNumeroCuenta(orden.getCuentaCliente());
			operacionDesbloqueo.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);
			operacionDesbloqueo.setNumeroRetencion(numeroRetencion); 			
			operacionDesbloqueo.setTipoTransaccionFinanc(TransaccionFinanciera.DESBLOQUEO);
			//System.out.println("flag 6");
			operacionDesbloqueo.setFechaAplicar(new Date());
			//CREACION QUERY OPERACION DESBLOQUEO SICADII CLAVENET PERSONAL
			querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDesbloqueo,false));
			//System.out.println("flag 7");
			//System.out.println("FINAL OPERACION DESBLOQUEO ");
		}else if(tipoCanal.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){//CANAL RED COMERCIAL	
			//System.out.println("RED COMERCIAL ");
			for (OrdenOperacion operacion : orden.getOperacion()) {					
				OrdenOperacion operacionDesbloqueo = new OrdenOperacion();				
				if(operacion.getInComision()==0){							
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.GENERAL_CAPITAL_SIN_IDB_MANEJO_MIXTO,false,TransaccionFinanciera.BLOQUEO);					 
					operacionDesbloqueo.setCodigoOperacion(codigo_operacion);
					operacionDesbloqueo.setNombreOperacion(nombre_operacion);	
					codigo_operacion="";
					nombre_operacion="";
					operacionDesbloqueo.setInComision(ConstantesGenerales.FALSO);
					operacionDesbloqueo.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.GENERAL_CAPITAL_SIN_IDB_MANEJO_MIXTO));					
					
					//BigDecimal montoDesbloqueoCapital=new BigDecimal(orden.getMontoTotal()).subtract(orden.getMontoComisionOrden());					
					//montoDesbloqueoCapital=montoDesbloqueoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN);
					//operacionDesbloqueo.setMontoOperacion(montoDesbloqueoCapital);
					operacionDesbloqueo.setMontoOperacion(operacion.getMontoOperacion());
					operacionDesbloqueo.setTasa(new BigDecimal(100));
				}else if(operacion.getInComision()==1){
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.GENERAL_COMISION_DEB_MANEJO_MIXTO,false,TransaccionFinanciera.BLOQUEO);
					operacionDesbloqueo.setCodigoOperacion(codigo_operacion);
					operacionDesbloqueo.setNombreOperacion(nombre_operacion);		
					codigo_operacion="";
					nombre_operacion="";
					operacionDesbloqueo.setInComision(ConstantesGenerales.VERDADERO);
					operacionDesbloqueo.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.GENERAL_COMISION_DEB_MANEJO_MIXTO));
					operacionDesbloqueo.setTasa(operacion.getTasa());
					//operacionDesbloqueo.setMontoOperacion(orden.getMontoComisionOrden());
					operacionDesbloqueo.setMontoOperacion(operacion.getMontoOperacion());
					
				}			
							
				operacionDesbloqueo.setIdOrden(orden.getIdOrden());
				operacionDesbloqueo.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
				operacionDesbloqueo.setAplicaReverso(false);				
				if(orden.isComisionInvariable()){//Validacion de comision fija Invariable					
					operacionDesbloqueo.setIndicadorComisionInvariable(ConstantesGenerales.VERDADERO);							
				}else {
					operacionDesbloqueo.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
				}							
				operacionDesbloqueo.setCuentaOrigen(orden.getCuentaCliente());
				operacionDesbloqueo.setNumeroCuenta(orden.getCuentaCliente());
				operacionDesbloqueo.setIdMoneda(operacion.getIdMoneda());
				operacionDesbloqueo.setNumeroRetencion(operacion.getNumeroRetencion()); 							
				operacionDesbloqueo.setTipoTransaccionFinanc(TransaccionFinanciera.DESBLOQUEO);																			
				operacionDesbloqueo.setFechaAplicar(new Date());				
				//CREACION QUERY OPERACION DESBLOQUEO SICADII RED COMERCIAL
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDesbloqueo,false));
			}
			/*for (String s  : querysTransacciones) {//TODO BORRAR SECCION DE CODIGO DESPUES DE LAS PRUEBAS
				//System.out.println("OPERACION DESBLOQUEO ---> " + s);
			}*/
		}
	}
	
	/**
	 * Metodo de creacion operaciones de DEBITO
	 * */
	private void creacionOperacionDebito(Orden orden,OrdenesCruce cruce,String clasificacionProducto) throws Exception{
		codigo_operacion="";
		nombre_operacion="";
		
		//System.out.println("TIPO CRUCE ---> " + cruceTipo);
					if(!cruceTipo.equals(NO_CRUZADA)){
						//System.out.println("ORDEN CRUZADA");
						//CREACION OPERACION CAPITAL
						OrdenOperacion operacionDebitoCapital = new OrdenOperacion();
						
						operacionDebitoCapital.setIdOrden(orden.getIdOrden());					
						operacionDebitoCapital.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);					
						 															
																											
						operacionDebitoCapital.setAplicaReverso(false);
						operacionDebitoCapital.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
						operacionDebitoCapital.setTasa(new BigDecimal(100));			
										
						operacionDebitoCapital.setCuentaOrigen(orden.getCuentaCliente());				
						operacionDebitoCapital.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);				
						//operacionDebitoCapital.setInComision(ConstantesGenerales.VERDADERO);				
						operacionDebitoCapital.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);											
						operacionDebitoCapital.setFechaAplicar(new Date());

						//CREACION OPERACION COMISION
						OrdenOperacion operacionDebitoComision = null;
						
						if(orden.isPoseeComision()){							
							operacionDebitoComision = new OrdenOperacion();
							
							operacionDebitoComision.setIdOrden(orden.getIdOrden());					
							operacionDebitoComision.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);					
							
							operacionDebitoComision.setAplicaReverso(false);								
							if(orden.isComisionInvariable()) {
								operacionDebitoComision.setIndicadorComisionInvariable(ConstantesGenerales.VERDADERO);
								operacionDebitoComision.setTasa(new BigDecimal(0));
							} else {
								operacionDebitoComision.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
								//operacionDebitoComisionTitulos.setTasa(tasaComisionVariable);
							}				
											
							operacionDebitoComision.setCuentaOrigen(orden.getCuentaCliente());				
							operacionDebitoComision.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);				
							operacionDebitoComision.setInComision(ConstantesGenerales.VERDADERO);				
							operacionDebitoComision.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);			
																		
							operacionDebitoComision.setFechaAplicar(new Date());	
						}
						
						if(clasificacionProducto.equals(ConstantesGenerales.CLASIFICACION_PRODUCTO_TITULO)){						
							//CODIGOS DE OPERACION PARA TITULOS
							//System.out.println("CONFIGURACION TITULOS");
							
							operacionDebitoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB_TITULOS));
							BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.CAPITAL_SIN_IDB_TITULOS,false,TransaccionFinanciera.DEBITO);
							//System.out.println("POST CODIGO OPERACION");
							operacionDebitoCapital.setCodigoOperacion(codigo_operacion);
							operacionDebitoCapital.setNombreOperacion(nombre_operacion);
							codigo_operacion="";
							nombre_operacion="";
							
							/*BigDecimal montoCapital=new BigDecimal(0);
							montoCapital=cruce.getMontoTitulos().multiply(new BigDecimal(cruce.getTasa()));*/
							//System.out.println("Configuracion Operacion Capital");
							//System.out.println("cruce.getMontoTotalTitulosBs() ----> " + cruce.getMontoTotalTitulosBs());
							operacionDebitoCapital.setMontoOperacion(cruce.getMontoTotalTitulosBs().setScale(2,BigDecimal.ROUND_HALF_EVEN)); 
							//System.out.println("Operacion Capital Lista");
							//****** CREACION OPERACION DEBITO CAPITAL POR CONCEPTO TITULO ******//
							querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoCapital,false));
							
							//OPERACION DEBITO POR COMISION
							if(orden.isPoseeComision()){								
								if(orden.isComisionInvariable() && !comisionFijaCobrada){
									comisionFijaCobrada=true;
									//BUSQUEDA DE NUEVO CODIGO DE OPERACION DE COMISION FIJA
									operacionDebitoComision.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO));
									BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO,false,TransaccionFinanciera.DEBITO);
									operacionDebitoComision.setCodigoOperacion(codigo_operacion);
									operacionDebitoComision.setNombreOperacion(nombre_operacion);
									codigo_operacion="";
									nombre_operacion="";
									
									//MONTO COMISION INVARIABLE
									operacionDebitoComision.setMontoOperacion(montoComisionInvariable);
									//****** CREACION OPERACION DEBITO COMISION INVARIABLE ******//
									querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComision,false));
								}else if (!orden.isComisionInvariable()){								
									BigDecimal montoComisionTitulo=new BigDecimal(0);
									operacionDebitoComision.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COMISION_DEB_TITULOS));
									BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COMISION_DEB_TITULOS,false,TransaccionFinanciera.DEBITO);
									operacionDebitoComision.setCodigoOperacion(codigo_operacion);
									operacionDebitoComision.setNombreOperacion(nombre_operacion);
									codigo_operacion="";
									nombre_operacion="";
									//System.out.println("configuracion comision variable de titulo");
									montoComisionTitulo=operacionDebitoCapital.getMontoOperacion().multiply(tasaComisionVariable).divide(new BigDecimal(100));
									operacionDebitoComision.setMontoOperacion(montoComisionTitulo.setScale(2,BigDecimal.ROUND_HALF_EVEN));
									//System.out.println("post configuracion ");
									//operacionDebitoComision.setMontoOperacion(montoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN)); 

									//****** CREACION OPERACION DEBITO COMISION POR CONCEPTO DE TITULO ******//
									querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComision,false));
								}
							}
						}else if(clasificacionProducto.equals(ConstantesGenerales.CLASIFICACION_PRODUCTO_DIVISA)){
							//System.out.println("OPERACION DIVISA ");
							operacionDebitoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB_DIVISAS));
							BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.CAPITAL_SIN_IDB_DIVISAS,false,TransaccionFinanciera.DEBITO);
							operacionDebitoCapital.setCodigoOperacion(codigo_operacion);
							operacionDebitoCapital.setNombreOperacion(nombre_operacion);
							codigo_operacion="";
							nombre_operacion="";							

							BigDecimal montoCapital=new BigDecimal(0);
							/*//System.out.println(" ****************** ORDEN ******************  " + cruce.getIdOrdenINFI());
							//System.out.println("MONTO CRUZADO DIVISAS ---> " + cruce.getMontoDivisas());
							//System.out.println("TASA DIVISAS ---> " + cruce.getTasa());
							//System.out.println("MONTO CAPITAL RECALCULADO ----> " + cruce.getMontoTotalDivisasBs());*/
							//montoCapital=cruce.getMontoDivisas().multiply(cruce.getTasa());
							operacionDebitoCapital.setMontoOperacion(cruce.getMontoTotalDivisasBs().setScale(2,BigDecimal.ROUND_HALF_EVEN));
							//****** CREACION OPERACION DEBITO CAPITAL POR CONCEPTO DIVISA ******//
							querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoCapital,false));
							//System.out.println("OPERACION DEBITO DIVISA " + operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoCapital,false));
							//OPERACION DEBITO POR COMISION
							
							if(orden.isPoseeComision()){
								if(orden.isComisionInvariable() && !comisionFijaCobrada){//COMISION FIJA
									comisionFijaCobrada=true;
									//BUSQUEDA DE NUEVO CODIGO DE OPERACION DE COMISION FIJA
									operacionDebitoComision.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO));
									BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO,false,TransaccionFinanciera.DEBITO);
									operacionDebitoComision.setCodigoOperacion(codigo_operacion);
									operacionDebitoComision.setNombreOperacion(nombre_operacion);
									codigo_operacion="";
									nombre_operacion="";
									//MONTO COMISION INVARIABLE
									operacionDebitoComision.setMontoOperacion(montoComisionInvariable);
									//****** CREACION OPERACION DEBITO COMISION INVARIABLE ******//
									querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComision,false));
								} else if(!orden.isComisionInvariable()) {//COMISION VARIABLE
									
									BigDecimal montoComisionDivisa=new BigDecimal(0);
									operacionDebitoComision.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COMISION_DEB_DIVISAS));
									BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COMISION_DEB_DIVISAS,false,TransaccionFinanciera.DEBITO);
									operacionDebitoComision.setCodigoOperacion(codigo_operacion);
									operacionDebitoComision.setNombreOperacion(nombre_operacion);
									codigo_operacion="";
									nombre_operacion="";

									montoComisionDivisa=operacionDebitoCapital.getMontoOperacion().multiply(tasaComisionVariable).divide(new BigDecimal(100));
									operacionDebitoComision.setMontoOperacion(montoComisionDivisa.setScale(2,BigDecimal.ROUND_HALF_EVEN));
									
									//****** CREACION OPERACION DEBITO COMISION POR CONCEPTO DE DIVISA ******//
									querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComision,false));
								}
							}
						}
						
				}else if(orden.isComisionInvariable()){	
					
					OrdenOperacion operacionDebitoComisionFija = new OrdenOperacion();					
					//BUSQUEDA POR NUEVO CODIGO DE OPERACION DE COMISIONES FIJAS
					operacionDebitoComisionFija.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO));
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO,false,TransaccionFinanciera.DEBITO);
					operacionDebitoComisionFija.setCodigoOperacion(codigo_operacion);
					operacionDebitoComisionFija.setNombreOperacion(nombre_operacion);
					
					operacionDebitoComisionFija.setIdOrden(orden.getIdOrden());					
					operacionDebitoComisionFija.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);					
					operacionDebitoComisionFija.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO)); 															
					
					//operacionDebitoComisionTitulos.setMontoOperacion(montoComision.setScale(2,BigDecimal.ROUND_HALF_EVEN)); 																						
					operacionDebitoComisionFija.setAplicaReverso(false);								
					if(orden.isComisionInvariable()){
						operacionDebitoComisionFija.setIndicadorComisionInvariable(ConstantesGenerales.VERDADERO);
						operacionDebitoComisionFija.setTasa(new BigDecimal(0));
					}				
									
					operacionDebitoComisionFija.setCuentaOrigen(orden.getCuentaCliente());				
					operacionDebitoComisionFija.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);				
					operacionDebitoComisionFija.setInComision(ConstantesGenerales.VERDADERO);				
					operacionDebitoComisionFija.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);			
					
					operacionDebitoComisionFija.setMontoOperacion(montoComisionInvariable);
					operacionDebitoComisionFija.setFechaAplicar(new Date());		
					//******CREACION DE OPERACION COBRO COMISION INVARIABLE ******//
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComisionFija,false));
					
				}
					
				  /*for (String e : querysTransacciones) {//TODO BORRAR SECCION DE CODIGO DESPUES DE LAS PRUEBAS 
						//System.out.println("QUERY DEBITO " + e);
					}*/
	}
	
	/**
	 * Metodo de creacion operaciones de CREDITO
	 * */
	private void creacionOperacionCredito(Orden orden,OrdenesCruce cruce,BigDecimal totalCruzado,BigDecimal porcenComision) throws Exception{
		
		BigDecimal remanente=new BigDecimal(0);
		BigDecimal remanenteBs=new BigDecimal(0);
		
		BigDecimal montoReintegroCapTitulo=new BigDecimal(0);
		BigDecimal montoReintegroComTitulo=new BigDecimal(0);
		
		BigDecimal montoReintegroCapDivisas=new BigDecimal(0);
		BigDecimal montoReintegroComDivisas=new BigDecimal(0);
				
		
		OrdenOperacion operacionCreditoCapital=new OrdenOperacion();		
		
		operacionCreditoCapital.setIdOrden(orden.getIdOrden());					
		operacionCreditoCapital.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);					
		 															
																							
		operacionCreditoCapital.setAplicaReverso(false);
		operacionCreditoCapital.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
		operacionCreditoCapital.setTasa(new BigDecimal(100));			
						
		operacionCreditoCapital.setCuentaOrigen(orden.getCuentaCliente());				
		operacionCreditoCapital.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);				
		//operacionDebitoCapital.setInComision(ConstantesGenerales.VERDADERO);				
		operacionCreditoCapital.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);											
		operacionCreditoCapital.setFechaAplicar(new Date());
		
		//CREACION DE OPERACION DEBITO COMISION
		OrdenOperacion operacionCreditoComision=null;
		
		if(orden.isPoseeComision() && !orden.isComisionInvariable()){
			
			operacionCreditoComision=new OrdenOperacion();					
			operacionCreditoComision.setIdOrden(orden.getIdOrden());					
			operacionCreditoComision.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);					
			operacionCreditoComision.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_COMISION)); 																																											
			operacionCreditoComision.setAplicaReverso(false);
			
			if(orden.isComisionInvariable()){
				operacionCreditoComision.setIndicadorComisionInvariable(ConstantesGenerales.VERDADERO);
				operacionCreditoComision.setTasa(new BigDecimal(0));
			}else {
				operacionCreditoComision.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
				//operacionDebitoComisionTitulos.setTasa(tasaComisionVariable);
			}										
			operacionCreditoComision.setCuentaOrigen(orden.getCuentaCliente());				
			operacionCreditoComision.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);				
			operacionCreditoComision.setInComision(ConstantesGenerales.VERDADERO);				
			operacionCreditoComision.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);															
			operacionCreditoComision.setFechaAplicar(new Date());	
		}
			
		
		if(cruceTipo.equals(StatusOrden.NO_CRUZADA)){//DEVOLUCION DEL MONTO TOTAL DE LA OPERACION 
						
			for (OrdenOperacion op : orden.getOperacion()) {				
				if(op.getInComision()!=1){//Generacion de operaciones de credito por Capital
					//BUSQUEDA DE CODIGO OPERACION CAPITAL  
					operacionCreditoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB_DIVISAS));
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.CAPITAL_SIN_IDB_DIVISAS,false,TransaccionFinanciera.CREDITO);
					operacionCreditoCapital.setCodigoOperacion(codigo_operacion);
					operacionCreditoCapital.setNombreOperacion(nombre_operacion);
					codigo_operacion="";
					nombre_operacion="";
					operacionCreditoCapital.setMontoOperacion(op.getMontoOperacion());
					
					//****** CREACION OPERACION CREDITO CAPITAL CONCEPTO DIVISA******//
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoCapital,false));
					
				}else if(op.getInComision()==1){//Generacion de operaciones de credito por Comision
					if(orden.isComisionInvariable()){
						continue;
					}
					
					//BUSQUEDA DE CODIGO OPERACION COMISION 
					operacionCreditoComision.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COMISION_DEB_DIVISAS));
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COMISION_DEB_DIVISAS,false,TransaccionFinanciera.CREDITO);
					operacionCreditoComision.setCodigoOperacion(codigo_operacion);
					operacionCreditoComision.setNombreOperacion(nombre_operacion);
					operacionCreditoComision.setMontoOperacion(op.getMontoOperacion());
					codigo_operacion="";
					nombre_operacion="";
					//****** CREACION OPERACION CREDITO COMISION CONCEPTO DIVISA******//
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoComision,false));
				}
			}		
		} else if(cruceTipo.equals(CRUZADO_PARCIAL)){//DEVOLUCION PARCIAL DEL MONTO SOLICITADO			
			
			remanente=new BigDecimal(orden.getMonto()).subtract(totalCruzado);
			remanenteBs=remanente.multiply(cruce.getTasa());//TODO VERIFICAR TASA 
			
			if(cruceMixto){				
				//************* CREACION DE MONTO POR CAPITAL TITULOS *******************
				montoReintegroCapTitulo=remanenteBs.multiply(participacionTitulo).divide(new BigDecimal(100));
				//montoReintegroCapTitulo=montoReintegroCapTitulo.multiply(new BigDecimal(cruce.getTasa()));

				//BUSQUEDA DE CODIGO OPERACION CAPITAL TITULO  				
				operacionCreditoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB_TITULOS));
				BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.CAPITAL_SIN_IDB_TITULOS,false,TransaccionFinanciera.CREDITO);
				operacionCreditoCapital.setCodigoOperacion(codigo_operacion);
				operacionCreditoCapital.setNombreOperacion(nombre_operacion);
				codigo_operacion="";
				nombre_operacion="";
				operacionCreditoCapital.setMontoOperacion(montoReintegroCapTitulo);			
				
				//****** CREACION OPERACION CREDITO CAPITAL CONCEPTO TITULO******//
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoCapital,false));
				
				//************* CREACION DE MONTO POR CAPITAL DIVISA *******************
				montoReintegroCapDivisas=remanenteBs.multiply(participacionDivisa).divide(new BigDecimal(100));
				//montoReintegroCapDivisas=montoReintegroCapDivisas.multiply(new BigDecimal(cruce.getTasa()));
				//BUSQUEDA DE CODIGO OPERACION CAPITAL DIVSAS  
				operacionCreditoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB_DIVISAS));
				BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.CAPITAL_SIN_IDB_DIVISAS,false,TransaccionFinanciera.CREDITO);
				operacionCreditoCapital.setCodigoOperacion(codigo_operacion);
				operacionCreditoCapital.setNombreOperacion(nombre_operacion);
				codigo_operacion="";
				nombre_operacion="";
				operacionCreditoCapital.setMontoOperacion(montoReintegroCapDivisas);			

				//****** CREACION OPERACION CREDITO CAPITAL CONCEPTO DIVISA******//
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoCapital,false));
				
				//************* COMISION *******************
				if(orden.isPoseeComision() && !orden.isComisionInvariable()){
					//operacionCreditoComision
					//************* CREACION DE MONTO POR COMISION TITULO *******************
					montoReintegroComTitulo=montoReintegroCapTitulo.multiply(porcenComision).divide(new BigDecimal(100));
										
					//BUSQUEDA DE CODIGO OPERACION COMISION TITULO  
					operacionCreditoComision.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COMISION_DEB_TITULOS));
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COMISION_DEB_TITULOS,false,TransaccionFinanciera.CREDITO);
					operacionCreditoComision.setCodigoOperacion(codigo_operacion);
					operacionCreditoComision.setNombreOperacion(nombre_operacion);
					codigo_operacion="";
					nombre_operacion="";
					operacionCreditoComision.setMontoOperacion(montoReintegroComTitulo);			
									
					//****** CREACION OPERACION CREDITO COMISION CONCEPTO TITULO******//
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoComision,false));
					
					//************* CREACION DE MONTO POR COMISION DIVISA *******************
					montoReintegroComDivisas=montoReintegroCapDivisas.multiply(porcenComision).divide(new BigDecimal(100));
										
					//BUSQUEDA DE CODIGO OPERACION COMISION DIVISAS  
					operacionCreditoComision.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COMISION_DEB_DIVISAS));
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COMISION_DEB_DIVISAS,false,TransaccionFinanciera.CREDITO);
					operacionCreditoComision.setCodigoOperacion(codigo_operacion);
					operacionCreditoComision.setNombreOperacion(nombre_operacion);
					codigo_operacion="";
					nombre_operacion="";
					operacionCreditoComision.setMontoOperacion(montoReintegroComDivisas);
					//****** CREACION OPERACION CREDITO COMISION CONCEPTO DIVISA******//
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoComision,false));
				}				
			} else {
				if(cruce.getMontoTitulos().compareTo(new BigDecimal(0))>0){
					//************* CREACION DE CREDITO CAPITAL TITULOS *******************
					//BUSQUEDA DE CODIGO OPERACION CAPITAL TITULO  
					
					operacionCreditoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB_TITULOS));
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.CAPITAL_SIN_IDB_TITULOS,false,TransaccionFinanciera.CREDITO);
					operacionCreditoCapital.setCodigoOperacion(codigo_operacion);
					operacionCreditoCapital.setNombreOperacion(nombre_operacion);
					codigo_operacion="";
					nombre_operacion="";
					operacionCreditoCapital.setMontoOperacion(remanenteBs);	
					
					if(!orden.isComisionInvariable()){						
						montoReintegroComTitulo=remanenteBs.multiply(porcenComision).divide(new BigDecimal(100));
						//BUSQUEDA DE CODIGO OPERACION COMISION TITULO  
						
						operacionCreditoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COMISION_DEB_TITULOS));
						BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COMISION_DEB_TITULOS,false,TransaccionFinanciera.CREDITO);
						operacionCreditoComision.setCodigoOperacion(codigo_operacion);
						operacionCreditoComision.setNombreOperacion(nombre_operacion);
						codigo_operacion="";
						nombre_operacion="";
						operacionCreditoComision.setMontoOperacion(montoReintegroComTitulo);
						//CREACION OPERACION CREDITO COMISION CONCEPTO TITULO/					
						querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoComision,false));
					}
				}else if(cruce.getMontoDivisas().compareTo(new BigDecimal(0))>0) {					
					//************* CREACION DE CREDITO CAPITAL DIVISA *******************
					//BUSQUEDA DE CODIGO OPERACION CAPITAL DIVISA  
					operacionCreditoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.CAPITAL_SIN_IDB_DIVISAS));
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.CAPITAL_SIN_IDB_DIVISAS,false,TransaccionFinanciera.CREDITO);
					operacionCreditoCapital.setCodigoOperacion(codigo_operacion);
					operacionCreditoCapital.setNombreOperacion(nombre_operacion);
					codigo_operacion="";
					nombre_operacion="";
					operacionCreditoCapital.setMontoOperacion(remanenteBs);		
					
					if(!orden.isComisionInvariable()){
						montoReintegroComDivisas=remanenteBs.multiply(porcenComision).divide(new BigDecimal(100));
						//BUSQUEDA DE CODIGO OPERACION COMISION TITULO  
						operacionCreditoCapital.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.COMISION_DEB_DIVISAS));
						BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),TransaccionFija.COMISION_DEB_DIVISAS,false,TransaccionFinanciera.CREDITO);
						operacionCreditoComision.setCodigoOperacion(codigo_operacion);
						operacionCreditoComision.setNombreOperacion(nombre_operacion);
						codigo_operacion="";
						nombre_operacion="";
						operacionCreditoComision.setMontoOperacion(montoReintegroComDivisas);
						//CREACION OPERACION CREDITO COMISION CONCEPTO TITULO/					
						querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCreditoComision,false));
					}
					
				}
			}
		}		
	}
	
   /**
	* Metodo de busqueda de Transacciones Fijas (En tabla INFI_TB_032_TRNF_FIJAS_VEHICU) asociados a una Unidad de inversion 
	* */
	protected void getTransaccionesFijas(String unidadInversion) throws Exception{
		if (transaccionesFijasCache.containsKey(unidadInversion)){
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);
		}else{
			transaccionFijaDAO.listarTransaccionesFijasAdjudicacion(unidadInversion);
			DataSet ds = transaccionFijaDAO.getDataSet();
			transaccionesFijasCache.put(unidadInversion,ds);
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);

		}
	}	
	
	/**
	* Metodo de busqueda de codigos de operacion (En tabla INFI_TB_032_TRNF_FIJAS_VEHICU) asociados a un Vehiculo y una transaccion fija 
	* */
	private void BuscarCodigoyNombreOperacion(String vehiculo_id, int transacionFija, boolean vehiculo, String transaccionFinanciera) throws Exception{
		
		codigo_operacion= "";
		nombre_operacion= "";
		
		//logger.info("* Buscando Codigo y nombre de operacion para el Vehiculo: "+vehiculo_id+", Transaccion Fija: "+transacionFija+", transaccion Financiera: "+transaccionFinanciera);
		//System.out.println("* Buscando Codigo y nombre de operacion para el Vehiculo: "+vehiculo_id+", Transaccion Fija: "+transacionFija+", transaccion Financiera: "+transaccionFinanciera);
		//System.out.println("transaccionesFijasAdjudicacion: "+ transaccionesFijasAdjudicacion.toString());
		if (transaccionesFijasAdjudicacion.count()>0){		
			transaccionesFijasAdjudicacion.first();

			while (transaccionesFijasAdjudicacion.next()){
				if(transaccionesFijasAdjudicacion.getValue("vehicu_id").equals(vehiculo_id) && Integer.parseInt(transaccionesFijasAdjudicacion.getValue("trnfin_id"))==transacionFija){
					
					if(transaccionFinanciera.equals(TransaccionFinanciera.DEBITO)){
		
						if(vehiculo){
							codigo_operacion=transaccionesFijasAdjudicacion.getValue("cod_operacion_veh_deb");
						}else{
		
							codigo_operacion=transaccionesFijasAdjudicacion.getValue("cod_operacion_cte_deb");
						}
					}else if(transaccionFinanciera.equals(TransaccionFinanciera.CREDITO)){
						if(vehiculo){
							codigo_operacion=transaccionesFijasAdjudicacion.getValue("cod_operacion_veh_cre");
						}else{
							codigo_operacion=transaccionesFijasAdjudicacion.getValue("cod_operacion_cte_cre");
						}
					}else if(transaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)){
						
						if(!vehiculo){							
							codigo_operacion=transaccionesFijasAdjudicacion.getValue("cod_operacion_cte_blo");						
						}
					}
					nombre_operacion=transaccionesFijasAdjudicacion.getValue("trnfin_nombre");
					break;
				}
								
			}
			if(nombre_operacion==null || nombre_operacion.equals("") || codigo_operacion==null || codigo_operacion.equals("")){
				    //System.out.println("**************** Codigos Operacion: No se encuentran los codigos de operacion asociados a la transaccion: " + transaccionFinanciera + " Solicitada. ");
					throw new Exception(" Codigos Operacion: No se encuentran los codigos de operacion asociados a la transaccion: " + transaccionFinanciera + " Solicitada. " );
				}
			
		}
		//logger.info("Nombre Operacion: "+nombre_operacion+" codigo operacion: "+codigo_operacion);
	}
	
	/**
	 * Metodo para la actualizacion de las solicitudes clavenet personal
	 * */
	private void actualizarSolicitudesClavenet(SolicitudClavenet ordenClavenet) throws Exception{
		
		try {
			ordenCLaveNet= new OrdenDAO(_dso);
			//logger.info("Actualizacion de la solicitud Clavenet Personal en la tabla SOLICITUDES_SITME ");
			//System.out.println("Actualizacion de la solicitud Clavenet Personal en la tabla SOLICITUDES_SITME ");
			querysTransacciones.add(ordenCLaveNet.actualizarSolicitudClavenet(ordenClavenet,StatusOrden.RECIBIDA));
								
		}catch(Exception e){
			//System.out.println("Se ha generado un error en el proceso de  actualizarOrdenesOPICS" + e.getMessage());
			//logger.error("Se ha generado un error en el proceso de actualizarOrdenesOPICS " + e.getMessage());
			throw new Exception("Se ha generado un error en el proceso de actualizarOrdenesOPICS " + e.getMessage());
		}
	}
	
	private boolean esComisionMontoFijo(String montoComision, String tasaComision){
		//si la tasa de comision es 0 pero el monto es mayor a 0, corresponde a comision de monto fijo
		if(Double.parseDouble(tasaComision)==0 && Double.parseDouble(montoComision)>0){
			return true;
		}		
		return false;
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
	
	/*
	 * Metodo de Generacion de Registro para el envio de los Deals de Opics
	 * */
	private void generarDealsOpics(Orden orden,OrdenesCruce cruce) throws Exception{
		
		UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);
		int idUserSession = Integer.parseInt((usuarioDAO.idUserSession(nmUsuario)));	
		IngresoOpicsSitme ingresoOpics = new IngresoOpicsSitme(_dso, _app,idUserSession, ip, nmUsuario);
		StringBuffer sql=new StringBuffer();
		
		ArrayList<String> querys=new ArrayList<String>();
		querys=ingresoOpics.rentaFijaSICAD2(orden,cruce);
																											
		for(int z=0;z<querys.size();z++){		
			//System.out.println("DEAL OPICS ---- > " + querys.get(z).toString());
			querysTransacciones.add(querys.get(z).toString());						
		}
	}
	/**
	 * Registra la auditoria del proceso de carga
	 * @throws Exception 
	 * @throws Exception 
	 */
	private void registrarAuditoria() {
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(_dso);
		ArrayList<String> querys=new ArrayList<String>();
		AuditoriaDAO auditoriaDAO = new AuditoriaDAO(_dso);
		
		Logger.info(this,"Registrando auditoria del proceso de carrga de cruces...");
			try {
				///********REGISTRAR LA AUDITORIA DE LA PETICIÓN DE LLAMADA AL REPROCESO DEL CIERRE DEL SISTEMA****///
				//Configuracion del objeto para el proceso de auditoria
				Auditoria auditoria= new Auditoria();
				auditoria.setDireccionIp(ip);			
				auditoria.setFechaAuditoria(Utilitario.DateToString(new Date(),ConstantesGenerales.FORMATO_FECHA));
				auditoria.setUsuario(nmUsuario);			
				auditoria.setPeticion(proceso.getTransaId());
				auditoria.setDetalle(proceso.getDescripcionError()==null?"":proceso.getDescripcionError());	
				
				querys.add(auditoriaDAO.insertRegistroAuditoria(auditoria));
				auditoriaDAO.ejecutarStatementsBatch(querys);				
				
			}catch(Exception ex){
				try {
					transaccion.rollback();
				} catch (Exception e) {
					Logger.error(this, "Ha ocurrido un error registrando la auditoría del proceso de carga de cruces : " + ex.getMessage());
				}
			}finally{	
				try {
					if(transaccion.getConnection()!=null){
						transaccion.getConnection().close();
					}
				} catch (Exception e) {
					Logger.error(this, "Ha ocurrido un error registrando la auditoría del proceso de carga de cruces : " + e.getMessage());
				}				
			}	
	}
	
	public String generarDocumentoAdjudicacion(Orden orden, ServletContext contexto) throws Exception{
					
		Orden copiaOrden=(Orden)orden.clone();	
		ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(_dso);

		copiaOrden.setIdTransaccion(TransaccionNegocio.ADJUDICACION);										
		copiaOrden.setMonto(copiaOrden.getMonto());
		copiaOrden.setMontoAdjudicado(copiaOrden.getMontoAdjudicado());
		copiaOrden.eliminarDocumento();// eliminamos los documentos que trae previamente ya que no nos interesa guardarlos por no ser de adjudicacion (son los de toma de orden)
		com.bdv.infi.data.Cliente objcliente = new com.bdv.infi.data.Cliente();
		objcliente.setIdCliente(orden.getCliente().getIdCliente());
		objcliente.setTipoPersona(orden.getCliente().getTipoPersona());
		//Busca los documentos asociados a la adjudicación
		procesarDocumentos.procesar(copiaOrden,contexto,ip,objcliente);																					

		//Creacion de documentos del proceso de Adjudicacion										
		return ordenDAO.insertarDocumentosTransaccion(copiaOrden);	
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub		
	}
	
	public void conciliacionRetenciones(){
		try {
			ArchivoRetencionDAO  archivoRetencionDAO=null;
			if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
				
				//VALIDAR SI EXISTEN OPERACIONES SIN CODIGO DE RETENCION
				archivoRetencionDAO=new ArchivoRetencionDAO(_dso);		
				archivoRetencionDAO.obtenerOperacionesSinRetencion("","",Integer.parseInt(unidadInversion),tipoProducto,true);
								
				if (archivoRetencionDAO.getDataSet().count()>0){
					
					Logger.debug("CIERRE DE CRUCE --> CONCILIACION DE RETENCIONES", "Antes de la llamada al Thread ConciliacionRetencionEnvio. vehiculoId:"+this.vehiculoId);
					
					//VALIDAR TIPO DE PRODUCTO
					ConciliacionRetencionEnvio conciliacionRetencionEnvio = new ConciliacionRetencionEnvio(_dso,idUsuario, nmUsuario,Integer.parseInt(unidadInversion));
									
					Thread t = new Thread(conciliacionRetencionEnvio);
					t.start();
					Logger.debug("CIERRE DE CRUCE --> CONCILIACION DE RETENCIONES", "Fin de la llamada al Thread ConciliacionRetencionEnvio");
					
				}else{
					Logger.debug("CIERRE DE CRUCE --> CONCILIACION DE RETENCIONES", "No existen operaciones sin código de retencion asociadas a la unidad de inversion "+unidadInversion);
				}				
		
			}else {
				Logger.debug("CIERRE DE CRUCE --> CONCILIACION DE RETENCIONES", "El proceso de Conciliacion de Cierre no Aplica para unidades de Inversion de tipo SICAD 2 Canal Red Comercial");
			}
		} catch (Exception e) {
			Logger.error("ERROR THREAD CONCILIACION", e.getMessage()+e.getCause());
			e.printStackTrace();
		}
		
	}
}
