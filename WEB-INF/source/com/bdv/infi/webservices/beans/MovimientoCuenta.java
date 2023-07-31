package com.bdv.infi.webservices.beans;

import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * Representa un registro de movimiento de la cuenta, se utiliza para ver la
 * lista de detalles de movimientos de la cuenta.
 * <p>
 * Se meten en una lista de movimientos que tiene la clase Cuenta.
 * <p>
 * Para ver el xml que se utiliza para generar estos objetos:
 * WEB-INF/templates/ejemplos_xml_para_ws/detalle_movimientos_de_cuentas.xml
 * 
 * @author cet
 * 
 */
public class MovimientoCuenta {
	/**
	 * Fecha en la que ocurre el movimiento
	 */
	private Date fecha = null;
	/**
	 * número de referencia de la transacción de este movimiento
	 */
	private String referencia = "";
	/**
	 * Descripción textual de la transacción
	 */
	private String descripcion = "";
	/**
	 * Cantidad debitada. Si no fue un débito debe venir vacío
	 */
	private BigDecimal debito = null;
	/**
	 * Cantidad acreditada. Si no fue un crédito debe venir vacío.
	 */
	private BigDecimal credito = null;
	/**
	 * Saldo al momento de generarse el movimiento
	 */
	private BigDecimal saldoTotal = null;

	/**
	 * Tipo de movimiento
	 */
	private String tipoMovimiento = "";

	public BigDecimal getCredito() {
		return credito;
	}

	public void setCredito(BigDecimal credito) {
		this.credito = credito;
	}

	public BigDecimal getDebito() {
		return debito;
	}

	public void setDebito(BigDecimal debito) {
		this.debito = debito;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public BigDecimal getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(BigDecimal saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
}
