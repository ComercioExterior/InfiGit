package com.bdv.infi.data;

public class EmpresaDefinicion {
	

	private String empres_id;
	 
	/**
	 * Getter of the property <tt>empres_id</tt>
	 *
	 * @return Returns the empres_id.
	 * 
	 */
	public String getEmpres_id()
	{
		return empres_id;
	}

	/**
	 * Setter of the property <tt>empres_id</tt>
	 *
	 * @param empres_id The empres_id to set.
	 *
	 */
	public void setEmpres_id(String empres_id ){
		this.empres_id = empres_id;
	}

	private String empres_nombre;
	 
	/**
	 * Getter of the property <tt>empres_nombre</tt>
	 *
	 * @return Returns the empres_nombre.
	 * 
	 */
	public String getEmpres_nombre()
	{
		return empres_nombre;
	}

	/**
	 * Setter of the property <tt>empres_nombre</tt>
	 *
	 * @param empres_nombre The empres_nombre to set.
	 *
	 */
	public void setEmpres_nombre(String empres_nombre ){
		this.empres_nombre = empres_nombre;
	}

	private String empres_rif;
	 
	/**
	 * Getter of the property <tt>empres_rif</tt>
	 *
	 * @return Returns the empres_rif.
	 * 
	 */
	public String getEmpres_rif()
	{
		return empres_rif;
	}

	/**
	 * Setter of the property <tt>empres_rif</tt>
	 *
	 * @param empres_rif The empres_rif to set.
	 *
	 */
	public void setEmpres_rif(String empres_rif ){
		this.empres_rif = empres_rif;
	}
	
	
	private int empres_in_emisor;
	 
	/**
	 * Getter of the property <tt>empres_in_emisor</tt>
	 *
	 * @return Returns the empres_in_emisor.
	 * 
	 */
	public int getEmpres_in_emisor()
	{
		return empres_in_emisor;
	}

	/**
	 * Setter of the property <tt>empres_in_emisor</tt>
	 *
	 * @param empres_in_emisor The empres_in_emisor to set.
	 *
	 */
	public void setEmpres_in_emisor(int empres_in_emisor){
		this.empres_in_emisor = empres_in_emisor;
	}

	private int empres_in_depositario_central;
	 
	/**
	 * Getter of the property <tt>empres_in_depositario</tt>
	 *
	 * @return Returns the empres_in_depositario.
	 * 
	 */
	public int getEmpres_in_depositario_central()
	{
		return empres_in_depositario_central;
	}

	/**
	 * Setter of the property <tt>empres_in_depositario_central</tt>
	 *
	 * @param empres_in_depositario_central The empres_in_depositario_central to set.
	 *
	 */
	public void setEmpres_in_depositario_central(int empres_in_depositario_central ){
		this.empres_in_depositario_central = empres_in_depositario_central;
	}
	
	private int empres_status;
	 
	/**
	 * Getter of the property <tt>empres_status</tt>
	 *
	 * @return Returns the empres_status.
	 * 
	 */
	public int getEmpres_status()
	{
		return empres_status;
	}

	/**
	 * Setter of the property <tt>empres_status</tt>
	 *
	 * @param empres_status The empres_status to set.
	 *
	 */
	public void setEmpres_status(int empres_status ){
		this.empres_status = empres_status;
	}
	
	/*
	 * (non-javadoc)
	 */
	private String empres_email;
	 
	/**
	 * Getter of the property <tt>empres_email</tt>
	 *
	 * @return Returns the empres_email.
	 * 
	 */
	public String getEmpres_email()
	{
		return empres_email;
	}

	/**
	 * Setter of the property <tt>empres_email</tt>
	 *
	 * @param empres_email The empres_email to set.
	 *
	 */
	public void setEmpres_email(String empres_email ){
		this.empres_email = empres_email;
	}
	
	private String empres_siglas;
	 
	/**
	 * Getter of the property <tt>empres_nombre</tt>
	 *
	 * @return Returns the empres_nombre.
	 * 
	 */
	public String getEmpres_siglas()
	{
		return empres_siglas;
	}

	/**
	 * Setter of the property <tt>empres_nombre</tt>
	 *
	 * @param empres_nombre The empres_nombre to set.
	 *
	 */
	public void setEmpres_siglas(String empres_siglas ){
		this.empres_siglas = empres_siglas;
	}
	
	/**
	 * N&uacute;mero de Cuenta de la empresa
	 */
	private String empresa_numero_cuenta = "";

	/**
	 * Obtiene el n&uacute;mero de cuenta de la empresa 
	 * @return empresa_numero_cuenta
	 */
	public String getEmpresa_numero_cuenta() {
		return empresa_numero_cuenta;
	}

	/**
	 * Asigna el n&uacute;mero de cuenta de la empresa
	 * @param empresa_numero_cuenta
	 */
	public void setEmpresa_numero_cuenta(String empresa_numero_cuenta) {
		this.empresa_numero_cuenta = empresa_numero_cuenta;
	}
}
