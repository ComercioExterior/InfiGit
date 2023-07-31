package com.bdv.infi.data;

import java.io.Serializable;
import java.math.BigDecimal;

import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.util.Utilitario;

public class SolicitudDICOM implements Serializable{
	/**
	 * Clase entidad que contiene los datos de una solicitud DICOM
	 */
	private static final long serialVersionUID = 1L;
	
	//CABECERA ARCHIVO BCV
	private String idSubasta;
	private java.util.Date fechaJornada;
	private BigDecimal totalRegistrosDemanda;
	private BigDecimal totalMontoDolarDemanda;
	private BigDecimal totalMontoBolivaresDemanda;
	private BigDecimal totalRegistrosOferta;
	private BigDecimal totalMontoDolarOferta;
	private BigDecimal totalMontoBolivaresOferta;
	
	private String idOperacion;
	private String tipoOperacion;
	private String tipoCliente;
	private Long cedRifCliente;
	private String numCuentaNacional;
	private String numCuentaMonedaExtranjera;
	private BigDecimal montoOperacionVEF;
	private BigDecimal montoOperacionUSD;	
	private String codigoDivisa;
	
	private java.util.Date fechaOperacion;
	private java.util.Date fechaValorOperacion;
	private String nombreCliente;
	private String telefono;
	private String mail;
	private String numRetencionCap;
	private String horaBloqueoCap;
	private String divisaMonNacional;
	private String numRetencionCom;
	
	private BigDecimal montoComision;	
	
	private BigDecimal porcentajeComision;	
	
	private String divisaMonExtranjera;
	private BigDecimal tasaCambio;
	private String respuestaArchivo;
	private BigDecimal nroOperacionDebito;
	private BigDecimal nroOperacionCredito;
	private Integer estatusRegistro=new Integer(0);
	private Integer estatusNotificacionWS=new Integer(0);
	private String idJornada;
	private String CodMoneda;
	private Long idUnidadInversion=new Long(0);
	private int codigoRespuesta;
	
	private BigDecimal ptcComisionIGTF;
	private BigDecimal montoComisionIGTF;
	private BigDecimal montoTotalRetencion;
	
	
	//CAMPOS ASOCIADOS AL REQUERIMIENTO DICOM INTERBANCARIO
	private BigDecimal montoDesbloqueoBs;
	private BigDecimal montoDesbloqueoDiv;
	private String monedaDesbloqueoDiv;	
	
	private BigDecimal montoCreditoBs;
	private BigDecimal montoCreditoDiv;
	private String monedaCreditoDiv;
	private BigDecimal montoDebitoBs;
	private BigDecimal montoDebitoDiv;
	private String monedaDebitoDiv;
	
	private int procesoTipo;
	
	
	public int getProcesoTipo() {
		return procesoTipo;
	}

	public void setProcesoTipo(int procesoTipo) {
		this.procesoTipo = procesoTipo;
	}

	public BigDecimal getMontoDesbloqueoBs() {
		return montoDesbloqueoBs;
	}

	public void setMontoDesbloqueoBs(BigDecimal montoCreditoBs) {
		this.montoDesbloqueoBs = montoCreditoBs;
	}

	public BigDecimal getMontoDesbloqueoDiv() {
		return montoDesbloqueoDiv;
	}

	public void setMontoDesbloqueoDiv(BigDecimal montoCreditoDiv) {
		this.montoDesbloqueoDiv = montoCreditoDiv;
	}

	public String getMonedaDesbloqueoDiv() {
		return monedaDesbloqueoDiv;
	}

	public void setMonedaDesbloqueoDiv(String monedaCreditoDiv) {
		this.monedaDesbloqueoDiv = monedaCreditoDiv;
	}
	
	public BigDecimal getMontoCreditoBs() {
		return montoCreditoBs;
	}

	public void setMontoCreditoBs(BigDecimal montoCreditoBs) {
		this.montoCreditoBs = montoCreditoBs;
	}

	public BigDecimal getMontoCreditoDiv() {
		return montoCreditoDiv;
	}

	public void setMontoCreditoDiv(BigDecimal montoCreditoDiv) {
		this.montoCreditoDiv = montoCreditoDiv;
	}

	public String getMonedaCreditoDiv() {
		return monedaCreditoDiv;
	}

	public void setMonedaCreditoDiv(String monedaCreditoDiv) {
		this.monedaCreditoDiv = monedaCreditoDiv;
	}

	public BigDecimal getMontoDebitoBs() {
		return montoDebitoBs;
	}

	public void setMontoDebitoBs(BigDecimal montoDebitoBs) {
		this.montoDebitoBs = montoDebitoBs;
	}

	public BigDecimal getMontoDebitoDiv() {
		return montoDebitoDiv;
	}

	public void setMontoDebitoDiv(BigDecimal montoDebitoDiv) {
		this.montoDebitoDiv = montoDebitoDiv;
	}

	public String getMonedaDebitoDiv() {
		return monedaDebitoDiv;
	}

	public void setMonedaDebitoDiv(String monedaDebitoDiv) {
		this.monedaDebitoDiv = monedaDebitoDiv;
	}

	
	public BigDecimal getPtcComisionIGTF() {
		return ptcComisionIGTF;
	}
	
	public void setPtcComisionIGTF(String ptcComisionIGTF) {		
		this.ptcComisionIGTF = Utilitario.getStringToBigDecimal(ptcComisionIGTF,EstructuraArchivoOperacionesDICOM.PORC_COM_IGTF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.PORC_COM_IGTF_LONG_DECIMALES.getValor());
	}
	
	/*public void setPtcComisionIGTF(BigDecimal ptcComisionIGTF) {
		this.ptcComisionIGTF = ptcComisionIGTF;
	}*/
	
	public BigDecimal getMontoComisionIGTF() {
		return montoComisionIGTF;
	}
	
	/*public void setMontoComisionIGTF(BigDecimal montoComisionIGTF) {
		this.montoComisionIGTF = montoComisionIGTF;
	}*/
	
	public void setMontoComisionIGTF(String montoComisionIGTF) {		
		this.montoComisionIGTF = Utilitario.getStringToBigDecimal(montoComisionIGTF,EstructuraArchivoOperacionesDICOM.MONTO_COM_IGTF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_COM_IGTF_LONG_DECIMALES.getValor());
	}
	
	public BigDecimal getMontoTotalRetencion() {
		return montoTotalRetencion;
	}
	
	public void setMontoTotalRetencion(String montoTotalRetencion) {		
		this.montoTotalRetencion = Utilitario.getStringToBigDecimal(montoTotalRetencion,EstructuraArchivoOperacionesDICOM.MONTO_TOTAL_RET_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_TOTAL_RET_LONG_DECIMALES.getValor());
	}
	/*public void setMontoTotalRetencion(BigDecimal montoTotalRetencion) {
		this.montoTotalRetencion = montoTotalRetencion;
	}*/
	
	public int getCodigoRespuesta() {
		return codigoRespuesta;
	}
	public void setCodigoRespuesta(int codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}
	private Integer tipoSolicitud=new Integer(0);
	
	public Integer getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(Integer tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public String getIdOperacion() {
		return idOperacion;
	}
	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public String getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public Long getCedRifCliente() {
		return cedRifCliente;
	}
	public void setCedRifCliente(Long cedRifCliente) {
		this.cedRifCliente = cedRifCliente;
	}
	public String getNumCuentaNacional() {
		return numCuentaNacional;
	}
	public void setNumCuentaNacional(String numCuentaNacional) {
		this.numCuentaNacional = numCuentaNacional;
	}
	public String getNumCuentaMonedaExtranjera() {
		return numCuentaMonedaExtranjera;
	}
	public void setNumCuentaMonedaExtranjera(String numCuentaMonedaExtranjera) {
		this.numCuentaMonedaExtranjera = numCuentaMonedaExtranjera;
	}
	public BigDecimal getMontoOperacionVEF() {
		return montoOperacionVEF;
	}
	public void setMontoOperacionVEF(BigDecimal montoOperacionVEF) {
		this.montoOperacionVEF = montoOperacionVEF;
	}
	
	public void setMontoOperacionVEF(String montoOperacionVEF) {		
		this.montoOperacionVEF = Utilitario.getStringToBigDecimal(montoOperacionVEF,EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
	}
	
	public BigDecimal getMontoOperacionUSD() {
		return montoOperacionUSD;
	}
	public void setMontoOperacionUSD(BigDecimal montoOperacionUSD) {
		this.montoOperacionUSD = montoOperacionUSD;
	}
	
	public void setMontoOperacionUSD(String montoOperacionUSD) {		
		this.montoOperacionUSD = Utilitario.getStringToBigDecimal(montoOperacionUSD,EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG_DECIMALES.getValor());
	}
	
	
	public String getCodigoDivisa() {
		return codigoDivisa;
	}

	public void setCodigoDivisa(String codigoDivisa) {
		this.codigoDivisa = codigoDivisa;
	}

	public java.util.Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(java.util.Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public java.sql.Date getFechaOperacionSqlDate(){
		return (fechaOperacion!=null?new java.sql.Date(fechaOperacion.getTime()):null);
	}
	public java.util.Date getFechaValorOperacion() {
		return fechaValorOperacion;
	}
	public java.sql.Date getFechaValorOperacionSqlDate(){
		return (fechaValorOperacion!=null?new java.sql.Date(fechaValorOperacion.getTime()):null);
	}
	public void setFechaValorOperacion(java.util.Date fechaValorOperacion) {
		this.fechaValorOperacion = fechaValorOperacion;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getNumRetencionCap() {
		return numRetencionCap;
	}
	public void setNumRetencionCap(String numRetencionCap) {
		this.numRetencionCap = numRetencionCap;
	}
	public String getHoraBloqueoCap() {
		return horaBloqueoCap;
	}
	public void setHoraBloqueoCap(String horaBloqueoCap) {
		this.horaBloqueoCap = horaBloqueoCap;
	}
	public String getDivisaMonNacional() {
		return divisaMonNacional;
	}
	public void setDivisaMonNacional(String divisaMonNacional) {
		this.divisaMonNacional = divisaMonNacional;
	}
	public String getNumRetencionCom() {
		return numRetencionCom;
	}
	public void setNumRetencionCom(String numRetencionCom) {
		this.numRetencionCom = numRetencionCom;
	}
	public BigDecimal getMontoComision() {
		return montoComision;
	}
	public void setMontoComision(BigDecimal montoComision) {
		this.montoComision = montoComision;
	}
	
	public void setMontoComision(String montoComision) {		
		this.montoComision = Utilitario.getStringToBigDecimal(montoComision,EstructuraArchivoOperacionesDICOM.MONTO_OPER_COM_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
	}
	
	public BigDecimal getPorcentajeComision() {
		return porcentajeComision;
	}
	public void setPorcentajeComision(BigDecimal porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}
	
	public void setPorcentajeComision(String porcentajeComision) {		
		this.porcentajeComision = Utilitario.getStringToBigDecimal(porcentajeComision,EstructuraArchivoOperacionesDICOM.PORC_COM_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
	}
	
	public String getDivisaMonExtranjera() {
		return divisaMonExtranjera;
	}
	public void setDivisaMonExtranjera(String divisaMonExtranjera) {
		this.divisaMonExtranjera = divisaMonExtranjera;
	}
	
	public BigDecimal getTasaCambio() {
		return tasaCambio;
	}
	
	public void setTasaCambio(String tasaCambio) {		
		this.tasaCambio = Utilitario.getStringToBigDecimal(tasaCambio,EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG_DECIMALES.getValor());
	}
	
	public void setTasaCambio(BigDecimal tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	public String getRespuestaArchivo() {
		return respuestaArchivo;
	}
	public void setRespuestaArchivo(String respuestaArchivo) {
		this.respuestaArchivo = respuestaArchivo;
	}
	public BigDecimal getNroOperacionDebito() {
		return nroOperacionDebito;
	}
	public void setNroOperacionDebito(BigDecimal nroOperacionDebito) {
		this.nroOperacionDebito = nroOperacionDebito;
	}
	public BigDecimal getNroOperacionCredito() {
		return nroOperacionCredito;
	}
	public void setNroOperacionCredito(BigDecimal nroOperacionCredito) {
		this.nroOperacionCredito = nroOperacionCredito;
	}
	public Integer getEstatusRegistro() {
		return estatusRegistro;
	}
	public void setEstatusRegistro(Integer estatusRegistro) {
		this.estatusRegistro = estatusRegistro;
	}
	public Integer getEstatusNotificacionWS() {
		return estatusNotificacionWS;
	}
	public void setEstatusNotificacionWS(Integer estatusNotificacionWS) {
		this.estatusNotificacionWS = estatusNotificacionWS;
	}
	public String getIdJornada() {
		return idJornada;
	}
	public void setIdJornada(String idJornada) {
		this.idJornada = idJornada;
	}
	public String getCodMoneda() {
		return CodMoneda;
	}
	public void setCodMoneda(String CodMoneda) {
		this.CodMoneda = CodMoneda;
	}
	public Long getIdUnidadInversion() {
		return idUnidadInversion;
	}
	public void setIdUnidadInversion(Long idUnidadInversion) {
		this.idUnidadInversion = idUnidadInversion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/*public BigDecimal getStringToBigDecimal(String monto,int cantidadEnteros,int cantidadDecimales) {
		BigDecimal result=null;
		if(monto!=null && monto.length()>0){
			try{												
				 String entero=monto.substring(0,cantidadEnteros);
				 String decimal=monto.substring(cantidadEnteros,+cantidadEnteros+cantidadDecimales);
				 String numeroFinal=null;
				 numeroFinal=new BigDecimal(entero).toString().concat(".").concat(new BigDecimal(decimal).toString());
				 result= new BigDecimal(numeroFinal);
			}catch(NumberFormatException ex){
				throw ex;
			}
		}
		return result;
	}*/
	
	public String getIdSubasta() {
		return idSubasta;
	}
	public void setIdSubasta(String idSubasta) {
		this.idSubasta = idSubasta;
	}
	public java.util.Date getFechaJornada() {
		return fechaJornada;
	}
	public void setFechaJornada(java.util.Date fechaJornada) {
		this.fechaJornada = fechaJornada;
	}
	public BigDecimal getTotalRegistrosDemanda() {
		return totalRegistrosDemanda;
	}
	public void setTotalRegistrosDemanda(BigDecimal totalRegistrosDemanda) {
		this.totalRegistrosDemanda = totalRegistrosDemanda;
	}
	public BigDecimal getTotalMontoDolarDemanda() {
		return totalMontoDolarDemanda;
	}
	public void setTotalMontoDolarDemanda(BigDecimal totalMontoDolarDemanda) {
		this.totalMontoDolarDemanda = totalMontoDolarDemanda;
	}
	public BigDecimal getTotalMontoBolivaresDemanda() {
		return totalMontoBolivaresDemanda;
	}
	public void setTotalMontoBolivaresDemanda(BigDecimal totalMontoBolivaresDemanda) {
		this.totalMontoBolivaresDemanda = totalMontoBolivaresDemanda;
	}
	public BigDecimal getTotalRegistrosOferta() {
		return totalRegistrosOferta;
	}
	public void setTotalRegistrosOferta(BigDecimal totalRegistrosOferta) {
		this.totalRegistrosOferta = totalRegistrosOferta;
	}
	public BigDecimal getTotalMontoDolarOferta() {
		return totalMontoDolarOferta;
	}
	public void setTotalMontoDolarOferta(BigDecimal totalMontoDolarOferta) {
		this.totalMontoDolarOferta = totalMontoDolarOferta;
	}
	public BigDecimal getTotalMontoBolivaresOferta() {
		return totalMontoBolivaresOferta;
	}
	public void setTotalMontoBolivaresOferta(BigDecimal totalMontoBolivaresOferta) {
		this.totalMontoBolivaresOferta = totalMontoBolivaresOferta;
	}	
	
	public String toStringCabecera (){
		StringBuffer sb = new StringBuffer();		
		sb.append("idSubasta: "+idSubasta);
		sb.append(", totalRegistrosDemanda: "+totalRegistrosDemanda);
		sb.append(", totalMontoDolarDemanda: "+totalMontoDolarDemanda);
		sb.append(", totalMontoBolivaresDemanda: "+totalMontoBolivaresDemanda);
		sb.append(", totalRegistrosOferta: "+totalRegistrosOferta);
		sb.append(", totalMontoDolarOferta: "+totalMontoDolarOferta);
		sb.append(", totalMontoBolivaresOferta: "+totalMontoBolivaresOferta);
		return sb.toString();
		
	}
	
}
