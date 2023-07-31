package models.ordenes.toma_de_orden;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.dao.UIIndicadoresDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDetalle;
import com.bdv.infi.data.OrdenRequisito;
import com.bdv.infi.data.PrecioRecompra;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.data.UsuarioEspecial;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.dao.ClienteDAO;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
import com.bdv.infi_toma_orden.data.Cliente;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.bdv.infi_toma_orden.logic.TomaDeOrdenesDivisas;


/**
 * Clase de construir los datos para la  Toma de Orden
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class InsertDivisasPersonas extends AbstractModel
{
	private	TomaDeOrdenesDivisas boSTO;
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
	
	String nro_solicitud = "";
	DataSet solicSit = null;
	ArrayList<SolicitudClavenet> solicSitme = new ArrayList<SolicitudClavenet>();

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
//System.out.println("Entra a execute de insertDivisasPersonas-------- ");
//System.out.println("BEAN_TO_SIMULADAAAAAAAAAAAAAAAAAAAAAAAA\n"+beanTOSimulada);
//System.out.println("TIPO PRODUCTO EN EL BEAN LLENADO EN ISVALID: "+beanTOSimulada.getTipoProductoId());
//System.out.println("ANTES DEL INSERTAAAAAAAARRRRRRRR-------- ");
				if(boTO.insertar(beanTOSimulada)){//si la orden es insertada correctamente
//System.out.println("INSERTOOOOOOOOOOOOOOOOOOOOOOO-------- ");
					//guarda el numero de la orden en el dataset de datos a mostrar al usuario
					datos.addNew();
					datos.setValue("ordene_id", String.valueOf(beanTOSimulada.getIdOrden()));
					
					if(solicSitme.size()>0){ //Si el array contiene la solicitud
						
						//Actualiza campos en la orden recien insertada con la info de la Solicitud
						ordenDAO.updateCtaAbonoOrden(beanTOSimulada.getIdOrden(), solicSitme.get(0).getCtaAbono(), beanTOSimulada.getContraparte());
						
						/*
						//Actualiza en la orden recien insertada el campo CTA_ABONO con la info de la solicitud SITME
						ordenDAO.updateCtaAbonoOrden(beanTOSimulada.getIdOrden(), solicSitme.get(0).getCtaAbono());
System.out.println("-----ACTUALIZO CTA_ABONO="+solicSitme.get(0).getCtaAbono()+" DE ORDEN "+beanTOSimulada.getIdOrden()+"---------");

						//Actualiza en la orden recien insertada el campo ORDENE_PED_CTA_BSNRO y CTECTA_NUMERO con la info de la solicitud SITME
System.out.println("ORDENE_PED_CTA_BSNRO-->"+solicSitme.get(0).getCuentaBsO()+"\nCTECTA_NUMERO-->"+solicSitme.get(0).getCtaNro());
						ordenDAO.updateCtasOrden(beanTOSimulada.getIdOrden(), solicSitme.get(0).getCuentaBsO(), solicSitme.get(0).getCtaNro());
System.out.println("-----ACTUALIZO ORDENE_PED_CTA_BSNRO="+solicSitme.get(0).getCuentaBsO()+" Y CTECTA_NUMERO="+solicSitme.get(0).getCtaNro()+" DE ORDEN "+beanTOSimulada.getIdOrden()+"---------");
						*/
						
						//INSERCION DEL NRO TICKET DE LA ORDEN EN DATA EXTENDIDA
						ordenDAO.insertValoresDataExtendida(beanTOSimulada.getIdOrden(), DataExtendida.NRO_TICKET, solicSitme.get(0).getIdOrden()+"");
//System.out.println("-----ACTUALIZO DATA EXT --ORDEN ID="+beanTOSimulada.getIdOrden()+" VALOR NRO_TICKET="+solicSitme.get(0).getIdOrden()+"---------");
						
						//NM29643 Se insertan los datos de solicitudes SITME en ordene detalles
						OrdenDetalle od = new OrdenDetalle();
						SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
						od.setIdOrden(beanTOSimulada.getIdOrden());
						
						if(solicSit!=null){
							solicSit.first();
							solicSit.next();
							//System.out.println("FECHA SALIDAAAAAAAAAAAAAA----"+solicSit.getValue("FECHA_SALIDA_VIAJE"));
							od.setFechaSalidaViaje(solicSit.getValue("FECHA_SALIDA_VIAJE"));
							//System.out.println("FECHA RETORNOOOOOOOOOOOOO----"+solicSit.getValue("FECHA_RETORNO_VIAJE"));
							od.setFechaRetornoViaje(solicSit.getValue("FECHA_RETORNO_VIAJE"));
							od.setNumeroPasaporte(solicSit.getValue("NUM_PASAPORTE"));
							od.setNumeroTicketClavenet(solicSitme.get(0).getIdOrden());
							
							ordenDAO.insertOrdenDetalle(od);
						//System.out.println("-----ACTUALIZO ORDEN DETALLE (ID_ORDEN: "+beanTOSimulada.getIdOrden()+")---------");
						}

						//Actualizar la solicitud SITME
						SolicitudesSitmeDAO ssDAO = new SolicitudesSitmeDAO(_dso);
						ssDAO.updateSolicitud(solicSitme.get(0).getIdOrden(), beanTOSimulada.getIdOrden(), StatusOrden.REGISTRADA);
//System.out.println("-----ACTUALIZO SOLICITUD SITME NRO. "+solicSitme.get(0).getIdOrden()+"---------");
						
					}//Si el array contiene la solicitud
					
					
					
					/*
					if(solicSitme.get(0)!=null){
System.out.println("--------SOLICITUD NO NULLLLLLLL----------");
System.out.println("SOLICITUD\n"+solicSitme.toString());
System.out.println("FechaSalidaViaje: "+solicSitme.get(0).getFechaSalidaViaje());
System.out.println("FechaSalidaViaje (toString): "+solicSitme.get(0).getFechaSalidaViaje().toString());
						if(solicSitme.get(0).getFechaSalidaViaje()!=null){
System.out.println("--------FechaSalidaViaje NO NULLLLL----------");
System.out.println("--------"+solicSitme.get(0).getFechaSalidaViaje().toString());
System.out.println("--------FEcha: "+sdf.format(solicSitme.get(0).getFechaSalidaViaje()));
							od.setFechaSalidaViaje(sdf.format(solicSitme.get(0).getFechaSalidaViaje()));
						}
						
System.out.println("FechaRetornoViaje: "+solicSitme.get(0).getFechaRetornoViaje());
System.out.println("FechaRetornoViaje (toString): "+solicSitme.get(0).getFechaRetornoViaje().toString());
						if(solicSitme.get(0).getFechaRetornoViaje()!=null){
System.out.println("--------FechaRetornoViaje NO NULLLLL----------");
System.out.println("--------"+solicSitme.get(0).getFechaRetornoViaje().toString());
System.out.println("--------FEcha: "+sdf.format(solicSitme.get(0).getFechaRetornoViaje()));
							od.setFechaRetornoViaje(sdf.format(solicSitme.get(0).getFechaRetornoViaje()));
						}
						od.setNumeroPasaporte(solicSitme.get(0).getNroPasaporte());
						od.setNumeroTicketClavenet(solicSitme.get(0).getIdOrden());
					}*/					
					
					
					
					//Busca el objeto orden para buscar los documentos asociados a la transacci&oacute;n de negocio
					
					orden = ordenDAO.listarOrden(beanTOSimulada.getIdOrden());										
				
				}			
			
			//Se almacena en sessi&oacute;n para que pueda ser recuperado para la impresi&oacute;n
			_req.getSession().setAttribute("OrdenDocumentos", orden);
					

		} catch (Exception e) {
e.printStackTrace();
//			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
//			_req.getSession().setAttribute("mensaje_error",e.getMessage().toString()); 
						
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
//System.out.println("FLAG ISVALIDDDDDDDD----"+flag);
		if (flag)
		{
			//Obtiene los datos de la Solicitud
			nro_solicitud = _req.getParameter("nro_solicitud");
//System.out.println("Nro SOLICITUD REQUEST--------"+nro_solicitud);
			//System.out.println("nro_solicitud en insertDivisasPersonas----------: "+nro_solicitud);
			SolicitudesSitmeDAO ssDao = new SolicitudesSitmeDAO(_dso);
			
			if(nro_solicitud!=null && !nro_solicitud.equals("")){
				//Busca la informacion de Solicitud SITME
				solicSitme = ssDao.getSolicitudes(Long.parseLong(nro_solicitud), 0, null, null, null, null, ConstantesGenerales.ESTATUS_ORDEN_ESPERA_RECAUDOS, null, null, false, false);
				solicSit = ssDao.getDataSet();
//System.out.println("solicSit------------------------\n"+solicSit);
				//System.out.println("solicSitme size-----"+solicSitme.size());
				//System.out.println("solicSitme-----\n"+solicSit);
				if(solicSitme.size()<=0){ //No encuentra la solicitud en solicitudes SITME
					_record.addError("Nro. Solicitud", "La solicitud indicada no existe o se encuentra en un estatus diferente a \"ESPERA RECAUDOS\".");
					flag = false;
				}else{
//System.out.println("SOLIC_SITME MAYOR A 0----"+solicSitme.size());					
					OrdenDAO ordenDAO = new OrdenDAO(_dso);
					
					//Verifica que no exista una entrada en data extendida para la solicitud
					ordenDAO.listarDataExtPorIdTicket(solicSitme.get(0).getIdOrden()+"");
					if(ordenDAO.getDataSet().count()>0){ //Si ya exite una entrada para la solicitud en data extendida
						_record.addError("Nro. Solicitud", "La Solicitud Nro. "+solicSitme.get(0).getIdOrden()+" ya posee una orden asociada en el sistema");
						flag = false;
					}else{ //Solicitud no asociada a orden INFI (data extendida)
//System.out.println("SOLICITUD NO POSEE ORDEN INFI ASOCIADA (Data Extendida)!!!!!!!!!!!!!!!"+ordenDAO.getDataSet().count());
						ordenDAO.getDetallesOrdenFromNroSolicitud(solicSitme.get(0).getIdOrden());
						if(ordenDAO.getDataSet().count()>0){ //Si ya exite una entrada para la solicitud en detalles ordenes
							_record.addError("Nro. Solicitud", "La Solicitud Nro. "+solicSitme.get(0).getIdOrden()+" ya posee una orden asociada en el sistema");
							flag = false;
						}else{ //Solicitud no asociada a orden INFI (detalles ordenes)
//System.out.println("SOLICITUD NO POSEE ORDEN INFI ASOCIADA (Detalles Ordenes)!!!!!!!!!!!!!!!"+ordenDAO.getDataSet().count());	
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
								//boSTO = new TomaDeOrdenesDivisas(_dso);
								boSTO = new TomaDeOrdenesDivisas(_dso);
								boSTO.setParametrosEntrada(parametrosEntrada);
//System.out.println("SE SETEAN LOS PARAMETROS DE ENTRADA DE boSTO!!!!!!!!!");
								listaMensajes = boSTO.validar();
//System.out.println("SE VALIDAN LOS PARAMETROS DE ENTRADA DE boSTO!!!!!!!!! listaMensajesSize: "+listaMensajes.size());
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
//System.out.println("NEW DE beanTOSimulada EN ISVALID: "+beanTOSimulada);
								try {
									//verificar existencia de cliente y registrarlo
									boSTO.verificarExistenciaCliente();
									//SIMULAR NUEVAMENTE YA QUE EL USUARIO PODRIA HABER CAMBIADO DATOS EN LA PANTALLA DEL CALCULADOR
//System.out.println("ANTES DEL SIMULADOR_TO DEL ISVALID------------");
									beanTOSimulada = boSTO.simuladorTO();
									
									//NM29643
									if(beanTOSimulada.getTipoProductoId().equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){ //Si es Subasta Divisas PERSONAL
										beanTOSimulada.setMontoComisiones(solicSitme.get(0).getMontoComision()); //Setea el monto comision obtenido de la solicitud clavenet
										beanTOSimulada.setMontoTotal(solicSitme.get(0).getMontoSolicitado().multiply( (new BigDecimal(solicSitme.get(0).getTasaCambio())).setScale(2, BigDecimal.ROUND_HALF_EVEN) ) );
										beanTOSimulada.setMontoTotal( beanTOSimulada.getMontoTotal().add(beanTOSimulada.getMontoComisiones()) );
										beanTOSimulada.setNroRetencion(solicSitme.get(0).getNumeroBloqueo());
										//TODO Verificar esto
									}
									
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
								
								/*
								//------Validar Saldo del Cliente en base a los calculos previamente hechos en la simulacion de la orden----
								ArrayList listaValidacionesSaldo;
								try{					
									listaValidacionesSaldo = boSTO.validarSaldoCliente(beanTOSimulada);
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
								*/	
							}
						
						}//Solicitud no asociada a orden INFI (detalles ordenes)
						
					}//Solicitud no asociada a orden INFI (data extendida)
				
				}//se encontro la solicitud
			
			}//nro_solicitud no vacio
//System.out.println("beanTOSimulada final isValidddddd\n"+beanTOSimulada);
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
