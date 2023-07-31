package com.bdv.infi.data;

import java.util.Date;

/**Clase para el manejo de fechas de cierre. Representa un registro en la tabla*/
public class FechasCierre {
	
	/**Fecha de cierre del mes anterior*/
	private Date fechaCierreAnterior;
	
	/**Fecha de cierre del próximo mes*/
	private Date fechaCierreProximo;

	/**Obtiene fecha del mes anterior*/
	public Date getFechaCierreAnterior() {
		return fechaCierreAnterior;
	}

	/**Setea fecha de mes anterior*/
	public void setFechaCierreAnterior(Date fechaCierreAnterior) {
		this.fechaCierreAnterior = fechaCierreAnterior;
	}

	/**Obtiene fecha del próximo cierre*/
	public Date getFechaCierreProximo() {
		return fechaCierreProximo;
	}

	/**Setea fecha del próximo cierre*/
	public void setFechaCierreProximo(Date fechaCierreProximo) {
		this.fechaCierreProximo = fechaCierreProximo;
	}

	
}
