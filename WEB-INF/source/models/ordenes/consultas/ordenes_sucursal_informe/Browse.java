package models.ordenes.consultas.ordenes_sucursal_informe;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import models.exportable.ExportableJasper;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Browse extends ExportableJasper {

	String nombreArchivo = "";

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {

		nombreArchivo = obtenerNombreArchivo("informeSucursal");
		registrarInicio(nombreArchivo);

		// Definicion de variables
		String imagen = ConstantesGenerales.IMAGEN_BDV;
		String separador = String.valueOf(File.separatorChar);
		StringBuffer p_filter = new StringBuffer();

		/* Par&aacute;metros que le pasamos al jasper */
		Map<String, Serializable> parametrosJasper = new HashMap<String, Serializable>();
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		parametrosJasper.put("p_aplicacion_descripcion", "Informe");
		parametrosJasper.put("p_titulo_reporte", "Ordenes por Sucursal");
		parametrosJasper.put("p_fecha_corte", new Date());

		// Creacion del filtro de busqueda
		if (_record.getValue("unidad_inversion") != null && _record.getValue("unidad_inversion") != "")
			p_filter.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(_record.getValue("unidad_inversion"));

		if (_record.getValue("fe_ord_desde") != null && _record.getValue("fe_ord_hasta") != null) {
			p_filter.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden between to_date('").append(_record.getValue("fe_ord_desde")).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(_record.getValue("fe_ord_hasta")).append("','" + ConstantesGenerales.FORMATO_FECHA + "')");
		}
		if (_record.getValue("status") != null)
			p_filter.append(" and infi_tb_204_ordenes.ordsta_id='").append(_record.getValue("status")).append("'");
		if (_record.getValue("sucursal") != null)
			p_filter.append(" and infi_tb_204_ordenes.ordene_usr_sucursal='").append(_record.getValue("sucursal")).append("'");

		// Solo transacciones del proceso de Toma de ordenes
		p_filter.append(" and infi_tb_204_ordenes.transa_id IN ('");
		p_filter.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','");
		p_filter.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");

		// registrar los datasets exportados por este modelo
		Pattern patron = Pattern.compile("\\\\");
		String rutaImagen = _app.getRealPath("images");
		String ruta[] = rutaImagen.split(String.valueOf(patron));
		String rutal = "";
		for (int i = 0; i < ruta.length; i++) {
			rutal = rutal + ruta[i] + "/";
		}
		rutal += imagen;

		parametrosJasper.put("p_ruta_absoluta", rutal);
		parametrosJasper.put("p_filter", p_filter.toString());
		/* La ruta al archivo .jasper */

		String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "ordenes_vehiculo" + separador + "ordenesVehiculo.jasper";

		generatePDFOutput(archivoJasper, parametrosJasper);

		/* Compilar el archivo .jasper */
		// JasperReport jasperReport = JasperCompileManager.compileReport(archivoJasper);
		/* Cargar el archivo .jasper */
		// JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
		/* Llenar el reporte con los datos */
		// JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
		/* Establecer el contenido de la p&aacute;gina como pdf */
		// _res.setContentType("application/pdf");
		/* Embeber el reporte en la p&aacute;gina */
		// JasperExportManager.exportReportToPdfStream(report, _res.getOutputStream());
	}

	public boolean isValid() throws Exception {
		boolean flag = super.isValid();

		String fecha_desde = _record.getValue("fe_ord_desde");
		String fecha_hasta = _record.getValue("fe_ord_hasta");
		java.util.Date fecha = new Date();
		java.text.SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

		if (flag) {
			if ((fecha_desde != null) && (fecha_hasta != null)) {
				Date fecha_1 = formato.parse(fecha_desde);
				Date fecha_2 = formato.parse(fecha_hasta);

				if (fecha_1.compareTo(fecha_2) > 0) {
					_record.addError("Fecha hasta", "Este campo debe tener una fecha posterior o igual al de Fecha Desde");
					flag = false;
				}

				if (fecha_1.compareTo(fecha) > 0) {
					_record.addError("Fecha Desde", "Este campo debe ser menor a la fecha actual:" + fecha);
					flag = false;
				}

				if (fecha_2.compareTo(fecha) > 0) {
					_record.addError("Fecha Hasta*", "Este campo debe ser menor a la fecha actual:" + fecha);
					flag = false;
				}
			}
		}
		return flag;
	}

	protected void generatePDFOutput(String reportName, Map parameters) throws Exception {
		try {
			String rutaFinal = getRutaTemporal() + nombreArchivo;
			JasperPrint jasperPrint = null;
			JasperReport reporteCompilado = getReporteCompilado(reportName, parameters);
			jasperPrint = llenarReporte(reporteCompilado, parameters);
			this.exportPDF(rutaFinal, jasperPrint);
			obtenerSalida(rutaFinal);
			registrarFin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}