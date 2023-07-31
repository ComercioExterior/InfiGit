package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
public class ConceptosDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public ConceptosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public ConceptosDAO(DataSource ds) {
		super(ds);
	}
	public void listar() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select CODIGO_ID,CONCEPTO,NUMERAL from INFI_TB_050_CONCEPTOS order by codigo_id");		
		dataSet = db.get(dataSource, sb.toString());		
	}	
	public void listar(String codigo_id) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select CODIGO_ID,CONCEPTO,NUMERAL from INFI_TB_050_CONCEPTOS WHERE codigo_id = '");
		sb.append(codigo_id).append("'");		
		dataSet = db.get(dataSource, sb.toString());		
	}

	public String modificar(String codigo_id, String concepto,String numeral){
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_050_CONCEPTOS SET concepto='").append(concepto).append("'");
		sql.append(" ,numeral='").append(numeral).append("'");
		sql.append(" WHERE codigo_id='").append(codigo_id).append("'");
		//System.out.println("SQL update: "+sql.toString());
		return(sql.toString());
	}
	public String insertar(String codigo_id,String concepto,String numeral) {
		StringBuffer sql = new StringBuffer();				
		sql.append("INSERT INTO INFI_TB_050_CONCEPTOS (codigo_id,concepto,numeral) values('");
		sql.append(codigo_id).append("','").append(concepto).append("','").append(numeral).append("')");
		//System.out.println("SQL insert: "+sql.toString());
		return(sql.toString());		
	}
	
	public String eliminar(String codigo_id){
		StringBuffer sql = new StringBuffer();		
		sql.append("DELETE FROM INFI_TB_050_CONCEPTOS WHERE codigo_id='").append(codigo_id).append("'");
		//System.out.println("SQL delete: "+sql.toString());
		return(sql.toString());
	}
	public Object moveNext() throws Exception {
		return null;
	}	
}   
