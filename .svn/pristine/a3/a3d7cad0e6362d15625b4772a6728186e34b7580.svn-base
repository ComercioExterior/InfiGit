package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

public class TipoCartaDAO extends com.bdv.infi.dao.GenericoDAO {
	/**
	 * Constructor.
	 * @param ds
	 * @throws Exception
	 */
	public TipoCartaDAO(DataSource ds) throws Exception {
		super(ds);
	}	
	
	/**
	 * Constructor.
	 * @param transaccion
	 * @throws Exception
	 */
	public TipoCartaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	/**
	 * Lista los tipos de Carta configurados en la tabla  y los almacena en un DataSet
	 * @throws Exception 
	*/
	public void listar() throws Exception{
		String sql = "SELECT * FROM INFI_TB_809_TIPO_CARTAS order by tipcar_id";
		
		dataSet = db.get(dataSource, sql);

	}
	
	/**
	 * Lista los tipos de Carta configurados en la tabla  y los almacena en un DataSet
	 * @throws Exception 
	*/
	public void listarPorTipoCartas(String IdsCartas) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_809_TIPO_CARTAS c WHERE TIPCAR_ID IN (");
		sql.append(IdsCartas);
		sql.append(")");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	/**
	 * Lista los tipos de Carta asociados a los bloter de una unidad de inversion y los almacena en un DataSet
	 * @throws Exception 
	*/
	public void listarCartas(String unidad) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select br.bloter_id, p.tipper_nombre , p.tipper_id, d.tipcar_id from INFI_TB_111_UI_BLOTTER_RANGOS br right join  INFI_TB_200_TIPO_PERSONAS p on p.tipper_id=br.tipper_id right join  INFI_TB_115_UI_DOC d on d.bloter_id=br.bloter_id where br.undinv_id=");
		sql.append(unidad);
		sql.append(" group by br.bloter_id, p.tipper_nombre , p.tipper_id, d.tipcar_id order by tipper_nombre");
		
		dataSet = db.get(dataSource, sql.toString());

	}
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
