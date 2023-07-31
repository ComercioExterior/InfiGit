package models.intercambio.recepcion.lectura_archivo_subasta_divisas;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ActividadEconomicaDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SectorProductivoDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDetalle;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.IngresoOpicsSitme;
import com.bdv.infi.logic.ProcesarDocumentos;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreosCall;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
import com.bdv.infi.logic.interfaz_varias.MensajeEstadistica;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.bdv.infi_toma_orden.logic.TomaDeOrdenes;

/**
 * Contiene la estructura logica para el manejo operativo del producto SICAD 1
 * */
public class LeerArchivoSubastaDivisas extends Transaccion {
	
	/**Parametros de la Toma de Orden 
	 */
	
	private com.bdv.infi.dao.Transaccion transaccion;
	private long contadorOrdenesAdjudicadas;
	
	SolicitudClavenet ordenOpics=null; 
	
	ArrayList<String> querysTransacciones=new ArrayList<String>();
	
	OrdenDAO ordenDAOClavenet;
	
	//Variable que contendra el valor del tipo de proceso que se esta ejecutando (Proceso de Adjudicacion o rechazo de solicitud)
	private String tipoProcesoAdjRech;
	
	CuentaCliente clienteCuentaEntidad=null;
	ClienteCuentasDAO clienteCuentasDAO=null;
	ClienteDAO clienteDAOClavenet;
	
	ControlArchivoDAO controlArchivoDAO;
	TitulosDAO ordenesTitulos;
	EmpresaDefinicionDAO empresaDAO;
	SolicitudClavenet ordenClavenet;
	OrdenDAO ordenCLaveNet;
	ActividadEconomicaDAO actividadEconomicaDAO=null;
	SectorProductivoDAO sectorProductivoDAO=null;
	
	BigDecimal totalMontoComisionOperaciones=null;
	BigDecimal totalMontoCapitalOperaciones=null;
	//private final int LISTAR_INFORMACION_VEHICULO=3;
	//DecimalFormat form = (DecimalFormat)NumberFormat.getInstance();
    	int cantidadOrdenesAdj=0;	int acumuladorOrdenesAdj=0;
	
	/*private UnidadInversionDAO unidadInversionDao;
	private HashMap<String, Object> parametrosEntrada = new HashMap<String, Object>();
	private ArrayList<CampoDinamico> ArrayCamposDinamicos = new ArrayList<CampoDinamico>();
	private ArrayList<PrecioRecompra> ArrayRecompraTitulos = new ArrayList<PrecioRecompra>();
	private ArrayList<OrdenOperacion> ArrayComisiones = new ArrayList<OrdenOperacion>();*/
	
	TomaOrdenSimulada beanTOSimulada 	= null;
	private	TomaDeOrdenes boSTO;
	//StringBuffer TrazasProceso = new StringBuffer();
	DataSet transaccionesFijasAdjudicacion = new DataSet();
	String codigo_operacion = null;
	String nombre_operacion = null;
	Boolean esBDV;
	Boolean existeOrdenCreditoVehiculo;
	String idOrdenVehiculo = null;
	TomaOrdenDAO tomaOrdenDAO = null;
	private MensajeOpicsDAO mensajeOpicsDAO = null;
	String unidadInversion = null;
	String numero_oficina = null;
	BigDecimal cero = null;
	boolean operacionEncontrada = false;//indicador de operacion encontrada en la lista de operaciones simuladas
	private boolean multiplesUI=false;
	private boolean cobroEnLinea=false;
	private HashMap hashCobroEnLineaPorUI = new HashMap();
	private HashMap hashIdPorUnidad = new HashMap();
	private DataSet mensajesPorRif = new DataSet(); 
	private HashMap<String, String> hashTituloBCV = new HashMap<String, String>();
		
	private DataSet dataExtComisiones = new DataSet();//Dataset para almacenar la data extendida referente a la informacion de las comisiones de las ordenes a adjudicar
	private Logger logger = Logger.getLogger(LeerArchivoSubastaDivisas.class);	
	
	private DataSource dataSource;
	//Cache de transacciones fijas y vehículo
	private HashMap<String,DataSet> transaccionesFijasCache = new HashMap<String,DataSet>();
	private TransaccionFijaDAO fijaDAO;
	private DataSet empresasCache=null;	

	String rifBDV = "";
	Proceso proceso = null;
	ProcesosDAO procesosDAO = null;
	EmpresaDefinicionDAO empresaDefinicionDAO = null;
	
	
	
	OperacionDAO operacionDAO; 
	UnidadInversionDAO unidadInvDAO;
	OrdenDAO ordenDAO;
	ClienteDAO clienteDAO;
	
	String unidadInversionParametro="";
	
	private final String ADJUDICADO_CERO="ADJUDICADO CERO";
	private final String ADJUDICADO_IGUAL_SOLICITADO="ADJUDICADO IGUAL A SOLICITADO";
	private final String ADJUDICADO_MENOR_SOLICITADO="ADJUDICADO MENOR A SOLICITADO";
	
	public void execute() throws Exception {
		iniciarProceso();
		
		String ejecucion_id="";
		
		try {

			transaccion = new com.bdv.infi.dao.Transaccion(_dso);
			transaccion.begin();
			
			
			fijaDAO = new TransaccionFijaDAO(_dso);
			
			String contenidoDocumento	= getSessionObject("contenidoDocumento").toString();
			String nombreDocumento		= getSessionObject("nombreDocumento").toString();
			
			FileInputStream documento = new FileInputStream(contenidoDocumento);//contenido del documento a leer		
			numero_oficina 		= (String)_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);		
			HSSFWorkbook libro = new HSSFWorkbook(documento);	
			
			
			HSSFSheet hoja = null;//hoja
			HSSFCell celdaControl = null;//primera celda o columna		
			HSSFCell identificadorArchivo = null;//1RA columna de Identificacion del tipo de Archivo
			
			//******** Bloque de atributos pertenecientes al proceso de Adjudicacion *******/
			HSSFCell ui = null;//columna Unidad de Inversion		
			HSSFCell codOrden = null;//columna codigo de orden		
			HSSFCell cedula = null;//columna cedula			
			HSSFCell mtoSolicitado = null;//columna monto adjudicado	
			HSSFCell mtoAdjudicado = null;//columna monto adjudicado
			HSSFCell tasaCambioAprobada = null;//columna que indica la tasa de cambio asignada		
			HSSFCell fechaValor = null;//columna de fecha valor de la operacion
			//******* Bloque de atributos pertenecientes al proceso de Adjudicacion *******/
	
			
			String tipoOperacionParametro="";
		
			HSSFRow row = null; //fila			
			String idCliente = null;//Id del cliente
			
			
			unidadInvDAO=new UnidadInversionDAO(_dso);
			ordenDAO=new OrdenDAO(_dso);
			clienteDAO=new ClienteDAO(_dso);
			
			
			//String cuentaCliente = null;//numero cuenta cliente. Obtenida de la orden
						
	
			//**************************************************
			BigDecimal montoSolicitado = new BigDecimal(0);//monto que pidio el cliente		
			BigDecimal montoAdjudicado = null;//new BigDecimal(0); //monto adjudicado
			BigDecimal tasaCambio=null;
			
			Orden orden=null;
			//ID de la orden que se esta procesando en la Adjudicacion de Subasta Divisas 		
			
			unidadInversionParametro=_record.getValue("unidad_inversion");
			tipoOperacionParametro=_record.getValue("tipo_operacion");
			
			ejecucion_id=MSCModelExtend.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS); //Tomamos la secuencia fuera del for para que todos los registros tengan el mismo valor
				
			
			setSessionObject("id_ejecucion_subasta_divisas",null);
			setSessionObject("id_ejecucion_subasta_divisas",ejecucion_id);
			
			for (int i = 0; i < libro.getNumberOfSheets(); i++) {
				
				hoja = libro.getSheet(libro.getSheetName(i));//objeto hoja
	
				for (int fila = 0; fila< hoja.getPhysicalNumberOfRows(); fila++) {//recorrido de filas
					//Identificador de la orden a procesar por fila.
					long idOrdenProcesar=0;
					
					row = hoja.getRow(fila);//Obtener la fila j de la hoja		
					//vas leyendo por cada celda (columna) de la fila
					//para cada fila el numero de columnas es fijo
					celdaControl = row.getCell((short)0);
					int tipo = celdaControl.getCellType();						
					if(tipo==HSSFCell.CELL_TYPE_BLANK){// romperemos la lectura de filas al encontrar la primera celda vacia (fin de registros)						
						if (fila==2){						
							//System.out.println("Archivo sin Registros para Procesar o Mal Formado");
							fila = hoja.getPhysicalNumberOfRows();
							logger.info("* Archivo sin Registros para Procesar o Mal Formado, Verifique e Intente Nuevamente)\n");
							break;
						} 
						
						if (fila>2){
							//System.out.println(" Fin de Lectura de Registros Exitosamente");	
							fila = hoja.getPhysicalNumberOfRows();
	
							logger.info("* Fin de Lectura de Registros Exitosamente.\n");
							break;
						}
					}
						
							if(fila==0){//Verificacion del campo 1 --> Campo de comprobacion de Archivo de Adjudicacion SUBASTA DIVISAS
								identificadorArchivo=row.getCell((short)0);							
								if(identificadorArchivo.toString().equals(ConstantesGenerales.IDENTIFICADOR_PLANTILLA_ADJUDICACION_SUBASTA_DIVISAS)){
									tipoProcesoAdjRech=ConstantesGenerales.IDENTIFICADOR_PROCESO_ADJUDICACION_SUBASTA_DIVISAS;
									if(!tipoOperacionParametro.equals(tipoProcesoAdjRech)){
										logger.error("La plantilla excel ingresada no concuerda con el proceso seleccionado en la pantalla principal por favor verifique");
										throw new Exception("La plantilla excel ingresada no concuerda con el proceso seleccionado en la pantalla principal por favor verifique");	
									}															
								}else if(identificadorArchivo.toString().equals(ConstantesGenerales.IDENTIFICADOR_PLANTILLA_RECHAZO_SUBASTA_DIVISAS)){
									tipoProcesoAdjRech=ConstantesGenerales.IDENTIFICADOR_PROCESO_RECHAZO_SUBASTA_DIVISAS;
									if(!tipoOperacionParametro.equals(tipoProcesoAdjRech)){
										logger.error("La plantilla excel ingresada no concuerda con el proceso seleccionado en la pantalla principal por favor verifique");
										throw new Exception("La plantilla excel ingresada no concuerda con el proceso seleccionado en la pantalla principal por favor verifique");	
									}
								}else {
									logger.error("El archivo ingresado no es el correspondiente al proceso de Recepcion / Lectura Archivo Subasta Divisas");
									throw new Exception("El archivo ingresado no es el correspondiente al proceso de Recepcion / Lectura Archivo Subasta Divisas");
								}
							} else if(fila>=2){//Recorrido de las filas del archivo a partir de la fila N° 2
															
								//***VALIDACIONES COMUNES PARA RECHAZOS Y ADJUDICACIONES	
								ui=row.getCell((short)0);
								codOrden = row.getCell((short)1);
								cedula = row.getCell((short)2);
								mtoSolicitado=row.getCell((short)3);
								mtoAdjudicado=row.getCell((short)4);
								tasaCambioAprobada=row.getCell((short)5);
								fechaValor= row.getCell((short)6);
													
							  /*//System.out.println("CAMPO UNIDAD INVERSION -------> " + ui.toString());
								//System.out.println("CAMPO CODIGO OPERACION -------> " + codOrden.toString());
								//System.out.println("CAMPO CEDULA -------> " + cedula.toString());
								//System.out.println("CAMPO MONTO SOLICITADO -------> " + mtoSolicitado.toString());
								//System.out.println("CAMPO MONTO ADJUDICADO -------> " + mtoAdjudicado.toString());
								//System.out.println("CAMPO TASA CAMBIO APROBADA -------> " + tasaCambioAprobada.toString());
								//System.out.println("CAMPO FECHA VALOR -------> " + fechaValor.toString());*/														
								
								//Validacion campo CODIGO ORDEN
								try{
									idOrdenProcesar=Long.parseLong(codOrden.toString().replaceAll("\\.0", ""));
									
								}catch(NumberFormatException ex){
									logger.info(" El valor del campo Código orden ingresado no es de tipo numerico ---->  "  + codOrden.toString());
									//System.out.println(" El valor del campo Código orden ingresado no es de tipo numerico ---->  "  + codOrden.toString());
									continue;
								}
								
								orden=ordenDAO.listarOrden(idOrdenProcesar,false,false,false,true,false);
								orden.setIdEjecucion(Long.parseLong(ejecucion_id));

								tasaCambio=new BigDecimal(tasaCambioAprobada.toString());
								//Procesamiento de plantilla de Adjudicacion
								if(tipoProcesoAdjRech.equals(ConstantesGenerales.IDENTIFICADOR_PROCESO_ADJUDICACION_SUBASTA_DIVISAS)){									
								    //Validacion Fecha Valor
									String dia=null;								
									String mes=null;								
									String anio=null;
									try {
										dia=fechaValor.toString().substring(0,2);								
										mes=fechaValor.toString().substring(3,5);								
										anio=fechaValor.toString().substring(6,10);
									}catch(Exception e){
										//System.out.println("Ocurrio un error con el campo Fecha Valor del archivo, por favor verifique que el formato sea DD/MM/YYYY. Descripcion del error " + e.getMessage());
										logger.info("Ocurrio un error con el campo Fecha Valor del archivo, por favor verifique que el formato sea DD/MM/YYYY. Descripcion del error " + e.getMessage());
										continue;
									}
									
									String fechaValorString=new String();
									fechaValorString=fechaValorString.concat(dia).concat("-").concat(mes).concat("-").concat(anio);
									
									DateFormat df = new SimpleDateFormat("dd-MM-yyyy");   
									String s = df.format(new Date());  
	
									//Validacion campo FECHA VALOR
									if((Utilitario.StringToDate(s,"dd-MM-yyyy")).compareTo(Utilitario.StringToDate(fechaValorString, "dd-MM-yyyy"))>0){
										logger.info("Orden: "+codOrden.toString().replaceAll("\\.0", "") +" - La fecha valor ingresada de la orden es menor a la fecha actual por favor verifique");
										//System.out.println("Orden: "+codOrden.toString().replaceAll("\\.0", "") +" - La fecha valor ingresada de la orden es menor a la fecha actual por favor verifique");
										continue;
									}
									
									//Validacion campo MONTO_SOLICITADO
									//TODO Verificar si se debe validar que el monto de compra NO sea menor al solicitado por el cliente 
									try{
										if(Double.parseDouble(mtoSolicitado.toString())<=0){
											logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El campo de monto Mto Solicitado $ en el archivo excel posee un valor menor o igual a cero para la orden");
											//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El campo de monto Mto Compra $ en el archivo excel posee un valor menor o igual a cero para la orden");
											continue;
										}
									}catch(NumberFormatException ex){
										logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Mto Solicitado $ en el archivo excel no es de tipo numerico por favor verifique");
										//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Mto Compra $ en el archivo excel no es de tipo numerico por favor verifique");
										continue;
									}
									//TODO Verificar si se debe validar que el monto solicitado en la plantilla no sea menor al que se guardo en el registro de base de datos
									montoSolicitado=new BigDecimal(Double.parseDouble(mtoSolicitado.toString()));
									
									//Validacion campo MONTO_ADJUDICADO
									//TODO Verificar si se debe validar que el monto de compra NO sea menor al solicitado por el cliente 
									try{
										if(Double.parseDouble(mtoAdjudicado.toString())<0){
											logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El campo de monto Mto Adjudicado $ en el archivo excel posee un valor menor a cero ");
											//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El campo de monto Mto Adjudicado $ en el archivo excel posee un valor menor a cero");
											continue;
										}
									}catch(NumberFormatException ex){
										logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Mto Adjudicado $ en el archivo excel no es de tipo numerico por favor verifique");
										//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Mto adjudicado $ en el archivo excel no es de tipo numerico por favor verifique");
										continue;
									}													
									montoAdjudicado=new BigDecimal(Double.parseDouble(mtoAdjudicado.toString()));
									
									if(montoAdjudicado.compareTo(montoSolicitado)>0){
										logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Mto Adjudicado $ en el archivo excel es mayor  al monto solicitado");
										//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Mto Adjudicado $ en el archivo excel es mayor  al monto solicitado");
										continue;
									}
									//Validacion campo TASA_CAMBIO_APROBADA
									//TODO Verificar si se debe validar que la tasa de cambio aprobada NO sea menor a la tasa negociada por el cliente
									try{
										if(Double.parseDouble(tasaCambioAprobada.toString())<=0){
											logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El campo Tasa cambio Asignada en el archivo excel posee un valor menor o igual a cero por favor verifique");
											//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El campo Tasa cambio Asignada en el archivo excel posee un valor menor o igual a cero por favor verifique");
											continue;
										}
									}catch(NumberFormatException ex){
										logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Tasa cambio Asignada en el archivo excel no es de tipo numerico por favor verifique");
										//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Tasa cambio Asignada en el archivo excel no es de tipo numerico por favor verifique");
										continue;
									}
									tasaCambio=new BigDecimal(Double.parseDouble(tasaCambioAprobada.toString()));
									
								    
									
									//VALIDACION UNIDAD DE INVERSION *******INFI_TB_106_UNIDAD_INVERSION 
									unidadInvDAO.listarPorNombre(ui.toString(),ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA,null);								
									if (unidadInvDAO.getDataSet().count()==0){// IF UNIDAD DE INVERSION NO EXISTE
										logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "La unidad de inversion '"+ui.toString()+"' no existe en la base de datos, por favor verifique el nombre de la Unidad de Inversion");
										//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "La unidad de inversion '"+ui.toString()+"' no existe en la base de datos, por favor verifique el nombre de la Unidad de Inversion");									
										continue;								
									}else {
										
										unidadInvDAO.getDataSet().first();
										unidadInvDAO.getDataSet().next();									
										
										
										if(!unidadInvDAO.getDataSet().getValue("UNDINV_ID").equals(unidadInversionParametro)){
											logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "La unidad de inversion '"+ui.toString()+"' especificada en el archivo no coincide con la seleccionada en la pantalla principal");
											//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "La unidad de inversion '"+ui.toString()+"' especificada en el archivo no coincide con la seleccionada en la pantalla principal");									
											continue;
										}
										
									}
										
									//Busqueda de la orden por el ID ingresado en la plantilla de Adjudicacion			
									//TODO MODIFICAR ESTATIS A ENVIADA 
									ordenDAO.listarDatosBasicosOrdenPorStatus(idOrdenProcesar,StatusOrden.ENVIADA);			
									
									//VALIDACION ORDEN *******INFI_TB_204_ORDENES 																		
									if(ordenDAO.getDataSet().count()==0){//IF ORDEN NO EXISTE										
										logger.info("Orden: "+idOrdenProcesar+ " -  Orden no aparece en la busqueda. Puede que dicha orden no se ha enviado a BCV o ya ha sido procesada");										
										//System.out.println("Orden: "+idOrdenProcesar+ " -  Orden no aparece en la busqueda. Puede que dicha orden no se ha enviado a BCV o ya ha sido procesada");										
										continue;								
									} else {//IF ORDEN EXISTE
										ordenDAO.getDataSet().first();
																			
										while(ordenDAO.getDataSet().next()){
	
										//VALIDACION CLIENTE *******INFI_TB_201_CTES 
										String ced_rif=cedula.toString().substring(1);
										String tipper_id=cedula.toString().substring(0,1);								
										logger.info("ced_rif: "+ ced_rif+", tipper_id: "+ tipper_id);																					
										
										clienteDAO.listarCliente(0,tipper_id, Long.parseLong(ced_rif));								
										if(!clienteDAO.getDataSet().next()){
											logger.info("Orden: " + idOrdenProcesar +" - La cedula "+ cedula.toString()+ " No se encuentra registrado en el sistema. Se debe verificar el numero de cedula");
											//System.out.println("Orden: " + idOrdenProcesar +" - La cedula "+ cedula.toString()+ " No se encuentra registrado en el sistema. Se debe verificar el numero de cedula");
											continue;
										}																
										idCliente=clienteDAO.getDataSet().getValue("CLIENT_ID");																										
																			
										
										//VALIDACION MONTO_SOLICITADO *******INFI_TB_204_ORDENES
										if(Double.parseDouble(ordenDAO.getDataSet().getValue("ORDENE_PED_MONTO"))!=Double.parseDouble(mtoSolicitado.toString())){
											logger.info("Orden: " + idOrdenProcesar +" - El campo Mto Solicitado $ de la plantilla excel contiene un monto diferente al ingresado en la toma de orden");
											//System.out.println("Orden: " + idOrdenProcesar +" - El campo Mto Solicitado $ de la plantilla excel contiene un monto diferente al ingresado en la toma de orden");
											continue;
										}
										
										//VALIDACION SI LA ORDEN PERTENECE AL CLIENTE QUE SE INGRESA EN LA PLANTILLA DE ADJUDICACION
										if(!ordenDAO.getDataSet().getValue("client_id").equals(idCliente)){
											logger.info("Orden: " + idOrdenProcesar +" - La cedula "+ cedula.toString()+ " del cliente no corresponde a la orden asignada");
											//System.out.println("Orden: " + idOrdenProcesar +" - La cedula "+ cedula.toString()+ " del cliente no corresponde a la orden asignada");
											continue;
										}
										
										//VALIDACION ORDEN PERTENECE A UNIDAD INVERSION
										if(!ordenDAO.getDataSet().getValue("uniinv_id").equals(unidadInversionParametro)){
											logger.info("Orden: " + idOrdenProcesar +" - La orden no corresponde con la unidad de inversion asignada");
											//System.out.println("Orden: " + idOrdenProcesar +" - La orden no corresponde con la unidad de inversion asignada");
											continue;
										}
										
										orden.setFechaValorString(fechaValorString);
														
										//Configuracion de los codigos de operacion
										getTransaccionesFijasClaveNet(unidadInvDAO.getDataSet().getValue("UNDINV_ID"),orden.getVehiculoTomador());
										
										//********************** ORDENES NO ADJUDICADAS ********************** 
										if(montoAdjudicado.equals(new BigDecimal(0))){//MONTO ADJUDICADO CERO 								
											//System.out.println("************ MONTO ADJUDICADO CERO ************");
											orden.setMontoAdjudicado(0);
											orden.setStatus(StatusOrden.NO_ADJUDICADA_INFI);
											orden.setFechaAdjudicacion(new Date());
											
											//PROCESO DE GENERACION DE OPERACIONES
											generarOperaciones(orden);
											
											//orden.setMontoComisionOrden(new BigDecimal(0));
											orden.setMontoTotal(-1);
											//Generacion de operaciones 
											//generarOperaciones(orden);
										} else {//MONTO ADJUDICADO MAYOR A CERO
											OrdenTitulo ordenTitulo=new OrdenTitulo(); //com.bdv.infi_toma_orden.data.
											//System.out.println("************ MONTO ADJUDICADO MAYOR CERO ************");
											orden.setTasaCambio(tasaCambio.doubleValue());										
											orden.setMontoAdjudicado(montoAdjudicado.doubleValue());
											orden.setStatus(StatusOrden.ADJUDICADA);
											//orden.setIdMoneda(orden.getOperacion().get(0).getIdMoneda());											
											
											orden.setFechaAdjudicacion(new Date());											
											actualizacionTitulosAsociados(orden);
																					
											//PROCESO DE GENERACION DE OPERACIONES
											generarOperaciones(orden);
																						
											ArrayList<OrdenTitulo> arregloOrdenTitulo=new ArrayList<OrdenTitulo>();
											arregloOrdenTitulo.add(ordenTitulo);
											
											orden.setIdEjecucion(Long.parseLong(ejecucion_id));
										}
																												
										
										Orden copiaOrden=(Orden)orden.clone();
										
										ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(_dso);
										//Busca los documentos asociados a la adjudicación	
										
										copiaOrden.setIdTransaccion(TransaccionNegocio.ADJUDICACION);
										idTransaccion = copiaOrden.getIdTransaccion();										
										copiaOrden.setMonto(montoSolicitado.doubleValue());
										copiaOrden.setMontoAdjudicado(montoAdjudicado.doubleValue());
										copiaOrden.eliminarDocumento();// eliminamos los documentos que trae previamente ya que no nos interesa guardarlos por no ser de adjudicacion (son los de toma de orden)
										com.bdv.infi.data.Cliente objcliente = new com.bdv.infi.data.Cliente();
										objcliente.setIdCliente(Long.parseLong(idCliente));
										objcliente.setTipoPersona(tipper_id);
										
										procesarDocumentos.procesar(copiaOrden,this._app,_req.getRemoteAddr(),objcliente);																					
										
										//Creacion de documentos del proceso de Adjudicacion										
										querysTransacciones.add(ordenDAO.insertarDocumentosTransaccion(copiaOrden));	
										
										orden.getOperacion().clear();
										for (String query : ordenDAO.modificar(orden)) {										
											querysTransacciones.add(query);	
										}		
																			
										}//FIN WHILE ORDEN 									
																																							
									}//FIN DE SI LA ORDEN EXISTE
																
								}else if (tipoProcesoAdjRech.equals(ConstantesGenerales.IDENTIFICADOR_PROCESO_RECHAZO_SUBASTA_DIVISAS)){//**INICIO DE PROCESO DE RECHAZO INTERNO DE ORDENES POR BDV								
									//Busqueda de la orden por el ID ingresado en la plantilla de Adjudicacion			
									//TODO MODIFICAR ESTATIS A ENVIADA ----- FLAG DANIEL 
									ordenDAO.listarDatosBasicosOrdenPorStatus(idOrdenProcesar,StatusOrden.REGISTRADA/*StatusOrden.ENVIADA*/);			
									
									//VALIDACION ORDEN *******INFI_TB_204_ORDENES 																		
									if(ordenDAO.getDataSet().count()==0){//IF ORDEN NO EXISTE										
										logger.info("Orden: "+idOrdenProcesar+ " -  Orden no aparece en la busqueda. Puede que dicha orden no se ha enviado a BCV o ya ha sido procesada");										
										//System.out.println("Orden: "+idOrdenProcesar+ " -  Orden no aparece en la busqueda. Puede que dicha orden no se ha enviado a BCV o ya ha sido procesada");										
										continue;								
									} else {//IF la orden existe																		
										logger.info("Orden: "+idOrdenProcesar+ " - Proceso de rechazo interno (Por BDV) de la orden realizado satisfactoriamente ");
										
										OrdenDetalle ordenDetalle=new OrdenDetalle();
										ordenDetalle.setIdOrden(idOrdenProcesar);
										ordenDetalle.setObservaciones(ConstantesGenerales.OBSERVACION_RECHAZO_INTERNO_SUBASTA_DIVISAS);
										
										orden.setMontoAdjudicado(0);
										orden.setStatus(StatusOrden.NO_ADJUDICADA_INFI);
										
										for (String query : ordenDAO.modificar(orden)) {
											querysTransacciones.add(query);	
										}										
										querysTransacciones.add(ordenDAO.insertarOrdenDetalle(ordenDetalle));									
									}																								
								}		
			
							}//FIN RECORRIDO DE FILAS A PARTIR DE 2DA FILA
							
							
							
							try {
								
								Statement s =transaccion.getConnection().createStatement();
								//System.out.println("*******************************************");
								for(String element:querysTransacciones){
									//System.out.println("Sentencia ----> " + element);
									s.addBatch(element);
								}
								//System.out.println("*******************************************************************");
								++cantidadOrdenesAdj;
								++contadorOrdenesAdjudicadas;
								s.executeBatch();						
								s.close();
								
								if(ConstantesGenerales.COMMIT_REGISTROS_ADJ==cantidadOrdenesAdj){
									try {
										
										acumuladorOrdenesAdj=acumuladorOrdenesAdj+cantidadOrdenesAdj;
										logger.info("Ordenes enviadas por COMMIT en proceso de ADJUDICACION: " + acumuladorOrdenesAdj);
										transaccion.getConnection().commit();
										
									}catch(Exception e){
										logger.error("Eror en el proceso de COMMIT de los registros en Adjudicacion de Subasta Divisas: " + e.getMessage());
										//System.out.println("Eror en el proceso de COMMIT de los registros en Adjudicacion de Subasta Divisas: " + e.getMessage());
										throw new Exception("He ocurrido en el proceso de realizacion de COMMIT de los registros de Adjudicacion de Subasta Divisas: " + e.getMessage());
									}
									logger.info("Realizacion de commit al numero de registro N° " + cantidadOrdenesAdj);
									cantidadOrdenesAdj=0;								
								}
								}catch(SQLException sql){
									//System.out.println("Ha ocurrido un error de tipo SQLException " + sql.getMessage());
									querysTransacciones.clear();
									transaccion.getConnection().rollback();
									logger.error("Ha ocurrido un error de tipo SQLException " + sql.getMessage());									
									continue;
								}catch(Exception e){
									querysTransacciones.clear();
									transaccion.getConnection().rollback();
									//System.out.println("Ha ocurrido un error Inesperado " + e.getMessage());
									logger.error("Ha ocurrido un error Inesperado " + e.getMessage());
									continue;
								}								
								querysTransacciones.clear();
				}//FIN RECORRIDO DE FILAS	
				
			}
			transaccion.getConnection().commit();
			/*for (String element : querysTransacciones) {		
				//System.out.println("QUERY --------> " + element);						
			}	*/
			
	}	finally	{	
		
		/*Codigo comentado en resolucion de incidencias Calidad SICAD_2*/
		//PROCESO DE CIERRE AUTOMATICO DE LA UNIDAD DE INVERSION
		//cerrarUnidadInversion(Long.parseLong(unidadInversionParametro));
		
		if(contadorOrdenesAdjudicadas>0){
			try{		
				//PROCESO DE ENVIO DE CORREO 		
				/** Colocar esto DESDE AQUI para realizar la llamada a EnvioCorreos()**/
		    	InetAddress direccion = InetAddress.getLocalHost();
		    	String direccionIpstr = direccion.getHostAddress();
		    	//System.out.println("Direccion IP: "+direccionIpstr);
		    	//System.out.println("_app en Job EnvioCorreos: "+this._app);
		    	EnvioCorreosCall ecCall = new EnvioCorreosCall();
		    	EnvioCorreos ec = new EnvioCorreos(_dso, null, direccionIpstr); //Como es un Job no se tiene el ServletContext (se pasa null)
		    	//EnvioCorreos ec = new EnvioCorreos(dso, this._app, direccionIpstr);
		    	ec.initParamEnvio();
		    	ecCall.execute(ec.parametros.get(ParametrosSistema.DEST_CLIENTE), "", ec, unidadInversionParametro, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA, StatusOrden.ADJUDICADA, Integer.parseInt(ejecucion_id));
	            /** Colocar esto HASTA AQUI para realizar la llamada a EnvioCorreos()**/
			} catch(Exception ex){
				logger.error(" El lote de ordenes ha sido Adjudicado correctamente pero ha ocurrido un error en el proceso de notificación por Correo de la Adjudicacion de Subasta Divisas - Detalle del error: " + ex.getMessage());
				//throw new Exception(" El lote de ordenes ha sido Adjudicado correctamente pero ha ocurrido un error en el proceso de notificación por Correo de la Adjudicacion de Subasta Divisas - Detalle del error: " + ex.getMessage());
			}
		}
			
		
		terminarProceso();
		
		if(transaccion.getConnection()!=null){			
				
			transaccion.closeConnection();
		}
				
		if(fijaDAO!=null){
			fijaDAO.cerrarConexion();
		}
					
		if(clienteDAOClavenet!=null){
			clienteDAOClavenet.cerrarConexion();
		}
		
		if(clienteDAO!=null){
			clienteDAO.cerrarConexion();
		}
		
		if(unidadInvDAO!=null){
			unidadInvDAO.cerrarConexion();
		}
		
		if(operacionDAO!=null){
			operacionDAO.cerrarConexion();
		}
		
		if(ordenDAO!=null){
			ordenDAO.cerrarConexion();
		}
	
		if(ordenesTitulos!=null){
			ordenesTitulos.cerrarConexion();				
		}			
						
	}
	} //FIN EXECUTE
		
	
	private void cerrarUnidadInversion(long idUnidadInv)throws Exception {
		StringBuffer idOrdenesSinProcesar=new StringBuffer();
		
		ordenDAO.ordenesPorAdjudicarByIdUnidad(idUnidadInv);
		if(ordenDAO.getDataSet().count()>0){
			ordenDAO.getDataSet().first();
			while(ordenDAO.getDataSet().next()){
				idOrdenesSinProcesar.append(ordenDAO.getDataSet().getValue("ORDENE_ID")+ " - ");				
			}
			logger.warn("No se puede cerrar la unidad de inversion debido a que la(s) siguiente(s) orden(es) no se han procesado:   " + idOrdenesSinProcesar.toString());
			//System.out.println("No se puede cerrar la unidad de inversion debido a que la(s) siguiente(s) orden(es) no se han procesado:   " + idOrdenesSinProcesar.toString());
		}else {
			unidadInvDAO.modificarStatus(idUnidadInv,UnidadInversionConstantes.UISTATUS_CERRADA);
		}
		
	}


	private void actualizacionTitulosAsociados(Orden orden)throws Exception {
				
		OrdenTitulo ordenTitulo=null;
		
		ordenDAO.listarTitulosOrden(orden.getIdOrden());
		
		if(ordenDAO.getDataSet().count()>0){
			ordenDAO.getDataSet().first();
			while(ordenDAO.getDataSet().next()){			
				ordenTitulo=new OrdenTitulo();
				
				ordenTitulo.setIdOrden(orden.getIdOrden());				
				ordenTitulo.setTituloId(ordenDAO.getDataSet().getValue("TITULO_ID"));
				ordenTitulo.setPorcentaje(ordenDAO.getDataSet().getValue("TITULO_PCT"));
				
				BigDecimal montoTituloRecalculado=new BigDecimal(orden.getMontoAdjudicado());
				montoTituloRecalculado.multiply(new BigDecimal(ordenDAO.getDataSet().getValue("TITULO_PCT"))).divide(new BigDecimal(100));
				ordenTitulo.setMonto(montoTituloRecalculado.doubleValue());
				//ordenTitulo. setPorcentaje(ordenDAO.getDataSet().getValue("TITULO_PCT"));
				querysTransacciones.add(ordenDAO.actualizarMontoOrdenTitulo(ordenTitulo));
			}
			
			
		}
		
	
	}

	private void generarOperaciones(Orden orden)throws Exception{
						
		
		String adjudicacion=""; 
			
		if(orden.getMontoAdjudicado()==0){
			adjudicacion=ADJUDICADO_CERO;
		}else if(orden.getMontoAdjudicado()==orden.getMonto()){
			adjudicacion=ADJUDICADO_IGUAL_SOLICITADO;
		}else if(orden.getMontoAdjudicado()<orden.getMonto()){
			adjudicacion=ADJUDICADO_MENOR_SOLICITADO;
		}
															
		ArrayList<OrdenOperacion> operaciones=new ArrayList<OrdenOperacion>();
		ordenOpics=new SolicitudClavenet();
		operacionDAO=new OperacionDAO(_dso);	
		//OrdenDAO ordenCLaveNet=new OrdenDAO(_dso);
		String tipoTransaccionFinanciera=null;
	try {
		//BUSQUEDA DE COMISION CONFIGURADA EN LA ORDEN
		
		boolean poseeComision=false;//Indicador 
		boolean poseeComisionInveriable=false;//Comision monto fijo que se cobra aun si el monto Adjudicado es cero
		
		BigDecimal tasaComisionVariable=new BigDecimal(0);
		
		String tipoMonedaOperacionComision=null;//Variable de almacenamiento de tipo de moneda de operacion de comision
		String tipoMonedaOperacionCapital=null;//Variable de almacenamiento de tipo de moneda de operacion de capital
		
		BigDecimal montoComision=new BigDecimal(0);
		for (OrdenOperacion operacion : orden.getOperacion()) {				
			tipoTransaccionFinanciera=operacion.getTipoTransaccionFinanc();			
			
			if(operacion.getInComision()==1){//La operacion es de tipo comision			
				poseeComision=true;		
				tipoMonedaOperacionComision=operacion.getIdMoneda();
				if(esComisionMontoFijo(operacion.getMontoOperacion().toString(),operacion.getTasa().toString())){//Operacion de comision por monto fijo
					poseeComisionInveriable=true;
					montoComision=new BigDecimal(operacion.getMontoOperacion().toBigInteger());
				} else {//Monto comision basado en % de monto adjudicado
					tasaComisionVariable=operacion.getTasa();//Configuracion de la tasa de la comision en caso de NO ser comision invariable   
					//if(orden.getMontoAdjudicado()!=0){					
						montoComision=(new BigDecimal(orden.getMontoAdjudicado()).multiply(operacion.getTasa().divide(new BigDecimal(100))));											
						montoComision=montoComision.multiply(new BigDecimal(orden.getTasaCambio()));
						montoComision=montoComision.setScale(2,BigDecimal.ROUND_HALF_EVEN);
					//}
				}
				break;
			} else {
				tipoMonedaOperacionCapital=operacion.getIdMoneda();
				//poseeComision=false;	
			}
		}
				
		if(tipoTransaccionFinanciera.equals(TransaccionFinanciera.MISCELANEO) || tipoTransaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)){
							
			
			if(tipoTransaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)){
											
				//Generacion de operaciones de desbloqueo Capital y Comision
				for (OrdenOperacion operacion : orden.getOperacion()) {					
					OrdenOperacion operacionDesbloqueo = new OrdenOperacion();
					
					if(operacion.getInComision()==0){
						BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN,false,TransaccionFinanciera.BLOQUEO);
						operacionDesbloqueo.setInComision(ConstantesGenerales.FALSO);
						operacionDesbloqueo.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN));
						
						BigDecimal montoDesbloqueoCapital=new BigDecimal(orden.getMontoTotal()).subtract(orden.getMontoComisionOrden());
						montoDesbloqueoCapital=montoDesbloqueoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN);
						operacionDesbloqueo.setMontoOperacion(montoDesbloqueoCapital);
						operacionDesbloqueo.setTasa(new BigDecimal(100));
					}else if(operacion.getInComision()==1){
						BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_COMISION,false,TransaccionFinanciera.BLOQUEO);
						operacionDesbloqueo.setInComision(ConstantesGenerales.VERDADERO);
						operacionDesbloqueo.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_COMISION));
						operacionDesbloqueo.setTasa(operacion.getTasa());
						operacionDesbloqueo.setMontoOperacion(orden.getMontoComisionOrden());
					}
	
					operacionDesbloqueo.setCodigoOperacion(codigo_operacion);
					operacionDesbloqueo.setNombreOperacion(nombre_operacion);								
					operacionDesbloqueo.setIdOrden(orden.getIdOrden());
					operacionDesbloqueo.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
					
					
					/*montoTotalDesb=monto_nominal.add(monto_comision);
					operacionDesbloqueo.setMontoOperacion(montoTotalDesb.setScale(2,BigDecimal.ROUND_HALF_EVEN));*/
					operacionDesbloqueo.setAplicaReverso(false);
					
					if(poseeComisionInveriable){//Validacion de comision fija Invariable					
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
					
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDesbloqueo,false));
				}				
			}//FIN SI OPERACION ES BLOQUEO
									
			//Verifica si monto adjudicado NO es CERO o si posee comision invariable para generar la operacion de debito por comision			
			if((poseeComision && (!adjudicacion.equals(ADJUDICADO_CERO))) || poseeComisionInveriable){//CREACION DE OPERACIONES DE DEBITO POR COMISION
				
				//Operaciones 3) debito por el monto comision --> Monto registrado en la tabla 204 campo	ORDENE_PED_COMISIONES									
				OrdenOperacion operacionDebitoComision = new OrdenOperacion();
					
				//Busqueda de codigos de operacion DEBITO COMISION
				BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_COMISION,false,TransaccionFinanciera.DEBITO);
					
				operacionDebitoComision.setCodigoOperacion(codigo_operacion);				
				operacionDebitoComision.setNombreOperacion(nombre_operacion);
										
				operacionDebitoComision.setIdOrden(orden.getIdOrden());					
				operacionDebitoComision.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);					
				operacionDebitoComision.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_COMISION));
													
				operacionDebitoComision.setMontoOperacion(montoComision.setScale(2,BigDecimal.ROUND_HALF_EVEN)); //configurar monto
															
				operacionDebitoComision.setAplicaReverso(false);	
				
				if(poseeComisionInveriable){
					operacionDebitoComision.setIndicadorComisionInvariable(ConstantesGenerales.VERDADERO);
				}else {
					operacionDebitoComision.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);	
				}
								
				operacionDebitoComision.setCuentaOrigen(orden.getCuentaCliente());				
				operacionDebitoComision.setIdMoneda(tipoMonedaOperacionComision);				
				operacionDebitoComision.setInComision(ConstantesGenerales.VERDADERO);				
				operacionDebitoComision.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);			
				
				if (!poseeComisionInveriable){					
					operacionDebitoComision.setTasa(tasaComisionVariable);
				}else {
					operacionDebitoComision.setTasa(new BigDecimal(0));	
				}
				
				operacionDebitoComision.setFechaAplicar(new Date());
				
				//operacionDebitoComision.setIdOperacionRelacion(operacionDesbloqueo.getIdOperacion());									
				operaciones.add(operacionDebitoComision);
				
				//Modificacion de COMISION para el registro de la tabla INFI_TB_204_ORDENES campo ORDENE_PED_COMISIONES
				orden.setMontoComisionOrden(montoComision);
				poseeComisionInveriable=false;			
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComision,false));							
			} else {//FIN VALIDACION PARA OPERACION DEBITO COMISION				
				orden.setMontoComisionOrden(montoComision);
				orden.setComisionCero(true);							
			}
					
			
			if(!adjudicacion.equals(ADJUDICADO_CERO)){

				//OPERACION DE DEBITO CAPITAL							
				OrdenOperacion operacionDebitoCapital = new OrdenOperacion();		

				//Busqueda de codigos de operacion DEBITO CAPITAL
				BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN,false,TransaccionFinanciera.DEBITO);				
				operacionDebitoCapital.setCodigoOperacion(codigo_operacion);			
				operacionDebitoCapital.setNombreOperacion(nombre_operacion);
									
				operacionDebitoCapital.setIdOrden(orden.getIdOrden());				
				operacionDebitoCapital.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);			
				operacionDebitoCapital.setIdTransaccionFinanciera(new String(ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN+""));
																				
				totalMontoCapitalOperaciones=new BigDecimal(orden.getMontoAdjudicado()).multiply(new BigDecimal(orden.getTasaCambio()));//TODO Validar si el campo ORD.ORDENE_PED_TOTAL es CAPITAL + COMISION          				
				totalMontoCapitalOperaciones=totalMontoCapitalOperaciones.setScale(2,BigDecimal.ROUND_HALF_EVEN);											
				operacionDebitoCapital.setMontoOperacion(totalMontoCapitalOperaciones);//configuracion de monto operacion
				
				//Configuracion del monto total (Capital + Comision) en la tabla INFI_TB_204_ORDENES campo ORDENE_PED_TOTAL
				orden.setMontoTotal(totalMontoCapitalOperaciones.add(montoComision).doubleValue());
				operacionDebitoCapital.setAplicaReverso(false);				
				operacionDebitoCapital.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);				
				operacionDebitoCapital.setCuentaOrigen(orden.getCuentaCliente());				
				operacionDebitoCapital.setIdMoneda(tipoMonedaOperacionCapital);					
				operacionDebitoCapital.setInComision(ConstantesGenerales.FALSO);				
				operacionDebitoCapital.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);				
				operacionDebitoCapital.setTasa(BigDecimal.valueOf(orden.getPrecioCompra()));			
				operacionDebitoCapital.setFechaAplicar(new Date());				
							
				operaciones.add(operacionDebitoCapital);							
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoCapital,false));		
			}
		}else if (tipoTransaccionFinanciera.equals(TransaccionFinanciera.DEBITO)){//ESCENARIO DEBITO
			
			BigDecimal montoSolicitadoBs=new BigDecimal(0);
			BigDecimal montoAdjudicadoBs=new BigDecimal(0);
			montoSolicitadoBs=new BigDecimal(orden.getMontoTotal()).subtract(orden.getMontoComisionOrden());				
			montoAdjudicadoBs=new BigDecimal(orden.getMontoAdjudicado()).multiply(new BigDecimal(orden.getTasaCambio()));

			//Operaciones para Capital
			OrdenOperacion operacionCapital = new OrdenOperacion();
			operacionCapital.setIdOrden(orden.getIdOrden());	
			operacionCapital.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			operacionCapital.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);
			operacionCapital.setCuentaOrigen(orden.getCuentaCliente());
			
			OrdenOperacion operacionComision=null;		
			if(!poseeComisionInveriable){
				//creacion de operacion para comision.
				operacionComision = new OrdenOperacion();
				operacionComision.setIdOrden(orden.getIdOrden());	
				operacionComision.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
				operacionComision.setIdMoneda(ConstantesGenerales.SIGLAS_MONEDA_LOCAL);
				operacionComision.setCuentaOrigen(orden.getCuentaCliente());
				operacionComision.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_COMISION));
				operacionComision.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);
				operacionComision.setTasa(tasaComisionVariable);
			}
			
			//MONTO ADJUDICADO MENOR AL SOLICITADO
			if(montoAdjudicadoBs.compareTo(montoSolicitadoBs)==-1){
				//System.out.println("MONTO ADJUDICADO MENOR AL SOLICITADO");
				//Creacion de operaciones CREDITO				
				BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN,false,TransaccionFinanciera.CREDITO);
				operacionCapital.setInComision(ConstantesGenerales.FALSO);
				operacionCapital.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN));						
			
				//******Calculo de monto de CREDITO por concepto CAPITAL				
				//Calculo monto cobrado por capital
				BigDecimal montoCapitalCobrado=new BigDecimal(orden.getMontoTotal()).subtract(orden.getMontoComisionOrden());						
				montoCapitalCobrado=montoCapitalCobrado.setScale(2,BigDecimal.ROUND_HALF_EVEN);
				
				//Calculo monto adjudicado por capital						
				BigDecimal montoCapitalAdjudicado=new BigDecimal(orden.getMontoAdjudicado()).multiply(new BigDecimal(orden.getTasaCambio()));
				//Calculo monto de operacion de credito 												
				BigDecimal montoCreditoCapital=new BigDecimal(montoCapitalCobrado.doubleValue()).subtract(montoCapitalAdjudicado);				
				operacionCapital.setMontoOperacion(montoCreditoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN));
				
				operacionCapital.setTasa(new BigDecimal(100));
				operacionCapital.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);			
				operacionCapital.setCodigoOperacion(codigo_operacion);
				operacionCapital.setNombreOperacion(nombre_operacion);							
				operacionCapital.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
										
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCapital,false));
				
				if(!poseeComisionInveriable && operacionComision!=null){	
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_COMISION,false,TransaccionFinanciera.CREDITO);
					BigDecimal comisionCobrada=orden.getMontoComisionOrden();						
					BigDecimal comisionAdjudicada=montoAdjudicadoBs.multiply(tasaComisionVariable).divide(new BigDecimal(100));					
					BigDecimal montoComisionAcreditar=comisionCobrada.subtract(comisionAdjudicada);					
					operacionComision.setMontoOperacion(montoComisionAcreditar.setScale(2,BigDecimal.ROUND_HALF_EVEN));
																	
					operacionComision.setCodigoOperacion(codigo_operacion);
					operacionComision.setNombreOperacion(nombre_operacion);							
					operacionComision.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionComision,false));
				}
			//MONTO ADJUDICADO MAYOR AL SOLICITADO
			} else if(montoAdjudicadoBs.compareTo(montoSolicitadoBs)==1){
				//System.out.println("MONTO ADJUDICADO MAYOR AL SOLICITADO");
				//Creacion de operaciones DEBITO
				
				BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN,false,TransaccionFinanciera.DEBITO);
				operacionCapital.setInComision(ConstantesGenerales.FALSO);
				operacionCapital.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN));						
			
				//******Calculo de monto de DEBITO por concepto CAPITAL				
				//Calculo monto cobrado por capital				
				BigDecimal montoCapitalCobrado=new BigDecimal(orden.getMontoTotal()).subtract(orden.getMontoComisionOrden());						
				montoCapitalCobrado=montoCapitalCobrado.setScale(2,BigDecimal.ROUND_HALF_EVEN);				
				
				//Calculo monto adjudicado por capital						
				BigDecimal montoCapitalAdjudicado=new BigDecimal(orden.getMontoAdjudicado()).multiply(new BigDecimal(orden.getTasaCambio()));								
				//Calculo monto de operacion de credito 												
				BigDecimal montoDebitoCapital=new BigDecimal(montoCapitalAdjudicado.doubleValue()).subtract(montoCapitalCobrado);								
				operacionCapital.setMontoOperacion(montoDebitoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN));

				operacionCapital.setTasa(new BigDecimal(100));
				operacionCapital.setIndicadorComisionInvariable(ConstantesGenerales.FALSO);			
				operacionCapital.setCodigoOperacion(codigo_operacion);
				operacionCapital.setNombreOperacion(nombre_operacion);							
				operacionCapital.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
										
				querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionCapital,false));
				if(!poseeComisionInveriable && operacionComision!=null){	
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_COMISION,false,TransaccionFinanciera.DEBITO);					
					BigDecimal comisionCobrada=orden.getMontoComisionOrden();						
					BigDecimal comisionAdjudicada=montoAdjudicadoBs.multiply(tasaComisionVariable).divide(new BigDecimal(100));
					BigDecimal montoComisionDeditar=comisionAdjudicada.subtract(comisionCobrada);					
					operacionComision.setMontoOperacion(montoComisionDeditar.setScale(2,BigDecimal.ROUND_HALF_EVEN));
																	
					operacionComision.setCodigoOperacion(codigo_operacion);
					operacionComision.setNombreOperacion(nombre_operacion);							
					operacionComision.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionComision,false));
				}								
			}
			
			for (OrdenOperacion operacion : orden.getOperacion()) {				
				if(operacion.getInComision()==1 && !esComisionMontoFijo(operacion.getMontoOperacion().toString(),operacion.getTasa().toString())){					
					BigDecimal comisionMontoAdjudicado=(new BigDecimal(orden.getMontoAdjudicado()).multiply(operacion.getTasa().divide(new BigDecimal(100))));											
					comisionMontoAdjudicado=comisionMontoAdjudicado.multiply(new BigDecimal(orden.getTasaCambio()));
					comisionMontoAdjudicado=comisionMontoAdjudicado.setScale(2,BigDecimal.ROUND_HALF_EVEN);
					orden.setMontoComisionOrden(comisionMontoAdjudicado);							
				} else if(operacion.getInComision()==1 && esComisionMontoFijo(operacion.getMontoOperacion().toString(),operacion.getTasa().toString())){
					orden.setMontoComisionOrden(operacion.getMontoOperacion());
				}				
		}											
		orden.setMontoTotal(orden.getMontoComisionOrden().add(new BigDecimal(orden.getMontoAdjudicado()).multiply(new BigDecimal(orden.getTasaCambio())).setScale(2,BigDecimal.ROUND_HALF_EVEN)).doubleValue());					
			
				
		}
		
			//GENERACION DATA ESTADISTICA  
			/*
				try{
					orden.setOperacion(operaciones);
					orden.setStatus(StatusOrden.ENVIADA);
					
					generarMensajeEstadistica(querysTransacciones, orden, clienteDAOClavenet.getDataSet(), new BigDecimal(orden.getMontoAdjudicado()), broker.toString(),consecutivo);
					orden.setStatus(StatusOrden.PROCESO_ADJUDICACION);
				}catch (Exception e){
					logger.error("Error en el proceso de generacion de Mensaje de Estadistica");
					 //System.out.println("Error en el proceso de generacion de Mensaje de Estadistica");
					 throw new Exception("Error en el proceso de generacion de Mensaje de Estadistica");
				}*/
				
				
				//GENERACION DEALS OPERACION CAMBIO OPICS 						
				/*try {
					
					UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);
					int idUserSession = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));	
									
					IngresoOpics ingresoOpics = new IngresoOpics(_dso, _app,idUserSession, _req.getRemoteAddr(), getUserName());
					
					StringBuffer sql=new StringBuffer();
					ArrayList<String> querys=new ArrayList<String>();
					querys=ingresoOpics.operacionCambio(orden, getUserName(), orden.getCuentaCliente(),orden.getIdMoneda(),ConstantesGenerales.SIGLAS_MONEDA_LOCAL,new BigDecimal(orden.getMontoAdjudicado()),new BigDecimal(orden.getTasaCambio()));
																															
					for(int z=0;z<querys.size();z++){		
						//System.out.println("query consulta " + querys.get(z).toString());
						querysTransacciones.add(querys.get(z).toString());						
					}										
				}catch (Exception e){					
					//System.out.println("Se ha generado un error en el proceso de generacion de DEALS OPICS " + e.getMessage());
					logger.error("Se ha generado un error en el proceso de generacion de DEALS OPICS " + e.getMessage());
					throw new Exception("Se ha generado un error en el proceso de generacion de DEALS OPICS " + e.getMessage());
				}*/
			//}

		}catch(Exception e){		
			//System.out.println("Ocurrido un error en el proceso de Generar Operaciones");
			throw new Exception(" Ocurrido un error en el proceso de Generar Operaciones. " + e.getMessage());					
		}finally{
			if(operacionDAO!=null){
				operacionDAO.cerrarConexion();
			}
			
			if(ordenCLaveNet!=null){
				ordenCLaveNet.cerrarConexion();
			}
		}
	}
	


	/**Metodo que retorna el codigo y nombre de la transaccion asociada a la operacion 
	 * 
	 * @param vehiculo_id vehiculo tomador
	 * @param transacionFija id de la transaccion fija
	 * @param vehiculo boolean para indicar si la opreacion es a la cuenta del cliente(false) o al vehiculo(true)
	 * @param transaccionFinanciera tipo de operacion (DEB, BLO, CRE)
	 * @throws Exception
	 */
	private void BuscarCodigoyNombreOperacion(String vehiculo_id, int transacionFija, boolean vehiculo, String transaccionFinanciera) throws Exception{
		
		codigo_operacion= "";
		nombre_operacion= "";
		
		logger.info("* Buscando Codigo y nombre de operacion para el Vehiculo: "+vehiculo_id+", Transaccion Fija: "+transacionFija+", transaccion Financiera: "+transaccionFinanciera);
		
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
				}
			}
			
		}
		//logger.info("Nombre Operacion: "+nombre_operacion+" codigo operacion: "+codigo_operacion);
	}
	
	/**Metodo que crea una operacion de credito hacia la cuenta del vehiculo tomador, asociada a la orden de tipo ORDEN_VEHICULO
	 * en caso de que no exista dicha orden se crea y la orden de tipo ORDEN_VEHICULO con su operacion de CREDITO
	 * @param montoOperacion
	 * @param vehiculo
	 * @param transaccionFija
	 * @param transaccionFinanciera
	 * @param status
	 * @param moneda
	 * @return operacion el objeto operacion con toda la informacion de la operacion hacia la cuenta del vehiculo
	 * @throws Exception
	 */
	
	
	
	
	
	
	
	public boolean isValid() throws Exception {
		boolean valido = true;
		procesosDAO = new ProcesosDAO(_dso);
		procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.ADJUDICACION_SUBASTA_DIVISAS);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Adjudicación de Subasta Divisas",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}
		return valido;
	}// fin isValid
	
	/**
	 * Inicia el proceso de adjudicación
	 */
	private void iniciarProceso() throws Exception{
		UsuarioDAO usu	= new UsuarioDAO(_dso);		
		
		proceso = new Proceso();
		proceso.setTransaId(TransaccionNegocio.ADJUDICACION_SUBASTA_DIVISAS);
		proceso.setUsuarioId(Integer.parseInt(usu.idUserSession(getUserName())));
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());		
		/* Primero creamos el proceso */
		db.exec(_dso, procesosDAO.insertar(proceso));
		logger.info("Comenzó proceso: Adjudicación");
		
		//Prepara el dataset de mensajes
		mensajesPorRif.append("orden", java.sql.Types.VARCHAR);
		mensajesPorRif.append("mensaje", java.sql.Types.VARCHAR);
	}
	
	/**
	 * Finaliza el proceso de adjudicación
	 */
	private void terminarProceso() {
		try {
			if(proceso != null) {
				db.exec(this._dso, procesosDAO.modificar(proceso));
			}
			
			if(procesosDAO != null) {
				procesosDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);				
		}
		finally {
			if(proceso != null) {
				logger.info("Finalizó proceso: Adjudicación");
			}
		}
	}
	
	/**
	 * Obtiene las transacciones fijas según la unidad y el vehículo tomador
	 * @param unidadInversion id de la unidad de inversión
	 * @param vehiculoTomador vehículo tomador
	 * @throws Exception en caso de error
	 */
	protected void getTransaccionesFijas(String unidadInversion) throws Exception{

		if (transaccionesFijasCache.containsKey(unidadInversion)){
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);

		}else{

			fijaDAO.listarTransaccionesFijasAdjudicacion(unidadInversion);
			DataSet ds = fijaDAO.getDataSet();

			transaccionesFijasCache.put(unidadInversion,ds);
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);

		}
	}	
	
	/**
	 * Genera los registros que deben enviarse a opics sólo si es unidad de inversión SITME
	 * @throws Exception en caso de error
	 */
	protected void generarOpics(Orden orden, ArrayList<String> querys) throws Exception{
		if (orden.getFechaPactoRecompra() != null){
			UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);
			int idUserSession = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));		
			IngresoOpicsSitme ingresoOpics = new IngresoOpicsSitme(_dso, _app,idUserSession, _req.getRemoteAddr(), getUserName());
			querys.addAll(ingresoOpics.rentaFija(orden));
		}
	}
	
	
	
	/**
	 * Genera el mensaje para Estadistica
	 * @param querys 
	 * @param orden 
	 * @param montoAdjudicado 
	 * @param numeroCedula 
	 * @param tipoPersona 
	 * @return mensajeEstadistica: objeto con el mensaje para estadistica
	 * @throws Exception 
	 */
	private void generarMensajeEstadistica(ArrayList<String> querys, Orden ordenOriginal, DataSet _cliente, BigDecimal montoAdjudicado, String broker,long consecutivo) throws Exception{
		
		//Generar Mensaje estadístico si es título SITME
		if (ordenOriginal.getFechaPactoRecompra() != null && ordenOriginal.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) && (ordenOriginal.getStatus().equals(StatusOrden.ENVIADA) || ordenOriginal.getStatus().equals(StatusOrden.REGISTRADA))){			
			
			logger.debug("Creando Mensaje Estadistica Orden: "+ordenOriginal.getIdOrden()+"...");
			
			MensajeEstadistica mensajeEstadistica = new MensajeEstadistica();
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);
			OrdenOperacion ordenOperacion = new OrdenOperacion();
			String cedRifCliente ="";
			String tipoPersona = "";
			String nombreCliente = "";
			
			//Datos del Cliente
			if(_cliente.count()>0){
				_cliente.first();_cliente.next();
				cedRifCliente = _cliente.getValue("client_cedrif");
				tipoPersona = _cliente.getValue("tipper_id");
				nombreCliente = _cliente.getValue("client_nombre");				
			}
		
			//Datos de las Operaciones:
			ArrayList<OrdenOperacion> listaOperaciones = ordenOriginal.getOperacion();
			if(!listaOperaciones.isEmpty()){
				//Para obtener el Numero de Cuenta, tomar la 1era operacion ya que es el mismo para Todas
				ordenOperacion = listaOperaciones.get(0);
			}
			
			 //Setear Valores de la Venta al Mensaje:
			  mensajeEstadistica.set(mensajeEstadistica.CEDULA_RIF_PASAPORTE , tipoPersona+cedRifCliente);
			  mensajeEstadistica.set(mensajeEstadistica.FECHA_PROCESO , new Date());
			  mensajeEstadistica.set(mensajeEstadistica.CONCEPTO_ESTADISTICO ,MensajeEstadistica.V_CONCEPTO_VENTA);
			  mensajeEstadistica.set(mensajeEstadistica.CODIGO_DIVISA , MensajeEstadistica.V_CODIGO_DIVISA_DOLAR);
			  mensajeEstadistica.set(mensajeEstadistica.NOMBRE_RAZON_SOCIAL , nombreCliente);
			  mensajeEstadistica.set(mensajeEstadistica.MONTO_MONEDA_EXTRANJERA , montoAdjudicado.doubleValue());
			  mensajeEstadistica.set(mensajeEstadistica.HORA , com.bdv.infi.util.Utilitario.DateToString(new Date(), "HHmm"));
			  mensajeEstadistica.set(mensajeEstadistica.FECHA_VALOR , ordenOriginal.getFechaValor());
			  mensajeEstadistica.set(mensajeEstadistica.USUARIO , getUserName());
			  mensajeEstadistica.set(mensajeEstadistica.REFERENCIA_BANKTRADE , "B" + Utilitario.rellenarCaracteres(broker, '0', 4, false) + Utilitario.rellenarCaracteres(String.valueOf(consecutivo),'0',5,false));//PENDIENTE
			  mensajeEstadistica.set(mensajeEstadistica.NOMBRE_DEL_BENEFICIARIO, nombreCliente);
			  mensajeEstadistica.set(mensajeEstadistica.NUM_CUENTA_CLIENTE , mensajeOpicsDAO.convertirCuenta20A12(ordenOperacion.getNumeroCuenta()));
			  mensajeEstadistica.set(mensajeEstadistica.FIRMAS_AUTORIZADAS , getUserName());
			  mensajeEstadistica.set(mensajeEstadistica.CEDULA_RIF_BENEFICIARIO , tipoPersona+cedRifCliente);
			  
			 //Establecer valores por Defecto al Mensaje:
			 mensajeDAO.estableceValoresPorDefecto(mensajeEstadistica);//
			 
			 if (mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA) != null && (mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA).equals("") ||  mensajeEstadistica.get(mensajeEstadistica.CODIGO_OFICINA).equals("0"))){
				  mensajeEstadistica.set(mensajeEstadistica.CODIGO_OFICINA, ordenOriginal.getSucursal());
			 }
			 mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_BOLIVAR , ordenOriginal.getPrecioCompra());

			 //TODO validar modificacion
			//mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_DOLAR , "1");
			 mensajeEstadistica.set(mensajeEstadistica.TASA_CAMBIO_DOLAR ,ordenOriginal.getTasaCambio());

			  if(tipoPersona.equalsIgnoreCase("G")){
				  mensajeEstadistica.set(mensajeEstadistica.SECTOR_PUBLICO_PRIVADO, MensajeEstadistica.V_SECTOR_PUBLICO); 
			  }else{
				  mensajeEstadistica.set(mensajeEstadistica.SECTOR_PUBLICO_PRIVADO, MensajeEstadistica.V_SECTOR_PRIVADO);  
			  }
			  
			  mensajeEstadistica.setUsuarioNM(getUserName());
			  mensajeEstadistica.setOrdeneId(Integer.parseInt(String.valueOf(ordenOriginal.getIdOrden())));
			  mensajeEstadistica.setFechaValor(ordenOriginal.getFechaPactoRecompra());
			  
			  //Generar sentecias sql para guardar el mensaje de Estadística
			  String[] sentenciasMje = mensajeDAO.ingresar(mensajeEstadistica);
			
			  //Agregar sentencias de Mensaje a vector Global de queries a ejecutar 
			  for(int k= 0; k<sentenciasMje.length; k++){
				  //System.out.println("SQL ESTADISTICA " + sentenciasMje[k]);
				  querys.add(sentenciasMje[k]);
			  }
		}			
	}
	
	/**
	 * Genera el mensaje para BCV
	 * @return mensajeBcv: objeto con el mensaje para bcv
	 * @throws Exception 
	 */
	private void generarMensajeBCV(ArrayList<String> querys, Orden ordenOriginal, DataSet _cliente, BigDecimal montoAdjudicado, String broker, long consecutivo) throws Exception{

		//Verificar si el tipo de producto es SITME y si la orden aun se encuentra en status ENVIADA
		if (ordenOriginal.getFechaPactoRecompra() != null && ordenOriginal.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) && (ordenOriginal.getStatus().equals(StatusOrden.ENVIADA) || ordenOriginal.getStatus().equals(StatusOrden.REGISTRADA))){		
	
			if (logger.isDebugEnabled()){
				logger.debug("Creando Mensaje BCV Orden: "+ordenOriginal.getIdOrden()+"...");	
			}			

			MensajeDAO mensajeDAO = new MensajeDAO(_dso);		
			MensajeBcv mensajeBcv = new MensajeBcv();
			OrdenTitulo titulo = new OrdenTitulo();
			String cedRifCliente ="";
			String tipoPersona = "";
			String nombreCliente = "";
			
			//Datos del Cliente
			if(_cliente.count()>0){
				_cliente.first();_cliente.next();
				cedRifCliente = _cliente.getValue("client_cedrif");
				tipoPersona = _cliente.getValue("tipper_id");
				nombreCliente = _cliente.getValue("client_nombre");				
			}

			//Datos del Titulo: (SITME ES 1 solo TITULO)
			ArrayList<OrdenTitulo> listaTitulos = ordenOriginal.getOrdenTitulo();
			if(!listaTitulos.isEmpty()){
				//Obtiene el Titulo
				titulo = listaTitulos.get(0);
			}

			//Setear Valores al Mensaje para Interfaz BCV
			mensajeBcv.set(mensajeBcv.SECUENCIA, consecutivo);
			mensajeBcv.set(mensajeBcv.NOMBRE_RAZON_SOCIAL, nombreCliente);
			mensajeBcv.set(mensajeBcv.RIF_LETRA, tipoPersona);
			mensajeBcv.set(mensajeBcv.RIF_NUMERO, cedRifCliente);
			mensajeBcv.set(mensajeBcv.TITULO, obtenerDatosTitulo(mensajeBcv,titulo.getTituloId()));
			mensajeBcv.set(mensajeBcv.OPERADOR_COMPRA, "001"); //TODO Pendiente
			mensajeBcv.set(mensajeBcv.OPERADOR_VENTA, "001"); //TODO Pendiente
			mensajeBcv.set(mensajeBcv.FECHA_OPERACION, new Date());
			mensajeBcv.set(mensajeBcv.FECHA_VALOR, ordenOriginal.getFechaValor());
			mensajeBcv.set(mensajeBcv.PRECIO_TITULO, titulo.getPorcentajeRecompra());
			mensajeBcv.set(mensajeBcv.MONTO_DOLARES, montoAdjudicado.doubleValue());
			mensajeBcv.set(mensajeBcv.CONCEPTO, ordenOriginal.getConceptoId());
			mensajeBcv.set(mensajeBcv.SECTOR, ordenOriginal.getSectorProductivoId());
			mensajeBcv.setUsuarioNM(getUserName());
			mensajeBcv.setOrdeneId(Integer.parseInt(String.valueOf(ordenOriginal.getIdOrden())));
			mensajeBcv.setFechaValor(new Date());
			mensajeBcv.set(mensajeBcv.BROKER, broker);

			//Establecer valores por defecto al mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeBcv);
			
			//Configuracion de la fecha valor para el mensaje BCV			
			mensajeBcv.setFechaValor(ordenOriginal.getFechaValor());
			
			//Generar sentecias sql para guardar el mensaje de Estadística
			String[] sentenciasMje = mensajeDAO.ingresar(mensajeBcv);
		
			//Agregar sentencias de Mensaje a vector Global de queries a ejecutar 
			for(int k= 0; k<sentenciasMje.length; k++){
				querys.add(sentenciasMje[k]);
			}
		}		
		
	}

	/**
	 * Obtiene el valor final para el campo de TITULO de BCV. Usa cache.
	 * @param mensajeBcv objeto correspondiente al mensajeBcv
	 * @param idTitulo id del título a buscar
	 * @return valor final que debe llevar el campo
	 * @throws Exception en caso de error
	 */
	private String obtenerDatosTitulo(MensajeBcv mensajeBcv, String idTitulo) throws Exception{
		String valorFinal = "";
		if (hashTituloBCV.containsKey(idTitulo)){
			valorFinal = hashTituloBCV.get(idTitulo); 
		}else{
			valorFinal = mensajeBcv.obtenerValorFinalTitulo(_dso, idTitulo);
			hashTituloBCV.put(idTitulo, valorFinal);
		}
		return valorFinal;
	}	
	
	


				
	/**
	 * Obtiene las transacciones fijas según la unidad y el vehículo tomador
	 * @param unidadInversion id de la unidad de inversión
	 * @param vehiculoTomador vehículo tomador
	 * @throws Exception en caso de error
	 */
	protected void getTransaccionesFijasClaveNet(String unidadInversion,String vehiculo) throws Exception{		
		
		if (transaccionesFijasCache.containsKey(unidadInversion)){	
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);				
		}else{
			fijaDAO.listarTransaccionesFijasAdjudicacionClaveNet(unidadInversion,vehiculo);
			DataSet ds = fijaDAO.getDataSet();
			
			transaccionesFijasCache.put(unidadInversion,ds);
			transaccionesFijasAdjudicacion = transaccionesFijasCache.get(unidadInversion);
		
		}
		
	}	
	
	/**
	 * Verifica si la comision es de monto fijo
	 * @param montoComision
	 * @param tasaComision
	 * @return
	 */
	private boolean esComisionMontoFijo(String montoComision, String tasaComision){
		//si la tasa de comision es 0 pero el monto es mayor a 0, corresponde a comision de monto fijo
		if(Double.parseDouble(tasaComision)==0 && Double.parseDouble(montoComision)>0){
			return true;
		}		
		return false;
	}
		
	
}// Fin clase

