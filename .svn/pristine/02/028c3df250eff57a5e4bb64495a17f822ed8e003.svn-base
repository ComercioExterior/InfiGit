package models.eventos.generacion_comisiones.browse;

import com.bdv.infi.dao.FechasCierresDAO;
import com.bdv.infi.data.FechasCierre;
import java.util.Calendar;
import java.util.GregorianCalendar;
import megasoft.DataSet;
import com.bdv.infi.util.Utilitario;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de la construcci&oacute;n del filtro de busqueda 
 * 
 *
 */
public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception 
	{
		FechasCierresDAO fechaCierreDAO= new FechasCierresDAO(_dso);
		FechasCierre fechasCierre= fechaCierreDAO.obtenerFechas();
		GregorianCalendar gregorianCalendar= new GregorianCalendar();
		gregorianCalendar.setTime(fechasCierre.getFechaCierreProximo());

		 //creacion del dataset
		DataSet _mes=new DataSet();
		_mes.append("meses", java.sql.Types.VARCHAR);
		_mes.append("numero_mes",java.sql.Types.VARCHAR);
		_mes.append("ano", java.sql.Types.VARCHAR);
		_mes.append("mensaje", java.sql.Types.VARCHAR);

		_mes.addNew();
		
		_mes.setValue("meses", Utilitario.nombreMes((gregorianCalendar.get(Calendar.MONTH)+1)));
		_mes.setValue("numero_mes",String.valueOf(gregorianCalendar.get(Calendar.MONTH)+1));
		_mes.setValue("ano", String.valueOf(gregorianCalendar.get(Calendar.YEAR)));
		
		gregorianCalendar.add(Calendar.DATE,15);
		_mes.setValue("mensaje", "Los montos generados ser&aacute;n cobrados el d&iacute;a " + gregorianCalendar.get(Calendar.DATE) + " del mes de " + Utilitario.nombreMes(gregorianCalendar.get(Calendar.MONTH)+1) + " de " + gregorianCalendar.get(Calendar.YEAR));
		
		
		//Se publica el Dataset exportado por este modelo 
		storeDataSet("mes",_mes);

	}//fin del excute

}//fin de la clase
