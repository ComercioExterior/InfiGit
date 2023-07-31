package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.data.IndicadoresDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import megasoft.db;

/** 
 * Clase para buscar, agregar, modificar y eliminar los indicadores registrados en la base de datos;
 */
public class InstruccionesPagoDAO extends com.bdv.infi.dao.GenericoDAO {

	  
	public InstruccionesPagoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);		
	}
	
	
	public InstruccionesPagoDAO(DataSource ds) {
		super(ds);		
	}


	/**
	 * Busca todos los tipos de instrucciones de pago registrados en la base de datos. 
	 * @throws Exception 
	*/
	public void listar() throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_045_TIPO_INSTRUCCION");
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	/**
	 * Busca los datos de la instrucci&oacute;n de pago por el id recibido.  
	 * @throws Exception 
	*/
	public void listar(String tipoInstruccionId) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_045_TIPO_INSTRUCCION WHERE TIPO_INSTRUCCION_ID ='").append(tipoInstruccionId).append("'");
		dataSet = db.get(dataSource, sb.toString());
						
	}
	
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	  
	
}   
