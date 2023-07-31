package models.bcv.menudeo_bcv;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.menudeo.Menudeo;


/**
 * segunda vista del producto menudeo, muestra un dataGrid con la informacion de BD
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
	//variables para valores del record
	String statusP = null;
	String statusE = null;
	String fecha = null;
	String Tipo = null;
	String statusP1 = null;
	String statusE1 = null;
	String fecha1 = null;
	String Tipo1 = null;
	String Status;
	int estatus_n;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
//		Menudeo menu = new Menudeo(_dso);
//		menu.Iniciar();
		this._ordenes = new DataSet();
		capturarValoresRecord();
		datosFilter = new DataSet();
		crearVaribaleDatosFilter();
		setearValoresDatosFilter();
			
		//SE CONSULTAN LOS REGISTROS
		this.ordenesCrucesDAO= new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false,true, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
		_ordenes = ordenesCrucesDAO.getDataSet();
		this.montoOperacion = new Double(0);
		this.cantidadOperaciones = getCantidadOperaciones();
		System.out.println("montoOperacion-->"+String.valueOf(montoOperacion));
		System.out.println("cantidadOperaciones-->"+String.valueOf(cantidadOperaciones));
		datosFilter.setValue("monto_operacion",String.valueOf(montoOperacion));
		datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
		
		publicarDataSet();
		getSeccionPaginacion(ordenesCrucesDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

		}
	
/**
 * captura los valores que viene de la primera vista
 * Nota: en el archivo record tiene que tener las variables declarada de la primera vista asociado al "name" de los campo HTML
 * @throws Exception
 */
	public void capturarValoresRecord() throws Exception{
		statusP = _record.getValue("statusP");
		fecha = (String) _record.getValue("fecha");
		statusE = _record.getValue("statusE");
		Tipo = _record.getValue("Tipo") == null?"": _record.getValue("Tipo");

	}

/**
 * Metodo para crear las variables del dataSet
 */
	public void crearVaribaleDatosFilter(){
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("statusE", java.sql.Types.VARCHAR);
		datosFilter.append("Tipo", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("boton_detalle", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
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

		
		if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)){
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
		}else if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_TODAS) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA)){
			datosFilter.setValue("boton_procesar", " <button id='btnbloquear' name='btnProcesar' onclick='bloquearlotodo()'>Procesar</button>&nbsp;");		
		}else{
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
		}
	}
	

/**
 * retorna la cantida de operaciones que viene de base de datos
 * @return
 */
	public int getCantidadOperaciones(){
		return _ordenes.count();
	}
	
/**
 * publica la informacion de los dataSet para poder mostrar en la vista actual
 * @throws Exception
 */
	public void publicarDataSet() throws Exception{

		storeDataSet("rows", _ordenes);		
		storeDataSet("datosFilter", datosFilter);
		storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(false)/*false*/);
	}

	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
			clienteID = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));

			
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un número valido");
			return false;
		}
			
		return valido;
	}
}