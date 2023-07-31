package com.bdv.infi.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/**
 * Clase data para la tabla 810
 */
public class ProcesoGestion implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
 * Indica si el el proceso que se esta generando viene de Pacto de Recompra
 */
	private boolean isRecompra;
/**
 * Id del proceso
 */
	private long procesoID;
/**
 * Id del cliente
 */
	private long clientId;
/**
 * Fecha de registro
 */
	private Date fechaRegistro;
/**
 * id del usuario
 */
	private long usuarioId;
	
	
/**
 * ArrayList que contiene las instrucciones de pago definidas para un proceso
 */
	private ArrayList<InstruccionesPago> instruccionesPago = new ArrayList<InstruccionesPago>();
/**
 * ArrayList que contiene las operaciones financieras involucradas en un proceso de gesti&oacute;n de pago
 */
	private ArrayList operaciones = new ArrayList();
/**
 * Agrega una instrucci&oacute;n de pago al ArrayList
 */
	@SuppressWarnings("unused")
	public void agregarInstruccion(InstruccionesPago instruccionesPago){
		this.instruccionesPago.add(instruccionesPago);
	}
/**
 * Agrega una operacion de pago al ArrayList
 */
	@SuppressWarnings("unused")
	public void agregarOperacion(long operacion){
		this.operaciones.add(operacion);
	}
/**
 * @return the clientId
 */

public long getClientId() {
	return clientId;
}
/**
 * @param clientId the clientId to set
 */
public void setClientId(long clientId) {
	this.clientId = clientId;
}
/**
 * @return the fechaRegistro
 */
public Date getFechaRegistro() {
	return fechaRegistro;
}
/**
 * @param fechaRegistro the fechaRegistro to set
 */
public void setFechaRegistro(Date fechaRegistro) {
	this.fechaRegistro = fechaRegistro;
}
/**
 * @return the procesoID
 */
public long getProcesoID() {
	return procesoID;
}
/**
 * @param procesoID the procesoID to set
 */
public void setProcesoID(long procesoID) {
	this.procesoID = procesoID;
}
/**
 * @return the usuarioId
 */
public long getUsuarioId() {
	return usuarioId;
}
/**
 * @param usuarioId the usuarioId to set
 */
public void setUsuarioId(long usuarioId) {
	this.usuarioId = usuarioId;
}
/**
 * @return the instruccionesPago
 */
public ArrayList<InstruccionesPago> getInstruccionesPago() {
	return instruccionesPago;
}
/**
 * @param instruccionesPago the instruccionesPago to set
 */
public void setInstruccionesPago(ArrayList<InstruccionesPago> instruccionesPago) {
	this.instruccionesPago = instruccionesPago;
}
/**
 * @return the operaciones
 */
public ArrayList getOperaciones() {
	return operaciones;
}
/**
 * @param operaciones the operaciones to set
 */
public void setOperaciones(ArrayList operaciones) {
	this.operaciones = operaciones;
}
/**
 * @return the isRecompra
 */
public boolean isRecompra() {
	return isRecompra;
}
/**
 * @param isRecompra the isRecompra to set
 */
public void setRecompra(boolean isRecompra) {
	this.isRecompra = isRecompra;
}
}
