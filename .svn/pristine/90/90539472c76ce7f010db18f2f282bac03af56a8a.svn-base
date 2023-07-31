package models.gestion_pago_cheque;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.logic.ValidacionInstruccionesPago;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de generar la orden y operacion de credito para el pago de cupones
 * Registra además informacion en data extendida, relaciona la orden con la instruccion de pago seleccionada,
 * actualiza el estatus de la orden y operaciones a PROCESADA y APLICADA respectivamente
 * @author NM25287
 */
public class GestionPagoInsert extends MSCModelExtend{
	private TransaccionFijaDAO transaccionFijaDAO;
	private OrdenDAO ordenDAO;
	private ClienteCuentasDAO clienteCuentasDAO;
	private OperacionDAO operacionDAO;
	private String seleccion="";
	private String idCuentaSwift="";
	private String productoCliente="";
	private String ctaNacDolares="";
	private CuentaCliente cuentaCliente= new CuentaCliente();
	
	@Override
	public void execute() throws Exception {
		ordenDAO=new OrdenDAO(_dso);
		clienteCuentasDAO=new ClienteCuentasDAO(_dso);		
		operacionDAO=new OperacionDAO(_dso);
		
		
		OrdenDataExt ordenDataExt=null;
		String codOpCredDolares=null;			
		ArrayList<String> querysEjecutar=new ArrayList();
		String codigoOperacion=null;
		int tipoInstruccionId=0;
		Date fechaValor=new Date();
		Date fechaActual=new Date();
		BigDecimal montoOperacion=null;
		String monedaId=null;
		String idsOperaciones=null;
		String idsOrdenes=null;		
		
		try {
			long clienteId=Long.parseLong(_req.getSession().getAttribute("client_id").toString());
			monedaId=_req.getSession().getAttribute("moneda").toString();
			montoOperacion=new BigDecimal(_req.getSession().getAttribute("infi.monto.operaciones").toString());
			idsOperaciones=_req.getSession().getAttribute("idOperac").toString();
			idsOrdenes=_req.getSession().getAttribute("idOrds").toString();
			
				
			Logger.debug(this, "---CREACIÓN DE ORDEN DE PAGO AL CLIENTE: "+_req.getSession().getAttribute("nombre").toString()+"---");
		
			if(seleccion.equalsIgnoreCase("internacional")){				
				tipoInstruccionId=TipoInstruccion.CUENTA_SWIFT;
			}else{
				tipoInstruccionId=TipoInstruccion.CUENTA_NACIONAL_DOLARES;
			}
			fechaValor=Utilitario.StringToDate(_req.getParameter("fecha_valor"), ConstantesGenerales.FORMATO_FECHA);
			
			Orden orden = new Orden();
			orden.setIdCliente(clienteId);
			orden.setStatus(StatusOrden.REGISTRADA);
			orden.setIdTransaccion(TransaccionNegocio.ORDEN_PAGO);
			orden.setFechaOrden(fechaActual);
			orden.setFechaValor(fechaValor);
			orden.setCarteraPropia(false);	
			orden.setTipoProducto("");
			orden.setSucursal((_req.getSession().getAttribute("CODIGO_SUCURSAL"))!=null?_req.getSession().getAttribute("CODIGO_SUCURSAL").toString():"");
			orden.setTerminal(_req.getRemoteAddr());
			orden.setNombreUsuario(getUserName());	
			orden.setTipoProducto(_req.getSession().getAttribute("tipo_producto_id").toString());
			orden.setCuentaCliente(productoCliente);	
			orden.setIdMoneda(monedaId);
			orden.setMonto(montoOperacion.doubleValue());
			
			OrdenOperacion ordenOperacion = new OrdenOperacion();
			ordenOperacion.setAplicaReverso(false);
			ordenOperacion.setDescripcionTransaccion("");
			ordenOperacion.setIdMoneda(monedaId);
			ordenOperacion.setMontoOperacion(montoOperacion);
			ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
			ordenOperacion.setFechaAplicar(fechaValor);
			ordenOperacion.setTasa(BigDecimal.ZERO);
			ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_CUPON));	
			ordenOperacion.setNombreOperacion("Orden de Pago de Cupones");
			
			//AGREGAR DATA EXT-TIPO_INSTRUCCION_PAGO
			ordenDataExt = new OrdenDataExt();
			ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
			ordenDataExt.setValor(String.valueOf(tipoInstruccionId));
			orden.agregarOrdenDataExt(ordenDataExt);			
			
			//DATOS PROPIOS DEL TIPO DE INSTRUCCIOM
			if(tipoInstruccionId==TipoInstruccion.CUENTA_SWIFT){
				Logger.debug(this, "Selección de pago SWIFT ");				
				orden.setTipoCuentaAbono(Integer.valueOf(ConstantesGenerales.ABONO_CUENTA_EXTRANJERO));
				
				ordenOperacion.setCodigoABA(cuentaCliente.getCtecta_bcocta_aba());		
				ordenOperacion.setCodigoSwiftBanco(cuentaCliente.getCtecta_bcocta_swift());	
				ordenOperacion.setCodigoBicBanco(cuentaCliente.getCtecta_bcocta_bic());	
				ordenOperacion.setNombreBanco(cuentaCliente.getCtecta_bcoint_bco());		
				ordenOperacion.setNombreReferenciaCuenta(cuentaCliente.getCtecta_nombre());
				ordenOperacion.setNumeroCuenta(cuentaCliente.getCtecta_numero());				
				ordenOperacion.setTelefonoBanco(cuentaCliente.getCtecta_bcocta_telefono());
				ordenOperacion.setCodigoOperacion(codigoOperacion);		
				
				//AGREGAR OPERACION A LA ORDEN
				orden.agregarOperacion(ordenOperacion);
				
				//AGREGAR QUERYS PARA LA INSERCION de ORDEN,OPERACION y DATA EXT
				querysEjecutar.addAll(Arrays.asList(ordenDAO.insertar(orden)));				
				cuentaCliente.setIdOrden(orden.getIdOrden());	
				
				//AGREGAR QUERYS PARA LA INSERCION de CUENTAS CLIENTE
				querysEjecutar.addAll(Arrays.asList(clienteCuentasDAO.insertarClienteCuentasOrd(cuentaCliente)));				
				
			}else
			{
				Logger.debug(this, "Abono a cuenta en dólares ");				
				//OBTENER EL CODIGO DE OPERACION PARA ABONO A CUENTA EN DOLARES		
				codOpCredDolares=obtenerTransaccionFijaCredCup();
				
				orden.setTipoCuentaAbono(Integer.valueOf(ConstantesGenerales.ABONO_CUENTA_NACIONAL));				
				ordenOperacion.setNumeroCuenta(ctaNacDolares);
				ordenOperacion.setCodigoOperacion(codOpCredDolares);
				
				//AGREGAR OPERACION A LA ORDEN
				orden.agregarOperacion(ordenOperacion);
				
				//AGREGAR QUERYS PARA LA INSERCION DE ORDEN,OPERACION y DATA EXT
				querysEjecutar.addAll(Arrays.asList(ordenDAO.insertar(orden)));						
			}
			
			Logger.debug(this, "ORDEN DE PAGO ID: "+orden.getIdOrden());
			
			//ACTUALIZAR ESTATUS DE OPERACIONES ASOCIADAS
			querysEjecutar.add(operacionDAO.actualizarEstatusOperacionesIn(idsOperaciones, ConstantesGenerales.STATUS_APLICADA));	
			
			//ACTUALIZAR ESTATUS DE ORDENES ASOCIADAS
			querysEjecutar.add(ordenDAO.actualizarEstatusOrdenesIn(idsOrdenes, StatusOrden.PROCESADA,orden.getIdOrden()));
			
			//EJECUCIÓN DE QUERYS
			ordenDAO.ejecutarStatementsBatch(querysEjecutar);		
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}

	}
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		Logger.debug(this, "isValid GestioPagoInsert");
		seleccion=_req.getParameter("seleccion");
		idCuentaSwift=_req.getParameter("id_cuenta_swift");
		productoCliente=_req.getParameter("prod_cliente");
		ctaNacDolares=_req.getParameter("cuenta_nac_dolares");
		
		if(seleccion==null||seleccion.equals("")){
			_record.addError("Error", "Debe seleccionar un tipo de Pago");
			flag=false;
		}
		if(seleccion.equalsIgnoreCase("internacional")){
			
			if(productoCliente==null||productoCliente.equals("")){
				_record.addError("Error", "Debe seleccionar un Producto asociado al cliente");
				flag=false;
			}
			if(idCuentaSwift==null||idCuentaSwift.equals("")){
				_record.addError("Error", "Debe seleccionar una Instruci&oacute;n de Pago SWIFT");
				flag=false;
			}else{
				//Validar campos de la instruccion de pago SWIFT
				ValidacionInstruccionesPago valInst = new ValidacionInstruccionesPago(_dso);
				HashMap<String, String> parametrosEntrada = new HashMap<String, String>();
				
				cuentaCliente.setIdInstruccion(Long.parseLong(idCuentaSwift));
				cuentaCliente.setCtecta_bcocta_aba(_req.getParameter("ctecta_bcocta_aba"));
				cuentaCliente.setCtecta_bcocta_bic(_req.getParameter("ctecta_bcocta_bic"));
				cuentaCliente.setCtecta_bcocta_swift(_req.getParameter("ctecta_bcocta_bic"));
				cuentaCliente.setCtecta_bcocta_bco(_req.getParameter("ctecta_bcocta_bco"));
				cuentaCliente.setCtecta_nombre(_req.getSession().getAttribute("nombre").toString());
				cuentaCliente.setCtecta_numero(_req.getParameter("ctecta_numero"));
				cuentaCliente.setCtecta_bcocta_telefono(_req.getParameter("ctecta_bcocta_telefono"));
				cuentaCliente.setCodCiudadOrigen(_req.getParameter("cod_ciudad_origen"));
				cuentaCliente.setCodEstadoOrigen(_req.getParameter("cod_estado_origen"));
				cuentaCliente.setNombre_beneficiario(_req.getParameter("nombre_beneficiario"));	
				
				parametrosEntrada.put(ValidacionInstruccionesPago.TIPO_INSTRUCCION, String.valueOf(TipoInstruccion.CUENTA_SWIFT));
				parametrosEntrada.put(ValidacionInstruccionesPago.NUMERO_CUENTA_EXTRANJERA, cuentaCliente.getCtecta_numero());
				parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BANCO_EXTRANJERO, cuentaCliente.getCtecta_bcocta_bco());
				parametrosEntrada.put(ValidacionInstruccionesPago.ABA_BANCO, cuentaCliente.getCtecta_bcocta_aba());
				parametrosEntrada.put(ValidacionInstruccionesPago.BIC_BANCO, cuentaCliente.getCtecta_bcocta_bic());
				parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BENEFICIARIO, cuentaCliente.getNombre_beneficiario());
				parametrosEntrada.put(ValidacionInstruccionesPago.ESTADO, cuentaCliente.getCodEstadoOrigen());
				parametrosEntrada.put(ValidacionInstruccionesPago.CIUDAD, cuentaCliente.getCodCiudadOrigen());	
				
				valInst.setParametrosEntrada(parametrosEntrada);
				
				ArrayList<String> listaMensajes = valInst.validadorMensajeSWIFT();
				// Verificar lista de errores
				if (listaMensajes.size() != 0) {
					for (int k = 0; k < listaMensajes.size(); k++) {
						_record.addError("Debe verificar su instruccion de pago SWIFT", (String) listaMensajes.get(k));
					}
					flag=false;
				}
			}
			
		}else
		{
			if(ctaNacDolares==null||ctaNacDolares.equals("")){
				_record.addError("Error", "Debe seleccionar una Cuenta Nacional en D&oacute;lares");
				flag=false;
			}
		}	
		
		
		return flag;	
	}
	
	public String obtenerTransaccionFijaCredCup() throws Exception{
		transaccionFijaDAO=new TransaccionFijaDAO(_dso);
		transaccionFijaDAO.listar(TransaccionFija.DEPOSITO_CUPON);
		DataSet ds = transaccionFijaDAO.getDataSet();
							
		if(ds.count()>0){
			ds.first();
			ds.next();
			
			if(ds.getValue("CODIGO_OPERACION")!=null&&!ds.getValue("CODIGO_OPERACION").equals("")&&!ds.getValue("CODIGO_OPERACION").equals("0")){
				return ds.getValue("CODIGO_OPERACION");	
			}else
			{
				throw new Exception("No se encuentran registrados los códigos de operacion para PAGO DE CUPONES");
			}
						
		}else{
			throw new Exception("No se encuentran registrados los códigos de operacion para PAGO DE CUPONES");
		}
		
	}
}
