package models.menu_role;

import megasoft.*;import models.msc_utilitys.*;

public class Edit extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;
	private DataSet _menu = null;
	private DataSet _menusel = null;
	private DataSet _applics = null;
	private DataSet _temp = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
			
		_menu = new DataSet();
		_menu.append("tab",java.sql.Types.VARCHAR);
		_menu.append("st_nombre",java.sql.Types.VARCHAR);
		_menu.append("nu_parent",java.sql.Types.VARCHAR);
		_menu.append("id",java.sql.Types.VARCHAR);
		

		//ensamblar SQL
		String sqls = getResource("browsesel.sql");
		sqls = Util.replace( sqls, "@id@", _req.getParameter("msc_role_id") );
		
		//crear dataset
		_menusel = db.get( _dso, sqls);

		//registrar los datasets exportados por este modelo
		storeDataSet("menusel", _menusel);




		_dso = db.getDataSource( _app.getInitParameter("datasource") );
		
		//ensamblar SQL
		String sql = getResource("getrecord.sql");
		sql = Util.replace( sql, "@id@", _req.getParameter("msc_role_id") );

		//crear dataset
		_table = db.get( _dso, sql );
		
		//PARA TRABAJAR MULTI-APLICACI&Oacute;N
		sql = getResource("get_aplicaciones.sql");
		
		_applics = db.get(_dso, sql);	//APLICACIONES	
		
		//Si se ha seleccionado una aplicaci&oacute;n: filtrar opciones del men&uacute;
		if(_req.getParameter("id_application")!=null && !_req.getParameter("id_application").equals("") && _req.getParameter("band")!=null){
			
			this.getItemsMenu(_req.getParameter("id_application"));
			
			storeDataSet("menu", _menu);
			
		}else{
			
			_menu = new DataSet();
			
			//registrar los datasets exportados por este modelo
			storeDataSet("menu", _menu);

		}

		/////////////////////////////////////////////////////////////////////////
		
		//registrar los datasets exportados por este modelo
		storeDataSet( "table", _table );
		storeDataSet( "applics", _applics );
		storeDataSet( "record", _record );
		
	}
	
	/** M&eacute;todo para seleccionar items del men&uacute; de acuerdo a una aplicaci&oacute;n
	 * NOTA: para men&uacute;es de una sola aplicaci&oacute;n se elimina el param&eacute;tro id_aplic
	 * @param id_aplic
	 * @throws Exception
	 * @author Erika Valerio
	 */
	public void getItemsMenu(String id_aplic) throws Exception{
		//Buscar opciones principales del menu (nivel 1)
		String sql = getResource("get_menu_principal.sql");
		sql = Util.replace(sql, "@id@", id_aplic);

		
		_temp = db.get(_dso, sql);
		
		//Por cada opcion principal del menu, buscar las opciones derivadas (hijos)
		//para un ordenamiento por jerarquia de menu
		while(_temp.next()){

			//Guardar la informaci&oacute;n de la opci&oacute;n principal
			_menu.addNew();
			_menu.setValue("st_nombre", "<b>"+_temp.getValue("st_nombre")+"</b>");			
			_menu.setValue("nu_parent", "");
			_menu.setValue("id", _temp.getValue("id"));

			//buscar los hijos de la opcion del menu
			if(Integer.parseInt(_temp.getValue("nu_hijos"))>0){
				_menu.setValue("st_nombre", _menu.getValue("st_nombre")+"<b>:</b>");
				
				this.buscar_hijos(_temp.getValue("id"), id_aplic);
			}
		}

	}
	
	/** Metodo recursivo para buscar los hijos de cada opci&oacute;n del menu (opciones derivadas)
	 * NOTA: para men&uacute;es de una sola aplicaci&oacute;n se elimina el param&eacute;tro id_aplic
	 * @param id_padre
	 * @throws Exception
	 * @author Erika Valerio
	 */
	public void buscar_hijos(String id_padre, String id_aplic) throws Exception{
		
		String sql = "select mi.id, mi.st_nombre, mi.st_url, mi.nu_hijos, mi.nu_orden, mi.nu_level, mi.nu_parent,"+
					 " (select st_nombre from MSC_MENU_ITEMS where id = mi.nu_parent) as no_parent"+
					 " from MSC_MENU_ITEMS mi "+ 
					 " where mi.nu_parent = " + id_padre +
					 " and id_application=" + id_aplic +
					 " order by mi.nu_orden";
		
		DataSet _aux = new DataSet();
		
		_aux = db.get(_dso, sql);
		
		while(_aux.next()){	
			
			//tabulaciones para la visualizacion jer&aacute;rquica del menu
			String tabulaciones = "";
			for(int i=0;i<Integer.parseInt(_aux.getValue("nu_level"));i++){
				tabulaciones += "---";
			}
			
			//Guardar la informaci&oacute;n de la opci&oacute;n hija
			_menu.addNew();
			_menu.setValue("tab", tabulaciones);		
			_menu.setValue("st_nombre", _aux.getValue("st_nombre"));			
			_menu.setValue("nu_parent", _aux.getValue("no_parent"));			
			_menu.setValue("id", _aux.getValue("id"));
		
				
			//Si la opci&oacute;n a su vez posee hijos, hacer una busqueda de ellos 
			//y asi sucesivamente 
			if(Integer.parseInt(_aux.getValue("nu_hijos"))>0){
				
				//resaltar opci&oacute;n padre en el htm
				_menu.setValue("st_nombre", "<b>"+_aux.getValue("st_nombre")+":</b>");
				
				//buscar hijos (Llamada recursiva)
				buscar_hijos(_aux.getValue("id"), id_aplic);
			}
		}
		
	}

}
