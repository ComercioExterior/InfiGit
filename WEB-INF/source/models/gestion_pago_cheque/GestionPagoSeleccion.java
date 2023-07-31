package models.gestion_pago_cheque;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.data.FechaValor;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que recibe la moneda y el monto involucrado, lo monta en sesion y muestra el html para seleccionar la instruccion de pago
 * @author elaucho
 */
public class GestionPagoSeleccion extends MSCModelExtend{
	
	
	@Override
	public void execute() throws Exception {
		ManejadorDeClientes manejadorCliente=null;
		ArrayList<Cuenta> productos=new ArrayList<Cuenta>(); 
		ArrayList<Cuenta> cuentasNacionalesMonedaExt=new ArrayList<Cuenta>(); 
		DataSet productosCliente=new DataSet();
		DataSet instruccionesPagoCtaDolares=new DataSet();		
		DataSet instruccionesPagoSWIFT=new DataSet();
		ClienteCuentasDAO clienteCuentasDAO = null;
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		Long idCliente=(Long)_req.getSession().getAttribute("client_id");
		clienteCuentasDAO = new ClienteCuentasDAO(_dso);
		String ciRifCliente=null;
		String tipo_persona=null;
		//Variables para consulta de fecha Valor
		FechaValor objFechaValor = new FechaValor();
		FechaValorDAO fechaValorDAO = new FechaValorDAO(_dso);
		DataSet fecha_valor=new DataSet();
		
		//Removemos de sesion el monto de operaciones al cambio
		_req.getSession().removeAttribute("infi.monto.operaciones.cambio");
		System.out.println("idss operaciones: "+_req.getParameter("idOperac"));
		
		if(_req.getParameter("montoPago")!=null){			
			BigDecimal monto = new BigDecimal(Util.replace(_req.getParameter("montoPago"), ",", ".")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
			_req.getSession().setAttribute("infi.monto.operaciones",String.valueOf(monto));	
			
		}
		//Removemos de sesion la seleccion
		_req.getSession().removeAttribute("seleccion");		
		_req.getSession().setAttribute("tipo_producto_id",_req.getParameter("tipo_producto_id"));
		_req.getSession().setAttribute("moneda",_req.getParameter("moneda"));
		_req.getSession().setAttribute("idOperac",_req.getParameter("idOperac"));
		_req.getSession().setAttribute("idOrds",_req.getParameter("idOrds"));
		
		String moneda        = _req.getParameter("moneda")==null?_req.getSession().getAttribute("moneda")==null?"vacio":_req.getSession().getAttribute("moneda").toString():Util.split(_req.getParameter("moneda"),"-")[0];
		MonedaDAO monedaDAO  = new MonedaDAO(_dso);
		String monedaLocal   = monedaDAO.listarIdMonedaLocal();

		if(moneda.equalsIgnoreCase(monedaLocal)){
			_config.template = "local.htm";
		}
		clienteDAO.listarPorId(String.valueOf(idCliente));		
		clienteDAO.getDataSet().first();
		if(clienteDAO.getDataSet().next()){			
			ciRifCliente = clienteDAO.getDataSet().getValue("client_cedrif");
			//ciRifCliente = "6290572";//"10872994";//
			tipo_persona = clienteDAO.getDataSet().getValue("tipper_id");
		}
		
		objFechaValor = fechaValorDAO.listar(com.bdv.infi.logic.interfaces.FechaValor.PAGO_CUPON);
		Date fechaValor = objFechaValor.getFechaValor();
		
		if(fechaValor!=null){
			fecha_valor.addNew();
			fecha_valor.append("fecha_valor",java.sql.Types.VARCHAR);
			fecha_valor.setValue("fecha_valor",Utilitario.DateToString(fechaValor,"dd-MM-yyyy"));
		}
		
		//Busqueda de Cuentas nacionales en Dolares en persona (Altair)
		manejadorCliente= new ManejadorDeClientes(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		try {			
			//Busqueda de productos y cuentas del cliente 
			
			//NM32454 TTS-488 MANEJO CORRELATIVOS
			String numeroPersona= "0";
			productos = manejadorCliente.listarProductosCliente(ciRifCliente, tipo_persona, getUserName(), _req.getRemoteAddr(),numeroPersona); 	 //Cliente de pruebas "6290572"		
			cuentasNacionalesMonedaExt = manejadorCliente.listaDeCuentasDolares(productos, getUserName(), _req.getRemoteAddr());
			
			//Generar DataSets de productos y cuentas del cliente
			productosCliente=manejadorCliente.cargarDataSetProductosCuentas(productos,ciRifCliente, tipo_persona,_dso,true);
			instruccionesPagoCtaDolares=manejadorCliente.cargarDataSetProductosCuentas(cuentasNacionalesMonedaExt,ciRifCliente, tipo_persona,_dso,false);
			
		} catch (Exception e) {
			System.out.println("Error en la consulta de cuentas del cliente");
		}		
		//Busqueda de instrucciones de pago para la venta 
		clienteCuentasDAO.browseClienteCuentas(String.valueOf(idCliente), String.valueOf(TipoInstruccion.CUENTA_SWIFT),null,null,UsoCuentas.PAGO_DE_CUPONES);
		instruccionesPagoSWIFT=clienteCuentasDAO.getDataSet();
		
		
		//Se publica el nombre del usuario	
		DataSet _nombreCliente = new DataSet();
		_nombreCliente.append("nombre",java.sql.Types.VARCHAR);
		_nombreCliente.addNew();
		_nombreCliente.setValue("nombre", _req.getSession().getAttribute("nombre").toString());
		
		//Se publica el monto a aplicar
		DataSet _montoOperacion = new DataSet();
		_montoOperacion.append("monto",java.sql.Types.VARCHAR);
		_montoOperacion.addNew();
		_montoOperacion.setValue("monto", _req.getParameter("montoPago").toString());		
		
		storeDataSet("nombre",_nombreCliente);
		storeDataSet("montoOperacion",_montoOperacion);
		storeDataSet("productos_cliente",productosCliente);//Instrucciones de pago para cuentas nacional en dolares
		storeDataSet("instrucciones_cuenta_dolares",instruccionesPagoCtaDolares);//Instrucciones de pago para cuentas nacional en dolares
		storeDataSet("instrucciones_pago_swift",instruccionesPagoSWIFT);//Instrucciones de pago para ventas de titulos
		storeDataSet("fecha_valor",fecha_valor);//Fecha valor de pago de cupones
		
	}//fin execute		
	
	
}//fin clase
