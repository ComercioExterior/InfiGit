package models.bcv.mesas_cambios_inter;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
/**
 * 
 *  muestra un dataGrid con la informacion de BD con las operaciones de interbancario de oferta, demanda y pacto (MESA DE CAMBIO)
 * 
 * @author nm36635
 * 
 */
public class Browse extends MSCModelExtend {
	DataSet _ordenes;
	DataSet _cantidad;
	DataSet _prueba;
	private DataSet datosFilter;
	Double montoOperacion;
	Integer cantidadOperaciones;
	MesaCambioDAO operaciones;
	// variables para valores del record
	String statusE = null;
	String fecha = null;
	String tipoMovimiento = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {

		this._ordenes = new DataSet();
		this._cantidad = new DataSet();
		capturarValoresRecord();
		datosFilter = new DataSet();
		_prueba = new DataSet();
		crearVaribaleDatosFilter();
		setearValoresDatosFilter();

		// SE CONSULTAN LOS REGISTROS
		operaciones = new MesaCambioDAO(_dso);
		if (tipoMovimiento.equalsIgnoreCase("D") || tipoMovimiento.equalsIgnoreCase("O")) {
			operaciones.ListarInter(true, getNumeroDePagina(), getPageSize(),fecha, statusE,tipoMovimiento);
		}else{
			operaciones.ListarPactos(true, getNumeroDePagina(), getPageSize(),fecha);
		}
		_ordenes = operaciones.getDataSet();
		this.montoOperacion = new Double(0);
		this.cantidadOperaciones = getCantidadOperaciones();
		publicarDataSet();
		getSeccionPaginacion(operaciones.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

	}

	/**
	 * captura los valores que viene de la primera vista Nota: en el archivo record tiene que tener las variables declarada de la primera vista asociado al "name" de los campo HTML
	 * 
	 * @throws Exception
	 */
	public void capturarValoresRecord() throws Exception {
		tipoMovimiento = _record.getValue("tipomovimiento");
		statusE = _record.getValue("statusE");
		fecha = (String) _record.getValue("fecha");

	}

	/**
	 * Metodo para crear las variables del dataSet
	 */
	public void crearVaribaleDatosFilter() {
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("tipomovimientoo", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("total", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("jornada", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("estatus_jornada", java.sql.Types.VARCHAR);
	}

	/**
	 * Metodo par setear los valores a las variables creadas del data set datosfilter
	 * 
	 * @throws Exception
	 */
	public void setearValoresDatosFilter() throws Exception {

		datosFilter.addNew();
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("tipomovimientoo", tipoMovimiento);
		if (statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
		} else if (statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_TODAS_NUMERICO) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA)) {
			datosFilter.setValue("boton_procesar", " <button id='btnbloquear' name='btnProcesar' onclick='bloquearlotodo()'>Procesar</button>&nbsp;");
		} else {
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
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
	public void publicarDataSet() throws Exception {

		storeDataSet("rows", _ordenes);
		storeDataSet("rowss", _ordenes);
		storeDataSet("datosFilter", datosFilter);
		storeDataSet("datos", operaciones.getTotalRegistros(false));
	}
}