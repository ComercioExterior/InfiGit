package models.ordenes.consultas.datos_ordenes_exportar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.Utilitario;

/**
 * @author eel
 * 
 */
public class ExportarOrdenes extends ExportableOutputStream {

	boolean cabeceraRecompras = false;
	boolean cabeceraCamposDinamicos = false;
	boolean ordenesCliente          = false;
	ResultSet ordenesTitulos = null;
	ResultSet ordenesCamposDinamicos = null;
	StringBuilder sb = new StringBuilder();
	StringBuilder sbcd = new StringBuilder();
	String nombreMonedaBs = "";
	String tipoProducto="";
	OrdenDAO ordenDAO=null;
	DataSet record =null;
	public void execute() throws Exception {
		record= (DataSet) _req.getSession().getAttribute("datos_ordenes_exportar-browse.framework.page.record");
		tipoProducto=record.getValue("tipo_producto_id")==null?"":record.getValue("tipo_producto_id");
		
		if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_DICOM)){						
			reporteDicom();			
		} else {			
			reporteGenerico();					
		}			 	
	}

	public void reporteDicom() throws Exception {
			
				
			ordenDAO=new OrdenDAO(_dso);
			DataSet solicitudesDicom = null;	
			long idUnidad=0;
			String idBlotter, status, fechaDesde, fechaHasta, tipoProductoId;
			idUnidad=Long.parseLong(record.getValue("unidad_inversion"));
			idBlotter=record.getValue("blotter");
			status=record.getValue("status");
			fechaDesde=record.getValue("fe_ord_desde");			
			fechaHasta=record.getValue("fe_ord_hasta");
			tipoProductoId=record.getValue("tipo_producto_id");
			
			registrarInicio(obtenerNombreArchivo("SolicitudesDicom"));								
			crearCabeceraOrdenesDicom();
			escribir("\r\n");			
			solicitudesDicom=ordenDAO.listarSolicitudesDicom(idUnidad,idBlotter, status, fechaDesde, fechaHasta, tipoProductoId);
//			Orden;Unidad de Inversión;Nro Jornada;Cliente;Cedula o Rif;Cuenta Cliente;Monto Solicitado;ContraValor;Monto Comision;Fecha Solicitud;Fecha Valor
			while (solicitudesDicom.next()) {									
								
				//Buscar Campos dinámicos de la orden
				
				registroProcesado++;
				//colocar datos en cada columna de la fila
				escribir(solicitudesDicom.getValue("ORDENE_ID")+";");
				escribir(solicitudesDicom.getValue("undinv_nombre")+";");
				escribir(solicitudesDicom.getValue("nro_jornada")+";");
				escribir(solicitudesDicom.getValue("client_nombre")+";");
				escribir(solicitudesDicom.getValue("client_cedrif")+";");
				escribir(solicitudesDicom.getValue("ctecta_numero")+";");
				escribir(solicitudesDicom.getValue("ordene_ped_monto")+";");
				escribir(solicitudesDicom.getValue("ORDENE_TASA_CAMBIO")+";");
				escribir(solicitudesDicom.getValue("contravalor")+";");				
				escribir(solicitudesDicom.getValue("ordene_ped_comisiones")+";");
				escribir(solicitudesDicom.getValue("PORC_COMISION_IGTF")+";");
				escribir(solicitudesDicom.getValue("MONTO_COMISION_IGTF")+";");
				escribir(solicitudesDicom.getValue("ordene_ped_fe_orden")+";");
				escribir(solicitudesDicom.getValue("ordene_ped_fe_valor")+";");
				escribir("\r\n");
			}	
	}
			
	public void reporteGenerico() throws Exception {

		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		CamposDinamicos camposDinamicos= new CamposDinamicos(_dso);
		TitulosDAO titulosDao = new TitulosDAO(_dso);
		SimpleDateFormat formato2 = new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);		
		
		// Recuperamos el dataset con la informacion para exportarla a excel
		Transaccion transaccion = new Transaccion(this._dso);
		Statement statement = null;
		PreparedStatement pstatement2 = null;
		PreparedStatement pstatement3 = null;
		ResultSet ordenes = null;		

		// Se realiza la consulta con los valores almacenados del record
		//DataSet record = (DataSet) _req.getSession().getAttribute("datos_ordenes_exportar-browse.framework.page.record");
		nombreMonedaBs = (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA);
		
		//REQUERIMIENTO EXPORTACION DE ORDENES POR CLIENTE
		//NM32454	
		try {
			record.getValue("ordenes_por_cliente");
			ordenesCliente = true;
		} catch (Exception e) {
			ordenesCliente = false;
		}
		
		try {			
			if (record.count() > 0) {
				if(ordenesCliente){
					ClienteDAO clienteDAO = new ClienteDAO(_dso);
					DataSet clienteDataSet = new DataSet();
					
					OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
					long idCliente; 
					String nombreCliente = null;
					String fecha_desde = null;
					String fecha_hasta = null;
					String cedulaRif="";
					String tipoPersona="";
					
					if(record.getValue("pick_cliente")!=null){
						if (record.getValue("pick_cliente")!=null)
							nombreCliente = record.getValue("pick_cliente");
						    idCliente = Long.valueOf(record.getValue("client_id")==null?"0":record.getValue("client_id"));
						if (record.getValue("fe_ord_desde")!=null)
							fecha_desde= record.getValue("fe_ord_desde");
						if (record.getValue("fe_ord_hasta")!=null)
							fecha_hasta= record.getValue("fe_ord_hasta");
						
						//SE BUSCA EL RIF DEL CLIENTE Y EL TIPO DE PERSONA
						clienteDAO.listarNombreCliente(idCliente);
						clienteDataSet = clienteDAO.getDataSet();

						if(clienteDataSet != null && clienteDataSet.count() > 0){
							cedulaRif   = clienteDataSet.getValue("CLIENT_CEDRIF");
							tipoPersona = clienteDataSet.getValue("TIPPER_ID");
						}

						//Realizar consulta
						confiD.listar(idCliente,fecha_desde, fecha_hasta,record.getValue("transaccion"));
						//registrar los datasets exportados por este modelo
						storeDataSet("table", confiD.getDataSet());
						_config.template="table2.htm";
					}else{
						if (record.getValue("pick_cliente")!=null)
							nombreCliente = record.getValue("pick_cliente");
					    idCliente = Long.valueOf(record.getValue("client_id")==null?"0":record.getValue("client_id"));			
						if (record.getValue("fe_ord_desde")!=null)
							fecha_desde= record.getValue("fe_ord_desde");
						if (record.getValue("fe_ord_hasta")!=null)
							fecha_hasta= record.getValue("fe_ord_hasta");
				
						//Realizar consulta
						confiD.listar(idCliente, fecha_desde, fecha_hasta,record.getValue("transaccion"));
						//registrar los datasets exportados por este modelo
						storeDataSet("table", confiD.getDataSet());
					}
					
					if(confiD.getDataSet().count() > 0){
						String ordeneId;
						String fecha;
						String transaccionDescripcion;
						String unidadInversion;
						String estatus;
						String montoPedido;
						String montoAdjudicado;
						
						registrarInicio(obtenerNombreArchivo("ordenesporcliente"));
						escribir("DATOS DEL CLIENTE: "+nombreCliente+". "+tipoPersona+cedulaRif);
						escribir("\r\n");
						escribir("\r\n");
						crearCabeceraOrdenesCliente();
						escribir("\r\n");
						
						while (confiD.getDataSet().next()) {
							ordeneId = confiD.getDataSet().getValue("ORDENE_ID");
							fecha = confiD.getDataSet().getValue("ORDENE_PED_FE_ORDEN");
							transaccionDescripcion = confiD.getDataSet().getValue("TRANSA_DESCRIPCION") == null ? "" : confiD.getDataSet().getValue("TRANSA_DESCRIPCION");
							unidadInversion        = confiD.getDataSet().getValue("UNDINV_NOMBRE")      == null ? "" : confiD.getDataSet().getValue("UNDINV_NOMBRE");
							estatus                = confiD.getDataSet().getValue("ORDSTA_NOMBRE")      == null ? "" : confiD.getDataSet().getValue("ORDSTA_NOMBRE");
							montoPedido            = confiD.getDataSet().getValue("ORDENE_PED_MONTO")   == null ? "" : confiD.getDataSet().getValue("ORDENE_PED_MONTO");
							montoAdjudicado        = confiD.getDataSet().getValue("ORDENE_ADJ_MONTO")   == null ? "" : confiD.getDataSet().getValue("ORDENE_ADJ_MONTO");
							
							escribir(ordeneId+";");
							escribir(fecha+";");
							escribir(transaccionDescripcion.replace(";", "") +";");
							escribir(unidadInversion+";");
							escribir(estatus.replace(";", "")+";");
							escribir(montoPedido+";");
							escribir(montoAdjudicado+";");
							escribir("\r\n");
						}
					}
				}else {
					
					registrarInicio(obtenerNombreArchivo("ordenes"));
					transaccion.begin();
					statement = transaccion.getConnection().createStatement();
					System.out.println("titulosDao.listarOrdenTitulosSql() --> " + titulosDao.listarOrdenTitulosSql());
					pstatement2 = transaccion.getConnection().prepareStatement(titulosDao.listarOrdenTitulosSql());
					System.out.println("camposDinamicos.listarCamposDinamicosOrdenes() --> " + camposDinamicos.listarCamposDinamicosOrdenes());
					pstatement3 = transaccion.getConnection().prepareStatement(camposDinamicos.listarCamposDinamicosOrdenes());				
					System.out.println("ordenDAO.listarMovimientosPorUbs --> " + ordenDAO.listarMovimientosPorUbs(Long.parseLong(record.getValue("unidad_inversion")), record.getValue("blotter"), record.getValue("status"), record.getValue("fe_ord_desde"), record.getValue("fe_ord_hasta"), false, record.getValue("tipo_producto_id")));
					ordenes = statement.executeQuery(ordenDAO.listarMovimientosPorUbs(Long.parseLong(record.getValue("unidad_inversion")), record.getValue("blotter"), record.getValue("status"), record.getValue("fe_ord_desde"), record.getValue("fe_ord_hasta"), false, record.getValue("tipo_producto_id")));
					
					//Si tipo de producto es Subasta de Divisas
					if(record.getValue("tipo_producto_id")!=null && record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)){
											
						crearCabeceraSubastaDivisas();
						while (ordenes.next()) {									
											
							//Buscar Campos dinámicos de la orden
							pstatement3.setLong(1,ordenes.getLong("ordene_id"));					
							ordenesCamposDinamicos = pstatement3.executeQuery();					
							camposDinamicos();
							
							registroProcesado++;
							//colocar datos en cada columna de la fila
							escribir(ordenes.getString("ordene_id")+";");
							escribir(ordenes.getString("unidad")+";");
							escribir(ordenes.getString("client_nombre")+";");
							escribir(ordenes.getString("client_cedrif")+";");
							if (ordenes.getString("ctecta_numero") != null){
								escribir("'"+ordenes.getString("ctecta_numero"));	
							}	
							escribir(";");
							escribir(ordenes.getString("ordsta_nombre").replaceAll("&Oacute;", "Ó")+";");
							escribir(ordenes.getString("empres_nombre")+";");
							escribir(ordenes.getString("transa_id")+";");
							escribir(ordenes.getString("ordene_cte_seg_bco"));
							escribir(";");
							escribir(ordenes.getString("ordene_cte_seg_infi"));
							escribir(";");
							
							escribir(ordenes.getString("bloter_descripcion")+";");						
							escribir(ordenes.getString("cartera_propia")+";");
							escribir(ordenes.getString("ordene_usr_nombre")+";");
							escribir(ordenes.getString("ordene_usr_sucursal")+";");
							escribir(ordenes.getString("ordene_usr_terminal")+";");	
							escribir(ordenes.getString("tomador")+";");
							escribir(ordenes.getString("colocador")+";");
							escribir(ordenes.getString("recompra_vec")+";");
							escribir(ordenes.getString("ordene_ped_fe_orden")+";");
							if (ordenes.getString("ordene_ped_fe_valor") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_ped_fe_valor")), "dd/MM/yyyy")+";");
							}
		
							if (ordenes.getString("ordene_fecha_adjudicacion") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_fecha_adjudicacion")), "dd/MM/yyyy")+";");
							}
							if (ordenes.getString("ordene_fecha_liquidacion") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_fecha_liquidacion")), "dd/MM/yyyy")+";");
							}
							if (ordenes.getString("ordene_fecha_custodia") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_fecha_custodia")), "dd/MM/yyyy")+";");
							}
							//Montos 
							escribir(ordenes.getDouble("ordene_ped_monto"));
							escribir(";");
							escribir((ordenes.getDouble("ordene_ped_monto") * (ordenes.getDouble("ordene_ped_precio")/100) * ordenes.getDouble("undinv_tasa_cambio")));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_int_caidos"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_comision_emisor"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ganancia_red"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_comision_oficina"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_total"));
							escribir(";");
							escribir(ordenes.getString("financiado")+";");
							escribir(ordenes.getDouble("ordene_ped_total_pend"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_adj_monto"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_comisiones"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_total"));
							escribir(";");
							escribir(ordenes.getDouble("undinv_tasa_cambio"));//Tasa unidad inversión
							escribir(";");
							escribir(ordenes.getDouble("ordene_tasa_pool"));//Tasa Propuesta (TASA POOL)
							escribir(";");
							escribir(ordenes.getDouble("ordene_tasa_cambio"));//Tasa Adjudicada 
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_precio"));
							escribir(";");

												
							escribir(sb.toString()); //Recompras
							escribir(sbcd.toString()); //Campos dinámicos
							escribir("\r\n");
		
						} // fin while
					
					}else{ //Si tipo de producto es Subasta o cualquier otro
						
						if(record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL) ||
								record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)||
									record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
							//NM29643 - INFI_SICAS_II: Se muestra la tasa propuesta para subasta Divisas Personal
							//CT19940 - INFI_SICAS_II: Y ahora Se muestra tambien la tasa propuesta para subasta Divisas Red Comercial
							crearCabeceraSubastaDivisasPersonal();
						}else{
							crearCabecera();
						}
						
						while (ordenes.next()) {					
							pstatement2.setLong(1,ordenes.getLong("ordene_id"));
							ordenesTitulos = pstatement2.executeQuery();
							pstatement3.setLong(1,ordenes.getLong("ordene_id"));					
							ordenesCamposDinamicos = pstatement3.executeQuery();
							recompras();
							camposDinamicos();
							registroProcesado++;
							escribir(ordenes.getString("ordene_id")+";");
							escribir(ordenes.getString("unidad")+";");
							escribir(ordenes.getString("client_nombre")+";");
							escribir(ordenes.getString("client_cedrif")+";");
							if (ordenes.getString("ctecta_numero") != null){
								escribir("'"+ordenes.getString("ctecta_numero"));	
							}	
							escribir(";");
							escribir(ordenes.getString("ordsta_nombre").replaceAll("&Oacute;", "Ó")+";");
							escribir(ordenes.getString("empres_nombre")+";");
							escribir(ordenes.getString("transa_id")+";");
							escribir(ordenes.getString("ordene_cte_seg_bco"));
							escribir(";");
							escribir(ordenes.getString("ordene_cte_seg_infi"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_monto"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_precio"));
							escribir(";");
							if(record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||
									record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)||
										record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
								//NM29643 - INFI_SICAS_II: Se muestra la tasa propuesta para subasta Divisas Personal
								//CT19940 - INFI_SICAS_II: Y ahora Se muestra tambien la tasa propuesta para subasta Divisas Red Comercial
								escribir(ordenes.getDouble("ordene_tasa_pool"));
								escribir(";");
								escribir(ordenes.getDouble("ordene_tasa_cambio"));
							}else{
								escribir(ordenes.getDouble("undinv_tasa_cambio"));
							}
							
							escribir(";");
							escribir((ordenes.getDouble("ordene_ped_monto") * (ordenes.getDouble("ordene_ped_precio")/100) * ordenes.getDouble("undinv_tasa_cambio")));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_int_caidos"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_comision_emisor"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ganancia_red"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_comision_oficina"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_total"));
							escribir(";");
							escribir(ordenes.getString("financiado")+";");
							escribir(ordenes.getDouble("ordene_ped_total_pend"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_adj_monto"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_comisiones"));
							escribir(";");
							escribir(ordenes.getDouble("ordene_ped_total"));
							escribir(";");
							escribir(ordenes.getString("bloter_descripcion")+";");
		
							escribir(ordenes.getString("cartera_propia")+";");
							escribir(ordenes.getString("ordene_usr_nombre")+";");
							escribir(ordenes.getString("ordene_usr_sucursal")+";");
							escribir(ordenes.getString("ordene_usr_terminal")+";");
		
							escribir(ordenes.getString("tomador")+";");
							escribir(ordenes.getString("colocador")+";");
							escribir(ordenes.getString("recompra_vec")+";");
							escribir(ordenes.getString("ordene_ped_fe_orden")+";");
							if (ordenes.getString("ordene_ped_fe_valor") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_ped_fe_valor")), "dd/MM/yyyy")+";");
							}
		
							if (ordenes.getString("ordene_fecha_adjudicacion") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_fecha_adjudicacion")), "dd/MM/yyyy")+";");
							}
							if (ordenes.getString("ordene_fecha_liquidacion") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_fecha_liquidacion")), "dd/MM/yyyy")+";");
							}
							if (ordenes.getString("ordene_fecha_custodia") == null) {
								escribir(" ;");
							} else {
								escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("ordene_fecha_custodia")), "dd/MM/yyyy")+";");
							}
							
							escribir(sb.toString()); //Recompras
							escribir(sbcd.toString()); //Campos dinámicos
							escribir("\r\n");
		
						} // fin while

					}
				}
				registrarFin();
				obtenerSalida();
			}
		} catch (Exception e) {
			Logger.error(this,"Error en la exportación del Excel",e);
		} finally{
			if (ordenes != null){
				ordenes.close();
			}
			if (ordenesTitulos != null){
				ordenesTitulos.close();
			}
			if (statement != null){
				statement.close();
			}
			if (pstatement2 != null){
				pstatement2.close();
			}
			if (pstatement3 != null){
				pstatement3.close();
			}
			if (transaccion != null){
				transaccion.end();
				transaccion.closeConnection();
			}
		}

		// Se limpian los datos de sesion
		_req.getSession().removeAttribute("exportar_excel");
	}// fin execute
	
	protected void crearCabeceraOrdenesCliente() throws Exception {
		escribir("Orden;Fecha;Transacción Negocio;Unidad de Inversión; Status; Monto Solicitado en Dolares; Monto Adjudicado en Dolares;".toUpperCase());
	}
	
	protected void crearCabecera() throws Exception {
		escribir("Orden;Unidad de Inversión;Cliente;Cédula o Rif;Cuenta Cliente;Estatus de la Orden;Empresa;Transacción;Segmento Banco;Segmento Infi; Monto Pedido; Precio Compra; Tasa de Cambio; Importe de la Orden; Intereses Caídos; Comisión Emisor; Ganancia Red; Comisión Oficina; Total Orden "+nombreMonedaBs+"; Financiada; Monto Financiado; Monto Adjudicado; Comisión Operación; Monto Total; Blotter; Cartera Propia; Usuario; Sucursal; Terminal; Vehículo Tomador; Vehículo Colocador; Vehículo Recompra; Fecha Orden; Fecha Valor; Fecha Adjudicación; Fecha Liquidación; Fecha Custodia;".toUpperCase());
	}
	
	protected void crearCabeceraSubastaDivisas() throws Exception {
		//escribir("Orden;Unidad de Inversión;Cliente;Cédula o Rif;Cuenta Cliente;Estatus de la Orden;Empresa;Transacción;Segmento Banco;Segmento Infi; Monto Pedido; Precio Compra; Tasa Unidas Inversión; Importe de la Orden; Intereses Caídos; Comisión Emisor; Ganancia Red; Comisión Oficina; Total Orden "+nombreMonedaBs+"; Financiada; Monto Financiado; Monto Adjudicado; Comisión Operación; Monto Total; Blotter; Cartera Propia; Usuario; Sucursal; Terminal; Vehículo Tomador; Vehículo Colocador; Vehículo Recompra; Fecha Orden; Fecha Valor; Fecha Adjudicación; Fecha Liquidación; Fecha Custodia; Tasa Adjudicada; Tasa Propuesta;".toUpperCase());
		escribir("Orden;Unidad de Inversión;Cliente;Cédula o Rif;Cuenta Cliente;Estatus de la Orden;Empresa;Transacción;Segmento Banco;Segmento Infi; Blotter; Cartera Propia; Usuario; Sucursal; Terminal; Vehículo Tomador; Vehículo Colocador; Vehículo Recompra; Fecha Orden; Fecha Valor; Fecha Adjudicación; Fecha Liquidación; Fecha Custodia; Monto Pedido; Importe de la Orden; Intereses Caídos; Comisión Emisor; Ganancia Red; Comisión Oficina; Total Orden "+nombreMonedaBs+"; Financiada; Monto Financiado; Monto Adjudicado; Comisión Operación; Monto Total; Tasa Unidad Inversión; Tasa Propuesta; Tasa Adjudicada; Precio Compra;".toUpperCase());
	}
	
	protected void crearCabeceraSubastaDivisasPersonal() throws Exception {
		escribir("Orden;Unidad de Inversión;Cliente;Cédula o Rif;Cuenta Cliente;Estatus de la Orden;Empresa;Transacción;Segmento Banco;Segmento Infi; Monto Pedido; Precio Compra; Tasa Propuesta; Tasa Adjudicada; Importe de la Orden; Intereses Caídos; Comisión Emisor; Ganancia Red; Comisión Oficina; Total Orden "+nombreMonedaBs+"; Financiada; Monto Financiado; Monto Adjudicado; Comisión Operación; Monto Total; Blotter; Cartera Propia; Usuario; Sucursal; Terminal; Vehículo Tomador; Vehículo Colocador; Vehículo Recompra; Fecha Orden; Fecha Valor; Fecha Adjudicación; Fecha Liquidación; Fecha Custodia;".toUpperCase());
	}

	protected void crearCabeceraOrdenesDicom() throws Exception {		
		escribir("Orden;Unidad de Inversión;Nro Jornada;Cliente;Cedula o Rif;Cuenta Cliente;Monto Solicitado;Tasa Cambio;ContraValor;Monto Comision;Porcentaje Comision IGTF;Monto Comision IGTF;Fecha Solicitud;Fecha Valor".toUpperCase());
	}
	
	/**
	 * Imprime la cabecera de los títulos de recompra
	 * @throws Exception en caso de error
	 */
	protected void recompras() throws Exception {
		sb.setLength(0);
		while(ordenesTitulos.next()){
			if (cabeceraRecompras==false){
				cabeceraRecompras=true;
				escribir("RECOMPRA DE "+ordenesTitulos.getString("titulo_id").toUpperCase());	
				escribir(";");
				escribir("PRECIO DE "+ordenesTitulos.getString("titulo_id").toUpperCase());
				escribir(";");
				escribir("POOL DE RECOMPRA DE "+ordenesTitulos.getString("titulo_id").toUpperCase());
				escribir(";");
				escribir("MONTO DE RECOMPRA "+ordenesTitulos.getString("titulo_id").toUpperCase());
				escribir(";");
			}			
				
			sb.append(this.getNumero(ordenesTitulos.getDouble("TITULO_UNIDADES"))).append(";");
			if(ordenesTitulos.getString("TITULO_PCT_RECOMPRA")!=null){
				sb.append(this.getNumero(ordenesTitulos.getDouble("TITULO_PCT_RECOMPRA")));
			}else{
				sb.append("0");
			}
			sb.append(";");
			if(ordenesTitulos.getString("ORDENE_TASA_POOL")!=null){
				sb.append(this.getNumero(ordenesTitulos.getDouble("ORDENE_TASA_POOL")));
			}else{
				sb.append("0");
			}
			sb.append(";");
			if(ordenesTitulos.getString("TITULO_PCT_RECOMPRA")!=null){
				sb.append(this.getNumero(ordenesTitulos.getDouble("TITULO_PCT_RECOMPRA")*ordenesTitulos.getLong("titulo_unidades")/100));
			}else{
				sb.append("0");
			}
			sb.append(";");
		}	
	}
	
	/**
	 * Imprime la cabecera de los campos dinámicos y su valor
	 * @throws Exception en caso de error
	 */
	protected void camposDinamicos() throws Exception {
		sbcd.setLength(0);
		while(ordenesCamposDinamicos.next()){
			if (cabeceraCamposDinamicos==false){
				escribir(ordenesCamposDinamicos.getString("campo_nombre").toUpperCase());
				escribir(";");
			}				
			sbcd.append(ordenesCamposDinamicos.getString("campo_valor")).append(";");
		}
		if (cabeceraCamposDinamicos==false){
			cabeceraCamposDinamicos=true;
			escribir("\r\n");
		}				
	}	
}// Fin Clase