package com.bdv.trace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.trace.config.MSCFilterConfig;
import com.bdv.trace.database.DbWrapper;
import com.bdv.trace.utils.UtilWrapper;
import com.megasoft.enginecachecontrol.MegaCacheControl;

public class MSCFilterUrlAudit implements Filter {

	DataSource dso = null;
	MegaCacheControl cache = new MegaCacheControl();
	String sqlInsertMaster 	="INSERT INTO log_url(action, fecha, time, ip, usuario, parameters)VALUES " +
			"('@action@', TO_DATE('@fecha@','yyyy-mm-dd'), '@time@', '@ip@', '@usuario@', '@parameters@')";
	
	
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
		long t1 = System.currentTimeMillis();
		
		
		// Verifica que el fitrol este habilitado
		if (!MSCFilterConfig.filter_enable)
			{
			chain.doFilter(req, res);
			return;
			}
		
		try	
			{
			

			HttpServletRequest _req = (HttpServletRequest) req;
			
			//Obtiene el url invocado
			String url = _req.getRequestURI();
			while(url.indexOf("/",1)!=-1){
				url= url.substring(url.indexOf("/", 1));
			}
			
			
			HttpSession s = _req.getSession(true);
			Hashtable htActionToAudit =null;
			
			if(!url.endsWith(".jpg")&&!url.endsWith(".gif")&&!url.endsWith(".png")&&!url.endsWith("menu_show-sidebar")&&!url.endsWith("show-sidebar1")&&!url.endsWith("show-view")&&!url.endsWith("menu-frameset")&&!url.endsWith("j_login")&&!url.endsWith("j_logout")){
				
				//Busca en memoria de no existir lo busca en base de datos
				htActionToAudit = getActionCache(url);

			
				// Busca en el cache si existe ese URL configurado
				if (htActionToAudit.containsKey(url))
				{
					log("Si existe entro");
					// Declara variables parpara insert
					String 	ip = 	_req.getRemoteAddr();
					String 	date = UtilWrapper.getDate();
					String 	time = UtilWrapper.getTime();
					//DataSource dso = DbWrapper.getDataSource("jdbc/infi");
					String 	usuario = nameUser(String.valueOf(s.getAttribute("framework.user.principal")),dso);
					String sql = sqlInsertMaster;
					
					//reemplaza los valores
					String docXml =null;
					sql = UtilWrapper.replace(sql, "@action@", url);
					sql = UtilWrapper.replace(sql, "@fecha@", date);
					sql = UtilWrapper.replace(sql, "@time@", time);
					sql = UtilWrapper.replace(sql, "@usuario@", usuario);
					sql = UtilWrapper.replace(sql, "@ip@", ip	);
					sql = Util.replace(sql,"/action/","");//adicional
					docXml ="";
					
					// Obtiene el ArrayList de parametros
					ArrayList htActionParameters = (ArrayList) htActionToAudit.get(url);
	
					// Si tiene parametros 
					if (htActionParameters.size()>0) 
					{
						log("tiene parametroes");
	
						Iterator iter = htActionParameters.iterator();
						 
						String name = "";
						//Por cada paramatro busca el valor
						while (iter.hasNext())
						{
							
							name  = (String) iter.next();
							String valor = getValue(name, _req);
							/*if (valor==null){
								valor="Registro Eliminado";
								docXml+= "<parameter><name>"+ name +": </name><value>"+ valor +"</value></parameter>";
							}else
							docXml+= "<parameter><name>"+ name +": </name><value>"+ valor +"</value></parameter>";					
							*/
							if (valor!=null&&!valor.equals("")){
								docXml+= "<parameter><name>"+ name +": </name><value>"+ valor +"</value></parameter>";
							}
						}
	
					}
					else
					{	
						//En el caso no tiene parametros
						log("NO   tiene parametros van todos");
						
						//Obtiene todos los parametros del request
						Enumeration parameters = _req.getParameterNames();
						String name = "";
						//Busca cada valor de cada parametro
						while (parameters.hasMoreElements())
						{
							name  = (String) parameters.nextElement();
							String valor = getValue(name, _req);
							/*if (valor==null){
								valor="Registro Eliminado";
								docXml+= "<parameter><name>"+ name +": </name><value>"+ valor +"</value></parameter>";
							}else
							docXml+= "<parameter><name>"+ name +": </name><value>"+ valor +"</value></parameter>";
							*/
							if (valor!=null&&!valor.equals("")){
								docXml+= "<parameter><name>"+ name +": </name><value>"+ valor +"</value></parameter>";
							}
						}
					}
	
					if (!docXml.equals(""))
						docXml="<log_url>"+docXml+"</log_url>";
					
					
					sql = UtilWrapper.replace(sql, "@parameters@", docXml);
					//Ejecuta el insert
					DbWrapper.exec(dso,sql);
				
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		log("TIEMPO " + (System.currentTimeMillis()-t1));
		chain.doFilter(req, res);
		return;
		
	}

	private static String getValue(String field, HttpServletRequest _req)
	{
		//Obtiene el arreglo de parametros
		String[] arrPara = _req.getParameterValues(field);
		if (arrPara==null)
			return null;
		
		log("aqui ");
		String value = "";
		
		// si es un arreglo los coloca entre corchete
		if (arrPara.length>1)
		{
		
			for (int i =0; i<arrPara.length; i++)
				value+="[" +arrPara[i]+"]";
		}
		else
			value = arrPara[0];
	
		return value;
	}
	public void init(FilterConfig arg0) throws ServletException {
		// captura el datasource
		
		try {
			String dataSource = arg0.getServletContext().getInitParameter(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI);
			dso=DbWrapper.getDataSource(dataSource);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public Hashtable getActionCache(String action) throws Exception
	{
		//crea el datasource
	
		
		
		
		log("Buscando " + action);
		Hashtable htInfo = (Hashtable)cache.getObject("CacheAuditInfo");
		
		
		//Si no esta en cache lo crea vacio 
		if (htInfo==null)
			htInfo = new Hashtable();
		
		
		log(action);
		
		
		
		//Verifica que este en el HT para retornarlo intacto
		if (htInfo.containsKey(action))
		{
			log("Cache encontrado se retornará");
			return htInfo;
		}
		else
		{
			log("Cache NO encontrado se buscara en base de datos");
			//DataSource dso = DbWrapper.getDataSource("jdbc/infi");
			DataSet dsInfo = null;
			// Si no esta realiza el query a base de datos para colocar la configuracion en cache
			dsInfo = DbWrapper.get(dso, "select  config.url , name from	url_parameters_log_config PARAM RIGHT JOIN url_log_config config ON  param.id_config=config.id_config where config.enable=1 and url='" +action+"'");
			
			if (! dsInfo.next())
				{
					log("No encontrado en BD");
					return new Hashtable();
					
				}
			
			ArrayList arr = new ArrayList();
			// busca por cada ocurrencia los parametros
			dsInfo.first();
			while (dsInfo.next())
			{
					log("Existen parametros");
				
					// No tiene parametros configurados
					if (dsInfo.getValue("name")!=null)
						{
							log("Se coloca " +dsInfo.getValue("name"));
							arr.add(dsInfo.getValue("name"));
						}
					
			}
			// Coloca el arrayList en el HT
			htInfo.put(action, arr);
			
			// Guarda en cache
			cache.setObject("CacheAuditInfo", htInfo);
			
			return htInfo;
		}
	}

	
	public  static void log(String text)
	{
		if (MSCFilterConfig.log_enable)
			System.err.println("[" + text +"]");
	}
	
	public String nameUser(String id,DataSource dso)throws Exception{
		String sql = "select msc_user_id from MSC_USER where userid='"+id+"'";
		DataSet usuario = db.get(dso,sql);
		usuario.first();
		usuario.next();
		return usuario.getValue("msc_user_id");
	}
}