package com.bdv.infi.data;


public class PlantillaMailArea {
	
	/**
	 * Id del area de la plantilla de correo
	 */	
	private int plantMailAreaId;
	
	/**
	 * Nombre del area de la plantilla de correo
	 */
	private String plantMailAreaName;
	
	/**
	 * Destinatario del correo
	 */
	private String destinatario;
	
	/**
	 * Id de la plantilla asociada al area
	 */
	private int plantillaMailId;
		
	/**
	 * Estatus de activaci&oacute;n del area
	 */	
	private int estatusActivacion;
	
	/**
	 * Obtiene el Id del area de la plantilla de correo
	 */
	public int getPlantMailAreaId() {
		return plantMailAreaId;
	}
	
	/**
	 * Asigna el Id del area de la plantilla de correo
	 */
	public void setPlantMailAreaId(int plantMailAreaId) {
		this.plantMailAreaId = plantMailAreaId;
	}
	
	/**
	 * Obtiene el nombre del area de la plantilla de correo
	 */
	public String getPlantMailAreaName() {
		return plantMailAreaName;
	}
	
	/**
	 * Asigna el nombre del area de la plantilla de correo
	 */
	public void setPlantMailAreaName(String plantMailAreaName) {
		this.plantMailAreaName = plantMailAreaName;
	}
	
	/**
	 * Obtiene el destinatario del correo
	 */
	public String getDestinatario() {
		return destinatario;
	}
	
	/**
	 * Asigna el destinatario del correo
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	/**
	 * Obtiene el Id de la plantilla de correo asociada al area
	 */
	public int getPlantillaMailId() {
		return plantillaMailId;
	}
	
	/**
	 * Asigna el Id de la plantilla de correo asociada al area
	 */
	public void setPlantillaMailId(int plantillaMailId) {
		this.plantillaMailId = plantillaMailId;
	}
	
	/**
	 * Obtiene el estatus de aprobaci&oacute;n del area
	 */
	public int getEstatusActivacion() {
		return estatusActivacion;
	}
	
	/**
	 * Asigna el estatus de aprobaci&oacute;n del area
	 */
	public void setEstatusActivacion(int estatusActivacion) {
		this.estatusActivacion = estatusActivacion;
	}
	
}
