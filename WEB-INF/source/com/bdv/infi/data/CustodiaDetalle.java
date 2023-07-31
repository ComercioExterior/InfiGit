package com.bdv.infi.data;

import java.util.Date;

/**Clase usada para mostrar un poco m&aacute;s de informaci&oacute;n en los t&iacute;tulos de custodia. Descripci&oacute;n de monedas, descripci&oacute;n de bloqueos y de garant&iacute;as*/

public class CustodiaDetalle extends Custodia {

	/**
 * Identificador especial del t&iacute;tulo
 */
private String isin;
 
/**
 * Getter of the property <tt>isin</tt>
 *
 * @return Returns the isin.
 * 
 */
public String getIsin()
{
	return isin;
}

/**
 * Setter of the property <tt>isin</tt>
 *
 * @param isin The isin to set.
 *
 */
public void setIsin(String isin ){
	this.isin = isin;
}

	/**
 * Valor nominal del t&iacute;tulo
 */
private String valorNominal;
 
/**
 * Getter of the property <tt>valorNominal</tt>
 *
 * @return Returns the valorNominal.
 * 
 */
public String getValorNominal()
{
	return valorNominal;
}

/**
 * Setter of the property <tt>valorNominal</tt>
 *
 * @param valorNominal The valorNominal to set.
 *
 */
public void setValorNominal(String valorNominal ){
	this.valorNominal = valorNominal;
}

	/**
 * Cuenta custodia del cliente
 */
private String cuentaCustodia;
 
/**
 * Getter of the property <tt>cuentaCustodia</tt>
 *
 * @return Returns the cuentaCustodia.
 * 
 */
public String getCuentaCustodia()
{
	return cuentaCustodia;
}

/**
 * Setter of the property <tt>cuentaCustodia</tt>
 *
 * @param cuentaCustodia The cuentaCustodia to set.
 *
 */
public void setCuentaCustodia(String cuentaCustodia ){
	this.cuentaCustodia = cuentaCustodia;
}

	/**
 * C&eacute;dula o rif
 */
private String cedulaRif;
 
/**
 * Getter of the property <tt>cedulaRif</tt>
 *
 * @return Returns the cedulaRif.
 * 
 */
public String getCedulaRif()
{
	return cedulaRif;
}

/**
 * Setter of the property <tt>cedulaRif</tt>
 *
 * @param cedulaRif The cedulaRif to set.
 *
 */
public void setCedulaRif(String cedulaRif ){
	this.cedulaRif = cedulaRif;
}

	/**
 * Tipo de persona
 */
private String tipoPersona;
 
/**
 * Getter of the property <tt>tipoPersona</tt>
 *
 * @return Returns the tipoPersona.
 * 
 */
public String getTipoPersona()
{
	return tipoPersona;
}

/**
 * Setter of the property <tt>tipoPersona</tt>
 *
 * @param tipoPersona The tipoPersona to set.
 *
 */
public void setTipoPersona(String tipoPersona ){
	this.tipoPersona = tipoPersona;
}

	/**
 * Nombre del cliente
 */
private String nombreCliente;
 
/**
 * Getter of the property <tt>nombreCliente</tt>
 *
 * @return Returns the nombreCliente.
 * 
 */
public String getNombreCliente()
{
	return nombreCliente;
}

/**
 * Setter of the property <tt>nombreCliente</tt>
 *
 * @param nombreCliente The nombreCliente to set.
 *
 */
public void setNombreCliente(String nombreCliente ){
	this.nombreCliente = nombreCliente;
}

	/**
 * Descripci&oacute;n del t&iacute;tulo
 */
private String descripcionTitulo;
 
/**
 * Getter of the property <tt>descripcionTitulo</tt>
 *
 * @return Returns the descripcionTitulo.
 * 
 */
public String getDescripcionTitulo()
{
	return descripcionTitulo;
}

/**
 * Setter of the property <tt>descripcionTitulo</tt>
 *
 * @param descripcionTitulo The descripcionTitulo to set.
 *
 */
public void setDescripcionTitulo(String descripcionTitulo ){
	this.descripcionTitulo = descripcionTitulo;
}

	/**
 * Fecha de vencimiento del t&iacute;tulo
 */
private Date fechaVencimientoTitulo;
 
/**
 * Getter of the property <tt>fechaVencimientoTitulo</tt>
 *
 * @return Returns the fechaVencimientoTitulo.
 * 
 */
public Date getFechaVencimientoTitulo()
{
	return fechaVencimientoTitulo;
}

/**
 * Setter of the property <tt>fechaVencimientoTitulo</tt>
 *
 * @param fechaVencimientoTitulo The fechaVencimientoTitulo to set.
 *
 */
public void setFechaVencimientoTitulo(Date fechaVencimientoTitulo ){
	this.fechaVencimientoTitulo = fechaVencimientoTitulo;
}

	/**
 * Fecha de emisi&oacute;n del t&iacute;tulo
 */
private Date fechaEmisionTitulo;
 
/**
 * Getter of the property <tt>fechaEmisionTitulo</tt>
 *
 * @return Returns the fechaEmisionTitulo.
 * 
 */
public Date getFechaEmisionTitulo()
{
	return fechaEmisionTitulo;
}

/**
 * Setter of the property <tt>fechaEmisionTitulo</tt>
 *
 * @param fechaEmisionTitulo The fechaEmisionTitulo to set.
 *
 */
public void setFechaEmisionTitulo(Date fechaEmisionTitulo ){
	this.fechaEmisionTitulo = fechaEmisionTitulo;
}

	/**
 * Descripci&oacute;n del tipo de garant&iacute;a
 */
private String descripcionTipoGarantia;
 
/**
 * Getter of the property <tt>descripcionTipoGarantia</tt>
 *
 * @return Returns the descripcionTipoGarantia.
 * 
 */
public String getDescripcionTipoGarantia()
{
	return descripcionTipoGarantia;
}

/**
 * Setter of the property <tt>descripcionTipoGarantia</tt>
 *
 * @param descripcionTipoGarantia The descripcionTipoGarantia to set.
 *
 */
public void setDescripcionTipoGarantia(String descripcionTipoGarantia ){
	this.descripcionTipoGarantia = descripcionTipoGarantia;
}

	/**
 * Descripci&oacute;n del tipo de bloqueo
 */
private String descripcionTipoBloqueo;
 
/**
 * Getter of the property <tt>descripcionTipoBloqueo</tt>
 *
 * @return Returns the descripcionTipoBloqueo.
 * 
 */
public String getDescripcionTipoBloqueo()
{
	return descripcionTipoBloqueo;
}

/**
 * Setter of the property <tt>descripcionTipoBloqueo</tt>
 *
 * @param descripcionTipoBloqueo The descripcionTipoBloqueo to set.
 *
 */
public void setDescripcionTipoBloqueo(String descripcionTipoBloqueo ){
	this.descripcionTipoBloqueo = descripcionTipoBloqueo;
}

	/**
 * Id de la moneda de negociaci&oacute;n
 */
private String idMonedaNegocia;
 
/**
 * Getter of the property <tt>idMonedaNegocia</tt>
 *
 * @return Returns the idMonedaNegocia.
 * 
 */
public String getIdMonedaNegocia()
{
	return idMonedaNegocia;
}

/**
 * Setter of the property <tt>idMonedaNegocia</tt>
 *
 * @param idMonedaNegocia The idMonedaNegocia to set.
 *
 */
public void setIdMonedaNegocia(String idMonedaNegocia ){
	this.idMonedaNegocia = idMonedaNegocia;
}

	/**
 * Id de la moneda nominal
 */
private String idMonedaNominal;
 
/**
 * Getter of the property <tt>idMonedaNominal</tt>
 *
 * @return Returns the idMonedaNominal.
 * 
 */
public String getIdMonedaNominal()
{
	return idMonedaNominal;
}

/**
 * Setter of the property <tt>idMonedaNominal</tt>
 *
 * @param idMonedaNominal The idMonedaNominal to set.
 *
 */
public void setIdMonedaNominal(String idMonedaNominal ){
	this.idMonedaNominal = idMonedaNominal;
}

	/**
 * Descripci&oacute;n de la moneda de negociaci&oacute;n
 */
private String descripcionMonedaNegocia;
 
/**
 * Getter of the property <tt>descripcionMonedaNegocia</tt>
 *
 * @return Returns the descripcionMonedaNegocia.
 * 
 */
public String getDescripcionMonedaNegocia()
{
	return descripcionMonedaNegocia;
}

/**
 * Setter of the property <tt>descripcionMonedaNegocia</tt>
 *
 * @param descripcionMonedaNegocia The descripcionMonedaNegocia to set.
 *
 */
public void setDescripcionMonedaNegocia(String descripcionMonedaNegocia ){
	this.descripcionMonedaNegocia = descripcionMonedaNegocia;
}

	/**
 * Descripci&oacute;n de la moneda nominal
 */
private String descripcionMonedaNominal;
 
/**
 * Getter of the property <tt>descripcionMonedaNominal</tt>
 *
 * @return Returns the descripcionMonedaNominal.
 * 
 */
public String getDescripcionMonedaNominal()
{
	return descripcionMonedaNominal;
}

/**
 * Setter of the property <tt>descripcionMonedaNominal</tt>
 *
 * @param descripcionMonedaNominal The descripcionMonedaNominal to set.
 *
 */
public void setDescripcionMonedaNominal(String descripcionMonedaNominal ){
	this.descripcionMonedaNominal = descripcionMonedaNominal;
}

}
