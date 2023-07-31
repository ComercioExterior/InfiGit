package com.bdv.infi.data;

/** 
 * Clase que indica c&oacute;mo debe ser calculada la comisi&oacute;n en base a un monto m&iacute;nimo o m&aacute;ximo de inversi&oacute;n.
 */
public class RangoComision {

	/**
 * Monto fijo de comisi&oacute;n a cobrar
 */
private long montoFijo;
 
/**
 * Getter of the property <tt>montoFijo</tt>
 *
 * @return Returns the montoFijo.
 * 
 */
public long getMontoFijo()
{
	return montoFijo;
}

/**
 * Setter of the property <tt>montoFijo</tt>
 *
 * @param montoFijo The montoFijo to set.
 *
 */
public void setMontoFijo(long montoFijo ){
	this.montoFijo = montoFijo;
}

	/**
 * Tasa a aplicar sobre el monto invertido
 */
private double tasa;
 
/**
 * Getter of the property <tt>tasa</tt>
 *
 * @return Returns the tasa.
 * 
 */
public double getTasa()
{
	return tasa;
}

/**
 * Setter of the property <tt>tasa</tt>
 *
 * @param tasa The tasa to set.
 *
 */
public void setTasa(double tasa ){
	this.tasa = tasa;
}

/*
 * (non-javadoc)
 */
private long codigoComision;
 
/**
 * Getter of the property <tt>codigoComision</tt>
 *
 * @return Returns the codigoComision.
 * 
 */
public long getCodigoComision()
{
	return codigoComision;
}

/**
 * Setter of the property <tt>codigoComision</tt>
 *
 * @param codigoComision The codigoComision to set.
 *
 */
public void setCodigoComision(long codigoComision ){
	this.codigoComision = codigoComision;
}

/**
 * Monto m&aacute;ximo de la inversi&oacute;n
 */
private double montoMaximo;
 
/**
 * Getter of the property <tt>montoMaximo</tt>
 *
 * @return Returns the montoMaximo.
 * 
 */
private double getMontoMaximo()
{
	return montoMaximo;
}

/**
 * Setter of the property <tt>montoMaximo</tt>
 *
 * @param montoMaximo The montoMaximo to set.
 *
 */
private void setMontoMaximo(double montoMaximo ){
	this.montoMaximo = montoMaximo;
}

	/**
 * Monto m&iacute;nimo de la inversi&oacute;n
 */
private double montoMinimo;
 
/**
 * Getter of the property <tt>montoMinimo</tt>
 *
 * @return Returns the montoMinimo.
 * 
 */
public double getMontoMinimo()
{
	return montoMinimo;
}

/**
 * Setter of the property <tt>montoMinimo</tt>
 *
 * @param montoMinimo The montoMinimo to set.
 *
 */
public void setMontoMinimo(double montoMinimo ){
	this.montoMinimo = montoMinimo;
}

}
