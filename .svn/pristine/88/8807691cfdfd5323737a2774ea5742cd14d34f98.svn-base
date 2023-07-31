package com.bdv.infi.logic.interfaz_altair.transaction;


/**
 * Clase gen&eacute;rica para el manejo de las transacciones financieras hacia ALTAIR
 **/
public abstract class GenericaTransaccion {
	
	/**
	 * Numero de la Cuenta ser afectada por la operacion
	 */
	protected String numeroCuenta = "";
	/**
	 * Serial de la Cuenta Contable 
	 */
	protected String serialContable = "";
	/**
	 * Fecha de la aplicacion de la operacion
	 */
	protected String fechaAplicacion = "";
	/**
	 * Monto de la operacion
	 */
	protected String montoOperacion = "";
	/**
	 * Siglas que identifica la moneda usada
	 */
	protected String siglasMoneda = "";
	/**
	 * Código de la operacion contable 
	 */
	protected String codigoOperacion = "";
	/**
	 * Centro contable 
	 */
	protected String centroContable = "";
	
	/**Tipo de operación a ejecutar. Débito, Créito, Bloqueo, Desbloqueo*/
	protected String tipoOperacion = "";
	
	/**Número de retención aplicado a la operación de bloqueo. Es usado para desbloqueo y débito*/
	protected String numeroRetencion = "";
	
	/**Setea el número de movimiento*/
	protected String numeroMovimiento = "";	
	
	/**
	 * Asigna valor al Numero de la Cuenta ser afectada por la operacion
	 * @param numeroCuenta
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	
	/**
	 * Asigna valor al Serial de la Cuenta Contable
	 * @param serialContable
	 */
	public void setSerialContable(String serialContable) {
		this.serialContable = serialContable;
	}
	
	/**
	 * Asigna valor a la Fecha de la aplicacion de la operacion
	 * @param fechaAplicacion
	 */
	public void setFechaAplicacion(String fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}
	/**
	 * Asigna valor al Monto de la operacion
	 * @param montoOperacion
	 */
	public void setMontoOperacion(String montoOperacion) {
		this.montoOperacion = montoOperacion;
	}
	/**
	 * Asigna valor a la Siglas asignadas a la Moneda.
	 * @param siglasMoneda
	 */
	public void setSiglasMoneda(String siglasMoneda) {
		this.siglasMoneda = siglasMoneda;
	}
	
	/**
	 * Asigna valor de la operacion contable.
	 * @param codigoOperacion
	 */
	public void setCodigoOperacion(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}
	
	/**
	 * Asigna en centro contable.
	 * @param centroContable
	 */
	public void setCentroContable(String centroContable) {
		this.centroContable = centroContable;
	}	

	public Object execute() throws Exception {
		return null;
	}

	/**Asigna el tipo de operación que se debe ejecutar*/
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	
	/**Recupera el tipo de operación a ejecutar*/
	public String getTipoOperacion() {
		return this.tipoOperacion;
	}
	
	/**Setea el número de retención*/
	public void setNumeroRetencion(String numeroRetencion){
		this.numeroRetencion = numeroRetencion;
	}
	
	/**Retorna el número de retención*/
	public String getNumeroRetencion(){
		return this.numeroRetencion;
	}
	
	/**Setea el número de movimiento*/
	public void setNumeroMovimiento(String numeroMovimiento){
		this.numeroMovimiento = numeroMovimiento;
	}
	
	/**Retorna el número de movimiento*/
	public String getNumeroMovimiento(){
		return this.numeroMovimiento;
	}	

}