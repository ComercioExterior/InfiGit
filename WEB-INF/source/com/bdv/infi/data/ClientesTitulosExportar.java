/**
 * 
 */
package com.bdv.infi.data;

import java.math.BigDecimal;

/**
 * @author eel
 *
 */
public class ClientesTitulosExportar {
	
	/**
	 * Nombre del cliente
	 */
	private String cliente;
	private long extranjero;	
	private long natural;
	private long juridico;
	private String tipoPersona;
	private String tipoProducto;
	private long titulosCantidad;
	private long valorNominal;
	private BigDecimal porcentaje;
	private BigDecimal monto;	
	private String titulo;	
	private long cantidad;	
	private String monedaDenominacion;
	private String monedaNegociacion;
	private String emision;
	private String custodia;	
	private String vencimiento;
	private String estado;
	
	/**
	 * @return the extranjero
	 */
	public long getExtranjero() {
		return extranjero;
	}
	/**
	 * @param extranjero the extranjero to set
	 */
	public void setExtranjero(long extranjero) {
		this.extranjero = extranjero;
	}
	/**
	 * @return the natural
	 */
	public long getNatural() {
		return natural;
	}
	/**
	 * @param natural the natural to set
	 */
	public void setNatural(long natural) {
		this.natural = natural;
	}
	/**
	 * @return the juridico
	 */
	public long getJuridico() {
		return juridico;
	}
	/**
	 * @param juridico the juridico to set
	 */
	public void setJuridico(long juridico) {
		this.juridico = juridico;
	}
	public String getCliente()
	{
		return cliente;
	}
	/**
	 * Setter of the property <tt>cliente</tt>
	 *
	 * @param String cliente The cliente to set.
	 *
	 */
	public void setCliente(String cliente){
		this.cliente = cliente;
	}

	 
	/**
	 * Getter of the property <tt>titulosCantidad</tt>
	 *
	 * @return Returns the titulosCantidad.
	 * 
	 */
	public long getTitulosCantidad()
	{
		return titulosCantidad;
	}
	/**
	 * Setter of the property <tt>titulosCantidad</tt>
	 *
	 * @param String titulosCantidad The titulosCantidad to set.
	 *
	 */
	public void setTitulosCantidad(long titulosCantidad){
		this.titulosCantidad = titulosCantidad;
	}

	 
	/**
	 * Getter of the property <tt>valorNominal</tt>
	 *
	 * @return Returns the valorNominal.
	 * 
	 */
	public long getValorNominal()
	{
		return valorNominal;
	}
	/**
	 * Setter of the property <tt>valorNominal</tt>
	 *
	 * @param String valorNominal The valorNominal to set.
	 *
	 */
	public void setValorNominal(long valorNominal){
		this.valorNominal = valorNominal;
	}
	 
	/**
	 * Getter of the property <tt>porcentaje</tt>
	 *
	 * @return Returns the porcentaje.
	 * 
	 */
	public BigDecimal getPorcentaje()
	{
		return porcentaje;
	}
	/**
	 * Setter of the property <tt>porcentaje</tt>
	 *
	 * @param BigDecimal porcentaje The porcentaje to set.
	 *
	 */
	public void setPorcentaje(BigDecimal porcentaje){
		this.porcentaje = porcentaje;
	}

	 
	/**
	 * Getter of the property <tt>monto</tt>
	 *
	 * @return Returns the monto.
	 * 
	 */
	public BigDecimal getMonto()
	{
		return monto;
	}
	/**
	 * Setter of the property <tt>monto</tt>
	 *
	 * @param String monto The monto to set.
	 *
	 */
	public void setMonto(BigDecimal monto){
		this.monto = monto;
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
	 * @param String titulo The titulo to set.
	 *
	 */
	public void setTitulo(String titulo){
		this.titulo = titulo;
	}

	 
	/**
	 * Getter of the property <tt>cantidad</tt>
	 *
	 * @return Returns the cantidad.
	 * 
	 */
	public long getcantidad()
	{
		return cantidad;
	}
	/**
	 * Setter of the property <tt>cantidad</tt>
	 *
	 * @param long cantidad The cantidad to set.
	 *
	 */
	public void setcantidad(long cantidad){
		this.cantidad = cantidad;
	}

	 
	/**
	 * Getter of the property <tt>monedaDenominacion</tt>
	 *
	 * @return Returns the monedaDenominacion.
	 * 
	 */
	public String getmonedaDenominacion()
	{
		return monedaDenominacion;
	}
	/**
	 * Setter of the property <tt>monedaDenominacion</tt>
	 *
	 * @param String monedaDenominacion The monedaDenominacion to set.
	 *
	 */
	public void setmonedaDenominacion(String monedaDenominacion){
		this.monedaDenominacion = monedaDenominacion;
	}

	 
	/**
	 * Getter of the property <tt>monedaNegociacion</tt>
	 *
	 * @return Returns the monedaNegociacion.
	 * 
	 */
	public String getmonedaNegociacion()
	{
		return monedaNegociacion;
	}
	/**
	 * Setter of the property <tt>monedaNegociacion</tt>
	 *
	 * @param String monedaNegociacion The monedaNegociacion to set.
	 *
	 */
	public void setmonedaNegociacion(String monedaNegociacion){
		this.monedaNegociacion = monedaNegociacion;
	}

	 
	/**
	 * Getter of the property <tt>emision</tt>
	 *
	 * @return Returns the emision.
	 * 
	 */
	public String getemision()
	{
		return emision;
	}
	/**
	 * Setter of the property <tt>emision</tt>
	 *
	 * @param String emision The emision to set.
	 *
	 */
	public void setemision(String emision){
		this.emision = emision;
	}

	 
	/**
	 * Getter of the property <tt>vencimiento</tt>
	 *
	 * @return Returns the vencimiento.
	 * 
	 */
	public String getvencimiento()
	{
		return vencimiento;
	}
	/**
	 * Setter of the property <tt>vencimiento</tt>
	 *
	 * @param String vencimiento The vencimiento to set.
	 *
	 */
	public void setvencimiento(String vencimiento){
		this.vencimiento = vencimiento;
	}

	 
	/**
	 * Getter of the property <tt>custodia</tt>
	 *
	 * @return Returns the custodia.
	 * 
	 */
	public String getcustodia()
	{
		return custodia;
	}
	/**
	 * Setter of the property <tt>custodia</tt>
	 *
	 * @param String custodia The custodia to set.
	 *
	 */
	public void setcustodia(String custodia){
		this.custodia = custodia;
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	
}
