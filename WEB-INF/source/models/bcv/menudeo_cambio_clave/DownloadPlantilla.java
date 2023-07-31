package models.bcv.menudeo_cambio_clave;

import java.io.File;
import java.io.FileInputStream;
import javax.servlet.ServletOutputStream;
import models.msc_utilitys.MSCModelExtend;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DownloadPlantilla extends MSCModelExtend {

	public void execute() throws Exception {

		String separador = String.valueOf(File.separatorChar);
		String PlantillaMesaDivisas = "Plantilla_Carga_MESA.xls";
		String PlantillaMesaClienteDivisas = "Plantilla_Carga_Cliente_MESA.xls";
		String nombreFinal = "";
		String rutaTemplate = "";
		
		if(_req.getParameter("nombre").equals("adjudicacion_mesa")){	
			rutaTemplate = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + PlantillaMesaDivisas;
			nombreFinal = "Plantilla_MS_BDV.xls";
		}else{
			rutaTemplate = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + PlantillaMesaClienteDivisas;
			nombreFinal = "Plantilla_MS_CLIENTE.xls";
		}

		FileInputStream file = new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		_res.addHeader("Content-Disposition", "attachment;filename=" + nombreFinal);
		_res.setContentType("application/x-download");

		ServletOutputStream os = _res.getOutputStream();
		workbook.write(os);
		os.flush();
		os.close();
	}
}