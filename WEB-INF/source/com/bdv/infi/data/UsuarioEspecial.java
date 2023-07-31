package com.bdv.infi.data;

/**Entidad de usuario especial que contiene la permisolog&iacute;a asociada a los valores que puede cambiar asociada a una toma de orden*/
public class UsuarioEspecial {

	protected String idUsuario;
	
	private String usresp_cambio_comision;
	private String usresp_cambio_vehiculo;
	private String usresp_multiblotter;
	private String usresp_financiamiento;
	private String usrespPrecioVentaTitulos;
	
	/**indica si el usuario puede hacer cambio de comision en una toma de orden*/
	private boolean cambioComision = false;
	/**indica si el usuario puede hacer cambio de veh&iacute;culo en una toma de orden*/	
	private boolean cambioVehiculo = false;
	/**indica si el usuario puede hacer cambio de bloter en una toma de orden*/	
	private boolean multibloter = false;
	/**indica si el usuario puede hacer cambio de financiamiento en una toma de orden*/	
	private boolean financiamiento = false;
	/**
	 * indica si el usuario puede cambiar el precio de recompra en una venta de t&iacute;tulos
	 */
	private boolean precioVentaTitulos = false;
	
	/**
	 * indica se el usuario puede cambiar el precio de recompra en una toma de orden
	 */
	private boolean cambioPrecioTomaOrden = false;
	
	/**Indicador de ingreso de instrucciones de pago para recompra**/
	private boolean ingresoInstruccionesPago = false;
	
	/**
	 * @return la variable idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}
	/**
	 * @param idUsuario establece la variable idUsuario
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	/**
	 * @return indica si el usuario puede hacer cambio de comision
	 */
	public boolean isCambioComision() {
		return cambioComision;
	}
	/**
	 * @param cambioComision establece la variable cambioComision
	 */
	public void setCambioComision(boolean cambioComision) {
		this.cambioComision = cambioComision;
	}
	/**
	 * @return indica si el usuario puede hacer cambio de veh&iacute;culo
	 */
	public boolean isCambioVehiculo() {
		return cambioVehiculo;
	}
	/**
	 * @param cambioVehiculo establece la variable cambioVehiculo
	 */
	public void setCambioVehiculo(boolean cambioVehiculo) {
		this.cambioVehiculo = cambioVehiculo;
	}
	/**
	 * @return indica si es multibloter
	 */
	public boolean isMultibloter() {
		return multibloter;
	}
	/**
	 * @param multibloter establece la variable multibloter
	 */
	public void setMultibloter(boolean multibloter) {
		this.multibloter = multibloter;
	}
	/**
	 * @return indica si tiene la permisolog&iacute;a adecuada para el cambio de financiamiento
	 */
	public boolean isFinanciamiento() {
		return financiamiento;
	}
	
	/**
	 * @param financiamiento establece la variable financiamiento
	 */
	public void setFinanciamiento(boolean financiamiento) {
		this.financiamiento = financiamiento;
	}	

	public String getUsresp_cambio_comision()
	{
		return usresp_cambio_comision;
	}
		
	public String getUsresp_cambio_vehiculo() {
		return usresp_cambio_vehiculo;

	}

	public String getUsresp_multiblotter() {
		return usresp_multiblotter;

	}

	public String getUsresp_financiamiento() {
		return usresp_financiamiento;

	}

	public void setUsresp_cambio_comision(String usresp_cambio_comision) {
		this.usresp_cambio_comision = usresp_cambio_comision;
	}	

	public void setUsresp_cambio_vehiculo(String usresp_cambio_vehiculo) {
		this.usresp_cambio_vehiculo = usresp_cambio_vehiculo;
	}
	
	public void setUsresp_multiblotter(String usresp_multiblotter) {
		this.usresp_multiblotter = usresp_multiblotter;
	}
	
	public void setUsresp_financiamiento(String usresp_financiamiento) {
		this.usresp_financiamiento = usresp_financiamiento;
	}
	
	public String getUsrespPrecioVentaTitulos() {
		return usrespPrecioVentaTitulos;
	}
	
	public void setUsrespPrecioVentaTitulos(String usrespPrecioVentaTitulos) {
		this.usrespPrecioVentaTitulos = usrespPrecioVentaTitulos;
	}
	
	public boolean isPrecioVentaTitulos() {
		return precioVentaTitulos;
	}
	
	public void setPrecioVentaTitulos(boolean precioVentaTitulos) {
		this.precioVentaTitulos = precioVentaTitulos;
	}
	
	public boolean isCambioPrecioTomaOrden() {
		return cambioPrecioTomaOrden;
	}
	
	public void setCambioPrecioTomaOrden(boolean cambioPrecioTomaOrden) {
		this.cambioPrecioTomaOrden = cambioPrecioTomaOrden;
	}

	public boolean isIngresoInstruccionesPago() {
		return ingresoInstruccionesPago;
	}
	public void setIngresoInstruccionesPago(boolean ingresoInstruccionesPago) {
		this.ingresoInstruccionesPago = ingresoInstruccionesPago;
	}

	

}
