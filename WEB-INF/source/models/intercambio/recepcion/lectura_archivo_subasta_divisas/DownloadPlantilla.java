package models.intercambio.recepcion.lectura_archivo_subasta_divisas;

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
		String PlantillaSubastaDivisasAdjudicados = "Plantilla_Adjudicacion_SUBASTA_DIVISAS.xls";
		String PlantillaSubastaDivisasRechazados = "Rechazo_SUBASTA_DIVISAS.xls";
		String nombreFinal			= "";
		String rutaTemplate=		"";
				
		 if(_req.getParameter("nombre").equals("subasta_divisas_adjudicacion")){			 
			rutaTemplate= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + PlantillaSubastaDivisasAdjudicados;
			nombreFinal="Plantilla_Adjudicacion_Subasta_Divisas.xls";
		} else if (_req.getParameter("nombre").equals("subasta_divisas_rechazo")) {		
			rutaTemplate= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "adjudicacionTemplate" + separador + PlantillaSubastaDivisasRechazados;
			nombreFinal="Plantilla_Rechazo_Subasta_Divisas.xls";
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