package models.security.login;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import megasoft.AbstractLogin;
import megasoft.ActionUser;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;

import org.apache.log4j.Logger;
import org.bcv.serviceDICOM.OperCamFileTransferPortBindingStub;

import com.bdv.infi.dao.OficinaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.LoginRACF;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.client.ClienteWs;
import com.bdv.infi.webservices.manager.ManejadorLoginCP;

import electric.xml.Document;
import electric.xml.Element;

/**
 * Plugin para implementar el Login utilizando la transaccion QG30 de AEA.
 * Implementa el plugin de login de La Fundacion.
 */
public class Login extends AbstractLogin{
	
	/*** Logger APACHE*/
	private Logger logger = Logger.getLogger(Login.class);
	
	private static final String ERROR_DATOS_OBLIGATORIOS = "Debe ingresar usuario y contrase&ntilde;a";

	
	/**
	 * Es el URL del web service de login. Ejemplo:
	 * http://180.183.194.41:34567/axis2-ws8/services/ValidacionUsuarioEnLinea
	 */
	private String endpoint = null;

	/**
	 * Esquema o xsd que se coloca para todos los WS. Se puede ver en el wsdl
	 * del WS. Seguramente es siempre:
	 * http://www.bancovenezuela.com/ws/ValidacionUsuarioEnLinea/xsd
	 */
	private String schema = null;

	/**
	 * La operacion de login para el WS. Es la operacion que se llamara dentro
	 * del WS. Siempre sera: getUsuarioEnLinea, al menos que a alguien le de por
	 * ponerse a cambiar eso en el WS.
	 */
	private String operation = null;

	/**
	 * Este contexto se utiliza para pasarlo al ClienteWs, el cual lo necesita
	 * para cargar los archivos de configuracion de axis como se explica en la
	 * clase LoginContext.
	 * 
	 * @see LoginContext
	 */
	private LoginContext context = null;

	/**
	 * Este es el datasource que se utilizará para traer los roles que estan
	 * definidos en la base de datos de seguridad. Esto es porque hay que verificar
	 * todos los roles en que puede estar el usuario.
	 */
	private DataSource datasource = null;
	
	/**
	 * Este es el datasource que se utilizará para verificar en la tabla active sessions
	 */
	private DataSource datasourceInfi = null;
	
	
	/**Agencia leida del archivo de la maquina del cliente*/
	private String agenciaDeArchivo = null;
	

	/**
	 * Llama a la transaccion QG30 de aea. Si no hace login, retorna null. Si
	 * logra el login, retorna el usuario con su rol. El rol se define por el
	 * grupo que tiene el usuario en racf
	 * 
	 * En la transaccion QG30 se envia el grupo de RACKF al que se esta
	 * realizando el login. Esto implica que la aplicacion debe tener el grupo
	 * del usuario, porque eso no se le pregunta al usuario. Ahora, puede que la
	 * aplicacion tenga varios grupos, siendo asi, hay que probar con cada
	 * grupo, a ver si el usuario se puede logear en alguno de ellos.
	 * 
	 * @see megasoft.AbstractLogin#login(java.lang.String, java.lang.String)
	 */
	public ActionUser login(String userid, String password, HttpServletRequest req) throws Exception {
		
		/**
		 * HasMap que contendra las propiedades del archivo .ini ubicado en la maquina del cliente y leido con ActiveX
		 */
		agenciaDeArchivo = null;
		
		/*try {
			OperCamFileTransferPortBindingStub stub = new OperCamFileTransferPortBindingStub(null,datasourceInfi);
			 byte[] _bytes;
			
			_bytes=stub.bajarSolicitudComprobacionBloqueo("0001_1_0102");//"0001"
			Utilitario.byteToFile(_bytes,"d:\\archivoDICOMBCV"+System.currentTimeMillis()+".txt");
			 
			//System.out.println("*******openFileToString**** "+Utilitario.openFileToString(_bytes));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("*******openFileToString**** "+e1.getMessage());
			e1.printStackTrace();
		}*/
		
		
		//Leyendo archivo de propiedades en la m�quina del cliente
		int band = 1;
		if(band==1){
			logger.info("EMPEZANDO A OBTENER DATOS...");
			obtenerDatos(req);
			logger.info("SE OBTUVIERON LOS DATOS...");
			band=0;
		}
		
		//Almacena en la variable de constantes la ruta de las imagenes
		if (ConstantesGenerales.RUTA_IMAGENES.equals("")){
			StringBuffer sb = new StringBuffer();
			String[] valores = req.getRequestURI().split("/");
			sb.append(req.getScheme()).append("://").append(req.getServerName()).append(":");
			sb.append(req.getServerPort()).append("/").append(valores[1]).append("/images/");
			ConstantesGenerales.RUTA_IMAGENES =  sb.toString(); 
		}
		
		//Boolean para saber si insertar o no en active session
		boolean activeSessions = true;
		
		//Verificamos en parametros de la aplicacion si se encuentra activado Validar Usuario sesion
		boolean enabled = Boolean.parseBoolean(ParametrosDAO.listarParametros(ParametrosSistema.SESION_ACTIVA, datasourceInfi));
		
		/*
		 * creo el cliente de WS y le paso los parametros necesarios para que
		 * pueda hacer su configuracion
		 */
		userid = userid.toUpperCase();
		//---sustituir caracteres inapropiados en el usuario---
		userid = filtro(userid);		
		
		ClienteWs clientews = new ClienteWs(); 
		clientews.setUrlDelWs(endpoint);
		clientews.setNamespace(schema);
		clientews.setOperacion(operation);
		String ip = req.getRemoteAddr();
		logger.info("IP "+ ip);
		
		//remover variables de sesion-------------------------------
		if(req.getSession().getAttribute("mensaje_error_login")!=null)
			req.getSession().removeAttribute("mensaje_error_login");
		if(req.getSession().getAttribute("error_login")!=null)
			req.getSession().removeAttribute("error_login");
		if(req.getSession().getAttribute("password_cambiado")!=null){
			req.getSession().removeAttribute("password_cambiado");
		}
		//------------------------------------------------------------
		
		
		
		/*
		 * Este contexto se crea al momento de inicializar, si quieres saber mas
		 * al respecto leete la clase interna LoginContext que esta mas abajo en
		 * este mismo archivo
		 */
		context = new LoginContext(req.getRealPath("/"));
		clientews.setContexto(context);
		clientews.setSecure("true");
		
		DataSet ds = null;
		Usuario usuario = null;
		/*
		 * crea un objeto con las credenciales para que el WS haga la
		 * autenticacion.
		 */
		ConsultaDeUsuario consulta = new ConsultaDeUsuario();
		consulta.credenciales = new CredencialesDeUsuario();
		consulta.credenciales.setNombreDeUsuario(userid);
		consulta.setUsuario(userid);
		consulta.setPassword(password.toUpperCase());
					
			/*
			 * para cada iteracion le coloca el nuevo rol para probar si logra
			 * hacer login en ese rol. Los demas datos quedan iguales para el
			 * objeto de consulta.
			 */
			consulta.setDireccion("        ");

			/*
			 * aqui hace la comunicacion con el WS. Si la comunicacion falla o
			 * el WS retorna alguna excepcion, no se hace nada para intentar con
			 * el siguiente rol
			 */
			try {	
				
				if(userid==null || userid.trim().equals("") || password==null || password.trim().equals("")){
					logger.error(ERROR_DATOS_OBLIGATORIOS);
					throw new Exception(ERROR_DATOS_OBLIGATORIOS); 
				}
				
				
				//Si es un cambio de Password
				if(req.getParameter("cambio_pass")!=null){
					
					logger.info("Cambiando password de usuario " +userid+ " en RACF...");					
					
						try {
							
							ManejadorLoginCP mLCP = new ManejadorLoginCP(context);
							
							//Crear bean de entrada
							QG30 qg30= new QG30(); 
							
							qg30.setNombreDeUsuario(userid);
							qg30.setPassword(password.toUpperCase());
							
							if(req.getParameter("j_password_nuevo")!=null && !req.getParameter("j_password_nuevo").trim().equals("") && req.getParameter("j_password_rep")!=null && !req.getParameter("j_password_rep").trim().equals("")){
								qg30.setNuevaPassword(req.getParameter("j_password_nuevo").toUpperCase());
								qg30.setVerifNuevaPassword(req.getParameter("j_password_rep").toUpperCase());
							}else{
								logger.error("Datos para la nueva contrase�a son nulos...");
								throw new Exception("Los datos para la nueva contrase&ntilde;a son obligatorios");
							}
									
							logger.info("LLamando al servicio QG30 para el cambio de contrase�a...");
							// crear bean de salida en cambio de password
							QG30Respuesta qg30Respuesta = new QG30Respuesta();
							qg30Respuesta = mLCP.getCambioPassword(qg30, ip);					
												
							//Password cambiado exitosamente si el bean de respuesta trae alguno de sus atributos diferente de null
							if(qg30Respuesta.getQGRCMIS()!=null
								|| qg30Respuesta.getQGRMCOD()!=null
								|| qg30Respuesta.getQGRMCOF()!=null 
								|| qg30Respuesta.getQGRMINIS()!=null 
								|| qg30Respuesta.getYPLESS3()!=null){
								
								req.getSession().setAttribute("password_cambiado", "Su contrase&ntilde;a ha sido cambiada exitosamente");
								req.getSession().setAttribute("error_login", "0");//para mostrar el formulario para login
								req.getSession().setAttribute("mensaje_error_login", "");
								logger.info("Contrase�a de usuario cambiada exitosamente...");						
							}
						
						} catch (Exception e) {							
							logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
							logger.error("Fallo al cambiar la contrase&ntilde;a de usuario... " +e.getMessage());
							req.getSession().setAttribute("password_cambiado", "");
							req.getSession().setAttribute("error_login", "2");//para mostrar el formulario para login
							req.getSession().setAttribute("mensaje_error_login", "Error ocurrido intentando cambiar la contrase&ntilde;a de usuario.");
		
							
						}					
					
				}else{//Si es login
					logger.info("Comenzando Login...");
					
					req.getSession().setAttribute("password_cambiado", "");
					
					logger.info("Verificando usuario " +userid+ " en RACF...");
				
					usuario = (Usuario) clientews.enviarYRecibir(consulta,
							ConsultaDeUsuario.class, Usuario.class, userid, ip);
										
					logger.info("Oficina" + usuario.getOficina());
					logger.info("Literal" + usuario.getLiteral());
					logger.info("Perfil" + usuario.getPerfilUsuario());

					//Se monta ens ession la oficina que retorna racf
					req.getSession().setAttribute(ConstantesGenerales.CODIGO_SUCURSAL, usuario.getOficina());
					
					//*******************************************************************************************
					//DAO a Utilizar
					OficinaDAO oficinaDAO = new OficinaDAO(datasourceInfi);

					//Se valida el n�mero de oficina del servicio primero con la tabla de oficinas					
					boolean oficinaTabla = oficinaDAO.verificarOficina(usuario.getOficina());
					
					//*******************************************************************************************
					logger.info("agenciaDeArchivo: "+agenciaDeArchivo);
					//Verificamos que el archivo se haya inicializado
					if (agenciaDeArchivo!=null){
						if (!usuario.getOficina().equals(agenciaDeArchivo)){
							logger.info("Usuario INVALIDO a nivel de Oficina . . . ");
							logger.info("IP del Cliente: " + req.getRemoteAddr());
							logger.error(LoginRACF.MSJ_NO_OFICINA);
							if(!oficinaTabla)
							{
								logger.error(LoginRACF.MSJ_NO_OFICINA);
								throw new Exception(LoginRACF.MSJ_NO_OFICINA);
							}							
						}
						else{
							logger.info("Usuario VALIDO a nivel de Oficina . . . ");
							}
						}// del else/if
					
					//Verifica que el usuario se est� conectando desde la oficina correcta					
					
					/*
					 * si trajo un usuario, retorna el usuario para que logee en la
					 * aplicacion
					 */
					if (usuario != null	&& usuario.getUsuario() != null) {
						logger.info("Usuario '" +usuario.getUsuario()+ "' encontrado en RACF");
						logger.info("Conexi�n satisfactoria a RACF");
						logger.info("Buscando usuario en SEPA...");					
						
						/*ANTERIOR
						ds = db.get(datasource, "select rolename "
								+ "from msc_user u, msc_role r "
								+ "where u.userid='" + userid + "' "
								+ "and u.msc_role_id=r.msc_role_id");*/					
						
						ds = db.get(datasource, "select r.rolename, u.user_status "
								+ "from msc_user u, msc_role r,MSC_APPLICATIONS a,MSC_ROLE_USER ru "
								+ "where UPPER(u.userid)=UPPER('"
								+ userid
								+ "') "
								+ "and r.id_application=a.ID_APPLICATION and a.SIGLAS_APPLIC='" +AppProperties.getProperty("app-name")+ "' "
								+ "and ru.MSC_ROLE_ID=r.MSC_ROLE_ID and ru.MSC_USER_ID=u.MSC_USER_ID");
						
						
						if (ds.next()) {
							logger.info("Consulta satisfactoria al usuario en tablas de Seguridad");
							String rolDeUsuario = ds.getValue("rolename");
							logger.info("ROL DE USUARIO : "+ rolDeUsuario);
							
							
							if(enabled)
							{
								DataSet sessiones = db.get(datasourceInfi,"select * from active_sessions where UPPER(userid)=UPPER('"+usuario.getUsuario().trim()+"')");
								
								if(sessiones.count()>0){

									logger.error("(FWRK-SEC-0001) - El Usuario ya se encuentra autenticado en otra sesi�n.");
									throw new Exception("(FWRK-SEC-0001) - El Usuario ya se encuentra autenticado en otra sesi�n.");
								}
								
								
								if(activeSessions)
								{
									StringBuffer sql = new StringBuffer();
									sql.append("insert into active_sessions (sessionid, userid, start_date, start_time, remote_address, host_uri) values(" );
									
									sql.append("'"+Util.replace(Util.replace(req.getSession().getId(), "[SessionImpl ", ""), "]", "")+"'").append(",UPPER('");
									sql.append(usuario.getUsuario().trim()).append("'),").append("sysdate").append(",").append("sysdate").append(",'");
									sql.append(req.getRemoteAddr()).append("','").append(req.getRemoteHost()).append("')");
									
									//Se inserta en active sessions
									db.exec(datasourceInfi, sql.toString());
								}
							}

							ConstantesGenerales.RIF=obtenerRifBDVReporteria();
												
							req.getSession().setAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA, ParametrosDAO.listarParametros(ParametrosSistema.MONEDA_BS_REPORTERIA, datasourceInfi));
							
							logger.info("MONEDA_LOCAL_REPORTERIA: "+req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));
							
							return new ActionUser(userid, rolDeUsuario);
														

						}else{
							logger.info("Usuario " +userid+ " no encontrado en Aplicaci&oacute;n de Seguridad");
							throw new Exception(
							"El usuario " +userid+ " no est� registrado en Aplicaci&oacute;n de Seguridad");
						}
						
					}
					
				}
				
				
			} catch (Exception e) {
				logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
				userid = null;//limpiar datos del usuario
				password = null;//limpiar datos del usuario
				//mensaje de error para login-error.jsp
				//--Para Control de la pagina login-error.jsp
				//--Codigos:
				//--error_login 0 : Fallo desconocido de conexi�n (Se inicializa de esta manera)
				//--error_login 1 : No es la oficina correcta o no se introdujeron todos los datos del usuario (mantiene la pagina en formulario de login)
				//--error_login 2 : Error relacionado con un cambio de contrase�a (muestra el formulario de cambio de contrase�a)
				//--error_login 3,4,5,6,7,8 : solo se utilizan para la cuenta de otras excepciones donde se necesite mantener la pagina en el formulario de login mientras se muestra el error al usuario
				
				req.getSession().setAttribute("mensaje_error_login", "FALLO DE CONEXI&Oacute;N");
				req.getSession().setAttribute("error_login", "0");//pantalla de login
				activeSessions = false;
				
				if(e.getMessage()!=null){					

					if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_NO_OFICINA.toUpperCase())>-1){
						req.getSession().setAttribute("mensaje_error_login", "No puede accesar a la aplicaci&oacute;n desde este equipo");
						req.getSession().setAttribute("error_login", "1");//pantalla de login
					}					
					
					//Si el usuario se encuentra ya registrado
					if(e.getMessage().toUpperCase().indexOf(LoginRACF.FWRK_SEC_0001.toUpperCase())>-1){
						
						throw e;
					}
					//Colocar el mensaje de error en sesion para ser leido por login-error.jsp
					
					if(e.getMessage().toUpperCase().indexOf(ERROR_DATOS_OBLIGATORIOS.toUpperCase())>-1){
						req.getSession().setAttribute("mensaje_error_login", ERROR_DATOS_OBLIGATORIOS);
						req.getSession().setAttribute("error_login", "1");//pantalla de login
					}else{
						
						if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_PASSWORD_INVALIDO.toUpperCase())>-1){
							req.getSession().setAttribute("mensaje_error_login", "Contrase&ntilde;a Inv&aacute;lida");
							req.getSession().setAttribute("error_login", "1");//pantalla de login
						}else{
							
							if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_PASSWORD_EXPIRADO.toUpperCase())>-1){
								req.getSession().setAttribute("mensaje_error_login", "La Contrase&ntilde;a ha expirado");					
								req.getSession().setAttribute("error_login", "2");//pantalla de cambio de password
							}else{
								
								if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_USUARIO_REVOCADO.toUpperCase())>-1){
									req.getSession().setAttribute("mensaje_error_login", "Usuario revocado por RACF");					
									req.getSession().setAttribute("error_login", "3");//pantalla de login
								
								}else{
									if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_NUEVO_PASS_INVALIDO.toUpperCase())>-1){
										req.getSession().setAttribute("mensaje_error_login", "La nueva contrase&ntilde;a ha sido utilizada con anterioridad. Intente de nuevo.");					
										req.getSession().setAttribute("error_login", "2");//pantalla de cambio de password 					
									}else{
										if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_BEAN_NO_COMPILADO.toUpperCase())>-1){
											req.getSession().setAttribute("mensaje_error_login", "Fallo conexi&oacute;n a RACF. No se puede acceder a los beans.");					
											req.getSession().setAttribute("error_login", "5");//pantalla de login		
										}else{
											if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_USUARIO_INVALIDO.toUpperCase())>-1){
												req.getSession().setAttribute("mensaje_error_login", "Usuario Inv&aacute;lido");					
												req.getSession().setAttribute("error_login", "6");	//pantalla de login
											}else{
												if(e.getMessage().toUpperCase().indexOf(LoginRACF.MSJ_PASS_NO_COINCIDEN.toUpperCase())>-1){
													req.getSession().setAttribute("mensaje_error_login", "Los campos para nueva contrase&ntilde;a y verificaci&oacute;n de nueva contrase&ntilde;a no coinciden");					
													req.getSession().setAttribute("error_login", "2");//pantalla de cambio de password	

												}else{
													if(e.getMessage().toUpperCase().indexOf(LoginRACF.TRANSACCION.toUpperCase())>-1){
														req.getSession().setAttribute("mensaje_error_login", "Transacci&oacute;n anterior no finalizada, intente m&aacute;s tarde.");					
														req.getSession().setAttribute("error_login", "7");//pantalla de login	
													}else{
														if(e.getMessage().toUpperCase().indexOf(LoginRACF.ARQUITECTURA_EXTENDIDA.toUpperCase())>-1){
															req.getSession().setAttribute("mensaje_error_login", "Error de comunicaci&oacute;n con Arquitectura Extendida.");					
															req.getSession().setAttribute("error_login", "8");//pantalla de login
														} else {
															req.getSession().setAttribute("mensaje_error_login", e.getMessage());					
															req.getSession().setAttribute("error_login", "9");//pantalla de login
														}
													}
												}
											}
												
										}
										
									}
								}
							}					
						}
						
					}
					
				}
				

			}
			
		

		/*
		 * En este caso no hizo login con ningun rol (tal vez no hay roles en la
		 * BD), por lo que retorna null para indicar que no hace login
		 */
		return null;
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
 

	/**
	 * Inicializa el login a partir de un xml ubicado en
	 * WEB-INF/login_config.xml.
	 * 
	 * El contenido del archivo de configuracion son los tres parametros
	 * necesarios para hacer la llamada al ws de login y el directorio para
	 * poder cargar la configuracion de WS.
	 * 
	 * @see megasoft.AbstractLogin#init(java.lang.String)
	 * @see #basePath
	 */
	public void init(String xmlConfig) throws Exception {
		
		super.init(xmlConfig);
		// leer la configuracion
		Document doc = new Document(xmlConfig);
		Element root = doc.getRoot();
		endpoint = root.getElement("endpoint").getTextString();
		schema = root.getElement("schema").getTextString();
		operation = root.getElement("operation").getTextString();
		/*String basePath = root.getElement("basepath").getTextString();
		context = new LoginContext(basePath);*/

		String dataSourceName = root.getElement(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA).getTextString();
		datasource = db.getDataSource(dataSourceName);
		
		String dataSourceNameInfi = root.getElement(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI).getTextString();
		datasourceInfi = db.getDataSource(dataSourceNameInfi);
		
	}

	/**
	 * Esta clase LoginContext existe solo con un unico proposito: el de poder
	 * hacer login hacia arquitectura extendida.
	 * 
	 * Para que el login se pueda hacer hacia AEA, hay que llamar a un Web
	 * Service de login. Para poder hacer la llamada al WS de login, hay que
	 * cargar un archivo de configuracion de axis, que se encuentra en
	 * /WEB-INF/classes/com/megasoft/bo/webservice/axis2.xml; tambien hay que
	 * cargar los modulos de rampart que se cuentran en /WEB-INF/modules.
	 * 
	 * Las rutas de estos archivos deben ser provistas dentro del ClienteWs, y
	 * para obtener estas rutas se utiliza, dentro de esa clase ClienteWs,
	 * ServletContext.getRealPath(). El ClienteWs utiliza ServletContext porque
	 * los modelos de La Fundacion tienen este objeto y con el se puede obtener
	 * facilmente las referencias a los archivos necesarios. El ClienteWs fue
	 * dise�ado y es ampliamente utilizado indirectamente por los modelos de la
	 * fundacion (en realidad lo usan son los manejadores, que son las clases de
	 * negocio).
	 * 
	 * Ahora, en la clase Login no hay forma de obtener una referencia a estos
	 * archivos, tomando en cuenta que la forma de hacerlo debe ser portable
	 * entre application servers y funcionar tanto en una aplicacion
	 * descomprimida como dentro de un war. Ya sabemos que con
	 * Class.getResourceAsString("../../../archivo.xml") no sirve en los .war,
	 * porque el cambio al directorio padre no te deja) en el lugar correcto (en
	 * todos los servidores, porque cada servidor descomprime el .war como le da
	 * la gana.
	 * 
	 * Por eso se crea esta clase context para pasarla al ClienteWs. Ahora, esta
	 * clase solo implementa el metodo getRealPath, porque es el unico que
	 * necesita; los demas no hacen nada y no se utilizan. Como esta clase no
	 * tiene forma de obtener el path real, este debe ser provisto externamente;
	 * y se provee en el archivo de configuracion de Login, que es
	 * /WEB-INF/login-config.xml.
	 * 
	 * Alli debe ser configurado el path del directorio padre de /WEB-INF/. Este
	 * parametro es cargado al inicializar el Login, y es reutilizado por
	 * getRealPath() en esta clase.
	 * 
	 * 
	 * @author camilo torres
	 * @see Login#basePath
	 * @see Login#init(String)
	 * @see Login
	 * @see #getRealPath(String)
	 */
	public class LoginContext implements ServletContext {
		/**
		 * Directorio padre del directorio /WEB-INF/. Este directorio es
		 * necesario para poder cargar los WS. Ver la explicacion del porque es
		 * necesario en la clase interna LoginContext. Este directorio no se
		 * puede determinar en el Login, por lo que debe ser provisto por el
		 * usuario manualmente en el archivo de configuracion
		 * /WEB-INF/login_config.xml y se carga en el metodo de inicializacion
		 * de login.
		 * 
		 * @see LoginContext
		 */
		private String basePath = null;

		/**
		 * Crea el contexto con el path base. El path base se usara para obtener
		 * el path real con getRealPath
		 * 
		 * @param basePath
		 *            que se indica manualmente y apunta al directorio padre de
		 *            /WEB-INF/
		 */
		public LoginContext(String basePath) {
			this.basePath = basePath;
		}

		/**
		 * este es el unico metodo que se utiliza de esta clase. retorna un path
		 * que es utilizado para cargar la configuracion de axis por el
		 * clientews.
		 */
		public String getRealPath(String arg0) {
			if (basePath != null) {
				return basePath + arg0;
			} else {
				return arg0;
			}
		}

		public Object getAttribute(String arg0) {
			return null;
		}

		public Enumeration getAttributeNames() {
			return null;
		}

		public ServletContext getContext(String arg0) {
			return null;
		}

		public String getInitParameter(String arg0) {
			return null;
		}

		public Enumeration getInitParameterNames() {
			return null;
		}

		public int getMajorVersion() {
			return 0;
		}

		public String getMimeType(String arg0) {
			return null;
		}

		public int getMinorVersion() {
			return 0;
		}

		public RequestDispatcher getNamedDispatcher(String arg0) {
			return null;
		}

		public RequestDispatcher getRequestDispatcher(String arg0) {
			return null;
		}

		public URL getResource(String arg0) throws MalformedURLException {
			return null;
		}

		public InputStream getResourceAsStream(String arg0) {
			return null;
		}

		public Set getResourcePaths(String arg0) {
			return null;
		}

		public String getServerInfo() {
			return null;
		}

		public Servlet getServlet(String arg0) throws ServletException {
			return null;
		}

		public String getServletContextName() {
			return null;
		}

		public Enumeration getServletNames() {
			return null;
		}

		public Enumeration getServlets() {
			return null;
		}

		public void log(String arg0) {
		}

		public void log(String arg0, Throwable arg1) {
		}

		public void log(Exception arg0, String arg1) {
		}

		public void removeAttribute(String arg0) {
		}

		public void setAttribute(String arg0, Object arg1) {
		}

	}
	

	/**Obtiene los datos del archivo leido de la m�quina del cliente*/
	private void obtenerDatos(HttpServletRequest req){
		
		agenciaDeArchivo=req.getParameter("iniFile");
	}
	
	private String obtenerRifBDVReporteria(){
		String tipoPersona="",rif="", digitoVerif="", separador1="-", separador2="";		
		String digitoVerifRIFBDV;
		try {
			rif=ParametrosDAO.listarParametros(ParametrosSistema.RIF_BDV, datasourceInfi);
			
			digitoVerifRIFBDV = ParametrosDAO.listarParametros(ParametrosSistema.RIF_DIG_VERIF_REPORTERIA, datasourceInfi);		
			
			if(digitoVerifRIFBDV.equalsIgnoreCase("1")){
				tipoPersona=rif.substring(0,1);				
				rif=Utilitario.rellenarCaracteresTrunc(String.valueOf(Integer.parseInt(rif.substring(2))),'0',8,false);
				separador2="-";
				digitoVerif=Utilitario.digitoVerificador(tipoPersona+rif,true);
				rif=tipoPersona+separador1+rif+separador2+digitoVerif;
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Login. Error en la configuracion de RIF del BDV"+e.getMessage());
		}
		return rif;
	}
	
}
