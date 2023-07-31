package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
public class CanalDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public CanalDAO(DataSource ds) {
		super(ds);
	}
	//NM32454  DEVUELVE LA LISTA DE CANALES
	public void listar() throws Exception{
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT * FROM INFI_TB_904_CANAL "); 
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	//NM25287 TTS-491 DEVUELVE LA LISTA DE CANALES CON FILTRO
	public void listar(int activo,String codigo) throws Exception{
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT CODIGO_CANAL,NOMBRE_CANAL,ACTIVO,CANAL_ID FROM INFI_TB_904_CANAL "); 
		sb.append(" WHERE ACTIVO =").append(activo);

		if(codigo!=null){			
			sb.append("AND CODIGO_CANAL =").append(codigo);
		}
		
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
}   
