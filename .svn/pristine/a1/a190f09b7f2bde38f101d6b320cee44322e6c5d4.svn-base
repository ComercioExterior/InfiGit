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
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.PagoCuponesDao;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SecsDao;
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
import com.bdv.infi.data.ProcesoGestion;

/**Genera las amortizaciones que se deben cancelar a los clientes
 * @author nvisbal*/
public class GenerarAmortizaciones implements Runnable {

	DataSource dataSource = null;
	int usuarioId;
	String idCliente = "";
	HashMap<String,Date> listaTitulos = new HashMap<String,Date>();
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
	CustodiaDAO custodiaDAO = null;
	ConversionMontos conversionMontos = null;
	Date fechaHoy = new Date();
	Date fechaInicio = null;	
	Date fechaFin = null;
	String idTitulo = null;
	/* Primero creamos el proceso */
	Proceso proceso = null;	
	double cantidad = 0;
		
	/**
	 * Constructor de la clase para la generación de las amortizaciones
	 * @param datasource datasource usado para la conexión a base de datos
	 * @param usuarioId usuario que efectúa la generación de amortización
	 * @param fechaInicio fecha de inicio del pago
	 * @param fechaFin fecha fin del pago
	 * @param idTitulo id del título para calcular la amortización 
	 */
	public GenerarAmortizaciones(DataSource datasource,int usuarioId, Date fechaInicio, Date fechaFin, String idTitulo) {
		this.dataSource = datasource;
		this.usuarioId=usuarioId;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.idTitulo = idTitulo;
	}
	
	Logger logger;
	
	public void run(){
/*
		transaccion=new com.bdv.infi.dao.Transaccion(this.dataSource);
		ProcesosDAO procesoDao = null;
		
		try {
			procesoDao = new ProcesosDAO(this.dataSource);
			CalculoAmortizaciones calculoAmortizaciones = new CalculoAmortizaciones(this.dataSource);
			SecsDao secsDao = new SecsDao(this.dataSource);
			custodiaDAO = new CustodiaDAO(this.dataSource);
			ordenDAO = new OrdenDAO(this.dataSource);
			monedaDAO = new MonedaDAO(this.dataSource); 
			cuentaClienteDao=new ClienteCuentasDAO(this.dataSource);
			gestionPagoDAO=new GestionPagoDAO(this.dataSource);
			conversionMontos = new ConversionMontos(this.dataSource);
			ProcesoGestion procesoGestion = new ProcesoGestion();
			//Se setea al inicio el usuario
			procesoGestion.setUsuarioId(usuarioId);
			
			long idConsulta;
			DataSet consulta = null;		
			
			logger.info(this,"Verificando si hay un proceso de generación de amortización...");
			
			procesoDao.listarPorTransaccionActiva(TransaccionNegocio.CUSTODIA_AMORTIZACION);		
			
			if (procesoDao.getDataSet().count()>0){
				logger.info(this,"Ya existe un proceso ejecutandose para la generación de amortización");
				throw new Exception("Ya existe un proceso ejecutandose para la generación de amortización");
			}
			
			logger.info(this,"Iniciado el proceso de generación de amortizaciones por el usuario que posee id " + usuarioId);
			
			proceso = new Proceso();
			proceso.setTransaId(TransaccionNegocio.CUSTODIA_AMORTIZACION);
			proceso.setUsuarioId(usuarioId);
			proceso.setFechaInicio(new Date());
			proceso.setFechaValor(new Date());
			
			//Se ejecuta el proceso sin transacción para registrar el inicio del mismo
			db.exec(this.dataSource, procesoDao.insertar(proceso));
			idEjecucion = proceso.getEjecucionId();			
						
			if (fechaInicio!=null){
				//Efectúa el cálculo de los cupones
				idConsulta = calculoAmortizaciones.CalcularAmortizaciones(fechaInicio, fechaFin, usuarioId, null,this.idTitulo);
				
				//Busca los datos generados de la consulta
				transaccion.begin();			
				CalculoMesDAO calculoMesDao = new CalculoMesDAO(transaccion);
				calculoMesDao.listarDetalles(idConsulta);
				
				//Recorre cada uno de los registros para generar las ordenes
				consulta = calculoMesDao.getDataSet();
				
				if (consulta.count()>0){
				
					consulta.first();
					SimpleDateFormat formatear	=new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);				
					while (consulta.next()){
						cantidad = Double.parseDouble(consulta.getValue("cantidad"));						
						logger.info(this,"Procesando cálculo de amortización para el cliente " + consulta.getValue("client_id") + ", título " + consulta.getValue("titulo_id"));
						
						if (idCliente.equals("")){
							idCliente = consulta.getValue("client_id");
							generarOrden();
						} else if (!idCliente.equals(consulta.getValue("client_id"))){
							guardarOrden(cantidad);
							idCliente = consulta.getValue("client_id");							
							generarOrden();							
						}
						
						/*Arma las operaciones en base a los cálculos generados y a las instrucciones de pago que
						posea el cliente*/
		/*
						OrdenOperacion ordenOperacion 	= new OrdenOperacion();	
						ordenOperacion.setTasa(new BigDecimal(consulta.getValue("tasa_monto")));
						ordenOperacion.setIdTitulo(consulta.getValue("titulo_id"));
						ordenOperacion.setMontoOperacion(new BigDecimal(consulta.getValue("monto_operacion")));
						ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_CUPON));
						ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
						ordenOperacion.setIdMoneda(consulta.getValue("moneda_id"));
						ordenOperacion.setFechaAplicar(formatear.parse(consulta.getValue("fecha_fin_pago_cupon")));
						ordenOperacion.setFechaInicioCP(formatear.parse(consulta.getValue("fecha_inicio_pago_cupon")));
						ordenOperacion.setFechaFinCP(formatear.parse(consulta.getValue("fecha_fin_pago_cupon")));
						
						//Agrega el título a la lista
						listaTitulos.put(consulta.getValue("titulo_id"),ordenOperacion.getFechaFinCP());
						
						//Actualiza la última fecha de pago de amortización del título que se está calculando
						listaSQL.add(custodiaDAO.modificarPagoAmortizacion(orden.getIdCliente(),ordenOperacion.getIdTitulo(),ordenOperacion.getFechaFinCP(),ordenOperacion.getMontoOperacion(),false));
									
						ordenOperacion.setTipoTransaccionFinanc(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
						orden.agregarOperacion(ordenOperacion);
					}
					guardarOrden(cantidad);			
				} else {
					throw new Exception("No hay detalle para las amortizaciones");
				}
			}
			transaccion.end();
			logger.info(this,"Proceso de generación de amortización finalizado con éxito");			
		} catch (Exception e) {
			try{
				transaccion.rollback();
				logger.error(this,"Error en el proceso de generación de amortizaciones. " + e.getMessage());			
			} catch (Exception ex){	
			}
			proceso.setDescripcionError(e.getMessage());
		} finally {
			proceso.setFechaFin(new Date());
			if (proceso.getDescripcionError() == null) {
				proceso.setDescripcionError("");
			}

			try {
				db.exec(this.dataSource, procesoDao.modificar(proceso));
				procesoDao.cerrarConexion();
			} catch (Exception e) {
				logger.error(this,"Error efectuando modificación al proceso. " + e.getMessage());				
			}
		}*/							
	}

	public void execute() throws Exception {

	}
		
	/**Genera una orden nueva*/
	private void generarOrden(){
		orden=new Orden();	
		orden.setIdEjecucion(idEjecucion);
		orden.setIdCliente(Long.parseLong(idCliente));
		orden.setIdTransaccion(TransaccionNegocio.CUSTODIA_AMORTIZACION);
		orden.setStatus(StatusOrden.REGISTRADA);
		orden.setFechaValor(new Date());
		orden.setIdEjecucion(proceso.getEjecucionId());
		
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
	private void guardarOrden(double cantidad) throws Exception{
		
		//Arma los títulos que deben ir con la orden
		Iterator iterator = listaTitulos.keySet().iterator();
		String idTitulo = null;
		while (iterator.hasNext()){
			idTitulo = (String) iterator.next();			
			OrdenTitulo ordenTitulo = new OrdenTitulo();
			ordenTitulo.setTituloId(idTitulo);
			ordenTitulo.setUnidades(cantidad);
			orden.agregarOrdenTitulo(ordenTitulo);			
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
		cuentaClienteDao.listarCuentaUsoCliente(String.valueOf(orden.getIdCliente()),UsoCuentas.PAGO_DE_CAPITAL);
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
		
		//Verificamos si hay instrucciones de pago
		if (instruccionesDePago != null){		
			HashMap<String,BigDecimal> montos = new HashMap<String,BigDecimal>(); //Agrupa montos por la moneda
			HashMap<String,ArrayList> idOperaciones = new HashMap<String,ArrayList>(); //Agrupa las operaciones id por los montos			
			BigDecimal montoMapa = null;
			String idMonedaOperacion = "";
			String idMonedaLocal = monedaDAO.listarIdMonedaLocal();
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
				//Verifica si el pago del cupón sólo es en bolívares
				if (idMonedaOperacion.equals(idMonedaLocal)){
					//Busca la instrucción de pago en cuenta nacional
					if (ubicarInstruccionPago(TipoInstruccion.CUENTA_NACIONAL)){
						instruccionesPago.setCedulaBeneficiario(instruccionesDePago.getValue("cedula_beneficiario"));
						instruccionesPago.setNombrebeneficiario(instruccionesDePago.getValue("nombre_beneficiario"));
						instruccionesPago.setFechaOperacion(fechaHoy);
						instruccionesPago.setClienteCuentaNumero(instruccionesDePago.getValue("ctecta_numero"));
						instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_NACIONAL);
						instruccionesPago.setMonedaId(idMonedaOperacion);
						instruccionesPago.setMontoInstruccion(montos.get(idMonedaOperacion));						
						
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
						
						//Ejecuta las instrucciones en la transacción
						procesoGestion.agregarInstruccion(instruccionesPago);
												
						//Recorre las operaciones y guarda su id
						iteradorOperaciones = idOperaciones.get(idMonedaOperacion).iterator();
						while (iteradorOperaciones.hasNext()){
							procesoGestion.agregarOperacion(Long.parseLong((String)iteradorOperaciones.next()));
						}						
					} else if (ubicarInstruccionPago(TipoInstruccion.CHEQUE)){
						//Se verifica si el cliente quiere el pago de cupones con cheque
						//Se generan las instrucciones de pago														
						instruccionesPago.setCedulaBeneficiario(instruccionesDePago.getValue("cedula_beneficiario"));
						instruccionesPago.setNombrebeneficiario(instruccionesDePago.getValue("nombre_beneficiario"));
						instruccionesPago.setFechaOperacion(new Date());
						//Buscar la fecha valor
						instruccionesPago.setMonedaId(idMonedaOperacion);
						instruccionesPago.setMontoInstruccion(montos.get(idMonedaOperacion));
						instruccionesPago.setFechaValor(fechaHoy);
						instruccionesPago.setTipoInstruccionId(TipoInstruccion.CHEQUE);
						instruccionesPago.setMonedaId(idMonedaOperacion);						
						
						//Ejecuta las instrucciones en la transacción
						procesoGestion.agregarInstruccion(instruccionesPago);						
						
						//Recorre las operaciones y guarda su id
						iteradorOperaciones = idOperaciones.get(idMonedaOperacion).iterator();
						while (iteradorOperaciones.hasNext()){
							procesoGestion.agregarOperacion(Long.parseLong((String)iteradorOperaciones.next()));
						}
						
					} else if (ubicarInstruccionPago(TipoInstruccion.OPERACION_DE_CAMBIO)){
						//El cliente desea que le paguen los cupones en bolívares
						instruccionesPago.setCedulaBeneficiario("");
						instruccionesPago.setNombrebeneficiario("");
						instruccionesPago.setFechaOperacion(fechaHoy);
						instruccionesPago.setMonedaId(idMonedaOperacion);
						instruccionesPago.setMontoInstruccion(montos.get(idMonedaOperacion));
						instruccionesPago.setFechaValor(fechaHoy);
						instruccionesPago.setTipoInstruccionId(TipoInstruccion.OPERACION_DE_CAMBIO);
						instruccionesPago.setMonedaId(idMonedaLocal);
						instruccionesPago.setTasaCambio(new BigDecimal(conversionMontos.obtenerTasaDeCambioCierre(idMonedaOperacion).getTasaCambio()));
														
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
			}
		}
	}
}