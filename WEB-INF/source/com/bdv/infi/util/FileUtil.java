package com.bdv.infi.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;


/**
 * Clase <b>FileUtil</b> <br>
 * Encapsula la funcionalidad de lectura y escritura de archivos .txt
 * @author Mega Soft Computación <br/>
 * Copyright 2008. Banco de Venezuela, All rights reserved
 */
public class FileUtil {

	/**
	 * Variable de logger
	 */
	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	/**
	 * Retorna un recurso estatico (archivo de texto) almacenado dentro de la
	 * estructura de directorios de una clase. 
	 */
	public static String getResource(String path, Class clase, Class app) throws Exception {
		StringBuffer buf = new StringBuffer(5000);
		byte[] data = new byte[5000];
		InputStream in = null;
		
		if (clase == null) {
			in = app.getResourceAsStream(path);
		} else {
			in = clase.getResourceAsStream(path);
		}
		try {
			if (in != null) {
				while (true) {
					int len = in.read(data);
					if (len != -1) {
						buf.append(new String(data, 0, len));
					} else {
						break;
					}
				}
				return buf.toString();
			} else {
				throw new Exception("Invalid path to the static resource or	template: "	+ path);
			}
		} catch (Throwable e) {
			throw new Exception(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	 /**
	  * Lista los archivos de manera recursiva comenzando por el directorio raiz "dir".
	  * @param dir : Directorio a analizar
	  * @param ext : extension de los archivos a recuperar
	  * @return Hashtable con la informacion recuperada
	  * @throws Exception
	  */
	@SuppressWarnings("unchecked")
	public static Hashtable list ( File root, String ext, String prefijo) throws IOException,Exception {
	
		Hashtable fileList = new Hashtable();
	
	   // Nombre de los archivos del directorio.
	   String[] files;  
	   files = root.list();
	   for (int i = 0; i < files.length; i++) {
	       File f;  // One of the files in the directory.
	       f = new File(root, files[i]);
	       if ( f.isDirectory() ) 
	    	   continue;
	       else {
	       		String name = files[i];
				// Filtra el nombre del archivo
				if ( name.toLowerCase().indexOf( ext.toLowerCase() ) == 0)
					continue;
				if (prefijo != null) {
					if (name.toLowerCase().startsWith(prefijo.toLowerCase())) 
						fileList.put(name, "");
				}
				else
					fileList.put(name, "");
			}
      }
	   return fileList;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList get (String path) throws Exception {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			File f = new File(path);
			fr = new FileReader(f);

			br = new BufferedReader(fr);
			ArrayList textoFile = new ArrayList();
			int i = -1;
			String linea = br.readLine().toString();
			while (linea != null) {
				++i;
				textoFile.add(linea);
				linea = br.readLine();
			}
			return textoFile;
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
				}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Graba un recurso estatico (archivo de texto) dentro de una estructura de directorios 
	 * @param path : direccion donde se grabara el archivo 
	 * @param arregloData : arreglo de string con la informacion a grabar
	 * @exception IOException
	 */
	//Modificacion de metodo en requerimiento TTS-443 05/06/2014 NM26659
	public static void put (String path, ArrayList arreglo,boolean saltoLinea,String ... verificacionEnvioOpics) throws Exception, IOException {
				
		FileWriter archivo = null;
		try {
			//File outputFile = new File("99INFIRentaFija.csv");
			//archivo = new FileWriter(outputFile);
			archivo = new FileWriter(path);
			int j = 1;
			
			// Generar la data del archivo
			for (int i=0; i<arreglo.size(); i++) {
				String linea = null;
				if(j==arreglo.size())
				{
					linea = (String) arreglo.get(i);
				}		
				else
				{
					if(saltoLinea){
						//Modificacion del metodo para la inclucion de validacion (Si es envio a OPICS servidor Windows salto de linea en archivo sea \r )						
						if(verificacionEnvioOpics.length>0 && verificacionEnvioOpics[0]!=null && verificacionEnvioOpics[0].equalsIgnoreCase(ConstantesGenerales.INTERFACE_OPICS)){
							linea = (String) arreglo.get(i)+ "\r";	
						}else{
							//linea = (String) arreglo.get(i)+ "\r\n";
							linea = (String) arreglo.get(i)+ "\n";	
						}						
					} else {
						linea = (String) arreglo.get(i);
					}
				}
					archivo.write(linea);
					if (logger.isDebugEnabled()){
						logger.debug("Archivo, Linea("+i+"): "+linea);	
					}
					
					j++;
			}//fin for

		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception(e.toString());
		} finally {
			if (archivo != null) {
				archivo.flush();
				archivo.close();
			}//fin if
		}//fin finally
	}//fin metodo
	
	/**
	 * Graba un recurso estatico (archivo de texto) dentro de una estructura de directorios 
	 * @param path : direccion donde se grabara el archivo 
	 * @param arreglo : arreglo de string con la informacion a grabar
	 * @param saltoLinea : indica si se debe añadir salto de linea luego de la escritura de los elementos del arreglo
	 * @param insertaAlFinal : indica si la grabación del arreglo se hace al final del archivo
	 * @exception IOException
	 */
	public static void put (String path, ArrayList arreglo, boolean saltoLinea, boolean insertaAlFinal) throws Exception, IOException {
				
		FileWriter archivo = null;
		try {
			archivo = new FileWriter(path, insertaAlFinal);
			int j = 1;
			
			// Generar la data del archivo
			for (int i=0; i<arreglo.size(); i++) {
				String linea = null;
				if(j==arreglo.size())
				{
					linea = (String) arreglo.get(i);
				}		
				else
				{
					if(saltoLinea)
						//linea = (String) arreglo.get(i)+ "\r\n";
						linea = (String) arreglo.get(i)+ "\n";
					else
						linea = (String) arreglo.get(i);
				}
					archivo.write(linea);
					if (logger.isDebugEnabled()){
						logger.debug("Archivo, Linea("+i+"): "+linea);	
					}
					
					j++;
			}//fin for

		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception(e.toString());
		} finally {
			if (archivo != null) {
				archivo.flush();
				archivo.close();
			}//fin if
		}//fin finally
	}//fin metodo
	
	/**
	 * Borrar un recurso estatico (archivo de texto) de una estructura de directorios 
	 * @param path : direccion donde se esta almacenado el archivo 
	 * @throws IOException
	 */
	public static void delete (String path) throws Exception {
		try {
			new File(path).delete();
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new IOException(e.toString());
		}
	}

	/**
	 * Buscar los archivos contenidos en un directorio
	 * @param dir : Directorio a revisar
	 * @return Lista de nombres de los archivos encontrados
	 */
	public static String [] listFilesDirectory (String dir, Date fechaDesde) throws IOException,Exception
	{ 
		int ca = 0;
		String [] nbFiles = null;		
		Calendar fechaProceso = Calendar.getInstance();
		fechaProceso.setTime(fechaDesde);
		fechaProceso.add(Calendar.MINUTE,-45);
		logger.info(fechaProceso.getTime());
		try {
			File[] array; 
			File f = new File(dir); 
			f.mkdir(); 
			array = f.listFiles();
			logger.info("array.length "+array.length);
			if (array.length != 0) {
				nbFiles = new String[array.length];
				for(int i=0; i<array.length; i++) 
				{ 
					Calendar lastModified = Calendar.getInstance();
					lastModified.setTimeInMillis(array[i].lastModified());
					if (lastModified.before(fechaProceso)) 
						continue;
					nbFiles[ca] = new String(array[i].getName());
					++ca;
				}
				logger.info("nbFiles.length "+nbFiles.length);
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));			
			throw new IOException(e.toString());
		}
		String [] nameFiles = null;
		if (nbFiles != null) {
			logger.info("ca "+ca);
			nameFiles = new String [ca];
			System.arraycopy(nbFiles,0,nameFiles,0,ca);
		}
		logger.info("nameFiles");
	   return nameFiles; 
	} 

	
	/**
	 * Permite colocar un lock a un archivo para que no sea leido o modificado
	 * concurrentemente
	 */
	public static void lockFile(String fileName){
		try {
			// Get a file channel for the file
			File file = new File(fileName);
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			
			// Use the file channel to create a lock on the file.
			// This method blocks until it can retrieve the lock.
			FileLock lock = channel.lock();
			
			// Try acquiring the lock without blocking. This method returns
			// null or throws an exception if the file is already locked.
			try {
				lock = channel.tryLock();
			} catch (OverlappingFileLockException e) {
				// File is already locked in this thread or virtual machine
			}
			
			// Release the lock
			lock.release();
			
			// Close the file
			channel.close();
		} catch (Exception e) {
		}
	}
	
    /**
     * Método usado para retornar el path de la raíz de la aplicación
     * 
     * @return String path raíz de la aplicación
     */
    public static String getRootApplicationPath() throws RuntimeException{

    	String appRoot = null;
        if (appRoot == null) {
            appRoot = "/";
            try {
                // Búqueda del root path de la aplicación Web
                URL url = FileUtil.class.getResource("FileUtil.class");
                String parentPath = null;

                // Se verifica si esta clase se encuentra en un jar de la aplicación
                String currentClassPath = url.getPath().toString();
                int index = currentClassPath.indexOf("!");
                if (index != -1) {
                    currentClassPath = currentClassPath.split("[!]")[0];
                    if (currentClassPath.startsWith("file:")) {
                        currentClassPath = currentClassPath.substring(5);
                    }
                    parentPath = new File(currentClassPath).getParentFile().getPath();
                    parentPath += "/..";
                } else { // La clase no se encuentra en jar de la aplicación
                    Package pack = FileUtil.class.getPackage();
                    int levels = 0;
                    if (pack != null) {
                        levels = pack.getName().split("[.]").length;
                    }
                    String intermediatePath = "/..";
                    for (int i = 0; i < levels; i++)
                        intermediatePath += "/..";

                    parentPath = new File(url.getPath()).getParentFile().getPath();
                    parentPath += intermediatePath;
                }

                appRoot = new File(parentPath.replaceAll("%20", " ")).getCanonicalPath();

            } catch (Exception e) {
            	logger.error(e.getMessage());
                throw new RuntimeException("Error obteniendo la ruta raíz de la aplicación", e);
            }
        }
        return appRoot;
    }	
    
    /**
     * Método usado para retornar el path de la raíz de la aplicación
     * 
     * @return String path raíz de la aplicación
     */
    public static String getRootWebApplicationPath() throws RuntimeException {

    	String appRoot = null;
        if (appRoot == null) {
            appRoot = "/";
            try {
                // Búqueda del root path de la aplicación Web
                URL url = FileUtil.class.getResource("FileUtil.class");

                // Se verifica si esta clase se encuentra en un jar de la aplicación
                String currentClassPath = url.getPath().toString();
                int index = currentClassPath.indexOf("WEB-INF");
                if (index != -1) {
                    appRoot = currentClassPath.substring(0,index);
                } else { // La clase no se encuentra en jar de la aplicación
                	throw new RuntimeException("Error obteniendo la ruta raíz de la aplicación");
                }

            } catch (Exception e) {
            	throw new RuntimeException("Error obteniendo la ruta raíz de la aplicación", e);                
            }
        }
        return appRoot;
    }
    
    /** 
     * Copia un solo archivo 
     * @param src Archivo de origen
     * @param dst Archivo de destino
     * @throws IOException 
     */ 
    public static void copiarArchivo(File src, File dst) throws IOException { 
        //NM29643 infi_TTS_466
    	if(src.exists()){
    		logger.debug("Src EXISTE--------");
	    	InputStream in = new FileInputStream(src); 
	    	OutputStream out = new FileOutputStream(dst); 
	        
	        byte[] buf = new byte[1024]; 
	        int len; 
	        while ((len = in.read(buf)) > 0) { 
	            out.write(buf, 0, len); 
	        } 
	        in.close(); 
	        out.close();
        }else logger.debug("Src NO existe!!!!---------");
    } 
	
	/** Crea un archivo a partir de un StringBuffer en la ruta especificada
	 * @param sb StringBuilder a escribir
	 * @param rutaNombre ruta final del archivo
	 * @throws IOException en caso de error
	 */
    public static void crearArchivo(StringBuffer sb,String rutaNombre) throws IOException{
        OutputStream salida =   new FileOutputStream(rutaNombre);  
         salida.write(sb.toString().getBytes());
         salida.close(); 
    }
    
	/**
	 * Crea un archivo a partir de un StringBuilder en la ruta especificada
	 * @param sb StringBuilder a escribir
	 * @param rutaNombre ruta final del archivo
	 * @param agregacion true para indicar que se va adicionar información al archivo existente, false para crear el archivo desde cero
	 * @throws IOException en caso de error
	 */
    public static void crearArchivo(StringBuilder sb,String rutaNombre,boolean agregacion) throws IOException{
        OutputStream salida =   new FileOutputStream(rutaNombre,agregacion);  
         salida.write(sb.toString().getBytes());
         salida.close(); 
    }    
    
            
    public File getArchivo(String ruta, HashMap<String, String> parametrosOPS) {
		String carpeta = parametrosOPS.get(ruta);
		System.out.println("parametrosOPS " + parametrosOPS);
		// if(!carpeta.endsWith(File.separator)){
		// carpeta = carpeta.concat(File.separator);
		// }
		System.out.println("carpeta --> "+ carpeta);
		return new File(carpeta);
		// return new File(carpeta + parametrosOPS.get(archivo));
	}

}
