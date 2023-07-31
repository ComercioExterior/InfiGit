package com.bdv.infi.model.mesaCambio;

import megasoft.Logger;


public class CambioClave extends MesaStub {
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
			System.out.println("respueta del servicio :" + respuesta);
		} catch (Exception e) {
			System.out.println("CambioClave : cambio()" + e);
			respuesta = e.toString();
		}

		return respuesta;
	}

}
