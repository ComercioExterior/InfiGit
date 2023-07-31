package models.liquidacion.instrucciones_venta_titulos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.*;

public class InstruccionesVentaTitulosBrowseSitme extends MSCModelExtend {

	public void execute()throws Exception {
		TransaccionDAO transDAO=new TransaccionDAO(_dso); 
		transDAO.listarTransaccionesPorId(TRANSACCION_PACTO_RECOMPRA,TRANSACCION_VENTA_TITULOS);
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listarUnidadInversionParaVentaTitClavenet();
		
		storeDataSet("transaid",transDAO.getDataSet());
		storeDataSet("unidadInversion",confiD.getDataSet());
		
		Calendar fechaHoy = Calendar.getInstance();
		Calendar dToday=new GregorianCalendar();
		dToday.add(Calendar.DATE, -5); //se le restan 5 días a la fecha actual
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		String haceCincoDias = sdIO.format(dToday.getTime());
		
		DataSet dsFecha = new DataSet();		
		dsFecha.append("filtroFechaEmisionDesde",java.sql.Types.VARCHAR);
		dsFecha.append("filtroFechaEmisionHasta",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("filtroFechaEmisionDesde",haceCincoDias);
		dsFecha.setValue("filtroFechaEmisionHasta",hoy);
		
		storeDataSet("dsFechas", dsFecha);	
	}
}
