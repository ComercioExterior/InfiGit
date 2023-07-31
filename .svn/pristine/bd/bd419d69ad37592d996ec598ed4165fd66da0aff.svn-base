package com.bdv.infi.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.data.TituloBloqueo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoBloqueos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;


/** 
 * Clase que contiene la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y b&uacute;squeda de los datos de la data t&iacute;tulos (TB_100_TITULOS)
 */
public class TitulosBloqueoDAO extends GenericoDAO {

	public TitulosBloqueoDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}
	
	public TitulosBloqueoDAO(DataSource ds) {
		super(ds);
	}

	public Object moveNext() throws Exception {
		return null;
	}
	
	
	/**
	 * Busca todos los titulos bloqueados para un cliente en particular.
	 * @param idCliente id del cliente que posee el t&iacute;tulo
	 * @throws Exception en caso de error
	 */
	public void listarTitulosBloqueados(long idCliente) throws Exception{
		listarTitulosBloqueadosDc(idCliente,null,null);
	}
	
	/**
     * Busca todos los titulos bloqueados para un cliente en particular.	
	 * @param idCliente id del cliente
	 * @param idTitulo id del título que se desea consultar
	 * @param idTipoProducto id del tipo de producto
	 * @throws Exception en caso de error
	 */
	public void listarTitulosBloqueados(long idCliente, String idTitulo, String idTipoProducto) throws Exception{
		listarTitulosBloqueadosDc(idCliente,idTitulo,idTipoProducto);
	}

	/**
	 * Busca todos los titulos bloqueados para un cliente en particular.
	 * @param idCliente id del cliente
	 * @param idTitulo id del título que se desea consultar
	 * @param idTipoProducto id del tipo de producto
	 * @throws Exception en caso de error
	 */
	private void listarTitulosBloqueadosDc(long idCliente, String idTitulo, String idTipoProducto) throws Exception{
		StringBuffer sql = new StringBuffer();		
		sql.append("select a.titulo_id,a.tipo_producto_id,b.titcus_cantidad cantidad_bloqueada," +
				"a.titcus_cantidad cantidad_custodia," +
				"b.numero_garantia,c.ccy titulo_moneda_den, b.tipblo_id, d.tipblo_descripcion," +
				"e.beneficiario_id,e.beneficiario_nombre from INFI_TB_701_TITULOS a, INFI_TB_704_TITULOS_BLOQUEO b," +
				"secm c,infi_tb_700_tipo_bloqueo d,infi_tb_039_beneficiarios e" +
				" WHERE a.titulo_id = b.titulo_id(+) and a.tipo_producto_id = b.tipo_producto_id(+) " +
				"and trim(a.titulo_id) = trim(c.secid) and b.tipblo_id = d.tipblo_id " +
				"and b.beneficiario_id = e.beneficiario_id and a.client_id = b.client_id ");
		
		if (idCliente > 0){
			sql.append(" and a.client_id=" + idCliente);
		}
		
		if (idTitulo != null && !idTitulo.equals("")){
			sql.append(" and a.titulo_id='" + idTitulo + "'");
		}
		
		if (idTipoProducto != null && !idTipoProducto.equals("")){
			sql.append(" and a.tipo_producto_id='" + idTipoProducto + "'");
		}
		System.out.println("listarTitulosBloqueadosDc: "+sql.toString());
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
		sql.append("select a.titulo_id,a.tipo_producto_id,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(a.titulo_fe_ingreso_custodia, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(b.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),b.titcus_cantidad) else b.titcus_cantidad end cantidad_bloqueada," +
				"case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(a.titulo_fe_ingreso_custodia, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA')then DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(a.titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),a.titcus_cantidad) else a.titcus_cantidad end cantidad_custodia," +
				"b.numero_garantia,case when '" + fecha_sistema + "' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),c.ccy) else DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),DECODE(c.ccy,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),c.ccy) end titulo_moneda_den, b.tipblo_id, d.tipblo_descripcion," +
				"e.beneficiario_id,e.beneficiario_nombre from INFI_TB_701_TITULOS a, INFI_TB_704_TITULOS_BLOQUEO b," +
				"secm c,infi_tb_700_tipo_bloqueo d,infi_tb_039_beneficiarios e" +
				" WHERE a.titulo_id = b.titulo_id(+) and a.tipo_producto_id = b.tipo_producto_id(+) " +
				"and trim(a.titulo_id) = trim(c.secid) and b.tipblo_id = d.tipblo_id " +
				"and b.beneficiario_id = e.beneficiario_id and a.client_id = b.client_id ");
		
		if (idCliente > 0){
			sql.append(" and a.client_id=" + idCliente);
		}
		
		if (idTitulo != null && !idTitulo.equals("")){
			sql.append(" and a.titulo_id='" + idTitulo + "'");
		}
		
		if (idTipoProducto != null && !idTipoProducto.equals("")){
			sql.append(" and a.tipo_producto_id='" + idTipoProducto + "'");
		}
		System.out.println("listarTitulosBloqueadosDc: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	*/
	}
	
	/**
	 * Busca los datos de un título bloqueado para un cliente
	 * @param idTitulo id del título que se desea consultar
	 * @param idCliente id del cliente que se desea consultar
	 * @param tipoBloqueo tipo de bloqueo que se desea consultar
	 * @param tipoBeneficiario tipo de beneficiario que se desea consultar	 * 
	 * @return null en caso de no conseguir el título o el objeto en caso de conseguir el registro
	 * @throws Exception
	 */	
	public TituloBloqueo listarBloqueo(String idTitulo, long idCliente, String tipoBloqueo, int tipoBeneficiario, String idTipoProducto) throws Exception{
	    TituloBloqueo tituloBloqueo = new TituloBloqueo(); 
		StringBuffer sql = new StringBuffer();
		sql.append("select titulo_id,client_id,tipblo_id,beneficiario_id,titcus_cantidad,fecha,numero_garantia,tipo_producto_id from infi_tb_704_titulos_bloqueo");
		sql.append(" where titulo_id = '").append(idTitulo).append("'");
		sql.append(" and client_id = ").append(idCliente);
		sql.append(" and tipblo_id = '").append(tipoBloqueo).append("'");
		sql.append(" and beneficiario_id = ").append(tipoBeneficiario);
		sql.append(" and tipo_producto_id = '").append(idTipoProducto).append("'") ;
		
		try {
			conn = this.dataSource.getConnection();
			
			//if (statement == null){
				statement = conn.createStatement();
			//}
			resultSet = statement.executeQuery(sql.toString()); 
			
			if(resultSet.next()){
				tituloBloqueo.setBeneficiario(resultSet.getLong("beneficiario_id"));
				tituloBloqueo.setCliente(resultSet.getLong("client_id"));
				tituloBloqueo.setFechaBloqueo(resultSet.getDate("fecha"));
				tituloBloqueo.setNumeroGarantia(resultSet.getString("numero_garantia"));
				tituloBloqueo.setTipoBloqueo(resultSet.getString("tipblo_id"));
				tituloBloqueo.setTitulo(resultSet.getString("titulo_id"));
				tituloBloqueo.setTituloCustodiaCantidad(resultSet.getInt("titcus_cantidad"));
				tituloBloqueo.setTipoProducto(resultSet.getString("tipo_producto_id"));
			}
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
		
		return tituloBloqueo;		
	}
	

	/**
	 * Obtiene todos los datos de un bloqueo de t&iacute;tulo en particular
	 * @param idTitulo
	 * @param idCliente
	 * @param idTipoBloqueo
	 * @param idTipoGarantia
	 * @throws Exception
	 */
	public void obtenerBloqueo(String idTitulo, long idCliente, String idTipoBloqueo, String idBeneficiario, String idTipoProducto) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		//sql.append("select b.*, tipb.TIPBLO_DESCRIPCION, benef.BENEFICIARIO_NOMBRE ");
		sql.append("select b.TITULO_ID,b.TITCUS_CANTIDAD,b.TIPO_PRODUCTO_ID,b.TIPBLO_ID,b.NUMERO_GARANTIA,b.FECHA,b.CLIENT_ID,b.BENEFICIARIO_ID, tipb.TIPBLO_DESCRIPCION, benef.BENEFICIARIO_NOMBRE ");
		sql.append(" from infi_tb_704_titulos_bloqueo b, infi_tb_700_tipo_bloqueo tipb, INFI_TB_039_BENEFICIARIOS benef");
		sql.append(" where b.TITULO_ID = '").append(idTitulo).append("'");
		sql.append(" and b.CLIENT_ID = ").append(idCliente); 
		sql.append(" and b.TIPBLO_ID = tipb.TIPBLO_ID ");
		sql.append(" and b.TIPBLO_ID = '").append(idTipoBloqueo).append("'"); 
		sql.append(" and b.BENEFICIARIO_ID = benef.BENEFICIARIO_ID(+) ");
		sql.append(" and b.BENEFICIARIO_ID = '").append(idBeneficiario).append("'");
		sql.append(" and b.tipo_producto_id = '").append(idTipoProducto).append("'"); 
		System.out.println("obtenerBloqueo: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
		
		/*Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;


		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		StringBuffer sql = new StringBuffer();
		
		//sql.append("select b.*, tipb.TIPBLO_DESCRIPCION, benef.BENEFICIARIO_NOMBRE ");
		sql.append("select b.TITULO_ID,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(b.FECHA, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(c.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(b.TITCUS_CANTIDAD,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),b.TITCUS_CANTIDAD) else b.TITCUS_CANTIDAD end TITCUS_CANTIDAD,b.TIPO_PRODUCTO_ID,b.TIPBLO_ID,b.NUMERO_GARANTIA,b.FECHA,b.CLIENT_ID,b.BENEFICIARIO_ID, tipb.TIPBLO_DESCRIPCION, benef.BENEFICIARIO_NOMBRE ");
		sql.append(" from infi_tb_704_titulos_bloqueo b, infi_tb_700_tipo_bloqueo tipb, INFI_TB_039_BENEFICIARIOS benef,infi_tb_100_titulos c");
		sql.append(" where b.TITULO_ID = '").append(idTitulo).append("'");
		sql.append(" and b.CLIENT_ID = ").append(idCliente); 
		sql.append(" and b.TIPBLO_ID = tipb.TIPBLO_ID ");
		sql.append(" and b.TIPBLO_ID = '").append(idTipoBloqueo).append("'"); 
		sql.append(" and b.BENEFICIARIO_ID = benef.BENEFICIARIO_ID(+) ");
		sql.append(" and b.BENEFICIARIO_ID = '").append(idBeneficiario).append("'");
		sql.append(" and b.tipo_producto_id = '").append(idTipoProducto).append("'"); 
		System.out.println("obtenerBloqueo: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());*/
	}
	
	/**
	 * Inserta en la base de datos un nuevo registro de bloqueo de t&iacute;tulo, para un cliente determinado
	 * @param tituloBloqueo objeto con todos los datos del bloqueo
	 * @return String[] con la sentencia a ejecutar
	 * @throws Exception
	 */
	public String insertar(TituloBloqueo tituloBloqueo) throws Exception{
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("insert into INFI_TB_704_TITULOS_BLOQUEO (titulo_id, client_id, tipblo_id, beneficiario_id, titcus_cantidad, fecha, numero_garantia,tipo_producto_id)");
		sql.append("values (").append("'").append(tituloBloqueo.getTitulo()).append("',");
		sql.append(tituloBloqueo.getCliente()).append(",");
		sql.append("'").append(tituloBloqueo.getTipoBloqueo()).append("',");
		sql.append(tituloBloqueo.getBeneficiario()).append(",");
		sql.append(tituloBloqueo.getTituloCustodiaCantidad()).append(",");
		sql.append("SYSDATE,");
		if(tituloBloqueo.getNumeroGarantia()!=null){
			sql.append("'").append(tituloBloqueo.getNumeroGarantia()).append("',");
		}else{
			sql.append("NULL,");
		}
		sql.append("'"+tituloBloqueo.getTipoProducto()+"')");
		System.out.println("insertar TituloBloqueo---->"+sql);
		return(sql.toString());
	}	
	
	/**
	 * Actualiza un registro de bloqueo de t&iacute;tulo de un cliente en particular, luego de un desbloqueo.
	 * @param tituloBloqueo objeto con los datos del bloqueo
	 * @return String con sentencia sql
	 */
	public String modificar(TituloBloqueo tituloBloqueo){
		
		StringBuilder sql = new StringBuilder();				
		if(tituloBloqueo.getTituloCustodiaCantidad()>0){
			sql.append("UPDATE INFI_TB_704_TITULOS_BLOQUEO set ");
			sql.append(" titcus_cantidad = ").append(tituloBloqueo.getTituloCustodiaCantidad());
			sql.append(" WHERE tipblo_id = '").append(tituloBloqueo.getTipoBloqueo()).append("'");
			sql.append(" and beneficiario_id = ").append(tituloBloqueo.getBeneficiario());
			sql.append(" and client_id = ").append(tituloBloqueo.getCliente());
			sql.append(" and titulo_id = '").append(tituloBloqueo.getTitulo()).append("'");
			sql.append(" and tipo_producto_id = '").append(tituloBloqueo.getTipoProducto()).append("'");
		}
		System.out.println("modificar-->"+sql);
		return(sql.toString());
	}
	
	/**
	 * Actualiza un registro de bloqueo de t&iacute;tulo de un cliente en particular, luego de un desbloqueo.
	 * @param tituloBloqueo objeto con los datos del bloqueo
	 * @return String con sentencia sql
	 */
	public String modificar(TituloBloqueo tituloBloqueo, String nuevoTipoBloqueo){
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("UPDATE INFI_TB_704_TITULOS_BLOQUEO set ");
		if(tituloBloqueo.getTituloCustodiaCantidad()>0){
			sql.append(" titcus_cantidad = ").append(tituloBloqueo.getTituloCustodiaCantidad());
		}		
		if(!nuevoTipoBloqueo.equals(null) && !nuevoTipoBloqueo.equals("") && tituloBloqueo.getTituloCustodiaCantidad()>0){
			sql.append(" ,tipblo_id = ").append(nuevoTipoBloqueo);
		}		
		sql.append(" WHERE tipblo_id = '").append(tituloBloqueo.getTipoBloqueo()).append("'");
		sql.append(" and beneficiario_id = ").append(tituloBloqueo.getBeneficiario());
		sql.append(" and client_id = ").append(tituloBloqueo.getCliente());
		sql.append(" and titulo_id = '").append(tituloBloqueo.getTitulo()).append("'");
		sql.append(" and tipo_producto_id = '").append(tituloBloqueo.getTipoProducto()).append("'");

		return(sql.toString());
	}
	
	/**
	 * Borra un registro de bloqueo de t&iacute;tulo
	 * @param tituloBloqueo objeto con los datos del bloqueo
	 * @return String con sentencia sql
	 */

	public String delete(TituloBloqueo tituloBloqueo){
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("DELETE FROM INFI_TB_704_TITULOS_BLOQUEO");
		sql.append(" WHERE tipblo_id = '").append(tituloBloqueo.getTipoBloqueo()).append("'");
		sql.append(" and beneficiario_id = ").append(tituloBloqueo.getBeneficiario()).append("");
		sql.append(" and client_id = ").append(tituloBloqueo.getCliente());
		sql.append(" and titulo_id = '").append(tituloBloqueo.getTitulo()).append("'");
		sql.append(" and tipo_producto_id = '").append(tituloBloqueo.getTipoProducto()).append("'");
		System.out.println("delete-->"+sql);
		return(sql.toString());
	}
	
	
	/**
	 * Verifica si existe un registro de bloqueo con un tipo de garant&iacute;a, tipo de bloqueo, titulo y cliente en particular 
	 * retornando la cantidad bloqueada   
	 * @param tituloBloqueo
	 * @return int con la cantidad bloqueada, 0 en caso de no existir el bloqueo
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public int existeBloqueo(TituloBloqueo tituloBloqueo) throws NumberFormatException, Exception{
		
		DataSet _aux = null;
		StringBuffer sql = new StringBuffer();
		Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;


		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		
		sql.append("SELECT case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') and TO_CHAR(a.FECHA, 'YYYYMMDD') < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(b.titulo_moneda_den,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(titcus_cantidad,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),titcus_cantidad) else a.titcus_cantidad end titcus_cantidad FROM INFI_TB_704_TITULOS_BLOQUEO a,infi_tb_100_titulos b");
		sql.append(" WHERE tipblo_id = '").append(tituloBloqueo.getTipoBloqueo()).append("'");
		sql.append(" and beneficiario_id = ").append(tituloBloqueo.getBeneficiario()).append("");
		sql.append(" and client_id = ").append(tituloBloqueo.getCliente());
		sql.append(" and a.titulo_id = '").append(tituloBloqueo.getTitulo().toString()).append("'");
		sql.append(" and tipo_producto_id = '").append(tituloBloqueo.getTipoProducto()).append("'");

		_aux = db.get(dataSource, sql.toString());
		
		if(_aux.next())
			return Integer.parseInt(_aux.getValue("titcus_cantidad"));
		else
			System.out.println("existeBloqueo--->"+sql);
			return 0;
		
	}
}
