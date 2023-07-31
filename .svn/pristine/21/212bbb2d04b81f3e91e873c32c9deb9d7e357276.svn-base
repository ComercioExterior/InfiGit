package com.bdv.infi.dao;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.webservices.beans.TCMGen1;


public class EstadoDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public EstadoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public EstadoDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> insertarEstados(ArrayList<TCMGen1> estados) throws Exception{		
		ArrayList<String> sentencias= new ArrayList<String>();	
		for (TCMGen1 estado : estados){			
			sentencias.add("INSERT INTO INFI_TB_ESTADO (COD_ESTADO, DESC_ESTADO) VALUES ('"+estado.getClave().trim()+"', '"+estado.getDatos1TablGeneral().substring(0,40).trim()+"')");
		}
		return sentencias;				
	}
	
	/**Elimina la data de la tabla infi_tb_estados
	 * @param idCliente cliente
	 * @throws Exception
	 */	
	public String deleteEstados() throws Exception{
		String query="DELETE FROM INFI_TB_ESTADO";
		return query;
	}
	
	/**Consulta los estados
	 * @throws Exception
	 */		
	public void consultarEstados ()throws Exception{		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COD_ESTADO, DESC_ESTADO FROM INFI_TB_ESTADO ORDER BY DESC_ESTADO");
		dataSet = db.get(dataSource, sql.toString());
	
	}
	
	
	
}
