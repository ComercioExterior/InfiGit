package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;


/**
 * Clase usada para el manejo de las distintas operaciones financieras que deben
 * enviarse hacia un sistema espec�fico
 */
public class TOrdenOperacion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Indica la transaccion financiera que se debe aplicar. 
	 * Ejemplo cobro de comisi�n, pago de cupones, pago de amortizaci�n, etc.
	 */
	private String idTransaccionFinanciera;
	/**
	 * Descripcion de la transaccion financiera
	 */
	private String descrTransaccionFinanciera;
	/**
	 * Monto involucrado en la operaci�n financiera. 
	 * Indica cuanto se debe debitar, bloquear o acreditar
	 */
	private String monto = "0";
	/**
	 * Tasa aplicable para conseguir el monto de d�bito o cr�dito
	 */
	private String tasa = "0";
	/**
	 * Tipo de Operaci&oacute;n financiera
	 */
	private String tipoOperacion;
	
	/**
	 * Indicador de Comisi&oacute;n
	 */
	private String comision;
	/**
	 * Contructor de la clase
	 */
	public TOrdenOperacion() throws Exception {
		
	}

	/**
	 * Contructor de la clase
	 */
	public TOrdenOperacion(Object [] objValor) throws Exception {

		this.idTransaccionFinanciera = (String)objValor[0];	
		this.monto = (String)objValor[1];
		this.tasa = (String)objValor[2];	
		this.descrTransaccionFinanciera = (String)objValor[3];	
		this.tipoOperacion= (objValor[4]!=null?(String)objValor[4]:"");
		this.comision = objValor[5]!=null?(objValor[5].toString().equals("1")?"SI":"NO"):"NO";
	}
	
	/**
	 * Retorna el valor del Id de la Transacci�n Financiera 
	 * @return idTransaccionFinanciera
	 */
	public String getIdTransaccionFinanciera() {
		return idTransaccionFinanciera;
	}
	/**
	 * Asigna valor al Id de la Transacci�n Financiera
	 * @param idTransaccionFinanciera
	 */	
	public void setIdTransaccionFinanciera(String idTransaccionFinanciera) {
		this.idTransaccionFinanciera = idTransaccionFinanciera;
	}
	
	/**
	 * Retorna el valor del Monto involucrado en la operaci�n financiera
	 * @return monto
	 */
	public String getMonto() {
		return monto;
	}
	/**
	 * Asigna valor al Monto involucrado en la operaci�n financiera
	 * @param monto
	 */	
	public void setMonto(String monto) {
		this.monto = monto;
	}
	
	/**
	 * Retorna el valor de la Tasa aplicable para conseguir el monto de d�bito o cr�dito 
	 * @return tasa
	 */
	public String getTasa() {
		return tasa;
	}
	/**
	 * Asigna valor a la Tasa aplicable para conseguir el monto de d�bito o cr�dito
	 * @param tasa
	 */	
	public void setTasa(String tasa) {
		this.tasa = tasa;
	}

	/**
	 * @return the descrTransaccionFinanciera
	 */
	public String getDescrTransaccionFinanciera() {
		return descrTransaccionFinanciera;
	}

	/**
	 * @param descrTransaccionFinanciera the descrTransaccionFinanciera to set
	 */
	public void setDescrTransaccionFinanciera(String descrTransaccionFinanciera) {
		this.descrTransaccionFinanciera = descrTransaccionFinanciera;
	}

	/**
	 * @return the comision
	 */
	public String getComision() {
		return comision;
	}

	/**
	 * @param comision the comision to set
	 */
	public void setComision(String comision) {
		this.comision = comision;
	}

	/**
	 * @return the tipoOperacion
	 */
	public String getTipoOperacion() {
		return tipoOperacion;
	}

	/**
	 * @param tipoOperacion the tipoOperacion to set
	 */
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
}
