package models.custodia.informes.transacciones_liquidadas;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import megasoft.AbstractModel;
import models.custodia.informes.transacciones_liquidadas.TransaccionesLiquidadasExportarExcel;
import models.msc_utilitys.MSCModelExtend;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
//import megasoft.Util;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import java.util.regex.Pattern;

public class View extends MSCModelExtend {
	
	public void execute() throws Exception {
		
	
		if (_record.getValue("formato_salida").equals("excel"))
		{
			TransaccionesLiquidadasExportarExcel excel = new TransaccionesLiquidadasExportarExcel (_record.getValue("client_id"),_record.getValue("transa_id"),_record.getValue("fecha_orden_desde"),_record.getValue("fecha_orden_hasta"),_dso,_app,_res);
			excel.execute();
		}else{
		
			String transa_id 			= null;
			String client_id 			= null;
			String fecha_orden_desde	= null;
			String fecha_orden_hasta	= null;
			String clienteParametro		= "";
			String imagen 				= ConstantesGenerales.IMAGEN_BDV;
			String separador 			= String.valueOf(File.separatorChar);
			
			
			/* Par&aacute;metros que le pasamos al jasper */
			Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
			parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
			parametrosJasper.put("p_aplicacion_descripcion", "Venta masiva de títulos");
			parametrosJasper.put("p_titulo_reporte", "Transacciones Liquidadas");
			parametrosJasper.put("p_fecha_corte", new Date());
			parametrosJasper.put("fechas", new Date());
			parametrosJasper.put("fecha_hasta", new Date());
			
			/* Consulta a base de datos */
			StringBuilder filtro = new StringBuilder();
			
			if(_record.getValue("client_id")!=null && !_record.getValue("client_id").equals("todos")){
				client_id = _record.getValue("client_id");
				filtro.append(" and INFI_TB_201_CTES.CLIENT_ID = ").append(client_id);
				
			}
			//Si el cliente es diferente de null se le asigna al parametro para ser reemplazado en el query
			clienteParametro=client_id!=null?"and INFI_TB_201_CTES.CLIENT_ID="+client_id:"";
			
			if(_record.getValue("transa_id")!=null && !_record.getValue("transa_id").equals("todos")){
				transa_id= _record.getValue("transa_id");
				filtro.append(" and INFI_TB_204_ORDENES.TRANSA_ID = '").append(transa_id).append("'");
			}
			
			if((_record.getValue("fecha_orden_desde")!=null) && (_record.getValue("fecha_orden_hasta")!=null)){
				fecha_orden_desde =_record.getValue("fecha_orden_desde");
				fecha_orden_hasta = _record.getValue("fecha_orden_hasta");
				filtro.append(" and INFI_TB_204_ORDENES.ORDENE_PED_FE_VALOR between to_date('").append(fecha_orden_desde);
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

			parametrosJasper.put("p_ruta_absoluta", rutal);
			parametrosJasper.put("p_query_filtro_1", filtro.toString() + " order by INFI_TB_201_CTES.CLIENT_NOMBRE,INFI_TB_204_ORDENES.ORDENE_PED_FE_VALOR");
			parametrosJasper.put("p_cliente", clienteParametro);
		
			parametrosJasper.put("fechas", _record.getValue("fecha_orden_desde"));
			parametrosJasper.put("fecha_hasta", _record.getValue("fecha_orden_hasta"));
			
			
			/* La ruta al archivo .jasper */
			String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "transaccionesLiquidadas.jasper";
			
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

