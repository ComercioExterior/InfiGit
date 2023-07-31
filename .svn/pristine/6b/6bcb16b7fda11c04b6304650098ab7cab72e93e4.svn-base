package com.bdv.infi.dao;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.data.FechaValor;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;


/**
 * Clase que permite el ingreso y recuperación de registros de la tabla
 * INFI_TB_046_FECHA_VALOR
 */
public class FechaValorDAO extends GenericoDAO {

	public FechaValorDAO(DataSource ds) {
		super(ds);
	}

	public FechaValorDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}

	/**
	 * Modifica los valores del registro recibido
	 * 
	 * @param fechaValor
	 *            objeto que contiene los datos a modificar
	 * @return SQL que se debe ejecutar
	 */
	public String modificar(FechaValor fechaValor) {
		StringBuffer consulta = new StringBuffer();
		consulta.append("UPDATE INFI_TB_046_FECHA_VALOR ");
		consulta.append(" SET FECHA_VALOR=").append(
				formatearFechaBD(fechaValor.getFechaValor()));
		consulta.append(" WHERE FECHA_VALOR_ID=").append(
				fechaValor.getIdFechaValor());
		return consulta.toString();
	}

	/**
	 * Lista todas las fechas valores encontradas en la tabla
	 * INFI_TB_046_FECHA_VALOR Genera un dataSet con la información encontrada
	 */
	public void listar() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_046_FECHA_VALOR");
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Lista la fecha valor encontrada según el id recibido. Si la fecha es menor a la actual, retorna la fecha actual
	 * @param idFecha
	 *            id de la fecha valor que se debe buscar
	 * @return FechaValor objeto con la información encontrada
	 * @throws lanza una exception en caso de error
	 */
	public FechaValor listar(int idFecha) throws Exception {
		StringBuffer sql = new StringBuffer();
		FechaValor fechaValor = new FechaValor();
		Date fechaActual = new Date();
		
		sql.append("SELECT FECHA_VALOR_ID,FECHA_VALOR_NOMBRE,FECHA_VALOR ");
		sql.append("FROM INFI_TB_046_FECHA_VALOR WHERE FECHA_VALOR_ID=").append(idFecha);
		try {
			conn = this.dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			if (resultSet.next()) {
				fechaValor.setIdFechaValor(resultSet.getInt("FECHA_VALOR_ID"));
				fechaValor.setNombre(resultSet.getString("FECHA_VALOR_NOMBRE"));
				fechaValor.setFechaValor(resultSet.getDate("FECHA_VALOR").compareTo(fechaActual)<0?fechaActual:resultSet.getDate("FECHA_VALOR"));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
		return fechaValor;
	}
	
	/**
	 * Retorna la fecha valor original configurada en BD de acuerdo al ID indicado. Retorna un objeto FechaValor 
	 * @param idFecha
	 * @return FechaValor
	 * @throws Exception
	 */
	public FechaValor listarFechaConfiguradaEnBD(int idFecha) throws Exception {
		StringBuffer sql = new StringBuffer();
		FechaValor fechaValor = new FechaValor();
				
		sql.append("SELECT FECHA_VALOR_ID,FECHA_VALOR_NOMBRE,FECHA_VALOR ");
		sql.append("FROM INFI_TB_046_FECHA_VALOR WHERE FECHA_VALOR_ID=").append(idFecha);
		try {
			conn = this.dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			if (resultSet.next()) {
				fechaValor.setIdFechaValor(resultSet.getInt("FECHA_VALOR_ID"));
				fechaValor.setNombre(resultSet.getString("FECHA_VALOR_NOMBRE"));
				fechaValor.setFechaValor(resultSet.getDate("FECHA_VALOR"));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
		return fechaValor;
	}

	
	/**
	 * Lista la fecha valor encontrada según el id recibido. Genera un dataSet
	 * @param idFecha id de la fecha valor que se debe buscar
	 * @throws lanza una exception en caso de error
	 */	
	public void listarFecha(int idFecha) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT FECHA_VALOR_ID,FECHA_VALOR_NOMBRE,FECHA_VALOR ");
		sql.append("FROM INFI_TB_046_FECHA_VALOR WHERE FECHA_VALOR_ID=").append(idFecha);
		dataSet = db.get(dataSource, sql.toString());
	}
	
/**
 * Lista todas las fechas valor que se encuentra en base de datos
 * @return HashMap para recorrer y obtener la fecha valor deseada
 * @throws Exception
 */	
	public HashMap listarFecha() throws Exception {
		StringBuffer sql 			  = new StringBuffer();
		HashMap<String,Date> parametros = new HashMap<String,Date>();
		SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA2);
		Date fechaActual = new Date();
		
		sql.append("SELECT FECHA_VALOR_ID,to_char(FECHA_VALOR,'"+ConstantesGenerales.FORMATO_FECHA2 +"')fecha_valor FROM INFI_TB_046_FECHA_VALOR");

		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()>0){
			dataSet.first();
			while(dataSet.next()){
				parametros.put(dataSet.getValue("FECHA_VALOR_ID"),formatoFecha.parse(dataSet.getValue("FECHA_VALOR")).compareTo(fechaActual)<0?fechaActual:formatoFecha.parse(dataSet.getValue("FECHA_VALOR")));
			}//FIN WHILE
		}//FIN IF
		
		return parametros;
	}//FIN METODO

	@Override
	public Object moveNext() throws Exception {
		return null;
	}

	/***
	 * Lista la fecha Valor segun el nombre de la fecha valor que se recibe
	 * @param nombreFechaValor
	 * @throws Exception
	 */
	public void listarFechaValor(String nombreFechaValor) throws Exception{
		StringBuffer sql= new StringBuffer();
		StringBuffer filtro= new StringBuffer("");
		
		sql.append("select fecha_valor_id,fecha_valor_nombre, to_char(fecha_valor,'DD-MM-YYYY') fecha_valor from INFI_TB_046_FECHA_VALOR");
		if(nombreFechaValor!=null && !nombreFechaValor.equals(""))
			filtro.append(" where upper (INFI_TB_046_FECHA_VALOR.fecha_valor_nombre)like upper('%").append(nombreFechaValor).append("%')");
		sql.append(filtro);	
		dataSet = db.get(dataSource, sql.toString());
	}
}
