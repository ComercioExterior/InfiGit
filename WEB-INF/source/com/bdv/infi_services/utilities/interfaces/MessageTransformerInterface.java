package com.bdv.infi_services.utilities.interfaces;

public interface MessageTransformerInterface {

	
	/**
	 * Retorna un Objeto del tipo espeficicado en classType
	 * @param classType
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public Object unmarshall(Class classType, String xml) throws Exception;
	
	/**
	 * Retorna un String xml el cual representa el Objecto
	 * @param classType
	 * @param xml
	 * @return
	 * @throws Exception
	 */

	public String marshall(Class classType, Object objeto) throws Exception;
}
