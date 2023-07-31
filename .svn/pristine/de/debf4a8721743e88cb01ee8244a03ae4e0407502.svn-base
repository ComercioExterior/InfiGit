package models.security.access;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.AbstractAccessMap;
import megasoft.ActionUser;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;

import electric.xml.Document;
import electric.xml.Element;
import electric.xml.XPath;
import electric.xml.ParseException;

 

/**
 * <b>Foundation2:</b> 
 * Esta clase encapsula la lectura del elemento solicitado
 * dentro del archivo action-map.xml
 *
 * @author Gabriell Calatrava
 * 
 */
public class SecurityActionAccessApp extends AbstractAccessMap
{

    //Performance: Cache ActionMap.xml
	static Element rootActionMap    = null;
	private DataSource _dso = null;
	
	
	/**
	 * Inicializa los atributos necesario para definir el constraint y acceso al action.
	 * @param req
	 * @return void
	 * @throws Exception
	 */
	public void init( HttpServletRequest req , ServletContext app ) throws Exception
	{

		debug("Action Map Init");
		
		this.req         = req;
		this.app         = app;
		
	    //Performance: Action Map Cache
	    if (rootActionMap==null || !isCachedActionMap() )
	    {
	    
		  //cargar configuracion XML
		  String xml = getResource("/WEB-INF/action-map.xml");
	
		  //ubicar configuracion de la lista solicitada
		  rootActionMap = new Document(xml).getRoot();
	    }

		
		this.actionID    = getActionID( req );
	}
	
	
	/**
	 * Retorna true si el contenido del ActionMap debe permanecer en cache. 
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isCachedActionMap() throws Exception
	{
			// Propiedad Global de la fundaci&oacute;n
			String cached = AppProperties.getProperty("cache-action-map");
			return cached!=null && cached.trim().length()>0 && cached.equals("true");
				
	}

	/**
	 * Retorna un ActionMap con la informaci&oacute;n del action
	 * @param req
	 * @return void
	 * @throws Exception
	 */
	public megasoft.ActionMap getActionInfo() throws Exception
	{
		megasoft.ActionMap a = new megasoft.ActionMap();
		a.constraint         = getConstraint();
		a.configDir          = getConfigDir();
		a.description        = getActionAttribute().getAttribute("description");
		a.debug              = getDebug();
		a.includeRequestBody = getIncludeRequestBody();
				
		return a;
	}
	
	
	/**
	 * Obtiene el constraint del action.
	 * @return Contraint Value
	 * @throws Exception
	 */
	public String getConstraint() throws Exception
	{		 
		
		/*
		 * La seguridad se esta controlando en la BD, por lo que hay que obtener
		 * un datasource para hacer los queries necesarios.
		 */
		/*DataSource datasource = getDataSource();

		DataSet ds = db.get(datasource, "select 1 from msc_actions where url='"
				+ this.actionID + "' and secured='N'");

		if (ds.count() == 0) {
			return "protected";
		} else {
			return null;
		}*/
		
		return getActionAttribute().getAttribute("constraint");

	}

	/**
	 * Indica si un usuario esta autorizado dado su rol
	 * y el contraint del action a ser ejecutado
	 * @param userName
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isAuthorized(Document secConfig, ActionUser user, megasoft.ActionMap action) throws Exception
	{		
		//return true;
		
		DataSet ds  = null;
		
		String sql = "select 1 "
			+ "from msc_role r, msc_roles_actions ra, msc_actions a, msc_applications ap "
			+ "where r.rolename='"
			+ user.getRoleName()
			+ "' "
			+ "and r.msc_role_id=ra.msc_role_id and ra.id_action = a.id_action "
			+ "and a.url='" + this.actionID + "' "
			+ "and a.id_application=ap.id_application " +
					"and ap.siglas_applic='" + AppProperties.getProperty("app-name") + "'";
				
			ds = db.get(getDataSource(), sql);
		
			if (ds.count() == 0) {
				return false;
			} else {
				return true;
			}
			
	}

	/**
	 * Determina si se debe enviar un hearbeat al cliente con el fin de evitar TimeOut en la conexi&oacute;n
	 * @return Include Request's Body Value
	 * @throws Exception
	 */
	public boolean getKeepAlive() throws Exception
	{
		String value = getActionAttribute().getAttribute("keep-alive");
		
		return value!=null && value.equals("true");

	}

	/**
	 * Determina si se debe incluir el Cuerpo del Request en el Model (web services).
	 * @return Include Request's Body Value
	 * @throws Exception
	 */
	public boolean getIncludeRequestBody() throws Exception
	{
		String value = getActionAttribute().getAttribute("include-request-body");
		
		return value!=null && value.equals("true");

	}

	/**
	 * Determina si el Debug estar&aacute; activo para este Action. 
	 * Si el action tiene el atributo debug='false' no se incluir&aacute; la informaci&oacute;n de Debug en l&iacute;nea en la respuesta.
	 * Si el action no tiene el atributo 'debug'  o lo tiene en true ( debug='true' ) si se incluir&aacute; la informaci&oacute;n de Debug en l&iacute;nea en la respuesta
	 * @return Debug Value
	 * @throws Exception
	 */
	public boolean getDebug() throws Exception
	{
		String value = getActionAttribute().getAttribute("debug");

		return (value==null) || ( (value!=null) && value.equals("true") );

	}


	/**
	 * Obtiene el getConfigDir del action.
	 * @return getConfigDir Value
	 * @throws Exception
	 */
	public String getConfigDir() throws Exception
	{
		
		//ejecutar query
		String configDir = getActionAttribute().getAttributeValue("config-dir");

		if (configDir==null)
		{
			throw new Exception("El elemento (" + actionID + ") no contiene el atributo 'config-dir'. Verifique el archivo /WEB-INF/action-map.xml");
		}

		Element configPathRemove = getElement( "/actions/config-path-remove" );

        //Remueve prefijo especificado en el action-map.xml		
		if (configPathRemove!=null)
		{
		configDir = Util.replace(configDir,configPathRemove.getText()+"","");
		}
	

		return configDir;
	}

	/**
	 * Obtiene la configuraci&oacute;n del action solicitado en el archivo action-map.xml
	 * @return Element con la configuraci&oacute;n solicitada
	 * @throws Exception
	 */
    public Element getActionAttribute() throws Exception
    {

		Element action = rootActionMap.getElement( new XPath("action[@id='" + actionID + "']") );


		if (action==null)
		{
			throw new Exception("El archivo action-map.xml no contiene la configuracion solicitada: " + actionID);
		}

		return action;

	}


    /**
     * Obtiene un elemento del archivo action-map.xml dado un path que contiene el id del tag requerido
     * @param xpath
     * @return Element solicitado
     * @throws Exception
     */
    public Element getElement( String xpath ) throws Exception
    {
    	
		Element element = rootActionMap.getElement( new XPath(xpath) );
		
		return element;

	}
    
	/**
	 * Obtiene el datasource de seguridad que se esta utilizando en la
	 * aplicacion y lo guarda en una propiedad de la clase para que quede como
	 * un cache.
	 * 
	 * @return el DataSet de seguridad de la aplicacion
	 * @throws ParseException
	 *             Si los archivos de configuracion security.xml y
	 *             login_config.xml estan mal formateados
	 * @throws Exception
	 *             Si algo falla
	 */
	protected DataSource getDataSource() throws ParseException, Exception {
		/*
		 * Esta estructura de if y synchronized esta pensada para que un solo
		 * thread carge la configuracion, los demas utilizaran el atributo _dso
		 * ya cargado
		 */
		if (_dso == null) {
			synchronized ("getDataSource") {
				if (_dso == null) {
					// Carga Configuraci�n de seguridad MVC
					Document securityXML = new Document(
							getResource("/WEB-INF/security.xml"));
					// leer configuracion del plugin de UserInfo
					Element root = securityXML.getRoot();
					String xmlConfig = root.getElement("login-config")
							.getTextString();
					/*
					 * ahora carga el archivo login_config.xml, desde donde saca
					 * el datasource que se esta utilizando para seguridad
					 */
					securityXML = new Document(getResource(xmlConfig));
					root = securityXML.getRoot();
					String dsName = root.getElement(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA)
							.getTextString();
					// cachear el DataSource
					_dso = db.getDataSource(dsName);
				}
			}
		}

		return _dso;
	}

 
}