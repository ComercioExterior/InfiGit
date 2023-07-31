package models.bcv.intervencion_operacion_consulta;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.enterprisedt.util.debug.Logger;

public class Browse extends MSCModelExtend {

	private Logger logger = Logger.getLogger(Browse.class);

	private DataSet datosFilter = new DataSet();
	private DataSet _intervenciones = new DataSet();
	private DataSet _jornada = new DataSet();
	private String fecha = "";
	private String estatusEnvio = "";
	IntervencionDAO intervencionDao;

	public void execute() throws Exception {
		iniciar_datos_sql();
		llenarVariables();
		llenarDataSet();
		operacionesIntervencionBancaria();
		publicarInformacion();

	}

	public String[] campos_datos() {
		String Campos[] = { "fecha", "statusE", "boton_procesar" };
		return Campos;
	}

	public void iniciar_datos_sql() {
		String Campos[] = campos_datos();
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			datosFilter.append(Campos[i], java.sql.Types.VARCHAR);
		}
	}

	public void llenarVariables() {
		try {
			estatusEnvio = _record.getValue("statusE");
			fecha = (String) _record.getValue("fecha");

		} catch (Exception e) {
			logger.error("Browse : llenarVariables()" + e);
			System.out.println("Browse : llenarVariables()" + e);

		}
	}

	private void llenarDataSet() {
		try {
			datosFilter.addNew();
			datosFilter.setValue("fecha", fecha);
			datosFilter.setValue("statusE", estatusEnvio);
			if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)) {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			} else if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_TODAS) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL)) {
				datosFilter.setValue("boton_procesar", " <button id='btnbloquear' name='btnProcesar' onclick='bloquearlotodo()'>Procesar</button>&nbsp;");
			} else {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
			}
		} catch (Exception e) {
			logger.error("Browse : llenarDataSet()" + e);
			System.out.println("Browse : llenarDataSet()" + e);

		}

	}

	private void operacionesIntervencionBancaria() {
		try {
			this.intervencionDao = new IntervencionDAO(_dso);
			intervencionDao.listarInterbancario(getNumeroDePagina(), getPageSize(), fecha, estatusEnvio);
			_intervenciones = intervencionDao.getDataSet();

		} catch (Exception e) {
			logger.error("Browse : operacionesIntervencionBancaria()" + e);
			System.out.println("Browse : operacionesIntervencionBancaria()" + e);

		}
	}

	private void publicarInformacion() {
		try {
			storeDataSet("datos", intervencionDao.getTotalRegistros());
			storeDataSet("rows", _intervenciones);
			storeDataSet("datosFilter", datosFilter);
			storeDataSet("jornada", _jornada);
			getSeccionPaginacion(intervencionDao.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

		} catch (Exception e) {
			logger.error("Browse : publicarInformacion()" + e);
			System.out.println("Browse : publicarInformacion()" + e);

		}
	}
}