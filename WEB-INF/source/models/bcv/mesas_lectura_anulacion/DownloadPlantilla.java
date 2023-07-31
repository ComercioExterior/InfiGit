package models.bcv.mesas_lectura_anulacion;

import java.io.File;
import java.io.FileInputStream;
import javax.servlet.ServletOutputStream;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Clase que exporta a excel la ordenes que deben ser enviadas al Banco de Venezuela
 * 
 * @author jvillegas
 */
public class DownloadPlantilla extends MSCModelExtend {

	DataSet camposDinamicos = new DataSet();
	DataSet titulos = new DataSet();

	public void execute() throws Exception {

		String separador = String.valueOf(File.separatorChar);
	
		String PlantillaAnulacionMenudeo = "Plantilla_Anulacion_MESA.xls";
		String nombreFinal = "";
		String rutaTemplate = "";

		rutaTemplate = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + PlantillaAnulacionMenudeo;
		nombreFinal = "Plantilla_Anulacion_Mesa.xls";

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