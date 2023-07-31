package models.custodia.informes.valores_garantias;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import megasoft.AbstractModel;
import models.custodia.informes.valores_garantias.ValoresGarantiasExportarExcel;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import java.util.regex.Pattern;

public class View extends AbstractModel {

	public void execute() throws Exception {

		if (_record.getValue("formato_salida").equals("excel")) {
			ValoresGarantiasExportarExcel excel = new ValoresGarantiasExportarExcel(_record.getValue("client_id"), _record.getValue("tipblo_id"),_record.getValue("tipo_producto_id"), _record.getValue("fecha_bloqueo"), _dso, _app, _res);
			excel.execute();
		} else {
			String client_id = null;
			String tipblo_id = null;
			String fecha_bloqueo = null;
			String imagen = ConstantesGenerales.IMAGEN_BDV;
			String separador = String.valueOf(File.separatorChar);
			String fecha_sistema_1= _record.getValue("fecha_bloqueo");
			SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);//nm36635
			String fecha_reme = fecha_sistema_1; //nm3363
			Date fecha_remediacion = null;
			fecha_remediacion = formato_fecha.parse(fecha_reme);
			SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
			String fecha_funcion = formatter.format(fecha_remediacion);
			int fecha_sistema= Integer.parseInt(fecha_funcion);
			System.out.println(fecha_sistema);

			/* Par&aacute;metros que le pasamos al jasper */
			Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
			parametrosJasper.put("p_fecha_sistema", fecha_funcion.toString());
			parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
			parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
			parametrosJasper.put("p_titulo_reporte", "Cuadro de Valores en Garantía");
			parametrosJasper.put("p_fecha_corte", new Date());

			/* Consulta a base de datos */
			StringBuilder filtro = new StringBuilder();

			if (_record.getValue("client_id") != null && !_record.getValue("client_id").equals("todos")) {
				client_id = _record.getValue("client_id");
				filtro.append(" and a.client_id = ").append(client_id);
			}

			if (_record.getValue("tipblo_id") != null) {
				tipblo_id = _record.getValue("tipblo_id");
				filtro.append(" and a.tipblo_id = '").append(tipblo_id).append("'");
			}
			
			if (_record.getValue("tipo_producto_id") != null && !_record.getValue("tipo_producto_id").equals("")) {
				filtro.append(" and a.tipo_producto_id = '").append(_record.getValue("tipo_producto_id")).append("'");
			}			

			if (_record.getValue("fecha_bloqueo") != null) {
				fecha_bloqueo = _record.getValue("fecha_bloqueo");
				filtro.append(" and trunc(a.fecha) <= to_date('").append(fecha_bloqueo).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");

				// Inserta la fecha de cierre
				parametrosJasper.put("p_fechaCierre", " to_date('" + fecha_bloqueo + "','" + ConstantesGenerales.FORMATO_FECHA + "')");
			}

			Pattern patron = Pattern.compile("\\\\");
			String rutaImagen = _app.getRealPath("images");
			String ruta[] = rutaImagen.split(String.valueOf(patron));
			String rutal = "";
			for (int i = 0; i < ruta.length; i++) {
				rutal = rutal + ruta[i] + "/";
			}
			rutal += imagen;

			parametrosJasper.put("p_ruta_absoluta", rutal);
			parametrosJasper.put("p_query_filtro_1", filtro.toString());

			/* La ruta al archivo .jasper */
			String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "valoresGarantia.jasper";

			/* Cargar el archivo .jasper */
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);

			/* Llenar el reporte con los datos */
			JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());

			/* Establecer el contenido de la p&aacute;gina como pdf */
			_res.setContentType("application/pdf");

			/* Embeber el reporte en la p&aacute;gina */
			JasperExportManager.exportReportToPdfStream(report, _res.getOutputStream());
		}
	}
}
