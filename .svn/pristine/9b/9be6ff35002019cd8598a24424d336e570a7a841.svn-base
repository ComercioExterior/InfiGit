package models.intercambio.transferencia.generar_archivo_subasta_divisas_personal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import models.exportable.ExportableBufferString;

import org.apache.log4j.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.AuditoriaDAO;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ConceptosDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.CallEnvioCorreos;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

/** Clase que exporta a excel la ordenes 
 * que deben ser enviadas al Banco de Venezuela
 * @author jvillegas, modif: nm25287
 */
public class ExportarOrdenes extends ExportableBufferString{
	
	private Logger logger = Logger.getLogger(ExportarOrdenes.class);
	DataSet camposDinamicosDataSet = new DataSet();
	DataSet titulos = new DataSet();
	boolean tieneCamposDinamicos = true;
	Archivo archivo	= new Archivo();
	ControlArchivoDAO control = null;
	OrdenDAO control_orden = null;	
	String nombreMonedaBs = "";
	//NM29643 13/06/2014
	private String transaccionId;
	private String  proceso_id = null;
	private ProcesosDAO procesoDAO;
	private Proceso proceso;
	private String mensajeError = "";
	private boolean procesoCreado = false;
	DataSet datosArchivo = new DataSet();
	String estatusSolicitudes=null;
	//nm29643 infi_TTS_466
	boolean registrarEnINFI = false;
	
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		this.aplicarFormato = false;			
		control	= new ControlArchivoDAO(_dso);	
		String ejecucion_id			=dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		String unidad_inversion 	= null;
		String fecha=null;
		String nombreArchivo = "";
		int inRecepcion 			= 0;
		String ruta ="";
		String ultimoRegExportar="";
		String tipoSolicitud="";
		
		nombreMonedaBs = (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA);
		
		if (_req.getParameter("undinv_id")!=null&&!_req.getParameter("undinv_id").equals("")){
			unidad_inversion= _req.getParameter("undinv_id");
		}else{
			unidad_inversion= getSessionObject("unidadInversion").toString();
		}
		
		fecha=_req.getParameter("fecha");
		//System.out.println(fecha);
		estatusSolicitudes=_req.getParameter("estatus");
		tipoSolicitud=_req.getParameter("tipo_solicitud");
		
		archivo.setUnidadInv(Integer.parseInt(unidad_inversion));
		
		datosArchivo.append("nombre_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("display", java.sql.Types.VARCHAR);
		datosArchivo.append("msg", java.sql.Types.VARCHAR);
		datosArchivo.addNew();
		
		try{
		
		
		//NM29643 13/06/2014
		transaccionId = TransaccionNegocio.GEN_ARCH_SICAD2_CLAVE;
		
		//Consulta el id del proximo proceso a aperturar
		proceso_id = dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_PROCESOS);
		//Creacion del proceso
		proceso = new Proceso();
		proceso.setEjecucionId(Integer.parseInt(proceso_id));
		proceso.setTransaId(transaccionId);
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		if(getUserName()!=null && !getUserName().equals("")){
			proceso.setUsuarioId(Integer.parseInt(usuarioDAO.idUserSession(getUserName())));
		}
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		procesoDAO = new ProcesosDAO(_dso);
		
		//SE INICIA EL PROCESO DE CARGA DE CRUCES
		procesoCreado = comenzarProceso();
		
		//System.out.println("request: "+getDataSetFromRequest());
		ultimoRegExportar=_req.getParameter("ult_registro");
		
				
		if(procesoCreado){ //PROCESO INICIADO
						
			//NM25287 Eliminación de valición de numero de ticket existente en data_ext 26/06/2014 Mejoras exportacion
	        /*UnidadInversionDAO unidadDAO_SITME = new UnidadInversionDAO(_dso);
			
			String var = unidadDAO_SITME.getInstrumentoFinancieroPorUI(Integer.parseInt(unidad_inversion));*/ 
					
			//Creamos el nombre del archivo dinamicamente
			//control.nombreArchivo(archivo);//retorna el numero del veiculo colocador
			nombreArchivo = obtenerNombreArchivo("intercambio");
			archivo.setNombreArchivo(nombreArchivo);
			archivo.setIdEjecucion(Long.parseLong(ejecucion_id));
			archivo.setInRecepcion(inRecepcion);
			archivo.setUsuario(getUserName());
			//Nm29643 infi_TTS_466
			archivo.setIdEjecucionRelacion(proceso.getEjecucionId());

			registrarInicio(nombreArchivo);
			
			//OBTENER LOS DATOS A EXPORTAR A EXCEL
			obtenerOrdenesExportarExcel(unidad_inversion,_dso, fecha, estatusSolicitudes,ultimoRegExportar,tipoSolicitud);
			
			_req.getSession().removeAttribute("unidadInversion");	
			
			
			//RESPALDAR ARCHIVO EN EL SERVIDOR 
			try {
				//Consultar ruta de respaldo
				ruta=ParametrosDAO.listarParametros("TEMP_DIRECTORY",_dso);
				logger.debug("getRootWebApplicationPath: "+FileUtil.getRootWebApplicationPath());
				logger.debug("RUTA + nombreArchivo: "+ruta+" + "+nombreArchivo);
				//Crear archivo de respaldo
				FileUtil.crearArchivo(sbArchivo,FileUtil.getRootWebApplicationPath()+nombreArchivo,false);
			
			} catch (Exception e) {
				logger.error("Ocurrio un error al generar el archivo de respaldo",e);
			}		
			
			
			datosArchivo.setValue("nombre_archivo", nombreArchivo);
			datosArchivo.setValue("display", "block");
			
			registrarFin();
			obtenerSalida();
			
			
		}else{ //PROCESO NO CREADO
			datosArchivo.setValue("nombre_archivo", "");
			datosArchivo.setValue("display", "none");
		}
		
		datosArchivo.setValue("msg", mensajeError);
		
		
		}catch(Exception e) {
			logger.error("Ha ocurrido un error en el proceso "+proceso.getTransaId()+": "+e.getMessage());
			proceso.setDescripcionError("Error inesperado durante el proceso "+proceso.getTransaId());
			setMensajeError("Error inesperado en el proceso de carga de cruces<br/>");
			ArrayList<String> query = new ArrayList<String>();
			query.add(procesoDAO.modificar(proceso));
			if(!procesoDAO.ejecutarStatementsBatchBool(query)){ //Si no se cerro el ciclo
				logger.error("No se pudo cerrar el proceso "+proceso.getTransaId());
				proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+"No se pudo cerrar el proceso "+proceso.getTransaId());
			}
			
		}finally{
			
			if(procesoCreado){ //Si se creo el proceso
				//TERMINAR PROCESO
				terminarProceso();
			}
			
			//REGISTRAR AUDITORIA
			registrarAuditoria();
			

			//NM29643 infi_TTS_466 17/07/2014: Actualizacion del proceso de envio de correos
			//Solo si en generacion de archivos de las ordenes en estatus RECIBIDA se llama al proceso de envio de correos
			if(registrarEnINFI){ //Si se trata del registro inicial de las ordenes de solicitudes sitme en INFI
				logger.debug("UI ID en ExportarOrdenes Personal - Llamada a ENVIO CORREOS -----------------: "+unidad_inversion);
				logger.debug("EJECUCION_ID de las ordenes a considerar - Llamada a ENVIO CORREOS -----------------: "+String.valueOf(archivo.getIdEjecucionRelacion()));
				//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Desde Aqui)**************
				CallEnvioCorreos callEnvio = new CallEnvioCorreos(ConstantesGenerales.ENVIO_BCV_PER, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, getUserName(), unidad_inversion, _dso, String.valueOf(archivo.getIdEjecucionRelacion()), null, null);
				Thread t = new Thread(callEnvio); //Ejecucion del hilo que envia los correos
				t.start();
				//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Hasta Aqui)**************
			}
			
		}
		
		storeDataSet("datosArchivo", datosArchivo);
		
		
	}//fin execute
	

	private void obtenerOrdenesExportarExcel(String unidadInversion, DataSource _dso, String fecha, String estatusSolicitud, String ultimoRegExportar, String tipoSolicitud) throws Exception{
		OrdenDAO  				ordenDAO 		= new OrdenDAO(_dso);			
		BlotterDAO  			blotterDAO 		= new BlotterDAO(_dso);
		VehiculoDAO  			vehiculoDAO 	= new VehiculoDAO(_dso);
		UnidadInversionDAO		uiDAO		= new UnidadInversionDAO(_dso);
		HashMap<String, String> conceptos 		= null;
        DataSet 				ordenes 		= null;
        
        String 					tipoPer 		= "";
        String 					cedRif 			= "";
        String 					empresaID 		= "";;
        String 					blotterId 		= "";
        String 					vehiculo_general="";
        BigDecimal 				tasa_pool		= null;
        BigDecimal 				generico  		= new BigDecimal(0);
        String 					idCliente 		= null;
        Orden 					orden 			= null; 
        String 					UINombre		= "";
        String 					codigoOperacionBloqueo = "";
        int 					instrumentoFinacncId =0;
        com.bdv.infi.data.TransaccionFija transaccionFija=null;
        String 					cabeceraDataExt = null;      
        String 					registrarOrdenInfi  ="";
        boolean					consultarOrdenEnviada=false;
        String 					cedula;
		logger.info("ExportarOrdenes.obtenerOrdenesExportarExcelSITME->inicio. Unidad Inversion: "+unidadInversion);
        try{	        
//        	NM25287 SICAD II. Se solicitó NO actualizar el estatus en SOLICITUDES SITME en la exportacion de ordenes. 27/03/2014
        	if (estatusSolicitud.equalsIgnoreCase(StatusOrden.EN_TRAMITE)){
				estatusSolicitud=ConstantesGenerales.ESTATUS_ORDEN_RECIBIDA;
				consultarOrdenEnviada=true;
			}
			//Obtener ordenes SITME
        	//NM25287 CONTINGENCIA SIMADI 11/02/2015. Filtro por oferta y demanda
			ordenDAO.listarSolicitudesSITME(estatusSolicitud,fecha,consultarOrdenEnviada,ultimoRegExportar,tipoSolicitud,Long.parseLong(unidadInversion));
			ordenes = ordenDAO.getDataSet();
			logger.info("ExportarOrdenes.obtenerOrdenesExportarExcelSITME->ordenDAO.listarSolicitudesSITME"+ordenes);					
			String idMoneda=null;
			String tipoCuentaAbono=null;
						
			//Obtener id de blotter
			blotterDAO.listarBlotterPorUI(unidadInversion);
			if (blotterDAO.getDataSet()!=null&& blotterDAO.getDataSet().count()>0) {
				blotterDAO.getDataSet().next();
				blotterId = blotterDAO.getDataSet().getValue("BLOTER_ID");
			}			
			logger.info("ExportarOrdenes.obtenerOrdenesExportarExcelSITME->blotterId: "+blotterId);
			
			//Obtener id del vehiculo
			vehiculoDAO.obtenerVehiculoBDV();			
			if (vehiculoDAO.getDataSet()!=null&& vehiculoDAO.getDataSet().count()>0) {
				vehiculoDAO.getDataSet().next();
				vehiculo_general = vehiculoDAO.getDataSet().getValue("vehicu_id");
				archivo.setVehiculoId(vehiculo_general);
			}
			
			logger.info("ExportarOrdenes.obtenerOrdenesExportarExcelSITME->vehiculo_general: "+vehiculo_general);
			//Obtener id de empresa y tasa pool			
			uiDAO.obtenerDatosUIporId("EMPRES_ID, UNDINV_TASA_POOL,UNDINV_NOMBRE",unidadInversion);
			if (uiDAO.getDataSet()!=null&& uiDAO.getDataSet().count()>0) {
				uiDAO.getDataSet().next();
				empresaID = uiDAO.getDataSet().getValue("EMPRES_ID");
				UINombre = uiDAO.getDataSet().getValue("UNDINV_NOMBRE");
			}
			logger.info("obtenerOrdenesExportarExcelSITME->empresaID: "+empresaID);
			logger.info("obtenerOrdenesExportarExcelSITME->tasa_pool: "+tasa_pool);
			//Obtener conceptos
			conceptos = obtenerConceptos();
			
			//OBTENER DATA EXTENDIDA DE LA UNIDAD DE INVERSION
			//cabeceraDataExt=stringDataExtendidaUnidadInv(unidadInversion);
			//cabeceraDataExt=DataExtendida.DESTINO_FONDOS+";"+DataExtendida.ORIGEN_FONDOS+";";
			cabeceraDataExt="";
			//Crear cabecera del archivo
			crearCabecera(cabeceraDataExt);
			
			transaccionFija=this.obtenerCodigosOperacion(vehiculo_general, unidadInversion);
			if(transaccionFija==null || transaccionFija.getCodigoOperacionCteBlo()==null||transaccionFija.getCodigoOperacionCteBlo().trim()=="0"||transaccionFija.getCodigoOperacionCteBlo().trim()==""){
				throw new Exception ("No se encuentra configurado el código de bloqueo asociado al vehículo e instrumento financiero");
			}
			codigoOperacionBloqueo=transaccionFija.getCodigoOperacionCteBlo();
			instrumentoFinacncId=transaccionFija.getIdTransaccion();
			System.out.println("instrumentoFinacncId: "+instrumentoFinacncId);
			
			
			if (ordenes!=null && ordenes.count()>0) {
				while (ordenes.next()) {
					logger.info("obtenerOrdenesExportarExcelSITME->id_ticket: "+ordenes.getValue("ID_ORDEN"));
					try {
						
						//Busqueda del tipo de monedda de negociacion que se especifica en desde el canal clavenet			
						idMoneda=ordenes.getValue("cta_abono_moneda");
						
						//Busqueda de cuenta abono: 1) Cuenta en el exterior 2)Cuenta nacional en dolares										
						tipoCuentaAbono=ordenes.getValue("cta_abono");
						
						//NM25287 Eliminación de valición de numero de ticket existente en data_ext 26/06/2014 Mejoras exportacion
						/*ordenDAO.listarDataExtPorIdTicket(ordenes.getValue("ID_ORDEN"));
						if (ordenDAO.getDataSet()!=null&& ordenDAO.getDataSet().count()>0) {
							logger.info("obtenerOrdenesExportarExcelSITME->La orden ya se encuentra registrada ");
							continue;
						}*/
						
						cedula=ordenes.getValue("CED_RIF_CLIENTE").trim();
						tipoPer = cedula.substring(0, 1);
						cedRif = cedula.substring(1, cedula.length() - 1);

						String CONTROL1="OK";
						registrarOrdenInfi=ordenes.getValue("REGISTRAR_ORDEN_INFI");
						orden = new Orden();
												
						if(registrarOrdenInfi.equalsIgnoreCase("SI")){
							
						//NM29643 INFI_TTS_466
						registrarEnINFI = true;
							
						//Obtener el id del cliente. Se consulta el cliente en la BD de INFI, si no existe se registra
						//idCliente = mClientes.consultarClientePEVB(tipoPer, Long.parseLong(cedRif),_dso,usuario,ipUsuario);
						
						//BUSQUEDA DE DATOS DE CLIENTE DESDE TABLA SOLICITUDES_SITME
						ClienteDAO clienteDAO 	= new ClienteDAO(_dso);
						clienteDAO.listarCliente(0, tipoPer,Long.parseLong(cedRif));						
						com.bdv.infi.data.Cliente clienteNuevo = null;
						//Si el cliente no existe en la base de datos de INFI se inserta con la informacion obtenida de la tabla SOLICITUDES_SITME
						if(clienteDAO.getDataSet().count()==0){
							if (Long.valueOf(cedRif) > 0){
								try{
																																															
									clienteNuevo = new com.bdv.infi.data.Cliente();
									clienteNuevo.setRifCedula(Long.parseLong(cedRif));
									String nombreCliente=ordenes.getValue("NOMBRE_CLIENTE");
									
									if(nombreCliente!=null){
										nombreCliente=nombreCliente.trim();
										nombreCliente=nombreCliente.toUpperCase();
									}
									clienteNuevo.setNombre(nombreCliente);
									clienteNuevo.setTipoPersona(tipoPer);
									String direccionCliente=ordenes.getValue("CTA_DIRECCION_C");
									
									if(direccionCliente!=null){
										direccionCliente=direccionCliente.trim();
										direccionCliente=direccionCliente.toUpperCase();
									}
									clienteNuevo.setDireccion(direccionCliente);
									
									//TODO: VERIFICAR CUAL CAMPO DE NUMERO SE INGRESARA
									String clienteNumeroTlf=ordenes.getValue("CTA_TELEFONO");
									if(clienteNumeroTlf!=null){
										clienteNumeroTlf=clienteNumeroTlf.trim();
										clienteNumeroTlf=clienteNumeroTlf.toUpperCase();
									}
									clienteNuevo.setTelefono(clienteNumeroTlf);
									
									clienteNuevo.setTipo(ConstantesGenerales.CLIENTE_BANCA_COMERCIAL);
									
									String correoCliente=ordenes.getValue("EMAIL_CLIENTE");
									if(correoCliente!=null){
										correoCliente=correoCliente.trim();
										correoCliente=correoCliente.toUpperCase();
									}
									clienteNuevo.setCorreoElectronico(correoCliente);
									clienteNuevo.setEmpleado(false);												
									clienteNuevo.setCodigoSegmento(ConstantesGenerales.CODIGO_SEGMENTO); //TODO: Verificar valor de la constante de segmento
									
									clienteDAO.insertar(clienteNuevo);
									idCliente=String.valueOf(clienteNuevo.getIdCliente());
									clienteDAO.cerrarConexion();
																	
								}catch (Exception e) {	
									System.out.println("Ha ocurrido un error en el proceso de creacion de cliente en INFI " + e.getMessage());
									logger.error("Ha ocurrido un error en el proceso de creacion de cliente en INFI " + e.getMessage());
									throw e;
								}
							}
						} else {
							//NM32454
							//AJUSTE PARA QUE NO FALLE AL MOMENTO DE REALIZAR LA EXPORTACION Y EL EMAIL TIENE CARACTER ESPECIAL
							try {
								if(clienteDAO.getDataSet().next()){
									idCliente=clienteDAO.getDataSet().getValue("CLIENT_ID");
									System.out.println("CLIENT_CORREO_ELECTRONICO " + clienteDAO.getDataSet().getValue("CLIENT_CORREO_ELECTRONICO"));
									System.out.println("EMAIL_CLIENTE " + ordenes.getValue("EMAIL_CLIENTE"));
									if (ordenes.getValue("EMAIL_CLIENTE") != null&&ordenes.getValue("EMAIL_CLIENTE").length()>1) {
										//NUEVO Se compara el correo electronico contenido en INFI contra el de SITME y se actualiza en INFI si son diferentes
										if (clienteDAO.getDataSet().getValue("CLIENT_CORREO_ELECTRONICO")==null||clienteDAO.getDataSet().getValue("CLIENT_CORREO_ELECTRONICO").trim().replaceAll("\\s+", "").compareToIgnoreCase(ordenes.getValue("EMAIL_CLIENTE")) != 0) {
											
											Cliente cliente = new Cliente();
											//ITS-2447 Error en la exportacion de ordenes de clientes - Se modifica validacion de empleado ya que el valor puede llegar null
											/*
											 boolean empleado = false;
											 if (clienteDAO.getDataSet().getValue("CLIENT_EMPLEADO").equalsIgnoreCase("true") || clienteDAO.getDataSet().getValue("CLIENT_EMPLEADO").equals("1")) {
												empleado = true;
											}
											cliente.setEmpleado(empleado);*/
											
											if (idCliente != null && !idCliente.equals("")) {
												cliente.setIdCliente(Long.parseLong(idCliente));
												cliente.setCorreoElectronico(ordenes.getValue("EMAIL_CLIENTE"));
												logger.info("Se actualizan los datos del cliente: " + cliente.toString());
												clienteDAO.actualizarCliente(cliente);
											}										
										}
									}								
								}else{
									logger.error("Ha ocurrido un error en el proceso de registro de ID del cliente");
									continue;
								}
							} catch (Exception e) {
								logger.error("Ha ocurrido un error en el proceso de actualizacion del correo del cliente: "+e.toString());
								e.printStackTrace();
							}															
						}
						
						//Cargar valores de la orden
						orden.setIdUnidadInversion(Integer.parseInt(unidadInversion)); 						//UNIINV_ID --> unidad de inversion 
						orden.setIdCliente(Long.parseLong(idCliente)); 										//CLIENT_ID --> id cliente						
						//NM29643 - 16/06/2014: Se insertan de una vez en estatus 'ENVIADA'
						orden.setStatus(StatusOrden.ENVIADA); 											//ORDSTA_ID --> status
//						orden.setStatus(StatusOrden.REGISTRADA); 											//ORDSTA_ID --> status
						orden.setIdSistema(0); 																//SISTEMA_ID --> id sistema
						orden.setIdEmpresa(empresaID); 														//EMPRES_ID --> id empresa 				
						orden.setIdTransaccion(TransaccionNegocio.TOMA_DE_ORDEN); 							//TRANSA_ID --> id transaccion
						orden.setEnviado(false); 															//ENVIADO --> es enviado
						orden.setSegmentoBanco(null); 														//ORDENE_CTE_SEG_BCO --> segmento banco
						orden.setSegmentoSegmento(null); 													//ORDENE_CTE_SEG_SEG --> segmento segmento
						orden.setSegmentoSubSegmento(null); 												//ORDENE_CTE_SEG_SUB --> segmento subsegmento
						orden.setSegmentoInfi(null); 		
						//ORDENE_CTE_SEG_INFI --> segmento infi												
						//TTS-491_NM26659_24/02/2015 Inclusion hora:minuto:segundo en ORDENE_PED_FE_ORDEN
						orden.setFechaOrden(Utilitario.StringToDate(ordenes.getValue("FECHA_REGISTRO")+" "+ordenes.getValue("HORA_REGISTRO"), ConstantesGenerales.FORMATO_FECHA_HORA_BD3));						
						//orden.setFechaOrden(Utilitario.StringToDate(ordenes.getValue("FECHA_MODIFICACION"), ConstantesGenerales.FORMATO_FECHA3)); //ORDENE_PED_FE_ORDEN --> fecha orden
						orden.setMonto(Double.parseDouble(ordenes.getValue("MONTO_SOLICITADO"))); 			//ORDENE_PED_MONTO --> monto solicitado 
						orden.setMontoPendiente(0); 														//ORDENE_PED_TOTAL_PEND --> monto pendiente
						orden.setMontoTotal(Double.parseDouble(ordenes.getValue("VALOR_NOMINAL"))+Double.parseDouble(ordenes.getValue("MONTO_COMISION"))); //ORDENE_PED_TOTAL
						orden.setIdBloter(blotterId); 														//BLOTER_ID --> bloter
						orden.setTipoCuenta(null); 															//ORDENE_PED_CTA_BSTIP --> tipo de cuenta
						orden.setCuentaCliente(ordenes.getValue("CUENTA_BSF_O")); 							//CTECTA_NUMERO --> cuenta cliente 						
						orden.setPrecioCompra(100); 														//ORDENE_PED_PRECIO -- > precio compra
						orden.setRendimiento(0); 															//ORDENE_PED_RENDIMIENTO --> rendimiento				
						orden.setCarteraPropia(false); 														//ORDENE_PED_IN_BDV-->cartera propia
						
						//Confugiracion del tipo de menoda de negociacion acorde con lo que se registra en la pagina de clavenet
						
						 
						 if(idMoneda!=null){
							 orden.setIdMoneda(idMoneda); //MONEDA_ID-->moneda	 
						 } else {							 
							System.out.println("Error en la SOLICITUD: " + ordenes.getValue("ID_ORDEN") +" El tipo de cuenta MONEDA se ha registrado con un valor NULL ");
							logger.error("Error en la SOLICITUD: " + ordenes.getValue("ID_ORDEN") +" El tipo de cuenta MONEDA se ha registrado con un valor NULL ");
							continue;
						 }
							 							
						//Validacion que el valor de cuenta Abono este entre los valores permitido (1 y 2)
						if(tipoCuentaAbono!=null && (!tipoCuentaAbono.equals(""))){
							if(tipoCuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO) || tipoCuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_NACIONAL) || tipoCuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_NACIONAL_BS)){
								orden.setTipoCuentaAbono(Integer.parseInt(tipoCuentaAbono)); 				//CTA_ABONO ---> Indicador de cuenta en la que se realizara el abono: 1)Cuenta en el exterior 2)Cuenta nacional en dolares	3)Cuenta nacional en bolivares
							}else {
							System.out.println("Error en la SOLICITUD: " + ordenes.getValue("ID_ORDEN") +" El tipo de cuenta abono se ha registrado con un valor INCORRECTO:  Valor Configurado  " + tipoCuentaAbono);
							logger.error("Error en la SOLICITUD: " + ordenes.getValue("ID_ORDEN") +" El tipo de cuenta abono se ha registrado con un valor INCORRECTO: Valor Configurado  " + tipoCuentaAbono);
							continue;	
							}								
						} else {
							System.out.println("Error en la SOLICITUD: " + ordenes.getValue("ID_ORDEN") +" El tipo de cuenta abono se ha registrado con un valor NULL ");
							logger.error("Error en la SOLICITUD: " + ordenes.getValue("ID_ORDEN") +" El tipo de cuenta abono se ha registrado con un valor NULL ");
							continue;
						}
						
						
						orden.setPrecioRecompra(0); 														//ORDENE_PED_RCP_PRECIO-->precio recompra
						orden.setMontoAdjudicado(0); 														//ORDENE_ADJ_MONTO --> monto adjudicado 				
						orden.setNombreUsuario(getUserName()); 												//ORDENE_USR_NOMBRE --> nombre ususario
						orden.setCentroContable(null); 														//ORDENE_USR_CEN_CONTABLE --> centro contable
						orden.setSucursal(null); 															//ORDENE_USR_SUCURSAL --> sucursal
						orden.setTerminal(null); 															//ORDENE_USR_TERMINAL --> terminal
						orden.setVehiculoTomador(vehiculo_general); 										//ORDENE_VEH_TOM --> vehiculo tomador //ESTAN llegando nulo
						orden.setVehiculoColocador(vehiculo_general); 										//ORDENE_VEH_COL --> vehiculo colocador
						orden.setVehiculoRecompra(vehiculo_general); 										//ORDENE_VEH_REC --> vehiculo recompra
						orden.setMontoComisionOrden(new BigDecimal(ordenes.getValue("MONTO_COMISION"))); 	//ORDENE_PED_COMISIONES						
						orden.setFinanciada(false); 														//ORDENE_FINANCIADO-->financiada				
						//NM29643 - infi_TTS_466
//						orden.setIdEjecucion(archivo.getIdEjecucion()); 									//EJECUCION_ID
						orden.setIdEjecucion(archivo.getIdEjecucionRelacion()); 							//EJECUCION_ID
						orden.setGananciaRed(generico); 													//ORDENE_GANANCIA_RED
						orden.setComisionOficina(generico); 												//ORDENE_COMISION_OFICINA				
						orden.setComisionOperacion(new BigDecimal(ordenes.getValue("PORC_COMISION"))); 		//ORDENE_COMISION_OPERACION		
						//SICAD II NM25287 - Registro de tasa propuesta						
						orden.setTasaPool(new BigDecimal(ordenes.getValue("TASA_CAMBIO"))); 				//ORDENE_TASA_POOL --> TasaPool
						//tasa_pool = new BigDecimal(uiDAO.getDataSet().getValue("UNDINV_TASA_POOL"));
						//orden.setTasaCambio(Double.parseDouble(ordenes.getValue("VALOR_NOMINAL"))/Double.parseDouble(ordenes.getValue("MONTO_SOLICITADO"))); //ORDENE_TASA_CAMBIO-->tasa de cambio						
						orden.setConceptoId(ordenes.getValue("ID_CONCEPTO")); 								//CONCEPTO_ID
						orden.setObservaciones(null); 														//ORDENE_OBSERVACION-->observaciones
						//******************************* comentado por Victor Goncalves (cambio 1)********************************************************************
						//orden.setTipoProducto(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL); 					//TIPO_PRODUCTO_ID --> tipo producto
						//Modificacion en Requerimiento TTS-443 NM26659_10/04/2014
						orden.setTipoProducto(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL); 					//TIPO_PRODUCTO_ID --> tipo producto
						orden.setContraparte(ordenes.getValue("ID_ORDEN")); 			
						//ID_ORDEN DE LA TABLA SOLICITUDES_SITME (NRO DE TICKET CLAVENET)
						//CONTRAPARTE --> contraparte EN NULL 
						//ORDENE_PED_FE_VALOR --> fecha valor
						//ORDENE_PED_INT_CAIDOS es igual a orden.setMontoPendiente(0);
						//ORDENE_PED_CTA_BSNRO --> cuenta cliente esta en null				
						//ORDENE_EJEC_ID en null				
						//ORDENE_FE_ULT_ACT en null	
						//ORDENE_OPERACION_PEND se pasa en 0 en el metodo			
						//ORDENE_ID_RELACION --> id orden relacionada
						
						//GENERAR OPERACION DE BLOQUEO 
						OrdenOperacion 	operacion = new OrdenOperacion();
						operacion.setAplicaReverso(false);
						operacion.setDescripcionTransaccion("");
						operacion.setIdMoneda(idMoneda);
						operacion.setMontoOperacion(new BigDecimal(ordenes.getValue("VALOR_NOMINAL")).add(new BigDecimal(ordenes.getValue("MONTO_COMISION"))));
						operacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
						operacion.setTipoTransaccionFinanc(TransaccionFinanciera.BLOQUEO);
						operacion.setFechaAplicar(new Date());
						operacion.setFechaProcesada(new Date());
						operacion.setTasa(BigDecimal.ZERO);		
						operacion.setNombreOperacion("");
						operacion.setNumeroRetencion(ordenes.getValue("NUM_BLOQUEO")!=null?ordenes.getValue("NUM_BLOQUEO").trim():"");
						operacion.setNumeroCuenta(ordenes.getValue("CUENTA_BSF_O"));						
						operacion.setCodigoOperacion(codigoOperacionBloqueo);
						operacion.setNombreOperacion("Operacion de Bloqueo");
						operacion.setIdTransaccionFinanciera(Integer.toString(instrumentoFinacncId));
													
						orden.agregarOperacion(operacion);
						
						//NM25287 TTS-441(SICAD II) Inclusion de campos ORIGEN_FONDOS y DESTINO FONDOS 21/03/2014	
						OrdenDataExt ordenDataExt = null;
						if (ordenes.getValue("ORIGEN_FONDOS")!=null) {	
							ordenDataExt = new OrdenDataExt();
							ordenDataExt.setIdData(DataExtendida.ORIGEN_FONDOS);
							ordenDataExt.setValor(ordenes.getValue("ORIGEN_FONDOS"));
							orden.agregarOrdenDataExt(ordenDataExt);
						}	
						if (ordenes.getValue("DESTINO_FONDOS")!=null) {
							ordenDataExt = new OrdenDataExt();
							ordenDataExt.setIdData(DataExtendida.DESTINO_FONDOS);
							ordenDataExt.setValor(ordenes.getValue("DESTINO_FONDOS"));
							orden.agregarOrdenDataExt(ordenDataExt);
						}	
							
							
						Date fechaActual = new Date();
						String fechaActualString = Utilitario.DateToString(fechaActual, ConstantesGenerales.FORMATO_FECHA2);
						String horaActualString = Utilitario.DateToString(fechaActual, ConstantesGenerales.FORMATO_FECHA_HORA_JAVA);	
						horaActualString=horaActualString.substring(11, horaActualString.length());
							
							//NM29643 - 16/06/2014 Se quita actualizacion de estatus de la orden 204 a 'ENVIADA'
							CONTROL1= guardarOrdenSitme(orden, false, false, Long.parseLong(ordenes.getValue("ID_ORDEN")),fechaActualString,horaActualString);
							//CONTROL1= guardarOrdenSitme(orden, false, true, Long.parseLong(ordenes.getValue("ID_ORDEN")),fechaActualString,horaActualString);
							//UINombre="";
						}else{	
													
							SolicitudClavenet solicitudClavenet =null;
							solicitudClavenet = new SolicitudClavenet();				       						       		
				       		solicitudClavenet.setIdOrden(Long.parseLong(ordenes.getValue("ID_ORDEN")));				       		
				       		solicitudClavenet.setFechaActualTramite(true);
				       		if(ordenes.getValue("NUMERO_ORDEN_INFI")!=null){
				       			orden.setIdOrden(Long.parseLong(ordenes.getValue("NUMERO_ORDEN_INFI")));
				       		}
							
							ArrayList <String> arregloConsultas = new ArrayList <String>();
							arregloConsultas.add(ordenDAO.actualizarSolicitudClavenet(solicitudClavenet,ConstantesGenerales.ESTATUS_ORDEN_RECIBIDA));
							ordenDAO.ejecutarStatementsBatchBool(arregloConsultas);
							
						}
						
						logger.info("obtenerOrdenesExportarExcelSITME->Registro de orden: "+CONTROL1+", estatusSolicitud: "+estatusSolicitud);
						if (CONTROL1.equalsIgnoreCase("OK")) {							
							escribir(UINombre + ";");
							escribir(String.valueOf(orden.getIdOrden()) + ";");
							escribir(tipoPer + Utilitario.rellenarCaracteres(Long.valueOf(cedRif).toString(), '0', 9, false) + ";");
							escribir(ordenes.getValue("NOMBRE_CLIENTE") + ";");
							escribir(String.valueOf(Double.parseDouble(ordenes.getValue("MONTO_SOLICITADO"))) + ";");
							escribir(String.valueOf(Double.parseDouble(ordenes.getValue("MONTO_COMISION"))) + ";");
							escribir(String.valueOf(Double.parseDouble(ordenes.getValue("TASA_CAMBIO"))) + ";");
							escribir(String.valueOf(Double.parseDouble(ordenes.getValue("PORC_COMISION"))) + ";");
							escribir(conceptos.get(ordenes.getValue("ID_CONCEPTO")) + ";");
							escribir(String.valueOf(Long.valueOf(ordenes.getValue("ID_ORDEN"))) + ";");
							escribir(String.valueOf(ordenes.getValue("CUENTA_BSF_O")) + ";");
							escribir(String.valueOf(ordenes.getValue("CTA_NUMERO")) + ";");
							escribir(String.valueOf(ordenes.getValue("HORA_REGISTRO")) + ";");
							escribir("\r\n");

							registroProcesado++;
							
						} else {
						
							System.out.println("Error en el ticket: " + ordenes.getValue("ID_ORDEN"));
							logger.error("Error en el ticket: " + ordenes.getValue("ID_ORDEN"));
							continue;
						}//if(CONTROL1.equalsIgnoreCase("OK"))
					} catch (Exception e) {
						
						System.out.println("Error en el ticket: " + ordenes.getValue("ID_ORDEN")+", " + e.getMessage());
						logger.error("Error en el ticket: " + ordenes.getValue("ID_ORDEN")+", " + e.getMessage());					
					}
				}//fin while
			}			
			if(registroProcesado>0){
				
				actualizarOrdenes(true,false);
				
			}

		}catch (Exception ex) {
			System.out.println("FALLA GENERAL: " + ex.getMessage());	
			logger.error("Error en el proceso de generación archivo batch para adjudicación SITME. " + ex.getMessage(),ex);
			
		} 	
	}

	protected void crearCabecera(String camposAdicionales) throws Exception {
		
		escribir("UNIDAD INVERSION;NUMERO DE ORDEN;CEDULA/RIF;NOMBRE;MONTO SOLICITADO EN DOLARES;MONTO COMISION "+nombreMonedaBs+";TASA CAMBIO PROPUESTA;TASA COMISION;TIPO DE SOLICITUD;NUMERO DE TICKET;CUENTA ORIGEN;CUENTA DESTINO;HORA REGISTRO"+camposAdicionales);
		escribir("\r\n");
	}
		
	/**
	 * Actualiza las ordenes con la ejecución del proceso
	 * @param insertarEn803 verdadero para indicar que se desea crear el registro en la tabla 803
	 * @throws Exception en caso de error
	 */

	private void actualizarOrdenes(boolean insertarEn803, boolean bandera) throws Exception{
		long inicioActOrdenes = System.currentTimeMillis();
		Connection cn = null;
		Statement st = null;

		try{
			cn = _dso.getConnection();
			st = cn.createStatement();
			String[] consulta =control.insertarArchivoTransferenciaPrivado(archivo,insertarEn803,bandera);
			//Almacena las consultas finales
			for(int cont=0;cont<consulta.length; cont++){
				st.addBatch((String) consulta[cont]);
			}
			if (consulta.length > 0){
				st.executeBatch();
				cn.commit();
			}
		    archivo.reiniciarDetalle();
		    if (logger.isDebugEnabled()){
		    	logger.debug("Tiempo actualizando ordenes " + (System.currentTimeMillis() - inicioActOrdenes) + " miliseg");
		    }
		    
		}catch (Exception e) {

			logger.error("Error en la actualización de ordenes",e);
			cn.rollback();

		}finally{
			if (st!=null)st.close();
			if (cn!=null)cn.close();
		}
	}
	
	
	
	//NUEVO VICTOR GONCALVES se usa
	/**
	 * insertar en la tabla 204, en data ext y modifica la orden en opics 
	 * @param orden  insertarEn803 bandera id
	 * @throws Exception en caso de error
	 */
	// Modificado por CT20315 03-02-2015.  Solución definitiva incidencia ITS-2345, para evitar que se dupliquen las ordenes en el proceso de Exportación 
	@SuppressWarnings("finally")
	private String guardarOrdenSitme(Orden orden,boolean insertarEn803, boolean bandera,long id, String fechaRegistro, String horaRegistro) throws Exception{
		
		OrdenDAO  		ordenDAO 			= new OrdenDAO(_dso);	
		long inicioActOrdenes = System.currentTimeMillis();
		//Connection cn = null;
		//Statement st = null;
		//CT20315 - 03/02/2015: el ArrayList almacenará las consultas
		ArrayList <String> arregloConsultas = new ArrayList <String>();
		boolean result = false;
		String control_var = null;
		SolicitudClavenet solicitudClavenet =null;
		try{
			
			//cn = _dso.getConnection();
			//st = cn.createStatement();
			
			control_orden = new OrdenDAO(_dso);			
			String[] consulta =control_orden.insertarSolicitudSITME(orden);
			//insumos
			Detalle detalle = new Detalle();
       		detalle.setIdOrden(orden.getIdOrden());  		
       		archivo.agregarDetalle(detalle);
       		solicitudClavenet = new SolicitudClavenet();
       		//NM25287 SICAD II. Se solicitó NO actualizar el estatus en SOLICITUDES SITME en la exportacion de ordenes. 27/03/2014
       		//solicitudClavenet.setEstatus(StatusOrden.EN_TRAMITE); 
       		solicitudClavenet.setIdOrden(id);
       		solicitudClavenet.setIdOrdenInfi(orden.getIdOrden());
       		solicitudClavenet.setFechaActualTramite(true);
       		//solicitudClavenet.setFechaRegistro(fechaRegistro);
       		//solicitudClavenet.setHoraRegistro(horaRegistro);
       		
       		String[] consulta2 = control.insertarArchivoTransferenciaPrivado(archivo,insertarEn803,bandera);
       		
			String consulta3 = ordenDAO.actualizarSolicitudClavenet(solicitudClavenet,ConstantesGenerales.ESTATUS_ORDEN_RECIBIDA);
			
			//Almacena las consultas finales
			/*for(int cont=0;cont<consulta.length; cont++){
				st.addBatch((String) consulta[cont]);
			}
		
			for(int cont=0;cont<consulta2.length; cont++){
				st.addBatch((String) consulta2[cont]);
			}*/
		
			//st.addBatch(consulta3);
			
			//NM29643 - 16-06-2014 Se quita actualizacion del estatus de la orden INFI a 'ENVIADA'
       		if(bandera){
				if (consulta.length > 0 && consulta2.length > 0 && consulta3.length() > 0){
					//st.executeBatch();
					//cn.commit();
					//CT20315 - 04/02/2015 - Se almacenan las consultas finales
					arregloConsultas.addAll(Arrays.asList(consulta));
					arregloConsultas.addAll(Arrays.asList(consulta2));
					arregloConsultas.add(consulta3);
					result = ordenDAO.ejecutarStatementsBatchBool(arregloConsultas);
					logger.debug("Resultado proceso batch de consulta a BD: " + result);
				}
       		}else{
       			if (consulta.length > 0 && consulta3.length() > 0){
					//st.executeBatch();
					//cn.commit();
       				//CT20315 - 04/02/2015 - Se almacenan las consultas finales
       				arregloConsultas.addAll(Arrays.asList(consulta));
       				arregloConsultas.add(consulta3);
					result = ordenDAO.ejecutarStatementsBatchBool(arregloConsultas);
					logger.debug("Resultado proceso batch de consulta a BD: " + result);
       				
				}
       		}
		
		    if (logger.isDebugEnabled()){
		    	logger.debug("Tiempo insertando orden SITME " + (System.currentTimeMillis() - inicioActOrdenes) + " miliseg");
		    }
		    
		    archivo.reiniciarDetalle();
		    
		    control_var = "OK";
		
		}catch (Exception e) {
			//cn.rollback();
			archivo.reiniciarDetalle();
			logger.error("Error en el registro de la orden SITME metodo guardarOrdenSitme ",e);
			System.out.println("ERROR en guardarOrdenSitme "+e.getMessage());
			control_var = "NOT_OK";
			
		}finally{
			//if (st!=null)st.close();
			//if (cn!=null)cn.close();
			return control_var;
		}
	}

	public HashMap<String, String> obtenerConceptos() throws Exception{
		HashMap<String, String> conceptos 	= null;
		ConceptosDAO  	conceptosDAO 		= new ConceptosDAO(_dso);
		conceptosDAO.listar();
		if (conceptosDAO.getDataSet()!=null&&conceptosDAO.getDataSet().count()>0){
			conceptos = new HashMap<String, String>();
			while (conceptosDAO.getDataSet().next()) {
				conceptos.put(conceptosDAO.getDataSet().getValue("CODIGO_ID"), conceptosDAO.getDataSet().getValue("CONCEPTO"));
				System.out.println(conceptosDAO.getDataSet().getValue("CODIGO_ID")+"-"+conceptosDAO.getDataSet().getValue("CONCEPTO"));
			}
		}	
		return conceptos;
	}
	
	public com.bdv.infi.data.TransaccionFija obtenerCodigosOperacion(String vehiculoID, String unidadInversionId)throws Exception{
		com.bdv.infi.data.TransaccionFija transaccionFija = new com.bdv.infi.data.TransaccionFija();
		try {
			VehiculoDAO vehiculoDAO = new VehiculoDAO(_dso);
			TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(_dso);
			//OBTENER VEHICULO				
			vehiculoDAO.obtenerVehiculoBDV();	
			if(vehiculoDAO.getDataSet().count()>0){
				vehiculoDAO.getDataSet().first();
				vehiculoDAO.getDataSet().next();
				vehiculoID=vehiculoDAO.getDataSet().getValue("VEHICU_ID");
			}
				
			//Obtener codigo de operaciones asociados a producto de manejo mixto Cambio TTS_443 NM26659_09/04/14 
			transaccionFija=transaccionFijaDAO.obtenerCodigosOperacionesTRNFVehiculo(vehiculoID,unidadInversionId,TransaccionFija.GENERAL_CAPITAL_CON_IDB_MANEJO_MIXTO);//COMISION_DEB
						
			System.out.println("CODIGO OPERACION BLOQUEO: "+transaccionFija.getCodigoOperacionCteBlo());
			
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en la consulta de códigos de operación");
		}
		return transaccionFija;
	}
		
	/**
	 * Registra el inicio de un proceso en la tabla 807
	 * @return
	 * @throws Exception
	 */
	protected boolean comenzarProceso() throws Exception {
		procesoDAO.listarPorTransaccionActiva(proceso.getTransaId());
		if (procesoDAO.getDataSet().count() > 0) {
			logger.info("No se pudo iniciar el proceso "+proceso.getTransaId()+" puesto que ya existe un proceso de este tipo en ejecución");
			proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+" | Proceso: "+proceso.getTransaId()+" ya está en ejecución.");
			setMensajeError(getMensajeError() + "Disculpe, no se pudo iniciar el proceso de generaci&oacute;n de archivo puesto que ya existe un proceso de este tipo en ejecuci&oacute;n. Debe esperar a que el mismo finalice.<br/>");
			return false;
		}
		try{
			//Logger.debug(this, "-----NO ENCUENTRA PROCESO EN EJECUCION");
			ArrayList<String> querysEjecutar = new ArrayList<String>();
			//Se crea el query de insercion del proceso
			querysEjecutar.add(procesoDAO.insertar(proceso));
			boolean insertado = procesoDAO.ejecutarStatementsBatchBool(querysEjecutar);
			if(insertado){
				logger.info("Comenzó proceso: " + proceso.getTransaId());
			}else{
				logger.error("Error al crear el proceso de tipo: " + proceso.getTransaId());
				proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+" | Error al crear el proceso de tipo: "+proceso.getTransaId());
				setMensajeError(getMensajeError() + "Ocurri&oacute; un error al intentar iniciar el proceso de generaci&oacute;n de archivo<br/>");
				return false;
			}
		}catch(Exception e){
			logger.error("Error al crear el proceso de tipo: " + proceso.getTransaId());
			proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError()+" | Error al crear el proceso de tipo: "+proceso.getTransaId());
			setMensajeError(getMensajeError() + "Ocurri&oacute; un error al intentar iniciar el proceso de generaci&oacute;n de archivo<br/>");
			return false;
		}
		return true;
	}
	
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	/**
	 * Registra el fin de un proceso en la tabla 807
	 */
	protected void terminarProceso() {
		try {
			if (proceso != null) {
				proceso.setFechaFin(new Date());
				if (proceso.getDescripcionError() == null) {
					proceso.setDescripcionError("");
				}
				proceso.setDescripcionError(proceso.getDescripcionError()==null?"":proceso.getDescripcionError());
				db.exec(_dso, procesoDAO.modificar(proceso));
			}
			if (procesoDAO != null) {
				procesoDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error("Excepcion ocurrida al cerrar el proceso "+proceso.getTransaId()+": "+e.getMessage());
		} finally {
			if (proceso != null) {
				if(proceso.getFechaInicio()!=null && proceso.getFechaFin()!=null){
					final long duracion = proceso.getFechaFin().getTime() - proceso.getFechaInicio().getTime();
					logger.info("Termino proceso: " + proceso.getTransaId() + ", duracion: " + (duracion / 1000) + " secs.");
				}
			}
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
		
		logger.info("Registrando auditoria del proceso "+proceso.getTransaId()+"...");
			try {
				///********REGISTRAR LA AUDITORIA DE LA PETICIÓN DE LLAMADA AL REPROCESO DEL CIERRE DEL SISTEMA****///
				//Configuracion del objeto para el proceso de auditoria
				Auditoria auditoria= new Auditoria();
				auditoria.setDireccionIp(_req.getRemoteAddr());			
				auditoria.setFechaAuditoria(Utilitario.DateToString(new Date(),ConstantesGenerales.FORMATO_FECHA));
				auditoria.setUsuario(getUserName());			
				auditoria.setPeticion(proceso.getTransaId());
				auditoria.setDetalle(proceso.getDescripcionError()==null?"":proceso.getDescripcionError());	
				
				querys.add(auditoriaDAO.insertRegistroAuditoria(auditoria));
				auditoriaDAO.ejecutarStatementsBatch(querys);				
				
			}catch(Exception ex){
				try {
					transaccion.rollback();
				} catch (Exception e) {
					logger.error("Ha ocurrido un error registrando la auditoría del proceso "+proceso.getTransaId()+": " + ex.getMessage());
				}
			}finally{	
				try {
					if(transaccion.getConnection()!=null){
						transaccion.getConnection().close();
					}
				} catch (Exception e) {
					logger.error("Ha ocurrido un error registrando la auditoría del proceso "+proceso.getTransaId()+": " + e.getMessage());
				}				
			}	
	}

}//Fin Clase
	
	
