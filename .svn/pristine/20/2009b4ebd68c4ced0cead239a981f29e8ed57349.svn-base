package com.bdv.infi.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.bdv.infi.util.Utilitario;

/**
 * <p>Representa una transacci&oacute;n para ser utilizada por los DAO.</p>
 * <p>Cada objeto que realize l&oacute;gica de negocio debe obtener una instancia de esta clase y utilizar la misma instancia en cada DAO que participaran en una transacci&oacute;n</p>
 */
public class Transaccion {
	
	private Logger logger = Logger.getLogger(Transaccion.class);
	
    /**
     * <p>Representa el pool de conexiones del cual se obtienen las conexiones a la base de datos</p>
     */
    private javax.sql.DataSource dataSource;

    /**
     * <p>Representa la conexi&oacute;n f&iacute;sica a la base de datos obtenida de un pool de conexiones</p>
     */
    private java.sql.Connection connection;

    /**
     * <p>Inicializa una nueva transacci&oacute;n</p>
     * @throws Exception ocurre un error inicializando la transacci&oacute;n
     */
    public void begin() throws Exception {
        /*Throwable t = new Throwable();
        StackTraceElement[] ste = t.getStackTrace();
        GenericLogManager.appLogger.info("-----------------------------Conexion abierta por: " + ste[1]);*/
        createConnection();

        //-------------------->
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
        	logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
            throw new Exception("No se pudo setear el autoCommit en falso", e);
        }
    }

    /**
     * <p>Realiza un commit de la transacci&oacute;n y la finaliza, se usa en caso de que no se hayan
     * capturado errores durante la interaccion con la BD, al finalizar cierra la conexi贸n</p>
     * @throws Exception si ocurre un error culminando la transacci贸n
     */
    public void end() throws Exception {
        try {
            if (connection != null) {
                /*
                Throwable t = new Throwable();
                StackTraceElement[] ste = t.getStackTrace();
                GenericLogManager.appLogger.info("-----------------------------Conexion cerrada por: " + ste[1]);*/

                connection.commit();
            }
        } catch (SQLException e) {
            throw new Exception("No se pudo realizar el commit", e);
        } finally {
            try {
                closeConnection();
            } catch (Exception de) {
            	logger.error(de.getMessage()+ Utilitario.stackTraceException(de));
                throw new Exception("No se pudo cerrar la conexi贸n", de);
            }

        }
    }

    /**
     * <p>Realiza un rollback de la transacci&oacute;n en caso de que se haya capturado un error
     * durante la interacci&oacute;n con la BD, al finalizar, cierra la transacci&oacute;n</p>
     * @throws Exception si ocurre un error realizando el rollback de la transacci&oacute;n
     */
    public void rollback() throws Exception {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new Exception("No se pudo hacer rollback", e);
        } finally {
            try {
                closeConnection();
            } catch (Exception e) {
            	logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
                throw new Exception("No se pudo cerrar la conexi&oacute;n", e);
            }
        }
    }

    /**
     * <p>Cierra la conexi&oacute;n con el repositorio de datos</p>
     * @throws Exception si no se pudo cerrar la conexi&oacute;n
     */
    public void closeConnection() throws Exception {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new Exception("No se pudo cerrar la conexi&oacute;n", e);
        } finally {
            connection = null;
        }
    }

    /**
     * <p>Crea la conexi贸n al repositorio de datos</p>
     * @throws Exception si hubo un error creando la conexi贸n
     */
    private void createConnection() throws Exception {
        try {
            if (dataSource != null) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            throw new Exception("No se pudo crear la conexi&oacute;n", e);
        }
    }

    /**
     * <p>Retorna la conexi&oacute;n relacionada a esta Transacci&oacute;n</p>
     */
    public Connection getConnection(){
        return connection;
    }

    /**
     * <p>Crea un objeto Transaction</p>
     * @param ds dataSource que sera utilizado para crear conexiones al repositorio de datos
     * @param config Configuraci贸n de la aplicaci&oacute;n
     */
    public Transaccion(DataSource ds) {
        this.dataSource = ds;
    }
    
    /**Obtiene el DataSource usado*/
    public DataSource getDataSource(){
    	return dataSource;
    }
    
    /**Ejecuta las instrucciones SQL sobre la conexi髇 abierta
     * @param consultasSQL conjunto de instrucciones sql a ejecutar
     * @throws lanza una exception si hay un error*/
    public void ejecutarConsultas(String[] consultasSQL) throws Exception{    	
        if (connection != null) {
        	int i=0;
        	Statement statement = connection.createStatement();
    		try {        	
	        	for (i=0; i < consultasSQL.length; i++){
	        		statement.execute(consultasSQL[i]);
	        	}
    		} catch (Exception e) {  
    			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));  			
				throw e;
			} finally{
				if (statement != null){
					statement.close();
				}
			}
    	}
    }
    
    /**Ejecuta las instrucciones SQL sobre la conexi髇 abierta
     * @param consultaSQL instrucci髇 sql a ejecutar
     * @throws lanza una exception si hay un error*/
    public void ejecutarConsultas(String consultaSQL) throws Exception{
        if (connection != null) {
        	Statement statement = null;
        	try {
				statement = connection.createStatement();
				statement.execute(consultaSQL);
			} catch (Exception e) {
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
				logger.error("Error SQL: " + consultaSQL);
				throw e;				
			} finally{
				if (statement != null){
					statement.close();
				}
			}
			
        }
    }    
}
