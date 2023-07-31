package models.menu_items;

import megasoft.*;import models.msc_utilitys.*;

public class Delete extends MSCModelExtend
{
private DataSet _padre = null;
private DataSet _bros = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
	String idm = _req.getParameter("id");
	
	String sqlp = getResource("getpadre.sql");
	sqlp = Util.replace(sqlp,"@id@",idm);
	
	//crear dataset
	_padre = db.get( _dso, sqlp);
	String padre="";

	if (_padre.count()!=0)
	{
		_padre.first();
		_padre.next();
		padre = _padre.getValue("nu_parent");

		

	}

		//ensamblar SQL
		String sqlh = getResource("deletehijos.sql");
		sqlh = Util.replace( sqlh, "@padre@",  idm);
		//ejecutar query
		db.exec( _dso, sqlh );		

		//ensamblar SQL
		String sql = getResource("delete.sql");
		sql = Util.replace( sql, "@id@",  _req.getParameter("id") );
		//ejecutar query
		db.exec( _dso, sql );


		String sql_padre = getResource("updatepadredel.sql");
	   	sql_padre = Util.replace(sql_padre,"@id@",idm);
	   	sql_padre = Util.replace(sql_padre,"@padre@",padre);


		//ejecutar query
		db.exec( _dso, sql_padre);

		//REORDENAR
		String sql_br = getResource("selectbro.sql");
	   	sql_br = Util.replace(sql_br,"@padre@",padre);
		_bros = db.get( _dso, sql_br);
		String sql_orden = "";
		int i = 0;
		 while  (_bros.next())
		{
			i ++;
			sql_orden = getResource("updateorden.sql");
	   		sql_orden = Util.replace(sql_orden,"@id@",_bros.getValue("id"));
	   		sql_orden = Util.replace(sql_orden,"@orden@",String.valueOf(i));
			db.exec( _dso, sql_orden);
		}
		
		
		

	}

}
