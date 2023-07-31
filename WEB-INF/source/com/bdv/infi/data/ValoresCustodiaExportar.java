/**
 * 
 */
package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @author bag
 *
 */
public class ValoresCustodiaExportar {

/**
 * Fecha de la orden
 */
private Date ordene_ped_fe_orden;
/**
 * Moneda
 */
private String moneda_id;
/**
 * Ordenes Entrantes
 */
private int ordenes_entrantes;
/**
 * Cantidad Monto Entrante
 */
private BigDecimal cantidad_entrantes;
/**
 * Cantidad Monto Total
 */
private BigDecimal monto_total;
/**
 * Cantidad Monto Salida
 */
private BigDecimal cantidad_salidas;
/**
 * Ordenes Salientes
 */
private int ordenes_salidas;
	
/**
 * Getter of the property <tt>moneda_id</tt>
 *
 * @return Returns the moneda_id.
 * 
 */
public String getMoneda_id()
{
	return moneda_id;
}
/**
 * Setter of the property <tt>moneda_id</tt>
 *
 * @param moneda_id The moneda_id to set.
 *
 */
public void setMoneda_id(String moneda_id ){
	this.moneda_id = moneda_id;
}
/**
 * Getter of the property <tt>ordenes_entrantes</tt>
 *
 * @return Returns the ordenes_entrantes.
 * 
 */
public int getOrdenes_entrantes()
{
	return ordenes_entrantes;
}
/**
 * Setter of the property <tt>ordenes_entrantes</tt>
 *
 * @param ordenes_entrantes The ordenes_entrantes to set.
 *
 */
public void setOrdenes_entrantes(int ordenes_entrantes ){
	this.ordenes_entrantes = ordenes_entrantes;
}
/**
 * Getter of the cantidad_entrantes <tt>cantidad_entrantes</tt>
 *
 * @return Returns the cantidad_entrantes.
 * 
 */
public BigDecimal getCantidad_entrantes()
{
	return cantidad_entrantes;
}
/**
 * Setter of the property <tt>cantidad_entrantes</tt>
 *
 * @param cantidad_entrantes The cantidad_entrantes to set.
 *
 */
public void setCantidad_entrantes(BigDecimal cantidad_entrantes ){
	this.cantidad_entrantes = cantidad_entrantes;
}
/**
 * Getter of the property <tt>ordene_ped_fe_orden</tt>
 *
 * @return Returns the ordene_ped_fe_orden.
 * 
 */
public Date getOrdene_ped_fe_orden()
{
	return ordene_ped_fe_orden;
}
/**
 * Setter of the property <tt>ordene_ped_fe_orden</tt>
 *
 * @param ordene_ped_fe_orden The ordene_ped_fe_orden to set.
 *
 */
public void setOrdene_ped_fe_orden(Date ordene_ped_fe_orden ){
	this.ordene_ped_fe_orden = ordene_ped_fe_orden;
}
/**
 * Getter of the property <tt>cantidad_salidas</tt>
 *
 * @return Returns the cantidad_salidas.
 * 
 */
public BigDecimal getCantidad_salidas()
{
	return cantidad_salidas;
}
/**
 * Setter of the property <tt>cantidad_salidas</tt>
 *
 * @param cantidad_salidas The cantidad_salidas to set.
 *
 */
public void setCantidad_salidas(BigDecimal cantidad_salidas ){
	this.cantidad_salidas = cantidad_salidas;
}
/**
 * Getter of the property <tt>ordenes_salidas</tt>
 *
 * @return Returns the ordenes_salidas.
 * 
 */
public int getOrdenes_salidas()
{
	return ordenes_salidas;
}
/**
 * Setter of the property <tt>ordenes_salidas</tt>
 *
 * @param ordenes_salidas The ordenes_salidas to set.
 *
 */
public void setOrdenes_salidas(int ordenes_salidas ){
	this.ordenes_salidas = ordenes_salidas;
}
/**
 * Getter of the property <tt>monto_total</tt>
 *
 * @return Returns the monto_total.
 * 
 */
public BigDecimal getMonto_total()
{
	return monto_total;
}
/**
 * Setter of the property <tt>monto_total</tt>
 *
 * @param monto_total The monto_total to set.
 *
 */
public void setMonto_total(BigDecimal monto_total ){
	this.monto_total = monto_total;
}
}
