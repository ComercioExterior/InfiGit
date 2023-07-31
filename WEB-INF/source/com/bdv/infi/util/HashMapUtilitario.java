package com.bdv.infi.util;

import java.util.HashMap;

public class HashMapUtilitario<K,V> extends HashMap {

	/**
	 * Clase Utilitaria que hereda de HashMap que implementa o sobreescribe métodos usados en INFI
	 * @author nm25287 18/09/2014 TTS-466
	 */
	private static final long serialVersionUID = 1L;
	private String valorPorDefecto = "";
	
	
	public HashMapUtilitario(String mensajePorDefecto) {		
		super();
		valorPorDefecto = mensajePorDefecto;
	}


	/**
	 * Método put que coloca un valor por defecto cuando el value es null o vacío
	 * @param key
	 * @param value
	 */
	public void defaultPut (K key, V value){
		if(value!=null){
			if (value instanceof String) {
				if((((String)value).trim()).length()>0){
					super.put(key, value);
				}else{
					super.put(key, valorPorDefecto);
				}					
			}else{
				super.put(key, value);
			}
			
		}else{
			super.put(key, valorPorDefecto);
		}
	}

}
