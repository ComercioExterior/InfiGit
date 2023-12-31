package com.bdv.infi.logic.interfaces;

public class TransaccionNegocio {

	/**Transaccion de BLOQUEO*/
	public static final String BLOQUEO_TITULOS = "BLOQUEO_TITULOS";
	
	/**Transaccion de DESBLOQUEO*/
	public static final String DESBLOQUEO_TITULOS = "DESBLOQUEO_TITULOS";
	
	/**Transaccion de TOMA DE ORDEN*/
	public static final String TOMA_DE_ORDEN = "TOMA_ORDEN";	

	/**Transaccion de TOMA DE ORDEN DE CARTERA PROPIA*/
	public static final String TOMA_DE_ORDEN_CARTERA_PROPIA = "TOMA_ORDEN_CARTERA_PROPIA";
		
	/**Transaccion de ENTRADA DE TITULOS*/
	public static final String ENTRADA_DE_TITULOS = "ENTRADA_TITULO";
	
	/**Transaccion de cobro de comisiones*/
	public static final String CUSTODIA_COMISIONES = "CUSTODIA_COMISIONES";
	
	/**Transaccion de cobro de comisiones*/
	public static final String CUSTODIA_AMORTIZACION = "PAGO_AMORTIZACION";
	
	/**Transaccion de adjudicaci�n*/
	public static final String ADJUDICACION = "ADJUDICACION";
	
	/**Transaccion de adjudicaci�n para la subasta de divisas*/
	public static final String ADJUDICACION_SUBASTA_DIVISAS = "ADJ_SUBASTA_DIVISA";
	
	/**Indica la transaccion a realizar Salida Interna*/
	public static final String SALIDA_INTERNA = "SALIDA_INTERNA";
	
	/**Indica la transaccion a realizar Salida Interna*/
	public static final String SALIDA_EXTERNA = "SALIDA_EXTERNA";
	
	/**Indica la transaccion a realizar Pago de Cupones*/
	public static final String PAGO_CUPON = "PAGO_CUPONES";
	
	/**Indican los tipos de Transacciones de Negocio posibles*/
	public static final String VENTA_TITULOS = "VENTA_TITULOS";
	
	/**Indican los tipos de Transacciones de Negocio posibles*/
	public static final String PACTO_RECOMPRA = "PACTO_RECOMPRA";
	
	/**Indican los tipos de Transacciones de Negocio posibles*/
	public static final String CARGA_INICIAL = "CARGA_INICIAL";
	
	/**Proceso de Liquidacion*/
	public static final String LIQUIDACION = "LIQUIDACION";
	
	/**Proceso de cierre de mes*/
	public static final String CIERRE_MES = "CIERRE_MES";
	
	/**Cancelaci�n de una unidad de inversi�n*/
	public static final String CANCELACION_UI = "CANCELACION_UI";	
	
	public static final String CANCELACION_ORDEN = "CANCELACION_ORDEN";	
	
	/**Cobro de financiamiento otorgado*/
	public static final String COBRO_FINANCIAMIENTO = "COBRO_FINANCIAMIENTO";	
	
	/**Orden de pago. Puede ser de cup�n o capital*/
	public static final String ORDEN_PAGO = "ORDEN_PAGO";	
	
	/**Orden de un veh�culo*/
	public static final String ORDEN_VEHICULO = "ORDEN_VEHICULO";		
	
	
	/**Proceso de cobro batch subasta*/
	public static final String PROC_BATCH_ADJ_SUBASTA_ENVIO = "BATCH_ADJ_SUBASTA_ENVIO";
	public static final String PROC_BATCH_ADJ_SUBASTA_RECEP = "BATCH_ADJ_SUBASTA_RECEP";
	
	public static final String PROC_BATCH_LIQ_SUBASTA_ENVIO = "BATCH_LIQ_SUBASTA_ENVIO";
	public static final String PROC_BATCH_LIQ_SUBASTA_RECEP = "BATCH_LIQ_SUBASTA_RECEP";
	
	/**Proceso de cobro Liquidacion batch subasta divisas canal Clavenet*/
	public static final String PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_ENVIO = "BATCH_LIQ_SUB_DIV_P_ENVIO";
	public static final String PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_RECEP = "BATCH_LIQ_SUB_DIV_P_RECEP";
		
	
	/**Proceso de cobro batch sitme*/
	public static final String PROC_BATCH_ADJ_SITME_ENVIO = "BATCH_ADJ_SITME_ENVIO";
	public static final String PROC_BATCH_LIQ_SITME_ENVIO = "BATCH_LIQ_SITME_ENVIO";
	public static final String PROC_BATCH_ADJ_SITME_RECEP = "BATCH_ADJ_SITME_RECEP";
	public static final String PROC_BATCH_LIQ_SITME_RECEP = "BATCH_LIQ_SITME_RECEP";
	
	/**Proceso de abono cuentas nacionales moneda extranjera batch sitme*/
	public static final String PROC_BATCH_CTA_NACIONAL_MONEDA_EXT_SITME_ENVIO = "BATCH_ABO_MEXT_SIT_ENVIO";
	public static final String PROC_BATCH_CTA_NACIONAL_MONEDA_EXT_SITME_RECEP = "BATCH_ABO_MEXT_SIT_RECEP";
	
	/**Proceso de abono cuentas nacionales moneda extranjera batch sitme*/
	public static final String PROC_BATCH_CTA_NACIONAL_MONEDA_EXT_SUBV_P_ENVIO = "BATCH_ABO_MEX_SUBVP_ENVIO";
	public static final String PROC_BATCH_CTA_NACIONAL_MONEDA_EXT_SUBV_P_RECEP = "BATCH_ABO_MEX_SUBVP_RECEP";
	
	/**Proceso de abono cuentas nacionales moneda extranjera batch venta titulo*/
	public static final String PROC_BATCH_CTA_NAC_MON_EXT_VENTA_ENVIO = "BATCH_ABO_MEX_VENTA_ENVIO";
	public static final String PROC_BATCH_CTA_NAC_MON_EXT_VENTA_RECEP = "BATCH_ABO_MEX_VENTA_RECEP";
	
	/**Proceso de abono cuentas nacionales moneda extranjera batch venta titulo*/
	public static final String PROC_BATCH_CTA_NAC_MON_EXT_CUPON_ENVIO = "BATCH_ABO_MEX_CUPON_ENVIO";
	public static final String PROC_BATCH_CTA_NAC_MON_EXT_CUPON_RECEP = "BATCH_ABO_MEX_CUPON_RECEP";
	
	/**Proceso de cobro batch subasta DIVISAS*/
	public static final String PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_ENVIO = "BATCH_SUB_DIVISAS_ENVIO";
	public static final String PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_RECEP = "BATCH_SUB_DIVISAS_RECEP";
	
	/**Proceso de cobro batch subasta divisas PERSONAL*/
	public static final String PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_PERSONAL_ENVIO = "BATCH_SUB_DIV_PER_ENVIO";
	public static final String PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_PERSONAL_RECEP = "BATCH_SUB_DIV_PER_RECEP";
	
	public static final String PROC_BATCH_CTA_NAC_MON_EXT_SUB_DIV_P_ENVIO = "BATCH_ABO_MEX_SUBVP_ENVIO";
	public static final String PROC_BATCH_CTA_NAC_MON_EXT_SUB_DIV_P_RECEP = "BATCH_ABO_MEX_SUBVP_RECEP";
	
	/**Proceso de cobro batch SICAD II CLAVENET PERSONAL*/
	public static final String PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_ENVIO = "BATCH_ADJ_SICAD2PER_ENVIO";
	public static final String PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_RECEP = "BATCH_ADJ_SICAD2PER_RECEP";
	public static final String PROC_BATCH_COBRO_LIQ_SICAD_II_CLAVENET_PERSONAL_ENVIO = "BATCH_LIQ_SICAD2PER_ENVIO";
	public static final String PROC_BATCH_COBRO_LIQ_SICAD_II_CLAVENET_PERSONAL_RECEP = "BATCH_LIQ_SICAD2PER_RECEP";
	
	/**Proceso de cobro batch SICAD II RED COMERCIAL*/
	public static final String PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_ENVIO = "BATCH_ADJ_SICAD2RED_ENVIO";
	public static final String PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_RECEP = "BATCH_ADJ_SICAD2RED_RECEP";
	public static final String PROC_BATCH_COBRO_LIQ_SICAD_II_RED_COMERCIAL_ENVIO = "BATCH_LIQ_SICAD2RED_ENVIO";
	public static final String PROC_BATCH_COBRO_LIQ_SICAD_II_RED_COMERCIAL_RECEP = "BATCH_LIQ_SICAD2RED_RECEP";
	
	/**Proceso de cobro batch Conciliacion Retencion*/
	public static final String PROC_BATCH_CONCILIACION_RETENCION_ENVIO = "BATCH_CONCIL_RET_ENVIO";
	public static final String PROC_BATCH_CONCILIACION_RETENCION_RECEP = "BATCH_CONCIL_RET_RECEP";
	
	/**Proceso de actualizaci�n de clientes en OPICS*/
	public static final String PROC_ACT_CLIENTES_OPICS = "PROC_ACT_CLIENTES_OPICS";
	
	/**Proceso de actualizaci�n de estatus de ordenes en Exportacion de Ordenes INFI*/
	public static final String PROC_ACT_ORDENES_INFI = "PROC_ACT_ORDENES_INFI";
	
	/**Proceso de cierre de sistema*/
	public static final String PROC_CIERRE_SISTEMA = "PROC_CIERRE_SISTEMA";
	
	/**Ciclo de cobro batch */
	public static final String CICLO_BATCH_SITME = "CICLO_BATCH_SITME";	
	public static final String CICLO_BATCH_SUBASTA = "CICLO_BATCH_SUBASTA";
	public static final String CICLO_BATCH_SUBASTA_DIVISAS = "CICLO_BATCH_SUB_DIVISA";
	public static final String CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL = "CICLO_BATCH_SUB_DIV_P";
	public static final String CICLO_BATCH_SICADII_CLAVENET_PERSONAL = "CICLO_BATCH_SICAD2PER";
	public static final String CICLO_BATCH_SICADII_RED_COMERCIAL = "CICLO_BATCH_SICAD2RED";
	public static final String CICLO_BATCH_CONCILIACION_RETENCION = "CICLO_BATCH_CONCIL_RET";
	
	/**Ciclo Abonos Cuentas Nacional Moneda Extranjera*/
	public static final String CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SITME = "CICLO_BATCH_ABO_MEXT_SIT";
	
	
	
	/**Ciclo de abonos a cuentas Nacional Moneda Extranjera por venta de Titulos*/
	public static final String CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_VENTA_TITULO = "CICLO_BATCH_ABO_MXT_VENTA";
	
	/**Ciclo de abonos a cuentas Nacional Moneda Extranjera por pago cupones*/
	public static final String CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_PAGO_CUPON = "CICLO_BATCH_ABO_MXT_CUPON";

	/**Ciclo Abonos Cuentas Nacional Moneda Extranjera*/
	public static final String CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SUB_DIV_P = "CICLO_BATCH_ABOMEXT_SUBVP";
	
	/**Proceso de generaci�n de archivos de las diferentes interfaces  */
	public static final String INTERFACE_BCV = "INTERFACE_BCV";	
	public static final String INTERFACE_CARMEN = "INTERFACE_CARMEN";
	public static final String INTERFACE_ESTADISTICA = "INTERFACE_ESTADISTICA";	
	
	/**Constante creada para asignar el valor recompra a las ordenes OPICS en caso de Venta Titulos*/
	public static final String RECOMPRA="RECOMPRA";
		
	/**Transaccion relacionada con el proceso de env�o de correos*/
	public static final String ENVIO_CORREOS = "ENVIO_CORREOS";
	/**Campo Indicador de activacion de envio de correos masivos*/
	public static final String CAMPO_INDICADOR_CORREO_ACTIVO = "INDICADOR_CORREO_ACTIVO";
	
	/**Transaccion relacionada con el proceso de Comision Buen Valor*/
	public static final String COMISION_BUEN_VALOR = "COMISION_BUEN_VALOR";
	
	/**Indicado de transaccion cruce Sicad 2 Carga para el canal Clavenet Personal */
	public static final String CRUCE_SICAD2_CLAVE_CARGA = "CRUCE_SICAD2_CLAVE_CARGA";
	
	/**Indicado de transaccion cruce Sicad 2 Carga para el canal Red Comercial */
	public static final String CRUCE_SICAD2_RED_CARGA = "CRUCE_SICAD2_RED_CARGA";
	
	/**Indicado de transaccion cruce Sicad 2 Cierre para el canal Clavenet Personal */
	public static final String CRUCE_SICAD2_CLAVE_CIERRE = "CRUCE_SICAD2_CLAVE_CIERRE";
	
	/**Indicado de transaccion cruce Sicad 2 Cierre para el canal Red Comercial */
	public static final String CRUCE_SICAD2_RED_CIERRE = "CRUCE_SICAD2_RED_CIERRE";
	
	/**Indicado de transaccion cruce Sicad 2 Carga para el canal Clavenet Personal */
	public static final String GEN_ARCH_SICAD2_CLAVE = "GEN_ARCH_SICAD2_CLAVE";
	
	/**Indicado de transaccion para WS BCV Menudeo */
	public static final String WS_BCV_MENUDEO = "WS_BCV_MENUDEO";
	public static final String PROCESO_HIST_MENUDEO = "PROCESO_HIST_MENUDEO";
	public static final String WS_BCV_MESAS = "WS_BCV_MESAS";
	public static final String WS_BCV_IMESAS = "WS_BCV_IMESAS";
	public static final String WS_BCV_PMESAS = "WS_BCV_PMESAS";
	public static final String CONCILIACION_MENUDEO = "CONCILIACION_MENUDEO";
	public static final String MENUDEO_LECTURA_ARCHIVO = "MENUDEO_LECTURA_ARCHIVO";
	public static final String INTERVENCION_LECTURA_ARCHIVO = "INTERVEN_LECTURA_ARCHIVO";
	public static final String INTERVENCION_ANULACIONES_INTER = "INTER_ANU_ARCHIVO";
	public static final String WS_BCV_MESAS_A = "WS_BCV_MESAS_A";
	public static final String WS_BCV_TASA = "WS_BCV_TASA";
	public static final String MESA_LECTURA_ARCHIVO = "MESA_LECTURA_ARCHIVO";
	/**Indicado de transaccion para WS BCV Intervencion */
	public static final String WS_BCV_INTERVENCION = "BATCH_INTERVENCION_ENVIO";
	public static final String INTERVENCION_INTERBAN = "INTERVENCION_INTERBAN";
	public static final String INVENTARIO_INTERVENCION = "INVENTARIO_INTERVENCION";
	public static final String OFICINAS_INTERVENCION = "OFICINAS_INTERVENCION";
	public static final String LECTURA_INVENTARIO = "LECTURA_INVENTARIO";
	public static final String LECTURA_INTERVENCION = "LECTURA_INTERVENCION";
	
	/**Indicado de transaccion para WS BCV Mesa de cambio */
	public static final String WS_BCV_MESADECAMBIO = "BATCH_MESA_CAMBIO_ENVIO";
	
	/**Indicado de transaccion para WS BCV Menudeo Taquilla Aeropuerto*/
	public static final String WS_BCV_MENUDEO_TAQUILLA = "WS_BCV_MENUDEO_TAQUILLA";
	
	public static final String WS_BCV_INTERVENCION_CLIENTE = "BATCH_INTERVENCION_CLIENT";
	
	/**Indicado de transaccion para WS BCV Menudeo Anulacion Taquilla Aeropuerto*/
	public static final String WS_BCV_MENUDEO_TAQUILLA_ANULACION = "WS_BCV_MENUDEO_TAQ_ANUL";
	
	/**Indicado de transaccion para WS BCV Alto Valor */
	public static final String WS_BCV_ALTO_VALOR_DEMAN = "WS_BCV_ALTO_VALOR_DEMAN";
	
	/**Indicado de transaccion para WS BCV Alto Valor */
	public static final String WS_BCV_ALTO_VALOR_OFER = "WS_BCV_ALTO_VALOR_OFER";
	
	public static final String WS_BCV_ANULAR_OFER_DEMAN = "WS_BCV_ANULAR_OFER_DEMAN";
	
	/**Indicado de transaccion para Carga de las operaciones de Oferta a enviar al WS BCV */
	public static final String WS_BCV_CARGA_OFERTA = "WS_BCV_CARGA_OFERTA";	
	
	/**Indicado de transaccion para WS BCV Pacto */
	public static final String WS_BCV_PACTO = "WS_BCV_PACTO";
	
	//NM26659_27/04/2015 TTS-491 Modificacion para notificar monto adjudicado de menudeo 
	public static final String CRUCE_SIMADI_MENUDEO_NATURAL_CARGA = "CARGA_SIMADI_MENUDEO_PN";
	
	public static final String CRUCE_SIMADI_MENUDEO_JURIDICO_CARGA = "CARGA_SIMADI_MENUDEO_PJ";
	
	/**Transaccion asociada al proceso de Force de cierre de los ciclos de abono y cobros asociados a la OPS (Cobros y abonos por Altair)*/
	public static final String FORCE_CIERRE_CICLO = "FORCE_CIERRE_CICLO";
	

	//NM26659_18/05/2017 TTS-541 Proceso de recepcion de Operaciones DICOM
	public static final String PROC_RECEPCION_DEMANDA_DICOM = "BATCH_DEMANDA_DICOM_RECEP";
	
	
	
	public static final String PROC_RECEPCION_OFERTA_DICOM = "BATCH_OFERTA_DICOM_RECEP";

	//NM26659_28/05/2017 TTS-541 Ciclo de recepcion (Del BCV) y envio (a Mainframe) de Operaciones DICOM
	public static final String CICLO_BATCH_DICOM = "CICLO_BATCH_DICOM";
	public static final String PROC_ENVIO_DICOM = "BATCH_DICOM_ENVIO";
	public static final String BATCH_MENUDEO_ENVIO = "BATCH_MENUDEO_ENVIO";
	public static final String CICLO_BATCH_MENUDEO = "CICLO_BATCH_MENUDEO";
	
	
	public static final String PROC_RECEP_CRUCE_DICOM = "BATCH_CRUCE_DICOM_ENVIO";
	
	//NM26659_15/11/2017 TTS-541 DICOM LIQUIDACION MULTIMONEDA Inclusion de ciclos asociados a la Liquidacion de DICOM
	public static final String CICLO_BATCH_DICOM_COBRO_DEMANDA = "CICLO_BATCH_DICOM_COBRO_D";
	public static final String CICLO_BATCH_DICOM_ABONO_DEMANDA = "CICLO_BATCH_DICOM_ABONO_D";
	public static final String CICLO_BATCH_DICOM_COBRO_OFERTA = "CICLO_BATCH_DICOM_COBRO_O";
	public static final String CICLO_BATCH_DICOM_COBRO_COMISION_OFERTA= "CICLO_BATCH_COBRO_COMISION_DICOM_OFERTA";
	public static final String CICLO_BATCH_DICOM_ABONO_OFERTA= "CICLO_BATCH_DICOM_ABONO_O";
	
	public static final String CICLO_BATCH_SUBASTA_LIQUIDACION= "CICLO_BATCH_LIQ_SUBASTA";
	
	public static final String PROC_CRUCE_DICOM_INTERBANCARIO = "BATCH_CRUCE_DICOM";
	// Agregado por ALexander Rincon para el manejo de los ciclos
	// NM11383 EL 10/09/2018	
		public static final String PROC_ORO_RECEP = "BATCH_ORO_RECEP";
		public static final String PROC_ORO_ENVIO = "BATCH_ORO_ENVIO";
		public static final String CICLO_BATCH_ORO = "CICLO_BATCH_ORO";	
		public static final String PROC_CRUCE_ORO_INTERBANCARIO = "BATCH_CRUCE_ORO";
		//NM26659_18/05/2017 TTS-541 Proceso de recepcion de Operaciones 
		public static final String PROC_RECEPCION_DEMANDA_ORO = "BATCH_DEMANDA_ORO_RECEP";		
		//NM36635 TTS-541 Ciclo de recepcion (Del BCV) y envio (a Mainframe) de Operaciones ORO
		public static final String PROC_ENVIO_ORO = "BATCH_ORO_ENVIO";
	
	}
		
															 	
