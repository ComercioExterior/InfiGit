package models.bcv.intervencion_oficina;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OficinaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Filter extends MSCModelExtend {
	Calendar fechaHoy;
	SimpleDateFormat sdIO;
	String hoy;
	DataSet dsFecha;

	public void execute() {

		capturarFecha();
		this.dsFecha = new DataSet();
		dsFecha.append("fechahoy", java.sql.Types.VARCHAR);
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		oficinaDAO.ListarEstado();
		
		try {
			dsFecha.addNew();
			dsFecha.setValue("fechahoy", hoy);
			storeDataSet("fechas", dsFecha);
			storeDataSet("estados", oficinaDAO.getDataSet());
			
		} catch (Exception e) {
			System.out.println("Filter : execute()" + e);
			Logger.error(this, "Filter : execute() " + e);
			
		}

	}

	public String capturarFecha() {
		try {
			this.fechaHoy = Calendar.getInstance();
			this.sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			this.hoy = sdIO.format(fechaHoy.getTime());
			
		} catch (Exception e) {
			System.out.println("Filter : capturarFecha() " + e);
			Logger.error(this, "Filter : capturarFecha() " + e);
			
		}
		return hoy;
	}

}
