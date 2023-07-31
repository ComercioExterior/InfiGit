package models.gestion_pago_cheque;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra los detalles de un proceso de instruccion de pago y los edita de ser necesario
 * @author elaucho
 */
public class GestionDetalleProceso extends MSCModelExtend{

	@Override
public void execute() throws Exception {
	
/*
 * DAO a utilizar
 */
	GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
	long proceso 				  = Long.parseLong(_req.getParameter("proceso"));
 //String que indica si se ingresa al modelo en modo EDITAR
	String editar 				  = _req.getParameter("editar")==null||_req.getParameter("editar").equals("")?"0":_req.getParameter("editar");
/*
 * Dataset que publica si el proceso actual posee alguna instruccion de pago en status APLICADA
 */
	DataSet _existe				  = new DataSet();
	_existe.append("existe",java.sql.Types.VARCHAR);
	
	
/*
 * Si entra por primera vez, es decir si no se estan editando los registros
 */
	
	if(!editar.equals("1")){

/*
 * Se verifican si el proceso contiene operaciones en estatus ESPERA
 */
		boolean existe = gestionPagoDAO.verificarExisteOperacionAplicada(proceso);
		_existe.addNew();
		_existe.setValue("existe",String.valueOf(existe));
		storeDataSet("existe", _existe);
		
		
		gestionPagoDAO.detalleProceso(proceso);
//Se publican las operaciones financieras que forman parte del proceso actual
		storeDataSet("proceso",gestionPagoDAO.getDataSet());
		
/*
 * Se muestran las instrucciones de pago definidas para pago con cheque
 */
		gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.CHEQUE);
		storeDataSet("cheques",gestionPagoDAO.getDataSet());
		
		
/*
 * Se muestran las instrucciones de pago cuenta nacional
 */
		gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.CUENTA_NACIONAL);
		storeDataSet("nacional",gestionPagoDAO.getDataSet());
		
		
/*
 * Se muestran las instrucciones de pago cuenta internacional
 */
		gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.CUENTA_SWIFT);
		storeDataSet("internacional",gestionPagoDAO.getDataSet());
		
/*
 * Se muestran las instrucciones de pago operacion cambio
 */
		gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.OPERACION_DE_CAMBIO);
		storeDataSet("operacion_cambio",gestionPagoDAO.getDataSet());
		
/*
 * Se publica el proceso
 */
		DataSet _proceso = new DataSet();
		_proceso.append("proceso",java.sql.Types.VARCHAR);
		_proceso.addNew();
		_proceso.setValue("proceso",String.valueOf(proceso));
		storeDataSet("proceso_hidden", _proceso);
		
	}else{
		Connection conn      = _dso.getConnection();
		Statement statement  = conn.createStatement();
		boolean existe       = gestionPagoDAO.verificarExisteOperacionAplicada(proceso);
		_existe.addNew();
		_existe.setValue("existe",String.valueOf(existe));
		
		storeDataSet("existe", _existe);
	try {
/*
 * Se publican las operaciones financieras que forman parte del proceso actual
 */
		gestionPagoDAO.detalleProceso(proceso);
		storeDataSet("proceso",gestionPagoDAO.getDataSet());
		
/*
 * Se actualizan las isntrucciones de pago CHEQUE
 */
		
		String nombreArray[] 				= _req.getParameterValues("nombre");
		String cedulaArray[] 				= _req.getParameterValues("cedula");
		String instruccionArray[] 			= _req.getParameterValues("instruccion_cheque");
		String montoChequeArray[]		    = _req.getParameterValues("monto_cheque");
		if(nombreArray!=null){
			for(int i=0;i<nombreArray.length;i++){
				BigDecimal monto = new BigDecimal(Util.replace(montoChequeArray[i], ",", ".")).setScale(4,BigDecimal.ROUND_HALF_EVEN);
				statement.execute(gestionPagoDAO.actualizarInstruccionesDePagoCheque(proceso, Long.parseLong(instruccionArray[i]), nombreArray[i], cedulaArray[i],monto));
			}//fin for
		}//FIN IF
/*
 * Se procesan las instrucciones de pago que seran canceladas por transferencia internacional para actualizarlas
 */
		String montoInternacionalArray[]			= _req.getParameterValues("monto_internacional");
		String ctectaNombreInternacionalArray[]		= _req.getParameterValues("ctecta_nombre_internacional");
		String ctectaNumeroInternacionalArray[]		= _req.getParameterValues("ctecta_numero_internacional");
		String ctectaBcoctaAbaArray[]				= _req.getParameterValues("ctecta_bcocta_aba");
		String ctectaBcoctaBcoArray[]				= _req.getParameterValues("ctecta_bcocta_bco");		
		String ctectaBcoctaBicArray[]				= _req.getParameterValues("ctecta_bcocta_bic");
		String ctectaBcoctaDireccionArray[]			= _req.getParameterValues("ctecta_bcocta_direccion");
		String ctectaBcoctaSwiftArray[]				= _req.getParameterValues("ctecta_bcocta_swift");
		String ctectaBcoctaTelefonoArray[]			= _req.getParameterValues("ctecta_bcocta_telefono");
		String observacionArray[]					= _req.getParameterValues("observacion");
		String ctectaBcointAbaArray[]				= _req.getParameterValues("ctecta_bcoint_aba");
		String ctectaBcointBcoArray[]				= _req.getParameterValues("ctecta_bcoint_bco");
		String ctectaBcointBicArray[]				= _req.getParameterValues("ctecta_bcoint_bic");
		String ctectaBcointDireccionArray[]			= _req.getParameterValues("ctecta_bcoint_direccion");
		String ctectaBcointSwiftArray[]				= _req.getParameterValues("ctecta_bcoint_swift");
		String ctectaBcointTelefonoArray[]			= _req.getParameterValues("ctecta_bcoint_telefono");
		String observacionIntermediarioArray[]  	= _req.getParameterValues("observacion_intermediario");
		String nombreInternacionalArray[]			= _req.getParameterValues("nombre_internacional");
		String cedulaInternacionalArray[]			= _req.getParameterValues("cedula_internacional");
		String instruccioninternacionalArray[]		= _req.getParameterValues("instruccion_internacional");
		
	if(ctectaNumeroInternacionalArray!=null){
		Date fechaActual = new Date();
		InstruccionesPago instruccionesPago = new InstruccionesPago();
		for(int i=0;i<ctectaNumeroInternacionalArray.length;i++){
			instruccionesPago.setInstruccionId(Long.parseLong(instruccioninternacionalArray[i]));
			instruccionesPago.setProcesoId(proceso);
			instruccionesPago.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			instruccionesPago.setCedulaBeneficiario(cedulaInternacionalArray[i]);
			instruccionesPago.setNombrebeneficiario(nombreInternacionalArray[i]);
			instruccionesPago.setMontoInstruccion(new BigDecimal(Util.replace( montoInternacionalArray[i], ",", ".")));
			//instruccionesPago.setMonedaId(_req.getSession().getAttribute("moneda").toString());//Pendiente
			instruccionesPago.setFechaOperacion(fechaActual);
			instruccionesPago.setFechaValor(fechaActual);//Pendiente
			instruccionesPago.setClienteCuentaNombre(ctectaNombreInternacionalArray[i]);
			instruccionesPago.setClienteCuentaNumero(ctectaNumeroInternacionalArray[i]);
			instruccionesPago.setCtectaBcoctaAba(ctectaBcoctaAbaArray[i]);
			instruccionesPago.setCtectaBcoctaBco(ctectaBcoctaBcoArray[i]);
			instruccionesPago.setCtectaBcoctaBic(ctectaBcoctaBicArray[i]);
			instruccionesPago.setCtectaBcoctaDireccion(ctectaBcoctaDireccionArray[i]);
			instruccionesPago.setCtectaBcoctaSwift(ctectaBcoctaSwiftArray[i]);
			instruccionesPago.setCtectaBcoctaTelefono(ctectaBcoctaTelefonoArray[i]);
			instruccionesPago.setObservacionCtectaBcocta(observacionArray[i]);
			instruccionesPago.setCtectaBcointAba(ctectaBcointAbaArray[i]);
			instruccionesPago.setCtectaBcointBco(ctectaBcointBcoArray[i]);
			instruccionesPago.setCtectaBcointBic(ctectaBcointBicArray[i]);
			instruccionesPago.setCtectaBcointDireccion(ctectaBcointDireccionArray[i]);
			instruccionesPago.setCtectaBcointSwift(ctectaBcointSwiftArray[i]);
			instruccionesPago.setCtectaBcointTelefono(ctectaBcointTelefonoArray[i]);
			instruccionesPago.setObservacionCtectaBcoint(observacionIntermediarioArray[i]);
			
			statement.execute(gestionPagoDAO.actualizarInstruccion(instruccionesPago));
			
		}//fin for
	}
/*
 * Se procesan las instrucciones de pago que seran canceladas por transferencia Nacional para actualizarlas
 */	
	String nombreNacional[]			= _req.getParameterValues("nombre_nacional");
	String cedulaNacional[]			= _req.getParameterValues("cedula_nacional");
	String cuentanombreNacional[]	= _req.getParameterValues("ctecta_nombre_nacional");
	String cuentaNumeroNacional[]	= _req.getParameterValues("ctecta_numero_nacional");
	String montoNacional[]			= _req.getParameterValues("monto_instruccion_nacional");
	String instruccionNacionalArray[] = _req.getParameterValues("instruccion_nacional");
	
	if(nombreNacional!=null){
		Date fechaActual = new Date();
		InstruccionesPago instruccionesPago = new InstruccionesPago();
		for(int i=0;i<cedulaNacional.length;i++){
			instruccionesPago.setInstruccionId(Long.parseLong(instruccionNacionalArray[i]));
			instruccionesPago.setProcesoId(proceso);
			instruccionesPago.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			instruccionesPago.setCedulaBeneficiario(cedulaNacional[i]);
			instruccionesPago.setNombrebeneficiario(nombreNacional[i]);
			instruccionesPago.setMontoInstruccion(new BigDecimal(Util.replace( montoNacional[i], ",", ".")).setScale(4,BigDecimal.ROUND_HALF_EVEN));
			instruccionesPago.setMonedaId(_req.getParameter("moneda_id"));//Pendiente
			instruccionesPago.setFechaOperacion(fechaActual);
			instruccionesPago.setFechaValor(fechaActual);//Pendiente
			instruccionesPago.setClienteCuentaNombre(cuentanombreNacional[i]);
			instruccionesPago.setClienteCuentaNumero(cuentaNumeroNacional[i]);
			
			statement.execute(gestionPagoDAO.actualizarInstruccion(instruccionesPago));

		}//fin for
	}
/*
 * Se hace el commit de la conexion
 */
		statement.executeBatch();
		conn.commit();
		
	/*
	 * Se publica el proceso
	 */
		DataSet _proceso = new DataSet();
		_proceso.append("proceso",java.sql.Types.VARCHAR);
		_proceso.addNew();
		_proceso.setValue("proceso",String.valueOf(proceso));
		storeDataSet("proceso_hidden", _proceso);
		
	/*
	 * Se muestran las instrucciones de pago actualizadas
	 */
		gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.CHEQUE);
		storeDataSet("cheques",gestionPagoDAO.getDataSet());
		
	/*
	 * Se muestran las instrucciones de pago cuenta nacional actualizadas
	 */
		gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.CUENTA_NACIONAL);
		storeDataSet("nacional",gestionPagoDAO.getDataSet());
		
	/*
	 * Se muestran las instrucciones de pago cuenta internacional actualizadas
	 */
		gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.CUENTA_SWIFT);
		storeDataSet("internacional",gestionPagoDAO.getDataSet());
		
		
	/*
	 * Se muestran las instrucciones de pago operacion cambio
	 */
			gestionPagoDAO.listarInstrucciones(proceso,TipoInstruccion.OPERACION_DE_CAMBIO);
			storeDataSet("operacion_cambio",gestionPagoDAO.getDataSet());
	
		
	} catch (Exception e) {
			conn.rollback();
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw e;
		}finally{
			if(statement!=null){
				statement.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
	}//fin else

  }//FIN EXECUTE
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		GestionPagoDAO gestionPagoDAO 	 = new GestionPagoDAO(_dso);
		long proceso 					 = Long.parseLong(_req.getParameter("proceso"));
		BigDecimal montoProceso 		 = gestionPagoDAO.listarMontoOperacionesproceso(proceso);
		BigDecimal operacionesprocesadas = gestionPagoDAO.listarMontoOperacionesProcesadasProceso(proceso);
		String montoChequeArray[]		 = _req.getParameterValues("monto_cheque");
		String montoNacionalArray[]		 = _req.getParameterValues("monto_instruccion_nacional");
		String montoInternacionalArray[] = _req.getParameterValues("monto_internacional");
		String readonlyChequeArray[] 	 = _req.getParameterValues("readonly_cheque");
		String readonlyNacionalArray[]	 = _req.getParameterValues("readonly_nacional");
		String readonlyInternacionalArray[]=_req.getParameterValues("readonly_internacional");
		String editar 					 = _req.getParameter("editar")==null||_req.getParameter("editar").equals("")?"0":_req.getParameter("editar");
		BigDecimal montoRequest 		 = new BigDecimal(0);
	/*
	 * Este monto debe ser igual a los que vienen de los campos del request
	 */
		BigDecimal montoValidar = montoProceso.subtract(operacionesprocesadas);
		
	/*
	 * Sumamos los montos de cheques de las operaciones con status EN ESPERA
	 */
		if(montoChequeArray!=null){
			for(int i = 0;i<montoChequeArray.length;i++){
				if(readonlyChequeArray[i].equalsIgnoreCase("0")){
					montoRequest = montoRequest.add(new BigDecimal(Util.replace(montoChequeArray[i]==null||montoChequeArray[i].equals("")?"0":montoChequeArray[i],",", ".")));
				}//fin if
			}//fin for
		}//fin if
	/*
	 * Sumamos los montos de transferencias nacionales de las operaciones con status EN ESPERA
	 */
		if(montoNacionalArray!=null){
			for(int i = 0;i<montoNacionalArray.length;i++){
				if(readonlyNacionalArray[i].equalsIgnoreCase("0")){
					montoRequest = montoRequest.add(new BigDecimal(Util.replace(montoNacionalArray[i]==null||montoNacionalArray[i].equals("")?"0":montoNacionalArray[i], ",", ".")));
				}//fin if
			}//fin for
		}//fin if
	
	/*
	 * Sumamos los montos de transferencias Internacionales de las operaciones con status EN ESPERA
	 */
		if(montoInternacionalArray!=null){
			for(int i = 0;i<montoInternacionalArray.length;i++){
				if(readonlyInternacionalArray[i].equalsIgnoreCase("0")){
					montoRequest = montoRequest.add(new BigDecimal(Util.replace(montoInternacionalArray[i]==null||montoInternacionalArray[i].equals("")?"0":montoInternacionalArray[i], ",", ".")));
				}//fin if
			}//fin for
		}//fin if
		if (flag)
		{		
		//Si se entra en el modo de editar se realizan validaciones
			if(editar.equals("1")){
				
				//Declaracion de variables a utilizar
				String nombreArray[] 				= _req.getParameterValues("nombre");
				String cedulaArray[] 				= _req.getParameterValues("cedula");
				String nombreCuentaArray[] 			= _req.getParameterValues("ctecta_nombre_nacional");
				String numeroCuentaArray[] 			= _req.getParameterValues("ctecta_numero_nacional");
				String nombreTransferenciaArray[] 	= _req.getParameterValues("nombre_nacional");
				String cedulaTransferenciaArray[] 	= _req.getParameterValues("cedula_nacional");
				String nombreBcoIntArray[] 			= _req.getParameterValues("ctecta_bcoint_bco");
				String nombreBeneficiarioArray[] 	= _req.getParameterValues("nombre_internacional");
				String numeroCuentaInterArray[] 	= _req.getParameterValues("ctecta_numero_internacional");
				String direccionBancoArray[] 		= _req.getParameterValues("ctecta_bcocta_direccion");
				String direccionBancoIntArray[] 	= _req.getParameterValues("ctecta_bcoint_direccion");
				String codigoBicArray[] 			= _req.getParameterValues("ctecta_bcocta_bic");
				String cedulaInternacionalArray[] 	= _req.getParameterValues("cedula_internacional");
				String nombreCuentaInternArray[] 	= _req.getParameterValues("ctecta_nombre_internacional");
				String codigoSwiftArray[] 			= _req.getParameterValues("ctecta_bcocta_swift");
				String codigoBicIntermediarioArray[]= _req.getParameterValues("ctecta_bcoint_bic");
				String codigoAbaArray[] 			= _req.getParameterValues("ctecta_bcocta_aba");
				String telefonoIntermediarioArray[] = _req.getParameterValues("ctecta_bcoint_telefono");
				String observacionesArray[] 		= _req.getParameterValues("observacion");
				String nombreBancoInternArray[] 	= _req.getParameterValues("ctecta_bcocta_bco");
				String telefonoArray[] 	= _req.getParameterValues("ctecta_bcocta_telefono");
				String observacionIntermediariosArray[] = _req.getParameterValues("observacion_intermediario");
				String codigoAbaIntermadiarioArray[]= _req.getParameterValues("ctecta_bcoint_aba");
				String codigoSwiftIntermediarioArray[]= _req.getParameterValues("ctecta_bcoint_swift");
				
				/*
				 * Validaciones para los campos de instrucciones internacionales
				 */
				if(nombreBeneficiarioArray!=null){
				for(int i=0;i<nombreBeneficiarioArray.length;i++)
				{
					if(nombreBeneficiarioArray[i]==null||nombreBeneficiarioArray[i].equals("")){
						int registro = i+1;
						_record.addError("Nombre del Beneficiario", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}//fin if
					
					if(cedulaInternacionalArray[i]==null||cedulaInternacionalArray[i].equals("")){
						int registro = i+1;
						_record.addError("C&eacute;dula", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					
					if(numeroCuentaInterArray[i]==null||numeroCuentaInterArray[i].equals("")){
						int registro = i+1;
						_record.addError("N&uacute;mero de Cuenta", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					if(direccionBancoArray[i]==null||direccionBancoArray[i].equals("")){
						int registro = i+1;
						_record.addError("Direcci&oacute;n", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					if(codigoBicArray[i]==null||codigoBicArray[i].equals("")||codigoAbaArray[i]==null||codigoAbaArray[i].equals("")){
						int registro = i+1;
						_record.addError("BIC o ABA", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					
					if(nombreCuentaInternArray[i]==null||nombreCuentaInternArray[i].equals("")){
						int registro = i+1;
						_record.addError("Nombre/Cuenta", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					
					if(codigoSwiftArray[i]==null||codigoSwiftArray[i].equals("")){
						int registro = i+1;
						_record.addError("C&oacute;digo SWIFT", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					if(codigoBicArray[i]!=null && !codigoBicArray[i].equals("") && codigoAbaArray[i]!=null && codigoAbaArray[i].equals("")){
						int registro = i+1;
						_record.addError("C&oacute;digo ABA", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					if(observacionesArray[i]==null||observacionesArray[i].equals("")){
						int registro = i+1;
						_record.addError("Observaciones", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
					if(telefonoArray[i]==null||telefonoArray[i].equals("")){
						int registro = i+1;
						_record.addError("Tel&eacute;fono", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}
				}//fin for		
			}//FIN Validaciones para los campos de instrucciones internacionales
				

/*
 * Valida el nombre del cheque
 */
				if(nombreArray!=null){
				for(int i=0;i<nombreArray.length;i++)
				{
					if(nombreArray[i]==null||nombreArray[i].equals("")){
						int registro = i+1;
						_record.addError("Nombre del Beneficiario/Cheque", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}//fin if
				
				/*
				 * Valida la cedula del Cheque
				 */
					if(cedulaArray[i]==null||cedulaArray[i].equals("")){
						int registro = i+1;
						_record.addError("C&eacute;dula del Beneficiario/Cheque", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}//fin if
				}//fin for		
				}//Fin validaciones Cheque
				
				/*
				 * Valida el nombre para la transferencia nacional
				 */
				if(nombreTransferenciaArray!=null){
				for(int i=0;i<nombreTransferenciaArray.length;i++)
				{
					if(nombreTransferenciaArray[i]==null||nombreTransferenciaArray[i].equals("")){
						int registro = i+1;
						_record.addError("Nombre/transferencia", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}//fin if
				/*
				 * Valida la cedula para la transferencia nacional
				 */
					if(cedulaTransferenciaArray[i]==null||cedulaTransferenciaArray[i].equals("")){
						int registro = i+1;
						_record.addError("C&eacute;dula/transferencia", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}//fin if
				}//fin for

				/*
				 * Valida el nombre de la cuenta para la transferencia nacional
				 */
				for(int i=0;i<nombreCuentaArray.length;i++)
				{
					if(nombreCuentaArray[i]==null||nombreCuentaArray[i].equals("")){
						int registro = i+1;
						_record.addError("Nombre cuenta", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}//fin if

				/*
				 * Valida el numero de la cuenta para la transferencia nacional
				 */

					if(numeroCuentaArray[i]==null||numeroCuentaArray[i].equals("")){
						int registro = i+1;
						_record.addError("N&uacute;mero cuenta", "Es obligatorio para el registro "+registro);
						flag = false;
					
					}//fin if
				}//fin for
				}//Fin validaciones transferencias Nacionales
			
		if(montoValidar.setScale(4, BigDecimal.ROUND_HALF_EVEN) .compareTo(montoRequest.setScale(4, BigDecimal.ROUND_HALF_EVEN))!=0){
			_record.addError("Monto", "El total debe ser igual al monto de la Transacci&oacute;n: "+montoValidar.setScale(4, BigDecimal.ROUND_HALF_EVEN));
			flag = false;
		}//fin if
/*
 * Valida el monto de la Transferencia Internacional
 */
		if(montoInternacionalArray!=null){
			for(int i=0;i<montoInternacionalArray.length;i++)
			{
				if(montoInternacionalArray[i]==null||montoInternacionalArray[i].equals("")){
					int registro = i+1;
					_record.addError("Monto/internacional", "Es obligatorio para el registro "+registro);
					flag = false;
				}//fin if
			}//fin for	
		}		
/*
 * Valida el monto de la Operacion del Cheque
 */		
		if(montoChequeArray!=null){
			for(int i=0;i<montoChequeArray.length;i++)
			{
				if(montoChequeArray[i]==null||montoChequeArray[i].equals("")){
					int registro = i+1;
					_record.addError("Monto/Cheque", "Es obligatorio para el registro "+registro);
					flag = false;
				}//fin if
			}//fin for
 		}//fin if
	  }//fin if EDITAR
	}//FIN if (flag)
		return flag;	
	}//fin isValid
	
}//FIN CLASE
