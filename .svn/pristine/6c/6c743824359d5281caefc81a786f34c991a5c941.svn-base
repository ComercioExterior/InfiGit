package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.CalculoMesDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.FechaValor;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.util.ConversionMontos;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.TCCCFMT;
import com.bdv.infi.webservices.manager.ManejadorTasaCambio;
import com.bdv.infi.data.ProcesoGestion;

/**Genera las comisiones que se deben cobrar a los clientes
 * @author nvisbal, elaucho*/
public class GenerarCupones implements Runnable {

	DataSource dataSource = null;
	int usuarioId;
	String usuarioNM;
	String sucursal;
	String direccionIp="";
	String idCliente = "";
	String[] titulos = null;
	HashMap<String,Date> fechasValores = new HashMap<String,Date>();
	HashMap<String,Date> listaTitulos = new HashMap<String,Date>(); //Lista de títulos calculados
	Orden orden = null;
	long idEjecucion;
	OrdenDAO ordenDAO = null;
	DataSet instruccionesDePago = null; //Instrucciones de pago del cliente
	ClienteCuentasDAO cuentaClienteDao = null;
	ArrayList<String> listaSQL = new ArrayList<String>();
	com.bdv.infi.dao.Transaccion transaccion = null;
	String idTransanccionFija = "";
	MonedaDAO monedaDAO = null;
	GestionPagoDAO gestionPagoDAO=null;
	ConversionMontos conversionMontos = null;
	Date fechaHoy = new Date();
	CustodiaDAO custodiaDao = null;
	FechaValorDAO fechaValorDAO = null;
	String idMonedaLocal = "";
	boolean ultimoPago = false; //Indica si el cupón a pagar es el último
	
		/**
		 * 
		 * @param datasource datasource, repositorio de datos 
		 * @param usuarioId id del usuario que inicia el proceso
		 * @param usuarioNM nm del usuario que inicia el proceso
		 * @param sucursal sucursal perteneciente al usuario del proceso
		 * @param titulos conjunto de títulos para la ejecución de pago de cupón.
		 *         Este conjunto esta conformado según el siguiente formato:
		 *           titulo_id--fecha_inicio--fecha_fin
		 */
	public GenerarCupones(DataSource datasource,int usuarioId,String usuarioNM, String sucursal, String[] titulos, String direccionIp) {
		this.dataSource = datasource;
		this.usuarioId=usuarioId;
		this.usuarioNM = usuarioNM;
		this.sucursal = sucursal;
		this.titulos = titulos;
		this.direccionIp = direccionIp;
		
				
		//Busca las fechas valores y carga el dao de cronograma de pagos
		fechaValorDAO = new FechaValorDAO(this.dataSource);	
	}
	
	public void run(){

		transaccion=new com.bdv.infi.dao.Transaccion(this.dataSource);
		ProcesosDAO procesoDao = null;
		/* Primero creamos el proceso */
		Proceso proceso = null;
		
		try {
			procesoDao = new ProcesosDAO(this.dataSource);
			CalculoCupones calculoCupones = new CalculoCupones(this.dataSource);
			ordenDAO = new OrdenDAO(this.dataSource);
			monedaDAO = new MonedaDAO(this.dataSource); 
			//Carga la moneda local
			idMonedaLocal = monedaDAO.listarIdMonedaLocal();
			cuentaClienteDao=new ClienteCuentasDAO(this.dataSource);
			gestionPagoDAO=new GestionPagoDAO(this.dataSource);
			conversionMontos = new ConversionMontos(this.dataSource);
			ProcesoGestion procesoGestion = new ProcesoGestion();
			//Se setea al inicio el usuario
			procesoGestion.setUsuarioId(usuarioId);
			custodiaDao = new CustodiaDAO(this.dataSource); 
			
			Date fechaInicio = null;
			Date fechaFin = null;			
			long idConsulta;
			DataSet consulta = null;		
			
			Logger.info(this,"Verificando si hay un proceso de generación de cupones...");
			
			procesoDao.listarPorTransaccionActiva(TransaccionNegocio.PAGO_CUPON);		
			
			if (procesoDao.getDataSet().count()>0){
				Logger.info(this,"Ya existe un proceso ejecutandose para el pago de cupones");
				throw new Exception("Ya existe un proceso ejecutandose para el pago de cupones");
			}
			
			Logger.info(this,"Iniciado el proceso de generación de cupones por el usuario que posee id " + usuarioId);
			
			proceso = new Proceso();
			proceso.setTransaId(TransaccionNegocio.PAGO_CUPON);
			proceso.setUsuarioId(usuarioId);
			proceso.setFechaInicio(new Date());
			proceso.setFechaValor(new Date());
			
			//Se ejecuta el proceso sin transacción para registrar el inicio del mismo
			db.exec(this.dataSource, procesoDao.insertar(proceso));
			idEjecucion = proceso.getEjecucionId();			
			
			//Busca las fechas de inicio y fin que vienen en el array de titulos
			String[] datos = this.titulos[0].split("--");
			if (datos.length<3){
				throw new Exception("Error en la obtención de datos para efectuar el proceso de cupón");
			}
			SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			fechaInicio = formatoFecha.parse(datos[1]);
			fechaFin = formatoFecha.parse(datos[2]);
			String[] titulosId = {datos[0]};
			
			//fechaInicio = custodiaDAO.buscarUltimaFechaPagoCupon();
			
			if (fechaInicio!=null){
				//Efectúa el cálculo de los cupones
				idConsulta = calculoCupones.calcularCupones(fechaInicio, fechaFin, usuarioId, null, titulosId);
				
				
				//Busca los datos generados de la consulta
				transaccion.begin();			
				CalculoMesDAO calculoMesDao = new CalculoMesDAO(transaccion);
				calculoMesDao.listarDetalles(idConsulta);
				
				//Recorre cada uno de los registros para generar las ordenes
				consulta = calculoMesDao.getDataSet();
				
				if (consulta.count()>0){
					
					consulta.first();
					SimpleDateFormat formatear	=new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);				
					
					if (consulta.count()>0){
						while (consulta.next()){
							
							//Verifica si es el último pago del cupón
							ultimoPago = false;
							if (consulta.getValue("signo_porcentaje").equals("1")){
								ultimoPago = true;
							}
							
							if (idCliente.equals("")){
								idCliente = consulta.getValue("client_id");
								generarOrden();
							} else if (!idCliente.equals(consulta.getValue("client_id"))){
								guardarOrden();
								idCliente = consulta.getValue("client_id");							
								generarOrden();							
							}
							Logger.info(this,"Procesando pago cupón del cliente " + consulta.getValue("client_id") + ", título " + consulta.getValue("titulo_id"));							
							
							/*Arma las operaciones en base a los cálculos generados y a las instrucciones de pago que
							posea el cliente*/
							OrdenOperacion ordenOperacion 	= new OrdenOperacion();	
							ordenOperacion.setTasa(new BigDecimal(consulta.getValue("tasa_monto")));
							Logger.info(this,"tasa_monto 1: "+ consulta.getValue("tasa_monto"));
							ordenOperacion.setIdTitulo(consulta.getValue("titulo_id"));
							ordenOperacion.setMontoOperacion(new BigDecimal(consulta.getValue("monto_operacion")));
							ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_CUPON));
							ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
							ordenOperacion.setIdMoneda(consulta.getValue("moneda_id"));
							ordenOperacion.setFechaAplicar(formatear.parse(consulta.getValue("fecha_fin_pago_cupon")));
							ordenOperacion.setFechaInicioCP(formatear.parse(consulta.getValue("fecha_inicio_pago_cupon")));
							ordenOperacion.setFechaFinCP(formatear.parse(consulta.getValue("fecha_fin_pago_cupon")));
							ordenOperacion.setNombreOperacion(consulta.getValue("operacion_nombre"));												
							
							//Agrega el título a la lista
							listaTitulos.put(consulta.getValue("titulo_id"),ordenOperacion.getFechaFinCP());						
										
							ordenOperacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
							orden.agregarOperacion(ordenOperacion);
						}
						guardarOrden();
					}			
				} else {
					throw new Exception("No hay detalle para los cupones");
				}
			}
			transaccion.end();
			Logger.info(this,"Proceso de generación de cupones finalizado con éxito");			
		} catch (Exception e) {
			try{
				transaccion.rollback();
				Logger.error(this,"Error en el proceso de generación de cupones. " + e.getMessage()+ Utilitario.stackTraceException(e));				
			} catch (Exception ex){
				Logger.error(this,Utilitario.stackTraceException(ex));
			}
			proceso.setDescripcionError("Error: " + e.getMessage());
		} finally {
			proceso.setFechaFin(new Date());
			if (proceso.getDescripcionError() == null) {
				proceso.setDescripcionError("");
			}

			try {
				db.exec(this.dataSource, procesoDao.modificar(proceso));
				procesoDao.cerrarConexion();
			} catch (Exception e) {
				Logger.error(this,"Error efectuando modificación al proceso. " + e.getMessage());
				Logger.error(this,Utilitario.stackTraceException(e));
			}
		}							
	}

	public void execute() throws Exception {

	}
		
	/**Genera una orden nueva*/
	private void generarOrden(){
		orden=new Orden();	
		orden.setIdEjecucion(idEjecucion);
		orden.setIdCliente(Long.parseLong(idCliente));
		orden.setIdTransaccion(TransaccionNegocio.PAGO_CUPON);
		orden.setStatus(StatusOrden.REGISTRADA);
		orden.setFechaValor(new Date());
		orden.setNombreUsuario(usuarioNM);
		orden.setSucursal(sucursal);
		orden.setTerminal(this.direccionIp);
		
		/*Reinicia la lista de sql a ejecutar por cliente. Se hace de esta forma para primero almacenar la orden
		 * y despues las demás instrucciones
		 */
		listaSQL = new ArrayList<String>();
		
		//Reinicia la lista de títulos que se deben generar
		listaTitulos.clear();	
	}
	
	/**Guarda la orden del cliente
	 * 
	 * @throws Exception lanza una excepción si hay un error
	 */
	private void guardarOrden() throws Exception{
		
		//Arma los títulos que deben ir con la orden
		Iterator iterator = listaTitulos.keySet().iterator();
		String idTitulo = null;
		while (iterator.hasNext()){
			idTitulo = (String) iterator.next();			
			OrdenTitulo ordenTitulo = new OrdenTitulo();
			ordenTitulo.setTituloId(idTitulo);
			orden.agregarOrdenTitulo(ordenTitulo);
						
			//Actualiza la fecha de último pago de cupón para el título en custodia
			listaSQL.add(custodiaDao.actualizarFechaUltPagCupon(listaTitulos.get(idTitulo), idTitulo, orden.getIdCliente(),ultimoPago));
		}
		
		
		//Guarda la orden generada por cada cliente
		transaccion.ejecutarConsultas(ordenDAO.insertar(orden));
		//Recorre el array list con las transacciones sql a ejecutar 
		for (String consultaSQL : listaSQL){
			transaccion.ejecutarConsultas(consultaSQL);
		}
		
		//Limpia el array de instrucciones sql porque ya fueron generadas
		listaSQL = new ArrayList<String>();		
		//Genera instrucciones de pago si hay
		generarProcesosDePago();
		//Recorre el array list con las transacciones sql a ejecutar 
		for (String consultaSQL : listaSQL){
			transaccion.ejecutarConsultas(consultaSQL);
		}		
	}
	
	/**Recorre las instrucciones del cliente en búsca de la instrucción de pago necesitada. Si la encuentra
	 * posición el cursor del dataSet de instrucciones en el registro necesitado
	 * 
	 * @param idInstruccion instrucción de pago a buscar en el dataSet
	 * @return retorna verdadero si la instrucción fue encontrada
	 * @throws Exception lanza una excepción si hay un error 
	 */
	private boolean ubicarInstruccionPago(int idInstruccion) throws Exception{
		boolean encontrado = false;
		if (instruccionesDePago != null){
			instruccionesDePago.first();
			while (instruccionesDePago.next()){
				if (instruccionesDePago.getValue("tipo_instruccion_id").equals(String.valueOf(idInstruccion))){
					encontrado = true;
					break;
				}
			}
		}
		return encontrado;
	}
	
	/**Carga en el dataSet instruccionesDePago las instrucciones de pago que tenga el cliente
	 * 
	 * @throws Exception lanza una excepción si hay un error
	 */
	private void cargarInstruccionesDePago() throws Exception{
		//busca la cuenta asociada al cliente para el pago de cupones
		cuentaClienteDao.listarCuentaUsoCliente(String.valueOf(orden.getIdCliente()),UsoCuentas.PAGO_DE_CUPONES);
		if(cuentaClienteDao.getDataSet().count()>0){
			instruccionesDePago = cuentaClienteDao.getDataSet();
		} else {
			//Lo coloca a null para evitar leer instrucciones de otro cliente
			instruccionesDePago = null;
		}
	}
	
	/**Busca las instrucciones de pago asociadas al cliente y genera los procesos correspondiente
	 * 
	 * @throws Exception lanza una excepción en caso de error
	 */
	private void generarProcesosDePago() throws Exception{
		
		//Recorremos todos los montos y buscamos como se desean pagar según las instrucciones del cliente
		cargarInstruccionesDePago(); //Carga las instrucciones de pago del cliente
		
		String monedaBase = ""; //Moneda de la operación
		
		//Verificamos si hay instrucciones de pago
		if (instruccionesDePago != null){		
			HashMap<String,BigDecimal> montos = new HashMap<String,BigDecimal>(); //Agrupa montos por la moneda
			HashMap<String,ArrayList> idOperaciones = new HashMap<String,ArrayList>(); //Agrupa las operaciones id por los montos			
			BigDecimal montoMapa = null;
			String idMonedaOperacion = "";
			Iterator iteradorOperaciones = null;			
			
			//Recorre las operaciones y acumula los montos de la operación según la moneda
			for (OrdenOperacion ordenOperacion: orden.getOperacion()){
				//Buscamos si el monto está en el hashmap por la moneda leída 
				if (montos.containsKey(ordenOperacion.getIdMoneda())){
					//Capturamos el monto almacenado y le sumamos en valor leído
					montoMapa = montos.get(ordenOperacion.getIdMoneda());
					//Adiciono el id de la operación si existe
					idOperaciones.get(ordenOperacion.getIdMoneda()).add(String.valueOf(ordenOperacion.getIdOperacion()));
				} else {
					montoMapa = new BigDecimal(0);
					ArrayList<String> listaId = new ArrayList<String>();
					listaId.add(String.valueOf(ordenOperacion.getIdOperacion()));
					idOperaciones.put(ordenOperacion.getIdMoneda(), listaId);
				}
				montos.put(ordenOperacion.getIdMoneda(), montoMapa.add(ordenOperacion.getMontoOperacion()));			
			}
			
			Iterator iterator = montos.keySet().iterator();				
			
			while (iterator.hasNext()){
				ProcesoGestion procesoGestion = new ProcesoGestion();
				//Se setea al inicio el usuario
				procesoGestion.setUsuarioId(usuarioId);
				procesoGestion.setClientId(orden.getIdCliente());
				
				InstruccionesPago instruccionesPago = new InstruccionesPago();
				instruccionesPago.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
				instruccionesPago.setFechaOperacion(fechaHoy);
				instruccionesPago.setFechaValor(fechaHoy);
				
				
				//Obtenemos el id de la moneda de la operación
				idMonedaOperacion = (String)iterator.next();
				monedaBase = idMonedaOperacion;
				//Verifica si el pago del cupón sólo es en bolívares
				if (idMonedaOperacion.equals(idMonedaLocal)){
					//Busca la instrucción de pago en cuenta nacional
					if (ubicarInstruccionPago(TipoInstruccion.CUENTA_NACIONAL)){
						Logger.info(this,"Instruccion Cuenta Nacional");
						instruccionesPago.setCedulaBeneficiario(instruccionesDePago.getValue("cedula_beneficiario"));
						instruccionesPago.setNombrebeneficiario(instruccionesDePago.getValue("nombre_beneficiario"));
						instruccionesPago.setFechaValor(fechaHoy);
						instruccionesPago.setClienteCuentaNumero(instruccionesDePago.getValue("ctecta_numero"));
						instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_NACIONAL);
						instruccionesPago.setMonedaId(idMonedaOperacion);
						instruccionesPago.setMontoInstruccion(montos.get(idMonedaOperacion));	
						instruccionesPago.setTasaCambio(new BigDecimal(1));
						
						//Ejecuta las instrucciones en la transacción
						procesoGestion.agregarInstruccion(instruccionesPago);
												
						//Recorre las operaciones y guarda su id
						iteradorOperaciones = idOperaciones.get(idMonedaOperacion).iterator();
						while (iteradorOperaciones.hasNext()){
							procesoGestion.agregarOperacion(Long.parseLong((String)iteradorOperaciones.next()));
						}						
					}
				} else {
					//Se verifica si el cliente quiere el pago de los bonos en moneda extranjera
					if (ubicarInstruccionPago(TipoInstruccion.CUENTA_SWIFT)){
						Logger.info(this,"Instruccion Swift");
						instruccionesPago.setCedulaBeneficiario(instruccionesDePago.getValue("cedula_beneficiario"));
						instruccionesPago.setNombrebeneficiario(instruccionesDePago.getValue("nombre_beneficiario"));						
						instruccionesPago.setClienteCuentaNumero(instruccionesDePago.getValue("ctecta_numero"));								
						instruccionesPago.setClienteCuentaNombre(instruccionesDePago.getValue("ctecta_nombre"));
						instruccionesPago.setCtectaBcoctaBco(instruccionesDePago.getValue("ctecta_bcocta_bco"));
						
						instruccionesPago.setCtectaBcoctaDireccion(instruccionesDePago.getValue("ctecta_bcocta_direccion"));
						instruccionesPago.setCtectaBcoctaSwift(instruccionesDePago.getValue("ctecta_bcocta_swift"));
						instruccionesPago.setCtectaBcoctaBic(instruccionesDePago.getValue("ctecta_bcocta_bic"));
						instruccionesPago.setCtectaBcoctaTelefono(instruccionesDePago.getValue("ctecta_bcocta_telefono"));
						instruccionesPago.setCtectaBcoctaAba(instruccionesDePago.getValue("ctecta_bcocta_aba"));
						instruccionesPago.setCtectaBcointBco(instruccionesDePago.getValue("ctecta_bcoint_bco"));
						instruccionesPago.setCtectaBcointDireccion(instruccionesDePago.getValue("ctecta_bcoint_direccion"));
						instruccionesPago.setCtectaBcointSwift(instruccionesDePago.getValue("ctecta_bcoint_swift"));
						instruccionesPago.setCtectaBcointBic(instruccionesDePago.getValue("ctecta_bcoint_bic"));
						instruccionesPago.setCtectaBcointTelefono(instruccionesDePago.getValue("ctecta_bcoint_telefono"));
						instruccionesPago.setCtectaBcointAba(instruccionesDePago.getValue("ctecta_bcoint_aba"));
						instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_SWIFT);
						instruccionesPago.setMonedaId(idMonedaOperacion);
						instruccionesPago.setMontoInstruccion(montos.get(idMonedaOperacion));						
						instruccionesPago.setFechaValor(this.getFechaValor(com.bdv.infi.logic.interfaces.FechaValor.TRANSFERENCIA_SWIFT));
						instruccionesPago.setTasaCambio(new BigDecimal(conversionMontos.obtenerTasaDeCambioCierre(idMonedaOperacion).getTasaCambio()));
						//Ejecuta las instrucciones en la transacción
						procesoGestion.agregarInstruccion(instruccionesPago);
												
						//Recorre las operaciones y guarda su id
						iteradorOperaciones = idOperaciones.get(idMonedaOperacion).iterator();
						while (iteradorOperaciones.hasNext()){
							procesoGestion.agregarOperacion(Long.parseLong((String)iteradorOperaciones.next()));
						}						
					} else if (ubicarInstruccionPago(TipoInstruccion.CHEQUE)){
						Logger.info(this,"Instruccion Cheque");
						//Se verifica si el cliente quiere el pago de cupones con cheque
						//Se generan las instrucciones de pago														
						instruccionesPago.setCedulaBeneficiario(instruccionesDePago.getValue("cedula_beneficiario"));
						instruccionesPago.setNombrebeneficiario(instruccionesDePago.getValue("nombre_beneficiario"));
						//Buscar la fecha valor
						instruccionesPago.setMonedaId(idMonedaOperacion);
						instruccionesPago.setMontoInstruccion(montos.get(idMonedaOperacion));
						instruccionesPago.setFechaValor(this.getFechaValor(com.bdv.infi.logic.interfaces.FechaValor.PAGO_CHEQUES));
						instruccionesPago.setTipoInstruccionId(TipoInstruccion.CHEQUE);
						instruccionesPago.setMonedaId(idMonedaOperacion);
						instruccionesPago.setTasaCambio(new BigDecimal(conversionMontos.obtenerTasaDeCambioCierre(idMonedaOperacion).getTasaCambio()));
						
						//Ejecuta las instrucciones en la transacción
						procesoGestion.agregarInstruccion(instruccionesPago);						
						
						//Recorre las operaciones y guarda su id
						iteradorOperaciones = idOperaciones.get(idMonedaOperacion).iterator();
						while (iteradorOperaciones.hasNext()){
							procesoGestion.agregarOperacion(Long.parseLong((String)iteradorOperaciones.next()));
						}
						
					} else if (ubicarInstruccionPago(TipoInstruccion.OPERACION_DE_CAMBIO)){
						Logger.info(this,"Instruccion Operacion de cambio");
						//El cliente desea que le paguen los cupones en bolívares
						instruccionesPago.setCedulaBeneficiario(instruccionesDePago.getValue("cedula_beneficiario"));
						instruccionesPago.setNombrebeneficiario(instruccionesDePago.getValue("nombre_beneficiario"));
						instruccionesPago.setMonedaId(idMonedaLocal);
						
						//Se convirte el monto
						BigDecimal montoCambio = new BigDecimal(conversionMontos.obtenerTasaDeCambioCierre(idMonedaOperacion).getTasaCambioCompra()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
						montoCambio = montoCambio.multiply(montos.get(idMonedaOperacion)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
						
						instruccionesPago.setMontoInstruccionNoConversion(montos.get(idMonedaOperacion));
						instruccionesPago.setMontoInstruccion(montoCambio);
						instruccionesPago.setClienteCuentaNumero(instruccionesDePago.getValue("ctecta_numero"));
						instruccionesPago.setFechaValor(this.getFechaValor(com.bdv.infi.logic.interfaces.FechaValor.OPERACION_CAMBIO));
						instruccionesPago.setTipoInstruccionId(TipoInstruccion.OPERACION_DE_CAMBIO);
						instruccionesPago.setTasaCambio(new BigDecimal(conversionMontos.obtenerTasaDeCambioCierre(idMonedaOperacion).getTasaCambioCompra()));
						procesoGestion.setClientId(Long.parseLong(idCliente));
						
						//Ejecuta las instrucciones en la transacción
						procesoGestion.agregarInstruccion(instruccionesPago);
						
						//Recorre las operaciones y guarda su id
						iteradorOperaciones = idOperaciones.get(idMonedaOperacion).iterator();
						while (iteradorOperaciones.hasNext()){
							procesoGestion.agregarOperacion(Long.parseLong((String)iteradorOperaciones.next()));
						}
					}
				}
				//Adiciona los sql generados
				this.listaSQL.addAll(gestionPagoDAO.insertarProceso(procesoGestion,null,this.usuarioNM,this.direccionIp,monedaBase,orden.getIdOrden()));
			}
		}
	}	
	
	/**Busca la fecha valor para los diferentes pagos*/
	private Date getFechaValor(int idFechaValor) throws Exception {
		if (fechasValores.containsKey(String.valueOf(idFechaValor))){
			return fechasValores.get(idFechaValor);
		} else {
			FechaValor fechaValor = fechaValorDAO.listar(idFechaValor);
			fechasValores.put(String.valueOf(idFechaValor), fechaValor.getFechaValor());
			return fechaValor.getFechaValor();
		}			
	}
}