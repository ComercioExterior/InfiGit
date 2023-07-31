package com.bdv.infi.dao;

import java.sql.Date;
import java.sql.Timestamp;

import javax.sql.DataSource;

import megasoft.Logger;
import megasoft.db;

import com.bdv.infi.data.CalculoMes;
import com.bdv.infi.data.CalculoMesDetalle;
import com.bdv.infi.data.Factura;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**Clase que permite el ingreso y recuperación de 
 * registros de la tabla INFI_TB_814_CALC_MES*/
public class CalculoMesDAO extends GenericoDAO{

	public CalculoMesDAO(Transaccion transanccion) throws Exception {
		super(transanccion);	
	}
	
	/**Almacena el cálculo efectuado por un usuario determinado para conocer el total de comisiones
	 * o cupones que se deben cobrar o pagar según la transacción
	 * @param calculoMes representa un objeto CalculoMes que debe insertarse
     * @return numero de ordenes insertadas	 * 
	 * @throws Exception lanza una excepción en caso de un error en el proceso de guardado*/
	public int insertar(CalculoMes calculoMes) throws Exception{
		StringBuffer sbCalculo = new StringBuffer();
		int filasInsertadas;
		
		sbCalculo.append("INSERT INTO INFI_TB_814_CALC_MES (CALCULO_MES_ID,USUARIO_ID,FECHA_DESDE,FECHA_HASTA,TRANSA_ID,FECHA_CONSULTA)");
		
		
		try {
			//Obtiene el id de la secuencia
			long secuencia = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.INFI_TB_814_CALC_MES)).longValue();			
			calculoMes.setIdCalculoMes(secuencia);
			
			sbCalculo.append(" VALUES (").append(calculoMes.getIdCalculoMes()).append(",");
			sbCalculo.append(calculoMes.getIdUsuario()).append(",");
			sbCalculo.append(this.formatearFechaBD(calculoMes.getFechaDesde())).append(",");			
			sbCalculo.append(this.formatearFechaBD(calculoMes.getFechaHasta())).append(",");			
			sbCalculo.append("'").append(calculoMes.getIdTransaccion()).append("',");
			sbCalculo.append("sysdate)");
			
			this.statement = conn.createStatement();
			
			filasInsertadas = statement.executeUpdate(sbCalculo.toString());
		} catch (Exception e) {
			Logger.info(this,"Error al intentar registrar el cálculo del mes. Error: " + e.getMessage());
			throw new Exception("Error al intentar registrar el cálculo del mes. Error: " + e.getMessage());
		} finally{
			this.closeResources();
		}
		return filasInsertadas;
	}
	
	/**Inserta un detalle del cálculo obtenido. Este método debe invocarse sólo después de insertar el cálculo
	 * @param calculoMesDetalle objeto que contiene un detalle del cálculo efectuado
	 * @throws Exception lanza un excepción si hay un error*/
	public int insertarDetalle(CalculoMesDetalle calculoMesDetalle) throws Exception{
		StringBuffer sbCalculo = new StringBuffer();
		int filasInsertadas;
		
		sbCalculo.append("INSERT INTO INFI_TB_815_CALC_MES_DETALLE (REGISTRO_OP_ID,CALCULO_MES_ID,OPERACION_NOMBRE,");
		sbCalculo.append("CANTIDAD,CANTIDAD_OPERACIONES,TASA_MONTO,MONTO_OPERACION,MONEDA_ID,TITULO_ID,");
		sbCalculo.append("CODIGO_OPERACION,CLIENT_ID,SIGNO_PORCENTAJE,FECHA_INICIO_PAGO_CUPON,FECHA_FIN_PAGO_CUPON,COMISION_OPERACION,DIAS_CALCULO)");
				
		try {
			//Obtiene el id de la secuencia
			long secuencia = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.INFI_TB_815_CALC_MES_DETALLE)).longValue();			
			calculoMesDetalle.setIdRegistro(secuencia);
			
			sbCalculo.append(" VALUES (");
			sbCalculo.append(calculoMesDetalle.getIdRegistro()).append(",");
			sbCalculo.append(calculoMesDetalle.getIdCalculoMes()).append(",");
			sbCalculo.append("'").append(calculoMesDetalle.getNombreOperacion()).append("',");
			sbCalculo.append(calculoMesDetalle.getCantidad()).append(",");		
			sbCalculo.append(calculoMesDetalle.getCantidadOperaciones()).append(",");			
			sbCalculo.append(calculoMesDetalle.getTasaMonto()).append(",");		
			sbCalculo.append(calculoMesDetalle.getMontoOperacion()).append(",");		
			sbCalculo.append("'").append(calculoMesDetalle.getIdMoneda()).append("',");		
			sbCalculo.append("'").append(calculoMesDetalle.getIdTitulo()).append("',");
			sbCalculo.append("'").append(calculoMesDetalle.getCodigoOperación()).append("',");
			sbCalculo.append(calculoMesDetalle.getIdCliente()).append(",");
			sbCalculo.append(calculoMesDetalle.isSignoPorcentaje()?1:0).append(",");			
			
			if (calculoMesDetalle.getFechaInicio()!= null){
				sbCalculo.append(this.formatearFechaBD(calculoMesDetalle.getFechaInicio())).append(",");				
			} else {
				sbCalculo.append("null").append(",");				
			}
			
			if (calculoMesDetalle.getFechaFin()!= null){
				sbCalculo.append(this.formatearFechaBD(calculoMesDetalle.getFechaFin())).append(",");				
			} else {
				sbCalculo.append("null").append(",");
			}
					
			if (calculoMesDetalle.getComisionOperacion().doubleValue()>0){
				sbCalculo.append(calculoMesDetalle.getComisionOperacion()).append(",");				
			} else {
				sbCalculo.append("null").append(",");
			}
			
			sbCalculo.append(calculoMesDetalle.getDiasCalculo());
			
			
			sbCalculo.append(")");
			statement = conn.createStatement();
			filasInsertadas = statement.executeUpdate(sbCalculo.toString());
		} catch (Exception e) {
			Logger.info(this,"Error en consulta: " + sbCalculo.toString());			
			throw new Exception("Error al intentar registrar el cálculo del mes. Error: " + e.getMessage());
		} finally{
			this.closeResources();
		}
		return filasInsertadas;		
	}
	
	/**Elimina el cálculo del mes sólo del usuario especificado
	 * @param idUsuario id de usuario que se usa para eliminar los registros asociados
	 * @throws lanza una excepcion en caso de error al ejecutar la operación*/	
	public void eliminar(int idUsuario, String transaccion) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM INFI_TB_814_CALC_MES WHERE USUARIO_ID=").append(idUsuario);
		sb.append("AND TRANSA_ID='").append(transaccion).append("'");
		db.exec(this.dataSource, sb.toString());
	}
	
	/**Lista la cabecera del cálculo efectuado según el id recibido. Arma un DataSet con el resultado
	 * @param idCalculo id referente al cálculo efectuado 
	 * @throws lanza una excepcion en caso de error al ejecutar la operación*/ 
	public void listarCalculo(long idCalculo) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_814_CALC_MES WHERE CALCULO_MES_ID=").append(idCalculo);
		db.exec(this.dataSource, sb.toString());
	}
	
	/**Lista todos los cálculos efectuados por el usuario. Arma un DataSet con el resultado
     * @param idUsuario id del usuario que está consultando la operación
	 * @throws lanza una excepcion en caso de error al ejecutar la operación*/ 
	public void listarCalculo(int idUsuario) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_814_CALC_MES WHERE USUARIO_ID=").append(idUsuario);
		dataSet = db.get(dataSource, sb.toString());
	}
	/**
	 * lista los calculos efectuados por el usuario que solo sean de custodia de comisiones
	 * @param idUsuario
	 * @param transaccion
	 * @throws Exception
	 */
	public void listarCalculos(int idUsuario, String transacciones) throws Exception
	{
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_814_CALC_MES ");
		if (idUsuario!=0);
		sb.append("WHERE USUARIO_ID=").append(idUsuario);
		if(transacciones!=null && !transacciones.equals(""))
		sb.append(" AND TRANSA_ID='").append(transacciones).append("'");
		dataSet = db.get(dataSource, sb.toString());
	}

	
	/**Lista los detalles del cálculo. Arma un dataSet con el resultado
	 * @param idCalculo id referente al cálculo efectuado 
	 * @throws lanza una excepcion en caso de error al ejecutar la operación*/ 
	public void listarDetalles(long idCalculo) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_815_CALC_MES_DETALLE WHERE CALCULO_MES_ID=").append(idCalculo);
		sb.append(" ORDER BY CLIENT_ID,FECHA_INICIO_PAGO_CUPON");
		this.dataSet = db.get(this.dataSource, sb.toString());
	}
	
	/**Lista los detalles del cálculo. Arma un dataSet con el resultado. Tiene la diferencia de buscar 
	 * en tablas de cliente para mostrar nombre y rif 
	 * @param idCalculo id referente al cálculo efectuado 
	 * @throws lanza una excepcion en caso de error al ejecutar la operación*/ 
	public void listarDetallesEspciales(long idCalculo) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.*,B.CLIENT_CEDRIF,B.CLIENT_NOMBRE,C.TITULO_FE_VENCIMIENTO FROM INFI_TB_815_CALC_MES_DETALLE A, INFI_TB_201_CTES B,INFI_TB_100_TITULOS C WHERE CALCULO_MES_ID=").append(idCalculo);
		sb.append(" AND A.CLIENT_ID=B.CLIENT_ID AND A.TITULO_ID = C.TITULO_ID ORDER BY A.CLIENT_ID,A.FECHA_INICIO_PAGO_CUPON");
		this.dataSet = db.get(this.dataSource, sb.toString());
		System.out.println("listarDetallesEspciales "+sb);
	}	
	
	
	public Object moveNext() throws Exception {		
		return null;
	}	
}

    
