package com.bdv.infi.logic.function.document;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import megasoft.DataSet;
import org.apache.log4j.Logger;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ConceptosDAO;
import com.bdv.infi.util.Utilitario;
/** 
 * Clase encargada del proceso de b&uacute;squeda de los datos necesarios para los documentos de salida en el proceso de venta
 */
public class FuncionPreOrden extends FuncionGenerica {
	
	/*** Variables globales privadas de la clase*/
	private Logger logger = Logger.getLogger(FuncionPreOrden.class);
	private HttpServletRequest _req;
	private java.lang.Object documentos;
	private ServletContext contexto;
	private String ip;
	private String nombreUsuario;
	private Map<String, String> mapa;
	
	public FuncionPreOrden(){
	}
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip, int ind) throws Exception{
		procesar(orden, documentos, contexto, ip);
	}
	
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip) throws Exception{
	}
	
	/**
	 * Método para procesar documentos cuando aún no se ha guardado una orden y los valores a sustituir provienen del request
	 * @param _req
	 * @param documentos
	 * @param contexto
	 * @param ip
	 * @param nombreUsuario
	 * @throws Exception
	 */
	public void procesar(HttpServletRequest _req, java.lang.Object documentos, ServletContext contexto, String ip, String nombreUsuario) throws Exception{
		
		this.mapa = new HashMap<String, String>();
		this._req = _req;
		this.documentos = documentos;
		this.contexto = contexto;
		this.ip = ip;
		this.nombreUsuario = nombreUsuario;	
	
		inicializarValoresMapa();
		obtenerDatosCliente();		
		obtenerDatosGenerales();		
		obtenerDatosRecompra();
		
		try{
			procesarPlantillas(mapa, (LinkedList)this.documentos);
		}catch (Exception e) {
			logger.error("Error en el proceso de documentos previos a la orden. " + e.getMessage()+ Utilitario.stackTraceException(e));			
			throw e;
		}
		
		//Este documento contendrá los siguientes datos:
	
		/*• Nombre del cliente
		•	Apellido del cliente
		•	Nacionalidad del cliente
		•	Estado civil del cliente natural
		•	Domicilio o dirección
		•	Cédula o rif
		•	Cuenta del cliente donde se efectuó la operación en la toma de orden
		•	Registro de información fiscal para el caso de cliente jurídicos
		•	Teléfono(s) del cliente
		•	Monto de la operación efectuada en la toma de orden
		•	Concepto de la toma de orden
		•	Día actual
		•	Mes actual en letras
		•	Mes actual en número
		•	Año
		•	Imagen del logo de cabecera y pie del Banco de Venezuela
		•	Datos Swift referentes a:
			o	Banco donde se efectuarán los depósitos
			o	Dirección del Banco
			o	ABA
			o	Nombre del beneficiario
			o	Cuenta del beneficiario
			o	Referencia
		*/
		
	}

	private void inicializarValoresMapa() {
		
		mapa.put("cliente", "");
		mapa.put("nacionalidad", "");
		mapa.put("estado_civil", "");
		mapa.put("direccion", "");
		mapa.put("cedula","");
		mapa.put("cuenta", "");
		mapa.put("telefono", "");
		mapa.put("monto_operacion", "");
		mapa.put("concepto", "");
		mapa.put("dia", "");
		mapa.put("mes_actual", "");
		mapa.put("mes", "");
		mapa.put("anio", "");
		mapa.put("banco", "");
		mapa.put("direccion_banco", "");
		mapa.put("cod_aba", "");
		mapa.put("beneficiario", "");
		mapa.put("cuenta_beneficiario", "");
		mapa.put("telefono_banco", "");
		mapa.put("cod_swift", "");
		mapa.put("referencia", "");	
		mapa.put("banco_intermediario", "");
		mapa.put("direccion_intermediario", "");
		mapa.put("cuenta_intermediario", "");
		mapa.put("telefono_intermediario", "");
		mapa.put("cod_aba_intermediario", "");
		mapa.put("cod_swift_intermediario", "");
	}

	/**
	 * Obtiene los datos asociados al cliente seleccionado para la toma de orden.
	 * @throws Exception
	 */
	protected void obtenerDatosCliente() throws Exception {
		
		ClienteDAO clienteDAO = new ClienteDAO(this.getDataSource());	
		DatosCliente datosCliente = new DatosCliente(this.getDataSource(),this.mapa);
		String cedula="";
		String tipo_persona="";

		//-Llenaremos el mapa con datos del cliente, datos adicionales seran buscados si es juridico o gubernamental (paso A)
		clienteDAO.listarPorId(_req.getParameter("client_id"));
		DataSet _cliente= clienteDAO.getDataSet();
		if(_cliente.count()>0){
			_cliente.first();_cliente.next();			
			cedula=_cliente.getValue("client_cedrif");
			tipo_persona = _cliente.getValue("tipper_id");			
		}
		//-Llenamos el mapa con datos del Cliente
		datosCliente.buscarDatos(cedula,tipo_persona,false,contexto, ip,nombreUsuario);
		
		//Cuenta
		mapa.put("cuenta", _req.getParameter("cta_cliente"));		
		
		//Estado Civil		
		if(_req.getParameter("estado_casado")!=null && _req.getParameter("estado_casado").equals("SI")){
			mapa.put("estado_civil", "Casado(a)");
		}else{
			mapa.put("estado_civil", "Soltero(a)");
		}

		
	}
	
	/**
	 * Obtiene los datos generales introducidos para la toma de orden, tales como monto, concepto, etc.
	 * @throws Exception 
	 */
	protected void obtenerDatosGenerales() throws Exception {
		ConceptosDAO conceptosDAO = new ConceptosDAO(this.getDataSource());
		
		//Buscar descripción del concepto
		conceptosDAO.listar(_req.getParameter("concepto"));		
		if(conceptosDAO.getDataSet().next()){
			mapa.put("concepto", conceptosDAO.getDataSet().getValue("concepto"));				
		}				
		//Monto total de la Operación
		mapa.put("monto_operacion", _req.getParameter("monto_total")+" "+_req.getParameter("id_moneda_local"));

	}
	
	/**
	 * Obtiene los datos asociados a la instrucción de pago para la recompra. 
	 * @throws Exception 
	 */
	protected void obtenerDatosRecompra() throws Exception {
		int idCliente = 0;
		DataSet cuentaSwiftCliente = new DataSet();
		ClienteCuentasDAO cuentaSwift = new ClienteCuentasDAO(this.getDataSource());
		long idInstruccionPago = 0;
		DatosInstruccionesPago datosInstruccionesPago = new DatosInstruccionesPago(this.getDataSource(), this.mapa);
		
		if(_req.getParameter("client_id")!=null && !_req.getParameter("client_id").equals("null") && !_req.getParameter("client_id").equals("")){
			idCliente = Integer.parseInt(_req.getParameter("client_id"));
		}
		cuentaSwift.listarCuentaSwift(idCliente);		
		cuentaSwiftCliente = cuentaSwift.getDataSet();
		
		if (cuentaSwiftCliente.next()){
			idInstruccionPago = Long.parseLong(cuentaSwiftCliente.getValue("ctes_cuentas_id"));
			datosInstruccionesPago.buscarDatosPreOrden(idInstruccionPago, this.contexto, this.ip);
		}
		
	}



}