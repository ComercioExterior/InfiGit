package com.bdv.infi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.Util;
import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * <p>
 * Clase que contiene metodos genericos de conexi&oacute;n y liberaci&oacute;n de recursos a
 * la base de datos. Por cualquier objeto que, a su vez puede tener acceso a
 * varias tablas de base de datos, se debe crear una clase que extienda esta
 * clase.
 * </p>
 */
public abstract class GenericoDAO {

	/**
	 * Total de registros de la consulta.
	 */
	private int totalDeRegistros = 0;
	
	/**
	 * <p>
	 * Representa un DataSet que ser&aacute; usado en las consultas lista que no
	 * devuelvan un objeto.
	 * </p>
	 * <p>
	 * Se usa mayormente para desplegar informaci&oacute;n en pantalla
	 * </p>
	 */
	protected DataSet dataSet = new DataSet();

	/**
	 * <p>
	 * Representa el conjuto de resultados retornados por una consulta
	 * determinada
	 * </p>
	 */
	protected java.sql.ResultSet resultSet;

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
	 * <p>
	 * Transacci&oacute;n que utilizara este objeto DAO
	 * </p>
	 */
	protected Transaccion transaccion;

	/**
	 * <p>
	 * Conexion a la base de datos
	 * </p>
	 */
	protected Connection conn;

	/**
	 * <p>
	 * DataSource usado
	 * </p>
	 */
	protected DataSource dataSource;

	/**
	 * <p>
	 * Crea un objeto tipo GenericDAO
	 * </p>
	 * 
	 * @param transanccion
	 *            La transacci&oacute;n que sera utilizada por este DAO
	 * @throws Exception
	 *             si el parametro es nulo
	 */
	public GenericoDAO(Transaccion transanccion) throws Exception {
		// Se valida el par&aacute;metro recibido
		if (transanccion == null) {
			throw new Exception("Objeto Transaction inv&aacute;lido");
		}
		this.transaccion = transanccion;
		conn = this.transaccion.getConnection();
		dataSource = transaccion.getDataSource();
	}

	/**
	 * <p>
	 * Crea un objeto tipo GenericDAO. Establece el dataSource para ser
	 * utilizado en las consultas
	 * </p>
	 * 
	 * @param DataSource
	 *            que ser&aacute; utilizado por el DAO
	 */
	public GenericoDAO(DataSource ds) {
		// Se valida el par&aacute;metro recibido
		dataSource = ds;
	}

	/**
	 * <p>
	 * Retorna el pr&oacute;ximo objeto resultado de haber realizado una consulta a
	 * traves de los m&eacute;todos listar
	 * </p>
	 * 
	 * @return El objeto obtenido resultado de moverse a la pr&oacute;xima fila del
	 *         resultSet
	 * @throws Exception
	 *             si hubo un error iterando en el ResulsetSet
	 */
	public abstract Object moveNext() throws Exception;

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
				Logger.error(this,"No se pudo cerrar el objeto de B/D:  ResultSet."
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
				Logger.error(this,"No se pudo cerrar el objeto de B/D:  Statement. "
								+ e.getMessage());
			}
			statement = null;
		}
		
  	    if (preparedStatement != null){
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				Logger.error(this,"No se pudo cerrar el objeto de B/D:  preparedStatement. "
								+ e.getMessage());
			}
			preparedStatement = null;
		}
  	    
  	    
		
	}

	/** Devuelve el dataset */
	public DataSet getDataSet() {
		return dataSet;
	}
	
	/** Asigna el dataset */
	public DataSet setDataSet(DataSet ds) {
		return this.dataSet = ds;
	}
	
	/**
	 * Este método fue creado por Alexander Rincon NM11383 14/03/2018
	 * El mismo devuelve el Id de Ciclo de un proceso que se encuentra en ejecución de acuerdo 
	 * al campo fecha y status de la tabla  INFI_TB_803_CONTROL_ARCHIVOS
	 * @param dsp
	 * @param  procesoEje estatus o Proceso ejem 'CICLO_BATCH_DICOM'
	 * @return sql El número de Ciclo que se esta ejecutando según la fecha y el estatus o Proceso ejem 'CICLO_BATCH_DICOM'
	 * @throws Exception
	 */
	public synchronized static String dbGetCicloProc(DataSource dsp, String procesoEje, String fecProceso)
	throws Exception {
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		//SimpleDateFormat fechaFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		//fechaFormat = fecProceso.
		try {

			// 	define standard SQL commands
			String sql = "SELECT AR.EJECUCION_ID  FROM INFI_TB_803_CONTROL_ARCHIVOS AR"
					+ " WHERE trunc(AR.FECHA) = " + fecProceso 
					+ " AND   AR.STATUS = '" + procesoEje + "'";
					
/*			String sql1 = "update SEQUENCE_NUMBERS set next_id = next_id + 1 where table_name = '"
					+ idTableNameProc + "'";
			String sql2 = "select next_id from SEQUENCE_NUMBERS where table_name = '"
					+ idTableNameProc + "'";*/
			// get connection
			conn = dsp.getConnection();
			// create sentence
			s = conn.createStatement();
			//get value
			rs = s.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			} else {
				throw new Exception(
						"No hay registro definido para el CICLO del proceso: "
						+ procesoEje + " en la tabla INFI_TB_803_CONTROL_ARCHIVOS");
			}
		}

		catch (Exception e) {
			throw new Exception("dbGetCicloProc():" + e.getMessage());
		}

		finally {
			if (null != conn) {
				if (null != rs) {
					rs.close();
				}
				if (null != s) {
					s.close();
				}
				conn.close();
			}
		}
	}
	

	/**
	 * 
	 * 
	 *  · RETORNAR EL NRO SIGUIENTE DE LA SECUENCIA SOLICITADA UBICADA EN ALA
	 * TABLA sequense_numbers
	 *  ·
	 * @parametro1 DataSource: JDBC DataSource
	 *  ·
	 * @parametro2 text: nombre con el cual fue definido el registros para la
	 *             tabla
	 * 
	 * 
	 * 
	 */

	public synchronized static String dbGetSequence(DataSource ds, String idTableName)
			throws Exception {
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		try {

			// define standard SQL commands
			String sql1 = "update SEQUENCE_NUMBERS set next_id = next_id + 1 where table_name = '"
					+ idTableName + "'";
			String sql2 = "select next_id from SEQUENCE_NUMBERS where table_name = '"
					+ idTableName + "'";

			// get connection
			conn = ds.getConnection();

			// increment value
			s = conn.createStatement();
			s.executeUpdate(sql1);

			// get value
			rs = s.executeQuery(sql2);
			if (rs.next()) {
				return rs.getString(1);
			} else {
				throw new Exception(
						"No hay registro definido para la secuencia: "
								+ idTableName + " en la tabla SEQUENCE_NUMBERS");
			}

		}

		catch (Exception e) {
			throw new Exception("dbGetSequence():" + e.getMessage());
		}

		finally {
			if (null != conn) {

				if (null != rs) {
					rs.close();
				}

				if (null != s) {
					s.close();
				}

				conn.close();
			}
		}
	}
	
	/**Formatea una fecha en base a las constantes establecidas en el sistema
	 * @param fecha objeto fecha que debe ser formateado
	 * @return fecha formateada*/
	protected String formatearFecha(Date fecha){
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		return formatoDeFecha.format(fecha);
	}
	
	/**Formatea una fecha y hora en base a las constantes establecidas en el sistema. 
	 * El valor de retorno estar&aacute; listo para ser usado en la consulta insert que se est&aacute; preparando con
	 * la funci&oacute;n de oracle to_date
	 * @param fecha objeto fecha que debe ser formateado
	 * @return un string con el siguiente formato to_date('dd/mm/yyyy','dd/mm/yyyy')*/
	protected String formatearFechaBD(Date fecha){
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		return "to_date('"+formatoDeFecha.format(fecha)+"','"+ConstantesGenerales.FORMATO_FECHA+"')";
	}
	
	/**Formatea una fecha y hora en base a las constantes establecidas en el sistema. 
	 * El valor de retorno estar&aacute; listo para ser usado en la consulta insert que se est&aacute; preparando con
	 * la funci&oacute;n de oracle to_date
	 * @param fecha objeto fecha que debe ser formateado con hora incluida
	 * @return un string con el siguiente formato to_date('dd/mm/yyyy hh:mi:ss','dd/mm/yyyy hh24:mi:ss')*/
	protected String formatearFechaBDHora(Date fecha){
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA + " HH:mm:ss");
		return "to_date('"+formatoDeFecha.format(fecha)+"','"+ConstantesGenerales.FORMATO_FECHA+" hh24:mi:ss')";
	}	
	
	/**Envia una cadena lista para ser insertada en la consulta que se est&aacute; construyendo con la fecha y hora de la base de datos.
	 * La m&aacute;scara usada ser&aacute; la configurada en las constantes del sistema 
	 * El valor de retorno sera el siguiente to_date(SYSDATE,'mascaraConfigurada(dd/mm/yyyy')
	 * @return un string con la funci&oacute;n de to_date usada en oracle*/
	protected String formatearFechaHoraBDActual(){
		//return "to_date(to_char(SYSDATE,'"+ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE+"'),'"+ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE+"')";
		return "SYSDATE";
	}
	
	/**Envia una cadena lista para ser insertada en la consulta que se est&aacute; construyendo con la fecha de la base de datos.
	 * La m&aacute;scara usada ser&aacute; la configurada en las constantes del sistema 
	 * El valor de retorno sera el siguiente to_date(SYSDATE,'mascaraConfigurada(dd/mm/yyyy')
	 * @return un string con la funci&oacute;n de to_date usada en oracle*/
	protected String formatearFechaBDActual(){
		return "to_date(SYSDATE,'"+ConstantesGenerales.FORMATO_FECHA_SYSDATE+"')";
	}
	
	/**Metodo que genera un dataset
	 * para mostrar los estatus Activo (1) e Inactivo (0)
	 * Util para combos en filtros
	 */
	public DataSet status() throws Exception {		
		
		int act=ConstantesGenerales.STATUS_ACTIVO;
		int ina=ConstantesGenerales.STATUS_INACTIVO;
		DataSet _status=new DataSet();
		_status.append("num_status",java.sql.Types.INTEGER);
		_status.append("estatus",java.sql.Types.VARCHAR);
		_status.addNew();
		_status.setValue("num_status",String.valueOf(act));
		_status.setValue("estatus","Activo");
		_status.addNew();
		_status.setValue("num_status",String.valueOf(ina));
		_status.setValue("estatus","Inactivo");
						
		return _status;
	}
	
	/**Metodo que genera un dataset
	 * para mostrar el indicador Si (1) o No (0)
	 * Util para combos en filtros
	 */
	public DataSet indicador() throws Exception {		
		
		int si=ConstantesGenerales.VERDADERO;
		int no=ConstantesGenerales.FALSO;
		DataSet _indicador=new DataSet();
		_indicador.append("num",java.sql.Types.INTEGER);
		_indicador.append("valor",java.sql.Types.VARCHAR);
		_indicador.addNew();
		_indicador.setValue("num",String.valueOf(si));
		_indicador.setValue("valor","Si");
		_indicador.addNew();
		_indicador.setValue("num",String.valueOf(no));
		_indicador.setValue("valor","No");
						
		return _indicador;
	}
	
	
	
	/**Metodo que genera un dataset
	 * para mostrar los meses del año
	 * Util para combos en filtros
	 */
	public DataSet meses() throws Exception {		
		DataSet _meses=new DataSet();
		_meses.append("mes",java.sql.Types.VARCHAR);
		_meses.append("valor",java.sql.Types.VARCHAR);
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("01"));
		_meses.setValue("valor","Enero");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("02"));
		_meses.setValue("valor","Febrero");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("03"));
		_meses.setValue("valor","Marzo");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("04"));
		_meses.setValue("valor","Abril");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("05"));
		_meses.setValue("valor","Mayo");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("06"));
		_meses.setValue("valor","Junio");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("07"));
		_meses.setValue("valor","Julio");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("08"));
		_meses.setValue("valor","Agosto");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("09"));
		_meses.setValue("valor","Septiembre");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("10"));
		_meses.setValue("valor","Octubre");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("11"));
		_meses.setValue("valor","Noviembre");
		_meses.addNew();
		_meses.setValue("mes",String.valueOf("12"));
		_meses.setValue("valor","Diciembre");
						
		return _meses;
	}
	
	public DataSet horas() throws Exception {
		DataSet dsHora = new DataSet();
		dsHora.append("hora", java.sql.Types.VARCHAR);
		String var = "";
		for (int i=1; i< 13; i++) {
			var = "";
			if (i < 10)
				var = "0";
			var += String.valueOf(i);
			dsHora.addNew();
			dsHora.setValue("hora", var);
		}
		return dsHora;
	}
	public DataSet minutos() throws Exception {
		DataSet dsMin = new DataSet();
		dsMin.append("min", java.sql.Types.VARCHAR);
		String var = "";
		for (int i=0; i< 60; i++) {
			var = "";
			if (i < 10)
				var = "0";
			var += String.valueOf(i);
			dsMin.addNew();
			dsMin.setValue("min", var);
		}
		return dsMin;
	}
	/**Metodo que genera un dataset
	 * para mostrar el indicador Meridiano AM o PM
	 * Util para combos en filtros
	 */
	public DataSet indMeridiano() throws Exception {		
		
		String AntesMeridian="AM";
		String PostMeridian="PM";
		DataSet _indicador=new DataSet();
		_indicador.append("num",java.sql.Types.INTEGER);
		_indicador.append("valor",java.sql.Types.VARCHAR);
		_indicador.addNew();
		_indicador.setValue("num",AntesMeridian);
		_indicador.setValue("valor","AM");
		_indicador.addNew();
		_indicador.setValue("num",PostMeridian);
		_indicador.setValue("valor","PM");
						
		return _indicador;
	}
	
	/**Metodo que genera un dataset
	 * para mostrar las Recomendaciones que se deben seguir a la hora de crear documentos
	 */
	public DataSet recomendaciones() throws Exception {		
		
		DataSet _recomendaciones = new DataSet();
		_recomendaciones.append("recomendaciones",java.sql.Types.VARCHAR);
		_recomendaciones.addNew();
		_recomendaciones.setValue("recomendaciones",ConstantesGenerales.RECOMENDACIONES_DOCUMENTOS);
						
		return _recomendaciones;
	}
	
	/**Metodo que retorna TRUE si el mail contienen los caracteres permitidos
	 * de lo contrario retorna FALSE 
	 */
	public boolean isEmail(String correo) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }        
    }
	
	/**Metodo que retorna TRUE si el rif contienen los caracteres permitidos
	 * de lo contrario retorna FALSE 
	 */
	public boolean isRif(String rif) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^([JGjg][-]\\d{9})$"); 
        mat = pat.matcher(rif);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }        
    }
	/**Para campos tipo Varchar
	 * Retorna TRUE si consigue un registro de los contrario retorna FALSE
	 * query sencillo solo para cuando se desea insertar en BD
	 * */
	public boolean encontrar_registro (String tabla, String campo, String valor) throws Exception{
		boolean encontro=false;
		String sql = ("select * from @tabla@ where upper(@campo@) = upper('@valor@')");
		sql=Util.replace(sql,"@tabla@",tabla);
		sql=Util.replace(sql,"@campo@",campo);
		sql=Util.replace(sql,"@valor@",valor);
		DataSet ds= db.get(dataSource, sql);
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
	
	/**Para campos tipo Varchar
	 * Retorna TRUE si consigue un registro de los contrario retorna FALSE
	 * query solo para cuando se desea actualizar en BD, compara si el 
	 * registro a insertar ya esta definido para otro registro
	 * (campo y valor) deben ser distintos y (campo1  valor1) iguales
	 * */
	public boolean comparar_registro (String tabla, String campo, String campo1, String valor, String valor1) throws Exception{
		boolean encontro=false;
		String sql = ("select * from @tabla@ where @campo@!='@valor@' and @campo1@= '@valor1@'");
		sql=Util.replace(sql,"@tabla@",tabla);
		sql=Util.replace(sql,"@campo@",campo);
		sql=Util.replace(sql,"@campo1@",campo1);
		sql=Util.replace(sql,"@valor@",valor);
		sql=Util.replace(sql,"@valor1@",valor1);
		DataSet ds= db.get(dataSource, sql);
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
	
	/**M&eacute;todo que verifica si una cadena posee alg&uacute;n car&aacute;cter especial(Si lo posee retorna TRUE)
	 * @param Solo se aceptan letras(may,min con sus respectivos acentos),n&uacute;meros,punto,coma,guion,underscore,slash,simbolo + y espacio en blanco 
	 * @param campo_cadena, String a validar. */
	public boolean verificarCaracteresEspeciales(String campo_cadena)
		{	
			boolean flag=false;
			String cadena=campo_cadena;
			char []array=cadena.toCharArray();
			
				for(int i=0;i<array.length;i++)
				{	
					String str=String.valueOf(array[i]);
						if(
							(int) str.charAt(0)>=0   &&  (int) str.charAt(0)<=31  ||
							(int) str.charAt(0)>=33  &&  (int) str.charAt(0)<=42  ||
							(int) str.charAt(0)>=58  &&  (int) str.charAt(0)<=64  ||
							(int) str.charAt(0)>=91  &&  (int) str.charAt(0)<=94  ||
							(int) str.charAt(0)>=96  &&  (int) str.charAt(0)<=96  ||
							(int) str.charAt(0)>=123 &&  (int) str.charAt(0)<=192 ||
							(int) str.charAt(0)>=194 &&  (int) str.charAt(0)<=200 ||
							(int) str.charAt(0)>=202 &&  (int) str.charAt(0)<=204 ||
							(int) str.charAt(0)>=206 &&  (int) str.charAt(0)<=208 ||
							(int) str.charAt(0)>=210 &&  (int) str.charAt(0)<=210 ||
							(int) str.charAt(0)>=212 &&  (int) str.charAt(0)<=217 ||
							(int) str.charAt(0)>=219 &&  (int) str.charAt(0)<=224 ||
							(int) str.charAt(0)>=226 &&  (int) str.charAt(0)<=232 ||
							(int) str.charAt(0)>=234 &&  (int) str.charAt(0)<=236 ||
							(int) str.charAt(0)>=238 &&  (int) str.charAt(0)<=240 ||
							(int) str.charAt(0)>=242 &&  (int) str.charAt(0)<=242 ||
							(int) str.charAt(0)>=244 &&  (int) str.charAt(0)<=249 ||
							(int) str.charAt(0)>=251 &&  (int) str.charAt(0)<=255
						  )
						{
							flag=true;
							break;
						}
				}				
			return flag;		
		}
	
	/** Metodo que formatea una cadena de caracteres a minuscula, reemplaza espacios por underscope y elimina espacios en blanco al final y al principio de la cadena
	 * @param cadena, String a ser tratado
	 */
	public String formateo(String cadena, int minuscula, int replace, int espaciosBlancos, String caracterAnterior, String caracterNuevo){
		String cadenaFormateada =cadena;
		if(minuscula==ConstantesGenerales.VERDADERO){
			cadenaFormateada = cadenaFormateada.toLowerCase();
		}
		if(replace==ConstantesGenerales.VERDADERO&&(caracterAnterior!=null)&&(caracterNuevo!=null)){
			cadenaFormateada = cadenaFormateada.replace(caracterAnterior, caracterNuevo);
		}
		if(espaciosBlancos==ConstantesGenerales.VERDADERO){
			cadenaFormateada = cadenaFormateada.trim();
		}
		/*if((minuscula==ConstantesGenerales.VERDADERO)&&(replace==ConstantesGenerales.VERDADERO)){
			cadenaFormateada = ((cadena.trim()).replace(" ", "_")).toLowerCase();
		}else if((minuscula==ConstantesGenerales.VERDADERO)&&(replace==ConstantesGenerales.FALSO)){
			cadenaFormateada = ((cadena.trim()).toLowerCase());
		}else if((minuscula==ConstantesGenerales.FALSO)&&(replace==ConstantesGenerales.VERDADERO)){
			cadenaFormateada = ((cadena.trim()).replace(" ", "_"));
		}else if((minuscula==ConstantesGenerales.FALSO)&&(replace==ConstantesGenerales.FALSO)){
			cadenaFormateada = cadena.trim();
		}	*/	
		return cadenaFormateada;
	}
	
	/**Traer id usuario de session*/
	public String idUserSession(String user) throws Exception {		
		StringBuffer sql = new StringBuffer();
		String usuario=null;
		sql.append("select msc_user_id from msc_user where userid='").append(user).append("'");	
		dataSet = db.get(dataSource, sql.toString());
		
	/**Cierra la conexi&oacute;n usada por el dao*/
		if(dataSet.count()>0){
			dataSet.next();
			usuario=dataSet.getValue("msc_user_id");
		}
		return  usuario;
	}
	
	/**traer Rol del usuario de session*/
	/***el id de la aplication es 3*/
	public String listarRolUser(String user) throws Exception{
		StringBuffer sql = new StringBuffer();
		String rolUsuario=null;
		
		sql.append("select r.description from MSC_ROLE r, MSC_ROLE_USER ru, MSC_USER u, MSC_APPLICATIONS ap where r.msc_role_id=ru.msc_role_id and u.msc_user_id=ru.msc_user_id and r.id_application = ap.id_application and ap.siglas_applic = '"+AppProperties.getProperty("app-name")+"'");
		sql.append(" and u.userid='").append(user).append("'");
		System.out.println("listarRolUser: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
		/**Cierra la conexi&oacute;n usada por el dao*/
		if(dataSet.count()>0){
			dataSet.next();
			rolUsuario=dataSet.getValue("description");
		}
		return  rolUsuario;
	}
	
	public void fechaHoy() throws Exception{
		
		String sql = "select trunc(sysdate) as fecha_hoy, trunc(sysdate-7) as hace_siete_dias, trunc(sysdate-60) as hace_60_dias from dual";
		
		dataSet = db.get(dataSource, sql);
		System.out.println("fechaHoy "+sql);
    }
		
	/**Cierra la conexi&oacute;n usada por el dao*/
	public void cerrarConexion() throws SQLException{
		if (conn != null){
			conn.close();
			conn = null;
		}
	}
	/** Metodo que valida el uso de comillas en querys para campos nulos 
	 * @param String campo
	 * */
	public String validarCampoNulo(String campo){
		return (campo==null?null:"'"+campo.trim()+"'");
	}

	/**
	 * Obtiene el registro inicial a mostrar por la página seleccionada
	 * @param paginaMostrar pagina que se desea mostrar
	 * @param registrosPorPagina registros por página
	 * @return registro inicial para que sea incorporado a la consulta
	 */
	protected int registroInicio(int paginaMostrar, int registrosPorPagina){
		return (paginaMostrar -1) * registrosPorPagina + 1;
	}
	
	/**
	 * Obtiene el registro final a mostrar por la página seleccionada
	 * @param paginaMostrar pagina que se desea mostrar
	 * @param registrosPorPagina registros por página
	 * @return registro inicial para que sea incorporado a la consulta
	 */	
	protected int registroFinal(int paginaMostrar, int registrosPorPagina){
		return paginaMostrar * registrosPorPagina;
	}
		
	/**
	 * Arma el DataSet paginado por Oracle
	 * @param sql sql original a convertir
	 * @param paginaMostrar número de página a mostrar
	 * @param registrosPorPagina cantidad de registros que deben ser mostardos por página
	 * @return el dataset que debe mostrarse al usuario
	 * @throws Exception en caso de error
	 */
	protected DataSet obtenerDataSetPaginado(String sql, int paginaMostrar, int registrosPorPagina) throws Exception{
		getTotalDeRegistros(sql.toString());
		int regInicio = registroInicio(paginaMostrar, registrosPorPagina);
		int regFinal = registroFinal(paginaMostrar, registrosPorPagina);
		String sqlFinal = "select * from (select rownum numregistros,a.* from (" + sql + ") a ) where numregistros >= " + regInicio  + " and numregistros <= " + regFinal;
		System.out.println("sqlFinal : " + sqlFinal);
		return db.get(this.dataSource, sqlFinal);
	}
	
		protected DataSet obtenerDataSetPaginado1(String sql, int paginaMostrar, int registrosPorPagina) throws Exception{
		getTotalDeRegistros(sql.toString());
		int regInicio = registroInicio(paginaMostrar, registrosPorPagina);
		System.out.println("regInicio-->"+regInicio);
		int regFinal = registroFinal(paginaMostrar, registrosPorPagina);
		System.out.println("regFinal-->"+regFinal);
		String sqlFinal = "select * from (select rownum numregistros,a.* from (" + sql + ") a ) where numregistros >= " + regInicio;// + " and numregistros <= "+ regFinal;
		System.out.println("sqlFinal-->"+sqlFinal);
		return db.get(this.dataSource, sqlFinal);
	}
	
	/**
	 * Obtiene el total de los registros consultados. Llena la variable totalDeRegistros
	 * @param sql sql de consulta para verificar el total de registros
	 * @return número total de registros
	 * @throws Exception en caso de error
	 */
	protected int getTotalDeRegistros(String sql) throws Exception{
		String sqlFinal = "select count(1) total from (" + sql + ") a";
		DataSet ds = db.get(this.dataSource, sqlFinal);
		ds.next();
		totalDeRegistros = Integer.parseInt(ds.getValue("total")); 
		return getTotalDeRegistros();
	}

	/**
	 * Obtiene el total de registros. 
	 * Este método debe ser invocado despues de hacer el llamado al método getTotalDeRegistros
	 * @return total de registros arrojados por la consulta. Este método debe ser utilzado en conjunto con getTotalDeRegistros(sql)
	 */
	public int getTotalDeRegistros() {
		return totalDeRegistros;
	}
	
	/**
	 * Devuelve un DataSet con la cantidad de registros retornados en una
	 * consulta. El campo del DataSet recibe el nombre de t_registros (total de
	 * registros retornados en la consulta).
	 * 
	 * @return DataSet
	 * @throws Exception
	 */
	public DataSet getTotalRegistros() throws Exception {
		return getTotalRegistrosPrivado(true);
	}
	
	/**
	 * Arma el dataset con el total de registros almacenado en la variable totalDeRegistros
	 * @param utilizarDataSet indica si el total se obtiene del dataset o de la variable. Este valor es false
	 * cuando los registros son muchos y se desea paginar por base de datos
	 * @return DataSet con el total de registros
	 * @throws Exception en caso de error
	 */
	public DataSet getTotalRegistros(boolean utilizarDataSet) throws Exception {
		return getTotalRegistrosPrivado(utilizarDataSet);
	}
	
	private DataSet getTotalRegistrosPrivado(boolean utilizarDataSet) throws Exception {
		DataSet _datos = new DataSet();
		_datos.append("t_registros", java.sql.Types.VARCHAR);
		_datos.addNew();
		if (utilizarDataSet){			
			_datos.setValue("t_registros", String.valueOf(dataSet.count()));
		}else{
			_datos.setValue("t_registros", String.valueOf(getTotalDeRegistros()));
		}
		return _datos;
	}	
	
	public void agregarStatementsBatch(Statement st,ArrayList<String> sentencias) throws SQLException{
		for(String sentencia:sentencias){
			st.addBatch(sentencia);
			System.out.println(sentencia);
		}
	}
	
	public String ejecutarStatementsBatch(ArrayList<String> sentencias) throws SQLException{
		Connection cn =null;
		Statement st = null;
		try {
			if (sentencias!=null&&sentencias.size() > 0) {
				cn = dataSource.getConnection();
				cn.setAutoCommit(false);//Agregado por NM25287 el 31/03/2015 para ITS-2345
				st = cn.createStatement();			
				agregarStatementsBatch(st, sentencias);
				st.executeBatch();
				cn.commit();
			}
		} catch (SQLException e) {
			try {
				cn.rollback();
			} catch (SQLException ex) {
				Logger.error(this, "Error en la ejecución Batch de querys");
				throw ex;
			}
			throw e;
		}finally{
			try {
				if(st!=null) st.close();
				if(cn!=null) cn.close();
			} catch (SQLException e) {
				Logger.error(this, "Error en la ejecución Batch");
				throw e;
			}			
			
		}
		return null;	
	}
	
	//NM29643 - INFI_TTS_443 - 25/03/2014: Se modifica  el metodo ejecutarStatementsBatch para que
	//retorne boolean indicando si ejecuto, en lugar de String
	//CT20315 - ITS-2345 - 04/02/2015: Se establece el AutoCommit en FALSE para el Connection cn, para asegurar un buen manejo de los commit y rollback.
	public boolean ejecutarStatementsBatchBool(ArrayList<String> sentencias) throws SQLException{
		boolean ejecuto = false;
		Connection cn =null;
		Statement st = null;
		try {
			if (sentencias!=null&&sentencias.size() > 0) {
				cn = dataSource.getConnection();
				cn.setAutoCommit(false);//Agregado por CT20315 el 04/02/2015 para ITS-2345
				st = cn.createStatement();			
				agregarStatementsBatch(st, sentencias);
				st.executeBatch();
				cn.commit();
				ejecuto = true;
			}
		} catch (SQLException e) {
			try {
				cn.rollback();
			} catch (SQLException ex) {
				Logger.error(this, "Error en la ejecución Batch de querys");
				throw ex;
			}
			throw e;
		}finally{
			try {
				if(st!=null) st.close();
				if(cn!=null) cn.close();
			} catch (SQLException e) {
				Logger.error(this, "Error en la ejecución Batch");
				throw e;
			}			
			
		}
		return ejecuto;	
	}
	
}