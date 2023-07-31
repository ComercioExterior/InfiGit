package models.custodia.consultas.clientes_titulos_exportar;

import com.bdv.infi.dao.CustodiaDAO;
import models.msc_utilitys.MSCModelExtend;

/**
 * @author elaucho, nvisbal
 */
public class Table extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		CustodiaDAO custodia = new CustodiaDAO(_dso);
		// Se guardan los valores que provienen del record para realizar la busqueda
		long idCliente = 0;
		String tituloId = "";

		if (_record.getValue("client_id") != null)
			idCliente = Long.parseLong(_record.getValue("client_id"));
		if (_record.getValue("titulo_id") != null)
			tituloId = _record.getValue("titulo_id");
		custodia.listarTitulosEnCustodiaExportarExcel(idCliente, tituloId);
		storeDataSet("titulos_custodia", custodia.getDataSet());
		storeDataSet("total", custodia.getTotalRegistros());
		// Se monta el dataset en sesion para luego exportar los datos a excel
		_req.getSession().setAttribute("exportar_excel", custodia.getDataSet());
	}// fin execute
}// fin calse
