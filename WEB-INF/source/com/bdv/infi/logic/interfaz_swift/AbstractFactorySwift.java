package com.bdv.infi.logic.interfaz_swift;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;

import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.CuentasBancoDAO;
import com.bdv.infi.dao.IntentoOperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.beans_swift.AbstractSwifLT;
import com.bdv.infi.data.beans_swift.SwiftConIntermedioABA;
import com.bdv.infi.data.beans_swift.SwiftConIntermedioSwift;
import com.bdv.infi.data.beans_swift.SwiftSinIntermedioABA;
import com.bdv.infi.data.beans_swift.SwiftSinIntermedioBIC;
import com.bdv.infi.ftp.FTPUtil;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.Sistemas;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.DBO;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.dao.ClienteDAO;
import com.bdv.infi_toma_orden.data.Cliente;

/**
 * Clase que contiene la l&oacute;gica para el proceso de las transacciones financieras
 * que contiene una orden o una toma de orden. <br>
 * Es la encargada de invocar los conectores para el proceso de las transacciones
 * financieras que deben ir a cada sistema. <br>
 * Adem&aacute;s es la responsable de almacenar los intentos de transacci&oacute;n en las tablas.
 * @author elaucho,mgalindo
 **/
public class AbstractFactorySwift implements TransaccionFinanciera {

	protected SimpleDateFormat sdBD = new SimpleDateFormat("yyyy-MM-dd");	
	protected SimpleDateFormat sdSwift = new SimpleDateFormat("yyMMdd");
	protected SimpleDateFormat sdSwiftFile = new SimpleDateFormat("yyyyMMddHHmmss");	
	private Logger logger = Logger.getLogger(AbstractFactorySwift.class);
	protected Calendar calendHoy = Calendar.getInstance();
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_803_CONTROL_ARCHIVOS*/
	public static final String SECUENCIA_CONTROL_ARCHIVOS = "INFI_TB_803_CONTROL_ARCHIVOS";	
	
	/** Indica el status de la creacion de un archivo **/
	public static final String ARCHIVOSTATUS_GENERADO = "GENERADO"; 
	public static final String ARCHIVOSTATUS_RECHAZADO = "RECHAZADO";
	/** Indica Comision a Cargo del BDV **/
	public static final String OUR = "OUR"; 
	/** Indica Comision a Cargo del Beneficiario **/
	public static final String BEN = "BEN"; 
	
	//Valores configurables de envío SWIFT (Rutas de colas y nombres de archivos)
	public String rutaArchivoSwift=null;
	public String paramColaArchivoSwift=null;
	public String paramNombreArchivoSwift=null;
	public String tipoProducto=null;
	
	/**
	 * DataSource a utilizar si el beanCliente no es un WebService
	 */
	public DataSource dso;
	/**
	 * Nombre del DataSource
	 */
	private String nombreDataSource;
	/**
	 * ResultSet resultado de un query a la base de datos
	 */
	private ResultSet resultSet;
	/**
	 * Control de WebService con excpecion
	 */
	protected int caIntentosFallidos = 0;
	/**
	 * Abstract de los Beans a utilizar
	 */
	protected AbstractSwifLT beanSwift = null;
	/**
	 * Clase que encapsula la funcionalidad de OperacionIntento
	 */
	protected IntentoOperacionDAO intentoOperacionDAO;
	/**
	 * Clase que encapsula la funcionalidad del Cliente
	 */
	protected ClienteDAO clienteDAO;
	/**
	 * Clase que encapsula la funcionalidad de las ordenes
	 */
	protected OrdenDAO ordenDAO;
	/**
	 * Clase que encapsula la funcionalidad del Cliente
	 */
	protected ClienteCuentasDAO clienteCuentasDAO;	
	
	/**
	 * Clase que encapsula la funcionalidad de Parametros
	 */
	protected ParametrosDAO parametrosDAO;
	/**
	 * Bean que encapsula la informacion del Cliente
	 */
	protected Cliente beanCliente = new Cliente();

	/**
	 * Beneficiario
	 */
	protected String beneficiario;
	
	
	/**
	 * tipoInstruccionPago
	 */
	protected String tipoInstruccionPago;
	/**
	 * Monto Comision SWIFT juridico
	 */
	protected BigDecimal montoComisionJ= new BigDecimal(0);
	
	/**
	 * Monto Comision SWIFT natural
	 */
	protected BigDecimal montoComisionN= new BigDecimal(0);
		
	/**
	 * Lista de bean de Intento de Ejecucion
	 */
	protected ArrayList<OperacionIntento> listaIntentos = new ArrayList<OperacionIntento>();
	/**
	 * Lista de bean de Logs de Ejecucion
	 */
	protected ArrayList listaLogEjecucion = new ArrayList();	
	/**
	 * Lista de Beans de Swift
	 */
	protected ArrayList<String> archivoSwiftMT100 = new ArrayList<String>();
		
	/**
	 * Parametros de Comunicacion con Swift
	 */
	protected HashMap<String, String> parametros = new HashMap<String, String>();
	/**
	 * Areas de Trabajo
	 */
	protected long idEjecucion = 0;
	/**
	 * Arreglos para persistencia
	 */
	private Object [][] dataInsertIntento = null;
	
	/**
	 * Variable contador
	 */
	int count;
	
	/**
	 * ArrayList de ordenes para cola de Custodia
	 */
	private ArrayList<Orden> arrayListTesoreria = new ArrayList<Orden>();
	
	/**
	 * Constructor de la clase
	 * @param nombreDataSource : nombre del DataSource a utilizar
	 * @param contexto : contecto de la aplicacion base
	 */
	public AbstractFactorySwift (String nombreDataSource, ServletContext contexto ) {
		this.nombreDataSource = nombreDataSource;
		intentoOperacionDAO = new IntentoOperacionDAO (dso);
		clienteDAO = new ClienteDAO(nombreDataSource, dso);
		clienteCuentasDAO= new ClienteCuentasDAO(dso);
		ordenDAO = new OrdenDAO(dso);	
		parametrosDAO = new ParametrosDAO(dso);
	}
	
	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 */
	public AbstractFactorySwift (DataSource dso, ServletContext contexto) {
		this.dso = dso;
		intentoOperacionDAO = new IntentoOperacionDAO (dso);
		clienteDAO = new ClienteDAO(nombreDataSource, dso);
		clienteCuentasDAO= new ClienteCuentasDAO(dso);
		ordenDAO = new OrdenDAO(dso);
		parametrosDAO = new ParametrosDAO(dso);
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getSqlUpdateOperacion() {
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("UPDATE INFI_TB_207_ORDENES_OPERACION SET ");
		sqlUpdate.append("status_operacion = ?, ");
		sqlUpdate.append("fecha_procesada = ? ");
		sqlUpdate.append("where ordene_operacion_id = ? ");
		return sqlUpdate.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getSqlUpdateOrden() {
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("UPDATE INFI_TB_204_ORDENES SET ");
		sqlUpdate.append("ordsta_id = ? ");
		sqlUpdate.append("where ordene_id = ? ");
		return sqlUpdate.toString();
	}
	
	protected void buscarParametros() throws Throwable { 
	
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select partip_nombre_parametro, parval_valor ");
		sqlSB.append("from INFI_TB_001_PARAM_GRUPO pgrupo ");
		sqlSB.append("inner join INFI_TB_002_PARAM_TIPOS ptipos on ptipos.pargrp_id = pgrupo.pargrp_id ");
		sqlSB.append("where pgrupo.pargrp_nombre = ? "); 
		sqlSB.append("order by partip_nombre_parametro ");
		
		Object [] criterios = new Object [1];
		criterios[0] = "INTERFACE SWIFT";
		
		try {
			resultSet = DBO.executeQuery(nombreDataSource, dso, sqlSB.toString(), criterios);
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception(e);
		} catch (Throwable t) {
			logger.error(t.getMessage()+ Utilitario.stackTraceException(t));
			throw new Exception(t);
		}
		
		while (resultSet.next()) {
			parametros.put(resultSet.getString("partip_nombre_parametro"), resultSet.getString("parval_valor"));
		}
		resultSet.close();
	}
	

	/**
	 * 
	 * @throws Exception
	 */
	protected void registrarIntentos() throws Exception {
		
		int pos = 0;
		dataInsertIntento= new Object[listaIntentos.size()][7];
		for (OperacionIntento beanIntento : listaIntentos) {
			
			dataInsertIntento[pos][0] = (String) intentoOperacionDAO.getSqlInsertIntento();
			dataInsertIntento[pos][1] = new Integer(7);
			dataInsertIntento[pos][2] = new Long(beanIntento.getIdOrden());
			dataInsertIntento[pos][3] = new Long(beanIntento.getIdOperacion());
			dataInsertIntento[pos][4] = new Long(beanIntento.getIdOperacionIntento());
			dataInsertIntento[pos][5] = calendHoy.getTime();
			dataInsertIntento[pos][6] = new Integer(0);
			++pos;
		}
		try {
			DBO.execBatchUpdate(null, dso, dataInsertIntento);	
		} catch (Throwable e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception(e);
		}
	}

	/**
	 * Metodo que permite ejecutar operaciones contra Swift
	 * @throws Throwable
	 */
	protected void actualizarIntentos (boolean envioExitoso) throws Throwable {
		
		ArrayList<String> consultas = new ArrayList<String>();
		for (OperacionIntento beanIntento : listaIntentos) {
			
			if (!envioExitoso) {
				beanIntento.setTextoError("Falla en el envio del archivo");
			}
			
			// 1.- Registrar el intento de ejecucion
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE INFI_TB_209_ORDENES_OPERAC_INT SET aplico = ");
			sql.append(1).append(" , error_desc ='").append(beanIntento.getTextoError()).append("' where ordene_operacion_id = ");
			sql.append(beanIntento.getIdOperacion()).append(" and operacion_intento_id = ").append(beanIntento.getIdOperacionIntento());
			consultas.add(sql.toString());
			
		}//fin for
		
		try {
			db.execBatch(dso,(String[]) consultas.toArray(new String[consultas.size()]));
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception(e);
		}
	}
/**
 * Actualiza las operaciones financieras de una orden a status <b>Aplicada</b><br>
 * Actualiza la orden a status <b>Procesada</b>
 * @param orden
 * @throws Throwable
 */
	protected ArrayList<String> actualizarOrdenOperaciones(Orden orden) throws Throwable {

		
		//arrayList que contendra las consultas a ser ejecutadas
		ArrayList<String> consultas = new ArrayList<String>();

		long idOrden = orden.getIdOrden();

		for (OrdenOperacion ordenOperacion : orden.getOperacion()) 
		{
						
			StringBuffer sql = new StringBuffer();

			sql.append("UPDATE INFI_TB_207_ORDENES_OPERACION SET status_operacion = '").append(ConstantesGenerales.STATUS_APLICADA);
			sql.append("', fecha_procesada = ").append("sysdate");
			sql.append(" where ordene_operacion_id = ").append(ordenOperacion.getIdOperacion());
			//System.out.println("SQL UPDATE Swift(1): "+sql.toString());
			//se agrega la consulta al arrayList
			consultas.add(sql.toString());
			
			 /* Busca todas las operaciones relacionadas a la operación que se esta APLICANDO y las marca
			 * con status APLICADA. Este método se invoca para cuando se envian las operaciones de SWIFT
			 * @param ordeneOperacionId id operación que se está aplicando.
			 * @return Conjunto de instrucciones SQL que deben ejecutarse
			 */
			sql = new StringBuffer();
			sql.append("UPDATE INFI_TB_207_ORDENES_OPERACION SET status_operacion = '").append(ConstantesGenerales.STATUS_APLICADA);
			sql.append("', fecha_procesada = ").append("sysdate");
			sql.append(" where ordene_relac_operacion_id = ").append(ordenOperacion.getIdOperacion());
			//System.out.println("SQL UPDATE Swift(2): "+sql.toString());
			//se agrega la consulta al arrayList
			consultas.add(sql.toString());			
			
		}//fin for
		
		//Actualizar la Orden	
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_204_ORDENES SET ordsta_id ='").append(StatusOrden.PROCESADA );
		sql.append("' where ordene_id = ").append(idOrden);
		consultas.add(sql.toString());
		
		return consultas;
	}
	
	/**
	 * Crear el registro del proceso asociado 
	 * @param usuarioId
	 * @throws Throwable
	 */
	protected void grabarProceso ( int usuarioId) throws Throwable {

		idEjecucion = DBO.dbGetSequence(null, dso, "INFI_TB_807_PROCESOS", 0);
		
		StringBuilder sql = new StringBuilder();			
		sql.append("insert into INFI_TB_807_PROCESOS (ejecucion_id, transa_id, usuario_id, fecha_inicio, fecha_valor) values(?, ?, ?, ?, ?)");
		
		Object [][] data = new Object [1][7];
		data [0][0] = sql.toString();
		data [0][1] = 7;
		data [0][2] = new Long(idEjecucion);
		data [0][3] = new String("INTERFACE SWIFT_ENVIO");		
		data [0][4] = new Long(usuarioId);	
		data [0][5] = new java.sql.Timestamp(calendHoy.getTimeInMillis());
		data [0][6] = calendHoy.getTime();	
		
		DBO.execBatchUpdate(null, dso, data);
	}
	
	protected void actualizaProceso ( String mensaje) throws Throwable {
		
		StringBuilder sql = new StringBuilder();	
		calendHoy = Calendar.getInstance();
		
		sql.append("update infi_tb_807_procesos set fecha_fin = sysdate");
		sql.append(", desc_error = '").append(mensaje);
		sql.append("' ").append("where ejecucion_id = ");
		sql.append(idEjecucion);

		db.exec(dso,sql.toString());
	}
	
	protected void generarTablaArchivo(String nombreArchivo,ArrayList<Orden> listaOrdenes) throws Exception{
		
		ArrayList<String> consultas = new ArrayList<String>();
		
		long idEjecucion = DBO.dbGetSequence(null, dso, SECUENCIA_CONTROL_ARCHIVOS, 0);
		StringBuffer sqlArchivo = new StringBuffer();
		sqlArchivo.append("insert into INFI_TB_803_CONTROL_ARCHIVOS (ejecucion_id, fecha, fecha_cierre, nombre, status, sistema_id) ");
		sqlArchivo.append("values (").append(idEjecucion).append(",sysdate,sysdate");
		sqlArchivo.append(", '").append(nombreArchivo).append("', '").append(ARCHIVOSTATUS_GENERADO);
		sqlArchivo.append("', ").append(Sistemas.SWIFT).append(")");
		consultas.add(sqlArchivo.toString());

		for (Orden beanOrden : listaOrdenes) {
			
			//ArrayList de operaciones por orden
			ArrayList<OrdenOperacion> ordenOperacionArrayList = beanOrden.getOperacion();
			
			for(OrdenOperacion ordenOperacion : ordenOperacionArrayList)
			{
				sqlArchivo = new StringBuffer();
				sqlArchivo.append("insert into INFI_TB_804_CONTROL_ARCH_DET (ejecucion_id, ordene_id, ordene_operacion_id, status) ");
				sqlArchivo.append("values (").append(idEjecucion).append(", ").append(beanOrden.getIdOrden());
				sqlArchivo.append(", ").append(ordenOperacion.getIdOperacion()).append(", '").append(ConstantesGenerales.STATUS_APLICADA).append("')");
				//Se agrega el query al arrayList
				consultas.add(sqlArchivo.toString());
			}//fin for operaciones
		} //fin for ordenes
		
		try {
			db.execBatch(dso,(String[]) consultas.toArray(new String[consultas.size()]));
		} catch (Throwable e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception (e);
		}
	}
	
	/**
	 * Elimina los espacios en blanco de una cadena dada
	 * @param cadenaConEspacios
	 * @return String cadenaSinEspacios
	 * @throws Exception
	 */
	public String eliminarEspaciosBlanco(String cadenaConEspacios)throws Exception{
		
		String cadenaArray[] = cadenaConEspacios.split(" ");
		String cadenaSinEspacios = "";
		
		for(int i=0;i<cadenaArray.length;i++)
		{
			if(!cadenaArray[i].equals(""))
				cadenaSinEspacios = cadenaSinEspacios + cadenaArray[i]+" ";
		}
		
		return cadenaSinEspacios;
	}
	
	public String establecerTipoSwift(OrdenOperacion ordenOperacion){
		int[] tipo = { 0, 0, 0, 0 };
		String nbBean = null;
		// Sin intermediario con BIC
		if (ordenOperacion.getCodigoBicBanco() != null && !ordenOperacion.getCodigoBicBanco().equals("") && ordenOperacion.getCodigoBicBanco().length() != 1) {
			tipo[0] = 1;
		}
		// Sin intermediario ABA
		if (ordenOperacion.getCodigoABA() != null && !ordenOperacion.getCodigoABA().equals("") && ordenOperacion.getCodigoABA().length() != 1) {
			tipo[1] = 1;
		}
		// Con intermediario BIC
		if (ordenOperacion.getCodigoBicBancoIntermediario() != null && !ordenOperacion.getCodigoBicBancoIntermediario().equals("") && ordenOperacion.getCodigoBicBancoIntermediario().length() != 1) {

			tipo[2] = 1;
		}
		// Con intermediario ABA
		if (ordenOperacion.getCodigoABAIntermediario() != null && !ordenOperacion.getCodigoABAIntermediario().equals("") && ordenOperacion.getCodigoABAIntermediario().length() != 1) {
			tipo[3] = 1;
		}

		// Analizar las combinaciones y asignar el bean correspondiente
		
		if (tipo[0] == 1 && (tipo[2] == 0 && tipo[3] == 0)) {
			beanSwift = new SwiftSinIntermedioBIC();
			nbBean = "SwiftSinIntermedioBIC";
		} else if (tipo[1] == 1 && (tipo[2] == 0 && tipo[3] == 0)) {
			beanSwift = new SwiftSinIntermedioABA();
			nbBean = "SwiftSinIntermedioABA";
		} else if (tipo[2] == 1) {
			beanSwift = new SwiftConIntermedioSwift();
			nbBean = "SwiftConIntermedioSwift";
		} else if (tipo[3] == 1) {
			beanSwift = new SwiftConIntermedioABA();
			nbBean = "SwiftConIntermedioABA";
		} else nbBean=null;
		return nbBean;
	}
	
	/**
	 * Set al BEAN SWIFT la cuenta correspondiente, dependiendo de la transaccion
	 * 
	 * @param transaccion
	 * @throws Exception
	 */
	public void buscarCuentaBDV(String transaccion) throws Exception {

		// DAO a utilizar
		CuentasBancoDAO cuentasBancoDAO = new CuentasBancoDAO(dso);

		// Se lista la cuenta asociada a la Transaccion
		cuentasBancoDAO.listarTransaccion(transaccion);
		cuentasBancoDAO.getDataSet().first();
		cuentasBancoDAO.getDataSet().next();

		// Set de valores al OBJECT AbstractSwifLT
		beanSwift.setHeaderCodBIC(cuentasBancoDAO.getDataSet().getValue("cod_bic_banco"));
		beanSwift.setHeaderDescripcion(cuentasBancoDAO.getDataSet().getValue("ds_banco"));
		beanSwift.setHeaderNumeroCuenta(cuentasBancoDAO.getDataSet().getValue("nu_cuenta"));
		if (transaccion.indexOf("_") > -1) {
			transaccion = Util.replace(transaccion, "_", " ");
		}
		beanSwift.setReferencia(transaccion);
	}// fin metodo
	
	/**
	 * Retorna el nombre para un archivo temporal
	 * 
	 * @return
	 * @throws Exception
	 */
	public String archivoTemporal(int count) throws Exception {
		String ruta = "";
		Date now = new Date();
		long lgStamp = now.getTime();
		String timeStamp = String.valueOf(lgStamp);
		ruta = "tmp" + timeStamp + count + ".txt";

		count++;

		return ruta;
	}
	
	private String getIp() {
		MessageContext curContext = MessageContext.getCurrentMessageContext();
		return curContext.getProperty(MessageContext.REMOTE_ADDR).toString();
	}  
	/**
	 * Ordena las operaciones con su correspondiente orden en un ArrayList La consulta debe estar ordenada por ordene_id
	 * 
	 * @param DataSet
	 *            operaciones operaciones a procesar
	 * @return ArrayList<Orden>
	 * @throws Exception
	 */
	public ArrayList<Orden> armarOrdenesConOperaciones(DataSet operaciones) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA2);
		SimpleDateFormat sdSwift2 = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);
		ArrayList<Orden> ordenes = new ArrayList<Orden>();
		com.bdv.infi.data.Cliente cliente = null;
		OrdenOperacion ordenOperacion = null;
		Orden orden = null;
		long idOrden = 0, id = 0;
		Date fecha = new Date();
		Date fechaCheque = null;

		if (operaciones != null && operaciones.count() > 0) {
			logger.info("armarOrdenesConOperaciones: Existen operaciones a procesar SWIFT");
			operaciones.first();

			while (operaciones.next()) {
				id = Long.valueOf(operaciones.getValue("ordene_id"));
				logger.info("armarOrdenesConOperaciones: ordeneId=" + id);
				if (idOrden != id) {
					if (orden != null) {
						ordenes.add(orden);
					}
					orden = new Orden();
					idOrden = id;
					//Datos de la orden
					orden.setIdOrden(idOrden);
					orden.setIdCliente(Long.parseLong(operaciones.getValue("client_id")));
					orden.setIdTransaccion(operaciones.getValue("transa_id"));
					orden.setIdOrdenRelacionada(Long.parseLong(operaciones.getValue("ORDENE_ID_RELACION")));
					orden.setCuentaCliente(operaciones.getValue("cuenta_cliente_bs"));
					orden.setIdOrdenClave(Long.parseLong(operaciones.getValue("id_ticket_clave")));
					
					//Datos de la cuenta Swift					
					orden.getCuentaSwift().setNombre_beneficiario(operaciones.getValue("NOMBRE_BENEFICIARIO"));
					orden.getCuentaSwift().setCtecta_numero(orden.getCuentaCliente());
					orden.getCuentaSwift().setCodPaisOrigen(operaciones.getValue("COD_PAIS_ORIGEN"));
					orden.getCuentaSwift().setDescCiudadOrigen(operaciones.getValue("DESC_CIUDAD_ORIGEN"));
					orden.getCuentaSwift().setDescEstadoOrigen(operaciones.getValue("DESC_ESTADO_ORIGEN"));	
					orden.getCuentaSwift().setCtecta_bcocta_bic(operaciones.getValue("CTECTA_BCOCTA_BIC"));
					orden.getCuentaSwift().setIdInstruccion(Long.parseLong(operaciones.getValue("TIPO_INSTRUCCION_ID")));
					orden.getCuentaSwift().setCtecta_observacion(operaciones.getValue("CTECTA_OBSERVACION"));
					
					//Datos del cliente					
					cliente = new com.bdv.infi.data.Cliente();
					cliente.setTelefono(operaciones.getValue("CLIENT_TELEFONO"));
					cliente.setTipoPersona(operaciones.getValue("TIPPER_ID"));
					cliente.setRifCedula(Long.parseLong(operaciones.getValue("CLIENT_CEDRIF")));
					cliente.setNombre(operaciones.getValue("CTECTA_NOMBRE"));
					orden.setCliente(cliente);
				}
				
				ordenOperacion = new OrdenOperacion();
				ordenOperacion.setIdOrden(Long.parseLong(operaciones.getValue("ordene_id")));

				if (operaciones.getValue("trnfin_id") != null)
					ordenOperacion.setIdTransaccionFinanciera(operaciones.getValue("trnfin_id"));
				ordenOperacion.setStatusOperacion(operaciones.getValue("status_operacion"));
				if (operaciones.getValue("aplica_reverso") != null)
					ordenOperacion.setAplicaReverso(Boolean.parseBoolean(operaciones.getValue("aplica_reverso")));
				if (operaciones.getValue("monto_operacion") != null)
					ordenOperacion.setMontoOperacion(new BigDecimal(operaciones.getValue("monto_operacion")));
				if (operaciones.getValue("tasa") != null)
					ordenOperacion.setTasa(new BigDecimal(operaciones.getValue("tasa")));
				//ITS-1172:Corrección de envío SWIFT con fecha actual en lugar de la fecha valor
				ordenOperacion.setFechaAplicar(sdSwift2.parse(operaciones.getValue("FECHA_APLICAR")));		
				ordenOperacion.setIdOperacion(Long.parseLong(operaciones.getValue("ordene_operacion_id")));
				if (operaciones.getValue("serial") != null)
					ordenOperacion.setSerialContable(operaciones.getValue("serial"));
				if (operaciones.getValue("in_comision") != null)
					ordenOperacion.setInComision(Integer.parseInt(operaciones.getValue("in_comision")));
				if (operaciones.getValue("ctecta_numero") != null)
					ordenOperacion.setNumeroCuenta(operaciones.getValue("ctecta_numero"));
				if (operaciones.getValue("ctecta_nombre") != null)
					ordenOperacion.setNombreReferenciaCuenta(operaciones.getValue("ctecta_nombre"));
				if (operaciones.getValue("ctecta_bcocta_bco") != null)
					ordenOperacion.setNombreBanco(operaciones.getValue("ctecta_bcocta_bco"));
				if (operaciones.getValue("ctecta_bcocta_direccion") != null)
					ordenOperacion.setDireccionBanco(operaciones.getValue("ctecta_bcocta_direccion"));
				if (operaciones.getValue("ctecta_bcocta_swift") != null)
					ordenOperacion.setCodigoSwiftBanco(operaciones.getValue("ctecta_bcocta_swift"));
				if (operaciones.getValue("ctecta_bcocta_bic") != null)
					ordenOperacion.setCodigoBicBanco(operaciones.getValue("ctecta_bcocta_bic"));
				if (operaciones.getValue("ctecta_bcocta_telefono") != null)
					ordenOperacion.setTelefonoBanco(operaciones.getValue("ctecta_bcocta_telefono"));
				if (operaciones.getValue("ctecta_bcocta_aba") != null)
					ordenOperacion.setCodigoABA(operaciones.getValue("ctecta_bcocta_aba"));
				if (operaciones.getValue("ctecta_bcocta_pais") != null)
					ordenOperacion.setPaisBancoCuenta(operaciones.getValue("ctecta_bcocta_pais"));
				if (operaciones.getValue("ctecta_bcoint_bco") != null)
					ordenOperacion.setNombreBancoIntermediario(operaciones.getValue("ctecta_bcoint_bco"));
				if (operaciones.getValue("ctecta_bcoint_direccion") != null)
					ordenOperacion.setDireccionBancoIntermediario(operaciones.getValue("ctecta_bcoint_direccion"));
				if (operaciones.getValue("ctecta_bcoint_swift") != null)
					ordenOperacion.setCodigoSwiftBancoIntermediario(operaciones.getValue("ctecta_bcoint_swift"));
				if (operaciones.getValue("ctecta_bcoint_bic") != null)
					ordenOperacion.setCodigoBicBancoIntermediario(operaciones.getValue("ctecta_bcoint_bic"));
				if (operaciones.getValue("ctecta_bcoint_telefono") != null)
					ordenOperacion.setTelefonoBancoIntermediario(operaciones.getValue("ctecta_bcoint_telefono"));
				if (operaciones.getValue("ctecta_bcoint_aba") != null)
					ordenOperacion.setCodigoABAIntermediario(operaciones.getValue("ctecta_bcoint_aba"));
				if (operaciones.getValue("ctecta_bcoint_pais") != null)
					ordenOperacion.setPaisBancoIntermediario(operaciones.getValue("ctecta_bcoint_pais"));
				ordenOperacion.setTipoTransaccionFinanc(operaciones.getValue("TRNF_TIPO"));
				ordenOperacion.setNumeroCheque(operaciones.getValue("CHEQUE_NUMERO"));
				if (operaciones.getValue("fecha_pagoCH") != null && !operaciones.getValue("fecha_pagoCH").equals(""))
					fechaCheque = sdf.parse(operaciones.getValue("fecha_pagoCH"));
				ordenOperacion.setFechaPagoCheque(fechaCheque);
				ordenOperacion.setIdMoneda(operaciones.getValue("moneda_id"));

				orden.getOperacion().add(ordenOperacion);

			}
			ordenes.add(orden);
		}
		return ordenes;
	}// fin metodo
	
	/**
	 * Metodo que procesa las ordenes para crear archivos con operaciones financieras MT103,MT110 y enviarlos vía FTP a sus Respectivas colas:
	 * <li>Tesoreria</li>
	 * 
	 * @param listaOrdenes
	 * @param usuarioId
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	public void aplicarOrdenes(ArrayList<Orden> listaOrdenes, int usuarioId) throws Throwable {

		boolean envioExitoso = true;
		String mensaje = "";
		String rutaArchivo = rutaArchivoSwift + this.archivoTemporal(count);
		FTPUtil transfer = null;
		String archivoSwiftCola = null;

		ArrayList<String> consultas = new ArrayList<String>();

		// Si la lista de ordenes es vacia se envía un msg al log
		if (listaOrdenes.size() == 0) {
			throw new Exception("No hay ordenes para ser procesadas");
		}

		// Grabar el registro de proceso
		try {
			grabarProceso(usuarioId);
		} catch (Exception e) {
			logger.error(e.getMessage() + Utilitario.stackTraceException(e));
		}

		// Buscar parametros de comunicacion con Swift
		parametros = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_SWIFT);
		try {
			for (Orden orden : listaOrdenes) {
				logger.info("Orden (Swift): " + orden.getIdOrden());
				if (orden.getOperacion().size() == 0)
					continue;

				// Se arman los archivos MT103,MT110 para ser enviados vía FTP
				armarBeanSwift(orden);
				consultas.addAll(actualizarOrdenOperaciones(orden));

			}// fin for ordenes
			logger.info("Querys actualizacion de ordenes y operaciones: " + consultas.toString());
			try {
				if (!archivoSwiftMT100.isEmpty()) {
					FileUtil.put(rutaArchivo, archivoSwiftMT100, false);
					if (logger.isDebugEnabled()) {
						//logger.debug("Envio de archivo via FTP( ruta+archivo): " + rutaArchivo + "\n" + archivoSwiftMT100);
						logger.debug("Envio de archivo via FTP( ruta+archivo): " + rutaArchivo + "\n" );
					}
				}

			} catch (IOException e) {
				logger.error(e.getMessage() + Utilitario.stackTraceException(e));
				throw new Exception(e);
			} catch (Exception e) {
				logger.error(e.getMessage() + Utilitario.stackTraceException(e));
				throw new Exception(e);
			}

			// Enviar el Archivo al servidor Swift
			transfer = new FTPUtil(parametros.get(ParametrosSistema.SWIFT_SERVIDOR), dso);
			archivoSwiftCola = parametros.get(paramColaArchivoSwift) + parametros.get(paramNombreArchivoSwift) + this.archivoTemporal(count);

			// Envio de archivo a la cola SWIFT 
			envioExitoso = false;
			if (!archivoSwiftMT100.isEmpty()) {
				logger.info("Inicio de envío de archivo a cola de "+rutaArchivoSwift);
				try {
					transfer.putFTPAscci(rutaArchivo, archivoSwiftCola, "", false);
					envioExitoso = true;
					logger.info("Enviado archivo a cola de "+rutaArchivoSwift);
				} catch (Exception e) {
					logger.error(e.getMessage() + Utilitario.stackTraceException(e));
					mensaje = e.getMessage();
					envioExitoso = false;

				}
			}

			// Se genera un registro en la tabla 803 indicando si el archivo fue enviado
			if (envioExitoso) {
				generarTablaArchivo(rutaArchivo, arrayListTesoreria);
				logger.info("Actualizacion de ordenes y operaciones: " + consultas.toString());
				db.execBatch(dso, (String[]) consultas.toArray(new String[consultas.size()]));
			} else {
				throw new Exception(mensaje);
			}

		} catch (Exception e) {
			mensaje = e.getMessage();
			logger.error(e.getMessage() + Utilitario.stackTraceException(e));
			throw e;
		} finally {
			// Eliminar el archivo temporal
			try {
				if (!archivoSwiftMT100.isEmpty())
					FileUtil.delete(rutaArchivo);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new Exception(e);
			}
			actualizaProceso(mensaje);
		}
	} // Fin de Metodo aplicarOrdenes()
	
	/**
	 * Se encarga de crear los mensajes por cada operación financiera. Dependiendo de la condición crea un archivo MT103(sin número de cheque) o MT110(Con número de cheque) Guarda el contenido de cada archivo en su respectivo formato para luego ser enviado a la Cola Correspondiente:
	 * <li>Custodia</li>
	 * <li>Tesoreria</li>
	 * 
	 * @param beanOrden
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	protected void armarBeanSwift(Orden beanOrden) throws Throwable {
		HashMap<String, String> parametrosSistema = new HashMap<String, String>();
		BigDecimal bdAux;
		String fechaOperacion = "";
		BigDecimal montoComision = null;
		String tipoCliente;
		ArrayList<OrdenOperacion> listaOperacionesArrayList = null; // ArrayList que contiene el objeto OrdenOperacion
		HashMap<String, OrdenOperacion> operacionesHashMap = null; // Hashmap que contendra las monedas y monto
		ParametrosDAO parametrosDAO = new ParametrosDAO(dso);

		// Se buscan los parametros de SWIFT
		parametrosSistema = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_SWIFT);

		// Se verifica el tipo de Persona para buscar en los Parametros de sistema la comision correspondiente
		if (beanOrden.getCliente().getTipoPersona().equalsIgnoreCase("V") || beanOrden.getCliente().getTipoPersona().equalsIgnoreCase("E")) {
			montoComision = new BigDecimal(parametrosSistema.get(ParametrosSistema.CO_SWIFT_NATURALES_USD));
		} else if (beanOrden.getCliente().getTipoPersona().equalsIgnoreCase("J")) {
			montoComision = new BigDecimal(parametrosSistema.get(ParametrosSistema.CO_SWIFT_JURIDICO_USD));
		}

		operacionesHashMap = new HashMap<String, OrdenOperacion>();
		// Recorremos las operaciones financieras para sumar montos que sean de una misma moneda
		for (OrdenOperacion ordenOperacion : beanOrden.getOperacion()) {

			// Si contiene la moneda
			if (operacionesHashMap.containsKey(ordenOperacion.getIdMoneda())) {
				OrdenOperacion ordenOperacionObject = operacionesHashMap.get(ordenOperacion.getIdMoneda());
				BigDecimal nuevoMonto = ordenOperacion.getMontoOperacion().add(ordenOperacionObject.getMontoOperacion());
				// Set del nuevo monto
				ordenOperacionObject.setMontoOperacion(nuevoMonto);
				// Set del nuevo valor al hashmap
				operacionesHashMap.remove(ordenOperacion.getIdMoneda());
				operacionesHashMap.put(ordenOperacion.getIdMoneda(), ordenOperacionObject);

			} else {
				// Set al objeto hashmap del objeto OrdenOperacion
				operacionesHashMap.put(ordenOperacion.getIdMoneda(), ordenOperacion);

			}// fin else
		}// fin for Listar Operaciones.

		// Iteramos el hashmap para agregar los objetos al arraylist
		listaOperacionesArrayList = new ArrayList<OrdenOperacion>();
		Set set = operacionesHashMap.entrySet();
		Iterator iter = set.iterator();

		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			// Agregamos el objeto al arrayList
			listaOperacionesArrayList.add((OrdenOperacion) entry.getValue());

		}

		// ************************************************
		// Se inicia la construccion del mensaje SWIFT
		// ************************************************

		for (OrdenOperacion ordenOperacion : listaOperacionesArrayList) {
			// Si el tipo de instruccion es SWIFT
			if (beanOrden.getCuentaSwift().getIdInstruccion() == TipoInstruccion.CUENTA_SWIFT) {
				String textoRegSwift = "";
				boolean conIntermediario;

				if ((ordenOperacion.getCodigoSwiftBanco() == null || ordenOperacion.getCodigoSwiftBanco().equals("")) && (!ordenOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(CREDITO))) {
					continue;
				}
				
				//Evaluar si la instrucción de pago es con intermediario
				if(ordenOperacion.getCodigoBicBancoIntermediario()!=null && !ordenOperacion.getCodigoBicBancoIntermediario().trim().equals("")){
					beanSwift = new SwiftConIntermedioSwift();
					conIntermediario = true;
				}else{//Sin intermediario
					beanSwift = new SwiftSinIntermedioBIC();
					conIntermediario = false;
				}			
				beanSwift.setTipoProducto(tipoProducto);		

				// Buscamos la cuenta asociada a la transaccion para la cabecera del mensaje SWIFT MT 103
				this.buscarCuentaBDV(beanOrden.getIdTransaccion());

				
				// 14/08/2013: inclusion de validacion para casos de ORDEN_PAGO generada a partir de una TOMA_ORDEN
				beanSwift.setNumeroDeOrden(beanSwift.getNroOrden(beanOrden.getIdTransaccion(),beanOrden.getIdOrden(),beanOrden.getIdOrdenRelacionada()));
				
				tipoCliente = beanOrden.getCliente().getTipoPersona();
				fechaOperacion = sdSwift.format(ordenOperacion.getFechaAplicar());
				bdAux = ordenOperacion.getMontoOperacion();
				bdAux = bdAux.setScale(2, BigDecimal.ROUND_HALF_EVEN);

				// Datos de la orden y cliente
				beanSwift.setTipoTransaccion(beanOrden.getIdTransaccion());
				beanSwift.setCodSwiftBDV(parametros.get(ParametrosSistema.SWIFT_CODIGO_SWIFT_BDV));
				beanSwift.setCedulaCliente(tipoCliente + "-" + String.valueOf(beanOrden.getCliente().getRifCedula()));
				beanSwift.setCedulaClienteSinGuion(tipoCliente + String.valueOf(beanOrden.getCliente().getRifCedula()));
				beanSwift.setNombreCliente(beanOrden.getCliente().getNombre());
				beanSwift.setTelefonoCliente(beanOrden.getCliente().getTelefono());
				String[] direccionCliente = {""};
				if(beanOrden.getCliente().getDireccion()!=null)direccionCliente[0] = beanOrden.getCliente().getDireccion();
				beanSwift.setDireccionCliente(direccionCliente);
				beanSwift.setIdOperacion(String.valueOf(ordenOperacion.getIdOperacion()));
				beanSwift.setFechaOperacion(fechaOperacion);
				beanSwift.setMontoOperacion(ordenOperacion.getMontoOperacion() != null ? Util.replace(String.valueOf(ordenOperacion.getMontoOperacion().setScale(2, BigDecimal.ROUND_HALF_EVEN)), ".", ",") : "0,00");
				beanSwift.setSiglasMoneda(ordenOperacion.getIdMoneda());

				// Datos de la instruccion de pago
				beanSwift.setCuentaDestinatario(ordenOperacion.getNumeroCuenta()); 
				beanSwift.setNombreDestinatario(beanOrden.getCuentaSwift().getNombre_beneficiario());
				beanSwift.setReferencia(beanOrden.getIdTransaccion());
				beanSwift.setUnidadInversion(beanOrden.getCuentaSwift().getCtecta_observacion());
				beanSwift.setCuentaCliente(beanOrden.getCuentaSwift().getCtecta_numero());
				beanSwift.setCodPaisResideCliente(beanOrden.getCuentaSwift().getCodPaisOrigen());
				beanSwift.setEstadoResideCliente(beanOrden.getCuentaSwift().getDescEstadoOrigen());
				beanSwift.setCiudadResideCliente(beanOrden.getCuentaSwift().getDescCiudadOrigen());

				logger.debug("tipoProducto: "+tipoProducto+", beanOrden.getIdOrdenClave(): " + beanOrden.getIdOrdenClave()+", beanOrden.getIdTransaccion(): " + beanOrden.getIdTransaccion()+
						", ordenOperacion.getIdOrden(): " + ordenOperacion.getIdOrden()+", beanOrden.getIdOrdenRelacionada(): " + beanOrden.getIdOrdenRelacionada()+
						", beanSwift.setNumeroDeOrden: " + beanSwift.getNumeroDeOrden()+", beanSwift.getCuentaCliente(): " + beanSwift.getCuentaCliente()+
						", beneficiario: " + beanOrden.getCuentaSwift().getNombre_beneficiario()+",cuenta cliente: "+beanOrden.getCuentaSwift().getCtecta_numero()+
						", numero de cuenta: "+ordenOperacion.getNumeroCuenta());
								
				beanSwift.setCurrencyInstructedAmount(ordenOperacion.getMontoOperacion().subtract(montoComision).setScale(2, BigDecimal.ROUND_HALF_EVEN));

				beanSwift.setBENOUR(OUR);
				beanSwift.setCodBancoDestino(ordenOperacion.getCodigoBicBanco());
				
				//Datos Especificos
				if(conIntermediario){
					beanSwift.setCodBancoIntermediario(ordenOperacion.getCodigoBicBancoIntermediario());
					beanSwift.setNumeroCuentaBancoEnIntermediario(ordenOperacion.getCodigoSwiftBancoIntermediario());
					beanSwift.setNombreBancoIntermediario(ordenOperacion.getNombreBancoIntermediario());
				}

				textoRegSwift = beanSwift.getBodyLT();				
				//System.out.println("textoRegSwift: " + textoRegSwift);
				
				// Se agrega el registro a la cola de Clavenet Personal
				archivoSwiftMT100.add(textoRegSwift);
				// Se agrega orden al arrayList para el control de archivos
				arrayListTesoreria.add(beanOrden);

			}// ************************************ Fin MT103
		}

	}// fin metodo

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}


}