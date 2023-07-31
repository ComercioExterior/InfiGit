package models.liquidacion.instrucciones_venta_titulos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.ProcesoGestion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.Util;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que inserta una instrucción de pago, asociada a una transacción de venta de titulos
 * @author elaucho
 */
public class InstruccionesVentaTitulosInsert extends MSCModelExtend{

	@Override
	public void execute() throws Exception {

	//DAO a utilizar 
	GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);

	//Arreglos a utilizar
	String nombreArray[] 						= _req.getParameterValues("nombre");
	String cedulaArray[] 						= _req.getParameterValues("cedula");
	String montoArray[] 						= _req.getParameterValues("monto");
	String nombreTransferenciaNacionalArray[] 	= _req.getParameterValues("nombre_transferir_nacional");
	String cedulaTransferenciaNacionalArray[] 	= _req.getParameterValues("cedula_transferir_nacional");
	String montoTransferenciaNacionalArray[] 	= _req.getParameterValues("monto_transferir_nacional");
	String cuentaNumeroNacionalarray[]			= _req.getParameterValues("cuenta_numero_nacional");
	String ctectaNumeroArray[]					= _req.getParameterValues("ctecta_numero");
	String montoInternacionalArray[]			= _req.getParameterValues("monto_internacional");
	String ctectaNumeroInternacionalArray[]		= _req.getParameterValues("ctecta_numero");
	String ctectaBcoctaAbaArray[]				= _req.getParameterValues("ctecta_bcocta_aba");
	String ctectaBcoctaBcoArray[]				= _req.getParameterValues("ctecta_bcocta_bco");		
	String ctectaBcoctaBicArray[]				= _req.getParameterValues("ctecta_bcocta_bic");
	String ctectaBcoctaDireccionArray[]			= _req.getParameterValues("ctecta_bcocta_direccion");
	String ctectaBcoctaTelefonoArray[]			= _req.getParameterValues("ctecta_bcocta_telefono");
	String observacionArray[]					= _req.getParameterValues("observacion");
	String ctectaBcointAbaArray[]				= _req.getParameterValues("ctecta_bcoint_aba");
	String ctectaBcointBcoArray[]				= _req.getParameterValues("ctecta_bcoint_bco");
	String ctectaBcointBicArray[]				= _req.getParameterValues("ctecta_bcoint_bic");
	String ctectaBcointDireccionArray[]			= _req.getParameterValues("ctecta_bcoint_direccion");
	String ctectaBcointTelefonoArray[]			= _req.getParameterValues("ctecta_bcoint_telefono");
	String observacionIntermediarioArray[]  	= _req.getParameterValues("observacion_intermediario");
	String nombreInternacionalArray[]			= _req.getParameterValues("nombre_internacional");
	String cedulaInternacionalArray[]			= _req.getParameterValues("cedula_internacional");
	String ctaBcoBcoint[]					    = _req.getParameterValues("cta_bco_bcoint");
	
	
	DataSet _operacionesProceso	= new DataSet();
	gestionPagoDAO.listarOperacionesVentaTitulos(Long.parseLong(_req.getSession().getAttribute("client_id").toString()),_req.getSession().getAttribute("moneda").toString(),_req.getSession().getAttribute("instruccion_pago").toString());
	_operacionesProceso = gestionPagoDAO.getDataSet();
	
try {

	//Query para la tabla 810.
	ProcesoGestion procesoGestion = new ProcesoGestion();
	procesoGestion.setClientId(Long.parseLong(_req.getSession().getAttribute("client_id").toString()));
	procesoGestion.setUsuarioId(Long.parseLong(gestionPagoDAO.idUserSession(_req.getSession().getAttribute("framework.user.principal").toString())));


	//Se procesan las instrucciones de pago para ser canceladas con cheque
	if(nombreArray!=null){
		
		Date fechaActual = new Date();
		InstruccionesPago instruccionesPago = null;
		for(int i=0;i<nombreArray.length;i++){
			
			instruccionesPago = new InstruccionesPago();
			instruccionesPago.setProcesoId(procesoGestion.getProcesoID());
			instruccionesPago.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			instruccionesPago.setCedulaBeneficiario(cedulaArray[i]);
			instruccionesPago.setNombrebeneficiario(nombreArray[i]);
			instruccionesPago.setMontoInstruccion(new BigDecimal(montoArray[i]));
			instruccionesPago.setMonedaId(_req.getSession().getAttribute("moneda").toString());//Pendiente
			instruccionesPago.setFechaOperacion(fechaActual);
			instruccionesPago.setFechaValor(fechaActual);//Pendiente
			instruccionesPago.setTipoInstruccionId(TipoInstruccion.CHEQUE);
			instruccionesPago.setTasaCambio(new BigDecimal(0));

			procesoGestion.agregarInstruccion(instruccionesPago);

		}//fin for
	}//FIN IF


	//Se procesan las instrucciones de pago que seran canceladas por transferencia Nacional 
	if(nombreTransferenciaNacionalArray!=null){
		
		Date fechaActual = new Date();
		InstruccionesPago instruccionesPago = new InstruccionesPago();
		
		for(int i=0;i<nombreTransferenciaNacionalArray.length;i++){
			instruccionesPago.setProcesoId(procesoGestion.getProcesoID());
			instruccionesPago.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			instruccionesPago.setCedulaBeneficiario(cedulaTransferenciaNacionalArray[i]);
			instruccionesPago.setNombrebeneficiario(nombreTransferenciaNacionalArray[i]);
			instruccionesPago.setMontoInstruccion(new BigDecimal(montoTransferenciaNacionalArray[i]));
			instruccionesPago.setFechaOperacion(fechaActual);
			instruccionesPago.setFechaValor(fechaActual);//Pendiente
			//instruccionesPago.setClienteCuentaNombre(cuentaNombreNacionalarray[i]);
			instruccionesPago.setClienteCuentaNumero(cuentaNumeroNacionalarray[i]);
			instruccionesPago.setTasaCambio(new BigDecimal(0));

			//Se verifica si la operacion creada es de cambio
			
			if(_req.getSession().getAttribute("operacion_cambio").toString().equalsIgnoreCase("true")){

				instruccionesPago.setTipoInstruccionId(TipoInstruccion.OPERACION_DE_CAMBIO);
				instruccionesPago.setTasaCambio(new BigDecimal(_req.getSession().getAttribute("tasa_servicio").toString()));
				MonedaDAO monedaDAO = new MonedaDAO(_dso);
				instruccionesPago.setMonedaId(monedaDAO.listarIdMonedaLocal());//Pendiente
				
			}else{
				
				instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_NACIONAL);
				instruccionesPago.setMonedaId(_req.getSession().getAttribute("moneda").toString());//Pendiente
			}

			//Se agrega la instruccion al proceso
			procesoGestion.agregarInstruccion(instruccionesPago);
			
		}//fin for
	}//FIN IF
	

	//Se procesan las instrucciones de pago que seran canceladas por transferencia internacional
	if(ctectaNumeroArray!=null){
		
		Date fechaActual = new Date();
		InstruccionesPago instruccionesPago = new InstruccionesPago();
		for(int i=0;i<ctectaNumeroArray.length;i++){
			instruccionesPago.setProcesoId(procesoGestion.getProcesoID());
			instruccionesPago.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			instruccionesPago.setCedulaBeneficiario(cedulaInternacionalArray[i]);
			instruccionesPago.setNombrebeneficiario(nombreInternacionalArray[i]);
			instruccionesPago.setMontoInstruccion(new BigDecimal(montoInternacionalArray[i]));
			instruccionesPago.setMonedaId(_req.getSession().getAttribute("moneda").toString());
			instruccionesPago.setFechaOperacion(fechaActual);
			instruccionesPago.setFechaValor(fechaActual);//Pendiente
			instruccionesPago.setClienteCuentaNumero(ctectaNumeroInternacionalArray[i]);
			instruccionesPago.setCtectaBcoctaAba(ctectaBcoctaAbaArray[i]);
			instruccionesPago.setCtectaBcoctaBco(ctectaBcoctaBcoArray[i]);
			instruccionesPago.setCtectaBcoctaBic(ctectaBcoctaBicArray[i]);
			instruccionesPago.setCtectaBcoctaDireccion(ctectaBcoctaDireccionArray[i]==null?"":ctectaBcoctaDireccionArray[i]);
			instruccionesPago.setCtectaBcoctaTelefono(ctectaBcoctaTelefonoArray[i]==null?"":ctectaBcoctaTelefonoArray[i]);
			instruccionesPago.setObservacionCtectaBcocta(observacionArray[i]);
			instruccionesPago.setCtectaBcointAba(ctectaBcointAbaArray[i]);
			instruccionesPago.setCtectaBcointBco(ctectaBcointBcoArray[i]);
			instruccionesPago.setCtectaBcointBic(ctectaBcointBicArray[i]);
			instruccionesPago.setCtectaBcointDireccion(ctectaBcointDireccionArray[i]==null?"":ctectaBcointDireccionArray[i]);
			instruccionesPago.setCtectaBcointSwift(ctaBcoBcoint[i]);
			instruccionesPago.setCtectaBcointTelefono(ctectaBcointTelefonoArray[i]==null?"":ctectaBcointTelefonoArray[i]);
			instruccionesPago.setObservacionCtectaBcoint(observacionIntermediarioArray[i]);
			instruccionesPago.setTipoInstruccionId(TipoInstruccion.CUENTA_SWIFT);
			instruccionesPago.setTasaCambio(new BigDecimal(0));
			procesoGestion.agregarInstruccion(instruccionesPago);
			
			
		}//fin for
	}//fin transferencias internacionales
	

	//Se realiza la insercion de las operaciones y se atan al proceso 
	if(_operacionesProceso.count()>0){
		_operacionesProceso.first();
		
		while(_operacionesProceso.next()){
			
			procesoGestion.agregarOperacion(Long.parseLong( _operacionesProceso.getValue("ordene_operacion_id")));
			
		}//FIN WHILE
	}//FIN IF
	
	ArrayList<String>sqls = gestionPagoDAO.insertarProceso(procesoGestion,_app,getUserName(),_req.getRemoteAddr(),_req.getSession().getAttribute("moneda").toString());
	
	//Pasamos el ArrayList A Array de String
	String sqlsFinalesArray[] = new String[sqls.size()];
	sqls.toArray(sqlsFinalesArray);

	//Se ejecuta el execbatch
	db.execBatch(_dso, sqlsFinalesArray);
	

	//Removemos datos de sesion
	_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.using-page");
	_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.record");
	_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.current-page");
	_req.getSession().removeAttribute("infi.operaciones.procesos");
	_req.getSession().removeAttribute("infi.monto.operaciones");
	_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.query-string");
	_req.getSession().removeAttribute("client_id");
	_req.getSession().removeAttribute("datasetParam");
	_req.getSession().removeAttribute("infi.banco.instrucciones");
	_req.getSession().removeAttribute("infi.transferencias");
	_req.getSession().removeAttribute("infi.cheques");
	_req.getSession().removeAttribute("infi.banco.instrucciones");
	_req.getSession().removeAttribute("seleccion");
	

		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw e;
		}finally{
			
		}//FIN FINALLY
		
	}//fin execute
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
	
		if (flag)
	{	
		String nombreArray[] 				= _req.getParameterValues("nombre");
		String cedulaArray[] 				= _req.getParameterValues("cedula");
		String montoArray[] 				= _req.getParameterValues("monto");
		String nombreTransferenciaArray[] 	= _req.getParameterValues("nombre_transferir_nacional");
		String cedulaTransferenciaArray[] 	= _req.getParameterValues("cedula_transferir_nacional");
		String montoTransferenciaArray[] 	= _req.getParameterValues("monto_transferir_nacional");
		String montoInternacionalArray[]	= _req.getParameterValues("monto_internacional");
		String cuentaNumeroNacionalarray[]			= _req.getParameterValues("cuenta_numero_nacional");
		BigDecimal montoOperaciones 		= new BigDecimal(0);

		BigDecimal montoOperacionesSession  = new BigDecimal(Util.replace(Util.replace(_req.getSession().getAttribute("infi.monto.operaciones")!=null && _req.getSession().getAttribute("infi.monto.operaciones.cambio")==null || _req.getSession().getAttribute("infi.monto.operaciones.cambio").toString().equalsIgnoreCase("") ?_req.getSession().getAttribute("infi.monto.operaciones").toString():_req.getSession().getAttribute("infi.monto.operaciones.cambio").toString(), ".", "."), ",", "."));

		//Valida el nombre del cheque
		if(nombreArray!=null){
		for(int i=0;i<nombreArray.length;i++)
		{
			if(nombreArray[i]==null||nombreArray[i].equals("")){
				int registro = i+1;
				_record.addError("Nombre del Beneficiario/Cheque", "Es obligatorio para el registro "+registro);
				flag = false;
			
			}//fin if
		}//fin for
		

		//Valida la cedula del Cheque
		for(int i=0;i<cedulaArray.length;i++)
		{
			if(cedulaArray[i]==null||cedulaArray[i].equals("")){
				int registro = i+1;
				_record.addError("C&eacute;dula del Beneficiario/Cheque", "Es obligatorio para el registro "+registro);
				flag = false;
			
			}//fin if
		}//fin for		

		//Valida el monto de la Operacion del Cheque
		for(int i=0;i<montoArray.length;i++)
		{
			montoOperaciones = montoOperaciones.add(montoArray[i]!=null&&!montoArray[i].equals("")?new BigDecimal(montoArray[i]):new BigDecimal(0));
			if(montoArray[i]==null||montoArray[i].equals("")){
				int registro = i+1;
				_record.addError("Monto/Cheque", "Es obligatorio para el registro "+registro);
				flag = false;
			
			}//fin if
		}//fin for
		}

		//Valida el nombre para la transferencia
		if(nombreTransferenciaArray!=null){
		for(int i=0;i<nombreTransferenciaArray.length;i++)
		{
			if(nombreTransferenciaArray[i]==null||nombreTransferenciaArray[i].equals("")){
				int registro = i+1;
				_record.addError("Nombre/transferencia", "Es obligatorio para el registro "+registro);
				flag = false;
			
			}//fin if
		}//fin for

		//Valida la cedula para la transferencia
		for(int i=0;i<cedulaTransferenciaArray.length;i++)
		{
			if(cedulaTransferenciaArray[i]==null||cedulaTransferenciaArray[i].equals("")){
				int registro = i+1;
				_record.addError("C&eacute;dula/transferencia", "Es obligatorio para el registro "+registro);
				flag = false;
			
			}//fin if
		}//fin for

		//Valida el monto de la Transferencia
		for(int i=0;i<montoTransferenciaArray.length;i++)
		{
			montoOperaciones = montoOperaciones.add(montoTransferenciaArray[i]!=null&&!montoTransferenciaArray[i].equals("")?new BigDecimal(montoTransferenciaArray[i]):new BigDecimal(0));
			if(montoTransferenciaArray[i]==null||montoTransferenciaArray[i].equals("")){
				int registro = i+1;
				_record.addError("Monto/transferencia", "Es obligatorio para el registro "+registro);
				flag = false;
			
			}//fin if
		}//fin for
		//Valida el monto de la Transferencia

			if(cuentaNumeroNacionalarray==null||cuentaNumeroNacionalarray.equals("")){
				_record.addError("Cuenta/transferencia", "Es obligatorio para realizar la transacci&oacute;n");
				flag = false;
			
			}//fin if

		}//fin if
		
		if(montoInternacionalArray!=null){
			

			//Valida el monto de la Transferencia Internacional
			for(int i=0;i<montoInternacionalArray.length;i++)
			{
				montoOperaciones = montoOperaciones.add(montoInternacionalArray[i]!=null&&!montoInternacionalArray[i].equals("")?new BigDecimal(montoInternacionalArray[i]):new BigDecimal(0));
				if(montoInternacionalArray[i]==null||montoInternacionalArray[i].equals("")){
					int registro = i+1;
					_record.addError("Monto/internacional", "Es obligatorio para el registro "+registro);
					flag = false;
				
				}//fin if
			}//fin for
		}
		/*
		 * Se valida que el monto ingresado en las intrucciones de pago sea igual al monto total de las
		 * operaciones involucradas
		 */
		if(montoOperaciones.compareTo(montoOperacionesSession)!=0){
			_record.addError("MONTO", "El total debe ser igual al monto de la Transacci&oacute;n");
			flag = false;
		}//fin if
	}//fin if
		return flag;	
	}//fin isValids
}
