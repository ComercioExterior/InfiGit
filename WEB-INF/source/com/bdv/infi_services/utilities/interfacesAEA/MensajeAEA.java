package com.bdv.infi_services.utilities.interfacesAEA;

import java.util.HashMap;

/**
 * Un mensaje de los qeu responde AEA. Corresponde a un unico "@DCXXXXX" y sus
 * datos.
 * 
 * @author CT04502
 * 
 */
public class MensajeAEA {
	/**
	 * Se refiere al formato "@DCXXXXX"
	 */
	public String formato = null;

	/**
	 * String con el formato original
	 */
	protected String stringOriginal = null;

	/**
	 * mapa para interpretar el mensaje
	 */
	protected HashMap<String, HashMap<String, int[]>> mapaDeSalida;

	/**
	 * Crea un mensaje a partir de un string. El string corresponde a un formato
	 * de salida de AEA
	 * 
	 * @param stringOriginal
	 *            el string de aea
	 * @param mapaDeSalida
	 *            el mapa para interpretar correctamente el mensaje
	 */
	public MensajeAEA(String stringOriginal,
			HashMap<String, HashMap<String, int[]>> mapaDeSalida) {
		
		this.stringOriginal = stringOriginal.substring(9);
		this.mapaDeSalida = mapaDeSalida;
		this.formato = stringOriginal.substring(0, 8).trim();
	}

	/**
	 * Retorna el valor del campo solicitado, segun el mensaje actual
	 * 
	 * @param campo
	 *            numero del campo: "01", "20", etc
	 * @return el valor del campo
	 */
	public String obtenerCampo(String campo) {
		String resultado = null;

		try {
			// Obtiene el inicio y final del campo para realizar el SubString
			int inicio = this.mapaDeSalida.get(this.formato).get(campo)[0];
			int longitud = this.mapaDeSalida.get(this.formato).get(campo)[1];

			// Realiza el substring del campo, sobre la variable
			// respuesta_actual
			resultado = this.stringOriginal
					.substring(inicio, inicio + longitud).trim();
		} catch (Exception e) {
	//		e.printStackTrace();
			// retorna el resultado vacio
			resultado = "";
		}

		return resultado;
	}

	/**
	 * Retorna el valor del campo numerico solicitado. los campos que vienen del
	 * host, cuando son numericos, tienen un caracter al final del campo que
	 * indica dos cosas, una parte del caracter en realidad es la parte final
	 * del monto. la otra parte del caracter se usa pasa saber el signo de todo
	 * el campo numerico. este metodo transforma esos campos para retornar un
	 * string con el valor numerico correcto. utiliza un BigDecimal para crear
	 * un numero a partir del string que yo retorno.
	 * 
	 * @param campo
	 *            numero del campo en AEA para la transaccion
	 * @return el campo numerico con el signo y el numero final
	 */
	public String obtenerCampoNumerico(String campo) {
		String campoNumericoResultante = "";
		String campoConCaracteres = obtenerCampo(campo);
		char ultimoCaracterDelCampo = campoConCaracteres.toUpperCase().charAt(
				campoConCaracteres.length() - 1);
		String ultimoDigitoReal = "";
		if ((ultimoCaracterDelCampo >= 'A' && ultimoCaracterDelCampo <= 'I')
				|| ultimoCaracterDelCampo == '{') {
			switch (ultimoCaracterDelCampo) {
			case 'A':
				ultimoDigitoReal = "1";
				break;
			case 'B':
				ultimoDigitoReal = "2";
				break;
			case 'C':
				ultimoDigitoReal = "3";
				break;
			case 'D':
				ultimoDigitoReal = "4";
				break;
			case 'E':
				ultimoDigitoReal = "5";
				break;
			case 'F':
				ultimoDigitoReal = "6";
				break;
			case 'G':
				ultimoDigitoReal = "7";
				break;
			case 'H':
				ultimoDigitoReal = "8";
				break;
			case 'I':
				ultimoDigitoReal = "9";
				break;
			case '{':
				ultimoDigitoReal = "0";
				break;
			}
			campoNumericoResultante = campoConCaracteres.substring(0,
					campoConCaracteres.length() - 1)
					+ ultimoDigitoReal;
		} else {
			switch (ultimoCaracterDelCampo) {
			case 'J':
				ultimoDigitoReal = "1";
				break;
			case 'K':
				ultimoDigitoReal = "2";
				break;
			case 'L':
				ultimoDigitoReal = "3";
				break;
			case 'M':
				ultimoDigitoReal = "4";
				break;
			case 'N':
				ultimoDigitoReal = "5";
				break;
			case 'O':
				ultimoDigitoReal = "6";
				break;
			case 'P':
				ultimoDigitoReal = "7";
				break;
			case 'Q':
				ultimoDigitoReal = "8";
				break;
			case 'R':
				ultimoDigitoReal = "9";
				break;
			case '}':
				ultimoDigitoReal = "0";
				break;
			}
			campoNumericoResultante = "-"
					+ campoConCaracteres.substring(0, campoConCaracteres
							.length() - 1) + ultimoDigitoReal;
		}
		if (campoNumericoResultante.trim().equals("")) {
			campoNumericoResultante = "0";
		}
		return campoNumericoResultante;
	}
}
