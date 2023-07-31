package models.carga_inicial_pagos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.CargaInicialPagoDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.PagoTMP;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.data.ProcesoCargaInicial;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

public class CargaInicialPagos extends AbstractModel{
	
	private Transaccion transaccion = null;				
	private Logger logger = Logger.getLogger(CargaInicialPagos.class);
	private Proceso proceso = null;
	private ProcesosDAO procesosDAO = null;
	CargaInicialPagoDAO cargaInicialPagoDAO =null;
	//ProcesoCargaInicial procesoCarga=null;
	
	public void execute() throws Exception {		
		int num_reg_malos = 0; 
		int num_reg_buenos = 0; 
		
		try{
			//Iniciamos una transacción
	        transaccion = new Transaccion(_dso);
	        transaccion.begin();
	        ClienteDAO clienteDao = new ClienteDAO(_dso);   
	        cargaInicialPagoDAO = new CargaInicialPagoDAO(_dso);  
			procesosDAO = new ProcesosDAO(_dso);			
			
			logger.info("************************ INICIANDO PROCESO DE CARGA INICIAL DE PAGOS ************************");
			iniciarProceso();    	       
	        String[] campos = null; //campos de la línea	        
		    String linea;
		    PagoTMP pagoTMP=new PagoTMP();
	        
	        //Inicializamos
	        String tipoCliente = "";
	        long cedulaCliente = 0;	       
	        String[] campoCedula = null;
        	DataSet cliente = null;
          	int lineaLeida = 0;
          	boolean error = false;
          	String errores = "";
          	
          	//BORRAR REGISTROS TEMPORALES DE CARGAS ANTERIORES         	
          	cargaInicialPagoDAO.eliminarRegistrosTemporales(_record.getValue("titulo_id")); 
          	
          	logger.info("RUTA DE ARCHIVO A LEER : "+_record.getValue("fichero.tempfile"));
        	BufferedReader br = new BufferedReader(new FileReader(_record.getValue("fichero.tempfile")));
           
	            /*LA ESTRUCTURA DE CADA LINEA ES LA SIGUIENTE: separado cada campo por ";"
	             * 1. Nombre del cliente
	             * 2. Cédula del cliente (V-xxxxxxx ó Vxxxxxxxxx) 
	             * 3. Capital o valor nominal
	             * 4. Monto pendiente
	             * 5. Monto pagado
	             * 6. Fecha de pago
	             * 7. Forma de pago
	             * 8. Título
	             * 9. Fecha inicio cupón
	             * 10. Fecha fin cupón
	             * 11. Tipo de Producto 
	             * */            
	            while ((linea = br.readLine()) != null) {
	            	String errorRegistro = "";
	            	
	            	lineaLeida++;
	            	/*if (lineaLeida==1){
	            		continue;
	            	}*/   	 
	            	            	
	            		            	
	            	//OBTENER LOS CAMPOS DEL ARCHIVO
	            	campos = linea.split(";");	
	            	logger.info("NRO CAMPOS "+ campos.length);
	            	
	            	if(campos!=null && campos.length < 11){
	            		transaccion.rollback();						
						errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Los campos est&aacute;n incompletos, verifique que el separador de campos sea ';'<br><br>";
						error =true;
	            	}else{	            		   	
	            		logger.info("   Cliente a procesar: " + campos[0] + " con cédula " + campos[1]);
	            		pagoTMP.setNombreCliente(campos[0]);
		            	pagoTMP.setCedulaRifCliente(campos[1]);
		            	pagoTMP.setMontoPago(campos[2]);
		            	pagoTMP.setInteresPorPagar(campos[3]);
		            	pagoTMP.setInteresPagado(campos[4]);
		            	pagoTMP.setFechaPago(campos[5]);
		            	pagoTMP.setTipoTransferencia(campos[6]);
		            	pagoTMP.setTitulo(campos[7]);	            	
		            	pagoTMP.setFechaInicioCupon(campos[8]);
		            	pagoTMP.setFechaFinCupon(campos[9]);
		            	pagoTMP.setTipoProducto(campos[10]); 	            	
		            	pagoTMP.setArchivo(_record.getValue("fichero.filename"));
		            	
		            	//----VALIDANDO CEDULAS/RIF-----------------------		            	
		            	if(pagoTMP.getCedulaRifCliente()==null || pagoTMP.getCedulaRifCliente().equals("")){
		            		transaccion.rollback();						
							errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: La c&eacute;dula/rif del cliente no es num&eacute;rica o esta mal formada dentro del archivo."+ "<br><br>";
							error =true;
		            	}else{	
		            		pagoTMP.setCedulaRifCliente(pagoTMP.getCedulaRifCliente().trim()); //Eliminar espacios en blanco al comienzo y al final		            		
		         	       	campoCedula = campos[1].split("-");
			            	//Si la longitud es 1 es que no consiguió el "-"
			            	try {
								if (campoCedula.length == 1){									
									tipoCliente = campoCedula[0].substring(0,1);
									cedulaCliente = convertirCedula(campoCedula[0].substring(1), lineaLeida);
								}else {
									tipoCliente = campoCedula[0];
									cedulaCliente = convertirCedula(campoCedula[1], lineaLeida);
								}
								pagoTMP.setCedulaRifClienteNum(cedulaCliente);
				            	pagoTMP.setTipoCedRifCliente(tipoCliente);
				            	
			            	}catch(StringIndexOutOfBoundsException sIOBE){
			            		transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: La c&eacute;dula/rif del cliente no es num&eacute;rica o esta mal formada dentro del archivo."+ "<br><br>";
								error =true;
		
							} catch (Exception e1) {	
								transaccion.rollback();						
								errores = errores+ e1.getMessage()+ "<br><br>";
								error =true;
							}
		            	}
		            	

		            	 if(pagoTMP.getInteresPorPagar()!=null && !pagoTMP.getInteresPorPagar().equals("0") && !pagoTMP.getInteresPorPagar().equals("")){
		            		 if(pagoTMP.getInteresPagado()!=null && !pagoTMP.getInteresPagado().trim().equals("") && !pagoTMP.getInteresPagado().trim().equals("0")){		            			 
				            		transaccion.rollback();
		            			 errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Se ha ingresado un monto Mayor a cero en el campo de Intereses por Pagar, el campo de intereses pagado debe estar vacio o ser igual a cero."+ "<br><br>";
		            			 error=true;
		            		 } else {		            			            			 
		            			 pagoTMP.setFechaPago("");		            			 
		            			 pagoTMP.setTipoTransferencia("");
		            		 }		            		 		            		
		            	 }else if(pagoTMP.getInteresPagado()!=null && !pagoTMP.getInteresPagado().trim().equals("") && !pagoTMP.getInteresPagado().trim().equals("0")){
		            		 
		            		 if(pagoTMP.getInteresPorPagar()!=null && !pagoTMP.getInteresPorPagar().trim().equals("") && !pagoTMP.getInteresPorPagar().trim().equals("0")){
				            		transaccion.rollback();
		            			 errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Se ha ingresado un monto Mayor a cero en el campo de Intereses Pagados, el campo de Intereses por Pagar debe estar vacio o ser igual a cero."+ "<br><br>";
		            			 error=true;
		            		 } else if (pagoTMP.getFechaPago()==null) {
		            			transaccion.rollback();
		            			errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Se ha ingresado un monto Mayor a cero en el campo de Intereses Pagados, el campo de Fecha de Pago no debe estar vacio."+ "<br><br>";
     	            			error=true;
		            		 }
		            		 		            		 
		            	 }
		            	 

		            	 //VALIDANDO INTERESES POR PAGAR Y PAGADOS INGRESADOS EN UN MISMO REGISTRO: NO ES CORRECTO-----------------------------------
		            	/*if(pagoTMP.getInteresPagado()!=null && pagoTMP.getInteresPorPagar()!=null && !pagoTMP.getInteresPagado().trim().equals("") && !pagoTMP.getInteresPorPagar().trim().equals("")){
		            		transaccion.rollback();						
							errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Se indican en un mismo registro los intereses pagados y por pagar."+ "<br><br>";								
							error =true;		            	
		            	}*/

						/*VALIDANDO VALOR NOMINAL-----------------------------------
		            	if(campos[2]!=null){
		            		campos[2] = campos[2].trim();
		            		if(!this.isDecimal(campos[2])){
			            		transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: El valor nominal no es un n&uacute;mero v&aacute;lido."+ "<br><br>";
								error =true;		
		            		}
		            	}
						//VALIDANDO INTERESES POR PAGAR-----------------------------------
		            	if(campos[3]!=null){
		            		campos[3] = campos[3].trim();
		            		if(!this.isDecimal(campos[3])){
			            		transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Los intereses por pagar no corresponden a un monto v&aacute;lido."+ "<br><br>";								
								error =true;		
		            		}
		            	}
		            	//-----------------------------------------------------------------

						//VALIDANDO INTERESES PAGADOS-----------------------------------
		            	if(campos[4]!=null){
		            		campos[4] = campos[4].trim();
		            		if(!this.isDecimal(campos[4])){
			            		transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Los intereses pagados no corresponden a un monto v&aacute;lido."+ "<br><br>";								
								error =true;		
		            		}
		            	}
		            	*/
		            	
						//VALIDANDO FECHA PAGO-----------------------------------
		            	if(pagoTMP.getFechaPago()!=null &&  !pagoTMP.getFechaPago().equals("")){
		            		pagoTMP.setFechaPago(pagoTMP.getFechaPago().trim());
		            		if(!this.formatFechaOk(pagoTMP.getFechaPago(), ConstantesGenerales.FORMATO_FECHA2)){
			            		transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: La fecha de pago no posee un formato adecuado a "+ ConstantesGenerales.FORMATO_FECHA2+ ".<br><br>";								
								error =true;		
		            		}
		            	}
						
		            	//---VALIDANDO CAMPOS DE PAGO------------------------------------------
		            	if(pagoTMP.getInteresPagado()!=null && !pagoTMP.getInteresPagado().trim().equals("") && !pagoTMP.getInteresPagado().trim().equals("0")){//SI SE INDICAN INTERESES PAGADOS
		            		//Si se indican intereses pagados se debe indicar fecha y tipo de pago
		            		if(pagoTMP.getFechaPago()==null || (pagoTMP.getFechaPago()!=null && pagoTMP.getFechaPago().trim().equals(""))){
			            		transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Se debe indicar la fecha de pago para los intereses pagados."+ "<br><br>";								
								error =true;		
		            		}
		            		if(pagoTMP.getTipoTransferencia()==null || (pagoTMP.getTipoTransferencia()!=null && pagoTMP.getTipoTransferencia().trim().equals(""))){
			            		transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: Se debe indicar el tipo de pago para los intereses pagados."+ "<br><br>";								
								error =true;		
		            		}

		            	}
		            
		            	//--VALIDANDO TITULO------------------------------------------	            	
		            	if(pagoTMP.getTitulo()==null || pagoTMP.getTitulo().equals("")){
		            		transaccion.rollback();						
							errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: No se indica el t&iacute;tulo asociado al pago de cup&oacute;n."+ "<br><br>";
							error =true;
		            	}else{
		            		pagoTMP.setTitulo(pagoTMP.getTitulo().trim());
		            		if(!pagoTMP.getTitulo().equalsIgnoreCase(_record.getValue("titulo_id").trim())){
		            			transaccion.rollback();						
								errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: El t&iacute;tulo ingresado en el archivo no corresponde con el seleccionado."+ "<br><br>";
								error =true;	
		            		}
		            		
		            	}
		            	
		            	//---Validando fecha de Inicio de pago de cupon-------------------	            	
		            	if(pagoTMP.getFechaInicioCupon()==null || pagoTMP.getFechaInicioCupon().equals("")){
		            		transaccion.rollback();						
							errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: No se indica la fecha de inicio del pago de cup&oacute;n."+ "<br><br>";
							error =true;
		            	}else{
		            		pagoTMP.setFechaInicioCupon(pagoTMP.getFechaInicioCupon().trim());
							//VALIDANDO FECHA INICIO-----------------------------------
			            	if(pagoTMP.getFechaInicioCupon()!=null){
			            		if(!this.formatFechaOk(pagoTMP.getFechaInicioCupon(), ConstantesGenerales.FORMATO_FECHA2)){
				            		transaccion.rollback();						
									errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: La fecha de inicio de pago no posee un formato adecuado a "+ ConstantesGenerales.FORMATO_FECHA2+ ".<br><br>";								
									error =true;		
			            		}
			            	}			            	

		            	}
		            	
		            	//---Validando fecha de Fin de pago de cupon-------------------	            	
		            	if(pagoTMP.getFechaFinCupon()==null || pagoTMP.getFechaFinCupon().equals("")){
		            		transaccion.rollback();						
							errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: No se indica la fecha final del pago de cup&oacute;n."+ "<br><br>";
							error =true;
		            	}else{
		            		pagoTMP.setFechaFinCupon(pagoTMP.getFechaFinCupon().trim());
		            		
							//VALIDANDO FECHA INICIO-----------------------------------
			            	if(pagoTMP.getFechaFinCupon()!=null){
			            		if(!this.formatFechaOk(pagoTMP.getFechaFinCupon(), ConstantesGenerales.FORMATO_FECHA2)){
				            		transaccion.rollback();						
									errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: La fecha final de pago no posee un formato adecuado a "+ ConstantesGenerales.FORMATO_FECHA2+ ".<br><br>";								
									error =true;		
			            		}
			            	}
		            	}
		            	
		            	//---Validando Tipo de Producto-------------------	            	
		            	if(pagoTMP.getTipoProducto()==null || pagoTMP.getTipoProducto().equals("")){
		            		transaccion.rollback();						
							errores = errores+ "L&iacute;nea " +lineaLeida+ ": {" +linea+ "}: No se indica el tipo de producto asociados al pago de cup&oacute;n."+ "<br><br>";
							error =true;
		            	}
		            		      
		            	//Verificar cliente existente en INFI
		            	clienteDao.listarPorCedRifyTipoPersona(cedulaCliente, tipoCliente);
		            	cliente = clienteDao.getDataSet();
		            	if (cliente.count()==0){		            		
		            		try{
		            			buscarYGuardarClienteAltair(cedulaCliente, tipoCliente);
							} catch (Exception e3) {	
								transaccion.rollback();						
								errores = errores+ e3.getMessage()+ "<br><br>";
								error =true;
							}	
							
		            	}  	
		            	
						//No se guarda ningun registro cuando existen errores con los datos de los clientes
						if(!error){
							
							//---truncar caracteres a la longitud maxima permitida en base de datos para el nombre del cliente
			            	if(pagoTMP.getNombreCliente()!=null){
			            		pagoTMP.setNombreCliente(pagoTMP.getNombreCliente().trim());
				            	if(pagoTMP.getNombreCliente().length()>50){
				            		pagoTMP.setNombreCliente(pagoTMP.getNombreCliente().substring(0,49));
								}
			            	}						
		
			              	//----Guardar en tabla temporal los campos para el registro actual-----------------
			            	try {			            						            	
				            	
			            		//Verificar si es registro repetido y guardar en el campo de error
			            		if(cargaInicialPagoDAO.esRegistroRepetidoTMPPagos(pagoTMP)){
			            			errorRegistro = errorRegistro+" Registro Duplicado";
			            			//error =true;
			            		}
			            		if(error){
			            			num_reg_malos++;
			            		}else{
			            			num_reg_buenos++;
			            		}
			            			
			            		pagoTMP.setError(errorRegistro);
			            		//Ejecutar insert en tabla temporal
			            		cargaInicialPagoDAO.cargarRegistroTemporaPago(pagoTMP);
								
							} catch (Exception e) {
								transaccion.rollback();	
								e.printStackTrace();				
								logger.info("L&iacute;nea " +lineaLeida+ ": Error intentando almacenar el registro {"+linea+"} del archivo "+_record.getValue("fichero.filename"));
								logger.info("Error: "+e.getMessage());					
								throw new Exception("L&iacute;nea " +lineaLeida+ ": Error intentando almacenar el registro {"+linea+"} del archivo "+_record.getValue("fichero.filename"));
							}
			            	//----------------------------------------------------------------------------------
						} 
	            	}
	            }
	            
	            if(!error){
	            	transaccion.end();	
	            	logger.info("El archivo " +_record.getValue("fichero.filename")+ "  de pagos de cupones fue cargado exitosamente.");
	            } else{
	            	logger.info("El archivo "+ _record.getValue("fichero.filename")+ " de pago de cupones contiene errores y no ha sido validado correctamente para la carga de sus datos.");
	            	throw new Exception(errores);
	            }
	            logger.info("Numero de registros con formato correcto: "+num_reg_buenos+", archivo: "+_record.getValue("fichero.filename"));
	            logger.info("Numero de registros con formato errado  : "+num_reg_malos +", archivo: "+_record.getValue("fichero.filename"));
		} catch (Exception ex){
						
			transaccion.rollback();
			logger.info("Error realizando la carga del archivo de pagos "+ _record.getValue("fichero.filename"));
			logger.info(ex.getMessage());
			ex.printStackTrace();	
			throw new Exception(ex.getMessage());				
			
		}
		terminarProceso();
	}
	
	/**
	 * Buscar un cliente en altair y lo registra en INFI
	 * @param cedulaCliente
	 * @param tipoCliente
	 * @throws Exception
	 */
	private void buscarYGuardarClienteAltair(long cedulaCliente,String tipoCliente) throws Exception {
		
		try {
			String nombreUsuario = getUserName();
			ManejadorDeClientes mdc = new ManejadorDeClientes(_app,null);
			
			//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
			com.bdv.infi.webservices.beans.Cliente clienteWS = mdc.getCliente(String.valueOf(cedulaCliente), tipoCliente, nombreUsuario, _req.getRemoteAddr(),false,true,false,false);
				
			//Si lo encuentra lo almacena en la tabla de cliente
			com.bdv.infi.data.Cliente clienteNuevo = new com.bdv.infi.data.Cliente();
			//clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi().replaceAll("^0+", "")));
			clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi()));
			clienteNuevo.setNombre(clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " "));
			clienteNuevo.setTipoPersona(clienteWS.getTipoDocumento().trim());
			clienteNuevo.setDireccion(clienteWS.getDireccion());
			clienteNuevo.setTelefono(clienteWS.getTelefono());
			clienteNuevo.setTipo(clienteWS.getTipo().trim());
			clienteNuevo.setCorreoElectronico(clienteWS.getCorreoElectronico());
			clienteNuevo.setEmpleado(clienteNuevo.EMPLEADO.equals(clienteWS.getEsEmpleado())?true:false);												
			clienteNuevo.setCodigoSegmento(clienteWS.getPEM1403().getSegmento().trim());
				
			//insertar nuevo cliente en base de datos INFI
			com.bdv.infi.dao.ClienteDAO clienteDao = new com.bdv.infi.dao.ClienteDAO(_dso);
			try{				
				clienteDao.insertar(clienteNuevo);				
				
				logger.info("CLIENTE NUEVO EN INFI CON ID " + clienteNuevo.getIdCliente());
				
			}catch(Exception e2){
				e2.printStackTrace();
				logger.info("Error insertando cliente de cedula/rif: '" +tipoCliente+"-"+cedulaCliente+ "' en INFI. "+e2.getMessage());
				throw new Exception("Error insertando cliente de cedula/rif: '" +tipoCliente+"-"+cedulaCliente+ "' en INFI. "+e2.getMessage());			
			}finally{
				clienteDao.cerrarConexion();
			}		
		
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.info("Error consultando el cliente de cedula/rif: '" +tipoCliente+"-"+cedulaCliente+ "' en arquitectura extendida. "+ e1.getMessage());
			throw new Exception("Error consultando el cliente de cedula/rif: '" +tipoCliente+"-"+cedulaCliente+ "' en arquitectura extendida. "+e1.getMessage());			
		}

		
	}

	

	/**Convertir c&eacute;dula del cliente 
	 * @param lineaLeida */
	private long convertirCedula(String cedula, int lineaLeida) throws Exception{
		long cedulaConv=0;
		try{
			cedulaConv = Long.parseLong(cedula);
		} catch (Exception e) {
			throw new Exception("L&iacute;nea " +lineaLeida+ ": La c&eacute;dula/rif del cliente no es num&eacute;rica o esta mal formada dentro del archivo.");
		}
		return cedulaConv;
	}	
		
	
	/**
	 * M&eacute;todo para validar un numero con decimales
	 * @param cadena
	 * @return true si el valor del par&aacute;metro puede ser convertido a un n&uacute;mero con decimales, false en caso contrario
	 */	
	public boolean isDecimal(String cadena){
		
		try {			
			Double.parseDouble(cadena);			
			return true;			
		} catch (NumberFormatException nfe){
			return false;			
		}
		
	}
	
	/**
	 * M&eacute;todo para validar que una fecha posea un formato espec&iacute;fico
	 * @param cadena_fecha
	 * @param formato
	 * @return true si el valor del par&aacute;metro cadena_fecha puede ser convertido a una fecha con formato especificado, false en caso contrario
	 */
	public boolean formatFechaOk (String cadena_fecha, String formato){
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		sdf.setLenient(false);
		
		try {			
			sdf.parse(cadena_fecha); //convertir string a date	
			return true;			
		} catch (ParseException pe){
			return false;			
		}

	}
	
	/**
	 * Envia parametros necesarios a la accion siguiente configurada en el tag '<next-action type="redirect">' del archivo config.xml
	 */
	@Override
	public String getRedirectParameters() throws Exception {
		return "&titulo_id=" + _record.getValue("titulo_id");
	}

	/**
	 * Inicia el proceso de carga inicial de cupones
	 */
	private void iniciarProceso() throws Exception{
		UsuarioDAO usu	= new UsuarioDAO(_dso);
		
		proceso = new Proceso();
		proceso.setTransaId(TransaccionNegocio.CARGA_INICIAL);
		proceso.setUsuarioId(Integer.parseInt(usu.idUserSession(getUserName())));
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());		
		/* Primero creamos el proceso */		
		db.exec(_dso, procesosDAO.insertar(proceso));

		logger.info("Comenzó proceso: Carga inicial de pago de cupones");
				
	}
	
	/**
	 * Finaliza el proceso de carga inicial de cupones
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
				logger.info("Finalizó proceso: Carga inicial de pago de cupones");
			}
		}
	}

}
