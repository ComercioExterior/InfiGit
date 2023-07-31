package com.bdv.infi.data;

import java.util.Date;

/** 
 * Clase usada para el manejo de las distintas operaciones financieras que deben enviarse hacia un sistema espec&iacute;fico
 */
public class Operacion {

/**Id de la moneda en la que est&aacute; la operaci&oacute;n financiera*/
private String idMoneda="";
	
/** id de titulo*/
private String tituloid="";
/**Fecha en que se envio la transacci&oacute;n financiera*/
private Date fechaOperacion;

/**Fecha en que se aplic&oacute; la transacci&oacute;n financiera*/
private Date fechaAplicacion;	

/**Indica el serial contable de la transacci&oacute;n financiera*/
private String serialContable="";

/**Si hay alg&uacute;n error aplicando la transacci&oacute;n aqui se muestra su descripci&oacute;n*/
private String error="";

/**Indicador de comisi&oacute;n */
private int inComision;

/**
 * Indica la transaccion financiera que se debe aplicar. 
 * Ejemplo cobro de comisi&oacute;n, pago de cupones, pago de amortizaci&oacute;n, etc.
 */
private String idTransaccionFinanciera="";

/**Descripci&oacute;n de la transacci&oacute;n financiera*/
private String descripcionTransaccion = "";	
 
	/**
 * Informaci&oacute;n de la tasa aplicable para conseguir el monto de d&eacute;bito o cr&eacute;dito
 */
private double tasa=0;

/**Id de la operaci&oacute;n generada*/
private long idOperacion;

/**
 * Indica si a la operaci&oacute;n se le puede aplicar un reverso, es decir, si es un d&eacute;bito y la opci&oacute;n es si, se le puede aplicar un cr&eacute;dito
 */
private boolean aplicaReverso=false;


/**Indica la cuenta para depositar, acreditar, bloquear, o desbloquear*/
private String tipoTransaccion="";

private String nombreReferenciaCuenta="";

/**Nombre del Banco donde se posee la Cuenta.*/
private String nombreBanco="";

/**Direccion del Banco donde se posee la Cuenta.*/
private String direccionBanco="";

/**C&oacute;digo SWIFT del Banco donde se posee la Cuenta.*/
private String codigoSwiftBanco="";

/**C&oacute;digo BIC del Banco donde se posee la Cuenta.*/
private String codigoBicBanco="";

/**T&eacute;lefono del Banco donde se posee la Cuenta.*/
private String telefonoBanco="";

/**C&oacute;digo ABA del Banco donde se posee la Cuenta. Este c&oacute;digo solo aplica a cuentas de Bancos en EEUU.*/
private String codigoABA="";

/**Pa&iacute;s de donde es la Direcci&oacute;n de la sucursal donde se abri&oacute; la Cuenta. */
private String pais="";

/**Nombre del Banco donde que fungir&aacute; como Intermediario.*/
private String nombreBancoIntermediario = "";

/**Direccion del Banco Intermediario.*/
private String direccionBancoIntermediario="";

/**C&oacute;digo SWIFT del Banco Intermediario.*/
private String codigoSwiftBancoIntermediario="";

/**C&oacute;digo BIC del Banco Intermediario.*/
private String codigoBicBancoIntermediario="";

/**T&eacute;lefono del Banco Intermediario.*/
private String telefonoBancoIntermediario="";

/**C&oacute;digo ABA del Banco Intermediario.*/
private String codigoABAIntermediario="";

/**Pa&iacute;s de donde es la Direcci&oacute;n del Banco Intermediario.*/
private String paisBancoIntermediario="";

/**
 * Getter of the property <tt>tasa</tt>
 *
 * @return Returns the tasa.
 * 
 */
public double getTasa()
{
	return tasa;
}

/**
 * Setter of the property <tt>tasa</tt>
 *
 * @param tasa The tasa to set.
 *
 */
public void setTasa(double tasa ){
	this.tasa = tasa;
}

	/**
 * Monto involucrado en la operaci&oacute;n financiera. Indica cuanto se debe debitar, bloquear o acreditar
 */
private double monto;
 
/**
 * Getter of the property <tt>monto</tt>
 *
 * @return Returns the monto.
 * 
 */
public double getMonto()
{
	return monto;
}

/**
 * Setter of the property <tt>monto</tt>
 *
 * @param monto The monto to set.
 *
 */
public void setMonto(double monto ){
	this.monto = monto;
}


 
/**
 * Getter of the property <tt>aplicaReverso</tt>
 *
 * @return Returns the aplicaReverso.
 * 
 */
public boolean getAplicaReverso()
{
	return aplicaReverso;
}

/**
 * Setter of the property <tt>aplicaReverso</tt>
 *
 * @param aplicaReverso The aplicaReverso to set.
 *
 */
public void setAplicaReverso(boolean aplicaReverso ){
	this.aplicaReverso = aplicaReverso;
}

/**
 * Cuenta de la operaci&oacute;n
 */
private String numeroCuenta="";
 
/**
 * Status de la operaci&oacute;n
 */
private String status;
 
/**
 * Getter of the property <tt>status</tt>
 *
 * @return Returns the status.
 * 
 */
public String getStatus()
{
	return status;
}

/**
 * Setter of the property <tt>status</tt>
 *
 * @param status The status to set.
 *
 */
public void setStatus(String status ){
	this.status = status;
}

/**Setea el valor del serial contable
 * @param serialContable serial contable de la transacci&oacute;n*/
public void setSerialContable(String serialContable){
	this.serialContable = serialContable;
}

/**Retorna el serial contable asociado a la transacci&oacute;n financiera*/
public String getSerialContable(){
	return this.serialContable;
}

public String getIdTransaccionFinanciera() {
	return idTransaccionFinanciera;
}

public void setIdTransaccionFinanciera(String idTransaccionFinanciera) {
	this.idTransaccionFinanciera = idTransaccionFinanciera;
}

public Date getFechaOperacion() {
	return fechaOperacion;
}

public void setFechaOperacion(Date fechaOperacion) {
	this.fechaOperacion = fechaOperacion;
}

public Date getFechaAplicacion() {
	return fechaAplicacion;
}

public void setFechaAplicacion(Date fechaAplicacion) {
	this.fechaAplicacion = fechaAplicacion;
}

public String getError() {
	return error;
}

public void setError(String error) {
	this.error = error;
}

public long getIdOperacion() {
	return idOperacion;
}

public void setIdOperacion(long idOperacion) {
	this.idOperacion = idOperacion;
}

private long idOrden;
/**
 * Getter of the property <tt>idOrden</tt>
 *
 * @return Returns the idOrden.
 * 
 */
public double getIdOrden()
{
	return idOrden;
}

/**
 * Setter of the property <tt>idOrden</tt>
 *
 * @param idOrden The idOrden to set.
 *
 */
public void setIdOrden(long idOrden ){
	this.idOrden = idOrden;
}

/**
 * Getter of the property <tt>inComision</tt>
 *
 * @return Returns the inComision.
 * 
 */
public int getInComision()
{
	return inComision;
}

/**
 * Setter of the property <tt>inComision</tt>
 *
 * @param inComision The tasa to set.
 *
 */
public void setInComision(int inComision ){
	this.inComision = inComision;
}

public String getIdMoneda() {
	return idMoneda;
}

public void setIdMoneda(String idMoneda) {
	this.idMoneda = idMoneda;
}

public String getTipoTransaccion() {
	return tipoTransaccion;
}

public void setTipoTransaccion(String tipoTransaccion) {
	this.tipoTransaccion = tipoTransaccion;
}

public String getNombreReferenciaCuenta() {
	return nombreReferenciaCuenta;
}

public void setNombreReferenciaCuenta(String nombreReferenciaCuenta) {
	this.nombreReferenciaCuenta = nombreReferenciaCuenta;
}

public String getNombreBanco() {
	return nombreBanco;
}

public void setNombreBanco(String nombreBanco) {
	this.nombreBanco = nombreBanco;
}

public String getDireccionBanco() {
	return direccionBanco;
}

public void setDireccionBanco(String direccionBanco) {
	this.direccionBanco = direccionBanco;
}

public String getCodigoSwiftBanco() {
	return codigoSwiftBanco;
}

public void setCodigoSwiftBanco(String codigoSwiftBanco) {
	this.codigoSwiftBanco = codigoSwiftBanco;
}

public String getCodigoBicBanco() {
	return codigoBicBanco;
}

public void setCodigoBicBanco(String codigoBicBanco) {
	this.codigoBicBanco = codigoBicBanco;
}

public String getTelefonoBanco() {
	return telefonoBanco;
}

public void setTelefonoBanco(String telefonoBanco) {
	this.telefonoBanco = telefonoBanco;
}

public String getCodigoABA() {
	return codigoABA;
}

public void setCodigoABA(String codigoABA) {
	this.codigoABA = codigoABA;
}

public String getPais() {
	return pais;
}

public void setPais(String pais) {
	this.pais = pais;
}

public String getNombreBancoIntermediario() {
	return nombreBancoIntermediario;
}

public void setNombreBancoIntermediario(String nombreBancoIntermediario) {
	this.nombreBancoIntermediario = nombreBancoIntermediario;
}

public String getDireccionBancoIntermediario() {
	return direccionBancoIntermediario;
}

public void setDireccionBancoIntermediario(String direccionBancoIntermediario) {
	this.direccionBancoIntermediario = direccionBancoIntermediario;
}

public String getCodigoSwiftBancoIntermediario() {
	return codigoSwiftBancoIntermediario;
}

public void setCodigoSwiftBancoIntermediario(
		String codigoSwiftBancoIntermediario) {
	this.codigoSwiftBancoIntermediario = codigoSwiftBancoIntermediario;
}

public String getCodigoBicBancoIntermediario() {
	return codigoBicBancoIntermediario;
}

public void setCodigoBicBancoIntermediario(String codigoBicBancoIntermediario) {
	this.codigoBicBancoIntermediario = codigoBicBancoIntermediario;
}

public String getTelefonoBancoIntermediario() {
	return telefonoBancoIntermediario;
}

public void setTelefonoBancoIntermediario(String telefonoBancoIntermediario) {
	this.telefonoBancoIntermediario = telefonoBancoIntermediario;
}

public String getCodigoABAIntermediario() {
	return codigoABAIntermediario;
}

public void setCodigoABAIntermediario(String codigoABAIntermediario) {
	this.codigoABAIntermediario = codigoABAIntermediario;
}

public String getPaisBancoIntermediario() {
	return paisBancoIntermediario;
}

public void setPaisBancoIntermediario(String paisBancoIntermediario) {
	this.paisBancoIntermediario = paisBancoIntermediario;
}

public String getNumeroCuenta() {
	return numeroCuenta;
}

public void setNumeroCuenta(String numeroCuenta) {
	this.numeroCuenta = numeroCuenta;
}

public String getDescripcionTransaccion() {
	return descripcionTransaccion;
}

public void setDescripcionTransaccion(String descripcionTransaccion) {
	this.descripcionTransaccion = descripcionTransaccion;
}

/**Calcula el nuevo monto de comisi&oacute;n*/
public void recalcularMonto(double nuevaTasa){

	/*this.monto = this.monto * nuevaTasa / this.tasa;//tasa anterior=0 INFINITY
	this.tasa = nuevaTasa;*/
	this.monto=(nuevaTasa*this.monto)/100;
	this.tasa=nuevaTasa;

}

public String getTituloid() {
	return tituloid;
}

public void setTituloid(String tituloid) {
	this.tituloid = tituloid;
}

private String numeroRetencion;
/**
 * Getter of the property <tt>numeroRetencion</tt>
 *
 * @return Returns the numeroRetencion.
 * 
 */
public String getNumeroRetencion()
{
	return numeroRetencion;
}
/**
 * Setter of the property <tt>numeroRetencion</tt>
 *
 * @param numeroRetencion The numeroRetencion to set.
 *
 */
public void setNumeroRetencion(String numeroRetencion ){
	this.numeroRetencion = numeroRetencion;
}
}
