package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.TipoPersona;

/** 
 * Clase encargada del manejo de los tipos de Transacciones Financieras 
 */
public class TipoTransaccionFinancieraDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public TipoTransaccionFinancieraDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public TipoTransaccionFinancieraDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Lista los tipos de transacciones financieras encontrados en la base de datos 
	 * @throws Exception 
	*/
	public void listarTodos() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_805_TRNFIN_TIPO order by trnfin_tipo_descripcion");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * 
	 * @param status
	 * @param tipoInstrumento
	 * @throws Exception
	 */
	public void listaEspecificos(String[] tipoTransaccion) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_805_TRNFIN_TIPO where ");
					
		//Verifica si se envian instrumentos financieros específicos
		if (tipoTransaccion != null){
			sql.append("trnfin_tipo in(");			
			for (int i=0; i< tipoTransaccion.length; i++) {
				sql.append("'").append(tipoTransaccion[i]).append("'");
				if (i < tipoTransaccion.length-1)
					sql.append(", ");
			}
			sql.append(") ");
		}		
		sql.append("order by trnfin_tipo_descripcion");		
		dataSet = db.get(dataSource, sql.toString());
    }

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
