package com.bdv.infi.data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import megasoft.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
//import com.bdv.infi.logic.interfaces.EstatusOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstatusOperacionesORO;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesORO;
//import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesORO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.EnvioOperacionesPreaprobadasDICOM;
import com.bdv.infi.logic.interfaz_operaciones_ORO.EnvioOperacionesORO;
import com.bdv.infi.util.Utilitario;
/**
 * 
 * @author NM11383 Alexander Rincon 
 * @since 10/09/2018
 * @version 1.0.0
 * Manejo de los Formatos para lectura desde BCV
 * para Enviar al MAINFRAME y tambien para leer de Mainframe y enviar al BCV
 */
public class FormatoOperacionesORO {
	
	private  HashMap<String, String> parametrosRecepcionORO;	
	//CABECERA ARCHIVO BCV
	String idSubasta;
	String fechaJornada;
	String totalRegistrosDemanda;
	String totalMontoDolarDemanda;
	String totalMontoBolivaresDemanda;
	String totalRegistrosOferta;
	String totalMontoDolarOferta;
	String totalMontoBolivaresOferta;
	
	//CABECERA ARCHIVO MAINFRAME
	String nroJornada;
	String tipoRegistroCabecera="01";
	String fechaInicio;
	String fechaFin;
	String idCiclo; //Número de Proceso
	String fechaProceso;
	String monedaSubasta; //Número de Proceso		

	String respuestaArchivo;
		
	//CUERPO DE ARCHIVO MAINFRAME	
	String tipoRegistroCuerpo="02"; 
	String idOperacion="";
	String tipoCliente="";	
	String cedRifCliente="";
	String nombreCliente="";
	String telefono="";
	String mail="";
	String numCuentaNacional="";
	String numRetencionCap="";
	String horaBloqueoCap="";	
	String montoOperacionVEF;
	//BigDecimal montoOperacionVEF;
	
	String divisaMonNacional="";
	String numRetencionCom="";
	String montoComision;
	//BigDecimal montoComision;
	String porcentajeComision="";
	String fechaOperacion;
	String numCuentaMonedaExtranjera="";
	String montoOperacionUSD;	
	//BigDecimal montoOperacionUSD;

	String divisaMonExtranjera="";
	String codBCVdivisaExtranjera="";
	String tasaCambio;
	//BigDecimal tasaCambio;
	String fechaValorOperacion;
	String indicadorRegistro="";//Indica si la solicitud fue aprobada o rechazada
	//String motivoRechazo="";//
	String tipoOperacion="";//Indica si la operaciones  Compra(C) o Venta (V) (Campo de uso exclusivo de Mainframe)
	String nroOperacionDebito;
	String codigoRespuesta;
	String nroOperacionCredito;		
	BigDecimal tasaCambioOperacion;
	
	String tipoRegistro;
	
	String ptcComisionIGTF;
	String montoComisionIGTF;
	String montoTotalRetencion;

	public void setParamtrosSistemaORO(HashMap<String, String> parametrosRecepcionORO){
		this.parametrosRecepcionORO=parametrosRecepcionORO;
	}
	
	public String getPtcComisionIGTF() {
		return ptcComisionIGTF;
	}

	public void setPtcComisionIGTF(String ptcComisionIGTF) {
		this.ptcComisionIGTF = ptcComisionIGTF;
	}

	public String getMontoComisionIGTF() {
		return montoComisionIGTF;
	}

	public void setMontoComisionIGTF(String montoComisionIGTF) {
		this.montoComisionIGTF = montoComisionIGTF;
	}

	public String getMontoTotalRetencion() {
		return montoTotalRetencion;
	}

	public void setMontoTotalRetencion(String montoTotalRetencion) {
		this.montoTotalRetencion = montoTotalRetencion;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getNroJornada() {
		return nroJornada;
	}

	public void setNroJornada(String nroJornada) {
		this.nroJornada = nroJornada;
	}

	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}
	
	public int getCodigoRespuestaInt() {
		return Integer.valueOf(codigoRespuesta).intValue();
	}

	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	

	public void setTasaCambioOperacion(String tasaCambioOperacion) {
		if(tasaCambioOperacion!=null && tasaCambioOperacion.length()>0){
			try{								
				//this.tasaCambioOperacion = new BigDecimal(tasaCambioOperacion);				
				 String entero=tasaCambioOperacion.substring(0,6);
				 String decimal=tasaCambioOperacion.substring(6,11);
				 String numeroFinal=null;
				 numeroFinal=new BigDecimal(entero).toString().concat(".").concat(new BigDecimal(decimal).toString());
				 this.tasaCambioOperacion= new BigDecimal(numeroFinal);
			}catch(NumberFormatException ex){
				this.tasaCambioOperacion=new BigDecimal(0);	
			}
		}
	}
	
	
	public BigDecimal getTasaCambioOperacion() {
		return Utilitario.getStringToBigDecimal(tasaCambio,EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG_DECIMALES.getValor());	
	}

	public String getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}
	
	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getCedRifCliente() {
		return cedRifCliente;
	}

	public void setCedRifCliente(String cedRifCliente) {
		this.cedRifCliente = cedRifCliente;
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

	public String getNumCuentaNacional() {
		return numCuentaNacional;
	}

	public void setNumCuentaNacional(String numCuentaNacional) {
		this.numCuentaNacional = numCuentaNacional;
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

	public String getMontoOperacionVEF() {
		return montoOperacionVEF;
	}

	public void setMontoOperacionVEF(String montoOperacionVEF) {
		this.montoOperacionVEF = montoOperacionVEF;
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

	public String getMontoComision() {
		return montoComision;
	}
	public void setMontoComision(String montoComision) {
		this.montoComision = montoComision;
	}

	public String getPorcentajeComision() {
		return porcentajeComision;
	}
	public void setPorcentajeComision(String porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

	public String getNumCuentaMonedaExtranjera() {
		return numCuentaMonedaExtranjera;
	}
	public void setNumCuentaMonedaExtranjera(String numCuentaMonedaExtranjera) {
		this.numCuentaMonedaExtranjera = numCuentaMonedaExtranjera;
	}

	public String getMontoOperacionUSD() {
		return montoOperacionUSD;
	}
	public void setMontoOperacionUSD(String montoOperacionUSD) {
		this.montoOperacionUSD = montoOperacionUSD;
	}

	public String getDivisaMonExtranjera() {
		return divisaMonExtranjera;
	}
	public void setDivisaMonExtranjera(String divisaMonExtranjera) {
		this.divisaMonExtranjera = divisaMonExtranjera;
	}

	
	public String getCodBCVdivisaExtranjera() {
		return codBCVdivisaExtranjera;
	}
	public void setCodBCVdivisaExtranjera(String codBCVdivisaExtranjera) {
		this.codBCVdivisaExtranjera = codBCVdivisaExtranjera;
	}

	public String getTasaCambio() {
		return tasaCambio;
	}
	public void setTasaCambio(String tasaCambio) {
		this.tasaCambio = tasaCambio;
	}

	public String getFechaValorOperacion() {
		return fechaValorOperacion;
	}
	public void setFechaValorOperacion(String fechaValorOperacion) {
		this.fechaValorOperacion = fechaValorOperacion;
	}

	public String getIndicadorRegistro() {
		return indicadorRegistro;
	}
	public void setIndicadorRegistro(String indicadorRegistro) {
		this.indicadorRegistro = indicadorRegistro;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}				
	
	public String getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(String fechaOperacion) {
		if(fechaOperacion!=null && fechaOperacion.length()>0)		
			this.fechaOperacion = fechaOperacion.trim();
	}
	
	public String getIdCiclo() {
		return idCiclo;
	}
	public void setIdCiclo(String idCiclo) {
		if(idCiclo!=null && idCiclo.length()>0)
		this.idCiclo = idCiclo.trim();
	}
	
	//TTS_543_DICOM_MULTIMONEDA_NM26659_12/09/2017 Se agrega la moneda asociada a la subasta
	public String getMonedaSubasta() {
		return monedaSubasta;
	}

	public void setMonedaSubasta(String monedaSubasta) {
		this.monedaSubasta = monedaSubasta;
	}
	
	public String getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public String getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaProceso() {
		return fechaProceso;
	}
	
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getRespuestaArchivo() {
		return respuestaArchivo;
	}
	
	public void setRespuestaArchivo(String respuestaArchivo) {
		this.respuestaArchivo = respuestaArchivo;
	}	
		
	public boolean esValidoFormatoRecepcion() {
		return(respuestaArchivo!=null&&respuestaArchivo.length()>0);//&&respuestaArchivo.substring(0,2).equalsIgnoreCase("OK"));		
	}	
	

	public String getTasaCambioOperacionFormatted() {
		return tasaCambioOperacion.toString();
	}
	
	

	public void setTasaCambioOperacion(BigDecimal tasaCambioOperacion) {
				
				 this.tasaCambioOperacion= tasaCambioOperacion;
		
	}
	
	public String getNroOperacionDebito() {
		return nroOperacionDebito;
	}

	public void setNroOperacionDebito(String nroOperacionDebito) {
		this.nroOperacionDebito = nroOperacionDebito;
	}

	public String getNroOperacionCredito() {
		return nroOperacionCredito;
	}

	public void setNroOperacionCredito(String nroOperacionCredito) {
		this.nroOperacionCredito = nroOperacionCredito;
	}
	
	public String getMontoFormatted(BigDecimal monto, int parteEntera, int parteDecimal) {
		String entero,decimal;
		if(monto==null){
			monto=new BigDecimal(0);
		}		
		entero=Utilitario.rellenarCaracteres(monto.toBigInteger().toString(),'0',parteEntera,false,true);
		monto=(monto.subtract(new BigDecimal(entero)));
		decimal=Utilitario.rellenarCaracteres(monto.toString().replaceFirst("0", "").replaceFirst(".", ""),'0',parteDecimal,true,true);
		return entero+decimal;
	}	
	public String getIdSubasta() {
		return idSubasta;
	}

	public void setIdSubasta(String idSubasta) {
		this.idSubasta = idSubasta;
	}

	public String getFechaJornada() {
		return fechaJornada;
	}

	public void setFechaJornada(String fechaJornada) {
		this.fechaJornada = fechaJornada;
	}

	public String getTotalRegistrosDemanda() {
		return totalRegistrosDemanda;
	}

	public void setTotalRegistrosDemanda(String totalRegistrosDemanda) {
		this.totalRegistrosDemanda = totalRegistrosDemanda;
	}

	public String getTotalMontoDolarDemanda() {
		return totalMontoDolarDemanda;
	}

	public void setTotalMontoDolarDemanda(String totalMontoDolarDemanda) {
		this.totalMontoDolarDemanda = totalMontoDolarDemanda;
	}

	public String getTotalMontoBolivaresDemanda() {
		return totalMontoBolivaresDemanda;
	}

	public void setTotalMontoBolivaresDemanda(String totalMontoBolivaresDemanda) {
		this.totalMontoBolivaresDemanda = totalMontoBolivaresDemanda;
	}

	public String getTotalRegistrosOferta() {
		return totalRegistrosOferta;
	}

	public void setTotalRegistrosOferta(String totalRegistrosOferta) {
		this.totalRegistrosOferta = totalRegistrosOferta;
	}

	public String getTotalMontoDolarOferta() {
		return totalMontoDolarOferta;
	}

	public void setTotalMontoDolarOferta(String totalMontoDolarOferta) {
		this.totalMontoDolarOferta = totalMontoDolarOferta;
	}

	public String getTotalMontoBolivaresOferta() {
		return totalMontoBolivaresOferta;
	}

	public void setTotalMontoBolivaresOferta(String totalMontoBolivaresOferta) {
		this.totalMontoBolivaresOferta = totalMontoBolivaresOferta;
	}

	public String getTipoRegistroCabecera() {
		return tipoRegistroCabecera;
	}

	public void setTipoRegistroCabecera(String tipoRegistroCabecera) {
		this.tipoRegistroCabecera = tipoRegistroCabecera;
	}

	public String getTipoRegistroCuerpo() {
		return tipoRegistroCuerpo;
	}

	public void setTipoRegistroCuerpo(String tipoRegistroCuerpo) {
		this.tipoRegistroCuerpo = tipoRegistroCuerpo;
	}

	public String crearCabecera(String cabeceraOriginal){//TODO No Se utiliza este metodo hasta saber si se recibira una cabecera desde BCV
		StringBuilder sb = new StringBuilder();		
		sb.append(tipoRegistroCabecera);
		//sb.append(Utilitario.rellenarCaracteres(nroJornada,' ',EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor(),false));
		sb.append(cabeceraOriginal);
		sb.append(Utilitario.rellenarCaracteres(idCiclo,' ',EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor(),false));		
		sb.append(Utilitario.rellenarCaracteres(monedaSubasta,' ',EstructuraArchivoOperacionesDICOM.ID_MONEDA_LONG.getValor(),false));
		
		return sb.toString();
	}
	
	public String crearCabecera(String cabeceraOriginal,HashMap<String, String> parametrosRecepcionORO){//TODO No Se utiliza este metodo hasta saber si se recibira una cabecera desde BCV
		StringBuilder sb = new StringBuilder();		
		sb.append(tipoRegistroCabecera);
		//sb.append(Utilitario.rellenarCaracteres(nroJornada,' ',EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor(),false));
		//sb.append(cabeceraOriginal);
		String primeraSeccionLinea=cabeceraOriginal.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.PRIMERA_SECCION_LINEA_CABECERA_LONG.getValor());
		sb.append(primeraSeccionLinea);
		sb.append(Utilitario.rellenarCaracteres(idCiclo,' ',EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor(),false));	
		sb.append(parametrosRecepcionORO.get(getCodBCVdivisaExtranjera()));	
		
		return sb.toString();
	}
/**
 * Método para Cargar del archivo del mainframe la Cabecera
 * para el manejo de Título de Ahorro Oro Soberano
 * 
 * @param linea
 */
	public void cargarCabeceraMainframeOro(String linea) {
		setTipoRegistro(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.TIPO_REGISTRO.getValor()));		//
		//System.out.println("getTipoRegistro() " +getTipoRegistro());
		linea=linea.substring(EstructuraArchivoOperacionesORO.TIPO_REGISTRO.getValor());

		setIdCiclo(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.ID_CICLO_LONG.getValor()).trim());		//
		//System.out.println("getIdSubasta() " +getIdSubasta());
		linea=linea.substring(EstructuraArchivoOperacionesORO.ID_CICLO_LONG.getValor());
		
		setIdSubasta(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.NRO_JORNADA.getValor()).trim());		//
		//System.out.println("getIdSubasta() " +getIdSubasta());
		linea=linea.substring(EstructuraArchivoOperacionesORO.NRO_JORNADA.getValor());

		setFechaJornada(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.DIVISA_EXT_LONG.getValor()));
		//System.out.println("getFechaJornada() " +getFechaJornada());
		linea=linea.substring(EstructuraArchivoOperacionesORO.DIVISA_EXT_LONG.getValor());
		
		
	}
	
	
	/*public void cargarCabeceraMainframe(String linea) {		
		//this.codOperacion=linea.substring(0,4);
		setTipoRegistro(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor()));		//
		//System.out.println("getTipoRegistro() " +getTipoRegistro());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor());

		setIdSubasta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor()).trim());		//
		//System.out.println("getIdSubasta() " +getIdSubasta());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor());

		setFechaJornada(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.FECHA_JORNADA_LONG.getValor()));
		//System.out.println("getFechaJornada() " +getFechaJornada());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.FECHA_JORNADA_LONG.getValor());
		
		setTotalRegistrosDemanda(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_DEMANDA_LONG.getValor()));
		//System.out.println("getTotalRegistrosDemanda() " +getTotalRegistrosDemanda());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_DEMANDA_LONG.getValor());
			
		setTotalMontoDolarDemanda(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_DEMANDA_LONG.getValor()));
		//System.out.println("getTotalMontoDolarDemanda() " +getTotalMontoDolarDemanda());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_DEMANDA_LONG.getValor());
		
		setTotalMontoBolivaresDemanda(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_DEMANDA_LONG.getValor()));
		//System.out.println("getTotalMontoBolivaresDemanda() " +getTotalMontoBolivaresDemanda());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_DEMANDA_LONG.getValor());
		
		setTotalRegistrosOferta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_OFERTA_LONG.getValor()));
		//System.out.println("getTotalRegistrosOferta() " +getTotalRegistrosOferta());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_OFERTA_LONG.getValor());
			
		setTotalMontoDolarOferta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_OFERTA_LONG.getValor()));
		//System.out.println("getTotalMontoDolarOferta() " +getTotalMontoDolarOferta());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_OFERTA_LONG.getValor());
		
		setTotalMontoBolivaresOferta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_OFERTA_LONG.getValor()));		
		//System.out.println("getTotalMontoBolivaresOferta() " +getTotalMontoBolivaresOferta());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_OFERTA_LONG.getValor());
		
		setIdCiclo(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor()).trim());		//
		//System.out.println("getIdCiclo() " +getIdCiclo());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor());	
		
		setDivisaMonExtranjera(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.CODIGO_DIVISA_LONG.getValor()));		
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.CODIGO_DIVISA_LONG.getValor());
		//System.out.println("setTotalMontoBolivaresOferta " + getTotalMontoBolivaresOferta());
	}*/

	public void cargarCuerpoOpVerificadas(String linea) {
		//System.out.println(" ********************************** cargarCuerpoOpVerificadas **********************************");
		setTipoRegistro(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor());
		//System.out.println("getTipoRegistro --> " + getTipoRegistro());
		
		setCodigoRespuesta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.CODIGO_RESPUESTA_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.CODIGO_RESPUESTA_LONG.getValor());
		//System.out.println("getCodigoRespuesta --> " + getCodigoRespuesta());
		
		setIdOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor());
		//System.out.println("getIdOperacion --> " + getIdOperacion());
		
		setTipoCliente(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_CLIENTE_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_CLIENTE_LONG.getValor());
		//System.out.println("getTipoCliente --> " + getTipoCliente());
		
		setCedRifCliente(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.CED_RIF_CLIENTE_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.CED_RIF_CLIENTE_LONG.getValor());
		//System.out.println("getCedRifCliente --> " + getCedRifCliente());
		
		setNombreCliente(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.NOMBRE_CLIENTE_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.NOMBRE_CLIENTE_LONG.getValor());
		//System.out.println("getNombreCliente --> " + getNombreCliente());
		
		setTelefono(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TLF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TLF_LONG.getValor());
		//System.out.println("getTelefono --> " + getTelefono());
		
		setMail(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MAIL_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MAIL_LONG.getValor());
		//System.out.println("getMail --> " + getMail());
		
		setNumCuentaNacional(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.NUM_CTA_VEF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.NUM_CTA_VEF_LONG.getValor());
		//System.out.println("getNumCuentaNacional --> " + getNumCuentaNacional());
		
		setNumRetencionCap(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.RETENCION_CAP_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.RETENCION_CAP_LONG.getValor());
		//System.out.println("getNumRetencionCap --> " + getNumRetencionCap());
		
		setHoraBloqueoCap(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.HORA_BLO_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.HORA_BLO_LONG.getValor());
		//System.out.println("getHoraBloqueoCap --> " + getHoraBloqueoCap());
		
		setMontoOperacionVEF(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG.getValor());
		//System.out.println("getMontoOperacionVEF --> " + getMontoOperacionVEF());
		
		setDivisaMonNacional(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.DIVISA_NAC_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.DIVISA_NAC_LONG.getValor());
		System.out.println("getDivisaMonNacional --> " + getDivisaMonNacional());
		
		setNumRetencionCom(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.RETENCION_COM_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.RETENCION_COM_LONG.getValor());
		System.out.println("getNumRetencionCom --> " + getNumRetencionCom());
		
		setMontoComision(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_COM_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MONTO_OPER_COM_LONG.getValor());
		System.out.println("getMontoComision --> " + getMontoComision());
		
		setPorcentajeComision(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.PORC_COM_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.PORC_COM_LONG.getValor());
		System.out.println("getPorcentajeComision --> " + getPorcentajeComision());
		
		setFechaOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.FECHA_OPER_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.FECHA_OPER_LONG.getValor());
		System.out.println("getFechaOperacion --> " + getFechaOperacion());
		
		setNumCuentaMonedaExtranjera(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.NUM_CTA_USD_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.NUM_CTA_USD_LONG.getValor());
		System.out.println("getNumCuentaMonedaExtranjera --> " + getNumCuentaMonedaExtranjera());
		
		setMontoOperacionUSD(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG.getValor());
		System.out.println("getMontoOperacionUSD --> " + getMontoOperacionUSD());
		
		setDivisaMonExtranjera(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.DIVISA_EXT_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.DIVISA_EXT_LONG.getValor());
		System.out.println("getDivisaMonExtranjera --> " + getDivisaMonExtranjera());
				
		setTasaCambio(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG.getValor());
		System.out.println("getTasaCambio --> " + getTasaCambio());
		
		setFechaValorOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.FECHA_VALOR_OPER_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.FECHA_VALOR_OPER_LONG.getValor());
		System.out.println("getFechaValorOperacion --> " + getFechaValorOperacion());
		
		setTipoOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_OPERACION_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_OPERACION_LONG.getValor());
		System.out.println("getTipoOperacion --> " + getTipoOperacion());

		
		setPtcComisionIGTF(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.PORC_COM_IGTF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.PORC_COM_IGTF_LONG.getValor());
		System.out.println("getPtcComisionIGTF --> " + getPtcComisionIGTF());
		
		setMontoComisionIGTF(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_COM_IGTF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MONTO_COM_IGTF_LONG.getValor());
		System.out.println("getMontoComisionIGTF --> " + getMontoComisionIGTF());
		
		setMontoTotalRetencion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_TOTAL_RET_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MONTO_TOTAL_RET_LONG.getValor());
		System.out.println("getMontoTotalRetencion --> " + getMontoTotalRetencion());
	}
		
	
// NM11383 Alexander Rincon -- 10/09/2018
// Manejo de la Estructura para cargar las operaciones de Certificado en ORO
	public void cargarOperacionesOro(String linea) {
System.out.println("EstNUM_OPER_LONG= "+EstructuraArchivoOperacionesORO.NUM_OPER_LONG.getValor());
		setIdOperacion(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.NUM_OPER_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesORO.NUM_OPER_LONG.getValor());
System.out.println("getIdOperacion= "+getIdOperacion().toString());

		setTipoCliente(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.TIPO_CLIENTE_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesORO.TIPO_CLIENTE_LONG.getValor());
		
		setCedRifCliente(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.CED_RIF_CLIENTE_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesORO.CED_RIF_CLIENTE_LONG.getValor());
		
		setNumCuentaNacional(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.NUM_CTA_VEF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesORO.NUM_CTA_VEF_LONG.getValor());
		
		setMontoOperacionVEF(linea.substring(EstructuraArchivoOperacionesORO.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesORO.MONTO_OPER_VEF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesORO.MONTO_OPER_VEF_LONG.getValor());
		
		setFechaOperacion(new SimpleDateFormat("dd-mm-yyyy").format(new Date()));		//
		
		setFechaValorOperacion(new SimpleDateFormat("dd-mm-yyyy").format(new Date()));		//
	}
///////////////////////////////////////////////////////////////////////////
	public String crearCuerpoOpPreaprobadas(String linea) {
		
		StringBuffer sf = new StringBuffer();						
		sf.append(tipoRegistroCuerpo);		
		sf.append(linea);
		return sf.toString();
	}
	

// NM11383 Alexander Rincon -- 10/09/2018
// Manejo de la Estructura para Validar las operaciones de Certificado en ORO
	public SolicitudORO validarOperacionesOro() throws ParseException {
		SolicitudORO solicitudORO=null;
		BigDecimal monto=null;
		Date fecha=null;
		boolean datosOK=true;
		
		
		try {
			if(((this.idOperacion.trim()).length()!=0)){
				solicitudORO = new SolicitudORO();
				solicitudORO.setIdOperacion(idOperacion);
			}else{
				datosOK=false;
			}
			
			if(((this.tipoCliente.trim()).length()>0)){
				solicitudORO.setTipoCliente(tipoCliente);
			}else{
				datosOK=false;
			}
					
			if(((this.cedRifCliente.trim()).length()>0)&&(Long.valueOf(this.cedRifCliente)!=0)){
				solicitudORO.setCedRifCliente(Long.valueOf(cedRifCliente));
			}else{
				datosOK=false;
			}
			
			if(((this.numCuentaNacional.trim()).length()>0)&&(Long.valueOf(this.numCuentaNacional)>0)){
				solicitudORO.setNumCuentaNacional(numCuentaNacional);
			}else{
				datosOK=false;
			}
			
			// Se actualiza a >= 0 para nueva data DICOM NM11383 23022018			
			if((this.montoOperacionVEF.trim()).length()>0){
				monto=Utilitario.getStringToBigDecimal(this.montoOperacionVEF,EstructuraArchivoOperacionesORO.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesORO.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
				if (monto.floatValue()>=0) {
					solicitudORO.setMontoOperacionVEF(monto);
				}else{
					datosOK=false;
				}
			}else{
				datosOK=false;
			}

			//TTS-546_Agregado en Desarrollo Dicom Interbancario NM26659_27/01/2018
			if(((this.codBCVdivisaExtranjera.trim()).length()>0)){	//Si el codigo BCV de la moneda (Numeros del 1 al 6) no estan vacios, se busca su equivalente en codigo ISO en los parametros del sistema								
				solicitudORO.setCodigoDivisa(parametrosRecepcionORO.get(codBCVdivisaExtranjera));						
			}else{
				datosOK=false;
			}
			
			if(((this.fechaOperacion.trim()).length()>0)){
				fecha=Utilitario.StringToDate(fechaOperacion, ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR);
				if(fecha!=null){
					solicitudORO.setFechaOperacion(fecha);
				}else{
					datosOK=false;
				}				
			}else{
				datosOK=false;
			}
			
			if((this.fechaValorOperacion.trim()).length()>0){
				fecha=Utilitario.StringToDate(fechaValorOperacion, ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR);
				if(fecha!=null){
					solicitudORO.setFechaValorOperacion(fecha);
				}else{
					datosOK=false;
				}				
			}else{
				datosOK=false;
			}
			
			if(datosOK){
				solicitudORO.setEstatusRegistro(EstatusOperacionesORO.ESTATUS_PREAPROBADA.getValor());
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			solicitudORO.setEstatusRegistro(EstatusOperacionesORO.ESTATUS_ERROR_DATOS.getValor());
		}
		
		return solicitudORO;
	}	
	public String crearCuerpoOpVerificadas() {		
		StringBuffer sf = new StringBuffer();
		sf.append(Utilitario.rellenarCaracteres(this.idOperacion,' ',EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor(),false,true));
		sf.append(Utilitario.rellenarCaracteres(this.respuestaArchivo,' ',EstructuraArchivoOperacionesDICOM.CODIGO_RESPUESTA_LONG.getValor(),true,true));
	
		return sf.toString();
	}
////////////////////////////////////////////////////////////////////////////	
	
	
}
