package models.msc_utilitys;
import megasoft.*;
import javax.sql.*;
/**
* Class que extiende de megasoft.GenericView, con el fin de nuevas remplazos en los template
*/
public class ClassView extends GenericView
{
	/**
	* Class para ejecutar la clase superior y agregar nuevas actividades 
	*/
	public void execute()  throws Exception
	{

	_page.setContentType( _config.contentType );
	//Obtener el dir de los distintos componentes para remplazarlos en el template
	DataSet dsComponentes = getUrlComponentes();
        
	while (dsComponentes.next()) {
		_page.replace ("@"+dsComponentes.getValue("cod_parametro") +"@",dsComponentes.getValue("cod_valor")); 
	}
	
	//ejecutar metodo execute de GenericView
	super.execute();
	}

	/**
	* Obtener el Directorio de Ubicaci&oacute;n para los distintos componentes
	*/
	public DataSet getUrlComponentes()throws Exception
	{
	DataSet ds = null;
	//(Implementar busqueda de parametros)

	//Obtener Parametros de la Aplciaci&oacute;n
	String sql_ud = getResource("busca_param.sql");
	//	Obtener DataSource
	DataSource _dso = db.getDataSource(_app.getInitParameter("datasource"));
	ds = db.get(_dso,sql_ud);
	return ds;
	}
}