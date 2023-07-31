package com.bdv.infi_toma_orden.dao;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi_toma_orden.data.Titulo;

/** 
 * Clase que contiene la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y b&uacute;squeda de los datos de la data t&iacute;tulos (TB_100_TITULOS)
 */
public class TitulosDAO extends GenericoDAO {

	/*public TitulosDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}*/
	
	/*public TitulosDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}*/
	
	/**
	 * Constructor de la clase
	 * @param nombreDataSource : nombre que se obtiene del ambiente de ejecucion de los WebService
	 * @param dso : DataSource instanciado por clases que se ejecutan en ambientes Web
	 */
	public TitulosDAO (String nombreDataSource, DataSource dso) {
		super(nombreDataSource, dso);
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**Metodo que retorna un dataset con los nombres de los titulos, pueden ser 1 o varios
	 * @param titulos Id del titulo que se desea buscar, es un string con uno o varios id
	 * se anexan al query en una condicion -> IN*/
	
	public ArrayList listarFechaCupon(String idTitulos,String fechaValor) throws Exception{
		
		ArrayList<String> retorno = new ArrayList<String>();     
		StringBuffer sql = new StringBuffer();
			
		sql.append("select to_char(intstrtdte,'DD-MM-YYYY') intstrtdte,intrate_8 tasa, to_char(INTENDDTE,'dd-MM-yyyy') as enddate  from secs where secid ='").append(idTitulos).append("'");
		sql.append(" and (intenddte>=to_date('"+fechaValor);
		sql.append("','dd-mm-yyyy')");
		sql.append(" and intstrtdte <=to_date('"+fechaValor);
		sql.append("','dd-mm-yyyy'))");
		//System.out.println("SQL(listarFechaCupon): "+sql.toString());
		try {
			conn = dso.getConnection();			
			statement = conn.createStatement();
			resultQuery = statement.executeQuery(sql.toString());			
			while(resultQuery.next()){
				retorno.add(resultQuery.getString("intstrtdte"));
				retorno.add(resultQuery.getString("tasa"));
				retorno.add(resultQuery.getString("enddate"));
			}
			System.out.println("listarFechaCupon "+sql);
		} catch (Exception e) {
			throw new Exception("Error al Fecha Inicio del Cupon: "+e);
		} finally {
			if (resultQuery != null){
				resultQuery.close();
				cerrarConexion();
			}
		}
		return retorno;
	}

	/**Metodo que retorna un dataset con los nombres de los titulos, pueden ser 1 o varios
	 * @param titulos Id del titulo que se desea buscar, es un string con uno o varios id
	 * se anexan al query en una condicion -> IN*/
	
	public ArrayList listarTitulos(String idTitulos) throws Exception{
		/*StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.titulo_id, t.titulo_descripcion, t.titulo_valor_nominal FROM INFI_TB_100_TITULOS t WHERE titulo_id IN (").append(idTitulos).append(")");
		dataSet = db.get(dataSource, sb.toString());*/
		ArrayList<String> listaTitulos = new ArrayList<String>();     
		
		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT titulo_id,couprate_8,titulo_descripcion,titulo_valor_nominal, to_char(titulo_fe_emision,'" + ConstantesGenerales.FORMATO_FECHA+"') as fecha_emision,basis,titulos_precio_mercado,intcalcrule FROM INFI_TB_100_TITULOS WHERE titulo_id IN ('").append(idTitulos).append("')");
		//NM29643 - INFI_TTS_466
		sql.append("SELECT titulo_id,couprate_8,titulo_descripcion,titulo_valor_nominal, to_char(titulo_fe_emision,'"+ConstantesGenerales.FORMATO_FECHA+"') as fecha_emision, to_char(TITULO_FE_VENCIMIENTO,'"+ConstantesGenerales.FORMATO_FECHA+"') as fecha_vencimiento, basis, titulos_precio_mercado, intcalcrule FROM INFI_TB_100_TITULOS WHERE titulo_id IN ('").append(idTitulos).append("')");
		
		try {
			conn = dso.getConnection();			
			statement = conn.createStatement();
			System.out.println("SQL (listarTitulos): "+sql.toString());
			resultQuery = statement.executeQuery(sql.toString());			
			while(resultQuery.next()){
				//Titulo titulo = new Titulo();
				listaTitulos.add(resultQuery.getString("titulo_id"));
				listaTitulos.add(resultQuery.getString("couprate_8"));
				listaTitulos.add(resultQuery.getString("titulo_descripcion"));
				listaTitulos.add(resultQuery.getString("titulo_valor_nominal"));
				listaTitulos.add(resultQuery.getString("fecha_emision"));
				listaTitulos.add(resultQuery.getString("basis"));
				listaTitulos.add(resultQuery.getString("titulos_precio_mercado"));
				listaTitulos.add(resultQuery.getString("intcalcrule"));
				listaTitulos.add(resultQuery.getString("fecha_vencimiento"));
			} 

		} catch (Exception e) {
			throw new Exception("Error al buscar los t&iacute;tulos");
		} finally {
			if (resultQuery != null){
				resultQuery.close();
				cerrarConexion();
			}
		}
		return listaTitulos;
	}	
}
