package models.security;

import megasoft.*;import models.msc_utilitys.*;

/**
 * Model para el formulario de cambio de password
 */
public class NewPassword extends MSCModelExtend 
{
	/**
	 * @see megasoft.AbstractModel#execute()
	 */
	public void execute() throws Exception 
	{

		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );

		_record.setValue("userid", getUserName() );
		_record.setValue("password_date", Util.getDate() );
		_record.setValue("password", Util.getPasswordHash( _record.getValue("userid"), _record.getValue("password") ) );
		
		//ensamblar SQL
		String sql = getResource("update.sql");
		sql = db.sql(sql, _record);

		//ejecutar query
		db.exec( _dso, sql);		

	}

	public boolean isValid() throws Exception
	{
		
		boolean flag = super.isValid();
		
		if (flag)
		{
			if (!_record.getValue("password").equals(_record.getValue("confirm")))
			{
				_record.addError("Repita contraseña","La contraseña y su repetici&oacute;n no son id&eacute;nticas.");
				flag = false;
			}
		}
				
		return flag;
		
	}
	
}
