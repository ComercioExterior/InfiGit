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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.logic.interfaz_altair.consult.ManejoDeClientes;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
/**
 * Clase que liquida una unidad de inversión de Tipo Inventario.<br>
 * Genera la operacion de debito a cuenta del vehículo.<br>
 * Crea la orden de credito al emisor por vehiculo, dependiendo si es mercado Primario o Secundario.<br>
 * Ingresa los titulos a Custodia.<br>
 * Genera el DEAL Ticket por los titulos involucrados en una orden.<br>
 * Liquida la orden.<br>
 * La unidad de inversión queda en status liquidada cuando :<br>
 * <li>No posea ordenes en status REGISTRADA</li>
 * <li>La fecha de cierre sea el sysdate</li>
 * @author elaucho
 */
public class LiquidacionTipoInventario {

	/*** DataSource*/
    private DataSource _dso;
    
    /***Id de moneda Local*/
    private String monedaLocal;

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
    private Logger logger = Logger.getLogger(LiquidacionTipoInventario.class);
    
    /*** Cliente Banco Central Id en INFI*/
    long idClienteBancoCentral = 0;
    
    boolean cobroEnLinea = false;
    
    private DataSet DsUnidadInversion = null;
    
	/*** Constructor* @param DataSource _dso
     * @param int usuario
     * @param ServletContext _app
     */
    public LiquidacionTipoInventario(DataSource _dso, int usuario,ServletContext _app,HttpServletRequest _req) {
    	
        this._dso = _dso;
        this.usuario = usuario;
        this._app = _app;
        this.ip = _req.getRemoteAddr();
        this.nombreUsuario = _req.getSession().getAttribute("framework.user.principal").toString();
        this.sucursal = _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL).toString();
        
    }//FIN CONSTRUCTOR
    
    
   
    /*** Genera la operacion de debito a cuenta del vehículo.<br>
     * Crea la orden de credito al emisor por vehiculo, dependiendo si es mercado Primario o Secundario.<br>
     * Ingresa los titulos a Custodia.<br>
     * Genera el DEAL Ticket por los titulos involucrados en una orden.<br>
     * Liquida la orden.<br>
     * La unidad de inversión queda en status liquidada cuando :<br>
     * <li>No posea ordenes en status REGISTRADA</li>
     * <li>La fecha de cierre sea el sysdate</li>
     */
	@SuppressWarnings("static-access")
	public void liquidarTipoInventario(long unidadInversionId)throws Exception,Throwable{
    	
    	//Definicion de Variables
    	Proceso proceso 				   	  = new Proceso();
    	int secuenciaProcesos 				  = Integer.parseInt(OrdenDAO.dbGetSequence(_dso,com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
    	ProcesosDAO procesosDAO 			  = new ProcesosDAO(_dso);
    	Date date 							  = new Date();
    	OrdenDAO ordenDAO 					  = new OrdenDAO(_dso);
    	FactoryAltair factoryAltair 	   	  = new FactoryAltair(_dso, _app);
        factoryAltair.ipTerminal			  = this.ip;
        factoryAltair.nombreUsuario			  = this.nombreUsuario;
        TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(_dso);
        TomaOrdenDAO tomaOrdenDAO 			  = new TomaOrdenDAO(null, _dso, _app,this.ip);
        UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
        MonedaDAO monedaDAO 			   	  = new MonedaDAO(_dso);
        monedaLocal 				   	  	  = monedaDAO.listarIdMonedaLocal();
        IngresoMasivoCustodia ingresoMasivoCustodia = new IngresoMasivoCustodia(_dso);
        IngresoOpics ingresoOpics 			  = new IngresoOpics(_dso, _app,usuario, ip, this.nombreUsuario);
        VehiculoDAO vehiculoDAO				  = new VehiculoDAO(_dso);
        OperacionDAO operacionDAO 			  = new OperacionDAO(_dso);
        
        Transaccion transaccion = new Transaccion(_dso);
        Statement statement = null;
        ResultSet _ordenes = null;
        
    	proceso.setEjecucionId(secuenciaProcesos);
        proceso.setFechaInicio(date);
        proceso.setFechaValor(date);
        proceso.setTransaId(TransaccionNegocio.LIQUIDACION);
        proceso.setUsuarioId(this.usuario);

        String queryProceso = procesosDAO.insertar(proceso);
        db.exec(_dso, queryProceso);
        
        //Se listan los vehiculos involucrados en la unidad de inversion, para realizar el respectivo credito al BCV
    	ordenDAO.listarOrdenesPorVehiculoInventario(unidadInversionId,StatusOrden.REGISTRADA);
    	DataSet vehiculoCreditos = ordenDAO.getDataSet();
    	
    	//Monto total del depósito al emisor
    	BigDecimal montoCreditoEmisor = BigDecimal.ZERO;
    	      
        //Se verifica el tipo de marcado para la unidad d einversion
        boolean esPrimario = unidadInversionDAO.esMercadoPrimario(unidadInversionId);
        
        //Verifica como cobrar la unidad
        setearCobroEnLineaDeLaUnidad(unidadInversionId);
        
        try {
        	/*
             * Si es mercado primario, se efectua un debito a la cu enta del vehiculo.
             * Se efectua un credito a la cuenta del emisor (Se crea orden con operacion de  CREDITO)
             */ 
        	
            if(vehiculoCreditos.count()>0)
            {
            	//Se coloca cursor del datatset en la primera posicion
        		vehiculoCreditos.first();

        		while(vehiculoCreditos.next())
        		{
        			
        			//Busca el monto a depositar
        			montoCreditoEmisor = new BigDecimal(vehiculoCreditos.getValue("total")).subtract(ordenDAO.listarCreditoPorVehiculoComision(unidadInversionId));

        			
        			//RIF Del Emisor
        			String rifEmisor = Util.replace(vehiculoCreditos.getValue("empres_rif"), "J-", ""); 
        			
    				 if(esPrimario){
    					 
    					 if(!vehiculoCreditos.getValue("total").equals("0")){
    						 
    						 //Orden de credito que se asociara a las ordenes por el cual se haya creado el monto de la operacion financiera
    						 Orden orden = new Orden();
    						 
    						 //Se procede a crear la transaccion						 
    						 logger.info("Se procede a crear la transaccion de debito y credito");
    						 this.crearTransaccion(rifEmisor,tomaOrdenDAO, vehiculoCreditos.getValue("vehicu_id"), unidadInversionId, transaccionFijaDAO, ordenDAO, factoryAltair, montoCreditoEmisor,vehiculoCreditos.getValue("vehicu_numero_cuenta"), vehiculoCreditos.getValue("empres_cuenta"),orden);
    					
    						 //Buscamos todas las ordenes registrada para este vehiculo
    						 ordenDAO.listarOrdenesTitulosInventario( vehiculoCreditos.getValue("vehicu_id"), unidadInversionId);
    						 logger.info("SE LISTAN LAS ORDENES REGISTRADAS POR VEHICULO");
    						 
    						 if(ordenDAO.getDataSet().count()>0){
    							 
    							 //Se procesan todas las ordenes registradas para este vehiculo
    							 this.procesarOrdenes(ordenDAO.getDataSet(), ingresoMasivoCustodia, ordenDAO, secuenciaProcesos, ingresoOpics,orden);
    							 
    						 }//fin if 
    					 } //fin if
    				 }//fin si es primario 
    				 /*
    				  * MERCADO SECUNDARIO
    				  */
    				 else{
    					 
    					 if(!vehiculoDAO.vehiculoEsBDV(vehiculoCreditos.getValue("ordene_veh_tom"))){
    						 
    						 if(!vehiculoCreditos.getValue("total").equals("0")){
    							 
    							 //Orden de credito que se asociara a las ordenes por el cual se haya creado el monto de la operacion financiera
    							 Orden orden = new Orden();
    							 
    							 //Se procede a crear la transaccion						 
    							 logger.info("Se procede a crear la transaccion de debito y credito");
    							 this.crearTransaccion(rifEmisor,tomaOrdenDAO, vehiculoCreditos.getValue("vehicu_id"), unidadInversionId, transaccionFijaDAO, ordenDAO, factoryAltair, montoCreditoEmisor,vehiculoCreditos.getValue("vehicu_numero_cuenta"), vehiculoCreditos.getValue("empres_cuenta"),orden);
    						
    							 //Buscamos todas las ordenes registrada para este vehiculo
    							 ordenDAO.listarOrdenesTitulosInventario( vehiculoCreditos.getValue("vehicu_id"), unidadInversionId);
    							 logger.info("SE LISTAN LAS ORDENES REGISTRADAS POR VEHICULO");
    							 
    							 if(ordenDAO.getDataSet().count()>0){
    								 
    								 //Se procesan todas las ordenes registradas para este vehiculo
    								 this.procesarOrdenes(ordenDAO.getDataSet(), ingresoMasivoCustodia, ordenDAO, secuenciaProcesos, ingresoOpics,orden);
    								 
    							 }//fin if 
    						 } //fin if
    					 }
    				 }//FIN MERCADO SECUNDARIO
        		}//fin while   	
            }//FIN vehiculoCreditos.count()>0
            
		} catch (Exception e) {
			
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			
		}finally{

			try {
				/*
				 * Se valida si no existe orden pendiente por cancelar al emisor
				 * Se valida que no existan ordenes con sattus registrada
				 * De ser asi y la unidad de inversion tiene fecha de cierre sysdate
				 * Se liquida la unidad de inversion
				 */
				if(!operacionDAO.listarOperacionesBCV(unidadInversionId, ConstantesGenerales.STATUS_RECHAZADA)){
					
					//Verificar si existen ordenes 
					int cantidadOrdenes =0;
					statement = transaccion.getConnection().createStatement();
					_ordenes = statement.executeQuery(ordenDAO.listarOrdenesPorUnidadInversion(unidadInversionId,StatusOrden.REGISTRADA,true,0));
					if(_ordenes.next()){
						cantidadOrdenes++;
					}			
					
					if(cantidadOrdenes==0){
					
						Date fechaCierre = unidadInversionDAO.fechaCierre(unidadInversionId);
						Date fecha = new Date();
						
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

						//Si las fechas son iguales se liquida la unidad de inversion
						if(simpleDateFormat.format(fechaCierre).compareTo(simpleDateFormat.format(fecha))<0)
						{
							
							String updateUnidadInversion = unidadInversionDAO.modificarStatusQuery(unidadInversionId,UnidadInversionConstantes.UISTATUS_LIQUIDADA);
				            db.exec(_dso, updateUnidadInversion);
				            logger.info("[UNIDAD INVERSION LIQUIDADA]--->CREDITO AL EMISOR REALIZADO Y 0 (CERO) ORDENES REGISTRADAS PENDIENTES");
				            
						}else{
							logger.info("La fecha de cierre no permite liquidar la unidad de inversión");
						}
					}
				}
				 //Al finalizar se actualiza la fecha de fin del proceso
				proceso = new Proceso();
				proceso.setEjecucionId(secuenciaProcesos);

				String queryProcesoCerrar = procesosDAO.modificar(proceso);
				db.exec(_dso, queryProcesoCerrar);
			
			} catch (Exception e) {							
				logger.error(e);				
			}finally{
				
		        //Cerrar conexiones
		        if(statement!=null){
		        	statement.close();
		        }
		        if(_ordenes!=null){
		        	_ordenes.close();
		        }
		        if(transaccion!=null){
		        	transaccion.closeConnection();
		        }
			}
		}
        
    }//FIN liquidarTipoInventario
    /**
     * Set de valores para la creacion del objeto ordenOperacion que depende de su transaccion
     * @param ordenOperacion
     * @param total
     * @param nuCuentaVehBCV
     * @param transaccionFija
     * @throws Exception
     */
    @SuppressWarnings("static-access")
	public void crearOrdenOperacion(OrdenOperacion ordenOperacion,BigDecimal total,String nuCuentaVehBCV,String nombreTransaccion,String codigoOperacion,String tipoTransaccion,long idOperacion)throws Exception{
    	
    	logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	logger.info("[CREANDO ORDEN OPERACION (NO EXISTE EN BD): ");
    	ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
    	logger.info("[STATUS]--->"+ConstantesGenerales.STATUS_EN_ESPERA);
    	ordenOperacion.setMontoOperacion(total);
    	logger.info("[TOTAL]--->"+total);
    	ordenOperacion.setNumeroCuenta(nuCuentaVehBCV);
    	logger.info("[NUMERO DE CUENTA]--->"+nuCuentaVehBCV);
    	ordenOperacion.setCodigoOperacion(codigoOperacion);
    	logger.info("[CODIGO OPERACION]--->"+codigoOperacion);
    	ordenOperacion.setNombreOperacion(nombreTransaccion);
    	logger.info("[NOMBRE DE LA TRANSACCION]--->"+nombreTransaccion);
    	ordenOperacion.setTipoTransaccionFinanc(tipoTransaccion);
    	logger.info("[TIPO DE TRANSACCION]--->"+tipoTransaccion);
    	ordenOperacion.setIdMoneda(monedaLocal);
    	logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	if(idOperacion!=0)
    		ordenOperacion.setIdOperacionRelacion(idOperacion);
    }//FIN METODO
    
    /**
     * Crea la orden de credito al emisor, con la operacion financiera
     * @param unidadInversion
     * @param VehiculoTomador
     * @param total
     * @param numeroCuentaEnBCV
     * @param nombreTransaccion
     * @param codigoOperacion
     * @param idOperacion
     * @param orden
     * @param ordenOperacion
     * @param ordenDAO
     * @return String[] consultas
     * @throws Exception
     * @throws Throwable
     */
    @SuppressWarnings({ "unchecked", "static-access" })
	public String[] crearOrdenCreditoEmisor(String rifEmisor,long unidadInversion,String VehiculoTomador,BigDecimal total,String numeroCuentaEnBCV,String nombreTransaccion,String codigoOperacion,long idOperacion,Orden orden,OrdenOperacion ordenOperacion,OrdenDAO ordenDAO)throws Exception,Throwable{
    	
    	ManejoDeClientes manejoDeClientes = new ManejoDeClientes(_dso);
    	ClienteDAO clienteDAO = new ClienteDAO(_dso);
    	long clienteEmisorId = 0;
    	
    	//Se verifica que el cliente exista en infi, de no existir se busca en Altair y se inserta en Infi
        DataSet _clienteEmisor = new DataSet();
        try {

        	//Objetos de los servicios
            Cliente clienteWS 	= new Cliente();
            clienteWS = manejoDeClientes.obtenerClienteAltair(rifEmisor,"J", ip, _app, true, false, true, false, this.nombreUsuario);

            //Se busca el cliente en INFI
            clienteDAO.listarPorCedRif(Long.parseLong(clienteWS.getCi()));
            _clienteEmisor = clienteDAO.getDataSet();

            //Si existe se setea a la variable de clienteEmisorId
            if (_clienteEmisor.count() > 0) {_clienteEmisor.first();_clienteEmisor.next();
            clienteEmisorId = Long.parseLong(_clienteEmisor.getValue("client_id"));
            }
            logger.info("Emisor existe en altair");
        } catch (Exception e) {
        	logger.error("Error buscando cliente Emisor en ALTAIR " +e.getMessage()+ Utilitario.stackTraceException(e));
           throw new Exception("Error buscando cliente Emisor en ALTAIR " +e.getMessage());
        }//fin catch
        
        
    	Date date = new Date();
    	String[] querys = null;
    	
    	//Generamos la orden la cual contendra una sola operacion de credito al banco central 
        orden = new Orden();
        orden.setIdCliente(clienteEmisorId);//EL cliente es el Emisor el cual se debe buscar
        orden.setStatus(StatusOrden.REGISTRADA);
        orden.setIdTransaccion(TransaccionNegocio.LIQUIDACION);
        orden.setFechaOrden(date);
        orden.setFechaValor(date);
        orden.setCarteraPropia(false);
        orden.setIdUnidadInversion((int) unidadInversion);
        orden.setVehiculoTomador(VehiculoTomador);
        
        //Se crea la orden Operacion
        this.crearOrdenOperacion(ordenOperacion,total,numeroCuentaEnBCV,nombreTransaccion,codigoOperacion,TransaccionFinanciera.CREDITO,idOperacion);
        
        //Operacion agregada a la orden
        orden.agregarOperacion(ordenOperacion);
        
        //Insertamos la orden
        querys = ordenDAO.insertar(orden);
        
        //Retornamos querys
        return querys;
    }//fin metodo
    /**
     * Metodo que crea el debito a la cuenta del vehiculo y la orden de credito al EMISOR
     * Envia las operaciones hacia altair
     * @param tomaOrdenDAO
     * @param idVehiculo
     * @param unidadInversionId
     * @param transaccionFijaDAO
     * @param ordenDAO
     * @param factoryAltair
     * @param total
     * @param numeroCuentaVehiculo
     * @param numeroCuentaEmisor
     * @throws Exception
     * @throws Throwable
     */
    @SuppressWarnings({ "static-access", "unchecked" })
	public void crearTransaccion(String rifEmisor,TomaOrdenDAO tomaOrdenDAO,String idVehiculo,long unidadInversionId,TransaccionFijaDAO transaccionFijaDAO,OrdenDAO ordenDAO,FactoryAltair factoryAltair,BigDecimal total,String numeroCuentaVehiculo,String numeroCuentaEmisor,Orden orden)throws Exception,Throwable{

		
		//ArrayList de consultas a ser ejcutadas en una transaccion
		ArrayList ejecutarExecBachtArrayList = new ArrayList();
		
		//Listar orden existente del vehiculo
    	Orden ordenVehiculo = tomaOrdenDAO.crearBuscarOrdenVehiculoUI(idVehiculo, this.ip, unidadInversionId, nombreUsuario, 0, this.sucursal);
		
    	logger.info("ORDEN VEHICULO>>>>"+ordenVehiculo.getIdOrden());
    	//Creamos la operacion de DEBITO a la cuenta del vehiculo
        OrdenOperacion ordenOperacionDebito  = new OrdenOperacion();  	 
        
        //Transaccion Fija DEBITO A CUENTA DEL VEHICULO
        com.bdv.infi.data.TransaccionFija transaccionFija = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.DEBITO_CUENTA_VEHICULO,idVehiculo,DsUnidadInversion.getValue("insfin_if"));
        ArrayList ordenOperacionArrayList = new ArrayList<OrdenOperacion>();
        
        //Le agregamos la operacion a la orden para ser enviada a Altair 
        this.crearOrdenOperacion(ordenOperacionDebito,total,numeroCuentaVehiculo,transaccionFija.getNombreTransaccion(),transaccionFija.getCodigoOperacionVehDeb(),TransaccionFinanciera.DEBITO,0);

        //Se agrega la operación a la orden
        ordenVehiculo.agregarOperacion(ordenOperacionDebito);
		
        //Insertamos la orden creada con la operacion financiera de credito al Emisor
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

		//Transaccion Fija CRE al EMISOR
        transaccionFija = transaccionFijaDAO.obtenerTransaccion(TransaccionFija.CREDITO_CUENTA_EMISOR,idVehiculo,DsUnidadInversion.getValue("insfin_if"));

        //Creacion de objetos
        OrdenOperacion ordenOperacion = new OrdenOperacion();
        
        //Creamos la orden de credito al BCV, ya que no existe
        String sqls[] = this.crearOrdenCreditoEmisor(rifEmisor,unidadInversionId,idVehiculo,total,numeroCuentaEmisor, transaccionFija.getNombreTransaccion(), transaccionFija.getCodigoOperacionCteCre(),operacionDebitoVehiculo.getIdOperacion(),orden,ordenOperacion,ordenDAO);
        
        //recorremos los querys para agregarlos al arrayList
        for(int i=0;i<sqls.length;i++){
        ejecutarExecBachtArrayList.add(sqls[i]);
        }
        
        //Ejecutamos el execbach
        db.execBatch(_dso,(String[]) ejecutarExecBachtArrayList.toArray(new String[ejecutarExecBachtArrayList.size()]));
        
        if (cobroEnLinea){
	        //Orden  Vehiculo Debito
	        this.enviarAltair(factoryAltair, ordenOperacionArrayList);
        }
        
        //Obtenemos las operaciones
        ArrayList ordenOperacionArrayListCre = new ArrayList<OrdenOperacion>();
        
        //recorremos las operaciones para agregarlas al arrayList que se enviara a ALTAIR
        ordenOperacionArrayListCre.add(ordenOperacion);

        if (cobroEnLinea){
	        //Enviamos la operacion hacia altair
	        this.enviarAltair(factoryAltair, ordenOperacionArrayListCre);
        }
        
        logger.info("CULMINA LA TRANSACCION DE CREDITO AL EMISOR");

    }
    
    /**
     * Enviar el arrayList hacia ALTAIR para que sean procesadas las operaciones
     * @param factoryAltair
     * @param ordenOperacionArrayList
     * @param inBCV
     * @throws Exception
     * @throws Throwable
     */
    @SuppressWarnings("static-access")
	public void enviarAltair(FactoryAltair factoryAltair,ArrayList<OrdenOperacion>ordenOperacionArrayList)throws Exception,Throwable{
    	
    	try {
    		
        	//Se intenta enviar la orden contra altair
        	factoryAltair.aplicarOrdenes(ordenOperacionArrayList);
        	logger.info("OPERACIONES ENVIADAS");
        	
		} catch (Throwable e) {	
			
			logger.error("[INCONVENIENTES EN PROCESAR OPERACIONES HACIA ALTAIR"+e.getMessage()+ Utilitario.stackTraceException(e));
			
		}//fin catch
    }//FIN METODO
    /**
     * Metodo que recorre las ordenes, les agrega los titulos e intenta ingresar a custodia y generar el DEAL
     * @param ordenes
     * @param ingresoMasivoCustodia
     * @param ordenDAO
     * @param secuenciaProcesos
     * @param ingresoOpics
     * @param orden
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "static-access" })
	public void procesarOrdenes(DataSet ordenes,IngresoMasivoCustodia ingresoMasivoCustodia,OrdenDAO ordenDAO,long secuenciaProcesos,IngresoOpics ingresoOpics,Orden orden)throws Exception{

    	//Se posiciona el cursor del dataset en el primer registro
		 ordenes.first();
		 
		 //HashMap que contendra los objetos ordenes
		 SimpleDateFormat formato			 = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		 HashMap<String,Orden> ordenesHasmap = new HashMap<String,Orden>();		 
		 ArrayList<OrdenTitulo> ordenTituloArraylist  = new ArrayList<OrdenTitulo>();		 
		 
		 //Se recorren las ordenes para ingresar los titulos a custodia y generar el DEAL TICKET
		 while(ordenes.next()){
			 
			 //SI NO LO CONTIENE
			 if(!ordenesHasmap.containsKey(ordenes.getValue("ordene_id"))){
				 	
				 //Objetos a utilizar
				 ordenTituloArraylist = new ArrayList<OrdenTitulo>();
				 OrdenTitulo ordenTitulo = new OrdenTitulo();
				 Orden ordenCliente = new Orden();
				 
				 //set valores orden cliente
				 ordenCliente.setVehiculoTomador(ordenes.getValue("ordene_veh_tom"));
				 ordenCliente.setIdCliente(Long.parseLong(ordenes.getValue("client_id")));
				 ordenCliente.setFechaValor(formato.parse(ordenes.getValue("ordene_ped_fe_valor")));
				 ordenCliente.setFechaOrden(formato.parse(ordenes.getValue("ordene_ped_fe_orden")));
				 ordenCliente.setIdOrden(Long.parseLong(ordenes.getValue("ordene_id")));
				 
				 //Set de valores a OrdenTitulo
				 ordenTitulo.setMonto(Double.parseDouble(ordenes.getValue("titulo_monto")));
				 ordenTitulo.setMontoIntCaidos(new BigDecimal(ordenes.getValue("titulo_mto_int_caidos")));
				 ordenTitulo.setMontoNeteo(new BigDecimal(ordenes.getValue("titulo_mto_neteo")));
				 ordenTitulo.setPorcentaje(ordenes.getValue("titulo_pct"));
				 ordenTitulo.setPorcentajeRecompra(Double.parseDouble(ordenes.getValue("titulo_pct_recompra")));
				 ordenTitulo.setPrecioMercado(Double.parseDouble(ordenes.getValue("TITULO_PRECIO_MERCADO")));
				 ordenTitulo.setTituloId(ordenes.getValue("titulo_id"));
				 ordenTitulo.setUnidades(Double.parseDouble(ordenes.getValue("titulo_unidades")));
				 
				 //Se agrega el titulo al arrayList
				 ordenTituloArraylist.add(ordenTitulo);
				 
				 //Se agrega el arrayList a la orden del cliente
				 ordenCliente.agregarOrdenTitulo(ordenTituloArraylist);
				 
				 //Set del id relacionado a la orden de credito
				 ordenCliente.setIdOrdenRelacionada(orden.getIdOrden());

				 //Set clavo valor al Hashmap
				 ordenesHasmap.put(ordenes.getValue("ordene_id"), ordenCliente);
			 }else{
				
				 //Se recupera la orden
				 Orden ordenExistente = ordenesHasmap.get(ordenes.getValue("ordene_id"));
				 
				 //Objeto ordenTitulo
				 OrdenTitulo ordenTitulo = new OrdenTitulo();
				
				 //Set de valores a OrdenTitulo
				 ordenTitulo.setMonto(Double.parseDouble(ordenes.getValue("titulo_monto")));
				 ordenTitulo.setMontoIntCaidos(new BigDecimal(ordenes.getValue("titulo_mto_int_caidos")));
				 ordenTitulo.setMontoNeteo(new BigDecimal(ordenes.getValue("titulo_mto_neteo")));
				 ordenTitulo.setPorcentaje(ordenes.getValue("titulo_pct"));
				 ordenTitulo.setPorcentajeRecompra(Double.parseDouble(ordenes.getValue("titulo_pct_recompra")));
				 ordenTitulo.setPrecioMercado(Double.parseDouble(ordenes.getValue("TITULO_PRECIO_MERCADO")));
				 ordenTitulo.setTituloId(ordenes.getValue("titulo_id"));
				 ordenTitulo.setUnidades(Double.parseDouble(ordenes.getValue("titulo_unidades")));
				 
				 //Se agrega el titulo 	 
				 ordenExistente.getOrdenTitulo().add(ordenTitulo);
				 
				 //Set clave valor
				 ordenesHasmap.put(ordenes.getValue("ordene_id"), ordenExistente);
			 } 
		 }//fin while
		 
		 Iterator it =  ordenesHasmap.entrySet().iterator();	
		 
		 while(it.hasNext())
		 {
			 //Se crea la orden que contendra los valores del hashmap
			 Orden ordenProcesar = new Orden();
			 
			 try {

				Map.Entry e 	     = (Map.Entry) it.next();
	            String ordeneId 	 = e.getKey().toString();	            
	            ordenProcesar 		 = (Orden) e.getValue();
	            ArrayList<String> sqlsFinales = new ArrayList<String>();
	            
	            logger.info("PROCESANDO ORDEN NUMERO... "+ordeneId);
	            
				 //Ingresamos los titulos a custodia				                 
	            String[] consultas = ingresoMasivoCustodia.ingresarCustodia(ordenProcesar.getIdOrden(),cobroEnLinea,false);
	           
	            //Recorremos las consultas para insertarlas en una conexion 
	           for (int i = 0; i < consultas.length; i++) {
	               sqlsFinales.add(consultas[i]);
	           }
	           
	           //Generamos el deal
	           ArrayList<String> sqlsMensajesOpics = ingresoOpics.rentaFija(ordenProcesar);
	           
	           //Recorremos las consultas para insertarlas en una conexion
	           for (int i = 0; i < sqlsMensajesOpics.size(); i++) {
	               sqlsFinales.add(sqlsMensajesOpics.get(i));
	           }
	           
	           //Query para liquidar la orden
	           String sqlLiquidar = ordenDAO.liquidarOrden(secuenciaProcesos,StatusOrden.LIQUIDADA,ordenProcesar.getIdOrden());
	           sqlsFinales.add(sqlLiquidar);
	            
	           db.execBatch(_dso,(String[]) sqlsFinales.toArray(new String[sqlsFinales.size()]));
	           		                        
	           logger.info("[INGRESADOS TITULOS A CUSTODIA]");
	           logger.info("[ORDEN LIQUIDADA]");
		           
			} catch (Exception e) {
				logger.error("La orden número :"+ordenProcesar.getIdOrden()+" ha presentado inconvenientes para ser procesada..."+e.getMessage()+ Utilitario.stackTraceException(e));
			}
		 }//fin while	 	 
    }
    
    /**
     * Busca si la unidad de inversión debe cobrarse batch
     * @param unidadInversion unidad de inversión
     * @throws Exception
     */
	private void setearCobroEnLineaDeLaUnidad(long unidadInversion) throws Exception{
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		unidadDAO.listarPorId(unidadInversion);
		DsUnidadInversion = unidadDAO.getDataSet();
		if (DsUnidadInversion.next()){
			if (DsUnidadInversion.getValue("in_cobro_batch_liq") != null && DsUnidadInversion.getValue("in_cobro_batch_liq").equals("0")){
				logger.debug("Cobro de liquidación vía batch");
				cobroEnLinea = true;	
			}
		}		
	}    
}//FIN CLASE
