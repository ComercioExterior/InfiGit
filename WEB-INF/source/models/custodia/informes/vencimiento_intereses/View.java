package models.custodia.informes.vencimiento_intereses;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import models.custodia.informes.vencimiento_intereses.VencimientoInteresesExportarExcel;
import models.msc_utilitys.MSCModelExtend;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import java.util.regex.Pattern;

public class View extends MSCModelExtend {
	
	public void execute() throws Exception {
		if (_record.getValue("formato_salida").equals("excel"))
		{
			VencimientoInteresesExportarExcel excel = new VencimientoInteresesExportarExcel(_record.getValue("fecha_desde"),_record.getValue("fecha_hasta"),_record.getValue("tipo"),_dso,_app,_res, this.getUserName());
			excel.execute();
		}else{
		
			String fecha_desde= null;
			String fecha_hasta=null;
			
			//String agrupando=null;
			String imagen = ConstantesGenerales.IMAGEN_BDV;
			String separador = String.valueOf(File.separatorChar);
			
			
			/* Par&aacute;metros que le pasamos al jasper */
			Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
			parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
			parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
			parametrosJasper.put("p_titulo_reporte", "Vencimiento de Intereses y/o Capital");
			parametrosJasper.put("p_fecha_corte", new Date());
			parametrosJasper.put("fechas", new Date());
			parametrosJasper.put("fecha_hasta", new Date());
			
			/* Consulta a base de datos */
			StringBuilder filtro = new StringBuilder();
			
			
			
			if((_record.getValue("fecha_desde")!=null) && (_record.getValue("fecha_hasta")!=null)){
				fecha_desde =_record.getValue("fecha_desde");
				fecha_hasta = _record.getValue("fecha_hasta");
				filtro.append(" and INFI_TB_207_ORDENES_OPERACION.FECHA_FIN_CP between to_date('").append(fecha_desde);
				filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fecha_hasta);
				filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
			
			}
			
			Pattern patron = Pattern.compile("\\\\");
			String rutaImagen = _app.getRealPath("images");
			String ruta[] = rutaImagen.split(String.valueOf(patron));
			String rutal = "";
			for(int i=0;i<ruta.length;i++){
				rutal = rutal + ruta[i] + "/";
			}
			rutal += imagen;

			parametrosJasper.put("p_ruta_absoluta", rutal);
			parametrosJasper.put("p_query_filtro_1", filtro.toString());
			parametrosJasper.put("fechas", _record.getValue("fecha_desde"));
			parametrosJasper.put("fecha_hasta", _record.getValue("fecha_hasta"));
			
			/* La ruta al archivo .jasper */
			String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "vencimientoIntereses.jasper";
			
			/* Cargar el archivo .jasper */
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
			
			/* Llenar el reporte con los datos */
			JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
			
			/* Establecer el contenido de la p&aacute;gina como pdf */
			_res.setContentType("application/pdf");
			
			/* Embeber el reporte en la p&aacute;gina */
			JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
		}
	}//fin execute
}

