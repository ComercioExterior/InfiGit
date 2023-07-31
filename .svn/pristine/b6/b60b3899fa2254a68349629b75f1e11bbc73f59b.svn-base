package com.bdv.infi.dao;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import com.bdv.infi.data.DetalleFactura;
import com.bdv.infi.data.Factura;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**Clase que permite el ingreso y recuperación de 
 * registros de la tabla INFI_TB_213_FACTURA*/
public class FacturaDAO extends GenericoDAO {
	

	public FacturaDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}

	/**Almacena una factura con su detalle en la base de datos
	 * @param factura representa un objeto Factura con sus detalles o renglones que la conforman
     * @return numero de ordenes insertadas	 * 
	 * @throws Exception lanza una excepción en caso de un error en el proceso de guardado*/
	public int insertar(Factura factura) throws Exception{
		StringBuffer sbFactura = new StringBuffer();
		StringBuffer sbDetalle = new StringBuffer();
		int filasInsertadas;
		
		PreparedStatement preparedDetalle = null;
		
		sbFactura.append("INSERT INTO INFI_TB_213_FACTURA (CLIENT_ID,FACTURA_ID,FECHA_FACT,MONTO,ORDENE_ID,FECHA_MES)");
		sbFactura.append(" VALUES (?,?,sysdate,?,?,?)");
		
		sbDetalle.append("INSERT INTO INFI_TB_214_FACTURA_DETALLE (FACTURA_DETALLE_ID,FACTURA_ID,SERVICIO,CANTIDAD,TASA_MONTO,MONTO_OPERACION,MONEDA_ID,CANTIDAD_OPERACIONES)");
		sbDetalle.append(" VALUES (?,?,?,?,?,?,?,?)");		
		
		 try {
			this.conn = this.transaccion.getConnection();
			
			//Obtiene el id de la secuencia
		    long secuencia = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.INFI_TB_213_FACTURA)).longValue();			
			factura.setIdFactura(secuencia);
			
			preparedStatement = conn.prepareStatement(sbFactura.toString());
			preparedStatement.setLong(1, factura.getIdCliente());
			preparedStatement.setLong(2, secuencia);
			preparedStatement.setDouble(3, factura.getMontoTotal());
			preparedStatement.setLong(4, factura.getIdOrden());
			Timestamp nowSql = new Timestamp(factura.getFechaMes().getTime());
			preparedStatement.setTimestamp(5, nowSql);
			
			filasInsertadas = preparedStatement.executeUpdate();			
			preparedDetalle = conn.prepareStatement(sbDetalle.toString());
			//Inserta el detalle de la factura			
			for (DetalleFactura detalleFact: factura.getDetalleFactura()){				
			    long secuenciaDetalle = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.INFI_TB_214_FACTURA_DETALLE)).longValue();				
				preparedDetalle.setLong(1, secuenciaDetalle);
				preparedDetalle.setLong(2, factura.getIdFactura());
				preparedDetalle.setString(3, detalleFact.getNombreServicio());
				preparedDetalle.setDouble(4, detalleFact.getCantidad());
				preparedDetalle.setDouble(5, detalleFact.getTasaMonto());
				preparedDetalle.setDouble(6, detalleFact.getMontoOperacion());
				preparedDetalle.setString(7, detalleFact.getIdMoneda());
				preparedDetalle.setLong(8, detalleFact.getCantidadOperaciones());				
				preparedDetalle.addBatch();				
			}
			preparedDetalle.executeBatch();
			preparedDetalle.close();
		 } catch (Exception e) {
				throw new Exception("Error al intentar registrar la factura. Error: " + e.getMessage());
		 } finally{
			 this.closeResources();
		 }
		 return filasInsertadas;
	}
	
}
