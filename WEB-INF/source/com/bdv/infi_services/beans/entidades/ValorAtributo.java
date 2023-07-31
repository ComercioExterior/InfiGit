package com.bdv.infi_services.beans.entidades;


/**
 * Lista de String que son parametros para la consulta de Unidades de Inversion
 * @author MegaSoft Computacion
 */
public class ValorAtributo {
	
	/**
	 * Valor del Atributo
	 */
	private String valorAtt;
	
	public ValorAtributo () {
	}
	
	public ValorAtributo (String valorAtt) {
		this.valorAtt = valorAtt;
	}
	
	/**
	 * Devuelve el valor del Atributo
	 * @return valorAtt
	 */
	public String getValorAtt() {
		return valorAtt;
	}
	/**
	 * Asigna el valor al Atributo
	 * @param valorAtt
	 */
	public void setValorAtt(String valorAtt) {
		this.valorAtt = valorAtt;
	}
}

