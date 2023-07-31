package models.msc_utilitys;

import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import megasoft.AbstractModel;
import megasoft.ActionConfig;
import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;

/**
 * Clase que extiende AbstractModel y contiene m&eacute;todos utilitarios para ser utilizados por los modelos
 * @author Megasoft Computaci&oacute;n.
 *
 */
public class MSCModelExtend extends AbstractModel{
	
	public DataSource dsoAplic= null;	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void init(ServletContext app, HttpServletRequest req, DataSource dso, DataSet record, ActionConfig config) throws Exception
	{
		//ejecutar init del AdstractModel
		super.init(app, req, dso, record, config);
		//Obtiene parametro del web.xml
		String strDataSource=app.getInitParameter("datasource");
		//Se genera un DataSource para Trabajar con la base de datos
		dsoAplic=db.getDataSource(strDataSource);
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
				return 50;
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
   	*  - El objeto no es NULL pero no contiene el atributo msc_user_id, asignado en el proceso de Login
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
		 String sql1 = "update SEQUENCE_NUMBERS set next_id = next_id + 1 where table_name = '" + idTableName + "'";
		 String sql2 = "select next_id from SEQUENCE_NUMBERS where table_name = '" + idTableName + "'";

		 //get connection
		 conn = ds.getConnection();

		 //increment value
		 s = conn.createStatement();
		 s.executeUpdate(sql1);
		 //NM29643 - infi_TTS_466 Se agrega commit
		 conn.commit();
		 
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
	* Obtiene una nueva instancia de un DataSet
	* @param dsOld
	* @return DataSet
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
	
	/**
	 * M&eacute;todo para convertir un string a un tipo Date
	 * @param fecha, formato
	 * @return Date
	 * @throws ParseException
	 */	
	public Date StringToDate(String fechaString, String formato) throws ParseException{
		
		java.util.Date fecha = null;
		//fecha = java.sql.Date.valueOf("1950-01-01");
		if(fechaString!=null && !fechaString.equals("")){
			SimpleDateFormat sdf = new SimpleDateFormat(formato);		
			//////////////Fecha actual///////////////////////
			fecha = sdf.parse(fechaString);	 
		    ////////////////////////////////////////////////////
		}
		
		return fecha;
	}
	
	/**
	 * M&eacute;todo para obtener la fecha del sistema con un formato especifico
	 * @param formato
	 * @return String con la fecha actual del sistema en un formato especificado
	 * @throws ParseException
	 */	
	public String getFechaHoyFormateada(String formato) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);		
		//////////////Fecha actual///////////////////////
	    Calendar fechaHoy = Calendar.getInstance();						
		Date aux = fechaHoy.getTime();
		String fhoy = sdf.format(aux); ////darle formato			
	    ////////////////////////////////////////////////////
		
		return fhoy;
	}
	
	
	/**
	 * M&eacute;todo para calcular la diferencia en hh:mm:ss entre dos tiempos determinados
	 * @param t1
	 * @param t2
	 * @param formato de las fechas de inicio y fin, por ejemplo la cadena "dd/MM/yyyy HH:mm:ss"
	 * @return String con la diferencia de tiempo
	 * @throws ParseException
	 */	
	public String getDiferenciaHoras(String t1, String t2, String formato) throws ParseException{
				
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		long horas, minutos, segundos;
		long restohora, restominuto;
		String hrs, min, seg;

		java.util.Date hora1 = sdf.parse(t1); 
		java.util.Date hora2 = sdf.parse(t2); 
		
		//obtener diferencia en milisegundos
		long lantes = hora1.getTime(); 
		long lahora = hora2.getTime(); 
		long dif_miliseg = lahora - lantes; 	

		//transformar milisegundos para obtener la cantidad en horas, minutos y segundos
		horas = dif_miliseg/3600000;
		restohora = dif_miliseg%3600000;
		
		minutos = restohora/60000;
		restominuto = restohora%60000;
		
		segundos = restominuto/1000;
		
		//colocar en formato hh:mm:ss
		if(horas<10) hrs = "0" + String.valueOf(horas);
		else hrs = String.valueOf(horas);
		
		if(minutos<10) min = "0" + String.valueOf(minutos);
		else min = String.valueOf(minutos);

		if(segundos<10) seg = "0" + String.valueOf(segundos);
		else seg = String.valueOf(segundos);
		
		String duracion = hrs +":"+ min +":"+ seg;		
	
		return String.valueOf(duracion);
	}
	
	/**
	 * Obtiene el total de páginas según los registros a paginar
	 * @param totalDeRegistros total de registros arrojados por la consulta
	 * @param registrosPorPagina registros mostrados por página
	 * @return total de páginas a mostrar
	 */
	protected int getTotalDePaginas(int totalDeRegistros, int registrosPorPagina){
		java.math.BigDecimal b1 = new java.math.BigDecimal(totalDeRegistros);
		java.math.BigDecimal b2 = new java.math.BigDecimal(registrosPorPagina);
		return  b1.divide(b2,java.math.BigDecimal.ROUND_UP).intValue();
		
	}
	
	/**
	 * Arma el html necesario para la sección de paginación
	 * @param totalDeRegistros total de registros de la consulta
	 * @param registrosPorPagina registros a mostrar por página
	 * @param numeroDePagina número de página seleccionada
	 * @throws Exception en caso de error
	 */
	protected void getSeccionPaginacion(int totalDeRegistros, int registrosPorPagina, int numeroDePagina) throws Exception{
		DataSet paginacion = new DataSet();		
		paginacion.append("page_index",java.sql.Types.VARCHAR);
		StringBuilder sb = new StringBuilder(); 
		int totalDePaginas = getTotalDePaginas(totalDeRegistros,registrosPorPagina);
		
		String xmlPrincipal = getResource("/WEB-INF/templates/paging_scroll/index-scroll");
		String xmlPagSeleccionada = getResource("/WEB-INF/templates/paging_scroll/index-scroll_current");
		String xmlPagNoSeleccionada = getResource("/WEB-INF/templates/paging_scroll/index-scroll_available");
		
		xmlPrincipal = xmlPrincipal.replaceAll("<page_index>", "@paginas@");
		xmlPrincipal = xmlPrincipal.replaceAll("</page_index>", "");
		xmlPrincipal = xmlPrincipal.replaceAll("@page_number@", "");
		
		//Arma las páginas
		for (int i=1; i <= totalDePaginas; i++){
			if (i==numeroDePagina){
				sb.append(xmlPagSeleccionada.replaceAll("@page_number@", String.valueOf(numeroDePagina)));	
			}else{
				sb.append(xmlPagNoSeleccionada.replaceAll("@page_number@", String.valueOf(i)));
			}			
		}
		xmlPrincipal = xmlPrincipal.replaceAll("@paginas@",sb.toString());
		xmlPrincipal = xmlPrincipal.replaceAll("@page_action@",obtenerAccion());
		if (_record.count()>0){
			xmlPrincipal = xmlPrincipal.replaceAll("\\?pagenumber=","&pagenumber=");
		}
				
		paginacion.addNew();
		paginacion.setValue("page_index", xmlPrincipal);
		storeDataSet("paginacion", paginacion);
		obtenerAccion();
		
//		_config.bind.addNew();
//		_config.bind.setValue("type", "replace");
//		_config.bind.setValue("source", "paginacion");
		
		//System.out.println(_record);
	}
	
	/**
	 * Obtiene la accion con las parámetros que vienen del record
	 * @return accion armada
	 * @throws Exception en caso de error
	 */
	private String obtenerAccion() throws Exception{
		StringBuilder accion = new StringBuilder(getActionID());
		if (_record != null && _record.count()>0){
			String campos[] = _record.getFields();
			for (int i=0; i < campos.length; i++){
				//System.out.println("--->  Campos -->" + campos[i].toString()+ " Value-->" + _record.getValue(campos[i]));
				if (_record.getValue(campos[i])!=null){
					accion.append((i==0?"?":"&") + campos[i] + "=" + _record.getValue(campos[i]));	
				}else if(i==0){accion.append("?");} 
			}
		}
		return accion.toString();
	}

	/**
	 * Devuelve el número de página seleccionada
	 * @return número de página seleccionada
	 */
	protected int getNumeroDePagina(){
		return Integer.parseInt(_req.getParameter("pagenumber")==null?"1":_req.getParameter("pagenumber"));
	}
	
	protected String getRutaTemporal() throws Exception{
		return _app.getRealPath("/WEB-INF/tmp");		
	}
}