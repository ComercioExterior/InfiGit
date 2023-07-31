package models.bcv.menudeo_envio;

import models.msc_utilitys.MSCModelExtend;
import megasoft.DataSet;
import com.bdv.infi.dao.OrdenesCrucesDAO;


public class Detalle extends MSCModelExtend {
	private DataSet datosFilter;
	DataSet _ordenes;
	Integer clienteID = null;
	String statusP = null;
	String statusE = null;
	String Tipo = null;
	String combustiblee = null;
	String fecha = null;
	Integer cantidadOperaciones;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		this._ordenes = new DataSet();
		capturarValoresRecord();
		this.datosFilter = new DataSet();
		crearVaribaleDatosFilter();
		setearValoresDatosFilter();
		
		//SE CONSULTAN LOS TOTALES
		OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(true, false, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,combustiblee);
		_ordenes = ordenesCrucesDAO.getDataSet();
		this.cantidadOperaciones= getCantidadOperaciones();

		storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(true));
		storeDataSet("rows", _ordenes);	
			
		_record.setValue("statusp",statusP);
		_record.setValue("statusE",statusE);
		_record.setValue("combustiblee",combustiblee);
		_record.setValue("Tipo",Tipo);
		_record.setValue("fecha",fecha);
			
		storeDataSet("record", _record);
		storeDataSet("datosFilter", datosFilter);

		getSeccionPaginacion(ordenesCrucesDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

		
	}
	
/**
* retorna la cantida de operaciones que viene de base de datos
* @return
*/
	public int getCantidadOperaciones(){
		return _ordenes.count();
	}

/**
 * captura las variables de la segunda vista a una 3era vista y se captura con _req
 */
	public void capturarValoresRecord(){
		this.statusP = _req.getParameter("statusp");
		this.statusE = _req.getParameter("statusE");
		this.Tipo = _req.getParameter("Tipo");
		this.fecha = _req.getParameter("fecha");
		this.combustiblee = _req.getParameter("combustiblee");
		System.out.println("combustibleeeeee . " + combustiblee);
	}
	

/**
 * Metodo para crear las variables del dataSet
 */
	public void crearVaribaleDatosFilter(){
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("combustiblee", java.sql.Types.VARCHAR);
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
		datosFilter.setValue("combustiblee", combustiblee);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
	}

}