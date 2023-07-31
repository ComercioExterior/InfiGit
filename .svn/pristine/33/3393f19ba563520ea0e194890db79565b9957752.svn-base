package com.bdv.infi.data;

import java.util.Date;


/**
 * Clase que representa los objetos que se obtienen de la consulta de cobro mensual de comisiones por concepto de
 * custodia de t&iacute;tulos.
 * @author jal
 *
 */
public class ComisionMensualTitulo {
	/** Id del t&iacute;tulo. Se utiliza para almacenarlo en los t&iacute;tulos asociados a una orden */
	private String tituloId;
	
	/** Id del cliente que posee uno m&aacute;s t&iacute;tulos en custodia */
	private long clienteId;
	
	/** La moneda de negociaci&oacute;n asociada a un t&iacute;tulo */
	private String tituloMonedaNeg;
	
	/** Cantidad de t&iacute;tulos a procesar */
	private int cantidadTitulos;
	
	/** El valor total de t&iacute;tulos multiplicado por su valor nominal */
	private int totalTitulos;	
	
	/** La tasa de cambio asociada a una moneda que no sea la moneda local */
	private double tasaCambio;
	
	/** La fecha del cobro de comisi&oacute;n que se va a procesar. Este valor se establece al momento
	 *  de ejecutar el proceso de Cobro de Comisiones y su valor es la fecha que le pasamos al proceso
	 */
	private Date fechaCobro;
	
	/** Obtiene el id del cliente */
	public long getClienteId() {
		return clienteId;
	}
	
	/** Establece el id del cliente */
	public void setClienteId(long clienteId) {
		this.clienteId = clienteId;
	}
	
	/** Obtiene la moneda asociada a un t&iacute;tulo */
	public String getTituloMonedaNeg() {
		return tituloMonedaNeg;
	}
	
	/** Modifica el valor de la moneda asociada al t&iacute;tulo */
	public void setTituloMonedaNeg(String tituloMonedaNeg) {
		this.tituloMonedaNeg = tituloMonedaNeg;
	}
	
	/** Obtiene el valor total de t&iacute;tulos multiplicado por el valor nominal */
	public int getTotalTitulos() {
		return totalTitulos;
	}
	
	/** Estable el valor total de t&iacute;tulos */
	public void setTotalTitulos(int totalTitulos) {
		this.totalTitulos = totalTitulos;
	}
	
	/** Obtiene el valor de la tasa de cambio asociada a una moneda no local */
	public double getTasaCambio() {
		return tasaCambio;
	}
	
	/** Establece el valor de la tasa de cambio */
	public void setTasaCambio(double tasaAplicar) {
		this.tasaCambio = tasaAplicar;
	}
	
	/** Obtiene el id del t&iacute;tulo */
	public String getTituloId() {
		return tituloId;
	}
	
	/** Estable el id del t&iacute;tulo */
	public void setTituloId(String tituloId) {
		this.tituloId = tituloId;
	}
	
	/** Obtiene la cantidad de t&iacute;tulos */
	public int getCantidadTitulos() {
		return cantidadTitulos;
	}
	
	/** Establece la cantidad de t&iacute;tulos */
	public void setCantidadTitulos(int cantidadTitulos) {
		this.cantidadTitulos = cantidadTitulos;
	}
	
	/** Obtiene el valor de la fecha de cobro de comisi&oacute;n */
	public Date getFechaCobro() {
		return fechaCobro;
	}
	
	/** Establece el valor de la fecha de cobro de comisi&oacute;n */
	public void setFechaCobro(Date fechaCobro) {
		this.fechaCobro = fechaCobro;
	}	
}
