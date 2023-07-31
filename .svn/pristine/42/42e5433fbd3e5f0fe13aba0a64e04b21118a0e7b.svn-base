package models.custodia.informes.recibo_custodias;
import java.io.File;
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

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import java.util.regex.Pattern;
/**
 * Clase encargada de exportar en  pdf el reporte de recibo de custodia.
 * @author bag
 *
 */
public class View extends AbstractModel {
	
	public void execute() throws Exception {
		
		try {
			/* declaracion de variables*/
			System.out.println("paso1");
			String client_id = null;
			String titulo_id = null;
			String fecha_hasta = null;
			String imagen    = ConstantesGenerales.IMAGEN_BDV;
			String separador = String.valueOf(File.separatorChar);
			String mesActual=Util.getMonth();
			String diaActual=Util.getDay();
			String mesNActual=Util.getMonthName(mesActual);
			System.out.println("paso2");
			String anoActual=Util.getYear();
			MSCModelExtend me = new MSCModelExtend();
			CustodiaDAO custodiaDao = new CustodiaDAO(_dso);
			ClienteDAO clienteDao = new ClienteDAO(_dso);
			System.out.println("paso3");
			/* Par&aacute;metros que le pasamos al jasper */
			Map<String, String> parametrosJasper = new HashMap<String, String>();
			parametrosJasper.put("p_moneda", "no");		
			parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
			System.out.println("paso4");
			parametrosJasper.put("p_fecha_corte", me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA));
			parametrosJasper.put("p_custodia_obs1", ParametrosDAO.listarParametros(ParametrosSistema.CUSTODIA_OBS1, this._dso));
			parametrosJasper.put("p_custodia_obs2", ParametrosDAO.listarParametros(ParametrosSistema.CUSTODIA_OBS2, this._dso));		
			/* Consulta a base de datos */
			System.out.println("paso5");
			StringBuilder filtro = new StringBuilder();
				
			if(_record.getValue("client_id")!=null && !_record.getValue("client_id").equals("todos")){
				client_id = _record.getValue("client_id");
				System.out.println("paso6");
				filtro.append(" and b.client_id = ").append(client_id);
			}
			
			if(_record.getValue("titulo_id")!=null){
				titulo_id = _record.getValue("titulo_id");
				filtro.append(" and a.titulo_id = '").append(titulo_id).append("'");
			}
			System.out.println("paso7");
			if((_record.getValue("fecha_hasta")!=null)){
				parametrosJasper.put("p_fecha_corte", _record.getValue("fecha_hasta"));
				fecha_hasta =_record.getValue("fecha_hasta");
				parametrosJasper.put("p_fechaCierre", " to_date('" + fecha_hasta+"', '"+ConstantesGenerales.FORMATO_FECHA + "')");		
			}
			
			/* Parametros que se le pasa  al jarper*/
			Pattern patron = Pattern.compile("\\\\");
			String rutaImagen = _app.getRealPath("images");
			String ruta[] = rutaImagen.split(String.valueOf(patron));
			String rutal = "";
			for(int i=0;i<ruta.length;i++){
				rutal = rutal + ruta[i] + "/";
			}
			rutal += imagen;
			System.out.println("paso8");
			clienteDao.buscarDatosPorIdCliente(client_id);
			if(clienteDao.getDataSet().count()>0){
				clienteDao.getDataSet().first();
				clienteDao.getDataSet().next();					
				parametrosJasper.put("p_client_nombre", clienteDao.getDataSet().getValue("CLIENT_NOMBRE"));
				parametrosJasper.put("p_client_cedrif", clienteDao.getDataSet().getValue("TIPPER_ID")+"-"+clienteDao.getDataSet().getValue("CLIENT_CEDRIF"));
			
			}
			System.out.println("paso9");
			// Parametros que se le pasa  al jasper
			parametrosJasper.put("p_ruta_absoluta", rutal);			
			parametrosJasper.put("mes", mesActual);
			parametrosJasper.put("nombre_mes", mesNActual);
			parametrosJasper.put("anio", anoActual);
			parametrosJasper.put("dia",diaActual);			
			parametrosJasper.put("p_query",custodiaDao.consultaCertificadoCustodia(client_id, titulo_id, fecha_hasta,_record.getValue("moneda_local")!=null?_record.getValue("moneda_local").equals("si"):true));
			System.out.println("paso10");
			// La ruta al archivo .jasper 
			String archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "reciboCustodias.jasper";
			
			// Cargar el archivo .jasper 
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
			
			// Llenar el reporte con los datos 
			JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
			
			// Establecer el contenido de la p&aacute;gina como pdf 
			_res.setContentType("application/pdf");
			System.out.println("paso11");
			// Embeber el reporte en la p&aacute;gina
			JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}/* fin de la excute*/
}/* fin de la clase*/
