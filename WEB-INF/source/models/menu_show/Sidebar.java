package models.menu_show;

import megasoft.AbstractModel;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;

/**
 * Clase encargada de generar en menu dinamico desplegable,esta atado a sepa
 * Actions asociados: menu_show-sidebar
 * @author elaucho.
 *
 */
public class Sidebar extends AbstractModel
{
	DataSet dinamicHtml			=new DataSet();
	DataSet _padres				=new DataSet();
	String role_id="";
	/** DataSet del modelo */
	 
	private DataSet _roles = null;
	

	/**
	 * Ejecuta la transaccion del modelo
	 */
	DataSet _table = new DataSet();
	public void execute() throws Exception
	{
		
	//Valores para reportes
	System.setProperty("java.awt.headless","true");
	System.setProperty("java.awt.toolkit","com.eteks.awt.PJAToolkit");
	System.setProperty("java2d.font.usePlatformFont","false");
		
	
	_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
	DataSet _table = new DataSet();
 	_table.append("rolename" , java.sql.Types.VARCHAR);
 	_table.append("username" , java.sql.Types.VARCHAR);
	//Identificar el rol del usuario que esta ensesion
	String rolename = "";
	String sql_rol = getResource("getroleside.sql");
	

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
	_table.addNew();
	_table.setValue("rolename",rolename);
	_table.setValue("username",getUserName());

	storeDataSet("table", _table);
//Se busca el rol del usuario conectado
	String nombreUsuario=getSessionObject("framework.user.principal").toString();
	StringBuffer userid=new StringBuffer();
	userid.append("select ru.MSC_ROLE_ID from MSC_ROLE_USER ru, MSC_USER u,msc_applications,msc_role where ru.MSC_USER_ID = u.MSC_USER_ID  and ru.msc_role_id=msc_role.msc_role_id and msc_applications.id_application=msc_role.id_application and UPPER(u.USERID)=UPPER('");
	userid.append(nombreUsuario).append("')");
	userid.append("and msc_applications.siglas_applic ='"+AppProperties.getProperty("app-name")).append("'");
	
	DataSet usuario = new DataSet();
	
	usuario = db.get(_dso, userid.toString());
	if(usuario.count()>0){
		usuario.first();
		usuario.next();
		role_id=usuario.getValue("msc_role_id");
	}
//Se crea los padres a que tiene acceso
	StringBuffer padres=new StringBuffer();
	padres.append("select MSC_MENU_ITEMS.*,MSC_MENU_ROLES.ID_ROLE from MSC_MENU_ITEMS left join MSC_MENU_ROLES on MSC_MENU_ITEMS.id=MSC_MENU_ROLES.ID_MENU left join MSC_APPLICATIONS ap on ap.id_application = MSC_MENU_ITEMS.ID_APPLICATION where MSC_MENU_ROLES.ID_ROLE=");
	padres.append(role_id);
	padres.append(" and MSC_MENU_ITEMS.nu_level=1 and MSC_MENU_ITEMS.nu_enable=1 and ap.siglas_applic = '"+AppProperties.getProperty("app-name")+ "' order by MSC_MENU_ITEMS.nu_orden");
	
	_padres=db.get(_dso, padres.toString());
	
	DataSet _tableDinamic		=_padres;
	dinamicHtml.append("campo_dinamico",java.sql.Types.VARCHAR);//Se crea el campo correspondiente
	if(_tableDinamic.count()>0){
		_tableDinamic.first();
		//_tableDinamic.next();
		while(_tableDinamic.next()){//Recorre la opcion padre la agrega al html y busca los hijos correspondientes
			if(_tableDinamic.getValue("nu_level").equals("1")){
				if(_tableDinamic.cursorPos()>0){
					StringBuffer categoria_cerrar=new StringBuffer();
					categoria_cerrar.append("</li></ul>");
					dinamicHtml.addNew();
					dinamicHtml.setValue("campo_dinamico",categoria_cerrar.toString());
				}
				//Buscar hijos en caso de que no tenga se cumple el if
				boolean th=tieneHijos(_tableDinamic.getValue("id"));
				//<h3 class='menuheader closedsubheader' ><img src='../images/arrow_left.gif'/><a href='convert_data-find' target='view'>Salir</a></h3>
				if(!th){
					String href=_tableDinamic.getValue("st_url");
					if(_tableDinamic.getValue("st_url")==null){
						href="show-view";
					}
					dinamicHtml.addNew();
					StringBuffer sb1=new StringBuffer();
					sb1.append("<h3 class='menuheader' ");
					sb1.append("onclick=\"top.frames[2].location.href='");
					sb1.append(href).append("'\">");
					sb1.append("<img src='../images/arrow_left.gif'/>&nbsp;");
					sb1.append(_tableDinamic.getValue("st_nombre"));
					sb1.append("</h3>");
					dinamicHtml.setValue("campo_dinamico",sb1.toString());
				}else{
				dinamicHtml.addNew();
				StringBuffer sb=new StringBuffer();
				sb.append("<h3 class='menuheader expandable'><img src='../images/arrow_left.gif'/>&nbsp;");
				sb.append(_tableDinamic.getValue("st_nombre")).append("</h3>");
				dinamicHtml.setValue("campo_dinamico",sb.toString());
				buscar_hijos(_tableDinamic.getValue("id"));//Busca los hijos por cada opcion padre
				
				}
			}
		}// fin while
	}
		storeDataSet("dinamico",dinamicHtml);
	}//fin execute
	/** Metodo recursivo para buscar los hijos de cada opci&oacute;n del menu (opciones derivadas)
	 * 
	 * @param id_padre
	 * @throws Exception
	 */
	public void buscar_hijos(String id_padre) throws Exception{//busca los hijos de manera recursiva y dibuja el html
		//Query para buscar los hijos de la opcion padre asociados a un Rol especifico
		String sql = "select r.ROLENAME, r.msc_role_id as id_role,mi.id, mi.st_nombre, mi.st_url, mi.nu_hijos, mi.nu_orden, mi.nu_level,"+
					 " (select st_nombre from MSC_MENU_ITEMS where id = mi.nu_parent) as no_parent"+
					 " from MSC_MENU_ITEMS mi "+
					 "left join MSC_MENU_ROLES on mi.id=MSC_MENU_ROLES.ID_MENU "+
					 " left join MSC_ROLE r on MSC_MENU_ROLES.ID_ROLE = r.MSC_ROLE_ID"+
					 " where mi.nu_parent = " + id_padre+
					 " and mi.nu_enable=1 and MSC_MENU_ROLES.ID_ROLE="+role_id+
					 " order by mi.nu_orden";
		//Creacion del Dataset para recorrer
		DataSet _aux = new DataSet();
		_aux = db.get(_dso, sql);
		if(_aux.count()>0){
			_aux.first();
			_aux.next();
			if(_aux.getValue("nu_level").equalsIgnoreCase("2")){//Si es nivel 2 class es igual a categoryitems
			StringBuffer categoria=new StringBuffer();
			categoria.append("<ul class='categoryitems'>");
			dinamicHtml.addNew();
			dinamicHtml.setValue("campo_dinamico",categoria.toString());
			}else{//class es igual a subcategoryitems
				if(_aux.getValue("nu_level").equalsIgnoreCase("4")){	
				StringBuffer categoria1=new StringBuffer();
				categoria1.append("<ul class='subcategoryitems1' style='margin-left: 15px'> ");
				dinamicHtml.addNew();
				dinamicHtml.setValue("campo_dinamico",categoria1.toString());
				}else{
					StringBuffer categoria1=new StringBuffer();
					categoria1.append("<ul class='subcategoryitems' style='margin-left: 15px'> ");
					dinamicHtml.addNew();
					dinamicHtml.setValue("campo_dinamico",categoria1.toString());
				}
			}
			_aux.first();
			int i=0;
			while(_aux.next()){	//Se recorre el dataset
				 i++;
				if(Integer.parseInt(_aux.getValue("nu_hijos"))>0){
				
					if(_aux.cursorPos()>0 && !_aux.getValue("nu_orden").equalsIgnoreCase("1") && !_aux.getValue("nu_orden").equalsIgnoreCase("0") && !_aux.getValue("nu_level").equalsIgnoreCase("1")){
						dinamicHtml.addNew();
						dinamicHtml.setValue("campo_dinamico","</li>");
					}
					//buscar hijos (Llamada recursiva)
					//dibujar hijos
					StringBuffer sb_sub_menu	=new StringBuffer();
					String valor				="";
					String clase				="";
					String target				="";
					String li					="";
					String cierreUl				="";
					boolean generarPadreHijo	=tieneHijos(_aux.getValue("id"));
					if(generarPadreHijo){
						if(_aux.getValue("nu_level").equalsIgnoreCase("3")){
							valor="";
							clase=" class='subexpandable1'>";
							target=" target='view'";
							li="";
							
						}else{
							valor="";
							clase=" class='subexpandable'>";
							target=" target='view'";
							li="";
						}
					}else{
						valor=_aux.getValue("st_url");
						/*
						 * Si valor es null se hace referencia al show view
						 */
						if(_aux.getValue("st_url")==null){
							valor="show-view";
						}
						target=" target='view'>";
						li="</li>";
						cierreUl="";
					}
					sb_sub_menu.append(cierreUl).append("<li><a href='").append(valor).append("'").append(target);
					sb_sub_menu.append(clase).append(_aux.getValue("st_nombre")).append("</a>").append(li);
					dinamicHtml.addNew();
					dinamicHtml.setValue("campo_dinamico",sb_sub_menu.toString());
					//Si la opci&oacute;n a su vez posee hijos, hacer una busqueda de ellos 
					//y asi sucesivamente
					buscar_hijos(_aux.getValue("id"));
				}else{
					
					//dibujar hijos
					StringBuffer sb_sub_menu1	=new StringBuffer();
					String valor="";
					String clase="";
					String target="";
					String li="";
					boolean generarPadreHijo	=tieneHijos(_aux.getValue("id"));
					if(generarPadreHijo){
						valor="";
						clase=" class='subexpandable'>";
						target=" target='view'";
						li="</li>";
					}else{
						valor=_aux.getValue("st_url");
						/*
						 * Si valor es null se hace referencia al show view
						 */
						if(_aux.getValue("st_url")==null){
							valor="show-view";
						}
						target=" target='view'>";
						li="</li>";
					}
					sb_sub_menu1.append("<li><a href='").append(valor).append("'").append(target);
					sb_sub_menu1.append(clase).append(_aux.getValue("st_nombre")).append("</a>").append(li);
					dinamicHtml.addNew();
					dinamicHtml.setValue("campo_dinamico",sb_sub_menu1.toString());
				}
			}
			if(!_aux.next()){//se agrego
				dinamicHtml.addNew();
				dinamicHtml.setValue("campo_dinamico","</ul>");	
				}
	  }
	}
	public boolean tieneHijos(String id_padre) throws Exception{
		boolean th=false;
		String sql = "select r.ROLENAME, r.msc_role_id as id_role, mi.id, mi.st_nombre, mi.st_url, mi.nu_hijos, mi.nu_orden, mi.nu_level,"+
		 " (select st_nombre from MSC_MENU_ITEMS where id = mi.nu_parent) as no_parent"+
		 " from MSC_MENU_ITEMS mi "+
		 "left join MSC_MENU_ROLES on mi.id=MSC_MENU_ROLES.ID_MENU "+
		 " left join MSC_ROLE r on MSC_MENU_ROLES.ID_ROLE = r.MSC_ROLE_ID"+
		 " where mi.nu_parent = " +id_padre+
		 " and mi.nu_enable=1 and MSC_MENU_ROLES.ID_ROLE="+role_id+
		 " order by mi.nu_orden";

				//Creacion del Dataset para recorrer
				DataSet _aux = new DataSet();
				_aux = db.get(_dso, sql);
				if(_aux.count()>0){
					th=true;
				}
		return th;
	}
}//fin clase