package com.bdv.infi.dao;

import javax.sql.DataSource;
import com.bdv.infi.data.CuentasBancoBDV;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.db;

/**
 * DAO para las tablas de cuentas BDV relacionadas a las transacciones y a ser utilizadas por swift:
 * <b>infi_tb_007</b> e
 * <b>infi_tb_008</b>
 * @author elaucho
 */
public class CuentasBancoDAO extends GenericoDAO{

	/**
	 * Constructor de la clase
	 * @param ds
	 */
	public CuentasBancoDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
/**
 * Lista las cuentas del Banco de Venezuela <br>
 * @param String idCuenta
 * @return boolean el cúal indica si existe
 * @throws Exception
 */

	public boolean listarBicBDV(String idCuenta)throws Exception{
		boolean existe = false;
		String filtro  = "";
		
		if(!idCuenta.equals(null)&& !idCuenta.equals(""))
			filtro = " and id_cuenta="+idCuenta;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from infi_tb_007_ctas_bic_banco ");
		sql.append("where 1=1 ").append(filtro).append(" ");
		sql.append("order by infi_tb_007_ctas_bic_banco.id_cuenta");
		
		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0)
		{
			existe = true;
		}
		return existe;
	}
	/**
	 * Metodo que inserta un registro en la tabla 007
	 * @param CuentasBancoBDV cuentasBancoBDV
	 * @throws Exception
	 */
	public void insertarCuenta(CuentasBancoBDV cuentasBancoBDV)throws Exception{
		
		String secuenciaID = dbGetSequence(dataSource,ConstantesGenerales.INFI_TB_007_CTAS_BIC_BANCO);
		StringBuffer sql   = new StringBuffer();
		
		sql.append("insert into INFI_TB_007_CTAS_BIC_BANCO(id_cuenta,cod_bic_banco,nu_cuenta,ds_banco,FE_ULT_ACTUALIZACION,USR_ULT_ACTUALIZACION) values(");
		sql.append(secuenciaID).append(",'" );
		sql.append(cuentasBancoBDV.getCodigoBicBanco()).append("','" );
		sql.append(cuentasBancoBDV.getNumeroCuenta()).append("','" );
		sql.append(cuentasBancoBDV.getDescripcion()).append("',");		
		sql.append("sysdate," );
		sql.append("'").append(cuentasBancoBDV.getUsrUltActualizacion()).append("')");		
		
		//Se ejecuta el query
		db.exec(dataSource, sql.toString());
	}
	/**
	 * Elimina un registro de la tabla 007 (Cuentas asociadas al BDV para el proceso de SWIFT)
	 * @param idCuenta
	 * @throws Exception
	 */
	public void eliminarCuenta(long idCuenta)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from infi_tb_007_ctas_bic_banco where id_cuenta=").append(idCuenta);
		
		//Se ejecuta el query
		db.exec(dataSource, sql.toString());
	}
	/**
	 * Actualiza un registro en base de datos en la tabla 007
	 * @param CuentasBancoBDV cuentasBancoBDV
	 * @throws Exception
	 */
	public void actualizar(CuentasBancoBDV cuentasBancoBDV)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("update infi_tb_007_ctas_bic_banco set ");
		sql.append("cod_bic_banco='").append(cuentasBancoBDV.getCodigoBicBanco()).append("',");
		sql.append("nu_cuenta='").append(cuentasBancoBDV.getNumeroCuenta()).append("',");
		sql.append("ds_banco='").append(cuentasBancoBDV.getDescripcion()).append("', ");
		sql.append("FE_ULT_ACTUALIZACION=sysdate,");
		sql.append("USR_ULT_ACTUALIZACION='").append(cuentasBancoBDV.getUsrUltActualizacion()).append("' ");		
		sql.append("where id_cuenta=").append(cuentasBancoBDV.getIdCuenta());
		
		//Se actualiza el registro
		db.exec(dataSource,sql.toString());
	}
	/**
	 * Lista la transaccion depende del parametro recibido
	 * @param String transaccion
	 * @throws Exception
	 */
	public void listarTransaccion(String transaccion)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_007_ctas_bic_banco.*,INFI_TB_012_TRANSACCIONES.transa_id from INFI_TB_012_TRANSACCIONES  left join infi_tb_008_ctas_bic_transa on INFI_TB_012_TRANSACCIONES.transa_id=infi_tb_008_ctas_bic_transa.transa_id left join infi_tb_007_ctas_bic_banco on infi_tb_008_ctas_bic_transa.id_cuenta=infi_tb_007_ctas_bic_banco.id_cuenta where INFI_TB_012_TRANSACCIONES.transa_id='");
		sql.append(transaccion).append("'");
		
		
		dataSet = db.get(dataSource,sql.toString());
	}
	
	/**
	 * Actualiza la relacion entre cuenta y transaccion
	 * @param cuentasBancoBDV
	 * @throws Exception
	 */
	public void actualizarRelacion(CuentasBancoBDV cuentasBancoBDV)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from infi_tb_008_ctas_bic_transa where transa_id='").append(cuentasBancoBDV.getTransaccion()).append("'");
		dataSet = db.get(dataSource,sql.toString());
		
		//existe actualizamos
		if(dataSet.count()>0)
		{
			sql = new StringBuffer();
			sql.append("update infi_tb_008_ctas_bic_transa set id_cuenta=").append(cuentasBancoBDV.getIdCuenta());
			sql.append(",FE_ULT_ACTUALIZACION=SYSDATE,");
			sql.append("USR_ULT_ACTUALIZACION='").append(cuentasBancoBDV.getUsrUltActualizacion()).append("' ");			
			sql.append(" where transa_id='").append(cuentasBancoBDV.getTransaccion()).append("'");
			
			db.exec(dataSource, sql.toString());
		}else
		//insertamos
		{
			sql = new StringBuffer();
			sql.append("insert into infi_tb_008_ctas_bic_transa(id_cuenta,transa_id,FE_ULT_ACTUALIZACION,USR_ULT_ACTUALIZACION)values(").append(cuentasBancoBDV.getIdCuenta());
			sql.append(" ,'").append(cuentasBancoBDV.getTransaccion()).append("',");
			sql.append("SYSDATE,");
			sql.append("'").append(cuentasBancoBDV.getUsrUltActualizacion()).append("')");			
			
			db.exec(dataSource, sql.toString());
		}//fin else
	
	}//fin metodo
}//fin clase DAO
