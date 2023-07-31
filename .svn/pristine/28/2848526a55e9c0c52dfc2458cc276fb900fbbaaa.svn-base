package com.bdv.infi.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.Util;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.Utilitario;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;

/**
 * Definición de la Clase <b>FTPUtil</b> la cual gestiona los accesos
 * a los directorios FTP
 * @author elaucho
 * Copyright 2008. Banco de Venezuela, All rights reserved
 */
public class FTPUtil{
/**
 * HashMap de parametros USUARIO, PASSWORD FTP
 */
HashMap<String,String> parametrosFTP = new HashMap<String, String>();
/**
 * Parametros Opics
 */
HashMap<String,String> parametrosOpics = new HashMap<String, String>();
/**
 * Variable de gestion de logs
 */
	public static Logger logger = Logger.getLogger(FTPUtil.class);

/**
 * Host a conectarse
 */
	private String direccionIpServidorFTP;

	/**
	 * DataSource
	 */
	private DataSource _dso;
/**
 * Constructor de un objeto del tipo <b>FTPUtil</b>
 * @param direccionIpServidorFTP
 * @param usuarioFTP
 * @param password
 */
	public FTPUtil(String direccionIpServidorFTP,DataSource _dso) {
		this.direccionIpServidorFTP = direccionIpServidorFTP; 
		this._dso					= _dso;
	
	}
/**
 * Envía un archivo FTP a una dirección dada con tipo BINARY
 * @param String localFile : Archivo temporal generado en disco
 * @param String remotefile : Ruta + Nombre del archivo concatenado
 * @param String ruta : Ruta donde se debe guardar el archivo en el servidor al ser enviado vía FTP
 * @param boolean validar : Indica si se debe validar si el archivo existe en el servidor
 * @throws Exception en caso de error
 */
	public void putFTP(String localFile, String remotefile,String ruta,boolean validar) throws Exception{
		ftpPut(localFile, remotefile, ruta, validar, FTPTransferType.BINARY);
	}
	
	/**
	 * Envía un archivo FTP a una dirección dada con tipo ASCCI
	 * @param String localFile : Archivo temporal generado en disco
	 * @param String remotefile : Ruta + Nombre del archivo concatenado
	 * @param String ruta : Ruta donde se debe guardar el archivo en el servidor al ser enviado vía FTP
	 * @param boolean validar : Indica si se debe validar si el archivo existe en el servidor
	 * @throws Exception en caso de error
	 */	
	public void putFTPAscci(String localFile, String remotefile,String ruta,boolean validar) throws Exception{
		ftpPut(localFile, remotefile, ruta, validar, FTPTransferType.ASCII);
	}
	
	/**
	 * Envía un archivo FTP a una dirección dada
	 * @param String localFile : Archivo temporal generado en disco
	 * @param String remotefile : Ruta + Nombre del archivo concatenado
	 * @param String ruta : Ruta donde se debe guardar el archivo en el servidor al ser enviado vía FTP
	 * @param boolean validar : Indica si se debe validar si el archivo existe en el servidor
	 * @param FTPTransferType tipo : Indica la forma de hacer la transferencia del archivo
	 * @throws Exception
	 */	
	private void ftpPut(String localFile, String remotefile,String ruta,boolean validar, FTPTransferType tipo) throws Exception{
		try {
			logger.info("localFile: "+localFile);
			logger.info("remotefile: "+remotefile);
			logger.info("ruta: "+ruta);
			logger.info("tipo transferencia: "+tipo);
			
			System.out.println("localFile: "+localFile);
			System.out.println("remotefile: "+remotefile);
			System.out.println("ruta: "+ruta);
			System.out.println("tipo transferencia: "+tipo);
			this.buscarParametrosFTP();
			
			/*
			 * Si la ruta es en la raiz, eliminamos el slash para validar que el nombre del archivo
			 *se encuentre o no en el directorio donde guardaremos el mismo
			 **/
			if(ruta.equals("/") || ruta.equals("\\")){
				ruta = "";
			}			
			FTPClient ftpclient = new FTPClient();
			logger.info("direccionIpServidorFTP--->"+direccionIpServidorFTP);
			ftpclient.setRemoteHost(direccionIpServidorFTP);
			System.out.println("direccionIpServidorFTP : " + direccionIpServidorFTP);
			ftpclient.connect();
			ftpclient.login(parametrosFTP.get(parametrosOpics.get(ParametrosSistema.USUARIO_FTP)), 
					parametrosFTP.get(parametrosOpics.get(ParametrosSistema.PASSWORD_FTP)));
			ftpclient.setType(tipo);
			if(validar){
				String directorio[]= ftpclient.dir(ruta);
				
				for(int i = 0;i<directorio.length;i++){
					
					logger.info("Validando si archivo existe en directorio");
					logger.info("Archivo a enviar..."+remotefile);
					logger.info("Directorio FTP..."+ruta.concat(directorio[i]));
					
					if(remotefile.equals(ruta.concat(directorio[i])))
					{
						logger.info("El archivo que intenta enviar, ya existe en el directorio...");
						throw new Exception("El archivo que intenta enviar, ya existe en el directorio");
					}	
				}
				
				logger.info("El archivo no existe en directorio, se intenta enviar v&iacute;a FTP");
			}//fin validar
			
			ftpclient.put(localFile, remotefile);
			ftpclient.quit();
			
			logger.info("El archivo Opics fue enviado v&iacute;a FTP al siguiente directorio " + remotefile);
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw e;
		}	
	}
	
	/**
	 * Envía un archivo FTP a una dirección dada
	 * @param String localFile : Archivo temporal generado en disco
	 * @param String remotefile : Ruta + Nombre del archivo concatenado
	 * @param String ruta : Ruta donde se debe guardar el archivo en el servidor al ser enviado vía FTP
	 * @param boolean validar : Indica si se debe validar si el archivo existe en el servidor
	 * @throws Exception
	 */
		/**public void putFTP(String localFile, String remotefile,String ruta,boolean validar) throws Exception{
			try {
				
				this.buscarParametrosFTP();
				InputStream inputStream = new FileInputStream(localFile);

				/*
				 * Si la ruta es en la raiz, eliminamos el slash para validar que el nombre del archivo
				 *se encuentre o no en el directorio donde guardaremos el mismo
				 *
				if(ruta.equals("/") || ruta.equals("\\")){
					ruta = "";
				}			
				org.apache.commons.net.ftp.FTPClient ftpclient = new org.apache.commons.net.ftp.FTPClient();
				ftpclient.connect(direccionIpServidorFTP);
				ftpclient.login(parametrosFTP.get(parametrosOpics.get(ParametrosSistema.USUARIO_FTP)), 
						parametrosFTP.get(parametrosOpics.get(ParametrosSistema.PASSWORD_FTP)));
				ftpclient.setFileType(ftpclient.BINARY_FILE_TYPE);
				
				if(validar){
					
				String directorio[]= ftpclient.dir(ruta);
				ftpclient.listFiles(arg0);
				
				for(int i = 0;i<directorio.length;i++){

					if(remotefile.equals(ruta.concat(directorio[i])))
					{
						logger.info("El archivo que intenta enviar, ya existe en el directorio...");
						throw new Exception("El archivo que intenta enviar, ya existe en el directorio");
					}	
				}
			
				}//fin validar
				
				
				ftpclient.cdup();
				ftpclient.changeToParentDirectory();
				ftpclient.cwd("");
				ftpclient.listNames();
				ftpclient.pwd();
				ftpclient.changeWorkingDirectory("prueba$");
				ftpclient.changeWorkingDirectory("opic");
				ftpclient.storeFile("INFIFX.csv", inputStream);
				//ftpclient.put(localFile, remotefile);
				ftpclient.quit();
			} catch (Exception e) {
				throw e;
			}
		}// fin metodo envio FTP**/
/**
 * Borrar un recurso estatico (archivo de texto) de una estructura de directorios 
 * @param path : direccion donde se esta almacenado el archivo 
 * @throws IOException
 */
	public static void delete (String path) throws IOException,Exception {
		try {
			new File(path).delete();
		} catch (Exception e) {

			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new IOException(e.toString());
		}
	}
	
/**
 * Graba un recurso estatico (archivo de texto) dentro de una estructura de directorios 
 * @param path : direccion donde se grabara el archivo 
 * @param arregloData : arreglo de string con la informacion a grabar
 * @exception IOException
 */
	public static void put (String path, ArrayList arreglo) throws Exception, IOException {
		FileWriter archivo = null;
		try {
			// Crea el nuevo archivo si no existe.
			try {
				archivo = new FileWriter(path);
			} catch (Exception e) {
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
				throw new IOException(e.toString());
			}
			// Generar la data del archivo
			for (int i=0; i<arreglo.size(); i++) {
				String linea = (String) arreglo.get(i)+ "\r\n";
				archivo.write(linea);			
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception(e.toString());
		} finally {
			if (archivo != null) {
				archivo.flush();
				archivo.close();
			}
		}
	}
	/**
	 * Busca en un archivo de configuracion la clave y usuario FTP
	 * @throws Exception
	 */
	public void buscarParametrosFTP()throws Exception{

		/*
		 * Se busca la ruta y el nombre del archivo donde estara ubicado el archivo de configuracion,
		 * para leer el usuario y password FTP
		 */
		ParametrosDAO parametrosDAO = new ParametrosDAO(this._dso);
		parametrosOpics = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_OPICS);

		//HOME DIRECTORY
		String homeDirectory = parametrosOpics.get(ParametrosSistema.HOME_DIRECTORY_FILE);
		logger.info("buscarParametrosFTP->homeDirectory: "+homeDirectory);
		System.out.println("homeDirectory: " + homeDirectory);
		//homeDirectory = "C:\\aplicacion1\\infi\\ssa_infi";
		//Archivo que contiene la configuracion de password y clave FTP y variables
		FileReader fileReader 				 = new FileReader(homeDirectory);
		BufferedReader entrada 				 = new BufferedReader(fileReader);
		String str;
		String cadena 						 = null;
		
		//Mientras el archivo tenga lineas por leer se guarda en un String
		while((str=entrada.readLine())!=null){
			cadena +=str+"/r";
		}//fin while
		
		//Se realiza split de la cadena por el simbolo retorno de carro
		String arreglo[] = Util.split(cadena,"/r");
		
		//Se recorre dicho arreglo para realizar el split por clave valor y setearlo al HashMap
		for (int k=1;k<arreglo.length-1;k++){
			String []claveValor = Util.split(arreglo[k], "=");
			String clave = claveValor[0];
			String valor = claveValor[1];
			System.out.println("clave : " + clave);
			System.out.println("valor : " + valor);
			logger.error("clave : " + clave);
			logger.error("valor : " + valor);
			parametrosFTP.put(clave, valor);
		}//fin for
	}

}//fin clase
