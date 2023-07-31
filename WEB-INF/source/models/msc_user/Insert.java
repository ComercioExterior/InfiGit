package models.msc_user;

import megasoft.*;import models.msc_utilitys.*;

public class Insert extends MSCModelExtend
{

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{


		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
		
		_record.setValue("user_password", Util.getPasswordHash( _record.getValue("userid"), _record.getValue("user_password") ) );
		
		//generar unique ID		
		_record.setValue("msc_user_id", dbGetSequence(_dso, "MSC_USER") );

		//ensamblar SQL
		String sql = getResource("insert.sql");
		sql = db.sql(sql, _record);

		//ejecutar query
		db.exec( _dso, sql);

	}

	public boolean isValid() throws Exception
	{
		
		boolean flag = super.isValid();
		
		if (flag)
		{
			if (!_record.getValue("user_password").equals(_record.getValue("confirm")))
			{
				_record.addError("Repita contraseña","La contraseña y su repetici&oacute;n no son id&eacute;nticas.");
				flag = false;
			}
		}
		return flag;
	}	
}