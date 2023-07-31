package com.bdv.infi.dao;

import java.util.Date;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.TitulosCierre;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**Clase para el manejo de inserción y recuperación de registros de la tabla INFI_TB_702_TITULOS_CIERRE*/
public class TitulosCierreDAO extends GenericoDAO{

	
	public TitulosCierreDAO(DataSource ds) {
		super(ds);
	}

	public TitulosCierreDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
	/**Lista la posición global del cliente a la fecha dada
	 * 
	 * @param idCliente id del cliente a consultar
	 * @param fechaDesde fecha desde para obtener la posición en custodia
	 * @param fechaHasta fecha hasta para obtener la posición en custodia
	 * @throws Exception lanza una exception en caso de error
	 */
	public void listarTitulosAFecha(String idCliente, Date fechaDesde, Date fechaHasta) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.client_id,sum(a.titcus_cantidad) cantidad, c.titulo_moneda_den from infi_tb_702_titulos_cierre a,");
		sql.append("(select client_id,titulo_id, max(fecha_cierre) fecha_cierre from infi_tb_702_titulos_cierre");
		sql.append(" where trunc(fecha_cierre) <= ").append(this.formatearFechaBD(fechaHasta));
		sql.append(" group by client_id,titulo_id) b, infi_tb_100_titulos c where a.client_id = b.client_id and ");
		sql.append(" a.fecha_cierre = b.fecha_cierre and a.titulo_id = b.titulo_id");
		sql.append(" and a.titulo_id = c.titulo_id" );
		sql.append(" and a.client_id =").append(idCliente);
		sql.append(" group by a.client_id, c.titulo_moneda_den");
		dataSet= db.get(this.dataSource, sql.toString());
	}
}
