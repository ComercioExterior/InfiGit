package com.bdv.infi.data;

import java.util.HashMap;

/**Contiene los valores de los par&aacute;metros configurados en el sistema*/
public class Parametros {

	private HashMap parametros=new HashMap();
	
	/**M&eacute;todo que adiciona un par&aacute;metro a la lista
	 * @param campo nombre del parametro que se desea guardar
	 * @param valor valor del par&aacute;metro*/
	public void addValor(String campo, String valor){
		parametros.put(campo, valor);		
	}
	
	/**Regresa el valor de un campo
	 * @param campo clave a buscar
	 * @return valor del campo. Regresa null en caso de no existir el campo*/
	public String getValor(String campo){
		return (String)parametros.get(campo);
	}
}
