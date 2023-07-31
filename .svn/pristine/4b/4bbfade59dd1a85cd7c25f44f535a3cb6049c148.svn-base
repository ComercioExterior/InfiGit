package models.custodia.informes.posicion_global;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.ClienteDAO;

public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		DataSet tipoProducto = new DataSet();
		tipoProducto.append("tipo", java.sql.Types.VARCHAR);
		tipoProducto.addNew();
		tipoProducto.setValue("tipo", com.bdv.infi.util.helper.Html.getSelectTipoProductoTodos(_dso));
		ClienteDAO clien= new ClienteDAO(_dso); 
		clien.fechaHoy();
		storeDataSet("fecha", clien.getDataSet());
		storeDataSet("tipoProducto", tipoProducto);

	}

}
