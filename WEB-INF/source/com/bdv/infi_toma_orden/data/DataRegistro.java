package com.bdv.infi_toma_orden.data;

import java.io.Serializable;
import java.sql.Timestamp;

/** 
 * Clase que contiene los atributos que se registran en una tabla para conocer el usuario que efectúa una operación sobre ella
 */
public class DataRegistro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5619562616346132086L;
	/**
 * Rol del usuario que actualiza el registro
 */
private String actUsuarioRolNombre;
 
/**
 * Getter of the property <tt>actUsuarioRolNombre</tt>
 *
 * @return Returns the actUsuarioRolNombre.
 * 
 */
public String getActUsuarioRolNombre()
{
	return actUsuarioRolNombre;
}

/**
 * Setter of the property <tt>actUsuarioRolNombre</tt>
 *
 * @param actUsuarioRolNombre The actUsuarioRolNombre to set.
 *
 */
public void setActUsuarioRolNombre(String actUsuarioRolNombre ){
	this.actUsuarioRolNombre = actUsuarioRolNombre;
}

	/**
 * Nombre del usuario que actualiza el registro
 */
private String actUsuarioNombre;
 
/**
 * Getter of the property <tt>actUsuarioNombre</tt>
 *
 * @return Returns the actUsuarioNombre.
 * 
 */
public String getActUsuarioNombre()
{
	return actUsuarioNombre;
}

/**
 * Setter of the property <tt>actUsuarioNombre</tt>
 *
 * @param actUsuarioNombre The actUsuarioNombre to set.
 *
 */
public void setActUsuarioNombre(String actUsuarioNombre ){
	this.actUsuarioNombre = actUsuarioNombre;
}

	/**
 * Id del usuario que actualiza el registro
 */
private String actUsuarioId;
 
/**
 * Getter of the property <tt>actUsuarioId</tt>
 *
 * @return Returns the actUsuarioId.
 * 
 */
public String getActUsuarioId()
{
	return actUsuarioId;
}

/**
 * Setter of the property <tt>actUsuarioId</tt>
 *
 * @param actUsuarioId The actUsuarioId to set.
 *
 */
public void setActUsuarioId(String actUsuarioId ){
	this.actUsuarioId = actUsuarioId;
}

/*
 * (non-javadoc)
 */
private String actIp;
 
/**
 * Getter of the property <tt>actIp</tt>
 *
 * @return Returns the actIp.
 * 
 */
public String getActIp()
{
	return actIp;
}

/**
 * Setter of the property <tt>actIp</tt>
 *
 * @param actIp The actIp to set.
 *
 */
public void setActIp(String actIp ){
	this.actIp = actIp;
}

/*
 * (non-javadoc)
 */
private String actTerminal;
 
/**
 * Getter of the property <tt>actTerminal</tt>
 *
 * @return Returns the actTerminal.
 * 
 */
public String getActTerminal()
{
	return actTerminal;
}

/**
 * Setter of the property <tt>actTerminal</tt>
 *
 * @param actTerminal The actTerminal to set.
 *
 */
public void setActTerminal(String actTerminal ){
	this.actTerminal = actTerminal;
}

/**
 * Fecha de actualización del registro
 */
private Timestamp actFechaHora;
 
/**
 * Getter of the property <tt>actFechaHora</tt>
 *
 * @return Returns the actFechaHora.
 * 
 */
public Timestamp getActFechaHora()
{
	return actFechaHora;
}

/**
 * Setter of the property <tt>actFechaHora</tt>
 *
 * @param actFechaHora The actFechaHora to set.
 *
 */
public void setActFechaHora(Timestamp actFechaHora ){
	this.actFechaHora = actFechaHora;
}

/*
 * (non-javadoc)
 */
private String actEstacion;
 
/**
 * Getter of the property <tt>actEstacion</tt>
 *
 * @return Returns the actEstacion.
 * 
 */
public String getActEstacion()
{
	return actEstacion;
}

/**
 * Setter of the property <tt>actEstacion</tt>
 *
 * @param actEstacion The actEstacion to set.
 *
 */
public void setActEstacion(String actEstacion ){
	this.actEstacion = actEstacion;
}

	/**
 * Fecha y hora de ingreso del registro
 */
private Timestamp inFechaHora;
 
/**
 * Getter of the property <tt>inFechaHora</tt>
 *
 * @return Returns the inFechaHora.
 * 
 */
public Timestamp getInFechaHora()
{
	return inFechaHora;
}

/**
 * Setter of the property <tt>inFechaHora</tt>
 *
 * @param inFechaHora The inFechaHora to set.
 *
 */
public void setInFechaHora(Timestamp inFechaHora ){
	this.inFechaHora = inFechaHora;
}

/*
 * (non-javadoc)
 */
private String inTerminal;
 
/**
 * Getter of the property <tt>inTerminal</tt>
 *
 * @return Returns the inTerminal.
 * 
 */
public String getInTerminal()
{
	return inTerminal;
}

/**
 * Setter of the property <tt>inTerminal</tt>
 *
 * @param inTerminal The inTerminal to set.
 *
 */
public void setInTerminal(String inTerminal ){
	this.inTerminal = inTerminal;
}

/*
 * (non-javadoc)
 */
private String inIp;
 
/**
 * Getter of the property <tt>inIp</tt>
 *
 * @return Returns the inIp.
 * 
 */
public String getInIp()
{
	return inIp;
}

/**
 * Setter of the property <tt>inIp</tt>
 *
 * @param inIp The inIp to set.
 *
 */
public void setInIp(String inIp ){
	this.inIp = inIp;
}

/*
 * (non-javadoc)
 */
private String inEstacion;
 
/**
 * Getter of the property <tt>inEstacion</tt>
 *
 * @return Returns the inEstacion.
 * 
 */
public String getInEstacion()
{
	return inEstacion;
}

/**
 * Setter of the property <tt>inEstacion</tt>
 *
 * @param inEstacion The inEstacion to set.
 *
 */
public void setInEstacion(String inEstacion ){
	this.inEstacion = inEstacion;
}

	/**
 * Rol del usuario
 */
private String inUsuarioRolNombre;
 
/**
 * Getter of the property <tt>inUsuarioRolNombre</tt>
 *
 * @return Returns the inUsuarioRolNombre.
 * 
 */
public String getInUsuarioRolNombre()
{
	return inUsuarioRolNombre;
}

/**
 * Setter of the property <tt>inUsuarioRolNombre</tt>
 *
 * @param inUsuarioRolNombre The inUsuarioRolNombre to set.
 *
 */
public void setInUsuarioRolNombre(String inUsuarioRolNombre ){
	this.inUsuarioRolNombre = inUsuarioRolNombre;
}

	/**
 * Nombre del usuario que crea el registro
 */
private String inUsuarioNombre;
 
/**
 * Getter of the property <tt>inUsuarioNombre</tt>
 *
 * @return Returns the inUsuarioNombre.
 * 
 */
public String getInUsuarioNombre()
{
	return inUsuarioNombre;
}

/**
 * Setter of the property <tt>inUsuarioNombre</tt>
 *
 * @param inUsuarioNombre The inUsuarioNombre to set.
 *
 */
public void setInUsuarioNombre(String inUsuarioNombre ){
	this.inUsuarioNombre = inUsuarioNombre;
}

	/**
 * Id del usuario que crea el registro
 */
private String inUsuarioId;
 
/**
 * Getter of the property <tt>inUsuarioId</tt>
 *
 * @return Returns the inUsuarioId.
 * 
 */
public String getInUsuarioId()
{
	return inUsuarioId;
}

/**
 * Setter of the property <tt>inUsuarioId</tt>
 *
 * @param inUsuarioId The inUsuarioId to set.
 *
 */
public void setInUsuarioId(String inUsuarioId ){
	this.inUsuarioId = inUsuarioId;
}

}