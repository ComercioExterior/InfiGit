package com.bdv.infi.dao;

import java.sql.CallableStatement;

import javax.sql.DataSource;

public class MigracionHistoricoDao extends com.bdv.infi.dao.GenericoDAO {

	public MigracionHistoricoDao(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	public MigracionHistoricoDao(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}

	public void migracionHistoricoDao(String fecha) throws Exception{
		
		try {
			//conn = this.dataSource.getConnection();
			conn = conn==null?this.dataSource.getConnection():conn;	
			//conn.setAutoCommit(false);
			CallableStatement procedure = conn.prepareCall("{ call ADM_INFI.MIGRACION.MIGRARHIST(?) }");
			// cargar parametros al SP
			procedure.setString(1, fecha);
			// ejecutar el SP
			procedure.execute();
			// confirmar si se ejecuto sin errores
			conn.commit();					
		
		} catch (Exception e) {
			if(conn!=null){conn.rollback();}			
			throw new Exception("Error al momento de ejecutar el proceso de migracion: " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}		
	}
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
