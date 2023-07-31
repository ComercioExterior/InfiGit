package models.msc_utilitys;

import electric.xml.*;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import megasoft.*;
/**
 * Plugin para implementar el Login contra
 * una base de datos SQL usando la estructura
 * de tabla estandar del framework (portal_user)
 * @author Martin Cordova
 */
public class DBLoginOracle extends AbstractLogin
{

	private DataSource _dso = null;


	/* (non-Javadoc)
	 * @see megasoft.AbstractLogin#login(java.lang.String, java.lang.String)
	 */
	public ActionUser login(String userid, String password, HttpServletRequest req) throws Exception
	{

		password = Util.getPasswordHash(userid, password);

		String sql	= "select userid, rolename from portal_user u, portal_role r "
					+ "where u.role_id = r.id and userid = '@userid@' "
					+ "and password='@password@'";

		sql = Util.replace(sql, "@userid@", userid);
		sql = Util.replace(sql, "@password@", password);

		DataSet ds = db.get(_dso, sql);
		if (ds.count() > 0)
		{
			ds.next();
			return new ActionUser( userid, ds.getValue("rolename"));
		}
		else
		{
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see megasoft.AbstractLogin#init(java.lang.String)
	 */
	public void init(String xmlConfig) throws Exception
	{
		super.init(xmlConfig);
		
		try 
		{
			//leer la configuracion
			Document doc = new Document(xmlConfig);		
			Element root = doc.getRoot();
			String dsName = root.getElement("datasource").getTextString();
		
			//cachear el DataSource
			_dso = db.getDataSource(dsName);
		}
		catch (Exception e)
		{
			throw new Exception("Error en la configuraci&oacute;n del plugin DBLogin: " + e.getMessage());
		}
		
	}
	
}
