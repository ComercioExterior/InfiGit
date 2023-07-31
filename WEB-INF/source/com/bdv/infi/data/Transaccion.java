package com.bdv.infi.data;

import java.util.ArrayList;

//Representa una transacci&oacute;n con sus documentos, campos asociados
public class Transaccion {
	
   /**Id de la transacci&oacute;n*/
   String idTransaccion = "";
   String descripcion = "";
   String funcionAsociada = "";
	
   ArrayList documentos;
   ArrayList  campos;
   ArrayList indicadores; //Indicadores de cotejo 
   
	public Object getDocumentos() {
		return documentos;
	}
	public void setDocumentos(ArrayList documentos) {
		this.documentos = documentos;
	}
	public Object getCampos() {
		return campos;
	}
	public void setCampos(ArrayList campos) {
		this.campos = campos;
	}
	
	public ArrayList getIndicadores() {
		return indicadores;
	}
	public void setIndicadores(ArrayList indicadores) {
		this.indicadores = indicadores;
	}
	public String getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFuncionAsociada() {
		return funcionAsociada;
	}
	public void setFuncionAsociada(String funcionAsociada) {
		this.funcionAsociada = funcionAsociada;
	}

	
}
