package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.logic.interfaz_altair.consult.ManejoDeClientes;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
/**
 * Clase encargada de realizar la liquidacion de la unidad de inversión.
 * @author Edgar Laucho
 */
@SuppressWarnings({"rawtypes","unused","unchecked","deprecation"})
public class LiquidacionUnidadInversion implements Runnable {
	
    /*** Constante PAGO_IN_BCV*/
    private boolean inBCV = true;
    
    /***Id de moneda Local*/
    private String monedaLocal; 
    
    /*** DataSource*/
    private DataSource _dso;

    /*** Contexto de la Aplicación*/
    private ServletContext _app;
    
    /*** Nombre del usuario*/
    private String nombreUsuario;

    /*** Usuario que inicializa el proceso de Liquidación*/
    private int usuario;

    /*** Direccion Ip*/
    private String ip;

    /*** Sucursal*/
    private String sucursal;
    
    /*** Variable de gestion de logs*/
    private Logger logger = Logger.getLogger(LiquidacionUnidadInversion.class);
    
    /*** Dataset Global*/
    //private DataSet _unidad_inversion;
    
	/**NM del usuario*/
	private String nmUsuario;    
    
    /*** Cliente Banco Central Id en INFI*/
    long clienteBCV = 0;
    
    //private int cierreUISITME=0;
    
    boolean cobroEnLinea = false;
    
    protected ClienteCuentasDAO cuentaCustodiaDAO;
    protected MensajeDAO mensajeDAO;
    protected OrdenDAO ordenDAO;
    private DataSet _cuentaCustodia = null;     
    boolean operacionesEnRechazo = false;
    
    private long unidadInversion;
    
    private int numeroGrupo; //Número de grupo a procesar
    
    private int customerNumberBDV; //Número de cliente de la contraparte de BDV
    
    private String instrumentoFinancieroId;
    private String instrumentoFinancieroDescripcion;
    
    public LiquidacionUnidadInversion() {
    }

    /*** Constructor
     * @param DataSource _dso
     * @param int usuario
     * @param ServletContext _app
     */
    public LiquidacionUnidadInversion(DataSource _dso, int usuario,
        ServletContext _app,HttpServletRequest _req, String nmUsuario,long unidadInversion, int numeroGrupo) {
        this._dso = _dso;
        this.usuario = usuario;
        this._app = _app;
        this.ip = _req.getRemoteAddr();
        this.nombreUsuario = _req.getSession().getAttribute("framework.user.principal").toString();
        this.sucursal = _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL).toString();
        this.nmUsuario = nmUsuario;
        this.unidadInversion = unidadInversion;
        this.numeroGrupo = numeroGrupo;
    }
    
    /*** Constructor
     * @param DataSource _dso
     * @param int usuario
     * @param ServletContext _app
     */
    public LiquidacionUnidadInversion(DataSource _dso, int usuario,
        ServletContext _app,String ip,String nombreusuario,String sucursal,String nmUsuario, int numeroGrupo) {
        this._dso = _dso;
        this.usuario = usuario;
        this._app = _app;
        this.ip = ip;
        this.nombreUsuario = nombreusuario;
        this.sucursal =sucursal;
        this.nmUsuario = nmUsuario;
        this.numeroGrupo = numeroGrupo;
    }

    /**
     * <li>Recibe la unidad de inversión y verifica si se puede ejecutar, dependiendo de las fechas 1 y 2 de la Unidad</li>
     * <li>Crea una Orden con el monto total de todas las ordenes involucradas por vehiculo,para que se envie a ALTAIR al BCV</li>
     * <li>Recorre las ordenes y se verifica que no tenga ninguna operacion pendiente por cobrar y de CREDITO en el caso de otro vehículo
     * diferente a BDV (Se coloca Status Liquidada)</li>
     * <li>Se ingresan los titulos en Custodia</li>
     * <li>Genera la Orden de Recompra en caso de haberse pactado (Con NETEO, Sin NETEO)</li>
     * <li>Genera el deal para Renta Fija (Recompra,Cartera Propia)</li>
     * @param long unidadInversion
     * @throws Exception
     */

    public void liquidarUnidadInversion(long unidadInversion)throws Exception, Throwable {
    	this.unidadInversion = unidadInversion;
    	liquidarUnidadInversion();
    }
    
	private void liquidarUnidadInversion()throws Exception, Throwable {
    	
        //Boolean para saber si la operacion de credito al banco central fue procesada con exito
       ordenDAO 					  = new OrdenDAO(_dso);
        ProcesosDAO procesosDAO 			  = new ProcesosDAO(_dso);
        Proceso proceso 				   	  = new Proceso();
        TitulosDAO titulosDAO				  = new TitulosDAO(_dso);
        VehiculoDAO vehiculoDAO 			  = new VehiculoDAO(_dso);
        TomaOrdenDAO tomaOrdenDAO 			  = new TomaOrdenDAO(null, _dso, _app,this.ip);
        int secuenciaProcesos 				  = Integer.parseInt(OrdenDAO.dbGetSequence(_dso,com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
        TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(_dso);
        Date date 							  = new Date();    	
    	Date fechaActual 					  = new Date();
        SimpleDateFormat formato 			  = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_LIQUIDACION);
        SimpleDateFormat formatoFechaValor    = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);
        MonedaDAO monedaDAO 			   	  = new MonedaDAO(_dso);
        monedaLocal 				   	  	  = monedaDAO.listarIdMonedaLocal();
        FactoryAltair factoryAltair 	   	  = new FactoryAltair(_dso, _app,true);
        factoryAltair.ipTerminal			  = this.ip;
        factoryAltair.nombreUsuario			  = this.nombreUsuario;
        String rifBancoCentral 		          = ParametrosDAO.listarParametros(ParametrosSistema.RIF_BCV,_dso);
        ManejoDeClientes manejoDeClientes 	  = new ManejoDeClientes(_dso);
        ClienteDAO clienteDAO 				  = new ClienteDAO(_dso);   
        UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
        boolean diferencia=false;
        IngresoMasivoCustodia ingresoMasivoCustodia = new IngresoMasivoCustodia(_dso);
        cuentaCustodiaDAO = new ClienteCuentasDAO (_dso);
        mensajeDAO = new MensajeDAO(_dso);

        Transaccion transaccion = new Transaccion(_dso);
        Statement statement = null;
        ResultSet _ordenes = null;
        ResultSet _ordenesAdjudicadas = null;

        proceso.setEjecucionId(secuenciaProcesos);
        proceso.setFechaInicio(date);
        proceso.setFechaValor(date);
        proceso.setTransaId(TransaccionNegocio.LIQUIDACION);
        proceso.setUsuarioId(this.usuario);

        String queryProceso = procesosDAO.insertar(proceso);
        db.exec(_dso, queryProceso); 

        logger.info("[Inicio del proceso de Liquidación]..."+new Date() + " grupo " + numeroGrupo);
        logger.info("[Inicio del proceso de Liquidación]..."+new Date());
            
        unidadInversionDAO.listarPorId(unidadInversion);
        if(unidadInversionDAO.getDataSet().count()>0){      
        	unidadInversionDAO.getDataSet().first();
        	unidadInversionDAO.getDataSet().next();
            instrumentoFinancieroId = unidadInversionDAO.getDataSet().getValue("insfin_id");
		    instrumentoFinancieroDescripcion = unidadInversionDAO.getDataSet().getValue("insfin_descripcion");
        }   
        
        /*_unidad_inversion = setearCobroEnLineaDeLaUnidad(unidadInversion);
        _unidad_inversion.first();
        _unidad_inversion.next();*/
        
        //Se valida que la fecha de liquidacion 1 y 2 sea menor a la actual
		Date unidinvFeLiquidacionHora1 = formato.parse(unidadInversionDAO.getDataSet().getValue("fecha1"));
        Date unidinvFeLiquidacionHora2 = formato.parse(unidadInversionDAO.getDataSet().getValue("fecha2"));
        String tipoProducto = unidadInversionDAO.getDataSet().getValue("TIPO_PRODUCTO_ID");
        logger.info("[Se verifica si la unidad de inversion puede ser liquidada]");
        
        try {
          //Esta validacion se hace tambien antes de llamar a este proceso, pero esta aqui para evitar errores en el Quartz LiquidacionScheduler
            if (unidinvFeLiquidacionHora1.compareTo(fechaActual) > 0) {
        		logger.error("Fecha de liquidación 1 es mayor a la fecha actual");
        		throw new Exception("Fecha de liquidación 1 es mayor a la fecha actual");
        	} else if (unidinvFeLiquidacionHora2.compareTo(fechaActual) > 0 && !tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)) {
        		logger.error("Fecha de liquidación 2 es mayor a la fecha actual");
        		throw new Exception("Fecha de liquidación 2 es mayor a la fecha actual");
        	}

    	//Se listan los vehiculos involucrados en la unidad de inversion, para realizar el respectivo credito al BCV
    	String status[] = new String[2];
    	status[0] = StatusOrden.ADJUDICADA;
    	status[1] = StatusOrden.LIQUIDADA;
    	// Primer Query !!!!.
        ordenDAO.listarCreditoPorVehiculo(unidadInversion,status);
        DataSet vehiculoCreditos = ordenDAO.getDataSet();
    	
    	BigDecimal montoCreditoBCV = BigDecimal.ZERO;
    	BigDecimal montoDiferenciaCreditoBCV = BigDecimal.ZERO;
    	BigDecimal sumatoriaMontoOrdenesBVC = BigDecimal.ZERO;
    	BigDecimal MontoOrdenesBVC = BigDecimal.ZERO;
    	
    	if (numeroGrupo == 1){
	    	//Se verifica que el cliente exista en infi, de no existir se busca en Altair y se inserta en Infi
    		obtenerClienteBcv(rifBancoCentral);
    	}

    	/*
    	 * Se recorre por vehiculo, para de esa forma verificar :
    	 * 1)Si existe la operacion financiera creada
    	 * a)APLICADA : no se hace nada
    	 * b)ESPERA o RECHAZADA : Se envia a ALTAIR
    	 * 2)Si la operacion no existe se crea y se agrega a la orden vehículo
    	 */
        //--------------------------------------------- Inicio vehiculoCreditos ----------------------------------
        // Primer Query !!!
    	if(vehiculoCreditos.count()>0 && numeroGrupo == 1){    	
    		vehiculoCreditos.first();
    		while(vehiculoCreditos.next()){
    			
    			//Se consulta el monto de credito comision asociado a la Unidad de inversion 
    	        //TODO si se agrega un nuevo vehiculo se debe revisar este método (listarCreditoPorVehiculoComision)
    	        
    			//ITS-635 
    			//BigDecimal montoCreditoComision = ordenDAO.listarCreditoPorVehiculoComision(unidadInversion);
    			//Busca el monto a depositar    	    			
    			//montoCreditoBCV = new BigDecimal(vehiculoCreditos.getValue("total")).subtract(montoCreditoComision);
    	        montoCreditoBCV = BigDecimal.valueOf(Double.parseDouble(ordenDAO.listarMontoOrdenBCVSubasta(unidadInversion)));
    			
    			
    			logger.info("Monto total Cobrado (total)      : "+vehiculoCreditos.getValue("total"));
    			//logger.info("Monto total Cobrado (comin)      : "+montoCreditoComision);
    			// Total por Capital  . . . . . . .
    			logger.info("Monto total Cobrado (total-comin): "+montoCreditoBCV);    			
    			// Si el Vehiculo tomador en BDV
    			if(vehiculoDAO.vehiculoEsBDV(vehiculoCreditos.getValue("ordene_veh_tom")))
    			{
    				//Buscamos orden perteneciente al Vehiculo BDV para esta unidad de inversion
    				
    				// Segundo Query !!!!
    				ordenDAO.listarOrdenBCV(unidadInversion, TransaccionNegocio.LIQUIDACION,vehiculoCreditos.getValue("ordene_veh_tom"));
    				//Dataset Orden de credito al BCV
    				DataSet _ordenCredito = ordenDAO.getDataSet();
    				
    				//Si existe la operacion de credito se verifica si fue procesada por altair
    				int i=0;
    				// Variable del Segundo Query !!!.
    				if(_ordenCredito.count()>0)
    				{	
    					_ordenCredito.first();
    					//_ordenCredito.next();
    					while(_ordenCredito.next()){
   							if(_ordenCredito.getValue("status_operacion").equals(ConstantesGenerales.STATUS_APLICADA))
   							{
   								logger.info("La operacion financiera (Vehiculo BDV) se encuentra en status aplicada");
   							}else{   								
   								/*if (cobroEnLinea){
   	   								logger.info("La operacion financiera (Vehiculo BDV) existe, se intenta enviar hacia altair");
	   								OrdenOperacion ordenOperacion= new OrdenOperacion();
									ArrayList ordenOperacionArrayList = new ArrayList<OrdenOperacion>();
	        					
	   								//Set de valores
	   								ordenOperacion.setStatusOperacion(_ordenCredito.getValue("status_operacion"));
	   								ordenOperacion.setMontoOperacion(new BigDecimal(_ordenCredito.getValue("monto_operacion")));
	   								ordenOperacion.setNumeroCuenta(_ordenCredito.getValue("ctecta_numero"));
	   								ordenOperacion.setSerialContable(_ordenCredito.getValue("serial"));
	   								ordenOperacion.setCodigoOperacion(_ordenCredito.getValue("codigo_operacion"));
	   								ordenOperacion.setNombreOperacion(_ordenCredito.getValue("operacion_nombre"));
	   								ordenOperacion.setTipoTransaccionFinanc(_ordenCredito.getValue("trnf_tipo"));
	   								ordenOperacion.setIdMoneda(_ordenCredito.getValue("moneda_id")); //
	   								ordenOperacion.setIdOrden(Long.parseLong(_ordenCredito.getValue("ordene_id")));
	   								ordenOperacion.setIdOperacion(Long.parseLong(_ordenCredito.getValue("ordene_operacion_id")));
	   								ordenOperacionArrayList.add(ordenOperacion);
	        		            
	   								if (cobroEnLinea){
		   								//Se intenta enviar hacia altair
	   								    this.enviarAltair(factoryAltair, ordenOperacionArrayList, true);
	   								}
   								}*/
   							}//fin else
   							i=i+1;
   							MontoOrdenesBVC=new BigDecimal(_ordenCredito.getValue("monto_operacion"));
   							logger.info("Monto Depositado al BCV ("+i+"):"+MontoOrdenesBVC);
   							// Ordenes de Depositos al BVC
   							sumatoriaMontoOrdenesBVC=sumatoriaMontoOrdenesBVC.add(MontoOrdenesBVC);
    					}// del while ordenes BCV . . . . .
    					// Orden Cobrada
						logger.info("Monto total de Depositos a BVC: "+sumatoriaMontoOrdenesBVC);
    					
    					montoDiferenciaCreditoBCV=montoCreditoBCV.subtract(sumatoriaMontoOrdenesBVC);
    					if(!montoDiferenciaCreditoBCV.equals(0)){
								diferencia=true;
						}
    				}// del IF _ordenCredito.count()>0
    				
    				// Se crea orden y operacion  . . . . . . . .
    				else{
    					
    					//Transaccion Fija CRE al BCV
        	            com.bdv.infi.data.TransaccionFija transaccionFija = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.DEPOSITO_BCV,vehiculoCreditos.getValue("vehicu_id"),instrumentoFinancieroId);
        	            
        	            //Objetos
        	            Orden orden= new Orden();
        	            OrdenOperacion ordenOperacion = new OrdenOperacion();
        	            ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_BCV));
        	            
        	            //Creamos la orden de credito al BCV, ya que no existe
        	            String sql[] = this.crearOrdenCreditoBCV(unidadInversion, vehiculoCreditos.getValue("ordene_veh_tom"), montoCreditoBCV, vehiculoCreditos.getValue("vehicu_numero_cuenta_bcv"), transaccionFija.getNombreTransaccion(), transaccionFija.getCodigoOperacionCteCre(),0,orden,ordenOperacion,ordenDAO);
        	            logger.info("Se crea Orden Credito BCV");
        	            //	Se ejecuta la transaccion
        	            db.execBatch(_dso, sql);
        	            
        	            // Obtenemos las operaciones
        	            ArrayList ordenOperacionArrayList = new ArrayList<OrdenOperacion>();
    	    	        
        	            //	Recorremos las operaciones para agregarlas al arrayList que se enviara a ALTAIR
        	            ordenOperacionArrayList.add(ordenOperacion);

        	            /*if (cobroEnLinea){
	        	            //	Enviamos la operacion hacia altair
	    	    	        this.enviarAltair(factoryAltair, ordenOperacionArrayList, true);
        	            }*/
    				}
    				
    				//Bloque de codigo no aplicable LIQUIDACION SUBASTA
    				if (instrumentoFinancieroDescripcion.equals("TITULOS_SITME") && diferencia==true && montoDiferenciaCreditoBCV.intValue()>0){
    					//Transaccion Fija CRE al BCV
        	            com.bdv.infi.data.TransaccionFija transaccionFija = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.DEPOSITO_BCV,vehiculoCreditos.getValue("vehicu_id"),instrumentoFinancieroId);
        	            
        	            //Objetos
        	            Orden orden= new Orden();
        	            OrdenOperacion ordenOperacion = new OrdenOperacion();
        	            ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_BCV));
        	            
        	            //Creamos la orden de credito al BCV, ya que no existe
        	            String sql[] = this.crearOrdenCreditoBCV(unidadInversion, vehiculoCreditos.getValue("ordene_veh_tom"), montoDiferenciaCreditoBCV, vehiculoCreditos.getValue("vehicu_numero_cuenta_bcv"), transaccionFija.getNombreTransaccion(), transaccionFija.getCodigoOperacionCteCre(),0,orden,ordenOperacion,ordenDAO);
        	            /*if (instrumentoFinancieroDescripcion.equals("TITULOS_SITME"))
        	            logger.info("Se crea Orden Credito BCV por UI tipo SITME");*/
        	            logger.info("Por monto: "+montoDiferenciaCreditoBCV);
        	            
        	            //Se ejecuta la transaccion
        	            db.execBatch(_dso, sql);
        	            
        	            /*if (cobroEnLinea){
	    	    	        //Obtenemos las operaciones
	    	    	        ArrayList ordenOperacionArrayList = new ArrayList<OrdenOperacion>();
	    	    	        
	    	    	        //recorremos las operaciones para agregarlas al arrayList que se enviara a ALTAIR
	    	    	        ordenOperacionArrayList.add(ordenOperacion);
	
	    	    	        //Enviamos la operacion hacia altair
	    	    	        this.enviarAltair(factoryAltair, ordenOperacionArrayList, true);
        	            }*/
    				} 
    			}//fin vehiculoCreditos.getValue("vehicu_rif").equalsIgnoreCase(rifBancoVenezuela)

    			//Vehiculo diferente a Banco de Venezuela
    			else
    			{
    				//Buscamos orden perteneciente al Vehiculo BDV para esta unidad de inversion
    				ordenDAO.listarOrdenBCV(unidadInversion, TransaccionNegocio.LIQUIDACION,vehiculoCreditos.getValue("ordene_veh_tom"));

    				//Dataset Orden de credito al BCV
    				DataSet _ordenCredito = ordenDAO.getDataSet();
    			
    				//Si existe la operacion de credito se verifica si fue procesada por altair
    				if(_ordenCredito.count()>0)
    				{
    					//validar tambien la orden de debito del vehiculo
    					_ordenCredito.first();_ordenCredito.next();

    					if(_ordenCredito.getValue("status_operacion").equals(ConstantesGenerales.STATUS_APLICADA))
    					{
    						logger.info("La operacion financiera se encuentra en status aplicada");
    						
    					}else{
    						
    						/*if (cobroEnLinea){    						
	    						logger.info("La operacion financiera existe, se intenta enviar hacia altair");
	    						OrdenOperacion ordenOperacion= new OrdenOperacion();
	        					ArrayList ordenOperacionArrayList = new ArrayList<OrdenOperacion>();
	        					
	        					//Set de valores
	    						ordenOperacion.setStatusOperacion(_ordenCredito.getValue("status_operacion"));
	        		            ordenOperacion.setMontoOperacion(new BigDecimal(_ordenCredito.getValue("monto_operacion")));
	        		            ordenOperacion.setNumeroCuenta(_ordenCredito.getValue("ctecta_numero"));
	        		            ordenOperacion.setSerialContable(_ordenCredito.getValue("serial"));
	        		            ordenOperacion.setCodigoOperacion(_ordenCredito.getValue("codigo_operacion"));
	        		            ordenOperacion.setNombreOperacion(_ordenCredito.getValue("operacion_nombre"));
	        		            ordenOperacion.setTipoTransaccionFinanc(_ordenCredito.getValue("trnf_tipo"));
	        		            ordenOperacion.setIdMoneda(_ordenCredito.getValue("moneda_id")); //
	        		            ordenOperacion.setIdOrden(Long.parseLong(_ordenCredito.getValue("ordene_id")));
	        		            ordenOperacion.setIdOperacion(Long.parseLong(_ordenCredito.getValue("ordene_operacion_id")));
	        		            ordenOperacionArrayList.add(ordenOperacion);
	        		            
	        		            //Se intenta enviar hacia altair
	        		            this.enviarAltair(factoryAltair, ordenOperacionArrayList, true);
    						}  */      		            
    					}//fin else
    				}else{
    					
    					try {
    						//ArrayList de consultas a ser ejcutadas en una transaccion
        					ArrayList ejecutarExecBachtArrayList = new ArrayList();
        					
        					//Listar orden existente del vehiculo
    	                	Orden ordenVehiculo = tomaOrdenDAO.crearBuscarOrdenVehiculoUI(vehiculoCreditos.getValue("vehicu_id"), this.ip, unidadInversion, nombreUsuario, 0, this.sucursal);
    	    				
    	                	//Creamos la operacion de DEBITO a la cuenta del vehiculo
    	                    OrdenOperacion ordenOperacionDebito  = new OrdenOperacion();  	 
    	    	            
    	    	            //Transaccion Fija DEBITO A CUENTA DEL VEHICULO
    	    	            com.bdv.infi.data.TransaccionFija transaccionFija = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.DEBITO_CUENTA_VEHICULO,vehiculoCreditos.getValue("vehicu_id"),instrumentoFinancieroId);
    	    	            ArrayList ordenOperacionArrayList = new ArrayList<OrdenOperacion>();
    	    	            
    	    	            //Le agregamos la operacion a la orden para ser enviada a Altair 
    	    	            this.crearOrdenOperacion(ordenOperacionDebito,montoCreditoBCV,vehiculoCreditos.getValue("vehicu_numero_cuenta"),transaccionFija.getNombreTransaccion(),transaccionFija.getCodigoOperacionVehDeb(),TransaccionFinanciera.DEBITO,0);

    	    	            //Se agrega la operación a la orden
    	    	            ordenVehiculo.agregarOperacion(ordenOperacionDebito);
        					
    	    	            //Insertamos la orden creada con la operacion financiera de credito al banco central
    	    	            String[] querys = ordenDAO.modificar(ordenVehiculo);	    	      
    	    	            
    	    	            //recorremos los querys para agregarlos al arrayList
            	            for(int i=0;i<querys.length;i++){
            	            	ejecutarExecBachtArrayList.add(querys[i]);
            	            }
    	    	            
    	    	            logger.info("[SE CREA OPERACION DE CREDITO]--->SE INTENTA ENVIAR HACIA ALTAIR--->ID ORDEN" +ordenVehiculo.getIdOrden());

    	    	            ordenOperacionArrayList = ordenVehiculo.getOperacion();
    	    	            
    	    	            //Recorremos el array para obtener el objeto orden Operacion y setear el id relacion de la operacion
    	    	            OrdenOperacion operacionDebitoVehiculo = new OrdenOperacion();
    	    	            
    	    	            for(int i=0;i<ordenOperacionArrayList.size();i++)
    	    	            {
    	    	            	 operacionDebitoVehiculo = (OrdenOperacion)ordenOperacionArrayList.get(i);
    	    	            }

        					//Transaccion Fija CRE al BCV
            	            transaccionFija = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.DEPOSITO_BCV,vehiculoCreditos.getValue("vehicu_id"),instrumentoFinancieroId);

            	            //Creacion de objetos
            	            Orden orden = new Orden(); 
            	            OrdenOperacion ordenOperacion = new OrdenOperacion();
            	            ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_BCV));
            	            
            	            //Creamos la orden de credito al BCV, ya que no existe
            	            String sqls[] = this.crearOrdenCreditoBCV(unidadInversion, vehiculoCreditos.getValue("ordene_veh_tom"), montoCreditoBCV, vehiculoCreditos.getValue("vehicu_numero_cuenta_bcv"), transaccionFija.getNombreTransaccion(), transaccionFija.getCodigoOperacionCteCre(),operacionDebitoVehiculo.getIdOperacion(),orden,ordenOperacion,ordenDAO);
            	            
            	            //Obtenemos las operaciones
        	    	        ArrayList ordenOperacionArrayListCre = new ArrayList<OrdenOperacion>();
        	    	        
            	            //recorremos las operaciones para agregarlas al arrayList que se enviara a ALTAIR
        	    	        ordenOperacionArrayListCre.add(ordenOperacion);
        	    	        
            	            //recorremos los querys para agregarlos al arrayList
            	            for(int i=0;i<sqls.length;i++){
            	            	ejecutarExecBachtArrayList.add(sqls[i]);
            	            }
            	            
            	            //Ejecutamos el execbach
            	            db.execBatch(_dso,(String[]) ejecutarExecBachtArrayList.toArray(new String[ejecutarExecBachtArrayList.size()]));
            	            
            	            /*if (cobroEnLinea){
	            	            //Orden  Vehiculo Debito
	            	            this.enviarAltair(factoryAltair, ordenOperacionArrayList,true);
	        	    	        
	        	    	        //Enviamos la operacion hacia altair
	        	    	        this.enviarAltair(factoryAltair, ordenOperacionArrayListCre, true);
            	            } */           	                       	            
						} catch (Exception e) {
							logger.error("Ha ocurrido un problema al crear la orden de credito para el vehiculo "+vehiculoCreditos.getValue("vehicu_id")+e.getMessage(),e);
						}    					
    				}//fin else
    			}//fin else
    		}//fin while
    	}// FIN DE  vehiculoCreditos.count()>0

        //buscamos todas las ordenes de la unidad de inversión con status adjudicada
        logger.info("[SE BUSCAN TODAS LAS ORDENES EN STATUS ADJUDICADAS PARA LA UNIDAD DE INVERSION]--->"+unidadInversion);
   
        //DataSet _ordenes = ordenDAO.getDataSet();
        transaccion.begin();
        statement = transaccion.getConnection().createStatement();
        _ordenes = statement.executeQuery(ordenDAO.listarOrdenesPorUnidadInversion(unidadInversion,StatusOrden.ADJUDICADA,false,numeroGrupo));
        int cantidadOrdenes = 0;
        
        /*
         * Mientras el resultset contenga datos se actualiza el status de la orden a liquidada, si no tiene ningun monto pendiente
         * por aplicar,se ingresan los titulos a custodia y se verifican si los titulos poseen pacto de recompra
         */
           while (_ordenes.next()) {
        	   
        	    cantidadOrdenes++;
        	    
            	operacionesEnRechazo = false;
                logger.info("[PROCESANDO ORDEN]---> " + _ordenes.getString("ordene_id"));
                
                //Si a la orden le fue ADJUDICADO (CERO) , solo se liquida la unidad de inversion
                if (_ordenes.getDouble("ordene_ped_total")==0){
                	
                	logger.info("[LA ORDEN TIENE MONTO ADJUDICADO DE 0]");
                	//update infi_tb_204_ordenes set ejecucion_id
                    String sqlAdjudicado = ordenDAO.liquidarOrden(secuenciaProcesos,StatusOrden.LIQUIDADA,_ordenes.getLong("ordene_id"));
                    try {
						//Ejecutamos en una transacción
                    	db.exec(_dso, sqlAdjudicado); 
                    	logger.info("[ORDEN LIQUIDADA...]");
					} catch (Exception e) {
						logger.error("[LA ORDEN PRESENTO INCONVENIENTES PARA INTENTAR SER LIQUIDADA]--->"+e.getMessage(),e);
					}
					//Se continua con el proceso
                    continue;
                }// FIN Si a la orden le fue adjudicado 0, solo se liquida la unidad de inversion
                
                // -----------  Inicio de generacion de Deal cuando es cartera propia -----------------
                //Si la orden es de cartera propia, solo generamos el DEAL y liquidamos la orden
                if(_ordenes.getString("transa_id").equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)){
                	logger.info("[ORDEN DE CARTERA PROPIA]--->"+"[VEHICULO TOMADOR]--->"+_ordenes.getString("ordene_veh_tom"));

                     //Generacion de los deal tickets por titulo
                    IngresoOpics ingresoOpics = new IngresoOpics(_dso, _app,usuario, ip, this.nombreUsuario);
                    Orden ordenCarteraPropia = new Orden();
                    
                    ordenCarteraPropia.setIdCliente(_ordenes.getLong("client_id"));
                    ordenCarteraPropia.setFechaValor(formatoFechaValor.parse(_ordenes.getString("ordene_ped_fe_valor")));
                    ordenCarteraPropia.setFechaOrden(formatoFechaValor.parse(_ordenes.getString("ordene_ped_fe_orden")));
                    ordenCarteraPropia.setIdOrden(_ordenes.getLong("ordene_id"));
                    ordenCarteraPropia.setVehiculoTomador(_ordenes.getString("ordene_veh_tom"));
                    ordenCarteraPropia.setIdTransaccion(_ordenes.getString("transa_id"));
                    ordenCarteraPropia.setPrecioCompra(Double.parseDouble(_ordenes.getString("ordene_ped_precio")));
                    
                    //Listamos los titulos asociados a una orden
                    titulosDAO.listarDatosTituloOrden(_ordenes.getString("ordene_id"));

                    
                    if(titulosDAO.getDataSet().count()>0)
                    {
                    	titulosDAO.getDataSet().first();
                    	while(titulosDAO.getDataSet().next())
                    	{
                    		OrdenTitulo ordenTitulo = new OrdenTitulo();
                    		ordenTitulo.setTituloId(titulosDAO.getDataSet().getValue("TITULO_ID"));
                    		ordenTitulo.setUnidades(Double.parseDouble(titulosDAO.getDataSet().getValue("TITULO_UNIDADES")));
                    		ordenTitulo.setPorcentajeRecompra(Double.parseDouble(titulosDAO.getDataSet().getValue("TITULO_PCT_RECOMPRA")));
                    		
                    		//Agregamos el titulo a la orden
                    		ordenCarteraPropia.agregarOrdenTitulo(ordenTitulo);
                    	}//fin while
                    }//fin if
                   
                    ArrayList<String> sqlsMensajesOpics = ingresoOpics.rentaFija(ordenCarteraPropia);
                    
                    //Agregamos el query de liquidar la orden
                    sqlsMensajesOpics.add(ordenDAO.liquidarOrden(secuenciaProcesos,StatusOrden.LIQUIDADA,_ordenes.getLong("ordene_id")));
                    
                    try {
                    	
                    	db.execBatch(_dso,(String[]) sqlsMensajesOpics.toArray(new String[sqlsMensajesOpics.size()]));
                    	logger.info("[SE GENERA DEAL TICKET Y SE LIQUIDA LA ORDEN]--->LOS TITULOS NO ENTRAN A CUSTODIA POR INFI");
                    } catch (Exception e) {
						logger.error("[LA ORDEN DE CARTERA PROPIA PRESENTO INCONVENIENTES PARA SER LIQUIDADA]--->"+e.getMessage(),e);
					}
                     
                    //Se continua con la siguiente orden
                    continue;
                }// FIN de generacion de Deal cuando es CARTERA PROPIA . .  . . . . . .

                
                // SE INICIA RECORRIDO DE ORDENES Y OPERACIONES FUERA DE CARTERRA PROPIA . . . . . . . .
                ArrayList<OrdenOperacion> operacionesFinancieras = new ArrayList<OrdenOperacion>();
                Orden ordenRecompra = new Orden();

                //Se verifica por orden las operaciones financieras
                //Lista las operaciones rechazadas o en espera por liquidacion para dicho proceso
                ordenDAO.listarOperacionesLiquidacion(_ordenes.getLong("ordene_id"));

                DataSet _operaciones = ordenDAO.getDataSet();
                // Si la orden tiene operaciones pendientes . . . . . .
                if (_operaciones.count() > 0) {
                    /*if (cobroEnLinea){
	                    _operaciones.first();
	                    logger.info("[RECORRIENDO OPERACIONES FINANCIERAS PENDIENTES DE LA ORDEN: "+_ordenes.getString("ordene_id")+"]");
	                    while (_operaciones.next()) {
	                        logger.info("[OPERACION ID]--->" +_operaciones.getValue("ordene_operacion_id"));
	                        OrdenOperacion ordenOperaciones = new OrdenOperacion();
	                        ordenOperaciones.setIdOperacion(Long.parseLong(_operaciones.getValue("ordene_operacion_id")));
	                        ordenOperaciones.setIdOrden(Long.parseLong(_operaciones.getValue("ordene_id")));
	                        ordenOperaciones.setTipoTransaccionFinanc(_operaciones.getValue("trnf_tipo"));
	                        ordenOperaciones.setNumeroCuenta(_operaciones.getValue("ctecta_numero"));
	                        ordenOperaciones.setSerialContable(_operaciones.getValue("serial"));
	                        ordenOperaciones.setMontoOperacion(((_operaciones.getValue("monto_operacion") != null) &&!_operaciones.getValue("monto_operacion").equals(""))? new BigDecimal(_operaciones.getValue("monto_operacion")) : new BigDecimal(0));
	                        ordenOperaciones.setIdMoneda(_operaciones.getValue("moneda_id"));
	                        ordenOperaciones.setCodigoOperacion(_operaciones.getValue("codigo_operacion"));
	                        ordenOperaciones.setCentroContable(_ordenes.getString("ordene_usr_cen_contable"));
	                        ordenOperaciones.setStatusOperacion(_operaciones.getValue("status_operacion"));
	                        operacionesFinancieras.add(ordenOperaciones);
	                    } //fin while operaciones financieras rechazadas o en espera.
		                logger.info("[ENVIANDO OPERACIONES PENDIENTES HACIA ALTAIR]");
		                this.enviarAltair(factoryAltair, operacionesFinancieras,false);
                    }*/

                    //Verificamos si quedaron pendientes operaciones Rechazadas o En Espera por liquidacion
                    boolean existe = ordenDAO.listarOperacionesLiquidacion(_ordenes.getLong("ordene_id"));
                    operacionesEnRechazo = existe;
                    /*
                     * Si las operaciones enviadas fueron todas aplicadas se ingresan los titulos a custodia y se verifica
                     * ,si existe pacto de recompra
                     */
                    if (logger.isDebugEnabled()){
                      logger.debug("Orden: "+_ordenes.getString("ordene_id")+" posee operaciones pendientes: "+existe);
                    }
                    //if (!existe || !cobroEnLinea) {
                    if (!existe) {
                        logger.info("[Se aplicaron las operaciones pendiente de la orden: "+_ordenes.getString("ordene_id")+"]");
                        try {
                            // Se invoca Ingreso Masivo a Custodia.
                            // Si la orden tiene recompra, Efectuando la salida de los títulos de recompra.
                        	 long inicioIC = System.currentTimeMillis();
                            String[] consultas = ingresoMasivoCustodia.ingresarCustodia(_ordenes.getLong("ordene_id"),cobroEnLinea,existe);
                            if (logger.isDebugEnabled()){
                            	logger.debug("Tiempo de procesamiento de ingresoMasivoCustodia " + (System.currentTimeMillis() - inicioIC) + " miliseg ");
                            }
                            
                            //ArrayList que contendra los querys finales para su ejecucion
                            ArrayList<String> sqlsFinales = new ArrayList<String>();

                            //Recorremos las consultas para insertarlas en una conexion
                            for (int i = 0; i < consultas.length; i++) {
                                sqlsFinales.add(consultas[i]);
                            }
                           
                            //Query para liquidar la orden
                            // update infi_tb_204_ordenes set ejecucion_id
                            long inicio = System.currentTimeMillis();
                            String sqlLiquidar = ordenDAO.liquidarOrden(secuenciaProcesos,StatusOrden.LIQUIDADA,_ordenes.getLong("ordene_id"));
                            if (logger.isDebugEnabled()){
                            	logger.debug("Tiempo de procesamiento de liquidacionOrden " + (System.currentTimeMillis() - inicio) + " miliseg ");
                            }
                            sqlsFinales.add(sqlLiquidar);

                            //Se verifica si el cliente pacto titulos para recompra
                            ordenRecompra.setIdCliente(_ordenes.getLong("client_id"));
                            ordenRecompra.setIdOrden(_ordenes.getLong("ordene_id"));
                            ordenRecompra.setVehiculoTomador(_ordenes.getString("ordene_veh_tom"));
                            ordenRecompra.setFechaPactoRecompra(Utilitario.StringToDate(_ordenes.getString("fecha_valor_recompra"), "dd/MM/yyyy") );
                            ordenRecompra.setTipoProducto(_ordenes.getString("tipo_producto_id"));
                            
                            //generarMensajeCarmen(_ordenes.getLong("ordene_id"), sqlsFinales);
                            
                            ArrayList<String> sqls = this.pactoRecompra(ordenRecompra);
                        	for (int i = 0; i < sqls.size(); i++) {
                                sqlsFinales.add(sqls.get(i));
                            }

                            //Pasamos el ArrayList A Array de String
                            String[] sqlsFinalesArray = new String[sqlsFinales.size()];
                            sqlsFinales.toArray(sqlsFinalesArray);
                            
                            long inicioEje=System.currentTimeMillis();
                            db.execBatch(_dso, sqlsFinalesArray);
                            if (logger.isDebugEnabled()){
                            	logger.debug("Tiempo de procesamiento de ejecución de querys " + (System.currentTimeMillis() - inicioEje) + " miliseg ");
                            }

                                                       
                            logger.info("[VERIFICADOS TITULOS PACTO RECOMPRA]");
                            logger.info("[INGRESADOS TITULOS A CUSTODIA]");
                            logger.info("[ORDEN LIQUIDADA]");
                        } catch (Exception e) {
                        	
                            logger.error("[LA ORDEN PRESENTO INCONVENIENTES PARA SER PROCESADA]--->"+"["+_ordenes.getString("ordene_id") + "]--->" +e.getMessage(),e);
                        }
                    } //FIN Si despues de altair no existen aun operaciones financieras
                } //fin if tiene operaciones financieras
                
                else {// Si la orden NO tiene operaciones pendientes . . . . . .
                    logger.info("[LA ORDEN: "+_ordenes.getString("ordene_id")+" NO POSEE OPERACIONES PENDIENTES]");
                    logger.info("Se procede a liquidar la orden 1. . . .");
                    try {
                        /* Si no posee operaciones financieras pendientes se liquida la orden se verifica si posee titulos
                         * pactados para recompra y se ingresan a custodia
                         */
                       
                    	long inicioIC = System.currentTimeMillis();
                        String[] consultas = ingresoMasivoCustodia.ingresarCustodia(_ordenes.getLong("ordene_id"),cobroEnLinea,false);
                        if (logger.isDebugEnabled()){
                        	logger.debug("Tiempo de procesamiento de ingresoMasivoCustodia " + (System.currentTimeMillis() - inicioIC) + " miliseg ");
                        }
                        ArrayList<String> sqlsFinales = new ArrayList<String>();

                        //generarMensajeCarmen(_ordenes.getLong("ordene_id"), sqlsFinales);
                        
                        //Recorremos las consultas para insertarlas en una conexion
                        for (int i = 0; i < consultas.length; i++) {
                            sqlsFinales.add(consultas[i]);
                        }
                        
                        //Query para liquidar la orden
                        long inicio = System.currentTimeMillis();
                        String sqlLiquidar = ordenDAO.liquidarOrden(secuenciaProcesos,StatusOrden.LIQUIDADA,_ordenes.getLong("ordene_id"));
                        sqlsFinales.add(sqlLiquidar);  
                        if (logger.isDebugEnabled()){
                        	logger.debug("Tiempo de procesamiento de liquidacionOrden " + (System.currentTimeMillis() - inicio) + " miliseg ");
                        }
                        

                        ordenRecompra.setIdCliente(_ordenes.getLong("client_id"));
                        ordenRecompra.setIdOrden(_ordenes.getLong("ordene_id"));
                        ordenRecompra.setVehiculoTomador(_ordenes.getString("ordene_veh_tom"));
                        ordenRecompra.setFechaPactoRecompra(Utilitario.StringToDate(_ordenes.getString("fecha_valor_recompra"), "dd/MM/yyyy") );
                        ordenRecompra.setTipoProducto(_ordenes.getString("tipo_producto_id"));
                        
                        // ACA DEBE IR EL FILTRO PARA NO CREAR ORDEN DE RECOMPRA Y DEAL DE OPICS
                        /* SI LA UI ES TIPO SITME . . . . . . .
                        if (!_unidad_inversion.getValue("insfin_descripcion").equals("TITULOS_SITME")){
                        	ArrayList<String> sqls = this.pactoRecompra(ordenRecompra);
                        	for (int i = 0; i < sqls.size(); i++) {
                                sqlsFinales.add(sqls.get(i));
                            }
                        }*/	
                        
                        ArrayList<String> sqls = this.pactoRecompra(ordenRecompra);
                    	for (int i = 0; i < sqls.size(); i++) {
                            sqlsFinales.add(sqls.get(i));
                        }

                        //Pasamos el ArrayList A Array de String
                        String[] sqlsFinalesArray = new String[sqlsFinales.size()];
                        sqlsFinales.toArray(sqlsFinalesArray);
                        
                        long inicioEje=System.currentTimeMillis();
                        //SE EJECUTA LA TRANSACCION
                        db.execBatch(_dso, sqlsFinalesArray);
                        if (logger.isDebugEnabled()){
                        	logger.debug("Tiempo de procesamiento de ejecución de querys " + (System.currentTimeMillis() - inicioEje) + " miliseg ");
                        }

                        
                        logger.info("[VERIFICADOS TITULOS PACTO RECOMPRA]");
                        logger.info("[INGRESADOS TITULOS A CUSTODIA]");
                        logger.info("[ORDEN LIQUIDADA]");
                        
                    } catch (Exception e) {
                    	
                        logger.error("[LA ORDEN PRESENTO INCONVENIENTES PARA SER PROCESADA]--->"+"["+_ordenes.getString("ordene_id") + "]--->" +e.getMessage(),e);
                        
                    } //fin catch
                } //FIN ELSE no posee operaciones pendientes con status en ESPERA
            } //fin while ORDEN

            logger.info("[ORDENES PROCESADAS...]");

            //El último hilo que queda procesa
            procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.LIQUIDACION);
        	if (procesosDAO.getDataSet().count() == 1){            
		            _ordenesAdjudicadas = statement.executeQuery(ordenDAO.listarOrdenesPorUnidadInversion(unidadInversion,StatusOrden.ADJUDICADA,false,0));
		            int cantidadOrdenesAdj = 0;
		           
		            if(_ordenesAdjudicadas.next()){//Verifica si existen ordenes procesadas
		            	cantidadOrdenesAdj++;
		            }
		            
		            //NM25287 26/04/2017 Se mueve la consulta a arriba, para no repetir tantos accesos a la misma data
		            //unidadInversionDAO.listarPorId(unidadInversion);
		            if (inBCV && (cantidadOrdenesAdj == 0)) { //Se valida que la fecha de liquidacion 1 y 2 sea menor a la actual
		            	
		            	logger.info("[Verificando operaciones de DEB y CRE asociadas a la orden vehiculo]");
		            	ArrayList<String> operacionesVehiculo = this.validarOperacionesVehiculo(ordenDAO, unidadInversion);
		        		
		            	if(operacionesVehiculo!=null && !operacionesVehiculo.toString().equals("[]"))
		            	{
		            		logger.info(operacionesVehiculo.toString());
		            		throw new Exception(operacionesVehiculo.toString());
		            	}
		            	//cierreUISITME=unidinvFeLiquidacionHora2.compareTo(fechaActual); Este valor no se usa
		            	//||cierreUISITME==-1           	
			            String updateUnidadInversion = unidadInversionDAO.modificarStatusQuery(unidadInversion,UnidadInversionConstantes.UISTATUS_LIQUIDADA);
		                db.exec(_dso, updateUnidadInversion);
			            logger.info("[UNIDAD INVERSION LIQUIDADA]--->CREDITO AL BCV REALIZADO Y 0 ORDENES ADJUDICADAS PENDIENTES");
		            }
		       
		
		        if(cantidadOrdenesAdj==0) { //No existen ordenes adjudicadas        	
		           
		            logger.info("[NO EXISTEN ORDENES POR PROCESAR EN STATUS ADJUDICADAS]");
		
		            if (inBCV) {
		            	
		            	logger.info("[Verificando operaciones de DEB y CRE asociadas a la orden vehiculo]");
		            	ArrayList<String> operacionesVehiculo = this.validarOperacionesVehiculo(ordenDAO, unidadInversion);
		        		if(operacionesVehiculo!=null && !operacionesVehiculo.toString().equals("[]"))
		            	{
		            		logger.info(operacionesVehiculo.toString());
		            		throw new Exception(operacionesVehiculo.toString());
		            	}
		        		//cierreUISITME=unidinvFeLiquidacionHora2.compareTo(fechaActual);
		        		//||cierreUISITME==-1
		            	//if ((!instrumentoFinancieroDescripcion.equals("TITULOS_SITME"))){
	            		String updateUnidadInversion = unidadInversionDAO.modificarStatusQuery(unidadInversion,UnidadInversionConstantes.UISTATUS_LIQUIDADA);
	                    db.exec(_dso, updateUnidadInversion);
		            	//}
		                logger.info("[UNIDAD INVERSION LIQUIDADA]--->CREDITO AL BCV REALIZADO Y 0 ORDENES ADJUDICADAS PENDIENTES");
		                
		            } //fin if inBCV
		        } //fin else
            }

        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
			throw e;
			
		}finally{
			
			 //Al finalizar se actualiza la fecha de fin del proceso
	        proceso = new Proceso();
	        proceso.setEjecucionId(secuenciaProcesos);

	        String queryProcesoCerrar = procesosDAO.modificar(proceso);
	        db.exec(_dso, queryProcesoCerrar);
	        
	        //Cerrar conexiones
	        if(statement!=null){
	        	statement.close();
	        }
	        if(_ordenes!=null){
	        	_ordenes.close();
	        }
	        if(_ordenesAdjudicadas!=null){
	        	_ordenesAdjudicadas.close();
	        }
	        if(transaccion!=null){
	        	transaccion.closeConnection();
	        }
	        
	        logger.info("Finalizado proceso de Liquidacion...."+new Date());
		}
       
    } //fin metodo para liquidar la unidad de inversion

    //****************************************************************************************************************
    /**
     * Se valida que exista para los titulos correspondiente a la orden, pacto de recompra
     * En caso de haber pactado se genera una orden con las operaciones financieras y las instrucciones de pago
     * @param orden
     * @throws Exception
     */
    public ArrayList<String> pactoRecompra(Orden orden)
        throws Exception {
    	
        //Se busca los titulos que el cliente haya pactado para recompra
        TitulosDAO titulosDAO = new TitulosDAO(_dso);
        long ordenlong = orden.getIdOrden();
        
        //ArrayList que contendra los SQL para las instrucciones de pago
        ArrayList<String> sqls = new ArrayList<String>();

        //Se crean las ordenes de pacto de recompra para titulos CON NETEO
        boolean neteo = true;
        titulosDAO.titulosRecompra(orden.getIdOrden(),neteo);
        
        //Dataset a utilizar
        DataSet _titulosRecompraNeteo = titulosDAO.getDataSet();
        
        //Se crean las ordenes de pacto de recompra para titulos con neteo
        try {
        	this.crearOrdenRecompra(_titulosRecompraNeteo, orden, sqls,neteo);
		} catch (Exception e) {
			logger.error("[PROBLEMAS AL GENERAR ORDEN RECOMPRA CON NETEO]--->"+ordenlong+" "+e.getMessage(),e);
		}
		
		
		//Se crean las ordenes de pacto de recompra para titulos SIN NETEO
        neteo = false;
        titulosDAO.titulosRecompra(orden.getIdOrden(),neteo);

        //Dataset a utilizar
        DataSet _titulosRecompraSinNeteo = titulosDAO.getDataSet();
        //Se crean las ordenes de pacto de recompra para titulos SIN NETEO
        	this.crearOrdenRecompra(_titulosRecompraSinNeteo, orden, sqls,neteo);

        if (sqls.isEmpty()) {
        	
            logger.info("[NO EXISTEN TITULOS PACTADOS PARA RECOMPRA PARA LA ORDEN NUMERO]--->" +ordenlong);
            
        }
        return sqls;
    } //fin verificarPactoRecompra
    
    /**
     * Crea la orden de recompra dependiendo si es con NETEO o sin NETEO
     * @param _titulosRecompra
     * @param orden
     * @param sqls
     * @param neteo
     * @throws Exception
     */
	public void crearOrdenRecompra(DataSet _titulosRecompra,Orden orden,ArrayList<String> sqls,boolean neteo)throws Exception
    {

    	Date fecha = new Date();
    	FechaValorDAO fechaValorDAO = new FechaValorDAO(_dso);
    	OrdenDAO ordenDAO = new OrdenDAO(_dso);
    	IngresoOpics ingresoOpics = new IngresoOpics(_dso, _app,usuario, ip, this.nombreUsuario);
    	ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(_dso);
    	MonedaDAO monedaDAO = new MonedaDAO(_dso);
    	ArrayList<String> sqlsMensajesOpics =new ArrayList<String>();
    	
    	//Se verifica si existen titulos pactados para recompra
        if (_titulosRecompra.count() > 0) {
            logger.info("[EXISTEN TITULOS PARA RECOMPRA PARA LA ORDEN NUMERO]--->" + orden.getIdOrden());
            _titulosRecompra.first();

            //HashMap a utilizar en el proceso para agrupar montos y titulos por moneda
            HashMap<String, BigDecimal> operacionesHashMap = new HashMap<String, BigDecimal>();
            HashMap<String, ArrayList> idTitulos = new HashMap<String, ArrayList>(); //Agrupa los titulos por moneda

                while (_titulosRecompra.next()) {
                	
                    //Se crea el objeto Orden Titulo
                    OrdenTitulo ordenTitulo = new OrdenTitulo();
                    ordenTitulo.setMonto(Double.parseDouble(_titulosRecompra.getValue("titulo_monto")));
                    ordenTitulo.setPorcentaje(_titulosRecompra.getValue("titulo_pct"));
                    ordenTitulo.setPorcentajeRecompra(Double.parseDouble(_titulosRecompra.getValue("titulo_pct_recompra")));
                    ordenTitulo.setTituloId(_titulosRecompra.getValue("titulo_id"));
                    ordenTitulo.setUnidades(Double.parseDouble(_titulosRecompra.getValue("titulo_unidades")));
                    ordenTitulo.setMontoNeteo(_titulosRecompra.getValue("TITULO_MTO_NETEO")!=null&&!_titulosRecompra.getValue("TITULO_MTO_NETEO").equals("")?new BigDecimal(_titulosRecompra.getValue("TITULO_MTO_NETEO")):new BigDecimal(0));

                    //Calculamos el monto para el precio de recompra indicado
                    BigDecimal precioRecompra = new BigDecimal(_titulosRecompra.getValue("titulo_pct_recompra"));
                    BigDecimal tituloMonto = new BigDecimal(_titulosRecompra.getValue("titulo_monto"));
                    BigDecimal montoRecompra = new BigDecimal(0);
                    montoRecompra = tituloMonto.setScale(3,BigDecimal.ROUND_HALF_EVEN).multiply(precioRecompra.setScale(3, BigDecimal.ROUND_HALF_EVEN));
                    montoRecompra = montoRecompra.divide(new BigDecimal(100).setScale(3, BigDecimal.ROUND_HALF_EVEN));

                    logger.info("[CALCULANDO PRECIO DE RECOMPRA...(MontoRecompra = tituloMonto*PrecioRecompra)/100...]--->" +montoRecompra);

                    boolean contiene = operacionesHashMap.containsKey(_titulosRecompra.getValue("titulo_moneda_neg"));

                    /*
                     * Se verifica si el HashMap contiene la Clave de la moneda, de ser asi se le suma el monto calculando el monto
                     * de recompra, de no contenerlo, se agrega la nueva clave valor.
                     * De igual forma se agrega el objeto orden titulo al arrayList perteneciente a la misma moneda
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
                        
                    } //fin else
                } //fin while titulos en recompra

                /*
                 * Se itera el HashMap para crear las Ordenes con sus titulos asociados, la operacion financiera,
                 * las instrucciones de pago y el deal para renta fija
                 */
                Iterator<Entry<String, ArrayList>> iterator = idTitulos.entrySet()
                                                                       .iterator();

                while (iterator.hasNext()) {
                    Map.Entry e = (Map.Entry) iterator.next();
                    String moneda = e.getKey().toString();
                    ArrayList<OrdenTitulo> ordenTituloArrayList = (ArrayList) e.getValue();

                    //Generamos una orden de Recompra
                    Orden ordenRecompra = new Orden();                    
                    InstruccionesPago instruccionesPago = new InstruccionesPago();                    

                    //Orden Recompra 
                    ordenRecompra.setCarteraPropia(false);
                    ordenRecompra.setTerminal(ip);
                    ordenRecompra.setVehiculoTomador(orden.getVehiculoTomador());
                    ordenRecompra.setSucursal(sucursal);
                    ordenRecompra.setIdUnidadInversion(orden.getIdUnidadInversion());
                    ordenRecompra.setNombreUsuario(nombreUsuario);
                    logger.info("Estableciendo id del clientettetetete " + orden.getIdCliente());
                    ordenRecompra.setIdCliente(orden.getIdCliente());
                    ordenRecompra.setStatus(neteo?StatusOrden.REGISTRADA:StatusOrden.REGISTRADA);
                    ordenRecompra.setIdTransaccion(TransaccionNegocio.PACTO_RECOMPRA);
                    ordenRecompra.setFechaOrden(fecha);
    				ordenRecompra.setFechaValor(orden.getFechaPactoRecompra()); // pendiente fechas valores
    				ordenRecompra.setTipoProducto(orden.getTipoProducto());
    				
    				/*Si al cliente no se le ha cobrado no puede establecerse fecha de pacto. Debe ser el proceso
    				 * de cobro de liquidación quien lo establezca.
    				*/
    				/*if (!cobroEnLinea){
    					if (ordenDAO.listarOperacionesLiquidacion(orden.getIdOrden())){
    						ordenRecompra.setFechaValor(new Date(2200,01,01));
    					}
    				}*/
    				
                    ordenRecompra.setIdOrdenRelacionada(orden.getIdOrden());
                    ordenRecompra.agregarOrdenTitulo(ordenTituloArrayList);
                    
                   //Si los titulos fueron pactados para recompra con neteo
                    if(neteo)
                    {
                    	//Creamos la operacion financiera
                        OrdenOperacion operacion = new OrdenOperacion();
                        operacion.setStatusOperacion(neteo?com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA:com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
                        operacion.setAplicaReverso(false);
                        operacion.setMontoOperacion(operacionesHashMap.get(moneda));
                        operacion.setTasa(new BigDecimal(0));
                        operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
                        operacion.setIdMoneda(moneda);
                        operacion.setFechaAplicar(ordenRecompra.getFechaValor());

                        //Agregamos la operacion financiera a la orden
                        ordenRecompra.agregarOperacion(operacion);
                        
                        //Se agrega la data extendida del tipo instruccion
                        OrdenDataExt ordenDataExt = new OrdenDataExt();
                        ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
                        ordenDataExt.setValor(String.valueOf(instruccionesPago.getTipoInstruccionId()));
                        ordenRecompra.agregarOrdenDataExt(ordenDataExt);
                        
                        String[] consultas = ordenDAO.insertar(ordenRecompra);

                        for (int i = 0; i < consultas.length; i++) {
                            sqls.add(consultas[i]);
                        }//fin for

                      //Generacion de los deal tickets por titulo
                      /*if (_unidad_inversion.getValue("insfin_descripcion").equals("TITULOS_SITME")){
                       	logger.info("Invocacion de metodo rentaFija para SITME");
                       	sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra,true);
                       	for (int i = 0; i < sqlsMensajesOpics.size(); i++) {
                               sqls.add(sqlsMensajesOpics.get(i).toString());
                           }//fin for
                        }*/
                        sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra);

                        for (int i = 0; i < sqlsMensajesOpics.size(); i++) {
                            sqls.add(sqlsMensajesOpics.get(i).toString());
                        }//fin for 
                        
                        /*
                         * Como los titulos fueron pactados para recompra, no es necesario
                         * buscar las intrucciones de pago, se continua
                         */
                        logger.info("[RECOMPRA CON NETEO, SE CONTINUA EL PROCESO]");
                        continue;
                    }//Si los titulos fueron pactados para recompra con neteo
                    
                    /*
                     * Por los diferentes tipos de moneda se genera la operacion financiera,
                     * la instruccion de pago y el deal ticket renta fija
                     */                    
                    if (moneda.equalsIgnoreCase(monedaLocal)) {		

                        //Buscamos la Instruccion de pago definida para transferencia cuenta nacional
                        boolean existeInstruccionPago = clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(),TipoInstruccion.CUENTA_NACIONAL, UsoCuentas.RECOMPRA);

	                    if(existeInstruccionPago){
	                        if (clienteCuentasDAO.getDataSet().count() > 0) {
	                            clienteCuentasDAO.getDataSet().first();
	                            clienteCuentasDAO.getDataSet().next();
	                        }
	                        
	                        instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_NACIONAL);
	
	                        //Creamos la operacion financiera
	                        OrdenOperacion operacion = new OrdenOperacion();
	                        operacion.setStatusOperacion(neteo?com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA:com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
	                        operacion.setAplicaReverso(false);
	                        operacion.setMontoOperacion(operacionesHashMap.get(moneda));
	                        operacion.setTasa(new BigDecimal(0));
	                        operacion.setNumeroCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_numero"));
	                        operacion.setNombreReferenciaCuenta(clienteCuentasDAO.getDataSet().getValue("ctecta_nombre"));
	                        operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
	                        operacion.setIdMoneda(moneda);
	                        operacion.setFechaAplicar(orden.getFechaPactoRecompra());
	
	                        //Agregamos la operacion financiera a la orden
	                        ordenRecompra.agregarOperacion(operacion);
	
	                        //Se agrega como data extendida el nombre del beneficiario
	                        OrdenDataExt ordenDataExt = new OrdenDataExt();
	                        ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
	                        ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
	                        ordenRecompra.agregarOrdenDataExt(ordenDataExt);
	
	                        //Se agrega como data extendida la cedula del beneficiario
	                        ordenDataExt = new OrdenDataExt();
	                        ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
	                        ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("CEDULA_BENEFICIARIO"));
	                        ordenRecompra.agregarOrdenDataExt(ordenDataExt);
	                    }else{
	                    	throw new Exception("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA LOCAL PARA LA ORDEN]--->"+orden.getIdOrden());
	                    }
                    } //moneda.equalsIgnoreCase(monedaLocal)

                    /*
                     * Si la moneda no es local, sabemos que es moneda extranjera y buscamos las instrucciones de pago definidas
                     * para dicha transaccion
                     */
                    else {

                        //Buscamos la Instruccion de pago definida para transferencia cuenta internacional o cheque                        
                        boolean cheque = clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(),TipoInstruccion.CHEQUE, UsoCuentas.RECOMPRA);

                        //Se verifica si existe una instruccion de pago definida para pago de cheque		
                        if (cheque) {
                        	
                            logger.info("[INSTRUCCION DE PAGO]--->[CHEQUE]");
                            clienteCuentasDAO.getDataSet().first();
                            clienteCuentasDAO.getDataSet().next();
                            instruccionesPago.setTipoInstruccionId(TipoInstruccion.CHEQUE);

                            //Creamos la operacion financiera
                            OrdenOperacion operacion = new OrdenOperacion();
                            operacion.setStatusOperacion(neteo?com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA:com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
                            operacion.setAplicaReverso(false);
                            operacion.setMontoOperacion(operacionesHashMap.get(moneda));
                            operacion.setTasa(new BigDecimal(0));
                            operacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
                            operacion.setIdMoneda(moneda);
                            operacion.setFechaAplicar(orden.getFechaPactoRecompra());

                            ordenRecompra.agregarOperacion(operacion);

                            //Se agrega como data extendida el nombre del beneficiario
                            OrdenDataExt ordenDataExt = new OrdenDataExt();
                            ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
                            ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
                            ordenRecompra.agregarOrdenDataExt(ordenDataExt);

                            //Se agrega como data extendida la cedula del beneficiario
                            ordenDataExt = new OrdenDataExt();
                            ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
                            ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("CEDULA_BENEFICIARIO"));
                            ordenRecompra.agregarOrdenDataExt(ordenDataExt);
                        } //FIN if(cheque)
                        
                        //Si no existe una operacion de cheque, deberia estar definida una para transferencia internacional SWIFT	
                        else {
                            logger.info("[INSTRUCCION DE PAGO]--->[SWIFT]");

                            //boolean swift = clienteCuentasDAO.listarCuentaCliente(orden.getIdOrden(),TipoInstruccion.CUENTA_SWIFT,UsoCuentas.RECOMPRA);
                            boolean swift = clienteCuentasDAO.existeCuentaClienteSwift(orden.getIdCliente());
                            if (!swift) {
                            	logger.error("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA EXTRANJERA PARA LA ORDEN]--->"+orden.getIdOrden());
                                throw new Exception("[NO EXISTEN INSTRUCCIONES ASOCIADAS DE MONEDA EXTRANJERA PARA LA ORDEN]--->"+orden.getIdOrden());
                                
                            } else {
                            	
                                clienteCuentasDAO.getDataSet().first();
                                clienteCuentasDAO.getDataSet().next();

                                instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_SWIFT);

                                //Creamos la operacion financiera
                                OrdenOperacion operacion = new OrdenOperacion();
                                operacion.setStatusOperacion(neteo?com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA:com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
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
                                operacion.setFechaAplicar(orden.getFechaPactoRecompra());
                                operacion.setIdMoneda(moneda);

                                /*Si al cliente no se le ha cobrado no puede establecerse fecha de pacto. Debe ser el proceso
            					 * de cobro de liquidación quien lo establezca.
            					*/
            					/*if (!cobroEnLinea){
            						if (operacionesEnRechazo){							
            							operacion.setFechaAplicar(new Date(2200,01,01));
            						}
            					}*/
            					
                                //Agregamos la operacion financiera a la orden
                                ordenRecompra.agregarOperacion(operacion);

                                //Se agrega como data extendida el nombre del beneficiario
                                OrdenDataExt ordenDataExt = new OrdenDataExt();
                                ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
                                ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("NOMBRE_BENEFICIARIO"));
                                ordenRecompra.agregarOrdenDataExt(ordenDataExt);

                                //Se agrega como data extendida la cedula del beneficiario
                                ordenDataExt = new OrdenDataExt();
                                ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
                                ordenDataExt.setValor(clienteCuentasDAO.getDataSet().getValue("CEDULA_BENEFICIARIO"));
                                ordenRecompra.agregarOrdenDataExt(ordenDataExt);
                            } //else SWIFT	
                        } //FIN ELSE si no es cheque
                    } //FIN ELSE ELSE si la moneda no es local

                    //Se agrega la data extendida del tipo instruccion
                    OrdenDataExt ordenDataExt = new OrdenDataExt();
                    ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
                    ordenDataExt.setValor(String.valueOf(instruccionesPago.getTipoInstruccionId()));
                    ordenRecompra.agregarOrdenDataExt(ordenDataExt);

                    String[] consultas = ordenDAO.insertar(ordenRecompra);

                    for (int i = 0; i < consultas.length; i++) {
                        sqls.add(consultas[i]);
                    }//fin for
                    
                    //Agrega el mensaje CARMEN de salida por la recompra
                    //generarMensajeCarmen(ordenRecompra, sqls);                    

                    
                    logger.info("Inicio de Generacion del en crearOrdenRecompra");
                    logger.info("insfin_descripcion: "+instrumentoFinancieroDescripcion);
                    //Generacion de los deal tickets por titulo
                    /*if (_unidad_inversion.getValue("insfin_descripcion").equals("TITULOS_SITME")){
                    	logger.info("Invocacion de metodo rentaFija para SITME");
                    	sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra,true);
                    	for (int i = 0; i < sqlsMensajesOpics.size(); i++) {
                            sqls.add(sqlsMensajesOpics.get(i).toString());
                        }//fin for
                    	
                    }*/
                    //Generacion de los deal tickets por titulo
                    sqlsMensajesOpics = ingresoOpics.rentaFija(ordenRecompra);
                    for (int i = 0; i < sqlsMensajesOpics.size(); i++) {
                        sqls.add(sqlsMensajesOpics.get(i).toString());
                    }//fin for
                } //fin iteracion del hashmap idTitulos
        } //FIN if(_titulosRecompra.count()>0){
    }//FIN crearOrdenRecompra
    
    /**
     * Enviar el arrayList hacia ALTAIR para que sean procesadas las operaciones
     * @param factoryAltair
     * @param ordenOperacionArrayList
     * @param inBCV
     * @throws Exception
     * @throws Throwable
     */
	public void enviarAltair(FactoryAltair factoryAltair,ArrayList<OrdenOperacion>ordenOperacionArrayList,boolean validar)throws Exception,Throwable{
    	
    	try {
	        	//Se intenta enviar la orden contra altair
	        	factoryAltair.aplicarOrdenes(ordenOperacionArrayList);
	        	logger.info("OPERACIONES ENVIADAS");
		} catch (Throwable e) {	
			
			logger.error("[INCONVENIENTES EN PROCESAR OPERACIONES HACIA ALTAIR"+e.getMessage(),e);
			if(validar)
			{
				inBCV = false;
			}	
			
		}//fin catch
    }//FIN METODO
    /**
     * Set de valores para la creacion del objeto ordenOperacion que depende de su transaccion
     * @param ordenOperacion
     * @param total
     * @param nuCuentaVehBCV
     * @param transaccionFija
     * @throws Exception
     */
	public void crearOrdenOperacion(OrdenOperacion ordenOperacion,BigDecimal total,String nuCuentaVehBCV,String nombreTransaccion,String codigoOperacion,String tipoTransaccion,long idOperacion)throws Exception{

    	if (logger.isDebugEnabled()){
	    	logger.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	    	logger.debug("[CREANDO ORDEN OPERACION (NO EXISTE EN BD): ");
	    	logger.debug("[STATUS]--->"+ConstantesGenerales.STATUS_EN_ESPERA);
	    	logger.debug("[TOTAL]--->"+total);
	    	logger.debug("[NUMERO DE CUENTA]--->"+nuCuentaVehBCV);
	    	logger.debug("[CODIGO OPERACION]--->"+codigoOperacion);
	    	logger.debug("[NOMBRE DE LA TRANSACCION]--->"+nombreTransaccion);
	    	logger.debug("[TIPO DE TRANSACCION]--->"+tipoTransaccion);
	    	logger.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	}

    	ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
    	ordenOperacion.setMontoOperacion(total);
    	ordenOperacion.setNumeroCuenta(nuCuentaVehBCV);
    	ordenOperacion.setCodigoOperacion(codigoOperacion);
    	ordenOperacion.setNombreOperacion(nombreTransaccion);
    	ordenOperacion.setTipoTransaccionFinanc(tipoTransaccion);
    	ordenOperacion.setIdMoneda(monedaLocal);
    	if(idOperacion!=0)
    		ordenOperacion.setIdOperacionRelacion(idOperacion);
    }//FIN METODO
    
    
	public String[] crearOrdenCreditoBCV(long unidadInversion,String VehiculoTomador,BigDecimal total,String numeroCuentaEnBCV,String nombreTransaccion,String codigoOperacion,long idOperacion,Orden orden,OrdenOperacion ordenOperacion,OrdenDAO ordenDAO)throws Exception,Throwable{
    	
    	Date date = new Date();
    	String[] querys = null;
    	//Generamos la orden la cual contendra una sola operacion de credito al banco central 
        orden = new Orden();
        orden.setIdCliente(clienteBCV);
        orden.setStatus(StatusOrden.REGISTRADA);
        orden.setIdTransaccion(TransaccionNegocio.LIQUIDACION);
        orden.setFechaOrden(date);
        orden.setFechaValor(date);
        orden.setCarteraPropia(false);
        orden.setIdUnidadInversion((int) unidadInversion);
        orden.setVehiculoTomador(VehiculoTomador);
        orden.setTipoProducto(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA);
        
        //Se crea la orden Operacion
        this.crearOrdenOperacion(ordenOperacion,total,numeroCuentaEnBCV,nombreTransaccion,codigoOperacion,TransaccionFinanciera.CREDITO,idOperacion);
        
        //Operacion agregada a la orden
        orden.agregarOperacion(ordenOperacion);
        
        //Insertamos la orden
        querys = ordenDAO.insertar(orden);

        //Retornamos querys
        return querys;
    }//fin metodo
    
    
    public ArrayList<String> validarOperacionesVehiculo(OrdenDAO ordenDAO,Long unidadInversion)throws Exception{
		ArrayList<String> mensajesVehiculos = new ArrayList<String>();
		ArrayList<OrdenOperacion> operacionesArrayList = new ArrayList<OrdenOperacion>();    	
    	try {
			/*if (cobroEnLinea){
		    	//Validacion de operaciones de debito al vehiculo involucrado*******************************
				ordenDAO.validarOperacionesVehiculo(unidadInversion);
				if(ordenDAO.getDataSet().count()>0)
				{			
					ordenDAO.getDataSet().first();
					
					while(ordenDAO.getDataSet().next())
					{
						//Se verifica si la operacion de los vehiculos involucrados para debito se encuentran rechazadas para lanzar exception
						if(ordenDAO.getDataSet().getValue("status_operacion").equals(ConstantesGenerales.STATUS_RECHAZADA) || ordenDAO.getDataSet().getValue("status_operacion").equals(ConstantesGenerales.STATUS_EN_ESPERA))
						{
							OrdenOperacion ordenOperaciones = new OrdenOperacion();
		                    ordenOperaciones.setIdOperacion(Long.parseLong(ordenDAO.getDataSet().getValue("ordene_operacion_id")));
		                    ordenOperaciones.setIdOrden(Long.parseLong(ordenDAO.getDataSet().getValue("ordene_id")));
		                    ordenOperaciones.setTipoTransaccionFinanc(ordenDAO.getDataSet().getValue("trnf_tipo"));
		                    ordenOperaciones.setNumeroCuenta(ordenDAO.getDataSet().getValue("ctecta_numero"));
		                    ordenOperaciones.setSerialContable(ordenDAO.getDataSet().getValue("serial"));
		                    ordenOperaciones.setMontoOperacion(((ordenDAO.getDataSet().getValue("monto_operacion") != null) &&!ordenDAO.getDataSet().getValue("monto_operacion").equals(""))? new BigDecimal(ordenDAO.getDataSet().getValue("monto_operacion")) : new BigDecimal(0));
		                    ordenOperaciones.setIdMoneda(ordenDAO.getDataSet().getValue("moneda_id"));
		                    ordenOperaciones.setCodigoOperacion(ordenDAO.getDataSet().getValue("codigo_operacion"));
		                    ordenOperaciones.setStatusOperacion(ordenDAO.getDataSet().getValue("status_operacion"));
		                    
		                    operacionesArrayList.add(ordenOperaciones);
		                    
						}//fin if
					}//fin while
					
					//Se intentan enviar las operaciones en espera y rechazada de los vehiculos involucrados
					FactoryAltair factoryAltair = new FactoryAltair(_dso, _app,true);					
				    this.enviarAltair(factoryAltair, operacionesArrayList, false);
					
					//Validamos el status de las operaciones
					ordenDAO.validarOperacionesVehiculo(unidadInversion);
					if(ordenDAO.getDataSet().count()>0)
					{			
						ordenDAO.getDataSet().first();
						
						while(ordenDAO.getDataSet().next())
						{
							if(ordenDAO.getDataSet().getValue("status_operacion").equals(ConstantesGenerales.STATUS_RECHAZADA)|| ordenDAO.getDataSet().getValue("status_operacion").equals(ConstantesGenerales.STATUS_EN_ESPERA))
							{
								mensajesVehiculos.add("La operacion financiera del vehículo Nro "+ordenDAO.getDataSet().getValue("ordene_operacion_id")+" perteneciente a la orden "+ordenDAO.getDataSet().getValue("ordene_id")+" no fue procesada por ALTAIR, se encuentra en status "+ordenDAO.getDataSet().getValue("status_operacion")+"; no se puede liquidar la unidad de inversión..."+ "\r\n");
							}
						}//fin while
					}//fin if			
				}//fin if
			}*/
    	} catch (Throwable e) {
			logger.info(e.getMessage()+"--->"+Utilitario.stackTraceException(e));
			e.printStackTrace();
		}
    	
    	return mensajesVehiculos;
    }
    
    /**
     * Busca si la unidad de inversión debe cobrarse batch
     * @param unidadInversion unidad de inversión
     * @throws Exception
     */
	/*private DataSet setearCobroEnLineaDeLaUnidad(long unidadInversion) throws Exception{		
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		unidadDAO.listarDatosUIPorId(unidadInversion);
		DataSet dataSet = unidadDAO.getDataSet();
		if (dataSet.next()){
			if (dataSet.getValue("in_cobro_batch_liq") != null && dataSet.getValue("in_cobro_batch_liq").equals("0")){
				logger.info("Cobro de liquidación vía batch");
				cobroEnLinea = true;	
			}
		}
		return dataSet;
	}*/
	   
    
	public void run() {
		try{
			liquidarUnidadInversion();
		}catch (Exception e) {
			logger.error("Error procesando la liquidación",e);
		} catch (Throwable e) {
			logger.info("Error procesando la liquidación",e);
		}
	}
	
	/**
	 * Genera el mensaje para Carmen
	 * @param orden id de la orden 
	 * @param titulo objeto orden de título
	 * @param queriesEjec una lista de consultas sql que deben ejecutarse. Se usa para adicionar alli las generadas en este método
	 * @throws Exception en caso de error
	 */
	/*private void generarMensajeCarmen(long idOrden, ArrayList<String> queriesExec) throws Exception{
				
		    Orden orden = ordenDAO.listarOrden(idOrden, false, true, false, false, false);
			_cuentaCustodia = getCuentaCustodia(orden.getIdCliente());
				
			//Datos de la cuenta custodia
			if(_cuentaCustodia.count()>0){
			_cuentaCustodia.first();
			
			_cuentaCustodia.next();
						
			ArrayList<OrdenTitulo> listaTitulos = orden.getOrdenTitulo();
			for (OrdenTitulo ordenTitulo : listaTitulos) {
				MensajeCarmen mensajeCarmen = new MensajeCarmen();			

				//Setear Valores al Mensaje para Interfaz Carmen
				mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, ordenTitulo.getTituloId());
				mensajeCarmen.set(mensajeCarmen.CANTIDAD, ordenTitulo.getUnidades());
				mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, new Date());
				mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, orden.getFechaValor());
				mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, obtenerCodigoClienteContraparteBDV());
				mensajeCarmen.setUsuarioNM(nmUsuario);			
				mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));
				
				//Establecer valores por defecto al mensaje:
				mensajeDAO.estableceValoresPorDefecto(mensajeCarmen);
					
				String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);
				
				
				for(int k=0; k<sentenciasMje.length; k++){
					queriesExec.add(sentenciasMje[k]);
				}
			}
			}
	}/*
	
	/**
	 * Genera el mensaje para Carmen
	 * @param orden iobjeto orden a procesar 
	 * @param titulo objeto orden de título
	 * @param queriesEjec una lista de consultas sql que deben ejecutarse. Se usa para adicionar alli las generadas en este método
	 * @throws Exception en caso de error
	 */
	/*private void generarMensajeCarmen(Orden orden, ArrayList<String> queriesExec) throws Exception{
				
			_cuentaCustodia = getCuentaCustodia(orden.getIdCliente());
				
			//Datos de la cuenta custodia
			_cuentaCustodia.first();
			_cuentaCustodia.next();
						
			ArrayList<OrdenTitulo> listaTitulos = orden.getOrdenTitulo();
			for (OrdenTitulo ordenTitulo : listaTitulos) {
				MensajeCarmen mensajeCarmen = new MensajeCarmen();			

				//Setear Valores al Mensaje para Interfaz Carmen
				mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, ordenTitulo.getTituloId());
				mensajeCarmen.set(mensajeCarmen.CANTIDAD, ordenTitulo.getUnidades());
				mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, new Date());
				mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, orden.getFechaValor());
				mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, obtenerCodigoClienteContraparteBDV());
				mensajeCarmen.setUsuarioNM(nmUsuario);			
				mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));
				
				//Establecer valores por defecto al mensaje:
				mensajeDAO.estableceValoresPorDefecto(mensajeCarmen);
					
				String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);
				
				
				for(int k=0; k<sentenciasMje.length; k++){
					queriesExec.add(sentenciasMje[k]);
				}
			}
	}*/
	
	/**
	 * Busca los datos de la cuenta custodia del cliente si existe.
	 * @param idCliente id del cliente
	 * @return dataSet con los datos de la cuenta custodia del cliente
	 * @throws Exception en caso de error
	 */
	public DataSet getCuentaCustodia(long idCliente) throws Exception{
		cuentaCustodiaDAO.getCuentaCustodia(idCliente);
		return cuentaCustodiaDAO.getDataSet();
	}	
	
	/**
	 * Obtiene el cliente de BCV
	 * @param rifBancoCentral rif del Banco Central
	 * @throws Exception en caso de error
	 */
	private void obtenerClienteBcv(String rifBancoCentral) throws Exception{
		ManejoDeClientes manejoDeClientes 	  = new ManejoDeClientes(_dso);
		ClienteDAO clienteDAO 				  = new ClienteDAO(_dso);
		DataSet _clienteBCV = null;
		try {
			logger.debug("Buscando cliente BCV en INFI");
            clienteDAO.buscarPorCedRif(Long.parseLong(rifBancoCentral));
            _clienteBCV = clienteDAO.getDataSet();
            
            //Si existe se setea a la variable de clienteBCV
            if (_clienteBCV.count() > 0) {
            	_clienteBCV.first();
            	_clienteBCV.next();
                clienteBCV = Long.parseLong(_clienteBCV.getValue("client_id"));
            }else{
            	//Objetos de los servicios
                manejoDeClientes.obtenerClienteAltair(rifBancoCentral,"J", ip, _app, true, false, true, false, this.nombreUsuario);
                obtenerClienteBcv(rifBancoCentral);
            }
        } catch (Exception e) {
           throw new Exception("Error buscando cliente BCV en ALTAIR " +e.getMessage(),e);
        }//fin catch
	}
	
	/**
	 * Obtiene el código del cliente registrado en CARMEN perteneciente a la contraparte de BDV
	 * @return código registrado en INFI
	 * @throws Exception en caso de error
	 */
	private int obtenerCodigoClienteContraparteBDV() throws Exception{
		if (customerNumberBDV == 0){
			String codigo = ParametrosDAO.listarParametros(ParametrosSistema.CUSTOMER_NUMBER_BDV, this._dso);
			if (codigo != null && !codigo.equals("")){
				customerNumberBDV = Integer.parseInt(codigo);
			}
		}
		return customerNumberBDV;
	}	
}

