package com.bdv.infi.webservices.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;


/**
 * <p>
 * Representa un credito que tiene el cliente en el banco. Contiene los datos
 * del credito tal como se muestran en la pantalla integral de call center. El
 * proposito de estos objetos es el de pasar la lista de creditos que tiene un
 * cliente hacia la capa de presentacion.
 * <p>
 * Los objetos de esta clase se crean con jibx a partir de un xml, y estan
 * contenidos en un objeto de clase ListaDeCreditos, metidos en un ArrayList.
 *
 * @author Camilo Torres
 *
 */
public class Credito {
	private String activo;
	/**
	 * Fecha en que se crea el credito
	 */
	private Date fechaDeInicio;
	private Date fecha;

	/**
	 * Numero del credito
	 */
	private String numero = "";

	/**
	 * Fecha del saldo: EN LINEA o una fecha dd/MM/yyyy
	 */
	private Date fechaDeConsulta;

	/**
	 * Indica si el saldo es en linea (traido del host) o diferido (de D-1)
	 */
	private boolean esSaldoEnLinea = false;

	/**
	 * Tipo o nombre del credito
	 */
	private String tipoDeCredito = "";

	/**
	 * Saldo deudor del credito
	 */
	private BigDecimal saldoDeudor;

	private Date fechaResumenDeuda;
	private BigDecimal pendientePorFacturar;
	private BigDecimal pendientePorPago;
	private BigDecimal otrosGastos;
	private BigDecimal deudaTotal;
	private BigDecimal comisionCancel;
	private BigDecimal cancelTotal;
	private BigDecimal cancelCuota;

	private Date fechaEstadoDeCuenta;
	private String plazoActual;
	private Date ultimoPagoACapital;
	private BigDecimal capitalAprobado;
	private BigDecimal saldoDeudorCapital;
	private Date fechaDeLiquidacion;
	private Date fechaDeVencimiento;
	private BigDecimal tasaDeInteres;
	private String estatus;

	private BigDecimal capitalPendietePorFacturar;
	private BigDecimal interesOrdinarioPendientePorFacturar;
	private BigDecimal seguroPendientePorFacturar;
	private BigDecimal gastosPendientesPorFacturar;
	private BigDecimal comisionesPendientesPorFacturar;
	private BigDecimal impuestosPendientesPorFacturar;

	private BigDecimal capitalPendienteDePago;
	private BigDecimal interesOrdinarioPendienteDePago;
	private BigDecimal seguroPendienteDePago;
	private BigDecimal gastosPendienteDePago;
	private BigDecimal comisionesPendienteDePago;
	private BigDecimal impuestosPendienteDePago;
	private BigDecimal comiCobExterPendienteDePago;
	private BigDecimal moraPendienteDePago;
	private BigDecimal interesesTranscurridosPendienteDePago;

	private Date fechaPendientePorVencer;
	private BigDecimal capitalPendientePorVencer;
	private BigDecimal interesOrdinarioPendientePorVencer;
	private BigDecimal seguroPendientePorVencer;

	private String cuotaDetalleDeuda;
	private BigDecimal moraPendienteDetalleDeuda;

	private ArrayList<DatoDeAmortizacionDeCredito> tablaDeAmortizacion;

	public String error;

	/**
	 * Usuario de la aplicacion
	 */
	private CredencialesDeUsuario credenciales;

	public BigDecimal getCancelCuota() {
		return cancelCuota;
	}

	public void setCancelCuota(BigDecimal cancelCuota) {
		this.cancelCuota = cancelCuota;
	}

	public BigDecimal getCancelTotal() {
		return cancelTotal;
	}

	public void setCancelTotal(BigDecimal cancelTotal) {
		this.cancelTotal = cancelTotal;
	}

	public BigDecimal getCapitalAprobado() {
		return capitalAprobado;
	}

	public void setCapitalAprobado(BigDecimal capitalAprobado) {
		this.capitalAprobado = capitalAprobado;
	}

	public BigDecimal getCapitalPendienteDePago() {
		return capitalPendienteDePago;
	}

	public void setCapitalPendienteDePago(BigDecimal capitalPendienteDePago) {
		this.capitalPendienteDePago = capitalPendienteDePago;
	}

	public BigDecimal getCapitalPendientePorVencer() {
		return capitalPendientePorVencer;
	}

	public void setCapitalPendientePorVencer(BigDecimal capitalPendientePorVencer) {
		this.capitalPendientePorVencer = capitalPendientePorVencer;
	}

	public BigDecimal getCapitalPendietePorFacturar() {
		return capitalPendietePorFacturar;
	}

	public void setCapitalPendietePorFacturar(BigDecimal capitalPendietePorFacturar) {
		this.capitalPendietePorFacturar = capitalPendietePorFacturar;
	}

	public BigDecimal getComiCobExterPendienteDePago() {
		return comiCobExterPendienteDePago;
	}

	public void setComiCobExterPendienteDePago(
			BigDecimal comiCobExterPendienteDePago) {
		this.comiCobExterPendienteDePago = comiCobExterPendienteDePago;
	}

	public BigDecimal getComisionCancel() {
		return comisionCancel;
	}

	public void setComisionCancel(BigDecimal comisionCancel) {
		this.comisionCancel = comisionCancel;
	}

	public BigDecimal getComisionesPendienteDePago() {
		return comisionesPendienteDePago;
	}

	public void setComisionesPendienteDePago(BigDecimal comisionesPendienteDePago) {
		this.comisionesPendienteDePago = comisionesPendienteDePago;
	}

	public BigDecimal getComisionesPendientesPorFacturar() {
		return comisionesPendientesPorFacturar;
	}

	public void setComisionesPendientesPorFacturar(
			BigDecimal comisionesPendientesPorFacturar) {
		this.comisionesPendientesPorFacturar = comisionesPendientesPorFacturar;
	}

	public CredencialesDeUsuario getCredenciales() {
		return credenciales;
	}

	public void setCredenciales(CredencialesDeUsuario credenciales) {
		this.credenciales = credenciales;
	}

	public String getCuotaDetalleDeuda() {
		return cuotaDetalleDeuda;
	}

	public void setCuotaDetalleDeuda(String cuotaDetalleDeuda) {
		this.cuotaDetalleDeuda = cuotaDetalleDeuda;
	}

	public BigDecimal getDeudaTotal() {
		return deudaTotal;
	}

	public void setDeudaTotal(BigDecimal deudaTotal) {
		this.deudaTotal = deudaTotal;
	}

	public boolean isEsSaldoEnLinea() {
		return esSaldoEnLinea;
	}

	public void setEsSaldoEnLinea(boolean esSaldoEnLinea) {
		this.esSaldoEnLinea = esSaldoEnLinea;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaDeConsulta() {
		return fechaDeConsulta;
	}

	public void setFechaDeConsulta(Date fechaDeConsulta) {
		this.fechaDeConsulta = fechaDeConsulta;
	}

	public Date getFechaDeInicio() {
		return fechaDeInicio;
	}

	public void setFechaDeInicio(Date fechaDeInicio) {
		this.fechaDeInicio = fechaDeInicio;
	}

	public Date getFechaDeLiquidacion() {
		return fechaDeLiquidacion;
	}

	public void setFechaDeLiquidacion(Date fechaDeLiquidacion) {
		this.fechaDeLiquidacion = fechaDeLiquidacion;
	}

	public Date getFechaDeVencimiento() {
		return fechaDeVencimiento;
	}

	public void setFechaDeVencimiento(Date fechaDeVencimiento) {
		this.fechaDeVencimiento = fechaDeVencimiento;
	}

	public Date getFechaEstadoDeCuenta() {
		return fechaEstadoDeCuenta;
	}

	public void setFechaEstadoDeCuenta(Date fechaEstadoDeCuenta) {
		this.fechaEstadoDeCuenta = fechaEstadoDeCuenta;
	}

	public Date getFechaPendientePorVencer() {
		return fechaPendientePorVencer;
	}

	public void setFechaPendientePorVencer(Date fechaPendientePorVencer) {
		this.fechaPendientePorVencer = fechaPendientePorVencer;
	}

	public Date getFechaResumenDeuda() {
		return fechaResumenDeuda;
	}

	public void setFechaResumenDeuda(Date fechaResumenDeuda) {
		this.fechaResumenDeuda = fechaResumenDeuda;
	}

	public BigDecimal getGastosPendienteDePago() {
		return gastosPendienteDePago;
	}

	public void setGastosPendienteDePago(BigDecimal gastosPendienteDePago) {
		this.gastosPendienteDePago = gastosPendienteDePago;
	}

	public BigDecimal getGastosPendientesPorFacturar() {
		return gastosPendientesPorFacturar;
	}

	public void setGastosPendientesPorFacturar(
			BigDecimal gastosPendientesPorFacturar) {
		this.gastosPendientesPorFacturar = gastosPendientesPorFacturar;
	}

	public BigDecimal getImpuestosPendienteDePago() {
		return impuestosPendienteDePago;
	}

	public void setImpuestosPendienteDePago(BigDecimal impuestosPendienteDePago) {
		this.impuestosPendienteDePago = impuestosPendienteDePago;
	}

	public BigDecimal getImpuestosPendientesPorFacturar() {
		return impuestosPendientesPorFacturar;
	}

	public void setImpuestosPendientesPorFacturar(
			BigDecimal impuestosPendientesPorFacturar) {
		this.impuestosPendientesPorFacturar = impuestosPendientesPorFacturar;
	}

	public BigDecimal getInteresesTranscurridosPendienteDePago() {
		return interesesTranscurridosPendienteDePago;
	}

	public void setInteresesTranscurridosPendienteDePago(
			BigDecimal interesesTranscurridosPendienteDePago) {
		this.interesesTranscurridosPendienteDePago = interesesTranscurridosPendienteDePago;
	}

	public BigDecimal getInteresOrdinarioPendienteDePago() {
		return interesOrdinarioPendienteDePago;
	}

	public void setInteresOrdinarioPendienteDePago(
			BigDecimal interesOrdinarioPendienteDePago) {
		this.interesOrdinarioPendienteDePago = interesOrdinarioPendienteDePago;
	}

	public BigDecimal getInteresOrdinarioPendientePorFacturar() {
		return interesOrdinarioPendientePorFacturar;
	}

	public void setInteresOrdinarioPendientePorFacturar(
			BigDecimal interesOrdinarioPendientePorFacturar) {
		this.interesOrdinarioPendientePorFacturar = interesOrdinarioPendientePorFacturar;
	}

	public BigDecimal getInteresOrdinarioPendientePorVencer() {
		return interesOrdinarioPendientePorVencer;
	}

	public void setInteresOrdinarioPendientePorVencer(
			BigDecimal interesOrdinarioPendientePorVencer) {
		this.interesOrdinarioPendientePorVencer = interesOrdinarioPendientePorVencer;
	}

	public BigDecimal getMoraPendienteDePago() {
		return moraPendienteDePago;
	}

	public void setMoraPendienteDePago(BigDecimal moraPendienteDePago) {
		this.moraPendienteDePago = moraPendienteDePago;
	}

	public BigDecimal getMoraPendienteDetalleDeuda() {
		return moraPendienteDetalleDeuda;
	}

	public void setMoraPendienteDetalleDeuda(BigDecimal moraPendienteDetalleDeuda) {
		this.moraPendienteDetalleDeuda = moraPendienteDetalleDeuda;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getOtrosGastos() {
		return otrosGastos;
	}

	public void setOtrosGastos(BigDecimal otrosGastos) {
		this.otrosGastos = otrosGastos;
	}

	public BigDecimal getPendientePorFacturar() {
		return pendientePorFacturar;
	}

	public void setPendientePorFacturar(BigDecimal pendientePorFacturar) {
		this.pendientePorFacturar = pendientePorFacturar;
	}

	public BigDecimal getPendientePorPago() {
		return pendientePorPago;
	}

	public void setPendientePorPago(BigDecimal pendientePorPago) {
		this.pendientePorPago = pendientePorPago;
	}

	public String getPlazoActual() {
		return plazoActual;
	}

	public void setPlazoActual(String plazoActual) {
		this.plazoActual = plazoActual;
	}

	public BigDecimal getSaldoDeudor() {
		return saldoDeudor;
	}

	public void setSaldoDeudor(BigDecimal saldoDeudor) {
		this.saldoDeudor = saldoDeudor;
	}

	public BigDecimal getSaldoDeudorCapital() {
		return saldoDeudorCapital;
	}

	public void setSaldoDeudorCapital(BigDecimal saldoDeudorCapital) {
		this.saldoDeudorCapital = saldoDeudorCapital;
	}

	public BigDecimal getSeguroPendienteDePago() {
		return seguroPendienteDePago;
	}

	public void setSeguroPendienteDePago(BigDecimal seguroPendienteDePago) {
		this.seguroPendienteDePago = seguroPendienteDePago;
	}

	public BigDecimal getSeguroPendientePorFacturar() {
		return seguroPendientePorFacturar;
	}

	public void setSeguroPendientePorFacturar(BigDecimal seguroPendientePorFacturar) {
		this.seguroPendientePorFacturar = seguroPendientePorFacturar;
	}

	public BigDecimal getSeguroPendientePorVencer() {
		return seguroPendientePorVencer;
	}

	public void setSeguroPendientePorVencer(BigDecimal seguroPendientePorVencer) {
		this.seguroPendientePorVencer = seguroPendientePorVencer;
	}

	public ArrayList<DatoDeAmortizacionDeCredito> getTablaDeAmortizacion() {
		return tablaDeAmortizacion;
	}

	public void setTablaDeAmortizacion(
			ArrayList<DatoDeAmortizacionDeCredito> tablaDeAmortizacion) {
		this.tablaDeAmortizacion = tablaDeAmortizacion;
	}

	public BigDecimal getTasaDeInteres() {
		return tasaDeInteres;
	}

	public void setTasaDeInteres(BigDecimal tasaDeInteres) {
		this.tasaDeInteres = tasaDeInteres;
	}

	public String getTipoDeCredito() {
		return tipoDeCredito;
	}

	public void setTipoDeCredito(String tipoDeCredito) {
		this.tipoDeCredito = tipoDeCredito;
	}

	public Date getUltimoPagoACapital() {
		return ultimoPagoACapital;
	}

	public void setUltimoPagoACapital(Date ultimoPagoACapital) {
		this.ultimoPagoACapital = ultimoPagoACapital;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}
