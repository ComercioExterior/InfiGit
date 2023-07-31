package models.intercambio.transferencia.generar_archivo_subasta_divisas;

import java.io.File;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.util.FileUtil;

/** Clase que elimina los archivos exportados 
 * @author nm25287
 */
public class LimpiarRespaldosArchivos extends MSCModelExtend{
	
	public void execute(){		
		try {
			Logger.debug(this,"Archivo a eliminar: "+_record.getValue("nombre_archivo"));
			FileUtil.delete(FileUtil.getRootWebApplicationPath() + _record.getValue("nombre_archivo"));
			Logger.debug(this,"Borrar archivo de exportación de ordenes");
			
		} catch (Exception e) {
			Logger.error(this,"Ha ocurrido un error al eliminar archivo de exportación de ordenes");
		}		
	}
	
	public void eliminarTodosLosArchivos(){ //Metodo usado sólo para pruebas en desarrollo
		try {
			File dir = new File(FileUtil.getRootWebApplicationPath());
			String[] ficheros = dir.list();
			if (ficheros == null)
				System.out.println("No hay ficheros en el directorio especificado");
			else {
				for (int x = 0; x < ficheros.length; x++) {
					if (ficheros[x].indexOf("intercambio") >= 0) {
						System.out.println(ficheros[x]);
						FileUtil.delete(FileUtil.getRootWebApplicationPath() + ficheros[x]);
					}
				}
			}
		} catch (Exception e) {
			Logger.error(this,"Ha ocurrido un error al eliminar archivo de exportación de ordenes");
		}				
	}
}
	
	
