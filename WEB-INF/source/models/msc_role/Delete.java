package models.msc_role;

import megasoft.*;import models.msc_utilitys.*;

public class Delete extends MSCModelExtend
{

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{

		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );

		//ensamblar SQL
		String sql = getResource("delete.sql");
		sql = Util.replace( sql, "@id@",  _req.getParameter("id") );

		//ejecutar query
		db.exec( _dso, sql );

	}

}
