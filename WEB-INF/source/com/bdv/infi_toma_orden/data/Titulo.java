package com.bdv.infi_toma_orden.data;

import java.math.BigDecimal;


/** 
 * T&iacute;tulos registrados en a base de datos
 */
public class Titulo {


	/**
 * C&oacute;digo del t&iacute;tulo
 */
private String id;

/**Fecha de emision*/
private String fechaEmision ="";

/**Tasa de cupón*/
private BigDecimal tasaCupon = new BigDecimal(0);  

/**Base de cálculo*/
private String base = "";

/**
 * valor expresado en moneda de la inversi&oacute;n
 */
private double valorNominal;


/**Precio de mercado*/
private double precioMercado;	

/**Fecha Inicio Cupon*/
private String fechaInicioCupon ="";	
 
public String getFechaInicioCupon() {
	return fechaInicioCupon;
}

public void setFechaInicioCupon(String fechaInicioCupon) {
	this.fechaInicioCupon = fechaInicioCupon;
}

/**
 * Getter of the property <tt>valorNominal</tt>
 *
 * @return Returns the valorNominal.
 * 
 */
public double getValorNominal()
{
	return valorNominal;
}

/**
 * Setter of the property <tt>valorNominal</tt>
 *
 * @param valorNominal The valorNominal to set.
 *
 */
public void setValorNominal(double valorNominal ){
	this.valorNominal = valorNominal;
}

	/**
 * Getter of the property <tt>codigo</tt>
 *
 * @return Returns the codigo.
 * 
 */
public String getId()
{
	return id;
}

/**
 * Setter of the property <tt>codigo</tt>
 *
 * @param codigo The codigo to set.
 *
 */
public void setId(String id ){
	this.id = id;
}

	/**
 * Descripci&oacute;n del t&iacute;tulo
 */
private String descripcion;
 
/**
 * Getter of the property <tt>descripcion</tt>
 *
 * @return Returns the descripcion.
 * 
 */
public String getDescripcion()
{
	return descripcion;
}

/**
 * Setter of the property <tt>descripcion</tt>
 *
 * @param descripcion The descripcion to set.
 *
 */
public void setDescripcion(String descripcion ){
	this.descripcion = descripcion;
}

public String getFechaEmision() {
	return fechaEmision;
}

public void setFechaEmision(String fechaEmision) {
	this.fechaEmision = fechaEmision;
}

public BigDecimal getTasaCupon() {
	return tasaCupon;
}

public void setTasaCupon(BigDecimal tasaCupon) {
	this.tasaCupon = tasaCupon;
}

public String getBase() {
	return base;
}

public void setBase(String base) {
	this.base = base;
}

/**
 * Retorna el precio de mercado asociado al t&iacute;tulo
 * @return
 */
public double getPrecioMercado() {
	return precioMercado;
}

/**
 * Asigna el precio de mercado del t&iacute;tulo
 * @param precioMercado
 */
public void setPrecioMercado(double precioMercado) {
	this.precioMercado = precioMercado;
}	

}
