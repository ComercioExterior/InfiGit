package models.configuracion.transacciones_fijas;

import megasoft.AbstractModel;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.VehiculoDAO;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del
 * fitro de busqueda
 */
public class Filter extends AbstractModel {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		String nombreTransaccion = "";
		VehiculoDAO vehiculoDAO = new VehiculoDAO(_dso);
		InstrumentoFinancieroDAO insfinanDao = new InstrumentoFinancieroDAO(_dso);
		insfinanDao.listarTodos();
		vehiculoDAO.listarTodos();
		storeDataSet("vehiculos", vehiculoDAO.getDataSet());
		storeDataSet("instrumentos", insfinanDao.getDataSet());
	}

}
