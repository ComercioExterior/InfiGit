package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.data.TipoBloqueo;
import com.bdv.infi.data.TipoGarantia;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import megasoft.db;

/** 
 * Clase para el manejo de los tipos de garant&iacute;a en la tabla INFI_TB_xxx_TIPO_GARANTIA
 */
public class TipoGarantiaDAO extends com.bdv.infi.dao.GenericoDAO {
	/**
	 * Constructor.
	 * @param ds
	 * @throws Exception
	 */
	public TipoGarantiaDAO(DataSource ds) throws Exception {
		super(ds);
	}	
	
	/**
	 * Constructor.
	 * @param transaccion
	 * @throws Exception
	 */
	public TipoGarantiaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Elimina el registro en la tabla. 
	*/
	public void eliminar(){
	
	}

	/**
	 * Lista los tipos de garant&iacute;a configurados en la tabla  y los almacena en un DataSet
	 * @throws Exception 
	*/
	public void listar() throws Exception{
		String sql = "SELECT * FROM INFI_TB_022_TIPO_GARANTIA order by tipgar_id";
		
		dataSet = db.get(dataSource, sql);

	}
	
	/**
	 * Lista por status los Tipos de Garantia configurados en la tabla y los almacena en un DataSet
	 * @throws Exception 
	 */
	public void listarPorStatus(String status) throws Exception{	
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT tipgar_id, tipgar_descripcion, case when tipgar_status=1 then 'Activo' when tipgar_status=0 then 'Inactivo' end tipgar_status FROM INFI_TB_022_TIPO_GARANTIA where 1=1");
		if(status!=null){
			filtro.append(" and tipgar_status=").append(status);
		}
		sql.append(filtro);
		sql.append(" order by tipgar_status");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista por id del Tipo Garantia los tipos de bloqueos configurados en la tabla y los almacena en un DataSet
	 * @throws Exception 
	 */
	public void listarPorId(String idGarantia) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT tipgar_id, tipgar_descripcion, tipgar_status FROM INFI_TB_022_TIPO_GARANTIA");
		sql.append(" where tipgar_id='").append(idGarantia).append("'");
		
		dataSet =db.get(dataSource,sql.toString());
	}

	/**
	 * Modifica el registro de una tabla 
	*/
	public String modificar(TipoGarantia tipoGarantia){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("update INFI_TB_022_TIPO_GARANTIA set ");
		
		filtro.append(" tipgar_descripcion='").append(tipoGarantia.getDescripcion().toUpperCase()).append("',");
		filtro.append(" tipgar_status=").append(tipoGarantia.getStatus());
		filtro.append(" where tipgar_id='").append(tipoGarantia.getTipo()).append("'");
		sql.append(filtro);		
		return(sql.toString());
	}

	/**
	 * Inserta el registro en la tabla  
	 */
	public String insertar(TipoGarantia tipoGarantia) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_022_TIPO_GARANTIA (tipgar_id, tipgar_descripcion, tipgar_status)values (");
		String idTipoBloqueo = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_TIPO_GARANTIA);
		
		sql.append("'").append(idTipoBloqueo).append("',");
		sql.append("'").append(tipoGarantia.getDescripcion().toUpperCase()).append("',");
		sql.append(tipoGarantia.getStatus()).append(")");
		
		return(sql.toString());
	}
	
	/**
	 * Elimina el registro en la tabla. 
	*/
	public String eliminar(TipoGarantia tipoGarantia){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("delete from INFI_TB_022_TIPO_GARANTIA where");
		
		filtro.append(" tipgar_id='").append(tipoGarantia.getTipo()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/**Verifica si el Id esta siendo referenciado por otra tabla
	 */
	public void verificar(TipoGarantia tipoGarantia) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select tipgar_id from INFI_TB_704_TITULOS_BLOQUEO where");
		sql.append(" tipgar_id='").append(tipoGarantia.getTipo()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}


	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Metodo que verifica si existe un registro con la misma descripcion del tipo de Garantia en base de datos
	*  @param String valorCampo
	*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
	*/
	public  boolean verificarDescripcionTipoGarantiaExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_022_TIPO_GARANTIA");
		sb.append(" where initCap(");
		sb.append("TIPGAR_DESCRIPCION");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
	

}
