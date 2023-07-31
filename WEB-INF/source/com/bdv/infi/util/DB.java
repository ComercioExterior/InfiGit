package com.bdv.infi.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.sun.rowset.CachedRowSetImpl;



/**
*
**/
public class DB {

    
    /**
     * Metodo con el cual se ejecutan las consultas a Base de Datos. 
     * La conexion a la base de datos se efectua a traves de un DataSource, segun los siguiente :
     * 		1.-	Generado utilizando un nombre dado por WebService
     * 		2.-	Generado por las clases que lo invocan desde el ambiente Web
     * Utilizando dicha conexion genera un preparedStatement
     * @param  dsName : nombre del Resource que apunta a la base de datos
     * @param dso : DataSource
     * @param sql Query a Ejecutar
     * @param data Arreglo de Objetos
     * @return RowSet juego de resultados de la consulta
     * @throws Throwable
     */
    public static RowSet executeQuery (DataSource dsoOrigen, String sql, Object[] data ) throws Exception  {
    	
    	if (dsoOrigen == null || data == null) {
        	throw new Exception("Nombre del DataSource JNDI Null o la data es NULL");
    	}
    	DataSource dso  = null; 
    	
    	// Si el nombre tiene valor se debe buscar el DataSource
    	//if (dsName != null) {
    		// dso = getDataSource(dsName);
    	//} else {
    		dso = dsoOrigen;
    	//}
    	
    	if (sql==null)
    		throw new Exception("Valor de comando SQL  Null");
    	
		Connection con = null;
    	PreparedStatement pStm = null;
    	ResultSet rs=null;
    	CachedRowSet crs = null;
		try {
			con = dso.getConnection();
			pStm= con.prepareStatement(sql);

			//Recorrer todos los Objectos para realizar los set Correspondientes			
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
				else if (data[i] instanceof java.sql.Date)
					pStm.setDate((i+1),(java.sql.Date)data[i]);
				
				else if  (data[i] instanceof Timestamp)
					pStm.setTimestamp((i+1),(Timestamp)data[i]);
		
				else{
					System.err.println(data[i]+" "+i);
					throw new Exception("El tipo de datos especificado no se corresponde con un tipo de datos SQL "+data[i]+" "+i);
				}
			}
			
			rs = pStm.executeQuery();
			crs = new CachedRowSetImpl();
			
			crs.populate(rs);
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Error en la consulta SQL" + e.getMessage(),e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error general en la Clase DB, Descripcion: "+ e.getMessage(),e);
		} finally {
			if(rs!=null)
				rs.close();
			if(pStm!=null)
				pStm.close();
			if(con!=null)
				con.close();
		}
		return crs;
	}

    /**
     * 
     * @param dsName
     * @param dsoOrigen
     * @param data
     * @return
     * @throws Throwable
     */
    public static int execBatchUpdate (DataSource dsoOrigen, Object[][] data, TomaOrdenSimulada orden ) throws Throwable  {
           	
    	if (dsoOrigen == null || data == null) {
        	throw new Exception("Nombre del DataSource JNDI Null o la data es NULL");
    	}
    	
    	DataSource dso  = null;     	
    	try {
        	// Si el nombre tiene valor se debe buscar el DataSource
       		dso = dsoOrigen;

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
			//<data.length
			for (pos = 0; pos<data.length; pos++) {
			
				pStm= con.prepareStatement((String)data[pos][0]);
				//System.out.println("Query: "+(String)data[pos][0]);
				maxFields = (Integer) data[pos][1];
				
				//Recorrer todos los Objectos para realizar los set Correspondientes
				int col = 1;
				for (int i= 2; i<maxFields.intValue(); i++) {
					//System.out.println("datos["+pos+"]["+i+"]: "+data[pos][i]);
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
					
					else if  (data[pos][i] instanceof Timestamp){						
						java.util.Date date = (java.util.Date)data[pos][i];							
						long t = date.getTime();
						java.sql.Timestamp sqlDate = new java.sql.Timestamp(t);				
						pStm.setTimestamp((col),sqlDate);						
					}

					else if (data[pos][i] instanceof java.util.Date) {
						java.util.Date date = (java.util.Date)data[pos][i];							
						long t = date.getTime();
						java.sql.Date sqlDate = new java.sql.Date(t);						
						pStm.setDate(col,(sqlDate));
					}
					
					// Nulo
					else if (data[pos][i] == null)  
						pStm.setNull((col), java.sql.Types.VARCHAR);

					else{
						System.err.println(data[pos][i]+" "+i);
						throw new Exception("El tipo de datos especificado no se corresponde con un tipo de datos SQL "+data[pos][i]+" "+i);
					}
					
					++col;
				}
				result = pStm.executeUpdate();
			}
			
			//guardarDocumentos(orden, con);
			con.commit();
		} catch (SQLException e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception(e);
		} catch (Exception e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception("Error general en la Clase DB, Descripcion: "+ e.getMessage());
		} finally {
			if(pStm!=null)
				pStm.close();
			if(con!=null)
				con.close();
		}
		return result;
	}    
    /**
     * 
     * @param dsName
     * @param dsoOrigen
     * @param data
     * @return
     * @throws Throwable
     */
    public static int execBatchUpdate (DataSource dsoOrigen, Object[][] data, TomaOrdenSimulada orden, ArrayList<String> sentencias ) throws Throwable  {
           	
    	if (dsoOrigen == null || data == null) {
        	throw new Exception("Nombre del DataSource JNDI Null o la data es NULL");
    	}
    	
    	DataSource dso  = null;     	
    	try {
        	// Si el nombre tiene valor se debe buscar el DataSource
       		dso = dsoOrigen;

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
			//<data.length
			for (pos = 0; pos<data.length; pos++) {
			
				pStm= con.prepareStatement((String)data[pos][0]);
				//System.out.println("Query: "+(String)data[pos][0]);
				maxFields = (Integer) data[pos][1];
				
				//Recorrer todos los Objectos para realizar los set Correspondientes
				int col = 1;
				for (int i= 2; i<maxFields.intValue(); i++) {
					//System.out.println("datos["+pos+"]["+i+"]: "+data[pos][i]);
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
					
					else if  (data[pos][i] instanceof Timestamp){						
						java.util.Date date = (java.util.Date)data[pos][i];							
						long t = date.getTime();
						java.sql.Timestamp sqlDate = new java.sql.Timestamp(t);				
						pStm.setTimestamp((col),sqlDate);						
					}

					else if (data[pos][i] instanceof java.util.Date) {
						java.util.Date date = (java.util.Date)data[pos][i];							
						long t = date.getTime();
						java.sql.Date sqlDate = new java.sql.Date(t);						
						pStm.setDate(col,(sqlDate));
					}
					
					// Nulo
					else if (data[pos][i] == null)  
						pStm.setNull((col), java.sql.Types.VARCHAR);

					else{
						System.err.println(data[pos][i]+" "+i);
						throw new Exception("El tipo de datos especificado no se corresponde con un tipo de datos SQL "+data[pos][i]+" "+i);
					}
					
					++col;
				}
				result = pStm.executeUpdate();
			}
			OrdenDAO ordenDAO = new OrdenDAO(dso);
			if(result > 0 &&sentencias!=null&&sentencias.size()>0){				
				if(!ordenDAO.ejecutarStatementsBatchBool(sentencias)){
					//logger.error("Error insertando la instruccion de pago de la orden "+beanTOSimulada.getIdOrden());
					result = 0;
					con.rollback();
					throw new Exception("Error al insertar la intruccion de pago de la orden ");
					
				}
			}
			//guardarDocumentos(orden, con);
			con.commit();
		} catch (SQLException e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception(e);
		} catch (Exception e) {
			if(con!=null)
				con.rollback();
			e.printStackTrace();
			throw new Exception("Error general en la Clase DB, Descripcion: "+ e.getMessage());
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
	 * @param ds          : DataSource a acceder
	 * @param idTableName : nombre de la sequence a buscar
	 * @return secuencia obtenida
	 * @throws Exception
	 */
	public static long dbGetSequence(DataSource dsoOrigen, String idTableName, Integer cantidadRegistros) throws Exception {
		
		long sequence = 0;    	
        	
    	DataSource dso  = null;     	
    	try {
       		dso = dsoOrigen;

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
       		con = dso.getConnection();
			
			// con = dso.getConnection();

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
		
	
	/**
	 * <p>
	 * Cierra el Statement,ResultSet
	 * </p>
	 */
	public static void closeResources(ResultSet resultSet, Statement statement) {
		// Cierra el resultSet en caso de que este sea distinto de null,
		// y le asigna null para que lo tome el GC
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.out
						.println("No se pudo cerrar el objeto de B/D:  ResultSet."
								+ e.getMessage());
			}
			resultSet = null;
		}

		// Cierra el statement en caso de que este sea distinto de null,
		// y le asigna null para que lo tome el GC
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out
						.println("No se pudo cerrar el objeto de B/D:  Statement. "
								+ e.getMessage());
			}
			statement = null;
		}
	}


}

