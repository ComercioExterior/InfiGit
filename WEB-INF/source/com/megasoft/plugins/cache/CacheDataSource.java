package com.megasoft.plugins.cache;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import com.megasoft.datasource.HandlerDataSource;
import com.megasoft.enginecachecontrol.interfaces.InterfaceCachedPlugin;

public class CacheDataSource implements InterfaceCachedPlugin {

	public Object getObject(Logger log, Properties propiedades) {
		 
		DataSource ds =null;
		try
			{
			
			// Crea el handler de DataSource
			HandlerDataSource handleDs = new HandlerDataSource();
			
			// Crea el datasource
			handleDs.createDataSource(propiedades);
			
			// Busca y retorna el datasource
			ds = handleDs.getDataSource("jdbc/infi");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("No se pudo registrar el DataSource"  , e);
		}
		return ds;
	}
}
	
	
	

