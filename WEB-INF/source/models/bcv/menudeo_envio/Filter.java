package models.bcv.menudeo_envio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;


/**
 * Vista principal para el manejo de menudeo
 * @author nm36635
 *
 */
public class Filter extends MSCModelExtend {

Calendar fechaHoy;
SimpleDateFormat sdIO;
String hoy;
DataSet dsFecha;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
	
		capturarFecha();
		
		this.dsFecha = new DataSet();		
		dsFecha.append("fechahoy",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechahoy",hoy);
		
		storeDataSet("fechas", dsFecha);
		
		
	}

	
/**
 * captura la fecha del dia
 * @return
 */
	public String capturarFecha(){
		
		this.fechaHoy = Calendar.getInstance();
		this.sdIO= new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		this.hoy = sdIO.format(fechaHoy.getTime());
		
		return hoy;
	}
}
