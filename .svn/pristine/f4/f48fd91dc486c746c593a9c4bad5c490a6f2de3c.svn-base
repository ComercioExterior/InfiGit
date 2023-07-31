package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;

/**
 * NM29643 infi_TTS_466 08/07/2014
 * Clase encargada de la persistencia de la tabla de tipo de producto
 */
public class EventoMailDao extends GenericoDAO{

	public EventoMailDao(DataSource ds) {
		super(ds);
	}
	
	/**
	 * Lista los datos de un tipo de producto por Id
	 * @param tipoProductoId
	 * @throws Exception
	 */
	public void listarEventosDelProd(String productoId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select EVENTO_ID, EVENTO_NAME from INFI_TB_230_EVENTO_MAIL ");
		sql.append(" where tipo_producto_id = '").append(productoId).append("'");
		sql.append(" order by EVENTO_NAME");
		this.dataSet = db.get(this.dataSource, sql.toString());
	}
	
	@Override
	public Object moveNext() throws Exception {
		return null;
	}	
}
