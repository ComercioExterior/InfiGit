package com.bdv.infi.logic.interfaces;

public interface ParametrosSistema {
	

	/**
	 * Interface SWIF
	 */
	public static final String INTERFACE_SWIFT="INTERFACE SWIFT";
	/**
	 * Dias Laborales
	 */
	public static final String DIAS_LABORABLES="DIAS-LABORABLES";
	/**
	 * Usuario para los servicios web
	 */
	public static final String USUARIO_WEB_SERVICES="USUARIO-WEB-SERVICES";
	/**
	 * Clave del usuario para los servicios
	 */
	public static final String CLAVE_WEB_SERVICES="CLAVE-WEB-SERVICES";
	/**
	 * Password FTP para Opics
	 */
	public static final String PASSWORD_FTP="PASSWORD-FTP";

	/**
	 * Nombre del archivo para Operacion de Cambio
	 */
	public static final String OPICS_ARCHIVO_NOMBRE="NOMBRE-ARCHIVO-OC";

	/**
	 * Ruta donde se dejara el archivo de los deal tickets
	 */
	public static final String OPICS_ARCHIVO_RUTA="OPICS-ARCHIVO-RUTA";

	/**
	 * Servidor FTP para dejar archivo SWIF mt103, mt110
	 */
	public static final String SWIFT_SERVIDOR="SWIFT-SERVIDOR";
	/**
	 * NOMBRE DEL ARCHIVO SWIFT QUE SE DEJAR� EN LA COLA DE CUSTODIA
	 */
	public static final String SWIFT_NOM_CUSTOVALORES="SWIFT-NOM-CUSTOVALORES";
	/**
	 * NOMBRE DEL ARCHIVO SWIFT QUE SE DEJAR� EN LA COLA DE TESORERIA
	 */
	public static final String SWIFT_NOM_TESORERIA="SWIFT-NOM-TESORERIA";
	/**
	 * NOMBRE DEL ARCHIVO SWIFT QUE SE DEJAR� EN LA COLA DE CLAVENET PERSONAL
	 */
	public static final String SWIFT_NOM_CLAVENET_PERSONAL="SWIFT-NOM-CLAVENET-P";
	/**
	 * DIRECTORIO DONDE SE ALMACENAR�N LOS ARCHIVOS SWIFT CON OPERACIONES RELACIONADAS A CUSTODIA
	 */
	public static final String SWIFT_COLA_CUSTOVALORES="SWIFT-COLA-CUSTOVALORES";
	/**
	 * DIRECTORIO DONDE SE ALMACENAR�N LOS ARCHIVOS SWIFT CON OPERACIONES RELACIONADAS A TESORERIA
	 */
	public static final String SWIFT_COLA_TESORERIA="SWIFT-COLA-TESORERIA";
	/**
	 * DIRECTORIO DONDE SE ALMACENAR�N LOS ARCHIVOS SWIFT CON OPERACIONES RELACIONADAS A CLAVENET PERSONAL
	 */
	public static final String SWIFT_COLA_CLAVENET_PERSONAL="SWIFT-COLA-CLAVENET-P";
	/**
	 * CODIGO SWIFT DEL BANCO DE VENEZUELA
	 */ 
	public static final String SWIFT_CODIGO_SWIFT_BDV="SWIFT-CODIGO_SWIFT_BDV";
	/**
	 * DIRECCION IP DEL SERVIDOR FTP
	 */
	public static final String DIRECCION_SERVIDOR_FTP="DIRECCION-SERVIDOR-FTP";
	/**
	 * USUARIO VALIDO PARA CONECTAR AL SERVIDOR FTP
	 */
	public static final String USUARIO_FTP="USUARIO-FTP";
	/**
	 * PASSWORD DEL USUARIO VALIDO PARA CONECTAR A SERVIDOR FTP
	 */
	public static final String CLAVE_USUARIO_FTP="CLAVE-USUARIO-FTP";
	/**
	 * 
	 */
	public static final String UBICACION_FISICA_FTPTXT="UBICACION-FISICA-FTPTXT";
	/**
	 * NOMBRE DEL ARCHIVO CON QUE SE GUARDARA VIA FTP
	 */
	public static final String NOMBRE_ARCHIVO_FTP="NOMBRE-ARCHIVO-FTP";
	
	/**
	 * CUENTA DEL BANCO CENTRAL DE VENEZUELA
	 */
	public static final String CUENTA_BCV="CUENTA_BCV";
	
	/**
	 * RIF DEL BANCO CENTRAL DE VENEZUELA
	 */
	public static final String RIF_BCV="RIF_BCV";
	
	/**
	 * RIF DEL BANCO DE VENEZUELA
	 */
	public static final String RIF_BDV="RIF_BDV";
	
	public static final String NOMBRE_BDV="NOMBRE_BDV";
	
	public static final String CUENTA_ME="CUENTA_ME";
	
	public static final String CUENTA_MN="CUENTA_MN";
	
	
	
	/**
	 * RIF DEL BANCO DE VENEZUELA
	 */
	public static final String RIF_DIG_VERIF_REPORTERIA="RIF_DIG_VERIF_REPORTERIA";	
		
	/**
	 * Constante para buscar el indicador de control de cambio en los par�metros generales del sistema
	 */
	public static final String CONTROL_DE_CAMBIO="CONTROL_DE_CAMBIO";
	
	/**
	 * Constante para CNO de archivo OPICS
	 */
	public static final String CNO_OPICS="CNO";	
	
	/**
	 * Constante para CNO de archivo OPICS
	 */
	public static final String PLEDGEID_JURIDICO_SICAD2="PLEDGEID";
	
	/**
	 * Trader por defecto
	 */
	public static final String TRADER="TRAD";	
	
	/**
	 * Grupo para los deal por operacion cambio
	 */
	public static final String MENSAJE_OPICS_OC="MENSAJE_OPICS_OC";
	
	/**
	 * Centro de Costo para deal operacion cambio
	 */
	public static final String COST="COST";
	
	/**
	 * Grupo para los deal por renta fija
	 */
	public static final String MENSAJE_OPICS_RF="MENSAJE_OPICS_RF";
	
	/**
	 * Grupo para los deal por renta fija sitme
	 */
	public static final String MENSAJE_OPICS_RF_SITME="MENSAJE_OPICS_RF_S";
	
	/**
	 * Grupo para los deal por renta fija sitme venta
	 */
	public static final String MENSAJE_OPICS_RF_SITME_VENTA="MENSAJE_OPICS_RF_SV";	
	
	
	/**
	 * Portafolio por defecto
	 */
	public static final String PORT="PORT";
	
	/**
	 * Comision cobrada a personas juridicas en SWIFT (BEN)
	 */
	public static final String CO_SWIFT_JURIDICO_USD="CO_SWIFT_JURIDICO_USD";
	/**
	 * Comision cobrada a personas naturales en SWIFT (BEN)
	 */
	public static final String CO_SWIFT_NATURALES_USD="CO_SWIFT_NATURALES_USD";
	/**
	 * Ruta y nombre del archivo donde estara ubicado usuario y contrase�a FTP
	 */
	public static final String HOME_DIRECTORY_FILE="HOME_DIRECTORY_FILE";
	/**
	 * Grupo para la interface de OPICS
	 */
	public static final String INTERFACE_OPICS="INTERFACE OPICS";
	/**
	 * Nombre del archivo para renta fija
	 */
	public static final String NOMBRE_ARCHIVO_RENTA_FIJA="NOMBRE-ARCHIVO-RENTA-FIJA";
	/**
	 * Directorio donde se guardadn archivos temporales
	 */
	public static final String TEMP_DIRECTORY ="TEMP_DIRECTORY";
	/**
	 * Valor booleano que indica si se debe validar una sola session por usuario
	 */
	public static final String SESION_ACTIVA ="SESION ACTIVA";
	
	/**
	 * Valor de observaci�n que llevar�n algunos informes de custodia
	 */
	public static final String CUSTODIA_OBS1 ="CUSTODIA_OBS1";

	/**
	 * Valor de observaci�n que llevar�n algunos informes de custodia
	 */	
	public static final String CUSTODIA_OBS2 ="CUSTODIA_OBS2";	
	 
	/**
	 * Cantidad de d&iacute;as minimo que debe poseer la cuenta de un cliente para ser valida para el cobro y toma de ordenes
	 */	
	public static final String DIAS_VALIDACION_CUENTAS = "DIAS_VALIDACION_CUENTAS";	 
	
	/**
	 * Valor del codigo del Serial Contable Para Capital
	 */
	public static final String SERIAL_CONTABLE_PARA_CAPITAL = "SERIALCONTABLECAPITAL";
	
	/**
	 * Valor del codigo del Serial Contable Para Abono de Capital
	 */
	public static final String SERIAL_CONTABLE_PARA_ABONO_CAPITAL = "SERIALCONT_CAPITAL_ABONO";
	
	/**
	 * Valor del codigo del Serial Contable Para Comision
	 */
	public static final String SERIAL_CONTABLE_PARA_COMISION = "SERIALCONTABLECOMISION";
	
	/**
	 * Valor del codigo del Serial Contable Para Abono Comision
	 */
	public static final String SERIAL_CONTABLE_PARA_ABONO_COMISION = "SERIALCONT_COMISION_ABONO";
	//----------------Par�metros de la interfaz de OPS
	/**Interfaz para la transferencia de archivos con cobro batch en ALTAIR*/
	public static final String INTERFACE_OPS = "INTERFACE_OPS";	
	public static final String RUTA_SUBASTA_ADJ_ENVIO = "RUTA_SUBASTA_ADJ_ENVIO";
	public static final String RUTA_SUBASTA_ADJ_RECEP = "RUTA_SUBASTA_ADJ_RECEP";
	public static final String RUTA_SUBASTA_ADJ_RESPALDO = "RUTA_SUBASTA_ADJ_RESPALDO";
	public static final String NOMBRE_ARCH_SUBASTA_ADJ = "NOMBRE_ARCH_SUBASTA_ADJ";
	
	public static final String RUTA_SITME_ADJ_ENVIO = "RUTA_SITME_ADJ_ENVIO";
	public static final String RUTA_SITME_ADJ_RECEP = "RUTA_SITME_ADJ_RECEP";
	public static final String RUTA_SITME_ADJ_RESPALDO = "RUTA_SITME_ADJ_RESPALDO";
	public static final String NOMBRE_ARCH_SITME_ADJ = "NOMBRE_ARCH_SITME_ADJ";
	public static final String DIRECCION_SERVIDOR_FTP_OPS = "DIRECCION_FTP_OPS";
	public static final String DIRECCION_FTP_INTER = "DIRECCION_FTP_INTER";
	public static final String RUTA_ARCHIVO_FPT = "RUTA_ARCHIVO_FPT";
	public static final String NOMBRE_ARCH_INTERVE_ENVIO = "NOMBRE_ARCH_INTERVE_ENVIO";
	
	public static final String RUTA_SITMEMP_ADJ_ENVIO = "RUTA_SITMEMP_ADJ_ENVIO";
	public static final String RUTA_SITMEMP_ADJ_RECEP = "RUTA_SITMEMP_ADJ_RECEP";
	public static final String RUTA_SITMEMP_ADJ_RESPALDO = "RUTA_SITMEMP_ADJ_RESPALDO";
	public static final String NOMBRE_ARCH_SITMEMP_ADJ = "NOMBRE_ARCH_SITMEMP_ADJ";
	
	/**Parametros de interfaz para transaferencia de archivos batch en ALTAIR para Cuentas Nacionales en Moneda Extranjera*/
	//VENTA TITULOS
	public static final String RUTA_VENTA_MONEDA_EXT_ENVIO = "RUTA_VENTA_MON_EXT_ENVIO";
	public static final String RUTA_VENTA_MONEDA_EXT_RECEP = "RUTA_VENTA_MON_EXT_RECEP";
	public static final String RUTA_VENTA_MONEDA_EXT_RESP = "RUTA_VENTA_MON_EXT_RESP";
	
	//PAGO CUPONES
	public static final String RUTA_CUPON_MONEDA_EXT_ENVIO = "RUTA_CUPON_MON_EXT_ENVIO";
	public static final String RUTA_CUPON_MONEDA_EXT_RECEP = "RUTA_CUPON_MON_EXT_RECEP";
	public static final String RUTA_CUPON_MONEDA_EXT_RESP = "RUTA_CUPON_MON_EXT_RESP";
	
	//SITME	
	public static final String RUTA_SITME_MONEDA_EXT_ENVIO = "RUTA_SIT_MON_EXT_ENVIO";
	public static final String RUTA_SITME_MONEDA_EXT_RECEP = "RUTA_SIT_MON_EXT_RECEP";
	public static final String RUTA_SITME_MONEDA_EXT_RESP = "RUTA_SIT_MON_EXT_RESP";
	
	//SITME	EMPRESARIAL
	public static final String RUTA_SITMEMP_MONEDA_EXT_ENVIO = "RUTA_SITMEMP_MEXT_ENVIO";
	public static final String RUTA_SITMEMP_MONEDA_EXT_RECEP = "RUTA_SITMEMP_MEXT_RECEP";
	public static final String RUTA_SITMEMP_MONEDA_EXT_RESP = "RUTA_SITMEMP_MEXT_RESP";
	
	//SUBASTA DIVISAS	
	public static final String RUTA_SUB_DIVISAS_ENVIO = "RUTA_SUB_DIVISAS_ENVIO";
	public static final String RUTA_SUB_DIVISAS_RECEP = "RUTA_SUB_DIVISAS_RECEP";
	public static final String RUTA_SUB_DIVISAS_RESP = "RUTA_SUB_DIVISAS_RESP";
	
	//CONCILIACION RECEPCION
	public static final String RUTA_CONC_RETENCION_ENVIO = "RUTA_CONCIL_RET_ENVIO";
	public static final String RUTA_CONC_RETENCION_RECEP = "RUTA_CONCIL_RET_RECEP";
	public static final String RUTA_CONC_RETENCION_RESP = "RUTA_CONCIL_RET_RESPALDO";
	
	//Parametro de direccion de archivo para moneda extranjera producto SITME
	public static final String NOMBRE_ARCH_CONC_RET = "NOMBRE_ARCH_CONCIL_RET";
	
	//Parametro de direccion de archivo para moneda extranjera producto SITME
	public static final String NOMBRE_ARCH_SITME_MONEDA_EXT = "NOMBRE_ARCH_SIT_MON_EXT";
	
	//Parametro de direccion de archivo para moneda extranjera producto SITME
	public static final String NOMBRE_ARCH_SITMEMP_MONEDA_EXT = "NOMBRE_ARCH_SITMEMP_MEXT";
	
	//Parametro de direccion de archivo para moneda extranjera producto SUBASTA	
	public static final String NOMBRE_ARCH_VENTA_MONEDA_EXT = "NOMBRE_ARCH_VENTA_MON_EXT";
	
	//Parametro de direccion (Altair) de archivo para moneda extranjera Pago Cupon	
	public static final String NOMBRE_ARCH_CUPON_MONEDA_EXT = "NOMBRE_ARCH_CUPON_MON_EXT";
	
	//Parametro de direccion (Altair) de archivo para moneda extranjera Envio DICOM
	public static final String NOMBRE_ARCH_DICOM_ENVIO = "NOMBRE_ARCH_DICOM_ENVIO";
	
	//Parametro de direccion (Altair) de archivo para moneda extranjera Envio DICOM
	public static final String NOMBRE_ARCH_CRUCE_DICOM = "NOMBRE_ARCH_CRUCE_DICOM";
	
	/**
	 * Parametros de busqueda interfaz DICOM con Mainframe
	 * */
//agregado por ALEXANDER RINCON NM11383	
//INTERFAZ PARA CERTIFICADO EN ORO PARAMETROS DE BASE DE DATOS
	public static final String INTERFACE_ORO = "ORO";
	public static final String JORNADA_ORO = "JORNADA_ORO";	
	public static final String MONEDA_SUBASTA_ORO = "MONEDA_SUBASTA_ORO";
	public static final String RUTA_ORO_RECEP = "RUTA_ORO_RECEP";
	public static final String RUTA_ORO_RESP = "RUTA_ORO_RESP";
	public static final String RUTA_ORO_ENVIO = "RUTA_ORO_ENVIO";	
	public static final String REENVIO_OP_PREAPROBADAS_ORO = "REENVIO_ARCH_PREAPR_ORO";	
	//Parametro de direccion (Altair) de archivo para moneda extranjera Envio DICOM
	public static final String NOMBRE_ARCH_ORO_ENVIO = "NOMBRE_ARCH_ORO_ENVIO";
	//Parametro de direccion (Altair) de archivo para moneda extranjera Envio ORO
	public static final String NOMBRE_ARCH_CRUCE_ORO = "NOMBRE_ARCH_CRUCE_ORO";
	//INTERFAZ PARA CERTIFICADO EN MENUDEO PARAMETROS DE BASE DE DATOS
	
	public static final String INTERFACE_MENUDEO = "MENUDEO";
	public static final String INTERFACE_HISTORICO = "HISTORICO";
	public static final String INTERFACE_GENERAl = "GENERAL";
	public static final String JORNADA_MENUDEO = "JORNADA_MENUDEO";
	public static final String FECHA_OPERACION = "FECHA_OPERACION";
	public static final String MONEDA_SUBASTA_MENUDEO = "MONEDA_SUBASTA_MENUDEO";
	public static final String RUTA_MENUDEO_RECEP = "RUTA_MENUDEO_RECEP";
	public static final String RUTA_MENUDEO_RESP = "RUTA_MENUDEO_RESP";
	public static final String RUTA_MENUDEO_ENVIO = "RUTA_MENUDEO_ENVIO";
	public static final String RUTA_MENUDEO_LOGS = "RUTA_MENUDEO_LOGS";
	public static final String RUTA_MENUDEO_CONCILIACION = "RUTA_CONCILIACION";
	public static final String RUTA_MENUDEO_CONCILIACION_RESP = "RUTA_CONCILIACION_RESP";
		
		
	//Interfaz Intervencion Bancaria
	public static final String TIPO_ENVIO = "TIPO_ENVIO";
	
	/**Grupo de las interfaces*/
	public static final String INTERFACE_ESTADISTICA = "INTERFACE_ESTADISTICA";
	public static final String INTERFACE_CARMEN = "INTERFACE_CARMEN";
	public static final String INTERFACE_BCV = "INTERFACE_BCV";
	public static final String INTERFACE_BDV = "BCV";
	
	//INTERFAZ PARA INTERVENCIONBANCARIA PARAMETROS DE BASE DE DATOS
	public static final String INTERFACE_INTERVENCION_BANCARIA = "INTERVENCION";
	public static final String PORCENTAJE_INVENTARIO = "PORCENTAJE_INVENTARIO";
	public static final String RUTA_ARCHIVO_INVETARIO = "RUTA_ARCHIVO_INVETARIO";
	public static final String RUTA_INVETARIO = "RUTA_INVETARIO";
	
	public static final String DIAS_ENTREGA = "DIAS_ENTREGA";
	public static final String DIAS_ENTREGA_INV = "DIAS_ENTREGA_INV";
	public static final String ESTATUS_INVENTARIO = "ESTATUS_INVENTARIO";
	public static final String JORNADA_INTERVENCION_BANCARIA = "JORNADA_INTERVENCION";	
	public static final String MONEDA_INTERVENCION_BANCARIA = "MONEDA_INTERVENCION_MENUDEO";
	public static final String RUTA_INTERVENCION_BANCARIA_RECEP = "RUTA_INTERVENCION_RECEP";
	public static final String RUTA_INTERVENCION_BANCARIA_RESP = "RUTA_INTERVENCION_RESP";
	public static final String RUTA_INTERVENCION_BANCARIA_ENVIO = "RUTA_INTERVENCION_ENVIO";
	public static final String RUTA_INTERVENCION_BANCARIA_LOGS = "RUTA_INTERVENCION_LOGS";
	
	
	//Interfaz de mesa de cambio
	public static final String INTERFACE_MESA_CAMBIO = "MESA_CAMBIO";
	public static final String JORNADA_MESA_CAMBIO = "JORNADA_MESA_CAMBIO";	
	//Grupo de parametros fechas
	public static final String FECHAS_CAMPOS_DINAMICOS = "FECHAS_CAMPOS_DINAMICOS";
	
	/**Par�metros generales de las interfaces*/
	public static final String RUTA_ARCHIVO = "RUTA-ARCHIVO";
	public static final String RUTA_FTP = "RUTA-FTP";
	public static final String SERVIDOR = "SERVIDOR";
	
	/**Par�metros generales de las interfaces de CARMEN*/
	public static final String RUTA_ARCHIVO_ENTRADA = "RUTA-ARCHIVO-ENTRADA";
	public static final String RUTA_ARCHIVO_SALIDA = "RUTA-ARCHIVO-SALIDA";
	
	/**C�digo del cliente en CARMEN perteneciente a la contraparte de BDV*/
	public static final String CUSTOMER_NUMBER_BDV = "CUSTOMER-NUMBER-BDV";
	
	/**
	/**
	 * Dias Validaci�n Ultra Temprano
	 */
	public static final String DIAS_VALIDACION_ULTRA_T="DIAS_VALIDACION_ULTRA_T";
	
	/**
	 * Indicador de activaci�n de la Contingencia
	 */
	public static final String INDICADOR_CONTINGENCIA="INDICADOR_CONTINGENCIA";	
	
	
	public static String MONEDA_BS_REPORTERIA="MONEDA_BS_REPORTERIA";
	
	/** GRUPO DE PARAMETROS PARA EL ENVIO DE CORREOS */
	
	/** Nombre del grupo de parametros para el envio de correos*/
	public static final String ENVIO_CORREOS = "ENVIO_CORREOS";
	
	/**
	 * Nombre del parametro con la ruta del directorio contenedor de archivos vinculados al envio de correos
	 */
	public static final String DESTINATION_PATH = "DESTINATION_PATH";
	
	/**
	 * Nombre del archivo con la cola de correos a enviar
	 */
	public static final String DIR_COLA_CORREOS = "DIR_COLA_CORREOS";
	
	/**
	 * Nombre del archivo con el registro de los correos enviados
	 */
	public static final String DIR_ENVIADOS = "DIR_ENVIADOS";
	
	/**
	 * Nombre del archivo con el registro de los correos no enviados
	 */
	public static final String DIR_NO_ENVIADOS = "DIR_NO_ENVIADOS";
	
	/**
	 * Nombre del archivo temporal con la informacion del asunto de los correos a enviar
	 */
	public static final String ARCH_TMP_ASUNTO = "ARCH_TMP_ASUNTO";
	
	/**
	 * Nombre del archivo temporal con la informacion del cuerpo de los correos a enviar
	 */
	public static final String ARCH_TMP_CUERPO = "ARCH_TMP_CUERPO";
	
	/**
	 * Nombre del archivo temporal con la informacion del destinatario de los correos a enviar
	 */
	public static final String ARCH_TMP_DESTINATARIO = "ARCH_TMP_DESTINATARIO";
	
	/**
	 * Nombre del archivo temporal con la informacion del path destinado al envio de correos
	 */
	public static final String ARCH_TMP_PATH = "ARCH_TMP_PATH";
	
	/**
	 * Nombre del script de envio de correos
	 */
	public static final String SCRIPT_NAME = "SCRIPT_NAME";
	
	/**
	 * Nombre del script de envio de correos
	 */
	public static final String SEPARADOR_ARCH = "SEPARADOR_ARCH";
	
	/**
	 * Estatus de los correos enviados
	 */
	public static final String STATUS_ENVIADO = "STATUS_ENVIADO";
	
	/**
	 * Estatus de los correos no enviados
	 */
	public static final String STATUS_NO_ENVIADO = "STATUS_NO_ENVIADO";
	
	/**
	 * Estatus de los correos invalidos (no enviados por informacion invalida)
	 */
	public static final String STATUS_INVALIDO = "STATUS_INVALIDO";
	
	/**
	 * Estatus de los correos precargados para su confirmacion de envio
	 */
	public static final String STATUS_PRECARGADO = "STATUS_PRECARGADO";
	
	/**
	 * Estatus de los correos cargados confirmados para su envio
	 */
	public static final String STATUS_CARGADO = "STATUS_CARGADO";
	
	/**
	 * Tipo de Destinatario: Funcional
	 */
	public static final String DEST_FUNCIONAL = "DEST_FUNCIONAL";

	/**
	 * Tipo de Destinatario: Cliente
	 */
	public static final String DEST_CLIENTE = "DEST_CLIENTE";
	
	/**
	 * Nombre del directorio donde se almacenan los archivos temporales
	 */
	public static final String DIR_TEMP = "DIR_TEMP";
	
	/**
	 * Nombre del directorio donde se almacenan los logs relacionados con el envio de correos
	 */
	public static final String DIR_LOG = "DIR_LOG";
	
	/**
	 * Nombre del directorio donde se almacenan los logs relacionados con el envio de correos
	 */
	public static final String DIR_COLA_RESPALDO = "DIR_COLA_RESPALDO";
	
	/**
	 * Nombre del directorio donde se almacena el archivo shell script que realiza el envio de correos
	 */
	public static final String DIR_SH = "DIR_SH";
	
	//NM29643 infi_TTS_466 20/07/2014
	/**
	 * Nombre de la etiqueta del cuerpo de la plantilla que define donde se reemplaza el bloque de texto a iterar por cruce
	 */
	public static final String ETIQUETA_BLOQ_CRUCE = "ETIQUETA_BLOQ_CRUCE";
	
	/**
	 * Mensaje a mostrar en la toma de ordenes Subasta Divisas por Red Comercial en caso de no cumplir con la banda de tasas propuestas configuradas en la Unidad de Inversi�n
	 */
	public static final String MSJ_TASA_PROPUESTA_SUBDIV = "MSJ_TASA_PROPUESTA_SUBDIV";
	
	/**
	 * Indica si Rentabilidad se encuentra inactivo (1 activo, 0 inactivo). ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16
	 */
	public static final String RENTABILIDAD_ONLINE = "RENTABILIDAD_ONLINE";

	//NM26659 infi_TTS-514_DICOM 18/05/2017
	/**
	 * Parametros de busqueda interfaz DICOM con Mainframe
	 * */
	public static final String INTERFACE_DICOM = "DICOM";
	
	public static final String INTERFACE_DICOM_ID = "133"; 
	
	public static final String RUTA_DICOM_OFERTA_RECEP = "RUTA_DICOM_OFERTA_RECEP";
	
	public static final String RUTA_ORO_OFERTA_RECEP = "RUTA_ORO_OFERTA_RECEP";
	
	public static final String RUTA_DICOM_OFERTA_RESP = "RUTA_DICOM_OFERTA_RESP";
	
	public static final String RUTA_ORO_OFERTA_RESP = "RUTA_ORO_OFERTA_RESP";
	
	public static final String RUTA_DICOM_DEMANDA_RECEP = "RUTA_DICOM_DEMANDA_RECEP";
	
	public static final String RUTA_ORO_DEMANDA_RECEP = "RUTA_ORO_DEMANDA_RECEP";
	
	public static final String RUTA_DICOM_DEMANDA_RESP = "RUTA_DICOM_DEMANDA_RESP";
	
	public static final String RUTA_ORO_DEMANDA_RESP = "RUTA_ORO_DEMANDA_RESP";
	
	public static final String NOMBRE_DICOM_OFERTA = "NOMBRE_ARCH_ANUL_CONV_36";
	
	public static final String JORNADA_DICOM = "JORNADA_DICOM";
	
	public static final String JORNADA_FECHA_INICIO = "JORNADA_FECHA_INICIO";
	
	public static final String JORNADA_FECHA_FIN = "JORNADA_FECHA_FIN";
	
	public static final String JORNADA_ENVIO_MANUAL = "JORNADA_ENVIO_MANUAL";
	
	public static final String CRUCE_DICOM_MANUAL = "CRUCE_DICOM_MANUAL";
	
	public static final String CRUCE_ORO_MANUAL = "CRUCE_ORO_MANUAL";
	
	public static final String JORNADA_RECEPCION_MANUAL = "JORNADA_RECEP_MANUAL";
	
	public static final String RUTA_DICOM_RESP = "RUTA_DICOM_RESP";
	
	
	
	public static final String RUTA_DICOM_RECEP = "RUTA_DICOM_RECEP";
	
	public static final String RUTA_DICOM_ENVIO = "RUTA_DICOM_ENVIO";
	
	//TTS-546-DICOM_INTERBANCARIO NM26659_27/01/2018
	public static final String RUTA_CRUCE_DICOM_RECEP = "RUTA_CRUCE_OP_DICOM_RECEP";	
	public static final String RUTA_CRUCE_DICOM_ENVIO = "RUTA_CRUCE_OP_DICOM_ENVIO";
	public static final String RUTA_CRUCE_DICOM_RESP = "RUTA_CRUCE_OP_DICOM_RESP";

	//TTS-546-DICOM_INTERBANCARIO NM26659_27/01/2018
	public static final String RUTA_CRUCE_ORO_RECEP = "RUTA_CRUCE_OP_ORO_RECEP";
	public static final String RUTA_CRUCE_ORO_ENVIO = "RUTA_CRUCE_OP_ORO_ENVIO";
	
	//TTS-546-DICOM_INTERBANCARIO NM26659_27/01/2018	
	public static final String RUTA_CRUCE_ORO_RESP = "RUTA_CRUCE_OP_ORO_RESP";
	
	//TTS-546-DICOM_INTERBANCARIO NM26659_07/02/2018
	public static final String RUTA_OP_INTERBANCARIAS_DICOM_RECEP = "RUTA_OP_INTBN_DICOM_RECEP";		
	public static final String RUTA_OP_INTERBANCARIAS_DICOM_RESP =  "RUTA_OP_INTBN_DICOM_RESP";
	
	//nm36635 DICOM_ORO
	public static final String RUTA_OP_INTERBANCARIAS_ORO_RECEP = "RUTA_OP_INTBN_DICOM_RECEP";
	public static final String RUTA_OP_INTERBANCARIAS_ORO_RESP =  "RUTA_OP_INTBN_DICOM_RESP";
	
	public static final String MONEDA_SUBASTA_DICOM = "MONEDA_SUBASTA_DICOM";
	
	//Parametros asociados a directorios de liquidacion DICOM
	public static final String RUTA_DICOM_LIQUIDAC_ENVIO = "RUTA_DICOM_LIQUIDAC_ENVIO";
	
	public static final String RUTA_DICOM_LIQUIDAC_RECEP = "RUTA_DICOM_LIQUIDAC_RECEP";
	
	public static final String RUTA_DICOM_LIQUIDAC_RESP = "RUTA_DICOM_LIQUIDAC_RESP";
	//TTS-546 - DICOM INTERBANCARIO_ Se agregar parametro para reenvio de archivo de operaciones verificada
	public static final String REENVIO_OP_PREAPROBADAS_DICOM = "REENVIO_ARCH_PREAPR_DICOM";
	
	//nm36635 DICOM_MULTIMONEDA
//	public static final String UNDINV_MONEDA_DEMANDA = "UNDINV_MONEDA_DEMANDA";
	
	public static final String FECHA_VALOR_MENUDEO = "FECHA_VALOR_MENUDEO";
	public static final String ACTIVAR_FECHA_MANUAL = "ACTIVAR_FECHA_MANUAL";
	public static final String FECHA_HIST_MN = "FECHA_HIST_MN";
	public static final String VALIDA_USUARIO_INTERVEN = "VALIDA_USUARIO_INTERVEN";
	public static final String FECHA_VALOR_INTERVENCION = "FECHA_VALOR_INTERVEN";
}
