package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;


public class CalendarioDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public CalendarioDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public CalendarioDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}


    /**Lista todos los registros de días feriados que se encuentren en la tabla. Los resultados son almacenados
     * en un DataSet
     * @throws excepción en caso de error*/
	public void listarTodos() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CALEN_FECHA_NO_TRAB, CALEN_MOTIVO FROM INFI_VI_CALENDARIO");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	
    /**Busca los registros de la tabla de días feriados según la fecha recibida como parámetro. Los resultados
     * son almacenados en un DataSet
     * @param fecha fecha que se desea buscar
     * @throws excepción en caso de error*/
	public void listar(String fecha) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT CALEN_FECHA_NO_TRAB, CALEN_MOTIVO FROM INFI_VI_CALENDARIO WHERE CALEN_FECHA_NO_TRAB = to_date('"+fecha+"','" + ConstantesGenerales.FORMATO_FECHA + "')");
		dataSet = db.get(dataSource, sb.toString());
		
				
	}
	
	/**
	 * Verifica si un una fecha es feriada segun el calendario del sistema
	 * @param fecha
	 * @return true si es d&iacute;a feriado, false en caso contrario
	 * @throws Exception
	 */
	public boolean esDiaFeriado(String fecha) throws Exception{
		MonedaDAO monedaDAO=new MonedaDAO(dataSource);	
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT CALEN_FECHA_NO_TRAB, CALEN_MOTIVO FROM INFI_VI_CALENDARIO WHERE CALEN_FECHA_NO_TRAB = to_date('" + fecha + "','" + ConstantesGenerales.FORMATO_FECHA + "')");
			dataSet = db.get(dataSource, sb.toString());
			if (dataSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			//ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jul-16. Se valida si RENTA esta offline para permitir la continuidad de la operativa NM25287
			if(monedaDAO.rentabilidadOffline())	{
				return false;
			}else
			{
				throw e;
			}
		}
	}


	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
	

}
