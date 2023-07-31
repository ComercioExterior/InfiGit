package com.bdv.infi.model.menudeo;

import megasoft.Logger;


public class CambioClave extends AutorizacionStub {
	String contenido = "";
	String codigoRechazo = "";
	String comentario = "";

	public CambioClave() {
		super();
	}

	public String cambio(String claveNueva) {
		String respuesta = "";
		try {
			respuesta = Stub.cambiarClave(claveNueva);
			System.out.println("CambioClave : cambio() Respuesta : " + respuesta);
			Logger.info(this, "CambioClave : cambio() Respuesta : " + respuesta);
		} catch (Exception e) {
			System.out.println("CambioClave (Menudeo): cambio() " + e);
			Logger.info(this, "CambioClave (Menudeo): cambio() " + e);
			respuesta = e.toString();
		}

		return respuesta;
	}

}
