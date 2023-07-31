package models.menu_role;

import megasoft.*;import models.msc_utilitys.*;

public class Update extends MSCModelExtend
{

	private DataSet _padre = null;
	private DataSet _roles = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		_dso = db.getDataSource( _app.getInitParameter("datasource") );

		String[] menu;
		String sql;
		
		//Borrar las opciones asociadas al rol en la aplicaci&oacute;n rescpectiva
		String sql_del= getResource("delete.sql");
		sql_del = Util.replace( sql_del, "@rolename@", _record.getValue("rolename"));
		
		//Reemplazar Id de Aplicaci&oacute;n seleccionado (MULTI-APLICACI&Oacute;N)
		sql_del = Util.replace( sql_del, "@id_application@", _record.getValue("id_application"));

		//ejecutar query
		db.exec( _dso, sql_del );
		
		

		menu =  _req.getParameterValues("menusel");

		
		for( int i=0; i < menu.length; i++ )
		{
			//ensamblar SQL
			sql= getResource("insert.sql");
			//generar unique ID
			_record.setValue("id", dbGetSequence(_dso, "object") );

		
			sql = Util.replace( sql, "@id_menu@", menu[i]);
			sql = Util.replace( sql, "@id_role@", _req.getParameter("msc_role_id"));
			
			sql= db.sql(sql, _record);
			
			//ejecutar query
			db.exec( _dso, sql);
		}


	}
public boolean isValid() throws Exception {

		boolean flag = super.isValid();

	//String rolename = "";
	String sql_rol = getResource("getroles.sql");

	_roles = db.get( _dso, sql_rol);

	if (_roles.count()!=0)
	{
		_roles.first();
		//while (_roles.next())
		//{
		//String role = _roles.getValue("rolename");
/*		if (isUserInRole(role))
		{
			//rolename=_roles.getValue("rolename");
			break;
		}*/
		//}
	}



	if (flag) {

		String[] menu;
		boolean existe;
		
		menu =  _req.getParameterValues("menusel");
		if(menu==null)
		{
			flag = false;
			_record.addError("Carta Cr&eacute;dito","Debe seleccionar al menos una Opci&oacute;n de Men&uacute;.");
		}
		else
		{	
		   for( int i=0; i < menu.length; i++ )
		   {
		   existe=false;	
		   String sql1 = getResource("buscarpadre.sql");
		   sql1 = Util.replace(sql1,"@id@",menu[i]);
    		   _padre = db.get( _dso, sql1);
		
		      if (_padre.count()!=0)
		      {
			_padre.first();
			_padre.next();
			if (!_padre.getValue("nu_parent").equals("0"))		
			{	
			
				for( int j=0; j < menu.length; j++ )
				{
					if (menu[j].equals(_padre.getValue("nu_parent")))
					{
				   	existe=true;
					break;
					}
				}
		


				if (existe==false) 
				{

					flag = false;
					_record.addError("Menu Roles",
						"No puede asignar un submenu sin haber asociado el menu principal correspondiente");
					break;
				}
			}
		      }
		    }
		}
	}
return flag;
}

}
