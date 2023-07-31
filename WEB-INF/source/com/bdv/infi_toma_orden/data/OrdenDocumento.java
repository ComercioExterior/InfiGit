package com.bdv.infi_toma_orden.data;

public class OrdenDocumento {

	/**Id de la orden*/
	private long idOrden;
	
	/**Id del documento*/
	private long idDocumento;
	
	/**Ruta donde se encuentra el documento asociado a la orden*/
	private String nombre;
	
	/**Documento con las transformaciones necesarias asociado a una orden*/
	private byte[] contenidoBytes;
	
	/**Documento con las transformaciones necesarias asociado a una orden*/
	private String contenido;	

	public long getIdOrden() {
		return idOrden;
	}
	
	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}

	public long getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(long idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String documento) {
		this.nombre = documento;
	}

	/**
	 * Obtener el contenido del archivo
	 * @return
	 */
	public byte[] getContenidoBytes() {
		return contenidoBytes;
	}

	public void setContenidoBytes(byte[] contenido) {
		this.contenidoBytes = contenido;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	
}
