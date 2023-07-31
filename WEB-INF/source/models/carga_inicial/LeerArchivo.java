package models.carga_inicial;

/**
 * @author Erika Valerio
 */

import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;
import models.msc_utilitys.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.lang.Math;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.util.Utilitario;
   
public class LeerArchivo extends MSCModelExtend
{
	private Vector vec_sql = new Vector();
	private String estadoProceso = "1";
	private String num_proceso = "00"; 
	private int num_reg_leidos = 0; 
	private int num_reg_malos = 0; 
	private int num_reg_buenos = 0; 
	private double porc_reg_malos = 0; 
	private double porc_reg_buenos = 0; 
	private String mje_campo_actual ="";
	private int registroOk;
	private String fecha_comienzo = "";
	private String fecha_fin ="";
	int campos_nulos = 0;//n&uacute;mero de campos por cada registro(fila) en el archivo.
	private String tituloACargar = ""; 
	
	private String FORMATO_003B = " N&uacute;mero Decimal";
	private  String FORMATO_003A = "N&uacute;mero Entero";
	private  String FORMATO_002A = "Cadena de Caracteres";
	private  String FORMATO_006A = "C&eacute;dula";
	private  String FORMATO_005A = "Valor en Lista";
	private  String FORMATO_001A = "DD/MM/AAAA";
	private  String FORMATO_001B = "MM/DD/AAAA";
	private  String FORMATO_001C = "AAAA/MM/DD";
	private  String FORMATO_001D = "DD-MMM-AAAA";
	
	private Logger logger = Logger.getLogger(LeerArchivo.class);
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{	
		if(_req.getSession().getAttribute("fecha_inicio")!=null)
			fecha_comienzo = String.valueOf(_req.getSession().getAttribute("fecha_inicio"));
		else
			fecha_comienzo = this.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
	
		
		//Definir n&uacute;mero de proceso
		num_proceso = dbGetSequence(_dso, "INFI_TB_Z11_PROCESOS");
		
		//Colocar en la sesion id del proceso que se esta ejecutando
		_req.getSession().setAttribute("id_proceso_actual", String.valueOf(num_proceso));
			
		//obtener archivo para ser leido (clase FileInputStream)
		FileInputStream archivo = new FileInputStream(_record.getValue("fichero.tempfile"));		
				
		leerArchivo(archivo, _record.getValue("num_archivo"), _record.getValue("cod_mapa"));
		
		//----Limpiar data de tabla temporal INFI_TB_z51_TITULOS (Carga Final paso 4) para el titulo		
		String sqlLimpiar = "DELETE FROM infi_tb_z51_titulos where z51_titulo_id = '"+tituloACargar+"'";
		db.exec(_dso, sqlLimpiar);
		//-------------------------------------------------------------------------------------------------
				
		porc_reg_malos = this.getPorcentaje(num_reg_malos, num_reg_leidos);
		porc_reg_buenos = this.getPorcentaje(num_reg_buenos, num_reg_leidos);
		
		//guardatr datos finales
		_record.setValue("reg_leidos", String.valueOf(num_reg_leidos));
		_record.setValue("reg_buenos", String.valueOf(num_reg_buenos));
		_record.setValue("reg_malos", String.valueOf(num_reg_malos));		
		_record.setValue("porc_malos", String.valueOf(porc_reg_malos));	
		_record.setValue("porc_buenos", String.valueOf(porc_reg_buenos));	
		
		_record.setValue("fecha_fin", fecha_fin);
		_record.setValue("fecha_inicio", fecha_comienzo);
		_record.setValue("tiempo_duracion", this.getDiferenciaHoras(fecha_comienzo, fecha_fin, "dd/MM/yyyy - HH:mm:ss"));
		
		storeDataSet( "record", _record );
		
		archivo.close();	
		
		//Borrar archivo una vez utilizado		
		String ar = _record.getValue("fichero.tempfile");
		ar = Util.replace(ar, "\\", "/");	
				
		Util.fileDelete(ar);
	
		

	}
	
	/**
	 * M&eacute;todo para leer todos los registros de un archivo XLS (Excel) 
	 * @param archivo
	 * @param cod_archivo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public void leerArchivo(FileInputStream archivo, String cod_archivo, String cod_mapa) throws FileNotFoundException, IOException, Exception{
				
		//int num_filas=0;
		int num_campos=0;
		String sql = "";		
		String cod_mapa_ar = "";
		
		HSSFSheet hoja = null;//hoja
		HSSFCell celda = null;//celda
		HSSFRow row = null; //fila

		DataSet _mapa_ar = this.getMapaArchivo(cod_mapa);
		_mapa_ar.next();
		
		//abrir el archivo xls
		HSSFWorkbook libro = new HSSFWorkbook(archivo);
		
		//num_filas = Integer.parseInt(_mapa_ar.getValue("z00_num_filas"));
		num_campos = Integer.parseInt(_mapa_ar.getValue("z00_num_campos"));
		cod_mapa_ar = _mapa_ar.getValue("z00_cod_mapa");

		
		//insertar datos del proceso de lectura y carga
		sql = getResource("insert_proceso.sql");		
		sql = Util.replace(sql, "@z11_cod_proceso@", num_proceso);
		sql = Util.replace(sql, "@z10_co_codigo_archivo@", cod_archivo);
		sql = Util.replace(sql, "@z11_nu_numero_proceso@", "'"+num_proceso+"-"+_record.getValue("fichero.filename")+"'");
		sql = Util.replace(sql, "@z11_fe_fecha_inicio@", "'"+fecha_comienzo+"'");
		sql = Util.replace(sql, "@z11_fe_fecha_final@", "''");
		sql = Util.replace(sql, "@z11_no_nombre_usuario@", _record.getValue("userid"));
		sql = Util.replace(sql, "@z11_nu_numero_campos@", String.valueOf(num_campos));
		sql = Util.replace(sql, "@z11_de_descripcion_estado@", estadoProceso);
		vec_sql.add(sql);			
		
		//leyendo por cada hoja del Libro 
		//for (int i = 0; i < libro.getNumberOfSheets(); i++) {
		for (int i = 0; i < 1; i++) {//leyendo primera hoja 
			
			//Obtener la hoja i del Libro
			hoja = libro.getSheet(libro.getSheetName(i));//objeto hoja
			
			///recorrer filas (registros)
			int j;//comenzar ciclo desde 1 para saltar el rengl&oacute;n de t&iacute;tulos del archivo excel (se asume que el primer renglon es el t&iacute;tulo)
			for (j = 1; j < hoja.getPhysicalNumberOfRows(); j++) {
				
				try{				
					row = hoja.getRow(j);//Obtener la fila j de la hoja		
					
					registroOk = 1;//partiendo de que todos los campos del registro(fila) estan correctos
					
					sql = getResource("insert_registro_proceso.sql");
					sql = Util.replace(sql, "@z12_co_codigo_registro@", dbGetSequence(_dso, "INFI_TB_Z12_REGISTROS"));
					sql = Util.replace(sql, "@z11_cod_proceso@", num_proceso);
					sql = Util.replace(sql, "@z12_nu_numero_registro@", String.valueOf(j));
	
					//vas leyendo por cada celda (columna) de la fila
					//para cada fila el numero de columnas es fijo (parametro de base de datos asociado al archivo)
					String nombres_campos="";
					String valores_campos="";
					campos_nulos = 0;
					for (int k = 0; k < num_campos; k++) {
						
						nombres_campos += ",z12_campo"+String.valueOf(k+1)+"_valor";
						nombres_campos += ",z12_campo"+String.valueOf(k+1)+"_resultado";
						nombres_campos += ",z12_campo"+String.valueOf(k+1)+"_mensaje";
						
						celda = row.getCell((short) k);
																	
						//validar campo
						String resultado = "1";
						String valorCelda = (String.valueOf(celda)).trim();
						valorCelda = valorCelda.replace("'", " ");//reemplazar posibles comillas
						
						//Titulo a Cargar
						if(k==11 && tituloACargar.equals(""))//si el campo es el del titulo almacenar en variable global el titulo a cargar en la hoja
							tituloACargar = valorCelda;
						
						boolean campo_ok = validarCampo(valorCelda, String.valueOf(k+1), cod_mapa_ar);					
					
						if(!campo_ok){	//si campo con errores		
							
							registroOk = 0;//colocar en 0 indicador de registro con al menos un campo con errores
							
							resultado = "0";
							//mensaje = "Campo con Errores";
							estadoProceso = "0";//Archivo con al menos un error
							
						}
																			
						valores_campos += "," + "'"+valorCelda+"'";
						valores_campos += ", "+resultado;
						valores_campos += ",'" +mje_campo_actual+ "'";
	
					}
				
					//tomar en cuenta los registros que tienen al menos un valor distinto de nulo
					if(campos_nulos < num_campos){
						sql = Util.replace(sql, "@z12_estatus@", String.valueOf(registroOk));
						sql = Util.replace(sql, "@nombres_campos@", nombres_campos);
						sql = Util.replace(sql, "@valores_campos@", valores_campos);
						vec_sql.add(sql);
						
						//num_reg_buenos++;//incrementar el n&uacute;mero de registros leidos correctamente
					}//else
					//	num_reg_malos++;//incrementar n&uacute;mero de registros que no pueden ser leidos correctamente
					
					
					if(registroOk==1){
						num_reg_buenos++; //incrementar numero de registros buenos
					}else{
						num_reg_malos++;//incrementar numero de registros malos
					}
					
				}catch(java.lang.NullPointerException npe){
					try {
						logger.error(npe.getMessage()+ Utilitario.stackTraceException(npe));
					} catch (Exception e1) {
						npe.printStackTrace();
					}
					//en caso de error al leer una celda o fila, remover el query anterior de inserci&oacute;n de registro
					vec_sql.remove(vec_sql.lastElement());
					num_reg_malos++;//incrementar n&uacute;mero de registros que no pueden ser leidos correctamente
				
				}catch(Throwable te){
					try {
						logger.error(te.getMessage()+ Utilitario.stackTraceException(te));
					} catch (Exception e1) {
						te.printStackTrace();
					}
					//en caso de cualquier tipo de error al leer una celda o fila, remover el query anterior de inserci&oacute;n de registro
					vec_sql.remove(vec_sql.lastElement());
					num_reg_malos++;//incrementar n&uacute;mero de registros que no pueden ser leidos correctamente
				}
				
			}
			num_reg_leidos = j - 1; //Guardar el numero de registros leidos
		}
		
		this.updateEstadoProceso();
		this.updateEstadoArchivo(cod_archivo);

		String[] arrSql = new String[vec_sql.size()];
		vec_sql.toArray(arrSql);	
		
		db.execBatch(_dso,arrSql);			
		
	}
	
	/**
	 * M&eacute;todo para actualizar el estado del proceso una vez terminado
	 * @throws Exception
	 */	
	public void updateEstadoProceso() throws Exception{
		
		String sql = getResource("update_proceso.sql");
		fecha_fin = this.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
		sql = Util.replace(sql, "@fecha_fin@", "'" +fecha_fin+ "'");
		sql = Util.replace(sql, "@estado@", estadoProceso);
		sql = Util.replace(sql, "@cod_proceso@", num_proceso);
		sql = Util.replace(sql, "@num_reg_leidos@", String.valueOf(num_reg_leidos));
		sql = Util.replace(sql, "@num_reg_buenos@", String.valueOf(num_reg_buenos));
		sql = Util.replace(sql, "@num_reg_malos@", String.valueOf(num_reg_malos));
			
		vec_sql.add(sql);
	}
	
	/**
	 * M&eacute;todo para actualizar el estado del archivo una vez cargado en base de datos
	 * @param cod_archivo
	 * @throws Exception
	 */
	public void updateEstadoArchivo(String cod_archivo) throws Exception{
		
		String sql = getResource("update_estado_archivo.sql");		
		sql = Util.replace(sql, "@estado@", "'Cargado'");
		sql = Util.replace(sql, "@cod_archivo@", cod_archivo);
		
		vec_sql.add(sql);	
	}

	/** 
	 * M&eacute;todo para buscar los datos asociados a un mapa
	 *  de archivo en particular cargado en base de datos 
	 * @param cod_archivo
	 * @return DataSet con los datos del mapa de un archivo
	 * @throws Exception
	 */	
	public DataSet getMapaArchivo(String cod_mapa) throws Exception{
		
		DataSet _datos_mapa = new DataSet();
		String sql = "";
		
		sql = "select * from INFI_TB_Z00_MAPAS where Z00_cod_mapa = @cod_mapa@";
		sql = Util.replace(sql, "@cod_mapa@", cod_mapa);		
		_datos_mapa = db.get(_dso, sql);
				
		return _datos_mapa;
	}
	
	
	/**
	 * M&eacute;todo para validar el valor del campo que se introducir&aacute; en la Base de Datos
	 * @param campo
	 * @return true si el campo tiene un formato valido, false en caso contrario 
	 */	
	public boolean validarCampo(String campo, String num_posicion_campo, String cod_mapa_ar) throws Exception{
		
		boolean ok = true;
		mje_campo_actual = "";
		DataSet _mapa = getMapaCampo(num_posicion_campo, cod_mapa_ar);
		
		if(_mapa.next()){					
						
			if(campo!=null & !campo.trim().equals("") & !campo.trim().equals("null")){//validar siempre que el campo sea distinto de vacio
				
				if(_mapa.getValue("z02_des_formato")!=null){
					
					//validar campo
					//N&uacute;mero con Decimales
					if(_mapa.getValue("z02_des_formato").equals("003B") || _mapa.getValue("z02_des_formato").equals("004B")){
						ok = isDecimal(campo, this.FORMATO_003B);	
						if(ok){
							ok = this.evaluarRangos(_mapa, campo,  this.FORMATO_003B);
						}
					}else{
					
						//N&uacute;mero sin decimales
						if(_mapa.getValue("z02_des_formato").equals("003A") || _mapa.getValue("z02_des_formato").equals("004A")){
							ok = isNumeric(campo,  this.FORMATO_003A);	
							if(ok){
								ok = this.evaluarRangos(_mapa, campo,  this.FORMATO_003A);
							}
						}else{
					
							//Cadenas (validar longitud)
							if(_mapa.getValue("z02_des_formato").equals("002A")){
								ok = isLengthOk(campo, _mapa.getValue("z02_num_longitud"), this.FORMATO_002A);
							}else{
					
								//C&eacute;dulas (que contengan V, J, o E o los valores de la lista especificada)
								if(_mapa.getValue("z02_des_formato").equals("006A")){
									ok = formatCedulaOk(campo, this.FORMATO_006A, _mapa.getValue("z02_de_valores_lista"));
									if(ok)//si el formato es correcto, validar longitud de cadena
										ok = isLengthOk(campo, _mapa.getValue("z02_num_longitud"),  this.FORMATO_006A);
								}else{
					
									//Valor en lista (si el valor esta contenido en una lista)
									if(_mapa.getValue("z02_des_formato").equals("005A")){
										ok = valorEnLista(campo, _mapa.getValue("z02_de_valores_lista"), this.FORMATO_005A);
									}else{
					
										//Fecha con formato DD/MM/AAAA
										/*if(_mapa.getValue("z02_des_formato").equals("001A")){
											ok = formatFechaOk(campo, "dd/MM/yyyy", this.FORMATO_001A);
										}else{
	
											//Fecha con formato MM/DD/AAAA
											if(_mapa.getValue("z02_des_formato").equals("001B")){
												ok = formatFechaOk(campo, "MM/dd/yyyy", this.FORMATO_001B);
											}else{
					
												//Fecha con formato AAAA/MM/DD
												if(_mapa.getValue("z02_des_formato").equals("001C")){
													ok = formatFechaOk(campo, "yyyy/MM/dd", this.FORMATO_001C);
												}else{
													if(_mapa.getValue("z02_des_formato").equals("001D"))
														ok = formatFechaOk(campo, "dd-MMM-yyyy", this.FORMATO_001D);
												}
								
											}
										}*/
									}
								}
							}
						}
					}
				
				}else{//no se especific&oacute; formato de campo
					ok = false;
					mje_campo_actual = "No se especific&oacute; un formato de campo";
				}
			
			}else{//si el campo es vacio
				campos_nulos++; //incrementar numero campos nulos
				if(_mapa.getValue("z02_requerido")!=null && _mapa.getValue("z02_requerido").equalsIgnoreCase("si")){
					ok = false;//datos incorrectos si es requerido
					mje_campo_actual = "Valor Requerido";
				}else
					ok = true;//datos correctos si NO es requerido
				
			}
			
						
		}
		
		return ok;
		
	}
	
		
	/**
	 * M&eacute;todo para obtener el mapeo de una columna dentro del archivo (validaciones)
	 * @param num_posicion_campo, cod_archivo
	 * @return DataSet con los datos y validaciones de un campo perteneciente a un mapa de archivo
	 */	
	public DataSet getMapaCampo(String num_posicion_campo, String cod_mapa_ar) throws Exception{
		
		DataSet _datos =new DataSet();
		String sql = "";
		
		sql = getResource("get_mapa_campo.sql");
		sql = Util.replace(sql, "@num_posicion@", num_posicion_campo);
		sql = Util.replace(sql, "@cod_mapa@", cod_mapa_ar);
		
		_datos = db.get(_dso, sql);		
		
		return _datos;		
	}
	
	/**
	 * M&eacute;todo para validar un numero con decimales
	 * @param cadena
	 * @return true si el valor del par&aacute;metro puede ser convertido a un n&uacute;mero con decimales, false en caso contrario
	 */	
	public boolean isDecimal(String cadena, String formato_campo){
		
		try {			
			Double.parseDouble(cadena);			
			return true;			
		} catch (NumberFormatException nfe){
			try {
				logger.error(nfe.getMessage()+ Utilitario.stackTraceException(nfe));
			} catch (Exception e1) {
				nfe.printStackTrace();
			}
			mje_campo_actual = getMensajeErrorEstandar()+ formato_campo+ " . No coincide formato";
			return false;			
		}
		
	}

	/**
	 * M&eacute;todo para validar un valor num&eacute;rico (sin decimales)
	 * @param cadena
	 * @return true si el valor del par&aacute;metro puede ser convertido a valor num&eacute;rico, false en caso contrario
	 */
	public boolean isNumeric(String cadena, String formato_campo){
				
		try {			
			Integer.parseInt(cadena);
			return true;
					
		} catch (NumberFormatException nfe){
			try {
				logger.error(nfe.getMessage()+ Utilitario.stackTraceException(nfe));
			} catch (Exception e1) {
				nfe.printStackTrace();
			}
			mje_campo_actual = getMensajeErrorEstandar() +formato_campo+ ". No coincide formato";
			return false;	
		}		
		
	}

	/**
	 * M&eacute;todo para validar un n&uacute;mero entero (sin decimales y mayor que cero)
	 * @param cadena
	 * @return true si el valor del par&aacute;metro puede ser convertido a un n&uacute;mero entero, false en caso contrario
	 */
	public boolean isNumEntero(String cadena, String formato_campo){
		int num = -1; 
		
		try {			
			num = Integer.parseInt(cadena);			
					
		} catch (NumberFormatException nfe){
			try {
				logger.error(nfe.getMessage()+ Utilitario.stackTraceException(nfe));
			} catch (Exception e1) {
				nfe.printStackTrace();
			}
			mje_campo_actual = getMensajeErrorEstandar() +formato_campo+ ". No coincide formato";
		}
		
		if (num > 0)
			return true;
		else
			return false;
		
	}

	/**
	 * M&eacute;todo para validar la longitud de una cadena
	 * @param cadena
	 * @param longitud_maxima
	 * @return true si el par&aacute;metro cadena tiene una longitud menor o igual a un m&aacute;ximo, false en caso contrario
	 */
	public boolean isLengthOk(String cadena, String longitud_maxima, String formato_campo){
				
		try{
			Integer.parseInt(longitud_maxima);
			
			if(cadena.length() > Integer.parseInt(longitud_maxima)){
				mje_campo_actual = getMensajeErrorEstandar() +formato_campo+ ". Longitud de campo sobrepasa el m&aacute;ximo";
				return false;
			}else				
				return true;	

		}catch(NumberFormatException nfe){
			try {
				logger.error(nfe.getMessage()+ Utilitario.stackTraceException(nfe));
			} catch (Exception e1) {
				nfe.printStackTrace();
			}
			return true; //No existe una longitud maxima establecida para comparar la cadena 
		}
		
	}	
	
	/**
	 * M&eacute;todo para validar formatos de C&eacute;dulas de Identidad
	 * @param cadena
	 * @return true si el valor del par&aacute;metro tiene un formato que corresponda a una c&eacute;dula de identidad, false en caso contrario
	 */
	public boolean formatCedulaOk(String cadena, String formato_campo, String lista){
		
		boolean comienzoOk = false;
		boolean verifOk;
		String num_cedula="";
				
		if(lista!=null){
			//Comienzo de cedula debe estar en la lista de valores
			String listaValores[] = Util.split(lista,",");//convertir lista separada por comas en arreglo
			//verificar comienzo
			for(int i=0; i<listaValores.length;i++){
				if(cadena.startsWith(listaValores[i])){
					comienzoOk=true;
					//obtener el n&uacute;mero sucesivo al prefijo de la c&eacute;dula
					num_cedula = cadena.substring(listaValores[i].length());			
					break;
				}
			}	
		}
		if(comienzoOk){//verificar si la siguiente cadena al comienzo es un numero v&aacute;lido			
			num_cedula = Util.replace(num_cedula, ".", "");//eliminar posibles puntos en la cedula
			verifOk = this.isNumEntero(num_cedula.trim(), formato_campo);	
		}else
			verifOk = false;
		
		if(verifOk){
			return true;
		}else{
			if(lista == null || lista.equals(""))//Si no existe una lista de valores para el prefijo de c&eacute;dula
				this.mje_campo_actual = this.getMensajeErrorEstandar() + formato_campo + ". No se espec&iacute;fico una lista de valores para el formato de C&eacute;dula de Identidad";
			else
				this.mje_campo_actual = this.getMensajeErrorEstandar() + formato_campo + ". Formato incorrecto de C&eacute;dula de Identidad";
			
			return false;
		}
		
		
			
	}
	
	/**
	 * M&eacute;todo para verificar que un valor se encuentre dentro de una lista de valores determinada
	 * @param cadena
	 * @param lista
	 * @return true si el valor del par&aacute;metro se encuentra contenido en una lista de valores especificada, false en caso contrario
	 */
	public boolean valorEnLista(String cadena, String lista, String formato_campo){
		
		boolean encontrado = false;
		mje_campo_actual = getMensajeErrorEstandar()+formato_campo+". Valor inv&aacute;lido";
		
		if(lista!=null){
			String listaValores[] = Util.split(lista,",");
			
			for(int i=0; i< listaValores.length; i++){
				
				if(cadena.equalsIgnoreCase(listaValores[i])){
					mje_campo_actual = "";
					encontrado = true;
					break;
				}
			}
		}
	
		return encontrado;		
	}
	
	/**
	 * M&eacute;todo para verificar si un n&uacute;mero es menor que una referencia establecida
	 * @param numero
	 * @param referencia
	 * @return true si el n&uacute;mero es menor, false en caso contrario
	 */
	public boolean esMenor(double numero, double referencia){
		
		if(numero < referencia)
			return true;
		else
			return false;				
	}
	
	/**
	 * M&eacute;todo para verificar si un n&uacute;mero es menor o igual a una referencia establecida
	 * @param numero
	 * @param referencia
	 * @return true si el n&uacute;mero es menor o igual, false en caso contrario
	 */
	public boolean esMenorOIgual(double numero, double referencia){
		
		if(numero <= referencia)
			return true;
		else
			return false;				
	}


	/**
	 * M&eacute;todo para verificar si un n&uacute;mero es mayor que una referencia establecida
	 * @param numero
	 * @param referencia
	 * @return true si el n&uacute;mero es mayor, false en caso contrario
	 */
	public boolean esMayor(double numero, double referencia){
		
		if(numero > referencia)
			return true;
		else
			return false;				
	}

	/**
	 * M&eacute;todo para verificar si un n&uacute;mero es mayor o igual que una referencia establecida
	 * @param numero
	 * @param referencia
	 * @return true si el n&uacute;mero es mayor o igual, false en caso contrario
	 */
	public boolean esMayorOIgual(double numero, double referencia){
		
		if(numero >= referencia)
			return true;
		else
			return false;				
	}
	
	/**
	 * M&eacute;todo para verificar que un n&uacute;mero se encuentre dentro de ciertos rangos establecidos
	 * @param _datos_campo
	 * @param campo
	 * @return true si el n&uacute;mero se encuentra dentro de los rangos establecidos, false en caso contrario
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean evaluarRangos(DataSet _datos_campo, String campo, String formato_campo) throws NumberFormatException, Exception{
		boolean ok=true;
		_datos_campo.first();
		_datos_campo.next();
		
		if(_datos_campo.getValue("z02_nu_numero_maximo")!=null){
			ok = this.esMenorOIgual(Double.parseDouble(campo), Double.parseDouble(_datos_campo.getValue("z02_nu_numero_maximo")));
			this.mje_campo_actual = getMensajeErrorEstandar() +formato_campo+ ". Valor mayor a requerido";
		}
		
		if(_datos_campo.getValue("z02_nu_numero_minimo")!=null){
			ok = this.esMayorOIgual(Double.parseDouble(campo), Double.parseDouble(_datos_campo.getValue("z02_nu_numero_minimo")));
			this.mje_campo_actual = getMensajeErrorEstandar() +formato_campo+ ". Valor menor a requerido";
		}
		
		/*if(_datos_campo.getValue("z02_nu_menor_campo")!=null){
			ok = this.esMenor(Double.parseDouble(campo), Double.parseDouble(_datos_campo.getValue("z02_nu_menor_campo")));
			this.mje_campo_actual = getMensajeErrorEstandar() +formato_campo+ ". Valor menor a requerido";
		}*/

		return ok;
	}

	/**
	 * M&eacute;todo para validar que una fecha posea un formato espec&iacute;fico
	 * @param cadena_fecha
	 * @param formato
	 * @return true si el valor del par&aacute;metro cadena_fecha puede ser convertido a una fecha con formato especificado, false en caso contrario
	 */
	public boolean formatFechaOk (String cadena_fecha, String formato, String formato_campo){
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);	
		
		try {			
			sdf.parse(cadena_fecha); //convertir string a date	
			return true;			
		} catch (ParseException pe){
			try {
				logger.error(pe.getMessage()+ Utilitario.stackTraceException(pe));
			} catch (Exception e1) {
				pe.printStackTrace();
			}
			mje_campo_actual = getMensajeErrorEstandar() +formato_campo+ "Fecha inv&aacute;lida";
			return false;			
		}

	}
	
	/**
	 * M&eacute;todo para obtener la fecha del sistema con un formato especifico
	 * @param formato
	 * @return cadena con la fecha actual del sistema en un formato especificado
	 * @throws ParseException
	 */	
	public String getFechaHoyFormateada(String formato) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);		
		//////////////Fecha actual///////////////////////
	    Calendar fechaHoy = Calendar.getInstance();						
		Date aux = fechaHoy.getTime();
		String fhoy = sdf.format(aux); ////darle formato			
	    ////////////////////////////////////////////////////
		
		return fhoy;
	}
	
	/**
	 * M&eacute;todo para obtener un mensaje de error estandar para los campos que no tengan formato correcto
	 * @return String con el mensaje de error
	 */
	public String getMensajeErrorEstandar(){
		
		return "Campo no cumple formato: ";
	}
	
	/**
	 * M&eacute;todo para obtener la representaci&oacute;n en porcentaje de una cantidad respecto a un total
	 * @param cantidad
	 * @param total
	 * @return double con el porcentaje representativo
	 */
	public double getPorcentaje(double cantidad, double total){
		double porc=0;
		
		if(total!=0){
			porc = (cantidad * 100)/total;		
			//redondear porcentaje con 2 decimales
			porc = Math.rint(porc *100)/100;
		}
		
		return porc;
	}
	
	/**
	 * M&eacute;todo para calcular la diferencia en hh:mm:ss entre dos tiempos determinados
	 * @param t1
	 * @param t2
	 * @param formato de las fechas de inicio y fin, por ejemplo la cadena "dd/MM/yyyy HH:mm:ss"
	 * @return String con la diferencia de tiempo
	 * @throws ParseException
	 */	
	public String getDiferenciaHoras(String t1, String t2, String formato) throws ParseException{
				
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		long horas, minutos, segundos;
		long restohora, restominuto;
		String hrs, min, seg;

		java.util.Date hora1 = sdf.parse(t1); 
		java.util.Date hora2 = sdf.parse(t2); 
		
		//obtener diferencia en milisegundos
		long lantes = hora1.getTime(); 
		long lahora = hora2.getTime(); 
		long dif_miliseg = lahora - lantes; 	

		//transformar milisegundos para obtener la cantidad en horas, minutos y segundos
		horas = dif_miliseg/3600000;
		restohora = dif_miliseg%3600000;
		
		minutos = restohora/60000;
		restominuto = restohora%60000;
		
		segundos = restominuto/1000;
		
		//colocar en formato hh:mm:ss
		if(horas<10) hrs = "0" + String.valueOf(horas);
		else hrs = String.valueOf(horas);
		
		if(minutos<10) min = "0" + String.valueOf(minutos);
		else min = String.valueOf(minutos);

		if(segundos<10) seg = "0" + String.valueOf(segundos);
		else seg = String.valueOf(segundos);
		
		String duracion = hrs +":"+ min +":"+ seg;		
	
		return String.valueOf(duracion);
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		if (flag)
		{	
			//validaciones de archivo
			try {	
				FileInputStream archivo = new FileInputStream(_record.getValue("fichero.tempfile"));
				new HSSFWorkbook(archivo);
				flag = true;		
			}catch(FileNotFoundException fnfe){ 
				try {
					logger.error(fnfe.getMessage()+ Utilitario.stackTraceException(fnfe));
				} catch (Exception e1) {
					fnfe.printStackTrace();
				}
				_record.addError("Carga Inicial", "Archivo no encontrado en la ruta especificada. Verifique.");
			} catch (java.io.IOException jie){//archivo en otro formato distinto a office
				try {
					logger.error(jie.getMessage()+ Utilitario.stackTraceException(jie));
				} catch (Exception e1) {
					jie.printStackTrace();
				}
				_record.addError("Carga Inicial", "El archivo seleccionado no tiene el formato correcto como libro de Microsoft Office Excel. Verifique.");
				flag = false;					
			}catch (java.lang.IllegalArgumentException jliae){//otro archivo de office
				try {
					logger.error(jliae.getMessage()+ Utilitario.stackTraceException(jliae));
				} catch (Exception e1) {
					jliae.printStackTrace();
				}
				_record.addError("Carga Inicial", "El archivo seleccionado no tiene el formato correcto como libro de Microsoft Office Excel. Verifique.");
				flag = false;	
			}catch(java.lang.OutOfMemoryError jloome){
				try {
					logger.error(jloome.getMessage()+ Utilitario.stackTraceException(jloome));
				} catch (Exception e1) {
					jloome.printStackTrace();
				}
				_record.addError("Carga Inicial", "El archivo seleccionado es demasiado grande. Verifique.");
				flag = false;		
			}catch (Throwable t){//cualquier otra excepcion
				try {
					logger.error(t.getMessage()+ Utilitario.stackTraceException(t));
				} catch (Exception e1) {
					t.printStackTrace();
				}
				_record.addError("Carga Inicial", "El archivo seleccionado no es correcto. Verifique.");
				flag = false;				
		    }

		
		}
		return flag;	
	}




}
