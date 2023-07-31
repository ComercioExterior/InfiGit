package models.bcv.menudeo_conciliacion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.conciliacion.Conciliacion;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {

	Calendar fechaHoy;
	SimpleDateFormat sdIO;
	String hoy;
	DataSet dsFecha;

	public void execute() throws Exception {
		
		
		System.out.println("llegoooooo");

		Conciliacion ccn = new Conciliacion();
//		ccn.respaldarArchivo();
//		ccn.respaldarArchivo();
		capturarFecha();
		this.dsFecha = new DataSet();
		dsFecha.append("fechahoy", java.sql.Types.VARCHAR);

		try {
			dsFecha.addNew();
			dsFecha.setValue("fechahoy", hoy);
			storeDataSet("fechas", dsFecha);

		} catch (Exception e) {
			System.out.println("Filter : execute()" + e);
			Logger.error(this, "Filter : execute()" + e);

		}

	}

	public String capturarFecha() {

		this.fechaHoy = Calendar.getInstance();
		this.sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		this.hoy = sdIO.format(fechaHoy.getTime());

		return hoy;
	}

}
