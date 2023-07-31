package com.bdv.infi.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import oracle.jdbc.rowset.OracleCachedRowSet;


/**
*
**/
public class DBO {


	/**
	 * Permite instanciar un DataSource
	 * @param dsName : nombre del DataSource a instanciar
	 * @return Objeto DataSource
	 * @throws Exception
	 */
	public static DataSource getDataSource(String dsName) throws Exception   {

		   	if (dsName==null)  throw new Exception("DataSource's Name NULL; Please the for datasource configuration parameters.");
		    	
		   	DataSource source = null;
		   	try   {
		   		Context ctx = new InitialContext();
		   		source = (DataSource) ctx.lookup( dsName );
		   		if (null == source)  {
		   			throw new Exception("JDBC DataSource not found; JNDI returned NULL");
		   		}
		   	} catch ( Exception e ) {
		   		e.printStackTrace();
		   		throw new Exception(e.getMessage() + " [" + dsName + "]");
		   	}
		   	return source;
	}
	
    /**
     * Metodo con el cual se ejecutan las consultas a Base de Datos. 
     * La conexion a la base de datos se efectua a traves de un DataSource, segun los siguiente :
     * 		1.-	Generado utilizando un nombre dado por WebService
     * 		2.-	Generado por las clases que lo invocan desde el ambiente Web
     * Utilizando dicha conexion genera un preparedStatement
     * @param dsName : nombre del Resource que apunta a la base de datos
     * @param dsoOrigen : DataSource
     * @param sql : Query a Ejecutar
     * @param data : Arreglo de Objetos
     * @return ResultSet juego de resultados de la consulta
     * @throws Throwable
     */
    public static ResultSet executeQuery (String dsName, DataSource dsoOrigen, String sql, Object[] data ) throws Exception  {
    	
    	if (dsName==null && dsoOrigen == null) {
        	throw new Exception("Nombre del DataSource JNDI Null o la dataCriterios es NULL");
    	}
    	DataSource dso  = null; 
    	
    	// Si el nombre tiene valor se debe buscar el DataSource
    	if (dsName != null) {
    		dso = getDataSource(dsName);
    	} else {
    		dso = dsoOrigen;
    	}
    	
    	if (sql==null)
    		throw new Exception("SQL es Null");
    	
		Connection con = null;
    	PreparedStatement pStm = null;
    	OracleCachedRowSet crs = new OracleCachedRowSet();
		try {
			con = dso.getConnection();
			pStm= con.prepareStatement(sql);

			//Recorrer todos los Objectos para realizar los set Correspondientes
			if (data == null) {
				data = new Object[0];
			}
			for (int i= 0; i<data.length; i++)	{
				//Strings
				if (data[i] instanceof String)
					pStm.setString((i+1),(String)data[i]);
				else if (data[i] instanceof StringBuffer)
					pStm.setString((i+1),((StringBuffer)data[i]).toString());
	
				//Numericos enteros
				else if (data[i] instanceof Integer)
					pStm.setInt((i+1),((Integer)data[i]).intValue());
				else if (data[i] instanceof Short)
					pStm.setShort((i+1),((Short)data[i]).shortValue());
				else if (data[i] instanceof Long)
					pStm.setLong((i+1),((Long)data[i]).longValue());		
				
				// Numericos con precision
				else if (data[i] instanceof Double)
					pStm.setDouble((i+1),((Double)data[i]).doubleValue());
				else if (data[i] instanceof Float)
					pStm.setFloat((i+1),((Float)data[i]).floatValue());
	
				// BigDecimal
				else if (data[i] instanceof BigDecimal)
					pStm.setBigDecimal((i+1),((BigDecimal)data[i]));
	
				//Fechas
				else if (data[i] instanceof java.util.Date) {
					java.util.Date date = (java.util.Date)data[i];
					long t = date.getTime();
					java.sql.Date sqlDate = new java.sql.Date(t);
					pStm.setDate((i+1),(sqlDate));
				}
				
				else if  (data[i] instanceof Timestamp)
					pStm.setTimestamp((i+1),(Timestamp)data[i]);
		
				else{
					System.err.println(data[i]+" "+i);
					throw new Exception("El tipo de datos especificado no se corresponde con un tipo de datos SQL "+data[i]+" "+i);
				}
			}		
			crs.populate(pStm.executeQuery());		
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Error en la consulta SQL" + e.getMessage(),e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error general en la Clase DBO, Descripcion: "+ e.getMessage(),e);
		} finally {
			if(pStm!=null)
				pStm.close();
			if(con!=null)
				con.close();
		}
		return crs;
	}
  
	
    /**
     * Metodo con el cual se ejecutan las actualizaciones  a Base de Datos, este recibe el nombre JNDI para obtener el 
     * DataSource y la conexion a la BD; Utilizando dicha conexion genera un preparedStatement
     * 
     * Para realizar los set´s de los campos del PreparedStatement utiliza el arreglo de
     * Objetos que recibe, comparando el instanceof de cada uno y realizando los setXXX dependiendo del 
     * grupo al cual pertenece la instancia del mismo.
     * 
     *  Grupos:
     *  	Metodo a Invocar setString
     *  	Objectos: java.lang.String 
     *  			  java.lang.StringBuffer
     *  
     *  	Metodo a Invocar setInt
     *  	Objectos: java.lang.Integer
     *  			  java.lang.Short
     *  
     *  	Metodo a Invocar setDouble
     *  	Objectos: java.lang.Double
     *  			  java.lang.Float
     *  			  java.lang.Long
	 *
     *  	Metodo a Invocar setDate
     *  	Objectos: java.sql.Date
     *  			
     *  	Metodo a Invocar setTimestamp
     *  	Objectos: java.sql.Timestamp
     * 	
     *  
     * @param dsName : Nombre JNDI DataSource
     * @param sql : Query a Ejecutar
     * @param data : Arreglo de Objetos
     * @return int Cantidad de registros afectado
     * @throws Throwable
     */
    public static int executeUpdate (String dsName, String sql, Object[] data ) throws Throwable {
    	
    	if (dsName==null) {
        	throw new Exception("Nombre del DataSource JNDI Null");
    	}
    	
    	DataSource dso  = null;     	
    	try {
       		dso = getDataSource(dsName);
		} catch (Exception e) {
			throw new Exception("Problemas con la generacion del DataSource");
		}
    	
    	
    	Connection con = null;
    	PreparedStatement pStm = null;
    	int result=0;
		try {
			con = dso.getConnection();
			pStm= con.prepareStatement(sql);
        	con.setAutoCommit(false);
        	
			if (data!=null)	{
				
				//Recorrer todos los Objectos para realizar los set Correspondientes			
				for (int i= 0; i<data.length; i++)
				{
					//Strings
					if (data[i] instanceof String)
						pStm.setString((i+1),(String)data[i]);
					else if (data[i] instanceof StringBuffer)
						pStm.setString((i+1),((StringBuffer)data[i]).toString());
		
					//Numericos enteros
					else if (data[i] instanceof Integer)
						pStm.setInt((i+1),((Integer)data[i]).intValue());
					else if (data[i] instanceof Short)
						pStm.setInt((i+1),((Short)data[i]).intValue());
		
					
					// Numericos con precision
					else if (data[i] instanceof Double)
						pStm.setDouble((i+1),((Double)data[i]).doubleValue());
					else if (data[i] instanceof Float)
						pStm.setDouble((i+1),((Float)data[i]).doubleValue());
					else if (data[i] instanceof Long)
						pStm.setDouble((i+1),((Long)data[i]).doubleValue());
		
		
					//Fechas
					else if (data[i] instanceof java.sql.Date)
						pStm.setDate((i+1),(java.sql.Date)data[i]);
					
					else if  (data[i] instanceof Timestamp)
						pStm.setTimestamp((i+1),(Timestamp)data[i]);
			
					else
						throw new Exception("El tipo de datos(" + data[i].getClass().getName()+") especificado en la posicion "+ (i+1)  + " no se corresponde con un tipo de datos SQL");
				}
			}
			
			result = pStm.executeUpdate();
		} catch (SQLException e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception("Error en la consulta SQL" + e.getMessage());
		}	catch (Exception e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception("Error general en la Clase DBO, Descripcion: "+ e.getMessage());
		} finally {
			if(pStm!=null)
				pStm.close();
			if(con!=null)
				con.close();
		}
		
		return result;
	}
    
    /**
    /**
     * Metodo con el cual se ejecutan actualizaciones  a la base de datos. 
     * La conexion a la base de datos se efectua a traves de un DataSource, segun los siguiente :
     * 		1.-	Generado utilizando un nombre dado por WebService
     * 		2.-	Generado por las clases que lo invocan desde el ambiente Web
     * Utilizando dicha conexion genera un preparedStatement
     * @param dsName : nombre del Resource que apunta a la base de datos
     * @param dsoOrigen : DataSource
     * @param data : Arreglo de Objetos
     * @return int Cantidad de registros afectado
     * @throws Throwable
     */
    public static int execBatchUpdate (String dsName, DataSource dsoOrigen, Object[][] data ) throws Throwable  {
           	
    	if ((dsName==null && dsoOrigen == null) && data == null) {
        	throw new Exception("Nombre del DataSource JNDI Null o la dataCriterios es NULL");
    	}
    	
    	DataSource dso  = null;     	
    	try {
        	// Si el nombre tiene valor se debe buscar el DataSource
        	if (dsName != null) {
        		dso = getDataSource(dsName);
        	} else {
        		dso = dsoOrigen;
        	}
		} catch (Exception e) {
			throw new Exception("Problemas con la generacion del DataSource");
		}
        	
    	Connection con = null;
    	PreparedStatement pStm = null;
    	int result=0, pos = 0;
		try {
       		con = dso.getConnection();
        	con.setAutoCommit(false);

			Integer maxFields = new Integer(0);
			for (pos = 0; pos<data.length; pos++) {
				
				pStm= con.prepareStatement((String)data[pos][0]);
				maxFields = (Integer) data[pos][1];
					
				//Recorrer todos los Objectos para realizar los set Correspondientes
				int col = 1;
				for (int i= 2; i<maxFields.intValue(); i++) {
	
					//Strings
					if (data[pos][i] instanceof String)
						pStm.setString((col),(String)data[pos][i]);
					else if (data[pos][i] instanceof StringBuffer)
						pStm.setString((col),((StringBuffer)data[pos][i]).toString());
					//Numericos enteros
					else if (data[pos][i] instanceof Integer)
						pStm.setInt((col),((Integer)data[pos][i]).intValue());
					else if (data[pos][i] instanceof Short)
						pStm.setShort((col),((Short)data[pos][i]).shortValue());
					else if (data[pos][i] instanceof Long)
						pStm.setLong((col),((Long)data[pos][i]).longValue());		
					
					// Numericos con precision
					else if (data[pos][i] instanceof Double)
						pStm.setDouble((col),((Double)data[pos][i]).doubleValue());
					else if (data[pos][i] instanceof Float)
						pStm.setFloat((col),((Float)data[pos][i]).floatValue());
					// BigDecimal
					else if (data[pos][i] instanceof BigDecimal)
						pStm.setBigDecimal((col),((BigDecimal)data[pos][i]));
					//Fechas
					else if (data[pos][i] instanceof java.util.Date) {
						java.util.Date date = (java.util.Date)data[pos][i];
						long t = date.getTime();
						java.sql.Date sqlDate = new java.sql.Date(t);
						pStm.setDate((col),(sqlDate));
					}
						
					else if  (data[pos][i] instanceof Timestamp)
						pStm.setTimestamp((col),(Timestamp)data[pos][i]);
			
					// Nulo
					else if (data[pos][i] == null)  
						pStm.setNull((col), java.sql.Types.VARCHAR);
					else{
						
						throw new Exception("El tipo de datos especificado no se corresponde con un tipo de datos SQL "+data[pos][i]+" "+i);
					}
					++col;
				}
				
				//System.out.println("SQL antes del pStm.executeUpdate();>>>"+data[pos][0]);
				result = pStm.executeUpdate();
				//System.out.println("SQL Despues del pStm.executeUpdate();>>>"+data[pos][0]);
			}
			con.commit();
		} catch (SQLException e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception("Error en la consulta SQL" + e.getMessage());
		} catch (Exception e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception("Error general en la Clase DBO, Descripcion: "+ e.getMessage());
		} finally {
			if(pStm!=null)
				pStm.close();
			if(con!=null)
				con.close();
		}
		return result;
	}    

	/**
	 * Recuperar el identificador de un registro basado en las secuencia generadas por
	 * cada identificador en la tabla SEQUENCE_NUMBERS
	 * @param dsName      : Nombre del DataSource a acceder
	 * @param dsoOrigen   : DataSource a acceder
	 * @param idTableName : nombre de la sequence a buscar
	 * @param cantidadRegistros : cantidad de registros a incrementar
	 * @return secuencia obtenida
	 * @throws Exception
	 */
	public static long dbGetSequence(String dsName, DataSource dsoOrigen, String idTableName, Integer cantidadRegistros) throws Exception {
		
		long sequence = 0;    	
        	
    	DataSource dso  = null;     	
    	try {
        	// Si el nombre tiene valor se debe buscar el DataSource
        	if (dsName != null) {
        		dso = getDataSource(dsName);
        	} else {
        		dso = dsoOrigen;
        	}
		} catch (Exception e) {
			throw new Exception("Problemas con la generacion del DataSource");
		}

		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		try {
						
			// define standard SQL commands
			StringBuffer sql1 = new StringBuffer();
			sql1.append("update SEQUENCE_NUMBERS set next_id = next_id + 1");
			sql1.append(" where table_name = '").append(idTableName).append("'");
			String sql2 = "select next_id from SEQUENCE_NUMBERS where table_name = '" + idTableName + "'";
			StringBuffer sql3 = new StringBuffer();
			if (cantidadRegistros.intValue() > 0) {
				sql3.append("update SEQUENCE_NUMBERS set next_id = next_id + ").append(cantidadRegistros);
				sql3.append(" where table_name = '").append(idTableName).append("'");
			}
			// get connection
			con = dso.getConnection();

			// increment value
			s = con.createStatement();
			s.executeUpdate(sql1.toString());

			// get value del primera secuencia
			rs = s.executeQuery(sql2);
			
			if (rs.next()) {
				sequence = rs.getLong(1);
			} else {
				throw new Exception(
						"No hay registro definido para la secuencia: " + idTableName + " en la tabla SEQUENCE_NUMBERS");
			}
			
			if (cantidadRegistros.intValue() == 0) {
				return sequence;
			}

			// increment value
			s = con.createStatement();
			s.executeUpdate(sql3.toString());
			
			return sequence;
		} catch (Exception e) {
			throw new Exception("dbGetSequence():" + e.getMessage());
		} finally {
			if (null != con) {
				if (null != rs) {
					rs.close();
				}
				if (null != s) {
					s.close();
				}
				con.close();
			}
		}
	}

}

