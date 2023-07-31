package models.bcv.mesa_cambio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {

	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
	
		Calendar fechaHoy = Calendar.getInstance();
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		
		DataSet dsFecha = new DataSet();		
		dsFecha.append("fechahoy",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechahoy",hoy);
		
		storeDataSet("fechas", dsFecha);
		
		
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		
		String urlInvocacion = _req.getPathInfo();

		return flag;
	}
}
