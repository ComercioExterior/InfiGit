package com.bdv.infi.data;

public class IndicadoresDefinicion {
	
	private String indica_id;
	private String indica_descripcion;
	private String indica_in_requerido;
	private String indica_in_requisito;

	 
	public String getIndica_in_requisito() {
		return indica_in_requisito;
	}

	public void setIndica_in_requisito(String indica_in_requisito) {
		this.indica_in_requisito = indica_in_requisito;
	}

	/**
	 * Getter of the property <tt>indica_id</tt>
	 *
	 * @return Returns the indicadores.
	 * 
	 */
	public String getIndica_id()
	{
		return indica_id;
	}

	public String getIndica_descripcion()
	{
		return indica_descripcion;
	}
		

	public String getIndica_in_requerido() {
		return indica_in_requerido;
	}

	/**
	 * Setter of the property <tt>indica_id</tt>
	 *
	 * @param indica_id The indica_id to set.
	 *
	 */
	public void setIndica_id(String indica_id ){
		this.indica_id = indica_id;
	}

	public void setIndica_descripcion(String indica_descripcion ){
		this.indica_descripcion = indica_descripcion;
	}
	
	
	public void setIndica_in_requerido(String indica_in_requerido ){
		this.indica_in_requerido = indica_in_requerido;
	}	

}
