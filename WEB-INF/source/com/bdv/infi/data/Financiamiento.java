package com.bdv.infi.data;

import java.util.Date;

public class Financiamiento {

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

/*
 * (non-javadoc)
 */
private int monto;
 
/**
 * Getter of the property <tt>monto</tt>
 *
 * @return Returns the monto.
 * 
 */
public int getMonto()
{
	return monto;
}

/**
 * Setter of the property <tt>monto</tt>
 *
 * @param monto The monto to set.
 *
 */
public void setMonto(int monto ){
	this.monto = monto;
}

/*
 * (non-javadoc)
 */
private int cuota;
 
/**
 * Getter of the property <tt>cuota</tt>
 *
 * @return Returns the cuota.
 * 
 */
public int getCuota()
{
	return cuota;
}

/**
 * Setter of the property <tt>cuota</tt>
 *
 * @param cuota The cuota to set.
 *
 */
public void setCuota(int cuota ){
	this.cuota = cuota;
}

}
