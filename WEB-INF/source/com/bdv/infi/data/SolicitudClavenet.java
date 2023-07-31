package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase que representa la orden SITME tomada desde Clavenet y registrada en OPICS
 * */
public class SolicitudClavenet {
	
	private long idOrden;
	private long idOrdenInfi;
	private String nombreCliente;
	private String cedRifCliente;
	//Nuevos atributos NM29643
	private String cedRifNroCli;
	private String tipPerCli;
	//Hasta aqui Nuevos
	private Date fechaVencRif;
	private String numRuSitme;
	private String emailCliente;
	private String cedRifContraparte;
	//Nuevos atributos NM29643
	private String cedRifNroContra;
	private String tipPerContra;
	//Hasta aqui Nuevos
	
	private BigDecimal montoSolicitado;
	private BigDecimal montoComision;	
	private double porcentajeComision;
	private String numeroBloqueo;
	private BigDecimal montoAdjudicado;
	
	private long idConcepto;
	private long sectorProductivo;
	private long sectorActividadEconomica;
	private String grupoEconomico;
	
	private String estatus;
	private String fechaRegistro;
	private String horaRegistro;
	private String fechaTramite;	
	private String fechaAdjudicacion;
	private String fechaCobro;
	private String fechaEnvioSwift;
	private String fechaRecepSwift;
	
	private String tituloAdjudicado;
	private double precioAdjudicacion;
	private BigDecimal valorEfectivoUSD;
	private BigDecimal valorNominalUSD;
	private BigDecimal valorEfectivo;
	private BigDecimal valorNominal;
	private String cuentaBsO;
	//Nuevos NM29643
	private String nombreBeneficiario;
	private String ctaNro;
	private String ctaBco;
	private String ctaDireccion;
	private String ctaBicSwift;
	private String ctaTlf;
	private String ctaTlf2;
	private String ctaTlf3;
	private String ctaAba;
	private String ctaDireccionC;
	private String ctaIban;
	private String ctaIntNro;
	private String ctaIntBco;
	private String ctaIntDireccion;
	private String ctaIntBicSwift;
	private String ctaIntTlf;
	private String ctaIntAba;
	//Nuevos hasta aqui	
	private long contadorEnviosSwift;
	private long idCanal;
	private Date fechaModificacion;
	//Nuevos NM29643
	private Date fechaSalidaViaje;
	private Date fechaRetornoViaje;
	private String codigoPaisOrigen;
	private String descPaisOrigen;
	private String codigoEdoOrigen;
	private String descEdoOrigen;
	private String codigoCiudadOrigen;
	private String descCiudadOrigen;
	private String codigoPaisDestino;
	private String descPaisDestino;
	private String nroPasaporte;
	private String nroBoleto;
	private int ctaAbono;
	private String ctaAbonoMoneda;
	private String nroBoletoVuelta;
	private int idProducto;
	private double tasaCambio;
	//Nuevos hasta Aqui
	
	private boolean fechaActualTramite;	
	private InstruccionesPago instruccionPago;

	public String getCedRifCliente() {
		return cedRifCliente;
	}

	public void setCedRifCliente(String cedRifCliente) {
		this.cedRifCliente = cedRifCliente;
	}

	public String getCedRifContraparte() {
		return cedRifContraparte;
	}

	public void setCedRifContraparte(String cedRifContraparte) {
		this.cedRifContraparte = cedRifContraparte;
	}

	public long getContadorEnviosSwift() {
		return contadorEnviosSwift;
	}

	public void setContadorEnviosSwift(long contadorEnviosSwift) {
		this.contadorEnviosSwift = contadorEnviosSwift;
	}

	public String getCuentaBsO() {
		return cuentaBsO;
	}

	public void setCuentaBsO(String cuentaBsO) {
		this.cuentaBsO = cuentaBsO;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getFechaAdjudicacion() {
		return fechaAdjudicacion;
	}

	public void setFechaAdjudicacion(String fechaAdjudicacion) {
		this.fechaAdjudicacion = fechaAdjudicacion;
	}

	public String getFechaCobro() {
		return fechaCobro;
	}

	public void setFechaCobro(String fechaCobro) {
		this.fechaCobro = fechaCobro;
	}

	public String getFechaEnvioSwift() {
		return fechaEnvioSwift;
	}

	public void setFechaEnvioSwift(String fechaEnvioSwift) {
		this.fechaEnvioSwift = fechaEnvioSwift;
	}

	public String getFechaRecepSwift() {
		return fechaRecepSwift;
	}

	public void setFechaRecepSwift(String fechaRecepSwift) {
		this.fechaRecepSwift = fechaRecepSwift;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getFechaTramite() {
		return fechaTramite;
	}

	public void setFechaTramite(String fechaTramite) {
		this.fechaTramite = fechaTramite;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	public Date getFechaVencRif() {
		return fechaVencRif;
	}

	public void setFechaVencRif(Date fechaVencRif) {
		this.fechaVencRif = fechaVencRif;
	}

	public String getGrupoEconomico() {
		return grupoEconomico;
	}

	public void setGrupoEconomico(String grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
	}

	public String getHoraRegistro() {
		return horaRegistro;
	}

	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	public long getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(long idCanal) {
		this.idCanal = idCanal;
	}

	public long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public long getIdOrden() {
		return idOrden;
	}

	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}

	public long getIdOrdenInfi() {
		return idOrdenInfi;
	}

	public void setIdOrdenInfi(long idOrdenInfi) {
		this.idOrdenInfi = idOrdenInfi;
	}

	public InstruccionesPago getInstruccionPago() {
		return instruccionPago;
	}

	public void setInstruccionPago(InstruccionesPago instruccionPago) {
		this.instruccionPago = instruccionPago;
	}

	public BigDecimal getMontoAdjudicado() {
		return montoAdjudicado;
	}

	public void setMontoAdjudicado(BigDecimal montoAdjudicado) {
		this.montoAdjudicado = montoAdjudicado;
	}

	public BigDecimal getMontoComision() {
		return montoComision;
	}

	public void setMontoComision(BigDecimal montoComision) {
		this.montoComision = montoComision;
	}

	public BigDecimal getMontoSolicitado() {
		return montoSolicitado;
	}

	public void setMontoSolicitado(BigDecimal montoSolicitado) {
		this.montoSolicitado = montoSolicitado;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNumRuSitme() {
		return numRuSitme;
	}

	public void setNumRuSitme(String numRuSitme) {
		this.numRuSitme = numRuSitme;
	}

	public double getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(double porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

	public double getPrecioAdjudicacion() {
		return precioAdjudicacion;
	}

	public void setPrecioAdjudicacion(double precioAdjudicacion) {
		this.precioAdjudicacion = precioAdjudicacion;
	}

	public long getSectorActividadEconomica() {
		return sectorActividadEconomica;
	}

	public void setSectorActividadEconomica(long sectorActividadEconomica) {
		this.sectorActividadEconomica = sectorActividadEconomica;
	}

	public long getSectorProductivo() {
		return sectorProductivo;
	}

	public void setSectorProductivo(long sectorProductivo) {
		this.sectorProductivo = sectorProductivo;
	}

	public String getTituloAdjudicado() {
		return tituloAdjudicado;
	}

	public void setTituloAdjudicado(String tituloAdjudicado) {
		this.tituloAdjudicado = tituloAdjudicado;
	}

	public BigDecimal getValorEfectivo() {
		return valorEfectivo;
	}

	public void setValorEfectivo(BigDecimal valorEfectivo) {
		this.valorEfectivo = valorEfectivo;
	}

	public BigDecimal getValorEfectivoUSD() {
		return valorEfectivoUSD;
	}

	public void setValorEfectivoUSD(BigDecimal valorEfectivoUSD) {
		this.valorEfectivoUSD = valorEfectivoUSD;
	}

	public BigDecimal getValorNominal() {
		return valorNominal;
	}

	public void setValorNominal(BigDecimal valorNominal) {
		this.valorNominal = valorNominal;
	}

	public BigDecimal getValorNominalUSD() {
		return valorNominalUSD;
	}

	public void setValorNominalUSD(BigDecimal valorNominalUSD) {
		this.valorNominalUSD = valorNominalUSD;
	}

	public String getNumeroBloqueo() {
		return numeroBloqueo;
	}

	public void setNumeroBloqueo(String numeroBloqueo) {
		this.numeroBloqueo = numeroBloqueo;
	}

	public boolean isFechaActualTramite() {
		return fechaActualTramite;
	}

	public void setFechaActualTramite(boolean fechaActualTramite) {
		this.fechaActualTramite = fechaActualTramite;
	}
	
	public String getNombreBeneficiario() {
		return nombreBeneficiario;
	}

	public void setNombreBeneficiario(String nombreBeneficiario) {
		this.nombreBeneficiario = nombreBeneficiario;
	}
	
	public String getCtaNro() {
		return ctaNro;
	}

	public void setCtaNro(String ctaNro) {
		this.ctaNro = ctaNro;
	}
	
	public String getCtaBco() {
		return ctaBco;
	}

	public void setCtaBco(String ctaBco) {
		this.ctaBco = ctaBco;
	}
	
	public String getCtaDireccion() {
		return ctaDireccion;
	}

	public void setCtaDireccion(String ctaDireccion) {
		this.ctaDireccion = ctaDireccion;
	}
	
	public String getCtaBicSwift() {
		return ctaBicSwift;
	}

	public void setCtaBicSwift(String ctaBicSwift) {
		this.ctaBicSwift = ctaBicSwift;
	}
	
	public String getCtaTlf() {
		return ctaTlf;
	}

	public void setCtaTlf(String ctaTlf) {
		this.ctaTlf = ctaTlf;
	}
	
	public String getCtaTlf2() {
		return ctaTlf2;
	}

	public void setCtaTlf2(String ctaTlf2) {
		this.ctaTlf2 = ctaTlf2;
	}
	
	public String getCtaTlf3() {
		return ctaTlf3;
	}

	public void setCtaTlf3(String ctaTlf3) {
		this.ctaTlf3 = ctaTlf3;
	}
	
	public String getCtaAba() {
		return ctaAba;
	}

	public void setCtaAba(String ctaAba) {
		this.ctaAba = ctaAba;
	}
	
	public String getCtaDireccionC() {
		return ctaDireccionC;
	}

	public void setCtaDireccionC(String ctaDireccionC) {
		this.ctaDireccionC = ctaDireccionC;
	}
	
	public String getCtaIban() {
		return ctaIban;
	}

	public void setCtaIban(String ctaIban) {
		this.ctaIban = ctaIban;
	}
	
	public String getCtaIntNro() {
		return ctaIntNro;
	}

	public void setCtaIntNro(String ctaIntNro) {
		this.ctaIntNro = ctaIntNro;
	}
	
	public String getCtaIntBco() {
		return ctaIntBco;
	}

	public void setCtaIntBco(String ctaIntBco) {
		this.ctaIntBco = ctaIntBco;
	}
	
	public String getCtaIntDireccion() {
		return ctaIntDireccion;
	}

	public void setCtaIntDireccion(String ctaIntDireccion) {
		this.ctaIntDireccion = ctaIntDireccion;
	}
	
	public String getCtaIntBicSwift() {
		return ctaIntBicSwift;
	}

	public void setCtaIntBicSwift(String ctaIntBicSwift) {
		this.ctaIntBicSwift = ctaIntBicSwift;
	}
	
	public String getCtaIntTlf() {
		return ctaIntTlf;
	}

	public void setCtaIntTlf(String ctaIntTlf) {
		this.ctaIntTlf = ctaIntTlf;
	}
	
	public String getCtaIntAba() {
		return ctaIntAba;
	}

	public void setCtaIntAba(String ctaIntAba) {
		this.ctaIntAba = ctaIntAba;
	}
	
	public Date getFechaSalidaViaje() {
		return fechaSalidaViaje;
	}

	public void setFechaSalidaViaje(Date fechaSalidaViaje) {
		this.fechaSalidaViaje = fechaSalidaViaje;
	}
	
	public Date getFechaRetornoViaje() {
		return fechaRetornoViaje;
	}

	public void setFechaRetornoViaje(Date fechaRetornoViaje) {
		this.fechaRetornoViaje = fechaRetornoViaje;
	}
	
	public String getCodigoPaisOrigen() {
		return codigoPaisOrigen;
	}

	public void setCodigoPaisOrigen(String codigoPaisOrigen) {
		this.codigoPaisOrigen = codigoPaisOrigen;
	}
	
	public String getDescPaisOrigen() {
		return descPaisOrigen;
	}

	public void setDescPaisOrigen(String descPaisOrigen) {
		this.descPaisOrigen = descPaisOrigen;
	}
	
	public String getCodigoEdoOrigen() {
		return codigoEdoOrigen;
	}

	public void setCodigoEdoOrigen(String codigoEdoOrigen) {
		this.codigoEdoOrigen = codigoEdoOrigen;
	}
	
	public String getDescEdoOrigen() {
		return descEdoOrigen;
	}

	public void setDescEdoOrigen(String descEdoOrigen) {
		this.descEdoOrigen = descEdoOrigen;
	}
	
	public String getCodigoCiudadOrigen() {
		return codigoCiudadOrigen;
	}

	public void setCodigoCiudadOrigen(String codigoCiudadOrigen) {
		this.codigoCiudadOrigen = codigoCiudadOrigen;
	}
	
	public String getDescCiudadOrigen() {
		return descCiudadOrigen;
	}

	public void setDescCiudadOrigen(String descCiudadOrigen) {
		this.descCiudadOrigen = descCiudadOrigen;
	}
	
	public String getCodigoPaisDestino() {
		return codigoPaisDestino;
	}

	public void setCodigoPaisDestino(String codigoPaisDestino) {
		this.codigoPaisDestino = codigoPaisDestino;
	}
	
	public String getDescPaisDestino() {
		return descPaisDestino;
	}

	public void setDescPaisDestino(String descPaisDestino) {
		this.descPaisDestino = descPaisDestino;
	}
	
	public String getNroPasaporte() {
		return nroPasaporte;
	}

	public void setNroPasaporte(String nroPasaporte) {
		this.nroPasaporte = nroPasaporte;
	}
	
	public String getNroBoleto() {
		return nroBoleto;
	}

	public void setNroBoleto(String nroBoleto) {
		this.nroBoleto = nroBoleto;
	}
	
	public int getCtaAbono() {
		return ctaAbono;
	}
	
	public void setCtaAbono(int ctaAbono) {
		this.ctaAbono = ctaAbono;
	}
	
	public String getCtaAbonoMoneda() {
		return ctaAbonoMoneda;
	}

	public void setCtaAbonoMoneda(String ctaAbonoMoneda) {
		this.ctaAbonoMoneda = ctaAbonoMoneda;
	}
	
	public String getNroBoletoVuelta() {
		return nroBoletoVuelta;
	}

	public void setNroBoletoVuelta(String nroBoletoVuelta) {
		this.nroBoletoVuelta = nroBoletoVuelta;
	}
	
	public int getIdProducto() {
		return idProducto;
	}
	
	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}
	
	public double getTasaCambio() {
		return tasaCambio;
	}
	
	public void setTasaCambio(double tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	
	public String getCedRifNroCli() {
		return cedRifNroCli;
	}
	
	public void setCedRifNroCli(String cedRifNroCli) {
		this.cedRifNroCli = cedRifNroCli;
	}
	
	public String getTipPerCli() {
		return tipPerCli;
	}
	
	public void setTipPerCli(String tipPerCli) {
		this.tipPerCli = tipPerCli;
	}
	
	public String getCedRifNroContra() {
		return cedRifNroContra;
	}
	
	public void setCedRifNroContra(String cedRifNroContra) {
		this.cedRifNroContra = cedRifNroContra;
	}
	
	public String getTipPerContra() {
		return tipPerContra;
	}
	
	public void setTipPerContra(String tipPerContra) {
		this.tipPerContra = tipPerContra;
	}
	
	public String[] getTipNroFromCedRif(String cedRif){
		String arr[] = new String[2];
		if(cedRif.length()>0){
			arr[0] = cedRif.charAt(0)+"";
			arr[1] = (Long.parseLong(cedRif.substring(1, cedRif.length()-1)))+""; //Se descarta el nro digito (verificador)
		}
		return arr;
	}
	
}
