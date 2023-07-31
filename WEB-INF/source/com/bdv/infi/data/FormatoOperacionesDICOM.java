package com.bdv.infi.data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import megasoft.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstatusOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.EnvioOperacionesPreaprobadasDICOM;
import com.bdv.infi.util.Utilitario;

public class FormatoOperacionesDICOM {
	
	private  HashMap<String, String> parametrosRecepcionDICOM;
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
	//String codOperacion;
	String idCiclo; //N�mero de Proceso
	String fechaProceso;
	String monedaSubasta; //N�mero de Proceso		

	String respuestaArchivo;
		
	//CUERPO	
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
	
	public void setParamtrosSistema(HashMap<String, String> parametrosRecepcionDICOM){
		this.parametrosRecepcionDICOM=parametrosRecepcionDICOM;
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
	
	/*public BigDecimal getStringToBigDecimal(String monto) {
		int cantidadEnteros=EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor();
		int cantidadDecimales=EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor();
		BigDecimal result=null;
		if(monto!=null && monto.length()>0){
			try{												
				 String entero=monto.substring(0,cantidadEnteros);
				 String decimal=monto.substring(cantidadEnteros,+cantidadEnteros+cantidadDecimales);
				 String numeroFinal=null;
				 numeroFinal=entero.concat(".").concat(decimal);
				 result= new BigDecimal(numeroFinal);
			}catch(NumberFormatException ex){
				throw ex;
			}
		}
		return result;
	}*/
	
	/*public BigDecimal getStringToBigDecimal(String monto,int cantidadEnteros,int cantidadDecimales) {
		//int cantidadEnteros=EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor();
		//int cantidadDecimales=EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor();
		BigDecimal result=null;
		if(monto!=null && monto.length()>0){
			try{												
				 String entero=monto.substring(0,cantidadEnteros);
				 String decimal=monto.substring(cantidadEnteros,cantidadEnteros+cantidadDecimales);
				 String numeroFinal=null;
				 numeroFinal=entero.concat(".").concat(decimal);
				 result= new BigDecimal(numeroFinal);
				 System.out.println("result -> " + result);
			}catch(NumberFormatException ex){
				throw ex;
			}
		}
		return result;
	}*/
	
	
	
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

	/*public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}*/

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
	
	//
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
	
	/*public void setMontoDivisa(String montoDivisa) {
		try{

		     String entero=montoDivisa.substring(0,11);
			 String decimal=montoDivisa.substring(11,13);
			 String numeroFinal=null;
			 numeroFinal=new BigDecimal(entero).toString().concat(".").concat(new BigDecimal(decimal).toString());
			 this.montoDivisa=new BigDecimal(numeroFinal);			 
		}catch(NumberFormatException ex){
			this.montoDivisa=new BigDecimal(0);	
		}		 
	}*/
	
	/*public void setMontoDivisa(BigDecimal montoDivisa) {

			 this.montoDivisa=montoDivisa;			 
		 
	}*/
	
	/*public String getMontoDivisaFormatted(int parteEntera, int parteDecimal) {
		String entero,decimal;
		if(montoDivisa==null){
			montoDivisa=new BigDecimal(0);
		}		
		entero=Utilitario.rellenarCaracteres(montoDivisa.toBigInteger().toString(),'0',parteEntera,false,true);
		montoDivisa=(montoDivisa.subtract(new BigDecimal(entero)));
		decimal=Utilitario.rellenarCaracteres(montoDivisa.toString().replaceFirst("0", "").replaceFirst(".", ""),'0',parteDecimal,true,true);
		return entero+decimal;
	}	*/
	
	
	
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
	
	public String crearCabecera(String cabeceraOriginal,HashMap<String, String> parametrosRecepcionDICOM){//TODO No Se utiliza este metodo hasta saber si se recibira una cabecera desde BCV
		StringBuilder sb = new StringBuilder();		
		sb.append(tipoRegistroCabecera);
		//sb.append(Utilitario.rellenarCaracteres(nroJornada,' ',EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor(),false));
		//sb.append(cabeceraOriginal);
		String primeraSeccionLinea=cabeceraOriginal.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.PRIMERA_SECCION_LINEA_CABECERA_LONG.getValor());
		sb.append(primeraSeccionLinea);
		sb.append(Utilitario.rellenarCaracteres(idCiclo,' ',EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor(),false));	
		sb.append(parametrosRecepcionDICOM.get(getCodBCVdivisaExtranjera()));
		
		
		return sb.toString();
	}
	
	public String crearCabeceraDicomInterbancario(String nroJornada){
		StringBuilder sb = new StringBuilder();		
		sb.append(tipoRegistroCabecera);
		//sb.append(Utilitario.rellenarCaracteres(nroJornada,' ',EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor(),false));
		//sb.append(cabeceraOriginal);//TODO Se comenta hasta saber si se recibira una cabecera desde BCV
		sb.append(Utilitario.rellenarCaracteres(idCiclo,' ',EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor(),false));
		sb.append(Utilitario.rellenarCaracteres(nroJornada,' ',EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor(),false));
		//sb.append(Utilitario.rellenarCaracteres(monedaSubasta,' ',EstructuraArchivoOperacionesDICOM.ID_MONEDA_LONG.getValor(),false));
		
		return sb.toString();
	}
	
	/*public void cargarCabecera(String linea) {		
		//this.codOperacion=linea.substring(0,4);
		this.fechaInicio=linea.substring(5,15);
		this.fechaFin=linea.substring(16,26);		
		this.idCiclo = linea.substring(27,33);
		this.respuestaArchivo=linea.substring(34,linea.length());
	}*/
	public void cargarCabecera(String linea) {		
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

	//TTS-546_Agregado en Desarrollo Dicom Interbancario 31/01/2018 --- Agregado por Alexander Rincon 
		setCodBCVdivisaExtranjera(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.CODIGO_BCV_DIVISA_LONG.getValor()));		
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.CODIGO_BCV_DIVISA_LONG.getValor());
		//System.out.println("setTotalMontoBolivaresOferta " + getTotalMontoBolivaresOferta());
		
		setIdCiclo(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor()).trim());		//
		//System.out.println("getIdCiclo() " +getIdCiclo());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor());
		

		
		
	}
	
	public void cargarCabeceraMainframe(String linea) {		
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
	}
		//TODO METODO COMENTADO HASTA SABER SI SE RECIBE CABECERA POR PARTE DEL BCV
	/*public void cargarCabecera(String linea) {		
		//this.codOperacion=linea.substring(0,4);
		/*setTipoRegistro(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor());
		setNroJornada(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.NRO_JORNADA.getValor());
		setIdCiclo(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor());

		setTipoRegistro(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor()));		//
		//System.out.println("getTipoRegistro() " +getTipoRegistro());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_REGISTRO.getValor());
		
		setIdSubasta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_SUBASTA_LONG.getValor()));
		//System.out.println("getIdSubasta() " +getIdSubasta());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_SUBASTA_LONG.getValor());
		
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
		 
		setIdCiclo(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor()));		//
		//System.out.println("getIdCiclo() " +getIdCiclo());
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_CICLO_LONG.getValor());
		
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
	
	public String crearCuerpo() {
		
		StringBuffer sf = new StringBuffer();
		
		sf.append(this.tipoRegistroCuerpo);
		
		sf.append(Utilitario.rellenarCaracteres(this.idOperacion,' ',EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.tipoCliente,' ',EstructuraArchivoOperacionesDICOM.TIPO_CLIENTE_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.cedRifCliente,' ',EstructuraArchivoOperacionesDICOM.CED_RIF_CLIENTE_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.nombreCliente,' ',EstructuraArchivoOperacionesDICOM.NOMBRE_CLIENTE_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.telefono,' ',EstructuraArchivoOperacionesDICOM.TLF_LONG.getValor(),true,true)); 		

		sf.append(Utilitario.rellenarCaracteres(this.mail,' ',EstructuraArchivoOperacionesDICOM.MAIL_LONG.getValor(),true,true));
		
		//sf.append(getMontoFormatted(getTasaCambioOperacion(),6,5));///TODO Revisar que hacer con esta seccion 

		sf.append(Utilitario.rellenarCaracteres(this.numCuentaNacional,' ',EstructuraArchivoOperacionesDICOM.NUM_CTA_VEF_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.numRetencionCap,' ',EstructuraArchivoOperacionesDICOM.RETENCION_CAP_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.horaBloqueoCap,' ',EstructuraArchivoOperacionesDICOM.HORA_BLO_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.montoOperacionVEF,' ',EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG.getValor(),true,true));
		
		//sf.append(Utilitario.rellenarCaracteres(this.montoOperacionVEF,' ',EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG.getValor(),true,true));
		
		//CODIGO DIVISA NACIONAL 
		sf.append(Utilitario.rellenarCaracteres(this.numRetencionCom,' ',EstructuraArchivoOperacionesDICOM.RETENCION_COM_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.montoComision,' ',EstructuraArchivoOperacionesDICOM.MONTO_OPER_COM_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.porcentajeComision,' ',EstructuraArchivoOperacionesDICOM.PORC_COM_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.fechaOperacion,' ',EstructuraArchivoOperacionesDICOM.FECHA_OPER_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.numCuentaMonedaExtranjera,' ',EstructuraArchivoOperacionesDICOM.NUM_CTA_USD_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.montoOperacionUSD,' ',EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG.getValor(),true,true));
		
		//sf.append(Utilitario.rellenarCaracteres(this.montoOperacionUSD,' ',EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG.getValor(),true,true));
		
		//CODIGO DIVISA EXTRANJERA		
		sf.append(Utilitario.rellenarCaracteres(this.tasaCambio,' ',EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.fechaValorOperacion,' ',EstructuraArchivoOperacionesDICOM.FECHA_VALOR_OPER_LONG.getValor(),true,true));
		
		//sf.append(Utilitario.rellenarCaracteres(this.indicadorRegistro,' ',EstructuraArchivoOperacionesDICOM.INDICADOR_REGISTRO_LONG.getValor(),true,true));
		
		//sf.append(Utilitario.rellenarCaracteres(this.motivoRechazo,' ',EstructuraArchivoOperacionesDICOM.MOTIVO_RECHAZO_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.tipoOperacion,' ',EstructuraArchivoOperacionesDICOM.TIPO_OPERACION_LONG.getValor(),true,true));
			
		return sf.toString();
	}
	public void cargarCabeceraPreaprobadas(String linea) {	
		System.out.println("paso cabecera");
		setIdSubasta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_SUBASTA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_SUBASTA_LONG.getValor());
		System.out.println("getIdSubasta --->" + getIdSubasta()+" y "+"linea-->"+linea);
		System.out.println("paso cabecera1");
		setFechaJornada(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.FECHA_JORNADA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.FECHA_JORNADA_LONG.getValor());
		System.out.println("setFechaJornada " + getFechaJornada());
		System.out.println("paso cabecera2");
		setTotalRegistrosDemanda(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_DEMANDA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_DEMANDA_LONG.getValor());
		System.out.println("setTotalRegistrosDemanda " + getTotalRegistrosDemanda());	
		System.out.println("paso cabecera3");
		setTotalMontoDolarDemanda(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_DEMANDA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_DEMANDA_LONG.getValor());
		System.out.println("setTotalMontoDolarDemanda " + getTotalMontoDolarDemanda());
		System.out.println("paso cabecera4");
		setTotalMontoBolivaresDemanda(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_DEMANDA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_DEMANDA_LONG.getValor());
		System.out.println("setTotalMontoBolivaresDemanda " + getTotalMontoBolivaresDemanda());
		System.out.println("paso cabecera5");
		setTotalRegistrosOferta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_OFERTA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_REGISTROS_OFERTA_LONG.getValor());
		System.out.println("setTotalRegistrosOferta " + getTotalRegistrosOferta());	
		System.out.println("paso cabecera6");
		setTotalMontoDolarOferta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_OFERTA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_DOLAR_OFERTA_LONG.getValor());
		System.out.println("setTotalMontoDolarOferta " + getTotalMontoDolarOferta());
		System.out.println("paso cabecera7");
		setTotalMontoBolivaresOferta(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_OFERTA_LONG.getValor()));
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TOTAL_MONTO_BOLIVARES_OFERTA_LONG.getValor());
   //TTS-546_Agregado en Desarrollo Dicom Interbancario NM26659_27/01/2018 --- Agregado por Alexander Rincon 
		System.out.println("paso cabecera8");
		setCodBCVdivisaExtranjera(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.CODIGO_BCV_DIVISA_LONG.getValor()));		
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.CODIGO_BCV_DIVISA_LONG.getValor());
		System.out.println("paso cabecera9");
		System.out.println("setTotalMontoBolivaresOferta " + getTotalMontoBolivaresOferta());
	}
	
	
	public void cargarCuerpoOpPreaprobadas(String linea) {
		
		setIdOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor());
		
		setTipoOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_OPERACION_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_OPERACION_LONG.getValor());
		
		setTipoCliente(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.TIPO_CLIENTE_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.TIPO_CLIENTE_LONG.getValor());
		
		setCedRifCliente(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.CED_RIF_CLIENTE_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.CED_RIF_CLIENTE_LONG.getValor());
		
		setNumCuentaNacional(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.NUM_CTA_VEF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.NUM_CTA_VEF_LONG.getValor());
		
		setNumCuentaMonedaExtranjera(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.NUM_CTA_USD_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.NUM_CTA_USD_LONG.getValor());
		
		setMontoOperacionVEF(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG.getValor());
		
		setMontoOperacionUSD(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG.getValor());

		//TTS-546_Agregado en Desarrollo Dicom Interbancario NM26659_27/01/2018
		//setCodBCVdivisaExtranjera(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.CODIGO_BCV_DIVISA_LONG.getValor()));		//
		//linea=linea.substring(EstructuraArchivoOperacionesDICOM.CODIGO_BCV_DIVISA_LONG.getValor());

		setFechaOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.FECHA_OPER_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.FECHA_OPER_LONG.getValor());

		setFechaValorOperacion(linea.substring(EstructuraArchivoOperacionesDICOM.LONGITUD_CERO.getValor(),EstructuraArchivoOperacionesDICOM.FECHA_VALOR_OPER_LONG.getValor()));		//
		linea=linea.substring(EstructuraArchivoOperacionesDICOM.FECHA_VALOR_OPER_LONG.getValor());										

	}

	
	public String crearCuerpoOpPreaprobadas(String linea) {
				
		StringBuffer sf = new StringBuffer();						

		sf.append(tipoRegistroCuerpo);		
		sf.append(linea);
		
		/*sf.append(Utilitario.rellenarCaracteres(this.idOperacion,' ',EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.tipoOperacion,' ',EstructuraArchivoOperacionesDICOM.TIPO_OPERACION_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.tipoCliente,' ',EstructuraArchivoOperacionesDICOM.TIPO_CLIENTE_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.cedRifCliente,' ',EstructuraArchivoOperacionesDICOM.CED_RIF_CLIENTE_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.nombreCliente,' ',EstructuraArchivoOperacionesDICOM.NOMBRE_CLIENTE_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.telefono,' ',EstructuraArchivoOperacionesDICOM.TLF_LONG.getValor(),true,true)); 		

		sf.append(Utilitario.rellenarCaracteres(this.mail,' ',EstructuraArchivoOperacionesDICOM.MAIL_LONG.getValor(),true,true));

		sf.append(Utilitario.rellenarCaracteres(this.numCuentaNacional,' ',EstructuraArchivoOperacionesDICOM.NUM_CTA_VEF_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.numRetencionCap,' ',EstructuraArchivoOperacionesDICOM.RETENCION_CAP_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.horaBloqueoCap,' ',EstructuraArchivoOperacionesDICOM.HORA_BLO_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.montoOperacionVEF,'0',EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.divisaMonNacional,' ',EstructuraArchivoOperacionesDICOM.DIVISA_NAC_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.numRetencionCom,' ',EstructuraArchivoOperacionesDICOM.RETENCION_COM_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.montoComision,'0',EstructuraArchivoOperacionesDICOM.MONTO_OPER_COM_LONG.getValor(),false,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.porcentajeComision,'0',EstructuraArchivoOperacionesDICOM.PORC_COM_LONG.getValor(),false,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.fechaOperacion,' ',EstructuraArchivoOperacionesDICOM.FECHA_OPER_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.numCuentaMonedaExtranjera,' ',EstructuraArchivoOperacionesDICOM.NUM_CTA_USD_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.montoOperacionUSD,'0',EstructuraArchivoOperacionesDICOM.MONTO_OPER_USD_LONG.getValor(),false,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.divisaMonExtranjera,' ',EstructuraArchivoOperacionesDICOM.DIVISA_EXT_LONG.getValor(),true,true));
	
		sf.append(Utilitario.rellenarCaracteres(this.tasaCambio,'0',EstructuraArchivoOperacionesDICOM.TASA_CAMBIO_LONG.getValor(),false,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.fechaValorOperacion,' ',EstructuraArchivoOperacionesDICOM.FECHA_VALOR_OPER_LONG.getValor(),true,true));
		
		sf.append(Utilitario.rellenarCaracteres(this.tipoOperacion,' ',EstructuraArchivoOperacionesDICOM.TIPO_OPERACION_LONG.getValor(),true,true));
		*/
			
		return sf.toString();
	}
	
	public SolicitudDICOM validarCuerpoOpPreaprobadas() throws ParseException {
		SolicitudDICOM solicitudDICOM=null;
		BigDecimal monto=null;
		Date fecha=null;
		boolean datosOK=true;
		System.out.println("paso validarCuerpoOpPreaprobadas");
		
		try {
			if(((this.idOperacion.trim()).length()!=0)){
				solicitudDICOM=new SolicitudDICOM();
				solicitudDICOM.setIdOperacion(idOperacion);			
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas2");
			if(((this.tipoOperacion.trim()).length()!=0)&&(this.tipoOperacion.equals("V")||this.tipoOperacion.equals("C"))){
				solicitudDICOM.setTipoOperacion(tipoOperacion);
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas3");
			if(((this.tipoCliente.trim()).length()>0)){
				solicitudDICOM.setTipoCliente(tipoCliente);
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas4");
			System.out.println("this.cedRifCliente.trim()--->"+this.cedRifCliente.trim());
			if(((this.cedRifCliente.trim()).length()>0)&&(Long.valueOf(this.cedRifCliente)!=0)){
				solicitudDICOM.setCedRifCliente(Long.valueOf(cedRifCliente));
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas5");
			if(((this.numCuentaNacional.trim()).length()>0)&&(Long.valueOf(this.numCuentaNacional)>0)){
				solicitudDICOM.setNumCuentaNacional(numCuentaNacional);
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas6");
			if(((this.numCuentaMonedaExtranjera.trim()).length()>0)&&(Long.valueOf(this.numCuentaMonedaExtranjera)>0)){
				solicitudDICOM.setNumCuentaMonedaExtranjera(numCuentaMonedaExtranjera);
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas7");
			// Se actualiza a >= 0 para nueva data DICOM NM11383 23022018			
			if((this.montoOperacionVEF.trim()).length()>0){
				monto=Utilitario.getStringToBigDecimal(this.montoOperacionVEF,EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
				if (monto.floatValue()>=0) {
					solicitudDICOM.setMontoOperacionVEF(monto);
				}else{
					datosOK=false;
				}
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas8");
			// Se actualiza a >= 0 para nueva data DICOM NM11383 23022018
			if(((this.montoOperacionUSD.trim()).length()>0)){
				monto=Utilitario.getStringToBigDecimal(this.montoOperacionUSD,EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
				if (monto.floatValue()>=0) {
					solicitudDICOM.setMontoOperacionUSD(monto);
				}else{
					datosOK=false;
				}			
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas9");

			//TTS-546_Agregado en Desarrollo Dicom Interbancario NM26659_27/01/2018
			if(((this.codBCVdivisaExtranjera.trim()).length()>0)){	//Si el codigo BCV de la moneda (Numeros del 1 al 6) no estan vacios, se busca su equivalente en codigo ISO en los parametros del sistema								
				solicitudDICOM.setCodigoDivisa(parametrosRecepcionDICOM.get(codBCVdivisaExtranjera));						
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas10");
			if(((this.fechaOperacion.trim()).length()>0)){
				fecha=Utilitario.StringToDate(fechaOperacion, ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR);
				if(fecha!=null){
					solicitudDICOM.setFechaOperacion(fecha);
				}else{
					datosOK=false;
				}				
			}else{
				datosOK=false;
			}
			System.out.println("paso validarCuerpoOpPreaprobadas11");
			if((this.fechaValorOperacion.trim()).length()>0){
				fecha=Utilitario.StringToDate(fechaValorOperacion, ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR);
				if(fecha!=null){
					solicitudDICOM.setFechaValorOperacion(fecha);
				}else{
					datosOK=false;
				}				
			}else{
				datosOK=false;
			}
			if(datosOK){
				solicitudDICOM.setEstatusRegistro(EstatusOperacionesDICOM.ESTATUS_PREAPROBADA.getValor());
			}
			System.out.println("paso validarCuerpoOpPreaprobadas12");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			solicitudDICOM.setEstatusRegistro(EstatusOperacionesDICOM.ESTATUS_ERROR_DATOS.getValor());
		}
		System.out.println("paso validarCuerpoOpPreaprobadas13");
		return solicitudDICOM;
	}
	
	public SolicitudDICOM validarCabeceraOpPreaprobadas(String jornada) throws Exception {
		SolicitudDICOM solicitudDICOM=null;
		BigDecimal monto=null;
		Date fecha=null;
		boolean datosOK=true;
		String mensajeError="";
		
		try {
			if(((this.idSubasta.trim()).length()!=0)&&(Long.valueOf(this.idSubasta)!=0)){
				solicitudDICOM=new SolicitudDICOM();
				solicitudDICOM.setIdSubasta(idSubasta);	
				
				if(!idSubasta.equals(jornada)){	
					datosOK=false;					
					mensajeError=": El numero de jornada asociado al archivo recibido no corresponde con la Jornada configurada en el sistema "+jornada;
					Logger.error(this, mensajeError);
				}
			}else{
				datosOK=false;
			}
			
			if(((this.fechaJornada.trim()).length()>0)){
				fecha=Utilitario.StringToDate(fechaJornada, ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR);
				if(fecha!=null){
					solicitudDICOM.setFechaOperacion(fecha);
				}else{
					datosOK=false;
				}				
			}else{
				datosOK=false;
			}		
		
			if((this.totalRegistrosDemanda.trim()).length()>0){
				monto=new BigDecimal(this.totalRegistrosDemanda);
				if (monto.intValue()>0) {
					solicitudDICOM.setTotalRegistrosDemanda(monto);
				}else{
					datosOK=false;
				}
			}else{
				datosOK=false;
			}
			// Se Actualizo >= la validaci�n del monto NM11383			
			if(((this.totalMontoDolarDemanda.trim()).length()>0)){
				monto=Utilitario.getStringToBigDecimal(this.totalMontoDolarDemanda,EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
				if (monto.floatValue()>=0) {
					solicitudDICOM.setTotalMontoDolarDemanda(monto);
				}else{
					datosOK=false;
				}			
			}else{
				datosOK=false;
			}
			// Se Actualizo >= la validaci�n del monto NM11383			
			if(((this.totalMontoBolivaresDemanda.trim()).length()>0)){
				monto=Utilitario.getStringToBigDecimal(this.totalMontoBolivaresDemanda,EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
				if (monto.floatValue()>=0) {
					solicitudDICOM.setTotalMontoBolivaresDemanda(monto);
				}else{
					datosOK=false;
				}			
			}else{
				datosOK=false;
			}
			
			if((this.totalRegistrosOferta.trim()).length()>0){
				monto=new BigDecimal(this.totalRegistrosOferta);
				if (monto.intValue()>0) {
					solicitudDICOM.setTotalRegistrosOferta(monto);
				}else{
					datosOK=false;
				}
			}else{
				datosOK=false;
			}
			// Se Actualizo >= la validaci�n del monto NM11383			
			if(((this.totalMontoDolarOferta.trim()).length()>0)){
				monto=Utilitario.getStringToBigDecimal(this.totalMontoDolarOferta,EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
				if (monto.floatValue()>=0) {
					solicitudDICOM.setTotalMontoDolarOferta(monto);
				}else{
					datosOK=false;
				}			
			}else{
				datosOK=false;
			}
			// Se Actualizo >= la validaci�n del monto NM11383 
			if(((this.totalMontoBolivaresOferta.trim()).length()>0)){
				monto=Utilitario.getStringToBigDecimal(this.totalMontoBolivaresOferta,EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_ENTEROS.getValor(),EstructuraArchivoOperacionesDICOM.MONTO_OPER_VEF_LONG_DECIMALES.getValor());
				if (monto.floatValue()>=0) {
					solicitudDICOM.setTotalMontoBolivaresOferta(monto);
				}else{
					datosOK=false;
				}			
			}else{
				datosOK=false;
			}
						
			if(!datosOK){
				throw new Exception(EnvioOperacionesPreaprobadasDICOM.ERROR_CABECERA_EXCEPTION+mensajeError);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		return solicitudDICOM;
	}
	
	public String crearCuerpoOpVerificadas() {		
		StringBuffer sf = new StringBuffer();
		sf.append(Utilitario.rellenarCaracteres(this.idOperacion,' ',EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor(),false,true));
		sf.append(Utilitario.rellenarCaracteres(this.respuestaArchivo,' ',EstructuraArchivoOperacionesDICOM.CODIGO_RESPUESTA_LONG.getValor(),true,true));
	
		return sf.toString();
	}
	
	public String crearCuerpoOpLiquidadas() {		
		StringBuffer sf = new StringBuffer();
		sf.append(Utilitario.rellenarCaracteres(this.idOperacion,' ',EstructuraArchivoOperacionesDICOM.ID_OPER_LONG.getValor(),false,true));
		sf.append(Utilitario.rellenarCaracteres(this.nroOperacionDebito,'0',EstructuraArchivoOperacionesDICOM.NRO_OP_DEBITO_LONG.getValor(),false,true));
		sf.append(Utilitario.rellenarCaracteres(this.nroOperacionCredito,'0',EstructuraArchivoOperacionesDICOM.NRO_OP_CREDITO_LONG.getValor(),false,true));
	
		return sf.toString();
	}
		
	public static void main(String [] a){
		
		String linea="000000001970VJ3071074560102050180000000797701020501800000023158014000000000000000000120000012801201802022018";
		String lineaEditada1=linea.substring(0,91);
		String lineaEditada2=linea.substring(92);
		System.out.println("Linea 1---> " + lineaEditada1);
		System.out.println("Linea 2---> " + lineaEditada2);
	}
}
