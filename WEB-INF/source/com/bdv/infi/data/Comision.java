package com.bdv.infi.data;

import java.util.ArrayList;

/** 
 * Esta clase maneja las comisiones que se deben cobrar al cliente. Las mimas pueden ser por unidades de inversi&oacute;n y asociadas a un tipo de persona espec&iacute;fico (V,E,J)
 */
public class Comision {

	/**
 * Indica si la comisi&oacute;n puede aplicar reverso
 */
private boolean aplicaReverso;
 
/**
 * Getter of the property <tt>aplicaReverso</tt>
 *
 * @return Returns the aplicaReverso.
 * 
 */
public boolean getAplicaReverso()
{
	return aplicaReverso;
}

/**
 * Setter of the property <tt>aplicaReverso</tt>
 *
 * @param aplicaReverso The aplicaReverso to set.
 *
 */
public void setAplicaReverso(boolean aplicaReverso ){
	this.aplicaReverso = aplicaReverso;
}

	/**
 *
 */
private ArrayList rangoComision = null;

/**
	 * M&eacute;todo que calcula la comisi&oacute;n que debe ser cobrada al cliente. 
	 * 1. La inversi&oacute;n se recibe como par&aacute;metro de entrada y se setea a la variable montoInversion
	 * 2. Se recorre la lista de los rangos de inversi&oacute;n contenidos en la lista rangoComision
	 * 2.1 Si monto m&iacute;mino o monto m&aacute;ximo son diferentes de cero (0)
	 * 2.1.1 Si monto invertido est&aacute; dentro del rango encontrado
	 * 2.1.1.1 Si tasa es cero(0)
	 * 2.1.1.1.1 monto = monto fijo encontrado 
	 * 2.1.1.2 Si la tasa es diferente de cero(0)
	 * 2.1.1.2.1 monto = montoInvertido x tasa / 100 
	 * 2.2 Si monto m&iacute;mino y monto m&aacute;ximo son cero (0) (No importa el monto invertido)
	 * 2.2.1 Se efect&uacute;a el mismo procedimiento del paso 2.1.1.1 
	*/
	public void calcular(double montoInvertido){
	
	}

	/**
 * Monto invertido para el c&aacute;lculo de la comisi&oacute;n a cobrar
 */
private double montoInvertido;
 
/**
 * Getter of the property <tt>montoInvertido</tt>
 *
 * @return Returns the montoInvertido.
 * 
 */
public double getMontoInvertido()
{
	return montoInvertido;
}

/**
 * Monto que se le debe cobrar al inversor
 */
private double monto;
 
/**
 * Getter of the property <tt>monto</tt>
 *
 * @return Returns the monto.
 * 
 */
public double getMonto()
{
	return monto;
}

/**
 * porcentaje que se debe aplicar sobre el monto de la inversi&oacute;n
 */
private double porcentaje;
 
/**
 * Getter of the property <tt>porcentaje</tt>
 *
 * @return Returns the porcentaje.
 * 
 */
public double getPorcentaje()
{
	return porcentaje;
}

/**
 * C&oacute;digo unico que identifica a una comisi&oacute;n
 */
private long codigo;
 
/**
 * Getter of the property <tt>codigo</tt>
 *
 * @return Returns the codigo.
 * 
 */
public long getCodigo()
{
	return codigo;
}

/**
 * Setter of the property <tt>codigo</tt>
 *
 * @param codigo The codigo to set.
 *
 */
public void setCodigo(long codigo ){
	this.codigo = codigo;
}

	/**
 * Descripci&oacute;n de la comisi&oacute;n
 */
private String descripcion;
 
/**
 * Getter of the property <tt>descripcion</tt>
 *
 * @return Returns the descripcion.
 * 
 */
private String getDescripcion()
{
	return descripcion;
}

/**
 * Setter of the property <tt>descripcion</tt>
 *
 * @param descripcion The descripcion to set.
 *
 */
private void setDescripcion(String descripcion ){
	this.descripcion = descripcion;
}

}

///**
// * Getter of the property <tt>codigo</tt>
// *
// * @return Returns the codigo.
// * 
// */
//public long getCodigo()
//{
//	return codigo;
//}
///**
// * Setter of the property <tt>codigo</tt>
// *
// * @param codigo The codigo to set.
// *
// */
//public void setCodigo(long codigo ){
//	this.codigo = codigo;
//}