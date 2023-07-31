package models.menu_items;

import megasoft.*;
import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;
	private DataSet _temp = null;
	private DataSet _applics = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		/*if(_req.getParameter("band")==null){//limpiar id_role del record al entrar la primera vez
			_record.setValue("id_application", "");			
		}*/

		String sql = "";
		
		_table = new DataSet();
		_table.append("tab",java.sql.Types.VARCHAR);
		_table.append("nombre_en_bd",java.sql.Types.VARCHAR);
		_table.append("st_nombre",java.sql.Types.VARCHAR);
		_table.append("st_url",java.sql.Types.VARCHAR);
		_table.append("no_parent",java.sql.Types.VARCHAR);
		_table.append("nu_level",java.sql.Types.VARCHAR);
		_table.append("nu_orden",java.sql.Types.VARCHAR);
		_table.append("id",java.sql.Types.VARCHAR);
		
		_table.append("applic",java.sql.Types.VARCHAR);
		
		//campo con columna para mostrar la aplicaci&oacute;n a la cual pertenece la opci&oacute;n
		_table.append("columna_applic",java.sql.Types.VARCHAR);
		
		
		//PARA TRABAJAR MULTI-APLICACI&Oacute;N
		sql = getResource("get_aplicaciones.sql");
		
		_applics = db.get(_dso, sql);	//APLICACIONES	
		
		
		//ensamblar SQL
		//Buscar opciones principales del menu (nivel 1)		
		sql = getResource("browse.sql");

		//Filtrar opciones por id_aplicacion, si es nulo, mostrar todas las opciones
		if(_record.getValue("id_application")!=null && _req.getParameter("band")!=null){
	
			sql = Util.replace(sql, "@filtro_aplicacion@", "and MSC_MENU_ITEMS.id_application = "+_record.getValue("id_application"));
			
			//Ocultar cabecera de columna de aplicaci&oacute;n si se ha seleccionado una en particular
			_record.setValue("cab_applic", "<div style="+"display:none"+">"+"&nbsp"+"</div>");			
		}else{
			
			sql = Util.replace(sql, "@filtro_aplicacion@", "");
			
			//Colocar cabecera columna de aplicaci&oacute;n si no se ha especificado una en particular
			_record.setValue("cab_applic", "<th> Aplic. </th>");
		}

		
		//guardar opciones del men&uacute; del nivel 1 
		_temp = db.get(_dso, sql);
		
		//Por cada opcion principal del menu, buscar las opciones derivadas (hijos)
		//para un ordenamiento por jerarquia de menu
		while(_temp.next()){

			//Guardar la informaci&oacute;n de la opci&oacute;n principal
			_table.addNew();
			_table.setValue("nombre_en_bd", _temp.getValue("st_nombre"));
			_table.setValue("st_nombre", "<b>"+_temp.getValue("st_nombre")+"</b>");
			_table.setValue("st_url", _temp.getValue("st_url"));
			_table.setValue("no_parent", "");
			_table.setValue("nu_level", _temp.getValue("nu_level"));
			_table.setValue("nu_orden", _temp.getValue("nu_orden"));
			_table.setValue("id", _temp.getValue("id"));
			
			_table.setValue("applic", _temp.getValue("siglas_applic"));
			
			if(_record.getValue("id_application")!=null && _req.getParameter("band")!=null)
				//Ocultar columna de aplicaci&oacute;n si se ha seleccionado una en particular
				_table.setValue("columna_applic", "<div style="+"display:none"+">"+"&nbsp"+"</div>");			
			else
				//Colocar columna de aplicaci&oacute;n si no se ha especificado una en particular
				_table.setValue("columna_applic", "<td>"+ _temp.getValue("siglas_applic")+ "</td>");
				
				
			//buscar los hijos de la opcion del menu
			if(Integer.parseInt(_temp.getValue("nu_hijos"))>0){
				_table.setValue("st_nombre", _table.getValue("st_nombre")+"<b>:</b>");
				
				this.buscar_hijos(_temp.getValue("id"), _table.getValue("columna_applic"), _temp.getValue("siglas_applic"));
			}
		}

		
 
		//registrar los datasets exportados por este modelo
		storeDataSet("table", _table);
		storeDataSet("applics", _applics);
		storeDataSet("record", _record);


		
	}
	
	/** Metodo recursivo para buscar los hijos de cada opci&oacute;n del menu (opciones derivadas)
	 * 
	 * @param id_padre
	 * @throws Exception
	 * @author Erika Valerio
	 */
	public void buscar_hijos(String id_padre, String colum_aplicacion, String siglas_applic) throws Exception{
		
		String sql = "select mi.id, mi.st_nombre, mi.st_url, mi.nu_hijos, mi.nu_orden, mi.nu_level,"+
					 " (select st_nombre from MSC_MENU_ITEMS where id = mi.nu_parent) as no_parent"+
					 " from MSC_MENU_ITEMS mi "+ 
					 " where mi.nu_parent = " + id_padre+
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
			_table.setValue("nombre_en_bd",  _aux.getValue("st_nombre"));
			_table.setValue("st_nombre", _aux.getValue("st_nombre"));
			_table.setValue("st_url", _aux.getValue("st_url"));
			_table.setValue("no_parent", _aux.getValue("no_parent"));
			_table.setValue("nu_level", _aux.getValue("nu_level"));
			_table.setValue("nu_orden", _aux.getValue("nu_orden"));
			_table.setValue("id", _aux.getValue("id"));
			_table.setValue("columna_applic", colum_aplicacion);	
			_table.setValue("applic", siglas_applic);
				
			//Si la opci&oacute;n a su vez posee hijos, hacer una busqueda de ellos 
			//y asi sucesivamente 
			if(Integer.parseInt(_aux.getValue("nu_hijos"))>0){
				
				//resaltar opci&oacute;n padre en el htm
				_table.setValue("st_nombre", "<b>"+_aux.getValue("st_nombre")+":</b>");
				
				//buscar hijos (Llamada recursiva)
				buscar_hijos(_aux.getValue("id"), colum_aplicacion, siglas_applic);
			}
		}
		
	}

}
