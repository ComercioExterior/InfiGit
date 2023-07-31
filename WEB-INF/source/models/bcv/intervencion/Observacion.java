package models.bcv.intervencion;

import com.bdv.infi.dao.IntervencionDAO;
import models.msc_utilitys.MSCModelExtend;
import megasoft.DataSet;

public class Observacion extends MSCModelExtend {
	private DataSet datosFilter = new DataSet();
	DataSet _ordenes = new DataSet();
	Integer clienteID = null;
	String estatusEnvio = null;
	String fecha = null;
	Integer cantidadOperaciones;

	public void execute() {
		capturarValores();
		iniciar_datos_sql();
		setearValoresDatosFilter();

		IntervencionDAO intervencionDao = new IntervencionDAO(_dso);
		intervencionDao.listarOrdenesReporte(fecha, estatusEnvio,"TODAS");
		_ordenes = intervencionDao.getDataSet();
		this.cantidadOperaciones = getCantidadOperaciones();

		try {
			storeDataSet("datos", intervencionDao.getTotalRegistros(true));
			storeDataSet("rows", _ordenes);
			_record.setValue("statusE", estatusEnvio);
			_record.setValue("fecha", fecha);
			storeDataSet("record", _record);
			storeDataSet("datosFilter", datosFilter);

			getSeccionPaginacion(intervencionDao.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

		} catch (Exception e) {
			System.out.println("Observacion : execute() " + e);

		}

	}

	public String[] campos_datos() {
		String Campos[] = { "statusp", "statusE", "Tipo", "fecha", "cantidad_operaciones", "monto_operacion", "moneda", "cliente_id" };
		return Campos;
	}

	public void iniciar_datos_sql() {
		String Campos[] = campos_datos();
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			datosFilter.append(Campos[i], java.sql.Types.VARCHAR);
		}
	}

	/**
	 * retorna la cantida de operaciones que viene de base de datos
	 * 
	 * @return
	 */
	public int getCantidadOperaciones() {
		return _ordenes.count();
	}

	/**
	 * captura las variables de la segunda vista a una 3era vista y se captura con _req
	 */
	public void capturarValores() {
		this.estatusEnvio = _req.getParameter("statusE");
		this.fecha = _req.getParameter("fecha");
	}

	/**
	 * Metodo par setear los valores a las variables creadas del data set datosfilter
	 * 
	 * @throws Exception
	 */
	public void setearValoresDatosFilter() {
		try {
			datosFilter.addNew();
			datosFilter.setValue("statusE", estatusEnvio);
			datosFilter.setValue("fecha", fecha);
			datosFilter.setValue("cliente_id", String.valueOf(clienteID));

		} catch (Exception e) {
			System.out.println("Observacion : setearValoresDatosFilter() " + e);

		}

	}

}