package com.bdv.infi.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.TransaccionDocumento;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;


/** 
 * Clase destinada para el proceso de inserci&oacute;n, modificaci&oacute;n y listado de los documentos asociados a una transacci&oacute;n.
 */
public class TransaccionDocumentosDAO extends GenericoDAO{

	
	/**
	 * Aprueba un documento indicando que est&aacute; listo para ser usado en una transacci&oacute;n 
	*/
	public int aprobarDocumento(int idTransaccion, int idDocumento){
	return 0;
		 }

	public TransaccionDocumentosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
		
	public TransaccionDocumentosDAO(DataSource ds) throws Exception {
		super(ds);
	}	

	/**
	 * M&eacute;todo para retornar un documento asociado a una transacci&oacute;n. Los datos ser&aacute;n almacenados en el dataSet heredado
	 * @param idTransaccion id de la transacci&oacute;n a la que pertenece el documento
	 * @param idDocumento id del documento 
	*/
	public void listarPorDocumento(int idTransaccion, int idDocumento){
	
	}

	/**
	 * M&eacute;todo para retornar una lista de documentos asociados a una transacci&oacute;n. 
	 * Los datos ser&aacute;n almacenados en el dataSet heredado.
	 * Una transacci&oacute;n seg&uacute;n el tipo de persona puede generar documentos distintos
	 * @param idTransaccion id de la transacci&oacute;n para buscar los documentos
	 * @param tipoPersona tipo de persona (V,E,J) para la b&uacute;squeda de los documentos.  
	*/
	public void listarPorPersona(String idTransaccion, String tipoPersona){
	
	}
	
	public int insertar(Object objeto) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int modificar(Object objeto){
		// TODO Auto-generated method stub
		return 0;
	}


	public Object moveNext() throws Exception {
		return null;		
	}

	
}
