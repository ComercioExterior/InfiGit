package models.security.login;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.AbstractLogin;
import megasoft.ActionUser;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;
import electric.xml.Document;
import electric.xml.Element;

/**
 * Clase Plug-in para implementar los m&eacute;todos del AbstractLogin. Se utilizar&aacute; esta clase 
 * para validar los usuarios que intenten logearse en la aplicaci&oacute;n.
 * @author Erika Valerio,elaucho Megasoft Computaci&oacute;n
 */

public class SecurityDB extends AbstractLogin
{
 

	private DataSource _dso = null;
	/**
	 * HasMap que contendra las propiedades del archivo .ini ubicado en la maquina del cliente y leido con ActiveX
	 */
	protected HashMap<String, String> parametros = new HashMap<String, String>(); 
	/* (non-Javadoc)
	 * @see megasoft.AbstractLogin#login(java.lang.String, java.lang.String)
	 */
	public ActionUser login(String userid, String password, HttpServletRequest req) throws Exception
	{
		
		DataSet ds = null;
		String sql = "";
		
		/*
		 * Datos obtenidos del archivo ini ubicado en la maquina del cliente
		 */
		String propiedadesArchivoIni = (req.getParameter("iniFile")!=null && !req.getParameter("iniFile").equals(""))?req.getParameter("iniFile").substring(0, req.getParameter("iniFile").length()-1):"";
		
		
		if(propiedadesArchivoIni!=null && !propiedadesArchivoIni.equals("")){
			/*
			 * Se guardan las propiedades con su valor en un arreglo
			 */
			String propiedadesArchivoIniArray[] = Util.split(propiedadesArchivoIni,";");
			/*
			 * Recorremos las propiedades del archivo que viene en un String para setearlas a un hashMap
			 */
			for(int i=0;i<propiedadesArchivoIniArray.length;i++){
				if(!propiedadesArchivoIniArray[i].equals("[Default]")){
					/*
					 * Separamos por Clave/Valor
					 */
					String parametrosArray[] = Util.split(propiedadesArchivoIniArray[i],"=");
					String clave			 = parametrosArray[0];
					String valor			 ="";
					if(parametrosArray.length==2)//Se valida que tenga un valor
					valor = (parametrosArray[1]==null || parametrosArray[1].equals(""))?"":parametrosArray[1];
					/*
					 * Se setean las clave/valor al hashMap
					 */
					parametros.put(clave, valor);
				}
			}
		/*
		 * Se monta en sesion el HashMap con todos los parametros leidos
		 */
			req.getSession().setAttribute("archivo.ini.local", parametros);
		}//fin if
		//---sustituir caracteres inapropiados en el usuario---
		
		userid = filtro(userid);		
		//--enviar nombre de usuario en mayusculas
		userid = userid.toUpperCase();

		password = Util.getPasswordHash(userid, password);

		//String sql	= "select u.userid, u.user_status, u.msc_user_id, r.rolename from msc_user u, msc_role r where u.msc_role_id = r.msc_role_id and u.userid='@user_id@' and u.user_password='@password@'";
				
        //create sql
       //sql = Util.replace(sql,"@user_id@", userid );
       //sql = Util.replace(sql,"@password@", password );

		sql = "select r.rolename, u.user_status "
			+ "from msc_user u, msc_role r,MSC_APPLICATIONS a,MSC_ROLE_USER ru "
			+ "where u.userid='"
			+ userid
			+ "' "
			+ "and r.id_application=a.ID_APPLICATION and a.SIGLAS_APPLIC='" +AppProperties.getProperty("app-name")+ "' "
			+ "and ru.MSC_ROLE_ID=r.MSC_ROLE_ID and ru.MSC_USER_ID=u.MSC_USER_ID";
					
		
			ds = db.get(_dso, sql);
		
		if (ds.count() == 0)
		{
			return null;
		}
		else
		{		
			ds.next();
			
			if(ds.getValue("user_status")!=null & ds.getValue("user_status").equals("0")){

				return new ActionUser();//INACTIVO: usuario sin acceso (nulo)--Sera rechazado por el metodo isAuthorized de la clase SecurityActionAccesApp	
				
			}else{	

				return new ActionUser( userid, ds.getValue("rolename") );
			}
		}
	}	
		
    /** Filtra caracteres utilizados en SQL Poisoning y Scripting
     * @see megasoft.AbstractLogin#init(java.lang.String)
     */
     public static String filtro(String userid) throws Exception {

     if (userid!=null){
    	userid = userid.replaceAll("'", "''");
    	userid = userid.replaceAll("<", "&lt;");
    	userid = userid.replaceAll(">", "&gt;");    	 
 		
		userid = Util.replace(userid, "\"", " ");
		userid = Util.replace(userid, ";", " ");
		//userid = Util.replace(userid, "(", " ");
		//userid = Util.replace(userid, ")", " ");
		userid = Util.replace(userid, "--", " ");
		userid = Util.replace(userid, "=", " ");
		userid = Util.replace(userid, "<", " ");
		userid = Util.replace(userid, ">", " ");
		userid = Util.replace(userid, "<", " ");
		userid = Util.replace(userid, "%", " ");

     }

     return userid;
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
			String dsName = root.getElement(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA).getTextString();

		
			//cachear el DataSource
			_dso = db.getDataSource(dsName); 
		}
		catch (Exception e)
		{
			throw new Exception("Error en la configuraci&oacute;n del plugin DBLogin: " + e.getMessage());
		}
		
	}
	
}
