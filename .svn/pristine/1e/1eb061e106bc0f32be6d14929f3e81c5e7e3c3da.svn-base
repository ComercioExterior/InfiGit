package framework_components.loggerview.browse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Util;

import org.apache.log4j.Logger;

import com.bdv.infi.util.FileUtil;

/**
 * @author Gabriell Calatrava
 *
 *
 */
public class Table extends AbstractModel
	{	
	private Logger logger = Logger.getLogger(Table.class);
	private String LOGGER_CONFIG_FILE = "/WEB-INF/log4j.properties";
	private Properties mp = new Properties();
		
		public void execute () throws Exception
		{	
			
			try{			
				//Obtiene la ruta inicial						
				String directorio = _record.getValue("directorio");
				
				if(directorio==null||directorio==""){
					directorio = getKey("log4j.appender.output.File");
					directorio=directorio.replace("app_log.log", "");
					
				}

				//Asigna archivo de log para visualizar en la vista
				_record.setValue("filename" , directorio);
				
				//DataSet para mostrar los diferente tipos de archivo ubicados en la carpeta de log de la aplicacion
				DataSet _ficheros = new DataSet();
				_ficheros.append("nombre",java.sql.Types.VARCHAR);
				_ficheros.append("fecha",java.sql.Types.VARCHAR);
				
				//Archivo				
				if(directorio!=null){
					//Archivos en el directorio
					File dir = new File(directorio);
					String[] ficheros = null;
					if (dir.isDirectory()){
						ficheros = dir.list();

						  for(int i=0;i<ficheros.length;i++){
							  
							  String rutaArchivo = directorio+ficheros[i];
							  File archivo = new File(rutaArchivo);
							  Date fechaModificacion = new Date(archivo.lastModified());
							  SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aaa");
							  _ficheros.addNew();
							  _ficheros.setValue("nombre",ficheros[i]);
							  _ficheros.setValue("fecha",formato.format(fechaModificacion));
						  }				
					} else{
						_record.setValue("filename" , directorio);
						String sCadena = "";
						FileReader fr = new FileReader(directorio);
						BufferedReader bf = new BufferedReader(fr); 
						StringBuffer logStringBuffer = new StringBuffer();
		
						while ((sCadena = bf.readLine())!=null) {
							sCadena = Util.replace(sCadena, "FATAL" , "<FONT COLOR='RED'><B>FATAL</B></FONT>");
							sCadena = Util.replace(sCadena, "ERROR" , "<FONT COLOR='RED'><B>ERROR</B></FONT>");
							sCadena = Util.replace(sCadena, "INFO" , "<FONT COLOR='#008000'><B>INFO</B></FONT>");
							sCadena = Util.replace(sCadena, "WARN" , "<FONT COLOR='#FF00FF'><B>WARN</B></FONT>");
							sCadena = Util.replace(sCadena, "DEBUG" , "<FONT COLOR='BLUE'><B>DEBUG</B></FONT>");
							logStringBuffer.append(sCadena).append("<BR>");
						}
						
						_record.setValue("logger" , logStringBuffer.toString());				
					}
				}				
				//Publicamos la lista de archivos en el directorio de logs
				storeDataSet("ficheros",_ficheros);
				storeDataSet("record", _record);
			}catch(Exception e){
				System.out.println("Error en la ejecucion del logger-find: "+ e.getMessage());
				throw e;
			}
		}		
		
		 public String getKey(String key)  throws Exception{
			 	String msg ="";
				try {
					File msgFile = new File(FileUtil.getRootWebApplicationPath() + LOGGER_CONFIG_FILE);
					mp.load(new FileInputStream(msgFile));
					msg=this.mp.getProperty(key);
				} catch (Exception e) {
					logger.error("No se encontro la ruta de escritura de LOG4J");
					throw e;
				}		        
				return msg;
		    }
		
		
	}
