package models.bcv.menudeo_envio;

import com.bdv.infi.dao.ParametrosDAO;
import com.enterprisedt.util.debug.Logger;

import megasoft.AbstractModel;
import megasoft.db;

/**
 * Clase encargada actualizar en Base de Datos un Valor del parametro.
 * 
 */
public class Update extends AbstractModel {
	private Logger logger = Logger.getLogger(Update.class);

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		try {
			String idOperacion = _record.getValue("operacion");
			String statusOperacion = _record.getValue("estatus");
			String operacion = _record.getValue("operacioncv");
			String nacionalidad = _record.getValue("nacionalidad");
			String rifOcedula = _record.getValue("rif");
			String nombreCliente = _record.getValue("nombre");
			String fecha = _record.getValue("fecha");
			String montoBs = _record.getValue("montobs");
			String montoDivisas = _record.getValue("monto");
			String tasaCambio = _record.getValue("tasacambio");
			String conceptoEstadistico = _record.getValue("conceptoestadistico");
			String codigoDivisas = _record.getValue("moneda");
			String correoCliente = _record.getValue("email");
			String telefonoCliente = _record.getValue("telefono");
			String contraValorUsd = _record.getValue("contravalor");
			
			ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);

			String sql = parametrosDAO.modificarClienteMenudeo(idOperacion, statusOperacion, operacion, nacionalidad, 
					rifOcedula, nombreCliente, fecha, montoBs, montoDivisas, tasaCambio, conceptoEstadistico, 
					codigoDivisas, correoCliente, telefonoCliente, contraValorUsd);

			db.exec(_dso, sql);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Update (Menudeo) : execute()" + e);
			System.out.println("Update (Menudeo) : execute()" + e);
		}

	}
}
