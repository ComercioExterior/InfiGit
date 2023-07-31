package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.data.ComisionMensualTitulo;
import com.bdv.infi.data.Custodia;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoBloqueos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_remediacion.dao.remediacionmonto;

/**
 * Clase usada para la modificaci&oacute;n, inserci&oacute;n y listado de los t&iacute;tulos en custodia
 */
public class CustodiaDAO extends com.bdv.infi.dao.GenericoDAO {

	public CustodiaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	public CustodiaDAO(DataSource ds) {
		super(ds);
	}

	/**
	 * Metodo que lista solo los clientes que poseen titulos en custodia
	 * 
	 * @param fechaInicio
	 *            fecha inicial de busqueda
	 * @param fechaFin
	 *            fecha final de busqueda
	 * @throws Exception
	 */
	public void listarClientesConTitulosCustodia(String idCliente) throws Exception {
		// Se debe usar la clase CustodiaDetalle e incorporarle CustodiaBloqueo
		StringBuffer sql = new StringBuffer();

		sql.append("select distinct client_id from infi_tb_701_titulos");

		if (idCliente != null && !idCliente.equals("")) {
			sql.append(" where client_id=").append(idCliente);
		}

		this.dataSet = db.get(this.dataSource, sql.toString());

	}

	/**
	 * Busca los t&iacute;tulos que cumplan con la condici&oacute;n solicitada.
	 * 
	 * @param idCliente
	 *            id del cliente que se desea buscar
	 * @param idTitulo
	 *            id del t&iacute;tulo en custodia
	 * @param idMoneda
	 *            id de la moneda
	 * @param emisionDesde
	 *            fecha de emision desde del t&iacute;tulo
	 * @param emisionHasta
	 *            fecha de emision hasta del t&iacute;tulo
	 * @param vencimientoDesde
	 *            fecha de vencimiento desde del t&iacute;tulo
	 * @param vencimientoHasta
	 *            fecha de vencimiento hasta del t&iacute;tulo *
	 * @throws Exception
	 * */
	public void listarDetalles(long idCliente, String idTitulo, String idMoneda, Date emisionDesde, Date emisionHasta, Date vencimientoDesde, Date vencimientoHasta) throws Exception {
		// Se debe usar la clase CustodiaDetalle e incorporarle CustodiaBloqueo
		StringBuilder sql = new StringBuilder();
		StringBuilder filtro = new StringBuilder("");
		
		Date fecha1 = emisionHasta;
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		
		sql.append(" select t2.titulo_fe_ult_cobro_comision,t2.titulo_fe_ult_pago_cupon,t2.titulo_id,t2.tipo_producto_id,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE ( t1.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(t2.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),t2.titcus_cantidad) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then t2.titcus_cantidad end TITCUS_CANTIDAD,cl.client_nombre, t1.vdate titulo_fe_emision, t1.mdate titulo_fe_vencimiento, " +
				" case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(t1.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t1.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),t1.ccy)  when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(t1.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t1.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),t1.ccy) end moneda_descripcion FROM secm t1, " +
				" INFI_TB_701_TITULOS t2, INFI_TB_201_CTES cl WHERE trim(t2.titulo_id) = trim(t1.secid) " +
				" AND t2.client_id = cl.client_id ");
		
		if (idCliente != 0)
			filtro.append(" AND t2.client_id = ").append(idCliente);

		if (idTitulo != null)
			filtro.append(" AND t2.titulo_id = '").append(idTitulo).append("'");

		if (idMoneda != null)
			filtro.append(" AND t1.ccy = '").append(idMoneda).append("'");

		if (emisionDesde != null)
			filtro.append(" AND trunc(t1.vdate)  >= ").append(formatearFechaBD(emisionDesde));

		if (emisionHasta != null)
			filtro.append(" AND trunc(t1.vdate) <= ").append(formatearFechaBD(emisionHasta));

		if (vencimientoDesde != null)
			filtro.append(" AND trunc(t1.mdate) >=").append(formatearFechaBD(vencimientoDesde));

		if (vencimientoHasta != null)
			filtro.append(" AND trunc(t1.mdate) <= ").append(formatearFechaBD(vencimientoHasta));

		sql.append(filtro);
		sql.append(" ORDER BY cl.client_nombre, t1.secid ");
		System.out.println("listarDetalles "+sql);

		dataSet = db.get(dataSource, sql.toString());

	}

	/**
	 * Busca los t&iacute;tulos en custodia que cumplan con la condici&oacute;n solicitada. La consulta es almacenada en un DataSet para ser desplegado en pantalla
	 * 
	 * @param idCliente
	 *            id del cliente
	 * @throws Exception
	 *             en caso de error
	 */
	public void listarTitulos(long idCliente) throws Exception {
		listarTitulosDeCustodia(idCliente, null, null);
	}

	/**
	 * Busca los t&iacute;tulos en custodia que cumplan con la condici&oacute;n solicitada. La consulta es almacenada en un DataSet para ser desplegado en pantalla
	 * 
	 * @param idCliente
	 *            id del cliente
	 * @param idTitulo
	 *            id del título
	 * @param idTipoProducto
	 *            id del tipo de producto
	 * @throws Exception
	 *             en caso de error
	 */
	public void listarTitulos(long idCliente, String idTitulo, String idTipoProducto) throws Exception {
		listarTitulosDeCustodia(idCliente, idTitulo, idTipoProducto);
	}

	/**
	 * Busca los t&iacute;tulos en custodia que cumplan con la condici&oacute;n solicitada. La consulta es almacenada en un DataSet para ser desplegado en pantalla
	 * 
	 * @param idCliente
	 *            id del cliente
	 * @throws Exception
	 *             en caso de error
	 */
	private void listarTitulosDeCustodia(long idCliente, String idTitulo, String idTipoProducto) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.titulo_fe_ult_pago_cupon,a.titulo_id,a.titulo_id titulo_descripcion,a.titulo_fe_ingreso_custodia,a.tipo_producto_id," + 
				" (a.titcus_cantidad - decode(b.titcus_cantidad,null,0,b.titcus_cantidad)) cantidad_disponible,"	+ 
				" a.titcus_cantidad,decode(b.titcus_cantidad,null,'NO','SI') estado_bloqueo,decode(b.titcus_cantidad,null,0,b.titcus_cantidad) cantidad_bloqueada, c.titulo_moneda_den  " + 
				" from INFI_TB_701_TITULOS a,(select titulo_id,tipo_producto_id,sum(titcus_cantidad) titcus_cantidad from INFI_TB_704_TITULOS_BLOQUEO where client_id=" + idCliente + " group by titulo_id,tipo_producto_id) b, " + "infi_tb_100_titulos c, infi_tb_201_ctes d " + "WHERE a.titulo_id = b.titulo_id(+) and a.tipo_producto_id = b.tipo_producto_id(+) " + 
				" and a.titulo_id = c.titulo_id and a.client_id = d.client_id and a.titcus_cantidad > 0");

		if (idCliente > 0) {
			sql.append(" and a.client_id=" + idCliente);
		}

		if (idTitulo != null && !idTitulo.equals("")) {
			sql.append(" and a.titulo_id='" + idTitulo + "'");
		}

		if (idTipoProducto != null && !idTipoProducto.equals("")) {
			sql.append(" and a.tipo_producto_id='" + idTipoProducto + "'");
		}
		System.out.println("listarTitulosDeCustodia: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		/*StringBuffer sql = new StringBuffer();
		Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		
		//if(fecha_sistema>=fecha_reconversion){
		sql.append("select a.titulo_fe_ult_pago_cupon,a.titulo_id,a.titulo_id titulo_descripcion,a.titulo_fe_ingreso_custodia,a.tipo_producto_id," + 
				" case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(a.titulo_fe_ingreso_custodia, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0((a.titcus_cantidad - decode(b.titcus_cantidad,null,0,b.titcus_cantidad)),FVJ8ODIV0('BFE_FECHA_VIGENCIA')),(a.titcus_cantidad - decode(b.titcus_cantidad,null,0,b.titcus_cantidad))) else (a.titcus_cantidad - decode(b.titcus_cantidad,null,0,b.titcus_cantidad)) end cantidad_disponible,"	+ 
				" case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(a.titulo_fe_ingreso_custodia, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(a.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),a.titcus_cantidad) else a.titcus_cantidad end titcus_cantidad, decode(b.titcus_cantidad,null,'NO','SI') estado_bloqueo,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(a.titulo_fe_ingreso_custodia, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(decode(b.titcus_cantidad,null,0,b.titcus_cantidad),FVJ8ODIV0('BFE_FECHA_VIGENCIA')),decode(b.titcus_cantidad,null,0,b.titcus_cantidad)) else decode(b.titcus_cantidad,null,0,b.titcus_cantidad) end cantidad_bloqueada, case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),c.titulo_moneda_den)  when '" + fecha_sistema + "' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),c.titulo_moneda_den) end titulo_moneda_den  " + 
				" from INFI_TB_701_TITULOS a,(select titulo_id,tipo_producto_id,sum(titcus_cantidad) titcus_cantidad from INFI_TB_704_TITULOS_BLOQUEO where client_id=" + idCliente + " group by titulo_id,tipo_producto_id) b, " + "infi_tb_100_titulos c, infi_tb_201_ctes d " + "WHERE a.titulo_id = b.titulo_id(+) and a.tipo_producto_id = b.tipo_producto_id(+) " + 
				" and a.titulo_id = c.titulo_id and a.client_id = d.client_id and a.titcus_cantidad > 0");

		if (idCliente > 0) {
			sql.append(" and a.client_id=" + idCliente);
			//sql.append(" and c.titulo_moneda_den ='VES'");
		}

		if (idTitulo != null && !idTitulo.equals("")) {
			sql.append(" and a.titulo_id='" + idTitulo + "'");
		}

		if (idTipoProducto != null && !idTipoProducto.equals("")) {
			sql.append(" and a.tipo_producto_id='" + idTipoProducto + "'");
		}
		System.out.println("listarTitulosDeCustodia: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		*/
		
	}

	// /**
	// * Lista todos los t&iacute;tulos en custodia que posee un cliente
	// * @param idCliente
	// * @throws Exception
	// */
	// public void listarBloqueosPorClienteTitulo(long idCliente, String idTitulo) throws Exception{
	// StringBuffer sql = new StringBuffer();
	// StringBuffer filtro = new StringBuffer("");
	//		
	// sql.append("SELECT t2.*, tb.numero_garantia, decode(tb.titulo_id, null, 'NO', 'SI') as estado_bloqueo, ");
	// sql.append(" tb.titcus_cantidad as cantidad_bloqueada, ");
	// sql.append(" t2.titcus_cantidad as cantidad_custodia, ");
	// sql.append(" (t2.titcus_cantidad * t1.titulo_valor_nominal) as valor_total, ");
	// sql.append(" cl.client_nombre, t1.titulo_fe_emision, t1.titulo_fe_vencimiento, ");
	// sql.append(" t1.titulo_descripcion, t1.titulo_valor_nominal, m.moneda_descripcion, ");
	// sql.append(" t1.titulo_moneda_den, tpblo.tipblo_descripcion, benef.beneficiario_nombre ");
	// sql.append(" FROM INFI_TB_100_TITULOS t1, INFI_TB_701_TITULOS t2, ");
	// sql.append(" INFI_VI_MONEDAS m, INFI_TB_700_TIPO_BLOQUEO tpblo, INFI_TB_039_BENEFICIARIOS benef, ");
	// sql.append(" INFI_TB_201_CTES cl, INFI_TB_704_TITULOS_BLOQUEO tb ");
	// sql.append(" WHERE trim(t2.titulo_id) = trim(t1.titulo_id) ");
	// sql.append(" AND t1.titulo_moneda_den = m.moneda_id ");
	// sql.append(" AND t2.client_id = cl.client_id");
	// sql.append(" AND t2.CLIENT_ID = tb.CLIENT_ID(+) ");
	// sql.append(" and trim(t2.TITULO_ID) = trim(tb.TITULO_ID(+))");
	// sql.append(" and tb.tipblo_id = tpblo.tipblo_id ");
	// sql.append(" and tb.beneficiario_id = benef.beneficiario_id(+) ");
	//				
	// if(idCliente!=0)
	// filtro.append(" AND t2.client_id = ").append(idCliente);
	//		
	// filtro.append(" AND t2.titulo_id = '").append(idTitulo).append("'");
	//		
	// sql.append(filtro);
	// sql.append(" ORDER BY cl.client_nombre, t1.titulo_descripcion, m.moneda_descripcion");
	//					
	// dataSet = db.get(dataSource, sql.toString());
	//		
	// }

	/*public void listarPorClienteYTitulo(String idTitulo, int idCliente) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");

		sql
				.append("SELECT t2.*, decode(tb.titulo_id, null, 'NO', 'SI') as estado_bloqueo, (t2.titcus_cantidad * t1.titulo_valor_nominal) as valor_total, cl.client_nombre, t1.titulo_fe_emision, t1.titulo_fe_vencimiento, t1.titulo_descripcion, t1.titulo_valor_nominal, m.moneda_descripcion, t1.titulo_moneda_den FROM INFI_TB_100_TITULOS t1, INFI_TB_701_TITULOS t2, INFI_VI_MONEDAS m, INFI_TB_201_CTES cl, INFI_TB_704_TITULOS_BLOQUEO tb WHERE t2.titulo_id = t1.titulo_id AND t1.titulo_moneda_den = m.moneda_id AND t2.client_id = cl.client_id");
		sql.append(" AND t2.CLIENT_ID = tb.CLIENT_ID(+) and t2.TITULO_ID = tb.TITULO_ID(+)");
		sql.append(" AND t2.client_id = ").append(idCliente);
		if (idTitulo != null) {
			sql.append(" AND t2.titulo_id='").append(idTitulo).append("'");
		}
		sql.append(filtro);

		dataSet = db.get(dataSource, sql.toString());

	}*/

	// /**Busca la informaci&oacute;n de un t&iacute;tulo en custodia de un cliente determinado.
	// * @param idTitulo id del t&iacute;tulo en custodia
	// * @param idCliente id del cliente que posee el titulo en custodia
	// * @throws Exception
	// */
	// public void listarPorTitulo(String idTitulo, long idCliente, String tipoProducto) throws Exception{
	// StringBuffer sql = new StringBuffer();
	//		
	// sql.append("select tit.titulo_moneda_den, to_char(a.titulo_fe_ult_cobro_comision, '"+ConstantesGenerales.FORMATO_FECHA+"')as titulo_fe_ult_cobro_comision,to_char(a.titulo_fe_ult_pago_cupon, '"+ConstantesGenerales.FORMATO_FECHA+"')as titulo_fe_ult_pago_cupon,tit.titulo_descripcion, tit.titulos_precio_recompra, a.titulo_id,a.CLIENT_ID, cl.CLIENT_NOMBRE, a.TITCUS_CANTIDAD, to_char(a.TITULO_FE_INGRESO_CUSTODIA, '"+ConstantesGenerales.FORMATO_FECHA+"') as TITULO_FE_INGRESO_CUSTODIA , decode(sum(b.TITCUS_CANTIDAD), null, 0, sum(b.TITCUS_CANTIDAD)) as bloqueada, (a.TITCUS_CANTIDAD - (decode(sum(b.TITCUS_CANTIDAD), null, 0, sum(b.TITCUS_CANTIDAD)))) as cant_disponible, '' as campo_adicional, '' as campo_adicional2 from infi_tb_701_titulos a,  infi_tb_704_titulos_bloqueo b, infi_tb_100_titulos tit, INFI_TB_201_CTES cl");
	// sql.append(" where a.CLIENT_ID = b.CLIENT_ID(+) and trim(a.TITULO_ID) = trim(b.TITULO_ID(+))");
	// sql.append(" AND a.client_id=").append(idCliente);
	//		
	// if(idTitulo!=null)
	// sql.append(" AND trim(a.titulo_id)=trim('").append(idTitulo).append("')");
	//		
	// sql.append(" AND trim(tit.titulo_id) = trim(a.titulo_id) and cl.CLIENT_ID=a.CLIENT_ID");
	// sql.append(" group by tit.titulo_moneda_den,a.titulo_fe_ingreso_custodia,a.titulo_fe_ult_cobro_comision,a.titulo_fe_ult_pago_cupon,a.titulo_id,a.CLIENT_ID, cl.CLIENT_NOMBRE, a.TITCUS_CANTIDAD, a.TITULO_FE_INGRESO_CUSTODIA, tit.titulo_descripcion, tit.titulos_precio_recompra");
	// System.out.println("AAAAAA->> " + sql.toString());
	// dataSet = db.get(dataSource, sql.toString());
	//		
	// }

	/**
	 * Busca los t&iacute;tulos que posea un cliente donde la cantidad en custodia sea mayor a 0.
	 * 
	 * @param idCliente
	 *            id del cliente
	 * @throws lanza
	 *             una excepci&oacute;n en caso de fallar en la consulta
	 */
	public void listarTitulosPorClientes(long idCliente) throws Exception {
		String sql = " SELECT DISTINCT B.TITULO_ID, B.TIPO_PRODUCTO_ID FROM INFI_TB_701_TITULOS B WHERE	" +
				" B.CLIENT_ID=" + idCliente + " AND B.TITCUS_CANTIDAD > 0 AND B.TIPO_PRODUCTO_ID NOT IN('SICAD2RED','SICAD2PER')";
		dataSet = db.get(dataSource, sql);
	}

	/** Inserta títulos en custodia */
	public String insertar(Custodia custodia) {
		StringBuffer sql = new StringBuffer();

		sql.append("insert into INFI_TB_701_TITULOS (titulo_id,client_id,titcus_cantidad,titulo_fe_ingreso_custodia,TITULO_FE_ULT_PAGO_CUPON,TITULO_FE_ULT_AMORTIZACION,tipo_producto_id) values (");
		sql.append("'").append(custodia.getIdTitulo()).append("',");
		sql.append(custodia.getIdCliente()).append(",");
		sql.append(custodia.getCantidad()).append(",");
		sql.append("SYSDATE,");
		sql.append(custodia.getFechaUltimoCupon()==null? "NULL" : this.formatearFechaBD(custodia.getFechaUltimoCupon())).append(",");
		sql.append(custodia.getFechaUltimaAmortizacion()==null? "NULL" : this.formatearFechaBD(custodia.getFechaUltimaAmortizacion())).append(",");
		sql.append("'" + custodia.getTipoProductoId() + "')");
		System.out.println("insertar"+sql);
		return (sql.toString());
		
	}

	/**
	 * Inserta en custodia los títulos de una orden recibida
	 * 
	 * @param idOrden
	 *            id de la orden de la cual se desea colocar los títulos en custodia
	 * @param titulosEnCero
	 *            verdadero si se desea almacenar los títulos en custodia con cantidad 0. Es útil cuando la orden enviada es motivo de recompra
	 */
	/*
	 * public String insertarPorOrden(long idOrden,boolean titulosEnCero) { StringBuffer sql = new StringBuffer(); sql.append("insert into INFI_TB_701_TITULOS (titulo_id,client_id,titcus_cantidad,titulo_fe_ingreso_custodia)");
	 * 
	 * if (titulosEnCero){ sql.append("(select a.titulo_id,b.client_id,0,sysdate from infi_tb_206_ordenes_titulos a, infi_tb_204_ordenes b"); } else{ sql.append("(select a.titulo_id,b.client_id,a.titulo_monto,sysdate from infi_tb_206_ordenes_titulos a, infi_tb_204_ordenes b"); } sql.append("where a.ordene_id=b.ordene_id and a.ordene_id=").append(idOrden); return(sql.toString()); }
	 */

	/** Modifica títulos de custodia */
	public String modificar(Custodia custodia) {
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_701_TITULOS set ");
		sql.append(" titcus_cantidad=").append(custodia.getCantidad());
		sql.append(" where titulo_id='").append(custodia.getIdTitulo());
		sql.append("' and client_id=").append(custodia.getIdCliente());
		sql.append(" and tipo_producto_id='").append(custodia.getTipoProductoId()).append("'");
		return (sql.toString());
	}

	/**
	 * Modifica en custodia los títulos de una orden recibida
	 * 
	 * @param idOrden
	 *            id de la orden de la cual se desea colocar los títulos en custodia
	 * @param titulosEnCero
	 */
	/*
	 * public String modificarPorOrden(long idOrden) { StringBuffer sql = new StringBuffer(); sql.append("update infi_tb_701_titulos c set c.titcus_cantidad = c.titcus_cantidad + (select a.titulo_monto from infi_tb_206_ordenes_titulos a, infi_tb_204_ordenes b"); sql.append("where a.ordene_id=b.ordene_id and a.ordene_id=").append(idOrden).append("and c.titulo_id = a.titulo_id");
	 * sql.append("and c.client_id=b.client_id) where c.client_id = (select client_id from infi_tb_204_ordenes where ordene_id=").append(idOrden); return(sql.toString()); }
	 */

	/**
	 * Modifica la fecha de &uacute;ltimo cobro de comisi&oacute;n
	 * 
	 * @param comision
	 * @return La sentencia sql a ejecutar
	 */
	public String modificarCobroComision(ComisionMensualTitulo comision) {
		StringBuilder sql = new StringBuilder();

		sql.append("update INFI_TB_701_TITULOS set ");
		sql.append("titulo_fe_ult_cobro_comision = ");
		sql.append(formatearFechaBD(comision.getFechaCobro()));
		sql.append(" where client_id = ");
		sql.append(comision.getClienteId());
		sql.append(" and titulo_id = '");
		sql.append(comision.getTituloId() + "'");
		sql.append(" and titcus_cantidad > 0");

		return sql.toString();
	}

	public Object moveNext() throws Exception {
		boolean bolPaso = false;
		ComisionMensualTitulo cmt = new ComisionMensualTitulo();
		try {
			/* Si no es &uacute;ltimo registro arma el objeto */
			if ((resultSet != null) && (!resultSet.isAfterLast())) {
				bolPaso = true;
				cmt.setClienteId(resultSet.getInt("CLIENT_ID"));
				cmt.setTituloMonedaNeg(resultSet.getString("TITULO_MONEDA_NEG"));
				cmt.setTotalTitulos(resultSet.getInt("TOTALTITULOS"));
				cmt.setTituloId(resultSet.getString("TITULO_ID"));
				resultSet.next();
			}
		} catch (SQLException e) {
			super.closeResources();
			throw new Exception("Error al tratar de crear el objeto ComisionMensualTitulo error: " + e);
		}
		if (bolPaso) {
			return cmt;
		} else {
			super.closeResources();
			return null;
		}
	}

	/** Metodo que verifica que el cliente ya tenga ese titulo en custodia */
	public void verificarTitulo(String idCliente, String idTitulo, String idTipoProducto) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from INFI_TB_701_TITULOS where ");
		sb.append("tipo_producto_id='" + idTipoProducto + "' and titulo_id='").append(idTitulo).append("' and client_id=").append(idCliente);
		dataSet = db.get(dataSource, sb.toString());
	}

	/**
	 * Actualiza la cantidad en custodia que posee un cliente sobre un titulo y un producto
	 * 
	 * @return String con sentencia a ejecutar
	 */
	public String actualizarCantidadEnCustodia(Custodia custodia) {
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_701_TITULOS set titcus_cantidad =").append(custodia.getCantidad());
		sql.append(" where client_id =").append(custodia.getIdCliente());
		sql.append(" and titulo_id = '").append(custodia.getIdTitulo()).append("'");
		sql.append(" and tipo_producto_id = '").append(custodia.getTipoProductoId()).append("'");
		System.out.println("actualizarCantidadEnCustodia--->"+sql);
		return (sql.toString());
	}

	/**
	 * Lista los titulos por cliente
	 * 
	 * @param idCliente
	 * @return Dataset para ser storedataset
	 */
	public DataSet listarTitulosCliente(long idCliente) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql
				.append("select '' as cantidad_bloqueada,'' as cantidad_disponible,'' as display,'' as bloqueo,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_201_ctes.TIPPER_ID,infi_tb_100_titulos.TITULO_VALOR_NOMINAL,infi_tb_100_titulos.TITULO_MONEDA_DEN,infi_tb_201_ctes.CLIENT_CEDRIF,infi_tb_201_ctes.CLIENT_CTA_CUSTOD_ID,infi_tb_701_titulos.CLIENT_ID,infi_tb_701_titulos.TITCUS_CANTIDAD,(infi_tb_701_titulos.TITCUS_CANTIDAD*infi_tb_100_titulos.TITULO_VALOR_NOMINAL)as valor_total,infi_tb_701_titulos.TITULO_ID from infi_tb_701_titulos inner join infi_tb_100_titulos on trim(infi_tb_100_titulos.TITULO_ID)=trim(infi_tb_701_titulos.TITULO_ID) inner join infi_tb_201_ctes on infi_tb_701_titulos.CLIENT_ID=infi_tb_201_ctes.CLIENT_ID where 1=1 ");
		sql.append(" and  infi_tb_701_titulos.TITCUS_CANTIDAD<>0 and infi_tb_701_titulos.CLIENT_ID=").append(idCliente).append(" order by  infi_tb_100_titulos.TITULO_DESCRIPCION");
		dataSet = db.get(dataSource, sql.toString());
		return dataSet;
	}

	/**
	 * Lista la cantidad total bloqueada por cliente y titulo
	 * 
	 * @param long idCliente
	 * @param String
	 *            idTitulo
	 * @return String para ser insertado en el dataset
	 */
	public String listarCantidaBloqueada(long idCliente, String idTitulo) throws Exception {
		String total = "";
		StringBuffer sql = new StringBuffer();
		sql.append("select SUM(TITCUS_CANTIDAD)as cant_bloqueada FROM INFI_TB_704_TITULOS_BLOQUEO WHERE ");
		sql.append("trim(TITULO_ID)=trim('").append(idTitulo).append("')");
		sql.append(" AND CLIENT_ID=").append(idCliente);
		dataSet = db.get(dataSource, sql.toString());
		if (dataSet.count() > 0) {
			dataSet.first();
			dataSet.next();
			total = dataSet.getValue("cant_bloqueada");
			if (total == null) {
				total = "NO";
			}
		}
		return total;
	}

	/**
	 * Lista el valor nominal por titulo
	 * 
	 * @param String
	 *            idCliente
	 * @return el valor nominal del titulo
	 */
	public String listarValorNominal(String tituloId) throws Exception {
		// TODO borrar este método
		String nominal = "";
		StringBuffer sql = new StringBuffer();
		sql.append("select titulo_valor_nominal from infi_tb_100_titulos where ");
		sql.append("trim(TITULO_ID)=trim('").append(tituloId).append("')");
		dataSet = db.get(dataSource, sql.toString());
		if (dataSet.count() > 0) {
			dataSet.next();
			nominal = dataSet.getValue("titulo_valor_nominal");
		}
		return nominal;
	}

	/**
	 * Lista la cantidad total bloqueada por cliente y titulo DETALLES
	 * 
	 * @param long idCliente
	 * @param String
	 *            idTitulo
	 * @return String para ser insertado en el dataset
	 */
	public DataSet listarCantidaBloqueadaDetalles(long idCliente, String idTitulo) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_700_tipo_bloqueo.TIPBLO_DESCRIPCION,INFI_TB_704_TITULOS_BLOQUEO.* FROM INFI_TB_704_TITULOS_BLOQUEO left join infi_tb_700_tipo_bloqueo on INFI_TB_704_TITULOS_BLOQUEO.TIPBLO_ID=infi_tb_700_tipo_bloqueo.TIPBLO_ID WHERE trim(TITULO_ID)=");
		sql.append("trim('").append(idTitulo).append("')");
		sql.append(" AND CLIENT_ID=").append(idCliente);
		dataSet = db.get(dataSource, sql.toString());
		return dataSet;
	}

	/**
	 * @param String
	 *            idTitulo
	 */
	public void listarNombreTituloId(String idTitulo) throws Exception {
		// TODO Borrar este método que no es de utilidad
		StringBuffer sql = new StringBuffer();
		sql.append("select titulo_descripcion from infi_tb_100_titulos where ");
		sql.append(" trim(infi_tb_100_titulos.titulo_id)=trim('").append(idTitulo).append("')");
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Verifica si el cliente que recibe titulos en salida interna existe o no para la siguiente accion
	 * 
	 * @param cantidad_custodia
	 * @param idCliente
	 * @param idTitulo
	 * @return String con sentencia a ejecutar
	 */
	public String insertarUpdateCteRecibeTitulos(Custodia custodia) throws Exception {

		StringBuffer sql = new StringBuffer();
		verificarTitulo(String.valueOf(custodia.getIdCliente()), custodia.getIdTitulo(), custodia.getTipoProductoId());
		if (getDataSet().count() > 0) {
			getDataSet().next();
			long montoUpdate = Long.parseLong(getDataSet().getValue("titcus_cantidad")) + custodia.getCantidad();
			sql.append("update INFI_TB_701_TITULOS set titcus_cantidad =").append(montoUpdate);
			sql.append(" where client_id =").append(custodia.getIdCliente());
			sql.append(" and titulo_id = '").append(custodia.getIdTitulo()).append("'");
			sql.append(" and tipo_producto_id = '").append(custodia.getTipoProductoId()).append("'");
		} else {
			System.out.println("paso por insertar");
			sql.append(insertar(custodia));
		}
		System.out.println("insertarUpdateCteRecibeTitulos--->"+sql);
		return (sql.toString());
	}

	/**
	 * Lista la moneda de negociacion asociada al titulo y su descripcion
	 * 
	 * @param long idCliente
	 * @param String
	 *            idTitulo
	 */
	/*public void listarTituloMoneda(String idTitulo) throws Exception {
		// TODO Eliminar este método
		StringBuffer sql = new StringBuffer();
		sql.append("select INFI_VI_MONEDAS.MONEDA_DESCRIPCION,infi_tb_100_titulos.TITULO_MONEDA_NEG from infi_tb_100_titulos left join INFI_VI_MONEDAS on infi_tb_100_titulos.TITULO_MONEDA_NEG=INFI_VI_MONEDAS.MONEDA_ID where ");
		sql.append(" trim(infi_tb_100_titulos.titulo_id)=trim('").append(idTitulo).append("')");
		dataSet = db.get(dataSource, sql.toString());
	}*/

	/**
	 * Lista la moneda de Denominacion asociada al titulo y su descripcion
	 * 
	 * @param String
	 *            idTitulo
	 */
	/*public void listarTituloMonedaDen(String idTitulo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select INFI_VI_MONEDAS.MONEDA_DESCRIPCION,titulo_moneda_den,titulo_id from infi_tb_100_titulos left join INFI_VI_MONEDAS on titulo_moneda_den=INFI_VI_MONEDAS.MONEDA_ID where trim(titulo_id)=");
		sql.append("trim('").append(idTitulo).append("')");
		dataSet = db.get(dataSource, sql.toString());
	}*/

	/**
	 * Busca los t&iacute;tulos que posea un cliente en custodia para el combo ajax.
	 * 
	 * @param long idCliente id del cliente
	 * @param boolean codAjax en caso de ser true se buscan los titulos de un cliente especifico,en caso contrario se muestran todos los titulos en CUSTODIA
	 * @throws lanza
	 *             una excepci&oacute;n en caso de fallar en la consulta
	 */
	public void listarTitulosEnCustodiaPorClienteAjax(long idCliente, boolean codAjax) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (codAjax) {
			sb.append("select infi_tb_100_titulos.TITULO_ID,infi_tb_100_titulos.TITULO_DESCRIPCION,infi_tb_701_titulos.* from infi_tb_701_titulos left join infi_tb_100_titulos on trim(infi_tb_701_titulos.TITULO_ID)=trim(infi_tb_100_titulos.TITULO_ID)where client_id=");
			sb.append(idCliente);
		} else {
			sb.append("select unique infi_tb_100_titulos.TITULO_ID,infi_tb_100_titulos.TITULO_DESCRIPCION from infi_tb_701_titulos left join infi_tb_100_titulos on trim(infi_tb_701_titulos.TITULO_ID)=trim(infi_tb_100_titulos.TITULO_ID)where 1=1");
		}
		dataSet = db.get(dataSource, sb.toString());
	}

	/**
	 * Busca los t&iacute;tulos segun la condicion de busqueda en custodia para exportar a Excel.
	 * 
	 * @param long idCliente id del cliente
	 * @param String
	 *            tituloId
	 * @throws lanza
	 *             una excepci&oacute;n en caso de fallar en la consulta
	 */
	public void listarTitulosEnCustodiaExportarExcel(long idCliente, String tituloId) throws Exception {
		StringBuffer sb = new StringBuffer();
		String filtroCliente = "";
		if (idCliente != 0)
			filtroCliente = " and b.client_id = " + idCliente;
		sb
				.append("select b.CLIENT_NOMBRE, c.titulo_moneda_den, c.TITULO_MONEDA_NEG, c.TITULO_FE_EMISION, c.titulo_fe_vencimiento, a.TITULO_FE_INGRESO_CUSTODIA,e.TITULO_ID,a.tipo_producto_id, e.TITCUS_CANTIDAD,(INFI_TB_700_TIPO_BLOQUEO.TIPBLO_DESCRIPCION) as estado, e.titcus_cantidad as total from infi_tb_701_titulos a, infi_tb_704_titulos_bloqueo e,infi_tb_201_ctes b,infi_tb_100_titulos c,INFI_TB_700_TIPO_BLOQUEO where a.client_id = b.client_id and a.titulo_id = c.titulo_id  and a.titulo_id = e.titulo_id(+) and a.client_id = e.client_id(+)and e.tipblo_id= INFI_TB_700_TIPO_BLOQUEO.tipblo_id and e.titcus_cantidad > 0 ");
		sb.append(filtroCliente);
		sb
				.append(" UNION select b.CLIENT_NOMBRE, c.titulo_moneda_den,c.TITULO_MONEDA_NEG, c.TITULO_FE_EMISION,c.titulo_fe_vencimiento,a.TITULO_FE_INGRESO_CUSTODIA, a.titulo_id,a.tipo_producto_id,a.titcus_cantidad,'Disponible' as estado, (a.titcus_cantidad - decode((select sum(bloq.titcus_cantidad)from infi_tb_704_titulos_bloqueo bloq where a.client_id = client_id(+) and a.titulo_id = titulo_id(+)) , null, 0, (select sum(bloq.titcus_cantidad) from infi_tb_704_titulos_bloqueo bloq where a.client_id = client_id(+) and a.titulo_id = titulo_id(+))  )) as total from infi_tb_701_titulos a, infi_tb_201_ctes b, infi_tb_100_titulos c,  infi_tb_704_titulos_bloqueo e where a.client_id = b.client_id  and a.client_id = e.client_id(+) and a.titulo_id = c.titulo_id  and a.titulo_id = e.titulo_id(+) and a.titcus_cantidad > 0");
		sb.append(filtroCliente);
		if (tituloId != null && !tituloId.equalsIgnoreCase("")) {
			sb.append(" and c.TITULO_ID='").append(tituloId).append("'");
		}
		dataSet = db.get(dataSource, sb.toString());
		/*	StringBuffer sb = new StringBuffer();
		Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		String filtroCliente = "";
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;


		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		try{
		//if(fecha_sistema>=fecha_reconversion){
		if (idCliente != 0)
			filtroCliente = " and b.client_id = " + idCliente;
		sb
				.append("select b.CLIENT_NOMBRE, case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),c.titulo_moneda_den)  when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),c.titulo_moneda_den) end titulo_moneda_den, case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(c.TITULO_MONEDA_NEG,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.TITULO_MONEDA_NEG,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),c.TITULO_MONEDA_NEG)  when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),c.TITULO_MONEDA_NEG) end TITULO_MONEDA_NEG, c.TITULO_FE_EMISION, c.titulo_fe_vencimiento, a.TITULO_FE_INGRESO_CUSTODIA,e.TITULO_ID,a.tipo_producto_id, case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(e.TITCUS_CANTIDAD,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),e.TITCUS_CANTIDAD) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then e.TITCUS_CANTIDAD end TITCUS_CANTIDAD,(INFI_TB_700_TIPO_BLOQUEO.TIPBLO_DESCRIPCION) as estado,case when '20180720' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(e.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),e.titcus_cantidad) when '20180720' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then e.titcus_cantidad end total from infi_tb_701_titulos a, infi_tb_704_titulos_bloqueo e,infi_tb_201_ctes b,infi_tb_100_titulos c,INFI_TB_700_TIPO_BLOQUEO where a.client_id = b.client_id and a.titulo_id = c.titulo_id  and a.titulo_id = e.titulo_id(+) and a.client_id = e.client_id(+)and e.tipblo_id= INFI_TB_700_TIPO_BLOQUEO.tipblo_id and e.titcus_cantidad > 0 ");
		sb.append(filtroCliente);
		sb.append(" UNION select b.CLIENT_NOMBRE, case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),c.titulo_moneda_den)  when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),c.titulo_moneda_den) end titulo_moneda_den,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(c.TITULO_MONEDA_NEG,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.TITULO_MONEDA_NEG,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),c.TITULO_MONEDA_NEG)  when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),c.TITULO_MONEDA_NEG) end TITULO_MONEDA_NEG, c.TITULO_FE_EMISION,c.titulo_fe_vencimiento,a.TITULO_FE_INGRESO_CUSTODIA, a.titulo_id,a.tipo_producto_id,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(a.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),a.titcus_cantidad) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then a.titcus_cantidad end titcus_cantidad,'Disponible' as estado,case when '20180720' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0((a.titcus_cantidad - decode((select sum(bloq.titcus_cantidad) from infi_tb_704_titulos_bloqueo bloq where  a.client_id = client_id(+) and a.titulo_id = titulo_id(+)) , null, 0, (select sum(bloq.titcus_cantidad) from infi_tb_704_titulos_bloqueo bloq where  a.client_id = client_id(+) and a.titulo_id = titulo_id(+))  )),FVJ8ODIV0('BFE_FECHA_VIGENCIA')),(a.titcus_cantidad - decode((select sum(bloq.titcus_cantidad) from infi_tb_704_titulos_bloqueo bloq where  a.client_id = client_id(+) and a.titulo_id = titulo_id(+)) , null, 0, (select sum(bloq.titcus_cantidad) from infi_tb_704_titulos_bloqueo bloq where  a.client_id = client_id(+) and a.titulo_id = titulo_id(+))  )))when '20180720' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then (a.titcus_cantidad - decode((select sum(bloq.titcus_cantidad) from infi_tb_704_titulos_bloqueo bloq where  a.client_id = client_id(+) and a.titulo_id = titulo_id(+)) , null, 0, (select sum(bloq.titcus_cantidad) from infi_tb_704_titulos_bloqueo bloq where  a.client_id = client_id(+) and a.titulo_id = titulo_id(+)))) end total from infi_tb_701_titulos a, infi_tb_201_ctes b, infi_tb_100_titulos c,  infi_tb_704_titulos_bloqueo e where a.client_id = b.client_id  and a.client_id = e.client_id(+) and a.titulo_id = c.titulo_id  and a.titulo_id = e.titulo_id(+) and a.titcus_cantidad > 0");
		sb.append(filtroCliente);
		if (tituloId != null && !tituloId.equalsIgnoreCase("")) {
			sb.append(" and c.TITULO_ID='").append(tituloId).append("'");
		}
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listarTitulosEnCustodiaExportarExcel "+sb+" 1");
	
		}catch(Exception e){
			System.out.println(e.getMessage());
		}*/
	}

	/**
	 * Suma la cantidad de clientes por un titulo especifico en custodia tambien la cantidad de tipo de persona por titulo y el monto total por titulo.
	 * 
	 * @param String
	 *            tituloId
	 * @throws lanza
	 *             una excepci&oacute;n en caso de fallar en la consulta
	 */
	public void sumarClientesPorTitulo(String tituloId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select titulo_id,tipo_producto_id,b.tipper_id,count(titulo_id)as clientes," + "sum(titcus_cantidad) as total,count(titulo_id) * 100 / (select count(*) as clientestotal  from infi_tb_701_titulos where titcus_cantidad>0) porcentaje from infi_tb_701_titulos a, infi_tb_201_ctes b " + "where 1=1 and a.client_id = b.client_id ");
		if (tituloId != null && !tituloId.equals("")) {
			sb.append(" and trim(titulo_id)='").append(tituloId).append("'");
		}
		sb.append(" and titcus_cantidad>0 group by TITULO_ID, TIPO_PRODUCTO_ID, b.tipper_id");
		if (tituloId == null || tituloId.equals("")) {
			sb.append(" order by TITULO_ID");
		}
		dataSet = db.get(dataSource, sb.toString());
		
		/*StringBuffer sb = new StringBuffer();
		//if(){
		Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;


		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		
		//if(fecha_sistema>=fecha_reconversion){
		sb.append("select titulo_id,tipo_producto_id,b.tipper_id,count(titulo_id)as clientes, case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(a.titulo_fe_ingreso_custodia, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(sum(titcus_cantidad),FVJ8ODIV0('BFE_FECHA_VIGENCIA')),sum(titcus_cantidad)) else sum(titcus_cantidad) end total, count(titulo_id) * 100 / (select count(*) as clientestotal  from infi_tb_701_titulos where titcus_cantidad>0) porcentaje from infi_tb_701_titulos a, infi_tb_201_ctes b, secm c " + "where 1=1 and a.client_id = b.client_id ");
		if (tituloId != null && !tituloId.equals("")) {
			sb.append(" and trim(titulo_id)='").append(tituloId).append("'");
		}
		sb.append(" and titcus_cantidad>0 and trim(a.titulo_id) = trim(c.secid) group by TITULO_ID, TIPO_PRODUCTO_ID, b.tipper_id,ccy,titulo_fe_ingreso_custodia");
		if (tituloId == null || tituloId.equals("")) {
			sb.append(" order by TITULO_ID");
		}
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("sumarClientesPorTitulo "+sb+" 1");*/
		
	}

	/**
	 * Lista el porcentaje de clientes para un titulo especifico
	 * 
	 * @param Bigdecimal
	 *            clientesPorTitulo
	 * @throws lanza
	 *             una excepci&oacute;n en caso de fallar en la consulta
	 */
	public BigDecimal listarPorcentajePorTitulo(BigDecimal clientes) throws Exception {
		StringBuffer sb = new StringBuffer();
		BigDecimal porcentaje = new BigDecimal(0);
		sb.append("select count(*)as clientestotal from infi_tb_701_titulos where titcus_cantidad>0");
		dataSet = db.get(dataSource, sb.toString());
		if (dataSet.count() > 0) {
			dataSet.first();
			dataSet.next();
			BigDecimal cliente = new BigDecimal(dataSet.getValue("clientestotal"));
			clientes = clientes.multiply(new BigDecimal(100));
			porcentaje = clientes.divide(cliente, 3, BigDecimal.ROUND_HALF_EVEN);
		}
		return porcentaje;

	}

	/**
	 * Metodo que lista los valores en custodia para exportar Excel
	 * 
	 * @param transaccion
	 * @param fechaDesde
	 * @param fechaHasta
	 * @throws Exception
	 */
	public void listarValoresEnCustodiaExcel(String transaccion, String fechaDesde, String fechaHasta) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select fecha ordene_ped_fe_orden,titulo_moneda_den moneda_id,sum(cantidad_entradas) ordenes_entrantes,sum(monto_entradas) cantidad_entrantes,sum(cantidad_salidas) ordenes_salidas,sum(monto_salidas) cantidad_salidas from (");
		sb.append(" select trunc(a.ordene_ped_fe_orden) as fecha,c.titulo_moneda_den,count(b.titulo_unidades) as cantidad_entradas,sum(b.titulo_unidades) as monto_entradas,0 cantidad_salidas,0 monto_salidas from infi_tb_204_ordenes a inner join");
		sb.append(" infi_tb_206_ordenes_titulos b on a.ordene_id = b.ordene_id ");
		sb.append(" inner join infi_tb_100_titulos c on b.titulo_id = c.titulo_id ");
		sb.append(" where transa_id in('TOMA_ORDEN','TOMA_ORDEN_CARTERA_PROPIA') ");
		sb.append(" and ORDSTA_ID = 'LIQUIDADA' and trunc(ordene_ped_fe_orden) between to_date('").append(fechaDesde).append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
		sb.append(" group by trunc(a.ordene_ped_fe_orden),c.titulo_moneda_den ");
		sb.append(" union ");
		sb.append(" select trunc(a.ordene_ped_fe_orden) as fecha,c.titulo_moneda_den,count(b.titulo_unidades) as cantidad_entradas,sum(b.titulo_unidades) as monto_entradas,0 cantidad_salidas,0 monto_salidas from infi_tb_204_ordenes a inner join ");
		sb.append(" infi_tb_206_ordenes_titulos b on a.ordene_id = b.ordene_id ");
		sb.append(" inner join infi_tb_100_titulos c on b.titulo_id = c.titulo_id ");
		sb.append(" where transa_id in('ENTRADA_TITULO') and trunc(ordene_ped_fe_orden)between to_date('").append(fechaDesde).append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
		sb.append(" group by trunc(a.ordene_ped_fe_orden),c.titulo_moneda_den ");
		sb.append(" union ");
		sb.append(" select trunc(a.ordene_ped_fe_orden) as fecha,c.titulo_moneda_den,0 as cantidad_entradas,0 monto_entradas,count(b.titulo_unidades) as cantidad_entradas,sum(b.titulo_unidades) as monto_entradas from infi_tb_204_ordenes a inner join ");
		sb.append(" infi_tb_206_ordenes_titulos b on a.ordene_id = b.ordene_id ");
		sb.append(" inner join infi_tb_100_titulos c on b.titulo_id = c.titulo_id ");
		sb.append(" where transa_id in('VENTA_TITULOS','SALIDA_INTERNA','SALIDA_EXTERNA','PACTO_RECOMPRA') and trunc(ordene_ped_fe_orden) between to_date('").append(fechaDesde).append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
		sb.append(" group by trunc(a.ordene_ped_fe_orden),c.titulo_moneda_den	 	  ");
		sb.append(" ) ");
		sb.append("group by fecha,titulo_moneda_den ");
		sb.append("order by moneda_id,ordene_ped_fe_orden");
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listarValoresEnCustodiaExcel "+sb);
	}

	/**
	 * Metodo que lista los Clientes para exportar Excel
	 * 
	 * @param tipoPersona
	 * @param titulo
	 * @param fechaHasta
	 * @throws Exception
	 */
	public void listarClientesExcel(String tipoPersona, String titulo, String tipoProductoId,String fechaHasta) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select a.client_id,a.titcus_cantidad, a.titulo_fe_ingreso_custodia, b.secid titulo_id, " +
				" a.tipo_producto_id, b.ccy TITULO_MONEDA_DEN, c.tipper_id, c.client_nombre, c.CLIENT_CEDRIF," +
				" (a.titcus_cantidad*d.tcc_tasa_cambio_compra) as total,d.tcc_tasa_cambio_compra as TITULO_VALOR_NOMINAL " +
				" from infi_tb_701_titulos a, secm b, infi_tb_201_ctes c,infi_vi_tasa_cam_cierre_diario d where " +
				" trim(a.titulo_id) = trim(b.secid) and b.ccy = d.TCC_CODIGO_DIVISA and a.client_id = c.client_id " +
				" and a.titulo_fe_ingreso_custodia <= to_date ('" + fechaHasta + "', '").append(ConstantesGenerales.FORMATO_FECHA + "') ");
				if (tipoPersona != null && !tipoPersona.equals("")){
					sb.append(" and c.tipper_id='").append(tipoPersona).append("'");
				}
				if (titulo != null && !titulo.equals("") && !titulo.equalsIgnoreCase("todos")){
					sb.append(" and trim(b.secid)='").append(titulo).append("'");
				}
				if (tipoProductoId != null && !tipoProductoId.equals("")){
					sb.append(" and a.tipo_producto_id='" + tipoProductoId + "'");
				}
				sb.append(" order by c.client_nombre ASC");
		dataSet = db.get(dataSource, sb.toString());
		
		/*	StringBuilder sb = new StringBuilder();
		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);//nm36635
		String fecha_reme = fechaHasta; //nm3363
		Date fecha_remediacion = null;
		fecha_remediacion = formato_fecha.parse(fecha_reme);
		//Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha_remediacion);
	
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		
		System.out.println(fecha_reme);
		System.out.println(fecha_remediacion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		
		if(fecha_sistema>=fecha_reconversion){
		sb.append("select a.client_id,DECODE(b.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(a.titcus_cantidad,'" + fecha_funcion + "'),a.titcus_cantidad) titcus_cantidad, a.titulo_fe_ingreso_custodia, b.secid titulo_id, " +
				" a.tipo_producto_id, b.ccy TITULO_MONEDA_DEN, c.tipper_id, c.client_nombre, c.CLIENT_CEDRIF," +
				" DECODE(b.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0((a.titcus_cantidad*d.tcc_tasa_cambio_compra),'" + fecha_funcion + "'),(a.titcus_cantidad*d.tcc_tasa_cambio_compra)) total,d.tcc_tasa_cambio_compra as TITULO_VALOR_NOMINAL " +
				" from infi_tb_701_titulos a, secm b, infi_tb_201_ctes c,infi_vi_tasa_cam_cierre_diario d where " +
				" trim(a.titulo_id) = trim(b.secid) and b.ccy = d.TCC_CODIGO_DIVISA and a.client_id = c.client_id " +
				" and a.titulo_fe_ingreso_custodia <= to_date ('" + fechaHasta + "', '").append(ConstantesGenerales.FORMATO_FECHA + "') ");
				if (tipoPersona != null && !tipoPersona.equals("")){
					sb.append(" and c.tipper_id='").append(tipoPersona).append("'");
				}
				if (titulo != null && !titulo.equals("") && !titulo.equalsIgnoreCase("todos")){
					sb.append(" and trim(b.secid)='").append(titulo).append("'");
				}
				if (tipoProductoId != null && !tipoProductoId.equals("")){
					sb.append(" and a.tipo_producto_id='" + tipoProductoId + "'");
				}
				sb.append(" order by c.client_nombre ASC");
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listarClientesExcel "+sb+" 1");
		}else{
			
			sb.append("select a.client_id,a.titcus_cantidad, a.titulo_fe_ingreso_custodia, b.secid titulo_id, " +
					" a.tipo_producto_id, b.ccy TITULO_MONEDA_DEN, c.tipper_id, c.client_nombre, c.CLIENT_CEDRIF," +
					" (a.titcus_cantidad*d.tcc_tasa_cambio_compra) as total,d.tcc_tasa_cambio_compra as TITULO_VALOR_NOMINAL " +
					" from infi_tb_701_titulos a, secm b, infi_tb_201_ctes c,infi_vi_tasa_cam_cierre_diario d where " +
					" trim(a.titulo_id) = trim(b.secid) and b.ccy = d.TCC_CODIGO_DIVISA and a.client_id = c.client_id " +
					" and a.titulo_fe_ingreso_custodia <= to_date ('" + fechaHasta + "', '").append(ConstantesGenerales.FORMATO_FECHA + "') ");
					if (tipoPersona != null && !tipoPersona.equals("")){
						sb.append(" and c.tipper_id='").append(tipoPersona).append("'");
					}
					if (titulo != null && !titulo.equals("") && !titulo.equalsIgnoreCase("todos")){
						sb.append(" and trim(b.secid)='").append(titulo).append("'");
					}
					if (tipoProductoId != null && !tipoProductoId.equals("")){
						sb.append(" and a.tipo_producto_id='" + tipoProductoId + "'");
					}
					sb.append(" order by c.client_nombre ASC");
			dataSet = db.get(dataSource, sb.toString());
			System.out.println("listarClientesExcel "+sb+" 2");
		}*/
	}

	/**
	 * Metodo que lista los valores de garantias para exportar Excel
	 * 
	 * @param cliente
	 * @param tipoBloqueo
	 * @param fechaHasta
	 * @throws Exception
	 */

	public void listarValoresEnGarantiasExcel(String cliente, String tipoBloqueo, String tipoProducto, String fechaHasta) throws Exception {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);//nm36635
		String fecha_reme = fechaHasta; //nm3363
		Date fecha_remediacion = null;
		fecha_remediacion = formato_fecha.parse(fecha_reme);
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha_remediacion);
		int fecha_sistema= Integer.parseInt(fecha_funcion);

		sb.append("select a.tasa_cambio,a.titulo_id,a.tipo_producto_id,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(a.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),a.titcus_cantidad) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then a.titcus_cantidad end cantidad_bloqueada,a.numero_garantia,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0((a.titcus_cantidad * a.tasa_cambio),FVJ8ODIV0('BFE_FECHA_VIGENCIA')),a.numero_garantia,(a.titcus_cantidad * a.tasa_cambio)) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then (a.titcus_cantidad * a.tasa_cambio) end total,c.ccy titulo_moneda_den, a.tipblo_id, d.tipblo_descripcion," +
				" e.beneficiario_id,e.beneficiario_nombre,f.client_nombre,f.client_cta_custod_id from infi_tb_705_titulos_bloq_hist a," +
				" (select client_id,titulo_id,tipo_producto_id, max(fecha) as fecha from infi_tb_705_titulos_bloq_hist " +
				" where trunc(fecha) <=  to_date('" + fechaHasta + "', '" + ConstantesGenerales.FORMATO_FECHA + "') group by client_id,titulo_id,tipo_producto_id) b," +
				" secm c,infi_tb_700_tipo_bloqueo d,infi_tb_039_beneficiarios e, infi_tb_201_ctes f " +
				" WHERE a.titulo_id = b.titulo_id and a.tipo_producto_id = b.tipo_producto_id and a.client_id = b.client_id and a.FECHA = b.fecha " +
				" and trim(a.titulo_id) = trim(c.secid) and a.tipblo_id = d.tipblo_id " +
				" and a.beneficiario_id = e.beneficiario_id and a.client_id = f.client_id ");
		
				if (tipoProducto != null && !tipoProducto.equals("")){
					sb.append(" and a.tipo_producto_id = '" + tipoProducto + "'");
				}
				if (cliente != null && !cliente.equals("") && !cliente.equalsIgnoreCase("todos")){
					sb.append(" and a.client_id= ").append(cliente);
				}
				if (tipoBloqueo != null && !tipoBloqueo.equals("")){
					sb.append(" and a.tipblo_id = '").append(tipoBloqueo).append("'");
				}
				sb.append(" and trunc(a.fecha) <= to_date('" + fechaHasta + "', '" + ConstantesGenerales.FORMATO_FECHA + "') " +
				" order by f.client_nombre");
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listarValoresEnGarantiasExcel "+sb);
	}

	/**
	 * Metodo que lista los valores liberados para exportar Excel
	 * 
	 * @param cliente
	 * @param fechaDesde
	 * @param fechaHasta
	 * @throws Exception
	 */

	public void listarValoresLiberadosExcel(String cliente, String fechaDesde, String fechaHasta) throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a.client_nombre,b.client_id, b.ordene_id, b.TRANSA_ID, c.TITULO_ID,c.titulo_unidades,d.TITULO_VALOR_NOMINAL,");
		sb.append("b.ORDENE_PED_FE_ORDEN,a.CLIENT_CTA_CUSTOD_ID,");
		sb.append("fvj8ocon0((c.titulo_unidades * b.ORDENE_TASA_CAMBIO),'20180503') TOTAL ");
		sb.append("from INFI_TB_201_CTES a,INFI_TB_204_ORDENES b, infi_tb_206_ordenes_titulos c,infi_tb_100_titulos d ");
		sb.append("where a.CLIENT_ID=b.CLIENT_ID ");
		sb.append("and b.TRANSA_ID='DESBLOQUEO_TITULOS' ");
		sb.append("and c.ORDENE_ID =b.ORDENE_ID ");
		sb.append("and d.TITULO_ID=c.TITULO_ID ");

		if (cliente != null && !cliente.equals("") && !cliente.equalsIgnoreCase("todos"))
			sb.append(" and a.client_id = ").append(cliente);
		sb.append("   and trunc(b.ordene_ped_fe_orden) between to_date('").append(fechaDesde);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");

		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listarValoresLiberadosExcel "+sb);

	}

	/**
	 * Metodo que lista los titulos Bloqueados para exportar Excel
	 * 
	 * @param cliente
	 * @param titulos
	 * @param fechaDesde
	 * @param fechaHasta
	 * @throws Exception
	 */

	public void listartitulosBloqueadosExcel(String cliente, String titulos, String fechaHasta, String fechaDesde) throws Exception {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);//nm36635
		String fecha_reme = fechaHasta; //nm3363
		Date fecha_remediacion = null;
		fecha_remediacion = formato_fecha.parse(fecha_reme);
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha_remediacion);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
	//	System.out.println(fecha_sistema);
		
		sb.append("select INFI_TB_704_TITULOS_BLOQUEO.titulo_id,INFI_TB_700_TIPO_BLOQUEO.tipblo_descripcion, INFI_TB_039_BENEFICIARIOS.beneficiario_nombre,INFI_TB_704_TITULOS_BLOQUEO.fecha, INFI_TB_704_TITULOS_BLOQUEO.numero_garantia,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(INFI_TB_704_TITULOS_BLOQUEO.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),INFI_TB_704_TITULOS_BLOQUEO.titcus_cantidad) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then INFI_TB_704_TITULOS_BLOQUEO.titcus_cantidad end titcus_cantidad,INFI_TB_201_CTES.client_nombre from INFI_TB_704_TITULOS_BLOQUEO left join INFI_TB_201_CTES on INFI_TB_201_CTES.client_id=INFI_TB_704_TITULOS_BLOQUEO.client_id left join INFI_TB_039_BENEFICIARIOS on INFI_TB_039_BENEFICIARIOS.beneficiario_id=INFI_TB_704_TITULOS_BLOQUEO.beneficiario_id left join INFI_TB_700_TIPO_BLOQUEO on INFI_TB_704_TITULOS_BLOQUEO.tipblo_id=INFI_TB_700_TIPO_BLOQUEO.tipblo_id inner join secm on trim(secm.SECID)=trim(INFI_TB_704_TITULOS_BLOQUEO.TITULO_ID) where INFI_TB_704_TITULOS_BLOQUEO.fecha=INFI_TB_704_TITULOS_BLOQUEO.fecha");
		if (cliente != null && !cliente.equals("") && !cliente.equalsIgnoreCase("todos"))
			sb.append(" and INFI_TB_201_CTES.client_id= ").append(cliente);
		if (titulos != null && !titulos.equals("") && !titulos.equalsIgnoreCase("todos"))
			sb.append(" and INFI_TB_704_TITULOS_BLOQUEO.titulo_id ='").append(titulos).append("'");
		sb.append(" and trunc(INFI_TB_704_TITULOS_BLOQUEO.fecha) between to_date ('").append(fechaDesde);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date ('").append(fechaHasta);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
		sb.append(" order by INFI_TB_201_CTES.client_nombre");
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listartitulosBloqueadosExcel "+sb);

	}

	/**
	 * Lista la informacion para el reporte en excel de Posicion Global
	 * @param clientId id del cliente
	 * @param idTitulo id del título
	 * @param tipoProductoId tipo de producto
	 * @param fecha fecha de corte 
	 * @throws Exception en caso de error
	 */
	public String listarPosicionGlobalDataExcel(long clientId, String idTitulo, String tipoProductoId, String fecha) throws Exception {
		SimpleDateFormat formato_fecha = new SimpleDateFormat("dd-MM-yyyy");
	    StringBuffer strSQL = new StringBuffer();
	    String filtroCliente = "";
	    String filtroTitulo = "";
	    String filtroTipoProducto = "";
	    String filtroTitulo2 = "";
	    String filtroTipoProducto2 = "";
	    String fecha_reme = fecha;
	    Date fecha_remediacion = null;
	    fecha_remediacion = formato_fecha.parse(fecha_reme);
	    
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    String fecha_funcion = formatter.format(fecha_remediacion);
	    
	    int fecha_sistema = Integer.parseInt(fecha_funcion);
	    System.out.println(fecha_reme);
	    System.out.println(fecha_remediacion);
	    System.out.println(fecha_funcion);
	    if (clientId != 0L) {
	      filtroCliente = " and b.client_id = " + clientId;
	    }
	    if (!idTitulo.equals(""))
	    {
	      filtroTitulo = " and a.titulo_id='" + idTitulo + "'";
	      filtroTitulo2 = " and bloq.titulo_id='" + idTitulo + "'";
	    }
	    if (!tipoProductoId.equals(""))
	    {
	      filtroTipoProducto = " and a.tipo_producto_id='" + tipoProductoId + "'";
	      filtroTipoProducto2 = " and bloq.tipo_producto_id='" + tipoProductoId + "'";
	    }
	    strSQL.append("select titulo_id,tipo_producto_id,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(fecha_cierre, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),titcus_cantidad) else titcus_cantidad end titcus_cantidad,client_cedrif,client_cta_custod_id,client_id,client_nombre,titulos_precio_recompra,cantidad_bloqueada,estados,TCC_TASA_CAMBIO_COMPRA,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(fecha_cierre, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(total,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),total) else total end total,total_precio,titulo_fe_vencimiento,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA')  then  DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),titulo_moneda_den) else DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),titulo_moneda_den) end titulo_moneda_den from (select " + "a.fecha_cierre,a.titulo_id, a.tipo_producto_id,a.titcus_cantidad-FUNC_INFI_CANTIDAD_BLOQUEADA(a.client_id,a.titulo_id,a.tipo_producto_id,'" + fecha + "') titcus_cantidad, (b.tipper_id || '-' || b.client_cedrif) client_cedrif,b.client_cta_custod_id,b.client_id,b.client_nombre, (FUNC_INFI_PRECIO_RECOMPRA_HIST(a.titulo_id,a.tipo_producto_id,'" + fecha + "')) titulos_precio_recompra, 0 cantidad_bloqueada, 'DISPONIBLE' estados,a.tasa_cambio TCC_TASA_CAMBIO_COMPRA, " + "(a.titcus_cantidad-FUNC_INFI_CANTIDAD_BLOQUEADA(a.client_id,a.titulo_id,a.tipo_producto_id,'" + fecha + "') * a.tasa_cambio)total," + " (a.titcus_cantidad * FUNC_INFI_PRECIO_RECOMPRA_HIST(a.titulo_id,a.tipo_producto_id,'" + fecha + "')/100) as total_precio," + " e.mdate titulo_fe_vencimiento,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),e.ccy)  else DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),e.ccy) end titulo_moneda_den from infi_tb_702_titulos_cierre a," + " infi_tb_201_ctes b," + "( select client_id,titulo_id,tipo_producto_id,max(fecha_cierre) as fecha_cierre " + " from infi_tb_702_titulos_cierre where trunc(fecha_cierre) <= to_date('").append(fecha);
	    strSQL.append("','").append("dd-MM-yyyy").append("') ").append(" group by client_id,titulo_id,tipo_producto_id) d, secm e where a.client_id = b.client_id ");
	    strSQL.append(" and a.client_id = d.client_id and a.titulo_id = d.titulo_id and a.tipo_producto_id = d.tipo_producto_id ");
	    strSQL.append(" and a.fecha_cierre = d.fecha_cierre ");
	    strSQL.append(filtroCliente);
	    strSQL.append(filtroTitulo);
	    strSQL.append(filtroTipoProducto);
	    strSQL.append(" and trunc(a.fecha_cierre) <= to_date('").append(fecha).append("','");
	    strSQL.append("dd-MM-yyyy").append("') and trim(a.titulo_id) = trim(e.secid) ");
	    strSQL.append(" union ");
	    strSQL.append("select bloq.fecha fecha_cierre ,bloq.titulo_id, bloq.tipo_producto_id,bloq.titcus_cantidad,(b.tipper_id || '-' || b.client_cedrif) client_cedrif,b.client_cta_custod_id,b.client_id,b.client_nombre,0 precio_recompra,0 cantidad_bloqueada, tipbloq.tipblo_descripcion estados,bloq.tasa_cambio,(bloq.titcus_cantidad*bloq.tasa_cambio) total,(bloq.titcus_cantidad*FUNC_INFI_PRECIO_RECOMPRA_HIST(bloq.titulo_id,bloq.tipo_producto_id,'")
	    
	      .append(fecha).append("')/100) total_precio,e.mdate titulo_fe_vencimiento,case when '" + 
	      fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),e.ccy) else DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),e.ccy) end titulo_moneda_den from infi_tb_705_titulos_bloq_hist bloq, " + 
	      "infi_tb_201_ctes b, (select client_id,titulo_id,tipo_producto_id,max(fecha) as fecha," + 
	      "tipblo_id from infi_tb_705_titulos_bloq_hist where trunc(fecha) <= to_date('").append(fecha).append("', 'dd-MM-yyyy') group by client_id,titulo_id,tipo_producto_id,tipblo_id) bloqhist, secm e, infi_tb_700_tipo_bloqueo tipbloq where bloq.client_id = b.client_id and bloq.client_id = bloqhist.client_id and bloq.titulo_id = bloqhist.titulo_id and bloq.tipo_producto_id = bloqhist.tipo_producto_id and bloq.fecha = bloqhist.fecha and bloq.tipblo_id = bloqhist.tipblo_id and bloq.titcus_cantidad > 0 and bloq.tipblo_id  = tipbloq.tipblo_id ");
	    
	    strSQL.append(filtroCliente);
	    strSQL.append(filtroTitulo2);
	    strSQL.append(filtroTipoProducto2);
	    strSQL.append(" and trim(bloq.titulo_id) = trim(e.secid) ) where titcus_cantidad > 0 order by tipo_producto_id,client_id,titulo_id,titulo_moneda_den,estados desc ");
	    
	    System.out.println("listarPosicionGlobalDataExcelhola " + strSQL.toString());
	    return strSQL.toString();
//		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);//nm36635
//		StringBuffer strSQL = new StringBuffer();
//		String filtroCliente = "";
//		String filtroTitulo = "";
//		String filtroTipoProducto = "";
//		String filtroTitulo2 = "";
//		String filtroTipoProducto2 = "";
//		String fecha_reme = fecha; //nm33631111111111111111111111111
//		Date fecha_remediacion = null;
//		fecha_remediacion = formato_fecha.parse(fecha_reme);
//		
//		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
//		String fecha_funcion = formatter.format(fecha_remediacion);
//		
//		
//		//String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
//
//
//		//int fecha_reconversion= Integer.parseInt(fecha_re);
//		int fecha_sistema= Integer.parseInt(fecha_funcion);
//		System.out.println(fecha_reme);
//		System.out.println(fecha_remediacion);
//		System.out.println(fecha_funcion);
//		//System.out.println(fecha_sistema+" "+fecha_reconversion);
//		//if(fecha_sistema >= fecha_reconversion){
//			
//		if (clientId != 0) {
//			filtroCliente = " and b.client_id = " + clientId;			
//		}
//		
//		if (!idTitulo.equals("")) {
//			filtroTitulo = " and a.titulo_id='" + idTitulo + "'";
//			filtroTitulo2 = " and bloq.titulo_id='" + idTitulo + "'";
//		}
//		if (!tipoProductoId.equals("")) {
//			filtroTipoProducto = " and a.tipo_producto_id='" + tipoProductoId + "'";
//			filtroTipoProducto2 = " and bloq.tipo_producto_id='" + tipoProductoId + "'";
//		}
//		
//		//case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(a.titcus_cantidad-FUNC_INFI_CANTIDAD_BLOQUEADA(a.client_id,a.titulo_id,a.tipo_producto_id,'" + fecha + "'),FVJ8ODIV0('BFE_FECHA_VIGENCIA')),a.titcus_cantidad-FUNC_INFI_CANTIDAD_BLOQUEADA(a.client_id,a.titulo_id,a.tipo_producto_id,'" + fecha + "')) when '" + fecha_sistema + "' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then a.titcus_cantidad-FUNC_INFI_CANTIDAD_BLOQUEADA(a.client_id,a.titulo_id,a.tipo_producto_id,'" + fecha + "') end titcus_cantidad
//		//String fechaprueba="20180503" ;
//		strSQL.append("select titulo_id,tipo_producto_id,titcus_cantidad,client_cedrif,client_cta_custod_id,client_id,client_nombre,titulos_precio_recompra,cantidad_bloqueada,estados,TCC_TASA_CAMBIO_COMPRA,total,total_precio,titulo_fe_vencimiento,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA')  then  DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),titulo_moneda_den) else DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),titulo_moneda_den) end titulo_moneda_den from (select " +
//				"a.fecha_cierre,a.titulo_id, a.tipo_producto_id,a.titcus_cantidad-FUNC_INFI_CANTIDAD_BLOQUEADA(a.client_id,a.titulo_id,a.tipo_producto_id,'" + fecha + "') titcus_cantidad, (b.tipper_id || '-' || b.client_cedrif) client_cedrif,b.client_cta_custod_id,b.client_id,b.client_nombre, (FUNC_INFI_PRECIO_RECOMPRA_HIST(a.titulo_id,a.tipo_producto_id,'" + fecha + "')) titulos_precio_recompra, 0 cantidad_bloqueada, 'DISPONIBLE' estados,a.tasa_cambio TCC_TASA_CAMBIO_COMPRA, " +
//				"(a.titcus_cantidad-FUNC_INFI_CANTIDAD_BLOQUEADA(a.client_id,a.titulo_id,a.tipo_producto_id,'" + fecha + "') * a.tasa_cambio)total,"+
//				 " (a.titcus_cantidad * FUNC_INFI_PRECIO_RECOMPRA_HIST(a.titulo_id,a.tipo_producto_id,'" + fecha + "')/100) as total_precio," + " e.mdate titulo_fe_vencimiento,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),e.ccy)  else DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),e.ccy) end titulo_moneda_den from infi_tb_702_titulos_cierre a," + " infi_tb_201_ctes b," +
//				"( select client_id,titulo_id,tipo_producto_id,max(fecha_cierre) as fecha_cierre " + " from infi_tb_702_titulos_cierre where trunc(fecha_cierre) <= to_date('").append(fecha);
//		strSQL.append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ").append(" group by client_id,titulo_id,tipo_producto_id) d, secm e where a.client_id = b.client_id ");
//		strSQL.append(" and a.client_id = d.client_id and a.titulo_id = d.titulo_id and a.tipo_producto_id = d.tipo_producto_id ");
//		strSQL.append(" and a.fecha_cierre = d.fecha_cierre ");
//		strSQL.append(filtroCliente);
//		strSQL.append(filtroTitulo);
//		strSQL.append(filtroTipoProducto);
//		strSQL.append(" and trunc(a.fecha_cierre) <= to_date('").append(fecha).append("','");
//		strSQL.append(ConstantesGenerales.FORMATO_FECHA).append("') and trim(a.titulo_id) = trim(e.secid) ");
//		strSQL.append(" union ");
//		strSQL.append("select bloq.fecha fecha_cierre ,bloq.titulo_id, bloq.tipo_producto_id,bloq.titcus_cantidad as titcus_cantidad,(b.tipper_id || '-' || b.client_cedrif) client_cedrif,b.client_cta_custod_id" 
//				  + ",b.client_id,b.client_nombre,0 precio_recompra,0 cantidad_bloqueada, tipbloq.tipblo_descripcion estados," +
//				  	"bloq.tasa_cambio,(bloq.titcus_cantidad*bloq.tasa_cambio) total," +
//				  	"(bloq.titcus_cantidad*FUNC_INFI_PRECIO_RECOMPRA_HIST(bloq.titulo_id,bloq.tipo_producto_id,'").append(fecha).append("')/100) total_precio," +
//				  	"e.mdate titulo_fe_vencimiento,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),e.ccy) else DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(e.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),e.ccy) end titulo_moneda_den from infi_tb_705_titulos_bloq_hist bloq, " +
//				  	"infi_tb_201_ctes b, (select client_id,titulo_id,tipo_producto_id,max(fecha) as fecha," +
//				  	"tipblo_id from infi_tb_705_titulos_bloq_hist where trunc(fecha) <= to_date('").append(fecha).append("', 'dd-MM-yyyy') " +
//				  	"group by client_id,titulo_id,tipo_producto_id,tipblo_id) bloqhist, secm e, infi_tb_700_tipo_bloqueo " +
//				  	"tipbloq where bloq.client_id = b.client_id and bloq.client_id = bloqhist.client_id and " +
//				  	"bloq.titulo_id = bloqhist.titulo_id and bloq.tipo_producto_id = bloqhist.tipo_producto_id and " +
//				  	"bloq.fecha = bloqhist.fecha and bloq.tipblo_id = bloqhist.tipblo_id and bloq.titcus_cantidad > 0 and " +
//				  	"bloq.tipblo_id  = tipbloq.tipblo_id ");
//		strSQL.append(filtroCliente);
//		strSQL.append(filtroTitulo2);
//		strSQL.append(filtroTipoProducto2); 
//		strSQL.append(" and trim(bloq.titulo_id) = trim(e.secid) ) where titcus_cantidad > 0 order by tipo_producto_id,client_id,titulo_id,titulo_moneda_den,estados desc ");
//		
//		System.out.println("listarPosicionGlobalDataExcelhola "+strSQL.toString());
//		return strSQL.toString();
	
		}	
	

	/**
	 * Método que busca la última fecha de pago de cupón. Es necesario para calcular los próximos cupones
	 * 
	 * @return Date fecha más antigua de pago de cupón. Devuelve null en caso de no encontrar registros
	 * */
	public Date buscarUltimaFechaPagoCupon() throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select min(titulo_fe_ult_pago_cupon) as ultimo_pago from infi_tb_701_titulos ");
		Date fechaUltimoPago = null;

		try {
			conn = this.dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(strSQL.toString());

			if (resultSet.next()) {
				fechaUltimoPago = resultSet.getDate("ultimo_pago");
			}
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}

		return fechaUltimoPago;
	}

	/**
	 * Lista los títulos que deben ser amortizados según la fecha indicada
	 * 
	 * @param fechaInicio
	 *            : fecha desde de seleccion de la data
	 * @param fechaFin
	 *            : fecha hasta de seleccion de la data
	 * @param idCliente
	 *            id del cliente que se desea consultar. Si se envia null consulta todos los clientes
	 * @param idTitulo
	 *            id del titulo para el cálculo de la amortización
	 * */
	public void listarTitulosAmortizar(Date fechaInicio, Date fechaFin, String idCliente, String idTitulo) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select distinct a.titulo_id,client_id,titcus_cantidad,intstrtdte fecha_inicio_amortizacion, intenddte fecha_fin_amortizacion,titulo_fe_ult_amortizacion,");
		strSQL.append("titulo_monto_ult_amortizacion,ipaydate,prinamt_8 pct_amortizacion,prinpayamt_8 depreciacion, c.titulo_moneda_den, c.titulo_moneda_neg from ");
		strSQL.append("  infi_tb_701_titulos a, ");
		strSQL.append("  secs b, ");
		strSQL.append("	 infi_tb_100_titulos c ");
		strSQL.append("	 where ");
		strSQL.append("		  trim(a.titulo_id) = trim(b.secid) ");
		strSQL.append("		  and trim(a.titulo_id) = c.titulo_id ");
		strSQL.append("		  and b.PRINPAYAMT_8 < 0 ");
		strSQL.append("		  and ipaydate between ").append(this.formatearFechaBD(fechaInicio));
		strSQL.append("       and ").append(this.formatearFechaBD(fechaFin));
		if (idCliente != null && !idCliente.equals("")) {
			strSQL.append("   and client_id=").append(idCliente);
		}
		// Verifica título
		if (idTitulo != null && !idTitulo.equals("")) {
			strSQL.append("   and a.titulo_id='").append(idTitulo).append("'");
		}
		strSQL.append("		  and a.TITULO_FE_ULT_AMORTIZACION < b.IPAYDATE order by client_id,ipaydate,titulo_id ");
		dataSet = db.get(dataSource, strSQL.toString());
	}

	/**
	 * Lista los títulos que deben ser amortizados
	 * 
	 * @throws lanza
	 *             una excepción en caso de error
	 */
	public void listarTitulosAmortizar() throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select distinct (a.titulo_id || '--' || to_char(intstrtdte,'").append(ConstantesGenerales.FORMATO_FECHA2).append("')");
		strSQL.append("|| '--' || to_char(intenddte,'").append(ConstantesGenerales.FORMATO_FECHA2).append("')) seleccion_id");
		strSQL.append(",a.titulo_id, intstrtdte fecha_inicio_amortizacion, ");
		strSQL.append(" intenddte fecha_fin_amortizacion, count(client_id) clientes from ");
		strSQL.append("infi_tb_701_titulos a,");
		strSQL.append("secs b, ");
		strSQL.append("infi_tb_100_titulos c");
		strSQL.append(" where ");
		strSQL.append("trim(a.titulo_id) = trim(b.secid)");
		strSQL.append("and trim(a.titulo_id) = c.titulo_id ");
		strSQL.append("and b.PRINPAYAMT_8 < 0 ");
		strSQL.append("and a.TITULO_FE_ULT_AMORTIZACION < b.IPAYDATE ");
		strSQL.append("and a.TITCUS_CANTIDAD > 0 ");
		strSQL.append("and SYSDATE >= b.IPAYDATE and a.TITULO_FE_ULT_AMORTIZACION <> intenddte ");
		strSQL.append("group by a.titulo_id, intstrtdte, intenddte ");
		strSQL.append("order by titulo_id ");
		
		dataSet = db.get(dataSource, strSQL.toString());
	}

	/**
	 * Metodo para listar el ultimo mes de pago de Amortizacion
	 * 
	 * @param String
	 *            mes
	 * @param String
	 *            anno
	 * */
	public String buscarUltimoMesAmortizacion() throws Exception {
		StringBuffer strSQl = new StringBuffer();
		strSQl.append("select to_char(min(IPAYDATE),'dd-mm-yyyy') as fecha_mas_antigua ");
		strSQl.append("from SECS ");
		strSQl.append("where trim(secs.secid) in ");
		strSQl.append("(select trim(tit.titulo_id) from INFI_TB_701_TITULOS tit inner join SECS on trim(secs.secid) = trim(tit.titulo_id) and PRINAMT_8 is not null ");
		strSQl.append("where tit.titulo_fe_ult_amortizacion is null or IPAYDATE >= tit.titulo_fe_ult_amortizacion ");
		strSQl.append("group by tit.titulo_id)");
		dataSet = db.get(dataSource, strSQl.toString());
		System.out.println("buscarUltimoMesAmortizacion "+strSQl);

		String resultado = null;
		if (dataSet.next()) {
			resultado = dataSet.getValue("fecha_mas_antigua");
		}
		return resultado;
	}

	/**
	 * Metodo que lista las transacciones liquidadas para exportar Excel
	 * 
	 * @param cliente
	 * @param transaccion
	 * @param fechaDesde
	 * @param fechaHasta
	 * @throws Exception
	 */
	public void listarTransaccionesLiquidadasExcel(String cliente, String transaccion, String fechaDesde, String fechaHasta) throws Exception {
		StringBuffer sb = new StringBuffer();
		String filtroCliente = "";
		if (cliente != null && !cliente.equals("") && !cliente.equalsIgnoreCase("todos"))
			filtroCliente = " and INFI_TB_201_CTES.client_id =" + cliente;

		sb.append("SELECT INFI_TB_201_CTES.CLIENT_NOMBRE, INFI_TB_204_ORDENES.ORDENE_PED_FE_VALOR,");
		sb.append("INFI_TB_201_CTES.CLIENT_CTA_CUSTOD_ID, INFI_TB_701_TITULOS.TITULO_ID,");
		sb.append("INFI_TB_204_ORDENES.CONTRAPARTE, INFI_TB_206_ORDENES_TITULOS.TITULO_UNIDADES AS TITULO_MONTO,");
		sb.append("INFI_TB_204_ORDENES.TRANSA_ID, (INFI_TB_206_ORDENES_TITULOS.TITULO_UNIDADES*INFI_VI_TASA_CAM_CIERRE_DIARIO.tcc_tasa_cambio_compra) AS TRANSACCION,");
		sb.append("  CASE WHEN INFI_TB_204_ORDENES.TRANSA_ID='SALIDA_INTERNA' THEN 'D/'||INFI_TB_204_ORDENES.ORDENE_ID ");
		sb.append("  	  WHEN INFI_TB_204_ORDENES.TRANSA_ID='SALIDA_EXTERNA' THEN 'D/'||INFI_TB_204_ORDENES.ORDENE_ID ");
		sb.append("		  WHEN INFI_TB_204_ORDENES.TRANSA_ID='ENTRADA_TITULO' THEN 'R/'||INFI_TB_204_ORDENES.ORDENE_ID ");
		sb.append("		  WHEN INFI_TB_204_ORDENES.TRANSA_ID= 'VENTA_TITULOS' THEN 'D/'||INFI_TB_204_ORDENES.ORDENE_ID ");
		sb.append("		  WHEN INFI_TB_204_ORDENES.TRANSA_ID= 'PACTO_RECOMPRA' THEN 'D/'||INFI_TB_204_ORDENES.ORDENE_ID ");		
		sb.append("		  END TIPO, ");
		sb.append("	 CASE WHEN INFI_TB_204_ORDENES.TRANSA_ID='SALIDA_INTERNA' THEN 'INTERNA' ");
		sb.append("	 	  WHEN INFI_TB_204_ORDENES.TRANSA_ID='SALIDA_EXTERNA' THEN 'EXTERNA' ");
		sb.append("	 	  WHEN INFI_TB_204_ORDENES.TRANSA_ID= 'VENTA_TITULOS' THEN 'INTERNA' END COMISION ");
		sb.append("	 FROM INFI_TB_204_ORDENES ");
		sb.append("		INNER JOIN     INFI_TB_206_ORDENES_TITULOS ON INFI_TB_204_ORDENES.ORDENE_ID=INFI_TB_206_ORDENES_TITULOS.ORDENE_ID ");
		sb.append("		INNER JOIN 	  INFI_TB_201_CTES ON INFI_TB_204_ORDENES.CLIENT_ID=INFI_TB_201_CTES.CLIENT_ID ");
		sb.append("		INNER JOIN	  INFI_TB_701_TITULOS on INFI_TB_206_ORDENES_TITULOS.TITULO_ID=INFI_TB_701_TITULOS.TITULO_ID AND INFI_TB_701_TITULOS.CLIENT_ID=INFI_TB_201_CTES.CLIENT_ID ");
		sb.append("		INNER JOIN    INFI_TB_100_TITULOS ON INFI_TB_206_ORDENES_TITULOS.TITULO_ID = INFI_TB_100_TITULOS.TITULO_ID ");
		sb.append("		INNER JOIN 	  INFI_VI_TASA_CAM_CIERRE_DIARIO ON INFI_TB_100_TITULOS.TITULO_MONEDA_DEN=INFI_VI_TASA_CAM_CIERRE_DIARIO.tcc_codigo_divisa ");
		sb.append("		WHERE 	  	  INFI_TB_204_ORDENES.TRANSA_ID in ('SALIDA_INTERNA','SALIDA_EXTERNA','ENTRADA_TITULO','VENTA_TITULOS','PACTO_RECOMPRA') ");
		if (transaccion != null && !transaccion.equals("") && !transaccion.equalsIgnoreCase("todos"))
			sb.append(" and INFI_TB_204_ORDENES.TRANSA_ID = '").append(transaccion).append("'");
		sb.append(filtroCliente);
		sb.append(" and INFI_TB_204_ORDENES.ORDENE_PED_FE_VALOR between to_date('").append(fechaDesde);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
		sb.append(" order by INFI_TB_201_CTES.CLIENT_NOMBRE,INFI_TB_204_ORDENES.ORDENE_PED_FE_VALOR");
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listarTransaccionesLiquidadasExcel "+ sb);
	}

	/**
	 * Metodo que lista el pago de cheque para Exportar a Excel
	 * 
	 * @param cliente
	 * @param numeroCheque
	 * @param fechaDesde
	 * @param fechaHasta
	 * @throws Exception
	 */
	public void listarPagoChequeExcel(String cliente, String numeroCheque, String fechaDesde, String fechaHasta) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb
				.append("SELECT  INFI_TB_201_CTES.CLIENT_NOMBRE,INFI_TB_201_CTES.CLIENT_CEDRIF, INFI_TB_207_ORDENES_OPERACION.FECHA_APLICAR, INFI_TB_207_ORDENES_OPERACION.FECHA_PAGO_CHEQUE, INFI_TB_207_ORDENES_OPERACION.CHEQUE_NUMERO, INFI_TB_207_ORDENES_OPERACION.MONEDA_ID,infi_tb_207_ordenes_operacion.monto_operacion as total FROM INFI_TB_204_ORDENES LEFT JOIN INFI_TB_207_ORDENES_OPERACION on INFI_TB_204_ORDENES.ORDENE_ID =INFI_TB_207_ORDENES_OPERACION.ORDENE_ID LEFT JOIN INFI_TB_201_CTES ON INFI_TB_204_ORDENES.CLIENT_ID=   INFI_TB_201_CTES.CLIENT_ID WHERE INFI_TB_207_ORDENES_OPERACION.FECHA_PAGO_CHEQUE IS NOT NULL ");
		if (cliente != null && !cliente.equals("") && !cliente.equalsIgnoreCase("todos"))
			sb.append(" and INFI_TB_201_CTES.client_id = ").append(cliente);
		if (numeroCheque != null && !numeroCheque.equals(""))
			sb.append(" and INFI_TB_207_ORDENES_OPERACION.CHEQUE_NUMERO= '").append(numeroCheque).append("'");
		sb.append(" and INFI_TB_207_ORDENES_OPERACION.FECHA_PAGO_CHEQUE between to_date('").append(fechaDesde);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')and to_date('").append(fechaHasta);
		sb.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
		sb.append(" GROUP BY  INFI_TB_201_CTES.CLIENT_NOMBRE,INFI_TB_201_CTES.CLIENT_CEDRIF,INFI_TB_207_ORDENES_OPERACION.FECHA_APLICAR,INFI_TB_207_ORDENES_OPERACION.FECHA_PAGO_CHEQUE,INFI_TB_207_ORDENES_OPERACION.CHEQUE_NUMERO,INFI_TB_207_ORDENES_OPERACION.MONEDA_ID,infi_tb_207_ordenes_operacion.monto_operacion");
		sb.append(" ORDER BY INFI_TB_207_ORDENES_OPERACION.FECHA_PAGO_CHEQUE, INFI_TB_207_ORDENES_OPERACION.CHEQUE_NUMERO asc");
		System.out.println("listarPagoChequeExcel "+sb);
		dataSet = db.get(dataSource, sb.toString());

	}

	/**
	 * Lista los titulos en custodia de un cliente para el posterior calculo de los intereses acumulados hasta la fecha actual
	 * 
	 * @param idCliente
	 * @throws Exception
	 */
	public void listarInteresAcumuladoCustodia(long idCliente) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT tc.*, to_char(tc.titulo_fe_ult_pago_cupon, '").append(ConstantesGenerales.FORMATO_FECHA).append("') as fe_ult_pago_cupon, t.titulo_moneda_neg, t.basis, c.client_nombre, t.couprate_8 as tasa_interes, '0.0' as interes_acumulado, ");
		sql.append(" (select intstrtdte from SECS where trim(secid) = trim(tc.titulo_id) and trunc(intstrtdte) = tc.titulo_fe_ult_pago_cupon) as fecha_prox_pago_cupon ");
		sql.append(" FROM infi_tb_701_titulos tc, infi_tb_100_titulos t, infi_tb_201_ctes c WHERE tc.client_id = ").append(idCliente);
		sql.append(" and trim(tc.titulo_id) = trim(t.titulo_id) ");
		sql.append(" and tc.client_id = c.client_id ");
		System.out.println("listarInteresAcumuladoCustodia "+sql);
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Actualiza la fecha de último pago de cupón para un cliente y un título específico. Si el cupón pagado es el último la posición en custodia del cliente es establecida a 0
	 * 
	 * @param fecha
	 *            fecha que se desea actualizar
	 * @param titulo
	 *            id del título que se desea actualizar
	 * @param clienteId
	 *            id del cliente para la actualización del título
	 * @param ultimoPago
	 *            indica si es el último pago del cupón
	 * @return String retorna la consulta SQL que se desea ejecutar
	 * @throws Exception
	 */
	public String actualizarFechaUltPagCupon(Date fecha, String titulo, long clienteId, boolean ultimoPago) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_701_TITULOS set titulo_fe_ult_pago_cupon=");
		sql.append(this.formatearFechaBD(fecha));
		sql.append(", titulo_fe_ult_amortizacion = ").append(this.formatearFechaBD(fecha)).append(" ");
		if (ultimoPago) {
			sql.append(", titcus_cantidad=0 ");
		}
		sql.append(" where titulo_id=").append("'").append(titulo).append("' and client_id=");
		sql.append(clienteId);
		System.out.println("actualizarFechaUltPagCupon "+sql);
		return sql.toString();
	}

	/**
 	* Procedimiento para obtener la cantidad bloqueada de un determinado título a 
	 * una fecha indicada y de un determinado tipo de bloqueo. 
	 * Esta función sólo trabaja con Transaccion, lo que quiere decir que no hace uso del db de la fundación.
	 * Para invocarla es necesario instanciar la clase con Transaccion.
	 * @param client_id id del cliente
	 * @param titulo_id id del título
	 * @param tipoProductoId tipo de producto
	 * @param fecha fecha de búsqueda
	 * @param tipoBloqueo tipo de bloqueo que se desea consultar
	 * @return cantidad cantidad bloqueada encontrada a la fecha dada
	 * @throws Exception en caso de error
	 */
	private BigDecimal cantidadBloqueadaPrivada(long client_id, String titulo_id, String tipoProductoId, Date fecha, String tipoBloqueo) throws Exception{
		BigDecimal cantidadBloqueada = null;		
		StringBuilder sql = new StringBuilder("select nvl(sum(titcus_cantidad),0) titcus_cantidad from infi_tb_705_titulos_bloq_hist where fecha = ( " +
				" select max(fecha) from infi_tb_705_titulos_bloq_hist " +
				" where client_id=" + client_id + " and titulo_id='" + titulo_id + "' and trunc(fecha) <= to_date('" + Utilitario.DateToString(fecha, ConstantesGenerales.FORMATO_FECHA2) + "', '" + ConstantesGenerales.FORMATO_FECHA2 + "') " +
				" and tipo_producto_id='" + tipoProductoId + "' )" + 
				" and client_id=" + client_id + " and titulo_id='" + titulo_id + "' and tipo_producto_id='" + tipoProductoId + "' ");
		
		if (tipoBloqueo != null){
			sql.append(" and tipblo_id=" + tipoBloqueo);
		}
		try {
			this.statement = this.conn.createStatement();
			this.resultSet = this.statement.executeQuery(sql.toString());
			if (this.resultSet.next()){
				cantidadBloqueada = new BigDecimal(this.resultSet.getLong("titcus_cantidad"));
			}else{
				cantidadBloqueada = new BigDecimal(0);
			}
		} catch (Exception e) {			
			throw e;
		}finally{
			if (this.resultSet != null){
				this.resultSet.close();
			}
			if (this.statement != null){
				this.statement.close();
			}
		}
		System.out.println("cantidadBloqueadaPrivada "+sql);
		return cantidadBloqueada;
	}

 	 /** Procedimiento para obtener la cantidad bloqueada de un determinado título a 
	 * una fecha indicada y de un determinado tipo de bloqueo. 
	 * Esta función sólo trabaja con Transaccion, lo que quiere decir que no hace uso del db de la fundación.
	 * Para invocarla es necesario instanciar la clase con Transaccion.
	 * @param client_id id del cliente
	 * @param titulo_id id del título
	 * @param tipoProductoId tipo de producto
	 * @param fecha fecha de búsqueda
	 * @return cantidad cantidad bloqueada encontrada a la fecha dada
	 * @throws Exception en caso de error
	 */	
	public BigDecimal cantidadBloqueada(long client_id, String titulo_id, String tipoProductoId, Date fecha) throws Exception{
		return cantidadBloqueadaPrivada(client_id, titulo_id, tipoProductoId, fecha,null);
	}

	/** Procedimiento para obtener la cantidad bloqueada de un determinado título a 
	 * una fecha indicada y de un determinado tipo de bloqueo. 
	 * Esta función sólo trabaja con Transaccion, lo que quiere decir que no hace uso del db de la fundación.
	 * Para invocarla es necesario instanciar la clase con Transaccion.
	 * @param client_id id del cliente
	 * @param titulo_id id del título
	 * @param tipoProductoId tipo de producto
	 * @param fecha fecha de búsqueda
	 * @param tipoBloqueo tipo de bloqueo que se desea consultar
	 * @return cantidad cantidad bloqueada encontrada a la fecha dada
	 * @throws Exception en caso de error
	 */
	public BigDecimal cantidadBloqueada(long client_id, String titulo_id, String tipoProductoId, Date fecha, String tipoBloqueo) throws Exception{
		return cantidadBloqueadaPrivada(client_id, titulo_id, tipoProductoId, fecha, tipoBloqueo);
	}

	/**
	 * Método que busca la posición global de un cliente a una fecha determinada.
	 * Esta función sólo trabaja con Transaccion, lo que quiere decir que NO hace uso del db de la fundación.
	 * @param client_id id del cliente
	 * @param titulo_id id del título a consultar
	 * @param tipoProductoId tipo de producto a buscar
	 * @param fecha fecha de consulta
	 * @return cantidad encontrada para el cliente
	 */
	public BigDecimal posicionAFecha(long client_id, String titulo_id, String tipoProductoId, Date fecha) throws Exception{
		BigDecimal cantidad = null;
		StringBuilder sql = new StringBuilder(" select nvl(sum(a.titcus_cantidad),0) titcus_cantidad from infi_tb_702_titulos_cierre a," +
				" (" +
				"   select client_id,titulo_id,tipo_producto_id,max(fecha_cierre) fecha_cierre " +
				"    from infi_tb_702_titulos_cierre " +
				"      where trunc(fecha_cierre) <= to_date('" + Utilitario.DateToString(fecha, ConstantesGenerales.FORMATO_FECHA2) + "', '" + ConstantesGenerales.FORMATO_FECHA2 + "')" +
		        "    group by client_id,titulo_id,tipo_producto_id ) b " +
		        " where a.client_id = b.client_id and a.titulo_id = b.titulo_id and " +
		        " a.tipo_producto_id = b.tipo_producto_id and " +
		        " a.fecha_cierre = b.fecha_cierre and a.client_id=" + client_id + 
		        " and a.titulo_id='" + titulo_id + "' and a.tipo_producto_id='" + tipoProductoId + "'");
		try {
			this.statement = this.conn.createStatement();
			this.resultSet = this.statement.executeQuery(sql.toString());
			System.out.println("posicionAFecha "+sql);
			if (this.resultSet.next()){
				cantidad = new BigDecimal(this.resultSet.getLong("titcus_cantidad"));
			}else{
				cantidad = new BigDecimal(0);
			}
		} catch (Exception e) {			
			throw e;
		}finally{
			if (this.resultSet != null){
				this.resultSet.close();
			}
			if (this.statement != null){
				this.statement.close();
			}
		}
		return cantidad;
	}
	
	/**
	 * Lista los movimientos presentados por los títulos y clientes en custodia.
	 * Esta función sólo trabaja con Transaccion, lo que quiere decir que NO hace uso del db de la fundación.
	 * @param idCliente id del cliente
	 * @param idTitulo id del título
	 * @param idTransaccion id de la transacción que se desea consultar
	 * @param fechaDesde fecha de inicio de la consulta
	 * @param fechaHasta fecha fin de la consulta
	 * @param tipoProductoId tipo de producto
	 * @return un resultset con el resultado de la consulta
	 * @throws Exception en caso de error
	 */
	public ResultSet listarMovimientosCustodia(long idCliente, String idTitulo,String idTransaccion, Date fechaDesde, Date fechaHasta, String tipoProductoId,String status) throws Exception{
		//Se debe usar la clase CustodiaDetalle e incorporarle CustodiaBloqueo
		String fechaD = Utilitario.DateToString(fechaDesde, ConstantesGenerales.FORMATO_FECHA);
		String fechaH = Utilitario.DateToString(fechaHasta, ConstantesGenerales.FORMATO_FECHA);
		
//		Date fecha1 = fechaHasta;
//		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
//		String fecha_funcion = formatter.format(fecha1);
//		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;


		/*int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);*/
		//if(fecha_sistema>=fecha_reconversion){
	/*	String sqlInicialC = "SELECT " +
			" ord.ordsta_id, ord.tipo_producto_id, " +
			" ord.ordene_ped_fe_orden fecha, ordene_fecha_liquidacion, ord.ordene_id, " +
			" ord.transa_id, ord.client_id, cl.client_nombre, (cl.tipper_id || cl.client_cedrif) cedularif, ot.titulo_id,fvj8ocon0(ot.titulo_unidades,'" + fecha_funcion + "') titulo_unidades,ot.titulo_pct_recompra FROM " +
			" INFI_TB_204_ORDENES ord, INFI_TB_201_CTES cl, " +
			" INFI_TB_206_ORDENES_TITULOS ot WHERE " +
			" ord.client_id = cl.client_id AND ord.ordene_id = ot.ordene_id AND ord.transa_id not in ('" + TransaccionNegocio.CANCELACION_ORDEN + "') ";*/
		String sqlInicialC = "SELECT " +
		" ord.ordsta_id, ord.tipo_producto_id, " +
		" ord.ordene_ped_fe_orden fecha, ordene_fecha_liquidacion, ord.ordene_id, " +
		" ord.transa_id, ord.client_id, cl.client_nombre, (cl.tipper_id || cl.client_cedrif) cedularif, ot.titulo_id, ot.titulo_unidades,ot.titulo_pct_recompra FROM " +
		" INFI_TB_204_ORDENES ord, INFI_TB_201_CTES cl, " +
		" INFI_TB_206_ORDENES_TITULOS ot WHERE " +
		" ord.client_id = cl.client_id AND ord.ordene_id = ot.ordene_id AND ord.transa_id not in ('" + TransaccionNegocio.CANCELACION_ORDEN + "') ";
	
		
		String sqlInicial = "SELECT " +
		" FUNC_INFI_CANT_BLOQUEADA_TIPO ( cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion,ord.ordene_ped_fe_orden),'dd-MM-yyyy')," + TipoBloqueos.BLOQUEO_POR_PAGO + ") BLOQUEO_POR_PAGO, " +
		" FUNC_INFI_CANT_BLOQUEADA_TIPO ( cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion,ord.ordene_ped_fe_orden),'dd-MM-yyyy')," + TipoBloqueos.BLOQUEO_POR_RECOMPRA + ") BLOQUEO_POR_RECOMPRA, " +
		" FUNC_INFI_POSICION_A_FECHA(cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion-1,ord.ordene_ped_fe_orden-1),'dd-MM-yyyy')) posicionMenosUno," +
		" FUNC_INFI_POSICION_A_FECHA(cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion,ord.ordene_ped_fe_orden),'dd-MM-yyyy')) posicion, " +
		" ord.ordsta_id, ord.tipo_producto_id, " +
	" ord.ordene_ped_fe_orden fecha, ord.ordene_fecha_liquidacion, ord.ordene_id, " +
	//" ord.transa_id, cl.client_nombre, (cl.tipper_id || cl.client_cedrif) cedularif, ot.titulo_id,fvj8ocon0(ot.titulo_unidades,'" + fecha_funcion + "') titulo_unidades,ot.titulo_pct_recompra,DECODE(transa_id,'PAGO_CUPONES',OP.monto_operacion,0)  AS monto_intereses FROM " +
	" ord.transa_id, cl.client_nombre, (cl.tipper_id || cl.client_cedrif) cedularif, ot.titulo_id, ot.titulo_unidades,ot.titulo_pct_recompra,DECODE(transa_id,'PAGO_CUPONES',OP.monto_operacion,0)  AS monto_intereses FROM " +
	" INFI_TB_204_ORDENES ord, INFI_TB_201_CTES cl, " +
	" INFI_TB_206_ORDENES_TITULOS ot,INFI_TB_207_ORDENES_OPERACION op WHERE " +
	" ord.client_id = cl.client_id AND ord.ordene_id = ot.ordene_id AND OP.ORDENE_ID(+) = ord.ordene_id AND ord.transa_id not in ('" + TransaccionNegocio.CANCELACION_ORDEN + "') ";
		
		String sqlOrderBy = " ORDER BY fecha desc ";
		String sqlFechaOrden = " AND trunc(ord.ordene_ped_fe_orden) >= to_date('" + fechaD + "','" + ConstantesGenerales.FORMATO_FECHA + "') " +
					           " AND trunc(ord.ordene_ped_fe_orden) <= to_date('" + fechaH + "','" + ConstantesGenerales.FORMATO_FECHA + "') ";
		String sqlFechaLiquidacion = " AND trunc(ord.ordene_fecha_liquidacion) >= to_date('" + fechaD + "','" + ConstantesGenerales.FORMATO_FECHA + "') " +
        							 " AND trunc(ord.ordene_fecha_liquidacion) <= to_date('" + fechaH + "','" + ConstantesGenerales.FORMATO_FECHA + "') ";
		String sqlTransaccion = " AND ord.transa_id in (%idTransaccion%)";
		String sqlTipoProducto = " AND ord.tipo_producto_id='%tipoProductoId%'";
		String sqlIdCliente = " AND ord.client_id=%idCliente%";
		String sqlIdTitulo = " AND ot.titulo_id='%idTitulo%'";
		
		//System.out.println("SELECCIONADO ESTATUS TODOS ********** ");
		String sqlStatusOrdenReg=null;
		String sqlStatusOrdenLiq=null;
		
		
		if(status.equals("todos")){		
			 sqlStatusOrdenReg = " AND ORDSTA_ID IN('" + StatusOrden.REGISTRADA + "','"+ StatusOrden.PROCESADA +"') ";
			 sqlStatusOrdenLiq = " AND (ORDSTA_ID IN('" + StatusOrden.LIQUIDADA + "') AND ordene_adj_monto > 0) ";
		}else {
			sqlStatusOrdenReg=" AND ORDSTA_ID='"+status+"'";
			sqlStatusOrdenLiq=sqlStatusOrdenReg;	
		}
		
		
		
		String sqlTransaIdNotIn = " AND TRANSA_ID NOT IN('" + TransaccionNegocio.TOMA_DE_ORDEN + "') ";
		
		StringBuilder sqlFinal = new StringBuilder();
		
		//Para aquellas transacciones que no sean de toma de orden la fecha a consultar debe ser la de orden, mientras que para las de toma de orden
		//la fecha es la de liquidación, ya que es ahí cuando entra a custodia
		
		if (idTransaccion != null){
			if (idTransaccion.equals("ENTRADAS_A_CUSTODIA")){
				idTransaccion = "'" + TransaccionNegocio.ENTRADA_DE_TITULOS + "','" + TransaccionNegocio.CARGA_INICIAL + "'";
				sqlFinal.append(sqlInicial).append(sqlFechaOrden).append(sqlTransaIdNotIn).append(sqlStatusOrdenReg).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				
				//RESOLUCION INCIDENCIA DETECTADA EN CALIDAD OGD-766
				//idTransaccion = "'" + TransaccionNegocio.TOMA_DE_ORDEN + "'";				
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}
				if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}
				sqlFinal.append(" union ").append(sqlInicial).append(sqlFechaLiquidacion).append(sqlStatusOrdenLiq).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}		if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}
			}else if (idTransaccion.equals(TransaccionNegocio.TOMA_DE_ORDEN) || idTransaccion.equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)){
				//RESOLUCION INCIDENCIA DETECTADA EN CALIDAD OGD-766
				//idTransaccion = "'" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "'";
				idTransaccion = "'" + idTransaccion + "'";
				
				sqlFinal.append(sqlInicial).append(sqlFechaLiquidacion).append(sqlStatusOrdenLiq).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}
				if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}
			}else{
				idTransaccion = "'" + idTransaccion + "'";
				sqlFinal.append(sqlInicial).append(sqlFechaOrden).append(sqlTransaIdNotIn).append(sqlStatusOrdenReg).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}
				if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}				
			}
		}else{			
			sqlFinal.append(sqlInicial).append(sqlFechaOrden).append(sqlTransaIdNotIn).append(sqlStatusOrdenReg);
			if (tipoProductoId != null){
				sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
			}
			if(idCliente!=0){
				sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
			}
			
			if(idTitulo!= null && !idTitulo.equals("")){
				sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
			}
			sqlFinal.append(" union ").append(sqlInicial).append(sqlFechaLiquidacion).append(sqlStatusOrdenLiq);
			if (tipoProductoId != null){
				sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
			}
			if(idCliente!=0){
				sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
			}
			
			if(idTitulo!= null && !idTitulo.equals("")){
				sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
			}			
		}
		
		
		sqlFinal.append(sqlOrderBy);
		System.out.println("listarMovimientosCustodia: "+sqlFinal.toString()+" 1");
		try {
			this.statement = this.conn.createStatement();
			this.resultSet = this.statement.executeQuery(sqlFinal.toString());
		} catch (Exception e) {			
			throw e;
		}
		
		System.out.println("SqlFinal"+sqlFinal+" 1");
		System.out.println("SqlFinal1 "+resultSet);
		return this.resultSet;
		/*}else{
			String sqlInicialC = "SELECT " +
			" ord.ordsta_id, ord.tipo_producto_id, " +
			" ord.ordene_ped_fe_orden fecha, ordene_fecha_liquidacion, ord.ordene_id, " +
			" ord.transa_id, ord.client_id, cl.client_nombre, (cl.tipper_id || cl.client_cedrif) cedularif, ot.titulo_id, ot.titulo_unidades,ot.titulo_pct_recompra FROM " +
			" INFI_TB_204_ORDENES ord, INFI_TB_201_CTES cl, " +
			" INFI_TB_206_ORDENES_TITULOS ot WHERE " +
			" ord.client_id = cl.client_id AND ord.ordene_id = ot.ordene_id AND ord.transa_id not in ('" + TransaccionNegocio.CANCELACION_ORDEN + "') ";
		
		String sqlInicial = "SELECT " +
		" FUNC_INFI_CANT_BLOQUEADA_TIPO ( cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion,ord.ordene_ped_fe_orden),'dd-MM-yyyy')," + TipoBloqueos.BLOQUEO_POR_PAGO + ") BLOQUEO_POR_PAGO, " +
		" FUNC_INFI_CANT_BLOQUEADA_TIPO ( cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion,ord.ordene_ped_fe_orden),'dd-MM-yyyy')," + TipoBloqueos.BLOQUEO_POR_RECOMPRA + ") BLOQUEO_POR_RECOMPRA, " +
		" FUNC_INFI_POSICION_A_FECHA(cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion-1,ord.ordene_ped_fe_orden-1),'dd-MM-yyyy')) posicionMenosUno," +
		" FUNC_INFI_POSICION_A_FECHA(cl.client_id, ot.titulo_id, ord.tipo_producto_id, to_char(decode(ord.transa_id,'TOMA_ORDEN',ordene_fecha_liquidacion,ord.ordene_ped_fe_orden),'dd-MM-yyyy')) posicion, " +
		" ord.ordsta_id, ord.tipo_producto_id, " +
	" ord.ordene_ped_fe_orden fecha, ord.ordene_fecha_liquidacion, ord.ordene_id, " +
	" ord.transa_id, cl.client_nombre, (cl.tipper_id || cl.client_cedrif) cedularif, ot.titulo_id, ot.titulo_unidades,ot.titulo_pct_recompra,DECODE(transa_id,'PAGO_CUPONES',OP.monto_operacion,0)  AS monto_intereses FROM " +
	" INFI_TB_204_ORDENES ord, INFI_TB_201_CTES cl, " +
	" INFI_TB_206_ORDENES_TITULOS ot,INFI_TB_207_ORDENES_OPERACION op WHERE " +
	" ord.client_id = cl.client_id AND ord.ordene_id = ot.ordene_id AND OP.ORDENE_ID(+) = ord.ordene_id AND ord.transa_id not in ('" + TransaccionNegocio.CANCELACION_ORDEN + "') ";
		
		String sqlOrderBy = " ORDER BY fecha desc ";
		String sqlFechaOrden = " AND trunc(ord.ordene_ped_fe_orden) >= to_date('" + fechaD + "','" + ConstantesGenerales.FORMATO_FECHA + "') " +
					           " AND trunc(ord.ordene_ped_fe_orden) <= to_date('" + fechaH + "','" + ConstantesGenerales.FORMATO_FECHA + "') ";
		String sqlFechaLiquidacion = " AND trunc(ord.ordene_fecha_liquidacion) >= to_date('" + fechaD + "','" + ConstantesGenerales.FORMATO_FECHA + "') " +
        							 " AND trunc(ord.ordene_fecha_liquidacion) <= to_date('" + fechaH + "','" + ConstantesGenerales.FORMATO_FECHA + "') ";
		String sqlTransaccion = " AND ord.transa_id in (%idTransaccion%)";
		String sqlTipoProducto = " AND ord.tipo_producto_id='%tipoProductoId%'";
		String sqlIdCliente = " AND ord.client_id=%idCliente%";
		String sqlIdTitulo = " AND ot.titulo_id='%idTitulo%'";
		
		//System.out.println("SELECCIONADO ESTATUS TODOS ********** ");
		String sqlStatusOrdenReg=null;
		String sqlStatusOrdenLiq=null;
		
		
		if(status.equals("todos")){		
			 sqlStatusOrdenReg = " AND ORDSTA_ID IN('" + StatusOrden.REGISTRADA + "','"+ StatusOrden.PROCESADA +"') ";
			 sqlStatusOrdenLiq = " AND (ORDSTA_ID IN('" + StatusOrden.LIQUIDADA + "') AND ordene_adj_monto > 0) ";
		}else {
			sqlStatusOrdenReg=" AND ORDSTA_ID='"+status+"'";
			sqlStatusOrdenLiq=sqlStatusOrdenReg;	
		}
		
		
		
		String sqlTransaIdNotIn = " AND TRANSA_ID NOT IN('" + TransaccionNegocio.TOMA_DE_ORDEN + "') ";
		
		StringBuilder sqlFinal = new StringBuilder();
		
		//Para aquellas transacciones que no sean de toma de orden la fecha a consultar debe ser la de orden, mientras que para las de toma de orden
		//la fecha es la de liquidación, ya que es ahí cuando entra a custodia
		
		if (idTransaccion != null){
			if (idTransaccion.equals("ENTRADAS_A_CUSTODIA")){
				idTransaccion = "'" + TransaccionNegocio.ENTRADA_DE_TITULOS + "','" + TransaccionNegocio.CARGA_INICIAL + "'";
				sqlFinal.append(sqlInicial).append(sqlFechaOrden).append(sqlTransaIdNotIn).append(sqlStatusOrdenReg).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				
				//RESOLUCION INCIDENCIA DETECTADA EN CALIDAD OGD-766
				//idTransaccion = "'" + TransaccionNegocio.TOMA_DE_ORDEN + "'";				
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}
				if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}
				sqlFinal.append(" union ").append(sqlInicial).append(sqlFechaLiquidacion).append(sqlStatusOrdenLiq).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}		if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}
			}else if (idTransaccion.equals(TransaccionNegocio.TOMA_DE_ORDEN) || idTransaccion.equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)){
				//RESOLUCION INCIDENCIA DETECTADA EN CALIDAD OGD-766
				//idTransaccion = "'" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "'";
				idTransaccion = "'" + idTransaccion + "'";
				
				sqlFinal.append(sqlInicial).append(sqlFechaLiquidacion).append(sqlStatusOrdenLiq).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}
				if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}
			}else{
				idTransaccion = "'" + idTransaccion + "'";
				sqlFinal.append(sqlInicial).append(sqlFechaOrden).append(sqlTransaIdNotIn).append(sqlStatusOrdenReg).append(sqlTransaccion.replaceAll("%idTransaccion%", idTransaccion));
				if (tipoProductoId != null){
					sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
				}
				if(idCliente!=0){
					sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
				}
				
				if(idTitulo!= null && !idTitulo.equals("")){
					sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
				}				
			}
		}else{			
			sqlFinal.append(sqlInicial).append(sqlFechaOrden).append(sqlTransaIdNotIn).append(sqlStatusOrdenReg);
			if (tipoProductoId != null){
				sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
			}
			if(idCliente!=0){
				sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
			}
			
			if(idTitulo!= null && !idTitulo.equals("")){
				sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
			}
			sqlFinal.append(" union ").append(sqlInicial).append(sqlFechaLiquidacion).append(sqlStatusOrdenLiq);
			if (tipoProductoId != null){
				sqlFinal.append(sqlTipoProducto.replaceAll("%tipoProductoId%", tipoProductoId));
			}
			if(idCliente!=0){
				sqlFinal.append(sqlIdCliente.replaceAll("%idCliente%", String.valueOf(idCliente)));
			}
			
			if(idTitulo!= null && !idTitulo.equals("")){
				sqlFinal.append(sqlIdTitulo.replaceAll("%idTitulo%", String.valueOf(idTitulo)));
			}*/			
	/*	}
		
		
		sqlFinal.append(sqlOrderBy);
		System.out.println("listarMovimientosCustodia: "+sqlFinal.toString());
		try {
			this.statement = this.conn.createStatement();
			this.resultSet = this.statement.executeQuery(sqlFinal.toString());
		} catch (Exception e) {			
			throw e;
		}
		
		System.out.println("SqlFinal"+sqlFinal+" 2");
		System.out.println("SqlFinal1 "+resultSet);
		return this.resultSet;	
		}*/
	}
	//NM26659_17/02/2016 Modificacion metodo para inclusion de ISIN en la consulta
	public String consultaCertificadoCustodia(String idCliente, String idTitulo,String fechaHasta, boolean monedaLocal) throws ParseException{
		StringBuilder sql = new StringBuilder("");
	    SimpleDateFormat formato_fecha = new SimpleDateFormat("dd-MM-yyyy");
	    String fecha_reme = fechaHasta;
	    Date fecha_remediacion = null;
	    fecha_remediacion = formato_fecha.parse(fecha_reme);
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    String fecha_funcion = formatter.format(fecha_remediacion);
	    
	    int fecha_sistema = Integer.parseInt(fecha_funcion);
	    System.out.println(fecha_reme);
	    System.out.println(fecha_remediacion);
	    System.out.println(fecha_funcion);
	    
	    String filtroTitulo = "";
	    if ((idTitulo != null) && (idTitulo != "")) {
	      filtroTitulo = "AND tt.titulo_id='" + idTitulo + "'";
	    }
	    sql.append("SELECT titulos.titulo_id,client_id, t100.titulo_fe_emision, t100.titulo_fe_vencimiento,");
	    sql.append("case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),t100.titulo_moneda_neg) else DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),t100.titulo_moneda_neg) end titulo_moneda_neg, estados, ");
	    if (monedaLocal) {
	      sql.append("(tasa_cambio * titcus_cantidad)  monto");
	    } else {
	      sql.append("case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(fecha, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(SUM(titcus_cantidad),FVJ8ODIV0('BFE_FECHA_VIGENCIA')),SUM(titcus_cantidad)) else SUM(titcus_cantidad) end monto,");
	    }
	    sql.append(" secaltid as ISIN ");
	    sql.append(" FROM ");
	    sql.append("\t(SELECT tt.client_id, tt.titulo_id, tt.titcus_cantidad,tt.fecha AS fecha, tip.tipblo_descripcion AS estados,tt.tasa_cambio, tt.tipo_producto_id");
	    sql.append("\tFROM infi_tb_705_titulos_bloq_hist tt,infi_tb_700_tipo_bloqueo tip,");
	    sql.append("         (SELECT   tipo_producto_id, client_id, t.titulo_id,MAX (t.fecha) AS fecha, t.tipblo_id");
	    sql.append("         FROM infi_tb_705_titulos_bloq_hist t");
	    sql.append("         WHERE TRUNC (t.fecha) <=TO_DATE ('" + fechaHasta + "', 'dd-MM-yyyy') AND t.client_id = " + idCliente);
	    sql.append("         GROUP BY client_id, titulo_id, t.tipblo_id,tipo_producto_id) tfecha");
	    sql.append("\tWHERE tt.tipblo_id = tip.tipblo_id");
	    sql.append("\tAND tt.tipo_producto_id = tfecha.tipo_producto_id");
	    sql.append("\tAND tt.tipblo_id = tfecha.tipblo_id");
	    sql.append("\tAND tt.titcus_cantidad IS NOT NULL");
	    sql.append("\tAND tt.titcus_cantidad > 0 " + filtroTitulo);
	    sql.append("\tAND tt.fecha = tfecha.fecha");
	    sql.append("\tAND tt.titulo_id = tfecha.titulo_id");
	    sql.append("\tAND tt.client_id = tfecha.client_id");
	    sql.append(" UNION");
	    sql.append(" \t(SELECT tt.client_id, tt.titulo_id, tt.titcus_cantidad-func_infi_cantidad_bloqueada (tt.client_id,tt.titulo_id,tt.tipo_producto_id,'" + fechaHasta + "') titcus_cantidad,");
	    sql.append(" \ttt.fecha_cierre AS fecha, 'DISPONIBLE' AS estados,tt.tasa_cambio, tt.tipo_producto_id");
	    sql.append("\tFROM infi_tb_702_titulos_cierre tt,");
	    sql.append("         (SELECT   tipo_producto_id, client_id, t.titulo_id,MAX (t.fecha_cierre) fecha_cierre");
	    sql.append("         FROM infi_tb_702_titulos_cierre t");
	    sql.append("         WHERE TRUNC (t.fecha_cierre) <=TO_DATE ('" + fechaHasta + "', 'dd-MM-yyyy') AND t.client_id = " + idCliente);
	    sql.append("         GROUP BY client_id, titulo_id, tipo_producto_id) tfecha");
	    sql.append("\tWHERE tt.tipo_producto_id = tfecha.tipo_producto_id");
	    sql.append("\tAND tt.fecha_cierre = tfecha.fecha_cierre");
	    sql.append("\tAND tt.titcus_cantidad IS NOT NULL");
	    sql.append("\tAND tt.titcus_cantidad > 0 " + filtroTitulo);
	    sql.append("\tAND tt.titulo_id = tfecha.titulo_id");
	    sql.append("\tAND tt.client_id = tfecha.client_id) ");
	    sql.append("\t) titulos,");
	    sql.append(" infi_tb_100_titulos t100");
	    sql.append(", asid ");
	    sql.append(" WHERE t100.titulo_id = titulos.titulo_id");
	    
	    sql.append(" AND trim(titulos.titulo_id)  =  trim(ASID.SECID(+)) AND  'ISIN' = ASID.SECIDTYPE (+)");
	    
	    sql.append(" AND titulos.titcus_cantidad > 0");
	    sql.append(" group by titulos.titulo_id,client_id,t100.titulo_fe_emision,t100.titulo_fe_vencimiento,t100.titulo_moneda_neg,estados, secaltid,fecha ");
	    sql.append(" ORDER BY titulo_id");
	    
	    System.out.println("consultaCertificadoCustodia: QUERY " + sql.toString() + " 1");
	    return sql.toString();
//		StringBuilder sql = new StringBuilder("");
//		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
//		String fecha_reme = fechaHasta; //nm3363
//		Date fecha_remediacion = null;
//		fecha_remediacion = formato_fecha.parse(fecha_reme);
//		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
//		String fecha_funcion = formatter.format(fecha_remediacion);
//		//String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
//
//
//		//int fecha_reconversion= Integer.parseInt(fecha_re);
//		int fecha_sistema= Integer.parseInt(fecha_funcion);
//		System.out.println(fecha_reme);
//		System.out.println(fecha_remediacion);
//		System.out.println(fecha_funcion);
//		//System.out.println(fecha_sistema+" "+fecha_reconversion);
//		String filtroTitulo="";
//		//System.out.println("consultaCertificadoCustodia: "+fechaHasta);
//	//	if(fecha_sistema>=fecha_reconversion){
//			
//		if(idTitulo!=null&&idTitulo!=""){
//			filtroTitulo="AND tt.titulo_id='"+idTitulo+"'";
//		}
//		//POSICION HISTORICA
//		sql.append("SELECT titulos.titulo_id,client_id, t100.titulo_fe_emision, t100.titulo_fe_vencimiento,");//TO_DATE ((titulos.fecha), 'dd-MM-RRRR') AS fecha,
//		sql.append("case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),t100.titulo_moneda_neg) else DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),t100.titulo_moneda_neg) end titulo_moneda_neg, estados, ");//tipo_producto_id,");
//		if(monedaLocal){
//			sql.append("(tasa_cambio * titcus_cantidad)  monto");
//		}else{
//			sql.append("SUM(titcus_cantidad) as monto,");
//		}
//		
//		//POSICION HISTORICA
//		sql.append("SELECT titulos.titulo_id,client_id, t100.titulo_fe_emision, t100.titulo_fe_vencimiento,");//TO_DATE ((titulos.fecha), 'dd-MM-RRRR') AS fecha,
//		sql.append("case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),t100.titulo_moneda_neg) else DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(t100.titulo_moneda_neg,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),t100.titulo_moneda_neg) end titulo_moneda_neg, estados, ");//tipo_producto_id,");
//		if(monedaLocal){
//			sql.append("(tasa_cambio * titcus_cantidad)  monto");
//		}else{
//			sql.append("SUM(titcus_cantidad) AS monto,");
//		}			
//		sql.append(" secaltid as ISIN ");		//Inclusion consulta de Isin NM26659_17/0/2016
//		sql.append(" FROM "); 
//		sql.append("	(SELECT tt.client_id, tt.titulo_id, tt.titcus_cantidad,tt.fecha AS fecha, tip.tipblo_descripcion AS estados,tt.tasa_cambio, tt.tipo_producto_id");
//		sql.append("	FROM infi_tb_705_titulos_bloq_hist tt,infi_tb_700_tipo_bloqueo tip,");
//		sql.append("         (SELECT   tipo_producto_id, client_id, t.titulo_id,MAX (t.fecha) AS fecha, t.tipblo_id");
//		sql.append("         FROM infi_tb_705_titulos_bloq_hist t");
//		sql.append("         WHERE TRUNC (t.fecha) <=TO_DATE ('"+fechaHasta+"', 'dd-MM-yyyy') AND t.client_id = "+idCliente);
//		sql.append("         GROUP BY client_id, titulo_id, t.tipblo_id,tipo_producto_id) tfecha");
//		sql.append("	WHERE tt.tipblo_id = tip.tipblo_id");
//		sql.append("	AND tt.tipo_producto_id = tfecha.tipo_producto_id");
//		sql.append("	AND tt.tipblo_id = tfecha.tipblo_id");
//		sql.append("	AND tt.titcus_cantidad IS NOT NULL");
//		sql.append("	AND tt.titcus_cantidad > 0 "+filtroTitulo+"");
//		sql.append("	AND tt.fecha = tfecha.fecha");
//		sql.append("	AND tt.titulo_id = tfecha.titulo_id");
//		sql.append("	AND tt.client_id = tfecha.client_id");
//		sql.append(" UNION");
//		sql.append(" 	(SELECT tt.client_id, tt.titulo_id, tt.titcus_cantidad-func_infi_cantidad_bloqueada (tt.client_id,tt.titulo_id,tt.tipo_producto_id,'"+fechaHasta+"') titcus_cantidad,");
//		sql.append(" 	tt.fecha_cierre AS fecha, 'DISPONIBLE' AS estados,tt.tasa_cambio, tt.tipo_producto_id");
//		sql.append("	FROM infi_tb_702_titulos_cierre tt,");
//		sql.append("         (SELECT   tipo_producto_id, client_id, t.titulo_id,MAX (t.fecha_cierre) fecha_cierre");
//		sql.append("         FROM infi_tb_702_titulos_cierre t");
//		sql.append("         WHERE TRUNC (t.fecha_cierre) <=TO_DATE ('"+fechaHasta+"', 'dd-MM-yyyy') AND t.client_id = "+idCliente);
//		sql.append("         GROUP BY client_id, titulo_id, tipo_producto_id) tfecha");
//		sql.append("	WHERE tt.tipo_producto_id = tfecha.tipo_producto_id");
//		sql.append("	AND tt.fecha_cierre = tfecha.fecha_cierre");
//		sql.append("	AND tt.titcus_cantidad IS NOT NULL");
//		sql.append("	AND tt.titcus_cantidad > 0 "+filtroTitulo+"");
//		sql.append("	AND tt.titulo_id = tfecha.titulo_id");
//		sql.append("	AND tt.client_id = tfecha.client_id) "); 
//		sql.append("	) titulos,");
//		sql.append(" infi_tb_100_titulos t100");
//		sql.append(", asid ");		//Inclusion consulta de Isin NM26659_17/0/2016
//		sql.append(" WHERE t100.titulo_id = titulos.titulo_id");		
//
//		sql.append(" AND trim(titulos.titulo_id)  =  trim(ASID.SECID(+)) AND  'ISIN' = ASID.SECIDTYPE (+)");		//Inclusion consulta de Isin NM26659_17/02/2016		
//
//		sql.append(" AND titulos.titcus_cantidad > 0"); // NM25287	12/02/2016 ITS-2983
//		sql.append(" group by titulos.titulo_id,client_id,t100.titulo_fe_emision,t100.titulo_fe_vencimiento,t100.titulo_moneda_neg,estados, secaltid "); //NM26659 Modificacion de consulta para sumar los registros por Estado de la posicion 10/03/2016
//		sql.append(" ORDER BY titulo_id");//, fecha");
//		
//		System.out.println("consultaCertificadoCustodia: QUERY "+sql.toString()+" 1");
//		return sql.toString();
		
	}	
	
}