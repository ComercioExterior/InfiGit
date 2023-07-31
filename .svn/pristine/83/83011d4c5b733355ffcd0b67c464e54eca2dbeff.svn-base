package models.intercambio.recepcion.lectura_archivo;

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
		String nombrePlantilla1		= "Asignaciones.xls";
		String nombrePlantilla2		= "Plantilla_Adjudicacion_SITME.xls";
		String nombrePlantilla3		= "Plantilla_Adjudicacion_SITME_CLAVENET.xls";
		String nombreFinal			= "Plantilla_Adjudicacion.xls";
		String rutaTemplate=		"";
		
		if (_req.getParameter("nombre").equals("SITME")){
			rutaTemplate= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + nombrePlantilla2;
		} else if (_req.getParameter("nombre").equals("SITME_CLAVENET")){
			rutaTemplate= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + nombrePlantilla3;
		}
		else{
		rutaTemplate= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + nombrePlantilla1;
		}
		XLSTransformer transformer	= new XLSTransformer ();
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