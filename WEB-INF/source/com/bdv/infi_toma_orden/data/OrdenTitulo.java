package com.bdv.infi_toma_orden.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * OrdenTitulo
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class OrdenTitulo implements Serializable {

	private static final long serialVersionUID = -5773401239234852217L;
	/** Identificador de la Orden */ 
	private long idOrden;
	/** Identificador del Titulo */
	private String idTitulo;
	/** Descripcion del OrdenTitulo	 */
	private String descrTitulo = "";	
	/** Porcentaje que forma parte del 100% de los títulos */
	private BigDecimal porcentaje = new BigDecimal(0);	
	/** Monto calculado en base al porcentaje definido */
	private BigDecimal valorNominal = new BigDecimal(0);
	/** Unidades compradas por la Orden */
	private BigDecimal unidadesInvertidas;
	/** Monto Comprado */
	private BigDecimal valorInvertido = new BigDecimal(0);	
	/** Siglas que identifica la moneda usada */
	private String siglasMoneda = "";	
	/** Porcentaje de Recompra del Título */
	private BigDecimal porcentajeRecompra = new BigDecimal(0);
	/** Precio de mercado*/
	private BigDecimal precioMercado;
	/** Monto de intereses caídos*/
	private BigDecimal montoIntCaidos = new BigDecimal(0);
	/** Fecha de Emisi&oacute;n del T&iacute;tulo */
	private Date fechaEmision;
	/** Fecha de Vencimiento del T&iacute;tulo */
	private Date fechaVencimiento;
	/** Base anual de c&aacute;lculo */
	private String basis;
	/** Indicador con IDB */
	private int indicadorConIDB = 0;
	/** Moneda de Negociacion */
	private String monedaNegociacion = "";
	/**Corresponde el monto cancelado por recompra con neteo*/
	private BigDecimal montoNeteo = BigDecimal.ZERO;
	
	/** Cupon para Orden de Recompra - SITME */
	public BigDecimal cupon = new BigDecimal(0);
	/** Diferencias de dias Para orden de RecompraCupon - SITME*/
	public int diferenciaDias;
	/** Fecha Valor para orden de Recompra - SITME*/
	public String fecha_valor_recompra;
	/** Factor para el cálculo de Interes segun la Base */
	private BigDecimal factorCalculo = BigDecimal.ZERO;

	/**
	 * Constructor del bean
	 * Permite asignar los valores a los atributos del bean
	 */
	public OrdenTitulo (Object [] objValor) throws Exception {
		Long lAux = (Long)objValor[0];
		this.idOrden = lAux.longValue();
		this.idTitulo = (String)objValor[1];
		this.descrTitulo = (String)objValor[2];	
		this.valorNominal = (BigDecimal)objValor[3];			
		this.porcentaje  = (BigDecimal)objValor[4];	
		this.siglasMoneda  = (String)objValor[5];
		//this.porcentajeRecompra  = (BigDecimal)objValor[6];
		this.fechaEmision = (Date)objValor[6];
		this.fechaVencimiento = (Date)objValor[7];
		this.basis = (String)objValor[8];
		this.indicadorConIDB = (Integer)objValor[9];
		this.monedaNegociacion = (String)objValor[10];
		//TODO montos con neteo
		//this.montoNeteo = (BigDecimal)objValor[11];
	}
	
	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public OrdenTitulo() {
		
	}
	public long getIdOrden() {
		return idOrden;
	}
	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}
	
	public String getIdTitulo() {
		return idTitulo;
	}

	public void setIdTitulo(String idTitulo) {
		this.idTitulo = idTitulo;
	}
	
	public String getDescrTitulo() {
		return descrTitulo;
	}

	public void setDescrTitulo(String descrTitulo) {
		this.descrTitulo = descrTitulo;
	}
	
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	public BigDecimal getValorNominal() {
		return valorNominal;
	}

	public void setValorNominal(BigDecimal monto) {
		this.valorNominal = monto;
	}
	
	public BigDecimal getUnidadesInvertidas() {
		return unidadesInvertidas;
	}

	public void setUnidadesInvertidas(BigDecimal unidadesInvertidas) {
		this.unidadesInvertidas = unidadesInvertidas;
	}
	
	public BigDecimal getValorInvertido() {
		return valorInvertido;
	}

	public void setValorInvertido(BigDecimal monto) {
		this.valorInvertido = monto;
	}
	
	public String getSiglasMoneda() {
		return siglasMoneda;
	}
	
	public void setSiglasMoneda(String siglasMoneda) {
		this.siglasMoneda = siglasMoneda;
	}
	
	public void setPorcentajeRecompra(BigDecimal porcentajeRecompra) {
		this.porcentajeRecompra = porcentajeRecompra;
	}
	
	public BigDecimal getPorcentajeRecompra() {
		return porcentajeRecompra;
	}

	public String getBasis() {
		return basis;
	}

	public void setBasis(String basis) {
		this.basis = basis;
	}
	
	public BigDecimal getPrecioMercado() {
		return precioMercado;
	}

	public void setPrecioMercado(BigDecimal precioMercado) {
		this.precioMercado = precioMercado;
	}
	
	public BigDecimal getMontoIntCaidos() {
		return montoIntCaidos;
	}

	public void setMontoIntCaidos(BigDecimal montoIntCaidos) {
		this.montoIntCaidos = montoIntCaidos;
	}

	public int getIndicadorConIDB() {
		return indicadorConIDB;
	}

	public void setIndicadorConIDB(int indicadorConIDB) {
		this.indicadorConIDB = indicadorConIDB;
	}

	public String getMonedaNegociacion() {
		return monedaNegociacion;
	}

	public void setMonedaNegociacion(String monedaNegociacion) {
		this.monedaNegociacion = monedaNegociacion;
	}

	public BigDecimal getMontoNeteo() {
		return montoNeteo;
	}

	public void setMontoNeteo(BigDecimal montoNeteo) {
		this.montoNeteo = montoNeteo;
	}
	
	public BigDecimal getCupon() {
		return cupon;
	}

	public void setCupon(BigDecimal cupon) {
		this.cupon = cupon;
	}

	public int getDiferenciaDias() {
		return diferenciaDias;
	}

	public void setDiferenciaDias(int diferenciaDias) {
		this.diferenciaDias = diferenciaDias;
	}
	public String getFecha_valor_recompra() {
		return fecha_valor_recompra;
	}

	public void setFecha_valor_recompra(String fechaValorRecompra) {
		fecha_valor_recompra = fechaValorRecompra;
	}

	public BigDecimal getFactorCalculo() {
		return factorCalculo;
	}

	public void setFactorCalculo(BigDecimal factorCalculo) {
		this.factorCalculo = factorCalculo;
	}
	
	
}
