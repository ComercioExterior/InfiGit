package com.bdv.infi.data;

import java.util.ArrayList;

/**
 * Documentos configurados para un tipo de transacci&oacute;n espec&iacute;fica
 */
@SuppressWarnings("serial")
public class UIComision extends com.bdv.infi.data.DataRegistro {

	/**
	 * Nombre de la Transacci&oacute;n Financiera
	 */
	private String nombre;

	/**
	 * Getter of the property <tt>nombre</tt>
	 * 
	 * @return Returns the nombre.
	 * 
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Setter of the property <tt>nombre</tt>
	 * 
	 * @param nombre
	 *            The nombre to set.
	 * 
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Monto Fijo aplicado a la Comisi&oacute;n
	 */
	private double montoFijo = 0;

	/**
	 * Porcentaje aplicado a la Comisi&oacute;n
	 */
	private double porcentaje = 0;

	/**
	 * Id de la Comisi&oacute;n
	 */
	private long idComision;

	/**
	 * Id de la unidad de inversi&oacute;n a la cual esta asociada la comisi&oacute;n
	 */
	private long idUnidadInversion;

	/**
	 * C&oacute;digo de la operaci&oacute;n
	 */

	private String codigoOperacion;

	/**
	 * Lista de reglas asociadas a una transacci&oacute;n financiera.
	 */
	private ArrayList<ReglaUIComision> reglas = new ArrayList<ReglaUIComision>();

	/** TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015
	 * Idica si la comision aplica para para Electronico o Efectivo. 1 = Electronico, 2= Efectivo	 */
	
	private int tipoComision; 
	
	/** Verifica si la lista de reglas est&aacute; vac&iacute;a */
	public boolean isReglasVacio() {
		return reglas.isEmpty();
	}

	/** Agrega una regla de transacci&oacute;n Financiera */
	public boolean agregarRegla(ReglaUIComision reglaUIComision) {
		return this.reglas.add(reglaUIComision);
	}

	/**
	 * Getter of the property <tt>reglas</tt>
	 * 
	 * @return Returns the reglas.
	 * 
	 */
	public ArrayList<ReglaUIComision> getReglas() {
		return reglas;
	}

	/**
	 * Setter of the property <tt>reglas</tt>
	 * 
	 * @param reglas
	 *            The reglas to set.
	 * 
	 */
	public void setReglas(ArrayList<ReglaUIComision> reglas) {
		this.reglas = reglas;
	}

	public long getIdComision() {
		return idComision;
	}

	public void setIdComision(long idComision) {
		this.idComision = idComision;
	}

	public double getMontoFijo() {
		return montoFijo;
	}

	public void setMontoFijo(double montoFijo) {
		this.montoFijo = montoFijo;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public long getIdUnidadInversion() {
		return idUnidadInversion;
	}

	public void setIdUnidadInversion(long idUnidadInversion) {
		this.idUnidadInversion = idUnidadInversion;
	}

	public String getCodigoOperacion() {
		return codigoOperacion;
	}

	public void setCodigoOperacion(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}

	public int getTipoComision() {
		return tipoComision;
	}

	public void setTipoComision(int tipoComision) {
		this.tipoComision = tipoComision;
	}

}
