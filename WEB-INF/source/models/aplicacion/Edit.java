package models.aplicacion;

import megasoft.*;import models.msc_utilitys.*;

public class Edit extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;	

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
				
		//ensamblar SQL
		String sql = getResource("getrecord.sql");
		sql = Util.replace( sql, "@id@", _req.getParameter("id") );

		//crear dataset
		_table = db.get( _dso, sql );

		//registrar los datasets exportados por este modelo
		storeDataSet( "table", _table );
		
	}

}
