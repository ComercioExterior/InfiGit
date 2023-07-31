package com.bdv.infi.dao;

import java.util.ArrayList;

/** 
 * Las comisiones asociadas a las transacciones
 */
public class TransaccionComisionDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public TransaccionComisionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Lista las comisiones que se deben aplicar en base a la transacci&oacute;n efectuada y a los par&aacute;metros recibidos 
	*/
	public ArrayList listar(long idTransaccion, String idUnidadInversion, String idBlotter, String idMoneda, String tipoCliente, long idCliente, String idDepositario){
		return null;
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
