package models.ordenes.venta_titulos;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.Page;
import megasoft.db;

import com.bdv.infi.dao.ActividadEconomicaDAO;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.ConceptosDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.dao.SectorProductivoDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.data.FechaValor;
import com.bdv.infi.data.TitulosPrecios;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;
import com.bdv.infi.logic.interfaces.UsoCuentas;
/**
 * Clase encargada de mostrar tanto los datos tanto del cliente y del titulo que se vender&aacute;, asi como tambi&eacute;n los datos que necesitan ser llenados para la venta y pago del titulo
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Table extends AbstractModel
{
	private DataSet titulos_custodia = null;
	private String tipoProducto="";
	private DataSet instruccionesPagoCtaDolares=new DataSet();
	private DataSet instruccionesPagoParaVentaTitulo=new DataSet();
	private DataSet productosCliente=new DataSet();
	private DataSet mensaje_erro_prod_cte=new DataSet();
	private DataSet mensaje_erro_cta_dolares=new DataSet();
	private DataSet mensaje_erro_cta_internacional=new DataSet();
	private DataSet mensaje_erro_cta_internacional2=new DataSet();
	private ManejadorDeClientes manejadorCliente=null;
	private ArrayList<Cuenta> cuentasNacionalesMonedaExt=new ArrayList<Cuenta>(); 
	private ArrayList<Cuenta> productos=new ArrayList<Cuenta>(); 
	
	private DataSet cte_ctas_nacionales = new DataSet();
	//private DataSet ctas_internac = null;
	private DataSet mensajes = null;
	private long ciRifCliente = 0;
	private String tipo_persona = "";
	private DataSet user_esp = null;
	private boolean usuarioEspecial;
	long idCliente = 0;
	
	private DataSet _sectorProductivo = new DataSet();
	private DataSet _conceptos = new DataSet();

	private String nombreUsuario;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception{		
		String datos_venta_titulo = "";
		DataSet datos_titulo = null;		
		DataSet ds_moneda_titulo = null;		
		DataSet moneda_local = null;
		DataSet monedas = new DataSet();
		TitulosDAO titulosDAO = new TitulosDAO(_dso);
		ClienteDAO clienteDAO = new ClienteDAO(_dso);		
		MonedaDAO monedaDAO = new MonedaDAO(_dso);		
		ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(_dso);
		ClienteCuentasDAO clienteCuentasNacionalDolaresDAO = new ClienteCuentasDAO(_dso);
		PreciosTitulosDAO preciosTitulosDAO = new PreciosTitulosDAO(_dso);
		CamposDinamicos camposDinamicosDAO = new CamposDinamicos(_dso);
		boolean precioActualizado = false;
		boolean precioAprobado = false;
		TitulosPrecios titulosPrecios = null;
		
		DataSet camposAdicionales = new DataSet();
		camposAdicionales.append("campo_adicional", java.sql.Types.VARCHAR);
		camposAdicionales.append("campo_adicional2", java.sql.Types.VARCHAR);		

		//Variables para fecha Valor
		FechaValor objFechaValor = new FechaValor();
		FechaValorDAO fechaValorDAO = new FechaValorDAO(_dso);
		DataSet fecha_valor=new DataSet();
		nombreUsuario = getUserName();
		//verificar usuario especial
		verificarUsuarioEspecial();
		user_esp.next();
				
		manejadorCliente= new ManejadorDeClientes(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		//Crear DataSet de Mensajes 
		mensajes = new DataSet();
		mensajes.append("mensaje_error_user_webs", java.sql.Types.VARCHAR);
		mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);	
		mensajes.append("mensaje_error_precio_recompra", java.sql.Types.VARCHAR);	
		mensajes.addNew();
		mensajes.setValue("mensaje_error_user_webs", "<div style='display:none'></div>");
		mensajes.setValue("mensaje_error_cuentas_cte", "<div style='display:none'></div>");		
		mensajes.setValue("mensaje_error_precio_recompra", "<div style='display:none'></div>");		

		//---Buscar parametro control de cambio---------------------------------------------
		String control_cambio=ParametrosDAO.listarParametros("CONTROL_DE_CAMBIO",_dso);
		_record.setValue("control_cambio", control_cambio);
		//----------------------------------------------------------------------------------
		
		////*****Detalles de cliente******////////////////////////////////////////////////////
		clienteDAO.detallesCliente(String.valueOf(idCliente));
		if(clienteDAO.getDataSet().next()){
			ciRifCliente = Long.parseLong(clienteDAO.getDataSet().getValue("client_cedrif"));
			tipo_persona = clienteDAO.getDataSet().getValue("tipper_id");
		}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * Buscamos el Conyugue de la persona que desea realizar la venta
		 * En caso de que la decula no exista en altair, el cliente no puede
		 * realizar la venta de los titulos
		 */
		
		DataSet _conyugue = new DataSet();
		
		_conyugue.append("tipo_persona_conyugue", java.sql.Types.VARCHAR);
		_conyugue.append("cedula_conyugue", java.sql.Types.VARCHAR);
		_conyugue.append("nombre_conyugue", java.sql.Types.VARCHAR);
		_conyugue.append("display", java.sql.Types.VARCHAR);
		_conyugue.addNew();
		_conyugue.setValue("display","none");
		_req.getSession().setAttribute("display","none");
		
		if(_req.getParameter("estado_casado")!=null && !_req.getParameter("estado_casado").equals("") && _req.getParameter("estado_casado").equalsIgnoreCase("SI")){

		try {
			
			ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
					(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
			
			//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
			Cliente clienteWS = mdc.getCliente(String.valueOf(_req.getParameter("cedula_conyuge")), _req.getParameter("tipo_persona_conyuge"), nombreUsuario, _req.getRemoteAddr(),false,true,false,false);

			//Se agregan datos al dataset de conyugue
			
			_conyugue.setValue("tipo_persona_conyugue",clienteWS.getTipoDocumento().trim());
			_conyugue.setValue("cedula_conyugue",clienteWS.getCi());
			_conyugue.setValue("nombre_conyugue",clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " "));
			_conyugue.setValue("display","block");
			
			_req.getSession().setAttribute("tipo_persona_conyugue",clienteWS.getTipoDocumento().trim());
			_req.getSession().setAttribute("cedula_conyugue",clienteWS.getCi());
			_req.getSession().setAttribute("display","block");
			_req.getSession().setAttribute("nombre_conyugue", clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " "));
			
		} catch (Exception e) {
			
			if(e.getMessage().toUpperCase().indexOf("PERSONA INEXISTENTE EN LA BASE DE DATOS")>-1)
			{
				throw new Exception("No se puede realizar la venta, debido a que la c&eacute;dula del conyuge no existe en ALTAIR");
			}				
		}
		}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		//////**************Datos de Pago**************************///////////////////
		//lista de cuentas internacionales
		/*clienteCuentasDAO.listarCtasInternacionales(idCliente);
		ctas_internac = clienteCuentasDAO.getDataSet(); */		
		
		//listar cuentas nacionales de ALTAIR para el cliente seleccionado//
		this.buscarCuentasCliente();		
		
		//Formas de pago fijas para la venta de titulos
		DataSet forma_pago = new DataSet();
		forma_pago.append("forma_pago_id", java.sql.Types.VARCHAR);		
		/*
		forma_pago.addNew();
		forma_pago.setValue("forma_pago_id", ConstantesGenerales.FORMA_PAGO_CTA_NAC);
		forma_pago.addNew();
		forma_pago.setValue("forma_pago_id", ConstantesGenerales.FORMA_PAGO_CTA_INTERN);
		forma_pago.addNew();
		forma_pago.setValue("forma_pago_id", ConstantesGenerales.FORMA_PAGO_CHEQUE);
		*/
		//Campos Dinamicos para Venta
		camposDinamicosDAO.listarPorTipo(CamposDinamicosConstantes.TIPO_VENTA);
		DataSet camposDinamicosVenta = camposDinamicosDAO.getDataSet();
		
		/////**************************************************************************/////
		String aux = getResource("datos_venta_titulo.htm");
		int numTitulo = 0;
		if(titulos_custodia.next()){
			
			//Seccion de codigo comentada ya que los productos sitme funcionan con recompra automatica
			/*if (titulos_custodia.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				aux = getResource("datos_venta_titulo_sitme.htm");
				ActividadEconomicaDAO actividadEconomica = new ActividadEconomicaDAO(_dso);
				SectorProductivoDAO sectorProductivo = new SectorProductivoDAO(_dso);
				ConceptosDAO conceptos	=new ConceptosDAO(_dso);

				actividadEconomica.listar();
				sectorProductivo.listar();
				conceptos.listar();
				
				storeDataSet("actividadeseconomicas", actividadEconomica.getDataSet());
				storeDataSet("sectoresproductivos", sectorProductivo.getDataSet());
				storeDataSet("conceptos", conceptos.getDataSet());
			}else{*/
				while (_config.bind.next()){
					if (_config.bind.getValue("source").equals("sectoresproductivos")
							|| _config.bind.getValue("source").equals("conceptos")){								
						_config.bind.removeRow(Integer.parseInt(_config.bind.getValue("_row")));
						_config.bind.first();
					}
				}
				_config.bind.first();
				_sectorProductivo.append("sectorproductivo", java.sql.Types.VARCHAR);
				_conceptos.append("conceptos", java.sql.Types.VARCHAR);
				_sectorProductivo.addNew();
				_sectorProductivo.setValue("sectorproductivo", "");
				_conceptos.addNew();
				_conceptos.setValue("conceptos", "");
				storeDataSet("sectoresproductivos", _sectorProductivo);
				storeDataSet("conceptos", _conceptos);
			//}
			

				
			titulosPrecios = new TitulosPrecios();
			String precioRecompraTitulo = "";
			
			numTitulo++;
							
			//obtener un numerico para el id del título para que sea manejado por las funcioes javascript del formulario
			String idTituloNumerico = String.valueOf(numTitulo);
			
			camposAdicionales.addNew();
			camposAdicionales.setValue("campo_adicional", "<div style="+"display:none"+">"+"&nbsp"+"</div>");
			//Realizar consulta
			titulosDAO.detallesTitulo(titulos_custodia.getValue("titulo_id"));
					
			datos_titulo = titulosDAO.getDataSet();
			
			if(datos_titulo.next()){			
			   		
				monedas = new DataSet();
				String valorPrecioRecompra = "0";
				//campos dataset monedas
				monedas.append("moneda_id", java.sql.Types.VARCHAR);
				monedas.append("moneda_descripcion", java.sql.Types.VARCHAR);

				
				
				/*TODO Verificar si no se necesita a futuro la moneda local para negociacion
				Codigo comentado: Para el proceso de ventas actual no se contempla la venta en moneda local			
 				
				//Monedas de negociacion							
				  monedas.addNew();
				//buscar moneda local
				
				monedaDAO.listarMonedaLocal();
				moneda_local = monedaDAO.getDataSet();
				//llenar dataSet con moneda local
				while(moneda_local.next()){
					monedas.setValue("moneda_id", moneda_local.getValue("moneda_id"));
					monedas.setValue("moneda_descripcion", moneda_local.getValue("moneda_descripcion"));
				}*/
				
				monedaDAO.listar(datos_titulo.getValue("titulo_moneda_neg"));
				ds_moneda_titulo = monedaDAO.getDataSet();
				
				if(ds_moneda_titulo.next()){
					//si la moneda de negociaci&oacute;n del titulo es extranjera, agregar al dataSet de opciones de moneda
					//si la moneda es local solo se almacenara en el dataset de opciones la local
					if(ds_moneda_titulo.getValue("moneda_in_local")!=null && ds_moneda_titulo.getValue("moneda_in_local").equals("0")){
						monedas.addNew();
						monedas.setValue("moneda_id", ds_moneda_titulo.getValue("moneda_id"));
						monedas.setValue("moneda_descripcion", ds_moneda_titulo.getValue("moneda_descripcion"));
					}
				}
				
				//Buscar precio de recompra del titulo
				//obtener precios del titulo
				/*PrecioRecompraDAO precioRecompraDAO = new PrecioRecompraDAO(_dso); 
				PrecioRecompra precioRecompra = new PrecioRecompra();
				precioRecompra = precioRecompraDAO.obtenerPrecioRecompraTitulo(titulos_custodia.getValue("titulo_id"), montoPedido, idMonedaUI);
				
				beanTitulo.setPorcentajeRecompra(precioRecompra.getTitulo_precio_recompra());

				*/		
				titulosPrecios.setTipoProductoId(titulos_custodia.getValue("tipo_producto_id"));
				titulosPrecios.setIdTitulo(titulos_custodia.getValue("titulo_id"));
				
				precioActualizado = preciosTitulosDAO.esHoyFechaUltimoPrecio(titulosPrecios);
				precioAprobado = preciosTitulosDAO.esAprobadoPrecioTitulo(titulosPrecios);
				if(!precioActualizado || !precioAprobado){
					valorPrecioRecompra = "0";
				}else{
					valorPrecioRecompra = titulosPrecios.getPRecompra();
				}
				
				precioRecompraTitulo = valorPrecioRecompra + "<INPUT TYPE='hidden' size='12' VALUE='"+ valorPrecioRecompra +"' NAME='titulos_precio_recompra_"+idTituloNumerico+"'/>" ;

				if(usuarioEspecial){
					if(user_esp.getValue("usresp_precio_venta_titulos")!=null && 
					user_esp.getValue("usresp_precio_venta_titulos").equals("1")){
						precioRecompraTitulo = "<INPUT TYPE='text' size='12' style='text-align:right' VALUE='"+ valorPrecioRecompra +"' NAME='titulos_precio_recompra_"+idTituloNumerico+"' MAXLENGTH='5' onkeypress=\"EvaluateText('%f',this)\"/>";				
					}
				}
				//Si no posee permiso para cambiar precio. Mostrar alerta de precio cero(0) o desactualizado al dia de hoy
				if(valorPrecioRecompra.equals("0")){
					mensajes.setValue("mensaje_error_precio_recompra", ConstantesGenerales.MENSAJE_PRECIOS_RECOMPRA);		
				}
				
				//crear plantilla htm 
				datos_venta_titulo = aux;
				
				//crear objeto Page con el template para los datos de recompra del t&iacute;tulo
				//y asi poder hacer los remplazos de data necesarios

				Page page = new Page(datos_venta_titulo);				
								
				page.repeat(camposDinamicosVenta, "rows_campos_dinamicos");
				page.repeat(monedas, "rows_mon");				
				page.repeat(cte_ctas_nacionales, "rows_ctas_nac");
				
				
				
				page.replace("@titulo_id@", idTituloNumerico);
				page.replace("@titulo_id_real@", titulos_custodia.getValue("titulo_id"));
				page.replace("@cant_disponible@",  titulos_custodia.getValue("cantidad_disponible"));
				mensajes.first();
				mensajes.next();
				page.replace("@mensaje_error_user_webs@",  mensajes.getValue("mensaje_error_user_webs"));
				page.replace("@mensaje_error_cuentas_cte@",  mensajes.getValue("mensaje_error_cuentas_cte"));
				
				
				//Seccion de codigo comentada ya que por la nueva funcionalidad las instrucciones de pago se buscan desde /ajax_instrucciones_venta
				
				//LLENAR LOS DATOS DE LA CUENTA SWIFT 
				//clienteCuentasDAO.browseClienteCuentas(String.valueOf(idCliente), String.valueOf(TipoInstruccion.CUENTA_SWIFT),null,null);
				
				/*if(clienteCuentasDAO.getDataSet().count()>0){
					
					clienteCuentasDAO.getDataSet().first();
					clienteCuentasDAO.getDataSet().next();
					page.replace("@ctecta_numero_ext@", clienteCuentasDAO.getDataSet().getValue("ctecta_numero")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_numero"):"");
					page.replace("@ctecta_bcocta_direccion@", clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_direccion")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_direccion"):"");
					page.replace("@ctecta_bcocta_bco@", clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bco")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bco"):"");
					page.replace("@ctecta_bcocta_bic@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bic")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_bic"):""));
					page.replace("@ctecta_bcocta_telefono@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_telefono")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_telefono"):""));					
					page.replace("@ctecta_bcocta_aba@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_aba")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcocta_aba"):""));
					page.replace("@ctecta_observacion@", (clienteCuentasDAO.getDataSet().getValue("ctecta_observacion")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_observacion"):""));
										
					if (clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bco")!=null) {
						page.replace("@bcoint_checked@","true");
						page.replace("@ctecta_bcoint_bco@", clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bco")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bco"):"");
						page.replace("@ctecta_bcoint_bic@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bic")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_bic"):""));
						page.replace("@ctecta_bcoint_swift@", clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_swift")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_swift"):"");
						page.replace("@ctecta_bcoint_aba@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_aba")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_aba"):""));
						page.replace("@ctecta_bcoint_direccion@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_direccion")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_direccion"):""));
						page.replace("@ctecta_bcoint_telefono@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_telefono")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_telefono"):""));
						page.replace("@ctecta_bcoint_observacion@", (clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_observacion")!=null?clienteCuentasDAO.getDataSet().getValue("ctecta_bcoint_observacion"):""));
					}
					else{
						page.replace("@bcoint_checked@","false");
						page.replace("@ctecta_bcoint_bco@", "");
						page.replace("@ctecta_bcoint_bic@", "");
						page.replace("@ctecta_bcoint_swift@", "");
						page.replace("@ctecta_bcoint_aba@", "");
						page.replace("@ctecta_bcoint_direccion@", "");
						page.replace("@ctecta_bcoint_telefono@", "");
						page.replace("@ctecta_bcoint_observacion@", "");												
					}
				*/	
				/*}else{
					mensajes.setValue("mensaje_error_precio_recompra", mensajes.getValue("mensaje_error_precio_recompra") + "<br>El usuario no posee instrucciones de pago para depósitos en cuentas del extrajero");
					//Setear valores en blanco
					page.replace("@ctecta_numero_ext@", "");
					page.replace("@ctecta_bcocta_direccion@", "");
					page.replace("@ctecta_bcocta_bco@", "");
					page.replace("@ctecta_bcocta_bic@", "");
					page.replace("@ctecta_bcocta_telefono@", "");					
					page.replace("@ctecta_bcocta_aba@", "");
					page.replace("@ctecta_observacion@", "");
					page.replace("@bcoint_checked@","false");
					page.replace("@ctecta_bcoint_bco@", "");
					page.replace("@ctecta_bcoint_bic@", "");
					page.replace("@ctecta_bcoint_swift@", "");
					page.replace("@ctecta_bcoint_aba@", "");
					page.replace("@ctecta_bcoint_direccion@", "");
					page.replace("@ctecta_bcoint_telefono@", "");
					page.replace("@ctecta_bcoint_observacion@", "");					
				}*/
				if(!titulos_custodia.getValue("tipo_producto_id").equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
					if(!titulos_custodia.getValue("tipo_producto_id").equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){	
					//Busqueda de instrucciones de pago para la venta 
					clienteCuentasNacionalDolaresDAO.browseClienteCuentas(String.valueOf(idCliente), String.valueOf(TipoInstruccion.CUENTA_SWIFT),null,null,UsoCuentas.VENTA_TITULO);
					instruccionesPagoParaVentaTitulo=clienteCuentasNacionalDolaresDAO.getDataSet();
						//Configuracion de mensaje de Error si el cliente no posee cuenta nacional en dolares
						if(instruccionesPagoParaVentaTitulo.count()==0){							
							mensaje_erro_cta_internacional.addNew();
							mensaje_erro_cta_internacional.append("error_cta_internacional",java.sql.Types.VARCHAR);
							mensaje_erro_cta_internacional.setValue("error_cta_internacional","El Cliente no posee instrucciones de pago para la transferencia a cuentas internacionales");					
						}
					}
				}else{
					mensaje_erro_cta_internacional2.addNew();
					mensaje_erro_cta_internacional2.append("error_cta_internacional2",java.sql.Types.VARCHAR);
					mensaje_erro_cta_internacional2.setValue("error_cta_internacional2","Solo se pueden agregar instrucciones de pago para cuentas Nacionales para el Tipo de Producto Sicad 2");//					
				}
				
				//NM32454 TTS-488 MANEJO CORRELATIVOS
				String numeroPersona="0";
				//Busqueda de productos y cuentas del cliente 
				productos = manejadorCliente.listarProductosCliente(String.valueOf(ciRifCliente), tipo_persona, nombreUsuario, _req.getRemoteAddr(),numeroPersona);			
				cuentasNacionalesMonedaExt = manejadorCliente.listaDeCuentasDolares(productos, nombreUsuario, _req.getRemoteAddr());
				
				//Generar DataSets de productos y cuentas del cliente
				productosCliente=manejadorCliente.cargarDataSetProductosCuentas(productos,String.valueOf(ciRifCliente), tipo_persona,_dso,true);
				instruccionesPagoCtaDolares=manejadorCliente.cargarDataSetProductosCuentas(cuentasNacionalesMonedaExt,String.valueOf(ciRifCliente), tipo_persona,_dso,false);

				//Configuracion de la Fecha Valor
				tipoProducto=titulos_custodia.getValue("tipo_producto_id");				
				objFechaValor = fechaValorDAO.listar(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)?com.bdv.infi.logic.interfaces.FechaValor.VENTA_TITULOS_SITME:com.bdv.infi.logic.interfaces.FechaValor.VENTA_TITULOS);
				Date fechaValor = objFechaValor.getFechaValor();
				
				if(fechaValor!=null){
					fecha_valor.addNew();
					fecha_valor.append("fecha_valor",java.sql.Types.VARCHAR);
					fecha_valor.setValue("fecha_valor",Utilitario.DateToString(fechaValor,"dd-MM-yyyy"));
				}
		
				//Configuracion de mensaje de Error si el cliente no posee cuenta nacional en dolares
				if(instruccionesPagoCtaDolares.count()==0){							
					mensaje_erro_cta_dolares.addNew();
					mensaje_erro_cta_dolares.append("error_cta_dolares",java.sql.Types.VARCHAR);
					mensaje_erro_cta_dolares.setValue("error_cta_dolares","El cliente no ninguna posee Cuenta Nacional en Dólares activa....    Debe realizar previamente la apertura de la cuenta a través de una agencia");					
				}
				
				//Configuracion de mensaje de Error si el cliente no posee cuenta nacional en dolares
				if(productosCliente.count()==0){							
					mensaje_erro_prod_cte.addNew();
					mensaje_erro_prod_cte.append("error_prod_cte",java.sql.Types.VARCHAR);
					mensaje_erro_prod_cte.setValue("error_prod_cte","El cliente no ning&uacute;n producto asociado al banco");					
				}
				
				//configuracion de combo para las cuentas nacionales en dolares
				page.repeat(instruccionesPagoCtaDolares, "inst_cta_dolares");
				
				//configuracion de instrucciones de pago para ventas de titulos
				page.repeat(instruccionesPagoParaVentaTitulo, "inst_pago_ventas");

				//configuracion de productos del cliente
				page.repeat(productosCliente, "prods_cte");
				
				//Configuracion de mensaje de error si el cliente no posee cuentas Nacionales en dolares
				page.repeat(mensaje_erro_cta_dolares,"msn_error_cta_dolares");

				//Configuracion de mensaje de error si el cliente no posee cuentas Nacionales en dolares				
				page.repeat(mensaje_erro_cta_internacional,"msn_error_cta_internacional");
				
				//Configuracion de mensaje de error si el cliente no posee cuentas Nacionales en dolares				
				page.repeat(mensaje_erro_cta_internacional2,"msn_error_cta_internacional2");

				//Configuracion de mensaje de error si el cliente no posee cuentas Nacionales en dolares				
				page.repeat(mensaje_erro_prod_cte,"msn_error_prod_cliente");
				
				//visibilidad tabla campos dinamicos
				if(camposDinamicosVenta.count()>0){
					page.replace("@display_campos_din@","display: block");
				}else{
					page.replace("@display_campos_din@","display: none");
				}
	
										
				camposAdicionales.setValue("campo_adicional", page.toString());
				
				//setear el id del titulo como numerico para el resto de los remplazos en el formulario
				titulos_custodia.setValue("titulo_id", idTituloNumerico);
				camposAdicionales.setValue("campo_adicional2", precioRecompraTitulo);
			}
			
			//----Verificador de usuario especial con permiso para ingresar instrucciones de pago 
			if(usuarioEspecial && user_esp.getValue("usresp_ingreso_instrucciones_pago")!=null && 
				user_esp.getValue("usresp_ingreso_instrucciones_pago").equals("1")){
				
				_record.setValue("usuario_especial", "1");//permite ingresar
			}else{				
				_record.setValue("usuario_especial", "0");//NO permite ingresar
				
			}
			//-----------------------------------------------------------------------------------------

			
		}
				
		//registrar los datasets exportados por este modelo	
		storeDataSet("cliente", clienteDAO.getDataSet());//datos del cliente
		storeDataSet("titulos_custodia", titulos_custodia);//titulos en custodia del cliente
		storeDataSet("productos_cliente",productosCliente);//Instrucciones de pago para cuentas nacional en dolares
		storeDataSet("instrucciones_cuenta_dolares",instruccionesPagoCtaDolares);//Instrucciones de pago para cuentas nacional en dolares
		storeDataSet("instrucciones_pago_ventas",instruccionesPagoParaVentaTitulo);//Instrucciones de pago para ventas de titulos
		storeDataSet("error_cta_dolares",mensaje_erro_cta_dolares);//Mensaje de error (Cuenta Nacional el dolares)
		storeDataSet("error_cta_internacional",mensaje_erro_cta_internacional);//Mensaje de error (Cuenta SWIFT)		
		storeDataSet("error_productos_ctes",mensaje_erro_prod_cte);//Mensaje de error (Productos del cliente)
		storeDataSet("fecha_valor",fecha_valor);//Mensaje de error (Productos del cliente)
		storeDataSet("record", _record);
		storeDataSet("mensajes", mensajes);
		storeDataSet("conyugue",_conyugue);
		storeDataSet("camposAdicionales",camposAdicionales);
	}
	
	/**
	 * Verifica si el usuario conectado es un usuario especial colocando la variable de usuario especial en falso o verdadero según sea el caso
	 * @throws Exception
	 */
	private void verificarUsuarioEspecial() throws Exception {
		
		UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO(_dso);
		
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		String usuario =  usuarioSegDAO.idUserSession(getUserName());
		//-----------------------------------------------------------

		userEspecialDAO.listar(usuario);
		user_esp = userEspecialDAO.getDataSet();
		
		if(user_esp.count()>0){
			usuarioEspecial = true;			
		}else{
			usuarioEspecial = false;
		}
		
	}
	
	/**
	 * Realiza la Búsqueda de las cuentas asociadas al cliente, y muestra un mensaje de error si ocurre algún error de conexión
	 * @throws Exception
	 */
	private void buscarCuentasCliente() throws Exception {
		String userWebServices = "";
		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		
		try{
			//buscar usuario de WebServices
			userWebServices = getUserName();
		
			try{
				//buscar cuentas asociadas al cliente
				ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentas(String.valueOf(ciRifCliente), tipo_persona, userWebServices, _req.getRemoteAddr()); 
				cte_ctas_nacionales = manejadorDeClientes.cargarDataSet(listaCuentas);
				
			}catch(Exception e){
				Logger.error(this,e.getMessage(),e);
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas del cliente en arquitectura extendida");
				//crearDataSetCuentasVacio();
			}

		}catch(Exception e){
			Logger.error(this,e.getMessage(),e);
			mensajes.setValue("mensaje_error_user_webs", "Error consultando el usuario de web services");
			//crearDataSetCuentasVacio();
		}		
		
	}
	
	/**
	 * Crea un DataSet limpio para las cuentas de los clientes
	 * @throws Exception
	 */
	/*private void crearDataSetCuentasVacio() throws Exception {
		//Dataset limpio de cuentas del cliente
		cte_ctas_nacionales = new DataSet();
		cte_ctas_nacionales.append("numero", java.sql.Types.VARCHAR);
		cte_ctas_nacionales.append("tipo", java.sql.Types.VARCHAR);
		cte_ctas_nacionales.append("saldo_disponible", java.sql.Types.VARCHAR);
		
		cte_ctas_nacionales.addNew();
		cte_ctas_nacionales.setValue("numero", "332248");
		cte_ctas_nacionales.setValue("tipo", "Corriente");
		cte_ctas_nacionales.setValue("saldo_disponible", "8000000.00");
	}*/


	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception{
		boolean flag = super.isValid();
		
		if (flag){				
			ClienteDAO clienteDAO = new ClienteDAO(_dso);
			clienteDAO.listarPorId(_record.getValue("client_id"));
			String cedulaCliente = clienteDAO.getDataSet().getValue("CLIENT_CEDRIF");
			
			String[] tituloProducto = _record.getValue("titulo_id").split("&&");
			
			if(_record.getValue("client_id")!=null)
				idCliente = Long.parseLong(_record.getValue("client_id"));

			//Buscar titulos en custodia del cliente
			//si no se seleciona un titulo, trae la lista completa
			CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);		
			custodiaDAO.listarTitulos(idCliente,tituloProducto[0],tituloProducto[1]);
			titulos_custodia = custodiaDAO.getDataSet();
			storeDataSet("datos", custodiaDAO.getTotalRegistros());
			
			//Validar que el cliente poseea el titulo solicitado en custodia	
			if(titulos_custodia.count()==0){
				_record.addError("Ordenes / Venta de T&iacute;tulos", "El cliente no posee titulos en custodia");
				flag = false;
			}
			
			//Validacion de la cedula del conyugue
			if(_req.getParameter("estado_casado")!=null && !_req.getParameter("estado_casado").equals("") && _req.getParameter("estado_casado").equalsIgnoreCase("SI")){
				
				if(_req.getParameter("cedula_conyuge").equals(null) || _req.getParameter("cedula_conyuge").equals("")){
					_record.addError("Ordenes / Venta de T&iacute;tulos", "Debe ingresar la c&eacute;dula del conyuge.");
					flag = false;
				}
				
				if(!_req.getParameter("cedula_conyuge").equals(null) && !_req.getParameter("cedula_conyuge").equals("")){
					
					if(_req.getParameter("cedula_conyuge").equals(cedulaCliente)){
						_record.addError("Ordenes / Venta de T&iacute;tulos", "La c&eacute;dula del conyuge debe ser diferente al cliente que realiza la venta");
						flag = false;
					}	
				}
			}
			
			//verificar si es persona natural pedir estado civil
			if(_req.getParameter("tipper_id_cliente")!=null && (_req.getParameter("tipper_id_cliente").equalsIgnoreCase("V") || _req.getParameter("tipper_id_cliente").equalsIgnoreCase("E"))){
				if(_req.getParameter("estado_casado").equalsIgnoreCase(null) || _req.getParameter("estado_casado").equalsIgnoreCase("")){
					
						_record.addError("Ordenes / Venta de T&iacute;tulos", "Debe indicar su estado civil");
						flag = false;
				}
			}			
		}
		
		return flag;	
	}

}
