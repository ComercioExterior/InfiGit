package com.bdv.infi.dao;
import javax.sql.DataSource;
import megasoft.db;
public class ActividadEconomicaDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public ActividadEconomicaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public ActividadEconomicaDAO(DataSource ds) {
		super(ds);
	}
	public void listar() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select CODIGO_ID,SECTOR,SECTOR_DESCRIPCION from INFI_TB_049_ACTIVIDAD order by codigo_id");
		dataSet = db.get(dataSource, sb.toString());		
	}
	public String modificar(String sector, String codigo_id, String sector_descripcion){
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_049_ACTIVIDAD SET sector='").append(sector).append("'");
		sql.append(" ,sector_descripcion='").append(sector_descripcion).append("'");
		sql.append(" WHERE codigo_id='").append(codigo_id).append("'");
			return(sql.toString());
	}
	public String insertar(String codigo_id,String sector,String sector_descripcion) {
		StringBuffer sql = new StringBuffer();				
		sql.append("INSERT INTO INFI_TB_049_ACTIVIDAD (codigo_id,sector,sector_descripcion) values('");
		sql.append(codigo_id).append("','").append(sector).append("','").append(sector_descripcion).append("')");
		return(sql.toString());		
	}
	
	public String eliminar(String codigo_id){
		StringBuffer sql = new StringBuffer();		
		sql.append("DELETE FROM INFI_TB_049_ACTIVIDAD WHERE codigo_id='").append(codigo_id).append("'");
		return(sql.toString());
	}
	public Object moveNext() throws Exception {
		return null;
	}	
}   
