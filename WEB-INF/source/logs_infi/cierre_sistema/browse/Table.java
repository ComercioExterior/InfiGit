package logs_infi.cierre_sistema.browse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Blob;
import megasoft.AbstractModel;
import megasoft.Util;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * @author Erika Valerio
 *
 *
 */
public class Table extends AbstractModel
	{	
	private Logger logger = Logger.getLogger(Table.class);	
	private String fechaLog;
	private String PREFIJO_NOMBRE_LOG = "Cierre";
	private Blob archivoBlob = null;
		
		public void execute () throws Exception
		{	
			try{	
								
				File f = Utilitario.blobToFile(PREFIJO_NOMBRE_LOG+fechaLog, archivoBlob);
				int vacio = 0;
				
				_record.setValue("filename" , f.getName());
				String sCadena = "";
				FileReader fr = new FileReader(f);
				BufferedReader bf = new BufferedReader(fr); 
				StringBuffer logStringBuffer = new StringBuffer();
				
				while ((sCadena = bf.readLine())!=null) {
					vacio++;
					sCadena = Util.replace(sCadena, "FATAL" , "<FONT COLOR='RED'><B>FATAL</B></FONT>");
					sCadena = Util.replace(sCadena, "ERROR" , "<FONT COLOR='RED'><B>ERROR</B></FONT>");
					sCadena = Util.replace(sCadena, "INFO" , "<FONT COLOR='#008000'><B>INFO</B></FONT>");
					sCadena = Util.replace(sCadena, "WARN" , "<FONT COLOR='#FF00FF'><B>WARN</B></FONT>");
					sCadena = Util.replace(sCadena, "DEBUG" , "<FONT COLOR='BLUE'><B>DEBUG</B></FONT>");
					logStringBuffer.append(sCadena).append("<BR>");
				}
				
				if(vacio>0){
					_record.setValue("logger" , logStringBuffer.toString());
				}else{
					_record.setValue("logger" , "<center>**El archivo consultado no contiene trazas de informaci&oacute;n**</center>");
				}
								
				storeDataSet("record", _record);
				
			}catch(Exception e){
				logger.error("Error en la ejecucion del logger_cierre_sistema-find: "+ e.getMessage());
				throw e;
			}
		}		
				 
		public boolean isValid()throws Exception{		
			
			boolean flag = super.isValid();
			
			if(flag){
				
				if(_req.getParameter("filtro")!=null && _req.getParameter("filtro").equals("1") && _record.getValue("fecha_log")==null){
					_record.addError("Fecha Archivo","Este campo es obligatorio");
					flag=false;	
				}else if(_record.getValue("fecha_log")!=null){
					
					CierreSistemaDAO cierreSistemaDAO = new CierreSistemaDAO(_dso);
					
					//Obtener la fecha enviada en el filtro
					fechaLog = Util.replace(_record.getValue("fecha_log"), "-", "");
							
					//Obtiene el contenido del archivo log de cierre almacenado en base de datos
					archivoBlob = cierreSistemaDAO.obtenerArchivoCierreSistema("Cierre"+fechaLog+".log",ConstantesGenerales.RUTA_ARCHIVO_CIERRE);
					
					if(archivoBlob==null){
						_record.addError("Para su informaci&oacute;n","No existe un archivo de cierre de sistema para la fecha especificada.");
						flag=false;	
					}
				}
			}
	
			return flag;
		}				
		
	}
