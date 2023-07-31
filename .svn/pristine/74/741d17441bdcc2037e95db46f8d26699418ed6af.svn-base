package com.bdv.infi.data;

import java.util.Date;

/**Clase que representa un registro de la tabla INFI_TB_814_CALC_MES 
 * donde se almacena el resultado del cálculo de las comisiones que
 * deben cobrarse a un cliente o el resultado del pago de cupones 
 * y amortizaciones que debe cancelarse a un cliente */
public class CalculoMes {
	
	/**Id que hace referencia al cálculo del mes*/
	private long idCalculoMes;
	
	/**Id del usuario que efectúa la operación*/
	private long idUsuario;
	
	/**Fecha desde que se inicia la consulta*/
	private Date fechaDesde = new Date();
	
	/**Fecha hasta que finaliza la consulta*/
	private Date fechaHasta = new Date();
	
	/**Id de la transacción involucrada*/
	private String idTransaccion = "";
	
	/**Fecha en que se generó la consulta*/
	private Date fechaConsulta = null;
		
	/**Devuelve el id referente al cálculo del mes*/
	public long getIdCalculoMes() {
		return idCalculoMes;
	}

	/**Setea el id referente al cálculo del mes*/
	public void setIdCalculoMes(long idCalculoMes) {
		this.idCalculoMes = idCalculoMes;
	}

	/**Devulve el id del usuario que efectúa la consulta*/
	public long getIdUsuario() {
		return idUsuario;
	}

	/**Setea el id del usuario que efectúa la consulta*/
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**Devuelve la fecha de inicio de la consulta*/
	public Date getFechaDesde() {
		return fechaDesde;
	}

	/**Setea la fecha de inicio de consulta*/
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	/**Devuelve la fecha de fin de consulta*/
	public Date getFechaHasta() {
		return fechaHasta;
	}

	/**Setea la fecha de fin de consulta*/
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	/**Devuelve el id de la transacción de negocio consultada*/
	public String getIdTransaccion() {
		return idTransaccion;
	}

	/**Setea el id de la transacción de negocio consultada*/
	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	
	/**Devuelve la fecha en que se generó la consulta*/
	public Date getFechaConsulta(){
		return this.fechaConsulta;
	}
}

