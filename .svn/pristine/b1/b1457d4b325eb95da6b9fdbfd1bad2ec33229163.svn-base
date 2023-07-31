package models.custodia.informes.bloqueo_garantias;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import megasoft.AbstractModel;
import megasoft.Util;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class View extends AbstractModel {

	
	public void execute() throws Exception {
		String client_id = null;
		String tipblo_id = null;
		String imagen = ConstantesGenerales.IMAGEN_BDV;
		String separador = String.valueOf(File.separatorChar);
		
		String mesActual=Util.getMonth();
		String diaActual=Util.getDay();
		String mesNActual=Util.getMonthName(mesActual);
		String anoActual=Util.getYear();
		
	//	Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
	//	SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		//String fecha_funcion = formatter.format(fecha1);
	//	int fecha_sistema= Integer.parseInt(fecha_funcion);
	//	System.out.println(fecha_sistema);
	//	System.out.println(fecha_funcion);
		
		/* Par&aacute;metros que le pasamos al jasper */
		Map<String, String> parametrosJasper = new HashMap<String, String>();
		//parametrosJasper.put("p_fecha_sistema", fecha_funcion);
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		
		
		/* Consulta a base de datos */
		StringBuilder filtro = new StringBuilder();
		
		
		
		if(_record.getValue("client_id")!=null && !_record.getValue("client_id").equals("todos")){
			client_id = _record.getValue("client_id");
			filtro.append(" and tb.client_id = ").append(client_id);
		}
		
		if(_record.getValue("tipblo_id")!=null){
			tipblo_id= _record.getValue("tipblo_id");
			filtro.append(" and tb.tipblo_id= '").append(tipblo_id).append("'");
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
		parametrosJasper.put("mes", mesActual);
		parametrosJasper.put("anio", anoActual);
		parametrosJasper.put("dia",diaActual);
		parametrosJasper.put("nombre_mes", mesNActual);
		parametrosJasper.put("p_custodia_obs1", ParametrosDAO.listarParametros(ParametrosSistema.CUSTODIA_OBS1, this._dso));
		parametrosJasper.put("p_custodia_obs2", ParametrosDAO.listarParametros(ParametrosSistema.CUSTODIA_OBS2, this._dso));
		
		//String[] retorno = new String("Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado");
		
		/* La ruta al archivo .jasper */
		String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "bloqueoGarantias.jasper";
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
