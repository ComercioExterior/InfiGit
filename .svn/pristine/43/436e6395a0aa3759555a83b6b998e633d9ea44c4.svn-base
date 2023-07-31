package models.msc_role;

import megasoft.*;
import models.msc_utilitys.*;
//import models.valid.*;

public class ConsultaUsuarios extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;
	private DataSet _datos = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		//ensamblar SQL
		String sql = getResource("usuarios_role.sql");
		sql = Util.replace(sql, "@id@", _req.getParameter("msc_role_id"));

		//crear dataset
		_table = db.get( _dso, sql);		
	
		sql = getResource("get_datos_role.sql");
		sql = Util.replace(sql, "@id@", _req.getParameter("msc_role_id"));
		
		_datos = db.get( _dso, sql);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", _table);
		storeDataSet("datos", _datos);
		
	}
}