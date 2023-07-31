package models.ordenes.toma_de_orden;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.CalendarioDAO;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.FechaValorDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.PrecioRecompraDAO;
import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.dao.UIDocumentosDAO;
import com.bdv.infi.dao.UIIndicadoresDAO;
import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.dao.VehiculoRolDAO;
import com.bdv.infi.data.PrecioRecompra;
import com.bdv.infi.data.UIBlotterRangos;
import com.bdv.infi.data.UsuarioEspecial;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.FechaValor;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoCartas;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;
import com.bdv.infi_toma_orden.dao.ClienteDAO;
import com.bdv.infi_toma_orden.data.CampoDinamico;
import com.bdv.infi_toma_orden.data.Cliente;
import com.bdv.infi_toma_orden.data.OrdenTitulo;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.bdv.infi_toma_orden.logic.TomaDeOrdenesDivisas;
import com.bdv.infi_toma_orden.util.Utilitario;
/**
 * Clase que permite manejar las reglas de negocio para la Toma de Ordenes de INFI
 * @author MegaSoft Computacion para Banco de Venezuela
 */  
public class CalculadorDeTomaDeOrdenesDivisas extends AbstractModel{
	
	DecimalFormat dFormato1 = new DecimalFormat("###,###,##0.00");
	DecimalFormat dFormato2 = new DecimalFormat("#,##0.000000");
	DecimalFormat dFormato3 = new DecimalFormat("#,##0.0000000");
	private DataSet cuentasCliente = new DataSet();
	private DataSet cuentaSwiftCliente = new DataSet();

	/**
	 * Parametros de la Toma de Orden 
	 */
	private HashMap parametrosEntrada = new HashMap();
	private	TomaDeOrdenesDivisas boSTO;
	private DataSet datos_unidad_inv = null;
	private DataSet indicadores_ui = new DataSet();
	private DataSet requisitos_ui = new DataSet();
	private DataSet titulos_ui = new DataSet();
	private long idUnidadInversion = 0;	
	private DataSet _datos = new DataSet();
	private String idBlotter ="";
	private DataSet user_esp = null;
	private DataSet blotters_ui = new DataSet();
	private ArrayList ArrayCamposDinamicos = new ArrayList();
	private ArrayList ArrayRecompraTitulos = null;
	private ArrayList ArrayComisiones = null;
	private boolean indicaFinanciada = false;
	private boolean flag;
	private String ctaCliente ="";
	private String ctaNacDolaresCliente ="";
	private Cliente cliente;
	private boolean usuarioEspecial;
	BigDecimal porcentajeFinanciado = new BigDecimal(0);
	private UsuarioEspecial objUsuarioEspecial = new UsuarioEspecial();
	private DataSet mensajes = null;
	private boolean inCarteraPropia = false;
	private boolean recompraConNeteo = false;
	private BigDecimal montoTotalNeteo = new BigDecimal(0);
	private String idMonedaLocal = "";
	private String idMonedaUI = "";
	private boolean titulosSinPrecio = false;
	private boolean fechaRecompraNOValida = false;
	private Date hoy = new Date();
	private Date dateHoyFormat;
	private String fechaValorOrden;
	Date dateValor=new Date();
	SimpleDateFormat formatear	=new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy");
	private String nombreDataSource;
	BigDecimal intCaidosRecompra = new BigDecimal(0);
	private String insfFormaOrden;
	
	// 1.-	Simulador de Toma de Orden	
	TomaOrdenSimulada beanTOSimulada = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		// Se inicializa el Objeto beanTOSimulada.
		_req.getSession().setAttribute("beanTOSimulada",null);
		
		//DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		mensajes.setValue("mensaje_error_user_webs", "");
		mensajes.setValue("mensaje_error_cuentas_cte", "");	
		mensajes.setValue("mensaje_error_precio_recompra", "");	
		mensajes.setValue("mensaje_error_fecha_valor","");
				
		//Se Obtiene la fecha actual		
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);		
		dateHoyFormat = com.bdv.infi.util.Utilitario.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA);
	
		//Se Verifica si el usuario conectado es un usuario especial
		verificarUsuarioEspecial();
		
		// Se validad el Objeto para no Sobre escribir los calculos de la Toma de Orden.
		try {
			if (_req.getSession().getAttribute("beanTOSimulada")==null){
				System.out.println("-------------------------cALCULADOR");
				// Se invoca el metodo para los calculos en TomaDeOrdenesDivisas.java
				beanTOSimulada = boSTO.simuladorTO();
				_req.getSession().setAttribute("beanTOSimulada",beanTOSimulada);
			}
			else{
				beanTOSimulada= (TomaOrdenSimulada) _req.getSession().getAttribute("beanTOSimulada");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} catch (Throwable e) {
			throw new Exception(e);
		}
		
		//----Buscar la Moneda Local-------------------------
		try {
			MonedaDAO boMoneda = new MonedaDAO( _dso);
			idMonedaLocal = boMoneda.listarIdMonedaLocal();
			boMoneda = null;			
		} catch (Exception e) {
			throw new Exception(e);
		}
		//----------------------------------------------------
		
		//Datos de usuario especial
		user_esp.next();
		
		//	2.- Armar los DataSet de respuesta
		//	a.-	calcular remanente
		BigDecimal montoRemanente = (BigDecimal) parametrosEntrada.get("montoInversion");
		montoRemanente = montoRemanente.subtract((BigDecimal) parametrosEntrada.get("montoPedido"));
		
		if(!inCarteraPropia){
			//verificar si es con financiamiento no permitir pacto de recompra
			if(porcentajeFinanciado.doubleValue()>0){
				_record.setValue("in_recompra", "0");
			}
		}else{//para cartera propia no hay recompra
			_record.setValue("in_recompra", "0");
		}
		
		//	b.1.-	armar toma de orden
		DataSet dsTomaOrden = new DataSet();
		dsTomaOrden.append("cliente", java.sql.Types.VARCHAR);
		dsTomaOrden.append("monto_inversion", java.sql.Types.VARCHAR);
		dsTomaOrden.append("monto_financiado", java.sql.Types.VARCHAR);
		dsTomaOrden.append("monto_pedido", java.sql.Types.VARCHAR);
		dsTomaOrden.append("cantidad_ordenada", java.sql.Types.VARCHAR);
		dsTomaOrden.append("monto_comisiones", java.sql.Types.VARCHAR);
		dsTomaOrden.append("monto_interes_caidos", java.sql.Types.VARCHAR);
		dsTomaOrden.append("monto_total", java.sql.Types.VARCHAR);
		dsTomaOrden.append("tasa_cambio", java.sql.Types.VARCHAR);
		dsTomaOrden.append("precio_compra", java.sql.Types.VARCHAR);
		dsTomaOrden.append("monto_total_neteo", java.sql.Types.VARCHAR);
		dsTomaOrden.append("id_moneda_local", java.sql.Types.VARCHAR);
		dsTomaOrden.append("sector_productivo", java.sql.Types.VARCHAR);
		dsTomaOrden.append("actividad_econimica", java.sql.Types.VARCHAR);
		dsTomaOrden.append("concepto", java.sql.Types.VARCHAR);
		dsTomaOrden.append("tasa_propuesta", java.sql.Types.VARCHAR);
		
		dsTomaOrden.addNew();
		dsTomaOrden.setValue("id_moneda_local", idMonedaLocal);
		dsTomaOrden.setValue("cliente", cliente.getNombre());
		dsTomaOrden.setValue("monto_inversion", dFormato1.format(beanTOSimulada.getMontoInversion().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		dsTomaOrden.setValue("monto_financiado", dFormato1.format(beanTOSimulada.getMontoFinanciado().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		dsTomaOrden.setValue("monto_pedido", dFormato1.format(beanTOSimulada.getMontoPedido().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		dsTomaOrden.setValue("cantidad_ordenada", String.valueOf((beanTOSimulada.getCantidadOrdenada())));
		dsTomaOrden.setValue("monto_comisiones", dFormato1.format(beanTOSimulada.getMontoComisiones().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		dsTomaOrden.setValue("monto_interes_caidos", dFormato1.format(beanTOSimulada.getMontoInteresCaidos().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		dsTomaOrden.setValue("monto_total", dFormato1.format(beanTOSimulada.getMontoTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		dsTomaOrden.setValue("tasa_cambio", dFormato2.format(beanTOSimulada.getTasaCambio().setScale(6, BigDecimal.ROUND_HALF_EVEN)));
		dsTomaOrden.setValue("actividad_econimica", beanTOSimulada.getActividadEconomica());
		dsTomaOrden.setValue("sector_productivo", beanTOSimulada.getSectorProductivo());
		dsTomaOrden.setValue("concepto", beanTOSimulada.getConcepto());
		dsTomaOrden.setValue("tasa_propuesta", _record.getValue("tasa_propuesta"));
		
		BigDecimal precio_compra = new BigDecimal(_record.getValue("undinv_precio_minimo"));
		dsTomaOrden.setValue("precio_compra", precio_compra.setScale(6, BigDecimal.ROUND_HALF_EVEN).toString());
		
		//	b.2.-	armar titulos de respuesta
		DataSet dsOrdenTitulo = new DataSet();
		dsOrdenTitulo.append("titulo_id", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("descripcion_titulo", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("valor_nominal", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("porcentaje", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("unidades_invertidas", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("monto_interes_caidos", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("valor_invertido", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("titulo_moneda_siglas", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("tasa_pool", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("moneda_negociacion", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("indicador_moneda_local", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("num_titulo", java.sql.Types.VARCHAR);
		
		// Valores para Ordenes de Recompra SITME . . . .
		dsOrdenTitulo.append("fecha_valor_recompra", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("int_caidos_recompra", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("cupon", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("diferencia_dias", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("valor_efectivo", java.sql.Types.VARCHAR);
		
		// Campos para recompra del titulo				
		dsOrdenTitulo.append("check_in_recompra", java.sql.Types.VARCHAR);
		dsOrdenTitulo.append("titulos_precio_recompra", java.sql.Types.VARCHAR);
		
		_datos = new DataSet();		
		_datos.append("colspan_titulos", java.sql.Types.VARCHAR);
		_datos.append("encab_in_recompra", java.sql.Types.VARCHAR);
		_datos.append("encab_tasa_pool", java.sql.Types.VARCHAR);
		_datos.append("botom_recalcular", java.sql.Types.VARCHAR);
		_datos.addNew();
		
		
		int numColTitulos = 5; //se asignan 5 fijas
		
		// Campos para precio de recompra
		if(_record.getValue("in_recompra")!=null && _record.getValue("in_recompra").equals("1")){
			numColTitulos++;//sumar columna dinamica para checkbox de recompra
			_datos.setValue("encab_in_recompra", "<th width='6%'>Recom.</th>");			
		}else{
			_datos.setValue("colspan_titulos", "5");
			_datos.setValue("encab_in_recompra", "<div style="+"display:none"+">"+"&nbsp"+"</div>");	
		}
		
		if(!inCarteraPropia){
			// Campos para tasa pool
			if(usuarioEspecial){
				numColTitulos++; //sumar columna dinamica para la tasa pool
				_datos.setValue("encab_tasa_pool", "<th>Tasa Pool</th>");				
			}else{
				_datos.setValue("encab_tasa_pool", "<div style="+"display:none"+">"+"&nbsp"+"</div>");
			}		
		}
		
		//SE ELIMINA EL BOTON RECALCULAR PARA SIMADI TAQUILLA
		if(insfFormaOrden.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){
			_datos.setValue("botom_recalcular", "<div id='oculto' style='display:none;'><button TYPE='button' name='boton_recalcular' onclick='enviarRecalcular();'>Recalcular</button></div>"); 
		}else {
			_datos.setValue("botom_recalcular", "<button TYPE='button' name='boton_recalcular' onclick='enviarRecalcular();'>Recalcular</button>"); 
		}
		
		// Asignar el numero de columnas en html para el encabezado de titulos
		_datos.setValue("colspan_titulos", String.valueOf(numColTitulos));
		
		// Inicio de Recorrido de Titulos  . . . . . .
		OrdenTitulo beanOT;
		ArrayList listaBase = beanTOSimulada.getListaOrdenTitulo();
		for (int i=0; i<listaBase.size(); i++) {
			
			beanOT = (OrdenTitulo)listaBase.get(i);
			String editadorRecompra = "";
			BigDecimal porcRecompra = new BigDecimal(0);
			BigDecimal tasaPool = new BigDecimal(0);
			
			dsOrdenTitulo.addNew();
			dsOrdenTitulo.setValue("titulo_id", beanOT.getIdTitulo());
			dsOrdenTitulo.setValue("descripcion_titulo", beanOT.getIdTitulo());
			dsOrdenTitulo.setValue("valor_nominal", dFormato1.format(beanOT.getValorNominal().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			dsOrdenTitulo.setValue("porcentaje", dFormato3.format(beanOT.getPorcentaje().setScale(7, BigDecimal.ROUND_HALF_EVEN)));
			dsOrdenTitulo.setValue("unidades_invertidas", String.valueOf(beanOT.getUnidadesInvertidas()));
			dsOrdenTitulo.setValue("monto_interes_caidos", String.valueOf(beanOT.getMontoIntCaidos()));
			dsOrdenTitulo.setValue("valor_invertido", dFormato1.format(beanOT.getValorInvertido().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			dsOrdenTitulo.setValue("titulo_moneda_siglas", beanOT.getSiglasMoneda());
			dsOrdenTitulo.setValue("moneda_negociacion", beanOT.getMonedaNegociacion());
			dsOrdenTitulo.setValue("num_titulo", String.valueOf(i+1));
			dsOrdenTitulo.setValue("fecha_valor_recompra", "N/A");
			dsOrdenTitulo.setValue("int_caidos_recompra", "N/A");
			dsOrdenTitulo.setValue("cupon", "N/A");
			dsOrdenTitulo.setValue("diferencia_dias","N/A");
			dsOrdenTitulo.setValue("valor_efectivo", "N/A");
			
			if(beanOT.getPorcentajeRecompra()==null || beanOT.getPorcentajeRecompra().doubleValue()==0){
				editadorRecompra ="readonly";
			}
			
			if(!inCarteraPropia){//Si NO es cartera propia
				//obtener precios de recompra en BD del titulo
				PrecioRecompraDAO precioRecompraDAO = new PrecioRecompraDAO(_dso); 
				PrecioRecompra precioRecompra = new PrecioRecompra();
				precioRecompra = precioRecompraDAO.obtenerPrecioRecompraTitulo(beanOT.getIdTitulo(), beanOT.getValorInvertido(), beanOT.getSiglasMoneda(), datos_unidad_inv.getValue("tipo_producto_id"));
				
				// Se busca y setea la fecha valor para la recompra de acuerdo al modelo de negocio . . . . .
				String fechaRecompraOrden = null;
				
				if(datos_unidad_inv.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
					fechaRecompraOrden = formatear.format(buscarFechaValorOrden(FechaValor.RECOMPRA_TITULOS_SITME));
				}else{
					fechaRecompraOrden = formatear.format(buscarFechaValorOrden(FechaValor.RECOMPRA_TITULOS));
				}
				
				fechaValorOrden= _req.getSession().getAttribute("fe_valor").toString();
				_req.getSession().setAttribute("fechaRecompra",fechaRecompraOrden);
				
				if(parametrosEntrada.get("recompraTitulos")!=null){
					
					// Se realiza la validacion entre Fecha Valor de Recompra y Fecha de la Toma De Orden
					Date c1= dformat.parse(fechaRecompraOrden);
					Date c2= dformat.parse(fechaValorOrden);
					if (c1.before(c2)) {
						fechaRecompraNOValida=true;
					}
					
					// Se invoca la llamada al Calculo de Intereses Caidos Para Titulos en Reecompra  . . . . .
					//Utilitario.calcularInteresesCaidos(beanTOSimulada.getMontoPedido(), beanOT.getIdTitulo(),fechaRecompraOrden, nombreDataSource, _dso, beanOT);
					dsOrdenTitulo.setValue("diferencia_dias",String.valueOf(beanOT.getDiferenciaDias()));
					dsOrdenTitulo.setValue("fecha_valor_recompra", beanOT.getFecha_valor_recompra());
					intCaidosRecompra=beanOT.getMontoIntCaidos().divide(beanTOSimulada.getTasaCambio());
					dsOrdenTitulo.setValue("int_caidos_recompra", dFormato1.format(intCaidosRecompra.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
					dsOrdenTitulo.setValue("cupon", beanOT.getCupon().multiply(new BigDecimal(100)).toString());
					dsOrdenTitulo.setValue("diferencia_dias",String.valueOf(beanOT.getDiferenciaDias()));
					BigDecimal valorEfectivo=(beanTOSimulada.getMontoPedido().multiply(beanOT.getPorcentajeRecompra()).divide(new BigDecimal(100))).add(intCaidosRecompra);
					dsOrdenTitulo.setValue("valor_efectivo", dFormato1.format(valorEfectivo.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
					
				}
				//Guardar fecha recompra en sesion

				//Si la fecha de la actualizacion del precio del título no es del día no se puede recomprar
				//si la fecha es distinta de null se encontro un registro de precios
				if ((precioRecompra.getFecha_act()!=null && precioRecompra.getFecha_act().compareTo(dateHoyFormat)!=0)
						|| !precioRecompraDAO.esAprobadoPrecioRecompraTitulo(precioRecompra)){
					precioRecompra.setTitulo_precio_recompra(new BigDecimal(0));
					precioRecompra.setTasaPool(new BigDecimal(0));
				}
				
				porcRecompra = precioRecompra.getTitulo_precio_recompra();
				tasaPool = precioRecompra.getTasaPool();
				
				//Compara los precios de recompra simulados (los cuales pudieron alterarse si se hizo un RECALCULO)
				//con los precios originales guardados en BD
				if(beanOT.getPorcentajeRecompra()!=null){
					if(beanOT.getPorcentajeRecompra().doubleValue()==0){
						beanOT.setPorcentajeRecompra(porcRecompra);
					}
				}else{
					beanOT.setPorcentajeRecompra(porcRecompra);
				}
				
				if(beanOT.getPrecioMercado()!=null){
					if(beanOT.getPrecioMercado().doubleValue()==0){
						beanOT.setPrecioMercado(tasaPool);
					}
				}else{
					beanOT.setPrecioMercado(tasaPool);
				}
				

				//colocar campos para valores de recompra
				if(usuarioEspecial && objUsuarioEspecial.isCambioPrecioTomaOrden() && _record.getValue("in_recompra")!=null && _record.getValue("in_recompra").equals("1")){
					//campo para recompra
					String campo_precio_recompra = "<INPUT TYPE='text' size='12' style='text-align:right' VALUE='" +beanOT.getPorcentajeRecompra().setScale(6, BigDecimal.ROUND_HALF_EVEN)+ "' NAME='titulos_precio_recompra_"+beanOT.getIdTitulo()+"' MAXLENGTH='9' " +editadorRecompra+ " onkeypress=\"EvaluateText('%f',this)\"/>";
					dsOrdenTitulo.setValue("titulos_precio_recompra", campo_precio_recompra);
				
					//campo para tasa pool
					String campo_tasa_pool = "<td align='right' style='padding-right=10px;'><INPUT TYPE='text' size='12' style='text-align:right' VALUE='" +beanOT.getPrecioMercado().setScale(6, BigDecimal.ROUND_HALF_EVEN)+ "' NAME='tasa_pool_"+beanOT.getIdTitulo()+"' MAXLENGTH='9' " +editadorRecompra+ " onkeypress=\"EvaluateText('%f',this)\"/>%</td>";
					dsOrdenTitulo.setValue("tasa_pool", campo_tasa_pool);
	
				}else{				
					//campo para recompra
					dsOrdenTitulo.setValue("titulos_precio_recompra", dFormato2.format(beanOT.getPorcentajeRecompra().setScale(6, BigDecimal.ROUND_HALF_EVEN)) + " <INPUT TYPE='hidden' size='12' VALUE='" +beanOT.getPorcentajeRecompra().setScale(6, BigDecimal.ROUND_HALF_EVEN)+ "' NAME='titulos_precio_recompra_"+beanOT.getIdTitulo()+"'/>" );
					
					//campo para tasa pool
					if(usuarioEspecial){
						dsOrdenTitulo.setValue("tasa_pool", "<td align='right' style='padding-right=10px;'>" +dFormato2.format(beanOT.getPrecioMercado().setScale(6, BigDecimal.ROUND_HALF_EVEN)) + "% <INPUT TYPE='hidden' size='12' VALUE='" +beanOT.getPrecioMercado().setScale(6, BigDecimal.ROUND_HALF_EVEN)+ "' NAME='tasa_pool_"+beanOT.getIdTitulo()+"'/></td>" );
					}else{
						dsOrdenTitulo.setValue("tasa_pool", "<div style="+"display:none"+">"+"&nbsp"+"</div>");
					}
					
				}
				
				//Sumar monto por Neteo (Puede ser cero para los titulos que no estan en Bolivares o
				//si la unidad de inversión no permite recompra con neteo o si es una orden por cartera propia
				montoTotalNeteo = montoTotalNeteo.add(beanOT.getMontoNeteo());
			}//FIN de Si NO es cartera propia.
			  
			//Verificar si la moneda de negociacion del titulo es local o extranjera
			MonedaDAO monedaDAO = new MonedaDAO(_dso);	
			//si es moneda local
			if(monedaDAO.listarIsMonedaLocal(beanOT.getMonedaNegociacion())){
				dsOrdenTitulo.setValue("indicador_moneda_local", "1");
			}else{
				dsOrdenTitulo.setValue("indicador_moneda_local", "0");
			}

			if(!inCarteraPropia){
				//colocar campos de chequeo para recompra del titulo
				if(_record.getValue("in_recompra")!=null && _record.getValue("in_recompra").equals("1")){
					String checked_recompra ="";					
					String chequeado = verificarTituloChequeadoRecompra(beanOT.getIdTitulo());
										
					if(beanOT.getPorcentajeRecompra().doubleValue()==0 || fechaRecompraNOValida){//deshabilitar checkbox de recompra
						checked_recompra = "<td CLASS='rtablecell_blanco' align='center'><INPUT TYPE='checkbox' DISABLED NAME='tit_recompra' VALUE='" +beanOT.getIdTitulo()+"'></td>";
					}else{//habilitar checkbox de recompra
						checked_recompra = "<td CLASS='rtablecell_blanco' align='center'><INPUT TYPE='checkbox' " +chequeado+ " NAME='tit_recompra' ONCLICK='mostrarDatosPago(); enviarRecalcular();' VALUE= '"+beanOT.getIdTitulo()+"'></td>";
					}
					
					dsOrdenTitulo.setValue("check_in_recompra", checked_recompra);
					
					//Verificar si el precio de recompra y la tasa pool quedaron diferentes de cero
					//Agregar mensaje de alerta a tabla de mensajes
					if(beanOT.getPorcentajeRecompra().doubleValue()==0 || beanOT.getPrecioMercado().doubleValue()==0){
						titulosSinPrecio = true;						
					}
					//---Buscar cuentas para el cliente para el pago en caso de pacto de recompra---
					buscarCuentasCliente();
					if (cuentaSwiftCliente.count()>0){
						cuentaSwiftCliente.next();
						parametrosEntrada.put("idInstruccion", cuentaSwiftCliente.getValue("ctes_cuentas_id"));
						// Necesaria para insertarClienteCuentas() en ClienteCuentasDAO
					}
				}else{
					dsOrdenTitulo.setValue("check_in_recompra", "<div style="+"display:none"+">"+"&nbsp"+"</div>");
				}
			}
		}
		

		
		//Agregar mensaje de titulos sin precio de recompra
		if(titulosSinPrecio){
			mensajes.setValue("mensaje_error_precio_recompra", ConstantesGenerales.MENSAJE_PRECIOS_RECOMPRA + datos_unidad_inv.getValue("tipo_producto_id"));
		}
		//mensaje de Fecha valor No valida
		if(fechaRecompraNOValida){
			mensajes.setValue("mensaje_error_fecha_valor", ConstantesGenerales.MENSAJE_FECHA_VALOR);
		}
		
		//	c.-	armar operaciones de respuesta
		DataSet dsOrdenOperacionComision = new DataSet();
		dsOrdenOperacionComision.append("id_transaccion", java.sql.Types.VARCHAR);
		dsOrdenOperacionComision.append("id_comision", java.sql.Types.VARCHAR);
		dsOrdenOperacionComision.append("descripcion_transaccion", java.sql.Types.VARCHAR);
		dsOrdenOperacionComision.append("monto_operacion", java.sql.Types.VARCHAR);
		dsOrdenOperacionComision.append("tasa", java.sql.Types.VARCHAR);
		dsOrdenOperacionComision.append("operacion_moneda_siglas", java.sql.Types.VARCHAR);		
		
		DataSet dsOrdenOperacionOtras = new DataSet();
		dsOrdenOperacionOtras.append("descripcion_transaccion", java.sql.Types.VARCHAR);
		dsOrdenOperacionOtras.append("monto_operacion", java.sql.Types.VARCHAR);
		dsOrdenOperacionOtras.append("tasa", java.sql.Types.VARCHAR);
		dsOrdenOperacionOtras.append("operacion_moneda_siglas", java.sql.Types.VARCHAR);		
		
		if(!inCarteraPropia){
			listaBase = beanTOSimulada.getListaOperaciones();
			int contadorComisiones = 0;
			if(!listaBase.isEmpty()){
							
				for (int i=0; i<listaBase.size(); i++) {
				
				//for (Iterator iter=listaBase.iterator(); iter.hasNext();){
					OrdenOperacion beanOO = new OrdenOperacion();
					//beanOO = (OrdenOperacion) iter.next();
					String campoComision = "";
					beanOO = (OrdenOperacion)listaBase.get(i);					
					
						
						if (beanOO.getInComision()==1) {
							
							dsOrdenOperacionComision.addNew();
							dsOrdenOperacionComision.setValue("id_transaccion", beanOO.getIdTransaccionFinanciera());
							dsOrdenOperacionComision.setValue("id_comision", "<INPUT TYPE='hidden' VALUE='"+beanOO.getIdOperacion()+"' NAME='id_comision_"+beanOO.getIdTransaccionFinanciera()+"_"+contadorComisiones+"'/>");
							dsOrdenOperacionComision.setValue("descripcion_transaccion", beanOO.getNombreOperacion());
							dsOrdenOperacionComision.setValue("monto_operacion", dFormato1.format(beanOO.getMontoOperacion().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
							dsOrdenOperacionComision.setValue("operacion_moneda_siglas", beanOO.getSiglasMoneda());
						
							if(usuarioEspecial && objUsuarioEspecial.isCambioComision()){
								campoComision = "<INPUT TYPE='text' size='12' style='text-align:right' VALUE='" +beanOO.getTasa().setScale(4, BigDecimal.ROUND_HALF_EVEN)+ "' NAME='comision_"+beanOO.getIdTransaccionFinanciera()+"_"+contadorComisiones+"' MAXLENGTH='8' onkeypress=\"return NumCheck(event, this)\"/>";
								
							}else{
								campoComision = dFormato1.format(beanOO.getTasa().setScale(2, BigDecimal.ROUND_HALF_EVEN));
							}
							contadorComisiones++;
						
							dsOrdenOperacionComision.setValue("tasa", campoComision );
							
						}
						
						dsOrdenOperacionOtras.addNew();
						dsOrdenOperacionOtras.setValue("descripcion_transaccion", beanOO.getNombreOperacion());
						
						dsOrdenOperacionOtras.setValue("monto_operacion", dFormato1.format(beanOO.getMontoOperacion().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
						dsOrdenOperacionOtras.setValue("tasa", dFormato1.format(beanOO.getTasa().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
						dsOrdenOperacionOtras.setValue("operacion_moneda_siglas", beanOO.getSiglasMoneda());
				}
			}
			

			
			if(recompraConNeteo){
				//Colocar Total por Cocepto de Neteo
				dsTomaOrden.setValue("monto_total_neteo", "<tr><td>&nbsp;</td>"+
				"<td><b>Monto Total por Neteo:<b/></td>"+
				"<td align='right' style='padding-right=10px;'><b> - "+dFormato1.format(montoTotalNeteo.setScale(2, BigDecimal.ROUND_HALF_EVEN))+ " " +idMonedaLocal +"</b></td></tr>");
			}
			
			//---Verificador de usuario especial para cambiar instrucciones de pago
			if(usuarioEspecial && objUsuarioEspecial.isIngresoInstruccionesPago()){
				_record.setValue("usuario_especial", "1");//permite ingresar			
			}else{				
				_record.setValue("usuario_especial", "0");//NO permite ingresar	
			}
			///--------------------------------------------------------------------
		}
		
		storeDataSet("dsTomaOrden", dsTomaOrden);
		storeDataSet("dsOrdenTitulo", dsOrdenTitulo);
		storeDataSet("dsOrdenOperacionComision", dsOrdenOperacionComision);
		storeDataSet("dsOrdenOperacionOtras", dsOrdenOperacionOtras);
		storeDataSet("datos_unidad_inv", datos_unidad_inv);
		storeDataSet("datos", _datos);
		
			
		//Colocar el hashmap de parametros de entrada en la sesi&oacute;n
		_req.getSession().setAttribute("parametrosEntrada", parametrosEntrada);
		
		//indicadores de la UI
		UIIndicadoresDAO uiIndicadoresDAO = new UIIndicadoresDAO(_dso);
		uiIndicadoresDAO.listarIndicadoresPorUi(idUnidadInversion);
		indicadores_ui = uiIndicadoresDAO.getDataSet();
		storeDataSet("indicadores_ui", indicadores_ui);
		
		//Requisitos
		uiIndicadoresDAO.listarRequisitosPorUi(idUnidadInversion);
		requisitos_ui = uiIndicadoresDAO.getDataSet();
		storeDataSet("requisitos_ui", requisitos_ui);
		
		getDataSetDocumentos();
		
		cargarCamposDinamicos();
		
		//verificar recalculos
		verificarCantidadRecalculos();	
		
		storeDataSet("param", _record);
		
		storeDataSet("cuentas_cliente", cuentasCliente);
		storeDataSet("cuentaswift", cuentaSwiftCliente);
		storeDataSet("mensajes", mensajes);
		_req.getSession().setAttribute("beanTOSimulada",beanTOSimulada);
	}// FIN METODO EXECUTE . . . .
	
	/**
	 * Busca un t&iacute;tulo en particular para verificar si fue marcado para recompra
	 * @param idTitulo
	 * @return "checked" si el titulo esta marcado, vacio en caso contrario
	 */
	private String verificarTituloChequeadoRecompra(String idTitulo) {
		
		String chequeado = "";
		String[] inRecompraTitulos = _req.getParameterValues("tit_recompra");		
		//Verificar si el titulo fue marcado en la lista de titulos chequeados para recompra
		if(inRecompraTitulos!=null){
			
			for(int k = 0;k< inRecompraTitulos.length;k++){ 				
				
				if(inRecompraTitulos[k].equals(idTitulo)){
					chequeado = "checked";
					break;
				}					
			}
		}
		return chequeado;
	}

	/**
	 * Realiza la Búsqueda de las cuentas asociadas al cliente, y muestra un mensaje de error si ocurre algún error de conexión
	 * @throws Exception
	 */
	private void buscarCuentasCliente() throws Exception {
		String userWebServices = "";
		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		ClienteCuentasDAO cuentaSwift = new ClienteCuentasDAO(_dso);
		try{
			userWebServices = getUserName();
		
			try{
				//buscar usuario de WebServices				
				ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentas(String.valueOf(cliente.getRifCedula()), cliente.getTipoPersona(), userWebServices, _req.getRemoteAddr(),this._dso,Integer.parseInt(datos_unidad_inv.getValue("dias_apertura_cuenta"))); 
				cuentasCliente = manejadorDeClientes.cargarDataSet(listaCuentas);
				//Buscando cuenta Swift
				long idCliente=cliente.getIdCliente();
				cuentaSwift.listarCuentaSwift(idCliente);
				cuentaSwiftCliente=cuentaSwift.getDataSet();
			}catch(Exception e){
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas del cliente en arquitectura extendida");
			}
		  }catch(Exception e){
			mensajes.setValue("mensaje_error_user_webs", "Error consultando el usuario de web services");
		}
	}

	/**
	 * Verifica la cantidad de c&aacute;lculos que se han hecho y lo almacena en el record.
	 * Este dato es utilizado para la navegaci&oacute;n en la pantalla
	 * @throws Exception
	 */
	private void verificarCantidadRecalculos() throws Exception {
		if(_req.getParameter("recalculo")!=null){
			int cantCalculos = Integer.parseInt(_record.getValue("cantidad_calculo"));
			cantCalculos++;
			_record.setValue("cantidad_calculo", String.valueOf(cantCalculos));
		}else{
			_record.setValue("cantidad_calculo", "1");
		}		
	}

	/**
	 * Carga los valores de los campos dinamicos recibidos en un dataSet para ser reemplazados en el html.
	 * Esto es en caso de que se realice un recalculo conservar los par&aacute;metros enviados desde la pantalla de datos
	 * @throws Exception
	 */
	private void cargarCamposDinamicos() throws Exception {
		DataSet camposDin = new DataSet();
		camposDin.append("valor", java.sql.Types.VARCHAR);
		camposDin.append("nombre_campo", java.sql.Types.VARCHAR);

		if(!ArrayCamposDinamicos.isEmpty()){
			for(int i=0;i<ArrayCamposDinamicos.size();i++){
				CampoDinamico campoDinamico = (CampoDinamico) ArrayCamposDinamicos.get(i);
				camposDin.addNew();			
				camposDin.setValue("valor", campoDinamico.getValor());
				camposDin.setValue("nombre_campo", "campo_dinamico_"+campoDinamico.getIdCampo());
				
			}
		}		
		storeDataSet("campos_din", camposDin);		
	}

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
	 * Verifica si el usuario conectado es un usuario especial colocando la variable de usuario especial en falso o verdadero según sea el caso
	 * @throws Exception
	 */
	private void verificarUsuarioEspecial() throws Exception {
		
			
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));

		UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO(_dsoSeguridad);	
		int usuario 			= Integer.parseInt((userEspecialDAO.idUserSession(getUserName())));
		userEspecialDAO.listar(String.valueOf(usuario));
		
		user_esp = userEspecialDAO.getDataSet();
		
		if(user_esp.count()>0){			
			objUsuarioEspecial = userEspecialDAO.obtenerUsuarioEspecial(user_esp);
			usuarioEspecial = true;
		}else{
			usuarioEspecial = false;
		}
		
	}
	/**
	 * Recupera los campos din&aacute;micos asociados a la orden y los almacena en una lista
	 * @return ArrayList con la lista de valores de los campos din&aacute;micos
	 * @throws Exception
	 */ 
	public ArrayList recuperarCamposDinamicos() throws Exception{
		
		DataSet campos_dinamicos = null;
		ArrayList ArrCamposDinamicos = new ArrayList();
		//Recuperar campos dinamicos asociados a la unidad de inversion
		CamposDinamicos camposDinamicosDAO = new CamposDinamicos(_dso);			
		campos_dinamicos = camposDinamicosDAO.listarCamposDinamicosUnidadInversion(idUnidadInversion,0);

		while(campos_dinamicos.next()){
			_app.equals("");
			CampoDinamico campoDinamico = new CampoDinamico();
			
			campoDinamico.setIdCampo(Integer.parseInt(campos_dinamicos.getValue("campo_id")));
			campoDinamico.setNombreCampo(campos_dinamicos.getValue("campo_nombre"));
						
			//campoDinamico.setTipoDato(campos_dinamicos.getValue("campo_tipo"));
			if(_req.getParameter("campo_dinamico_"+campos_dinamicos.getValue("campo_id"))!=null && !_req.getParameter("campo_dinamico_"+campos_dinamicos.getValue("campo_id")).equals("")){
				campoDinamico.setValor(_req.getParameter("campo_dinamico_"+campos_dinamicos.getValue("campo_id")));
			}
			
			ArrCamposDinamicos.add(campoDinamico);
		}
		
		return ArrCamposDinamicos;
		//////////////////////////////////////////////////////////////

	}	
	
	
	/**
	 * Recupera en una lista los títulos que estan chequeados para recompra.
	 * @return ArrayList con la lista de titulos con recompra.
	 * @throws Exception
	 */
	public ArrayList recuperarRecompraTitulos() throws Exception{
		//recuperar titulos con recompra chequeados
		String[] inRecompraTitulos = _req.getParameterValues("tit_recompra");
		ArrayList<PrecioRecompra> ArrRecompraTitulos = null;
		if(inRecompraTitulos!=null){	
			
			ArrRecompraTitulos = new ArrayList<PrecioRecompra>();
				
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
			}
		}
		return ArrRecompraTitulos;
	}


	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception {
		
		flag = super.isValid();			
		
		if (flag)
		{	
			//NM29643 - INFI_TTS_443 23/03/2014: Se verifica si se viene de sicad2
			//Se crean e inicializan  los mensajes de error a mostrar en pagina de Calculos. 
			mensajes = new DataSet();
			mensajes.append("mensaje_error_user_webs", java.sql.Types.VARCHAR);
			mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);	
			mensajes.append("mensaje_error_precio_recompra", java.sql.Types.VARCHAR);
			mensajes.append("mensaje_error_fecha_valor", java.sql.Types.VARCHAR);
			mensajes.append("mensaje_error_ultrat", java.sql.Types.VARCHAR);
			mensajes.append("menu_migaja", java.sql.Types.VARCHAR);
			mensajes.append("sicad2", java.sql.Types.VARCHAR);
			mensajes.addNew();
			
			if(_req.getParameter("sicad2")!=null && _req.getParameter("sicad2").equals(ConstantesGenerales.VERDADERO+"")){
				mensajes.setValue("menu_migaja", "Toma de Orden SIMADI");
				mensajes.setValue("sicad2", ConstantesGenerales.VERDADERO+"");
			}else{
				mensajes.setValue("menu_migaja", "Toma de Orden Subasta Divisas");
				mensajes.setValue("sicad2", ConstantesGenerales.FALSO+"");
			}
			
			//----Verificar si es Cartera propia---------------------------------------------------------------------
			if(_req.getParameter("in_cartera_propia")!=null && _req.getParameter("in_cartera_propia").equals("1")){
				inCarteraPropia = true; //TODO No aplica cartera propia para subasta divisas
			}
			// Captuaramos el blotter que nos llega de la pantalla anterior			
			idBlotter = _req.getParameter("blotter_id");			
			UnidadInversionDAO unidInversionDAO = new UnidadInversionDAO(_dso);			
			UIBlotterDAO uiBlotterDAO = new UIBlotterDAO(_dso);
			VehiculoRolDAO vehiculoRolDAO = new VehiculoRolDAO(_dso);
			UIBlotterRangosDAO uiBlotterRangosDAO = new UIBlotterRangosDAO(_dso);
			UIBlotterRangos uiBlotterRangos = new UIBlotterRangos();
			////Buscar los datos asociados a la unidad de inversion/////
			
							
			//obtener datos asociados a la unidad de inversion
			idUnidadInversion = Long.parseLong(_record.getValue("undinv_id"));
			UIIndicadoresDAO uiIndicadoresDAO = new UIIndicadoresDAO(_dso);
			UITitulosDAO uiTitulosDAO = new UITitulosDAO(_dso);
	
			//indicadores de la UI
			uiIndicadoresDAO.listarIndicadoresPorUi(idUnidadInversion);
			indicadores_ui = uiIndicadoresDAO.getDataSet();
			
			//titulos de la UI
			uiTitulosDAO.listarSubastas(idUnidadInversion);
			titulos_ui = uiTitulosDAO.getDataSet();
				
			//Buscar los blotters asociados a la unidad de inversi&oacute;n
			uiBlotterDAO.listarBlottersUI(idUnidadInversion);
			blotters_ui = uiBlotterDAO.getDataSet();
					
			unidInversionDAO.listarPorId(idUnidadInversion);
			datos_unidad_inv = unidInversionDAO.getDataSet();				
				
				
			//Recuperar campos dinamicos asociados a la unidad de inversion
			ArrayCamposDinamicos = this.recuperarCamposDinamicos();
				
			if(!inCarteraPropia){
				//Recuperar Comisiones. En caso de que vengan por parametro por recalculo
				ArrayComisiones = this.obtenerComisionesOrden();

				///Recuperar Recompra de Titulos
				ArrayRecompraTitulos = this.recuperarRecompraTitulos();
		
			}else{
				ArrayComisiones = null;
			}
					
			if(datos_unidad_inv.count()>0){
				datos_unidad_inv.next();
				idMonedaUI = datos_unidad_inv.getValue("moneda_id_ui");
				// 	1.-	Buscar informacion del Cliente
				Long idCliente = new Long(_req.getParameter("client_id"));
					
				ClienteDAO clienteDAO = new ClienteDAO(null, _dso);
				clienteDAO.listarPorId(idCliente.longValue()); 
				cliente = clienteDAO.getCliente();
				
				
				
				if(!inCarteraPropia){
					//---Verificar indicador de RECOMPRA CON NETEO---------------------------------------------------------------------------------------
					if(datos_unidad_inv.getValue("undinv_in_recompra_neteo")!=null && datos_unidad_inv.getValue("undinv_in_recompra_neteo").equals("1")){
						recompraConNeteo = true;
					}
					//------------------------------------------------------------------------------------------------------------------------------------
				}
				
				//NM32454 LPEREZ 20/08/2015 SE AGREGA LA FORMA ORDEN PARA LA VALIDACION DEL REQUERIMIENTO DE SIMADI TAQUILLA
				insfFormaOrden = _record.getValue("insfin_forma_orden") == null ? "0" : _record.getValue("insfin_forma_orden"); 
				
				if(insfFormaOrden.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){
					//NM32454 LPEREZ 20/08/2015 SE VALIDA MONTO MAXIMO PARA LA ORDEN. SE SUMA EL MONTO EFECTIVO CON EL MONTO ELECTRONICO
					BigDecimal montoMaximoUI           = new BigDecimal(datos_unidad_inv.getValue("UNDINV_UMAX_UNIDAD") == null ? "0" : datos_unidad_inv.getValue("UNDINV_UMAX_UNIDAD"));
					BigDecimal montoComprarEfectivo    = new BigDecimal(_record.getValue("monto_comprar_efec") == null ? "0" : _record.getValue("monto_comprar_efec")); 
					BigDecimal montoComprarElectronico = new BigDecimal(_record.getValue("monto_comprar") == null ? "0" : _record.getValue("monto_comprar"));
					Integer    multiploEfectivoUI      = new Integer(datos_unidad_inv.getValue("UNDINV_MULTIPLOS_EFECTIVO") == null ? "1" : datos_unidad_inv.getValue("UNDINV_MULTIPLOS_EFECTIVO"));
					
					BigDecimal montoTotal = montoComprarEfectivo.add(montoComprarElectronico);
					
					int comparacion = montoTotal.compareTo(montoMaximoUI);
					//SE VALIDA QUE EL MONTO TOTAL DE LA OPERACION NO SUPERE EL MONTO MAXIMO CONFIGURADO
					if(comparacion == 1){
						_record.addError("Monto Total: ", "El monto total de la operacion no puede superar "+montoMaximoUI+ " dolares.");
						flag = false;
					}
					
					//SE VALIDA MULTIPLO DE INVERSION DEL EFECTIVO
					double resto = montoComprarEfectivo.doubleValue() % multiploEfectivoUI.doubleValue();
					if(resto > 0){				
						_record.addError("Multiplo de Inversion: ","El Monto en Efectivo a Invertir (" +montoComprarEfectivo+ ") debe ser m&uacute;ltiplo de " + multiploEfectivoUI);
						flag = false;
					}
					
					parametrosEntrada.put("instru_financiero_form_orden", insfFormaOrden);
					parametrosEntrada.put("monto_compra_efectivo", montoComprarEfectivo);
					parametrosEntrada.put("monto_maximo_ui", montoMaximoUI);
				}
				
				//Buscar Rangos de Validación de acuerdo al blotter
				uiBlotterRangos = uiBlotterRangosDAO.listarBlotterRangosObj(idUnidadInversion, idBlotter, cliente.getTipoPersona());
				if(uiBlotterRangos!=null){//Si el objeto con la informacion requerida de acuerdo al blotter, la UI y el tipo de persona NO es nulo, es posible tomar la orden ni realizar las validadciones respectivas
								//Indicador de financiamiento						
								BigDecimal porcentajeMaxFinan = uiBlotterRangos.getMaxFinanciamiento();						
								
								//Si NO es Cartera Propia
								if(!inCarteraPropia){
									
									if(porcentajeMaxFinan.doubleValue() > 0){
										indicaFinanciada = true;//PERMITE FINANCIAMIENTO
																
										if(_record.getValue("porc_financiar")!=null){
											porcentajeFinanciado = new BigDecimal(_record.getValue("porc_financiar"));
											if(porcentajeFinanciado.doubleValue()==0){
												indicaFinanciada = false;
											}
										}
										
									}else{//NO PERMITE FINANCIAMIENTO
										porcentajeMaxFinan = new BigDecimal(0);
										porcentajeFinanciado = new BigDecimal(0);			
									}
									
								}else{
									porcentajeMaxFinan = new BigDecimal(0);
									porcentajeFinanciado = new BigDecimal(0);
								}
										
							
								
								//	2.-	Armar los parametros  	
								
								long cantidadPedida = 0;
								if(_record.getValue("cantidad_pedida")!=null)
									cantidadPedida = Long.parseLong(_record.getValue("cantidad_pedida"));
					
								BigDecimal montoComprar = new BigDecimal(0) ;	
								BigDecimal tasaPropuesta = new BigDecimal(0) ;
								BigDecimal tasaReferencial = new BigDecimal(0);
								
									
									//VALIDACIONES PARA UNIDAD DE INVERSIÓN CON MONTO
								
									if(datos_unidad_inv.getValue("undinv_in_pedido_monto")!=null && datos_unidad_inv.getValue("undinv_in_pedido_monto").equals("1")){
									
										if(_record.getValue("monto_comprar")!=null){							
																		
											montoComprar = new BigDecimal(_record.getValue("monto_comprar"));										
			
										}
										if(_record.getValue("tasa_referencial")!=null){
											tasaReferencial = new BigDecimal(_record.getValue("tasa_referencial")).setScale(4, BigDecimal.ROUND_HALF_EVEN);
										}
										//System.out.println("REFERENCIAL------------------- "+tasaReferencial);
										if(_record.getValue("tasa_propuesta")!=null){							
											
											tasaPropuesta = new BigDecimal(_record.getValue("tasa_propuesta")).setScale(4);										
											//System.out.println("TASA PROPUESTAAAAAAA: "+tasaPropuesta);
										}
										//System.out.println("PROPUESTA------------------- "+tasaPropuesta);
										if(tasaPropuesta.doubleValue() > 0){
											//System.out.println("COMPARACIONNNN///////////// "+tasaPropuesta.compareTo(tasaReferencial));
											//if(tasaPropuesta.doubleValue() < tasaReferencial.doubleValue()){
											if(tasaPropuesta.compareTo(tasaReferencial) < 0 ){ //Valor negativo significa que el parametro es mayor que el objeto sobre el que se invoca el metodo
												_record.addError("tasa Propuesta", "La tasa propuesta no puede ser menor que la tasa de cambio referencial");
												flag = false;
											}
										}
									}
									
								if(flag){
									//Consultar datos del blotter asociado a la UI
									uiBlotterDAO.listarBlotterUI(idUnidadInversion, idBlotter);
									DataSet blotter_ui = null;
									BigDecimal gananciaRedBlotter = new BigDecimal(0);
									
									blotter_ui = uiBlotterDAO.getDataSet();
									
									if(blotter_ui.next()){
										
										gananciaRedBlotter = new BigDecimal(blotter_ui.getValue("uiblot_ganancia_red"));
										//disponibilidad del blotter
																			
																			
									}else{
										BlotterDAO blotterDAO = new BlotterDAO(_dso);
										
										blotterDAO.listar(idBlotter);
										blotterDAO.getDataSet().next();
										//Blotter no asociado a la Unidad de Inversi&oacute;n.
										/*_record.addError("Ordenes / Toma de Ordenes", "El blotter "+blotterDAO.getDataSet().getValue("bloter_descripcion")+ " no est&aacute; asociado a la Unidad de Inversi&oacute;n seleccionada");
										flag = false;	*/
								
									}														
																	
				
									if(_record.getValue("cta_cliente")!=null){
										ctaCliente = _record.getValue("cta_cliente");										
									}else{//Para cartera propia
										ctaCliente = null;
									}
									
									//Almacena la Cuenta Nacional en Dolares Seleccionada
									if(_record.getValue("cta_nac_dolares_cte")!=null){
										ctaNacDolaresCliente = _record.getValue("cta_nac_dolares_cte");										
									}else{//Para cartera propia
										ctaNacDolaresCliente = null;
									}
				
								///////////////////////////////////////////////////////////////////////////
								
								//PARAMETROS DE ENTRADA
								//Obtener vehiculos dado el rol
								String vehiculoTomador="";
								String vehiculoColocador="";
								String vehiculoRecompra="";
								if(_record.getValue("vehicu_rol_id")!=null){
									vehiculoRolDAO.listarVehiculoRolPorId(Long.parseLong(_record.getValue("vehicu_rol_id")));
									if(vehiculoRolDAO.getDataSet().next()){
										vehiculoTomador = vehiculoRolDAO.getDataSet().getValue("id_tomador");
										vehiculoColocador = vehiculoRolDAO.getDataSet().getValue("id_colocador");
										vehiculoRecompra = vehiculoRolDAO.getDataSet().getValue("id_veh_recompra");
									}
								}
								//Datos de conyuge de cliente 
								String estadoCasado = null;
								String cedulaConyuge= null;
								String tipoPersonaConyuge= null;	
								
								//--Si NO es cartera propia
								if(!inCarteraPropia){
									
									if(cliente.getTipoPersona().equalsIgnoreCase("V") || cliente.getTipoPersona().equalsIgnoreCase("E")){
										//si esta casadiiisimo
										if(_record.getValue("estado_casado")!=null){
											estadoCasado = _record.getValue("estado_casado");
											if(_record.getValue("estado_casado").equalsIgnoreCase("SI")){											
												if(_record.getValue("cedula_conyuge")!=null && _record.getValue("tipo_persona_conyuge")!=null){
													cedulaConyuge= _record.getValue("cedula_conyuge");
													tipoPersonaConyuge= _record.getValue("tipo_persona_conyuge");
												}
											}
										}
									}
								}
								
								//----Obtener parametros de archivo de inicio guardado en sesion
								String nroOficina = null;
								if(_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL)!=null){
									nroOficina = (String)_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);
								}else{
									//throw new Exception("No se encuentra el n&uacute;mero de oficina");									
								}
								//--------------------------------------------------------------------
												
								//-------------
								
								//Orden Instrumento SITME
								// Se setean variables SITME en Sesion para luego guardar en los parametrosEntrada
								if(datos_unidad_inv.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
								//if((_req.getParameter("fe_pacto")!=null) && (_req.getParameter("fe_valor")!=null)){
									
									fechaValorOrden = formatear.format(buscarFechaValorOrden(FechaValor.DEBITO_TOMA_ORDEN_SITME));
									_req.getSession().setAttribute("fe_pacto",_req.getParameter("fe_pacto"));									
									_req.getSession().setAttribute("fe_valor",fechaValorOrden);
									_req.getSession().setAttribute("actividad_economica",_req.getParameter("actividad_economica"));
									_req.getSession().setAttribute("sector_productivo",_req.getParameter("sector_productivo"));
									_req.getSession().setAttribute("concepto",_req.getParameter("concepto"));
								}else{
									//Orden Instrumento Subasta
									fechaValorOrden = formatear.format(new Date());
									_req.getSession().setAttribute("fe_valor",fechaValorOrden);
								}
								
								// Parametros de Entrada Utilizados tambien en Adjudicacion . . . .
								parametrosEntrada.put("fechaPacto", _req.getSession().getAttribute("fe_pacto"));
								parametrosEntrada.put("fechaValor", _req.getSession().getAttribute("fe_valor"));
								parametrosEntrada.put("fechaRecompra", _req.getSession().getAttribute("fechaRecompra"));
								parametrosEntrada.put("actividadEconomica", _req.getSession().getAttribute("actividad_economica"));
								parametrosEntrada.put("sectorProductivo", _req.getSession().getAttribute("sector_productivo"));
								parametrosEntrada.put("concepto", _req.getSession().getAttribute("concepto"));
									
								if(parametrosEntrada.get("fechaValor")!=null){
									// Verifica que Fecha Valor no este en calendario		
									CalendarioDAO calendarioDAO = new CalendarioDAO(_dso);
									boolean diaFeriado = calendarioDAO.esDiaFeriado(parametrosEntrada.get("fechaValor").toString());
									if (diaFeriado==true){
										_record.addError("Ordenes / Toma de Ordenes", "Fecha Valor es un Día Feriado");
										flag = false;	
										//throw new Exception("Fecha Valor es un Día Feriado . . .");
									}
									//--------------
									//Verificar que la fecha valor no es menor a la fecha actual
									
									if(formatear.parse(parametrosEntrada.get("fechaValor").toString()).before(formatear.parse(formatear.format(new Date())))){
										_record.addError("Ordenes / Toma de Ordenes", "Fecha Valor es menor al d&iacute;a de hoy.");
										flag = false;	
									}									

								}else{
									_record.addError("Ordenes / Toma de Ordenes", "La Fecha Valor No se ha configurado correctamente para la toma de orden");
									flag = false;	
								}
								
								parametrosEntrada.put("idUnidadInversion", new Long(idUnidadInversion));
								parametrosEntrada.put("idBlotter", _record.getValue("blotter_id"));
								parametrosEntrada.put("tipoPersona",  cliente.getTipoPersona());
								parametrosEntrada.put("cedulaCliente", String.valueOf(cliente.getRifCedula()));
								//NM29643 - INFI_TTS_443 08/04/2014: Se agrega el nombre del cliente (nombre beneficiario Instruccion Pago Cta Nac Dolares)
								parametrosEntrada.put("nombreCliente", cliente.getNombre());
								parametrosEntrada.put("montoInversion", montoComprar);
								parametrosEntrada.put("tasaPropuesta", tasaPropuesta);
								//System.out.println("TASA PARAMETROS: "+parametrosEntrada.get("tasaPropuesta"));
								//parametrosEntrada.put("montoMaximoComprar", new BigDecimal(_record.getValue("monto_max_sol")));
								parametrosEntrada.put("cantidadComprar", new Long(cantidadPedida));			
								parametrosEntrada.put("precioOfrecido", new BigDecimal(_record.getValue("undinv_precio_minimo")));
								parametrosEntrada.put("numeroCuentaCliente", ctaCliente);
								parametrosEntrada.put("numeroCuentaNacDolaresCliente", ctaNacDolaresCliente);
								parametrosEntrada.put("porcentajeFinanciado", porcentajeFinanciado);
								parametrosEntrada.put("camposDinamicos", ArrayCamposDinamicos);
								
								parametrosEntrada.put("ipTerminal", _req.getRemoteAddr());
								parametrosEntrada.put("idVehiculoTomador", vehiculoTomador);
								parametrosEntrada.put("idVehiculoColocador", vehiculoColocador);
								parametrosEntrada.put("idVehiculoRecompra", vehiculoRecompra);
								parametrosEntrada.put("userName", getUserName());
								parametrosEntrada.put("IndicadorFinanciada", new Boolean(indicaFinanciada));
									
								parametrosEntrada.put("recompraTitulos", ArrayRecompraTitulos);
								parametrosEntrada.put("gananciaRedBlotter", gananciaRedBlotter);
								//tipo de Transaccion de Negocio
								
								//------------Asignar tipio de transacción-------------------------------------------------
								if(inCarteraPropia){
									parametrosEntrada.put("tipoTransaccionNegocio", TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
								}else{
									parametrosEntrada.put("tipoTransaccionNegocio", TransaccionNegocio.TOMA_DE_ORDEN);
								}	
								//------------------------------------------------------------------------------------------
								
								//***Nuevo parametro comisiones 27-08-2008
								parametrosEntrada.put("comisionesOrden", ArrayComisiones);
								
								//Servlet Context como parametro
								parametrosEntrada.put("servletContext", _app);
								
								//parametros del conyuge del cliente
								parametrosEntrada.put("estadoCasado", estadoCasado);
								parametrosEntrada.put("tipoPersonaConyuge", tipoPersonaConyuge);
								parametrosEntrada.put("cedulaConyuge", cedulaConyuge);
								
																
								//--Parametros para instrucciones de pago
								parametrosEntrada.put("instruccionPagoRecompra", null);	
								
								//--Parametro que indica que sólo se realizaran calculos: viene del calcular
								parametrosEntrada.put("calculador", true);	
								
								///--Parametro Nro de Oficina o Sucursal
								parametrosEntrada.put("numeroOficina", nroOficina);
								
								parametrosEntrada.put("montoPedidoEfectivo", _record.getValue("monto_comprar_efec"));
								
								}//if flag
							}
				
							if(flag){
							
								// 	3.-	Validar la aplicacion de la transaccion
								
								ArrayList listaMensajes = new ArrayList();
						
								try {
									boSTO = new TomaDeOrdenesDivisas(_dso);									
									boSTO.setParametrosEntrada(parametrosEntrada);	
									// Se Invoca Metodo validar() en TomaDeOrdenesDivisas.
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
									flag = false;
								}				
							}
				}else{
					//Sino se seleccion&oacute; una unidad de inversi&oacute;n para hacer los c&aacute;lculos
					_record.addError("Ordenes / Toma de Ordenes", "Debe ingresar los datos de la Unidad de Inversi&oacute;n");
					flag = false;	
					
				}
		}
		
		//Valida la compra que desea hacer el cliente
		
		return flag;	
	}
	
	/**
	 * Almacena en un dataset los documentos que se desean mostrar antes de tomar la orden
	 * @throws Exception en caso de error
	 */	
	private void getDataSetDocumentos() throws Exception{
		try {
			
			DataSet ds = new DataSet();
			ds.append("documento_id", java.sql.Types.VARCHAR);
			ds.append("nombre_doc", java.sql.Types.VARCHAR);
			ds.append("iddocunico", java.sql.Types.VARCHAR);
			
			//Busca los documentos tipo 8		
			UIDocumentosDAO uiDocumentosDAO = new UIDocumentosDAO(_dso); 
			uiDocumentosDAO.listarNombreDoc(idBlotter,cliente.getTipoPersona(),String.valueOf(idUnidadInversion));
			DataSet dsTemp = uiDocumentosDAO.getDataSet();
			dsTemp.first();
			
			while(dsTemp.next()){
				if (dsTemp.getValue("tipcar_id").equals(String.valueOf(TipoCartas.CARTA_ANT_ORDEN))){
					ds.addNew();
					ds.setValue("documento_id", dsTemp.getValue("documento_id"));
					ds.setValue("nombre_doc", dsTemp.getValue("nombre_doc"));
					ds.setValue("iddocunico", dsTemp.getValue("uidoc_unico"));
				}
			}			
			storeDataSet("documentos_ui", ds);
			
		}catch(Exception ex){
			throw new Exception("No se encuentran configurados los documentos asociados a este tipo de persona");
		}
		
	}
	
	/**
	 * Método para buscar la fecha valor correspondiente 
	 * @return fecha valor
	 * @throws Exception
	 */
	private Date buscarFechaValorOrden(int idFecha) throws Exception {
		com.bdv.infi.data.FechaValor objFechaValor = new com.bdv.infi.data.FechaValor();
		FechaValorDAO fechaValorDAO = new FechaValorDAO(_dso);
		objFechaValor = fechaValorDAO.listarFechaConfiguradaEnBD(idFecha);
		Date feValor = null;
				
		if(objFechaValor.getNombre()!=null){
			feValor = objFechaValor.getFechaValor();
		}
		return feValor;
	}

	
	
}


