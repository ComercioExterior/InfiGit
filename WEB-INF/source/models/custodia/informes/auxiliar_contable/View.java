package models.custodia.informes.auxiliar_contable;

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
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class View extends AbstractModel {

	
	public void execute() throws Exception {
		
		String client_id = null;
		int mes = Integer.parseInt(_record.getValue("mes"));
		int anio = Integer.parseInt(_record.getValue("anio"));
		String imagen = ConstantesGenerales.IMAGEN_BDV;
		String separador = String.valueOf(File.separatorChar);
		
		/* Par&aacute;metros que le pasamos al jasper */
		Map parametrosJasper = new HashMap();
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
		parametrosJasper.put("p_titulo_reporte", "AUXILIAR CONTABLE");
		parametrosJasper.put("p_fecha_corte", new Date());
		
		/* Consulta a base de datos */
		StringBuilder filtro = new StringBuilder();
		
		if(_record.getValue("client_id")!=null){
			client_id = _record.getValue("client_id");
			filtro.append(" and c.client_id=").append(client_id);
		}
		
		//Fechas de busqueda para el query
		//rango de 6 meses: rano desde(primer dia del mes hace 6 meses) hasta(ultimo dia del mes cunsultado)
		String desde = "'01-"+mes+"-"+anio+"'";
		String hasta = "'28-"+mes+"-"+anio+"'"; // se usa 28 porque febrero podria llegar hasta ese dia a menos que sea bisiesto
		
		
		String rango_desde = "add_months(to_date("+desde+",'dd-mm-yyyy'),-5)";
		String rango_hasta = "last_day(to_date("+hasta+",'dd-mm-yyyy'))";
		
		//Transaccion que buscaremos, se setea en caso de que algun dia cambie pa evitar modificar el query
		String transa_id = "'"+TransaccionNegocio.CUSTODIA_COMISIONES+"'";
		
		//Estatus operaciones a buscar, en caso de que algun dia cambie pa evitar modificar el query
		String status = "('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"')";
		
		Pattern patron = Pattern.compile("\\\\");
		String rutaImagen = _app.getRealPath("images");
		String ruta[] = rutaImagen.split(String.valueOf(patron));
		String rutal = "";
		for(int i=0;i<ruta.length;i++){
			rutal = rutal + ruta[i] + separador;
		}
		rutal += imagen;
		
		String fecha_consulta = String.valueOf(mes)+" / "+String.valueOf(anio);

		parametrosJasper.put("p_ruta_absoluta", rutal);
		parametrosJasper.put("p_fecha_consulta", fecha_consulta);
		parametrosJasper.put("p_query_filtro", filtro.toString());
		parametrosJasper.put("p_query_fecha_desde", rango_desde);
		parametrosJasper.put("p_query_fecha_hasta", rango_hasta);
		parametrosJasper.put("p_query_transa_id", transa_id);
		parametrosJasper.put("p_query_status_operacion", status);
		parametrosJasper.put("p_query_orderby", " order by num_fecha ASC , client_id");
		
		/* La ruta al archivo .jasper */
		String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "auxiliarContable.jasper";
		
		/* Cargar el archivo .jasper */
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
		
		/* Llenar el reporte con los datos */
		
		JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
		
		/* Establecer el contenido de la p&aacute;gina como pdf */
		//_res.setContentType("application/pdf");
		
		/* Embeber el reporte en la p&aacute;gina */
		JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
	}
}
