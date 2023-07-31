package com.bdv.infi.logic.interfaces;

import java.text.SimpleDateFormat;

/**
 * Lista de constantes usadas dentro de la aplicación
 */
public class ConstantesGenerales {
	
	/**
	 * Raíz URL de la ruta que contiene las imágenes
	 */
	public static String RUTA_IMAGENES="";	

	/**Indica un registro activo*/
	public static final int STATUS_ACTIVO = 1;
	
	/**Indica la fecha de la Reconversion*/
	public static final String FECHA_RECONVERSION = "20180325";
	
	/**Indica un registro inactivo*/
	public static final int STATUS_INACTIVO = 0;
	
	/**Usado en los campos indicadores que reflejan un si*/
	public static final int VERDADERO = 1;
	
	/**Usado en los campos indicadores que reflejan un no*/	
	public static final int FALSO = 0;
	
	/**Indica que es cartera propia*/	
	public static final int IN_CARTERA_PROPIA_TRUE =1;
	
	public static final String CONTROL_DE_CAMBIO ="CONTROL_DE_CAMBIO";
	
/**Indica Tipo de envio para intervencion*/
	
	public static final String CODIGO_TIPO_ENVIO ="0";
	public static final String CODIGO_TIPO_ENVIO_LOTE ="1";
	
	/**Indica el formato de fecha usado en a aplicaci&oacute;n*/
	public static final String FORMATO_FECHA_RRRR = "dd-MM-rrrr";
	public static final String FORMATO_FECHA_MENUDEO = "DD-MM-AAAA";
	public static final String FORMATO_FECHA = "dd-MM-yyyy";
	public static final String FORMATO_FECHA1 = "dd-mm-yyyy";
	public static final String FORMATO_FECHA_HORA = "dd-MM-yyyy HH12:mi:ss";
	public static final String FORMATO_FECHA3 = "yyyy-MM-dd";
	public static final String FORMATO_FECHA4 = "dd-MM-yy";
	public static final String FORMATO_FECHA2 = "dd/MM/yyyy";
	public static final String FORMATO_FECHA_LIQUIDACION = "dd-MM-yyyy hh:mm:ss";
	public static final String SEPARADOR_FECHA = "-";
	public static final String FORMATO_FECHA_HORA_JAVA = "dd-MM-yyyy HH:mm:ss";
	public static final String FORMATO_FECHA_HORA_BD = "dd-MM-yyyy HH24:mm:ss";
	public static final String FORMATO_FECHA_HORA_BD2 = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMATO_FECHA_HORA_BD3 = "yyyy-MM-dd HH:mm:ss";
	
	/**Acciones de intervencion bancaria*/
	public static final String INICIO_SESSION = "/intervencion/api/auntenticacion/iniciarSesion";
	public static final String OPERACION_ENTRE_BANCOS= "/intervencion/api/notificaOperaciones/interbancarias";
	public static final String OPERACIONES_ACTIVAS= "/intervencion/api/intervencion/Activa";
	public static final String OPERACION_VENTAS= "/intervencion/api/notificaOperaciones/Venta";
	
	
	
	/**Indica el formato de fecha usado para comparar con la base de datos el horario de un blotter*/
	public static final String FORMATO_FECHA_BLOTTER = "dd/MM/yyyy hh:mm:ss,SSSSSS a";
	public static final String FORMATO_HORA_JAVA = "HH:mm:ss";
	
	/**Indica el formato de fecha usado en a aplicaci&oacute;n*/
	public static final String FORMATO_FECHA_SYSDATE = "DD-MM-RRRR";
	public static final String FORMATO_FECHA_SYSDATE_2 = "DD/MM/RRRR";
	public static final String FORMATO_FECHA_SYSDATE_3 = "DD/MM/RRRR hh:mi:ss am";
	
	/**Indica el formato de fecha usado en a aplicaci&oacute;n*/
	public static final String FORMATO_FECHA_HORA_SYSDATE = "DD-MM-RRRR hh:mi:ss";
	
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
	/**Indica el formato de fecha usado para nombres de archivos*/
	public static final String FORMATO_FECHA_FILE = "ddMMyyyy_HHmmss";
	
	/**Indica el formato de fecha usado para registros del archivo de la cola de correos*/
	public static final String FORMATO_FECHA_REGISTRO = "dd/MM/yyyy HH:mm:ss";
	
	public static final String FORMATO_FECHA_SIN_SEPARADOR= "ddMMyyyy";
	/*NM26659 TTS-541_DICOM 28/05/2017 Inclusion formato de fecha años mes dia sin separador */
	public static final String FORMATO_FECHA_SIN_SEPARADOR_2= "yyyyMMdd";
	
	/**Indica el formato para las horas*/
	public static final String FORMATO_HORA = "hh:mi:ss am";
	public static final String FORMATO_HORA_24 = "HH24:mi:ss";
	
	//NM29643 - INFI_TTS_443 08/04/2014: Requerido para creacion de Instruccion Cta Nac Dolares (SICAD II)
	/**Banco de Venezuela*/
	public static String BCO_DE_VENEZUELA = "BANCO DE VENEZUELA";
	
	/**RIF del banco de Venezuela usado para la reporteria*/
	public static String RIF = "G-020009997-6";
	
	/**Imagen del banco de Venezuela*/
	public static final String IMAGEN_BDV = "infi_gn_logo_bdv.jpg";
	
	/**Imagen gubernamental para Banco de Venezuela*/
	public static final String IMAGEN_BDV2 = "infi_gn_logo_bdv2.jpg";
	
	/**Extension de los documentos guardados en la aplicacion*/
	public static final String EXTENSION_DOC_RTF = ".rtf";
	
	/**Extension de los documentos guardados en la aplicacion*/
	public static final String EXTENSION_DOC_HTM = ".htm";
	
	/**Extension de los documentos guardados en la aplicacion*/
	public static final String EXTENSION_DOC_HTML = ".html";
	
	/**Extension del archivo para lectura en adjudicacion*/
	public static final String EXTENSION_DOC_XLS = ".xls";
	
	public static final String EXTENSION_DOC_CSV = ".csv";
	
	/**Extension del archivo para lectura en adjudicacion*/
	public static final String EXTENSION_DOC_TXT = ".txt";
	
	/**Indica un registro en status Registrada*/
	public static final String STATUS_REGISTRADO = "R";
	
	/**Indica un registro en status Aprobado*/
	public static final String STATUS_APROBADO = "A";
	
	/**Indica un registro en status Registrada*/
	public static final double RANGO_MINIMO = 0;
	
	/**Indica un registro en status Aprobado*/
	public static final double RANGO_MAXIMO = 99999999;
	
	//NM32454 LPEREZ SIMADI_TAQUILLA
	/**Indica los tipos de operacion del blotter */
	public static final String BLOTTER_TIPO_OPERACION_ELEC = "1";
	public static final String BLOTTER_TIPO_OPERACION_EFEC = "2";
	
	/**Indica status de una operacion de una Orden*/
	public static final String STATUS_EN_ESPERA = "EN ESPERA";
	public static final String STATUS_APLICADA = "APLICADA";
	public static final String STATUS_RECHAZADA = "RECHAZADA";
	public static final String STATUS_CANCELADA = "CANCELADA";
	public static final String EN_ESPERA_LIQUIDACION = "EN ESPERA LIQUIDACION";
	public static final String STATUS_PENDIENTE_INSTRUCCION = "PENDIENTE INSTRUCCION";
	
	
	//Incluido en requerimiento TTS_443 NM26659_24/03/2014 - Estatus asociados a operaciones de cruces
	public static final String STATUS_CRUZADA = "CRUZADA";
	public static final String STATUS_INVALIDA = "INVALIDA";
	public static final String STATUS_CERRADA = "CERRADA";
	public static final String STATUS_NO_CRUZADA = "NO_CRUZADA";
	
	//Incluido para requerimiento de envio a bvc menudeo
	
	public static final String STATUS_ENVIADAS = "";

	
	//Nm229643 TTS_443 - Estatus asociados a operaciones de cruces
	
	
	/**Indica el estatus de un archivo*/
	public static final String GENERADO = "GENERADO";
	public static final String RECIBIDO = "RECIBIDO";
	public static final String CERRADO = "CERRADO";
	
	/**Indica el formas de pago para la venta de t&iacute;tulos*/
	public static final String FORMA_PAGO_CTA_INTERN = "Transferencia a Cuenta Internacional";
	public static final String FORMA_PAGO_CTA_NAC = "Cuenta Nacional";
	public static final String FORMA_PAGO_CHEQUE = "Cheque";
	public static final String FORMA_PAGO_OPERACION_CAMBIO = "Operaci&oacute;n de Cambio";
	public static final String FORMA_PAGO_CTA_NACIONAL_DOLARES = "Transferencia a Cuenta Nacional en Dólares";

	/**Indica un status Adjudicada de la unidad de inversion*/
	public static final String UNINV_ADJUDICADA = "ADJUDICADA";
			
	/**Indican tipos de Instrumentos financieros*/
	public static final String INST_TIPO_SUBASTA = "SUBASTA";
	public static final String INST_TIPO_SUBASTA_COMPETITIVA = "SUBASTA_C";
	public static final String INST_INTERVENCION = "INTERVENCION";
	public static final String INST_TIPO_INVENTARIO = "INVENTARIO";
	public static final String INST_TIPO_INVENTARIO_CON_PRECIO = "INVENTARIO_CP";
	public static final String INST_TIPO_SITME = "TITULOS_SITME";
	public static final String INST_FINANCIERO_SITME_CLAVENET_P="CLAVENET_P";	
	public static final String INST_FINANCIERO_SITME_CLAVENET_E="CLAVENET_E";
	public static final String INST_FINANCIERO_SIMADI_TAQUILLA= "SIMADI_TAQUILLA";

	//Instrumento financiero asociado a la subasta de divisas
	//public static final String INST_FINANCIERO_SUBASTA_DIVISAS="SUBASTA_DIVISAS";
	
	public static final String ID_TIPO_PRODUCTO_SITME = "SITME";
	public static final String ID_TIPO_PRODUCTO_INTERVENCION = "INTERVEN";
	public static final String ID_TIPO_PRODUCTO_SUBASTA = "SUBASTA";
	public static final String ID_TIPO_PRODUCTO_SUBASTA_DIVISA = "SUB_DIVISA";
	public static final String ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL = "SUB_DIV_P";
	public static final String ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL = "SICAD2PER";
	public static final String ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL = "SICAD2RED";
	public static final String ID_TIPO_PRODUCTO_DICOM = "DICOM";
	
	//nm36635 //Instrumento financiero asociado al oro
	public static final String ID_TIPO_PRODUCTO_ORO = "ORO";
//	NM29643 - infi_TTS_466_Calidad 03/10/2014 Se agrega el tipo de producto "GENERAL"
	public static final String ID_TIPO_PRODUCTO_GENERAL = "GENERAL";
	
	/**Indican metodo de cupones de Instrumentos financieros*/
	public static final String RENDIMIENTO = "PRECIO";
	public static final String TASA = "CUP&Oacute;N";
	
	//Constantes de OPICS		
	/**Indica el grupo para parametros de Opics*/
	public static final String INTERFACE_OPICS = "INTERFACE OPICS";				
	//Constantes de OPICS	
	
	/**Indica el formato de fecha y hora usado en los servicios web*/
	public static final String FORMATO_FECHA_WEB_SERVICES = "ddMMyyyy";
	public static final String FORMATO_HORA_WEB_SERVICES = "HHMIAM";

	/**Indica el cliente del Banco de Venezuela para ser utilizado en la Orden que se envia al Banco Central*/
	public static final long CLIENTE_BANCO_VENEZUELA = 0;
	
	
	/**Indica el tipo de instrucciones de pago*/
	public static final String CHEQUE = "CHEQUE";
	public static final String ALTAIR = "ALTAIR";
	public static final String SWIFT = "SWIFT";
	
	public static final String INTERFACE_OPICS_ENVIO = "INTERFACE OPICS ENVIO";
	/*Constantes usadas para la obtenci&oacute;n de las secuencias num&eacute;ricas en la tabla MSC_SEQUENCE_NUMBER*/
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_204_ORDENES*/
	public static final String SECUENCIA_ORDENES = "INFI_TB_204_ORDENES";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_207_ORDENES_OPERACION*/
	public static final String SECUENCIA_ORDENES_OPERACION = "INFI_TB_207_ORDENES_OPERACION";	
		
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_210_ORDENES_REQUISITO*/
	public static final String SECUENCIA_ORDENES_REQUISITOS= "INFI_TB_210_ORDENES_REQUISITO";	
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_208_ORDENES_DOCUMENTOS*/
	public static final String SECUENCIA_ORDENES_DOC = "INFI_TB_208_ORDENES_DOCUMENTOS";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_024_TRANSACCION_DOCS*/
	public static final String SECUENCIA_TRANSACCION_DOC = "INFI_TB_024_TRANSACCION_DOCS";	
			
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_029_TRANSACC_TRNFIN*/
	public static final String SECUENCIA_TRANSACC_TRNFIN = "INFI_TB_029_TRANSACC_TRNFIN";	

	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_037_CTES_TRN_TRNFIN*/
	public static final String SECUENCIA_CONTROL_ARCHIVOS = "INFI_TB_803_CONTROL_ARCHIVOS";
	
	//NM29643 - INFI_TTS_423: Nueva Tabla para el control de ciclos
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_037_CTES_TRN_TRNFIN*/
	public static final String SECUENCIA_CONTROL_CICLOS = "INFI_TB_817_CONTROL_CICLOS";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_037_CTES_TRN_TRNFIN*/
	public static final String SQ_CONTROL_CICLOS = "INFI_SQ_817";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_037_CTES_TRN_TRNFIN*/
	public static final String SQ_ENVIO_MAIL = "INFI_SQ_228";

	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_102_BLOTER*/
	public static final String SECUENCIA_BLOTER = "INFI_TB_102_BLOTER";	
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_011_INDICADORES*/
	public static final String SECUENCIA_INDICADORES = "INFI_TB_011_INDICADORES";
		
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_101_INST_FINANCIEROS*/
	public static final String SECUENCIA_INST_FINANCIEROS = "INFI_TB_101_INST_FINANCIEROS";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_017_ROLES*/
	public static final String SECUENCIA_ROLES = "INFI_TB_017_ROLES";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_700_TIPO_BLOQUEO*/
	public static final String SECUENCIA_TIPO_BLOQUEO = "INFI_TB_700_TIPO_BLOQUEO";

	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_018_VEHICULOS*/
	public static final String SECUENCIA_VEHICULOS = "INFI_TB_018_VEHICULOS";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_022_TIPO_GARANTIA*/
	public static final String SECUENCIA_TIPO_GARANTIA= "INFI_TB_022_TIPO_GARANTIA";
					
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_039_BENEFICIARIOS*/
	public static final String SECUENCIA_BENEFICIARIO = "INFI_TB_039_BENEFICIARIOS";	
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_016_EMPRESAS*/
	public static final String SECUENCIA_EMPRESAS= "INFI_TB_016_EMPRESAS";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_807_PROCESOS*/
	public static final String SECUENCIA_PROCESOS= "INFI_TB_807_PROCESOS";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_031_VEHICULO_ROL*/
	public static final String SECUENCIA_VEHICULO_ROL= "INFI_TB_031_VEHICULO_ROL";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_201_CTES*/
	public static final String SECUENCIA_CLIENTE= "INFI_TB_201_CTES";
	
	/**Indica la clave para buscar la secuencia relacionada a el n&uacute;mero cuenta custodia del cliente*/
	public static final String SECUENCIA_CTA_CUSTODIA= "CTA_CUSTODIA";
	
	/**Indica la clave para buscar la secuencia relacionada a los campos dinámicos*/
	public static final String SECUENCIA_CAMPOS_DINAMICOS= "INFI_TB_036_CAMPOS_DINAMICOS";		
	
	/**Indica la clave para buscar la secuencia relacionada a los campos dinámicos*/
	public static final String SECUENCIA_UI_DOCUMENTOS= "INFI_TB_115_UI_DOC";		
	
	/**Indica la clave para buscar la secuencia relacionada a los campos dinámicos*/
	public static final String SECUENCIA_UI_COMISIONES= "INFI_TB_112_UI_COMISION";	
	
	/**Indica la clave para buscar la secuencia relacionada a los campos dinámicos*/
	public static final String SECUENCIA_UI_COMISIONES_REGLAS= "INFI_TB_113_UI_COMISION_REGLAS";
	/**Indica la clave para buscar la secuencia relacionada a los comisiones de custodia*/
	public static final String SECUENCIA_COM_CUSTODIA= "INFI_TB_040_COM_CUSTODIA";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_215_MENSAJE_OPICS*/
	public static final String SECUENCIA_MENSAJE_OPICS= "INFI_TB_215_MENSAJE_OPICS";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_216_MENSAJE_OPICS_DET*/
	public static final String SECUENCIA_MENSAJE_OPICS_DET= "INFI_TB_216_MENSAJE_OPICS_DET";		
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_215_MENSAJE_OPICS*/
	public static final String SECUENCIA_OPERACION_INTENTO= "INFI_TB_209_OPERACION_INT";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_120_TITULOS_PREC_RCMP*/
	public static final String INFI_TB_120_TITULOS_PREC_RCMP= "INFI_TB_120_TITULOS_PREC_RCMP";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_121_TIT_PREC_RCMP_HIST*/
	public static final String INFI_TB_121_TIT_PREC_RCMP_HIST="INFI_TB_121_TIT_PREC_RCMP_HIST";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_810_PROCESO_INST*/
	public static final String INFI_TB_810_PROCESO_INST="INFI_TB_810_PROCESO_INST";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_813_PROCESO_OPERACION*/
	public static final String INFI_TB_813_PROCESO_OPERACION= "INFI_TB_813_PROCESO_OPERACION";

	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_811_INST_OPERACION*/
	public  static final String INFI_TB_811_INST_OPERACION= "INFI_TB_811_INST_OPERACION";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla URL_LOG_CONFIG*/
	public static final String SECUENCIA_URL_LOG_CONFIG = "URL_LOG_CONFIG";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_811_INST_OPERACION_INST_ID*/
	public static final String INFI_TB_811_INST_OPERACION_INST_ID = "INFI_TB_811_INST_OPERACION_INST_ID";
		
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_814_CALC_MES*/
	public static final String INFI_TB_814_CALC_MES = "INFI_TB_814_CALC_MES";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_815_CALC_MES_OP*/
	public static final String INFI_TB_815_CALC_MES_DETALLE = "INFI_TB_815_CALC_MES_DETALLE";
	
	/**Indica la clave para buscar la secuencia relacionada a la INFI_TB_213_FACTURA*/
	public static final String INFI_TB_213_FACTURA = "INFI_TB_213_FACTURA";
	
	/**Indica la clave para buscar la secuencia relacionada a la INFI_TB_214_FACTURA_DETALLE*/
	public static final String INFI_TB_214_FACTURA_DETALLE = "INFI_TB_214_FACTURA_DETALLE";
	
	/**Indica la clave para buscar la secuencia relacionada a la INFI_TB_202_CTES_CUENTAS*/
	public static final String INFI_TB_202_CTES_CUENTAS = "INFI_TB_202_CTES_CUENTAS";
	
	/**Indica la clave para buscar la secuencia relacionada a la INFI_TB_007_CTAS_BIC_BANCO*/
	public static final String INFI_TB_007_CTAS_BIC_BANCO = "INFI_TB_007_CTAS_BIC_BANCO";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_210_ORDENES_REQUISITO*/	
	public static final String SECUENCIA_ORDENES_REQUISITO = "INFI_TB_210_ORDENES_REQUISITO";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_218_MENSAJES*/	
	public static final String SECUENCIA_MENSAJE = "INFI_TB_218_MENSAJE";
	
	/**Indica la clave para buscar la secuencia relacionada a la tabla INFI_TB_219_MENSAJE_DETALLE*/	
	public static final String SECUENCIA_MENSAJE_DETALLE = "INFI_TB_219_MENSAJE_DETALLE";	
	
	/**Indica la clave para buscar el tamaño del campo CED_RIF_CLIENTE de la tabla solicitudes_sitme*/
	public static final String SOLICITUDES_SITME_CED_RIF_CLIENTE = "CED_RIF_CLIENTE";
	
	/**Credenciales de usuario para ser enviado a los web services*/
	public static final String CREDENCIALES_USUARIO= "CREDENCIALES_USUARIO";
	
	/**usuario para ser enviado a los web services*/
	public static final String USUARIO_WEB_SERVICES= "USUARIO-WEB-SERVICES";
	
	/**Clave del usuario del web services encriptada*/
	public static final String CLAVE_WEB_SERVICES= "CLAVE-WEB-SERVICES";	
	
	/**
	 * Nombres de los DataSource utilizados en la aplicación
	 */
	public static final String DATASOURCE_PRINCIPAL_INFI = "datasource";
	public static final String DATASOURCE_SEGURIDAD_SEPA = "datasource-security";
	
	
	/*Tipos de mercado*/
	/**Tipo de mercado primario*/
	public static final String MERCADO_PRIMARIO= "PRIMARIO";
	/**Tipo de mercado secundario*/
	public static final String MERCADO_SECUNDARIO= "SECUNDARIO";
	
	/**Indicador de cuenta europea*/
	public static final String INDICADOR_IBAN = "IBAN ";
	
    /**Indicador de código de sucursal*/	
	public static final String CODIGO_SUCURSAL = "CODIGO_SUCURSAL";
	
	/**Recomedaciones para la creacion de documentos*/
	public static final String RECOMENDACIONES_DOCUMENTOS = "<table border='0' cellspacing='1' cellpadding='2' width='100%' class='datatable'>" +
			"<tr CLASS='formCaption'><th ALIGN='center'><FONT class='formCaptionText'>" +
			"Recomendaciones para la Sustituci&oacute;n</FONT></th></tr>" +
			"<tr><td nowrap>" +
			"<b>1.-</b>Si va a colocar formato (negritas, cursivas, etc.) a cualquiera de los campos agregado, deber&aacute; seleccionarlo completo" +
			"<br>&nbsp;&nbsp;&nbsp;&nbsp;" +
			"desde el '@' de inicio hasta el '@' de fin, para que el sistema pueda efectuar las sustituciones sin problemas.<br>" +
			"<b>2.-</b>Copie y pegue los campos disponibles que se le muestran no los tipee para evitar errores.<br>" +
			"<b>3.-</b>No olvide guardar el documento en formato .htm &oacute; .html, es el formato v&aacute;lido que acepta el sistema.<br>" +
			"<b>4.-</b>Procure que ninguna de las palabras contenidas dentro de los @ poseea el subrayado color rojo que indica " +
			"<br>&nbsp;&nbsp;&nbsp;&nbsp;"+			
			"error ortográfico, debido a que el documento al ser salvado presenta inconvenientes en la sustituciones" +
			"<br>&nbsp;&nbsp;&nbsp;&nbsp; con los datos reales." +			
			"</td></tr></table>";
	
	/**Formateo del id del tiulo para mostrar los campos disponibles para titulos y para el mapa que reemplaza dichos valores
	 * OJO tener cuidado que el alias del titulo debera ser siempre 'titu.'
	 * */
	public static final String FORMATEO_ID_TITULO = " lower(replace(replace(replace(replace(replace(replace(replace(titu.titulo_id,' ','_'),'.','_'),'/','_'),'=',''),'*',''),'-',''),'#','num')) as ";

	/**
	 * Mensaje de alerta para precios de recompra en caso de que no se hayan configurado y no se haya actualizado al dia de hoy
	 */
	public static final String MENSAJE_PRECIOS_RECOMPRA = "Verifique que los precios de recompra se encuentren configurados para los t&iacute;tulos y que se hayan aprobado y actualizado al d&iacute;a de hoy. Compruebe el que el precio se haya configurado para el tipo de producto ";
	
	public static final String MENSAJE_FECHA_VALOR = "La Fecha Valor de la Orden es Mayor a la Fecha Valor de Recompra";
		
	/**
	 * Constante para indicar control de cambio
	 */
	public static int CONTROL_CAMBIO_ACTIVO=1;	
	
	/**
	 * Mensaje de excepcion en el proceso automatico de QUATRZ
	 */
	public static String  MENSAJE="USUARIO-WEB-SERVICES / El usuario configurado en parametros de sistema no existe en SEPA...";
	
	/*Codigos de los tipos de cuenta*/
	public static String CUENTA_CORRIENTE="00";	
	
	public static String CUENTA_AHORRO="01";
	
	/**
	 * Mensaje de validacion de estatus de la contingencia
	 */
	
	public static String MENSAJE_VALIDACION_CONTINGENCIA="No puede acceder a este módulo. Debe activar el estatus de la contingencia";
	
	
	/**
	 * Constantes para el envío de mensajes carmen
	 */
	public static String MENSAJE_CARMEN_SALIDA="SALIDA";
	
	public static String MENSAJE_CARMEN_ENTRADA="ENTRADA";
	
	/*Estatus de las ordenes registradas en OPICS (CLAVENET)
	 * */
	public static String ESTATUS_ORDEN_ESPERA_RECAUDOS="ESPERA RECAUDOS";
	public static String ESTATUS_ORDEN_COBRADA="COBRADA";	
	public static String ESTATUS_ORDEN_ADJUDICADA="ADJUDICADA";
	public static String ESTATUS_ORDEN_PENDIENTE_COBRO="PENDIENTE DE COBRO";
	public static String ESTATUS_ORDEN_ENVIADA="ENVIADA";
	public static String ESTATUS_ORDEN_RECIBIDA="RECIBIDA";	
	public static String ESTATUS_ORDEN_CRUZADA="CRUZADA";
	public static String ESTATUS_ORDEN_NO_CRUZADA="NO_CRUZADA";
	
	public static String ESTATUS_ORDEN_PACTADA="PACTADA";
	public static String ESTATUS_ORDEN_NO_PACTADA="NO PACTADA";
	
	public static final int LISTAR_INFORMACION_VEHICULO=3;
	
	public static int TRANSACCION_FIJA_TOMA_ORDEN=1;
	
	public static int TRANSACCION_FIJA_COMISION=11;	
	
	public static String TICKET_CLAVENET="NRO_TICKET";
	
	public static String COMISION_CLAVENET="ID_COMISION_UI"; 
	
	public static final String LOCALIZACION_INVOCACION_EDIT_UI="Editar_UI";
	
	public static final String CLIENTE_BANCA_COMERCIAL="BANCA COMERCIAL";
	
	public static final String CLIENTE_SECUENCIA_DOCUMENTO="01";
	
	public static final String CLIENTE_INDICADOR_GRUPO="00";
	
	public static final String CODIGO_SEGMENTO="34";
	
	public static String SIGLAS_BCV="BCV";	
	
	public static final int COMMIT_REGISTROS_ADJ=250;
	
	public static final int COMMIT_REGISTROS_LIQ=250;
	
	public static final int COMMIT_REGISTROS_CARGA_OFERTAS_OPICS=250;
	
	public static final int COMMIT_REGISTROS_ADJ_EMP=250;
	public static final int MAX_VALUES_IN_CLAUSE_ORACLE=500;
	
	public static final String CODIGO_CARGOS_CTA_CTE="0670";
	public static final String CODIGO_CARGOS_CTA_AHO="1670";
	
	public static final String CODIGO_ABONOS_CTA_CTE="0770";
	public static final String CODIGO_ABONOS_CTA_AHO="1770";
	
	/*Constantes que indica si la cuenta donde se realizar la transferencia de divisa 
	 de moneda extranjera es cuenta nacional o cuanta en el exterior*/
	public static final String ABONO_CUENTA_EXTRANJERO="1";
	
	//Cuenta nacional en dolares
	public static final String ABONO_CUENTA_NACIONAL="2";
	
	//NM25287 TTS_491 SIMADI. Manejo de cuenta de abono en bolivares
	public static final String ABONO_CUENTA_NACIONAL_BS="3";
	public static final String TRANSACCION_ABONO_CTA_DOLARES="TOMA_ORDEN_CTA_DOLARES";
	
	public static final String TRANSACCION_ORDEN_PAGO="ORDEN_PAGO";
	
	public static final String OPS_MONEDA_NACIONAL="1";
	public static final String OPS_MONEDA_EXTRANJERA="2";
	
	public static final int CICLO_ABONO_CTA_NACIONAL_MONEDA_EXT=3;
	public static final int CICLO_ABONO_CUENTA_DOLARES_VENTAS=4;
	public static final int CICLO_ABONO_CUENTA_DOLARES_CUPON=5;	
	
	public static final String INSTRUCCION_SWIFT="1";
	public static final String INSTRUCCION_ABONO_CTA_NACIONAL_DOLARES="3";
	
	public static final String CANAL_SERVICIOS_BUS="infi";
	
	public static final String CANAL_CLAVENET_PERSONAL="05";
	
	public static final String CANAL_CLAVENET_EMPRESARIAL="99";
	
	public static final String CANAL_INFI="I";
	
	public static final String CANAL_CLAVENET="C";
	
	//NM25287	TTS-491 Mercado Abierto SIMADI. Manejo de canales	
	public static final String ID_CANAL_INFI="1";	
	
	public static final String ID_CANAL_CLAVENET_PERSONAL="2";
	
	//TTS-504-SIMADI Efectivo Taquilla NM25287 31/08/2015
	public static final String ID_CANAL_TAQUILLA="3";
	
	//public static final String ID_CANAL_CLAVENET_EMPRESARIAL="3"; canal inactivo
	
	//public static final String ID_CANAL_SERVICIOS_BUS="4";	canal inactivo

	public static final String LOGGER_FILE_ADJ_EMP = "WEB-INF/logs/app_log_adj.log";
	
	public static final String IDENTIFICADOR_PLANTILLA_ADJUDICACION_SUBASTA_DIVISAS="PLATILLA ADJUDICACION SUBASTA DIVISAS";
	public static final String IDENTIFICADOR_PLANTILLA_RECHAZO_SUBASTA_DIVISAS="PLATILLA RECHAZO SUBASTA DE DIVISAS";
	
	public static final String IDENTIFICADOR_PLANTILLA_ADJUDICACION_SUBASTA_DIVISAS_PERSONAL="PLANTILLA ADJUDICACION SUBASTA DIVISAS PERSONAL";	
	public static final String IDENTIFICADOR_PROCESO_ADJUDICACION_SUBASTA_DIVISAS="proceso adjudicacion";
	public static final String IDENTIFICADOR_PROCESO_RECHAZO_SUBASTA_DIVISAS="proceso rechazo";
	
	public static final String OBSERVACION_RECHAZO_INTERNO_SUBASTA_DIVISAS="RECHAZO DE OPERACION POR VERIFICACION INTERNA BDV";
	
	public static final String SIGLAS_MONEDA_LOCAL="VES";
	public static final String SIGLAS_MONEDA_DOLAR="USD";
	public static final String SIGLAS_MONEDA_EURO="EUR";
	public static final String FECHA_LECTURA="FECHA_LECTURA";
	//NM32454	27/02/2015 INFI_TTS_491_WEB_SERVICE_BCV
	public static final String CODIGO_MONEDA_ISO_USD="840";
	public static final String CODIGO_MONEDA_ISO_EUR="978";
	public static final String MONTO_MAXIMO_MENUDEO="MONTO_MAXIMO_MENUDEO";
	public static final String TIPO_DE_MOVIMIENTO_PN_COM="BUVTRC";
	public static final String TIPO_DE_MOVIMIENTO_PN_VEN="BUVTRV";//ACOMODAR A VENTA DE DIVISAS
	public static final String TIPO_DE_MOVIMIENTO_EX_COM="BUETRC";
	public static final String TIPO_DE_MOVIMIENTO_EX_VEN="BUETRV"; //ACOMODAR PARA COMPRA
	public static final String TIPO_DE_MOVIMIENTO_ANU_COM="BUANUC";
	public static final String TIPO_DE_MOVIMIENTO_ANU_VEN="BUANUV";
	public static final String TIPO_DE_MOVIMIENTO_TRANSFE_JURI="BJTRCD";//COMPRA EN TRANSFERENCIA JURIDICO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_TRANSFE_GOBI="BGTRCD";//COMPRA EN TRANSFERENCIA GOBIERNO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_GOBI="BGEFCD";//COMPRA EN EFECTIVO GOBIERNO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_TRANSFE_EXTRAN="BETRCD";//COMPRA EN TRANSFERENCIA EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_TRANSFE_VEN="BVTRCD";//COMPRA EN TRANSFERENCIA VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_EXTRAN="BEEFCD";//COMPRA EN EFECTIVO EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_V_EXTRAN="BEEFVD";//VENTA EN EFECTIVO EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_VEN="BVEFCD";//COMPRA EN EFECTIVO VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_CHEQUE_EXTRAN="BECHCD";//COMPRA EN CHEQUE EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_CHEQUE_VEN="BVCHCD";//COMPRA EN CHEQUE VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_PASS="BPEFCD";//COMPRA EN EFECTIVO PASAPORTE EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_VENT_="BECHVD";//VENTA EN CHEQUE EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_VENT_EFEC_EXTRAN="BEEFVD";//VENTA EN EFECTIVO EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_VENT_TRANF_EXTRAN="BETRVD";//VENTA EN TRANSFERENCIA EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_VENT_PAS="BPENVD";//VENTA EN ENCOMIENDA PASAPORTE EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_CHEQUE_VEN_VENT="BVCHVD";//VENTA EN CHEQUE VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_VEN_VENT="BVEFVD";//VENTA EN EFECTIVO VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_GOB_VENT="BGEFCV";//VENTA EN EFECTIVO GOBIERNO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_COMP_ENCO_EXTRAN="BEENCD";//COMPRA EN ENCOMIENDA EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_COMP_ENCO_PAS="BPENCD";//COMPRA EN ENCOMIENDA PASAPORTE EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_COMP_ENCO_VEN="BVENCD";//COMPRA EN ENCOMIENDA VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_COMP_ENCO_EXTRAN_VENT="BEENVD";//VENTA EN ENCOMIENDA EXTRANJERO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_COMP_ENCO_VEN_VENT="BVENVD";//VENTA EN ENCOMIENDA VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_TRANF_VEN_VENT="BVTRVD";//VENTA EN TRANSFERENCIA VENEZOLANO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_PAS="BPEFVD";//VENTA EN EFECTIVO PASAPORTE EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_EFEC_JUR="BJEFCD";//COMPRA EN EFECTIVO JURÍDICO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_VEN_EFEC_JUR="BJEFVD";//VENTA EN EFECTIVO JURÍDICO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_COMPRA_EFEC_GOBIERNO="BGEFCD";//COMPRA EN EFECTIVO GOBIERNO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_VENTA_EFEC_GOBIERNO="BGEFCV";//VENTA EN EFECTIVO GOBIERNO EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_VENTA_EFEC_PASAPORTE="BPEFVD";//VENTA EN EFECTIVO PASAPORTE EN BANCO UNIVERSAL
	public static final String TIPO_DE_MOVIMIENTO_COMPRA_EFEC_PASAPORTE="BPEFCD";//COMPRA EN EFECTIVO PASAPORTE EN BANCO UNIVERSAL
	
	
	
	
	
	


	
	
	
	//NM32454 SIMADI_TAQUILLA
	public static final String TIPO_DE_MOVIMIENTO_PN_VEN_TAQ_ELE ="BTVTRV";//"BTETRV";
	public static final String TIPO_DE_MOVIMIENTO_PN_VEN_TAQ_EFE ="BTVEFV"; 
	
	public static final String TIPO_DE_MOVIMIENTO_EX_VEN_TAQ_ELE ="BTETRV";//"BTVTRV";
	public static final String TIPO_DE_MOVIMIENTO_EX_VEN_TAQ_EFE ="BTEEFV";	 
		
	public static final String TIPO_DE_MOVIMIENTO_ANU_TAQ_EFE="BUANUC"; //SE DEBE CAMBIAR POR LOS CODIGOS DE ANULACION QUE ENVIE BCV
	public static final String TIPO_DE_MOVIMIENTO_ANU_TAQ_ELE="BUANUV"; //SE DEBE CAMBIAR POR LOS CODIGOS DE ANULACION QUE ENVIE BCV
	
	public static final String TIPO_DE_OPERACION="1";
	public static final String MENSAJE_OPICS_OPERACION_CAMBIO="OPERACION_CAMBIO";
	
	//Constantes asociadas al proceso de cierre de sistema
	public static final String PETICION_CONFIGURACION_FECHA_PRECIERRE="CONFIGURACION_FECHA_PRECIERRE_SISTEMA";	
	public static final String PETICION_ACTIVACION_CIERRE="ACTIVACION_CIERRE_SISTEMA";
	public static final String PETICION_DESACTIVACION_CIERRE="DESACTIVACION_CIERRE_SISTEMA";	
	public static final  int DESACTIVACION_CIERRE_SISTEMA=0;
	public static final int ACTIVACION_CIERRE_SISTEMA=1;
	public static final String PETICION_REPROCESO_CIERRE="PETICION_REPROCESO_CIERRE";	
	public static final String PETICION_PROC_AUTO_CIERRE="PETICION_PROC_AUTO_CIERRE";
	
	public static final String RUTA_ARCHIVO_CIERRE="RUTA_ARCHIVO_CIERRE";
	
	public static final String TIPO_CIERRE_MANUAL="CIERRE_MANUAL";
	public static final String TIPO_CIERRE_AUTOMATICO="CIERRE_AUTOMATICO";
	
	
	//CONSTANTES DE CAMPOS DE LA TABLA PARAMETROS SITME
	public static final String HORARIO_CIERRE="HORARIO CIERRE";
	public static final String MERCADO_ABIERTO="MERCADO ABIERTO";
	
	public static final String DIR_TEMPLATE_CRUCE = "templates/cruceTemplate";
	public static final String ARCH_TEMPLATE_CRUCE = "Plantilla_Cruce_SicadII";
	public static final String ARCH_TEMPLATE_NO_CRUCE = "Plantilla_No_Cruce_SicadII";
	public static final String INDICADOR_CRUCE = "cruce";
	public static final String INDICADOR_NO_CRUCE = "no_cruce";
	public static final String TITULO_TEMPLATE_CRUCE = "PLATILLA CRUCE SICAD II";
	public static final String TITULO_TEMPLATE_NO_CRUCE = "PLATILLA NO CRUCE SICAD II";

	public static final String ARCH_TEMPLATE_CRUCE_SIMADI_AV = "Plantilla_Cruce_Simadi_Alto_Valor";
	public static final String ARCH_TEMPLATE_NO_CRUCE_SIMADI_AV = "Plantilla_No_Cruce_Simadi_Alto_Valor";	
	public static final String TITULO_TEMPLATE_CRUCE_SIMADI_AV = "PLATILLA CRUCE SIMADI";
	public static final String TITULO_TEMPLATE_NO_CRUCE_SIMADI_AV = "PLATILLA NO CRUCE SIMADI";
	
	//NM26659 27/04/2015 TTS-491 Modificacion monto a notificar de Menudeo
	public static final String TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO = "PLANTILLA CRUCE SIMADI MENUDEO";
	public static final String TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO = "PLANTILLA NO CRUCE SIMADI MENUDEO";
	public static final String ARCH_TEMPLATE_CRUCE_SIMADI_MENUDEO = "Plantilla_Cruce_Simadi_Menudeo";
	public static final String ARCH_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO  = "Plantilla_No_Cruce_Simadi_Menudeo";	
	
	
	public static final String IDENTIFICADOR_PLANTILLA_CARGA_LISTA_DINAMICA="PLATILLA CARGA LISTA DINAMICA";
	
	public static final String CLASIFICACION_PRODUCTO_TITULO = "TITULO";
	
	public static final String CLASIFICACION_PRODUCTO_DIVISA = "DIVISA";
	
	public static final String CAMPO_PLANTILLA_UI = "Unidad Inversion";
	public static final String CAMPO_PLANTILLA_RIFCI = "Rif/Cedula";
	public static final String CAMPO_PLANTILLA_COD_ORDEN = "Codigo Orden";
	//public static final String CAMPO_PLANTILLA_NRO_ORDEN_BCV = "Nro Orden BCV";
	public static final String CAMPO_PLANTILLA_NRO_COTIZACION = "Nro Cotizacion";
	public static final String CAMPO_PLANTILLA_NRO_OPERACION = "Nro Operacion";
	public static final String CAMPO_PLANTILLA_MONTO_SOLIC = "Monto Solicitado";
	public static final String CAMPO_PLANTILLA_TASA = "Tasa";
	public static final String CAMPO_PLANTILLA_CONTRAPARTE = "Contraparte";
	public static final String CAMPO_PLANTILLA_MONTO_CRUCE = "Monto Cruce";
	public static final String CAMPO_PLANTILLA_ISIN = "ISIN";
	public static final String CAMPO_PLANTILLA_PRECIO_TITULO = "Precio Titulo";
	public static final String CAMPO_PLANTILLA_VALOR_EFECTIVO_TIT = "Valor Efectivo Titulo";
	public static final String CAMPO_PLANTILLA_FECHA_VALOR = "Fecha Valor";
	
	//**** NUEVOS CAMPOS DE PLANTILLA DE CRUCE SIMADI ALTO VALOR**/
	public static final String CAMPO_PLANTILLA_COD_ORDEN_DEMANDA = "Codigo Orden Demanda";
	public static final String CAMPO_PLANTILLA_COD_ORDEN_OFERTA = "Codigo Orden Oferta";
	
	public static final String CAMPO_PLANTILLA_COD_BCV_DEMANDA = "Codigo BCV Demanda";
	public static final String CAMPO_PLANTILLA_COD_BCV_OFERTA = "Codigo BCV Oferta";
	
	
	
	public static final String MANEJO_PRODUCTO_UNICO = "0";
	public static final String MANEJO_PRODUCTO_MIXTO = "1";
	
	//NM29643 - INFI_TTS_443 08/04/2014: Tipos de instrucciones de pago
	public static final String TIPO_INSTRUCCION_ID_CTA_SWIFT = "1";
	public static final String TIPO_INSTRUCCION_ID_CTA_NACIONAL = "2";
	public static final String TIPO_INSTRUCCION_ID_OEPRACION_CAMBIO = "3";
	public static final String TIPO_INSTRUCCION_ID_CHEQUE = "4";
	public static final String TIPO_INSTRUCCION_ID_CTA_NACIONAL_USD = "5";
	
	//NM29643 - INFI_TTS_443 09/04/2014: Cada cuantos cruces se inserta en la carga de los mismos
	public static final int COMMIT_REGISTROS_CARGA_CRUCES = 250;
	
	//NM29643 - INFI_TTS_443 10/06/2014: Cada cuantas ordenes se actualiza su estatus luego de la carga de cruces
	public static final int UPDATE_ORDENES_CARGA_CRUCES = 250;
	
	/**Indica un registro activo*/
	public static final int CRUCE_PROCESADO = 1;
	
	/**Indica un registro inactivo*/
	public static final int CRUCE_SIN_PROCESADO = 0;
	
	//Constantes incluidas en desarrollo requerimiento TTS-423 NM29643
	//CONSTANTES ASOCIADAS A LOS ESTATUS DE CONTROL DE CICLOS DE ENVIO DE CORREOS
	public static final String STATUS_ENVIO_CORREO_PREINICIADO="PRE_INICIADO";
	public static final String STATUS_ENVIO_CORREO_INICIADO="INICIADO";
	public static final String STATUS_ENVIO_CORREO_DESCARTADO="DESCARTADO";
	public static final String STATUS_ENVIO_CORREO_FINALIZADO="FINALIZADO";
	//NM29643 - INFI_TTS_423: Nombres de archivo de informe de envio de correos
	public static final String ARCH_INFORME_ENVIO_CORREOS = "InformeEnvioCorreos";
	//NM29643 - INFI_TTS_423: Nombres de campos de informe de envio de correos
	public static final String CAMPO_INF_STATUS_ENVIO = "Estatus Envio";
	public static final String CAMPO_INF_PLANT = "Plantilla";
	public static final String CAMPO_INF_CICLO = "ID Ciclo";
	public static final String CAMPO_INF_FECHA_ENV = "Fecha Envio";
	//NM29643 - INFI_TTS_423: Nombres de campos de informe de envio de correos - Tipo de Destinatario Cliente
	public static final String CAMPO_INF_RIF_CI = "Rif/cedula";
	public static final String CAMPO_INF_NOMBRE = "Nombre";
	public static final String CAMPO_INF_CORREO = "Correo";
	public static final String CAMPO_INF_COD_ORDEN = "Codigo Orden";
	public static final String CAMPO_INF_UI = "Unidad de Insersion";
	//NM29643 - INFI_TTS_423: Nombres de campos de informe de envio de correos - Tipo de Destinatario Funcional
	public static final String CAMPO_INF_AREA = "Area";
	public static final String CAMPO_INF_DEST = "Destinatario";
	
	//NM29643 - INFI_TTS_466: ID's de Eventos configurados para el tipo de producto SICAD2PER
	public static final String ENVIO_BCV_PER = "ENVIO_BCV_PER";
	//NM29643 - INFI_TTS_466: ID's de Eventos configurados para el tipo de producto SICAD2RED
	public static final String TOMA_ORDEN_RED = "TOMA_ORDEN_RED";
	public static final String ENVIO_BCV_RED = "ENVIO_BCV_RED";
	//NM29643 - INFI_TTS_466: ID's de Eventos configurados para el tipo de producto GENERAL
	public static final String CRUCE = "CRUCE";
	public static final String NO_CRUCE = "NO_CRUCE";
	public static final String LIQUIDACION_EFECTIVO = "LIQUIDACION_EFECTIVO";
	public static final String RECEPCION_TITULO = "RECEPCION_TITULO";
	
	//NM29643 - INFI_TTS_466: enmascaramiento de nros de cuentas
	public static final String caracterRelleno = "*";
	public static final int cantCaractToShow = 4;
	public static final boolean showFromEnd = true;
	
	//NM29643 - INFI_TTS_466: envio de correos
	public static final int tamMaxContenido = 4000; 
	
	//PRODUCTOS SIMADI - CONTINGENCIA 11/02/2015
	public static final String PRODUCTO_DEMANDA="6"; //COMPRA
	public static final String PRODUCTO_OFERTA="7"; //VENTA
	
	//NM32454 SE AGREGA CONSTANTE PARA RUTA DEL KEYSTORE
	public static final String RUTA_CER_MENUDEO_BCV="ruta.cert.menudeo";
	public static final String RUTA_CER_ALTO_VALOR_BCV="ruta.cert.alto.valor";
	public static final String HTTPS_PROXY_HOST = "https.proxyHost";
	public static final String HTTPS_PROXY_PORT = "https.proxyPort";
	public static final String END_POINT_BCV_MENUDEO = "endpoint.bcv.menudeo";
	public static final String END_POINT_BCV_ALTO_VALOR = "endpoint.bcv.alto.valor";
	public static final String END_POINT_BCV_DICOM = "endpoint.bcv.dicom";
	public static final String END_POINT_BCV_MESA = "endpoint.bcv.mesa";
	
	// NM11383 VARIABLE PARA EL ENDPOINT DEL WS.PROPERTY
	public static final String END_POINT_BCV_ORO = "endpoint.bcv.oro";

	public static final String END_POINT_BCV_INTERVENCION = "endpoint.bcv.intervencion";
	public static final String END_POINT_BCV_INTERVENCION_VENTA = "endpoint.bcv.intervencion.path.notificar";
	public static final String END_POINT_BCV_INTERVENCION_LISTAR_MONEDA = "endpoint.bcv.intervencion.path.listar.moneda";
	public static final String END_POINT_BCV_INTERVENCION_SESION = "endpoint.bcv.intervencion.path.sesion";
	public static final String END_POINT_BCV_INTERVENCION_LISTAR_OPERACIONES = "endpoint.bcv.intervencion.path.listar.operaciones";
	public static final String END_POINT_BCV_INTERVENCION_LISTAR_ARCHIVO = "endpoint.bcv.intervencion.path.listar.archivos";
	public static final String END_POINT_BCV_INTERVENCION_ACTIVAS = "endpoint.bcv.intervencion.path.intervencion.activa";
	public static final String END_POINT_BCV_INTERVENCION_CAMBIO ="endpoint.bcv.intervencion.path.intervencion.cambio";
	public static final String END_POINT_BCV_INTERVENCION_BANCARIA ="endpoint.bcv.intervencion.path.intervencion.bancaria";
	public static final String END_POINT_BDV_OFICINAS = "endpoint.bdv.oficina.path";
	//ESTATUS DE ORDENES AL BCV
	public static final String TODOS="-1";
	public static final String SIN_VERIFICAR="0";
	public static final String VERIFICADA_APROBADA="1";
	public static final String VERIFICADA_RECHAZADA="2";	
	public static final String VERIFCADA_MANUAL_APROBADA="3";
	public static final String VERIFIDA_MANUAL_RECHAZADA="4";
	public static final String ANULADA_BCV="5";
	
	//CODIGOS DE COMPRA Y VENTA MENUDEO
	public static final String COD_COMPRA="1203";
	public static final String COD_VENTA="5204";
	
//tipo de envio
	
	//CODIGOS DE INTERVENCION
	public static final String COD_TRANSFERENCIA_INTERBANCARIA="TIB";
	public static final String COD_VENTA_OPERADOR_A_CLIENTE="VOC";
	//LETRA PARA EL MANEJO DE ORIGINAL Y REVERSADA
	
	public static final String ORIGINAL="O";
	public static final String REVERSADA="R";
	public static final String ENVIO_MENUDEO_RECHAZADA="4";
	public static final String ESTATUS_RECHAZADAS="RECHAZADA";
	public static final String ENVIO_MENUDEO_ANULADA="3";
	public static final String ENVIO_MENUDEO_MANUAL="2";
	public static final String ENVIO_MENUDEO="1";
	public static final String ENVIO_Y_RECHAZADAS_MENUDEO="6";
	public static final String ENVIO_MENUDEO_FALTANTES="0";
	public static final String ENVIO_MENUDEO_TODAS="TODAS";
	public static final String ENVIO_MENUDEO_TODAS_NUMERICO= "5";
	public static final String TIPO_MOVIMIENTOS_MENUDEO_V="VENTA";
	public static final String TIPO_MOVIMIENTOS_MENUDEO_C="COMPRA";
	
	
	//CONSTANTES PARA EL TIPO DE NEGOCIO: ALTO O BAJO VALOR. 
	public static final String TIPO_NEGOCIO_NORMAL="0";
	public static final String TIPO_NEGOCIO_ALTO_VALOR="1";
	public static final String TIPO_NEGOCIO_BAJO_VALOR="2";
	public static final String TIPO_NEGOCIO_BAJO_VALOR_TAQUILLA="3";
	
	//CONSTANTES PARA ORIGEN DE OFERTAS ALTO VALOR
	public static final String ORIGEN_BANCO_DE_VENEZUELA="0";
	public static final String ORIGEN_BCV_INTERBANCARIO="1";
	
//manejo de intervencion bancaria
	
	public static final String ENVIO_INTERVENCION_RECHAZADA="4";
	public static final String ENVIO_INTERVENCION_ANULADA="3";
	public static final String ENVIO_INTERVENCION_MANUAL="2";
	public static final String ENVIO_INTERVENCION="1";
	public static final String ENVIO_INTERVENCION_FALTANTES="0";
	public static final String ENVIO_INTERVENCION_TODAS="TODAS";
	public static final String TIPO_MOVIMIENTOS_INTERVENCION_V="VENTA";
	public static final String TIPO_MOVIMIENTOS_INTERVENCION_C="COMPRA";
	
	//CONSTANTES PARA TIPOS DE PERSONA
	public static final String TIPPER_ID_PN="V";
	public static final String TIPPER_ID_PE="E";
	public static final String TIPPER_ID_PJ="J";
	public static final String TIPPER_ID_PG="G";
	public static final String TIPPER_ID_PP="P";
	
	
	//TTS-491 NM26659_03/03/2015 - Validacion Web Service BCV
	public static final String TRANSF_BCV_ONLINE="TRANSF_BCV_ONLINE";
	
	//TTS-491 NM26659_20/04/2015 - Validacion Web Service BCV Alto Valor
	public static final String TRANSF_BCV_ONLINE_AVALOR="TRANSF_BCV_ONLINE_AVALOR";
	
	//SIMADI TAQUILLA NM32454_19/08/2015 - Validacion Web Service BCV Taquilla 
	public static final String TRANSF_BCV_ONLINE_TAQ="TRANSF_BCV_ONLINE_TAQ";
	
	//manejo de mesa de cambio
	public static final String TIPO_MOVIMIENTOS_MESA_CAMBIO_VENTA="VENTA";
	
	//TTS_491_CONTINTENGIA 09/03/2015 RUTA PARA LLAVES DE ENCRIPTACION
	public static final String RUTA_CUSTODIO_1 = "ruta.custodio1";
	public static final String RUTA_CUSTODIO_2 = "ruta.custodio2";
	
	//TTS_491_CONTINTENGIA 09/03/2015 RUTA PARA LLAVES DE ENCRIPTACION
	public static final String WS_BCV_MENUDEO = "WS_BCV_MENUDEO";
	public static final String WS_INTERVENCION = "WS_INTERVENCION";
	public static final String WS_BCV_ALTO_VALOR = "WS_BCV_ALTO_VALOR";
	public static final String WS_BCV_DICOM = "WS_BCV_DICOM";
	public static final String WS_BCV_ORO = "WS_BCV_ORO";
	public static final String MENUDEO = "MENUDEO";
	public static final String WS_BCV_INTERVENCION = "WS_BCV_INTERVENCION";
	
	//TTS-491_WS_ALTO_VALOR - Consulta de ofertas en OPICS
	public static final String ID_SUBPROD_OFERTA_OPICS_INTERBANCARIA = "S1";
	public static final String ID_SUBPROD_OFERTA_ALTOV_OPICS_PN= "SO";	
	public static final String ID_SUBPROD_OFERTA_BAJOV_OPICS = "S3";
	public static final String ID_SUBPROD_OFERTA_ALTOV_OPICS_PJ = "S4";	
	public static final String ID_PRODUCTO_OPICS_OPER_CAMBIO = "FXD";
	public static final String ID_BR_OPICS = "99";
	
	public static final String OFERTA_ANULACION = "1";
	public static final String DEMANDA_ANULACION = "2";
	
	//NM32454 SIMADI TAQUILLA ESTATUS PARA LA OPERACION EN LA TABLA TAQUILLA
	public static final String STATUS_TAQ_NO_LIQUIDADA = "0";
	public static final String STATUS_TAQ_LIQUIDADA = "1";
	public static final String STATUS_TAQ_RESERVADA_X_TF = "2";
	public static final String STATUS_TAQ_LIQUIDADA_INFI = "3";
	
	//NM32454 SIMADI TAQUILLA VALORES PARA EL PARAMETRO TRANSF_BCV_ONLINE_TAQ
	public static final String PARAM_TRANSF_BCV_OFFLINE_TAQ = "0"; //APAGADO
	public static final String PARAM_TRANSF_BCV_ONLINE_TAQ = "1";  //ENCEDIDO (SI BCV RECHAZA SE RECHAZA LA OPERACION COMPLETA)
	public static final String PARAM_TRANSF_BCV_NOTI_TAQ = "2";    //MODO NOTIFICACION (SI BCV RECHAZA CONTINUA LA OPERACION)

	//NM25287 TTS-504-SIMADI Efectivo Taquilla  31/08/2015
	public static final int TIPO_OP_ELECTRONICO = 1;
	public static final int TIPO_OP_EFECTIVO = 2;
	
	//NM26659 Transacciones para manejo de operaciones SIMADI Taquilla 07/09/2015
	public static final String TIPO_OPERACION_ELECTRONICO = "ELECTRONICO";
	public static final String TIPO_OPERACION_EFECTIVO = "EFECTIVO";
	
	/**Campo indicador si la unidad de inversion que se esta procesado es de Oferta (1)*/
	public static final int UI_OFERTA = 1;
	
	
	
	
	/**Campo indicador si la unidad de inversion que se esta procesado es de Demanda (2)*/
	public static final int UI_DEMANDA= 0;
	
	/**Campo indicador si la solicitud a procesar es de Oferta (O)*/
	public static final String SOLICITUD_OFERTA = "O";
	/**Campo indicador si la solicitud a procesar es de Demanda (D)*/
	public static final String SOLICITUD_DEMANDA = "D";

	/**Campo indicador si la solicitud a procesar es de Certificado ORO (C)*/
	public static final String SOLICITUD_ORO = "C";
	
	
	public static final String SOLICITUD_COMPRA = "C";
	public static final String SOLICITUD_VENTA = "V";
	
	public static final int PROCESO_VERIFICACION_DICOM = 0;
	
	public static final int PROCESO_VERIFICACION_ORO = 0;
	
	public static final int PROCESO_CRUCE_DICOM = 1;
	
	public static final int ESTATUS_REGISTRO_PROCESADO_DICOM=1;
	
	public static final int ESTATUS_REGISTRO_PROCESADO_ORO=1;
	
	/*Constantes para el manejo del archivo DICOM*/
	
	public static final String CODIGO_DOLAR = "COM";
	
	public static final String CODIGO_MONEDA_ISO_USD_DICOM="0840";
	
	public static final String FORTAMO_MONTO_BASE="#.0000";
	
	public double MAXIMO_TIEMPO_EJECUCION = 1200; //SEGUNDOS
	
	public double MINIMO_TIEMPO_ITERACION = 3; //SEGUNDOS
	
	public double MAXIMO_TIEMPO_ITERACION = 10; //SEGUNDOS
	
	public int MAXIMO_ERROR_ITERACION = 4; //SEGUNDOS
	
	
	//Manejo de la conciliacion Menudeo.
	
	public static final String FECHA_MANUAL_CONCILIACION = "1";
	
	public static final String RUTA_CONCILIACION = "RUTA_CONCILIACION";
	public static final String RUTA_CONCILIACION_RESP = "RUTA_CONCILIACION_RESP";
	public static final String NOMBRE_ARCH_ANULADAS = "NOMBRE_ARCH_ANULADAS";
	public static final String NOMBRE_ARCH_RESP_ANULADAS = "NOMBRE_ARCH_RESP_ANULADAS";
	public static final String RUTA_INVETARIO ="RUTA_INVETARIO";
	public static final String RUTA_ARCHIVO_INVETARIO ="RUTA_ARCHIVO_INVETARIO";
	public static final String RUTA_INVETARIO_RESP ="RUTA_INVETARIO_RESP";
	public static final String RUTA_MENUDEO_CON = "RUTA_MENUDEO_CON";
	public static final String RUTA_MENUDEO_CONR = "RUTA_MENUDEO_CONR";
	public static final String MESA_CAMBIO = "MESA_CAMBIO";
	
	
}

