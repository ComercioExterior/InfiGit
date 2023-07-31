package com.bdv.infi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import org.apache.axis.transport.http.HTTPConstants;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.PagoCuponesDao;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.CustodiaComisionTitulo;
import com.bdv.infi.data.CustodiaEstructuraTarifaria;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

import criptografia.TripleDes;

/**
 * Clase utilitaria que nos ofrece m&eacute;todos a ser utilizados en todo el sistema
 * 
 * @author jal, nev
 * 
 */
public class Utilitario {

	// Bases de c&aacute;lculo
	public static final String BASE_30F360 = "30F360";
	public static final String BASE_A360 = "A360";
	public static final String BASE_A365 = "A365";
	public static final String BASE_A365GT = "A365GT";
	public static final String BASE_ACT365 = "ACT365";
	public static final String BASE_ACTUAL = "ACTUAL";
	public static final String BASE_BOND = "BOND";
	public static final String BASE_EBOND = "EBOND";
	public static final String BASE_NL360 = "NL360";
	public static final String BASE_NL365 = "NL365";
	public static final String numeroDecimales = "###,###,##0.00";
	public static final String numeroSinDecimales = "##"; // ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/** Guarda un documento en la ruta especificada con el contenido indicado */
	public static void guardarDocumento(String ruta, String documento) throws Exception {
		if (ruta == null || documento == null) {
			throw new Exception("Error en la ruta o contenido del archivo");
		}

		BufferedReader leer = new BufferedReader(new StringReader(documento));
		try {
			PrintWriter escribir = new PrintWriter(new BufferedWriter(new FileWriter(ruta)));
			String linea = null;
			while ((linea = leer.readLine()) != null) {
				escribir.write(linea);
			}
			escribir.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Guarda un documento en la ruta especificada con el contenido indicado */
	public static void guardarDocumento(File ruta, String documento) throws Exception {
		if (ruta == null || documento == null) {
			throw new Exception("Error en la ruta o contenido del archivo");
		}
		BufferedReader leer = new BufferedReader(new StringReader(documento));
		try {
			PrintWriter escribir = new PrintWriter(new BufferedWriter(new FileWriter(ruta)));
			String linea = null;
			while ((linea = leer.readLine()) != null) {
				escribir.write(linea);
			}
			escribir.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Formatea un n&uacute;mero en base a la m&aacute;scara recibida */
	public static String formatearNumero(String numero, String mascara) {

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator('.');
		dfs.setDecimalSeparator(',');

		DecimalFormat decfmt = new DecimalFormat();
		decfmt.setDecimalFormatSymbols(dfs);
		decfmt.applyPattern(mascara);
		return decfmt.format(Double.parseDouble(numero));
	}

	/** Formatea un n&uacute;mero en base a la m&aacute;scara recibida */
	public static String formatearMontoLocale(String numero, String mascara, Locale localidad) {
		NumberFormat nf = NumberFormat.getNumberInstance(localidad);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator('.');
		dfs.setDecimalSeparator(',');

		DecimalFormat decfmt = (DecimalFormat) nf;
		decfmt.setDecimalFormatSymbols(dfs);
		decfmt.applyPattern(mascara);
		return decfmt.format(Double.parseDouble(numero));
	}

	/**
	 * Calcula el precio para los t&iacute;tulos de inventario
	 * 
	 * @param idTitulo
	 *            id del t&iacute;tulo para buscar su precio
	 * @throws Exception
	 *             si hay un error obteniendo el precio o buscamdo el t&iacute;tulo
	 */
	public static double calcularPrecio(String idTitulo, double rendimiento, DataSource dso) throws Exception {
		TitulosDAO titulosDAO = new TitulosDAO(dso);
		titulosDAO.detallesTitulo(idTitulo);
		DataSet dataSet = titulosDAO.getDataSet();
		Date fechaVencimiento = null;
		Date fechaEmision = null;
		String baseCalculo = "";
		int totalDias = 0; // Total de dias del a�o
		double precioFinal = 0;
		Date fechaActual = new Date();
		int diasTranscurridos = 0;
		int diasDiferencia = 0;
		double cupon;

		if (dataSet.count() == 0) {
			throw new Exception("No se encontr&oacute; ning&uacute;n t&iacute;tulo por el id seleccionado");
		}
		dataSet.next();

		// Calcula el precio
		fechaVencimiento = StringToDate(dataSet.getValue("fecha_vencimiento"), ConstantesGenerales.FORMATO_FECHA);
		fechaEmision = StringToDate(dataSet.getValue("fecha_emision"), ConstantesGenerales.FORMATO_FECHA);
		baseCalculo = dataSet.getValue("basis").trim();
		cupon = Double.parseDouble(dataSet.getValue("couprate_8"));
		totalDias = diasBaseCalculo(baseCalculo);

		// Calula el precio en base a los parametros leidos
		// 100*(1+(cupon*plazoTotal(fechaVcto-fechaEmision)/Base))/(1+(Rendimiento*(fechaVcto-FechaOrden)/Base))
		diasTranscurridos = fechasDiferenciaEnDias(fechaEmision, fechaVencimiento);
		diasDiferencia = fechasDiferenciaEnDias(fechaActual, fechaVencimiento);
		precioFinal = 100 * (1 + ((cupon / 100) * diasTranscurridos / totalDias)) / (1 + ((rendimiento / 100) * diasDiferencia / totalDias));

		return Math.rint(precioFinal * 1000000) / 1000000;
	}

	/**
	 * M&eacute;todo para convertir un string a un tipo Date
	 * 
	 * @param fecha
	 *            , formato
	 * @return Date
	 * @throws ParseException
	 */
	public static Date StringToDate(String fechaString, String formato) throws ParseException {

		java.util.Date fecha = null;
		// fecha = java.sql.Date.valueOf("1950-01-01");
		if (fechaString != null && !fechaString.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			// ////////////Fecha actual///////////////////////
			fecha = sdf.parse(fechaString);
			// //////////////////////////////////////////////////
		}

		return fecha;
	}

	/**
	 * Retorna una fecha de tipo String en el formato indicado a partir de un tipo Date
	 * 
	 * @param fechaDate
	 * @param formato
	 * @return String de la fecha
	 * @throws ParseException
	 */
	public static String DateToString(Date fechaDate, String formato) throws ParseException {

		String fecha = null;
		if (fechaDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			fecha = sdf.format(fechaDate);
		}

		return fecha;
	}

	/**
	 * Convierte una fecha Tipo Date a tipo Calendar
	 * 
	 * @param fechaDate
	 * @return objeto calendar con la fecha
	 * @throws ParseException
	 */
	public static Calendar DateToCalendar(Date fechaDate) throws ParseException {

		Calendar fechaCalendar = new GregorianCalendar();
		fechaCalendar.setTime(fechaDate);// convertir fecha Date a Calendar

		return fechaCalendar;
	}

	/**
	 * 
	 * @param fechaCalendar
	 * @param formato
	 *            de fecha
	 * @return fecha tipo String
	 * @throws ParseException
	 */
	public static String CalendarToString(Calendar fechaCalendar, String formato) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		String fechaString = sdf.format(fechaCalendar.getTime());
		return fechaString;
	}

	/**
	 * Obtiene una fecha pasada restando una cantidad de d�as a una fecha especificada tipo Date
	 * 
	 * @param fechaDate
	 * @param cantidadDias
	 * @param formato
	 * @return fecha pasada tipo Date
	 * @throws ParseException
	 */
	public static Date obtenerFechaPasada(Date fechaDate, int cantidadDias) throws ParseException {

		Date fechaPasada;

		Calendar calendar = DateToCalendar(fechaDate);// convertir fecha Date a Calendar
		calendar.add(Calendar.DATE, -cantidadDias); // se le restan la cantidad d�as enviada

		fechaPasada = calendar.getTime();

		return fechaPasada;
	}

	/**
	 * Metodo que devuelve la diferencia de dias entre dos Fechas
	 * 
	 * @param fechaInicial
	 *            Date
	 * @param fechaFinal
	 *            Date
	 * @return int
	 */
	public static int fechasDiferenciaEnDias(Date fechaInicial, Date fechaFinal) {

		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = (fechaFinalMs - fechaInicialMs);
		// conversion miliseg a dias
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ((int) dias - 1);
	}

	/**
	 * <p>
	 * Completa el String de entrada con el caracter indicado, hasta completar la longitud indicada
	 * </p>
	 * <p>
	 * Si la longitud indicada para la cadena final es menor a la longitud la cadena actual, se devuelve exactamente la misma cadena si modificaciones.
	 * </p>
	 * 
	 * @param src
	 *            Cadena de caracteres origen
	 * @param c
	 *            Caracter a repetir en la completacion
	 * @param length
	 *            Longitud de la cadena de caracteres resultante}
	 * @param right
	 *            El valor es true si el relleno se adjuntara a la derecha de la cadena
	 * @return cadena formateada
	 */
	public static final String rellenarCaracteres(String src, char c, int length, boolean right) {

		if (src == null) {
			src = " ";
		}
		StringBuffer filler = new StringBuffer();
		// Se completa el String de entrada con el caracter indicado
		// hasta completar la longitud indicada
		for (int i = 0; i < Math.abs(length - src.length()); i++) {
			filler.append(c);
		}
		if (right) {
			// Si el relleno se debe hacer por la derecha
			src = src.concat(filler.toString());
		} else {
			// Si el relleno se debe hacer por la izquierda
			src = filler.append(src).toString();
		}

		return src;
	}

	// NM32454 03/03/2015 INFI_TTS_491_WS_BCV
	/*
	 * public static final void cargarCertificado(String rutaCertificado) { //SE AGREGA UBICACION DE CERTIFICADO PARA PODER ESTABLECER LA COMUNICACION try { AxisSSLSocketFactory.setKeystorePassword("changeit"); AxisSSLSocketFactory.setResourcePathToKeystore(rutaCertificado); AxisProperties.setProperty("axis.socketSecureFactory", "org.bcv.utilities.AxisSSLSocketFactory"); } catch (Exception e) { //
	 * TODO Auto-generated catch block Logger.info(null,"Ha ocurrido un error al momento de cargar el certificado de la ruta: "+rutaCertificado+" Error: " +e.toString()); } }
	 */

	// NM32454 03/03/2015 INFI_TTS_491_WS_BCV
	public static final void configurarProxy() {
		try {
			Propiedades propiedades = Propiedades.cargar();
			System.setProperty(ConstantesGenerales.HTTPS_PROXY_HOST, propiedades.getProperty("https.proxyHost"));
			System.setProperty(ConstantesGenerales.HTTPS_PROXY_PORT, propiedades.getProperty("https.proxyPort"));
		} catch (IOException e) {
			Logger.info(null, "Ha ocurrido un error al momento de cargar las configuraciones del WS para bcv: " + e.getMessage());
			// TODO Auto-generated catch block
			throw new RuntimeException("Error obteniendo la ruta ra�z de la aplicaci�n", e);
		}
	}

	/**
	 * <p>
	 * Completa el String de entrada con el caracter indicado, hasta completar la longitud indicada
	 * </p>
	 * <p>
	 * Si la longitud indicada para la cadena final es menor a la longitud la cadena actual, se devuelve exactamente la misma cadena si modificaciones.
	 * </p>
	 * <p>
	 * Si el parametro truncar es verdadero, se verifica si la longitud de la cadena enviada es mayor a la cantidad de caracteres a los cuales se desea formatear, se trunca dicha cadena a la longitud solicitada y se retorna enseguida el valor resultante.
	 * </p>
	 * 
	 * @param src
	 *            Cadena de caracteres origen
	 * @param c
	 *            Caracter a repetir en la completacion
	 * @param length
	 *            Longitud de la cadena de caracteres resultante}
	 * @param right
	 *            El valor es true si el relleno se adjuntara a la derecha de la cadena
	 * @param truncar
	 *            indicador booleano para saber si se debe truncar o no la cadena, en caso de que su longitud sea mayor a la longitud a formatear
	 * @return cadena formateada
	 */
	public static final String rellenarCaracteres(String src, char c, int length, boolean right, boolean truncar) {

		if (src == null) {
			src = " ";
		}

		// Si la longitud de la cadena enviada es Mayor a la longitud que se debe formatear, truncar dicha cadena
		// si el parametro truncar es verdadero
		if (truncar) {
			if (src.length() > length) {
				src = src.substring(0, length);

				return src; // retornar cadena ya que ya tiene la cantidad de caracteres correctos
			}
		}

		StringBuffer filler = new StringBuffer();
		// Se completa el String de entrada con el caracter indicado
		// hasta completar la longitud indicada
		for (int i = 0; i < Math.abs(length - src.length()); i++) {
			filler.append(c);
		}
		if (right) {
			// Si el relleno se debe hacer por la derecha
			src = src.concat(filler.toString());
		} else {
			// Si el relleno se debe hacer por la izquierda
			src = filler.append(src).toString();
		}

		return src;
	}

	/**
	 * <p>
	 * Completa el String de entrada con el caracter indicado, hasta completar la longitud indicada. Si posee una longitud mayor trunca los caracteres sobrantes
	 * </p>
	 * <p>
	 * Si la longitud indicada para la cadena final es menor a la longitud la cadena actual, se devuelve exactamente la misma cadena si modificaciones.
	 * </p>
	 * 
	 * @param src
	 *            Cadena de caracteres origen
	 * @param c
	 *            Caracter a repetir en la completacion
	 * @param length
	 *            Longitud de la cadena de caracteres resultante}
	 * @param right
	 *            El valor es true si el relleno se adjuntara a la derecha de la cadena
	 * @return
	 */
	public static final String rellenarCaracteresTrunc(String src, char c, int length, boolean right) {

		try {
			if (src == null) {
				src = " ";
			} else {
				if (src.length() > length) {
					src = src.substring(0, length - 1);
				}
			}
			StringBuffer filler = new StringBuffer();
			// Se completa el String de entrada con el caracter indicado
			// hasta completar la longitud indicada
			for (int i = 0; i < Math.abs(length - src.length()); i++) {
				filler.append(c);
			}
			if (right) {
				// Si el relleno se debe hacer por la derecha
				src = src.concat(filler.toString());
			} else {
				// Si el relleno se debe hacer por la izquierda
				src = filler.append(src).toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return src;
	}

	/**
	 * Metodo para formatear un numero con una longitud especifica, tantos enteros y tantos decimales dependiendo de los parametros recibidos para los servicios web
	 * 
	 * @param numero
	 * @param cantEnteros
	 * @param cantDecimales
	 * @return String numeroFormateado
	 * @throws Exception
	 */
	public static String formatoDecimalesWS(BigDecimal numero, int cantEnteros, int cantDecimales) throws Exception {

		String numeroFormateado = "";
		/*
		 * Obtenemos el valor entero del numero a formatear y lo guardamos en un String para utilizar el metodo de rellenar caracteres a la izquierda, segun sea el caso
		 */
		int entero = numero.intValue();
		String enteroString = String.valueOf(entero);

		// RELLENAR CARACTERES PARA LA CANTIDAD IZQUIERDA
		enteroString = rellenarCaracteres(enteroString, '0', cantEnteros, false);

		/*
		 * Le restamos al numero bigdecimal su entero para obtener el valor decimal String
		 */
		numero = numero.subtract(new BigDecimal(entero));
		int cantMaxSubString = 2 + cantDecimales;
		String decimalString = String.valueOf(numero).substring(2, cantMaxSubString);

		// RELLENAR CARACTERES PARA LA CANTIDAD DERECHA

		decimalString = rellenarCaracteres(decimalString, '0', cantDecimales, true);

		numeroFormateado = enteroString.concat(decimalString);

		return numeroFormateado;
	}

	/**
	 * M&eacute;todo para buscar la diferencia de meses
	 * 
	 * @param fechaUltPagCupon
	 *            , fechaGuiaPagCupon
	 * @return Double
	 * @throws Exception
	 */
	public static int diferenciaMeses(GregorianCalendar g1, GregorianCalendar g2) throws Exception {
		int elapsed = -1; // Por defecto estaba en 0 y siempre asi no haya pasado un mes contaba 1)
		GregorianCalendar gc1, gc2;

		if (g2.after(g1)) {
			gc2 = (GregorianCalendar) g2.clone();
			gc1 = (GregorianCalendar) g1.clone();
		} else {
			gc2 = (GregorianCalendar) g1.clone();
			gc1 = (GregorianCalendar) g2.clone();
		}

		while (gc1.before(gc2)) {
			gc1.add(Calendar.MONTH, 1);
			elapsed++;
		}
		if (gc1.get(Calendar.DATE) == (gc2.get(Calendar.DATE)))
			elapsed++; // si es el mismo dia cuenta para la suma de meses
		return elapsed;
	}

	/**
	 * Metodo donde se obtiene el nombre del Mes
	 * 
	 * @param mes
	 * @return
	 */
	public static String nombreMes(int mes) {
		String meses = null;
		switch (mes) {
		case 1:
			meses = "Enero";
			break;
		case 2:
			meses = "Febrero";
			break;
		case 3:
			meses = "Marzo";
			break;
		case 4:
			meses = "Abril";
			break;
		case 5:
			meses = "Mayo";
			break;
		case 6:
			meses = "Junio";
			break;
		case 7:
			meses = "Julio";
			break;
		case 8:
			meses = "Agosto";
			break;
		case 9:
			meses = "Septiembre";
			break;
		case 10:
			meses = "Octubre";
			break;
		case 11:
			meses = "Noviembre";
			break;
		case 12:
			meses = "Diciembre";
			break;

		}
		return meses;

	}

	/**
	 * M&eacute;todo Obtener el ultimo dia del mes
	 * 
	 * @param int mes, int a�o
	 * @return int
	 * @throws Exception
	 */
	public static int diasDelMes(int mes, int a�o) {
		switch (mes) {
		case 0: // Enero
		case 2: // Marzo
		case 4: // Mayo
		case 6: // Julio
		case 7: // Agosto
		case 9: // Octubre
		case 11: // Diciembre
			return 31;
		case 3: // Abril
		case 5: // Junio
		case 8: // Septiembre
		case 10: // Noviembre
			return 30;
		case 1: // Febrero
			if (((a�o % 100 == 0) && (a�o % 400 == 0)) || ((a�o % 100 != 0) && (a�o % 4 == 0)))
				return 29; // A�o Bisiesto
			else
				return 28;
		default:
			throw new java.lang.IllegalArgumentException("El mes debe estar entre 0 y 11");
		}
	}

	/**
	 * M&eacute;todo para realizar el calculo de intereses para el pago de cupones
	 * 
	 * @param fechaUltPagCupon
	 *            ,fechaGuiaPagCupon,base,int valor es 1 cuando se realiza el calculo para intereses caidos,otro valor para pago de cupones
	 * @return BigDecimal
	 * @throws Exception
	 */
	public static BigDecimal cuponesDiferenciaBaseDias(Date fePagUltCupon, Date fePagGuiaCupon, String base, int valor) throws Exception {
		// Declaracion de variables
		BigDecimal diferenciaDias = new BigDecimal(0);
		int mes = 0;
		int annio = 0;
		int diferenciaDiaMay = 0;
		int mesesResta = 30;
		String nuevaFecha = "";
		BigDecimal basePorDiferencia = new BigDecimal(0);
		BigDecimal totalDiferencia = new BigDecimal(0);
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);
		Date fechadesde = fePagUltCupon;
		Date fechahasta = fePagGuiaCupon;
		String baseCalculo = base.trim();
		GregorianCalendar fePagUltCuponMeses = new GregorianCalendar();
		GregorianCalendar fePagGuiaCuponMeses = new GregorianCalendar();
		fePagUltCuponMeses.setTime(fechadesde);
		fePagGuiaCuponMeses.setTime(fechahasta);
		int mesFePagUltCupon = fePagUltCuponMeses.get(Calendar.MONTH);
		int mesfePagGuiaCuponMeses = fePagGuiaCuponMeses.get(Calendar.MONTH);
		int anniofePagUltCuponMeses = fePagUltCuponMeses.get(Calendar.YEAR);
		int anniofePagGuiaCuponMeses = fePagGuiaCuponMeses.get(Calendar.YEAR);
		int diaFePagUltCupon = fePagUltCuponMeses.get(Calendar.DATE);
		int diaFePagUltCuponVer = fePagUltCuponMeses.get(Calendar.DATE);// verificar ultimo de mes
		int diaFePagGuiaCupon = fePagGuiaCuponMeses.get(Calendar.DATE);
		int meses = diferenciaMeses(fePagUltCuponMeses, fePagGuiaCuponMeses) * 30;
		// Calcula la base por diferencia cuando la BASE es BASE_A365
		if (baseCalculo.equals(BASE_A365)) {
			diferenciaDias = new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta));
			if (valor != 1) {
				diferenciaDias = diferenciaDias.divide(new BigDecimal(365), 7, BigDecimal.ROUND_HALF_EVEN);
			}
			totalDiferencia = diferenciaDias;

			// Calcula la base por diferencia cuando la BASE es BASE_A360
		} else if (baseCalculo.equals(BASE_A360)) {
			diferenciaDias = new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta));
			if (valor != 1) {
				diferenciaDias = diferenciaDias.divide(new BigDecimal(360), 7, BigDecimal.ROUND_HALF_EVEN);
			}
			totalDiferencia = diferenciaDias;
			// Calcula la base por diferencia cuando la BASE es BASE_EBOND o BASE_30F360 o BASE_BOND
		} else if (baseCalculo.equals(BASE_EBOND) || baseCalculo.equals(BASE_30F360) || baseCalculo.equals(BASE_BOND)) {
			String cero = "";
			if (diaFePagGuiaCupon == 31) {// Se verifica que los meses esten en base a 30 dias
				diaFePagGuiaCupon = 30;
			}
			if (String.valueOf(mes).length() == 1) {
				cero = "0";
			}
			// En el caso de que el dia de la fechaPagUltCupon sea mayor a fePagoGuiaCupon se asigna una nueva fecha
			mes = 1 + fePagGuiaCuponMeses.get(Calendar.MONTH);
			annio = fePagGuiaCuponMeses.get(Calendar.YEAR);
			nuevaFecha = String.valueOf(annio).concat("-").concat(cero).concat(String.valueOf(mes)).concat("-").concat(String.valueOf(diaFePagUltCupon));
			fechadesde = formato.parse(nuevaFecha);
			// Fin
			if (fechadesde.compareTo(fechahasta) == 1) {
				nuevaFecha = String.valueOf(diaFePagUltCupon).concat("-").concat(String.valueOf(mes - 1)).concat("-").concat(String.valueOf(annio));
				fechadesde = StringToDate(nuevaFecha, "dd-MM-yyyy");
				// Se obtienen los dias del mes para sumarizar
				int diaFeHasta = fePagGuiaCuponMeses.get(Calendar.DATE);
				if (diaFePagUltCupon == 30) {
					diferenciaDiaMay = diferenciaDiaMay - 1;
				}
				if (diaFePagUltCupon == 31) {
					diaFePagUltCupon = 30;
					diferenciaDiaMay = diferenciaDiaMay - 1;
				}
				diferenciaDiaMay = mesesResta - diaFePagUltCupon + diaFeHasta + meses;
				int ultimoMesUltCupon = diasDelMes(mesFePagUltCupon, anniofePagUltCuponMeses);
				int ultimoMesPagGuiaCupon = diasDelMes(mesfePagGuiaCuponMeses, anniofePagGuiaCuponMeses);
				if (diaFePagUltCupon > diaFePagGuiaCupon && mesFePagUltCupon == mesfePagGuiaCuponMeses) {
					if (diaFePagUltCupon == 31) {
						diaFePagUltCupon = 30;
					}
					diferenciaDiaMay = meses - (diaFePagUltCupon - diaFePagGuiaCupon);
				}
				// Si los dias son ultimo de mes, la diferencia de dias sera los meses multiplicado * 30 dias
				if (ultimoMesUltCupon == diaFePagUltCuponVer && ultimoMesPagGuiaCupon == diaFePagGuiaCupon) {
					diferenciaDiaMay = meses;
				}
				// Si el ultimo de mes para la fePagGuiaCupon es el ultimo de febrero, la diferencia de dias sera los meses multiplicado * 30 dias, cuando el dia de fePagUltCupon sea Mayor a el de FepagGuiaCupon
				if (ultimoMesUltCupon != diaFePagUltCupon && ultimoMesPagGuiaCupon == diaFePagGuiaCupon && mesfePagGuiaCuponMeses == 1) {
					diferenciaDiaMay = meses;
				}
				// Se realiza el calculo
				if (valor != 1) {
					Logger.info("", "*******Diferencia entre fechas en dias:" + diferenciaDiaMay);
					basePorDiferencia = new BigDecimal(diferenciaDiaMay).divide(new BigDecimal(360), 7, BigDecimal.ROUND_HALF_EVEN);
					totalDiferencia = totalDiferencia.add(basePorDiferencia);
				} else {
					totalDiferencia = totalDiferencia.add(new BigDecimal(diferenciaDiaMay));
				}
			} else {
				diferenciaDias = (new BigDecimal(meses));
				diferenciaDias = diferenciaDias.add(new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta)));
				int ultimoMesUltCupon = diasDelMes(mesFePagUltCupon, anniofePagUltCuponMeses);
				int ultimoMesPagGuiaCupon = diasDelMes(mesfePagGuiaCuponMeses, anniofePagGuiaCuponMeses);
				if (ultimoMesUltCupon == diaFePagUltCupon && ultimoMesPagGuiaCupon == diaFePagGuiaCupon) {
					diferenciaDias = new BigDecimal(meses);
				}
				if (ultimoMesUltCupon != diaFePagUltCupon && ultimoMesPagGuiaCupon == diaFePagGuiaCupon && mesfePagGuiaCuponMeses == 2) {
					diferenciaDias = new BigDecimal(meses).subtract(new BigDecimal(30));
				}
				if (valor != 1) {
					Logger.info("", "*******Diferencia entre fechas en dias:" + diferenciaDiaMay);
					basePorDiferencia = diferenciaDias.divide(new BigDecimal(360), 7, BigDecimal.ROUND_HALF_EVEN);
					// basePorDiferencia=diferenciaDias;
					totalDiferencia = totalDiferencia.add(basePorDiferencia);
				} else {
					totalDiferencia = totalDiferencia.add(diferenciaDias);
				}
			}
			// Calcula la base por diferencia cuando la BASE es BASE_NL360 o BASE_NL365
		} else if (baseCalculo.equals(BASE_NL360) || baseCalculo.equals(BASE_NL365)) {
			int baseAniio = 0;
			if (baseCalculo.equals(BASE_NL360)) {
				baseAniio = 360;
			} else {
				baseAniio = 365;
			}
			diferenciaDias = new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta));
			int ciclo = anniofePagGuiaCuponMeses - anniofePagUltCuponMeses;
			int bisiestos = 0;
			for (int i = 0; i < ciclo + 1; i++) {
				if (((anniofePagUltCuponMeses % 100 == 0) && (anniofePagUltCuponMeses % 400 == 0)) || ((anniofePagUltCuponMeses % 100 != 0) && (anniofePagUltCuponMeses % 4 == 0))) {
					bisiestos++;
					if (mesfePagGuiaCuponMeses == 1 && diaFePagGuiaCupon <= 28 && anniofePagUltCuponMeses == anniofePagGuiaCuponMeses) {
						bisiestos--;
					}
				}
				anniofePagUltCuponMeses = anniofePagUltCuponMeses + 1;
			}
			diferenciaDias = diferenciaDias.subtract(new BigDecimal(bisiestos));
			if (valor != 1) {
				basePorDiferencia = diferenciaDias.divide(new BigDecimal(baseAniio), 7, BigDecimal.ROUND_HALF_EVEN);
				totalDiferencia = totalDiferencia.add(basePorDiferencia);
			} else {
				totalDiferencia = totalDiferencia.add(diferenciaDias);
			}
		}// fin else if
			// Verifica si la base es actual actual
		else if (baseCalculo.equals(BASE_ACTUAL)) {
			GregorianCalendar actual = new GregorianCalendar();
			int baseActual = 0;
			baseActual = actual.isLeapYear(actual.get(GregorianCalendar.YEAR)) ? 366 : 365;
			diferenciaDias = new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta));
			if (valor != 1) {
				basePorDiferencia = diferenciaDias.divide(new BigDecimal(baseActual), 7, BigDecimal.ROUND_HALF_EVEN);
				totalDiferencia = totalDiferencia.add(basePorDiferencia);
			} else {
				totalDiferencia = totalDiferencia.add(diferenciaDias);
			}
		}
		// Calcula la base por la diferencia de dias para acltual 365 o actual 366
		else if (baseCalculo.equals(BASE_ACT365)) {
			int ciclo = anniofePagGuiaCuponMeses - anniofePagUltCuponMeses;// diferencia de a�os
			int diferencias = 0;
			for (int i = 0; i < ciclo + 1; i++) {
				diferencias = 0;
				int base365_366 = 0;
				String anio = String.valueOf(anniofePagUltCuponMeses);
				String fechaUltimoAnio = anio.concat("-12-31");
				base365_366 = fePagUltCuponMeses.isLeapYear(anniofePagUltCuponMeses) ? 366 : 365;
				Date fechaUltimoAno = formato.parse(fechaUltimoAnio);
				if (fechaUltimoAno.compareTo(fechahasta) == -1) {
					diferencias = (fechasDiferenciaEnDias(fechadesde, fechaUltimoAno));
					BigDecimal divide = new BigDecimal(diferencias).divide(new BigDecimal(base365_366), 7, BigDecimal.ROUND_HALF_EVEN);
					totalDiferencia = totalDiferencia.add(divide);
					anniofePagUltCuponMeses++;
				} else {
					diferencias = 0;
					String anioActual = String.valueOf(anniofePagUltCuponMeses);
					String fechaPrimerAnio = anioActual.concat("-01-01");
					Date fechaPrimerAnioDate = formato.parse(fechaPrimerAnio);
					diferencias = (fechasDiferenciaEnDias(fechaPrimerAnioDate, fechahasta)) + 1;
					BigDecimal divide = new BigDecimal(diferencias).divide(new BigDecimal(base365_366), 7, BigDecimal.ROUND_HALF_EVEN);
					totalDiferencia = totalDiferencia.add(divide);
					anniofePagUltCuponMeses++;
				}
			}// fin for
		}// fin BASE_ACT365
		return totalDiferencia;
	}

	/**
	 * M&eacute;todo para realizar el calculo de pago para un cupon
	 * 
	 * @param BigDecimal
	 *            calculoGenerado,int cantidadTitulos,BigDecimal intereses
	 * @return BigDecimal
	 * @throws Exception
	 */
	public static BigDecimal calculoCupones(BigDecimal calculoGenerado, double cantidadTitulos, BigDecimal intereses) throws Exception {
		/*
		 * El c�lculo de cupones siempre debe llegar con el valor residual de la posici�n en custodia que tenga el cliente esto es importante porque hay algunos t�tulos que tienen redenciones de capital y hay calcular el valor residual de la posici�n en custodia. Para obtener el valor residual del valor nominal que posee el cliente invoque el m�todo valorResidual de esta clase antes de invocar
		 * este m�todo.
		 */
		Logger.info("", "********************INICIO PROCESO DE CALCULO*************************************");
		BigDecimal totalCalculoCupon = (new BigDecimal(cantidadTitulos));
		Logger.info("", "*******Cantidad Titulos Custodia          :" + totalCalculoCupon);
		Logger.info("", "*******Dias de Diferencias/a�o financiero :" + calculoGenerado);
		intereses = intereses.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_EVEN);
		Logger.info("", "*******Intereses						  :" + intereses);
		totalCalculoCupon = totalCalculoCupon.multiply(intereses);
		Logger.info("", "*******Cantidad Titulos * Intereses		  :" + totalCalculoCupon);
		// Logger.info("","Resultado dividido entre el anio financiero>>>>"+totalCalculoCupon);
		totalCalculoCupon = totalCalculoCupon.multiply(calculoGenerado);
		Logger.info("", "*******multiplicado * Diferencia Dias	  :" + totalCalculoCupon);
		Logger.info("", "********************FIN    PROCESO DE CALCULO*************************************");
		return totalCalculoCupon;
	}

	/**
	 * Cambia el formato de fecha de yyyy-MM-dd a dd-MM-yyyy
	 * 
	 * @param String
	 *            fecha
	 * @return String
	 * @throws Exception
	 */
	public static String cambioFormatoFecha(String fecha) throws Exception {
		// Cambio de formato de fecha dd-mm-yyyy fecha desde Venta
		try {
			String fecha1 = fecha.substring(0, 4);
			String fecha2 = fecha.substring(5, 7);
			String fecha3 = fecha.substring(8, 10);
			String all = fecha3 + "-".concat(fecha2) + "-".concat(fecha1);
			return all;
		} catch (Exception e) {
			return fecha;
		}

	}

	/**
	 * Retorna el a�o financiero en dias dependiendo de la base
	 * 
	 * @param String
	 *            base
	 * @return int diasAnio
	 * @throws Exception
	 */
	public static int diasBaseCalculo(String base) throws Exception {
		int diasAnio = 0;
		base = base.trim();
		if (base.equals(BASE_A360) || base.equals(BASE_BOND) || base.equals(BASE_EBOND) || base.equals(BASE_30F360) || base.equals(BASE_NL360)) {
			diasAnio = 360;
		}
		if (base.equals(BASE_A365) || base.equals(BASE_A365GT) || base.equals(BASE_ACT365) || base.equals(BASE_NL365)) {
			diasAnio = 365;
		}
		if (base.equals(BASE_ACTUAL)) {
			GregorianCalendar actual = new GregorianCalendar();
			diasAnio = actual.isLeapYear(actual.get(GregorianCalendar.YEAR)) ? 366 : 365;
		}
		return diasAnio;
	}

	/**
	 * Obtiene la fecha valor para la aplicacion de una operacion financiera, a partir de la fecha actual m&aacute;s los d&iacute;as laborales configurados en el sistema
	 * 
	 * @param dso
	 *            , Datasource
	 * @return Fecha Valor obtenida
	 * @throws Exception
	 */
	public static Date calcularFechaValor(DataSource dso) throws Exception {

		Calendar hoy = Calendar.getInstance();// obtener fecha actual

		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

		// obtener dias laborales para venta de titulos
		String parametro = com.bdv.infi.dao.ParametrosDAO.listarParametros("DIAS-LABORABLES", dso);

		if (parametro == null || parametro.equals("")) {
			parametro = "1";
		}

		int diasLaborales = Integer.parseInt(parametro);

		// sumar dias laborales a la fecha actual
		hoy.add(Calendar.DATE, +(diasLaborales));

		// obtenes Date
		Date fechaValor = hoy.getTime();
		String fechaValorSt = sdf.format(fechaValor);

		// verificar si el dia obtenido es valido como dia laboral (No: domingos, sabados o feriados)
		boolean diaPermitido = false;
		// mientras el dia no es permitido como laboral, sumar 1 dia a la fecha valor obtenida
		while (diaPermitido == false) {

			com.bdv.infi.dao.CalendarioDAO calendarioFeriadosDAO = new com.bdv.infi.dao.CalendarioDAO(dso);
			calendarioFeriadosDAO.listar(fechaValorSt);

			if (calendarioFeriadosDAO.getDataSet().count() > 0 || diaSabadoODomingo(fechaValor)) { // si es dia feriado o sabado o domingo
				// sumar un dia a la fecha valor calculada
				hoy.add(Calendar.DATE, +1);
				fechaValor = hoy.getTime();
				fechaValorSt = sdf.format(fechaValor);

			} else {
				diaPermitido = true;
			}

		}

		return fechaValor;
	}

	/**
	 * Verifica dada una fecha si el dia de la semana es sabado o es domingo
	 * 
	 * @param hoy
	 *            fecha a verificar
	 * @return true si es sabado o domingo, false en caso contrario
	 */
	public static boolean diaSabadoODomingo(Date fecha) {
		// 0: domingo, 6: Sabado
		if (fecha.getDay() == 0 || fecha.getDay() == 6)
			return true;
		else
			return false;

	}

	/**
	 * Obtiene un numero de Rif eliminando el prefijo de tipo de persona
	 * 
	 * @param cadenaRif
	 * @return
	 * @throws Exception
	 */
	public static String obtenerNumeroRifCI(String cadenaRifCI) throws Exception {
		String numeroRif = "";
		// Formato de Rif con V-, E-, J-, G-
		try {
			numeroRif = cadenaRifCI.substring(2, cadenaRifCI.length());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("El Rif/C&eacute;dula " + cadenaRifCI + " no posee un formato v&aacute;lido.");
		}

		return numeroRif;
	}

	/**
	 * Obtiene la fecha mas actual en la cual se pago el cup&oacute;n o amortizaci&oacute;n del t&iacute;tulo
	 * 
	 * @param idTitulo
	 * @return
	 * @throws Exception
	 */
	public String obtenerFechaUltimoPagoCuponAmortizacion(String idTitulo, DataSource _dso) throws Exception {
		String fecha = "";
		DataSet _aux = null;

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT to_char(IPAYDATE,'").append(ConstantesGenerales.FORMATO_FECHA).append("') as fecha").append(" FROM SECS WHERE SECID= '");
		sql.append(idTitulo).append("'");
		sql.append(" AND IPAYDATE <= TO_DATE(SYSDATE, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE).append("')");
		sql.append(" ORDER BY IPAYDATE DESC");

		_aux = db.get(_dso, sql.toString());

		if (_aux.next()) {
			fecha = _aux.getValue("fecha");
		} else {
			// Si el primer cup�n no ha sido pagado busca la primera fecha
			sql = new StringBuffer();
			sql.append("SELECT to_char(intstrtdte,'").append(ConstantesGenerales.FORMATO_FECHA).append("') as fecha").append(" FROM SECS WHERE SECID= '");
			sql.append(idTitulo).append("'");
			sql.append(" ORDER BY intstrtdte ");
			_aux = db.get(_dso, sql.toString());

			if (_aux.next()) {
				fecha = _aux.getValue("fecha");
			}
		}

		return fecha;
	}

	/**
	 * busca las cuentas que posee el cliente en altair
	 * 
	 * @param usuarioWeb
	 * @param cedulaCliente
	 * @param tipoPersona
	 * @param direccionIp
	 * @param _app
	 * @param _req
	 * @param mensajes
	 * @return
	 * @throws Exception
	 */
	public DataSet buscarCuentasCliente(String usuarioWeb, String cedulaCliente, String tipoPersona, String direccionIp, ServletContext _app, HttpServletRequest _req, DataSet mensajes) throws Exception {
		String userWebServices = "";
		DataSet cte_ctas_nacionales = new DataSet();

		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, (CredencialesDeUsuario) _req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));

		try {
			// buscar usuario de WebServices
			userWebServices = usuarioWeb;

			try {

				// buscar cuentas asociadas al cliente
				ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentas(cedulaCliente, tipoPersona, userWebServices, direccionIp);
				cte_ctas_nacionales = manejadorDeClientes.cargarDataSet(listaCuentas);

				Logger.info(this, "CUENTAS------------------->>>>>>>>>>>>><<" + cte_ctas_nacionales);

			} catch (Exception e) {
				e.printStackTrace();
				mensajes.addNew();
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas del cliente en arquitectura extendida");
			}// FIN catch

		} catch (Exception e) {
			e.printStackTrace();
			mensajes.addNew();
			mensajes.setValue("mensaje_error_user_webs", "Error consultando el usuario de web services");
		}// FIN catch

		// Se retorna el dataset con las cuentas
		return cte_ctas_nacionales;
	}// FIN buscarCuentasCliente

	/**
	 * Recibe la Excepcion y retorna un String con el Stacktrace (Traza)
	 * 
	 * @param Exception
	 *            e
	 * @return String
	 * @throws Exception
	 */
	public static String stackTraceException(Exception e) {
		// Variable Strinbuffer
		StringBuffer stringBuffer = new StringBuffer();

		// Objetos para acceder a la traza
		StackTraceElement stackTraceElement[] = null;
		stackTraceElement = e.getStackTrace();

		// Recorremos el arreglo de la traza para agregarlo a un stringbuffer
		for (int i = 0; i < stackTraceElement.length; i++) {
			if (i == 0) {
				stringBuffer.append("\r\n" + stackTraceElement[i] + "\r\n");
			} else {
				stringBuffer.append(stackTraceElement[i] + "\r\n");
			}

		}

		// Retornamos el String de la traza
		return stringBuffer.toString();
	}

	/**
	 * Recibe la Excepcion y retorna un String con el Stacktrace (Traza)
	 * 
	 * @param Exception
	 *            e
	 * @return String
	 * @throws Exception
	 */
	public static String stackTraceException(Throwable e) throws Exception {
		// Variable Strinbuffer
		StringBuffer stringBuffer = new StringBuffer();

		// Objetos para acceder a la traza
		StackTraceElement stackTraceElement[] = null;
		stackTraceElement = e.getStackTrace();

		// Recorremos el arreglo de la traza para agregarlo a un stringbuffer
		for (int i = 0; i < stackTraceElement.length; i++) {
			if (i == 0) {
				stringBuffer.append("\r\n" + stackTraceElement[i] + "\r\n");
			} else {
				stringBuffer.append(stackTraceElement[i] + "\r\n");
			}
		}

		// Retornamos el String de la traza
		return stringBuffer.toString();
	}

	/**
	 * Arma un DataSet con las transacciones que s�lo puede visualizar el �rea de tesorer�a en sus consultas
	 * 
	 * @return DataSet con las transacciones
	 * @exception Lanza
	 *                una excepci�n en caso de error
	 */
	public static DataSet transaccionesConsultaOrdenes() throws Exception {

		// Transacciones
		DataSet dTransacciones = new DataSet();
		dTransacciones.append("transaccion", java.sql.Types.VARCHAR);

		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.CANCELACION_ORDEN);
		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.COBRO_FINANCIAMIENTO);
		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.ORDEN_PAGO);
		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.ORDEN_VEHICULO);
		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.PACTO_RECOMPRA);
		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.TOMA_DE_ORDEN);
		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		dTransacciones.addNew();
		dTransacciones.setValue("transaccion", TransaccionNegocio.VENTA_TITULOS);
		return dTransacciones;
	}

	/**
	 * Usa una cache para almacenar el porcentaje amortizado pagado a la fecha del cup�n
	 * 
	 * @param idTitulo
	 *            id del t�tulo a buscar
	 * @param fechaInicioCupon
	 *            fecha de inicio del cup�n del t�tulo a pagar
	 * @param pagoCuponesDao
	 *            el dao instanciado para la busqueda de cupones
	 * @param valorNominalCustodia
	 *            valor nominal que posee el cliente en custodia del t�tulo a calcular
	 */
	public static double valorResidual(String idTitulo, Date fechaInicioCupon, PagoCuponesDao pagoCuponesDao, double valorNominalCustodia) throws Exception {
		double amortizacionAcumulada = 0; // Amortizaci�n acumulada
		Logger.debug("", "Valor Nominal " + valorNominalCustodia);
		amortizacionAcumulada = pagoCuponesDao.obtenerAmortizacionHistorica(idTitulo, fechaInicioCupon);
		Logger.debug("", "Amortizaci�n obtenida " + amortizacionAcumulada);
		double valorResidual = valorNominalCustodia - (valorNominalCustodia * amortizacionAcumulada);
		Logger.info("", "Valor residual " + valorResidual);
		return valorResidual;
	}

	/**
	 * C�lcula el monto de inter�s que se debe aplicar sobre el cup�n de un determinado t�tulo
	 * 
	 * @param idTitulo
	 *            id del t�tulo que est� aplicando cup�n
	 * @param totalInteres
	 *            Total de interes calculado sobre el cu�l se debe aplicar el inter�s
	 * @param monedaPagoTitulo
	 *            moneda de pago del t�tulo
	 * @param dso
	 *            DataSource que posee la conexi�n a la base de datos
	 * @return total de interes que se debe aplicar sobre el monto de cup�n a pagar
	 * @throws Exception
	 *             Lanza una excepci�n si ocurre un error.
	 */
	public static BigDecimal calculoComisionParaTitulo(String idTitulo, BigDecimal totalInteres, String monedaPagoTitulo, DataSource dso) throws Exception {
		// Calcula comisi�n para el t�tulo
		// Carga la estructura tarifaria general
		CustodiaEstructuraTarifariaDAO custodiaTarifasDao = new CustodiaEstructuraTarifariaDAO(dso);
		CustodiaEstructuraTarifaria custodiaTarifas = custodiaTarifasDao.listarEstructura();
		CustodiaComisionTitulo custodiaComisionTitulo = null;
		BigDecimal calculoComision = new BigDecimal(0);

		ConversionMontos conversionMontos = new ConversionMontos(dso);

		if (custodiaTarifas != null) {
			if (custodiaTarifas.getTitulos().containsKey(idTitulo)) {
				// Se obtiene la clase
				custodiaComisionTitulo = custodiaTarifas.getTitulos().get(idTitulo);
				if (custodiaComisionTitulo.getPctComision() > 0) {
					calculoComision = totalInteres.multiply(new BigDecimal(custodiaComisionTitulo.getPctComision()));
					calculoComision = calculoComision.divide(new BigDecimal(100));
				} else if (custodiaComisionTitulo.getMontoComision() > 0) {
					if (monedaPagoTitulo.equals(custodiaComisionTitulo.getMonedaComision())) {
						calculoComision = new BigDecimal(custodiaComisionTitulo.getMontoComision());
					} else {
						calculoComision = conversionMontos.convertir(custodiaComisionTitulo.getMonedaComision(), new BigDecimal(custodiaComisionTitulo.getMontoComision()), monedaPagoTitulo);
						calculoComision = totalInteres.subtract(calculoComision);
					}
				}
			}
		}
		return calculoComision;
	}

	/**
	 * Busca el digito verificador del correspondiente al rif o c�dula, dependiendo del tipo de persona
	 * 
	 * @param rif
	 *            con longitud de 8 digitos num�ricos y tipo de persona. Ej: V06289511
	 * @return digito verificador del rif/cedula
	 * @throws Exception
	 */
	/*
	 * TTS-544 TTS-544 - Creaci�n, Modificaci�n y Transacci�n de Refundici�n del Tipo de Documento "C" (Comunas) Se comenta para centralizar el calculo del digito verificador
	 */
	/*
	 * public static String digitoVerificardor(String rif) throws Exception {
	 * 
	 * int dig11 = 0, totdig = 0, col = 0, colin = 0, colout = 0, pdigvrf = 0, temporal = 0; String dig1 = "", dig2 = "", dig3 = "", dig4 = "", dig5 = "", dig6 = "", dig7 = "", dig8 = "", dig9 = ""; colin = 1; colout = 3;
	 * 
	 * dig1 = rif.substring(0, 1); dig2 = rif.substring(1, 2); temporal = Integer.parseInt(dig2) * 3; dig2 = String.valueOf(temporal);
	 * 
	 * dig3 = rif.substring(2, 3); temporal = Integer.parseInt(dig3) * 2; dig3 = String.valueOf(temporal);
	 * 
	 * dig4 = rif.substring(3, 4); temporal = Integer.parseInt(dig4) * 7; dig4 = String.valueOf(temporal);
	 * 
	 * dig5 = rif.substring(4, 5); temporal = Integer.parseInt(dig5) * 6; dig5 = String.valueOf(temporal);
	 * 
	 * dig6 = rif.substring(5, 6); temporal = Integer.parseInt(dig6) * 5; dig6 = String.valueOf(temporal);
	 * 
	 * dig7 = rif.substring(6, 7); temporal = Integer.parseInt(dig7) * 4; dig7 = String.valueOf(temporal);
	 * 
	 * dig8 = rif.substring(7, 8); temporal = Integer.parseInt(dig8) * 3; dig8 = String.valueOf(temporal);
	 * 
	 * dig9 = rif.substring(8, 9); temporal = Integer.parseInt(dig9) * 2; dig9 = String.valueOf(temporal);
	 * 
	 * if (dig1.equals("V")) { dig11 = 1; } if (dig1.equals("E")) { dig11 = 2; } if (dig1.equals("J")) { dig11 = 3; } if (dig1.equals("G")) { dig11 = 5; } if (dig1.equals("W")) { dig11 = 9; } totdig = Integer.parseInt(dig2) + Integer.parseInt(dig3) + Integer.parseInt(dig4) + Integer.parseInt(dig5) + Integer.parseInt(dig6) + Integer.parseInt(dig7) + Integer.parseInt(dig8) + Integer.parseInt(dig9) +
	 * (dig11 * 4); while (totdig >= 11) { totdig = totdig - 11; } if (totdig == 0 || totdig == 1) { pdigvrf = 0; } else if (totdig != 0 || totdig != 1) { pdigvrf = totdig - 11; }
	 * 
	 * return rif.concat(String.valueOf(Math.abs(pdigvrf)));
	 * 
	 * }
	 */// fin del metodo . . .

	/**
	 * Busca el digito verificador del correspondiente al rif o c�dula, dependiendo del tipo de persona Este m�todo puede retornar el digito verificador solamente
	 * 
	 * @param rif
	 *            con longitud de 8 digitos num�ricos y tipo de persona. Ej: V06289511
	 * @return digito verificador del rif/cedula
	 * @return retornarSoloDigVerif indica si solo se quiere el digito verificador o el valor concatenado
	 * @throws Exception
	 */
	/*
	 * TTS-544 TTS-544 - Creaci�n, Modificaci�n y Transacci�n de Refundici�n del Tipo de Documento "C" (Comunas) Se incluye el nuevo manejo de documento tipo "C"
	 */
	public static String digitoVerificador(String rif, boolean retornarSoloDigVerif) throws Exception {

		int dig11 = 0, totdig = 0, col = 0, colin = 0, colout = 0, pdigvrf = 0, temporal = 0;
		String dig1 = "", dig2 = "", dig3 = "", dig4 = "", dig5 = "", dig6 = "", dig7 = "", dig8 = "", dig9 = "";
		colin = 1;
		colout = 3;

		dig1 = rif.substring(0, 1);
		dig2 = rif.substring(1, 2);
		temporal = Integer.parseInt(dig2) * 3;
		dig2 = String.valueOf(temporal);

		dig3 = rif.substring(2, 3);
		temporal = Integer.parseInt(dig3) * 2;
		dig3 = String.valueOf(temporal);

		dig4 = rif.substring(3, 4);
		temporal = Integer.parseInt(dig4) * 7;
		dig4 = String.valueOf(temporal);

		dig5 = rif.substring(4, 5);
		temporal = Integer.parseInt(dig5) * 6;
		dig5 = String.valueOf(temporal);

		dig6 = rif.substring(5, 6);
		temporal = Integer.parseInt(dig6) * 5;
		dig6 = String.valueOf(temporal);

		dig7 = rif.substring(6, 7);
		temporal = Integer.parseInt(dig7) * 4;
		dig7 = String.valueOf(temporal);

		dig8 = rif.substring(7, 8);
		temporal = Integer.parseInt(dig8) * 3;
		dig8 = String.valueOf(temporal);

		dig9 = rif.substring(8, 9);
		temporal = Integer.parseInt(dig9) * 2;
		dig9 = String.valueOf(temporal);

		if (dig1.equals("V")) {
			dig11 = 1;
		}
		if (dig1.equals("E")) {
			dig11 = 2;
		}
		if (dig1.equals("J") || dig1.equals("C")) { // Se incluye el nuevo manejo de documento tipo "C"
			dig11 = 3;
		}
		if (dig1.equals("G")) {
			dig11 = 5;
		}
		if (dig1.equals("W")) {
			dig11 = 9;
		}
		totdig = Integer.parseInt(dig2) + Integer.parseInt(dig3) + Integer.parseInt(dig4) + Integer.parseInt(dig5) + Integer.parseInt(dig6) + Integer.parseInt(dig7) + Integer.parseInt(dig8) + Integer.parseInt(dig9) + (dig11 * 4);
		while (totdig >= 11) {
			totdig = totdig - 11;
		}
		if (totdig == 0 || totdig == 1) {
			pdigvrf = 0;
		} else if (totdig != 0 || totdig != 1) {
			pdigvrf = totdig - 11;
		}
		if (retornarSoloDigVerif) {
			return String.valueOf(Math.abs(pdigvrf));
		} else {
			return rif.concat(String.valueOf(Math.abs(pdigvrf)));
		}

	}// / fin del metodo . . .

	/**
	 * Crea mascara para formatear numeros con la cantidad de decimales indicados por parametro
	 * 
	 * @param numDecimales
	 * @return
	 */
	public static DecimalFormat crearMascaraDecimales(int numDecimales) {

		String ceros = "";

		for (int k = 0; k < numDecimales; k++) {
			ceros = ceros + "0";
		}

		DecimalFormat nf = new DecimalFormat("###." + ceros);
		return nf;
	}

	/**
	 * Resta los dias indicados a una fecha dada
	 * 
	 * @param fecha
	 *            fecha de base
	 * @param numeroDeDias
	 *            dias a restar
	 * @return fecha calculada
	 */
	public static Date restarDias(Date fch, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
		cal.add(Calendar.DATE, -dias);
		return cal.getTime();
	}

	/**
	 * Retorna el rif del cliente con X caracteres, rellena a la izquierda con ceros
	 * 
	 * @param rif
	 *            RIF/CED del cliente
	 * @param caracteres
	 *            numero de caracteres deseado
	 * @return String rif con formato
	 */
	public static String rifAXCaracteres(String rif, int caracteres) throws Exception {
		if (rif.length() < 11) {
			return (rif.substring(0, 2)) + (rellenarCaracteresTrunc(obtenerNumeroRifCI(rif), '0', caracteres - 2, false));
		}
		return rif;
	}

	public static String[] fechaFormateada(String fechaActual) {

		String[] fechas = new String[3];

		String dia = null;
		dia = fechaActual.toString().substring(0, 2);

		String mes = null;
		int mesNumerico = Integer.parseInt(fechaActual.toString().substring(3, 5));

		String anio = null;
		anio = fechaActual.toString().substring(6, 10);

		switch (mesNumerico) {
		case 1:
			mes = "Enero";
			break;
		case 2:
			mes = "Febrero";
			break;
		case 3:
			mes = "Marzo";
			break;
		case 4:
			mes = "Abril";
			break;
		case 5:
			mes = "Mayo";
			break;
		case 6:
			mes = "Junio";
			break;
		case 7:
			mes = "Julio";
			break;
		case 8:
			mes = "Agosto";
			break;
		case 9:
			mes = "Septiembre";
			break;
		case 10:
			mes = "Octubre";
			break;
		case 11:
			mes = "Noviembre";
			break;
		case 12:
			mes = "Diciembre";
			break;
		}

		fechas[0] = dia;
		fechas[1] = mes;
		fechas[2] = anio;

		return fechas;
	}

	public static boolean cadenaVacia(String cadena) {
		for (int i = 0; i < cadena.length(); i++) {
			if (cadena.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	public static int caculadorDiferenciaDiasDeFechas(Date fechaInicial, Date fechaFinal) {

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechaInicioString = df.format(fechaInicial);
		try {
			fechaInicial = df.parse(fechaInicioString);
		} catch (Exception ex) {
		}

		String fechaFinalString = df.format(fechaFinal);
		try {
			fechaFinal = df.parse(fechaFinalString);
		} catch (Exception ex) {
		}

		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ((int) dias);
	}

	/**
	 * Reemplaza acentos en formato html
	 * 
	 * @param cadena
	 *            a reemplazarle los acentros html
	 * @return cadena con acentos
	 */
	public static String replaceAcentosHTML(String cad) {
		cad = cad.toLowerCase();
		cad = cad.replaceAll("&aacute;", "�");
		cad = cad.replaceAll("&eacute;", "�");
		cad = cad.replaceAll("&iacute;", "�");
		cad = cad.replaceAll("&oacute;", "�");
		cad = cad.replaceAll("&uacute;", "�");
		cad = cad.substring(0, 1).toUpperCase() + cad.substring(1, cad.length());
		return cad;
	}

	/**
	 * Verifica si existe un proceso de tipo espec�fico en ejecuci�n
	 * 
	 * @param tipoProceso
	 * @return
	 * @throws Exception
	 */
	public static boolean procesoEnEjecuci�n(String tipoProceso, DataSource dso) throws Exception {

		ProcesosDAO procesoDAO = new ProcesosDAO(dso);

		procesoDAO.listarPorTransaccionActiva(tipoProceso);

		if (procesoDAO.getDataSet().count() > 0) {
			return true;

		} else
			return false;
	}

	/**
	 * Convierte un archivo bLob de base de datos a un archivo java.io.File de java
	 * 
	 * @param filename
	 * @param archivoBlob
	 * @return archivo tipo File java
	 * @throws Exception
	 */
	public static File blobToFile(String filename, Blob archivoBlob) throws Exception {
		File blobFile = new File(filename);
		try {

			FileOutputStream outStream = new FileOutputStream(blobFile);
			InputStream inStream = archivoBlob.getBinaryStream();

			int length = -1;
			int size = Integer.parseInt(String.valueOf(archivoBlob.length()));
			byte[] buffer = new byte[size];

			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
				outStream.flush();
			}

			inStream.close();
			outStream.close();
		} catch (Exception e) {
			Logger.error("Error convirtiendo el archivo Blob en Base de Datos.. ", e.getMessage());
			throw new Exception("Error convirtiendo el archivo Blob en Base de Datos.. ");
		}

		return blobFile;

	}

	public static String getHoraActual(String formato) {
		if ((formato == null) || formato.equalsIgnoreCase(""))
			formato = ConstantesGenerales.FORMATO_FECHA;// dd-MM-yyyy by DEFAULT

		SimpleDateFormat sdfDate = new SimpleDateFormat(formato);// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	/**
	 * @param date1
	 *            , date2
	 * @return retorna valor negativo si la fecha date1 es anterior a date2
	 * @return retorna 0 si las fechas son iguales
	 * @return retorna valor positivo si la fecha date1 es posteriir a date2
	 **/
	public static long compareDates(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}

	/**
	 * @author nm29643 INFI_TTS_466 28/07/2014 Funcion que enmascara un nro de Cuenta
	 * @param cta
	 *            Nro de Cuenta a enmascarar
	 * @param cant2show
	 *            cantidad de d�gitos de la cuenta a mostrar
	 * @param charRelleno
	 *            caracter utlizado para enmascarar los d�gitos
	 * @param showEnd
	 *            especifica que los d�gitos a mostrar son los finales, de lo contrario se muestran los iniciales
	 * @return Nro de Cuenta enmascarado
	 */
	public static String enmascararCta(String cta, int cant2show, String charRelleno, boolean showEnd) {
		String relleno = "";
		if (cta != null && cta.length() > 0) {
			if (cta.length() > cant2show) {
				int cantRelleno = cta.length() - cant2show;
				for (int i = 0; i < cantRelleno; i++)
					relleno += charRelleno;
				if (showEnd)
					return relleno.concat(cta.substring(cta.length() - cant2show, cta.length()));
				else
					return cta.substring(0, cant2show).concat(relleno);
			}
		}
		return relleno;
	}

	public static boolean isNumeric(char caracter) {
		try {
			Integer.parseInt(String.valueOf(caracter));
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

//	@SuppressWarnings("unused")
//	public boolean sololetras(String string) {
//		System.out.println("Entro validacion caracteres");
//		String out = "";
//		String filtro = "^*[-])(?==.*[A-Z])(?=.*[@#$%^&+}!#$%&()*+,-.:;=?@[]^`{|}~";
//		for (int i = 0; i < string.length(); i++) {
//			System.out.println("filtro.indexOf(string.charAt(i)) :" + filtro.indexOf(string.charAt(i)));
//			if (filtro.indexOf(string.charAt(i)) == -1) {
//				System.out.println("string.charAt(i) " + string.charAt(i));
//				out += string.charAt(i);
//				
//			}else{
//				System.out.println("llego trueeeeeeeeeeeeeee");
//				return true;
//			}
//
//		}
//
//		return false;
//	}

	public static String depurarString(String cadena) {
		String cadenaRetornar = "";
		for (short indice = 0; indice < cadena.length(); indice++) {
			char caracter = cadena.charAt(indice);
			if (isNumeric(caracter)) {
				cadenaRetornar += caracter;
			}
		}

		cadenaRetornar = completarCaracterDerecha(cadenaRetornar, 12, "0");
		return cadenaRetornar.substring(0, 12).trim();
	}

	public static String completarCaracterDerecha(String str, int size, String caracter) {
		String temp = "";
		for (int i = (size - str.length()); i > 0; --i)
			temp = temp + caracter;
		return str + temp;
	}

	public static String completarCaracterIzquierda(String str, int size, String caracter) {
		String temp = "";
		for (int i = (size - str.length()); i > 0; --i)
			temp = temp + caracter;
		return temp + str;
	}

	public static String openFileToString(byte[] _bytes) {
		String file_string = "";

		for (int i = 0; i < _bytes.length; i++) {
			file_string += (char) _bytes[i];
		}

		return file_string;
	}

	public static void byteToFile(byte[] _bytes, String nombreArchivo) throws IOException {
		FileOutputStream fileOuputStream = new FileOutputStream(nombreArchivo);
		fileOuputStream.write(_bytes);
		fileOuputStream.close();
	}

	public static byte[] fileToByte(String filePath) {

		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;

		try {

			File file = new File(filePath);
			bytesArray = new byte[(int) file.length()];

			// read file into bytes[]
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return bytesArray;

	}

	public static java.net.URL configurarProxyYCredencialesStubAxisWSBCV(org.apache.axis.client.Stub stub, DataSource dso, String constantePropertyEndPoint, String constanteSistemaCredenciales) throws org.apache.axis.AxisFault, java.lang.Exception {
		java.net.URL endpointURL = null;
		DataSet _credenciales = new DataSet();
		CredencialesDAO credencialesDAO = new CredencialesDAO(dso);
		credencialesDAO.listarCredencialesPorTipo(constanteSistemaCredenciales);
		_credenciales = credencialesDAO.getDataSet();
		Propiedades propiedades = Propiedades.cargar();
		String userName = "";
		String clave = "";

		// SE CONFIGURA EL URL
		endpointURL = new URL(propiedades.getProperty(constantePropertyEndPoint));
		Logger.info("configurarProxyYCredencialesStubAxisWSBCV", "endpointURL:  " + endpointURL);

		if (_credenciales.next()) {
			// SE CONFIGURA QUE SE USARA PROXY
			if (propiedades.getProperty("use_https_proxy").equals("1")) {
				Utilitario.configurarProxy();
			}

			String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
			String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

			TripleDes desc = new TripleDes();

			userName = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("USUARIO"));
			clave = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("CLAVE"));
		} else {
			Logger.error(stub, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: " + ConstantesGenerales.WS_BCV_MENUDEO);
			throw new org.bcv.service.Exception();
		}

		Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
		if (headers == null) {
			headers = new Hashtable();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}
		headers.put("Username", userName);
		headers.put("Password", clave);

		return endpointURL;
	}

	public static boolean longitudValida(String str, int sizeMin, int sizeMax) {
		if (!(str != null && (str.length() >= sizeMin && str.length() <= sizeMax))) {
			return false;
		}
		return true;
	}

	public static Date fechaDateFormateada(Date fecha, String formato) throws ParseException {
		Date fechaRetorno = null;
		String fechaString = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA2).format(fecha);
		fechaRetorno = Utilitario.StringToDate(fechaString, ConstantesGenerales.FORMATO_FECHA2);
		return fechaRetorno;
	}

	public static Date fechaDateFormateada1(Date fecha, String formato) throws ParseException {
		Date fechaRetorno = null;
		String fechaString = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_MENUDEO).format(fecha);
		fechaRetorno = Utilitario.StringToDate(fechaString, ConstantesGenerales.FORMATO_FECHA_MENUDEO);
		return fechaRetorno;
	}

	public static boolean betweenDates(Date fechaInicio, Date fechaFin, Date fechaReferencia) throws ParseException {
		int fechaInicioValida = fechaInicioValida = fechaInicio.compareTo(fechaReferencia);
		int fechaFinValida = fechaFinValida = fechaFin.compareTo(fechaReferencia);
		if ((fechaInicioValida <= 0) && (fechaFinValida >= 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static BigDecimal getStringToBigDecimal(String monto, int cantidadEnteros, int cantidadDecimales) {
		BigDecimal result = null;
		if (monto != null && monto.length() > 0) {
			try {
				String entero = monto.substring(0, cantidadEnteros);
				String decimal = monto.substring(cantidadEnteros, cantidadEnteros + cantidadDecimales);
				String numeroFinal = null;
				numeroFinal = entero.concat(".").concat(decimal);
				result = new BigDecimal(numeroFinal);
			} catch (NumberFormatException ex) {
				throw ex;
			}
		}
		return result;
	}

	public static boolean isNumericc(String s) {
		if (s == null || s.equals("")) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	/*
	 * public static void main(String args[]) throws Exception{ Utilitario u= new Utilitario(); System.out.println("Caso 1 C81946284 "+u.digitoVerificador("C81946284", true)); System.out.println("Caso 2 C81957450 "+u.digitoVerificador("C81957450", true)); System.out.println("Caso 3 C83144621 "+u.digitoVerificador("C83144621", true));
	 * System.out.println("Caso 4 C81946304 "+u.digitoVerificador("C81946304", true)); System.out.println("Caso 5 C81957452 "+u.digitoVerificador("C81957452", true));
	 * 
	 * }
	 */
}
