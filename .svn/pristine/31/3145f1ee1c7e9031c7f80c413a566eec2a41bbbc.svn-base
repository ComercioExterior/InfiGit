package models.custodia.consultas.movimientos;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de ejecutar la consulta de Movimientos (transacciones) registrados para los Clientes.
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Table extends ExportableOutputStream {

	/**Cache usada para almacenar las monedas de negociación de los títulos*/
	HashMap<String, String> cacheMonedaTitulos = new HashMap<String, String>();
	HashMap<String, String> cacheMonedaTitulos1 = new HashMap<String, String>();
	//HashMap<String, String> cacheMonedaTitulos1 = new HashMap<String, String>();
	CustodiaDAO custodiaDao = null;	
	TitulosDAO titulosDao = null;
	long idCliente = 0;
	String idTitulo = "";
	String idTransaccion = "";
	String idTipoProducto = "";
	String status="";
	SimpleDateFormat formato2 = new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		long inicio = System.currentTimeMillis();
		MSCModelExtend me = new MSCModelExtend();
		DataSet dsOrdenes = new DataSet();
		DataSet dsDatos = new DataSet();
		ResultSet rs = null;
		//OrdenDAO ordenDAO = new OrdenDAO(_dso);
		Transaccion transaccion = new Transaccion(_dso);
		transaccion.begin();
		custodiaDao  = new CustodiaDAO(transaccion);
		titulosDao = new TitulosDAO(_dso);

		// Conversion de fechas
		Date fechaTransacDesde = me.StringToDate(_record.getValue("fe_transac_desde"), ConstantesGenerales.FORMATO_FECHA);
		Date fechaTransacHasta = me.StringToDate(_record.getValue("fe_transac_hasta"), ConstantesGenerales.FORMATO_FECHA);

		if (_record.getValue("client_id") != null){
			idCliente = Long.parseLong(_record.getValue("client_id"));
		}
		
		if (_record.getValue("status_operacion") != null){
			status = _record.getValue("status_operacion");		
		}
		
		
		idTitulo = _record.getValue("titulo_id");
		idTransaccion = _record.getValue("transa_id");
		idTipoProducto = _record.getValue("tipo_producto_id");

		// Realizar consulta		
		
		try {
			rs = custodiaDao.listarMovimientosCustodia(idCliente, idTitulo, idTransaccion, fechaTransacDesde, fechaTransacHasta, idTipoProducto,status);			
			if (_record.getValue("mostrar").equals("1")){
				exportarAExcel(rs);
			}else{
				 
				dsOrdenes.append("client_nombre", java.sql.Types.VARCHAR);
				dsOrdenes.append("transa_id", java.sql.Types.VARCHAR);
				dsOrdenes.append("tipo_producto_id", java.sql.Types.VARCHAR);
				dsOrdenes.append("fecha", java.sql.Types.VARCHAR);
				dsOrdenes.append("ordene_id", java.sql.Types.VARCHAR);
				dsOrdenes.append("titulo_id", java.sql.Types.VARCHAR);
				dsOrdenes.append("titulo_unidades", java.sql.Types.VARCHAR);
				
				dsDatos.append("t_registros", java.sql.Types.VARCHAR);
				
				while(rs.next()){
					dsOrdenes.addNew();
					dsOrdenes.setValue("client_nombre", rs.getString("client_nombre"));
					dsOrdenes.setValue("transa_id", rs.getString("transa_id"));
					dsOrdenes.setValue("tipo_producto_id", rs.getString("tipo_producto_id"));
					dsOrdenes.setValue("fecha", rs.getString("fecha"));
					dsOrdenes.setValue("titulo_id", rs.getString("titulo_id"));
					dsOrdenes.setValue("ordene_id", rs.getString("ordene_id"));
					dsOrdenes.setValue("titulo_unidades", Utilitario.formatearNumero(rs.getString("titulo_unidades"), "###,###.00"));
					registroProcesado++;
				}
				
				dsDatos.addNew();
				dsDatos.setValue("t_registros", String.valueOf(registroProcesado));
				
				storeDataSet("table", dsOrdenes);
				storeDataSet("datos", dsDatos);
			}
		} catch (Exception e) {
			Logger.error(this, e.getMessage(),e);			
			throw new Exception("Problemas con la generación del reporte");			
		}finally{
			transaccion.closeConnection();
			transaccion.end();
			if (custodiaDao != null){
				custodiaDao.closeResources();
				custodiaDao.cerrarConexion();
			}
		}
		long fin = System.currentTimeMillis();
		Logger.info(this,"Inicio " + inicio);		
		Logger.info(this,"Fin " + fin);
		Logger.info(this,((fin-inicio) / 1000) + " secs.");
		
	}
		
	
	/**
	 * Exporta las ordenes a Excel
	 * @throws Exception en caso de error
	 */
	private void exportarAExcel(ResultSet ds) throws Exception{		
		MSCModelExtend me = new MSCModelExtend();
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		Date fechaTransacDesde = me.StringToDate(_record.getValue("fe_transac_desde"), ConstantesGenerales.FORMATO_FECHA);
		Date fechaTransacHasta = me.StringToDate(_record.getValue("fe_transac_hasta"), ConstantesGenerales.FORMATO_FECHA);
//		String fecha_desde = formatter.format(fechaTransacDesde);
//		String fecha_hasta = formatter.format(fechaTransacHasta);
////		System.out.println("fecha_desde--->"+fecha_desde);
//		System.out.println("fecha_hasta--->"+fecha_hasta);
//		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
//		String fecha_funcion = formatter.format(fechaTransacHasta);
//		int fecha_reconversion= Integer.parseInt(fecha_re);
//		int fecha_sistema= Integer.parseInt(fecha_funcion);
		
		registrarInicio(obtenerNombreArchivo("movimientosCustodia"));
		escribirCabecera();
		while(ds.next()){
			escribir(ds.getString("client_nombre"));
			escribir(";");
			escribir(Utilitario.DateToString(formato2.parse(ds.getString("fecha")), "dd/MM/yyyy"));
			escribir(";");
			if (ds.getString("ordene_fecha_liquidacion") != null){
				escribir(Utilitario.DateToString(formato2.parse(ds.getString("ordene_fecha_liquidacion")), "dd/MM/yyyy"));	
			}else{
				escribir("");
			}			
			escribir(";");
			escribir(ds.getString("ordene_id"));
			escribir(";");
			escribir(ds.getString("cedularif"));
			escribir(";");
			escribir(ds.getString("ordsta_id"));
			escribir(";");
			escribir(ds.getString("transa_id"));
			escribir(";");
			escribir(ds.getString("tipo_producto_id"));
			escribir(";");
			escribir(ds.getString("titulo_id"));
			escribir(";");
			escribir(ds.getLong("titulo_unidades"));
			escribir(";");
			escribir(obtenerMonedaDelTitulo(ds.getString("titulo_id")));
			escribir(";");
			escribir(ds.getDouble("bloqueo_por_pago"));
			escribir(";");
			escribir(ds.getDouble("bloqueo_por_recompra"));
			escribir(";");
			escribir(ds.getDouble("posicionmenosuno"));
			escribir(";");
			escribir(ds.getDouble("posicion"));
			escribir(";");		
			escribir(ds.getDouble("monto_intereses"));		
			escribir(";");
			escribir("\r\n");
		}
		registrarFin();
		obtenerSalida();
	}	
	
	/**
	 * Obtiene la moneda del título
	 * @param idTitulo id del título
	 * @return id de la moneda
	 */
	
	private String obtenerMonedaDelTitulo(String idTitulo) throws Exception{
		String moneda = "";
		if (cacheMonedaTitulos.containsKey(idTitulo)){
			moneda = cacheMonedaTitulos.get(idTitulo);
		}else{
			titulosDao.obtenerMonedaNegociacion(idTitulo);
			DataSet ds = titulosDao.getDataSet();
			if (ds.count()>0){
				ds.next();
				cacheMonedaTitulos.put(idTitulo, ds.getValue("titulo_moneda_den"));
				moneda = ds.getValue("titulo_moneda_den");
			}
		}
		return moneda;
	}
	/*private String obtenerMonedaDelTitulo1(String idTitulo, String Fecha_inicio, String Fecha_fin) throws Exception{
		String moneda = "";
		if (cacheMonedaTitulos1.containsKey(idTitulo)){
			moneda = cacheMonedaTitulos1.get(idTitulo);
		}else{
			titulosDao.obtenerMonedaNegociacion1(idTitulo,Fecha_inicio,Fecha_fin);
			DataSet ds = titulosDao.getDataSet();
			if (ds.count()>0){
				ds.next();
				cacheMonedaTitulos1.put(idTitulo, ds.getValue("titulo_moneda_den"));
				moneda = ds.getValue("titulo_moneda_den");
			}
		}
		return moneda;
	}*/
	
	
	protected void escribirCabecera() throws Exception{
		escribir("Cliente;Fecha de Orden;Fecha de Liquidación;Orden;Cedula o RIF;Status de la Orden;Transacción;Tipo De Producto;Título;Nominal;Moneda;Nominal Bloqueado por Pago;Nominal Bloqueado por Recompra;Posición Global a Fecha - 1;Posición Global a Fecha;Monto Interes a Pagar;\r\n".toUpperCase());
	}
}