package com.bdv.infi.data;

import java.util.Date;

public class DocumentoDefinicion {
	
	private String transaId;
	 
	/**
	 * Getter of the property <tt>transa_id</tt>
	 *
	 * @return Returns the transa_id.
	 * 
	 */
	public String getTransaId()
	{
		return transaId;
	}

	/**
	 * Setter of the property <tt>transa_id</tt>
	 *
	 * @param transa_id The transa_id to set.
	 *
	 */
	public void setTransaId(String transa_id ){
		this.transaId = transa_id;
	}
	
	private int idUnidadInversion;
	 
	/**
	 * Getter of the property <tt>IdUnidadInversion</tt>
	 *
	 * @return Returns the IdUnidadInversion.
	 * 
	 */
	public int getIdUnidadInversion()
	{
		return idUnidadInversion;
	}

	/**
	 * Setter of the property <tt>IdUnidadInversion</tt>
	 *
	 * @param IdUnidadInversion The IdUnidadInversion to set.
	 *
	 */
	public void setIdUnidadInversion(int idUnidadInversion ){
		this.idUnidadInversion = idUnidadInversion;
	}
	
	private int documentoId;
	 
	/**
	 * Getter of the property <tt>documento_id</tt>
	 *
	 * @return Returns the documento_id.
	 * 
	 */
	public int getDocumentoId()
	{
		return documentoId;
	}

	/**
	 * Setter of the property <tt>documento_id</tt>
	 *
	 * @param documento_id The documento_id to set.
	 *
	 */
	public void setDocumentoId(int documento_id ){
		this.documentoId = documento_id;
	}

	private String statusDocumento;
	 
	/**
	 * Getter of the property <tt>status_documento</tt>
	 *
	 * @return Returns the status_documento.
	 * 
	 */
	public String getStatusDocumento()
	{
		return statusDocumento;
	}

	/**
	 * Setter of the property <tt>status_documento</tt>
	 *
	 * @param status_documento The status_documento to set.
	 *
	 */
	public void setStatusDocumento(String status_documento ){
		this.statusDocumento = status_documento;
	}
	
	private String creUsuarioUserid;
	 
	/**
	 * Getter of the property <tt>cre_usuario_userid</tt>
	 *
	 * @return Returns the cre_usuario_userid.
	 * 
	 */
	public String getCreUsuarioUserid()
	{
		return creUsuarioUserid;
	}

	/**
	 * Setter of the property <tt>cre_usuario_userid</tt>
	 *
	 * @param cre_usuario_userid The cre_usuario_userid to set.
	 *
	 */
	public void setCreUsuarioUserid(String cre_usuario_userid ){
		this.creUsuarioUserid = cre_usuario_userid;
	}
	
	private Date creFecha;
	 
	/**
	 * Getter of the property <tt>cre_fecha</tt>
	 *
	 * @return Returns the cre_fecha.
	 * 
	 */
	public Date getCreFecha()
	{
		return creFecha;
	}

	/**
	 * Setter of the property <tt>cre_fecha</tt>
	 *
	 * @param cre_fecha The cre_fecha to set.
	 *
	 */
	public void setCreFecha(Date cre_fecha ){
		this.creFecha = cre_fecha;
	}
	
	private String aproUsuarioUserid;
	 
	/**
	 * Getter of the property <tt>apro_usuario_userid</tt>
	 *
	 * @return Returns the apro_usuario_userid.
	 * 
	 */
	public String getAproUsuarioUserid()
	{
		return aproUsuarioUserid;
	}

	/**
	 * Setter of the property <tt>apro_usuario_userid</tt>
	 *
	 * @param apro_usuario_userid The apro_usuario_userid to set.
	 *
	 */
	public void setAproUsuarioUserid(String apro_usuario_userid ){
		this.aproUsuarioUserid = apro_usuario_userid;
	}
	
	private Date aproFecha;
	 
	/**
	 * Getter of the property <tt>apro_fecha</tt>
	 *
	 * @return Returns the apro_fecha.
	 * 
	 */
	public Date getAproFecha()
	{
		return aproFecha;
	}

	/**
	 * Setter of the property <tt>apro_fecha</tt>
	 *
	 * @param apro_fecha The apro_fecha to set.
	 *
	 */
	public void setAproFecha(Date apro_fecha ){
		this.aproFecha = apro_fecha;
	}

	private String rutaDocumento;
	 
	/**
	 * Getter of the property <tt>ruta_documento</tt>
	 *
	 * @return Returns the ruta_documento.
	 * 
	 */
	public String getRutaDocumento()
	{
		return rutaDocumento;
	}

	/**
	 * Setter of the property <tt>ruta_documento</tt>
	 *
	 * @param ruta_documento The ruta_documento to set.
	 *
	 */
	public void setRutaDocumento(String ruta_documento){
		this.rutaDocumento = ruta_documento;
	}
	
	private String nombreDoc;
	
	/**
	 * Getter of the property <tt>nombre_doc</tt>
	 *
	 * @return Returns the nombre_doc
	 * 
	 */
	public String getNombreDoc() {
		return nombreDoc;
	}
	
	/**
	 * Setter of the property <tt>nombre_doc</tt>
	 *
	 * @param nombre_doc The nombre_doc to set.
	 *
	 */
	public void setNombreDoc(String nombre_doc) {
		this.nombreDoc = nombre_doc;
	}
	
	
	/**
	 * Determina el valor del radio button del formulario de modificaci&oacute;n de documento.
	 * Permite validar si se va a modificar el contenido del archivo o s&oacute;lo el id de transacci&oacute;n.
	 */
	private short modificarDocumento;

	/**
	 * Obtener el valor de la variable radioButton
	 * @return 
	 */
	public short getModificarDocumento() {
		return modificarDocumento;
	}
	
	/**
	 * Establecer el valor de la variable radioButton
	 * @param radioButton
	 */
	public void setModificarDocumento(short radioButton) {
		this.modificarDocumento = radioButton;
	}
	
	/**
	 * Representa el contenido del archivo 
	 */
	private byte[] contenido;
	
	/**
	 * Obtener el contenido del archivo
	 * @return
	 */
	public byte[] getContenido() {
		return contenido;
	}

	/**
	 * Establecer el contenido del archivo
	 * @param contenido
	 */
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	
	/**
	 * Campo que representa el tipo de persona
	 */
	private String tipoPersona;
	
	/**
	 * Obtener el valor del campo tipoPersona
	 * @return
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}
	
	/**
	 * Establecer el valor del campo tipoPersona
	 * @param tipoPersona
	 */
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("aproUsuarioUserid->").append(this.aproUsuarioUserid).append("\n");
		sb.append("creUsuarioUserid->").append(this.creUsuarioUserid).append("\n");
		sb.append("documentoId->").append(this.documentoId).append("\n");
		sb.append("idUnidadInversion->").append(this.idUnidadInversion).append("\n");
		sb.append("nombreDoc->").append(this.nombreDoc).append("\n");
		sb.append("rutaDocumento->").append(this.rutaDocumento).append("\n");
		sb.append("statusDocumento->").append(this.statusDocumento).append("\n");
		sb.append("tipoPersona->").append(this.tipoPersona).append("\n");
		sb.append("transaId->").append(this.transaId).append("\n");
		return sb.toString();
	}
	
}
