package com.bdv.infi.data;

/** 
 * Clase para configurar clientes asociados a una Regla de Transacci&oacute;n Financiera
 */
public class ClienteReglaTrnFinanciera extends com.bdv.infi.data.DataRegistro {

/**
 * Id de la Regla de Transacci&oacute;n Financiera 
 * 
 */
private int idReglaTransaccionFinanciera;
 
/**
 * Getter of the property <tt>idReglaTransaccionFinanciera</tt>
 *
 * @return Returns the idReglaTransaccionFinanciera.
 * 
 */
public int getIdReglaTransaccionFinanciera()
{
	return idReglaTransaccionFinanciera;
}

/**
 * Setter of the property <tt>idReglaTransaccionFinanciera</tt>
 *
 * @param idReglaTransaccionFinanciera The idReglaTransaccionFinanciera to set.
 *
 */
public void setIdReglaTransaccionFinanciera(int idReglaTransaccionFinanciera ){
	this.idReglaTransaccionFinanciera = idReglaTransaccionFinanciera;
}

/**
 * Id de Cliente
 */
private int idCliente; 
 
/**
 * Getter of the property <tt>idCliente</tt>
 *
 * @return Returns the idCliente.
 * 
 */
public int getIdCliente()
{
	return idCliente;
}

/**
 * Setter of the property <tt>idCliente</tt>
 *
 * @param idCliente The idCliente to set.
 *
 */
public void setIdCliente(int idCliente ){
	this.idCliente = idCliente;
}


}
