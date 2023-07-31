package models.custodia.informes.cierre_mes;

import java.io.File;
import com.bdv.infi.util.FileUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import megasoft.AbstractModel;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import java.util.regex.Pattern;

public class View extends MSCModelExtend{

	
	public void Cobros(Date fechaDesde,Date fechaHasta, int idUsuario, int idCliente) throws Exception {
		String client_id = null;
		String valor_mes = null;
		String valor_anio = null;
		String mes=null;
		//String nombre_mes=null;
		String anio=null;
		String imagen    = ConstantesGenerales.IMAGEN_BDV;
		String separador = String.valueOf(File.separatorChar);
		//String mesActual=Util.getMonth();
		//String diaActual=Util.getDay();
		//String mesNActual=Util.getMonthName(_record.getValue("mes"));
		//String anoActual=Util.getYear();
		
		/* Par&aacute;metros que le pasamos al jasper */
		Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
		parametrosJasper.put("p_titulo_reporte", "Cobros");
		parametrosJasper.put("p_fecha_corte", new Date());
		parametrosJasper.put("p_mes", new Date());
		
		/* Consulta a base de datos */
		StringBuilder filtro = new StringBuilder();
		if ((idCliente)!=0){
			filtro.append(" and INFI_TB_815_CALC_MES_DETALLE.client_id =").append(idCliente);
		}
		if ((idUsuario)!=0){
			filtro.append(" and INFI_TB_814_CALC_MES.usuario_id= ").append(idUsuario);
		}
		
		
		Pattern patron = Pattern.compile("\\\\");
		String rutaImagen = FileUtil.getRootApplicationPath()+("images");
		String ruta[] = rutaImagen.split(String.valueOf(patron));
		String rutal = "";
		for(int i=0;i<ruta.length;i++){
			rutal = rutal + ruta[i] + "/";
		}
		rutal += imagen;
		parametrosJasper.put("p_mes", mes);
		parametrosJasper.put("p_ruta_absoluta", rutal);
		parametrosJasper.put("p_query_filtro", filtro.toString());
		//parametrosJasper.put("mes", _record.getValue("mes"));
		//parametrosJasper.put("nombre_mes", mesNActual);
		//parametrosJasper.put("dia",diaActual);
		//parametrosJasper.put("anio",anio);
		

		/*if (_record.getValue("anio")==null){
			parametrosJasper.put("anio", anoActual);
		}else{
			parametrosJasper.put("anio", _record.getValue("anio"));
		}*/

		//String[] retorno = new String("Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado");
		
		/* La ruta al archivo .jasper */
		String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "avisoCobro.jasper";
		
		/* Cargar el archivo .jasper */
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
		
		/* Llenar el reporte con los datos */
		JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
		
		/* Establecer el contenido de la p&aacute;gina como pdf */
		_res.setContentType("application/pdf");
		
		/* Embeber el reporte en la p&aacute;gina */
		JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
	}
}
