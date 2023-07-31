package com.bdv.infi.dao;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import megasoft.db;
import com.bdv.infi.data.Pais;
import com.bdv.infi.util.Utilitario;



/** 
 * Clase que se conecta con la base de datos y busca los paises)
 */
public class PaisesDAO extends com.bdv.infi.dao.GenericoDAO {

	private Logger logger = Logger.getLogger(PaisesDAO.class);
	public PaisesDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public PaisesDAO(DataSource ds) throws Exception {
		super(ds);
	}

	/**Lista todos los países encontrados en tabla. Crea un DataSet con la información encontrada
	 * @throws lanza una exception si hay algún error*/
	public void listar() throws Exception{			
		String sb = "SELECT * FROM INFI_VI_PAISES ";
		dataSet = db.get(this.dataSource, sb);
		
	}	
	
	/**Lista la descripción del país según el código recibido en el parámetro
	 * @param id id del pais a buscar
	 * @throws lanza una exception si hay algún error
	 * @return Pais objeto pais con su id y descripción o null si el código no fue encontrado*/
	public Pais listar(String id) throws Exception{
	
		Pais pais = null;		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT * FROM INFI_VI_PAISES WHERE TAB_CAMPO_CLAVE ='").append(id).append("'");
			conn = this.dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sb.toString());
			pais = (Pais) moveNext();			
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
		return pais;
	}
	

	/**Obtiene el próximo registro en la consulta efectuada*/
	public Object moveNext() throws Exception {
		Pais pais = null;
		if (resultSet.next()){
			String idPais = resultSet.getString("TAB_CAMPO_CLAVE");
			String descripcion = resultSet.getString("TAB_DESCRIPCION");
			pais = new Pais(idPais,descripcion);
		}
		return pais;
	}
/**
 * Lista los paises segun el nombre del pais que recibe
 * @param descripcion
 * @throws Exception
 */
	public void listarPaises(String descripcion) throws Exception{
	StringBuffer sql= new StringBuffer();
	sql.append("SELECT * FROM  INFI_VI_PAISES");
	if(descripcion!=null && !descripcion.equals(""))
	sql.append(" WHERE UPPER (INFI_VI_PAISES.TAB_DESCRIPCION)LIKE UPPER('%").append(descripcion).append("%')");
	sql.append(" ORDER BY TAB_DESCRIPCION");
	dataSet =db.get(dataSource, sql.toString());
	}
	
	/**
	 * Obtiene el código internacional del país dado un id numerico
	 * @param codigoNumericoPais
	 * @throws Exception 
	 */
	public String obtenerCodigoISOPais(String codigoNumericoPais) throws Exception{
		//Buscamos la nacionalidad por medio del id que retorna el servicio, enviamos la descripción
		String nombrePais="";
		PaisesDAO paisesDAO = new PaisesDAO(dataSource);
		Pais pais = paisesDAO.listar(codigoNumericoPais);
		if(pais!=null){
			nombrePais = pais.getPaises_descripcion();//OJO OBTENER CODIGO ISO INTERNACIONAL
			//nombrePais = nombrePais.replace("0", "");
			//nombrePais = nombrePais.replace("  S", "");
			nombrePais = nombrePais.trim();
		}
		return nombrePais;
	}

}
