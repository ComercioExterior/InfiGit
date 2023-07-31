package com.bdv.infi.logic.interfaz_varias;

/**
 * Clase que instancia el mensaje dependiendo del tipo recibido
 */
public class FactoryMensaje {

	/**
	 * Instancia el Mensaje dependiendo del tipo
	 * @param tipo tipo de mensaje a instanciar
	 * @return Instancia del Mensaje según el tipo
	 * @throws Exception en caso de error
	 */
	public static Mensaje getMensaje(String tipo) throws Exception{
		Mensaje mensaje = null;
		try {
			if (tipo.equals(MensajeBcv.TIPO_MENSAJE)){
				mensaje = new MensajeBcv();
			}else if (tipo.equals(MensajeCarmen.TIPO_MENSAJE)){
				mensaje = new MensajeCarmen();
			}else if (tipo.equals(MensajeEstadistica.TIPO_MENSAJE)){
				mensaje = new MensajeEstadistica();
			}else{
				throw new Exception("Mensaje no definido");
			}
		} catch (Exception e) {
			throw e ;
		}
		return mensaje;
	}
}