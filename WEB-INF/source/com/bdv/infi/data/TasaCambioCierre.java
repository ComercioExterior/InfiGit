package com.bdv.infi.data;

import java.util.Date;

/**Contiene las tasas de compra y venta de un mes de cierre.
 * Es usado para el c�lculo de comisiones por custodia*/
public class TasaCambioCierre {

	/**Fecha de cierre o de cambio*/
	private Date fechaCambio;
	
	/**Tasa de cambio*/
	private double tasaCambio;
	
	/**Tasa de cambio para la compra*/
	private double tasaCambioCompra;
	
	/**C�digo de la moneda*/
	private String codigoMoneda;

	
	/**Constructor para armar los datos referentes a la tasa de cambio de cierre de un determinado d�a*/
	public TasaCambioCierre(){
	}
	
	/**Constructor para armar los datos referentes a la tasa de cambio de cierre de un determinado d�a*/
	public TasaCambioCierre(Date fechaCambio, double tasaCambio, double tasaCambioCompra, String codigoMoneda){
		this.fechaCambio = fechaCambio;
		this.tasaCambio = tasaCambio;
		this.tasaCambioCompra = tasaCambioCompra;
		this.codigoMoneda = codigoMoneda;
	}
	
	
	/**Obtiene la fecha de cambio*/
	public Date getFechaCambio() {
		return fechaCambio;
	}

	/**Devuelve la tasa de cambio*/	
	public double getTasaCambio() {
		return tasaCambio;
	}

	/**Devuelve la tasa de cambio para la compra*/
	public double getTasaCambioCompra() {
		return tasaCambioCompra;
	}

	/**Devuelve el c�digo de la moneda*/
	public String getCodigoMoneda() {
		return codigoMoneda;
	}		
}
