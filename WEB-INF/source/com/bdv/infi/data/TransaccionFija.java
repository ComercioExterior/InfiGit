package com.bdv.infi.data;

/**Clase para el manejo de las operaciones fijas en un determinado proceso*/
public class TransaccionFija {
	
	/**Id �nico de la transacci�n*/
	private int idTransaccion;
	
	/**Id de la transacci�n de negocio. TOMA_ORDEN,ADJUDICACION,LIQUIDACION,etc*/	
	private String idTransaccionNegocio = "";
	
	/**Nombre de la transacci�n que debe mostrarse*/
	private String nombreTransaccion = "";
	
	/**Descripci�n de la transacci�n*/
	private String descripcionTransaccion = "";
		
	/**C�digo de la operaci�n de d�bito para el cliente que debe enviarse a ALTAIR*/
	private String codigoOperacionCteDeb = "";

	/**C�digo de la operaci�n de cr�dito para el cliente que debe enviarse a ALTAIR*/
	private String codigoOperacionCteCre = "";
	
	/**C�digo de la operaci�n de bloqueo para el cliente  que debe enviarse a ALTAIR*/
	private String codigoOperacionCteBlo = "";
	
	/**C�digo de la operaci�n de d�bito para el veh�culo  que debe enviarse a ALTAIR*/
	private String codigoOperacionVehDeb = "";
	
	/**C�digo de la operaci�n de d�bito para el veh�culo  que debe enviarse a ALTAIR*/
	private String codigoOperacionVehCre = "";
	
	/**C�digo de la operaci�n fija que debe enviarse a ALTAIR*/
	private String codigoOperacionFija = "";	
	
	/**Tipo de la operaci�n fija*/
	private String tipoOperacionFija = "";		
		
	/**Vehiculo asociado a la transaccion (si aplica)*/
	private int idVehiculo;
	
	/**Instrumento financiero*/
	private int idInstrumentoFinanciero;
	
	
	public int getIdInstrumentoFinanciero() {
		return idInstrumentoFinanciero;
	}

	public void setIdInstrumentoFinanciero(int idInstrumentoFinanciero) {
		this.idInstrumentoFinanciero = idInstrumentoFinanciero;
	}

	public int getIdVehiculo() {
		return idVehiculo;
	}

	public void setIdVehiculo(int idVehiculo) {
		this.idVehiculo = idVehiculo;
	}

	public TransaccionFija(){		
	}
		
	/**Obtiene el id de la trnasacci�n*/
	public int getIdTransaccion() {
		return idTransaccion;
	}
	
	/**Setea el id de la transacci�n*/
	public void setIdTransaccion(int idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	
	/**Obtiene el id de la transacci�n de negocio*/
	public String getIdTransaccionNegocio() {
		return idTransaccionNegocio;
	}
	
	/**Setea el id de la transacci�n de negocio*/
	public void setIdTransaccionNegocio(String idTransaccionNegocio) {
		this.idTransaccionNegocio = idTransaccionNegocio;
	}
	
	/**Obtiene el nombre de la transacci�n*/
	public String getNombreTransaccion() {
		return nombreTransaccion;
	}
	
	/**Setea el nombre de la transacci�n*/
	public void setNombreTransaccion(String nombreTransaccion) {
		this.nombreTransaccion = nombreTransaccion;
	}
	
	/**Obtiene la descripci�n*/
	public String getDescripcionTransaccion() {
		return descripcionTransaccion;
	}
	
	/**Setea la descripci�n*/
	public void setDescripcionTransaccion(String descripcionTransaccion) {
		this.descripcionTransaccion = descripcionTransaccion;
	}

	public String getCodigoOperacionCteDeb() {
		return codigoOperacionCteDeb;
	}

	public void setCodigoOperacionCteDeb(String codigoOperacionCteDeb) {
		this.codigoOperacionCteDeb = codigoOperacionCteDeb;
	}

	public String getCodigoOperacionCteCre() {
		return codigoOperacionCteCre;
	}

	public void setCodigoOperacionCteCre(String codigoOperacionCteCre) {
		this.codigoOperacionCteCre = codigoOperacionCteCre;
	}

	public String getCodigoOperacionCteBlo() {
		return codigoOperacionCteBlo;
	}

	public void setCodigoOperacionCteBlo(String codigoOperacionCteBlo) {
		this.codigoOperacionCteBlo = codigoOperacionCteBlo;
	}

	public String getCodigoOperacionVehDeb() {
		return codigoOperacionVehDeb;
	}

	public void setCodigoOperacionVehDeb(String codigoOperacionVehDeb) {
		this.codigoOperacionVehDeb = codigoOperacionVehDeb;
	}

	public String getCodigoOperacionVehCre() {
		return codigoOperacionVehCre;
	}

	public void setCodigoOperacionVehCre(String codigoOperacionVehCre) {
		this.codigoOperacionVehCre = codigoOperacionVehCre;
	}

	public String getCodigoOperacionFija() {
		return codigoOperacionFija;
	}

	public void setCodigoOperacionFija(String codigoOperacionFija) {
		this.codigoOperacionFija = codigoOperacionFija;
	}

	public String getTipoOperacionFija() {
		return tipoOperacionFija;
	}

	public void setTipoOperacionFija(String tipoOperacionFija) {
		this.tipoOperacionFija = tipoOperacionFija;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("codigoOperacionCteBlo> ").append(this.codigoOperacionCteBlo).append("\n");
		sb.append("codigoOperacionCteCre> ").append(this.codigoOperacionCteCre).append("\n");
		sb.append("codigoOperacionCteDeb> ").append(this.codigoOperacionCteDeb).append("\n");
		sb.append("codigoOperacionFija> ").append(this.codigoOperacionFija).append("\n");
		sb.append("codigoOperacionVehCre> ").append(this.codigoOperacionVehCre).append("\n");
		sb.append("codigoOperacionVehDeb> ").append(this.codigoOperacionVehDeb).append("\n");
		return sb.toString();
	}
	
}
