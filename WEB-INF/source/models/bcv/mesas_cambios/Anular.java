package models.bcv.mesas_cambios;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Anular extends MSCModelExtend {
	private DataSet datosFilter;
	DataSet _ordenes;
	String statusP = null;
	String statusE = null;
	String Tipo = null;
	String fecha = null;
	String idOrdenes = null;
	String nacionalidad = null;
	String cedulaRif = null;
	Integer clienteID = null;
	Integer cantidadOperaciones;
	
/**
 * nm366635 datagrid para mandar anular operaciones enviadas a BCV
 */
	public void execute() throws Exception {
		this._ordenes = new DataSet();
		capturarValoresRecord();
		datosFilter = new DataSet();
		crearVaribaleDatosFilter();
		setearValoresDatosFilter();
		MesaCambioDAO operaciones = new MesaCambioDAO(_dso);
		operaciones.Listar(true, getNumeroDePagina(), getPageSize() ,statusP, fecha, statusE, Tipo, nacionalidad, cedulaRif,0);
		_ordenes = operaciones.getDataSet();
		this.cantidadOperaciones = 0;

		storeDataSet("datos", operaciones.getTotalRegistros(true));
		storeDataSet("rows", _ordenes);	
			
		_record.setValue("statusp",statusP);
		_record.setValue("statusE",statusE);
		_record.setValue("Tipo",Tipo);
		_record.setValue("fecha",fecha);
			
		storeDataSet("record", _record);
		storeDataSet("datosFilter", datosFilter);

		getSeccionPaginacion(operaciones.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
	
	
/**
* captura las variables de la segunda vista a una 3era vista y se captura con _req
*/
	public void capturarValoresRecord(){
		this.statusP = _req.getParameter("statusp");
		this.statusE = _req.getParameter("statusE");
		this.Tipo = _req.getParameter("Tipo");
		this.fecha = _req.getParameter("fecha");
		this.idOrdenes = _req.getParameter("idOrdenes");
		this.nacionalidad = _req.getParameter("nacionalidad");
		this.cedulaRif = _req.getParameter("rif");
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
	public void crearVaribaleDatosFilter(){
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("moneda", java.sql.Types.VARCHAR);
		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
	}
	
/**
* Metodo par setear los valores a las variables creadas del data set datosfilter
* @throws Exception
*/
	public void setearValoresDatosFilter() throws Exception{
		
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("statusE", statusE);
		datosFilter.setValue("Tipo", Tipo);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
		datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
	}
}