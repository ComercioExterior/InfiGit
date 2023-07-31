package models.custodia.informes.titulos_bloqueados;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import megasoft.AbstractModel;
import models.custodia.informes.titulos_bloqueados.titulosBloqueadosExcel;
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
		
		if (_record.getValue("formato_salida").equals("excel"))
		{
			titulosBloqueadosExcel excel = new titulosBloqueadosExcel(_record.getValue("client_id"),_record.getValue("titulo_id"),_record.getValue("fe_blo_hasta"),_record.getValue("fe_blo_desde"),_dso,_app,_res);
			excel.execute();
		}else{
		String client_id = null;
		String fe_blo_hasta = null;
		String fe_blo_desde = null;
		String titulo_id=null;
		String imagen = ConstantesGenerales.IMAGEN_BDV;
		String separador = String.valueOf(File.separatorChar);
		String fecha_sistema1=_record.getValue("fe_blo_hasta");
		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String fecha_reme = fecha_sistema1;
		Date fecha_remediacion = null;
		fecha_remediacion = formato_fecha.parse(fecha_reme);
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha_remediacion);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_sistema);
		
		
		
		/* Par&aacute;metros que le pasamos al jasper */
		Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
		
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
		parametrosJasper.put("p_titulo_reporte", "Títulos Bloqueados");
		parametrosJasper.put("p_fecha_corte", new Date());
		parametrosJasper.put("fecha_desde", new Date());
		parametrosJasper.put("fecha_hasta", new Date());
		
		/* Consulta a base de datos */
		StringBuilder filtro = new StringBuilder();
		
		if(_record.getValue("client_id")!=null && !_record.getValue("client_id").equals("todos")){
			client_id = _record.getValue("client_id");
			filtro.append(" and INFI_TB_201_CTES.client_id = ").append(client_id);
		}
		if(_record.getValue("titulo_id")!=null && !_record.getValue("titulo_id").equals("todos")){
			titulo_id=_record.getValue("titulo_id");
			filtro.append(" and infi_tb_705_titulos_bloq_hist.titulo_id='").append(titulo_id).append("'");
		}
		
		if((_record.getValue("fe_blo_desde")!=null) && (_record.getValue("fe_blo_hasta")!=null)){
			fe_blo_desde =_record.getValue("fe_blo_desde");
			fe_blo_hasta = _record.getValue("fe_blo_hasta");
			filtro.append(" and trunc(infi_tb_705_titulos_bloq_hist.fecha) between to_date('").append(fe_blo_desde);
			filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fe_blo_hasta);
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
		parametrosJasper.put("fecha_desde",_record.getValue("fe_blo_desde"));
		parametrosJasper.put("fecha_hasta",_record.getValue("fe_blo_hasta") );
		parametrosJasper.put("p_fecha_sistema",fecha_sistema);
		System.out.println(fecha_sistema);
		
		/* La ruta al archivo .jasper */
		String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "titulosBloqueados.jasper";
		System.out.println("1");
		/* Cargar el archivo .jasper */
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
		System.out.println("2");
		/* Llenar el reporte con los datos */
		JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
		System.out.println("3");
		/* Establecer el contenido de la p&aacute;gina como pdf */
		_res.setContentType("application/pdf");
		System.out.println("4");
		/* Embeber el reporte en la p&aacute;gina */
		JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
		System.out.println("5");
	}
}
}
