package com.bdv.infi.data;

/**Clase para el manejo de las operaciones fijas en un determinado proceso*/
public class TransaccionFija {
	
	/**Id único de la transacción*/
	private int idTransaccion;
	
	/**Id de la transacción de negocio. TOMA_ORDEN,ADJUDICACION,LIQUIDACION,etc*/	
	private String idTransaccionNegocio = "";
	
	/**Nombre de la transacción que debe mostrarse*/
	private String nombreTransaccion = "";
	
	/**Descripción de la transacción*/
	private String descripcionTransaccion = "";
		
	/**Código de la operación de débito para el cliente que debe enviarse a ALTAIR*/
	private String codigoOperacionCteDeb = "";

	/**Código de la operación de crédito para el cliente que debe enviarse a ALTAIR*/
	private String codigoOperacionCteCre = "";
	
	/**Código de la operación de bloqueo para el cliente  que debe enviarse a ALTAIR*/
	private String codigoOperacionCteBlo = "";
	
	/**Código de la operación de débito para el vehículo  que debe enviarse a ALTAIR*/
	private String codigoOperacionVehDeb = "";
	
	/**Código de la operación de débito para el vehículo  que debe enviarse a ALTAIR*/
	private String codigoOperacionVehCre = "";
	
	/**Código de la operación fija que debe enviarse a ALTAIR*/
	private String codigoOperacionFija = "";	
	
	/**Tipo de la operación fija*/
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
		
	/**Obtiene el id de la trnasacción*/
	public int getIdTransaccion() {
		return idTransaccion;
	}
	
	/**Setea el id de la transacción*/
	public void setIdTransaccion(int idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	
	/**Obtiene el id de la transacción de negocio*/
	public String getIdTransaccionNegocio() {
		return idTransaccionNegocio;
	}
	
	/**Setea el id de la transacción de negocio*/
	public void setIdTransaccionNegocio(String idTransaccionNegocio) {
		this.idTransaccionNegocio = idTransaccionNegocio;
	}
	
	/**Obtiene el nombre de la transacción*/
	public String getNombreTransaccion() {
		return nombreTransaccion;
	}
	
	/**Setea el nombre de la transacción*/
	public void setNombreTransaccion(String nombreTransaccion) {
		this.nombreTransaccion = nombreTransaccion;
	}
	
	/**Obtiene la descripción*/
	public String getDescripcionTransaccion() {
		return descripcionTransaccion;
	}
	
	/**Setea la descripción*/
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
