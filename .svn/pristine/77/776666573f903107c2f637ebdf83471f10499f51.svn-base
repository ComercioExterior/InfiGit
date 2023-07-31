package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;

public class CredencialesDAO extends com.bdv.infi.dao.GenericoDAO {

	public CredencialesDAO(DataSource ds) {
		super(ds);
		
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	 /**Lista los ciclos del tipo especificado
	 * @param tipo Tipo de ciclo a consultar
	 * @throws Exception
	 * NM32454 INFI_TB_WS_BCV 10/03/2015
	 */
	public void listarCredencialesPorTipo(String tipo) throws Exception{
		String sql; 
		if(tipo.equals("") || tipo == null){
			sql = "SELECT * FROM INFI_TB_911_CREDENCIALES C ";
		}else {
			sql = "SELECT * FROM INFI_TB_911_CREDENCIALES C WHERE C.SISTEMA = '"+tipo+"' ";
		}
		dataSet = db.get(dataSource, sql);
		System.out.println("listarCredencialesPorTipo : "+sql);
	}
	
	public String insertar (String sistema, String clave, String usuario, String activo, String interno){
		String sql = "INSERT INTO INFI_TB_911_CREDENCIALES (SISTEMA, CLAVE, USUARIO,ACTIVO, INTERNO)";
			   sql+= "VALUES ('"+sistema+"' ,'"+clave+"' ,'"+usuario+"' , "+activo+" ,"+interno+")";
		return(sql.toString());		   
	}
	
	public String delete (String sistema){
		String sql = "DELETE FROM INFI_TB_911_CREDENCIALES WHERE SISTEMA = '"+sistema+"'";
		return(sql.toString());		   
	}
	
	public String update (String sistema, String usuario, String clave){
		String sql = "UPDATE INFI_TB_911_CREDENCIALES SET USUARIO = '"+usuario+"', CLAVE = '"+clave+"' WHERE SISTEMA = '"+sistema+"'";
		return(sql.toString());		   
	}
}
