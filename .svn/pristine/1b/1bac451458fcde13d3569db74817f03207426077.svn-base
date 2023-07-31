package com.bdv.infi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.data.FechasCierre;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

public class FechasCierresDAO extends GenericoDAO {

	private Logger logger = Logger.getLogger(FechasCierresDAO.class);
	
	public FechasCierresDAO(DataSource ds) {
		super(ds);
	}

	public FechasCierresDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}
	
	/**Inserta las fechas de cierres
	 * @param fechasCierre objeto que contiene las fechas de cierre
	 * throws lanza una excepcion en caso de error*/
	private String[] insertar(FechasCierre fechasCierre) throws Exception{
		String[] registros = new String[2]; 
		String deleteRegistro = "DELETE FROM INFI_TB_706_FECHAS_CIERRE";
		StringBuffer sql = new StringBuffer();
		String fechaAnterior = this.formatearFecha(fechasCierre.getFechaCierreAnterior());
		String fechaProxima = this.formatearFecha(fechasCierre.getFechaCierreProximo());		
		//Borra los registros y los vuelve a insertar		
		sql.append("INSERT INTO INFI_TB_706_FECHAS_CIERRE (FECHA_CIERRE_ANTERIOR, FECHA_CIERRE_PROXIMO) ");
		sql.append(" VALUES(");
		sql.append("to_date('").append(fechaAnterior).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
		sql.append("to_date('").append(fechaProxima).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		sql.append(")");
		
		registros[0] = deleteRegistro;
		registros[1] = sql.toString();
		
		return registros;
	}
	
	/**Obtiene la fecha próxima de cierre
	 * @return devuelve el objeto con las fechas cargadas
	 * @throws lanza una excepcion si hay un error*/
	public FechasCierre obtenerFechas() throws Exception{
		FechasCierre fechasCierre = new FechasCierre();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT FECHA_CIERRE_ANTERIOR, FECHA_CIERRE_PROXIMO FROM INFI_TB_706_FECHAS_CIERRE ");		
		try {
			conn = this.dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			
			if (resultSet.next()){
				fechasCierre.setFechaCierreAnterior(resultSet.getDate("FECHA_CIERRE_ANTERIOR"));
				fechasCierre.setFechaCierreProximo(resultSet.getDate("FECHA_CIERRE_PROXIMO"));
			}else{
				throw new Exception("Error en la obtención de fechas de cierre");
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));			
			throw e;
		} finally{
			this.closeResources();
			this.cerrarConexion();
		}
		return fechasCierre;
	}
	
	/**Cierre el mes actual
	 * @throws lanza una excepcion en caso de error*/
	public String[] cerrarMes() throws Exception {
		FechasCierre fechasCierre = obtenerFechas();
		if (fechasCierre != null){
			fechasCierre.setFechaCierreAnterior(fechasCierre.getFechaCierreProximo());
			Calendar cal = GregorianCalendar.getInstance();			
			cal.setTime(fechasCierre.getFechaCierreProximo());			
			cal.add(Calendar.MONTH, +1);
			cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.getActualMaximum(Calendar.DATE));
			fechasCierre.setFechaCierreProximo(cal.getTime());
		}
		return insertar(fechasCierre);		
	}
	
	/**
	 * Agrega el registro de fechas de cierre a la tabla 706 si esta no tiene registros
	 * @return retorna un array de instrucciones SQL que se deben ejecutar si no hay registros en la tabla, 
	 * en caso contrario retorna null
	 * @throws Exception lanza una excepción en caso de error.
	 */
	public String[] insertarRegistro() throws Exception {
		FechasCierre fechasCierre = new FechasCierre();
		String[] retorno = null;
				
		String sql = "SELECT FECHA_CIERRE_ANTERIOR, FECHA_CIERRE_PROXIMO FROM INFI_TB_706_FECHAS_CIERRE ";
		System.out.println("SQL (Fecha de cierre): "+sql.toString());
		DataSet dsFecha = db.get(this.dataSource, sql);
		if (dsFecha.count()==0){
		   //Agrega las fechas de cierre
			Calendar cal = GregorianCalendar.getInstance();			
			cal.setTime(new Date());			
			cal.add(Calendar.MONTH, -1);
			cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.getActualMaximum(Calendar.DATE));
			fechasCierre.setFechaCierreAnterior(cal.getTime());
			
			//Fecha Próxima
			Calendar calp = GregorianCalendar.getInstance();			
			calp.setTime(new Date());			
			calp.set(calp.get(Calendar.YEAR),calp.get(Calendar.MONTH),calp.getActualMaximum(Calendar.DATE));
			fechasCierre.setFechaCierreProximo(calp.getTime());
			retorno = insertar(fechasCierre);
		}
		return retorno;
	}	


	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
