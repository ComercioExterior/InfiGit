package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.Date;

/**Representa el detalle de los cálculos efectuados para fechas determinadas en los procesos de cobro
 * de comisiones o pago de cupones o amortizaciones*/
public class CalculoMesDetalle {

	/**Id del detalle de la operación*/
	private long idRegistro;
	
	/**Id que hace referencia al cálculo del mes*/
	private long idCalculoMes;
	
	/**Nombre de la operación o servicio*/
	private String nombreOperacion = "";
	
	/**Indica el total de montos o cantidades calculadas en la transacción*/
	private BigDecimal cantidad;
	
	/**Indica la tasa o el monto fijo obtenido para obtener el monto de la operación*/
	private double tasaMonto;
	
	/**Monto de la operación*/
	private BigDecimal montoOperacion;
	
	/**Comision de la operación*/
	private BigDecimal comisionOperacion;	
	
	/**Id de la moneda en la que se expresa el monto*/
	private String idMoneda = "";
	
	/**Id del título involucrado cuando es pago de cupones o amortizaciones*/
	private String idTitulo = "";	
	
	/**Código de la operación que debe enviarse a altair*/
	private String codigoOperacion = "";
	
	/**Id del cliente*/
	private long idCliente;

	/**Indica si el campo tasa_monto debe mostrarse en el reporte con signo de porcentaje*/
	private boolean signoPorcentaje;
	
	/**Fecha inicio del pago de cupón o amortización*/
	private Date fechaInicio = null;
	
	/**Fecha fin del pago de cupón o amortización*/
	private Date fechaFin = null;
	
	/**Indica la cantidad de operaciones efectuadas */
	private long cantidadOperaciones;
	
	/**Días de cálculo */
	private int diasCalculo;	
	
	
	/**Devuelve el Id del detalle de la operación*/
	public long getIdRegistro() {
		return idRegistro;
	}

	/**Setea el Id del detalle de la operación*/	
	public void setIdRegistro(long idRegistro) {
		this.idRegistro = idRegistro;
	}

	/**Devuelve el id que hace referencia al cálculo del mes*/	
	public long getIdCalculoMes() {
		return idCalculoMes;
	}

	/**Setea el id que hace refencia al cálculo del mes*/
	public void setIdCalculoMes(long idCalculoMes) {
		this.idCalculoMes = idCalculoMes;
	}

	/**Devuelve el nombre de la operación*/
	public String getNombreOperacion() {
		return nombreOperacion;
	}

	/**Setea el nombre de la operación*/
	public void setNombreOperacion(String nombreOperacion) {
		this.nombreOperacion = nombreOperacion;
	}

	/**Devuelve el el total de montos o cantidades calculadas en la transacción*/
	public BigDecimal getCantidad() {
		return cantidad;
	}

	/**Setea el el total de montos o cantidades calculadas en la transacción*/	
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	/**Devuelve la tasa o el monto fijo obtenido para obtener el monto de la operación*/	
	public double getTasaMonto() {
		return tasaMonto;
	}

	/**Establece la tasa o el monto fijo obtenido para obtener el monto de la operación*/	
	public void setTasaMonto(double tasaMonto) {
		this.tasaMonto = tasaMonto;
	}

	/**Devuelve el monto de la operación*/
	public BigDecimal getMontoOperacion() {
		return montoOperacion;
	}

	/**Establece el monto de la operación*/
	public void setMontoOperacion(BigDecimal montoOperacion) {
		this.montoOperacion = montoOperacion;
	}
	
	/**Devuelve el monto de la comision*/
	public BigDecimal getComisionOperacion() {
		return comisionOperacion==null?new BigDecimal(0):comisionOperacion;
	}

	/**Establece el monto de la operación*/
	public void setComisionOperacion(BigDecimal comisionOperacion) {
		this.comisionOperacion = comisionOperacion;
	}	

	/**Devuelve el id de la moneda*/
	public String getIdMoneda() {
		return idMoneda;
	}

	/**Establece el id de la moneda*/
	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}

	/**Devuelve el id del título involucrado en el cálculo*/
	public String getIdTitulo() {
		return idTitulo;
	}

	/**Establece el id del título involucrado en el cálculo*/	
	public void setIdTitulo(String idTitulo) {
		this.idTitulo = idTitulo;
	}

	/**Devuelve el código de la operación*/
	public String getCodigoOperación() {
		return codigoOperacion;
	}

	/**Establece el código de la operación*/
	public void setCodigoOperación(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}

	/**Devuelve el id del cliente*/
	public long getIdCliente() {
		return idCliente;
	}

	/**Establece el id del cliente*/
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	/**Indica si el campo tasa_monto debe mostrarse con signo de porcentaje*/
	public boolean isSignoPorcentaje() {
		return signoPorcentaje;
	}

	/**Establece si el campo tasa_monto debe mostrarse con signo de porcentaje*/	
	public void setSignoPorcentaje(boolean signoPorcentaje) {
		this.signoPorcentaje = signoPorcentaje;
	}

	/**Devuelve la fecha de inicio del pago de cupón o de amortización*/
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**Establece la fecha de inicio del pago de cupón o de amortización*/	
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**Devuelve la fecha fin del pago de cupón o de amortización*/	
	public Date getFechaFin() {
		return fechaFin;
	}

	/**Establece la fecha fin del pago de cupón o de amortización*/	
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	/**Establece la cantidad de operaciones efectuadas*/
	public void setCantidadOperaciones(long cantidadOperaciones){
		this.cantidadOperaciones = cantidadOperaciones;
	}
	
	/**Retorna la cantidad de operaciones efectuadas*/
	public long getCantidadOperaciones(){
		return this.cantidadOperaciones;
	}

	/**Obtiene los días de cálculo*/
	public int getDiasCalculo() {
		return diasCalculo;
	}

	/**Establece los días de cálculo*/	
	public void setDiasCalculo(int diasCalculo) {
		this.diasCalculo = diasCalculo;
	}
	
	
}

