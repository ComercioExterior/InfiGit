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
public class PagoCheque {
	
/**
 * Cedula del cliente
 */	
	
private String cedulaCliente;

/**
 * Nombre del Cliente
 */

private String nombreCliente;
/**
 * Monto de operacion
 */

private BigDecimal montoOperacion;

/**
 * fecha operacion
 */

private Date fechaOperacion;

/**
 * fecha pago cheque
 */

private Date fechaPagoCheque;

/**
 * numero de cheque
 */

private String numeroCheque;

/**
 * moneda de denominacion
 */

private String monedaOperacion;



/**
 * @return the monedaOperacion
 */
public String getMonedaOperacion() {
	return monedaOperacion;
}

/**
 * @param monedaOperacion the monedaOperacion to set
 */
public void setMonedaOperacion(String monedaOperacion) {
	this.monedaOperacion = monedaOperacion;
}

public String getCedulaCliente() {
	return cedulaCliente;
}

public void setCedulaCliente(String cedulaCliente) {
	this.cedulaCliente = cedulaCliente;
}

public Date getFechaOperacion() {
	return fechaOperacion;
}

public void setFechaOperacion(Date fechaOperacion) {
	this.fechaOperacion = fechaOperacion;
}

public Date getFechaPagoCheque() {
	return fechaPagoCheque;
}

public void setFechaPagoCheque(Date fechaPagoCheque) {
	this.fechaPagoCheque = fechaPagoCheque;
}

public BigDecimal getMontoOperacion() {
	return montoOperacion;
}

public void setMontoOperacion(BigDecimal montoOperacion) {
	this.montoOperacion = montoOperacion;
}

public String getNombreCliente() {
	return nombreCliente;
}

public void setNombreCliente(String nombreCliente) {
	this.nombreCliente = nombreCliente;
}

public String getNumeroCheque() {
	return numeroCheque;
}

public void setNumeroCheque(String numeroCheque) {
	this.numeroCheque = numeroCheque;
}



	
	
	
	

}
