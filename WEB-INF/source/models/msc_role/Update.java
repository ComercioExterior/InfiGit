package models.msc_role;

import megasoft.*;import models.msc_utilitys.*;

public class Update extends MSCModelExtend
{

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );

		//ensamblar SQL
		String sql = getResource("update.sql");
		sql = db.sql(sql, _record);

		//ejecutar query
		db.exec( _dso, sql);

	}

}
