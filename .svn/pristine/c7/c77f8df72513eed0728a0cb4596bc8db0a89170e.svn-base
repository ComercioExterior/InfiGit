package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa los bloter asociados a una unidad de inversi&oacute;n
 **/
public class UIBlotter {
	
	/**
	 * Identificador de la Unidad de Inversion
	 */
	private long idUnidadInversion;
	/**
	 * Identificador del Blotter
	 */
	private String idBlotter;	
	/**
	 * Porcentaje de ganancia de la red 
	 */
	private BigDecimal gananciaRed = new BigDecimal(0); 
	/**
	 * Horario de inicio del servicio del blotter en la UI
	 * Se debe tomar como base el horario desde del Blotter
	 */
	private String horarioDesde;
	/**
	 * Horario de final del servicio del blotter en la UI
	 * Se debe tomar como base el horario hasta del Blotter
	 */
	private String horarioHasta;	
	/**
	 * Horario de inicio del servicio del blotter en la UI para el ultimo dia 
	 */
	private String horarioDesdeUltDia;
	/**
	 * Horario de final del servicio del blotter en la UI  para el ultimo dia 
	 */
	private String horarioHastaUltDia;		
	/**
	 * Fecha de inicio de Toma de Ordenes
	 * Se debe tomar como base la Fecha de emision de la Unidad de Inversion 
	 */
	private Date periodoInicial;
	/**
	 * Fecha de inicio de Toma de Ordenes
	 * Se debe tomar como base la Fecha de cierre de la Unidad de Inversion
	 */
	private Date periodoFinal;	
	/**
	 * Indicadores de uso de la asociacion
	 */
	private int indicaDisponible = 1;
	private int indicaTesoreria = 0;
	
	/**
	 * Indicador de blotter por defecto
	 */
	private int indicaBlotterDefecto = 0;
	
	/**
	 * Indicador de recompra permitida
	 */
	private int indicaRecompra;
	
	/**
	 * Descripcion del Blotter
	 */
	private String descripcionBlotter ="";
	
	/**
	 * Indicador de Blotter restringido
	 */
	private int indicaRestringido = 0;
	
	public String getDescripcionBlotter() {
		return descripcionBlotter;
	}

	public void setDescripcionBlotter(String descripcionBlotter) {
		this.descripcionBlotter = descripcionBlotter;
	}

	public int getIndicaRestringido() {
		return indicaRestringido;
	}

	public void setIndicaRestringido(int indicaRestringido) {
		this.indicaRestringido = indicaRestringido;
	}

	/**
	 * Retorna el indicador de pacto de recompra
	 * @return
	 */
	public int getIndicaRecompra() {
		return indicaRecompra;
	}
	
	/**
	 * Asigna el valor del indicador de pacto de recompra
	 * @param indicaRecompra
	 */
	public void setIndicaRecompra(int indicaRecompra) {
		this.indicaRecompra = indicaRecompra;
	}
	/**
	 * Retorna el valor del atributo Identificador de la Unidad de Inversion
	 * @return
	 */
	public long getIdUnidadInversion() {
		return idUnidadInversion;
	}
	/**
	 * Asigna valor al atributo Identificador de la Unidad de Inversion
	 * @param idUnidadInversion
	 */	
	public void setIdUnidadInversion(long idUnidadInversion) {
		this.idUnidadInversion = idUnidadInversion;
	}
	
	/**
	 * Retorna el valor del atributo Identificador ddel Blotter
	 * @return
	 */
	public String getIdBlotter() {
		return idBlotter;
	}
	/**
	 * Asigna valor al atributo Identificador del Blotter
	 * @param idBlotter
	 */	
	public void setIdBlotter(String idBlotter) {
		this.idBlotter = idBlotter;
	}
	
	
	/**
	 * Retorna el valor del atributo Porcentaje de ganancia de la red 
	 * @return
	 */
	public BigDecimal getGananciaRed() {
		return gananciaRed;
	}
	/**
	 * Asigna valor al atributo Porcentaje de ganancia de la red 
	 * @param idUnidadInversion
	 */	
	public void setGananciaRed(BigDecimal gananciaRed) {
		this.gananciaRed = gananciaRed;
	}
	

	/**
	 * Retorna el valor del atributo Horario de inicio del servicio del blotter
	 * @return
	 */
	public String getHorarioDesde() {
		return horarioDesde;
	}
	/**
	 * Asigna valor al atributo Horario de inicio del servicio del blotter
	 * @param horarioDesde
	 */	
	public void setHorarioDesde(String horarioDesde) {
		this.horarioDesde = horarioDesde;
	}
	
	/**
	 * Retorna el valor del atributo Horario de final del servicio del blotter
	 * @return
	 */
	public String getHorarioHasta() {
		return horarioHasta;
	}
	/**
	 * Asigna valor al atributo Horario de final del servicio del blotter
	 * @param horarioHasta
	 */	
	public void setHorarioHasta(String horarioHasta) {
		this.horarioHasta = horarioHasta;
	}
	
	
	/**
	 * Retorna el valor del atributo Horario de inicio del servicio del blotter el ultimo dia
	 * @return
	 */
	public String getHorarioDesdeUltDia() {
		return horarioDesdeUltDia;
	}
	/**
	 * Asigna valor al atributo Horario de inicio del servicio del blotter el ultimo dia
	 * @param horarioDesdeUltDia
	 */	
	public void setHorarioDesdeUltDia(String horarioDesdeUltDia) {
		this.horarioDesdeUltDia = horarioDesdeUltDia;
	}
	
	/**
	 * Retorna el valor del atributo Horario de final del servicio del blotter el ultimo dia
	 * @return
	 */
	public String getHorarioHastaUltDia() {
		return horarioHastaUltDia;
	}
	/**
	 * Asigna valor al atributo Horario de final del servicio del blotter el ultimo dia
	 * @param horarioHastaUltDia
	 */	
	public void setHorarioHastaUltDia(String horarioHastaUltDia) {
		this.horarioHastaUltDia = horarioHastaUltDia;
	}
	
	/**
	 * Retorna el valor del atributo Fecha de inicio de Toma de Ordenes
	 * @return
	 */
	public Date getPeriodoInicial() {
		return periodoInicial;
	}
	/**
	 * Asigna valor al atributo Fecha de inicio de Toma de Ordenes
	 * @param periodoInicial
	 */	
	public void setPeriodoInicial(Date periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	
	/**
	 * Retorna el valor del atributo Fecha de final de Toma de Ordenes
	 * @return
	 */
	public Date getPeriodoFinal() {
		return periodoFinal;
	}
	/**
	 * Asigna valor al atributo Fecha de final de Toma de Ordenes
	 * @param periodoFinal
	 */	
	public void setPeriodoFinal(Date periodoFinal) {
		this.periodoFinal = periodoFinal;
	}
	
	/**
	 * Retorna el valor del atributo Indicador de uso de la asociacion
	 * @return
	 */
	public int getIndicaDisponible() {
		return indicaDisponible;
	}
	/**
	 * Asigna valor al atributo Indicador de uso de la asociacion
	 * @param indicaDisponible
	 */	
	public void setIndicaDisponible(int indicaDisponible) {
		this.indicaDisponible = indicaDisponible;
	}
	
	/**
	 * Retorna el valor del atributo Indicador de uso Tesoreria
	 * @return
	 */
	public int getIndicaTesoreria() {
		return indicaTesoreria;
	}
	/**
	 * Asigna valor al atributo Indicador de uso Tesoreria
	 * @param indicaTesoreria
	 */	
	public void setIndicaTesoreria(int indicaTesoreria) {
		this.indicaTesoreria = indicaTesoreria;
	}
	
	/**
	 * Verifica si e bloter tiene la permisolog&iacute;a en cuanto al per&iacute;odo de fechas se refiere 
	 * para la toma de una orden
	 * @return verdadero en caso de tener permiso
	 * */
	public boolean comprobarFechaToma(){
		return false;
	}

	public int getIndicaBlotterDefecto() {
		return indicaBlotterDefecto;
	}

	public void setIndicaBlotterDefecto(int indicaBlotterDefecto) {
		this.indicaBlotterDefecto = indicaBlotterDefecto;
	}
}
