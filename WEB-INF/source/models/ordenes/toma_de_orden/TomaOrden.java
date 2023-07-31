package models.ordenes.toma_de_orden;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.sql.DataSource;
import com.bdv.infi_toma_orden.dao.ClienteDAO;
import com.bdv.infi_toma_orden.data.Cliente;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.dao.UIIndicadoresDAO;
import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.dao.VehiculoRolDAO;
import com.bdv.infi.dao.ActividadEconomicaDAO;
import com.bdv.infi.dao.SectorProductivoDAO;
import com.bdv.infi.dao.ConceptosDAO;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.data.UIBlotterRangos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.FechaValor;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_altair.consult.ManejoDeClientes;
import com.bdv.infi.util.Utilitario;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.Util;
import megasoft.db;
import models.security.userInfo.SecurityDBUserInfo;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Clase de construir los datos para la  Toma de Orden
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class TomaOrden extends AbstractModel
{ 
	private DataSet datos_unidad_inv = new DataSet();
	private DataSet indicadores_ui = new DataSet();
	private DataSet requisitos_ui = new DataSet();
	private DataSet titulos_ui = new DataSet();
	private long idUnidadInversion = 0;	
	private DataSet _datos = new DataSet();
	private DataSet _actividadEconomica = new DataSet();
	private DataSet _sectorProductivo = new DataSet();
	private DataSet _conceptos = new DataSet();
	private String idBlotter ="";
	private String descripcionBlotter ="";
	private DataSet user_esp = null;
	private DataSet blotters_ui = new DataSet();
	private DataSet blotter_defecto = null;
	private DataSet campos_dinamicos = new DataSet();
	private boolean usuarioEspecial;
	private long idCliente = 0;
	private DataSet cuentasCliente = new DataSet();
	private DataSet mensajes = null;
	private DataSet datosUIBlotter = null;
	private int indicaRecompra = 0;
	private int diasAperturaDeCuenta;
	
	BigDecimal INDINVPUB = new BigDecimal(0);
			BigDecimal  INDINVVIG = new BigDecimal(0);
			BigDecimal  TASAINTANU = new BigDecimal(0);
			BigDecimal  PLAZO = new BigDecimal(0);
	
	Cliente cliente;
	private long ciRifCliete=0;
	private String tipo_persona ="";
	private boolean inCarteraPropia = false;
	
	private Date fecha = new Date();
	
	boolean exiteCuentaCustodia=true;
	boolean exiteCuentaSwift=true;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		UnidadInversionDAO unidInversionDAO = new UnidadInversionDAO(_dso);	
		UIBlotterRangosDAO uiBlotterRangosDAO = new UIBlotterRangosDAO(_dso);
		UIBlotterDAO uiBlotterDAO = new UIBlotterDAO(_dso);
		UIBlotterRangos uiBlotterRangos = new UIBlotterRangos();
		ActividadEconomicaDAO actividadEconomica = new ActividadEconomicaDAO(_dso);
		SectorProductivoDAO sectorProductivo = new SectorProductivoDAO(_dso);
		ConceptosDAO conceptos	=new ConceptosDAO(_dso);
		FechaValorDAO fechaValorDAO = new FechaValorDAO(_dso);
		com.bdv.infi.data.FechaValor objfechaValor = new com.bdv.infi.data.FechaValor();
		
		//obtener fecha actual		
		//dd-MM-yyyy
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String fechaValor = df.format(fecha); //inicializar en fecha actual
		String fechaPacto = df.format(fecha); //La Fecha de Pacto es la fecha actual
		
		//datos dinamicos para la toma de orden
		_datos.append("calculos", java.sql.Types.VARCHAR);
		_datos.append("montos_blotter", java.sql.Types.VARCHAR);
		_datos.append("rangos_precio_subasta", java.sql.Types.VARCHAR);
		_datos.append("blotter_id", java.sql.Types.VARCHAR);
		_datos.append("blotter_descripcion", java.sql.Types.VARCHAR);
		_datos.append("combo_blotters_ui", java.sql.Types.VARCHAR);
		_datos.append("campo_financiamiento", java.sql.Types.VARCHAR);
		_datos.append("disponible_inventario", java.sql.Types.VARCHAR);
		_datos.append("encab_campos_dinamicos", java.sql.Types.VARCHAR);
		_datos.append("funciones_datepicker", java.sql.Types.VARCHAR);
		//_datos.append("cantidad_pedida", java.sql.Types.VARCHAR);
		_datos.append("combo_vehiculos", java.sql.Types.VARCHAR);
		_datos.append("icono_detalles_vehiculo", java.sql.Types.VARCHAR);
		_datos.append("vehicu_rol_id", java.sql.Types.VARCHAR);
		_datos.append("vehicu_nombre", java.sql.Types.VARCHAR);
		_datos.append("precio_compra", java.sql.Types.VARCHAR);
		_datos.append("monto_cant_comprar", java.sql.Types.VARCHAR);
		_datos.append("cantidades_blotter", java.sql.Types.VARCHAR);
		_datos.append("valor_por_unidad", java.sql.Types.VARCHAR);
		_datos.append("in_recompra", java.sql.Types.VARCHAR);
		_datos.append("datos_conyuge", java.sql.Types.VARCHAR);
		_datos.append("html_sitme", java.sql.Types.VARCHAR);
		_datos.append("fv_activa", java.sql.Types.VARCHAR);
		_datos.append("fechaValor", java.sql.Types.VARCHAR);
		_datos.append("fecha_pacto", java.sql.Types.VARCHAR);//Es fecha actual
		_actividadEconomica.append("actividadeconomica", java.sql.Types.VARCHAR);
		_sectorProductivo.append("sectorproductivo", java.sql.Types.VARCHAR);
		_conceptos.append("conceptos", java.sql.Types.VARCHAR);
		
		_datos.addNew();
		_datos.setValue("calculos", _req.getParameter("calculos"));
		_datos.setValue("montos_blotter", "");
		_datos.setValue("blotter_id", "");
		_datos.setValue("blotter_descripcion", "");
		_datos.setValue("combo_blotters_ui", "");
		_datos.setValue("campo_financiamiento", "0,00");
		_datos.setValue("disponible_inventario", "");
		_datos.setValue("encab_campos_dinamicos", "");
		_datos.setValue("funciones_datepicker", "");
		//_datos.setValue("cantidad_pedida", "");
		_datos.setValue("combo_vehiculos", "");
		_datos.setValue("icono_detalles_vehiculo", "");
		_datos.setValue("vehicu_rol_id", "");
		_datos.setValue("vehicu_nombre", "");
		_datos.setValue("rangos_precio_subasta", "");
		_datos.setValue("precio_compra", "");
		_datos.setValue("monto_cant_comprar", "");
		_datos.setValue("cantidades_blotter", "");
		_datos.setValue("valor_por_unidad", "");
		_datos.setValue("in_recompra", "NO");
		_datos.setValue("datos_conyuge", "");
		_datos.setValue("html_sitme", "");
		_datos.setValue("fv_activa", "");
		_datos.setValue("fechaValor", "");
		_datos.setValue("fecha_pacto", "");
		_actividadEconomica.addNew();
		_actividadEconomica.setValue("actividadeconomica", "");
		_sectorProductivo.addNew();
		_sectorProductivo.setValue("sectorproductivo", "");
		_conceptos.addNew();
		_conceptos.setValue("conceptos", "");

		if(datos_unidad_inv.count()>0 && exiteCuentaCustodia==true && exiteCuentaSwift==true){
			
			datos_unidad_inv.first();
			datos_unidad_inv.next();
						
			//Buscar Rangos de Validación de acuerdo al blotter
			uiBlotterRangos = uiBlotterRangosDAO.listarBlotterRangosObj(idUnidadInversion, idBlotter, cliente.getTipoPersona());
			
			if(uiBlotterRangos == null)
				 uiBlotterRangos = new UIBlotterRangos();
			
			//precio Mínimo a nivel de Blotter
			BigDecimal precioMinimoBlotter = uiBlotterRangos.getPrecioMinimo().setScale(6, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal undinv_porc_financ = uiBlotterRangos.getMaxFinanciamiento();

			//obtener datos del blotter asociado a la UI
			uiBlotterDAO.listarBlotterUI(idUnidadInversion, idBlotter);
			datosUIBlotter = uiBlotterDAO.getDataSet();
			if(datosUIBlotter.next()){
				indicaRecompra = Integer.parseInt(datosUIBlotter.getValue("uiblot_in_recompra"));
			}
			
			datos_unidad_inv.setValue("undinv_precio_minimo", uiBlotterRangos.getPrecioMinimo().toString());
			datos_unidad_inv.setValue("undinv_precio_maximo", uiBlotterRangos.getPrecioMaximo().toString());
			
			datos_unidad_inv.setValue("undinv_pct_max_finan", String.valueOf(undinv_porc_financ));

			//Unidad de inversión con Campo para Monto o Campo para Cantidad
			if(datos_unidad_inv.getValue("undinv_in_pedido_monto")!=null && datos_unidad_inv.getValue("undinv_in_pedido_monto").equals("1")){
				_datos.setValue("monto_cant_comprar", "<td>Monto a comprar:</td><td><INPUT TYPE='TEXT' VALUE='' style='text-align:right' NAME='monto_comprar' SIZE='12' MAXLENGTH='10' onkeypress=\"EvaluateText('%f',this)\"></td>");				
			
				//montos minimos y máximos obtenidos del Blotter
				datos_unidad_inv.setValue("undinv_umi_inv_mto_min", uiBlotterRangos.getMontoMinimoInversion().toString());
				datos_unidad_inv.setValue("undinv_umi_inv_mto_max", uiBlotterRangos.getMontoMaximoInversion().toString());
				
				_datos.setValue("montos_blotter",	"<tr>"+
				"<td>Monto M&iacute;nimo a Comprar:</td>"+
				"<td>@undinv_umi_inv_mto_min format-mask='#,###,##0.00'@</td>"+
				"<td>Monto M&aacute;ximo a Comprar:</td>"+
				"<td>@undinv_umi_inv_mto_max format-mask='#,###,##0.00'@</td></tr>");

			}else{
				_datos.setValue("monto_cant_comprar", "<td>Cantidad a Comprar:</td>"+ "<td><INPUT TYPE='text' size='12' style='text-align:right' VALUE='' NAME='cantidad_pedida' onkeypress='solo_numericos();'/></td>");
				
				//cantidades minimas y maximas obtenidas del blotter
				datos_unidad_inv.setValue("undinv_umi_um_cant_min", String.valueOf(uiBlotterRangos.getCantMinimaInversion()));
				datos_unidad_inv.setValue("undinv_umi_um_cant_max", String.valueOf(uiBlotterRangos.getCantMaximaInversion()));

				_datos.setValue("cantidades_blotter", "<tr>"+
				"<td>Cantidad M&iacute;nima a Comprar:</td>"+
				"<td>@undinv_umi_um_cant_min format-mask='#,###,##0.00'@</td>"+				
				"<td>Cantidad M&aacute;xima a Comprar:</td>"+
				"<td>@undinv_umi_um_cant_max format-mask='#,###,##0.00'@</td></tr>");

			}
			//System.out.println("Datos de UI: "+datos_unidad_inv);
			// ------------------------ HTML para UI tipo SITME --------------------------
			actividadEconomica.listar();
			sectorProductivo.listar();
			conceptos.listar();
			if(datos_unidad_inv.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				objfechaValor = fechaValorDAO.listarFechaConfiguradaEnBD(FechaValor.DEBITO_TOMA_ORDEN_SITME);
				
				if(objfechaValor.getNombre()!=null)
					fechaValor = df.format(objfechaValor.getFechaValor());
				
				objfechaValor = fechaValorDAO.listarFechaConfiguradaEnBD(FechaValor.RECOMPRA_TITULOS_SITME);
				fechaPacto = df.format(objfechaValor.getFechaValor());
								
				_datos.setValue("fv_activa","");
			}else{
				_datos.setValue("fv_activa","style='visibility: hidden'");
			}
			_datos.setValue("fechaValor", fechaValor);
			_datos.setValue("fecha_pacto", fechaPacto);
			// ----------------------- fin HTML para UI tipo SITME -----------------------
				
			//SI NO ES CARTERA PROPIA
			if(!inCarteraPropia){
				//SI el blotter permite recompra
				if(indicaRecompra==1 && !datos_unidad_inv.getValue("insfin_descripcion").equals("TITULOS_SITME")){
					armarComboSeleccionRecompra();
				}
				if(indicaRecompra==1 && datos_unidad_inv.getValue("insfin_descripcion").equals("TITULOS_SITME")){
					armarComboSeleccionRecompraSITME();
				}
			}
			
			//datos a mostrar  
			    
			//SUBASTA COMPETITIVA
			if(datos_unidad_inv.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA)){
				//permite editar el campo de Precio M&iacute;nimo
				
				//colocar campo para cantidad pedida
				_datos.setValue("valor_por_unidad", "<td>Monto por Unidad:</td><td>"+datos_unidad_inv.getValue("undinv_umi_unidad")+"</td>");
				
				//datos_unidad_inv.setValue("undinv_precio_minimo", Util.formatNumber(datos_unidad_inv.getValue("undinv_precio_minimo"), "#,###,##0.00"));
				_datos.setValue("precio_compra", "<INPUT TYPE='text' size='12' style='text-align:right' VALUE='"+ precioMinimoBlotter +"' NAME='undinv_precio_minimo' MAXLENGTH='9' onkeypress=\"EvaluateText('%f',this)\"/>");
				
				this.armarRangosPrecioCompra();
				
			}else{
				//INVENTARIO
				if(datos_unidad_inv.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO) || datos_unidad_inv.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO)){
					//Buscar titulo de la unidad de Inversi&oacute;n (para tipo inventario solo debe existir un &uacute;nico t&iacute;tulo)
					unidInversionDAO.listarTitulosPorUI(idUnidadInversion);
					unidInversionDAO.getDataSet().next();
					String idTitulo = unidInversionDAO.getDataSet().getValue("titulo_id");
					
					double rendimientoUi = 0;
					
					if(datos_unidad_inv.getValue("undinv_rendimiento")!=null)
						rendimientoUi = Double.parseDouble(datos_unidad_inv.getValue("undinv_rendimiento"));
					
					String undiv_precio_minimo = "0";
					//para inventario normal calcular precio
					if(datos_unidad_inv.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO)){
						undiv_precio_minimo = new BigDecimal(Utilitario.calcularPrecio(idTitulo, rendimientoUi, _dso)).setScale(6, BigDecimal.ROUND_HALF_EVEN).toString();						
					}else{//si es inventario con precio					
						undiv_precio_minimo = precioMinimoBlotter.setScale(6, BigDecimal.ROUND_HALF_EVEN).toString();
					}					
					
					double disp_inventario = 0;
					if(datos_unidad_inv.getValue("undinv_umi_inv_disponible")!=null)
						disp_inventario = Double.parseDouble(datos_unidad_inv.getValue("undinv_umi_inv_disponible"));
						
					_datos.setValue("disponible_inventario", "<tr>"+
					"<td>Inventario Disponible:</td>"+
					"<td>"+ Util.formatNumber(String.valueOf(disp_inventario), "#,###,##0.00")+ "</td>");
					
					_datos.setValue("precio_compra", "<INPUT TYPE='text' size='12' style='text-align:right' VALUE='"+ undiv_precio_minimo +"' MAXLENGTH='9' NAME='undinv_precio_minimo' readonly />");

				}else{
					//SUBASTA 
					_datos.setValue("precio_compra", "<INPUT TYPE='text' size='12' style='text-align:right' VALUE='"+ precioMinimoBlotter +"' MAXLENGTH='9' NAME='undinv_precio_minimo' readonly />");
					//colocar campo para cantidad pedida
					//_datos.setValue("cantidad_pedida", "<tr><td>Cantidad a Comprar:</td>"+ "<td><INPUT TYPE='text' size='12' style='text-align:right' VALUE='' NAME='cantidad_pedida' onkeypress='solo_numericos();'/></td></tr>");
					_datos.setValue("valor_por_unidad", "<td>Monto por Unidad:</td><td>"+datos_unidad_inv.getValue("undinv_umi_unidad")+"</td>");
					
					this.armarRangosPrecioCompra();
					
				}
			}
			//Cambios en los datos seg&uacute;n usuario especial
			//si el usuario conectado es un Usuario especial
			//verificar cada una de sus restricciones
			if(usuarioEspecial){
				
				user_esp.next();
				//si puede cambiar el blotter
				if(user_esp.getValue("usresp_multiblotter")!=null &&  user_esp.getValue("usresp_multiblotter").equals("1")){
				
					//construir combo de blotters asociados a la unidad de inversi&oacute;n
					//ya que el usuario conectado es un usuario especial
					//puede seleccionar un blotter
					String combo_blotters_ui = construirComboBlottersUI();
						
					_datos.setValue("combo_blotters_ui", combo_blotters_ui);
					////////////////////////////////////////////////////////////////////////
				
				}else{
					//buscarIdBlotterUsuario();
					//colocar valores del blotter
					_datos.setValue("blotter_id", "<INPUT TYPE='hidden' size='30' VALUE='"+ idBlotter +"' NAME='blotter_id'/>");
					_datos.setValue("blotter_descripcion", descripcionBlotter);

				}				
				
			
				//si el usuario especial puede tomar ordenes con financiamiento
				//y si la unidad de inversion seleccionada permite financiamiento
				if(user_esp.getValue("usresp_financiamiento")!=null && 
				   user_esp.getValue("usresp_financiamiento").equals("1")
				   && undinv_porc_financ.doubleValue() > 0){
					
					String campo_financ = "<INPUT TYPE='TEXT' VALUE='" + 0.00+ "' style='text-align:right' NAME='porc_financiar' SIZE='12' MAXLENGTH='5' onkeypress=\"EvaluateText('%f',this)\"/>";

					_datos.setValue("campo_financiamiento", campo_financ);
				}
					
				
			}else{
				//this.buscarIdBlotterUsuario();
				//colocar valores del blotter
				_datos.setValue("blotter_id", "<INPUT TYPE='hidden' size='30' VALUE='"+ idBlotter +"' NAME='blotter_id'/>");
				_datos.setValue("blotter_descripcion", descripcionBlotter);

				this.buscarVehiculoPorDefecto();				

			}		

			///buscar campos dinamicos
			campos_dinamicos = new DataSet();
			armarCamposDinamicos(datos_unidad_inv.getValue("tipo_producto_id"));		
			
			//SI NO ES CARTERA PROPIA
			if(!inCarteraPropia){
				//LLAMDA A ALTAIR BUSCAR CUENTAS ASOCIADAS AL CLIENTE SELECCIONADO y EXPORTAR DATASET CON LA INFORMACION DEL CLIENTE SELECCIONADO
				buscarCuentasCliente();	
				
				if(cliente.getTipoPersona().equals("V") || cliente.getTipoPersona().equals("E")){
					armarDatosAdicionalesCliente();
				}
				
				//--ARMAR CAMPO PARA VEHICULO
				this.armarVehiculo();
				///--------------------------
			}			
			
			
		}
		storeDataSet("datos_unidad_inv", datos_unidad_inv);				
		storeDataSet("indicadores_ui", indicadores_ui);	
		storeDataSet("requisitos_ui", requisitos_ui);		
		storeDataSet("titulos_ui", titulos_ui);	
		storeDataSet("titulos_ui", titulos_ui);	
		storeDataSet("blotters_ui", blotters_ui);	
		storeDataSet("cuentas_cliente", cuentasCliente);
		storeDataSet("mensajes", mensajes);
		
		///Obtener lista de vehiculos para cartera propia
		if(inCarteraPropia){
			//--ARMAR CAMPO PARA VEHICULO
			this.armarVehiculo();
			///--------------------------
		}
		
		/////Obtener lista de unidades de inversion////////////////////////
		//unidInversionDAO.listarUnidadesTomaOrden(idBlotter);

		//Resolucion de Incidencia (Se muestra unidades de otros productos en toma orden subasta titulos) en requerimiento TTS_443 NM26659_08/05/2014  
		//unidInversionDAO.listarUnidadesTomaOrden(idBlotter,false,ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA,ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		unidInversionDAO.listarUnidadesTomaOrden(idBlotter, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA,true); 
	
		storeDataSet( "unidades_inv", unidInversionDAO.getDataSet());			
		/////////////////////////////////////////////////////////////////////

			
		///////////////////////////////////////////////////////////
		storeDataSet("datos", _datos);	
		storeDataSet("record", _record);	
		storeDataSet("camposdinamicos", campos_dinamicos);
		storeDataSet("actividadeseconomicas", actividadEconomica.getDataSet());
		storeDataSet("sectoresproductivos", sectorProductivo.getDataSet());
		storeDataSet("conceptos", conceptos.getDataSet());
		
	}// Fin del Metodo Execute  . . . . .

	/**
	 * Armar el campo para veh&iacute;culo dependiendo si es o no usuario especial
	 * @throws Exception
	 */
	private void armarVehiculo() throws Exception {
	
		if(usuarioEspecial){		
				user_esp.first();
				user_esp.next();
			
			//si puede cambiar el veh&iacute;culo
			if(user_esp.getValue("usresp_cambio_vehiculo")!=null &&  user_esp.getValue("usresp_cambio_vehiculo").equals("1")){
			
				//construir combo de vehiculos asociados para la toma de orden
				//ya que el usuario conectado es un usuario especial
				//puede seleccionar un vehiculo
				String combo_vehiculos = this.construirComboVehiculos();
					
				_datos.setValue("combo_vehiculos", combo_vehiculos);
				////////////////////////////////////////////////////////////////////////
			
			}else{
				this.buscarVehiculoPorDefecto();				
	
			}
		}
		
	}

	/**
	 * Arma un c&oacute;digo htm para la solicitud de los datos del conyuge del cliente
	 * @throws Exception
	 */
	private void armarDatosAdicionalesCliente() throws Exception {
		
		String datosConyuge ="";
		
		datosConyuge = getResource("datos_conyuge.htm");
		
		_datos.setValue("datos_conyuge", datosConyuge);
	}

	/**
	 * Realiza la Búsqueda de las cuentas asociadas al cliente, y muestra un mensaje de error si ocurre algún error de conexión
	 * @throws Exception
	 */
	private void buscarCuentasCliente() throws Exception {
		String userWebServices = "";
		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		
		try{
			userWebServices = getUserName();
			try{
				//buscar usuario de WebServices				
				ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentas(String.valueOf(ciRifCliete), tipo_persona, userWebServices, _req.getRemoteAddr(),this._dso, Integer.parseInt(datos_unidad_inv.getValue("dias_apertura_cuenta"))); 
				cuentasCliente = manejadorDeClientes.cargarDataSet(listaCuentas);
				
			}catch(Exception e){
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas del cliente en arquitectura extendida");
				crearDataSetCuentasVacio();
			}catch(Throwable t){
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas del cliente en arquitectura extendida");
				crearDataSetCuentasVacio();
			}
		}catch(Exception e){
			mensajes.setValue("mensaje_error_user_webs", "Error consultando el usuario de web services");
			//crearDataSetCuentasVacio();
		}catch(Throwable t){
			mensajes.setValue("mensaje_error_user_webs", "Error consultando el usuario de web services");
			//crearDataSetCuentasVacio();
		}
	}

	/**
	 * Verifica si el usuario conectado es un usuario especial colocando la variable de usuario especial en falso o verdadero según sea el caso
	 * @throws Exception
	 */
	private void verificarUsuarioEspecial() throws Exception {
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO(_dsoSeguridad);	
		int usuario 			= Integer.parseInt((userEspecialDAO.idUserSession(getUserName())));
		userEspecialDAO.listar(String.valueOf(usuario));
		user_esp = userEspecialDAO.getDataSet();
		if(user_esp.count()>0){
			usuarioEspecial = true;
		}else{
			usuarioEspecial = false;
		}
	}

	/**
	 * Arma el html con los rangos establecidos para precio de compra
	 * @throws Exception
	 */
	private void armarRangosPrecioCompra() throws Exception {
		
		String rangos_subasta = "<tr>"+
		"<td>Precio M&iacute;nimo de Compra:</td>"+
		"<td>@undinv_precio_minimo format-mask='#,###,##0.00'@</td>";
		
		//para subasta competitiva mostrar precio maximo
		if(datos_unidad_inv.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA)){
			rangos_subasta += "<td>Precio M&aacute;ximo de Compra:</td>"+
			"<td>@undinv_precio_maximo format-mask='#,###,##0.00'@</td></tr>";

		}else rangos_subasta+="</tr>";

		_datos.setValue("rangos_precio_subasta", rangos_subasta);
	}

	/**
	 * Crea un combo de seleci&oacute;n para permitir o no recompra de algun titulo asociado a la unidad de inversi&oacute;n 
	 * @throws Exception
	 */
	private void armarComboSeleccionRecompra() throws Exception {
		String combo_recompra = "<SELECT NAME='in_recompra'>"+
		"<OPTION VALUE='0' SELECTED>NO</OPTION>"+
		"<OPTION VALUE='1'>SI</OPTION>"+		
		"</SELECT>";
		
		_datos.setValue("in_recompra", combo_recompra);
		
	}
	
	private void armarComboSeleccionRecompraSITME() throws Exception {
		String combo_recompra = "<SELECT NAME='in_recompra'>"+
		"<OPTION VALUE='1' SELECTED>SI</OPTION>"+
		"<OPTION VALUE='0'>NO</OPTION>"+		
		"</SELECT>";
		_datos.setValue("in_recompra", combo_recompra);
	}

	/**
	 * Construye un combo de selecci&oacute;n con los blotters asociados a la Unidad de Inversi&oacute;n.
	 * Es llamado en caso de que el Usuario tenga permitido cambiar el blotter para la Toma de Ordenes 
	 * @return String que contiene el combo con los blotters
	 * @throws Exception
	 */
	public String construirComboBlottersUI() throws Exception{		
				
		String combo_blotters_ui = "<SELECT NAME='blotter_id' SIZE='1' ONCHANGE='javascript:recargarPagina();'>"+				
		"<OPTION VALUE='' SELECTED>Seleccione</OPTION>";
		
		if(blotters_ui.count()>0){
			blotters_ui.first();
			while(blotters_ui.next()){	
				String selected="";
				//if(_record.getValue("blotter_id")!=null){
					if(idBlotter.equals(blotters_ui.getValue("bloter_id"))){
						selected="SELECTED";
					}					
				//}
				combo_blotters_ui +="<OPTION VALUE='"+blotters_ui.getValue("bloter_id")+"' " +selected+ ">"+blotters_ui.getValue("bloter_descripcion")+"</OPTION>";
			}			
		}
		combo_blotters_ui +="</SELECT>";
		//limpiar campos de blotter por defecto
		return combo_blotters_ui;
	}
	
	/**
	 * Construye un combo de selecci&oacute;n con los veh&iacute;culos tomadores para la toma de orden
	 * Es llamaddo en caso de que el Usuario tenga permitido cambiar el veh&iacute;culo para la Toma de Ordenes 
	 * @return String que contiene el combo con los veh&iacute;culos
	 * @throws Exception
	 */
	public String construirComboVehiculos() throws Exception{
		VehiculoRolDAO vehiculoRolDAO = new VehiculoRolDAO(_dso); 
		vehiculoRolDAO.listarTodos();
		DataSet vehiculos_rol = vehiculoRolDAO.getDataSet();
		
		//Verificar si es cartera Propia colocar funcion recargar pagina en ONCHANGE del combo
		//ya que el vehiculo en cartera propia sera el cliente y se necesitan recargar los datos al cambiar la seleccion del vehiculo
		String funcion = "";
		//if(inCarteraPropia)
			funcion = " ONCHANGE='javascript:recargarVehiculo();' ";
				
		//verificar si ya se selcciono un vehiculo
		String vehiculoSeleccionado = "";
		if(_record.getValue("vehicu_rol_id")!=null)
			vehiculoSeleccionado = _record.getValue("vehicu_rol_id");
		
		String combo_vehiculos = "<SELECT NAME='vehicu_rol_id' SIZE='1' " +funcion+ ">"+				
		"<OPTION VALUE='' SELECTED>Seleccione</OPTION>";
			
		while(vehiculos_rol.next()){
			String selected = "";
			String value_op = vehiculos_rol.getValue("vehicu_rol_id");
			if(value_op.equals(vehiculoSeleccionado))
				selected="SELECTED";
			
			combo_vehiculos +="<OPTION VALUE='"+value_op+"' "+selected+">"+vehiculos_rol.getValue("siglas_tomador")+ "-" + vehiculos_rol.getValue("siglas_colocador")+"-"+vehiculos_rol.getValue("siglas_veh_recompra")+"</OPTION>";
		}
		
		combo_vehiculos +="</SELECT>";		
		return combo_vehiculos;
	}	
	
	
	/**
	 * Busca el blotter asociado al usuario conectado y coloca los valores a mostrar el la vista
	 * @throws Exception
	 */
	public void buscarIdBlotterUsuario() throws Exception{
		
		SecurityDBUserInfo userInfo = new SecurityDBUserInfo(_dso);
		idBlotter = userInfo.getUserBlotterId(getUserName());
		if(idBlotter!="" && idBlotter!=null && !idBlotter.equals("0")){
			BlotterDAO blotterDAO = new BlotterDAO(_dso);
			blotterDAO.listar(idBlotter);
			if (blotterDAO.getDataSet().count()>0){
				blotterDAO.getDataSet().first();
				blotterDAO.getDataSet().next();
				descripcionBlotter = blotterDAO.getDataSet().getValue("BLOTER_DESCRIPCION");		
			}
			
		}
	}
	
	/**
	 * Busca el veh&iacute;culo por defecto para la toma de orden y coloca los valores a mostrar en la vista
	 * @throws Exception
	 */
	public void buscarVehiculoPorDefecto() throws Exception{
		VehiculoRolDAO vehiculoRolDAO = new VehiculoRolDAO(_dso); 
		vehiculoRolDAO.listarVehiculoRolPorDefecto();
		DataSet vehiculo_defecto = vehiculoRolDAO.getDataSet();
		try {			
		
			vehiculo_defecto.next();
			String nombreRolVeh =  vehiculo_defecto.getValue("siglas_tomador")+"-"+ vehiculo_defecto.getValue("siglas_colocador")+"-"+ vehiculo_defecto.getValue("siglas_veh_recompra");
	
			_datos.setValue("vehicu_rol_id", "<INPUT TYPE='hidden' size='30' VALUE='"+ vehiculo_defecto.getValue("vehicu_rol_id") +"' NAME='vehicu_rol_id'/>");
			_datos.setValue("vehicu_nombre", nombreRolVeh);

		} catch (Exception e) {
			throw new Exception("No se encontr&oacute; el Veh&iacute;culo por defecto para la Toma de Orden");
		}

	}
	/**
	 * Recupera los Campos Din&aacute;micos asociados a la Unidad de Inversi&oacute;n armando el html a mostrar
	 * @throws Exception
	 */
	public void armarCamposDinamicos(String tipoProd) throws Exception{
		
		CamposDinamicos camposDinamicosDAO = new CamposDinamicos(_dso);
		String encabezado = "";
		campos_dinamicos = camposDinamicosDAO.listarCamposDinamicosUnidadInversion(idUnidadInversion,1,tipoProd);
		
		if(campos_dinamicos.count()>0){
			StringBuffer sb = new StringBuffer();
			campos_dinamicos.first();
			
			for(int i=0; i<campos_dinamicos.count(); i++){
				campos_dinamicos.next();
				if(campos_dinamicos.getValue("func_datepicker")!=null && campos_dinamicos.getValue("func_datepicker").length()>0){
					sb.append(campos_dinamicos.getValue("func_datepicker"));
				}
				//System.out.println("TRTD: "+campos_dinamicos.getValue("trtd"));
				//System.out.println("FUNC DATEPICKER: "+campos_dinamicos.getValue("func_datepicker"));
			}
			_datos.setValue("funciones_datepicker", sb.toString());
			//System.out.println("FUNCIONES:\n"+sb.toString());
			
			encabezado = "<table border='0' cellspacing='1' cellpadding='2' width='100%' class='dataform'>"
		        +"<tr><th COLSPAN='4' ALIGN='center'>Datos Adicionales</th></tr></table>";
		 
			_datos.setValue("encab_campos_dinamicos", encabezado);
		}
		
	}
		
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();		
		if (flag)
		{	
			//Crear DataSet de Mensajes 
			mensajes = new DataSet();
			mensajes.append("mensaje_error_user_webs", java.sql.Types.VARCHAR);
			mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);
			mensajes.append("mensaje_error_cuenta_custodia", java.sql.Types.VARCHAR);
			mensajes.append("mensaje_error_cuenta_swift", java.sql.Types.VARCHAR);
			mensajes.append("cuenta_custodia", java.sql.Types.VARCHAR);
			mensajes.append("cuenta_swift", java.sql.Types.VARCHAR);
			mensajes.addNew();
			mensajes.setValue("mensaje_error_user_webs","");
			mensajes.setValue("mensaje_error_cuentas_cte","");
			mensajes.setValue("mensaje_error_cuenta_custodia","");
			mensajes.setValue("mensaje_error_cuenta_swift","");
			mensajes.setValue("cuenta_custodia","true");
			mensajes.setValue("cuenta_swift", "true");
			
			verificarUsuarioEspecial();
			
			UnidadInversionDAO unidInversionDAO = new UnidadInversionDAO(_dso);			
			UIBlotterDAO uiBlotterDAO = new UIBlotterDAO(_dso);
			BlotterDAO blotterDAO = new BlotterDAO(_dso);
			UIBlotterRangosDAO uiBlotterRangosDAO = new UIBlotterRangosDAO(_dso);
			UIBlotterRangos uiBlotterRangos = new UIBlotterRangos();
			VehiculoRolDAO vrDAO = new VehiculoRolDAO(_dso);
			TransaccionFijaDAO tfDAO = new TransaccionFijaDAO(_dso);

			////Buscar los datos asociados a la unidad de inversion/////
			
			//Buscar Blotter usuario
			buscarIdBlotterUsuario();	
			
			//Si el Url contiene el action de cartera propia (cartera-datos)
			//if(_req.getParameter("in_cartera_propia")!=null && _req.getParameter("in_cartera_propia").equals("1")){
			if(_req.getRequestURI().toUpperCase().indexOf("cartera-datos".toUpperCase())>-1){
				inCarteraPropia = true;					
			}
			//--------------------------------------------------------------------------------------------------------
						
			if(_req.getParameter("undinv_id")!=null && !_req.getParameter("undinv_id").equals("0")){
				
				//SI ES CARTERA PROPIA
				if(inCarteraPropia){
					
					if(_record.getValue("vehicu_rol_id")==null){
						
						_record.addError("Ordenes / Toma de Ordenes", "Debe seleccionar el veh&iacute;culo para tomar la orden por cartera propia");
						flag = false;	

					}else{					
						try {
							BuscarClientePorRifVehiculo();
						} catch (Exception e) {							
							Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
							mensajes.setValue("mensaje_error_cuentas_cte", "Error buscando el veh&iacute;culo en Altair.");
							return flag;
						}
					}
										
				}
				
				if(_record.getValue("blotter_id")!=null){
					//buscar datos del bloter seleccionado
					blotterDAO.listar(_record.getValue("blotter_id"));
					if(blotterDAO.getDataSet().next()){
						idBlotter = blotterDAO.getDataSet().getValue("bloter_id");
						descripcionBlotter = blotterDAO.getDataSet().getValue("bloter_descripcion");
					}
				}
				
				// 	1.-	Buscar informacion del Cliente
				idCliente = Long.parseLong(_record.getValue("client_id"));
			
				ClienteDAO clienteDAO = new ClienteDAO(null, _dso);
				clienteDAO.listarPorId(idCliente); 
				cliente = clienteDAO.getCliente();
				ciRifCliete = cliente.getRifCedula();
				tipo_persona= cliente.getTipoPersona();
				idUnidadInversion = Long.parseLong(_req.getParameter("undinv_id"));
				UIIndicadoresDAO uiIndicadoresDAO = new UIIndicadoresDAO(_dso);
				UITitulosDAO uiTitulosDAO = new UITitulosDAO(_dso);
				
				//indicadores de la UI
				uiIndicadoresDAO.listarIndicadoresPorUi(idUnidadInversion);
				indicadores_ui = uiIndicadoresDAO.getDataSet();
				
				//Requisitos de la UI
				uiIndicadoresDAO.listarRequisitosPorUi(idUnidadInversion);
				requisitos_ui = uiIndicadoresDAO.getDataSet();
				
				//titulos de la UI
				uiTitulosDAO.listarTitulosPorID(idUnidadInversion);
				titulos_ui = uiTitulosDAO.getDataSet();
				
				//Buscar los blotters asociados a la unidad de inversi&oacute;n
				uiBlotterDAO.listarBlottersUI(idUnidadInversion);
				blotters_ui = uiBlotterDAO.getDataSet();				
							
				//Buscar Rangos de Validación de acuerdo al blotter
				uiBlotterRangos = uiBlotterRangosDAO.listarBlotterRangosObj(idUnidadInversion, idBlotter, cliente.getTipoPersona());
		
				if(!usuarioEspecial){
					if(uiBlotterRangos==null){//Si el objeto con la informacion requerida de acuerdo al blotter, la UI y el tipo de persona es nulo, no es posible tomar la orden ni realizar las validadciones respectivas
						
						_record.addError("Ordenes / Toma de Ordenes", "El Blotter no puede tomar ordenes para los tipos de persona " +cliente.getTipoPersona() + " y la unidad de Inversi&oacute;n seleccionada");
						flag = false;	
					
					}
				}		
			}
			
			// Si la Unidad de Invesion es tipo SITME, se valida la exitencia de Cuenta Custodia del cliente
			// Custodia del Clienta para la toma de orden.
			unidInversionDAO.listarPorId(idUnidadInversion);
			if (idUnidadInversion!=0)
			{
				unidInversionDAO.getDataSet().next();
				if (unidInversionDAO.getDataSet().getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
					exiteCuentaCustodia=existeCuentaCustodia();
					exiteCuentaSwift=existeCuentaSwift();
				}
			}
			datos_unidad_inv = unidInversionDAO.getDataSet();				
			
			//Se valida que los codigos de operacion asociados al vehiculo del tomador para la transaccion TOMA DE ORDEN no sean vacios
			if(_req.getParameter("undinv_id")!=null && !_req.getParameter("undinv_id").equals("0")){
				if(_record.getValue("vehicu_rol_id")!=null && !_record.getValue("vehicu_rol_id").equals("")){
					vrDAO.listarVehiculoRolPorId(Long.parseLong(_record.getValue("vehicu_rol_id")));
					DataSet vehiRol = vrDAO.getDataSet();
					if(vehiRol.count() > 0){ //Si se encuentra el registro en Vehiculo - Rol
						vehiRol.first();
						vehiRol.next();
						//TransaccionFija tf = tfDAO.obtenerTransaccion(TransaccionNegocio.TOMA_DE_ORDEN, vehiRol.getValue("id_tomador"), datos_unidad_inv.getValue("INSFIN_ID"));
						tfDAO.obtenerTransaccion(TransaccionNegocio.TOMA_DE_ORDEN, vehiRol.getValue("id_tomador"), datos_unidad_inv.getValue("INSFIN_ID"));
						DataSet transFijasVeh = tfDAO.getDataSet();
						if(transFijasVeh.count() > 0) {
							//System.out.println("ENCONTRO "+transFijasVeh.count()+" TRANS VEHICULOS");
							transFijasVeh.first();
							for(int i=0; i<transFijasVeh.count(); i++) {
								transFijasVeh.next();
								if(transFijasVeh.getValue("cod_operacion_cte_deb")==null || transFijasVeh.getValue("cod_operacion_cte_deb").equals("") || transFijasVeh.getValue("cod_operacion_cte_cre")==null || transFijasVeh.getValue("cod_operacion_cte_cre").equals("") || transFijasVeh.getValue("cod_operacion_veh_deb")==null || transFijasVeh.getValue("cod_operacion_veh_deb").equals("") || transFijasVeh.getValue("cod_operacion_veh_cre")==null || transFijasVeh.getValue("cod_operacion_veh_cre").equals("")){
									//System.out.println("ENCONTRO COD NULL EN TRANSACCION: "+transFijasVeh.getValue("trnfin_nombre"));
									_record.addError("Ordenes / Toma de Ordenes", "Los c&oacute;digos de operaci&oacute;n de cliente y veh&iacute;culo (c&eacute;dito y d&eacute;bito) asociados a las transacciones fijas de la toma de orden y al veh&iacute;culo del tomador no pueden ser vac&iacute;os");
									flag = false;
								}
							}
						}
					}
				}
			}
		}
		return flag;	
	}
	
	public boolean existeCuentaCustodia() throws Exception{
		boolean cuentaCustodia=true;
		ClienteCuentasDAO cuentaCustodiaDAO = new ClienteCuentasDAO(_dso);
		idCliente = Long.parseLong(_record.getValue("client_id"));
		
		cuentaCustodiaDAO.getCuentaCustodia(idCliente);
		storeDataSet("cuentascustodias", cuentaCustodiaDAO.getDataSet());
		if(cuentaCustodiaDAO.getDataSet().count()==0){
			mensajes.setValue("mensaje_error_cuenta_custodia", "Cliente No Posee Cuenta Custodia.");
			mensajes.setValue("cuenta_custodia", "");
			cuentaCustodia=false;
		}
		//return true;
		return cuentaCustodia;
	}
	
	public boolean existeCuentaSwift() throws Exception{
		boolean cuenta=true;
		ClienteCuentasDAO cuentaSwift = new ClienteCuentasDAO(_dso);
		idCliente = Long.parseLong(_record.getValue("client_id"));
		ClienteDAO clienteDAO = new ClienteDAO(null, _dso);
		clienteDAO.listarPorId(idCliente); 
		cliente = clienteDAO.getCliente();
		ciRifCliete = cliente.getRifCedula();
		tipo_persona= cliente.getTipoPersona();
		if(cuentaSwift.existeCuentaClienteSwift(idCliente)==false){
			mensajes.setValue("mensaje_error_cuenta_swift", "Cliente No Posee Cuenta Swift.");
			mensajes.setValue("cuenta_swift", "");
			cuenta=false;
		}
		return cuenta;
	}
	
	/**
	 * Busca el cliente BDV acuerdo al rif y se obtiene el respectivo id
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private void BuscarClientePorRifVehiculo() throws NumberFormatException, Exception {
		 
		com.bdv.infi.dao.ClienteDAO clienteDAO = new com.bdv.infi.dao.ClienteDAO(_dso);
		String tipoPersona = "";	
		String rifVehiculo = "";
		if(_record.getValue("vehicu_rol_id")!=null){
			//--Buscar rif del vehiculo tomador
			VehiculoRolDAO vehiculoRolDAO = new VehiculoRolDAO(_dso);
			vehiculoRolDAO.listarVehiculoRolPorId(Long.parseLong(_record.getValue("vehicu_rol_id")));
			
			if(vehiculoRolDAO.getDataSet().next()){
				rifVehiculo = vehiculoRolDAO.getDataSet().getValue("rif_tomador");
				tipoPersona = rifVehiculo.substring(0,1);
				String rifNumero = Utilitario.obtenerNumeroRifCI(rifVehiculo);
				rifVehiculo = rifNumero;
			}
		}		
			
		if(rifVehiculo.equals("")){
			throw new Exception("No se encuentra el rif del veh&iacute;culo tomador.");
		}else{
			try{
				clienteDAO.listarPorCedRif(Long.parseLong(rifVehiculo));
			}catch(NumberFormatException nfe){
				throw new Exception("El rif del veh&iacute;culo tomador encontrado ('"+ rifVehiculo +"') no es un n&uacute;mero valido.");
			}
			if(clienteDAO.getDataSet().next()){//Cliente encontrado en INFI
				idCliente = Long.parseLong(clienteDAO.getDataSet().getValue("client_id"));
				tipoPersona = clienteDAO.getDataSet().getValue("tipper_id");
			}else{//cliente NO encontrado en INFI
				try {
					ManejoDeClientes manejoClientes = new ManejoDeClientes(_dso);
					//obtener y guardar cliente en altair
					manejoClientes.obtenerClienteAltair(rifVehiculo, tipoPersona, _req.getRemoteAddr(), _app, true, false, false, false, getUserName());	
					//obtener id de cliente almacenado en IFI
					clienteDAO.listarPorCedRif(Long.parseLong(rifVehiculo));			
					if(clienteDAO.getDataSet().next())//Cliente encontrado en INFI				
						idCliente = Long.parseLong(clienteDAO.getDataSet().getValue("client_id"));
				} catch (Exception e) {					
					Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
					//limpiar unidad de inversión seleccionada
					_record.setValue("undinv_id", "0");
					throw new Exception(e);
				}		
			}
			_record.setValue("client_id", String.valueOf(idCliente));
		}
	}
	
//	
//	private void formulaPrueba (){
//		
//		BigDecimal formulaPrueba= ((IND-INV-PUB / IND-INV-VIG) * (1 + ((TASA-INT-ANU/360) * PLAZO))) - 1) * 100  
//		           
//		           System.out.println("formulaPrueba : " + formulaPrueba);
//	}
	
	private void crearDataSetCuentasVacio() throws Exception {
	//Dataset limpio de cuentas del cliente
		cuentasCliente = new DataSet();
		cuentasCliente.append("numero", java.sql.Types.VARCHAR);
		cuentasCliente.append("tipo", java.sql.Types.VARCHAR);
		cuentasCliente.append("saldo_disponible", java.sql.Types.VARCHAR);
	
		cuentasCliente.addNew();
		cuentasCliente.setValue("numero", "01022456789456123541");
		cuentasCliente.setValue("tipo", "Corriente");
		cuentasCliente.setValue("saldo_disponible", "8000000.00");
    }
	
	//Este valor cambia dependiendo si es instrumento sitme o no
	private void establecerDiasDeAperturaDeCuenta(){
		
	}	
}