package models.bcv.menudeo_conciliacion;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * segunda vista del producto menudeo, muestra un dataGrid con la informacion de BD
 * 
 * @author nm36635
 * 
 */
public class Browse extends MSCModelExtend {
	DataSet _ordenes;
	private DataSet datosFilter;
	Integer clienteID = null;
	Integer incluirCliente = null;
	Double montoOperacion;
	Integer cantidadOperaciones;
	OrdenesCrucesDAO ordenesCrucesDAO;
	String statusE = null;
	String fecha = null;
	int estatus_n;

	public void execute() throws Exception {

		this._ordenes = new DataSet();
		capturarValoresRecord();
		datosFilter = new DataSet();
		crearVaribaleDatosFilter();
		setearValoresDatosFilter();

		// SE CONSULTAN LOS REGISTROS
		this.ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesAnuladasMenudeo(fecha, statusE, getNumeroDePagina(), getPageSize());
		_ordenes = ordenesCrucesDAO.getDataSet();
		this.montoOperacion = new Double(0);
		this.cantidadOperaciones = getCantidadOperaciones();
		
		try {
			datosFilter.setValue("monto_operacion", String.valueOf(montoOperacion));
			datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
			publicarDataSet();
			getSeccionPaginacion(ordenesCrucesDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
			
		} catch (Exception e) {
			System.out.println("Browse Conciliacion Consulta: execute()" + e);
			Logger.error(this, "Browse Conciliacion Consulta: execute()" + e);

		}
	}

	/**
	 * captura los valores que viene de la primera vista Nota: en el archivo record tiene que tener las variables declarada de la primera vista asociado al "name" de los campo HTML
	 * 
	 * @throws Exception
	 */
	public void capturarValoresRecord() {
		try {
			fecha = (String) _record.getValue("fecha");
			statusE = _record.getValue("statusE");

		} catch (Exception e) {
			System.out.println("Browse Conciliacion Consulta: capturarValoresRecord() " + e);
			Logger.error(this, "Browse Conciliacion Consulta: capturarValoresRecord() " + e);
		}

	}

	/**
	 * Metodo para crear las variables del dataSet
	 */
	public void crearVaribaleDatosFilter() {
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
	}

	/**
	 * Metodo par setear los valores a las variables creadas del data set datosfilter
	 * 
	 * @throws Exception
	 */
	public void setearValoresDatosFilter() {
		try {
			datosFilter.addNew();
			datosFilter.setValue("statusE", statusE);
			datosFilter.setValue("fecha", fecha);
			
			if (statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			} else if (statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_TODAS) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA)) {
				datosFilter.setValue("boton_procesar", " <button id='btnbloquear' name='btnProcesar' onclick='bloquearlotodo()'>Procesar</button>&nbsp;");
			} else {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
			}

		} catch (Exception e) {
			System.out.println("Browse Conciliacion Consulta: setearValoresDatosFilter() " + e);
			Logger.error(this, "Browse Conciliacion Consulta: setearValoresDatosFilter() " + e);

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
	 * publica la informacion de los dataSet para poder mostrar en la vista actual
	 * 
	 * @throws Exception
	 */
	public void publicarDataSet() {
		try {
			storeDataSet("rows", _ordenes);
			storeDataSet("datosFilter", datosFilter);
			storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(false));

		} catch (Exception e) {
			System.out.println("Browse Conciliacion Consulta: publicarDataSet() " + e);
			Logger.error(this, "Browse Conciliacion Consulta: publicarDataSet() " + e);
			
		}
	}

	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;

		try {
			clienteID = Integer.parseInt(_record.getValue("client_id") == null ? "0" : _record.getValue("client_id"));

		} catch (NumberFormatException e) {
			_record.addError("Error: ", "Debe Introducir un número valido");
			return false;
		}

		return valido;
	}
}