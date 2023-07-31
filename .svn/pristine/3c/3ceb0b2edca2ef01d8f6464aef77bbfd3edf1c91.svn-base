package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.data.InstrumentoFinanciero;
import com.bdv.infi.data.TipoBloqueo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import megasoft.db;

/** 
 * Clase para el manejo de los tipos de bloqueo en la tabla INFI_TB_700_TIPO_BLOQUEO
 */
public class TipoBloqueoDAO extends com.bdv.infi.dao.GenericoDAO {

	public TipoBloqueoDAO(DataSource ds) throws Exception {
		super(ds);
	}	
	
	public TipoBloqueoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Lista los tipos de bloqueos configurados en la tabla y los almacena en un DataSet
	 * @throws Exception 
	*/
	public void listar() throws Exception{
		
		String sql = "SELECT * FROM INFI_TB_700_TIPO_BLOQUEO order by tipblo_id";
		
		dataSet = db.get(dataSource, sql);
		System.out.println("listar "+sql);
	}
	
	/**
	 * Lista por status los tipos de bloqueos configurados en la tabla y los almacena en un DataSet
	 * @throws Exception 
	*/
	public void listarPorStatus(String status) throws Exception{	
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT tipblo_id, tipblo_descripcion, case when tipblo_status=1 then 'Activo' when tipblo_status=0 then 'Inactivo' end tipblo_status FROM INFI_TB_700_TIPO_BLOQUEO where 1=1");
		if(status!=null){
			filtro.append(" and tipblo_status=").append(status);
		}
		sql.append(filtro);
		sql.append(" order by tipblo_id");		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista por id del Tipo Bloqueo los tipos de bloqueos configurados en la tabla y los almacena en un DataSet
	 * @throws Exception 
	*/
	public void listarPorId(String idBloqueo) throws Exception{	
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT tipblo_id, tipblo_descripcion, tipblo_status FROM INFI_TB_700_TIPO_BLOQUEO");
		sql.append(" where tipblo_id='").append(idBloqueo).append("'");
		
		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**
	 * Modifica el registro de una tabla 
	*/
	public String modificar(TipoBloqueo tipoBloqueo){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("update INFI_TB_700_TIPO_BLOQUEO set ");
		
		filtro.append(" tipblo_descripcion='").append(tipoBloqueo.getDescripcion().toUpperCase()).append("',");
		filtro.append(" tipblo_status=").append(tipoBloqueo.getStatus()).append("");
		filtro.append(" where tipblo_id='").append(tipoBloqueo.getTipo()).append("'");
		sql.append(filtro);		
		return(sql.toString());
	}


	/**
	 * Inserta el registro en la tabla  
	 */
	public String insertar(TipoBloqueo tipoBloqueo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_700_TIPO_BLOQUEO (tipblo_id, tipblo_descripcion, tipblo_status) values (");
		String idTipoBloqueo = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_TIPO_BLOQUEO);
		sql.append("'").append(idTipoBloqueo).append("',");
		sql.append("'").append(tipoBloqueo.getDescripcion().toUpperCase()).append("',");
		sql.append(tipoBloqueo.getStatus()).append(")");
		return(sql.toString());
		
	}
	
	/**
	 * Elimina el registro en la tabla. 
	*/
	public String eliminar(TipoBloqueo tipoBloqueo){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("delete from INFI_TB_700_TIPO_BLOQUEO where");
		
		filtro.append(" tipblo_id='").append(tipoBloqueo.getTipo()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
		

	/**Verifica si el Id esta siendo referenciado por otra tabla
	 */
	public void verificar(TipoBloqueo tipoBloqueo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select tipblo_id from INFI_TB_704_TITULOS_BLOQUEO where");
		sql.append(" tipblo_id='").append(tipoBloqueo.getTipo()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Metodo que verifica si existe un registro con la misma descripcion del tipo de bloqueo en base de datos
	*  @param String valorCampo
	*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
	*/
	public  boolean verificarDescripcionBloqueoExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_700_TIPO_BLOQUEO");
		sb.append(" where initCap(");
		sb.append("TIPBLO_DESCRIPCION");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
	
}
