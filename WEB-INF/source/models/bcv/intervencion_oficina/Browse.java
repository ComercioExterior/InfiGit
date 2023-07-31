package models.bcv.intervencion_oficina;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.model.inventariodivisas.Oficina;
import com.enterprisedt.util.debug.Logger;

public class Browse extends MSCModelExtend {

	private Logger logger = Logger.getLogger(Browse.class);
	private DataSet datosFilter = new DataSet();
	private DataSet _ordenes = new DataSet();
	private String fecha = "";
	private String moneda = "";
	private String estado = "";
	Oficina ofi;

	public void execute() {
		iniciar_datos_sql();
		llenarDataSet();
		detalleOficinaprueba();
		publicarInformacion();
	}

	public String[] campos_datos() {
		String Campos[] = { "fecha", "moneda", "estado", "boton_procesar" };
		return Campos;
	}

	public void iniciar_datos_sql() {
		String Campos[] = campos_datos();
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			datosFilter.append(Campos[i], java.sql.Types.VARCHAR);
		}
	}

	private void llenarDataSet() {
		capturarValorFilter();

		try {
			datosFilter.addNew();
			datosFilter.setValue("fecha", fecha);
			datosFilter.setValue("moneda", moneda);
			datosFilter.setValue("estado", estado);
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");

		} catch (Exception e) {
			logger.error("Browse : llenarDataSet()" + e);
			System.out.println("Browse : llenarDataSet()" + e);

		}
	}

	private void capturarValorFilter() {

		try {
			this.moneda = _record.getValue("moneda");
			fecha = (String) _record.getValue("fecha");
			estado = _record.getValue("estado");
			System.out.println("estado : " + estado);

		} catch (Exception e) {
			logger.error("Browse : llenarDataSet()" + e);
			System.out.println("Browse : llenarDataSet()" + e);

		}
	}

	private void detalleOficinaprueba() {

		try {
			this.ofi = new Oficina(_dso);
			ofi.Fecha = fecha;
			ofi.Moneda = this.moneda;
			System.out.println("this.estado : " + this.estado);
			if (this.estado != null) {
				ofi.Estado = this.estado;
			}
			ofi.Estatus = "";
			ofi.PaginaAMostrar = getNumeroDePagina();
			ofi.RegistroPorPagina = getPageSize();
			ofi.Listar();

			_ordenes = ofi.getDataSet();

		} catch (Exception e) {
			logger.error("Browse : detalleOficinas()" + e);
			System.out.println("Browse : detalleOficinas()" + e);

		}
	}

	private void publicarInformacion() {

		try {
			Integer cantidadOperaciones = _ordenes.count();
			System.out.println("cantidadOperaciones : " + cantidadOperaciones);
			storeDataSet("datos", ofi.getTotalRegistros());
			storeDataSet("rows", _ordenes);
			storeDataSet("datosFilter", datosFilter);
			getSeccionPaginacion(ofi.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

		} catch (Exception e) {
			logger.error("Browse : publicarInformacion()" + e);
			System.out.println("Browse : publicarInformacion()" + e);

		}
	}

}