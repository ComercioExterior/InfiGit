package models.bcv.mesas_cambios;

import models.msc_utilitys.MSCModelExtend;
import megasoft.DataSet;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Envio_Manual extends MSCModelExtend {
	private DataSet datosFilter;
	DataSet _ordenes;
	Integer clienteID = null;
	String statusP = null;
	String statusE = null;
	String Tipo = null;
	String fecha   = null;
	int cantidadOperaciones;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		this._ordenes = new DataSet();
		
		capturarValoresRecord();
		datosFilter = new DataSet();
		crearVaribaleDatosFilter();
		setearValoresDatosFilter();
		
		OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesParaEnviarmanualMenudeoBCV(false, true, getNumeroDePagina(),getPageSize(),true, statusP, "SYSDATE",ConstantesGenerales.ENVIO_MENUDEO_FALTANTES,Tipo,"");
		_ordenes = ordenesCrucesDAO.getDataSet();

		this.cantidadOperaciones=_ordenes.count();
		
		storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(true));
		storeDataSet("rows", _ordenes);	
			
		_record.setValue("statusp",statusP);
		_record.setValue("statusE",statusE);
		_record.setValue("Tipo",Tipo);
		_record.setValue("fecha",fecha);
		
		storeDataSet("record", _record);
		storeDataSet("datosFilter", datosFilter);
		
		getSeccionPaginacion(ordenesCrucesDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
	
/**
* @throws Exception 
* capturar los valores para la consulta a base de datos
*/
	public void capturarValoresRecord() throws Exception{
			
		this.statusP = _req.getParameter("statusp");
		this.statusE = _req.getParameter("statusE");
		this.Tipo = _req.getParameter("Tipo");
		this.fecha = _req.getParameter("fecha");
		
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
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un número valido");
			return false;
		}
			
		return valido;
	}
}