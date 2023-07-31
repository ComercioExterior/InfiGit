package com.bdv.infi_services.beans.entidades.mensajes_peticion;

import java.io.Serializable;
import java.util.ArrayList;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;
import com.bdv.infi_services.beans.entidades.ValorAtributo;

/**
 * Mensaje de peticion para la consulta de Unidades de Inversion
 * @author MegaSoft Computacion
 */
public class ParametrosConsultaUI implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Credenciales credenciales;
	/**
	 * Bean con la informacion de paginacion
	 */
	private ParametrosPaginacion parametrosPaginacion;
	/**
	 * Criterio Status a aplicar
	 */
	private String criterioStatus = "";
	/**
	 * Criterio Moneda a aplicar
	 */
	private String criterioMoneda = "";	

	/**
	 * Valores del Atributo a utilizar de criterio 
	 */
	private ArrayList<ValorAtributo> valorAtributo = new ArrayList<ValorAtributo>();



	/**
	 * Devuelve el Bean con la informacion de paginacion
	 * @return indicaPagineo
	 */
	public ParametrosPaginacion getParametrosPaginacion() {
		return parametrosPaginacion;
	}
	/**
	 * Almacena el Bean con la informacion de paginacion
	 * @param indicaPagineo
	 */
	public void setParametrosPaginacion(ParametrosPaginacion beanPaginacion) {
		this.parametrosPaginacion = beanPaginacion;
	}
	
	/**
	 * Devuelve el CriterioStatus a aplicar
	 * @return criterioStatus
	 */
	public String getCriterioStatus() {
		return criterioStatus;
	}
	/**
	 * Asigna valor al CriterioStatus a aplicar
	 * @param criterioStatus
	 */
	public void setCriterioStatus(String criterio) {
		this.criterioStatus = criterio;
	}
	
	/**
	 * Devuelve el CriterioMoneda a aplicar
	 * @return criterioMoneda
	 */
	public String getCriterioMoneda() {
		return criterioMoneda;
	}
	/**
	 * Asigna valor al CriterioStatus a aplicar
	 * @param criterioMoneda
	 */
	public void setCriterioMoneda(String criterio) {
		this.criterioMoneda = criterio;
	}
	
	/**
	 * Devuelve los valores del Atributo a utilizar de criterio 
	 * @return valorAtributo
	 */
	public ArrayList getValorAtributo() {
		return valorAtributo;
	}
	/**
	 * Asigna valores al Atributo a utilizar de criterio 
	 * @param valorAtributo
	 */
	public void setValorAtributo(ValorAtributo valorAtributo) {
		this.valorAtributo.add(valorAtributo);
	}

	/**
	 * @return the credenciales
	 */
	public Credenciales getCredenciales() {
		return credenciales;
	}
	/**
	 * @param credenciales the credenciales to set
	 */
	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}
	/**
	 * @param valorAtributo the valorAtributo to set
	 */
	public void setValorAtributo(ArrayList<ValorAtributo> valorAtributo) {
		this.valorAtributo = valorAtributo;
	}
}