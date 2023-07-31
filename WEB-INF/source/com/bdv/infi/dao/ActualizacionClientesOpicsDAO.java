package com.bdv.infi.dao;

import java.sql.CallableStatement;
import javax.sql.DataSource;

public class ActualizacionClientesOpicsDAO extends com.bdv.infi.dao.GenericoDAO {
	
	
	public ActualizacionClientesOpicsDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public ActualizacionClientesOpicsDAO(DataSource ds) throws Exception {
		super(ds);
	}	

	
	/**
	 * Actualizar a través la llamada a un stored procedure los clientes en Opics asociados a las órdenes enviadas de una unidad de inversión en la generación del archivo
	 * @param idUnidadInversion
	 * @param estatusOrden
	 * @throws Exception
	 */
	public void actualizarClientesOpics(int idUnidadInversion, String estatusOrden) throws Exception{
		
		
		try {
			//conn = this.dataSource.getConnection();
			conn = conn==null?this.dataSource.getConnection():conn;	
			//conn.setAutoCommit(false);
			CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call INFI_CLIENTES_OPICS(?,?) }");
			// cargar parametros al SP
			procedimientoAlmacenado.setInt(1, idUnidadInversion);
			procedimientoAlmacenado.setString(2, estatusOrden);
			// ejecutar el SP
			procedimientoAlmacenado.execute();
			// confirmar si se ejecuto sin errores
			//conn.commit();					
		
		} catch (Exception e) {
			if(conn!=null){conn.rollback();}			
			throw new Exception("Error en la actualización de clientes en Opics: " + e.getMessage());
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
