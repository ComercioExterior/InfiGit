package models.custodia.transacciones.bloqueo_titulos;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;

import megasoft.*;

/**
 * Clase encargada de ejecutar la consulta de T&iacute;tulos en custodia de de un cliente en particular que pueden ser bloqueados
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Table extends AbstractModel {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		CustodiaDAO cusD = new CustodiaDAO(_dso);
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		long idCliente = 0;
		// DataSet _dsparam = getDataSetFromRequest();

		if (_record.getValue("client_id") != null){
			idCliente = Long.parseLong(_record.getValue("client_id"));
		}

		// Realizar consulta
		cusD.listarTitulos(idCliente);

		clienteDAO.detallesCliente(String.valueOf(idCliente));

		storeDataSet("datos_cliente", clienteDAO.getDataSet());

		// registrar los datasets exportados por este modelo
		storeDataSet("table", cusD.getDataSet());

		storeDataSet("datos", cusD.getTotalRegistros());
		// storeDataSet("dsparam", _dsparam);

	}

}
