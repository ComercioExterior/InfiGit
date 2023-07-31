package com.bdv.infi_services.utilities;

import com.bdv.infi_services.utilities.interfaces.MessageTransformerInterface;
import com.megasoft.soa.webservices.commom.WSProperties;

public class MessageTransformer {

	private static MessageTransformerInterface plug = null;
	private static String classPluging = null;
	public static String marshall(Class classType, Object objeto) throws Exception
	{
		if (plug==null)
		{
			classPluging = WSProperties.getProperty("message-transformer-pluging");
			
			if (classPluging==null)
				throw new Exception ("La propiedad (message-transformer-pluging) para el plugin de Message Transformer no esta Definida en el archivo ws_properties.xml");
			
			
			Class Object = Class.forName(classPluging);
			plug = (MessageTransformerInterface) Object.newInstance();
		}
		
		return plug.marshall(classType, objeto);
	}
	
	public  static  Object unmarshall(Class classType, String xml) throws Exception
	{
		classPluging = WSProperties.getProperty("message-transformer-pluging");
		
		if (classPluging==null)
			throw new Exception ("La propiedad (message-transformer-pluging) para el plugin de Message Transformer no esta Definida en el archivo ws_properties.xml");

		if (plug==null)
		{
			Class Object = Class.forName(classPluging);
			plug = (MessageTransformerInterface) Object.newInstance();
		}
		
		return plug.unmarshall(classType, xml);
		
	}
}
