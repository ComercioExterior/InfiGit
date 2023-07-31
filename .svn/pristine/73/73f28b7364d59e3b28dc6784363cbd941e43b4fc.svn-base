package com.bdv.infi_toma_orden.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenDocumento;
import com.bdv.infi.data.OrdenRequisito;
import com.bdv.infi_toma_orden.data.CampoDinamico;
import com.bdv.infi_toma_orden.data.OrdenTitulo;

/**
 * TomaOrdenSimulada 
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class TomaOrdenSimulada implements Serializable {

	private static final long serialVersionUID = -6739432179139890520L;
	/** Identificador de la Orden */
	private long idOrden = 0;
	/** id de la unidad de inversión */
	private long idUnidadInversion  = 0;
	/** N&uacute;mero de Oficina o Sucursal	 */
	private String numeroOficina = null;
	/** Fecha en que se tomo la orden */
	private Date fechaOrden;	
	/** Fecha valor de la orden */
	private Date fechaValor;	
	/** Fecha valor de la orden Recompra */
	private String fechaValorRecompra;
	/** Monto pedido en la toma de una orden */
	private BigDecimal montoPedido = new BigDecimal(0);
	/** Monto pedido en efectivo en la toma de una orden */
	private BigDecimal montoPedidoEfectivo = new BigDecimal(0);
	/** Cantidad de UI ordenada */
	private long cantidadOrdenada = 0;
	/** Monto de la Inversion despues de aplicar las reglas asociadas*/
	private BigDecimal montoInversion = new BigDecimal(0);
	/** Monto de la Inversion en efectivo despues de aplicar las reglas asociadas*/
	private BigDecimal montoInversionEfectivo = new BigDecimal(0);
	/** Monto total de la orden incluyendo comisiones */
	private BigDecimal montoTotal = new BigDecimal(0);	
	/** Monto de financiamiento */
	private BigDecimal montoFinanciado = new BigDecimal(0);
	/** Monto total a pagar en la orden sin financiamiento */
	private BigDecimal montoTotalPagar = new BigDecimal(0);
	/** Monto total de los comisiones **/
	private BigDecimal montoComisiones = new BigDecimal(0);	
	/** Monto comision en efectivo **/
	private BigDecimal montoComisionesEfectivo = new BigDecimal(0);	
	/**Tasa de cambio usada  */
	private BigDecimal tasaCambio = new BigDecimal(0);	
	/** Precio ofrecido para la Compra **/
	private BigDecimal precioCompra = new BigDecimal(0);	
	/** Id del blotter emisor */
	private String idBlotter;
	/** Id del cliente **/
	private long idCliente = 0;
	/** Id de la empresa **/
	private String idEmpresa = "";
	//NM29643
	/** Contraparte de la orden **/
	private String contraparte = "";
	//NM29643
	/** NroRetencionSolicitudClavenet **/
	private String nroRetencion = "";
	/** Id de la transacción **/
	private String idTransaccion = "";	
	/** Id del Status de la TomaOrdenSimulada */
	private String idStatusOrden = "";			
	/** Numero de la Cuenta del Cliente */
	private String numeroCuentaCliente = "";
	/** Numero de la Cuenta Nacional en Dolares del Cliente */
	private String numeroCuentaNacDolaresCliente = "";
	/** Porcentaje de financimiento */
	private BigDecimal porcentajeFinanciado = new BigDecimal(0);
	/** Indica si la orden es indicaFinanciada */
	private boolean indicaFinanciada = false;
	/** Indica si la unidad de inversion es inventario */
	private boolean indicaInventario = false;
	/** Títulos asociados a la orden */
	private ArrayList <OrdenTitulo> listaOrdenTitulo = new ArrayList <OrdenTitulo>();
	/** Lista de operaciones financieras aplicadas a una orden */
	private ArrayList<OrdenOperacion> listaOperaciones = new ArrayList<OrdenOperacion>();	
	/** Lista de Mensaje a presentar al cliente */
	private ArrayList listaMensajes = new ArrayList();
	/** Lista de campos dinámicos que deben ser guardados */
	private ArrayList<CampoDinamico> listaCamposDinamicos = new ArrayList<CampoDinamico>();
	/** Lista de Documentos asociados a la Orden */
	private ArrayList<OrdenDocumento> listaDocumentos = new ArrayList<OrdenDocumento>();
	/** Lista de Data Extendida	 */
	private ArrayList<OrdenDataExt> ordenDataExt = new ArrayList<OrdenDataExt>();	
	/** IP del Terminal que toma la orden */
	private String ipTerminal = "";
	/** Veh&iacute;culo Tomador de la Orden */
	private String vehiculoTomador = "";
	/** Veh&iacute;culo Colocador de la Orden */
	private String vehiculoColocador = "";
	/** Veh&iacute;culo para Recompra de la Orden */
	private String vehiculoRecompra = "";
	/** Usuario conectado que tomo la Orden */
	private String nombreUsuario = "";
	/** Indicador de Toma de Orden Interna de BDV (Cartera Propia)  */
	private Integer internoBDV;
	/** Monto total de los intereses caidos**/
	private BigDecimal montoInteresCaidos = new BigDecimal(0);
	
	/** Monto total de los intereses caidos Orden De Recompra**/
	private BigDecimal montoInteresCaidosRecompra = new BigDecimal(0);
	/** Porcentaje de recompra Orden De Recompra **/
	private BigDecimal porsentajeRecompra = new BigDecimal(0);
	
	private BigDecimal comisionEmisor = new BigDecimal(0);
	
	private BigDecimal tasaPool = new BigDecimal(0);
	
	private BigDecimal gananciaRed = new BigDecimal(0);
	
	private BigDecimal comisionOficina = new BigDecimal(0);
	
	private BigDecimal comisionOperacion = new BigDecimal(0);
	
	/** Instrucci&oacute;n de pago para la recompra de t&iacute;tulos */
	private ArrayList<CuentaCliente> instruccionPagoRecompra = new ArrayList<CuentaCliente>();
	
	//Nm29643 - INFI_TTS_443 08/04/2014: Se agrega objeto con informacion de la instruccion de pago a cta nac en dolares (SICAD II)
	private CuentaCliente instPagoCtaNacDolares = null;
	
	/** Actividad Economica de la orden - SITME */
	private String actividadEconomica;
	/** Sector Productivo de la orden - SITME*/
	private String sectorProductivo;
	/** Concepto de la orden - SITME*/
	private String concepto;
	/** ID de la Instruccion de pago - SITME */
	private String IdInstruccion;
	
	/**Id del instrumento financiero*/
	private String instrumentoId;
	
	/**Requisitos del Cliente en la Orden*/
	private ArrayList<OrdenRequisito> ordenRequisitos = new ArrayList<OrdenRequisito>();	
	
	/**Tipo de Producto */
	private String tipoProductoId;
	
	/**
	 * Observaciones de la orden
	 */
	private String observaciones;
	
	private String monedaId;
	
	/**
	 * Constructor de la Clase usado para rellenalo
	 * @param objValor
	 * @throws Exception
	 */
	public TomaOrdenSimulada() throws Exception {
		
	}

	/**
	 * Constructor de la Clase usado para rellenalo
	 * @param objValor
	 * @throws Exception
	 */
	public TomaOrdenSimulada(Object [] objValor) throws Exception {
		
		Long lAux = (Long)objValor[0];
		this.idOrden = lAux.longValue();
		lAux = (Long)objValor[1];
		this.idUnidadInversion = lAux.longValue();
		this.fechaOrden = (Date)objValor[2];
		this.fechaValor = (Date)objValor[3];
		this.montoPedido = (BigDecimal)objValor[4];	
		this.cantidadOrdenada = (Long)objValor[5];
		this.montoInversion = (BigDecimal)objValor[6];
		this.montoTotal = (BigDecimal)objValor[7];	
		this.montoFinanciado = (BigDecimal)objValor[8];	
		this.montoInteresCaidos = (BigDecimal)objValor[9];
		this.montoComisiones = (BigDecimal)objValor[10];
		this.tasaCambio = (BigDecimal)objValor[11];
		this.precioCompra = (BigDecimal)objValor[12];
		this.idBlotter = (String)objValor[13];
		lAux = (Long)objValor[14];
		this.idCliente = lAux.longValue();
		this.idEmpresa = (String)objValor[15];
		this.idTransaccion = (String)objValor[16];
		this.idStatusOrden = (String)objValor[17];
		this.numeroCuentaCliente = (String)objValor[18];
		this.porcentajeFinanciado = (BigDecimal) objValor[19];		
		this.indicaFinanciada = (Boolean)objValor[20];
		this.indicaInventario = (Boolean)objValor[21];
		listaOrdenTitulo = new ArrayList<OrdenTitulo> ();
		listaOperaciones = new ArrayList<OrdenOperacion>();
		listaCamposDinamicos = new ArrayList<CampoDinamico>();
		listaDocumentos = new ArrayList<OrdenDocumento>();
		
		this.comisionEmisor = new BigDecimal(0);
		this.tasaPool =  new BigDecimal(0);
		this.comisionOficina =  new BigDecimal(0);
		this.comisionOperacion =  new BigDecimal(0);
		this.gananciaRed =  new BigDecimal(0);
		
		this.ipTerminal = (String)objValor[22];
		this.vehiculoTomador = (String)objValor[23];
		this.vehiculoColocador = (String)objValor[24];
		this.vehiculoRecompra = (String)objValor[25];
		this.nombreUsuario = (String)objValor[26];
		
		this.internoBDV = (Integer)objValor[27];
		
		this.actividadEconomica = (String)objValor[28];
		this.sectorProductivo = (String)objValor[29];
		this.concepto = (String)objValor[30];
		this.IdInstruccion = (String)objValor[31];
		this.fechaValorRecompra=(String) objValor[32];
		this.instrumentoId = (String) objValor[33];
		setNumeroCuentaNacDolaresCliente((String)objValor[34]);
	}
	
	public ArrayList<CuentaCliente> getInstruccionPagoRecompra() {
		return instruccionPagoRecompra;
	}
	public void setInstruccionPagoRecompra(ArrayList<CuentaCliente> instruccionPagoRecompra) {
		this.instruccionPagoRecompra = instruccionPagoRecompra;
	}
	
	public void agregarInstruccionPagoRecompra(CuentaCliente cuentaCliente){
		this.instruccionPagoRecompra.add(cuentaCliente);
	}

	public BigDecimal getComisionEmisor() {
		return comisionEmisor;
	}

	public void setComisionEmisor(BigDecimal comisionEmisor) {
		this.comisionEmisor = comisionEmisor;
	}

	public BigDecimal getComisionOficina() {
		return comisionOficina;
	}

	public void setComisionOficina(BigDecimal comisionOficina) {
		this.comisionOficina = comisionOficina;
	}

	public BigDecimal getComisionOperacion() {
		return comisionOperacion;
	}

	public void setComisionOperacion(BigDecimal comisionOperacion) {
		this.comisionOperacion = comisionOperacion;
	}

	public BigDecimal getGananciaRed() {
		return gananciaRed;
	}

	public void setGananciaRed(BigDecimal gananciaRed) {
		this.gananciaRed = gananciaRed;
	}

	public BigDecimal getTasaPool() {
		return tasaPool;
	}

	public void setTasaPool(BigDecimal tasaPool) {
		this.tasaPool = tasaPool;
	}
	
	/**
	 * Devuelve el valor del Identificador de la TomaOrdenSimulada 
	 * @return idOrden
	 */
	public long getIdOrden() {
		return idOrden;
	}
	/**
	 * Asigna valor al Identificador de la TomaOrdenSimulada 
	 * @param idOrden
	 */
	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}
	
	/**
	 * Devuelve el valor del Identificador de la Unidad Inversion
	 * @return idUnidadInversion
	 */
	public long getIdUnidadInversion() {
		return idUnidadInversion;
	}
	/**
	 * Asigna valor al Identificador de la Unidad de Inversion
	 * @param idUnidadInversion
	 */
	public void setIdUnidadInversion(long idUnidadInversion) {
		this.idUnidadInversion = idUnidadInversion;
	}
	
	/**
	 * Devuelve el valor de al Fecha en la que se efectuó la orden 
	 * @return fechaOrden
	 */
	public Date getFechaOrden() {
		return fechaOrden;
	}
	/**
	 * Asigna valor a la Fecha en la que se efectuó la orden 
	 * @param fechaOrden
	 */	
	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}
	
	/**
	 * Devuelve el valor de la Fecha Valor
	 * @return fechaValor
	 */
	public Date getFechaValor() {
		return fechaValor;
	}
	/**
	 * Asigna valor a la Fecha Valor 
	 * @param fechaValor
	 */
	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}
	
	/**
	 * Retorna el valor del Cantidad de UI ordenada
	 * @return cantidadOrdenada
	 */
	public long getCantidadOrdenada() {
		return cantidadOrdenada;
	}
	/**
	 * Asigna valor al Cantidad de UI ordenada
	 * @param montoInvertido
	 */	
	public void setCantidadOrdenada(long cantidadOrdenada) {
		this.cantidadOrdenada = cantidadOrdenada;
	}
	
	/**
	 * Devuelve el valor del Monto de la Inversion en la toma de una orden 
	 * @return montoInversion
	 */
	public BigDecimal getMontoInversion() {
		return montoInversion;
	}
	/**
	 * Asigna valor al Monto de la Inversion en la toma de una orden
	 * @param montoPedido
	 */
	public void setMontoInversion(BigDecimal montoInversion) {
		this.montoInversion = montoInversion;
	}
		
	/**
	 * Devuelve el valor del Monto pedido en la toma de una orden 
	 * @return montoPedido
	 */
	public BigDecimal getMontoPedido() {
		return montoPedido;
	}
	/**
	 * Asigna valor al Monto pedido en la toma de una orden
	 * @param montoPedido
	 */
	public void setMontoPedido(BigDecimal montoPedido) {
		this.montoPedido = montoPedido;
	}
	
	/**
	 * Devuelve el valor del Monto Total de la TomaOrdenSimulada 
	 * @return montoAdjudicado
	 */
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}
	/**
	 * Asigna valor al Monto Total de la TomaOrdenSimulada
	 * @param montoAdjudicado
	 */
	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}
		
	/**
	 * Devuelve el valor del Monto Financiado
	 * @return montoFinanciado
	 */
	public BigDecimal getMontoFinanciado() {
		return montoFinanciado;
	}
	/**
	 * Asigna valor al Monto Financiado
	 * @param montoFinanciado
	 */
	public void setMontoFinanciado(BigDecimal montoFinanciado) {
		this.montoFinanciado = montoFinanciado;
	}
	
	/**
	 * Devuelve el valor del Monto de Intereses Caidos 
	 * @return montoInteresCaidos
	 */
	public BigDecimal getMontoInteresCaidos() {
		return montoInteresCaidos;
	}
	/**
	 * Asigna valor al Monto de Intereses Caidos 
	 * @param montoInteresCaidos
	 */
	public void setMontoInteresCaidos(BigDecimal montoInteresCaidos) {
		this.montoInteresCaidos = montoInteresCaidos;
	}

	/**
	 * Devuelve el valor del Monto generado por la Comisiones
	 * @return montoComisiones
	 */
	public BigDecimal getMontoComisiones() {
		return montoComisiones;
	}
	/**
	 * Asigna valor al Monto generado por la Comisiones
	 * @param montoComisiones
	 */
	public void setMontoComisiones(BigDecimal montoComisiones) {
		this.montoComisiones = montoComisiones;
	}
		
	/**
	 * Devuelve el valor de la Tasa de Cambio utilizada 
	 * @return tasaCambio
	 */
	public BigDecimal getTasaCambio() {
		return tasaCambio;
	}

	/**
	 * Asigna valor a  la Tasa de Cambio utilizada 
	 * @param tasaCambio
	 */
	public void setTasaCambio(BigDecimal tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	
	/**
	 * Devuelve el valor del Precio ofertado por el cliente 
	 * @return precioCompra
	 */
	public BigDecimal getPrecioCompra() {
		return precioCompra;
	}
	/**
	 * Asigna valor al Precio ofertado por el cliente 
	 * @param precioCompra
	 */
	public void setPrecioCompra(BigDecimal precioCompra) {
		this.precioCompra = precioCompra;
	}

	
	/**
	 * Devuelve el valor del Identificador del Blotter
	 * @return idBlotter
	 */
	public String getIdBlotter() {
		return idBlotter;
	}
	/**
	 * Asigna valor al Identificador del Blotter
	 * @param idBlotter
	 */
	public void setIdBlotter(String idBloter) {
		this.idBlotter = idBloter;
	}
	
	/**
	 * Devuelve el valor del Identificador del Cliente
	 * @return idDepositario
	 */
	public long getIdCliente() {
		return idCliente;
	}
	/**
	 * Asigna valor al Identificador del Cliente
	 * @param idCliente
	 */
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}
		
	/**
	 * Devuelve el valor del Identificador de la Empresa
	 * @return idEmpresa
	 */
	public String getIdEmpresa() {
		return idEmpresa;
	}
	/**
	 * Asigna valor al Identificador de la Empresa
	 * @param idEmpresa
	 */
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	//NM29643
	/**
	 * Devuelve el valor de la Contraparte
	 * @return contraparte
	 */
	public String getContraparte() {
		return contraparte;
	}
	//NM29643
	/**
	 * Asigna valor a la contraparte
	 * @param contraparte
	 */
	public void setContraparte(String contraparte) {
		this.contraparte = contraparte;
	}
	
	//NM29643
	/**
	 * Devuelve el valor del Nro Retencion
	 * @return nroRetencion
	 */
	public String getNroRetencion() {
		return nroRetencion;
	}
	//NM29643
	/**
	 * Asigna valor al Nro Retencion
	 * @param contraparte
	 */
	public void setNroRetencion(String nroRetencion) {
		this.nroRetencion = nroRetencion;
	}
	
	/**
	 * Devuelve el valor del Identificador de la Transaccion
	 * @return idTransaccion
	 */
	public String getIdTransaccion() {
		return idTransaccion;
	}
	/**
	 * Asigna valor al Identificador de la Transaccion
	 * @param idTransaccion
	 */
	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	
	/**
	 * Devuelve el valor del Identificador del Status de la TomaOrdenSimulada
	 * @return idStatusOrden
	 */
	public String getIdStatusOrden() {
		return idStatusOrden;
	}
	/**
	 * Asigna valor al Identificador del Status de la TomaOrdenSimulada
	 * @param idSistema
	 */
	public void setIdStatusOrden(String idStatusOrden) {
		this.idStatusOrden = idStatusOrden;
	}
	
	/**
	 * Retorna el valor del Numero de la Cuenta del Cliente
	 * @return numeroCuentaCliente
	 */
	public String getNumeroCuentaCliente() {
		return numeroCuentaCliente;
	}
	/**
	 * Asigna valor al Numero de la Cuenta del Cliente
	 * @param numeroCuentaCliente
	 */	
	public void setNumeroCuentaCliente(String numeroCuentaCliente) {
		this.numeroCuentaCliente = numeroCuentaCliente;
	}
	
	/**
	 * Retorna el valor del Porcentaje de financimiento
	 * @return porcentajeFinanciado
	 */
	public BigDecimal getPorcentajeFinanciado() {
		return porcentajeFinanciado;
	}
	/**
	 * Asigna valor al Porcentaje de financimiento
	 * @param porcentajeFinanciado
	 */
	public void setPorcentajeFinanciado(BigDecimal porcentajeFinanciado) {
		this.porcentajeFinanciado = porcentajeFinanciado;
	}
	
	/**
	 * Retorna el valor del Indicador de Orden Financiada
	 * @return indicaFinanciada
	 */
	public boolean isIndicaFinanciada() {
		return indicaFinanciada;
	}
	/**
	 * Asigna valor al Indicador de Orden Financiada
	 * @param indicaFinanciada
	 */	
	public void setFinanciada(boolean indicaFinanciada) {
		this.indicaFinanciada = indicaFinanciada;
	}
	
	/**
	 * Retorna el valor del Indicador de UI de Inventario
	 * @return indicaInventario
	 */
	public boolean isIndicaInventario() {
		return indicaInventario;
	}
	/**
	 * Asigna valor al Indicador de UI de Inventario
	 * @param indicaInventario
	 */	
	public void setInventario(boolean indicaInventario) {
		this.indicaInventario = indicaInventario;
	}
	
	/**
	 * Retorna la lista de Titulos asociadas a la orden
	 * @return listaOrdenTitulo
	 */
	public ArrayList<OrdenTitulo> getListaOrdenTitulo() {
		return listaOrdenTitulo;
	}
	/**
	 * Asigna valores la lista de Titulos asociadas a la orden
	 * @param listaOrdenTitulo
	 */	
	public void setListaOrdenTitulo(ArrayList<OrdenTitulo> listaOrdenTitulo) {
		this.listaOrdenTitulo = listaOrdenTitulo;
	}
	
	/**Asigna los valores de campos dinámicos a la orden
	* @param listaOrdenTitulo
	*/
	public void setListaCamposDinamicos(ArrayList<CampoDinamico> listaCamposDinamicos) {
		this.listaCamposDinamicos = listaCamposDinamicos;
	}
	
	/**
	 * Retorna la lista de los campos dinámicos
	 * @return listaCamposDinamicos
	 */
	public ArrayList getListaCamposDinamicos() {
		return listaCamposDinamicos;
	}	

	/**
	 * Retorna la lista de Operaciones generadas para la orden
	 * @return listaOperaciones
	 */
	public ArrayList<OrdenOperacion> getListaOperaciones() {
		return listaOperaciones;
	}
	/**
	 * Añade un bean OperacionRespuesta a la lista
	 * @param beanOperacion : bean OperacionRespuesta
	 */	
	public void addListaOperaciones(OrdenOperacion beanOperacion) {
		this.listaOperaciones.add(beanOperacion);
	}

	/**
	 * Retorna la lista de Mensajes generados por el Simulador a presentar al Cliente
	 * @return listaMensajes
	 */
	public ArrayList getListaMensajes() {
		return listaMensajes;
	}
	/**
	 * Añade un mensaje a la lista de Mensajes generados por el Simulador a presentar al Cliente
	 * @param listaMensajes
	 */	
	public void setListaMensajes(ArrayList listaMensajes) {
		this.listaMensajes = listaMensajes;
	}

	/**
	 * Retorna el Ip del Terminal que Toma la Orden
	 * @return ipTerminal
	 */
	public String getIpTerminal() {
		return ipTerminal;
	}

	/**
	 * Asigna el ip del terminal que toma la orden
	 * @param ipTerminal
	 */
	public void setIpTerminal(String ipTerminal) {
		this.ipTerminal = ipTerminal;
	}

	/**
	 * Retorna el nombre de usuario conectado al sistema en el momento de la toma de orden
	 * @return
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Asigna el nombre del usuario conectado que tom&oacute; la orden
	 * @param nombreUsuario
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Retorna el Veh&iacute;culo Colocador de la Orden
	 * @return
	 */
	public String getVehiculoColocador() {
		return vehiculoColocador;
	}

	/**
	 * Asigna el Veh&iacute;culo Colocador de la orden
	 * @param vehiculoColocador
	 */
	public void setVehiculoColocador(String vehiculoColocador) {
		this.vehiculoColocador = vehiculoColocador;
	}

	/**
	 * Retorna el Veh&iacute;culo para Recompra de la Orden
	 * @return
	 */
	public String getVehiculoRecompra() {
		return vehiculoRecompra;
	}

	/**
	 * Asigna el Veh&iacute;culo para Recompra de la Orden
	 * @param vehiculoRecompra
	 */
	public void setVehiculoRecompra(String vehiculoRecompra) {
		this.vehiculoRecompra = vehiculoRecompra;
	}

	/**
	 * Retorna el Veh&iacute;culo tomador de la Orden
	 * @return
	 */
	public String getVehiculoTomador() {
		return vehiculoTomador;
	}

	/**
	 * Asigna el Veh&iacute;culo tomador de la orden
	 * @param vehiculoTomador
	 */
	public void setVehiculoTomador(String vehiculoTomador) {
		this.vehiculoTomador = vehiculoTomador;
	}

	/**
	 * Retorna la lista de Documentos asociados a la orden
	 * @return listaDocumentos
	 */
	public ArrayList<OrdenDocumento> getListaDocumentos() {
		return listaDocumentos;
	}

	/**
	 * Añade un objeto de tipo OrdenDocumento a la lista de documentos asociados a la orden
	 * @param ordenDocumento
	 */
	public void addListaDocumentos(OrdenDocumento ordenDocumento) {
		this.listaDocumentos.add(ordenDocumento);
	}

	public Integer getInternoBDV(){
		return internoBDV;
	}

	public void setInternoBDV(Integer internoBDV) {
		this.internoBDV = internoBDV;
	}
	
	/**
	 * Agrega un T&iacute;tulo a la lista de t&iacute;tulos de la orden
	 * @param ordenTitulo
	 * @return
	 */
	public boolean agregarOrdenTitulo(OrdenTitulo ordenTitulo){
		return this.listaOrdenTitulo.add(ordenTitulo);
	}
	
	/**
	 * Retorna la lista de data extendida de la orden
	 * @return ArrayList ordenDataExt
	 */
	public ArrayList<OrdenDataExt> getOrdenDataExt()
	{
		return ordenDataExt;
	}

	/**
	 * Verifica si la lista de data extendida de la orden es vacia
	 * @return true si la lista es vacia, false en caso contrario
	 */
	public boolean isOrdenDataExtVacio(){
		return ordenDataExt.isEmpty();
	}
	
	/**
	 * Agrega un objeto de tipo data extendida a la lista de la orden
	 * @param ordenDataExt
	 * @return true si es agregado, false en caso contrario
	 */
	public boolean agregarOrdenDataExt(OrdenDataExt ordenDataExt){
		return this.ordenDataExt.add(ordenDataExt);
	}
	
	/**
	 * Agrega la lista de data extendida a la orden
	 * @param ordenDataExt
	 */
	public void setOrdenDataExt(ArrayList ordenDataExt) {
		this.ordenDataExt = ordenDataExt;
	}

	/**
	 * Asigna una lista de Documentos a la orden
	 * @param listaDocumentos
	 */
	public void setListaDocumentos(ArrayList<OrdenDocumento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	/**
	 * Obtiene el n&uacute;mero de oficina a la orden
	 * @return
	 */
	public String getNumeroOficina() {
		return numeroOficina;
	}

	/**
	 * Asigna el n&uacute;mero de oficina a la orden
	 * @param numeroOficina
	 */
	public void setNumeroOficina(String numeroOficina) {
		this.numeroOficina = numeroOficina;
	}
	
	
	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}
	
	public String getSectorProductivo() {
		return sectorProductivo;
	}

	public void setSectorProductivo(String sectorProductivo) {
		this.sectorProductivo = sectorProductivo;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	public String getFechaValorRecompra() {
		return fechaValorRecompra;
	}

	public void setFechaValorRecompra(String fechaValorRecompra) {
		this.fechaValorRecompra = fechaValorRecompra;
	}
	
	public String getIdInstruccion() {
		return IdInstruccion;
	}

	public void setIdInstruccion(String idInstruccion) {
		IdInstruccion = idInstruccion;
	}
	
	public BigDecimal getMontoTotalPagar() {
		return montoTotalPagar;
	}

	public void setMontoTotalPagar(BigDecimal montoTotalPagar) {
		this.montoTotalPagar = montoTotalPagar;
	}
	
	public BigDecimal getMontoInteresCaidosRecompra() {
		return montoInteresCaidosRecompra;
	}

	public void setMontoInteresCaidosRecompra(BigDecimal montoInteresCaidosRecompra) {
		this.montoInteresCaidosRecompra = montoInteresCaidosRecompra;
	}

	public BigDecimal getPorsentajeRecompra() {
		return porsentajeRecompra;
	}

	public void setPorsentajeRecompra(BigDecimal porsentajeRecompra) {
		this.porsentajeRecompra = porsentajeRecompra;
	}

	public String getInstrumentoId() {
		return instrumentoId;
	}	
	
	/**
	 * Retorna la lista de requisitos en la orden
	 * @return ArrayList ordenRequisitos
	 */
	public ArrayList<OrdenRequisito> getOrdenRequisitos()
	{
		return ordenRequisitos;
	}
	
	/**
	 * Verifica si la lista de requisitos de la orden esta vacia
	 * @return true si la lista es vacia, false en caso contrario
	 */
	public boolean isOrdenRequisitosVacio(){
		return ordenRequisitos.isEmpty();
	}
	
	/**
	 * Agrega un objeto de tipo OrdenRequisito a la lista de la orden
	 * @param ordenRequisitos
	 * @return true si es agregado, false en caso contrario
	 */
	public boolean agregarOrdenRequisito(OrdenRequisito ordenRequisito){
		return this.ordenRequisitos.add(ordenRequisito);
	}
	
	/**
	 * Agrega la lista de requisitos a la orden
	 * @param ordenRequisitos
	 */
	public void setOrdenRequisitos(ArrayList<OrdenRequisito> ordenRequisitos) {
		this.ordenRequisitos = ordenRequisitos;
	}

	
	/**
	 * Obtiene el tipo de producto en la Orden simulada
	 * @return
	 */
	public String getTipoProductoId() {
		return tipoProductoId;
	}

	/**
	 * Asifna el tipo de producto a la orden
	 * @param tipoProductoId
	 */
	public void setTipoProductoId(String tipoProductoId) {
		this.tipoProductoId = tipoProductoId;
	}
	
	/**
	 * Obtiene las observaciones del objeto TomaOrdenSimulada
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Coloca las observaciones en el objeto TomaOrdenSimulada
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getMonedaId() {
		return monedaId;
	}

	public void setMonedaId(String monedaId) {
		this.monedaId = monedaId;
	}

	public String getNumeroCuentaNacDolaresCliente() {
		return numeroCuentaNacDolaresCliente;
	}
	
	public void setNumeroCuentaNacDolaresCliente(String numeroCuentaNacDolaresCliente) {
		this.numeroCuentaNacDolaresCliente = numeroCuentaNacDolaresCliente;
	}

	public CuentaCliente getInstPagoCtaNacDolares() {
		return instPagoCtaNacDolares;
	}

	public void setInstPagoCtaNacDolares(CuentaCliente instPagoCtaNacDolares) {
		this.instPagoCtaNacDolares = instPagoCtaNacDolares;
	}

	public BigDecimal getMontoPedidoEfectivo() {
		return montoPedidoEfectivo;
	}

	public void setMontoPedidoEfectivo(BigDecimal montoPedidoEfectivo) {
		this.montoPedidoEfectivo = montoPedidoEfectivo;
	}

	public BigDecimal getMontoInversionEfectivo() {
		return montoInversionEfectivo;
	}

	public void setMontoInversionEfectivo(BigDecimal montoInversionEfectivo) {
		this.montoInversionEfectivo = montoInversionEfectivo;
	}

	public BigDecimal getMontoComisionesEfectivo() {
		return montoComisionesEfectivo;
	}

	public void setMontoComisionesEfectivo(BigDecimal montoComisionesEfectivo) {
		this.montoComisionesEfectivo = montoComisionesEfectivo;
	}
}
