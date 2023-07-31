package models.configuracion.generales.campos_dinamicos;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/** Clase que exporta a excel la ordenes 
 * que deben ser enviadas al Banco de Venezuela
 * @author jvillegas
 */
public class DownloadPlantilla extends MSCModelExtend{

	DataSet camposDinamicos = new DataSet();
	DataSet titulos = new DataSet();
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
						
		Map beans					= new HashMap();
		String separador 			= String.valueOf(File.separatorChar);			
		String PlantillaCargaCampoDinamico = "Plantilla_Carga_Campo_Dinamico.xls";
		
		String nombreFinal			= "";
		String rutaTemplate=		"";
				
		 	 
		rutaTemplate= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "campos_dinamicos" + separador + PlantillaCargaCampoDinamico;			
		nombreFinal="Plantilla_Carga_Campo_Dinamico.xls";				 		
		
		//XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		XLSTransformer transformer	= new XLSTransformer ();		
		HSSFWorkbook workbook		= new HSSFWorkbook(file);
		
		_res.addHeader("Content-Disposition","attachment;filename="+nombreFinal);
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os);
		os.flush();
		os.close();
	}	
}//Fin Clase