package models.msc_user;

import megasoft.*;import models.msc_utilitys.*;

public class Edit extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _table = null;
	private DataSet _roles = null;
	//private DataSet _sucursal = null;
	//private DataSet _almacen = null;
	//private DataSet _departamento = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
		
		//ensamblar SQL
		String sql = getResource("getrecord.sql");
		
		if ( _req.getParameter("id")!= null )
		{
			sql = Util.replace( sql, "@id@", _req.getParameter("id") );
		
			_record.setValue("msc_user_id", _req.getParameter("id"));
		}
		else
		{
			sql = Util.replace( sql, "@id@", _req.getParameter("msc_user_id") );
		
			_record.setValue("msc_user_id", _req.getParameter("msc_user_id"));
		}

		String sql1 = getResource("get-roles.sql");

		//crear dataset
		_table = db.get( _dso, sql );
		_roles = db.get( _dso, sql1 );
		
		//ensamblar SQL para combo de sucursal
		//sql = getResource("getsucursal.sql");
		//crear dataset para llenar combo de sucursal
		//_sucursal = db.get( _dso, sql );
		
		//String filter = "";
		
		//ensamblar SQL para combo de almacen
		/*sql = getResource("getalmacen.sql");
		
		if ( _req.getParameter("indicador") != null )
		{
			if ( _req.getParameter("indicador").equals("1") )
			{
				_record.setValue("id_sucursal", _req.getParameter("id_sucursal"));				
				filter = filter + " And al.id_sucursal=" + _req.getParameter("id_sucursal");
			}
		}
		else
		{
			_table.first();
			_table.next();
			
			_record.setValue("id_sucursal", _table.getValue("id_sucursal"));
			filter = filter + " And al.id_sucursal=" + _table.getValue("id_sucursal");
		}*/
		
		//sql = Util.replace(sql, "@filter@", filter);
		
		//crear dataset para llenar combo de almacen
		//_almacen = db.get( _dso, sql );
		
		//ensamblar SQL para combo de departamento
		//filter = "";
		
		/*sql = getResource("getdepartamento.sql");
		
		if ( _req.getParameter("indicador") != null )
		{
			if ( _req.getParameter("indicador").equals("1") )
			{
				_record.setValue("id_sucursal", _req.getParameter("id_sucursal"));				
				filter = filter + " And dep.id_sucursal=" + _req.getParameter("id_sucursal");
			}
		}
		else
		{
			_table.first();
			_table.next();
			
			_record.setValue("id_sucursal", _table.getValue("id_sucursal"));
			filter = filter + " And dep.id_sucursal=" + _table.getValue("id_sucursal");
		}*/
		
		//sql = Util.replace(sql, "@filter@", filter);
		

		
		//crear dataset para llenar combo de almacen
		//_departamento = db.get( _dso, sql );

		//registrar los datasets exportados por este modelo
		storeDataSet( "table", _table );
		storeDataSet( "roles", _roles );
		//storeDataSet( "sucursal", _sucursal );
		//storeDataSet( "almacen", _almacen );
		//storeDataSet( "departamento", _departamento );
		storeDataSet( "record", _record );
	}
}