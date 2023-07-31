package models.menu_show;

import megasoft.*;

import java.io.*;

/**
 * Clase encargada de obtener todas las opciones de men&uacute; a las cuales tiene acceso el usuario que acaba de iniciar sesi&oacute;n de acuerdo a su rol y a la aplicaci&oacute;n que est&aacute; invocando. A su vez, genera el archivo javascript que contendr&aacute; la informaci&oacute;n recuperada organizada en forma de men&uacute; para ser mostrada en la aplicaci&oacute;n. 
 * Actions asociados: menu_show-find. 
 * @author Megasoft Computaci&oacute;n. 
 */
public class TableDinamico extends AbstractModel
{
 
	/** DataSet del modelo */
	private DataSet _table = null;
	private DataSet _tablec = null;
	private DataSet _primeros = null;
	private DataSet _hijos = null;
	private DataSet _roles = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */	
	public void execute() throws Exception
	{
			_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
		
			//String dir = _app.getInitParameter("menu-dir");
			//Se reemplazo el valor del parametro menu-dir por el metodo getRealPath
			String dir = _app.getRealPath("js/menu");
				
			//Identificar el rol del usuario que esta ensesion
			String rolename = "";
			String sql_rol = getResource("getroles.sql");
			_roles = db.get( _dso, sql_rol);
			
			if (_roles.count()!=0)
			{
				_roles.first();
				while (_roles.next())
				{
				String role = _roles.getValue("rolename");		
						
				if (isUserInRole(role))
				{
					rolename=_roles.getValue("rolename");			
					break;
				}
				}
			}
		
		    	File Archivo = new File(dir +"/"+ rolename + ".js");
		
				FileOutputStream dfs = new FileOutputStream(Archivo);
		
			    PrintStream salida = new PrintStream(dfs);
		
			String primero = getResource("primernivel.sql");
			
			primero = Util.replace(primero,"@rolename@",rolename);
			//Reemplazar nombre de la aplicaci&oacute;n:
			primero = Util.replace(primero, "@app_name@", getAppProperty("app-name"));

			String header= getResource("header.txt");

			//Buscar cuantos Items hay en el primer nivel (NoOffFirstLineMenus=n)
			_primeros = db.get( _dso, primero);
		   	header = Util.replace(header,"@primer@",String.valueOf(_primeros.count()));
			salida.println(header);
		
			String num = "";
			String padre = "";
			String sql = getResource("browse.sql");
			sql = Util.replace(sql,"@rolename@",rolename);
			//Reemplazar nombre de la aplicaci&oacute;n:
			sql = Util.replace(sql, "@app_name@", getAppProperty("app-name"));

			//crear dataset
			_table = db.get( _dso, sql);
			_tablec = db.get( _dso, sql);
		
		
			String nivel = "";
			String hij = "";
			int i = 1;
			if (_table.count()!=0)
			{
			_table.first();
			_table.next();
			
		
			nivel = _table.getValue("nu_level");
			padre = _table.getValue("nu_parent");
		
			_table.setValue("nu_orden", "1");
		
			//REORDENA Y ASIGNA HIJOS SEGUN LAS OPCIONES PERMITIDAS PARA EL ROLE
		
			hij=getResource("cuantoshijos.sql");
			hij = Util.replace(hij,"@rolename@",rolename);
			
			//Reemplazar nombre de la aplicaci&oacute;n:
			hij = Util.replace(hij, "@app_name@", getAppProperty("app-name"));
		
			String hijo = Util.replace(hij,"@id@",_table.getValue("id"));

			_hijos = db.get( _dso, hijo);
			_hijos.first();
			_hijos.next();
		
			_table.setValue("nu_hijos", _hijos.getValue("num"));
		
			   while  (_table.next())
			   {
				if ((nivel.equals(_table.getValue("nu_level")))&& (padre.equals(_table.getValue("nu_parent"))))
					i ++;
				else
				{
					i = 1;
					nivel = _table.getValue("nu_level");
					padre = _table.getValue("nu_parent");
				}
				_table.setValue("nu_orden", String.valueOf(i));
				hijo = Util.replace(hij,"@id@",_table.getValue("id"));

				_hijos = db.get( _dso, hijo);
				_hijos.first();
				_hijos.next();
				_table.setValue("nu_hijos", _hijos.getValue("num"));
			   }
			}
		
			i = 1;
			if (_tablec.count()!=0)
			{
			_tablec.first();
			_tablec.next();
			nivel = _tablec.getValue("nu_level");
			padre = _tablec.getValue("nu_parent");
			_tablec.setValue("nu_orden", "1");
			hij=getResource("cuantoshijos.sql");
			hij = Util.replace(hij,"@rolename@",rolename);
			
			//Reemplazar nombre de la aplicaci&oacute;n:
			hij = Util.replace(hij, "@app_name@", getAppProperty("app-name"));
		
			String hijoc = Util.replace(hij,"@id@",_tablec.getValue("id"));
			_hijos = db.get( _dso, hijoc);
			_hijos.first();
			_hijos.next();
			_tablec.setValue("nu_hijos", _hijos.getValue("num"));
		
			while  (_tablec.next())
			   {
				if ((nivel.equals(_tablec.getValue("nu_level"))) && (padre.equals(_tablec.getValue("nu_parent"))))
					i ++;
				else
				{
					i = 1;
					nivel = _tablec.getValue("nu_level");
					padre = _tablec.getValue("nu_parent");
				}
				_tablec.setValue("nu_orden", String.valueOf(i));
				hijoc = Util.replace(hij,"@id@",_tablec.getValue("id"));
				_hijos = db.get( _dso, hijoc);
				_hijos.first();
				_hijos.next();
				_tablec.setValue("nu_hijos", _hijos.getValue("num"));
		
			   }
			}
		
		//CONSTRUYE EL MENU EN EL PRUEBA.JS
		
		if (_table.count()!=0)
		{
		_table.first();
			while (_table.next())
		  {
			boolean esHijo=true;
			if (_table.getValue("nu_parent").equals("0"))
			{
				esHijo=false;
			}
		
		        padre = _table.getValue("nu_parent");
			num = _table.getValue("nu_orden");
			while (esHijo==true)
			{
		
				_tablec.first();
				String ordenpadre = "";
				String padrepadre = "";
				while (_tablec.next())
				{
				 if (_tablec.getValue("id").equals(padre))
				 	{
				 	ordenpadre = _tablec.getValue("nu_orden");
				 	padrepadre = _tablec.getValue("nu_parent");
		
					break;
				 	}
				}
				num = ordenpadre + "_" + num;
		
		
				if (padrepadre.equals("0"))
				{
					esHijo=false;
				}
				else
				{
					padre=padrepadre;
				}
		
		
			}
		String pr= getResource("table.txt");
		
		
			    pr = Util.replace(pr,"@numero@",num);
			    pr = Util.replace(pr,"@st_nombre@",_table.getValue("st_nombre"));
			if (_table.getValue("st_url")!= null)
		{
			    pr = Util.replace(pr,"@st_url@",_table.getValue("st_url"));
		}
		else
		{
			    pr = Util.replace(pr,"@st_url@","");
		}
			    pr = Util.replace(pr,"@nu_hijos@",_table.getValue("nu_hijos"));
			    pr = Util.replace(pr,"@nu_heigth@",_table.getValue("nu_heigth"));
			    pr = Util.replace(pr,"@nu_width@",_table.getValue("nu_width"));
			    salida.println(pr);
		   }
		}
				//registrar los datasets exportados por este modelo
				storeDataSet("table", _table);
				//se monta el dataset en sesion
				//_req.getSession().setAttribute("generardinamichtml", _table);
	
		dfs.close();
			
	}


}
