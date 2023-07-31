package models.exportable;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import megasoft.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;

public abstract class ExportableJasper extends Exportable {

	ServletOutputStream os = null;
		
	private long inicioProceso = 0;
		
	/**
	 * Obtiene el OutputStreamy marca el inicio del proceso
	 * Registra el inicio del proceso de exportación a excel
	 * @throws Exception en caso de error
	 */
	protected void registrarInicio(String nombre) throws Exception{
        inicioProceso=System.currentTimeMillis();
		Logger.info(this, "Inicio del proceso de exportacion de reportes jasper ");
		_res.addHeader("Content-Disposition", "attachment;filename=" + nombre);
		_res.setContentType("application/x-download");		
		os = _res.getOutputStream();
	}
	
		
	
	/**
	 * Registra el fin del proceso de exportación a excel
	 * @throws Exception 
	 */
	protected void registrarFin() throws Exception{
		Logger.info(this, "Fin del proceso de exportacion de reporte jasper ");
		Logger.info(this, "Tiempo total de exportacion: " + ((System.currentTimeMillis()-inicioProceso)/1000) + " segundos");
		inicioProceso = 0;
	}
	
	/**
	 * Envia los datos hacia el explorador.
	 * @rutaArchivo ruta absoluta donde se escribe el archivo
	 * @throws Exception en caso de error
	 */
	protected void obtenerSalida(String rutaArchivo) throws Exception{
		FileInputStream file = new FileInputStream(rutaArchivo);
		ServletOutputStream os = _res.getOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = file.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
		eliminarArchivoTemporal(rutaArchivo);
		os.flush();
	}
	
	/**
	 * Borra el archivo creado
	 */
	protected void eliminarArchivoTemporal(String rutaArchivo){
		Logger.debug(this, "Eliminando " + rutaArchivo);
		File file = new File(rutaArchivo);
		file.delete();
	}
	
	
	/**
	 * Arma la ruta completa donde se debe crear el archivo en base al nombre deseado
	 * @param nombre nombre del archivo
	 * @throws Exception en caso de error
	 */
	protected String obtenerNombreArchivo(String nombre) throws Exception{
		return (nombre + getFechaHora() + ".pdf");
	}
	
	/**
	 * Arma la ruta completa donde se debe crear el archivo en base al nombre deseado
	 * @param nombre nombre del archivo
	 * @throws Exception en caso de error
	 */
	protected String obtenerNombreArchivo(String nombre, String extension) throws Exception{
		return (nombre + getFechaHora() + extension);
	}

	/**
	 * LLena el reporte usando una conexión activa
	 * @param reporteCompilado reporte compilado que debe llenar
	 * @param parametros parámetros con que llenar el reporte
	 * @return objeto JasperPrint 
	 * @throws Exception
	 */
	protected JasperPrint llenarReporte(JasperReport reporteCompilado, Map parametros) throws Exception{
		JasperPrint jasperPrint = null;
		Connection conn = null;
		try {
			conn = _dso.getConnection();
			jasperPrint = JasperFillManager.fillReport(reporteCompilado, parametros, conn);
		} catch (Exception e) {
			Logger.error(this, "Error en el llenado del reporte ",e);
			throw e;
		} finally{
			if (conn!=null){
				conn.close();
			}
		}
		return jasperPrint;
	}
	
	/**
	* Obtiene el archivo .jasper
	 * @param nombreReporte ruta completo de donde se encuentra el archivo compilado .jasper
	 * @param parametros parámetros que se envían al reporte
	 * @return Objeto JasperReport
	 * @throws Exception en caso de error
	 */
	protected JasperReport getReporteCompilado(String nombreReporte,Map parametros) throws Exception {
		JRFileVirtualizer virtualizer = new JRFileVirtualizer (200, getRutaTemporal());
		virtualizer.setReadOnly(true);
		parametros.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		File reportFile = new File(nombreReporte);
	
		// If compiled file is not found, then
		// compile XML template
//		if (!reportFile.exists()) {
//			JasperCompileManager.compileReportToFile(_app.getRealPath(folder + reportName + ".jrxml"));
//		}
	
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());
		return jasperReport;
	}
	
	/**
	 * Exporta el documento a HTML
	 * @param rutaArchivo ruta final donde estará el archivo
	 * @param jasperPrint
	 * @throws JRException
	 */
	protected void exportHTML(String rutaArchivo, JasperPrint jasperPrint) throws JRException{
		long inicio = System.currentTimeMillis();
		JasperExportManager.exportReportToHtmlFile(jasperPrint, rutaArchivo);
		tiempoDeExportacion(inicio, "HTML");
	}

	/**
	 * Exporta el documento a XML
	 * @param rutaArchivo rutaArchivo ruta final donde estará el archivo
	 * @param jasperPrint
	 * @throws JRException
	 */
	protected void exportXML(String rutaArchivo, JasperPrint jasperPrint) throws JRException{
		long inicio = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile(jasperPrint, rutaArchivo, false);
		tiempoDeExportacion(inicio, "XML");
	}

	/**
	 * Exporta el archivo a PDF
	 * @param rutaArchivo rutaArchivo ruta final donde estará el archivo
	 * @param jasperPrint
	 * @throws JRException
	 */
	protected void exportPDF(String rutaArchivo, JasperPrint jasperPrint) throws JRException{
		long inicio = System.currentTimeMillis();
		JasperExportManager.exportReportToPdfFile(jasperPrint, rutaArchivo);
		tiempoDeExportacion(inicio, "PDF");
	}
	
	/**
	 * Imprime el tiempo que se demora la exportación seleccionada
	 * @param tiempo tiempo inicial
	 */
	private void tiempoDeExportacion(long tiempo, String tipo){
		Logger.debug(this,"Tiempo de exportacion " + tipo);
		Logger.debug(this, (System.currentTimeMillis() - tiempo) / 1000 + " segundos" );
	}
}