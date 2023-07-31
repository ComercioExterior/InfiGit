package models.custodia.informes.valores_custodia;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import models.msc_utilitys.MSCModelExtend;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import java.util.regex.Pattern;

public class View extends MSCModelExtend {
	
	public void execute() throws Exception {
		
		if (_record.getValue("formato_salida").equals("excel"))
		{
			ValoresCustodiaExportarExcel excel = new ValoresCustodiaExportarExcel(_record.getValue("transa_id"),_record.getValue("fecha_orden_desde"),_record.getValue("fecha_orden_hasta"),_dso,_app,_res);
			excel.execute();
		}else{
			String transa_id = null;
			String fecha_orden_desde= null;
			String fecha_orden_hasta=null;
			
			String imagen = ConstantesGenerales.IMAGEN_BDV;
			String separador = String.valueOf(File.separatorChar);
			
			
			/* Par&aacute;metros que le pasamos al jasper */
			Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
			parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
			parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
			parametrosJasper.put("p_titulo_reporte", "Valores en Custodia");
			
			/* Consulta a base de datos */
			StringBuilder filtro = new StringBuilder();
			
			
			if(_record.getValue("transa_id")!=null && !_record.getValue("transa_id").equals("todos")){
				transa_id= _record.getValue("transa_id");
				filtro.append(" and a.transa_id = '").append(transa_id).append("'");
			}
			
			if((_record.getValue("fecha_orden_desde")!=null) && (_record.getValue("fecha_orden_hasta")!=null)){
				fecha_orden_desde =_record.getValue("fecha_orden_desde");
				fecha_orden_hasta = _record.getValue("fecha_orden_hasta");
				filtro.append(" and trunc(ordene_ped_fe_orden) between to_date('").append(fecha_orden_desde);
				filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fecha_orden_hasta);
				filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
			
			}
			
			
			
			Pattern patron = Pattern.compile("\\\\");
			String rutaImagen = _app.getRealPath("images");
			String ruta[] = rutaImagen.split(String.valueOf(patron));
			String rutal = "";
			for(int i=0;i<ruta.length;i++){
				rutal = rutal + ruta[i] + separador;
			}
			rutal += imagen;

			parametrosJasper.put("p_fecha_desde", fecha_orden_desde);
			parametrosJasper.put("p_fecha_hasta", fecha_orden_hasta);			
			parametrosJasper.put("p_ruta_absoluta", rutal);
			parametrosJasper.put("p_query_filtro_1", filtro.toString());

			
			/* La ruta al archivo .jasper */
			String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "valoresCustodia.jasper";
			
			/* Cargar el archivo .jasper */
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
			
			/* Llenar el reporte con los datos */
			_res.setContentType("application/pdf");
			
			JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
			/* Establecer el contenido de la p&aacute;gina como pdf */
			
			/* Embeber el reporte en la p&aacute;gina */
			JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
		}
	}//fin execute
}//fin clase
