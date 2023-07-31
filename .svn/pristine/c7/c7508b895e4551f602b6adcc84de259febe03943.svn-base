package com.bdv.infi.logic.interfaz_opics.message;

import java.util.ArrayList;
import java.util.Date;

/**Mensaje de opics que contiene detalle de operaciones*/
public class MensajeOpics {

	/**Corresponde al id del mensaje generado*/
	private long idOpics;
	
	/**Indica si el mensaje se envió o nó*/
	private boolean enviado = false;
	
	/**Id del usuario que generó el mensaje*/
	private long idUsuario;
	
	/**última fecha de generación del mensaje*/
	private Date fechaMensaje = null;
	
	/**Fecha de envio del mensaje*/
	private Date fechaEnvio = null;
	
	/**Id de ejecución. Indica el número de proceso en el que se envió el mensaje*/
	private long idEjecucion;
	
	/**Fecha valor o fecha en la que se debe enviar el mensaje a opics*/
	private Date fechaValor = null;
	
	private long idOrdenInfi;
	
	/**Contiene la lista de los mensajes detalles*/
	private ArrayList<MensajeOpicsDetalle> mensajesDetalle = new ArrayList<MensajeOpicsDetalle>(); 

	/**Devuelve el id del mensaje. Identificador único del mensaje*/
	public long getIdOpics() {
		return idOpics;
	}

	/**Establece el id del mensaje.*/
	public void setIdOpics(long idMensaje) {
		this.idOpics = idMensaje;
	}

	/**Indica si el mensaje fue enviado o no*/
	public boolean isEnviado() {
		return enviado;
	}

	/**Establece si el mensaje fue enviado o no*/
	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	/**Devuelve el id del usuario que generó el mensaje*/
	public long getIdUsuario() {
		return idUsuario;
	}

	/**Establece el id del usuario que generó el mensaje*/
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**Devuelve la fecha en que fue generado el mensaje*/
	public Date getFechaMensaje() {
		return fechaMensaje;
	}

	/**Setea la fecha en que fue generado el mensaje*/
	public void setFechaMensaje(Date fechaMensaje) {
		this.fechaMensaje = fechaMensaje;
	}

	/**Devuelve la fecha de envio del mensaje*/
	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	/**Establece la fecha de envio del mensaje*/
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	/**Devuelve el id de ejecución del mensaje*/
	public long getIdEjecucion() {
		return idEjecucion;
	}

	/**Establece el id de ejecución del mensaje*/	
	public void setIdEjecucion(long idEjecucion) {
		this.idEjecucion = idEjecucion;
	}

	/**Devuelve la fecha valor o fecha en la que debe ser enviado el mensaje a OPICS*/
	public Date getFechaValor() {
		return fechaValor;
	}

	/**Establece la fecha en la que debe ser enviado el mensaje a OPICS*/
	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}
	
	/**Agrega un mensaje de detalle al mensaje general de opics*/
	public void agregarMensajeDetalle(MensajeOpicsDetalle mensajeDetalle){
		mensajesDetalle.add(mensajeDetalle);
	}
	
	/**Devuelve la lista de mensajes detalles asociados al mensaje opics general*/
	public ArrayList<MensajeOpicsDetalle> getMensajesDetalles(){
		return mensajesDetalle;
	}
	
	/**Sobreescribe el método toString*/
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("--------------MENSAJE OPICS----------------");
		sb.append("IDOPICS->").append(idOpics).append("\n");
		sb.append("ENVIADO->").append(enviado).append("\n");		
		sb.append("IDUSUARIO->").append(idUsuario).append("\n");		
		sb.append("FECHAMENSAJE->").append(fechaMensaje).append("\n");		
		sb.append("FECHAENVIO->").append(fechaEnvio).append("\n");
		sb.append("IDEJECUCION->").append(idEjecucion).append("\n");
		sb.append("FECHAVALOR->").append(fechaValor).append("\n");
		sb.append(this.getMensajesDetalles().toString());
		sb.append("--------------FIN MENSAJE OPICS----------------");		
		
		return sb.toString();
	}

	public long getIdOrdenInfi() {
		return idOrdenInfi;
	}

	public void setIdOrdenInfi(long idOrdenInfi) {
		this.idOrdenInfi = idOrdenInfi;
	}
}
