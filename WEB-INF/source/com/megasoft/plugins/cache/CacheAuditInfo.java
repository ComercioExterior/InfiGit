package com.megasoft.plugins.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.db;
import org.apache.log4j.Logger;
import com.megasoft.enginecachecontrol.interfaces.InterfaceCachedPlugin;

public class CacheAuditInfo implements InterfaceCachedPlugin {

	public Object getObject(Logger log, Properties propiedades) {
		 
		//Inicio del proceso
		long ini = System.currentTimeMillis();
		Hashtable htInfoUrl = new Hashtable();
		String nombreActual ="";
		try	
		{
			
			//Carga toda la configuracion de los action en memoria
			
			DataSource dso = db.getDataSource(propiedades.getProperty("JNDIName"));
			DataSet dsInfo = db.get(dso, "select  config.url , name from	url_parameters_log_config PARAM RIGHT JOIN url_log_config config ON  param.id_config=config.id_config and config.enable=1");
			
			
			ArrayList arrListParametros = null;
			//Por cada valor itera y verifica que este cargado
			while (dsInfo.next())
			{
			
				
				if (!nombreActual.equals(dsInfo.getValue("url")))
				{
					nombreActual = dsInfo.getValue("url");
					arrListParametros = new  ArrayList();
					// No tiene parametros configurados
					if (dsInfo.getValue("name")==null)
						{
							//System.err.println("Name null");
							arrListParametros=new ArrayList();
						}
					// Tiene parametros configurados
					else
						{

							arrListParametros.add(dsInfo.getValue("name"));
						}
				//	System.err.println("Coloco por primera vez" +dsInfo.getValue("name"));
				}
				else
				{
				//	System.err.println("Coloco " +dsInfo.getValue("name") +" y salio ");
					arrListParametros.add(dsInfo.getValue("name"));
				}
				htInfoUrl.put(nombreActual, arrListParametros);
				
				
				
				
				
			}
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ocurrio un error obteniendo la secuencia de log en " + this.getClass().getName() + " ERROR: " + e.getMessage());
		}
		
	//	System.err.println(htInfoUrl.get(nombreActual).toString());
		
		
		long fin = System.currentTimeMillis();
		System.err.println("TIEMPO DE PLUGING = " + (fin-ini));
		
		return htInfoUrl;
		
		
	}



}
	
	
	

