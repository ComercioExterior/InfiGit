package models.exportable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public abstract class ExportableArchivo extends MSCModelExtend {

	private DecimalFormat nf = new DecimalFormat("###,###.00");
	
	/**Indica el número de registro procesado*/	
	public int registroProcesado = 0;
	
	/**Ruta de creación del archivo temporal*/
	
	public String rutaTemplateArchivo = "";
	
	/**Nombre del archivo que debe ser mostrado al usuario*/
	public String nombreArchivo = "";
	
	
	private long inicioProceso = 0;
	
	
	/**
	 * Crea la cabecera del archivo
	 * @param sb StringBuilder original donde se debe crear la cabecera
	 */
	protected abstract void crearCabecera(StringBuilder sb);

	/**
	 * Escribe a un archivo según la ruta especificada
	 * @throws IOException en caso de error
	 */
	protected void escribirAArchivo(StringBuilder sb, boolean agregacion) throws IOException {
		FileUtil.crearArchivo(sb, rutaTemplateArchivo, agregacion);
	}
	
	/**
	 * Obtiene la fecha y hora para que sea incorporada en el nombre del archivo
	 * @return fecha y hora concatenada
	 * @throws ParseException 
	 */
	protected String getFechaHora() throws ParseException{		
		return Utilitario.DateToString(new Date(), "ddMMyyyyhhmmss");
	}
	
	/**
	 * Verifica si el registro procesado es múltiplo de 5000 para hacer un vaciado temporal de la información al archivo
	 * @param rutaTemplateArchivo ruta donde se debe escribir el contenido procesado
	 * @throws IOException en caso de error
	 */
	protected void verificarRegistros(StringBuilder sb) throws Exception{
		if (registroProcesado % 5000 == 0) {
			escribirAArchivo(sb, true);
			sb.setLength(0);
		}
	}

	/**
	 * Registra el inicio del proceso de exportación a excel
	 */
	protected void registrarInicio(){
        inicioProceso=System.currentTimeMillis();
		Logger.info(this, "Inicio del proceso de exportacion a excel a ruta " + rutaTemplateArchivo);		
	}
	
	/**
	 * Registra el fin del proceso de exportación a excel
	 */
	protected void registrarFin(){
		Logger.info(this, "Fin del proceso de exportacion a excel a ruta " + rutaTemplateArchivo);
		Logger.info(this, "Tiempo total de exportacion: " + ((System.currentTimeMillis()-inicioProceso)/1000) + " segundos");
		inicioProceso = 0;
	}

	/**
	 * Borra el archivo creado
	 */
	protected void eliminarArchivoTemporal(){
		Logger.debug(this, "Eliminando " + rutaTemplateArchivo);
		File file = new File(rutaTemplateArchivo);
		file.delete();
	}
	
	/**
	 * Obtiene la salida hacia el explorador
	 * @throws Exception en caso de error
	 */
	protected void obtenerSalida() throws Exception{
		FileInputStream file = new FileInputStream(rutaTemplateArchivo);
		ServletOutputStream os = _res.getOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = file.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
		os.flush();
		file.close();
	}
	
	/**
	 * Arma la ruta completa donde se debe crear el archivo en base al nombre deseado
	 * @param nombre nombre del archivo
	 * @throws Exception en caso de error
	 */
	protected void obtenerNombreArchivo(String nombre) throws Exception{
		String separador = String.valueOf(File.separatorChar);
		nombreArchivo = nombre + getFechaHora() +  ".csv";
		rutaTemplateArchivo =  _app.getRealPath("WEB-INF") + separador + "tmp" + separador + nombreArchivo;		
	}
	
	protected String getString(String valor){
		return valor==null?"":valor;
	}
	
	protected String getNumero(double numero){
		return String.valueOf(nf.format(numero));
	}
	
	protected String getNumero(long numero){
		return String.valueOf(nf.format(numero));
	}
	
	protected String getNumero(String numero){
		return String.valueOf(nf.format(Double.parseDouble(numero)));
	}
}