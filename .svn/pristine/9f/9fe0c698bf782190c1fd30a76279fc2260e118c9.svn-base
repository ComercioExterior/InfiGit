package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
public class GrupoDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public GrupoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public GrupoDAO(DataSource ds) {
		super(ds);
	}
	public void listar() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select grupo_id, descripcion from INFI_TB_032_TRNF_GRUPO order by descripcion");
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public void listarEspecificos() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select grupo_id, descripcion from INFI_TB_032_TRNF_GRUPO order by descripcion");
		dataSet = db.get(dataSource, sb.toString());		
	}	
//	public String modificar(Grupo grupo){
//		StringBuffer sql = new StringBuffer();
//		sql.append("update INFI_TB_048_SECTOR_PRODUCTIVO SET descripcion='").append(descripcion).append("'");
//		sql.append(" WHERE sector_id='").append(sector_id).append("'");
//		//System.out.println("SQL update: "+sql.toString());
//		return(sql.toString());
//	}
//	public String insertar(Grupo grupo) {
//		StringBuffer sql = new StringBuffer();				
//		sql.append("INSERT INTO INFI_TB_048_SECTOR_PRODUCTIVO (sector_id, descripcion) values('").append(sector_id).append("','").append(descripcion).append("')");
//		return(sql.toString());		
//	}
//	
//	public String eliminar(int idGrupo){
//		String sql = "";		
//		sql = "DELETE FROM INFI_TB_032_TRNF_GRUPO WHERE grupo_id=" + idGrupo;
//		return(sql);
//	}
	
	public Object moveNext() throws Exception {
		return null;
	}	
}   
