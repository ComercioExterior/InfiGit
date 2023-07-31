package com.bdv.infi_services.beans.entidades.mensajes_respuesta;

import java.io.Serializable;


/**
 * Unidad de Inversion 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class UnidadInversion implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/**
		 * Tipo de instrumento financiero
		 */
		private String tipoInstrumento;
		/**
		 * Identificador de la Unidad de Inversion
		 */
		private String idUnidadInversion = "";	
		/**
		 * bloterId
		 */
		private String bloterId;
		
		/**
		 * Fecha desde para la toma de orden
		 */
		private String fechaDesdeTomaOrden;
		/**
		 * Fecha hasta para la toma de orden
		 */
		private String fechaHastaTomaOrden;
		/**
		 * Hora desde para la toma de orden
		 */
		private String horaDesdeTomaOrden;
		/**
		 * Hora hasta para la toma de orden
		 */
		private String horaHastaTomaOrden;
		/**
		 * Hora desde ultimo dia para la toma de orden
		 */
		private String horaDesdeUltimoDia;
		/**
		 * Hora hasta ultimo dia para la toma de orden
		 */
		private String horaHastaUltimoDia;
		/**
		 * Nombre asignado de la Unidad de Inversion
		 */
		private String nombreUnidadInversion = "";	
		/**
		 * Descripcion de la Unidad de Inversion
		 */
		private String descrUnidadInversion = "";
		/**
		 * Descripcion del Instrumento Financiero que es la Unidad de Inversion
		 */
		private String descrInstrumentoFinanciero = "";		
		/**
		 * Emision a la cual pertenece la Unidad de Inversion
		 */
		private String emisionUnidadInversion = "";
		/**
		 * Serie a la cual pertenece la Unidad de Inversion
		 */
		private String serieUnidadInversion = "";
		/**
		 * Tasa de Cambio que se aplica en el proceso de Toma de Orden
		 */
		private String tasaCambio = "";	
		/**
		 * Multiplo de Inversi&oacute;n
		 */
		private String multiploInversion;
		/**
		 * Fecha de emision de la Unidad de Inversion
		 */
		private String fechaEmisionUI = "";	
		/**
		 * Indicador para venta a los Empleados
		 */
		private String indicaVtaEmpleados = "";
		/**
		 * Fecha de adjudicacion de la Unidad de Inversion
		 */
		private String fechaAdjudicacionUI = "";
		/**
		 * Fecha de liquidacion de la Unidad de Inversion
		 */
		private String fechaLiquidaUI = "";
		/**
		 * Indicador se puede pactar recompra
		 */
		private String indicaRecompra= "";
		/**
		 * Descripción de la moneda
		 */
		private String descrMoneda = "";		
		/**
		 * Monto total de Inventario disponible para la Venta
		 */
		private String inventarioTotal = "";	
		/**
		 * Monto minimo Inventario
		 */
		private String inventarioMontoMinimo = "";
		/**
		 * Monto maximo Inventario
		 */
		private String inventarioMontoMaximo = "";
		/**
		 * Inventario Disponible
		 */
		private String inventarioDisponible ="";
		/**
		 * Identificador del Status que tiene la Unidad de Inversion
		 */
		private String idStatus = "";
		/**
		 * Nombre del status que tiene la OrdenRespuesta
		 */
		private String descrStatus = "";
		/**
		 * Object para los T&iacute;tulos asociados
		 */
		private TitulosAsociados titulosAsociados;
		@SuppressWarnings("unused")
		/**
		 *tipo de personas
		 */
		private TipoPersonasValidas tipoPersonasValidas;
		/**
		 * Campos Dinamicos
		 */
		private CamposDinamicos camposDinamicos;
		
		/**
		 * @return the multiploInversion
		 */
		public String getMultiploInversion() {
			return multiploInversion;
		}


		/**
		 * @param multiploInversion the multiploInversion to set
		 */
		public void setMultiploInversion(String multiploInversion) {
			this.multiploInversion = multiploInversion;
		}


		/**
		 * @return the camposDinamicos
		 */
		public CamposDinamicos getCamposDinamicos() {
			return camposDinamicos;
		}


		/**
		 * @param camposDinamicos the camposDinamicos to set
		 */
		public void setCamposDinamicos(CamposDinamicos camposDinamicos) {
			this.camposDinamicos = camposDinamicos;
		}


		/**
		 * Constructor del bean
		 */
		public UnidadInversion () throws Exception {
			
		}
	
		
		/**
		 * Constructor del bean
		 * Permite asignar los valores a los atributos del bean
		 */
		public UnidadInversion (Object [] objValor) throws Exception {
			this.idUnidadInversion 		= (String)objValor[0];
			this.multiploInversion		= (String)objValor[1];
			this.bloterId 				= (String)objValor[2];
			this.nombreUnidadInversion 	= (String)objValor[3];
			if (objValor[4] != null)
				this.descrUnidadInversion = (String)objValor[4];		
			this.descrInstrumentoFinanciero = (String)objValor[5];
			if (objValor[6] != null)
				this.emisionUnidadInversion  = (String)objValor[6];
			if (objValor[7] != null)
				this.serieUnidadInversion = (String)objValor[7];
			this.tasaCambio  = (String)objValor[8];
			this.fechaEmisionUI = (String)objValor[9];
			this.indicaVtaEmpleados = (String)(objValor[10].toString().equalsIgnoreCase("1")?"SI":(objValor[10].toString().equalsIgnoreCase("0")?"NO":"NO"));
			if (objValor[13] != null)
				this.fechaAdjudicacionUI = (String)objValor[13];
			if (objValor[14] != null)
				this.fechaLiquidaUI = (String)objValor[14];	
			this.indicaRecompra = (String) (objValor[15].toString().equalsIgnoreCase("1")?"SI":(objValor[15].toString().equalsIgnoreCase("0")?"NO":"NO"));
			this.descrMoneda = (String)objValor[16];
			this.inventarioTotal = (String)objValor[18];
			this.inventarioMontoMinimo = (String)objValor[19];
			this.inventarioMontoMaximo = (String)objValor[20];
			this.inventarioDisponible  = (String)objValor[21];
			this.idStatus = (String)objValor[22];
			this.descrStatus = (String)objValor[23];
			this.fechaDesdeTomaOrden = (String)objValor[24];
			this.horaDesdeTomaOrden  = (String)objValor[25];
			this.fechaHastaTomaOrden = (String)objValor[26];
			this.horaHastaTomaOrden  = (String)objValor[27];
			this.horaDesdeUltimoDia  = (String)objValor[28];
			this.horaHastaUltimoDia  = (String)objValor[29];
			this.tipoInstrumento     = (String)objValor[30];
		}
		
		/**
		 * Retorna el valor del atributo Identificador de la Unidad de Inversion
		 * @return
		 */
		public String getIdUnidadInversion() {
			return idUnidadInversion;
		}
		/**
		 * Asigna valor al atributo Identificador de la Unidad de Inversion
		 * @param idUnidadInversion
		 */	
		public void setIdUnidadInversion(String idUnidadInversion) {
			this.idUnidadInversion = idUnidadInversion;
		}
		
		/**
		 * Retorna el valor del atributo Nombre de la Unidad de Inversion
		 * @return
		 */
		public String getNombreUnidadInversion() {
			return nombreUnidadInversion;
		}
		/**
		 * Asigna valor al atributo Nombre de la Unidad de Inversion
		 * @param nombreUnidadInversion
		 */	
		public void setNombreUnidadInversion(String nombreUnidadInversion) {
			this.nombreUnidadInversion = nombreUnidadInversion;
		}
		
		/**
		 * Retorna el valor del atributo Descripcion de la Unidad de Inversion
		 * @return
		 */
		public String getDescrUnidadInversion() {
			return descrUnidadInversion;
		}
		/**
		 * Asigna valor al atributo Descripcion de la Unidad de Inversion
		 * @param descrUnidadInversion
		 */		
		public void setDescrUnidadInversion(String descrUnidadInversion) {
			this.descrUnidadInversion = descrUnidadInversion;
		}
			
		/**
		 * Retorna el valor del atributo Identificador del Instrumento Financiero que es la Unidad de Inversion
		 * @return
		 */
		public String getDescrInstrumentoFinanciero() {
			return descrInstrumentoFinanciero;
		}
		/**
		 * Asigna valor al atributo Identificador del Instrumento Financiero que es la Unidad de Inversion
		 * @param descrInstrumentoFinanciero
		 */	
		public void setDescrInstrumentoFinanciero(String idInstrumentoFinanciero) {
			this.descrInstrumentoFinanciero = idInstrumentoFinanciero;
		}
				
		/**
		 * Retorna el valor del atributo Emision a la cual pertenece la Unidad de Inversion
		 * @return
		 */
		public String getEmisionUnidadInversion() {
			return emisionUnidadInversion;
		}
		/**
		 * Asigna valor al atributo Emision a la cual pertenece la Unidad de Inversion
		 * @param emisionUnidadInversion
		 */	
		public void setEmisionUnidadInversion(String emisionUnidadInversion) {
			this.emisionUnidadInversion = emisionUnidadInversion;
		}
		
		/**
		 * Retorna el valor del atributo Serie a la cual pertenece la Unidad de Inversion
		 * @return
		 */
		public String getSerieUnidadInversion() {
			return serieUnidadInversion;
		}
		/**
		 * Asigna valor al atributo Serie a la cual pertenece la Unidad de Inversion
		 * @param serieUnidadInversion
		 */	
		public void setSerieUnidadInversion(String serieUnidadInversion) {
			this.serieUnidadInversion = serieUnidadInversion;
		}
		
		/**
		 * Retorna el valor del atributo Tasa de Cambio que se aplica en el proceso de Toma de Orden 
		 * @return
		 */
		public String getTasaCambio() {
			return tasaCambio;
		}
		/**
		 * Asigna valor al atributo Tasa de Cambio que se aplica en el proceso de Toma de Orden 
		 * @param tasaCambio
		 */	
		public void setTasaCambio(String tasaCambio) {
			this.tasaCambio = tasaCambio;
		}
		
		/**
		 * Retorna el valor del atributo Fecha de emision de la Unidad de Inversion
		 * @return
		 */
		public String getFechaEmisionUI() {
			return fechaEmisionUI;
		}
		/**
		 * Asigna valor al atributo Fecha de emision de la Unidad de Inversion
		 * @param fechaEmisionUI
		 */	
		public void setFechaEmisionUI(String fechaEmisionUI) {
			this.fechaEmisionUI = fechaEmisionUI;
		}
		
		/**
		 * Retorna el valor del atributo Indicador para venta a los Empleados
		 * @return
		 */
		public String getIndicaVtaEmpleados() {
			return indicaVtaEmpleados;
		}
		/**
		 * Asigna valor al atributo Indicador para venta a los Empleados
		 * @param indicaVtaEmpleados
		 */	
		public void setIndicaVtaEmpleados(String indicaVtaEmpleados) {
			this.indicaVtaEmpleados = indicaVtaEmpleados;
		}
		
		/**
		 * Retorna el valor del atributo Fecha de adjudicacion de la Unidad de Inversion
		 * @return
		 */
		public String getFechaAdjudicacionUI() {
			return fechaAdjudicacionUI;
		}
		/**
		 * Asigna valor al atributo Fecha de adjudicacion de la Unidad de Inversion
		 * @param fechaAdjudicacionUI
		 */	
		public void setFechaAdjudicacionUI(String fechaAdjudicacionUI) {
			this.fechaAdjudicacionUI = fechaAdjudicacionUI;
		}
		
		/**
		 * Retorna el valor del atributo Fecha de liquidacion de la Unidad de Inversion
		 * @return
		 */
		public String getFechaLiquidaUI() {
			return fechaLiquidaUI;
		}
		/**
		 * Asigna valor al atributo Fecha de liquidacion de la Unidad de Inversion
		 * @param fechaLiquidaUI
		 */	
		public void setFechaLiquidaUI(String fechaLiquidaUI) {
			this.fechaLiquidaUI = fechaLiquidaUI;
		}
		
		/**
		 * Retorna el valor del atributo Indicador se puede pactar recompra
		 * @return
		 */
		public String getIndicaRecompra() {
			return indicaRecompra;
		}
		/**
		 * Asigna valor al atributo Indicador se puede pactar recompra
		 * @param indicaRecompra
		 */	
		public void setIndicaRecompra(String indicaRecompra) {
			this.indicaRecompra = indicaRecompra;
		}
		
		/**
		 * Retorna el valor del atributo Descripción de la moneda
		 * @return descrMoneda
		 */
		public String getDescrMoneda() {
			return descrMoneda;
		}
		/**
		 * Asigna valor al atributo Descripción de la moneda
		 * @param descrMoneda
		 */	
		public void setDescrMoneda(String descrMoneda) {
			this.descrMoneda = descrMoneda;
		}
		
		/**
		 * Retorna el valor del atributo Monto total de Inventario disponible para la Venta
		 * @return
		 */
		public String getTotalInversion() {
			return inventarioTotal;
		}
		/**
		 * Asigna valor al atributo Monto total de Inventario disponible para la Venta
		 * @param totalInversion
		 */	
		public void setTotalInversion(String totalInversion) {
			this.inventarioTotal = totalInversion;
		}
		/**
		 * Retorna el valor del atributo Identificador del Status que tiene la Unidad de Inversion
		 * @return
		 */
		public String getIdStatus() {
			return idStatus;
		}
		/**
		 * Asigna valor al atributo Identificador del Status que tiene la Unidad de Inversion
		 * @param idStatus
		 */
		public void setIdStatus(String idStatus) {
			this.idStatus = idStatus;
		}
		
		/**
		 * Devuelve el valor de la Nombre del status que tiene la Unidad de Inversion
		 * @return descrStatus
		 */
		public String getDescrStatus() {
			return descrStatus;
		}
		/**
		 * Asigna valor a la Nombre del status que tiene la Unidad de Inversion
		 * @param descrStatus
		 */	
		public void setDescrStatus(String descrStatus) {
			this.descrStatus = descrStatus;
		}


		/**
		 * @return the bloterId
		 */
		public String getBloterId() {
			return bloterId;
		}


		/**
		 * @param bloterId the bloterId to set
		 */
		public void setBloterId(String bloterId) {
			this.bloterId = bloterId;
		}


		/**
		 * @return the tipoPersonasValidas
		 */
		public TipoPersonasValidas getTipoPersonasValidas() {
			return tipoPersonasValidas;
		}


		/**
		 * @param tipoPersonasValidas the tipoPersonasValidas to set
		 */
		public void setTipoPersonasValidas(TipoPersonasValidas tipoPersonasValidas) {
			this.tipoPersonasValidas = tipoPersonasValidas;
		}


		/**
		 * @return the fechaDesdeTomaOrden
		 */
		public String getFechaDesdeTomaOrden() {
			return fechaDesdeTomaOrden;
		}


		/**
		 * @param fechaDesdeTomaOrden the fechaDesdeTomaOrden to set
		 */
		public void setFechaDesdeTomaOrden(String fechaDesdeTomaOrden) {
			this.fechaDesdeTomaOrden = fechaDesdeTomaOrden;
		}


		/**
		 * @return the fechaHastaTomaOrden
		 */
		public String getFechaHastaTomaOrden() {
			return fechaHastaTomaOrden;
		}


		/**
		 * @param fechaHastaTomaOrden the fechaHastaTomaOrden to set
		 */
		public void setFechaHastaTomaOrden(String fechaHastaTomaOrden) {
			this.fechaHastaTomaOrden = fechaHastaTomaOrden;
		}


		/**
		 * @return the horaDesdeTomaOrden
		 */
		public String getHoraDesdeTomaOrden() {
			return horaDesdeTomaOrden;
		}


		/**
		 * @param horaDesdeTomaOrden the horaDesdeTomaOrden to set
		 */
		public void setHoraDesdeTomaOrden(String horaDesdeTomaOrden) {
			this.horaDesdeTomaOrden = horaDesdeTomaOrden;
		}


		/**
		 * @return the horaDesdeUltimoDia
		 */
		public String getHoraDesdeUltimoDia() {
			return horaDesdeUltimoDia;
		}


		/**
		 * @param horaDesdeUltimoDia the horaDesdeUltimoDia to set
		 */
		public void setHoraDesdeUltimoDia(String horaDesdeUltimoDia) {
			this.horaDesdeUltimoDia = horaDesdeUltimoDia;
		}


		/**
		 * @return the horaHastaTomaOrden
		 */
		public String getHoraHastaTomaOrden() {
			return horaHastaTomaOrden;
		}


		/**
		 * @param horaHastaTomaOrden the horaHastaTomaOrden to set
		 */
		public void setHoraHastaTomaOrden(String horaHastaTomaOrden) {
			this.horaHastaTomaOrden = horaHastaTomaOrden;
		}


		/**
		 * @return the horaHastaUltimoDia
		 */
		public String getHoraHastaUltimoDia() {
			return horaHastaUltimoDia;
		}


		/**
		 * @param horaHastaUltimoDia the horaHastaUltimoDia to set
		 */
		public void setHoraHastaUltimoDia(String horaHastaUltimoDia) {
			this.horaHastaUltimoDia = horaHastaUltimoDia;
		}


		/**
		 * @return the titulosAsociados
		 */
		public TitulosAsociados getTitulosAsociados() {
			return titulosAsociados;
		}


		/**
		 * @param titulosAsociados the titulosAsociados to set
		 */
		public void setTitulosAsociados(TitulosAsociados titulosAsociados) {
			this.titulosAsociados = titulosAsociados;
		}


		/**
		 * @return the tipoInstrumento
		 */
		public String getTipoInstrumento() {
			return tipoInstrumento;
		}


		/**
		 * @param tipoInstrumento the tipoInstrumento to set
		 */
		public void setTipoInstrumento(String tipoInstrumento) {
			this.tipoInstrumento = tipoInstrumento;
		}


		/**
		 * @return the inventarioMontoMaximo
		 */
		public String getInventarioMontoMaximo() {
			return inventarioMontoMaximo;
		}


		/**
		 * @param inventarioMontoMaximo the inventarioMontoMaximo to set
		 */
		public void setInventarioMontoMaximo(String inventarioMontoMaximo) {
			this.inventarioMontoMaximo = inventarioMontoMaximo;
		}


		/**
		 * @return the inventarioMontoMinimo
		 */
		public String getInventarioMontoMinimo() {
			return inventarioMontoMinimo;
		}


		/**
		 * @param inventarioMontoMinimo the inventarioMontoMinimo to set
		 */
		public void setInventarioMontoMinimo(String inventarioMontoMinimo) {
			this.inventarioMontoMinimo = inventarioMontoMinimo;
		}


		/**
		 * @return the inventarioTotal
		 */
		public String getInventarioTotal() {
			return inventarioTotal;
		}


		/**
		 * @param inventarioTotal the inventarioTotal to set
		 */
		public void setInventarioTotal(String inventarioTotal) {
			this.inventarioTotal = inventarioTotal;
		}


		/**
		 * @return the inventarioDisponible
		 */
		public String getInventarioDisponible() {
			return inventarioDisponible;
		}


		/**
		 * @param inventarioDisponible the inventarioDisponible to set
		 */
		public void setInventarioDisponible(String inventarioDisponible) {
			this.inventarioDisponible = inventarioDisponible;
		}
}

