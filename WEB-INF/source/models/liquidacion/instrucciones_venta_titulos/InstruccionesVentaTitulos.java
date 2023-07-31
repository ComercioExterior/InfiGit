package models.liquidacion.instrucciones_venta_titulos;

import java.math.BigDecimal;
import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.TCCCFMT;
import com.bdv.infi.webservices.manager.ManejadorTasaCambio;
/**
 * 
 * @author CT09153
 *
 */
public class InstruccionesVentaTitulos extends MSCModelExtend{

	@Override
public void execute() throws Exception {

	//Variables y dataset a utilizar
	String controlCambio = ParametrosDAO.listarParametros(ConstantesGenerales.CONTROL_DE_CAMBIO,_dso);
	DataSet _cheques 						= new DataSet();
	DataSet _transferencias 				= new DataSet();
	String registroCheque 					= "";
	String registrotransferenciasNacionales = "<tr class='tableCell'><td nowrap align='center'><input type='text' value='' name='nombre_transferir_nacional' maxlength=\"100\"></td><td nowrap align='center'><input type='text' value='' name='cedula_transferir_nacional' onkeypress=\"solo_numericos()\" maxlength=\"10\"></td><td nowrap align='center'><input type='text' value='' name='cuenta_numero_nacional' maxlength=\"34\"></td><td nowrap align='center'><input type='text' value='' name='cuenta_nombre_nacional' maxlength=\"50\"></td><td nowrap align='center'><input type='text' value='' name='monto_transferir_nacional'  onkeypress=\"EvaluateText('%f',this)\" maxlength='18'></td></tr>";
	String seleccion 						= _req.getParameter("seleccion")==null?_req.getSession().getAttribute("seleccion").toString()!=null?_req.getSession().getAttribute("seleccion").toString():_req.getParameter("seleccion"):_req.getParameter("seleccion");
											  _req.getSession().setAttribute("seleccion",seleccion);
											  _cheques.append("cheques",java.sql.Types.VARCHAR);
											  _transferencias.append("transferencias_nacionales",java.sql.Types.VARCHAR);
	boolean operacionCambio 				= false;							
	String monedaSesion  					= _req.getSession().getAttribute("moneda").toString();
	MonedaDAO monedaDAO  					= new MonedaDAO(_dso);
	String monedaLocal   					= monedaDAO.listarIdMonedaLocal();
	
	
	//si el tipo de instruccion es operacion de cambio
	if(Integer.parseInt(_req.getSession().getAttribute("instruccion_pago").toString())==TipoInstruccion.OPERACION_DE_CAMBIO)
		operacionCambio=true;
	
	//Instruccion pago CHEQUE	
	if(seleccion.equals("cheque")){
		
		long cheques = Long.parseLong(_req.getParameter("numero_cheques")==null?_req.getSession().getAttribute("infi.cheques").toString():_req.getParameter("numero_cheques"));
					   _req.getSession().setAttribute("infi.cheques", cheques);
			
	   if(controlCambio.equals("1"))
	   {
		   //Buscamos el cliente
		   ClienteDAO cliente = new ClienteDAO(_dso);
		   String arrayClient[] = new String[1];
		   arrayClient[0]=_req.getSession().getAttribute("client_id").toString();
		   cliente.listarClientesPorId(arrayClient);
		   cliente.getDataSet().next();
		   registroCheque = "<tr class='tableCell'><td nowrap align='center'><input type='text' value='" +cliente.getDataSet().getValue("CLIENT_NOMBRE")+"' name='nombre' size='40' maxlength=\"100\" readonly></td><td nowrap align='center'><input type='text' value='" +cliente.getDataSet().getValue("CLIENT_CEDRIF") +"' name='cedula' onkeypress=\"solo_numericos()\" maxlength=\"10\" align='right' readonly></td><td nowrap align='center'><input type='text' value='" +_req.getSession().getAttribute("infi.monto.operaciones").toString() +"' name='monto' onkeypress=\"EvaluateText('%f',this)\" maxlength='18' readonly></td></tr>";
	   
	   }else{
		   registroCheque = "<tr class='tableCell'><td nowrap align='center'><input type='text' value='' name='nombre' maxlength=\"100\"></td><td nowrap align='center'><input type='text' value='' name='cedula' onkeypress=\"solo_numericos()\" maxlength=\"10\"></td><td nowrap align='center'><input type='text' value='" +_req.getSession().getAttribute("infi.monto.operaciones").toString() +"' name='monto' onkeypress=\"EvaluateText('%f',this)\" maxlength='18' readonly></td></tr>";
	   }
	   if(cheques!=0){
			for(int i =0;i<cheques;i++){
				_cheques.addNew();
				_cheques.setValue("cheques", registroCheque);
			}//fin for
		}//fin if

	//Instruccion pago NACIONAL moneda Local
	}else if(seleccion.equals("nacionales") && monedaSesion.equalsIgnoreCase(monedaLocal)){
		
		long transferenciasNacionales = Long.parseLong(_req.getParameter("numero_transferencias_nacionales")==null?_req.getSession().getAttribute("infi.transferencias").toString():_req.getParameter("numero_transferencias_nacionales"));
										_req.getSession().setAttribute("infi.transferencias", transferenciasNacionales);
										
		if(transferenciasNacionales!=0){
			for(int i =0;i<transferenciasNacionales;i++){
				_transferencias.addNew();
				_transferencias.setValue("transferencias_nacionales", registrotransferenciasNacionales);
			}//fin for
		}//fin if	
	//Instruccion pago INTERNACIONAL		
	}else if(seleccion.equals("internacionales")){
		
		long numeroTransferenciasInternacionales = Long.parseLong(_req.getParameter("numero_transferencias_internacionales")==null?_req.getSession().getAttribute("infi.transferencias.internacionales").toString():_req.getParameter("numero_transferencias_internacionales"));
												   _req.getSession().setAttribute("infi.transferencias.internacionales", numeroTransferenciasInternacionales);

	//Instruccion pago NACIONAL moneda Extranjera												 
	}else if(seleccion.equals("nacionales") && !monedaSesion.equalsIgnoreCase(monedaLocal)){

		
		long transferenciasNacionales = Long.parseLong(_req.getParameter("numero_transferencias_nacionales")==null?_req.getSession().getAttribute("infi.transferencias").toString():_req.getParameter("numero_transferencias_nacionales"));
										_req.getSession().setAttribute("infi.transferencias", transferenciasNacionales);
										
		if(transferenciasNacionales!=0){
			for(int i =0;i<transferenciasNacionales;i++){
				_transferencias.addNew();
				_transferencias.setValue("transferencias_nacionales", registrotransferenciasNacionales);
			}//fin for
		}//fin if	
		operacionCambio= true;
		
	}
	
	DataSet _transferenciasInternacionales =_req.getSession().getAttribute("infi.banco.instrucciones")==null?new DataSet():(DataSet) _req.getSession().getAttribute("infi.banco.instrucciones");
	storeDataSet("cheques",_cheques);
	storeDataSet("transferencias",_transferencias);
	storeDataSet("transferencias_internacionales", _transferenciasInternacionales);
	

	//Se trae de session el total del monto de las operaciones pendientes
	BigDecimal montoOperaciones = _req.getSession().getAttribute("infi.monto.operaciones")!=null?new BigDecimal(Util.replace(_req.getSession().getAttribute("infi.monto.operaciones").toString(),",",".")):new BigDecimal(0);
	montoOperaciones.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	String moneda               = "";
	
		if(_req.getParameter("moneda")!=null){
			String montoMoneda[] = Util.split(_req.getParameter("moneda"),"-");
			moneda               = montoMoneda[0];
			montoOperaciones     = new BigDecimal(montoMoneda[1]).setScale(2,BigDecimal.ROUND_HALF_EVEN);
			_req.getSession().setAttribute("moneda",moneda);
		}
	DataSet _operacionesmonto = new DataSet();
	_operacionesmonto.append("operaciones_monto",java.sql.Types.VARCHAR);
	_operacionesmonto.append("monto_operaciones_sin_formato", java.sql.Types.VARCHAR);
	_operacionesmonto.append("seleccion", java.sql.Types.VARCHAR);
	_operacionesmonto.append("moneda", java.sql.Types.VARCHAR);
	_operacionesmonto.addNew();
	

	//Si la transaccion es de operacion de cambio, se busca la tasa y se le muestra al cliente el monto en moneda local
	if(operacionCambio){
		
		//Credenciales del usuario
		CredencialesDeUsuario credencialesDeUsuario = new CredencialesDeUsuario();
		credencialesDeUsuario.setNombreDeUsuario("");
		credencialesDeUsuario.setClaveSecreta("");
		
		// Objeto que retorna el metodo a invocar TCMCCC1
		TCCCFMT tcmccc1 = new TCCCFMT();
		
		//Invocacion al WS de Tasas de Cambio	
		ManejadorTasaCambio manejadorTasaCambio = new ManejadorTasaCambio(_app,credencialesDeUsuario);
		tcmccc1 = manejadorTasaCambio.getTasaCambio(monedaSesion, _req.getUserPrincipal().toString(),_req.getRemoteAddr());
		String tasaVenta = tcmccc1.getVenta().toString();
		//String tasaVenta = "2.1415";
		
		BigDecimal montoCambio = new BigDecimal(tasaVenta).setScale(4, BigDecimal.ROUND_HALF_EVEN);
		montoCambio 		   = montoCambio.multiply(montoOperaciones).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		_operacionesmonto.setValue("operaciones_monto",Utilitario.formatearNumero(String.valueOf(montoCambio), "###,###,###,###.00"));
		_req.getSession().setAttribute("infi.monto.operaciones",montoOperaciones.setScale(2, BigDecimal.ROUND_HALF_EVEN));
		_req.getSession().setAttribute("infi.monto.operaciones.cambio",montoCambio.setScale(2, BigDecimal.ROUND_HALF_EVEN));
		_operacionesmonto.setValue("monto_operaciones_sin_formato",_req.getSession().getAttribute("infi.monto.operaciones.cambio").toString());
		
		//Si la transaccion es operacion de cambio se monta en sesion true y la tasa que responde el servicio
		_req.getSession().setAttribute("operacion_cambio", "true");
		_req.getSession().setAttribute("tasa_servicio", tasaVenta);
		_operacionesmonto.setValue("moneda",monedaLocal);
	}else{
		_operacionesmonto.setValue("operaciones_monto",Utilitario.formatearNumero(String.valueOf(montoOperaciones), "###,###,###,###.00"));
		_req.getSession().setAttribute("infi.monto.operaciones",montoOperaciones.setScale(2, BigDecimal.ROUND_HALF_EVEN));
		_operacionesmonto.setValue("moneda", _req.getSession().getAttribute("moneda").toString());
		_req.getSession().setAttribute("operacion_cambio", "false");
	}
	_operacionesmonto.setValue("seleccion", seleccion);
	storeDataSet("operaciones_monto", _operacionesmonto);
	

	// Seleccion del html por instrucción
	if(seleccion.equals("cheque")){
		_config.template = "cheque.htm";
	}else if(seleccion.equals("nacionales")){
		_config.template = "nacional.htm";
	}else if(seleccion.equals("internacionales")){
		_config.template = "table.htm";
	}
	
	//Se publica el nombre del usuario	
	DataSet _nombreCliente = new DataSet();
	_nombreCliente.append("nombre",java.sql.Types.VARCHAR);
	_nombreCliente.addNew();
	_nombreCliente.setValue("nombre", _req.getSession().getAttribute("nombre").toString());
	storeDataSet("nombre",_nombreCliente);

	//Si existe control de cambio el beneficiario es el mismo cliente
	DataSet _cuentasCliente = new DataSet();
	DataSet mensajes = new DataSet();
	mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);
	
	DataSet _cedulaCliente = new DataSet();
	_cedulaCliente.append("tipoPersona",java.sql.Types.VARCHAR);
	_cedulaCliente.append("cedula",java.sql.Types.VARCHAR);
	
	if(controlCambio.equals("1"))
	{
		Utilitario utilitario = new Utilitario();
		
		//Buscamos el tipo de persona y la cedula del cliente
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		clienteDAO.listarPorId(_req.getSession().getAttribute("client_id").toString());
		String cedulaCliente = null;
		String tipoPersona = null;
		
		if(clienteDAO.getDataSet().count()>0)
		{
			
			cedulaCliente = clienteDAO.getDataSet().getValue("client_cedrif");
			tipoPersona = clienteDAO.getDataSet().getValue("tipper_id");
			
			_cedulaCliente.addNew();
			_cedulaCliente.setValue("tipoPersona", tipoPersona);
			_cedulaCliente.setValue("cedula", cedulaCliente);
		}

		_cuentasCliente = utilitario.buscarCuentasCliente(getUserName(), cedulaCliente, tipoPersona, _req.getRemoteAddr(), _app, _req, mensajes);
		
		if(mensajes.count()==0)
		{
			mensajes.addNew();
			mensajes.setValue("mensaje_error_cuentas_cte", "&nbsp;");
		}
		
	}else
	{
		if(seleccion.equals("nacionales")){
			
			_config.template = "nacionalBeneficiario.htm";
		}
	}
	//Se publican los dataset
	storeDataSet("cuentas_clientes",_cuentasCliente);
	storeDataSet("mensajes",mensajes);
	storeDataSet("cedulaCliente",_cedulaCliente);
}	//fin execute
	
	public boolean isValid() throws Exception
	{
		boolean flag 			= super.isValid();
		String moneda 		    = _req.getParameter("moneda")==null?_req.getSession().getAttribute("moneda")==null?"vacio":_req.getSession().getAttribute("moneda").toString():_req.getParameter("moneda");
		String transferencias 	= _req.getParameter("numero_transferencias_nacionales")==null?_req.getSession().getAttribute("infi.transferencias")==null?"vacio":_req.getSession().getAttribute("infi.transferencias").toString(): _req.getParameter("numero_transferencias_nacionales");
		String cheques			= _req.getParameter("numero_cheques")==null?_req.getSession().getAttribute("infi.cheques")==null?"vacio":_req.getSession().getAttribute("infi.cheques").toString():_req.getParameter("numero_cheques");
		String internacionales  = _req.getParameter("numero_transferencias_internacionales")==null?_req.getSession().getAttribute("infi.transferencias.internacionales")==null?"vacio":_req.getSession().getAttribute("infi.transferencias.internacionales").toString():_req.getParameter("numero_transferencias_internacionales");
		String seleccion 		= _req.getParameter("seleccion")==null?_req.getSession().getAttribute("seleccion")==null?"vacio":_req.getSession().getAttribute("seleccion").toString():_req.getParameter("seleccion");
		
		if (flag)
		{		
			if(seleccion==null || seleccion.equalsIgnoreCase("vacio")){
				_record.addError("Instruccion de Pago", "Debe seleccionar una Instrucci&oacute;n para continuar");
				flag = false;
			
			}
			
			if(moneda==null || moneda.equalsIgnoreCase("vacio")){
				_record.addError("Operaci&oacute;n", "Debe seleccionar una Operaci&oacute;n para continuar");
				flag = false;
			
			}
			if(seleccion!=null || seleccion.equalsIgnoreCase("vacio")){
				
				if(seleccion.equalsIgnoreCase("cheque")){
					if(cheques==null || cheques.equalsIgnoreCase("vacio")){
						_record.addError("Cheques", "Debe seleccionar la cantidad de cheques");
						flag = false;
					}
				}
				if(seleccion.equalsIgnoreCase("nacionales")){
					if(transferencias==null || transferencias.equalsIgnoreCase("vacio")){
						_record.addError("Nacionales", "Debe seleccionar la cantidad de transferencias");
						flag = false;
					}
				}
				if(seleccion.equalsIgnoreCase("internacionales")){
					if(internacionales==null || internacionales.equalsIgnoreCase("vacio")){
						_record.addError("Internacionales", "Debe seleccionar la cantidad de transferencias");
						flag = false;
					}
				}
			}
		}//fin if
		return flag;	
	}//fin isValids
}
