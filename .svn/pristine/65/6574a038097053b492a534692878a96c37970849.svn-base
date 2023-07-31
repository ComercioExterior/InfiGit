package models.ordenes.toma_de_orden;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UIIndicadoresDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenRequisito;
import com.bdv.infi.data.PrecioRecompra;
import com.bdv.infi.data.UsuarioEspecial;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.dao.ClienteDAO;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
import com.bdv.infi_toma_orden.data.Cliente;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.bdv.infi_toma_orden.logic.TomaDeOrdenes;


/**
 * Clase de construir los datos para la  Toma de Orden
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Insert extends AbstractModel
{
	private	TomaDeOrdenes boSTO;
	private HashMap parametrosEntrada = new HashMap();
	private DataSet datos = new DataSet();
	private ArrayList ArrayRecompraTitulos = new ArrayList();	
	private ArrayList ArrayComisiones = new ArrayList();	
	private ArrayList ArrayCtasInstruccionPago = new ArrayList();
	private ArrayList<OrdenRequisito> ArrayRequisitos = new ArrayList<OrdenRequisito>();	
	Cliente cliente = new Cliente();
	private boolean inCarteraPropia = false;
	public TomaOrdenSimulada beanTOSimulada = null;
		
	DecimalFormat dFormato1 = new DecimalFormat("###,###,##0.00");

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{				
				
		TomaOrdenDAO boTO = new TomaOrdenDAO(null, _dso, _app, (String)parametrosEntrada.get("ipTerminal"));
								
		// Esto es para grabar los resultados ------------------------------------------ no va aqui
		// --	Registrar resultados de la simulacion
		try {
			OrdenDAO ordenDAO = new OrdenDAO(_dso);		
			Orden orden  = new Orden();
						
				if(boTO.insertar(beanTOSimulada)){//si la orden es insertada correctamente
					//guarda el numero de la orden en el dataset de datos a mostrar al usuario
					datos.addNew();
					datos.setValue("ordene_id", String.valueOf(beanTOSimulada.getIdOrden()));
					
					//Busca el objeto orden para buscar los documentos asociados a la transacci&oacute;n de negocio
					
					orden = ordenDAO.listarOrden(beanTOSimulada.getIdOrden());										
				
				}			
			
			//Se almacena en sessi&oacute;n para que pueda ser recuperado para la impresi&oacute;n
			_req.getSession().setAttribute("OrdenDocumentos", orden);
					

		} catch (Exception e) {	
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			_req.getSession().setAttribute("mensaje_error",e.getMessage().toString()); 
						
		} catch (Throwable e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			_req.getSession().setAttribute("mensaje_error",e.getMessage().toString()); 						
		}

		//cargar dataset de datos
		storeDataSet("datos", datos);
		parametrosEntrada.clear();	
		_req.getSession().removeAttribute("beanTOSimulada");
		_req.getSession().removeAttribute("fe_pacto");
		_req.getSession().removeAttribute("sector_productivo");
		_req.getSession().removeAttribute("fe_valor");
		_req.getSession().removeAttribute("concepto");
		_req.getSession().removeAttribute("actividad_economica");
		_req.getSession().removeAttribute("fechaRecompra");
	}// FIN DEL EXECUTE  . . . . . .
	
	/**
	 * Obtiene las comisiones para la orden, dado un arreglo de comisiones enviado por par&aacute;metro
	 * @return ArrayList
	 */
	private ArrayList obtenerComisionesOrden() {
		//recuperar comisiones en la orden
		String[] comisiones = _req.getParameterValues("comisiones");		
		ArrayList<OrdenOperacion> ArrComisiones = null;
		
		if(comisiones!=null){
			ArrComisiones = new ArrayList<OrdenOperacion>();
			for(int i=0; i<comisiones.length;i++){
				if(_req.getParameter("comision_"+comisiones[i]+"_"+i)!=null && !_req.getParameter("comision_"+comisiones[i]+"_"+i).trim().equals("")){
					OrdenOperacion ordenOperacion = new OrdenOperacion();
					ordenOperacion.setTasa(new BigDecimal(_req.getParameter("comision_"+comisiones[i]+"_"+i)));
					ordenOperacion.setIdTransaccionFinanciera(comisiones[i]);
					//setear id_comision en id operacion para diferenciar cada una de las comisiones
					ordenOperacion.setIdOperacion(Long.parseLong(_req.getParameter("id_comision_"+comisiones[i]+"_"+i)));
					ArrComisiones.add(ordenOperacion);
				}
			}
		}
		return ArrComisiones;
	}


	/**
	 * Recupera en una lista los títulos que estan chequeados para recompra.
	 * @return ArrayList con la lista de titulos con recompra.
	 * @throws Exception
	 */
	public ArrayList recuperarRecompraTitulos() throws Exception{
		//recuperar titulos con recompra chequeados
		String[] inRecompraTitulos = _req.getParameterValues("tit_recompra");	
		ArrayList<PrecioRecompra> ArrRecompraTitulos = new ArrayList<PrecioRecompra>();
		
		if(inRecompraTitulos!=null){			
			boolean conMonedaNegExt = false;
			boolean conMonedaLocal = false;
			
			for(int i=0; i < inRecompraTitulos.length; i++){				
				PrecioRecompra precioRecompra = new PrecioRecompra();
				BigDecimal precioRec = null;
				BigDecimal tasaPool = null; 
				
				if(_req.getParameter("titulos_precio_recompra_"+inRecompraTitulos[i])!=null && !_req.getParameter("titulos_precio_recompra_"+inRecompraTitulos[i]).trim().equals("")){
					precioRec = new BigDecimal(_req.getParameter("titulos_precio_recompra_"+inRecompraTitulos[i]));
				}
				
				if(_req.getParameter("tasa_pool_"+inRecompraTitulos[i])!=null && !_req.getParameter("tasa_pool_"+inRecompraTitulos[i]).trim().equals("")){
					tasaPool = new BigDecimal(_req.getParameter("tasa_pool_"+inRecompraTitulos[i]));
				}
				
				//asignar recompra para el título chequeado con recompra
				precioRecompra.setTituloId(inRecompraTitulos[i]);
				precioRecompra.setTitulo_precio_recompra(precioRec);
				precioRecompra.setTasaPool(tasaPool);
				ArrRecompraTitulos.add(precioRecompra);		
				
				//VERIFICAR LAS MONEDAS DE NEGOCIACION PARA LOS TITULOS CON RECOMPRA				
				//si la moneda de negociacion para el titulo es extranjera
				if(_req.getParameter("indicador_moneda_local_"+inRecompraTitulos[i])!=null && _req.getParameter("indicador_moneda_local_"+inRecompraTitulos[i]).equals("0")){
					conMonedaNegExt = true;
				}else{
					conMonedaLocal = true;
				}
			}
			//Se Ingresa Instruccion de pago sin importar usuario especial
			crearInstruccionesPago(conMonedaNegExt, conMonedaLocal);
		}
		
		return ArrRecompraTitulos;
		//////////////////////////////////////////////////////////////
	}
	
	/**
	 * Verifica si un usuario especial puede ingresar instrucciones de pago
	 * @return true si el usuario especial ingresa instrucciones, false en caso contrario
	 * @throws Exception
	 */
	private boolean usuarioIngresaInstruccionesPago() throws Exception {
		
		UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO(_dso);
		
		UsuarioEspecial usuarioEspecial = userEspecialDAO.listaUsuarioEspecial(getUserName());

		if(usuarioEspecial.isIngresoInstruccionesPago()){
			return true;
		}else
			return false;
	}

	/**
	 * Crea un arreglo de instrucciones de pago tanto para moneda internacional como nacional para la recompra de t&iacute;tulos
	 * @param conMonedaNegExt
	 * @param conMonedaLocal
	 */
	private void crearInstruccionesPago(boolean conMonedaNegExt, boolean conMonedaLocal) {
		//si hay moneda de negociacion extranjera	
		if(conMonedaNegExt){		
			
			CuentaCliente cuentaCliente = new CuentaCliente();
			
			//Datos Generales
			cuentaCliente.setNombre_beneficiario(cliente.getNombre());
			cuentaCliente.setCedrif_beneficiario(String.valueOf(cliente.getRifCedula()));
			cuentaCliente.setCtecta_uso(UsoCuentas.PAGO_DE_CUPONES);
			//cuentaCliente.setCtecta_uso(UsoCuentas.RECOMPRA);
			cuentaCliente.setClient_id(cliente.getIdCliente());
			
			//Obtener instrucciones de pago
			if(_req.getParameter("tipo_inst_int")!=null){			
				if(_req.getParameter("tipo_inst_int").equals("1")){//si es una tranferencia a cuenta internacional
					
					//-----verificar si la cuenta es europea (IBAN)
					String numeroCtaInternacional = _req.getParameter("ctecta_numero_ext");
					//Si es una cuenta europea concatenar el numero de cuenta con indicador de IBAN
					if(_req.getParameter("iban_cta_europea")!=null && _req.getParameter("iban_cta_europea").equals("1")){
						numeroCtaInternacional = numeroCtaInternacional + ConstantesGenerales.INDICADOR_IBAN;
					}
					//---------------------------------------------------------------------
					
					cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
					
					//datos de la cuenta internacional
					cuentaCliente.setCtecta_numero(numeroCtaInternacional);
					cuentaCliente.setCtecta_bcocta_bco(_req.getParameter("ctecta_bcocta_bco"));
					cuentaCliente.setCtecta_bcocta_direccion(_req.getParameter("ctecta_bcocta_direccion"));
					//setear cuenta del banco en el banco intermediario reusando el campo para codigo swift
					cuentaCliente.setCtecta_bcocta_swift(_req.getParameter("cta_bco_bcoint"));
					///------
					cuentaCliente.setCtecta_bcocta_bic(_req.getParameter("ctecta_bcocta_bic"));
					cuentaCliente.setCtecta_bcocta_telefono(_req.getParameter("ctecta_bcocta_telefono"));
					cuentaCliente.setCtecta_bcocta_aba(_req.getParameter("ctecta_bcocta_aba"));
					cuentaCliente.setCtecta_observacion(_req.getParameter("ctecta_observacion"));
					
					//--datos del banco intermediario
					if(_req.getParameter("intermediario")!=null && _req.getParameter("intermediario").equals("1")){
						cuentaCliente.setCtecta_bcoint_aba(_req.getParameter("ctecta_bcoint_aba"));
						cuentaCliente.setCtecta_bcoint_bco(_req.getParameter("ctecta_bcoint_bco"));
						cuentaCliente.setCtecta_bcoint_bic(_req.getParameter("ctecta_bcoint_bic"));
						cuentaCliente.setCtecta_bcoint_direccion(_req.getParameter("ctecta_bcoint_direccion"));
						cuentaCliente.setCtecta_bcoint_observacion(_req.getParameter("ctecta_bcoint_observacion"));
						//cuentaCliente.setCtecta_bcoint_pais(ctecta_bcoint_pais);
						cuentaCliente.setCtecta_bcoint_swift(_req.getParameter("cta_bco_bcoint"));
						cuentaCliente.setCtecta_bcoint_telefono(_req.getParameter("ctecta_bcoint_telefono"));							
					}							

				}else{//si es un cheque						
					cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CHEQUE));						
				}
			
			}else{
				//Si no se indica tipo de instrucción tomar cheque por defecto
				cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CHEQUE));
			}
			ArrayCtasInstruccionPago.add(cuentaCliente);		
		}
		
		//Si no hay recompra con neteo
		if(_req.getParameter("undinv_in_recompra_neteo")==null || _req.getParameter("undinv_in_recompra_neteo").equals("") || _req.getParameter("undinv_in_recompra_neteo").equals("0")){
			//si hay moneda de negociación local
			if(conMonedaLocal){
				CuentaCliente cuentaCliente = new CuentaCliente();
				//----Datos Generales-------------------------------------------------------
				cuentaCliente.setNombre_beneficiario(cliente.getNombre());
				cuentaCliente.setCedrif_beneficiario(String.valueOf(cliente.getRifCedula()));
				cuentaCliente.setCtecta_uso(UsoCuentas.PAGO_DE_CUPONES);
				//cuentaCliente.setCtecta_uso(UsoCuentas.RECOMPRA);
				cuentaCliente.setClient_id(cliente.getIdCliente());
				//----------------------------------------------------------------------------
				cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
				cuentaCliente.setCtecta_numero(_req.getParameter("cta_nac_recompra"));	
				cuentaCliente.setCtecta_nombre(_req.getParameter("cta_nac_nombre_recompra"));

				ArrayCtasInstruccionPago.add(cuentaCliente);		
			}
		}
		// Parametro Utilizado en la Validacion para la insercion.
		parametrosEntrada.put("instruccionPagoRecompra", ArrayCtasInstruccionPago);
	}// FIN DE METODO crearInstruccionesPago() . . . . .
	
	/**
	 * Carga los requisitos recibidos en el hashMap de Parametros 
	 * @throws Exception
	 */
	private ArrayList<OrdenRequisito> obtenerRequisitos() throws Exception {
		//recuperar comisiones en la orden
		DataSet requisitosUi;
		String[] requisitosOrden = _req.getParameterValues("requisitos");//Requisitos en la orden		
		ArrayList<OrdenRequisito> arrRequisitos = null;
		
		//Requisitos en la Unidad de Inversión
		UIIndicadoresDAO uiIndicadoresDAO = new UIIndicadoresDAO(_dso);
		uiIndicadoresDAO.listarRequisitosPorUi((Long)parametrosEntrada.get("idUnidadInversion"));
		requisitosUi = uiIndicadoresDAO.getDataSet();
		
		if(requisitosUi.count()>0){
			arrRequisitos =  new ArrayList<OrdenRequisito>();

			while(requisitosUi.next()){			
				OrdenRequisito ordenRequisito = new OrdenRequisito();
				ordenRequisito.setIndicaId(Integer.parseInt(requisitosUi.getValue("indica_id")));
				ordenRequisito.setUsuarioRecepcion(getUserName());
				ordenRequisito.setFechaRecepcion(null);
							
				if(requisitosOrden!=null){				
					for(int i=0; i<requisitosOrden.length;i++){					
						if(requisitosOrden[i]!=null && requisitosOrden[i].equals(requisitosUi.getValue("indica_id"))){
							ordenRequisito.setFechaRecepcion(new Date());
							i = requisitosOrden.length;
						}
					}
				}
				//Guardar Requisito en arreglo
				arrRequisitos.add(ordenRequisito);
			}
		}
		return arrRequisitos;
	}

	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();		
	
		boolean error = false;//Bandera de error 
		if (flag)
		{
			//----Verificar si es Cartera propia---------------------------------------------------------------------
			if(_req.getParameter("in_cartera_propia")!=null && _req.getParameter("in_cartera_propia").equals("1")){
				inCarteraPropia = true;
			}
			//--------------------------------------------------------------------------------------------------------
				
			parametrosEntrada = (HashMap) _req.getSession().getAttribute("parametrosEntrada");
			
			//-----Obtener datos del cliente-----
			ClienteDAO clienteDAO = new ClienteDAO(null, _dso);
			clienteDAO.listarPorCedula((String)parametrosEntrada.get("tipoPersona"), (String)parametrosEntrada.get("cedulaCliente"));
			cliente = clienteDAO.getCliente();
			//-----------------------------------

			if(!inCarteraPropia){
				// Se cren la instruccion de pago RECOM
				ArrayRecompraTitulos = this.recuperarRecompraTitulos();
				ArrayComisiones = obtenerComisionesOrden();
				ArrayRequisitos = obtenerRequisitos();
				
			}else{
				ArrayRecompraTitulos = new ArrayList();;
				ArrayComisiones = null;
				ArrayRequisitos = null;
			}
			
			datos.append("ordene_id", java.sql.Types.VARCHAR);		
			
			parametrosEntrada.put("recompraTitulos", ArrayRecompraTitulos);
			parametrosEntrada.put("comisionesOrden", ArrayComisiones);
			
			
			//Indicador especial para validaciones de Instrucciones de Pago
			//NOTA: Para los servicios y adjudicación no es necesario enviar el parametro ya que se tomara como nulo
			if(!usuarioIngresaInstruccionesPago()){// SI ES USUARIO ESPECIAL NO VALIDAR INSTRUCCIONES DE PAGO YA QUE NO SE PIDEN
				parametrosEntrada.put("validarInstruccionesPago", null);
			}else{//SI EL USUARIO ESPECIAL INGRESA INSTRUCCIONES VALIDAR INSTRUCCIONES DE PAGO
				parametrosEntrada.put("validarInstruccionesPago", "1");
			}
			
			//indicador de realizacion de calculos
			parametrosEntrada.put("calculador", null);

			// 	3.-	Validar la aplicacion de la transaccion
			ArrayList listaMensajes = new ArrayList();
	
			try {
				boSTO = new TomaDeOrdenes(_dso);
				boSTO.setParametrosEntrada(parametrosEntrada);
				listaMensajes = boSTO.validar();
			} catch (Exception e) {
				Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
				throw new Exception(e);
			}
		
			//	Salida por problemas de la informacion de la transaccion
			if (listaMensajes.size() != 0) {
				for (int i=0; i<listaMensajes.size(); i++) {
					_record.addError("Para su informaci&oacute;n", (String)listaMensajes.get(i));
				}
				error = true;
			
			}else{//Sino se encontraron errores de validacion iniciales
				
				//---Simular Orden--------------------------------------
				beanTOSimulada = new TomaOrdenSimulada();
				try {
					//verificar existencia de cliente y registrarlo
					boSTO.verificarExistenciaCliente();
					//SIMULAR NUEVAMENTE YA QUE EL USUARIO PODRIA HABER CAMBIADO DATOS EN LA PANTALLA DEL CALCULADOR
					beanTOSimulada = boSTO.simuladorTO();
					
					//--Setear Instrucciones de pago
					beanTOSimulada.setInstruccionPagoRecompra(ArrayCtasInstruccionPago);
					//agregar requisitos a la orden
					//Guardando Lista de Requisitos en el Objeto Orden:
					if(ArrayRequisitos!=null && !ArrayRequisitos.isEmpty()){
						beanTOSimulada.setOrdenRequisitos(ArrayRequisitos);						
					}
					
					//colocar observaciones de la orden
					if(_req.getParameter("observaciones")!=null && !_req.getParameter("observaciones").trim().equals("")){
						beanTOSimulada.setObservaciones(_req.getParameter("observaciones"));
					}
								
				} catch (Exception e) {
					Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
					throw new Exception(e);
				} catch (Throwable e) {
					throw new Exception(e);
				}
				//------------------------------------------------------	
				
				//------LLamada a Validación contra Servicio Ultra Temprano-----------------
				//error = validacionUltraTemprano();
				//-------------------------------------------------------------------------
				
				//------Validar Saldo del Cliente en base a los calculos previamente hechos en la simulacion de la orden----
				ArrayList listaValidacionesSaldo;
				try{					
					listaValidacionesSaldo = boSTO.validarSaldoCliente(getUserName(),_req.getRemoteAddr(),beanTOSimulada.getNumeroCuentaCliente());
				} catch (Exception e) {
					Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
					throw new Exception(e);
				}
				
				//validaciones de saldo insuficiente
				if (listaValidacionesSaldo.size() != 0) {
					for (int i=0; i<listaValidacionesSaldo.size(); i++) {
						_record.addError("Para su informaci&oacute;n", (String)listaValidacionesSaldo.get(i));
					}
					error = true;				
				}				
				//-----------------------------------------------------------------------------------------------------------
					
			}	
			
			if(error)
				flag = false;//Detener el proceso por errores de validacion	
		}
		return flag;	
	}

	/**
	 * Realiza Validación contra el servicio Ultra Temprano para verificar si el cliente adquirió divisas en los últimos días
	 * @return true si el cliente adquirió divisas, false en caso contrario
	 * @throws Exception
	 */
	private boolean validacionUltraTemprano() throws Exception {
		
		boolean error = false;
		
		//Validar adquisición de divisas en los últimos X (parámetro) cantidad de días
		InstrumentoFinancieroDAO instrumentoFinancieroDAO = new InstrumentoFinancieroDAO(_dso);
		instrumentoFinancieroDAO.listarPorId(beanTOSimulada.getInstrumentoId());
		String descripcionInstrumento = "";
		if(instrumentoFinancieroDAO.getDataSet().next()){
			descripcionInstrumento = instrumentoFinancieroDAO.getDataSet().getValue("insfin_descripcion");
		}			
		
		if(descripcionInstrumento.equals(ConstantesGenerales.INST_TIPO_SITME)){
			
			int cantDiasUltraT = 0;
			String parametroDias = ParametrosDAO.listarParametros(ParametrosSistema.DIAS_VALIDACION_ULTRA_T, _dso); //obtener la cantidad de días configurada en la tabla de parametros
			if(parametroDias!=null && !parametroDias.trim().equals("")){
				cantDiasUltraT = Integer.parseInt(parametroDias);
			}
			boolean esSolicitudUltraT = false;
			
			try {
				esSolicitudUltraT = boSTO.validarSolicitudUltraTemprano(cantDiasUltraT);
			} catch (Exception e) {
				_record.addError("Para su informaci&oacute;n", e.getMessage());
				error = true;	
			}
											
			if(esSolicitudUltraT){
				_record.addError("Para su informaci&oacute;n", "No se puede tomar la orden ya que el cliente adquirió divisas en los &uacute;ltimos "+cantDiasUltraT+" d&iacute;as");
				error = true;			
			}
		}
		//----------------------------------------------------------------------------------------

		return error;
	}
}
