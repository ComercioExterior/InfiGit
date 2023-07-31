package models.menu_items;

import megasoft.*;import models.msc_utilitys.*;

public class ConsultaRoles extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		//ensamblar SQL
		String sql = getResource("roles_asociados_menu.sql");
		sql = Util.replace(sql, "@id@", _record.getValue("id"));
		
		//crear dataset
		_table = db.get( _dso, sql);

		//registrar los datasets exportados por este modelo
		storeDataSet("table", _table);
		
	}

}
