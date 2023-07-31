package models.menu_items;

import megasoft.*;import models.msc_utilitys.*;

public class Insert extends MSCModelExtend
{

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		//generar unique ID
		_record.setValue("id", dbGetSequence(_dso, "MSC_MENU_ITEMS") );

		//ensamblar SQL
		String sql = getResource("insert.sql");
		sql = db.sql(sql, _record);
		db.exec( _dso, sql);

		String sql_padre = getResource("updatepadre.sql");
	   sql_padre = Util.replace(sql_padre,"@id@",_record.getValue("id"));

		//ejecutar query
		db.exec( _dso, sql_padre);

	}

		

}
