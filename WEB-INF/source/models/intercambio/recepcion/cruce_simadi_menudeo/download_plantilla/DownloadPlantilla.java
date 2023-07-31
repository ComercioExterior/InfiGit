package models.intercambio.recepcion.cruce_simadi_menudeo.download_plantilla;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/** Clase que exporta a excel la ordenes 
 * que deben ser enviadas al Banco de Venezuela
 * @author jvillegas
 */
public class DownloadPlantilla extends MSCModelExtend{

	DataSet camposDinamicos = new DataSet();
	DataSet titulos = new DataSet();
	
	public void execute() throws Exception {
						
		String separador 			= String.valueOf(File.separatorChar);			
		String nombreFinal			= "";
		String rutaTemplate 		= _app.getRealPath("WEB-INF") + separador;
				
		 if(_req.getParameter("nombre").equals(ConstantesGenerales.INDICADOR_CRUCE)){			 
			rutaTemplate += ConstantesGenerales.DIR_TEMPLATE_CRUCE + separador + ConstantesGenerales.ARCH_TEMPLATE_CRUCE_SIMADI_MENUDEO + ConstantesGenerales.EXTENSION_DOC_XLS;
			nombreFinal = ConstantesGenerales.ARCH_TEMPLATE_CRUCE_SIMADI_MENUDEO + ConstantesGenerales.EXTENSION_DOC_XLS;
		} else if (_req.getParameter("nombre").equals(ConstantesGenerales.INDICADOR_NO_CRUCE)) {		
			rutaTemplate += ConstantesGenerales.DIR_TEMPLATE_CRUCE + separador + ConstantesGenerales.ARCH_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO + ConstantesGenerales.EXTENSION_DOC_XLS;
			nombreFinal = ConstantesGenerales.ARCH_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO + ConstantesGenerales.EXTENSION_DOC_XLS;
		}
		 
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= new HSSFWorkbook(file);
		
		_res.addHeader("Content-Disposition","attachment;filename="+nombreFinal);
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os);
		os.flush();
		os.close();
	}	
}//Fin Clase