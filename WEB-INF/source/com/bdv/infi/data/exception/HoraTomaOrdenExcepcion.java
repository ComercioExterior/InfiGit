package com.bdv.infi.data.exception;

/**Indica que no hay permisolog&iacute;a para la toma de orden por motivos de hora*/
public class HoraTomaOrdenExcepcion extends Exception{

	public HoraTomaOrdenExcepcion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HoraTomaOrdenExcepcion(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public HoraTomaOrdenExcepcion(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public HoraTomaOrdenExcepcion(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
