package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.TipoPersona;

/** 
 * Clase que se conecta con la base de datos y busca los tipos de clientes registrados en configuraci&oacute;n. (V,E,J)
 */
public class ClienteSegmentoDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public ClienteSegmentoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public ClienteSegmentoDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Lista los tipos de clientes encontrados en la base de datos (V,E,J) 
	 * @throws Exception 
	*/
	public void listarTodos() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_015_CTES_SEGMENTOS");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista los tipos de clientes por el tipo seleccionado (V,E,J) 
	*/
	public void listarPorTipo(String tipo){
	
	}	
	
	public int insertar(TipoPersona tipoPersona) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int modificar(TipoPersona tipoPersona) {
		// TODO Auto-generated method stub
		return 0;
	}


	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
	

}
