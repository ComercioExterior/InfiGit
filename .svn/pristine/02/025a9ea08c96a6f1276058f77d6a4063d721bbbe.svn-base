package models.bcv.mesa_cambio_operaciones;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Filter extends MSCModelExtend {
	DataSet dsFecha = new DataSet();
	String hoy;

	public void execute() throws Exception {

		capturarFecha();
		publicarDatos();

	}

	public String capturarFecha() {
		try {
			Calendar fechaHoy = Calendar.getInstance();
			SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			this.hoy = sdIO.format(fechaHoy.getTime());

		} catch (Exception e) {
			System.out.println("Filter : capturarFecha()");

		}
		return hoy;
	}

	public void publicarDatos() {
		dsFecha.append("fechahoy", java.sql.Types.VARCHAR);

		try {
			dsFecha.addNew();
			dsFecha.setValue("fechahoy", hoy);
			storeDataSet("fechas", dsFecha);

		} catch (Exception e) {
			System.out.println("Filter : publicarDatos()");
			
		}
	}
		
}
