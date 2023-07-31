package models.help;

import megasoft.*;

import java.sql.*;

/**
 * Se encarga de listar el contenido de la ayuda en linea Dinamicamente(Menu).
 * @author Megasoft Computaci&oacute;n
 */

public class View extends AbstractModel
{

	//Variable de conneccion JDBC
	Connection con = null;
	java.sql.Statement stmt = null;
	//variables para remplazo en texto html
	String url_image = "";
    String url_css = "";
	    
	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception
	{
		// establecer conneccion con la DB con el DataSource
		con = _dso.getConnection();
		
	    //** Buscar parametros en tabla de config que contengan la palabra "url_images" y "url_css". Para remplazarlos en la formas de ayuda
		//url_image = dbGetParameter("url_images");
		//url_css = dbGetParameter("url_css");
	    
		//Crear Ds para Guardar tabla de contenido de la ayuda
		DataSet dsHelp = new DataSet();
		dsHelp.append("help",java.sql.Types.VARCHAR);
		dsHelp.addNew();
		dsHelp.setValue("help", "");
		
		String data_table = "";
		
		//determinar si se hara una busqueda o se mostrara una Ayuda especifica
		if(_req.getParameter("cod_help")!=null && _req.getParameter("texto_buscar")==null){
			//**Se esta solicitando una ayuda especifica
			data_table = GetHelp(_req.getParameter("cod_help"));
			
		}
		else if(_req.getParameter("cod_help")!=null && _req.getParameter("texto_buscar")!=null){
			//**Se esta solicitando una ayuda especifica con texto encontrado
			data_table = GetHelp(_req.getParameter("cod_help"));
		}
		else if( _req.getParameter("cod_help")==null && _req.getParameter("texto_buscar")!=null)
			//**Se esta solicitando buscar texto en ayudas registradas
			data_table = GetTextHelp(_req.getParameter("texto_buscar"));
			
		//**Si hay texto de busqueda, enviar a resaltar Help
		if (_req.getParameter("cod_help")!=null && _req.getParameter("texto_buscar")!=null)
			data_table = ResaltarText(data_table, _req.getParameter("texto_buscar"));
		
		if(data_table.equals(""))
			data_table = getResource("help_no_disp.txt");
		
		//Asignar url para componentes staticos
		data_table = Util.replace(data_table,"@url_images@",url_image);
		data_table = Util.replace(data_table,"@url_css@","../css/");
		
		//Asignar contenido en dataset 
		dsHelp.setValue("help", data_table);

		//publicar dataset
		storeDataSet("help",dsHelp); 
	}

	/**
	 * Retornar HTML de ayuda a Mostrar seg&uacute;n el codigio de la ayuda dde entrada
	 * Si el ind:buscar eas true, significa que se debera resaltar el texto de entrada
	 * @param cod_help
	 * @return String
	 * @throws Exception
	 */
	 public String GetHelp(String cod_help) throws Exception{

		java.sql.ResultSet rs = null;

		try{
			if (cod_help == null)
				cod_help ="";
				
			//Preparar Variable de Conneccion para realizar consultas JDBC
			stmt = con.createStatement();
			StringBuffer buff = new StringBuffer();
			
			//long requerido para especificar desde donde se quiere obtener el campo tipo clob
			long aux_long = 1;
			
			//Obtener Inf. de la DB
			String sql = Util.replace(ReplaceParamHelp(getResource("get_help.sql")),"@cod_help@", cod_help);
			rs = stmt.executeQuery(sql);
			
			//armar menu de contenido para los temas
			if (rs.next()){//Convertir en String en campo LOB						
				if(rs.getClob("contenido_help")!=null)				
					buff.append(rs.getClob("contenido_help").getSubString(aux_long,(int)rs.getClob("contenido_help").length()));
				else
					buff.append(getResource("help_no_disp.txt"));
			}else
				buff.append("");
			
			//retornar String 
			return buff.toString();
		}
		catch(Exception e){
			throw new Exception(e.getMessage());
		}
		finally{
			//Cerrar conexion con DB 
			try { 
				if ( con != null ) {
	                if ( rs != null ) 
	                    rs.close();
	                if ( stmt != null ) 
	                    stmt.close();
	                //cerrar conexi&oacute;n con DB
	                con.close();
	            }
	        }
	        catch( Exception e) {
	        	throw new Exception("Error: Cerrando Conexi&oacute;n a DB" + e.getMessage());
			}
	    }
	 
	 }
	 
	 
	/**
	 * Retornar men&uacute; de contenido donde se encontro el texto requerido
	 * @param texto_buscar
	 * @return String
	 * @throws Exception
	 */
	 public String GetTextHelp(String texto_buscar) throws Exception{

		if (texto_buscar == null || texto_buscar.length()==0)
			return getResource("buscar_vacio.txt");
		
		//Remplazar simbolos de mayor y menos por texto html
		texto_buscar = Util.replace( texto_buscar , "<" , "&lt;");
		texto_buscar = Util.replace( texto_buscar , ">" , "&gt;");
		
		Page p = null;

	 	//Obtener Inf. de la DB
	 	String sql = ReplaceParamHelp(getResource("get_tema.sql"));
	 	//DataSet dsTema = dbGet(_dso,sql);
	 	DataSet dsTema = db.get(_dso,sql);
		DataSet ds = null;
		String help_temp = "";
		StringBuffer help = new StringBuffer();
		
		StringBuffer header = new StringBuffer(ReplaceParamHelp(getResource("header_buscar.txt")));
		
		//dataSet para uso auxiliar 
		DataSet dsAux = null;
		
		//armar menu de contenido para los temas
		while(dsTema.next()){
			sql = ReplaceParamHelp(getResource("get_contenido_buscar.sql"));
			sql = Util.replace(sql,"@texto_buscar@",texto_buscar);
			sql = Util.replace(sql,"@id_msc_ayuda_online_tema@" ,dsTema.getValue("id_msc_ayuda_online_tema"));
			ds = db.get(_dso,sql);
			
			while (ds.next() ){
				sql = ReplaceParamHelp(getResource("get_data_help.sql"));
				sql = Util.replace(sql,"@nombre_help@", ds.getValue("nombre_help"));
				sql = Util.replace(sql,"@id_msc_ayuda_online_tema@", dsTema.getValue("id_msc_ayuda_online_tema"));
				dsAux = db.get(_dso,sql);
				if (dsAux.next()) {
					ds.setValue("id_msc_ayuda_online_tema", dsAux.getValue("id_msc_ayuda_online_tema") );
					ds.setValue("cod_help", dsAux.getValue("cod_help") );
				}
			}
			
			help_temp = ReplaceParamHelp(getResource("table_result.txt"));	//Tabla de contenido
			help_temp = Util.replace(help_temp,"@id_msc_ayuda_online_tema@",dsTema.getValue("id_msc_ayuda_online_tema"));
			help_temp = Util.replace(help_temp,"@nombre_tema@",dsTema.getValue("nombre_tema"));
			help_temp = Util.replace(help_temp,"@texto_buscar@",texto_buscar);
			help_temp = Util.replace(help_temp,"@url_images@",url_image);
			help_temp = Util.replace(help_temp,"@url_css@","../css/");
			p = new Page(help_temp);			
			p.repeat(ds,"rows");		
			
			if (ds.count()> 0)				
				// Añadir a string principal
				help.append(p.toString());
		}
		
		//no se encontro texto a buscar
		if (help.length() == 0)
			help.append(getResource("buscar_vacio.txt"));  
			
		//armar html de salida
		String title = header.toString();
		title = Util.replace(title,"@result@", help.toString());
		
		return title;
 	
	 }
	 
	 /**
	 * RESALTAR TEXTO BUSCADO en la ayuda.
	 * @param buff, texto_buscar
	 * @return String
	 * @throws Exception
	 */
	 public String ResaltarText(String buff, String texto_buscar) throws Exception{
	 	
	 	if (texto_buscar == null || texto_buscar.length()== 0){
	 		return buff;
	 	}
	 	
	 	int i = 0;
   		int x = 0;
   		String texto_replace = "<span style=\"background-color: #C0C0C0\"><font color=\"#000080\"><b>" + texto_buscar + "</b></font></span>";
	    StringBuffer result = new StringBuffer();

		//Verificar texto a buscar
        while ((x = buff.indexOf(texto_buscar, i)) >= 0) {
            
            //adicionar a la salida el texto verificado
            result.append(buff.substring(i, x));
            	
            //Verificar que el texto a buscar no este dentro de una etiqueta
            if ((buff.indexOf(">", x)) > (buff.indexOf("<", x)) )
            	//remplazar en el texto de salida
            	result.append(texto_replace);
        	else
        		//mover texto a buscar a la salida
        		result.append(texto_buscar);
        	//ajustar indices para seguir chequeando string
        	i = x + texto_buscar.length();
        }
        result.append(buff.substring(i));
   		
	   	//return html
	   	return result.toString();
	 	
	 }	
	 
	 /**
	 * Remplazar en el String de entrada los parametros de ayuda necesarios
	 * @param content
	 * @return String
	 * @throws Exception
	 */
	 public String ReplaceParamHelp(String content) throws Exception{
	 	
	 	content = Util.replace(content,"@table_tema@","msc_ayuda_online_tema");
	 	content = Util.replace(content,"@table_help@","msc_ayuda_online");
	 
	 	//return query
	 	return content;
	 	
	 }
	 

}
