package models.exportable;

import java.math.BigDecimal;
import java.util.Date;

import megasoft.Logger;

import com.bdv.infi.util.Utilitario;

public abstract class ExportableBufferString extends Exportable {

	public StringBuilder sbArchivo = new StringBuilder();		
	protected long inicioProceso = 0;
	protected long tiempoEjecucion =0;
		
	/**
	 * Registra el inicio del proceso de exportación a excel
	 * @throws Exception en caso de error
	 */
	protected void registrarInicio(String nombre) throws Exception{
		Logger.info(this,"Fin de exportación de ordenes "+(System.currentTimeMillis()-tiempoEjecucion));
        inicioProceso=System.currentTimeMillis();
	}
	
	/**
	 * Agrega el valor String al buffer
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	public void escribir(String valor) throws Exception{
		sbArchivo.append(valor!=null&&!valor.equalsIgnoreCase("null")?valor:"");
	}
	
	/**
	 * Agrega el valor BigDecimal al buffer
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	protected void escribir(BigDecimal valor) throws Exception{
		sbArchivo.append(getNumero(valor.toString()));
	}	
	
	/**
	 * Agrega el valor String con longitud fija al buffer
	 * @param valor valor a escribir
	 * @param longitud longitud máxima que debe abarcar el valor
	 * @throws Exception en caso de error
	 */
	protected void escribir(String valor, int longitud) throws Exception{
		sbArchivo.append(Utilitario.rellenarCaracteres(getString(valor).toString(),' ',longitud,true));
	}	
	
	/**
	 * Agrega el valor double al buffer
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	public void escribir(double valor) throws Exception{		
		sbArchivo.append(getNumero(valor).toString());
	}

	/**
	 * Agrega el valor double con longitud fija al buffer
	 * @param valor valor a escribir
	 * @param longitud longitud máxima que debe abarcar el valor
	 * @throws Exception en caso de error
	 * */
	protected void escribir(double valor, int longitud) throws Exception{
		sbArchivo.append(Utilitario.rellenarCaracteres(getNumero(valor),' ',longitud,false));
	}	
	
	/**
	 * Agrega el valor long al buffer
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	protected void escribir(long valor) throws Exception{
		sbArchivo.append(getNumero(valor).toString());
	}
	
	/**
	 * Agrega el valor Date al buffer
	 * @param valor valor a escribir
	 * @throws Exception en caso de error
	 */
	protected void escribir(Date valor) throws Exception{
		sbArchivo.append(Utilitario.DateToString(valor, "dd/MM/yyyy"));
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
		tiempoEjecucion=System.currentTimeMillis();
		Logger.info(this,"Inicio de exportación de ordenes "+tiempoEjecucion);
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