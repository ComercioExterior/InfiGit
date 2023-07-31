package models.bcv.intervencion_lectura_inventario;

import java.io.File;
import java.io.FileInputStream;
import javax.servlet.ServletOutputStream;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DownloadPlantilla extends MSCModelExtend {

	DataSet camposDinamicos = new DataSet();
	DataSet titulos = new DataSet();
	
	public void execute() throws Exception {
		
		String separador = String.valueOf(File.separatorChar);			
		String PlantillaMenudeoDivisas = "Plantilla_Adjudicacion_MENUDEO.xls";
		String nombreFinal = "";
		String rutaTemplate = "";
				
		 	if(_req.getParameter("nombre").equals("adjudicacion_menudeo")){			 
		 		rutaTemplate= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + PlantillaMenudeoDivisas;
		 		nombreFinal="Plantilla_Adjudicacion_Subasta_Divisas.xls";
		 	}
		 	
		FileInputStream file = new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		
		_res.addHeader("Content-Disposition","attachment;filename="+nombreFinal);
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os);
		os.flush();
		os.close();
	}	
}