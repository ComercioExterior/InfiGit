package com.bdv.infi.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * T&iacute;tulos asociados a la orden
 */
public class OrdenTitulo implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Id del t&iacute;tulo */
	private String tituloId;
	
	/** Porcentaje de compra */
	private String porcentaje = "0";
	
	
	/** Monto manejado */
	private double monto;
	
	/**Unidades */
	private double unidades;
	
	
	/**
	 * Porcentaje de Recompra del Título
	 */
	private double porcentajeRecompra;
	
	/**Precio de mercado*/
	private double precioMercado;
	
	/**Monto de intereses caídos*/
	private BigDecimal montoIntCaidos = new BigDecimal(0);
	
	/**Corresponde el monto cancelado por recompra con neteo*/
	private BigDecimal montoNeteo = BigDecimal.ZERO;	
	

	private long idOrden;
	
	private BigDecimal tasaCambio;
	
	private String fechaValor;
	/**
	 * Recupera en monto. Dependiendo de la transacci&oacute;n hace referencia. Si es una toma de orden 
	 * representa el porcentaje comprado para el t&iacute;tulo.
	 * 
	 * @return retorna el monto.
	 * 
	 */
	public double getMonto() {
		return monto;
	}

	/**
	 * Establece el monto vendido o comprado
	 * 
	 * @param monto Monto a establecer
	 * 
	 */
	public void setMonto(double monto) {
		this.monto = monto;
	}

	/**Recupera el id del t&iacute;tulo
	 * @return id del t&iacute;tulo*/
	public String getTituloId() {
		return tituloId;
	}

	/**Establece el id del t&iacute;tulo
	 * @param tituloId id del t&iacute;tulo relacionado a la orden*/
	public void setTituloId(String tituloId) {
		this.tituloId = tituloId;
	}

	/**Recupera el porcentaje definido en la unidad de inversi&oacute;n al momento de una toma de orden
	 * @return el porcentaje definido en la configuraci&oacute;n de la unidad de inversi&oacute;n cuando es una toma de orden*/
	public String getPorcentaje() {
		return porcentaje;
	}

	/**Establece el porcentaje de t&iacute;tulo
	 * @param porcentaje porcentaje a establecer en el t&iacute;tulo*/
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**Recupera las unidades que est&aacute;n en el t&iacute;tulo. Depende de la transacci&oacute;n se puede hacer referencia a lo que significa.
	 * Si es una toma de orden cuantas unidades corresponden con el porcentaje definido en el t&iacute;tulo
	 * @return unidades calculadas o vendidas*/
	public double getUnidades() {
		return unidades;
	}

	/**Establece las unidades relacionadas al t&iacute;tulo
	 * @param unidades unidades relacionadas al t&iacute;tulo*/
	public void setUnidades(double unidades) {
		this.unidades = unidades;
	}
	
	/**
	 * Retorna el Porcentaje de Recompra asociado al t&iacute;tulo
	 * @return
	 */
	public double getPorcentajeRecompra() {
		return porcentajeRecompra;
	}

	/**
	 * Asigna el porcentaje de Recompra del t&iacute;tulo
	 * @param porcentajeRecompra
	 */
	public void setPorcentajeRecompra(double porcentajeRecompra) {
		this.porcentajeRecompra = porcentajeRecompra;
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
	
	/**
	 * Retorna el monto de los intereses caídos por el título
	 * @return
	 */
	public BigDecimal getMontoIntCaidos() {
		return montoIntCaidos;
	}

	/**
	 * Asigna el monto de los intereses caídos
	 * @param precioMercado
	 */
	public void setMontoIntCaidos(BigDecimal montoIntCaidos) {
		this.montoIntCaidos = montoIntCaidos;
	}
	
	/**Recupera el monto por concepto de neteo*/
	public BigDecimal getMontoNeteo() {
		return montoNeteo;
	}

	/**Establece el monto por concepto de neteo*/	
	public void setMontoNeteo(BigDecimal montoNeteo) {
		this.montoNeteo = montoNeteo;
	}

	
	public long getIdOrden() {
		return idOrden;
	}

	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}	
	
	public String toString(){
		
		return "Orden --> " + idOrden + "Titulo ---> " + tituloId;
	}

	public BigDecimal getTasaCambio() {
		return tasaCambio;
	}

	public void setTasaCambio(BigDecimal tasaCambio) {
		this.tasaCambio = tasaCambio;
	}

	public String getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}
	
}