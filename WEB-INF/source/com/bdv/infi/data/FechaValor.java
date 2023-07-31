package com.bdv.infi.data;

import java.util.Date;

/**Representa una fecha valor usada dentro de los procesos de INFI.
 * Representa un registro de la tabla INFI_TB_046_FECHA_VALOR*/
public class FechaValor {
	
	/**Id de la fecha valor*/
	private int idFechaValor;
	
	/**Id del nombre referente a la descripción de la fecha valor*/
	private String nombre;
	
	/**Fecha establecida para el seteo de las operaciones*/
	private Date fechaValor = new Date();

	
	/**Devuelve el id del registro*/
	public int getIdFechaValor() {
		return idFechaValor;
	}

	/**Establece el id del registro*/
	public void setIdFechaValor(int idFechaValor) {
		this.idFechaValor = idFechaValor;
	}

	/**Devuelve la descripción referente a la fecha valor*/
	public String getNombre() {
		return nombre;
	}

	/**Establece el nombre o descripción de la fecha valor*/
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**Devuelve la fecha valor*/
	public Date getFechaValor() {
		return fechaValor;
	}

	/**Establece la fecha valor*/
	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}
}
