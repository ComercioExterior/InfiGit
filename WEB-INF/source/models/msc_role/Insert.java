package models.msc_role;

import megasoft.*;import models.msc_utilitys.*;

public class Insert extends MSCModelExtend
{

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
		
		//generar unique ID
		//_record.setValue("id", dbGetSequence(_dso, "object") );
		_record.setValue("msc_role_id", dbGetSequence(_dso, "MSC_ROLE") );


		//ensamblar SQL
		String sql = getResource("insert.sql");
		sql = db.sql(sql, _record);

		//ejecutar query
		db.exec( _dso, sql);

	}

}
