package com.bdv.infi_services.beans.entidades.mensajes_peticion;

import java.io.Serializable;



/**
 * Mensaje de peticion para los procesos de toma de ordenes
 * @author MegaSoft Computacion
 */
public class ParametrosTomaDeOrden implements Serializable{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Credenciales credencialesDeUsuario;
	/**
	 * Identificador de la Unidad de Inversion
	 */
	private String idUnidadInversion = "0";
	/**
	 * Identificador del Blotter
	 */
	private String idBlotter = "";	
	/**
	 * Tipo de Persona del Cliente
	 */
	private String tipoPersona = "";
	/**
	 * Cedula del Cliente
	 */
	private String cedulaCliente = "";
	/**
	 * Estado civil del Cliente de la Orden
	 */
	private String estadoCivilCasado;
	/**
	 * Cedula del conyuge en caso de ser casado
	 */
	private String cedulaConyuge;
	/**
	 * Monto a invertir
	 */
	private String montoInversion = "0";
	/**
	 * Cantidad a comprar
	 */
	private String cantidadComprar = "0";
	/**
	 * Precio ofrecido
	 */
	private String precioOfrecido = "0";
	/**
	 * Numero de la Cuenta del Cliente
	 */
	private String numeroCuentaCliente = "";
	/**
	 * Porcentaje de financimiento
	 */
	private String porcentajeFinanciado = "0";	
	/**
	 * Indicador de TomaOrdenSimulada financiada
	 */
	private String generarOrden = "0";		
	/**
	 * Campos Dinamicos
	 */
	private CamposDinamicosTo camposDinamicos;
	/**
	 * ArrayList de Titulos precio recompra
	 */
		private ListaTitulosRecompra listaTitulosRecompra;

	/**
	 * Constructor del bean
	 */
	public ParametrosTomaDeOrden () throws Exception {
	
	
	}

	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public ParametrosTomaDeOrden (Object [] objValor) throws Exception {
		if (objValor[0] != null && !objValor[0].equals(""))
			this.idUnidadInversion = (String)objValor[0];
		if (objValor[1] != null && !objValor[1].equals(""))
			this.idBlotter = (String)objValor[1];
		if (objValor[2] != null && !objValor[2].equals(""))
			this.tipoPersona = (String)objValor[2];	
		if (objValor[3] != null && !objValor[3].equals(""))
			this.cedulaCliente = (String)objValor[3];	
		if (objValor[4] != null && !objValor[4].equals(""))		
			this.montoInversion = (String) objValor[4];
		if (objValor[5] != null && !objValor[5].equals(""))		
			this.cantidadComprar = (String) objValor[5];	
		if (objValor[6] != null && !objValor[6].equals(""))	
			this.precioOfrecido = (String) objValor[6];
		if (objValor[7] != null && !objValor[7].equals(""))	
			this.numeroCuentaCliente = (String)objValor[7];
		if (objValor[8] != null && !objValor[8].equals(""))
			this.porcentajeFinanciado = (String) objValor[8];
		if (objValor[12] != null && !objValor[12].equals(""))
			//this.ip = (String)objValor[12];	
		if (objValor[13] != null && !objValor[13].equals(""))		
			//this.usuario = (String) objValor[13];	
		if (objValor[14] != null && !objValor[14].equals(""))
			this.generarOrden = (String) objValor[14];		
	}

	
	/**
	 * Retorna el valor del atributo Tipo de Persona del de la Unidad de Inversion
	 * @return
	 */
	public String getIdUnidadInversion() {
		return idUnidadInversion;
	}
	/**
	 * Asigna valor al atributo Tipo de Persona del de la Unidad de Inversion
	 * @param idUnidadInversion
	 */	
	public void setIdUnidadInversion(String idUnidadInversion) {
		this.idUnidadInversion = idUnidadInversion;
	}
	
	/**
	 * Retorna el valor del Tipo de Persona del del Blotter
	 * @return idBlotter
	 */
	public String getIdBlotter() {
		return idBlotter;
	}
	/**
	 * Asigna valor al Tipo de Persona del del Blotter
	 * @param idBlotter
	 */	
	public void setIdBlotter(String idBlotter) {
		this.idBlotter = idBlotter;
	}
	
	/**
	 * Retorna el valor del Tipo de Persona del Cliente
	 * @return tipoPersona
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}
	/**
	 * Asigna valor al Tipo de Persona del Cliente
	 * @param tipoPersona
	 */	
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	/**
	 * Retorna el valor de la Cedula del Cliente
	 * @return cedulaCliente
	 */
	public String getCedulaCliente() {
		return cedulaCliente;
	}
	/**
	 * Asigna valor a la Cedula del Cliente
	 * @param cedulaCliente
	 */	
	public void setCedulaCliente(String cedulaCliente) {
		this.cedulaCliente = cedulaCliente;
	}
	
	/**
	 * Retorna el valor del Monto a invertir
	 * @return montoInversion
	 */
	public String getMontoInversion() {
		return montoInversion;
	}
	/**
	 * Asigna valor al Monto a invertir
	 * @param montoInversion
	 */	
	public void setMontoInversion(String montoInversion) {
		this.montoInversion = montoInversion;
	}
	
	/**
	 * Retorna el valor de la Cantidad a Comprar
	 * @return cantidadComprar
	 */
	public String getCantidadComprar() {
		return cantidadComprar;
	}
	/**
	 * Asigna valor a la Cantidad a Comprar
	 * @param cantidadComprar
	 */	
	public void setCantidadComprar(String cantidadComprar) {
		this.cantidadComprar = cantidadComprar;
	}
	
	/**
	 * Retorna el valor del Precio ofrecido
	 * @return precioOfrecido
	 */
	public String getPrecioOfrecido() {
		return precioOfrecido;
	}
	/**
	 * Asigna valor al Precio ofrecido
	 * @param precioOfrecido
	 */	
	public void setPrecioOfrecido(String montoMinimoInversion) {
		this.precioOfrecido = montoMinimoInversion;
	}
	
	/**
	 * Retorna el valor del Numero de la Cuenta del Cliente
	 * @return numeroCuentaCliente
	 */
	public String getNumeroCuentaCliente() {
		return numeroCuentaCliente;
	}
	/**
	 * Asigna valor al Numero de la Cuenta del Cliente
	 * @param numeroCuentaCliente
	 */	
	public void setNumeroCuentaCliente(String nombreUnidadInversion) {
		this.numeroCuentaCliente = nombreUnidadInversion;
	}

	/**
	 * Retorna el valor del Porcentaje de financimiento
	 * @return porcentajeFinanciado
	 */
	public String getPorcentajeFinanciado() {
		return porcentajeFinanciado;
	}
	/**
	 * Asigna valor al Porcentaje de financimiento
	 * @param porcentajeFinanciado
	 */
	public void setPorcentajeFinanciado(String porcentajeFinanciado) {
		this.porcentajeFinanciado = porcentajeFinanciado;
	}
	
	/**
	 * Retorna el valor del Indicador de generar la Orden
	 * @return generarOrden
	 */
	public String isGenerarOrden() {
		return generarOrden;
	}
	/**
	 * Asigna valor al Indicador de generar la Orden
	 * @param generarOrden
	 */
	public void setGenerarOrden(String generarOrden) {
		this.generarOrden = generarOrden;
	}

	/**
	 * @return the camposDinamicos
	 */
	public CamposDinamicosTo getCamposDinamicos() {
		return camposDinamicos;
	}

	/**
	 * @param camposDinamicos the camposDinamicos to set
	 */
	public void setCamposDinamicos(CamposDinamicosTo camposDinamicos) {
		this.camposDinamicos = camposDinamicos;
	}

	/**
	 * @return the credencialesDeUsuario
	 */
	public Credenciales getCredencialesDeUsuario() {
		return credencialesDeUsuario;
	}

	/**
	 * @param credencialesDeUsuario the credencialesDeUsuario to set
	 */
	public void setCredencialesDeUsuario(Credenciales credencialesDeUsuario) {
		this.credencialesDeUsuario = credencialesDeUsuario;
	}

	/**
	 * @return the generarOrden
	 */
	public String getGenerarOrden() {
		return generarOrden;
	}

	/**
	 * @return the listaTitulosRecompra
	 */
	public ListaTitulosRecompra getListaTitulosRecompra() {
		return listaTitulosRecompra;
	}

	/**
	 * @param listaTitulosRecompra the listaTitulosRecompra to set
	 */
	public void setListaTitulosRecompra(ListaTitulosRecompra listaTitulosRecompra) {
		this.listaTitulosRecompra = listaTitulosRecompra;
	}

	/**
	 * @return the cedulaConyuge
	 */
	public String getCedulaConyuge() {
		return cedulaConyuge;
	}

	/**
	 * @param cedulaConyuge the cedulaConyuge to set
	 */
	public void setCedulaConyuge(String cedulaConyuge) {
		this.cedulaConyuge = cedulaConyuge;
	}

	/**
	 * @return the estadoCivilCasado
	 */
	public String getEstadoCivilCasado() {
		return estadoCivilCasado;
	}

	/**
	 * @param estadoCivilCasado the estadoCivilCasado to set
	 */
	public void setEstadoCivilCasado(String estadoCivilCasado) {
		this.estadoCivilCasado = estadoCivilCasado;
	}
}
