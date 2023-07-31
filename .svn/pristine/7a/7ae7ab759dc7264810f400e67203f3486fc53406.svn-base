package models.custodia.informes.posicion_global;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import megasoft.Logger;
import models.exportable.ExportableJasper;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

/**
 * Clase encargada de exportar a Excel y a Pdf el Reporte de Posicion Global el cual puede ser generado para Valor Nominal o Valor de Mercado.
 * 
 *
 */
public class View extends ExportableJasper {

	String nombreArchivo = "";
	
	/**
	 * Ejecución del modelo
	 */
	public void execute() throws Exception {
		try {
			
			
			SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			//SimpleDateFormat formateador = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);//nm36635
			//formateador.format(sdf);//nm36635
			String titulo_id = "";
			String tipoProducto = "";
			
			if(_record.getValue("tipo_producto_id")!=null){ 
				tipoProducto =_record.getValue("tipo_producto_id");
			}
			
			if(_record.getValue("titulo_id")!=null){ 
				titulo_id =_record.getValue("titulo_id");
			}				
			
			if(_record.getValue("formato").equalsIgnoreCase("csv")){
				long cliente   = 0;
				String fecha   = null;
				//String fecha1  = null;//nm36635
					
				if(_record.getValue("client_id")!=null)
					cliente = Long.parseLong(_record.getValue("client_id"));
				if(_record.getValue("fe_em_hasta")!=null){ 
					fecha =_req.getParameter("fe_em_hasta");
				//	fecha1 =formateador.format(fecha); //nm36635
				}
				
				String idioma = _record.getValue("idioma");
				String reporte = _record.getValue("reporte");
			
				//Carga el reporte
				PosicionGlobalExportarCSV posicionGlobalExcel = new PosicionGlobalExportarCSV(cliente,titulo_id,tipoProducto,fecha,_dso,_app,_res,idioma,reporte);
				System.out.println("excelprueba");
				posicionGlobalExcel.execute();
			}else
				if(_record.getValue("formato").equalsIgnoreCase("excel")){
					long cliente   = 0;
					String fecha   = null;
					//String fecha1  = null;//nm36635
						
					if(_record.getValue("client_id")!=null)
						cliente = Long.parseLong(_record.getValue("client_id"));
					if(_record.getValue("fe_em_hasta")!=null){ 
						fecha =_req.getParameter("fe_em_hasta");
				//		fecha1 =formateador.format(fecha); //nm36635
					}
					
					String rutaTemplates = "";
					String separadores = String.valueOf(File.separatorChar);			
				
					/*
					 * Se ultiliza la Plantilla en Ingles para Valor de Mercado
					 * */
					if (_record.getValue("idioma").equals("ingles")){
						if(_record.getValue("reporte").equals("valorMercado")){
							rutaTemplates = this._app.getRealPath("WEB-INF") + separadores + "templates" + separadores + "informes" + separadores + "custodia" + separadores +"excel"+separadores+"PosicionGlobalMercadoIngles.xls";
						} else{
							rutaTemplates = this._app.getRealPath("WEB-INF") + separadores + "templates" + separadores + "informes" + separadores + "custodia"+separadores+"excel"+separadores+"PosicionGlobalIngles.xls";
						}
					} else{
						//Español
						if(_record.getValue("reporte").equals("valorMercado")){
							rutaTemplates = this._app.getRealPath("WEB-INF") + separadores + "templates" + separadores + "informes" + separadores + "custodia" + separadores +"excel"+separadores+"PosicionGlobalMercado.xls";
						}else{
							rutaTemplates = this._app.getRealPath("WEB-INF") + separadores + "templates" + separadores + "informes" + separadores + "custodia"+separadores+"excel"+separadores+"PosicionGlobal.xls";	
						}
					}
					//Carga el reporte
					PosicionGlobalExportarExcel posicionGlobalExcel = new PosicionGlobalExportarExcel(cliente,titulo_id,tipoProducto,fecha,_dso,_app,_res,rutaTemplates);
					System.out.println("excelllll");
					posicionGlobalExcel.execute();
				}
			//fin del if
			else{
				System.out.println("PDF");
				nombreArchivo = obtenerNombreArchivo("posicionGlobal");
				registrarInicio(nombreArchivo);
				String fe_em_hasta_sistema = null;
				SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
				fe_em_hasta_sistema = _req.getParameter("fe_em_hasta");
				String fecha_reme = fe_em_hasta_sistema;
				Date fecha_remediacion = null;
				fecha_remediacion = formato_fecha.parse(fecha_reme);
				SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
				String fecha_funcion = formatter.format(fecha_remediacion);
				int fecha_sistema= Integer.parseInt(fecha_funcion);
				System.out.println(fecha_sistema);
				
				
				/* Desclaracion de variables*/
				String client_id = null;
				String fe_em_hasta = null;
				
				
				String clienteParametro="";
				String imagen = ConstantesGenerales.IMAGEN_BDV;
				String separador = String.valueOf(File.separatorChar);
				String archivoJasper = "";
				
				/* Par&aacute;metros que le pasamos al jasper */
				Map<String, Object> parametrosJasper = new HashMap<String, Object>();
				parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
				parametrosJasper.put("p_titulo_reporte", "Consolidado de Posición en Custodia");
				parametrosJasper.put("p_tipoDocumento", "nominal");
				parametrosJasper.put("p_query_orderBy", " order by client_nombre, titulo_moneda_den");
				
				/* Consulta a base de datos */
				StringBuilder filtro = new StringBuilder();
				
				if(_record.getValue("client_id")!=null && !_record.getValue("client_id").equals("todos")){
					client_id = _record.getValue("client_id");
				}
				
//			Si el cliente es diferente de null se le asigna al parametro para ser reemplazado en el query
				clienteParametro=client_id!=null?" and b.client_id="+client_id:"";
				
				if(_record.getValue("fe_em_hasta")!=null){
					
					fe_em_hasta = _req.getParameter("fe_em_hasta");
					filtro.append(" to_date('").append(fe_em_hasta);
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
				/* parametros se le pasa al jasper*/
				parametrosJasper.put("p_fecha_sistema", fecha_funcion.toString());
				parametrosJasper.put("p_ruta_absoluta", rutal);
				parametrosJasper.put("p_fechaCierre", filtro.toString());
				parametrosJasper.put("p_fecha_cierre_sf", fe_em_hasta);
				
				parametrosJasper.put("p_cliente", clienteParametro);
				
				if(!tipoProducto.equals("")){
					parametrosJasper.put("p_tipoProducto", " and a.tipo_producto_id='" +  tipoProducto + "'");
					parametrosJasper.put("p_tipoProducto_bloq", " and bloq.tipo_producto_id='" +  tipoProducto + "'");
				} else{
					parametrosJasper.put("p_tipoProducto", " ");
					parametrosJasper.put("p_tipoProducto_bloq", " ");
				}			
				
				if(!titulo_id.equals("")){
					parametrosJasper.put("p_titulo", " and a.titulo_id='" +  titulo_id + "'");
					parametrosJasper.put("p_titulo_bloq", " and bloq.titulo_id='" +  titulo_id + "'");
				} else{
					parametrosJasper.put("p_titulo", " ");
					parametrosJasper.put("p_titulo_bloq", " ");
				}
				
				parametrosJasper.put("p_fecha_corte", sdf.parse(fe_em_hasta));
				parametrosJasper.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle("i18n.i18n",new Locale("es","VE"))); 
							
				/* La ruta al archivo .jasper */
				archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "posicionGlobal.jasper";			
				/*  Se utiliza la plantilla en ingles del reporte valor nominal 
				 * */
				 if(_record.getValue("idioma").equals("ingles")){
					/* parametros que se le pasa al jasper*/
					parametrosJasper.put("p_titulo_reporte", "Statement of Holdings");
					parametrosJasper.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle("i18n.i18n",new Locale("en","US")));				
					//archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "posicionGlobal.jasper";
					
					 if(_record.getValue("reporte").equals("valorMercado")){
						parametrosJasper.put("p_tipoDocumento", "mercado");
						//archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "posicionGlobal.jasper";					 
					 }
			     } else{
			    	 if(_record.getValue("reporte").equals("valorMercado")) {
						parametrosJasper.put("p_tipoDocumento", "mercado");		    		 
					    //archivoJasper = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia" + separador + "posicionGlobal.jasper";				    
			    	 }
			     }
				 System.out.println("SQL PDF Filtro"+filtro);
				 System.out.println("SQL PDF "+parametrosJasper);
				 generatePDFOutput(archivoJasper, parametrosJasper);
				 
//				/* Cargar el archivo .jasper */
//				JasperReport jasperReports = (JasperReport) JRLoader.loadObject(archivoJasper);	
//				/* Llenar el reporte con los datos */
//				JasperPrint reports = JasperFillManager.fillReport(jasperReports, parametrosJasper, _dso.getConnection());			
////				/* Establecer el contenido de la p&aacute;gina como pdf */
//				_res.setContentType("application/pdf");			
//				/* Embeber el reporte en la p&aacute;gina */
//				JasperExportManager.exportReportToPdfStream(reports,_res.getOutputStream());
			}//fin del else	
		} catch (Exception e) {
			Logger.error(this, "Error generando el reporte",e);
			throw new Exception("Error generando el reporte"+e);
		}
	}/*Fin del execute*/
	
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
}/* Fin de la Clase*/
