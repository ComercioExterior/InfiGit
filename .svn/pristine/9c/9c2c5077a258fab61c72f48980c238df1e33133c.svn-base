package com.bdv.infi_services.utilities;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;

import com.bdv.infi_services.utilities.interfaces.MessageTransformerInterface;


/**
 * Clase utilitaria para hacer marshall y unmarshall
 * 
 * @author ncv
 * 
 */
public class MarshallUnMarshallJIBXS implements MessageTransformerInterface {


	private static HashMap<Class, IUnmarshallingContext> mapaDeUnmarshallContext = new HashMap<Class, IUnmarshallingContext>();

	private static HashMap<Class, IMarshallingContext> mapaDeMarshallContext = new HashMap<Class, IMarshallingContext>();

	/**
	 * Retorna una instancia del Bean con datos, segun el xml recibido
	 * 
	 * @param classType
	 *            Clase beans para hacer el Marshall
	 * @param xml
	 *            String del xml para realizar UnMarshall
	 * @return
	 * @throws Exception
	 */
	public Object unmarshall(Class classType, String xml) throws Exception {
		return MarshallUnMarshallJIBXS.unmarshallStatic(classType, xml);
	}
	public static synchronized Object unmarshallStatic(Class classType, String xml)
			throws Exception {

		IUnmarshallingContext iunmarshallingcontext = mapaDeUnmarshallContext
				.get(classType);
		if (iunmarshallingcontext == null) {
			IBindingFactory ibindingfactory = BindingDirectory
					.getFactory(classType);
			iunmarshallingcontext = ibindingfactory
					.createUnmarshallingContext();
			mapaDeUnmarshallContext.put(classType, iunmarshallingcontext);
		}

		ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
				xml.getBytes());
		Object ob=iunmarshallingcontext.unmarshalDocument(bytearrayinputstream,
		"ISO-8859-1");

		return ob;
	}

	/**
	 * Retorna el XML Representado por los Objetos
	 * 
	 * @param classType
	 *            Clase beans para hacer el Marshall
	 * @param objeto
	 *            Instancia del bean
	 * @return
	 * @throws Exception
	 */
	public String marshall(Class classType, Object objeto)throws Exception {
		return MarshallUnMarshallJIBXS.marshallStatic(classType, objeto);
	}
	public static synchronized String marshallStatic(Class classType, Object objeto)
			throws Exception {
		IMarshallingContext mctx = mapaDeMarshallContext.get(classType);
		if (mctx == null) {
			IBindingFactory ibindingfactory = BindingDirectory
					.getFactory(classType);
			mctx = ibindingfactory.createMarshallingContext();
			mapaDeMarshallContext.put(classType, mctx);
			mctx.setIndent(3);
		}

		StringWriter swri = new StringWriter();

		mctx.marshalDocument(objeto, "ISO-8859-1", null, swri);

		return swri.toString();
	}
}
