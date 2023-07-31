package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

/** 
 * Clase que encapsula los procesos de inserci&oacute;n, modificaci&oacute;n, eliminaci&oacute;n y lista de los veh&iacute;culos registrados. Hace uso de la tabla INFI_TB_018_VEHICULOS
 */
public class FormaInstrumentoDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public FormaInstrumentoDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}
	
	public FormaInstrumentoDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Lista Todas las formas de instrumentos financieros existentes
	*/
	public void listarTodos() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_038_INST_FORMA_ORDEN order by insfin_forma_orden_desc");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}
