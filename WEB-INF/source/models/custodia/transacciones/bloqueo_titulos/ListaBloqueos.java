package models.custodia.transacciones.bloqueo_titulos;

import megasoft.AbstractModel;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TitulosBloqueoDAO;

/**
 * Clase encargada de ejecutar la consulta de T&iacute;tulos en custodia de de un cliente en particular que pueden ser bloqueados
 * @author Erika Valerio, Megasoft Computaci&oacute;n, Nelson Visbal
 */
public class ListaBloqueos extends AbstractModel {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);

		titulosBloqueoDAO.listarTitulosBloqueados(Long.parseLong(_record.getValue("client_id")), _record.getValue("titulo_id"), _record.getValue("tipo_producto"));
		clienteDAO.detallesCliente(String.valueOf(Long.parseLong(_record.getValue("client_id"))));

		storeDataSet("datos_cliente", clienteDAO.getDataSet());

		// registrar los datasets exportados por este modelo
		storeDataSet("table", titulosBloqueoDAO.getDataSet());
		storeDataSet("record", _record);

		storeDataSet("datos", titulosBloqueoDAO.getTotalRegistros());
		// storeDataSet("dsparam", _dsparam);
	}
}
