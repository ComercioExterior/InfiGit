package com.bdv.infi.data;

import java.util.Date;

public class TitulosPrecios extends DataRegistro{

	/**C&oacute;digo del t&iacute;tulo*/
	private String idTitulo;
	
	/**Precio Nominal de titulo*/
	private String pNominal;
	
	/**Precio Mercado de titulo*/
	private String pMercado;
	
	/**Precio Recompra de titulo*/
	private String pRecompra;
	
	/**Usuario Aprobador de precio*/
	private String usuarioAprobador;
	
	/**Fecha de Aprobacion de precio*/
	private Date fechaAprobacion;
	
	/**Tipo de Producto*/
	private String tipoProductoId;

	public String getUsuarioAprobador() {
		return usuarioAprobador;
	}

	public void setUsuarioAprobador(String usuarioAprobador) {
		this.usuarioAprobador = usuarioAprobador;
	}

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public String getIdTitulo() {
		return idTitulo;
	}

	public void setIdTitulo(String idTitulo) {
		this.idTitulo = idTitulo;
	}

	public String getPMercado() {
		return pMercado;
	}

	public void setPMercado(String mercado) {
		pMercado = mercado;
	}

	public String getPNominal() {
		return pNominal;
	}

	public void setPNominal(String nominal) {
		pNominal = nominal;
	}

	public String getPRecompra() {
		return pRecompra;
	}

	public void setPRecompra(String recompra) {
		pRecompra = recompra;
	}

	public String getTipoProductoId() {
		return tipoProductoId;
	}

	public void setTipoProductoId(String tipoProductoId) {
		this.tipoProductoId = tipoProductoId;
	}
	
}
