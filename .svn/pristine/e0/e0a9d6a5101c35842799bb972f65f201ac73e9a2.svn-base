package com.bdv.infi.data;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import megasoft.DataSet;


/** 
 * Clase que maneja la unidad de inversi&oacute;n registrada en la base de datos
 */
public class UnidadInversion {
	
	/**
	 * Credenciales del Usuario
	 */
	private DataRegistro credenciales;
	/**
	 * Identificador de la Unidad de Inversion
	 */
	private long idUnidadInversion;
	/**
	 * Nombre asignado de la Unidad de Inversion
	 */
	private String nombreUnidadInversion = "";	
	/**
	 * Emision a la cual pertenece la Unidad de Inversion
	 */
	private String emisionUnidadInversion = "";
	/**
	 * Serie a la cual pertenece la Unidad de Inversion
	 */
	private String serieUnidadInversion = "";
	/**
	 * Descripcion de la Unidad de Inversion
	 */
	private String descrUnidadInversion = "";
	/**
	 * Identificador del Instrumento Financiero que es la Unidad de Inversion
	 */
	private String idInstrumentoFinanciero = "";
	/**
	 * Tasa de Cambio que se aplica en el proceso de Toma de Ordenes
	 */
	private BigDecimal tasaCambio = new BigDecimal(0);
	/**
	 * Tasa de Cambio de la Oferta que se aplica en el proceso de Toma de Ordenes
	 */	
	private BigDecimal tasaCambioOferta = new BigDecimal(0);
	
	/**
	 * Comision IGTF a aplicar para los clientes
	 */	
	private BigDecimal comisionIGTF = new BigDecimal(0);
	
	/**
	 * Fecha de emision de la Unidad de Inversion
	 */
	private Date fechaEmisionUI;
	/**
	 * Fecha de adjudicacion de la Unidad de Inversion
	 */
	private Date fechaAdjudicacionUI;
	/**
	 * Fecha de liquidacion de la Unidad de Inversion
	 */
	private Date fechaLiquidaUI;
	
	/**
	 * Hora y fecha en que debe ejecutarse el proceso de liquidación primer intento
	 */
	private Date fechaLiquidaUIHora1;
	
	/**
	 * Hora y fecha en que debe ejecutarse el proceso de liquidación segundo intento 
	 */
	private Date fechaLiquidaUIHora2;	
	
	/**
	 * Fecha de cierre de la Unidad de Inversion
	 */
	private Date fechaCierreUI;	
	/**
	 * Indicador de como se debe tomar la Orden: 1:Monto 0: Cantidad
	 */
	private int indicaPedidoMonto = 0;
	/**
	 * Indicador para venta a los Empleados
	 */
	private int indicaVtaEmpleados = 0;
	/**
	 * Indicador se puede pactar recompra de neteo
	 */
	private int indicaRecompraNeteo = 0;
	/**
	 * Identificador de la moneda
	 */
	private String idMoneda = "";	
	/**
	 * Identificador de la empresa que es emisor
	 */
	private String idEmpresaEmisor = "";	
	/**
	 * Comentarios
	 */
	private String comentarios = "";	
	/**
	 * Monto total de Inventario disponible para la Venta, aplica a Inventario
	 */
	private BigDecimal totalInversion = new BigDecimal(0);		
	/**
	 * Identificador del Status que tiene la Unidad de Inversion
	 */
	private String idUIStatus = "";
	/**
	 * Tipo de Instrumento : insfin_forma_orden
	 */
	private String tipoInstrumento = "";
	
	/**
	 * Monto m&iacute;nimo de inversi&oacute;n cuando es subasta
	 **/
	private BigDecimal montoMinimoInversionSubasta = new BigDecimal(0);	
	/**
	 * Multiplo de inversi&oacute;n
	 **/
	private BigDecimal montoMultiplos = new BigDecimal(0);	
	/**
	 * Indicador a precio sucio o no para las ordenes
	 */
	private int indicadorPrecioSucio  = 0;
	
	/**Tasa pool asociada a los instrumentos de tipo inventario*/
	private BigDecimal tasaPool = new BigDecimal(0);
	
	/**Porcentaje de rendimiento cuando el instrumento es de inventario*/
	private BigDecimal pctRendimiento = new BigDecimal(0);
	
	/**Tipo de mercado para la venta de instrumentos. Mercado PRIMARIO, SECUNDARIO. Está en ConstantesGenerales*/
	private String tipoMercado = "";
	
	/**Indica si el pago de liquidación fue efectuado al Banco Central de Venezuela 1=si, 0=no*/
	private String inPagoBCV;	
	
	private int inCobroAdjudicacion;
	private int inCobroLiquidacion;
	private int diasAperturaDeCuenta;
	
	/**Indica si la unidad de inversion permite cancelacion de ordenes 1=si, 0=no*/
	private int permiteCancelacion;	
	
	/**Indica si la unidad de inversion permite manejo de alto valor 0=no aplica, 1=alto valor, 0=bajo valor*/
	private int permiteAltoValor;
		
	/**
	 * Multiplo de inversi&oacute;n para efectivo.TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015
	 **/
	private BigDecimal montoMultiplosEfectivo = new BigDecimal(0);	

	/**
	 * Monto maximo de inversion.TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015
	 **/
	private BigDecimal montoMaximoInversionSubasta = new BigDecimal(0);	
	
	/**Indica la periodicidad de venta. Cada cuantos días se permite la venta.TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015*/
	private int periodicidadVenta =0;
	
	public int getDiasAperturaDeCuenta() {
		return diasAperturaDeCuenta;
	}
	public void setDiasAperturaDeCuenta(int diasAperturaDeCuenta) {
		this.diasAperturaDeCuenta = diasAperturaDeCuenta;
	}
	public int getInCobroAdjudicacion() {
		return inCobroAdjudicacion;
	}
	public void setInCobroAdjudicacion(int inCobroAdjudicacion) {
		this.inCobroAdjudicacion = inCobroAdjudicacion;
	}
	public int getInCobroLiquidacion() {
		return inCobroLiquidacion;
	}
	public void setInCobroLiquidacion(int inCobroLiquidacion) {
		this.inCobroLiquidacion = inCobroLiquidacion;
	}
	public DataRegistro getCredenciales() {
		return credenciales;
	}
	public void setCredenciales(DataRegistro credenciales) {
		this.credenciales = credenciales;
	}
	/**
	 * Retorna el valor del atributo Identificador de la Unidad de Inversion
	 * @return
	 */
	public long getIdUnidadInversion() {
		return idUnidadInversion;
	}
	/**
	 * Asigna valor al atributo Identificador de la Unidad de Inversion
	 * @param idUnidadInversion
	 */	
	public void setIdUnidadInversion(long idUnidadInversion) {
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
	public String getIdInstrumentoFinanciero() {
		return idInstrumentoFinanciero;
	}
	/**
	 * Asigna valor al atributo Identificador del Instrumento Financiero que es la Unidad de Inversion
	 * @param idInstrumentoFinanciero
	 */	
	public void setIdInstrumentoFinanciero(String idInstrumentoFinanciero) {
		this.idInstrumentoFinanciero = idInstrumentoFinanciero;
	}
	
	
	/**
	 * Retorna el valor del atributo Tasa de Cambio que se aplica en el proceso de Toma de Ordenes
	 * @return
	 */
	public BigDecimal getTasaCambio() {
		return tasaCambio;
	}
	/**
	 * Asigna valor al atributo Tasa de Cambio que se aplica en el proceso de Toma de Ordenes
	 * @param tasaCambio
	 */	
	public void setTasaCambio(BigDecimal tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	
	/**
	 * Retorna el valor del atributo Fecha de emision de la Unidad de Inversion
	 * @return
	 */
	public Date getFechaEmisionUI() {
		return fechaEmisionUI;
	}
	/**
	 * Asigna valor al atributo Fecha de emision de la Unidad de Inversion
	 * @param fechaEmisionUI
	 */	
	public void setFechaEmisionUI(Date fechaEmisionUI) {
		this.fechaEmisionUI = fechaEmisionUI;
	}
	
	/**
	 * Retorna el valor del atributo Fecha de adjudicacion de la Unidad de Inversion
	 * @return
	 */
	public Date getFechaAdjudicacionUI() {
		return fechaAdjudicacionUI;
	}
	/**
	 * Asigna valor al atributo Fecha de adjudicacion de la Unidad de Inversion
	 * @param fechaAdjudicacionUI
	 */	
	public void setFechaAdjudicacionUI(Date fechaAdjudicacionUI) {
		this.fechaAdjudicacionUI = fechaAdjudicacionUI;
	}
	
	/**
	 * Retorna el valor del atributo Fecha de liquidacion de la Unidad de Inversion
	 * @return
	 */
	public Date getFechaLiquidaUI() {
		return fechaLiquidaUI;
	}
	/**
	 * Asigna valor al atributo Fecha de liquidacion de la Unidad de Inversion
	 * @param fechaLiquidaUI
	 */	
	public void setFechaLiquidaUI(Date fechaLiquidaUI) {
		this.fechaLiquidaUI = fechaLiquidaUI;
	}
	
	/**
	 * Retorna el valor del atributo Fecha de cierre de la Unidad de Inversion
	 * @return
	 */
	public Date getFechaCierreUI() {
		return fechaCierreUI;
	}
	/**
	 * Asigna valor al atributo Fecha de cierre de la Unidad de Inversion
	 * @param fechaCierreUI
	 */	
	public void setFechaCierreUI(Date fechaCierreUI) {
		this.fechaCierreUI = fechaCierreUI;
	}
	
	/**
	 * Retorna el valor del atributo Indicador para venta a los Empleados
	 * @return
	 */
	public int getIndicaPedidoMonto() {
		return indicaPedidoMonto;
	}
	/**
	 * Asigna valor al atributo Indicador para venta a los Empleados
	 * @param indicaPedidoMonto
	 */	
	public void setIndicaPedidoMonto(int indicaPedidoMonto) {
		this.indicaPedidoMonto = indicaPedidoMonto;
	}
	
	/**
	 * Retorna el valor del atributo Indicador para venta a los Empleados
	 * @return
	 */
	public int getIndicaVtaEmpleados() {
		return indicaVtaEmpleados;
	}
	/**
	 * Asigna valor al atributo Indicador para venta a los Empleados
	 * @param indicaVtaEmpleados
	 */	
	public void setIndicaVtaEmpleados(int indicaVtaEmpleados) {
		this.indicaVtaEmpleados = indicaVtaEmpleados;
	}
	
	/**
	 * Retorna el valor del atributo Indicador se puede pactar recompra de neteo
	 * @return
	 */
	public int getIndicaRecompraNeteo() {
		return indicaRecompraNeteo;
	}
	/**
	 * Asigna valor al atributo Indicador se puede pactar recompra de neteo
	 * @param indicaRecompraNeteo
	 */	
	public void setIndicaRecompraNeteo(int indicaRecompraNeteo) {
		this.indicaRecompraNeteo = indicaRecompraNeteo;
	}
	
	/**
	 * Retorna el valor del atributo Identificador de la moneda
	 * @return
	 */
	public String getIdMoneda() {
		return idMoneda;
	}
	/**
	 * Asigna valor al atributo Identificador de la moneda
	 * @param idMoneda
	 */	
	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}
	
	/**
	 * Retorna el valor del atributo Identificador de la empresa que es emisor
	 * @return
	 */
	public String getIdEmpresaEmisor() {
		return idEmpresaEmisor;
	}
	/**
	 * Asigna valor al atributo Identificador de la empresa que es emisor
	 * @param idEmpresaEmisor
	 */	
	public void setIdEmpresaEmisor(String idEmpresaEmisor) {
		this.idEmpresaEmisor = idEmpresaEmisor;
	}
	
	/**
	 * Retorna el valor del atributo Comentarios
	 * @return
	 */
	public String getComentarios() {
		return comentarios;
	}
	/**
	 * Asigna valor al atributo Comentarios
	 * @param comentarios
	 */	
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	
	/**
	 * Retorna el valor del atributo Monto total de Inventario disponible para la Venta
	 * @return
	 */
	public BigDecimal getTotalInversion() {
		return totalInversion;
	}
	/**
	 * Asigna valor al atributo Monto total de Inventario disponible para la Venta
	 * @param totalInversion
	 */	
	public void setTotalInversion(BigDecimal totalInversion) {
		this.totalInversion = totalInversion;
	}
	

	
	/**
	 * Retorna el valor del atributo Identificador del Status que tiene la Unidad de Inversion
	 * @return
	 */
	public String getIdUIStatus() {
		return idUIStatus;
	}
	/**
	 * Asigna valor al atributo Identificador del Status que tiene la Unidad de Inversion
	 * @param idUIStatus
	 */
	public void setIdUIStatus(String idUIStatus) {
		this.idUIStatus = idUIStatus;
	}
	
	/**
	 * Retorna el valor del atributo Tipo de Instrumento
	 * @return
	 */
	public String getTipoInstrumento() {
		return tipoInstrumento;
	}
	/**
	 * Asigna valor al atributo Tipo de Instrumento
	 * @param tipoInstrumento
	 */
	public void setTipoInstrumento(String tipoInstrumento) {
		this.tipoInstrumento = tipoInstrumento;
	}
	
	/**
	 * Retorna el valor del atributo Monto Minimo de Inversion
	 * @return
	 */		
	public BigDecimal getMontoMinimoInversionSubasta() {
		return montoMinimoInversionSubasta;
	}
	/**
	 * Asigna valor al atributo Monto Minimo de Inversion
	 * @param montoMinimoInversionSubasta
	 */
	public void setMontoMinimoInversionSubasta(BigDecimal montoMinimoInversionSubasta) {
		this.montoMinimoInversionSubasta = montoMinimoInversionSubasta;
	}
	
	/**
	 * Retorna el valor del atributo Multiplo de Inversion
	 * @return
	 */		
	public BigDecimal getMontoMultiplos() {
		return montoMultiplos;
	}
	/**
	 * Asigna valor al atributo Multiplo de Inversion
	 * @param montoMultiplos
	 */
	public void setMontoMultiplos(BigDecimal montoMultiplos) {
		this.montoMultiplos = montoMultiplos;
	}
	
	/**
	 * Construir un DataSet del Framework Mega
	 * @return
	 * @throws Exception
	 */
	public DataSet builDataSet() throws Exception {
		
		DataSet ds = new DataSet();

		int i1 =0;	
		Field [] fields = getClass().getDeclaredFields();		
		for (int i=0; i<fields.length; i++ ) {
			if (fields[i].getName() == null) {
				continue;
			}
			if (fields[i].getType() == String.class) {
				ds.append(fields[i].getName(), java.sql.Types.VARCHAR);
				++i1;
			}
			else if (fields[i].getType() == Date.class) {
				ds.append(fields[i].getName(), java.sql.Types.VARCHAR);
				++i1;
			}
			else if (fields[i].getType() == BigDecimal.class) {
				ds.append(fields[i].getName(), java.sql.Types.DECIMAL);
				++i1;
			}
			else if (fields[i].getType().getName().equals("int")) { 
				ds.append(fields[i].getName(), java.sql.Types.INTEGER);
				++i1;
			}
			else if (fields[i].getType().getName().equals("long")) { 
				ds.append(fields[i].getName(), java.sql.Types.INTEGER);
				++i1;
			}
			else if (fields[i].getType().getName().equals("double")) { 
				ds.append(fields[i].getName(), java.sql.Types.DECIMAL);
				++i1;
			}
		}
		return ds;

	}
	
	/**Obtiene la tasa pool de la uniad de inversión*/
	public BigDecimal getTasaPool() {
		return tasaPool;
	}
	
	/**Establece la tasa pool de la uniad de inversión*/
	public void setTasaPool(BigDecimal tasaPool) {
		this.tasaPool = tasaPool;
	}
	
	/**Obtiene el porcentaje de rendimiento de la uniad de inversión. Aplica solo a instrumentos de tipo inventario*/
	public BigDecimal getPctRendimiento() {
		return pctRendimiento;
	}
	
	/**Establece el porcentaje de rendimiento de la uniad de inversión. Aplica solo a instrumentos de tipo inventario*/	
	public void setPctRendimiento(BigDecimal pctRendimiento) {
		this.pctRendimiento = pctRendimiento;
	}
	
	/**Obtiene el tipo de mercado de la uniad de inversión. */	
	public String getTipoMercado() {
		return tipoMercado;
	}
	
	/**Establece el tipo de mercado de la uniad de inversión. */	
	public void setTipoMercado(String tipoMercado) {
		this.tipoMercado = tipoMercado;
	}
	
	/**
	 * Retorna el valor del atributo Indicador de Precio Sucio
	 * @return indicadorPrecioSucio
	 */	
	public int getIndicadorPrecioSucio() {
		return indicadorPrecioSucio;
	}
	/**
	 * Asigna valor al atributo Indicador de Precio Sucio
	 * @param indicadorPrecioSucio
	 */
	public void setIndicadorPrecioSucio(int indicadorPrecioSucio) {
		this.indicadorPrecioSucio = indicadorPrecioSucio;
	}
	
	/**Asigna la hora de ejecución del primer intento para el proceso de liquidación automático
	 * @param fechaHora fecha y hora de ejecución*/
	public void setFechaLiquidaUIHora1(Date fechaHora){
		this.fechaLiquidaUIHora1 = fechaHora;
	}
	
	/**Recupera la hora de ejecución del primer intento para el proceso de liquidación automático*/
	public Date getFechaLiquidaUIHora1(){
		return this.fechaLiquidaUIHora1;
	}
	
	/**Asigna la hora de ejecución del segundo intento para el proceso de liquidación automático
	 * @param fechaHora fecha y hora de ejecución*/
	public void setFechaLiquidaUIHora2(Date fechaHora){
		this.fechaLiquidaUIHora2 = fechaHora;
	}
	
	/**Recupera la hora de ejecución del segundo intento para el proceso de liquidación automático*/
	public Date getFechaLiquidaUIHora2(){
		return this.fechaLiquidaUIHora2;
	}	
	
	/**Valida que los intentos de liquidación sean un día antes que la fecha de liquidación
	 * @return verdadero en caso de que las fecha de liquidacion de hora 1 y hora 2 sean un día mes de la fecha de liquidación*/
	public boolean validarFechasLiquidacion(){
		boolean resultado = false;
		Calendar calendario1 = Calendar.getInstance();
		Calendar calendario2 = Calendar.getInstance();
		Calendar calendario3 = Calendar.getInstance();
		calendario1.setTime(fechaLiquidaUIHora1);
		calendario1.add(Calendar.DATE, 1);
		calendario2.setTime(fechaLiquidaUIHora2);
		calendario2.add(Calendar.DATE, 1);
		//Fecha de liquidación
		calendario3.setTime(fechaLiquidaUI);
		
		//Compara años
		if (calendario3.get(Calendar.YEAR) == calendario1.get(Calendar.YEAR) && calendario3.get(Calendar.YEAR) == calendario3.get(Calendar.YEAR)){
			//Compara meses
			if (calendario3.get(Calendar.MONTH) == calendario1.get(Calendar.MONTH) && calendario3.get(Calendar.MONTH) == calendario3.get(Calendar.MONTH)){
				if (calendario3.get(Calendar.DATE) == calendario1.get(Calendar.DATE) && calendario3.get(Calendar.DATE) == calendario3.get(Calendar.DATE)){
					resultado = true;
				}
			}
		}
		return resultado;
	}
	
	/**Seteal el valor de pago al Banco Central de Venezuela*/
	public void setInPagoBCV(String pago){
		this.inPagoBCV=pago;
	}
	
	/**Recupera el valor de pago al Banco Central de Venezuela*/
	public String getInPagoBCV(){
		return this.inPagoBCV;
	}
	
	public int getPermiteCancelacion() {
		return permiteCancelacion;
	}
	public void setPermiteCancelacion(int permiteCancelacion) {
		this.permiteCancelacion = permiteCancelacion;
	}
	public int getPermiteAltoValor() {
		return permiteAltoValor;
	}
	public void setPermiteAltoValor(int permiteAltoValor) {
		this.permiteAltoValor = permiteAltoValor;
	}
	public BigDecimal getMontoMultiplosEfectivo() {
		return montoMultiplosEfectivo;
	}
	public void setMontoMultiplosEfectivo(BigDecimal montoMultiplosEfectivo) {
		this.montoMultiplosEfectivo = montoMultiplosEfectivo;
	}
	public BigDecimal getMontoMaximoInversionSubasta() {
		return montoMaximoInversionSubasta;
	}
	public void setMontoMaximoInversionSubasta(BigDecimal montoMaximoInversionSubasta) {
		this.montoMaximoInversionSubasta = montoMaximoInversionSubasta;
	}
	public int getPeriodicidadVenta() {
		return periodicidadVenta;
	}
	public void setPeriodicidadVenta(int periodicidadVenta) {
		this.periodicidadVenta = periodicidadVenta;
	}
	public BigDecimal getTasaCambioOferta() {
		return tasaCambioOferta;
	}
	public void setTasaCambioOferta(BigDecimal tasaCambioOferta) {
		this.tasaCambioOferta = tasaCambioOferta;
	}	
	
	public BigDecimal getComisionIGTF() {
		return comisionIGTF;
	}
	
	public void setComisionIGTF(BigDecimal comisionIGTF) {
		this.comisionIGTF = comisionIGTF;
	}
}