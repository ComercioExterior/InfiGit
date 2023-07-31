package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de la persistencia de la tabla de tipo de producto
 */
public class TipoProductoDao extends GenericoDAO{

	public TipoProductoDao(Transaccion transanccion) throws Exception {
		super(transanccion);
	}
	
	public TipoProductoDao(DataSource ds) {
		super(ds);
	}
	
	/**
	 * Lista todos los tipos de productos encontrados
	 * @throws Exception en caso de error
	 */
	public void listarProductos() throws Exception{
		String sql = "select tipo_producto_id, nombre from infi_tb_019_tipo_de_producto";
		//NM29643 - infi_TTS_466_Calidad 03/10/2014 Se excluye el tipo de producto "GENERAL"
		sql += " where tipo_producto_id NOT IN ('"+ConstantesGenerales.ID_TIPO_PRODUCTO_GENERAL+"')";
		this.dataSet = db.get(this.dataSource, sql);
	}
	
	/**
	 * Lista los datos de un tipo de producto por Id
	 * @param tipoProductoId
	 * @throws Exception
	 */
	public void listarProducto(String tipoProductoId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select tipo_producto_id, nombre from infi_tb_019_tipo_de_producto ");
		sql.append(" where tipo_producto_id = '").append(tipoProductoId).append("'");
		
		this.dataSet = db.get(this.dataSource, sql.toString());
	}
	
	/**
	 * NM29643 INFI_TTS_466
	 * Lista los productos con eventos de envio de correo asociados
	 * @param tipoProductoId
	 * @throws Exception
	 */
	public void listarProductosConEventos() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select tp.tipo_producto_id, tp.nombre from infi_tb_019_tipo_de_producto tp ");
		sql.append(" where tipo_producto_id IN (");
		sql.append("select distinct(evm.TIPO_PRODUCTO_ID) from INFI_TB_230_EVENTO_MAIL evm)");
		sql.append(" order by tp.nombre");
		
		this.dataSet = db.get(this.dataSource, sql.toString());
	}

	
	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
	/**
	 * Lista solo los tipos de producto asociados al SIMADI
	 * @throws Exception en caso de error
	 */
	//CT20315 - 16-02-2015 Añadido para TTS_491_contingencia 
	public void listarProductosSimadi() throws Exception{
		String sql = "select tipo_producto_id, nombre from infi_tb_019_tipo_de_producto";
		sql += " where tipo_producto_id in ('OFERTA','DEMANDA')";
		this.dataSet = db.get(this.dataSource, sql);
	}
}
