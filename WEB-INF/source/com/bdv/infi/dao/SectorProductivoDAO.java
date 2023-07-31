package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
public class SectorProductivoDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public SectorProductivoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public SectorProductivoDAO(DataSource ds) {
		super(ds);
	}
	public void listar() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select sector_id,descripcion from INFI_TB_048_SECTOR_PRODUCTIVO order by sector_id");
		//System.out.println("SQL: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
	}
	public String modificar(String sector_id, String descripcion){
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_048_SECTOR_PRODUCTIVO SET descripcion='").append(descripcion).append("'");
		sql.append(" WHERE sector_id='").append(sector_id).append("'");
		//System.out.println("SQL update: "+sql.toString());
		return(sql.toString());
	}
	public String insertar(String sector_id,String descripcion) {
		StringBuffer sql = new StringBuffer();				
		sql.append("INSERT INTO INFI_TB_048_SECTOR_PRODUCTIVO (sector_id, descripcion) values('").append(sector_id).append("','").append(descripcion).append("')");
		return(sql.toString());		
	}
	
	public String eliminar(String sector_id){
		StringBuffer sql = new StringBuffer();		
		sql.append("DELETE FROM INFI_TB_048_SECTOR_PRODUCTIVO WHERE sector_id='").append(sector_id).append("'");
		return(sql.toString());
	}
	public Object moveNext() throws Exception {
		return null;
	}	
}   
