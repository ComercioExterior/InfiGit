package models.custodia.informes.lista_clientes;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import megasoft.AbstractModel;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class View extends AbstractModel {

	public void execute() throws Exception {

		if (_record.getValue("formato_salida").equals("excel")) {
			ListaClientesExportarExcel excel = new ListaClientesExportarExcel(_record.getValue("tipper_id"), _record.getValue("titulo_id"), _record.getValue("tipo_producto_id"), _record.getValue("fecha_ingreso_custodia"), _dso, _app, _res);
			excel.execute();
		} else {
			String tipper_id = null;
			String titulo_id = null;
			String fecha_ingreso_custodia = null;
			String imagen = ConstantesGenerales.IMAGEN_BDV;
			String separador = String.valueOf(File.separatorChar);

			/* Par&aacute;metros que le pasamos al jasper */
			Map parametrosJasper = new HashMap();
			parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
			parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
			parametrosJasper.put("p_titulo_reporte", "Lista de Clientes");
			parametrosJasper.put("p_fecha_corte", new Date());

			/* Consulta a base de datos */
			StringBuilder filtro = new StringBuilder();

			if (_record.getValue("tipper_id") != null) {
				tipper_id = _record.getValue("tipper_id");
				filtro.append(" and c.tipper_id = '").append(tipper_id).append("'");
			}

			if (_record.getValue("titulo_id") != null && !_record.getValue("titulo_id").equals("todos")) {
				titulo_id = _record.getValue("titulo_id");
				filtro.append(" and trim(b.secid) = '").append(titulo_id).append("'");
			}

			if ((_record.getValue("fecha_ingreso_custodia") != null)) {
				fecha_ingreso_custodia = _record.getValue("fecha_ingreso_custodia");
				filtro.append(" and a.titulo_fe_ingreso_custodia <= to_date('").append(fecha_ingreso_custodia);
				filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
			}

			if (_record.getValue("tipo_producto_id") != null && !_record.getValue("tipo_producto_id").equals("")) {
				filtro.append(" and trim(a.tipo_producto_id) = '").append(_record.getValue("tipo_producto_id")).append("'");
			}

			Pattern patron = Pattern.compile("\\\\");
			String rutaImagen = _app.getRealPath("images");
			String ruta[] = rutaImagen.split(String.valueOf(patron));
			String rutal = "";
			for (int i = 0; i < ruta.length; i++) {
				rutal = rutal + ruta[i] + separador;
			}
			rutal += imagen;

			parametrosJasper.put("p_ruta_absoluta", rutal);
			parametrosJasper.put("p_query_filtro_1", filtro.toString());
			parametrosJasper.put("p_query_orderBy_1", " order by c.client_nombre ASC");

			/* La ruta al archivo .jasper */
			String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "listaCliente.jasper";

			/* Cargar el archivo .jasper */
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);

			/* Llenar el reporte con los datos */

			JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());

			/* Establecer el contenido de la p&aacute;gina como pdf */
			// _res.setContentType("application/pdf");
			/* Embeber el reporte en la p&aacute;gina */
			JasperExportManager.exportReportToPdfStream(report, _res.getOutputStream());

		}
	}
}
