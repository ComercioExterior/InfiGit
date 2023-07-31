package models.bcv.menudeo_conciliacion;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.ConciliacionDAO;

/**
 * clase para general el excel
 * 
 * @author nm36635
 * 
 */
public class ExportarCVS extends ExportableOutputStream {
	DataSet _ordenes;
	String statusP = null;
	String statusE = null;
	String Tipo = null;
	String fecha = null;
	Integer clienteID;

	public void execute(){
		
		this._ordenes = new DataSet();
		capturarValoresRecord();
		ConciliacionDAO conc = new ConciliacionDAO(_dso);
		conc.listarOrdenesAnuladasMenudeo(fecha, "1");
		_ordenes = conc.getDataSet();

		try {
			
			registrarInicio("anuladas.csv");
			crearCabecera("ID OPERACION;CEDULA_RIF;PRODUCTO;FECHA;MONTO;ID RECHAZO;ESTATUS;ID");
			escribir("\r\n");

			while (_ordenes.next()) {
				registroProcesado++;
				escribir(_ordenes.getValue("ID_OPERACION") == null ? " ;" : _ordenes.getValue("ID_OPERACION") + ";");
				escribir(_ordenes.getValue("CEDULA_RIF") == null ? " ;" : _ordenes.getValue("CEDULA_RIF") + ";");
				escribir(_ordenes.getValue("PRODUCTO") == null ? " ;" : _ordenes.getValue("PRODUCTO") + ";");
				escribir(_ordenes.getValue("FECHA") == null ? " ;" : _ordenes.getValue("FECHA") + ";");
				escribir(_ordenes.getValue("MONTO") == null ? " ;" : _ordenes.getValue("MONTO") + ";");
				escribir(_ordenes.getValue("ID_RECHAZO") == null ? " ;" : _ordenes.getValue("ID_RECHAZO") + ";");
				escribir(_ordenes.getValue("ESTATUS") == null ? " ;" : _ordenes.getValue("ESTATUS") + ";");
				escribir(_ordenes.getValue("ID") == null ? " ;" : _ordenes.getValue("ID") + ";");

				escribir("\r\n");
				
			}
			registrarFin();
			obtenerSalida();
			
		} catch (Exception e) {
			System.out.println("ExportarCVS : execute()" + e);
			Logger.error(this, "ExportarCVS : execute()", e);
		}
	}

	/**
	 * captura las variables de la segunda vista a una 3era vista y se captura con _req
	 * 
	 * @throws Exception
	 */
	public void capturarValoresRecord() {
		
		try {
			this.fecha = (String) _record.getValue("fecha");
			this.statusE = _req.getParameter("estatusEnvio"); // cord.get_record.getValue("estatusEnvio");
			System.out.println("estatusEnvio : " + statusE);
			this.clienteID = Integer.parseInt(_req.getParameter("cliente_id") == null ? "0" : _req.getParameter("cliente_id"));
			
		} catch (Exception e) {
			Logger.error(this, "ExportarCVS : capturarValoresRecord() ", e);
			System.out.println("ExportarCVS : capturarValoresRecord() " + e);

		}
	}

	/**
	 * metodo para crear cabecera
	 * 
	 * @param cabecera
	 * @throws Exception
	 */
	protected void crearCabecera(String cabecera) {
		
		try {
			escribir(cabecera.toUpperCase());
			
		} catch (Exception e) {
			Logger.error(this, "ExportarCVS : crearCabecera() ", e);
			System.out.println("ExportarCVS : crearCabecera() " + e);
			
		}
	}

}