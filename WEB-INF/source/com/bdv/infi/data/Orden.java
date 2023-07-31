package com.bdv.infi.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.bancovenezuela.comun.data.OrdenOperacion;

/**
 * Clase usada para el manejo de las ordenes que se generan en una
 * transacci&oacute;n
 */
public class Orden implements Serializable,Cloneable {

	
	private String clienteRifCed=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int operaciones = 0;

	private boolean comisionCero=false;
	/** Contructor de la clase */
	public Orden() {
		camposDinamicos = new ArrayList();
		documentos = new ArrayList();
		ordenDataExt = new ArrayList();
		ordenTitulo = new ArrayList();
		formaPago = new ArrayList();
		operacion = new ArrayList<OrdenOperacion>();
		operaciones = 0;
		cuentaSwift= new CuentaCliente();
		cliente = new Cliente();
		//NM29643 - infi_TTS_466 20/07/2014
		setOrdenesCrucesTitulos(new ArrayList<OrdenesCruce>());
		setOrdenesCrucesDivisas(new ArrayList<OrdenesCruce>());
	}
	
	/**Indicador de Comision Variable*/
	private boolean poseeComision;
	
	/**Indicador de Comision Variable*/
	private boolean poseeComisionInvariable;
	
	/**Fecha valor en formato String */	
	private String fechaValorString;
	
	/** Id de ejecuci&oacute;n */
	private long idEjecucion;

	/**
	 * Fecha Cancelacion de la orden, campo en BD=ordene_fe_cancelacion
	 */
	private Date fechaCancelacion;

	/**
	 * Fecha Pacto de recompra
	 */
	private Date fechaPactoRecompra;

	/**
	 * Fecha de la orden, campo en BD=ordene_ped_fe_orden
	 */
	private Date fechaOrden;

	/**
	 * Indica si la orden es financiada, campo en BD=ordene_financiado
	 */
	private boolean financiada = false;

	/**
	 * Lista de documentos asociados a una orden.
	 */
	private ArrayList documentos = new ArrayList();

	/**
	 * Lista de campos din&aacute;micos
	 */
	private ArrayList<CampoDinamico> camposDinamicos = new ArrayList<CampoDinamico>();
	
	//NM29643 - infi_TTS_466 20/07/2014
	/**
	 * Lista de cruces tipo en Titulos de la orden
	 */
	private ArrayList<OrdenesCruce> ordenesCrucesTitulos = new ArrayList<OrdenesCruce>();
	
	/**
	 * Lista de cruces tipo en Divisas de la orden
	 */
	private ArrayList<OrdenesCruce> ordenesCrucesDivisas = new ArrayList<OrdenesCruce>();
	
	/**Dias de intereses caidos en formato String */	
	private String diasIntCaidos;
	
	/**
	 *
	 */
	private ArrayList ordenDataExt = new ArrayList();

	/**
	 * Este monto va a variar depende de la transacci&oacute;n que se este
	 * efectuando. Si es una toma de orden indica que cantidad se quiere
	 * comprar, campo en BD=ordene_ped_monto si es una venta cual es el monto de
	 * la venta
	 */
	private double monto;

	/**
	 * Lista de operaciones financieras aplicadas a una orden
	 */
	private ArrayList operacion = new ArrayList();

	/**
	 * Contraparte seleccionada en la orden campo en BD=contraparte
	 */
	private String contraparte = "";

	/**
	 * T&iacute;tulos asociados a la orden
	 */
	private ArrayList<OrdenTitulo> ordenTitulo = new ArrayList<OrdenTitulo>();
	/**
	 * Muestra la relaci&oacute;n de una orden con otra
	 */
	private long idOrdenRelacionada;

	/**
	 * Monto que fue adjudicado campo en BD=ordene_adj_monto
	 */
	private double montoAdjudicado;

	/**
	 * Tasa de cambio usada
	 */
	private double tasaCambio;

	/**
	 * N&uacute;mero de unidades que se invirtieron
	 */
	private long unidadesInvertidas;

	/**
	 * Status de la orden, procesada, no procesada campo en BD=ordsta_id
	 */
	private String status = "";

	/**
	 * Cuenta del cliente
	 */
	private String cuentaCliente = "";

	/**
	 * Id del bloter emisor
	 */
	private String idBloter;

	/**
	 * Tipo de persona
	 */
	private String idTipoPersona;

	/**
	 * Id del sistema al cu&aacute;l se envi&oacute; la transacci&oacute;n
	 */
	private int idSistema;

	/**
	 * Id depositario
	 */
	private String idDepositario;

	/**
	 * Indica si el archivo fue enviado en los archivos que necesita un
	 * determinado emisor
	 */
	private boolean enviado = false;

	/** Id del cliente */
	private long idCliente;

	/** Id de la empresa */
	private String idEmpresa = "";

	/** Id de la transacci&oacute;n */
	private String idTransaccion = "";

	/**
	 * Segmento de ALTAIR que tenia el Cliente asignado en el momento de la toma
	 * de orden. Porci&oacute;n de Banca.
	 */
	private String segmentoBanco = "";

	/**
	 * Segmento de ALTAIR que tenia el Cliente asignado en el momento de la toma
	 * de orden. Porci&oacute;n de Segmento
	 */
	private String segmentoSegmento = "";

	/**
	 * Segmento de ALTAIR que tenia el Cliente asignado en el momento de la toma
	 * de orden. Porci&oacute;n de SubSegmento
	 */
	private String segmentoSubSegmento = "";

	/**
	 * Segmento de INFI que tenia el Cliente asignado en el momento de la toma
	 * de orden.
	 */
	private String segmentoInfi = "";

	/**
	 * Fecha valor de la orden campo en BD=ordene_ped_fe_valor
	 */
	private Date fechaValor;

	/**
	 * Monto pendiente por cobrar campo en BD=ordene_ped_total_pen
	 * */
	private double montoPendiente;

	/**
	 * Monto total de la orden incluyendo comisiones campo en
	 * BD=ordene_ped_total
	 * */
	private double montoTotal;

	/**
	 * Monto total de los intereses caidos campo en BD=ordene_ped_int_caidos
	 * */
	private double montoInteresCaidos;

	/** Tipo de cuenta donde se aplican las transacciones financieras */
	private String tipoCuenta = "";

	/**
	 * Precio ofrecido para la Compra campo en BD=ordene_ped_precio
	 * */
	private double precioCompra;

	/**
	 * Rendimiento campo en BD=ordene_ped_redimiento
	 * */
	private double rendimiento;

	/** Indica si la orden es de cartera propia */
	private boolean carteraPropia = false;

	/**
	 * Precio Pacto de recompra campo en BD=ordene_ped_rcp_precio
	 * */
	private double precioRecompra;

	/** Nombre del usuario que tomo la orden */
	private String nombreUsuario = "";

	/** Centro contable al que pertenece el usuario que tom&oacute; la orden */
	private String centroContable = "";

	/** Sucursal del usuario que tom&oacute; la orden */
	private String sucursal = "";

	/** Terminal del usuario que tom&oacute; la orden */
	private String terminal = "";

	/** Veh&iacute;culo Tomador */
	private String vehiculoTomador = "";

	/** Veh&iacute;culo Colocador */
	private String vehiculoColocador = "";

	/** Veh&iacute;culo Recompra */
	private String vehiculoRecompra = "";

	/**
	 * Fecha en que fue adjudicada la orden campo en
	 * BD=ordene_fecha_adjudicacion
	 * */
	private Date fechaAdjudicacion;

	/**
	 * Fecha en que fue liquidada la orden campo en BD=ordene_fecha_liquidacion
	 * */
	private Date fechaLiquidacion;

	/**
	 * Fecha en que paso a custodia la orden campo en BD=ordene_fecha_custodia
	 * */
	private Date fechaCustodia;

	/**
	 * Indicador de Toma de Orden Interna de BDV (Cartera Propia) campo en
	 * BD=ordene_ped_in_bdv
	 */
	private Integer internoBDV;

	private BigDecimal comisionEmisor = new BigDecimal(0);

	private BigDecimal tasaPool = new BigDecimal(0);

	private BigDecimal gananciaRed = new BigDecimal(0);

	private BigDecimal comisionOficina = new BigDecimal(0);

	private BigDecimal comisionOperacion = new BigDecimal(0);
	/**
	 * Monto total de las comisiones que se aplican sobre la orden Campo en
	 * BD=ordene_ped_comisiones
	 */
	private BigDecimal montoComisionOrden = new BigDecimal(0);

	private boolean operacionesPendientes = false;

	/** Id del instrumento financiero */
	private String instrumentoId;
	
	private String observaciones;
	
	private String conceptoId;
	
	private String sectorProductivoId;
	
	private String actividadEconomicaId;
	
	private String tipoProducto;
	
	private long idOrdenClave=0;
	
	private CuentaCliente cuentaSwift=null;
	
	private Cliente cliente = new Cliente();

	/**
	 * Atributo que indica si la cuenta a la que se realizara el abono es: 1)Cuenta en el exterior 2)Cuenta nacional en dolares
	 * */
	private int tipoCuentaAbono;
	
	/**
	 * Atributo que indica la fecha de la ultima actualizacion de la Orden
	 * */
	private Date fechaUltActualizacion;
	
	/**
	 * Indicador del esatus de la verificacion segun el BCV sin_verificar=0  verificado_BCV_aceptado=1 verificado_BCV_rechazado=2  verificado_manual_aceptado=3 verificado_manual_rechazado=4
	 */
	private String statusVerificacionBCV;
	
	/**
	 * Identificador de la orden BCV posterior a proceso de verificacion via Web Service
	 * */
	private String idOrdeneBCV;
	
   /**
	* metodo para la obtencion de la fecha de ultima actualizacion 
	* */
	public Date getFechaUltActualizacion() {
		return fechaUltActualizacion;
	}

	/**
	* metodo para la configuracion de la fecha de ultima actualizacion 
	* */
	public void setFechaUltActualizacion(Date fechaUltActualizacion) {
		this.fechaUltActualizacion = fechaUltActualizacion;
	}

	/**
	 * Metodo que permite optener el tipo de cuenta para abono: 1)Cuenta en el exterior 2)Cuenta nacional en dolares
	 * */
	public int getTipoCuentaAbono() {
		return tipoCuentaAbono;
	}

	/**
	 * Metodo que permite configurar el tipo de cuenta para abono: 1)Cuenta en el exterior 2)Cuenta nacional en dolares
	 * */
	public void setTipoCuentaAbono(int tipoCuentaAbono) {
		if(tipoCuentaAbono==1 || tipoCuentaAbono==2){		
			this.tipoCuentaAbono = tipoCuentaAbono;
		}
	}

	/**
	 * Getter of the property <tt>contraparte</tt>
	 * 
	 * @return Returns the contraparte.
	 * 
	 */
	public String getContraparte() {
		return contraparte;
	}

	/**
	 * Setter of the property <tt>contraparte</tt>
	 * 
	 * @param contraparte
	 *            The contraparte to set.
	 * 
	 */
	public void setContraparte(String contraparte) {
		this.contraparte = contraparte;
	}

	/**
	 * Getter of the property <tt>idDepositario</tt>
	 * 
	 * @return Returns the idDepositario.
	 * 
	 */
	public String getIdDepositario() {
		return idDepositario;
	}

	/**
	 * Setter of the property <tt>idDepositario</tt>
	 * 
	 * @param idDepositario
	 *            The idDepositario to set.
	 * 
	 */
	public void setIdDepositario(String idDepositario) {
		this.idDepositario = idDepositario;
	}

	/** Indica si la orden es financiada */
	public boolean isFinanciada() {
		return financiada;
	}

	/**
	 * Getter of the property <tt>idOrdenRelacionada</tt>
	 * 
	 * @return Returns the idOrdenRelacionada.
	 * 
	 */
	public long getIdOrdenRelacionada() {
		return idOrdenRelacionada;
	}

	/**
	 * Setter of the property <tt>idOrdenRelacionada</tt>
	 * 
	 * @param idOrdenRelacionada
	 *            The idOrdenRelacionada to set.
	 * 
	 */
	public void setIdOrdenRelacionada(long idOrdenRelacionada) {
		this.idOrdenRelacionada = idOrdenRelacionada;
	}

	/**
	 * Getter of the property <tt>ordenTitulo</tt>
	 * 
	 * @return Returns the ordenTitulo.
	 * 
	 */
	public ArrayList<OrdenTitulo> getOrdenTitulo() {
		return ordenTitulo;
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
	 * Indica si la lista de campos din&aacute;micos est&aacute; vacia
	 * 
	 * @return <tt>true</tt> Verdadero en caso que la lista no tenga elementos
	 * @see java.util.ArrayList#isEmpty()
	 * 
	 */
	public boolean isCampoDinamicoVacio() {
		return camposDinamicos.isEmpty();
	}

	/** Agrega la forma de pago relacionada a la orden */
	public boolean agregarFormaPago(FormaPago formaPago) {
		return this.formaPago.add(formaPago);
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

	public void agregarOrdenTitulo(ArrayList ordenTitulos) {
		this.ordenTitulo.addAll(ordenTitulos);
	}

	/**
	 * Getter of the property <tt>ordenDataExt</tt>
	 * 
	 * @return Returns the ordenDataExt.
	 * 
	 */
	public ArrayList<OrdenDataExt> getOrdenDataExt() {
		return ordenDataExt;
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * 
	 * @return <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * 
	 */
	public boolean isOrdenDataExtVacio() {
		return ordenDataExt.isEmpty();
	}

	/** Verifica si la lista de los documentos est&aacute; vac&iacute;a */
	public boolean isDocumentosVacio() {
		return documentos.isEmpty();
	}

	/** Agrega un documento a la orden */
	public boolean agregarDocumento(OrdenDocumento ordenDocumento) {
		return this.documentos.add(ordenDocumento);
	}

	/** Elimina los documentos de la orden */
	public void eliminarDocumento() {
		this.documentos.clear();
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
	public boolean agregarOrdenDataExt(OrdenDataExt ordenDataExt) {
		return this.ordenDataExt.add(ordenDataExt);
	}

	/**
	 * Getter of the property <tt>documentos</tt>
	 * 
	 * @return Returns the documentos.
	 * 
	 */
	public ArrayList<OrdenDocumento> getDocumentos() {
		return documentos;
	}

	/**
	 * Setter of the property <tt>documentos</tt>
	 * 
	 * @param documentos
	 *            The documentos to set.
	 * 
	 */
	public void setDocumentos(ArrayList<OrdenDocumento> documentos) {
		this.documentos = documentos;
	}

	/**
	 * Getter of the property <tt>fechaOrden</tt>
	 * 
	 * @return Returns the fechaOrden.
	 * 
	 */
	public Date getFechaOrden() {
		return fechaOrden;
	}

	/**
	 * Setter of the property <tt>fechaOrden</tt>
	 * 
	 * @param fechaOrden
	 *            The fechaOrden to set.
	 * 
	 */
	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	/**
	 * Getter of the property <tt>idTipoPersona</tt>
	 * 
	 * @return Returns the idTipoPersona.
	 * 
	 */
	public String getIdTipoPersona() {
		return idTipoPersona;
	}

	/**
	 * Setter of the property <tt>idTipoPersona</tt>
	 * 
	 * @param idTipoPersona
	 *            The idTipoPersona to set.
	 * 
	 */
	public void setIdTipoPersona(String idTipoPersona) {
		this.idTipoPersona = idTipoPersona;
	}

	/*
	 * (non-javadoc)
	 */
	private ArrayList formaPago;

	/**
	 * Getter of the property <tt>formaPago</tt>
	 * 
	 * @return Returns the formaPago.
	 * 
	 */
	public ArrayList getFormaPago() {
		return formaPago;
	}

	/**
	 * Setter of the property <tt>formasDePago</tt>
	 * 
	 * @param formasDePago
	 *            The formasDePago to set.
	 * 
	 */
	public void setFormasDePago(ArrayList formasDePago) {
		this.formaPago = formasDePago;
	}

	/**
	 * Id que identifica la moneda usada
	 */
	private String idMoneda;

	/**
	 * Getter of the property <tt>idMoneda</tt>
	 * 
	 * @return Returns the idMoneda.
	 * 
	 */
	public String getIdMoneda() {
		return idMoneda;
	}

	/**
	 * Setter of the property <tt>idMoneda</tt>
	 * 
	 * @param idMoneda
	 *            The idMoneda to set.
	 * 
	 */
	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}

	/**
	 * Getter of the property <tt>status</tt>
	 * 
	 * @return Returns the status.
	 * 
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter of the property <tt>status</tt>
	 * 
	 * @param status
	 *            The status to set.
	 * 
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Monto cobrado de la orden
	 */
	private double montoCobrado;

	/**
	 * Getter of the property <tt>montoCobrado</tt>
	 * 
	 * @return Returns the montoCobrado.
	 * 
	 */
	public double getMontoCobrado() {
		return montoCobrado;
	}

	/**
	 * Setter of the property <tt>montoCobrado</tt>
	 * 
	 * @param montoCobrado
	 *            The montoCobrado to set.
	 * 
	 */
	public void setMontoCobrado(double montoCobrado) {
		this.montoCobrado = montoCobrado;
	}

	/**
	 * Indica cual es el monto de financiamiento
	 */
	private double montoFinanciado;

	/**
	 * Getter of the property <tt>montoFinanciado</tt>
	 * 
	 * @return Returns the montoFinanciado.
	 * 
	 */
	public double getMontoFinanciado() {
		return montoFinanciado;
	}

	/**
	 * Setter of the property <tt>montoFinanciado</tt>
	 * 
	 * @param montoFinanciado
	 *            The montoFinanciado to set.
	 * 
	 */
	public void setMontoFinanciado(double montoFinanciado) {
		this.montoFinanciado = montoFinanciado;
	}

	/**
	 * Getter of the property <tt>financiada</tt>
	 * 
	 * @return Returns the financiada.
	 * 
	 */
	public boolean getFinanciada() {
		return financiada;
	}

	/**
	 * Setter of the property <tt>financiada</tt>
	 * 
	 * @param financiada
	 *            The financiada to set.
	 * 
	 */
	public void setFinanciada(boolean financiada) {
		this.financiada = financiada;
	}

	/**
	 * Getter of the property <tt>tasaCambio</tt>
	 * 
	 * @return Returns the tasaCambio.
	 * 
	 */
	public double getTasaCambio() {
		return tasaCambio;
	}

	/**
	 * Setter of the property <tt>tasaCambio</tt>
	 * 
	 * @param tasaCambio
	 *            The tasaCambio to set.
	 * 
	 */
	public void setTasaCambio(double tasaCambio) {
		this.tasaCambio = tasaCambio;
	}

	/**
	 * Getter of the property <tt>unidadesInvertidas</tt>
	 * 
	 * @return Returns the unidadesInvertidas.
	 * 
	 */
	public long getUnidadesInvertidas() {
		return unidadesInvertidas;
	}

	/**
	 * Setter of the property <tt>unidadesInvertidas</tt>
	 * 
	 * @param unidadesInvertidas
	 *            The unidadesInvertidas to set.
	 * 
	 */
	public void setUnidadesInvertidas(long unidadesInvertidas) {
		this.unidadesInvertidas = unidadesInvertidas;
	}

	/**
	 * Getter of the property <tt>montoAdjudicado</tt>
	 * 
	 * @return Returns the montoAdjudicado.
	 * 
	 */
	public double getMontoAdjudicado() {
		return montoAdjudicado;
	}

	/**
	 * Setter of the property <tt>montoAdjudicado</tt>
	 * 
	 * @param montoAdjudicado
	 *            The montoAdjudicado to set.
	 * 
	 */
	public void setMontoAdjudicado(double montoAdjudicado) {
		this.montoAdjudicado = montoAdjudicado;
	}

	/**
	 * Getter of the property <tt>montoPedido</tt>
	 * 
	 * @return Returns the montoPedido.
	 * 
	 */
	public double getMonto() {
		return monto;
	}

	/**
	 * Setter of the property <tt>montoPedido</tt>
	 * 
	 * @param montoPedido
	 *            The montoPedido to set.
	 * 
	 */
	public void setMonto(double monto) {
		this.monto = monto;
	}

	/**
	 * Getter of the property <tt>cuentaCliente</tt>
	 * 
	 * @return Returns the cuentaCliente.
	 * 
	 */
	public String getCuentaCliente() {
		return cuentaCliente;
	}

	/**
	 * Setter of the property <tt>cuentaCliente</tt>
	 * 
	 * @param cuentaCliente
	 *            The cuentaCliente to set.
	 * 
	 */
	public void setCuentaCliente(String cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	/**
	 * Getter of the property <tt>idSistema</tt>
	 * 
	 * @return Returns the idSistema.
	 * 
	 */
	public int getIdSistema() {
		return idSistema;
	}

	/**
	 * Setter of the property <tt>idSistema</tt>
	 * 
	 * @param idSistema
	 *            The idSistema to set.
	 * 
	 */
	public void setIdSistema(int idSistema) {
		this.idSistema = idSistema;
	}

	/**
	 * Getter of the property <tt>idBloter</tt>
	 * 
	 * @return Returns the idBloter.
	 * 
	 */
	public String getIdBloter() {
		return idBloter;
	}

	/**
	 * Setter of the property <tt>idBloter</tt>
	 * 
	 * @param idBloter
	 *            The idBloter to set.
	 * 
	 */
	public void setIdBloter(String idBloter) {
		this.idBloter = idBloter;
	}

	/**
	 * id de la unidad de inversi&oacute;n
	 */
	private int idUnidadInversion;

	/**
	 * Getter of the property <tt>idUnidadInversion</tt>
	 * 
	 * @return Returns the idUnidadInversion.
	 * 
	 */
	public int getIdUnidadInversion() {
		return idUnidadInversion;
	}

	/**
	 * Setter of the property <tt>idUnidadInversion</tt>
	 * 
	 * @param idUnidadInversion
	 *            The idUnidadInversion to set.
	 * 
	 */
	public void setIdUnidadInversion(int idUnidadInversion) {
		this.idUnidadInversion = idUnidadInversion;
	}

	/**
	 * Descripcion de la unidad de inversi&oacute;n
	 */
	private String undinv_status_descripcion;

	/**
	 * Getter of the property <tt>descripcionUnidadInversion</tt>
	 * 
	 * @return Returns the descripcionUnidadInversion.
	 * 
	 */
	public String getDescripcionUnidadInversion() {
		return undinv_status_descripcion;
	}

	/**
	 * Setter of the property <tt>descripcionUnidadInversion</tt>
	 * 
	 * @param descripcionUnidadInversion
	 *            The descripcionUnidadInversion to set.
	 * 
	 */
	public void setDescripcionUnidadInversion(String undinv_status_descripcion) {
		this.undinv_status_descripcion = undinv_status_descripcion;
	}

	/*
	 * (non-javadoc)
	 */
	private long idOrden;

	/**
	 * Getter of the property <tt>idOrden</tt>
	 * 
	 * @return Returns the idOrden.
	 * 
	 */
	public long getIdOrden() {
		return idOrden;
	}

	/**
	 * Setter of the property <tt>idOrden</tt>
	 * 
	 * @param idOrden
	 *            The idOrden to set.
	 * 
	 */
	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}

	/**
	 * Getter of the property <tt>operacion</tt>
	 * 
	 * @return Returns the operacion.
	 * 
	 */
	public ArrayList<OrdenOperacion> getOperacion() {
		return operacion;
	}

	/**
	 * Indica si la lista de operaciones financieras est&aacute; vacia
	 * 
	 * @return <tt>true</tt> Verdadero en caso que la lista no tenga elementos
	 * @see java.util.ArrayList#isEmpty()
	 * 
	 */
	public boolean isOperacionVacio() {
		return operacion.isEmpty();
	}

	/**
	 * Indica si la lista de formas de pago est&aacute; vacia
	 * 
	 * @return <tt>true</tt> Verdadero en caso que la lista no tenga elementos
	 * @see java.util.ArrayList#isEmpty()
	 * 
	 */
	public boolean isFormaPagoVacio() {
		return formaPago.isEmpty();
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
	public boolean agregarOperacion(OrdenOperacion ordenOperacion) {
		this.operacion.add(operaciones++, ordenOperacion);
		return true;
	}

	/**
	 * Indica el total de las comisiones asociadas a una orden
	 * 
	 * @return el monto total de comisiones de la orden
	 */
	public double getTotalComisiones() {
		double montoComisiones = 0;
		if (!isOperacionVacio()) {
			// calcular comisiones
			for (Iterator iter = operacion.iterator(); iter.hasNext();) {
				OrdenOperacion ordenOperacion = (OrdenOperacion) iter.next();
				if (ordenOperacion.getInComision() == 1) {
					// acumular comisiones encontradas
					montoComisiones += ordenOperacion.getMontoOperacion()
							.doubleValue();
				}
			}
		}
		return montoComisiones;
	}

	/**
	 * Agrega el campo din&aacute;mico a la orden
	 * 
	 * @param campoDinamico
	 *            campo din&aacute;mico
	 */
	public boolean agregarCampoDinamico(CampoDinamico campoDinamico) {
		return this.camposDinamicos.add(campoDinamico);
	}

	public boolean isCampoDinamicoisEmpty() {
		return camposDinamicos.isEmpty();
	}

	/**
	 * Indica si la orden fue enviada
	 * 
	 * @return <tt>true</tt> Verdadero en caso que la orden se haya enviado
	 */
	public boolean isEnviado() {
		return enviado;
	}

	/**
	 * @param enviado
	 *            establece la variable enviado
	 */
	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	public ArrayList<CampoDinamico> getCamposDinamicos() {
		return camposDinamicos;
	}

	public void setCamposDinamicos(ArrayList<CampoDinamico> camposDinamicos) {
		this.camposDinamicos = camposDinamicos;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public String getSegmentoBanco() {
		return segmentoBanco;
	}

	public void setSegmentoBanco(String segmentoBanco) {
		this.segmentoBanco = segmentoBanco;
	}

	public String getSegmentoSegmento() {
		return segmentoSegmento;
	}

	public void setSegmentoSegmento(String segmentoSegmento) {
		this.segmentoSegmento = segmentoSegmento;
	}

	public String getSegmentoSubSegmento() {
		return segmentoSubSegmento;
	}

	public void setSegmentoSubSegmento(String segmentoSubSegmento) {
		this.segmentoSubSegmento = segmentoSubSegmento;
	}

	public String getSegmentoInfi() {
		return segmentoInfi;
	}

	public void setSegmentoInfi(String segmentoInfi) {
		this.segmentoInfi = segmentoInfi;
	}

	public Date getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}

	public double getMontoPendiente() {
		return montoPendiente;
	}

	public void setMontoPendiente(double montoPendiente) {
		this.montoPendiente = montoPendiente;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public double getMontoInteresCaidos() {
		return montoInteresCaidos;
	}

	public void setMontoInteresCaidos(double montoInteresCaidos) {
		this.montoInteresCaidos = montoInteresCaidos;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public double getRendimiento() {
		return rendimiento;
	}

	public void setRendimiento(double rendimiento) {
		this.rendimiento = rendimiento;
	}

	public boolean isCarteraPropia() {
		return carteraPropia;
	}

	public void setCarteraPropia(boolean carteraPropia) {
		this.carteraPropia = carteraPropia;
	}

	public double getPrecioRecompra() {
		return precioRecompra;
	}

	public void setPrecioRecompra(double precioRecompra) {
		this.precioRecompra = precioRecompra;
	}

	public String getVehiculoTomador() {
		return vehiculoTomador;
	}

	public void setVehiculoTomador(String vehiculoTomador) {
		this.vehiculoTomador = vehiculoTomador;
	}

	public String getVehiculoColocador() {
		return vehiculoColocador;
	}

	public void setVehiculoColocador(String vehiculoColocador) {
		this.vehiculoColocador = vehiculoColocador;
	}

	public String getVehiculoRecompra() {
		return vehiculoRecompra;
	}

	public void setVehiculoRecompra(String vehiculoRecompra) {
		this.vehiculoRecompra = vehiculoRecompra;
	}

	public void setOrdenDataExt(ArrayList ordenDataExt) {
		this.ordenDataExt = ordenDataExt;
	}

	public void setOperacion(ArrayList operacion) {
		this.operacion = operacion;
	}

	public void setOrdenTitulo(ArrayList ordenTitulo) {
		this.ordenTitulo = ordenTitulo;
	}

	public void setFormaPago(ArrayList formaPago) {
		this.formaPago = formaPago;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getCentroContable() {
		return centroContable;
	}

	public void setCentroContable(String centroContable) {
		this.centroContable = centroContable;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public Date getFechaAdjudicacion() {
		return fechaAdjudicacion;
	}

	public void setFechaAdjudicacion(Date fechaAdjudicacion) {
		this.fechaAdjudicacion = fechaAdjudicacion;
	}

	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	public Date getFechaCustodia() {
		return fechaCustodia;
	}

	public void setFechaCustodia(Date fechaCustodia) {
		this.fechaCustodia = fechaCustodia;
	}

	public long getIdEjecucion() {
		return idEjecucion;
	}

	public void setIdEjecucion(long idEjecucion) {
		this.idEjecucion = idEjecucion;
	}

	public Integer getInternoBDV() {
		return internoBDV;
	}

	public void setInternoBDV(Integer internoBDV) {
		this.internoBDV = internoBDV;
	}

	public BigDecimal getComisionEmisor() {
		return comisionEmisor;
	}

	public void setComisionEmisor(BigDecimal comisionEmisor) {
		this.comisionEmisor = comisionEmisor;
	}

	public BigDecimal getTasaPool() {
		return tasaPool;
	}

	public void setTasaPool(BigDecimal tasaPool) {
		this.tasaPool = tasaPool;
	}

	public BigDecimal getGananciaRed() {
		return gananciaRed;
	}

	public void setGananciaRed(BigDecimal gananciaRed) {
		this.gananciaRed = gananciaRed;
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

	public boolean isOperacionesPendientes() {
		return operacionesPendientes;
	}

	public void setOperacionesPendientes(boolean operacionesPendientes) {
		this.operacionesPendientes = operacionesPendientes;
	}

	public String getUndinv_status_descripcion() {
		return undinv_status_descripcion;
	}

	public void setUndinv_status_descripcion(String undinv_status_descripcion) {
		this.undinv_status_descripcion = undinv_status_descripcion;
	}

	public BigDecimal getMontoComisionOrden() {
		return montoComisionOrden;
	}

	public void setMontoComisionOrden(BigDecimal montoComisionOrden) {
		this.montoComisionOrden = montoComisionOrden;
	}

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public String getInstrumentoId() {
		return instrumentoId;
	}

	public void setInstrumentoId(String instrumentoId) {
		this.instrumentoId = instrumentoId;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String idTipoProducto) {
		this.tipoProducto = idTipoProducto;
	}

	public Date getFechaPactoRecompra() {
		return fechaPactoRecompra;
	}

	public void setFechaPactoRecompra(Date fechaPactoRecompra) {
		this.fechaPactoRecompra = fechaPactoRecompra;
	}

	public String getConceptoId() {
		return conceptoId;
	}

	public void setConceptoId(String conceptoId) {
		this.conceptoId = conceptoId;
	}

	public String getSectorProductivoId() {
		return sectorProductivoId;
	}

	public void setSectorProductivoId(String sectorId) {
		this.sectorProductivoId = sectorId;
	}

	public String getActividadEconomicaId() {
		return actividadEconomicaId;
	}

	public void setActividadEconomicaId(String actividadEconomicaId) {
		this.actividadEconomicaId = actividadEconomicaId;
	}

	public String getFechaValorString() {
		return fechaValorString;
	}

	public void setFechaValorString(String fechaValorString) {
		this.fechaValorString = fechaValorString;
	}

	public String getClienteRifCed() {
		return clienteRifCed;
	}

	public void setClienteRifCed(String clienteRifCed) {
		this.clienteRifCed = clienteRifCed;
	}

	public long getIdOrdenClave() {
		return idOrdenClave;
	}

	public void setIdOrdenClave(long idOrdenClave) {
		this.idOrdenClave = idOrdenClave;
	}

	public CuentaCliente getCuentaSwift() {
		return cuentaSwift;
	}

	public void setCuentaSwift(CuentaCliente cuentaSwift) {
		this.cuentaSwift = cuentaSwift;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean getComisionCero(){
		return comisionCero;
	}
	
	public void setComisionCero(boolean comision){				
			this.comisionCero=comision;		
	}		
	

    public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
        	throw new RuntimeException("Error en proceso de Clonacion de objeto ORDEN:  " + ex.getMessage());
        }
        return obj;
    }
    
	public boolean isComisionInvariable() {
		return poseeComisionInvariable;
	}

	public void setComisionInvariable(boolean comisionInvariable) {
		this.poseeComisionInvariable = comisionInvariable;
	}

	public boolean isPoseeComision() {
		return poseeComision;
	}

	public void setPoseeComision(boolean poseeComision) {
		this.poseeComision = poseeComision;
	}
	
	public String getDiasIntCaidos() {
		return diasIntCaidos;
	}

	public void setDiasIntCaidos(String diasIntCaidos) {
		this.diasIntCaidos = diasIntCaidos;
	}

	//NM29643 - infi_TTS_466 20/07/2014
	public ArrayList<OrdenesCruce> getOrdenesCrucesTitulos() {
		return ordenesCrucesTitulos;
	}

	public void setOrdenesCrucesTitulos(ArrayList<OrdenesCruce> ordenesCrucesTitulos) {
		this.ordenesCrucesTitulos = ordenesCrucesTitulos;
	}

	public ArrayList<OrdenesCruce> getOrdenesCrucesDivisas() {
		return ordenesCrucesDivisas;
	}

	public void setOrdenesCrucesDivisas(ArrayList<OrdenesCruce> ordenesCrucesDivisas) {
		this.ordenesCrucesDivisas = ordenesCrucesDivisas;
	}

	public String getStatusVerificacionBCV() {
		return statusVerificacionBCV;
	}

	public void setStatusVerificacionBCV(String statusVerificacionBCV) {
		this.statusVerificacionBCV = statusVerificacionBCV;
	}

	public String getIdOrdeneBCV() {
		return idOrdeneBCV;
	}

	public void setIdOrdeneBCV(String idOrdeneBCV) {
		this.idOrdeneBCV = idOrdeneBCV;
	}
	
}
