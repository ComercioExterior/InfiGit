package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;

import megasoft.db;
public class CuentasUsoDAO extends GenericoDAO {

	public CuentasUsoDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}

	public CuentasUsoDAO(Transaccion transaccion)throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * lista los tipos de uso de cuentas
	 * **/
	public void listar()throws Exception{
		dataSet=db.get(dataSource,"select * from infi_tb_808_ctas_uso");
	}
	/*
	 * Listar Solo los tipos de cuentas de cobro de comisiones, pago de cupones, pago capital
	 */
	public void listarUsuoCuenta()throws Exception
	{
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT * FROM  INFI_TB_808_CTAS_USO WHERE  CTECTA_USO IN ").append("('").append(UsoCuentas.COBRO_DE_COMISIONES).append("'").append(',').append("'").append(UsoCuentas.PAGO_DE_CAPITAL).append("'").append(',').append("'").append(UsoCuentas.PAGO_DE_CUPONES).append("'").append(",'").append(UsoCuentas.VENTA_TITULO).append("')");
		dataSet = db.get(dataSource, sb.toString());
	}
/**
 * Verifica que el cliente tenga solo una cuenta de uso asociada para una Transacci&oacute;n espec&iacute;fica
 * @param long clienteId
 * @param String cuentaUso
 * @return boolean
 * @throws Exception
 */
	public boolean verificarCuentaUsoExiste(long clienteId,String cuentaUso, String tipoInstruccion)throws Exception{
		StringBuffer sql = new StringBuffer();
		boolean existe   = false;
		
		if (!tipoInstruccion.equals(String.valueOf(TipoInstruccion.CUENTA_NACIONAL))){
			//El tipo de instrucción es diferente y se valida si el cliente posee instrucciones
			tipoInstruccion = TipoInstruccion.CUENTA_SWIFT + "," + TipoInstruccion.CHEQUE + "," + TipoInstruccion.OPERACION_DE_CAMBIO;
		}		
		
		sql.append("select * from INFI_TB_202_CTES_CUENTAS where client_id=");
		sql.append(clienteId).append(" and ctecta_uso='").append(cuentaUso).append("'");
		sql.append(" and tipo_instruccion_id in(").append(tipoInstruccion).append(")");
		dataSet=db.get(dataSource,sql.toString());
		if(dataSet.count()>0){
			existe=true;
		}
		return existe;
	}

}
