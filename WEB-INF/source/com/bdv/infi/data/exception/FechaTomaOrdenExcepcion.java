package com.bdv.infi.data.exception;

/**Indica que no hay permisolog&iacute;a para la toma de orden por motivos de fecha*/
public class FechaTomaOrdenExcepcion extends Exception {

	public FechaTomaOrdenExcepcion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FechaTomaOrdenExcepcion(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FechaTomaOrdenExcepcion(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FechaTomaOrdenExcepcion(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
