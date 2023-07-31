package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.data.TipoPersonaValidoDefinicion;

/** 
 * Clase que se conecta con la base de datos y busca los tipos de clientes registrados en configuraci&oacute;n. (V,E,J)
 */
public class TipoPersonaValidoDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public TipoPersonaValidoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public TipoPersonaValidoDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Lista los tipos de clientes encontrados en la base de datos (V,E,J) 
	 * @throws Exception 
	*/
	public void listarTodos() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_014_TIP_PERS_VALIDO");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista los tipos de clientes por el tipo seleccionado (V,E,J) 
	*/
	public void listarPorTipo(String tipo){
	
	}	
	
	/**
	 * Busca los tipos de personas validos en la base de datos por el id recibido.  
	 * @throws Exception 
	*/
	public void listar(String id_tppeva) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TPPEVA_ID, TPPEVA_DESCRIPCION FROM INFI_TB_014_TIP_PERS_VALIDO WHERE TPPEVA_ID ='").append(id_tppeva).append("'");
		dataSet = db.get(dataSource, sb.toString());
		
				
	}
	
	public String insertar(TipoPersonaValidoDefinicion tipoPersonaValidoDefinicion) throws Exception  {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
				
		sql.append("INSERT INTO INFI_TB_014_TIP_PERS_VALIDO ( TPPEVA_ID, TPPEVA_DESCRIPCION) VALUES (");
		
				
		filtro.append("'").append(tipoPersonaValidoDefinicion.getTppeva_id().toUpperCase()).append("',");	
		filtro.append("'").append(tipoPersonaValidoDefinicion.getTppeva_descripcion()).append("')");

		sql.append(filtro);		
		return(sql.toString());
	}

	public String modificar(TipoPersonaValidoDefinicion tipoPersonaValidoDefinicion) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
	    	
		
		sql.append("UPDATE INFI_TB_014_TIP_PERS_VALIDO SET ");		
		filtro.append(" TPPEVA_DESCRIPCION='").append(tipoPersonaValidoDefinicion.getTppeva_descripcion()).append("'");
		filtro.append(" WHERE TPPEVA_ID ='").append(tipoPersonaValidoDefinicion.getTppeva_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());				

	}


	public String eliminar(TipoPersonaValidoDefinicion tipoPersonaValidoDefinicion) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("DELETE FROM INFI_TB_014_TIP_PERS_VALIDO WHERE");
		
		filtro.append(" tppeva_id='").append(tipoPersonaValidoDefinicion.getTppeva_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}

	public void verificar(TipoPersonaValidoDefinicion tipoPersonaValidoDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select TPPEVA_ID from INFI_TB_106_UNIDAD_INVERSION  where");
		sql.append(" TPPEVA_ID='").append(tipoPersonaValidoDefinicion.getTppeva_id()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	public void existe(TipoPersonaValidoDefinicion tipoPersonaValidoDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select TPPEVA_ID from INFI_TB_014_TIP_PERS_VALIDO  where");
		sql.append(" TPPEVA_ID='").append(tipoPersonaValidoDefinicion.getTppeva_id()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
	

}
