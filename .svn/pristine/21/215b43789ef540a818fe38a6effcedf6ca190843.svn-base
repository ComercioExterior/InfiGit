package models.security.userInfo;

import javax.sql.DataSource;

import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.AbstractUserInfo;
import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;

import electric.xml.Document;
import electric.xml.Element;

/**
 * <b>Foundation2: </b> 
 * Plugin para obtener la informacion del usuario
 * a partir de las tablas de seguridad estandar del framework
 * Foundation-2 RELEASE 6 (proveedor de seguridad SQL). Para mayor informacion
 * ver el documento parte-7.doc que describe el diccionario de tablas
 * basicas usadas por el framework
 * @author Gabriell Calatrava
 */
public class SecurityDBUserInfo extends AbstractUserInfo
{

	private DataSource _dso = null;//datasource para tablas de seguridad
	//private DataSource _dsoPrincipalInfi = null;//datasource para tablas de base de datos infi
	
	public SecurityDBUserInfo(DataSource _dso){
		this._dso = _dso;
	}
	
	public SecurityDBUserInfo(){		
	}


	/* (non-Javadoc)
	 * @see megasoft.AbstractUserInfo#getDisplayName()
	 */
	public String getDisplayName(String userid) throws Exception
	{

		//define sql for retrieving username
		String sql = "select fullname as nombre_completo from msc_user where userid='%1'";
        
		//create sql
		sql = Util.replace(sql,"%1", userid );
    
		//execute query to retrieve user info
		DataSet ds = db.get(_dso, sql);
		if (ds.count()==0)
		{
			throw new Exception("El usuario no esta registrado: " + userid);
		}
		else
		{
			ds.next();
			return ds.getValue("nombre_completo");
		}    

	}

	/* (non-Javadoc)
	 * @see megasoft.AbstractUserInfo#getEmail()
	 */
	public String getEmail(String userid) throws Exception
	{

		//define sql for retrieving username
		String sql = "select email from msc_user where userid='%1'";
        
		//create sql
		sql = Util.replace(sql,"%1", userid );
    
		//execute query to retrieve user info
		DataSet ds = db.get(_dso, sql);
		if (ds.count()==0)
		{
			throw new Exception("El usuario no esta registrado: " + userid);
		}
		else
		{
			ds.next();
			return ds.getValue("email");
		}    

	}
	
	/**
	 * Obtiene el Id del Blotter asociado al usuario conectado.
	 * @param userid
	 * @return Id del Blotter
	 * @throws Exception
	 */
	public String getUserBlotterId(String userid) throws Exception{
		
		String blotterId ="";		
		UsuarioDAO usuarioDao = new UsuarioDAO(_dso);
		blotterId = usuarioDao.listarUsuarioBlotter(userid);
						
		if (blotterId.equals("0")){
			throw new Exception("El usuario " + userid + " no posee un blotter asociado." );
		}
		
		return blotterId;
		
	}
	
	
	/**
	 * Obtiene el Nombre del Blotter asociado al usuario conectado.
	 * @param userid
	 * @return Id del Blotter
	 * @throws Exception
	 */
	public String getUserBlotterName(String userid) throws Exception{
		String blotterName ="";
	
		StringBuffer sql = new StringBuffer(); 
			
		sql.append("select b.bloter_descripcion from infi_tb_104_bloter_usuarios bu, infi_tb_102_bloter b where bu.bloter_id = b.bloter_id and  bu.userid = '").append(userid).append("'");
		
		DataSet ds = db.get(_dso, sql.toString());
		
		if (ds.count()==0)
		{
			throw new Exception("El usuario " + userid + " no posee un blotter asociado." );
		}
		else
		{
			ds.next();
			blotterName = ds.getValue("bloter_descripcion");
		}    

		
		return blotterName;
		
	}
	

	/* (non-Javadoc)
	 * @see megasoft.AbstractUserInfo#init(java.lang.String)
	 */
	public void init(String xmlConfig) throws Exception
	{
		super.init(xmlConfig);
		
		try 
		{
			//leer la configuracion
			Document doc = new Document(xmlConfig);		
			Element root = doc.getRoot();
			String dsName = root.getElement(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA).getTextString();			
			
			//cachear el DataSource
			_dso = db.getDataSource(dsName);//datasorce tablas de seguridad
						
			
		}
		catch (Exception e)
		{
			throw new Exception("Error en la configuración del plugin DBUserInfo: " + e.getMessage());
		}
		
	}

}
