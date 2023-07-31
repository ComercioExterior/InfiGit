package com.bdv.infi.logic.function.document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import megasoft.Util;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * Clase gen&eacute;rica usada para la b&uacute;squeda y mezcla de los datos que necesita una
 * determinada plantilla configurada y que debe ser mostrada en transacciones.
 * Para la mezcla de la plantilla con los datos se debe usar una clase de la
 * fundaci&oacute;n llamada Page. Los datos deben almacenarse en el dataSet (clase de
 * la fundaci&oacute;n) para que sean perfectamente entendidas por la clase Page. Por
 * defecto al instanciar las clases se almacenan en el dataSet valores como
 * fecha actual, dia, mes, año que pueden ser usados en cualquier plantilla.
 */
public abstract class FuncionGenerica {

	/**
	 * lista de documentos (plantillas) que se deben modificar
	 */
	private String[] documentos;
	
	/*** Logger*/
	private org.apache.log4j.Logger logger = Logger.getLogger(FuncionGenerica.class);
	
	private DataSource datasource;

	public void setDataSource(DataSource dso) {
		this.datasource = dso;
	}

	public DataSource getDataSource() {
		return this.datasource;
	}

	/**
	 * Getter of the property <tt>documentos</tt>
	 * 
	 * @return Returns the documentos.
	 * 
	 */
	public Object getDocumentos() {
		return documentos;
	}

	/**
	 * Setter of the property <tt>documentos</tt>
	 * 
	 * @param documentos
	 *            The documentos to set.
	 * 
	 */
	public void setDocumentos(String[] documentos) {
		this.documentos = documentos;
	}

	/**
	 * Constante que representa como se llama el key en el dataSet para el uso
	 * de la fecha actual
	 */
	private final String FECHA_ACTUAL = new SimpleDateFormat("dd-MM-yyyy")
			.format(new Date());

	/**
	 * Getter of the property <tt>FECHA_ACTUAL</tt>
	 * 
	 * @return Returns the FECHA_ACTUAL.
	 * 
	 */
	public String getFECHA_ACTUAL() {
		return FECHA_ACTUAL;
	}

	/**
	 * Constructor que por defecto almacena en el dataSet los valores de fecha
	 * actual, dia, mes y año
	 */
	public FuncionGenerica() {

	}

	/**
	 * Constante que representa como se llama el key en el dataSet con el valor
	 * del año actual
	 */
	private final String ANIO_ACTUAL = new SimpleDateFormat("yyyy")
			.format(new Date());

	/**
	 * Getter of the property <tt>ANIO_ACTUAL</tt>
	 * 
	 * @return Returns the ANIO_ACTUAL.
	 * 
	 */
	public String getANIO_ACTUAL() {
		return ANIO_ACTUAL;
	}

	/**
	 * Constante que representa como se llama el key en el dataSet con el valor
	 * del dia actual
	 */
	private final String DIA_ACTUAL = new SimpleDateFormat("dd")
			.format(new Date());

	/**
	 * Getter of the property <tt>DIA_ACTUAL</tt>
	 * 
	 * @return Returns the DIA_ACTUAL.
	 * 
	 */
	public String getDIA_ACTUAL() {
		return DIA_ACTUAL;
	}

	/**
	 * Constante que representa como se llama el key en el dataSet con el valor
	 * del mes actual
	 */
	private final String MES_ACTUAL = new SimpleDateFormat("MM")
			.format(new Date());

	/**
	 * Getter of the property <tt>MES_ACTUAL</tt>
	 * 
	 * @return Returns the MES_ACTUAL.
	 * 
	 */
	public String getMES_ACTUAL() {
		return MES_ACTUAL;
	}

	/**
	 * DataSet que contiene la informaci&oacute;n de las claves=valor que deben ser
	 * sustituidos dentro de la plantilla
	 */
	private String dataSet;

	/**
	 * Getter of the property <tt>dataSet</tt>
	 * 
	 * @return Returns the dataSet.
	 * 
	 */
	public String getDataSet() {
		return dataSet;
	}

	/**
	 * Setter of the property <tt>dataSet</tt>
	 * 
	 * @param dataSet
	 *            The dataSet to set.
	 * 
	 */
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * M&eacute;todo encargado del proceso de b&uacute;squeda y carga de datos en el DataSet
	 * para que pueda ser sustituido en la plantilla que se debe armar. Se van a
	 * buscar los documentos asociadas a la transacci&oacute;n y se aplica la consulta
	 * para efectuar la sustituci&oacute;n de los datos. Cada uno de los documentos
	 * generados se almacena en la lista de documentos.
	 * 
	 * @param orden
	 *            Objeto orden que contiene informaci&oacute;n necesaria en la b&uacute;squeda
	 *            de datos para la mezcla de los documentos con la data
	 * @param documentos
	 *            lista de documentos(plantillas) asociados a la transaci&oacute;n y
	 *            que se le debe aplicar el proceso de mezcla con los datos
	 * @throws DocumentosException
	 *             excepci&oacute;n que indica que la lista de documentos recibida es
	 *             nula
	 * @throws OrdenException
	 *             excepci&oacute;n que indica que el objeto orden es nulo
	 */
	public abstract void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip)
			throws Exception;

	/**
	 * M&eacute;todo encargado del proceso de b&uacute;squeda y carga de datos en el DataSet
	 * para que pueda ser sustituido en la plantilla que se debe armar. Se van a
	 * buscar los documentos asociadas a la transacci&oacute;n y se aplica la consulta
	 * para efectuar la sustituci&oacute;n de los datos. Cada uno de los documentos
	 * generados se almacena en la lista de documentos.
	 * 
	 * @param orden
	 *            Objeto orden que contiene informaci&oacute;n necesaria en la b&uacute;squeda
	 *            de datos para la mezcla de los documentos con la data
	 * @param documentos
	 *            lista de documentos(plantillas) asociados a la transaci&oacute;n y
	 *            que se le debe aplicar el proceso de mezcla con los datos
	 * @param contexto
	 *            contexto desde el que se llama a la funcion
	 * @param ip
	 *            Direccion IP desde donde se llama la funcion
	 * @param indiceCruce
	 *            Indice del cruce de la orden
	 * @throws DocumentosException
	 *             excepci&oacute;n que indica que la lista de documentos recibida es
	 *             nula
	 * @throws OrdenException
	 *             excepci&oacute;n que indica que el objeto orden es nulo
	 */
	public abstract void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip, int indiceCruce)
			throws Exception;

	
	/**
	 * M&eacute;todo encargado de procesar las plantillas asociadas a una transacci&oacute;n;
	 * Se encarga de sustituir las etiquetas predefinidas en las plantillas por
	 * los valores asociados en el Map que se le pasa como argumento, generando
	 * un documento por cada plantilla procesada con los datos cambiados.
	 */
	public void procesarPlantillas(Map<String, String> matches, LinkedList plantillas)throws Exception {
		int size = plantillas.size(); // La cantidad de plantillas que se procesar&aacute;
		String plantilla = null;
		StringBuilder[] sout = new StringBuilder[size]; // Un array donde almacenar los documentos resultantes
		String etiqueta = "@";
		Map<String, String> megaMap = new HashMap<String, String>();
		megaMap.putAll(matches);
		megaMap.put("fecha", this.getFECHA_ACTUAL());
		megaMap.put("anio", this.getANIO_ACTUAL());
		megaMap.put("dia", this.getDIA_ACTUAL());
		megaMap.put("mes", this.getMES_ACTUAL());
		megaMap.put("nombre_mes", Util.getMonthName(this.getMES_ACTUAL()));
			
		//--Armar campos para logotipo de cartas----------------------------
		
		String rutaLogo1 = ConstantesGenerales.RUTA_IMAGENES + ConstantesGenerales.IMAGEN_BDV;
		String rutaLogo2 = ConstantesGenerales.RUTA_IMAGENES + ConstantesGenerales.IMAGEN_BDV2;
		megaMap.put("logo", "<img border='0' src='"+rutaLogo1+"'/>");
		megaMap.put("logo2", "<img border='0' src='"+rutaLogo2+"' width='965' height='65'/>");
				
		/*
		 * Iterar sobre cada plantilla para reemplazar el contenido de las
		 * etiquetas que posea
		 */
		Iterator itr = megaMap.entrySet().iterator();
		while (itr.hasNext()) {						
			  Map.Entry e = (Map.Entry) itr.next();
		}
		for (int i = 0; i < size; i++) {
			
			/*
			 * Iniciar la variable del sub &iacute;ndice del StringBuilder para
			 * almacenar el archivo resultante
			 */
			sout[i] = new StringBuilder();

			/* Abrir obtener la plantilla */
			plantilla = (String) plantillas.get(i);
			try {				
				
				Iterator it = megaMap.entrySet().iterator();
				while (it.hasNext()) {						
				  Map.Entry e = (Map.Entry) it.next();
				  if (e.getValue() instanceof String){
					  plantilla = plantilla.replaceAll(etiqueta + e.getKey() + etiqueta, (String) e.getValue());
				  }
				}
				/* Añadimos el string modificado al buffer */
				sout[i].append(plantilla.toString());				
				

			} catch (Exception e) {

				logger.info("Error en procesamiento de documento " + e.getMessage());
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			}		
		}
		
		/*
		 * Guardar los documentos que se encuentra en la variable local sout en
		 * la variable de instancia documentos
		 */
		//LinkedList<String> plantillaResultante = new LinkedList<String>();
		documentos = new String[sout.length];
		for (int i = 0; i < sout.length; i++) {
			documentos[i] = sout[i].toString();
			//plantillaResultante.add(sout[i].toString());
		}
		//procesarPlantillasOld(megaMap, plantillaResultante);		
	}

	/**
	 * M&eacute;todo encargado de procesar las plantillas asociadas a una transacci&oacute;n;
	 * Se encarga de sustituir las etiquetas predefinidas en las plantillas por
	 * los valores asociados en el Map que se le pasa como argumento, generando
	 * un documento por cada plantilla procesada con los datos cambiados.
	 */
	public void procesarPlantillasOld(Map matches, LinkedList plantillas) throws Exception{
		int size = plantillas.size(); // La cantidad de plantillas que se procesar&aacute;
		String plantilla = null;
		StringBuilder[] sout = new StringBuilder[size]; // Un array donde almacenar los documentos resultantes
		Pattern patron = Pattern.compile("\\@((\\w)*_?)+\\@", Pattern.MULTILINE); // El patr&oacute;n de b&uacute;squeda
		Matcher matcher = patron.matcher(""); // El objeto que efect&uacute;a la b&uacute;squeda del patr&oacute;n
		String etiqueta = "@";
		Map megaMap = new HashMap();
		megaMap.putAll(matches);
		megaMap.put("fecha", this.getFECHA_ACTUAL());
		megaMap.put("anio", this.getANIO_ACTUAL());
		megaMap.put("dia", this.getDIA_ACTUAL());
		megaMap.put("mes", this.getMES_ACTUAL());
		megaMap.put("mes_actual", Util.getMonthName(this.getMES_ACTUAL()));
		String grupo = "";
		String valor = "";

		/*
		 * Iterar sobre cada plantilla para reemplazar el contenido de las
		 * etiquetas que posea
		 */
		for (int i = 0; i < size; i++) {
			
			/*
			 * Iniciar la variable del sub &iacute;ndice del StringBuilder para
			 * almacenar el archivo resultante
			 */
			sout[i] = new StringBuilder();

			/* Abrir obtener la plantilla */
			
			plantilla = (String) plantillas.get(i);
			
			try {
				StringBuffer result = new StringBuffer();
				matcher.reset(plantilla);
				/* Iterar mientras se encuentren etiquetas en el documento */
				while (matcher.find()) {					
					/*
					 * Preparar el mapa que recibimos para que pueda ser
					 * utilizado en el bucle
					 */
					Iterator it = megaMap.entrySet().iterator();
					
					
					while (it.hasNext()) {						
						Map.Entry e = (Map.Entry) it.next();
						/*
						 * Si la etiqueta es igual a la clave del elemento del mapa
						 * añadir el reemplazo en un buffer temporal y salir del bucle
						 * actual.
						 */
						if (matcher.group().equals(								
								etiqueta + e.getKey() + etiqueta)) {
							matcher.appendReplacement(result, e.getValue()==null?"":e.getValue().toString());						
							break;
						} else {
							/* Si no son iguales, se contin&uacute;a comparando con las claves del mapa */
							continue;
						}
					}					
				}
				/*
				 * Añadimos el string modificado al buffer despu&eacute;s de haber roto
				 * el bucle interno donde hubo un match.
				 */
				matcher.appendTail(result);
				sout[i].append(result.toString());

			} catch (Exception e) {
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			}		
		}
		/*
		 * Guardar los documentos que se encuentra en la variable local sout en
		 * la variable de instancia documentos
		 */
		documentos = new String[sout.length];
		for (int i = 0; i < sout.length; i++) {
			documentos[i] = sout[i].toString();
		}
	}
	
	/**
	 * M&eacute;todo encargado de procesar las plantillas asociadas a una transacci&oacute;n;
	 * Se encarga de sustituir las etiquetas predefinidas en las plantillas por
	 * los valores asociados en el Map que se le pasa como argumento, generando
	 * un documento por cada plantilla procesada con los datos cambiados.
	 */
	public void procesarExpresionRegular(Map matches, String plantilla) throws Exception{
		int size = 1;
		StringBuilder[] sout = new StringBuilder[1]; // Un array donde almacenar los documentos resultantes
		Pattern patron = Pattern.compile("\\@((\\w)*_?)+\\@", Pattern.MULTILINE); // El patr&oacute;n de b&uacute;squeda
		Matcher matcher = patron.matcher(""); // El objeto que efect&uacute;a la b&uacute;squeda del patr&oacute;n
		String etiqueta = "@";
		Map megaMap = new HashMap();

		/*
		 * Iterar sobre cada plantilla para reemplazar el contenido de las
		 * etiquetas que posea
		 */
		for (int i = 0; i < size; i++) {
			
			/*
			 * Iniciar la variable del sub &iacute;ndice del StringBuilder para
			 * almacenar el archivo resultante
			 */
			sout[i] = new StringBuilder();

			/* Abrir obtener la plantilla */
			try {
				StringBuffer result = new StringBuffer();
				matcher.reset(plantilla);
				/* Iterar mientras se encuentren etiquetas en el documento */
				while (matcher.find()) {
					/*
					 * Preparar el mapa que recibimos para que pueda ser
					 * utilizado en el bucle
					 */
					Iterator it = megaMap.entrySet().iterator();
					
					
					while (it.hasNext()) {						
						Map.Entry e = (Map.Entry) it.next();
						/*
						 * Si la etiqueta es igual a la clave del elemento del mapa
						 * añadir el reemplazo en un buffer temporal y salir del bucle
						 * actual.
						 */
						if (matcher.group().equals(								
								etiqueta + e.getKey() + etiqueta)) {
							matcher.appendReplacement(result, e.getValue()==null?"":e.getValue().toString());						
							break;
						} else {
							/* Si no son iguales, se contin&uacute;a comparando con las claves del mapa */
							continue;
						}
					}					
				}
				/*
				 * Añadimos el string modificado al buffer despu&eacute;s de haber roto
				 * el bucle interno donde hubo un match.
				 */
				matcher.appendTail(result);
				sout[i].append(result.toString());

			} catch (Exception e) {
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			}		
		}
		/*
		 * Guardar los documentos que se encuentra en la variable local sout en
		 * la variable de instancia documentos
		 */
		documentos = new String[sout.length];
		for (int i = 0; i < sout.length; i++) {
			documentos[i] = sout[i].toString();
		}
	}	
}