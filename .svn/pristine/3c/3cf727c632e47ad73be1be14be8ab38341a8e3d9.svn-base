package com.bdv.infi_toma_orden.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sql.DataSource;
import javax.sql.RowSet;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;


/**
 * <p>
 * Clase que contiene metodos genericos de conexión y liberación de recursos a
 * la base de datos. Por cualquier objeto que, a su vez puede tener acceso a
 * varias tablas de base de datos, se debe crear una clase que extienda esta
 * clase.
 * </p>
 */
public abstract class GenericoDAO {

	/**
	 * Nombre del DataSource
	 */
	protected String nombreDataSource;
	/**
	 * DataSource a utilizar si el cliente no es un WebService
	 */
	protected DataSource dso;
	/**
	 *  ResulSet desconecta usado para recuperar la informacion resultado del query
	 */
	protected RowSet resultSet = null;
	
	/**
	 * <p>
	 * Conexion a la base de datos
	 * </p>
	 */
	protected Connection conn;

	/**
	 * <p>
	 * Representa el conjuto de resultados retornados por una consulta
	 * determinada
	 * </p>
	 */
	protected java.sql.ResultSet resultQuery;
	
	/**
	 * <p>
	 * Representa la sentencia a ajecutar contra el repositorio de datos
	 * </p>
	 */
	protected java.sql.PreparedStatement preparedStatement;


	/**
	 * <p>
	 * Representa la sentencia a ajecutar contra el repositorio de datos
	 * </p>
	 */
	protected java.sql.Statement statement;	


	
	/**
	 * Constructor de la clase
	 * @param ds : DataSource a utilizar para acceder a la base de datos
	 */
	public GenericoDAO (String nombreDataSource, DataSource dso) {
		this.nombreDataSource = nombreDataSource;
		this.dso = dso;
	}
	
	

	/**
	 * Formatea una fecha en base a las constantes establecidas en el sistema
	 * @param fecha : Objeto fecha que debe ser formateado
	 * @return fecha formateada
	 **/
	protected String formatearFecha(Date fecha){
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		return formatoDeFecha.format(fecha);
	}
	
	/**
	 * Formatea una fecha y hora en base a las constantes establecidas en el sistema. 
	 * El valor de retorno estará listo para ser usado en la consulta insert que se está preparando con
	 * la función de oracle to_date
	 * @param fecha : Objeto fecha que debe ser formateado
	 * @return un string con el siguiente formato to_date('dd/mm/yyyy','dd/mm/yyyy')
	 **/
	protected String formatearFechaBD(Date fecha){
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		return "to_date('"+formatoDeFecha.format(fecha)+"','"+ConstantesGenerales.FORMATO_FECHA+"')";
	}
	
	/**
	 * Envia una cadena lista para ser insertada en la consulta que se está construyendo con la fecha de la base de datos.
	 * La máscara usada será la configurada en las constantes del sistema 
	 * El valor de retorno sera el siguiente to_date(SYSDATE,'mascaraConfigurada(dd/mm/yyyy')
	 * @return un string con la función de to_date usada en oracle*/
	protected String formatearFechaBDActual(){
		return "to_date(SYSDATE,'"+ConstantesGenerales.FORMATO_FECHA_SYSDATE+"')";
	}
	
	/**Cierra la conexi&oacute;n usada por el dao*/
	public void cerrarConexion() throws SQLException{
		if (conn != null){
			conn.close();
		}
	}
	
	
	/**
	 * <p>
	 * Cierra el Statement,ResultSet asociados a este objeto
	 * </p>
	 */
	public void closeResources() {
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
