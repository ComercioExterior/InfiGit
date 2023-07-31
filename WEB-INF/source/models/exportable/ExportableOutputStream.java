package models.exportable;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.ServletOutputStream;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import megasoft.Logger;

import com.bdv.infi.util.Utilitario;

public abstract class ExportableOutputStream extends Exportable {

	ServletOutputStream os = null;
		
	protected long inicioProceso = 0;
		
	/**
	 * Obtiene el OutputStreamy marca el inicio del proceso
	 * Registra el inicio del proceso de exportación a excel
	 * @throws Exception en caso de error
	 */
	protected void registrarInicio(String nombre) throws Exception{
        inicioProceso=System.currentTimeMillis();
		Logger.info(this, "Inicio del proceso de exportacion por OutputStream ");
		_res.addHeader("Content-Disposition", "attachment;filename=" + nombre);
		_res.setContentType("application/x-download");		
		os = _res.getOutputStream();
	}
	
	/**
	 * Escribe en el OutputStream el valor indicado
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	protected void escribir(String valor) throws Exception{
		os.write(getString(valor).toString().getBytes());
	}
	
	protected void escribir(String valor,int longitudInicial,int longitudFinal) throws Exception{
		os.write(getString(valor).substring(longitudInicial, longitudFinal).toString().getBytes());
	}
	
	protected void escribir(BigDecimal valor) throws Exception{
		os.write(getNumero(valor.toString()).getBytes());
	}	
	
	/**
	 * Escribe en el OutputStream el valor indicado pero con un espacio definido
	 * @param valor valor a escribir
	 * @param longitud longitud máxima que debe abarcar el valor
	 * @throws Exception en caso de error
	 */
	protected void escribir(String valor, int longitud) throws Exception{
		os.write(Utilitario.rellenarCaracteres(getString(valor).toString(),' ',longitud,true).getBytes());
	}
		
	
	/**
	 * Escribe en el OutputStream el valor indicado
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	protected void escribir(double valor) throws Exception{
		os.write(getNumero(valor).toString().getBytes());
	}

	/**
	 * Escribe en el OutputStream el valor indicado pero con un espacio definido
	 * @param valor valor a escribir
	 * @param longitud longitud máxima que debe abarcar el valor
	 * @throws Exception en caso de error
	 * */
	protected void escribir(double valor, int longitud) throws Exception{
		os.write(Utilitario.rellenarCaracteres(getNumero(valor),' ',longitud,false).getBytes());
	}	
	
	/**
	 * Escribe en el OutputStream el valor indicado
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	protected void escribir(long valor) throws Exception{
		os.write(getNumero(valor).toString().getBytes());
	}	
	
	protected void escribir(Date valor) throws Exception{
		os.write(Utilitario.DateToString(valor, "dd/MM/yyyy").getBytes());
	}
	
	/**
	 * Registra el fin del proceso de exportación a excel
	 * @throws Exception 
	 */
	protected void registrarFin() throws Exception{
		Logger.info(this, "Fin del proceso de exportacion por OutputStream ");
		Logger.info(this, "Tiempo total de exportacion: " + ((System.currentTimeMillis()-inicioProceso)/1000) + " segundos");
		inicioProceso = 0;
	}
	
	/**
	 * Envia los datos hacia el explorador
	 * @throws Exception en caso de error
	 */
	protected void obtenerSalida() throws Exception{
		os.flush();
	}
	
	/**
	 * Arma la ruta completa donde se debe crear el archivo en base al nombre deseado
	 * @param nombre nombre del archivo
	 * @throws Exception en caso de error
	 */
	protected String obtenerNombreArchivo(String nombre) throws Exception{
		return (nombre + getFechaHora() +  ".csv");
	}
}