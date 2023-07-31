package models.menu_role;

import megasoft.*;import models.msc_utilitys.*;

public class Insert extends MSCModelExtend
{

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );		
		
		String[] menu;
		String sql;
		
		String sql_del = getResource("delete.sql");
		sql_del = Util.replace( sql_del, "@rolename@", _record.getValue("rolename"));

		//ejecutar query
		db.exec( _dso, sql_del );

		menu =  _req.getParameterValues("menusel");

		
		for( int i=0; i < menu.length; i++ )
		{
		//ensamblar SQL
		sql= getResource("insert.sql");
		//generar unique ID
		_record.setValue("id", dbGetSequence(_dso, "menu_role") );

		
		sql = Util.replace( sql, "@id_menu@", menu[i]);
		sql= db.sql(sql, _record);
		//ejecutar query
		db.exec( _dso, sql);
		}

	}

}
