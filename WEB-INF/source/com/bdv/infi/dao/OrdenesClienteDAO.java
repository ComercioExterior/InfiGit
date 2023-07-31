/**
 * 
 */
package com.bdv.infi.dao;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.*;

/** 
 * Clase usada para la modificaci&oacute;n, inserci&oacute;n y listado de los t&iacute;tulos en custodia
 */
public class OrdenesClienteDAO extends com.bdv.infi.dao.GenericoDAO {

	public OrdenesClienteDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public OrdenesClienteDAO(DataSource ds) throws Exception {
		super(ds);
	}	
	
	
	public void listar1(long idCliente, String fecha_desde, String fecha_hasta, String transaccion) throws Exception{
		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);//nm36635
		String fecha_reme = fecha_hasta; //nm33635
		Date fecha_remediacion = null;
		fecha_remediacion = formato_fecha.parse(fecha_reme);
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha_remediacion);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_reme);
		System.out.println(fecha_remediacion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append(" SELECT transa_descripcion, case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(INFI_TB_106_UNIDAD_INVERSION.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(infi_tb_204_ordenes.ordene_ped_monto,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),infi_tb_204_ordenes.ordene_ped_monto) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then infi_tb_204_ordenes.ordene_ped_monto end ordene_ped_monto,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(INFI_TB_106_UNIDAD_INVERSION.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(infi_tb_204_ordenes.ordene_adj_monto,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),infi_tb_204_ordenes.ordene_adj_monto) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then infi_tb_204_ordenes.ordene_adj_monto end ordene_adj_monto,infi_tb_201_ctes.client_nombre, infi_tb_201_ctes.TIPPER_ID, infi_tb_201_ctes.CLIENT_CEDRIF, " );
		sql.append(" infi_tb_204_ordenes.ordene_ped_fe_orden,infi_tb_204_ordenes.ORDENE_ID,infi_tb_203_ordenes_status.ORDSTA_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_NOMBRE ");
		sql.append(" from infi_tb_201_ctes left join infi_tb_204_ordenes on infi_tb_201_ctes.client_id=infi_tb_204_ordenes.client_id ");
		sql.append(" left join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID ");
		sql.append(" left join infi_tb_203_ordenes_status on infi_tb_204_ordenes.ORDSTA_ID=infi_tb_203_ordenes_status.ORDSTA_ID ");
		sql.append(" left join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id where 1=1");
		if(idCliente!=0){
			filtro.append(" and infi_tb_204_ordenes.client_id=").append(idCliente);
		}
		if(fecha_desde!=null && fecha_hasta==null){
			filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden>='").append(fecha_desde).append("'");
		}
		if(fecha_hasta!=null && fecha_desde==null){
			filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden<='").append(fecha_desde).append("'");
		}
		if(fecha_hasta!=null && fecha_desde!=null){
			filtro.append(" and trunc(infi_tb_204_ordenes.ordene_ped_fe_orden) between to_date('").append(fecha_desde).append("','");
			filtro.append(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
			filtro.append("') and to_date('").append(fecha_hasta).append("','");
			filtro.append(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
			filtro.append("')");;
		}
		sql.append(filtro);
		
		//Busca sólo por la transacción seleccionada
		if (!transaccion.equals("-1") && !transaccion.equals("")){
			sql.append(" and infi_tb_204_ordenes.TRANSA_ID ='").append(transaccion).append("'");
		}
		
		sql.append(" and infi_tb_204_ordenes.ORDSTA_ID is not null ORDER BY infi_tb_204_ordenes.ordene_id desc");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listar "+sql);
	}
	public void listar(long idCliente, String fecha_desde, String fecha_hasta, String transaccion) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append(" SELECT transa_descripcion, infi_tb_204_ordenes.ordene_ped_monto,infi_tb_204_ordenes.ordene_adj_monto,infi_tb_201_ctes.client_nombre, infi_tb_201_ctes.TIPPER_ID, infi_tb_201_ctes.CLIENT_CEDRIF, " );
		sql.append(" infi_tb_204_ordenes.ordene_ped_fe_orden,infi_tb_204_ordenes.ORDENE_ID,infi_tb_203_ordenes_status.ORDSTA_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_NOMBRE ");
		sql.append(" from infi_tb_201_ctes left join infi_tb_204_ordenes on infi_tb_201_ctes.client_id=infi_tb_204_ordenes.client_id ");
		sql.append(" left join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID ");
		sql.append(" left join infi_tb_203_ordenes_status on infi_tb_204_ordenes.ORDSTA_ID=infi_tb_203_ordenes_status.ORDSTA_ID ");
		sql.append(" left join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id where 1=1");
		if(idCliente!=0){
			filtro.append(" and infi_tb_204_ordenes.client_id=").append(idCliente);
		}
		if(fecha_desde!=null && fecha_hasta==null){
			filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden>='").append(fecha_desde).append("'");
		}
		if(fecha_hasta!=null && fecha_desde==null){
			filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden<='").append(fecha_desde).append("'");
		}
		if(fecha_hasta!=null && fecha_desde!=null){
			filtro.append(" and trunc(infi_tb_204_ordenes.ordene_ped_fe_orden) between to_date('").append(fecha_desde).append("','");
			filtro.append(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
			filtro.append("') and to_date('").append(fecha_hasta).append("','");
			filtro.append(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
			filtro.append("')");;
		}
		sql.append(filtro);
		
		//Busca sólo por la transacción seleccionada
		if (!transaccion.equals("-1") && !transaccion.equals("")){
			sql.append(" and infi_tb_204_ordenes.TRANSA_ID ='").append(transaccion).append("'");
		}
		
		sql.append(" and infi_tb_204_ordenes.ORDSTA_ID is not null ORDER BY infi_tb_204_ordenes.ordene_id desc");
		dataSet = db.get(dataSource, sql.toString());
	}
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/**Crea un dataset con las fechas requeridas para el filtro de busqueda 
	 * Fecha actual y Fecha - 60 days
	 * */
	public DataSet mostrar_fechas_filter() throws Exception {
		
		Date date = new Date(); 
		DateFormat dateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA); 
		String fecha = dateFormat.format(date); //te devuelve la fecha en string con el formato dd/MM/yyyy
		Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
		calendar.add(Calendar.DATE, -60); //el -60 indica que se le restaran 60 dias
		String fecha_less60=dateFormat.format(calendar.getTime());
		//Se coloca la informacion en el dataset para ser retornado
		DataSet _fecha_serv=new DataSet();
		_fecha_serv.append("fecha_actual",java.sql.Types.VARCHAR);
		_fecha_serv.append("fecha_less60",java.sql.Types.VARCHAR);
		_fecha_serv.addNew();
		_fecha_serv.setValue("fecha_actual",fecha);
		_fecha_serv.setValue("fecha_less60",fecha_less60);
		
		return _fecha_serv;
	}
	/**Crea un dataset con las fechas requeridas para el filtro de busqueda 
	 * Fecha actual y Fecha - 60 days
	 * */
	public DataSet mostrarFechasFilterCupones() throws Exception {
		
		Date date = new Date(); 
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String fecha = dateFormat.format(date); //te devuelve la fecha en string con el formato dd/MM/yyyy
		Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
		calendar.add(Calendar.DATE, +7); //el +7 indica que se le suman 7 dias
		String fecha_7=dateFormat.format(calendar.getTime());
		//Se coloca la informacion en el dataset para ser retornado
		DataSet _fecha_serv=new DataSet();
		_fecha_serv.append("fecha_actual",java.sql.Types.VARCHAR);
		_fecha_serv.append("fecha_7",java.sql.Types.VARCHAR);
		_fecha_serv.addNew();
		_fecha_serv.setValue("fecha_actual",fecha);
		_fecha_serv.setValue("fecha_7",fecha_7);
		
		return _fecha_serv;
	}
	
	 
	
	//Verificar si se agrega restriccion por fecha tb
	/**Crea un dataset con los correos de los clientes con ordenes del producto subasta divisas adjudicadas
	 * */
	public void getCorreosCteOrden(String idUnidadInversion, String estatusOrden, String idProducto, int idEjecucion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select cte.CLIENT_CORREO_ELECTRONICO as correo, cte.CLIENT_ID as id, cte.CLIENT_NOMBRE as name, ord.ORDENE_ID ");
		sql.append("from INFI_TB_201_CTES cte, INFI_TB_204_ORDENES ord where ");
		sql.append("ord.TIPO_PRODUCTO_ID='").append(idProducto).append("' and ord.ORDSTA_ID='").append(estatusOrden).append("' ");
		sql.append("and ord.UNIINV_ID=").append(idUnidadInversion).append(" ");
		if(idEjecucion!=0){
			sql.append("and ord.EJECUCION_ID=").append(idEjecucion).append(" ");
		}
		sql.append("and ord.CLIENT_ID = cte.CLIENT_ID");
		dataSet = db.get(dataSource, sql.toString());
	}
	
}

