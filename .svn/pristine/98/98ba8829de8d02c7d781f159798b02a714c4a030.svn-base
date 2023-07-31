package models.menu_items;

import megasoft.*;import models.msc_utilitys.*;

public class Addnew extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;
	private DataSet _applics = null;
	

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		String sql = "";
		//ensamblar SQL
		sql = getResource("getnewdata.sql");
		if (_req.getParameter("id")!=null )
		sql = Util.replace( sql, "@id@", _req.getParameter("id"));
		else
		sql = Util.replace( sql, "@id@", "0");
		
		//crear dataset
		
		_table = db.get( _dso, sql );
		if (_table.count()!=0)
		{

			_table.first();
			_table.next();
		
			int level = Integer.parseInt(_table.getValue("nu_level")) + 1;
			_table.setValue("prox_level", String.valueOf(level));
			
			_table.setValue("st_nombre", "Submen&uacute; de "+ _table.getValue("st_nombre"));
		}
		else
		{

			 sql = getResource("getblankdata.sql");
			_table = db.get( _dso, sql );
						
			
			if (_table.count()==0)
			{
				_table.addNew();

				_table.setValue("id", "0");
				_table.setValue("nu_level", "1");
				_table.setValue("st_nombre","OPCI&Oacute;N PRINCIPAL");
				_table.setValue("prox_level", "1");
				
			}

		}
		
		sql = getResource("get_aplicaciones.sql");
		
		_applics = db.get(_dso, sql);

		//registrar los datasets exportados por este modelo
		storeDataSet( "table", _table );
		storeDataSet( "applics", _applics );
				
	}

}
