package com.bdv.infi.data;

import java.util.ArrayList;
import java.util.Date;

/** 
 * Clase que representa la informaci&oacute;n principal en el proceso de una generaci&oacute;n de archivos
 */
public class Archivo {

	/**
 * Status del proceso
 */
private String status;

/***
 * 
 * Id del ciclo al que esta asociado el control de archivo
 */
 private long idCiclo;
/**
 * Getter of the property <tt>status</tt>
 *
 * @return Returns the status.
 * 
 */
public String getStatus()
{
	return status;
}

/**
 * Setter of the property <tt>status</tt>
 *
 * @param status The status to set.
 *
 */
public void setStatus(String status ){
	this.status = status;
}

/**
 * Fecha del registro generado
 */
private Date fecha;

/**
 * Fecha de cierre de la generaci&oacute;n o recepci&oacute;n de archivo
 */
private Date fechaCierre;

/**Id de ejecuci&oacute;n relacionado a otro proceso. 
 * Generalmente es visto en los procesos de recepci&oacute;n de archivos.*/
private long idEjecucionRelacion;

/**
 * Nombre del archivo generado
 */
private String nombreArchivo;
 
/**
 * Getter of the property <tt>nombreArchivo</tt>
 *
 * @return Returns the nombreArchivo.
 * 
 */
public String getNombreArchivo()
{
	return nombreArchivo;
}

/**
 * Setter of the property <tt>nombreArchivo</tt>
 *
 * @param nombreArchivo The nombreArchivo to set.
 *
 */
public void setNombreArchivo(String nombreArchivo ){
	this.nombreArchivo = nombreArchivo;
}

	/**
 * Id de ejecuci&oacute;n
 */
private long idEjecucion;
 
/**
 * Getter of the property <tt>idEjecucion</tt>
 *
 * @return Returns the idEjecucion.
 * 
 */
public long getIdEjecucion()
{
	return idEjecucion;
}

/**
 * Setter of the property <tt>idEjecucion</tt>
 *
 * @param idEjecucion The idEjecucion to set.
 *
 */
public void setIdEjecucion(long idEjecucion ){
	this.idEjecucion = idEjecucion;
}

	/**
 *
 */
private ArrayList detalle = new ArrayList();

/**
 * Getter of the property <tt>detalle</tt>
 *
 * @return Returns the detalle.
 * 
 */
public ArrayList getDetalle()
{
	return detalle;
}
 
/**
 * Returns <tt>true</tt> if this collection contains no elements.
 *
 * @return <tt>true</tt> if this collection contains no elements
 * @see	java.util.Collection#isEmpty()
 *
 */
public boolean isDetalleEmpty(){
	return detalle.isEmpty();
}

/**
 * Ensures that this collection contains the specified element (optional
 * operation). 
 *
 * @param element whose presence in this collection is to be ensured.
 * @see	java.util.Collection#add(Object)
 *
 */
public boolean agregarDetalle(Detalle detalle){
	return this.detalle.add(detalle);
}

/**
 * Elimina todos los detalles asociados
 */
public void reiniciarDetalle(){
	this.detalle.clear(); 
}

/**Veh&iacute;culo que se selecciono para la generaci&oacute;n del archivo.
 * No es un valor obligatorio, solo aplica para el archivo
 * hacia el Banco Central de Venezuela*/
private String vehiculoId;

public String getVehiculoId() {
	return vehiculoId;
}

public void setVehiculoId(String vehiculoId) {
	this.vehiculoId = vehiculoId;
}

/**
 * @return la variable fechaGeneracion
 */
public Date getFechaGeneracion() {
	return fecha;
}

/**
 * @param fechaGeneracion establece la variable fechaGeneracion
 */
public void setFechaGeneracion(Date fechaGeneracion) {
	this.fecha = fechaGeneracion;
}

/**
 * @return la variable fechaCierreGeneracion
 */
public Date getFechaCierreGeneracion() {
	return fechaCierre;
}

/**
 * @param fechaCierreGeneracion establece la variable fechaCierreGeneracion
 */
public void setFechaCierreGeneracion(Date fechaCierreGeneracion) {
	this.fechaCierre = fechaCierreGeneracion;
}

/**
 * @return la variable idEjecucionRelacion
 */
public long getIdEjecucionRelacion() {
	return idEjecucionRelacion;
}

/**
 * @param idEjecucionRelacion establece la variable idEjecucionRelacion
 */
public void setIdEjecucionRelacion(long idEjecucionRelacion) {
	this.idEjecucionRelacion = idEjecucionRelacion;
}

public long unidadInv;

public long getUnidadInv() {
	return unidadInv;
}

public void setUnidadInv(long unidadInv) {
	this.unidadInv = unidadInv;
}

public int inRecepcion;

public int getInRecepcion() {
	return inRecepcion;
}

public void setInRecepcion(int inRecepcion) {
	this.inRecepcion = inRecepcion;
}

private String usuario;

public String getUsuario() {
	return usuario;
}

public void setUsuario(String usuario) {
	this.usuario = usuario;
}

public long getIdCiclo() {
	return idCiclo;
}

public void setIdCiclo(long idCiclo) {
	this.idCiclo = idCiclo;
}


}
