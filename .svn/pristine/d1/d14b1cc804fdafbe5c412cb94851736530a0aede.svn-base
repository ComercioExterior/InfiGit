package models.intercambio.recepcion.clavenet_personal.adjudicacion_subasta_divisas;

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
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenDetalle;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.IngresoOpicsSitme;
import com.bdv.infi.logic.ProcesarDocumentos;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreosCall;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
import com.bdv.infi.logic.interfaz_varias.MensajeEstadistica;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;

@SuppressWarnings("unused")
@Deprecated
/**
 * Clase utilizada para el manejo operativo de producto SICAD 2 Clavenet Personal (Producto fuera de funcionamiento)   
 * */
public class AdjudicacionSubastaDivisasPersonal extends Transaccion {
	
	/**Parametros de la Toma de Orden 
	 */
	
	private com.bdv.infi.dao.Transaccion transaccion;
	private long contadorOrdenesAdjudicadas;
	
	SolicitudClavenet ordenOpics=null; 
	
	ArrayList<String> querysTransacciones=new ArrayList<String>();
	
	OrdenDAO ordenDAOClavenet;
	
	//Variable que contendra el valor del tipo de proceso que se esta ejecutando (Proceso de Adjudicacion o rechazo de solicitud)
	//private String tipoProcesoAdjRech;
	
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
    	int cantidadOrdenesAdj=0;
	int acumuladorOrdenesAdj=0;

	
	/*private UnidadInversionDAO unidadInversionDao;
	private HashMap<String, Object> parametrosEntrada = new HashMap<String, Object>();
	private ArrayList<CampoDinamico> ArrayCamposDinamicos = new ArrayList<CampoDinamico>();
	private ArrayList<PrecioRecompra> ArrayRecompraTitulos = new ArrayList<PrecioRecompra>();
	private ArrayList<OrdenOperacion> ArrayComisiones = new ArrayList<OrdenOperacion>();*/
	
	TomaOrdenSimulada beanTOSimulada 	= null;
	
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
	
	private DataSet mensajesPorRif = new DataSet(); 
	private HashMap<String, String> hashTituloBCV = new HashMap<String, String>();
	
	private Logger logger = Logger.getLogger(AdjudicacionSubastaDivisasPersonal.class);	
	
	
	//Cache de transacciones fijas y vehículo
	private HashMap<String,DataSet> transaccionesFijasCache = new HashMap<String,DataSet>();
	private TransaccionFijaDAO fijaDAO;

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
	
	boolean poseeComisionInveriable=false;//Comision monto fijo que se cobra aun si el monto Adjudicado es cero
	BigDecimal pctComision;//Porcentaje comision 
	BigDecimal montoComisionVariable;//monto de comision en caso de ser comision Variable
	BigDecimal montoComisionInvariable;
	
	private boolean nuevaConexion = false; 
	
	//Nuevos campos incluidos en requerimiento SICAD_2
	private String idOrdenBCV;
	private String contraparte;
	
	private OrdenDataExt ordenDataExt;	
	
	public void execute() throws Exception {
		
		iniciarProceso();
		logger.info(" ******* Inicio de Proceso AdjudicacionSubastaDivisasPersonal *********");
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
			
			//CAMBIO DE REQUERIMIENTO SICAD 2
			HSSFCell ordenBCV = null;//columna que indica la tasa de cambio asignada		
			HSSFCell rifContraparte = null;//columna de fecha valor de la operacion
		
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
			
			unidadInversionParametro=_req.getParameter("unidad_inversion");						
			ejecucion_id=MSCModelExtend.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS); //Tomamos la secuencia fuera del for para que todos los registros tengan el mismo valor
				
			
			setSessionObject("id_ejecucion_subasta_divisas_personal",null);
			setSessionObject("id_ejecucion_subasta_divisas_personal",ejecucion_id);
			
			for (int i = 0; i < libro.getNumberOfSheets(); i++) {
				
				hoja = libro.getSheet(libro.getSheetName(i));//objeto hoja
	
				for (int fila = 0; fila< hoja.getPhysicalNumberOfRows(); fila++) {//recorrido de filas
					
					if(nuevaConexion){
						transaccion.begin();
						nuevaConexion = false;
					}
					
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
								if(!identificadorArchivo.toString().equals(ConstantesGenerales.IDENTIFICADOR_PLANTILLA_ADJUDICACION_SUBASTA_DIVISAS_PERSONAL)){								
									logger.error("Se ha ingresado un archivo que no corresponde al proceso Adjudicacion Subasta Divisas para personas Naturales, por favor verifique");
									throw new Exception("Se ha ingresado un archivo que no corresponde al proceso Adjudicacion Subasta Divisas para personas Naturales, por favor verifique");
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
										
								//Campos solicitados en nuevo requerimiento SICAD_2	
								rifContraparte = row.getCell((short)7);
								ordenBCV = row.getCell((short)8);		
								
								
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
								//Configuracion de informacion Data Extendida
													
								
								tasaCambio=new BigDecimal(tasaCambioAprobada.toString());
								//Procesamiento de plantilla de Adjudicacion
								//if(tipoProcesoAdjRech.equals(ConstantesGenerales.IDENTIFICADOR_PROCESO_ADJUDICACION_SUBASTA_DIVISAS)){									
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
									
								    //Modificacion de Codigo en requerimiento SICAD_2
									//Configuracion del Rif de Contraparte
									contraparte=rifContraparte.toString();
									if(contraparte!=null && !contraparte.equals("")){
										
										/*logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + " No se ha configurado el Rif de la contraparte ");
										//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Tasa cambio Asignada en el archivo excel no es de tipo numerico por favor verifique");
										continue;*/
										
										ordenDataExt=new OrdenDataExt();			
										ordenDataExt.setIdData(DataExtendida.RIF_CONTRAPARTE);
										ordenDataExt.setValor(contraparte);										
										orden.agregarOrdenDataExt(ordenDataExt);
									}/* else {
										ordenDataExt=new OrdenDataExt();			
										ordenDataExt.setIdData(DataExtendida.RIF_CONTRAPARTE);
										ordenDataExt.setValor(contraparte);										
										orden.agregarOrdenDataExt(ordenDataExt);
										
									}*/
									//Modificacion de Codigo en requerimiento SICAD_2
									//Configuracion de numero de Orden BCV
									idOrdenBCV=ordenBCV.toString();									
									if(idOrdenBCV==null || idOrdenBCV.equals("")){
										logger.info("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + " No se ha configurado el valor del campo Orden BCV");
										//System.out.println("Orden: "+ codOrden.toString().replaceAll("\\.0", "")+ " - " + "El valor ingrsado en el campo Tasa cambio Asignada en el archivo excel no es de tipo numerico por favor verifique");
										continue;
									} else {		
										ordenDataExt=new OrdenDataExt();			
										ordenDataExt.setIdData(DataExtendida.ORDEN_BCV);
										ordenDataExt.setValor(idOrdenBCV);										
										orden.agregarOrdenDataExt(ordenDataExt);
									}
									
									//VALIDACION UNIDAD DE INVERSION *******INFI_TB_106_UNIDAD_INVERSION 
									unidadInvDAO.listarPorNombre(ui.toString(),ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL,null);								
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
									//TODO MODIFICAR ESTATUS A ENVIADA 
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

										//Creacion de solicitud Clavenet Personal Informacion de la solicitud que no varia independientemente del monto Adjudicado
										SolicitudClavenet solicitud=new SolicitudClavenet();
										solicitud.setIdOrdenInfi(orden.getIdOrden());
										solicitud.setFechaAdjudicacion(Utilitario.DateToString(new Date(),"dd/MM/yyyy"));
										solicitud.setPrecioAdjudicacion(100);//Precio negociado es monto fijo de 100%
								
										
										//BUSQUEDA DE INFORMACION DE LA TABLA OPICS SOLICITUD_SITME
									 	ordenCLaveNet=new OrdenDAO(_dso);																																		
										ordenCLaveNet.mostrarSolicitudesClaveNet(solicitud);
										if(ordenCLaveNet.getDataSet().count()>0){
											ordenCLaveNet.getDataSet().first();
											ordenCLaveNet.getDataSet().next();	
										} else {
											logger.info("Orden: " + idOrdenProcesar +" - La orden no tiene una solicitud de Clavenet Asociada");
											//System.out.println("Orden: " + idOrdenProcesar +" - La orden no tiene una solicitud de Clavenet Asociada");
											continue;
										}
										
										pctComision=new BigDecimal(ordenCLaveNet.getDataSet().getValue("PORC_COMISION"));//PORC_COMISION
										
										//CALCULO DE MONTO COMISION
										if(pctComision.equals(new BigDecimal(0))){//COMISION INVARIABLE. Si monto porcentaje comision es cero, se trata de comision invariable											
											poseeComisionInveriable=true;	
											montoComisionInvariable=new BigDecimal(ordenCLaveNet.getDataSet().getValue("monto_comision"));											
										} else {											
											poseeComisionInveriable=false;
											montoComisionVariable=new BigDecimal(0);
									    	pctComision=pctComision.divide(new BigDecimal(100));
									    	montoComisionVariable=montoAdjudicado.multiply(tasaCambio).multiply(pctComision).setScale(2,BigDecimal.ROUND_HALF_EVEN);
									    	//System.out.println("Comision monto Variable ----> " + montoComisionVariable);
									    	solicitud.setMontoComision(montoComisionVariable);
										}
										
										//********************** ORDENES NO ADJUDICADAS ********************** 
										if(montoAdjudicado.equals(new BigDecimal(0))){//MONTO ADJUDICADO CERO 								
											//System.out.println("************ MONTO ADJUDICADO CERO ************");
											orden.setMontoAdjudicado(0);
											orden.setStatus(StatusOrden.NO_ADJUDICADA_INFI);
											orden.setFechaAdjudicacion(new Date());
											
											//PROCESO DE GENERACION DE OPERACIONES
											generarOperaciones(orden);

											//Modificacion de estatus y monto para la solicitud Clavenet Personal 
											solicitud.setEstatus(ConstantesGenerales.ESTATUS_ORDEN_NO_PACTADA);
											solicitud.setMontoComision(new BigDecimal(0));
											solicitud.setMontoAdjudicado(new BigDecimal(0));
											actualizarSolicitudesClavenet(solicitud);//Actualizacion de solicitud clavenet personal
											
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
											orden.setFechaUltActualizacion(Utilitario.StringToDate(fechaValorString,"dd-MM-yyyy"));
											
																														
											actualizacionTitulosAsociados(orden);
																																												
											
											
											//Modificacion de estatus y monto para la solicitud Clavenet Personal
											
											/*MODIFICACION REQUERIMIENTO SICAD 2 NM26659*/ 
											solicitud.setEstatus(ConstantesGenerales.ESTATUS_ORDEN_PACTADA);//											
											
											solicitud.setValorNominalUSD(montoAdjudicado);//Configuacion de Valor Nominal
											solicitud.setValorNominal(montoAdjudicado.multiply(tasaCambio).setScale(2,BigDecimal.ROUND_HALF_EVEN));//Configuacion de Valor Nominal en Bolivares
											solicitud.setMontoAdjudicado(montoAdjudicado);
											solicitud.setValorEfectivoUSD(montoAdjudicado);
										    solicitud.setValorEfectivo(montoAdjudicado.multiply(tasaCambio).setScale(2,BigDecimal.ROUND_HALF_EVEN));//Configuacion de Valor Efectivo en Bolivares

									
										    
											generarOperaciones(orden);//PROCESO DE GENERACION DE OPERACIONES
											actualizarSolicitudesClavenet(solicitud);//Actualizacion de solicitud clavenet personal
											
											ArrayList<OrdenTitulo> arregloOrdenTitulo=new ArrayList<OrdenTitulo>();
											arregloOrdenTitulo.add(ordenTitulo);
											
											
											//Configuracion de la Informacion de la INSTRUCCION DE PAGO
											clienteCuentaEntidad=new CuentaCliente();									
											clienteCuentaEntidad.setIdOrden(orden.getIdOrden());
											clienteCuentaEntidad.setClient_id(Long.parseLong(idCliente));
											clienteCuentaEntidad.setCtecta_uso(UsoCuentas.RECOMPRA);
											
											//consulta del tipo de cuenta ha realizar el abono: 1)Cuenta en el exterior 2)Cuenta nacional en Dolares
											//System.out.println("DATASET ------ordenCLaveNet.getDataSet() " + ordenCLaveNet.getDataSet());
											String cuentaAbono=ordenCLaveNet.getDataSet().getValue("cta_abono");  	
											
											if(cuentaAbono!=null && !cuentaAbono.equals("")){
												//Verifica el tipo de cuenta abono especificado desde clavenet personal
												if(cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO)){//Si especifica cuenta en el extranjero se configura el tipo de instruccion como cuenta Swift
													clienteCuentaEntidad.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
												} else if(cuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_NACIONAL)){//Si especifica cuenta nacional en dolares se configura el tipo de instruccion como Cuenta Nacional en Dolares
													clienteCuentaEntidad.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_NACIONAL_DOLARES));	
												}										
											}else {
												logger.info("Orden: " + idOrdenProcesar +" - No posee especificacion de tipo de operacion (Cta Nacional en Dolares o Cuenta en el Extranjero)");
												//System.out.println("Orden: " + idOrdenProcesar +" - No posee especificacion de tipo de operacion (Cta Nacional en Dolares o Cuenta en el Extranjero)");
												throw new Exception("Orden: " + idOrdenProcesar +" - No posee especificacion de tipo de operacion (Cta Nacional en Dolares o Cuenta en el Extranjero)");
											}
											String nombreCLienteClavenet=ordenCLaveNet.getDataSet().getValue("NOMBRE_CLIENTE");											
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
										/*	String aba=ordenCLaveNet.getDataSet().getValue("CTA_ABA");
											
											if(aba!=null && (!aba.equals(""))){
												aba=aba.trim();
												clienteCuentaEntidad.setCtecta_bcocta_aba(aba.toUpperCase());
											}*/
											
											//String observacion="Orden "+orden.getIdOrden()+" Canal Clavenet";
											
											String nombreUnidadInversion=null;
											clienteCuentaEntidad.setCtecta_observacion(nombreUnidadInversion);

											String cedulaBeneficiario=ordenCLaveNet.getDataSet().getValue("CED_RIF_CLIENTE");
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
										
											clienteCuentasDAO=new ClienteCuentasDAO(_dso);
											String [] clientesCuentasSql=null;											
											clientesCuentasSql=clienteCuentasDAO.insertarClienteCuentas(clienteCuentaEntidad);											
											for(String element : clientesCuentasSql){
												////System.out.println("INSERT DE CLIENTES CUENTAS: " + element);
												querysTransacciones.add(element);												
											}
											
										//ASIGNACION DE ID DE EJECUCION A LA ORDEN 204
										orden.setIdEjecucion(Long.parseLong(ejecucion_id));
											
										//Busca los documentos asociados a la adjudicación										
										Orden copiaOrden=(Orden)orden.clone();										

										ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(_dso);
																					
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
										
										//Insercion del Detalle de la operacion 
										OrdenDetalle ordenDetalle=new OrdenDetalle();
										ordenDetalle.setIdOrden(orden.getIdOrden());
										ordenDetalle.setNumeroTicketClavenet(Long.parseLong(ordenCLaveNet.getDataSet().getValue("ID_ORDEN")));
										ordenDetalle.setFechaSalidaViaje(ordenCLaveNet.getDataSet().getValue("FECHA_SALIDA_VIAJE"));
										ordenDetalle.setFechaRetornoViaje(ordenCLaveNet.getDataSet().getValue("FECHA_RETORNO_VIAJE"));
										ordenDetalle.setNumeroPasaporte(ordenCLaveNet.getDataSet().getValue("NUM_PASAPORTE"));
										
										
										querysTransacciones.add(ordenDAO.insertarOrdenDetalle(ordenDetalle));																													
										}//FIN MONTO ADJUDICADO MAYOR A CERO 											
										orden.getOperacion().clear();										
										for (String query : ordenDAO.modificar(orden)) {										
											querysTransacciones.add(query);	
										}																													
									}//FIN DE SI LA ORDEN EXISTE
											
							}//FIN RECORRIDO DE FILAS A PARTIR DE 2DA FILA
																				
							try {
								
								Statement statement =transaccion.getConnection().createStatement();
								//System.out.println("*******************************************");
								for(String element:querysTransacciones){
									//System.out.println("Sentencia ----> " + element);
									statement.addBatch(element);
								}
								//System.out.println("*******************************************************************");
								++cantidadOrdenesAdj;
								++contadorOrdenesAdjudicadas;
								statement.executeBatch();						
								statement.close();
								
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
									
									//cerrando conexion
									transaccion.closeConnection();
									nuevaConexion = true;	
									continue;
									
								}catch(Exception e){
									querysTransacciones.clear();
									transaccion.getConnection().rollback();
									//System.out.println("Ha ocurrido un error Inesperado " + e.getMessage());
									logger.error("Ha ocurrido un error Inesperado " + e.getMessage());

									//cerrando conexion
									transaccion.closeConnection();
									nuevaConexion = true;	
									continue;
								}								
								querysTransacciones.clear();
				}//FIN RECORRIDO DE FILAS	
				
			}
			
			/*for (String element : querysTransacciones) {		
				//System.out.println("QUERY --------> " + element);						
			}*/	
			
			}
	
		}	finally	{	
				
			transaccion.getConnection().commit();
			/*Codigo comentado en resolucion de incidencias Calidad SICAD_2*/
			//PROCESO DE CIERRE AUTOMATICO DE LA UNIDAD DE INVERSION
			//cerrarUnidadInversion(Long.parseLong(unidadInversionParametro));
			
			transaccion.getConnection().commit();
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
			    	ecCall.execute(ec.parametros.get(ParametrosSistema.DEST_CLIENTE), "", ec, unidadInversionParametro, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL, StatusOrden.ADJUDICADA, Integer.parseInt(ejecucion_id));
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
		DataSet respuestaAdjudicacion=new DataSet();
		respuestaAdjudicacion.append("respuesta_adjudicacion",java.sql.Types.VARCHAR);
		respuestaAdjudicacion.addNew();
		
		ordenDAO.ordenesPorAdjudicarByIdUnidad(idUnidadInv);
		if(ordenDAO.getDataSet().count()>0){
			ordenDAO.getDataSet().first();
			while(ordenDAO.getDataSet().next()){
				idOrdenesSinProcesar.append(ordenDAO.getDataSet().getValue("ORDENE_ID")+ " - ");				
			}
			
			logger.warn("CIERRE DE ADJUDICACION: No se puede cerrar la unidad de inversion debido a que la(s) siguiente(s) orden(es) no se han procesado:   " + idOrdenesSinProcesar.toString());
			respuestaAdjudicacion.setValue("respuesta_adjudicacion","CIERRE DE ADJUDICACION: El proceso de Adjudicacion no ha finalizado, Quedan operaciones que no han sido procesadas asociadas a la Unidad de Inversion");
			//System.out.println("No se puede cerrar la unidad de inversion debido a que la(s) siguiente(s) orden(es) no se han procesado:   " + idOrdenesSinProcesar.toString());
		} else {
			respuestaAdjudicacion.setValue("respuesta_adjudicacion","CIERRE DE ADJUDICACION:  Se ha Adjudicado la unidad de inversion de forma satisfactoria");
			logger.warn("CIERRE DE ADJUDICACION:  Se ha Adjudicado la unidad de inversion de forma satisfactoria");
			unidadInvDAO.modificarStatus(idUnidadInv,UnidadInversionConstantes.UISTATUS_ADJUDICADA);
		}
		storeDataSet("respuesta_adjudicacion",respuestaAdjudicacion);
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
		
		
	try {
				
		String tipoMonedaOperacionComision=ConstantesGenerales.SIGLAS_MONEDA_LOCAL;//Variable de almacenamiento de tipo de moneda de operacion de comision
		String tipoMonedaOperacionCapital=ConstantesGenerales.SIGLAS_MONEDA_LOCAL;//Variable de almacenamiento de tipo de moneda de operacion de capital				
		BigDecimal montoComision=new BigDecimal(0);			
									
					String numeroRetencion=null;
					BigDecimal montoOperacion=new BigDecimal(0);
					for (OrdenOperacion ordenOperacion: orden.getOperacion()) {
						numeroRetencion=ordenOperacion.getNumeroRetencion();
						montoOperacion=ordenOperacion.getMontoOperacion();
					}
				   //*************************GENERACION DE OPERACION DESBLOQUE CAPITAL Y COMISION*************************							
					OrdenOperacion operacionDesbloqueo = new OrdenOperacion();
					
					BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN,false,TransaccionFinanciera.BLOQUEO);
					operacionDesbloqueo.setInComision(ConstantesGenerales.FALSO);
					operacionDesbloqueo.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_TOMA_ORDEN));						
											
					montoOperacion=montoOperacion.setScale(2,BigDecimal.ROUND_HALF_EVEN);
					operacionDesbloqueo.setMontoOperacion(montoOperacion);
			
					operacionDesbloqueo.setTasa(new BigDecimal(100));
			
	
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
					operacionDesbloqueo.setIdMoneda(tipoMonedaOperacionComision);
					operacionDesbloqueo.setNumeroRetencion(numeroRetencion); 
					
					operacionDesbloqueo.setTipoTransaccionFinanc(TransaccionFinanciera.DESBLOQUEO);
					
										
					operacionDesbloqueo.setFechaAplicar(new Date());
					
					querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDesbloqueo,false));
								
					
			   //************************CONFIGURACION DE OPERACION DEBITO COMISION******************************
				montoComision=orden.getMontoComisionOrden();

				//INCIDENCIA PRODUCCION TTS_441 (SICAD 2) NM26659_21/03/2014
				//Validacion para creacion de operacion Debito Comision. NO generar operacion si monto adjudicado es cero y la comision NO es fija 
					if(!adjudicacion.equals(ADJUDICADO_CERO) || poseeComisionInveriable){
										
						//Operaciones 3) debito por el monto comision --> Monto registrado en la tabla 204 campo	ORDENE_PED_COMISIONES									
						OrdenOperacion operacionDebitoComision = new OrdenOperacion();
							
						//Busqueda de codigos de operacion DEBITO COMISION
						BuscarCodigoyNombreOperacion(orden.getVehiculoTomador(),ConstantesGenerales.TRANSACCION_FIJA_COMISION,false,TransaccionFinanciera.DEBITO);
							
						operacionDebitoComision.setCodigoOperacion(codigo_operacion);				
						operacionDebitoComision.setNombreOperacion(nombre_operacion);
												
						operacionDebitoComision.setIdOrden(orden.getIdOrden());					
						operacionDebitoComision.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);					
						operacionDebitoComision.setIdTransaccionFinanciera(String.valueOf(ConstantesGenerales.TRANSACCION_FIJA_COMISION));
																																	
						operacionDebitoComision.setAplicaReverso(false);									
						operacionDebitoComision.setIndicadorComisionInvariable(ConstantesGenerales.VERDADERO);
														
						operacionDebitoComision.setCuentaOrigen(orden.getCuentaCliente());					
						operacionDebitoComision.setIdMoneda(tipoMonedaOperacionComision);				
						operacionDebitoComision.setInComision(ConstantesGenerales.VERDADERO);				
						operacionDebitoComision.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);			
						
						if (poseeComisionInveriable){					
							operacionDebitoComision.setTasa(new BigDecimal(0));
							operacionDebitoComision.setMontoOperacion(montoComisionInvariable.setScale(2,BigDecimal.ROUND_HALF_EVEN)); //configurar monto comision Invariable
						} else {					
							operacionDebitoComision.setTasa(pctComision);
							operacionDebitoComision.setMontoOperacion(montoComisionVariable.setScale(2,BigDecimal.ROUND_HALF_EVEN)); //configurar monto comision Invariable
						}
						
						operacionDebitoComision.setFechaAplicar(new Date());
						
						//operacionDebitoComision.setIdOperacionRelacion(operacionDesbloqueo.getIdOperacion());									
						operaciones.add(operacionDebitoComision);
						
						//Modificacion de COMISION para el registro de la tabla INFI_TB_204_ORDENES campo ORDENE_PED_COMISIONES
						//orden.setMontoComisionOrden(montoComision);
						poseeComisionInveriable=false;			
						querysTransacciones.add(operacionDAO.insertarOrdenesOperacionesCLaveNet(operacionDebitoComision,false));		
			
					}
			/*} else {//FIN VALIDACION PARA OPERACION DEBITO COMISION				
				orden.setMontoComisionOrden(montoComision);
				orden.setComisionCero(true);							
			}*/
					
			
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
												
				//System.out.println("MONTO ADJUDICADO ----> " + orden.getMontoAdjudicado());
				//System.out.println("TASA CAMBIO ----> " + orden.getTasaCambio());
				
				totalMontoCapitalOperaciones=new BigDecimal(orden.getMontoAdjudicado()).multiply(new BigDecimal(orden.getTasaCambio()));//TODO Validar si el campo ORD.ORDENE_PED_TOTAL es CAPITAL + COMISION          				
				totalMontoCapitalOperaciones=totalMontoCapitalOperaciones.setScale(2,BigDecimal.ROUND_HALF_EVEN);											
				//System.out.println("totalMontoCapitalOperaciones -----> "  + totalMontoCapitalOperaciones);
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
		
	/**
	 * Metodo para la actualizacion de las solicitudes clavenet personal
	 * */
	private void actualizarSolicitudesClavenet(SolicitudClavenet ordenClavenet) throws Exception{
		
		try {
			ordenDAOClavenet= new OrdenDAO(_dso);
			logger.info("Actualizacion de la solicitud Clavenet Personal en la tabla SOLICITUDES_SITME ");
			//System.out.println("Actualizacion de la solicitud Clavenet Personal en la tabla SOLICITUDES_SITME ");
			querysTransacciones.add(ordenDAOClavenet.actualizarSolicitudClavenet(ordenClavenet,StatusOrden.EN_TRAMITE));
								
		}catch(Exception e){
			//System.out.println("Se ha generado un error en el proceso de  actualizarOrdenesOPICS" + e.getMessage());
			logger.error("Se ha generado un error en el proceso de actualizarOrdenesOPICS " + e.getMessage());
			throw new Exception("Se ha generado un error en el proceso de actualizarOrdenesOPICS " + e.getMessage());
		}
	}
	
}// Fin clase

