package com.bdv.infi.data;

import java.util.Date;

/** 
 * T&iacute;tulos Bloqueados registrados en a base de datos
 */
public class TituloBloqueo {

/**Nombre del Cliente*/
private String clienteNombre;

/**Nombre del Beneficiario*/
private String nombreBeneficiario;


/**Número de garantía*/
private String numeroGarantia;	

/** Nombre del titulo*/
private String titulo;

/**Tipo de producto*/
private String tipoProducto;
 
public String getTipoProducto() {
	return tipoProducto;
}

public void setTipoProducto(String tipoProducto) {
	this.tipoProducto = tipoProducto;
}

/**
 * Getter of the property <tt>titulo</tt>
 *
 * @return Returns the titulo.
 * 
 */
public String getTitulo()
{
	return titulo;
}

/**
 * Setter of the property <tt>titulo</tt>
 *
 * @param titulo The titulo to set.
 *
 */
public void setTitulo(String titulo ){
	this.titulo = titulo;
}


private long cliente;
 
/**
 * Getter of the property <tt>cliente</tt>
 *
 * @return Returns the cliente.
 * 
 */
public long getCliente()
{
	return cliente;
}

/**
 * Setter of the property <tt>cliente</tt>
 *
 * @param cliente The cliente to set.
 *
 */
public void setCliente(long cliente ){
	this.cliente = cliente;
}

/*
 * (non-javadoc)
 */
private String tipoBloqueo;
 
/**
 * Getter of the property <tt>tipoBloqueo</tt>
 *
 * @return Returns the tipoBloqueo.
 * 
 */
public String getTipoBloqueo()
{
	return tipoBloqueo;
}

/**
 * Setter of the property <tt>tipoBloqueo</tt>
 *
 * @param tipoBloqueo The tipoBloqueo to set.
 *
 */
public void setTipoBloqueo(String tipoBloqueo ){
	this.tipoBloqueo = tipoBloqueo;
}

private int tituloCustodiaCantidad;
 
/**
 * Getter of the property <tt>tituloCustodiaCantidad</tt>
 *
 * @return Returns the tituloCustodiaCantidad.
 * 
 */
public int getTituloCustodiaCantidad()
{
	return tituloCustodiaCantidad;
}

/**
 * Setter of the property <tt>tituloCustodiaCantidad</tt>
 *
 * @param tituloCustodiaCantidad The tituloCustodiaCantidad to set.
 *
 */
public void setTituloCustodiaCantidad(int tituloCustodiaCantidad ){
	this.tituloCustodiaCantidad = tituloCustodiaCantidad;
}


private Date fechaBloqueo;
 
/**
 * Getter of the property <tt>fechaBloqueo</tt>
 *
 * @return Returns the fechaBloqueo.
 * 
 */
public Date getFechaBloqueo()
{
	return fechaBloqueo;
}

/**
 * Setter of the property <tt>fechaBloqueo</tt>
 *
 * @param fechaBloqueo The fechaBloqueo to set.
 *
 */
public void setFechaBloqueo(Date fechaBloqueo ){
	this.fechaBloqueo = fechaBloqueo;
}

private long idBeneficiario;

/**
 * Getter of the property <tt>beneficiario</tt>
 *
 * @return Returns the beneficiario.
 * 
 */
public long getBeneficiario()
{
	return idBeneficiario;
}

/**
 * Setter of the property <tt>beneficiario</tt>
 *
 * @param beneficiario The beneficiario to set.
 *
 */
public void setBeneficiario(long idBeneficiario){
	this.idBeneficiario = idBeneficiario;
}

/**Obtiene el número de garantía otorgado*/
public String getNumeroGarantia() {
	return numeroGarantia;
}

/**Setea el número de garantía otorgado*/
public void setNumeroGarantia(String numeroGarantia) {
	this.numeroGarantia = numeroGarantia;
}

/**
 * @return the clienteNombre
 */
public String getClienteNombre() {
	return clienteNombre;
}

/**
 * @param clienteNombre the clienteNombre to set
 */
public void setClienteNombre(String clienteNombre) {
	this.clienteNombre = clienteNombre;
}

/**
 * @return the nombreBeneficiario
 */
public String getNombreBeneficiario() {
	return nombreBeneficiario;
}

/**
 * @param nombreBeneficiario the nombreBeneficiario to set
 */
public void setNombreBeneficiario(String nombreBeneficiario) {
	this.nombreBeneficiario = nombreBeneficiario;
}

/**Suma la cantidad de títulos recibidos a lo que existe*/
public void agregarCantidad(int cantidad){
	this.tituloCustodiaCantidad+= cantidad;
}

}
