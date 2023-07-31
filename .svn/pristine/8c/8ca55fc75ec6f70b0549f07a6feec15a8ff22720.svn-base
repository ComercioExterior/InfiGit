package models.custodia.consulta_comisiones;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import megasoft.AbstractModel;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import java.util.regex.Pattern;

public class View extends AbstractModel {

	
	public void execute() throws Exception {
	
		
		
		String client_id = null;
		String calculo_mes_id = null;
		String usuario_id = null;
		String imagen    = ConstantesGenerales.IMAGEN_BDV;
		String separador = String.valueOf(File.separatorChar);
		String directorio= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador;
		
		/* Par&aacute;metros que le pasamos al jasper */
		Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		parametrosJasper.put("p_titulo_reporte", "Reporte de Vencimientos para Intereses y/o Capital");
		parametrosJasper.put("p_fecha_corte", new Date());
		parametrosJasper.put("fechas", new Date());
		parametrosJasper.put("fecha_hasta", new Date());
		
		/* Consulta a base de datos */
		StringBuilder filtro = new StringBuilder();
		
		if(_record.getValue("calculo_mes_id")!=null){
			calculo_mes_id = _record.getValue("calculo_mes_id");
			filtro.append(" AND INFI_TB_814_CALC_MES.CALCULO_MES_ID= ").append(calculo_mes_id);
		}
		
		if(_record.getValue("usuario_id")!=null){
			usuario_id = _record.getValue("usuario_id");
			filtro.append(" AND INFI_TB_814_CALC_MES.USUARIO_ID= ").append(usuario_id);
		}
		
		if(_record.getValue("client_id")!=null&& !_record.getValue("client_id").equals("todos")){
			client_id = _record.getValue("client_id");
			filtro.append(" and INFI_TB_201_CTES.client_id= ").append(client_id);
		}
		
		
		Pattern patron = Pattern.compile("\\\\");
		String rutaImagen = _app.getRealPath("images");
		String ruta[] = rutaImagen.split(String.valueOf(patron));
		String rutal = "";
		for(int i=0;i<ruta.length;i++){
			rutal = rutal + ruta[i] + separador;
		}
		rutal += imagen;
		parametrosJasper.put("p_ruta_absoluta", rutal);
		parametrosJasper.put("p_query_filtro", filtro.toString());
		parametrosJasper.put("fechas",_record.getValue("fecha_desde"));
		parametrosJasper.put("fecha_hasta",_record.getValue("fecha_hasta"));
		parametrosJasper.put("p_ruta_subReporte", directorio);
		parametrosJasper.put("p_moneda_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));
		
		//String[] retorno = new String("Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado");			
		/* La ruta al archivo .jasper */
		
		String archivoJasper = directorio + "cierreMesCupones.jasper";
		//String archivoJasperss = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "cierreMesCupones.jasper";
		
		
		/* Cargar el archivo .jasper */
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
		
		/* Llenar el reporte con los datos */
		JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
		
		/* Establecer el contenido de la p&aacute;gina como pdf */
		_res.setContentType("application/pdf");
		
		/* Embeber el reporte en la p&aacute;gina */
		JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
		
		 if(_record.getValue("transa_id").equals(TransaccionNegocio.CUSTODIA_COMISIONES)){
		
			 /* parametros que se le pasa al jasper*/
				parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
				parametrosJasper.put("p_titulo_reporte", "Aviso de Cobro");
				parametrosJasper.put("p_fecha_corte", new Date());
				String archivoJaspers = directorio + "cierreMes.jasper";
				
				/* Cargar el archivo .jasper */
				JasperReport jasperReports = (JasperReport) JRLoader.loadObject(archivoJaspers);
				
				/* Llenar el reporte con los datos */
				JasperPrint reports = JasperFillManager.fillReport(jasperReports, parametrosJasper, _dso.getConnection());
				
				/* Establecer el contenido de la p&aacute;gina como pdf */
				_res.setContentType("application/pdf");
				
				/* Embeber el reporte en la p&aacute;gina */
				JasperExportManager.exportReportToPdfStream(reports,_res.getOutputStream());
		 }		
		 else if(_record.getValue("transa_id").equals(TransaccionNegocio.CUSTODIA_AMORTIZACION))
				{
					
					 /* parametros que se le pasa al jasper*/
						parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
						parametrosJasper.put("p_titulo_reporte", "Pago de Amortización");
						parametrosJasper.put("p_fecha_corte", new Date());
						String archivoJasperss = directorio + "cierreMesAmortizacion.jasper";
						
						/* Cargar el archivo .jasper */
						JasperReport jasperReportss = (JasperReport) JRLoader.loadObject(archivoJasperss);
						
						/* Llenar el reporte con los datos */
						JasperPrint reportss = JasperFillManager.fillReport(jasperReportss, parametrosJasper, _dso.getConnection());
						
						/* Establecer el contenido de la p&aacute;gina como pdf */
						_res.setContentType("application/pdf");
						
						/* Embeber el reporte en la p&aacute;gina */
						JasperExportManager.exportReportToPdfStream(reportss,_res.getOutputStream());
				}
}
		 
}




