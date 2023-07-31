package com.bdv.infi.data;

import com.bdv.infi.logic.interfaces.EstructuraArchivosOps;
import com.bdv.infi.util.Utilitario;


public class FormatoArchivosOps {
	private String linea;
	private String tipoRegistro;
	
	//ENCABEZADO
	private static final String TIPO_REGISTRO_CABECERA="01";
	private String fechaProceso="";
	private String horaProceso="";
	private String totalRegistros="";
	private String numeroProceso="";
	private String codigoLote="";
	
	public String getTipoRegistroCabecera() {
		return TIPO_REGISTRO_CABECERA;
	}
	
	public String getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getHoraProceso() {
		return horaProceso;
	}
	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}
	public String getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(String totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public String getNumeroProceso() {
		return numeroProceso;
	}
	public int getNumeroProcesoInt() {
		return Integer.valueOf(numeroProceso);
	}
	public void setNumeroProceso(String numeroProceso) {
		this.numeroProceso = numeroProceso;
	}
	public String getCodigoLote() {
		return codigoLote;
	}
	public void setCodigoLote(String codigoLote) {
		this.codigoLote = codigoLote;
	}	
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}


	//CUERPO	
	private static final String TIPO_REGISTRO_CUERPO="02"; 	
	private String consecutivo="";	
	private String tipoOperacion="";//Indica si la operaciones  Compra(C) o Venta (V) (Campo de uso exclusivo de Mainframe)	
	private String codOperacion="";	
	private String numeroCuenta="";	
	private String referencia="";
		//Sub campos que componen el campo  referencia
		private String digitoIndicadorRef;//Digito 9 definido 		
		private String codigoOperacion;
		private String ordeneIdReferencia; //Id de orden de TOMA_ORDEN
		private String digitosIndicadorFinalesRef;//Digitos 00 definidos
		
	private String claveLiga="";
	private String montoOperacion="";
	private String indicadorRetencion="";	
	private String numeroRetencion="";
	private String montoRetencion="";
	private String codigoRetorno="";
	private String codigoRespuesta="";
	private String camposAdicionales="";		
		private String idOperacion="";
		private String idOrdenProceso="";	
		private String reservado="";
	private String divisa="";
	private String descripcionRespuesta="";
	private String codigoRespuestaRetencion="";
	private String descripcionRespuestaRetencion="";
	private String fechaOperacion="";
	private String horaOperacion="";
	
	public String getTipoRegistroCuerpo() {
		return TIPO_REGISTRO_CUERPO;
	}
	public String getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public String getCodOperacion() {
		return codOperacion;
	}
	public void setCodOperacion(String codOperacion) {
		this.codOperacion = codOperacion;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getReferencia() {
		return referencia;
	}
	
	public String getDigitoIndicadorRef() {
		return digitoIndicadorRef;
	}
	public void setDigitoIndicadorRef(String digitoIndicadorRef) {
		this.digitoIndicadorRef = digitoIndicadorRef;
	}
	public String getCodigoOperacion() {
		return codigoOperacion;
	}
	public void setCodigoOperacion(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}
	public String getOrdeneId() {
		return ordeneIdReferencia;
	}
	public long getOrdeneIdProcesoLong() {
		return Long.valueOf(idOrdenProceso);
	}
	
	public void setOrdeneId(String ordeneId) {
		this.ordeneIdReferencia = ordeneId;
	}
	public String getDigitosIndicadorFinalesRef() {
		return digitosIndicadorFinalesRef;
	}
	public void setDigitosIndicadorFinalesRef(String digitosIndicadorFinalesRef) {
		this.digitosIndicadorFinalesRef = digitosIndicadorFinalesRef;
	}
	
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getClaveLiga() {
		return claveLiga;
	}
	public void setClaveLiga(String claveLiga) {
		this.claveLiga = claveLiga;
	}
	public String getMontoOperacion() {
		return montoOperacion;
	}
	public void setMontoOperacion(String montoOperacion) {
		this.montoOperacion = montoOperacion;
	}
	public String getIndicadorRetencion() {
		return indicadorRetencion;
	}
	public void setIndicadorRetencion(String indicadorRetencion) {
		this.indicadorRetencion = indicadorRetencion;
	}
	public String getNumeroRetencion() {
		return numeroRetencion;
	}
	public void setNumeroRetencion(String numeroRetencion) {
		this.numeroRetencion = numeroRetencion;
	}
	public String getMontoRetencion() {
		return montoRetencion;
	}
	public void setMontoRetencion(String montoRetencion) {
		this.montoRetencion = montoRetencion;
	}
	
	public String getCodigoRetorno() {
		return codigoRetorno;
	}
	 
	public void setCodigoRetorno(String codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}
	
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}
	public String getCamposAdicionales() {
		return camposAdicionales;
	}
	public void setCamposAdicionales(String camposAdicionales) {
		this.camposAdicionales = camposAdicionales;
	}
	
	public void armarCamposAdicionales() {
		this.camposAdicionales = getIdOperacion()+getIdOrdenProceso()+getReservado();
	}
	
	public String getIdOperacion() {
		return idOperacion;
	}
	
	public long getIdOperacionLong() {
		return Long.valueOf(idOperacion);
	}
	
	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}
	
	
	public String getIdOrdenProceso() {
		return idOrdenProceso;
	}
	public void setIdOrdenProceso(String idOrdenProceso) {
		this.idOrdenProceso = idOrdenProceso;
	}
	
	public String getOrdeneIdReferencia() {
		return ordeneIdReferencia;
	}

	public void setOrdeneIdReferencia(String ordeneIdReferencia) {
		this.ordeneIdReferencia = ordeneIdReferencia;
	}

	public String getReservado() {
		return reservado;
	}
	public void setReservado(String reservado) {
		this.reservado = reservado;
	}
	
	public String getDivisa() {
		return divisa;
	}
	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}
	public String getDescripcionRespuesta() {
		return descripcionRespuesta;
	}
	public void setDescripcionRespuesta(String descripcionRespuesta) {
		this.descripcionRespuesta = descripcionRespuesta;
	}
	public String getCodigoRespuestaRetencion() {
		return codigoRespuestaRetencion;
	}
	public void setCodigoRespuestaRetencion(String codigoRespuestaRetencion) {
		this.codigoRespuestaRetencion = codigoRespuestaRetencion;
	}
	public String getDescripcionRespuestaRetencion() {
		return descripcionRespuestaRetencion;
	}
	public void setDescripcionRespuestaRetencion(String descripcionRespuestaRetencion) {
		this.descripcionRespuestaRetencion = descripcionRespuestaRetencion;
	}
	public String getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public String getHoraOperacion() {
		return horaOperacion;
	}
	public void setHoraOperacion(String horaOperacion) {
		this.horaOperacion = horaOperacion;
	}
	
	
	//PIE DE PAGINA
	private static final String TIPO_REGISTRO_PIE_PAGINA="03";
	
	private String totalDebito="";
	private String totalCredito="";
	
	public String getTotalDebito() {
		return totalDebito;
	}
	public void setTotalDebito(String totalDebito) {
		this.totalDebito = totalDebito;
	}
	public String getTotalCredito() {
		return totalCredito;
	}
	public void setTotalCredito(String totalCredito) {
		this.totalCredito = totalCredito;
	}
	
	public String getTipoRegistroPiePagina() {
		return TIPO_REGISTRO_PIE_PAGINA;
	}
	
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public String formatearNumero(String numero) {
		String s = Utilitario.formatearNumero(numero, "0000000000000.00");
		s = s.replace(",", "");
		s = s.replace(".", "");
		return s;
	}
	
	public String generarRegistroDeCabecera() {
		StringBuilder sb = new StringBuilder();
		//sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(TIPO_REGISTRO), '0', 2, false));
		sb.append(Utilitario.rellenarCaracteres(getTipoRegistroCabecera(), '0', EstructuraArchivosOps.TIPO_REGISTRO_LONG.getValor(), false));
		//sb.append(camposCabecera.get(FECHA_PROCESO));
		sb.append(getFechaProceso());
		//sb.append(camposCabecera.get(HORA_PROCESO));
		sb.append(getHoraProceso());
		//sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(TOTAL_REGISTROS), '0', 7, false));
		sb.append(Utilitario.rellenarCaracteres(getTotalRegistros(), '0', EstructuraArchivosOps.TOTAL_REGISTROS_CABECERA_LONG.getValor(), false));
		//sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(NUMERO_PROCESO), '0', 6, false));
		sb.append(Utilitario.rellenarCaracteres(getNumeroProceso(), '0', EstructuraArchivosOps.NRO_PROCESO_CABECERA_LONG.getValor(), false));
		//sb.append(Utilitario.rellenarCaracteres(camposCabecera.get(CODIGO_VALIDADOR_LOTE), '0', 2, false));
		sb.append(Utilitario.rellenarCaracteres(getCodigoLote(), '0', EstructuraArchivosOps.CODIGO_LOTE_CABECERA_LONG.getValor(), false));
		return sb.toString();
	}
	
	/** Generar el totales */
	public String generarRegistroDeTotales() {
		StringBuilder sb = new StringBuilder();
		//sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TIPO_REGISTRO), '0', 2, false));
		sb.append(Utilitario.rellenarCaracteres(getTipoRegistroPiePagina(), '0', EstructuraArchivosOps.TIPO_REGISTRO_LONG.getValor(), false));
		//sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_DEBITOS), '0', 15, false));
		//System.out.println("getTotalDebito() --> " + getTotalDebito());
		sb.append(Utilitario.rellenarCaracteres(getTotalDebito(), '0', EstructuraArchivosOps.TOTAL_DEBITOS_PIE_LONG.getValor(), false));
		//sb.append(Utilitario.rellenarCaracteres(camposTotales.get(TOTAL_CREDITOS), '0', 15, false));
		//System.out.println("getTotalDebito() --> " +getTotalCredito());
		sb.append(Utilitario.rellenarCaracteres(getTotalCredito(), '0', EstructuraArchivosOps.TOTAL_CREDITOS_PIE_LONG.getValor(), false));
		
		return sb.toString();
	}
	
	public String generarRegistroDeDetalle() {
		StringBuilder sb = new StringBuilder();
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(TIPO_REGISTRO), '0', LONGITUD_TIPO_REGISTRO, false));
		//System.out.println("getTipoRegistroCuerpo " + this.getTipoRegistroCuerpo());
		sb.append(Utilitario.rellenarCaracteres(this.getTipoRegistroCuerpo(), '0', EstructuraArchivosOps.TIPO_REGISTRO_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CONSECUTIVO), '0', LONGITUD_CONSECUTIVO, false));
		//System.out.println("getConsecutivo " + this.getConsecutivo());
		sb.append(Utilitario.rellenarCaracteres(this.getConsecutivo(), '0', EstructuraArchivosOps.CONSECUTIVO_LONG.getValor(), false));

		//sb.append(camposDetalle.get(TIPO_OPERACION));
		//System.out.println("getTipoOperacion " + this.getTipoOperacion());
		sb.append(this.getTipoOperacion());

		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CODIGO_OPERACION), '0', LONGITUD_CODIGO_OPERACION, false));
		//System.out.println("getCodOperacion " + this.getCodOperacion());
		sb.append(Utilitario.rellenarCaracteres(this.getCodOperacion(), '0', EstructuraArchivosOps.COD_OPERACION_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CTA_CLIENTE), '0', LONGITUD_CTA_CLIENTE, false));
		//System.out.println("getNumeroCuenta " + this.getNumeroCuenta());
		sb.append(Utilitario.rellenarCaracteres(this.getNumeroCuenta(), '0', EstructuraArchivosOps.NRO_CTA_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(REFERENCIA), ' ', LONGITUD_REFERENCIA, true));
		//System.out.println("getReferencia " + this.getReferencia());
		sb.append(Utilitario.rellenarCaracteres(this.getReferencia(), ' ', EstructuraArchivosOps.NRO_REF_LONG.getValor(), false));		
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(CLAVE_LIG), ' ', LONGITUD_CLAVE_LIG, true));
		//System.out.println("getClaveLiga " + this.getClaveLiga());
		sb.append(Utilitario.rellenarCaracteres(this.getClaveLiga(), ' ', EstructuraArchivosOps.CLAVE_LIG_LONG.getValor(), false));
		
			// v13 13 enteros, 2 decimales
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MONTO_OPERACION), '0', LONGITUD_MONTO_OPERACION, false));
		//System.out.println("getMontoOperacion " + this.getMontoOperacion());
		sb.append(Utilitario.rellenarCaracteres(this.getMontoOperacion(), '0', EstructuraArchivosOps.MTO_OPER_LONG.getValor(), false));
			
		//sb.append(camposDetalle.get(INDICA_RETENCION));
		//System.out.println("getIndicadorRetencion " + this.getIndicadorRetencion());
		sb.append(Utilitario.rellenarCaracteres(this.getIndicadorRetencion(), '0', EstructuraArchivosOps.IND_RET_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(NRO_RETENCION), '0', LONGITUD_NRO_RETENCION, false));
		//System.out.println("getNumeroRetencion " + this.getNumeroRetencion());
		sb.append(Utilitario.rellenarCaracteres(this.getNumeroRetencion(), '0', EstructuraArchivosOps.NRO_RET_LONG.getValor(), false));

			
		// v13 11 enteros, 2 decimales
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(MTO_RETENCION), '0', LONGITUD_MTO_RETENCION, false));
		//System.out.println("getMontoRetencion " + this.getMontoRetencion());
		sb.append(Utilitario.rellenarCaracteres(this.getMontoRetencion(), '0', EstructuraArchivosOps.MTO_RET_LONG.getValor(), false));

		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RETORNO), ' ', LONGITUD_CODIGO_RETORNO, false));
		//System.out.println("getCodigoRetorno " + this.getCodigoRetorno());
		sb.append(Utilitario.rellenarCaracteres(this.getCodigoRetorno(), ' ', EstructuraArchivosOps.COD_RETORNO_LONG.getValor(), false));
			
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RESPUESTA), ' ', 7, false));
		//System.out.println("getCodigoRespuesta " + this.getCodigoRespuesta());
		sb.append(Utilitario.rellenarCaracteres(this.getCodigoRespuesta(), ' ', EstructuraArchivosOps.COD_RESP_LONG.getValor(), false));
		
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(NUMERO_OPERACION_ID), '0', LONGITUD_NUMERO_OPERACION_ID, false));
		//System.out.println("getIdOperacion " + this.getIdOperacion());
		sb.append(Utilitario.rellenarCaracteres(this.getIdOperacion(), '0', EstructuraArchivosOps.ID_OPERACION_LONG.getValor(), false));
		//System.out.println("getIdOrdenProceso " + this.getIdOrdenProceso());
		sb.append(Utilitario.rellenarCaracteres(this.getIdOrdenProceso(), '0', EstructuraArchivosOps.ID_ORDEN_PROCESO_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(RESERVADO), ' ', LONGITUD_RESERVADO, false));
		//System.out.println("getReservado " + this.getReservado());
		sb.append(Utilitario.rellenarCaracteres(this.getReservado(), ' ', EstructuraArchivosOps.RESERVADO_LONG.getValor(), false));
		
		//Configuracion de DIVISA
		//System.out.println("getDivisa " + this.getDivisa());
		sb.append(Utilitario.rellenarCaracteres(this.getDivisa(), ' ', EstructuraArchivosOps.DIVISA_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(DESC_RESPUESTA), ' ', LONGITUD_DESCRIPCION_RESPUESTA, false));
		//System.out.println("getDescripcionRespuesta " + this.getDescripcionRespuesta());
		sb.append(Utilitario.rellenarCaracteres(this.getDescripcionRespuesta(), ' ', EstructuraArchivosOps.DES_RESP_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(COD_RETORNO_RETENCION), ' ', LONGITUD_CODIGO_RETENCION, false));
		//System.out.println("getCodigoRespuestaRetencion " + this.getCodigoRespuestaRetencion());
		sb.append(Utilitario.rellenarCaracteres(this.getCodigoRespuestaRetencion(), ' ', EstructuraArchivosOps.COD_RESP_RET_LONG.getValor(), false));
		
		//sb.append(Utilitario.rellenarCaracteres(camposDetalle.get(DESC_RESPUESTA_RETENCION), ' ', LONGITUD_DESCRIPCION_RESPUESTA_RETENCION, false));
		//System.out.println("getDescripcionRespuestaRetencion " + this.getDescripcionRespuestaRetencion());
		sb.append(Utilitario.rellenarCaracteres(this.getDescripcionRespuestaRetencion(), ' ', EstructuraArchivosOps.DES_RESP_RET_LONG.getValor(), false));
		
		//sb.append(camposDetalle.get(FECHA_APLIC_OPERACION));
		//System.out.println("getFechaOperacion " + this.getFechaOperacion());
		sb.append(this.getFechaOperacion());
		
		//sb.append(camposDetalle.get(HORA_APLIC_OPERACION));
		//System.out.println("getHoraOperacion " + this.getHoraOperacion());
		sb.append(this.getHoraOperacion());
			//System.out.println("generarRegistroDeDetalle ---> " + sb.toString());
		return sb.toString();
	}
	
	public String leerCampoLinea(int longitudCampo){
		String campo="";
		if(linea!=null&&linea.length()>=longitudCampo){
			campo=linea.substring(EstructuraArchivosOps.LONGITUD_CERO.getValor(),longitudCampo);
			linea=linea.substring(longitudCampo);
		}
		return campo;
	}
	
	public boolean leerCabecera(){
		boolean validarTipoRegistro=false;
		setTipoRegistro(leerCampoLinea(EstructuraArchivosOps.TIPO_REGISTRO_LONG.getValor()));
		if(getTipoRegistro().equalsIgnoreCase(getTipoRegistroCabecera())){
			validarTipoRegistro=true;
			setFechaProceso(leerCampoLinea(EstructuraArchivosOps.FECHA_OPER_CABECERA_LONG.getValor()));
			setHoraProceso(leerCampoLinea(EstructuraArchivosOps.HORA_OPER_CABECERA_LONG.getValor()));
			setTotalRegistros(leerCampoLinea(EstructuraArchivosOps.TOTAL_REGISTROS_CABECERA_LONG.getValor()));
			setNumeroProceso(leerCampoLinea(EstructuraArchivosOps.NRO_PROCESO_CABECERA_LONG.getValor()));
			setCodigoLote(leerCampoLinea(EstructuraArchivosOps.CODIGO_LOTE_CABECERA_LONG.getValor()));
		}
		return validarTipoRegistro;
	}
	
	public boolean leerPieTotales(){
		boolean validarTipoRegistro=false;
		setTipoRegistro(leerCampoLinea(EstructuraArchivosOps.TIPO_REGISTRO_LONG.getValor()));
		if(getTipoRegistro().equalsIgnoreCase(getTipoRegistroPiePagina())){
			validarTipoRegistro=true;
			setTotalDebito(leerCampoLinea(EstructuraArchivosOps.TOTAL_DEBITOS_PIE_LONG.getValor()));
			setTotalCredito((leerCampoLinea(EstructuraArchivosOps.TOTAL_CREDITOS_PIE_LONG.getValor())));
		}
		return validarTipoRegistro;
	}

		
	public boolean leerCuerpo(){		
		boolean validarTipoRegistro=false;
		setTipoRegistro(leerCampoLinea(EstructuraArchivosOps.TIPO_REGISTRO_LONG.getValor()));
		
		if(getTipoRegistro().equalsIgnoreCase(getTipoRegistroCuerpo())){
			validarTipoRegistro=true;
			setConsecutivo(leerCampoLinea(EstructuraArchivosOps.CONSECUTIVO_LONG.getValor()));
			//System.out.println("Consecutivo --> " + getConsecutivo());
			setTipoOperacion(leerCampoLinea(EstructuraArchivosOps.TIPO_OPERACION_LONG.getValor()));
			//System.out.println("TipoOperacion --> " + getTipoOperacion());
			setCodOperacion(leerCampoLinea(EstructuraArchivosOps.CODIGO_OPERACION.getValor()));
			//System.out.println("CodOperacion --> " + getCodOperacion());
			setNumeroCuenta(leerCampoLinea(EstructuraArchivosOps.NRO_CTA_LONG.getValor()));
			//System.out.println("NumeroCuenta --> " + getNumeroCuenta());
			
			//Composicion de campo Referencia (WREG-REFER)       
			setDigitoIndicadorRef(leerCampoLinea(EstructuraArchivosOps.DIGITO_INDENT_REF.getValor()));
			//System.out.println("DigitoIndicadorRef --> " + getDigitoIndicadorRef());
			setCodigoOperacion(leerCampoLinea(EstructuraArchivosOps.CODIGO_OPERACION.getValor()));
			//System.out.println("CodigoOperacion --> " + getCodigoOperacion());
			setOrdeneIdReferencia(leerCampoLinea(EstructuraArchivosOps.ID_ORDEN_PROCESO_LONG.getValor()));
			//System.out.println("OrdeneIdReferencia --> "+getOrdeneIdReferencia());
			setDigitosIndicadorFinalesRef(leerCampoLinea(EstructuraArchivosOps.DIGITO_INDENT_FINALES_REF.getValor()));
			//System.out.println("DigitosIndicadorFinalesRef -->" + getDigitosIndicadorFinalesRef());
			
			setReferencia(getDigitoIndicadorRef()+getCodigoOperacion()+getOrdeneIdReferencia()+getDigitosIndicadorFinalesRef());			
			//System.out.println("Referencia --> " + getReferencia());
			
			setClaveLiga(leerCampoLinea(EstructuraArchivosOps.CLAVE_LIG_LONG.getValor()));
			//System.out.println("ClaveLiga --> " + getClaveLiga());			
			setMontoOperacion(leerCampoLinea(EstructuraArchivosOps.MTO_OPER_LONG.getValor()));
			//System.out.println("MontoOperacion --> " + getMontoOperacion());
			setIndicadorRetencion(leerCampoLinea(EstructuraArchivosOps.IND_RET_LONG.getValor()));
			//System.out.println("IndicadorRetencion --> " + getIndicadorRetencion());
			setNumeroRetencion(leerCampoLinea(EstructuraArchivosOps.NRO_RET_LONG.getValor()));
			//System.out.println("NumeroRetencion --> " + getNumeroRetencion());
			setMontoRetencion(leerCampoLinea(EstructuraArchivosOps.MTO_RET_LONG.getValor()));
			//System.out.println("MontoRetencion --> "+ getMontoRetencion());			
			setCodigoRetorno(leerCampoLinea(EstructuraArchivosOps.COD_RETORNO_LONG.getValor()));
			//System.out.println("CodigoRetorno --> " + getCodigoRetorno());
			setCodigoRespuesta(leerCampoLinea(EstructuraArchivosOps.COD_RESP_LONG.getValor()));
			//System.out.println("CodigoRespuesta --> " + getCodigoRespuesta());
			//Composicion de campo CamposAdicionales (WREG-CAMPOS-ADD)   
			setIdOperacion(leerCampoLinea(EstructuraArchivosOps.ID_OPERACION_LONG.getValor()));
			//System.out.println("IdOperacion() --> " + getIdOperacion());
			setIdOrdenProceso(leerCampoLinea(EstructuraArchivosOps.ORDENE_ID.getValor()));
			//System.out.println("IdOrdenProceso --> " + getIdOrdenProceso());
			setReservado(leerCampoLinea(EstructuraArchivosOps.RESERVADO_LONG.getValor()));
			//System.out.println("Reservado --> " + getReservado());
			//setCamposAdicionales(getIdOperacion()+getIdOrdenProceso()+getReservado());
			armarCamposAdicionales();
			//System.out.println("CamposAdicionales --> " + getCamposAdicionales());
					
			setDivisa(leerCampoLinea(EstructuraArchivosOps.DIVISA_LONG.getValor()));
			//System.out.println("Divisa " + getDivisa());
			setDescripcionRespuesta(leerCampoLinea(EstructuraArchivosOps.DES_RESP_LONG.getValor()));
			//System.out.println("DescripcionRespuesta --> " + getDescripcionRespuesta());
			setCodigoRespuestaRetencion(leerCampoLinea(EstructuraArchivosOps.COD_RESP_RET_LONG.getValor()));
			//System.out.println("CodigoRespuestaRetencion --> " + getCodigoRespuestaRetencion());
			setDescripcionRespuestaRetencion(leerCampoLinea(EstructuraArchivosOps.DES_RESP_RET_LONG.getValor()));
			//System.out.println("DescripcionRespuestaRetencion --> " + getDescripcionRespuestaRetencion());
			setFechaOperacion(leerCampoLinea(EstructuraArchivosOps.FECHA_OPER_LONG.getValor()));
			//System.out.println("FechaOperacion --> " + getFechaOperacion());
			setHoraOperacion(leerCampoLinea(EstructuraArchivosOps.HORA_OPER_LONG.getValor()));
			//System.out.println("HoraOperacion --> " + getHoraOperacion());
		}
		return validarTipoRegistro;
	}
		
	public void reiniciarDetaller(){
		//this.tipoRegistroCuerpo=""; 	
		this.consecutivo="";	
		this.tipoOperacion="";//Indica si la operaciones  Compra(C) o Venta (V) (Campo de uso exclusivo de Mainframe)	
		this.codOperacion="";	
		this.numeroCuenta="";	
		this.referencia="";
			//Sub campos que componen el campo  referencia
			this.digitoIndicadorRef="";//Digito 9 definido 		
			this.codigoOperacion="";
			this.ordeneIdReferencia="";
			this.digitosIndicadorFinalesRef="";//Digitos 00 definidos
			
		this.claveLiga="";
		this.montoOperacion="";
		this.indicadorRetencion="";	
		this.numeroRetencion="";
		this.montoRetencion="";
		this.codigoRetorno="";
		this.codigoRespuesta="";
		this.camposAdicionales="";
			this.idOperacion="";		
			this.reservado="";
		this.divisa="";
		this.descripcionRespuesta="";
		this.codigoRespuestaRetencion="";
		this.descripcionRespuestaRetencion="";
		this.fechaOperacion="";
		this.horaOperacion="";
	}
	
	public String toString(){
		
			
		
			

//			//Sub campos que componen el campo  referencia
//			String digitoIndicadorRef;//Digito 9 definido 		
//			String codigoOperacion;
//			String ordeneIdReferencia; //Id de orden de TOMA_ORDEN
//			String digitosIndicadorFinalesRef;//Digitos 00 definidos			

		
		//String ="";	
		String numeroRetencion="";
		String montoRetencion="";
		String codigoRetorno="";
		String codigoRespuesta="";
		String camposAdicionales="";		
			 String idOperacion="";
			 String idOrdenProceso="";	
			 String reservado="";
		String divisa="";
		String descripcionRespuesta="";
		String codigoRespuestaRetencion="";
		String descripcionRespuestaRetencion="";
		String fechaOperacion="";
		String horaOperacion="";
		return "consecutivo " + consecutivo+" tipoOperacion " + tipoOperacion + " codOperacion " + codOperacion +" numeroCuenta " + numeroCuenta + " referencia " + referencia +" claveLiga "+ claveLiga + " montoOperacion " + montoOperacion + " indicadorRetencion " + indicadorRetencion;
	}
}
