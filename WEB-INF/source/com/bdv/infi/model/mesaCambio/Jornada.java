package com.bdv.infi.model.mesaCambio;

import megasoft.Logger;


public class Jornada extends MesaStub {
	String contenido = "";
	String codigoRechazo = "";
	String comentario = "";

	public Jornada() {
		super();
	}

	public String jornadaActiva() {
		String jornada = "";
		try {
			jornada = Stub.jornadaActiva();
		} catch (Exception e) {
			System.out.println("Jornada : jornadaActiva()" + e);
		}

		return jornada;
	}

	public String estatusJornada(String jornada) {
		String resultado = "";
		try {
			resultado = Stub.statusjornada(jornada);
	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Jornada : estatusJornada()" + e);
		}
		return resultado;
	}

}
