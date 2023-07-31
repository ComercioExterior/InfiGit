package models.custodia.transacciones.salida_externa;

import megasoft.*;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.Transaccion;

/**
 * @author elaucho, nvisbal
 */
public class SalidaExternaProcesar extends Transaccion {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _idClient = (DataSet) _req.getSession().getAttribute("salida_externa-browse.framework.page.record");
		CustodiaDAO custodia = new CustodiaDAO(_dso);
		ClienteDAO cliente = new ClienteDAO(_dso);
		OrdenDAO ordenDao = new OrdenDAO(_dso);
		String clte_transfiere = "";
		String seleccion[] = _req.getParameterValues("seleccion");
		if (_idClient.count() > 0) {
			_idClient.first();
			_idClient.next();
			clte_transfiere = _idClient.getValue("client_id");
		}
		// Se crea el dataset para los titulos involucrados
		DataSet _titulos = new DataSet();
		_titulos.append("titulo_id", java.sql.Types.VARCHAR);
		_titulos.append("tipo_producto", java.sql.Types.VARCHAR);
		_titulos.append("cantidad_disponible", java.sql.Types.VARCHAR);
		_titulos.append("cantidad_transferir", java.sql.Types.VARCHAR);
		_titulos.append("depositario", java.sql.Types.VARCHAR);
		_titulos.append("moneda_id", java.sql.Types.VARCHAR);
		_titulos.append("moneda_descripcion", java.sql.Types.VARCHAR);
		_titulos.append("titulo_moneda_den", java.sql.Types.VARCHAR);

		// Se guarda en el dataset los titulos seleccionados
		for (int i = 0; i < seleccion.length; i++) {
			String[] valores = seleccion[i].split("&&"); // Titulo&&TipoDeProducto
			custodia.listarTitulos(Long.parseLong(clte_transfiere), valores[0], valores[1]);
			if (custodia.getDataSet().next()) {
				_titulos.addNew();
				_titulos.setValue("titulo_id", custodia.getDataSet().getValue("titulo_id"));
				_titulos.setValue("tipo_producto", custodia.getDataSet().getValue("tipo_producto_id"));
				_titulos.setValue("cantidad_disponible", custodia.getDataSet().getValue("cantidad_disponible"));
				_titulos.setValue("titulo_moneda_den", custodia.getDataSet().getValue("titulo_moneda_den"));
				_titulos.setValue("moneda_id", custodia.getDataSet().getValue("titulo_moneda_den"));
			}
		}// Fin del for
		_req.getSession().setAttribute("titulos_seleccionados", _titulos);

		// Se obtienen los ID de los clientes involucrados en la transaccion
		cliente.listarPorId(clte_transfiere);
		DataSet publicar_transfiere = new DataSet();
		publicar_transfiere.append("nombre_transfiere", java.sql.Types.VARCHAR);
		publicar_transfiere.append("cuenta_custodia_transfiere", java.sql.Types.VARCHAR);
		publicar_transfiere.addNew();
		publicar_transfiere.setValue("nombre_transfiere", cliente.getDataSet().getValue("client_nombre"));
		publicar_transfiere.setValue("cuenta_custodia_transfiere", cliente.getDataSet().getValue("client_cta_custod_id"));
		storeDataSet("transfiere", publicar_transfiere);

		// Mostrar la seleccion del depositario
		ordenDao.listarDepositario();
		storeDataSet("depositario", ordenDao.getDataSet());
		storeDataSet("titulos", _titulos);
	}

	public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		if (flag) {
			if (_req.getParameterValues("seleccion") == null) {
				_record.addError("Titulo", "Debe seleccionar un t&iacute;tulo para continuar");
				flag = false;
			}
		}// fin if
		return flag;
	}
}
