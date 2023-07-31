package models.intercambio.transferencia.generar_archivo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.sql.DataSource;

import megasoft.DataSet;
import models.exportable.ExportableOutputStream;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.ConceptosDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.PEM2010;
import com.bdv.infi.webservices.beans.PEVB;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

/** Clase que exporta a excel la ordenes 
 * que deben ser enviadas al Banco de Venezuela
 * @author jvillegas, modif: nm25287
 */
public class ExportarOrdenes extends ExportableOutputStream{
	
	private Logger logger = Logger.getLogger(ExportarOrdenes.class);
	DataSet camposDinamicosDataSet = new DataSet();
	DataSet titulos = new DataSet();
	boolean tieneCamposDinamicos = true;
	Archivo archivo	= new Archivo();
	ControlArchivoDAO control = null;
	OrdenDAO control_orden = null;	
	String nombreMonedaBs = "";
	
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		this.aplicarFormato = false;
		CamposDinamicos camposDinamicos	= new CamposDinamicos(_dso);			
		control	= new ControlArchivoDAO(_dso);	
		String ejecucion_id			=dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		String unidad_inversion 	= null;
		String vehiculo 			= null;
		String nombreArchivo = "";
		int inRecepcion 			= 0;	
		ArrayList<CampoDinamico> nombreCamposD		= null;
		nombreMonedaBs = (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA);
		
			
		if (_req.getParameter("undinv_id")!=null&&!_req.getParameter("undinv_id").equals("")){
			unidad_inversion= _req.getParameter("undinv_id");
		}else{
			unidad_inversion= getSessionObject("unidadInversion").toString();
		}
		if (_req.getParameter("ordene_veh_col")!=null&&!_req.getParameter("ordene_veh_col").equals("")){
			vehiculo= _req.getParameter("ordene_veh_col");
		}
			
		archivo.setUnidadInv(Integer.parseInt(unidad_inversion));
	
		//NUEVO VICTOR GONCALVES se usa
        UnidadInversionDAO unidadDAO_SITME = new UnidadInversionDAO(_dso);
		
		String var = unidadDAO_SITME.getInstrumentoFinancieroPorUI(Integer.parseInt(unidad_inversion)); 
		
		if(var.equalsIgnoreCase(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P)){
			
			//Creamos el nombre del archivo dinamicamente
			//control.nombreArchivo(archivo);//retorna el numero del veiculo colocador
			nombreArchivo = obtenerNombreArchivo("intercambio");
			archivo.setNombreArchivo(nombreArchivo);
			archivo.setIdEjecucion(Long.parseLong(ejecucion_id));
			archivo.setInRecepcion(inRecepcion);
			archivo.setUsuario(getUserName());

			registrarInicio(nombreArchivo);
			
			//OBTENER LOS DATOS A EXPORTAR A EXCEL
			obtenerOrdenesExportarExcelSITME(unidad_inversion,_dso);
			
		}else{
			archivo.setVehiculoId(vehiculo);
			//CONSULTAR CAMPOS DINAMICOS DE LA UNIDAD DE INVERSION		
			nombreCamposD=camposDinamicos.listarCamposDinamicosUnidadInversion(unidad_inversion,_dso);
			
			//Creamos el nombre del archivo dinamicamente
			control.nombreArchivo(archivo);//retorna el numero del veiculo colocador
			nombreArchivo = obtenerNombreArchivo("intercambio");
			archivo.setNombreArchivo(nombreArchivo);
			archivo.setIdEjecucion(Long.parseLong(ejecucion_id));
			archivo.setInRecepcion(inRecepcion);
			archivo.setUsuario(getUserName());

			registrarInicio(nombreArchivo);
			
			//OBTENER LOS DATOS A EXPORTAR A EXCEL
			obtenerOrdenesExportarExcel(unidad_inversion,_dso,nombreCamposD);
		}
		//nuevo

		_req.getSession().removeAttribute("unidadInversion");		
		
		registrarFin();
		obtenerSalida();
	}//fin execute
		
	private void obtenerOrdenesExportarExcel(String unidadInversion, DataSource _dso,ArrayList<CampoDinamico> camposD) throws Exception{
		ControlArchivoDAO control			= new ControlArchivoDAO(_dso);	
		CamposDinamicos camposDinamicos		= new CamposDinamicos(_dso);
		ArrayList<CampoDinamico> listaCampos= null;			
        Transaccion transaccion 			= new Transaccion(this._dso);
        Statement statement 				= null;
        ResultSet ordenes 					= null;
        StringBuilder sb = new StringBuilder(); //Contenido
       
		logger.info("ExportarOrdenes.obtenerOrdenesExportarExcel->inicio. Unidad Inversion: "+unidadInversion);
        try{
	        transaccion.begin();
			statement = transaccion.getConnection().createStatement();			
			ordenes = statement.executeQuery(control.listarDetallesString(unidadInversion));

			crearCabecera();
			
			while(ordenes.next()){
				registroProcesado++;
				//AGREGAR CAMPOS DINAMICOS-ordenados segun los titulos de la Unidad de Inversion
				if (tieneCamposDinamicos){
					listaCampos=camposDinamicos.listarCamposDinamicosOrdenes(ordenes.getString("ORDENE_ID"),_dso,camposD);
				}
				
				if (registroProcesado==1){
					adjuntarCabeceraCamposDinamicos(sb,listaCampos);
				}				
				
				escribir(ordenes.getString("INSFIN_DESCRIPCION")+";");
				escribir(ordenes.getString("UNDINV_EMISION")+";");
				escribir((ordenes.getString("UNDINV_SERIE")==null?"":ordenes.getString("UNDINV_SERIE"))+";");
				escribir(ordenes.getString("ORDENE_ID")+";");
				escribir(ordenes.getString("tipper_id").concat(Utilitario.rellenarCaracteres(String.valueOf(ordenes.getString("CLIENT_CEDRIF")), '0', 9, false))+";");
				escribir(ordenes.getString("CLIENT_NOMBRE")+";");
				escribir(getString("'"+ordenes.getString("ctecta_numero"))+";");				
				escribir(ordenes.getString("BLOTER_DESCRIPCION")+";");
				escribir(ordenes.getString("ORDENE_FINANCIADO")+";");
				escribir(ordenes.getDouble("ORDENE_PED_TOTAL_PEND"));
				escribir(";");
				escribir(ordenes.getDouble("ORDENE_PED_PRECIO"));
				escribir(";");
				escribir(ordenes.getDouble("ORDENE_PED_MONTO"));
				escribir(";");
				escribir(ordenes.getDouble("VALOR_NOMINAL_BOLIVARES"));
				escribir(";");				
				escribir(ordenes.getDouble("ORDENE_PED_MONTO") * (ordenes.getDouble("UNDINV_TASA_CAMBIO") * ordenes.getDouble("ORDENE_PED_PRECIO")) / 100 );
				escribir(";");
				escribir(ordenes.getDouble("ordene_ped_int_caidos"));
				escribir(";");
				
				escribir(ordenes.getDouble("ordene_ped_comisiones"));
				escribir(";");
				escribir(ordenes.getDouble("ordene_ped_total"));
				escribir(";");
				escribir(ordenes.getString("ORDENE_PED_FE_ORDEN")+";");
				escribir(ordenes.getString("ORDENE_PED_FE_VALOR")+";");
				escribir(ordenes.getString("ORDENE_USR_SUCURSAL")+";");
				escribir(ordenes.getString("ORDENE_USR_NOMBRE")+";");
				escribir(ordenes.getString("CLIENT_DIRECCION")+";");
				escribir(ordenes.getString("CLIENT_TELEFONO")+";");
				escribir(getString(ordenes.getString("codigo_id"))+";");
				escribir(getString(ordenes.getString("sector_id"))+";");				
				escribir(getString(ordenes.getString("concepto_id"))+";");								
				adjuntarCamposDinamicos(sb,listaCampos);

				Detalle detalle = new Detalle();
	       		detalle.setIdOrden(ordenes.getLong("ORDENE_ID"));   		
	       		archivo.agregarDetalle(detalle);
				verificarRegistros(sb);
				escribir("\r\n");
			}//fin while
			
			if (registroProcesado < 5000){
				actualizarOrdenes(true);	
			}else{
				actualizarOrdenes(false);
			}			
		}catch (Exception ex) {
			logger.error("Error en el proceso de generación archivo batch para adjudicación tipo subasta. " + ex.getMessage(),ex);
			transaccion.rollback();
		} finally {			
			try {
				if (ordenes != null){
					ordenes.close();
				}
				if (statement != null){
					statement.close();
				}
				transaccion.closeConnection();
			} catch (Exception e) {
				logger.error("Error en el proceso de exportación",e);				
			}
		}			
	}
	
	//NUEVO VICTOR GONCALVES
	private void obtenerOrdenesExportarExcelSITME(String unidadInversion, DataSource _dso) throws Exception{
		OrdenDAO  				ordenDAO 		= new OrdenDAO(_dso);			
		BlotterDAO  			blotterDAO 		= new BlotterDAO(_dso);
		VehiculoDAO  			vehiculoDAO 	= new VehiculoDAO(_dso);
		UnidadInversionDAO		uiDAO		= new UnidadInversionDAO(_dso);
		HashMap<String, String> conceptos 		= null;
        DataSet 				ordenes 		= null;
        String 					fecha 			= null;
        String 					tipoPer 		= "";
        String 					cedRif 			= "";
        String 					empresaID 		= "";;
        String 					blotterId 		= "";
        String 					vehiculo_general="";
        BigDecimal 				tasa_pool		= null;
        BigDecimal 				generico  		= new BigDecimal(0);
        String 					idCliente 		= null;
        Orden 					orden 			= null;
       
		logger.info("ExportarOrdenes.obtenerOrdenesExportarExcelSITME->inicio. Unidad Inversion: "+unidadInversion);
        try{
        	    
	        fecha = getSessionObject("fechaProductoCLaveNet").toString();
			
			//Obtener ordenes SITME
			ordenDAO.listarSolicitudesSITME(fecha);
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
			uiDAO.obtenerDatosUIporId("EMPRES_ID, UNDINV_TASA_POOL",unidadInversion);
			if (uiDAO.getDataSet()!=null&& uiDAO.getDataSet().count()>0) {
				uiDAO.getDataSet().next();
				empresaID = uiDAO.getDataSet().getValue("EMPRES_ID");				
				tasa_pool = new BigDecimal(uiDAO.getDataSet().getValue("UNDINV_TASA_POOL"));
			}
			logger.info("obtenerOrdenesExportarExcelSITME->empresaID: "+empresaID);
			logger.info("obtenerOrdenesExportarExcelSITME->tasa_pool: "+tasa_pool);
			//Obtener conceptos
			conceptos = obtenerConceptos();
			
			//Crear cabecera del archivo
			crearCabeceraSITME();
			
			if (ordenes!=null && ordenes.count()>0) {
				while (ordenes.next()) {
					logger.info("obtenerOrdenesExportarExcelSITME->id_ticket: "+ordenes.getValue("ID_ORDEN"));
					try {
						
						//Busqueda del tipo de monedda de negociacion que se especifica en desde el canal clavenet			
						idMoneda=ordenes.getValue("cta_abono_moneda");
						
						//Busqueda de cuenta abono: 1) Cuenta en el exterior 2)Cuenta nacional en dolares										
						tipoCuentaAbono=ordenes.getValue("cta_abono");
						
						//Validar si el numero de ticket ya existe en data_ext
						ordenDAO.listarDataExtPorIdTicket(ordenes.getValue("ID_ORDEN"));
						if (ordenDAO.getDataSet()!=null&& ordenDAO.getDataSet().count()>0) {
							logger.info("obtenerOrdenesExportarExcelSITME->La orden ya se encuentra registrada ");
							continue;
						}
						
						tipoPer = ordenes.getValue("CED_RIF_CLIENTE").substring(0, 1);
						cedRif = ordenes.getValue("CED_RIF_CLIENTE").substring(1, ordenes.getValue("CED_RIF_CLIENTE").length() - 1);

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
							if(clienteDAO.getDataSet().next()){
								idCliente=clienteDAO.getDataSet().getValue("CLIENT_ID");	
							}else{
								logger.error("Ha ocurrido un error en el proceso de registro de ID del cliente");
								continue;
							}															
						}
						
						//Cargar valores de la orden
						orden = new Orden();
						orden.setIdUnidadInversion(Integer.parseInt(unidadInversion)); 						//UNIINV_ID --> unidad de inversion 
						orden.setIdCliente(Long.parseLong(idCliente)); 										//CLIENT_ID --> id cliente						
						orden.setStatus(StatusOrden.REGISTRADA); 											//ORDSTA_ID --> status
						orden.setIdSistema(0); 																//SISTEMA_ID --> id sistema
						orden.setIdEmpresa(empresaID); 														//EMPRES_ID --> id empresa 				
						orden.setIdTransaccion(TransaccionNegocio.TOMA_DE_ORDEN); 							//TRANSA_ID --> id transaccion
						orden.setEnviado(false); 															//ENVIADO --> es enviado
						orden.setSegmentoBanco(null); 														//ORDENE_CTE_SEG_BCO --> segmento banco
						orden.setSegmentoSegmento(null); 													//ORDENE_CTE_SEG_SEG --> segmento segmento
						orden.setSegmentoSubSegmento(null); 												//ORDENE_CTE_SEG_SUB --> segmento subsegmento
						orden.setSegmentoInfi(null); 		
						//ORDENE_CTE_SEG_INFI --> segmento infi
						orden.setFechaOrden(Utilitario.StringToDate(ordenes.getValue("FECHA_REGISTRO"), ConstantesGenerales.FORMATO_FECHA3)); //ORDENE_PED_FE_ORDEN --> fecha orden			
						orden.setMonto(Double.parseDouble(ordenes.getValue("MONTO_SOLICITADO"))); 			//ORDENE_PED_MONTO --> monto solicitado 
						orden.setMontoPendiente(0); 														//ORDENE_PED_TOTAL_PEND --> monto pendiente
						orden.setMontoTotal(Double.parseDouble(ordenes.getValue("VALOR_NOMINAL"))); 		//ORDENE_PED_TOTAL
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
							if(tipoCuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO) || tipoCuentaAbono.equals(ConstantesGenerales.ABONO_CUENTA_NACIONAL)){
								orden.setTipoCuentaAbono(Integer.parseInt(tipoCuentaAbono)); 				//CTA_ABONO ---> Indicador de cuenta en la que se realizara el abono: 1)Cuenta en el exterior 2)Cuenta nacional en dolares	
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
						orden.setIdEjecucion(archivo.getIdEjecucion()); 									//EJECUCION_ID
						orden.setTasaPool(tasa_pool); 														//ORDENE_TASA_POOL --> TasaPool
						orden.setGananciaRed(generico); 													//ORDENE_GANANCIA_RED
						orden.setComisionOficina(generico); 												//ORDENE_COMISION_OFICINA				
						orden.setComisionOperacion(new BigDecimal(ordenes.getValue("PORC_COMISION"))); 		//ORDENE_COMISION_OPERACION							
						orden.setTasaCambio(Double.parseDouble(ordenes.getValue("VALOR_NOMINAL"))/Double.parseDouble(ordenes.getValue("MONTO_SOLICITADO"))); //ORDENE_TASA_CAMBIO-->tasa de cambio						
						orden.setConceptoId(ordenes.getValue("ID_CONCEPTO")); 								//CONCEPTO_ID
						orden.setObservaciones(null); 														//ORDENE_OBSERVACION-->observaciones
						orden.setTipoProducto(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME); 					//TIPO_PRODUCTO_ID --> tipo producto
						orden.setContraparte(ordenes.getValue("ID_ORDEN")); 								//ID_ORDEN DE LA TABLA SOLICITUDES_SITME (NRO DE TICKET CLAVENET)
						
						//CONTRAPARTE --> contraparte EN NULL 
						//ORDENE_PED_FE_VALOR --> fecha valor
						//ORDENE_PED_INT_CAIDOS es igual a orden.setMontoPendiente(0);
						//ORDENE_PED_CTA_BSNRO --> cuenta cliente esta en null				
						//ORDENE_EJEC_ID en null				
						//ORDENE_FE_ULT_ACT en null	
						//ORDENE_OPERACION_PEND se pasa en 0 en el metodo			
						//ORDENE_ID_RELACION --> id orden relacionada
										
						String CONTROL1 = guardarOrdenSitme(orden, false, true, Long.parseLong(ordenes.getValue("ID_ORDEN")));
						logger.info("obtenerOrdenesExportarExcelSITME->Registro de orden: "+CONTROL1);
						if (CONTROL1.equalsIgnoreCase("OK")) {

							escribir(String.valueOf(orden.getIdOrden()) + ";");
							escribir(tipoPer + Utilitario.rellenarCaracteres(Long.valueOf(cedRif).toString(), '0', 9, false) + ";");
							escribir(ordenes.getValue("NOMBRE_CLIENTE") + ";");
							escribir(String.valueOf(Double.parseDouble(ordenes.getValue("MONTO_SOLICITADO"))) + ";");
							escribir(String.valueOf(Double.parseDouble(ordenes.getValue("MONTO_COMISION"))) + ";");
							escribir(String.valueOf(Double.parseDouble(ordenes.getValue("PORC_COMISION"))) + ";");
							escribir(conceptos.get(ordenes.getValue("ID_CONCEPTO")) + ";");
							escribir(String.valueOf(Long.valueOf(ordenes.getValue("ID_ORDEN"))) + ";");
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
				actualizarOrdenesSITME(true,false);	
			}

		}catch (Exception ex) {
			System.out.println("FALLA GENERAL: " + ex.getMessage());	
			logger.error("Error en el proceso de generación archivo batch para adjudicación SITME. " + ex.getMessage(),ex);
			
		} 	
	}


	protected void crearCabecera() throws Exception {
		
		escribir("INSTRUMENTO;EMISION;SERIE;ORDEN;CEDULA;NOMBRE;CUENTA;BLOTER;% FINANCIAMIENTO;MONTO FINANCIADO;PRECIO OPERACIÓN;VALOR NOMINAL;VALOR NOMINAL "+nombreMonedaBs+";MONTO INVERTIDO "+nombreMonedaBs+";INTERESES CAIDOS "+nombreMonedaBs+";COMISIONES "+nombreMonedaBs+";MONTO TOTAL DE LA OPERACIÓN "+nombreMonedaBs+";FECHA OPERACION;FECHA VALOR;AGENCIA RECEPTORA;OPERADOR;DIRECCION CLIENTE;TELEFONO CLIENTE;ACTIVIDAD ECONOMICA;SECTOR PRODUCTIVO;CONCEPTO;");
	}
	
	//NUEVO VICTOR GONCALVES SE USA
	protected void crearCabeceraSITME() throws Exception {
		
		escribir("NUMERO DE ORDEN;CEDULA/RIF;NOMBRE;MONTO SOLICITADO EN DOLARES;MONTO COMISION "+nombreMonedaBs+";TASA COMISION;TIPO DE SOLICITUD;NUMERO DE TICKET;");
		escribir("\r\n");
	}
	
	/**
	 * Adjunta los campos cabecera de los valores dinámicos de la orden
	 * @param sb contenido de la consulta
	 * @param listaCampos lista de campos dinámicos encontrados para la orden
	 * @throws Exception 
	 */
	protected void adjuntarCabeceraCamposDinamicos(StringBuilder sb, ArrayList<CampoDinamico> listaCampos) throws Exception{
		if (listaCampos != null){			
			for (Iterator iterator = listaCampos.iterator(); iterator.hasNext();) {
				CampoDinamico campoDinamico = (CampoDinamico) iterator.next();
				escribir(campoDinamico.getCampoNombre()+";");
			}
		}
		escribir("\r\n");
	}
	
	/**
	 * Adjunta los campos dinámicos a la consulta
	 * @param sbCabeceraCD cabecera de campos dinámicos. Sólo es llenada la primera vez que entra a este ciclo
	 * @param sb contenido de la consulta
	 * @param listaCampos lista de campos dinámicos encontrados para la orden
	 * @throws Exception 
	 */
	protected void adjuntarCamposDinamicos(StringBuilder sb, ArrayList<CampoDinamico> listaCampos) throws Exception{
		if (listaCampos != null){
			if (listaCampos.size()>0){
				for (Iterator iterator = listaCampos.iterator(); iterator.hasNext();) {
					CampoDinamico campoDinamico = (CampoDinamico) iterator.next();
					escribir(getString(campoDinamico.getValor())+";");
				}
			}else{
				tieneCamposDinamicos = false;
			}
		}
	}

	/**
	 * Actualiza las ordenes con la ejecución del proceso
	 * @param insertarEn803 verdadero para indicar que se desea crear el registro en la tabla 803
	 * @throws Exception en caso de error
	 */
	private void actualizarOrdenes(boolean insertarEn803) throws Exception{
		long inicioActOrdenes = System.currentTimeMillis();
		Connection cn = null;
		Statement st = null;
		try{
			cn = _dso.getConnection();
			st = cn.createStatement();
			String[] consulta =control.insertarArchivoTransferencia(archivo,insertarEn803);
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
			
			throw new Exception();
		}finally{
			if (st!=null)st.close();
			if (cn!=null)cn.close();
		}
	}
	
	//se usa 2
	/**
	 * Actualiza las ordenes con la ejecución del proceso
	 * @param insertarEn803 verdadero para indicar que se desea crear el registro en la tabla 803
	 * @throws Exception en caso de error
	 */
	@SuppressWarnings("finally")
	private void actualizarOrdenesSITME(boolean insertarEn803, boolean bandera) throws Exception{
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
	
	@SuppressWarnings("finally")
	private String guardarOrdenSitme(Orden orden,boolean insertarEn803, boolean bandera,long id) throws Exception{
		
		OrdenDAO  		ordenDAO 			= new OrdenDAO(_dso);	
		long inicioActOrdenes = System.currentTimeMillis();
		Connection cn = null;
		Statement st = null;
		String control_var = null;
		SolicitudClavenet solicitudClavenet =null;
		try{
			
			cn = _dso.getConnection();
			st = cn.createStatement();
			
			control_orden = new OrdenDAO(_dso);			
			String[] consulta =control_orden.insertarSolicitudSITME(orden);
			//insumos
			Detalle detalle = new Detalle();
       		detalle.setIdOrden(orden.getIdOrden());  		
       		archivo.agregarDetalle(detalle);
       		solicitudClavenet = new SolicitudClavenet();
       		solicitudClavenet.setEstatus(StatusOrden.EN_TRAMITE);
       		solicitudClavenet.setIdOrden(id);
       		solicitudClavenet.setIdOrdenInfi(orden.getIdOrden());
       		solicitudClavenet.setFechaActualTramite(true);   		     		
       				
			String[] consulta2 = control.insertarArchivoTransferenciaPrivado(archivo,insertarEn803,bandera);			
			
			String consulta3 = ordenDAO.actualizarSolicitudClavenet(solicitudClavenet,StatusOrden.REGISTRADA);
		
			//Almacena las consultas finales
			for(int cont=0;cont<consulta.length; cont++){
				st.addBatch((String) consulta[cont]);
			}
		
			for(int cont=0;cont<consulta2.length; cont++){
				st.addBatch((String) consulta2[cont]);
			}
		
			st.addBatch(consulta3);		
			
			if (consulta.length > 0 && consulta2.length > 0 && consulta3.length() > 0){
				st.executeBatch();
				cn.commit();
			}
		
		    if (logger.isDebugEnabled()){
		    	logger.debug("Tiempo insertando orden SITME " + (System.currentTimeMillis() - inicioActOrdenes) + " miliseg");
		    }
		    
		    archivo.reiniciarDetalle();
		    
		    control_var = "OK";
		
		}catch (Exception e) {
			cn.rollback();
			archivo.reiniciarDetalle();
			logger.error("Error en el registro de la orden SITME metodo guardarOrdenSitme ",e);
			System.out.println("ERROR en guardarOrdenSitme "+e.getMessage());
			control_var = "NOT_OK";
			
		}finally{
			if (st!=null)st.close();
			if (cn!=null)cn.close();
			return control_var;
		}
	}
	
	
	
	//se usa
	private void verificarRegistros(StringBuilder sb) throws Exception{
		if (registroProcesado % 5000 == 0) {
			actualizarOrdenes(registroProcesado<=5000?true:false);
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
	

}//Fin Clase
	
	
