package com.bdv.infi.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/** 
 * Clase que contiene la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y b&uacute;squeda de los datos de la data t&iacute;tulos (TB_100_TITULOS)
 */
public class TitulosDAO extends GenericoDAO {

	
	private Logger logger = Logger.getLogger(TitulosDAO.class);
	
	
	public TitulosDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}
	
	public TitulosDAO(DataSource ds) {
		super(ds);
	}

	public Object moveNext() throws Exception {
		return null;
	}
	
	/**Modifica una orden especifica al recibir un archivo 
	 * @param idUnidadInversion
	 * @param status
	 * @param consultas
	 * @throws Exception
	 */
	public String modificarOrdenTitulos(OrdenTitulo ordenTitulo, Orden orden) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();	
		
		sqlSB.append("update INFI_TB_206_ORDENES_TITULOS set");
		sqlSB.append(" titulo_monto = ").append(ordenTitulo.getMonto()).append(", ");
		sqlSB.append(" titulo_unidades = ").append(ordenTitulo.getUnidades()).append(", ");
		sqlSB.append(" titulo_mto_int_caidos = ").append(ordenTitulo.getMontoIntCaidos()).append(", ");
		sqlSB.append(" titulo_mto_neteo = ").append(ordenTitulo.getMontoNeteo());
		sqlSB.append(" where ordene_id = ");
		sqlSB.append(orden.getIdOrden());
		sqlSB.append(" and titulo_id ='");
		sqlSB.append(ordenTitulo.getTituloId()).append("'");
		
		return(sqlSB.toString());
	}
	
	/**Lista todos los t&iacute;tulos encontrados en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarTitulos() throws Exception{
		String strSQL = "SELECT * FROM INFI_TB_100_TITULOS ORDER BY TITULO_ID";
		dataSet = db.get(dataSource, strSQL);
	}
	
/**
 * Lista las caracteristicas del titulo especificado
 * @param tituloId
 * @throws Exception
 */
	public void listarTitulos(String tituloId) throws Exception{
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT * FROM INFI_TB_100_TITULOS where 1=1 ");
		strSQL.append("and titulo_id='").append(tituloId).append("'");
		System.out.println("listarTitulos-->"+strSQL);
		dataSet = db.get(dataSource, strSQL.toString());
	}
	
	public void detallesTitulo(String idTitulo) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT to_char(titulo_fe_vencimiento, '" +ConstantesGenerales.FORMATO_FECHA+ "') as fecha_vencimiento, to_char(titulo_fe_emision, '" +ConstantesGenerales.FORMATO_FECHA+ "') as fecha_emision, A.*  FROM INFI_TB_100_TITULOS A WHERE titulo_id='").append(idTitulo).append("'");
		
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("detallesTitulo "+sb.toString());
	}
	/**Metodo que retorna un dataset con informacion de los titulos asociados a una orden
	 * el campo que titulo que deculeve el dataset debera ser SIEMPRE IGUAL al 
	 * campo titulo que retorna el dataset el metodo listarTitulosParaUnidadesInv ubicado en UICamposDinamicosDAO
	 * @param titulos Id del titulo que se desea buscar
	 */
	
	public void listaTitulosOrden(long idOrden) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select initcap(to_char(titulo_fe_vencimiento,'yyyy')) as anio, initcap(to_char(titulo_fe_vencimiento,'mm')) as mes, initcap(to_char(titulo_fe_vencimiento,'month')) as nombre_mes, initcap(to_char(titulo_fe_vencimiento,'dd')) as dia, ");
		sb.append(ConstantesGenerales.FORMATEO_ID_TITULO).append(" titulo,");
		sb.append("titu.*, t.basis, t.couprate_8, t.titulo_moneda_neg, ((round(t.titulo_fe_vencimiento,'DAY'))-(round(sysdate,'DAY'))) as diasal_vencimiento, ((round(t.titulo_fe_vencimiento,'DAY'))-(round(t.titulo_fe_emision,'DAY'))) as diasdel_plazo, ((round(o.ordene_ped_fe_orden,'DAY'))-(round(u.undinv_fe_emision,'DAY'))) as dias_interes_transcurridos, o.ORDENE_TASA_POOL, u.undinv_tasa_cambio, to_char(o.ordene_fecha_adjudicacion,'"+ConstantesGenerales.FORMATO_FECHA2+"') as ordene_fecha_adjudicacion, to_char(o.ordene_ped_fe_valor,'"+ConstantesGenerales.FORMATO_FECHA2+"') as ordene_ped_fe_valor,to_char(o.ordene_ped_fe_orden,'"+ConstantesGenerales.FORMATO_FECHA2+"') as ordene_ped_fe_orden, o.ordene_usr_sucursal,o.ordene_ped_precio,o.ordene_adj_monto,o.ordene_ped_monto, o.ordene_ped_total, t.titulo_valor_nominal,to_char(t.titulo_fe_vencimiento,'"+ConstantesGenerales.FORMATO_FECHA2+"') as titulo_fe_vencimiento, t.titulo_descripcion, o.transa_id from INFI_TB_206_ORDENES_TITULOS titu left join infi_tb_100_titulos t on trim(titu.TITULO_ID)=trim(t.TITULO_ID), INFI_TB_204_ORDENES o , INFI_TB_106_UNIDAD_INVERSION u where o.ORDENE_ID=titu.ORDENE_ID and o.uniinv_id=u.UNDINV_ID and o.ORDENE_ID=");
		sb.append(idOrden);
		dataSet = db.get(dataSource, sb.toString());
	}

	/**Metodo que lista el titulo asociado a la venta del titulo para ser usado n FuncionVentaTitulo 
	 * @param idOrden
	 * @throws Exception
	 */
	public void listaTituloVentaOrden(long idOrden) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select initcap(to_char(titulo_fe_vencimiento,'yyyy')) as anio, initcap(to_char(titulo_fe_vencimiento,'mm')) as mes, initcap(to_char(titulo_fe_vencimiento,'month')) as nombre_mes, initcap(to_char(titulo_fe_vencimiento,'dd')) as dia, ");
		sb.append(ConstantesGenerales.FORMATEO_ID_TITULO).append(" titulo,");
		sb.append("titu.*, t.basis, t.titulo_moneda_neg, o.ordene_tasa_cambio, o.ordene_ped_precio, t.titulo_valor_nominal,to_char(t.titulo_fe_vencimiento,'"+ConstantesGenerales.FORMATO_FECHA2+"') as titulo_fe_vencimiento, t.titulo_descripcion, op.MONEDA_ID, o.transa_id from INFI_TB_206_ORDENES_TITULOS titu left join infi_tb_100_titulos t on trim(titu.TITULO_ID)=trim(t.TITULO_ID), INFI_TB_204_ORDENES o , INFI_TB_207_ORDENES_OPERACION op where o.ORDENE_ID=titu.ORDENE_ID and op.ORDENE_ID=o.ORDENE_ID and o.ORDENE_ID=");
		sb.append(idOrden);
		dataSet = db.get(dataSource, sb.toString());
	}
	
	/** Metodo que lista los titulos existentes 
	 * para ser mostrados en el PICKLIST
	 * @param fechaVencHasta 
	 * @param fechaVencDesde 
	 * @param fechaEmHasta 
	 * @param fechaEmDesde */
	public void listar(String id, String descripcion, String idMoneda, Date fechaEmDesde, Date fechaEmHasta, Date fechaVencDesde, Date fechaVencHasta, String comision) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select titulo_id, titulo_descripcion, titulo_fe_emision, titulo_fe_vencimiento, titulo_moneda_den from INFI_TB_100_TITULOS where 1=1");
		
		if(id!=null ){
			filtro.append(" and upper(titulo_id) like upper('%").append(id).append("%')");
		}
		
		if(descripcion!=null){
			filtro.append(" and upper(titulo_descripcion) like upper('%").append(descripcion).append("%')");
		}
		
		if(idMoneda!=null && !idMoneda.equals("")){
			filtro.append(" and upper(titulo_moneda_den) like upper('%").append(idMoneda).append("%')"); 
		}
		
		if(fechaEmDesde!=null){
			filtro.append(" AND titulo_fe_emision >= ").append(formatearFechaBD(fechaEmDesde));
		}
		
		if(fechaEmHasta!=null){
			filtro.append(" AND titulo_fe_emision <= ").append(formatearFechaBD(fechaEmHasta));
		}
		
		if(fechaVencDesde!=null){
			filtro.append(" AND titulo_fe_vencimiento >=").append(formatearFechaBD(fechaVencDesde));
		}
		
		if(fechaVencHasta!=null){
			filtro.append(" AND titulo_fe_vencimiento <= ").append(formatearFechaBD(fechaVencHasta));
		}
		
		if(comision!=null ){
			filtro.append(" AND titulo_id not in (select titulo_id from INFI_TB_044_COM_TITULO where comision_id=").append(comision).append(") and titulo_id is not null");
		}
		
		sql.append(filtro);
		sql.append(" ORDER BY titulo_descripcion");
		dataSet = db.get(dataSource, sql.toString());

	}
	/** Metodo que lista los titulos existentes 
	 * para ser mostrados en el PICKLIST
	 * @param fechaVencHasta 
	 * @param fechaVencDesde 
	 * @param fechaEmHasta 
	 * @param fechaEmDesde */
	public void listarSiTienePrecio(String id, String descripcion, String idMoneda, Date fechaEmDesde, Date fechaEmHasta, Date fechaVencDesde, Date fechaVencHasta) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("(select t.titulo_id, t.titulo_descripcion, titulo_fe_emision, titulo_fe_vencimiento, titulo_moneda_den from INFI_TB_100_TITULOS t where 1=1");
		
		if(id!=null ){
			filtro.append(" and upper(titulo_id) like upper('%").append(id).append("%')");
		}
		if(descripcion!=null){
			filtro.append(" and upper(titulo_descripcion) like upper('%").append(descripcion).append("%')");
		}
		if(idMoneda!=null && !idMoneda.equals("")){
			filtro.append(" and upper(titulo_moneda_den) like upper('%").append(idMoneda).append("%')"); 
			
		}
		
		if(fechaEmDesde!=null)
			filtro.append(" AND titulo_fe_emision >= ").append(formatearFechaBD(fechaEmDesde));
			
		if(fechaEmHasta!=null)
			filtro.append(" AND titulo_fe_emision <= ").append(formatearFechaBD(fechaEmHasta));
					
		if(fechaVencDesde!=null)
			filtro.append(" AND titulo_fe_vencimiento >=").append(formatearFechaBD(fechaVencDesde));
			
		if(fechaVencHasta!=null)
			filtro.append(" AND titulo_fe_vencimiento <= ").append(formatearFechaBD(fechaVencHasta));


		sql.append(filtro);
		sql.append(" ) minus (select p.titulo_id, t.titulo_descripcion, t.titulo_fe_emision, t.titulo_fe_vencimiento, t.titulo_moneda_den from INFI_TB_118_TITULOS_PRECIOS p, INFI_TB_100_TITULOS t)");
		sql.append(" ORDER BY titulo_descripcion");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/** Metodo que lista los titulos existentes 
	 * para ser mostrados en el PICKLIST*/
	public void listarTitulosOrden(String ordenId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select INFI_TB_206_ORDENES_TITULOS.*,INFI_TB_204_ORDENES.TIPO_PRODUCTO_ID," +
				" infi_tb_100_titulos.TITULO_FE_EMISION,infi_tb_100_titulos.TITULO_MONEDA_DEN," +
				" infi_tb_100_titulos.TITULO_MONEDA_NEG,infi_tb_100_titulos.TITULO_DESCRIPCION " +
				" ,(((INFI_TB_206_ORDENES_TITULOS.TITULO_MONTO * (INFI_TB_206_ORDENES_TITULOS.TITULO_PCT_RECOMPRA/100)*(INFI_TB_206_ORDENES_TITULOS.TITULO_PCT/100))+INFI_TB_206_ORDENES_TITULOS.TITULO_MTO_INT_CAIDOS) ) AS valor_efectivo " + 
				" from INFI_TB_206_ORDENES_TITULOS, INFI_TB_100_TITULOS, INFI_TB_204_ORDENES " +
				" where INFI_TB_206_ORDENES_TITULOS.TITULO_ID=INFI_TB_100_TITULOS.TITULO_ID " +
				" and INFI_TB_204_ORDENES.ORDENE_ID = INFI_TB_206_ORDENES_TITULOS.ORDENE_ID and " +
				" INFI_TB_206_ORDENES_TITULOS.ORDENE_ID=" + ordenId);
		
		//System.out.println("QEURY: " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/** Metodo que lista los titulos a partir del id de una orden asociada al tipo de producto SUB_DIVISA
	* @param ordenId: Id de la orden a consultar los titulos
	*/
	public void listarTitulosFromOrdenSubDiv(String ordenId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select A.titulo_id, A.titulo_pct, A.titulo_monto, A.titulo_unidades, A.titulo_pct_recompra, A.titulo_precio_mercado, ");
		sql.append("A.titulo_mto_int_caidos, A.titulo_mto_neteo, B.TIPO_PRODUCTO_ID, C.UNDINV_FE_EMISION as TITULO_FE_EMISION, ");
		sql.append("B.MONEDA_ID as TITULO_MONEDA_DEN, B.MONEDA_ID as TITULO_MONEDA_NEG, A.TITULO_ID as TITULO_DESCRIPCION, ");
		sql.append("B.ordene_tasa_cambio, 0 AS valor_efectivo "); 
		sql.append("from  INFI_TB_206_ORDENES_TITULOS A, INFI_TB_204_ORDENES B, INFI_TB_106_UNIDAD_INVERSION C ");
		sql.append("where C.undinv_id = B.uniinv_id and B.ORDENE_ID = A.ORDENE_ID and A.ORDENE_ID =").append(ordenId);
		
		/*
		sql.append("select A.*, B.TIPO_PRODUCTO_ID, D.UNDINV_FE_EMISION as TITULO_FE_EMISION, ");
		sql.append("B.MONEDA_ID as TITULO_MONEDA_DEN, B.MONEDA_ID as TITULO_MONEDA_NEG, ");
		sql.append("A.TITULO_ID as TITULO_DESCRIPCION, B.ordene_tasa_cambio, ");
		sql.append("(((A.TITULO_MONTO * (A.TITULO_PCT_RECOMPRA/100)*(A.TITULO_PCT/100))+A.TITULO_MTO_INT_CAIDOS) ) AS valor_efectivo "); 
		sql.append("from (((INFI_TB_206_ORDENES_TITULOS A left join INFI_TB_204_ORDENES B on A.ORDENE_ID=B.ORDENE_ID) ");
		sql.append("left join INFI_TB_701_TITULOS C on B.client_id=C.client_id) ");
		sql.append("left join INFI_TB_106_UNIDAD_INVERSION D on B.UNIINV_ID = D.UNDINV_ID) ");
		sql.append("where A.ORDENE_ID = ").append(ordenId);
		
		/*sql.append("select INFI_TB_206_ORDENES_TITULOS.*, INFI_TB_204_ORDENES.TIPO_PRODUCTO_ID, ");
		sql.append("INFI_TB_106_UNIDAD_INVERSION.UNDINV_FE_EMISION as TITULO_FE_EMISION, ");
		sql.append("INFI_TB_204_ORDENES.MONEDA_ID as TITULO_MONEDA_DEN, ");
		sql.append("INFI_TB_204_ORDENES.MONEDA_ID as TITULO_MONEDA_NEG, ");
		sql.append("INFI_TB_701_TITULOS.TITULO_ID as TITULO_DESCRIPCION, INFI_TB_204_ORDENES.ordene_tasa_cambio, ");
		sql.append("(((INFI_TB_206_ORDENES_TITULOS.TITULO_MONTO * (INFI_TB_206_ORDENES_TITULOS.TITULO_PCT_RECOMPRA/100)*(INFI_TB_206_ORDENES_TITULOS.TITULO_PCT/100))+INFI_TB_206_ORDENES_TITULOS.TITULO_MTO_INT_CAIDOS) ) AS valor_efectivo ");
		sql.append("from INFI_TB_206_ORDENES_TITULOS, INFI_TB_204_ORDENES, INFI_TB_701_TITULOS, INFI_TB_106_UNIDAD_INVERSION where ");
		sql.append("INFI_TB_206_ORDENES_TITULOS.TITULO_ID=INFI_TB_701_TITULOS.TITULO_ID and ");
		sql.append("INFI_TB_204_ORDENES.ORDENE_ID = INFI_TB_206_ORDENES_TITULOS.ORDENE_ID and ");
		sql.append("INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID = INFI_TB_204_ORDENES.UNIINV_ID and ");
		sql.append("INFI_TB_206_ORDENES_TITULOS.ORDENE_ID=").append(ordenId);*/
		//logger.info("listaaaaaaaarTitulosFromOrdenSubDiv:\n"+sql);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	/** Metodo que lista los titulos asociados a una orden
	 * Muestra data completa de los titulos de las tablas 206, 204 y 100
	 *@param ordenId, id de la orden 
	 */
	public void listarOrdenTitulos(Long ordenId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select t.titulo_moneda_den,ot.*, o.ordene_ped_monto,o.ORDENE_TASA_POOL, u.undinv_tasa_cambio, to_char(o.ordene_fecha_adjudicacion,'"+ConstantesGenerales.FORMATO_FECHA2+"') as ordene_fecha_adjudicacion, to_char(o.ordene_ped_fe_valor,'"+ConstantesGenerales.FORMATO_FECHA2+"') as ordene_ped_fe_valor,to_char(o.ordene_ped_fe_orden,'"+ConstantesGenerales.FORMATO_FECHA2+"') as ordene_ped_fe_orden, o.ordene_usr_sucursal,o.ordene_ped_precio,o.ordene_adj_monto,o.ordene_ped_monto, o.ordene_ped_total, t.titulo_valor_nominal,to_char(t.titulo_fe_vencimiento,'"+ConstantesGenerales.FORMATO_FECHA2+"') as titulo_fe_vencimiento, t.titulo_descripcion, (select titulos_precio_pool from INFI_TB_120_TITULOS_PREC_RCMP pr where ot.titulo_id=pr.titulo_id and o.ordene_ped_monto between pr.titulo_rango_invertido_desde and pr.titulo_rango_invertido_hasta and o.tipo_producto_id = pr.tipo_producto_id) as precio_pool from INFI_TB_206_ORDENES_TITULOS ot inner join infi_tb_100_titulos t on trim(ot.TITULO_ID)=trim(t.TITULO_ID), INFI_TB_204_ORDENES o , INFI_TB_106_UNIDAD_INVERSION u where o.ORDENE_ID=ot.ORDENE_ID and o.uniinv_id=u.UNDINV_ID and o.ORDENE_ID=");
		sql.append(ordenId);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/** Metodo que lista los titulos asociados a una orden. Devuelve el sql que debe ejecutarse. El sql es al estilo preparedStatement siendo el valor de condición el id de la orden
	 * Muestra data completa de los titulos de las tablas 206, 204 y 100
	 *@param ordenId, id de la orden 
	 *@return el sql que debe ejecutarse
	 */
	public String listarOrdenTitulosSql() throws Exception{
		return "select ot.titulo_id,ot.titulo_monto,ot.titulo_unidades,ot.titulo_pct_recompra, o.ORDENE_TASA_POOL from INFI_TB_204_ORDENES o, INFI_TB_206_ORDENES_TITULOS ot WHERE o.ordene_id = ot.ordene_id and o.ORDENE_ID=?";
	}	
	
	/**
	 * Metodo para listar el ultimo mes de pago de comision
	 * @param String mes 
	 * @param String anno
	 * */
	public void listarUltimoMesCobro(String mes,String anno)throws Exception{
		String sql=" select to_char(titulo_fe_ult_cobro_comision,'MM')as mes "+
				   " from infi_tb_701_titulos "+
				   " where to_char(titulo_fe_ult_cobro_comision,'MM') <"+mes+
				   " and to_char(titulo_fe_ult_cobro_comision,'YYYY')>="+anno+ 
				   " and to_char(titulo_fe_ult_cobro_comision,'YYYY')<="+anno+
				   " and rownum=1";
		dataSet=db.get(dataSource,sql);
	}
	/**Lista la tasa activa actual para el momento de la consulta (Tasa Cupon)
	 * @return La tasa activa actual para el momento de la consulta en caso de no encontrarse en un rango del cronograma de pago, se toma la primera
	 * @param tituloId
	 * @throws Exception
	 */
	public String listarTasaActivaWS(String tituloId)throws Exception{
		String tasaActiva="";
		try {
			String sql="select intrate_8 from secs where secid='"+tituloId +"' and intstrtdte<sysdate and intenddte>sysdate";
			dataSet=db.get(dataSource,sql);
			/*
			 * Si trae registro es la tasa activa del periodo donde se encuentre el sysdate
			 * De lo contrario sera  la tasa de pago para la primera fecha en que comience
			 */
			if(dataSet.count()>0){
				dataSet.first();
				dataSet.next();
				tasaActiva = dataSet.getValue("intrate_8");
			}else{
				sql="select intrate_8 from secs where secid='"+tituloId+"' and rownum=1 order by intstrtdte asc ";
				dataSet=db.get(dataSource,sql);
				if(dataSet.count()>0){
					dataSet.first();
					dataSet.next();
					tasaActiva = dataSet.getValue("intrate_8");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));			
			tasaActiva="";
			return tasaActiva;
		}
		return tasaActiva;
	}
	
	/**
	 * Lista lso titulos de una orden que se encuentren pactados para recompra,con sus operaciones financieras
	 * @throws Exception
	 */
	public void titulosRecompra(long ordenId,boolean neteo)throws Exception{
		StringBuffer filtro = new StringBuffer();
		if(neteo){
			filtro.append(" and TITULO_MTO_NETEO>0");
		}else{
			filtro.append(" and TITULO_MTO_NETEO=0");
		}
		StringBuffer sb = new StringBuffer();
		
		sb.append("select ot.TITULO_ID,ot.TITULO_MONTO,ot.TITULO_PCT,ot.TITULO_PCT_RECOMPRA,ot.TITULO_UNIDADES,ot.TITULO_MTO_NETEO,ot.TITULO_MTO_INT_CAIDOS,ot.TITULO_PRECIO_MERCADO,t.TITULO_MONEDA_NEG");
		sb.append(" from infi_tb_206_ordenes_titulos ot,infi_tb_100_titulos t where t.TITULO_ID=ot.TITULO_ID and ot.ORDENE_ID=");
		sb.append(ordenId);	
		sb.append(" and ot.TITULO_PCT_RECOMPRA<>0").append(filtro);

		dataSet = db.get(dataSource, sb.toString());
	}
	
	/**
	 * Lista la tasa, moneda de negociación y el titulo involucrado en una orden ene specifico
	 * @param ordeneId
	 * @throws Exception
	 */
	/*public void listarDatosTitulos(long ordeneId)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select infi_tb_204_ordenes.ordene_tasa_cambio,infi_tb_100_titulos.titulo_id,infi_tb_100_titulos.titulo_moneda_neg from infi_tb_204_ordenes ");
		sql.append("left join INFI_TB_206_ORDENES_TITULOS on infi_tb_204_ordenes.ordene_id=INFI_TB_206_ORDENES_TITULOS.ordene_id ");
		sql.append("left join infi_tb_100_titulos on INFI_TB_206_ORDENES_TITULOS.titulo_id=infi_tb_100_titulos.titulo_id ");
		sql.append("where infi_tb_204_ordenes.ordene_id=").append(ordeneId);
		
		dataSet = db.get(dataSource,sql.toString());
	}*/
	
		/** Metodo que lista los datos de los titulos asociados a una orden
	 *  Es usado en la adjudicación*/
	public void listarDatosTituloOrden(String ordenId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ORD.TIPO_PRODUCTO_ID,T.TITULO_ID,T.TITULO_MONTO,T.TITULO_MTO_INT_CAIDOS,T.TITULO_MTO_NETEO,T.TITULO_PCT,T.TITULO_PRECIO_MERCADO,T.TITULO_PCT_RECOMPRA,T.TITULO_UNIDADES");
		sql.append(" FROM INFI_TB_204_ORDENES ORD, INFI_TB_206_ORDENES_TITULOS T WHERE ORD.ORDENE_ID = T.ORDENE_ID AND T.ORDENE_ID=");
		sql.append(ordenId);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Obtiene la moneda de denominación de un título
	 * @param idTitulo id del título a consultar
	 * @return moneda de negociación
	 * @throws Exception en caso de error
	 */
	public void obtenerMonedaNegociacion(String idTitulo) throws Exception{

		String sql = "select titulo_moneda_den from infi_tb_100_titulos where titulo_id='" + idTitulo + "'";
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("obtenerMonedaNegociacion "+sql);
		
	}
	
//	public void obtenerMonedaNegociacion1(String idTitulo, String fecha_inicio, String fecha_fin) throws Exception{
//			
//			String sql = "select case when '" + fecha_inicio + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA')  then  DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),titulo_moneda_den) else DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),titulo_moneda_den) end titulo_moneda_den from infi_tb_100_titulos where titulo_id='" + idTitulo + "'";//"select titulo_moneda_den from infi_tb_100_titulos where titulo_id='" + idTitulo + "'";
//			dataSet = db.get(dataSource, sql.toString());
//			System.out.println("obtenerMonedaNegociacion1 "+sql);
//			
//		
//		/*String sql = "select decode(titulo_moneda_den, FVJ8ODIV0('BFE_SWIFT'), FVJ8ODIV0('BFE_SWIFT_OLD'),titulo_moneda_den) titulo_moneda_den from infi_tb_100_titulos where titulo_id='" + idTitulo + "'";
//		dataSet = db.get(dataSource, sql.toString());
//		System.out.println("obtenerMonedaNegociacion "+sql);*/
//		
//	}
	
	
	
	/**
	 * Obtiene el nombre de título equivalente a como lo solicita BCV
	 * @param idTitulo id del título
	 * @throws Exception en caso de error
	 */
	public void listarTituloEquivalenciaBCV(String idTitulo) throws Exception{
		String sql = "SELECT trim(secid) titulo,trim(tabletext) nombre_bcv FROM secm,gent " +
				" where trim(secid)=trim(tablevalue) and tableid='IDENTBCV'	" +
				" and trim(secid)='" + idTitulo + "' order by secid";
		dataSet = db.get(dataSource, sql);
	}
	
	public String insertarTitulosOrden(long ordeneID,OrdenTitulo ordenTitulo) throws Exception{
	
		/*System.out.println("ORDEN_ID " + ordeneID + " NOT NULL");
		System.out.println("ordenTitulo.getPorcentaje() " + ordenTitulo.getPorcentaje());
		System.out.println("ordenTitulo.getMonto() "  + ordenTitulo.getMonto() + " NOT NULL ");
		System.out.println("ordenTitulo.getUnidades() " +ordenTitulo.getUnidades() + " NOT NULL");
		System.out.println("");*/
		
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO INFI_TB_206_ORDENES_TITULOS (");
		sql.append("ORDENE_ID,TITULO_ID,TITULO_PCT,TITULO_MONTO,TITULO_UNIDADES,TITULO_PCT_RECOMPRA, ");
		sql.append("TITULO_PRECIO_MERCADO,TITULO_MTO_INT_CAIDOS,TITULO_MTO_NETEO) VALUES (");
				
		sql.append(ordeneID);
		sql.append(",'");
		sql.append(ordenTitulo.getTituloId());
		sql.append("',");
		sql.append(Double.parseDouble(ordenTitulo.getPorcentaje()));
		sql.append(",");
		sql.append(ordenTitulo.getMonto());
		sql.append(",");
		sql.append(ordenTitulo.getUnidades());
		sql.append(",");
		sql.append(ordenTitulo.getPorcentajeRecompra());
		sql.append(",");
		sql.append(ordenTitulo.getPrecioMercado());
		sql.append(",");
		sql.append(ordenTitulo.getMontoIntCaidos());		
		sql.append(",");
		sql.append(ordenTitulo.getMontoNeteo());
		sql.append(")");
				
		//db.exec(dataSource, sql.toString());
		return sql.toString();
	}
	
	public String obtenerTasaLiquidacionOPICS(String idTitulo) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT TABLETEXT TASA FROM GENT");
		sql.append(" WHERE TABLEID='TASALIQUIDA' AND TRIM(TABLEVALUE) =");
		sql.append("(SELECT TRIM(PRODUCT)||'-'||PRODTYPE FROM SECM WHERE SECID='"+idTitulo+"')");
		db.exec(dataSource, sql.toString());
		
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			return  dataSet.getValue("TASA");
		}
		return null;
		
	}
	
	public void listarOrdenesVentaTitulos(long idOrden)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT OT.TITULO_ID AS titulo,UI.MONEDA_ID AS moneda_denominacion,ORD.ORDENE_ADJ_MONTO AS valor_nominal,");
		sql.append("OT.TITULO_PCT_RECOMPRA AS precio_venta,round(((((OT.TITULO_MONTO * (OT.TITULO_PCT_RECOMPRA / 100))   + OT.TITULO_MTO_INT_CAIDOS))*( OT.TITULO_PCT/100)),2) AS monto_efectivo ");
		//" ORD.ORDENE_PED_TOTAL AS  monto_efectivo ");
		sql.append("FROM INFI_TB_204_ORDENES ORD, INFI_TB_206_ORDENES_TITULOS OT,INFI_TB_106_UNIDAD_INVERSION UI ");
		sql.append("WHERE     ORD.ORDENE_ID = OT.ORDENE_ID AND UI.UNDINV_ID = ORD.UNIINV_ID AND ORD.ORDENE_ID =").append(idOrden);
		System.out.println("CONSULTA DE TITULOS PARA LA VENTA " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	public void listarVencimientoTituloPorUI(long unidadInversionID)throws Exception{
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT TO_CHAR(S.MDATE,'DD-MM-YYYY') AS MDATE,TO_CHAR (UI.UNDINV_FE_LIQUIDACION,'DD-MM-YYYY') AS UNDINV_FE_LIQUIDACION FROM INFI_TB_106_UNIDAD_INVERSION UI, INFI_TB_108_UI_TITULOS UT, SECM S ");
		sql.append("WHERE UT.UNDINV_ID = UI.UNDINV_ID AND TRIM(S.SECID) = TRIM(UT.TITULO_ID)		 ");
		sql.append("AND UI.UNDINV_ID=").append(unidadInversionID);
		
		dataSet = db.get(dataSource, sql.toString());		
		
	}
	
	public void listarCantidadTitulosPorUnudadInversionId(long unidadInversionID)throws Exception{		
		StringBuffer sql=new StringBuffer();		
		sql.append("SELECT COUNT(*) as TITULOS  FROM INFI_TB_106_UNIDAD_INVERSION UI,INFI_TB_108_UI_TITULOS UIT WHERE UIT.UNDINV_ID=UI.UNDINV_ID AND UI.UNDINV_ID='").append(unidadInversionID).append("'");	
		
		dataSet = db.get(dataSource, sql.toString());		
		
	}
	
	//NM29643 - INFI_TTS_443: Obtiene el ID de un Titulo a partir de su cod ISIN
	public void verifIsinTitulo(String isin)throws Exception{		
		StringBuffer sql=new StringBuffer();		
		sql.append("SELECT ASID.secaltid ISIN, SECM.SECID, SECM.MININCREMENT as incremental FROM SECM, ASID WHERE ASID.SECID=SECM.SECID ");
		sql.append("AND ASID.secidtype='ISIN' ");
		sql.append("AND ASID.SECALTID='").append(isin).append("'");
		
		dataSet = db.get(dataSource, sql.toString());		
		
	}
}