package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/** 
 * Clase que se encarga de la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n
 * y listado de las comisiones relacionadas a una unidad de inversi&oacute;n y a un bloter
 */


public class ComisionDAO extends com.bdv.infi.dao.GenericoDAO {

	public ComisionDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}
	
	public ComisionDAO(DataSource ds) {
		super(ds);
	}	

	/**
	 * Busca las comisiones asociadas a una unidad de inversi&oacute;n. 
	 * @param idUnidad id de la unidad de inversi&oacute;n
	 * @param tipoPersona tipo de persona que realiza la operaci&oacute;n (v,E,J)
	 * @param bloter bloter asociado a la transaccion realizada
	 * @param tipoMoneda moneda referida en la transacci&oacute;n
	*/
	public void listar(long idUnidad, String bloter, int tipoPersona, int tipoMoneda){
	//INFO_TB_106_UI_COMISIONES
	}

	/***/
	public int insertar(Object objeto) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int modificar(Object objeto) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Metodo que Busca el Año menor al que se debe cobrar comisones,
	 * util para filtros para mostrar el año menor hasta el año en curso 
	 */
	public void listarMesesComisionesPorCobrar()throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT MIN(TO_CHAR(op.fecha_aplicar,'YYYY')) as desde, TO_CHAR(SYSDATE,'YYYY') as hasta FROM INFI_TB_204_ORDENES o, INFI_TB_207_ORDENES_OPERACION op where o.ordene_id=op.ordene_id and o.transa_id='");
		sql.append(TransaccionNegocio.CUSTODIA_COMISIONES);
		sql.append("' and op.status_operacion in ('");
		sql.append(ConstantesGenerales.STATUS_RECHAZADA);
		sql.append("','");
		sql.append(ConstantesGenerales.STATUS_EN_ESPERA);
		sql.append("')");
		
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarMesesComisionesPorCobrar "+sql);
	}
	
	/**Metodo que Busca el Año menor al que se debe cobrar comisones,
	 * util para filtros para mostrar el año menor hasta el año en curso 
	 */
	public void listarMesesFacturasCobradas()throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select DISTINCT MIN(TO_CHAR(op.fecha_aplicar,'YYYY')) as desde, TO_CHAR(SYSDATE,'YYYY') as hasta from INFI_TB_207_ORDENES_OPERACION op, INFI_TB_204_ORDENES ord where op.ordene_id=ord.ordene_id and ord.transa_id='");
		sql.append(TransaccionNegocio.CUSTODIA_COMISIONES);
		sql.append("'");
		
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarMesesFacturasCobradas "+sql);
	}
}
