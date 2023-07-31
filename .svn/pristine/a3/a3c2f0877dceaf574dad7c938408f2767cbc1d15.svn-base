package models.aplicacion;

import megasoft.*;
import models.msc_utilitys.*;

public class Update extends MSCModelExtend
{

	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
				
		//ensamblar SQL
		String sql= getResource("update.sql");
			
		sql = Util.replace( sql, "@id_application@", _record.getValue("id_application"));
		
		sql= db.sql(sql, _record);
		//ejecutar query
		db.exec( _dso, sql);
		
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
	
		if (flag)
		{
			DataSet _temp = null;
			String sql = getResource("valida_siglas_applic.sql");	
			sql= db.sql(sql, _record);
			_temp = db.get(_dso, sql);
				
			if(_temp.next()){
				_record.addError("Seguridad/Aplicaciones", "Ya existe una aplicaci&oacute;n registrada con las siglas "+ _record.getValue("siglas_applic")+ ". Verifique.");
				flag = false;
			
			}			
			
			sql = getResource("valida_nombre_applic.sql");	
			sql= db.sql(sql, _record);
			_temp = db.get(_dso, sql);
				
			if(_temp.next()){
				_record.addError("Seguridad/Aplicaciones", "Ya existe una aplicaci&oacute;n registrada con el nombre "+ _record.getValue("description")+ ". Verifique.");
				flag = false;
			
			}
		
		}
		return flag;	
	}



}
