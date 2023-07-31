package models.valid;

import megasoft.*;
 
import java.io.RandomAccessFile;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.sql.*;

import javax.sql.DataSource;

/**
 * Clase que extiende a AbstractModel y contiene m&eacute;todos utilitarios para ser utilizados por los modelos.
 * @author Megasoft Computaci&oacute;n.
 *
 */
public class MSCModelExtend extends AbstractModel
{

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public DataSource dsoApp= null;
	public void init(ServletContext app, HttpServletRequest req, DataSource dso, DataSet record, ActionConfig config) throws Exception
	{
		//ejecutar init del AdstractModel
		super.init(app, req, dso, record, config);
		//Obtiene parametro del web.xml
		String strDataSource=app.getInitParameter("datasource");
		//Se genera un DataSource para Trabajar con la base de datos de la Armada
		dsoApp=db.getDataSource(strDataSource);
	}
	public void execute() throws Exception
	{

	}


	/**
	 * Retorna el N&uacute;mero de p&aacute;ginas configuradas en el web.xml

  <env-entry>
        <description>Cantidad de registros por pagina</description>
        <env-entry-name>rowsxpage</env-entry-name>
        <env-entry-type>java.lang.String</env-entry-type>
        <env-entry-value>10</env-entry-value>
  </env-entry>

	Si no existe configurado se asume 20 Registro por p&aacute;gina
	*/
	public int _getPageSize() throws Exception
	{
			String size = Util.getEnvEntry("rows_in_page");

			if (size!=null && size.trim().length()>0)
			{
					try
					{
						return Integer.parseInt(size);
					}
					catch(Exception e)
					{
						return 20;
					}
			}
			else
				return 20;
	}

	/**
	 * Retorna el N&uacute;mero de p&aacute;ginas configuradas en la Tabla
	 * de Configuracion del Sistema
	 */
	public int getPageSize() throws Exception
	{
			String sql = "select * from parametros_sistema where parametro = 'rows_in_page'";
			DataSet ds = db.get(_dso,sql);

			if (ds.next())
				return Integer.parseInt(ds.getValue("valor"));
			else
				return 20;
	}

	// Devuelve un n&uacute;mero que representa el id del usuario que ingresa al sistema
	//
	public String getIdUsuario() throws Exception
	{
		String user = getUserName();
		String sql = "";
		
		DataSet _usuarios = new DataSet();
		
		//Se obtiene el usuario en base al login introducido
		sql = "SELECT * FROM prueba_usuarios_sah pu WHERE pu.usuario= '@usuario@'";
		
		sql = Util.replace(sql,"@usuario@", user);
			    
		_usuarios = db.get(_dso, sql);
		 
		if (_usuarios.count()>0)			 
		{
			_usuarios.first();
			_usuarios.next();
						
			return _usuarios.getValue("id_medico_servicio");
		}
		else
		{
			return "1";		
		}							
	}


   	 public DataSet getSessionDataSet (String name_ds) throws Exception
    	{
		DataSet ds = null;
		//Validar si la session esta activa para recuperar DataSet solicitado
		if (isValidSession(_req))
			ds = (DataSet) _req.getSession().getAttribute(name_ds);
		return ds;
    	}

	/**
    	* Retorna el contexto de sessiòn si existe. De lo contrario retorna NULL
    	**/
   	 public HttpSession getSessionContext (HttpServletRequest request) throws Exception
    	{
	       	 HttpSession session = request.getSession(false);

	        	if ( session!=null && request.isRequestedSessionIdValid() )
            			return session;
	        	else
            			return null;
    	}

	 /**
	 * Retorna FALSE si:
	*  - El objeto session, correspondiente al request, es NULL
    	*  - El el m&eacute;todo isRequestedSessionIdValid() es FALSE
   	*  - El objeto no es NULL pero no contiene el atributo msc_userid, asignado en el proceso de Login
    	*
    	* Retorna TRUE si:
    	*  - EL usuario tiene una sessi&oacute;n v&aacute;lida y el atributo userid no el null
    	**/
    	public boolean isValidSession (HttpServletRequest req) throws Exception
    	{
        		HttpSession session = getSessionContext(req);

        		// Retorna false si no tiene una sessiòn v&aacute;lida
        		if (session==null)
            			return false;

/*        		// Retorna false si, aun teniendo sessi&oacute;n v&aacute;lida, no tiene el atributo userid (guardado durante el login)
       		 else if (session.getAttribute("msc_userid")==null)
            			return false;
*/
       		 //retorna true.
       		 else
           			return true;
	}


	/*
	* Si en el Request viene el par&aacute;metro URL
	* se configura la acci&oacute;n para que redireccione al mismo y no tome en cuenta el definido en config.xml
	**/
	public void setActionURL() throws Exception
	{
		String url = _req.getParameter("url");
		if ( url!=null )
			_config.nextAction=url;
	}

	/*
	 * Cambia template a ser tomado por el View
	**/
	public void setActionTemplate(String template) throws Exception
	{
		if (template!=null)
			_config.template = template;
	}

	public DataSet changeComillaHtml(DataSet _ds, String campo)throws Exception
	{
		if (_ds!=null)
		{
			char doble_comillas = '"';
			char porcentaje = '%';
			String frase;
			while (_ds.next())
			{
				frase = _ds.getValue(campo);
				
				frase = Util.replace(frase, String.valueOf(doble_comillas), "&quot;" );				
				
				frase = Util.replace(frase, String.valueOf(porcentaje), "&#37;" );
				
				_ds.setValue(campo, frase);
			}
		}
		return _ds;
	}

	public DataSet changeHtmlComilla(DataSet _ds, String campo)throws Exception
	{
		if (_ds!=null)
		{
			char doble_comillas = '"';
			String frase;
			while (_ds.next())
			{
				frase = _ds.getValue(campo);
				frase = Util.replace(frase, "&quot;", String.valueOf(doble_comillas));
				_ds.setValue(campo, frase);

			}

		}

		return _ds;
	}


	public void printDS(DataSet ds, boolean append) throws Exception
			{
				//Nombre del Archivo para Guardar la Salida
				String pathFile="log/ds.htm";
				RandomAccessFile text= null;
				text = new RandomAccessFile(pathFile ,"rw");
				try
				{

					String out="";
					//Verifica si el DataSet es Null para escribir Null en el Archivo
					if (ds==null)
					out="<b>Action Id:</b>&nbsp  <b><br> Fecha </b>"+ Util.getDate()+ "<br><b>  Hora </b>"+ Util.getTime() +"<br> NULL<br><br>" ;
					else
					out="<b>Action Id:</b>&nbsp <b><br> Fecha </b>"+ Util.getDate()+ "<br><b>  Hora </b>"+ Util.getTime() +"<br>"+  ds.toString() +"<br><br>" ;

					//Verifica Si borra el archivo y lo crea de nuevo con el ultimo DataSet Impreso, o Escribe al Final de los que ya estan
					if (append)
						text.seek(text.length());
					else
						text.setLength(0);
					//Escribe la Variable Out en el Archivo
					text.writeBytes(out);
					//Cierra el Archivo
					text.close();
				}
				catch (Exception e)
				{
					 throw new Exception ("No se puede escribir en " + pathFile +  e.getMessage());
				}
				finally
				{
					if (text!=null)
						{
						text.close();
						}
				}
		}
	public void print(String text) throws Exception
	{
		System.out.println(text);
	}
	

	
	/**

		*

	·         RETORNAR EL NRO SIGUIENTE DE LA SECUENCIA SOLICITADA UBICADA EN ALA TABLA &eacute;quense_numbers

	·         @parametro1 DataSource: JDBC DataSource

	·         @parametro2 text: nombre con el cual fue definido el registros para la tabla 

		*

		**/

	public static String dbGetSequence( DataSource ds, String idTableName ) throws Exception
	{
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		try
 		{

		 //define standard SQL commands
		 String sql1 = "update SEQUENCE_NUMBERS set next_id = next_id + 1 where lower(table_name) = like lower('" + idTableName + "')";
		 String sql2 = "select next_id from SEQUENCE_NUMBERS where lower(table_name) = like lower('" + idTableName + "')";

		 //get connection
		 conn = ds.getConnection();

		 //increment value
		 s = conn.createStatement();
		 s.executeUpdate(sql1);

		 //get value
		 rs = s.executeQuery(sql2);
		 if ( rs.next() )
		 {
			 return rs.getString(1);
		 }
		 else
		 {
			 throw new Exception("No hay registro definido para la secuencia: " + idTableName + " en la tabla SEQUENCE_NUMBERS");
		 }

	 }

	 catch ( Exception e )
	 {
		 throw new Exception("dbGetSequence():" + e.getMessage());
	 }


	 finally
	 {
		 if ( null != conn )
		 {

			 if ( null != rs )
			 {
				 rs.close();
			 }

			 if ( null != s )
			 {
				 s.close();
			 }

			 conn.close();
		 }
	 }
	}
	/**
	* Ejecuta la transaccion del modelo
	*/
	public DataSet getNewDataSet(DataSet dsOld) throws Exception
	{
		DataSet dsNew = new DataSet();
		String[] strCampos = dsOld.getFields();
		String[] strTypes = dsOld.getTypeNames();
	
		for (int i=0; i< strCampos.length; i++)
		{
//			print("entro en cada campo");
			if (strTypes[i].indexOf("VARCHAR")>=0)
				dsNew.append(strCampos[i] ,java.sql.Types.VARCHAR );
			else if (strTypes[i].indexOf("INTEGER")>=0)
				dsNew.append(strCampos[i] ,java.sql.Types.INTEGER );
			else if (strTypes[i].indexOf("DECIMAL")>=0)
				dsNew.append(strCampos[i] ,java.sql.Types.DOUBLE );
			else if (strTypes[i].indexOf("DATE")>=0)
				dsNew.append(strCampos[i] ,java.sql.Types.DATE );
		}
		return dsNew;
	}
}