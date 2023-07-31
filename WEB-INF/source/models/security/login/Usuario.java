package models.security.login;

import java.util.Date;

import com.bdv.infi.webservices.beans.CredencialesDeUsuario;

public class Usuario {
	public CredencialesDeUsuario credenciales = null;
	
	private String oficina="";
	private Date fecha= null;
	private String terminal="";
	private String entidad="";
	private String usuario="";
	private String cics="";
	private String literal="";
	private String enviroment= "";
	private String daysLeft="";
	private String perfilUsuario="";
	private String ubicacion="";
	private String sesion="";
	private Date fechaContable= null;
	public String getCics() {
		return cics;
	}
	public void setCics(String cics) {
		this.cics = cics;
	}
	public CredencialesDeUsuario getCredenciales() {
		return credenciales;
	}
	public void setCredenciales(CredencialesDeUsuario credenciales) {
		this.credenciales = credenciales;
	}
	public String getDaysLeft() {
		return daysLeft;
	}
	public void setDaysLeft(String daysLeft) {
		this.daysLeft = daysLeft;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getEnviroment() {
		return enviroment;
	}
	public void setEnviroment(String enviroment) {
		this.enviroment = enviroment;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Date getFechaContable() {
		return fechaContable;
	}
	public void setFechaContable(Date fechaContable) {
		this.fechaContable = fechaContable;
	}
	public String getLiteral() {
		return literal;
	}
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}
	public String getSesion() {
		return sesion;
	}
	public void setSesion(String sesion) {
		this.sesion = sesion;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Oficina-->").append(this.oficina).append("\n");
		sb.append("Fecha-->").append(this.fecha).append("\n");
		sb.append("Terminal-->").append(this.terminal).append("\n");
		sb.append("Entidad-->").append(this.entidad).append("\n");		
		sb.append("Usuario-->").append(this.usuario).append("\n");		
		sb.append("Cics-->").append(this.cics).append("\n");
		sb.append("Literal-->").append(this.literal).append("\n");
		sb.append("Enviroment-->").append(this.enviroment).append("\n");
		sb.append("DaysLeft-->").append(this.daysLeft).append("\n");	
		sb.append("PerfilUsuario-->").append(this.perfilUsuario).append("\n");
		sb.append("Ubicacion-->").append(this.ubicacion).append("\n");
		sb.append("Sesion-->").append(this.sesion).append("\n");
		sb.append("FechaContable-->").append(this.fechaContable).append("\n");		
		return sb.toString();
	}

}
