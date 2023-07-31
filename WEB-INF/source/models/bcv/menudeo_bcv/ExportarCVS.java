package models.bcv.menudeo_bcv;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.data.ClienteMenudeo;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.menudeo.Movimientos;

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
	List<ClienteMenudeo> lstCliente = new ArrayList<ClienteMenudeo>();

	public void execute() {
		try {
			
		this._ordenes = new DataSet();
		capturarValoresRecord();
		Movimientos mvts = new Movimientos();
		mvts.EjecutarMovimiento(fecha);
		Method Fn = Movimientos.class.getMethod("lecturaMovimientoBcv");
		mvts.procesar(mvts, Fn);
		lstCliente = mvts.ListarCliente();
		try {
			registrarInicio("menudeobcv.csv");
			crearCabecera("ID BCV; CEDULA; FECHA; MONEDA;MONTO BASE;MONEDA TRANSACCION;MONTO TRANSACCION; TIPO MOVIMIENTO");
			escribir("\r\n");
			
			for (ClienteMenudeo lst : lstCliente) {

				registroProcesado++;
				escribir(lst.IdOrdenes == null ? " ;" : lst.IdOrdenes + ";");
				escribir(lst.Cedula == null ? " ;" : lst.Cedula + ";");
				escribir(lst.Fecha == null ? " ;" : lst.Fecha + ";");
				escribir(lst.MonedaBase == null ? " ;" : lst.MonedaBase + ";");
				escribir(lst.MontoBase == null ? " ;" : lst.MontoBase + ";");
				escribir(lst.MonedaTransaccion == null ? " ;" : lst.MonedaTransaccion + ";");
				escribir(lst.MontoTransaccion == null ? " ;" : lst.MontoTransaccion + ";");
				escribir(lst.TipoMovimiento == null ? " ;" : lst.TipoMovimiento + ";");
				escribir("\r\n");
			}

		
			registrarFin();
			obtenerSalida();
		} catch (Exception e) {
			_record.addError("Nombre", "Error en la exportación del Excel" + "Error:" + e.getMessage());
			Logger.error(this, "Error en la exportación del Excel", e);
		}
		} catch (Exception e) {
			Logger.error(this, "Error en la exportación del Excel", e);
		}
	}

	/**
	 * captura las variables de la segunda vista a una 3era vista y se captura con _req
	 * 
	 * @throws Exception
	 */
	public void capturarValoresRecord() throws Exception {
		this.statusP = _record.getValue("statusp");
		this.statusE = _record.getValue("statuse");
		this.Tipo = _record.getValue("tipo");
		this.fecha = (String) _record.getValue("fecha");
		this.clienteID = Integer.parseInt(_req.getParameter("cliente_id") == null ? "0" : _req.getParameter("cliente_id"));
	}

	/**
	 * metodo para crear cabecera
	 * 
	 * @param cabecera
	 * @throws Exception
	 */
	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());

	}

}