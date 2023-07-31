package com.bdv.infi.logic.interfaz_opics.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class MensajeOpicsDetalle {
	
	/**Representa el valor de un campo*/
	public final String  VALOR_VACIO = "";
	
	/*** BRANCH*/
	public final static String  BRANCH = "BRANCH";
	
	/*** Orden*/
	public final static String  ORDENEID = "ORDENEID";

	/*** Valor por defecto del BRANCH BDV*/
	public final String  BRANCHDEFECTO = "99";

	
	/**Separador de campos*/
	final String SEPARADOR = ",";
	
	/**Tipo de operación del mensaje opics*/
	public final static String TIPO_MENSAJE="TIPO_MENSAJE";
	
	/**Mensaje de renta fija*/
	public final static String RENTA_FIJA="RENTA_FIJA";
	
	/**Mensaje de operación de cambio*/
	public final static String OPERACION_CAMBIO="OPERACION_CAMBIO";
	
	
	/**Constante del grupo de valores fijos que debe enviarse en el mensaje de operación de cambio*/
	public final static String MENSAJE_OPICS_OC = "MENSAJE_OPICS_OC";
	
	/**Constante del grupo de valores fijos que debe enviarse en el mensaje de renta fija*/
	public final static String MENSAJE_OPICS_RF = "MENSAJE_OPICS_RF";
	
	/**Forma de fecha que deben contener los campos de fecha*/
	public final static String FORMATO_FECHA = "yyyyMMdd";
	
	/**Ultimo campo para registro de renta fija*/
	public final static String ULTIMO_CAMPO_RF = "SUPPCCY";
	
	/**Ultimo campo para registro de operacion de cambio*/
	public final static String ULTIMO_CAMPO_OC = "FMARGINREBATEAMT";
	
	/**Corresponde al id del mensaje generado*/
	private long idOpics;
	
	/**Corresponde al número del mensaje*/
	private int numeroMensaje;
	
	/**Valores del mensaje*/
	LinkedHashMap<String,String> valores = new LinkedHashMap<String,String>();	
	
	/**Genera la cadena de caracteres que debe ser enviada en el archivo plano */
	public String generarRegistro(){
		String clave="";
		String valor="";
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = valores.keySet().iterator();
		while (it.hasNext()){
			clave = it.next();
			//Si la clave es tipo de mensaje lee la próxima, ya que este valor no debe ir en el archivo opics
			if (clave.equals(MensajeOpicsDetalle.TIPO_MENSAJE) || clave.equals(MensajeOpicsDetalle.BRANCH) || clave.equals(ORDENEID)){
				continue;
			}
			valor = valores.get(clave);
			
			//Verifica si el valor contiene una , para encerrarlo entre "
			if (valor.indexOf(",") > -1){
				valor = "\"" + valor + "\"";
			}			
			sb.append(valor);
			if (it.hasNext() && !clave.equals(ULTIMO_CAMPO_RF) && it.hasNext() && !clave.equals(ULTIMO_CAMPO_OC)){
				sb.append(SEPARADOR);
			}
		}
		System.out.println("generarRegistro: ---------> "+sb.toString());
		return sb.toString();
	}
	
	/**Estable cel valor de la variable
	 * @param clave campo clave para buscar el objeto
	 * @param valor valor de la clave
	 * @throws lanza una excepción si se está seteando el tipo de mensaje y esta no es del tipo esperado*/
	public void set(String clave, String valor) throws Exception{
		if (clave.equals(MensajeOpicsDetalle.TIPO_MENSAJE)){
			if (!valor.equals(MensajeOpicsDetalle.RENTA_FIJA) && !valor.equals(MensajeOpicsDetalle.OPERACION_CAMBIO)){
				throw new Exception("El tipo de operación no es el esperado");
			}
		}
		valores.put(clave,valor);
	}
	
	/**Sustituye todos los valores que contenga el HashMap a los valores del mensaje 
	 * siempre y cuando coincidan en su clave
	 * @param claveValor hashMap con el contenido de campos a sustituir*/
	public void setHashMap(HashMap<String,String> claveValor) throws Exception{
		Iterator<String> it = claveValor.keySet().iterator();
		String clave = "";
		String valor = "";
		while (it.hasNext()){
			clave = it.next();
			valor = claveValor.get(clave);
			this.set(clave, valor);
		}		
	}
	
	/**Convierte la fecha en el formato correcto a utilizar en los campos de opics
	 * @param fecha objeto fecha que se desea convertir
	 * @return String que contiene la fecha transformada en el formato correcto*/
	public String establecerFecha(Date fecha){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
		return sdf.format(fecha);
	}
	
	/**Devuelve el valor del campo
	 * @param clave clave o nombre del campo a buscar su valor
	 * @return el valor que contiene el campo o nulo si no existe*/
	public String get(String clave){
		return valores.get(clave);
	}
	
	/**Devuelve el detalle del mensaje*/
	public LinkedHashMap<String,String> getDetalle(){
		return this.valores;
	}
	
	/**Devuelve el id del mensaje. Identificador único del mensaje*/
	public long getIdOpics() {
		return idOpics;
	}

	/**Establece el id del mensaje.*/
	public void setIdOpics(long idMensaje) {
		this.idOpics = idMensaje;
	}
	
	/**Devuelve el número del mensaje*/
	public int getNumeroMensaje(){
		return this.numeroMensaje;
	}
	
	/**Setea el número del mensaje*/
	public void setNumeroMensaje(int numeroMensaje){
		this.numeroMensaje = numeroMensaje;
	}
	
	/**Sobreescribe el método toString*/
	public String toString(){
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = valores.keySet().iterator();
		String clave="";
		while (it.hasNext()){
			clave = it.next();
			sb.append(clave).append("->").append(valores.get(clave)).append("\n");			
		}
		return sb.toString();
	}	
}
