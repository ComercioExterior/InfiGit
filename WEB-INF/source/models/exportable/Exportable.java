package models.exportable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import megasoft.ActionConfig;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.util.Utilitario;

public abstract class Exportable extends MSCModelExtend {

	private DecimalFormat nf = new DecimalFormat("###,###.00######");
	private DecimalFormat nfsf = new DecimalFormat("######.########");
//	private DecimalFormat nfsff = new DecimalFormat("####################");
	protected boolean aplicarFormato = true;
	//private DecimalFormat nf = new DecimalFormat("###,###,###,###.00");
	//DecimalFormatSymbols decsim = DecimalFormatSymbols.getInstance();
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void init(ServletContext app, HttpServletRequest req, DataSource dso, DataSet record, ActionConfig config) throws Exception{
		//ejecutar init del AdstractModel
		super.init(app, req, dso, record, config);
		//Obtiene parametro del web.xml
		String strDataSource=app.getInitParameter("datasource");
		//Se genera un DataSource para Trabajar con la base de datos
		dsoAplic=db.getDataSource(strDataSource);
		//establecerSeparadores();
	}
	
	/**Indica el número de registro procesado*/	
	public int registroProcesado = 0;
	
	/**
	 * Obtiene la fecha y hora para que sea incorporada en el nombre del archivo
	 * @return fecha y hora concatenada
	 * @throws ParseException 
	 */
	protected String getFechaHora() throws ParseException{		
		return Utilitario.DateToString(new Date(), "ddMMyyyyhhmmss");
	}
	
	/**
	 * Arma la ruta completa donde se debe crear el archivo en base al nombre deseado
	 * @param nombre nombre del archivo
	 * @throws Exception en caso de error
	 */
	protected abstract String obtenerNombreArchivo(String nombre) throws Exception;
	
	/**
	 * Registra el inicio del proceso de exportación a excel
	 * @throws Exception en caso de error
	 */
	protected abstract void registrarInicio(String nombre) throws Exception;
	
	/**
	 * Establece el caracter de grupo y decimal para el formato de número
	 */
	/*protected void establecerSeparadores(){
		char decimal = ',';
		char grupo = '.';		
		Logger.debug(this, "Estableciendo caracteres de formato...");
		Logger.debug(this, "Caracter de grupo->" + grupo);
		Logger.debug(this, "Caracter de decimal->" + decimal);
		decsim.setDecimalSeparator(decimal);
		decsim.setGroupingSeparator(grupo);
		nf.setDecimalFormatSymbols(decsim);
	}*/
	
	/**
	 * Registra el fin del proceso de exportación a excel
	 * @throws Exception 
	 */
	protected abstract void registrarFin() throws Exception;
	
	protected String getString(String valor){
		return valor==null?"":valor;
	}
	
	protected String getNumero(double numero){
		return aplicarFormato?String.valueOf(nf.format(numero)):nfsf.format(numero);			
	}
	
	protected String getNumero(long numero){
		return aplicarFormato?String.valueOf(nf.format(numero)):nfsf.format(numero);
	}
	
	protected String getNumero(String numero){
		return aplicarFormato?String.valueOf(nf.format(Double.parseDouble(numero))):nfsf.format(numero);
	}
}