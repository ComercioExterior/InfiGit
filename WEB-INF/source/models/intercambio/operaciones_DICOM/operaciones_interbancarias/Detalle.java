package  models.intercambio.operaciones_DICOM.operaciones_interbancarias;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import megasoft.AbstractModel;
import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Detalle extends AbstractModel
	{	
	private Logger logger = Logger.getLogger(Detalle.class);
	protected HashMap<String, String> parametrosRecepcionDICOM;
	
		public void execute () throws Exception
		{	
			
			try{			
				//Obtiene la ruta inicial						
				String directorio =_record.getValue("directorio");
				
				if(directorio==null||directorio==""){
					ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);						
					parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
					//directorio =  parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_DICOM_RESP);
					directorio =  parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_OP_INTERBANCARIAS_DICOM_RECEP);
					
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
							/*sCadena = Util.replace(sCadena, "FATAL" , "<FONT COLOR='RED'><B>FATAL</B></FONT>");
							sCadena = Util.replace(sCadena, "ERROR" , "<FONT COLOR='RED'><B>ERROR</B></FONT>");
							sCadena = Util.replace(sCadena, "INFO" , "<FONT COLOR='#008000'><B>INFO</B></FONT>");
							sCadena = Util.replace(sCadena, "WARN" , "<FONT COLOR='#FF00FF'><B>WARN</B></FONT>");
							sCadena = Util.replace(sCadena, "DEBUG" , "<FONT COLOR='BLUE'><B>DEBUG</B></FONT>");*/
							logStringBuffer.append(sCadena).append("<BR>");
						}
						
						_record.setValue("archivo" , logStringBuffer.toString());				
					}
				}				
				//Publicamos la lista de archivos en el directorio 
				storeDataSet("ficheros",_ficheros);
				storeDataSet("record", _record);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Error en la ejecucion del logger-find: "+ e.getMessage());
				throw e;
			}
		}
		
	}
