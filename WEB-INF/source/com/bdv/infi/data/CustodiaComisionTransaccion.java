package com.bdv.infi.data;

public class CustodiaComisionTransaccion {

	/**
	 * Identificador de la Comision
	 */
	private int idComision;

	/**
	 * Porcentaje para la Transaccion de Salida Interna 
	 */
	private double pctTransaccionInterna;
	
	/**
	 * Porcentaje para la Transaccion de Salida Externa 
	 */
	private double pctTransaccionExterna;
	
	/**
	 * Monto para la Transaccion de Salida Interna 
	 */
	private double montoTransaccionInterna;
	
	/**
	 * Moneda Definida para la Transaccion de Salida Interna 
	 */
	private String monedaTransaccionInterna;
		
	/**
	 * Monto para la Transaccion de Salida Externa 
	 */
	private double montoTransaccionExterna;
	
	/**
	 * Moneda Definida para la Transaccion de Salida Externa 
	 */
	private String monedaTransaccionExterna;
	
	/**
	 * Porcentaje para la Comision Anual Nacional 
	 */
	private double pctAnualNacional;
	
	/**
	 * Porcentaje para la Comision Anual Extranjera 
	 */
	private double pctAnualExtranjera;
	
	/**
	 * Monto para la Comision Anual Nacional 
	 */
	private double montoAnualNacional;
	
	/**
	 * Moneda Definida para la Comision Anual Nacional 
	 */
	private String monedaAnualNacional;
	
	/**
	 * Monto para la Comision Anual Extranjera 
	 */
	private double montoAnualExtranjera;
	
		/**
	 * Moneda Definida para la Comision Anual Extranjera
	 */
	private String monedaAnualExtranjera;

		public int getIdComision() {
			return idComision;
		}

		public void setIdComision(int idComision) {
			this.idComision = idComision;
		}

		public String getMonedaAnualExtranjera() {
			return monedaAnualExtranjera;
		}

		public void setMonedaAnualExtranjera(String monedaAnualExtranjera) {
			this.monedaAnualExtranjera = monedaAnualExtranjera;
		}

		public String getMonedaAnualNacional() {
			return monedaAnualNacional;
		}

		public void setMonedaAnualNacional(String monedaAnualNacional) {
			this.monedaAnualNacional = monedaAnualNacional;
		}

		public String getMonedaTransaccionExterna() {
			return monedaTransaccionExterna;
		}

		public void setMonedaTransaccionExterna(String monedaTransaccionExterna) {
			this.monedaTransaccionExterna = monedaTransaccionExterna;
		}

		public String getMonedaTransaccionInterna() {
			return monedaTransaccionInterna;
		}

		public void setMonedaTransaccionInterna(String monedaTransaccionInterna) {
			this.monedaTransaccionInterna = monedaTransaccionInterna;
		}

		public double getMontoAnualExtranjera() {
			return montoAnualExtranjera;
		}

		public void setMontoAnualExtranjera(double montoAnualExtranjera) {
			this.montoAnualExtranjera = montoAnualExtranjera;
		}

		public double getMontoAnualNacional() {
			return montoAnualNacional;
		}

		public void setMontoAnualNacional(double montoAnualNacional) {
			this.montoAnualNacional = montoAnualNacional;
		}

		public double getMontoTransaccionExterna() {
			return montoTransaccionExterna;
		}

		public void setMontoTransaccionExterna(double montoTransaccionExterna) {
			this.montoTransaccionExterna = montoTransaccionExterna;
		}

		public double getMontoTransaccionInterna() {
			return montoTransaccionInterna;
		}

		public void setMontoTransaccionInterna(double montoTransaccionInterna) {
			this.montoTransaccionInterna = montoTransaccionInterna;
		}

		public double getPctAnualExtranjera() {
			return pctAnualExtranjera;
		}

		public void setPctAnualExtranjera(double pctAnualExtranjera) {
			this.pctAnualExtranjera = pctAnualExtranjera;
		}

		public double getPctAnualNacional() {
			return pctAnualNacional;
		}

		public void setPctAnualNacional(double pctAnualNacional) {
			this.pctAnualNacional = pctAnualNacional;
		}

		public double getPctTransaccionExterna() {
			return pctTransaccionExterna;
		}

		public void setPctTransaccionExterna(double pctTransaccionExterna) {
			this.pctTransaccionExterna = pctTransaccionExterna;
		}

		public double getPctTransaccionInterna() {
			return pctTransaccionInterna;
		}

		public void setPctTransaccionInterna(double pctTransaccionInterna) {
			this.pctTransaccionInterna = pctTransaccionInterna;
		}
	
}
