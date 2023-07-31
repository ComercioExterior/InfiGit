package com.bdv.infi.data.beans_swift;

import java.math.BigDecimal;
import java.util.Random;

import megasoft.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;


public abstract class AbstractSwifLT {
	
	/*	Constantes de los LT de Swift */
	
	/**
	 * Número de orden procesada
	 */
	protected long numeroDeOrden = 0;
	
	/**
	 * Tipo de transacción de la orden
	 */
	protected String tipoTransaccion = "";
	
	
	/**
	 * Tag 1 del LT: Basic header block
	 */
	protected String messageTypes1Header = "{1:F01";	
	/**
	 * Cierre del Tag 1  :  
	 */
	protected String messageTypes1Foot = "0000000000}";	
	/**
	 * Tag 2 del LT: Application header block
	 */
	protected String messageTypes2HeaderMt103 = "{2:I103";
	
	/**
	 * Tag 2 del LT: Application header block MT110
	 */
	protected String messageTypes2HeaderMt110 = "{2:I110";
	/**
	 * Cierre del Tag 2  :  
	 */
	protected String messageTypes2Foot = "}";		
	/**
	 * Tag 4 del LT: Text block
	 */
	protected String messageTypes4Header = "{4:";
	/**
	 * Cuerpo del Tag 3 del LT
	 */
	protected StringBuffer messageTypes4Body = new StringBuffer();
	/**
	 * Cierre del Tag 4 : despues del ultimo elemento 
	 */
	protected String messageTypes4Foot = "-}";
	
	/* Tag de Respuesta */
	/**
	 * Tag 177: fecha de respuesta del LT.
	 * Fomato DateTime
	 */
	protected String messageTypes177 = "{177:x}";
	/**
	 * Tag 451: indica accept (0) / reject (1) del LT
	 */
	protected String messageTypes451 = "{451:x}";
	/**
	 * Tag 405: razon del rechazo del LT
	 */
	protected String messageTypes405 = "{405:x}";	
	/**
	 * Inicio y Cierre del LT
	 */
	protected String caracterEnd = "$";
	
	/*  Variables del Cuerpo */
	
	/** Transaction Reference Number	 */
	protected static final String FIELD_STRUCTURE_20 = ":20:";
	
	/** Valor inicial del campo 20	 */
	protected static final String VALOR_CAMPO_20_RENTA_FIJA = "99RF";
	
	/** Valor inicial del campo 20	 */
	protected static final String VALOR_CAMPO_20_CUSTODIA = "97CU";	

	
	/** numero de Cheque	 */
	protected static final String FIELD_STRUCTURE_21 = ":21:";
	
	/** Prioridad del mensaje	 */
	protected static final String PRIOTITY = "N";
	
	/**
	 * Fecha de Emisión
	 */
	protected static final String FIELD_STRUCTURE_30 =":30:";
	
	/**
	 * Moneda ID y monto de la operacion
	 */
	protected static final String FIELD_STRUCTURE_32B=":32B:";
	
	/** codigo de la transaccion bancaria  */
	protected static final String FIELD_STRUCTURE_23B = ":23B:CRED";
	
	/** Transaction type code  */
	protected static final String FIELD_STRUCTURE_26T = ":26T:";
	
	/** FechaValor / Codigo de Divisa / Monto	 */
	protected static final String FIELD_STRUCTURE_32A = ":32A:";
	
	/** Codigo de Divisa / instructed amount	 */
	String FIELD_STRUCTURE_33B = ":33B:";
	
	/** Tasa de cambio (No aplica)*/
	protected static final String FIELD_STRUCTURE_36 = ":36:";
	
	/** Informacion del Cliente Ordenante/ordering customer	 */
	protected static final String FIELD_STRUCTURE_50K = ":50K:/";
	
	/** Informacion del Cliente Ordenante/ordering customer	 */
	protected static final String FIELD_STRUCTURE_50F = ":50F:/";
	
	/** Sending Institution - BIC */
	protected static final String FIELD_STRUCTURE_51A = ":51A:";
	
	/** Informacion del Corresponsal/sender correspondent */
	protected static final String FIELD_STRUCTURE_53A = ":53A:/";
	protected static final String FIELD_STRUCTURE_53B = ":53B:/";
	
	/** Institucion Intermediaria	 
	 * <li>Identificador de parte</li>
	 * <li>Codigo BIC</li>
	 * */
	protected static final String FIELD_STRUCTURE_56A = ":56A:";
	
	/** Institucion Intermediaria	 
	 * <li>Identificador de parte</li>
	 * <li>Nombre y Dirección</li>
	 * */
	protected static final String FIELD_STRUCTURE_56D = ":56D:";
	
	/** Institucion donde el beneficiario posee la cuenta	 
	 * <li>Identificador de parte</li>
	 * <li>Codigo BIC</li>
	 * */
	protected static final String FIELD_STRUCTURE_57A = ":57A:";
	
	/** Institucion donde el beneficiario posee la cuenta	 
	 * <li>Identificador de parte</li>
	 * <li>Nombre y Dirección</li>
	 * */
	protected static final String FIELD_STRUCTURE_57D = ":57D:/";

	/** Drawer bank*/
	protected static final String FIELD_STRUCTURE_52D =":52D:";
	
	/**Remitente a receptor de información (Narrativo)*/
	protected static final String FIELD_STRUCTURE_72 =":72:";
	
	/**Referencia del campo 72*/
	protected static final String REF72 ="/REF/";
	
	/**Referencia del campo 72 BNF*/
	protected static final String BNF72 ="/BNF/";	

	/** Cliente Beneficiario	 */
	protected static final String FIELD_STRUCTURE_59 = ":59:/";
	
	/** Informacion del Remitente (Narrativo)	 */
	protected static final String FIELD_STRUCTURE_70 = ":70:";
	
	/** Detalles del gastos	 */
	protected static final String FIELD_STRUCTURE_71A = ":71A:";
	
	/** Detalles del gastos	 */
	protected static final String INFI = "INFI";
	
	/** Si el campo 71A = OUR 71G optional/ Si el campo 71A = BEN 71F mandatory/  Si el campo 71A = SHA 71F optional*/
	String FIELD_STRUCTURE_71 = "";
	
	/** Regulatory Reporting**/
	protected static final String FIELD_STRUCTURE_77B = ":77B:";
	
	protected static final String SALTO_LINEA = "\n";
	
	public static final String VE = "VE";
	public static final String DESC_VE = "VENEZUELA";
	
	//	-----------------------------------------------------------------
	
	//	Datos de la Operacion, comun a todas las operaciones
	/**
	 * Cuenta del Cliente
	 */
	protected String cuentaCliente = "";
	/**
	 * Currency Instructed Amount 33B
	 */
	protected BigDecimal currencyInstructedAmount = new BigDecimal(0);
	/** Codigo BIC cabecera del banco donde se sacaran los fondos para la tranferencia SWIFT**/
	protected String headerCodBIC;
	/** Numero de cuenta del banco donde se sacaran los fondos para la tranferencia SWIFT**/
	protected String headerNumeroCuenta;
	/** Descripcion del banco donde se sacaran los fondos para la tranferencia SWIFT**/
	protected String headerDescripcion;
	/**
	 * Cedula del Cliente
	 */
	protected String cedulaCliente = "";
	/**
	 * Cedula del Cliente
	 */
	protected String cedulaClienteSinGuion = "";
	/**
	 * Numero de Cheque
	 */
	protected String numeroCheque;
	/**
	 * Nombre del Cliente
	 */
	protected String nombreCliente = "";
	/**
	 * Direccion del Cliente
	 */
	protected String [] direccionCliente;
	
	/**
	 * Nacionalidad del Cliente
	 */
	protected String nacionalidadCliente;
	
	/**
	 * Pais de Residencia del Cliente
	 */
	protected String paisResideCliente;
	/**
	 * Código de Pais de Residencia del Cliente
	 */
	protected String codPaisResideCliente;	
	/**
	 * Estado de Residencia del Cliente
	 */
	protected String estadoResideCliente;
	/**
	 * Código de Estado de Residencia del Cliente
	 */
	protected String codEstadoResideCliente;	
	/**
	 * Pais de Residencia del Cliente
	 */
	protected String ciudadResideCliente;
	/**
	 * Código de Pais de Residencia del Cliente
	 */
	protected String codCiudadResideCliente;

	/**
	 * Telefono del Cliente
	 */
	protected String telefonoCliente = "";	
	/**
	 * Identificador de la Operacion
	 */
	protected String idOperacion = "";		
	/**
	 * Fecha de la operacion formato : yymmdd
	 */
	protected String fechaOperacion = "";
	/**
	 * referencia
	 */
	protected String referencia ="";
	/**
	 * Monto de la Operacion formato 99999,99
	 */
	protected String montoOperacion = "";
	
	/**
	 * Monto de la Operacion Comision
	 */
	protected String montoOperacionComision = "";
	/**
	 * Moneda de la operacion
	 */
	protected String siglasMoneda = "";
	
	/**
	 * Moneda de la operacion (Comision)
	 */
	protected String siglasMonedaComision = "";
	
	/**
	 * <b>Cuenta origen</b> del BDV de donde se sacaran los fondos para realizar la transacción<br>
	 * Estas cuentas se encuentran en el módulo correspondiente a cuentas BDV.
	 */
	protected String cuentaOrigenBDV = "";
	/**
	 * Nombre de la sucursal o nombre que hace referencia a la cuenta Origen
	 */
	protected String nombreCuentaOrigen = "";
	
	/**
	 * Cantidad de Acciones 
	 */
	protected String cantidadOperacion = "";
	/**
	 * Cuenta del Destinatario
	 */
	protected String cuentaDestinatario = "";
	
	
	/**
	 * Comision aplicada a Juridicos y personas naturales en Moneda != local
	 */
	protected BigDecimal comision = new BigDecimal(0);
	
	/**
	 * Nombre del Destinatario
	 */
	protected String nombreDestinatario = "";	
	/**
	 * Codigo del Banco Destino de la operacion
	 */
	protected String codBancoDestino = "";
	/**
	 * Nombre del Banco Destino
	 */
	protected String nombreBancoDestino = "";
	/**
	 * Direccion del Banco Destino
	 */
	protected String  direccionBancoDestino;
	/**
	 * Codigo del Banco Intermediario de la operacion
	 */
	protected String codBancoIntermediario = "";
	
	/**
	 * Numero de cuenta del Banco Destino en el banco intermediario
	 */
	protected String numeroCuentaBancoEnIntermediario = "";
	
	/**
	 * Nombre del Banco Intermediario
	 */
	protected String nombreBancoIntermediario = "";	
	/**
	 * Direccion del Banco Intermediario
	 */
	protected String direccionBancoIntermediario;
	/**
	 * Codigo Swift del BDV
	 */
	protected String codSwiftBDV = "";
	/**
	 * Campo que indica quien cancelara las comisiones (BEN)beneficiario (OUR)Corre por cuenta del banco
	 */
	protected String BENOUR = "";
	
	/**
	 * Campo que indica quien cancelara las comisiones (BEN)beneficiario (OUR)Corre por cuenta del banco
	 */
	protected String unidadInversion = "";
	
	/** 
	 * Id de producto asociado a 
	 */
	public String tipoProducto=null;
	/**
	 * String con el registro formateado
	 */
	protected StringBuffer bodyLT = new StringBuffer();
	
	/**
	 * Retorna el valor de la Cuenta del Cliente
	 * @return cuentaCliente
	 */
	public String getCuentaCliente() {
		return cuentaCliente;
	}
	/**
	 * Asigna valor a la Cuenta del Cliente
	 * @param cuentaCliente
	 */
	public void setCuentaCliente(String cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}
	
	/**
	 * Retorna el valor de la Cedula del Cliente
	 * @return cedulaCliente
	 */
	public String getCedulaCliente() {
		return cedulaCliente;
	}
	/**
	 * Asigna valor a la Cedula del Cliente
	 * @param cedulaCliente
	 */
	public void setCedulaCliente(String cedulaCliente) {
		this.cedulaCliente = cedulaCliente;
	}	
	
	/**
	 * Retorna el valor del Nombre del Cliente
	 * @return nombreCliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}
	/**
	 * Asigna valor al Nombre del Cliente
	 * @param nombreCliente
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	
	/**
	 * Retorna el valor de la Direccion del Cliente
	 * @return direccionCliente
	 */
	public String [] getDireccionCliente() {
		return direccionCliente;
	}
	/**
	 * Asigna valor a la Direccion del Cliente
	 * @param direccionCliente
	 */
	public void setDireccionCliente(String direccionCliente) {
		if (direccionCliente == null) {
			this.direccionCliente = new String [1];
			this.direccionCliente[0] = "";
		} else {
			this.direccionCliente = new String [1];
			this.direccionCliente[0] = direccionCliente;
		}
	}
	
	public String getNacionalidadCliente() {
		return nacionalidadCliente;
	}
	public void setNacionalidadCliente(String nacionalidadCliente) {
		this.nacionalidadCliente = nacionalidadCliente;
	}
	public String getPaisResideCliente() { 
		return paisResideCliente;
	}
	public void setPaisResideCliente(String paisResideCliente) {
		this.paisResideCliente = paisResideCliente;
	}
	/**
	 * Retorna el valor del Telefono del Cliente
	 * @return telefonoCliente
	 */
	public String getTelefonoCliente() {
		return telefonoCliente;
	}
	/**
	 * Asigna valor alTelefono del Cliente
	 * @param telefonoCliente
	 */
	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
	}
	
	/**
	 * Retorna el valor del Identificador de la Operacion
	 * @return idOperacion
	 */
	public String getIdOperacion() {
		return idOperacion;
	}
	/**
	 * Asigna valor al Identificador de la Operacion
	 * @param idOperacion
	 */
	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}
	
	/**
	 * Retorna el valor de la Fecha de la operacion formato : yymmdd
	 * @return fechaOperacion
	 */
	public String getFechaOperacion() {
		return fechaOperacion;
	}
	/**
	 * Asigna valor a la Fecha de la operacion formato : yymmdd
	 * @param fechaOperacion
	 */
	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}

	/**
	 * Retorna el valor de la Moneda de la operacion
	 * @return siglasMoneda
	 */
	public String getSiglasMoneda() {
		return siglasMoneda;
	}
	/**
	 * Asigna valor a la Moneda de la operacion
	 * @param siglasMoneda
	 */
	public void setSiglasMoneda(String siglasMoneda) {
		this.siglasMoneda = siglasMoneda;
	}
	
	/**
	 * Retorna el valor del Monto de la Operacion formato 99999,99
	 * @return montoOperacion
	 */
	public String getMontoOperacion() {
		return montoOperacion;
	}
	/**
	 * Asigna valor al Monto de la Operacion formato 99999,99
	 * @param montoOperacion
	 */
	public void setMontoOperacion(String montoOperacion) {
		this.montoOperacion = montoOperacion;
		this.montoOperacion.replaceAll(".", ",");
	}
	
	/**
	 * Retorna el valor de la Cantidad de Acciones 
	 * @return cantidadOperacion
	 */
	public String getCantidadOperacion() {
		return cantidadOperacion;
	}
	/**
	 * Asigna valor a la Cantidad de Acciones 
	 * @param cantidadOperacion
	 */
	public void setCantidadOperacion(String cantidadOperacion) {
		this.cantidadOperacion = cantidadOperacion;
	}
	
	
	/**
	 * Retorna el valor del Cuenta del Destinatario
	 * @return nombreDestinatario
	 */
	public String getCuentaDestinatario() {
		return cuentaDestinatario;
	}
	/**
	 * Asigna valor al Cuenta del Destinatario
	 * @param CuentaDestinatario
	 */
	public void setCuentaDestinatario(String cuentaDestinatario) {
		this.cuentaDestinatario = cuentaDestinatario;
	}

	/**
	 * Retorna el valor del Nombre del Destinatario
	 * @return nombreDestinatario
	 */
	public String getNombreDestinatario() {
		return nombreDestinatario;
	}
	/**
	 * Asigna valor al Nombre del Destinatario
	 * @param nombreDestinatario
	 */
	public void setNombreDestinatario(String nombreDestinatario) {
		this.nombreDestinatario = nombreDestinatario;
	}

	/**
	 * Retorna el valor del Banco Destino de la operacion
	 * @return codBancoDestino
	 */
	public String getCodBancoDestino() {
		return codBancoDestino;
	}
	/**
	 * Asigna valor al Banco Destino de la operacion
	 * @param codBancoDestino
	 */
	public void setCodBancoDestino(String codBancoDestino) {
		this.codBancoDestino = codBancoDestino;
	}
	
	/**
	 * Retorna el valor del Nombre del Banco Destino
	 * @return nombreBancoDestino
	 */
	public String getNombreBancoDestino() {
		return nombreBancoDestino;
	}
	/**
	 * Asigna valor al Nombre del Banco Destino
	 * @param nombreBancoDestino
	 */
	public void setNombreBancoDestino(String nombreBancoDestino) {
		this.nombreBancoDestino = nombreBancoDestino;
	}

	/**
	 * Retorna el valor de la Direccion del Banco Destino
	 * @return direccionBancoDestino
	 */
	public String getDireccionBancoDestino() {
		return direccionBancoDestino;
	}
	/**
	 * Asigna valor a la Direccion del Banco Destino
	 * @param direccionBancoDestino
	 */
	public void setDireccionBancoDestino(String direccionBancoDestino) {
		this.direccionBancoDestino = direccionBancoDestino;
	}
	
	/**
	 * Retorna el valor del Codigo del Banco Intermediario de la operacion
	 * @return codBancoIntermediario
	 */
	public String getCodBancoIntermediario() {
		return codBancoIntermediario;
	}
	/**
	 * Asigna valor al Codigo del Banco Intermediario de la operacion
	 * @param codBancoIntermediario
	 */
	public void setCodBancoIntermediario(String codBancoIntermediario) {
		this.codBancoIntermediario = codBancoIntermediario;
	}
	
	/**
	 * Retorna el valor del Nombre del Banco Intermediario
	 * @return nombreBancoIntermediario
	 */
	public String getNombreBancoIntermediario() {
		return nombreBancoIntermediario;
	}
	/**
	 * Asigna valor al Nombre del Banco Intermediario
	 * @param nombreBancoIntermediario
	 */
	public void setNombreBancoIntermediario(String nombreBancoIntermediario) {
		this.nombreBancoIntermediario = nombreBancoIntermediario;
	}
	
	/**
	 * Retorna el valor de la Direccion del Banco Intermediario
	 * @return direccionBancoIntermediario
	 */
	public String getDireccionBancoIntermediario() {
		return direccionBancoIntermediario;
	}
	/**
	 * Asigna valor a la Direccion del Banco Intermediario
	 * @param direccionBancoIntermediario
	 */
	public void setDireccionBancoIntermediario(String direccionBancoIntermediario) {
		this.direccionBancoIntermediario = direccionBancoIntermediario;
	}

	/**
	 * Retorna el valor del mCodigo Swift del BDV
	 * @return codSwiftBDV
	 */
	public String getCodSwiftBDV() {
		return codSwiftBDV;
	}
	/**
	 * Asigna valor al Codigo Swift del BDV
	 * @param codSwiftBDV
	 */	
	public void setCodSwiftBDV(String codSwiftBDV) {
		this.codSwiftBDV = codSwiftBDV;
	}
	
	/**
	 * Permite armar el registro
	 * @return
	 * @throws Exception
	 */
	public String getBodyLT() throws Exception {
		return null;
	}
	/**
	 * @return the numeroCheque
	 */
	public String getNumeroCheque() {
		return numeroCheque;
	}
	/**
	 * @param numeroCheque the numeroCheque to set
	 */
	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}
	public String getCuentaOrigenBDV() {
		return cuentaOrigenBDV;
	}
	public void setCuentaOrigenBDV(String cuentaOrigenBDV) {
		this.cuentaOrigenBDV = cuentaOrigenBDV;
	}
	public String getMontoOperacionComision() {
		return montoOperacionComision;
	}
	public void setMontoOperacionComision(String montoOperacionComision) {
		this.montoOperacionComision = montoOperacionComision;
	}
	public String getNombreCuentaOrigen() {
		return nombreCuentaOrigen;
	}
	public void setNombreCuentaOrigen(String nombreCuentaOrigen) {
		this.nombreCuentaOrigen = nombreCuentaOrigen;
	}
	public String getNumeroCuentaBancoEnIntermediario() {
		return numeroCuentaBancoEnIntermediario;
	}
	public void setNumeroCuentaBancoEnIntermediario(
			String numeroCuentaBancoEnIntermediario) {
		this.numeroCuentaBancoEnIntermediario = numeroCuentaBancoEnIntermediario;
	}
	public String getBENOUR() {
		return BENOUR;
	}
	public void setBENOUR(String benour) {
		BENOUR = benour;
	}
	public BigDecimal getCurrencyInstructedAmount() {
		return currencyInstructedAmount;
	}
	public void setCurrencyInstructedAmount(BigDecimal currencyInstructedAmount) {
		this.currencyInstructedAmount = currencyInstructedAmount;
	}
	public String getHeaderCodBIC() {
		return headerCodBIC;
	}
	public void setHeaderCodBIC(String headerCodBIC) {
		this.headerCodBIC = headerCodBIC;
	}
	public String getHeaderDescripcion() {
		return headerDescripcion;
	}
	public void setHeaderDescripcion(String headerDescripcion) {
		this.headerDescripcion = headerDescripcion;
	}
	public String getHeaderNumeroCuenta() {
		return headerNumeroCuenta;
	}
	public void setHeaderNumeroCuenta(String headerNumeroCuenta) {
		this.headerNumeroCuenta = headerNumeroCuenta;
	}
	public String getFIELD_STRUCTURE_71() {
		return FIELD_STRUCTURE_71;
	}
	public void setFIELD_STRUCTURE_71(String field_structure_71) {
		FIELD_STRUCTURE_71 = field_structure_71;
	}
	//---------------------------------------------------------------------------------------------------
	public String getFIELD_STRUCTURE_33B() {
		return FIELD_STRUCTURE_33B;
	}
	public void setFIELD_STRUCTURE_33B(String field_structure_33B) {
		FIELD_STRUCTURE_33B = field_structure_33B;
	}
	//---------------------------------------------------------------------------------------------------
	public String getSiglasMonedaComision() {
		return siglasMonedaComision;
	}
	public void setSiglasMonedaComision(String siglasMonedaComision) {
		this.siglasMonedaComision = siglasMonedaComision;
	}
	public BigDecimal getComision() {
		return comision;
	}
	public void setComision(BigDecimal comision) {
		this.comision = comision;
		this.comision.toString().replaceAll(",", ".");
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String transaccion) {
		if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)){
			if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
				this.referencia = "POR COMPRA DE DIVISAS";
			}
		}else{
			if (transaccion.equals(TransaccionNegocio.PACTO_RECOMPRA)){
				this.referencia = "POR COMPRA DE BONOS";
			}else if(transaccion.equals(TransaccionNegocio.VENTA_TITULOS)){
				this.referencia = "POR VENTA DE BONOS";
			}else if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
				this.referencia = "POR PAGO DE INTERESES";
			}
		}		
	}
	
	public long getNroOrden(String transaccion, long idOrden, long idOrdenRelacion) {
		if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)){
			return idOrdenRelacion;
		}else{
			if(transaccion.equalsIgnoreCase(TransaccionNegocio.PACTO_RECOMPRA)){
				return idOrdenRelacion;
			}else {
				return idOrden;
			}
		}		
	}
	
	public long getNumeroDeOrden() {
		return numeroDeOrden;
	}
	public void setNumeroDeOrden(long numeroDeOrden) {
		this.numeroDeOrden = numeroDeOrden;
	}
	
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}	
	
	public String getCiudadResideCliente() {
		return ciudadResideCliente;
	}
	public void setCiudadResideCliente(String ciudadResideCliente) {
		this.ciudadResideCliente = ciudadResideCliente;
	}
	public String getCodCiudadResideCliente() {
		return codCiudadResideCliente;
	}
	public void setCodCiudadResideCliente(String codCiudadResideCliente) {
		this.codCiudadResideCliente = codCiudadResideCliente;
	}
	public String getCodEstadoResideCliente() {
		return codEstadoResideCliente;
	}
	public void setCodEstadoResideCliente(String codEstadoResideCliente) {
		this.codEstadoResideCliente = codEstadoResideCliente;
	}
	public String getCodPaisResideCliente() {
		return codPaisResideCliente;
	}
	public void setCodPaisResideCliente(String codPaisResideCliente) {
		this.codPaisResideCliente = codPaisResideCliente;
	}
	public String getEstadoResideCliente() {
		return estadoResideCliente;
	}
	public void setEstadoResideCliente(String estadoResideCliente) {
		this.estadoResideCliente = estadoResideCliente;
	}
	public void setDireccionCliente(String[] direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	/**
	 * Retorna un numero aleatorio para identificar cada registro SWIFT
	 * @return
	 * @throws Exception
	 */
	public int obtenerNumeroAleatorio()throws Exception{
		
		//Numero aleatorio
		Random r = new Random();
		int limite=99999999;
		
		//Retornamos el numero obtenido
		return r.nextInt(limite+1);
	}
	
	/**
	 * Arma la cedula del cliente eliminando la identificación del sistema "INFI" y colocandola
	 * en formato "tipoPersona-numRifCedula"
	 * @param cedulaCliente
	 * @return rifCedula sin formato de mensaje infi
	 */
	public String armarRifCedula(String cedulaCliente) {
		
		//Elimina la identificacion con la palabra "INFI"
		String rifCedulaAux = cedulaCliente.substring(4, cedulaCliente.length());
		//Obtiene el tipo de persona
		String tipoPersona = rifCedulaAux.substring(0,1);
		//Obtiene el número de rif/cedula
		rifCedulaAux = rifCedulaAux.substring(1,cedulaCliente.length());
		
		//obtiene la el numero de rif/cedula con el tipo de persona (Ej. V-15363014)
		String rifCedula = tipoPersona+"-"+rifCedulaAux;
		
		return rifCedula;
	}
	
	/**
	 * Establece el valor del campo 20 dependiendo de la transacción de la orden
	 */
	protected void establecerCampo20(){
		if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)){
			messageTypes4Body.append(FIELD_STRUCTURE_20).append(VALOR_CAMPO_20_RENTA_FIJA).append(getNumeroDeOrden()).append(SALTO_LINEA);
		}else{	
			//VENTA y PACTO corresponden al área de liquidación
			if (this.getTipoTransaccion().equals(TransaccionNegocio.PACTO_RECOMPRA)||this.getTipoTransaccion().equals(TransaccionNegocio.VENTA_TITULOS)){
				messageTypes4Body.append(FIELD_STRUCTURE_20).append(VALOR_CAMPO_20_RENTA_FIJA).append(getNumeroDeOrden()).append(SALTO_LINEA);	
			}else{
				messageTypes4Body.append(FIELD_STRUCTURE_20).append(VALOR_CAMPO_20_CUSTODIA).append(getCedulaClienteSinGuion()).append(SALTO_LINEA);
			}
		}
	}
	public String getUnidadInversion() {
		return unidadInversion;
	}
	public void setUnidadInversion(String unidadInversion) {
		this.unidadInversion = unidadInversion;
	}
	public String getCedulaClienteSinGuion() {
		return cedulaClienteSinGuion;
	}
	public void setCedulaClienteSinGuion(String cedulaClienteSinGuion) {
		this.cedulaClienteSinGuion = cedulaClienteSinGuion;
	}
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	
}