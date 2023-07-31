package org.bcv.serviceINTERVENCION;

public class TipoIntervencion {

	private String nombreTipoIntervencion;
	
	
	 public TipoIntervencion(String nombreTipoIntervencion) {
			//super();
			this.nombreTipoIntervencion = nombreTipoIntervencion;
		}

	public String getNombreTipoIntervencion() {
		return nombreTipoIntervencion;
	}

	public void setNombreTipoIntervencion(String nombreTipoIntervencion) {
		this.nombreTipoIntervencion = nombreTipoIntervencion;
	}
}
