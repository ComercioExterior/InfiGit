package models.consulta_auditoria;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {
	
	public void execute() throws Exception{
		
		Calendar fechaHoy = Calendar.getInstance();
		Calendar dToday=new GregorianCalendar();
		dToday.add(Calendar.DATE, -5); //se le restan 5 días a la fecha actual
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		String haceCincoDias = sdIO.format(dToday.getTime());
		DataSet dsFecha = new DataSet();		
		dsFecha.append("hace_cinco_dias",java.sql.Types.VARCHAR);
		dsFecha.append("hoy",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("hace_cinco_dias",haceCincoDias);
		dsFecha.setValue("hoy",hoy);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsFechas", dsFecha);	
		
	}
}
