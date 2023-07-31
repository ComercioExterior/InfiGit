package com.bdv.infi.webservices.beans;

/**
 * <p>
 * Representa un cliente y contiene los datos de contactos del cliente. Estos
 * datos se traen desde un web service u otro repositorio.
 * <p>
 * Este cliente tambien se utiliza para realizar consultas por cedula y
 * posiblemente otros datos de un cliente.
 * <p>
 * Este objeto es generado a partir de un xml utilizando jibx en los manejadores
 * (com.megasoft.bo.manejadores). Lo que se recibe del web service es el xml.
 * <p>
 * Luego estos objetos son pasados a la capa de presentacion para que utilicen
 * los datos.
 * <p>
 * El ejemplo del xml para este objeto esta en
 * WEB-INF/templates/ejemplos_xml_para_ws/cliente.xml
 * 
 * @author Camilo Torres
 * 
 */
public class Cliente {
	/**
	 * Cedula de identidad
	 */
	private String ci = "";//
	private String nombreCompleto = "";//
	private String codigoSucursal = "";
	private String nombreSucursal = "";
	private String esEmpleado = "";
	private String direccion = "";//
	private String telefono = "";
	private String correoElectronico = "";//
	private String genero = "";//
	private int edad;//
	private String tipo = "";//
	private String clase = "";
	private String indicadorGrupo = "";//
	private String tipoDocumento = ""; //
	private String numeroPersona = ""; //
	private String marcaUAI = ""; //
	private String marcaFEVE = ""; //	
	private CredencialesDeUsuario credenciales;
	
	//datos adicionales del cliente juridico
	private PE55Respuesta datosJuridicos = new PE55Respuesta();
	
	//datos adicionales del cliente PEV7
	private PEM1403 datosAdicionales = new PEM1403();
	
	//datos adicionales del cliente PE68Respuesta
	private PEM1400 datosAdicionalesSexNacEc = new PEM1400();	
	
	
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public CredencialesDeUsuario getCredenciales() {
		return credenciales;
	}
	public void setCredenciales(CredencialesDeUsuario credenciales) {
		this.credenciales = credenciales;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getEsEmpleado() {
		return esEmpleado;
	}
	public void setEsEmpleado(String esEmpleado) {
		this.esEmpleado = esEmpleado;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getIndicadorGrupo() {
		return indicadorGrupo;
	}
	public void setIndicadorGrupo(String indicadorGrupo) {
		this.indicadorGrupo = indicadorGrupo;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getNombreSucursal() {
		return nombreSucursal;
	}
	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}
	public String getNumeroPersona() {
		return numeroPersona;
	}
	public void setNumeroPersona(String numeroPersona) {
		this.numeroPersona = numeroPersona;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public String getMarcaUAI() {
		return marcaUAI;
	}

	public void setMarcaUAI(String marcaUAI) {
		this.marcaUAI = marcaUAI;
	}

	public String getMarcaFEVE() {
		return marcaFEVE;
	}

	public void setMarcaFEVE(String marcaFEVE) {
		this.marcaFEVE = marcaFEVE;
	}	
	
	/**Setea datos adicionales del cliente sólo cuando es jurídico*/
	public void setPE55Respuesta(PE55Respuesta datosJuridicos){
		this.datosJuridicos = datosJuridicos;
	}
	
	/**Devuelve datos adicionales del cliente sólo cuando es jurídico*/
	public PE55Respuesta getPE55Respuesta(){
		return datosJuridicos;
	}
	
	/**Setea datos adicionales del cliente*/
	public void setPEM1403(PEM1403 datosAdicionales){
		this.datosAdicionales = datosAdicionales;
	}
	
	/**Devuelve datos adicionales del cliente sólo cuando es jurídico*/
	public PEM1403 getPEM1403(){
		return datosAdicionales;
	}
	
	/**Setea datos adicionales del cliente, sexo, nacionalidad, fecha de nacimiento y estado civil*/
	public void setPEM1400(PEM1400 datosAdicionalesSexNacEc){
		this.datosAdicionalesSexNacEc = datosAdicionalesSexNacEc;
	}
	
	/**Devuelve datos adicionales del cliente nacionalidad, sexo, estado civil y fecha de nacimiento*/
	public PEM1400 getPEM1400(){
		return datosAdicionalesSexNacEc;
	}
}
