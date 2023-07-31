package models.msc_user;

import megasoft.*;
import models.msc_utilitys.*;
//import models.valid.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{

		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
		
		//System.out.print("_req.getRemoteUser() en Table.java tiene: " + _req.getRemoteUser());
		//ensamblar SQL
		String sql = getResource("browse.sql");

		//crear dataset
		_table = db.get( _dso, sql);
		
		//ValidaUsuario record_val = new ValidaUsuario();
		
		//String codbanca_usuario = record_val.getIdUsuario(_req, _dso);


		//registrar los datasets exportados por este modelo
		storeDataSet("table", _table);
		
	}
}