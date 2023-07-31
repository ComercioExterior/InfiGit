package models.msc_user;

import megasoft.*;import models.msc_utilitys.*;

public class AddNew extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _roles = null;
	//private DataSet _sucursal = null;
	//private DataSet _almacen = null;
	//private DataSet _departamento = null;	

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		//String sql="";
		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
		
		String sql1 = getResource("get-roles.sql");

		//crear dataset
		_roles = db.get( _dso, sql1 );
		
		//ensamblar SQL para combo de sucursal
		//String sql = getResource("getsucursal.sql");
		
		//crear dataset para llenar combo de sucursal
		//_sucursal = db.get( _dso, sql );
		
		//String filter = "";
		
		//ensamblar SQL para combo de almac&eacute;n
		/*sql = getResource("getalmacen.sql");
		

		
		if ( _req.getParameter("indicador") != null )
		{
			if ( _req.getParameter("indicador").equals("1") )
			{
				_record.setValue("id_sucursal", _req.getParameter("id_sucursal"));
				
			
					
				filter = filter + " And al.id_sucursal=" + _req.getParameter("id_sucursal");
			}
		}
		
		sql = Util.replace(sql, "@filter@", filter);
		
		//crear dataset para llenar combo de almac&eacute;n
		_almacen = db.get( _dso, sql );*/
		
		//ensamblar SQL para combo de departamento
		/*filter = "";
		
		sql = getResource("getdepartamento.sql");
		
		if ( _req.getParameter("indicador") != null )
		{
			if ( _req.getParameter("indicador").equals("1") )
			{
				_record.setValue("id_sucursal", _req.getParameter("id_sucursal"));
					
				filter = filter + " And dep.id_sucursal=" + _req.getParameter("id_sucursal");
			}
		}
		
		sql = Util.replace(sql, "@filter@", filter);
		
		//crear dataset para llenar combo de almac&eacute;n
		_departamento = db.get( _dso, sql );*/

		//registrar los datasets exportados por este modelo
		storeDataSet( "roles", _roles );
		//storeDataSet( "sucursal", _sucursal );
		//storeDataSet( "almacen", _almacen );
		//storeDataSet( "departamento", _departamento );
		storeDataSet( "record", _record );
	}
}