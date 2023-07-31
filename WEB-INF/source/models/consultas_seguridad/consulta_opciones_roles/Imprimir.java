package models.consultas_seguridad.consulta_opciones_roles;

import megasoft.*;


public class Imprimir extends AbstractModel
{
	/** DataSet del modelo */
	private DataSet _table = null;
	private DataSet _temp = null;
	private DataSet _roles = null;
	private DataSet _aux = null;
	
/**
* Ejecuta la transaccion del modelo
* Invoca los Procedimientos y funciones requeridas para poblar las tablas 
* DETALLE_FACTURA
*/
	public void execute() throws Exception
	{
		String sql = "";			
	   
		/*****Obtener fecha y hora de la impresi&oacute;n*****/
		String Fecha = Util.getDate();
		String Hora = Util.getTime();
		Hora = Hora.substring(0,Hora.indexOf("."));
	   
		DataSet _DateTime = new DataSet();
		_DateTime.append("fecha_hoy",java.sql.Types.DATE);
		_DateTime.append("hora",java.sql.Types.VARCHAR);
		_DateTime.addNew();
		_DateTime.setValue("fecha_hoy",Fecha);
		_DateTime.setValue("hora",Hora);
	
		storeDataSet("fechahora",_DateTime);
	    
		if(_req.getParameter("band")==null){//limpiar id_role del record al entrar la primera vez
			_record.setValue("msc_role_id", "");			
		}
		
		sql = "";
		String q1 = getResource("browse.sql");//buscar opciones principales del menu
		
		_table = new DataSet();
		_table.append("tab",java.sql.Types.VARCHAR);
		_table.append("st_nombre",java.sql.Types.VARCHAR);
		_table.append("st_url",java.sql.Types.VARCHAR);
		_table.append("no_parent",java.sql.Types.VARCHAR);
		_table.append("nu_level",java.sql.Types.VARCHAR);
		_table.append("nu_orden",java.sql.Types.VARCHAR);
		_table.append("id",java.sql.Types.VARCHAR);
		_table.append("applic",java.sql.Types.VARCHAR);		
		
		//campo con columna para mostrar el rol 
		_table.append("colum_role",java.sql.Types.VARCHAR);	
		
			
		if(_record.getValue("msc_role_id")!=null && _req.getParameter("band")!=null){			
			//Ocultar cabecera de columna de aplicaci&oacute;n si se ha seleccionado una en particular
			_record.setValue("cab_role", "<div style="+"display:none"+">"+"&nbsp"+"</div>");			
		}else{											
			//Colocar cabecera columna de aplicaci&oacute;n si no se ha especificado una en particular
			_record.setValue("cab_role", "<th width='20%'> Rol </th>");
		}

		
		sql = getResource("get_roles.sql");
		
		_roles = db.get(_dso, sql);	//ROLES	
		 _aux = db.get(_dso, sql);	//ROLES		
								
		if(_record.getValue("msc_role_id")!=null && _req.getParameter("band")!=null){
			//Buscar opciones para un solo role: el seleccionado por el usuario
			//Dataset auxiliar para roles 
			_aux = new DataSet();
			_aux.append("msc_role_id",java.sql.Types.VARCHAR);
			_aux.append("role_name",java.sql.Types.VARCHAR);	

			_aux.addNew();
			_aux.setValue("msc_role_id", _record.getValue("msc_role_id"));			
			_aux.first();
		}
		
		
		while(_aux.next()){//roles
			
			//ensamblar SQL
			//Buscar opciones principales del menu (nivel 1)		
			sql = q1;
	
			sql = Util.replace(sql, "@msc_role_id@", _aux.getValue("msc_role_id"));
			
			//guardar opciones del men&uacute; del nivel 1 
			_temp = db.get(_dso, sql);
			
			//Por cada opcion principal del menu, buscar las opciones derivadas (hijos)
			//para un ordenamiento por jerarquia de menu
			while(_temp.next()){
								
				//Guardar la informaci&oacute;n de la opci&oacute;n principal
				_table.addNew();
				_table.setValue("st_nombre", "<b>"+_temp.getValue("st_nombre")+"</b>");
				_table.setValue("st_url", _temp.getValue("st_url"));
				_table.setValue("no_parent", "");
				_table.setValue("nu_level", _temp.getValue("nu_level"));
				_table.setValue("nu_orden", _temp.getValue("nu_orden"));
				_table.setValue("id", _temp.getValue("id"));								
				_table.setValue("applic", _temp.getValue("siglas_applic"));
				
					
				if(_record.getValue("msc_role_id")!=null && _req.getParameter("band")!=null){			
				
					//Ocultar cabecera de columna de aplicaci&oacute;n si se ha seleccionado una en particular
					_table.setValue("colum_role", "<div style="+"display:none"+">"+"&nbsp"+"</div>");			
				
				}else{			
					//Colocar cabecera columna de aplicaci&oacute;n si no se ha especificado una en particular
					_table.setValue("colum_role", "<td><b>"+_aux.getValue("role_name") +"</b></td>");
					
				}

				
				//buscar los hijos de la opcion del menu
				if(Integer.parseInt(_temp.getValue("nu_hijos"))>0){
					_table.setValue("st_nombre", _table.getValue("st_nombre")+"<b>:</b>");
					
					this.buscar_hijos(_temp.getValue("id"), _table.getValue("applic"), _aux.getValue("msc_role_id"), _table.getValue("colum_role"));
				}
			}
					
		}
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", _table);
		storeDataSet("roles", _roles);
		storeDataSet("record", _record);

	}
	
	
	/** Metodo recursivo para buscar los hijos de cada opci&oacute;n del menu (opciones derivadas)
	 * 
	 * @param id_padre
	 * @throws Exception
	 * @author Erika Valerio
	 */
	public void buscar_hijos(String id_padre, String aplicacion, String id_role, String role_name) throws Exception{
		
		String sql = "select mi.id, mi.st_nombre, mi.st_url, mi.nu_hijos, mi.nu_orden, mi.nu_level,"+
					 " (select st_nombre from MSC_MENU_ITEMS where id = mi.nu_parent) as no_parent"+
					 " from MSC_MENU_ITEMS mi "+ 
					 " where mi.nu_parent = " + id_padre+
					 " and mi.id IN (select id_menu from MSC_MENU_ROLES where id_role = " +id_role+ ")"+
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
			_table.addNew();
			_table.setValue("tab", tabulaciones);		
			_table.setValue("st_nombre", _aux.getValue("st_nombre"));
			_table.setValue("st_url", _aux.getValue("st_url"));
			_table.setValue("no_parent", _aux.getValue("no_parent"));
			_table.setValue("nu_level", _aux.getValue("nu_level"));
			_table.setValue("nu_orden", _aux.getValue("nu_orden"));
			_table.setValue("id", _aux.getValue("id"));
			_table.setValue("applic", aplicacion);	
			_table.setValue("colum_role", role_name);
				
			//Si la opci&oacute;n a su vez posee hijos, hacer una busqueda de ellos 
			//y asi sucesivamente 
			if(Integer.parseInt(_aux.getValue("nu_hijos"))>0){
				
				//resaltar opci&oacute;n padre en el htm
				_table.setValue("st_nombre", "<b>"+_aux.getValue("st_nombre")+":</b>");
				
				//buscar hijos (Llamada recursiva)
				buscar_hijos(_aux.getValue("id"), aplicacion, id_role, role_name);
			}
		}
		
	}


    
}