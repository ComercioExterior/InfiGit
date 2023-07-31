package models.custodia.informes.pago_cheque;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import megasoft.AbstractModel;
import models.custodia.informes.pago_cheque.PagoChequeExportarExcel;
//import megasoft.Util;
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
			PagoChequeExportarExcel excel = new PagoChequeExportarExcel(_record.getValue("client_id"),_record.getValue("num_cheque"),_record.getValue("fecha_desde"),_record.getValue("fecha_hasta"),_dso,_app,_res);
			excel.execute();
		}else{
		String client_id = null;
		String num_cheque  = null;
		String fecha_desde = null;
		String fecha_hasta = null;
		
		
		String imagen    = ConstantesGenerales.IMAGEN_BDV;
		String separador = String.valueOf(File.separatorChar);
		
		
		/* Par&aacute;metros que le pasamos al jasper */
		Map<String, Comparable> parametrosJasper = new HashMap<String, Comparable>();
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		parametrosJasper.put("p_titulo_reporte", "Pagos Emitidos");
		parametrosJasper.put("p_fecha_corte", new Date());

		
		/* Consulta a base de datos */
		StringBuilder filtro = new StringBuilder();
		
		if(_record.getValue("client_id")!=null&& !_record.getValue("client_id").equals("todos")){
			client_id = _record.getValue("client_id");
			filtro.append(" and INFI_TB_201_CTES.client_id= ").append(client_id);
		}
		
		if(_record.getValue("num_cheque")!=null&& !_record.getValue("num_cheque").equals("")){
			num_cheque = _record.getValue("num_cheque");
			filtro.append(" and INFI_TB_207_ORDENES_OPERACION.CHEQUE_NUMERO= '").append(num_cheque).append("'");
		}
		
		if((_record.getValue("fecha_desde")!=null) && (_record.getValue("fecha_hasta")!=null)){
			fecha_desde =_record.getValue("fecha_desde");
			fecha_hasta = _record.getValue("fecha_hasta");
			filtro.append(" and INFI_TB_207_ORDENES_OPERACION.FECHA_PAGO_CHEQUE between to_date('").append(fecha_desde);
			filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fecha_hasta);
			filtro.append("', '").append(ConstantesGenerales.FORMATO_FECHA + "')");
		
		}
		
		Pattern patron = Pattern.compile("\\\\");
		String rutaImagen = _app.getRealPath("images");
		String ruta[] = rutaImagen.split(String.valueOf(patron));
		String rutal = "";
		for(int i=0;i<ruta.length;i++)
		{
			rutal = rutal + ruta[i] + separador;
		}
		rutal += imagen;
		
		parametrosJasper.put("p_ruta_absoluta", rutal);
		parametrosJasper.put("p_query_filtro_1", filtro.toString());
		
		//String[] retorno = new String("Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado");
		
		/* La ruta al archivo .jasper */
		String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "pagoCheque.jasper";
		
		/* Cargar el archivo .jasper */
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
		
		/* Llenar el reporte con los datos */
		JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
		
		_res.setContentType("application/pdf");
		
		/* Embeber el reporte en la p&aacute;gina */
		JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
	}
}
}
