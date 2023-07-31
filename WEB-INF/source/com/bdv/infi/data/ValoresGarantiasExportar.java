/**
 * 
 */
package com.bdv.infi.data;

/**
 * @author bag
 * 
 */
public class ValoresGarantiasExportar {

	/**
	 * Cuenta del Cliente
	 */

	private String CuentaCliente;

	/**
	 * Nombre del Cliente
	 */

	private String ClienteNombre;

	/**
	 * Nombre del Titulo
	 */

	private String TituloDescripcion;

	/**
	 * Cantidad
	 */

	private int titcusCantidad;

	/**
	 * Valor
	 */

	private double tituloValorNominal;

	/**
	 * Benefeciario
	 */

	private String Beneficiario;

	private String tipoProductoId;

	public String getBeneficiario() {
		return Beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		Beneficiario = beneficiario;
	}

	public String getClienteNombre() {
		return ClienteNombre;
	}

	public void setClienteNombre(String clienteNombre) {
		ClienteNombre = clienteNombre;
	}

	public String getCuentaCliente() {
		return CuentaCliente;
	}

	public void setCuentaCliente(String cuentaCliente) {
		CuentaCliente = cuentaCliente;
	}

	public int getTitcusCantidad() {
		return titcusCantidad;
	}

	public void setTitcusCantidad(int titcusCantidad) {
		this.titcusCantidad = titcusCantidad;
	}

	public String getTituloDescripcion() {
		return TituloDescripcion;
	}

	public void setTituloDescripcion(String tituloDescripcion) {
		TituloDescripcion = tituloDescripcion;
	}

	public double getTituloValorNominal() {
		return tituloValorNominal;
	}

	public void setTituloValorNominal(double tituloValorNominal) {
		this.tituloValorNominal = tituloValorNominal;
	}

	public String getTipoProductoId() {
		return tipoProductoId;
	}

	public void setTipoProductoId(String tipoProductoId) {
		this.tipoProductoId = tipoProductoId;
	}

}
