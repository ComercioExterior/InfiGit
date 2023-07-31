package models.custodia.transacciones.entrada_titulos;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.util.helper.Html;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ClienteDAO clien = new ClienteDAO(_dso);
		TitulosDAO titu = new TitulosDAO(_dso);
		EmpresaDefinicionDAO empre = new EmpresaDefinicionDAO(_dso);
		DataSet tipoProducto = new DataSet();
		tipoProducto.append("selectipoproducto", java.sql.Types.VARCHAR);
		tipoProducto.addNew();
		tipoProducto.setValue("selectipoproducto", Html.getSelectTipoProducto(_dso));

		// Realizar consulta
		clien.listarPorId( _record.getValue("client_id"));
		empre.depositarioCentralSi();
		titu.detallesTitulo(_record.getValue("titulo_id"));

		// registrar los datasets exportados por este modelo
		storeDataSet("table", clien.getDataSet());
		storeDataSet("depositario", empre.getDataSet());
		storeDataSet("titulos", titu.getDataSet());
		storeDataSet("tipoProducto", tipoProducto);
	}
}
