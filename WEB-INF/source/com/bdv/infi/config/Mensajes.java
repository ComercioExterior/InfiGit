package com.bdv.infi.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

/**Clase destinada para la recuperaci&oacute;n y manejo de los errores 
 * configurados en el archivo mensaje.properties*/
public class Mensajes {
	
	/**Instancia del objeto*/
	private static Mensajes instancia = null;
	
	/**Ruta del archivo de mensajes*/
	private String ERROR_MESSAGES_FILE = "/config/mensajes.properties";
	
	/**C&oacute;digo de error*/
	private String codigoError = "";
	
	/**Archivo de propiedades*/
	Properties mp = new Properties();	
	
	/**Recupera la &uacute;nica instancia del objeto mensajes
	 * @return Objeto de mensajes para recuperar los mensajes o errores de la aplicaci&oacute;n*/
	public static Mensajes getInstancia(){
		if (instancia==null){
			instancia = new Mensajes();
		}
		return instancia;
	}
	
    /**
     * <p>mensaje retornado al no encontrar un mensaje asociado al key especificado en el archivo de mensajes de error</p> 
     */
    protected static String MENSAJE_NO_ENCONTRADO = "***[MENSAJE NO DISPONIBLE]***";	
	
	/**Carga el archivo de mensajes mensajes.properties*/
	private void Mensajes(){
		 // Se establece la ubicaci&oacute;n exacta del archivo de mensajes de error
        File errorMsgFile = new File(ERROR_MESSAGES_FILE);		
		try {
			mp.load(new FileInputStream(errorMsgFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	  /**
     * <p>Retorna el mensaje de error que corresponde a errorKey. 
     * Los mensajes retornados por este metodo corresponden a errores propios de Kenan</p>
     * 
     * @param errorKey Identificador del c&oacute;digo de error a buscar en el properties
     * @throws InvalidArgumentException si el codigo de error recibo es nulo o blanco
     * @return Descripcion del mensaje encontrado en el properties
     */
    public String getMensaje(String errorKey) throws Exception {
        if (errorKey == null) {
            throw new Exception("Parametro errorKey nulo");
        }
        if (errorKey.trim().equals("")) {
            throw new Exception("Parametro errorKey Inv&aacute;lido");
        }

        //Seteamos el codigo de error usado
        codigoError = errorKey;
        
        String msg = this.mp.getProperty(errorKey);
        if (msg == null) {
            return MENSAJE_NO_ENCONTRADO + " (errorKey: " + errorKey + ")";
        } else {
            return msg;
        }
    }

    /**
     * <p>Retorna el mensaje de error que corresponde a errorKey.</p>
     * 
     * @param errorKey Identificador del c&oacute;digo de error a buscar en el properties
     * @param msgParts un array de mensajes que deben ser sustituidos en la descripci&oacute;n del mensaje principal
     *         encontrado en el archivo properties
     * @throws Exception si el codigo de error recibo es nulo o blanco
     * @return Descripcion del mensaje encontrado en el properties
     */
    public String getMensaje(String errorKey, Object[] msgParts) throws Exception {
        if (errorKey == null) {
            throw new Exception("Parametro errorKey nulo");
        }
        if (msgParts == null) {
            throw new Exception("Parametro msgParts nulo");
        }

        //Seteamos el codigo de error usado        
        codigoError = errorKey;

        String msg = this.mp.getProperty(errorKey);
        
        int cont = 0;
        int pos = -1;
        while ((pos = (msg.indexOf('{', pos + 1))) != -1) {
            cont++;
        }
        if (cont != msgParts.length) {
            throw new Exception(" El Numero de Parametro del mensaje no es igual al numero de parametro que estan en msgParts");
        }

        if (msg == null) {
            return MENSAJE_NO_ENCONTRADO + " (errorKey: " + errorKey + ")";
        } else {
            return MessageFormat.format(msg, msgParts);
        }
    }	
}