package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeSet;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


/** 
 * T&iacute;tulos registrados en a base de datos
 */
public class OrdenesCruce {

	/**
 * Depositario asociado al t&iacute;tulo
 */
private long idCruce;
 
private ArrayList<Long>listaIdCruces=new ArrayList<Long>(); 
/**
 * Getter of the property <tt>idCruce</tt>
 *
 * @return Returns the idCruce.
 * 
 */

public long getIdCruce()
{
	return idCruce;
}


/**
 * Setter of the property <tt>idCruce</tt>
 *
 * @param idCruce The idCruce to set.
 *
 */

public void setIdCruce(long idCruce ){
	this.idCruce = idCruce;
}

//Numero de orden INFI asociado el campo INFI_TB_229_ORDENES_CRUCES.ORDENE_ID
private long idOrdenINFI;

//Id de la orden de Oferta
private long idOrdenOferta;

private String idOrdenInfiString;

private String idOrdenOfertaString;

private String cruceProcesadoString;

//NM29643 - INFI_TTS_466 20/07/2014
private String liquidacionProcesadaString;

private String indTituloProcesado;

//Numero de Unidad de inversion asociado el campo INFI_TB_229_ORDENES_CRUCES.UNDINV_ID
private long idUI;

private String nameUI;

private String ciRif;

//Numero de orden BCV asociado el campo INFI_TB_229_ORDENES_CRUCES.ORDENE_ID_BCV
private long idOrdenBCV;

private TreeSet<String> listaIdBcv=new TreeSet<String>();

private String idOrdenBcvString;

//Numero de Cotizacion asignada asociado el campo INFI_TB_229_ORDENES_CRUCES.NRO_COTIZACION
private long nroCotizacion;

private String nroCotizacionString;

private String nroBCVOrdenDemanda;

private String nroBCVOrdenOferta;

//Numero de Operaciones asignada asociado el campo INFI_TB_229_ORDENES_CRUCES.NRO_OPERACION
private long nroOperacion;

private String nroOperacionString;

private BigDecimal montoSolicitado;

private String montoSolicitadoString;


//Numero de Cotizacion asignada asociado el campo INFI_TB_229_ORDENES_CRUCES.NRO_COTIZACION
private BigDecimal montoOperacion;

private String montoOperacionString;

private BigDecimal tasa;

private String tasaString;

private BigDecimal precioTitulo;

private String precioTituloString;

private long incrementalTitulo;

private double precioSucio;

private BigDecimal contravalorBolivares;

private BigDecimal contravalorBolivaresCapital;

private BigDecimal contravalorBolivaresComision;

private BigDecimal pctComision;
//private BigDecimal valorEfectivo;
//
//private String valorEfectivoString;

//Rif de contraparte INFI_TB_229_ORDENES_CRUCES.CONTRAPARTE
private String contraparte;

//Estatus asociado al movimiento de cruce INFI_TB_229_ORDENES_CRUCES.ESTATUS
private String estatus;
private String observacion;
private int indicadorTitulo;

//Campo de calculo del monto total a pagar en bolivares por el total de titulos
private BigDecimal montoTotalTitulosBs;

//Campo de calculo del monto total a pagar en bolivares por el total de titulos
private BigDecimal montoTotalDivisasBs;

private long idEjecucion;
private long idCliente;
private long clienteCiRif;
private String nombreCliente;
private String idTitulo;
private BigDecimal mtoInteresesCaidosTitulo;
private String fechaValor;
private String fechaValorGuion;

/**
 * Listado de titulos asociados a un cruce
 * */
private ArrayList<OrdenTitulo> ordenTitulo = new ArrayList<OrdenTitulo>();
private TreeSet<String> contraparteList = new TreeSet<String>();


//Campo de monto por el total de los movimientos asociados a cruce de Titulos
private BigDecimal montoTitulos=new BigDecimal(0);

//Campo de monto por el total de los movimientos asociados a cruce de Divisas
private BigDecimal montoDivisas=new BigDecimal(0);

	/**
 * Identificador &uacute;nico en el sistema donde se encuentran registrados los t&iacute;tulos
 */
private int isin;
private String isinString;
 

private boolean cruceConAdvertencia;

private boolean NoCruceConAdvertencia;
/**
 * Getter of the property <tt>isin</tt>
 *
 * @return Returns the isin.
 * 
 */
public int getIsin()
{
	return isin;
}

/**
 * Setter of the property <tt>isin</tt>
 *
 * @param isin The isin to set.
 *
 */
public void setIsin(int isin ){
	this.isin = isin;
}


/**
 * valor expresado en moneda de la inversi&oacute;n
 */
private double valorNominal;
 
/**
 * Getter of the property <tt>valorNominal</tt>
 *
 * @return Returns the valorNominal.
 * 
 */
public double getValorNominal()
{
	return valorNominal;
}

/**
 * Setter of the property <tt>valorNominal</tt>
 *
 * @param valorNominal The valorNominal to set.
 *
 */
public void setValorNominal(double valorNominal ){
	this.valorNominal = valorNominal;
}

public long getIdOrdenINFI() {
	return idOrdenINFI;
}

public void setIdOrdenINFI(long idOrdenINFI) {
	this.idOrdenINFI = idOrdenINFI;
}

public long getIdUI() {
	return idUI;
}

public void setIdUI(long idUI) {
	this.idUI = idUI;
}

/*
public long getIdOrdenBCV() {
	return idOrdenBCV;
}

public void setIdOrdenBCV(long idOrdenBCV) {
	this.idOrdenBCV = idOrdenBCV;
}
*/

public long getNroCotizacion() {
	return nroCotizacion;
}

public void setNroCotizacion(long nroCotizacion) {
	this.nroCotizacion = nroCotizacion;
}

public BigDecimal getMontoOperacion() {
	return montoOperacion;
}

public void setMontoOperacion(BigDecimal montoOperacion) {
	this.montoOperacion = montoOperacion;
}

public BigDecimal getTasa() {
	return tasa;
}

public void setTasa(BigDecimal tasa) {
	this.tasa = tasa;
}

public long getNroOperacion() {
	return nroOperacion;
}

public void setNroOperacion(long nroOperacion) {
	this.nroOperacion = nroOperacion;
}

public BigDecimal getPrecioTitulo() {
	return precioTitulo;
}

public void setPrecioTitulo(BigDecimal precioTitulo) {
	this.precioTitulo = precioTitulo;
}

//public BigDecimal getValorEfectivo() {
//	return valorEfectivo;
//}
//
//public void setValorEfectivo(BigDecimal valorEfectivo) {
//	this.valorEfectivo = valorEfectivo;
//}

public String getContraparte() {
	return contraparte;
}

public void setContraparte(String contraparte) {
	this.contraparte = contraparte;
}

public String getEstatus() {
	return estatus;
}

public void setEstatus(String estatus) {
	this.estatus = estatus;
}

public long getIdEjecucion() {
	return idEjecucion;
}

public void setIdEjecucion(long idEjecucion) {
	this.idEjecucion = idEjecucion;
}

public BigDecimal getMontoDivisas() {
	return montoDivisas;
}

public void setMontoDivisas(BigDecimal montoDivisas) {
	this.montoDivisas = montoDivisas;
}

public BigDecimal getMontoTitulos() {
	return montoTitulos;
}

public void setMontoTitulos(BigDecimal montoTitulos) {
	this.montoTitulos = montoTitulos;
}

public String toString(){
	
	return "Orden > " + idOrdenINFI +" Estatus --> "+ estatus +" MONTO DIVISA --> " + montoDivisas.toString() + " MONTO TITULO ---> " + montoTitulos +" TITULOS --->  "+  ordenTitulo.toString();
}

public BigDecimal getMontoTotalTitulosBs() {
	return montoTotalTitulosBs;
}

/*public void setMontoTotalBs(BigDecimal montoTotalBs) {
	this.montoTotalTitulosBs = montoTotalBs;
}*/

public long getIdCliente() {
	return idCliente;
}

public void setIdCliente(long idCliente) {
	this.idCliente = idCliente;
}

public long getClienteCiRif() {
	return clienteCiRif;
}

public void setClienteCiRif(long clienteCiRif) {
	this.clienteCiRif = clienteCiRif;
}

public String getNameUI() {
	return nameUI;
}

public void setNameUI(String nameUI) {
	this.nameUI = nameUI;
}

public String getCiRif() {
	return ciRif;
}

public void setCiRif(String ciRif) {
	this.ciRif = ciRif;
}

public String getIdOrdenInfiString() {
	return idOrdenInfiString;
}

public void setIdOrdenInfiString(String idOrdenInfiString) {
	this.idOrdenInfiString = idOrdenInfiString;
}

public String getIdOrdenBcvString() {
	return idOrdenBcvString;
}

public void setIdOrdenBcvString(String idOrdenBcvString) {
	this.idOrdenBcvString = idOrdenBcvString;
}

public String getNroCotizacionString() {
	return nroCotizacionString;
}

public void setNroCotizacionString(String nroCotizacionString) {
	this.nroCotizacionString = nroCotizacionString;
}

public String getNroOperacionString() {
	return nroOperacionString;
}

public void setNroOperacionString(String nroOperacionString) {
	this.nroOperacionString = nroOperacionString;
}

public String getMontoOperacionString() {
	return montoOperacionString;
}

public void setMontoOperacionString(String montoOperacionString) {
	this.montoOperacionString = montoOperacionString;
}

public String getTasaString() {
	return tasaString;
}

public void setTasaString(String tasaString) {
	this.tasaString = tasaString;
}

public String getPrecioTituloString() {
	return precioTituloString;
}

public void setPrecioTituloString(String precioTituloString) {
	this.precioTituloString = precioTituloString;
}

//public String getValorEfectivoString() {
//	return valorEfectivoString;
//}
//
//public void setValorEfectivoString(String valorEfectivoString) {
//	this.valorEfectivoString = valorEfectivoString;
//}

public BigDecimal getMontoSolicitado() {
	return montoSolicitado;
}

public void setMontoSolicitado(BigDecimal montoSolicitado) {
	this.montoSolicitado = montoSolicitado;
}

public String getMontoSolicitadoString() {
	return montoSolicitadoString;
}

public void setMontoSolicitadoString(String montoSolicitadoString) {
	this.montoSolicitadoString = montoSolicitadoString;
}

public String getIsinString() {
	return isinString;
}

public void setIsinString(String isinString) {
	this.isinString = isinString;
}

public String getObservacion() {
	return observacion;
}

public void setObservacion(String observacion) {
	this.observacion = observacion;
}

/**
 * @return indicadorTitulo
 */
public int getIndicadorTitulo() {
	return indicadorTitulo;
}

/**
 * @param indicadorTitulo
 */
public void setIndicadorTitulo(int indicadorTitulo) {
	this.indicadorTitulo = indicadorTitulo;
}

public String getNombreCliente() {
	return nombreCliente;
}

public void setNombreCliente(String nombreCliente) {
	this.nombreCliente = nombreCliente;
}

public void setMontoTotalTitulosBs(BigDecimal montoTotalTitulosBs) {
	this.montoTotalTitulosBs = montoTotalTitulosBs;
}

/**
 * @return ArrayList<OrdenTitulo>
 */
public ArrayList<OrdenTitulo> getOrdenTitulo() {
	return ordenTitulo;
}

/**
 * @param ordenTitulo
 */
public void setOrdenTitulo(ArrayList<OrdenTitulo> ordenTitulo) {
	this.ordenTitulo = ordenTitulo;
}


/**
 * Indica si la lista de t&iacute;tulos est&aacute; vacia
 * 
 * @return <tt>true</tt> Verdadero en caso que la lista no tenga elementos
 * @see java.util.ArrayList#isEmpty()
 * 
 */
public boolean isOrdenTituloVacio() {
	return ordenTitulo.isEmpty();
}

/**
 * Ensures that this collection contains the specified element (optional
 * operation).
 * 
 * @param element
 *            whose presence in this collection is to be ensured.
 * @see java.util.Collection#add(Object)
 * 
 */
public boolean agregarOrdenTitulo(OrdenTitulo ordenTitulo) {
	return this.ordenTitulo.add(ordenTitulo);
}


/**
 * @param ArrayList <ordenTitulos>
 */
public void agregarOrdenTitulo(ArrayList ordenTitulos) {
	this.ordenTitulo.addAll(ordenTitulos);
}

public String getFechaValor() {
	return fechaValor;
}

public void setFechaValor(String fechaValor) {
	this.fechaValor = fechaValor;
}

public String getIdTitulo() {
	return idTitulo;
}

public void setIdTitulo(String idTitulo) {
	this.idTitulo = idTitulo;
}

public BigDecimal getMtoInteresesCaidosTitulo() {
	return mtoInteresesCaidosTitulo;
}

public void setMtoInteresesCaidosTitulo(BigDecimal mtoInteresesCaidosTitulo) {
	this.mtoInteresesCaidosTitulo = mtoInteresesCaidosTitulo;
}

public BigDecimal getMontoTotalDivisasBs() {
	return montoTotalDivisasBs;
}

public void setMontoTotalDivisasBs(BigDecimal montoTotalDivisasBs) {
	this.montoTotalDivisasBs = montoTotalDivisasBs;
}

public String getCruceProcesadoString() {
	return cruceProcesadoString;
}

public void setCruceProcesadoString(String cruceProcesadoString) {
	this.cruceProcesadoString = cruceProcesadoString;
}

public long getIncrementalTitulo() {
	return incrementalTitulo;
}

public void setIncrementalTitulo(long incrementalTitulo) {
	this.incrementalTitulo = incrementalTitulo;
}

public ArrayList<Long> getListaIdCruces() {
	return listaIdCruces;
}

public void setListaIdCruces(ArrayList<Long> listaIdCruces) {
	this.listaIdCruces = listaIdCruces;
}

public void agregarListaIdCruces(long idCruce) {
	this.listaIdCruces.add(idCruce);
}

public double getPrecioSucio() {
	return precioSucio;
}

public void setPrecioSucio(double precioSucio) {
	this.precioSucio = precioSucio;
}

public BigDecimal getContravalorBolivares() {
	return contravalorBolivares;
}

public void setContravalorBolivares(BigDecimal contravalorBolivares) {
	this.contravalorBolivares = contravalorBolivares;
}

public TreeSet<String> getListaIdBcv() {
	return listaIdBcv;
}

public void setListaIdBcv(TreeSet<String> listaIdBcv) {
	this.listaIdBcv = listaIdBcv;
}

public void agregarIdBcv(String idBcv) {
	this.listaIdBcv.add(idBcv);
}
public boolean isListaBcvIdVacio() {
	return listaIdBcv.isEmpty();
}

public BigDecimal getContravalorBolivaresCapital() {
	return contravalorBolivaresCapital;
}

public void setContravalorBolivaresCapital(BigDecimal contravalorBolivaresCapital) {
	this.contravalorBolivaresCapital = contravalorBolivaresCapital;
}

public BigDecimal getContravalorBolivaresComision() {
	return contravalorBolivaresComision;
}

public void setContravalorBolivaresComision(BigDecimal contravalorBolivaresComision) {
	this.contravalorBolivaresComision = contravalorBolivaresComision;
}

public BigDecimal getPctComision() {
	return pctComision;
}

public void setPctComision(BigDecimal pctComision) {
	this.pctComision = pctComision;
}

public TreeSet<String> getContraparteList() {
	return contraparteList;
}

public void setContraparteList(TreeSet<String> contraparteList) {
	this.contraparteList = contraparteList;
}
	
public boolean isContraparteListVacio() {
	return contraparteList.isEmpty();
}

public boolean agregarContraparteList(String contraparte) {
	return this.contraparteList.add(contraparte);
}

public String getFechaValorGuion() {
	return fechaValorGuion;
}

public void setFechaValorGuion(String fechaValorGuion) {
	this.fechaValorGuion = fechaValorGuion;
}

//NM29643 - INFI_TTS_466 20/07/2014
public String getLiquidacionProcesadaString() {
	return liquidacionProcesadaString;
}

public void setLiquidacionProcesadaString(String liquidacionProcesadaString) {
	this.liquidacionProcesadaString = liquidacionProcesadaString;
}

//NM29643 - INFI_TTS_466 20/07/2014
public String getIndTituloProcesado() {
	return indTituloProcesado;
}

public void setIndTituloProcesado(String indTituloProcesado) {
	this.indTituloProcesado = indTituloProcesado;
}

public long getIdOrdenOferta() {
	return idOrdenOferta;
}

public void setIdOrdenOferta(long idOrdenOferta) {
	this.idOrdenOferta = idOrdenOferta;
}

public String getNroBCVOrdenDemanda() {
	return nroBCVOrdenDemanda;
}

public void setNroBCVOrdenDemanda(String nroBCVOrdenDemanda) {
	this.nroBCVOrdenDemanda = nroBCVOrdenDemanda;
}

public String getIdOrdenOfertaString() {
	return idOrdenOfertaString;
}

public void setIdOrdenOfertaString(String idOrdenOfertaString) {
	this.idOrdenOfertaString = idOrdenOfertaString;
}

public String getNroBCVOrdenOferta() {
	return nroBCVOrdenOferta;
}

public void setNroBCVOrdenOferta(String nroBCVOrdenOferta) {
	this.nroBCVOrdenOferta = nroBCVOrdenOferta;
}

}
