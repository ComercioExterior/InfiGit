package com.bdv.infi_services.utilities.interfacesAEA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.megasoft.soa.webservices.commom.WSProperties;

/**
 * <p>
 * Permite ejecutar un shell script, que a su vez sirve para ejecutar
 * transacciones AEA (Aplicacion Extendida).
 * <p>
 * Es necesario, porque resulta que el banco tiene una maquina (Host) donde
 * estan todos sus datos en linea. Para poder interactuar con el Host se deben
 * utilizar transacciones de CICS. Estas transacciones tienen un mecanismo en el
 * cual se mandan los parametros de consulta como un solo string y se reciben
 * los datos tambien como un unico string. La forma de hacer esto desde Java es
 * utilizando Aplicacion Extendida (AEA); pero aplicacion extendida solo se
 * puede llamar desde un programa en lenguaje C.
 * <p>
 * Entonces, se hizo un programa en lenguaje C que es capaz de llamar a
 * cualquier transaccion de AEA y este programa es llamado con un Shell Script
 * que le pasa todos los parametros necesarios. Esta clase construye la llamada
 * al Shell Script y es capaz tambien de realizar la llamada y retornar los
 * datos de la transaccion.
 * <p>
 * Para la comunicacion con AEA se utilizan unos parametros que son "fijos",
 * porque son los mismos para todas las transacciones. Ademas existe un
 * encabezado y un cuerpo, que son variables dependiendo de cada transaccion.
 * <p>
 * La respuesta que retorna RunShell es el mismo string que retorna la
 * transaccion de AEA, sin ninguna modificacion ni tratamiento.
 * <h2>Parametros fijos desde la BD</h2>
 * Los parametros fijos pueden configurarse automaticamente si estan guardados
 * en una base de datos. Se debe indicar el DataSource de la BD al momento de
 * crear el RunShell. Los unicos parametros que no se configuran automaticamente
 * son el cuerpo y el encabezado.
 * <p>
 * La tabla de la BD que debe estar configurada, debe tener la siguiente forma:
 * <code>
 * <pre>
 *                                     CREATE TABLE propiedades (
 *                                     nombre VARCHAR(255),
 *                                     valor VARCHAR(255)
 *                                     )
 * </pre>
 * </code>
 * <p>
 * Los nombres de las propiedades en la tabla deben ser, para casa parametro:
 * <li>url del Shell Script - AEA_SHELLSCRIPT
 * <li>protocolo - AEA_PROTOCOLO
 * <li>version - AEA_VERSION
 * <li>entidad - AEA_ENTIDAD
 * <li>servicio - AEA_SERVICIO
 * <li>aplicacion - AEA_APLICACION
 * <li>proceso - AEA_PROCESO
 * <h2>Forma de uso</h2>
 * Si se tiene un DataSource con la tabla de propiedades correctamente llena:
 * <code>
 * <pre>
 * SunShell rs = new RunShell(datasource);
 * runshell.agregarEncabezado(&quot;...&quot;);
 * runshell.agregarCuerpo(&quot;...&quot;);
 * String respuesta = runshell.exec();
 * </pre>
 * </code>
 * <p>
 * Si no se tiene el DataSource ni la tabla, deben indicarse manualmente los
 * parametros fijos, con llamadas a los metodos setXXXX() <code>
 * <pre>
 *                                     SunShell rs = new RunShell();
 *                                     rs.setXXXX(...) //para cada parametro
 *                                     runshell.agregarEncabezado(&quot;...&quot;);
 *                                     runshell.agregarCuerpo(&quot;...&quot;);
 *                                     String respuesta = runshell.exec();
 * </pre>
 * </code>
 * <p>
 * Tambien tiene una clase Factory para optimizar la creacion de RunShell sin
 * tener que ir tantas veces a BD (una por cada RunShell creado). Utilice este
 * Factory si lo considera necesario.s
 * 
 * @author Camilo Torres
 * 
 */

public class ConectorAEA {
	public long id;

	public static Log log = LogFactory.getLog(ConectorAEA.class);

	/**
	 * Indica el url o path donde esta el shell script que ejecuta las
	 * transacciones AEA. Es del tipo path de unix: /usr/bin/shell_aea.sh
	 */
	protected String urlDelShellScript;

	/**
	 * Parametro Protolo de AEA.
	 */
	protected String protocolo;

	/**
	 * Parametro Version de AEA.
	 */
	protected String version;

	/**
	 * Parametro Entidad de AEA.
	 */
	protected String entidad;

	/**
	 * Parametro Servicio de AEA.
	 */
	protected String servicio;

	/**
	 * Parametro Aplicacion de AEA.
	 */
	protected String aplicacion;

	/**
	 * Parametro Proceso de AEA.
	 */
	protected String proceso;

	/**
	 * Encabezado del mensaje AEA. Se puede colocar completamente con
	 * setEncabezado() si tienes listo todo el string de encabezado. Si no
	 * tienes todo el string de encabezado, puedes ir pasando las partes del
	 * encabezado en secuencia con el medoto agregarEncabezado(); al momento de
	 * enviar el mensaje se armara automaticamente el encabezado en la misma
	 * secuencia de las llamadas.
	 */
	protected StringBuffer encabezado;

	/**
	 * Cuerpo del mensaje AEA. Se puede colocar completamente con setCuerpo() si
	 * tienes listo todo el string del cuerpo. Si no tienes todo el string del
	 * cuerpo, que ademas puede ser larguisimo, puedes ir pasando las partes del
	 * curpo en secuencia con el medoto agregarCuerpo(); al momento de enviar el
	 * mensaje se armara automaticamente el cuerpo en la misma secuencia de las
	 * llamadas.
	 */
	protected StringBuffer cuerpo;

	/**
	 * Guarda la transaccion con la que se esta trabajando, es decir, con la que
	 * se hara la consulta hacia aea. Se utiliza para construir el encabezado y
	 * parametro "proceso"
	 */
	private String transaccion;

	/**
	 * guarda la longitud de la transaccion que se va a ejecutar. se utiliza
	 * para construir el encabezado
	 */
	private String longitud;

	/**
	 * Campos con los valores de cuerpo de la transaccion que se va a enviar. Se
	 * utiliza para construir el cuerpo, pasando cada valor de cada campo por
	 * separado
	 */
	private HashMap<String, String> campos = new HashMap<String, String>();

	/**
	 * Se utiliza para clonarlo cada vez que se quiera un runshell en la
	 * aplicacion. esta aqui porque el runshell debe configurarse leyendo un
	 * archivo de texto de propiedades y para no estar leyendo el archivo cada
	 * vez que se cree un runshell, se guarda esta referencia estatica y se
	 * clona en cada peticion de un nuevo runshell (es una optimizacion muy
	 * importante)
	 */
	protected static ConectorAEA rsh = null;

	public String respuestaAea = null;

	/**
	 * Para simplificar el uso del runshell y la construccion del cuerpo del
	 * mensaje de la transaccion aea que se va a enviar, este mapa contiene el
	 * archivo con todos los formatos de las transacciones de entrada, es decir,
	 * los formatos de los campos del cuerpo de las transacciones que se envia
	 * hacia aea.
	 * 
	 * contiene una estrucutra que es, el mapa externo es: clave => nombre de la
	 * transaccion, valor => mapa con los campos. por ejemplo
	 * mapaDeEntrada.get("B401") retorna un mapa con los campos de la
	 * transaccion
	 * 
	 * el segundo mapa, el de los campos, contiene: clave => numero del campo,
	 * valor => arreglo con los datos de configuracion del campo. ejemplp:
	 * mapaDeEntrada.get("B401").get("01") retorna un arreglo de string con el
	 * formato del campo 01 (el primero)
	 * 
	 * por ultimo, se obtienen los datos de los campos, que son, por indice:
	 * <li>0 nombre del campo - un texto corto
	 * <li>1 descripcion del campo - un texto menos corto
	 * <li>2 posicion del campo en el string de entrada (para saber donde
	 * ponerlo en el mensaje)
	 * <li>3 longitud del campo, para saber si hay que rellenar con espacios o
	 * ceros
	 * <li>4 tipo del campo, A alfanumerico, N numerico, M numerico
	 * <li>5 campo (R)equerido y (O)pcional Ejemplo:
	 * mapaDeEntrada.get("B401").get("01")[4] indica si el campo es requerido u
	 * opcional (R u O).
	 */
	protected static HashMap<String, HashMap<String, String[]>> mapaDeEntrada = new HashMap<String, HashMap<String, String[]>>();

	/**
	 * Para simplificar el uso del AEAFormateador, la interpretacion del cuerpo
	 * del mensaje de la transaccion aea que se va a enviar, y la obtencion de
	 * los campos de esa transaccion, este mapa contiene el archivo con todos
	 * los formatos de las transacciones de salida, es decir, los formatos de
	 * los campos del cuerpo de las transacciones que se reciben desde aea.
	 * 
	 * contiene una estrucutra que es, el mapa externo es: clave => nombre de la
	 * transaccion, valor => mapa con los campos. por ejemplo
	 * mapaDeSalida.get("BGMCOM1") retorna un mapa con los campos de la
	 * transaccion
	 * 
	 * el segundo mapa, el de los campos, contiene: clave => numero del campo,
	 * valor => arreglo con dos datos {posicion, longitud}. ejemplp:
	 * mapaDeEntrada.get("BGMCOM1").get("01") retorna un arreglo de string con
	 * el formato del campo 01 (el primero)
	 * 
	 * por ultimo, se obtienen los datos de los campos, que son, por indice:
	 * <li>0 posicion de inicio del campo en el cuerpo de la transaccion
	 * <li>1 longitud del campo en el cuerpo de la transaccion
	 * 
	 * Ejemplo: mapaDeEntrada.get("BGMCOM1").get("01")[0] da la posicion del
	 * campo.
	 */
	protected static HashMap<String, HashMap<String, int[]>> mapaDeSalida = null;

	/**
	 * Es un mapa que contiene, para cada transaccion, su longitud en el mensaje
	 * de entrada. se usa para construir los encabezados.
	 * 
	 * contiene: clave => nombre de transaccion, valor => longitud de entrada
	 */
	protected static HashMap<String, String> mapaLongitudesDeEntrada = new HashMap<String, String>();

	/**
	 * Mapa con todos los mensajes de respueta para esta transaccion
	 */
	public HashMap<String, ArrayList<MensajeAEA>> mapaDeMensajesDeRespuesta = new HashMap<String, ArrayList<MensajeAEA>>();

	public static void main(String args[]) throws Exception {
		ConectorAEA c = ConectorAEA.getConector();
		c.setTransaccion("B401");
		c.setCampo("01", "01020310470003418411");
		c.setCampo("02", "VEF");
		c.setCampo("03", "14-01-2008");
		c.setCampo("04", "14-02-2008");
		c.setCampo("05", "000000000");
		c.setCampo("06", "999999999");
		c.setCampo("07", "D");
		c.setCampo("08", "14/02/2008 11:26:32 a.m.  ");
		c.setCampo("09", "50");
		c.setCampo("10", "N");

		c.traerDatosDesdeAEA();
		MensajeAEA m = c.getMensaje("BGM4013");
		ArrayList<MensajeAEA> listaDeMensajes = c.getListaMensajes("BGM4011");

		log.debug("Un solo mensaje:" + m.obtenerCampo("01"));
		log.debug(":" + m.obtenerCampo("02"));

		Iterator<MensajeAEA> iter = listaDeMensajes.iterator();

		while (iter.hasNext()) {
			m = iter.next();
			log.debug(m.obtenerCampo("01"));
			log.debug(" : " + m.obtenerCampo("02"));
			log.debug(" : " + m.obtenerCampoNumerico("03"));
			log.debug(" : " + m.obtenerCampoNumerico("10"));
		}
	}

	/**
	 * Retorna todos los mensajes de un mismo tipo.
	 * 
	 * Se utiliza para las transacciones qeu traen listas de cosas, como la
	 * lista de movimientos de cuenta, lista de productos, etc.
	 * 
	 * @param string
	 *            con el tipo del mensaje
	 * @return lista de mensajes de ese tipo
	 */
	public ArrayList<MensajeAEA> getListaMensajes(String string) {
		return this.mapaDeMensajesDeRespuesta.get(string);
	}

	/**
	 * Obtiene un mensaje para el tipo de formato de respuesta indicado. Retorna
	 * el primer mensaje que consiga con este tipo.
	 * 
	 * Se utiliza para cuando la transaccion retorna solo un mensaje de ese
	 * tipo.
	 * 
	 * @param string
	 *            con el tipo del mensaje
	 * @return un mensaje con ese tipo
	 */
	public MensajeAEA getMensaje(String string) {
		return this.mapaDeMensajesDeRespuesta.get(string).get(0);
	}

	protected ConectorAEA() {
	}

	/**
	 * <p>
	 * Crea un RunShell con los parametros fijos de la llamada AEA ya
	 * configurados a partir del archivo de propiedades.
	 * <p>
	 * Los parametros o valores que se trae listos son:
	 * <li>url del Shell Script
	 * <li>protocolo
	 * <li>version
	 * <li>entidad
	 * <li>servicio
	 * <li>aplicacion
	 * <li>proceso
	 * <p>
	 * El archivo propiedades debe contener, a su vez, datos con las propiedades
	 * adecuadas para poder tomar los valores correctos y rellenar los
	 * parametros fijos de AEA. Los nombres de las propiedades deben ser, para
	 * cada parametro:
	 * <li>url del Shell Script - AEA_SHELLSCRIPT
	 * <li>protocolo - AEA_PROTOCOLO
	 * <li>version - AEA_VERSION
	 * <li>entidad - AEA_ENTIDAD
	 * <li>servicio - AEA_SERVICIO
	 * <li>aplicacion - AEA_APLICACION
	 * <li>proceso - AEA_PROCESO
	 * 
	 * @throws Exception
	 */
	public static ConectorAEA getConector() throws Exception {
		synchronized ("") {
			if (rsh == null) {
				rsh = new ConectorAEA();
				// traer el valor, invocacion Shell
				rsh.urlDelShellScript = WSProperties
						.getProperty("aea_shellscript");
				// traer el protocolo
				rsh.protocolo = WSProperties.getProperty("aea_protocolo");
				// traer la version
				rsh.version = WSProperties.getProperty("aea_version");
				// traer la entidad
				rsh.entidad = WSProperties.getProperty("aea_entidad");
				// traer el servicio
				rsh.servicio = WSProperties.getProperty("aea_servicio");
				// traer la aplicacion
				rsh.aplicacion = WSProperties.getProperty("aea_aplicacion");
				// traer el proceso
				rsh.proceso = WSProperties.getProperty("aea_proceso");

				/*
				 * Carga el mapa de transacciones desde el archivo
				 * WEB-INF/fdfe.txt una sola vez, la primera vez que se llama un
				 * runshell
				 */
				getMapaDeEntrada();
				getMapaDeSalida();
			}
		}
		return (ConectorAEA) rsh.clone();
	}

	/**
	 * Carga el archivo de transacciones
	 * 
	 */
	synchronized protected static void getMapaDeEntrada() {
		/*
		 * El archivo esta en WEB-INF/fdfe.txt
		 */
		InputStream is = ConectorAEA.class.getResourceAsStream("fdfe.txt");

		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String linea = null;
		/*
		 * hay que saber cuando se cambia de transaccion, ya que todas las
		 * transacciones vienen pegadas o juntas en el mismo archivo
		 */
		String transaccionActual = null, transaccionAnterior = null;
		String longitudActual = null, longitudAnterior = null;
		String numeroDelCampo = null;
		/*
		 * en estas estructuras se guarda el contenido del archivo. se explican
		 * en los atributos de la clase
		 */
		HashMap<String, HashMap<String, String[]>> mapa = new HashMap<String, HashMap<String, String[]>>();
		HashMap<String, String[]> listaDeCampos = null;
		String[] campos = new String[6];
		HashMap<String, String> mapaLongitudesTransaccion = new HashMap<String, String>();

		try {
			while ((linea = br.readLine()) != null) {
				/*
				 * lee todo el archivo, pero ignora las lineas que comeinzan con --
				 * y con TRN... porque esas son lineas de encabezado (y da la
				 * opcion de incluir comentarios)
				 */
				if (!linea.startsWith("--") && !linea.startsWith("TRN # CAMPO")) {
					// el codigo de la transaccion
					transaccionActual = linea.substring(0, 4);
					// la longitud logina de la transaccion
					longitudActual = linea.substring(42, 46);
					if (transaccionAnterior == null) {
						/*
						 * llega la primera transaccion si es null, hay que
						 * inicializar los valores que cambian
						 */
						transaccionAnterior = transaccionActual;
						longitudAnterior = longitudActual;
						listaDeCampos = new HashMap<String, String[]>();
					} else if (!transaccionActual.equals(transaccionAnterior)) {
						// significa que hubo cambio de transaccion
						// guardar la transaccion anterior
						mapa.put(transaccionAnterior, listaDeCampos);
						mapaLongitudesTransaccion.put(transaccionAnterior,
								longitudAnterior);
						// cambiar de transaccion
						transaccionAnterior = transaccionActual;
						longitudAnterior = longitudActual;
						listaDeCampos = new HashMap<String, String[]>();
					}
					/*
					 * parsea la linea para obtener los campos que considera
					 * necesarios.
					 */
					// numero del campo para la transaccion
					numeroDelCampo = linea.substring(4, 6);

					campos = new String[6];
					// nombre del campo
					campos[0] = linea.substring(6, 13);
					// descripcion del campo
					campos[1] = linea.substring(13, 33);
					// posicion del campo en el string de entrada
					campos[2] = linea.substring(33, 36);
					// longitud del campo
					campos[3] = linea.substring(36, 39);
					// tipo del campo
					campos[4] = linea.substring(39, 40);
					// campo (R)equerido y (O)pcional
					campos[5] = linea.substring(40, 41);

					listaDeCampos.put(numeroDelCampo, campos);
				}
			}
			// ultima transaccion del archivo
			// es un caso borde, la ultima transaccion no se guardo en el while
			mapa.put(transaccionActual, listaDeCampos);
			mapaLongitudesTransaccion.put(transaccionActual, longitudActual);
		} catch (IOException e3) {
			e3.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			try {
				isr.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/*
		 * guarda los mapas en las estructuras fijas
		 */
		mapaDeEntrada = mapa;
		mapaLongitudesDeEntrada = mapaLongitudesTransaccion;
	}

	/**
	 * Retorna un mapa con el contenido del archivo de configuracion de las
	 * transacciones de salida de aea.
	 * 
	 * la primera vez que se llama, como esta vacio, crea el mapa a partir del
	 * archivo ubicado en WEB-INF/fdfs.txt
	 * 
	 * @return el mapa.
	 */
	synchronized protected static HashMap<String, HashMap<String, int[]>> getMapaDeSalida() {
		if (mapaDeSalida == null) {
			String transaccionActual = null, transaccionAnterior = null;
			String numeroDelCampo = null;
			mapaDeSalida = new HashMap<String, HashMap<String, int[]>>();
			BufferedReader br = null;
			HashMap<String, int[]> camposDeUnaTransaccion = new HashMap<String, int[]>();
			int[] campo = new int[2];
			// System.err.println("archivo de salida:");
			// System.err.println(ConectorAEA.class.getResource("fdfs.txt")
			// .toExternalForm());
			br = new BufferedReader(new InputStreamReader(ConectorAEA.class
					.getResourceAsStream("fdfs.txt")));
			// System.err.println("archivo leido");

			try {
				String linea;
				while ((linea = br.readLine()) != null) {
					// System.err.println("linea leida:");
					// System.err.println(linea);
					if (!linea.startsWith("--")
							&& !linea.startsWith("FORMATO #")) {
						/*
						 * es el codigo de la transaccion
						 */
						transaccionActual = linea.substring(0, 7).trim();
						if (transaccionAnterior == null) {
							/*
							 * es la priemra iteracion, por lo tanto hay que
							 * colocar la transaccion anterior igual a la
							 * primera
							 */
							transaccionAnterior = transaccionActual;
						} else if (!transaccionActual
								.equals(transaccionAnterior)) {
							/*
							 * hubo un cambio de transaccion, hay que meter la
							 * lista de campos de la transaccion anterio en el
							 * mapa
							 */
							mapaDeSalida.put(transaccionAnterior,
									camposDeUnaTransaccion);
							/*
							 * inicializar de nuevo las variables para la nueva
							 * transaccion
							 */
							transaccionAnterior = transaccionActual;
							camposDeUnaTransaccion = new HashMap<String, int[]>();
						}
						/*
						 * es el numero del campo "01", "12", etc.
						 */
						numeroDelCampo = linea.substring(8, 10);
						/*
						 * La posicion de inicio va en el indice 0.
						 * 
						 * la longitud va en el indice 1
						 */
						campo = new int[2];
						campo[0] = Integer.parseInt(linea.substring(37, 41)) - 1;
						campo[1] = Integer.parseInt(linea.substring(41, 44));

						// System.err.println("Campos de entrada:");
						// System.err.println(campo[0] + ":" + campo[1]);

						/*
						 * Meto el campo en la lista actual
						 */
						camposDeUnaTransaccion.put(numeroDelCampo, campo);
					}
				}
				/*
				 * la ultima transaccion no se mete, coloca, adhiere en el
				 * iterador, por lo tanto hay que meterla aqui. es un caso borde
				 */
				mapaDeSalida.put(transaccionActual, camposDeUnaTransaccion);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return mapaDeSalida;
	}

	/**
	 * Indica la transaccion con la que se esta trabajando. Es necesario
	 * indicarla para poder construir el "proceso", el encabezado y el cuerpo
	 * 
	 * @param tr
	 *            Codigo de la transaccion
	 * @return el runshell
	 */
	public ConectorAEA setTransaccion(String tr) {
		this.transaccion = tr;
		this.longitud = mapaLongitudesDeEntrada.get(this.transaccion);
		this.setProceso(this.transaccion);
		this.encabezado = new StringBuffer("            ").append(
				this.transaccion).append(this.longitud).append("0     1O00N0");

		return this;
	}

	/**
	 * Indica un campo para agregar al cuerpo. de indicar el numero del campo
	 * (ej: "01") y el valor ya debe venir formateado en string, aunque ni no
	 * cumple con la longitud, se rellena con espacio si es alfanumerico, o con
	 * ceros si es numerico
	 * 
	 * @param numeroDeCampo
	 *            "01", "02", "10", etc. es un string
	 * @param valor
	 *            valor a meter en ese campo
	 * @return el runshell
	 */
	public ConectorAEA setCampo(String numeroDeCampo, String valor) {

		this.campos.put(numeroDeCampo, valor);

		return this;
	}

	/**
	 * Indica un campo para agregar al cuerpo. de indicar el numero del campo
	 * (ej: "01") y el valor ya debe venir formateado en string, aunque ni no
	 * cumple con la longitud, se rellena con espacio si es alfanumerico, o con
	 * ceros si es numerico
	 * 
	 * @param numeroDeCampo
	 *            "01", "02", "10", etc. es un string
	 * @param valor
	 *            valor a meter en ese campo
	 * @return el runshell
	 */
	public ConectorAEA setCampo(String numeroDeCampo, String valor,
			String caracterDeRelleno) {

		String configuracionDeCampo[] = ConectorAEA.mapaDeEntrada.get(
				this.transaccion).get(numeroDeCampo);
		StringBuffer campoRelleno = new StringBuffer();
		int longi = Integer.parseInt(configuracionDeCampo[3]);
		for (int i = 0; i < (longi - valor.length()); i++) {
			campoRelleno.append(caracterDeRelleno);
		}
		campoRelleno.append(valor);

		this.campos.put(numeroDeCampo, campoRelleno.toString());
		return this;
	}

	/**
	 * Indica un campo para agregar al cuerpo. de indicar el numero del campo
	 * (ej: "01") y el valor ya debe venir formateado en string, aunque ni no
	 * cumple con la longitud, se rellena con espacio si es alfanumerico, o con
	 * ceros si es numerico
	 * 
	 * @param numeroDeCampo
	 *            "01", "02", "10", etc. es un string
	 * @param valor
	 *            valor a meter en ese campo
	 * @return el runshell
	 */
	public ConectorAEA setCampoRellenoDerecha(String numeroDeCampo,
			String valor, String caracterDeRelleno) {
		String configuracionDeCampo[] = ConectorAEA.mapaDeEntrada.get(
				this.transaccion).get(numeroDeCampo);
		for (String s : configuracionDeCampo) {
			log.debug(s);
		}
		StringBuffer campoRelleno = new StringBuffer();
		int longi = Integer.parseInt(configuracionDeCampo[3]);
		if (valor==null)
			valor="";
		campoRelleno.append(valor);
		for (int i = 0; i < (longi - valor.length()); i++) {
			campoRelleno.append(caracterDeRelleno);
		}

		this.campos.put(numeroDeCampo, campoRelleno.toString());

		return this;
	}

	/**
	 * Coloca un campo vacio, es decir, relleno con espacios si es alfanumerico
	 * o con ceros si es numerico
	 * 
	 * @param numeroDeCampo
	 *            "01", "02", "10", etc. es un string
	 * @return el runshell
	 */
	public ConectorAEA setCampoVacio(String numeroDeCampo) {
		String configuracionDeCampo[] = ConectorAEA.mapaDeEntrada.get(
				this.transaccion).get(numeroDeCampo);

		int longi = Integer.parseInt(configuracionDeCampo[3]);
		String caracterDeRelleno = " ";
		if (configuracionDeCampo[4].equalsIgnoreCase("N")
				|| configuracionDeCampo[4].equalsIgnoreCase("N")) {
			caracterDeRelleno = "0";
		}
		StringBuffer valor = new StringBuffer("");
		while (valor.length() < longi) {
			valor.append(caracterDeRelleno);
		}

		this.campos.put(numeroDeCampo, valor.toString());
		return this;
	}

	/**
	 * Elimina los valores de en encabezado y cuerpo, se debe ejecutar si se
	 * posee una sola instancia y se van a enviar varias transacciones con la
	 * misma instancia
	 * 
	 */
	public void clearValues() {
		this.encabezado = null;
		this.cuerpo = null;
	}

	/**
	 * <p>
	 * Agrega datos al encabezado del mensaje AEA.
	 * <p>
	 * Se utiliza para construir el encabezado del mensaje. Siempre se debe
	 * colocar al menos un encabezado. Va como un metodo separado con la
	 * esperanza de que el codigo que coloca los encabezados quede un poco mas
	 * legible.
	 * <p>
	 * Si se colocan varios encabezados, con llamadas sucesivas a este metodo,
	 * lo que se hace es que se concatenan todos los datos y se pasan como un
	 * solo encabezado al momento de la llamada.
	 * 
	 * @param dato
	 *            de encabezado o parte del encabezado
	 * @return El mismo RunShell
	 */
	public ConectorAEA agregarEncabezado(String dato) {
		if (this.encabezado == null) {
			this.encabezado = new StringBuffer();
		}
		this.encabezado.append(dato);
		return this;
	}

	/**
	 * <p>
	 * Agrega datos al cuerpo del mensaje AEA.
	 * <p>
	 * Se utiliza para construir el cuerpo del mensaje. Siempre se debe colocar
	 * al menos un cuerpo. Va como un metodo separado con la esperanza de que el
	 * codigo que coloca el cuerpo del mensaje quede un poco mas legible, debido
	 * a que la mayoria de los mensajes llevan varios datos que se deben
	 * formatear para coincidir con el formato exacto de mensaje AEA.
	 * <p>
	 * Si se colocan varios cuerpo, con llamadas sucesivas a este metodo, lo que
	 * se hace es que se concatenan todos los datos y se pasan como un solo
	 * cuerpo al momento de la llamada.
	 * 
	 * @param dato
	 *            cuerpo o parte del cuerpo
	 * @return El mismo RunShell
	 */
	public ConectorAEA agregarCuerpo(String dato) {
		this.cuerpo.append(dato);
		return this;
	}

	public static ArrayList<String[]> tiemposCorridasAea = new ArrayList<String[]>(
			25);

	public static int maximoBufferCorridasAea = 20;

	public static boolean habilitarCorridasAea = false;

	/**
	 * Ejecuta el comando hacia AEA y retorna la respuesta de AEA como un
	 * string. La respuesta debe ser procesada por Usted, ya que este RunShell
	 * la retorna completamente <em>en bruto</em>.
	 * 
	 * @return La respuesta de AEA tal cual.
	 * @throws Exception
	 *             Si falla la llamada a AEA
	 */
	public String traerDatosDesdeAEA() throws Exception {
		long msecInicio = System.currentTimeMillis();

		this.respuestaAea = exec(this.construirComando());

		long msecFin = System.currentTimeMillis();
		String[] corridaAea = { this.transaccion, Long.toString(msecInicio),
				Long.toString(msecFin - msecInicio) };
		if (habilitarCorridasAea) {
			synchronized (tiemposCorridasAea) {
				tiemposCorridasAea.add(0, corridaAea);
				if (tiemposCorridasAea.size() > maximoBufferCorridasAea) {
					tiemposCorridasAea.remove(tiemposCorridasAea.size()-1);
				}
			}
		}
		// System.err.println("AEA " + id + "----" + this.transaccion + ": "
		// + (msecFin - msecInicio));

		/*
		 * Ahora parsea la respuesta
		 */
		this.interpretarRespuesta();

		return this.respuestaAea;
	}

	/**
	 * Construye una linea de comandos que se va a ejecutar, a partir de los
	 * datos con que fue configurado este RunShell. Existe porque el shell
	 * script a ejecutar utiliza como parametros los valores de este RunShell en
	 * un orden especifico
	 * 
	 * @return linea de comando para el shell script de AEA
	 */
	protected String[] construirComando() {
		if (this.cuerpo == null) {
			this.cuerpo = new StringBuffer();
			Iterator<String> it = new TreeSet<String>(this.campos.keySet())
					.iterator();
			while (it.hasNext()) {
				this.cuerpo.append(this.campos.get(it.next()));
			}
		}

		log.debug(":::::::::::::: cuerpo a enviar");
		log.debug(this.proceso);
		log.debug(this.encabezado);
		log.debug(this.cuerpo);

		String comando[] = { this.urlDelShellScript, this.protocolo,
				this.version, this.entidad, this.servicio, this.aplicacion,
				this.proceso, this.encabezado.toString(),
				this.cuerpo.toString() };

		return comando;
	}

	/**
	 * Realiza una llamada al comando indicado. Se espera que el comando sea un
	 * shell script que ejecuta una transaccion hacia AEA. Retorna el resultado
	 * de esa transaccion, tal cual, sin modificaciones.
	 * 
	 * @param command
	 *            Comando o shell a ejecutar
	 * @return Resultado de la transaccion
	 * @throws Exception
	 *             Si falla la llamada hacia AEA
	 */
	protected String exec(String command[]) throws Exception {
		StringBuffer response = new StringBuffer(10000);
		// long msecInicio = System.currentTimeMillis();
		Runtime rt;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		for (String s : command) {
//			log.debug(s);
			// System.err.println(s);
		}
		rt = Runtime.getRuntime();
		Process proc = rt.exec(command[0]);
		OutputStream os=proc.getOutputStream();
		PrintWriter pw = new PrintWriter(os);

		try {
			
			for(int i=1; i<command.length;i++){
				pw.println(command[i]);
				System.err.println();
				System.err.print("."+command[i]+".");
//				log.info("."+command[i]+".");
			}
			for(int i=1; i<8;i++){
				pw.println("fin");
//				log.info(".fin.");
//				System.err.println(".fin.");
			}
			try{
			pw.close();
			os.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			is = proc.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String linea;
			while ((linea = br.readLine()) != null) {
				if (!linea.matches(".*Sun Microsystems.*")) {
					response.append(linea);
					
				}
			}
			proc.destroy();
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			try {
				br.close();
			} catch (RuntimeException e) {
				// solo para cerrar
				e.printStackTrace();
			}
			try {
				isr.close();
			} catch (RuntimeException e) {
				// solo para cerrar
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (RuntimeException e) {
				// solo para cerrar
				e.printStackTrace();
			}
		}

		// long msecFin = System.currentTimeMillis();
		// System.err.println("Milisegundos ejecucion Ejecutar Shell:
		// "+command[6]+": " + (msecFin - msecInicio));
//		log.debug(response);
//		log.info("respuesta:\n" + response);
		System.err.println("respuesta:\n" + response);
		return response.toString();
	}

	/**
	 * Parametro Proceso de AEA.
	 */
	public void setProceso(String proceso) {
		if (proceso.length() == 4) {
			this.proceso = "ADS" + proceso + "S";
		} else {
			this.proceso = proceso;
		}
	}

	/**
	 * Encabezado del mensaje AEA. Se puede colocar completamente con
	 * setEncabezado() si tienes listo todo el string de encabezado. Si no
	 * tienes todo el string de encabezado, puedes ir pasando las partes del
	 * encabezado en secuencia con el medoto agregarEncabezado(); al momento de
	 * enviar el mensaje se armara automaticamente el encabezado en la misma
	 * secuencia de las llamadas.
	 */
	public void setEncabezado(StringBuffer encabezado) {
		this.encabezado = encabezado;
	}

	/**
	 * Cuerpo del mensaje AEA. Se puede colocar completamente con setCuerpo() si
	 * tienes listo todo el string del cuerpo. Si no tienes todo el string del
	 * cuerpo, que ademas puede ser larguisimo, puedes ir pasando las partes del
	 * curpo en secuencia con el medoto agregarCuerpo(); al momento de enviar el
	 * mensaje se armara automaticamente el cuerpo en la misma secuencia de las
	 * llamadas.
	 */
	public void setCuerpo(StringBuffer cuerpo) {
		this.cuerpo = cuerpo;
	}

	/**
	 * se usa para clonar el objeto que se configuro la primera vez desde el
	 * archivo de parametros, y asi no tener que leer el archivo nuevamente
	 */
	public Object clone() {
		ConectorAEA r = new ConectorAEA();

		r.urlDelShellScript = this.urlDelShellScript;

		r.protocolo = this.protocolo;
		r.version = this.version;
		r.entidad = this.entidad;
		r.servicio = this.servicio;
		r.aplicacion = this.aplicacion;
		r.proceso = this.proceso;

		r.cuerpo = null;
		r.encabezado = null;

		return r;
	}

	/**
	 * TODO abregar documentacion
	 * 
	 * @return
	 * @throws Exception
	 */
	protected Map<String, ArrayList<MensajeAEA>> interpretarRespuesta()
			throws Exception {
		log.debug("Respuesta original de AEA:" + this.respuestaAea);

		/*
		 * separa la respuesta por @, ya que este es el caracter que separa cada
		 * formato (cada mensaje)
		 */
		String[] arregloDeFormatosSeparados = this.respuestaAea
				.split("@DC|@AV|@ER");

		/*
		 * Lo primero es ver si viene un error. Si es asi, se debe lanzar una
		 * excepcion
		 */
		if (!this.respuestaAea.startsWith("@1")) {
			if (arregloDeFormatosSeparados.length > 1) {
				throw new Exception(arregloDeFormatosSeparados[1]);
			}
			throw new Exception(this.respuestaAea);
		}

		if (arregloDeFormatosSeparados.length > 0) {
			for (String formato : arregloDeFormatosSeparados) {
				try {
					MensajeAEA mensaje = new MensajeAEA(formato, mapaDeSalida);
					ArrayList<MensajeAEA> mensajesAgrupadosPorTipo = this.mapaDeMensajesDeRespuesta
							.get(mensaje.formato);
					if (mensajesAgrupadosPorTipo == null) {
						mensajesAgrupadosPorTipo = new ArrayList<MensajeAEA>();
						this.mapaDeMensajesDeRespuesta.put(mensaje.formato,
								mensajesAgrupadosPorTipo);
					}
					mensajesAgrupadosPorTipo.add(mensaje);
				} catch (StringIndexOutOfBoundsException sioobe) {
					/*
					 * las lineas que no tengan el formato, en especial la
					 * primera linea se saltan para no procesarse
					 */
					log
							.debug(
									"Se salta este formato porque ocurrion una excepcion",
									sioobe);
				}
			}
		}
		return this.mapaDeMensajesDeRespuesta;
	}
	public String encrypt( String data ,  String key ) throws Exception
	{

		byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");

        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal( data.getBytes() );
		
		BASE64Encoder encoder = new BASE64Encoder();
		
        return encoder.encode(encrypted);
	}
	public String decrypt( String data ,  String key ) throws Exception
	{
	
		BASE64Decoder b64 = new BASE64Decoder();
		byte[] decoded = b64.decodeBuffer( data );

		byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");

        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal( decoded );
		
		return new String( encrypted );
		
	}

}