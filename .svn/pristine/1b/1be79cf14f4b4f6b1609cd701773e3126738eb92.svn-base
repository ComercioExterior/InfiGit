package com.bdv.infi.data;

import java.util.ArrayList;

/**
 * Representa una forma de pago. Usada para almacenar las formas de pago definidas y las formas de pago 
 * relacionadas a una orden.
 */
public class FormaPago {

	/**
	 * M&eacute;todo usado para validar los datos ingresados por el usuario referentes
	 * a los valores necesarios en la forma de pago
	 */
	public boolean validarDatos() {
		return false;
	}

	/**Monto ingresado para la forma de pago*/
	private double monto;
	
	/**N&uacute;mero generado por una secuencia num&eacute;rica para almacenar el valor en la base de datos y que forma parte de la clave primaria*/
	private long idFormaPagoSecuencia;
	
	
	/**
	 * Lista de los detalles asociados a la forma de pago
	 */
	private ArrayList formaPagoRef = new ArrayList();

	/**
	 * Getter of the property <tt>formaPagoRef</tt>
	 * 
	 * @return Returns the formaPagoRef.
	 * 
	 */
	public ArrayList getFormaPagoRef() {
		return formaPagoRef;
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * 
	 */
	public boolean isFormaPagoRefEmpty() {
		return formaPagoRef.isEmpty();
	}


	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).
	 * 
	 * @param element
	 *            whose presence in this collection is to be ensured.
	 * @see java.util.Collection#add(Object)
	 * 
	 */
	public boolean agregarFormaPagoRef(FormaPagoRef formaPagoRef) {
		return this.formaPagoRef.add(formaPagoRef);
	}

	/**
	 * Id &uacute;nico que identifica una forma de pago
	 */
	private int idFrmPago;

	/**
	 * Getter of the property <tt>codigo</tt>
	 * 
	 * @return Returns the codigo.
	 * 
	 */
	public int getIdFrmPago() {
		return idFrmPago;
	}

	/**
	 * Setter of the property <tt>codigo</tt>
	 * 
	 * @param codigo
	 *            The codigo to set.
	 * 
	 */
	public void setIdFrmPago(int idFrmPago) {
		this.idFrmPago = idFrmPago;
	}

	/**
	 * Descripci&oacute;n de la forma de pago
	 */
	private String descripcion;

	/**
	 * Getter of the property <tt>descripcion</tt>
	 * 
	 * @return Returns the descripcion.
	 * 
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Setter of the property <tt>descripcion</tt>
	 * 
	 * @param descripcion
	 *            The descripcion to set.
	 * 
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**Monto pagado con la forma de pago seleccionada*/
	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public long getIdFormaPagoSecuencia() {
		return idFormaPagoSecuencia;
	}

	public void setIdFormaPagoSecuencia(long idFormaPagoSecuencia) {
		this.idFormaPagoSecuencia = idFormaPagoSecuencia;
	}

	public void setFormaPagoRef(ArrayList formaPagoRef) {
		this.formaPagoRef = formaPagoRef;
	}	

}