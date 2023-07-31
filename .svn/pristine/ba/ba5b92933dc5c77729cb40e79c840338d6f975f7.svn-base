package com.bdv.infi_toma_orden.dao;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import models.bcv.menudeo.ErroresMenudeo;

import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.bcv.service.AutorizacionPortBindingStub;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenRequisito;
import com.bdv.infi.data.TransaccionFija;
import com.bdv.infi.logic.ProcesarDocumentos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.logic.interfaz_altair.consult.ManejoDeClientes;
import com.bdv.infi.util.DB;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.data.CampoDinamico;
import com.bdv.infi_toma_orden.data.OrdenTitulo;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;

import criptografia.TripleDes;



/** 
 * Clase usada para la lógica de inserción, modificación y recuperación de las ordenes en la base de datos
 */
public class TomaOrdenDAO extends GenericoDAO {

	/**
	 * Arreglos para persistencia
	 */
	private int pos = 0;
	private Object [][] data;
	private ServletContext contexto = null;
	private String ip = null;
	private boolean existeCuentaComis = false;
	private boolean existeCuentaCupones = false;
	private boolean existeCuentaAmortiz = false;
	private long idCliente = 0;
	private boolean vehiculoTomadorBDV = true;
	private Orden ordenVehiculo = new Orden();
	private Logger logger = Logger.getLogger(TomaOrdenDAO.class);
	DataSet _credenciales = new DataSet();
	CredencialesDAO credencialesDAO = new CredencialesDAO(dso);
	
	/**
	 * Constructor de la clase
	 * @param nombreDataSource : nombre que se obtiene del ambiente de ejecucion de los WebService
	 * @param dso : DataSource instanciado por clases que se ejecutan en ambientes Web
	 */
	public TomaOrdenDAO (String nombreDataSource, DataSource dso, ServletContext contexto, String ip) {
		super(nombreDataSource, dso);
		this.ip = ip;
		this.contexto = contexto;
	}
	
	/**
	 * inserta una orden en la base de datos
	 * @param tomaOrden a ser insertada
	 * @throws Exception lanza una excepción si hay un error
	*/
	public boolean insertar(TomaOrdenSimulada beanTOSimulada) throws Throwable, Exception{
		//NM29643 Se añade variable para validacion de subasta divisas personal
		String cuentaAbono="";
		boolean subDivisasPersonal = false;
		String insertsInstCtaNacUSD[] = null;
//System.out.println("TomaOrdenSimulada Objetoooooo----\n"+beanTOSimulada);
		if(beanTOSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)) subDivisasPersonal = true;
//System.out.println("INSERTAAAAAAAAARRRRRRRRRR--------subDivisasPersonal: "+subDivisasPersonal);		
		DateFormat df = new SimpleDateFormat("d-MMM-yyyy");
		long secuencia = 0;
		//	1.- 	Sequence para Orden
		try {
			secuencia = DB.dbGetSequence(dso, ConstantesGenerales.SECUENCIA_ORDENES, new Integer(0));
		} catch (Exception e) {

			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error generando secuencia para el n&uacute;mero de orden");
		}
		//Agrega el código a la orden
		beanTOSimulada.setIdOrden(secuencia);
		
		//Nm29643 - INFI_TTS_443 08/04/2014: Se inserta instruccion de pago cta nacional en dolares para SICAD II
		if(beanTOSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL) || beanTOSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
			ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(dso);
			beanTOSimulada.getInstPagoCtaNacDolares().setIdOrden(beanTOSimulada.getIdOrden());
			beanTOSimulada.getInstPagoCtaNacDolares().setIdInstruccion(clienteCuentasDAO.getMaxId()+1);
			cuentaAbono= ConstantesGenerales.ABONO_CUENTA_NACIONAL;
			insertsInstCtaNacUSD = clienteCuentasDAO.insertarClienteCuentas(beanTOSimulada.getInstPagoCtaNacDolares());
		}
		
		//	2.- 	Sequence para OrdenTitulo
		for (int i=0; i<beanTOSimulada.getListaOrdenTitulo().size(); i++) {
			OrdenTitulo beanOrdenTitulo = (OrdenTitulo)beanTOSimulada.getListaOrdenTitulo().get(i);
			beanOrdenTitulo.setIdOrden(beanTOSimulada.getIdOrden());
		}
		
		//	3.- 	Sequence para OrdenOperacion	
		try {
			secuencia = DB.dbGetSequence(dso, ConstantesGenerales.SECUENCIA_ORDENES_OPERACION, new Integer(beanTOSimulada.getListaOperaciones().size()));
		} catch (Exception e) {
			throw new Exception("Error generando secuencia para n&uacute;meros de operaciones");
		}			
		for (int i=0; i<beanTOSimulada.getListaOperaciones().size(); i++) {
//System.out.println("LISTA OPERACIONES SIZE: "+beanTOSimulada.getListaOperaciones().size());
			OrdenOperacion beanOperacion = (OrdenOperacion)beanTOSimulada.getListaOperaciones().get(i);
			beanOperacion.setIdOrden(beanTOSimulada.getIdOrden());
			beanOperacion.setIdOperacion(secuencia);
			//NM29643 Modificacion para subasta divisas personal
			//TODO VErificar
			if(subDivisasPersonal){
				beanOperacion.setMontoOperacion(beanTOSimulada.getMontoTotal());
				beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
				beanOperacion.setNumeroRetencion(beanTOSimulada.getNroRetencion());
			}
			++secuencia;
		}
		
		
		
		//	4.-		Armar lista de campos para la Orden
		StringBuffer sqlBase = new StringBuffer();
		sqlBase.append("INSERT INTO INFI_TB_204_ORDENES (");
		sqlBase.append("ordene_id, uniinv_id, client_id, bloter_id, empres_id, transa_id, ordsta_id, ordene_ped_fe_orden, ordene_ped_fe_valor, ");
		sqlBase.append("ordene_ped_monto, ordene_ped_precio, ordene_ped_total_pend, ordene_ped_total, ordene_ped_int_caidos, ordene_ped_comisiones, ordene_financiado, ordene_ped_in_bdv, ");
		sqlBase.append(" ordene_usr_terminal, ordene_veh_tom, ordene_veh_col, ordene_veh_rec, ordene_usr_nombre, ");
		sqlBase.append(" ordene_comision_emisor, ordene_tasa_pool, ordene_ganancia_red, ordene_comision_oficina, ordene_comision_operacion, ctecta_numero, ORDENE_USR_SUCURSAL, ");
		sqlBase.append(" codigo_id, sector_id, concepto_id, ordene_fe_ult_act, tipo_producto_id, ordene_observacion, ordene_tasa_cambio, moneda_id, CTA_ABONO, ORDENE_ID_BCV, ORDENE_ESTATUS_BCV, ");
		sqlBase.append(" ORDENE_ADJ_MONTO");
		sqlBase.append(") VALUES (");		
		String parametros = "?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? ";
		int maximoParametros = (parametros.length()/3)+3;
		
		//	5.-		Armar el sql de insertar la Orden	
		StringBuffer sql = sqlBase;
		sql.append(parametros);
		sql.append(")");
		
		idCliente = new Long(beanTOSimulada.getIdCliente());
		//ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(nombreDataSource, dso);
		//procesarDocumentos.procesar(beanTOSimulada);

		
		//	6.-		Armar el arreglo de datos para enviar al metodo de persistencia
		int cant;
		//NM29643 - INFI_TTS_443 16-04-14: No se crea el titulo de la subasta para SICAD II Red Comercial
		if(beanTOSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
			cant = 1 + beanTOSimulada.getListaOperaciones().size() + beanTOSimulada.getListaCamposDinamicos().size() + beanTOSimulada.getOrdenDataExt().size();
		}else{
			cant = 1 + beanTOSimulada.getListaOrdenTitulo().size() + beanTOSimulada.getListaOperaciones().size() + beanTOSimulada.getListaCamposDinamicos().size() + beanTOSimulada.getOrdenDataExt().size();
		}
			
		if (beanTOSimulada.isIndicaInventario()){
			++cant;
		}
		//---Buscar cuenta para el cobro de comisones del cliente
		if(existeCuentaParaCliente(beanTOSimulada.getIdCliente(), UsoCuentas.COBRO_DE_COMISIONES)){
			existeCuentaComis = true;			
		}else{
			//++cant;//si no existe reservar espacio para el query de insercion de la cuenta
		}

		//---Buscar cuenta para el pago de cupones del cliente
		if(existeCuentaParaCliente(beanTOSimulada.getIdCliente(), UsoCuentas.PAGO_DE_CUPONES)){
			existeCuentaCupones = true;			
		}else{
			//++cant;//si no existe reservar espacio para el query de insercion de la cuenta
		}
		
		//---Buscar cuenta para el pago de amortizaciones del cliente
		if(existeCuentaParaCliente(beanTOSimulada.getIdCliente(), UsoCuentas.PAGO_DE_CAPITAL)){
			existeCuentaAmortiz = true;			
		}else{
			//++cant;//si no existe reservar espacio para el query de insercion de la cuenta
		}
		
		//--Verificar si se insertará una instruccion de pago
		if(beanTOSimulada.getInstruccionPagoRecompra()!=null && beanTOSimulada.getInstruccionPagoRecompra().size()>0){
			//Por cada instruccion reservar expacio (2) para la insercion en tabla de cuentas
			// y en tabla relacionada con la orden (INFI_TB_217_CTES_CUENTAS_ORD)
			cant = cant + (beanTOSimulada.getInstruccionPagoRecompra().size());
		}
		
		if(beanTOSimulada.getInternoBDV() == 0){//--SI NO ES CARTERA PROPIA--
			VehiculoDAO vehDAO = new VehiculoDAO(dso);
			//---Verificar si el vehiculo Tomador es BDV		
			if(!vehDAO.vehiculoEsBDV(beanTOSimulada.getVehiculoTomador())){
				vehiculoTomadorBDV = false;
				//obtener la orden para el vehiculo (en caso de no estar creada se insertara en base de datos
				ordenVehiculo = this.crearBuscarOrdenVehiculoUI(beanTOSimulada.getVehiculoTomador(), beanTOSimulada.getIpTerminal(), beanTOSimulada.getIdUnidadInversion(), beanTOSimulada.getNombreUsuario(), beanTOSimulada.getTasaCambio().doubleValue(), beanTOSimulada.getNumeroOficina());
				
				//agregar operaciones de credito a la orden del vehiculo. Por cada operacion de debito de la orden a guardar se creara una de credito para el vehiculo tomador
				this.crearOperacionesOrdenVehiculo(ordenVehiculo, beanTOSimulada);
				//reservar espacio para insercion de cada operacion de credito agregada para el vehiculo tomador
				cant = cant + ordenVehiculo.getOperacion().size();
				
				//Reservar ids de secuencia para operaciones de credito de la orden del vehiculo
				//asignar ID_OPERACION
				//	3.- 	Sequence para OrdenOperacion	
				long secuenc = 0;
				try {
					secuenc = DB.dbGetSequence(dso, ConstantesGenerales.SECUENCIA_ORDENES_OPERACION, new Integer(ordenVehiculo.getOperacion().size()));
				} catch (Exception exc) {

					logger.error(exc.getMessage()+ Utilitario.stackTraceException(exc));
					throw new Exception("Error generando secuencia para n&uacute;meros de operaciones de veh&iacute;culo.");
				}	
				if(!ordenVehiculo.getOperacion().isEmpty()){
					for (int i=0; i<ordenVehiculo.getOperacion().size(); i++) {
						OrdenOperacion operacionVehiculo = (OrdenOperacion)ordenVehiculo.getOperacion().get(i);
						operacionVehiculo.setIdOperacion(secuenc);
						++secuenc;
					}
				}			
			}
		}// FIN DE SI NO ES CARTERA PROPIA . . . .
		
		//--Verificar si se insertarán requisitos asociados a la orden
		if(beanTOSimulada.getOrdenRequisitos()!=null && beanTOSimulada.getOrdenRequisitos().size()>0){
			//reservar espacio por cada requisito
			cant = cant + (beanTOSimulada.getOrdenRequisitos().size());			
			//reservar secuencias para los requisitos de la orden
			long secuenc = 0;
			try {
				secuenc = DB.dbGetSequence(dso, ConstantesGenerales.SECUENCIA_ORDENES_REQUISITOS, new Integer(beanTOSimulada.getOrdenRequisitos().size()));
			} catch (Exception e) {
				throw new Exception("Error generando los n&uacute;meros de secuencia para requisitos");
			}		
			//Asignar secuencias (primary key)
			for (int i=0; i<beanTOSimulada.getOrdenRequisitos().size();i++){
				OrdenRequisito ordenRequisito = (OrdenRequisito)beanTOSimulada.getOrdenRequisitos().get(i);
				ordenRequisito.setId(Integer.parseInt(String.valueOf(secuenc)));
				secuenc++;				
			}			
		}
		//-----------------------------------------------------------------------------------

//System.out.println("Antes llenado arreglo data");		
		data = new Object[cant][maximoParametros];
		
		//	7.-		Almacena la orden en la base de datos
		Integer iAux = 0;
		if (beanTOSimulada.isIndicaFinanciada())
			iAux = 1;
		data[pos][0] = sql.toString();
		data[pos][1] = maximoParametros;
		data[pos][2] = new Long(beanTOSimulada.getIdOrden());
		data[pos][3] = new Long(beanTOSimulada.getIdUnidadInversion());
		data[pos][4] = new Long(beanTOSimulada.getIdCliente());
		data[pos][5] = beanTOSimulada.getIdBlotter();
		data[pos][6] = beanTOSimulada.getIdEmpresa();
		data[pos][7] = beanTOSimulada.getIdTransaccion();
		data[pos][8] = beanTOSimulada.getIdStatusOrden();		
		//NM26659_25022015 TTS_491 Inclusion Hora:Minutos:Segundo en campo ORDENE_PED_FE_ORDEN
		long msFA = new Date().getTime();
		java.sql.Timestamp fechaActualTimestamp = new java.sql.Timestamp(msFA);		
		data[pos][9] = fechaActualTimestamp; 		
		//data[pos][9] = new Date();//beanTOSimulada.getFechaOrden();		
		
		long msFV = beanTOSimulada.getFechaValor().getTime();
		java.sql.Timestamp fechaValorTimestamp = new java.sql.Timestamp(msFV);
		data[pos][10] = fechaValorTimestamp;
		
		//NM32454 SIMADI TAQUILLA
		//SE AGREGA EL MONTO PEDIDO EN EFECTIVO A EL INSERT DE LA TABLA ORDENES
		data[pos][11] = beanTOSimulada.getMontoPedido().add(beanTOSimulada.getMontoPedidoEfectivo());
		
		data[pos][12] = beanTOSimulada.getPrecioCompra().setScale(6,BigDecimal.ROUND_HALF_EVEN);
		data[pos][13] = beanTOSimulada.getMontoFinanciado();
		data[pos][14] = beanTOSimulada.getMontoTotal();
		data[pos][15] = beanTOSimulada.getMontoInteresCaidos();
		data[pos][16] = beanTOSimulada.getMontoComisiones();
		data[pos][17] = iAux;
		data[pos][18] = beanTOSimulada.getInternoBDV();
		//AQUI añadidos
		data[pos][19] = beanTOSimulada.getIpTerminal();
		data[pos][20] = beanTOSimulada.getVehiculoTomador();
		data[pos][21] = beanTOSimulada.getVehiculoColocador();
		data[pos][22] = beanTOSimulada.getVehiculoRecompra();
		data[pos][23] = beanTOSimulada.getNombreUsuario();
		
		data[pos][24] = beanTOSimulada.getComisionEmisor();
		data[pos][25] = beanTOSimulada.getTasaPool();
		data[pos][26] = beanTOSimulada.getGananciaRed();
		data[pos][27] = beanTOSimulada.getComisionOficina().setScale(6,BigDecimal.ROUND_HALF_EVEN);
		data[pos][28] = beanTOSimulada.getComisionOperacion();
		data[pos][29] = beanTOSimulada.getNumeroCuentaCliente(); 
		data[pos][30] = beanTOSimulada.getNumeroOficina();
		data[pos][31] = beanTOSimulada.getActividadEconomica();
		data[pos][32] = beanTOSimulada.getSectorProductivo();
		data[pos][33] = beanTOSimulada.getConcepto();
		
		/*if (beanTOSimulada.getFechaValorRecompra()!=null && !beanTOSimulada.getFechaValorRecompra().equals("")){
			DateFormat formatter ; 
			Date dateValor ; 
	        formatter = new SimpleDateFormat("dd-MM-yy");
	        String dateFechaRecompra="";
	        dateValor = formatter.parse(beanTOSimulada.getFechaValorRecompra().toString());
	        dateFechaRecompra = df.format(dateValor);
			data[pos][34] = dateFechaRecompra;
		}else{
			data[pos][34] = "";
		}*/
		//NM29643 Modificacion para que el campo ordene_fe_ult_act se llene con la fecha actual en lugar de la fecha valor recompra
		Calendar calendario = GregorianCalendar.getInstance();
		Date fecha = calendario.getTime();
		//SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA4);
		//data[pos][34] = sdf.format(fecha);
		data[pos][34] = fecha;
		
		data[pos][35] = beanTOSimulada.getTipoProductoId();
		data[pos][36] = beanTOSimulada.getObservaciones();
		data[pos][37] = beanTOSimulada.getTasaCambio();
		data[pos][38] = beanTOSimulada.getMonedaId();
		data[pos][39] = cuentaAbono;
		
		++pos;
		
		// 8. Buscar los titulos, tabla 206
		//NM29643 - INFI_TTS_443 16-04-14: No se crea el titulo de la subasta para SICAD II Red Comercial
		if(!beanTOSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
			armarTitulos(beanTOSimulada);
		}
//System.out.println("ARMAR TITULOSSSSSSSSSSSSSSSSS");
		
		// 9. Buscar las Operaciones tabla, 207
		// Se arman las inserciones de cuentas a clientes.
		armarOperaciones(beanTOSimulada);
//System.out.println("ARMAR OPERACIONESSSSSSSSSSS");			
		// 10. Arma los campos dinámicos, tabla 205
		armarCamposDinamicos(beanTOSimulada);
//System.out.println("ARMAR CAMPOS DINAMICOSSSSSSSSS");		
		//-- armar sentencias para data extendida, tabla 212
		this.armarDataExtendida(beanTOSimulada);
//System.out.println("ARMAR DATA EXTENDIDAAAAA");
		//-- armar sentencias para los requisitos
		this.armarOrdenRequisitos(beanTOSimulada);
//System.out.println("ARMAR ORDEN REQUISITOSSSSSSSSSS");
		
		//arma los documentos
		//armarDocumentos(beanTOSimulada);
				
		// 	11.-	Actualizar Unidad de Inversion si es inventario
		if (beanTOSimulada.isIndicaInventario()) {
			sql = new StringBuffer();
			sql.append("UPDATE INFI_TB_106_UNIDAD_INVERSION set undinv_umi_inv_disponible = undinv_umi_inv_disponible - ? ");
			sql.append("where undinv_id = ? ");
			data[pos][0] = sql.toString();
			data[pos][1] = new Integer(4);		
			data[pos][2] = beanTOSimulada.getMontoPedido();
			data[pos][3] = beanTOSimulada.getIdUnidadInversion();
			++pos;
		}
		
		// 12.---Insertar instrucciones de pago
		if(beanTOSimulada.getInstruccionPagoRecompra()!=null && beanTOSimulada.getInstruccionPagoRecompra().size()>0){
			ArrayList<CuentaCliente> instruccionesPago = beanTOSimulada.getInstruccionPagoRecompra();
			for(int j=0; j<instruccionesPago.size();j++){
				CuentaCliente cuentaCliente = (CuentaCliente)instruccionesPago.get(j);
				//setear id orden al objeto para la instruccion de pago
				cuentaCliente.setIdOrden(beanTOSimulada.getIdOrden());
				this.armarInsercionUsoDeCuenta(cuentaCliente, beanTOSimulada,false);				
			}
		}
		//NM19643 - INFI_TTS_443 08/04/2014: Insercion de Instruccion Pago (cta nac USD) para SICAD II
		ArrayList<String> arrInserts = new ArrayList<String>();
		if(beanTOSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL) || beanTOSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
			
			if(insertsInstCtaNacUSD!=null && insertsInstCtaNacUSD.length>=2){
				
				arrInserts.add(insertsInstCtaNacUSD[0]);
				arrInserts.add(insertsInstCtaNacUSD[1]);
			}
		}
		//13.-----Agregar operaciones de credito a vehiculo si éste NO es BDV, tabla 207
		if(!vehiculoTomadorBDV){
			this.armarInsertOperacionesVehiculo(ordenVehiculo);
		}
		
		
		//13.1 VERIFICAR SI LA UNIDAD DE INVERSION ESTA CONTEMPLADA PARA ENVIAR OPERACIONES AL BCV
		//LPEREZ SIMADI TAQUILLA 17/08/2015
		String codigoAprobacionBCV = "0";
		String ordeneEstatusBCV    = "0";
		String codigoAprobacionBCVEfe = "0";
		String ordeneEstatusBCVEfe    = "0";
	    DataSet _instrumentoFinanciero;
		InstrumentoFinancieroDAO instFinancieroDAO = new InstrumentoFinancieroDAO(dso);
		instFinancieroDAO.listarPorId(beanTOSimulada.getInstrumentoId());
		_instrumentoFinanciero = instFinancieroDAO.getDataSet();
		String tipoInstrumentoFinanciero;
		if(_instrumentoFinanciero.count() > 0){
			_instrumentoFinanciero.next();
			tipoInstrumentoFinanciero = _instrumentoFinanciero.getValue("insfin_forma_orden");
		}else {
			logger.error("No se han conseguido los datos del instrumento financiero id: "+beanTOSimulada.getInstrumentoId()+". ");
			throw new Exception("No se han conseguido datos del instrumento financiero: "+beanTOSimulada.getInstrumentoId()+". "); 
		}
		
		//SE VERIFICA SI EL PARAMETRO BCV EN LINEA SE ENCUENTRA ACTIVO
		String paranBCV = ParametrosDAO.listarParametros(ConstantesGenerales.TRANSF_BCV_ONLINE_TAQ,dso);
		String observacion = "";
		String observacionEfe = "";
		String telefono = "";
		String email= "";
		String codCliente = "";				
		String tipperPersona = "";
		String cedRifCliente = "";
		String nombreCliente = "";
		BigDecimal tsCambio  = beanTOSimulada.getTasaCambio();
		String codigoTipoMovimientoAnuEfe = ConstantesGenerales.TIPO_DE_MOVIMIENTO_ANU_TAQ_EFE;
		String codigoTipoMovimientoAnuEle = ConstantesGenerales.TIPO_DE_MOVIMIENTO_ANU_TAQ_ELE;
		
		
		
		//REQUERIMIENTO SIMADI TAQUILLA NM32454 LUIS PEREZ
		ClienteDAO clienteDAO = new ClienteDAO(dso);
		DataSet _cliente = new DataSet();
		clienteDAO.listar(beanTOSimulada.getIdCliente(), null, 0, null, 0, null);
		_cliente = clienteDAO.getDataSet();

		if(_cliente.next()){
			telefono = _cliente.getValue("client_telefono");
			email    = _cliente.getValue("client_correo_electronico");
			nombreCliente = _cliente.getValue("client_nombre");
			tipperPersona = _cliente.getValue("tipper_id");
			cedRifCliente = _cliente.getValue("client_cedrif");
			
			//SE COMPLETA CON 0 A LA IZQUIERDA YA QUE ES EL FORMATO QUE SOLICITA EL BCV
			cedRifCliente = Utilitario.completarCaracterIzquierda(cedRifCliente.toString(), 8, "0");
			codCliente    = tipperPersona + cedRifCliente;
			
			if(email != null && email.contains("@")){
				//NM26659 17/03/2015 RESOLUCION INCIDENCIA CALIDAD
				email=email.toLowerCase().trim();							
			}else { //EMAIL ES NULL
				email = "sinemail@mail.com";
			}
			
			if(telefono == null){
				telefono = "05006425283";
			}else {
				telefono = Utilitario.depurarString(telefono.trim());
			}
		}else {
			logger.error("No se han conseguido los datos del cliente id: "+beanTOSimulada.getIdCliente()+". No sera enviado al BCV.");
			throw new Exception("No se han conseguido los datos del cliente id: "+beanTOSimulada.getIdCliente()+". No sera enviado al BCV."); 
		}

		//EL PARAMETRO CONTRA BCV SE ENCUENTRA ACTIVO O EN NOTIFICACION VALORES: 1 = ONLINE O 2 = NOTIFICACION 
		if(paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_ONLINE_TAQ) || paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_NOTI_TAQ)){
			//SE REALIZA EL ENVIO DE OPERACIONES AL BCV
			if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){
				String  codMonedaIso =  ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
				Long coMotivoOperacion = (long) 1;
				String nuCtaconvenio20 = beanTOSimulada.getNumeroCuentaNacDolaresCliente();
				String[] envioOperacionBCV;

				//SE HACE LA LLAMADA AL WS PARA EL EFECTIVO EN EL CASO QUE APLIQUE
				if(beanTOSimulada.getMontoPedidoEfectivo().doubleValue() > 0){
					String codTipoMovimiento="";
					if(tipperPersona.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PN)){
						codTipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_PN_VEN_TAQ_EFE;
					}else if (tipperPersona.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PE) ){
						codTipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EX_VEN_TAQ_EFE;
					}
					
					envioOperacionBCV = this.enviarOperacionBCV(codTipoMovimiento, codCliente, nombreCliente, beanTOSimulada.getMontoPedidoEfectivo(), tsCambio, 
			                codMonedaIso, beanTOSimulada.getMontoPedidoEfectivo(), coMotivoOperacion, nuCtaconvenio20, 
			                telefono, email);
				
					//SE VERIFICA EL RESULTADO DEL ENVIO DE LA OPERACION
					 //LA OPERACION FUE RECHAZADA POR BCV U OCURRIO UN ERROR NO CONTROLADO. NO SE REALIZA NINGUNA OPERACION ADICIONAL PARAMETRO EN VALIDACION
					if((envioOperacionBCV[0].equals("0") || envioOperacionBCV[0].equals("2")) && paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_ONLINE_TAQ)){
						throw new Exception("Error enviando la orden al BCV: " +envioOperacionBCV[1]);
				    //LA OPERACION FUE RECHAZADA POR BCV CONTINUA LA OPERACION PARAMETRO EN NOTIFICACION
					}else if(envioOperacionBCV[0].equals("0") && paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_NOTI_TAQ)) {
						logger.error("Orden ha sido rechazada por BCV. La operacion continua parametro configurado como solo notificacion. Rechazo de BCV: "+envioOperacionBCV[1]);
						codigoAprobacionBCVEfe = "0";
						ordeneEstatusBCVEfe    = "2"; //ESTATUS ORDEN BCV RECHAZADA
						observacionEfe         = envioOperacionBCV[1];
					//OCURRIO UN ERROR NO CONTROLADO SE MARCA LA ORDEN COMO NO ENVIADA. CONTINUA LA OPERACION PARAMETRO EN NOTIFICACION 
					}else if(envioOperacionBCV[0].equals("2") && paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_NOTI_TAQ)) {
						codigoAprobacionBCVEfe = "0";
						ordeneEstatusBCVEfe    = "0"; //ESTATUS ORDEN BCV NO ENVIADA
						observacionEfe         = envioOperacionBCV[1];
					}
					//LA OPERACION FUE APROBADA POR EL BCV
					else { 
						codigoAprobacionBCVEfe = envioOperacionBCV[1];
						ordeneEstatusBCVEfe    = "1"; //ESTATUS ORDEN BCV APROBADA
						observacionEfe      = "Envio a BCV ejecutado con exito";
					}
				}
				
				//SE HACE LA LLAMADA AL WS PARA EL ELECTRONICO EN EL CASO QUE APLIQUE
				if(beanTOSimulada.getMontoPedido().doubleValue() > 0){
					String codTipoMovimiento="";
					if(tipperPersona.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PN)){
						codTipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_PN_VEN_TAQ_ELE;
					}else if (tipperPersona.equalsIgnoreCase(ConstantesGenerales.TIPPER_ID_PE) ){
						codTipoMovimiento    = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EX_VEN_TAQ_ELE;
					}
					
					envioOperacionBCV = this.enviarOperacionBCV(codTipoMovimiento, codCliente, nombreCliente, beanTOSimulada.getMontoPedido(), tsCambio, 
			                codMonedaIso, beanTOSimulada.getMontoPedido(), coMotivoOperacion, nuCtaconvenio20, 
			                telefono, email);
				
					//SE VERIFICA EL RESULTADO DEL ENVIO DE LA OPERACION
					 //LA OPERACION FUE RECHAZADA POR BCV U OCURRIO UN ERROR NO CONTROLADO NO SE REALIZA NINGUNA OPERACION ADICIONAL PARAMETRO EN VALIDACION
					if((envioOperacionBCV[0].equals("0") || envioOperacionBCV[0].equals("2")) && paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_ONLINE_TAQ)){
						anularOperacionBCV(codigoAprobacionBCVEfe, codigoTipoMovimientoAnuEfe, "Fallo interno en sistema del banco");
						throw new Exception("Error enviando la orden al BCV: " +envioOperacionBCV[1]);
				    //LA OPERACION FUE RECHAZADA POR BCV CONTINUA LA OPERACION PARAMETRO EN NOTIFICACION
					}else if(envioOperacionBCV[0].equals("0") && paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_NOTI_TAQ)) {
						logger.error("Orden ha sido rechazada por BCV. La operacion continua parametro configurado como solo notificacion. Rechazo de BCV: "+envioOperacionBCV[1]);
						codigoAprobacionBCV = "0";
						ordeneEstatusBCV    = "2"; //ESTATUS ORDEN BCV RECHAZADA
						observacion         = envioOperacionBCV[1];
						data[0][36]         = observacion;
					//OCURRIO UN ERROR NO CONTROLADO SE MARCA LA ORDEN COMO NO ENVIADA. CONTINUA LA OPERACION PARAMETRO EN NOTIFICACION 
					}else if(envioOperacionBCV[0].equals("2") && paranBCV.equalsIgnoreCase(ConstantesGenerales.PARAM_TRANSF_BCV_NOTI_TAQ)) {
						codigoAprobacionBCV = "0";
						ordeneEstatusBCV    = "0"; //ESTATUS ORDEN BCV NO ENVIADA
						observacion         = envioOperacionBCV[1];	
						data[0][36]         = observacion;
					//LA OPERACION FUE APROBADA POR EL BCV
					}else { 
						codigoAprobacionBCV = envioOperacionBCV[1];
						ordeneEstatusBCV    = "1"; //ESTATUS ORDEN BCV APROBADA
						data[0][36]         = "Envio a BCV ejecutado con exito";
					}
				}
			}
		}
		
		
		//NM32454 SIMADI TAQUILLA
		//SE AGREGA MONTO ADJUDICADO PARA EL MONTO EN ELECTRONICO PARA SIMADI TAQUILLA
		if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){
			data[0][42] = beanTOSimulada.getMontoPedido();
		}else {
			data[0][42] = "";
		}
		
		//SE AGREGA EL CODIGO DE APROBACION BCV AL INSERT DE LA ORDEN EN LA POSICION 40 DEL ARREGLO
		data[0][40] = codigoAprobacionBCV;
		data[0][41] = ordeneEstatusBCV; 

		// 	14.- Aplicar persistencia
		//System.out.println("ANTES DE EXECbATCHuPDATE");		
		try {
			DB.execBatchUpdate(dso, data, beanTOSimulada, arrInserts);
		//System.out.println("DESPUES DE EXECbATCHuPDATE");	
		} catch (Exception e) {

			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error insertando la orden: " +e);
		}
		
		//--Buscar documentos y aplicar transacciones financieras solo cuando NO es cartera propia
		if(beanTOSimulada.getInternoBDV()!=1){			
			boolean documentosInsertados = true;
			//Busca el objeto orden para buscar los documentos asociados a la transacci&oacute;n de negocio
			OrdenDAO ordenDAO = new OrdenDAO(dso);		
			Orden orden = new Orden();
			boolean ordenListada = false; //indicador de orden listada correctamente
			//Intento de guardar documentos asociados a la orden ingresada
			try {
				orden = ordenDAO.listarOrden(beanTOSimulada.getIdOrden());
				ordenListada = true;
				ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(dso);
				procesarDocumentos.procesar(orden,contexto, ip);
				ordenDAO.insertarDocumentos(orden);
				ArrayList<com.bdv.infi.data.OrdenDocumento> listaDocumentos = new ArrayList<com.bdv.infi.data.OrdenDocumento>();
				listaDocumentos = orden.getDocumentos();
				beanTOSimulada.setListaDocumentos(listaDocumentos);	
			} catch (Exception e) {
				documentosInsertados = false;
				//SI SE REALIZO EL ENVIO DE LA OPERACION AL BCV SE ANULA LA MISMA ELECTRONICO
				if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) && !ordeneEstatusBCV.equals("2") && beanTOSimulada.getMontoPedido().doubleValue() > 0){
					anularOperacionBCV(codigoAprobacionBCV, codigoTipoMovimientoAnuEle, "Problema al insertar operaciones en base de datos del Banco de Venezuela");
				}
				
				//SE ANULA EL EFECTIVO
				if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) && !ordeneEstatusBCVEfe.equals("2") && beanTOSimulada.getMontoPedidoEfectivo().doubleValue() > 0){
					anularOperacionBCV(codigoAprobacionBCV, codigoTipoMovimientoAnuEfe, "Problema al insertar operaciones en base de datos del Banco de Venezuela");
				}
				
				//Eliminar la orden en caso de falla al insertar los documentos			
				eliminarOrden(beanTOSimulada, ordenVehiculo);
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
				if(ordenListada)
					throw new Exception("Error insertando los documentos");	
				else
					throw new Exception("Error obteniendo la orden que intentaba ingresarse");	
			}catch(Throwable t){					
				documentosInsertados = false;
				//Eliminar la orden en caso de falla al insertar los documentos				
				eliminarOrden(beanTOSimulada, ordenVehiculo);
				
				//SI SE REALIZO EL ENVIO DE LA OPERACION AL BCV SE ANULA LA MISMA ELECTRONICO
				if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) && !ordeneEstatusBCV.equals("2") && beanTOSimulada.getMontoPedido().doubleValue() > 0){
					anularOperacionBCV(codigoAprobacionBCV, codigoTipoMovimientoAnuEle, "Problema al insertar operaciones en base de datos del Banco de Venezuela");
				}
				
				//SE ANULA EL EFECTIVO
				if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) && !ordeneEstatusBCVEfe.equals("2") && beanTOSimulada.getMontoPedidoEfectivo().doubleValue() > 0){
					anularOperacionBCV(codigoAprobacionBCV, codigoTipoMovimientoAnuEfe, "Problema al insertar operaciones en base de datos del Banco de Venezuela");
				}
				logger.error(t.getMessage()+ Utilitario.stackTraceException(t));
				if(ordenListada)
					throw new Exception("Error insertando los documentos");	
				else
					throw new Exception("Error obteniendo la orden que intentaba ingresarse");	
			}

			//si se ingresan los documentos, aplicar operaciones hacia altair
			if(documentosInsertados){
				logger.info("DOCUMENTOS INSERTADOS... INTENTAR APLICAR OPERACIONES DE LA ORDEN NRO: " +beanTOSimulada.getIdOrden() +"...");
				//NM29643 Modificacion para no aplicar operaciones para SUBASTA DIVISAS PERSONAL
				if(!beanTOSimulada.getTipoProductoId().equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){ //Si no es Subasta Divisas PERSONAL
					//System.out.println("ENTRA A ALTAIRRRRRRRRRRR");
					FactoryAltair factoryAltair = new FactoryAltair(dso,contexto, true);
					factoryAltair.nombreUsuario = beanTOSimulada.getNombreUsuario();
					factoryAltair.ipTerminal    = beanTOSimulada.getIpTerminal();
					try {				
						//--Aplicar operaciones de la orden
						logger.info("LLAMANDO AL PROCESO DE ENVIO DE OPERACIONES DEL CLIENTE A ALTAIR....");
						factoryAltair.aplicarOrdenes(beanTOSimulada.getListaOperaciones()); 	
						//-------------------------------------------------------------------
						//--Aplicar operaciones de la orden del vehiculo si es el caso
						if(!vehiculoTomadorBDV){
							if(ordenVehiculo.getOperacion().size()>0){
								logger.info("LLAMANDO AL PROCESO DE ENVIO DE OPERACIONES DEL VEHICULO A ALTAIR....");
								factoryAltair.aplicarOrdenes(ordenVehiculo.getOperacion()); 	
							}
						}
						logger.info("PROCESO DE VERIFICACION Y ENVIO DE OPERACIONES A ALTAIR CULMINADO EXITOSAMENTE...");
					}catch(Throwable t){
						logger.error("ERROR OCURRIDO AL ENVIAR OPERACIONES A ALTAIR...CONTINUAR..");
						logger.error(t.getMessage()+ Utilitario.stackTraceException(t));	
					}
					
					//--SI ALGUNAS DE LAS OPERACIONES FUE RECHAZADA O ESTA EN ESPERA YO NO INSERTO EN LA TABLA SIMADI TAQUILLA
					//NM32454 SIMADI TAQUILLA
					Boolean insertTaquilla = false;
					if(!operacionesRechazadas(beanTOSimulada.getListaOperaciones()) || !operacionesEnEspera(beanTOSimulada.getListaOperaciones())){
						//NM32454 SIMADI TAQUILLA
						//SE INSERTA EN TABLA NUEVA DE SIMADI TAQUILLA INFI_TB_232_ORDENES_TAQUILLA - SE REALIZA ABONO DIRECTO EN CUENTA EN DOLARES DEL CLIENTE
						
						if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){
							//StringBuffer sqlInsert = new StringBuffer();
							//sqlInsert.append("INSERT INTO INFI_TB_232_ORDENES_TAQUILLA ");
							
							String ordeneID  = String.valueOf(orden.getIdOrden()); 
							String clienteID = String.valueOf(orden.getIdCliente());
							String tipOper   = ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC;
							String estatus   = ConstantesGenerales.STATUS_TAQ_NO_LIQUIDADA;
							String undinvID  = String.valueOf(orden.getIdUnidadInversion());
							String insfID    = beanTOSimulada.getInstrumentoId();
							String tipoDivisa = "1"; //DOLLAR
							String vigencia   = "1"; //VIGENTE
							String fechaRegistro = "SYSDATE";
							String fechaValor    = "SYSDATE";
							BigDecimal montoTotalBolivaresEfectivo = beanTOSimulada.getMontoComisionesEfectivo().add(beanTOSimulada.getMontoInversionEfectivo());
							String ctaDivisa = beanTOSimulada.getNumeroCuentaNacDolaresCliente() == null ? "0" : beanTOSimulada.getNumeroCuentaNacDolaresCliente();  
							String ctaBs = beanTOSimulada.getNumeroCuentaCliente() == null ? "0" : beanTOSimulada.getNumeroCuentaCliente();
							BigDecimal montoComDivisa = new BigDecimal(0);
							String numeroRetencionCapital ="0";
							String codigoOperacionCapital="0";
							String numeroRetencionComision="0";
							String codigoOperacionComision="0";
							Boolean efectivoOperacion = false;
							//SE BUSCA EL NUMERO DE RETENCION DE EFECTIVO (CAPITAL Y COMISION) SI ES BLANCO COLOCO SETEO EL VALOR 0
							for (OrdenOperacion ordenOperacion : beanTOSimulada.getListaOperaciones()) {
								//SI ES CAPITAL 
								if(ordenOperacion.getTipoOperacion().equals(ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC) && ordenOperacion.getInComision() == 0 ){
									numeroRetencionCapital = ordenOperacion.getNumeroRetencion() == "" ? "0" : ordenOperacion.getNumeroRetencion();
									codigoOperacionCapital = ordenOperacion.getCodigoOperacion() == "" ? "0" : ordenOperacion.getCodigoOperacion();
									efectivoOperacion = true;
								}
								//SI ES COMISION 
								else if(ordenOperacion.getTipoOperacion().equals(ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC) && ordenOperacion.getInComision() == 1 ){
									numeroRetencionComision = ordenOperacion.getNumeroRetencion() == "" ? "0" : ordenOperacion.getNumeroRetencion();
									codigoOperacionComision = ordenOperacion.getCodigoOperacion() == "" ? "0" : ordenOperacion.getCodigoOperacion();
									efectivoOperacion = true;
								}
							}
							
							if(efectivoOperacion == true){
								insertTaquilla = this.insertTaquilla(ordeneID, clienteID, cedRifCliente, tipperPersona, tipOper, numeroRetencionCapital, codigoOperacionCapital, numeroRetencionComision, codigoOperacionComision , nombreCliente, 
										undinvID, insfID, beanTOSimulada.getMontoPedidoEfectivo(), montoComDivisa, beanTOSimulada.getMontoPedidoEfectivo(), beanTOSimulada.getMontoPedidoEfectivo(), beanTOSimulada.getMontoInversionEfectivo(), 
										beanTOSimulada.getMontoComisionesEfectivo(), montoTotalBolivaresEfectivo, montoTotalBolivaresEfectivo, tsCambio, ctaDivisa, ctaBs, telefono, email, fechaRegistro, fechaValor, 
										observacionEfe, estatus, vigencia, tipoDivisa, ordeneEstatusBCVEfe, codigoAprobacionBCVEfe);
							}else {
								logger.info("NO SE INSERTA EN LA TABLA TAQUILLA PORQUE NO EXISTEN OPERACIONES DE EFECTIVO EN ESTA TRANSACCION ");
								insertTaquilla = true;
							}
													
						}else {
							insertTaquilla = true;
						} 
					}else {
						logger.info("EXISTEN OPERACIONES RECHAZADAS NO SE VA A REALIZAR EL INSERT EN LA TABLA INFI_TB_232_ORDENES_TAQUILLA ");
					}
					
					//--Si alguna de las operaciones es rechazada o quedo en espera... O HUBO UN PROBLEMA EN EL INSERT DE LA TABLA TAQUILLA eliminar orden
					if(     operacionesRechazadas(beanTOSimulada.getListaOperaciones())                   || 
							((!vehiculoTomadorBDV) && operacionesRechazadas(ordenVehiculo.getOperacion())) || 
							operacionesEnEspera(beanTOSimulada.getListaOperaciones())                      || 
							operacionesEnEspera(ordenVehiculo.getOperacion())                              ||
							!insertTaquilla
					   ){
						//SI SE REALIZO EL ENVIO DE LA OPERACION AL BCV SE ANULA LA MISMA ELECTRONICO
						if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) && !ordeneEstatusBCV.equals("2") && beanTOSimulada.getMontoPedido().doubleValue() > 0){
							anularOperacionBCV(codigoAprobacionBCV, codigoTipoMovimientoAnuEle, "Problema al insertar operaciones en base de datos del Banco de Venezuela");
						}
						
						//SE ANULA EL EFECTIVO
						if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) && !ordeneEstatusBCVEfe.equals("2") && beanTOSimulada.getMontoPedidoEfectivo().doubleValue() > 0){
							anularOperacionBCV(codigoAprobacionBCV, codigoTipoMovimientoAnuEfe, "Problema al insertar operaciones en base de datos del Banco de Venezuela");
						}
						
						//Reverso de operaciones del vehiculo en caso de ser rechazada alguna
						if((!vehiculoTomadorBDV && operacionesRechazadas(ordenVehiculo.getOperacion())) || !insertTaquilla){
							if(insertTaquilla == false && operacionesRechazadas(ordenVehiculo.getOperacion())){
								logger.info("NO SE PUDO INSERTAR EN LA TABLA TAQUILLA Y EXISTEN OPERACIONES RECHAZADAS ");
							}else if (operacionesRechazadas(ordenVehiculo.getOperacion())) {
								logger.info("HAY OPERACIONES DEL VEHICULO RECHAZADAS... ");
							}else if (insertTaquilla == false){
								logger.info("NO SE PUDO INSERTAR EN LA TABLA TAQUILLA ");
							}
							
							try{
								logger.info("VERIFICANDO SI SE DEBE HACER REVERSO OPERACIONES VEHICULO...");
								//NM32454 SIMADI TAQUILLA COMPORTAMIENTO ORIGINAL
								if(!tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){
									//--Hacer el reverso a todas la operaciones que hayan sido aplicadas en altair						
									hacerReversoOperaciones(ordenVehiculo.getOperacion(), factoryAltair);
								}else {
									logger.info("ELIMINANDO ORDEN NRO: " +beanTOSimulada.getIdOrden());
									hacerReversoOperaciones(beanTOSimulada.getListaOperaciones(), factoryAltair);
									//NM32454 SIMADI TAQUILLA
									eliminarOrden(beanTOSimulada, ordenVehiculo);
									//SI SE REALIZO EL ENVIO DE LA OPERACION AL BCV SE ANULA LA MISMA
								}
							} catch (Throwable e){								
								logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
								throw new Exception("Error aplicando el reverso de las operaciones financieras del veh&iacute;culo. " + factoryAltair.getError());
							}
						}
						
						//Hacer reverso de operaciones de la orden en caso 
						//de rechazo de operaciones del cliente o del vehiculo 
						logger.info("HAY OPERACIONES DEL CLIENTE RECHAZADAS O EN ESPERA...");
						try{						
							logger.info("VERIFICANDO SI SE DEBE HACER REVERSO OPERACIONES DEL CLIENTE...");
							if(operacionesRechazadas(beanTOSimulada.getListaOperaciones()) || !insertTaquilla){
								//NM32454 SIMADI TAQUILLA 24/08/2015
								//SE HACE RESERSO SI EXISTEN OPERACIONES RECHAZADAS O SI FALLO EL INSERT DE TABLA DE TAQUILLA
								//--Hacer el reverso a todas la operaciones que hayan sido aplicadas en altair						
								hacerReversoOperaciones(beanTOSimulada.getListaOperaciones(), factoryAltair);
								try{ //SE SUBE EL TRAY CATCH A ESTE IF PARA NO ELIMINAR LA ORDEN DE MANERA ERRADA NM32545
									//SI SE REALIZO EL ENVIO DE LA OPERACION AL BCV SE ANULA LA MISMA
									//if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){ comentado por NM25287 08/06/2016 Este if no estaba anteriormente, al colocarlo afecta a la incidencia ITS-3110 Orden con cuenta incorrecta (por caso PRUEBAS BGE2001: MONTO EXCEDE EL DISPONIBLE )
										logger.info("ELIMINANDO ORDEN NRO: " +beanTOSimulada.getIdOrden());
										// Eliminar la orden insertada con anterioridad						
										eliminarOrden(beanTOSimulada, ordenVehiculo);
									//}
								} catch (Throwable e){
									logger.error("ERROR ELIMINANDO ORDEN NRO: "+beanTOSimulada.getIdOrden()+" "+e.getMessage()+ Utilitario.stackTraceException(e));
									throw new Exception("Error eliminando orden número " + beanTOSimulada.getIdOrden());
								}
							}else {
								logger.info("NO HAY OPERACIONES RECHAZADAS DEL CLIENTE.. NO SE APLICARA REVERSO.."); 
							}
						} catch (Throwable e){ //SE MODIFICA CAMBIANDO EL EXEPCION POR THROWABLE NM32454
							//Se cambia el status de la orden ya registrada
							//SOPORTE ITS-636 Verificacion que el codigo no permita la generacion de carta mandato al contener un error en toma de orde
							OrdenDAO ordenDao = new OrdenDAO(this.dso);
							Orden ordenStatus = new Orden();
							ordenStatus.setIdOrden(beanTOSimulada.getIdOrden());
							ordenStatus.setStatus(StatusOrden.PENDIENTE);
							db.exec(this.dso, ordenDao.modificarStatus(ordenStatus));
							logger.error("Error aplicando el reverso de las operaciones financieras del cliente..."+e.getMessage()+ Utilitario.stackTraceException(e));
							
							//SI SE REALIZO EL ENVIO DE LA OPERACION AL BCV SE ANULA LA MISMA
							if(tipoInstrumentoFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) && !ordeneEstatusBCV.equals("2")){
								logger.info("ELIMINANDO ORDEN NRO: " +beanTOSimulada.getIdOrden());
								// Eliminar la orden insertada con anterioridad						
								eliminarOrden(beanTOSimulada, ordenVehiculo);
							}
							throw new Exception("Error aplicando el reverso de las operaciones financieras del cliente. " + e.getMessage());						
						}
						logger.error("OPERACIONES RECHAZADAS..LANZANDO EXCEPCION DE ERROR DE OPERACIONES A ALTAIR..."); 
						throw new Exception("Error aplicando las operaciones en ALTAIR. " + factoryAltair.getError());	
					}
				}//No es Subasta Divisas PERSONAL
			}//Documentos insertados
		}			
     	return true;	
	}
	
	/**
	 * Prepara las sentencias de insercion de las operaciones de credito a la orden del veh&iacute;culo tomador
	 * @param ordenVehiculo
	 * @param beanTOSimulada
	 */
	private void armarInsertOperacionesVehiculo(Orden ordenVehiculo) {
		
		//Busca llas operaciones asociadas a la orden del vehiculo
		ArrayList<OrdenOperacion> listaOperaciones = ordenVehiculo.getOperacion();
			
		//Objeto para el query de operaciones
		StringBuffer sql = new StringBuffer();
		String parametros = "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		sql.append("INSERT INTO INFI_TB_207_ORDENES_OPERACION (");
		sql.append("ordene_operacion_id, ordene_id, status_operacion, ctecta_numero,trnf_tipo, aplica_reverso, monto_operacion, tasa, ");
		sql.append("fecha_aplicar, in_comision, moneda_id, serial,codigo_operacion,numero_retencion, in_comision_invariable, operacion_nombre, ordene_relac_operacion_id, trnfin_id ");		
		sql.append(") VALUES(").append(parametros).append(")");
		
		if(!listaOperaciones.isEmpty()){
			//Se recorren los títulos y se arman los parametros para la persistencia	
			for (int i=0; i<listaOperaciones.size(); i++) {
				OrdenOperacion beanOperacion = (OrdenOperacion)listaOperaciones.get(i);
				data[pos][0] = sql.toString();
				data[pos][1] = ((parametros.length()/3)+3);
				data[pos][2] = beanOperacion.getIdOperacion();
				data[pos][3] = ordenVehiculo.getIdOrden(); 			
				data[pos][4] = beanOperacion.getStatusOperacion();
				data[pos][5] = beanOperacion.getNumeroCuenta();
				data[pos][6] = beanOperacion.getTipoTransaccionFinanc();
				
				if ( beanOperacion.isAplicaReverso())
					data[pos][7] = new Integer(1);
				else
					data[pos][7] = new Integer(0);
				
				data[pos][8] = beanOperacion.getMontoOperacion();			
	
				data[pos][9] = beanOperacion.getTasa();
				data[pos][10] = beanOperacion.getFechaAplicar();
				
				if (beanOperacion.getInComision()==1)
					data[pos][11] = new Integer(1);
				else
					data[pos][11] = new Integer(0);
				
				data[pos][12] = beanOperacion.getIdMoneda();
					
				data[pos][13] = beanOperacion.getSerialContable();
				data[pos][14] = beanOperacion.getCodigoOperacion();
				data[pos][15] = beanOperacion.getNumeroRetencion();	
				data[pos][16] = beanOperacion.getIndicadorComisionInvariable();	
				data[pos][17] = beanOperacion.getNombreOperacion();	
				data[pos][18] = beanOperacion.getIdOperacionRelacion();	
				data[pos][19] = beanOperacion.getIdTransaccionFinanciera();	
				++pos;		
				
			}	
		}
	}
	
	/**
	 * NM32454 SIMADI TAQUILLA 24/08/2015
	 * Prepara las sentencias de insercion de las operaciones de credito a la orden del veh&iacute;culo tomador
	 * @param ordenVehiculo
	 * @param beanTOSimulada
	 */
	private boolean insertTaquilla(String ordeneId, String clienteID, String clienteCedRif, String tipperID, String tipOper,String numeroRetencionCapital, String codigoOperacionCapital, String numeroRetencionComision, 
								   String codigoOperacionComision, String clientNombre, String undinvID, String insfID, BigDecimal montoCapDivisa, BigDecimal montoComDivisa, BigDecimal montoAdjuDivisa, BigDecimal montoTotalDivisa, 
			                       BigDecimal montoCapBs, BigDecimal montoComBs, BigDecimal montoAdjuBs, BigDecimal montoTotalBs, BigDecimal tasaCambio,String ctaDivisa,String ctaBs,String telefono,String correo,  
			                       String fechaRegistro, String fechaValor, String observacion, String estatus, String vigencia, String tipoDivisa, String ordeneEstatusBCV, String ordeneIDBCV) {
		
		observacion = observacion.replaceAll("\n", " "); 
		if(observacion.length() > 1000){
			observacion = observacion.replaceAll("\n", " "); 
			observacion = observacion.substring(0, 999);
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_232_ORDENES_TAQUILLA ( ");
		sql.append("ORDENE_ID, ORDENE_TAQ_CLIENT_ID, ORDENE_TAQ_CLIENT_CERIF, ORDENE_TAQ_TIPPER_ID, ORDENE_TAQ_TIPO_OPERA, ORDENE_TAQ_NRO_RETEN_CAP, ");
		sql.append("ORDENE_TAQ_CODIGO_OPERA_CAP, ORDENE_TAQ_NRO_RETEN_COM, ORDENE_TAQ_CODIGO_OPERA_COM, ORDENE_TAQ_CLIENT_NOMBRE, ORDENE_TAQ_UNDINV_ID, ORDENE_TAQ_INST_FINA_ID, ");
		sql.append("ORDENE_TAQ_MONTO_CAP_DIVISA, ORDENE_TAQ_MONTO_COM_DIVISA, ORDENE_TAQ_MONTO_ADJ_DIVISA, ORDENE_TAQ_MONTO_TOTAL_DIVISA, ORDENE_TAQ_MONTO_CAP_BS, ORDENE_TAQ_MONTO_COM_BS, ");
		sql.append("ORDENE_TAQ_MONTO_ADJ_BS, ORDENE_TAQ_MONTO_TOTAL_BS, ORDENE_TAQ_TASA_CAMBIO, ORDENE_TAQ_CTA_DIVISAS, ORDENE_TAQ_CTA_BS, ORDENE_TAQ_TEL_CLIENT, ORDENE_TAQ_CORREO_CLIENT, ");
		sql.append("ORDENE_TAQ_FE_REGISTRO, ORDENE_TAQ_FE_VALOR, ORDENE_TAQ_OBSERVACION, ORDENE_TAQ_ESTATUS, ORDENE_TAQ_VIGENCIA, ORDENE_TAQ_TIPO_DIVISA, ORDENE_TAQ_ESTATUS_BCV, ORDENE_TAQ_ORDENE_ID_BCV ");
		sql.append(") VALUES (");
		sql.append(ordeneId).append(" , ");
		sql.append(clienteID).append(" , ");
		sql.append("'").append(clienteCedRif).append("' , ");
		sql.append("'").append(tipperID).append("' , ");
		sql.append("'").append(tipOper).append("' , ");
		sql.append("'").append(numeroRetencionCapital).append("' , ");
		sql.append("'").append(codigoOperacionCapital).append("' , ");
		sql.append("'").append(numeroRetencionComision).append("' , ");
		sql.append("'").append(codigoOperacionComision).append("' , ");
		sql.append("'").append(clientNombre).append("' , ");
		sql.append(undinvID).append(" , ");
		sql.append("'").append(insfID).append("' , ");
		sql.append(montoCapDivisa).append(" , ");
		sql.append(montoComDivisa).append(" , ");
		sql.append(montoAdjuDivisa).append(" , ");
		sql.append(montoTotalDivisa).append(" , ");
		sql.append(montoCapBs).append(" , ");
		sql.append(montoComBs).append(" , ");
		sql.append(montoAdjuBs).append(" , ");
		sql.append(montoTotalBs).append(" , ");
		sql.append(tasaCambio).append(" , ");
		sql.append("'").append(ctaDivisa).append("' , ");
		sql.append("'").append(ctaBs).append("' , ");
		sql.append("'").append(telefono).append("' , ");
		sql.append("'").append(correo).append("' , ");
		sql.append(fechaRegistro).append(" , ");
		sql.append(fechaValor).append(" , ");
		sql.append("'").append(observacion).append("' , ");
		sql.append(estatus).append(" , ");
		sql.append(vigencia).append(" , ");
		sql.append(tipoDivisa).append(", ");
		sql.append(ordeneEstatusBCV).append(", ");
		sql.append("'").append(ordeneIDBCV).append("' ");
		sql.append(")");
	
		try {
			conn = dso.getConnection();	
			statement = conn.createStatement();
			statement.executeUpdate(sql.toString());
			return true;
		} catch (SQLException e) {
			logger.info("Ha ocurrido un error al insertar en la tabla INFI_TB_232_ORDENES_TAQUILLA error: "+e.getMessage().toString());
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Verifica si hay operaciones rechazadas en el intento de aplicar las operaciones en altair
	 * @param listaOperaciones
	 * @return true si hay operaciones rechazadas, false en caso
	 */
	private boolean operacionesRechazadas(ArrayList<OrdenOperacion> listaOperaciones) {
		if(listaOperaciones!=null && !listaOperaciones.isEmpty()){	
			for(int k = 0; k < listaOperaciones.size(); k++){
				OrdenOperacion operacion = (OrdenOperacion) listaOperaciones.get(k);
				
				if(operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_RECHAZADA)){
					logger.info("ENCONTRADA UNA OPERACION RECHAZADA... DE TIPO: "+operacion.getTipoTransaccionFinanc());
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Verifica si quedaron operaciones en espera para la orden
	 * @param listaOperaciones
	 * @return true si hay operaciones rechazadas, false en caso
	 */
	private boolean operacionesEnEspera(ArrayList<OrdenOperacion> listaOperaciones) {
		
		if(listaOperaciones!=null && !listaOperaciones.isEmpty()){	
			for(int k = 0; k < listaOperaciones.size(); k++){
				OrdenOperacion operacion = (OrdenOperacion) listaOperaciones.get(k);
				
				if(operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_EN_ESPERA)){
					logger.info("ENCONTRADA UNA OPERACION EN ESPERA... DE TIPO: "+operacion.getTipoTransaccionFinanc());
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Hacer el reverso de las operaciones fallidas 
	 * @param beanTOSimulada
	 * @param factoryAltair
	 * @throws Throwable
	 */
	private void hacerReversoOperaciones(ArrayList<OrdenOperacion> listaOperaciones, FactoryAltair factoryAltair) throws Throwable {
		logger.info("Entrando al reverso para verificar si existen operaciones en estatus aplicada...");		
		//verificar operaciones fallidas de la orden
		//verificar operaciones aplicadas
		if(listaOperaciones!=null && !listaOperaciones.isEmpty()){
			for(int k = 0; k < listaOperaciones.size(); k++){
				OrdenOperacion operacion = (OrdenOperacion) listaOperaciones.get(k);
				//--si la operacion fue aplicada
				logger.info("Status de la operación con código " + operacion.getCodigoOperacion());
				logger.info("NIO " + operacion.getNumeroMovimiento());
				logger.info("TIPO " + operacion.getTipoTransaccionFinanc());			
				logger.info("Status " + operacion.getStatusOperacion());
				if(operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_APLICADA)){		
					factoryAltair.aplicarReverso(operacion);//--aplicar reverso de operacion aplicada
					
				}
			}	
		}
	}

	/**
	 * Elimina una orden de la base de datos, sus operaciones, titulos y todos los datos asociados a ella, 
	 * asi como tambi&eacute;n las operaciones de cr&eacute;dito para el veh&iacute;culo tomador en caso de que &eacute;ste no sea Banco de Venezuela
	 * @param beanTOSimulada
	 * @throws SQLException 
	 */
	private void eliminarOrden(TomaOrdenSimulada beanTOSimulada, Orden ordenVehiculo) throws Exception {
		
		try {
			conn = dso.getConnection();			
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			
			logger.info("ERROR OCURRIDO... SE ELIMINARA ORDEN INGRESADA...");
			
			//--0. Eliminar operaciones de credito asociadas al vehiculo en caso de NO ser BDV
			if(!vehiculoTomadorBDV){
				ArrayList<OrdenOperacion> operacionesVehiculo = ordenVehiculo.getOperacion();
				if(operacionesVehiculo!=null && !operacionesVehiculo.isEmpty()){
					for(int k=0; k< operacionesVehiculo.size();k++ ){
						OrdenOperacion operacionVehiculo = (OrdenOperacion)operacionesVehiculo.get(k);
						//--1. Eliminar operacion de credito vehiculo
						StringBuffer sql = new StringBuffer();
						sql.append("DELETE FROM INFI_TB_207_ORDENES_OPERACION WHERE ordene_operacion_id = ").append(operacionVehiculo.getIdOperacion());	
						statement.executeUpdate(sql.toString());
					}
				}
			}
						
			//--1. Eliminar operaciones
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_207_ORDENES_OPERACION WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--2. Eliminar intentos de operaciones
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_209_ORDENES_OPERAC_INT WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());
			statement.executeUpdate(sql.toString());
			//--3. Eliminar documentos
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_208_ORDENES_DOCUMENTOS WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--4. Eliminar titulos
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_206_ORDENES_TITULOS WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--5. Eliminar Campos dinamicos
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_205_ORDENES_CAMP_DIN WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--6. Eliminar Data extendida
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_212_ORDENES_DATAEXT WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--7. Eliminar Instrucccion de pago
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_217_CTES_CUENTAS_ORD WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//SIMADI TAQUILLA NM32454
			//--8. Eliminar orden tabla INFI_TB_232_ORDENES_TAQUILLA
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_232_ORDENES_TAQUILLA WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--9. Eliminar la orden
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_204_ORDENES WHERE ORDENE_ID = ").append(beanTOSimulada.getIdOrden());	
			statement.executeUpdate(sql.toString());
			

			conn.commit();
		
		} catch (Exception e) {
			conn.rollback();			
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error elimimando la orden");			
		}finally{					
			if(statement!=null)
				statement.close();
			
			if(conn!=null)
				conn.close();
		}
		
	}

	/**
	 * Verifica si existe una cuenta para un uso determinado asociada al cliente
	 * @param idCliente
	 * @return true, si existe la cuenta, false en caso contrario
	 * @throws Exception
	 */
	private boolean existeCuentaParaCliente(long idCliente, String usoCuenta) throws Exception {
		
		boolean existe = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CTECTA_NUMERO FROM INFI_TB_202_CTES_CUENTAS WHERE client_id = ").append(idCliente);
		sql.append(" and CTECTA_USO = '").append(usoCuenta).append("'");
		
		try {
			conn = dso.getConnection();			
			statement = conn.createStatement();
			resultQuery = statement.executeQuery(sql.toString());			
			
			if(resultQuery.next()){
				existe = true;
	 		} 

		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error al buscar el tipo de uso para la cuenta del cliente");
		} finally {
			if (resultQuery != null){
				resultQuery.close();
				cerrarConexion();
			}
		}
		return existe;

	}

	
	/**
	 * 
	 * @param beanTomaOrden
	 * @throws Exception
	 */
	private void armarTitulos(TomaOrdenSimulada beanTomaOrden) throws Exception {
		
		//Busca los títulos asociados a la orden
		ArrayList listaTitulos = beanTomaOrden.getListaOrdenTitulo();
		
		//Objeto para el query de titulos
		StringBuffer sql = new StringBuffer();
		String parametros = "?, ?, ?, ?, ?, ?, ?, ?, ?";
		sql.append("INSERT INTO INFI_TB_206_ORDENES_TITULOS (");
		sql.append("ordene_id, titulo_id, titulo_pct, titulo_monto, titulo_unidades, titulo_pct_recompra, titulo_precio_mercado, titulo_mto_int_caidos, TITULO_MTO_NETEO ");
		sql.append(") VALUES(").append(parametros).append(")");
		
		//Se recorren los títulos y se arman los parametros para la persistencia
		for (int i=0; i<listaTitulos.size(); i++) {
			OrdenTitulo beanOrdenTitulo = (OrdenTitulo)listaTitulos.get(i);
			data[pos][0] = sql.toString();
			data[pos][1] = ((parametros.length()/3)+3);
			data[pos][2] = beanOrdenTitulo.getIdOrden();
			data[pos][3] = beanOrdenTitulo.getIdTitulo(); 
			data[pos][4] = beanOrdenTitulo.getPorcentaje();
			data[pos][5] = beanOrdenTitulo.getValorInvertido();
			data[pos][6] = beanOrdenTitulo.getUnidadesInvertidas();
			data[pos][7] = beanOrdenTitulo.getPorcentajeRecompra();
			data[pos][8] = beanOrdenTitulo.getPrecioMercado();
			data[pos][9] = beanTomaOrden.getMontoInteresCaidosRecompra().setScale(6,BigDecimal.ROUND_HALF_EVEN);
			data[pos][10] = beanOrdenTitulo.getMontoNeteo().setScale(6, BigDecimal.ROUND_HALF_EVEN);
			++pos;
		}
	}
	
	/**Arma los campos dinámicos de la orden*/
	private void armarCamposDinamicos(TomaOrdenSimulada beanTomaOrden) throws Exception {
		// 12.- Guarda los campos dinamicos
		ArrayList listaCampos =  beanTomaOrden.getListaCamposDinamicos();
		if (!listaCampos.isEmpty()){
			//Objeto para el query de titulos
			StringBuffer sql = new StringBuffer();
			String parametros = "?, ?, ?";			
			sql.append("insert into INFI_TB_205_ORDENES_CAMP_DIN(ordene_id,campo_id,campo_valor)");
			sql.append(" VALUES(").append(parametros).append(")");
			// Se recorren campos dinamicos se almacenan en la base de datos			
			for (Iterator iter = listaCampos.iterator(); iter.hasNext(); ) {
				CampoDinamico campoDinamico 	=	(CampoDinamico) iter.next();				
				data[pos][0] = sql.toString();
				data[pos][1] = (new Integer(5));
				data[pos][2] = beanTomaOrden.getIdOrden();
				data[pos][3] = campoDinamico.getIdCampo(); 
				data[pos][4] = campoDinamico.getValor();	
				++pos;
			}
		}
	}
	
	/**Arma los campos dinámicos de la orden*/
	private void armarOrdenRequisitos(TomaOrdenSimulada beanTomaOrden) throws Exception {
		// 12.- Guarda los campos dinamicos
		ArrayList<OrdenRequisito> listaRequisitos =  beanTomaOrden.getOrdenRequisitos();
		if (listaRequisitos!=null && !listaRequisitos.isEmpty()){
			//Objeto para el query de titulos
			StringBuffer sql = new StringBuffer();
			String parametros = "?, ?, ?, ?, ?";			
			sql.append("insert into INFI_TB_210_ORDENES_REQUISITO(ORDENE_REQUISITO_ID,ORDENE_ID,FECHA_RECEPCION, RECEPCION_USUARIO_NM, INDICA_ID)");
			sql.append(" VALUES(").append(parametros).append(")");
			// Se recorren campos dinamicos se almacenan en la base de datos			
			for (int i=0; i<listaRequisitos.size(); i++) {
				OrdenRequisito ordenRequisito = (OrdenRequisito)listaRequisitos.get(i);			
				data[pos][0] = sql.toString();
				data[pos][1] = (new Integer(7));
				data[pos][2] = ordenRequisito.getId();
				data[pos][3] = beanTomaOrden.getIdOrden();
				data[pos][4] = ordenRequisito.getFechaRecepcion();
				data[pos][5] = ordenRequisito.getUsuarioRecepcion();
				data[pos][6] = ordenRequisito.getIndicaId();
				
				++pos;
			}
		}
	}

	/**
	 * 
	 * @param beanTomaOrden
	 * @throws Exception
	 */
	private void armarOperaciones(TomaOrdenSimulada beanTomaOrden) throws Exception {
		
		//Busca los títulos asociados a la orden
		ArrayList listaOperaciones = beanTomaOrden.getListaOperaciones();
		String numeroCuenta = "";
		
		//Objeto para el query de operaciones
		StringBuffer sql = new StringBuffer();
		String parametros = "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		sql.append("INSERT INTO INFI_TB_207_ORDENES_OPERACION (");
		sql.append("ordene_operacion_id, ordene_id, status_operacion, ctecta_numero,trnf_tipo, aplica_reverso, monto_operacion, tasa, ");
		sql.append("fecha_aplicar, in_comision, moneda_id, serial,codigo_operacion,numero_retencion, in_comision_invariable, operacion_nombre, trnfin_id ");
		sql.append(") VALUES(").append(parametros).append(")");

		//Se recorren los títulos y se arman los parametros para la persistencia	
		for (int i=0; i<listaOperaciones.size(); i++) {
			OrdenOperacion beanOperacion = (OrdenOperacion)listaOperaciones.get(i);
			data[pos][0] = sql.toString();
			data[pos][1] = ((parametros.length()/3)+3);
			data[pos][2] = beanOperacion.getIdOperacion();
			data[pos][3] = beanOperacion.getIdOrden(); 			
			data[pos][4] = beanOperacion.getStatusOperacion();
			data[pos][5] = beanOperacion.getNumeroCuenta();
			data[pos][6] = beanOperacion.getTipoTransaccionFinanc();
			if ( beanOperacion.isAplicaReverso())
				data[pos][7] = new Integer(1);
			else
				data[pos][7] = new Integer(0);
			data[pos][8] = beanOperacion.getMontoOperacion();			
			logger.info("monto oper " +beanOperacion.getNombreOperacion()+ ": " +beanOperacion.getMontoOperacion());

			data[pos][9] = beanOperacion.getTasa();
			data[pos][10] = beanOperacion.getFechaAplicar();
			if (beanOperacion.getInComision()==1)
				data[pos][11] = new Integer(1);
			else
				data[pos][11] = new Integer(0);
			data[pos][12] = beanOperacion.getIdMoneda();
				
			data[pos][13] = beanOperacion.getSerialContable();
			data[pos][14] = beanOperacion.getCodigoOperacion();
			data[pos][15] = beanOperacion.getNumeroRetencion();	
			data[pos][16] = beanOperacion.getIndicadorComisionInvariable();	
			data[pos][17] = beanOperacion.getNombreOperacion();	
			data[pos][18] = beanOperacion.getIdTransaccionFinanciera();	
			++pos;
			numeroCuenta = beanOperacion.getNumeroCuenta();
		}
		//SE COMENTO YA QUE AHORA NO SE GUARDARAN INSTRUCCIONES DE PAGO DISTINTAS (EN CASO DE NO EXISTIR) SINO UNA UNICA INSTRUCCION DE PAGO
		// Obtener datos de cliente
		/*Cliente beanCliente;
		ClienteDAO boCliente = new ClienteDAO(nombreDataSource, dso);
		// Select * from INFI_TB_201_CTES where client_id = ?
		boCliente.listarPorId(beanTomaOrden.getIdCliente());
		beanCliente = boCliente.getCliente();	
		//------------------------------------------------------------------
		CuentaCliente cuentaCliente = new CuentaCliente();
				
		//cuentaCliente.setIdOrden(beanTomaOrden.getIdOrden());
		cuentaCliente.setCtecta_numero(numeroCuenta);		
		cuentaCliente.setCtecta_nombre(numeroCuenta);
		cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
		cuentaCliente.setClient_id(beanTomaOrden.getIdCliente());
		cuentaCliente.setNombre_beneficiario(beanCliente.getNombre());
		cuentaCliente.setCedrif_beneficiario(String.valueOf(beanCliente.getRifCedula()));
		cuentaCliente.setIdOrden(0);
		
	
		// De no existir cuenta validada en L145 Metodo insertar()
		// Se Insertar cuenta para el cobro de las comisiones asociada al cliente
		if(!existeCuentaComis){		
			cuentaCliente.setCtecta_uso(UsoCuentas.COBRO_DE_COMISIONES);
			armarInsercionUsoDeCuenta(cuentaCliente, beanTomaOrden,false);			
		}
		// De no existir cuenta validada en L152 Metodo insertar()
		// Se Insertar cuenta para el pago de cupones asociada al cliente
		if(!this.existeCuentaCupones){
			cuentaCliente.setCtecta_uso(UsoCuentas.PAGO_DE_CUPONES);
			armarInsercionUsoDeCuenta(cuentaCliente, beanTomaOrden,false);			
		}
		// De no existir cuenta validada en L159 Metodo insertar()
		// Se Insertar cuenta para el pago deamortizaciones asociada al cliente
		if(!this.existeCuentaAmortiz){
			cuentaCliente.setCtecta_uso(UsoCuentas.PAGO_DE_CAPITAL);
			armarInsercionUsoDeCuenta(cuentaCliente, beanTomaOrden,false);			
		}*/
	}	

	/**
	 * Arma la insercion de la cuenta los pagos asociados al cliente
	 * @param numeroCuenta
	 * @throws Exception 
	 */
	private void armarInsercionUsoDeCuenta(CuentaCliente cuentaCliente, TomaOrdenSimulada beanTomaOrden, boolean nuevaInstrucionPago) throws Exception {
		//listar datos del cliente por id
		ClienteCuentasDAO clienteCuentaDAO = new ClienteCuentasDAO(dso);
		if (beanTomaOrden.getIdInstruccion()!=null){
			cuentaCliente.setIdInstruccion(Long.parseLong(beanTomaOrden.getIdInstruccion()));
			String[] sql = clienteCuentaDAO.insertarClienteCuentasOrd(cuentaCliente);
			for(int k=0; k<sql.length; k++){
				data[pos][0] = sql[k];
				data[pos][1] = (new Integer(2));
				pos++;
			}
		}	
	}

	/**Inserta la data extendida asociada a la orden*/
	private void armarDataExtendida(TomaOrdenSimulada beanTomaOrden) throws Exception{
				
		//Busca los títulos asociados a la orden
		ArrayList listaDataExtendida = beanTomaOrden.getOrdenDataExt();
		
		//Objeto para el query de operaciones
		StringBuffer sql = new StringBuffer();
		String parametros = "?, ?, ?";
		sql.append("INSERT INTO INFI_TB_212_ORDENES_DATAEXT (ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR)");
		sql.append(" VALUES(").append(parametros).append(")");

		//Se recorren los títulos y se arman los parametros para la persistencia	
		for (int i=0; i<listaDataExtendida.size(); i++) {
			OrdenDataExt ordenDataExt = (OrdenDataExt)listaDataExtendida.get(i);
			data[pos][0] = sql.toString();
			data[pos][1] = ((parametros.length()/3)+3);
			data[pos][2] = beanTomaOrden.getIdOrden();
			data[pos][3] = ordenDataExt.getIdData(); 
			data[pos][4] = ordenDataExt.getValor();			
			++pos;
		}
	}
	
	/**
	 * Retorna la orden asociada al veh&iacute;culo y unidad de inversion que contiene las operaciones de credito que
	 * se le han hecho a su n&uacute;mero de cuenta correspondiente. Crea la orden para el veh&iacute;culo y unidad de inversi&oacute;n
	 * en caso de no existir
	 * @param beanTomaOrden
	 * @return orden veh&iacute;culo
	 * @throws Exception
	 */
	public Orden crearBuscarOrdenVehiculoUI(String idVehiculoTomador, String ipTerminal, long idUnidadInversion, String nombreUsuario, double tasaCambio, String numeroOficina) throws Exception{
		
		Orden ordenVehiculoUI = new Orden();
		OrdenDAO ordenDAO = new OrdenDAO(dso);
		VehiculoDAO vehiculoDAO = new VehiculoDAO(dso);
		com.bdv.infi.dao.ClienteDAO clienteDAO = new com.bdv.infi.dao.ClienteDAO(dso);
		long idClienteVehiculo = -1;
		String letraRif = "";
		
		//---obtener el rif del Vehiculo Tomador de la orden--------------
		String rifVehiculo = vehiculoDAO.obtenerRifVehiculo(idVehiculoTomador);
			
		letraRif = rifVehiculo.substring(0,1);
		//Obtener rif Numerico
		String rifNumerico = Utilitario.obtenerNumeroRifCI(rifVehiculo);	
		rifVehiculo = rifNumerico;
					
		//-------------------------------------------------------------------
		
		//----Obtener idCliente del Vehiculo, sino existe como cliente, entonces
		//almacenarlo en la tabla de clientes de infi con todos sus datos de altair
		try{
			clienteDAO.buscarPorCedRif(Long.parseLong(rifVehiculo));
		
		}catch(NumberFormatException nfe){
			logger.error(nfe.getMessage()+ Utilitario.stackTraceException(nfe));
			throw new Exception("El rif del veh&iacute;culo tomador encontrado ('"+ rifVehiculo +"') no es un n&uacute;mero valido.");
		}
		
		//Vehiculo encontrado en INFI como Cliente
		if(clienteDAO.getDataSet().next()){
			
			idClienteVehiculo = Long.parseLong(clienteDAO.getDataSet().getValue("client_id"));
			letraRif = clienteDAO.getDataSet().getValue("tipper_id");
			
		}else{//cliente NO encontrado en INFI
			ManejoDeClientes manejoClientes = new ManejoDeClientes(dso);
			//obtener y guardar cliente vehiculo en altair
			manejoClientes.obtenerClienteAltair(rifVehiculo, letraRif, ipTerminal, contexto, true, false, false, false, nombreUsuario);	
			
			//obtener id de cliente almacenado en IFI
			clienteDAO.buscarPorCedRif(Long.parseLong(rifVehiculo));			
			if(clienteDAO.getDataSet().next())//Cliente encontrado en INFI				
				idClienteVehiculo = Long.parseLong(clienteDAO.getDataSet().getValue("client_id"));		
			
		}
		//--------------------------------------------------------------------------------------------------------------------------
		
		//--Verificar si ya existe una orden para el vehiculo asociada a la unidad de inversión------------------------
		long idOrdenVehiculo = ordenDAO.verificarOrdenVehiculoUI(idClienteVehiculo, idUnidadInversion);
		
		if(idOrdenVehiculo!=-1){//existe orden para el vehiculo
			//Listar orden existente
			ordenVehiculoUI = ordenDAO.listarOrden(idOrdenVehiculo,false,false,false,false,false);
			//Limpiar lista de operaciones de credito ejecutadas anteriormente
			ArrayList<OrdenOperacion> operacionesVeh = new ArrayList<OrdenOperacion>();
			ordenVehiculoUI.setOperacion(operacionesVeh);
		
		}else{//Crear orden para el vehiculo y la unidad de inversion seleccionada
			
			ordenVehiculoUI.setIdCliente(idClienteVehiculo);
			ordenVehiculoUI.setStatus(StatusOrden.REGISTRADA);
			ordenVehiculoUI.setIdTransaccion(TransaccionNegocio.ORDEN_VEHICULO);
			ordenVehiculoUI.setFechaValor(new Date());
			ordenVehiculoUI.setNombreUsuario(nombreUsuario);
			ordenVehiculoUI.setTerminal(ipTerminal);
			ordenVehiculoUI.setTasaCambio(tasaCambio);
			ordenVehiculoUI.setSucursal(numeroOficina);
			ordenVehiculoUI.setIdUnidadInversion(Integer.parseInt(String.valueOf(idUnidadInversion)));		
			ordenVehiculoUI.setFinanciada(false);
			//ordenVehiculoUI.setIdBloter(idBloter);
			ordenVehiculoUI.setInternoBDV(0);	
						
			//insertar la orden nueva del vehiculo en base de datos
			insertarOrdenVehiculo(ordenVehiculoUI);
						
		}
		//---------------------------------------------------------------------------------------------------------------
		//retornar la orden del vehiculo
		return ordenVehiculoUI;
		
	}	

	/**
	 * Inserta en base de datos la orden para los cr&eacute;ditos asociados al vehiculo tomador de la orden.
	 * @param ordenVehiculo
	 * @throws Exception
	 */
	public void insertarOrdenVehiculo(Orden ordenVehiculo) throws Exception{
		OrdenDAO ordenDAO = new OrdenDAO(dso);
		String[] insertsOrden = ordenDAO.insertar(ordenVehiculo);
		
		try{
			conn = dso.getConnection();			
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			
			for(int i=0; i<insertsOrden.length; i++){
				statement.executeUpdate(insertsOrden[i]);
			}			
			conn.commit();
		
		} catch (Exception e) {
			conn.rollback();			
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error insertando la orden para las operaciones de cr&eacute;dito del veh&iacute;culo.");			
		}finally{					
			if(statement!=null)
				statement.close();
			
			if(conn!=null)
				conn.close();
		}	

	}
	
	/**
	 * Agrega operaciones al objeto orden del vehiculo. Por cada operaci&oacute;n de d&eacute;bito en la orden a guardar,
	 * se crea una operaci&oacute;n de cr&eacute;dito para la orden del veh&iacute;culo de la unidad de inversi&oacute;n en cuesti&oacute;n
	 * @param ordenVehiculo
	 * @param ordenSimulada
	 * @throws Exception 
	 */
	public void crearOperacionesOrdenVehiculo(Orden ordenVehiculo, TomaOrdenSimulada ordenSimulada) throws Exception {
		
		ArrayList<OrdenOperacion> listaOperaciones =  ordenSimulada.getListaOperaciones();
		TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(dso);
		TransaccionFija transaccionFijaVeh = new TransaccionFija();

		for(int i=0; i<listaOperaciones.size(); i++){
			OrdenOperacion beanOperacionOrden = (OrdenOperacion) listaOperaciones.get(i);			
			
			//Verificar si la operacion es de debito
			//----			
			if(beanOperacionOrden.getTipoTransaccionFinanc().equals(TransaccionFinanciera.DEBITO)){
				//crear operacion de credito para el vehiculo
				OrdenOperacion operacionCreditoVehiculo = new OrdenOperacion();
				//Buscar transaccion fija con el codigo correspondiente para el vehiculo
				
				//transaccionFijaVeh = transaccionFijaDAO.obtenerTransaccion(com.bdv.infi.logic.interfaces.TransaccionFija.CREDITO_CUENTA_VEHICULO, ordenSimulada.getVehiculoTomador());
				transaccionFijaVeh = transaccionFijaDAO.obtenerTransaccion(Integer.parseInt(beanOperacionOrden.getIdTransaccionFinanciera()), ordenSimulada.getVehiculoTomador(),ordenSimulada.getInstrumentoId());
				
				if(transaccionFijaVeh!=null){				
				
					if(transaccionFijaVeh.getCodigoOperacionVehCre()==null || transaccionFijaVeh.getCodigoOperacionVehCre().equals("")){
						throw new Exception("No se ha definido el c&oacute;digo de operaci&oacute;n de cr&eacute;dito asociado al veh&iacute;culo tomador de la orden.");
					}
					
					operacionCreditoVehiculo.setMontoOperacion(beanOperacionOrden.getMontoOperacion());
					operacionCreditoVehiculo.setTasa(new BigDecimal(100));		
					operacionCreditoVehiculo.setInComision(beanOperacionOrden.getInComision());
					operacionCreditoVehiculo.setNombreOperacion(transaccionFijaVeh.getNombreTransaccion());
					operacionCreditoVehiculo.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
					operacionCreditoVehiculo.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
					//setear codigo para credito al vehiculo
					operacionCreditoVehiculo.setCodigoOperacion(transaccionFijaVeh.getCodigoOperacionVehCre());	
					operacionCreditoVehiculo.setIdTransaccionFinanciera(beanOperacionOrden.getIdTransaccionFinanciera());
					operacionCreditoVehiculo.setIdOperacionRelacion(beanOperacionOrden.getIdOperacion());
					
					operacionCreditoVehiculo.setFechaAplicar(new Date());
					operacionCreditoVehiculo.setFechaProcesada(new Date());
					
					//---Buscar numero de cuenta vehiculo
					VehiculoDAO  vehiculoDAO = new VehiculoDAO(dso);
					vehiculoDAO.listarPorId(ordenSimulada.getVehiculoTomador());
					String numeroCuentaVehiculo = null;
					
					if(vehiculoDAO.getDataSet().next()){
						numeroCuentaVehiculo = vehiculoDAO.getDataSet().getValue("vehicu_numero_cuenta");
					}
	
					if(numeroCuentaVehiculo==null){
						throw new Exception("No se encuentra el n&uacute;mero de cuenta asociado al veh&iacute;culo tomador de la orden.");
					}
					//--------------------------------
				
					operacionCreditoVehiculo.setNumeroCuenta(numeroCuentaVehiculo);
					operacionCreditoVehiculo.setIdMoneda(beanOperacionOrden.getIdMoneda());
					operacionCreditoVehiculo.setSiglasMoneda(beanOperacionOrden.getIdMoneda());
	
					//agregar operacion a la orden del vehiculo
					ordenVehiculo.agregarOperacion(operacionCreditoVehiculo);
					
				}else{
					throw new Exception("No se ha definido una transacci&oacute;n de cr&eacute;dito para el veh&iacute;culo tomador.");
				}

			}
		}
	}
	
	public String[] enviarOperacionBCV(String codTipoMovimiento,String codCliente,String nbCliente,BigDecimal moBase,BigDecimal tsCambio, String couctaTransa, BigDecimal moTrans,long coMotivoOperacion,String nuCtaconvenio20, String txtTlfCliente,String txtEmailPinc){
		try {
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_MENUDEO);
			_credenciales = credencialesDAO.getDataSet();
			Propiedades propiedades =  Propiedades.cargar();
			String userName = "";
			String clave    = "";
			String codigoAprobacionBCV = "";
			String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR;
			
			//SE LLAMA AL WS DE BCV PARA EL TIPO DE UNIDAD DE INVERSION CORRECTO
			AutorizacionPortBindingStub stub;
			
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
				logger.error("Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: "+ConstantesGenerales.WS_BCV_MENUDEO);
				throw new org.bcv.service.Exception();
			}
			
			stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_MENUDEO)), null);
			Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null) {
				headers = new Hashtable();
				stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
			}
			headers.put("Username", userName);
			headers.put("Password", clave);
		
			try {
				 codigoAprobacionBCV = stub.VENTADIV(codTipoMovimiento, codCliente, nbCliente, moBase, tsCambio, couctaTransa, moTrans, coMotivoOperacion, nuCtaconvenio20, txtTlfCliente, txtEmailPinc);
				 return new String[] {"1",codigoAprobacionBCV};
				 //SE RETORNA QUE LA OPERACION FUE APROBADA Y ADEMAS EL CODIGO DE APROBACION DEL BCV 
			} catch (Exception e) {
				logger.error("Ha ocurrido un error al momento de enviar la operacion al BCV "
						+e.toString()+" "+Utilitario.stackTraceException(e));
				e.printStackTrace();
				
				logger.error("INFORMACION ENVIADA AL WS DE BCV");
				logger.error("tipoMovimiento: "+codTipoMovimiento);
				logger.error("codigoCliente: "+codCliente);
				logger.error("nombreCliente: "+nbCliente);
				logger.error("montoBase: "+moBase);
				logger.error("tasaCambio: "+tsCambio);
				logger.error("codMonedaIso: "+couctaTransa);
				logger.error("montoTransaccion: "+moTrans);
				logger.error("tipoOperacion: "+coMotivoOperacion);
				logger.error("ctaConvenio20: "+nuCtaconvenio20);
				logger.error("telefono: "+txtTlfCliente);
				logger.error("email: "+txtEmailPinc);
				
				
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
					//SE RETORNA QUE LA OPERACION FUE RECHAZADA Y EL MOTIVO DEL RECHAZO
					return new String[] {"0",e.toString()};
				}else {//SE GENERA UN ERROR NO CONTROLADO
					//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
					//SE RETORNA QUE SE GENERO UN ERROR NO CONTROLADO
					return new String[] {"2",e.toString()};
				}

				
			}
		}  catch (Exception e1) {
			logger.error("Error ocurrido al momento de realizar la configuracion para el envio de operacion al BCV: ");
			logger.error("Error: "+e1.toString()+" "+Utilitario.stackTraceException(e1));
			e1.printStackTrace();
			
			
			logger.error("INFORMACION ENVIADA O INTENTADA ENVIAR AL WS DE BCV");
			logger.error("tipoMovimiento: "+codTipoMovimiento);
			logger.error("codigoCliente: "+codCliente);
			logger.error("nombreCliente: "+nbCliente);
			logger.error("montoBase: "+moBase);
			logger.error("tasaCambio: "+tsCambio);
			logger.error("codMonedaIso: "+couctaTransa);
			logger.error("montoTransaccion: "+moTrans);
			logger.error("tipoOperacion: "+coMotivoOperacion);
			logger.error("ctaConvenio20: "+nuCtaconvenio20);
			logger.error("telefono: "+txtTlfCliente);
			logger.error("email: "+txtEmailPinc);
			
			boolean errorControlado = false;
			//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
			for (ErroresMenudeo tmp: ErroresMenudeo.values() ) {
				if(e1.toString().contains(tmp.getCodigoError())){
					errorControlado = true;
					break;
				}
	        }
			
			if(errorControlado){
				//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS VERIFICADA RECHAZADA
				//SE RETORNA QUE LA OPERACION FUE RECHAZADA Y EL MOTIVO DEL RECHAZO
				return new String[] {"0",e1.toString()};
			}else {//SE GENERA UN ERROR NO CONTROLADO
				//SE ACTUALIZA LA ORDEN Y SE LE COLOCA EL ESTATUS SIN VERIFICAR
				//SE RETORNA QUE SE GENERO UN ERROR NO CONTROLADO
				return new String[] {"2",e1.toString()};
			}
		} 
	}
	
	public String[] anularOperacionBCV(String codigoBCV,String codigoTipoMovimiento,String motivoAnulacion){
		try {
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_MENUDEO);
			_credenciales = credencialesDAO.getDataSet();
			Propiedades propiedades =  Propiedades.cargar();
			String userName = "";
			String clave    = "";
			String codigoAprobacionBCV = "";
			
			//SE LLAMA AL WS DE BCV PARA EL TIPO DE UNIDAD DE INVERSION CORRECTO
			AutorizacionPortBindingStub stub;
			
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
				logger.error("Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: "+ConstantesGenerales.WS_BCV_MENUDEO);
				throw new org.bcv.service.Exception();
			}
			
			stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_MENUDEO)), null);
			Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null) {
				headers = new Hashtable();
				stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
			}
			headers.put("Username", userName);
			headers.put("Password", clave);
		
			try {
				 codigoAprobacionBCV = stub.ANULAR(codigoBCV, codigoTipoMovimiento, motivoAnulacion);
				 return new String[] {"1",codigoAprobacionBCV};
				 //SE RETORNA QUE LA OPERACION FUE APROBADA Y ADEMAS EL CODIGO DE APROBACION DEL BCV 
			} catch (Exception e) {
				logger.error("Ha ocurrido un error al momento de enviar la anulacion al BCV. Codigo de operacion BCV: " +codigoBCV
						+e.toString()+" "+Utilitario.stackTraceException(e));
				e.printStackTrace();
				
				logger.error("INFORMACION ENVIADA AL WS DE ANULACION DE BCV");
				logger.error("codigo de operacion BCV: "+codigoBCV);
				logger.error("codigo tipo de movimiento: "+codigoTipoMovimiento);
				logger.error("motivo anulacion: "+motivoAnulacion);
				//SE RETORNA QUE LA OPERACION FUE RECHAZADA Y EL MOTIVO DEL PROBLEMA
				return new String[] {"0",e.toString()};
			}
		} catch (Exception e1) {
			logger.error("Error ocurrido al momento de realizar la configuracion para el envio de operacion al BCV: ");
			logger.error("Error: "+e1.toString()+" "+Utilitario.stackTraceException(e1));
			e1.printStackTrace();
			return new String[] {"0",e1.toString()};
		} 
	}
}
