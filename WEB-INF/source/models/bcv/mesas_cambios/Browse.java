package models.bcv.mesas_cambios;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.mesaCambio.Jornada;

/**
 * segunda vista del producto menudeo, muestra un dataGrid con la informacion de BD
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
	String statusP = null;
	String statusE = null;
	String fecha = null;
	String Tipo = null;
	String nacionalidad = null;
	String cedulaRif = null;
	int tipoMovimiento = 0;
	String Status;
	int estatus_n;

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
		operaciones.Listar(true, getNumeroDePagina(), getPageSize(), statusP, fecha, statusE, Tipo, nacionalidad, cedulaRif, tipoMovimiento);
		_ordenes = operaciones.getDataSet();
		operaciones.Listar(statusP, fecha, statusE, Tipo, nacionalidad, cedulaRif);
		_cantidad = operaciones.getDataSet();
		_cantidad.next();
		
//		operaciones.ListarCantidadOferta(statusP, fecha, statusE, Tipo, nacionalidad, cedulaRif);
//		operaciones.ListarCantidadDemanda(statusP, fecha, statusE, Tipo, nacionalidad, cedulaRif);
		this.montoOperacion = new Double(0);
		this.cantidadOperaciones = getCantidadOperaciones();
		Jornada jornada = new Jornada();
		String jrd = jornada.jornadaActiva();
		String estatusJornada;
		System.out.println("Jornadaaaaa : " + jrd);
		if (jrd.equalsIgnoreCase("")) {
			jrd = "No existe jornada";
			estatusJornada = "Inactiva";
		} else {
			estatusJornada = jornada.estatusJornada(jrd);
		}

		datosFilter.setValue("monto_operacion", String.valueOf(_cantidad.getValue("cant")));
		datosFilter.setValue("cantidad_operaciones", String.valueOf(_cantidad.getValue("cant")));
		datosFilter.setValue("total", String.valueOf(_cantidad.getValue("total")));
		datosFilter.setValue("jornada", jrd);
		datosFilter.setValue("estatus_jornada", estatusJornada);
		publicarDataSet();
		getSeccionPaginacion(operaciones.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

	}

	/**
	 * captura los valores que viene de la primera vista Nota: en el archivo record tiene que tener las variables declarada de la primera vista asociado al "name" de los campo HTML
	 * 
	 * @throws Exception
	 */
	public void capturarValoresRecord() throws Exception {
		statusP = "";//_record.getValue("statusP");
		fecha = (String) _record.getValue("fecha");
		statusE = _record.getValue("statusE");
		Tipo = _record.getValue("Tipo") == null ? "" : _record.getValue("Tipo");
		nacionalidad = _record.getValue("nacionalidad");
		cedulaRif = _record.getValue("rif");
		tipoMovimiento = Integer.parseInt(_record.getValue("tipomovimiento"));
		System.out.println("tipoMovimiento : " + tipoMovimiento);
		if (cedulaRif == null) {
			cedulaRif = "";
		}
		if (nacionalidad == null) {
			nacionalidad = "";
		}

	}

	/**
	 * Metodo para crear las variables del dataSet
	 */
	public void crearVaribaleDatosFilter() {
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("total", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("jornada", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("estatus_jornada", java.sql.Types.VARCHAR);
		datosFilter.append("nacionalidad", java.sql.Types.VARCHAR);
		datosFilter.append("rif", java.sql.Types.VARCHAR);
	}

	/**
	 * Metodo par setear los valores a las variables creadas del data set datosfilter
	 * 
	 * @throws Exception
	 */
	public void setearValoresDatosFilter() throws Exception {

		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("nacionalidad", nacionalidad);
		datosFilter.setValue("rif", cedulaRif);
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
		storeDataSet("datosFilter", datosFilter);
		storeDataSet("datos", operaciones.getTotalRegistros(false));
	}
}