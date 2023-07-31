package com.bdv.infi.logic;
/**
 * Clase que valida longitud de los campos por cada registro swift generado
 * elaucho
 */
public class ValidacionSwift {

	/*** Salto de Linea*/
	protected static final String SALTO_LINEA = "\n";
	
	/*** Salto de Linea*/
	protected static final String DOUBLEQUOTE = "//";
	
	/**
	 * Valida que una cadena dada sea de longitud 16
	 * @return
	 * @throws Exception
	 */
	public String validar16X(String cadena)throws Exception{

		if(cadena.length()>16)
		{
			cadena = cadena.substring(0,16);
		}
		
		//Retornamos la cadena validada		
		return cadena;	
		
	}//fin validar16x
	
	public String validarCodigoBIC12X(String cadena)throws Exception{
		int i=1;
		if(cadena.length()>16){
			cadena = cadena.substring(0,16);
		}
		if(cadena.length()<16){
			int longitud=12-cadena.length();
			for(i=1;i<=longitud;++i)				
			{
				cadena = cadena.concat("X");
			}
		}// fin del else	
		
		//Agrega X a la cadena para llevarla a 12X	
		return cadena;	
		
	}//fin validar12x

	
	public String validarCodigoBIC11X(String cadena)throws Exception{
		int i=1;
		if(cadena.length()>16){
			cadena = cadena.substring(0,16);
		}
		if(cadena.length()<16){
			int longitud=11-cadena.length();
			for(i=1;i<=longitud;++i)				
			{
				cadena = cadena.concat("X");
			}
		}// fin del else	
		
		//Agrega X a la cadena para llevarla a 11X	
		return cadena;	
		
	}//fin validar11x

	/**
	 * Valida que una cadena dada sea de longitud 4*35 (Max 4 lineas y cada una de 35 caracteres)
	 * @param String cadenaOriginal
	 * @throws Exception
	 */
	public String validar4x35X(String cadenaOriginal)throws Exception{
		
		StringBuffer cadenaValidada = new StringBuffer();
		String cadena = cadenaOriginal;
		
		
		if(cadena!=null)
		{
			if(cadena.length()>35)
			{
				cadena = cadenaOriginal.substring(0,35);
				cadenaValidada.append(cadena).append(SALTO_LINEA);
				
				//Se reinicializa la cadena a la cadena original
				cadena = cadenaOriginal;
				
				if(cadena.length()>70)
				{
					cadena = cadenaOriginal.substring(35,70);
					cadenaValidada.append(cadena).append(SALTO_LINEA);
					
					//Se reinicializa la cadena a la cadena original
					cadena = cadenaOriginal;
					
					if(cadena.length()>105)
					{
						cadena = cadenaOriginal.substring(70,105);
						cadenaValidada.append(cadena).append(SALTO_LINEA);
						
						//Se reinicializa la cadena a la cadena original
						cadena = cadenaOriginal;
						
						if(cadena.length()>140)
						{
							cadena = cadenaOriginal.substring(105,140);
							cadenaValidada.append(cadena);
						}else
						{
							cadena = cadenaOriginal.substring(105,cadenaOriginal.length());
							cadenaValidada.append(cadena);
						}
						
					}else					
					{
						cadena = cadenaOriginal.substring(70,cadenaOriginal.length());
						cadenaValidada.append(cadena);		
					}				
				}else				
				{
					cadena = cadenaOriginal.substring(35,cadenaOriginal.length());
					cadenaValidada.append(cadena);
				}
			}else{			
				cadena = cadenaOriginal.substring(0,cadenaOriginal.length());
				cadenaValidada.append(cadena);
			}
		}
		
		//Retornamos la cadena validada	
		return cadenaValidada.toString();
	}//fin validar35X
	
	/**
	 * Valida que una cadena dada sea de longitud 34X incluyendo el slash (/)
	 * @param String cadena
	 * @return
	 * @throws Exception
	 */
	public String validar34X(String cadena)throws Exception{
		
		if(cadena.length()>34)
		{
			cadena = cadena.substring(0,34);
		}
		
		return cadena;		
	}//fin validar34X
	
	/**
	 * Valida que el registro no exceda de 6 lineas y cada una con un maximo de 35 caracteres
	 * @param cadenaOriginal
	 * @return
	 * @throws Exception
	 */
	public String validar6x35X(String cadenaOriginal)throws Exception{

		StringBuffer cadenaValidada = new StringBuffer();
		String cadena = cadenaOriginal;
		
		if(cadena.length()>35)
		{
			cadena = cadenaOriginal.substring(0,35);
			cadenaValidada.append(cadena).append(SALTO_LINEA);
			
			//Se reinicializa la cadena a la cadena original
			cadena = cadenaOriginal;
			
			if(cadena.length()>70)
			{
				cadena = cadenaOriginal.substring(35,70);
				cadenaValidada.append(DOUBLEQUOTE).append(cadena).append(SALTO_LINEA);
				
				//Se reinicializa la cadena a la cadena original
				cadena = cadenaOriginal;
				
				if(cadena.length()>105)
				{
					cadena = cadenaOriginal.substring(70,105);
					cadenaValidada.append(DOUBLEQUOTE).append(cadena).append(SALTO_LINEA);
					
					//Se reinicializa la cadena a la cadena original
					cadena = cadenaOriginal;
					
					if(cadena.length()>140)
					{
						cadena = cadenaOriginal.substring(105,140);
						cadenaValidada.append(DOUBLEQUOTE).append(cadena);
					}else
					{
						cadena = cadenaOriginal.substring(105,cadenaOriginal.length());
						cadenaValidada.append(DOUBLEQUOTE).append(cadena);
					}
					
				}else					
				{
					cadena = cadenaOriginal.substring(70,cadenaOriginal.length());
					cadenaValidada.append(DOUBLEQUOTE).append(cadena);		
				}				
			}else				
			{
				cadena = cadenaOriginal.substring(35,cadenaOriginal.length());
				cadenaValidada.append(DOUBLEQUOTE).append(cadena);
			}
		}else{			
			cadena = cadenaOriginal.substring(0,cadenaOriginal.length());
			cadenaValidada.append(cadena);
		}
		//Retornamos la cadena validada	
		return cadenaValidada.toString();
	
	}
	
	/**
	 * Valida si la cadena es menor o igual a 35,para concatenar mas información
	 * @param cadena
	 * @return
	 * @throws Exception
	 */
	public String validar35x(String cadena)throws Exception{
		
		StringBuffer sb = new StringBuffer();
		
		if(cadena.length()<=35)
		{
			sb.append(cadena).append(SALTO_LINEA);
			if (cadena.length()==34){
			   sb.append(DOUBLEQUOTE).append(cadena.substring(0,33));
			} else {
				sb.append(DOUBLEQUOTE).append(cadena);
			}
		}
		else{
			sb.append(cadena);
		}
			
		return sb.toString();
	}
}
