package models.aplicacion;

import megasoft.*;import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		_dso = db.getDataSource( _app.getInitParameter("datasource") );

		//ensamblar SQL
		String sql = getResource("browse.sql");

		//crear dataset
		_table = db.get( _dso, sql);

		//registrar los datasets exportados por este modelo
		storeDataSet("table", _table);
		
	}

}
