package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

/**Clase que contiene los métodos para la recuperación de data
 * relacionada a los operadores que se encuentran en tablas OPICS*/
public class OperadorDAO extends GenericoDAO {

	public OperadorDAO(DataSource ds) {
		super(ds);
	}

	public OperadorDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}

	/**Lista el operador según el NM del usuario que ingresa a 
	 * INFI. Genera un dataSet
	 * @param nmUsuario nm de usuario que ingresa a INFI
	 * return Devuelve el operador o trader del usuario asociado. Retorna null en caso de no encontrar ninguna coincidencia
	 * @throws lanza una excepción en caso de error*/
	public String listarOperador(String nmUsuario) throws Exception{
		String trader = null;
	    StringBuffer sb = new StringBuffer();
	    sb.append("SELECT OPER FROM INFI_VI_OPER WHERE BR=99 AND EMPLOYEENUMBER='").append(nmUsuario).append("'");
	    DataSet ds = db.get(this.dataSource, sb.toString());
	    if (ds.count()>0){
	    	ds.next();
	    	trader = ds.getValue("OPER");
	    }
	    return trader;
	}	
}
