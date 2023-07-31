package models.security.login;

import java.util.Date;

import com.bdv.infi.webservices.beans.CredencialesDeUsuario;

public class ConsultaDeUsuario {
	public CredencialesDeUsuario credenciales = null;
	
	private String usuario="";
	private String password="";
	private String nuevaPassword="";
	private String verifNuevaPassword="";
	private String direccion="";
	private String oficina="";
	private Date fecha= null;
	private String sesion="";
	private String tipoDeCaja="";
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNuevaPassword() {
		return nuevaPassword;
	}
	public void setNuevaPassword(String nuevaPassword) {
		this.nuevaPassword = nuevaPassword;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSesion() {
		return sesion;
	}
	public void setSesion(String sesion) {
		this.sesion = sesion;
	}
	public String getTipoDeCaja() {
		return tipoDeCaja;
	}
	public void setTipoDeCaja(String tipoDeCaja) {
		this.tipoDeCaja = tipoDeCaja;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getVerifNuevaPassword() {
		return verifNuevaPassword;
	}
	public void setVerifNuevaPassword(String verifNuevaPassword) {
		this.verifNuevaPassword = verifNuevaPassword;
	}
}
