package com.bdv.infi.data;

/**Clase que representa un elemento detalle dentro de una factura*/
public class DetalleFactura {

	/**Id del detalle de la factura*/
	private long idDetalleFactura;
	
	/**Id de la factura*/
	private long idFactura;
	
	/**Nombre del servicio cobrado*/
	private String nombreServicio = "";
	
	/**Cantidad o monto del servicio proporcionado*/
	private double cantidad;

	/**Tasa o monto aplicado por cada servicio*/
	private double tasaMonto = 0;
	
	/**Monto total de la comisión a cobrar*/
	private double MontoOperacion = 0;
	
	/**Id de la moneda sobre el cuál está el monto*/
	private String idMoneda = "";
	
	/**Indica la cantidad de operaciones efectuadas */
	private long cantidadOperaciones;		


	/**Devuelve el id del detalle de la factura*/
	public long getIdDetalleFactura() {
		return idDetalleFactura;
	}

	/**Setea el id del detalle de la factura*/
	public void setIdDetalleFactura(long idDetalleFactura) {
		this.idDetalleFactura = idDetalleFactura;
	}	
	
	/**Devuelve el id de la factura*/
	public long getIdFactura() {
		return idFactura;
	}

	/**Devuelve el id de la factura*/
	public void setIdFactura(long idFactura) {
		this.idFactura = idFactura;
	}

	/**Devuelve el nombre del servicio prestado o descripción de la operación*/
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**Setea el nombre del servicio prestado o descripción de la operación*/
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**Devuelve la cantidad del servicio prestado*/
	public double getCantidad() {
		return cantidad;
	}

	/**Setea la cantidad del servicio prestado*/
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	/**Devuelve la tasa o monto que debe aplicarse sobre cada servicio prestado.
	 * Ej. cantidad = 200.000.000,00 valor de los títulos por salida interna
	 *     tasa = 0,2%
	 *     Resultado = cantidad * tasa */
	public double getTasaMonto() {
		return tasaMonto;
	}

	/**Setea la tasa o monto que debe aplicarse sobre cada servicio prestado.*/
	public void setTasaMonto(double tasaMonto) {
		this.tasaMonto = tasaMonto;
	}

	/**Obtiene el monto de la operación resultado de la multiplicación de cantidad por tasa*/
	public double getMontoOperacion() {
		return MontoOperacion;
	}

	/**Setea el monto de la operación por el servicio prestado*/
	public void setMontoOperacion(double montoOperacion) {
		MontoOperacion = montoOperacion;
	}

	/**Devuelve el id de la moneda sobre la cuál está el monto*/
	public String getIdMoneda() {
		return idMoneda;
	}

	/**Setea el id de la moneda sobre la cuál está el monto*/	
	public void setIdMoneda(String id) {
		this.idMoneda = id;
	}
	
	/**Establece la cantidad de operaciones efectuadas*/
	public void setCantidadOperaciones(long cantidadOperaciones){
		this.cantidadOperaciones = cantidadOperaciones;
	}
	
	/**Retorna la cantidad de operaciones efectuadas*/
	public long getCantidadOperaciones(){
		return this.cantidadOperaciones;
	}	
	
}
