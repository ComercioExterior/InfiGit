package com.bdv.infi.data;

import java.util.Date;

/** 
 * Entidad de bloqueo de t&iacute;tulos en custodia
 */
public class CustodiaBloqueo extends com.bdv.infi.data.DataRegistro {

	/*
 * (non-javadoc)
 */
private Custodia custodia;
 
/**
 * Getter of the property <tt>custodia</tt>
 *
 * @return Returns the custodia.
 * 
 */
public Custodia getCustodia()
{
	return custodia;
}

/**
 * Setter of the property <tt>custodia</tt>
 *
 * @param custodia The custodia to set.
 *
 */
public void setCustodia(Custodia custodia ){
	this.custodia = custodia;
}

/*
 * (non-javadoc)
 */
private Date fecha;
 
/**
 * Getter of the property <tt>fecha</tt>
 *
 * @return Returns the fecha.
 * 
 */
public Date getFecha()
{
	return fecha;
}

/**
 * Setter of the property <tt>fecha</tt>
 *
 * @param fecha The fecha to set.
 *
 */
public void setFecha(Date fecha ){
	this.fecha = fecha;
}

	/**
 * Id del t&iacute;tulo
 */
private String idTitulo;
 
/**
 * Getter of the property <tt>idTitulo</tt>
 *
 * @return Returns the idTitulo.
 * 
 */
public String getIdTitulo()
{
	return idTitulo;
}

/**
 * Setter of the property <tt>idTitulo</tt>
 *
 * @param idTitulo The idTitulo to set.
 *
 */
public void setIdTitulo(String idTitulo ){
	this.idTitulo = idTitulo;
}

	/**
 * Indica la cantidad que se encuentra bloqueada
 */
private int cantidad;
 
/**
 * Getter of the property <tt>cantidad</tt>
 *
 * @return Returns the cantidad.
 * 
 */
public int getCantidad()
{
	return cantidad;
}

/**
 * Setter of the property <tt>cantidad</tt>
 *
 * @param cantidad The cantidad to set.
 *
 */
public void setCantidad(int cantidad ){
	this.cantidad = cantidad;
}

	/**
 * Tipo de garant&iacute;a aplicada
 */
private String tipoGarantia;
 
/**
 * Getter of the property <tt>tipoGarantia</tt>
 *
 * @return Returns the tipoGarantia.
 * 
 */
public String getTipoGarantia()
{
	return tipoGarantia;
}

/**
 * Setter of the property <tt>tipoGarantia</tt>
 *
 * @param tipoGarantia The tipoGarantia to set.
 *
 */
public void setTipoGarantia(String tipoGarantia ){
	this.tipoGarantia = tipoGarantia;
}

	/**
 * Tipo de bloqueo aplicado al t&iacute;tulo
 */
private String tipoBloqueo;
 
/**
 * Getter of the property <tt>tipoBloqueo</tt>
 *
 * @return Returns the tipoBloqueo.
 * 
 */
public String getTipoBloqueo()
{
	return tipoBloqueo;
}

/**
 * Setter of the property <tt>tipoBloqueo</tt>
 *
 * @param tipoBloqueo The tipoBloqueo to set.
 *
 */
public void setTipoBloqueo(String tipoBloqueo ){
	this.tipoBloqueo = tipoBloqueo;
}
}
