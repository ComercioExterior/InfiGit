package com.bdv.infi.webservices.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 * Representa una cuenta que tiene el cliente en el banco. Contiene los datos de
 * una cuenta tal como se muestran en la pantalla integral de call center. El
 * proposito de estos objetos es el de pasar la lista de cuentas que tiene un
 * cliente hacia la capa de presentacion.
 * <p>
 * Tambien contiene los movimientos de la cuenta, en el caso de que la consulta
 * sea de movimientos de la cuenta.
 * <p>
 * Los objetos de esta clase se crean con jibx a partir de un xml, y estan
 * contenidos en un objeto de clase ListaDeCuentas, metidos en un ArrayList que
 * tiene ListaDeCuentas.
 * <p>
 * Para ver el xml que se utiliza para generar estos objetos:
 * WEB-INF/templates/ejemplos_xml_para_ws/lista_de_cuentas.xml
 *
 * @author Camilo Torres
 *
 */
public class Cuenta {
	private String activo;
	/**
	 * Fecha en que se inicia la cuenta
	 */
	private Date fechaDeInicio;

	/**
	 * Numero de la cuenta
	 */
	private String numero = "";

	/**
	 * Tipo de cuenta o nombre del producto
	 */
	private String tipoDeCuenta = "";

	/**
	 * fecha a la que se guardo el saldo. puede contener el texto EN LINEA si el
	 * saldo se calcula en linea
	 */
	private Date fechaDeSaldo;

	/**
	 * Indica si el saldo es en linea (traido del host) o diferido (de D-1)
	 */
	private boolean esSaldoEnLinea = false;

	/**
	 * Saldo total de la cuetna
	 */
	private BigDecimal saldoTotal;

	/**
	 * Saldo disponible de la cuenta
	 */
	private BigDecimal saldoDisponible;

	/**
	 * saldo bloqueado en la cuenta
	 */
	private BigDecimal saldoBloqueado;

	/**
	 * saldo diferido en la cuenta
	 */
	private BigDecimal saldoDiferido;
	/**
	 * limite de credito de la cuenta
	 */
	private BigDecimal limiteDeCredito;

	/**
	 * fecha a la que se calcula el saldo promedio. puede contener el texto EN
	 * LINEA si el calculo se realiza en linea
	 */
	private Date fechaDeSaldoPromedio;

	/**
	 * cantidad del saldo promedio de la cuenta
	 */
	private BigDecimal saldoPromedio;

	/**
	 * monto del IDB consultado
	 */
	private BigDecimal totalIdb;

	/**
	 * cantidad de registros consultados que corresponden a IDB
	 */
	private long cantidadRegistrosIdb;
	
	private String codProducto;
	
	private String codSubProducto;

	/**
	 * Movimientos de la cuenta
	 */
	private ArrayList<MovimientoCuenta> movimientos;

	public String error;

	public long getCantidadRegistrosIdb() {
		return cantidadRegistrosIdb;
	}

	public void setCantidadRegistrosIdb(long cantidadRegistrosIdb) {
		this.cantidadRegistrosIdb = cantidadRegistrosIdb;
	}

	public boolean isEsSaldoEnLinea() {
		return esSaldoEnLinea;
	}

	public void setEsSaldoEnLinea(boolean esSaldoEnLinea) {
		this.esSaldoEnLinea = esSaldoEnLinea;
	}

	public Date getFechaDeInicio() {
		return fechaDeInicio;
	}

	public void setFechaDeInicio(Date fechaDeInicio) {
		this.fechaDeInicio = fechaDeInicio;
	}

	public Date getFechaDeSaldo() {
		return fechaDeSaldo;
	}

	public void setFechaDeSaldo(Date fechaDeSaldo) {
		this.fechaDeSaldo = fechaDeSaldo;
	}

	public Date getFechaDeSaldoPromedio() {
		return fechaDeSaldoPromedio;
	}

	public void setFechaDeSaldoPromedio(Date fechaDeSaldoPromedio) {
		this.fechaDeSaldoPromedio = fechaDeSaldoPromedio;
	}

	public BigDecimal getLimiteDeCredito() {
		return limiteDeCredito;
	}

	public void setLimiteDeCredito(BigDecimal limiteDeCredito) {
		this.limiteDeCredito = limiteDeCredito;
	}

	public ArrayList<MovimientoCuenta> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(ArrayList<MovimientoCuenta> movimientos) {
		this.movimientos = movimientos;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getSaldoBloqueado() {
		return saldoBloqueado;
	}

	public void setSaldoBloqueado(BigDecimal saldoBloqueado) {
		this.saldoBloqueado = saldoBloqueado;
	}

	public BigDecimal getSaldoDiferido() {
		return saldoDiferido;
	}

	public void setSaldoDiferido(BigDecimal saldoDiferido) {
		this.saldoDiferido = saldoDiferido;
	}

	public BigDecimal getSaldoDisponible() {
		return saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	public BigDecimal getSaldoPromedio() {
		return saldoPromedio;
	}

	public void setSaldoPromedio(BigDecimal saldoPromedio) {
		this.saldoPromedio = saldoPromedio;
	}

	public BigDecimal getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(BigDecimal saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public String getTipoDeCuenta() {
		return tipoDeCuenta;
	}

	public void setTipoDeCuenta(String tipoDeCuenta) {
		this.tipoDeCuenta = tipoDeCuenta;
	}

	public BigDecimal getTotalIdb() {
		return totalIdb;
	}

	public void setTotalIdb(BigDecimal totalIdb) {
		this.totalIdb = totalIdb;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	public String toString(){
		return numero + " " + activo + " "  +  tipoDeCuenta;
	}

	public String getCodProducto() {
		return codProducto;
	}

	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCodSubProducto() {
		return codSubProducto;
	}

	public void setCodSubProducto(String codSubProducto) {
		this.codSubProducto = codSubProducto;
	}

}
