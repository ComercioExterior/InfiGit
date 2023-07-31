package com.bdv.infi.data;

import java.util.ArrayList;
import java.util.Date;

/** 
 * Documentos configurados para un tipo de transacci&oacute;n espec&iacute;fica
 */
public class TransaccionFinanciera extends com.bdv.infi.data.DataRegistro {

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
public String getNombre()
{
	return nombre;
}

/**
 * Setter of the property <tt>nombre</tt>
 *
 * @param nombre The nombre to set.
 *
 */
public void setNombre(String nombre ){
	this.nombre = nombre;
}

	/**
 * Descripci&oacute;n de la Transacci&oacute;n Financiera
 */
private String descripcion;
 
/**
 * Getter of the property <tt>descripcion</tt>
 *
 * @return Returns the descripcion.
 * 
 */
public String getDescripcion()
{
	return descripcion;
}

/**
 * Setter of the property <tt>descripcion</tt>
 *
 * @param descripcion The descripcion to set.
 *
 */
public void setDescripcion(String descripcion ){
	this.descripcion = descripcion;
}



/**
 * Valor aplicado a la Transacci&oacute;n Financiera
 */
private double valor;
 
/**
 * Getter of the property <tt>valor</tt>
 *
 * @return Returns the valor.
 * 
 */
public double getValor()
{
	return valor;
}

/**
 * Setter of the property <tt>valor</tt>
 *
 * @param valor The valor to set.
 *
 */
public void setValor(double valor ){
	this.valor = valor;
}

	/**
 * Tipo de aplicaci&oacute;n de la Transacci&oacute;n Financiera: Monto Fijo o Porcentaje 
 */
private String aplicacion;
 
/**
 * Getter of the property <tt>aplicacion</tt>
 *
 * @return Returns the aplicacion.
 * 
 */
public String getAplicacion()
{
	return aplicacion;
}

/**
 * Setter of the property <tt>aplicacion</tt>
 *
 * @param aplicacion The aplicacion to set.
 *
 */
public void setAplicacion(String aplicacion ){
	this.aplicacion = aplicacion;
}

	/**
 * Tipo de Transacci&oacute;n Financiera
 */
private String tipoTranscaccionFinanc;
 
/**
 * Getter of the property <tt>tipoTranscaccionFinanc</tt>
 *
 * @return Returns the tipoTranscaccionFinanc.
 * 
 */
public String getTipoTranscaccionFinanc()
{
	return tipoTranscaccionFinanc;
}

/**
 * Setter of the property <tt>tipoTranscaccionFinanc</tt>
 *
 * @param tipoTranscaccionFinanc The tipoTranscaccionFinanc to set.
 *
 */
public void setTipoTranscaccionFinanc(String tipoTranscaccionFinanc ){
	this.tipoTranscaccionFinanc = tipoTranscaccionFinanc;
}

	/**
 * Id de la Transacci&oacute;n Financiera
 */
private String idTransaccionFinanciera;
 
/**
 * Getter of the property <tt>idTransaccionFinanciera</tt>
 *
 * @return Returns the idTransaccionFinanciera.
 * 
 */
public String getIdTransaccionFinanciera()
{
	return idTransaccionFinanciera;
}

/**
 * Setter of the property <tt>idTransaccionFinanciera</tt>
 *
 * @param idTransaccionFinanciera The idTransaccionFinanciera to set.
 *
 */
public void setIdTransaccionFinanciera(String idTransaccionFinanciera ){
	this.idTransaccionFinanciera = idTransaccionFinanciera;
}

/**
 * Funci&oacute;n asociada a la Transacci&oacute;n Financiera
 */
private String funcion;
 
/**
 * Getter of the property <tt>funcion</tt>
 *
 * @return Returns the funcion.
 * 
 */
public String getFuncion()
{
	return funcion;
}

/**
 * Setter of the property <tt>funcion</tt>
 *
 * @param funcion The funci&oacute;n to set.
 *
 */
public void setFuncion(String funcion ){
	this.funcion = funcion;
}

/**
 * Indicador de Transacci&oacute;n Financiera de Comisi&oacute;n 
 * 
 */
private int indicadorComision;
 
/**
 * Getter of the property <tt>indicadorComision</tt>
 *
 * @return Returns the indicadorComision.
 * 
 */
public int getIndicadorComision()
{
	return indicadorComision;
}

/**
 * Setter of the property <tt>indicadorComision</tt>
 *
 * @param indicadorComision The indicadorComision to set.
 *
 */
public void setIndicadorComision(int indicadorComision ){
	this.indicadorComision = indicadorComision;
}

/**
 * Fecha de Aplicaci&oacute;n de la Transacci&oacute;n Financiera 
 * 
 */
private Date fechaAplicacion;
 
/**
 * Getter of the property <tt>fechaAplicacion</tt>
 *
 * @return Returns the fechaAplicacion.
 * 
 */
public Date getFechaAplicacion()
{
	return fechaAplicacion;
}

/**
 * Setter of the property <tt>fechaAplicacion</tt>
 *
 * @param fechaAplicacion The fechaAplicacion to set.
 *
 */
public void setFechaAplicacion(Date fechaAplicacion ){
	this.fechaAplicacion = fechaAplicacion;
}

/**
 * Estatus de la Transacci&oacute;n Financiera 
 * 
 */
private int statusTransaccionFinanc;
 
/**
 * Getter of the property <tt>statusTransaccionFinanc</tt>
 *
 * @return Returns the statusTransaccionFinanc.
 * 
 */
public int getStatusTransaccionFinanc()
{
	return statusTransaccionFinanc;
}

/**
 * Setter of the property <tt>statusTransaccionFinanc</tt>
 *
 * @param statusTransaccionFinanc  The statusTransaccionFinanc to set.
 *
 */
public void setStatusTransaccionFinanc(int statusTransaccionFinanc ){
	this.statusTransaccionFinanc = statusTransaccionFinanc;
}

/**
 * Lista de reglas asociadas a una transacci&oacute;n financiera.
 */
private ArrayList reglas = new ArrayList();

/**Verifica si la lista de reglas est&aacute; vac&iacute;a*/
public boolean isReglasVacio(){
	return reglas.isEmpty();
}

/**Agrega una regla de transacci&oacute;n Financiera*/
public boolean agregarRegla(ReglaTransaccionFinanciera reglaTransaccionFinanciera){
	return this.reglas.add(reglaTransaccionFinanciera);
}

/**
 * Getter of the property <tt>reglas</tt>
 *
 * @return Returns the reglas.
 * 
 */
public ArrayList getReglas()
{
	return reglas;
}
/**
 * Setter of the property <tt>reglas</tt>
 *
 * @param reglas The reglas to set.
 *
 */
public void setReglas(ArrayList reglas ){
	this.reglas = reglas;
}


}
